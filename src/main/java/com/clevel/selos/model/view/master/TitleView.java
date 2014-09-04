package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class TitleView implements Serializable {

    private int id;
    private String code;
    private String titleTh;
    private String titleEn;
    private int customerEntityId;
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

    public String getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(String titleTh) {
        this.titleTh = titleTh;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
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
                .append("titleTh", titleTh)
                .append("titleEn", titleEn)
                .append("customerEntityId", customerEntityId)
                .append("active", active)
                .toString();
    }
}
