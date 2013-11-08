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
    private BankAccountTypeView bankAccountTypeView;
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
    private BigDecimal avgSwingPercent;
    private BigDecimal avgUtilizationPercent;
    private BigDecimal avgGrossInflowPerLimit;
    private BigDecimal chequeReturn;
    private BigDecimal trdChequeReturnAmount;
    private BigDecimal trdChequeReturnPercent;
    private BigDecimal overLimitTimes;
    private BigDecimal overLimitDays;
    private String remark;

    private List<BankStmtDetailView> bankStmtDetailViewList;
    private List<BankStmtSrcOfCollateralProofView> srcOfCollateralProofViewList;
    private BigDecimal avgOSBalanceAmount;

    public BankStmtView() {
        reset();
    }

    public void reset() {
        this.bankView = new BankView();
        this.branchName = "";
        this.bankAccountTypeView = new BankAccountTypeView();
        this.accountNumber = "";
        this.accountName = "";
        this.accountStatusView = new AccountStatusView();
        this.limit = BigDecimal.ZERO;
        this.avgIncomeGross = BigDecimal.ZERO;
        this.avgIncomeNetBDM = BigDecimal.ZERO;
        this.avgIncomeNetUW = BigDecimal.ZERO;
        this.avgWithDrawAmount = BigDecimal.ZERO;
        this.avgSwingPercent = BigDecimal.ZERO;
        this.avgUtilizationPercent = BigDecimal.ZERO;
        this.avgGrossInflowPerLimit = BigDecimal.ZERO;
        this.chequeReturn = BigDecimal.ZERO;
        this.trdChequeReturnAmount = BigDecimal.ZERO;
        this.trdChequeReturnPercent = BigDecimal.ZERO;
        this.overLimitTimes = BigDecimal.ZERO;
        this.overLimitDays = BigDecimal.ZERO;
        this.remark = "";
        this.avgOSBalanceAmount = BigDecimal.ZERO;
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

    public boolean isNotCountIncome() {
        return isNotCountIncome != 0;
    }

    public void setNotCountIncome(boolean notCountIncome) {
        setNotCountIncome(notCountIncome ? 1 : 0);
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

    public BankAccountTypeView getBankAccountTypeView() {
        return bankAccountTypeView;
    }

    public void setBankAccountTypeView(BankAccountTypeView bankAccountTypeView) {
        this.bankAccountTypeView = bankAccountTypeView;
    }

    public int getOtherAccountType() {
        return otherAccountType;
    }

    public void setOtherAccountType(int otherAccountType) {
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

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
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

    public BigDecimal getAvgWithDrawAmount() {
        return avgWithDrawAmount;
    }

    public void setAvgWithDrawAmount(BigDecimal avgWithDrawAmount) {
        this.avgWithDrawAmount = avgWithDrawAmount;
    }

    public BigDecimal getAvgSwingPercent() {
        return avgSwingPercent;
    }

    public void setAvgSwingPercent(BigDecimal avgSwingPercent) {
        this.avgSwingPercent = avgSwingPercent;
    }

    public BigDecimal getAvgUtilizationPercent() {
        return avgUtilizationPercent;
    }

    public void setAvgUtilizationPercent(BigDecimal avgUtilizationPercent) {
        this.avgUtilizationPercent = avgUtilizationPercent;
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

    public List<BankStmtSrcOfCollateralProofView> getSrcOfCollateralProofViewList() {
        return srcOfCollateralProofViewList;
    }

    public void setSrcOfCollateralProofViewList(List<BankStmtSrcOfCollateralProofView> srcOfCollateralProofViewList) {
        this.srcOfCollateralProofViewList = srcOfCollateralProofViewList;
    }

    public BigDecimal getAvgOSBalanceAmount() {
        return avgOSBalanceAmount;
    }

    public void setAvgOSBalanceAmount(BigDecimal avgOSBalanceAmount) {
        this.avgOSBalanceAmount = avgOSBalanceAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isNotCountIncome", isNotCountIncome)
                .append("bankView", bankView)
                .append("branchName", branchName)
                .append("bankAccountTypeView", bankAccountTypeView)
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
                .append("avgSwingPercent", avgSwingPercent)
                .append("avgUtilizationPercent", avgUtilizationPercent)
                .append("avgGrossInflowPerLimit", avgGrossInflowPerLimit)
                .append("chequeReturn", chequeReturn)
                .append("trdChequeReturnAmount", trdChequeReturnAmount)
                .append("trdChequeReturnPercent", trdChequeReturnPercent)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("remark", remark)
                .append("bankStmtDetailViewList", bankStmtDetailViewList)
                .append("srcOfCollateralProofViewList", srcOfCollateralProofViewList)
                .append("avgOSBalanceAmount", avgOSBalanceAmount)
                .toString();
    }
}
