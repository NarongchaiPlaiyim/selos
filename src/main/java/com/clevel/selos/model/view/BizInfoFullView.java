package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 13/9/2556
 * Time: 14:34 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizInfoFullView {

    private String businessInfoDetail;
    private String tradeType;
    private BusinessType bizType;
    private BusinessGroup bizGroup;
    private BusinessDescription bizDesc;
    private String bizCode;
    private BigDecimal incomeFactor;
    private BigDecimal adjustedIncomeFactor;
    private BigDecimal percentBusiness;
    private String bizPermission;
    private String comment;
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

    private List<BizProductView> bizProductViewList;
    private List<StakeholderView> supplierList;
    private List<StakeholderView> buyerList;

    public String getBusinessInfoDetail() {
        return businessInfoDetail;
    }

    public void setBusinessInfoDetail(String businessInfoDetail) {
        this.businessInfoDetail = businessInfoDetail;
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

    public BigDecimal getPercentBusiness() {
        return percentBusiness;
    }

    public void setPercentBusiness(BigDecimal percentBusiness) {
        this.percentBusiness = percentBusiness;
    }

    public String getBizPermission() {
        return bizPermission;
    }

    public void setBizPermission(String bizPermission) {
        this.bizPermission = bizPermission;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<BizProductView> getBizProductViewList() {
        return bizProductViewList;
    }

    public void setBizProductViewList(List<BizProductView> bizProductViewList) {
        this.bizProductViewList = bizProductViewList;
    }

    public List<StakeholderView> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<StakeholderView> supplierList) {
        this.supplierList = supplierList;
    }

    public List<StakeholderView> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<StakeholderView> buyerList) {
        this.buyerList = buyerList;
    }

}
