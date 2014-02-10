package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.User;
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
public class PrescreenResult implements Serializable {
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
    PrescreenBusinessControl prescreenBusinessControl;

    enum ModeForButton {ADD, EDIT, DELETE}

    private ModeForButton modeForButton;
    private long workCasePreScreenId;
    private long stepId;
    private String queueName;
    private User user;

    private String message;
    private String messageHeader;

    private PrescreenResultView prescreenResultView;
    private PrescreenView prescreenView;

    public PrescreenResult() {

    }

    public void preRender() {
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

        if (session.getAttribute("workCasePreScreenId") != null) {
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());

            if (stepId != 1003) {
                try {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                    return;
                } catch (Exception ex) {
                    log.info("Exception :: {}", ex);
                }
            }
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCasePrescreenId is null.");
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
    public void onCreation() {
        log.info("PrescreenResult ::: onCreation");
        HttpSession session = FacesUtil.getSession(true);

        if (session.getAttribute("workCasePreScreenId") != null) {
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.info("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();
            user = (User) session.getAttribute("user");

            prescreenResultView = prescreenBusinessControl.getPrescreenResult(workCasePreScreenId);
            prescreenView = prescreenBusinessControl.getPreScreen(workCasePreScreenId);
        }
    }

    public void onRetrieveInterfaceInfo() {
        log.info("Start on Retrieve Interface Info");

        List<CustomerInfoView> customerInfoViews = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        if(customerInfoViews != null){
            customerInfoViewList = generateCustomerInfoList(customerInfoViews);
        }
        try{
            prescreenResultView = prescreenBusinessControl.getInterfaceInfo(customerInfoViewList, prescreenResultView);
            messageHeader = "Information.";
            message = "Retrieve interface info success.";
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
        }

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
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
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
