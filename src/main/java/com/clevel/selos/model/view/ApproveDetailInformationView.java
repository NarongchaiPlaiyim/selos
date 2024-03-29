package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApproveDetailInformationView implements Serializable{

	private long id;
	private List<ProposeCreditInfoDetailView> newCreditDetailViewList;
    private List<DecisionFollowConditionView> decisionFollowConditionViewList;
	private List<OpenAccountFullView> openAccountFullViewList;
	
	private BigDecimal totalApprovedCredit;
	
	private Date signingDate;
	private Date firstPaymentDate;
	private int payDate;

    private User modifyBy;
    private Date modifyDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("newCreditDetailView", getNewCreditDetailViewList())
                .toString();
    }


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public User getModifyBy() {
		return modifyBy;
	}


	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}


	public Date getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

    public List<ProposeCreditInfoDetailView> getNewCreditDetailViewList() {
        return newCreditDetailViewList;
    }

    public void setNewCreditDetailViewList(List<ProposeCreditInfoDetailView> newCreditDetailViewList) {
        this.newCreditDetailViewList = newCreditDetailViewList;
    }

    public List<OpenAccountFullView> getOpenAccountFullViewList() {
        return openAccountFullViewList;
    }

    public void setOpenAccountFullViewList(List<OpenAccountFullView> openAccountFullViewList) {
        this.openAccountFullViewList = openAccountFullViewList;
    }

    public BigDecimal getTotalApprovedCredit() {
        return totalApprovedCredit;
    }

    public void setTotalApprovedCredit(BigDecimal totalApprovedCredit) {
        this.totalApprovedCredit = totalApprovedCredit;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }

    public Date getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(Date firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public int getPayDate() {
        return payDate;
    }

    public void setPayDate(int payDate) {
        this.payDate = payDate;
    }

    public List<DecisionFollowConditionView> getDecisionFollowConditionViewList() {
        return decisionFollowConditionViewList;
    }

    public void setDecisionFollowConditionViewList(List<DecisionFollowConditionView> decisionFollowConditionViewList) {
        this.decisionFollowConditionViewList = decisionFollowConditionViewList;
    }
}
