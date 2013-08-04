package com.clevel.selos.model.db.ext.csi;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "ext_csi")
public class CSIData {
    @Id
    @SequenceGenerator(name="SEQ_EXT_CSI_ID", sequenceName="SEQ_EXT_CSI_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_EXT_CSI_ID")
    private long id;
    @Column(name="citizen_id", length = 13)
    private String citizenId;
    @Column(name="passport_no", length = 25)
    private String passportNo;
    @Column(name="business_reg_no", length = 25)
    private String businessRegNo;
    @Column(name="name_th", length = 100)
    private String nameTh;
    @Column(name="name_en", length = 100)
    private String nameEn;
    @Column(name="warning_code", length = 4)
    private String warningCode;
    @Column(name="source", length = 10)
    private String source;
    @Column(name="data_date", length = 8)
    private String dataDate;
    @Column(name="date_warning_code", length = 8)
    private String dateWarningCode;

    public CSIData() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("citizenId", citizenId).
                append("passportNo", passportNo).
                append("businessRegNo", businessRegNo).
                append("nameTh", nameTh).
                append("nameEn", nameEn).
                append("warningCode", warningCode).
                append("source", source).
                append("dataDate", dataDate).
                append("dateWarningCode", dateWarningCode).
                toString();
    }
}
