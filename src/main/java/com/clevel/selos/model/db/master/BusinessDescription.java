package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "mst_businessdescription")
public class BusinessDescription implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "tmbCode")
    private String tmbCode;
    @Column(name = "incomeFactor")
    private BigDecimal incomeFactor;
    @Column(name = "isicCode")
    private String isicCode;
    @Column(name = "ar")
    private int ar;
    @Column(name = "ap")
    private int ap;
    @Column(name = "inv")
    private int inv;
    @OneToOne
    @JoinColumn(name="businessgroup_id")
    private BusinessGroup businessGroup;
    @Column(name = "active")
    private int active;

    public BusinessDescription() {
    }

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

    public String getTmbCode() {
        return tmbCode;
    }

    public void setTmbCode(String tmbCode) {
        this.tmbCode = tmbCode;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public String getIsicCode() {
        return isicCode;
    }

    public void setIsicCode(String isicCode) {
        this.isicCode = isicCode;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getInv() {
        return inv;
    }

    public void setInv(int inv) {
        this.inv = inv;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
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
                append("name", name).
                append("tmbCode", tmbCode).
                append("incomeFactor", incomeFactor).
                append("isicCode", isicCode).
                append("ar", ar).
                append("ap", ap).
                append("inv", inv).
                append("businessGroup", businessGroup).
                append("active", active).
                toString();
    }
}
