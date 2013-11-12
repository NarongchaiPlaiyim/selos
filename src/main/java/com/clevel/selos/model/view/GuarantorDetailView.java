package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuarantorDetailView implements Serializable {

    private String guarantorName;
    private String tcgLgNo ;

    private List<CreditTypeDetailView> creditTypeDetailViewList ;
    private BigDecimal guaranteeAmount;
    private BigDecimal totalGuaranteeAmount;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    //test
    private CreditTypeDetailView creditTypeDetailView;

    public GuarantorDetailView() {
        reset();
    }

    public void reset() {

        this.guarantorName = "";
        this.tcgLgNo = "";
        this.guaranteeAmount = BigDecimal.ZERO;
        this.totalGuaranteeAmount = BigDecimal.ZERO;
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

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
    }


    // test
    public CreditTypeDetailView getCreditTypeDetailView() {
        return creditTypeDetailView;
    }

    public void setCreditTypeDetailView(CreditTypeDetailView creditTypeDetailView) {
        this.creditTypeDetailView = creditTypeDetailView;
    }
}
