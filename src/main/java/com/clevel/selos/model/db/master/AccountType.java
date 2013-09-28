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
    @Column(name = "name")
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("dbrFlag", dbrFlag).
                append("wcFlag", wcFlag).
                append("customerEntity", customerEntity).
                append("active", active).
                toString();
    }
}
