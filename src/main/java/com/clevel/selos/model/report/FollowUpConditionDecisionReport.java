package com.clevel.selos.model.report;

import com.clevel.selos.model.view.FollowConditionView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

public class FollowUpConditionDecisionReport extends ReportModel{

    private String conditionView;
    private String detail;
    private Date followDate;

    public FollowUpConditionDecisionReport() {
        conditionView = getDefaultString();
        detail = getDefaultString();
        followDate = getDefaultDate();
    }

    public String getConditionView() {
        return conditionView;
    }

    public void setConditionView(String conditionView) {
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
                .append("conditionView", conditionView)
                .append("detail", detail)
                .append("followDate", followDate)
                .toString();
    }
}
