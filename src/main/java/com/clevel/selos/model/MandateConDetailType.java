package com.clevel.selos.model;

public enum MandateConDetailType {
    AND("app.mandateCond.condition.detail.type.AND"),
    OR("app.mandateCond.condition.detail.type.OR");

    private String desc;

    private MandateConDetailType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
