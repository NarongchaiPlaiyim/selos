package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CreditTierDetailView implements Serializable {
    private int no;
    private BaseRate finalBase;
    private BigDecimal finalPriceRate;
    private BigDecimal installment;
    private BaseRate suggestBase;
    private BigDecimal suggestPrice;
    private BaseRate standardBase;
    private BigDecimal standardPrice;
    private int tenor;
    private boolean canEdit;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public CreditTierDetailView() {
        reset();
    }

    public void reset() {
        this.finalBase = new BaseRate();
        this.finalPriceRate = BigDecimal.ZERO;
        this.tenor = 0;
        this.installment = BigDecimal.ZERO;
        this.standardBase = new BaseRate();
        this.standardPrice = BigDecimal.ZERO;
        this.suggestBase = new BaseRate();
        this.suggestPrice = BigDecimal.ZERO;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public BaseRate getFinalBase() {
        return finalBase;
    }

    public void setFinalBase(BaseRate finalBase) {
        this.finalBase = finalBase;
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

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public BaseRate getSuggestBase() {
        return suggestBase;
    }

    public void setSuggestBase(BaseRate suggestBase) {
        this.suggestBase = suggestBase;
    }

    public BigDecimal getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(BigDecimal suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BaseRate getStandardBase() {
        return standardBase;
    }

    public void setStandardBase(BaseRate standardBase) {
        this.standardBase = standardBase;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(BigDecimal standardPrice) {
        this.standardPrice = standardPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
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
