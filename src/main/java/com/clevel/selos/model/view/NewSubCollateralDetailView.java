package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewSubCollateralDetailView {
    private long id;
    private int no;
    private SubCollateralType subCollateralType;
    private String address;
    private String landOffice;
    private String titleDeed;
    private String collateralOwner;
    private String collateralOwnerAAD;
    private List<CustomerInfoView> collateralOwnerUWList;
    private MortgageType mortgageType;
    private List<MortgageType> mortgageList;
    private List<NewSubCollateralDetailView> relatedWithList;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public NewSubCollateralDetailView() {
        reset();
    }

    public void reset() {
        this.address    = "";
        this.titleDeed  = "";
        this.landOffice = "";
        this.collateralOwner = "";
        this.collateralOwnerAAD = "";
        this.appraisalValue = BigDecimal.ZERO;
        this.mortgageValue  = BigDecimal.ZERO;
        this.subCollateralType    = new SubCollateralType();
        this.mortgageType= new MortgageType();
        this.collateralOwnerUWList = new ArrayList<CustomerInfoView>();
        this.mortgageList = new ArrayList<MortgageType>();
        this.relatedWithList = new ArrayList<NewSubCollateralDetailView>();
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

    public MortgageType getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageType mortgageType) {
        this.mortgageType = mortgageType;
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

    public List<MortgageType> getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(List<MortgageType> mortgageList) {
        this.mortgageList = mortgageList;
    }

    public List<CustomerInfoView> getCollateralOwnerUWList() {
        return collateralOwnerUWList;
    }

    public void setCollateralOwnerUWList(List<CustomerInfoView> collateralOwnerUWList) {
        this.collateralOwnerUWList = collateralOwnerUWList;
    }

    public List<NewSubCollateralDetailView> getRelatedWithList() {
        return relatedWithList;
    }

    public void setRelatedWithList(List<NewSubCollateralDetailView> relatedWithList) {
        this.relatedWithList = relatedWithList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("subCollateralType", subCollateralType)
                .append("address", address)
                .append("landOffice", landOffice)
                .append("titleDeed", titleDeed)
                .append("collateralOwner", collateralOwner)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("collateralOwnerUWList", collateralOwnerUWList)
                .append("mortgageType", mortgageType)
                .append("mortgageList", mortgageList)
                .append("relatedWithList", relatedWithList)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
