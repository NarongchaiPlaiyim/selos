package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mandate_doc_fileName")
public class MandateDocFileName implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_FILE_ID", sequenceName = "SEQ_WRK_MANDATE_FILE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_FILE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_doc_id")
    private MandateDocDetail mandateDocDetail;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "fn_doc_id", length = 19)
    private String fnDocId;

    @Column(name = "ecm_doc_id", length = 19)
    private String ecmDocId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateDocDetail getMandateDocDetail() {
        return mandateDocDetail;
    }

    public void setMandateDocDetail(MandateDocDetail mandateDocDetail) {
        this.mandateDocDetail = mandateDocDetail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFnDocId() {
        return fnDocId;
    }

    public void setFnDocId(String fnDocId) {
        this.fnDocId = fnDocId;
    }

    public String getEcmDocId() {
        return ecmDocId;
    }

    public void setEcmDocId(String ecmDocId) {
        this.ecmDocId = ecmDocId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("fileName", fileName)
                .append("fnDocId", fnDocId)
                .append("ecmDocId", ecmDocId)
                .toString();
    }
}
