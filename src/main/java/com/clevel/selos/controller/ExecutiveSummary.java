package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.model.view.NCBInfoView;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "executiveSummary")
public class ExecutiveSummary implements Serializable {

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
    private User user;
    private Date date;
    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}
    private ModeForDB modeForDB;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    private ExSummaryView exSummaryView;

    @Inject
    UserDAO userDAO;
    @Inject
    CustomerDAO customerDAO;

    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    NCBInfoControl ncbInfoControl;

    public ExecutiveSummary() {}

    public void preRender(){
//        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("workCaseId", 101);
//        session.setAttribute("stepId", 1004);
//
//        log.info("preRender ::: setSession ");
//
//        session = FacesUtil.getSession(true);
//
//        user = getCurrentUser();
//
//        if(session.getAttribute("workCaseId") != null){
//            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//        }else{
//            log.info("preRender ::: workCaseId is null.");
//            try{
//                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
//            }catch (Exception ex){
//                log.info("Exception :: {}",ex);
//            }
//        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 101);    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox
        user = (User) session.getAttribute("user");

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("preRender ::: workCaseId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

        if(exSummaryView == null){
            exSummaryView = new ExSummaryView();
        }

        getBorrower();

        getNCBRecord();
    }

    public void onSaveExecutiveSummary() {
        log.info("onSaveExecutiveSummary ::: ModeForDB  {}", modeForDB);

        try {
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("") + ex.getMessage();
            }
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelExecutiveSummary() {
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelExecutiveSummary ::: ");

        onCreation();
    }

    public void getBorrower(){
        List<CustomerInfoView> cusListView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        if(cusListView != null && cusListView.size() > 0){
            exSummaryView.setBorrowerViewList(cusListView);
        } else {
            exSummaryView.setBorrowerViewList(null);
        }
    }

    public void getNCBRecord(){
        List<NCBInfoView> ncbInfoViewList = ncbInfoControl.getNCBInfoViewByWorkCaseId(workCaseId);
        if(ncbInfoViewList != null && ncbInfoViewList.size() > 0){
            exSummaryView.setNcbInfoViewList(ncbInfoViewList);
        } else {
            exSummaryView.setNcbInfoViewList(null);
        }
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

    public ExSummaryView getExSummaryView() {
        return exSummaryView;
    }

    public void setExSummaryView(ExSummaryView exSummaryView) {
        this.exSummaryView = exSummaryView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

