package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 20/9/2556
 * Time: 15:13 น.
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="wrk_businessinfo_detail")
public class BusinessInfoDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BIZ_INFO_DETAIL_ID", sequenceName="SEQ_WRK_BIZ_INFO_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BIZ_INFO_DETAIL_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="business_type_id")
    private BusinessType businessType;

    @OneToOne
    @JoinColumn(name="business_group_id")
    private BusinessGroup businessGroup;

    @OneToOne
    @JoinColumn(name="business_desc_id")
    private BusinessDescription businessDescription;

    @Column(name="business_code")
    private String businessCode;

    @Column(name="income_factor")
    private BigDecimal incomeFactor;

    @Column(name="adjusted_income_factor")
    private BigDecimal adjustedIncomeFactor;

    @Column(name="business_comment")
    private String businessComment;

    @Column(name="precent_business")
    private BigDecimal percentBusiness;

    @Column(name="business_permission")
    private String businessPermission;

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

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
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

    public String getBusinessComment() {
        return businessComment;
    }

    public void setBusinessComment(String businessComment) {
        this.businessComment = businessComment;
    }

    public BigDecimal getPercentBusiness() {
        return percentBusiness;
    }

    public void setPercentBusiness(BigDecimal percentBusiness) {
        this.percentBusiness = percentBusiness;
    }

    public String getBusinessPermission() {
        return businessPermission;
    }

    public void setBusinessPermission(String businessPermission) {
        this.businessPermission = businessPermission;
    }
}
