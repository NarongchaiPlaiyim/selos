package com.clevel.selos.controller;

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
import javax.faces.context.Flash;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "bankStatementDetail")
public class BankStatementDetail implements Serializable {
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

    public BankStatementDetail(){
    }

    @PostConstruct
    public void onCreation() {
        ExternalContext ec = FacesUtil.getExternalContext();
        Flash flash = ec.getFlash();
        Map<String, Object> bankStmtSumParameters = (Map<String, Object>) flash.get("bankStmtSumParameters");
        //Passed parameters from Bank Statement Summary
        if (bankStmtSumParameters != null) {
            boolean isTmbBank = (Boolean) bankStmtSumParameters.get("isTmbBank");
            int seasonal = (Integer) bankStmtSumParameters.get("seasonal");
            Date expectedSubmissionDate = (Date) bankStmtSumParameters.get("expectedSubmissionDate");

            log.debug("onCreation() Flash{seasonal: {}, expectedSubmissionDate: {}, isTmbBank: {}}",
                    seasonal, expectedSubmissionDate, isTmbBank);
        } else {
            try {
                ec.redirect("bankStatementSummary.jsf");
            } catch (IOException e) {
                log.error("redirect: bankStatementSummary failed!");
            }
        }
    }

    public void onSave() {
        log.debug("onSave()");
    }

    public void onCancel() {
        log.debug("onCancel()");
    }
}
