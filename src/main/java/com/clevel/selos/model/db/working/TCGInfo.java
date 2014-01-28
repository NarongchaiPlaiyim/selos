package com.clevel.selos.model.db.working;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_tcg_info")
public class TCGInfo {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_TCG_INFO_ID", sequenceName = "SEQ_WRK_TCG_INFO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_TCG_INFO_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payin_slip_send_date")
    private Date payinSlipSendDate;

    @Column(name = "receive_tcg_slip")
    private int receiveTCGSlip;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tcg_submit_date")
    private Date tcgSubmitDate;

    @Column(name = "approve_result")
    private int approvedResult;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approve_date")
    private Date approveDate;

    @OneToOne
    @JoinColumn(name = "wrk_case_id")
    private WorkCase workCase;

}
