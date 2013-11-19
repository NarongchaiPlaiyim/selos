package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Relation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_relation_customer")
public class RelationCustomer implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_entity_id")
    private CustomerEntity customerEntity;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;
    @Column(name = "spouse")
    private int spouse;
    @OneToOne
    @JoinColumn(name = "relation_id")
    private Relation relation;

    public RelationCustomer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public CustomerEntity getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(CustomerEntity borrowerType) {
        this.borrowerType = borrowerType;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public int getSpouse() {
        return spouse;
    }

    public void setSpouse(int spouse) {
        this.spouse = spouse;
    }
}
