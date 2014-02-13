package com.clevel.selos.integration.brms.model.request.data;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AccountTypeLevelBorrower implements Serializable {
    private String dataSource; //todo : to be enum
    private boolean tdrFlag;
    private AttributeTypeLevelBorrower attributeType;
    public AccountTypeLevelBorrower() {
    }

    public AccountTypeLevelBorrower(String dataSource, boolean tdrFlag, AttributeTypeLevelBorrower attributeType) {
        this.dataSource = dataSource;
        this.tdrFlag = tdrFlag;
        this.attributeType = attributeType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(boolean tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public AttributeTypeLevelBorrower getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeTypeLevelBorrower attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("dataSource", dataSource)
                .append("tdrFlag", tdrFlag)
                .append("attributeType", attributeType)
                .toString();
    }
}
