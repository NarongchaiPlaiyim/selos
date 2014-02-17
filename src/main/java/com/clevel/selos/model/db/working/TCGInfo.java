package com.clevel.selos.model.db.working;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_tcg_info")
public class TCGInfo implements Serializable {

    private static final long serialVersionUID = 5620522401365835268L;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getPayinSlipSendDate() {
		return payinSlipSendDate;
	}

	public void setPayinSlipSendDate(Date payinSlipSendDate) {
		this.payinSlipSendDate = payinSlipSendDate;
	}

	public int getReceiveTCGSlip() {
		return receiveTCGSlip;
	}

	public void setReceiveTCGSlip(int receiveTCGSlip) {
		this.receiveTCGSlip = receiveTCGSlip;
	}

	public Date getTcgSubmitDate() {
		return tcgSubmitDate;
	}

	public void setTcgSubmitDate(Date tcgSubmitDate) {
		this.tcgSubmitDate = tcgSubmitDate;
	}

	public int getApprovedResult() {
		return approvedResult;
	}

	public void setApprovedResult(int approvedResult) {
		this.approvedResult = approvedResult;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public WorkCase getWorkCase() {
		return workCase;
	}

	public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

    
}
