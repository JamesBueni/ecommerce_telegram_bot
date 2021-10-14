package com.fracta.james.domain.entities;

public enum OrderStatus {
	
	WAITING("Waiting"),
	PROCESSED("Processed"),
	COMPLETED("Completed"),
	CANCELED("Canceled");
	
	private final String value;
	
	OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
