package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RoleUser;
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
        Date startBankStmtDate = getLastMonthDateBankStmt(expectedSumitDate);
        int numberOfMonthBankStmt = getNumberOfMonthsBankStmt(seasonal);

        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();
        List<BankStmtView> bankStmtViewList = new ArrayList<BankStmtView>();

        for (CustomerInfoView customerInfoView : customerInfoViewList) {
            if (customerInfoView.getRelation().getId() == 1) {
                if (!Util.isEmpty(customerInfoView.getTmbCustomerId())) {
                    log.info("Finding Account Number List for TMB Cus ID: {}", customerInfoView.getTmbCustomerId());
                    CustomerAccountResult customerAccountResult = rmInterface.getCustomerAccountInfo(getCurrentUserID(), customerInfoView.getTmbCustomerId());
                    log.debug("Account Number List : {}", customerAccountResult);

                    //*** FOR TEST ***//
                    //TODO REMOVE WHEN DEPLOY
                    customerAccountResult = new CustomerAccountResult();
                    customerAccountResult.setActionResult(ActionResult.SUCCESS);
                    CustomerAccountListModel customerAccountListModels = new CustomerAccountListModel();
                    customerAccountListModels.setAccountNo("3417619677");
                    List<CustomerAccountListModel> accountListModels = new ArrayList<CustomerAccountListModel>();
                    accountListModels.add(customerAccountListModels);
                    customerAccountResult.setAccountListModels(accountListModels);

                    //CustomerAccountResult customerAccountResult = getBankAccountList(customerInfoView.getTmbCustomerId());
                    if (customerAccountResult.getActionResult().equals(ActionResult.SUCCESS)) {
                        List<CustomerAccountListModel> accountListModelList = customerAccountResult.getAccountListModels();
                        log.info("Finding account {}", accountListModelList);
                        for (CustomerAccountListModel customerAccountListModel : accountListModelList) {
                            DWHBankStatementResult dwhBankStatementResult = dwhInterface.getBankStatementData(getCurrentUserID(), customerAccountListModel.getAccountNo(), startBankStmtDate, numberOfMonthBankStmt);
                            log.debug("DWHBankStatementResult : {}", dwhBankStatementResult);

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

    /**
     * To get starting date of retrieving bank account <br/>
     * If expected submission date less than 15 get current month -2 (T-2), If more than 15 get current month -1 (T-1) <br/>
     * Ex. expectedSubmissionDate: 15/10/2013 -> (T-1) -> '15/09/2013'
     * @param expectedSubmissionDate
     * @return Start previous date by bank account condition
     */
    public Date getLastMonthDateBankStmt(Date expectedSubmissionDate) {
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
    public int getNumberOfMonthsBankStmt(int seasonalFlag) {
        return seasonalFlag == 1 ? 12 : 6;
    }

    /**
     * Formula: CreditAmount(BDM/UW) = grossCreditBalance - excludeListAmount(BDM/UW) - chequeReturnAmount
     * @param grossCreditBalance
     * @param excludeListAmount
     * @param chequeReturnAmount
     * @return CreditAmount(BDM/UW)
     */
    public BigDecimal calCreditAmountNet(BigDecimal grossCreditBalance, BigDecimal excludeListAmount, BigDecimal chequeReturnAmount) {
        return grossCreditBalance.subtract(excludeListAmount).subtract(chequeReturnAmount);
    }

    /**
     * Formula: <br/>
     * if(creditAmountUW is non value) -> timesOfAvgCredit = [ creditAmountBDM / avgIncomeNetBDM ] <br/>
     * else -> timesOfAvgCredit = [ creditAmountUW / avgIncomeNetUW ]
     * @param creditAmount(BDM/UW)
     * @param avgIncomeNet(BDM/UW)
     * @return timesOfAvgCredit(BDM/UW)
     */
    public BigDecimal calTimesOfAvgCredit(BigDecimal creditAmount, BigDecimal avgIncomeNet) {
        return Util.divide(creditAmount, avgIncomeNet);
    }

    /**
     * Formula: Swing(%) = Absolute[ maxBalance - minBalance ] / overLimitAmount
     * @param maxBalance
     * @param minBalance
     * @param overLimitAmount
     * @return Swing(%)
     */
    public BigDecimal calSwingPercent(BigDecimal maxBalance, BigDecimal minBalance, BigDecimal overLimitAmount) {
        return Util.divide(maxBalance.subtract(minBalance).abs(), overLimitAmount);
    }

    /**
     * Formula: Utilization(%) = [ monthBalance * (-1) ] / overLimitAmount
     * @param monthBalance
     * @param overLimitAmount
     * @return Utilization(%)
     */
    public BigDecimal calUtilizationPercent(BigDecimal monthBalance, BigDecimal overLimitAmount) {
        return Util.divide(monthBalance.multiply(BigDecimal.ONE.negate()), overLimitAmount);
    }

    /**
     * 1.avgIncomeGross 2.avgIncomeNet(BDM/UW) 3.avgWithdrawAmount <br/>
     * Formula: <br/>
     * seasonalFlag = Yes(1) -> avgAmount = sumAmount / 12 <br/>
     * seasonalFlag = No(0) -> avgAmount = sumAmount / 6
     * @param seasonalFlag
     * @param sumAmount
     * @return avgAmount
     */
    public BigDecimal calAvgAmount(int seasonalFlag, BigDecimal sumAmount) {
        return Util.divide(sumAmount, seasonalFlag == 1 ? 12 : 6);
    }

    /**
     * Formula: <br/>
     * source.maxBalance = bankStmtDetail.maxBalance(T - x) <br/>
     * if(Account Type = "Current" & bankStmtDetail.maxBalance(T - x) <= 0) -> 0
     * @param bankStmtDetailView
     * @return maxBalance
     */
    public BigDecimal getMaxBalance(BankStmtDetailView bankStmtDetailView) {
        if (ValidationUtil.isValueLessEqualZero(bankStmtDetailView.getMaxBalance()))
            return BigDecimal.ZERO;
        else
            return bankStmtDetailView.getMaxBalance();
    }

    public BigDecimal getAvgMaxBalance(BankAccountTypeView bankAccountTypeView, BigDecimal maxBalance1, BigDecimal maxBalance2, BigDecimal maxBalance3) {
        log.debug("getAvgMaxBalance() bankAccountTypeView: {}, maxBalance1: {}, maxBalance2: {}, maxBalance3: {}",
                bankAccountTypeView, maxBalance1, maxBalance2, maxBalance3);
        // if(Account Type = Saving or Current or Other: Transaction Delivery or Draft) -> [maxBalance1 + maxBalance2 + maxBalance3] / 3
        // else -> maxBalance3
        //todo: Confirm logic from DE
        return Util.divide(maxBalance1.add(maxBalance2).add(maxBalance3), 3);
    }

    public BigDecimal getAvgMaxBalance(BankStmtView bankStmtView) {
        log.debug("getAvgMaxBalance() bankStmtView");
        if (bankStmtView.getSrcOfCollateralProofViewList() != null && bankStmtView.getSrcOfCollateralProofViewList().size() < 3) {
            log.error("getAvgMaxBalance() srcOfCollateralProofViewList is null! or size < 3");
            return BigDecimal.ZERO;
        }
        return getAvgMaxBalance(bankStmtView.getBankAccountTypeView(),
                bankStmtView.getSrcOfCollateralProofViewList().get(0).getMaxBalance(),
                bankStmtView.getSrcOfCollateralProofViewList().get(1).getMaxBalance(),
                bankStmtView.getSrcOfCollateralProofViewList().get(2).getMaxBalance());
    }

    public List<BankStmtDetailView> getLastThreeMonthBankStmtDetails(List<BankStmtDetailView> bankStmtDetailViewList) {
        log.debug("getLastThreeMonthBankStmtDetails()");
        if (bankStmtDetailViewList == null || bankStmtDetailViewList.size() < 6) {
            log.debug("getLastThreeMonthBankStmtDetails is null! or size is less than 6");
            return new ArrayList<BankStmtDetailView>();
        }
        // if(12 Month)
        if (bankStmtDetailViewList.size() > 6)
            return bankStmtDetailViewList.subList(9, 12);
        // if(6 Month)
        else
            return bankStmtDetailViewList.subList(3, 6);
    }

    public List<BankStmtDetailView> getLastSixMonthBankStmtDetails(List<BankStmtDetailView> bankStmtDetailViewList) {
        log.debug("getLastSixMonthBankStmtDetails()");
        if (bankStmtDetailViewList == null || bankStmtDetailViewList.size() < 6) {
            log.debug("bankStmtDetailViewList is null! or size is less than 6");
            return new ArrayList<BankStmtDetailView>();
        }
        // if(12 Month)
        if (bankStmtDetailViewList.size() > 6)
            return bankStmtDetailViewList.subList(6, 12);
        // if(6 Month)
        else
            return bankStmtDetailViewList;
    }

    public void bankStmtDetailCalculation(BankStmtView bankStmtView, int seasonalFlag) {

        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();

        if (bankStmtView != null && bankStmtDetailViewList != null) {
            // Summary var
            BigDecimal sumGrossCreditBalance = BigDecimal.ZERO;
            BigDecimal sumCreditAmountBDM = BigDecimal.ZERO;
            BigDecimal sumCreditAmountUW = BigDecimal.ZERO;
            BigDecimal sumDebitAmount = BigDecimal.ZERO;
            BigDecimal sumGrossInflowPerLimit = BigDecimal.ZERO;

            BigDecimal sumSwingPctOfLastSixM = BigDecimal.ZERO;
            BigDecimal sumUtilPctOfLastSixM = BigDecimal.ZERO;
            BigDecimal sumChqRetAmtOfLastSixM = BigDecimal.ZERO;

            // Average var
            BigDecimal avgIncomeGross = BigDecimal.ZERO;
            BigDecimal avgIncomeNetBDM = BigDecimal.ZERO;
            BigDecimal avgIncomeNetUW = BigDecimal.ZERO;
            BigDecimal avgWithDrawAmount = BigDecimal.ZERO;
            BigDecimal avgSwingPercent = BigDecimal.ZERO;
            BigDecimal avgUtilizationPercent = BigDecimal.ZERO;
            BigDecimal avgGrossInflowPerLimit = BigDecimal.ZERO;

            // Number var
            int numMonthOvrLmtAmtOfLastSixM = 0;
            int numMonthNonOvrLmtAmt = 0;

            int sumNumChqRetOfLastSixM = 0;
            int sumOvrLmtTimesOfLastSixM = 0;
            int maxOvrLmtDaysOfLastSixM = 0;

            // Calculate from all months
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                // ---------- CreditAmountNet(BDM/UW) ---------- //
                detailView.setCreditAmountBDM(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount()));
                detailView.setCreditAmountUW(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount()));

                // ---------- Swing & Utilization (%) ---------- //
                // overLimitAmount = 0 -> Swing & Utilization (%) = 0
                if (ValidationUtil.isValueEqualZero(detailView.getOverLimitAmount())) {
                    detailView.setSwingPercent(BigDecimal.ZERO);
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setSwingPercent(calSwingPercent(detailView.getMaxBalance(), detailView.getMinBalance(), detailView.getOverLimitAmount()));
                    detailView.setUtilizationPercent(calUtilizationPercent(detailView.getMonthBalance(), detailView.getOverLimitAmount()));
                }
                // monthBalance > 0 -> Utilization (%) = 0
                if (ValidationUtil.isValueGreaterThanZero(detailView.getMonthBalance())) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                }
                // todo: limit field is means?
                // grossInflowPerLimit = [ grossCreditBalance / limit ]
                detailView.setGrossInflowPerLimit(Util.divide(detailView.getGrossCreditBalance(), bankStmtView.getLimit()));
                // totalTransaction = [ numberOfCreditTxn + numberOfDebitTxn ]
                detailView.setTotalTransaction(detailView.getNumberOfCreditTxn() + detailView.getNumberOfDebitTxn());

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
                if (ValidationUtil.isValueEqualZero(detailView.getOverLimitAmount())) {
                    numMonthNonOvrLmtAmt += 1;
                }

                // ---------- Summary ---------- //
                sumGrossCreditBalance = sumGrossCreditBalance.add(detailView.getGrossCreditBalance());
                sumCreditAmountBDM = sumCreditAmountBDM.add(detailView.getCreditAmountBDM());
                sumCreditAmountUW = sumCreditAmountUW.add(detailView.getCreditAmountUW());
                sumDebitAmount = sumDebitAmount.add(detailView.getDebitAmount());
                sumGrossInflowPerLimit = sumGrossInflowPerLimit.add(detailView.getGrossInflowPerLimit());
            }
            // Calculate Average all months
            avgIncomeGross = calAvgAmount(seasonalFlag, sumGrossCreditBalance);
            avgIncomeNetBDM = calAvgAmount(seasonalFlag, sumCreditAmountBDM);
            avgIncomeNetUW = calAvgAmount(seasonalFlag, sumCreditAmountUW);
            avgWithDrawAmount = calAvgAmount(seasonalFlag, sumDebitAmount);

            // ---------- timesOfAvgCredit(BDM/UW) ---------- //
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                detailView.setTimesOfAvgCreditBDM(calTimesOfAvgCredit(detailView.getCreditAmountBDM(), avgIncomeNetBDM));
                detailView.setTimesOfAvgCreditUW(calTimesOfAvgCredit(detailView.getCreditAmountUW(), avgIncomeNetUW));
            }

            // Calculate from The last six months
            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtDetailViewList)) {
                sumSwingPctOfLastSixM = sumSwingPctOfLastSixM.add(detailView.getSwingPercent());
                sumUtilPctOfLastSixM = sumUtilPctOfLastSixM.add(detailView.getUtilizationPercent());

                if (ValidationUtil.isValueGreaterThanZero(detailView.getOverLimitAmount())) {
                    numMonthOvrLmtAmtOfLastSixM += 1;
                }

                sumNumChqRetOfLastSixM += detailView.getNumberOfChequeReturn();
                sumChqRetAmtOfLastSixM = sumChqRetAmtOfLastSixM.add(detailView.getChequeReturnAmount());
                sumOvrLmtTimesOfLastSixM += detailView.getOverLimitTimes();

                if (detailView.getOverLimitDays() > maxOvrLmtDaysOfLastSixM) {
                    maxOvrLmtDaysOfLastSixM = detailView.getOverLimitDays();
                }
            }
            // Calculate Average from The last six months
            avgSwingPercent = Util.divide(sumSwingPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM);
            avgUtilizationPercent = Util.divide(sumUtilPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM);
            avgGrossInflowPerLimit = Util.divide(sumGrossInflowPerLimit, numMonthNonOvrLmtAmt);

            // set summary Bank statement
            bankStmtView.setAvgIncomeGross(avgIncomeGross);
            bankStmtView.setAvgIncomeNetBDM(avgIncomeNetBDM);
            bankStmtView.setAvgIncomeNetUW(avgIncomeNetUW);
            bankStmtView.setAvgWithDrawAmount(avgWithDrawAmount);
            bankStmtView.setAvgSwingPercent(avgSwingPercent);
            bankStmtView.setAvgUtilizationPercent(avgUtilizationPercent);
            bankStmtView.setAvgGrossInflowPerLimit(avgGrossInflowPerLimit);
            bankStmtView.setChequeReturn(BigDecimal.valueOf(sumNumChqRetOfLastSixM));
            bankStmtView.setTrdChequeReturnAmount(sumChqRetAmtOfLastSixM);
            bankStmtView.setOverLimitTimes(BigDecimal.valueOf(sumOvrLmtTimesOfLastSixM));
            bankStmtView.setOverLimitDays(BigDecimal.valueOf(maxOvrLmtDaysOfLastSixM));
        }
    }

    public void bankStmtSumTotalCalculation(BankStmtSummaryView bankStmtSummaryView) {
        //todo: Check calculate logic here
        BigDecimal tmbTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal othTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal grdTotalTrdChqRetAmount = BigDecimal.ZERO;
        BigDecimal grdTotalAvgOSBalance = BigDecimal.ZERO;

        for (BankStmtView tmbBankStmtView : bankStmtSummaryView.getTmbBankStmtViewList()) {
            tmbTotalIncomeGross = tmbTotalIncomeGross.add(tmbBankStmtView.getAvgIncomeGross());
            tmbTotalIncomeNetBDM = tmbTotalIncomeNetBDM.add(tmbBankStmtView.getAvgIncomeNetBDM());
            tmbTotalIncomeNetUW = tmbTotalIncomeNetUW.add(tmbBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = grdTotalTrdChqRetAmount.add(tmbBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = grdTotalAvgOSBalance.add(tmbBankStmtView.getAvgOSBalanceAmount());
        }

        for (BankStmtView othBankStmtView : bankStmtSummaryView.getOthBankStmtViewList()) {
            othTotalIncomeGross = othTotalIncomeGross.add(othBankStmtView.getAvgIncomeGross());
            othTotalIncomeNetBDM = othTotalIncomeNetBDM.add(othBankStmtView.getAvgIncomeNetBDM());
            othTotalIncomeNetUW = othTotalIncomeNetUW.add(othBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = grdTotalTrdChqRetAmount.add(othBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = grdTotalAvgOSBalance.add(othBankStmtView.getAvgOSBalanceAmount());
        }
        // Total
        bankStmtSummaryView.setTMBTotalIncomeGross(tmbTotalIncomeGross);
        bankStmtSummaryView.setTMBTotalIncomeNetBDM(tmbTotalIncomeNetBDM);
        bankStmtSummaryView.setTMBTotalIncomeNetUW(tmbTotalIncomeNetUW);
        bankStmtSummaryView.setOthTotalIncomeGross(othTotalIncomeGross);
        bankStmtSummaryView.setOthTotalIncomeNetBDM(othTotalIncomeNetBDM);
        bankStmtSummaryView.setOthTotalIncomeNetUW(othTotalIncomeNetUW);

        // Grand total
        BigDecimal grdTotalIncomeGross = tmbTotalIncomeGross.add(othTotalIncomeGross);
        BigDecimal grdTotalIncomeNetBDM = tmbTotalIncomeNetBDM.add(othTotalIncomeNetBDM);
        BigDecimal grdTotalIncomeNetUW = tmbTotalIncomeNetUW.add(othTotalIncomeNetUW);

        // if (grdTotalIncomeNetUW = 0)
        //      grdTotalTrdChqRetPercent = [ grdTotalTrdChqRetAmount / grdTotalIncomeNetBDM ]
        // else
        //      grdTotalTrdChqRetPercent = [ grdTotalTrdChqRetAmount / grdTotalIncomeNetUW ]
        BigDecimal grdTotalTrdChqRetPercent = BigDecimal.ZERO;
        if (ValidationUtil.isValueEqualZero(grdTotalIncomeNetUW)) {
            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetBDM);
        } else {
            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetUW);
        }
        bankStmtSummaryView.setGrdTotalIncomeGross(grdTotalIncomeGross);
        bankStmtSummaryView.setGrdTotalIncomeNetBDM(grdTotalIncomeNetBDM);
        bankStmtSummaryView.setGrdTotalIncomeNetUW(grdTotalIncomeNetUW);
        bankStmtSummaryView.setGrdTotalTDChqRetAmount(grdTotalTrdChqRetAmount);
        bankStmtSummaryView.setGrdTotalTDChqRetPercent(grdTotalTrdChqRetPercent);
        bankStmtSummaryView.setGrdTotalAvgOSBalanceAmount(grdTotalAvgOSBalance);
    }

    public void saveBankStmtSummary(BankStmtSummaryView bankStmtSummaryView, long workCaseId, long workCasePrescreenId, String userId) {
        log.debug("saveBankStmtSummary() bankStmtSummaryView.id: {}, workCaseId: {}, workCasePrescreenId: {}, userId: {}",
                bankStmtSummaryView.getId(), workCaseId, workCasePrescreenId, userId);
        WorkCase workCase = null;
        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
        }
        WorkCasePrescreen workCasePrescreen = null;
        if(workCasePrescreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        }
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

    // --------------- Source of Collateral Proof ---------------
    public void calSourceOfCollateralProof(BankStmtView bankStmtView) {
        log.debug("calSourceOfCollateralProof()");
        List<BankStmtDetailView> lastThreeMonthBankStmtDetail = getLastThreeMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList());
        List<BankStmtSrcOfCollateralProofView> srcOfCollateralProofViewList = bankStmtView.getSrcOfCollateralProofViewList();
        /*  source.lastThreeMonth1 = BankStmtDetail.asOfDate( [T-x] )
            source.lastThreeMonth2 = BankStmtDetail.asOfDate( [T-x]+1 )
            source.lastThreeMonth3 = BankStmtDetail.asOfDate( [T-x]+2 )
         */
        // if source of collateral proof is already exist
        // re-calculate & replace the old data
        if (srcOfCollateralProofViewList != null && srcOfCollateralProofViewList.size() > 0) {
            for (int i=0; i < srcOfCollateralProofViewList.size(); i++) {
                BankStmtDetailView detailView = lastThreeMonthBankStmtDetail.get(i);

                BankStmtSrcOfCollateralProofView srcOfCollateralProofView = srcOfCollateralProofViewList.get(i);
                srcOfCollateralProofView.setDateOfMaxBalance(detailView.getDateOfMaxBalance());
                srcOfCollateralProofView.setMaxBalance( getMaxBalance(detailView) );
            }
        } else {
            // create & add new source of collateral proof list
            srcOfCollateralProofViewList = new ArrayList<BankStmtSrcOfCollateralProofView>(3);
            for (BankStmtDetailView detailView : lastThreeMonthBankStmtDetail) {
                BankStmtSrcOfCollateralProofView srcOfCollateralProofView = new BankStmtSrcOfCollateralProofView();
                srcOfCollateralProofView.setDateOfMaxBalance(detailView.getDateOfMaxBalance());
                srcOfCollateralProofView.setMaxBalance( getMaxBalance(detailView) );
                srcOfCollateralProofViewList.add(srcOfCollateralProofView);
            }
            bankStmtView.setSrcOfCollateralProofViewList(srcOfCollateralProofViewList);
        }
        bankStmtView.setAvgOSBalanceAmount( getAvgMaxBalance(bankStmtView) );
    }

    public boolean isBDMUser() {
        User user = getCurrentUser();
        if (RoleUser.ABDM.getValue() == user.getRole().getId() || RoleUser.BDM.getValue() == user.getRole().getId())
            return true;
        else
            return false;
    }
}
