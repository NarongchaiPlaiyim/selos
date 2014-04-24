package com.clevel.selos.model;

public enum StepValue {
    PRESCREEN_INITIAL(1001),
    PRESCREEN_CHECKER(1002),
    PRESCREEN_MAKER(1003),
    FULLAPP_BDM_SSO_ABDM(2001),
    FULLAPP_ZM(2002),
    CREDIT_DECISION_UW1(2003),
    CREDIT_DECISION_RETURN(2004),
    REQUEST_APPRAISAL(2005),
    REVIEW_APPRAISAL_REQUEST(2006),
    APPOINTMENT_APPRAISAL(2007),
    CREDIT_DECISION_UW2(2016);

    int value;

    StepValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
