package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CustomerInfoSimpleView implements Serializable {
	private static final long serialVersionUID = -5884116872190014343L;
	private long id;
	private String customerName;
	private String citizenId;
	private String tmbCustomerId;
	private String relation;
	private boolean juristic;
	
	public CustomerInfoSimpleView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getTmbCustomerId() {
		return tmbCustomerId;
	}

	public void setTmbCustomerId(String tmbCustomerId) {
		this.tmbCustomerId = tmbCustomerId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public boolean isJuristic() {
		return juristic;
	}
	public void setJuristic(boolean juristic) {
		this.juristic = juristic;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerName", customerName)
                .append("citizenId", citizenId)
                .append("tmbCustomerId", tmbCustomerId)
                .append("relation", relation)
                .append("juristic", juristic)
                .toString();
    }
}
