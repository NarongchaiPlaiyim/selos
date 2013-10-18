package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditView;
import com.clevel.selos.transform.business.ExistingCreditTransform;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExistingCreditControl extends BusinessControl{

    @Inject
    DWHInterface dwhInterface;

    @Inject
    ReferenceDAO referenceDAO;

    @Inject
    ExistingCreditTransform existingCreditTransform;

    public ExistingCreditView getExistingCredit(List<CustomerInfoView> customerInfoViewList){
        log.info("Start GetExistingCredit with borrowers{}, related{}");

        List<String> tmbCusIDList = new ArrayList<String>();
        List<String> _borrowerTMBCusID = new ArrayList<String>();
        List<String> _relatedTMBCusID = new ArrayList<String>();
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getTmbCustomerId())){
                log.info("get tmbCusId {}", customerInfoView.getTmbCustomerId());
                Reference reference = customerInfoView.getReference();
                if(Util.isTrue(reference.getSll())){
                    tmbCusIDList.add(customerInfoView.getTmbCustomerId());
                    log.info("get reference {}", reference);
                }

                if(customerInfoView.getRelation().getId() == 1){
                    _borrowerTMBCusID.add(customerInfoView.getTmbCustomerId());
                }
            }
        }

        List<Obligation> obligationList = new ArrayList<Obligation>();
        if(tmbCusIDList.size() > 0)
            dwhInterface.getObligationData(getCurrentUserID(), tmbCusIDList);

        Map<String, ExistingCreditDetailView> borrowerComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
        Map<String, ExistingCreditDetailView> borrowerRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

        Map<String, ExistingCreditDetailView> relatedComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
        Map<String, ExistingCreditDetailView> relatedRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

        BigDecimal _totalBorrowerComLimit = new BigDecimal(0);
        BigDecimal _totalRelatedComLimit = new BigDecimal(0);
        ExistingCreditView existingCreditView = new ExistingCreditView();

        for(Obligation obligation : obligationList){
            ExistingCreditDetailView existingCreditDetailView = existingCreditTransform.getExistingCredit(obligation);
            if(_borrowerTMBCusID.contains(obligation.getTmbCusId())){
                borrowerComCreditDetailHashMap.put(existingCreditDetailView.getAccountNumber(), existingCreditDetailView);
                _totalBorrowerComLimit.add(existingCreditDetailView.getLimit());
            } else {
                relatedComCreditDetailHashMap.put(existingCreditDetailView.getAccountNumber(), existingCreditDetailView);
                _totalRelatedComLimit.add(existingCreditDetailView.getLimit());
            }
        }

        existingCreditView.setBorrowerComExistingCredit(new ArrayList<ExistingCreditDetailView>(borrowerComCreditDetailHashMap.values()));
        existingCreditView.setTotalBorrowerComLimit(_totalBorrowerComLimit);
        existingCreditView.setRelatedComExistingCredit(new ArrayList<ExistingCreditDetailView>(relatedComCreditDetailHashMap.values()));
        existingCreditView.setTotalRelatedComLimit(_totalRelatedComLimit);


        return existingCreditView;
    }

    /**
     * @param customerInfoViewList
     * @return
     */

    private List<String> getCustomerList(List<CustomerInfoView> customerInfoViewList){
        log.info("Start getCustomerList");
        List<String> tmbCusIDList = new ArrayList<String>();

        return tmbCusIDList;
    }
}
