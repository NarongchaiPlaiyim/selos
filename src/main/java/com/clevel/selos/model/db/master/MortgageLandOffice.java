package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_mortgage_land_office")
public class MortgageLandOffice {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 150)
    private String companyName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active")
    private int active;
}
