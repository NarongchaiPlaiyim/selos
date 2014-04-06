package com.clevel.selos.integration.ecm.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.sql.Date;

public class ECMCAPShare implements Serializable {
    private String crsAccount1;     //CRS_ACCOUNT1
    private String crsAccount2;     //CRS_ACCOUNT2
    private String crsAccount3;     //CRS_ACCOUNT3
    private String crsBranchCode;   //CRS_BRANCHCODE  *
    private String crsCancelCA;     //CRS_CANCEL_CA   *N
    private Date crsCreateDate;     //CRS_CRATEDATE   *
    private String crsCustName;     //CRS_CUSTNAME    *
    private String crsCusType;      //CRS_CUSTTYPE    *
    private String crsHubCode;      //CRS_HUBCODE     *
    private String crsIdNumber;     //CRS_IDNUMBER
    private String crsIdType;       //CRS_IDTYPE
    private Date crsLastUpdate;     //CRS_LASTUPDATE  *
    private String crsRegionCode;   //CRS_REGIONCODE  *
    private String crsRoCode;       //CRS_ROCODE      *
    private String crsSuffix1;      //CRS_SUFFIX1
    private String crsSuffix2;      //CRS_SUFFIX2
    private String crsSuffix3;      //CRS_SUFFIX3
    private String crsTMBCustId;    //CRS_TMBCUSTID
    private String crsUKCANumber;   //CRS_UK_CANUMBER *

    public String getCrsAccount1() {
        return crsAccount1;
    }

    public void setCrsAccount1(String crsAccount1) {
        this.crsAccount1 = crsAccount1;
    }

    public String getCrsAccount2() {
        return crsAccount2;
    }

    public void setCrsAccount2(String crsAccount2) {
        this.crsAccount2 = crsAccount2;
    }

    public String getCrsAccount3() {
        return crsAccount3;
    }

    public void setCrsAccount3(String crsAccount3) {
        this.crsAccount3 = crsAccount3;
    }

    public String getCrsBranchCode() {
        return crsBranchCode;
    }

    public void setCrsBranchCode(String crsBranchCode) {
        this.crsBranchCode = crsBranchCode;
    }

    public String getCrsCancelCA() {
        return crsCancelCA;
    }

    public void setCrsCancelCA(String crsCancelCA) {
        this.crsCancelCA = crsCancelCA;
    }

    public Date getCrsCreateDate() {
        return crsCreateDate;
    }

    public void setCrsCreateDate(Date crsCreateDate) {
        this.crsCreateDate = crsCreateDate;
    }

    public String getCrsCustName() {
        return crsCustName;
    }

    public void setCrsCustName(String crsCustName) {
        this.crsCustName = crsCustName;
    }

    public String getCrsCusType() {
        return crsCusType;
    }

    public void setCrsCusType(String crsCusType) {
        this.crsCusType = crsCusType;
    }

    public String getCrsHubCode() {
        return crsHubCode;
    }

    public void setCrsHubCode(String crsHubCode) {
        this.crsHubCode = crsHubCode;
    }

    public String getCrsIdNumber() {
        return crsIdNumber;
    }

    public void setCrsIdNumber(String crsIdNumber) {
        this.crsIdNumber = crsIdNumber;
    }

    public String getCrsIdType() {
        return crsIdType;
    }

    public void setCrsIdType(String crsIdType) {
        this.crsIdType = crsIdType;
    }

    public Date getCrsLastUpdate() {
        return crsLastUpdate;
    }

    public void setCrsLastUpdate(Date crsLastUpdate) {
        this.crsLastUpdate = crsLastUpdate;
    }

    public String getCrsRegionCode() {
        return crsRegionCode;
    }

    public void setCrsRegionCode(String crsRegionCode) {
        this.crsRegionCode = crsRegionCode;
    }

    public String getCrsRoCode() {
        return crsRoCode;
    }

    public void setCrsRoCode(String crsRoCode) {
        this.crsRoCode = crsRoCode;
    }

    public String getCrsSuffix1() {
        return crsSuffix1;
    }

    public void setCrsSuffix1(String crsSuffix1) {
        this.crsSuffix1 = crsSuffix1;
    }

    public String getCrsSuffix2() {
        return crsSuffix2;
    }

    public void setCrsSuffix2(String crsSuffix2) {
        this.crsSuffix2 = crsSuffix2;
    }

    public String getCrsSuffix3() {
        return crsSuffix3;
    }

    public void setCrsSuffix3(String crsSuffix3) {
        this.crsSuffix3 = crsSuffix3;
    }

    public String getCrsTMBCustId() {
        return crsTMBCustId;
    }

    public void setCrsTMBCustId(String crsTMBCustId) {
        this.crsTMBCustId = crsTMBCustId;
    }

    public String getCrsUKCANumber() {
        return crsUKCANumber;
    }

    public void setCrsUKCANumber(String crsUKCANumber) {
        this.crsUKCANumber = crsUKCANumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("crsAccount1", crsAccount1)
                .append("crsAccount2", crsAccount2)
                .append("crsAccount3", crsAccount3)
                .append("crsBranchCode", crsBranchCode)
                .append("crsCancelCA", crsCancelCA)
                .append("crsCreateDate", crsCreateDate)
                .append("crsCustName", crsCustName)
                .append("crsCusType", crsCusType)
                .append("crsHubCode", crsHubCode)
                .append("crsIdNumber", crsIdNumber)
                .append("crsIdType", crsIdType)
                .append("crsLastUpdate", crsLastUpdate)
                .append("crsRegionCode", crsRegionCode)
                .append("crsRoCode", crsRoCode)
                .append("crsSuffix1", crsSuffix1)
                .append("crsSuffix2", crsSuffix2)
                .append("crsSuffix3", crsSuffix3)
                .append("crsTMBCustId", crsTMBCustId)
                .append("crsUKCANumber", crsUKCANumber)
                .toString();
    }
}

