package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BankAccountTypeView implements Serializable {
    private int id;
    private String name;
    private String shortname;
    private int openAccountFlag;
    private int bankStatementFlag;
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
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
                .append("shortname", shortname)
                .append("openAccountFlag", openAccountFlag)
                .append("bankStatementFlag", bankStatementFlag)
                .append("active", active)
                .toString();
    }
}
