package com.ltceng.serialization.core;

import java.util.Objects;

public class Sequence {
	
    private long id = 0;

    private String sequence;
    
	public Sequence() {
	}
	
	public Sequence(String sequence) {
		this.sequence = sequence;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sequence)) {
            return false;
        }
        final Sequence that = (Sequence) o;
        return Objects.equals(this.id, that.id) && 
        		Objects.equals(this.sequence,  that.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequence);
    }
	
}
