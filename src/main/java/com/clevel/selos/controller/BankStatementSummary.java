package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;

@ViewScoped
@ManagedBean(name = "bankStatementSummary")
public class BankStatementSummary implements Serializable {
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
    PrescreenBusinessControl prescreenBusinessControl;
    @Inject
    DBRControl dbrControl;
    @Inject
    ExSummaryControl exSummaryControl;

    //DAO
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    //Transform
    @Inject
    BankStmtTransform bankStmtTransform;

    //View
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
    private int yesValue;

    //Session
    private long workCaseId;
    private long workCasePreScreenId;

    //Message Dialog
    private String messageHeader;
    private String message;

    //Confirm Dialog
    private String confirmMessageHeader;
    private String confirmMessage;

    private Date lastMonthDate;
    private int numberOfMonths;
    private int maxRefreshTime = 3;
    private boolean isABDM_BDM;
    private boolean disableRefresh;
    private boolean dwhIsDown;
    private boolean isTMB;

    public BankStatementSummary() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        /*HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);*/

        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

            if (session.getAttribute("workCasePreScreenId") != null) {
                workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            }

            // Check Role (ABDM/BDM)
            int roleId = bankStmtControl.getUserRoleId();
            if (RoleValue.ABDM.id() == roleId || RoleValue.BDM.id() == roleId) {
                isABDM_BDM = true;
            }

        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e) {
                log.info("Exception :: {}", e);
            }
        }

    }

    @PostConstruct
    public void onCreation() {
        preRender();

        yesValue = RadioValue.YES.value();

        //retrieveBankStmtFromDWH();
        summaryView = bankStmtControl.getBankStmtSummaryByWorkCaseId(workCaseId);
        if (summaryView != null && summaryView.getId() != 0) {
            seasonalFlag = summaryView.getSeasonal();
            expectedSubmitDate = summaryView.getExpectedSubmitDate();
            currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(summaryView.getExpectedSubmitDate());
            // Set Summary Colors
            bankStmtControl.setSummaryColor(summaryView, workCaseId);

            Date[] threeMonthsOfSrcOfColl = bankStmtControl.getSourceOfCollateralMonths(summaryView);
            if (threeMonthsOfSrcOfColl.length == 3) {
                lastThreeMonth1 = threeMonthsOfSrcOfColl[0];
                lastThreeMonth2 = threeMonthsOfSrcOfColl[1];
                lastThreeMonth3 = threeMonthsOfSrcOfColl[2];
            }
            else {
                lastThreeMonth3 = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
                lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -1);
                lastThreeMonth1 = DateTimeUtil.getOnlyDatePlusMonth(lastThreeMonth3, -2);
            }

            provideSOCPAndCalSummary();
        }
        else {
            // Create new Bank statement summary
            log.debug("Create new Bank statement summary");
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

        checkDisableRefreshButton();
    }

    private void retrieveBankStmtFromDWH() {
//        List<CustomerInfoView> customerInfoViewList = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
//        summaryView = bankStmtControl.retrieveBankStmtInterface(customerInfoViewList, expectedSubmitDate, seasonalFlag);
//        List<ActionStatusView> actionStatusViewList = summaryView.getActionStatusViewList();
//        if (actionStatusViewList != null && actionStatusViewList.size() > 0) {
//            ActionStatusView actionStatusView = actionStatusViewList.get(0);
//            if (ActionResult.FAILED == actionStatusView.getStatusCode()) {
//                dwhIsDown = true;
//                messageHeader = "Connection to DWH is down!";
//                message = actionStatusView.getStatusDesc();
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            } else {
//                dwhIsDown = false;
//            }
//        }
    }

    private void provideSOCPAndCalSummary() {
        if (summaryView == null || summaryView.getId() == 0) return;
        // provide Source of Collateral Proof from all Bank statement
        provideSrcOfCollateralProofList();
        // calculate total & grand total summary
        bankStmtControl.bankStmtSumTotalCalculation(summaryView, false);
    }

    private void provideSrcOfCollateralProofList() {
        log.debug("provideSrcOfCollateralProofList()");
        bankStmtSrcOfCollateralProofList = new ArrayList<BankStmtView>();
        for (BankStmtView tmbBankStmtView : Util.safetyList(summaryView.getTmbBankStmtViewList())) {
            bankStmtControl.calSourceOfCollateralProof(tmbBankStmtView);
            bankStmtSrcOfCollateralProofList.add(tmbBankStmtView);
        }
        for (BankStmtView othBankStmtView : Util.safetyList(summaryView.getOthBankStmtViewList())) {
            bankStmtControl.calSourceOfCollateralProof(othBankStmtView);
            bankStmtSrcOfCollateralProofList.add(othBankStmtView);
        }
    }

    private void checkDisableRefreshButton() {
        disableRefresh = summaryView.getCountRefresh() >= maxRefreshTime;
    }

    public void onRefresh() {
        log.debug("onRefresh()");
        // user (ABDM/BDM) can click refresh by 3 times in Full Application step.
        if (isABDM_BDM) {
            if (summaryView.getCountRefresh() < maxRefreshTime) {
                summaryView.setCountRefresh(summaryView.getCountRefresh() + 1);
            }
            checkDisableRefreshButton();
        }

        // calculate for the last month & a number of months to be retrieved the bank statement detail
        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        log.debug("numberOfMonths: {}, lastMonthDate: {}", numberOfMonths, lastMonthDate);

        // retrieve new TMB data (all fields) to replace previous data
//        retrieveBankStmtFromDWH();
        BankStmtSummaryView resultSummaryView = bankStmtControl.getBankStmtSummaryByWorkCaseId(workCaseId);
        if (resultSummaryView != null && resultSummaryView.getId() != 0) {
            summaryView = new Cloner().deepClone(resultSummaryView);
            provideSOCPAndCalSummary();
        }
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() summaryView: {}", summaryView);
        if (!checkSelectSeasonalFlag())
            return;

        // set Seasonal and Expected submit date, if modified
        summaryView.setSeasonal(seasonalFlag);
        summaryView.setExpectedSubmitDate(expectedSubmitDate);

        try {
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, 0);
            dbrControl.updateValueOfDBR(workCaseId);
            exSummaryControl.calForBankStmtSummary(workCaseId);

            onCreation();

            messageHeader = "Save Bank Statement Summary Success.";
            message = "Save Bank Statement Summary data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        } catch (Exception e) {
            messageHeader = "Save Bank Statement Summary Failed.";
            if (e.getCause() != null) {
                message = "Save Bank Statement Summary data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement Summary data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onDeleteBankStmt() {
        log.debug("onDeleteBankStmt() selectedBankStmtView: {}", selectedBankStmtView);
        if (summaryView.getTmbBankStmtViewList().contains(selectedBankStmtView)) {
            summaryView.getTmbBankStmtViewList().remove(selectedBankStmtView);
        } else {
            summaryView.getOthBankStmtViewList().remove(selectedBankStmtView);
        }
        // re-provide Source of collateral proof
        provideSrcOfCollateralProofList();
        // re-calculate Summary
        bankStmtControl.bankStmtSumTotalCalculation(summaryView, false);

        try {
            bankStmtControl.deleteBankStmt(selectedBankStmtView.getId());

            messageHeader = "Delete Bank Statement Success.";
            message = "Delete Bank Statement success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception e) {
            messageHeader = "Delete Bank Statement Failed.";
            if (e.getCause() != null) {
                message = "Delete Bank Statement failed. Cause : " + e.getCause().toString();
            } else {
                message = "Delete Bank Statement failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onEditTmbBankStmt() {
        log.debug("onEditTmbBankStmt() selectedBankStmtView: {}", selectedBankStmtView);
        isTMB = true;
        onRedirectToBankStmtDetail();
    }

    public void onEditOthBankStmt() {
        log.debug("onEditOthBankStmt() selectedBankStmtView: {}", selectedBankStmtView);
        isTMB = false;
        onRedirectToBankStmtDetail();
    }

    public void onClickAddTMBBankStmt() {
        log.debug("onClickAddTMBBankStmt()");
        if (!checkSelectSeasonalFlag()) return;

        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        log.debug("numberOfMonths: {}, lastMonthDate: {}", numberOfMonths, lastMonthDate);

        selectedBankStmtView = null;
        isTMB = true;

        if (!checkConfirmToAddBankStmt()) return;

        onRedirectToBankStmtDetail();
    }

    public void onClickAddOtherBankStmt() {
        log.debug("onClickAddOtherBankStmt()");
        if (!checkSelectSeasonalFlag()) return;

        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
        log.debug("numberOfMonths: {}, lastMonthDate: {}", numberOfMonths, lastMonthDate);

        selectedBankStmtView = null;
        isTMB = false;

        if (!checkConfirmToAddBankStmt()) return;

        onRedirectToBankStmtDetail();
    }

    public void onRedirectToBankStmtDetail() {
        log.debug("onRedirectToBankStmtDetail()");
        passParamsToBankStmtDetail();
        FacesUtil.redirect("/site/bankStatementDetail.jsf");
    }

    private boolean checkSelectSeasonalFlag() {
        log.debug("checkSelectSeasonalFlag()");
        if (RadioValue.NOT_SELECTED.value() == seasonalFlag) {
            messageHeader = "Message dialog";
            message = "Please, select the Seasonal";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.debug("result: false");
            return false;
        }
        log.debug("result: true");
        return true;
    }

    private boolean checkConfirmToAddBankStmt() {
        // number of months based on expected submission date and seasonal flag, which have to be aligned with TMB Bank Statement
        if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
            List<BankStmtDetailView> detailViewList = summaryView.getTmbBankStmtViewList().get(0).getBankStmtDetailViewList();
            int numberOfMonthsFromTMB = detailViewList.size();
            int numberOfMonthsFromView = bankStmtControl.getNumberOfMonthsBankStmt(seasonalFlag);
            // Check number of months
            if (numberOfMonthsFromTMB != numberOfMonthsFromView) {
                confirmMessageHeader = "Confirm message dialog";
                confirmMessage = "Number of months are not aligned with TMB Bank Statement!";
                RequestContext.getCurrentInstance().execute("confirmChangeScreenBankStmtDlg.show()");
                log.debug("result: false");
                return false;
            }

            bankStmtControl.sortAsOfDateBankStmtDetails(detailViewList, SortOrder.ASCENDING);
            Date dateFromTMB = detailViewList.get(numberOfMonthsFromTMB - 1).getAsOfDate();
            Date dateFromView = bankStmtControl.getLastMonthDateBankStmt(expectedSubmitDate);
            int lastMonthTMB = DateTimeUtil.getMonthOfDate(dateFromTMB);
            int lastMonthFromView = DateTimeUtil.getMonthOfDate(dateFromView);
            int yearOfLastMonthTMB = DateTimeUtil.getYearOfDate(dateFromTMB);
            int yearOfLastMonthView = DateTimeUtil.getYearOfDate(dateFromView);
            // Check last month Bank Statement
            if (lastMonthTMB != lastMonthFromView || yearOfLastMonthTMB != yearOfLastMonthView) {
                confirmMessageHeader = "Confirm message dialog";
                confirmMessage = "The last of month is not mapped with TMB Bank Statement!";
                RequestContext.getCurrentInstance().execute("confirmChangeScreenBankStmtDlg.show()");
                log.debug("result: false");
                return false;
            }
        }
        log.debug("result: true");
        return true;
    }

    private void passParamsToBankStmtDetail() {
        log.debug("passParamsToBankStmtDetail() summaryView.id: {}, seasonalFlag: {}, expectedSubmitDate: {}, isTmbBank: {}, lastMonthDate: {}, numberOfMonths: {}, selectedBankStmtView: {}",
                summaryView.getId(), seasonalFlag, expectedSubmitDate, isTMB, lastMonthDate, numberOfMonths, selectedBankStmtView);

        summaryView.setSeasonal(seasonalFlag);
        summaryView.setExpectedSubmitDate(expectedSubmitDate);

        FacesUtil.setSessionMapValue("bankStmtSumView", summaryView);
        FacesUtil.setSessionMapValue("isTmbBank", isTMB);
        FacesUtil.setSessionMapValue("lastMonthDate", lastMonthDate);
        FacesUtil.setSessionMapValue("numberOfMonths", numberOfMonths);
        FacesUtil.setSessionMapValue("selectedBankStmtView", selectedBankStmtView);
    }

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

    public boolean isDisableRefresh() {
        return disableRefresh;
    }

    public void setDisableRefresh(boolean disableRefresh) {
        this.disableRefresh = disableRefresh;
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

    public boolean isDwhIsDown() {
        return dwhIsDown;
    }

    public void setDwhIsDown(boolean dwhIsDown) {
        this.dwhIsDown = dwhIsDown;
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
}
