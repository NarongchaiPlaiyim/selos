package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewGuarantorDetailView implements Serializable {
    private long id;
    private Customer guarantorName;
    private String tcgLgNo;
    private BigDecimal totalLimitGuaranteeAmount;
    private int isApproved;

    private List<CreditTypeDetailView> creditTypeDetailViewList;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public NewGuarantorDetailView() {
        reset();
    }

    public void reset() {
        this.guarantorName = new Customer();
        this.tcgLgNo = "";
        this.totalLimitGuaranteeAmount = BigDecimal.ZERO;
        this.creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Customer getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(Customer guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public List<CreditTypeDetailView> getCreditTypeDetailViewList() {
        return creditTypeDetailViewList;
    }

    public void setCreditTypeDetailViewList(List<CreditTypeDetailView> creditTypeDetailViewList) {
        this.creditTypeDetailViewList = creditTypeDetailViewList;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
    }

    public int getApproved() {
        return isApproved;
    }

    public void setApproved(int approved) {
        isApproved = approved;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("guarantorName", guarantorName)
                .append("tcgLgNo", tcgLgNo)
                .append("isApproved", isApproved)
                .append("creditTypeDetailViewList", creditTypeDetailViewList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
