package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingGuarantorDetailView implements Serializable {

    private String guarantorName;
    private String tcgLgNo;
    private BigDecimal guaranteeAmount;

    private List<ExistingCreditDetailView> creditFacilityList;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public ExistingGuarantorDetailView() {
        reset();
    }

    public void reset() {
        this.guarantorName = "";
        this.tcgLgNo = "";
        this.guaranteeAmount = BigDecimal.ZERO;
        this.creditFacilityList = new ArrayList<ExistingCreditDetailView>();
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

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public List<ExistingCreditDetailView> getCreditFacilityList() {
        return creditFacilityList;
    }

    public void setCreditFacilityList(List<ExistingCreditDetailView> creditFacilityList) {
        this.creditFacilityList = creditFacilityList;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("guarantorName", guarantorName)
                .append("tcgLgNo", tcgLgNo)
                .append("guaranteeAmount", guaranteeAmount)
                .append("creditFacilityList", creditFacilityList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
