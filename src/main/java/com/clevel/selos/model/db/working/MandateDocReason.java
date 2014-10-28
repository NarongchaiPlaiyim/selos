package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Reason;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_mandate_doc_reason")
public class MandateDocReason implements Serializable{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_REASON_ID", sequenceName = "SEQ_WRK_MANDATE_REASON_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_REASON_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_doc_id")
    private MandateDocDetail mandateDocDetail;

    @OneToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

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

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("reason", reason)
                .toString();
    }
}
