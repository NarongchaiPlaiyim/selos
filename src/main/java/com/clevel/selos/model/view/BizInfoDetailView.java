package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BizInfoDetailView implements Serializable {
    private long id;
    private String bizInfoText;
    private String tradeType;
    private BusinessType bizType;
    private BusinessGroup bizGroup;
    private BusinessDescription bizDesc;
    private String bizCode;
    private BigDecimal incomeFactor;
    private BigDecimal adjustedIncomeFactor;
    private String bizComment;
    private BigDecimal percentBiz;
    private String bizPermission;
    private String bizDocPermission;
    private Date bizDocExpiryDate;
    private String expIndCountryName;
    private BigDecimal percentExpIndCountryName;
    private BigDecimal supplierTotalPercentBuyVolume;
    private BigDecimal supplierTotalPercentCredit;
    private BigDecimal supplierTotalCreditTerm;
    private BigDecimal supplierUWAdjustPercentCredit;
    private BigDecimal supplierUWAdjustCreditTerm;
    private BigDecimal buyerTotalPercentBuyVolume;
    private BigDecimal buyerTotalPercentCredit;
    private BigDecimal buyerTotalCreditTerm;
    private BigDecimal buyerUWAdjustPercentCredit;
    private BigDecimal buyerUWAdjustCreditTerm;
    private BigDecimal standardAccountReceivable;
    private BigDecimal averagePurchaseAmount;
    private BigDecimal purchasePercentCash;
    private BigDecimal purchasePercentCredit;
    private BigDecimal purchasePercentLocal;
    private BigDecimal purchasePercentForeign;
    private BigDecimal purchaseTerm;
    private BigDecimal standardAccountPayable;
    private BigDecimal averagePayableAmount;
    private BigDecimal payablePercentCash;
    private BigDecimal payablePercentCredit;
    private BigDecimal payablePercentLocal;
    private BigDecimal payablePercentForeign;
    private BigDecimal payableTerm;
    private BigDecimal standardStock;
    private BigDecimal stockDurationBDM;
    private BigDecimal stockDurationUW;
    private BigDecimal stockValueBDM;
    private BigDecimal stockValueUW;

    private List<BizProductDetailView> bizProductDetailViewList;
    private List<BizStakeHolderDetailView> supplierDetailList;
    private List<BizStakeHolderDetailView> buyerDetailList;

    public void reset(){
        this.bizDesc = new BusinessDescription();
        this.bizDesc.setBusinessGroup(new BusinessGroup());
        this.bizGroup = new BusinessGroup();
        this.bizType = new BusinessType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBizInfoText() {
        return bizInfoText;
    }

    public void setBizInfoText(String bizInfoText) {
        this.bizInfoText = bizInfoText;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BusinessType getBizType() {
        return bizType;
    }

    public void setBizType(BusinessType bizType) {
        this.bizType = bizType;
    }

    public BusinessGroup getBizGroup() {
        return bizGroup;
    }

    public void setBizGroup(BusinessGroup bizGroup) {
        this.bizGroup = bizGroup;
    }

    public BusinessDescription getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(BusinessDescription bizDesc) {
        this.bizDesc = bizDesc;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public BigDecimal getAdjustedIncomeFactor() {
        return adjustedIncomeFactor;
    }

    public void setAdjustedIncomeFactor(BigDecimal adjustedIncomeFactor) {
        this.adjustedIncomeFactor = adjustedIncomeFactor;
    }

    public String getBizComment() {
        return bizComment;
    }

    public void setBizComment(String bizComment) {
        this.bizComment = bizComment;
    }

    public BigDecimal getPercentBiz() {
        return percentBiz;
    }

    public void setPercentBiz(BigDecimal percentBiz) {
        this.percentBiz = percentBiz;
    }

    public String getBizPermission() {
        return bizPermission;
    }

    public void setBizPermission(String bizPermission) {
        this.bizPermission = bizPermission;
    }

    public String getBizDocPermission() {
        return bizDocPermission;
    }

    public void setBizDocPermission(String bizDocPermission) {
        this.bizDocPermission = bizDocPermission;
    }

    public Date getBizDocExpiryDate() {
        return bizDocExpiryDate;
    }

    public void setBizDocExpiryDate(Date bizDocExpiryDate) {
        this.bizDocExpiryDate = bizDocExpiryDate;
    }

    public String getExpIndCountryName() {
        return expIndCountryName;
    }

    public void setExpIndCountryName(String expIndCountryName) {
        this.expIndCountryName = expIndCountryName;
    }

    public BigDecimal getPercentExpIndCountryName() {
        return percentExpIndCountryName;
    }

    public void setPercentExpIndCountryName(BigDecimal percentExpIndCountryName) {
        this.percentExpIndCountryName = percentExpIndCountryName;
    }

    public BigDecimal getSupplierTotalPercentBuyVolume() {
        return supplierTotalPercentBuyVolume;
    }

    public void setSupplierTotalPercentBuyVolume(BigDecimal supplierTotalPercentBuyVolume) {
        this.supplierTotalPercentBuyVolume = supplierTotalPercentBuyVolume;
    }

    public BigDecimal getSupplierTotalPercentCredit() {
        return supplierTotalPercentCredit;
    }

    public void setSupplierTotalPercentCredit(BigDecimal supplierTotalPercentCredit) {
        this.supplierTotalPercentCredit = supplierTotalPercentCredit;
    }

    public BigDecimal getSupplierTotalCreditTerm() {
        return supplierTotalCreditTerm;
    }

    public void setSupplierTotalCreditTerm(BigDecimal supplierTotalCreditTerm) {
        this.supplierTotalCreditTerm = supplierTotalCreditTerm;
    }

    public BigDecimal getSupplierUWAdjustPercentCredit() {
        return supplierUWAdjustPercentCredit;
    }

    public void setSupplierUWAdjustPercentCredit(BigDecimal supplierUWAdjustPercentCredit) {
        this.supplierUWAdjustPercentCredit = supplierUWAdjustPercentCredit;
    }

    public BigDecimal getSupplierUWAdjustCreditTerm() {
        return supplierUWAdjustCreditTerm;
    }

    public void setSupplierUWAdjustCreditTerm(BigDecimal supplierUWAdjustCreditTerm) {
        this.supplierUWAdjustCreditTerm = supplierUWAdjustCreditTerm;
    }

    public BigDecimal getBuyerTotalPercentBuyVolume() {
        return buyerTotalPercentBuyVolume;
    }

    public void setBuyerTotalPercentBuyVolume(BigDecimal buyerTotalPercentBuyVolume) {
        this.buyerTotalPercentBuyVolume = buyerTotalPercentBuyVolume;
    }

    public BigDecimal getBuyerTotalPercentCredit() {
        return buyerTotalPercentCredit;
    }

    public void setBuyerTotalPercentCredit(BigDecimal buyerTotalPercentCredit) {
        this.buyerTotalPercentCredit = buyerTotalPercentCredit;
    }

    public BigDecimal getBuyerTotalCreditTerm() {
        return buyerTotalCreditTerm;
    }

    public void setBuyerTotalCreditTerm(BigDecimal buyerTotalCreditTerm) {
        this.buyerTotalCreditTerm = buyerTotalCreditTerm;
    }

    public BigDecimal getBuyerUWAdjustPercentCredit() {
        return buyerUWAdjustPercentCredit;
    }

    public void setBuyerUWAdjustPercentCredit(BigDecimal buyerUWAdjustPercentCredit) {
        this.buyerUWAdjustPercentCredit = buyerUWAdjustPercentCredit;
    }

    public BigDecimal getBuyerUWAdjustCreditTerm() {
        return buyerUWAdjustCreditTerm;
    }

    public void setBuyerUWAdjustCreditTerm(BigDecimal buyerUWAdjustCreditTerm) {
        this.buyerUWAdjustCreditTerm = buyerUWAdjustCreditTerm;
    }

    public BigDecimal getStandardAccountReceivable() {
        return standardAccountReceivable;
    }

    public void setStandardAccountReceivable(BigDecimal standardAccountReceivable) {
        this.standardAccountReceivable = standardAccountReceivable;
    }

    public BigDecimal getAveragePurchaseAmount() {
        return averagePurchaseAmount;
    }

    public void setAveragePurchaseAmount(BigDecimal averagePurchaseAmount) {
        this.averagePurchaseAmount = averagePurchaseAmount;
    }

    public BigDecimal getPurchasePercentCash() {
        return purchasePercentCash;
    }

    public void setPurchasePercentCash(BigDecimal purchasePercentCash) {
        this.purchasePercentCash = purchasePercentCash;
    }

    public BigDecimal getPurchasePercentCredit() {
        return purchasePercentCredit;
    }

    public void setPurchasePercentCredit(BigDecimal purchasePercentCredit) {
        this.purchasePercentCredit = purchasePercentCredit;
    }

    public BigDecimal getPurchasePercentLocal() {
        return purchasePercentLocal;
    }

    public void setPurchasePercentLocal(BigDecimal purchasePercentLocal) {
        this.purchasePercentLocal = purchasePercentLocal;
    }

    public BigDecimal getPurchasePercentForeign() {
        return purchasePercentForeign;
    }

    public void setPurchasePercentForeign(BigDecimal purchasePercentForeign) {
        this.purchasePercentForeign = purchasePercentForeign;
    }

    public BigDecimal getPurchaseTerm() {
        return purchaseTerm;
    }

    public void setPurchaseTerm(BigDecimal purchaseTerm) {
        this.purchaseTerm = purchaseTerm;
    }

    public BigDecimal getStandardAccountPayable() {
        return standardAccountPayable;
    }

    public void setStandardAccountPayable(BigDecimal standardAccountPayable) {
        this.standardAccountPayable = standardAccountPayable;
    }

    public BigDecimal getAveragePayableAmount() {
        return averagePayableAmount;
    }

    public void setAveragePayableAmount(BigDecimal averagePayableAmount) {
        this.averagePayableAmount = averagePayableAmount;
    }

    public BigDecimal getPayablePercentCash() {
        return payablePercentCash;
    }

    public void setPayablePercentCash(BigDecimal payablePercentCash) {
        this.payablePercentCash = payablePercentCash;
    }

    public BigDecimal getPayablePercentCredit() {
        return payablePercentCredit;
    }

    public void setPayablePercentCredit(BigDecimal payablePercentCredit) {
        this.payablePercentCredit = payablePercentCredit;
    }

    public BigDecimal getPayablePercentLocal() {
        return payablePercentLocal;
    }

    public void setPayablePercentLocal(BigDecimal payablePercentLocal) {
        this.payablePercentLocal = payablePercentLocal;
    }

    public BigDecimal getPayablePercentForeign() {
        return payablePercentForeign;
    }

    public void setPayablePercentForeign(BigDecimal payablePercentForeign) {
        this.payablePercentForeign = payablePercentForeign;
    }

    public BigDecimal getPayableTerm() {
        return payableTerm;
    }

    public void setPayableTerm(BigDecimal payableTerm) {
        this.payableTerm = payableTerm;
    }

    public BigDecimal getStandardStock() {
        return standardStock;
    }

    public void setStandardStock(BigDecimal standardStock) {
        this.standardStock = standardStock;
    }

    public BigDecimal getStockDurationBDM() {
        return stockDurationBDM;
    }

    public void setStockDurationBDM(BigDecimal stockDurationBDM) {
        this.stockDurationBDM = stockDurationBDM;
    }

    public BigDecimal getStockDurationUW() {
        return stockDurationUW;
    }

    public void setStockDurationUW(BigDecimal stockDurationUW) {
        this.stockDurationUW = stockDurationUW;
    }

    public BigDecimal getStockValueBDM() {
        return stockValueBDM;
    }

    public void setStockValueBDM(BigDecimal stockValueBDM) {
        this.stockValueBDM = stockValueBDM;
    }

    public BigDecimal getStockValueUW() {
        return stockValueUW;
    }

    public void setStockValueUW(BigDecimal stockValueUW) {
        this.stockValueUW = stockValueUW;
    }

    public List<BizProductDetailView> getBizProductDetailViewList() {
        return bizProductDetailViewList;
    }

    public void setBizProductDetailViewList(List<BizProductDetailView> bizProductDetailViewList) {
        this.bizProductDetailViewList = bizProductDetailViewList;
    }

    public List<BizStakeHolderDetailView> getBuyerDetailList() {
        return buyerDetailList;
    }

    public void setBuyerDetailList(List<BizStakeHolderDetailView> buyerDetailList) {
        this.buyerDetailList = buyerDetailList;
    }

    public List<BizStakeHolderDetailView> getSupplierDetailList() {
        return supplierDetailList;
    }

    public void setSupplierDetailList(List<BizStakeHolderDetailView> supplierDetailList) {
        this.supplierDetailList = supplierDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("bizInfoText", bizInfoText)
                .append("tradeType", tradeType)
                .append("bizType", bizType)
                .append("bizGroup", bizGroup)
                .append("bizDesc", bizDesc)
                .append("bizCode", bizCode)
                .append("incomeFactor", incomeFactor)
                .append("adjustedIncomeFactor", adjustedIncomeFactor)
                .append("percentBiz", percentBiz)
                .append("bizPermission", bizPermission)
                .append("bizComment", bizComment)
                .append("standardAccountReceivable", standardAccountReceivable)
                .append("averagePurchaseAmount", averagePurchaseAmount)
                .append("purchasePercentCash", purchasePercentCash)
                .append("purchasePercentCredit", purchasePercentCredit)
                .append("purchasePercentLocal", purchasePercentLocal)
                .append("purchasePercentForeign", purchasePercentForeign)
                .append("purchaseTerm", purchaseTerm)
                .append("standardAccountPayable", standardAccountPayable)
                .append("averagePayableAmount", averagePayableAmount)
                .append("payablePercentCash", payablePercentCash)
                .append("payablePercentCredit", payablePercentCredit)
                .append("payablePercentLocal", payablePercentLocal)
                .append("payablePercentForeign", payablePercentForeign)
                .append("payableTerm", payableTerm)
                .append("standardStock", standardStock)
                .append("stockDurationBDM", stockDurationBDM)
                .append("stockDurationUW", stockDurationUW)
                .append("stockValueBDM", stockValueBDM)
                .append("stockValueUW", stockValueUW)
                .append("bizProductDetailViewList", bizProductDetailViewList)
                .append("supplierDetailList", supplierDetailList)
                .append("buyerDetailList", buyerDetailList)
                .toString();
    }
}
