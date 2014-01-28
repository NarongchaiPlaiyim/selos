package com.clevel.selos.model.db.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_insurance_company")
public class InsuranceCompany {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "active")
    private int active;


}
