package com.clevel.selos.transform.business;

import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.transform.Transform;

public class BizInfoSummaryTransform extends Transform {

    public BizInfoSummary transformToModel(BizInfoSummaryView bizInfoSummaryView){
        BizInfoSummary bizInfoSummary;

        bizInfoSummary = new BizInfoSummary();

        bizInfoSummary.setBizLocationDetail(bizInfoSummaryView.getBizLocationDetail());
        bizInfoSummary.setBizLocationName(bizInfoSummaryView.getBizLocationName());
        bizInfoSummary.setReduceInterestAmount(bizInfoSummaryView.getReduceInterestAmount());

        bizInfoSummary.setAddressBuilding(bizInfoSummaryView.getAddressBuilding());


        return bizInfoSummary;
    }
}
