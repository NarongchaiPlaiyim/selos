package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.BankAccountTypeTransform;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.util.DateTimeUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bankStatementSummary")
public class BankStatementSummary extends BaseController {
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

    //Business logic
    @Inject
    BankStmtControl bankStmtControl;
    @Inject
    DBRControl dbrControl;
    @Inject
    ExSummaryControl exSummaryControl;
    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    CustomerInfoControl customerInfoControl;

    //DAO
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    //Transform
    @Inject
    BankStmtTransform bankStmtTransform;
    @Inject
    BankAccountTypeTransform bankAccTypeTransform;

    // Variables for binding from page
    private int seasonalFlag;
    private Date expectedSubmitDate;
    private BankStmtSummaryView summaryView;
    private BankStmtView selectedBankStmtView;
    private List<BankStmtView> bankStmtSrcOfCollateralProofList;
    private Date lastThreeMonth1;
    private Date lastThreeMonth2;
    private Date lastThreeMonth3;
    private Date currentDate;
    private String currentDateDDMMYY;
    private List<BankAccountTypeView> othBankAccTypeViewList;
    private int yesValue;
    private int actionNo;

    // Variables for control
    private long workCaseId;
    private boolean isABDM_BDM;
    private boolean isTMB;
    private boolean isRetrieveSuccess;
    private boolean hasDataFromRetrieve;
    private boolean isNotAlignWithPrevData;
    private Date lastMonthDate;
    private int numberOfMonths;
    private int countRefresh;
    private static final int MAX_REFRESH_TIME = 3;

    // Variables for messages dialog
    private String messageHeader;
    private String message;
    private String severity;
    private String confirmMessageHeader;
    private String confirmMessage;

    public BankStatementSummary() {
    }

    public void preRender() {
        log.info("preRender");
        HttpSession session = FacesUtil.getSession(true);

        if (checkSession(session)) {
            log.debug("preRender ::: Found session for case.");
        }
        else {
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");
        HttpSession session = FacesUtil.getSession(true);

        if(checkSession(session)){
            log.debug("Init default value and load necessary data.");

            workCaseId = (Long)session.getAttribute("workCaseId");

            loadFieldControl(workCaseId, Screen.BANK_STATEMENT_SUMMARY);

            // Check Role (ABDM/BDM)
            int roleId = bankStmtControl.getUserRoleId();
            if (RoleValue.ABDM.id() == roleId || RoleValue.BDM.id() == roleId) {
                isABDM_BDM = true;
            }

            yesValue = RadioValue.YES.value();
            isRetrieveSuccess = false;
            hasDataFromRetrieve = false;
            isNotAlignWithPrevData = false;
            bankStmtSrcOfCollateralProofList = new ArrayList<BankStmtView>();
            othBankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.getOtherAccountTypeList());

            log.debug("Find Bank statement summary by workCaseId: {}", workCaseId);
            summaryView = bankStmtControl.getBankStmtSummaryByWorkCaseId(workCaseId);
            if (summaryView != null && summaryView.getId() != 0) {
                log.debug("Bank statement summary was found.");

                seasonalFlag = summaryView.getSeasonal();
                expectedSubmitDate = summaryView.getExpectedSubmitDate();
                currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(summaryView.getExpectedSubmitDate());

                lastThreeMonth3 = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -1);
                lastThreeMonth1 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -2);

                // calculate and provide source of collateral proof
                for (BankStmtView tmbBankStmtView : Util.safetyList(summaryView.getTmbBankStmtViewList())) {
                    bankStmtControl.calSourceOfCollateralProof(tmbBankStmtView);
                    bankStmtSrcOfCollateralProofList.add(tmbBankStmtView);
                }

                for (BankStmtView othBankStmtView : Util.safetyList(summaryView.getOthBankStmtViewList())) {
                    bankStmtControl.calSourceOfCollateralProof(othBankStmtView);
                    bankStmtSrcOfCollateralProofList.add(othBankStmtView);
                }

                // calculate total and grand total summary
                bankStmtControl.bankStmtSumTotalCalculation(summaryView, false);
            }
            else {
                log.debug("Bank statement summary was NOT found, Create new data.");
                seasonalFlag = RadioValue.NOT_SELECTED.value();
                expectedSubmitDate = getCurrentDate();
                currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());

