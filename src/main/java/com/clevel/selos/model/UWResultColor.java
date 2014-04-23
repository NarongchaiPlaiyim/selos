package com.clevel.selos.model;

public enum UWResultColor {
    RED("R","#FF0000", "result_red"),GREEN("G","#00FF00", "result_green"),YELLOW("Y","#FFCC00", "result_yellow");

    private final String code;
    private final String colorCode;
    private final String resultClass;

    UWResultColor(String code, String colorCode, String resultClass) {
        this.code = code;
        this.colorCode = colorCode;
        this.resultClass = resultClass;
    }

    public String code() {
        return this.code;
    }

    public String colorCode(){
        return this.colorCode;
    }

    public String getResultClass(){
        return resultClass;
    }

    public static final UWResultColor lookup(String value) {
        for (UWResultColor color : UWResultColor.values()) {
            if (color.code.equals(value))
                return color;
        }
        return null;
    }
}
