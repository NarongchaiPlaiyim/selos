package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_title")
public class Title implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "titleTh")
    private String titleTh;
    @Column(name = "titleEn")
    private String titleEn;
    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerType customerType;
    @Column(name = "active")
    private int active;

    public Title() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(String titleTh) {
        this.titleTh = titleTh;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("code", code).
                append("titleTh", titleTh).
                append("titleEn", titleEn).
                append("customerType", customerType).
                append("active", active).
                toString();
    }
}
