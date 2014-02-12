package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class NewCreditTierDetailView implements Serializable {
    private long id;

    private int no;

    private BaseRate finalBasePrice;
    private String finalPriceRate;
    private BigDecimal finalInterest;

    private BaseRate standardBasePrice;
    private String standardPrice;
    private BigDecimal standardInterest;

    private BaseRate suggestBasePrice;
    private String suggestPrice;
    private BigDecimal suggestInterest;

    private BigDecimal installment;
    private int tenor;
    private boolean canEdit;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public NewCreditTierDetailView() {
        reset();
    }

    public void reset() {
        this.tenor = 0;
        this.installment = BigDecimal.ZERO;

        this.standardPrice = "";
        this.suggestPrice = "";
        this.finalPriceRate = "";

        this.standardInterest = BigDecimal.ZERO;
        this.suggestInterest = BigDecimal.ZERO;
        this.finalInterest = BigDecimal.ZERO;

        this.standardBasePrice =  new BaseRate();
        this.suggestBasePrice = new BaseRate();
        this.finalBasePrice = new BaseRate();
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
        this.no = no;
    }

    public BaseRate getFinalBasePrice() {
        return finalBasePrice;
    }

    public void setFinalBasePrice(BaseRate finalBasePrice) {
        this.finalBasePrice = finalBasePrice;
    }

    public String getFinalPriceRate() {
        return finalPriceRate;
    }

    public void setFinalPriceRate(String finalPriceRate) {
        this.finalPriceRate = finalPriceRate;
    }

    public BigDecimal getFinalInterest() {
        return finalInterest;
    }

    public void setFinalInterest(BigDecimal finalInterest) {
        this.finalInterest = finalInterest;
    }

    public BaseRate getStandardBasePrice() {
        return standardBasePrice;
    }

    public void setStandardBasePrice(BaseRate standardBasePrice) {
        this.standardBasePrice = standardBasePrice;
    }

    public String getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(String standardPrice) {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
    }

    public BaseRate getSuggestBasePrice() {
        return suggestBasePrice;
    }

    public void setSuggestBasePrice(BaseRate suggestBasePrice) {
        this.suggestBasePrice = suggestBasePrice;
    }

    public String getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(String suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public BigDecimal getSuggestInterest() {
        return suggestInterest;
    }

    public void setSuggestInterest(BigDecimal suggestInterest) {
        this.suggestInterest = suggestInterest;
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

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("no", no).
                append("finalBasePrice", finalBasePrice).
                append("finalPriceRate", finalPriceRate).
                append("finalInterest", finalInterest).
                append("standardBasePrice", standardBasePrice).
                append("standardPrice", standardPrice).
                append("standardInterest", standardInterest).
                append("suggestBasePrice", suggestBasePrice).
                append("suggestPrice", suggestPrice).
                append("suggestInterest", suggestInterest).
                append("installment", installment).
                append("tenor", tenor).
                append("canEdit", canEdit).
                append("createDate", createDate).
                append("modifyDate", modifyDate).
                append("createBy", createBy).
                append("modifyBy", modifyBy).
                toString();
    }
}
