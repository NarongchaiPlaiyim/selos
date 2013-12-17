package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BaPaInfoView implements Serializable{

    private int approvedType;
    private int applyBA;
    private int baPaymentMethod;
    private List<ApplyBaInfoView> ba;
    private List<BaPaInfoAddView> bapa;
    private BigDecimal totalLimit;
    private BigDecimal totalPremium;
    private BigDecimal totalMorePay;
    private String insureName;
    private long insureAccountNumber;




    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public int getApplyBA() {
        return applyBA;
    }

    public void setApplyBA(int applyBA) {
        this.applyBA = applyBA;
    }

    public int getBaPaymentMethod() {
        return baPaymentMethod;
    }

    public void setBaPaymentMethod(int baPaymentMethod) {
        this.baPaymentMethod = baPaymentMethod;
    }

    public List<ApplyBaInfoView> getBa() {
        return ba;
    }

    public void setBa(List<ApplyBaInfoView> ba) {
        this.ba = ba;
    }

    public List<BaPaInfoAddView> getBapa() {
        return bapa;
    }

    public void setBapa(List<BaPaInfoAddView> bapa) {
        this.bapa = bapa;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(BigDecimal totalPremium) {
        this.totalPremium = totalPremium;
    }

    public BigDecimal getTotalMorePay() {
        return totalMorePay;
    }

    public void setTotalMorePay(BigDecimal totalMorePay) {
        this.totalMorePay = totalMorePay;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public long getInsureAccountNumber() {
        return insureAccountNumber;
    }

    public void setInsureAccountNumber(long insureAccountNumber) {
        this.insureAccountNumber = insureAccountNumber;
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("approvedType", approvedType)
                .append("applyBA", applyBA)
                .append("baPaymentMethod", baPaymentMethod)
                .append("ba", ba)
                .append("bapa", bapa)
                .append("totalLimit", totalLimit)
                .append("totalPremium", totalPremium)
                .append("totalMorePay", totalMorePay)
                .append("insureName", insureName)
                .append("insureAccountNumber", insureAccountNumber)
                .toString();
    }
}
