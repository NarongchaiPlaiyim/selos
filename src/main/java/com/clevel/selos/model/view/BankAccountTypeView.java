package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BankAccountTypeView implements Serializable {
    private int id;
    private String name;
    private String shortName;
    private int openAccountFlag;
    private int bankStatementFlag;
    private int othBankStatementFlag;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getOpenAccountFlag() {
        return openAccountFlag;
    }

    public void setOpenAccountFlag(int openAccountFlag) {
        this.openAccountFlag = openAccountFlag;
    }

    public int getBankStatementFlag() {
        return bankStatementFlag;
    }

    public void setBankStatementFlag(int bankStatementFlag) {
        this.bankStatementFlag = bankStatementFlag;
    }

    public int getOthBankStatementFlag() {
        return othBankStatementFlag;
    }

    public void setOthBankStatementFlag(int othBankStatementFlag) {
        this.othBankStatementFlag = othBankStatementFlag;
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
                .append("shortName", shortName)
                .append("openAccountFlag", openAccountFlag)
                .append("bankStatementFlag", bankStatementFlag)
                .append("othBankStatementFlag", othBankStatementFlag)
                .append("active", active)
                .toString();
    }
}
