package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PledgeACDepView implements Serializable {
	private static final long serialVersionUID = 6818524179568474194L;
	private long id;
	private String dep;
	private BigDecimal holdAmount;
	
	private boolean updatable = true;
	private boolean deletable = true;
	private boolean needUpdate;
	
	public PledgeACDepView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public BigDecimal getHoldAmount() {
		return holdAmount;
	}

	public void setHoldAmount(BigDecimal holdAmount) {
		this.holdAmount = holdAmount;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}
	
	public boolean isNew() {
		return id <= 0;
	}
	public boolean isDeletable() {
		return deletable;
	}
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
	public boolean isUpdatable() {
		return updatable;
	}
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("dep", dep)
                .append("holdAmount", holdAmount)
                .append("deletable", deletable)
                .append("updatable", updatable)
                .append("needUpdate", needUpdate)
                .toString();
    }
}
