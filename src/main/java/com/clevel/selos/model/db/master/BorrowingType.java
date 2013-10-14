package com.clevel.selos.model.db.master;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_borrowing_type")
public class BorrowingType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private int code;
    @Column(name = "name",length = 50)
    private String name;
    @Column(name = "active")
    private int active;
    @OneToOne
    @JoinColumn(name="customer_entity_id")
    private CustomerEntity customerEntity;

    public BorrowingType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
