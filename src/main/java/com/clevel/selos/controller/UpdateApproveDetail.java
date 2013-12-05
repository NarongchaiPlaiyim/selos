package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.model.view.OpenAccountView;
import com.clevel.selos.model.view.openaccount.AccountNameView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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

    //Modify

    // content //
    private List<NewCreditDetailView> approveCreditDetailViews;
    private List<OpenAccountView> openAccountViews;

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
        approveCreditDetailViews = new ArrayList<NewCreditDetailView>();
        openAccountViews = new ArrayList<OpenAccountView>();
        try{
        List<OpenAccountView> openAccountViewList = new ArrayList<OpenAccountView>();

        OpenAccountView openAccountView = new OpenAccountView();
            List<AccountNameView> accountNameViews = new ArrayList<AccountNameView>();
            for(int i =0; i<10; i++){
                AccountNameView accountNameView = new AccountNameView();
                accountNameView.setName(String.valueOf(i));
                accountNameViews.add(accountNameView);
            }

            openAccountView.setAccountNameViewList(accountNameViews);
            openAccountView.setAccountNumber("1234567890");
            openAccountViewList.add(openAccountView);
            openAccountViews = openAccountViewList;

        }catch (Exception e){

        }
    }

    public BigDecimal getTotalApprovedCredit(){
        BigDecimal result = BigDecimal.ZERO;
        for(NewCreditDetailView newCreditDetailView : Util.safetyList(approveCreditDetailViews)){
            result = Util.add(result,newCreditDetailView.getLimit());
        }
    return result;
    }

    public List<String> getStringList(){
        List<String> result = new ArrayList<String>();
        for(int i = 0; i <5; i++){
            result.add(String.valueOf(i));
        }
        return result;
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

    public List<NewCreditDetailView> getApproveCreditDetailViews() {
        return approveCreditDetailViews;
    }

    public void setApproveCreditDetailViews(List<NewCreditDetailView> approveCreditDetailViews) {
        this.approveCreditDetailViews = approveCreditDetailViews;
    }

    public List<OpenAccountView> getOpenAccountViews() {
        return openAccountViews;
    }

    public void setOpenAccountViews(List<OpenAccountView> openAccountViews) {
        this.openAccountViews = openAccountViews;
    }
}
