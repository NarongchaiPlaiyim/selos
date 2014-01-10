package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingCollateralDetailView implements Serializable {
    private long id;
    private int no;
    private int borrowerType;

    private PotentialCollateral potentialCollateral;
    private CollateralType collateralType;
    private String owner;
    private Relation relation;
    private Date appraisalDate;
    private String collateralNumber;
    private String collateralLocation;
    private String remark;
    private String cusName;
    private String accountNumber;
    private String accountSuffix;
    private String productProgram;
    private String creditFacility;
    private BigDecimal limit;
    private MortgageType mortgageType;
    private BigDecimal appraisalValue;
    private BigDecimal mortgageValue;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    private List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;


    public ExistingCollateralDetailView() {
        reset();
    }

    public void reset() {
        this.potentialCollateral = new PotentialCollateral();
        this.collateralType = new CollateralType();
        this.owner = "";
        this.relation = new Relation();
        this.appraisalDate = new Date();
        this.collateralNumber = "";
        this.collateralLocation = "";
        this.remark = "";
        this.cusName = "";
        this.accountNumber = "";
        this.accountSuffix = "";
        this.productProgram = "";
        this.creditFacility = "";
        this.limit = BigDecimal.ZERO;
        this.mortgageType = new MortgageType();
        this.existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
    }

    public long getId(){
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

    public int getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(int borrowerType) {
        this.borrowerType = borrowerType;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getCollateralNumber() {
        return collateralNumber;
    }

    public void setCollateralNumber(String collateralNumber) {
        this.collateralNumber = collateralNumber;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountSuffix() {
        return accountSuffix;
    }

    public void setAccountSuffix(String accountSuffix) {
        this.accountSuffix = accountSuffix;
    }

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditFacility() {
        return creditFacility;
    }

    public void setCreditFacility(String creditFacility) {
        this.creditFacility = creditFacility;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public MortgageType getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageType mortgageType) {
        this.mortgageType = mortgageType;
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

    public List<ExistingCreditTypeDetailView> getExistingCreditTypeDetailViewList() {
        return existingCreditTypeDetailViewList;
    }

    public void setExistingCreditTypeDetailViewList(List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList) {
        this.existingCreditTypeDetailViewList = existingCreditTypeDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("potentialCollateral", potentialCollateral)
                .append("collateralType", collateralType)
                .append("owner", owner)
                .append("relation", relation)
                .append("appraisalDate", appraisalDate)
                .append("collateralNumber", collateralNumber)
                .append("collateralLocation", collateralLocation)
                .append("remark", remark)
                .append("cusName", cusName)
                .append("accountNumber", accountNumber)
                .append("accountSuffix", accountSuffix)
                .append("creditFacility", creditFacility)
                .append("limit", limit)
                .append("mortgageType", mortgageType)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("existingCreditTypeDetailViewList", existingCreditTypeDetailViewList)
                .toString();
    }
}
