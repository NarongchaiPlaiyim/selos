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
    private BigDecimal totalLimitGuaranteeAmount;

    private List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;

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
        this.totalLimitGuaranteeAmount = BigDecimal.ZERO;
        this.existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
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

    public List<ExistingCreditTypeDetailView> getExistingCreditTypeDetailViewList() {
        return existingCreditTypeDetailViewList;
    }

    public void setExistingCreditTypeDetailViewList(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList) {
        this.existingCreditTypeDetailViewList = existingCreditTypeDetailViewList;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("guarantorName", guarantorName)
                .append("tcgLgNo", tcgLgNo)
                .append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount)
                .append("existingCreditTypeDetailViewList", existingCreditTypeDetailViewList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
