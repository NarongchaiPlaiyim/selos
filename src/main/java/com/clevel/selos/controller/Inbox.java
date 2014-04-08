package com.clevel.selos.controller;

import com.clevel.selos.dao.master.RoleInboxDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.InboxType;
import com.clevel.selos.model.db.relation.RelRoleBasedInbox;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "inbox")
public class Inbox implements Serializable {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    @NormalMessage
    private Message msg;

    @Inject
    @ValidationMessage
    private Message validationMsg;

    @Inject
    @ExceptionMessage
    private Message exceptionMsg;

    @Inject
    private InboxType inboxType;

    @Inject
    private RoleInboxDAO roleInboxDAO;

    @Inject
    private RelRoleBasedInbox relRoleBasedInbox;

    private UserDetail userDetail;

    private List<String> inboxTypeList;

    private List<RelRoleBasedInbox> relReturnInboxList;

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

        log.info("onCreation ::: userDetail roleId : {}", roleId);

        relRoleBasedInbox.setRoleId(roleId);

        log.info("relRoleBasedInbox ::: {}", relRoleBasedInbox.getRoleId());

        int inboxRoleId =   relRoleBasedInbox.getRoleId();
        int inboxId = relRoleBasedInbox.getInboxId();
        log.info("inboxId ::::::: {}", inboxId);

        relReturnInboxList = new ArrayList<RelRoleBasedInbox>();

        inboxTypeList = new ArrayList<String>();
        inboxTypeList = roleInboxDAO.getUserBasedRole(inboxRoleId,relRoleBasedInbox,inboxType);
        log.info("inboxTypeList::::::::::::::::::::::: {}", inboxTypeList);

        for(String inboxName : inboxTypeList){
            InboxType inboxType = new InboxType();
            inboxType.setInbox_name(inboxName);
            inboxTypeArrayList.add(inboxType);
        }
        log.info("inboxTypeArrayList ::::::: {}", inboxTypeArrayList);
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public ArrayList<InboxType> getInboxTypeArrayList() {
        return inboxTypeArrayList;
    }

    public void setInboxTypeArrayList(ArrayList<InboxType> inboxTypeArrayList) {
        this.inboxTypeArrayList = inboxTypeArrayList;
    }

}
