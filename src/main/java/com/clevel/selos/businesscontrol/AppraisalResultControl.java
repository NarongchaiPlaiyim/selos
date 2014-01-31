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
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralSubTransform newCollateralSubTransform;

    private Appraisal appraisal;

    private WorkCase workCase;
    private User user;
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
            newCollateralList = safetyList(newCollateralDAO.findNewCollateralByTypeP(newCreditFacility));
            newCollateralViewList = newCollateralTransform.transformToView(newCollateralList);
            appraisalView.setNewCollateralViewList(newCollateralViewList);
            log.info("-- getAppraisalResult ::: AppraisalView : {}", appraisalView.toString());
            return appraisalView;
        } else {
            log.debug("-- Appraisal = null find by work case id = {}", workCaseId);
            appraisalView = new AppraisalView();
            return appraisalView;
        }
    }

    public void onSaveAppraisalResult(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("-- onSaveAppraisalResult begin");
        workCase = workCaseDAO.findById(workCaseId);

//        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);
//        appraisalDAO.persist(appraisal);
//        log.info( "appraisalDAO persist end" );


        newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        newCollateralViewList = safetyList(appraisalView.getNewCollateralViewList());

        newCollateralList = safetyList(newCollateralDAO.findNewCollateralByTypeA(newCreditFacility));

        if(newCollateralList.size() > 0){
            clearDB(newCollateralList);
        } else {

        }

        log.info("onSaveAppraisalResult end");
    }

    private void insertToDB(){

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
        newCollateralDAO.delete(newCollateralList);
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}
