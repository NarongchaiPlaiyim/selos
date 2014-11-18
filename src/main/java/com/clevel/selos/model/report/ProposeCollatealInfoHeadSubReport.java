package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

public class ProposeCollatealInfoHeadSubReport {

    //Coll Head
    private String path;
    private String collateralDescription;
    private String percentLTVDescription;
    private BigDecimal existingCredit;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private String collTypeDescription;
    private String headCollTypeDescription;
    private String insuranceCompany;

    private List<ProposeCollateralInfoSubReport> proposeCollateralInfoSubReports;

    public ProposeCollatealInfoHeadSubReport() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCollateralDescription() {
        return collateralDescription;
    }

    public void setCollateralDescription(String collateralDescription) {
        this.collateralDescription = collateralDescription;
    }

    public String getPercentLTVDescription() {
        return percentLTVDescription;
    }

    public void setPercentLTVDescription(String percentLTVDescription) {
        this.percentLTVDescription = percentLTVDescription;
    }

    public BigDecimal getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.existingCredit = existingCredit;
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

    public String getCollTypeDescription() {
        return collTypeDescription;
    }

    public void setCollTypeDescription(String collTypeDescription) {
        this.collTypeDescription = collTypeDescription;
    }

    public String getHeadCollTypeDescription() {
        return headCollTypeDescription;
    }

    public void setHeadCollTypeDescription(String headCollTypeDescription) {
        this.headCollTypeDescription = headCollTypeDescription;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public List<ProposeCollateralInfoSubReport> getProposeCollateralInfoSubReports() {
        return proposeCollateralInfoSubReports;
    }

    public void setProposeCollateralInfoSubReports(List<ProposeCollateralInfoSubReport> proposeCollateralInfoSubReports) {
        this.proposeCollateralInfoSubReports = proposeCollateralInfoSubReports;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("path", path)
                .append("collateralDescription", collateralDescription)
                .append("percentLTVDescription", percentLTVDescription)
                .append("existingCredit", existingCredit)
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("appraisalValue", appraisalValue)
                .append("collTypeDescription", collTypeDescription)
                .append("headCollTypeDescription", headCollTypeDescription)
                .append("insuranceCompany", insuranceCompany)
                .append("proposeCollateralInfoSubReports", proposeCollateralInfoSubReports)
                .toString();
    }
}
