package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

import com.clevel.selos.util.Util;

public class MortgageInfoCollSubView implements Serializable,Comparable<MortgageInfoCollSubView> {
	private static final long serialVersionUID = -8382657364110336483L;
	private long id;
	private long newCollSubId;
	
	private String jobNo;
	private String hcNo;
	private String subNo;
	private String owner;
	private String collateralType;
	private String titleDeed;
	private String mortgageDetail;
	private String relatedWith;
	private BigDecimal appraisalValue;
	private boolean main;
	
	public MortgageInfoCollSubView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNewCollSubId() {
		return newCollSubId;
	}

	public void setNewCollSubId(long newCollSubId) {
		this.newCollSubId = newCollSubId;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getHcNo() {
		return hcNo;
	}

	public void setHcNo(String hcNo) {
		this.hcNo = hcNo;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCollateralType() {
		return collateralType;
	}
	
	public void setCollateralType(String collateralType) {
		this.collateralType = collateralType;
	}
	

	public String getTitleDeed() {
		return titleDeed;
	}

	public void setTitleDeed(String titleDeed) {
		this.titleDeed = titleDeed;
	}

	public String getMortgageDetail() {
		return mortgageDetail;
	}

	public void setMortgageDetail(String mortgageDetail) {
		this.mortgageDetail = mortgageDetail;
	}

	public String getRelatedWith() {
		return relatedWith;
	}

	public void setRelatedWith(String relatedWith) {
		this.relatedWith = relatedWith;
	}

	public BigDecimal getAppraisalValue() {
		return appraisalValue;
	}

	public void setAppraisalValue(BigDecimal appraisalValue) {
		this.appraisalValue = appraisalValue;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}
	
	public int compareTo(MortgageInfoCollSubView obj) {
		if (obj == null)
			return 1;
		if (this == obj)
			return 0;
		if (this.main != obj.main) {
			if (this.main)
				return -1;
			else
				return 1;
		}
		return Util.compareLong(newCollSubId, obj.newCollSubId);
	}
}
