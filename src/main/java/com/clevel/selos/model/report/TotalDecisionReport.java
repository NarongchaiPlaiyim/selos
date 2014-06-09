package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;


public class TotalDecisionReport extends ReportModel{

    //Existing Credit Borrower
    //Commercial Credit
    private BigDecimal extBorrowerTotalComLimit;
    //Retail Credit
    private BigDecimal extBorrowerTotalRetailLimit;
    //App in RLOS Process
    private BigDecimal extBorrowerTotalAppInRLOSLimit;
    //Borrower
    private BigDecimal extBorrowerTotalCommercial;
    private BigDecimal extBorrowerTotalComAndOBOD;
    private BigDecimal extBorrowerTotalExposure;

    //Existing Credit Related Person
    //Commercial Credit
    private BigDecimal extRelatedTotalComLimit;
    //Retail Credit
    private BigDecimal extRelatedTotalRetailLimit;
    //App in RLOS Process
    private BigDecimal extRelatedTotalAppInRLOSLimit;

    //Total Related
    private BigDecimal extRelatedTotalCommercial;
    private BigDecimal extRelatedTotalComAndOBOD;
    private BigDecimal extRelatedTotalExposure;

    //Total Group
    private BigDecimal extGroupTotalCommercial;
    private BigDecimal extGroupTotalComAndOBOD;
    private BigDecimal extGroupTotalExposure;

    //Existing Collateral Borrower
    private BigDecimal extBorrowerTotalAppraisalValue;
    private BigDecimal extBorrowerTotalMortgageValue;

    //Existing Related Person
    private BigDecimal extRelatedTotalAppraisalValue;
    private BigDecimal extRelatedTotalMortgageValue;

    //Existing Guarantor
    //Borrower
    private BigDecimal extTotalGuaranteeAmount;
    //Propose Credit
    private BigDecimal proposeTotalCreditLimit;

    //Approved Propose Credit
    private BigDecimal approveTotalCreditLimit;
    private int creditCusType;
    private String crdRequestTypeName;
    private String countryName;
    private BigDecimal existingSMELimit;
    private BigDecimal maximumSMELimit;

    //Total Borrower
    private BigDecimal approveBrwTotalCommercial;
    private BigDecimal approveBrwTotalComAndOBOD;
    private BigDecimal approveTotalExposure;

    //Proposed Guarantor
    private BigDecimal proposeTotalGuaranteeAmt;

    //Approved Guarantor
    private BigDecimal approveTotalGuaranteeAmt;


    public TotalDecisionReport() {
    }

    public BigDecimal getApproveBrwTotalComAndOBOD() {
        return approveBrwTotalComAndOBOD;
    }

    public void setApproveBrwTotalComAndOBOD(BigDecimal approveBrwTotalComAndOBOD) {
        this.approveBrwTotalComAndOBOD = approveBrwTotalComAndOBOD;
    }

    public BigDecimal getApproveBrwTotalCommercial() {
        return approveBrwTotalCommercial;
    }

    public void setApproveBrwTotalCommercial(BigDecimal approveBrwTotalCommercial) {
        this.approveBrwTotalCommercial = approveBrwTotalCommercial;
    }

    public BigDecimal getApproveTotalCreditLimit() {
        return approveTotalCreditLimit;
    }

    public void setApproveTotalCreditLimit(BigDecimal approveTotalCreditLimit) {
        this.approveTotalCreditLimit = approveTotalCreditLimit;
    }

    public BigDecimal getApproveTotalExposure() {
        return approveTotalExposure;
    }

    public void setApproveTotalExposure(BigDecimal approveTotalExposure) {
        this.approveTotalExposure = approveTotalExposure;
    }

    public BigDecimal getExtBorrowerTotalAppInRLOSLimit() {
        return extBorrowerTotalAppInRLOSLimit;
    }

    public void setExtBorrowerTotalAppInRLOSLimit(BigDecimal extBorrowerTotalAppInRLOSLimit) {
        this.extBorrowerTotalAppInRLOSLimit = extBorrowerTotalAppInRLOSLimit;
    }

    public BigDecimal getExtBorrowerTotalAppraisalValue() {
        return extBorrowerTotalAppraisalValue;
    }

    public void setExtBorrowerTotalAppraisalValue(BigDecimal extBorrowerTotalAppraisalValue) {
        this.extBorrowerTotalAppraisalValue = extBorrowerTotalAppraisalValue;
    }

    public BigDecimal getExtBorrowerTotalComAndOBOD() {
        return extBorrowerTotalComAndOBOD;
    }

    public void setExtBorrowerTotalComAndOBOD(BigDecimal extBorrowerTotalComAndOBOD) {
        this.extBorrowerTotalComAndOBOD = extBorrowerTotalComAndOBOD;
    }

