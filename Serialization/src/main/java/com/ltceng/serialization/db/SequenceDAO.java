package com.ltceng.serialization.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.ltceng.serialization.core.Sequence;

import oracle.kv.Consistency;
import oracle.kv.KVStore;
import oracle.kv.Key;
import oracle.kv.RequestTimeoutException;
import oracle.kv.Value;
import oracle.kv.ValueVersion;
import oracle.kv.Version;

public class SequenceDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDAO.class);
	private static final String KEY_ALPHA = "alpha";
	private static final String KEY_DIGIT = "digit";
	private static final String DEFAULT_INIT_ALPHA = "ZZ";
	private static final String DEFAULT_INIT_DIGIT = "99";
	private final int maxTimeoutWait;
	private final int attemptTimeoutWait;
	private final Counter alphaCounter;
	private final Counter digitCounter;
	private final Counter requestTimeouts;
	private final Counter storeSequenceCollision;
	private final KVStore store;
	private final Key alphaKey;
	private final Key digitKey;
	private String initValue;
	

	public SequenceDAO(KVStore store, int maxTimeoutWait, int attemptTimeoutWait, MetricRegistry metrics) {
		this.store = store;
		this.maxTimeoutWait = maxTimeoutWait;
		this.attemptTimeoutWait = attemptTimeoutWait;
		this.alphaCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.counter.alpha");
		this.digitCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.counter.digit");
		this.requestTimeouts = metrics.counter("com.ltceng.serialization.SequenceDAO.counter.requestTimeouts");
		this.storeSequenceCollision = metrics.counter("com.ltceng.serialization.SequenceDAO.counter.storeSequenceCollision");
		alphaKey = Key.createKey(KEY_ALPHA);
		digitKey = Key.createKey(KEY_DIGIT);
	}

	public Sequence initSequence(boolean isAlpha, String initValue) {
		this.initValue = initValue;
		Key key = getKey(isAlpha);
		store.delete(key);
		final ValueVersion valueVersion = getCurrentSequenceNum(isAlpha, key);
		return new Sequence(new String(valueVersion.getValue().getValue()));
	}



	/**
	 * Generate and return the next value for the globally unique sequence
	 * number
	 */

	public Sequence getNextSequence(boolean isAlpha) {
		// Get the current value of the sequence number
		Key key = getKey(isAlpha);
		ValueVersion valueVersionCurrentSequenceNumber = getCurrentSequenceNum(isAlpha, key);
		Version version = null;
		String nextSequenceNumber = null;
		int currWaitMillis = 0;

		// Keep trying until we're successful or we time out
		while (version == null) {
			String sequenceNumber = new String(valueVersionCurrentSequenceNumber.getValue().getValue());
			nextSequenceNumber = incrementSequence(sequenceNumber, isAlpha);
			Value valueNextSequenceNumber = Value.createValue(nextSequenceNumber.getBytes());
			try {
				version = store.putIfVersion(key, valueNextSequenceNumber, valueVersionCurrentSequenceNumber.getVersion());
			} catch (RequestTimeoutException e) {
				currWaitMillis = handleRequestTimeout(currWaitMillis, e);
			}
			// Someone got in there and incremented the sequence number
			// before we could. We'll have to try again.
			if (version == null) {
				storeSequenceCollision.inc();
				LOGGER.trace("Attempted to store incremented sequence {} but it already existed.", nextSequenceNumber);
				valueVersionCurrentSequenceNumber = getCurrentSequenceNum(isAlpha,key);
			}
		}
		return new Sequence(nextSequenceNumber);
	}

	private ValueVersion getCurrentSequenceNum(boolean isAlpha, Key key) {
		// Retrieve the current value of the global sequence number
		ValueVersion currentSequenceNumber = store.get(key, Consistency.ABSOLUTE, 0, null);
		int currWaitMillis = 0;

		// No one has created it yet, we'll go and create it now
		if (currentSequenceNumber == null) {
			if (initValue == null) {
				initValue = (isAlpha) ? DEFAULT_INIT_ALPHA : DEFAULT_INIT_DIGIT;
			}
			Value value = Value.createValue(initValue.getBytes());
			while (currentSequenceNumber == null) {
				try {
					Version seqVersion = store.putIfAbsent(key, value);
					if (seqVersion == null) {
						// Someone got here before us
						currentSequenceNumber = store.get(key, Consistency.ABSOLUTE, 0, null);
						LOGGER.trace("Tried to store init value of {} but it already existed", initValue);
					} else {
						// We inserted it, wrap it up in ValueVersion class for return
						currentSequenceNumber = new ValueVersion(value, seqVersion);
						LOGGER.trace("Init value of {} stored.", initValue);
					}
				} catch (RequestTimeoutException e) {
					currWaitMillis = handleRequestTimeout(currWaitMillis, e);
				}
			}
		}
		return (currentSequenceNumber);
	}

	private int handleRequestTimeout(int currWaitMillis, RequestTimeoutException e) {
		requestTimeouts.inc();
		try {
			if (currWaitMillis >= maxTimeoutWait) {
				LOGGER.debug("MaxTimeoutWait of {} reached: {}", maxTimeoutWait, e.getMessage());
				throw e;
			}
			Thread.currentThread().wait(attemptTimeoutWait);
			currWaitMillis += attemptTimeoutWait;
			LOGGER.debug("Attempt Timeout Occured: {}", e.getMessage());
		} catch (InterruptedException ie) {
			LOGGER.debug("Interruption occured: {}", e.getMessage());
		}
		return currWaitMillis;
	}

	private Key getKey(boolean isAlpha) {
		return (isAlpha) ? alphaKey : digitKey;
	}
	
	private String incrementSequence(String current, boolean isAlpha) {
		if (isAlpha) {
			return incrementAlphaSequence(current);
		} else {
			return incrementDigitSequence(current);
		}
	}

	private String incrementAlphaSequence(String current) {
		String next = null;
		long number;
		number = toNumber(current);
		next = toAlpha(++number);
		alphaCounter.inc();
		return next;
	}

	private String incrementDigitSequence(String current) {
		String next = null;
		long number;
		number = Long.parseLong(current);
		next = String.valueOf(++number);
		digitCounter.inc();
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