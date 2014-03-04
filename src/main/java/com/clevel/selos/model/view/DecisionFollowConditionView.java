package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class DecisionFollowConditionView implements Serializable {
    private long id;
    private FollowConditionView conditionView;
    private String detail;
    private Date followDate;

    public DecisionFollowConditionView() {
        reset();
    }

    public void reset() {
        this.conditionView = new FollowConditionView();
        this.detail = "";
        this.followDate = DateTime.now().toDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FollowConditionView getConditionView() {
        return conditionView;
    }

    public void setConditionView(FollowConditionView conditionView) {
        this.conditionView = conditionView;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("conditionView", conditionView)
                .append("detail", detail)
                .append("followDate", followDate)
                .toString();
    }
}
