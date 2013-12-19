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

        if (existingCreditFacilityView.getExistingConditionDetailViewList().size() > 0) {
            List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailDAO.findByExistingCreditFacility(existingCreditFacility);
            existingConditionDetailDAO.delete(existingConditionDetailList);
        }
        log.info("getExistingConditionDetailViewList size = ... " + existingCreditFacilityView.getExistingConditionDetailViewList().size());
        List<ExistingConditionDetail> existingConditionDetailList = existingConditionDetailTransform.transformsToModel(existingCreditFacilityView.getExistingConditionDetailViewList(), existingCreditFacility, user);
        existingConditionDetailDAO.persist(existingConditionDetailList);
        log.info("persist :: existingConditionDetailList ...");

        log.info("persist borrower getBorrowerComExistingCredit size ..." + existingCreditFacilityView.getBorrowerComExistingCredit().size());


        List<ExistingCreditDetail> borrowerComExistingCreditListDel = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,1);
        if(borrowerComExistingCreditListDel.size()>0){
            for (int i=0 ;i<borrowerComExistingCreditListDel.size();i++) {
                log.info(" Round borrowerComExistingCreditListDel  is " + i );
                ExistingCreditDetail existingCreditDetail =  borrowerComExistingCreditListDel.get(i);

                List<ExistingCreditTierDetail>  existingCreditTierDetailListDel = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingCreditTierDetailListDel.size()>0){
                    existingCreditTierDetailDAO.delete(existingCreditTierDetailListDel);
                }

                List<ExistingSplitLineDetail>  existingSplitLineDetailListDel = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingSplitLineDetailListDel.size()>0){
                    existingSplitLineDetailDAO.delete(existingSplitLineDetailListDel);
                }
            }
            existingCreditDetailDAO.delete(borrowerComExistingCreditListDel);
        }

        List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerComExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerComExistingCreditList);
        log.info("persist borrower existingCreditDetailList...");

        for (int i=0 ;i<borrowerComExistingCreditList.size();i++) {
            log.info(" Round borrowerComExistingCreditList  is " + i );
            ExistingCreditDetail existingCreditDetail =  borrowerComExistingCreditList.get(i);
            ExistingCreditDetailView existingCreditDetailView = existingCreditFacilityView.getBorrowerComExistingCredit().get(i);

            List<ExistingCreditTierDetail> existingCreditTierDetailList = existingCreditTierTransform.transformsToModel(existingCreditDetailView.getExistingCreditTierDetailViewList(), existingCreditDetail, user);
            log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
            existingCreditTierDetailDAO.persist(existingCreditTierDetailList);
            log.info("persist borrower existingCreditTierDetailList...");

            List<ExistingSplitLineDetail> existingSplitLineDetailList = existingSplitLineTransform.transformsToModel(existingCreditDetailView.getExistingSplitLineDetailViewList(), existingCreditDetail, user);
            log.info("persist borrower existingCreditDetailList..." + existingCreditDetail.getId() );
            existingSplitLineDetailDAO.persist(existingSplitLineDetailList);
            log.info("persist borrower existingSplitLineDetailList...");
        }


        List<ExistingCreditDetail> borrowerRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerRetailExistingCredit);
        log.info("persist borrower Retail existingCreditDetailList...");

        List<ExistingCreditDetail> borrowerAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(borrowerAppInRLOSCredit);
        log.info("persist borrower RLOS existingCreditDetailList...");

        List<ExistingCreditDetail> relatedComExistingCreditListDel = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,1);
        if(relatedComExistingCreditListDel.size()>0){
            for (int i=0 ;i<relatedComExistingCreditListDel.size();i++) {
                log.info(" Round relatedComExistingCreditListDel  is " + i );
                ExistingCreditDetail existingCreditDetail =  relatedComExistingCreditListDel.get(i);

                List<ExistingCreditTierDetail>  existingCreditTierDetailListDel = existingCreditTierDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingCreditTierDetailListDel.size()>0){
                    existingCreditTierDetailDAO.delete(existingCreditTierDetailListDel);
                }

                List<ExistingSplitLineDetail>  existingSplitLineDetailListDel = existingSplitLineDetailDAO.findByExistingCreditDetail(existingCreditDetail);
                if(existingSplitLineDetailListDel.size()>0){
                    existingSplitLineDetailDAO.delete(existingSplitLineDetailListDel);
                }
                existingCreditDetailDAO.delete(relatedComExistingCreditListDel);
            }
        }

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

        List<ExistingCreditDetail> relatedRetailExistingCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedRetailExistingCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedRetailExistingCredit);
        log.info("persist related Retail existingCreditDetailList...");

        List<ExistingCreditDetail> relatedAppInRLOSCredit = existingCreditDetailTransform.transformsToModel(existingCreditFacilityView.getRelatedAppInRLOSCredit(), existingCreditFacility, user);
        existingCreditDetailDAO.persist(relatedAppInRLOSCredit);
        log.info("persist related RLOS existingCreditDetailList...");

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

        List<ExistingGuarantorDetail> borrowerGuarantorDetailListDel = existingGuarantorDetailDAO.findByExistingCreditFacility(existingCreditFacility);
        if(borrowerGuarantorDetailListDel.size()>0){
            for (int i=0 ;i<borrowerGuarantorDetailListDel.size();i++) {
                log.info(" Round relatedComExistingCreditListDel  is " + i );
                ExistingGuarantorDetail existingGuarantorDetail =  borrowerGuarantorDetailListDel.get(i);
                List<ExistingCreditTypeDetail>  existingCreditTypeDetailtDel = existingCreditTypeDetailDAO.findByExistingGuarantorDetail(existingGuarantorDetail);
                existingCreditTypeDetailDAO.delete(existingCreditTypeDetailtDel);
            }
            existingGuarantorDetailDAO.delete(borrowerGuarantorDetailListDel);
        }
        List<ExistingGuarantorDetail> borrowerGuarantorDetailList = existingGuarantorDetailTransform.transformsToModel(existingCreditFacilityView.getBorrowerGuarantorList(), existingCreditFacility, user);
        existingGuarantorDetailDAO.persist(borrowerGuarantorDetailList);
        log.info("persist borrowerGuarantorDetailList...");

        for (int i=0 ;i<borrowerGuarantorDetailList.size();i++) {
            log.info(" Round borrowerGuarantorDetailList  is " + i );
            ExistingGuarantorDetail existingGuarantorDetail =  borrowerGuarantorDetailList.get(i);

            List<ExistingCreditTypeDetail>  existingCreditTypeDetailListDel = existingCreditTypeDetailDAO.findByExistingGuarantorDetail(existingGuarantorDetail);
            if(existingCreditTypeDetailListDel.size()>0){
                existingCreditTypeDetailDAO.delete(existingCreditTypeDetailListDel);
            }

            ExistingGuarantorDetailView existingGuarantorDetailView = existingCreditFacilityView.getBorrowerGuarantorList().get(i);
            List<ExistingCreditTypeDetail> existingCreditTypeDetailList = existingCreditTypeDetailTransform.transformsToModelForGuarantor(existingGuarantorDetailView.getExistingCreditTypeDetailViewList(), existingGuarantorDetail, user);
            existingCreditTypeDetailDAO.persist(existingCreditTypeDetailList);
            log.info("persist related existingCreditTypeDetailList...");
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
            log.info("onFind :: existingConditionDetailList ..." + existingConditionDetailList.size());
            List<ExistingConditionDetailView> existingConditionDetailListView = existingConditionDetailTransform.transformsToView(existingConditionDetailList);
            log.info("onFind :: existingConditionDetailList ...");
            existingCreditFacilityView.setExistingConditionDetailViewList(existingConditionDetailListView);
            
            List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,1);
            log.info("onFind :: borrowerComExistingCreditList ..." + borrowerComExistingCreditList.size());
            List<ExistingCreditDetailView> borrowerComExistingCreditViewList = onFindCreditDetailChild(borrowerComExistingCreditList);
            log.info("onFind :: borrowerComExistingCreditList ...");
            existingCreditFacilityView.setBorrowerComExistingCredit(borrowerComExistingCreditViewList);

            List<ExistingCreditDetail> borrowerRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,2);
            log.info("onFind :: borrowerRetailExistingCreditList ..." + borrowerRetailExistingCreditList.size());
            List<ExistingCreditDetailView> borrowerRetailExistingCreditViewList = onFindCreditDetailChild(borrowerRetailExistingCreditList);
            log.info("onFind :: borrowerRetailExistingCreditList ...");
            existingCreditFacilityView.setBorrowerRetailExistingCredit(borrowerRetailExistingCreditViewList);

            List<ExistingCreditDetail> borrowerAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,1,3);
            log.info("onFind :: borrowerAppInRLOSCreditList ..." + borrowerAppInRLOSCreditList.size());
            List<ExistingCreditDetailView> borrowerAppInRLOSCreditViewList = onFindCreditDetailChild(borrowerAppInRLOSCreditList);
            log.info("onFind :: borrowerAppInRLOSCreditList ...");
            existingCreditFacilityView.setBorrowerAppInRLOSCredit(borrowerAppInRLOSCreditViewList);

            List<ExistingCreditDetail> relatedComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,1);
            log.info("onFind :: relatedComExistingCreditList ..." + relatedComExistingCreditList.size());
            List<ExistingCreditDetailView> relatedComExistingCreditViewList = onFindCreditDetailChild(relatedComExistingCreditList);
            log.info("onFind :: relatedComExistingCreditList ...");
            existingCreditFacilityView.setRelatedComExistingCredit(relatedComExistingCreditViewList);

            List<ExistingCreditDetail> relatedRetailExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,2);
            log.info("onFind :: relatedRetailExistingCreditList ..." + relatedRetailExistingCreditList.size());
            List<ExistingCreditDetailView> relatedRetailExistingCreditViewList = onFindCreditDetailChild(relatedRetailExistingCreditList);
            log.info("onFind :: relatedRetailExistingCreditList ...");
            existingCreditFacilityView.setRelatedRetailExistingCredit(relatedRetailExistingCreditViewList);

            List<ExistingCreditDetail> relatedAppInRLOSCreditList = existingCreditDetailDAO.findByExistingCreditFacility(existingCreditFacility,2,3);
            log.info("onFind :: relatedAppInRLOSCreditList ..." + relatedAppInRLOSCreditList.size());
            List<ExistingCreditDetailView> relatedAppInRLOSCreditViewList = onFindCreditDetailChild(relatedAppInRLOSCreditList);
            log.info("onFind :: relatedAppInRLOSCreditList ...");
            existingCreditFacilityView.setRelatedAppInRLOSCredit(relatedAppInRLOSCreditViewList);

            List<ExistingCollateralDetail> borrowerCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,1);
            log.info("onFind :: borrowerCollateralDetailList ..." + borrowerCollateralDetailList.size());
            List<ExistingCollateralDetailView> borrowerCollateralDetailViewList = onFindCollateralDetailChild(borrowerCollateralDetailList);
            log.info("onFind :: borrowerCollateralDetailViewList ...");
            existingCreditFacilityView.setBorrowerCollateralList(borrowerCollateralDetailViewList);

            List<ExistingCollateralDetail> relatedCollateralDetailList = existingCollateralDetailDAO.findByExistingCreditFacility(existingCreditFacility,2);
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