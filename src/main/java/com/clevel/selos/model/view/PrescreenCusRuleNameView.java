package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Map;

public class PrescreenCusRuleNameView implements Serializable{
    private UWRuleNameView uwRuleNameView;
    private Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap;

    public UWRuleNameView getUwRuleNameView() {
        return uwRuleNameView;
    }

    public void setUwRuleNameView(UWRuleNameView uwRuleNameView) {
        this.uwRuleNameView = uwRuleNameView;
    }

    public UWRuleResultDetailView getUWRuleDetailView(String key){
        return uwRuleResultDetailViewMap.get(key);
    }

    public Map<String, UWRuleResultDetailView> getUwRuleResultDetailViewMap() {
        return uwRuleResultDetailViewMap;
    }

    public void setUwRuleResultDetailViewMap(Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap) {
        this.uwRuleResultDetailViewMap = uwRuleResultDetailViewMap;
    }
}
