package com.clevel.selos.model.view;

import com.clevel.selos.integration.ncb.nccrs.models.response.AccountDisputeModel;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.RiskType;
import com.clevel.selos.model.db.master.SpecialProgram;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BasicInfoView implements Serializable {
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
    private boolean isSpecialProgram;
    private SpecialProgram specialProgram;
    private boolean isRefIn;
//    private Bank refIn;
    private boolean isRefOut;
//    private Bank refOut;
    private RiskType riskType;
    private String qualitative;
    private boolean existingSME;
    private String since;
    private Date lastReviewDate;
    private Date extReviewDate;
//    private xxxxx sbf;
    private boolean isLoan;
    private boolean isMoreOneYear;
    private boolean isAnnual;
//    private xxxxx style;
    private String refName;
    private String refId;
    private List<BasicInfoAccountView> basicInfoAccountViews;
    private boolean isApplyBA;
    private String baPayment;

    public BasicInfoView(){
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

    public boolean isSpecialProgram() {
        return isSpecialProgram;
    }

    public void setSpecialProgram(boolean specialProgram) {
        isSpecialProgram = specialProgram;
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

    public boolean isRefOut() {
        return isRefOut;
    }

    public void setRefOut(boolean refOut) {
        isRefOut = refOut;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public String getQualitative() {
        return qualitative;
    }

    public void setQualitative(String qualitative) {
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
}
