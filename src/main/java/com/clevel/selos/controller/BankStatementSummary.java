package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BankStmtView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.BankStmtTransform;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "bankStatementSummary")
public class BankStatementSummary {
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
    private Date currentDate;

    //Messages Dialog
    private String messageHeader;
    private String message;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //Dialog
    enum ModeForButton { ADD, EDIT }
    private ModeForButton modeForButton;

    public BankStatementSummary() {
    }

    private void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2);
        session.setAttribute("stepId", 1001);
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
        onLoadSummary();
    }

    public void onLoadSummary() {
        log.debug("onLoadSummary()");
        com.clevel.selos.model.db.working.BankStatementSummary bankStmtSummary = bankStmtSummaryDAO.findById(1L);
        if (bankStmtSummary != null) {
            summaryView = bankStmtTransform.getBankStmtSummaryView(bankStmtSummary);
        } else {
            summaryView = new BankStmtSummaryView();
        }
    }

    public void onSaveSummary() {
        log.debug("onSaveSummary() summaryView: {}", summaryView);
        try {
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);
            messageHeader = "Save Bank Statement Summary Success.";
            message = "Save Bank Statement Summary data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onLoadSummary();
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

    public String onLinkToAddTmbBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: true, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        passParamsToBankStmtDetail(summaryView, true);
        return "bankStatementDetail?faces-redirect=true";
    }

    public String onLinkToAddOtherBankDetail() {
        log.debug("Link to Bank statement detail with params{isTmbBank: false, seasonal: {}, expectedSubmissionDate: {}}",
                summaryView.getSeasonal(), summaryView.getExpectedSubmitDate());
        passParamsToBankStmtDetail(summaryView, false);
        return "bankStatementDetail?faces-redirect=true";
    }

    private void passParamsToBankStmtDetail(BankStmtSummaryView bankStmtSumView, boolean isTmbBank) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("bankStmtSumView", bankStmtSumView);
        map.put("isTmbBank", isTmbBank);
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
}
