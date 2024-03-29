package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExistingCreditTierDetailView implements Serializable {
    private long id;
    private int no;
    private BigDecimal finalInterest;
    private BaseRateView finalBasePrice;
    private String finalPriceRate;
    private BigDecimal installment;
    private BaseRateView standardBasePrice;
    private String standardPrice;
    private BigDecimal standardInterest;
    private BaseRateView suggestBasePrice;
    private String suggestPrice;
    private BigDecimal suggestInterest;
    private int tenor;
    private boolean canEdit;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public ExistingCreditTierDetailView() {
        reset();
    }

    public void reset() {
        this.standardBasePrice =  new BaseRateView();
        this.suggestBasePrice = new BaseRateView();
        this.finalBasePrice = new BaseRateView();
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

    public String getFinalPriceRate() {
        return finalPriceRate;
    }

    public void setFinalPriceRate(String finalPriceRate) {
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

    public BigDecimal getFinalInterest() {
        return finalInterest;
    }

    public void setFinalInterest(BigDecimal finalInterest) {
        this.finalInterest = finalInterest;
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

    public BaseRateView getFinalBasePrice() {
        return finalBasePrice;
    }

    public void setFinalBasePrice(BaseRateView finalBasePrice) {
        this.finalBasePrice = finalBasePrice;
    }

    public BaseRateView getStandardBasePrice() {
        return standardBasePrice;
    }

    public void setStandardBasePrice(BaseRateView standardBasePrice) {
        this.standardBasePrice = standardBasePrice;
    }

    public BaseRateView getSuggestBasePrice() {
        return suggestBasePrice;
    }

    public void setSuggestBasePrice(BaseRateView suggestBasePrice) {
        this.suggestBasePrice = suggestBasePrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("finalInterest", finalInterest)
                .append("finalBasePrice", finalBasePrice)
                .append("finalPriceRate", finalPriceRate)
                .append("installment", installment)
                .append("standardBasePrice", standardBasePrice)
                .append("standardPrice", standardPrice)
                .append("standardInterest", standardInterest)
                .append("suggestBasePrice", suggestBasePrice)
                .append("suggestPrice", suggestPrice)
                .append("suggestInterest", suggestInterest)
                .append("tenor", tenor)
                .append("canEdit", canEdit)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
