package com.ltceng.serialization.db;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ltceng.serialization.core.Sequence;

import oracle.kv.KVStore;
import oracle.kv.Key;
import oracle.kv.Value;
import oracle.kv.ValueVersion;

public class SequenceDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDAO.class);
	private static final String KEY_ALPHA = "alpha";
	private static final String KEY_DIGIT = "digit";
	private Counter alphaCounter;
	private Counter digitCounter;
	private KVStore store;

	public SequenceDAO(KVStore store, MetricRegistry metrics) {
		this.store = store;
		this.alphaCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.alpha");
		this.digitCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.digit");
	}

	public Sequence initSequence(String type) {
		String sequenceValue = "";
		if (type == null || type.isEmpty()
				|| (!type.equalsIgnoreCase(KEY_ALPHA) && !type.equalsIgnoreCase(KEY_DIGIT))) {
			LOGGER.error("Sequence init failed type is not valid.");
		} else if (type.equalsIgnoreCase(KEY_ALPHA)) {
			sequenceValue = "A";
		} else {
			sequenceValue = "1";
		}
		store.put(Key.createKey(type), Value.createValue(sequenceValue.getBytes()));
		return new Sequence(sequenceValue);
	}

	public Sequence updateSequence(String type) throws TimeoutException {
		
		if (type == null || type.isEmpty()
				|| (!type.equalsIgnoreCase(KEY_ALPHA) && !type.equalsIgnoreCase(KEY_DIGIT))) {
			LOGGER.error("Sequence update failed type is not valid.");
			return null;
		}
		String next = getNextSequence(type);
		LOGGER.trace("Next Sequence Number: {}", next);
		return new Sequence(next);
	}

	private String getNextSequence(String type) {
		final ValueVersion valueVersion = store.get(Key.createKey(type));
		String current = new String(valueVersion.getValue().getValue());
		String next = incrementSequence(current, type);
		store.put(Key.createKey(type), Value.createValue(next.getBytes()));
		return next;
	}

	private String incrementSequence(String current, String type) {
		String next = null;
		long number;
		if (type.equalsIgnoreCase(KEY_ALPHA)) {
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