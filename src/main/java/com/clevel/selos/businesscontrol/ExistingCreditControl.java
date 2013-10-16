package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.model.Obligation;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditView;

import javax.inject.Inject;
import java.util.List;

public class ExistingCreditControl extends BusinessControl{

    @Inject
    DWHInterface dwhInterface;

    @Inject
    ReferenceDAO referenceDAO;

    public ExistingCreditView getExistingCredit(List<CustomerInfoView> customerInfoViewList){
        log.debug("Start GetExistingCredit with borrowers{}, related{}");

        for(CustomerInfoView customerInfoView : customerInfoViewList){

        }

        List<Obligation> obligationList = dwhInterface.getObligation(getCurrentUserID(), null);

        ExistingCreditView existingCreditView = new ExistingCreditView();

        List<ExistingCreditDetailView> _borrowerComExistingCredit;
        List<ExistingCreditDetailView> _borrowerRetailExistingCredit;
        List<ExistingCreditDetailView> _borrowerAppInRLOSCredit;
        List<ExistingCreditDetailView> _relatedComExistingCredit;
        List<ExistingCreditDetailView> _relatedRetailExistingCredit;
        List<ExistingCreditDetailView> _relatedAppInRLOSCredit;

        for(Obligation obligation : obligationList){


        }
        //dwhInterface.getObligation(tmbCusIDList);

        return existingCreditView;
    }

}
