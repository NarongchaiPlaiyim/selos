package com.clevel.selos.model.db.ext.coms;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ext_coms_app_index")
public class AgreementAppIndex {
    @Id
    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "app_number", length = 16,nullable = false)
    protected String appNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "extract_date")
    private Date extractDate;

    @Column(name = "extract_type", length = 1)
    private String extractType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne()
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public Date getExtractDate() {
        return extractDate;
    }

    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    public String getExtractType() {
        return extractType;
    }

    public void setExtractType(String extractType) {
        this.extractType = extractType;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("workCase", workCase != null?workCase.getId():"null")
                .append("appNumber", appNumber)
                .append("extractDate", extractDate)
                .append("extractType", extractType)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy != null? modifyBy.getId():"null")
                .toString();
    }
}
