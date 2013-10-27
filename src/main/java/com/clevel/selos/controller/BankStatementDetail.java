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
    private int numberOfMonths;

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
        numberOfMonths = bankStmtControl.getRetrieveMonthBankStmt(seasonal);
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>(numberOfMonths);
        Date date;
        for (int i=0; i<numberOfMonths; i++) {
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
            bankStmtControl.saveBankStmt(null, bankStmtView, workCaseId, userId);
            messageHeader = "Save Bank Statement Detail Success.";
            message = "Save Bank Statement Detail data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            initNewForm();
        } catch (Exception e) {
            messageHeader = "Save Bank Statement Detail Failed.";
            if(e.getCause() != null) {
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

        // avgIncomeGross = SUM(grossCreditBalance) / numberOfMonths
        // avgIncomeNetBDM = SUM(creditAmountBDM) / numberOfMonths
        // avgIncomeNetUW = SUM(creditAmountUW) / numberOfMonths

        BigDecimal sumGrossCreditBalance = BigDecimal.ZERO;
        BigDecimal sumCreditAmountBDM = BigDecimal.ZERO;
        BigDecimal sumCreditAmountUW = BigDecimal.ZERO;

        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumGrossCreditBalance = sumGrossCreditBalance.add(detailView.getGrossCreditBalance());

            // creditAmountBDM = grossCreditBalance - excludeListBDM - chequeReturnAmount
            detailView.setCreditAmountBDM(
                    detailView.getGrossCreditBalance().subtract(detailView.getExcludeListBDM()).subtract(detailView.getChequeReturnAmount()));
            log.debug("creditAmountBDM: {} = grossCreditBalance: {} - excludeListBDM: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountBDM(), detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount());
            sumCreditAmountBDM = sumCreditAmountBDM.add(detailView.getCreditAmountBDM());

            // creditAmountUW = grossCreditBalance - excludeListUW - chequeReturnAmount
            detailView.setCreditAmountUW(
                    detailView.getGrossCreditBalance().subtract(detailView.getExcludeListUW()).subtract(detailView.getChequeReturnAmount()));
            log.debug("creditAmountUW: {} = grossCreditBalance: {} - excludeListUW: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountUW(), detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount());
            sumCreditAmountUW = sumCreditAmountUW.add(detailView.getCreditAmountUW());
        }

        bankStmtView.setAvgIncomeGross(sumGrossCreditBalance.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeGross: {} = SUM(grossCreditBalance): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeGross(), sumGrossCreditBalance, numberOfMonths);

        bankStmtView.setAvgIncomeNetBDM(sumCreditAmountBDM.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeNetBDM: {} = SUM(creditAmountBDM): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeNetBDM(), sumCreditAmountBDM, numberOfMonths);

        bankStmtView.setAvgIncomeNetUW(sumCreditAmountUW.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP));
        log.debug("avgIncomeNetUW: {} = SUM(CreditAmountUW): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeNetUW(), sumCreditAmountUW, numberOfMonths);

        BigDecimal sumTrdChequeReturnAmount = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails()) {
            sumTrdChequeReturnAmount = sumTrdChequeReturnAmount.add(detailView.getChequeReturnAmount());
        }
        bankStmtView.setTrdChequeReturnAmount(sumTrdChequeReturnAmount);
        log.debug("SUM(chequeReturnAmount) = {}", sumTrdChequeReturnAmount);

        // *** Remark ***
        // 1. if(creditAmountUW is non value) -> timesOfAvgCredit = creditAmountBDM / avgIncomeNetBDM
        // 2. if(excludeListUW is available) -> timesOfAvgCredit = creditAmountUW / avgIncomeNetUW
        log.debug("Calculate timesOfAvgCredit(BDM/UW)");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            try {
                if (ValidationUtil.isValueEqualZero(detailView.getCreditAmountUW())) {
                    detailView.setTimesOfAvgCreditBDM(detailView.getCreditAmountBDM().divide(bankStmtView.getAvgIncomeNetBDM(), 2, RoundingMode.HALF_UP));
                    log.debug("timesOfAvgCreditBDM: {} = creditAmountBDM: {} / avgIncomeNetBDM: {}",
                            detailView.getTimesOfAvgCreditBDM(), detailView.getCreditAmountBDM(), bankStmtView.getAvgIncomeNetBDM());
                } else {
                    detailView.setTimesOfAvgCreditUW(detailView.getCreditAmountUW().divide(bankStmtView.getAvgIncomeNetUW(), 2, RoundingMode.HALF_UP));
                    log.debug("timesOfAvgCreditUW: {} = creditAmountUW: {} / avgIncomeNetUW: {}",
                            detailView.getTimesOfAvgCreditUW(), detailView.getCreditAmountUW(), bankStmtView.getAvgIncomeNetUW());
                }
            } catch (ArithmeticException ae) {
                log.error("", ae.getMessage());
            }
        }
    }

    public void calAvgWithdrawAmount() {
        log.debug("calAvgWithdrawAmount()");
        // avgWithDrawAmount = SUM(debitAmount) / numberOfMonths
        BigDecimal sumDebitAmount = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumDebitAmount = sumDebitAmount.add(detailView.getDebitAmount());
        }
        bankStmtView.setAvgWithDrawAmount(sumDebitAmount.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP));
        log.debug("avgWithDrawAmount: {} = SUM(debitAmount): {} / numberOfMonths: {}",
                bankStmtView.getAvgWithDrawAmount(), sumDebitAmount, numberOfMonths);
    }

    public void calAvgGrossInflowPerLimit() {
        log.debug("calAvgGrossInflowPerLimit()");
        // avgGrossInflowPerLimit = SUM(grossInflowPerLimit) / numberOfMonths
        BigDecimal sumGrossInflowPerLimit = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumGrossInflowPerLimit = sumGrossInflowPerLimit.add(detailView.getGrossInflowPerLimit());
        }
        bankStmtView.setAvgGrossInflowPerLimit(sumGrossInflowPerLimit.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP));
        log.debug("avgGrossInflowPerLimit: {} = SUM(grossInflowPerLimit): {} / numberOfMonths: {}",
                bankStmtView.getAvgWithDrawAmount(), sumGrossInflowPerLimit, numberOfMonths);
    }

    //********** Swing & Utilization **********//
    public void calSwingAndUtilization() {
        log.debug("calSwingAndUtilization()");
        // swingPercent = Absolute(highestBalance - lowestBalance) / overLimitAmount
        // utilizationPercent = (monthEndBalance * (-1)) / overLimitAmount
        //*** Remark ***
        // 1. if(overLimitAmount == 0) -> (swingPercent & utilizationPercent) = 0
        // 2. if(monthEndBalance > 0) -> utilizationPercent = 0
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            if (ValidationUtil.isValueEqualZero(detailView.getOverLimitAmount())) {
                detailView.setSwingPercent(BigDecimal.ZERO);
                detailView.setUtilizationPercent(BigDecimal.ZERO);
            }
            else {
                detailView.setSwingPercent(detailView.getHighestBalance().subtract(detailView.getLowestBalance()).abs()
                        .divide(detailView.getOverLimitAmount(), 2, RoundingMode.HALF_UP));

                if (ValidationUtil.isValueGreaterThanZero(detailView.getMonthEndBalance())) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setUtilizationPercent(detailView.getMonthEndBalance().multiply(BigDecimal.valueOf(-1))
                            .divide(detailView.getOverLimitAmount(), 2, RoundingMode.HALF_UP));
                }
            }
        }

        // avgSwingPercent = SUM(swingPercent[lastSixMonth]) / numberOfLastSixMonth(overLimitAmount > 0)
        // avgUtilizationPercent = SUM(utilizationPercent[lastSixMonth]) / numberOfLastSixMonth(overLimitAmount > 0)
        int numberOfOverLimitAmountMonth = 0;
        BigDecimal sumSwingPercent = BigDecimal.ZERO;
        BigDecimal sumUtilizePercent = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails()) {
            sumSwingPercent = sumSwingPercent.add(detailView.getSwingPercent());
            sumUtilizePercent = sumUtilizePercent.add(detailView.getUtilizationPercent());

            if (ValidationUtil.isValueGreaterThanZero(detailView.getOverLimitAmount()))
                numberOfOverLimitAmountMonth += 1;
        }
        try {
            bankStmtView.setSwingPercent(sumSwingPercent.divide(BigDecimal.valueOf(numberOfOverLimitAmountMonth), 2, RoundingMode.HALF_UP));
            bankStmtView.setUtilizationPercent(sumUtilizePercent.divide(BigDecimal.valueOf(numberOfOverLimitAmountMonth), 2, RoundingMode.HALF_UP));
        } catch (ArithmeticException ae) {
            log.error("", ae.getMessage());
        }
    }

    //********** SUM , MAX **********//
    public void sumNumberOfChequeReturn() {
        log.debug("sumNumberOfChequeReturn()");
        int sumNumOfChequeReturn = 0;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails()) {
            sumNumOfChequeReturn += detailView.getNumberOfChequeReturn();
        }
        bankStmtView.setChequeReturn(BigDecimal.valueOf(sumNumOfChequeReturn));
    }

    public void sumOverLimitTimes() {
        log.debug("sumOverLimitTimes()");
        int sumOverLimitTimes = 0;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails()) {
            sumOverLimitTimes += detailView.getOverLimitTimes();
        }
        bankStmtView.setOverLimitTimes(BigDecimal.valueOf(sumOverLimitTimes));
    }

    public void maxOverLimitDays() {
        log.debug("maxOverLimitDays()");
        int maxOverLimitDays = 0;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails()) {
            if (detailView.getOverLimitDays() > maxOverLimitDays)
                maxOverLimitDays = detailView.getOverLimitDays();
        }
        bankStmtView.setOverLimitDays(BigDecimal.valueOf(maxOverLimitDays));
    }

    private List<BankStmtDetailView> getLastSixMonthBankStmtDetails() {
        log.debug("getLastSixMonthBankStmtDetails()");
        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();
        if (bankStmtDetailViewList == null || bankStmtDetailViewList.size() < 6) {
            log.debug("bankStmtDetailViewList is null! or size is less than 6");
            return new ArrayList<BankStmtDetailView>();
        }
        else {
            // 12 Month
            if (bankStmtDetailViewList.size() > 6)
                return bankStmtDetailViewList.subList(6, 12);
            // 6 Month
            else
                return bankStmtDetailViewList;
        }
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
}
