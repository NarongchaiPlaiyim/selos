package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Role;
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

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "ecm_doc_type")
    private String ecmDocType;

    @Column(name = "ecm_doc_type_desc")
    private String ecmDocTypeDesc;

    @Column(name = "mandate_type", columnDefinition = "int default 0")
    private int mandateType;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEcmDocType() {
        return ecmDocType;
    }

    public void setEcmDocType(String ecmDocType) {
        this.ecmDocType = ecmDocType;
    }

    public String getEcmDocTypeDesc() {
        return ecmDocTypeDesc;
    }

    public void setEcmDocTypeDesc(String ecmDocTypeDesc) {
        this.ecmDocTypeDesc = ecmDocTypeDesc;
    }

    public int getMandateType() {
        return mandateType;
    }

    public void setMandateType(int mandateType) {
        this.mandateType = mandateType;
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
                .append("ecmDocType", ecmDocType)
                .append("ecmDocTypeDesc", ecmDocTypeDesc)
                .append("mandateType", mandateType)
                .append("isCompleted", isCompleted)
                .append("remark", remark)
                .append("mandateDocBRMSList", mandateDocBRMSList)
                .append("mandateDocReasonList", mandateDocReasonList)
                .append("mandateDocCustList", mandateDocCustList)
                .toString();
    }
}
