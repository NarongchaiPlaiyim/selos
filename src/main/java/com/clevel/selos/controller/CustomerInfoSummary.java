package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.master.CustomerEntityControl;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "custInfoSummary")
public class CustomerInfoSummary extends BaseController {
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
    private CustomerDAO customerDAO;

    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private CustomerEntityControl customerEntityControl;

    private List<SelectItem> customerEntityList;

    private CustomerInfoSummaryView customerInfoSummaryView;

    //session
    private long workCaseId;

    private CustomerInfoView selectedItemCustomerBorrower;
    private CustomerInfoView selectedItemCustomerGuarantor;
    private CustomerInfoView selectedItemCustomerRelated;

    private String messageHeader;
    private String message;
    private String severity;

    public CustomerInfoSummary(){
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(true);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");

        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            loadFieldControl(workCaseId, Screen.CUSTOMER_INFO_SUMMARY, ownerCaseUserId);

            customerEntityList = customerEntityControl.getCustomerEntitySelectItemList();
            customerInfoSummaryView = new CustomerInfoSummaryView();
            customerInfoSummaryView = customerInfoControl.getCustomerInfoSummary(workCaseId);
        }
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
            boolean isExist = customerInfoControl.checkExistingAll(selectedItemCustomerGuarantor.getId());
            if(isExist){
                messageHeader = "Information.";
                message = msg.get("app.message.customer.existing.error2");
                severity = "info";
            } else {
                onDelete(selectedItemCustomerGuarantor);
                messageHeader = "Information.";
                message = "Delete Customer Info Guarantor Success.";
                severity = "info";
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception :: {}",ex);
            messageHeader = "Error.";
            if(ex.getCause() != null){
                message = "Delete Customer Info Guarantor Failed. <br/><br/> Cause : " + ex.getCause().toString();
            } else {
                message = "Delete Customer Info Guarantor Failed. <br/><br/> Cause : " + ex.getMessage();
            }
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
        onCreation();
    }

    public void onDeleteRelated(){
        try{
            onDelete(selectedItemCustomerRelated);
            messageHeader = "Information.";
            message = "Delete Customer Info Related Success.";
            severity = "info";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception :: {}",ex);
            messageHeader = "Error.";
            if(ex.getCause() != null){
                message = "Delete Customer Info Related failed. <br/><br/> Cause : " + ex.getCause().toString();
            } else {
                message = "Delete Customer Info Related failed. <br/><br/> Cause : " + ex.getMessage();
            }
            severity = "alert";
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

    public List<SelectItem> getCustomerEntityList() {
        return customerEntityList;
    }

    public void setCustomerEntityList(List<SelectItem> customerEntityList) {
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
