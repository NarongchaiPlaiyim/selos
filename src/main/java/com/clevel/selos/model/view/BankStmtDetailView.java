package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankStmtDetailView implements Serializable{
    private BigDecimal overLimitAmount;
    private BigDecimal grossCreditBalance;
    private int numberOfCreditTxn;
    private BigDecimal excludeListBDM;
    private BigDecimal excludeListUW;
    private BigDecimal creditAmountBDM;
    private BigDecimal creditAmountUW;
    private BigDecimal timesOfAverageCreditBDM;
    private BigDecimal timesOfAverageCreditUW;
    private BigDecimal debitAmount;
    private int numberOfDebitTxn;
    private Date dateOfMaxBalance;
    private BigDecimal maxBalance;
    private Date dateOfMinBalance;
    private BigDecimal minBalance;
    private BigDecimal monthBalance;
    private int numberOfChequeReturn;
    private BigDecimal chequeReturnAmount;
    private int overLimitTimes;
    private int overLimitDays;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal grossInflowPerLimit;
    private int totalTransaction;
}
