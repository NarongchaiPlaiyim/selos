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
    private BigDecimal finalInterest;
    private String finalPriceLabel;

    private BaseRate standardBasePrice;
    private BigDecimal standardInterest;
    private String standardPriceLabel;

    private BaseRate suggestBasePrice;
    private BigDecimal suggestInterest;
    private String suggestPriceLabel;

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

        this.standardPriceLabel = "";
        this.suggestPriceLabel = "";
        this.finalPriceLabel = "";

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

    public String getFinalPriceLabel() {
        return finalPriceLabel;
    }

    public void setFinalPriceLabel(String finalPriceLabel) {
        this.finalPriceLabel = finalPriceLabel;
    }

    public String getStandardPriceLabel() {
        return standardPriceLabel;
    }

    public void setStandardPriceLabel(String standardPriceLabel) {
        this.standardPriceLabel = standardPriceLabel;
    }

    public String getSuggestPriceLabel() {
        return suggestPriceLabel;
    }

    public void setSuggestPriceLabel(String suggestPriceLabel) {
        this.suggestPriceLabel = suggestPriceLabel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("no", no).
                append("finalBasePrice", finalBasePrice).
                append("finalInterest", finalInterest).
                append("finalPriceLabel", finalPriceLabel).
                append("standardBasePrice", standardBasePrice).
                append("standardInterest", standardInterest).
                append("standardPriceLabel", standardPriceLabel).
                append("suggestBasePrice", suggestBasePrice).
                append("suggestInterest", suggestInterest).
                append("suggestPriceLabel", suggestPriceLabel).
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
