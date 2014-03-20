package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ReturnControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "returnBDM")
public class ReturnInfoBDM implements Serializable {
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


    @Inject
    ReturnControl returnControl;

    private long workCaseId;

    private ReturnInfoView returnInfoHeaderView;
    private List<ReturnInfoView> returnInfoViewList;
    private List<ReturnInfoView> returnInfoHistoryViewList;

    private boolean isViewHistory;
    private boolean isViewAll;

    private static final int MAX_RESULT_HISTORY = 20;

    User modifyBy;
    Date modifyDate;

    private String message;
    private String messageHeader;

    public ReturnInfoBDM() {

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
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
    public void onCreation() {
        log.info("ReturnInfoBDM ::: onCreation");
        HttpSession session = FacesUtil.getSession(true);

        if (session.getAttribute("workCaseId") != null) {
            log.info("onCreation ::: getAttrubute workCaseId : {}", session.getAttribute("workCaseId"));
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            initialResultView();
        }
    }

    public void initialResultView(){
        returnInfoHeaderView = new ReturnInfoView();
        returnInfoViewList = new ArrayList<ReturnInfoView>();
        returnInfoHistoryViewList = new ArrayList<ReturnInfoView>();

        returnInfoViewList = returnControl.getReturnInfoViewList(workCaseId);
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            returnInfoHeaderView = returnInfoViewList.get(0);
        }

        isViewHistory = false;
        isViewAll = false;
    }


    public void onSave() {
        log.info("Start onSave {}", returnInfoViewList);
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            User user = (User) session.getAttribute("user");

            returnControl.saveReturnInformation(workCaseId, queueName, user, returnInfoViewList);
            messageHeader = "Information.";
            message = "Save Return Information Success.";
            log.debug("onReturnBDMSubmit ::: success.");
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Save Return Information Failed, cause : " + Util.getMessageException(ex);
            log.error("onReturnBDMSubmit ::: exception occurred : ", ex);
        }

        onCreation();
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onViewHistory(){
        log.debug("onViewHistory.");
        isViewHistory = true;
        isViewAll = false;
        returnInfoHistoryViewList = new ArrayList<ReturnInfoView>();
        returnInfoHistoryViewList = returnControl.getReturnInfoHistoryViewList(workCaseId, MAX_RESULT_HISTORY);

        log.debug("onViewHistory. returnInfoHistoryViewList size: {}",returnInfoHistoryViewList.size());
    }

    public void onViewHistoryAll(){
        log.debug("onViewHistoryAll.");
        isViewHistory = true;
        isViewAll = true;
        returnInfoHistoryViewList = new ArrayList<ReturnInfoView>();
        returnInfoHistoryViewList = returnControl.getReturnInfoHistoryViewList(workCaseId,0);

        log.debug("onViewHistoryAll. returnInfoHistoryViewList size: {}",returnInfoHistoryViewList.size());
    }

    public ReturnInfoView getReturnInfoHeaderView() {
        return returnInfoHeaderView;
    }

    public void setReturnInfoHeaderView(ReturnInfoView returnInfoHeaderView) {
        this.returnInfoHeaderView = returnInfoHeaderView;
    }

    public List<ReturnInfoView> getReturnInfoViewList() {
        return returnInfoViewList;
    }

    public void setReturnInfoViewList(List<ReturnInfoView> returnInfoViewList) {
        this.returnInfoViewList = returnInfoViewList;
    }

    public List<ReturnInfoView> getReturnInfoHistoryViewList() {
        return returnInfoHistoryViewList;
    }

    public void setReturnInfoHistoryViewList(List<ReturnInfoView> returnInfoHistoryViewList) {
        this.returnInfoHistoryViewList = returnInfoHistoryViewList;
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

    public boolean isViewHistory() {
        return isViewHistory;
    }

    public void setViewHistory(boolean viewHistory) {
        isViewHistory = viewHistory;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public boolean isViewAll() {
        return isViewAll;
    }

    public void setViewAll(boolean viewAll) {
        isViewAll = viewAll;
    }
}
