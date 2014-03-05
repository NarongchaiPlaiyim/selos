package com.clevel.selos.model.view;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 2/21/14
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
import com.clevel.selos.integration.SELOS;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;

public class PERoster implements Serializable {

    @Inject
    @SELOS
    Logger log;

    private String ReceivedDate;
    private String TeamName;
    private String AppNumber;
    private String Name;
    private String ProductGroup;
    private String RequestType;
    private String Status;
    private String SLAEndTime;
    private String TotalTimeAtUser;
    private String TotalTimeAtProcess;
    private String F_WobNum;
    private String CurrentUser;
    private long workCasePreScreenId;
    private long workCaseId;
    private long stepId;
    private String queuename;
    private String step;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getReceivedDate() {
        return ReceivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        ReceivedDate = receivedDate;
    }

    public String getF_WobNum() {
        return F_WobNum;
    }

    public void setF_WobNum(String f_WobNum) {
        F_WobNum = f_WobNum;
    }

    public String getCurrentUser() {
        return CurrentUser;
    }

    public void setCurrentUser(String currentUser) {
        CurrentUser = currentUser;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSLAEndTime() {
        return SLAEndTime;
    }

    public void setSLAEndTime(String SLAEndTime) {
        this.SLAEndTime = SLAEndTime;
    }

    public String getTotalTimeAtProcess() {
        return TotalTimeAtProcess;
    }

    public void setTotalTimeAtProcess(String totalTimeAtProcess) {
        TotalTimeAtProcess = totalTimeAtProcess;
    }


    public String getTotalTimeAtUser() {
        return TotalTimeAtUser;
    }

    public void setTotalTimeAtUser(String totalTimeAtUser) {
        TotalTimeAtUser = totalTimeAtUser;
    }


    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getProductGroup() {
        return ProductGroup;
    }

    public void setProductGroup(String productGroup) {
        ProductGroup = productGroup;
    }

    public String getAppNumber() {
        return AppNumber;
    }

    public void setAppNumber(String appNumber) {
        AppNumber = appNumber;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getWorkCasePreScreenId() {
        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public String getQueuename() {
        return queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ReceivedDate", ReceivedDate)
                .append("TeamName", TeamName)
                .append("AppNumber", AppNumber)
                .append("Name", Name)
                .append("ProductGroup", ProductGroup)
                .append("RequestType", RequestType)
                .append("Status", Status)
                .append("CurrentUser",CurrentUser)
                .append("SLAEndTime", SLAEndTime)
                .append("workCasePreScreenId", workCasePreScreenId)
                .append("workCaseId", workCaseId)
                .append("stepId", stepId)
                .append("queuename", queuename)
                .append("TotalTimeAtUser",TotalTimeAtUser)
                .append("TotalTimeAtProcess", TotalTimeAtProcess)
                .append("F_WobNum",F_WobNum)
                .toString();
    }


}
