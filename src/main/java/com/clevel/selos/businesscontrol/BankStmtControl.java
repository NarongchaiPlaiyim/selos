package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RoleUser;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ActionStatusTransform;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Stateless
public class BankStmtControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private UserDAO userDAO;

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
            int days = DateTimeUtil.getDayOfDate(expectedSubmissionDate);
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
        return seasonalFlag == RadioValue.YES.value() ? 12 : 6;
    }

    /**
     * Formula: CreditAmount(BDM/UW) = grossCreditBalance - excludeListAmount(BDM/UW) - chequeReturnAmount
     * @param grossCreditBalance
     * @param excludeListAmount
     * @param chequeReturnAmount
     * @return CreditAmount(BDM/UW)
     */
    public BigDecimal calCreditAmountNet(BigDecimal grossCreditBalance, BigDecimal excludeListAmount, BigDecimal chequeReturnAmount) {
        if (grossCreditBalance == null && excludeListAmount == null && chequeReturnAmount == null)
            return null;

        if (grossCreditBalance == null)
            grossCreditBalance = BigDecimal.ZERO;

        if (excludeListAmount == null)
            excludeListAmount = BigDecimal.ZERO;

        if (chequeReturnAmount == null)
            chequeReturnAmount = BigDecimal.ZERO;

        return grossCreditBalance.subtract(excludeListAmount).subtract(chequeReturnAmount);
    }

    /**
     * Formula: <br/>
     * if(creditAmountUW[(Net)-UW] is blank) -> all values will be blank <br/>
     * else -> timesOfAvgCredit = [ creditAmount(BDM/UW) / avgIncomeNet(BDM/UW) ]
     * @param creditAmount(BDM/UW)
     * @param avgIncomeNet(BDM/UW)
     * @param creditAmountUW
     * @return timesOfAvgCredit(BDM/UW)
     */
    public BigDecimal calTimesOfAvgCredit(BigDecimal creditAmount, BigDecimal avgIncomeNet, BigDecimal creditAmountUW) {
        if (creditAmount == null || avgIncomeNet == null || creditAmountUW == null)
            return null;

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
        if (maxBalance == null || minBalance == null || overLimitAmount == null)
            return null;

        return Util.divide(maxBalance.subtract(minBalance).abs(), overLimitAmount);
    }

    /**
     * Formula: Utilization(%) = [ monthBalance * (-1) ] / overLimitAmount
     * @param monthBalance
     * @param overLimitAmount
     * @return Utilization(%)
     */
    public BigDecimal calUtilizationPercent(BigDecimal monthBalance, BigDecimal overLimitAmount) {
        if (monthBalance == null || overLimitAmount == null)
            return null;

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

    public boolean isBorrowerAndOD(BankStmtView bankStmtView) {
        // AccCharacteristic is Borrower (value = 1)
        if (bankStmtView.getAccountCharacteristic() != 1)
            return false;

        // Within 6 months, must has limit at least one month
        boolean hasODLimit = false;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            if (detailView.getOverLimitAmount() != null) {
                hasODLimit = true;
                break;
            }
        }
        return hasODLimit;
    }

    public void updateMainAccount(BankStmtSummaryView bankStmtSummaryView) {
        BankStmtView mainAccBankStmt = null;
        // Candidate list
        List<BankStmtView> mainAccBankStmtList;
        // Vars for compare
        BigDecimal maxAvgNetCrdAmt;
        BigDecimal maxLimit;
        BigDecimal maxDebitTx;
        BigDecimal maxCreditTx;

        // -------------------- TMB -------------------- //
        List<BankStmtView> tmbBankStmtList = bankStmtSummaryView.getTmbBankStmtViewList();
        if (tmbBankStmtList != null) {
            mainAccBankStmtList = new ArrayList<BankStmtView>();

            maxAvgNetCrdAmt = BigDecimal.ZERO;
            maxLimit = BigDecimal.ZERO;
            maxDebitTx = BigDecimal.ZERO;
            maxCreditTx = BigDecimal.ZERO;

            for (BankStmtView tmbBankStmt : tmbBankStmtList) {
                if (!isBorrowerAndOD(tmbBankStmt))
                    continue;

                // Compare Max avgIncomeNet(UW/BDM)
                if (tmbBankStmt.getAvgIncomeNetUW() != null) {
                    if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgIncomeNetUW(), maxAvgNetCrdAmt)) {
                        maxAvgNetCrdAmt = tmbBankStmt.getAvgIncomeNetUW();
                        mainAccBankStmt = tmbBankStmt;
                        mainAccBankStmtList.clear();
                        mainAccBankStmtList.add(tmbBankStmt);
                    }
                    else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgIncomeNetUW(), maxAvgNetCrdAmt)) {
                        // Compare Max limit
                        if (tmbBankStmt.getAvgLimit() != null) {
                            if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgLimit(), maxLimit)) {
                                maxLimit = tmbBankStmt.getAvgLimit();
                                mainAccBankStmt = tmbBankStmt;
                                mainAccBankStmtList.clear();
                                mainAccBankStmtList.add(tmbBankStmt);
                            }
                            else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgLimit(), maxLimit)) {
                                // Compare Max of Debit Transaction
                                if (tmbBankStmt.getAvgWithDrawAmount() != null) {
                                    if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgWithDrawAmount(), maxDebitTx)) {
                                        maxDebitTx = tmbBankStmt.getAvgWithDrawAmount();
                                        mainAccBankStmt = tmbBankStmt;
                                        mainAccBankStmtList.clear();
                                        mainAccBankStmtList.add(tmbBankStmt);
                                    }
                                    else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgWithDrawAmount(), maxDebitTx)) {
                                        // Compare Max of Credit Transaction
                                        if (tmbBankStmt.getAvgIncomeGross() != null) {
                                            if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgIncomeGross(), maxCreditTx)) {
                                                maxDebitTx = tmbBankStmt.getAvgIncomeGross();
                                                mainAccBankStmt = tmbBankStmt;
                                                mainAccBankStmtList.clear();
                                                mainAccBankStmtList.add(tmbBankStmt);
                                            }
                                            else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgIncomeGross(), maxCreditTx)) {
                                                mainAccBankStmtList.add(tmbBankStmt);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else if (tmbBankStmt.getAvgIncomeNetBDM() != null) {
                    if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgIncomeNetBDM(), maxAvgNetCrdAmt)) {
                        maxAvgNetCrdAmt = tmbBankStmt.getAvgIncomeNetBDM();
                        mainAccBankStmtList.clear();
                        mainAccBankStmtList.add(tmbBankStmt);
                    }
                    else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgIncomeNetBDM(), maxAvgNetCrdAmt)) {
                        mainAccBankStmtList.add(tmbBankStmt);
                    }
                }

                // If no any AvgIncomeNet(UW/BDM)
                if (mainAccBankStmtList.size() == 0) {
                    if (tmbBankStmt.getAvgLimit() != null) {
                        if (ValidationUtil.isGreaterThan(tmbBankStmt.getAvgLimit(), maxLimit)) {
                            maxLimit = tmbBankStmt.getAvgLimit();
                            mainAccBankStmtList.clear();
                            mainAccBankStmtList.add(tmbBankStmt);
                            continue;
                        }
                        else if (ValidationUtil.isValueEqual(tmbBankStmt.getAvgLimit(), maxLimit)) {
                            mainAccBankStmtList.add(tmbBankStmt);
                            continue;
                        }
                    }
                }

            }
        }

        // -------------------- OTH -------------------- //
