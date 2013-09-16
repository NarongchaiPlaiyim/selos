package com.clevel.selos.controller;


import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.NcbRecordView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "fullappNcb")
public class FullappNcb implements Serializable {

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

    private List<SettlementStatus> settlementStatusList;
    private List<AccountStatus> accountStatusList;
    private List<AccountType> accountTypeList;
    private List<TDRCondition> tdrConditionList;

    private AccountType dlgAccountType;
    private AccountStatus dlgAccountStatus;
    private TDRCondition dlgCurrentPayment;
    private TDRCondition dlgHistoryPayment;

    private String modeForButton;
    private int rowIndex;

    //test
    private double monthly;
    private boolean genTextBoxFlag;
    //test
    private NcbRecordView ncbRecordView;
    private NcbRecordView selectNcbRecordItem;
    private List<NcbRecordView> ncbRecordViewList;

    @Inject
    private AccountStatusDAO accountStatusDAO;
    @Inject
    private AccountTypeDAO accountTypeDAO;
    @Inject
    private SettlementStatusDAO settlementStatusDAO;
    @Inject
    private TDRConditionDAO tdrConditionDAO;

    public FullappNcb() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";
        genTextBoxFlag=false;
        if (ncbRecordView == null) {
            ncbRecordView = new NcbRecordView();
        }

        if (ncbRecordViewList == null) {
            ncbRecordViewList = new ArrayList<NcbRecordView>();
        }

        if (accountStatusList == null) {
            accountStatusList = new ArrayList<AccountStatus>();
        }

        if (accountTypeList == null) {
            accountTypeList = new ArrayList<AccountType>();
        }

        if (settlementStatusList == null) {
            settlementStatusList = new ArrayList<SettlementStatus>();
        }

        if (tdrConditionList == null) {
            tdrConditionList = new ArrayList<TDRCondition>();
        }

        if (dlgAccountType == null) {
            dlgAccountType = new AccountType();
        }

        if (dlgAccountStatus == null) {
            dlgAccountStatus = new AccountStatus();
        }

        if (dlgCurrentPayment == null) {
            dlgCurrentPayment = new TDRCondition();
        }

        if (dlgHistoryPayment == null) {
            dlgHistoryPayment = new TDRCondition();
        }

