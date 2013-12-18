package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PledgeDetailView implements Serializable {
    private int approveType;
    private Date pledgeSignDate;
    private String pledgeType;
    private BigDecimal pledgeAmount;
    private List<PledgeACView> pledgeACViews;
    private Date modifyDate;
    private String modifyBy;

    public PledgeDetailView(){
     reset();
    }

    public void reset(){
        this.approveType = 1;
        this.pledgeSignDate = new Date();
        this.pledgeType = "";
        this.pledgeAmount = BigDecimal.ZERO;
        this.pledgeACViews = new ArrayList<PledgeACView>();


    }

    public int getApproveType() {
        return approveType;
    }

    public void setApproveType(int approveType) {
        this.approveType = approveType;
    }

    public Date getPledgeSignDate() {
        return pledgeSignDate;
    }

    public void setPledgeSignDate(Date pledgeSignDate) {
        this.pledgeSignDate = pledgeSignDate;
    }

    public String getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }

    public BigDecimal getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(BigDecimal pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public List<PledgeACView> getPledgeACViews() {
        return pledgeACViews;
    }

    public void setPledgeACViews(List<PledgeACView> pledgeACViews) {
        this.pledgeACViews = pledgeACViews;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("approveType", approveType)
                .append("pledgeSignDate", pledgeSignDate)
                .append("pledgeType", pledgeType)
                .append("pledgeAmount", pledgeAmount)
                .append("pledgeACViews", pledgeACViews)
                .toString();
    }
}
