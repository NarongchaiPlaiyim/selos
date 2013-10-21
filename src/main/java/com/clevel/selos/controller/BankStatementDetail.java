package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
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
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    RelationDAO relationDAO;

    //Transform
    @Inject
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccTypeTransform;
    @Inject
    BankAccountStatusTransform bankAccStatusTransform;
    @Inject
    RelationTransform relationTransform;

    //Parameters from Bank statement summary
    private boolean isTmbBank;
    private int seasonal;
    private Date expectedSubmissionDate;

    //View form
    private BankStmtView bankStmtView;
    private int numberOfPrevMonth;

    //Select items list
    private List<BankView> bankViewList;
    private List<BankAccountTypeView> bankAccTypeViewList;
    private List<BankAccountTypeView> othBankAccTypeViewList;
    private List<AccountStatusView> accStatusViewList;
    private List<RelationView> relationViewList;

    public BankStatementDetail(){
    }

    @PostConstruct
    public void onCreation() {
        preRender();

        bankStmtView = new BankStmtView();
        bankStmtView.setBankStmtDetailViewList(initDetailList());

        //init items list
        bankViewList = new ArrayList<BankView>();
        if (isTmbBank)
            bankViewList.add(bankTransform.getBankView(bankDAO.getTMBBank()));
        else
            bankViewList = bankTransform.getBankViewList(bankDAO.getListExcludeTMB());

        bankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        othBankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        //todo: change accStatusViewList to bankAccountStatusViewList
//        accStatusViewList = bankAccStatusTransform.transformToViewList(accountStatusDAO.findAll());
        relationViewList = relationTransform.transformToViewList(relationDAO.findAll());
    }

    public void onSave() {
        log.debug("onSave() bankStmtView: {}", bankStmtView);
        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();
        for (BankStmtDetailView bStmtDetailView : bankStmtDetailViewList) {
            log.debug("bStmtDetailView: {}", bStmtDetailView);
        }
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    //Private method
    private void preRender() {
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
        }
    }

    private List<BankStmtDetailView> initDetailList() {
        List<BankStmtDetailView> bankStmtDetailViewList;

        Date startBankStmtDate = bankStmtControl.getStartBankStmtDate(expectedSubmissionDate);
        numberOfPrevMonth = bankStmtControl.getRetrieveMonthBankStmt(seasonal);
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>(numberOfPrevMonth);
        Date date;
        for (int i=0; i<numberOfPrevMonth; i++) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(startBankStmtDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailViewList.add(bankStmtDetailView);
        }

        //Sorting asOfDate
        Collections.sort(bankStmtDetailViewList, new Comparator<BankStmtDetailView>() {
            public int compare(BankStmtDetailView o1, BankStmtDetailView o2) {
                if (o1.getAsOfDate() == null || o2.getAsOfDate() == null)
                    return 0;
                return o1.getAsOfDate().compareTo(o2.getAsOfDate());
            }
        });
        return bankStmtDetailViewList;
    }

    //-------------------- Getter/Setter --------------------
    public BankStmtView getBankStmtView() {
        return bankStmtView;
    }

    public void setBankStmtView(BankStmtView bankStmtView) {
        this.bankStmtView = bankStmtView;
    }

    public int getNumberOfPrevMonth() {
        return numberOfPrevMonth;
    }

    public void setNumberOfPrevMonth(int numberOfPrevMonth) {
        this.numberOfPrevMonth = numberOfPrevMonth;
    }

    public List<BankView> getBankViewList() {
        return bankViewList;
    }

    public void setBankViewList(List<BankView> bankViewList) {
        this.bankViewList = bankViewList;
    }

    public List<BankAccountTypeView> getBankAccTypeViewList() {
        return bankAccTypeViewList;
    }

    public void setBankAccTypeViewList(List<BankAccountTypeView> bankAccTypeViewList) {
        this.bankAccTypeViewList = bankAccTypeViewList;
    }

    public List<BankAccountTypeView> getOthBankAccTypeViewList() {
        return othBankAccTypeViewList;
    }

    public void setOthBankAccTypeViewList(List<BankAccountTypeView> othBankAccTypeViewList) {
        this.othBankAccTypeViewList = othBankAccTypeViewList;
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
