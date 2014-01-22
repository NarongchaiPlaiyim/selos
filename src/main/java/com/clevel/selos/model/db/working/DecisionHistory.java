package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_decision_history")
public class DecisionHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DECISION_HISTORY_ID", sequenceName = "SEQ_WRK_DECISION_HISTORY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DECISION_HISTORY_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "limit_propose")
    private BigDecimal limitPropose;

    @Column(name = "limit_final")
    private BigDecimal limitFinal;

    @Column(name = "is_cancel")
    private int cancel;

    @Column(name = "remark")
    private String remark;

    @OneToOne
    @JoinColumn(name = "uw_id")
    private User uw;

    @OneToOne
    @JoinColumn(name = "uw_lead_id")
    private User uwLead;

    @OneToOne
    @JoinColumn(name = "uw_head_id")
    private User uwHead;

    @OneToOne
    @JoinColumn(name = "sso_id")
    private User sso;

    @Column(name = "rejected_date")
    private Date rejectedDate;

    @OneToOne
    @JoinColumn(name = "rejected_reason_id")
    private Reason rejectedReason;



}
