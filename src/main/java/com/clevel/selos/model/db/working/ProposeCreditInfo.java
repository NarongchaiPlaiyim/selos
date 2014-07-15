package com.clevel.selos.model.db.working;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_new_credit_detail")
public class ProposeCreditInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_CREDIT_DET_ID", sequenceName = "SEQ_WRK_NEW_CREDIT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_CREDIT_DET_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "uw_decision", columnDefinition = "int default 0", length = 1)
    @Enumerated(EnumType.ORDINAL)
    private DecisionType uwDecision;

    @Column(name = "request_type", columnDefinition = "int default 0")
    private int requestType;

    @Column(name = "refinance", columnDefinition = "int default 0")
    private int refinance;

    @OneToOne
    @JoinColumn(name = "product_program_id")
    private ProductProgram productProgram;

    @OneToOne
    @JoinColumn(name = "credit_type_id")
    private CreditType creditType;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "pce_percent")
    private BigDecimal pcePercent;

    @Column(name = "pce_amount")
    private BigDecimal pceAmount;

    @Column(name = "reduce_price_flag", columnDefinition = "int default 0")
    private int reducePriceFlag;

    @Column(name = "reduce_front_end_fee", columnDefinition = "int default 0")
    private int reduceFrontEndFee;

    @Column(name = "front_end_fee")
    private BigDecimal frontEndFee;

    @Column(name = "front_end_fee_original")
    private BigDecimal frontEndFeeOriginal;

    @OneToOne
    @JoinColumn(name = "loan_purpose_id")
    private LoanPurpose loanPurpose;

    @Column(name = "remark")
    private String remark;

    @OneToOne
    @JoinColumn(name = "disbursement_type_id")
    private DisbursementType disbursementType;

    @Column(name = "hold_limit_amount")
    private BigDecimal holdLimitAmount;

    @Column(name = "use_count", columnDefinition = "int default 0")
    private int useCount;

    @Column(name = "seq", columnDefinition = "int default 0")
    private int seq;

    @Column(name = "last_no", columnDefinition = "int default 0")
    private int lastNo;

    @Column(name = "propose_type", length = 1, columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private ProposeType proposeType;

    @Column(name = "installment")
    private BigDecimal installment;

    @ManyToOne
    @JoinColumn(name = "credit_facility_propose_id")
    private ProposeLine proposeLine;

    @OneToOne
    @JoinColumn(name = "suggest_rate_id")
    private BaseRate suggestBasePrice ;

    @Column(name = "suggest_interest")
    private BigDecimal suggestInterest;

    @OneToOne
    @JoinColumn(name = "standard_rate_id")
    private BaseRate standardBasePrice;

    @Column(name = "standard_interest")
    private BigDecimal standardInterest;

    @OneToMany(mappedBy = "proposeCreditInfo")
    private List<ProposeCreditInfoTierDetail> proposeCreditInfoTierDetailList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    @Column(name = "setup_completed", nullable = false, columnDefinition = "int default 0")
    private int setupCompleted;

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

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
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

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
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

    public BigDecimal getPcePercent() {
        return pcePercent;
    }

    public void setPcePercent(BigDecimal pcePercent) {
        this.pcePercent = pcePercent;
    }

    public BigDecimal getPceAmount() {
        return pceAmount;
    }

    public void setPceAmount(BigDecimal pceAmount) {
        this.pceAmount = pceAmount;
    }

    public int getReducePriceFlag() {
        return reducePriceFlag;
    }

    public void setReducePriceFlag(int reducePriceFlag) {
        this.reducePriceFlag = reducePriceFlag;
    }

    public int getReduceFrontEndFee() {
        return reduceFrontEndFee;
    }

    public void setReduceFrontEndFee(int reduceFrontEndFee) {
        this.reduceFrontEndFee = reduceFrontEndFee;
    }

    public BigDecimal getFrontEndFee() {
        return frontEndFee;
    }

    public void setFrontEndFee(BigDecimal frontEndFee) {
        this.frontEndFee = frontEndFee;
    }

    public LoanPurpose getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(LoanPurpose loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public DisbursementType getDisbursementType() {
        return disbursementType;
    }

    public void setDisbursementType(DisbursementType disbursementType) {
        this.disbursementType = disbursementType;
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

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public ProposeLine getProposeLine() {
        return proposeLine;
    }

    public void setProposeLine(ProposeLine proposeLine) {
        this.proposeLine = proposeLine;
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

    public int getSetupCompleted() {
        return setupCompleted;
    }

    public void setSetupCompleted(int setupCompleted) {
        this.setupCompleted = setupCompleted;
    }

    public List<ProposeCreditInfoTierDetail> getProposeCreditInfoTierDetailList() {
        return proposeCreditInfoTierDetailList;
    }

    public void setProposeCreditInfoTierDetailList(List<ProposeCreditInfoTierDetail> proposeCreditInfoTierDetailList) {
        this.proposeCreditInfoTierDetailList = proposeCreditInfoTierDetailList;
    }

    public BaseRate getSuggestBasePrice() {
        return suggestBasePrice;
    }

    public void setSuggestBasePrice(BaseRate suggestBasePrice) {
        this.suggestBasePrice = suggestBasePrice;
    }

    public BigDecimal getSuggestInterest() {
        return suggestInterest;
    }

    public void setSuggestInterest(BigDecimal suggestInterest) {
        this.suggestInterest = suggestInterest;
    }

    public BaseRate getStandardBasePrice() {
        return standardBasePrice;
    }

    public void setStandardBasePrice(BaseRate standardBasePrice) {
        this.standardBasePrice = standardBasePrice;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                append("uwDecision", uwDecision).
                append("requestType", requestType).
                append("refinance", refinance).
                append("productProgram", productProgram).
                append("creditType", creditType).
                append("productCode", productCode).
                append("projectCode", projectCode).
                append("limit", limit).
                append("pcePercent", pcePercent).
                append("pceAmount", pceAmount).
                append("reducePriceFlag", reducePriceFlag).
                append("reduceFrontEndFee", reduceFrontEndFee).
                append("frontEndFee", frontEndFee).
                append("frontEndFeeOriginal", frontEndFeeOriginal).
                append("loanPurpose", loanPurpose).
                append("remark", remark).
                append("disbursementType", disbursementType).
                append("holdLimitAmount", holdLimitAmount).
                append("useCount", useCount).
                append("seq", seq).
                append("lastNo", lastNo).
                append("proposeType", proposeType).
                append("installment", installment).
                append("proposeLine", proposeLine).
                append("suggestBasePrice", suggestBasePrice).
                append("suggestInterest", suggestInterest).
                append("standardBasePrice", standardBasePrice).
                append("standardInterest", standardInterest).
                append("proposeCreditInfoTierDetailList", proposeCreditInfoTierDetailList).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                append("setupCompleted", setupCompleted).
                toString();
    }
}
