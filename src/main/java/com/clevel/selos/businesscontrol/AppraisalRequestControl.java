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
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;

    @Inject
    private AppraisalContactDetailTransform appraisalContactDetailTransform;


    private Appraisal appraisal;
    private AppraisalView appraisalView;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private WorkCase workCase;
    private NewCreditFacility newCreditFacility;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;

    private List<NewCollateralView> newCollateralViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;

    @Inject
    public AppraisalRequestControl(){

    }
	
	public AppraisalView getAppraisalRequest(final long workCaseId, final User user){
        log.info("-- getAppraisalRequest WorkCaseId : {}, UserId : {}", workCaseId, user.getId());
        workCase = workCaseDAO.findById(workCaseId);
        log.debug("-- work case : {}", workCase.toString());
        appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
        log.debug("-- Appraisal {}", appraisal.toString());
        return null;
//        if(appraisal == null){
//            appraisalView = appraisalTransform.transformToView(appraisal);
//            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
//            if(newCreditFacility != null){
//                newCollateralList = safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
//                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
//
//            } else {
//                log.debug("-- newCreditFacility = null");
//            }
//            log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
//            return appraisalView;
//        } else {
//            log.debug("-- Find by work case id = {} appraisal is null. ", workCaseId);
//            return appraisalView;
//        }
    }

    public void onSaveAppraisalRequest(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("onSaveAppraisalRequest ");
        Appraisal appraisal;
        List<AppraisalDetailView> appraisalDetailViewList;
        List<AppraisalPurpose> appraisalDetailList;
        List<AppraisalContactDetailView> appraisalContactDetailViewList;
        List<AppraisalContactDetail> appraisalContactDetailList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);
        appraisal.setWorkCase(workCase);

        appraisalDAO.persist(appraisal);
        log.info( "appraisalDAO persist end" );

        appraisalDetailViewList = appraisalView.getAppraisalDetailViewList();

        if(appraisalDetailViewList.size()>0){
            List<AppraisalPurpose>   appraisalDetailListDel = appraisalDetailDAO.findByAppraisal(appraisal);
            appraisalDetailDAO.delete(appraisalDetailListDel);
        }
//        appraisalDetailList = appraisalDetailTransform.transformToModel(appraisalDetailViewList, appraisal);
//        appraisalDetailDAO.persist(appraisalDetailList);
        log.info( "appraisalDetailDAO persist end" );

        appraisalContactDetailList = null;//appraisalContactDetailTransform.transformToModel(appraisalContactDetailViewList, appraisal);
        appraisalContactDetailDAO.persist(appraisalContactDetailList);
        log.info( "appraisalContactDetailDAO persist end" );

    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}