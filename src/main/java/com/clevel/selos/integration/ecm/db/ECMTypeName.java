package com.clevel.selos.integration.ecm.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ECMTypeName implements Serializable {
    private String id;
    private String typeNameTH;//TYPE_NAME_TH
    private String typeNameEN;//TYPE_NAME_EN

    public ECMTypeName() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeNameTH() {
        return typeNameTH;
    }

    public void setTypeNameTH(String typeNameTH) {
        this.typeNameTH = typeNameTH;
    }

    public String getTypeNameEN() {
        return typeNameEN;
    }

    public void setTypeNameEN(String typeNameEN) {
        this.typeNameEN = typeNameEN;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("typeNameTH", typeNameTH)
                .append("typeNameEN", typeNameEN)
                .toString();
    }
}
