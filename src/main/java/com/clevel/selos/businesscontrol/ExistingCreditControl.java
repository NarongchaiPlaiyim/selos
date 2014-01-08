package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.ExistingCreditFacilityDAO;
import com.clevel.selos.integration.DWHInterface;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.AppInProcessResult;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.CreditRelationType;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.ActionStatusView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditFacilityView;
import com.clevel.selos.transform.business.ObligationBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ExistingCreditControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    DWHInterface dwhInterface;
    @Inject
    RLOSInterface rlosInterface;

    @Inject
    ReferenceDAO referenceDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    ObligationBizTransform existingCreditTransform;

    @Inject
    public ExistingCreditControl(){

    }
                                                        /**
     * To refresh/retrieve the Existing Credit Information from DWH Obligation Database.
     *
     * @param customerInfoViewList
     * @return
     */
    public ExistingCreditFacilityView refreshExistingCredit(List<CustomerInfoView> customerInfoViewList) {
        log.info("Start refreshExistingCredit with customerInfo{}", customerInfoViewList);

        ExistingCreditFacilityView existingCreditFacilityView = getExistingCreditObligation(customerInfoViewList);
        ExistingCreditFacilityView _tmpRLOSAppIn = getRLOSAppInProcess(customerInfoViewList);

        existingCreditFacilityView.setBorrowerAppInRLOSCredit(_tmpRLOSAppIn.getBorrowerAppInRLOSCredit());
        existingCreditFacilityView.setRelatedAppInRLOSCredit(_tmpRLOSAppIn.getRelatedAppInRLOSCredit());
        existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit(_tmpRLOSAppIn.getTotalBorrowerAppInRLOSLimit());
        existingCreditFacilityView.setTotalRelatedAppInRLOSLimit(_tmpRLOSAppIn.getTotalRelatedAppInRLOSLimit());

        List<ActionStatusView> actionStatusViewList = new ArrayList<ActionStatusView>();


        existingCreditFacilityView.setStatus(actionStatusViewList);

        log.info("return existing credit view {}", existingCreditFacilityView);
        return existingCreditFacilityView;
    }

    private ExistingCreditFacilityView getExistingCreditObligation(List<CustomerInfoView> customerInfoViewList) {
        List<String> tmbCusIDList = new ArrayList<String>();
        List<String> _borrowerTMBCusID = new ArrayList<String>();
        for (CustomerInfoView customerInfoView : customerInfoViewList) {
            if (!Util.isEmpty(customerInfoView.getTmbCustomerId())) {
                log.info("get tmbCusId {}", customerInfoView.getTmbCustomerId());
                Reference reference = customerInfoView.getReference();
                if (Util.isTrue(reference.getSll())) {
                    tmbCusIDList.add(customerInfoView.getTmbCustomerId());
                    log.info("get reference {}", reference);
                }

                if (customerInfoView.getRelation().getId() == 1) {
                    _borrowerTMBCusID.add(customerInfoView.getTmbCustomerId());
                }

                //get for spouse
                if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getId() != 0){
                    CustomerInfoView spouseInfoView = customerInfoView.getSpouse();
                    log.info("get spouse tmbCusId {}", spouseInfoView.getTmbCustomerId());
                    Reference referenceSpouse = spouseInfoView.getReference();
                    if (Util.isTrue(referenceSpouse.getSll())) {
                        tmbCusIDList.add(spouseInfoView.getTmbCustomerId());
                        log.info("get spouse reference {}", referenceSpouse);
                    }

                    if (spouseInfoView.getRelation().getId() == 1) {
                        _borrowerTMBCusID.add(spouseInfoView.getTmbCustomerId());
                    }
                }
            }
        }

        ExistingCreditFacilityView existingCreditFacilityView = new ExistingCreditFacilityView();
        if (tmbCusIDList.size() > 0) {

            //Retrieve Obligation
            log.info("retrieve interface");
            ObligationResult obligationResult = dwhInterface.getObligationData(getCurrentUserID(), tmbCusIDList);
            if (obligationResult.getActionResult().equals(ActionResult.SUCCESS)) {

                Map<String, ExistingCreditDetailView> borrowerComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
                Map<String, ExistingCreditDetailView> borrowerRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

                Map<String, ExistingCreditDetailView> relatedComCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();
                Map<String, ExistingCreditDetailView> relatedRetailCreditDetailHashMap = new HashMap<String, ExistingCreditDetailView>();

                BigDecimal _totalBorrowerComLimit = new BigDecimal(0);
                BigDecimal _totalRelatedComLimit = new BigDecimal(0);
                List<Obligation> obligationList = obligationResult.getObligationList();
                for (Obligation obligation : obligationList) {
                    ExistingCreditDetailView existingCreditDetailView = existingCreditTransform.getExistingCredit(obligation);
                    if (_borrowerTMBCusID.contains(obligation.getTmbCusId())) {
                        log.info("add obligation into borrower");
                        String borrowerKey = existingCreditDetailView.getAccountNumber().concat(existingCreditDetailView.getAccountSuf()).concat(existingCreditDetailView.getAccountRef());
                        existingCreditDetailView.setCreditRelationType(CreditRelationType.BORROWER);
                        if(!borrowerComCreditDetailHashMap.containsKey(borrowerKey)){
                            borrowerComCreditDetailHashMap.put(borrowerKey, existingCreditDetailView);
                            _totalBorrowerComLimit = _totalBorrowerComLimit.add(existingCreditDetailView.getLimit());
                        }
                    } else {
                        log.info("add obligation into relate");
                        String relateKey = existingCreditDetailView.getAccountNumber().concat(existingCreditDetailView.getAccountSuf()).concat(existingCreditDetailView.getAccountRef());
                        existingCreditDetailView.setCreditRelationType(CreditRelationType.RELATED);
                        if(!relatedComCreditDetailHashMap.containsKey(relateKey)){
                            relatedComCreditDetailHashMap.put(relateKey, existingCreditDetailView);
                            _totalRelatedComLimit = _totalRelatedComLimit.add(existingCreditDetailView.getLimit());
                        }
                    }
                }

                existingCreditFacilityView.setBorrowerComExistingCredit(new ArrayList<ExistingCreditDetailView>(borrowerComCreditDetailHashMap.values()));
                existingCreditFacilityView.setTotalBorrowerComLimit(_totalBorrowerComLimit);
                existingCreditFacilityView.setRelatedComExistingCredit(new ArrayList<ExistingCreditDetailView>(relatedComCreditDetailHashMap.values()));
                existingCreditFacilityView.setTotalRelatedComLimit(_totalRelatedComLimit);

            }
            ActionStatusView actionStatusView = new ActionStatusView();
            actionStatusView.setStatusCode(obligationResult.getActionResult());
            actionStatusView.setStatusDesc(obligationResult.getReason());


            log.info("return existing credit view {}", existingCreditFacilityView);

        }
        return existingCreditFacilityView;
    }

    private ExistingCreditFacilityView getRLOSAppInProcess(List<CustomerInfoView> customerInfoViewList) {

        ExistingCreditFacilityView existingCreditFacilityView = new ExistingCreditFacilityView();

        List<String> personalIDList = new ArrayList<String>();
        List<String> _borrowerPersonalID = new ArrayList<String>();
        for (CustomerInfoView customerInfoView : customerInfoViewList) {

            if (!Util.isEmpty(customerInfoView.getCitizenId())) {
                log.info("get citizen {}", customerInfoView.getCitizenId());
                Reference reference = customerInfoView.getReference();
                if (Util.isTrue(reference.getSll())) {
                    personalIDList.add(customerInfoView.getCitizenId());
                    log.info("get reference for RLOS {}", reference);
                }

                if (customerInfoView.getRelation().getId() == 1) {
                    _borrowerPersonalID.add(customerInfoView.getCitizenId());
                }
            }
        }
        log.info("personal id in RLOS size {}", personalIDList);

        if (personalIDList.size() > 0) {
            //Retrieve Obligation
            log.info("retrieve RLOS interface");
            AppInProcessResult appInProcessResult = rlosInterface.getAppInProcessData(getCurrentUserID(), personalIDList);
            log.info("Result from RLOSInterface, {} from personalID {}", appInProcessResult, personalIDList);
            if (appInProcessResult.getActionResult().equals(ActionResult.SUCCESS)) {

                log.info("Start Transform Result");
                List<ExistingCreditDetailView> borrowerRLOSApp = new ArrayList<ExistingCreditDetailView>();
                BigDecimal totalBorrowerRLOSApp = BigDecimal.ZERO;

                List<ExistingCreditDetailView> relatedRLOSApp = new ArrayList<ExistingCreditDetailView>();
                BigDecimal totalRelatedRLOSApp = BigDecimal.ZERO;

                List<AppInProcess> appInProcessList = appInProcessResult.getAppInProcessList();
                log.info("App In {}", appInProcessList);
                for (AppInProcess appInProcess : appInProcessList) {
                    List<ExistingCreditDetailView> existingCreditDetailViews = existingCreditTransform.getExistingCredit(appInProcess);
                    List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
                    boolean isBorrower = false;
                    for (CustomerDetail customerDetail : customerDetailList) {
                        if (_borrowerPersonalID.contains(customerDetail.getCitizenId())) {
                            isBorrower = true;

                        }
                    }

                    for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViews) {
                        if (isBorrower) {
                            existingCreditDetailView.setCreditRelationType(CreditRelationType.BORROWER);

                            borrowerRLOSApp.add(existingCreditDetailView);

                            log.info("Existing Credit : {} ", existingCreditDetailView.getLimit());
                            totalBorrowerRLOSApp = totalBorrowerRLOSApp.add(existingCreditDetailView.getLimit());

                            log.info("total borrower RLOS Limit {}", totalBorrowerRLOSApp);
                        } else {
                            relatedRLOSApp.add(existingCreditDetailView);
                            existingCreditDetailView.setCreditRelationType(CreditRelationType.RELATED);
                            log.info("Existing Credit : {} ", existingCreditDetailView.getLimit());
                            totalRelatedRLOSApp = totalRelatedRLOSApp.add(existingCreditDetailView.getLimit());
                            log.info("total related RLOS Limit {}", totalRelatedRLOSApp);
                        }
                    }

                }
                existingCreditFacilityView.setBorrowerAppInRLOSCredit(borrowerRLOSApp);
                existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit(totalBorrowerRLOSApp);

                existingCreditFacilityView.setRelatedAppInRLOSCredit(relatedRLOSApp);
                existingCreditFacilityView.setTotalRelatedAppInRLOSLimit(totalRelatedRLOSApp);
            }

            //TODO: Update Retrieving Status.
        }
        return existingCreditFacilityView;
    }

    /**
     * To get the Existing Credit Facility from SE-LOS Database.
     *
     * @param workCasePrescreenId
     * @return
     */
    public ExistingCreditFacilityView getExistingCredit(long workCasePrescreenId) {
        ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCasePreScreenId(workCasePrescreenId);
        ExistingCreditFacilityView existingCreditFacilityView = existingCreditTransform.getExistingCreditView(existingCreditFacility);
        return existingCreditFacilityView;
    }

    public void saveExistingCredit(ExistingCreditFacilityView existingCreditFacilityView, WorkCasePrescreen workCasePrescreen) {
        ExistingCreditFacility existingCreditFacility = null;
        if (workCasePrescreen != null && workCasePrescreen.getId() != 0) {
            existingCreditFacility = existingCreditFacilityDAO.findByWorkCasePreScreenId(workCasePrescreen.getId());
            if (existingCreditFacility != null) {
                deleteExistingCreditDetail(existingCreditFacility);
            }
        }

        existingCreditFacility = existingCreditTransform.getExistingCreditFacility(existingCreditFacilityView, existingCreditFacility, getCurrentUser());
        existingCreditFacility.setWorkCasePrescreen(workCasePrescreen);
        existingCreditFacilityDAO.persist(existingCreditFacility);
        //existingCreditDetailDAO.persist(existingCreditFacility.getExistingCreditDetailList());
    }

    public void deleteExistingCreditDetail(ExistingCreditFacility existingCreditFacility) {
        log.info("start delete {}", existingCreditFacility);
        List<ExistingCreditDetail> existingCreditDetailList = existingCreditFacility.getExistingCreditDetailList();

        log.info("list of existing detail {}", existingCreditDetailList);
        existingCreditDetailDAO.delete(existingCreditDetailList);
        existingCreditFacility.setExistingCreditDetailList(null);
        existingCreditFacilityDAO.persist(existingCreditFacility);
        log.info("end delete {}", existingCreditFacility);
    }

    public void saveExistingCredit(ExistingCreditFacilityView existingCreditFacilityView, WorkCase workCase) {

    }
}