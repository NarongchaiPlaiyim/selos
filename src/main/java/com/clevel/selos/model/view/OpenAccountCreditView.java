package com.clevel.selos.model.view;

import com.clevel.selos.util.Util;

public class OpenAccountCreditView extends CreditDetailSimpleView implements Comparable<OpenAccountCreditView>,Cloneable {
	private static final long serialVersionUID = -4152575932252205392L;
	private long openAccountCreditId;
	private boolean fromPledge;
	private boolean checked; 
	
	public OpenAccountCreditView() {
		
	}

	public long getOpenAccountCreditId() {
		return openAccountCreditId;
	}

	public void setOpenAccountCreditId(long openAccountCreditId) {
		this.openAccountCreditId = openAccountCreditId;
	}

	public boolean isFromPledge() {
		return fromPledge;
	}

	public void setFromPledge(boolean fromPledge) {
		this.fromPledge = fromPledge;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public int compareTo(OpenAccountCreditView obj) {
		if (obj == null)
			return 1;
		if (this == obj)
			return 0;
		return Util.compareLong(getId(), obj.getId());
	}
	@Override
	public OpenAccountCreditView clone() {
		OpenAccountCreditView clone = new OpenAccountCreditView();
		clone.setId(getId());
		clone.setAccountName(getAccountName());
		clone.setAccountNo(getAccountNo());
		clone.setAccountStatus(getAccountStatus());
		clone.setProductProgram(getProductProgram());
		clone.setCreditFacility(getCreditFacility());
		clone.setLimit(getLimit());
		clone.setHasAccountInfo(isHasAccountInfo());
		clone.setNewCredit(isNewCredit());
		clone.setOpenAccountCreditId(openAccountCreditId);
		clone.setFromPledge(fromPledge);
		clone.setChecked(checked);
		return clone;
	}
}
