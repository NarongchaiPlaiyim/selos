package com.clevel.selos.integration.bpm.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class BPMInbox implements Serializable {
    private int receivedTime; //RECEIVEDTIME
    private String appNumber; //APPNUMBER
    private String borrowerName; //BORROWERNAME
    private String productGroup; //PRODUCTGROUP
    private int requestType; //REQUESTTYPE
    private String fStepName; //F_STEPNAME
    private String status; //STATUS
    private String previousUser; //PREVIOUSUSER
    private int appointmentDate; //APPOINTMENTDATE
    private String doaLevel; //APPOINTMENTDATE
    private String previousAction; //PREVIOUSACTION
    private int slaEndTime; //SLAENDTIME
    private int slaPeriod; //SLAPERIOD
    private int totalTimeAtUser; //TOTALTIMEATUSER
    private int totalTimeAtProcess; //TOTALTIMEATPROCESS

    private String currentUser; //CURRENTUSER
    private String returnFlag; //RETURNFLAG
    private String teamName; //TEAMNAME

    public int getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(int receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getfStepName() {
        return fStepName;
    }

    public void setfStepName(String fStepName) {
        this.fStepName = fStepName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousUser() {
        return previousUser;
    }

    public void setPreviousUser(String previousUser) {
        this.previousUser = previousUser;
    }

    public int getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(int appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDoaLevel() {
        return doaLevel;
    }

    public void setDoaLevel(String doaLevel) {
        this.doaLevel = doaLevel;
    }

    public String getPreviousAction() {
        return previousAction;
    }

    public void setPreviousAction(String previousAction) {
        this.previousAction = previousAction;
    }

    public int getSlaEndTime() {
        return slaEndTime;
    }

    public void setSlaEndTime(int slaEndTime) {
        this.slaEndTime = slaEndTime;
    }

    public int getSlaPeriod() {
        return slaPeriod;
    }

    public void setSlaPeriod(int slaPeriod) {
        this.slaPeriod = slaPeriod;
    }

    public int getTotalTimeAtUser() {
        return totalTimeAtUser;
    }

    public void setTotalTimeAtUser(int totalTimeAtUser) {
        this.totalTimeAtUser = totalTimeAtUser;
    }

    public int getTotalTimeAtProcess() {
        return totalTimeAtProcess;
    }

    public void setTotalTimeAtProcess(int totalTimeAtProcess) {
        this.totalTimeAtProcess = totalTimeAtProcess;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("receivedTime", receivedTime)
                .append("appNumber", appNumber)
                .append("borrowerName", borrowerName)
                .append("productGroup", productGroup)
                .append("requestType", requestType)
                .append("fStepName", fStepName)
                .append("status", status)
                .append("previousUser", previousUser)
                .append("appointmentDate", appointmentDate)
                .append("doaLevel", doaLevel)
                .append("previousAction", previousAction)
                .append("slaEndTime", slaEndTime)
                .append("slaPeriod", slaPeriod)
                .append("totalTimeAtUser", totalTimeAtUser)
                .append("totalTimeAtProcess", totalTimeAtProcess)
                .append("currentUser", currentUser)
                .append("returnFlag", returnFlag)
                .append("teamName", teamName)
                .toString();
    }
}
