package com.clevel.selos.integration.brms.model.request.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSAccountStmtInfo implements Serializable{
    private BigDecimal avgUtilizationPercent;
    private BigDecimal avgSwingPercent;
    private BigDecimal avgIncomeGross;
    private BigDecimal totalTransaction; // total of Inflow and Outflow Transaction
    private BigDecimal checkReturn;
    private BigDecimal overLimitDays; //OD Over limit Days;
    private BigDecimal avgGrossInflowPercentLimit;
    private boolean mainAccount;
    private boolean highestInflow;
    private boolean tmb;
    private boolean notCountIncome;

}
