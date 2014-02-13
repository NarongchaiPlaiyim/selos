package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class  ProposeCreditDetailView implements Serializable {
    private int id;
    private int seq;
    private boolean noFlag;
    private String typeOfStep;
    private String accountName;
    private String accountNumber;
    private String accountSuf;
    private int requestType;
    private ProductProgram productProgram;
    private CreditType creditFacility;
    private BigDecimal limit;
    private BigDecimal guaranteeAmount;
    private int useCount;


    public  ProposeCreditDetailView(){
        reset();
    }

    public void reset(){
        this.typeOfStep = "";
        this.accountName = "";
        this.accountNumber = "";
        this.accountSuf = "";
        this.requestType = 0;
        this.productProgram = new ProductProgram();
        this.creditFacility = new CreditType();
        this.limit = BigDecimal.ZERO;
        this.guaranteeAmount = BigDecimal.ZERO;
        this.noFlag = false;
        this.useCount=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(CreditType creditFacility) {
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

    public String getTypeOfStep() {
        return typeOfStep;
    }

    public void setTypeOfStep(String typeOfStep) {
        this.typeOfStep = typeOfStep;
    }

    public boolean isNoFlag() {
        return noFlag;
    }

    public void setNoFlag(boolean noFlag) {
        this.noFlag = noFlag;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("seq", seq)
                .append("noFlag", noFlag)
                .append("typeOfStep", typeOfStep)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("accountSuf", accountSuf)
                .append("requestType", requestType)
                .append("productProgram", productProgram)
                .append("creditFacility", creditFacility)
                .append("limit", limit)
                .append("guaranteeAmount", guaranteeAmount)
                .toString();
    }
}