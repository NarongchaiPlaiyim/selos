package com.clevel.selos.integration.coms.model;

public enum AddressType {
    TYPE1("APPR_LAND", 1),
    TYPE2("APPR_LAND", 2),
    TYPE3("APPR_LAND", 3),
    TYPE4("APPR_LAND", 4),
    TYPE5("APPR_BUILDING", 5),
    TYPE6("APPR_LAND", 6),
    TYPE7("APPR_MACHINE", 7),
    TYPE8("APPR_GOODS", 7),
    TYPE9("APPR_CAR", 9),
    TYPE10("APPR_SHIP", 10),
    TYPE11("APPR_RENT", 11),
    TYPE12("APPR_OTHER", 12);

    String tableName;
    int value;

    AddressType(String tableName, int value) {
        this.tableName = tableName;
        this.value = value;
    }

    public String tableName() {
        return this.tableName;
    }

    public int value() {
        return this.value;
    }

}
