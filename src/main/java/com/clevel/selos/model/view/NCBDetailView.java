package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NCBDetailView implements Serializable {
    private long id;
    private int isTMBAccount;   // NCBBizTransform use
    private int refinanceFlag;
    private int wcFlag;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private int noOfOutstandingPaymentIn12months;
    private int noOfOverLimit;
    private AccountType accountType;
    private AccountStatus accountStatus;
    //private TDRCondition tdrCondition;
    private SettlementStatusView currentPayment;
    private SettlementStatusView historyPayment;
    private boolean canToEdit;
    private boolean monthFlagPage;

    //TO SHOW CHECKBOX IN DATA TABLE
    private boolean tmbCheck;
    private boolean refinanceCheck;
    private boolean wcCheck;

    private long customerId;

    private String accountName;
    private LoanAccountTypeView loanAccountTypeView;
    private BigDecimal debtForCalculate;

    //ADD NEW COLUMN FOR BRMS
    private SettlementStatus historySixPayment;
    private SettlementStatus historyTwelvePayment;
    private int nplFlag;
    private BigDecimal nplCreditAmount;
    private int tdrFlag;

    private Date accountClosedDate;
    private Date nplInfoDate;

    public NCBDetailView() {
        reset();
    }

    public void reset() {
        this.dateOfInfo = new Date();
        this.accountOpenDate = new Date();
        this.limit = BigDecimal.ZERO;
        this.outstanding = BigDecimal.ZERO;
        this.installment = BigDecimal.ZERO;
        this.dateOfDebtRestructuring = new Date();
        this.noOfOutstandingPaymentIn12months = 0;
        this.noOfOverLimit = 0;
        this.accountType = new AccountType();
        this.accountStatus = new AccountStatus();
        //this.tdrCondition = new TDRCondition();
        this.currentPayment = new SettlementStatusView();
        this.historyPayment = new SettlementStatusView();
        this.canToEdit = false;
        this.monthFlagPage = false;
        this.loanAccountTypeView = new LoanAccountTypeView();
        this.accountName = "";

        this.historySixPayment = new SettlementStatus();
        this.historyTwelvePayment = new SettlementStatus();
        this.nplFlag = 0;
        this.nplCreditAmount = BigDecimal.ZERO;
        this.tdrFlag = 0;
        this.accountClosedDate =  null;
        this.nplInfoDate =  null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTMBAccount() {
        return isTMBAccount;
    }

    public void setTMBAccount(int TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public int getRefinanceFlag() {
        return refinanceFlag;
    }

    public void setRefinanceFlag(int refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }

    public int getWcFlag() {
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    public Date getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(Date dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
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

    public Date getDateOfDebtRestructuring() {
        return dateOfDebtRestructuring;
    }

    public void setDateOfDebtRestructuring(Date dateOfDebtRestructuring) {
        this.dateOfDebtRestructuring = dateOfDebtRestructuring;
    }

    public int getNoOfOutstandingPaymentIn12months() {
        return noOfOutstandingPaymentIn12months;
    }

    public void setNoOfOutstandingPaymentIn12months(int noOfOutstandingPaymentIn12months) {
        this.noOfOutstandingPaymentIn12months = noOfOutstandingPaymentIn12months;
    }

    public int getNoOfOverLimit() {
        return noOfOverLimit;
    }

    public void setNoOfOverLimit(int noOfOverLimit) {
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

    /*public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }*/

    public SettlementStatusView getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(SettlementStatusView currentPayment) {
        this.currentPayment = currentPayment;
    }

    public SettlementStatusView getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(SettlementStatusView historyPayment) {
        this.historyPayment = historyPayment;
    }

    public boolean isCanToEdit() {
        return canToEdit;
    }

    public void setCanToEdit(boolean canToEdit) {
        this.canToEdit = canToEdit;
    }

    public boolean isMonthFlagPage() {
        return monthFlagPage;
    }

    public void setMonthFlagPage(boolean monthFlagPage) {
        this.monthFlagPage = monthFlagPage;
    }

    public boolean getTmbCheck() {
        return tmbCheck;
    }

    public void setTmbCheck(boolean tmbCheck) {
        this.tmbCheck = tmbCheck;
    }

    public boolean getRefinanceCheck() {
        return refinanceCheck;
    }

    public void setRefinanceCheck(boolean refinanceCheck) {
        this.refinanceCheck = refinanceCheck;
    }

    public boolean getWcCheck() {
        return wcCheck;
    }

    public void setWcCheck(boolean wcCheck) {
        this.wcCheck = wcCheck;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LoanAccountTypeView getLoanAccountTypeView() {
        return loanAccountTypeView;
    }

    public void setLoanAccountTypeView(LoanAccountTypeView loanAccountTypeView) {
        this.loanAccountTypeView = loanAccountTypeView;
    }

    public BigDecimal getDebtForCalculate() {
        return debtForCalculate;
    }

    public void setDebtForCalculate(BigDecimal debtForCalculate) {
        this.debtForCalculate = debtForCalculate;
    }

    public SettlementStatus getHistorySixPayment() {
        return historySixPayment;
    }

    public void setHistorySixPayment(SettlementStatus historySixPayment) {
        this.historySixPayment = historySixPayment;
    }

    public SettlementStatus getHistoryTwelvePayment() {
        return historyTwelvePayment;
    }

    public void setHistoryTwelvePayment(SettlementStatus historyTwelvePayment) {
        this.historyTwelvePayment = historyTwelvePayment;
    }

    public int getNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(int nplFlag) {
        this.nplFlag = nplFlag;
    }

    public BigDecimal getNplCreditAmount() {
        return nplCreditAmount;
    }

    public void setNplCreditAmount(BigDecimal nplCreditAmount) {
        this.nplCreditAmount = nplCreditAmount;
    }

    public int getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(int tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public Date getAccountClosedDate() {
        return accountClosedDate;
    }

    public void setAccountClosedDate(Date accountClosedDate) {
        this.accountClosedDate = accountClosedDate;
    }

    public Date getNplInfoDate() {
        return nplInfoDate;
    }

    public void setNplInfoDate(Date nplInfoDate) {
        this.nplInfoDate = nplInfoDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isTMBAccount", isTMBAccount)
                .append("refinanceFlag", refinanceFlag)
                .append("wcFlag", wcFlag)
                .append("dateOfInfo", dateOfInfo)
                .append("accountOpenDate", accountOpenDate)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("dateOfDebtRestructuring", dateOfDebtRestructuring)
                .append("noOfOutstandingPaymentIn12months", noOfOutstandingPaymentIn12months)
                .append("noOfOverLimit", noOfOverLimit)
                .append("accountType", accountType)
                .append("accountStatus", accountStatus)
                //.append("tdrCondition", tdrCondition)
                .append("currentPayment", currentPayment)
                .append("historyPayment", historyPayment)
                .append("canToEdit", canToEdit)
                .append("monthFlagPage", monthFlagPage)
                .append("tmbCheck", tmbCheck)
                .append("refinanceCheck", refinanceCheck)
                .append("wcCheck", wcCheck)
                .append("customerId", customerId)
                .append("accountName", accountName)
                .append("loanAccountTypeView", loanAccountTypeView)
                .append("debtForCalculate", debtForCalculate)
                .append("accountClosedDate", accountClosedDate)
                .append("nplInfoDate", nplInfoDate)
                .toString();
    }
}
