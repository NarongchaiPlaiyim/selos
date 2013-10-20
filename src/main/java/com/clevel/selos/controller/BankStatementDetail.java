package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.RelationDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.AccountStatusTransform;
import com.clevel.selos.transform.AccountTypeTransform;
import com.clevel.selos.transform.BankTransform;
import com.clevel.selos.transform.RelationTransform;
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

    //Business logic
    @Inject
    BankStmtControl bankStmtControl;

    //DAO
    @Inject
    BankDAO bankDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    AccountTypeDAO accountTypeDAO;
    @Inject
    RelationDAO relationDAO;

    //Transform
    @Inject
    BankTransform bankTransform;
    @Inject
    AccountTypeTransform accountTypeTransform;
    @Inject
    AccountStatusTransform accountStatusTransform;
    @Inject
    RelationTransform relationTransform;

    //Parameters from Bank statement summary
    private boolean isTmbBank;
    private int seasonal;
    private Date expectedSubmissionDate;

    private BankStmtView bankStmtView;

    //Select items list
    private List<BankView> bankViewList;
    private List<AccountTypeView> accTypeViewList;
    private List<AccountTypeView> othAccTypeViewList;
    private List<AccountStatusView> accStatusViewList;
    private List<RelationView> relationViewList;

    public BankStatementDetail(){
    }

    @PostConstruct
    public void onCreation() {
        Flash flash = FacesUtil.getFlash();
        //Passed parameters from Bank statement summary page
        Map<String, Object> bankStmtSumParams = (Map<String, Object>) flash.get("bankStmtSumParams");
        if (bankStmtSumParams != null) {
            isTmbBank = (Boolean) bankStmtSumParams.get("isTmbBank");
            seasonal = (Integer) bankStmtSumParams.get("seasonal");
            expectedSubmissionDate = (Date) bankStmtSumParams.get("expectedSubmissionDate");

            log.debug("onCreation() bankStmtSumParams:{isTmbBank: {}, seasonal: {}, expectedSubmissionDate: {}}",
                    isTmbBank, seasonal, expectedSubmissionDate);
        } else {
            //Return to Bank statement summary if parameter is null
            FacesUtil.redirect("/site/bankStatementSummary.jsf");
            return;
        }

        bankStmtView = new BankStmtView();

        //init items list
        bankViewList = new ArrayList<BankView>();
        if (isTmbBank)
            bankViewList.add(bankTransform.getBankView(bankDAO.getTMBBank()));
        /*else
            bankViewList = bankTransform.getBankViewList(bankDAO.getListExcludeTMB());*/

        accTypeViewList = accountTypeTransform.transformToViewList(accountTypeDAO.findAll());
        othAccTypeViewList = accountTypeTransform.transformToViewList(accountTypeDAO.findAll());
        accStatusViewList = accountStatusTransform.transformToViewList(accountStatusDAO.findAll());
        relationViewList = relationTransform.transformToViewList(relationDAO.findAll());
    }

    public void onSave() {
        log.debug("onSave()");
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    public BankStmtView getBankStmtView() {
        return bankStmtView;
    }

    public void setBankStmtView(BankStmtView bankStmtView) {
        this.bankStmtView = bankStmtView;
    }

    public List<BankView> getBankViewList() {
        return bankViewList;
    }

    public void setBankViewList(List<BankView> bankViewList) {
        this.bankViewList = bankViewList;
    }

    public List<AccountTypeView> getAccTypeViewList() {
        return accTypeViewList;
    }

    public void setAccTypeViewList(List<AccountTypeView> accTypeViewList) {
        this.accTypeViewList = accTypeViewList;
    }

    public List<AccountTypeView> getOthAccTypeViewList() {
        return othAccTypeViewList;
    }

    public void setOthAccTypeViewList(List<AccountTypeView> othAccTypeViewList) {
        this.othAccTypeViewList = othAccTypeViewList;
    }

    public List<AccountStatusView> getAccStatusViewList() {
        return accStatusViewList;
    }

    public void setAccStatusViewList(List<AccountStatusView> accStatusViewList) {
        this.accStatusViewList = accStatusViewList;
    }

    public List<RelationView> getRelationViewList() {
        return relationViewList;
    }

    public void setRelationViewList(List<RelationView> relationViewList) {
        this.relationViewList = relationViewList;
    }
}
