package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_ba_payment_method")
public class BAPaymentMethod implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description")
    private String description;
    @Column(name = "active")
    private int active;

    public BAPaymentMethod() {
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
