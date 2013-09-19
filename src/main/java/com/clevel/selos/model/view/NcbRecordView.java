package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 6/9/2556
 * Time: 15:31 น.
 * To change this template use File | Settings | File Templates.
 */
public class NcbRecordView {

    private String isTMBAccount;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String noOfOutstandingPaymentIn12months;
    private String noOfOverLimit;
    private String refinanceFlag;
    private boolean monthsPaymentFlag;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private SettlementStatus settlementStatus;
    private TDRCondition currentPayment;
    private TDRCondition historyPayment;
    private List<MonthsPaymentView> noOfmonthsPaymentList;
    private int noOfmonthsPayment;
    private List<MoneyPaymentView> moneyPaymentViewList;
    private String moneyTotal;


    public NcbRecordView() {
        reset();
    }

    public void reset() {
        this.isTMBAccount = "";
        this.dateOfInfo = new Date();
        this.accountOpenDate = new Date();
        this.limit = new BigDecimal(0);
        this.outstanding = new BigDecimal(0);
        this.installment = new BigDecimal(0);
        this.dateOfDebtRestructuring = new Date();
        this.typeOfCurrentPayment = "";
        this.typeOfHistoryPayment = "";
        this.noOfOutstandingPaymentIn12months = "";
        this.noOfOverLimit = "";
        this.refinanceFlag = "";
        this.monthsPaymentFlag = true;
        this.accountType = new AccountType();
        this.accountStatus = new AccountStatus();
        this.settlementStatus = new SettlementStatus();
        this.currentPayment = new TDRCondition();
        this.historyPayment = new TDRCondition();
        this.noOfmonthsPaymentList = new ArrayList<MonthsPaymentView>();
        this.noOfmonthsPayment = 0;
        this.moneyPaymentViewList = new ArrayList<MoneyPaymentView>();
        this.moneyTotal = "";

        noOfmonthsPaymentList = new ArrayList<MonthsPaymentView>();
        for (int i = 1; i < 13; i++) {
            noOfmonthsPaymentList.add(new MonthsPaymentView(i));
        }
        moneyPaymentViewList = new ArrayList<MoneyPaymentView>();

    }

    public String getMoneyTotal() {
        return moneyTotal;
    }

    public void setMoneyTotal(String moneyTotal) {
        this.moneyTotal = moneyTotal;
    }

    public List<MoneyPaymentView> getMoneyPaymentViewList() {
        return moneyPaymentViewList;
    }

    public void setMoneyPaymentViewList(List<MoneyPaymentView> moneyPaymentViewList) {
        this.moneyPaymentViewList = moneyPaymentViewList;
    }

    public List<MonthsPaymentView> getNoOfmonthsPaymentList() {
        return noOfmonthsPaymentList;
    }

    public void setNoOfmonthsPaymentList(List<MonthsPaymentView> noOfmonthsPaymentList) {
        this.noOfmonthsPaymentList = noOfmonthsPaymentList;
    }

    public int getNoOfmonthsPayment() {
        return noOfmonthsPayment;
    }

    public void setNoOfmonthsPayment(int noOfmonthsPayment) {
        this.noOfmonthsPayment = noOfmonthsPayment;
    }

    public boolean isMonthsPaymentFlag() {
        return monthsPaymentFlag;
    }

    public void setMonthsPaymentFlag(boolean monthsPaymentFlag) {
        this.monthsPaymentFlag = monthsPaymentFlag;
    }

    public Date getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(Date dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
    }


    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public Date getDateOfDebtRestructuring() {
        return dateOfDebtRestructuring;
    }

    public void setDateOfDebtRestructuring(Date dateOfDebtRestructuring) {
        this.dateOfDebtRestructuring = dateOfDebtRestructuring;
    }

    public String getTypeOfCurrentPayment() {
        return typeOfCurrentPayment;
    }

    public void setTypeOfCurrentPayment(String typeOfCurrentPayment) {
        this.typeOfCurrentPayment = typeOfCurrentPayment;
    }

    public String getTypeOfHistoryPayment() {
        return typeOfHistoryPayment;
    }

    public void setTypeOfHistoryPayment(String typeOfHistoryPayment) {
        this.typeOfHistoryPayment = typeOfHistoryPayment;
    }

    public String getNoOfOutstandingPaymentIn12months() {
        return noOfOutstandingPaymentIn12months;
    }

    public void setNoOfOutstandingPaymentIn12months(String noOfOutstandingPaymentIn12months) {
        this.noOfOutstandingPaymentIn12months = noOfOutstandingPaymentIn12months;
    }

    public String getNoOfOverLimit() {
        return noOfOverLimit;
    }

    public void setNoOfOverLimit(String noOfOverLimit) {
        this.noOfOverLimit = noOfOverLimit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public SettlementStatus getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public TDRCondition getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(TDRCondition currentPayment) {
        this.currentPayment = currentPayment;
    }

    public TDRCondition getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(TDRCondition historyPayment) {
        this.historyPayment = historyPayment;
    }

    public String getTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(String TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public String getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(String refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("isTMBAccount", isTMBAccount)
                .append("dateOfInfo", dateOfInfo)
                .append("accountOpenDate", accountOpenDate)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("dateOfDebtRestructuring", dateOfDebtRestructuring)
                .append("typeOfCurrentPayment", typeOfCurrentPayment)
                .append("typeOfHistoryPayment", typeOfHistoryPayment)
                .append("noOfOutstandingPaymentIn12months", noOfOutstandingPaymentIn12months)
                .append("noOfOverLimit", noOfOverLimit)
                .append("refinanceFlag", refinanceFlag)
                .append("monthsPaymentFlag", monthsPaymentFlag)
                .append("accountType", accountType)
                .append("accountStatus", accountStatus)
                .append("settlementStatus", settlementStatus)
                .append("currentPayment", currentPayment)
                .append("historyPayment", historyPayment)
                .append("noOfmonthsPaymentList", noOfmonthsPaymentList)
                .append("noOfmonthsPayment", noOfmonthsPayment)
                .append("moneyPaymentViewList", moneyPaymentViewList)
                .toString();
    }
}
