package com.clevel.selos.transform;

import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatement;
import com.clevel.selos.model.view.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BankStmtTransform {

    @Inject
    public BankStmtTransform(){

    }

    public BankStmtView getBankStmtView(DWHBankStatement dwhBankStatement){
        BankStmtView bankStmtView = new BankStmtView();
        bankStmtView.setAccountNumber(dwhBankStatement.getAccountNumber());
        bankStmtView.setAccountName(dwhBankStatement.getAccountName());

        AccountStatusView accountStatusView = new AccountStatusView();
        accountStatusView.setId(dwhBankStatement.getAccountStatus());
        bankStmtView.setAccountStatusView(accountStatusView);

        AccountTypeView accountTypeView = new AccountTypeView();
        accountTypeView.setAccountType(dwhBankStatement.getAccountType());

        BankView bankView = new BankView();
        //TODO: get the bank description from DB.
        bankView.setCode(7);
        bankView.setBankName("TMBBank");
        bankView.setBankShortName("TMB");
        bankStmtView.setBankView(bankView);

        bankStmtView.setBranchName(dwhBankStatement.getBranchCode());
        return bankStmtView;
    }

    public BankStmtDetailView getBankStmtDetailView(DWHBankStatement dwhBankStatement){

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
        return bankStmtDetailView;

    }



}
