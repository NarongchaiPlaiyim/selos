package com.clevel.selos.integration.coms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class HeadCollateralData implements Serializable {
    private String titleDeed;
    private String collateralLocation;
    private String appraisalValue;
    private String headCollType;
    private List<SubCollateralData> subCollateralDataList;

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

    public String getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(String appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public String getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(String headCollType) {
        this.headCollType = headCollType;
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
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("appraisalValue", appraisalValue)
                .append("headCollType", headCollType)
                .append("subCollateralDataList", subCollateralDataList)
                .toString();
    }
}
