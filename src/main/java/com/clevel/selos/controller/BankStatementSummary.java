package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.RelationDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
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
    private Date currentDate;
    private List<BankStmtView> bankStmtSrcOfCollateralProofList;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //Dialog
    private String messageHeader;
    private String message;

    // init on click refresh button
    private Date lastMonthDate;
    private int numberOfMonths;

    public BankStatementSummary() {
    }

    private void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCasePrescreenId = 21;
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
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

        // todo: change find bankStatementSummary by criteria(?)
        summaryView = bankStmtTransform.getBankStmtSummaryView(bankStmtSummaryDAO.findById(1l));
        bankStmtSrcOfCollateralProofList = new ArrayList<BankStmtView>();
        for (BankStmtView tmbBankStmtView : summaryView.getTmbBankStmtViewList()) {
            bankStmtSrcOfCollateralProofList.add(tmbBankStmtView);
        }
        for (BankStmtView othBankStmtView : summaryView.getOthBankStmtViewList()) {
            bankStmtSrcOfCollateralProofList.add(othBankStmtView);
        }
    }

    public void onRefresh() {
        log.debug("onRefresh()");
        lastMonthDate = bankStmtControl.getLastMonthDateBankStmt(summaryView.getExpectedSubmitDate());
        numberOfMonths = bankStmtControl.getNumberOfMonthsBankStmt(summaryView.getSeasonal());
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() summaryView: {}", summaryView);
        try {
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);

            messageHeader = "Save Bank Statement Summary Success.";
            message = "Save Bank Statement Summary data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            // todo: call onRefresh() ?
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
        passParamsToBankStmtDetail(summaryView, true, null);
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddOthBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: false, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
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

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
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
}
