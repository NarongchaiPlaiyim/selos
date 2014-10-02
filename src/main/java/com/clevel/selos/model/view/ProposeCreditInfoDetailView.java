package com.clevel.selos.model.view;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposeCreditInfoDetailView implements Serializable {
    private long id;
    private int requestType;
    private int refinance;
    private ProductProgramView productProgramView;
    private CreditTypeView creditTypeView;
    private String productCode;
    private String projectCode;
    private BigDecimal limit;
    private BigDecimal PCEPercent;
    private BigDecimal PCEAmount;
    private boolean reducePriceFlag;
    private boolean reduceFrontEndFee;
    private BigDecimal frontEndFee;
    private BigDecimal frontEndFeeOriginal;
    private LoanPurposeView loanPurposeView;
    private String proposeDetail;
    private DisbursementTypeView disbursementTypeView;
    private BigDecimal holdLimitAmount;

    private BaseRateView standardBaseRate;
    private BigDecimal standardInterest;
    private BaseRateView suggestBaseRate;
    private BigDecimal suggestInterest;

    private int useCount;
    private int seq;
    private ProposeType proposeType;
    private List<Long> deleteCreditTierIdList;
    private List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList;

    private int requestTypeMode; // 1 = Change , 2 = New ( Not have tier ) , 3 = New ( Have tier ( Retrieve Pricing ) )

    private boolean isCannotCheckReducePricing;
    private boolean isCannotCheckReduceFront;

    private BigDecimal installment;

    private DecisionType uwDecision;

    private int lastNo;

    private int setupCompleted;

    //for only show for checkbox credit
    private BigDecimal guaranteeAmount;
    private String accountName;
    private String accountNumber;
    private String accountSuf;
    private boolean noFlag;
    private boolean existingCredit;

    public ProposeCreditInfoDetailView() {
        reset();
    }

    public void reset() {
        this.id = 0;
        this.requestType = 2; //New
        this.refinance = 1; //No
        this.productProgramView = new ProductProgramView();
        this.creditTypeView = new CreditTypeView();
        this.productCode = "";
        this.projectCode = "";
        this.limit = null;
        this.PCEPercent = BigDecimal.ZERO;
        this.PCEAmount = BigDecimal.ZERO;
        this.reducePriceFlag = false;
        this.reduceFrontEndFee = false;
        this.frontEndFee = BigDecimal.ZERO;
        this.loanPurposeView = new LoanPurposeView();
        this.proposeDetail = "";
        this.disbursementTypeView = new DisbursementTypeView();
        this.holdLimitAmount = BigDecimal.ZERO;
        this.useCount = 0;
        this.seq = 0;
        this.deleteCreditTierIdList = new ArrayList<Long>();
        this.proposeCreditInfoTierDetailViewList = new ArrayList<ProposeCreditInfoTierDetailView>();
        this.installment = BigDecimal.ZERO;
        this.uwDecision = DecisionType.NO_DECISION;
        this.guaranteeAmount = BigDecimal.ZERO;
        this.accountName = "";
        this.accountNumber = "";
        this.accountSuf = "";
        this.noFlag = false;
        this.existingCredit = false;
        this.standardBaseRate = new BaseRateView();
        this.standardInterest = BigDecimal.ZERO;
        this.suggestBaseRate = new BaseRateView();
        this.suggestInterest = BigDecimal.ZERO;
        this.requestTypeMode = 1;
        this.isCannotCheckReduceFront = true;
        this.isCannotCheckReducePricing = true;
        this.frontEndFeeOriginal = BigDecimal.ZERO;
        this.lastNo = 0;
        this.setupCompleted = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRefinance() {
        return refinance;
    }

    public void setRefinance(int refinance) {
        this.refinance = refinance;
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

    public LoanPurposeView getLoanPurposeView() {
        return loanPurposeView;
    }

    public void setLoanPurposeView(LoanPurposeView loanPurposeView) {
        this.loanPurposeView = loanPurposeView;
    }

    public String getProposeDetail() {
        return proposeDetail;
    }

    public void setProposeDetail(String proposeDetail) {
        this.proposeDetail = proposeDetail;
    }

    public DisbursementTypeView getDisbursementTypeView() {
        return disbursementTypeView;
    }

    public void setDisbursementTypeView(DisbursementTypeView disbursementTypeView) {
        this.disbursementTypeView = disbursementTypeView;
    }

    public BigDecimal getHoldLimitAmount() {
        return holdLimitAmount;
    }

    public void setHoldLimitAmount(BigDecimal holdLimitAmount) {
        this.holdLimitAmount = holdLimitAmount;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public List<Long> getDeleteCreditTierIdList() {
        return deleteCreditTierIdList;
    }

    public void setDeleteCreditTierIdList(List<Long> deleteCreditTierIdList) {
        this.deleteCreditTierIdList = deleteCreditTierIdList;
    }

    public List<ProposeCreditInfoTierDetailView> getProposeCreditInfoTierDetailViewList() {
        return proposeCreditInfoTierDetailViewList;
    }

    public void setProposeCreditInfoTierDetailViewList(List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList) {
        this.proposeCreditInfoTierDetailViewList = proposeCreditInfoTierDetailViewList;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isNoFlag() {
        return noFlag;
    }

    public void setNoFlag(boolean noFlag) {
        this.noFlag = noFlag;
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

    public boolean isExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(boolean existingCredit) {
        this.existingCredit = existingCredit;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public BaseRateView getStandardBaseRate() {
        return standardBaseRate;
    }

    public void setStandardBaseRate(BaseRateView standardBaseRate) {
        this.standardBaseRate = standardBaseRate;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
    }

    public BaseRateView getSuggestBaseRate() {
        return suggestBaseRate;
    }

    public void setSuggestBaseRate(BaseRateView suggestBaseRate) {
        this.suggestBaseRate = suggestBaseRate;
    }

    public BigDecimal getSuggestInterest() {
        return suggestInterest;
    }

    public void setSuggestInterest(BigDecimal suggestInterest) {
        this.suggestInterest = suggestInterest;
    }

    public int getRequestTypeMode() {
        return requestTypeMode;
    }

    public void setRequestTypeMode(int requestTypeMode) {
        this.requestTypeMode = requestTypeMode;
    }

    public boolean isCannotCheckReduceFront() {
        return isCannotCheckReduceFront;
    }

    public void setCannotCheckReduceFront(boolean cannotCheckReduceFront) {
        isCannotCheckReduceFront = cannotCheckReduceFront;
    }

    public boolean isCannotCheckReducePricing() {
        return isCannotCheckReducePricing;
    }

    public void setCannotCheckReducePricing(boolean cannotCheckReducePricing) {
        isCannotCheckReducePricing = cannotCheckReducePricing;
    }

    public BigDecimal getFrontEndFeeOriginal() {
        return frontEndFeeOriginal;
    }

    public void setFrontEndFeeOriginal(BigDecimal frontEndFeeOriginal) {
        this.frontEndFeeOriginal = frontEndFeeOriginal;
    }

    public int getLastNo() {
        return lastNo;
    }

    public void setLastNo(int lastNo) {
        this.lastNo = lastNo;
    }

    public int getSetupCompleted() {
        return setupCompleted;
    }

    public void setSetupCompleted(int setupCompleted) {
        this.setupCompleted = setupCompleted;
    }
}
