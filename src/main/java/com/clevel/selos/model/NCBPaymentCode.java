package com.clevel.selos.model;

public enum NCBPaymentCode {
    CODE___N("N", 1),
    CODE_000("000", 2),
    CODE___Y("Y", 3),
    CODE_001("001", 4),
    CODE_002("002", 5),
    CODE_003("003", 6),
    CODE_004("004", 7),
    CODE_005("005", 8),
    CODE_006("006", 9),
    CODE_007("007", 10),
    CODE_008("008", 11),
    CODE_009("009", 12),
    CODE_010("010", 13),
    CODE_011("011", 14),
    CODE___F("F", 15),
    CODE_999("999", -1);

    String code;
    int value;

    NCBPaymentCode(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String code() {
        return this.code;
    }

    public int value() {
        return this.value;
    }

    public static NCBPaymentCode getValue(String code) {
        if (code != null && !code.trim().equals(""))
            for (NCBPaymentCode ncbPaymentCode : values()) {
                if (ncbPaymentCode.code.equals(code.trim())) {
                    return ncbPaymentCode;
                }
            }
        return null;
    }

}
