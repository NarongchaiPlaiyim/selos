package com.clevel.selos.model;

public enum ActionCode {
    ASSIGN_TO_CHECKER(1001),        CHECK_MANDATE_DOC(1002),        CANCEL_CA_PRESCREEN(1003),
    CHECK_NCB(1004),                RETURN_TO_BDM_PRESCREEN(1005),  CHECK_PRESCREEN(1006),
    REQUEST_APPRAISAL(1007),        CLOSE_SALES(1008),              CHECK_CRITERIA(1009),
    ASSIGN_TO_ABDM(1010),           SUBMIT_TO_BDM(1011),            REPLY_TO_AAD(1012),
    PRINT_OP_SHEET(1013),           CANCEL_CA_FULLAPP(1014),        SUBMIT_CA_BDM(1015),
    RETURN_TO_BDM_FULLAPP(1016),    VIEW_RELATE_APP(1017),          SUBMIT_TO_UW1(1018),
    COMPLETE(1019),                 REVISE_CA(1020),                SUBMIT_TO_ADD_COMMITTEE(1021),
    SUBMIT_TO_UW2(1022),            RETURN_TO_AAD_ADMIN(1023),      REPLY_TO_AAD_ADMIN(1024),
    CANCEL_APPRAISAL(1025),         SUBMIT_TO_ZM(1026),             CUSTOMER_ACCEPT(1027),
    REQUEST_PRICE_REDUCE(1028),     PENDING_FOR_DECISION(1029);

    long value;

    private ActionCode(long value){
        this.value = value;
    }

    public long value(){
        return this.value();
    }
}
