package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_exist_credit_type_detail")
public class ExistingCreditTypeDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_CREDIT_TYPE_DET_ID", sequenceName = "SEQ_WRK_EXT_CREDIT_TYPE_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_CREDIT_TYPE_DET_ID")
    private long id;

    @Column(name = "no")
    private int no;

    @Column(name = "account")
    private String account;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_suff")
    private String accountSuf;

    @Column(name = "type")
    private String type;

    @Column(name = "request_type")
    private int requestType;

    @Column(name = "product_program")
    private String productProgram;

    @Column(name = "credit_facility")
    private String creditFacility;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "guarantee_amount")
    private BigDecimal guaranteeAmount;

    @Column(name = "use_count")
    private int useCount;

    @Column(name = "seq")
    private int seq;

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
    @JoinColumn(name = "existing_collateral_detail_id")
    private ExistingCollateralDetail existingCollateralDetail;

    @ManyToOne
    @JoinColumn(name = "existing_guarantor_detail_id")
    private ExistingGuarantorDetail existingGuarantorDetail;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public ExistingCollateralDetail getExistingCollateralDetail() {
        return existingCollateralDetail;
    }

    public void setExistingCollateralDetail(ExistingCollateralDetail existingCollateralDetail) {
        this.existingCollateralDetail = existingCollateralDetail;
    }

    public ExistingGuarantorDetail getExistingGuarantorDetail() {
        return existingGuarantorDetail;
    }

    public void setExistingGuarantorDetail(ExistingGuarantorDetail existingGuarantorDetail) {
        this.existingGuarantorDetail = existingGuarantorDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("account", account)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("accountSuf", accountSuf)
                .append("type", type)
                .append("requestType", requestType)
                .append("productProgram", productProgram)
                .append("creditFacility", creditFacility)
                .append("limit", limit)
                .append("guaranteeAmount", guaranteeAmount)
                .append("useCount", useCount)
                .append("seq", seq)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("existingCollateralDetail", existingCollateralDetail)
                .append("existingGuarantorDetail", existingGuarantorDetail)
                .toString();
    }
}
