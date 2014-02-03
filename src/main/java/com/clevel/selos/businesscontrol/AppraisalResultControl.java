package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.model.view.NewCollateralSubView;
import com.clevel.selos.model.view.NewCollateralView;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.NewCollateralHeadTransform;
import com.clevel.selos.transform.NewCollateralSubTransform;
import com.clevel.selos.transform.NewCollateralTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AppraisalResultControl extends BusinessControl {
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
    private AppraisalTransform appraisalTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralSubTransform newCollateralSubTransform;

    private Appraisal appraisal;
    private AppraisalView appraisalView;

    private WorkCase workCase;
    private NewCreditFacility newCreditFacility;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralSub> newCollateralSubList;

    private List<NewCollateralView> newCollateralViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    private List<NewCollateralSubView> newCollateralSubViewList;

    @Inject
    public AppraisalResultControl(){

    }

    public AppraisalView getAppraisalResult(final long workCaseId, final User user){
        log.info("-- getAppraisalResult WorkCaseId : {}, UserId : {}", workCaseId, user.getId());
        appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
        AppraisalView appraisalView = null;

        newCollateralViewList = new ArrayList<NewCollateralView>();
        if(appraisal != null){
            appraisalView = appraisalTransform.transformToView(appraisal);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if(newCreditFacility != null){
                newCollateralList = safetyList(newCollateralDAO.findNewCollateralByTypeP(newCreditFacility));
                newCollateralViewList = newCollateralTransform.transformToView(newCollateralList);
                appraisalView.setNewCollateralViewList(newCollateralViewList);
            } else {
                log.debug("-- newCreditFacility = null");
            }
            log.info("-- getAppraisalResult ::: AppraisalView : {}", appraisalView.toString());
            return appraisalView;
        } else {
            log.debug("-- When find by work case id = {}, Appraisal is null, ", workCaseId);
            return appraisalView;
        }
    }

    public void onSaveAppraisalResult(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("-- onSaveAppraisalResult begin");
        workCase = workCaseDAO.findById(workCaseId);

        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);
        appraisalDAO.persist(appraisal);
        log.info( "appraisalDAO persist end" );

        newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);

        newCollateralList = safetyList(newCollateralDAO.findNewCollateralByTypeA(newCreditFacility));

        if(newCollateralList.size() > 0){
            clearDB(newCollateralList);
            newCollateralDAO.delete(newCollateralList);
            newCollateralViewList = safetyList(appraisalView.getNewCollateralViewList());
            insertToDB(newCollateralViewList, user);
            updateWRKNewColl(newCollateralViewList, user);
        } else {
            newCollateralDAO.delete(newCollateralList);
            newCollateralViewList = safetyList(appraisalView.getNewCollateralViewList());
            insertToDB(newCollateralViewList, user);
            updateWRKNewColl(newCollateralViewList, user);
        }

        log.info("onSaveAppraisalResult end");
    }

    private void updateWRKNewColl(final List<NewCollateralView> newCollateralViewList, final User user){
        newCollateralList = safetyList(newCollateralTransform.transformToModel(newCollateralViewList, user, newCreditFacility));
        newCollateralDAO.updateAppraisalRequest(newCollateralList);
    }

    private void insertToDB(final List<NewCollateralView> newCollateralViewList, final User user){
        newCollateralList = safetyList(newCollateralTransform.transformToNewModel(newCollateralViewList, user, newCreditFacility));
        newCollateralDAO.persistProposeTypeA(newCollateralList);
        for(NewCollateral newCollateral : newCollateralList){
            newCollateralHeadList = safetyList(newCollateral.getNewCollateralHeadList());
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                newCollateralHead.setNewCollateral(newCollateral);
                newCollateralSubList = safetyList(newCollateralHead.getNewCollateralSubList());
                for(NewCollateralSub newCollateralSub : newCollateralSubList){
                    newCollateralSub.setNewCollateralHead(newCollateralHead);
                }
                newCollateralSubDAO.persist(newCollateralSubList);
            }
            newCollateralHeadDAO.persist(newCollateralHeadList);
        }
    }
    private void clearDB(final List<NewCollateral> newCollateralList){
        long id;
        for(NewCollateral newCollateral : newCollateralList){
            id = newCollateral.getId();
            newCollateralHeadList = safetyList(newCollateralHeadDAO.findByNewCollateralId(id));
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                id = newCollateralHead.getId();
                newCollateralSubList = safetyList(newCollateralSubDAO.findByNewCollateralHeadId(id));
                newCollateralSubDAO.delete(newCollateralSubList);
            }
            newCollateralHeadDAO.delete(newCollateralHeadList);
        }
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}
