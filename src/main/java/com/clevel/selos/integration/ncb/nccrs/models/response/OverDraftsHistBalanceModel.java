package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class OverDraftsHistBalanceModel implements Serializable {
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
