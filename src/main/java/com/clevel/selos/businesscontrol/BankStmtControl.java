package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
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
    @Inject
    BankDAO bankDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;

    //Transform
    @Inject
    ActionStatusTransform actionStatusTransform;
    @Inject
    BankStmtTransform bankStmtTransform;

    public BankStmtSummaryView retrieveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSubmitDate) {
        return retrieveBankStmtInterface(customerInfoViewList, getLastMonthDateBankStmt(expectedSubmitDate), RadioValue.NO.value());
    }

    public BankStmtSummaryView retrieveBankStmtInterface(List<CustomerInfoView> customerInfoViewList, Date expectedSubmitDate, int seasonal) {
        log.info("Start retrieveBankStmtInterface with {}", customerInfoViewList);
        Date startBankStmtDate = getLastMonthDateBankStmt(expectedSubmitDate);
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
                            log.debug("DWH Bank statement result: {} by Account No: {}", dwhBankStatementResult.getActionResult(), customerAccountListModel.getAccountNo());
                            if (dwhBankStatementResult.getActionResult().equals(ActionResult.SUCCESS)) {
                                List<DWHBankStatement> dwhBankStatementList = dwhBankStatementResult.getBankStatementList();
                                BankStmtView bankStmtView = null;
                                List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
                                for (DWHBankStatement dwhBankStatement : dwhBankStatementList) {
                                    if (bankStmtView == null) {
                                        bankStmtView = bankStmtTransform.getBankStmtView(dwhBankStatement);
                                    }
                                    BankStmtDetailView bankStmtDetailView = bankStmtTransform.getBankStmtDetailView(dwhBankStatement);
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

        log.debug("Result action status list: {}", actionStatusViewList);
        log.debug("Result TMB Bank statement list: {}", bankStmtViewList);
        bankStmtSummaryView.setActionStatusViewList(actionStatusViewList);
        bankStmtSummaryView.setTmbBankStmtViewList(bankStmtViewList);
        log.info("End retrieveBankStmtInterface...");
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

    public BankStmtSummaryView getBankStmtSummaryByWorkCaseId(long workCaseId) {
        return bankStmtTransform.getBankStmtSummaryView(bankStatementSummaryDAO.findByWorkCaseId(workCaseId));
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
            int retrieveMonth = days < 15 ? -2 : -1;
            return DateTimeUtil.getOnlyDatePlusMonth(expectedSubmissionDate, retrieveMonth);
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
     * timesOfAvgCredit = [ creditAmount(BDM/UW) / avgIncomeNet(BDM/UW) ]
     * @param creditAmount(BDM/UW)
     * @param avgIncomeNet(BDM/UW)
     * @return timesOfAvgCredit(BDM/UW)
     */
    public BigDecimal calTimesOfAvgCredit(BigDecimal creditAmount, BigDecimal avgIncomeNet) {
        return Util.divide(creditAmount, avgIncomeNet);
    }

    /**
     * Formula: Swing(%) = ( |maxBalance - minBalance| / overLimitAmount ) x 100
     * @param maxBalance
     * @param minBalance
     * @param overLimitAmount
     * @return Swing(%)
     */
    public BigDecimal calSwingPercent(BigDecimal maxBalance, BigDecimal minBalance, BigDecimal overLimitAmount) {
        if (maxBalance == null && minBalance == null && overLimitAmount == null)
            return null;

        BigDecimal diffMinMaxBalance = Util.subtract(maxBalance, minBalance);

        if (diffMinMaxBalance != null) {
            BigDecimal result = Util.multiply(Util.divide(diffMinMaxBalance.abs(), overLimitAmount), Util.ONE_HUNDRED);
            return (result != null) ? result.setScale(2, RoundingMode.HALF_UP) : result;
        }
        else {
            return null;
        }
    }

    /**
     * Formula: Utilization(%) = ( [ monthBalance * (-1) ]  / overLimitAmount ) x 100
     * @param monthBalance
     * @param overLimitAmount
     * @return Utilization(%)
     */
    public BigDecimal calUtilizationPercent(BigDecimal monthBalance, BigDecimal overLimitAmount) {
        if (monthBalance == null && overLimitAmount == null)
            return null;

        BigDecimal result = Util.multiply(Util.divide(Util.multiply(monthBalance, BigDecimal.ONE.negate()), overLimitAmount), Util.ONE_HUNDRED);
        return (result != null) ? result.setScale(2, RoundingMode.HALF_UP) : result;
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
        return Util.divide(sumAmount, seasonalFlag == RadioValue.YES.value() ? 12 : 6);
    }

    /**
     * Formula: <br/>
     * source.maxBalance = bankStmtDetail.maxBalance(T - x) <br/>
     * if(Account Type = "Current" & bankStmtDetail.maxBalance(T - x) <= 0) -> 0
     * @param bankStmtDetailView
     * @return maxBalance
     */
    public BigDecimal getMaxBalance(BankStmtDetailView bankStmtDetailView, BankAccountType bankAccountType) {
        if (bankStmtDetailView == null || bankStmtDetailView.getMaxBalance() == null)
            return null;

        if ((bankAccountType != null && BankAccountTypeEnum.CA.name().equalsIgnoreCase(bankAccountType.getShortName()))
            && ValidationUtil.isValueCompareToZero(bankStmtDetailView.getMaxBalance(), ValidationUtil.CompareMode.LESS_THAN))
            return BigDecimal.ZERO;
        else
            return bankStmtDetailView.getMaxBalance();
    }

    public BigDecimal getAvgMaxBalance(BankAccountType bankAccTypeFromBankStmt, BigDecimal maxBalance1, BigDecimal maxBalance2, BigDecimal maxBalance3,
                                       BankAccountType savingAccType, BankAccountType currentAccType, BankAccountType othFDType, BankAccountType othBOEType) {
        log.debug("getAvgMaxBalance() bankAccTypeFromBankStmt: {}, maxBalance1: {}, maxBalance2: {}, maxBalance3: {}, savingAccType: {}, currentAccType: {}, othFDType: {}, othBOEType: {}",
                bankAccTypeFromBankStmt, maxBalance1, maxBalance2, maxBalance3, savingAccType, currentAccType, othFDType, othBOEType);
        if(maxBalance1 == null){
            maxBalance1 = BigDecimal.ZERO;
        }
        if(maxBalance2 == null){
            maxBalance2 = BigDecimal.ZERO;
        }
        if(maxBalance3 == null){
            maxBalance3 = BigDecimal.ZERO;
        }

        // if(Account Type = 'Saving Account', 'Current Account', Other: 'Fixed Deposit', 'Bill of Exchange') -> [maxBalance1 + maxBalance2 + maxBalance3] / 3
        if (savingAccType != null && savingAccType.getId() == bankAccTypeFromBankStmt.getId()) {
            return Util.divide(maxBalance1.add(maxBalance2).add(maxBalance3), 3);
        }

        if (currentAccType != null && currentAccType.getId() == bankAccTypeFromBankStmt.getId()) {
            return Util.divide(maxBalance1.add(maxBalance2).add(maxBalance3), 3);
        }

        if (othFDType != null && othFDType.getId() == bankAccTypeFromBankStmt.getId()) {
            return Util.divide(maxBalance1.add(maxBalance2).add(maxBalance3), 3);
        }

        if (othBOEType != null && othBOEType.getId() == bankAccTypeFromBankStmt.getId()) {
            return Util.divide(maxBalance1.add(maxBalance2).add(maxBalance3), 3);
        }
        // else -> maxBalance3
        return maxBalance3;
    }

    public BigDecimal getAvgMaxBalance(BankStmtView bankStmtView, BankAccountType bankAccTypeFromBankStmt, BankAccountType savingAccType, BankAccountType currentAccType, BankAccountType othDepositType, BankAccountType othDraftType) {
        log.debug("getAvgMaxBalance() bankStmtView");
        if (bankStmtView == null || bankStmtView.getSrcOfCollateralProofViewList() == null) {
            log.error("getAvgMaxBalance() bankStmtView is null or srcOfCollateralProofViewList is null!");
            return BigDecimal.ZERO;
        }

        if (bankStmtView.getSrcOfCollateralProofViewList().size() < 3) {
            log.error("getAvgMaxBalance() srcOfCollateralProofViewList size < 3!");
            return BigDecimal.ZERO;
        }

        if (bankAccTypeFromBankStmt == null) {
            log.error("getAvgMaxBalance() bankAccTypeFromBankStmt is null!");
            return BigDecimal.ZERO;
        }

        return getAvgMaxBalance(bankAccTypeFromBankStmt,
                bankStmtView.getSrcOfCollateralProofViewList().get(0).getMaxBalance(),
                bankStmtView.getSrcOfCollateralProofViewList().get(1).getMaxBalance(),
                bankStmtView.getSrcOfCollateralProofViewList().get(2).getMaxBalance(),
                savingAccType, currentAccType, othDepositType, othDraftType);
    }

    public List<BankStmtDetailView> getLastThreeMonthBankStmtDetails(List<BankStmtDetailView> bankStmtDetailViewList) {
        log.debug("getLastThreeMonthBankStmtDetails()");
        if (bankStmtDetailViewList == null || bankStmtDetailViewList.size() < 6) {
            log.debug("getLastThreeMonthBankStmtDetails is null! or size is less than 6");
            return new ArrayList<BankStmtDetailView>();
        }
        sortAsOfDateBankStmtDetails(bankStmtDetailViewList, SortOrder.ASCENDING);
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

    public boolean isBorrowerAndHasODLimit(BankStmtView bankStmtView) {
        // AccCharacteristic is Borrower (value = 1)
        if (bankStmtView.getAccountCharacteristic() != 1)
            return false;
        // Within 6 months, must has limit(OD Limit is NOT Blank and OD Limit > 0) at least one month
        boolean hasODLimit = false;
        for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList())) {
            if (detailView.getOverLimitAmount() != null
                && ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.GREATER_THAN)) {
                hasODLimit = true;
                break;
            }
        }
        return hasODLimit;
    }

    private void setMainAccountAtId(List<BankStmtView> bankStmtViewList, long atId) {
        if (bankStmtViewList == null || bankStmtViewList.size() == 0) return;
        for (BankStmtView bankStmtView : bankStmtViewList) {
            if (atId == bankStmtView.getId()) {
                bankStmtView.setMainAccount(RadioValue.YES.value());
            } else {
                bankStmtView.setMainAccount(RadioValue.NO.value());
            }
        }
    }

    public void updateMainAccount(List<BankStmtView> bankStmtViewList) {
        log.debug("updateMainAccount()");
        /*
        if avgIncomeNet same, use one that has max limit., then max of debit transaction, then max of credit transaction.
        */
        if (bankStmtViewList != null) {
            List<BankStmtView> candidateMaxIncomeNetList = new ArrayList<BankStmtView>();
            BigDecimal maxValue = BigDecimal.ZERO;
            long atId = 0;

            // find MAX AvgIncomeNet(UW/BDM) from all
            for (int i=0; i<bankStmtViewList.size(); i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);
                // skip to next, if BankStmt is not Borrower or does not have any ODLimit within the last Six month
                if (!isBorrowerAndHasODLimit(bankStmtView))
                    continue;

                if (bankStmtView.getAvgIncomeNetUW() != null) {
                    if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetUW(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                        maxValue = bankStmtView.getAvgIncomeNetUW();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetUW(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
                    }
                } else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getNetIncomeLastSix(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                        maxValue = bankStmtView.getNetIncomeLastSix();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    } //Main OD NetIncome Last Six
//                else if (bankStmtView.getAvgIncomeNetBDM() != null) {
//                    if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetBDM(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
//                        maxValue = bankStmtView.getAvgIncomeNetBDM();
//                        candidateMaxIncomeNetList.clear();
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                        atId = bankStmtView.getId();
//                    }
//                    else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetBDM(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                    }
//                }
            }

            if (candidateMaxIncomeNetList.size() == 1) {
                setMainAccountAtId(bankStmtViewList, atId);
            }
            else if (candidateMaxIncomeNetList.size() > 1) {
                List<BankStmtView> candidateMaxLimitList = new ArrayList<BankStmtView>();
                maxValue = BigDecimal.ZERO;
                atId = 0;

                // find MAX Limit from the same max incomeNet
                for (BankStmtView bankStmtView : candidateMaxIncomeNetList) {
                    if (bankStmtView.getAvgLimit() != null) {
                        if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgLimit(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                            maxValue = bankStmtView.getAvgLimit();
                            candidateMaxLimitList.clear();
                            candidateMaxLimitList.add(bankStmtView);
                            atId = bankStmtView.getId();
                        }
                        else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgLimit(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                            candidateMaxLimitList.add(bankStmtView);
                        }
                    }
                }

                if (candidateMaxLimitList.size() == 1) {
                    setMainAccountAtId(bankStmtViewList, atId);
                }
                else if (candidateMaxLimitList.size() > 1) {
                    List<BankStmtView> candidateMaxDebitTxList = new ArrayList<BankStmtView>();
                    maxValue = BigDecimal.ZERO;
                    atId = 0;

                    // find MAX Debit tx from the same max limit
                    for (BankStmtView bankStmtView : candidateMaxLimitList) {
                        if (bankStmtView.getAvgWithDrawAmount() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgWithDrawAmount(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                                maxValue = bankStmtView.getAvgWithDrawAmount();
                                candidateMaxDebitTxList.clear();
                                candidateMaxDebitTxList.add(bankStmtView);
                                atId = bankStmtView.getId();
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgWithDrawAmount(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                                candidateMaxDebitTxList.add(bankStmtView);
                            }
                        }
                    }

                    if (candidateMaxDebitTxList.size() == 1) {
                        setMainAccountAtId(bankStmtViewList, atId);
                    }
                    else if (candidateMaxDebitTxList.size() > 1) {
                        List<BankStmtView> candidateMaxCreditTxList = new ArrayList<BankStmtView>();
                        maxValue = BigDecimal.ZERO;
                        atId = 0;

                        // find MAX Credit tx from the same max debit tx
                        for (BankStmtView bankStmtView : candidateMaxDebitTxList) {
                            if (bankStmtView.getAvgIncomeGross() != null) {
                                if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeGross(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                                    maxValue = bankStmtView.getAvgIncomeGross();
                                    candidateMaxCreditTxList.clear();
                                    candidateMaxCreditTxList.add(bankStmtView);
                                    atId = bankStmtView.getId();
                                }
                                else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeGross(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                                    candidateMaxCreditTxList.add(bankStmtView);
                                }
                            }
                        }

                        if (candidateMaxCreditTxList.size() == 1) {
                            setMainAccountAtId(bankStmtViewList, atId);
                        }
                        else if (candidateMaxCreditTxList.size() > 1) {
                            //.....To do more condition
                        }

                    }

                }

            }
            /*
                - All: not Borrower and does not have OD Limit or avgIncomeNet is NULL or below Zero
            */
        }
    }

    public void updateHighestInflow(List<BankStmtView> bankStmtViewList) {
        log.debug("updateHighestInflow()");
        if (bankStmtViewList != null) {
            BigDecimal maxAvgGrossInflowPerLimit = BigDecimal.ZERO;
            BankStmtView highestBankStmtView = null;
            int atIndex = 0;
            // find maxAvgGrossInflowPerLimit from all of BankStmt
            for (int i=0; i<bankStmtViewList.size(); i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);
                if (bankStmtView.getAvgGrossInflowPerLimit() != null
                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgGrossInflowPerLimit(), maxAvgGrossInflowPerLimit, ValidationUtil.CompareMode.GREATER_THAN)) {
                    maxAvgGrossInflowPerLimit = bankStmtView.getAvgGrossInflowPerLimit();
                    highestBankStmtView = bankStmtView;
                    atIndex = i;
                }
            }
            // update isHighestInflow
            if (highestBankStmtView != null) {
                for (int i=0; i<bankStmtViewList.size(); i++) {
                    BankStmtView bankStmtView = bankStmtViewList.get(i);
                    if (atIndex == i) {
                        bankStmtView.setHighestInflow("Y");
                    } else {
                        bankStmtView.setHighestInflow("N");
                    }
                }
            }
        }
    }

    public void updateMainAccAndHighestInflow(List<BankStmtView> bankStmtViewList) {

        if (!Util.isNull(bankStmtViewList)) {
            List<BankStmtView> candidateMaxIncomeNetList = new ArrayList<BankStmtView>();
            BigDecimal maxValue = BigDecimal.ZERO;
            long atId = 0;

            BigDecimal maxAvgGrossInflowPerLimit = BigDecimal.ZERO;
            BankStmtView highestBankStmtView = null;
            int highestAtIndex = 0;

            for (int i=0; i<bankStmtViewList.size(); i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);
                log.debug("--toString. {}",bankStmtView.toString());
                // find MAX AvgGrossInflowPerLimit
                if (bankStmtView.getAvgGrossInflowPerLimit() != null
                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgGrossInflowPerLimit(), maxAvgGrossInflowPerLimit, ValidationUtil.CompareMode.GREATER_THAN)) {
                    maxAvgGrossInflowPerLimit = bankStmtView.getAvgGrossInflowPerLimit();
                    highestBankStmtView = bankStmtView;
                    highestAtIndex = i;
                }

                // skip to next, if BankStmt is not Borrower or does not have any ODLimit within the last Six month
                if (!isBorrowerAndHasODLimit(bankStmtView))
                    continue;

                // find MAX AvgIncomeNet(UW/BDM)
                if (!Util.isNull(bankStmtView.getNetIncomeLastSix())){
                    if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getNetIncomeLastSix(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                        maxValue = bankStmtView.getNetIncomeLastSix();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getNetIncomeLastSix(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
                    }
                }
            }
//                if (bankStmtView.getAvgIncomeNetUW() != null) {
//                    if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetUW(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
//                        maxValue = bankStmtView.getAvgIncomeNetUW();
//                        candidateMaxIncomeNetList.clear();
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                        atId = bankStmtView.getId();
//                    }
//                    else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetUW(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                    }
//                }
//                else if (bankStmtView.getAvgIncomeNetBDM() != null) {
//                    if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetBDM(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
//                        maxValue = bankStmtView.getAvgIncomeNetBDM();
//                        candidateMaxIncomeNetList.clear();
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                        atId = bankStmtView.getId();
//                    }
//                    else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeNetBDM(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
//                        candidateMaxIncomeNetList.add(bankStmtView);
//                    }
//                }
//            }

            // update isHighestInflow
            if (highestBankStmtView != null) {
                for (int i=0; i<bankStmtViewList.size(); i++) {
                    BankStmtView bankStmtView = bankStmtViewList.get(i);
                    if (highestAtIndex == i) {
                        bankStmtView.setHighestInflow("Y");
                    } else {
                        bankStmtView.setHighestInflow("N");
                    }
                }
            }

            if (candidateMaxIncomeNetList.size() == 1) {
                setMainAccountAtId(bankStmtViewList, atId);
            }
            else if (candidateMaxIncomeNetList.size() > 1) {
                List<BankStmtView> candidateMaxLimitList = new ArrayList<BankStmtView>();
                maxValue = BigDecimal.ZERO;
                atId = 0;
                // find MAX Limit from the same max incomeNet
                for (BankStmtView bankStmtView : candidateMaxIncomeNetList) {
                    if (bankStmtView.getAvgLimit() != null) {
                        if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgLimit(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                            maxValue = bankStmtView.getAvgLimit();
                            candidateMaxLimitList.clear();
                            candidateMaxLimitList.add(bankStmtView);
                            atId = bankStmtView.getId();
                        }
                        else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgLimit(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                            candidateMaxLimitList.add(bankStmtView);
                        }
                    }
                }

                if (candidateMaxLimitList.size() == 1) {
                    setMainAccountAtId(bankStmtViewList, atId);
                }
                else if (candidateMaxLimitList.size() > 1) {
                    List<BankStmtView> candidateMaxDebitTxList = new ArrayList<BankStmtView>();
                    maxValue = BigDecimal.ZERO;
                    atId = 0;
                    // find MAX Debit tx from the same max limit
                    for (BankStmtView bankStmtView : candidateMaxLimitList) {
                        if (bankStmtView.getAvgWithDrawAmount() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgWithDrawAmount(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                                maxValue = bankStmtView.getAvgWithDrawAmount();
                                candidateMaxDebitTxList.clear();
                                candidateMaxDebitTxList.add(bankStmtView);
                                atId = bankStmtView.getId();
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgWithDrawAmount(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                                candidateMaxDebitTxList.add(bankStmtView);
                            }
                        }
                    }

                    if (candidateMaxDebitTxList.size() == 1) {
                        setMainAccountAtId(bankStmtViewList, atId);
                    }
                    else if (candidateMaxDebitTxList.size() > 1) {
                        List<BankStmtView> candidateMaxCreditTxList = new ArrayList<BankStmtView>();
                        maxValue = BigDecimal.ZERO;
                        atId = 0;
                        // find MAX Credit tx from the same max debit tx
                        for (BankStmtView bankStmtView : candidateMaxDebitTxList) {
                            if (bankStmtView.getAvgIncomeGross() != null) {
                                if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeGross(), maxValue, ValidationUtil.CompareMode.GREATER_THAN)) {
                                    maxValue = bankStmtView.getAvgIncomeGross();
                                    candidateMaxCreditTxList.clear();
                                    candidateMaxCreditTxList.add(bankStmtView);
                                    atId = bankStmtView.getId();
                                }
                                else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgIncomeGross(), maxValue, ValidationUtil.CompareMode.EQUAL)) {
                                    candidateMaxCreditTxList.add(bankStmtView);
                                }
                            }
                        }

                        if (candidateMaxCreditTxList.size() == 1) {
                            setMainAccountAtId(bankStmtViewList, atId);
                        }
                        else if (candidateMaxCreditTxList.size() > 1) {
                            //.....To do more condition
                        }

                    }

                }
            }
        }
    }

    public void updateMainAccAndHighestInflow(BankStmtSummaryView bankStmtSummaryView) {
        log.debug("updateMainAccAndHighestInflow()");
        updateMainAccAndHighestInflow(bankStmtSummaryView.getTmbBankStmtViewList());
        updateMainAccAndHighestInflow(bankStmtSummaryView.getOthBankStmtViewList());
    }

    public void bankStmtDetailCalculation(BankStmtView bankStmtView, int seasonalFlag) {
        log.debug("bankStmtDetailCalculation() bankStmtView: {}, seasonalFlag: {}", bankStmtView, seasonalFlag);
        if (bankStmtView == null
            || bankStmtView.getBankStmtDetailViewList() == null
            || bankStmtView.getBankStmtDetailViewList().size() == 0)
            return;

        boolean isRoleUW = RoleValue.UW.id() == getUserRoleId();

        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();
        // Summary var
        BigDecimal sumGrossCreditBalance = BigDecimal.ZERO;
        BigDecimal sumCreditAmountBDM = BigDecimal.ZERO;
        BigDecimal sumCreditAmountUW = BigDecimal.ZERO;
        BigDecimal sumDebitAmount = BigDecimal.ZERO;
        BigDecimal sumGrossInflowPerLimit = BigDecimal.ZERO;
        BigDecimal totalTransaction = BigDecimal.ZERO;

        int numMonthNonOvrLmtAmt = 0;

        // limit = limit of last month
        BigDecimal limit = bankStmtDetailViewList.get(bankStmtDetailViewList.size() - 1).getOverLimitAmount();
        BigDecimal tmpGrossInflowLimit;

        // ========== Calculate from All of Months ==========
        if (isRoleUW) {
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                // ---------- CreditAmountNet(BDM) ---------- //
                detailView.setCreditAmountBDM(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount()));
                // ---------- CreditAmountNet(UW) ---------- //
                detailView.setCreditAmountUW(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount()));

                // ---------- Swing & Utilization (%) ---------- //
                // overLimitAmount = 0 -> Swing & Utilization (%) = 0
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
                    detailView.setSwingPercent(BigDecimal.ZERO);
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setSwingPercent(calSwingPercent(detailView.getMaxBalance(), detailView.getMinBalance(), detailView.getOverLimitAmount()));
                    detailView.setUtilizationPercent(calUtilizationPercent(detailView.getMonthBalance(), detailView.getOverLimitAmount()));
                }
                // monthBalance > 0 -> Utilization (%) = 0
                if (detailView.getMonthBalance() != null && ValidationUtil.isValueCompareToZero(detailView.getMonthBalance(), ValidationUtil.CompareMode.GREATER_THAN)) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                }

                // grossInflowPerLimit(%) = [ grossCreditBalance / limit ] x 100
                tmpGrossInflowLimit = Util.multiply(Util.divide(detailView.getGrossCreditBalance(), detailView.getOverLimitAmount()), Util.ONE_HUNDRED);
                if (tmpGrossInflowLimit != null) {
                    tmpGrossInflowLimit = tmpGrossInflowLimit.setScale(2, RoundingMode.HALF_UP);
                }
                detailView.setGrossInflowPerLimit(tmpGrossInflowLimit);

                // totalTransaction = [ numberOfCreditTxn + numberOfDebitTxn ]
                detailView.setTotalTransaction(detailView.getNumberOfCreditTxn() + detailView.getNumberOfDebitTxn());

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
//                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
//                    numMonthNonOvrLmtAmt += 1;
//                }

                // ---------- Summary ---------- //
                sumGrossCreditBalance = Util.add(sumGrossCreditBalance, detailView.getGrossCreditBalance());
                sumCreditAmountUW = Util.add(sumCreditAmountUW, detailView.getCreditAmountUW());
                sumCreditAmountBDM = Util.add(sumCreditAmountBDM, detailView.getCreditAmountBDM()); //Role UW show netBDM
                sumDebitAmount = Util.add(sumDebitAmount, detailView.getDebitAmount());
