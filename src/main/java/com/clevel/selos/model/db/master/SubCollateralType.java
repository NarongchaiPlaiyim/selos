package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_sub_collateral_type")
public class SubCollateralType implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "description", length = 255)
    private String description;

    @OneToOne
    @JoinColumn(name = "collateral_type_id")
    private CollateralType collateralType;

    @Column(name = "default_type", length = 1, columnDefinition = "int default 0")
    private int defaultType;

    @Column(name = "active")
    private int active;

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

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(int defaultType) {
        this.defaultType = defaultType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("code", code)
                .append("description", description)
                .append("collateralType", collateralType)
                .append("defaultType", defaultType)
                .append("active", active)
                .toString();
    }
}
