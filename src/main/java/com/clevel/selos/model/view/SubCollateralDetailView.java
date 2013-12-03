package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubCollateralDetailView {
    private long id;
    private int no;
    private SubCollateralType subCollateralType;
    private String address;
    private String landOffice;
    private String titleDeed;
    private String collateralOwner;
    private String collateralOwnerAAD;
    private String  collateralOwnerUW;
    private List<String> collateralOwnerUWList;
    private String  mortgageType;
    private List<String> mortgageList;
    private String  relatedWith;
    private List<String> relatedWithList;
    private BigDecimal  appraisalValue;
    private BigDecimal  mortgageValue;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;



    public SubCollateralDetailView() {
        reset();
    }

    public void reset() {
        this.id = 0;
        this.address    = "";
        this.titleDeed  = "";
        this.landOffice = "";
        this.collateralOwner = "";
        this.collateralOwnerAAD = "";
        this.appraisalValue = BigDecimal.ZERO;
        this.mortgageValue  = BigDecimal.ZERO;
        this.subCollateralType    = new SubCollateralType();
        this.collateralOwnerUW = "";
        this.mortgageType= "";
        this.relatedWith = "";
        this.collateralOwnerUWList = new ArrayList<String>();
        this.mortgageList = new ArrayList<String>();
        this.relatedWithList = new ArrayList<String>();
    }

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

    public SubCollateralType getSubCollateralType() {
        return subCollateralType;
    }

    public void setSubCollateralType(SubCollateralType subCollateralType) {
        this.subCollateralType = subCollateralType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandOffice() {
        return landOffice;
    }

    public void setLandOffice(String landOffice) {
        this.landOffice = landOffice;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(String collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public String getCollateralOwnerAAD() {
        return collateralOwnerAAD;
    }

    public void setCollateralOwnerAAD(String collateralOwnerAAD) {
        this.collateralOwnerAAD = collateralOwnerAAD;
    }

    public String getCollateralOwnerUW() {
        return collateralOwnerUW;
    }

    public void setCollateralOwnerUW(String collateralOwnerUW) {
        this.collateralOwnerUW = collateralOwnerUW;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    public String getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(String relatedWith) {
        this.relatedWith = relatedWith;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
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

    public List<String> getCollateralOwnerUWList() {
        return collateralOwnerUWList;
    }

    public void setCollateralOwnerUWList(List<String> collateralOwnerUWList) {
        this.collateralOwnerUWList = collateralOwnerUWList;
    }

    public List<String> getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(List<String> mortgageList) {
        this.mortgageList = mortgageList;
    }

    public List<String> getRelatedWithList() {
        return relatedWithList;
    }

    public void setRelatedWithList(List<String> relatedWithList) {
        this.relatedWithList = relatedWithList;
    }
}
