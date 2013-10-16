package com.clevel.selos.businesscontrol;

import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.model.view.ExistingCreditView;

import javax.inject.Inject;
import java.util.List;

public class ExistingCreditControl extends BusinessControl{

    @Inject
    DWHInterface dwhInterface;

    public List<ExistingCreditView> getExistingCredit(List<String> tmbCusIDList){
        log.debug("Start GetExistingCredit with {}", tmbCusIDList);

        dwhInterface.getObligation(getCurrentUserID(), tmbCusIDList);
        //dwhInterface.getObligation(tmbCusIDList);

        return null;
    }


}
