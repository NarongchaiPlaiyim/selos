package com.clevel.selos.model.report;

import com.clevel.selos.model.view.ExistingCreditTierDetailView;
import com.clevel.selos.model.view.ExistingSplitLineDetailView;
import com.clevel.selos.report.ReportModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class BorrowerCreditDecisionReport extends ReportModel{

    private int count;
    private String accountName;
    private String accountNumber;
    private String accountSuf;
    private String description;
    private String productProgramName;
    private String creditTypeName;
    private String productCode;
    private String projectCode;
    private BigDecimal limit;
    private Boolean usePCE;
    private BigDecimal pcePercent;
    private BigDecimal pceLimit;
    private BigDecimal outstanding;
    private BigDecimal finalInterest;

    private List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
    private List<ExistingSplitLineDetailView> existingSplitLineDetailViewList;

    public BorrowerCreditDecisionReport() {
        count = getDefaultInteger();
        accountName = getDefaultString();
        accountNumber = getDefaultString();
        accountSuf = getDefaultString();
        description = getDefaultString();
        productProgramName = getDefaultString();
        creditTypeName = getDefaultString();
        productCode = getDefaultString();
        projectCode = getDefaultString();
        limit = getDefaultBigDecimal();
        usePCE = getDefaultBoolean();
        pcePercent = getDefaultBigDecimal();
        pceLimit = getDefaultBigDecimal();
        outstanding = getDefaultBigDecimal();
        existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();
    }



    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountSuf() {
        return accountSuf;
    }

    public void setAccountSuf(String accountSuf) {
        this.accountSuf = accountSuf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public String getCreditTypeName() {
        return creditTypeName;
    }

    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public Boolean getUsePCE() {
        return usePCE;
    }

    public void setUsePCE(Boolean usePCE) {
        this.usePCE = usePCE;
    }

    public BigDecimal getPcePercent() {
        return pcePercent;
    }

    public void setPcePercent(BigDecimal pcePercent) {
        this.pcePercent = pcePercent;
    }

    public BigDecimal getPceLimit() {
        return pceLimit;
    }

    public void setPceLimit(BigDecimal pceLimit) {
        this.pceLimit = pceLimit;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public List<ExistingCreditTierDetailView> getExistingCreditTierDetailViewList() {
        return existingCreditTierDetailViewList;
    }

    public void setExistingCreditTierDetailViewList(List<ExistingCreditTierDetailView> existingCreditTierDetailViewList) {
        this.existingCreditTierDetailViewList = existingCreditTierDetailViewList;
    }

    public List<ExistingSplitLineDetailView> getExistingSplitLineDetailViewList() {
        return existingSplitLineDetailViewList;
    }

    public void setExistingSplitLineDetailViewList(List<ExistingSplitLineDetailView> existingSplitLineDetailViewList) {
        this.existingSplitLineDetailViewList = existingSplitLineDetailViewList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountName", accountName)
                .append("count", count)
                .append("accountNumber", accountNumber)
                .append("accountSuf", accountSuf)
                .append("description", description)
                .append("productProgramName", productProgramName)
                .append("creditTypeName", creditTypeName)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("limit", limit)
                .append("usePCE", usePCE)
                .append("pcePercent", pcePercent)
                .append("pceLimit", pceLimit)
                .append("outstanding", outstanding)
                .append("existingCreditTierDetailViewList", existingCreditTierDetailViewList)
                .append("existingSplitLineDetailViewList", existingSplitLineDetailViewList)
                .toString();
    }
}
