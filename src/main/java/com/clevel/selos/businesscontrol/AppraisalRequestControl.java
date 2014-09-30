package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestAppraisalValue;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.ProposeLineTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
@Stateless
public class AppraisalRequestControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private AppraisalDAO appraisalDAO;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO newCollateralHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO newCollateralSubDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private ProposeLineTransform proposeLineTransform;


    private Appraisal appraisal;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private AppraisalView appraisalView;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private AppraisalContactDetailView appraisalContactDetailView;

    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;
    private ProposeLine newCreditFacility;

    private List<ProposeCollateralInfo> newCollateralList;
    private List<ProposeCollateralInfoHead> newCollateralHeadList;

    @Inject
    public AppraisalRequestControl(){

    }

    private void init(){
        log.debug("-- init()");
        appraisalView = null;
    }

    public String getZoneLocation(String bdmId){
        log.debug("-- getZoneLocation()");
        User user = userDAO.findUserByID(bdmId);
        String zoneTeam = "";
        try {
            if (!Util.isNull(user.getTeam())){
                zoneTeam = user.getTeam().getTeam_name();
            }
            return zoneTeam;
        } catch (Exception e){
            return "";
        }
    }
	
	public AppraisalView getAppraisalRequest(long workCaseId, long workCasePreScreenId, ProposeType proposeType){
        log.info("-- getAppraisalRequest WorkCaseId : {}, workCasePreScreenId : {}, User.id[{}]", workCaseId, workCasePreScreenId, getCurrentUserID());
        init();
        String bdmUserId = "";

        //Find for Appraisal to show on screen
        if(!Util.isZero(workCaseId)){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            workCase = workCaseDAO.findById(workCaseId);
            bdmUserId = workCase.getCreateBy() != null ? workCase.getCreateBy().getId() : "";
        } else if(!Util.isZero(workCasePreScreenId)){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            bdmUserId = workCasePrescreen.getCreateBy() != null ? workCasePrescreen.getCreateBy().getId() : "";
        }
        log.debug("getAppraisalRequest appraisal : {}", appraisal);

        if(!Util.isNull(appraisal)) {
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
        }else{
            appraisalView = new AppraisalView();
        }

        appraisalView.setZoneLocation(getZoneLocation(bdmUserId));

        if(!Util.isZero(workCaseId)){
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        } else if(!Util.isZero(workCasePreScreenId)){
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }

        log.debug("getAppraisalRequest ::: newCreditFacility : {}", newCreditFacility);

        if(!Util.isNull(newCreditFacility)){
            newCollateralList = safetyList(newCollateralDAO.findCollateralForAppraisalRequest(newCreditFacility, proposeType));
            log.debug("getAppraisalRequest ::: newCollateralList : {}", newCollateralList);
            appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
            appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
            return appraisalView;
        } else {
            log.debug("-- NewCreditFacility = null");
            return appraisalView;
        }

    }

    public void onSaveAppraisalRequest(AppraisalView appraisalView,long workCaseId, long workCasePreScreenId, long statusId){
        log.info("-- onSaveAppraisalRequest ::: workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
        User currentUser = getCurrentUser();
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            workCasePrescreen = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            /*if(workCase != null){
                workCase.setRequestAppraisal(1);
                workCaseDAO.persist(workCase);
            }*/
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCase = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }

        //remove all collateral head from list in database
        ProposeType proposeType;
        if(statusId != StatusValue.REQUEST_CORRECT_DOC_INFO_UW2.value()){
            proposeType = ProposeType.P;
        }else{
            proposeType = ProposeType.A;
        }

        log.debug("onSaveAppraisalRequest ::: workCase : {}, workCasePrescreen : {}", workCase, workCasePrescreen);
        log.debug("onSaveAppraisalRequest ::: newCreditFacility : {}", newCreditFacility);

        if(!Util.isNull(workCase) || !Util.isNull(workCasePrescreen)){
            log.debug("onSaveAppraisalRequest ::: workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
            appraisal = appraisalTransform.transformToModel(appraisalView, workCase, workCasePrescreen, currentUser);

            //remove all appraisalContactDetailList
            List<AppraisalContactDetail> appraisalContactDetailDeleteList = safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailDeleteList);

            log.debug("onSaveAppraisalRequest ::: before persist appraisal : {}", appraisal);
            appraisalDAO.persist(appraisal);
            log.debug("onSaveAppraisalRequest ::: after persist appraisal : {}", appraisal);

            //check newCreditFacility is exist? else create new one
            if(Util.isNull(newCreditFacility)){
                newCreditFacility = new ProposeLine();
                newCreditFacility.setWorkCasePrescreen(workCasePrescreen);
                newCreditFacility.setWorkCase(workCase);
            }
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());

            appraisalDetailViewList = safetyList(appraisalView.getAppraisalDetailViewList());

            //find all collateral in credit facility
            if(newCreditFacility.getId() != 0){
                newCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            }else{
                newCollateralList = new ArrayList<ProposeCollateralInfo>();
            }

            //set flag 0 for all collateral
            log.debug("onSaveAppraisalRequest ::: newCollateralList from database : {}", newCollateralList);
            for(ProposeCollateralInfo newCollateral : newCollateralList){
                newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
                for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
                }
                newCollateral.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
                newCollateralDAO.persist(newCollateral);
            }

            //transform collateral head from view
            newCollateralList.clear();
            newCollateralList = safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, currentUser, RequestAppraisalValue.READY_FOR_REQUEST, proposeType));
            /*log.debug("onSaveAppraisalRequest ::: before persist newCreditfacility : {}", newCreditFacility);
            newCreditFacilityDAO.persist(newCreditFacility);
            log.debug("onSaveAppraisalRequest ::: after persist newCreditfacility : {}", newCreditFacility);*/

            log.debug("onSaveAppraisalRequest ::: before persist newCollateralList : {}", newCollateralList);
            newCollateralDAO.persist(newCollateralList);
            log.debug("onSaveAppraisalRequest ::: after persist newCollateralList : {}", newCollateralList);
            log.info("-- onSaveAppraisalRequest end");
        }
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}