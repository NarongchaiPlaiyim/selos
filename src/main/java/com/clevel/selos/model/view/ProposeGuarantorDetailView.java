package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposeGuarantorDetailView implements Serializable {

    private Customer guarantorName;
    private String tcgLgNo;
    private BigDecimal guaranteeAmount;

    private List<CreditTypeDetailView> creditTypeDetailViewList;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public ProposeGuarantorDetailView() {
        reset();
    }

    public void reset() {
        this.guarantorName = new Customer();
        this.tcgLgNo = "";
        this.guaranteeAmount = BigDecimal.ZERO;
        this.creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
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

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

}
