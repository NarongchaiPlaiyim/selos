package com.clevel.selos.model.report;

import com.clevel.selos.model.view.NewCreditTierDetailView;
import com.clevel.selos.report.ReportModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ProposedCreditDecisionReport extends ReportModel{

    private int count;
    private String prodName;
    private String credittypeName;
    private String prodCode;
    private String projectCode;
    private BigDecimal limit;
    private String standardPriceLabel;
    private String suggestPriceLabel;
    private String finalPriceLabel;
    private BigDecimal installment;
    private int tenor;
    private BigDecimal frontEndFee;
    private String requestType;
    private String refinance;
    private String purposeDescription;
    private String remark;
    private String disbursement;
    private BigDecimal holdLimitAmount;
    private String path;

    //Approved Credit
    private String uwDecision;

    private List<NewCreditTierDetailView> newCreditTierDetailViews;


    public ProposedCreditDecisionReport() {
        count = getDefaultInteger();
        prodName = getDefaultString();
        credittypeName = getDefaultString();
        prodCode = getProjectCode();
        projectCode = getProjectCode();
        limit = getDefaultBigDecimal();
        standardPriceLabel = getDefaultString();
        suggestPriceLabel = getDefaultString();
        finalPriceLabel = getDefaultString();
        installment = getDefaultBigDecimal();
        tenor = getDefaultInteger();
        frontEndFee = getDefaultBigDecimal();
        requestType = getDefaultString();
        refinance = getDefaultString();
        purposeDescription = getDisbursement();
        remark = getDisbursement();
        disbursement = getDefaultString();
        holdLimitAmount = getDefaultBigDecimal();
        uwDecision = getDefaultString();
        newCreditTierDetailViews = new ArrayList<NewCreditTierDetailView>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCredittypeName() {
        return credittypeName;
    }

    public void setCredittypeName(String credittypeName) {
        this.credittypeName = credittypeName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getStandardPriceLabel() {
        return standardPriceLabel;
    }

    public void setStandardPriceLabel(String standardPriceLabel) {
        this.standardPriceLabel = standardPriceLabel;
    }

    public String getSuggestPriceLabel() {
        return suggestPriceLabel;
    }

    public void setSuggestPriceLabel(String suggestPriceLabel) {
        this.suggestPriceLabel = suggestPriceLabel;
    }

    public String getFinalPriceLabel() {
        return finalPriceLabel;
    }

    public void setFinalPriceLabel(String finalPriceLabel) {
        this.finalPriceLabel = finalPriceLabel;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public BigDecimal getFrontEndFee() {
        return frontEndFee;
    }

    public void setFrontEndFee(BigDecimal frontEndFee) {
        this.frontEndFee = frontEndFee;
    }

    public List<NewCreditTierDetailView> getNewCreditTierDetailViews() {
        return newCreditTierDetailViews;
    }

    public void setNewCreditTierDetailViews(List<NewCreditTierDetailView> newCreditTierDetailViews) {
        this.newCreditTierDetailViews = newCreditTierDetailViews;
    }

    public String getDisbursement() {
        return disbursement;
    }

    public void setDisbursement(String disbursement) {
        this.disbursement = disbursement;
    }

    public BigDecimal getHoldLimitAmount() {
        return holdLimitAmount;
    }

    public void setHoldLimitAmount(BigDecimal holdLimitAmount) {
        this.holdLimitAmount = holdLimitAmount;
    }

    public void setRefinance(String refinance) {
        this.refinance = refinance;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRefinance() {
        return refinance;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getPurposeDescription() {
        return purposeDescription;
    }

    public void setPurposeDescription(String purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(String uwDecision) {
        this.uwDecision = uwDecision;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("prodName", prodName)
                .append("credittypeName", credittypeName)
                .append("prodCode", prodCode)
                .append("projectCode", projectCode)
                .append("limit", limit)
                .append("standardPriceLabel", standardPriceLabel)
                .append("suggestPriceLabel", suggestPriceLabel)
                .append("finalPriceLabel", finalPriceLabel)
                .append("installment", installment)
                .append("tenor", tenor)
                .append("frontEndFee", frontEndFee)
                .append("requestType", requestType)
                .append("refinance", refinance)
                .append("purposeDescription", purposeDescription)
                .append("remark", remark)
                .append("disbursement", disbursement)
                .append("holdLimitAmount", holdLimitAmount)
                .append("newCreditTierDetailViews", newCreditTierDetailViews)
                .append("uwDecision", uwDecision)
                .toString();
    }
}
