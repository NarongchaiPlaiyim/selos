package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA CHITSUP
 * Date: 23/9/2556
 * Time: 22:15 น.
 * To change this template use File | Settings | File Templates.
 */
public class TCGView {
    private String isTCG;
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
        this.isTCG ="Y";
        this.requestLimitRequiredTCG = new BigDecimal(0);
        this.requestLimitNotRequiredTCG = new BigDecimal(0);
        this.existingLoanRatioUnderSameCollateral = new BigDecimal(0);
        this.existingLoanRatioNotUnderSameCollateral = new BigDecimal(0);
        this.tcbFloodAmount = new BigDecimal(0);
        this.collateralRuleResult = "";
        this.requestTCGAmount = new BigDecimal(0);
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

    public String getTCG() {
        return isTCG;
    }

    public void setTCG(String TCG) {
        isTCG = TCG;
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
