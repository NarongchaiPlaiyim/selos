package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="wrk_basicinfo")
public class BasicInfo implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BASIC_INFO_ID", sequenceName="SEQ_WRK_BASIC_INFO_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BASIC_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name="request_type_id")
    private RequestType requestType;

    @OneToOne
    @JoinColumn(name="product_group_id")
    private ProductGroup productGroup;

    @Column(name="is_unpaind_fee_insurance")
    private boolean unpaidFeeInsurance;

    @Column(name="is_no_pending_claim_lg")
    private boolean noPendingClaimLG;

    @Column(name="is_contruct_request_lg")
    private boolean isConstructionRequestLG;

    @Column(name="is_able_get_guarantor")
    private boolean isAbleToGettingGuarantorJob;

    @Column(name="is_claim_lg_history")
    private boolean noClaimLGHistory;

    @Column(name="is_revoke_license")
    private boolean noRevokedLicense;

    @Column(name="is_late_work_delivery")
    private boolean noLateWorkDelivery;

    @Column(name="is_adequate_capital_resource")
    private boolean isAdequateOfCapitalResource;

    @Column(name="is_apply_special_program")
    private boolean isApplySpecialProgram;

    @OneToOne
    @JoinColumn(name="special_program_id")
    private SpecialProgram specialProgram;

    @Column(name="is_refinance_in")
    private boolean isRefinanceIN;

    @OneToOne
    @JoinColumn(name="refinance_in_id")
    private Bank refinanceInValue;

    @Column(name="is_refinance_out")
    private boolean isRefinanceOUT;

    @OneToOne
    @JoinColumn(name="refinance_out_id")
    private Bank refinanceOutValue;

    @OneToOne
    @JoinColumn(name="risk_customer_type_id")
    private RiskType riskCustomerType;

    @Column(name="is_qualitative_type")
    private int qualitativeType;

    @Column(name="is_existing_sme_customer")
    private boolean isExistingSMECustomer;

    @Column(name="existing_since")
    private String existingSMECustomerSince;

    @Column(name="last_review_date")
    private Date lastReviewDate;

    @Column(name="extended_review_date")
    private Date extendedReviewDate;

    @OneToOne
    @JoinColumn(name="sbf_score_id")
    private SBFScore sbfScore;

    @Column(name="is_request_loan_same_name")
    private boolean requestLoanWithSameName;

    @Column(name="is_loan_in_one_year")
    private boolean haveLoanInOneYear;

    @Column(name="is_pass_annual_review")
    private boolean passAnnualReview;

    @OneToOne
    @JoinColumn(name="borrowing_type_id")
    private BorrowingType loanRequestPattern;

    @Column(name="referral_name")
    private String referralName;

    @Column(name="referral_id")
    private String referralID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

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

    public boolean isUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(boolean unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public boolean isNoPendingClaimLG() {
        return noPendingClaimLG;
    }

    public void setNoPendingClaimLG(boolean noPendingClaimLG) {
        this.noPendingClaimLG = noPendingClaimLG;
    }

    public boolean isConstructionRequestLG() {
        return isConstructionRequestLG;
    }

    public void setConstructionRequestLG(boolean constructionRequestLG) {
        isConstructionRequestLG = constructionRequestLG;
    }

    public boolean isAbleToGettingGuarantorJob() {
        return isAbleToGettingGuarantorJob;
    }

    public void setAbleToGettingGuarantorJob(boolean ableToGettingGuarantorJob) {
        isAbleToGettingGuarantorJob = ableToGettingGuarantorJob;
    }

    public boolean isNoClaimLGHistory() {
        return noClaimLGHistory;
    }

    public void setNoClaimLGHistory(boolean noClaimLGHistory) {
        this.noClaimLGHistory = noClaimLGHistory;
    }

    public boolean isNoRevokedLicense() {
        return noRevokedLicense;
    }

    public void setNoRevokedLicense(boolean noRevokedLicense) {
        this.noRevokedLicense = noRevokedLicense;
    }

    public boolean isNoLateWorkDelivery() {
        return noLateWorkDelivery;
    }

    public void setNoLateWorkDelivery(boolean noLateWorkDelivery) {
        this.noLateWorkDelivery = noLateWorkDelivery;
    }

    public boolean isAdequateOfCapitalResource() {
        return isAdequateOfCapitalResource;
    }

    public void setAdequateOfCapitalResource(boolean adequateOfCapitalResource) {
        isAdequateOfCapitalResource = adequateOfCapitalResource;
    }

    public boolean isApplySpecialProgram() {
        return isApplySpecialProgram;
    }

    public void setApplySpecialProgram(boolean applySpecialProgram) {
        isApplySpecialProgram = applySpecialProgram;
    }

    public SpecialProgram getSpecialProgram() {
        return specialProgram;
    }

    public void setSpecialProgram(SpecialProgram specialProgram) {
        this.specialProgram = specialProgram;
    }

    public boolean isRefinanceIN() {
        return isRefinanceIN;
    }

    public void setRefinanceIN(boolean refinanceIN) {
        isRefinanceIN = refinanceIN;
    }

    public Bank getRefinanceInValue() {
        return refinanceInValue;
    }

    public void setRefinanceInValue(Bank refinanceInValue) {
        this.refinanceInValue = refinanceInValue;
    }

    public boolean isRefinanceOUT() {
        return isRefinanceOUT;
    }

    public void setRefinanceOUT(boolean refinanceOUT) {
        isRefinanceOUT = refinanceOUT;
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

    public boolean isExistingSMECustomer() {
        return isExistingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        isExistingSMECustomer = existingSMECustomer;
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

    public boolean isRequestLoanWithSameName() {
        return requestLoanWithSameName;
    }

    public void setRequestLoanWithSameName(boolean requestLoanWithSameName) {
        this.requestLoanWithSameName = requestLoanWithSameName;
    }

    public boolean isHaveLoanInOneYear() {
        return haveLoanInOneYear;
    }

    public void setHaveLoanInOneYear(boolean haveLoanInOneYear) {
        this.haveLoanInOneYear = haveLoanInOneYear;
    }

    public boolean isPassAnnualReview() {
        return passAnnualReview;
    }

    public void setPassAnnualReview(boolean passAnnualReview) {
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
