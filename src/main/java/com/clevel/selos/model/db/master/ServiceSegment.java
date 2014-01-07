package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_service_segment")
public class ServiceSegment {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "code", length = 2)
    private int code;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "existing_sme_flag")
    private int existingSME;
    @Column(name = "non_existing_sme_flag")
    private int nonExistingSME;
    @Column(name = "active", length = 1)
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExistingSME() {
        return existingSME;
    }

    public void setExistingSME(int existingSME) {
        this.existingSME = existingSME;
    }

    public int getNonExistingSME() {
        return nonExistingSME;
    }

    public void setNonExistingSME(int nonExistingSME) {
        this.nonExistingSME = nonExistingSME;
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
                .append("code", code)
                .append("description", description)
                .append("existingSME", existingSME)
                .append("nonExistingSME", nonExistingSME)
                .append("active", active)
                .toString();
    }
}
