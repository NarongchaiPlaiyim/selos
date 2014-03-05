package com.clevel.selos.integration.ecm.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ECMDetail implements Serializable{
    private String ecmDocId;//ECM_DOC_ID   ---
    private String caNumber;//CA_NUMBER
    private String fnDocId;//FN_DOC_ID     ---
    private String orgFileName;//ORG_FILENAME
    private String typeCode;//TYPE_CODE
    private String typeNameTH;//TYPE_NAME_TH

    public String getEcmDocId() {
        return ecmDocId;
    }

    public void setEcmDocId(String ecmDocId) {
        this.ecmDocId = ecmDocId;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getFnDocId() {
        return fnDocId;
    }

    public void setFnDocId(String fnDocId) {
        this.fnDocId = fnDocId;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeNameTH() {
        return typeNameTH;
    }

    public void setTypeNameTH(String typeNameTH) {
        this.typeNameTH = typeNameTH;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ecmDocId", ecmDocId)
                .append("caNumber", caNumber)
                .append("fnDocId", fnDocId)
                .append("orgFileName", orgFileName)
                .append("typeCode", typeCode)
                .append("typeNameTH", typeNameTH)
                .toString();
    }
}
