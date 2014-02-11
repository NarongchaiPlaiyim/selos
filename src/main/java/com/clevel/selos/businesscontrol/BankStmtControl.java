package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountListModel;
import com.clevel.selos.integration.corebanking.model.customeraccount.CustomerAccountResult;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.ProductGroup;
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
        return retrieveBankStmtInterface(customerInfoViewList, expectedSubmitDate, RadioValue.NO.value());
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
     * if(creditAmountUW[(Net)-UW] is blank) -> timesOfAvgCredit(BDM/UW) both values will be blank <br/>
     * else -> timesOfAvgCredit = [ creditAmount(BDM/UW) / avgIncomeNet(BDM/UW) ]
     * @param creditAmount(BDM/UW)
     * @param avgIncomeNet(BDM/UW)
     * @param creditAmountUW
     * @return timesOfAvgCredit(BDM/UW)
     */
    public BigDecimal calTimesOfAvgCredit(BigDecimal creditAmount, BigDecimal avgIncomeNet, BigDecimal creditAmountUW) {
        if (creditAmountUW == null)
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
        if (maxBalance == null && minBalance == null && overLimitAmount == null)
            return null;

        BigDecimal diffMinMaxBalance = Util.subtract(maxBalance, minBalance);

        if (diffMinMaxBalance != null)
            return Util.divide(diffMinMaxBalance.abs(), overLimitAmount);
        else
            return null;
    }

    /**
     * Formula: Utilization(%) = [ monthBalance * (-1) ] / overLimitAmount
     * @param monthBalance
     * @param overLimitAmount
     * @return Utilization(%)
     */
    public BigDecimal calUtilizationPercent(BigDecimal monthBalance, BigDecimal overLimitAmount) {
        if (monthBalance == null && overLimitAmount == null)
            return null;

        return Util.divide(Util.multiply(monthBalance, BigDecimal.ONE.negate()), overLimitAmount);
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
                && ValidationUtil.isValueLessEqualZero(bankStmtDetailView.getMaxBalance()))
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
            if (detailView.getOverLimitAmount() != null && ValidationUtil.isValueGreaterThanZero(detailView.getOverLimitAmount())) {
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
                    if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeNetUW(), maxValue)) {
                        maxValue = bankStmtView.getAvgIncomeNetUW();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeNetUW(), maxValue)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
                    }
                }
                else if (bankStmtView.getAvgIncomeNetBDM() != null) {
                    if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeNetBDM(), maxValue)) {
                        maxValue = bankStmtView.getAvgIncomeNetBDM();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeNetBDM(), maxValue)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
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
                        if (ValidationUtil.isGreaterThan(bankStmtView.getAvgLimit(), maxValue)) {
                            maxValue = bankStmtView.getAvgLimit();
                            candidateMaxLimitList.clear();
                            candidateMaxLimitList.add(bankStmtView);
                            atId = bankStmtView.getId();
                        }
                        else if (ValidationUtil.isValueEqual(bankStmtView.getAvgLimit(), maxValue)) {
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
                            if (ValidationUtil.isGreaterThan(bankStmtView.getAvgWithDrawAmount(), maxValue)) {
                                maxValue = bankStmtView.getAvgWithDrawAmount();
                                candidateMaxDebitTxList.clear();
                                candidateMaxDebitTxList.add(bankStmtView);
                                atId = bankStmtView.getId();
                            }
                            else if (ValidationUtil.isValueEqual(bankStmtView.getAvgWithDrawAmount(), maxValue)) {
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
                                if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeGross(), maxValue)) {
                                    maxValue = bankStmtView.getAvgIncomeGross();
                                    candidateMaxCreditTxList.clear();
                                    candidateMaxCreditTxList.add(bankStmtView);
                                    atId = bankStmtView.getId();
                                }
                                else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeGross(), maxValue)) {
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
        if (bankStmtViewList != null) {
            BigDecimal maxAvgGrossInflowPerLimit = BigDecimal.ZERO;
            BankStmtView highestBankStmtView = null;
            int atIndex = 0;
            // find maxAvgGrossInflowPerLimit from all of BankStmt
            for (int i=0; i<bankStmtViewList.size(); i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);
                if (bankStmtView.getAvgGrossInflowPerLimit() != null && ValidationUtil.isGreaterThan(bankStmtView.getAvgGrossInflowPerLimit(), maxAvgGrossInflowPerLimit)) {
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
        if (bankStmtViewList != null) {
            List<BankStmtView> candidateMaxIncomeNetList = new ArrayList<BankStmtView>();
            BigDecimal maxValue = BigDecimal.ZERO;
            long atId = 0;

            BigDecimal maxAvgGrossInflowPerLimit = BigDecimal.ZERO;
            BankStmtView highestBankStmtView = null;
            int highestAtIndex = 0;

            for (int i=0; i<bankStmtViewList.size(); i++) {
                BankStmtView bankStmtView = bankStmtViewList.get(i);

                // find MAX AvgGrossInflowPerLimit
                if (bankStmtView.getAvgGrossInflowPerLimit() != null && ValidationUtil.isGreaterThan(bankStmtView.getAvgGrossInflowPerLimit(), maxAvgGrossInflowPerLimit)) {
                    maxAvgGrossInflowPerLimit = bankStmtView.getAvgGrossInflowPerLimit();
                    highestBankStmtView = bankStmtView;
                    highestAtIndex = i;
                }

                // skip to next, if BankStmt is not Borrower or does not have any ODLimit within the last Six month
                if (!isBorrowerAndHasODLimit(bankStmtView))
                    continue;

                // find MAX AvgIncomeNet(UW/BDM)
                if (bankStmtView.getAvgIncomeNetUW() != null) {
                    if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeNetUW(), maxValue)) {
                        maxValue = bankStmtView.getAvgIncomeNetUW();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeNetUW(), maxValue)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
                    }
                }
                else if (bankStmtView.getAvgIncomeNetBDM() != null) {
                    if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeNetBDM(), maxValue)) {
                        maxValue = bankStmtView.getAvgIncomeNetBDM();
                        candidateMaxIncomeNetList.clear();
                        candidateMaxIncomeNetList.add(bankStmtView);
                        atId = bankStmtView.getId();
                    }
                    else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeNetBDM(), maxValue)) {
                        candidateMaxIncomeNetList.add(bankStmtView);
                    }
                }
            }

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
                        if (ValidationUtil.isGreaterThan(bankStmtView.getAvgLimit(), maxValue)) {
                            maxValue = bankStmtView.getAvgLimit();
                            candidateMaxLimitList.clear();
                            candidateMaxLimitList.add(bankStmtView);
                            atId = bankStmtView.getId();
                        }
                        else if (ValidationUtil.isValueEqual(bankStmtView.getAvgLimit(), maxValue)) {
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
                            if (ValidationUtil.isGreaterThan(bankStmtView.getAvgWithDrawAmount(), maxValue)) {
                                maxValue = bankStmtView.getAvgWithDrawAmount();
                                candidateMaxDebitTxList.clear();
                                candidateMaxDebitTxList.add(bankStmtView);
                                atId = bankStmtView.getId();
                            }
                            else if (ValidationUtil.isValueEqual(bankStmtView.getAvgWithDrawAmount(), maxValue)) {
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
                                if (ValidationUtil.isGreaterThan(bankStmtView.getAvgIncomeGross(), maxValue)) {
                                    maxValue = bankStmtView.getAvgIncomeGross();
                                    candidateMaxCreditTxList.clear();
                                    candidateMaxCreditTxList.add(bankStmtView);
                                    atId = bankStmtView.getId();
                                }
                                else if (ValidationUtil.isValueEqual(bankStmtView.getAvgIncomeGross(), maxValue)) {
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
        updateMainAccAndHighestInflow(bankStmtSummaryView.getTmbBankStmtViewList());
        updateMainAccAndHighestInflow(bankStmtSummaryView.getOthBankStmtViewList());
    }

    public void bankStmtDetailCalculation(BankStmtView bankStmtView, int seasonalFlag) {
        if (bankStmtView == null
            || bankStmtView.getBankStmtDetailViewList() == null
            || bankStmtView.getBankStmtDetailViewList().size() == 0)
            return;

        List<BankStmtDetailView> bankStmtDetailViewList = bankStmtView.getBankStmtDetailViewList();
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

        Bank bank = bankDAO.findById(bankStmtView.getBankView().getCode());
        if (bank != null) {
            bankStmtView.setTMB(BankType.TMB.shortName().equalsIgnoreCase(bank.getShortName()) ? "Y" : "N");
        } else {
            bankStmtView.setTMB("");
        }

    }

    public void bankStmtSumTotalCalculation(BankStmtSummaryView bankStmtSummaryView, boolean isBorrower) {
        if (bankStmtSummaryView == null)
            return;

        BigDecimal tmbTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal tmbTotalIncomeNetUW = BigDecimal.ZERO;
        BigDecimal othTotalIncomeGross = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetBDM = BigDecimal.ZERO;
        BigDecimal othTotalIncomeNetUW = BigDecimal.ZERO;

        BigDecimal grdTotalTrdChqRetAmount = BigDecimal.ZERO;
        BigDecimal grdTotalTrdChqRetPercent = BigDecimal.ZERO;
        BigDecimal grdTotalAvgOSBalance = BigDecimal.ZERO;

        boolean useNetUWToCal = false;

        for (BankStmtView tmbBankStmtView : Util.safetyList(bankStmtSummaryView.getTmbBankStmtViewList())) {
            tmbTotalIncomeGross = Util.add(tmbTotalIncomeGross, tmbBankStmtView.getAvgIncomeGross());
            tmbTotalIncomeNetBDM = Util.add(tmbTotalIncomeNetBDM, tmbBankStmtView.getAvgIncomeNetBDM());
            tmbTotalIncomeNetUW = Util.add(tmbTotalIncomeNetUW, tmbBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, tmbBankStmtView.getTrdChequeReturnAmount());
            grdTotalTrdChqRetPercent = Util.add(grdTotalTrdChqRetPercent, tmbBankStmtView.getTrdChequeReturnPercent());
            grdTotalAvgOSBalance = Util.add(grdTotalAvgOSBalance, tmbBankStmtView.getAvgOSBalanceAmount());

            if (tmbBankStmtView.getAvgIncomeNetUW() != null) {
                useNetUWToCal = true;
            }
        }

        for (BankStmtView othBankStmtView : Util.safetyList(bankStmtSummaryView.getOthBankStmtViewList())) {
            othTotalIncomeGross = Util.add(othTotalIncomeGross, othBankStmtView.getAvgIncomeGross());
            othTotalIncomeNetBDM = Util.add(othTotalIncomeNetBDM, othBankStmtView.getAvgIncomeNetBDM());
            othTotalIncomeNetUW = Util.add(othTotalIncomeNetUW, othBankStmtView.getAvgIncomeNetUW());
            grdTotalTrdChqRetAmount = Util.add(grdTotalTrdChqRetAmount, othBankStmtView.getTrdChequeReturnAmount());
            grdTotalTrdChqRetPercent = Util.add(grdTotalTrdChqRetPercent, othBankStmtView.getTrdChequeReturnPercent());
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
        BigDecimal grdTotalIncomeGross = Util.add(tmbTotalIncomeGross, othTotalIncomeGross);
        BigDecimal grdTotalIncomeNetBDM = Util.add(tmbTotalIncomeNetBDM, othTotalIncomeNetBDM);
        BigDecimal grdTotalIncomeNetUW = Util.add(tmbTotalIncomeNetUW, othTotalIncomeNetUW);

//        BigDecimal grdTotalTrdChqRetPercent;
//        if (useNetUWToCal)
//            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetUW);
//        else
//            grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetBDM);

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

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if (basicInfo != null && basicInfo.getProductGroup() != null) {
            ProductGroup productGroup = basicInfo.getProductGroup();

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
                            if (ValidationUtil.isValueEqualZero(bankStmtView.getAvgIncomeGross())) {
                                bankStmtView.setColorIncomeGross(ColorStyleType.RED.code());
                            } else {
                                bankStmtView.setColorIncomeGross(ColorStyleType.GREEN.code());
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
                            if (ValidationUtil.isLessEqual(bankStmtView.getAvgUtilizationPercent(), ninety)) {
                                bankStmtView.setColorSwingPercent(ColorStyleType.GREEN.code());
                                bankStmtView.setColorUtilPercent(ColorStyleType.GREEN.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getAvgUtilizationPercent(), ninety)
                                    && ValidationUtil.isGreaterEqual(bankStmtView.getAvgSwingPercent(), fifteen)) {
                                bankStmtView.setColorSwingPercent(ColorStyleType.YELLOW.code());
                                bankStmtView.setColorUtilPercent(ColorStyleType.YELLOW.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getAvgUtilizationPercent(), ninety)
                                    && ValidationUtil.isLessThan(bankStmtView.getAvgSwingPercent(), fifteen)) {
                                bankStmtView.setColorSwingPercent(ColorStyleType.RED.code());
                                bankStmtView.setColorUtilPercent(ColorStyleType.RED.code());
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
                            if (ValidationUtil.isLessThan(bankStmtView.getOverLimitTimes(), BigDecimal.ONE)) {
                                bankStmtView.setColorOvrLimitTime(ColorStyleType.GREEN.code());
                            }
                            else if (ValidationUtil.isGreaterEqual(bankStmtView.getOverLimitTimes(), BigDecimal.ONE)
                                    && ValidationUtil.isLessEqual(bankStmtView.getOverLimitTimes(), two)) {
                                bankStmtView.setColorOvrLimitTime(ColorStyleType.YELLOW.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getOverLimitTimes(), two)) {
                                bankStmtView.setColorOvrLimitTime(ColorStyleType.RED.code());
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
                            if (ValidationUtil.isLessEqual(bankStmtView.getOverLimitDays(), three)) {
                                bankStmtView.setColorOvrLimitDays(ColorStyleType.GREEN.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getOverLimitDays(), three)
                                    && ValidationUtil.isLessEqual(bankStmtView.getOverLimitDays(), seven)) {
                                bankStmtView.setColorOvrLimitDays(ColorStyleType.YELLOW.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getOverLimitDays(), seven)) {
                                bankStmtView.setColorOvrLimitDays(ColorStyleType.RED.code());
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
                            if (ValidationUtil.isLessThan(bankStmtView.getChequeReturn(), BigDecimal.ONE)) {
                                bankStmtView.setColorChequeReturn(ColorStyleType.GREEN.code());
                            }
                            else if (ValidationUtil.isGreaterEqual(bankStmtView.getChequeReturn(), BigDecimal.ONE)
                                    && ValidationUtil.isLessEqual(bankStmtView.getChequeReturn(), two)) {
                                bankStmtView.setColorChequeReturn(ColorStyleType.YELLOW.code());
                            }
                            else if (ValidationUtil.isGreaterThan(bankStmtView.getChequeReturn(), two)) {
                                bankStmtView.setColorChequeReturn(ColorStyleType.RED.code());
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
                        if (ValidationUtil.isLessEqual(bankStmtView.getTrdChequeReturnPercent(), three)) {
                            bankStmtView.setColorTrdChqRetPercent(ColorStyleType.GREEN.code());
                        }
                        else if (ValidationUtil.isGreaterThan(bankStmtView.getTrdChequeReturnPercent(), three)
                                && ValidationUtil.isLessEqual(bankStmtView.getTrdChequeReturnPercent(), five)) {
                            bankStmtView.setColorTrdChqRetPercent(ColorStyleType.YELLOW.code());
                        }
                        else if (ValidationUtil.isGreaterThan(bankStmtView.getTrdChequeReturnPercent(), five)) {
                            bankStmtView.setColorTrdChqRetPercent(ColorStyleType.RED.code());
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
        if (workCaseId != 0) {
            workCase = workCaseDAO.findById(workCaseId);
        }
        if (workCasePrescreenId != 0) {
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        }
        User user = getCurrentUser();

        BankStatementSummary bankStatementSummary = bankStmtTransform.getBankStatementSummary(bankStmtSummaryView, user);
        bankStatementSummary.setWorkCase(workCase);
        bankStatementSummary.setWorkCasePrescreen(workCasePrescreen);
        BankStatementSummary returnBankStmtSummary = bankStatementSummaryDAO.persist(bankStatementSummary);
        log.debug("persist BankStatementSummary: {}", bankStatementSummary);
        return bankStmtTransform.getBankStmtSummaryView(returnBankStmtSummary);
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

    public void calSrcOfCollateral(BankStmtSummaryView summaryView) {
        // Calculate reference from CA Web Formula

    }

    public Date[] getSourceOfCollateralMonths(BankStmtSummaryView summaryView) {
        Date[] threeMonths = new Date[3];
        if (summaryView != null &&
            ((summaryView.getTmbBankStmtViewList() != null && !summaryView.getTmbBankStmtViewList().isEmpty())
              || (summaryView.getOthBankStmtViewList() != null && !summaryView.getOthBankStmtViewList().isEmpty()))) {

            Date maxDate = null;
            for (BankStmtView tmbBankStmtView : summaryView.getTmbBankStmtViewList()) {
                for (BankStmtDetailView detailView : tmbBankStmtView.getBankStmtDetailViewList()) {
                    if (maxDate == null) {
                        maxDate = detailView.getAsOfDate();
                    }
                    else if (DateTimeUtil.compareDate(detailView.getAsOfDate(), maxDate) > 1) {
                        maxDate = detailView.getAsOfDate();
                    }
                }
            }

            for (BankStmtView othBankStmtView : summaryView.getOthBankStmtViewList()) {
                for (BankStmtDetailView detailView : othBankStmtView.getBankStmtDetailViewList()) {
                    if (maxDate == null) {
                        maxDate = detailView.getAsOfDate();
                    }
                    else if (DateTimeUtil.compareDate(detailView.getAsOfDate(), maxDate) > 1) {
                        maxDate = detailView.getAsOfDate();
                    }
                }
            }

            threeMonths[0] = DateTimeUtil.getOnlyDatePlusMonth(maxDate, -2);
            threeMonths[1] = DateTimeUtil.getOnlyDatePlusMonth(maxDate, -1);
            threeMonths[2] = maxDate;
        }
        log.debug("getSourceOfCollateralMonths() threeMonths is empty!");
        return threeMonths;
    }

    public boolean isABDMorBDM() {
        User user = getCurrentUser();
        if (RoleUser.ABDM.getValue() == user.getRole().getId() || RoleUser.BDM.getValue() == user.getRole().getId())
            return true;
        else
            return false;
    }

    public boolean isUW() {
        User user = getCurrentUser();
        if (RoleUser.UW.getValue() == user.getRole().getId())
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
