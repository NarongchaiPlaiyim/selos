package com.clevel.selos.model.view;

import com.clevel.selos.model.RadioValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public  abstract class CustomerInfoPostBaseView <T extends CustomerInfoPostBaseView<T>> implements Serializable {
	private static final long serialVersionUID = 863095588631439899L;
	protected long id;
	private int relationId;
	protected RadioValue collateralOwner;
	protected String displayDocumentType;
	
	protected String personalId;
	protected String tmbCustomerId;
	protected int age;
	protected int titleId;
	protected String nameTH;
	protected String mobile;
	protected String fax;
	protected String email;
	protected int mailingAddressTypeId;
	protected int businessTypeId;
	
	protected String displayRelation;
	protected String displayTitle;
	protected String displayMailingAddressType;
	protected String displayBusinessType;
	
	protected long workCaseId;
	
	protected List<CustomerInfoPostAddressView> addresses;
	
	protected abstract void updateOwnValue(T view);
	public abstract void calculateAge();
	public abstract int getDefaultCustomerEntityId();
	
	public CustomerInfoPostBaseView() {
		addresses = new ArrayList<CustomerInfoPostAddressView>();
		collateralOwner = RadioValue.NA;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RadioValue getCollateralOwner() {
		if (collateralOwner == null)
			collateralOwner = RadioValue.NA;
		return collateralOwner;
	}

	public void setCollateralOwner(RadioValue collateralOwner) {
		this.collateralOwner = collateralOwner;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public String getNameTH() {
		return nameTH;
	}

	public void setNameTH(String nameTH) {
		this.nameTH = nameTH;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<CustomerInfoPostAddressView> getAddresses() {
		return addresses;
	}
	
	public void addAddress(CustomerInfoPostAddressView address) {
		this.addresses.add(address);
	}
	public int getMailingAddressTypeId() {
		return mailingAddressTypeId;
	}
	public void setMailingAddressTypeId(int mailingAddressTypeId) {
		this.mailingAddressTypeId = mailingAddressTypeId;
	}
	public int getBusinessTypeId() {
		return businessTypeId;
	}
	public void setBusinessTypeId(int businessTypeId) {
		this.businessTypeId = businessTypeId;
	}
	
	public String getDisplayRelation() {
		return displayRelation;
	}
	public void setDisplayRelation(String displayRelation) {
		this.displayRelation = displayRelation;
	}
	public String getDisplayDocumentType() {
		return displayDocumentType;
	}
	public void setDisplayDocumentType(String displayDocumentType) {
		this.displayDocumentType = displayDocumentType;
	}
	public String getDisplayBusinessType() {
		return displayBusinessType;
	}
	public void setDisplayBusinessType(String displayBusinessType) {
		this.displayBusinessType = displayBusinessType;
	}
	public void setDisplayMailingAddressType(String displayMailingAddressType) {
		this.displayMailingAddressType = displayMailingAddressType;
	}
	public String getDisplayMailingAddressType() {
		return displayMailingAddressType;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	public String getDisplayTitle() {
		return displayTitle;
	}
	public int getRelationId() {
		return relationId;
	}
	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}
	public String getTmbCustomerId() {
		return tmbCustomerId;
	}
	public void setTmbCustomerId(String tmbCustomerId) {
		this.tmbCustomerId = tmbCustomerId;
	}
	public final void updateValue(T view) {
		_updateBaseValue(view);
		updateOwnValue(view);
	}
	public long getWorkCaseId() {
		return workCaseId;
	}
	public void setWorkCaseId(long workCaseId) {
		this.workCaseId = workCaseId;
	}
	private void _updateBaseValue(T view) {
		id = view.id;
		collateralOwner = view.collateralOwner;
		personalId = view.personalId;
		tmbCustomerId = view.tmbCustomerId;
		age =view.age;
		titleId = view.titleId;
		nameTH = view.nameTH;
		mobile = view.mobile;
		fax = view.fax;
		email =view.email;
		mailingAddressTypeId = view.mailingAddressTypeId;
		businessTypeId = view.businessTypeId;
		displayBusinessType = view.displayBusinessType;
		displayDocumentType = view.displayDocumentType;
		displayRelation = view.displayRelation;
		displayTitle = view.displayTitle;
		displayMailingAddressType = view.displayMailingAddressType;
		workCaseId = view.workCaseId;
//		relationId = view.relationId;
				
		addresses.clear();
		for (CustomerInfoPostAddressView address : view.addresses) {
			CustomerInfoPostAddressView toAdd = new CustomerInfoPostAddressView();
			toAdd.updateValue(address);
			addAddress(toAdd);
		}
	}
}
