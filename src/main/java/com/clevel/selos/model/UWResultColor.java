package com.clevel.selos.model;

public enum UWResultColor {
    RED("R","#FF0000"),GREEN("G","#00FF00"),YELLOW("Y","#FFCC00");

    private final String colorCode;
    private final String code;

    UWResultColor(String code, String colorCode) {
        this.code = code;
        this.colorCode = colorCode;
    }

    public String code() {
        return this.code;
    }

    public String colorCode(){
        return this.colorCode;
    }

    public static final UWResultColor lookup(String value) {
        for (UWResultColor color : UWResultColor.values()) {
            if (color.code == value)
                return color;
        }
        return null;
    }
}
