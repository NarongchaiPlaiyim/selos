package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ProfileNameModel implements Serializable {
    private String asofdate;
    private String thainame;
    private String engname;
    private String submittedby;

    public String getAsofdate() {
        return asofdate;
    }

    public String getThainame() {
        return thainame;
    }

    public String getEngname() {
        return engname;
    }

    public String getSubmittedby() {
        return submittedby;
    }

}
