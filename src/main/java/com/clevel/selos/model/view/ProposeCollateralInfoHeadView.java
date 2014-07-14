package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposeCollateralInfoHeadView implements Serializable {
    private long id;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private CollateralType headCollType;
    private PotentialCollateral potentialCollateral;
    private TCGCollateralType tcgCollateralType;
    private BigDecimal existingCredit;
    private int insuranceCompany;

    private List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList;
    private List<Long> deleteSubColHeadIdList;

    private boolean haveSubColl;

    private List<PotentialColToTCGCol> potentialColToTCGColList;

    public ProposeCollateralInfoHeadView(){
          reset();
    }

    public void reset(){
        this.titleDeed = "";
        this.collateralLocation = "";
        this.appraisalValue = BigDecimal.ZERO;
        this.headCollType = new CollateralType();
        this.potentialCollateral = new PotentialCollateral();
        this.tcgCollateralType = new TCGCollateralType();
        this.existingCredit = BigDecimal.ZERO;
        this.insuranceCompany = 0;
        this.proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
        this.deleteSubColHeadIdList = new ArrayList<Long>();
        this.haveSubColl = false;
        this.potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public TCGCollateralType getTcgCollateralType() {
        return tcgCollateralType;
    }

    public void setTcgCollateralType(TCGCollateralType tcgCollateralType) {
        this.tcgCollateralType = tcgCollateralType;
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

    public List<ProposeCollateralInfoSubView> getProposeCollateralInfoSubViewList() {
        return proposeCollateralInfoSubViewList;
    }

    public void setProposeCollateralInfoSubViewList(List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList) {
        this.proposeCollateralInfoSubViewList = proposeCollateralInfoSubViewList;
    }

    public List<Long> getDeleteSubColHeadIdList() {
        return deleteSubColHeadIdList;
    }

    public void setDeleteSubColHeadIdList(List<Long> deleteSubColHeadIdList) {
        this.deleteSubColHeadIdList = deleteSubColHeadIdList;
    }

    public boolean isHaveSubColl() {
        return haveSubColl;
    }

    public void setHaveSubColl(boolean haveSubColl) {
        this.haveSubColl = haveSubColl;
    }

    public List<PotentialColToTCGCol> getPotentialColToTCGColList() {
        return potentialColToTCGColList;
    }

    public void setPotentialColToTCGColList(List<PotentialColToTCGCol> potentialColToTCGColList) {
        this.potentialColToTCGColList = potentialColToTCGColList;
    }
}
