package com.clevel.selos.model;

public enum MandateConditionType {
    BASE("app.mandateCond.con.type.BASE"),
    CHECK_RESULT("app.mandateCond.con.type.CHECK_RESULT");
    private String desc;

    private MandateConditionType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
