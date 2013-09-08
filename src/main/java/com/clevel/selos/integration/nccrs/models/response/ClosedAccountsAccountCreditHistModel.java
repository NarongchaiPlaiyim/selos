package com.clevel.selos.integration.nccrs.models.response;
public class ClosedAccountsAccountCreditHistModel {
    private String asofdate;
    private String outstanding;
    private String daypastdue;

    public String getAsofdate() {
        return asofdate;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public String getDaypastdue() {
        return daypastdue;
    }
}