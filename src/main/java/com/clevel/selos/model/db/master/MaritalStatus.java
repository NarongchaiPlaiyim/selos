package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_marital_status")
public class MaritalStatus implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "active")
    private int active;
    @Column(name = "code")
    private String code;
    @Column(name = "spouse_flag")
    private int spouseFlag;


    public MaritalStatus() {
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSpouseFlag() {
        return spouseFlag;
    }

    public void setSpouseFlag(int spouseFlag) {
        this.spouseFlag = spouseFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("active", active)
                .append("code", code)
                .append("spouseFlag", spouseFlag)
                .toString();
    }
}
