package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_dbr_detail")
public class DBRDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_DBR_DETAIL_ID", sequenceName="SEQ_WRK_DBR_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_DBR_DETAIL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="dbr_id")
    private DBR dbr;

    @Column(name = "active")
    private boolean active;

    @Column(name = "account_name" , length = 100)
    private String accountName;

    @OneToOne
    @JoinColumn(name = "loan_type_id")
    private AccountType loanType;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "installment")
    private BigDecimal installment;

    @Column(name = "debt_for_calculate")
    private BigDecimal debtForCalculate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public DBRDetail(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DBR getDbr() {
        return dbr;
    }

    public void setDbr(DBR dbr) {
        this.dbr = dbr;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getDebtForCalculate() {
        return debtForCalculate;
    }

    public void setDebtForCalculate(BigDecimal debtForCalculate) {
        this.debtForCalculate = debtForCalculate;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AccountType getLoanType() {
        return loanType;
    }

    public void setLoanType(AccountType loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("dbr", dbr)
                .append("active", active)
                .append("accountName", accountName)
                .append("loanType", loanType)
                .append("limit", limit)
                .append("installment", installment)
                .append("debtForCalculate", debtForCalculate)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
