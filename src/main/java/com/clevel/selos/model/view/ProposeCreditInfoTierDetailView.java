package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProposeCreditInfoTierDetailView implements Serializable {
    private long id;
    private BaseRateView finalBasePrice;
    private BigDecimal finalInterest;
    private BigDecimal finalInterestOriginal;
    private String finalPriceLabel;
    private BaseRateView standardBasePrice;
    private BigDecimal standardInterest;
    private String standardPriceLabel;
    private BaseRateView suggestBasePrice;
    private BigDecimal suggestInterest;
    private String suggestPriceLabel;
    private BigDecimal installment;
    private BigDecimal installmentOriginal;
    private int tenor;
    private int no;
    private int brmsFlag;

    public ProposeCreditInfoTierDetailView() {
        reset();
    }

    public void reset() {
        this.tenor = 0;
        this.installment = BigDecimal.ZERO;
        this.standardPriceLabel = "-";
        this.suggestPriceLabel = "-";
        this.finalPriceLabel = "-";
        this.standardInterest = BigDecimal.ZERO;
        this.suggestInterest = BigDecimal.ZERO;
        this.finalInterest = BigDecimal.ZERO;
        this.finalInterestOriginal = BigDecimal.ZERO;
        this.standardBasePrice =  new BaseRateView();
        this.suggestBasePrice = new BaseRateView();
        this.finalBasePrice = new BaseRateView();
        this.no = 0;
        this.brmsFlag = 0;
        this.installmentOriginal = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getFinalInterest() {
        return finalInterest;
    }

    public void setFinalInterest(BigDecimal finalInterest) {
        this.finalInterest = finalInterest;
    }

    public BigDecimal getStandardInterest() {
        return standardInterest;
    }

    public void setStandardInterest(BigDecimal standardInterest) {
        this.standardInterest = standardInterest;
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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getBrmsFlag() {
        return brmsFlag;
    }

    public void setBrmsFlag(int brmsFlag) {
        this.brmsFlag = brmsFlag;
    }

    public BigDecimal getFinalInterestOriginal() {
        return finalInterestOriginal;
    }

    public void setFinalInterestOriginal(BigDecimal finalInterestOriginal) {
        this.finalInterestOriginal = finalInterestOriginal;
    }

    public BigDecimal getInstallmentOriginal() {
        return installmentOriginal;
    }

    public void setInstallmentOriginal(BigDecimal installmentOriginal) {
        this.installmentOriginal = installmentOriginal;
    }
}
