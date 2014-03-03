package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.MortgageType;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralSubView implements Serializable {
    private long id;
    private int no;
    private String collID;
    private String headCollID;
    private SubCollateralType subCollateralType;
    private String address;
    private String landOffice;
    private String titleDeed;
    private String collateralOwner;
    private String collateralOwnerAAD;
    private CustomerInfoView collateralOwnerUW;
    private List<CustomerInfoView> collateralOwnerUWList;
    private MortgageType mortgageType;
    private List<MortgageType> mortgageList;
    private long relatedWithId;
    private List<NewCollateralSubView> relatedWithList;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private String usage;
    private String typeOfUsage;
    private int lineNo;

    public NewCollateralSubView() {
        reset();
    }

    public void reset() {
        this.address = "";
        this.titleDeed  = "";
        this.landOffice = "";
        this.collateralOwner = "";
        this.collateralOwnerAAD = "";
        this.appraisalValue = BigDecimal.ZERO;
        this.mortgageValue = BigDecimal.ZERO;
        this.subCollateralType = new SubCollateralType();
        this.mortgageType= new MortgageType();
        this.collateralOwnerUWList = new ArrayList<CustomerInfoView>();
        this.mortgageList = new ArrayList<MortgageType>();
        this.relatedWithList = new ArrayList<NewCollateralSubView>();
        this.collateralOwnerUW = new CustomerInfoView();
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
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

    public List<NewCollateralSubView> getRelatedWithList() {
        return relatedWithList;
    }

    public void setRelatedWithList(List<NewCollateralSubView> relatedWithList) {
        this.relatedWithList = relatedWithList;
    }

    public CustomerInfoView getCollateralOwnerUW() {
        return collateralOwnerUW;
    }

    public void setCollateralOwnerUW(CustomerInfoView collateralOwnerUW) {
        this.collateralOwnerUW = collateralOwnerUW;
    }

    public long getRelatedWithId() {
        return relatedWithId;
    }

    public void setRelatedWithId(long relatedWithId) {
        this.relatedWithId = relatedWithId;
    }

    public String getCollID() {
        return collID;
    }

    public void setCollID(String collID) {
        this.collID = collID;
    }

    public String getHeadCollID() {
        return headCollID;
    }

    public void setHeadCollID(String headCollID) {
        this.headCollID = headCollID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("collID", collID)
                .append("headCollID", headCollID)
                .append("subCollateralType", subCollateralType)
                .append("address", address)
                .append("landOffice", landOffice)
                .append("titleDeed", titleDeed)
                .append("collateralOwner", collateralOwner)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("collateralOwnerUW", collateralOwnerUW)
                .append("collateralOwnerUWList", collateralOwnerUWList)
                .append("mortgageType", mortgageType)
                .append("mortgageList", mortgageList)
                .append("relatedWithId", relatedWithId)
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
