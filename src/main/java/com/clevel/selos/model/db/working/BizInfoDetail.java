package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name="precent_biz")
    private BigDecimal percentBiz;

    @Column(name="biz_permission")
    private String bizPermission;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizStakeholder> supplierList;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizStakeholder> buyerList;

    @OneToMany(mappedBy="bizInfoDetail")
    private List<BizProduct> bizProductList;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;


    public BizInfoDetail() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBizInfoText() {
        return bizInfoText;
    }

    public void setBizInfoText(String bizInfoText) {
        this.bizInfoText = bizInfoText;
    }

    public List<BizStakeholder> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<BizStakeholder> supplierList) {
        this.supplierList = supplierList;
    }

    public List<BizStakeholder> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<BizStakeholder> buyerList) {
        this.buyerList = buyerList;
    }

    public List<BizProduct> getBizProductList() {
        return bizProductList;
    }

    public void setBizProductList(List<BizProduct> bizProductList) {
        this.bizProductList = bizProductList;
    }
}
