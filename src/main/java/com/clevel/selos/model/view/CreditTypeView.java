package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CreditTypeView implements Serializable {
    private int id;
    private String name;
    private String description;
    private String brmsCode;
    private String comsIntType;
    private int canSplit;
    private int calLimitType;
    private int creditGroup;
    private boolean contingentFlag;
    private int active;

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

    public String getComsIntType() {
        return comsIntType;
    }

    public void setComsIntType(String comsIntType) {
        this.comsIntType = comsIntType;
    }

    public int getCanSplit() {
        return canSplit;
    }

    public void setCanSplit(int canSplit) {
        this.canSplit = canSplit;
    }

    public int getCalLimitType() {
        return calLimitType;
    }

    public void setCalLimitType(int calLimitType) {
        this.calLimitType = calLimitType;
    }

    public int getCreditGroup() {
        return creditGroup;
    }

    public void setCreditGroup(int creditGroup) {
        this.creditGroup = creditGroup;
    }

    public boolean isContingentFlag() {
        return contingentFlag;
    }

    public void setContingentFlag(boolean contingentFlag) {
        this.contingentFlag = contingentFlag;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("comsIntType", comsIntType)
                .append("canSplit", canSplit)
                .append("calLimitType", calLimitType)
                .append("creditGroup", creditGroup)
                .append("contingentFlag", contingentFlag)
                .append("active", active)
                .toString();
    }
}
