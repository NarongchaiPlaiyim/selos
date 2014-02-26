package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.RelationValue;
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
    ExistingGuarantorCreditTransform existingGuarantorCreditTransform;
    @Inject
    ExistingCollateralCreditTransform existingCollateralCreditTransform;
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
    ExistingGuarantorCreditDAO existingGuarantorCreditDAO;
    @Inject
    ExistingCollateralCreditDAO existingCollateralCreditDAO;
    @Inject
    ExistingSplitLineDetailDAO existingSplitLineDetailDAO;
    @Inject
    ExistingCollateralDetailDAO existingCollateralDetailDAO;
    @Inject
    ExistingGuarantorDetailDAO existingGuarantorDetailDAO;

    @Inject
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
        log.info("workCase Id  "+ workCase.getId() + " getApp " + workCase.getAppNumber());

        ExistingCreditFacility existingCreditFacility = existingCreditFacilityTransform.transformsToModelDB(existingCreditFacilityView, workCase, user);
        existingCreditFacilityDAO.persist(existingCreditFacility);
        log.info("persist :: existingCreditFacility..." + existingCreditFacility.getId());

        List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailDAO.findByExistingCreditFacility(existingCreditFacility);
        if(existingConditionDetailList!=null && existingConditionDetailList.size()>0){
            existingConditionDetailDAO.delete(existingConditionDetailList);
        }

        if(existingCreditFacilityView.getExistingConditionDetailViewList()!=null && existingCreditFacilityView.getExistingConditionDetailViewList().size() > 0) {
            log.info("getExistingConditionDetailViewList size = ... " + existingCreditFacilityView.getExistingConditionDetailViewList().size());
            List<ExistingConditionDetail> existingConditionDetailListAdd = existingConditionDetailTransform.transformsToModel(existingCreditFacilityView.getExistingConditionDetailViewList(), existingCreditFacility, user);
            existingConditionDetailDAO.persist(existingConditionDetailListAdd);
            log.info("persist :: existingConditionDetailList ...");
        }

        List<ExistingCollateralDetail> borrowerCollateralDetailListDel = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,1);
        if(borrowerCollateralDetailListDel!=null && borrowerCollateralDetailListDel.size()>0){
            for (int i=0 ;i<borrowerCollateralDetailListDel.size();i++) {
                log.info(" Round borrowerComExistingCreditListDel  is " + i );
                ExistingCollateralDetail existingCollateralDetail =  borrowerCollateralDetailListDel.get(i);
                List<ExistingCollateralCredit>  existingCollateralCreditDel = existingCollateralCreditDAO.findByExistingCollateralDetail(existingCollateralDetail);
                existingCollateralCreditDAO.delete(existingCollateralCreditDel);
            }
            existingCollateralDetailDAO.delete(borrowerCollateralDetailListDel);
        }

        List<ExistingCollateralDetail> relatedCollateralDetailListDel = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,2);
        if(relatedCollateralDetailListDel!=null && relatedCollateralDetailListDel.size()>0){
            for (int i=0 ;i<relatedCollateralDetailListDel.size();i++) {
                log.info(" Round relatedComExistingCreditListDel  is " + i );
                ExistingCollateralDetail existingCollateralDetail =  relatedCollateralDetailListDel.get(i);
                List<ExistingCollateralCredit>  existingCollateralCreditDel = existingCollateralCreditDAO.findByExistingCollateralDetail(existingCollateralDetail);
                existingCollateralCreditDAO.delete(existingCollateralCreditDel);
            }
            existingCollateralDetailDAO.delete(relatedCollateralDetailListDel);
        }

        List<ExistingGuarantorDetail> borrowerGuarantorDetailListDel = existingGuarantorDetailDAO.findByExistingCreditFacility(existingCreditFacility);
        if(borrowerGuarantorDetailListDel!=null && borrowerGuarantorDetailListDel.size()>0){
            for (int i=0 ;i<borrowerGuarantorDetailListDel.size();i++) {
                ExistingGuarantorDetail existingGuarantorDetail =  borrowerGuarantorDetailListDel.get(i);
                List<ExistingGuarantorCredit>  existingGuarantorCreditListDel = existingGuarantorCreditDAO.findByExistingGuarantorDetail(existingGuarantorDetail);
                if(existingGuarantorCreditListDel.size()>0){
                    existingGuarantorCreditDAO.delete(existingGuarantorCreditListDel);
                }
            }
            existingGuarantorDetailDAO.delete(borrowerGuarantorDetailListDel);
        }


        List<ExistingCreditDetail> borrowerComExistingCreditListDel = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(),CreditCategory.COMMERCIAL);
        if(borrowerComExistingCreditListDel!=null && borrowerComExistingCreditListDel.size()>0){
            for (int i=0 ;i<borrowerComExistingCreditListDel.size();i++) {
                log.info(" Round borrowerComExistingCreditListDel  is " + i );
                ExistingCreditDetail existingCreditDetail =  borrowerComExistingCreditListDel.get(i);

                List<ExistingCreditTierDetail>  existingCreditTierDetailListDel = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingCreditTierDetailListDel!=null && existingCreditTierDetailListDel.size()>0){
                    existingCreditTierDetailDAO.delete(existingCreditTierDetailListDel);
                }

                List<ExistingSplitLineDetail>  existingSplitLineDetailListDel = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingSplitLineDetailListDel!=null && existingSplitLineDetailListDel.size()>0){
                    existingSplitLineDetailDAO.delete(existingSplitLineDetailListDel);
                }
            }
            existingCreditDetailDAO.delete(borrowerComExistingCreditListDel);
        }

        if(existingCreditFacilityView.getBorrowerComExistingCredit()!=null && existingCreditFacilityView.getBorrowerComExistingCredit().size()>0){
            List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerComExistingCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(borrowerComExistingCreditList);
            log.info("persist borrower existingCreditDetailList...");

            for (int i=0 ;i<borrowerComExistingCreditList.size();i++) {
                log.info(" Round borrowerComExistingCreditList  is " + i );
                ExistingCreditDetail existingCreditDetail =  borrowerComExistingCreditList.get(i);
                ExistingCreditDetailView existingCreditDetailView = existingCreditFacilityView.getBorrowerComExistingCredit().get(i);

                if(existingCreditDetailView.getExistingCreditTierDetailViewList()!=null && existingCreditDetailView.getExistingCreditTierDetailViewList().size()>0){
                    List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformsToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                    log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
                    existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                    log.info("persist borrower existingCreditTierDetailList...");
                }

                if(existingCreditDetailView.getExistingSplitLineDetailViewList()!=null && existingCreditDetailView.getExistingSplitLineDetailViewList().size()>0){
                    List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformsToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                    log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
                    existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                    log.info("persist borrower existingSplitLineDetailList...");
                }

            }

            if(existingCreditFacilityView.getBorrowerCollateralList()!=null && existingCreditFacilityView.getBorrowerCollateralList().size()>0){
                List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerCollateralList(), existingCreditFacility, user);
                existingCollateralDetailDAO.persist(borrowerCollateralDetailList);
                log.info("persist borrowerCollateralDetailList...");

                for (int i=0 ;i<borrowerCollateralDetailList.size();i++) {
                    log.info(" Round borrowerCollateralDetailList  is " + i );
                    ExistingCollateralDetail existingCollateralDetail =  borrowerCollateralDetailList.get(i);
                    ExistingCollateralDetailView existingCollateralDetailView = existingCreditFacilityView.getBorrowerCollateralList().get(i);
                    List<ExistingCollateralCredit> existingCollateralCreditList = existingCollateralCreditTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(),borrowerComExistingCreditList, existingCollateralDetail, user);
                    existingCollateralCreditDAO.persist(existingCollateralCreditList);
                    log.info("persist borrower existingCollateralCreditList...");
                }
            }

            if(existingCreditFacilityView.getBorrowerGuarantorList()!=null && existingCreditFacilityView.getBorrowerGuarantorList().size()>0){
                List<ExistingGuarantorDetail> borrowerGuarantorDetailList = existingGuarantorDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerGuarantorList(), existingCreditFacility, user);
                existingGuarantorDetailDAO.persist(borrowerGuarantorDetailList);
                log.info("persist borrowerGuarantorDetailList...");

                for (int i=0 ;i<borrowerGuarantorDetailList.size();i++) {
                    log.info(" Round borrowerGuarantorDetailList  is " + i );
                    ExistingGuarantorDetail existingGuarantorDetail = borrowerGuarantorDetailList.get(i);
                    ExistingGuarantorDetailView existingGuarantorDetailView = existingCreditFacilityView.getBorrowerGuarantorList().get(i);
                    List<ExistingGuarantorCredit> existingGuarantorCreditList= existingGuarantorCreditTransform.transformsToModelForGuarantor(existingGuarantorDetailView.getExistingCreditTypeDetailViewList(), borrowerComExistingCreditList ,existingGuarantorDetail, user);
                    existingGuarantorCreditDAO.persist(existingGuarantorCreditList);
                    log.info("persist related existingGuarantorCreditDAO...");
                }
            }
        }

        //Borrower Retail
        List<ExistingCreditDetail> borrowerRetailExistingCredit = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility, RelationValue.BORROWER.value() ,CreditCategory.RETAIL);
        if(borrowerRetailExistingCredit!=null && borrowerRetailExistingCredit.size()>0){
            for (int i=0 ;i<borrowerRetailExistingCredit.size();i++) {
                log.info(" Round borrowerRetailExistingCredit  is " + i );
                ExistingCreditDetail existingCreditDetail =  borrowerRetailExistingCredit.get(i);

                List<ExistingCreditTierDetail>  existingCreditTierDetailListDel = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingCreditTierDetailListDel!=null && existingCreditTierDetailListDel.size()>0){
                    existingCreditTierDetailDAO.delete(existingCreditTierDetailListDel);
                }

                List<ExistingSplitLineDetail>  existingSplitLineDetailListDel = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingSplitLineDetailListDel!=null && existingSplitLineDetailListDel.size()>0){
                    existingSplitLineDetailDAO.delete(existingSplitLineDetailListDel);
                }
            }
            existingCreditDetailDAO.delete(borrowerRetailExistingCredit);
        }

        if(existingCreditFacilityView.getBorrowerRetailExistingCredit()!=null && existingCreditFacilityView.getBorrowerRetailExistingCredit().size()>0){
            borrowerRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerRetailExistingCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
            log.info("persist borrower Retail existingCreditDetailList...");
        }

        //Borrower RLOS
        List<ExistingCreditDetail> borrowerAppInRLOSCredit = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(),CreditCategory.RLOS_APP_IN);
        if(borrowerAppInRLOSCredit!=null && borrowerAppInRLOSCredit.size()>0){
            existingCreditDetailDAO.delete(borrowerAppInRLOSCredit);
        }

        if(existingCreditFacilityView.getBorrowerAppInRLOSCredit()!=null && existingCreditFacilityView.getBorrowerAppInRLOSCredit().size()>0){
            borrowerAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerAppInRLOSCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(borrowerAppInRLOSCredit);
            log.info("persist borrower RLOS existingCreditDetailList...");
        }


        List<ExistingCreditDetail> relatedComExistingCreditListDel = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value(),CreditCategory.COMMERCIAL);
        if(relatedComExistingCreditListDel!=null && relatedComExistingCreditListDel.size()>0){
            existingCreditDetailDAO.delete(relatedComExistingCreditListDel);
        }

        if(existingCreditFacilityView.getRelatedComExistingCredit()!=null && existingCreditFacilityView.getRelatedComExistingCredit().size()>0){
            List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedComExistingCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(relatedComExistingCreditList);
            log.info("persist related existingCreditDetailList...");

            for (int i=0 ;i<relatedComExistingCreditList.size();i++) {
                log.info(" Round relatedComExistingCreditList  is " + i );
                ExistingCreditDetail existingCreditDetail =  relatedComExistingCreditList.get(i);
                ExistingCreditDetailView existingCreditDetailView = existingCreditFacilityView.getRelatedComExistingCredit().get(i);

                List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformsToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
                log.info("persist related existingCreditDetailList..." + existingCreditDetail.getId() );
                existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
                log.info("persist related existingCreditTierDetailList...");

                List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformsToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
                log.info("persist related existingCreditDetailList..." + existingCreditDetail.getId() );
                existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
                log.info("persist related existingSplitLineDetailList...");
            }

            if(existingCreditFacilityView.getRelatedCollateralList()!=null && existingCreditFacilityView.getRelatedCollateralList().size()>0){
                List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedCollateralList(), existingCreditFacility, user);
                existingCollateralDetailDAO.persist(relatedCollateralDetailList);
                log.info("persist relatedCollateralDetailList...");

                for (int i=0 ;i<relatedCollateralDetailList.size();i++) {
                    log.info(" Round relatedCollateralDetailList  is " + i );
                    ExistingCollateralDetail existingCollateralDetail =  relatedCollateralDetailList.get(i);
                    ExistingCollateralDetailView existingCollateralDetailView = existingCreditFacilityView.getRelatedCollateralList().get(i);
                    List<ExistingCollateralCredit> existingCollateralCreditList = existingCollateralCreditTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(),relatedComExistingCreditList, existingCollateralDetail, user);
                    existingCollateralCreditDAO.persist(existingCollateralCreditList);
                    log.info("persist related existingCollateralCreditList...");
                }
            }
        }

        //Borrower Retail
        List<ExistingCreditDetail> relatedRetailExistingCredit = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility, RelationValue.DIRECTLY_RELATED.value() ,CreditCategory.RETAIL);
        if(relatedRetailExistingCredit!=null && relatedRetailExistingCredit.size()>0){
            existingCreditDetailDAO.delete(relatedRetailExistingCredit);
        }

        if(existingCreditFacilityView.getRelatedRetailExistingCredit()!=null && existingCreditFacilityView.getRelatedRetailExistingCredit().size()>0){
            relatedRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedRetailExistingCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(relatedRetailExistingCredit);
            log.info("persist related Retail existingCreditDetailList...");
        }

        //Borrower RLOS
        List<ExistingCreditDetail> relatedAppInRLOSCredit = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value(),CreditCategory.RLOS_APP_IN);
        if(relatedAppInRLOSCredit!=null && relatedAppInRLOSCredit.size()>0){
            existingCreditDetailDAO.delete(relatedAppInRLOSCredit);
        }

        if(existingCreditFacilityView.getRelatedAppInRLOSCredit()!=null && existingCreditFacilityView.getRelatedAppInRLOSCredit().size()>0){
            relatedAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedAppInRLOSCredit(), existingCreditFacility, user);
            existingCreditDetailDAO.persist(relatedAppInRLOSCredit);
            log.info("persist related RLOS existingCreditDetailList...");
        }


       /*
        List<ExistingCollateralDetail> borrowerCollateralDetailListDel = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,1);
        if(borrowerCollateralDetailListDel.size()>0){
            for (int i=0 ;i<borrowerCollateralDetailListDel.size();i++) {
                log.info(" Round relatedComExistingCreditListDel  is " + i );
                ExistingCollateralDetail existingCollateralDetail =  borrowerCollateralDetailListDel.get(i);
                List<ExistingCreditTypeDetail>  existingCreditTypeDetailtDel = existingCreditTypeDetailDAO.findByExistingCollateralDetail(existingCollateralDetail);
                existingCreditTypeDetailDAO.delete(existingCreditTypeDetailtDel);
            }
            existingCollateralDetailDAO.delete(borrowerCollateralDetailListDel);
        }
        List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(borrowerCollateralDetailList);
        log.info("persist borrowerCollateralDetailList...");


        for (int i=0 ;i<borrowerCollateralDetailList.size();i++) {
            log.info(" Round borrowerCollateralDetailList  is " + i );
            ExistingCollateralDetail existingCollateralDetail =  borrowerCollateralDetailList.get(i);
            ExistingCollateralDetailView existingCollateralDetailView = existingCreditFacilityView.getBorrowerCollateralList().get(i);
            List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
            existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
            log.info("persist related existingCreditTypeDetailList...");
        } 
        */


        
        /*
        List<ExistingCollateralDetail> relatedCollateralDetailListDel = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,2);
        if(relatedCollateralDetailListDel.size()>0){
            for (int i=0 ;i<relatedCollateralDetailListDel.size();i++) {
                log.info(" Round relatedComExistingCreditListDel  is " + i );
                ExistingCollateralDetail existingCollateralDetail =  relatedCollateralDetailListDel.get(i);
                List<ExistingCreditTypeDetail>  existingCreditTypeDetailtDel = existingCreditTypeDetailDAO.findByExistingCollateralDetail(existingCollateralDetail);
                existingCreditTypeDetailDAO.delete(existingCreditTypeDetailtDel);
            }
            existingCollateralDetailDAO.delete(relatedCollateralDetailListDel);
        }

        List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedCollateralList(), existingCreditFacility, user);
        existingCollateralDetailDAO.persist(relatedCollateralDetailList);
        log.info("persist relatedCollateralDetailList...");

        for (int i=0 ;i<relatedCollateralDetailList.size();i++) {
            log.info(" Round relatedCollateralDetailList  is " + i );
            ExistingCollateralDetail existingCollateralDetail =  relatedCollateralDetailList.get(i);
            ExistingCollateralDetailView existingCollateralDetailView = existingCreditFacilityView.getRelatedCollateralList().get(i);
            List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForCollateral(existingCollateralDetailView.getExistingCreditTypeDetailViewList(), existingCollateralDetail, user);
            existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
            log.info("persist related existingCreditTypeDetailList...");
        }
        */

    }

    public List<ExistingCreditDetailView> onFindBorrowerExistingCreditFacility(Long workCaseId) {
        log.info("onFindBorrowerExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        ExistingCreditFacility existingCreditFacility;
        List<ExistingCreditDetailView> borrowerCreditDetailViewList;
        existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
        borrowerCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

        if(existingCreditFacility != null){
            List<ExistingCreditDetail> borrowerCreditDetailList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,1, CreditCategory.COMMERCIAL);
            borrowerCreditDetailViewList = existingCreditDetailTransform.transformsToView(borrowerCreditDetailList);
            log.info("find :: borrowerCreditDetailViewList ...");
        }

        return  borrowerCreditDetailViewList;
    }

    public ExistingCreditFacilityView onFindExistingCreditFacility(Long workCaseId) {
        log.info("onFindExistingCreditFacility begin");

        log.info("workCaseId {} ", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        log.info("workCase Id  "+ workCase.getId() + " getCaNumber " + basicInfo.getCaNumber());
        ExistingCreditFacility existingCreditFacility;
        ExistingCreditFacilityView existingCreditFacilityView;

        existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);

        if(existingCreditFacility != null){
            existingCreditFacilityView = existingCreditFacilityTransform.transformsToView(existingCreditFacility);

            List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailDAO.findByExistingCreditFacility(existingCreditFacility);
            log.info("onFind :: existingConditionDetailList ..." + existingConditionDetailList.size());
            List<ExistingConditionDetailView> existingConditionDetailListView = existingConditionDetailTransform.transformsToView(existingConditionDetailList);
            log.info("onFind :: existingConditionDetailList ...");
            existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailListView);
            
            List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(),CreditCategory.COMMERCIAL);
            log.info("onFind :: borrowerComExistingCreditList ..." + borrowerComExistingCreditList.size());
            List<ExistingCreditDetailView> borrowerComExistingCreditViewList = onFindCreditDetailChild(borrowerComExistingCreditList);
            log.info("onFind :: borrowerComExistingCreditList ...");
            existingCreditFacilityView.setBorrowerComExistingCredit(borrowerComExistingCreditViewList);

            List<ExistingCreditDetail> borrowerRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(),CreditCategory.RETAIL);
            log.info("onFind :: borrowerRetailExistingCreditList ..." + borrowerRetailExistingCreditList.size());
            List<ExistingCreditDetailView> borrowerRetailExistingCreditViewList = onFindCreditDetailChild(borrowerRetailExistingCreditList);
            log.info("onFind :: borrowerRetailExistingCreditList ...");
            existingCreditFacilityView.setBorrowerRetailExistingCredit(borrowerRetailExistingCreditViewList);

            List<ExistingCreditDetail> borrowerAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(),CreditCategory.RLOS_APP_IN);
            log.info("onFind :: borrowerAppInRLOSCreditList ..." + borrowerAppInRLOSCreditList.size());
            List<ExistingCreditDetailView> borrowerAppInRLOSCreditViewList = onFindCreditDetailChild(borrowerAppInRLOSCreditList);
            log.info("onFind :: borrowerAppInRLOSCreditList ...");
            existingCreditFacilityView.setBorrowerAppInRLOSCredit(borrowerAppInRLOSCreditViewList);

            List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value(),CreditCategory.COMMERCIAL);
            log.info("onFind :: relatedComExistingCreditList ..." + relatedComExistingCreditList.size());
            List<ExistingCreditDetailView> relatedComExistingCreditViewList = onFindCreditDetailChild(relatedComExistingCreditList);
            log.info("onFind :: relatedComExistingCreditList ...");
            existingCreditFacilityView.setRelatedComExistingCredit(relatedComExistingCreditViewList);

            List<ExistingCreditDetail> relatedRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value(),CreditCategory.RETAIL);
            log.info("onFind :: relatedRetailExistingCreditList ..." + relatedRetailExistingCreditList.size());
            List<ExistingCreditDetailView> relatedRetailExistingCreditViewList = onFindCreditDetailChild(relatedRetailExistingCreditList);
            log.info("onFind :: relatedRetailExistingCreditList ...");
            existingCreditFacilityView.setRelatedRetailExistingCredit(relatedRetailExistingCreditViewList);

            List<ExistingCreditDetail> relatedAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value(),CreditCategory.RLOS_APP_IN);
            log.info("onFind :: relatedAppInRLOSCreditList ..." + relatedAppInRLOSCreditList.size());
            List<ExistingCreditDetailView> relatedAppInRLOSCreditViewList = onFindCreditDetailChild(relatedAppInRLOSCreditList);
            log.info("onFind :: relatedAppInRLOSCreditList ...");
            existingCreditFacilityView.setRelatedAppInRLOSCredit(relatedAppInRLOSCreditViewList);

            List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,RelationValue.BORROWER.value());
            log.info("onFind :: borrowerCollateralDetailList ..." + borrowerCollateralDetailList.size());
            List<ExistingCollateralDetailView> borrowerCollateralDetailViewList = onFindCollateralDetailChild(borrowerCollateralDetailList);
            log.info("onFind :: borrowerCollateralDetailViewList ...");
            existingCreditFacilityView.setBorrowerCollateralList(borrowerCollateralDetailViewList);

            List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,RelationValue.DIRECTLY_RELATED.value());
            log.info("onFind :: relatedCollateralDetailList ..." + relatedCollateralDetailList.size());
            List<ExistingCollateralDetailView> relatedCollateralDetailViewList = onFindCollateralDetailChild(relatedCollateralDetailList);
            log.info("onFind :: relatedCollateralDetailViewList ...");
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
        log.info("onFindCreditDetailChild begin");
        List<ExistingSplitLineDetail> existingSplitLineDetailList;
        List<ExistingSplitLineDetailView> existingSplitLineDetailViewList;
        List<ExistingCreditTierDetail> existingCreditTierDetailList;
        List<ExistingCreditTierDetailView> existingCreditTierDetailViewList;
        List<ExistingCreditDetailView>      existingCreditDetailViewList;

        existingCreditDetailViewList = existingCreditDetailTransform.transformsToView(existingCreditDetailList);

        for(int i =0;i<existingCreditDetailViewList.size();i++){
            log.info("existingCreditDetail at  " + i + " ID  " + existingCreditDetailList.get(i).getId());

            existingCreditTierDetailList = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetailList.get(i));
            log.info("existingCreditTierDetailList size " + existingCreditTierDetailList.size());
            existingCreditTierDetailViewList = existingCreditTierTransform.transformsToView(existingCreditTierDetailList);
            existingCreditDetailViewList.get(i).setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);
            
            existingSplitLineDetailList = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetailList.get(i));
            log.info("existingSplitLineDetailList size " + existingSplitLineDetailList.size());
            existingSplitLineDetailViewList = existingSplitLineTransform.transformsToView(existingSplitLineDetailList);
            existingCreditDetailViewList.get(i).setExistingSplitLineDetailViewList(existingSplitLineDetailViewList);
        }
        log.info("onFindCreditDetailChild end ");
        return existingCreditDetailViewList;

    }

    public List<ExistingCollateralDetailView> onFindCollateralDetailChild(List<ExistingCollateralDetail> existingCollateralDetailList) {
        List<ExistingCollateralCredit> existingCollateralCreditList;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
        List<ExistingCollateralDetailView> existingCollateralDetailViewList;

        existingCollateralDetailViewList = existingCollateralDetailTransform.transformsToView(existingCollateralDetailList);

        for(int i =0;i<existingCollateralDetailViewList.size();i++){
            existingCollateralCreditList = existingCollateralCreditDAO.findByExistingCollateralDetail(existingCollateralDetailList.get(i));
            existingCreditTypeDetailViewList = existingCollateralCreditTransform.transformsToView(existingCollateralCreditList);
            existingCollateralDetailViewList.get(i).setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        }

        return existingCollateralDetailViewList;
    }

    public List<ExistingGuarantorDetailView> onFindGuarantorDetailChild(List<ExistingGuarantorDetail> existingGuarantorDetailList) {
        List<ExistingGuarantorCredit> existingGuarantorCreditList;
        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList;
        List<ExistingGuarantorDetailView> existingGuarantorDetailViewList;

        existingGuarantorDetailViewList = existingGuarantorDetailTransform.transformsToView(existingGuarantorDetailList);

        for(int i =0;i<existingGuarantorDetailViewList.size();i++){
            existingGuarantorCreditList = existingGuarantorCreditDAO.findByExistingGuarantorDetail(existingGuarantorDetailList.get(i));
            existingCreditTypeDetailViewList = existingGuarantorCreditTransform.transformsToView(existingGuarantorCreditList);
            existingGuarantorDetailViewList.get(i).setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        }

        return existingGuarantorDetailViewList;
    }

    public List<CustomerInfoView> getCustomerListByWorkCaseId(long workCaseId){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        List<Customer> customerList = customerDAO.findBorrowerByWorkCaseId(workCaseId);

        CustomerInfoView customerInfoView;
        CustomerInfoView spouseInfoView;
        for(Customer customer : customerList){
            customerInfoView = new CustomerInfoView();
            customerInfoView = customerTransform.transformToView(customer);

            customerInfoView.setListIndex(customerInfoViewList.size());

            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(Long.toString(customer.getSpouseId()) != null && customer.getSpouseId() != 0 ){
                    Customer spouse = new Customer();
                    spouse = customerDAO.findSpouseById(customer.getSpouseId());
                    if(spouse != null){
                        spouseInfoView = new CustomerInfoView();
                        spouseInfoView = customerTransform.transformToView(spouse);
                        spouseInfoView.setListIndex(customerInfoViewList.size());
                        spouseInfoView.setIsSpouse(1);
                        customerInfoView.setSpouse(spouseInfoView);
                    }
                }
            }

            customerInfoViewList.add(customerInfoView);
        }

        log.info("getCustomerListByWorkCaseId ::: customerList : {}", customerList);
        //customerInfoViewList = customerTransform.transformToViewList(customerList);
        log.info("getCustomerListByWorkCaseId ::: customerInfoViewList : {}", customerInfoViewList);

        return customerInfoViewList;
    }
}