//                sumGrossInflowPerLimit = Util.add(sumGrossInflowPerLimit, detailView.getGrossInflowPerLimit());

                //----------- Total Transaction -----------//
                totalTransaction = Util.add(totalTransaction, new BigDecimal(detailView.getTotalTransaction()));
            }

//            sumCreditAmountBDM = null;
        }
        else {
            // ABDM, BDM
            for (BankStmtDetailView detailView : bankStmtDetailViewList) {
                // ---------- CreditAmountNet(BDM) ---------- //
                detailView.setCreditAmountBDM(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListBDM(), detailView.getChequeReturnAmount()));
                // ---------- CreditAmountNet(UW) ---------- //
//                detailView.setCreditAmountUW(calCreditAmountNet(detailView.getGrossCreditBalance(), detailView.getExcludeListUW(), detailView.getChequeReturnAmount()));

                // ---------- Swing & Utilization (%) ---------- //
                // overLimitAmount = 0 -> Swing & Utilization (%) = 0
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
                    detailView.setSwingPercent(BigDecimal.ZERO);
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                } else {
                    detailView.setSwingPercent(calSwingPercent(detailView.getMaxBalance(), detailView.getMinBalance(), detailView.getOverLimitAmount()));
                    detailView.setUtilizationPercent(calUtilizationPercent(detailView.getMonthBalance(), detailView.getOverLimitAmount()));
                }
                // monthBalance > 0 -> Utilization (%) = 0
                if (detailView.getMonthBalance() != null && ValidationUtil.isValueCompareToZero(detailView.getMonthBalance(), ValidationUtil.CompareMode.GREATER_THAN)) {
                    detailView.setUtilizationPercent(BigDecimal.ZERO);
                }

                // grossInflowPerLimit(%) = [ grossCreditBalance / limit ] x 100
                tmpGrossInflowLimit = Util.multiply(Util.divide(detailView.getGrossCreditBalance(), detailView.getOverLimitAmount()), Util.ONE_HUNDRED);
                if (tmpGrossInflowLimit != null) {
                    tmpGrossInflowLimit = tmpGrossInflowLimit.setScale(2, RoundingMode.HALF_UP);
                }
                detailView.setGrossInflowPerLimit(tmpGrossInflowLimit);

                // totalTransaction = [ numberOfCreditTxn + numberOfDebitTxn ]
                detailView.setTotalTransaction(detailView.getNumberOfCreditTxn() + detailView.getNumberOfDebitTxn());

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
//                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
//                    numMonthNonOvrLmtAmt += 1;
//                }

                // ---------- Summary ---------- //
                sumGrossCreditBalance = Util.add(sumGrossCreditBalance, detailView.getGrossCreditBalance());
                sumCreditAmountBDM = Util.add(sumCreditAmountBDM, detailView.getCreditAmountBDM());
