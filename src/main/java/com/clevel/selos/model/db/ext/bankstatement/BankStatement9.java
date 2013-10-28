package com.clevel.selos.model.db.ext.bankstatement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ext_dwh_bankstatement9")
public class BankStatement9 implements Serializable {
    @Id
    private long id;

    @Column(name = "account_status", length = 2)
    private String accountStatus;

    @Column(name = "account_open_date")
    private Date accountOpenDate;

    @Column(name = "branch_code", length = 3)
    private String branchCode;

    @Column(name = "data_source", length = 2)
    private String dataSource;

    @Column(name = "account_number", length = 10)
    private String accountNumber;

    @Column(name = "account_name", length = 50)
    private String accountName;

    @Column(name = "od_limit", length = 16, scale = 2)
    private BigDecimal odLimit;

    @Column(name = "gross_credit_balance", length = 14, scale = 2)
    private BigDecimal grossCreditBalance;

    @Column(name = "credit_txn_number", length = 5)
    private Integer creditTXNNumber;

    @Column(name = "gross_debit_balance", length = 14, scale = 2)
    private BigDecimal grossDebitBalance;

    @Column(name = "debit_txn_number", length = 14, scale = 2)
    private Integer debitTXNNumber;

    @Column(name = "highest_balance_date")
    private Date highestBalanceDate;

    @Column(name = "highest_balance", length = 14, scale = 2)
    private BigDecimal highestBalance;

    @Column(name = "lowest_balance_date")
    private Date lowestBalanceDate;

    @Column(name = "lowest_balance", length = 14, scale = 2)
    private BigDecimal lowestBalance;

    @Column(name = "month_end_balance", length = 14, scale = 2)
    private BigDecimal monthEndBalance;

    @Column(name = "check_return_number", length = 5)
    private Integer checkReturnNumber;

    @Column(name = "check_return_amount", length = 14, scale = 2)
    private BigDecimal checkReturnAmount;

    @Column(name = "od_limit_number", length = 5)
    private Integer odLimitNumber;

    @Column(name = "start_od_date")
    private Date startOdDate;

    @Column(name = "end_od_date")
    private Date endOdDate;

    @Column(name = "as_of_date")
    private Date asOfDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public BigDecimal getOdLimit() {
        if (odLimit == null) {
            return BigDecimal.ZERO;
        }
        return odLimit;
    }

    public void setOdLimit(BigDecimal odLimit) {
        this.odLimit = odLimit;
    }

    public BigDecimal getGrossCreditBalance() {
        if (grossCreditBalance == null) {
            return BigDecimal.ZERO;
        }
        return grossCreditBalance;
    }

    public void setGrossCreditBalance(BigDecimal grossCreditBalance) {
        this.grossCreditBalance = grossCreditBalance;
    }

    public Integer getCreditTXNNumber() {
        if (creditTXNNumber == null) {
            return 0;
        }
        return creditTXNNumber;
    }

    public void setCreditTXNNumber(Integer creditTXNNumber) {
        this.creditTXNNumber = creditTXNNumber;
    }

    public BigDecimal getGrossDebitBalance() {
        if (grossDebitBalance == null) {
            return BigDecimal.ZERO;
        }
        return grossDebitBalance;
    }

    public void setGrossDebitBalance(BigDecimal grossDebitBalance) {
        this.grossDebitBalance = grossDebitBalance;
    }

    public Integer getDebitTXNNumber() {
        if (debitTXNNumber == null) {
            return 0;
        }
        return debitTXNNumber;
    }

    public void setDebitTXNNumber(Integer debitTXNNumber) {
        this.debitTXNNumber = debitTXNNumber;
    }

    public Date getHighestBalanceDate() {
        return highestBalanceDate;
    }

    public void setHighestBalanceDate(Date highestBalanceDate) {
        this.highestBalanceDate = highestBalanceDate;
    }

    public BigDecimal getHighestBalance() {
        if (highestBalance == null) {
            return BigDecimal.ZERO;
        }
        return highestBalance;
    }

    public void setHighestBalance(BigDecimal highestBalance) {
        this.highestBalance = highestBalance;
    }

    public Date getLowestBalanceDate() {
        return lowestBalanceDate;
    }

    public void setLowestBalanceDate(Date lowestBalanceDate) {
        this.lowestBalanceDate = lowestBalanceDate;
    }

    public BigDecimal getLowestBalance() {
        if (lowestBalance == null) {
            return BigDecimal.ZERO;
        }
        return lowestBalance;
    }

    public void setLowestBalance(BigDecimal lowestBalance) {
        this.lowestBalance = lowestBalance;
    }

    public BigDecimal getMonthEndBalance() {
        if (monthEndBalance == null) {
            return BigDecimal.ZERO;
        }
        return monthEndBalance;
    }

    public void setMonthEndBalance(BigDecimal monthEndBalance) {
        this.monthEndBalance = monthEndBalance;
    }

    public Integer getCheckReturnNumber() {
        if (checkReturnNumber == null) {
            return 0;
        }
        return checkReturnNumber;
    }

    public void setCheckReturnNumber(int checkReturnNumber) {
        this.checkReturnNumber = checkReturnNumber;
    }

    public BigDecimal getCheckReturnAmount() {
        if (checkReturnAmount == null) {
            return BigDecimal.ZERO;
        }
        return checkReturnAmount;
    }

    public void setCheckReturnAmount(BigDecimal checkReturnAmount) {
        this.checkReturnAmount = checkReturnAmount;
    }

    public Integer getOdLimitNumber() {
        if (odLimitNumber == null) {
            return 0;
        }
        return odLimitNumber;
    }

    public void setOdLimitNumber(int odLimitNumber) {
        this.odLimitNumber = odLimitNumber;
    }

    public Date getStartOdDate() {
        return startOdDate;
    }

    public void setStartOdDate(Date startOdDate) {
        this.startOdDate = startOdDate;
    }

    public Date getEndOdDate() {
        return endOdDate;
    }

    public void setEndOdDate(Date endOdDate) {
        this.endOdDate = endOdDate;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountStatus", accountStatus)
                .append("accountOpenDate", accountOpenDate)
                .append("branchCode", branchCode)
                .append("dataSource", dataSource)
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("odLimit", odLimit)
                .append("grossCreditBalance", grossCreditBalance)
                .append("creditTXNNumber", creditTXNNumber)
                .append("grossDebitBalance", grossDebitBalance)
                .append("debitTXNNumber", debitTXNNumber)
                .append("highestBalanceDate", highestBalanceDate)
                .append("highestBalance", highestBalance)
                .append("lowestBalanceDate", lowestBalanceDate)
                .append("lowestBalance", lowestBalance)
                .append("monthEndBalance", monthEndBalance)
                .append("checkReturnNumber", checkReturnNumber)
                .append("checkReturnAmount", checkReturnAmount)
                .append("odLimitNumber", odLimitNumber)
                .append("startOdDate", startOdDate)
                .append("endOdDate", endOdDate)
                .append("asOfDate", asOfDate)
                .toString();
    }
}
