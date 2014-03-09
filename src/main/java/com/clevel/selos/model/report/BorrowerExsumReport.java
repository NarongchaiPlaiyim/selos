package com.clevel.selos.model.report;

import java.math.BigDecimal;

public class BorrowerExsumReport {
    private int no;
    private String titleTh;
    private String  firstNameTh;
    private String lastNameTh;
    private String citizenId;
    private String registrationId;
    private String tmbCustomerId;
    private String relation;
    private String collateralOwner;
    private String indLv;
    private String jurLv;
    private BigDecimal percentShare;
    private int age;
    private int kycLevel;
    private String worthiness;
    private String customerCSIList;
    private String businessLocationName;
    private String businessLocationAddress;
    private String businessLocationAddressEN;
    private String owner;

    public BorrowerExsumReport() {
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(String titleTh) {
        this.titleTh = titleTh;
    }

    public String getFirstNameTh() {
        return firstNameTh;
    }

    public void setFirstNameTh(String firstNameTh) {
        this.firstNameTh = firstNameTh;
    }

    public String getLastNameTh() {
        return lastNameTh;
    }

    public void setLastNameTh(String lastNameTh) {
        this.lastNameTh = lastNameTh;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getTmbCustomerId() {
        return tmbCustomerId;
    }

    public void setTmbCustomerId(String tmbCustomerId) {
        this.tmbCustomerId = tmbCustomerId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(String collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public String getIndLv() {
        return indLv;
    }

    public void setIndLv(String indLv) {
        this.indLv = indLv;
    }

    public String getJurLv() {
        return jurLv;
    }

    public void setJurLv(String jurLv) {
        this.jurLv = jurLv;
    }

    public BigDecimal getPercentShare() {
        return percentShare;
    }

    public void setPercentShare(BigDecimal percentShare) {
        this.percentShare = percentShare;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(int kycLevel) {
        this.kycLevel = kycLevel;
    }

    public String getWorthiness() {
        return worthiness;
    }

    public void setWorthiness(String worthiness) {
        this.worthiness = worthiness;
    }

    public String getCustomerCSIList() {
        return customerCSIList;
    }

    public void setCustomerCSIList(String customerCSIList) {
        this.customerCSIList = customerCSIList;
    }

    public String getBusinessLocationName() {
        return businessLocationName;
    }

    public void setBusinessLocationName(String businessLocationName) {
        this.businessLocationName = businessLocationName;
    }

    public String getBusinessLocationAddress() {
        return businessLocationAddress;
    }

    public void setBusinessLocationAddress(String businessLocationAddress) {
        this.businessLocationAddress = businessLocationAddress;
    }

    public String getBusinessLocationAddressEN() {
        return businessLocationAddressEN;
    }

    public void setBusinessLocationAddressEN(String businessLocationAddressEN) {
        this.businessLocationAddressEN = businessLocationAddressEN;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "BorrowerExsumReport{" +
                "no=" + no +
                ", titleTh='" + titleTh + '\'' +
                ", firstNameTh='" + firstNameTh + '\'' +
                ", lastNameTh='" + lastNameTh + '\'' +
                ", citizenId='" + citizenId + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", tmbCustomerId='" + tmbCustomerId + '\'' +
                ", relation='" + relation + '\'' +
                ", collateralOwner='" + collateralOwner + '\'' +
                ", indLv='" + indLv + '\'' +
                ", jurLv='" + jurLv + '\'' +
                ", percentShare=" + percentShare +
                ", age=" + age +
                ", kycLevel=" + kycLevel +
                ", worthiness='" + worthiness + '\'' +
                ", customerCSIList='" + customerCSIList + '\'' +
                ", businessLocationName='" + businessLocationName + '\'' +
                ", businessLocationAddress='" + businessLocationAddress + '\'' +
                ", businessLocationAddressEN='" + businessLocationAddressEN + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
