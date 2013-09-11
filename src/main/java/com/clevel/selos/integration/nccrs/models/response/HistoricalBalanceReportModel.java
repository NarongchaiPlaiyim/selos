package com.clevel.selos.integration.nccrs.models.response;
public class HistoricalBalanceReportModel {
    private NonOverDraftsModel nonoverdrafts;
    private OverDraftsModel overdrafts; 
    public NonOverDraftsModel getNonoverdrafts() {
        return nonoverdrafts;
    }

    public OverDraftsModel getOverdrafts() {
        return overdrafts;
    }
}
