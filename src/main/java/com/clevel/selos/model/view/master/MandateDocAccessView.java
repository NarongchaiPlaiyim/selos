package com.clevel.selos.model.view.master;

import com.clevel.selos.model.*;
import com.clevel.selos.model.AccessType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;

public class MandateDocAccessView implements Serializable {

    private long id;
    private long stepId;
    private int roleId;
    private AccessType accessType;
    private boolean checkBRMS;
    private boolean checkECM;
    private int active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public boolean isCheckBRMS() {
        return checkBRMS;
    }

    public void setCheckBRMS(boolean checkBRMS) {
        this.checkBRMS = checkBRMS;
    }

    public boolean isCheckECM() {
        return checkECM;
    }

    public void setCheckECM(boolean checkECM) {
        this.checkECM = checkECM;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("stepId", stepId)
                .append("roleId", roleId)
                .append("accessType", accessType)
                .append("active", active)
                .toString();
    }
}