    public BigDecimal getExtBorrowerTotalComLimit() {
        return extBorrowerTotalComLimit;
    }

    public void setExtBorrowerTotalComLimit(BigDecimal extBorrowerTotalComLimit) {
        this.extBorrowerTotalComLimit = extBorrowerTotalComLimit;
    }

    public BigDecimal getExtBorrowerTotalCommercial() {
        return extBorrowerTotalCommercial;
    }

    public void setExtBorrowerTotalCommercial(BigDecimal extBorrowerTotalCommercial) {
        this.extBorrowerTotalCommercial = extBorrowerTotalCommercial;
    }

    public BigDecimal getExtBorrowerTotalExposure() {
        return extBorrowerTotalExposure;
    }

    public void setExtBorrowerTotalExposure(BigDecimal extBorrowerTotalExposure) {
        this.extBorrowerTotalExposure = extBorrowerTotalExposure;
    }

    public BigDecimal getExtBorrowerTotalMortgageValue() {
        return extBorrowerTotalMortgageValue;
    }

    public void setExtBorrowerTotalMortgageValue(BigDecimal extBorrowerTotalMortgageValue) {
        this.extBorrowerTotalMortgageValue = extBorrowerTotalMortgageValue;
    }

    public BigDecimal getExtBorrowerTotalRetailLimit() {
        return extBorrowerTotalRetailLimit;
    }

    public void setExtBorrowerTotalRetailLimit(BigDecimal extBorrowerTotalRetailLimit) {
        this.extBorrowerTotalRetailLimit = extBorrowerTotalRetailLimit;
    }

    public BigDecimal getExtGroupTotalComAndOBOD() {
        return extGroupTotalComAndOBOD;
    }

    public void setExtGroupTotalComAndOBOD(BigDecimal extGroupTotalComAndOBOD) {
        this.extGroupTotalComAndOBOD = extGroupTotalComAndOBOD;
    }

    public BigDecimal getExtGroupTotalCommercial() {
        return extGroupTotalCommercial;
    }

    public void setExtGroupTotalCommercial(BigDecimal extGroupTotalCommercial) {
        this.extGroupTotalCommercial = extGroupTotalCommercial;
    }

    public BigDecimal getExtGroupTotalExposure() {
        return extGroupTotalExposure;
    }

    public void setExtGroupTotalExposure(BigDecimal extGroupTotalExposure) {
        this.extGroupTotalExposure = extGroupTotalExposure;
    }

    public BigDecimal getExtRelatedTotalAppInRLOSLimit() {
        return extRelatedTotalAppInRLOSLimit;
    }

    public void setExtRelatedTotalAppInRLOSLimit(BigDecimal extRelatedTotalAppInRLOSLimit) {
        this.extRelatedTotalAppInRLOSLimit = extRelatedTotalAppInRLOSLimit;
    }

    public BigDecimal getExtRelatedTotalAppraisalValue() {
        return extRelatedTotalAppraisalValue;
    }

    public void setExtRelatedTotalAppraisalValue(BigDecimal extRelatedTotalAppraisalValue) {
        this.extRelatedTotalAppraisalValue = extRelatedTotalAppraisalValue;
    }

    public BigDecimal getExtRelatedTotalComAndOBOD() {
        return extRelatedTotalComAndOBOD;
    }

    public void setExtRelatedTotalComAndOBOD(BigDecimal extRelatedTotalComAndOBOD) {
        this.extRelatedTotalComAndOBOD = extRelatedTotalComAndOBOD;
    }

    public BigDecimal getExtRelatedTotalComLimit() {
        return extRelatedTotalComLimit;
    }

    public void setExtRelatedTotalComLimit(BigDecimal extRelatedTotalComLimit) {
        this.extRelatedTotalComLimit = extRelatedTotalComLimit;
    }

    public BigDecimal getExtRelatedTotalCommercial() {
        return extRelatedTotalCommercial;
    }

    public void setExtRelatedTotalCommercial(BigDecimal extRelatedTotalCommercial) {
        this.extRelatedTotalCommercial = extRelatedTotalCommercial;
    }

    public BigDecimal getExtRelatedTotalExposure() {
        return extRelatedTotalExposure;
    }

    public void setExtRelatedTotalExposure(BigDecimal extRelatedTotalExposure) {
        this.extRelatedTotalExposure = extRelatedTotalExposure;
    }

    public BigDecimal getExtRelatedTotalMortgageValue() {
        return extRelatedTotalMortgageValue;
    }

    public void setExtRelatedTotalMortgageValue(BigDecimal extRelatedTotalMortgageValue) {
        this.extRelatedTotalMortgageValue = extRelatedTotalMortgageValue;
    }

    public BigDecimal getExtRelatedTotalRetailLimit() {
        return extRelatedTotalRetailLimit;
    }

