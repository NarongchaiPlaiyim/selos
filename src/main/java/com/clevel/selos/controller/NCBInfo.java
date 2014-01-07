package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Month;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "ncbInfo")
public class NCBInfo implements Serializable {

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

    private List<SettlementStatus> settlementStatusList;
    private List<AccountStatus> accountStatusList;
    private List<AccountType> accountTypeList;
    private List<TDRCondition> tdrConditionList;

    private AccountType dlgAccountType;
    private AccountStatus dlgAccountStatus;
    private SettlementStatus dlgCurrentPayment;
    private SettlementStatus dlgHistoryPayment;
    private TDRCondition tdrCondition;

    private String modeForButton;
    private int rowIndex;
    private boolean monthRender1;
    private boolean monthRender2;
    private boolean monthRender3;
    private boolean monthRender4;
    private boolean monthRender5;
    private boolean monthRender6;

    private boolean genTextBoxFlag;
    private boolean noOfmonthsPaymentFlag;

    private NCBDetailView ncbDetailView;
    private NCBDetailView selectNcbRecordItem;
    private List<NCBDetailView> ncbDetailViewList;
    private NCBInfoView ncbInfoView;
    private String messageHeader;
    private String message;
    private boolean messageErr;
    private Long customerId;
    private User user;

    private boolean nplRendered;
    private boolean tdrRendered;
    private List<String> yearList;

    @Inject
    private AccountStatusDAO accountStatusDAO;
    @Inject
    private AccountTypeDAO accountTypeDAO;
    @Inject
    private SettlementStatusDAO settlementStatusDAO;
    @Inject
    private TDRConditionDAO tdrConditionDAO;
    @Inject
    NCBInfoControl ncbInfoControl;
    @Inject
    private UserDAO userDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private NCBDAO ncbDAO;


    public NCBInfo() {}

    public Month[] getStatMonths(){
        return Month.values();
    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";
        genTextBoxFlag = false;
        noOfmonthsPaymentFlag = false;

        HttpSession session = FacesUtil.getSession(true);
        user = (User) session.getAttribute("user");

        if (session.getAttribute("customerId") != null) {
            customerId = Long.parseLong(session.getAttribute("customerId").toString());
            log.info("customerId :: {} ", customerId);

            ncbInfoView = ncbInfoControl.getNCBInfoView(customerId); // find NCB by customer

            if (ncbInfoView != null) {
                ncbDetailViewList = ncbInfoControl.getNcbDetailListView(ncbInfoView);
                log.info("ncbDetailViewList  :::::::::::: {} ", ncbDetailViewList);
                toControlNplFlagRendered();
                toControlTdrFlagRendered();
            }
        }else{
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/NCBSummary.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }

        if (ncbDetailView == null) {
            ncbDetailView = new NCBDetailView();
        }

        if (ncbDetailViewList == null) {
            ncbDetailViewList = new ArrayList<NCBDetailView>();
        }

        if (ncbInfoView == null) {
            ncbInfoView = new NCBInfoView();
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
            dlgCurrentPayment = new SettlementStatus();
        }

        if (dlgHistoryPayment == null) {
            dlgHistoryPayment = new SettlementStatus();
        }

        if (tdrCondition == null) {
            tdrCondition = new TDRCondition();
        }


        accountStatusList = accountStatusDAO.findAll();
        accountTypeList = accountTypeDAO.findAll();
        settlementStatusList = settlementStatusDAO.findAll();
        tdrConditionList = tdrConditionDAO.findAll();


        monthRender1 = false;
        monthRender2 = false;
        monthRender3 = false;
        monthRender4 = false;
        monthRender5 = false;
        monthRender6 = false;

        ncbDetailView.reset();

        yearList = DateTimeUtil.getPreviousHundredYearTH();

    }

    public void toControlNplFlagRendered(){
        log.info("nplFlag :: {}", ncbInfoView.getNplFlag());

        if(ncbInfoView.getNplFlag() == 2){
            nplRendered = true;
        }else{
            nplRendered = false;
        }

    }

