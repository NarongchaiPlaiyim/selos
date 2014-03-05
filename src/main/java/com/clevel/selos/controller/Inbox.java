package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.dao.master.RoleInboxDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.InboxType;
import com.clevel.selos.model.db.relation.RelRoleBasedInbox;
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


    List<String> inboxTypeList;

    private InboxView inboxViewSelectItem;


    @Inject
    InboxType inboxType;
    @Inject
    RoleInboxDAO roleInboxDAO;

    @Inject
    RelRoleBasedInbox relRoleBasedInbox;

    List<RelRoleBasedInbox> relReturnInboxList;
    List<String> stringInboxList = null;
    List<Integer> inboxValue;

    public ArrayList<InboxType> getInboxTypeArrayList() {
        return inboxTypeArrayList;
    }

    public void setInboxTypeArrayList(ArrayList<InboxType> inboxTypeArrayList) {
        this.inboxTypeArrayList = inboxTypeArrayList;
    }

    private ArrayList<InboxType> inboxTypeArrayList;



    public Inbox() {

    }

    @PostConstruct
    public void onCreation()
    {
        inboxTypeArrayList = new ArrayList<InboxType>();
        // *** Get user from Session *** //
        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("controler in oncreation method of inbox.java class ");

        log.info("onCreation ::: userDetail : {}", userDetail);


        int roleId = userDetail.getRoleId();

        log.info("onCreation ::: userDetail : {}", roleId);

        relRoleBasedInbox.setRoleId(roleId);

        log.info("relRoleBasedInbox ::: {}", relRoleBasedInbox.getRoleId());


        int inboxRoleId =   relRoleBasedInbox.getRoleId();
        int inboxId = relRoleBasedInbox.getInboxId();
        log.info("inboxId ::::::: {}"+inboxId);

        relReturnInboxList = new ArrayList<RelRoleBasedInbox>();

        inboxTypeList = new ArrayList<String>();


        inboxTypeList = roleInboxDAO.getUserBasedRole(inboxRoleId,relRoleBasedInbox,inboxType);
        log.info("inboxTypeList:::::::::::::::::::::::"+inboxTypeList);
        for(int inboxNameNo=0; inboxNameNo < inboxTypeList.size(); inboxNameNo++)
        {
            InboxType inboxType = new InboxType();
            inboxType.setInbox_name(inboxTypeList.get(inboxNameNo));
            inboxTypeArrayList.add(inboxType);
            inboxType = null;
        }
        log.info("inboxTypeArrayList ::::::: {}"+inboxTypeArrayList);



        try
        {
            //inboxViewList = inboxControl.getInboxFromBPM(userDetail);

            log.debug("onCreation ::: inboxViewList : {}", inboxViewList);

        }
        catch (Exception e)
        {
            log.error("Exception while getInbox : ", e);
        }
    }

    public void onSelectInbox() {


        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("userDetails  : "+userDetail);

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

        session.setAttribute("stepId", inboxViewSelectItem.getStepId());
        session.setAttribute("queueName", inboxViewSelectItem.getQueueName());
        session.setAttribute("requestAppraisal", inboxViewSelectItem.getRequestAppraisal());

        //*** Get Information for Header ***//
        AppHeaderView appHeaderView = inboxControl.getHeaderInformation(inboxViewSelectItem.getWorkCasePreScreenId(), inboxViewSelectItem.getWorkCaseId());
        session.setAttribute("appHeaderInfo", appHeaderView);

        long selectedStepId = inboxViewSelectItem.getStepId();
        String landingPage = inboxControl.getLandingPage(selectedStepId);

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

    public InboxView getInboxViewSelectItem() {
        return inboxViewSelectItem;
    }

    public void setInboxViewSelectItem(InboxView inboxViewSelectItem) {
        this.inboxViewSelectItem = inboxViewSelectItem;
    }

}
