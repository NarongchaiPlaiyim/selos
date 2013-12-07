package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class  CreditTypeDetailView implements Serializable {
    private long id;
    private int no;
    private boolean noFlag;
    private String account;
    private String Type;
    private int requestType;
    private String productProgram;
    private String creditFacility;
    private BigDecimal  limit;
    private BigDecimal  guaranteeAmount;
    private int usaCount;
    private int seq;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private BigDecimal purpose;

    public CreditTypeDetailView() {
        reset();
    }



    public void reset() {
        this.id = 0;
        this.no = 0;
        this.account = "";
        this.Type  = "";
        this.requestType = 0;
        this.productProgram = "";
        this.creditFacility  = "";
        this.limit = BigDecimal.ZERO;
        this.guaranteeAmount = BigDecimal.ZERO;
        this.usaCount = 0;
        this.seq = 0;
        this.noFlag = false;
        this.purpose = BigDecimal.ZERO;
    }

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
        if(this.noFlag){
           this.no = 1;
        }else{
           this.no = 0;
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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


    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getUsaCount() {
        return usaCount;
    }

    public void setUsaCount(int usaCount) {
        this.usaCount = usaCount;
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

    public BigDecimal getPurpose() {
        return purpose;
    }

    public void setPurpose(BigDecimal purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("account", account)
                .append("Type", Type)
                .append("productProgram", productProgram)
                .append("creditFacility", creditFacility)
                .append("limit", limit)
                .toString();
    }
}