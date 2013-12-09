package com.clevel.selos.integration.coms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class HeadCollateralData implements Serializable {
    private String collId;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private String headCollType;
    private String subCollType;
    private List<SubCollateralData> subCollateralDataList;

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public String getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(String headCollType) {
        this.headCollType = headCollType;
    }

    public String getSubCollType() {
        return subCollType;
    }

    public void setSubCollType(String subCollType) {
        this.subCollType = subCollType;
    }

    public List<SubCollateralData> getSubCollateralDataList() {
        return subCollateralDataList;
    }

    public void setSubCollateralDataList(List<SubCollateralData> subCollateralDataList) {
        this.subCollateralDataList = subCollateralDataList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("collId", collId)
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("appraisalValue", appraisalValue)
                .append("headCollType", headCollType)
                .append("subCollateralDataList", subCollateralDataList)
                .append("subCollateralDataList", subCollateralDataList)
                .toString();
    }
}
