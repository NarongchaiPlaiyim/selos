package com.clevel.selos.model;

public enum Gender {
	NA(0,"app.gender.radio.na") ,
    MALE(1,"app.gender.radio.male"), 
    FEMALE(2,"app.gender.radio.female");
	
    private final int value;
    private final String msgKey;
    
    private Gender(int value,String msgKey) {
        this.value = value;
        this.msgKey = msgKey;
    }

    public int value() {
        return this.value;
    }
    
    public String getMsgKey() {
		return msgKey;
	}
	public static final Gender lookup(int value) {
		for (Gender gender : Gender.values()) {
			if (gender.value == value)
				return gender;
		}
		return NA;
	}
	
	public static final Gender[] displayList() {
		return new Gender[] {
			MALE,FEMALE
		};
	}
}
