package com.clevel.selos.model.view;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCreditDetailView implements Serializable {
    private long id;
    private boolean noFlag;
    private BigDecimal guaranteeAmount;
    private String accountName;
    private String accountNumber;
    private String accountSuf;
    private int useCount;
    private int seq;
    private int requestType;
    private int isSetupCompleted;
    private int isRefinance;
    private ProductProgramView productProgramView;
    private CreditTypeView creditTypeView;
    private String productCode;
    private String projectCode;
    private String borrowerName;
    private BigDecimal limit;
    private BigDecimal PCEPercent;
    private BigDecimal PCEAmount;
    private boolean reducePriceFlag;
    private boolean reduceFrontEndFee;
    private BigDecimal frontEndFee;
    private String remark;
    private BigDecimal holdLimitAmount;
    private BigDecimal finalPrice;
    private BigDecimal tenor;
    private DisbursementTypeView disbursementTypeView;
    private LoanPurposeView loanPurposeView;
    private BigDecimal purpose;
    private DecisionType uwDecision;
    private BigDecimal installment;
    private BigDecimal outstanding;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private ProposeType proposeType;

    public NewCreditDetailView() {
        reset();
    }

    public void reset() {
        this.noFlag = false;
        this.guaranteeAmount = BigDecimal.ZERO;
        this.accountName = "";
        this.accountNumber = "";
        this.accountSuf = "";
        this.useCount = 0;
        this.seq = 0;
        this.requestType = RequestTypes.NEW.value();
        this.isRefinance = RadioValue.NO.value();
        this.productProgramView = new ProductProgramView();
        this.creditTypeView = new CreditTypeView();
        this.disbursementTypeView = new DisbursementTypeView();
        this.loanPurposeView = new LoanPurposeView();
        this.productCode = "";
        this.projectCode = "";
        this.borrowerName = "";
        this.limit = BigDecimal.ZERO;
        this.PCEPercent = BigDecimal.ZERO;
        this.PCEAmount = BigDecimal.ZERO;
        this.installment = BigDecimal.ZERO;
        this.outstanding = BigDecimal.ZERO;
        this.reducePriceFlag = false;
        this.reduceFrontEndFee = false;
        this.frontEndFee = BigDecimal.ZERO;
        this.remark = "";
        this.holdLimitAmount = BigDecimal.ZERO;
        this.finalPrice = BigDecimal.ZERO;
        this.tenor = BigDecimal.ZERO;
        this.purpose = BigDecimal.ZERO;
        this.newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        this.proposeType= ProposeType.P;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRefinance() {
        return isRefinance;
    }

    public void setRefinance(int refinance) {
        isRefinance = refinance;
    }

    public ProductProgramView getProductProgramView() {
        return productProgramView;
    }

    public void setProductProgramView(ProductProgramView productProgramView) {
        this.productProgramView = productProgramView;
    }

    public CreditTypeView getCreditTypeView() {
        return creditTypeView;
    }

    public void setCreditTypeView(CreditTypeView creditTypeView) {
        this.creditTypeView = creditTypeView;
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

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getPCEPercent() {
        return PCEPercent;
    }

    public void setPCEPercent(BigDecimal PCEPercent) {
        this.PCEPercent = PCEPercent;
    }

    public BigDecimal getPCEAmount() {
        return PCEAmount;
    }

    public void setPCEAmount(BigDecimal PCEAmount) {
        this.PCEAmount = PCEAmount;
    }

    public boolean isReducePriceFlag() {
        return reducePriceFlag;
    }

    public void setReducePriceFlag(boolean reducePriceFlag) {
        this.reducePriceFlag = reducePriceFlag;
    }

    public boolean isReduceFrontEndFee() {
        return reduceFrontEndFee;
    }

    public void setReduceFrontEndFee(boolean reduceFrontEndFee) {
        this.reduceFrontEndFee = reduceFrontEndFee;
    }

    public BigDecimal getFrontEndFee() {
        return frontEndFee;
    }

    public void setFrontEndFee(BigDecimal frontEndFee) {
        this.frontEndFee = frontEndFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getHoldLimitAmount() {
        return holdLimitAmount;
    }

    public void setHoldLimitAmount(BigDecimal holdLimitAmount) {
        this.holdLimitAmount = holdLimitAmount;
    }

    public List<NewCreditTierDetailView> getNewCreditTierDetailViewList() {
        return newCreditTierDetailViewList;
    }

    public void setNewCreditTierDetailViewList(List<NewCreditTierDetailView> newCreditTierDetailViewList) {
        this.newCreditTierDetailViewList = newCreditTierDetailViewList;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
    }

    public DisbursementTypeView getDisbursementTypeView() {
        return disbursementTypeView;
    }

    public void setDisbursementTypeView(DisbursementTypeView disbursementTypeView) {
        this.disbursementTypeView = disbursementTypeView;
    }

    public BigDecimal getPurpose() {
        return purpose;
    }

    public void setPurpose(BigDecimal purpose) {
        this.purpose = purpose;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public LoanPurposeView getLoanPurposeView() {
        return loanPurposeView;
    }

    public void setLoanPurposeView(LoanPurposeView loanPurposeView) {
        this.loanPurposeView = loanPurposeView;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public boolean isNoFlag() {
        return noFlag;
    }

    public void setNoFlag(boolean noFlag) {
        this.noFlag = noFlag;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
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

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("noFlag", noFlag)
                .append("guaranteeAmount", guaranteeAmount)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("accountSuf", accountSuf)
                .append("useCount", useCount)
                .append("seq", seq)
                .append("requestType", requestType)
                .append("isRefinance", isRefinance)
                .append("productProgramView", productProgramView)
                .append("creditTypeView", creditTypeView)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("borrowerName", borrowerName)
                .append("limit", limit)
                .append("PCEPercent", PCEPercent)
                .append("PCEAmount", PCEAmount)
                .append("reducePriceFlag", reducePriceFlag)
                .append("reduceFrontEndFee", reduceFrontEndFee)
                .append("frontEndFee", frontEndFee)
                .append("remark", remark)
                .append("holdLimitAmount", holdLimitAmount)
                .append("finalPrice", finalPrice)
                .append("tenor", tenor)
                .append("disbursementTypeView", disbursementTypeView)
                .append("loanPurposeView", loanPurposeView)
                .append("purpose", purpose)
                .append("uwDecision", uwDecision)
                .append("installment", installment)
                .append("outstanding", outstanding)
                .append("newCreditTierDetailViewList", newCreditTierDetailViewList)
                .toString();
    }

	public int getIsSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(int isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}
}
