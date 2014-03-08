package com.clevel.selos.integration.brms.model.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class DocumentDetail implements Serializable{

    private String documentGroup;
    private String id;
    private String type;
    private String description;
    private String condition;
    private boolean mandateFlag;
    private String step;
    private String showFlag;
    private String operStep;
    private String operShowFlag;

    public String getDocumentGroup() {
        return documentGroup;
    }

    public void setDocumentGroup(String documentGroup) {
        this.documentGroup = documentGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isMandateFlag() {
        return mandateFlag;
    }

    public void setMandateFlag(boolean mandateFlag) {
        this.mandateFlag = mandateFlag;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getOperShowFlag() {
        return operShowFlag;
    }

    public void setOperShowFlag(String operShowFlag) {
        this.operShowFlag = operShowFlag;
    }

    public String getOperStep() {
        return operStep;
    }

    public void setOperStep(String operStep) {
        this.operStep = operStep;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("documentGroup", documentGroup)
                .append("id", id)
                .append("description", description)
                .append("condition", condition)
                .append("mandateFlag", mandateFlag)
                .append("step", step)
                .append("showFlag", showFlag)
                .append("operStep", operStep)
                .append("operShowFlag", operShowFlag)
                .toString();
    }
}
