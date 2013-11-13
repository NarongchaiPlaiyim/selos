package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class HistoricalBalanceReportModel implements Serializable {
    private NonOverDraftsModel nonoverdrafts;
    private OverDraftsModel overdrafts;

    public NonOverDraftsModel getNonoverdrafts() {
        return nonoverdrafts;
    }

    public OverDraftsModel getOverdrafts() {
        return overdrafts;
    }
}
