package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_iddocumenttype")
public class IDDocumentType {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerType customerType;
    @Column(name = "description")
    private String description;
    @Column(name = "active")
    private int active;

    public IDDocumentType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customerType", customerType).
                append("description", description).
                append("active", active).
                toString();
    }
}
