package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "custInfoSummary")
public class CustomerInfoSummary implements Serializable {
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
    private CustomerEntityDAO customerEntityDAO;
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private CustomerInfoControl customerInfoControl;

    //*** Drop down List ***//
    private List<CustomerEntity> customerEntityList;

    //*** View ***//
    private CustomerInfoSummaryView customerInfoSummaryView;

    //session
    private long workCaseId;

    private CustomerInfoView selectedItemCustomerBorrower;
    private CustomerInfoView selectedItemCustomerGuarantor;
    private CustomerInfoView selectedItemCustomerRelated;

    private String messageHeader;
    private String message;

    public CustomerInfoSummary(){
    }

    @PostConstruct
    public void onCreation() {
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("preRender ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

        customerEntityList = customerEntityDAO.findAll();
        customerInfoSummaryView = new CustomerInfoSummaryView();

        customerInfoSummaryView = customerInfoControl.getCustomerInfoSummary(workCaseId);
    }

    public String onLinkToEditBorrower() {
        long customerId;
        if(selectedItemCustomerBorrower.getSpouse() != null && selectedItemCustomerBorrower.getIsSpouse() == 1){
            Customer customer = customerDAO.findMainCustomerBySpouseId(selectedItemCustomerBorrower.getId());
            customerId = customer.getId();
        }else{
            customerId = selectedItemCustomerBorrower.getId();
        }

        if(selectedItemCustomerBorrower.getCustomerEntity().getId() == 1){ // Individual
            passParamsToIndividual(customerId);
            return "customerInfoIndividual?faces-redirect=true";
        }else{
            passParamsToJuristic(customerId);
            return "customerInfoJuristic?faces-redirect=true";
        }
    }

    public String onLinkToEditGuarantor() {
        long customerId;
        if(selectedItemCustomerGuarantor.getSpouse() != null && selectedItemCustomerGuarantor.getIsSpouse() == 1){
            Customer customer = customerDAO.findMainCustomerBySpouseId(selectedItemCustomerGuarantor.getId());
            customerId = customer.getId();
        }else{
            customerId = selectedItemCustomerGuarantor.getId();
        }

        if(selectedItemCustomerGuarantor.getCustomerEntity().getId() == 1){ // Individual
            passParamsToIndividual(customerId);
            return "customerInfoIndividual?faces-redirect=true";
        }else{
            passParamsToJuristic(customerId);
            return "customerInfoJuristic?faces-redirect=true";
        }
    }

    public String onLinkToEditRelated() {
        long customerId;
        if(selectedItemCustomerRelated.getSpouse() != null && selectedItemCustomerRelated.getIsSpouse() == 1){
            Customer customer = customerDAO.findMainCustomerBySpouseId(selectedItemCustomerRelated.getId());
            customerId = customer.getId();
        }else{
            customerId = selectedItemCustomerRelated.getId();
        }

        if(selectedItemCustomerRelated.getCustomerEntity().getId() == 1){ // Individual
            passParamsToIndividual(customerId);
            return "customerInfoIndividual?faces-redirect=true";
        }else{
            passParamsToJuristic(customerId);
            return "customerInfoJuristic?faces-redirect=true";
        }
    }

    private void passParamsToIndividual(long customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",true);
        map.put("isFromJuristicParam",false);
        map.put("isFromIndividualParam",false);
        map.put("isEditFromJuristic", false);
        map.put("customerId", customerId);
        CustomerInfoView cusView = new CustomerInfoView();
        cusView.reset();
        map.put("customerInfoView", cusView);
        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("cusInfoParams", map);
        FacesUtil.getFlash().put("cusInfoParams", map);
    }

    private void passParamsToJuristic(long customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",true);
        map.put("isFromJuristicParam",false);
        map.put("isFromIndividualParam",false);
        map.put("isEditFromJuristic", false);
        map.put("customerId", customerId);
        CustomerInfoView cusView = new CustomerInfoView();
        cusView.reset();
        map.put("customerInfoView", cusView);
        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("cusInfoParams", map);
        FacesUtil.getFlash().put("cusInfoParams", map);
    }

    public String onLinkToAddIndividual(){
        passParamsToIndividual(0);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public String onLinkToAddJuristic(){
        passParamsToJuristic(0);
        return "customerInfoJuristic?faces-redirect=true";
    }

    public void onDeleteGuarantor(){
        try{
            onDelete(selectedItemCustomerGuarantor);
            messageHeader = "Delete Customer Info Guarantor Success.";
            message = "Delete Customer Info Guarantor Success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Delete Customer Info Guarantor Failed.";
            if(ex.getCause() != null){
                message = "Delete Customer Info Guarantor failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Delete Customer Info Guarantor failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
        onCreation();
    }

    public void onDeleteRelated(){
        try{
            onDelete(selectedItemCustomerRelated);
            messageHeader = "Delete Customer Info Related Success.";
            message = "Delete Customer Info Related Success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Delete Customer Info Related Failed.";
            if(ex.getCause() != null){
                message = "Delete Customer Info Related failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Delete Customer Info Related failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
        onCreation();
    }

    private void onDelete(CustomerInfoView customerInfoView){
        if(customerInfoView.getCustomerEntity().getId() == 1){ // Individual
            customerInfoControl.deleteCustomerIndividual(customerInfoView.getId());
        } else {
            customerInfoControl.deleteCustomerJuristic(customerInfoView.getId());
        }
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

    public CustomerInfoView getSelectedItemCustomerBorrower() {
        return selectedItemCustomerBorrower;
    }

    public void setSelectedItemCustomerBorrower(CustomerInfoView selectedItemCustomerBorrower) {
        this.selectedItemCustomerBorrower = selectedItemCustomerBorrower;
    }

    public CustomerInfoView getSelectedItemCustomerGuarantor() {
        return selectedItemCustomerGuarantor;
    }

    public void setSelectedItemCustomerGuarantor(CustomerInfoView selectedItemCustomerGuarantor) {
        this.selectedItemCustomerGuarantor = selectedItemCustomerGuarantor;
    }

    public CustomerInfoView getSelectedItemCustomerRelated() {
        return selectedItemCustomerRelated;
    }

    public void setSelectedItemCustomerRelated(CustomerInfoView selectedItemCustomerRelated) {
        this.selectedItemCustomerRelated = selectedItemCustomerRelated;
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
