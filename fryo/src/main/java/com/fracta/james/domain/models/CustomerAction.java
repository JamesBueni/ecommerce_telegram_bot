package com.fracta.james.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class CustomerAction {
	
	private final LocalDateTime creationDateTime;
	private final String action;
	
	public CustomerAction(LocalDateTime creationDateTime, String action) {
		this.creationDateTime = creationDateTime;
		this.action = action;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public String getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, creationDateTime);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (CustomerAction) o;
		return Objects.equals(creationDateTime, other.creationDateTime)
				&& Objects.equals(action, other.action);
	}

	@Override
	public String toString() {
		return "CustomerAction{creationDateTime=" + creationDateTime
				+ ", action=" + action
				+ "}";
	}
	
	
	

}
