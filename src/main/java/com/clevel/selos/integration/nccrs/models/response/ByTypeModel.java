package com.clevel.selos.integration.nccrs.models.response;

public class ByTypeModel {
    private String credittype;
    private String totalaccounts;
    private String activeaccounts;
    private String closedaccounts;
    private String overdueaccounts;
    private String maxdaypastdue;
    private String installmentamount;
    private String creditlimit;
    private String outstanding;
    private String pastdueamount;

    public String getCredittype() {
        return credittype;
    }

    public String getTotalaccounts() {
        return totalaccounts;
    }

    public String getActiveaccounts() {
        return activeaccounts;
    }

    public String getClosedaccounts() {
        return closedaccounts;
    }

    public String getOverdueaccounts() {
        return overdueaccounts;
    }

    public String getMaxdaypastdue() {
        return maxdaypastdue;
    }

    public String getInstallmentamount() {
        return installmentamount;
    }

    public String getCreditlimit() {
        return creditlimit;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public String getPastdueamount() {
        return pastdueamount;
    }
}
