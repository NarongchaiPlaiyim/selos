package com.clevel.selos.model;

public enum ProductProgramType {
    TMB_SME_SMARTBIZ("SB", 1),
    SMARTBIZ_FOR_CONSTRUCTION("SBC", 2),
    TOP_UP_BANCASSURANCE("BA", 3),
    F_CASH("FC", 4),
    OD_NO_ASSET("NA", 5),
    RETENTION_PRE_APPROVE("RTP", 6),
    RETENTION_NON_PRE_APPROVE("RTN", 7),
    QUICK_LOAN("QL", 8),
    NON_PRODUCT_PROGRAM("", 9);

    String code;
    int value;

    ProductProgramType(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String code() {
        return this.code;
    }

    public int value() {
        return this.value;
    }

    public static ProductProgramType getValue(String code) {
        if (code != null && !code.trim().equals(""))
            for (ProductProgramType productProgramType : values()) {
                if (productProgramType.code.equals(code.trim())) {
                    return productProgramType;
                }
            }
        return null;
    }
}
