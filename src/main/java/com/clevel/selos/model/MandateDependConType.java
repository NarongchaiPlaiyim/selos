package com.clevel.selos.model;

public enum MandateDependConType {
    INTERNAL("app.mandateCond.depend.con.type.INTERNAL"),
    EXTERNAL("app.mandateCond.depend.con.type.EXTERNAL");
    private String desc;

    private MandateDependConType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
