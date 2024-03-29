package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_account_type")
public class AccountType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "dbr_flag")
    private int dbrFlag;
    @Column(name = "wc_flag")
    private int wcFlag;
    @OneToOne
    @JoinColumn(name = "customerentity_id")
    private CustomerEntity customerEntity;
    @Column(name = "active")
    private int active;
    @Column(name = "ncb_code", length = 10)
    private String ncbCode;
    @Column(name = "month_flag")
    private int monthFlag;
    @Column(name = "calculatetype", length = 1)
    private int calculateType;
    @Column(name = "code", length = 4)
    private String code;

    public AccountType() {
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

    public int getDbrFlag() {
        return dbrFlag;
    }

    public void setDbrFlag(int dbrFlag) {
        this.dbrFlag = dbrFlag;
    }

    public int getWcFlag() {
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getNcbCode() {
        return ncbCode;
    }

    public void setNcbCode(String ncbCode) {
        this.ncbCode = ncbCode;
    }

    public int getMonthFlag() {
        return monthFlag;
    }

    public void setMonthFlag(int monthFlag) {
        this.monthFlag = monthFlag;
    }

    public int getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(int calculateType) {
        this.calculateType = calculateType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("dbrFlag", dbrFlag)
                .append("wcFlag", wcFlag)
                .append("customerEntity", customerEntity)
                .append("active", active)
                .append("ncbCode", ncbCode)
                .append("monthFlag", monthFlag)
                .append("calculateType", calculateType)
                .append("code", code)
                .toString();
    }
}
