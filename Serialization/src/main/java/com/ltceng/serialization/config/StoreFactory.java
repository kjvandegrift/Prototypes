package com.ltceng.serialization.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import oracle.kv.Consistency;
import oracle.kv.Durability;
import oracle.kv.Durability.ReplicaAckPolicy;
import oracle.kv.Durability.SyncPolicy;
import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;

public class StoreFactory {
	private KVStore store;

	@NotEmpty
	@JsonProperty("storeName")
	private String storeName;

	@NotNull
	@JsonProperty("durability")
	private DurabilityPolicy durabilityPolicy;

	@NotNull
	@JsonProperty("consistency")
	private ConsistencyPolicy consistencyPolicy;

	@Min(1)
	@Max(60000)
	@JsonProperty("maxTimeoutWait")
	private int maxTimeoutWait = 5000;

	@Min(1)
	@Max(10000)
	@JsonProperty("attemptTimeoutWait")
	private int attemptTimeoutWait = 500;

	@Valid
	@NotNull
	@JsonProperty("helperNodes")
	private List<Node> helperNodes;

	public List<Node> getHelperNodes() {
		return helperNodes;
	}

	public String getStoreName() {
		return storeName;
	}

	public int getMaxTimeoutWait() {
		return maxTimeoutWait;
	}

	public int getAttemptTimeoutWait() {
		return attemptTimeoutWait;
	}

	public KVStore build(Environment environment) {
		String[] helpers = new String[helperNodes.size()];
		int i = 0;
		for (Node node : helperNodes) {
			helpers[i] = node.getHostName() + ":" + node.getPort();
			i++;
		}
		KVStoreConfig kconfig = new KVStoreConfig(getStoreName(), helpers);
		kconfig.setDurability(getDurability());
		kconfig.setConsistency(getConsistency());
		store = KVStoreFactory.getStore(kconfig);
		environment.lifecycle().manage(new Managed() {
			@Override
			public void start() {
			}

			@Override
			public void stop() {
				store.close();
			}
		});
		return store;
	}

	public Durability getDurability() {
		SyncPolicy masterSyncPolicy = SyncPolicy.valueOf(durabilityPolicy.getMasterSyncPolicy());
		SyncPolicy replicaSyncPolicy = SyncPolicy.valueOf(durabilityPolicy.getReplicaSyncPolicy());
		ReplicaAckPolicy replicaAckPolicy = ReplicaAckPolicy.valueOf(durabilityPolicy.getReplicaAckPolicy());
		return new Durability(masterSyncPolicy, replicaSyncPolicy, replicaAckPolicy);
	}

	public Consistency getConsistency() {
		Consistency consistency = null;
		String type = consistencyPolicy.getType();
		switch (type) {
		case "NONE_REQUIRED_NO_MASTER":
			consistency = Consistency.NONE_REQUIRED_NO_MASTER;
			break;
		case "NONE_REQUIRED":
			consistency = Consistency.NONE_REQUIRED;
			break;
		case "TIME":
			if (consistencyPolicy.getTimeoutUnit() != null) {
				consistency = new Consistency.Time(consistencyPolicy.getPermissibleLag(),
						TimeUnit.valueOf(consistencyPolicy.getPermissibleLagUnit()), consistencyPolicy.getTimeout(),
						TimeUnit.valueOf(consistencyPolicy.getTimeoutUnit()));
			} else {
				throw new IllegalArgumentException("Missing Timeout Unit.");
			}
			break;
		case "ABSOLUTE":
			consistency = Consistency.ABSOLUTE;
			break;
		default:
			throw new IllegalArgumentException("Invalid type: " + type);
		}
		return consistency;
	}

	public long getConsistencyTimeout() {
		return consistencyPolicy.getTimeout();
	}

	public TimeUnit getConsistencyTimeoutUnit() {
		String timeoutUnit = consistencyPolicy.getTimeoutUnit();
		return (timeoutUnit == null) ? null : TimeUnit.valueOf(timeoutUnit);
	}

	public long getDurabilityTimeout() {
		return durabilityPolicy.getTimeout();
	}

	public TimeUnit getDurabilityTimeoutUnit() {
		String timeoutUnit = durabilityPolicy.getTimeoutUnit();
		return (timeoutUnit == null) ? null : TimeUnit.valueOf(timeoutUnit);
	}

}
