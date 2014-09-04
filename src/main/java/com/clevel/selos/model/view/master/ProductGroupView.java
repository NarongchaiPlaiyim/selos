package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ProductGroupView implements Serializable{

    private int id;
    private String name;
    private String description;
    private String brmsCode;
    private int specialLTV;
    private int active;
    private int addProposeCredit;

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

    public int getSpecialLTV() {
        return specialLTV;
    }

    public void setSpecialLTV(int specialLTV) {
        this.specialLTV = specialLTV;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAddProposeCredit() {
        return addProposeCredit;
    }

    public void setAddProposeCredit(int addProposeCredit) {
        this.addProposeCredit = addProposeCredit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("specialLTV", specialLTV)
                .append("active", active)
                .toString();
    }
}
