package com.clevel.selos.model;


public enum MandateDependType {
    NO_DEPENDENCY("app.mandateCond.depend.type.NO_DEPENDENCY"),
    DEPEND_TRUE("app.mandateCond.depend.type.DEPENDS_TRUE"),
    DEPEND_FALSE("app.mandateCond.depend.type.DEPEND_FALSE");

    private String desc;

    private MandateDependType(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return desc;
    }
}
