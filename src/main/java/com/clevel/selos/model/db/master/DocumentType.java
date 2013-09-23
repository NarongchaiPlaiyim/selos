package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_documenttype")
public class DocumentType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description")
    private String description;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerEntity customerEntity;
    @JoinColumn(name="active")
    private int active;
    @Column(name="type")
    private String type;


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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("customerEntity", customerEntity)
                .append("active", active)
                .append("type", type)
                .toString();
    }
}
