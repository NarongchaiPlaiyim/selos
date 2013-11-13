package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CreditTierDetailView implements Serializable {

    private BigDecimal finalPriceRate;
    private BigDecimal tenor;
    private BigDecimal installment;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public CreditTierDetailView() {
        reset();
    }

    public void reset() {

        this.finalPriceRate = BigDecimal.ZERO;
        this.tenor = BigDecimal.ZERO;
        this.installment = BigDecimal.ZERO;
    }

    public BigDecimal getFinalPriceRate() {
        return finalPriceRate;
    }

    public void setFinalPriceRate(BigDecimal finalPriceRate) {
        this.finalPriceRate = finalPriceRate;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
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
}
