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
    @Column(name = "code",length = 5)
    private String code;
    @Column(name = "title_th",length = 100)
    private String titleTh;
    @Column(name = "title_en",length = 100)
    private String titleEn;
    @OneToOne
    @JoinColumn(name="customerentity_id")
    private CustomerEntity customerEntity;
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

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
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
                append("customerEntity", customerEntity).
                append("active", active).
                toString();
    }
}