//        List<BankStmtView> othBankStmtList = bankStmtSummaryView.getOthBankStmtViewList();
//        if (othBankStmtList != null) {
//            maxAvgNetCrdAmt = BigDecimal.ZERO;
//            maxLimit = BigDecimal.ZERO;
//            maxDebitTx = BigDecimal.ZERO;
//            maxCreditTx = BigDecimal.ZERO;
//
//            BankStmtView mainOTHBankStmt = null;
//
//        }
    }

    public void bankStmtDetailCalculation(BankStmtView bankStmtView, int seasonalFlag) {

        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();

        if (bankStmtView != null && bankStmtDetailViewList != null && bankStmtDetailViewList.size() > 0) {
            // Summary var
            BigDecimal sumGrossCreditBalance = BigDecimal.ZERO;
            BigDecimal sumCreditAmountBDM = BigDecimal.ZERO;
            BigDecimal sumCreditAmountUW = BigDecimal.ZERO;
            BigDecimal sumDebitAmount = BigDecimal.ZERO;

            int numMonthNonOvrLmtAmt = 0;

            // limit = limit of last month
            BigDecimal limit = bankStmtDetailViewList.get(bankStmtDetailViewList.size() - 1).getOverLimitAmount();

            // ========== Calculate from All of Months ==========
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                // ---------- CreditAmountNet(BDM/UW) ---------- //
                detailView.setCreditAmountBDM(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount()));
                detailView.setCreditAmountUW(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount()));

                // ---------- Swing & Utilization (%) ---------- //
                // overLimitAmount = 0 -> Swing & Utilization (%) = 0
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueEqualZero(detailView.getOverLimitAmount())) {
                    detailView.setSwingPercent(BigDecimal.ZERO);
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setSwingPercent(calSwingPercent(detailView.getMaxBalance(), detailView.getMinBalance(), detailView.getOverLimitAmount()));
                    detailView.setUtilizationPercent(calUtilizationPercent(detailView.getMonthBalance(), detailView.getOverLimitAmount()));
                }
                // monthBalance > 0 -> Utilization (%) = 0
                if (detailView.getMonthBalance() != null && ValidationUtil.isValueGreaterThanZero(detailView.getMonthBalance())) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                }

                // grossInflowPerLimit = [ grossCreditBalance / limit ]
                detailView.setGrossInflowPerLimit(Util.divide(detailView.getGrossCreditBalance(), limit));

                // totalTransaction = [ numberOfCreditTxn + numberOfDebitTxn ]
                detailView.setTotalTransaction(detailView.getNumberOfCreditTxn() + detailView.getNumberOfDebitTxn());

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueEqualZero(detailView.getOverLimitAmount())) {
                    numMonthNonOvrLmtAmt += 1;
                }

                // ---------- Summary ---------- //
                sumGrossCreditBalance = Util.add(sumGrossCreditBalance, detailView.getGrossCreditBalance());
                sumCreditAmountBDM = Util.add(sumCreditAmountBDM, detailView.getCreditAmountBDM());
                sumCreditAmountUW = Util.add(sumCreditAmountUW, detailView.getCreditAmountUW());
                sumDebitAmount = Util.add(sumDebitAmount, detailView.getDebitAmount());
            }

            // Calculate Average all months
            BigDecimal avgIncomeGross = calAvgAmount(seasonalFlag, sumGrossCreditBalance);
            BigDecimal avgIncomeNetBDM = calAvgAmount(seasonalFlag, sumCreditAmountBDM);
            BigDecimal avgIncomeNetUW = calAvgAmount(seasonalFlag, sumCreditAmountUW);
            BigDecimal avgWithDrawAmount = calAvgAmount(seasonalFlag, sumDebitAmount);

            // ---------- timesOfAvgCredit(BDM/UW) ---------- //
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                detailView.setTimesOfAvgCreditBDM(calTimesOfAvgCredit(detailView.getCreditAmountBDM(), avgIncomeNetBDM, detailView.getCreditAmountUW()));
                detailView.setTimesOfAvgCreditUW(calTimesOfAvgCredit(detailView.getCreditAmountUW(), avgIncomeNetUW, detailView.getCreditAmountUW()));
            }

            // ========== Calculate from The last Six months ==========
            BigDecimal sumSwingPctOfLastSixM = BigDecimal.ZERO;
            BigDecimal sumUtilPctOfLastSixM = BigDecimal.ZERO;
            BigDecimal sumChqRetAmtOfLastSixM = BigDecimal.ZERO;
            BigDecimal sumNetUWofLastSixM = BigDecimal.ZERO;
            BigDecimal sumNetBDMofLastSixM = BigDecimal.ZERO;

            int numMonthOvrLmtAmtOfLastSixM = 0;
            int sumNumChqRetOfLastSixM = 0;
            int sumOvrLmtTimesOfLastSixM = 0;
            int maxOvrLmtDaysOfLastSixM = 0;

            boolean useNetUWToCal = false;

            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtDetailViewList)) {
                sumSwingPctOfLastSixM = Util.add(sumSwingPctOfLastSixM, detailView.getSwingPercent());
                sumUtilPctOfLastSixM = Util.add(sumUtilPctOfLastSixM, detailView.getUtilizationPercent());
                sumChqRetAmtOfLastSixM = Util.add(sumChqRetAmtOfLastSixM, detailView.getChequeReturnAmount());

                if (detailView.getOverLimitAmount() != null && ValidationUtil.isValueGreaterThanZero(detailView.getOverLimitAmount())) {
                    numMonthOvrLmtAmtOfLastSixM += 1;
                }

                sumNumChqRetOfLastSixM += detailView.getNumberOfChequeReturn();
                sumOvrLmtTimesOfLastSixM += detailView.getOverLimitTimes();

                if (detailView.getOverLimitDays() > maxOvrLmtDaysOfLastSixM) {
                    maxOvrLmtDaysOfLastSixM = detailView.getOverLimitDays();
                }

                if (detailView.getCreditAmountUW() != null) {
                    sumNetUWofLastSixM = sumNetUWofLastSixM.add(detailView.getCreditAmountUW());
                    useNetUWToCal = true;
                }
                sumNetBDMofLastSixM = Util.add(sumNetBDMofLastSixM, detailView.getCreditAmountBDM());
            }
            // Calculate Average from The last Six months
            BigDecimal avgSwingPercent = Util.divide(sumSwingPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM);
            BigDecimal avgUtilizationPercent = Util.divide(sumUtilPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM);

            // avgGrossInflowPerLimit = [ SUM(grossCreditBalance) / Limit ] / [ 6 - NumberOfNonODLimit]
            BigDecimal avgGrossInflowPerLimit = Util.divide( Util.divide(sumGrossCreditBalance, limit) , 6 - numMonthNonOvrLmtAmt);

            // trdChequeReturnPercent = [ SUM(trdChequeReturnAmount of Last Six Months) / SUM(NetUW of Last Six Months) ] if(UW is Blank) then use BDM instead
            BigDecimal trdChequeReturnPercent = Util.divide( sumChqRetAmtOfLastSixM, (useNetUWToCal ? sumNetUWofLastSixM : sumNetBDMofLastSixM) );

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
            bankStmtView.setTrdChequeReturnPercent(trdChequeReturnPercent);
            bankStmtView.setOverLimitTimes(BigDecimal.valueOf(sumOvrLmtTimesOfLastSixM));
            bankStmtView.setOverLimitDays(BigDecimal.valueOf(maxOvrLmtDaysOfLastSixM));
            bankStmtView.setAvgLimit(limit);
        }
    }

    public void bankStmtSumTotalCalculation(BankStmtSummaryView bankStmtSummaryView) {
        BigDecimal tmbTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal othTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal grdTotalTrdChqRetAmount = BigDecimal.ZERO;
        BigDecimal grdTotalAvgOSBalance = BigDecimal.ZERO;

        boolean useNetUWToCal = false;

        for (BankStmtView tmbBankStmtView : bankStmtSummaryView.getTmbBankStmtViewList()) {
            tmbTotalIncomeGross = Util.add(tmbTotalIncomeGross, tmbBankStmtView.getAvgIncomeGross());
            tmbTotalIncomeNetBDM = Util.add(tmbTotalIncomeNetBDM, tmbBankStmtView.getAvgIncomeNetBDM());
            tmbTotalIncomeNetUW = Util.add(tmbTotalIncomeNetUW, tmbBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, tmbBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = Util.add(grdTotalAvgOSBalance, tmbBankStmtView.getAvgOSBalanceAmount());
            if (tmbBankStmtView.getAvgIncomeNetUW() != null) {
                useNetUWToCal = true;
            }
        }
        for (BankStmtView othBankStmtView : bankStmtSummaryView.getOthBankStmtViewList()) {
            othTotalIncomeGross = Util.add(othTotalIncomeGross, othBankStmtView.getAvgIncomeGross());
            othTotalIncomeNetBDM = Util.add(othTotalIncomeNetBDM, othBankStmtView.getAvgIncomeNetBDM());
            othTotalIncomeNetUW = Util.add(othTotalIncomeNetUW, othBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, othBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = Util.add(grdTotalAvgOSBalance, othBankStmtView.getAvgOSBalanceAmount());
            if (othBankStmtView.getAvgIncomeNetUW() != null) {
                useNetUWToCal = true;
            }
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

        BigDecimal grdTotalTrdChqRetPercent;
        if (useNetUWToCal)
            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetUW);
        else
            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetBDM);

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
        // todo: check Bank account type before calculate average
        bankStmtView.setAvgOSBalanceAmount( getAvgMaxBalance(bankStmtView) );
    }

    public boolean isBDMUser() {
        User user = getCurrentUser();
        if (RoleUser.ABDM.getValue() == user.getRole().getId() || RoleUser.BDM.getValue() == user.getRole().getId())
            return true;
        else
            return false;
    }

    public void sortAsOfDateBankStmtDetails(List<BankStmtDetailView> detailViews, final SortOrder order) {
        Collections.sort(detailViews, new Comparator<BankStmtDetailView>() {
            public int compare(BankStmtDetailView o1, BankStmtDetailView o2) {
                if (o1.getAsOfDate() == null || o2.getAsOfDate() == null)
                    return 0;
                switch (order) {
                    case ASCENDING: return o1.getAsOfDate().compareTo(o2.getAsOfDate());
                    case DESCENDING: return o2.getAsOfDate().compareTo(o1.getAsOfDate());
                    default: return 0;
                }
            }
        });
    }

}
