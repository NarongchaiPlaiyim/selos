package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.DWHBankDataSourceDAO;
import com.clevel.selos.dao.working.BankStatementDAO;
import com.clevel.selos.dao.working.BankStatementDetailDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementDetail;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;

import javax.inject.Inject;
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
    BankTransform bankTransform;
    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BankStmtTransform() {
    }

    public BankStmtView getBankStmtView(DWHBankStatement dwhBankStatement) {
        log.info("Transform BankStmtView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtView bankStmtView = new BankStmtView();
        bankStmtView.setAccountNumber(dwhBankStatement.getAccountNumber());
        bankStmtView.setAccountName(dwhBankStatement.getAccountName());

        DWHBankDataSource dwhBankDataSource = dwhBankDataSourceDAO.findByDataSource(dwhBankStatement.getDataSource());

        bankStmtView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(dwhBankDataSource.getBankAccountType()));

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

            bankStatementSummary.setTMBTotalIncomeGross(bankStmtSummaryView.getTMBTotalIncomeGross());
            bankStatementSummary.setTMBTotalIncomeNetBDM(bankStmtSummaryView.getTMBTotalIncomeNetBDM());
            bankStatementSummary.setTMBTotalIncomeNetUW(bankStmtSummaryView.getTMBTotalIncomeNetUW());

            bankStatementSummary.setOthTotalIncomeGross(bankStmtSummaryView.getOthTotalIncomeGross());
            bankStatementSummary.setOthTotalIncomeNetBDM(bankStmtSummaryView.getOthTotalIncomeNetBDM());
            bankStatementSummary.setOthTotalIncomeNetUW(bankStmtSummaryView.getTMBTotalIncomeNetUW());

            bankStatementSummary.setGrdTotalIncomeGross(bankStmtSummaryView.getGrdTotalIncomeGross());
            bankStatementSummary.setGrdTotalIncomeNetBDM(bankStmtSummaryView.getGrdTotalIncomeNetBDM());
            bankStatementSummary.setGrdTotalIncomeNetUW(bankStmtSummaryView.getGrdTotalIncomeNetUW());

            bankStatementSummary.setGrdTotalAvgOSBalanceAmount(bankStmtSummaryView.getGrdTotalAvgOSBalanceAmount());
            bankStatementSummary.setGrdTotalTDChqRetAmount(bankStmtSummaryView.getGrdTotalTDChqRetAmount());
            bankStatementSummary.setGrdTotalTDChqRetPercent(bankStmtSummaryView.getGrdTotalTDChqRetPercent());

            List<BankStatement> bankStatementList = new ArrayList<BankStatement>();
            List<BankStmtView> bankStmtViewList = bankStmtSummaryView.getBankStmtViewList();
            for (BankStmtView bankStmtView : bankStmtViewList) {
                bankStatementList.add(getBankStatement(bankStmtView, bankStatementSummary, user));
            }
            bankStatementSummary.setBankStmtList(bankStatementList);
            bankStatementSummary.setId(bankStmtSummaryView.getId());

        }

        return null;
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
            bankStatementDetail.setBankStatement(bankStatement);
        }
        return bankStatementDetail;
    }
}
