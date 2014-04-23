package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.NCBOutputView;
import com.clevel.selos.model.view.NcbView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
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

    private List<NcbView> ncbViewList;
    private NCBOutputView ncbOutputView;

    private int customerEntityId;

    boolean success;

    public PrescreenChecker(){

    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(true);
        log.debug("preRender ::: setSession ");

        if(session.getAttribute("workCasePreScreenId") != null){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }else{
            //TODO return to inbox
            log.debug("onCreation ::: workCasePrescreenId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/prescreenInitial.jsf");
                return;
            }catch (Exception ex){
                log.debug("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            log.debug("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = userDetail.getUserName();
        }

        //TODO Generate row for textBox to check Citizen id
        List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        if(customerInfoViews != null){
            customerInfoViewList = generateCustomerInfoList(customerInfoViews);
        }

        if(customerInfoViewList != null){
            int row = customerInfoViewList.size();
            citizenID = new String[row];
        }

        success = false;
        ncbViewList = new ArrayList<NcbView>();

        log.debug("customerinfoList : {}", customerInfoViewList);

    }

    public List<CustomerInfoView> generateCustomerInfoList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoList = new ArrayList<CustomerInfoView>();
        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation().getId() == 1){
                customerInfoList.add(item);
                if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    customerEntityId = BorrowerType.INDIVIDUAL.value();
                    if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                        CustomerInfoView spouse = new CustomerInfoView();
                        spouse = item.getSpouse();
                        if(spouse != null){
                            if(spouse.getRelation() != null && spouse.getRelation().getId() == 1){
                                customerInfoList.add(spouse);
                            }
                        }
                    }
                } else {
                    customerEntityId = BorrowerType.JURISTIC.value();
                }
            }
        }
        return customerInfoList;
    }

    public void onCheckCustomer(){
        log.debug("onCheckCustomer :::");
        RequestContext.getCurrentInstance().execute("blockUICheckCustomer.show()");
        List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        tmpCustomerInfoViewList = customerInfoViewList;
        customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
        int failCount = 0;
        for(CustomerInfoView customer : tmpCustomerInfoViewList){
            log.debug("CustomerInfo : {}", customer);
            if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(customer.getCitizenId().trim().equals(customer.getInputId().trim())){
                    log.debug("Check CitizenID Customer : {}, Match", customer.getFirstNameTh());
                    customer.setValidId(1);
                    customer.setNcbReason("");
                }else{
                    log.debug("Check CitizenID Customer : {}, Not Match", customer.getFirstNameTh());
                    customer.setValidId(0);
                    customer.setNcbReason("");
                    failCount = failCount + 1;
                }
            } else {
                if(customer.getRegistrationId().trim().equals(customer.getInputId().trim())){
                    log.debug("Check RegistrationID Customer : {}, Match", customer.getFirstNameTh());
                    customer.setValidId(1);
                    customer.setNcbReason("");
                }else{
                    log.debug("Check RegistrationID Customer : {}, Not Match", customer.getFirstNameTh());
                    customer.setValidId(0);
                    customer.setNcbReason("");
                    failCount = failCount + 1;
                }
            }
            customerInfoViewList.add(customer);
        }


        if(failCount == 0){
            //TODO Check ncb
            RequestContext.getCurrentInstance().execute("blockUICheckCustomer.hide()");
            RequestContext.getCurrentInstance().execute("commandCheckNCB()");
        }
    }

    public void onReturnToMaker(){
        returnReason = new Reason();
        reasonList = new ArrayList<Reason>();
        reasonList = reasonDAO.getCancelList();
        remark = "";

        bdmMakerName = prescreenBusinessControl.getBDMMakerName(workCasePreScreenId);
    }

    public void onSubmitReturnToMaker(){
        log.debug("onReturnToMaker ::: starting...");
        boolean complete = false;
        try{
            if(returnReason != null && returnReason.getId() != 0 && !Integer.toString(returnReason.getId()).equals("")){
                prescreenBusinessControl.returnBDM(workCasePreScreenId, queueName, ActionCode.RETURN_TO_BDM.getVal());
                messageHeader = "Information.";
                message = "Return to BDM Maker success. Click 'OK' return to inbox.";
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
                log.debug("onReturnToMaker ::: success...");
            } else {
                complete = false;
            }
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Return to BDM Maker failed. " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("onReturnToMaker ::: error... : ", ex);
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
        }
    }

    public void onCheckNCB(){
        //To get NCB data
        log.debug("onCheckNCB ::: starting...");
        try{
            //TODO get data for NCB
            //** Retrieve new customer data !protect data is not up to date **//
            ncbOutputView = prescreenBusinessControl.getNCBFromNCB(customerInfoViewList, userId, workCasePreScreenId);
            RequestContext.getCurrentInstance().execute("commandSaveNCB()");
            log.debug("onCheckNCB ::: success...");
        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = "Exception.";
            message = "Check NCB failed." + Util.getMessageException(ex);
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSaveNCB(){
        //To save NCB data
        log.debug("onSaveNCB ::: starting...");
        try{
            ncbViewList = prescreenBusinessControl.getNCBData(ncbOutputView, workCasePreScreenId);
            log.debug("onSaveNCB ::: ncbViewList : {}", ncbViewList);
            int index = 0;
            int failedCount = 0;
            int customerEntityId = 0;

            if(ncbOutputView != null){
                if(ncbOutputView.getNcrsOutputModelList() != null && ncbOutputView.getNcrsOutputModelList().size() > 0){
                    customerEntityId = 1;
                } else if (ncbOutputView.getNccrsOutputModelList() != null && ncbOutputView.getNccrsOutputModelList().size() > 0){
                    customerEntityId = 2;
                }
            }

            for(CustomerInfoView customerInfoView : customerInfoViewList){
                if(customerInfoView.getNcbFlag() == RadioValue.YES.value()){
                    customerInfoView.setNcbReason("");
                    customerInfoView.setNcbResult(ActionResult.SUCCESS.name());
                }
            }

            if(ncbViewList != null && ncbViewList.size() > 0){
                for(NcbView item : ncbViewList){
                    index = 0;
                    for(CustomerInfoView customerInfoView : customerInfoViewList){
                        if(item.getIdNumber() != null){
                            log.debug("onSaveNCB ::: index : {}", index);
                            if(customerInfoView.getCustomerEntity() != null){
                                if(customerInfoView.getCustomerEntity().getId() == 1 && customerInfoView.getCitizenId() != null){
                                    if(item.getIdNumber().equals(customerInfoView.getCitizenId())){
                                        log.debug("onCheckNCB ::: individual citizenId : {}", customerInfoView.getCitizenId());
                                        customerInfoView.setNcbReason(item.getReason());
                                        customerInfoView.setNcbResult(item.getResult().name());
                                        if(item.getResult() == ActionResult.SUCCESS){
                                            customerInfoView.setNcbFlag(RadioValue.YES.value());
                                        }else{
                                            failedCount = failedCount + 1;
                                        }
                                    }
                                }else if(customerInfoView.getCustomerEntity().getId() == 2 && customerInfoView.getRegistrationId() != null){
                                    if(item.getIdNumber().equals(customerInfoView.getRegistrationId())){
                                        log.debug("onSaveNCB ::: juristic registerId : {}", customerInfoView.getRegistrationId());
                                        customerInfoView.setNcbReason(item.getReason());
                                        customerInfoView.setNcbResult(item.getResult().name());
                                        if(item.getResult() == ActionResult.SUCCESS){
                                            customerInfoView.setNcbFlag(RadioValue.YES.value());
                                        }else{
                                            failedCount = failedCount + 1;
                                        }
                                    }
                                }
                            }
                        }
                        log.debug("onSaveNCB ::: setCustomerInfo : {}", customerInfoView);
                        customerInfoViewList.set(index, customerInfoView);
                        index = index + 1;
                    }
                }
                //TODO update customer to database
                log.debug("onSaveNCB ::: customerInfoViewList : {}", customerInfoViewList);
                prescreenBusinessControl.savePreScreenChecker(customerInfoViewList, ncbViewList, customerEntityId, workCasePreScreenId);
                log.debug("onSaveNCB ::: savePreScreenChecker success...");
                log.debug("onSaveNCB ::: failedCount : {}", failedCount);
            }

            if(failedCount != 0){
                success = false;
            } else {
                success = true;
            }

            RequestContext.getCurrentInstance().execute("commandCheckCSI()");
            log.debug("onSaveNCB ::: success...");
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Save NCB failed. " + Util.getMessageException(ex);
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("onSaveNCB ::: Exception : ", ex);
        }

    }

    public void onCheckCSI(){
        try{
            if(ncbViewList != null && ncbViewList.size() > 0){
                int failedCount = 0;
                List<CSIResult> csiResultList = new ArrayList<CSIResult>();
                csiResultList = prescreenBusinessControl.getCSIData(ncbViewList, customerEntityId, userId, workCasePreScreenId);
                log.debug("onCheckCSI ::: check CSI with NCB Data : csiResultList : {}", csiResultList);

                List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
                tmpCustomerInfoViewList = customerInfoViewList;
                customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
                for(CustomerInfoView customerInfoView : tmpCustomerInfoViewList){
                    for(CSIResult csiResult : csiResultList){
                        boolean checkPersonalId = false;
                        if(customerEntityId == 1){
                            if(csiResult.getIdNumber().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                checkPersonalId = true;
                            }
                        } else if(customerEntityId == 2){
                            if(csiResult.getIdNumber().equalsIgnoreCase(customerInfoView.getRegistrationId())){
                                checkPersonalId = true;
                            }
                        }

                        if(checkPersonalId){
                            if(csiResult.getActionResult() == ActionResult.SUCCESS){
                                customerInfoView.setCsiFlag(1);
                                if(customerInfoView.getNcbReason() != null){
                                    customerInfoView.setNcbReason(customerInfoView.getNcbReason().concat(", Check CSI SUCCESS."));
                                } else {
                                    customerInfoView.setNcbReason("Check CSI SUCCESS.");
                                }
                                customerInfoView.setCsiResult(ActionResult.SUCCESS.toString());
                            } else if (csiResult.getActionResult() == ActionResult.FAILED){
                                failedCount = failedCount + 1;
                                customerInfoView.setCsiFlag(0);
                                if(customerInfoView.getNcbReason() != null){
                                    customerInfoView.setNcbReason(customerInfoView.getNcbReason().concat(", Check CSI FAILED, ").concat(csiResult.getResultReason()));
                                } else {
                                    customerInfoView.setNcbReason("Check CSI FAILED, ".concat(csiResult.getResultReason()));
                                }
                                customerInfoView.setCsiResult(ActionResult.FAILED.toString());
                            }
                            customerInfoView.setCsiReason(csiResult.getResultReason());
                        }
                    }
                    customerInfoViewList.add(customerInfoView);
                }

                if(failedCount == 0){
                    if(success){
                        success = true;
                    }
                } else {
                    success = false;
                }

                prescreenBusinessControl.savePreScreenCheckerOnlyCSI(customerInfoViewList, customerEntityId, workCasePreScreenId);

                if(success){
                    RequestContext.getCurrentInstance().execute("commandSubmitCase()");
                }
            } else {
                log.debug("onCheckCSI ::: check CSI without NCB Data");
                int failedCount = 0;
                List<CSIResult> csiResultList = new ArrayList<CSIResult>();
                csiResultList = prescreenBusinessControl.getCSIDataWithOutNCB(customerInfoViewList, customerEntityId, userId, workCasePreScreenId);
                log.debug("onCheckCSI ::: check CSI without NCB Data : csiResultList : {}", csiResultList);

                List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
                tmpCustomerInfoViewList = customerInfoViewList;
                customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
                for(CustomerInfoView customerInfoView : tmpCustomerInfoViewList){
                    for(CSIResult csiResult : csiResultList){
                        boolean checkPersonalId = false;
                        if(customerEntityId == 1){
                            if(csiResult.getIdNumber().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                checkPersonalId = true;
                            }
                        } else if(customerEntityId == 2){
                            if(csiResult.getIdNumber().equalsIgnoreCase(customerInfoView.getRegistrationId())){
                                checkPersonalId = true;
                            }
                        }

                        if(checkPersonalId){
                            if(csiResult.getActionResult() == ActionResult.SUCCESS){
                                customerInfoView.setCsiFlag(1);
                                if(customerInfoView.getNcbReason() != null){
                                    customerInfoView.setNcbReason(customerInfoView.getNcbReason().concat(", Check CSI SUCCESS."));
                                } else {
                                    customerInfoView.setNcbReason("Check CSI SUCCESS.");
                                }
                                customerInfoView.setCsiResult(ActionResult.SUCCESS.toString());
                            } else if (csiResult.getActionResult() == ActionResult.FAILED){
                                failedCount = failedCount + 1;
                                customerInfoView.setCsiFlag(0);
                                if(customerInfoView.getNcbReason() != null){
                                    log.debug("CSI REASON : {}", csiResult.getResultReason());
                                    customerInfoView.setNcbReason(customerInfoView.getNcbReason().concat(", Check CSI FAILED, ").concat(csiResult.getResultReason()));
                                } else {
                                    customerInfoView.setNcbReason("Check CSI FAILED, ".concat(csiResult.getResultReason()));
                                }

                                customerInfoView.setCsiResult(ActionResult.FAILED.toString());
                            }
                            customerInfoView.setCsiReason(csiResult.getResultReason());
                        }
                    }
                    customerInfoViewList.add(customerInfoView);
                }

                for(CustomerInfoView customerInfoView : customerInfoViewList){
                    if(customerInfoView.getCsiFlag() == 0){
                        failedCount = failedCount + 1;
                    }
                }

                log.debug("onCheckCSI ::: failedCount : {}", failedCount);
                log.debug("onCheckCSI ::: success (old) : {}", success);

                if(failedCount == 0){
                    if(success){
                        success = true;
                    }
                } else {
                    success = false;
                }

                prescreenBusinessControl.savePreScreenCheckerOnlyCSI(customerInfoViewList, customerEntityId, workCasePreScreenId);

                log.debug("onCheckCSI ::: savePrescreenCheckerOnlyCSI Complete !! success (new) : {}", success);
                if(success){
                    RequestContext.getCurrentInstance().execute("commandSubmitCase()");
                }
            }
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Check CSI failed. " + Util.getMessageException(ex);
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("onCheckCSI ::: Exception : ", ex);
        }
    }

    //TODO Check PreScreen Before Submit Case
    public void onCheckingPreScreen(){

    }

    public void onCompleteChecker(){
        //TODO Show message box
        if(success){
            //TODO submit case
            try{
                log.debug("Submit case to Maker :::");
                prescreenBusinessControl.submitBDM(workCasePreScreenId, queueName, ActionCode.CHECK_NCB.getVal());
                messageHeader = "Information.";
                message = "Check NCB success. Click 'OK' return to inbox.";
                RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
            }catch (Exception ex){
                messageHeader = "Exception.";
                message = ex.getMessage();
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
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
