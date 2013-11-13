package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CollateralHeaderDetailView {
    private long id;
    private int no;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private CollateralType headCollType;

    //dropdown
//    private potential;
//    private collTypePercentLTV ;

    private String existingCredit;
    private int insuranceCompany;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    List<SubCollateralDetailView> subCollateralDetailViewList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public CollateralType getHeadCollType() {
        return headCollType;
    }

    public void setHeadCollType(CollateralType headCollType) {
        this.headCollType = headCollType;
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

    public List<SubCollateralDetailView> getSubCollateralDetailViewList() {
        return subCollateralDetailViewList;
    }

    public void setSubCollateralDetailViewList(List<SubCollateralDetailView> subCollateralDetailViewList) {
        this.subCollateralDetailViewList = subCollateralDetailViewList;
    }

    public String getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(String existingCredit) {
        this.existingCredit = existingCredit;
    }

    public int getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(int insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }


}
