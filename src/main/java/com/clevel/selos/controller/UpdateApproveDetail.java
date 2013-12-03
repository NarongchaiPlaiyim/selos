package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.ProposeCreditDetailView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "updateApproveDetail")
public class UpdateApproveDetail {

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

    // message //
    private String messageHeader;
    private String message;

    //session
    private long workCaseId;

    // content //
    List<ProposeCreditDetailView> approveCreditDetailViews;

    public void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2001);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);
        log.info("preRender ::: setSession ");
        session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//            stepId = Long.parseLong(session.getAttribute("stepId").toString());
//            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
    }

    @PostConstruct
    public void onCreation(){
        approveCreditDetailViews = new ArrayList<ProposeCreditDetailView>();
        try{

        }catch (Exception e){

        }
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
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

    public List<ProposeCreditDetailView> getApproveCreditDetailViews() {
        return approveCreditDetailViews;
    }

    public void setApproveCreditDetailViews(List<ProposeCreditDetailView> approveCreditDetailViews) {
        this.approveCreditDetailViews = approveCreditDetailViews;
    }
}
