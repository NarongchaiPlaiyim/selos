package com.clevel.selos.integration.ncb.nccrs.nccrsmodel;


public enum RegistType {
    CompanyLimited("1140001"),
    LimitedPartnership("1140002"),
    RegisteredOrdinaryPartnership("1140003"),
    PublicCompanyLimited("1140004"),//,"04"),
    ForeignRegistrationIdOrOthers("1140005");

    private String value;

    private RegistType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }


}
