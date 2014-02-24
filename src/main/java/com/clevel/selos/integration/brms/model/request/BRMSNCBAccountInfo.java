package com.clevel.selos.integration.brms.model.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class BRMSNCBAccountInfo implements Serializable{
    private String loanAccountStatus;
    private String loanAccountType;
    private boolean tmbFlag;
    private boolean nplFlag;
    private BigDecimal creditAmtAtNPLDate;
    private boolean tdrFlag;
    private int customerEntity;
    private String currentPaymentType;
    private String sixMonthPaymentType; //Payment Pattern in last 6 months.
    private String twelveMonthPaymentType; //Payment Pattern in last 12 months.
    private int numberOfOverDue;//Over Due 31-60 days within last 12 months.
    private int numberOfOverLimit;//Number of Over Limit last 6 months.
    private String accountCloseDateMonths; //Number of month from account close date;



}