//                sumCreditAmountUW = Util.add(sumCreditAmountUW, detailView.getCreditAmountUW()); //Role BDM show netUW
                sumDebitAmount = Util.add(sumDebitAmount, detailView.getDebitAmount());
//                sumGrossInflowPerLimit = Util.add(sumGrossInflowPerLimit, detailView.getGrossInflowPerLimit());

                //---------- Total Transaction --------//
                totalTransaction = Util.add(totalTransaction, new BigDecimal(detailView.getTotalTransaction()));
                log.debug("--GrossInflowPerLimit.{}",detailView.getGrossInflowPerLimit());
            }

            sumCreditAmountUW = null;
        }

        // Calculate Average all months
        BigDecimal avgIncomeGross = calAvgAmount(seasonalFlag, sumGrossCreditBalance);
        BigDecimal avgIncomeNetBDM = calAvgAmount(seasonalFlag, sumCreditAmountBDM);
        BigDecimal avgIncomeNetUW = calAvgAmount(seasonalFlag, sumCreditAmountUW);
        BigDecimal avgWithDrawAmount = calAvgAmount(seasonalFlag, sumDebitAmount);

        // ---------- timesOfAvgCredit(BDM/UW) ---------- //
        for (BankStmtDetailView detailView : bankStmtDetailViewList) {
            detailView.setTimesOfAvgCreditBDM(calTimesOfAvgCredit(detailView.getCreditAmountBDM(), avgIncomeNetBDM));
            detailView.setTimesOfAvgCreditUW(calTimesOfAvgCredit(detailView.getCreditAmountUW(), avgIncomeNetUW));
        }

        // ========== Calculate from The last Six months ==========
        BigDecimal sumSwingPctOfLastSixM = BigDecimal.ZERO;
        BigDecimal sumUtilPctOfLastSixM = BigDecimal.ZERO;
        BigDecimal sumChqRetAmtOfLastSixM = BigDecimal.ZERO;
        BigDecimal sumChqRetAmtCountIncomeOfLastSizM = BigDecimal.ZERO;
        BigDecimal sumNetUWofLastSixM = BigDecimal.ZERO;
        BigDecimal sumNetBDMofLastSixM = BigDecimal.ZERO;

        int numMonthOvrLmtAmtOfLastSixM = 0;
        int sumNumChqRetOfLastSixM = 0;
        int sumOvrLmtTimesOfLastSixM = 0;
        int maxOvrLmtDaysOfLastSixM = 0;

        boolean isCountIncome = bankStmtView.getNotCountIncome() == 0;

        if (isRoleUW) {
            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtDetailViewList)) {
                sumSwingPctOfLastSixM = Util.add(sumSwingPctOfLastSixM, detailView.getSwingPercent());
                sumUtilPctOfLastSixM = Util.add(sumUtilPctOfLastSixM, detailView.getUtilizationPercent());
//                sumChqRetAmtOfLastSixM = Util.add(sumChqRetAmtOfLastSixM, detailView.getChequeReturnAmount());

                if (isCountIncome) {
                    sumChqRetAmtOfLastSixM = Util.add(sumChqRetAmtOfLastSixM, detailView.getChequeReturnAmount());
                    sumChqRetAmtCountIncomeOfLastSizM = Util.add(sumChqRetAmtCountIncomeOfLastSizM, detailView.getChequeReturnAmount());
                }

                if (detailView.getOverLimitAmount() != null && ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.GREATER_THAN)) {
                    numMonthOvrLmtAmtOfLastSixM += 1;
                }

                sumNumChqRetOfLastSixM += detailView.getNumberOfChequeReturn();
                sumOvrLmtTimesOfLastSixM += detailView.getOverLimitTimes();

                if (detailView.getOverLimitDays() > maxOvrLmtDaysOfLastSixM) {
                    maxOvrLmtDaysOfLastSixM = detailView.getOverLimitDays();
                }

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
                    numMonthNonOvrLmtAmt += 1;
                }

                sumNetUWofLastSixM = Util.add(sumNetUWofLastSixM, detailView.getCreditAmountUW());
                sumGrossInflowPerLimit = Util.add(sumGrossInflowPerLimit, detailView.getGrossInflowPerLimit());
            }

            sumNetBDMofLastSixM = null;
        }
        else {
            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(bankStmtDetailViewList)) {
                sumSwingPctOfLastSixM = Util.add(sumSwingPctOfLastSixM, detailView.getSwingPercent());
                sumUtilPctOfLastSixM = Util.add(sumUtilPctOfLastSixM, detailView.getUtilizationPercent());
//                sumChqRetAmtOfLastSixM = Util.add(sumChqRetAmtOfLastSixM, detailView.getChequeReturnAmount());

                if (isCountIncome) {
                    sumChqRetAmtOfLastSixM = Util.add(sumChqRetAmtOfLastSixM, detailView.getChequeReturnAmount());
                    sumChqRetAmtCountIncomeOfLastSizM = Util.add(sumChqRetAmtCountIncomeOfLastSizM, detailView.getChequeReturnAmount());
                }

                if (detailView.getOverLimitAmount() != null && ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.GREATER_THAN)) {
                    numMonthOvrLmtAmtOfLastSixM += 1;
                }

                sumNumChqRetOfLastSixM += detailView.getNumberOfChequeReturn();
                sumOvrLmtTimesOfLastSixM += detailView.getOverLimitTimes();

                if (detailView.getOverLimitDays() > maxOvrLmtDaysOfLastSixM) {
                    maxOvrLmtDaysOfLastSixM = detailView.getOverLimitDays();
                }

                // Count number of month Non-OverLimitAmount (overLimitAmount = 0)
                if (detailView.getOverLimitAmount() == null || ValidationUtil.isValueCompareToZero(detailView.getOverLimitAmount(), ValidationUtil.CompareMode.EQUAL)) {
                    numMonthNonOvrLmtAmt += 1;
                }

                sumNetBDMofLastSixM = Util.add(sumNetBDMofLastSixM, detailView.getCreditAmountBDM());
                sumGrossInflowPerLimit = Util.add(sumGrossInflowPerLimit, detailView.getGrossInflowPerLimit());
            }

            sumNetUWofLastSixM = null;
        }

        // Calculate Average from The last Six months
        BigDecimal avgSwingPercent = Util.divide(sumSwingPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM).setScale(2, RoundingMode.HALF_UP);
        BigDecimal avgUtilizationPercent = Util.divide(sumUtilPctOfLastSixM, numMonthOvrLmtAmtOfLastSixM).setScale(2, RoundingMode.HALF_UP);

        // avgGrossInflowPerLimit(%) = ([ SUM(grossCreditBalance) / Limit ] / [ 6 - NumberOfNonODLimit]) x 100
        //BigDecimal avgGrossInflowPerLimit = Util.multiply(Util.divide( Util.divide(sumGrossCreditBalance, limit) , 6 - numMonthNonOvrLmtAmt), Util.ONE_HUNDRED);
        log.debug("sumGrossInflowPerLimit.{},numMonthNonOvrLmtAmt.{}",sumGrossInflowPerLimit,numMonthNonOvrLmtAmt);
        BigDecimal avgGrossInflowPerLimit = Util.divide(sumGrossInflowPerLimit , (6 - numMonthNonOvrLmtAmt));
        if (avgGrossInflowPerLimit != null) {
            avgGrossInflowPerLimit = avgGrossInflowPerLimit.setScale(2, RoundingMode.HALF_UP);
        }

        // trdChequeReturnPercent = [ SUM(trdChequeReturnAmount of Last Six Months) if(count income) / SUM(Net[UW/BDM] of Last Six Months) ] x 100
        // *if(UW is Blank) then use BDM instead
        BigDecimal trdChequeReturnPercent = Util.multiply(Util.divide(sumChqRetAmtCountIncomeOfLastSizM, (isRoleUW ? sumNetUWofLastSixM : sumNetBDMofLastSixM)), Util.ONE_HUNDRED);
        BigDecimal sumNetIncomeLastSix = Util.divide((isRoleUW ? sumNetUWofLastSixM : sumNetBDMofLastSixM),6).setScale(2, RoundingMode.HALF_UP);
        if (trdChequeReturnPercent != null) {
            trdChequeReturnPercent = trdChequeReturnPercent.setScale(2, RoundingMode.HALF_UP);
        }

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
        bankStmtView.setNetIncomeLastSix(sumNetIncomeLastSix);
        log.debug("---NetIncomeLastSix. {}",bankStmtView.getNetIncomeLastSix());
        bankStmtView.setTotalTransaction(totalTransaction);

        Bank bank = bankDAO.findById(bankStmtView.getBankView().getCode());
        if (bank != null) {
            bankStmtView.setTMB(BankType.TMB.shortName().equalsIgnoreCase(bank.getShortName()) ? "Y" : "N");
        } else {
            bankStmtView.setTMB("");
        }

    }

    public void bankStmtSumTotalCalculation(BankStmtSummaryView bankStmtSummaryView, boolean isBorrower) {
        log.debug("bankStmtSumTotalCalculation() bankStmtSummaryView: {}, isBorrower: {}", bankStmtSummaryView, isBorrower);
        if (bankStmtSummaryView == null)
            return;

        BigDecimal tmbTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetUW = BigDecimal.ZERO;
        BigDecimal othTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal grdTotalTrdChqRetAmount = BigDecimal.ZERO;
        BigDecimal grdTotalAvgOSBalance = BigDecimal.ZERO;

        boolean isRoleUW = RoleValue.UW.id() == getUserRoleId();
        boolean isCountIncome;

        BigDecimal sumChqRetAmtCountIncomeOfLastSizM = BigDecimal.ZERO;
        BigDecimal sumNetUWofLastSixM = BigDecimal.ZERO;
        BigDecimal sumNetBDMofLastSixM = BigDecimal.ZERO;

        for (BankStmtView tmbBankStmtView : Util.safetyList(bankStmtSummaryView.getTmbBankStmtViewList())) {
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, tmbBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = Util.add(grdTotalAvgOSBalance, tmbBankStmtView.getAvgOSBalanceAmount());

            isCountIncome = tmbBankStmtView.getNotCountIncome() == 0;
            if (isCountIncome) {
                tmbTotalIncomeGross = Util.add(tmbTotalIncomeGross, tmbBankStmtView.getAvgIncomeGross());
                tmbTotalIncomeNetBDM = Util.add(tmbTotalIncomeNetBDM, tmbBankStmtView.getAvgIncomeNetBDM());
                tmbTotalIncomeNetUW = Util.add(tmbTotalIncomeNetUW, tmbBankStmtView.getAvgIncomeNetUW());
            }

            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(tmbBankStmtView.getBankStmtDetailViewList())) {
                if (isCountIncome) {
                    sumChqRetAmtCountIncomeOfLastSizM = Util.add(sumChqRetAmtCountIncomeOfLastSizM, detailView.getChequeReturnAmount());
                    sumNetUWofLastSixM = Util.add(sumNetUWofLastSixM, detailView.getCreditAmountUW());
                    sumNetBDMofLastSixM = Util.add(sumNetBDMofLastSixM, detailView.getCreditAmountBDM());
                }

            }

        }

        for (BankStmtView othBankStmtView : Util.safetyList(bankStmtSummaryView.getOthBankStmtViewList())) {
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, othBankStmtView.getTrdChequeReturnAmount());
            grdTotalAvgOSBalance = Util.add(grdTotalAvgOSBalance, othBankStmtView.getAvgOSBalanceAmount());

            isCountIncome = othBankStmtView.getNotCountIncome() == 0;
            if (isCountIncome) {
                othTotalIncomeGross = Util.add(othTotalIncomeGross, othBankStmtView.getAvgIncomeGross());
                othTotalIncomeNetBDM = Util.add(othTotalIncomeNetBDM, othBankStmtView.getAvgIncomeNetBDM());
                othTotalIncomeNetUW = Util.add(othTotalIncomeNetUW, othBankStmtView.getAvgIncomeNetUW());
            }

            for (BankStmtDetailView detailView : getLastSixMonthBankStmtDetails(othBankStmtView.getBankStmtDetailViewList())) {
                if (isCountIncome) {
                    sumChqRetAmtCountIncomeOfLastSizM = Util.add(sumChqRetAmtCountIncomeOfLastSizM, detailView.getChequeReturnAmount());
                    sumNetUWofLastSixM = Util.add(sumNetUWofLastSixM, detailView.getCreditAmountUW());
                    sumNetBDMofLastSixM = Util.add(sumNetBDMofLastSixM, detailView.getCreditAmountBDM());
                }

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
        BigDecimal grdTotalIncomeGross = Util.add(tmbTotalIncomeGross, othTotalIncomeGross);
        BigDecimal grdTotalIncomeNetBDM = Util.add(tmbTotalIncomeNetBDM, othTotalIncomeNetBDM);
        BigDecimal grdTotalIncomeNetUW = Util.add(tmbTotalIncomeNetUW, othTotalIncomeNetUW);

        BigDecimal grdTotalTrdChqRetPercent = Util.multiply(Util.divide(sumChqRetAmtCountIncomeOfLastSizM, isRoleUW ? sumNetUWofLastSixM : sumNetBDMofLastSixM), Util.ONE_HUNDRED);
//        BigDecimal grdTotalTrdChqRetPercent = Util.multiply(Util.divide(grdTotalTrdChqRetAmount, isRoleUW ? grdTotalIncomeNetUW : grdTotalIncomeNetBDM), Util.ONE_HUNDRED);
        if (grdTotalTrdChqRetPercent != null) {
            grdTotalTrdChqRetPercent = grdTotalTrdChqRetPercent.setScale(2, RoundingMode.HALF_UP);
        }

        bankStmtSummaryView.setGrdTotalIncomeGross(grdTotalIncomeGross);
        bankStmtSummaryView.setGrdTotalIncomeNetBDM(grdTotalIncomeNetBDM);
        bankStmtSummaryView.setGrdTotalIncomeNetUW(grdTotalIncomeNetUW);
        bankStmtSummaryView.setGrdTotalTDChqRetAmount(grdTotalTrdChqRetAmount);
        bankStmtSummaryView.setGrdTotalTDChqRetPercent(grdTotalTrdChqRetPercent);
        bankStmtSummaryView.setGrdTotalAvgOSBalanceAmount(grdTotalAvgOSBalance);

        if (isBorrower) {
            bankStmtSummaryView.setGrdTotalBorrowerIncomeGross(grdTotalIncomeGross);
            bankStmtSummaryView.setGrdTotalBorrowerIncomeNetBDM(grdTotalIncomeNetBDM);
            bankStmtSummaryView.setGrdTotalBorrowerIncomeNetUW(grdTotalIncomeNetUW);
        }
    }

    public void setSummaryColor(BankStmtSummaryView bankStmtSummaryView, long workCaseId) {
        //ref. CA Web Summary Formula document

        // NOT Doing if non or create new Bank statement summary
        if (bankStmtSummaryView == null || bankStmtSummaryView.getId() == 0) {
            return;
        }
        // NOT Doing if no any TMB Bank statements
        if (bankStmtSummaryView.getTmbBankStmtViewList() == null || bankStmtSummaryView.getTmbBankStmtViewList().size() == 0) {
            return;
        }

        /*BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if (basicInfo != null && basicInfo.getProductGroup() != null) {
            ProductGroup productGroup = basicInfo.getProductGroup();*/
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if (workCase != null && workCase.getProductGroup() != null) {
            ProductGroup productGroup = workCase.getProductGroup();

            // if Product = (TMB SmartBiz | TMB SME O/D No Asset)
            if (ProductProgramType.TMB_SME_SMARTBIZ.code().equalsIgnoreCase(productGroup.getBrmsCode())
                || ProductProgramType.OD_NO_ASSET.code().equalsIgnoreCase(productGroup.getBrmsCode())) {

                BigDecimal two = new BigDecimal("2");
                BigDecimal three = new BigDecimal("3");
                BigDecimal five = new BigDecimal("5");
                BigDecimal seven = new BigDecimal("7");
                BigDecimal fifteen = new BigDecimal("15");
                BigDecimal ninety = new BigDecimal("90");

                for (BankStmtView bankStmtView : Util.safetyList(bankStmtSummaryView.getTmbBankStmtViewList())) {

                    // Is Main Account
                    if (RadioValue.YES.value() == bankStmtView.getMainAccount()) {
                        /*
                        1. Income Gross
                            - Income Gross <, > 0   -> Green
                            - Income Gross = 0      -> Red
                         */
                        if (bankStmtView.getAvgIncomeGross() != null) {
                            if (ValidationUtil.isValueCompareToZero(bankStmtView.getAvgIncomeGross(), ValidationUtil.CompareMode.EQUAL)) {
                                bankStmtView.setColorIncomeGross(UWResultColor.RED.code());
                            } else {
                                bankStmtView.setColorIncomeGross(UWResultColor.GREEN.code());
                            }
                        } else {
                            bankStmtView.setColorIncomeGross("");
                        }

                        /*
                        2. Swing & Utilization (%)
                            - Utilization <= 90%                 -> Green
                            - Utilization > 90% & Swing >= 15%   -> Yellow
                            - Utilization > 90% & Swing < 15%    -> Red
                         */
                        if (bankStmtView.getAvgSwingPercent() != null && bankStmtView.getAvgUtilizationPercent() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgUtilizationPercent(), ninety, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                                bankStmtView.setColorSwingPercent(UWResultColor.GREEN.code());
                                bankStmtView.setColorUtilPercent(UWResultColor.GREEN.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgUtilizationPercent(), ninety, ValidationUtil.CompareMode.GREATER_THAN)
                                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgSwingPercent(), fifteen, ValidationUtil.CompareMode.GREATER_THAN_OR_EQUAL)) {
                                bankStmtView.setColorSwingPercent(UWResultColor.YELLOW.code());
                                bankStmtView.setColorUtilPercent(UWResultColor.YELLOW.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgUtilizationPercent(), ninety, ValidationUtil.CompareMode.GREATER_THAN)
                                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getAvgSwingPercent(), fifteen, ValidationUtil.CompareMode.LESS_THAN)) {
                                bankStmtView.setColorSwingPercent(UWResultColor.RED.code());
                                bankStmtView.setColorUtilPercent(UWResultColor.RED.code());
                            }
                            else {
                                bankStmtView.setColorSwingPercent("");
                                bankStmtView.setColorUtilPercent("");
                            }
                        } else {
                            bankStmtView.setColorSwingPercent("");
                            bankStmtView.setColorUtilPercent("");
                        }

                        /*
                        3. Over Limit (Times)
                            - Over Limit (Times) < 1            -> Green
                            - Over Limit (Times) >= 1, <= 2     -> Yellow
                            - Over Limit (Times) > 2            -> Red
                         */
                        if (bankStmtView.getOverLimitTimes() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitTimes(), BigDecimal.ONE, ValidationUtil.CompareMode.LESS_THAN)) {
                                bankStmtView.setColorOvrLimitTime(UWResultColor.GREEN.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitTimes(), BigDecimal.ONE, ValidationUtil.CompareMode.GREATER_THAN_OR_EQUAL)
                                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitTimes(), two, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                                bankStmtView.setColorOvrLimitTime(UWResultColor.YELLOW.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitTimes(), two, ValidationUtil.CompareMode.GREATER_THAN)) {
                                bankStmtView.setColorOvrLimitTime(UWResultColor.RED.code());
                            }
                            else {
                                bankStmtView.setColorOvrLimitTime("");
                            }
                        } else {
                            bankStmtView.setColorOvrLimitTime("");
                        }

                        /*
                        4. Over Limit (Days)
                            - Over Limit (Days) <= 3        -> Green
                            - Over Limit (Days) > 3, <= 7   -> Yellow
                            - Over Limit (Days) > 7         -> Red
                         */
                        if (bankStmtView.getOverLimitDays() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitDays(), three, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                                bankStmtView.setColorOvrLimitDays(UWResultColor.GREEN.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitDays(), three, ValidationUtil.CompareMode.GREATER_THAN)
                                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitDays(), seven, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                                bankStmtView.setColorOvrLimitDays(UWResultColor.YELLOW.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getOverLimitDays(), seven, ValidationUtil.CompareMode.GREATER_THAN)) {
                                bankStmtView.setColorOvrLimitDays(UWResultColor.RED.code());
                            }
                            else {
                                bankStmtView.setColorOvrLimitDays("");
                            }
                        } else {
                            bankStmtView.setColorOvrLimitDays("");
                        }

                        /*
                        5. Cheque Return
                            - Cheque Return < 1         -> Green
                            - Cheque Return >= 1, <= 2  -> Yellow
                            - Cheque Return > 2         -> Red
                         */
                        if (bankStmtView.getChequeReturn() != null) {
                            if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getChequeReturn(), BigDecimal.ONE, ValidationUtil.CompareMode.LESS_THAN)) {
                                bankStmtView.setColorChequeReturn(UWResultColor.GREEN.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getChequeReturn(), BigDecimal.ONE, ValidationUtil.CompareMode.GREATER_THAN_OR_EQUAL)
                                    && ValidationUtil.isFirstCompareToSecond(bankStmtView.getChequeReturn(), two, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                                bankStmtView.setColorChequeReturn(UWResultColor.YELLOW.code());
                            }
                            else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getChequeReturn(), two, ValidationUtil.CompareMode.GREATER_THAN)) {
                                bankStmtView.setColorChequeReturn(UWResultColor.RED.code());
                            }
                            else {
                                bankStmtView.setColorChequeReturn("");
                            }
                        } else {
                            bankStmtView.setColorChequeReturn("");
                        }

                    }

                    // Is count income
                    // 6.Trade Cheque Return (%)
                    // - Trade Cheque Return <= 3%          -> Green
                    // - Trade Cheque Return > 3%, <= 5%    -> Yellow
                    // - Trade Cheque Return > 5%           -> Red
                    if ( !(RadioValue.YES.value() == bankStmtView.getNotCountIncome()) && bankStmtView.getTrdChequeReturnPercent() != null) {
                        if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getTrdChequeReturnPercent(), three, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                            bankStmtView.setColorTrdChqRetPercent(UWResultColor.GREEN.code());
                        }
                        else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getTrdChequeReturnPercent(), three, ValidationUtil.CompareMode.GREATER_THAN)
                                && ValidationUtil.isFirstCompareToSecond(bankStmtView.getTrdChequeReturnPercent(), five, ValidationUtil.CompareMode.LESS_THAN_OR_EQUAL)) {
                            bankStmtView.setColorTrdChqRetPercent(UWResultColor.YELLOW.code());
                        }
                        else if (ValidationUtil.isFirstCompareToSecond(bankStmtView.getTrdChequeReturnPercent(), five, ValidationUtil.CompareMode.GREATER_THAN)) {
                            bankStmtView.setColorTrdChqRetPercent(UWResultColor.RED.code());
                        }
                        else {
                            bankStmtView.setColorTrdChqRetPercent("");
                        }
                    }

                }// End Loop

            }
        }

    }

    public BankStmtView saveBankStmt(BankStmtView bankStmtView) {
        log.debug("saveBankStmt() bankStmtView: {}", bankStmtView);
        BankStatement returnBankStmt = bankStatementDAO.persist(bankStmtTransform.getBankStmtForPersist(bankStmtView, getCurrentUser()));
        return bankStmtTransform.getBankStmtView(returnBankStmt);
    }

    public BankStmtSummaryView saveBankStmtSummary(BankStmtSummaryView bankStmtSummaryView, long workCaseId, long workCasePrescreenId) {
        log.debug("saveBankStmtSummary() bankStmtSummaryView.id: {}, workCaseId: {}, workCasePrescreenId: {}",
                bankStmtSummaryView.getId(), workCaseId, workCasePrescreenId);

        WorkCase workCase = null;
        WorkCasePrescreen workCasePrescreen = null;
        BankStatementSummary bankStatementSummaryWorkCase = null;
        BankStatementSummary bankStatementSummaryWorkCasePreScreen = null;

        if (workCaseId != 0) {
            workCase = workCaseDAO.findById(workCaseId);
            bankStatementSummaryWorkCase = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
            //--Update flag in WorkCase ( for check before submit )
            workCase.setCaseUpdateFlag(1);
            workCaseDAO.persist(workCase);
        }

        if (workCasePrescreenId != 0) {
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
            bankStatementSummaryWorkCasePreScreen = bankStatementSummaryDAO.findByWorkcasePrescreenId(workCasePrescreenId);
        }

        User user = getCurrentUser();

        log.debug("saveBankStmtSummary bankStmtSummaryView : {}", bankStmtSummaryView);
        BankStatementSummary bankStatementSummary = bankStmtTransform.getBankStatementSummary(bankStmtSummaryView, user);
        log.debug("saveBankStmtSummary bankStmtSummary : {}", bankStatementSummary);

        if(bankStatementSummaryWorkCase!=null && bankStatementSummaryWorkCase.getId()!=0){
            bankStatementSummaryDAO.deleteById(bankStatementSummaryWorkCase.getId());
        }

        if(bankStatementSummaryWorkCasePreScreen!=null && bankStatementSummaryWorkCasePreScreen.getId()!=0){
            bankStatementSummaryDAO.deleteById(bankStatementSummaryWorkCasePreScreen.getId());
        }

        bankStatementSummary.setWorkCase(workCase);
        bankStatementSummary.setWorkCasePrescreen(workCasePrescreen);
        BankStatementSummary returnBankStmtSummary = bankStatementSummaryDAO.persist(bankStatementSummary);
        log.debug("persist BankStatementSummary: {}", bankStatementSummary);

        return bankStmtTransform.getBankStmtSummaryView(returnBankStmtSummary);
    }

    public BankStmtSummaryView saveBankStmtSumFullApp(BankStmtSummaryView bankStmtSummaryView, long workCaseId) {
        log.debug("saveBankStmtSumFullApp() bankStmtSummaryView.id: {}, workCaseId: {}", bankStmtSummaryView.getId(), workCaseId);
        BankStmtSummaryView returnBankStmtSumView = null;
        if (workCaseId != 0) {
            User user = getCurrentUser();
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            BankStatementSummary bankStmtSumForPersist = bankStmtTransform.getBankStatementSummary(bankStmtSummaryView, user);
            bankStmtSumForPersist.setWorkCase(workCase);
            BankStatementSummary returnBankStmtSummary = bankStatementSummaryDAO.persist(bankStmtSumForPersist);
            log.debug("After persist BankStatementSummary: {}", returnBankStmtSummary);
            returnBankStmtSumView = bankStmtTransform.getBankStmtSummaryView(returnBankStmtSummary);
        }
        return returnBankStmtSumView;
    }

    public void deleteBankStmtById(long bankStmtId) {
        log.debug("deleteBankStmt() bankStmtId: {}", bankStmtId);
        if (bankStmtId != 0) {
            BankStatement bankStatement = bankStatementDAO.findById(bankStmtId);
            bankStatementDAO.delete(bankStatement);
        }
    }

    public void deleteBankStmtList(List<BankStmtView> bankStmtViewList) {
        log.debug("deleteBankStmtList()");
        if (bankStmtViewList != null && bankStmtViewList.size() > 0) {
            log.debug("start delete Bank statement list, size: {}", bankStmtViewList.size());
            List<BankStatement> deleteList = new ArrayList<BankStatement>();
            int size = bankStmtViewList.size();
            for (int i=0; i<size; i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);
                if (bankStmtView.getId() != 0) {
                    deleteList.add(bankStatementDAO.findById(bankStmtView.getId()));
                }
            }
            bankStatementDAO.delete(deleteList);
        } else {
            log.debug("No more data to delete");
        }
    }

    // --------------- Source of Collateral Proof ---------------
    public void calSourceOfCollateralProof(BankStmtView bankStmtView) {
        log.debug("calSourceOfCollateralProof() bankStmtView: {}", bankStmtView);
        List<BankStmtDetailView> lastThreeMonthBankStmtDetail = getLastThreeMonthBankStmtDetails(bankStmtView.getBankStmtDetailViewList());
        List<BankStmtSrcOfCollateralProofView> srcOfCollateralProofViewList = bankStmtView.getSrcOfCollateralProofViewList();

        BankAccountType bankAccTypeFromBankStmt = null;
        if (bankStmtView.getBankAccountTypeView() != null && bankStmtView.getBankAccountTypeView().getId() != 0) {
            bankAccTypeFromBankStmt = bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeView().getId());
        }
        else if (bankStmtView.getOtherAccountType() != 0) {
            bankAccTypeFromBankStmt = bankAccountTypeDAO.findById(bankStmtView.getOtherAccountType());
        }
        /*  source.lastThreeMonth1 = BankStmtDetail.asOfDate( [T-x] )
            source.lastThreeMonth2 = BankStmtDetail.asOfDate( [T-x]+1 )
            source.lastThreeMonth3 = BankStmtDetail.asOfDate( [T-x]+2 )
         */
        // if source of collateral proof is already exist
        // re-calculate & replace the old data
        if ((srcOfCollateralProofViewList != null && srcOfCollateralProofViewList.size() > 0) && lastThreeMonthBankStmtDetail.size() == 3) {
            //Prevent NOT being index at 0 from ArrayList.subList()
            BankStmtDetailView detail_1 = null;
            BankStmtDetailView detail_2 = null;
            BankStmtDetailView detail_3 = null;
            Iterator<BankStmtDetailView> it = lastThreeMonthBankStmtDetail.iterator();
            while (it.hasNext()) {
                if (detail_1 == null) {
                    detail_1 = it.next();
                }
                else if (detail_2 == null) {
                    detail_2 = it.next();
                }
                else if (detail_3 == null) {
                    detail_3 = it.next();
                }
            }

            for (int i=0; i < srcOfCollateralProofViewList.size(); i++) {
                BankStmtSrcOfCollateralProofView srcOfCollateralProofView = srcOfCollateralProofViewList.get(i);
                if (i == 0) {
                    srcOfCollateralProofView.setDateOfMaxBalance(detail_1.getDateOfMaxBalance());
                    srcOfCollateralProofView.setMaxBalance( getMaxBalance(detail_1, bankAccTypeFromBankStmt) );
                } else if (i == 1){
                    srcOfCollateralProofView.setDateOfMaxBalance(detail_2.getDateOfMaxBalance());
                    srcOfCollateralProofView.setMaxBalance( getMaxBalance(detail_2, bankAccTypeFromBankStmt) );
                } else if (i == 2){
                    srcOfCollateralProofView.setDateOfMaxBalance(detail_3.getDateOfMaxBalance());
                    srcOfCollateralProofView.setMaxBalance( getMaxBalance(detail_3, bankAccTypeFromBankStmt) );
                }
            }
        } else {
            // create & add new source of collateral proof list
            srcOfCollateralProofViewList = new ArrayList<BankStmtSrcOfCollateralProofView>(3);
            for (BankStmtDetailView detailView : lastThreeMonthBankStmtDetail) {
                BankStmtSrcOfCollateralProofView srcOfCollateralProofView = new BankStmtSrcOfCollateralProofView();
                srcOfCollateralProofView.setDateOfMaxBalance(detailView.getDateOfMaxBalance());
                srcOfCollateralProofView.setMaxBalance( getMaxBalance(detailView, bankAccTypeFromBankStmt) );
                srcOfCollateralProofViewList.add(srcOfCollateralProofView);
            }
            bankStmtView.setSrcOfCollateralProofViewList(srcOfCollateralProofViewList);
        }

        BankAccountType currentAccType = bankAccountTypeDAO.getByShortName(BankAccountTypeEnum.CA.name());
        BankAccountType savingAccType = bankAccountTypeDAO.getByShortName(BankAccountTypeEnum.SA.name());
        BankAccountType othFDType = bankAccountTypeDAO.getByName(BankAccountTypeEnum.FD.name());
        BankAccountType othBOEType = bankAccountTypeDAO.getByName(BankAccountTypeEnum.BOE.name());

        bankStmtView.setAvgOSBalanceAmount( getAvgMaxBalance(bankStmtView, bankAccTypeFromBankStmt, savingAccType, currentAccType, othFDType, othBOEType) );
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
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

    public List<BankStmtDetailView> generateBankStmtDetail(int numberOfMonths, Date lastMonthDate) {
        List<BankStmtDetailView> bankStmtDetailViewList;
        bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
        Date date;
        for (int i=(numberOfMonths-1); i>=0; i--) {
            BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
            date = DateTimeUtil.getOnlyDatePlusMonth(lastMonthDate, -i);
            bankStmtDetailView.setAsOfDate(date);
            bankStmtDetailView.setDateOfMaxBalance(DateTimeUtil.getFirstDayOfMonth(date));
            bankStmtDetailView.setDateOfMinBalance(DateTimeUtil.getFirstDayOfMonth(date));
            bankStmtDetailViewList.add(bankStmtDetailView);
        }
        return bankStmtDetailViewList;
    }

}
