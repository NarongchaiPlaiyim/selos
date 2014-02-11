package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;


public class TCGInfoView implements Serializable{
	private static final long serialVersionUID = 1227047620433296147L;
	private long id;
    private Date payinSlipSendDate;
    private int receiveTCGSlip;
    private Date tcgSubmitDate;
    private int approvedResult;
    private Date approveDate;
    
    public TCGInfoView() {
    	
    }
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
	
}
