package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.MortgageSummaryControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "mortDetail")
public class MortgageDetail implements Serializable {
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
    MortgageSummaryControl mortgageSummaryControl;

    //session
    private long workCaseId;

    private MortgageSummaryView mortgageSummaryView;

    private MortgageSummaryView mortgageDetailView;

    public MortgageDetail(){
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

        mortgageSummaryView = mortgageSummaryControl.getMortgageSummaryViewByWorkCaseId(workCaseId);
    }

    public void onSave() {

    }

    public MortgageSummaryView getMortgageSummaryView() {
        return mortgageSummaryView;
    }

    public void setMortgageSummaryView(MortgageSummaryView mortgageSummaryView) {
        this.mortgageSummaryView = mortgageSummaryView;
    }

    public MortgageSummaryView getMortgageDetailView() {
        return mortgageDetailView;
    }

    public void setMortgageDetailView(MortgageSummaryView mortgageDetailView) {
        this.mortgageDetailView = mortgageDetailView;
    }
}
