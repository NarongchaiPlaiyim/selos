package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.AppInProcessResult;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.ActionStatusView;
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
    RLOSInterface rlosInterface;

    @Inject
    ReferenceDAO referenceDAO;

    @Inject
    ExistingCreditTransform existingCreditTransform;

    /**
     * To refresh/retrieve the Existing Credit Information from DWH Obligation Database.
     * @param customerInfoViewList
     * @return
     */
    public ExistingCreditView refreshExistingCredit(List<CustomerInfoView> customerInfoViewList){
        log.info("Start refreshExistingCredit with customerInfo{}", customerInfoViewList);

        ExistingCreditView existingCreditView = getExistingCreditObligation(customerInfoViewList);

        ExistingCreditView _tmpRLOSAppIn = getRLOSAppInProcess(customerInfoViewList);

        existingCreditView.setBorrowerAppInRLOSCredit(_tmpRLOSAppIn.getBorrowerAppInRLOSCredit());
        existingCreditView.setRelatedAppInRLOSCredit(_tmpRLOSAppIn.getRelatedAppInRLOSCredit());
        existingCreditView.setTotalBorrowerAppInRLOSLimit(_tmpRLOSAppIn.getTotalBorrowerAppInRLOSLimit());
        existingCreditView.setTotalRelatedAppInRLOSLimit(_tmpRLOSAppIn.getTotalRelatedAppInRLOSLimit());

        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();


        existingCreditView.setStatus(actionStatusViewList);

        log.info("return existing credit view {}", existingCreditView);
        return existingCreditView;
    }

    private ExistingCreditView getExistingCreditObligation(List<CustomerInfoView> customerInfoViewList){
        List<String> tmbCusIDList = new ArrayList<String>();
        List<String> _borrowerTMBCusID = new ArrayList<String>();
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

        ExistingCreditView existingCreditView = new ExistingCreditView();
        if(tmbCusIDList.size() > 0) {

            //Retrieve Obligation
            log.info("retrieve interface");
            ObligationResult obligationResult = dwhInterface.getObligationData(getCurrentUserID(), tmbCusIDList);
            if(obligationResult.getActionResult().equals(ActionResult.SUCCESS)){

                Map<String, ExistingCreditDetailView> borrowerComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
                Map<String, ExistingCreditDetailView> borrowerRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

                Map<String, ExistingCreditDetailView> relatedComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
                Map<String, ExistingCreditDetailView> relatedRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

                BigDecimal _totalBorrowerComLimit = new BigDecimal(0);
                BigDecimal _totalRelatedComLimit = new BigDecimal(0);
                List<Obligation> obligationList = obligationResult.getObligationList();
                for(Obligation obligation : obligationList){
                    ExistingCreditDetailView existingCreditDetailView = existingCreditTransform.getExistingCredit(obligation);
                    if(_borrowerTMBCusID.contains(obligation.getTmbCusId())){
                        log.info("add obligation into borrower");
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

            }
            ActionStatusView actionStatusView = new ActionStatusView();
            actionStatusView.setStatusCode(obligationResult.getActionResult());
            actionStatusView.setStatusDesc(obligationResult.getReason());


            log.info("return existing credit view {}", existingCreditView);

        }
        return existingCreditView;
    }

    private ExistingCreditView getRLOSAppInProcess(List<CustomerInfoView> customerInfoViewList){

        ExistingCreditView existingCreditView = new ExistingCreditView();

        List<String> personalIDList = new ArrayList<String>();
        List<String> _borrowerPersonalID = new ArrayList<String>();
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getCitizenId())){
                log.info("get citizen {}", customerInfoView.getCitizenId());
                Reference reference = customerInfoView.getReference();
                if(Util.isTrue(reference.getSll())){
                    personalIDList.add(customerInfoView.getCitizenId());
                    log.info("get reference {}", reference);
                }

                if(customerInfoView.getRelation().getId() == 1){
                    _borrowerPersonalID.add(customerInfoView.getCitizenId());
                }
            }
        }

        if(personalIDList.size() > 0){
            //Retrieve Obligation
            log.info("retrieve RLOS interface");
            AppInProcessResult appInProcessResult = rlosInterface.getAppInProcessData(getCurrentUserID(), personalIDList);
            log.info("Result from RLOSInterface, {} from personalID {}", appInProcessResult, personalIDList);
            if(appInProcessResult.getActionResult().equals(ActionResult.SUCCESS)){

                log.info("Start Transform Result");
                List<ExistingCreditDetailView> borrowerRLOSApp = new ArrayList<ExistingCreditDetailView>();
                BigDecimal totalBorrowerRLOSApp = BigDecimal.ZERO;

                List<ExistingCreditDetailView> relatedRLOSApp = new ArrayList<ExistingCreditDetailView>();
                BigDecimal totalRelatedRLOSApp = new BigDecimal(0);

                List<AppInProcess> appInProcessList = appInProcessResult.getAppInProcessList();
                for(AppInProcess appInProcess : appInProcessList){
                    List<ExistingCreditDetailView> existingCreditDetailViews = existingCreditTransform.getExistingCredit(appInProcess);
                    List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                    boolean isBorrower = false;
                    for(CustomerDetail customerDetail : customerDetailList) {
                        if(_borrowerPersonalID.contains(customerDetail.getCitizenId())){
                            isBorrower = true;
                        }
                    }

                    for(ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViews){
                        if(isBorrower){
                            borrowerRLOSApp.add(existingCreditDetailView);
                            totalBorrowerRLOSApp.add(existingCreditDetailView.getLimit());
                        } else {
                            relatedRLOSApp.add(existingCreditDetailView);
                            totalRelatedRLOSApp.add(existingCreditDetailView.getLimit());
                        }
                    }

                }
                existingCreditView.setBorrowerAppInRLOSCredit(borrowerRLOSApp);
                existingCreditView.setTotalBorrowerAppInRLOSLimit(totalBorrowerRLOSApp);

                existingCreditView.setRelatedAppInRLOSCredit(relatedRLOSApp);
                existingCreditView.setTotalRelatedAppInRLOSLimit(totalRelatedRLOSApp);
            }

            //TODO: Update Retrieving Status.
        }
        return existingCreditView;
    }

    /**
     * To get the Existing Credit Facility from SE-LOS Database.
     * @param customerInfoViewList
     * @return
     */
    public ExistingCreditView getExistingCredit(List<CustomerInfoView> customerInfoViewList){
        return null;
    }
}