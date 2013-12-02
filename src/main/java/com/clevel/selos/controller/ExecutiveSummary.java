package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.AuthorizationDOADAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
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
    //private User user;
    private Date date;
    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}
    private ModeForDB modeForDB;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    private ExSummaryView exSummaryView;

    private ExSumReasonView selectDeviate;

    @Inject
    UserDAO userDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    ReasonDAO reasonDAO;
    @Inject
    AuthorizationDOADAO authorizationDOADAO;

    @Inject
    ExSummaryControl exSummaryControl;

    //*** Drop down List ***//
    private List<AuthorizationDOA> authorizationDOAList;
    private List<Reason> reasonList;

    //
    private ExSumReasonView reason;

    public ExecutiveSummary() {
    }

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
        //session.setAttribute("workCaseId", 101);    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox
//        user = (User) session.getAttribute("user");
        //user = getCurrentUser();

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

        reasonList = reasonDAO.getRejectList();
        authorizationDOAList = authorizationDOADAO.findAll();

        reason = new ExSumReasonView();

        exSummaryView = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);

        if(exSummaryView == null){
            exSummaryView = new ExSummaryView();
        }



        /*ExSumCharacteristicView ec = new ExSumCharacteristicView();
        ec.reset();
        exSummaryView.setExSumCharacteristicView(ec);

        ExSumBusinessInfoView eb = new ExSumBusinessInfoView();
        eb.reset();
        exSummaryView.setExSumBusinessInfoView(eb);

        ExSumAccountMovementView ea = new ExSumAccountMovementView();
        ea.reset();
        exSummaryView.setExSumAccMovementView(ea);

        ExSumCollateralView ecc = new ExSumCollateralView();
        ecc.reset();
        exSummaryView.setExSumCollateralView(ecc);*/
    }

    public void onSaveExecutiveSummary() {
        log.info("onSaveExecutiveSummary ::: ModeForDB  {}", modeForDB);

        try {
            exSummaryControl.saveExSummary(exSummaryView,workCaseId);

            messageHeader = msg.get("app.header.save.success");
//            message = msg.get("Save Ex Summary data success.");
            message = "Save Ex Summary data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
//                message = msg.get("")+ ex.getCause().toString();
                message = "Save Ex Summary data failed. Cause : " + ex.getCause().toString();
            } else {
//                message = msg.get("") + ex.getMessage();
                message = "Save Ex Summary data failed. Cause : " + ex.getMessage();
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

    public void onAddReason() {
        ExSumReasonView exSumReasonView = new ExSumReasonView();
        exSumReasonView.setCode(reason.getCode());
        exSummaryView.getDeviateCode().add(exSumReasonView);
    }

    public void onDeleteDeviate(){
        exSummaryView.getDeviateCode().remove(selectDeviate);
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
}

