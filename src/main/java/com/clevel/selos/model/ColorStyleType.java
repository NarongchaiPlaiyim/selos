package com.clevel.selos.model;

public enum ColorStyleType {
    RED("#FF0000"),GREEN("#00FF00"),YELLOW("#FFCC00");

    String code;

    ColorStyleType(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }
}
