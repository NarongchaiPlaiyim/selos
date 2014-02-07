package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Month;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
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
    private boolean noOfmonthsPaymentFlag;

    private NCBDetailView ncbDetailView;
    private NCBDetailView selectNcbRecordItem;
    private List<NCBDetailView> ncbDetailViewList;
    private NCBInfoView ncbInfoView;
    private String messageHeader;
    private String message;
    private boolean messageErr;
    private Long customerId;
    //private User user;

    private boolean nplRendered;
    private boolean tdrRendered;
    private List<String> yearList;
    private Customer customerInfoView;

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
    @Inject
    DBRControl dbrControl;
    @Inject
    CreditFacProposeControl creditFacProposeControl;

    private long workCaseId;

    public NCBInfo() {
    }

    public Month[] getStatMonths() {
        return Month.values();
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        modeForButton = "add";
        noOfmonthsPaymentFlag = false;

        HttpSession session = FacesUtil.getSession(true);

        if (session.getAttribute("customerId") != null) {
            customerId = Long.parseLong(session.getAttribute("customerId").toString());
            log.debug("customerId :: {} ", customerId);

            customerInfoView = customerDAO.findById(customerId);
            ncbInfoView = ncbInfoControl.getNCBInfoView(customerId); // find NCB by customer

            if (ncbInfoView != null) {
                ncbDetailViewList = ncbInfoControl.getNcbDetailListView(ncbInfoView);
                log.debug("ncbDetailViewList  :::::::::::: {} ", ncbDetailViewList.size());
                if (ncbDetailViewList.size() > 0) {
                    ncbInfoView.setNcbFlag("Y");
                }
                toControlNplFlagRendered();
                toControlTdrFlagRendered();
            }

            if (session.getAttribute("workCaseId") != null) {
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                log.info("workCaseId :: {} ", workCaseId);
            }
        } else {
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/NCBSummary.jsf");
                return;
            } catch (Exception ex) {
                log.debug("Exception :: {}", ex);
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
//      accountTypeList = accountTypeDAO.findAll();
        tdrConditionList = tdrConditionDAO.findAll();

        if(customerInfoView != null){
            log.debug("customerInfoView.getCustomerEntity().getId() :: {}", customerInfoView.getCustomerEntity().getId());

            accountTypeList = accountTypeDAO.getListLoanTypeByCusEntity(customerInfoView.getCustomerEntity().getId());
            log.debug("accountTypeList :: {}", accountTypeList.size());

            settlementStatusList = settlementStatusDAO.getListSettlementStatusByCusEntity(customerInfoView.getCustomerEntity().getId());
            log.debug("settlementStatusList :: {}", settlementStatusList.size());

            log.debug("customerInfoView : {}", customerInfoView.toString());
        }

        yearList = DateTimeUtil.getPreviousHundredYearTH();

    }

    public void toControlNplFlagRendered() {
        log.debug("nplFlag :: {}", ncbInfoView.getNplFlag());

        if (ncbInfoView.getNplFlag() == 2) {
            nplRendered = true;
        } else {
            nplRendered = false;
        }

    }

    public void toControlTdrFlagRendered() {
        log.debug("tdrFlag :: {}", ncbInfoView.getTdrFlag());

        if (ncbInfoView.getTdrFlag() == 2) {
            tdrRendered = true;
        } else {
            tdrRendered = false;
        }

    }

    // onclick add button
    public void onAddNcbRecord() {
        //*** Reset form ***//
        log.debug("onAddNcbRecord ::: Reset Form");
        ncbDetailView = new NCBDetailView();
        ncbDetailView.reset();
        modeForButton = "add";

    }

    public void onEditNcbDetail() {  //copy row that choose to dialog
        modeForButton = "edit";
        log.debug("onEditNcbDetail ::: selectNcbRecordItem  : {}", selectNcbRecordItem.toString());
        Cloner cloner = new Cloner();

        if (selectNcbRecordItem != null) {
            ncbDetailView = new NCBDetailView();
            ncbDetailView = cloner.deepClone(selectNcbRecordItem);
        }
    }

    public void onDeleteNcbDetail() {
        log.debug("onDeleteNcbDetail ::: selectNcbRecordItem: {}", selectNcbRecordItem);
        ncbDetailViewList.remove(selectNcbRecordItem);
    }

    public void onSaveNcbRecord() {
        log.debug("onSaveNcbRecord ::: mode : {}", modeForButton);

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

                //checkbox
                if(ncbDetailView.getTMBAccount() == RadioValue.YES.value()){
                    ncbAdd.setTmbCheck(true);
                } else {
                    ncbAdd.setTmbCheck(false);
                }
                if(ncbDetailView.getRefinanceFlag() == RadioValue.YES.value()){
                    ncbAdd.setRefinanceCheck(true);
                } else {
                    ncbAdd.setRefinanceCheck(false);
                }
                if(ncbDetailView.getWcFlag() == RadioValue.YES.value()){
                    ncbAdd.setWcCheck(true);
                } else {
                    ncbAdd.setWcCheck(false);
                }
                ncbAdd.setCanToEdit(true);
                ncbDetailViewList.add(ncbAdd);

                log.debug("add finish");

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.debug("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(ncbDetailView.getAccountType().getId());
                AccountStatus accountStatus = accountStatusDAO.findById(ncbDetailView.getAccountStatus().getId());
                SettlementStatus tdrConditionCurrent = settlementStatusDAO.findById(ncbDetailView.getCurrentPayment().getId());
                SettlementStatus tdrConditionHistory = settlementStatusDAO.findById(ncbDetailView.getHistoryPayment().getId());

                //checkbox
                if(ncbDetailView.getTMBAccount() == RadioValue.YES.value()){
                    ncbDetailView.setTmbCheck(true);
                } else {
                    ncbDetailView.setTmbCheck(false);
                }
                if(ncbDetailView.getRefinanceFlag() == RadioValue.YES.value()){
                    ncbDetailView.setRefinanceCheck(true);
                } else {
                    ncbDetailView.setRefinanceCheck(false);
                }
                if(ncbDetailView.getWcFlag() == RadioValue.YES.value()){
                    ncbDetailView.setWcCheck(true);
                } else {
                    ncbDetailView.setWcCheck(false);
                }
                ncbDetailView.setCanToEdit(true);

                ncbDetailViewList.set(rowIndex, ncbDetailView);

            } else {
                log.debug("onSaveNcbRecord ::: Undefined modeForbutton !!");
            }

            complete = true;

        } else {

            log.debug("onSaveNcbRecord ::: validation failed.");
            complete = false;
        }

        log.debug("  complete >>>>  :  {}", complete);

        context.addCallbackParam("functionComplete", complete);

    }
