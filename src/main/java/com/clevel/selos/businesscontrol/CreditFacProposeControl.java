package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CreditFacProposeControl extends BusinessControl {
    @SELOS
    @Inject
    Logger log;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    NewCreditFacilityTransform newCreditFacilityTransform;
    @Inject
    NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    CreditTypeDetailTransform creditTypeDetailTransform;
    @Inject
    NewCollateralInfoTransform newCollateralInfoTransform;
    @Inject
    NewCollHeadDetailTransform newCollHeadDetailTransform;
    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;
    @Inject
    NewGuarantorDetailTransform newGuarantorDetailTransform;
    @Inject
    NewConditionDetailTransform newConditionDetailTransform;
    @Inject
    NewCreditTierTransform newCreditTierTransform;
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
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    NewFeeCreditDAO newFeeCreditDAO;
    @Inject
    NewConditionDetailDAO newConditionDetailDAO;
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    NewCreditTierDetailDAO newCreditTierDetailDAO;
    @Inject
    NewGuarantorDetailDAO newGuarantorDetailDAO;
    @Inject
    CreditTypeDetailDAO creditTypeDetailDAO;
    @Inject
    NewCollateralDetailDAO newCollateralDetailDAO;
    @Inject
    NewCollateralSubDetailDAO newCollateralSubDetailDAO;
    @Inject
    NewCollateralHeadDetailDAO newCollateralHeadDetailDAO;


    public CreditFacProposeControl() {
    }

    public List<Customer> getListOfGuarantor(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return customerDAO.findGuarantorByWorkCaseId(workCaseId);
    }

    public BasicInfo getBasicByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return basicInfoDAO.findByWorkCaseId(workCaseId);

    }

    public NewCreditFacility getNewCreditFacilityViewByWorkCaseId(long workCaseId) {
        log.info("workCaseId :: {}", workCaseId);
        return newCreditFacilityDAO.findByWorkCaseId(workCaseId);

    }

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

    }


}