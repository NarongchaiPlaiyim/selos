package com.clevel.selos.model.view;

import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.CreditRelationType;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExistingCreditDetailView implements Serializable {

    private long id;
    private int no;
    private int seq;
    private boolean noFlag;
    private String stage;
    private String accountName;
    private String accountSuf;
    private String accountNumber;
    private int accountStatusID;
    private BankAccountStatusView accountStatus;

    private AccountStatus existAccountStatus;
    private String productProgram;
    private ProductProgram existProductProgram;
    private String creditType;
    private CreditType existCreditType;
    private String productCode;
    private String projectCode;
    private CreditCategory creditCategory;
    private CreditRelationType creditRelationType;
    private BigDecimal limit;
    private BigDecimal notional;
    private BigDecimal pcePercent;
    private BigDecimal pceLimit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private BigDecimal intFeePercent;
    private String source;
    private BigDecimal tenor;
    private String accountRef;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
    private List<ExistingSplitLineDetailView> existingSplitLineDetailViewList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isNoFlag() {
        return noFlag;
    }

    public void setNoFlag(boolean noFlag) {
        this.noFlag = noFlag;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountSuf() {
        return accountSuf;
    }

    public void setAccountSuf(String accountSuf) {
        this.accountSuf = accountSuf;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountStatusID() {
        return accountStatusID;
    }

    public void setAccountStatusID(int accountStatusID) {
        this.accountStatusID = accountStatusID;
    }

    public BankAccountStatusView getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(BankAccountStatusView accountStatus) {
        this.accountStatus = accountStatus;
    }

    public AccountStatus getExistAccountStatus() {
        return existAccountStatus;
    }

    public void setExistAccountStatus(AccountStatus existAccountStatus) {
        this.existAccountStatus = existAccountStatus;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public ProductProgram getExistProductProgram() {
        return existProductProgram;
    }

    public void setExistProductProgram(ProductProgram existProductProgram) {
        this.existProductProgram = existProductProgram;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public CreditType getExistCreditType() {
        return existCreditType;
    }

    public void setExistCreditType(CreditType existCreditType) {
        this.existCreditType = existCreditType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public CreditCategory getCreditCategory() {
        return creditCategory;
    }

    public void setCreditCategory(CreditCategory creditCategory) {
        this.creditCategory = creditCategory;
    }

    public CreditRelationType getCreditRelationType() {
        return creditRelationType;
    }

    public void setCreditRelationType(CreditRelationType creditRelationType) {
        this.creditRelationType = creditRelationType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getNotional() {
        return notional;
    }

    public void setNotional(BigDecimal notional) {
        this.notional = notional;
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

    public BigDecimal getIntFeePercent() {
        return intFeePercent;
    }

    public void setIntFeePercent(BigDecimal intFeePercent) {
        this.intFeePercent = intFeePercent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
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

    public List<ExistingCreditTierDetailView> getExistingCreditTierDetailViewList() {
        return existingCreditTierDetailViewList;
    }

    public void setExistingCreditTierDetailViewList(List<ExistingCreditTierDetailView> existingCreditTierDetailViewList) {
        this.existingCreditTierDetailViewList = existingCreditTierDetailViewList;
    }

    public List<ExistingSplitLineDetailView> getExistingSplitLineDetailViewList() {
        return existingSplitLineDetailViewList;
    }

    public void setExistingSplitLineDetailViewList(List<ExistingSplitLineDetailView> existingSplitLineDetailViewList) {
        this.existingSplitLineDetailViewList = existingSplitLineDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("seq", seq)
                .append("noFlag", noFlag)
                .append("accountName", accountName)
                .append("accountSuf", accountSuf)
                .append("accountNumber", accountNumber)
                .append("accountStatusID", accountStatusID)
                .append("accountStatus", accountStatus)
                .append("existAccountStatus", existAccountStatus)
                .append("productProgram", productProgram)
                .append("existProductProgram", existProductProgram)
                .append("creditType", creditType)
                .append("existCreditType", existCreditType)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("creditCategory", creditCategory)
                .append("creditRelationType", creditRelationType)
                .append("limit", limit)
                .append("notional", notional)
                .append("pcePercent", pcePercent)
                .append("pceLimit", pceLimit)
                .append("outstanding", outstanding)
                .append("installment", installment)
                .append("intFeePercent", intFeePercent)
                .append("source", source)
                .append("tenor", tenor)
                .append("accountRef", accountRef)
                .append("existingCreditTierDetailViewList", existingCreditTierDetailViewList)
                .append("existingSplitLineDetailViewList", existingSplitLineDetailViewList)
                .toString();
    }
}
