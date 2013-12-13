package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.ExistingCollateralDetailView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditFacilityView;
import com.clevel.selos.model.view.ExistingGuarantorDetailView;
import com.clevel.selos.transform.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CreditFacExistingControl extends BusinessControl {
    @SELOS
    @Inject
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    ExistingCreditFacilityTransform existingCreditFacilityTransform;
    /*@Inject
    ExistingFeeDetailTransform existingFeeDetailTransform;

    */
    @Inject
    ExistingCreditDetailTransform existingCreditDetailTransform;
    @Inject
    ExistingCollateralDetailTransform existingCollateralDetailTransform;
    @Inject
    ExistingGuarantorDetailTransform existingGuarantorDetailTransform;
    @Inject
    ExistingCreditTypeDetailTransform existingCreditTypeDetailTransform;
    @Inject
    ExistingConditionDetailTransform existingConditionDetailTransform;
    @Inject
    ExistingCreditTierTransform existingCreditTierTransform;
    @Inject
    ExistingSplitLineDetailTransform existingSplitLineTransform;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    ExistingConditionDetailDAO existingConditionDetailDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    ExistingCreditTierDetailDAO existingCreditTierDetailDAO;
    @Inject
    ExistingCreditTypeDetailDAO existingCreditTypeDetailDAO;
    @Inject
    ExistingSplitLineDetailDAO existingSplitLineDetailDAO;
    @Inject
    ExistingCollateralDetailDAO existingCollateralDetailDAO;
    @Inject
    ExistingGuarantorDetailDAO existingGuarantorDetailDAO;

    public CreditFacExistingControl() {
    }

    public List<Customer> getListOfGuarantor(long workCaseId) {
        log.info("workCaseId getListOfGuarantor:: {}", workCaseId);
        return customerDAO.findGuarantorByWorkCaseId(workCaseId);
    }

    public List<Customer> getListOfCollateralOwnerUW(long workCaseId) {
        log.info("workCaseId findCollateralOwnerUWByWorkCaseId :: {}", workCaseId);
        return customerDAO.findCollateralOwnerUWByWorkCaseId(workCaseId);
    }

    public BasicInfo getBasicByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return basicInfoDAO.findByWorkCaseId(workCaseId);

    }

    public ExistingCreditFacility getExistingCreditSummaryViewByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return existingCreditFacilityDAO.findByWorkCaseId(workCaseId);

    }

    public ExistingCreditFacilityView getExistingCreditFacility(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        ExistingCreditFacilityView existingCreditFacilityView =  existingCreditFacilityTransform.transformToView(existingCreditFacilityDAO.findByWorkCaseId(workCaseId));
        return existingCreditFacilityView;
    }


    /*
    public NewCreditFacilityView findNewCreditFacilityByWorkCase(long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);

        NewCreditFacilityView newCreditFacilityView = newCreditFacilityTransform.transformToView(newCreditFacility);

        if (newCreditFacility.getNewFeeDetailList() != null) {
            List<NewFeeDetailView> newFeeDetailViewList = newFeeDetailTransform.transformToView(newCreditFacility.getNewFeeDetailList());
            newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
        }

        if (newCreditFacility.getNewCreditDetailList() != null) {
            List<NewCreditDetailView> newCreditDetailViewList = newCreditDetailTransform.transformToView(newCreditFacility.getNewCreditDetailList());

            for (NewCreditDetailView newCreditDetailView : newCreditDetailViewList) {

                for (NewCreditDetail newCreditDetail : newCreditFacility.getNewCreditDetailList()) {
                    if (newCreditDetail.getProposeCreditTierDetailList() != null) {
                        List<NewCreditTierDetailView> newCreditTierDetailViewList = newCreditTierTransform.transformToView(newCreditDetail.getProposeCreditTierDetailList());
                        newCreditDetailView.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
                    }
                }

            }

            newCreditFacilityView.setNewCreditDetailViewList(newCreditDetailViewList);
        }

        if (newCreditFacility.getNewCollateralDetailList() != null) {
            List<NewCollateralInfoView> newCollateralInfoViewList = newCollateralInfoTransform.transformsToView(newCreditFacility.getNewCollateralDetailList());
            for (NewCollateralInfoView newCollateralInfoView : newCollateralInfoViewList) {
                for (NewCollateralDetail newCollateralDetail : newCreditFacility.getNewCollateralDetailList()) {
                    if (newCollateralDetail.getNewCollateralHeadDetailList() != null) {
                        List<NewCollateralHeadDetailView> newCollateralHeadDetailViews = newCollHeadDetailTransform.transformToView(newCollateralDetail.getNewCollateralHeadDetailList());

                        for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViews) {
                            for (NewCollateralHeadDetail newCollateralHeadDetail : newCollateralDetail.getNewCollateralHeadDetailList()) {
                                if (newCollateralHeadDetail.getNewCollateralSubDetailList() != null) {
                                    List<NewSubCollateralDetailView> newSubCollateralDetailViews = newSubCollDetailTransform.transformToView(newCollateralHeadDetail.getNewCollateralSubDetailList());
                                    newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViews);
                                }
                            }
                        }

                        newCollateralInfoView.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViews);
                    }

                    if (newCollateralDetail.getCreditTypeDetailList() != null) {
                        List<CreditTypeDetailView> creditTypeDetailViews = creditTypeDetailTransform.transformToView(newCollateralDetail.getCreditTypeDetailList());
                        newCollateralInfoView.setCreditTypeDetailViewList(creditTypeDetailViews);
                    }
                }
            }

            newCreditFacilityView.setNewCollateralInfoViewList(newCollateralInfoViewList);
        }

       if (newCreditFacility.getNewGuarantorDetailList() != null) {
            List<NewGuarantorDetailView> newGuarantorDetailViewList = newGuarantorDetailTransform.transformToView(newCreditFacility.getNewGuarantorDetailList());

            for (NewGuarantorDetailView newGuarantorDetailView : newGuarantorDetailViewList) {
                for (NewGuarantorDetail newGuarantorDetail : newCreditFacility.getNewGuarantorDetailList()) {
                    if (newGuarantorDetail.getCreditTypeDetailList() != null) {
                        List<CreditTypeDetailView> creditTypeDetailViewList = creditTypeDetailTransform.transformToView(newGuarantorDetail.getCreditTypeDetailList());
                        newGuarantorDetailView.setCreditTypeDetailViewList(creditTypeDetailViewList);

                    }
                }

            }

            newCreditFacilityView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);
        }


        if (newCreditFacility.getNewConditionDetailList() != null) {
            List<NewConditionDetailView> newConditionDetailViewList = newConditionDetailTransform.transformToView(newCreditFacility.getNewConditionDetailList());
            newCreditFacilityView.setNewConditionDetailViewList(newConditionDetailViewList);
        }


        return newCreditFacilityView;
    }

    public void onSaveNewCreditFacility(NewCreditFacilityView newCreditFacilityView, Long workCaseId, User user) {
        log.info("onSaveNewCreditFacility begin");
        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        NewCreditFacility newCreditFacility = newCreditFacilityTransform.transformToModelDB(newCreditFacilityView, workCase, user);
        newCreditFacilityDAO.persist(newCreditFacility);
        log.info("persist :: creditFacilityPropose...");

        List<NewFeeDetail> newFeeDetailList = newFeeDetailTransform.transformToModel(newCreditFacilityView.getNewFeeDetailViewList(), newCreditFacility, user);
        newFeeCreditDAO.persist(newFeeDetailList);
        log.info("persist :: newFeeDetailList...");

        List<NewConditionDetail> newConditionDetailList = newConditionDetailTransform.transformToModel(newCreditFacilityView.getNewConditionDetailViewList(), newCreditFacility, user);
        newConditionDetailDAO.persist(newConditionDetailList);
        log.info("persist :: newConditionDetail ...");

        List<NewCreditDetail> newCreditDetailList = newCreditDetailTransform.transformToModel(newCreditFacilityView.getNewCreditDetailViewList(), newCreditFacility, user);
        newCreditDetailDAO.persist(newCreditDetailList);
        log.info("persist newCreditDetailList...");

        for (NewCreditDetail newCreditDetail : newCreditDetailList) {
            for (NewCreditDetailView newCreditDetailView : newCreditFacilityView.getNewCreditDetailViewList()) {
                List<NewCreditTierDetail> newCreditTierDetailList = newCreditTierTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList(), newCreditDetail, user);
                newCreditTierDetailDAO.persist(newCreditTierDetailList);
                log.info("persist newCreditTierDetailList...");
            }
        }

        List<NewGuarantorDetail> newGuarantorDetailList = newGuarantorDetailTransform.transformToModel(newCreditFacilityView.getNewGuarantorDetailViewList(), newCreditFacility, user);
        newGuarantorDetailDAO.persist(newGuarantorDetailList);
        log.info("persist newGuarantorDetailList...");

        for (NewGuarantorDetail newGuarantorDetail : newGuarantorDetailList) {
            for (NewGuarantorDetailView newGuarantorDetailView : newCreditFacilityView.getNewGuarantorDetailViewList()) {
                List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModelForGuarantor(newGuarantorDetailView.getCreditTypeDetailViewList(), newGuarantorDetail, user);
                creditTypeDetailDAO.persist(creditTypeDetailList);
                log.info("persist creditTypeDetailList...");
            }

        }

        List<NewCollateralDetail> newCollateralDetailList = newCollateralInfoTransform.transformsToModel(newCreditFacilityView.getNewCollateralInfoViewList(), newCreditFacility, user);
        newCollateralDetailDAO.persist(newCollateralDetailList);
        log.info("persist newCollateralDetailList...");

        for (NewCollateralDetail newCollateralDetail : newCollateralDetailList) {
            for (NewCollateralInfoView newCollateralInfoView : newCreditFacilityView.getNewCollateralInfoViewList()) {
                List<NewCollateralHeadDetail> newCollateralHeadDetailList = newCollHeadDetailTransform.transformToModel(newCollateralInfoView.getNewCollateralHeadDetailViewList(), newCollateralDetail, user);
                newCollateralHeadDetailDAO.persist(newCollateralHeadDetailList);
                log.info("persist newCollateralHeadDetailList...");

                for (NewCollateralHeadDetail newCollateralHeadDetail : newCollateralHeadDetailList) {
                    for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                        List<NewCollateralSubDetail> newCollateralSubDetails = newSubCollDetailTransform.transformToModel(newCollateralHeadDetailView.getNewSubCollateralDetailViewList(), newCollateralHeadDetail, user);
                        newCollateralSubDetailDAO.persist(newCollateralSubDetails);
                        log.info("persist newCollateralSubDetailList...");
                    }
                }

                List<CreditTypeDetail> creditTypeDetailList = creditTypeDetailTransform.transformToModelForCollateral(newCollateralInfoView.getCreditTypeDetailViewList(), newCollateralDetail, user);
                creditTypeDetailDAO.persist(creditTypeDetailList);
                log.info("persist creditTypeDetailList...");

            }
        }

    }*/
    public void onSaveExistingCreditFacility(ExistingCreditFacilityView existingCreditFacilityView, Long workCaseId, User user) {
        log.info("onSaveExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase Id  "+ workCase.getId() + " getCaNumber " + workCase.getCaNumber());

        ExistingCreditFacility existingCreditFacility = existingCreditFacilityTransform.transformToModelDB(existingCreditFacilityView, workCase, user);
        existingCreditFacilityDAO.persist(existingCreditFacility);
        log.info("persist :: existingCreditFacility..." + existingCreditFacility.getId());

        log.info("getExistingConditionDetailViewList size = ... " + existingCreditFacilityView.getExistingConditionDetailViewList().size());
        List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailTransform.transformToModel(existingCreditFacilityView.getExistingConditionDetailViewList(), existingCreditFacility, user);
        existingConditionDetailDAO.persist(existingConditionDetailList);
        log.info("persist :: existingConditionDetailList ...");

        log.info("persist borrower getBorrowerComExistingCredit size ..." + existingCreditFacilityView.getBorrowerComExistingCredit());
        List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getBorrowerComExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerComExistingCreditList);
        log.info("persist borrower existingCreditDetailList...");

        for (ExistingCreditDetail existingCreditDetail : borrowerComExistingCreditList) {
            log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                log.info("persist borrower getExistingCreditTierDetailViewList... " + existingCreditDetailView.getExistingCreditTierDetailViewList().size());
                List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                log.info("persist borrower existingCreditTierDetailList...");

                log.info("persist borrower getExistingSplitLineDetailViewList... " + existingCreditDetailView.getExistingSplitLineDetailViewList().size());
                List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                log.info("persist borrower existingCreditTierDetailList...");
            }
        }

        List<ExistingCreditDetail> borrowerRetailExistingCredit = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getBorrowerRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
        log.info("persist borrower Retail existingCreditDetailList...");

        List<ExistingCreditDetail> borrowerAppInRLOSCredit = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getBorrowerAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
        log.info("persist borrower RLOS existingCreditDetailList...");
        

        List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getRelatedComExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedComExistingCreditList);
        log.info("persist related existingCreditDetailList...");

        for (ExistingCreditDetail existingCreditDetail : relatedComExistingCreditList) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getRelatedComExistingCredit()) {
                List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                log.info("persist related existingCreditTierDetailList...");
                List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                log.info("persist related existingSplitLineDetailList...");
            }
        }

        List<ExistingCreditDetail> relatedRetailExistingCredit = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getRelatedRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedRetailExistingCredit);
        log.info("persist related Retail existingCreditDetailList...");

        List<ExistingCreditDetail> relatedAppInRLOSCredit = existingCreditDetailTransform.transformToModel(existingCreditFacilityView.getRelatedAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedRetailExistingCredit);
        log.info("persist related RLOS existingCreditDetailList..."); 

        List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(borrowerCollateralDetailList);
        log.info("persist borrowerCollateralDetailList...");

        for (ExistingCollateralDetail existingCollateralDetail : borrowerCollateralDetailList) {
            for (ExistingCollateralDetailView existingCollateralDetailView : existingCreditFacilityView.getBorrowerCollateralList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist borrower Collateral  existingCreditTypeDetailList...");
            }
        }

        List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(relatedCollateralDetailList);
        log.info("persist relatedCollateralDetailList...");

        for (ExistingCollateralDetail existingCollateralDetail : relatedCollateralDetailList) {
            for (ExistingCollateralDetailView existingCollateralDetailView : existingCreditFacilityView.getBorrowerCollateralList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist related Collateral existingCreditTypeDetailList...");
            }
        }

        List<ExistingGuarantorDetail> borrowerGuarantorDetailList = existingGuarantorDetailTransform.transformToModel(existingCreditFacilityView.getBorrowerGuarantorList(), existingCreditFacility, user);
        existingGuarantorDetailDAO.persist(borrowerGuarantorDetailList);
        log.info("persist borrowerGuarantorDetailList...");

        for (ExistingGuarantorDetail existingGuarantorDetail : borrowerGuarantorDetailList) {
            for (ExistingGuarantorDetailView existingGuarantorDetailView : existingCreditFacilityView.getBorrowerGuarantorList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformToModelForGuarantor(existingGuarantorDetailView.getExistingCreditTypeDetailViewList(), existingGuarantorDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist existingCreditTypeDetailList...");
            }
        }
    }
}