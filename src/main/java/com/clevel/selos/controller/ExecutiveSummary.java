package com.clevel.selos.controller;


import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.master.User;
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
import java.io.Serializable;


@ViewScoped
@ManagedBean(name = "executiveSummary")
public class ExecutiveSummary implements Serializable {

    @Inject
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
    private User user;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    enum ModeForDB{ ADD_DB, EDIT_DB,CANCEL_DB }
    private ModeForDB  modeForDB;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    @Inject
    UserDAO userDAO;

    public ExecutiveSummary(){}


    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session  = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", new Long(2)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
        }
    }

    public void onSaveExecutiveSummary(){
        log.info("onSaveExecutiveSummary ::: ModeForDB  {}", modeForDB);

        try{
                messageHeader = msg.get("app.header.save.success");
                message = msg.get("");
                onCreation();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");


        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if(ex.getCause() != null){
                message = msg.get("") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }

    }


    public void onCancelExecutiveSummary(){
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelExecutiveSummary ::: ");

        onCreation();
    }


    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
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
}

