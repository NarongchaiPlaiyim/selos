package com.clevel.selos.controller;


import com.clevel.selos.busiensscontrol.FullAppBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NCB;
import com.clevel.selos.model.view.MoneyPaymentView;
import com.clevel.selos.model.view.NcbRecordView;
import com.clevel.selos.model.view.NcbResultView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
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
    //test
    private NcbRecordView ncbRecordView;
    private NcbRecordView selectNcbRecordItem;
    private List<NcbRecordView> ncbRecordViewList;
    private NcbResultView ncbResultView;

    @Inject
    private AccountStatusDAO accountStatusDAO;
    @Inject
    private AccountTypeDAO accountTypeDAO;
    @Inject
    private SettlementStatusDAO settlementStatusDAO;
    @Inject
    private TDRConditionDAO tdrConditionDAO;
    @Inject
    FullAppBusinessControl fullAppBusinessControl;
    @Inject
    private UserDAO userDAO;
    @Inject
    private CustomerEntityDAO customerEntityDAO;

    public FullappNcb() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";
        genTextBoxFlag = false;

        if (ncbRecordView == null) {
            ncbRecordView = new NcbRecordView();
        }

        if (ncbRecordViewList == null) {
            ncbRecordViewList = new ArrayList<NcbRecordView>();
        }

        if (ncbResultView == null) {
            ncbResultView = new NcbResultView();
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
        ncbRecordView.setCurrentPayment(new SettlementStatus());
        ncbRecordView.setHistoryPayment(new SettlementStatus());
        ncbRecordView.setTMBAccount(false);
        ncbRecordView.setRefinanceFlag(false);
        modeForButton = "add";
        genTextBoxFlag = false;

        //test default for first section
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
            SettlementStatus conditionCurrentEdit = selectNcbRecordItem.getCurrentPayment();
            SettlementStatus conditionHistoryEdit = selectNcbRecordItem.getHistoryPayment();

            ncbRecordView.setAccountType(accountTypeEdit);
            ncbRecordView.setAccountStatus(accountStatusEdit);
            ncbRecordView.setCurrentPayment(conditionCurrentEdit);
            ncbRecordView.setHistoryPayment(conditionHistoryEdit);
            ncbRecordView.setTMBAccount(selectNcbRecordItem.isTMBAccount());
            ncbRecordView.setDateOfInfo(selectNcbRecordItem.getDateOfInfo());
            ncbRecordView.setAccountOpenDate(selectNcbRecordItem.getAccountOpenDate());
            ncbRecordView.setLimit(selectNcbRecordItem.getLimit());
            ncbRecordView.setOutstanding(selectNcbRecordItem.getOutstanding());
            ncbRecordView.setInstallment(selectNcbRecordItem.getInstallment());
            ncbRecordView.setDateOfDebtRestructuring(selectNcbRecordItem.getDateOfDebtRestructuring());
            ncbRecordView.setNoOfOutstandingPaymentIn12months(selectNcbRecordItem.getNoOfOutstandingPaymentIn12months());
            ncbRecordView.setNoOfOverLimit(selectNcbRecordItem.getNoOfOverLimit());
            ncbRecordView.setRefinanceFlag(selectNcbRecordItem.isRefinanceFlag());
            ncbRecordView.setNoOfmonthsPayment(selectNcbRecordItem.getNoOfmonthsPayment());
            ncbRecordView.setMoneyPaymentViewList(selectNcbRecordItem.getMoneyPaymentViewList());
            ncbRecordView.setMonth1(selectNcbRecordItem.getMonth1());
            ncbRecordView.setMonth2(selectNcbRecordItem.getMonth2());
            ncbRecordView.setMonth3(selectNcbRecordItem.getMonth3());
            ncbRecordView.setMonth4(selectNcbRecordItem.getMonth4());
            ncbRecordView.setMonth5(selectNcbRecordItem.getMonth5());
            ncbRecordView.setMonth6(selectNcbRecordItem.getMonth6());

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
        ncbRecordViewList.remove(selectNcbRecordItem);
    }

    public void onSaveNcbRecord() {
        log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;
        String moneyTotal = "";
        List<BigDecimal>  moneys;

        log.info("  dlgAccountType : {}", dlgAccountType.getId());
        log.info("  dlgAccountStatus :  {}", dlgAccountStatus.getId());
        log.info("  dlgCurrentPayment :  {}", dlgCurrentPayment.getId());
        log.info("  dlgHistoryPayment :  {}", dlgHistoryPayment.getId());


        if (dlgAccountType.getId() != 0 && dlgAccountStatus.getId() != 0 && dlgCurrentPayment.getId() != 0 && dlgHistoryPayment.getId() != 0) {
            if (modeForButton != null && modeForButton.equalsIgnoreCase("add")) {

                AccountType accountType = accountTypeDAO.findById(dlgAccountType.getId());
                AccountStatus accountStatus = accountStatusDAO.findById(dlgAccountStatus.getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(dlgCurrentPayment.getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(dlgHistoryPayment.getId());

                NcbRecordView ncbAdd = new NcbRecordView();
                ncbAdd.setAccountType(accountType);
                ncbAdd.setAccountStatus(accountStatus);
                ncbAdd.setTMBAccount(ncbRecordView.isTMBAccount());
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
                ncbAdd.setRefinanceFlag(ncbRecordView.isRefinanceFlag());
                ncbAdd.setNoOfmonthsPayment(ncbRecordView.getNoOfmonthsPayment());
                ncbAdd.setMonth1(ncbRecordView.getMonth1());
                ncbAdd.setMonth2(ncbRecordView.getMonth2());
                ncbAdd.setMonth3(ncbRecordView.getMonth3());
                ncbAdd.setMonth4(ncbRecordView.getMonth4());
                ncbAdd.setMonth5(ncbRecordView.getMonth5());
                ncbAdd.setMonth6(ncbRecordView.getMonth6());

                moneys = new ArrayList<BigDecimal>();
                moneys.add(ncbRecordView.getMonth1());
                moneys.add(ncbRecordView.getMonth2());
                moneys.add(ncbRecordView.getMonth3());
                moneys.add(ncbRecordView.getMonth4());
                moneys.add(ncbRecordView.getMonth5());
                moneys.add(ncbRecordView.getMonth6());

                log.info("ncbRecordView.getNoOfmonthsPayment   >> " + ncbRecordView.getNoOfmonthsPayment());

                if ((ncbRecordView.getNoOfmonthsPayment() != 0)) {
                    ncbAdd.setMonthsPaymentFlag(true);

                    for(int i = 0 ; i<moneys.size() ; i ++){
                        if(i < ncbRecordView.getNoOfmonthsPayment()){
                            moneyTotal += " เดือนที่#"+ (i+1) + " : " +moneys.get(i).toString() + " บาท ";
                            ncbAdd.setMoneyTotal(moneyTotal);
                        }
                    }

                  /*  ncbAdd.setMoneyPaymentViewList(ncbRecordView.getMoneyPaymentViewList());

                    for(int i=0;i<ncbRecordView.getMoneyPaymentViewList().size();i++)
                    {
                        log.info("ncbAdd.getMoneyPaymentViewList().get(i).getMoneyPayment()   >> {} " , ncbAdd.getMoneyPaymentViewList().get(i).getMoneyPayment());
                        moneyTotal += " เดือนที่#"+ (i+1) + " : " +ncbAdd.getMoneyPaymentViewList().get(i).getMoneyPayment() + " บาท ";
                        ncbAdd.setMoneyTotal(moneyTotal);

                    }*/

                } else {
                    ncbAdd.setMonthsPaymentFlag(false);
                    ncbAdd.setMoneyTotal("");
                }
                log.info("ncbAdd.isMonthsPaymentFlag   {} ", ncbAdd.isMonthsPaymentFlag());

                ncbRecordViewList.add(ncbAdd);
                log.info("add finish");
                log.info(" ncbRecordViewList.getMoneyPaymentViewList().size() ::: {} ", ncbRecordViewList.get(0).getMoneyPaymentViewList().size());

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(dlgAccountType.getId());
                AccountStatus accountStatus = accountStatusDAO.findById(dlgAccountStatus.getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(dlgCurrentPayment.getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(dlgHistoryPayment.getId());

                ncbRecordViewList.get(rowIndex).setAccountType(accountType);
                ncbRecordViewList.get(rowIndex).setAccountStatus(accountStatus);
                ncbRecordViewList.get(rowIndex).setCurrentPayment(tdrConditionCurrent);
                ncbRecordViewList.get(rowIndex).setHistoryPayment(tdrConditionHistory);
                ncbRecordViewList.get(rowIndex).setTMBAccount(ncbRecordView.isTMBAccount());
                ncbRecordViewList.get(rowIndex).setDateOfInfo(ncbRecordView.getDateOfInfo());
                ncbRecordViewList.get(rowIndex).setAccountOpenDate(ncbRecordView.getAccountOpenDate());
                ncbRecordViewList.get(rowIndex).setLimit(ncbRecordView.getLimit());
                ncbRecordViewList.get(rowIndex).setOutstanding(ncbRecordView.getOutstanding());
                ncbRecordViewList.get(rowIndex).setInstallment(ncbRecordView.getInstallment());
                ncbRecordViewList.get(rowIndex).setDateOfDebtRestructuring(ncbRecordView.getDateOfDebtRestructuring());
                ncbRecordViewList.get(rowIndex).setNoOfOutstandingPaymentIn12months(ncbRecordView.getNoOfOutstandingPaymentIn12months());
                ncbRecordViewList.get(rowIndex).setNoOfOverLimit(ncbRecordView.getNoOfOverLimit());
                ncbRecordViewList.get(rowIndex).setRefinanceFlag(ncbRecordView.isRefinanceFlag());
                ncbRecordViewList.get(rowIndex).setNoOfmonthsPayment(ncbRecordView.getNoOfmonthsPayment());
                ncbRecordViewList.get(rowIndex).setMoneyPaymentViewList(ncbRecordView.getMoneyPaymentViewList());
                ncbRecordViewList.get(rowIndex).setMonth1(ncbRecordView.getMonth1());
                ncbRecordViewList.get(rowIndex).setMonth2(ncbRecordView.getMonth2());
                ncbRecordViewList.get(rowIndex).setMonth3(ncbRecordView.getMonth3());
                ncbRecordViewList.get(rowIndex).setMonth4(ncbRecordView.getMonth4());
                ncbRecordViewList.get(rowIndex).setMonth5(ncbRecordView.getMonth5());
                ncbRecordViewList.get(rowIndex).setMonth6(ncbRecordView.getMonth6());

                moneys = new ArrayList<BigDecimal>();
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth1());
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth2());
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth3());
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth4());
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth5());
                moneys.add(ncbRecordViewList.get(rowIndex).getMonth6());

                if ((ncbRecordView.getNoOfmonthsPayment() != 0)) {
                    ncbRecordViewList.get(rowIndex).setMonthsPaymentFlag(true);

                    if ((ncbRecordView.getNoOfmonthsPayment() != 0)) {
                        ncbRecordViewList.get(rowIndex).setMonthsPaymentFlag(true);

                        for(int i = 0 ; i<moneys.size() ; i ++){
                            if(i < ncbRecordView.getNoOfmonthsPayment()){
                                moneyTotal += " เดือนที่#"+ (i+1) + " : " +moneys.get(i).toString() + " บาท ";
                                ncbRecordViewList.get(rowIndex).setMoneyTotal(moneyTotal);
                            }
                        }
                    }
                    /*for (int i = 0; i < ncbRecordView.getMoneyPaymentViewList().size(); i++) {
                        log.info("ncbRecordViewList.get(rowIndex).getMoneyPaymentViewList().get(i).getMoneyPayment()   >> {} ", ncbRecordViewList.get(rowIndex).getMoneyPaymentViewList().get(i).getMoneyPayment());
                        moneyTotal += " เดือนที่#" + (i + 1) + " : " + ncbRecordViewList.get(rowIndex).getMoneyPaymentViewList().get(i).getMoneyPayment();
                        ncbRecordViewList.get(rowIndex).setMoneyTotal(moneyTotal);
                    }*/

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

    public void onchangeToGenMoneyOfmonthsPayment() {
        if (ncbRecordView.getMoneyPaymentViewList().size() > 0) {
            ncbRecordView.getMoneyPaymentViewList().removeAll(ncbRecordView.getMoneyPaymentViewList());
        }

        log.info("  ncbRecordView.getNoOfmonthsPayment() >>>>  :  {}", ncbRecordView.getNoOfmonthsPayment());

        if (ncbRecordView.getNoOfmonthsPayment() > 0) {
            genTextBoxFlag = true;
            toSetRenderedFlag(ncbRecordView.getNoOfmonthsPayment());

            /*log.info("ncbRecordView.getMoneyPaymentViewList().size():: {} " ,ncbRecordView.getMoneyPaymentViewList().size());

            for(int i = 0 ; i< ncbRecordView.getNoOfmonthsPayment() ;i ++){
               ncbRecordView.getMoneyPaymentViewList().add(new MoneyPaymentView());
            }*/

        } else {
            genTextBoxFlag = false;
        }
    }

    public void toSetRenderedFlag(int noOfMonth){

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


    // *** Function for NCB ***//
    public void onSaveNcb() {     // call transform  and then call businessControl
        log.info("onSaveNcb:::: {} ", ncbResultView.toString());
        //Customer customerSave = customerEntityDAO.findById();

//        HttpSession session = FacesUtil.getSession(true);
//        session.setAttribute("workCaseId", 1);
//        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

        //save to DB
        TDRCondition tdrConditionSave = tdrConditionDAO.findById(tdrCondition.getId());
        ncbResultView.setTdrCondition(tdrConditionSave);

        fullAppBusinessControl.onSaveNCBToDB(ncbResultView,ncbRecordViewList);
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

    public NcbRecordView getSelectNcbRecordItem() {
        return selectNcbRecordItem;
    }

    public void setSelectNcbRecordItem(NcbRecordView selectNcbRecordItem) {
        this.selectNcbRecordItem = selectNcbRecordItem;
    }

    public NcbResultView getNcbResultView() {
        return ncbResultView;
    }

    public void setNcbResultView(NcbResultView ncbResultView) {
        this.ncbResultView = ncbResultView;
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
}
