package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class UWRuleNameView implements Serializable{

    private int id;
    private String name;
    private String description;
    private String brmsCode;
    private UWRuleGroupView uwRuleGroupView;
    private boolean finalRateFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public UWRuleGroupView getUwRuleGroupView() {
        return uwRuleGroupView;
    }

    public void setUwRuleGroupView(UWRuleGroupView uwRuleGroupView) {
        this.uwRuleGroupView = uwRuleGroupView;
    }

    public boolean isFinalRateFlag() {
        return finalRateFlag;
    }

    public void setFinalRateFlag(boolean finalRateFlag) {
        this.finalRateFlag = finalRateFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("uwRuleGroupView", uwRuleGroupView)
                .append("finalRateFlag", finalRateFlag)
                .toString();
    }
}
