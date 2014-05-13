package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class UserAccessView implements Serializable {
    private int screenId;
    private int stageId;
    private long stepId;
    private int roleId;
    private boolean accessFlag;

    public UserAccessView(){

    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(boolean accessFlag) {
        this.accessFlag = accessFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("screenId", screenId)
                .append("stageId", stageId)
                .append("stepId", stepId)
                .append("roleId", roleId)
                .append("accessFlag", accessFlag)
                .toString();
    }
}