    public void toControlTdrFlagRendered(){
        log.info("tdrFlag :: {}", ncbInfoView.getTdrFlag());

        if(ncbInfoView.getTdrFlag() == 2){
            tdrRendered = true;
        }else{
            tdrRendered = false;
        }

    }

    // onclick add button
    public void onAddNcbRecord() {
        //*** Reset form ***//
        log.info("onAddNcbRecord ::: Reset Form");
        ncbDetailView = new NCBDetailView();
        ncbDetailView.reset();
        modeForButton = "add";
        genTextBoxFlag = false;

    }

    public void onEditNcbDetail() {  //copy row that choose to dialog
        log.info("rowIndex {} ", rowIndex);
        modeForButton = "edit";
        log.info("onEditNcbDetail ::: selectNcbRecordItem  : {}", selectNcbRecordItem.toString());
        log.info("rowIndex :::   : {}", rowIndex);
        log.info("ncbDetailViewList.size() :::   : {}", ncbDetailViewList.size());

        if (rowIndex < ncbDetailViewList.size()) {
            ncbDetailView = new NCBDetailView();
            AccountType accountTypeEdit = selectNcbRecordItem.getAccountType();
            AccountStatus accountStatusEdit = selectNcbRecordItem.getAccountStatus();
            SettlementStatus conditionCurrentEdit = selectNcbRecordItem.getCurrentPayment();
            SettlementStatus conditionHistoryEdit = selectNcbRecordItem.getHistoryPayment();

            //checkbox
            ncbDetailView.setTMB(selectNcbRecordItem.isTMB());
            ncbDetailView.setRefinance(selectNcbRecordItem.isRefinance());
            ncbDetailView.setWc(selectNcbRecordItem.isWc());

            ncbDetailView.setAccountType(accountTypeEdit);
            ncbDetailView.setAccountStatus(accountStatusEdit);
            ncbDetailView.setCurrentPayment(conditionCurrentEdit);
            ncbDetailView.setHistoryPayment(conditionHistoryEdit);
            ncbDetailView.setDateOfInfo(selectNcbRecordItem.getDateOfInfo());
            ncbDetailView.setAccountOpenDate(selectNcbRecordItem.getAccountOpenDate());
            ncbDetailView.setLimit(selectNcbRecordItem.getLimit());
            ncbDetailView.setOutstanding(selectNcbRecordItem.getOutstanding());
            ncbDetailView.setInstallment(selectNcbRecordItem.getInstallment());
            ncbDetailView.setDateOfDebtRestructuring(selectNcbRecordItem.getDateOfDebtRestructuring());
            ncbDetailView.setNoOfOutstandingPaymentIn12months(selectNcbRecordItem.getNoOfOutstandingPaymentIn12months());
            ncbDetailView.setNoOfOverLimit(selectNcbRecordItem.getNoOfOverLimit());
            ncbDetailView.setNoOfmonthsPayment(selectNcbRecordItem.getNoOfmonthsPayment());
//            ncbDetailView.setMonth1(selectNcbRecordItem.getMonth1());
//            ncbDetailView.setMonth2(selectNcbRecordItem.getMonth2());
//            ncbDetailView.setMonth3(selectNcbRecordItem.getMonth3());
//            ncbDetailView.setMonth4(selectNcbRecordItem.getMonth4());
//            ncbDetailView.setMonth5(selectNcbRecordItem.getMonth5());
//            ncbDetailView.setMonth6(selectNcbRecordItem.getMonth6());

            if (selectNcbRecordItem.isRefinance() == true) {
                ncbDetailView.setRefinanceFlag(1);
            } else {
                ncbDetailView.setRefinanceFlag(0);
            }

            if (selectNcbRecordItem.isTMB() == true) {
                ncbDetailView.setTMBAccount(1);
            } else {
                ncbDetailView.setTMBAccount(0);
            }

            if (selectNcbRecordItem.isWc() == true) {
                ncbDetailView.setWcFlag(1);
            } else {
                ncbDetailView.setWcFlag(0);
            }


            if (selectNcbRecordItem.getNoOfmonthsPayment() > 0) {
                genTextBoxFlag = true;
                toSetRenderedFlag(selectNcbRecordItem.getNoOfmonthsPayment());
            } else {
                genTextBoxFlag = false;
            }

        }
    }

