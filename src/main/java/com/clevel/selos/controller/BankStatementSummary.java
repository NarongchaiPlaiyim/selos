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
import java.math.BigDecimal;
import java.util.*;

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
    @Inject
    BankDAO bankDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    RelationDAO relationDAO;

    //Transform
    @Inject
    BankStmtTransform bankStmtTransform;
    @Inject
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccTypeTransform;
    @Inject
    BankAccountStatusTransform bankAccStatusTransform;
    @Inject
    AccountStatusTransform accountStatusTransform;
    @Inject
    RelationTransform relationTransform;

    //View
    private BankStmtSummaryView summaryView;
    private BankStmtView selectBankStmt;
    private int rowIndex;
    private Date currentDate;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //Dialog
    private String messageHeader;
    private String message;

    enum ModeForButton { ADD, EDIT }
    private ModeForButton modeForButton;

    //Edit Dialog
    private List<BankView> bankViewList;
    private List<BankAccountTypeView> bankAccTypeViewList;
    private List<BankAccountTypeView> othBankAccTypeViewList;
    private List<AccountStatusView> accStatusViewList;
    private List<RelationView> relationViewList;
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
        onLoadSummary();
    }

    public void onLoadSummary() {
        log.debug("onLoadSummary()");
        //todo: change find bankStatementSummary by criteria(?)
        com.clevel.selos.model.db.working.BankStatementSummary bankStmtSummary = bankStmtSummaryDAO.findById(1l);
        if (bankStmtSummary != null) {
            summaryView = bankStmtTransform.getBankStmtSummaryView(bankStmtSummary);
        } else {
            summaryView = new BankStmtSummaryView();
            summaryView.setTmbBankStmtViewList(new ArrayList<BankStmtView>());
            summaryView.setOthBankStmtViewList(new ArrayList<BankStmtView>());
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

    public void onEditBankStmt() {
        log.debug("onEditBankStmt() selectBankStmt: {}", selectBankStmt);
        bankViewList = new ArrayList<BankView>();

        Bank tmbBank = bankDAO.getTMBBank();
        if ( tmbBank != null && (tmbBank.getCode() == selectBankStmt.getBankView().getCode()) )
            bankViewList.add(bankTransform.getBankView(tmbBank));
        else
            bankViewList = bankTransform.getBankViewList(bankDAO.getListExcludeTMB());

        bankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        othBankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        accStatusViewList = accountStatusTransform.transformToViewList(accountStatusDAO.findAll());
        relationViewList = relationTransform.transformToViewList(relationDAO.findAll());

        numberOfMonths = selectBankStmt.getBankStmtDetailViewList().size();
    }

    public void onSaveBankStmt() {
        log.debug("onSaveBankStmt() selectBankStmt: {}", selectBankStmt);
        int index;
        if (summaryView.getTmbBankStmtViewList().contains(selectBankStmt)) {
            index = summaryView.getTmbBankStmtViewList().indexOf(selectBankStmt);
            summaryView.getTmbBankStmtViewList().set(index, selectBankStmt);
        } else {
            index = summaryView.getOthBankStmtViewList().indexOf(selectBankStmt);
            summaryView.getOthBankStmtViewList().set(index, selectBankStmt);
        }

        try {
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);

            messageHeader = "Save Bank Statement Success.";
            message = "Save Bank Statement data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onLoadSummary();
        } catch (Exception e) {
            messageHeader = "Save Bank Statement Failed.";
            if (e.getCause() != null) {
                message = "Save Bank Statement data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onDeleteBankStmt() {
        log.debug("onDeleteBankStmt() selectBankStmt: {}", selectBankStmt);
        if (summaryView.getTmbBankStmtViewList().contains(selectBankStmt)) {
            summaryView.getTmbBankStmtViewList().remove(selectBankStmt);
        } else {
            summaryView.getOthBankStmtViewList().remove(selectBankStmt);
        }

        try {
            bankStmtControl.deleteBankStmt(selectBankStmt.getId());
            bankStmtControl.saveBankStmtSummary(summaryView, workCaseId, workCasePrescreenId, userId);

            messageHeader = "Delete Bank Statement Success.";
            message = "Delete Bank Statement success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onLoadSummary();
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

    //********** Calculate Average **********//
    public void calAverages() {
        log.debug("calAverages()");
        bankStmtControl.calAverages(selectBankStmt, numberOfMonths);
    }

    public void calAvgWithdrawAmount() {
        log.debug("calAvgWithdrawAmount()");
        bankStmtControl.calAvgWithdrawAmount(selectBankStmt, numberOfMonths);
    }

    public void calAvgGrossInflowPerLimit() {
        log.debug("calAvgGrossInflowPerLimit()");
        bankStmtControl.calAvgGrossInflowPerLimit(selectBankStmt, numberOfMonths);
    }

    //********** Swing & Utilization **********//
    public void calSwingAndUtilization() {
        log.debug("calSwingAndUtilization()");
        bankStmtControl.calSwingAndUtilization(selectBankStmt);
    }

    //********** SUM , MAX **********//
    public void sumNumberOfChequeReturn() {
        log.debug("sumNumberOfChequeReturn()");
        int sumNumOfChequeReturn = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(selectBankStmt.getBankStmtDetailViewList())) {
            sumNumOfChequeReturn += detailView.getNumberOfChequeReturn();
        }
        selectBankStmt.setChequeReturn(BigDecimal.valueOf(sumNumOfChequeReturn));
    }

    public void sumOverLimitTimes() {
        log.debug("sumOverLimitTimes()");
        int sumOverLimitTimes = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(selectBankStmt.getBankStmtDetailViewList())) {
            sumOverLimitTimes += detailView.getOverLimitTimes();
        }
        selectBankStmt.setOverLimitTimes(BigDecimal.valueOf(sumOverLimitTimes));
    }

    public void maxOverLimitDays() {
        log.debug("maxOverLimitDays()");
        int maxOverLimitDays = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(selectBankStmt.getBankStmtDetailViewList())) {
            if (detailView.getOverLimitDays() > maxOverLimitDays)
                maxOverLimitDays = detailView.getOverLimitDays();
        }
        selectBankStmt.setOverLimitDays(BigDecimal.valueOf(maxOverLimitDays));
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

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public BankStmtView getSelectBankStmt() {
        return selectBankStmt;
    }

    public void setSelectBankStmt(BankStmtView selectBankStmt) {
        this.selectBankStmt = selectBankStmt;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<BankView> getBankViewList() {
        return bankViewList;
    }

    public void setBankViewList(List<BankView> bankViewList) {
        this.bankViewList = bankViewList;
    }

    public List<BankAccountTypeView> getBankAccTypeViewList() {
        return bankAccTypeViewList;
    }

    public void setBankAccTypeViewList(List<BankAccountTypeView> bankAccTypeViewList) {
        this.bankAccTypeViewList = bankAccTypeViewList;
    }

    public List<BankAccountTypeView> getOthBankAccTypeViewList() {
        return othBankAccTypeViewList;
    }

    public void setOthBankAccTypeViewList(List<BankAccountTypeView> othBankAccTypeViewList) {
        this.othBankAccTypeViewList = othBankAccTypeViewList;
    }

    public List<AccountStatusView> getAccStatusViewList() {
        return accStatusViewList;
    }

    public void setAccStatusViewList(List<AccountStatusView> accStatusViewList) {
        this.accStatusViewList = accStatusViewList;
    }

    public List<RelationView> getRelationViewList() {
        return relationViewList;
    }

    public void setRelationViewList(List<RelationView> relationViewList) {
        this.relationViewList = relationViewList;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }
}
