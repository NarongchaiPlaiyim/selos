package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BankStmtView implements Serializable {
    private int isNotCountIncome;
    private BankView bankView;
    private String branchName;
    private AccountTypeView accountTypeView;
    private String otherAccountType;
    private String accountNumber;
    private String accountName;
    private AccountStatusView accountStatusView;
    private String accountStatus;
    private int isMainAccount;
    private int accountCharacteristic;

    private BigDecimal averageIncomeGross;
    private BigDecimal averageIncomeNetBDM;
    private BigDecimal averageIncomeNetUW;
    private BigDecimal averageDrawAmount;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal averageGrossInflowPerLimit;
    private BigDecimal chequeReturn;
    private BigDecimal tradeChequeReturnAmount;
    private BigDecimal overLimitTimes;
    private BigDecimal overLimitDays;
    private String remark;

    private List<BankStmtDetailView> bankStmtDetailViewList;
    private List<BankStmtSrcCollateralProof> bankStmtSrcCollateralProofList;

    public BankStmtView() {
        reset();
    }

    public void reset() {

    }

    public int getNotCountIncome() {
        return isNotCountIncome;
    }

    public void setNotCountIncome(int notCountIncome) {
        isNotCountIncome = notCountIncome;
    }

    public BankView getBankView() {
        return bankView;
    }

    public void setBankView(BankView bankView) {
        this.bankView = bankView;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public AccountTypeView getAccountTypeView() {
        return accountTypeView;
    }

    public void setAccountTypeView(AccountTypeView accountTypeView) {
        this.accountTypeView = accountTypeView;
    }

    public String getOtherAccountType() {
        return otherAccountType;
    }

    public void setOtherAccountType(String otherAccountType) {
        this.otherAccountType = otherAccountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountStatusView getAccountStatusView() {
        return accountStatusView;
    }

    public void setAccountStatusView(AccountStatusView accountStatusView) {
        this.accountStatusView = accountStatusView;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getMainAccount() {
        return isMainAccount;
    }

    public void setMainAccount(int mainAccount) {
        isMainAccount = mainAccount;
    }

    public int getAccountCharacteristic() {
        return accountCharacteristic;
    }

    public void setAccountCharacteristic(int accountCharacteristic) {
        this.accountCharacteristic = accountCharacteristic;
    }

    public BigDecimal getAverageIncomeGross() {
        return averageIncomeGross;
    }

    public void setAverageIncomeGross(BigDecimal averageIncomeGross) {
        this.averageIncomeGross = averageIncomeGross;
    }

    public BigDecimal getAverageIncomeNetBDM() {
        return averageIncomeNetBDM;
    }

    public void setAverageIncomeNetBDM(BigDecimal averageIncomeNetBDM) {
        this.averageIncomeNetBDM = averageIncomeNetBDM;
    }

    public BigDecimal getAverageIncomeNetUW() {
        return averageIncomeNetUW;
    }

    public void setAverageIncomeNetUW(BigDecimal averageIncomeNetUW) {
        this.averageIncomeNetUW = averageIncomeNetUW;
    }

    public BigDecimal getAverageDrawAmount() {
        return averageDrawAmount;
    }

    public void setAverageDrawAmount(BigDecimal averageDrawAmount) {
        this.averageDrawAmount = averageDrawAmount;
    }

    public BigDecimal getSwingPercent() {
        return swingPercent;
    }

    public void setSwingPercent(BigDecimal swingPercent) {
        this.swingPercent = swingPercent;
    }

    public BigDecimal getUtilizationPercent() {
        return utilizationPercent;
    }

    public void setUtilizationPercent(BigDecimal utilizationPercent) {
        this.utilizationPercent = utilizationPercent;
    }

    public BigDecimal getAverageGrossInflowPerLimit() {
        return averageGrossInflowPerLimit;
    }

    public void setAverageGrossInflowPerLimit(BigDecimal averageGrossInflowPerLimit) {
        this.averageGrossInflowPerLimit = averageGrossInflowPerLimit;
    }

    public BigDecimal getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(BigDecimal chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getTradeChequeReturnAmount() {
        return tradeChequeReturnAmount;
    }

    public void setTradeChequeReturnAmount(BigDecimal tradeChequeReturnAmount) {
        this.tradeChequeReturnAmount = tradeChequeReturnAmount;
    }

    public BigDecimal getOverLimitTimes() {
        return overLimitTimes;
    }

    public void setOverLimitTimes(BigDecimal overLimitTimes) {
        this.overLimitTimes = overLimitTimes;
    }

    public BigDecimal getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(BigDecimal overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<BankStmtDetailView> getBankStmtDetailViewList() {
        return bankStmtDetailViewList;
    }

    public void setBankStmtDetailViewList(List<BankStmtDetailView> bankStmtDetailViewList) {
        this.bankStmtDetailViewList = bankStmtDetailViewList;
    }

    public List<BankStmtSrcCollateralProof> getBankStmtSrcCollateralProofList() {
        return bankStmtSrcCollateralProofList;
    }

    public void setBankStmtSrcCollateralProofList(List<BankStmtSrcCollateralProof> bankStmtSrcCollateralProofList) {
        this.bankStmtSrcCollateralProofList = bankStmtSrcCollateralProofList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("isNotCountIncome", isNotCountIncome)
                .append("bankView", bankView)
                .append("branchName", branchName)
                .append("accountTypeView", accountTypeView)
                .append("otherAccountType", otherAccountType)
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("accountStatusView", accountStatusView)
                .append("accountStatus", accountStatus)
                .append("isMainAccount", isMainAccount)
                .append("accountCharacteristic", accountCharacteristic)
                .append("averageIncomeGross", averageIncomeGross)
                .append("averageIncomeNetBDM", averageIncomeNetBDM)
                .append("averageIncomeNetUW", averageIncomeNetUW)
                .append("averageDrawAmount", averageDrawAmount)
                .append("swingPercent", swingPercent)
                .append("utilizationPercent", utilizationPercent)
                .append("averageGrossInflowPerLimit", averageGrossInflowPerLimit)
                .append("chequeReturn", chequeReturn)
                .append("tradeChequeReturnAmount", tradeChequeReturnAmount)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("remark", remark)
                .append("bankStmtDetailViewList", bankStmtDetailViewList)
                .append("bankStmtSrcCollateralProofList", bankStmtSrcCollateralProofList)
                .toString();
    }
}
