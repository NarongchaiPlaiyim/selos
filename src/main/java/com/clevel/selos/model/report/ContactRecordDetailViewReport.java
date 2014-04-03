package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ContactRecordDetailViewReport extends ReportModel {

    private String path;
    private int count;
    private Date callingDate;
    private int callingResult;
    private int acceptResult;
    private Date nextCallingDate;
    private String reasonDescription;
    private String remark;
    private String statusDescription;
    private String displayName;


    public ContactRecordDetailViewReport() {
    }

    public Date getCallingDate() {
        return callingDate;
    }

    public void setCallingDate(Date callingDate) {
        this.callingDate = callingDate;
    }

    public int getCallingResult() {
        return callingResult;
    }

    public void setCallingResult(int callingResult) {
        this.callingResult = callingResult;
    }

    public int getAcceptResult() {
        return acceptResult;
    }

    public void setAcceptResult(int acceptResult) {
        this.acceptResult = acceptResult;
    }

    public Date getNextCallingDate() {
        return nextCallingDate;
    }

    public void setNextCallingDate(Date nextCallingDate) {
        this.nextCallingDate = nextCallingDate;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("path", path)
                .append("count", count)
                .append("callingDate", callingDate)
                .append("callingResult", callingResult)
                .append("acceptResult", acceptResult)
                .append("nextCallingDate", nextCallingDate)
                .append("reasonDescription", reasonDescription)
                .append("remark", remark)
                .append("statusDescription", statusDescription)
                .append("displayName", displayName)
                .toString();
    }
}
