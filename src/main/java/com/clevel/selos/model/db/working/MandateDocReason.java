package com.clevel.selos.model.db.working;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mandate_doc_reason")
public class MandateDocReason implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_REASON_ID", sequenceName = "SEQ_WRK_MANDATE_REASON_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_REASON_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_doc_id")
    private MandateDoc mandateDoc;

    @Column(name = "reason_incomplete", columnDefinition = "int default 0")
    private int reasonIncomplete;

    @Column(name = "reason_indistinct", columnDefinition = "int default 0")
    private int reasonIndistinct;

    @Column(name = "reason_incorrect", columnDefinition = "int default 0")
    private int reasonIncorrect;

    @Column(name = "reason_expire", columnDefinition = "int default 0")
    private int reasonExpire;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateDoc getMandateDoc() {
        return mandateDoc;
    }

    public void setMandateDoc(MandateDoc mandateDoc) {
        this.mandateDoc = mandateDoc;
    }

    public int getReasonIncomplete() {
        return reasonIncomplete;
    }

    public void setReasonIncomplete(int reasonIncomplete) {
        this.reasonIncomplete = reasonIncomplete;
    }

    public int getReasonIndistinct() {
        return reasonIndistinct;
    }

    public void setReasonIndistinct(int reasonIndistinct) {
        this.reasonIndistinct = reasonIndistinct;
    }

    public int getReasonIncorrect() {
        return reasonIncorrect;
    }

    public void setReasonIncorrect(int reasonIncorrect) {
        this.reasonIncorrect = reasonIncorrect;
    }

    public int getReasonExpire() {
        return reasonExpire;
    }

    public void setReasonExpire(int reasonExpire) {
        this.reasonExpire = reasonExpire;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateDoc", mandateDoc)
                .append("reasonIncomplete", reasonIncomplete)
                .append("reasonIndistinct", reasonIndistinct)
                .append("reasonIncorrect", reasonIncorrect)
                .append("reasonExpire", reasonExpire)
                .toString();
    }
}
