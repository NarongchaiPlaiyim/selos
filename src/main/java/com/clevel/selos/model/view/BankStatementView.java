package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.BankAccountType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankStatementView implements Serializable {
    private boolean isNotCountIncome;
    private Bank bank;
    private String branchName;
    private BankAccountType bankAccountType;
    private BankAccountType otherAccountType;
    private String accountNo;
    private String accountName;
    private AccountStatus accountStatus;
    private int accountCharId;
    private List<BankStmtDetailView> bankStmtDetailViews;
    private BigDecimal averageIncomeGross;
    private BigDecimal averageIncomeNetBDM;
    private BigDecimal averageIncomeNetUW;
    private BigDecimal averageDrawAmount;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal averageGrossInflowPerLimit;
    private int chequeReturn;
    private BigDecimal tradeChequeReturnAmount;
    private int overLimitTimes;
    private int overLimitDays;
    private String remark;

    public BankStatementView() {
        reset();
    }

    public void reset() {
        this.bank = new Bank();
        this.branchName = "";
        this.bankAccountType = new BankAccountType();
        this.otherAccountType = new BankAccountType();
        this.accountNo = "";
        this.accountName = "";
        this.accountStatus = new AccountStatus();
        this.bankStmtDetailViews = new ArrayList<BankStmtDetailView>();
        this.averageIncomeGross = BigDecimal.valueOf(0.00);
        this.averageIncomeNetBDM = BigDecimal.valueOf(0.00);
        this.averageIncomeNetUW = BigDecimal.valueOf(0.00);
        this.averageDrawAmount = BigDecimal.valueOf(0.00);
        this.swingPercent = BigDecimal.valueOf(0.00);
        this.utilizationPercent = BigDecimal.valueOf(0.00);
        this.averageGrossInflowPerLimit = BigDecimal.valueOf(0.00);
        this.tradeChequeReturnAmount = BigDecimal.valueOf(0.00);
        this.remark = "";
    }

    public boolean isNotCountIncome() {
        return isNotCountIncome;
    }

    public void setNotCountIncome(boolean notCountIncome) {
        isNotCountIncome = notCountIncome;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public BankAccountType getOtherAccountType() {
        return otherAccountType;
    }

    public void setOtherAccountType(BankAccountType otherAccountType) {
        this.otherAccountType = otherAccountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getAccountCharId() {
        return accountCharId;
    }

    public void setAccountCharId(int accountCharId) {
        this.accountCharId = accountCharId;
    }

    public List<BankStmtDetailView> getBankStmtDetailViews() {
        return bankStmtDetailViews;
    }

    public void setBankStmtDetailViews(List<BankStmtDetailView> bankStmtDetailViews) {
        this.bankStmtDetailViews = bankStmtDetailViews;
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

    public int getChequeReturn() {
        return chequeReturn;
    }

    public void setChequeReturn(int chequeReturn) {
        this.chequeReturn = chequeReturn;
    }

    public BigDecimal getTradeChequeReturnAmount() {
        return tradeChequeReturnAmount;
    }

    public void setTradeChequeReturnAmount(BigDecimal tradeChequeReturnAmount) {
        this.tradeChequeReturnAmount = tradeChequeReturnAmount;
    }

    public int getOverLimitTimes() {
        return overLimitTimes;
    }

    public void setOverLimitTimes(int overLimitTimes) {
        this.overLimitTimes = overLimitTimes;
    }

    public int getOverLimitDays() {
        return overLimitDays;
    }

    public void setOverLimitDays(int overLimitDays) {
        this.overLimitDays = overLimitDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("isNotCountIncome", isNotCountIncome)
                .append("bank", bank)
                .append("branchName", branchName)
                .append("bankAccountType", bankAccountType)
                .append("otherAccountType", otherAccountType)
                .append("accountNo", accountNo)
                .append("accountName", accountName)
                .append("accountStatus", accountStatus)
                .append("accountCharId", accountCharId)
                .append("bankStmtDetailViews", bankStmtDetailViews)
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
                .toString();
    }
}
