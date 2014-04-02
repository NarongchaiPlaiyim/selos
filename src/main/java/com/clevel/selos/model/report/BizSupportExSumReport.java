package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BizSupportExSumReport extends ReportModel{

    private String natureOfBusiness;
    private String historicalAndReasonOfChange;
    private String tmbCreditHistory;
    private String supportReason;
    private int    rm008Code;     //radio
    private String rm008Remark;
    private int    rm204Code;     //radio
    private String rm204Remark;
    private int    rm020Code;    //radio
    private String rm020Remark;

    public BizSupportExSumReport() {
        this.natureOfBusiness = "";
        this.historicalAndReasonOfChange = "";
        this.tmbCreditHistory = "";
        this.supportReason = "";
        this.rm008Remark = "";
        this.rm204Remark = "";
        this.rm020Remark = "";
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getHistoricalAndReasonOfChange() {
        return historicalAndReasonOfChange;
    }

    public void setHistoricalAndReasonOfChange(String historicalAndReasonOfChange) {
        this.historicalAndReasonOfChange = historicalAndReasonOfChange;
    }

    public String getTmbCreditHistory() {
        return tmbCreditHistory;
    }

    public void setTmbCreditHistory(String tmbCreditHistory) {
        this.tmbCreditHistory = tmbCreditHistory;
    }

    public String getSupportReason() {
        return supportReason;
    }

    public void setSupportReason(String supportReason) {
        this.supportReason = supportReason;
    }

    public int getRm008Code() {
        return rm008Code;
    }

    public void setRm008Code(int rm008Code) {
        this.rm008Code = rm008Code;
    }

    public String getRm008Remark() {
        return rm008Remark;
    }

    public void setRm008Remark(String rm008Remark) {
        this.rm008Remark = rm008Remark;
    }

    public int getRm204Code() {
        return rm204Code;
    }

    public void setRm204Code(int rm204Code) {
        this.rm204Code = rm204Code;
    }

    public String getRm204Remark() {
        return rm204Remark;
    }

    public void setRm204Remark(String rm204Remark) {
        this.rm204Remark = rm204Remark;
    }

    public int getRm020Code() {
        return rm020Code;
    }

    public void setRm020Code(int rm020Code) {
        this.rm020Code = rm020Code;
    }

    public String getRm020Remark() {
        return rm020Remark;
    }

    public void setRm020Remark(String rm020Remark) {
        this.rm020Remark = rm020Remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("natureOfBusiness", natureOfBusiness).
                append("historicalAndReasonOfChange", historicalAndReasonOfChange).
                append("tmbCreditHistory", tmbCreditHistory).
                append("supportReason", supportReason).
                append("rm008Code", rm008Code).
                append("rm008Remark", rm008Remark).
                append("rm204Code", rm204Code).
                append("rm204Remark", rm204Remark).
                append("rm020Code", rm020Code).
                append("rm020Remark", rm020Remark).
                toString();
    }
}
