package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Reason;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_cancel_reject_info")
public class CancelRejectInfo implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CANCEL_REJECT_ID", sequenceName = "SEQ_WRK_CANCEL_REJECT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CANCEL_REJECT_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
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
                .append("workCasePrescreen", workCasePrescreen)
                .append("workCase", workCase)
                .append("reason", reason)
                .toString();
    }
}
