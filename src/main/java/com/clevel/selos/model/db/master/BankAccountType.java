package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_bankaccount_type")
public class BankAccountType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "shortname")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private int active;

    @Column(name = "open_account_flag")
    private int openAccountFlag;

    @Column(name = "bank_statement_flag")
    private int bankStatementFlag;

    public BankAccountType() {
    }

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

    public void setShortName(String shortname) {
        this.shortName = shortname;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("shortname", shortName)
                .append("description", description)
                .append("active", active)
                .append("openAccountFlag", openAccountFlag)
                .append("bankAccountFlag", bankStatementFlag)
                .toString();
    }
}
