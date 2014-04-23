package com.clevel.selos.model.view;

import java.io.Serializable;

public class UserSysParameterView implements Serializable{

    private long id;
    private String code;
    private String name;
    private String description;
    private String value;
    private String fieldType;
    private boolean noBDMSubmit;
    private boolean passBUApproval;
    private boolean passFinalApprovalStep;
    private boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isNoBDMSubmit() {
        return noBDMSubmit;
    }

    public void setNoBDMSubmit(boolean noBDMSubmit) {
        this.noBDMSubmit = noBDMSubmit;
    }

    public boolean isPassBUApproval() {
        return passBUApproval;
    }

    public void setPassBUApproval(boolean passBUApproval) {
        this.passBUApproval = passBUApproval;
    }

    public boolean isPassFinalApprovalStep() {
        return passFinalApprovalStep;
    }

    public void setPassFinalApprovalStep(boolean passFinalApprovalStep) {
        this.passFinalApprovalStep = passFinalApprovalStep;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
