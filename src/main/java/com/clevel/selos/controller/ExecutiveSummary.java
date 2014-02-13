package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.dao.master.AuthorizationDOADAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.ExSumReasonView;
import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "executiveSummary")
public class ExecutiveSummary extends MandatoryFieldsControl {

    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    private Long workCaseId;

    private String messageHeader;
    private String message;
    private String severity;

    private boolean isRoleUW;

    private ExSummaryView exSummaryView;

    private ExSumReasonView selectDeviate;

    @Inject
    private UserDAO userDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ReasonDAO reasonDAO;
    @Inject
    private AuthorizationDOADAO authorizationDOADAO;

    @Inject
    private ExSummaryControl exSummaryControl;

    //*** Drop down List ***//
    private List<AuthorizationDOA> authorizationDOAList;
    private List<Reason> reasonList;

    //
    private ExSumReasonView reason;

    public ExecutiveSummary() {
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.debug("onCreation ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }

        reasonList = new ArrayList<Reason>();
        authorizationDOAList = authorizationDOADAO.findAll();

        reason = new ExSumReasonView();

        exSummaryView = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);

        if(exSummaryView == null){
            exSummaryView = new ExSummaryView();
        }

        exSummaryView.setUwCode("6500000000");

        //for reasonList
        if(exSummaryView.getDecision() == 2){ //1 approve , 2 deviate , 3 reject
            reasonList = reasonDAO.getDeviateList();
        } else if(exSummaryView.getDecision() == 3){
            reasonList = reasonDAO.getRejectList();
        } else {
            reasonList = new ArrayList<Reason>();
        }

        User user = getCurrentUser();
        if(user != null && user.getRole() != null && user.getRole().getId() == RoleValue.UW.id()){
            isRoleUW = true;
        } else {
            isRoleUW = false;
        }
    }

    public void onSaveExecutiveSummary() {
        log.debug("onSaveExecutiveSummary :::");
        if(exSummaryView.getDecision() == 2 || exSummaryView.getDecision() == 3){ //1 approve , 2 deviate , 3 reject
            if(exSummaryView.getDeviateCode() == null || exSummaryView.getDeviateCode().size() < 1){
                messageHeader = msg.get("app.header.information");
                message = "Save Ex Summary Failed. "+
                        "<br/><br/> Cause : Reason is null.";
                severity = "info";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
        }

        try {
            exSummaryControl.saveExSummary(exSummaryView, workCaseId);

            messageHeader = msg.get("app.header.information");
            message = "Save Ex Summary data success.";
            severity = "info";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.error");
            if (ex.getCause() != null) {
                message = "Save Ex Summary data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Ex Summary data failed. Cause : " + ex.getMessage();
            }
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeDeviate(){
        exSummaryView.setDeviateCode(new ArrayList<ExSumReasonView>());
        if(exSummaryView.getDecision() == 2){ //1 approve , 2 deviate , 3 reject
            reasonList = reasonDAO.getDeviateList();
        } else if(exSummaryView.getDecision() == 3){
            reasonList = reasonDAO.getRejectList();
        } else {
            reasonList = new ArrayList<Reason>();
        }
    }

    public void onAddReason() {
        if(reason.getId() != 0){
            Reason findReason = reasonDAO.findById(reason.getId());
            if(findReason != null){
                ExSumReasonView exSumReasonView = new ExSumReasonView();
                exSumReasonView.setCode(findReason.getCode());
                exSumReasonView.setDescription(findReason.getDescription());
                exSummaryView.getDeviateCode().add(exSumReasonView);
            }
        }
    }

    public void onDeleteDeviate(){
        exSummaryView.getDeviateCode().remove(selectDeviate);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public ExSummaryView getExSummaryView() {
        return exSummaryView;
    }

    public void setExSummaryView(ExSummaryView exSummaryView) {
        this.exSummaryView = exSummaryView;
    }

    public List<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public List<AuthorizationDOA> getAuthorizationDOAList() {
        return authorizationDOAList;
    }

    public void setAuthorizationDOAList(List<AuthorizationDOA> authorizationDOAList) {
        this.authorizationDOAList = authorizationDOAList;
    }

    public ExSumReasonView getReason() {
        return reason;
    }

    public void setReason(ExSumReasonView reason) {
        this.reason = reason;
    }

    public ExSumReasonView getSelectDeviate() {
        return selectDeviate;
    }

    public void setSelectDeviate(ExSumReasonView selectDeviate) {
        this.selectDeviate = selectDeviate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public boolean isRoleUW() {
        return isRoleUW;
    }

    public void setRoleUW(boolean roleUW) {
        isRoleUW = roleUW;
    }
}

