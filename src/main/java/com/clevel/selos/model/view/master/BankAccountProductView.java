package com.clevel.selos.model.view.master;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BankAccountProductView implements Serializable {
    private int id;
    private String name;
    private int active;
    private int bankAccountTypeId;
    private int collateralTypeId;
    private int subCollateralTypeId;

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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getBankAccountTypeId() {
        return bankAccountTypeId;
    }

    public void setBankAccountTypeId(int bankAccountTypeId) {
        this.bankAccountTypeId = bankAccountTypeId;
    }

    public int getCollateralTypeId() {
        return collateralTypeId;
    }

    public void setCollateralTypeId(int collateralTypeId) {
        this.collateralTypeId = collateralTypeId;
    }

    public int getSubCollateralTypeId() {
        return subCollateralTypeId;
    }

    public void setSubCollateralTypeId(int subCollateralTypeId) {
        this.subCollateralTypeId = subCollateralTypeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("active", active)
                .append("bankAccountTypeId", bankAccountTypeId)
                .append("collateralTypeId", collateralTypeId)
                .append("subCollateralTypeId", subCollateralTypeId)
                .toString();
    }
}