/*

    public void toSetRenderedFlag(int noOfMonth) {

        ArrayList<Boolean> rendered = new ArrayList<Boolean>();

        for (int i = 0; i < 6; i++) {
            if (i < noOfMonth) {
                rendered.add(Boolean.TRUE);
            } else if (i >= noOfMonth) {
                rendered.add(Boolean.FALSE);
            }

            log.debug("rendered.get(i).booleanValue() {} ", rendered.get(i).booleanValue());
        }

    }

    //for rendered จำนวนเดือนที่หาร
    public void onChangeAccountType() {
        log.debug("onChangeAccountType::");
        log.debug("ncbDetailView.getAccountType().getId :: {}", ncbDetailView.getAccountType().getId());
        log.debug("ncbDetailView.getMonthFlag() :: {}", ncbDetailView.getAccountType().getMonthFlag());
        log.debug("ncbDetailView.monthFlagPage() :: {}", ncbDetailView.isMonthFlagPage());

    }
*/

    // *** Function for save NCB To DB ***//
    public void onSaveNcb() {    // call transform  and then call businessControl
        log.debug("onSaveNcb::::");
        log.debug("ncbDetailViewList.size() ::: {} ", ncbDetailViewList.size());
        try {
            if (ncbDetailViewList.size() > 0) {
                /*if (ncbInfoView.getId() == 0) {
                    ncbInfoView.setCreateBy(user);
                    ncbInfoView.setCreateDate(DateTime.now().toDate());
                } else {
                    ncbInfoView.setModifyBy(user);
                }*/
                ncbInfoControl.onSaveNCBToDB(ncbInfoView, ncbDetailViewList);
                dbrControl.updateValueOfDBR(workCaseId);
                messageHeader = msg.get("app.header.save.success");
                message = msg.get("app.ncb.response.save.success");
                creditFacProposeControl.calWC(workCaseId);
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
        log.debug("onCancelNcbInfo::::  ");
        onCreation();
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

    public List<NCBDetailView> getNcbDetailViewList() {
        return ncbDetailViewList;
    }

    public void setNcbDetailViewList(List<NCBDetailView> ncbDetailViewList) {
        this.ncbDetailViewList = ncbDetailViewList;
    }

    public NCBDetailView getNcbDetailView() {
        return ncbDetailView;
    }

    public void setNcbDetailView(NCBDetailView ncbDetailView) {
        this.ncbDetailView = ncbDetailView;
    }

    public NCBInfoControl getNcbInfoControl() {
        return ncbInfoControl;
    }

    public void setNcbInfoControl(NCBInfoControl ncbInfoControl) {
        this.ncbInfoControl = ncbInfoControl;
    }

    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
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

    public NCBInfoView getNcbInfoView() {
        return ncbInfoView;
    }

    public void setNcbInfoView(NCBInfoView ncbInfoView) {
        this.ncbInfoView = ncbInfoView;
    }

}