        accountStatusList = accountStatusDAO.findAll();
        accountTypeList = accountTypeDAO.findAll();
        settlementStatusList = settlementStatusDAO.findAll();
        tdrConditionList = tdrConditionDAO.findAll();

    }


    // onclick add button
    public void onAddNcbRecord() {
        //*** Reset form ***//
        log.info("onAddNcbRecord ::: ");
        //*** Reset form ***//
        log.info("onAddNcbRecord ::: Reset Form");
        ncbRecordView = new NcbRecordView();
        ncbRecordView.setAccountStatus(new AccountStatus());
        ncbRecordView.setAccountType(new AccountType());
        ncbRecordView.setCurrentPayment(new TDRCondition());
        ncbRecordView.setHistoryPayment(new TDRCondition());
        modeForButton = "add";
        genTextBoxFlag = false;
    }

    public void onEditNcbDetail() {  //copy row that choose to dialog
        log.info("rowIndex {} ", rowIndex);
        modeForButton = "edit";
        log.info("onEditNcbDetail ::: selectNcbRecordItem  : {}", selectNcbRecordItem.toString());
        log.info("rowIndex :::   : {}", rowIndex);
        log.info("ncbRecordViewList.size() :::   : {}", ncbRecordViewList.size());

        if (rowIndex < ncbRecordViewList.size()) {
            ncbRecordView = new NcbRecordView();
            AccountType accountTypeEdit = selectNcbRecordItem.getAccountType();
            AccountStatus accountStatusEdit = selectNcbRecordItem.getAccountStatus();
            TDRCondition conditionCurrentEdit = selectNcbRecordItem.getCurrentPayment();
            TDRCondition conditionHistoryEdit = selectNcbRecordItem.getHistoryPayment();

            ncbRecordView.setAccountType(accountTypeEdit);
            ncbRecordView.setAccountStatus(accountStatusEdit);
            ncbRecordView.setCurrentPayment(conditionCurrentEdit);
            ncbRecordView.setHistoryPayment(conditionHistoryEdit);
            ncbRecordView.setTMBAccount(selectNcbRecordItem.getTMBAccount());
            ncbRecordView.setDateOfInfo(selectNcbRecordItem.getDateOfInfo());
            ncbRecordView.setAccountOpenDate(selectNcbRecordItem.getAccountOpenDate());
            ncbRecordView.setLimit(selectNcbRecordItem.getLimit());
            ncbRecordView.setOutstanding(selectNcbRecordItem.getOutstanding());
            ncbRecordView.setInstallment(selectNcbRecordItem.getInstallment());
            ncbRecordView.setDateOfDebtRestructuring(selectNcbRecordItem.getDateOfDebtRestructuring());
            ncbRecordView.setNoOfOutstandingPaymentIn12months(selectNcbRecordItem.getNoOfOutstandingPaymentIn12months());
            ncbRecordView.setNoOfOverLimit(selectNcbRecordItem.getNoOfOverLimit());
            ncbRecordView.setRefinanceFlag(selectNcbRecordItem.getRefinanceFlag());
            ncbRecordView.setNoOfmonthsPayment(selectNcbRecordItem.getNoOfmonthsPayment());
        }
    }

    public void onDeleteNcbDetail() {
        log.info("onDeleteNcbDetail ::: selectNcbRecordItem: {}", selectNcbRecordItem);
        ncbRecordViewList.remove(selectNcbRecordItem);
    }

    public void onSaveNcbRecord() {
        log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("  dlgAccountType : {}", dlgAccountType.getId());
        log.info("  dlgAccountStatus :  {}", dlgAccountStatus.getId());
        log.info("  dlgCurrentPayment :  {}", dlgCurrentPayment.getId());
        log.info("  dlgHistoryPayment :  {}", dlgHistoryPayment.getId());


        if (dlgAccountType.getId() != 0 && dlgAccountStatus.getId() != 0 && dlgCurrentPayment.getId() != 0 && dlgHistoryPayment.getId() != 0) {
            if (modeForButton != null && modeForButton.equalsIgnoreCase("add")) {

                AccountType accountType = accountTypeDAO.findById(dlgAccountType.getId());
                AccountStatus accountStatus = accountStatusDAO.findById(dlgAccountStatus.getId());
                TDRCondition tdrConditionCurrent = tdrConditionDAO.findById(dlgCurrentPayment.getId());
                TDRCondition tdrConditionHistory = tdrConditionDAO.findById(dlgHistoryPayment.getId());

                NcbRecordView ncbAdd = new NcbRecordView();
                ncbAdd.setAccountType(accountType);
                ncbAdd.setAccountStatus(accountStatus);
                ncbAdd.setTMBAccount(ncbRecordView.getTMBAccount());
                ncbAdd.setDateOfInfo(ncbRecordView.getDateOfInfo());
                ncbAdd.setAccountOpenDate(ncbRecordView.getAccountOpenDate());
                ncbAdd.setLimit(ncbRecordView.getLimit());
                ncbAdd.setOutstanding(ncbRecordView.getOutstanding());
                ncbAdd.setInstallment(ncbRecordView.getInstallment());
                ncbAdd.setDateOfDebtRestructuring(ncbRecordView.getDateOfDebtRestructuring());
                ncbAdd.setCurrentPayment(tdrConditionCurrent);
                ncbAdd.setHistoryPayment(tdrConditionHistory);
                ncbAdd.setNoOfOutstandingPaymentIn12months(ncbRecordView.getNoOfOutstandingPaymentIn12months());
                ncbAdd.setNoOfOverLimit(ncbRecordView.getNoOfOverLimit());
                ncbAdd.setRefinanceFlag(ncbRecordView.getRefinanceFlag());
                ncbAdd.setNoOfmonthsPayment(ncbRecordView.getNoOfmonthsPayment());

                log.info("ncbRecordView.getNoOfmonthsPayment   >> " + ncbRecordView.getNoOfmonthsPayment());

                if ((ncbRecordView.getNoOfmonthsPayment() != 0)) {
                    ncbAdd.setMonthsPaymentFlag(true);
                    monthly = 1000.00;
                } else {
                    ncbAdd.setMonthsPaymentFlag(false);
                }
                log.info("ncbAdd.isMonthsPaymentFlag   {} ", ncbAdd.isMonthsPaymentFlag());

                ncbRecordViewList.add(ncbAdd);
                log.info("add finish");

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(dlgAccountType.getId());
                AccountStatus accountStatus = accountStatusDAO.findById(dlgAccountStatus.getId());
                TDRCondition tdrConditionCurrent = tdrConditionDAO.findById(dlgCurrentPayment.getId());
                TDRCondition tdrConditionHistory = tdrConditionDAO.findById(dlgHistoryPayment.getId());

                ncbRecordViewList.get(rowIndex).setAccountType(accountType);
                ncbRecordViewList.get(rowIndex).setAccountStatus(accountStatus);
                ncbRecordViewList.get(rowIndex).setCurrentPayment(tdrConditionCurrent);
                ncbRecordViewList.get(rowIndex).setHistoryPayment(tdrConditionHistory);
                ncbRecordViewList.get(rowIndex).setTMBAccount(ncbRecordView.getTMBAccount());
//                log.info("onsave mode edit :: ncbRecordView.getDateOfInfo() {} " , ncbRecordView.getDateOfInfo());
                ncbRecordViewList.get(rowIndex).setDateOfInfo(ncbRecordView.getDateOfInfo());
                ncbRecordViewList.get(rowIndex).setAccountOpenDate(ncbRecordView.getAccountOpenDate());
                ncbRecordViewList.get(rowIndex).setLimit(ncbRecordView.getLimit());
                ncbRecordViewList.get(rowIndex).setOutstanding(ncbRecordView.getOutstanding());
                ncbRecordViewList.get(rowIndex).setInstallment(ncbRecordView.getInstallment());
                ncbRecordViewList.get(rowIndex).setDateOfDebtRestructuring(ncbRecordView.getDateOfDebtRestructuring());
                ncbRecordViewList.get(rowIndex).setNoOfOutstandingPaymentIn12months(ncbRecordView.getNoOfOutstandingPaymentIn12months());
                ncbRecordViewList.get(rowIndex).setNoOfOverLimit(ncbRecordView.getNoOfOverLimit());
                ncbRecordViewList.get(rowIndex).setRefinanceFlag(ncbRecordView.getRefinanceFlag());
                ncbRecordViewList.get(rowIndex).setNoOfmonthsPayment(ncbRecordView.getNoOfmonthsPayment());

                if ((ncbRecordView.getNoOfmonthsPayment() != 0)) {
                    ncbRecordViewList.get(rowIndex).setMonthsPaymentFlag(true);
                    monthly = 1000.00;
                } else {
                    ncbRecordViewList.get(rowIndex).setMonthsPaymentFlag(false);
                }

            } else {
                log.info("onSaveNcbRecord ::: Undefined modeForbutton !!");
            }

            complete = true;

        } else {

            log.info("onSaveNcbRecord ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);

        context.addCallbackParam("functionComplete", complete);

    }

    public void onblurToGenMoneyOfmonthsPayment(){

//        log.info("  ncbRecordView.getNoOfmonthsPayment() >>>>  :  {}", ncbRecordView.getNoOfmonthsPayment());
        log.info("  ncbRecordView.getNoOfmonthsPaymentList() >>>>  :  {}", ncbRecordView.getNoOfmonthsPaymentList());


//        if(ncbRecordView.getNoOfmonthsPayment() > 0){
//            genTextBoxFlag=true;
//        }
//        else{
//            genTextBoxFlag=false;
//        }
    }

    public boolean isGenTextBoxFlag() {
        return genTextBoxFlag;
    }

    public void setGenTextBoxFlag(boolean genTextBoxFlag) {
        this.genTextBoxFlag = genTextBoxFlag;
    }

    public double getMonthly() {
        return monthly;
    }

    public void setMonthly(double monthly) {
        this.monthly = monthly;
    }

    public List<AccountStatus> getAccountStatusList() {
        return accountStatusList;
    }

    public void setAccountStatusList(List<AccountStatus> accountStatusList) {
        this.accountStatusList = accountStatusList;
    }

    public List<AccountType> getAccountTypeList() {
        return accountTypeList;
    }

    public void setAccountTypeList(List<AccountType> accountTypeList) {
        this.accountTypeList = accountTypeList;
    }

    public NcbRecordView getNcbRecordView() {
        return ncbRecordView;
    }

    public void setNcbRecordView(NcbRecordView ncbRecordView) {
        this.ncbRecordView = ncbRecordView;
    }

    public List<NcbRecordView> getNcbRecordViewList() {
        return ncbRecordViewList;
    }

    public void setNcbRecordViewList(List<NcbRecordView> ncbRecordViewList) {
        this.ncbRecordViewList = ncbRecordViewList;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(String modeForButton) {
        this.modeForButton = modeForButton;
    }

    public List<SettlementStatus> getSettlementStatusList() {
        return settlementStatusList;
    }

    public void setSettlementStatusList(List<SettlementStatus> settlementStatusList) {
        this.settlementStatusList = settlementStatusList;
    }

    public List<TDRCondition> getTdrConditionList() {
        return tdrConditionList;
    }

    public void setTdrConditionList(List<TDRCondition> tdrConditionList) {
        this.tdrConditionList = tdrConditionList;
    }

    public AccountType getDlgAccountType() {
        return dlgAccountType;
    }

    public void setDlgAccountType(AccountType dlgAccountType) {
        this.dlgAccountType = dlgAccountType;
    }

    public AccountStatus getDlgAccountStatus() {
        return dlgAccountStatus;
    }

    public void setDlgAccountStatus(AccountStatus dlgAccountStatus) {
        this.dlgAccountStatus = dlgAccountStatus;
    }

    public TDRCondition getDlgCurrentPayment() {
        return dlgCurrentPayment;
    }

    public void setDlgCurrentPayment(TDRCondition dlgCurrentPayment) {
        this.dlgCurrentPayment = dlgCurrentPayment;
    }

    public TDRCondition getDlgHistoryPayment() {
        return dlgHistoryPayment;
    }

    public void setDlgHistoryPayment(TDRCondition dlgHistoryPayment) {
        this.dlgHistoryPayment = dlgHistoryPayment;
    }

    public NcbRecordView getSelectNcbRecordItem() {
        return selectNcbRecordItem;
    }

    public void setSelectNcbRecordItem(NcbRecordView selectNcbRecordItem) {
        this.selectNcbRecordItem = selectNcbRecordItem;
    }
}
