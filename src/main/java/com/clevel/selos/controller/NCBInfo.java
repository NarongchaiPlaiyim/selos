package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CalculationControl;
import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.NCBDetailView;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.model.view.SettlementStatusView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.SettlementStatusTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "ncbInfo")
public class NCBInfo extends BaseController {

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

    @Inject
    private SLOSAuditor slosAuditor;

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
    private CalculationControl calculationControl;
    @Inject
    SettlementStatusTransform settlementStatusTransform;

    private List<SettlementStatusView> settlementStatusList;
    private List<AccountStatus> accountStatusList;
    private List<AccountType> accountTypeList;
    private List<TDRCondition> tdrConditionList;

    private AccountType dlgAccountType;
    private AccountStatus dlgAccountStatus;
    private SettlementStatus dlgCurrentPayment;
    private SettlementStatus dlgHistoryPayment;
    private TDRCondition tdrCondition;

    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;
    private boolean noOfmonthsPaymentFlag;

    private NCBDetailView ncbDetailView;
    private NCBDetailView selectNcbRecordItem;
    private List<NCBDetailView> ncbDetailViewList;
    private NCBInfoView ncbInfoView;

    private String messageHeader;
    private String message;
    private String severity;

    private Long customerId;
    //private User user;

    private boolean nplRendered;
    private boolean tdrRendered;
    private List<String> yearList;
    private Customer customerInfo;

    private long workCaseId;

    String userId;

