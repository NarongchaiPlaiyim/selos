package com.clevel.selos.model.view;

import java.io.Serializable;

public class OpenAccountNameView implements Serializable,Cloneable {
	private static final long serialVersionUID = 6574656122407036912L;
	private long id;
	private long customerId;
	private String name;
	private boolean fromPledge;
	
	public OpenAccountNameView() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFromPledge() {
		return fromPledge;
	}

	public void setFromPledge(boolean fromPledge) {
		this.fromPledge = fromPledge;
	}
	@Override
	public OpenAccountNameView clone() {
		OpenAccountNameView clone = new OpenAccountNameView();
		clone.setId(id);
		clone.setCustomerId(customerId);
		clone.setFromPledge(fromPledge);
		clone.setName(name);
		return clone;
	}
}
