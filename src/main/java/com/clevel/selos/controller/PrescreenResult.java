package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.CaseRequestTypes;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.PrescreenResultView;
import com.clevel.selos.model.view.PrescreenView;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenResult")
public class PrescreenResult extends BaseController {
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
    private WorkCaseDAO workCaseDAO;

    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;


    @Inject
    PrescreenBusinessControl prescreenBusinessControl;

    enum ModeForButton {ADD, EDIT, DELETE}

    private long workCasePreScreenId;
    private long stepId;
    private User user;

    private String message;
    private String messageHeader;

    private PrescreenResultView prescreenResultView;
    private PrescreenView prescreenView;

    public PrescreenResult() {

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        log.info("preRender ::: setSession ");
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);

        if (Util.isZero(workCasePreScreenId)){
            /*stepId = Long.parseLong(session.getAttribute("stepId").toString());
            if (stepId != StepValue.PRESCREEN_MAKER.value()) {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }*/
            long workCaseId = getCurrentWorkCaseId(session);
            if(Util.isZero(workCaseId)) {
                log.info("preRender ::: workCasePrescreenId is null.");
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("PrescreenResult ::: onCreation");
        HttpSession session = FacesUtil.getSession(false);
        int requestType = 0;
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        if(Util.isZero(workCasePreScreenId)){
            long workCaseId = getCurrentWorkCaseId(session);
            if(!Util.isZero(workCaseId)){
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                if(!Util.isNull(workCase)){
                    workCasePreScreenId = workCase.getWorkCasePrescreen() != null ? workCase.getWorkCasePrescreen().getId() : 0;
                }
            }else{
                log.error("No workCaseId and workCasePreScreenId");
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
        }
        if (!Util.isZero(workCasePreScreenId)) {
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.info("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);
            user = (User) session.getAttribute("user");

            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(!Util.isNull(workCasePrescreen))
                requestType = workCasePrescreen.getRequestType() != null ? workCasePrescreen.getRequestType().getId() : 0;

            prescreenResultView = prescreenBusinessControl.getPrescreenResult(workCasePreScreenId);
            prescreenView = prescreenBusinessControl.getPreScreen(workCasePreScreenId);
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            if(requestType == CaseRequestTypes.NEW_CASE.value())
                loadFieldControlPreScreen(workCasePreScreenId, Screen.PRESCREEN_RESULT, ownerCaseUserId);
        }
    }

    public void onRetrieveInterfaceInfo() {
        log.info("Start on Retrieve Interface Info");

        List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        if(customerInfoViews != null){
            customerInfoViewList = generateCustomerInfoList(customerInfoViews);
        }
        HttpSession session = FacesUtil.getSession(false);
        try{
            long workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
            prescreenResultView = prescreenBusinessControl.getInterfaceInfo(customerInfoViewList, prescreenResultView, workCasePreScreenId);
            messageHeader = "Information.";
            message = "Retrieve interface info success.";
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            log.error("exception while retrieve interface info : ", ex);
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageResultDlg.show()");
    }

    public List<CustomerInfoView> generateCustomerInfoList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoList = new ArrayList<CustomerInfoView>();
        for(CustomerInfoView item : customerInfoViews){
            customerInfoList.add(item);
            if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse = item.getSpouse();
                    if(spouse != null){
                        customerInfoList.add(spouse);
                    }
                }
            }
        }
        return customerInfoList;
    }

    public void onSave() {
        log.info("Start onSave {}", prescreenResultView);
        prescreenBusinessControl.savePrescreenResult(prescreenResultView, workCasePreScreenId, user);

        messageHeader = "Save PreScreen Result Success.";
        message = "Save PreScreen Result data success.";

        onCreation();
        RequestContext.getCurrentInstance().execute("msgBoxRefreshDlg.show()");
    }

    public void onCloseSale() {

    }

    public PrescreenResultView getPrescreenResultView() {
        return prescreenResultView;
    }

    public void setPrescreenResultView(PrescreenResultView prescreenResultView) {
        this.prescreenResultView = prescreenResultView;
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

    public PrescreenView getPrescreenView() {
        return prescreenView;
    }

    public void setPrescreenView(PrescreenView prescreenView) {
        this.prescreenView = prescreenView;
    }
}
