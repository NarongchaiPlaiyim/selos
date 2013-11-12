package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_customer_account_name")
public class CustomerAccountName implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CUST_ACC_NAME_ID", sequenceName = "SEQ_WRK_CUST_ACC_NAME_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CUST_ACC_NAME_ID")
    long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @Column(name = "name_th")
    String nameTh;

    @Column(name = "surname_th")
    String surnameTh;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "surname_en")
    String surnameEn;

    public CustomerAccountName(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getSurnameTh() {
        return surnameTh;
    }

    public void setSurnameTh(String surnameTh) {
        this.surnameTh = surnameTh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getSurnameEn() {
        return surnameEn;
    }

    public void setSurnameEn(String surnameEn) {
        this.surnameEn = surnameEn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("nameTh", nameTh)
                .append("surnameTh", surnameTh)
                .append("nameEn", nameEn)
                .append("surnameEn", surnameEn)
                .toString();
    }
}
