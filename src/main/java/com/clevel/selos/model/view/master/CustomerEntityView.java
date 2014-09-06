package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Id;

public class CustomerEntityView {

    private int id;
    private String description;
    private int active;
    private String brmsCode;
    private int defaultQualitative;
    private boolean changeQualtiEnable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public int getDefaultQualitative() {
        return defaultQualitative;
    }

    public void setDefaultQualitative(int defaultQualitative) {
        this.defaultQualitative = defaultQualitative;
    }

    public boolean isChangeQualtiEnable() {
        return changeQualtiEnable;
    }

    public void setChangeQualtiEnable(boolean changeQualtiEnable) {
        this.changeQualtiEnable = changeQualtiEnable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("active", active)
                .append("brmsCode", brmsCode)
                .append("defaultQualitative", defaultQualitative)
                .append("changeQualtiEnable", changeQualtiEnable)
                .toString();
    }
}
