package com.clevel.selos.model;

public enum MandateConditionType {
    BASE("app.mandateCond.condition.type.BASE"),
    AND("app.mandateCond.condition.type.AND"),
    OR("app.mandateCond.condition.type.OR");

    private String desc;

    private MandateConditionType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
