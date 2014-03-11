package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.StaticDocument;

import javax.persistence.*;

public class StaticDocCusEntity {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "static_document_id")
    private StaticDocument document;

    @OneToMany
    @JoinColumn(name = "customer_entity_id")
    private CustomerEntity customerEntity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StaticDocument getDocument() {
        return document;
    }

    public void setDocument(StaticDocument document) {
        this.document = document;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
