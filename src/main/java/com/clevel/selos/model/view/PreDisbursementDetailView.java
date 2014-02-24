package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;


public class PreDisbursementDetailView implements Serializable {

	private long id;
	private long preDisbursement_id;
	private int preDisbursementData_id;
	private int value = 0;
	private String description;
	private Date submission_date = new Date();

	public PreDisbursementDetailView() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPreDisbursementData_id() {
		return preDisbursementData_id;
	}

	public void setPreDisbursementData_id(int preDisbursementData_id) {
		this.preDisbursementData_id = preDisbursementData_id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPreDisbursement_id() {
		return preDisbursement_id;
	}

	public void setPreDisbursement_id(long preDisbursement_id) {
		this.preDisbursement_id = preDisbursement_id;
	}

	public Date getSubmission_date() {
		return submission_date;
	}

	public void setSubmission_date(Date submission_date) {
		this.submission_date = submission_date;
	}
}
