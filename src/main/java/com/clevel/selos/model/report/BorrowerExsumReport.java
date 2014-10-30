package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class BorrowerExsumReport extends ReportModel{
    private int no;
    private String customerName;
    private String citizenId;
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
        this.citizenId = "";
        this.tmbCustomerId = "";
        this.relation = "";
        this.collateralOwner = "";
        this.indLv = "";
        this.jurLv = "";
        this.worthiness = "";
        this.customerCSIList = "";
        this.businessLocationName = "";
        this.businessLocationAddress = "";
        this.businessLocationAddressEN = "";
        this.owner = "";
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCustomerName() {

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("no", no)
                .append("customerName", customerName)
                .append("citizenId", citizenId)
                .append("tmbCustomerId", tmbCustomerId)
                .append("relation", relation)
                .append("collateralOwner", collateralOwner)
                .append("indLv", indLv)
                .append("jurLv", jurLv)
                .append("percentShare", percentShare)
                .append("age", age)
                .append("kycLevel", kycLevel)
                .append("worthiness", worthiness)
                .append("customerCSIList", customerCSIList)
                .append("businessLocationName", businessLocationName)
                .append("businessLocationAddress", businessLocationAddress)
                .append("businessLocationAddressEN", businessLocationAddressEN)
                .append("owner", owner)
                .toString();
    }
}
