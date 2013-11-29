package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_new_credit_detail")
public class NewCreditDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_CREDIT_DET_ID", sequenceName = "SEQ_WRK_NEW_CREDIT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_CREDIT_DET_ID")
    private long id;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "borrower_name")
    private String borrowerName;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "pce_percent")
    private BigDecimal pcePercent;

    @Column(name = "pce_amount")
    private BigDecimal pceAmount;

    @Column(name = "reduce_price_flag")
    private int reducePriceFlag;

    @Column(name = "reduce_front_end_fee")
    private int reduceFrontEndFee;

    @Column(name = "outstanding")
    private BigDecimal outstanding;

    @Column(name = "standard_price")
    private String standardPrice;

    @Column(name = "standard_interest")
    private BigDecimal standardInterest;

    @Column(name = "suggest_price")
    private String suggestPrice;

    @Column(name = "suggest_interest")
    private BigDecimal suggestInterest;

    @Column(name = "front_end_fee")
    private BigDecimal frontEndFee;

    @Column(name = "remark")
    private String remark;

    @Column(name = "hold_limit_amount")
    private BigDecimal holdLimitAmount;

    @Column(name = "installment")
    private BigDecimal installment;

    @Column(name = "finalPrice")
    private BigDecimal finalPrice;

    @Column(name = "tenor")
    private BigDecimal tenor;

    @Column(name = "purpose")
    private BigDecimal purpose;

    @Column(name = "seq")
    private int seq;

    @OneToOne
    @JoinColumn(name = "product_program_id")
    private ProductProgram productProgram;

    @OneToOne
    @JoinColumn(name = "credit_type_id")
    private CreditType creditType;

    @OneToOne
    @JoinColumn(name = "base_rate_id")
    private BaseRate standardBasePrice;

    @OneToOne
    @JoinColumn(name = "base_rate_id")
    private BaseRate suggestBasePrice;

    @OneToOne
    @JoinColumn(name = "disbursement_id")
    private Disbursement disbursement;

    @OneToOne
    @JoinColumn(name = "loan_purpose_id")
    private LoanPurpose loanPurpose;

    @ManyToOne
    @JoinColumn(name = "credit_facility_propose_id")
    private CreditFacilityPropose creditFacilityPropose;

/*    @OneToMany(mappedBy = "proposeCreditTierDetail", cascade = CascadeType.ALL)
    private List<ProposeCreditTierDetail> proposeCreditTierDetailList;*/

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public String getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(String standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
    }

    public String getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(String suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BigDecimal getSuggestInterest() {
        return suggestInterest;
    }

    public void setSuggestInterest(BigDecimal suggestInterest) {
        this.suggestInterest = suggestInterest;
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

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
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

    public BigDecimal getPurpose() {
        return purpose;
    }

    public void setPurpose(BigDecimal purpose) {
        this.purpose = purpose;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public BaseRate getStandardBasePrice() {
        return standardBasePrice;
    }

    public void setStandardBasePrice(BaseRate standardBasePrice) {
        this.standardBasePrice = standardBasePrice;
    }

    public BaseRate getSuggestBasePrice() {
        return suggestBasePrice;
    }

    public void setSuggestBasePrice(BaseRate suggestBasePrice) {
        this.suggestBasePrice = suggestBasePrice;
    }

    public Disbursement getDisbursement() {
        return disbursement;
    }

    public void setDisbursement(Disbursement disbursement) {
        this.disbursement = disbursement;
    }

    public LoanPurpose getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(LoanPurpose loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public CreditFacilityPropose getCreditFacilityPropose() {
        return creditFacilityPropose;
    }

    public void setCreditFacilityPropose(CreditFacilityPropose creditFacilityPropose) {
        this.creditFacilityPropose = creditFacilityPropose;
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

/*    public List<ProposeCreditTierDetail> getProposeCreditTierDetailList() {
        return proposeCreditTierDetailList;
    }

    public void setProposeCreditTierDetailList(List<ProposeCreditTierDetail> proposeCreditTierDetailList) {
        this.proposeCreditTierDetailList = proposeCreditTierDetailList;
    }*/

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("projectCode", projectCode)
                .append("productCode", productCode)
                .append("borrowerName", borrowerName)
                .append("limit", limit)
                .append("pcePercent", pcePercent)
                .append("pceAmount", pceAmount)
                .append("reducePriceFlag", reducePriceFlag)
                .append("reduceFrontEndFee", reduceFrontEndFee)
                .append("outstanding", outstanding)
                .append("standardPrice", standardPrice)
                .append("standardInterest", standardInterest)
                .append("suggestPrice", suggestPrice)
                .append("suggestInterest", suggestInterest)
                .append("frontEndFee", frontEndFee)
                .append("remark", remark)
                .append("holdLimitAmount", holdLimitAmount)
                .append("installment", installment)
                .append("finalPrice", finalPrice)
                .append("tenor", tenor)
                .append("purpose", purpose)
                .append("seq", seq)
                .append("productProgram", productProgram)
                .append("creditType", creditType)
                .append("standardBasePrice", standardBasePrice)
                .append("suggestBasePrice", suggestBasePrice)
                .append("disbursement", disbursement)
                .append("loanPurpose", loanPurpose)
                .append("creditFacilityPropose", creditFacilityPropose)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
