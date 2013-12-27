package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class FollowUpConditionView implements Serializable {
    private long id;
    private String condition;
    private String detailOfFollowUp;
    private Date dateOfFollowUp;

    public FollowUpConditionView() {
        reset();
    }

    public void reset() {
        this.condition = "";
        this.detailOfFollowUp = "";
        this.dateOfFollowUp = DateTime.now().toDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDetailOfFollowUp() {
        return detailOfFollowUp;
    }

    public void setDetailOfFollowUp(String detailOfFollowUp) {
        this.detailOfFollowUp = detailOfFollowUp;
    }

    public Date getDateOfFollowUp() {
        return dateOfFollowUp;
    }

    public void setDateOfFollowUp(Date dateOfFollowUp) {
        this.dateOfFollowUp = dateOfFollowUp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("condition", condition)
                .append("detailOfFollowUp", detailOfFollowUp)
                .append("dateOfFollowUp", dateOfFollowUp)
                .toString();
    }
}
