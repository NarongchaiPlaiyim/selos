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
import java.util.ArrayList;
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
        ExistingCreditFacilityView existingCreditFacilityView =  existingCreditFacilityTransform.transformsToView(existingCreditFacilityDAO.findByWorkCaseId(workCaseId));
        return existingCreditFacilityView;
    }

    public void onSaveExistingCreditFacility(ExistingCreditFacilityView existingCreditFacilityView, Long workCaseId, User user) {
        log.info("onSaveExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase Id  "+ workCase.getId() + " getCaNumber " + workCase.getCaNumber());

        ExistingCreditFacility existingCreditFacility = existingCreditFacilityTransform.transformsToModelDB(existingCreditFacilityView, workCase, user);
        existingCreditFacilityDAO.persist(existingCreditFacility);
        log.info("persist :: existingCreditFacility..." + existingCreditFacility.getId());

        log.info("getExistingConditionDetailViewList size = ... " + existingCreditFacilityView.getExistingConditionDetailViewList().size());
        List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailTransform.transformsToModel(existingCreditFacilityView.getExistingConditionDetailViewList(), existingCreditFacility, user);
        existingConditionDetailDAO.persist(existingConditionDetailList);
        log.info("persist :: existingConditionDetailList ...");

        log.info("persist borrower getBorrowerComExistingCredit size ..." + existingCreditFacilityView.getBorrowerComExistingCredit());
        List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerComExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerComExistingCreditList);
        log.info("persist borrower existingCreditDetailList...");

        for (ExistingCreditDetail existingCreditDetail : borrowerComExistingCreditList) {
            log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getBorrowerComExistingCredit()) {
                log.info("persist borrower getExistingCreditTierDetailViewList... " + existingCreditDetailView.getExistingCreditTierDetailViewList().size());
                List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformsToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                log.info("persist borrower existingCreditTierDetailList...");

                log.info("persist borrower getExistingSplitLineDetailViewList... " + existingCreditDetailView.getExistingSplitLineDetailViewList().size());
                List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformsToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                log.info("persist borrower existingCreditTierDetailList...");
            }
        }

        List<ExistingCreditDetail> borrowerRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
        log.info("persist borrower Retail existingCreditDetailList...");

        List<ExistingCreditDetail> borrowerAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
        log.info("persist borrower RLOS existingCreditDetailList...");
        

        List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedComExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedComExistingCreditList);
        log.info("persist related existingCreditDetailList...");

        for (ExistingCreditDetail existingCreditDetail : relatedComExistingCreditList) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditFacilityView.getRelatedComExistingCredit()) {
                List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformsToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                log.info("persist related existingCreditTierDetailList...");
                List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformsToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                log.info("persist related existingSplitLineDetailList...");
            }
        }

        List<ExistingCreditDetail> relatedRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedRetailExistingCredit);
        log.info("persist related Retail existingCreditDetailList...");

        List<ExistingCreditDetail> relatedAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedRetailExistingCredit);
        log.info("persist related RLOS existingCreditDetailList..."); 

        List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(borrowerCollateralDetailList);
        log.info("persist borrowerCollateralDetailList...");

        for (ExistingCollateralDetail existingCollateralDetail : borrowerCollateralDetailList) {
            for (ExistingCollateralDetailView existingCollateralDetailView : existingCreditFacilityView.getBorrowerCollateralList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist borrower Collateral  existingCreditTypeDetailList...");
            }
        }

        List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(relatedCollateralDetailList);
        log.info("persist relatedCollateralDetailList...");

        for (ExistingCollateralDetail existingCollateralDetail : relatedCollateralDetailList) {
            for (ExistingCollateralDetailView existingCollateralDetailView : existingCreditFacilityView.getBorrowerCollateralList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist related Collateral existingCreditTypeDetailList...");
            }
        }

        List<ExistingGuarantorDetail> borrowerGuarantorDetailList = existingGuarantorDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerGuarantorList(), existingCreditFacility, user);
        existingGuarantorDetailDAO.persist(borrowerGuarantorDetailList);
        log.info("persist borrowerGuarantorDetailList...");

        for (ExistingGuarantorDetail existingGuarantorDetail : borrowerGuarantorDetailList) {
            for (ExistingGuarantorDetailView existingGuarantorDetailView : existingCreditFacilityView.getBorrowerGuarantorList()) {
                List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForGuarantor(existingGuarantorDetailView.getExistingCreditTypeDetailViewList(), existingGuarantorDetail, user);
                existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
                log.info("persist existingCreditTypeDetailList...");
            }
        }
    }

    public List<ExistingCreditDetailView> onFindBorrowerExistingCreditFacility(Long workCaseId) {
        log.info("onFindBorrowerExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        ExistingCreditFacility existingCreditFacility;
        List<ExistingCreditDetailView> borrowerCreditDetailViewList;
        existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
        borrowerCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

        if(existingCreditFacility != null){
            List<ExistingCreditDetail> borrowerCreditDetailList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,1);
            borrowerCreditDetailViewList = existingCreditDetailTransform.transformsToView(borrowerCreditDetailList);
            log.info("find :: borrowerCreditDetailViewList ...");
        }

        return  borrowerCreditDetailViewList;
    }

    public ExistingCreditFacilityView onFindExistingCreditFacility(Long workCaseId) {
        log.info("onSaveExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase Id  "+ workCase.getId() + " getCaNumber " + workCase.getCaNumber());
        ExistingCreditFacility existingCreditFacility;
        ExistingCreditFacilityView existingCreditFacilityView;

        existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);

        if(existingCreditFacility != null){
            existingCreditFacilityView = existingCreditFacilityTransform.transformsToView(existingCreditFacility);
            List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailDAO.findByExistingCreditFacility(existingCreditFacility);
            List<ExistingConditionDetailView> existingConditionDetailListView = existingConditionDetailTransform.transformsToView(existingConditionDetailList);
            log.info("onFind :: existingConditionDetailList ...");
            existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailListView);
            
            List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,1);
            List<ExistingCreditDetailView> borrowerComExistingCreditViewList = onFindCreditDetailChild(borrowerComExistingCreditList);
            log.info("onFind :: borrowerComExistingCreditList ...");
            existingCreditFacilityView.setBorrowerComExistingCredit(borrowerComExistingCreditViewList);

            List<ExistingCreditDetail> borrowerRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,2);
            List<ExistingCreditDetailView> borrowerRetailExistingCreditViewList = onFindCreditDetailChild(borrowerRetailExistingCreditList);
            log.info("onFind :: borrowerRetailExistingCreditList ...");
            existingCreditFacilityView.setBorrowerRetailExistingCredit(borrowerRetailExistingCreditViewList);

            List<ExistingCreditDetail> borrowerAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,3);
            List<ExistingCreditDetailView> borrowerAppInRLOSCreditViewList = onFindCreditDetailChild(borrowerAppInRLOSCreditList);
            log.info("onFind :: borrowerAppInRLOSCreditList ...");
            existingCreditFacilityView.setBorrowerAppInRLOSCredit(borrowerAppInRLOSCreditViewList);

            List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,1);
            List<ExistingCreditDetailView> relatedComExistingCreditViewList = onFindCreditDetailChild(relatedComExistingCreditList);
            log.info("onFind :: relatedComExistingCreditList ...");
            existingCreditFacilityView.setRelatedComExistingCredit(relatedComExistingCreditViewList);

            List<ExistingCreditDetail> relatedRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,2);
            List<ExistingCreditDetailView> relatedRetailExistingCreditViewList = onFindCreditDetailChild(relatedRetailExistingCreditList);
            log.info("onFind :: relatedRetailExistingCreditList ...");
            existingCreditFacilityView.setRelatedRetailExistingCredit(relatedRetailExistingCreditViewList);

            List<ExistingCreditDetail> relatedAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,3);
            List<ExistingCreditDetailView> relatedAppInRLOSCreditViewList = onFindCreditDetailChild(relatedAppInRLOSCreditList);
            log.info("onFind :: relatedAppInRLOSCreditList ...");
            existingCreditFacilityView.setRelatedAppInRLOSCredit(relatedAppInRLOSCreditViewList);

            List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,1);
            List<ExistingCollateralDetailView> borrowerCollateralDetailViewList = onFindCollateralDetailChild(borrowerCollateralDetailList);
            log.info("onFind :: borrowerCollateralDetailList ...");
            existingCreditFacilityView.setBorrowerCollateralList(borrowerCollateralDetailViewList);

            List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,2);
            List<ExistingCollateralDetailView> relatedCollateralDetailViewList = onFindCollateralDetailChild(relatedCollateralDetailList);
            log.info("onFind :: relatedCollateralDetailList ...");
            existingCreditFacilityView.setRelatedCollateralList(relatedCollateralDetailViewList);


            List<ExistingGuarantorDetail> borrowerGuarantorDetailList = existingGuarantorDetailDAO.findByExistingCreditFacility(existingCreditFacility);
            List<ExistingGuarantorDetailView> borrowerGuarantorDetailViewList = onFindGuarantorDetailChild(borrowerGuarantorDetailList);
            log.info("onFind :: borrowerGuarantorDetailList ...");
            existingCreditFacilityView.setBorrowerGuarantorList(borrowerGuarantorDetailViewList);
            return existingCreditFacilityView;
        }

        return null;
    }

    public List<ExistingCreditDetailView> onFindCreditDetailChild(List<ExistingCreditDetail> existingCreditDetailList) {
        List<ExistingSplitLineDetail> existingSplitLineDetailList;
        List<ExistingSplitLineDetailView> existingSplitLineDetailViewList;
        List<ExistingCreditTierDetail> existingCreditTierDetailList;
        List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
        List<ExistingCreditDetailView>      existingCreditDetailViewList;

        existingCreditDetailViewList = existingCreditDetailTransform.transformsToView(existingCreditDetailList);

        for(int i =0;i<existingCreditDetailViewList.size();i++){
            existingCreditTierDetailList = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetailList.get(i));
            existingCreditTierDetailViewList = existingCreditTierTransform.transformsToView(existingCreditTierDetailList);
            existingCreditDetailViewList.get(i).setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);
            
            existingSplitLineDetailList = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetailList.get(i));
            existingSplitLineDetailViewList = existingSplitLineTransform.transformsToView(existingSplitLineDetailList);
            existingCreditDetailViewList.get(i).setExistingSplitLineDetailViewList(existingSplitLineDetailViewList);
        }

        return existingCreditDetailViewList;

    }

    public List<ExistingCollateralDetailView> onFindCollateralDetailChild(List<ExistingCollateralDetail> existingCollateralDetailList) {
        List<ExistingCreditTypeDetail> existingCreditTypeDetailList;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
        List<ExistingCollateralDetailView> existingCollateralDetailViewList;

        existingCollateralDetailViewList = existingCollateralDetailTransform.transformsToView(existingCollateralDetailList);

        for(int i =0;i<existingCollateralDetailViewList.size();i++){
            existingCreditTypeDetailList = existingCreditTypeDetailDAO.findByExistingCollateralDetail(existingCollateralDetailList.get(i));
            existingCreditTypeDetailViewList = existingCreditTypeDetailTransform.transformsToView(existingCreditTypeDetailList);
            existingCollateralDetailViewList.get(i).setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        }

        return existingCollateralDetailViewList;
    }

    public List<ExistingGuarantorDetailView> onFindGuarantorDetailChild(List<ExistingGuarantorDetail> existingGuarantorDetailList) {
        List<ExistingCreditTypeDetail> existingCreditTypeDetailList;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
        List<ExistingGuarantorDetailView> existingGuarantorDetailViewList;

        existingGuarantorDetailViewList = existingGuarantorDetailTransform.transformsToView(existingGuarantorDetailList);

        for(int i =0;i<existingGuarantorDetailViewList.size();i++){
            existingCreditTypeDetailList = existingCreditTypeDetailDAO.findByExistingGuarantorDetail(existingGuarantorDetailList.get(i));
            existingCreditTypeDetailViewList = existingCreditTypeDetailTransform.transformsToView(existingCreditTypeDetailList);
            existingGuarantorDetailViewList.get(i).setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        }

        return existingGuarantorDetailViewList;
    }
}