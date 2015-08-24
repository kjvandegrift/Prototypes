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
import oracle.kv.Version;

public class SequenceDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDAO.class);
	private static final String KEY_ALPHA = "alpha";
	private static final String KEY_DIGIT = "digit";
	private static final int NUM_RETRYS = 10;
	private Counter alphaCounter;
	private Counter digitCounter;
	private KVStore store;
	private Version lastKnownAlphaVersion;
	private Version lastKnownDigitVersion;
	private String alphaSequenceNumber;
	private String digitSequenceNumber;
	private Key alphaKey;
	private Key digitKey;

	public SequenceDAO(KVStore store, MetricRegistry metrics) {
		this.store = store;
		this.alphaCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.alpha");
		this.digitCounter = metrics.counter("com.ltceng.serialization.SequenceDAO.incrementCounter.digit");
		alphaKey = Key.createKey(KEY_ALPHA);
		digitKey = Key.createKey(KEY_DIGIT);
	}

	public Sequence initAlphaSequence() {
		alphaSequenceNumber = "AZ";
		lastKnownAlphaVersion = store.put(alphaKey, Value.createValue(alphaSequenceNumber.getBytes()));
		return new Sequence(alphaSequenceNumber);
	}

	public Sequence initDigitSequence() {
		digitSequenceNumber = "99";
		lastKnownDigitVersion = store.put(digitKey, Value.createValue(digitSequenceNumber.getBytes()));
		return new Sequence(digitSequenceNumber);
	}

	public Sequence getNextAlphaSequence() {
		for (int i = 0; i < NUM_RETRYS; i++) {
			alphaSequenceNumber = incrementAlphaSequence(alphaSequenceNumber);
			/* Try to put the next sequence number with the lastKnownVersion. */
			Version newVersion = store.putIfVersion(alphaKey, Value.createValue(alphaSequenceNumber.getBytes()),
					lastKnownAlphaVersion);
			if (newVersion == null) {
				/* Put was unsuccessful get the one in the store. */
				ValueVersion valueVersion = store.get(alphaKey);
				alphaSequenceNumber = new String(valueVersion.getValue().getValue());
				lastKnownAlphaVersion = valueVersion.getVersion();
			} else {
				/* Put was successful. */
				lastKnownAlphaVersion = newVersion;
				return new Sequence(alphaSequenceNumber);
			}
		}
		throw new RuntimeException("Reached maximum number of retries.");
	}

	public Sequence getNextDigitSequence() {
		for (int i = 0; i < NUM_RETRYS; i++) {
			digitSequenceNumber = incrementDigitSequence(digitSequenceNumber);
			/* Try to put the next sequence number with the lastKnownVersion. */
			Version newVersion = store.putIfVersion(digitKey, Value.createValue(digitSequenceNumber.getBytes()),
					lastKnownDigitVersion);
			if (newVersion == null) {
				/* Put was unsuccessful get the one in the store. */
				ValueVersion valueVersion = store.get(digitKey);
				digitSequenceNumber = new String(valueVersion.getValue().getValue());
				lastKnownDigitVersion = valueVersion.getVersion();
			} else {
				/* Put was successful. */
				lastKnownDigitVersion = newVersion;
				return new Sequence(digitSequenceNumber);
			}
		}
		throw new RuntimeException("Reached maximum number of retries.");
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