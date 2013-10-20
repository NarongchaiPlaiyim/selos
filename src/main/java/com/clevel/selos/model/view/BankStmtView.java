package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BankStmtView implements Serializable {
    private long id;
    private int isNotCountIncome;
    private BankView bankView;
    private String branchName;
    private AccountTypeView accountTypeView;
    private int otherAccountType;
    private String accountNumber;
    private String accountName;
    private AccountStatusView accountStatusView;
    private int isMainAccount;
    private int accountCharacteristic;

    private BigDecimal limit;
    private BigDecimal avgIncomeGross;
    private BigDecimal avgIncomeNetBDM;
    private BigDecimal avgIncomeNetUW;
    private BigDecimal avgWithDrawAmount;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal avgGrossInflowPerLimit;
    private BigDecimal chequeReturn;
    private BigDecimal trdChequeReturnAmount;
    private BigDecimal trdChequeReturnPercent;
    private BigDecimal overLimitTimes;
    private BigDecimal overLimitDays;
    private String remark;

    private List<BankStmtDetailView> bankStmtDetailViewList;
    private List<BankStmtSrcCollateralProof> bankStmtSrcCollateralProofList;

    //Average = (Sum of bankStmtSrcCollateralProofList[i].OSBalanceAmount) / bankStmtSrcCollateralProofList.size()
    private BigDecimal averageOSBalanceAmount;

    public BankStmtView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getAvgIncomeGross() {
        return avgIncomeGross;
    }

    public void setAvgIncomeGross(BigDecimal avgIncomeGross) {
        this.avgIncomeGross = avgIncomeGross;
    }

    public BigDecimal getAvgIncomeNetBDM() {
        return avgIncomeNetBDM;
    }

    public void setAvgIncomeNetBDM(BigDecimal avgIncomeNetBDM) {
        this.avgIncomeNetBDM = avgIncomeNetBDM;
    }

    public BigDecimal getAvgIncomeNetUW() {
        return avgIncomeNetUW;
    }

    public void setAvgIncomeNetUW(BigDecimal avgIncomeNetUW) {
        this.avgIncomeNetUW = avgIncomeNetUW;
    }

    public void setOtherAccountType(int otherAccountType) {
        this.otherAccountType = otherAccountType;
    }

    public BigDecimal getAvgWithDrawAmount() {
        return avgWithDrawAmount;
    }

    public void setAvgWithDrawAmount(BigDecimal avgWithDrawAmount) {
        this.avgWithDrawAmount = avgWithDrawAmount;
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

    public BigDecimal getAvgGrossInflowPerLimit() {
        return avgGrossInflowPerLimit;
    }

    public void setAvgGrossInflowPerLimit(BigDecimal avgGrossInflowPerLimit) {
        this.avgGrossInflowPerLimit = avgGrossInflowPerLimit;
    }

    public BigDecimal getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(BigDecimal chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getTrdChequeReturnAmount() {
        return trdChequeReturnAmount;
    }

    public void setTrdChequeReturnAmount(BigDecimal trdChequeReturnAmount) {
        this.trdChequeReturnAmount = trdChequeReturnAmount;
    }

    public BigDecimal getTrdChequeReturnPercent() {
        return trdChequeReturnPercent;
    }

    public void setTrdChequeReturnPercent(BigDecimal trdChequeReturnPercent) {
        this.trdChequeReturnPercent = trdChequeReturnPercent;
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

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getAverageOSBalanceAmount() {
        return averageOSBalanceAmount;
    }

    public void setAverageOSBalanceAmount(BigDecimal averageOSBalanceAmount) {
        this.averageOSBalanceAmount = averageOSBalanceAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isNotCountIncome", isNotCountIncome)
                .append("bankView", bankView)
                .append("branchName", branchName)
                .append("accountTypeView", accountTypeView)
                .append("otherAccountType", otherAccountType)
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("accountStatusView", accountStatusView)
                .append("isMainAccount", isMainAccount)
                .append("accountCharacteristic", accountCharacteristic)
                .append("limit", limit)
                .append("avgIncomeGross", avgIncomeGross)
                .append("avgIncomeNetBDM", avgIncomeNetBDM)
                .append("avgIncomeNetUW", avgIncomeNetUW)
                .append("avgWithDrawAmount", avgWithDrawAmount)
                .append("swingPercent", swingPercent)
                .append("utilizationPercent", utilizationPercent)
                .append("avgGrossInflowPerLimit", avgGrossInflowPerLimit)
                .append("chequeReturn", chequeReturn)
                .append("trdChequeReturnAmount", trdChequeReturnAmount)
                .append("trdChequeReturnPercent", trdChequeReturnPercent)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("remark", remark)
                .append("bankStmtDetailViewList", bankStmtDetailViewList)
                .append("bankStmtSrcCollateralProofList", bankStmtSrcCollateralProofList)
                .append("averageOSBalanceAmount", averageOSBalanceAmount)
                .toString();
    }
}
