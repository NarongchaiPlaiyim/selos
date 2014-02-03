package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
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

    @Column(name = "oth_bank_statement_flag")
    private int othBankStatementFlag;

    @OneToOne
    @JoinColumn(name = "collateral_type_id", nullable = true)
    private CollateralType collateralType;

    @OneToOne
    @JoinColumn(name = "sub_collateral_type_id", nullable = true)
    private SubCollateralType subCollateralType;

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

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public int getOthBankStatementFlag() {
        return othBankStatementFlag;
    }

    public void setOthBankStatementFlag(int othBankStatementFlag) {
        this.othBankStatementFlag = othBankStatementFlag;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("shortName", shortName)
                .append("description", description)
                .append("active", active)
                .append("openAccountFlag", openAccountFlag)
                .append("bankStatementFlag", bankStatementFlag)
                .append("othBankStatementFlag", othBankStatementFlag)
                .toString();
    }
}
