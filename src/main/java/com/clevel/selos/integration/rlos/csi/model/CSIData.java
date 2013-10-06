package com.clevel.selos.integration.rlos.csi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CSIData implements Serializable{
    private String citizenId;
    private String passportNo;
    private String businessRegNo;
    private String nameTh;
    private String nameEn;
    private String warningCode;
    private String source;
    private String dataDate;
    private String dateWarningCode;

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getBusinessRegNo() {
        return businessRegNo;
    }

    public void setBusinessRegNo(String businessRegNo) {
        this.businessRegNo = businessRegNo;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getWarningCode() {
        return warningCode;
    }

    public void setWarningCode(String warningCode) {
        this.warningCode = warningCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getDateWarningCode() {
        return dateWarningCode;
    }

    public void setDateWarningCode(String dateWarningCode) {
        this.dateWarningCode = dateWarningCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("citizenId", citizenId)
                .append("passportNo", passportNo)
                .append("businessRegNo", businessRegNo)
                .append("nameTh", nameTh)
                .append("nameEn", nameEn)
                .append("warningCode", warningCode)
                .append("source", source)
                .append("dataDate", dataDate)
                .append("dateWarningCode", dateWarningCode)
                .toString();
    }
}
