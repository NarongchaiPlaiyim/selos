package com.clevel.selos.transform;

import com.clevel.selos.controller.BankStatementDetail;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.working.BankStatementDAO;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BankStatement;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class BankStmtTransform extends Transform{

    @Inject
    BankDAO bankDAO;

    @Inject
    BankTransform bankTransform;

    @Inject
    BankStatementSummaryDAO bankStatementSummaryDAO;

    @Inject
    BankStatementDAO bankStatementDAO;

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public BankStmtTransform(){

    }

    public BankStmtView getBankStmtView(DWHBankStatement dwhBankStatement){
        log.info("Transform BankStmtView with DWHBankStatement : {}", dwhBankStatement);
        BankStmtView bankStmtView = new BankStmtView();
        bankStmtView.setAccountNumber(dwhBankStatement.getAccountNumber());
        bankStmtView.setAccountName(dwhBankStatement.getAccountName());

        AccountStatusView accountStatusView = new AccountStatusView();
        accountStatusView.setId(dwhBankStatement.getAccountStatus());
        bankStmtView.setAccountStatusView(accountStatusView);

        AccountTypeView accountTypeView = new AccountTypeView();
        accountTypeView.setAccountType(dwhBankStatement.getDataSource()); //TODO: transform data source to account type



        BankView bankView = bankTransform.getBankView(bankDAO.getTMBBank());
        bankStmtView.setBankView(bankView);

        bankStmtView.setBranchName(dwhBankStatement.getBranchCode());
        log.info("Return BankStmtView : {}", bankStmtView);
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(DWHBankStatement dwhBankStatement){
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

    public BankStatementSummary getBankStatementSummary(BankStmtSummaryView bankStmtSummaryView, User user){
        BankStatementSummary bankStatementSummary = null;
        Date now = new Date();
        if(bankStmtSummaryView != null){
            if(bankStmtSummaryView.getId() != 0){
                bankStatementSummary = bankStatementSummaryDAO.findById(bankStmtSummaryView.getId());
                bankStatementSummary.setModifyBy(user);
                bankStatementSummary.setModifyDate(now);
            } else {
                bankStatementSummary = new BankStatementSummary();
                bankStatementSummary.setCreateBy(user);
                bankStatementSummary.setCreateDate(now);
                bankStatementSummary.setModifyBy(user);
                bankStatementSummary.setModifyDate(now);
            }
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

            List<BankStmtView> bankStmtViewList = bankStmtSummaryView.getBankStmtViewList();
            for(BankStmtView bankStmtView : bankStmtViewList){


            }
            bankStatementSummary.setId(bankStmtSummaryView.getId());

        }

        return null;
    }

    public BankStatement getBankStatement(BankStmtView bankStmtView, BankStatementSummary bankStatementSummary, User user){
        BankStatement bankStatement = null;
        if(bankStmtView != null){
            Date now = new Date();
            if(bankStmtView.getId() != 0){
                bankStatement = bankStatementDAO.findById(bankStmtView.getId());
                bankStatement.setModifyDate(now);
                bankStatement.setModifyBy(user);
            } else {
                bankStatement = new BankStatement();
                bankStatement.setCreateBy(user);
                bankStatement.setCreateDate(now);
                bankStatement.setModifyBy(user);
                bankStatement.setModifyDate(now);
            }

            bankStatement.setBank(bankDAO.findById(bankStmtView.getBankView().getCode()));
            bankStatement.setBranch(bankStmtView.getBranchName());
            bankStatement.setBankAccountType(bankAccountTypeDAO.findById(bankStatement.getBankAccountType().getId()));
            bankStatement.setAccountNo(bankStmtView.getAccountNumber());
            bankStatement.setAccountName(bankStmtView.getAccountName());

        }
        return bankStatement;
    }

    public BankStatementDetail getBankStatementDetail(){
        return null;
    }
}
