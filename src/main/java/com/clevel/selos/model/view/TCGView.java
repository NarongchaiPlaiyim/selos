package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TCGView implements Serializable {
    private long id;
    private int isTCG;
    private BigDecimal requestLimitRequiredTCG;
    private BigDecimal requestLimitNotRequiredTCG;
    private BigDecimal existingLoanRatioUnderSameCollateral;
    private BigDecimal existingLoanRatioNotUnderSameCollateral;
    private BigDecimal tcbFloodAmount;
    private BigDecimal collateralRuleResult;
    private BigDecimal requestTCGAmount;
    private BigDecimal sumAppraisalAmount;
    private BigDecimal sumLtvValue;
    private BigDecimal sumInThisAppraisalAmount;
    private BigDecimal sumInThisLtvValue;

    private boolean active;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;


    private String sumAppraisalAmtText;
    private String sumLtvAmtText;
    private String sumThisAppraisalAmtText;
    private String sumThisLtvAmtText;

    public TCGView(){
        reset();
    }

    public void reset(){

        this.isTCG = 0;
        this.requestLimitRequiredTCG = new BigDecimal(0);
        this.requestLimitNotRequiredTCG = new BigDecimal(0);
        this.existingLoanRatioUnderSameCollateral = new BigDecimal(0);
        this.existingLoanRatioNotUnderSameCollateral = new BigDecimal(0);
        this.tcbFloodAmount = new BigDecimal(0);
        this.collateralRuleResult = new BigDecimal(0);
        this.requestTCGAmount = new BigDecimal(0);
        this.sumAppraisalAmount = new BigDecimal(0);
        this.sumLtvValue = new BigDecimal(0);
        this.sumInThisAppraisalAmount = new BigDecimal(0);
        this.sumInThisLtvValue = new BigDecimal(0);
        sumAppraisalAmtText="0.00";
        sumLtvAmtText="0.00";
        sumThisAppraisalAmtText = "0.00";
        sumThisLtvAmtText="0.00";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTCG() {
        return isTCG;
    }

    public void setTCG(int TCG) {
        isTCG = TCG;
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

    public String getSumAppraisalAmtText() {
        return sumAppraisalAmtText = Util.formatNumber(sumAppraisalAmount.doubleValue());
    }

    public void setSumAppraisalAmtText(String sumAppraisalAmtText) {
        this.sumAppraisalAmtText = sumAppraisalAmtText;
    }

    public String getSumLtvAmtText() {
        return sumLtvAmtText = Util.formatNumber(sumLtvValue.doubleValue());
    }

    public void setSumLtvAmtText(String sumLtvAmtText) {
        this.sumLtvAmtText = sumLtvAmtText;
    }

    public String getSumThisAppraisalAmtText() {
        return sumThisAppraisalAmtText = Util.formatNumber(sumInThisAppraisalAmount.doubleValue());
    }

    public void setSumThisAppraisalAmtText(String sumThisAppraisalAmtText) {
        this.sumThisAppraisalAmtText = sumThisAppraisalAmtText;
    }

    public String getSumThisLtvAmtText() {
        return sumThisLtvAmtText = Util.formatNumber(sumInThisLtvValue.doubleValue());
    }

    public void setSumThisLtvAmtText(String sumThisLtvAmtText) {
        this.sumThisLtvAmtText = sumThisLtvAmtText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isTCG", isTCG)
                .append("requestLimitRequiredTCG", requestLimitRequiredTCG)
                .append("requestLimitNotRequiredTCG", requestLimitNotRequiredTCG)
                .append("existingLoanRatioUnderSameCollateral", existingLoanRatioUnderSameCollateral)
                .append("existingLoanRatioNotUnderSameCollateral", existingLoanRatioNotUnderSameCollateral)
                .append("tcbFloodAmount", tcbFloodAmount)
                .append("collateralRuleResult", collateralRuleResult)
                .append("requestTCGAmount", requestTCGAmount)
                .append("sumAppraisalAmount", sumAppraisalAmount)
                .append("sumLtvValue", sumLtvValue)
                .append("sumInThisAppraisalAmount", sumInThisAppraisalAmount)
                .append("sumInThisLtvValue", sumInThisLtvValue)
                .append("active", active)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