    public NCBInfo() {
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0 && (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }
        return checkSession;
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid stepId
            log.debug("preRender ::: Check valid stepId");
        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void initial(){
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
            settlementStatusList = new ArrayList<SettlementStatusView>();
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
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        User user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        modeForButton = ModeForButton.ADD;
        noOfmonthsPaymentFlag = false;

        initial();

        if(checkSession(session)){
            workCaseId = (Long)session.getAttribute("workCaseId");
            if(session.getAttribute("customerId") != null && (Long)session.getAttribute("customerId") != 0){
                customerId = (Long)session.getAttribute("customerId");
                log.debug("onCreation ::: customerId : {}", customerId);

                customerInfo = customerDAO.findById(customerId);
                ncbInfoView = ncbInfoControl.getNCBInfoView(customerId); // find NCB by customer

                if (ncbInfoView != null) {
                    ncbDetailViewList = ncbInfoControl.getNcbDetailListView(ncbInfoView);
                    log.debug("onCreation ::: ncbDetailViewList.size() : {} ", ncbDetailViewList.size());
                    if (ncbDetailViewList.size() > 0) {
                        ncbInfoView.setNcbFlag("Y");
                    }
                    toControlNplFlagRendered();
                    toControlTdrFlagRendered();
                }

                accountStatusList = accountStatusDAO.findAll();
                tdrConditionList = tdrConditionDAO.findAll();

                if(customerInfo != null){
                    log.debug("customerInfo.getCustomerEntity().getId() :: {}", customerInfo.getCustomerEntity().getId());

                    accountTypeList = accountTypeDAO.getListLoanTypeByCusEntity(customerInfo.getCustomerEntity().getId());
                    log.debug("accountTypeList :: {}", accountTypeList.size());

                    settlementStatusList = settlementStatusTransform.transformToView(settlementStatusDAO.getListSettlementStatusByCusEntity(customerInfo.getCustomerEntity().getId()));
                    log.debug("settlementStatusList :: {}", settlementStatusList.size());

                    log.debug("customerInfo : {}", customerInfo.toString());
                }

                yearList = DateTimeUtil.getPreviousHundredYearTH();
            }
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.NCB_DETAIL, ownerCaseUserId);

            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void toControlNplFlagRendered() {
        log.debug("nplFlag :: {}", ncbInfoView.getNplFlag());

        if(ncbInfoView.getNplFlag() == 2) {
            nplRendered = true;
        } else {
            nplRendered = false;
        }
    }

    public void toControlTdrFlagRendered() {
        log.debug("tdrFlag :: {}", ncbInfoView.getTdrFlag());

        if(ncbInfoView.getTdrFlag() == 2) {
            tdrRendered = true;
        } else {
            tdrRendered = false;
        }

    }

    // onclick add button
    public void onAddNcbRecord() {
        Date date = new Date();
        //*** Reset form ***//
        log.debug("onAddNcbRecord ::: Reset Form");
        ncbDetailView = new NCBDetailView();
        ncbDetailView.reset();
        modeForButton = ModeForButton.ADD;

        slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_ADD, "On Add NCB Record Information", date, ActionResult.SUCCESS, "");
    }

    public void onEditNcbDetail() {  //copy row that choose to dialog
        Date date = new Date();
        modeForButton = ModeForButton.EDIT;
        log.debug("onEditNcbDetail ::: selectNcbRecordItem  : {}", selectNcbRecordItem.toString());
        try {
            Cloner cloner = new Cloner();
            if (selectNcbRecordItem != null) {
                ncbDetailView = new NCBDetailView();
                ncbDetailView = cloner.deepClone(selectNcbRecordItem);
                if(ncbDetailView.getRefinanceCheck()){
                    ncbDetailView.setRefinanceFlag(RadioValue.YES.value());
                } else {
                    ncbDetailView.setRefinanceFlag(RadioValue.NO.value());
                }

                if(ncbDetailView.getWcCheck()){
                    ncbDetailView.setWcFlag(RadioValue.YES.value());
                } else {
                    ncbDetailView.setWcFlag(RadioValue.NO.value());
                }
            }

            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_EDIT, "On Edit NCB Record Information :: NCB Detail ID :: " + ncbDetailView.getId(), date, ActionResult.SUCCESS, "");
        } catch (Exception e) {
            log.error("onEditCollateralDetail Exception : {}", Util.getMessageException(e));
            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_EDIT, "On Edit NCB Record Information", date, ActionResult.FAILED, Util.getMessageException(e));
        }
    }

    public void onDeleteNcbDetail() {
        log.debug("onDeleteNcbDetail ::: selectNcbRecordItem: {}", selectNcbRecordItem);
        ncbDetailViewList.remove(selectNcbRecordItem);

        slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_DELETE, "On Delete NCB Record Information :: NCB Detail ID :: " + selectNcbRecordItem.getId(), new Date(), ActionResult.SUCCESS, "");
    }

    public void onSaveNcbRecord() {
        log.debug("onSaveNcbRecord ::: mode : {}", modeForButton);

        Date date = new Date();

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete;

        if (ncbDetailView.getAccountType().getId() != 0 && ncbDetailView.getAccountStatus().getId() != 0 && ncbDetailView.getCurrentPayment().getId() != 0 && ncbDetailView.getHistoryPayment().getId() != 0) {
            if (modeForButton != null && modeForButton == ModeForButton.ADD) {
                AccountType accountType = accountTypeDAO.findById(ncbDetailView.getAccountType().getId());
                AccountStatus accountStatus = accountStatusDAO.findById(ncbDetailView.getAccountStatus().getId());
                SettlementStatusView tdrConditionCurrent = settlementStatusTransform.transformToView(settlementStatusDAO.findById(ncbDetailView.getCurrentPayment().getId()));
                SettlementStatusView tdrConditionHistory = settlementStatusTransform.transformToView(settlementStatusDAO.findById(ncbDetailView.getHistoryPayment().getId()));

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
                ncbAdd.setTMBAccount(ncbDetailView.getTMBAccount());
                if(ncbDetailView.getTMBAccount() == RadioValue.YES.value()){
                    ncbAdd.setTmbCheck(true);
                } else {
                    ncbAdd.setTmbCheck(false);
                }

                ncbAdd.setRefinanceFlag(ncbDetailView.getRefinanceFlag());
                if(ncbDetailView.getRefinanceFlag() == RadioValue.YES.value()){
                    ncbAdd.setRefinanceCheck(true);
                } else {
                    ncbAdd.setRefinanceCheck(false);
                }

                ncbAdd.setWcFlag(ncbDetailView.getWcFlag());
                if(ncbDetailView.getWcFlag() == RadioValue.YES.value()){
                    ncbAdd.setWcCheck(true);
                } else {
                    ncbAdd.setWcCheck(false);
                }

                ncbAdd.setCanToEdit(true);
                ncbDetailViewList.add(ncbAdd);

                log.debug("add finish :: ncbAdd : {}", ncbAdd);
                log.debug("add finish :: ncbDetailViewList : {}", ncbDetailViewList);
            } else if (modeForButton != null && modeForButton == ModeForButton.EDIT) {
                log.debug("onSaveNcbRecord ::: mode : {}", modeForButton);

                AccountType accountType = accountTypeDAO.findById(ncbDetailView.getAccountType().getId());
                AccountStatus accountStatus = accountStatusDAO.findById(ncbDetailView.getAccountStatus().getId());
                SettlementStatusView tdrConditionCurrent = settlementStatusTransform.transformToView(settlementStatusDAO.findById(ncbDetailView.getCurrentPayment().getId()));
                SettlementStatusView tdrConditionHistory = settlementStatusTransform.transformToView(settlementStatusDAO.findById(ncbDetailView.getHistoryPayment().getId()));

                NCBDetailView tmpNcbDetail = new NCBDetailView();
                tmpNcbDetail.setAccountType(accountType);
                tmpNcbDetail.setAccountStatus(accountStatus);
                tmpNcbDetail.setDateOfInfo(ncbDetailView.getDateOfInfo());
                tmpNcbDetail.setAccountOpenDate(ncbDetailView.getAccountOpenDate());
                tmpNcbDetail.setLimit(ncbDetailView.getLimit());
                tmpNcbDetail.setOutstanding(ncbDetailView.getOutstanding());
                tmpNcbDetail.setInstallment(ncbDetailView.getInstallment());
                tmpNcbDetail.setDateOfDebtRestructuring(ncbDetailView.getDateOfDebtRestructuring());
                tmpNcbDetail.setCurrentPayment(tdrConditionCurrent);
                tmpNcbDetail.setHistoryPayment(tdrConditionHistory);
                tmpNcbDetail.setNoOfOutstandingPaymentIn12months(ncbDetailView.getNoOfOutstandingPaymentIn12months());
                tmpNcbDetail.setNoOfOverLimit(ncbDetailView.getNoOfOverLimit());

                //checkbox
                tmpNcbDetail.setTMBAccount(ncbDetailView.getTMBAccount());
                if(ncbDetailView.getTMBAccount() == RadioValue.YES.value()){
                    tmpNcbDetail.setTmbCheck(true);
                } else {
                    tmpNcbDetail.setTmbCheck(false);
                }

                tmpNcbDetail.setRefinanceFlag(ncbDetailView.getRefinanceFlag());
                if(ncbDetailView.getRefinanceFlag() == RadioValue.YES.value()){
                    tmpNcbDetail.setRefinanceCheck(true);
                } else {
                    tmpNcbDetail.setRefinanceCheck(false);
                }

                tmpNcbDetail.setWcFlag(ncbDetailView.getWcFlag());
                if(ncbDetailView.getWcFlag() == RadioValue.YES.value()){
                    tmpNcbDetail.setWcCheck(true);
                } else {
                    tmpNcbDetail.setWcCheck(false);
                }
                tmpNcbDetail.setCanToEdit(true);

                ncbDetailViewList.set(rowIndex, tmpNcbDetail);

                log.debug("edit finish :: tmpNcbDetail : {}", tmpNcbDetail);
                log.debug("edit finish :: ncbDetailViewList : {}", ncbDetailViewList);
            } else {
                log.debug("onSaveNcbRecord ::: Undefined modeForbutton !!");
            }
            complete = true;
        } else {
            log.debug("onSaveNcbRecord ::: validation failed.");
            complete = false;
        }
        context.addCallbackParam("functionComplete", complete);

        slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_SAVE, "On Save NCB Record Information", date, ActionResult.SUCCESS, "");
    }

    public void onCancelNcbRecord() {
        slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "On Cancel NCB Record Information", new Date(), ActionResult.SUCCESS, "");

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
    }

    // *** Function for save NCB To DB ***//
    public void onSaveNcb() {    // call transform  and then call businessControl
        Date date = new Date();
        log.debug("onSaveNcb::::");
        log.debug("ncbDetailViewList.size() ::: {} ", ncbDetailViewList.size());
        try {
            ncbInfoControl.onSaveNCBToDB(ncbInfoView, ncbDetailViewList, customerInfo, workCaseId);
            dbrControl.updateValueOfDBR(workCaseId);

            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.ncb.response.save.success");
            severity = MessageDialogSeverity.INFO.severity();

            calculationControl.calWC(workCaseId);

            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, "");

            onCreation();

        } catch(Exception ex) {
            log.error("NCB Info :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, Util.getMessageException(ex));

            messageHeader = msg.get("app.messageHeader.error");
            message = "Save ncb info data failed. Cause : " + Util.getMessageException(ex);
            severity = MessageDialogSeverity.ALERT.severity();
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancelNcbInfo() {
        log.debug("onCancelNcbInfo::::  ");
        slosAuditor.add(Screen.NCB_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");

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

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public List<SettlementStatusView> getSettlementStatusList() {
        return settlementStatusList;
    }

    public void setSettlementStatusList(List<SettlementStatusView> settlementStatusList) {
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
