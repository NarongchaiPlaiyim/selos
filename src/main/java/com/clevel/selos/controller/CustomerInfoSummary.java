package com.clevel.selos.controller;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

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

    //*** Drop down List ***//
    private List<CustomerEntity> customerEntityList;

    //*** View ***//
    private CustomerInfoSummaryView customerInfoSummaryView;

    //Dialog
//    private BasicInfoAccountView basicInfoAccountView;
//    enum ModeForButton{ ADD, EDIT }
//    private ModeForButton modeForButton;
//    private BasicInfoAccountView selectAccount;
//    private int rowIndex;

    private String messageHeader;
    private String message;

    //session
    private long workCaseId;
    private long stepId;
    private String userId;

    public CustomerInfoSummary(){
    }

    public void preRender(){
        /*HttpSession session = FacesUtil.getSession(false);
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
        }*/
    }


    @PostConstruct
    public void onCreation() {
        preRender();

        customerEntityList = customerEntityDAO.findAll();

        customerInfoSummaryView = new CustomerInfoSummaryView();
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
}
