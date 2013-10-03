package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

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
        session.setAttribute("workCasePreScreenId", new Long(1));
        session.setAttribute("stepId", new Long(2));
        session = FacesUtil.getSession(true);
        //user = (User)session.getAttribute("user");
        if(session.getAttribute("workCasePreScreenId") != null){
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
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
}
