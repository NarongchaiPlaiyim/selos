package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_sattlementstatus")
public class SettlementStatus implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerEntity customerEntity;
    @Column(name = "active")
    private int active;
    @Column(name = "code")
    private String code;

    public SettlementStatus() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
                append("customerEntity", customerEntity).
                append("active", active).
                append("code", code).
                toString();
    }
}
