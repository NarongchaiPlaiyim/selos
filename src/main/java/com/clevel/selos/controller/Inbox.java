package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "inbox")
public class Inbox implements Serializable {
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
    InboxControl inboxControl;

    private UserDetail userDetail;
    private List<InboxView> inboxViewList;

    private InboxView inboxViewSelectItem;

    public Inbox() {

    }

    @PostConstruct
    public void onCreation() {
        // *** Get user from Session *** //
        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("onCreation ::: userDetail : {}", userDetail);
        try {
            inboxViewList = inboxControl.getInboxFromBPM(userDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("onCreation ::: inboxViewList : {}", inboxViewList);
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
        session.setAttribute("workCasePreScreenId", inboxViewSelectItem.getWorkCasePreScreenId());
        session.setAttribute("workCaseId", inboxViewSelectItem.getWorkCaseId());
        session.setAttribute("stepId", inboxViewSelectItem.getStepId());
        session.setAttribute("queueName", inboxViewSelectItem.getQueueName());

        //*** Get Information for Header ***//
        AppHeaderView appHeaderView = inboxControl.getHeaderInformation(inboxViewSelectItem.getWorkCasePreScreenId(), inboxViewSelectItem.getWorkCaseId());
        session.setAttribute("appHeaderInfo", appHeaderView);

        if (inboxViewSelectItem.getStepId() == 1001) {
            FacesUtil.redirect("/site/prescreenInitial.jsf");
        } else if (inboxViewSelectItem.getStepId() == 1002) {
            FacesUtil.redirect("/site/prescreenChecker.jsf");
        } else if (inboxViewSelectItem.getStepId() == 1003) {
            FacesUtil.redirect("/site/prescreenMaker.jsf");
        }
        return;
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

    public InboxView getInboxViewSelectItem() {
        return inboxViewSelectItem;
    }

    public void setInboxViewSelectItem(InboxView inboxViewSelectItem) {
        this.inboxViewSelectItem = inboxViewSelectItem;
    }
}
