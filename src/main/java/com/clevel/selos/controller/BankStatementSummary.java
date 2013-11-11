package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
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

    //DAO
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;

    //Transform
    @Inject
    BankStmtTransform bankStmtTransform;

    //View
    private BankStmtSummaryView summaryView;
    private BankStmtView selectedBankStmtView;
    private List<BankStmtView> bankStmtSrcOfCollateralProofList;
    private Date lastThreeMonth1;
    private Date lastThreeMonth2;
    private Date lastThreeMonth3;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //Dialog
    private String messageHeader;
    private String message;

    private Date lastMonthDate;
    private int numberOfMonths;
    private boolean isABDM_BDM;
    private int countRefresh;

    public BankStatementSummary() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCasePrescreenId = 21;
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
            // check user (ABDM/BDM)
            isABDM_BDM = bankStmtControl.isBDMUser();
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

        // todo: check connection to DWH, if the connection to DWH is down, SE-LOS must alert an error message on screen.

        // todo: retrieve Bank statement summary from DHW
        summaryView = bankStmtTransform.getBankStmtSummaryView(bankStmtSummaryDAO.findById(1l));

        // calculates and generate last three months
        Date theLastMonthDate = bankStmtControl.getLastMonthDateBankStmt(summaryView.getExpectedSubmitDate());
        if (summaryView.getTmbBankStmtViewList() != null && summaryView.getTmbBankStmtViewList().size() > 0) {
            // TMB Bank
            BankStmtView bankStmtView = summaryView.getTmbBankStmtViewList().get(0);
            List<BankStmtDetailView> detailViews = bankStmtControl.getLastThreeMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList());
            if (detailViews != null && detailViews.size() > 0) {
                lastThreeMonth1 = detailViews.get(0).getAsOfDate();
                lastThreeMonth2 = detailViews.get(1).getAsOfDate();
                lastThreeMonth3 = detailViews.get(2).getAsOfDate();
            }
        }
        else if (summaryView.getOthBankStmtViewList() != null && summaryView.getOthBankStmtViewList().size() > 0) {
            // Other Bank
            BankStmtView bankStmtView = summaryView.getOthBankStmtViewList().get(0);
            List<BankStmtDetailView> detailViews = bankStmtControl.getLastThreeMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList());
            if (detailViews != null && detailViews.size() > 0) {
                lastThreeMonth1 = detailViews.get(0).getAsOfDate();
                lastThreeMonth2 = detailViews.get(1).getAsOfDate();
                lastThreeMonth3 = detailViews.get(2).getAsOfDate();
            }
        }
        else {
            // Anything else retrieve from expectedSubmitDate (T - x)
            lastThreeMonth1 = theLastMonthDate; // Ex. April 2013
            lastThreeMonth2 = DateTimeUtil.getOnlyDatePlusMonth(theLastMonthDate, 1); // Ex. May 2013
            lastThreeMonth3 = DateTimeUtil.getOnlyDatePlusMonth(theLastMonthDate, 2); // Ex
        }

        // count Source of Collateral Proof from All Bank statement
        BigDecimal sumAvgOsBalance = BigDecimal.ZERO;
        bankStmtSrcOfCollateralProofList = new ArrayList<BankStmtView>();
        for (BankStmtView tmbBankStmtView : summaryView.getTmbBankStmtViewList()) {
            bankStmtControl.calSourceOfCollateralProof(tmbBankStmtView);
            sumAvgOsBalance = sumAvgOsBalance.add(tmbBankStmtView.getAvgOSBalanceAmount());

            bankStmtSrcOfCollateralProofList.add(tmbBankStmtView);
        }
        for (BankStmtView othBankStmtView : summaryView.getOthBankStmtViewList()) {
            bankStmtControl.calSourceOfCollateralProof(othBankStmtView);
            sumAvgOsBalance = sumAvgOsBalance.add(othBankStmtView.getAvgOSBalanceAmount());

            bankStmtSrcOfCollateralProofList.add(othBankStmtView);
        }
        summaryView.setGrdTotalAvgOSBalanceAmount(sumAvgOsBalance);
        // recalculate Summary
        bankStmtControl.bankStmtSumTotalCalculation(summaryView);
    }

    public void onRefresh() {
        log.debug("onRefresh()");
        // user (ABDM/BDM) can click refresh by 3 times.
        if (isABDM_BDM) {
            if (countRefresh < 3) {
                countRefresh++;
            } else {
                messageHeader = "Can click refresh by 3 times.";
                message = "Can click refresh by 3 times.";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
        }
        // todo: check if(lastMonthDate from expectedSubmitDate != lastMonthDate from Bank statement detail)

        // check for seasonal flag & expected submission date
        // calculate for the last month & a number of months to be retrieved the bank statement detail
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(summaryView.getExpectedSubmitDate());
        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(summaryView.getSeasonal());

        // todo: retrieve new TMB data (all fields) to replace previous data
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() summaryView: {}", summaryView);
        try {
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);

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

        try {
            bankStmtControl.deleteBankStmt(selectedBankStmtView.getId());
            // todo: re-calculate summary and save ?

            messageHeader = "Delete Bank Statement Success.";
            message = "Delete Bank Statement success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            // todo: call onRefresh() ?
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

    public String onEditTmbBankStmt() {
        log.debug("onEditTmbBankStmt() selectedBankStmtView: {}", selectedBankStmtView);
        passParamsToBankStmtDetail(summaryView, true, selectedBankStmtView);
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onEditOthBankStmt() {
        log.debug("onEditOthBankStmt() selectedBankStmtView: {}", selectedBankStmtView);
        passParamsToBankStmtDetail(summaryView, false, selectedBankStmtView);
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddTmbBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: true, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        // todo: lastMonthDate and numberOfMonths will be aligned with TMB Bank statement
        passParamsToBankStmtDetail(summaryView, true, null);
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddOthBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: false, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        // todo: lastMonthDate and numberOfMonths will be aligned with TMB Bank statement
        passParamsToBankStmtDetail(summaryView, false, null);
        return "bankStatementDetail?faces-redirect=true";
    }

    private void passParamsToBankStmtDetail(BankStmtSummaryView bankStmtSumView, boolean isTmbBank, BankStmtView bankStmtView) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bankStmtSumView", bankStmtSumView);
        map.put("isTmbBank", isTmbBank);
        map.put("lastMonthDate", lastMonthDate);
        map.put("numberOfMonths", numberOfMonths);
        map.put("selectedBankStmtView", bankStmtView);
        FacesUtil.getFlash().put("bankStmtSumParams", map);
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

    public int getCountRefresh() {
        return countRefresh;
    }

    public void setCountRefresh(int countRefresh) {
        this.countRefresh = countRefresh;
    }
}
