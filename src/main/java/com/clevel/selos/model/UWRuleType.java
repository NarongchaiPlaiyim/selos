package com.clevel.selos.model;

public enum UWRuleType {
    GROUP_LEVEL("Group Result"), CUS_LEVEL("");

    private String type;

    private UWRuleType(String type){
        this.type = type;
    }

    public static final UWRuleType lookup(String value) {
        for (UWRuleType ruleType : UWRuleType.values()) {
            if (ruleType.type == value)
                return ruleType;
        }
        return CUS_LEVEL;
    }

}
