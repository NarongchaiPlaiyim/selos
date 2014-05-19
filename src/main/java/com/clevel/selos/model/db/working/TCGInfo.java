package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    @Column(name = "payin_slip_send_date", nullable = true)
    private Date payinSlipSendDate;

    @Column(name = "receive_tcg_slip", nullable = true)
    private int receiveTCGSlip;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tcg_submit_date", nullable = true)
    private Date tcgSubmitDate;

    @Column(name = "approve_result", nullable = true)
    private int approvedResult;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approve_date", nullable = true)
    private Date approveDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @OneToOne
    @JoinColumn(name = "workcase_id")
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
	
	public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("approveDate", approveDate)
                .append("approvedResult", approvedResult)
                .append("tcgSubmitDate", tcgSubmitDate)
                .append("payinSlipSendDate", payinSlipSendDate)
                .append("receiveTCGSlip", receiveTCGSlip)
                .toString();
    }

    
}
