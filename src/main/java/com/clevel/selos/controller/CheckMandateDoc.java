package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CheckMandateDocControl;
import com.clevel.selos.businesscontrol.master.ReasonControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DocLevel;
import com.clevel.selos.model.ReasonTypeValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.master.ReasonView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    @NormalMessage
    private Message msg;

    @Inject
    private CheckMandateDocControl checkMandateDocControl;
    @Inject
    private ReasonControl reasonControl;

    private CheckMandateDocView checkMandateDocView;
    private String messageHeader;
    private String message;

    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;

    private List<SelectItem> reasonList;

    private TestInfo testInfo;

    @Inject
    public CheckMandateDoc() {
    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
            workCasePrescreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), -1);
            stepId = Util.parseLong(session.getAttribute("stepId"), -1);
        }
        reasonList = reasonControl.getReasonSelectItem(ReasonTypeValue.MANDATE_DOC.value());
        //if(FacesUtil.getRequest().getRequestURL().toString().contains("testManDateDoc")){
            testInfo = new TestInfo();
        //}
    }

    public void onCheckMandateDoc(){
        if(workCasePrescreenId > 0)
            checkMandateDocView = checkMandateDocControl.checkMandateDoc(stepId, workCasePrescreenId);
        else
            checkMandateDocView = checkMandateDocControl.checkMandateDoc(stepId, workCaseId);
    }

    public void onSaveCheckMandateDoc(){
        log.debug("-- begin onSaveCheckMandateDoc: {} --", checkMandateDocView);
        if(checkMandateDocView != null){
            try{
                if(workCasePrescreenId > 0)
                    checkMandateDocControl.saveMandateDoc(stepId, workCasePrescreenId, checkMandateDocView);
                else
                    checkMandateDocControl.saveMandateDoc(stepId, workCaseId, checkMandateDocView);
            }catch (Exception ex){

            }
        }

        messageHeader = "Information";
        message = "Success";
        RequestContext.getCurrentInstance().execute("msgBoxMandateDocMessagePanel.show()");

        log.debug("-- end onSaveCheckMandateDoc --");
    }

    public CheckMandateDocView getCheckMandateDocView() {
        return checkMandateDocView;
    }

    public void setCheckMandateDocView(CheckMandateDocView checkMandateDocView) {
        this.checkMandateDocView = checkMandateDocView;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SelectItem> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<SelectItem> reasonList) {
        this.reasonList = reasonList;
    }

    public boolean isApplicationLevel(DocLevel docLevel){
        if(docLevel.value() == DocLevel.CUS_LEVEL.value())
            return false;
        else
            return true;
    }

    public void onTestMandateDocControl(){
        log.debug("-- onTestMandateDocControl --");
        workCasePrescreenId = testInfo.testWorkCasePrescreenId;
        workCaseId = testInfo.testWorkCaseId;
        stepId = testInfo.stepId;
        log.debug("testInfo: {}", testInfo);
        onCheckMandateDoc();
    }

    public TestInfo getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    public class TestInfo{
        private long testWorkCaseId = 0L;
        private long testWorkCasePrescreenId = 0L;
        private long stepId = 0L;

        //CA NUMBER: 01569921447112010006 --> APPNUMBER: 0420140211000101

        public TestInfo(){}

        public long getTestWorkCaseId() {
            return testWorkCaseId;
        }

        public void setTestWorkCaseId(long testWorkCaseId) {
            this.testWorkCaseId = testWorkCaseId;
        }

        public long getTestWorkCasePrescreenId() {
            return testWorkCasePrescreenId;
        }

        public void setTestWorkCasePrescreenId(long testWorkCasePrescreenId) {
            this.testWorkCasePrescreenId = testWorkCasePrescreenId;
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
                    .append("testWorkCaseId", testWorkCaseId)
                    .append("testWorkCasePrescreenId", testWorkCasePrescreenId)
                    .append("stepId", stepId)
                    .toString();
        }
    }
}
