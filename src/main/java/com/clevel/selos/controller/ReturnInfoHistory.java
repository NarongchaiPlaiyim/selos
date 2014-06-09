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
@ManagedBean(name = "returnHistory")
public class ReturnInfoHistory implements Serializable {
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

    private List<ReturnInfoView> returnInfoHistoryViewList;

    private static final int MAX_RESULT_HISTORY = 20;

    User modifyBy;
    Date modifyDate;

    public ReturnInfoHistory() {

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
        modifyBy = new User();

        if (session.getAttribute("workCaseId") != null) {
            log.info("onCreation ::: getAttrubute workCaseId : {}", session.getAttribute("workCaseId"));
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

            initialResultView();

            if(returnInfoHistoryViewList!=null && returnInfoHistoryViewList.size()>0){
                modifyBy = returnInfoHistoryViewList.get(0).getModifyBy();
                modifyDate = returnInfoHistoryViewList.get(0).getModifyDate();
            }
        }
    }

    public void initialResultView(){
        returnInfoHistoryViewList = new ArrayList<ReturnInfoView>();
        returnInfoHistoryViewList = returnControl.getReturnInfoHistoryViewList(workCaseId, MAX_RESULT_HISTORY);
    }

    public void onViewHistoryAll(){
        log.debug("onViewHistoryAll.");
        returnInfoHistoryViewList = new ArrayList<ReturnInfoView>();
        returnInfoHistoryViewList = returnControl.getReturnInfoHistoryViewList(workCaseId,0);

        log.debug("onViewHistoryAll. returnInfoHistoryViewList size: {}",returnInfoHistoryViewList.size());
    }

    public List<ReturnInfoView> getReturnInfoHistoryViewList() {
        return returnInfoHistoryViewList;
    }

    public void setReturnInfoHistoryViewList(List<ReturnInfoView> returnInfoHistoryViewList) {
        this.returnInfoHistoryViewList = returnInfoHistoryViewList;
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
}
