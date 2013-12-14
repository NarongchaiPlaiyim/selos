package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.*;
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
import javax.swing.text.html.parser.AttributeList;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "updateApproveDetail")
public class UpdateApproveDetail implements Serializable {

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
    private List<AccountInfoDetailView> accountInfoDetailViews;
    private List<FollowUpConditionView>  followUpConditionViews;
    private LoanPaymentDetailView loanPaymentDetailView;

    public void preRender() {

        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);
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
    public void onCreation(){
        followUpConditionViews = new ArrayList<FollowUpConditionView>();
        approveCreditDetailViews = new ArrayList<NewCreditDetailView>();
        accountInfoDetailViews = new ArrayList<AccountInfoDetailView>();
        loanPaymentDetailView = new LoanPaymentDetailView();
        try{
            loanPaymentDetailView.setPayDate(3);
            loanPaymentDetailView.setFirstPayDate(null);
            loanPaymentDetailView.setSignDate(new Date());
            FollowUpConditionView followUpConditionView = new FollowUpConditionView();
            followUpConditionView.setCondition("ให้ Operation");
            followUpConditionView.setDetailOfFollowUp("dfdfdfdfdf");
            followUpConditionView.setDateOfFollowUp(new Date());
            followUpConditionViews.add(followUpConditionView);

            AccountInfoDetailView accountInfoDetailView = new AccountInfoDetailView();
            AccountInfoCreditTypeView accountInfoCreditTypeView = new AccountInfoCreditTypeView();
            accountInfoCreditTypeView.setLimit(BigDecimal.TEN);

            accountInfoCreditTypeView.setCreditFacility("Facility");
            accountInfoCreditTypeView.setProductProgram("product");
            List<AccountInfoCreditTypeView> accountInfoCreditTypeViews = new ArrayList<AccountInfoCreditTypeView>();
            accountInfoCreditTypeViews.add(accountInfoCreditTypeView);
            accountInfoDetailView.setAccountInfoCreditTypeViewList(accountInfoCreditTypeViews);

            AccountNameView accountNameView = new AccountNameView();
            accountNameView.setName("sdsd");
            List<AccountNameView> accountNameViews = new ArrayList<AccountNameView>();
            accountNameViews.add(accountNameView);
            accountInfoDetailView.setAccountNameViewList(accountNameViews);
            accountInfoDetailView.setAccountNumber("1212121212");

            accountInfoDetailViews.add(accountInfoDetailView);



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

    public List<AccountInfoDetailView> getAccountInfoDetailViews() {
        return accountInfoDetailViews;
    }

    public void setAccountInfoDetailViews(List<AccountInfoDetailView> accountInfoDetailViews) {
        this.accountInfoDetailViews = accountInfoDetailViews;
    }

    public List<FollowUpConditionView> getFollowUpConditionViews() {
        return followUpConditionViews;
    }

    public void setFollowUpConditionViews(List<FollowUpConditionView> followUpConditionViews) {
        this.followUpConditionViews = followUpConditionViews;
    }

    public LoanPaymentDetailView getLoanPaymentDetailView() {
        return loanPaymentDetailView;
    }

    public void setLoanPaymentDetailView(LoanPaymentDetailView loanPaymentDetailView) {
        this.loanPaymentDetailView = loanPaymentDetailView;
    }
}
