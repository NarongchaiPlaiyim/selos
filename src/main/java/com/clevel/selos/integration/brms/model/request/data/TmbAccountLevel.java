package com.clevel.selos.integration.brms.model.request.data;

public class TmbAccountLevel {
    public String accountId;
    public int adjustClass; //enum
    public int numberOfMonthsTDRClosedDate;
    public boolean unpaidFeeInsurance;
    public boolean noPendingClaimLG;
    //todo add more field

    public TmbAccountLevel() {
    }

    public TmbAccountLevel(String accountId, int adjustClass, int numberOfMonthsTDRClosedDate, boolean unpaidFeeInsurance, boolean noPendingClaimLG) {
        this.accountId = accountId;
        this.adjustClass = adjustClass;
        this.numberOfMonthsTDRClosedDate = numberOfMonthsTDRClosedDate;
        this.unpaidFeeInsurance = unpaidFeeInsurance;
        this.noPendingClaimLG = noPendingClaimLG;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(int adjustClass) {
        this.adjustClass = adjustClass;
    }

    public int getNumberOfMonthsTDRClosedDate() {
        return numberOfMonthsTDRClosedDate;
    }

    public void setNumberOfMonthsTDRClosedDate(int numberOfMonthsTDRClosedDate) {
        this.numberOfMonthsTDRClosedDate = numberOfMonthsTDRClosedDate;
    }

    public boolean isUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(boolean unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public boolean isNoPendingClaimLG() {
        return noPendingClaimLG;
    }

    public void setNoPendingClaimLG(boolean noPendingClaimLG) {
        this.noPendingClaimLG = noPendingClaimLG;
    }
}
