package com.clevel.selos.model;

public enum ActionCode {
    ASSIGN_TO_CHECKER(1001, "Assign to Checker"),
    CHECK_MANDATE_DOC(1002, "Check Mandate Doc"),
    CANCEL_CA(1003, "Cancel CA"),
    CHECK_NCB(1004, "Check NCB"),
    RETURN_TO_BDM_PRESCREEN(1005, "Return to BDM"),
    CHECK_PRESCREEN(1006, "Check Prescreen"),
    REQUEST_APPRAISAL(1007, "Request Appraisal"),
    CLOSE_SALES(1008, "Close Sales"),
    CHECK_CRITERIA(1009, "Check Criteria"),
    ASSIGN_TO_ABDM(1010, "Assign to ABDM"),
    SUBMIT_TO_BDM(1011, "Submit to BDM"),
    REPLY_TO_AAD(1012, "Reply to AAD"),
    PRINT_OP_SHEET(1013, "Print PDF"),
    SUBMIT_CA_BDM(1015, "Submit CA"),
    RETURN_TO_BDM_FULLAPP(1016, "Return to BDM 2"),
    VIEW_RELATE_APP(1017, "View Related Application CA"),
    SUBMIT_TO_UW1(1018, "Submit to UW1"),
    COMPLETE(1019, "Complete"),
    REVISE_CA(1020, "Revise CA"),
    SUBMIT_TO_ADD_COMMITTEE(1021, "Submit to AAD Committee"),
    SUBMIT_TO_UW2(1022, "Submit to UW2"),
    RETURN_TO_AAD_ADMIN(1023, "Return to AAD Admin"),
    REPLY_TO_AAD_ADMIN(1024, "Reply to AAD Admin"),
    CANCEL_APPRAISAL(1025, "Cancel Appraisal request"),
    SUBMIT_TO_ZM(1026, "Submit to ZM"),
    CUSTOMER_ACCEPT(1027, "Assign To Me"),
    REQUEST_PRICE_REDUCE(1028, ""),
    PENDING_FOR_DECISION(1029, "");

    long val;
    String actionName;

    private ActionCode(long val, String actionName){
        this.val = val;
        this.actionName = actionName;
    }

    public long getVal(){
        return this.val;
    }

    public String getActionName(){
        return this.actionName;
    }


}
