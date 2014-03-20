package com.clevel.selos.model;

public enum StageValue {
    PRESCREEN(101),
    FULL_APPLICATION(201),
    UNDERWRITE_I(202),
    APPRAISAL(203),
    UNDERWRITING_BU_APPROVAL(204),
    CUSTOMER_ACCEPTANCE_PRE_APPROVAL(205),
    UNDERWRITE_II(206),
    PRICE_FEE_REDUCTION(207),
    COLLATERAL_RULES(208),
    CUSTOMER_ACCEPTANCE_FINAL_APPROVAL(301),
    AGREEMENT_PLEDGE_REGISTRATION(302),
    LIMIT_SET_UP(303),
    DISBURSEMENT(304);

    int value;

    StageValue(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