    public void onDeleteNcbDetail() {
        log.info("onDeleteNcbDetail ::: selectNcbRecordItem: {}", selectNcbRecordItem);
        ncbDetailViewList.remove(selectNcbRecordItem);
    }

    public void onSaveNcbRecord() {
        log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;
        String moneyTotal = "";
        List<BigDecimal> moneys;

        if (ncbDetailView.getAccountType().getId() != 0 && ncbDetailView.getAccountStatus().getId() != 0 && ncbDetailView.getCurrentPayment().getId() != 0 && ncbDetailView.getHistoryPayment().getId() != 0) {
            if (modeForButton != null && modeForButton.equalsIgnoreCase("add")) {

                AccountType accountType = accountTypeDAO.findById(ncbDetailView.getAccountType().getId());
                AccountStatus accountStatus = accountStatusDAO.findById(ncbDetailView.getAccountStatus().getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(ncbDetailView.getCurrentPayment().getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(ncbDetailView.getHistoryPayment().getId());

                NCBDetailView ncbAdd = new NCBDetailView();
                ncbAdd.setAccountType(accountType);
                ncbAdd.setAccountStatus(accountStatus);
                ncbAdd.setDateOfInfo(ncbDetailView.getDateOfInfo());
                ncbAdd.setAccountOpenDate(ncbDetailView.getAccountOpenDate());
                ncbAdd.setLimit(ncbDetailView.getLimit());
                ncbAdd.setOutstanding(ncbDetailView.getOutstanding());
                ncbAdd.setInstallment(ncbDetailView.getInstallment());
                ncbAdd.setDateOfDebtRestructuring(ncbDetailView.getDateOfDebtRestructuring());
                ncbAdd.setCurrentPayment(tdrConditionCurrent);
                ncbAdd.setHistoryPayment(tdrConditionHistory);
                ncbAdd.setNoOfOutstandingPaymentIn12months(ncbDetailView.getNoOfOutstandingPaymentIn12months());
                ncbAdd.setNoOfOverLimit(ncbDetailView.getNoOfOverLimit());
                ncbAdd.setNoOfmonthsPayment(ncbDetailView.getNoOfmonthsPayment());
//                ncbAdd.setMonth1(ncbDetailView.getMonth1());
//                ncbAdd.setMonth2(ncbDetailView.getMonth2());
//                ncbAdd.setMonth3(ncbDetailView.getMonth3());
//                ncbAdd.setMonth4(ncbDetailView.getMonth4());
//                ncbAdd.setMonth5(ncbDetailView.getMonth5());
//                ncbAdd.setMonth6(ncbDetailView.getMonth6());
                //checkbox
                ncbAdd.setTMB(ncbDetailView.isTMB());
                ncbAdd.setRefinance(ncbDetailView.isRefinance());
                ncbAdd.setWc(ncbDetailView.isWc());
                ncbAdd.setTMBAccount(Util.returnNumForFlag(ncbDetailView.isTMB()));
                ncbAdd.setWcFlag(Util.returnNumForFlag(ncbDetailView.isWc()));
                ncbAdd.setRefinanceFlag(Util.returnNumForFlag(ncbDetailView.isRefinance()));

               /* if (ncbDetailView.isWc() == true) {
                    ncbAdd.setWcFlag(1);
                } else {
                    ncbAdd.setWcFlag(0);
                }

                if (ncbDetailView.isRefinance() == true) {
                    ncbAdd.setRefinanceFlag(1);
                } else {
                    ncbAdd.setRefinanceFlag(0);
                }*/

/*                moneys = new ArrayList<BigDecimal>();
                moneys.add(ncbDetailView.getMonth1());
                moneys.add(ncbDetailView.getMonth2());
                moneys.add(ncbDetailView.getMonth3());
                moneys.add(ncbDetailView.getMonth4());
                moneys.add(ncbDetailView.getMonth5());
                moneys.add(ncbDetailView.getMonth6());*/

//                log.info("ncbDetailView.getNoOfmonthsPayment   >> " + ncbDetailView.getNoOfmonthsPayment());

//                if ((ncbDetailView.getNoOfmonthsPayment() != 0)) {
//                    ncbAdd.setMonthsPaymentFlag(true);
//
//                    for (int i = 0; i < moneys.size(); i++) {
//                        if (i < ncbDetailView.getNoOfmonthsPayment()) {
//                            moneyTotal += msg.get("app.ncbDetail.table.monthNo") + (i + 1) + " : " + Util.formatNumber(moneys.get(i).doubleValue()) + msg.get("app.ncbDetail.table.bath");
//                            ncbAdd.setMoneyTotal(moneyTotal);
//                        }
//                    }
//
//                } else {
//                    ncbAdd.setMonthsPaymentFlag(false);
//                    ncbAdd.setMoneyTotal("");
//                }
//                log.info("ncbAdd.isMonthsPaymentFlag   {} ", ncbAdd.isMonthsPaymentFlag());

                ncbAdd.setCanToEdit(true);
                ncbDetailViewList.add(ncbAdd);

                log.info("add finish");

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(ncbDetailView.getAccountType().getId());
                AccountStatus accountStatus = accountStatusDAO.findById(ncbDetailView.getAccountStatus().getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(ncbDetailView.getCurrentPayment().getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(ncbDetailView.getHistoryPayment().getId());

                ncbDetailViewList.get(rowIndex).setAccountType(accountType);
                ncbDetailViewList.get(rowIndex).setAccountStatus(accountStatus);
                ncbDetailViewList.get(rowIndex).setCurrentPayment(tdrConditionCurrent);
                ncbDetailViewList.get(rowIndex).setHistoryPayment(tdrConditionHistory);
                ncbDetailViewList.get(rowIndex).setDateOfInfo(ncbDetailView.getDateOfInfo());
                ncbDetailViewList.get(rowIndex).setAccountOpenDate(ncbDetailView.getAccountOpenDate());
                ncbDetailViewList.get(rowIndex).setLimit(ncbDetailView.getLimit());
                ncbDetailViewList.get(rowIndex).setOutstanding(ncbDetailView.getOutstanding());
                ncbDetailViewList.get(rowIndex).setInstallment(ncbDetailView.getInstallment());
                ncbDetailViewList.get(rowIndex).setDateOfDebtRestructuring(ncbDetailView.getDateOfDebtRestructuring());
                ncbDetailViewList.get(rowIndex).setNoOfOutstandingPaymentIn12months(ncbDetailView.getNoOfOutstandingPaymentIn12months());
                ncbDetailViewList.get(rowIndex).setNoOfOverLimit(ncbDetailView.getNoOfOverLimit());
                ncbDetailViewList.get(rowIndex).setNoOfmonthsPayment(ncbDetailView.getNoOfmonthsPayment());
//                ncbDetailViewList.get(rowIndex).setMonth1(ncbDetailView.getMonth1());
//                ncbDetailViewList.get(rowIndex).setMonth2(ncbDetailView.getMonth2());
//                ncbDetailViewList.get(rowIndex).setMonth3(ncbDetailView.getMonth3());
//                ncbDetailViewList.get(rowIndex).setMonth4(ncbDetailView.getMonth4());
//                ncbDetailViewList.get(rowIndex).setMonth5(ncbDetailView.getMonth5());
//                ncbDetailViewList.get(rowIndex).setMonth6(ncbDetailView.getMonth6());
                //checkbox
                ncbDetailViewList.get(rowIndex).setTMB(ncbDetailView.isTMB());
                ncbDetailViewList.get(rowIndex).setRefinance(ncbDetailView.isRefinance());
                ncbDetailViewList.get(rowIndex).setWc(ncbDetailView.isWc());

                ncbDetailViewList.get(rowIndex).setTMBAccount(ncbDetailView.getTMBAccount());
                ncbDetailViewList.get(rowIndex).setWcFlag(ncbDetailView.getWcFlag());
                ncbDetailViewList.get(rowIndex).setRefinanceFlag(ncbDetailView.getRefinanceFlag());

/*                moneys = new ArrayList<BigDecimal>();
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth1());
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth2());
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth3());
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth4());
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth5());
                moneys.add(ncbDetailViewList.get(rowIndex).getMonth6());*/

/*                if ((ncbDetailView.getNoOfmonthsPayment() != 0)) {
                    ncbDetailViewList.get(rowIndex).setMonthsPaymentFlag(true);

                    if ((ncbDetailView.getNoOfmonthsPayment() != 0)) {
                        ncbDetailViewList.get(rowIndex).setMonthsPaymentFlag(true);

                        for (int i = 0; i < moneys.size(); i++) {
                            if (i < ncbDetailView.getNoOfmonthsPayment()) {

                                moneyTotal += msg.get("app.ncbDetail.table.monthNo") + (i + 1) + " : " + Util.formatNumber(moneys.get(i).doubleValue()) + "  " + msg.get("app.ncbDetail.table.bath");
                                ncbDetailViewList.get(rowIndex).setMoneyTotal(moneyTotal);
                            }
                        }
                    }

                } else {
                    ncbDetailViewList.get(rowIndex).setMonthsPaymentFlag(false);
                }*/
                ncbDetailViewList.get(rowIndex).setCanToEdit(true);
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

/*    public void onchangeToGenMoneyOfmonthsPayment() {

        log.info("  ncbDetailView.getNoOfmonthsPayment() >>>>  :  {}", ncbDetailView.getNoOfmonthsPayment());

        if (ncbDetailView.getNoOfmonthsPayment() > 0) {
            genTextBoxFlag = true;
            toSetRenderedFlag(ncbDetailView.getNoOfmonthsPayment());
        } else {
            genTextBoxFlag = false;
        }
    }*/

    public void toSetRenderedFlag(int noOfMonth) {

        ArrayList<Boolean> rendered = new ArrayList<Boolean>();

        for (int i = 0; i < 6; i++) {
            if (i < noOfMonth) {
                rendered.add(Boolean.TRUE);
            } else if (i >= noOfMonth) {
                rendered.add(Boolean.FALSE);
            }

            log.info("rendered.get(i).booleanValue() {} ", rendered.get(i).booleanValue());
        }

        monthRender1 = rendered.get(0).booleanValue();
        monthRender2 = rendered.get(1).booleanValue();
        monthRender3 = rendered.get(2).booleanValue();
        monthRender4 = rendered.get(3).booleanValue();
        monthRender5 = rendered.get(4).booleanValue();
        monthRender6 = rendered.get(5).booleanValue();
    }

    //for rendered จำนวนเดือนที่หาร
    public void onChangeAccountType() {
        log.info("onChangeAccountType::");
        log.info("ncbDetailView.getAccountType().getId :: {}", ncbDetailView.getAccountType().getId());
        log.info("ncbDetailView.getMonthFlag() :: {}", ncbDetailView.getAccountType().getMonthFlag());
        log.info("ncbDetailView.monthFlagPage() :: {}", ncbDetailView.isMonthFlagPage());

    }

    // *** Function for save NCB To DB ***//
    public void onSaveNcb() {    // call transform  and then call businessControl
        log.info("onSaveNcb::::");
        log.info("ncbDetailViewList.size() ::: {} ", ncbDetailViewList.size());
        try {
            if (ncbDetailViewList.size() > 0) {
                if (ncbInfoView.getId() == 0) {
                    ncbInfoView.setCreateBy(user);
                    ncbInfoView.setCreateDate(DateTime.now().toDate());
                }

                ncbInfoView.setModifyBy(user);
                ncbInfoView.setModifyDate(DateTime.now().toDate());
                ncbInfoControl.onSaveNCBToDB(ncbInfoView, ncbDetailViewList);

                messageHeader = msg.get("app.header.save.success");
                message = msg.get("app.ncb.response.save.success");
                onCreation();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                messageHeader = msg.get("app.ncb.response.cannot.save");
                message = msg.get("app.ncb.response.desc.cannot.save");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }


        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("app.header.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.header.save.failed") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }
    }


    public void onCancelNcbInfo() {
        log.info("onCancelNcbInfo::::  ");
        onCreation();
    }

    public boolean isGenTextBoxFlag() {
        return genTextBoxFlag;
    }

    public void setGenTextBoxFlag(boolean genTextBoxFlag) {
        this.genTextBoxFlag = genTextBoxFlag;
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

    public NCBDetailView getNcbDetailView() {
        return ncbDetailView;
    }

    public void setNcbDetailView(NCBDetailView ncbDetailView) {
        this.ncbDetailView = ncbDetailView;
    }

    public List<NCBDetailView> getNcbDetailViewList() {
        return ncbDetailViewList;
    }

    public void setNcbDetailViewList(List<NCBDetailView> ncbDetailViewList) {
        this.ncbDetailViewList = ncbDetailViewList;
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

    public SettlementStatus getDlgCurrentPayment() {
        return dlgCurrentPayment;
    }

    public void setDlgCurrentPayment(SettlementStatus dlgCurrentPayment) {
        this.dlgCurrentPayment = dlgCurrentPayment;
    }

    public SettlementStatus getDlgHistoryPayment() {
        return dlgHistoryPayment;
    }

    public void setDlgHistoryPayment(SettlementStatus dlgHistoryPayment) {
        this.dlgHistoryPayment = dlgHistoryPayment;
    }

    public NCBDetailView getSelectNcbRecordItem() {
        return selectNcbRecordItem;
    }

    public void setSelectNcbRecordItem(NCBDetailView selectNcbRecordItem) {
        this.selectNcbRecordItem = selectNcbRecordItem;
    }

    public NCBInfoView getNcbInfoView() {
        return ncbInfoView;
    }

    public void setNcbInfoView(NCBInfoView ncbInfoView) {
        this.ncbInfoView = ncbInfoView;
    }

    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }

    public boolean isMonthRender1() {
        return monthRender1;
    }

    public void setMonthRender1(boolean monthRender1) {
        this.monthRender1 = monthRender1;
    }

    public boolean isMonthRender2() {
        return monthRender2;
    }

    public void setMonthRender2(boolean monthRender2) {
        this.monthRender2 = monthRender2;
    }

    public boolean isMonthRender3() {
        return monthRender3;
    }

    public void setMonthRender3(boolean monthRender3) {
        this.monthRender3 = monthRender3;
    }

    public boolean isMonthRender4() {
        return monthRender4;
    }

    public void setMonthRender4(boolean monthRender4) {
        this.monthRender4 = monthRender4;
    }

    public boolean isMonthRender5() {
        return monthRender5;
    }

    public void setMonthRender5(boolean monthRender5) {
        this.monthRender5 = monthRender5;
    }

    public boolean isMonthRender6() {
        return monthRender6;
    }

    public void setMonthRender6(boolean monthRender6) {
        this.monthRender6 = monthRender6;
    }

    public boolean isNoOfmonthsPaymentFlag() {
        return noOfmonthsPaymentFlag;
    }

    public void setNoOfmonthsPaymentFlag(boolean noOfmonthsPaymentFlag) {
        this.noOfmonthsPaymentFlag = noOfmonthsPaymentFlag;
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

    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public boolean isNplRendered() {
        return nplRendered;
    }

    public void setNplRendered(boolean nplRendered) {
        this.nplRendered = nplRendered;
    }

    public boolean isTdrRendered() {
        return tdrRendered;
    }

    public void setTdrRendered(boolean tdrRendered) {
        this.tdrRendered = tdrRendered;
    }
}
