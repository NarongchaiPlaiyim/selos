package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wrk_mandate_doc")
public class MandateDoc implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_ID", sequenceName = "SEQ_WRK_MANDATE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @Column(name = "role")
    private String role;

    @Column(name = "is_completed", columnDefinition = "int default 0")
    private int isCompleted;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocBRMS> mandateDocBRMSList;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocReason> mandateDocReasonList;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocCust> mandateDocCustList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getCompleted() {
        return isCompleted;
    }

    public void setCompleted(int completed) {
        isCompleted = completed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<MandateDocBRMS> getMandateDocBRMSList() {
        return mandateDocBRMSList;
    }

    public void setMandateDocBRMSList(List<MandateDocBRMS> mandateDocBRMSList) {
        this.mandateDocBRMSList = mandateDocBRMSList;
    }

    public List<MandateDocReason> getMandateDocReasonList() {
        return mandateDocReasonList;
    }

    public void setMandateDocReasonList(List<MandateDocReason> mandateDocReasonList) {
        this.mandateDocReasonList = mandateDocReasonList;
    }

    public List<MandateDocCust> getMandateDocCustList() {
        return mandateDocCustList;
    }

    public void setMandateDocCustList(List<MandateDocCust> mandateDocCustList) {
        this.mandateDocCustList = mandateDocCustList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("role", role)
                .append("isCompleted", isCompleted)
                .append("remark", remark)
                .append("mandateDocBRMSList", mandateDocBRMSList)
                .append("mandateDocReasonList", mandateDocReasonList)
                .append("mandateDocCustList", mandateDocCustList)
                .toString();
    }
}
