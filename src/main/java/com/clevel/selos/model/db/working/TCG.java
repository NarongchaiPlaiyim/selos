package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_tcg")
public class TCG implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_TCG_ID", sequenceName="SEQ_WRK_TCG_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_TCG_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @Column(name="active")
    private boolean active;

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

    @Column(name="tcg_flag")
    private boolean tcgFlag;

    @Column(name="request_limit_tcg")
    private BigDecimal requestLimitRequiredTCG;

    @Column(name="request_limit_not_tcg")
    private BigDecimal requestLimitNotRequiredTCG;

    @Column(name="exist_loan_under_same")
    private BigDecimal existingLoanRatioUnderSameCollateral;

    @Column(name="exist_loan_not_under_same")
    private BigDecimal existingLoanRatioNotUnderSameCollateral;

    @Column(name="tcb_flood_amount")
    private BigDecimal tcbFloodAmount;

    @Column(name="collateral_rule_result")
    private BigDecimal collateralRuleResult;

    @Column(name="request_tcg_amount")
    private BigDecimal requestTCGAmount;

    @Column(name="sum_appraisal_amount")
    private BigDecimal sumAppraisalAmount;

    @Column(name="sum_ltv_value")
    private BigDecimal sumLtvValue;

    @Column(name="sum_in_this_appraisal_amount")
    private BigDecimal sumInThisAppraisalAmount;

    @Column(name="sum_in_this_ltv_value")
    private BigDecimal sumInThisLtvValue;


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


    public boolean isTcgFlag() {
        return tcgFlag;
    }

    public void setTcgFlag(boolean tcgFlag) {
        this.tcgFlag = tcgFlag;
    }

    public BigDecimal getRequestLimitRequiredTCG() {
        return requestLimitRequiredTCG;
    }

    public void setRequestLimitRequiredTCG(BigDecimal requestLimitRequiredTCG) {
        this.requestLimitRequiredTCG = requestLimitRequiredTCG;
    }

    public BigDecimal getRequestLimitNotRequiredTCG() {
        return requestLimitNotRequiredTCG;
    }

    public void setRequestLimitNotRequiredTCG(BigDecimal requestLimitNotRequiredTCG) {
        this.requestLimitNotRequiredTCG = requestLimitNotRequiredTCG;
    }

    public BigDecimal getExistingLoanRatioUnderSameCollateral() {
        return existingLoanRatioUnderSameCollateral;
    }

    public void setExistingLoanRatioUnderSameCollateral(BigDecimal existingLoanRatioUnderSameCollateral) {
        this.existingLoanRatioUnderSameCollateral = existingLoanRatioUnderSameCollateral;
    }

    public BigDecimal getExistingLoanRatioNotUnderSameCollateral() {
        return existingLoanRatioNotUnderSameCollateral;
    }

    public void setExistingLoanRatioNotUnderSameCollateral(BigDecimal existingLoanRatioNotUnderSameCollateral) {
        this.existingLoanRatioNotUnderSameCollateral = existingLoanRatioNotUnderSameCollateral;
    }

    public BigDecimal getTcbFloodAmount() {
        return tcbFloodAmount;
    }

    public void setTcbFloodAmount(BigDecimal tcbFloodAmount) {
        this.tcbFloodAmount = tcbFloodAmount;
    }

    public BigDecimal getCollateralRuleResult() {
        return collateralRuleResult;
    }

    public void setCollateralRuleResult(BigDecimal collateralRuleResult) {
        this.collateralRuleResult = collateralRuleResult;
    }

    public BigDecimal getRequestTCGAmount() {
        return requestTCGAmount;
    }

    public void setRequestTCGAmount(BigDecimal requestTCGAmount) {
        this.requestTCGAmount = requestTCGAmount;
    }

    public BigDecimal getSumAppraisalAmount() {
        return sumAppraisalAmount;
    }

    public void setSumAppraisalAmount(BigDecimal sumAppraisalAmount) {
        this.sumAppraisalAmount = sumAppraisalAmount;
    }

    public BigDecimal getSumLtvValue() {
        return sumLtvValue;
    }

    public void setSumLtvValue(BigDecimal sumLtvValue) {
        this.sumLtvValue = sumLtvValue;
    }

    public BigDecimal getSumInThisAppraisalAmount() {
        return sumInThisAppraisalAmount;
    }

    public void setSumInThisAppraisalAmount(BigDecimal sumInThisAppraisalAmount) {
        this.sumInThisAppraisalAmount = sumInThisAppraisalAmount;
    }

    public BigDecimal getSumInThisLtvValue() {
        return sumInThisLtvValue;
    }

    public void setSumInThisLtvValue(BigDecimal sumInThisLtvValue) {
        this.sumInThisLtvValue = sumInThisLtvValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
