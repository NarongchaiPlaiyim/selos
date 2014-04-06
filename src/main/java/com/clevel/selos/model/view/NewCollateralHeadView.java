package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralHeadView implements Serializable {
    private long id;
    private int no;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private CollateralType headCollType;
    private CollateralType collTypePercentLTV;
    private PotentialCollateral potentialCollateral ;
    private TCGCollateralType tcgCollateralType;
    private TCGCollateralType tcgHeadCollType;
    private String collID;
    private BigDecimal existingCredit;
    private int insuranceCompany;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private List<NewCollateralSubView> newCollateralSubViewList;
    private List<NewCollateralSubView> newCollateralSubDeleteList;

    public NewCollateralHeadView(){
          reset();
    }

    public void reset(){
        this.potentialCollateral = new PotentialCollateral();
        this.headCollType = new CollateralType();
        this.collTypePercentLTV = new CollateralType();
        this.appraisalValue = BigDecimal.ZERO;
        this.collateralLocation = "";
        this.existingCredit = BigDecimal.ZERO;
        this.insuranceCompany = 0;
        this.titleDeed = "";
        this.newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        this.newCollateralSubDeleteList = new ArrayList<NewCollateralSubView>();
        this.tcgCollateralType = new TCGCollateralType();
        this.tcgHeadCollType = new TCGCollateralType();
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

    public List<NewCollateralSubView> getNewCollateralSubViewList() {
        return newCollateralSubViewList;
    }

    public void setNewCollateralSubViewList(List<NewCollateralSubView> newCollateralSubViewList) {
        this.newCollateralSubViewList = newCollateralSubViewList;
    }

    public BigDecimal getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.existingCredit = existingCredit;
    }

    public int getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(int insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public CollateralType getCollTypePercentLTV() {
        return collTypePercentLTV;
    }

    public void setCollTypePercentLTV(CollateralType collTypePercentLTV) {
        this.collTypePercentLTV = collTypePercentLTV;
    }

    public String getCollID() {
        return collID;
    }

    public void setCollID(String collID) {
        this.collID = collID;
    }

    public List<NewCollateralSubView> getNewCollateralSubDeleteList() {
        return newCollateralSubDeleteList;
    }

    public void setNewCollateralSubDeleteList(List<NewCollateralSubView> newCollateralSubDeleteList) {
        this.newCollateralSubDeleteList = newCollateralSubDeleteList;
    }

    public TCGCollateralType getTcgCollateralType() {
        return tcgCollateralType;
    }

    public void setTcgCollateralType(TCGCollateralType tcgCollateralType) {
        this.tcgCollateralType = tcgCollateralType;
    }

    public TCGCollateralType getTcgHeadCollType() {
        return tcgHeadCollType;
    }

    public void setTcgHeadCollType(TCGCollateralType tcgHeadCollType) {
        this.tcgHeadCollType = tcgHeadCollType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("appraisalValue", appraisalValue)
                .append("headCollType", headCollType)
                .append("collTypePercentLTV", collTypePercentLTV)
                .append("potentialCollateral", potentialCollateral)
                .append("tcgCollateralType", tcgCollateralType)
                .append("tcgHeadCollType", tcgHeadCollType)
                .append("collID", collID)
                .append("existingCredit", existingCredit)
                .append("insuranceCompany", insuranceCompany)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("newCollateralSubViewList", newCollateralSubViewList)
                .append("newCollateralSubDeleteList", newCollateralSubDeleteList)
                .toString();
    }
}
