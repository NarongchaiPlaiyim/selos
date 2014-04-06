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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("flag", flag).
                append("group", group).
                append("ruleName", ruleName).
                append("cusName", cusName).
                append("deviationReason", deviationReason).
                toString();
    }
}
