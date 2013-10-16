package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BankStatementView implements Serializable {
    private int isNotCountIncome;
    private String bankName;
    private String brachName;
    private int accountTypeId;
    private int otherTypeId;
    private String accountNumber;
    private String accountName;
    private int accountStatusId;
    private int accountCharId;
    private List<BankStmtDetailView> bankStmtDetailViews;
    private BigDecimal averageIncomeGross;
    private BigDecimal averageIncomeNetBDM;
    private BigDecimal averageIncomeNetUW;
    private BigDecimal averageDrawAmount;
    private BigDecimal swingPercent;
    private BigDecimal utilizationPercent;
    private BigDecimal averageGrossInflowPerLimit;
    private int chequeReturn;
    private BigDecimal tradeChequeReturnAmount;
    private int overLimitTimes;
    private int overLimitDays;
    private String remark;
}
