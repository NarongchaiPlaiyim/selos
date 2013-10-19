package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.PrescreenResultView;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenResult")
public class PrescreenResult implements Serializable {
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
    PrescreenBusinessControl prescreenBusinessControl;

    enum ModeForButton{ ADD, EDIT, DELETE }
    private ModeForButton modeForButton;
    private long workCasePreScreenId;
    private long stepId;
    private String queueName;
    private User user;

    private PrescreenResultView prescreenResultView;

    public PrescreenResult(){

    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

        if(session.getAttribute("workCasePreScreenId") != null){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());

            if(stepId != 1003){
                try{
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                    return;
                }catch (Exception ex){
                    log.info("Exception :: {}",ex);
                }
            }
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCasePrescreenId is null.");
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
        log.info("PrescreenResult ::: onCreation");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.info("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();
            user = (User)session.getAttribute("user");

            modeForButton = ModeForButton.ADD;
        }
    }

    public void onRetrieveInterfaceInfo(){
        log.info("Start on Retrieve Interface Info");
        prescreenResultView = prescreenBusinessControl.getInterfaceInfo(workCasePreScreenId);

    }

    private List<ExistingCreditDetailView> getCreditFact(int num){

        List<ExistingCreditDetailView> existingCreditViewList = new ArrayList<ExistingCreditDetailView>();
        for(int i = 0; i < num; i++){
            ExistingCreditDetailView existingCreditView = new ExistingCreditDetailView();
            existingCreditView.setAccountName("Test 0" + (i+1));
            existingCreditView.setAccountNumber("123456789" + i);
            existingCreditView.setAccountSuf("00"+(i+1));
            existingCreditView.setAccountStatusID(1);
            existingCreditView.setAccountStatus("Normal");
            existingCreditView.setCreditType("Loan");
            existingCreditView.setId(1);
            existingCreditView.setInstallment(new BigDecimal(10000 + ((i+1)*5000)));
            existingCreditView.setIntFeePercent(new BigDecimal(1));
            existingCreditView.setLimit(new BigDecimal(10000000 + ((i+1)*5000000)));
            existingCreditView.setOutstanding(new BigDecimal(8000000 + ((i+1)*500000)));
            existingCreditView.setProductCode("EAC1");
            existingCreditView.setProjectCode("1000" + i);
            existingCreditView.setProductProgram("SME Smart Biz");
            existingCreditView.setTenor(24 + ((i+1)*12));

            existingCreditViewList.add(existingCreditView);
        }
        return existingCreditViewList;
    }

    public void onCloseSale(){

    }

    public PrescreenResultView getPrescreenResultView() {
        return prescreenResultView;
    }

    public void setPrescreenResultView(PrescreenResultView prescreenResultView) {
        this.prescreenResultView = prescreenResultView;
    }
}
