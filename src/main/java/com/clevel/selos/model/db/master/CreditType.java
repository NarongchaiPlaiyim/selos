package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_credit_type")
public class CreditType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "brms_code")
    private String brmsCode;
    @Column(name = "coms_int_type", length = 1)
    private String comsIntType;
    @Column(name = "can_split",columnDefinition="int default 0")
    private int canSplit;
    @Column(name = "cal_limit_type",columnDefinition="int default 1")
    private int calLimitType;
    @Column(name = "credit_group",columnDefinition="int default 10")
    private int creditGroup;
    @Column(name = "contingent_flag", columnDefinition = "int default 1")
    private boolean contingentFlag;
    @Column(name = "WC_INCOME_FLAG",columnDefinition="int default 0")
    private int wcIncomeFlag;
    @Column(name = "active")
    private int active;

    public CreditType() {
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

    public int getWcIncomeFlag() {
        return wcIncomeFlag;
    }

    public void setWcIncomeFlag(int wcIncomeFlag) {
        this.wcIncomeFlag = wcIncomeFlag;
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
                .append("active", active)
                .toString();
    }
}
