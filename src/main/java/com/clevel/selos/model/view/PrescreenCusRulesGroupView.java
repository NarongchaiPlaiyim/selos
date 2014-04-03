package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrescreenCusRulesGroupView implements Serializable{
    private UWRuleGroupView uwRuleGroupView = null;
    private int count = 0;
    private int numberOfRuleName = 0;
    private Map<Integer, PrescreenCusRuleNameView> prescreenCusRuleNameViewMap;

    public UWRuleGroupView getUwRuleGroupView() {
        return uwRuleGroupView;
    }

    public void setUwRuleGroupView(UWRuleGroupView uwRuleGroupView) {
        this.uwRuleGroupView = uwRuleGroupView;
    }

    public int getCount() {
        System.out.println("Count +++ " + count);
        return count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNumberOfRuleName() {
        return numberOfRuleName;
    }

    public void setNumberOfRuleName(int numberOfRuleName) {
        this.numberOfRuleName = numberOfRuleName;
    }

    public Map<Integer, PrescreenCusRuleNameView> getPrescreenCusRuleNameViewMap() {
        return prescreenCusRuleNameViewMap;
    }

    public void setPrescreenCusRuleNameViewMap(Map<Integer, PrescreenCusRuleNameView> prescreenCusRuleNameViewMap) {
        this.prescreenCusRuleNameViewMap = prescreenCusRuleNameViewMap;
    }

    public List<Map.Entry<Integer, PrescreenCusRuleNameView>> getCusRuleNameViewMapList(){
        Set<Map.Entry<Integer, PrescreenCusRuleNameView>> entry = prescreenCusRuleNameViewMap.entrySet();
        return new ArrayList<Map.Entry<Integer, PrescreenCusRuleNameView>>(entry);
    }
}