                summaryView = new BankStmtSummaryView();
                summaryView.setSeasonal(seasonalFlag);
                summaryView.setExpectedSubmitDate(expectedSubmitDate);

                lastThreeMonth3 = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -1);
                lastThreeMonth1 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -2);
            }

            numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
            lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
            log.debug("numberOfMonths: {}, lastMonthDate: {}", numberOfMonths, lastMonthDate);
        }

    }

    public void onChangeSeasonalFlag() {
        log.debug("onChangeSeasonalFlag() seasonalFlag: {}", seasonalFlag);
        int newNumberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        boolean isNotAlign = (newNumberOfMonths != numberOfMonths);

        if (((summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) ||
             (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0)) &&
              isNotAlign)
        {
            isNotAlignWithPrevData = true;

            messageHeader = msg.get("app.messageHeader.warn");
            message = "The number of month based on seasonal flag, not aligned with Bank statement.";
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
        else {
            isNotAlignWithPrevData = false;
        }
    }

    public void onChangeExpectedSubmitDate() {
        log.debug("onChangeExpectedSubmitDate() expectedSubmitDate: {}", expectedSubmitDate);
        Date newLastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        boolean isNotAlign = !Util.compareDateByMonthAndYear(lastMonthDate, newLastMonthDate);

        if (((summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) ||
             (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0)) &&
              isNotAlign)
        {
            isNotAlignWithPrevData = true;

            messageHeader = msg.get("app.messageHeader.warn");
            message = "The number of month based on expected submission date, not aligned with Bank statement.";
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } else {
            isNotAlignWithPrevData = false;
        }
    }

    public void onRefresh() {
        log.debug("onRefresh()");
        if (!checkSelectSeasonalFlag())
            return;

        // user (ABDM/BDM) can click refresh by 3 times in Full Application step.
        if (isABDM_BDM) {
            if (countRefresh < MAX_REFRESH_TIME) {
                countRefresh += 1;
            }
            // check disable refresh button
            if(!isDisabled("refreshButton")){
                if(countRefresh >= MAX_REFRESH_TIME){
                   setDisabledValue("refreshButton", true);
                }
            }
        }

        if (workCaseId != 0) {
            // retrieve all for borrowers
            List<CustomerInfoView> customerInfoViewList = customerInfoControl.getBorrowerByWorkCase(workCaseId);
            BankStmtSummaryView retrieveResult = bankStmtControl.retrieveBankStmtInterface(customerInfoViewList, expectedSubmitDate, seasonalFlag);

            // ****************************** Start Data for Test ******************************
//            BankStmtSummaryView retrieveResult = new BankStmtSummaryView();
//            List<BankStmtView> tmbBankStmtList = new ArrayList<BankStmtView>();
//
//            BankView tmbBank = new BankView();
//            tmbBank.setCode(7);
//            tmbBank.setBankShortName(BankType.TMB.shortName());
//
//            AccountStatusView normalAccountStatus = new AccountStatusView();
//            normalAccountStatus.setId("1");
//
//            BankStmtView bankStmt1 = new BankStmtView();
//            bankStmt1.setBankView(tmbBank);
//            bankStmt1.setBranchName("Branch name 1");
//            bankStmt1.setBankAccountTypeId(1);
//            bankStmt1.setAccountNumber("1111111111");
//            bankStmt1.setAccountName("Mr.First One");
//            bankStmt1.setAccountStatusView(normalAccountStatus);
//            bankStmt1.setAccountCharacteristic(1);
//            bankStmt1.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
//            tmbBankStmtList.add(bankStmt1);
//
//            BankStmtView bankStmt2 = new BankStmtView();
//            bankStmt2.setBankView(tmbBank);
//            bankStmt2.setBranchName("Branch name 2");
//            bankStmt2.setBankAccountTypeId(2);
//            bankStmt2.setAccountNumber("2222222222");
//            bankStmt2.setAccountName("Ms.Second Two");
//            bankStmt2.setAccountStatusView(normalAccountStatus);
//            bankStmt2.setAccountCharacteristic(2);
//            bankStmt2.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
//            tmbBankStmtList.add(bankStmt2);
//
//            BankStmtView bankStmt3 = new BankStmtView();
//            bankStmt3.setBankView(tmbBank);
//            bankStmt3.setBranchName("Branch name 3");
//            bankStmt3.setBankAccountTypeId(3);
//            bankStmt3.setAccountNumber("3333333333");
//            bankStmt3.setAccountName("Miss.Third Three");
//            bankStmt3.setAccountStatusView(normalAccountStatus);
//            bankStmt3.setAccountCharacteristic(3);
//            bankStmt3.setBankStmtDetailViewList(bankStmtControl.generateBankStmtDetail(numberOfMonths, lastMonthDate));
//            tmbBankStmtList.add(bankStmt3);
//
//            retrieveResult.setTmbBankStmtViewList(tmbBankStmtList);
//
//            List<ActionStatusView> actionStatusList = new ArrayList<ActionStatusView>();
//            ActionStatusView actionStatus = new ActionStatusView();
//            actionStatus.setStatusCode(ActionResult.SUCCESS);
//            actionStatus.setStatusDesc("SUCCESS");
//            actionStatusList.add(actionStatus);
//
//            retrieveResult.setActionStatusViewList(actionStatusList);
            // ****************************** End Data for Test ******************************

            List<ActionStatusView> actionStatusViewList = retrieveResult.getActionStatusViewList();
            if (actionStatusViewList != null && actionStatusViewList.size() > 0) {
                ActionStatusView actionStatusView = actionStatusViewList.get(0);
                if (ActionResult.FAILED == actionStatusView.getStatusCode()) {
                    log.debug("Retrieve data from DWH is FAILED!");
                    isRetrieveSuccess = false;
                    hasDataFromRetrieve = false;

                    messageHeader = msg.get("app.messageHeader.error");
                    message = "Retrieve data from DWH is FAILED!, " + actionStatusView.getStatusDesc();
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }

            isRetrieveSuccess = true;
            log.debug("Retrieve data from DWH is SUCCESS.");

            if (retrieveResult.getTmbBankStmtViewList() != null && retrieveResult.getTmbBankStmtViewList().size() > 0) {
                log.debug("Has data from DWH.");
                hasDataFromRetrieve = true;

                if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
                    // delete previous TMB data
                    Iterator<BankStmtView> i = summaryView.getTmbBankStmtViewList().iterator();
                    while (i.hasNext()) {
                        BankStmtView tmbBankStmtView = i.next();
                        if (tmbBankStmtView.getId() != 0) {
                            try {
                                bankStmtControl.deleteBankStmtById(tmbBankStmtView.getId());
                            } catch (Exception e) {
                                log.debug("", e);
                                messageHeader = msg.get("app.messageHeader.error");
                                message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                                severity = MessageDialogSeverity.ALERT.severity();
                                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                                return;
                            }
                        }
                        if (bankStmtSrcOfCollateralProofList.contains(tmbBankStmtView)) {
                            bankStmtSrcOfCollateralProofList.remove(tmbBankStmtView);
                        }
                        i.remove();
                    }
                }

                if (isNotAlignWithPrevData && (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0)) {
                    // if not align with previous data -> delete all Other Bank statement
                    Iterator<BankStmtView> j = summaryView.getOthBankStmtViewList().iterator();
                    while (j.hasNext()) {
                        BankStmtView othBankStmtView = j.next();
                        if (othBankStmtView.getId() != 0) {
                            try {
                                bankStmtControl.deleteBankStmtById(othBankStmtView.getId());
                            } catch (Exception e) {
                                log.debug("", e);
                                messageHeader = msg.get("app.messageHeader.error");
                                message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                                severity = MessageDialogSeverity.ALERT.severity();
                                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                                return;
                            }
                        }
                        if (bankStmtSrcOfCollateralProofList.contains(othBankStmtView)) {
                            bankStmtSrcOfCollateralProofList.remove(othBankStmtView);
                        }
                        j.remove();
                    }
                }

                // replace previous data
                List<BankStmtView> newTMBBankStmtViewList = new ArrayList<BankStmtView>();
                BankStmtView newBankStmtView;
                for (BankStmtView originalTMBBankStmt : retrieveResult.getTmbBankStmtViewList()) {
                    newBankStmtView = bankStmtTransform.copyToNewBankStmtView(originalTMBBankStmt);
                    if (newBankStmtView.getBankStmtDetailViewList() != null && newBankStmtView.getBankStmtDetailViewList().size() > 0) {
                        int numOfMonths = newBankStmtView.getBankStmtDetailViewList().size();
                        Date tmpDate;
                        for (int i=(numOfMonths-1), j=0; i>=0; i--, j++) {
                            BankStmtDetailView bankStmtDetailView = newBankStmtView.getBankStmtDetailViewList().get(j);
                            // Replace the AsOfDate to NULL
                            if (Util.isNull(bankStmtDetailView.getAsOfDate())) {
                                tmpDate = DateTimeUtil.getOnlyDatePlusMonth(lastMonthDate, -i);
                                bankStmtDetailView.setAsOfDate(tmpDate);
                                bankStmtDetailView.setDateOfMaxBalance(DateTimeUtil.getFirstDayOfMonth(tmpDate));
                                bankStmtDetailView.setDateOfMinBalance(DateTimeUtil.getFirstDayOfMonth(tmpDate));
                            }
                        }
                    }
                    newTMBBankStmtViewList.add(newBankStmtView);
                }
                summaryView.setTmbBankStmtViewList(newTMBBankStmtViewList);

                // calculate data from DWH
                for (BankStmtView tmbBankStmtView : Util.safetyList(summaryView.getTmbBankStmtViewList())) {
                    bankStmtControl.bankStmtDetailCalculation(tmbBankStmtView, seasonalFlag);
                    bankStmtControl.calSourceOfCollateralProof(tmbBankStmtView);
                    bankStmtSrcOfCollateralProofList.add(tmbBankStmtView);
                }
                // set last three month label to Source of collateral proof column header
                lastThreeMonth3 = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -1);
                lastThreeMonth1 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -2);

                numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
                lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(summaryView.getExpectedSubmitDate());

                summaryView.setSeasonal(seasonalFlag);
                summaryView.setExpectedSubmitDate(expectedSubmitDate);

                try {
                    bankStmtControl.bankStmtSumTotalCalculation(summaryView, true);
                    summaryView = bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, 0);
                    // update related parts
                    dbrControl.updateValueOfDBR(workCaseId);
                    exSummaryControl.calForBankStmtSummary(workCaseId);
                    bizInfoSummaryControl.calByBankStatement(workCaseId);

                    onCreation();

                }
                catch (Exception e) {
                    log.debug("", e);
                    messageHeader = msg.get("app.messageHeader.error");
                    message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }

            }
            else {
                log.debug("No more data from DWH");
                hasDataFromRetrieve = false;

                messageHeader = msg.get("app.messageHeader.info");
                message = "No more data from DWH.";
                severity = MessageDialogSeverity.INFO.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        }

    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() summaryView: {}", summaryView);
        if (!checkSelectSeasonalFlag())
            return;

        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(summaryView.getExpectedSubmitDate());
        log.debug("re-calculate: numberOfMonths: {}, lastMonthDate: {}", numberOfMonths, lastMonthDate);

        summaryView.setSeasonal(seasonalFlag);
        summaryView.setExpectedSubmitDate(expectedSubmitDate);

        try {
            // clear previous data if not aligned
            if (isNotAlignWithPrevData) {
                if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
                    Iterator<BankStmtView> i = summaryView.getTmbBankStmtViewList().iterator();
                    while (i.hasNext()) {
                        BankStmtView tmbBankStmtView = i.next();
                        if (tmbBankStmtView.getId() != 0) {
                            try {
                                bankStmtControl.deleteBankStmtById(tmbBankStmtView.getId());
                            } catch (Exception e) {
                                log.debug("", e);
                                message = "Save Bank statement summary data failed!, Cause : ";
                                message += e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            }
                        }
                        i.remove();
                    }
                }

                if (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0) {
                    Iterator<BankStmtView> j = summaryView.getOthBankStmtViewList().iterator();
                    while (j.hasNext()) {
                        BankStmtView othBankStmtView = j.next();
                        if (othBankStmtView.getId() != 0) {
                            try {
                                bankStmtControl.deleteBankStmtById(othBankStmtView.getId());
                            } catch (Exception e) {
                                log.debug("", e);
                                message = "Save Bank statement summary data failed!, Cause : ";
                                message += e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            }
                        }
                        j.remove();
                    }
                }

                bankStmtSrcOfCollateralProofList = new ArrayList<BankStmtView>();

                lastThreeMonth3 = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -1);
                lastThreeMonth1 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -2);
            }

            bankStmtControl.bankStmtSumTotalCalculation(summaryView, (isRetrieveSuccess && hasDataFromRetrieve));
            bankStmtControl.saveBankStmtSumFullApp(summaryView, workCaseId);

            // update related parts
            dbrControl.updateValueOfDBR(workCaseId);
            exSummaryControl.calForBankStmtSummary(workCaseId);
            bizInfoSummaryControl.calByBankStatement(workCaseId);

            onCreation();

            messageHeader = msg.get("app.messageHeader.info");
            message = "Save Bank statement summary data success.";
            severity = MessageDialogSeverity.INFO.severity();
        }
        catch (Exception e) {
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            message = "Save Bank statement summary data failed!, Cause : ";
            message += e.getCause() != null ? e.getCause().toString() : e.getMessage();
        }

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onDeleteBankStmt() {
        log.debug("onDeleteBankStmt() selectedBankStmtView: {}", selectedBankStmtView);

        try {
            if (selectedBankStmtView.getId() != 0) {
                bankStmtControl.deleteBankStmtById(selectedBankStmtView.getId());
            }

            // remove Bank statement selected from list
            if (summaryView.getTmbBankStmtViewList().contains(selectedBankStmtView)) {
                summaryView.getTmbBankStmtViewList().remove(selectedBankStmtView);
                log.debug("selectedBankStmtView: {} has been removed from TMB list.", selectedBankStmtView);
            }
            else {
                summaryView.getOthBankStmtViewList().remove(selectedBankStmtView);
                log.debug("selectedBankStmtView: {} has been removed from Other list.", selectedBankStmtView);
            }

            // remove Bank statement from source of collateral proof
            if (bankStmtSrcOfCollateralProofList.contains(selectedBankStmtView)) {
                bankStmtSrcOfCollateralProofList.remove(selectedBankStmtView);
                log.debug("selectedBankStmtView: {} has been removed from Source of collateral proof list.");
            }

            bankStmtControl.bankStmtSumTotalCalculation(summaryView, (isRetrieveSuccess && hasDataFromRetrieve));

            messageHeader = msg.get("app.messageHeader.info");
            message = "Delete Bank statement data success.";
            severity = MessageDialogSeverity.INFO.severity();
        }
        catch (Exception e) {
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            message = "Delete Bank statement data failed!, Cause : ";
            message += e.getCause() != null ? e.getCause().toString() : e.getMessage();
        }

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onAddTMBBankStmt() {
        log.debug("onAddTMBBankStmt()");
        if (!checkSelectSeasonalFlag()) return;

        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        selectedBankStmtView = null;
        isTMB = true;

        if (isNotAlignWithPrevData) {
            if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
                Iterator<BankStmtView> i = summaryView.getTmbBankStmtViewList().iterator();
                while (i.hasNext()) {
                    BankStmtView tmbBankStmtView = i.next();
                    if (tmbBankStmtView.getId() != 0) {
                        try {
                            bankStmtControl.deleteBankStmtById(tmbBankStmtView.getId());
                        } catch (Exception e) {
                            log.debug("", e);
                            messageHeader = msg.get("app.messageHeader.error");
                            message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            severity = MessageDialogSeverity.ALERT.severity();
                            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        }
                    }
                    if (bankStmtSrcOfCollateralProofList.contains(tmbBankStmtView)) {
                        bankStmtSrcOfCollateralProofList.remove(tmbBankStmtView);
                    }
                    i.remove();
                }
            }

            if (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0) {
                Iterator<BankStmtView> j = summaryView.getOthBankStmtViewList().iterator();
                while (j.hasNext()) {
                    BankStmtView othBankStmtView = j.next();
                    if (othBankStmtView.getId() != 0) {
                        try {
                            bankStmtControl.deleteBankStmtById(othBankStmtView.getId());
                        } catch (Exception e) {
                            log.debug("", e);
                            messageHeader = msg.get("app.messageHeader.error");
                            message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            severity = MessageDialogSeverity.ALERT.severity();
                            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        }
                    }
                    if (bankStmtSrcOfCollateralProofList.contains(othBankStmtView)) {
                        bankStmtSrcOfCollateralProofList.remove(othBankStmtView);
                    }
                    j.remove();
                }
            }

            bankStmtControl.bankStmtSumTotalCalculation(summaryView, (isRetrieveSuccess && hasDataFromRetrieve));
        }

        onRedirectToBankStmtDetail();
    }

    public void onAddOtherBankStmt() {
        log.debug("onAddOtherBankStmt()");
        if (!checkSelectSeasonalFlag()) return;

        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        selectedBankStmtView = null;
        isTMB = false;

        if (isNotAlignWithPrevData) {
            if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
                Iterator<BankStmtView> i = summaryView.getTmbBankStmtViewList().iterator();
                while (i.hasNext()) {
                    BankStmtView tmbBankStmtView = i.next();
                    if (tmbBankStmtView.getId() != 0) {
                        try {
                            bankStmtControl.deleteBankStmtById(tmbBankStmtView.getId());
                        } catch (Exception e) {
                            log.debug("", e);
                            messageHeader = msg.get("app.messageHeader.error");
                            message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            severity = MessageDialogSeverity.ALERT.severity();
                            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        }
                    }
                    if (bankStmtSrcOfCollateralProofList.contains(tmbBankStmtView)) {
                        bankStmtSrcOfCollateralProofList.remove(tmbBankStmtView);
                    }
                    i.remove();
                }
            }

            if (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0) {
                Iterator<BankStmtView> j = summaryView.getOthBankStmtViewList().iterator();
                while (j.hasNext()) {
                    BankStmtView othBankStmtView = j.next();
                    if (othBankStmtView.getId() != 0) {
                        try {
                            bankStmtControl.deleteBankStmtById(othBankStmtView.getId());
                        } catch (Exception e) {
                            log.debug("", e);
                            messageHeader = msg.get("app.messageHeader.error");
                            message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
                            severity = MessageDialogSeverity.ALERT.severity();
                            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        }
                    }
                    if (bankStmtSrcOfCollateralProofList.contains(othBankStmtView)) {
                        bankStmtSrcOfCollateralProofList.remove(othBankStmtView);
                    }
                    j.remove();
                }
            }

            bankStmtControl.bankStmtSumTotalCalculation(summaryView, (isRetrieveSuccess && hasDataFromRetrieve));
        }

        onRedirectToBankStmtDetail();
    }

    public void onEditTmbBankStmt() {
        log.debug("onEditTmbBankStmt() selectedBankStmtView: {}", selectedBankStmtView);

        if (isNotAlignWithPrevData) {
            messageHeader = msg.get("app.messageHeader.info");
            message = "Can not edit this item!, Because this data is not aligned base on seasonal and expected submission date.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        if (summaryView.getTmbBankStmtViewList().contains(selectedBankStmtView) && selectedBankStmtView.getId() == 0) {
            summaryView.getTmbBankStmtViewList().remove(selectedBankStmtView);
        }

        isTMB = true;
        onRedirectToBankStmtDetail();
    }

    public void onEditOthBankStmt() {
        log.debug("onEditOthBankStmt() selectedBankStmtView: {}", selectedBankStmtView);

        if (isNotAlignWithPrevData) {
            messageHeader = msg.get("app.messageHeader.info");
            message = "Can not edit this item!, Because this data is not aligned base on seasonal and expected submission date.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        isTMB = false;
        onRedirectToBankStmtDetail();
    }

    public void onRedirectToBankStmtDetail() {
        log.debug("onRedirectToBankStmtDetail() summaryView.id: {}, seasonalFlag: {}, expectedSubmitDate: {}, isTmbBank: {}, lastMonthDate: {}, numberOfMonths: {}, selectedBankStmtView: {}",
                summaryView.getId(), seasonalFlag, expectedSubmitDate, isTMB, lastMonthDate, numberOfMonths, selectedBankStmtView);

        summaryView.setSeasonal(seasonalFlag);
        summaryView.setExpectedSubmitDate(expectedSubmitDate);

        FacesUtil.setSessionMapValue("bankStmtSumView", summaryView);
        FacesUtil.setSessionMapValue("isTmbBank", isTMB);
        FacesUtil.setSessionMapValue("lastMonthDate", lastMonthDate);
        FacesUtil.setSessionMapValue("numberOfMonths", numberOfMonths);
        FacesUtil.setSessionMapValue("selectedBankStmtView", selectedBankStmtView);

        FacesUtil.redirect("/site/bankStatementDetail.jsf");
    }

    public String getOtherBankAccType(int othBankAccTypeId) {
        String bankAccTypeName = "";
        if (othBankAccTypeViewList != null && othBankAccTypeId != 0) {
            for (BankAccountTypeView typeView : othBankAccTypeViewList) {
                if (typeView.getId() == othBankAccTypeId) {
                    bankAccTypeName = typeView.getName();
                    break;
                }
            }
        }
        return bankAccTypeName;
    }

    public void onConfirmBeforeAction() {
        log.debug("checkConfirmBeforeAction()");
        if (((summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0)
            || (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0))
            && isNotAlignWithPrevData) {

            confirmMessageHeader = msg.get("app.messageHeader.info");
            confirmMessage = "The number of month based on seasonal flag and expected submission date is not aligned with previous data. The previous data will be delete? 'Yes' or 'No'";
            RequestContext.getCurrentInstance().execute("confirmActionDlg.show()");
        }
        else {
            continueToAction();
        }
    }

    public void continueToAction() {
        log.debug("continueToAction() actionNo: {}", actionNo);
        switch (actionNo) {
            case 1 : onSaveSummary(); break;
            case 2 : onRefresh(); break;
            case 3 : onAddTMBBankStmt(); break;
            case 4 : onAddOtherBankStmt(); break;
            case 5 : onEditTmbBankStmt(); break;
            case 6 : onEditOthBankStmt(); break;
            case 7 : onDeleteBankStmt(); break;
        }
    }

    private boolean checkSelectSeasonalFlag() {
        log.debug("checkSelectSeasonalFlag()");
        if (RadioValue.NOT_SELECTED.value() == seasonalFlag) {
            messageHeader = msg.get("app.messageHeader.info");
            message = "Please, select the Seasonal.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.debug("result: false");
            return false;
        }
        log.debug("result: true");
        return true;
    }

    // ****************************** Getter/Setter ******************************
    public BankStmtSummaryView getSummaryView() {
        return summaryView;
    }

    public void setSummaryView(BankStmtSummaryView summaryView) {
        this.summaryView = summaryView;
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

    public BankStmtView getSelectedBankStmtView() {
        return selectedBankStmtView;
    }

    public void setSelectedBankStmtView(BankStmtView selectedBankStmtView) {
        this.selectedBankStmtView = selectedBankStmtView;
    }

    public List<BankStmtView> getBankStmtSrcOfCollateralProofList() {
        return bankStmtSrcOfCollateralProofList;
    }

    public void setBankStmtSrcOfCollateralProofList(List<BankStmtView> bankStmtSrcOfCollateralProofList) {
        this.bankStmtSrcOfCollateralProofList = bankStmtSrcOfCollateralProofList;
    }

    public Date getLastThreeMonth1() {
        return lastThreeMonth1;
    }

    public void setLastThreeMonth1(Date lastThreeMonth1) {
        this.lastThreeMonth1 = lastThreeMonth1;
    }

    public Date getLastThreeMonth2() {
        return lastThreeMonth2;
    }

    public void setLastThreeMonth2(Date lastThreeMonth2) {
        this.lastThreeMonth2 = lastThreeMonth2;
    }

    public Date getLastThreeMonth3() {
        return lastThreeMonth3;
    }

    public void setLastThreeMonth3(Date lastThreeMonth3) {
        this.lastThreeMonth3 = lastThreeMonth3;
    }

    public String getConfirmMessageHeader() {
        return confirmMessageHeader;
    }

    public void setConfirmMessageHeader(String confirmMessageHeader) {
        this.confirmMessageHeader = confirmMessageHeader;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public int getSeasonalFlag() {
        return seasonalFlag;
    }

    public void setSeasonalFlag(int seasonalFlag) {
        this.seasonalFlag = seasonalFlag;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateDDMMYY() {
        return currentDateDDMMYY;
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public int getYesValue() {
        return yesValue;
    }

    public void setYesValue(int yesValue) {
        this.yesValue = yesValue;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getActionNo() {
        return actionNo;
    }

    public void setActionNo(int actionNo) {
        this.actionNo = actionNo;
    }

}