    public void setExtRelatedTotalRetailLimit(BigDecimal extRelatedTotalRetailLimit) {
        this.extRelatedTotalRetailLimit = extRelatedTotalRetailLimit;
    }

    public BigDecimal getExtTotalGuaranteeAmount() {
        return extTotalGuaranteeAmount;
    }

    public void setExtTotalGuaranteeAmount(BigDecimal extTotalGuaranteeAmount) {
        this.extTotalGuaranteeAmount = extTotalGuaranteeAmount;
    }
    public BigDecimal getProposeTotalCreditLimit() {
        return proposeTotalCreditLimit;
    }

    public void setProposeTotalCreditLimit(BigDecimal proposeTotalCreditLimit) {
        this.proposeTotalCreditLimit = proposeTotalCreditLimit;
    }

    public BigDecimal getProposeTotalGuaranteeAmt() {
        return proposeTotalGuaranteeAmt;
    }

    public void setProposeTotalGuaranteeAmt(BigDecimal proposeTotalGuaranteeAmt) {
        this.proposeTotalGuaranteeAmt = proposeTotalGuaranteeAmt;
    }

    public BigDecimal getApproveTotalGuaranteeAmt() {
        return approveTotalGuaranteeAmt;
    }

    public void setApproveTotalGuaranteeAmt(BigDecimal approveTotalGuaranteeAmt) {
        this.approveTotalGuaranteeAmt = approveTotalGuaranteeAmt;
    }

    public int getCreditCusType() {
        return creditCusType;
    }

    public void setCreditCusType(int creditCusType) {
        this.creditCusType = creditCusType;
    }

    public String getCrdRequestTypeName() {
        return crdRequestTypeName;
    }

    public void setCrdRequestTypeName(String crdRequestTypeName) {
        this.crdRequestTypeName = crdRequestTypeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public BigDecimal getExistingSMELimit() {
        return existingSMELimit;
    }

    public void setExistingSMELimit(BigDecimal existingSMELimit) {
        this.existingSMELimit = existingSMELimit;
    }

    public BigDecimal getMaximumSMELimit() {
        return maximumSMELimit;
    }

    public void setMaximumSMELimit(BigDecimal maximumSMELimit) {
        this.maximumSMELimit = maximumSMELimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("approveBrwTotalComAndOBOD", approveBrwTotalComAndOBOD)
                .append("approveBrwTotalCommercial", approveBrwTotalCommercial)
                .append("approveTotalCreditLimit", approveTotalCreditLimit)
                .append("approveTotalExposure", approveTotalExposure)
                .append("approveTotalGuaranteeAmt", approveTotalGuaranteeAmt)
                .append("countryName", countryName)
                .append("crdRequestTypeName", crdRequestTypeName)
                .append("creditCusType", creditCusType)
                .append("existingSMELimit", existingSMELimit)
                .append("extBorrowerTotalAppInRLOSLimit", extBorrowerTotalAppInRLOSLimit)
                .append("extBorrowerTotalAppraisalValue", extBorrowerTotalAppraisalValue)
                .append("extBorrowerTotalComAndOBOD", extBorrowerTotalComAndOBOD)
                .append("extBorrowerTotalComLimit", extBorrowerTotalComLimit)
                .append("extBorrowerTotalCommercial", extBorrowerTotalCommercial)
                .append("extBorrowerTotalExposure", extBorrowerTotalExposure)
                .append("extBorrowerTotalMortgageValue", extBorrowerTotalMortgageValue)
                .append("extBorrowerTotalRetailLimit", extBorrowerTotalRetailLimit)
                .append("extGroupTotalComAndOBOD", extGroupTotalComAndOBOD)
                .append("extGroupTotalCommercial", extGroupTotalCommercial)
                .append("extGroupTotalExposure", extGroupTotalExposure)
                .append("extRelatedTotalAppInRLOSLimit", extRelatedTotalAppInRLOSLimit)
                .append("extRelatedTotalAppraisalValue", extRelatedTotalAppraisalValue)
                .append("extRelatedTotalComAndOBOD", extRelatedTotalComAndOBOD)
                .append("extRelatedTotalComLimit", extRelatedTotalComLimit)
                .append("extRelatedTotalCommercial", extRelatedTotalCommercial)
                .append("extRelatedTotalExposure", extRelatedTotalExposure)
                .append("extRelatedTotalMortgageValue", extRelatedTotalMortgageValue)
                .append("extRelatedTotalRetailLimit", extRelatedTotalRetailLimit)
                .append("extTotalGuaranteeAmount", extTotalGuaranteeAmount)
                .append("maximumSMELimit", maximumSMELimit)
                .append("proposeTotalCreditLimit", proposeTotalCreditLimit)
                .append("proposeTotalGuaranteeAmt", proposeTotalGuaranteeAmt)
                .toString();
    }
}
