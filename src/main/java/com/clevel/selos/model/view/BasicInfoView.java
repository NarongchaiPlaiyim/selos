package com.clevel.selos.model.view;

import com.clevel.selos.integration.ncb.nccrs.models.response.AccountDisputeModel;
import com.clevel.selos.model.db.master.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BasicInfoView implements Serializable {
    private long id;
    private String appNo;
    private String refAppNo;
    private String caNo;
    private RequestType requestType;
    private ProductGroup productGroup;
    private boolean charUnPaid;
    private boolean charNoPending;
    private boolean charFCLG;
    private boolean charFCIns;
    private boolean charFCCom;
    private boolean charFCAba;
    private boolean charFCLate;
    private boolean charFCFund;
    private boolean isSpProgram;
    private SpecialProgram specialProgram;
    private boolean isRefIn;
    private Bank refinanceIn;
    private boolean isRefOut;
    private Bank refinanceOut;
    private RiskType riskType;
    private int qualitative;
    private boolean existingSME;
    private String since;
    private Date lastReviewDate;
    private Date extReviewDate;
    private SBFScore sbfScore;
    private boolean isLoan;
    private boolean isMoreOneYear;
    private boolean isAnnual;
    private BorrowingType loanRequestPattern;
    private String refName;
    private String refId;
    private List<BasicInfoAccountView> basicInfoAccountViews;
    private boolean isApplyBA;
    private String baPayment;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public BasicInfoView(){
        reset();
    }

    public void reset(){
        this.requestType = new RequestType();
        this.productGroup = new ProductGroup();
        this.specialProgram = new SpecialProgram();
        this.refinanceIn = new Bank();
        this.refinanceOut = new Bank();
        this.riskType = new RiskType();
        this.sbfScore = new SBFScore();
        this.loanRequestPattern = new BorrowingType();
        this.basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getRefAppNo() {
        return refAppNo;
    }

    public void setRefAppNo(String refAppNo) {
        this.refAppNo = refAppNo;
    }

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public boolean isCharUnPaid() {
        return charUnPaid;
    }

    public void setCharUnPaid(boolean charUnPaid) {
        this.charUnPaid = charUnPaid;
    }

    public boolean isCharNoPending() {
        return charNoPending;
    }

    public void setCharNoPending(boolean charNoPending) {
        this.charNoPending = charNoPending;
    }

    public boolean isCharFCLG() {
        return charFCLG;
    }

    public void setCharFCLG(boolean charFCLG) {
        this.charFCLG = charFCLG;
    }

    public boolean isCharFCIns() {
        return charFCIns;
    }

    public void setCharFCIns(boolean charFCIns) {
        this.charFCIns = charFCIns;
    }

    public boolean isCharFCCom() {
        return charFCCom;
    }

    public void setCharFCCom(boolean charFCCom) {
        this.charFCCom = charFCCom;
    }

    public boolean isCharFCAba() {
        return charFCAba;
    }

    public void setCharFCAba(boolean charFCAba) {
        this.charFCAba = charFCAba;
    }

    public boolean isCharFCLate() {
        return charFCLate;
    }

    public void setCharFCLate(boolean charFCLate) {
        this.charFCLate = charFCLate;
    }

    public boolean isCharFCFund() {
        return charFCFund;
    }

    public void setCharFCFund(boolean charFCFund) {
        this.charFCFund = charFCFund;
    }

    public boolean isSpProgram() {
        return isSpProgram;
    }

    public void setSpProgram(boolean spProgram) {
        isSpProgram = spProgram;
    }

    public SpecialProgram getSpecialProgram() {
        return specialProgram;
    }

    public void setSpecialProgram(SpecialProgram specialProgram) {
        this.specialProgram = specialProgram;
    }

    public boolean isRefIn() {
        return isRefIn;
    }

    public void setRefIn(boolean refIn) {
        isRefIn = refIn;
    }

    public Bank getRefinanceIn() {
        return refinanceIn;
    }

    public void setRefinanceIn(Bank refinanceIn) {
        this.refinanceIn = refinanceIn;
    }

    public boolean isRefOut() {
        return isRefOut;
    }

    public void setRefOut(boolean refOut) {
        isRefOut = refOut;
    }

    public Bank getRefinanceOut() {
        return refinanceOut;
    }

    public void setRefinanceOut(Bank refinanceOut) {
        this.refinanceOut = refinanceOut;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public int getQualitative() {
        return qualitative;
    }

    public void setQualitative(int qualitative) {
        this.qualitative = qualitative;
    }

    public boolean isExistingSME() {
        return existingSME;
    }

    public void setExistingSME(boolean existingSME) {
        this.existingSME = existingSME;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getExtReviewDate() {
        return extReviewDate;
    }

    public void setExtReviewDate(Date extReviewDate) {
        this.extReviewDate = extReviewDate;
    }

    public SBFScore getSbfScore() {
        return sbfScore;
    }

    public void setSbfScore(SBFScore sbfScore) {
        this.sbfScore = sbfScore;
    }

    public boolean isLoan() {
        return isLoan;
    }

    public void setLoan(boolean loan) {
        isLoan = loan;
    }

    public boolean isMoreOneYear() {
        return isMoreOneYear;
    }

    public void setMoreOneYear(boolean moreOneYear) {
        isMoreOneYear = moreOneYear;
    }

    public boolean isAnnual() {
        return isAnnual;
    }

    public void setAnnual(boolean annual) {
        isAnnual = annual;
    }

    public BorrowingType getLoanRequestPattern() {
        return loanRequestPattern;
    }

    public void setLoanRequestPattern(BorrowingType loanRequestPattern) {
        this.loanRequestPattern = loanRequestPattern;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public List<BasicInfoAccountView> getBasicInfoAccountViews() {
        return basicInfoAccountViews;
    }

    public void setBasicInfoAccountViews(List<BasicInfoAccountView> basicInfoAccountViews) {
        this.basicInfoAccountViews = basicInfoAccountViews;
    }

    public boolean isApplyBA() {
        return isApplyBA;
    }

    public void setApplyBA(boolean applyBA) {
        isApplyBA = applyBA;
    }

    public String getBaPayment() {
        return baPayment;
    }

    public void setBaPayment(String baPayment) {
        this.baPayment = baPayment;
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
}
