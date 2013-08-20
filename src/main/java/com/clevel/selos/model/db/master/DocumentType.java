package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "mst_documenttype")
public class DocumentType {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description")
    private String description;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerType customerType;
    @JoinColumn(name="active")
    private int active;

    public DocumentType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                append("description", description).
                append("customerType", customerType).
                append("active", active).
                toString();
    }
}
