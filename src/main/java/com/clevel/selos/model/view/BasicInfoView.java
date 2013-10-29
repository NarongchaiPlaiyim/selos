package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private int spProgram;
    private SpecialProgram specialProgram;
    private int refIn;
    private Bank refinanceIn;
    private int refOut;
    private Bank refinanceOut;
    private RiskType riskType;
    private int qualitative;
    private int existingSME;
    private String since;
    private Date lastReviewDate;
    private Date extReviewDate;
    private SBFScore sbfScore;
    private int loan;
    private int moreOneYear;
    private int annual;
    private BorrowingType loanRequestPattern;
    private String refName;
    private String refId;
    private List<BasicInfoAccountView> basicInfoAccountViews;
    private int applyBA;
    private BAPaymentMethod baPaymentMethod;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    //for disable
    private int individual;

    public BasicInfoView() {
        reset();
    }

    public void reset() {
        this.requestType = new RequestType();
        this.productGroup = new ProductGroup();
        this.specialProgram = new SpecialProgram();
        this.refinanceIn = new Bank();
        this.refinanceOut = new Bank();
        this.riskType = new RiskType();
        this.sbfScore = new SBFScore();
        this.loanRequestPattern = new BorrowingType();
        this.basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
        this.baPaymentMethod = new BAPaymentMethod();
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

    public int getSpProgram() {
        return spProgram;
    }

    public void setSpProgram(int spProgram) {
        this.spProgram = spProgram;
    }

    public SpecialProgram getSpecialProgram() {
        return specialProgram;
    }

    public void setSpecialProgram(SpecialProgram specialProgram) {
        this.specialProgram = specialProgram;
    }

    public int getRefIn() {
        return refIn;
    }

    public void setRefIn(int refIn) {
        this.refIn = refIn;
    }

    public Bank getRefinanceIn() {
        return refinanceIn;
    }

    public void setRefinanceIn(Bank refinanceIn) {
        this.refinanceIn = refinanceIn;
    }

    public int getRefOut() {
        return refOut;
    }

    public void setRefOut(int refOut) {
        this.refOut = refOut;
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

    public int getExistingSME() {
        return existingSME;
    }

    public void setExistingSME(int existingSME) {
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

    public int getLoan() {
        return loan;
    }

    public void setLoan(int loan) {
        this.loan = loan;
    }

    public int getMoreOneYear() {
        return moreOneYear;
    }

    public void setMoreOneYear(int moreOneYear) {
        this.moreOneYear = moreOneYear;
    }

    public int getAnnual() {
        return annual;
    }

    public void setAnnual(int annual) {
        this.annual = annual;
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

    public int getApplyBA() {
        return applyBA;
    }

    public void setApplyBA(int applyBA) {
        this.applyBA = applyBA;
    }

    public BAPaymentMethod getBaPaymentMethod() {
        return baPaymentMethod;
    }

    public void setBaPaymentMethod(BAPaymentMethod baPaymentMethod) {
        this.baPaymentMethod = baPaymentMethod;
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

    public int getIndividual() {
        return individual;
    }

    public void setIndividual(int individual) {
        this.individual = individual;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("appNo", appNo).
                append("refAppNo", refAppNo).
                append("caNo", caNo).
                append("requestType", requestType).
                append("productGroup", productGroup).
                append("charUnPaid", charUnPaid).
                append("charNoPending", charNoPending).
                append("charFCLG", charFCLG).
                append("charFCIns", charFCIns).
                append("charFCCom", charFCCom).
                append("charFCAba", charFCAba).
                append("charFCLate", charFCLate).
                append("charFCFund", charFCFund).
                append("spProgram", spProgram).
                append("specialProgram", specialProgram).
                append("refIn", refIn).
                append("refinanceIn", refinanceIn).
                append("refOut", refOut).
                append("refinanceOut", refinanceOut).
                append("riskType", riskType).
                append("qualitative", qualitative).
                append("existingSME", existingSME).
                append("since", since).
                append("lastReviewDate", lastReviewDate).
                append("extReviewDate", extReviewDate).
                append("sbfScore", sbfScore).
                append("loan", loan).
                append("moreOneYear", moreOneYear).
                append("annual", annual).
                append("loanRequestPattern", loanRequestPattern).
                append("refName", refName).
                append("refId", refId).
                append("basicInfoAccountViews", basicInfoAccountViews).
                append("applyBA", applyBA).
                append("baPaymentMethod", baPaymentMethod).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("individual", individual).
                toString();
    }
}
