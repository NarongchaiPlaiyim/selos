package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class PEInbox implements Serializable
{

    private String receiveddate;
    private String atuserteam;
    private String applicationno;
    private String name;
    private String productgroup;
    private String requestTypeStr;
    private String step;
    private String status;
    private String fromuser;
    private String atuser;
    private String appointmentdate;
    private String doalevel;
    private String action;
    private String slastatus;
    private String slaenddate;
    private int totaltimespentatuser;
    private int totaltimespentatprocess;
    private String statuscode;
    private String fwobnumber;
    private long workCasePreScreenId;
    private long workCaseId;
    private Long stepId;
    private String queuename;
    private int fetchType;
    private Integer locked;

    public Integer getLocked()
    {
        return locked;
    }

    public void setLocked(Integer locked)
    {
        this.locked = locked;
    }

    public int getFetchType() {
        return fetchType;
    }

    public void setFetchType(int fetchType) {
        this.fetchType = fetchType;
    }

    public String getFwobnumber() {
        return fwobnumber;
    }

    public void setFwobnumber(String fwobnumber) {
        this.fwobnumber = fwobnumber;
    }

    public String getRequestTypeStr() {
        return requestTypeStr;
    }

    public void setRequestTypeStr(String requestTypeStr) {
        this.requestTypeStr = requestTypeStr;
    }



    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }



    public String getSlastatus() {
        return slastatus;
    }

    public void setSlastatus(String slastatus) {
        this.slastatus = slastatus;
    }

    public String getQueuename() {
        return queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }



    public long getWorkCasePreScreenId() {

        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }



    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }



    public String getReceiveddate() {
        return receiveddate;
    }

    public void setReceiveddate(String receiveddate) {
        this.receiveddate = receiveddate;
    }

    public String getAtuserteam() {
        return atuserteam;
    }

    public void setAtuserteam(String atuserteam) {
        this.atuserteam = atuserteam;
    }

    public String getApplicationno() {
        return applicationno;
    }

    public void setApplicationno(String applicationno) {
        this.applicationno = applicationno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getProductgroup() {
        return productgroup;
    }

    public void setProductgroup(String productgroup) {
        this.productgroup = productgroup;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromuser() {
        return fromuser;
    }

    public void setFromuser(String fromuser) {
        this.fromuser = fromuser;
    }

    public String getAtuser() {
        return atuser;
    }

    public void setAtuser(String atuser) {
        this.atuser = atuser;
    }

    public String getAppointmentdate() {
        return appointmentdate;
    }

    public void setAppointmentdate(String appointmentdate) {
        this.appointmentdate = appointmentdate;
    }

    public String getDoalevel() {
        return doalevel;
    }

    public void setDoalevel(String doalevel) {
        this.doalevel = doalevel;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSlaenddate() {
        return slaenddate;
    }

    public void setSlaenddate(String slaenddate) {
        this.slaenddate = slaenddate;
    }

    public int getTotaltimespentatuser() {
        return totaltimespentatuser;
    }

    public void setTotaltimespentatuser(int totaltimespentatuser) {
        this.totaltimespentatuser = totaltimespentatuser;
    }

    public int getTotaltimespentatprocess() {
        return totaltimespentatprocess;
    }

    public void setTotaltimespentatprocess(int totaltimespentatprocess) {
        this.totaltimespentatprocess = totaltimespentatprocess;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("receiveddate", receiveddate)
                .append("atuserteam", atuserteam)
                .append("applicationno", applicationno)
                .append("name", name)
                .append("requestTypeStr", requestTypeStr)
                .append("productgroup", productgroup)
                .append("step", step)
                .append("status", status)
                .append("fromuser", fromuser)
                .append("atuser", atuser)
                .append("appointmentdate", appointmentdate)
                .append("doalevel", doalevel)
                .append("action", action)
                .append("slastatus",slastatus)
                .append("slaenddate", slaenddate)
                .append("totaltimespentatuser", totaltimespentatuser)
                .append("totaltimespentatprocess", totaltimespentatprocess)
                .append("statuscode",statuscode)
                .append("fwobnumber",fwobnumber)
                .append("workCasePreScreenId", workCasePreScreenId)
                .append("workCaseId", workCaseId)
                .append("stepId", stepId)
                .append("queuename",queuename)
                .append("fetchType",fetchType)
                .toString();
    }


}
