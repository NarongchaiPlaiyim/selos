package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ApprovalHistoryDecisionReport extends ReportModel{

    private int count;
    private String action;
    private String userName;
    private String roleDescription;
    private String titleName;
    private Date submitDate;
    private String comments;

    public ApprovalHistoryDecisionReport() {
        count = 0;
        action = "";
        userName = "";
        roleDescription = "";
        titleName = "";
        comments = "";
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("count", count)
                .append("action", action)
                .append("userName", userName)
                .append("roleDescription", roleDescription)
                .append("titleName", titleName)
                .append("submitDate", submitDate)
                .append("comments", comments)
                .toString();
    }
}
