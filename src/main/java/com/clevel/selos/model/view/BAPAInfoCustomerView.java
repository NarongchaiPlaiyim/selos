package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.db.master.BAResultHC;

public class BAPAInfoCustomerView implements Serializable , Comparable<BAPAInfoCustomerView>{

	private static final long serialVersionUID = 7955472668207204620L;

	private long id;
	
	//Read only field
	private long customerId;
	private String customerName;
	private String customerContractNo;
	private String relationship;
	
	//Editable field
	private BAResultHC baResultHC;
	private Date checkDate;
	
	private boolean applyBA;
	
	private int updBAResultHC;
	
	//internal use
	private int relationPriority;
	
	public BAPAInfoCustomerView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerContractNo() {
		return customerContractNo;
	}

	public void setCustomerContractNo(String customerContractNo) {
		this.customerContractNo = customerContractNo;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public BAResultHC getBaResultHC() {
		return baResultHC;
	}

	public void setBaResultHC(BAResultHC baResultHC) {
		this.baResultHC = baResultHC;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public boolean isUpdateable() {
		return applyBA;
	}


	public boolean isApplyBA() {
		return applyBA;
	}

	public void setApplyBA(boolean applyBA) {
		this.applyBA = applyBA;
	}

	public int getUpdBAResultHC() {
		return updBAResultHC;
	}

	public void setUpdBAResultHC(int updBAResultHC) {
		this.updBAResultHC = updBAResultHC;
	}
	
	public void setRelationPriority(int relationPriority) {
		this.relationPriority = relationPriority;
	}
	@Override
	public int compareTo(BAPAInfoCustomerView obj) {
		if (obj == null)
			return 1;
		if (this == obj)
			return 0;
		//compare relation
		int compare = relationPriority - obj.relationPriority;
		if (compare > 0)
			return 1;
		else if (compare < 0)
			return -1;
		
		compare = (int) (id - obj.id);
		if (compare > 0)
			return 1;
		else if (compare < 0)
			return -1;
		else
			return 0;
	}
}
