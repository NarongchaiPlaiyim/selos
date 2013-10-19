package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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

    @Inject
    BankStmtControl bankStmtControl;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    BankDAO bankDAO;

    //Passed variables from Bank Statement Summary
    private boolean isTmbBank;
    private int seasonal;
    private Date expectedSubmissionDate;

    private List<Bank> bankList;
    private List<AccountStatus> accountStatusList;

    public BankStatementDetail(){
    }

    @PostConstruct
    public void onCreation() {
        ExternalContext ec = FacesUtil.getExternalContext();
        Flash flash = ec.getFlash();
        Map<String, Object> bankStmtSumParameters = (Map<String, Object>) flash.get("bankStmtSumParameters");
        //Passed parameters from Bank Statement Summary
        if (bankStmtSumParameters != null) {
            isTmbBank = (Boolean) bankStmtSumParameters.get("isTmbBank");
            seasonal = (Integer) bankStmtSumParameters.get("seasonal");
            expectedSubmissionDate = (Date) bankStmtSumParameters.get("expectedSubmissionDate");

            log.debug("onCreation() Flash{seasonal: {}, expectedSubmissionDate: {}, isTmbBank: {}}",
                    seasonal, expectedSubmissionDate, isTmbBank);
        } else {
            try {
                ec.redirect("bankStatementSummary.jsf");
            } catch (IOException e) {
                log.error("redirect: bankStatementSummary failed!");
            }
        }

        //init select list
        bankList = new ArrayList<Bank>();
        if (isTmbBank) {
            bankList.add(bankDAO.getTMBBank());
        } else {
            bankList = bankDAO.getListExcludeTMB();
        }

        accountStatusList = accountStatusDAO.findAll();

    }

    public void onSave() {
        log.debug("onSave()");
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    public List<AccountStatus> getAccountStatusList() {
        return accountStatusList;
    }

    public void setAccountStatusList(List<AccountStatus> accountStatusList) {
        this.accountStatusList = accountStatusList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }
}
