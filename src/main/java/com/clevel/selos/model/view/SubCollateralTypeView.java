package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SubCollateralTypeView implements Serializable {
    private int id;
    private String code;
    private String description;
    private CollateralTypeView collateralTypeView;
    private int defaultType;
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

    public CollateralTypeView getCollateralTypeView() {
        return collateralTypeView;
    }

    public void setCollateralTypeView(CollateralTypeView collateralTypeView) {
        this.collateralTypeView = collateralTypeView;
    }

    public int getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(int defaultType) {
        this.defaultType = defaultType;
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
                .append("collateralTypeView", collateralTypeView)
                .append("defaultType", defaultType)
                .append("active", active)
                .toString();
    }
}
