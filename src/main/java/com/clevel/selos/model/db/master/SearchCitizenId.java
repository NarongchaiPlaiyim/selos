package com.clevel.selos.model.db.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wrk_individual")
public class SearchCitizenId implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "citizen_id")
    private String citizenid;

    @Column(name= "customer_id")
    private int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCitizenid() {
        return citizenid;
    }

    public void setCitizenid(String citizenid) {
        this.citizenid = citizenid;
    }


}
