package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_credit_type")
public class CreditType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "brms_code")
    private String brmsCode;
    @Column(name = "coms_int_type", length = 1)
    private String comsIntType;
    @Column(name = "can_split")
    private int canSplit;
    @Column(name = "active")
    private int active;

    public CreditType() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    public String getComsIntType() {
        return comsIntType;
    }

    public void setComsIntType(String comsIntType) {
        this.comsIntType = comsIntType;
    }

    public int getCanSplit() {
        return canSplit;
    }

    public void setCanSplit(int canSplit) {
        this.canSplit = canSplit;
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
                .append("name", name)
                .append("description", description)
                .append("brmsCode", brmsCode)
                .append("comsIntType", comsIntType)
                .append("canSplit", canSplit)
                .append("active", active)
                .toString();
    }
}
