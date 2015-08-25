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
	private static final String INIT_ALPHA = "AZ";
	private static final String INIT_DIGIT = "99";
	private static final int MAX_TIMEMOUT_WAIT_MILLIS = 5000;
	private static final long TIMEMOUT_WAIT_MILLIS = 1000;
	private Counter alphaCounter;
	private Counter digitCounter;
	private KVStore store;
	private Key alphaKey;
	private Key digitKey;
	private String initValue;

	public SequenceDAO(KVStore store, MetricRegistry metrics) {
		this.store = store;
		this.alphaCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.alpha");
		this.digitCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.digit");
		alphaKey = Key.createKey(KEY_ALPHA);
		digitKey = Key.createKey(KEY_DIGIT);
	}

	public Sequence initSequence(boolean isAlpha, String initValue) {
		Version v = null;
		int currWaitMillis = 0;
		this.initValue = initValue;
		Key key = (isAlpha) ? alphaKey : digitKey;
		// Keep trying until we're successful or we time out
		while (v == null) {
			Value newVal = Value.createValue(initValue.getBytes());
			try {
				v = store.put(key, newVal);
			} catch (RequestTimeoutException e) {
				try {
					if (currWaitMillis >= MAX_TIMEMOUT_WAIT_MILLIS) {
						throw e;
					}
					Thread.currentThread().wait(TIMEMOUT_WAIT_MILLIS);
					currWaitMillis += TIMEMOUT_WAIT_MILLIS;
				} catch (InterruptedException ie) {
				}
			}
		}
		final ValueVersion valueVersion = store.get(key, Consistency.ABSOLUTE, 0, null);
		return new Sequence(new String(valueVersion.getValue().getValue()));
	}

	/**
	 * Generate and return the next value for the globally unique sequence
	 * number
	 */

	public Sequence getNextSequence(boolean isAlpha) {
		// Get the current value of the sequence number
		ValueVersion currentSequenceNumber = getCurrentSequenceNum(isAlpha);
		Version v = null;
		String nextSequenceNumber = null;
		int currWaitMillis = 0;

		// Keep trying until we're successful or we time out
		while (v == null) {
			String newSequenceNumber = new String(currentSequenceNumber.getValue().getValue());
			nextSequenceNumber = incrementSequence(newSequenceNumber, isAlpha);
			Value newVal = Value.createValue(nextSequenceNumber.getBytes());
			try {
				Key key = (isAlpha) ? alphaKey : digitKey;
				v = store.putIfVersion(key, newVal, currentSequenceNumber.getVersion());
			} catch (RequestTimeoutException e) {
				try {
					if (currWaitMillis >= MAX_TIMEMOUT_WAIT_MILLIS) {
						throw e;
					}
					Thread.currentThread().wait(TIMEMOUT_WAIT_MILLIS);
					currWaitMillis += TIMEMOUT_WAIT_MILLIS;
				} catch (InterruptedException ie) {
				}
			}
			// Someone got in there and incremented the sequence number
			// before we could. We'll have to try again.
			if (v == null) {
				currentSequenceNumber = getCurrentSequenceNum(isAlpha);
			}
		}
		return new Sequence(nextSequenceNumber);
	}

	private ValueVersion getCurrentSequenceNum(boolean isAlpha) {
		// Retrieve the current value of the global sequence number
		Key key = (isAlpha) ? alphaKey : digitKey;
		ValueVersion currentSequenceNumber = store.get(key, Consistency.ABSOLUTE, 0, null);
		int currWaitMillis = 0;

		// No one has created it yet, we'll go and create it now
		if (currentSequenceNumber == null) {
			if (initValue == null) {
				initValue = (isAlpha) ? INIT_ALPHA : INIT_DIGIT;
			}
			Value value = Value.createValue(initValue.getBytes());
			while (currentSequenceNumber == null) {
				try {
					Version seqVersion = store.putIfAbsent(key, value);
					if (seqVersion == null) {
						// Someone got here before us
						currentSequenceNumber = store.get(key, Consistency.ABSOLUTE, 0, null);
					} else {
						/*
						 * We inserted it, wrap it up in ValueVersion class for
						 * return
						 */
						currentSequenceNumber = new ValueVersion(value, seqVersion);
					}
				} catch (RequestTimeoutException e) {
					try {
						if (currWaitMillis >= MAX_TIMEMOUT_WAIT_MILLIS) {
							throw e;
						}
						Thread.currentThread().wait(TIMEMOUT_WAIT_MILLIS);
						currWaitMillis += TIMEMOUT_WAIT_MILLIS;
					} catch (InterruptedException ie) {
					}
				}
			}
		}
		return (currentSequenceNumber);
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