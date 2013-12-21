package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.LoanPaymentDetailView;
import com.clevel.selos.model.view.PledgeACView;
import com.clevel.selos.model.view.PledgeDetailView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.filenet.apiimpl.wsi.serialization.Serialization;
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
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "pledgeDetail")
public class PledgeDetail implements Serializable {
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

    // message //
    private String messageHeader;
    private String message;

    // content //
    private PledgeDetailView pledgeDetailView;
    private PledgeACView pledgeACView;
    private int rowIndex;

    //session
    private long workCaseId;


    public PledgeDetail(){}

    public void preRender() {
        log.info("preRender ::: setSession ");

        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//            stepId = Long.parseLong(session.getAttribute("stepId").toString());
//            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
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
    public void onCreation(){
        pledgeDetailView = new PledgeDetailView();
        pledgeACView = new PledgeACView();
        try {
            List<PledgeACView> pledgeACViews = new ArrayList<PledgeACView>();
            for(int i =0;i<5;i++){
                PledgeACView model = new PledgeACView();
                model.setAcAccountNumber("0123456789");
                model.setAcHoldAmount(BigDecimal.valueOf(i));
                model.setAcNumberofDep(String.valueOf(i));
                pledgeACViews.add(model);

            }
            pledgeDetailView.setPledgeACViews(pledgeACViews);

        }catch (Exception e){

        }
    }

    public void onAddPledgeAC(){
        log.debug("onAdd seleteItem pledgeACView :{} ", pledgeACView);
        pledgeDetailView.getPledgeACViews().add(pledgeACView);

    }

    public void onEditPledgeAC(){
        log.debug("onEditPledgeAC rowIndex:{}", rowIndex);
        pledgeACView = pledgeDetailView.getPledgeACViews().get(rowIndex);

    }

    public void onDeletePledgeAC(){
        log.debug("onDeletePledgeAC rowIndex:{}", rowIndex);
        pledgeDetailView.getPledgeACViews().remove(rowIndex);
    }

    public BigDecimal getTotalHoldAmount(){
        BigDecimal result = BigDecimal.ZERO;
        for(PledgeACView model : pledgeDetailView.getPledgeACViews()){
            result = Util.add(result, model.getAcHoldAmount());
        }
        return result;
    }

    public String getCurrentDateDDMMYY(){
        return  DateTimeUtil.convertToStringDDMMYYYY(new Date());
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

    public PledgeDetailView getPledgeDetailView() {
        return pledgeDetailView;
    }

    public void setPledgeDetailView(PledgeDetailView pledgeDetailView) {
        this.pledgeDetailView = pledgeDetailView;
    }


    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public PledgeACView getPledgeACView() {
        return pledgeACView;
    }

    public void setPledgeACView(PledgeACView pledgeACView) {
        this.pledgeACView = pledgeACView;
    }
}
