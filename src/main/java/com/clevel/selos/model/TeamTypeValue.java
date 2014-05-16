package com.clevel.selos.model;

public enum TeamTypeValue {
    TEAM(1),
    TEAM_LEAD(2),
    TEAM_HEAD(3),
    GROUP_HEAD(4),
    CSSO(5);

    int value;

    TeamTypeValue(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }
}
