package com.ltceng.serialization.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.hyperdex.client.Client;
import org.hyperdex.client.HyperDexClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ltceng.serialization.core.Sequence;

public class SequenceDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDAO.class);
	private static final String HYPER_DEX_SPACE = "sequence";
	private static final String HYPER_DEX_SPACE_KEY_ALPHA = "alpha";
	private static final String HYPER_DEX_SPACE_KEY_DIGIT = "digit";
	private static final String HYPER_DEX_ATTR_KEY = "seq_number";
	private static final int TIMEOUT = 60000; // 1 Minute
	private Counter alphaCounter;
	private Counter digitCounter;
	private Client client;

	public SequenceDAO(Client client, MetricRegistry metrics) {
		this.client = client;
		this.alphaCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.alpha");
		this.digitCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.digit"); 
	}

	public Sequence updateSequence(String type) throws TimeoutException {
		Sequence sequence = new Sequence();
		try {
			if (client == null) {
				LOGGER.error("Sequence update failed client is null.");
				return null;
			}
			if (type == null || type.isEmpty() || (!type.equalsIgnoreCase(HYPER_DEX_SPACE_KEY_ALPHA)
					&& !type.equalsIgnoreCase(HYPER_DEX_SPACE_KEY_DIGIT))) {
				LOGGER.error("Sequence update failed type is not valid.");
				return null;
			}
			String next = getNextSequence(type);
			LOGGER.trace("Next Sequence Number: {}", next);
			sequence.setSequence(next);

		} catch (HyperDexClientException e) {
			LOGGER.error("Client get failed.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sequence;
	}

	private String getNextSequence(String type) throws HyperDexClientException, TimeoutException {
		String next = null;
        
        boolean stored = false;
        long startTime = System.currentTimeMillis();
        while (!stored) {
        	Map<String, Object> currentAttrs = client.get(HYPER_DEX_SPACE, type);
        	String current = currentAttrs.get(HYPER_DEX_ATTR_KEY).toString();
        	next = incrementSequence(current, type);
        	Map<String, Object> nextAttrs = new HashMap<String, Object>();
        	nextAttrs.put(HYPER_DEX_ATTR_KEY, next);
        	stored = client.cond_put(HYPER_DEX_SPACE, type, currentAttrs, nextAttrs);
        	if ((System.currentTimeMillis() - startTime) > TIMEOUT) {
        		LOGGER.error("getNextSequence did not complete due to timeout.");
        		throw new TimeoutException();
        	}
        }
        return next;
	}

	private String incrementSequence(String current, String type) {
		String next = null;
		long number;
		if (type.equalsIgnoreCase(HYPER_DEX_SPACE_KEY_ALPHA)) {
			number = toNumber(current);
			next = toAlpha(++number);
			alphaCounter.inc();
		} else {
			number = Long.parseLong(current);
			next = String.valueOf(++number);
			digitCounter.inc();
		}
		return next;
	}

	private long toNumber(String name) {
		long number = 0;
		for (int i = 0; i < name.length(); i++) {
			number = number * 26 + (name.charAt(i) - ('A' - 1));
		}
		return number;
	}

	private String toAlpha(long number) {
		StringBuilder sb = new StringBuilder();
		while (number-- > 0) {
			sb.append((char) ('A' + (number % 26)));
			number /= 26;
		}
		return sb.reverse().toString();
	}

}