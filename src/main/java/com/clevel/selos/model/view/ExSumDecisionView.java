package com.clevel.selos.model.view;

import com.clevel.selos.model.UWResultColor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ExSumDecisionView implements Serializable {
    private long id;
    private UWResultColor flag;
    private String group;
    private String ruleName;
    private String cusName;
    private String deviationReason;
    private int uwRuleNameId;
    private long customerId;
    private boolean isCanEdit;

    public ExSumDecisionView() {
        reset();
    }

    public void reset() {
//        this.flag = "";
        this.group = "";
        this.ruleName = "";
        this.cusName = "";
        this.deviationReason = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UWResultColor getFlag() {
        return flag;
    }

    public void setFlag(UWResultColor flag) {
        this.flag = flag;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getDeviationReason() {
        return deviationReason;
    }

    public void setDeviationReason(String deviationReason) {
        this.deviationReason = deviationReason;
    }

    public int getUwRuleNameId() {
        return uwRuleNameId;
    }

    public void setUwRuleNameId(int uwRuleNameId) {
        this.uwRuleNameId = uwRuleNameId;
    }

    public boolean isCanEdit() {
        return isCanEdit;
    }

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("flag", flag).
                append("group", group).
                append("ruleName", ruleName).
                append("cusName", cusName).
                append("deviationReason", deviationReason).
                append("uwRuleNameId", uwRuleNameId).
                append("customerId", customerId).
                append("isCanEdit", isCanEdit).
                toString();
    }
}
