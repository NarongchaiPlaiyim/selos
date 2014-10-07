package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.system.Config;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProposeCollateralInfoSubView implements Serializable {
    private long id;
    private SubCollateralType subCollateralType;
    private String titleDeed;
    private String address;
    private String landOffice;
    private String collateralOwnerAAD;
    private List<CustomerInfoView> collateralOwnerUWList;
    private List<MortgageTypeView> mortgageList;
    private List<ProposeCollateralInfoSubView> relatedWithList;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;
    private String subId;
    private int createdByAAD;
    private int createdByBDM;

    //create by bird
    private String path;
    private int no;

    public ProposeCollateralInfoSubView() {
        reset();
    }

    public void reset() {
        this.subCollateralType = new SubCollateralType();
        this.address = "";
        this.landOffice = "";
        this.titleDeed  = "";
        this.collateralOwnerAAD = "";
        this.collateralOwnerUWList = new ArrayList<CustomerInfoView>();
        this.mortgageList = new ArrayList<MortgageTypeView>();
        this.relatedWithList = new ArrayList<ProposeCollateralInfoSubView>();
        this.appraisalValue = BigDecimal.ZERO;
        this.mortgageValue = BigDecimal.ZERO;
        this.subId = "";
        this.createdByAAD = 0;
        this.createdByBDM = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCollateralOwnerAAD() {
        return collateralOwnerAAD;
    }

    public void setCollateralOwnerAAD(String collateralOwnerAAD) {
        this.collateralOwnerAAD = collateralOwnerAAD;
    }

    public List<CustomerInfoView> getCollateralOwnerUWList() {
        return collateralOwnerUWList;
    }

    public void setCollateralOwnerUWList(List<CustomerInfoView> collateralOwnerUWList) {
        this.collateralOwnerUWList = collateralOwnerUWList;
    }

    public List<MortgageTypeView> getMortgageList() {
        return mortgageList;
    }

    public void setMortgageList(List<MortgageTypeView> mortgageList) {
        this.mortgageList = mortgageList;
    }

    public List<ProposeCollateralInfoSubView> getRelatedWithList() {
        return relatedWithList;
    }

    public void setRelatedWithList(List<ProposeCollateralInfoSubView> relatedWithList) {
        this.relatedWithList = relatedWithList;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getCreatedByAAD() {
        return createdByAAD;
    }

    public void setCreatedByAAD(int createdByAAD) {
        this.createdByAAD = createdByAAD;
    }

    public int getCreatedByBDM() {
        return createdByBDM;
    }

    public void setCreatedByBDM(int createdByBDM) {
        this.createdByBDM = createdByBDM;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("subCollateralType", subCollateralType)
                .append("titleDeed", titleDeed)
                .append("address", address)
                .append("landOffice", landOffice)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("collateralOwnerUWList", collateralOwnerUWList)
                .append("mortgageList", mortgageList)
                .append("relatedWithList", relatedWithList)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("subId", subId)
                .append("createdByAAD", createdByAAD)
                .append("createdByBDM", createdByBDM)
                .append("path", path)
                .append("no", no)
                .toString();
    }
}
