package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clevel.selos.model.ConfirmAccountType;
import com.clevel.selos.model.RequestAccountType;

public class OpenAccountFullView implements Serializable,Cloneable {
	private static final long serialVersionUID = 5325274175463654261L;
	private long id;
	private RequestAccountType requestAccountType;
	private String accountNo;
	private int branchId;
	private int accountTypeId;
	private int productTypeId;
	private String term;
	
	private List<OpenAccountCreditView> credits;
	private List<OpenAccountPurposeView> purposes;
	private List<OpenAccountNameView> names;
	
	private String displayBranch;
	private String displayAccountType;
	private String displayProductType;
	private boolean fromPledge;
	
	//For using in accountInfo
	private List<OpenAccountNameView> deleteNames;
	private boolean needUpdate = false;
	public OpenAccountFullView() {
		requestAccountType = RequestAccountType.NA;
		credits = new ArrayList<OpenAccountCreditView>();
		purposes = new ArrayList<OpenAccountPurposeView>();
		names = new ArrayList<OpenAccountNameView>();
		deleteNames = new ArrayList<OpenAccountNameView>();
		fromPledge = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RequestAccountType getRequestAccountType() {
		if (requestAccountType == null)
			requestAccountType = RequestAccountType.NA;
		return requestAccountType;
	}

	public void setRequestAccountType(RequestAccountType requestAccountType) {
		this.requestAccountType = requestAccountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(int accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<OpenAccountCreditView> getCredits() {
		return credits;
	}

	public void setCredits(List<OpenAccountCreditView> credits) {
		this.credits = credits;
	}

	public List<OpenAccountPurposeView> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<OpenAccountPurposeView> purposes) {
		this.purposes = purposes;
	}

	public String getDisplayBranch() {
		return displayBranch;
	}

	public void setDisplayBranch(String displayBranch) {
		this.displayBranch = displayBranch;
	}

	public String getDisplayAccountType() {
		return displayAccountType;
	}

	public void setDisplayAccountType(String displayAccountType) {
		this.displayAccountType = displayAccountType;
	}

	public String getDisplayProductType() {
		return displayProductType;
	}

	public void setDisplayProductType(String displayProductType) {
		this.displayProductType = displayProductType;
	}
	
	public boolean isFromPledge() {
		return fromPledge;
	}
	public void setFromPledge(boolean fromPledge) {
		this.fromPledge = fromPledge;
	}
	
	public List<OpenAccountNameView> getNames() {
		return names;
	}
	public void setNames(List<OpenAccountNameView> names) {
		this.names = names;
	}
	public List<OpenAccountNameView> getDeleteNames() {
		return deleteNames;
	}
	
	public ConfirmAccountType getConfirmAccountType() {
		if (requestAccountType == null)
			return ConfirmAccountType.NA;
		switch (requestAccountType) {
			case NEW : return ConfirmAccountType.OPEN;
			case EXISTING : return ConfirmAccountType.NOT_OPEN;
			default :
				return ConfirmAccountType.NA;
		}
	}
	
	public String getAllAccountNames() {
		StringBuilder builder = new StringBuilder();
		List<OpenAccountNameView> names = getNames();
		if (names != null && !names.isEmpty()) {
    		for (OpenAccountNameView name : names) {
    			builder.append(name.getName());
    			builder.append("<br/>");
    		}
    		builder.setLength(builder.length() - 5);
    	}
		return builder.toString();
	}
	public String getAllPurposes() {
		StringBuilder builder = new StringBuilder();
		List<OpenAccountPurposeView> purposes = getPurposes();
		if (purposes != null && !purposes.isEmpty()) {
			for (OpenAccountPurposeView purpose : purposes) {
				if (!purpose.isChecked())
					continue;
				builder.append(purpose.getPurpose());
				builder.append("<br/>");
			}
			if (builder.length() > 0)
				builder.setLength(builder.length() - 5);
		}
		return builder.toString();
	}
	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}
	public boolean isNeedUpdate() {
		return needUpdate;
	}
	
	public void deleteOpenAccountName(OpenAccountNameView view) {
		if (view == null)
			return;
		names.remove(view);
		if (view.getId() <= 0)
			return;
		deleteNames.add(view);
	}
	
	public void updateValues(OpenAccountFullView view) {
		id = view.id;
		requestAccountType = view.requestAccountType;
		accountNo = view.accountNo;
		branchId = view.branchId;
		accountTypeId = view.accountTypeId;
		productTypeId = view.productTypeId;
		term = view.term;
		
		displayAccountType = view.displayAccountType;
		displayBranch = view.displayBranch;
		displayProductType = view.displayProductType;
		fromPledge = view.fromPledge;
		needUpdate = view.needUpdate;
		
		credits.clear();
		for (OpenAccountCreditView data : view.credits) {
			credits.add(data.clone());
		}
		purposes.clear();
		for (OpenAccountPurposeView data : view.purposes) {
			purposes.add(data.clone());
		}
		names.clear();
		for (OpenAccountNameView data : view.names) {
			names.add(data.clone());
		}
		deleteNames.clear();
		for (OpenAccountNameView data : view.deleteNames) {
			deleteNames.add(data.clone());
		}
	}
}
