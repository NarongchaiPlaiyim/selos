package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.InboxDevControl;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "inboxdev")
public class InboxDev implements Serializable {
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
    BPMInterface bpmInterface;

    @Inject
    InboxDevControl inboxDevControl;
    @Inject
    HeaderControl headerControl;

    @Inject
    StepDAO stepDAO;

    private UserDetail userDetail;
    private List<InboxView> inboxViewList;
    private List<InboxView> inboxPoolViewList;
    private boolean renderedPool;

    private InboxView inboxViewSelectItem;

    public InboxDev() {

    }

    @PostConstruct
    public void onCreation() {
        // *** Get user from Session *** //
        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("onCreation ::: userDetail : {}", userDetail);
        try {
            inboxViewList = inboxDevControl.getInboxFromBPM(userDetail);
            inboxPoolViewList = new ArrayList<InboxView>();
            if(userDetail.getRole().equals("ROLE_UW") || userDetail.getRole().equals("ROLE_AAD")){
                inboxPoolViewList = inboxDevControl.getInboxPoolFromBPM(userDetail);
                renderedPool = true;
            } else {
                renderedPool = false;
            }
            HttpSession httpSession = FacesUtil.getSession(true);
            httpSession.setAttribute("workCaseId", null);
            httpSession.setAttribute("workCasePreScreenId", null);
            httpSession.setAttribute("stepId", null);
            log.debug("onCreation ::: inboxViewList : {}", inboxViewList);
        } catch (Exception e) {
            log.error("Exception while getInbox : ", e);
        }
    }

    public void onSelectInbox() {

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetail == null){
            FacesUtil.redirect("/login.jsf");
            return;
        }

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", inboxViewSelectItem);
        if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCasePreScreenId()))){
            session.setAttribute("workCasePreScreenId", inboxViewSelectItem.getWorkCasePreScreenId());
        } else {
            session.setAttribute("workCasePreScreenId", 0);
        }
        if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCaseId()))){
            session.setAttribute("workCaseId", inboxViewSelectItem.getWorkCaseId());
        } else {
            session.setAttribute("workCaseId", 0);
        }

        //TODO Change for Appraisal WorkCase

        session.setAttribute("stepId", inboxViewSelectItem.getStepId());
        session.setAttribute("queueName", inboxViewSelectItem.getQueueName());
        session.setAttribute("requestAppraisal", inboxViewSelectItem.getRequestAppraisal());
        session.setAttribute("statusId", inboxViewSelectItem.getStatusCode());

        if(Long.toString(inboxViewSelectItem.getStepId()) != null && inboxViewSelectItem.getStepId() != 0){
            Step step = stepDAO.findById(inboxViewSelectItem.getStepId());
            session.setAttribute("stageId", step != null ? step.getStage().getId() : 0);
        }

        //*** Get Information for Header ***//
        AppHeaderView appHeaderView = headerControl.getHeaderInformation(inboxViewSelectItem.getStepId(), inboxViewSelectItem.getCaNo());
        session.setAttribute("appHeaderInfo", appHeaderView);

        long selectedStepId = inboxViewSelectItem.getStepId();
        String landingPage = inboxDevControl.getLandingPage(selectedStepId);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {
            //TODO Show dialog
        }

        /*if (inboxViewSelectItem.getStepId() == 1001) {
            FacesUtil.redirect("/site/prescreenInitial.jsf");
        } else if (inboxViewSelectItem.getStepId() == 1002) {
            FacesUtil.redirect("/site/prescreenChecker.jsf");
        } else if (inboxViewSelectItem.getStepId() == 1003) {
            FacesUtil.redirect("/site/prescreenMaker.jsf");
        }
        return;*/
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<InboxView> getInboxViewList() {
        return inboxViewList;
    }

    public void setInboxViewList(List<InboxView> inboxViewList) {
        this.inboxViewList = inboxViewList;
    }

    public List<InboxView> getInboxPoolViewList() {
        return inboxPoolViewList;
    }

    public void setInboxPoolViewList(List<InboxView> inboxPoolViewList) {
        this.inboxPoolViewList = inboxPoolViewList;
    }

    public boolean getRenderedPool() {
        return renderedPool;
    }

    public void setRenderedPool(boolean renderedPool) {
        this.renderedPool = renderedPool;
    }

    public InboxView getInboxViewSelectItem() {
        return inboxViewSelectItem;
    }

    public void setInboxViewSelectItem(InboxView inboxViewSelectItem) {
        this.inboxViewSelectItem = inboxViewSelectItem;
    }
}
