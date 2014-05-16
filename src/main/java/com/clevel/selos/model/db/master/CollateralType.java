package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_collateral_type")
public class CollateralType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "code", length = 6)
    private String code;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "appraisal_require", columnDefinition = "int default 0")
    private int appraisalRequire;
    @Column(name = "active")
    private int active;

    public CollateralType() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAppraisalRequire() {
        return appraisalRequire;
    }

    public void setAppraisalRequire(int appraisalRequire) {
        this.appraisalRequire = appraisalRequire;
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
                .append("appraisalRequire", appraisalRequire)
                .append("active", active)
                .toString();
    }
}
