package com.clevel.selos.integration.brms.model.request.data;

import java.math.BigDecimal;

public class NcbAccountLevel {
    public String accountId;
    public boolean nplFlag;
    public BigDecimal creditAmountAtFirstNPLDate;
    public boolean tdrStatusFromNCB;
    public int accountClosePeriod;
    public int typeOfCurrentPayment; //enum
    public int typeOf12monthsPayment; //enum
    public int numberODIn12Months;
    public int typeOf6monthsPayment; //enum
    public int numberODIn6Months;
    public int ncbAccountStatus; //enum
    //todo add more field

    public NcbAccountLevel() {
    }

    public NcbAccountLevel(String accountId, boolean nplFlag, BigDecimal creditAmountAtFirstNPLDate, boolean tdrStatusFromNCB, int accountClosePeriod, int typeOfCurrentPayment, int typeOf12monthsPayment, int numberODIn12Months, int typeOf6monthsPayment, int numberODIn6Months, int ncbAccountStatus) {
        this.accountId = accountId;
        this.nplFlag = nplFlag;
        this.creditAmountAtFirstNPLDate = creditAmountAtFirstNPLDate;
        this.tdrStatusFromNCB = tdrStatusFromNCB;
        this.accountClosePeriod = accountClosePeriod;
        this.typeOfCurrentPayment = typeOfCurrentPayment;
        this.typeOf12monthsPayment = typeOf12monthsPayment;
        this.numberODIn12Months = numberODIn12Months;
        this.typeOf6monthsPayment = typeOf6monthsPayment;
        this.numberODIn6Months = numberODIn6Months;
        this.ncbAccountStatus = ncbAccountStatus;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(boolean nplFlag) {
        this.nplFlag = nplFlag;
    }

    public BigDecimal getCreditAmountAtFirstNPLDate() {
        return creditAmountAtFirstNPLDate;
    }

    public void setCreditAmountAtFirstNPLDate(BigDecimal creditAmountAtFirstNPLDate) {
        this.creditAmountAtFirstNPLDate = creditAmountAtFirstNPLDate;
    }

    public boolean isTdrStatusFromNCB() {
        return tdrStatusFromNCB;
    }

    public void setTdrStatusFromNCB(boolean tdrStatusFromNCB) {
        this.tdrStatusFromNCB = tdrStatusFromNCB;
    }

    public int getAccountClosePeriod() {
        return accountClosePeriod;
    }

    public void setAccountClosePeriod(int accountClosePeriod) {
        this.accountClosePeriod = accountClosePeriod;
    }

    public int getTypeOfCurrentPayment() {
        return typeOfCurrentPayment;
    }

    public void setTypeOfCurrentPayment(int typeOfCurrentPayment) {
        this.typeOfCurrentPayment = typeOfCurrentPayment;
    }

    public int getTypeOf12monthsPayment() {
        return typeOf12monthsPayment;
    }

    public void setTypeOf12monthsPayment(int typeOf12monthsPayment) {
        this.typeOf12monthsPayment = typeOf12monthsPayment;
    }

    public int getNumberODIn12Months() {
        return numberODIn12Months;
    }

    public void setNumberODIn12Months(int numberODIn12Months) {
        this.numberODIn12Months = numberODIn12Months;
    }

    public int getTypeOf6monthsPayment() {
        return typeOf6monthsPayment;
    }

    public void setTypeOf6monthsPayment(int typeOf6monthsPayment) {
        this.typeOf6monthsPayment = typeOf6monthsPayment;
    }

    public int getNumberODIn6Months() {
        return numberODIn6Months;
    }

    public void setNumberODIn6Months(int numberODIn6Months) {
        this.numberODIn6Months = numberODIn6Months;
    }

    public int getNcbAccountStatus() {
        return ncbAccountStatus;
    }

    public void setNcbAccountStatus(int ncbAccountStatus) {
        this.ncbAccountStatus = ncbAccountStatus;
    }
}
