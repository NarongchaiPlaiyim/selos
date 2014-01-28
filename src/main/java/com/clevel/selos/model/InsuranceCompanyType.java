package com.clevel.selos.model;

public enum InsuranceCompanyType {
    NA(0), PARTNER(1), NON_PARTNER(2);

    private int value;

    private InsuranceCompanyType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
