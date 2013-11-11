package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ProfileIdModel implements Serializable {
    private String asofdate;
    private String idtypeandnumber;
    private String submittedby;

    public String getAsofdate() {
        return asofdate;
    }

    public String getIdtypeandnumber() {
        return idtypeandnumber;
    }

    public String getSubmittedby() {
        return submittedby;
    }
}
