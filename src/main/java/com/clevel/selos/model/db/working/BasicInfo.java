package com.clevel.selos.model.db.working;

import com.clevel.selos.model.ApproveResult;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_basicinfo")
public class BasicInfo implements Serializable {
    private static final long serialVersionUID = -6027640476330388411L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_BASIC_INFO_ID", sequenceName = "SEQ_WRK_BASIC_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BASIC_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "ca_number", length = 30)
    private String caNumber;

    @Column(name = "ref_app_number")
    private String refAppNumber;

    @OneToOne
    @JoinColumn(name = "request_reason_code")
    private Reason requestReason;

    @OneToOne
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;

    @Column(name = "is_no_unpaid_fee_insurance", length = 1, nullable = false, columnDefinition = "int default 0")
    private int noUnpaidFeeInsurance;

    @Column(name = "is_no_pending_claim_lg", length = 1, nullable = false, columnDefinition = "int default 0")
    private int noPendingClaimLG;

    @Column(name = "construct_request_lg", length = 1, nullable = false, columnDefinition = "int default 0")
    private int constructionRequestLG;

    @Column(name = "able_to_get_guarantor", length = 1, nullable = false, columnDefinition = "int default 0")
    private int ableToGettingGuarantorJob;

    @Column(name = "claim_lg_history", length = 1, nullable = false, columnDefinition = "int default 0")
    private int noClaimLGHistory;

    @Column(name = "revoke_license", length = 1, nullable = false, columnDefinition = "int default 0")
    private int noRevokedLicense;

    @Column(name = "late_work_delivery", length = 1, nullable = false, columnDefinition = "int default 0")
    private int noLateWorkDelivery;

    @Column(name = "adequate_capital_resource", length = 1, nullable = false, columnDefinition = "int default 0")
    private int adequateOfCapitalResource;

    @Column(name = "apply_special_program", length = 1, nullable = false, columnDefinition = "int default 0")
    private int applySpecialProgram;

    @OneToOne
    @JoinColumn(name = "special_program_id")
    private SpecialProgram specialProgram;

    @Column(name = "refinance_in", length = 1, nullable = false, columnDefinition = "int default -1")
    private int refinanceIN;

    @OneToOne
    @JoinColumn(name = "refinance_in_id")
    private Bank refinanceInValue;

    @Column(name = "refinance_out", length = 1, nullable = false, columnDefinition = "int default -1")
    private int refinanceOUT;

    @OneToOne
    @JoinColumn(name = "refinance_out_id")
    private Bank refinanceOutValue;

    @OneToOne
    @JoinColumn(name = "risktype_id")
    private RiskType riskCustomerType;

    @Column(name = "qualitative_type", length = 1, nullable = false, columnDefinition = "int default -1")
    private int qualitativeType;

    @Column(name = "existing_sme_customer", length = 1, nullable = false, columnDefinition = "int default -1")
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

    @Column(name = "request_loan_same_name", length = 1, nullable = false, columnDefinition = "int default 0")
    private int requestLoanWithSameName;

    @Column(name = "loan_in_one_year", length = 1, nullable = false, columnDefinition = "int default 0")
    private int haveLoanInOneYear;

    @Column(name = "pass_annual_review", length = 1, nullable = false, columnDefinition = "int default 0")
    private int passAnnualReview;

    @OneToOne
    @JoinColumn(name = "borrowing_type_id")
    private BorrowingType loanRequestPattern;

    @Column(name = "referral_name")
    private String referralName;

    @Column(name = "referral_id")
    private String referralID;

    @Column(name = "bdm_submit_date")
    private Date bdmSubmitDate;

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

    @Column(name = "retrieved_flag", length = 1, nullable = false, columnDefinition = "int default 0")
    private int retrievedFlag;
    
    //Update for Post Approval
    @Column(name = "approved_type", length = 1, nullable = false, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ApproveType approveType;

    @Column(name = "approved_result", length = 1, nullable = false, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ApproveResult approveResult;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "limit_setup_expiry_date")
    private Date limitSetupExpiryDate;

    @Column(name = "premium_quote", columnDefinition = "int default 0")
    private int premiumQuote;

    @Column(name = "tcg_flag", columnDefinition = "int default 0")
    private int tcgFlag;

    @OneToOne
    @JoinColumn(name = "appeal_resubmit_reason_id")
    private Reason appealResubmitReason;


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

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getRefAppNumber() {
        return refAppNumber;
    }

    public void setRefAppNumber(String refAppNumber) {
        this.refAppNumber = refAppNumber;
    }

    public Reason getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(Reason requestReason) {
        this.requestReason = requestReason;
    }

    public CustomerEntity getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(CustomerEntity borrowerType) {
        this.borrowerType = borrowerType;
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

    public Date getBdmSubmitDate() {
        return bdmSubmitDate;
    }

    public void setBdmSubmitDate(Date bdmSubmitDate) {
        this.bdmSubmitDate = bdmSubmitDate;
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

    public int getRetrievedFlag() {
        return retrievedFlag;
    }

    public void setRetrievedFlag(int retrievedFlag) {
        this.retrievedFlag = retrievedFlag;
    }
	public ApproveResult getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(ApproveResult approveResult) {
		this.approveResult = approveResult;
	}

	public ApproveType getApproveType() {
		return approveType;
	}

	public void setApproveType(ApproveType approveType) {
		this.approveType = approveType;
	}

    public Date getLimitSetupExpiryDate() {
        return limitSetupExpiryDate;
    }

    public void setLimitSetupExpiryDate(Date limitSetupExpiryDate) {
        this.limitSetupExpiryDate = limitSetupExpiryDate;
    }

    public int getPremiumQuote() {
        return premiumQuote;
    }

    public void setPremiumQuote(int premiumQuote) {
        this.premiumQuote = premiumQuote;
    }

    public int getTcgFlag() {
        return tcgFlag;
    }

    public void setTcgFlag(int tcgFlag) {
        this.tcgFlag = tcgFlag;
    }

    public Reason getAppealResubmitReason() {
        return appealResubmitReason;
    }

    public void setAppealResubmitReason(Reason appealResubmitReason) {
        this.appealResubmitReason = appealResubmitReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("caNumber", caNumber)
                .append("refAppNumber", refAppNumber)
                .append("requestReason", requestReason)
                .append("borrowerType", borrowerType)
                .append("noUnpaidFeeInsurance", noUnpaidFeeInsurance)
                .append("noPendingClaimLG", noPendingClaimLG)
                .append("constructionRequestLG", constructionRequestLG)
                .append("ableToGettingGuarantorJob", ableToGettingGuarantorJob)
                .append("noClaimLGHistory", noClaimLGHistory)
                .append("noRevokedLicense", noRevokedLicense)
                .append("noLateWorkDelivery", noLateWorkDelivery)
                .append("adequateOfCapitalResource", adequateOfCapitalResource)
                .append("applySpecialProgram", applySpecialProgram)
                .append("specialProgram", specialProgram)
                .append("refinanceIN", refinanceIN)
                .append("refinanceInValue", refinanceInValue)
                .append("refinanceOUT", refinanceOUT)
                .append("refinanceOutValue", refinanceOutValue)
                .append("riskCustomerType", riskCustomerType)
                .append("qualitativeType", qualitativeType)
                .append("existingSMECustomer", existingSMECustomer)
                .append("existingSMECustomerSince", existingSMECustomerSince)
                .append("lastReviewDate", lastReviewDate)
                .append("extendedReviewDate", extendedReviewDate)
                .append("sbfScore", sbfScore)
                .append("requestLoanWithSameName", requestLoanWithSameName)
                .append("haveLoanInOneYear", haveLoanInOneYear)
                .append("passAnnualReview", passAnnualReview)
                .append("loanRequestPattern", loanRequestPattern)
                .append("referralName", referralName)
                .append("referralID", referralID)
                .append("bdmSubmitDate", bdmSubmitDate)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("retrievedFlag", retrievedFlag)
                .append("approveType", approveType)
                .append("approveResult", approveResult)
                .append("limitSetupExpiryDate", limitSetupExpiryDate)
                .append("premiumQuote", premiumQuote)
                .append("tcgFlag", tcgFlag)
                .toString();
    }
}
