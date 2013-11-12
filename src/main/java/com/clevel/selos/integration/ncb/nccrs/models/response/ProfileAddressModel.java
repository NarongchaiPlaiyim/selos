package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ProfileAddressModel implements Serializable {
    private String asofdate;
    private String address;
    private String submittedby;

    public String getAsofdate() {
        return asofdate;
    }

    public String getAddress() {
        return address;
    }

    public String getSubmittedby() {
        return submittedby;
    }
}
