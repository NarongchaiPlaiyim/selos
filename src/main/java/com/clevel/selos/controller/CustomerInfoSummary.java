package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "custInfoSummary")
public class CustomerInfoSummary implements Serializable {
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

    @Inject
    private CustomerEntityDAO customerEntityDAO;
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private CustomerInfoControl customerInfoControl;

    //*** Drop down List ***//
    private List<CustomerEntity> customerEntityList;

    //*** View ***//
    private CustomerInfoSummaryView customerInfoSummaryView;

    private String messageHeader;
    private String message;

    //session
    private long workCaseId;
    private long stepId;
    private String userId;

    private CustomerInfoView selectEditCustomerBorrower;

    public CustomerInfoSummary(){
    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 101);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }


    @PostConstruct
    public void onCreation() {
        preRender();
        customerEntityList = customerEntityDAO.findAll();
        customerInfoSummaryView = new CustomerInfoSummaryView();

        customerInfoSummaryView = customerInfoControl.getCustomerInfoSummary(workCaseId);
    }

    public String onLinkToEditBorrower() {
        long customerId;
        if(selectEditCustomerBorrower.getIsSpouse() == 1){
            Customer customer = customerDAO.findCustomerBySpouseId(selectEditCustomerBorrower.getId());
            customerId = customer.getId();
        }else{
            customerId = selectEditCustomerBorrower.getId();
        }

        if(selectEditCustomerBorrower.getCustomerEntity().getId() == 1){ // Individual
            passParamsToIndividual(customerId);
            return "customerInfoIndividual?faces-redirect=true";
        }else{
            passParamsToJuristic(customerId);
            return "customerInfoJuristic?faces-redirect=true";
        }
    }

    public String onLinkToEditGuarantor() {
        return null;
    }

    public String onLinkToEditRelated() {
        return null;
    }

    private void passParamsToIndividual(long customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        FacesUtil.getFlash().put("cusInfoParams", map);
    }

    private void passParamsToJuristic(long customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        FacesUtil.getFlash().put("cusInfoParams", map);
    }

    public String onLinkToAddIndividual(){
        passParamsToIndividual(0);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public String onLinkToAddJuristic(){
        passParamsToIndividual(0);
        return "customerInfoJuristic?faces-redirect=true";
    }

    public CustomerInfoSummaryView getCustomerInfoSummaryView() {
        return customerInfoSummaryView;
    }

    public void setCustomerInfoSummaryView(CustomerInfoSummaryView customerInfoSummaryView) {
        this.customerInfoSummaryView = customerInfoSummaryView;
    }

    public List<CustomerEntity> getCustomerEntityList() {
        return customerEntityList;
    }

    public void setCustomerEntityList(List<CustomerEntity> customerEntityList) {
        this.customerEntityList = customerEntityList;
    }

    public CustomerInfoView getSelectEditCustomerBorrower() {
        return selectEditCustomerBorrower;
    }

    public void setSelectEditCustomerBorrower(CustomerInfoView selectEditCustomerBorrower) {
        this.selectEditCustomerBorrower = selectEditCustomerBorrower;
    }
}
