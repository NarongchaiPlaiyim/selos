package com.clevel.selos.model;

public enum NCBPaymentCode {
    CODE___N("N", 1),
    CODE_000("000", 2),
    CODE_999("999", 3),
    CODE_XXX("XXX", 4),
    CODE___Y("Y", 5),
    CODE_001("001", 6),
    CODE_002("002", 7),
    CODE_003("003", 8), // >90 day, individual
    CODE_004("004", 9), // >90 day, juristic
    CODE_005("005", 10),
    CODE_006("006", 11),
    CODE_007("007", 12),
    CODE_008("008", 13),
    CODE_009("009", 14),
    CODE_010("010", 15),
    CODE_011("011", 16),
    CODE___F("F", 17);

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
