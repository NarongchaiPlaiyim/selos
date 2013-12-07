package com.clevel.selos.integration.coms.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubCollateral implements Serializable {
    private String colId; //1. COL_ID
    private String headColId; //2. HEAD_COL_ID
    private long lineNo; //3. LINE_NO
    private String colType; //4. COL_TYPE
    private String colSubType; //5. COL_SUB_TYPE
    private String ownDocNo; //6. OWN_DOC_NO
    private String cusId; //7. APPR_PRICE_TR_OWNER.CUS_ID
    private String preName; //8. PRENAME.PRENAME
    private String name; //9. ONL_PERSON.NAME
    private String sirName; //10. ONL_PERSON.SIR_NAME
    private BigDecimal cPrice; //11. C_PRICE
    private BigDecimal matiPrice; //12. MATI_PRICE
    private String onlType; //13. ONL_TYPE

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getHeadColId() {
        return headColId;
    }

    public void setHeadColId(String headColId) {
        this.headColId = headColId;
    }

    public long getLineNo() {
        return lineNo;
    }

    public void setLineNo(long lineNo) {
        this.lineNo = lineNo;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getColSubType() {
        return colSubType;
    }

    public void setColSubType(String colSubType) {
        this.colSubType = colSubType;
    }

    public String getOwnDocNo() {
        return ownDocNo;
    }

    public void setOwnDocNo(String ownDocNo) {
        this.ownDocNo = ownDocNo;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSirName() {
        return sirName;
    }

    public void setSirName(String sirName) {
        this.sirName = sirName;
    }

    public BigDecimal getcPrice() {
        return cPrice;
    }

    public void setcPrice(BigDecimal cPrice) {
        this.cPrice = cPrice;
    }

    public BigDecimal getMatiPrice() {
        return matiPrice;
    }

    public void setMatiPrice(BigDecimal matiPrice) {
        this.matiPrice = matiPrice;
    }

    public String getOnlType() {
        return onlType;
    }

    public void setOnlType(String onlType) {
        this.onlType = onlType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("colId", colId)
                .append("headColId", headColId)
                .append("lineNo", lineNo)
                .append("colType", colType)
                .append("colSubType", colSubType)
                .append("ownDocNo", ownDocNo)
                .append("cusId", cusId)
                .append("preName", preName)
                .append("name", name)
                .append("sirName", sirName)
                .append("cPrice", cPrice)
                .append("matiPrice", matiPrice)
                .append("onlType", onlType)
                .toString();
    }
}
