package com.clevel.selos.model;

public enum AppraisalStatus {
    NA(0, false), IN_PROCESS(1, false), APPRAISAL_COMPLETED(2, true), CANCEL(3, false);
    private final int value;
    private final boolean complete;

    private AppraisalStatus(int value, boolean complete){
        this.value = value;
        this.complete = complete;
    }

    public int value(){
        return value;
    }

    public boolean booleanValue(){
        return complete;
    }

    public static final AppraisalStatus lookup(int value) {
        for (AppraisalStatus appraisalStatus : AppraisalStatus.values()) {
            if (appraisalStatus.ordinal() == value)
                return appraisalStatus;
        }
        return NA;
    }
}
