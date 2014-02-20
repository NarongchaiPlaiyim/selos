package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class LoanAccountTypeView implements Serializable {
    private int id;
    private String name;
    private int calculateType;
    private int wcFlag;

    public LoanAccountTypeView() {
        reset();
    }

    public void reset() {
        this.id = 0;
        this.name = "";
        this.calculateType = 0;
        this.wcFlag = 0;
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

    public int getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(int calculateType) {
        this.calculateType = calculateType;
    }

    public int getWcFlag() {
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .toString();
    }
}
