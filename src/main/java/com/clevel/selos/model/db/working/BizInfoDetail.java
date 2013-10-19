package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.BusinessActivity;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 20/9/2556
 * Time: 15:13 น.
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="wrk_biz_info_detail")
public class BizInfoDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BIZ_INFO_DETAIL_ID", sequenceName="SEQ_WRK_BIZ_INFO_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BIZ_INFO_DETAIL_ID")
    private long id;

    @Column(name="biz_info_text")
    private String bizInfoText;

    @OneToOne
    @JoinColumn(name="business_activity_id")
    private BusinessActivity BusinessActivity;

    @OneToOne
    @JoinColumn(name="business_type_id")
    private BusinessType businessType;

    @OneToOne
    @JoinColumn(name="business_group_id")
    private BusinessGroup businessGroup;

    @OneToOne
    @JoinColumn(name="business_desc_id")
    private BusinessDescription businessDescription;

    @Column(name="biz_code")
    private String bizCode;

    @Column(name="income_factor")
    private BigDecimal incomeFactor;

    @Column(name="adjusted_income_factor")
    private BigDecimal adjustedIncomeFactor;

    @Column(name="biz_comment")
    private String bizComment;

    @Column(name="percent_biz")
    private BigDecimal percentBiz;

    @Column(name="biz_permission")
    private String bizPermission;

    @Column(name="biz_doc_permission")
    private String bizDocPermission;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="biz_doc_expiry_date")
    private Date bizDocExpiryDate;

    @Column(name="exp_ind_country_name")
    private String expIndCountryName;

    @Column(name="percent_exp_ind_country_name")
    private BigDecimal percentExpIndCountryName;

    @Column(name="sup_sum_percent_buy_volume")
    private BigDecimal supplierTotalPercentBuyVolume;

    @Column(name="sup_sum_percent_credit")
    private BigDecimal supplierTotalPercentCredit;

    @Column(name="sup_sum_credit_term")
    private BigDecimal supplierTotalCreditTerm;

    @Column(name="sup_uw_adjust_percent_credit")
    private BigDecimal supplierUWAdjustPercentCredit;

    @Column(name="sup_uw_adjust_credit_term")
    private BigDecimal supplierUWAdjustCreditTerm;

    @Column(name="buyer_sum_percent_buy_volume")
    private BigDecimal buyerTotalPercentBuyVolume;

    @Column(name="buyer_sum_percent_credit")
    private BigDecimal buyerTotalPercentCredit;

    @Column(name="buyer_sum_credit_term")
    private BigDecimal buyerTotalCreditTerm;

    @Column(name="buyer_uw_adjust_percent_credit")
    private BigDecimal buyerUWAdjustPercentCredit;

    @Column(name="buyer_uw_adjust_credit_term")
    private BigDecimal buyerUWAdjustCreditTerm;

    @Column(name="standard_account_receivable")
    private BigDecimal standardAccountReceivable;

    @Column(name="average_purchase_amount")
    private BigDecimal averagePurchaseAmount;

    @Column(name="purchase_percent_cash")
    private BigDecimal purchasePercentCash;

    @Column(name="purchase_percent_credit")
    private BigDecimal purchasePercentCredit;

    @Column(name="purchase_percent_local")
    private BigDecimal purchasePercentLocal;

    @Column(name="purchase_percent_foreign")
    private BigDecimal purchasePercentForeign;

    @Column(name="purchase_term")
    private BigDecimal purchaseTerm;

    @Column(name="standard_account_payable")
    private BigDecimal standardAccountPayable;

    @Column(name="average_payable_amount")
    private BigDecimal averagePayableAmount;

    @Column(name="payable_percent_cash")
    private BigDecimal payablePercentCash;

    @Column(name="payable_percent_credit")
    private BigDecimal payablePercentCredit;

    @Column(name="payable_percent_local")
    private BigDecimal payablePercentLocal;

    @Column(name="payable_percent_foreign")
    private BigDecimal payablePercentForeign;

    @Column(name="payable_term")
    private BigDecimal payableTerm;

    @Column(name="standard_stock")
    private BigDecimal standardStock;

    @Column(name="stock_duration_bdm")
    private BigDecimal stockDurationBDM;

    @Column(name="stock_duration_uw")
    private BigDecimal stockDurationUW;

    @Column(name="stock_value_bdm")
    private BigDecimal stockValueBDM;

    @Column(name="stock_value_uw")
    private BigDecimal stockValueUW;

    @Column(name="no")
    private long no;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizStakeHolderDetail> supplierList;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizStakeHolderDetail> buyerList;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizProductDetail> bizProductDetailList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @ManyToOne
    @JoinColumn(name="biz_info_summary_id")
    private BizInfoSummary bizInfoSummary;

    public BizInfoDetail() {
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

    public BusinessActivity getBusinessActivity() {
        return BusinessActivity;
    }

    public void setBusinessActivity(BusinessActivity businessActivity) {
        BusinessActivity = businessActivity;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
    }

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
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

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public List<BizStakeHolderDetail> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<BizStakeHolderDetail> supplierList) {
        this.supplierList = supplierList;
    }

    public List<BizStakeHolderDetail> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<BizStakeHolderDetail> buyerList) {
        this.buyerList = buyerList;
    }

    public List<BizProductDetail> getBizProductDetailList() {
        return bizProductDetailList;
    }

    public void setBizProductDetailList(List<BizProductDetail> bizProductDetailList) {
        this.bizProductDetailList = bizProductDetailList;
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

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public BizInfoSummary getBizInfoSummary() {
        return bizInfoSummary;
    }

    public void setBizInfoSummary(BizInfoSummary bizInfoSummary) {
        this.bizInfoSummary = bizInfoSummary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("bizInfoText", bizInfoText)
                .append("businessType", businessType)
                .append("businessGroup", businessGroup)
                .append("businessDescription", businessDescription)
                .append("bizCode", bizCode)
                .append("incomeFactor", incomeFactor)
                .append("adjustedIncomeFactor", adjustedIncomeFactor)
                .append("bizComment", bizComment)
                .append("percentBiz", percentBiz)
                .append("bizPermission", bizPermission)
                .append("bizDocPermission", bizDocPermission)
                .append("bizDocExpiryDate", bizDocExpiryDate)
                .append("expIndCountryName", expIndCountryName)
                .append("percentExpIndCountryName", percentExpIndCountryName)
                .append("supplierTotalPercentBuyVolume", supplierTotalPercentBuyVolume)
                .append("supplierTotalPercentCredit", supplierTotalPercentCredit)
                .append("supplierTotalCreditTerm", supplierTotalCreditTerm)
                .append("supplierUWAdjustPercentCredit", supplierUWAdjustPercentCredit)
                .append("supplierUWAdjustCreditTerm", supplierUWAdjustCreditTerm)
                .append("buyerTotalPercentBuyVolume", buyerTotalPercentBuyVolume)
                .append("buyerTotalPercentCredit", buyerTotalPercentCredit)
                .append("buyerTotalCreditTerm", buyerTotalCreditTerm)
                .append("buyerUWAdjustPercentCredit", buyerUWAdjustPercentCredit)
                .append("buyerUWAdjustCreditTerm", buyerUWAdjustCreditTerm)
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
                .append("supplierList", supplierList)
                .append("buyerList", buyerList)
                .append("bizProductDetailList", bizProductDetailList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("workCasePrescreen", workCasePrescreen)
                .append("bizInfoSummary", bizInfoSummary)
                .toString();
    }
}
