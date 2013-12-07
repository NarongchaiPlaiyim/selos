package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_loan_purpose")
public class LoanPurpose implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "brms_code")
    private int brmsCode;

    @Column(name = "active")
    private int active;

    public LoanPurpose(){}

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

    public int getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(int brmsCode) {
        this.brmsCode = brmsCode;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("active", active)
                .toString();
    }
}
