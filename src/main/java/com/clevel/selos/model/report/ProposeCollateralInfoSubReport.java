package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pakorn
 * Date: 6/10/2557
 * Time: 14:30 น.
 * To change this template use File | Settings | File Templates.
 */
public class ProposeCollateralInfoSubReport {

    private String path;
    private int no;
    private String deceptionSubCollType;
    private String titleDeed;
    private String collateralOwnerAAD;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;
    private String CollateralOwner;
    private String mortgage;
    private String relatedWith;

    public ProposeCollateralInfoSubReport() {
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

    public String getDeceptionSubCollType() {
        return deceptionSubCollType;
    }

    public void setDeceptionSubCollType(String deceptionSubCollType) {
        this.deceptionSubCollType = deceptionSubCollType;
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

    public String getCollateralOwner() {
        return CollateralOwner;
    }

    public void setCollateralOwner(String collateralOwner) {
        CollateralOwner = collateralOwner;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(String relatedWith) {
        this.relatedWith = relatedWith;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("path", path)
                .append("no", no)
                .append("deceptionSubCollType", deceptionSubCollType)
                .append("titleDeed", titleDeed)
                .append("collateralOwnerAAD", collateralOwnerAAD)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("CollateralOwner", CollateralOwner)
                .append("mortgage", mortgage)
                .append("relatedWith", relatedWith)
                .toString();
    }
}
