package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class TCGView implements Serializable {
    private Long id;
    private boolean isTCG;
    private BigDecimal requestLimitRequiredTCG;
    private BigDecimal requestLimitNotRequiredTCG;
    private BigDecimal existingLoanRatioUnderSameCollateral;
    private BigDecimal existingLoanRatioNotUnderSameCollateral;
    private BigDecimal tcbFloodAmount;
    private String collateralRuleResult;
    private BigDecimal requestTCGAmount;

    public TCGView(){
        reset();
    }

    public void reset(){
        this.isTCG = false;
        this.requestLimitRequiredTCG = new BigDecimal(0);
        this.requestLimitNotRequiredTCG = new BigDecimal(0);
        this.existingLoanRatioUnderSameCollateral = new BigDecimal(0);
        this.existingLoanRatioNotUnderSameCollateral = new BigDecimal(0);
        this.tcbFloodAmount = new BigDecimal(0);
        this.collateralRuleResult = "";
        this.requestTCGAmount = new BigDecimal(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public boolean isTCG() {
        return isTCG;
    }

    public void setTCG(boolean TCG) {
        isTCG = TCG;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("isTCG", isTCG)
                .append("requestLimitRequiredTCG", requestLimitRequiredTCG)
                .append("requestLimitNotRequiredTCG", requestLimitNotRequiredTCG)
                .append("existingLoanRatioUnderSameCollateral", existingLoanRatioUnderSameCollateral)
                .append("existingLoanRatioNotUnderSameCollateral", existingLoanRatioNotUnderSameCollateral)
                .append("tcbFloodAmount", tcbFloodAmount)
                .toString();
    }
}
