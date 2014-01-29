package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.SettlementStatus;
import com.clevel.selos.model.db.master.TDRCondition;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NCBDetailView implements Serializable {
    private long id;
    private int isTMBAccount;   // NCBBizTransform use
    private int refinanceFlag;
    private boolean monthsPaymentFlag;    // check render
    private int wcFlag;
    private Date dateOfInfo;
    private Date accountOpenDate;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private Date dateOfDebtRestructuring;
    private BigDecimal noOfOutstandingPaymentIn12months;
    private int noOfOverLimit;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private TDRCondition tdrCondition;
    private SettlementStatus currentPayment;
    private SettlementStatus historyPayment;
    private int noOfmonthsPayment;
    private boolean canToEdit;
    private boolean monthFlagPage;
    private boolean TMB;
    private boolean refinance;
    private boolean wc;

    private String accountName;
    private LoanAccountTypeView loanAccountTypeView;
    private BigDecimal debtForCalculate;

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
        this.noOfOutstandingPaymentIn12months = BigDecimal.ZERO;
        this.noOfOverLimit = 0;
        this.monthsPaymentFlag = false;
        this.accountType = new AccountType();
        this.accountStatus = new AccountStatus();
        this.tdrCondition = new TDRCondition();
        this.currentPayment = new SettlementStatus();
        this.historyPayment = new SettlementStatus();
        this.noOfmonthsPayment = 0;
        this.canToEdit = false;
        this.monthFlagPage = false;
        this.loanAccountTypeView = new LoanAccountTypeView();
        this.accountName = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNoOfmonthsPayment() {
        return noOfmonthsPayment;
    }

    public void setNoOfmonthsPayment(int noOfmonthsPayment) {
        this.noOfmonthsPayment = noOfmonthsPayment;
    }

    public Date getDateOfInfo() {
        return dateOfInfo;
    }

    public void setDateOfInfo(Date dateOfInfo) {
        this.dateOfInfo = dateOfInfo;
    }

    public int getTMBAccount() {
        if (TMB == true) {
            this.isTMBAccount = 1;
        } else if (TMB == false) {
            this.isTMBAccount = 0;
        }
        return isTMBAccount;
    }

    public void setTMBAccount(int TMBAccount) {
        isTMBAccount = TMBAccount;
    }

    public boolean isTMB() {

        if (isTMBAccount == 1) {
            this.TMB = true;
        } else {
            this.TMB = false;
        }
        return TMB;
    }

    public void setTMB(boolean TMB) {
        this.TMB = TMB;
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

    public BigDecimal getNoOfOutstandingPaymentIn12months() {
        return noOfOutstandingPaymentIn12months;
    }

    public void setNoOfOutstandingPaymentIn12months(BigDecimal noOfOutstandingPaymentIn12months) {
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


    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }

    public SettlementStatus getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(SettlementStatus currentPayment) {
        this.currentPayment = currentPayment;
    }

    public SettlementStatus getHistoryPayment() {
        return historyPayment;
    }

    public void setHistoryPayment(SettlementStatus historyPayment) {
        this.historyPayment = historyPayment;
    }

    public boolean isRefinance() {
        if (refinanceFlag == 2) {
            this.refinance = true;
        } else {
            this.refinance = false;
        }
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public boolean isWc() {
        if (wcFlag == 2) {
            this.wc = true;
        } else {
            this.wc = false;
        }
        return wc;
    }

    public void setWc(boolean wc) {
        this.wc = wc;
    }


    public int getRefinanceFlag() {
        if (refinance == true) {
            refinanceFlag = 2;
        } else if (refinance == false) {
            refinanceFlag = 1;
        }
        return refinanceFlag;
    }

    public void setRefinanceFlag(int refinanceFlag) {
        this.refinanceFlag = refinanceFlag;
    }


    public int getWcFlag() {
        if (wc == true) {
            wcFlag = 2;
        } else if (wc == false) {
            wcFlag = 1;
        }
        return wcFlag;
    }

    public void setWcFlag(int wcFlag) {
        this.wcFlag = wcFlag;
    }

    public boolean isMonthsPaymentFlag() {
        if (this.noOfmonthsPayment != 0) {
            this.monthsPaymentFlag = true;
        } else {
            this.monthsPaymentFlag = false;
        }
        return monthsPaymentFlag;
    }

    public void setMonthsPaymentFlag(boolean monthsPaymentFlag) {
        this.monthsPaymentFlag = monthsPaymentFlag;
    }

    public boolean isCanToEdit() {
        return canToEdit;
    }

    public void setCanToEdit(boolean canToEdit) {
        this.canToEdit = canToEdit;
    }

    public boolean isMonthFlagPage() {
        if (accountType.getMonthFlag() == 1) {       // test before this the future 1 > true 0 > false
            monthFlagPage = false;
        } else {
            monthFlagPage = true;
        }

        return monthFlagPage;
    }

    public void setMonthFlagPage(boolean monthFlagPage) {
        this.monthFlagPage = monthFlagPage;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("isTMBAccount", isTMBAccount)
                .append("dateOfInfo", dateOfInfo)
                .append("accountOpenDate", accountOpenDate)
                .append("limit", limit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("dateOfDebtRestructuring", dateOfDebtRestructuring)
                .append("noOfOutstandingPaymentIn12months", noOfOutstandingPaymentIn12months)
                .append("noOfOverLimit", noOfOverLimit)
                .append("refinanceFlag", refinanceFlag)
                .append("monthsPaymentFlag", monthsPaymentFlag)
                .append("accountType", accountType)
                .append("accountStatus", accountStatus)
                .append("tdrCondition", tdrCondition)
                .append("currentPayment", currentPayment)
                .append("historyPayment", historyPayment)
                .append("noOfmonthsPayment", noOfmonthsPayment)
                .append("wcFlag", wcFlag)
                .toString();
    }
}
