package com.clevel.selos.model;

public enum StatusValue {
    REVIEW_CA(20005),
    REPLY_FROM_BDM(20007),
    REQUEST_CORRECT_DOC_INFO_UW2(20015),
    CANCEL_CA(90001),
    REJECT_UW1(90002),
    CA_APPROVED_UW1(90003),
    REJECT_CA(90004),
    CA_APPROVED_ZM(90005),
    CA_APPROVED_UW2(90006),
    REJECT_UW2(90007)
    ;

    int value;

    StatusValue(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
