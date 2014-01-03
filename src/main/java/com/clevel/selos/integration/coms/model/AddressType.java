package com.clevel.selos.integration.coms.model;

public enum AddressType {
    TYPE1("LAND","APPR_LAND", 1),
    TYPE2("BUILDING","APPR_BUILDING", 2),
    TYPE3("MACHINE","APPR_MACHINE", 3),
    TYPE4("GOODS","APPR_GOODS", 4),
    TYPE5("CAR","APPR_CAR", 5),
    TYPE6("SHIP","APPR_SHIP", 6),
    TYPE7("RENT","APPR_RENT", 7),
    TYPE8("OTHER","APPR_OTHER", 8);

    String type;
    String tableName;
    int value;

    AddressType(String type, String tableName, int value) {
        this.type = type;
        this.tableName = tableName;
        this.value = value;
    }

    public String tableName() {
        return this.tableName;
    }

    public String type() {
        return this.type;
    }

    public int value() {
        return this.value;
    }

    public static AddressType getValue(String type) {
        if (type != null && !type.trim().equals(""))
            for (AddressType addressType : values()) {
                if (addressType.type.equals(type.trim())) {
                    return addressType;
                }
            }
        return null;
    }

}
