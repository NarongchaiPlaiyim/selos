package com.clevel.selos.model;

public enum NCBPaymentCode {
    CODE___N("N", 1),
    CODE_000("000", 2),
    CODE_na("na", 3),
    CODE_XXX("XXX", 4),
    CODE_999("999", 5),
    CODE___Y("Y", 6),
    CODE_001("001", 7),
    CODE_002("002", 8),
    CODE_003("003", 9), // >90 day, individual
    CODE_004("004", 10), // >90 day, juristic
    CODE_005("005", 11),
    CODE_006("006", 12),
    CODE_007("007", 13),
    CODE_008("008", 14),
    CODE_009("009", 15),
    CODE_010("010", 16),
    CODE_011("011", 17),
    CODE___F("F", 18);

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
