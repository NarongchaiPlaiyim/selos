package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ShareHolderInfoModel implements Serializable {
    private String name;
    private String asofdate;
    private String percentage;

    public String getName() {
        return name;
    }

    public String getAsofdate() {
        return asofdate;
    }

    public String getPercentage() {
        return percentage;
    }
}
