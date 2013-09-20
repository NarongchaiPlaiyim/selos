package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
@Table(name="wrk_business_info_detail")
public class BusinessInfoDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BUSINESS_INFO_DETAIL_ID", sequenceName="SEQ_WRK_BUSINESS_INFO_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BUSINESS_INFO_DETAIL_ID")
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

    @Column(name="comment")
    private String comment;

    @Column(name="precent_business")
    private BigDecimal percentBusiness;

    @Column(name="biz_permission")
    private String bizPermission;

    public String getBizPermission() {
        return bizPermission;
    }

    public void setBizPermission(String bizPermission) {
        this.bizPermission = bizPermission;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getPercentBusiness() {
        return percentBusiness;
    }

    public void setPercentBusiness(BigDecimal percentBusiness) {
        this.percentBusiness = percentBusiness;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("businessType", businessType)
                .append("businessGroup", businessGroup)
                .append("businessDescription", businessDescription)
                .append("businessCode", businessCode)
                .append("incomeFactor", incomeFactor)
                .append("adjustedIncomeFactor", adjustedIncomeFactor)
                .append("comment", comment)
                .append("percentBusiness", percentBusiness)
                .append("bizPermission", bizPermission)
                .toString();
    }
}
