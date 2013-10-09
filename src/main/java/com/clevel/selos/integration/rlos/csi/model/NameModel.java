package com.clevel.selos.integration.rlos.csi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NameModel implements Serializable {
    private String nameTh;
    private String surnameTh;
    private String nameEn;
    private String surnameEn;

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getSurnameTh() {
        return surnameTh;
    }

    public void setSurnameTh(String surnameTh) {
        this.surnameTh = surnameTh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getSurnameEn() {
        return surnameEn;
    }

    public void setSurnameEn(String surnameEn) {
        this.surnameEn = surnameEn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("nameTh", nameTh)
                .append("surnameTh", surnameTh)
                .append("nameEn", nameEn)
                .append("surnameEn", surnameEn)
                .toString();
    }
}
