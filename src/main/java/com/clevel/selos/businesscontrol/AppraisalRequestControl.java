package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.AppraisalContactDetailTransform;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.NewCollateralTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
@Stateless
public class AppraisalRequestControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private AppraisalDAO appraisalDAO;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;


    private Appraisal appraisal;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private AppraisalView appraisalView;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private AppraisalContactDetailView appraisalContactDetailView;

    private WorkCase workCase;
    private NewCreditFacility newCreditFacility;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralSub> newCollateralSubList;

    private List<NewCollateralView> newCollateralViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;

    @Inject
    public AppraisalRequestControl(){

    }
	
	public AppraisalView getAppraisalRequest(final long workCaseId, final User user){
        log.info("-- getAppraisalRequest WorkCaseId : {}, UserId : {}", workCaseId, user.getId());
        appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
        if(appraisal != null){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, user);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if(newCreditFacility != null){
                newCollateralList = safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
                for(NewCollateral newCollateral : newCollateralList){
                    newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                }

                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            } else {
                log.debug("-- newCreditFacility = null");
            }
            log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
            return appraisalView;
        } else {
            log.debug("-- Find by work case id = {} appraisal is null. ", workCaseId);
            return appraisalView;
        }
    }

    public void onSaveAppraisalRequest(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("-- onSaveAppraisalRequest");
        workCase = workCaseDAO.findById(workCaseId);
        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);

        if(!Util.isZero(appraisal.getId())){
            log.debug("-- Appraisal id is {}", appraisal.getId());
            appraisalDAO.persist(appraisal);
            appraisalContactDetailList = safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailList);
            appraisalContactDetailList.clear();
            appraisalContactDetailList = safetyList(appraisal.getAppraisalContactDetailList());
            for(AppraisalContactDetail model : appraisalContactDetailList){
                model.setAppraisal(appraisal);
            }
            appraisalContactDetailDAO.persist(appraisalContactDetailList);
        } else {
            log.debug("-- Appraisal id is zero");
            appraisalDAO.persist(appraisal);
            appraisalContactDetailList = safetyList(appraisal.getAppraisalContactDetailList());
            for(AppraisalContactDetail model : appraisalContactDetailList){
                model.setAppraisal(appraisal);
            }
            appraisalContactDetailDAO.persist(appraisalContactDetailList);
        }
        log.info("-- appraisalDAO persist end" );


        newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(newCreditFacility)){
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());
            appraisalDetailViewList = safetyList(appraisalView.getAppraisalDetailViewList());


            newCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralHeadList = safetyList(newCollateralHeadDAO.findByNewCollateralId(newCollateral.getId()));
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setAppraisalRequest(0);
                }
                newCollateralHeadDAO.persist(newCollateralHeadList);
            }

            newCollateralList.clear();
            newCollateralList = safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, user));
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralDAO.persist(newCollateral);
                newCollateralHeadList = safetyList(newCollateral.getNewCollateralHeadList());
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setNewCollateral(newCollateral);
                    newCollateralHeadDAO.persist(newCollateralHead);
                }
            }


        } else {
            log.debug("-- NewCreditFacility is null");
        }
        log.info("-- onSaveAppraisalRequest end");
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}