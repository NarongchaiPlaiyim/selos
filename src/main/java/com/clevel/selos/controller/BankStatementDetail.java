package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.ValidationUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

    //View form
    private BankStmtView bankStmtView;
    private int numberOfPrevMonth;

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
    private long stepId;
    private String userId;

    public BankStatementDetail(){
    }

    private void preRender() {
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 101);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e){
                log.info("Exception :: {}", e);
            }
        }

        //Parameters passed from Bank statement summary page
        Flash flash = FacesUtil.getFlash();
        Map<String, Object> bankStmtSumParams = (Map<String, Object>) flash.get("bankStmtSumParams");
        if (bankStmtSumParams != null) {
            isTmbBank = (Boolean) bankStmtSumParams.get("isTmbBank");
            seasonal = (Integer) bankStmtSumParams.get("seasonal");
            expectedSubmissionDate = (Date) bankStmtSumParams.get("expectedSubmissionDate");

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
        numberOfPrevMonth = bankStmtControl.getRetrieveMonthBankStmt(seasonal);
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>(numberOfPrevMonth);
        Date date;
        for (int i=0; i<numberOfPrevMonth; i++) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(startBankStmtDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailViewList.add(bankStmtDetailView);
        }

        //Sorting asOfDate
        Collections.sort(bankStmtDetailViewList, new Comparator<BankStmtDetailView>() {
            public int compare(BankStmtDetailView o1, BankStmtDetailView o2) {
                if (o1.getAsOfDate() == null || o2.getAsOfDate() == null)
                    return 0;
                return o1.getAsOfDate().compareTo(o2.getAsOfDate());
            }
        });
        return bankStmtDetailViewList;
    }

    @PostConstruct
    public void onCreation() {
        preRender();

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

    public void onSave() {
        log.debug("onSave() bankStmtView: {}", bankStmtView);

        try {
            bankStmtControl.saveBankStmt(null, bankStmtView, workCaseId, userId);
            messageHeader = "Save Bank Statement Detail Success.";
            message = "Save Bank Statement Detail data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
        } catch (Exception e) {
            messageHeader = "Save Bank Statement Detail Failed.";
            if(e.getCause() != null) {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Bank Statement Detail data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
        }
    }

    public void onCancel() {
        log.debug("onCancel()");
        onCreation();
    }

    //Calculate method
    public void calAvgIncomeGross() {
        log.debug("calAvgIncomeGross()");
        //avgIncomeGross = SUM(grossCreditBalance) / numberOfPrevMonth
        BigDecimal sumGrossCreditBalance = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumGrossCreditBalance = sumGrossCreditBalance.add(detailView.getGrossCreditBalance());
        }
        bankStmtView.setAvgIncomeGross(sumGrossCreditBalance.divide(BigDecimal.valueOf(numberOfPrevMonth), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeGross: {} = sumGrossCreditBalance: {} / numberOfPrevMonth: {}",
                bankStmtView.getAvgIncomeGross(), sumGrossCreditBalance, numberOfPrevMonth);
        //todo: call affect calculate
    }

    public void calAvgIncomeNetBDM() {
        log.debug("calAvgIncomeNetBDM()");
        //creditAmountBDM = grossCreditBalance - excludeListBDM - chequeReturnAmount
        //avgIncomeNetBDM = SUM(creditAmountBDM) / numberOfPrevMonth
        BigDecimal sumCreditAmountBDM = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            detailView.setCreditAmountBDM(
                    detailView.getGrossCreditBalance().subtract(detailView.getExcludeListBDM()).subtract(detailView.getChequeReturnAmount())
            );
            log.debug("creditAmountBDM: {} = grossCreditBalance: {} - excludeListBDM: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountBDM(), detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount());
            sumCreditAmountBDM = sumCreditAmountBDM.add(detailView.getCreditAmountBDM());
        }
        bankStmtView.setAvgIncomeNetBDM(sumCreditAmountBDM.divide(BigDecimal.valueOf(numberOfPrevMonth), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeNetBDM: {} = sumCreditAmountBDM: {} / numberOfPrevMonth: {}",
                bankStmtView.getAvgIncomeNetBDM(), sumCreditAmountBDM, numberOfPrevMonth);

        calTimesOfAvgCreditBDM();
    }

    public void calTimesOfAvgCreditBDM() {
        log.debug("calTimesOfAvgCreditBDM()");
        //timesOfAvgCreditBDM = creditAmountBDM / avgIncomeNetBDM
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            detailView.setTimesOfAvgCreditBDM(detailView.getCreditAmountBDM().divide(bankStmtView.getAvgIncomeNetBDM(), 2, RoundingMode.HALF_UP));
            log.debug("timesOfAvgCreditBDM: {} = creditAmountBDM: {} / avgIncomeNetBDM: {}",
                    detailView.getTimesOfAvgCreditBDM(), detailView.getCreditAmountBDM(), bankStmtView.getAvgIncomeNetBDM());
        }
    }

    public void calAvgIncomeNetUW() {
        log.debug("calAvgIncomeNetUW()");
        //creditAmountUW = grossCreditBalance - excludeListUW - chequeReturnAmount
        //avgIncomeNetUW = SUM(creditAmountUW) / numberOfPrevMonth
        BigDecimal sumCreditAmountUW = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            detailView.setCreditAmountUW(
                    detailView.getGrossCreditBalance().subtract(detailView.getExcludeListUW()).subtract(detailView.getChequeReturnAmount())
            );
            log.debug("creditAmountUW: {} = grossCreditBalance: {} - excludeListUW: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountUW(), detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount());
            sumCreditAmountUW = sumCreditAmountUW.add(detailView.getCreditAmountUW());
        }
        bankStmtView.setAvgIncomeNetUW(sumCreditAmountUW.divide(BigDecimal.valueOf(numberOfPrevMonth), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeNetUW: {} = sumCreditAmountUW: {} / numberOfPrevMonth: {}",
                bankStmtView.getAvgIncomeNetUW(), sumCreditAmountUW, numberOfPrevMonth);

        calTimesOfAvgCreditUW();
    }

    public void calTimesOfAvgCreditUW() {
        log.debug("calTimesOfAvgCreditUW()");
        //timesOfAvgCreditUW = creditAmountUW / avgIncomeNetUW
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            detailView.setTimesOfAvgCreditUW(detailView.getCreditAmountUW().divide(bankStmtView.getAvgIncomeNetUW(), 2, RoundingMode.HALF_UP));
            log.debug("timesOfAvgCreditUW: {} = creditAmountUW: {} / avgIncomeNetUW: {}",
                    detailView.getTimesOfAvgCreditUW(), detailView.getCreditAmountUW(), bankStmtView.getAvgIncomeNetUW());
        }
    }

    public void calAvgWithdrawAmount() {
        log.debug("calAvgWithdrawAmount()");
        //avgWithDrawAmount = SUM(debitAmount) / numberOfPrevMonth
        BigDecimal sumDebitAmount = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumDebitAmount = sumDebitAmount.add(detailView.getDebitAmount());
        }
        bankStmtView.setAvgWithDrawAmount(sumDebitAmount.divide(BigDecimal.valueOf(numberOfPrevMonth), 2, RoundingMode.HALF_UP));
        log.debug("avgWithDrawAmount: {} = sumDebitAmount: {} / numberOfPrevMonth: {}",
                bankStmtView.getAvgWithDrawAmount(), sumDebitAmount, numberOfPrevMonth);
        //todo: call affect calculate
    }

    public void calSwingPercentPerMonth() {
        log.debug("calSwingPercentPerMonth()");
        //swingPercent = Absolute(highestBalance - lowestBalance) / overLimitAmount
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            detailView.setSwingPercent(detailView.getHighestBalance().subtract(detailView.getLowestBalance()).abs()
                    .divide(detailView.getOverLimitAmount(), 2, RoundingMode.HALF_UP));
        }
    }

    public void calAvgSwingPercent() {
        log.debug("calAvgSwingPercent()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calUtilizationPercentPerMonth() {
        log.debug("calUtilizationPercentPerMonth()");
        //utilizationPercent = (monthEndBalance * (-1)) / overLimitAmount
        //if(monthEndBalance > 0) -> utilizationPercent = 0
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            if (ValidationUtil.isValueGreaterThanZero(detailView.getMonthEndBalance())) {
                detailView.setUtilizationPercent(BigDecimal.ZERO);
            } else {
                detailView.setUtilizationPercent(detailView.getMonthEndBalance().multiply(BigDecimal.valueOf(-1))
                        .divide(detailView.getOverLimitAmount(), 2, RoundingMode.HALF_UP));
            }
        }
    }

    public void calAvgUtilizationPercent() {
        log.debug("calAvgUtilizationPercent()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calAvgGrossInflowPerLimit() {
        log.debug("calAvgGrossInflowPerLimit()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calChequeReturn() {
        log.debug("calChequeReturn()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calTrdChequeReturnAmount() {
        log.debug("calTrdChequeReturnAmount()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calOverLimitTimes() {
        log.debug("calOverLimitTimes()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    public void calOverLimitDays() {
        log.debug("calOverLimitDays()");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {

        }
    }

    //-------------------- Getter/Setter --------------------
    public BankStmtView getBankStmtView() {
        return bankStmtView;
    }

    public void setBankStmtView(BankStmtView bankStmtView) {
        this.bankStmtView = bankStmtView;
    }

    public int getNumberOfPrevMonth() {
        return numberOfPrevMonth;
    }

    public void setNumberOfPrevMonth(int numberOfPrevMonth) {
        this.numberOfPrevMonth = numberOfPrevMonth;
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
}
