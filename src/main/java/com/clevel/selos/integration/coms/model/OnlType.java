package com.clevel.selos.integration.coms.model;

public enum OnlType {
    LAND("LAND", 1),
    BUILDING("BUILDING", 2),
    MACHINE("MACHINE", 3),
    GOODS("GOODS", 4),
    CAR("CAR", 5),
    SHIP("SHIP", 6),
    RENT("RENT", 7),
    OTHER("OTHER", 8);

    String type;
    int value;

    OnlType(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String type() {
        return this.type;
    }

    public int value() {
        return this.value;
    }

    public static OnlType getValue(String type) {
        if (type != null && !type.trim().equals(""))
            for (OnlType onlType : values()) {
                if (onlType.type.equals(type.trim())) {
                    return onlType;
                }
            }
        return null;
    }

}
