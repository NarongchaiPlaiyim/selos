package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_existing_credit_detail")
public class ExistingCreditDetail {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXISTING_CREDIT_DET_ID", sequenceName = "SEQ_WRK_EXISTING_CREDIT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXISTING_CREDIT_DET_ID")
    private long id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_suf")
    private String accountSuf;

    @Column(name = "product_group")
    private String productGroup;

    @Column(name = "product_program")
    private String productProgram;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "credit_type")
    private String creditType;

    @Column(name = "credit_category")
    private int creditCategory;

    @Column(name = "credit_relation_type")
    private int creditRelationType;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "pce_percent")
    private BigDecimal pcePercent;

    @Column(name = "pce_limit")
    private BigDecimal pceLimit;

    @Column(name = "outstanding")
    private BigDecimal outstanding;

    @Column(name = "installment")
    private BigDecimal installment;

    @Column(name = "int_fee")
    private BigDecimal intFee;

    @Column(name = "tenor")
    private BigDecimal tenor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "existing_credit_id")
    private ExistingCreditSummary existingCreditSummary;

    @OneToOne
    @JoinColumn(name = "account_status_id")
    private BankAccountStatus accountstatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountSuf() {
        return accountSuf;
    }

    public void setAccountSuf(String accountSuf) {
        this.accountSuf = accountSuf;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public int getCreditCategory() {
        return creditCategory;
    }

    public void setCreditCategory(int creditCategory) {
        this.creditCategory = creditCategory;
    }

    public int getCreditRelationType() {
        return creditRelationType;
    }

    public void setCreditRelationType(int creditRelationType) {
        this.creditRelationType = creditRelationType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getPcePercent() {
        return pcePercent;
    }

    public void setPcePercent(BigDecimal pcePercent) {
        this.pcePercent = pcePercent;
    }

    public BigDecimal getPceLimit() {
        return pceLimit;
    }

    public void setPceLimit(BigDecimal pceLimit) {
        this.pceLimit = pceLimit;
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

    public BigDecimal getIntFee() {
        return intFee;
    }

    public void setIntFee(BigDecimal intFee) {
        this.intFee = intFee;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
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

    public ExistingCreditSummary getExistingCreditSummary() {
        return existingCreditSummary;
    }

    public void setExistingCreditSummary(ExistingCreditSummary existingCreditSummary) {
        this.existingCreditSummary = existingCreditSummary;
    }

    public BankAccountStatus getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(BankAccountStatus accountstatus) {
        this.accountstatus = accountstatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("accountSuf", accountSuf)
                .append("productGroup", productGroup)
                .append("productProgram", productProgram)
                .append("projectCode", projectCode)
                .append("productCode", productCode)
                .append("creditType", creditType)
                .append("creditCategory", creditCategory)
                .append("limit", limit)
                .append("pcePercent", pcePercent)
                .append("pceLimit", pceLimit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("intFee", intFee)
                .append("tenor", tenor)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
