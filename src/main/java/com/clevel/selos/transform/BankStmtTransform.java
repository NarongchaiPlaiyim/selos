package com.clevel.selos.transform;

import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;

import javax.inject.Inject;

public class BankStmtTransform extends Transform{

    @Inject
    BankDAO bankDAO;

    @Inject
    BankTransform bankTransform;

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
        accountTypeView.setAccountType(dwhBankStatement.getAccountType());

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
}
