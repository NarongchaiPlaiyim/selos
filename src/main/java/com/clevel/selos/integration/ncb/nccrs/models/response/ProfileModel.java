package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    private String thainame;
    private String engname;
    private String registtype;
    private String registid;
    private String taxid;
    private String registdate;
    private String address;
    private AdditionalModel additional;

    public String getThainame() {
        return thainame;
    }

    public String getEngname() {
        return engname;
    }

    public String getRegisttype() {
        return registtype;
    }

    public String getRegistid() {
        return registid;
    }

    public String getTaxid() {
        return taxid;
    }

    public String getRegistdate() {
        return registdate;
    }

    public String getAddress() {
        return address;
    }

    public AdditionalModel getAdditional() {
        return additional;
    }
}
