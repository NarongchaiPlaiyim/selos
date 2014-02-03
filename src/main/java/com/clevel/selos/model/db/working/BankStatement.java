package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_bankstatement")
public class BankStatement implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BANKSTMT_ID", sequenceName = "SEQ_WRK_BANKSTMT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BANKSTMT_ID")
    private long id;

    @Column(name = "is_not_count_income", length = 1, nullable = false, columnDefinition = "int default 0")
    private int isNotCountIncome;

    @OneToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "branch_name")
    private String branch;

    @OneToOne
    @JoinColumn(name = "bank_account_type_id")
    private BankAccountType bankAccountType;

    @Column(name = "account_number")
    private String accountNo;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "other_account_type")
    private int otherAccountType;

    @OneToOne
    @JoinColumn(name = "account_status_id")
    private AccountStatus accountStatus;

    @Column(name = "main_account")
    private int mainAccount;

    @Column(name = "account_character")
    private int accountCharacteristic;

    @Column(name = "is_tmb")
    private String isTMB;

    @Column(name = "is_highest_inflow")
    private String isHighestInflow;

    @Column(name = "limit")
    private BigDecimal avgLimit;

    @Column(name = "avg_income_gross")
    private BigDecimal avgIncomeGross;

    @Column(name = "avg_income_bdm")
    private BigDecimal avgIncomeNetBDM;

    @Column(name = "avg_income_uw")
    private BigDecimal avgIncomeNetUW;

    @Column(name = "avg_withdraw_amount")
    private BigDecimal avgDrawAmount;

    @Column(name = "avg_swing_percent")
    private BigDecimal avgSwingPercent;

    @Column(name = "avg_utilization_percent")
    private BigDecimal avgUtilizationPercent;

    @Column(name = "avg_gross_inflow_per_limit")
    private BigDecimal avgGrossInflowPerLimit;

    @Column(name = "avg_os_balance_amount")
    private BigDecimal avgOSBalanceAmount;

    @Column(name = "td_chq_return_times")
    private BigDecimal chequeReturn;

    @Column(name = "td_chq_return_amount")
    private BigDecimal tdChequeReturnAmount;

    @Column(name = "td_chq_return_percent")
    private BigDecimal tdChequeReturnPercent;

    @Column(name = "over_limit_times")
    private BigDecimal overLimitTimes;

    @Column(name = "over_limit_days")
    private BigDecimal overLimitDays;

    @Column(name = "remark")
    private String remark;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @ManyToOne
    @JoinColumn(name = "bank_stmt_summary_id")
    private BankStatementSummary bankStatementSummary;

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL)
    private List<BankStatementDetail> bankStatementDetailList;

    @OneToMany(mappedBy = "bankStatement", cascade = CascadeType.ALL)
    private List<BankStmtSrcOfCollateralProof> srcOfCollateralProofList;

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

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
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

    public int getOtherAccountType() {
        return otherAccountType;
    }

    public void setOtherAccountType(int otherAccountType) {
        this.otherAccountType = otherAccountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(int mainAccount) {
        this.mainAccount = mainAccount;
    }

    public int getAccountCharacteristic() {
        return accountCharacteristic;
    }

    public void setAccountCharacteristic(int accountCharacteristic) {
        this.accountCharacteristic = accountCharacteristic;
    }

    public String getTMB() {
        return isTMB;
    }

    public void setTMB(String TMB) {
        isTMB = TMB;
    }

    public String getHighestInflow() {
        return isHighestInflow;
    }

    public void setHighestInflow(String highestInflow) {
        isHighestInflow = highestInflow;
    }

    public BigDecimal getAvgLimit() {
        return avgLimit;
    }

    public void setAvgLimit(BigDecimal avgLimit) {
        this.avgLimit = avgLimit;
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

    public BigDecimal getAvgDrawAmount() {
        return avgDrawAmount;
    }

    public void setAvgDrawAmount(BigDecimal avgDrawAmount) {
        this.avgDrawAmount = avgDrawAmount;
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

    public BigDecimal getTdChequeReturnAmount() {
        return tdChequeReturnAmount;
    }

    public void setTdChequeReturnAmount(BigDecimal tdChequeReturnAmount) {
        this.tdChequeReturnAmount = tdChequeReturnAmount;
    }

    public BigDecimal getTdChequeReturnPercent() {
        return tdChequeReturnPercent;
    }

    public void setTdChequeReturnPercent(BigDecimal tdChequeReturnPercent) {
        this.tdChequeReturnPercent = tdChequeReturnPercent;
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

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public List<BankStatementDetail> getBankStatementDetailList() {
        return bankStatementDetailList;
    }

    public void setBankStatementDetailList(List<BankStatementDetail> bankStatementDetailList) {
        this.bankStatementDetailList = bankStatementDetailList;
    }

    public BankStatementSummary getBankStatementSummary() {
        return bankStatementSummary;
    }

    public void setBankStatementSummary(BankStatementSummary bankStatementSummary) {
        this.bankStatementSummary = bankStatementSummary;
    }

    public BigDecimal getAvgOSBalanceAmount() {
        return avgOSBalanceAmount;
    }

    public void setAvgOSBalanceAmount(BigDecimal avgOSBalanceAmount) {
        this.avgOSBalanceAmount = avgOSBalanceAmount;
    }

    public List<BankStmtSrcOfCollateralProof> getSrcOfCollateralProofList() {
        return srcOfCollateralProofList;
    }

    public void setSrcOfCollateralProofList(List<BankStmtSrcOfCollateralProof> srcOfCollateralProofList) {
        this.srcOfCollateralProofList = srcOfCollateralProofList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("bank", bank)
                .append("branch", branch)
                .append("bankAccountType", bankAccountType)
                .append("accountNo", accountNo)
                .append("accountName", accountName)
                .append("otherAccountType", otherAccountType)
                .append("accountStatus", accountStatus)
                .append("mainAccount", mainAccount)
                .append("accountCharacteristic", accountCharacteristic)
                .append("isTMB", isTMB)
                .append("isHighestInflow", isHighestInflow)
                .append("avgLimit", avgLimit)
                .append("avgIncomeGross", avgIncomeGross)
                .append("avgIncomeNetBDM", avgIncomeNetBDM)
                .append("avgIncomeNetUW", avgIncomeNetUW)
                .append("avgDrawAmount", avgDrawAmount)
                .append("avgSwingPercent", avgSwingPercent)
                .append("avgUtilizationPercent", avgUtilizationPercent)
                .append("avgGrossInflowPerLimit", avgGrossInflowPerLimit)
                .append("avgOSBalanceAmount", avgOSBalanceAmount)
                .append("chequeReturn", chequeReturn)
                .append("tdChequeReturnAmount", tdChequeReturnAmount)
                .append("tdChequeReturnPercent", tdChequeReturnPercent)
                .append("overLimitTimes", overLimitTimes)
                .append("overLimitDays", overLimitDays)
                .append("remark", remark)
                .append("createBy", createBy)
                .append("createDate", createDate)
                .append("modifyBy", modifyBy)
                .append("modifyDate", modifyDate)
                .append("bankStatementSummary", bankStatementSummary)
                .append("bankStatementDetailList", bankStatementDetailList)
                .append("srcOfCollateralProofList", srcOfCollateralProofList)
                .toString();
    }
}
