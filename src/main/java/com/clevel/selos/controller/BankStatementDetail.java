package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.RelationDAO;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
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
import java.math.RoundingMode;
import java.util.*;

@ViewScoped
@ManagedBean(name = "bankStatementDetail")
public class BankStatementDetail implements Serializable {
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
    BankDAO bankDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;
    @Inject
    RelationDAO relationDAO;

    //Transform
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

    //Parameters from Bank statement summary
    private boolean isTmbBank;
    private int seasonal;
    private Date expectedSubmissionDate;
    private BankStmtSummaryView summaryView;

    //View form
    private BankStmtView bankStmtView;
    private int numberOfMonths;
    private Date currentDate;

    //Select items list
    private List<BankView> bankViewList;
    private List<BankAccountTypeView> bankAccTypeViewList;
    private List<BankAccountTypeView> othBankAccTypeViewList;
    private List<AccountStatusView> accStatusViewList;
    private List<RelationView> relationViewList;

    //Messages Dialog
    private String messageHeader;
    private String message;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    public BankStatementDetail() {
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

        //Parameters passed from Bank statement summary page
        Flash flash = FacesUtil.getFlash();
        Map<String, Object> bankStmtSumParams = (Map<String, Object>) flash.get("bankStmtSumParams");
        if (bankStmtSumParams != null) {
            isTmbBank = (Boolean) bankStmtSumParams.get("isTmbBank");
            summaryView = (BankStmtSummaryView) bankStmtSumParams.get("bankStmtSumView");
            if (summaryView != null) {
                seasonal = summaryView.getSeasonal();
                expectedSubmissionDate = summaryView.getExpectedSubmitDate();
            }
            log.debug("onCreation() bankStmtSumParams:{isTmbBank: {}, seasonal: {}, expectedSubmissionDate: {}}",
                    isTmbBank, seasonal, expectedSubmissionDate);
        } else {
            //Return to Bank statement summary if parameter is null
            FacesUtil.redirect("/site/bankStatementSummary.jsf");
            return;
        }
    }

    private List<BankStmtDetailView> initDetailList() {
        List<BankStmtDetailView> bankStmtDetailViewList;

        Date startBankStmtDate = bankStmtControl.getStartBankStmtDate(expectedSubmissionDate);
        numberOfMonths = bankStmtControl.getRetrieveMonthBankStmt(seasonal);
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>(numberOfMonths);
        Date date;
        for (int i = 0; i < numberOfMonths; i++) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(startBankStmtDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailViewList.add(bankStmtDetailView);
        }

        //Ascending: asOfDate
        Collections.sort(bankStmtDetailViewList, new Comparator<BankStmtDetailView>() {
            public int compare(BankStmtDetailView o1, BankStmtDetailView o2) {
                if (o1.getAsOfDate() == null || o2.getAsOfDate() == null)
                    return 0;
                return o1.getAsOfDate().compareTo(o2.getAsOfDate());
            }
        });
        return bankStmtDetailViewList;
    }

    private void initNewForm() {
        bankStmtView = new BankStmtView();
        bankStmtView.setBankStmtDetailViewList(initDetailList());

        //init items list
        bankViewList = new ArrayList<BankView>();
        if (isTmbBank)
            bankViewList.add(bankTransform.getBankView(bankDAO.getTMBBank()));
        else
            bankViewList = bankTransform.getBankViewList(bankDAO.getListExcludeTMB());

        bankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        othBankAccTypeViewList = bankAccTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findAll());
        accStatusViewList = accountStatusTransform.transformToViewList(accountStatusDAO.findAll());
        relationViewList = relationTransform.transformToViewList(relationDAO.findAll());
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        initNewForm();
    }

    public void onSave() {
        log.debug("onSave() bankStmtView: {}", bankStmtView);
        try {
            bankStmtControl.saveBankStmt(summaryView, bankStmtView, workCaseId, workCasePrescreenId, userId);
            messageHeader = "Save Bank Statement Detail Success.";
            message = "Save Bank Statement Detail data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            initNewForm();
        } catch (Exception e) {
            messageHeader = "Save Bank Statement Detail Failed.";
            if (e.getCause() != null) {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancel() {
        log.debug("onCancel()");
        initNewForm();
    }

    //********** Calculate Average **********//
    public void calAverages() {
        log.debug("calAverages()");
        bankStmtControl.calAverages(bankStmtView, numberOfMonths);
    }

    public void calAvgWithdrawAmount() {
        log.debug("calAvgWithdrawAmount()");
        bankStmtControl.calAvgWithdrawAmount(bankStmtView, numberOfMonths);
    }

    public void calAvgGrossInflowPerLimit() {
        log.debug("calAvgGrossInflowPerLimit()");
        bankStmtControl.calAvgGrossInflowPerLimit(bankStmtView, numberOfMonths);
    }

    //********** Swing & Utilization **********//
    public void calSwingAndUtilization() {
        log.debug("calSwingAndUtilization()");
        bankStmtControl.calSwingAndUtilization(bankStmtView);
    }

    //********** SUM , MAX **********//
    public void sumNumberOfChequeReturn() {
        log.debug("sumNumberOfChequeReturn()");
        int sumNumOfChequeReturn = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            sumNumOfChequeReturn += detailView.getNumberOfChequeReturn();
        }
        bankStmtView.setChequeReturn(BigDecimal.valueOf(sumNumOfChequeReturn));
    }

    public void sumOverLimitTimes() {
        log.debug("sumOverLimitTimes()");
        int sumOverLimitTimes = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            sumOverLimitTimes += detailView.getOverLimitTimes();
        }
        bankStmtView.setOverLimitTimes(BigDecimal.valueOf(sumOverLimitTimes));
    }

    public void maxOverLimitDays() {
        log.debug("maxOverLimitDays()");
        int maxOverLimitDays = 0;
        for (BankStmtDetailView detailView :
                bankStmtControl.getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            if (detailView.getOverLimitDays() > maxOverLimitDays)
                maxOverLimitDays = detailView.getOverLimitDays();
        }
        bankStmtView.setOverLimitDays(BigDecimal.valueOf(maxOverLimitDays));
    }

    //-------------------- Getter/Setter --------------------
    public BankStmtView getBankStmtView() {
        return bankStmtView;
    }

    public void setBankStmtView(BankStmtView bankStmtView) {
        this.bankStmtView = bankStmtView;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
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

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
