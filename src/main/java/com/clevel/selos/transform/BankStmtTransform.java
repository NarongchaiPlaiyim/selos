package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.DWHBankDataSourceDAO;
import com.clevel.selos.dao.working.BankStatementDAO;
import com.clevel.selos.dao.working.BankStatementDetailDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementDetail;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.BankStmtSrcOfCollateralProof;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankStmtTransform extends Transform {
    //DAO
    @Inject
    BankDAO bankDAO;
    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    BankStatementDAO bankStatementDAO;
    @Inject
    BankStatementDetailDAO bankStatementDetailDAO;
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    DWHBankDataSourceDAO dwhBankDataSourceDAO;
    @Inject
    AccountStatusDAO accountStatusDAO;

    //Transform
    @Inject
    AccountStatusTransform accountStatusTransform;
    @Inject
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BankStmtTransform() {
    }

    //================================================== Get View ==================================================//
    public BankStmtView getBankStmtView(DWHBankStatement dwhBankStatement) {
        log.info("Transform BankStmtView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtView bankStmtView = new BankStmtView();
        bankStmtView.setAccountNumber(dwhBankStatement.getAccountNumber());
        bankStmtView.setAccountName(dwhBankStatement.getAccountName());

        DWHBankDataSource dwhBankDataSource = dwhBankDataSourceDAO.findByDataSource(dwhBankStatement.getDataSource());

        if(dwhBankDataSource != null){
            bankStmtView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(dwhBankDataSource.getBankAccountType()));
        } else {
            bankStmtView.setBankAccountTypeView(new BankAccountTypeView());
        }

        AccountTypeView accountTypeView = new AccountTypeView();
        accountTypeView.setAccountType(dwhBankStatement.getDataSource()); //TODO: transform data source to account type

        BankView bankView = bankTransform.getBankView(bankDAO.getTMBBank());
        bankStmtView.setBankView(bankView);

        bankStmtView.setBranchName(dwhBankStatement.getBranchCode());
        log.info("Return BankStmtView : {}", bankStmtView);
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(DWHBankStatement dwhBankStatement) {
        log.info("Transform BankStmtDetailView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
        bankStmtDetailView.setOverLimitAmount(dwhBankStatement.getOverLimitAmount());
        bankStmtDetailView.setGrossCreditBalance(dwhBankStatement.getGrossCreditBalance());
        bankStmtDetailView.setNumberOfCreditTxn(dwhBankStatement.getNumberOfCreditTxn());
        bankStmtDetailView.setDebitAmount(dwhBankStatement.getDebitAmount());
        bankStmtDetailView.setNumberOfDebitTxn(dwhBankStatement.getNumberOfDebitTxn());
        bankStmtDetailView.setHighestBalanceDate(dwhBankStatement.getHighestBalanceDate());
        bankStmtDetailView.setHighestBalance(dwhBankStatement.getHighestBalance());
        bankStmtDetailView.setLowestBalanceDate(dwhBankStatement.getLowestBalanceDate());
        bankStmtDetailView.setLowestBalance(dwhBankStatement.getLowestBalance());
        bankStmtDetailView.setNumberOfChequeReturn(dwhBankStatement.getNumberOfChequeReturn());
        bankStmtDetailView.setChequeReturnAmount(dwhBankStatement.getChequeReturnAmount());
        bankStmtDetailView.setAsOfDate(dwhBankStatement.getAsOfDate());
        bankStmtDetailView.setOverLimitDays(DateTimeUtil.daysBetween2Dates(dwhBankStatement.getStartODDate(), dwhBankStatement.getEndODDate()));
        bankStmtDetailView.setOverLimitTimes(dwhBankStatement.getNumberOfTimesOD());
        bankStmtDetailView.setOverLimitAmount(dwhBankStatement.getOverLimitAmount());

        log.info("Return BankStmtView : {}", bankStmtDetailView);
        return bankStmtDetailView;
    }

    public BankStmtSummaryView getBankStmtSummaryView(BankStatementSummary bankStatementSummary) {
        BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();
        if (bankStatementSummary == null) {
            log.debug("getBankStmtSummaryView() bankStatementSummary is null!");
            return bankStmtSummaryView;
        }
        bankStmtSummaryView.setId(bankStatementSummary.getId());
        bankStmtSummaryView.setSeasonal(bankStatementSummary.getSeasonal());
        bankStmtSummaryView.setExpectedSubmitDate(bankStatementSummary.getExpectedSubmitDate());
        bankStmtSummaryView.setTMBTotalIncomeGross(bankStatementSummary.getTMBTotalIncomeGross());
        bankStmtSummaryView.setTMBTotalIncomeNetBDM(bankStatementSummary.getTMBTotalIncomeNetBDM());
        bankStmtSummaryView.setTMBTotalIncomeNetUW(bankStatementSummary.getTMBTotalIncomeNetUW());

        bankStmtSummaryView.setOthTotalIncomeGross(bankStatementSummary.getOthTotalIncomeGross());
        bankStmtSummaryView.setOthTotalIncomeNetBDM(bankStatementSummary.getOthTotalIncomeNetBDM());
        bankStmtSummaryView.setOthTotalIncomeNetUW(bankStatementSummary.getOthTotalIncomeNetUW());

        bankStmtSummaryView.setGrdTotalIncomeGross(bankStatementSummary.getGrdTotalIncomeGross());
        bankStmtSummaryView.setGrdTotalIncomeNetBDM(bankStatementSummary.getGrdTotalIncomeNetBDM());
        bankStmtSummaryView.setGrdTotalIncomeNetUW(bankStatementSummary.getGrdTotalIncomeNetUW());
        bankStmtSummaryView.setGrdTotalTDChqRetAmount(bankStatementSummary.getGrdTotalTDChqRetAmount());
        bankStmtSummaryView.setGrdTotalTDChqRetPercent(bankStatementSummary.getGrdTotalTDChqRetPercent());
        bankStmtSummaryView.setGrdTotalAvgOSBalanceAmount(bankStatementSummary.getGrdTotalAvgOSBalanceAmount());

        List<BankStmtView> tmbBankStmtViewList = new ArrayList<BankStmtView>();
        List<BankStmtView> othBankStmtViewList = new ArrayList<BankStmtView>();

        Bank tmbBank = bankDAO.getTMBBank();
        for (BankStatement bankStmt : bankStatementSummary.getBankStmtList()) {
            if (tmbBank.getCode() == bankStmt.getBank().getCode()) {
                tmbBankStmtViewList.add(getBankStmtView(bankStmt));
            } else {
                othBankStmtViewList.add(getBankStmtView(bankStmt));
            }
        }
        bankStmtSummaryView.setTmbBankStmtViewList(tmbBankStmtViewList);
        bankStmtSummaryView.setOthBankStmtViewList(othBankStmtViewList);
        return bankStmtSummaryView;
    }

    public BankStmtView getBankStmtView(BankStatement bankStatement) {
        BankStmtView bankStmtView = new BankStmtView();
        if (bankStatement == null) {
            log.debug("getBankStmtView() bankStatement is null!");
            return bankStmtView;
        }
        bankStmtView.setId(bankStatement.getId());
        //todo: bankStmtView.setNotCountIncome();
        //bankStmtView.setNotCountIncome();
        bankStmtView.setBankView(bankTransform.getBankView(bankStatement.getBank()));
        bankStmtView.setBranchName(bankStatement.getBranch());
        bankStmtView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(bankStatement.getBankAccountType()));
        bankStmtView.setOtherAccountType(bankStatement.getOtherAccountType());
        bankStmtView.setAccountNumber(bankStatement.getAccountNo());
        bankStmtView.setAccountName(bankStatement.getAccountName());
        bankStmtView.setAccountStatusView(accountStatusTransform.transformToView(bankStatement.getAccountStatus()));
        bankStmtView.setMainAccount(bankStatement.getMainAccount());
        bankStmtView.setAccountCharacteristic(bankStatement.getAccountCharacteristic());
        bankStmtView.setLimit(bankStatement.getLimit());
        bankStmtView.setAvgIncomeGross(bankStatement.getAvgIncomeGross());
        bankStmtView.setAvgIncomeNetBDM(bankStatement.getAvgIncomeNetBDM());
        bankStmtView.setAvgIncomeNetUW(bankStatement.getAvgIncomeNetUW());
        bankStmtView.setAvgWithDrawAmount(bankStatement.getAvgDrawAmount());
        bankStmtView.setSwingPercent(bankStatement.getSwingPercent());
        bankStmtView.setUtilizationPercent(bankStatement.getUtilizationPercent());
        bankStmtView.setAvgGrossInflowPerLimit(bankStatement.getAvgGrossInflowPerLimit());
        bankStmtView.setChequeReturn(bankStatement.getChequeReturn());
        bankStmtView.setTrdChequeReturnAmount(bankStatement.getTdChequeReturnAmount());
        bankStmtView.setTrdChequeReturnPercent(bankStatement.getTdChequeReturnPercent());
        bankStmtView.setOverLimitTimes(bankStatement.getOverLimitTimes());
        bankStmtView.setOverLimitDays(bankStatement.getOverLimitDays());
        bankStmtView.setRemark(bankStatement.getRemark());
        bankStmtView.setAvgOSBalanceAmount(bankStatement.getAvgOSBalanceAmount());

        List<BankStmtDetailView> bankStmtDetailViewList = new ArrayList<BankStmtDetailView>();
        for (BankStatementDetail detail : bankStatement.getBankStatementDetailList()) {
            bankStmtDetailViewList.add(getBankStmtDetailView(detail));
        }
        bankStmtView.setBankStmtDetailViewList(bankStmtDetailViewList);

        List<BankStmtSrcOfCollateralProofView> srcCollateralProofList = new ArrayList<BankStmtSrcOfCollateralProofView>();
        for (BankStmtSrcOfCollateralProof srcOfCollateralProof : bankStatement.getSrcOfCollateralProofList()) {
            srcCollateralProofList.add(getSrcOfCollateralProofView(srcOfCollateralProof));
        }
        bankStmtView.setSrcOfCollateralProofViewList(srcCollateralProofList);
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(BankStatementDetail bankStatementDetail) {
        BankStmtDetailView bankStmtDetailView = new BankStmtDetailView();
        if (bankStatementDetail == null) {
            log.debug("getBankStmtDetailView() bankStatementDetail is null!");
            return bankStmtDetailView;
        }
        bankStmtDetailView.setId(bankStatementDetail.getId());
        bankStmtDetailView.setOverLimitAmount(bankStatementDetail.getOverLimitAmount());
        bankStmtDetailView.setGrossCreditBalance(bankStatementDetail.getGrossCreditBalance());
        bankStmtDetailView.setNumberOfCreditTxn(bankStatementDetail.getNumberOfCreditTxn());
        bankStmtDetailView.setExcludeListBDM(bankStatementDetail.getExcludeListBDM());
        bankStmtDetailView.setExcludeListUW(bankStatementDetail.getExcludeListUW());
        bankStmtDetailView.setCreditAmountBDM(bankStatementDetail.getCreditAmountBDM());
        bankStmtDetailView.setCreditAmountUW(bankStatementDetail.getCreditAmountUW());
        bankStmtDetailView.setTimesOfAvgCreditBDM(bankStatementDetail.getTimesOfAverageCreditBDM());
        bankStmtDetailView.setTimesOfAvgCreditUW(bankStatementDetail.getTimesOfAverageCreditUW());
        bankStmtDetailView.setDebitAmount(bankStatementDetail.getDebitAmount());
        bankStmtDetailView.setNumberOfDebitTxn(bankStatementDetail.getNumberOfDebitTxn());
        bankStmtDetailView.setHighestBalanceDate(bankStatementDetail.getHighestBalanceDate());
        bankStmtDetailView.setHighestBalance(bankStatementDetail.getHighestBalance());
        bankStmtDetailView.setLowestBalanceDate(bankStatementDetail.getLowestBalanceDate());
        bankStmtDetailView.setLowestBalance(bankStatementDetail.getLowestBalance());
        bankStmtDetailView.setMonthEndBalance(bankStatementDetail.getMonthEndBalance());
        bankStmtDetailView.setNumberOfChequeReturn(bankStatementDetail.getNumberOfChequeReturn());
        bankStmtDetailView.setChequeReturnAmount(bankStatementDetail.getChequeReturnAmount());
        bankStmtDetailView.setOverLimitTimes(bankStatementDetail.getOverLimitTimes());
        bankStmtDetailView.setOverLimitDays(bankStatementDetail.getOverLimitDays());
        bankStmtDetailView.setSwingPercent(bankStatementDetail.getSwingPercent());
        bankStmtDetailView.setUtilizationPercent(bankStatementDetail.getUtilizationPercent());
        bankStmtDetailView.setGrossInflowPerLimit(bankStatementDetail.getGrossInflowPerLimit());
        bankStmtDetailView.setTotalTransaction(bankStatementDetail.getTotalTransaction());
        bankStmtDetailView.setAsOfDate(bankStatementDetail.getAsOfDate());
        return bankStmtDetailView;
    }

    public BankStmtSrcOfCollateralProofView getSrcOfCollateralProofView(BankStmtSrcOfCollateralProof srcOfCollateralProof) {
        BankStmtSrcOfCollateralProofView srcOfCollateralProofView = new BankStmtSrcOfCollateralProofView();
        if (srcOfCollateralProof == null) {
            log.debug("getSrcOfCollateralProofView() srcOfCollateralProof is null!");
            return srcOfCollateralProofView;
        }
        srcOfCollateralProofView.setId(srcOfCollateralProof.getId());
        srcOfCollateralProofView.setDateOfMaxBalance(srcOfCollateralProof.getDateOfMaxBalance());
        srcOfCollateralProofView.setMaxBalance(srcOfCollateralProof.getMaxBalance());
        return srcOfCollateralProofView;
    }

    //================================================== Get Model ==================================================//
    public BankStatementSummary getBankStatementSummary(BankStmtSummaryView bankStmtSummaryView, User user) {
        BankStatementSummary bankStatementSummary = null;
        Date now = new Date();
        if (bankStmtSummaryView != null) {
            if (bankStmtSummaryView.getId() != 0) {
                bankStatementSummary = bankStatementSummaryDAO.findById(bankStmtSummaryView.getId());
            } else {
                bankStatementSummary = new BankStatementSummary();
                bankStatementSummary.setCreateBy(user);
                bankStatementSummary.setCreateDate(now);
            }
            bankStatementSummary.setModifyBy(user);
            bankStatementSummary.setModifyDate(now);

            bankStatementSummary.setSeasonal(bankStmtSummaryView.getSeasonal());
            bankStatementSummary.setExpectedSubmitDate(bankStmtSummaryView.getExpectedSubmitDate());

            //todo: Check bank statement summary Recalculate logic here
            // ----- Recalculate Total & Grand total ----- //
            BigDecimal tmbTotalIncomeGross = BigDecimal.ZERO;
            BigDecimal tmbTotalIncomeNetBDM = BigDecimal.ZERO;
            BigDecimal tmbTotalIncomeNetUW = BigDecimal.ZERO;

            BigDecimal othTotalIncomeGross = BigDecimal.ZERO;
            BigDecimal othTotalIncomeNetBDM = BigDecimal.ZERO;
            BigDecimal othTotalIncomeNetUW = BigDecimal.ZERO;

            // grdTotalTrdChqRetAmount = SUM(trdChequeReturnAmount)
            BigDecimal grdTotalTrdChqRetAmount = BigDecimal.ZERO;

            List<BankStatement> bankStatementList = new ArrayList<BankStatement>();
            for (BankStmtView tmbBankStmtView : bankStmtSummaryView.getTmbBankStmtViewList()) {
                tmbTotalIncomeGross = tmbTotalIncomeGross.add(tmbBankStmtView.getAvgIncomeGross());
                tmbTotalIncomeNetBDM = tmbTotalIncomeNetBDM.add(tmbBankStmtView.getAvgIncomeNetBDM());
                tmbTotalIncomeNetUW = tmbTotalIncomeNetUW.add(tmbBankStmtView.getAvgIncomeNetUW());
                grdTotalTrdChqRetAmount = grdTotalTrdChqRetAmount.add(tmbBankStmtView.getTrdChequeReturnAmount());

                bankStatementList.add(getBankStatement(tmbBankStmtView, bankStatementSummary, user));
            }

            for (BankStmtView othBankStmtView : bankStmtSummaryView.getOthBankStmtViewList()) {
                othTotalIncomeGross = othTotalIncomeGross.add(othBankStmtView.getAvgIncomeGross());
                othTotalIncomeNetBDM = othTotalIncomeNetBDM.add(othBankStmtView.getAvgIncomeNetBDM());
                othTotalIncomeNetUW = othTotalIncomeNetUW.add(othBankStmtView.getAvgIncomeNetUW());
                grdTotalTrdChqRetAmount = grdTotalTrdChqRetAmount.add(othBankStmtView.getTrdChequeReturnAmount());

                bankStatementList.add(getBankStatement(othBankStmtView, bankStatementSummary, user));
            }
            bankStatementSummary.setBankStmtList(bankStatementList);

            bankStatementSummary.setTMBTotalIncomeGross(tmbTotalIncomeGross);
            bankStatementSummary.setTMBTotalIncomeNetBDM(tmbTotalIncomeNetBDM);
            bankStatementSummary.setTMBTotalIncomeNetUW(tmbTotalIncomeNetUW);

            bankStatementSummary.setOthTotalIncomeGross(othTotalIncomeGross);
            bankStatementSummary.setOthTotalIncomeNetBDM(othTotalIncomeNetBDM);
            bankStatementSummary.setOthTotalIncomeNetUW(othTotalIncomeNetUW);

            // Grand total
            BigDecimal grdTotalIncomeGross = tmbTotalIncomeGross.add(othTotalIncomeGross);
            BigDecimal grdTotalIncomeNetBDM = tmbTotalIncomeNetBDM.add(othTotalIncomeNetBDM);
            BigDecimal grdTotalIncomeNetUW = tmbTotalIncomeNetUW.add(othTotalIncomeNetUW);

            // if (grdTotalIncomeNetUW = grdTotalTrdChqRetAmount / grdTotalIncomeNetBDM)
            // else (grdTotalTrdChqRetAmount / grdTotalIncomeNetUW)
            BigDecimal grdTotalTrdChqRetPercent = BigDecimal.ZERO;
            if (ValidationUtil.isValueEqual(grdTotalIncomeNetUW, Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetBDM))) {
                grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetBDM);
            } else {
                grdTotalTrdChqRetPercent = Util.divide(grdTotalTrdChqRetAmount, grdTotalIncomeNetUW);
            }
            bankStatementSummary.setGrdTotalIncomeGross(grdTotalIncomeGross);
            bankStatementSummary.setGrdTotalIncomeNetBDM(grdTotalIncomeNetBDM);
            bankStatementSummary.setGrdTotalIncomeNetUW(grdTotalIncomeNetUW);
            bankStatementSummary.setGrdTotalTDChqRetAmount(grdTotalTrdChqRetAmount);
            bankStatementSummary.setGrdTotalTDChqRetPercent(grdTotalTrdChqRetPercent);

            bankStatementSummary.setGrdTotalAvgOSBalanceAmount(bankStmtSummaryView.getGrdTotalAvgOSBalanceAmount());
        }
        return bankStatementSummary;
    }

    public BankStatement getBankStatement(BankStmtView bankStmtView, BankStatementSummary bankStatementSummary, User user) {
        BankStatement bankStatement = null;
        if (bankStmtView != null) {
            Date now = new Date();
            if (bankStmtView.getId() != 0) {
                bankStatement = bankStatementDAO.findById(bankStmtView.getId());
            } else {
                bankStatement = new BankStatement();
                bankStatement.setCreateBy(user);
                bankStatement.setCreateDate(now);
            }
            bankStatement.setModifyBy(user);
            bankStatement.setModifyDate(now);

            bankStatement.setBank(bankDAO.findById(bankStmtView.getBankView().getCode()));
            bankStatement.setBranch(bankStmtView.getBranchName());
            bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStmtView.getBankAccountTypeView().getId()));
            bankStatement.setAccountNo(bankStmtView.getAccountNumber());
            bankStatement.setAccountName(bankStmtView.getAccountName());
            bankStatement.setOtherAccountType(bankStmtView.getOtherAccountType());
            bankStatement.setAccountStatus(accountStatusDAO.findById(Integer.parseInt(bankStmtView.getAccountStatusView().getId())));
            bankStatement.setMainAccount(bankStmtView.getMainAccount());
            bankStatement.setAccountCharacteristic(bankStmtView.getAccountCharacteristic());
            bankStatement.setLimit(bankStmtView.getLimit());
            bankStatement.setAvgIncomeGross(bankStmtView.getAvgIncomeGross());
            bankStatement.setAvgIncomeNetBDM(bankStmtView.getAvgIncomeNetBDM());
            bankStatement.setAvgIncomeNetUW(bankStmtView.getAvgIncomeNetUW());
            bankStatement.setAvgDrawAmount(bankStmtView.getAvgWithDrawAmount());
            bankStatement.setSwingPercent(bankStmtView.getSwingPercent());
            bankStatement.setUtilizationPercent(bankStmtView.getUtilizationPercent());
            bankStatement.setAvgGrossInflowPerLimit(bankStmtView.getAvgGrossInflowPerLimit());
            bankStatement.setChequeReturn(bankStmtView.getChequeReturn());
            bankStatement.setTdChequeReturnAmount(bankStmtView.getTrdChequeReturnAmount());
            bankStatement.setTdChequeReturnPercent(bankStmtView.getTrdChequeReturnPercent());
            bankStatement.setOverLimitTimes(bankStmtView.getOverLimitTimes());
            bankStatement.setOverLimitDays(bankStmtView.getOverLimitDays());
            bankStatement.setRemark(bankStmtView.getRemark());
            bankStatement.setAvgOSBalanceAmount(bankStmtView.getAvgOSBalanceAmount());
            //set child list
            List<BankStatementDetail> bankStatementDetailList = new ArrayList<BankStatementDetail>();
            for (BankStmtDetailView detailView : bankStmtView.getBankStmtDetailViewList()) {
                bankStatementDetailList.add(getBankStatementDetail(detailView, bankStatement));
            }
            bankStatement.setBankStatementDetailList(bankStatementDetailList);
            //set parent
            bankStatement.setBankStatementSummary(bankStatementSummary);
        }
        return bankStatement;
    }

    public BankStatementDetail getBankStatementDetail(BankStmtDetailView bankStmtDetailView, BankStatement bankStatement) {
        BankStatementDetail bankStatementDetail = null;
        if (bankStmtDetailView != null) {
            if (bankStmtDetailView.getId() != 0) {
                bankStatementDetail = bankStatementDetailDAO.findById(bankStmtDetailView.getId());
            } else {
                bankStatementDetail = new BankStatementDetail();
            }
            bankStatementDetail.setOverLimitAmount(bankStmtDetailView.getOverLimitAmount());
            bankStatementDetail.setGrossCreditBalance(bankStmtDetailView.getGrossCreditBalance());
            bankStatementDetail.setNumberOfCreditTxn(bankStmtDetailView.getNumberOfCreditTxn());
            bankStatementDetail.setExcludeListBDM(bankStmtDetailView.getExcludeListBDM());
            bankStatementDetail.setExcludeListUW(bankStmtDetailView.getExcludeListUW());
            bankStatementDetail.setCreditAmountBDM(bankStmtDetailView.getCreditAmountBDM());
            bankStatementDetail.setCreditAmountUW(bankStmtDetailView.getCreditAmountUW());
            bankStatementDetail.setTimesOfAverageCreditBDM(bankStmtDetailView.getTimesOfAvgCreditBDM());
            bankStatementDetail.setTimesOfAverageCreditUW(bankStmtDetailView.getTimesOfAvgCreditUW());
            bankStatementDetail.setDebitAmount(bankStmtDetailView.getDebitAmount());
            bankStatementDetail.setNumberOfDebitTxn(bankStmtDetailView.getNumberOfDebitTxn());
            bankStatementDetail.setHighestBalanceDate(bankStmtDetailView.getHighestBalanceDate());
            bankStatementDetail.setHighestBalance(bankStmtDetailView.getHighestBalance());
            bankStatementDetail.setLowestBalanceDate(bankStmtDetailView.getLowestBalanceDate());
            bankStatementDetail.setLowestBalance(bankStmtDetailView.getLowestBalance());
            bankStatementDetail.setMonthEndBalance(bankStmtDetailView.getMonthEndBalance());
            bankStatementDetail.setNumberOfChequeReturn(bankStmtDetailView.getNumberOfChequeReturn());
            bankStatementDetail.setChequeReturnAmount(bankStmtDetailView.getChequeReturnAmount());
            bankStatementDetail.setOverLimitTimes(bankStmtDetailView.getOverLimitTimes());
            bankStatementDetail.setOverLimitDays(bankStmtDetailView.getOverLimitDays());
            bankStatementDetail.setSwingPercent(bankStmtDetailView.getSwingPercent());
            bankStatementDetail.setUtilizationPercent(bankStmtDetailView.getUtilizationPercent());
            bankStatementDetail.setGrossInflowPerLimit(bankStmtDetailView.getGrossInflowPerLimit());
            bankStatementDetail.setTotalTransaction(bankStmtDetailView.getTotalTransaction());
            bankStatementDetail.setAsOfDate(bankStmtDetailView.getAsOfDate());
            //set parent
            bankStatementDetail.setBankStatement(bankStatement);
        }
        return bankStatementDetail;
    }

}
