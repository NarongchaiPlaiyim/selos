package com.clevel.selos.model;

public enum TMBTDRFlag {
    NORMAL("0", false), TDR("1", true), TDR_INPROCESS("2", false);

    private String value;
    private boolean tdrFlag;

    private TMBTDRFlag(String value, boolean tdrFlag){
        this.value = value;
        this.tdrFlag = tdrFlag;
    }

    public static TMBTDRFlag lookup(String value){
        for(TMBTDRFlag tdrFlag : TMBTDRFlag.values()){
            if(tdrFlag.value.equals(value)){
                return tdrFlag;
            }
        }
        return TMBTDRFlag.NORMAL;
    }

    public boolean isTdrFlag(){
        return tdrFlag;
    }
}
