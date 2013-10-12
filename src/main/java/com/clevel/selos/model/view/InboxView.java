package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class InboxView implements Serializable {
    private String caNo;
    private String bdmId;
    private String fnWobNum;
    private String fnStepName;
    private String stepCode;
    private String lockStatus;
    private String lockedUser;
    private String queueName;
    private long workCasePreScreenId;
    private long workCaseId;
    private long stepId;

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public String getBdmId() {
        return bdmId;
    }

    public void setBdmId(String bdmId) {
        this.bdmId = bdmId;
    }

    public String getFnWobNum() {
        return fnWobNum;
    }

    public void setFnWobNum(String fnWobNum) {
        this.fnWobNum = fnWobNum;
    }

    public String getFnStepName() {
        return fnStepName;
    }

    public void setFnStepName(String fnStepName) {
        this.fnStepName = fnStepName;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getLockedUser() {
        return lockedUser;
    }

    public void setLockedUser(String lockedUser) {
        this.lockedUser = lockedUser;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public long getWorkCasePreScreenId() {
        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("caNo", caNo)
                .append("bdmId", bdmId)
                .append("fnWobNum", fnWobNum)
                .append("fnStepName", fnStepName)
                .append("stepCode", stepCode)
                .append("lockStatus", lockStatus)
                .append("lockedUser", lockedUser)
                .append("queueName", queueName)
                .append("workCasePreScreenId", workCasePreScreenId)
                .append("workCaseId", workCaseId)
                .append("stepId", stepId)
                .toString();
    }
}

