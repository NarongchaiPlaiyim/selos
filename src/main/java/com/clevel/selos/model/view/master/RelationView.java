package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class RelationView implements Serializable {

    private int id;
    private String description;
    private int priority;
    private int active;
    private String brmsCode;
    private boolean canBePOA;
    private boolean canBeAttorney;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public boolean isCanBePOA() {
        return canBePOA;
    }

    public void setCanBePOA(boolean canBePOA) {
        this.canBePOA = canBePOA;
    }

    public boolean isCanBeAttorney() {
        return canBeAttorney;
    }

    public void setCanBeAttorney(boolean canBeAttorney) {
        this.canBeAttorney = canBeAttorney;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("priority", priority)
                .append("active", active)
                .append("brmsCode", brmsCode)
                .append("canBePOA", canBePOA)
                .append("canBeAttorney", canBeAttorney)
                .toString();
    }
}
