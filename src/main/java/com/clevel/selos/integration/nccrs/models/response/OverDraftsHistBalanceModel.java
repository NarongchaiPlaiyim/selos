package com.clevel.selos.integration.nccrs.models.response;
public class OverDraftsHistBalanceModel {
    private String asofdate;
    private String daypastdue;
    private String balance;

    public String getAsofdate() {
        return asofdate;
    }

    public String getDaypastdue() {
        return daypastdue;
    }

    public String getBalance() {
        return balance;
    }
}