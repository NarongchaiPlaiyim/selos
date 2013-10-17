package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
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
@ManagedBean(name = "ncbInfo")
public class NCBInfo implements Serializable {

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
    private boolean noOfmonthsPaymentFlag;
    private boolean genColumnEdit;

    private NCBDetailView NCBDetailView;
    private NCBDetailView selectNcbRecordItem;
    private List<NCBDetailView> NCBDetailViewList;
    private NCBInfoView NCBInfoView;
    private String messageHeader;
    private String message;
    private boolean messageErr;
    private Long customerId;
    private User user;

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

    public NCBInfo() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";
        genTextBoxFlag = false;
        noOfmonthsPaymentFlag = false;
        genColumnEdit = false;

        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("customerId") != null){
            customerId = Long.parseLong(session.getAttribute("customerId").toString());
            log.info("customerId :: {} ",customerId);

            NCBInfoView = ncbInfoControl.getNCBInfoView(customerId); // find NCB by customer

            if(NCBInfoView != null){
                NCBDetailViewList = ncbInfoControl.getNcbDetailListView(NCBInfoView);
                log.info("NCBDetailViewList  :::::::::::: {} ", NCBDetailViewList);
            }
        }

        if (NCBDetailView == null) {
            NCBDetailView = new NCBDetailView();
        }

        if (NCBDetailViewList == null) {
            NCBDetailViewList = new ArrayList<NCBDetailView>();
        }

        if (NCBInfoView == null) {
            NCBInfoView = new NCBInfoView();
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

        try{
            accountStatusList = accountStatusDAO.findAll();
            accountTypeList = accountTypeDAO.findAll();
            settlementStatusList = settlementStatusDAO.findAll();
            tdrConditionList = tdrConditionDAO.findAll();
        }catch (Exception e){
            log.error( " findAll  error ::: {}" , e.getMessage());
        }

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
        log.info("onAddNcbRecord ::: Reset Form");
        NCBDetailView = new NCBDetailView();
        NCBDetailView.setAccountStatus(new AccountStatus());
        NCBDetailView.setAccountType(new AccountType());
        NCBDetailView.setCurrentPayment(new SettlementStatus());
        NCBDetailView.setHistoryPayment(new SettlementStatus());
        NCBDetailView.setTMBAccount(0);
        NCBDetailView.setRefinanceFlag(0);
        NCBDetailView.setWcFlag(0);
        modeForButton = "add";
        genTextBoxFlag = false;
        genColumnEdit = true;

    }

    public void onEditNcbDetail() {  //copy row that choose to dialog
        log.info("rowIndex {} ", rowIndex);
        modeForButton = "edit";
        log.info("onEditNcbDetail ::: selectNcbRecordItem  : {}", selectNcbRecordItem.toString());
        log.info("rowIndex :::   : {}", rowIndex);
        log.info("NCBDetailViewList.size() :::   : {}", NCBDetailViewList.size());

        if (rowIndex < NCBDetailViewList.size()) {
            NCBDetailView = new NCBDetailView();
            AccountType accountTypeEdit = selectNcbRecordItem.getAccountType();
            AccountStatus accountStatusEdit = selectNcbRecordItem.getAccountStatus();
            SettlementStatus conditionCurrentEdit = selectNcbRecordItem.getCurrentPayment();
            SettlementStatus conditionHistoryEdit = selectNcbRecordItem.getHistoryPayment();

            NCBDetailView.setAccountType(accountTypeEdit);
            NCBDetailView.setAccountStatus(accountStatusEdit);
            NCBDetailView.setCurrentPayment(conditionCurrentEdit);
            NCBDetailView.setHistoryPayment(conditionHistoryEdit);
            NCBDetailView.setTMBAccount(selectNcbRecordItem.getTMBAccount());
            NCBDetailView.setDateOfInfo(selectNcbRecordItem.getDateOfInfo());
            NCBDetailView.setAccountOpenDate(selectNcbRecordItem.getAccountOpenDate());
            NCBDetailView.setLimit(selectNcbRecordItem.getLimit());
            NCBDetailView.setOutstanding(selectNcbRecordItem.getOutstanding());
            NCBDetailView.setInstallment(selectNcbRecordItem.getInstallment());
            NCBDetailView.setDateOfDebtRestructuring(selectNcbRecordItem.getDateOfDebtRestructuring());
            NCBDetailView.setNoOfOutstandingPaymentIn12months(selectNcbRecordItem.getNoOfOutstandingPaymentIn12months());
            NCBDetailView.setNoOfOverLimit(selectNcbRecordItem.getNoOfOverLimit());
            NCBDetailView.setRefinanceFlag(selectNcbRecordItem.getRefinanceFlag());
            NCBDetailView.setNoOfmonthsPayment(selectNcbRecordItem.getNoOfmonthsPayment());
            NCBDetailView.setWcFlag(selectNcbRecordItem.getWcFlag());
            NCBDetailView.setMonth1(selectNcbRecordItem.getMonth1());
            NCBDetailView.setMonth2(selectNcbRecordItem.getMonth2());
            NCBDetailView.setMonth3(selectNcbRecordItem.getMonth3());
            NCBDetailView.setMonth4(selectNcbRecordItem.getMonth4());
            NCBDetailView.setMonth5(selectNcbRecordItem.getMonth5());
            NCBDetailView.setMonth6(selectNcbRecordItem.getMonth6());

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
        NCBDetailViewList.remove(selectNcbRecordItem);
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

                NCBDetailView ncbAdd = new NCBDetailView();
                ncbAdd.setAccountType(accountType);
                ncbAdd.setAccountStatus(accountStatus);
                ncbAdd.setTMBAccount(NCBDetailView.getTMBAccount());
                ncbAdd.setDateOfInfo(NCBDetailView.getDateOfInfo());
                ncbAdd.setAccountOpenDate(NCBDetailView.getAccountOpenDate());
                ncbAdd.setLimit(NCBDetailView.getLimit());
                ncbAdd.setOutstanding(NCBDetailView.getOutstanding());
                ncbAdd.setInstallment(NCBDetailView.getInstallment());
                ncbAdd.setDateOfDebtRestructuring(NCBDetailView.getDateOfDebtRestructuring());
                ncbAdd.setCurrentPayment(tdrConditionCurrent);
                ncbAdd.setHistoryPayment(tdrConditionHistory);
                ncbAdd.setNoOfOutstandingPaymentIn12months(NCBDetailView.getNoOfOutstandingPaymentIn12months());
                ncbAdd.setNoOfOverLimit(NCBDetailView.getNoOfOverLimit());
                ncbAdd.setRefinanceFlag(NCBDetailView.getRefinanceFlag());
                ncbAdd.setWcFlag(NCBDetailView.getWcFlag());
                ncbAdd.setNoOfmonthsPayment(NCBDetailView.getNoOfmonthsPayment());
                ncbAdd.setMonth1(NCBDetailView.getMonth1());
                ncbAdd.setMonth2(NCBDetailView.getMonth2());
                ncbAdd.setMonth3(NCBDetailView.getMonth3());
                ncbAdd.setMonth4(NCBDetailView.getMonth4());
                ncbAdd.setMonth5(NCBDetailView.getMonth5());
                ncbAdd.setMonth6(NCBDetailView.getMonth6());

                moneys = new ArrayList<BigDecimal>();
                moneys.add(NCBDetailView.getMonth1());
                moneys.add(NCBDetailView.getMonth2());
                moneys.add(NCBDetailView.getMonth3());
                moneys.add(NCBDetailView.getMonth4());
                moneys.add(NCBDetailView.getMonth5());
                moneys.add(NCBDetailView.getMonth6());

                log.info("NCBDetailView.getNoOfmonthsPayment   >> " + NCBDetailView.getNoOfmonthsPayment());

                if ((NCBDetailView.getNoOfmonthsPayment() != 0)) {
                    ncbAdd.setMonthsPaymentFlag(true);

                    for(int i = 0 ; i<moneys.size() ; i ++){
                        if(i < NCBDetailView.getNoOfmonthsPayment()){
                            moneyTotal += " เดือนที่ "+ (i+1) + " : " + Util.formatNumber(moneys.get(i).doubleValue()) + " บาท ";
                            ncbAdd.setMoneyTotal(moneyTotal);
                        }
                    }

                } else {
                    ncbAdd.setMonthsPaymentFlag(false);
                    ncbAdd.setMoneyTotal("");
                }
                log.info("ncbAdd.isMonthsPaymentFlag   {} ", ncbAdd.isMonthsPaymentFlag());

                NCBDetailViewList.add(ncbAdd);
                log.info("add finish");

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(dlgAccountType.getId());
                AccountStatus accountStatus = accountStatusDAO.findById(dlgAccountStatus.getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(dlgCurrentPayment.getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(dlgHistoryPayment.getId());

                NCBDetailViewList.get(rowIndex).setAccountType(accountType);
                NCBDetailViewList.get(rowIndex).setAccountStatus(accountStatus);
                NCBDetailViewList.get(rowIndex).setCurrentPayment(tdrConditionCurrent);
                NCBDetailViewList.get(rowIndex).setHistoryPayment(tdrConditionHistory);
                NCBDetailViewList.get(rowIndex).setTMBAccount(NCBDetailView.getTMBAccount());
                NCBDetailViewList.get(rowIndex).setDateOfInfo(NCBDetailView.getDateOfInfo());
                NCBDetailViewList.get(rowIndex).setAccountOpenDate(NCBDetailView.getAccountOpenDate());
                NCBDetailViewList.get(rowIndex).setLimit(NCBDetailView.getLimit());
                NCBDetailViewList.get(rowIndex).setOutstanding(NCBDetailView.getOutstanding());
                NCBDetailViewList.get(rowIndex).setInstallment(NCBDetailView.getInstallment());
                NCBDetailViewList.get(rowIndex).setDateOfDebtRestructuring(NCBDetailView.getDateOfDebtRestructuring());
                NCBDetailViewList.get(rowIndex).setNoOfOutstandingPaymentIn12months(NCBDetailView.getNoOfOutstandingPaymentIn12months());
                NCBDetailViewList.get(rowIndex).setNoOfOverLimit(NCBDetailView.getNoOfOverLimit());
                NCBDetailViewList.get(rowIndex).setRefinanceFlag(NCBDetailView.getRefinanceFlag());
                NCBDetailViewList.get(rowIndex).setNoOfmonthsPayment(NCBDetailView.getNoOfmonthsPayment());
                NCBDetailViewList.get(rowIndex).setMonth1(NCBDetailView.getMonth1());
                NCBDetailViewList.get(rowIndex).setMonth2(NCBDetailView.getMonth2());
                NCBDetailViewList.get(rowIndex).setMonth3(NCBDetailView.getMonth3());
                NCBDetailViewList.get(rowIndex).setMonth4(NCBDetailView.getMonth4());
                NCBDetailViewList.get(rowIndex).setMonth5(NCBDetailView.getMonth5());
                NCBDetailViewList.get(rowIndex).setMonth6(NCBDetailView.getMonth6());
                NCBDetailViewList.get(rowIndex).setWcFlag(NCBDetailView.getWcFlag());

                moneys = new ArrayList<BigDecimal>();
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth1());
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth2());
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth3());
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth4());
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth5());
                moneys.add(NCBDetailViewList.get(rowIndex).getMonth6());

                if ((NCBDetailView.getNoOfmonthsPayment() != 0)) {
                    NCBDetailViewList.get(rowIndex).setMonthsPaymentFlag(true);

                    if ((NCBDetailView.getNoOfmonthsPayment() != 0)) {
                        NCBDetailViewList.get(rowIndex).setMonthsPaymentFlag(true);

                        for(int i = 0 ; i<moneys.size() ; i ++){
                            if(i < NCBDetailView.getNoOfmonthsPayment()){

                                moneyTotal += " เดือนที่ "+ (i+1) + " : " + Util.formatNumber(moneys.get(i).doubleValue()) + " บาท ";
                                NCBDetailViewList.get(rowIndex).setMoneyTotal(moneyTotal);
                            }
                        }
                    }

                } else {
                    NCBDetailViewList.get(rowIndex).setMonthsPaymentFlag(false);
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

        log.info("  NCBDetailView.getNoOfmonthsPayment() >>>>  :  {}", NCBDetailView.getNoOfmonthsPayment());

        if (NCBDetailView.getNoOfmonthsPayment() > 0) {
            genTextBoxFlag = true;
            toSetRenderedFlag(NCBDetailView.getNoOfmonthsPayment());
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

    //for rendered จำนวนเดือนที่หาร
    public void onChangeAccountType(){
        log.info("onChangeAccountType::");
        log.info("dlgAccountType.getId() :: {}" ,dlgAccountType.getId());
        noOfmonthsPaymentFlag = true;
       // ต้องมีสูตรมาให้

    }

    // *** Function for save NCB To DB ***//
    public void onSaveNcb() {    // call transform  and then call businessControl
        log.info("onSaveNcb::::");
        log.info("NCBDetailViewList.size() ::: {} ",NCBDetailViewList.size());
        try{
            if(NCBDetailViewList.size() > 0){
                if(NCBInfoView.getId() == 0){
                    NCBInfoView.setCreateBy(user);
                    NCBInfoView.setCreateDate(DateTime.now().toDate());
                }

                ncbInfoControl.onSaveNCBToDB(NCBInfoView, NCBDetailViewList);

                messageHeader = "Save NCB Success.";
                message = "Save NCB data success.";
                onCreation();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else{
                messageHeader = "can not to save NCB .";
                message = "please add NCB record information.";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }


         } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = "Save NCB failed.";

            if(ex.getCause() != null){
                message = "Save NCB failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save NCB failed. Cause : " + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }
    }


    public void onCancelNcbInfo(){
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

    public NCBDetailView getNCBDetailView() {
        return NCBDetailView;
    }

    public void setNCBDetailView(NCBDetailView NCBDetailView) {
        this.NCBDetailView = NCBDetailView;
    }

    public List<NCBDetailView> getNCBDetailViewList() {
        return NCBDetailViewList;
    }

    public void setNCBDetailViewList(List<NCBDetailView> NCBDetailViewList) {
        this.NCBDetailViewList = NCBDetailViewList;
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

    public NCBInfoView getNCBInfoView() {
        return NCBInfoView;
    }

    public void setNCBInfoView(NCBInfoView NCBInfoView) {
        this.NCBInfoView = NCBInfoView;
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

    public boolean isGenColumnEdit() {
        return genColumnEdit;
    }

    public void setGenColumnEdit(boolean genColumnEdit) {
        this.genColumnEdit = genColumnEdit;
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
}
