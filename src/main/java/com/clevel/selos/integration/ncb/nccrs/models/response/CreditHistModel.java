package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class CreditHistModel implements Serializable {
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
