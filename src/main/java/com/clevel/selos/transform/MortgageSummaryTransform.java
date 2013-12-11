package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.view.MortgageSummaryView;

import javax.inject.Inject;

public class MortgageSummaryTransform extends Transform {

    @Inject
    public MortgageSummaryTransform() {
    }

    public MortgageSummary transformToModel(MortgageSummaryView mortgageSummaryView){
        MortgageSummary mortgageSummary = new MortgageSummary();

        return mortgageSummary;
    }

    public MortgageSummaryView transformToView(MortgageSummary mortgageSummary){
        MortgageSummaryView mortgageSummaryView = new MortgageSummaryView();

        return mortgageSummaryView;
    }
}
