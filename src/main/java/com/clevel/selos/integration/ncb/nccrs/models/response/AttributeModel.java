package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class AttributeModel implements Serializable {
    private String historicalbalancereport;

    public AttributeModel(String historicalbalancereport) {
        this.historicalbalancereport = historicalbalancereport;
    }

}
