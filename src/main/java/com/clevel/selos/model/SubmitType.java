package com.clevel.selos.model;

public enum SubmitType {
	NA(0),
	SUBMIT(1),
	CANCEL(2),
    RETURN(3);

	private final int value;
	private SubmitType(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
}
