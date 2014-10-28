package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CheckMandateDocView implements Serializable{
    private long id;
    private Map<String, MandateDocView> mandatoryDocumentsMap;
    private Map<String, MandateDocView> optionalDocumentsMap;
    private Map<String, MandateDocView> otherDocumentsMap;

    private String messageHeader;
    private String message;
    private boolean passMandateList;
    private boolean passECMList;
    private boolean allowCheckComplete;
    private boolean allowUpdateRemark;
    private boolean allowUpdateReason;
    private long stepId;
    private int roleId;
    private long workCaseId;
    private long workCasePrescreenId;
    private int forStageId;

    public CheckMandateDocView() {
        init();
    }

    private void init(){
        mandatoryDocumentsMap = new ConcurrentHashMap<String, MandateDocView>();
        optionalDocumentsMap = new ConcurrentHashMap<String, MandateDocView>();
        otherDocumentsMap = new ConcurrentHashMap<String, MandateDocView>();
        messageHeader = null;
        message = null;
        passMandateList = false;
        passECMList = false;
        allowCheckComplete = false;
        allowUpdateRemark = false;
        allowUpdateReason = false;
        stepId = 0;
        roleId = 0;
        workCaseId = 0;
        workCasePrescreenId = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, MandateDocView> getMandatoryDocumentsMap() {
        return mandatoryDocumentsMap;
    }

    public List<MandateDocView> getMandateDocumentsList(){
        return new ArrayList<MandateDocView>(mandatoryDocumentsMap.values());
    }

    public void setMandatoryDocumentsMap(Map<String, MandateDocView> mandatoryDocumentsMap) {
        this.mandatoryDocumentsMap = mandatoryDocumentsMap;
    }

    public Map<String, MandateDocView> getOptionalDocumentsMap() {
        return optionalDocumentsMap;
    }

    public List<MandateDocView> getOptionalDocumentsList(){
        return new ArrayList<MandateDocView>(optionalDocumentsMap.values());
    }

    public void setOptionalDocumentsMap(Map<String, MandateDocView> optionalDocumentsMap) {
        this.optionalDocumentsMap = optionalDocumentsMap;
    }

    public void setOtherDocumentsMap(Map<String, MandateDocView> otherDocumentsMap) {
        this.otherDocumentsMap = otherDocumentsMap;
    }

    public List<MandateDocView> getOtherDocumentsList(){
        return new ArrayList<MandateDocView>(otherDocumentsMap.values());
    }

    public Map<String, MandateDocView> getOtherDocumentsMap() {
        return otherDocumentsMap;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public boolean isPassMandateList() {
        return passMandateList;
    }

    public void setPassMandateList(boolean passMandateList) {
        this.passMandateList = passMandateList;
    }

    public boolean isPassECMList() {
        return passECMList;
    }

    public void setPassECMList(boolean passECMList) {
        this.passECMList = passECMList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAllowCheckComplete() {
        return allowCheckComplete;
    }

    public void setAllowCheckComplete(boolean allowCheckComplete) {
        this.allowCheckComplete = allowCheckComplete;
    }

    public boolean isAllowUpdateRemark() {
        return allowUpdateRemark;
    }

    public void setAllowUpdateRemark(boolean allowUpdateRemark) {
        this.allowUpdateRemark = allowUpdateRemark;
    }

    public boolean isAllowUpdateReason() {
        return allowUpdateReason;
    }

    public void setAllowUpdateReason(boolean allowUpdateReason) {
        this.allowUpdateReason = allowUpdateReason;
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

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getWorkCasePrescreenId() {
        return workCasePrescreenId;
    }

    public void setWorkCasePrescreenId(long workCasePrescreenId) {
        this.workCasePrescreenId = workCasePrescreenId;
    }

    public int getForStageId() {
        return forStageId;
    }

    public void setForStageId(int forStageId) {
        this.forStageId = forStageId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandatoryDocumentsMap", mandatoryDocumentsMap)
                .append("optionalDocumentsMap", optionalDocumentsMap)
                .append("otherDocumentsMap", otherDocumentsMap)
                .append("messageHeader", messageHeader)
                .append("message", message)
                .append("passMandateList", passMandateList)
                .append("passECMList", passECMList)
                .append("allowCheckComplete", allowCheckComplete)
                .append("allowUpdateRemark", allowUpdateRemark)
                .append("allowUpdateReason", allowUpdateReason)
                .append("stepId", stepId)
                .append("roleId", roleId)
                .append("workCaseId", workCaseId)
                .append("workCasePrescreenId", workCasePrescreenId)
                .toString();
    }
}
