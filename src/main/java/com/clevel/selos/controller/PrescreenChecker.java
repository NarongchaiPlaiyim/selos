package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenChecker")
public class PrescreenChecker implements Serializable {
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


    private List<CustomerInfoView> customerInfoViewList;
    private String[] citizenID;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    PrescreenBusinessControl prescreenBusinessControl;

    private long workCasePreScreenId;
    private long stepId;
    private String queueName;
    private String userId;

    private String messageHeader;
    private String message;

    public PrescreenChecker(){

    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(false);
        log.info("preRender ::: setSession ");
        session.setAttribute("workCasePreScreenId", new Long(1));
        session.setAttribute("stepId", new Long(2));

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }else{
            //TODO return to inbox
            log.info("onCreation ::: workCasePrescreenId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/prescreenInitial.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation");
        HttpSession session = FacesUtil.getSession(false);
        /*session.setAttribute("workCasePreScreenId", new Long(1));
        session.setAttribute("stepId", new Long(2));*/
        session = FacesUtil.getSession(true);
        //user = (User)session.getAttribute("user");
        if(session.getAttribute("workCasePreScreenId") != null){
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = userDetail.getUserName();
        }

        //TODO Generate row for textBox to check Citizen id
        List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        if(customerInfoViews != null){
            customerInfoViewList = prescreenBusinessControl.getBorrowerViewListByCustomerViewList(customerInfoViews);
        }

        if(customerInfoViewList != null){
            int row = customerInfoViewList.size();
            citizenID = new String[row];
        }
    }

    public void onCheckCustomer(){
        List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        tmpCustomerInfoViewList = customerInfoViewList;
        customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
        for(CustomerInfoView customer : tmpCustomerInfoViewList){
            if(customer.getCitizenId().trim().equals(customer.getInputId().trim())){
                log.info("Check CitizenID Customer : {}, Match", customer.getFirstNameTh());
                customer.setValidId(1);
            }else{
                log.info("Check CitizenID Customer : {}, Not Match", customer.getFirstNameTh());
                customer.setValidId(0);
            }
            customerInfoViewList.add(customer);
        }

        /*List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        tmpCustomerInfoViewList = customerInfoViewList;
        customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
        for(CustomerInfoView customer : tmpCustomerInfoViewList){
            if(customer.getCitizenID().trim().equals(customer.getInputCitizenID().trim())){
                log.info("Check CitizenID Customer : {}, Match", customer.getCustomerName());
                customer.setValidCitizenID(1);
            }else{
                log.info("Check CitizenID Customer : {}, Not Match", customer.getCustomerName());
                customer.setValidCitizenID(0);
            }
            customerInfoViewList.add(customer);
        }*/

    }

    public void onReturnToMaker(){
        log.info("onReturnToMaker :::");
        //Only return to MAKER actionCode =
        String actionCode = "1005";
        prescreenBusinessControl.nextStepPreScreen(workCasePreScreenId, queueName, actionCode);
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            return;
        } catch (Exception ex) {
            log.error("Error to redirect : {}", ex.getMessage());
        }
    }

    public void onCheckNCB(){
        //To Get NCB Data and submit to MAKER
        log.info("onCheckNCB :::");
        try{
            //TODO get data for NCB
            //** Retrieve new customer data !protect data is not up to date **//
            List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
            prescreenBusinessControl.getNCBFromNCB(customerInfoViews, userId, workCasePreScreenId);
        } catch(Exception ex){
            messageHeader = "Operation failed.";
            message = "Check NCB error.";
        }

        //TODO get csi data for

        //messageHeader = customerInfoResultView.getActionResult().toString();
        //message = customerInfoResultView.getReason();
        //TODO Show message box
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCompleteCheckNCB(){
        String actionCode = "1004";
        prescreenBusinessControl.nextStepPreScreen(workCasePreScreenId, queueName, actionCode);
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            return;
        } catch (Exception ex) {
            log.error("Error to redirect : {}", ex.getMessage());
        }
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public String[] getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String[] citizenID) {
        this.citizenID = citizenID;
    }

    public long getWorkCasePreScreenId() {
        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
