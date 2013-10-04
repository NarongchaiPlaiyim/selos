package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private String collateralRuleResult;

    @Column(name="request_tcg_amount")
    private BigDecimal requestTCGAmount;

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

    public String getCollateralRuleResult() {
        return collateralRuleResult;
    }

    public void setCollateralRuleResult(String collateralRuleResult) {
        this.collateralRuleResult = collateralRuleResult;
    }

    public BigDecimal getRequestTCGAmount() {
        return requestTCGAmount;
    }

    public void setRequestTCGAmount(BigDecimal requestTCGAmount) {
        this.requestTCGAmount = requestTCGAmount;
    }
}
