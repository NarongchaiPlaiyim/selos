package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.RefRate;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CreditTierDetailView implements Serializable {
    private int no;
    private BigDecimal standardPrice;
    private RefRate standardBase;
    private BigDecimal standardAdd;
    private BigDecimal suggestPrice;
    private RefRate suggestBase;
    private BigDecimal suggestAdd;
    private BigDecimal finalPriceRate;
    private BigDecimal installment;
    private int tenor;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public CreditTierDetailView() {
        reset();
    }

    public void reset() {

        this.finalPriceRate = BigDecimal.ZERO;
        this.tenor = 0;
        this.installment = BigDecimal.ZERO;
        this.standardPrice = BigDecimal.ZERO;
        this.suggestPrice = BigDecimal.ZERO;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    public RefRate getStandardBase() {
        return standardBase;
    }

    public void setStandardBase(RefRate standardBase) {
        this.standardBase = standardBase;
    }

    public BigDecimal getStandardAdd() {
        return standardAdd;
    }

    public void setStandardAdd(BigDecimal standardAdd) {
        this.standardAdd = standardAdd;
    }

    public BigDecimal getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(BigDecimal suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public RefRate getSuggestBase() {
        return suggestBase;
    }

    public void setSuggestBase(RefRate suggestBase) {
        this.suggestBase = suggestBase;
    }

    public BigDecimal getSuggestAdd() {
        return suggestAdd;
    }

    public void setSuggestAdd(BigDecimal suggestAdd) {
        this.suggestAdd = suggestAdd;
    }

    public BigDecimal getFinalPriceRate() {
        return finalPriceRate;
    }

    public void setFinalPriceRate(BigDecimal finalPriceRate) {
        this.finalPriceRate = finalPriceRate;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("standardPrice", standardPrice)
                .append("standardBase", standardBase)
                .append("standardAdd", standardAdd)
                .append("suggestPrice", suggestPrice)
                .append("suggestBase", suggestBase)
                .append("suggestAdd", suggestAdd)
                .append("finalPriceRate", finalPriceRate)
                .append("installment", installment)
                .append("tenor", tenor)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
