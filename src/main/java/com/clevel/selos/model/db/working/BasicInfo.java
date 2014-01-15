package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_basicinfo")
public class BasicInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BASIC_INFO_ID", sequenceName = "SEQ_WRK_BASIC_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BASIC_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "is_no_unpaid_fee_insurance", length = 1)
    private int noUnpaidFeeInsurance;

    @Column(name = "is_no_pending_claim_lg", length = 1)
    private int noPendingClaimLG;

    @Column(name = "construct_request_lg")
    private int constructionRequestLG;

    @Column(name = "able_to_get_guarantor")
    private int ableToGettingGuarantorJob;

    @Column(name = "claim_lg_history")
    private int noClaimLGHistory;

    @Column(name = "revoke_license")
    private int noRevokedLicense;

    @Column(name = "late_work_delivery")
    private int noLateWorkDelivery;

    @Column(name = "adequate_capital_resource")
    private int adequateOfCapitalResource;

    @Column(name = "apply_special_program")
    private int applySpecialProgram;

    @OneToOne
    @JoinColumn(name = "specialprogram_id")
    private SpecialProgram specialProgram;

    @Column(name = "refinance_in")
    private int refinanceIN;

    @OneToOne
    @JoinColumn(name = "refinancein_id")
    private Bank refinanceInValue;

    @Column(name = "refinance_out")
    private int refinanceOUT;

    @OneToOne
    @JoinColumn(name = "refinanceout_id")
    private Bank refinanceOutValue;

    @OneToOne
    @JoinColumn(name = "risktype_id")
    private RiskType riskCustomerType;

    @Column(name = "qualitative_type")
    private int qualitativeType;

    @Column(name = "existing_sme_customer")
    private int existingSMECustomer;

    @Column(name = "existing_since")
    private String existingSMECustomerSince;

    @Column(name = "last_review_date")
    private Date lastReviewDate;

    @Column(name = "extended_review_date")
    private Date extendedReviewDate;

    @OneToOne
    @JoinColumn(name = "sbfscore_id")
    private SBFScore sbfScore;

    @Column(name = "request_loan_same_name")
    private int requestLoanWithSameName;

    @Column(name = "loan_in_one_year")
    private int haveLoanInOneYear;

    @Column(name = "pass_annual_review")
    private int passAnnualReview;

    @OneToOne
    @JoinColumn(name = "borrowingtype_id")
    private BorrowingType loanRequestPattern;

    @Column(name = "referral_name")
    private String referralName;

    @Column(name = "referral_id")
    private String referralID;

    @Column(name = "apply_ba")
    private int applyBA;

    @OneToOne
    @JoinColumn(name = "bapaymentmethod_id")
    private BAPaymentMethod baPaymentMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @OneToMany(mappedBy = "basicInfo")
    private List<OpenAccount> openAccountList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public int getNoUnpaidFeeInsurance() {
        return noUnpaidFeeInsurance;
    }

    public void setNoUnpaidFeeInsurance(int noUnpaidFeeInsurance) {
        this.noUnpaidFeeInsurance = noUnpaidFeeInsurance;
    }

    public int getNoPendingClaimLG() {
        return noPendingClaimLG;
    }

    public void setNoPendingClaimLG(int noPendingClaimLG) {
        this.noPendingClaimLG = noPendingClaimLG;
    }

    public int getConstructionRequestLG() {
        return constructionRequestLG;
    }

    public void setConstructionRequestLG(int constructionRequestLG) {
        this.constructionRequestLG = constructionRequestLG;
    }

    public int getAbleToGettingGuarantorJob() {
        return ableToGettingGuarantorJob;
    }

    public void setAbleToGettingGuarantorJob(int ableToGettingGuarantorJob) {
        this.ableToGettingGuarantorJob = ableToGettingGuarantorJob;
    }

    public int getNoClaimLGHistory() {
        return noClaimLGHistory;
    }

    public void setNoClaimLGHistory(int noClaimLGHistory) {
        this.noClaimLGHistory = noClaimLGHistory;
    }

    public int getNoRevokedLicense() {
        return noRevokedLicense;
    }

    public void setNoRevokedLicense(int noRevokedLicense) {
        this.noRevokedLicense = noRevokedLicense;
    }

    public int getNoLateWorkDelivery() {
        return noLateWorkDelivery;
    }

    public void setNoLateWorkDelivery(int noLateWorkDelivery) {
        this.noLateWorkDelivery = noLateWorkDelivery;
    }

    public int getAdequateOfCapitalResource() {
        return adequateOfCapitalResource;
    }

    public void setAdequateOfCapitalResource(int adequateOfCapitalResource) {
        this.adequateOfCapitalResource = adequateOfCapitalResource;
    }

    public int getApplySpecialProgram() {
        return applySpecialProgram;
    }

    public void setApplySpecialProgram(int applySpecialProgram) {
        this.applySpecialProgram = applySpecialProgram;
    }

    public SpecialProgram getSpecialProgram() {
        return specialProgram;
    }

    public void setSpecialProgram(SpecialProgram specialProgram) {
        this.specialProgram = specialProgram;
    }

    public int getRefinanceIN() {
        return refinanceIN;
    }

    public void setRefinanceIN(int refinanceIN) {
        this.refinanceIN = refinanceIN;
    }

    public Bank getRefinanceInValue() {
        return refinanceInValue;
    }

    public void setRefinanceInValue(Bank refinanceInValue) {
        this.refinanceInValue = refinanceInValue;
    }

    public int getRefinanceOUT() {
        return refinanceOUT;
    }

    public void setRefinanceOUT(int refinanceOUT) {
        this.refinanceOUT = refinanceOUT;
    }

    public Bank getRefinanceOutValue() {
        return refinanceOutValue;
    }

    public void setRefinanceOutValue(Bank refinanceOutValue) {
        this.refinanceOutValue = refinanceOutValue;
    }

    public RiskType getRiskCustomerType() {
        return riskCustomerType;
    }

    public void setRiskCustomerType(RiskType riskCustomerType) {
        this.riskCustomerType = riskCustomerType;
    }

    public int getQualitativeType() {
        return qualitativeType;
    }

    public void setQualitativeType(int qualitativeType) {
        this.qualitativeType = qualitativeType;
    }

    public int getExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(int existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public String getExistingSMECustomerSince() {
        return existingSMECustomerSince;
    }

    public void setExistingSMECustomerSince(String existingSMECustomerSince) {
        this.existingSMECustomerSince = existingSMECustomerSince;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public SBFScore getSbfScore() {
        return sbfScore;
    }

    public void setSbfScore(SBFScore sbfScore) {
        this.sbfScore = sbfScore;
    }

    public int getRequestLoanWithSameName() {
        return requestLoanWithSameName;
    }

    public void setRequestLoanWithSameName(int requestLoanWithSameName) {
        this.requestLoanWithSameName = requestLoanWithSameName;
    }

    public int getHaveLoanInOneYear() {
        return haveLoanInOneYear;
    }

    public void setHaveLoanInOneYear(int haveLoanInOneYear) {
        this.haveLoanInOneYear = haveLoanInOneYear;
    }

    public int getPassAnnualReview() {
        return passAnnualReview;
    }

    public void setPassAnnualReview(int passAnnualReview) {
        this.passAnnualReview = passAnnualReview;
    }

    public BorrowingType getLoanRequestPattern() {
        return loanRequestPattern;
    }

    public void setLoanRequestPattern(BorrowingType loanRequestPattern) {
        this.loanRequestPattern = loanRequestPattern;
    }

    public String getReferralName() {
        return referralName;
    }

    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }

    public String getReferralID() {
        return referralID;
    }

    public void setReferralID(String referralID) {
        this.referralID = referralID;
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

    @OrderBy("id ASC")
    public List<OpenAccount> getOpenAccountList() {
        return openAccountList;
    }

    public void setOpenAccountList(List<OpenAccount> openAccountList) {
        this.openAccountList = openAccountList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                append("noUnpaidFeeInsurance", noUnpaidFeeInsurance).
                append("noPendingClaimLG", noPendingClaimLG).
                append("constructionRequestLG", constructionRequestLG).
                append("ableToGettingGuarantorJob", ableToGettingGuarantorJob).
                append("noClaimLGHistory", noClaimLGHistory).
                append("noRevokedLicense", noRevokedLicense).
                append("noLateWorkDelivery", noLateWorkDelivery).
                append("adequateOfCapitalResource", adequateOfCapitalResource).
                append("applySpecialProgram", applySpecialProgram).
                append("specialProgram", specialProgram).
                append("refinanceIN", refinanceIN).
                append("refinanceInValue", refinanceInValue).
                append("refinanceOUT", refinanceOUT).
                append("refinanceOutValue", refinanceOutValue).
                append("riskCustomerType", riskCustomerType).
                append("qualitativeType", qualitativeType).
                append("existingSMECustomer", existingSMECustomer).
                append("existingSMECustomerSince", existingSMECustomerSince).
                append("lastReviewDate", lastReviewDate).
                append("extendedReviewDate", extendedReviewDate).
                append("sbfScore", sbfScore).
                append("requestLoanWithSameName", requestLoanWithSameName).
                append("haveLoanInOneYear", haveLoanInOneYear).
                append("passAnnualReview", passAnnualReview).
                append("loanRequestPattern", loanRequestPattern).
                append("referralName", referralName).
                append("referralID", referralID).
                append("applyBA", applyBA).
                append("baPaymentMethod", baPaymentMethod).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("openAccountList", openAccountList).
                toString();
    }
}
