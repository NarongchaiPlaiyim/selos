package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_sattlementstatus")
public class SettlementStatus {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerType customerType;
    @Column(name = "active")
    private int active;

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

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
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
                append("customerType", customerType).
                append("active", active).
                toString();
    }
}
