package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_tcgcollateral_type")
public class TCGCollateralType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private int active;

    @Column(name = "percentLTV")
    private double percentLTV;

    @Column(name = "tenPercentLTV")
    private double tenPercentLTV;

    @Column(name = "retentionLTV")
    private double retentionLTV;


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

    public double getPercentLTV() {
        return percentLTV;
    }

    public void setPercentLTV(double percentLTV) {
        this.percentLTV = percentLTV;
    }

    public double getTenPercentLTV() {
        return tenPercentLTV;
    }

    public void setTenPercentLTV(double tenPercentLTV) {
        this.tenPercentLTV = tenPercentLTV;
    }

    public double getRetentionLTV() {
        return retentionLTV;
    }

    public void setRetentionLTV(double retentionLTV) {
        this.retentionLTV = retentionLTV;
    }
}
