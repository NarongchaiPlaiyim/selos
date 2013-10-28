package com.clevel.selos.integration.ncb.nccrs.models.response;

public class ExecutiveSummaryModel {
    private String totalaccounts;
    private String overdueaccounts;
    private String totaloutstanding;
    private String totalpastdueamount;
    private String highcreditlimit;
    private String initialcreditlimit;
    private String invsdsptremark;
    private String inqhistory;

    public String getTotalaccounts() {
        return totalaccounts;
    }

    public String getOverdueaccounts() {
        return overdueaccounts;
    }

    public String getTotaloutstanding() {
        return totaloutstanding;
    }

    public String getTotalpastdueamount() {
        return totalpastdueamount;
    }

    public String getHighcreditlimit() {
        return highcreditlimit;
    }

    public String getInitialcreditlimit() {
        return initialcreditlimit;
    }

    public String getInvsdsptremark() {
        return invsdsptremark;
    }

    public String getInqhistory() {
        return inqhistory;
    }
}
