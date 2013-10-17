package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.NcbView;
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
    ReasonDAO reasonDAO;
    @Inject
    UserDAO userDAO;
    @Inject
    PrescreenBusinessControl prescreenBusinessControl;

    private List<Reason> reasonList;

    private long workCasePreScreenId;
    private long stepId;
    private String queueName;
    private String userId;

    private String messageHeader;
    private String message;
    private boolean messageErr;

    private Reason returnReason;
    private String bdmMakerName;
    private String remark;

    public PrescreenChecker(){

    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

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
        HttpSession session = FacesUtil.getSession(true);

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

        log.info("customerinfoList : {}", customerInfoViewList);

    }

    public void onCheckCustomer(){
        log.info("onCheckCustomer :::");
        List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        tmpCustomerInfoViewList = customerInfoViewList;
        customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
        boolean validate = false;
        boolean tmpValidate = false;
        int count = 0;
        for(CustomerInfoView customer : tmpCustomerInfoViewList){
            log.info("CustomerInfo : {}", customer);
            if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(customer.getCitizenId().trim().equals(customer.getInputId().trim())){
                    log.info("Check CitizenID Customer : {}, Match", customer.getFirstNameTh());
                    customer.setValidId(1);
                    customer.setNcbReason("");
                    tmpValidate = true;
                }else{
                    log.info("Check CitizenID Customer : {}, Not Match", customer.getFirstNameTh());
                    customer.setValidId(0);
                    customer.setNcbReason("");
                    tmpValidate = false;
                }
            } else {
                if(customer.getRegistrationId().trim().equals(customer.getInputId().trim())){
                    log.info("Check RegistrationID Customer : {}, Match", customer.getFirstNameTh());
                    customer.setValidId(1);
                    customer.setNcbReason("");
                    tmpValidate = true;
                }else{
                    log.info("Check RegistrationID Customer : {}, Not Match", customer.getFirstNameTh());
                    customer.setValidId(0);
                    customer.setNcbReason("");
                    tmpValidate = false;
                }
            }

            if(count == 0){
                if(tmpValidate == true)
                    validate = true;
            } else {
                if(tmpValidate == true && validate == true){
                    validate = true;
                } else if(tmpValidate == false && validate == true){
                    validate = false;
                } else if(tmpValidate == true && validate == false){
                    validate = false;
                }
            }
            customerInfoViewList.add(customer);
        }
        if(validate){
            //TODO Check ncb
            onCheckNCB();
        }
    }

    public void onReturnToMaker(){
        returnReason = new Reason();
        reasonList = new ArrayList<Reason>();
        reasonList = reasonDAO.getCancleList();

        bdmMakerName = prescreenBusinessControl.getBDMMakerName(workCasePreScreenId);
    }

    public void onSubmitReturnToMaker(){
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
        boolean success = false;
        try{
            //TODO get data for NCB
            //** Retrieve new customer data !protect data is not up to date **//
            List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
            log.info("onCheckNCB ::: customerInfoView size : {}", customerInfoViews.size());
            List<NcbView> ncbViewList = prescreenBusinessControl.getNCBFromNCB(customerInfoViews, userId, workCasePreScreenId);
            int index = 0;
            int failedCount = 0;
            for(CustomerInfoView customerInfoView : customerInfoViewList){
                if(customerInfoView.getNcbFlag() == RadioValue.YES.value()){
                    customerInfoView.setNcbReason("");
                    customerInfoView.setNcbResult(ActionResult.SUCCEED.name());
                }
            }
            if(ncbViewList != null){
                for(NcbView item : ncbViewList){
                    index = 0;
                    for(CustomerInfoView customerInfoView : customerInfoViewList){
                        if(item.getIdNumber() != null){
                            log.info("onCheckNCB ::: index : {}", index);
                            if(customerInfoView.getCustomerEntity() != null){
                                if(customerInfoView.getCustomerEntity().getId() == 1 && customerInfoView.getCitizenId() != null){
                                    if(item.getIdNumber().equals(customerInfoView.getCitizenId())){
                                        log.info("onCheckNCB ::: individual citizenId : {}", customerInfoView.getCitizenId());
                                        customerInfoView.setNcbReason(item.getReason());
                                        customerInfoView.setNcbResult(item.getResult().name());
                                        if(item.getResult().equals(ActionResult.SUCCEED)){
                                            customerInfoView.setNcbFlag(RadioValue.YES.value());
                                        }else{
                                            failedCount = failedCount + 1;
                                        }
                                    }
                                }else if(customerInfoView.getCustomerEntity().getId() == 2 && customerInfoView.getRegistrationId() != null){
                                    if(item.getIdNumber().equals(customerInfoView.getRegistrationId())){
                                        log.info("onCheckNCB ::: juristic registerId : {}", customerInfoView.getRegistrationId());
                                        customerInfoView.setNcbReason(item.getReason());
                                        customerInfoView.setNcbResult(item.getResult().name());
                                        if(item.getResult().equals(ActionResult.SUCCEED)){
                                            customerInfoView.setNcbFlag(RadioValue.YES.value());
                                        }else{
                                            failedCount = failedCount + 1;
                                        }
                                    }
                                }
                            }
                        }
                        log.info("onCheckNCB ::: setCustomerInfo : {}", customerInfoView);
                        customerInfoViewList.set(index, customerInfoView);
                        index = index + 1;
                    }
                }
                //TODO update customer to database
                log.debug("onCheckNCB ::: customerInfoViewList : {}", customerInfoViewList);
                prescreenBusinessControl.savePreScreenChecker(customerInfoViewList, workCasePreScreenId);
                if(failedCount != 0){
                    success = false;
                }
            }
        } catch(Exception ex){
            ex.printStackTrace();
            log.error("Exception : {}", ex);
            messageHeader = "Check NCB failed.";
            message = ex.getMessage();
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        //TODO Show message box
        if(success){
            //TODO submit case
            try{
                String actionCode = "1004";
                prescreenBusinessControl.nextStepPreScreen(workCasePreScreenId, queueName, actionCode);
            }catch (Exception ex){
                messageHeader = "Check NCB failed.";
                message = ex.getMessage();
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
            //TODO Redirect to inbox
            log.debug("onCheckNCB ::: success without failed. redirect to inbox!");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

    }

    public void onCompleteCheckNCB(){
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

    public List<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public Reason getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(Reason returnReason) {
        this.returnReason = returnReason;
    }

    public String getBdmMakerName() {
        return bdmMakerName;
    }

    public void setBdmMakerName(String bdmMakerName) {
        this.bdmMakerName = bdmMakerName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
    }
}
