package com.clevel.selos.model.db.working;

import com.clevel.selos.model.DocMandateType;
import com.clevel.selos.model.RadioValue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wrk_mandate_doc_detail")
public class MandateDocDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_DOC_DET_ID", sequenceName = "SEQ_WRK_MANDATE_DOC_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_DOC_DET_ID")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "mandate_doc_sum_id")
    private MandateDocSummary mandateDocSummary;

    @Column(name = "ecm_doc_type")
    private String ecmDocType;

    @Column(name = "ecm_doc_type_desc")
    private String ecmDocTypeDesc;

    @Column(name = "mandate_type", columnDefinition = "int default 0")
    private DocMandateType mandateType;

    @Column(name = "is_completed", columnDefinition = "int default 0")
    private RadioValue completedFlag;

    @Column(name = "remark")
    private String remark;

    @Column(name = "display", columnDefinition = "int default 0")
    private boolean display;

    @OneToMany(mappedBy = "mandateDocDetail", cascade = CascadeType.ALL)
    private List<MandateDocReason> mandateDocReasonList;

    @OneToMany(mappedBy = "mandateDocDetail", cascade = CascadeType.ALL)
    private List<MandateDocBRMS> mandateDocBRMSList;

    @OneToMany(mappedBy = "mandateDocDetail", cascade = CascadeType.ALL)
    private List<MandateDocCust> mandateDocCustList;

    @OneToMany(mappedBy = "mandateDocDetail", cascade = CascadeType.ALL)
    private List<MandateDocFileName> mandateDocFileNameList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateDocSummary getMandateDocSummary() {
        return mandateDocSummary;
    }

    public void setMandateDocSummary(MandateDocSummary mandateDocSummary) {
        this.mandateDocSummary = mandateDocSummary;
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

    public DocMandateType getMandateType() {
        return mandateType;
    }

    public void setMandateType(DocMandateType mandateType) {
        this.mandateType = mandateType;
    }

    public RadioValue getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(RadioValue completedFlag) {
        this.completedFlag = completedFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public List<MandateDocReason> getMandateDocReasonList() {
        return mandateDocReasonList;
    }

    public void setMandateDocReasonList(List<MandateDocReason> mandateDocReasonList) {
        this.mandateDocReasonList = mandateDocReasonList;
    }

    public List<MandateDocBRMS> getMandateDocBRMSList() {
        return mandateDocBRMSList;
    }

    public void setMandateDocBRMSList(List<MandateDocBRMS> mandateDocBRMSList) {
        this.mandateDocBRMSList = mandateDocBRMSList;
    }

    public List<MandateDocCust> getMandateDocCustList() {
        return mandateDocCustList;
    }

    public void setMandateDocCustList(List<MandateDocCust> mandateDocCustList) {
        this.mandateDocCustList = mandateDocCustList;
    }

    public List<MandateDocFileName> getMandateDocFileNameList() {
        return mandateDocFileNameList;
    }

    public void setMandateDocFileNameList(List<MandateDocFileName> mandateDocFileNameList) {
        this.mandateDocFileNameList = mandateDocFileNameList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("ecmDocType", ecmDocType)
                .append("mandateType", mandateType)
                .append("completedFlag", completedFlag)
                .append("remark", remark)
                .append("mandateDocReasonList", mandateDocReasonList)
                .append("mandateDocBRMSList", mandateDocBRMSList)
                .append("mandateDocCustList", mandateDocCustList)
                .append("mandateDocFileNameList", mandateDocFileNameList)
                .toString();
    }
}
