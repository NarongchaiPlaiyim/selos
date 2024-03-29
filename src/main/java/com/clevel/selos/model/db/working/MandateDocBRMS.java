package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mandate_doc_brms")
public class MandateDocBRMS implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_BRMS_ID", sequenceName = "SEQ_WRK_MANDATE_BRMS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_BRMS_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_doc_id")
    private MandateDocDetail mandateDocDetail;

    @Column(name = "brms_doc_type")
    private String BRMSDocType;

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

    public String getBRMSDocType() {
        return BRMSDocType;
    }

    public void setBRMSDocType(String BRMSDocType) {
        this.BRMSDocType = BRMSDocType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("BRMSDocType", BRMSDocType)
                .toString();
    }
}
