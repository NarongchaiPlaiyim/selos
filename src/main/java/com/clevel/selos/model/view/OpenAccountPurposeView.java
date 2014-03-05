package com.clevel.selos.model.view;

import java.io.Serializable;

public class OpenAccountPurposeView implements Serializable,Cloneable {
	private static final long serialVersionUID = 1153014153286547712L;
	private long id;
	private String purpose;
	private boolean checked;
	private boolean editable;
	private long purposeId;
	
	public OpenAccountPurposeView() {
		editable = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public long getPurposeId() {
		return purposeId;
	}
	public void setPurposeId(long purposeId) {
		this.purposeId = purposeId;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	@Override
	public OpenAccountPurposeView clone() {
		OpenAccountPurposeView clone = new OpenAccountPurposeView();
		clone.setId(id);
		clone.setPurpose(purpose);
		clone.setPurposeId(purposeId);
		clone.setChecked(checked);
		clone.setEditable(editable);
		clone.setPurposeId(purposeId);
		return clone;
	}
}
