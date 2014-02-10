package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.transform.AppraisalContactDetailTransform;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.ContactRecordDetailTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AppraisalAppointmentControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private AppraisalDAO appraisalDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private ContactRecordDetailDAO contactRecordDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private AppraisalContactDetailTransform appraisalContactDetailTransform;
    @Inject
    private ContactRecordDetailTransform contactRecordDetailTransform;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;

    private Appraisal appraisal;
    private AppraisalView appraisalView;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private List<ContactRecordDetail> contactRecordDetailList;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private WorkCase workCase;
    private NewCreditFacility newCreditFacility;

    @Inject
    public AppraisalAppointmentControl(){

    }
	
	public AppraisalView getAppraisalAppointment(final long workCaseId, final User user){
        log.info("-- getAppraisalAppointment WorkCaseId : {}, User.id[{}]", workCaseId, user.getId());
        appraisal  = appraisalDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(appraisal)){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, user);

            //TODO : wrk_contact_record waiting for .....................
//            contactRecordDetailList = Util.safetyList(contactRecordDetailDAO.findByWorkCaseId(workCaseId));
//            if(!Util.isZero(contactRecordDetailList.size())){
//                contactRecordDetailViewList = contactRecordDetailTransform.transformToView(contactRecordDetailList);
//                appraisalView.setContactRecordDetailViewList(Util.safetyList(contactRecordDetailViewList));
//            }

            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if(!Util.isNull(newCreditFacility)){
                newCollateralList = Util.safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
                for(NewCollateral newCollateral : newCollateralList){
                    newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                }
                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
                log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
                return appraisalView;
            } else {
                log.debug("-- NewCreditFacility = null");
                return appraisalView;
            }
        } else {
            log.debug("-- Find by work case id = {} appraisal is null. ", workCaseId);
            return appraisalView;
        }
    }

    public void onSaveAppraisalAppointment(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("-- onSaveAppraisalAppointment");
        workCase = workCaseDAO.findById(workCaseId);
        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);

        if(!Util.isZero(appraisal.getId())){
            log.debug("-- Appraisal id is {}", appraisal.getId());
            appraisalDAO.persist(appraisal);
        } else {
            log.debug("-- Appraisal id is zero");
            appraisalDAO.persist(appraisal);
            appraisalContactDetailList = Util.safetyList(appraisal.getAppraisalContactDetailList());
            for(AppraisalContactDetail model : appraisalContactDetailList){
                model.setAppraisal(appraisal);
            }
            appraisalContactDetailDAO.persist(appraisalContactDetailList);
        }
        log.info("-- appraisalDAO persist end" );

        newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(newCreditFacility)){
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());
            appraisalDetailViewList = Util.safetyList(appraisalView.getAppraisalDetailViewList());


            newCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralHeadList = Util.safetyList(newCollateralHeadDAO.findByNewCollateralId(newCollateral.getId()));
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setAppraisalRequest(0);
                }
                newCollateralHeadDAO.persist(newCollateralHeadList);
            }

            newCollateralList.clear();
            newCollateralList = Util.safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, user));
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralDAO.persist(newCollateral);
                newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setNewCollateral(newCollateral);
                    newCollateralHeadDAO.persist(newCollateralHead);
                }
            }
        } else {
            log.debug("-- NewCreditFacility is null");
        }
        log.info("-- onSaveAppraisalAppointment end");
    }
}