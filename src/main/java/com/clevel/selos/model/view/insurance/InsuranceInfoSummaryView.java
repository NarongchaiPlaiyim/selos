package com.clevel.selos.model.view.insurance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InsuranceInfoSummaryView implements Serializable {
    private long id;
    private BigDecimal totalPremiumAmount = BigDecimal.ZERO;
    private Date modifyDate;
    private User modifyBy;
    
    public InsuranceInfoSummaryView() {

    }

	public BigDecimal getTotalPremiumAmount() {
		return totalPremiumAmount;
	}

	public void setTotalPremiumAmount(BigDecimal totalPremiumAmount) {
		this.totalPremiumAmount = totalPremiumAmount;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("totalPremiumAmount", totalPremiumAmount)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
