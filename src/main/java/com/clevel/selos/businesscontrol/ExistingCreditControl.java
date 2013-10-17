package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ExistingCreditControl extends BusinessControl{

    @Inject
    DWHInterface dwhInterface;

    @Inject
    ReferenceDAO referenceDAO;

    public ExistingCreditView getExistingCredit(List<CustomerInfoView> borrowerList, List<CustomerInfoView> relatedList){
        log.debug("Start GetExistingCredit with borrowers{}, related{}");

        List<String> tmbCusIDList = new ArrayList<String>();
        getCustomerList(borrowerList, tmbCusIDList);
        log.debug("Customer List from Borrower :", tmbCusIDList);
        getCustomerList(relatedList, tmbCusIDList);
        log.debug("Customer List all : ");

        List<Obligation> obligationList = dwhInterface.getObligationData(getCurrentUserID(), tmbCusIDList);

        ExistingCreditView existingCreditView = new ExistingCreditView();

        for(Obligation obligation : obligationList){

        }

        List<ExistingCreditDetailView> _borrowerComExistingCredit;
        List<ExistingCreditDetailView> _borrowerRetailExistingCredit;
        List<ExistingCreditDetailView> _relatedComExistingCredit;
        List<ExistingCreditDetailView> _relatedRetailExistingCredit;

        for(Obligation obligation : obligationList){

        }
        //dwhInterface.getObligation(tmbCusIDList);

        return existingCreditView;
    }

    /**
     * @param customerInfoViewList
     * @param tmbCusIDList - to add TMB Customer ID which required for retrieving Existing Credit and Calculate Group Exposure
     * @return
     */
    private void getCustomerList(List<CustomerInfoView> customerInfoViewList, List<String> tmbCusIDList){
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(Util.isEmpty(customerInfoView.getTmbCustomerId())){
                Reference reference = customerInfoView.getReference();
                if(Util.isTrue(reference.getSll())){
                    tmbCusIDList.add(customerInfoView.getTmbCustomerId());
                }
            }
        }
    }
}
