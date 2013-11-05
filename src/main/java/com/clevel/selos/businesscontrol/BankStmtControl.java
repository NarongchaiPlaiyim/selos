package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ActionStatusTransform;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class BankStmtControl extends BusinessControl {
    //Interface
    @Inject
    private RMInterface rmInterface;
    @Inject
    DWHInterface dwhInterface;

    //DAO
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    BankStatementDAO bankStatementDAO;
    @Inject
    BankStatementDetailDAO bankStatementDetailDAO;
    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    BankStmtSrcOfCollateralProofDAO srcOfCollateralProofDAO;

    //Transform
    @Inject
    ActionStatusTransform actionStatusTransform;
    @Inject
    BankStmtTransform bankStmtTransform;

    public BankStmtSummaryView retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSumitDate) {
        return retreiveBankStmtInterface(customerInfoViewList, expectedSumitDate, 0);
    }

    public BankStmtSummaryView retreiveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSumitDate, int seasonal) {
        log.info("Start retreiveBankStmtInterface with {}", customerInfoViewList);
        Date startBankStmtDate = getStartBankStmtDate(expectedSumitDate);
        int numberOfMonthBankStmt = getRetrieveMonthBankStmt(seasonal);

        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();
        List<BankStmtView> bankStmtViewList = new ArrayList<BankStmtView>();

        for (CustomerInfoView customerInfoView : customerInfoViewList) {
            if (customerInfoView.getRelation().getId() == 1) {
                if (!Util.isEmpty(customerInfoView.getTmbCustomerId())) {
                    log.info("Finding Account Number List for TMB Cus ID: {}", customerInfoView.getTmbCustomerId());
                    CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                    //CustomerAccountResult customerAccountResult = getBankAccountList(customerInfoView.getTmbCustomerId());
                    if (customerAccountResult.getActionResult().equals(ActionResult.SUCCESS)) {
                        List<CustomerAccountListModel> accountListModelList = customerAccountResult.getAccountListModels();
                        log.info("Finding account {}", accountListModelList);
                        for (CustomerAccountListModel customerAccountListModel : accountListModelList) {
                            DWHBankStatementResult dwhBankStatementResult = dwhInterface.getBankStatementData(getCurrentUserID(), customerAccountListModel.getAccountNo(), startBankStmtDate, numberOfMonthBankStmt);

                            if (dwhBankStatementResult.getActionResult().equals(ActionResult.SUCCESS)) {
                                List<DWHBankStatement> dwhBankStatementList = dwhBankStatementResult.getBankStatementList();
                                BankStmtView bankStmtView = null;
                                List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
                                for (DWHBankStatement dwhBankStatement : dwhBankStatementList) {
                                    BankStmtDetailView bankStmtDetailView = bankStmtTransform.getBankStmtDetailView(dwhBankStatement);
                                    if (bankStmtView == null) {
                                        bankStmtView = bankStmtTransform.getBankStmtView(dwhBankStatement);
                                    }
                                    bankStmtDetailViewList.add(bankStmtDetailView);
                                }
                                bankStmtView.setBankStmtDetailViewList(bankStmtDetailViewList);
                                bankStmtViewList.add(bankStmtView);
                            } else {
                                actionStatusViewList.add(actionStatusTransform.getActionStatusView(dwhBankStatementResult.getActionResult(), dwhBankStatementResult.getReason()));
                            }
                        }
                    } else {
                        actionStatusViewList.add(actionStatusTransform.getActionStatusView(customerAccountResult.getActionResult(), customerAccountResult.getReason()));
                    }
                }
            }
        }
        bankStmtSummaryView.setActionStatusViewList(actionStatusViewList);
        bankStmtSummaryView.setTmbBankStmtViewList(bankStmtViewList);
        //todo: set Other bank statement list
        return bankStmtSummaryView;
    }

    /**
     * To get starting date of retrieving bank account<br/>
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1)<br/>
     * Ex. expectedSubmissionDate: 15/10/2013 -> (T-1) -> '15/09/2013'
     * @param expectedSubmissionDate
     * @return Start previous date by bank account condition
     */
    public Date getStartBankStmtDate(Date expectedSubmissionDate) {
        if (expectedSubmissionDate != null) {
            int days = Util.getDayOfDate(expectedSubmissionDate);
            int retrieveMonth = days < 15 ? 2 : 1;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, -retrieveMonth);
        }
        return null;
    }

    /**
     * To get number of month by seasonal flag
     * @param seasonalFlag
     * @return number of month
     */
    public int getRetrieveMonthBankStmt(int seasonalFlag) {
        return seasonalFlag == 1 ? 12 : 6;
    }

    /**
     * Formula: CreditAmount(BDM/UW) = grossCreditBalance - excludeListAmount(BDM/UW) - chequeReturnAmount
     * @param grossCreditBalance
     * @param excludeListAmount
     * @param chequeReturnAmount
     * @return CreditAmount(BDM/UW)
     */
    public BigDecimal calCreditAmountNet(BigDecimal grossCreditBalance,BigDecimal excludeListAmount, BigDecimal chequeReturnAmount) {
        return grossCreditBalance.subtract(excludeListAmount).subtract(chequeReturnAmount);
    }

    /**
     * Formula: Swing(%) = Absolute[ highestBalance - lowestBalance ] / overLimitAmount
     * @param highestBalance
     * @param lowestBalance
     * @param overLimitAmount
     * @return Swing(%)
     */
    public BigDecimal calSwingPercent(BigDecimal highestBalance, BigDecimal lowestBalance, BigDecimal overLimitAmount) {
        return Util.divide(highestBalance.subtract(lowestBalance).abs(), overLimitAmount);
    }

    /**
     * Formula: Utilization(%) = [ monthEndBalance * (-1) ] / overLimitAmount
     * @param monthEndBalance
     * @param overLimitAmount
     * @return Utilization(%)
     */
    public BigDecimal calUtilizationPercent(BigDecimal monthEndBalance, BigDecimal overLimitAmount) {
        return Util.divide(monthEndBalance.multiply(BigDecimal.ONE.negate()), overLimitAmount);
    }

    public List<BankStmtDetailView> getLastSixMonthBankStmtDetails(List<BankStmtDetailView> bankStmtDetailViewList) {
        log.debug("getLastSixMonthBankStmtDetails()");
        if (bankStmtDetailViewList == null || bankStmtDetailViewList.size() < 6) {
            log.debug("bankStmtDetailViewList is null! or size is less than 6");
            return new ArrayList<BankStmtDetailView>();
        } else { // 12 Month
            if (bankStmtDetailViewList.size() > 6)
                return bankStmtDetailViewList.subList(6, 12);
            else // 6 Month
                return bankStmtDetailViewList;
        }
    }

    public void calAverages(BankStmtView bankStmtView, int numberOfMonths) {
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
            detailView.setCreditAmountBDM(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount()));
            log.debug("creditAmountBDM: {} = grossCreditBalance: {} - excludeListBDM: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountBDM(), detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount());

            sumCreditAmountBDM = sumCreditAmountBDM.add(detailView.getCreditAmountBDM());

            // creditAmountUW = grossCreditBalance - excludeListUW - chequeReturnAmount
            detailView.setCreditAmountUW(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount()));
            log.debug("creditAmountUW: {} = grossCreditBalance: {} - excludeListUW: {} - chequeReturnAmount: {}",
                    detailView.getCreditAmountUW(), detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount());

            sumCreditAmountUW = sumCreditAmountUW.add(detailView.getCreditAmountUW());
        }

        bankStmtView.setAvgIncomeGross(Util.divide(sumGrossCreditBalance, numberOfMonths));
        log.debug("avgIncomeGross: {} = SUM(grossCreditBalance): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeGross(), sumGrossCreditBalance, numberOfMonths);

        bankStmtView.setAvgIncomeNetBDM(Util.divide(sumCreditAmountBDM, numberOfMonths));
        log.debug("avgIncomeNetBDM: {} = SUM(creditAmountBDM): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeNetBDM(), sumCreditAmountBDM, numberOfMonths);

        bankStmtView.setAvgIncomeNetUW(Util.divide(sumCreditAmountUW, numberOfMonths));
        log.debug("avgIncomeNetUW: {} = SUM(CreditAmountUW): {} / numberOfMonths: {}",
                bankStmtView.getAvgIncomeNetUW(), sumCreditAmountUW, numberOfMonths);

        // SUM(Trade Cheque Return Amount of the last 6 month)
        BigDecimal sumTrdChequeReturnAmount = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            sumTrdChequeReturnAmount = sumTrdChequeReturnAmount.add(detailView.getChequeReturnAmount());
        }
        bankStmtView.setTrdChequeReturnAmount(sumTrdChequeReturnAmount);
        log.debug("SUM(chequeReturnAmount) = {}", sumTrdChequeReturnAmount);

        // *** Remark ***
        // 1. if(creditAmountUW is non value) -> timesOfAvgCredit = creditAmountBDM / avgIncomeNetBDM
        // 2. if(excludeListUW is available) -> timesOfAvgCredit = creditAmountUW / avgIncomeNetUW
        log.debug("Calculate timesOfAvgCredit(BDM/UW)");
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            if (ValidationUtil.isValueEqualZero(detailView.getCreditAmountUW())) {
                detailView.setTimesOfAvgCreditBDM(Util.divide(detailView.getCreditAmountBDM(), bankStmtView.getAvgIncomeNetBDM()));
                log.debug("timesOfAvgCreditBDM: {} = creditAmountBDM: {} / avgIncomeNetBDM: {}",
                        detailView.getTimesOfAvgCreditBDM(), detailView.getCreditAmountBDM(), bankStmtView.getAvgIncomeNetBDM());
            } else {
                detailView.setTimesOfAvgCreditUW(Util.divide(detailView.getCreditAmountUW(), bankStmtView.getAvgIncomeNetUW()));
                log.debug("timesOfAvgCreditUW: {} = creditAmountUW: {} / avgIncomeNetUW: {}",
                        detailView.getTimesOfAvgCreditUW(), detailView.getCreditAmountUW(), bankStmtView.getAvgIncomeNetUW());
            }
        }
    }

    public void calAvgWithdrawAmount(BankStmtView bankStmtView, int numberOfMonths) {
        log.debug("calAvgWithdrawAmount()");
        // avgWithDrawAmount = SUM(debitAmount) / numberOfMonths
        BigDecimal sumDebitAmount = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumDebitAmount = sumDebitAmount.add(detailView.getDebitAmount());
        }
        bankStmtView.setAvgWithDrawAmount(Util.divide(sumDebitAmount, numberOfMonths));
        log.debug("avgWithDrawAmount: {} = SUM(debitAmount): {} / numberOfMonths: {}",
                bankStmtView.getAvgWithDrawAmount(), sumDebitAmount, numberOfMonths);
    }

    public void calAvgGrossInflowPerLimit(BankStmtView bankStmtView, int numberOfMonths) {
        log.debug("calAvgGrossInflowPerLimit()");
        // avgGrossInflowPerLimit = SUM(grossInflowPerLimit) / numberOfMonths
        BigDecimal sumGrossInflowPerLimit = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
            sumGrossInflowPerLimit = sumGrossInflowPerLimit.add(detailView.getGrossInflowPerLimit());
        }
        bankStmtView.setAvgGrossInflowPerLimit(Util.divide(sumGrossInflowPerLimit, numberOfMonths));
        log.debug("avgGrossInflowPerLimit: {} = SUM(grossInflowPerLimit): {} / numberOfMonths: {}",
                bankStmtView.getAvgWithDrawAmount(), sumGrossInflowPerLimit, numberOfMonths);
    }

    public void calSwingAndUtilization(BankStmtView bankStmtView) {
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
            } else {
                detailView.setSwingPercent(
                        calSwingPercent(detailView.getHighestBalance(), detailView.getLowestBalance(), detailView.getOverLimitAmount()));

                if (ValidationUtil.isValueGreaterThanZero(detailView.getMonthEndBalance())) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setUtilizationPercent(
                            calUtilizationPercent(detailView.getMonthEndBalance(), detailView.getOverLimitAmount()));
                }
            }
        }

        // avgSwingPercent = SUM(swingPercent[lastSixMonth]) / numberOfLastSixMonth(overLimitAmount > 0)
        // avgUtilizationPercent = SUM(utilizationPercent[lastSixMonth]) / numberOfLastSixMonth(overLimitAmount > 0)
        int numberOfOverLimitAmountMonth = 0;
        BigDecimal sumSwingPercent = BigDecimal.ZERO;
        BigDecimal sumUtilizePercent = BigDecimal.ZERO;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            sumSwingPercent = sumSwingPercent.add(detailView.getSwingPercent());
            sumUtilizePercent = sumUtilizePercent.add(detailView.getUtilizationPercent());

            if (ValidationUtil.isValueGreaterThanZero(detailView.getOverLimitAmount()))
                numberOfOverLimitAmountMonth += 1;
        }
        bankStmtView.setSwingPercent(Util.divide(sumSwingPercent, numberOfOverLimitAmountMonth));
        bankStmtView.setUtilizationPercent(Util.divide(sumUtilizePercent, numberOfOverLimitAmountMonth));
    }

    private CustomerAccountResult getBankAccountList(String tmbCusId) {
        List<CustomerAccountListModel> accountListModelList = new ArrayList<CustomerAccountListModel>();
        if (tmbCusId.equals("001100000000000000000006106302")) {
            CustomerAccountListModel customerAccountListModel1 = new CustomerAccountListModel();
            customerAccountListModel1.setAccountNo("3042582720");

            CustomerAccountListModel customerAccountListModel2 = new CustomerAccountListModel();
            customerAccountListModel2.setAccountNo("3042886758");

            CustomerAccountListModel customerAccountListModel3 = new CustomerAccountListModel();
            customerAccountListModel3.setAccountNo("3042843353");

            CustomerAccountListModel customerAccountListModel4 = new CustomerAccountListModel();
            customerAccountListModel4.setAccountNo("3052116807");

            accountListModelList.add(customerAccountListModel1);
            accountListModelList.add(customerAccountListModel2);
            accountListModelList.add(customerAccountListModel3);
            accountListModelList.add(customerAccountListModel4);


        }

        CustomerAccountResult customerAccountResult = new CustomerAccountResult();
        customerAccountResult.setActionResult(ActionResult.SUCCESS);
        customerAccountResult.setCustomerId(tmbCusId);
        customerAccountResult.setAccountListModels(accountListModelList);
        return customerAccountResult;
    }

    public void saveBankStmtSummary(BankStmtSummaryView bankStmtSummaryView, long workCaseId, long workCasePrescreenId, String userId) {
        log.debug("saveBankStmtSummary() bankStmtSummaryView.id: {}, workCaseId: {}, workCasePrescreenId: {}, userId: {}",
                bankStmtSummaryView.getId(), workCaseId, workCasePrescreenId, userId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        User user = userDAO.findById(userId);

        BankStatementSummary bankStatementSummary = bankStmtTransform.getBankStatementSummary(bankStmtSummaryView, user);
        bankStatementSummary.setWorkCase(workCase);
        bankStatementSummary.setWorkCasePrescreen(workCasePrescreen);
        bankStatementSummaryDAO.persist(bankStatementSummary);
        log.debug("persist BankStatementSummary: {}", bankStatementSummary);
    }

    public void deleteBankStmt(long bankStmtId) {
        log.debug("deleteBankStmt() bankStmtId: {}", bankStmtId);
        BankStatement bankStatement = bankStatementDAO.findById(bankStmtId);
        bankStatementDAO.delete(bankStatement);
    }
}
