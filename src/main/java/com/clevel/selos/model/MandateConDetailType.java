package com.clevel.selos.model;

public enum MandateConDetailType {
    AND("app.mandateCond.condition.type.AND"),
    OR("app.mandateCond.condition.type.OR");

    private String desc;

    private MandateConDetailType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
