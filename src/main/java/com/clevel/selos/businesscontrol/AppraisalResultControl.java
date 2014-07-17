package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.ProposeLineTransform;
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
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ProposeCollateralInfoDAO proposeCollateralInfoDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO proposeCollateralInfoHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO proposeCollateralInfoSubDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO proposeCollateralInfoRelationDAO;

    @Inject
    private ProposeCollateralSubMortgageDAO proposeCollateralSubMortgageDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO proposeCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO proposeCollateralSubRelatedDAO;

    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private ProposeLineTransform proposeLineTransform;

    @Inject
    private COMSInterface comsInterface;

    private Appraisal appraisal;
    private AppraisalView appraisalView;

    private ProposeLine newCreditFacility;
    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;

    private List<ProposeCollateralInfo> newCollateralList;
    private List<ProposeCollateralInfoHead> newCollateralHeadList;
    private List<ProposeCollateralInfoSub> newCollateralSubList;

    private List<ProposeCollateralInfoView> newCollateralViewList;

    @Inject
    public AppraisalResultControl(){

    }

    public AppraisalView getAppraisalResult(final long workCaseId, final long workCasePreScreenId){
        log.info("-- getAppraisalResult workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
            workCase = newCreditFacility.getWorkCase();
            log.debug("-- getAppraisalResult ::: findByWorkCaseId :{}", workCaseId);
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            newCreditFacility = proposeLineDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            workCasePrescreen = newCreditFacility.getWorkCasePrescreen();
            log.debug("-- getAppraisalResult ::: findByWorkCasePreScreenId :{}", workCasePreScreenId);
        }

        appraisalView = new AppraisalView();

        if(!Util.isNull(appraisal)){
            newCollateralViewList = new ArrayList<ProposeCollateralInfoView>();
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
            if(!Util.isNull(newCreditFacility)){
                List<ProposeCollateralInfo> newCollateralListTypeP = null;
                List<ProposeCollateralInfo> newCollateralListTypeA2 = null;

                newCollateralListTypeP = Util.safetyList(proposeCollateralInfoDAO.findNewCollateralByTypeP(newCreditFacility));//normal query
                newCollateralListTypeA2 = Util.safetyList(proposeCollateralInfoDAO.findNewCollateralByTypeA2(newCreditFacility));

                newCollateralList = new ArrayList<ProposeCollateralInfo>();
                if(!Util.isZero(newCollateralListTypeP.size())){
                    newCollateralList.addAll(newCollateralListTypeP);
                }
                if(!Util.isZero(newCollateralListTypeA2.size())){
                    newCollateralList.addAll(newCollateralListTypeA2);
                }

                List<ProposeCollateralInfo> tempNewCollateralList = new ArrayList<ProposeCollateralInfo>();
                for(ProposeCollateralInfo newCollateral : newCollateralList){
                    newCollateral.setProposeCollateralInfoHeadList(proposeCollateralInfoHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                    tempNewCollateralList.add(newCollateral);
                }
                newCollateralViewList = proposeLineTransform.transformProposeCollateralToViewList(tempNewCollateralList, null);
                appraisalView.setNewCollateralViewList(newCollateralViewList);
            } else {
                log.debug("-- NewCreditFacility = null");
            }
            log.info("-- getAppraisalResult ::: AppraisalView : {}", appraisalView.toString());
        }
        return appraisalView;
    }










    public void onSaveAppraisalResult(AppraisalView appraisalView, long workCaseId, long workCasePreScreenId) {
        log.debug("onSaveAppraisalResult ::: appraisalView ::: {} , workCaseId ::: {} , workCasePreScreenId ::: {}", appraisalView, workCaseId, workCasePreScreenId);
        User currentUser = getCurrentUser();

        if(!Util.isNull(appraisalView) && Util.isSafetyList(appraisalView.getRemoveCollListId())){
            for(Long proposeCollateralInfoId : appraisalView.getRemoveCollListId()){
                final ProposeCollateralInfo proposeCollateralInfo = proposeCollateralInfoDAO.findById(proposeCollateralInfoId);
                if(!Util.isNull(proposeCollateralInfo)){
                    clearProposeCollateralInfo(proposeCollateralInfo);
                    proposeCollateralInfoDAO.delete(proposeCollateralInfo);
                }
            }
        }

        if(!Util.isNull(appraisalView) && Util.isSafetyList(appraisalView.getNewCollateralViewList())){
            List<ProposeCollateralInfoView> newCollateralViewList = appraisalView.getNewCollateralViewList();
            insertToDB(newCollateralViewList, currentUser);
        }
    }

    private void insertToDB(final List<ProposeCollateralInfoView> newCollateralViewList, final User user){
        log.debug("-- insertIntoDB ::: newCollateralViewList ::: {} ", newCollateralViewList);
        List<ProposeCollateralInfo> newCollateralList = new ArrayList<ProposeCollateralInfo>();
        for(ProposeCollateralInfoView proposeCollateralInfoView : newCollateralViewList) {
            newCollateralList.add(proposeLineTransform.transformProposeCollateralToModel(workCase, newCreditFacility, proposeCollateralInfoView, user, ProposeType.A));
        }

        for (final ProposeCollateralInfo newCollateral : newCollateralList) {
            log.debug("-- NewCollateral[{}]", newCollateral);
            if(!proposeCollateralInfoDAO.isExist(newCollateral.getId())){
                log.debug("-- Insert into new record of NewCollateral");
                log.debug("-- processing one step...");
                insertProposeCollateralInfo(newCollateral);
                log.debug("-- id[{}] saved", newCollateral.getId());
            } else {
                log.debug("-- Update to exist record of NewCollateral.id[{}]", newCollateral.getId());
                log.debug("-- processing first step...");
                ProposeCollateralInfo model = proposeCollateralInfoDAO.findById(newCollateral.getId());
                log.debug("-- Model[{}]", model.toString());
                log.debug("-- processing second step...");
                insertProposeCollateralInfo(model);
                log.debug("-- id[{}] updated", model.getId());
            }
        }

//        for(final ProposeCollateralInfo newCollateral : newCollateralList){
//            List<ProposeCollateralInfoHead> newCollateralHeadList = Util.safetyList(newCollateral.getProposeCollateralInfoHeadList());
//            for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
//                newCollateralHead.setProposeCollateral(newCollateral);
//                newCollateralHead.setProposeType(ProposeType.P);
//                newCollateralHead.setAppraisalRequest(2);
//                List<ProposeCollateralInfoSub> newCollateralSubList = Util.safetyList(newCollateralHead.getProposeCollateralInfoSubList());
//                for(ProposeCollateralInfoSub newCollateralSub : newCollateralSubList){
//                    newCollateralSub.setProposeCollateralHead(newCollateralHead);
//                }
//                if(!Util.isZero(newCollateralSubList.size())){
//                    log.debug("-- persist newCollateralSubList.size()[{}]", newCollateralSubList.size());
//                    proposeCollateralInfoSubDAO.persist(newCollateralSubList);
//                    log.debug("-- newCollateralSubList[{}]", newCollateralSubList);
//                }
//            }
//            if(!Util.isZero(newCollateralHeadList.size())){
//                log.debug("-- persist newCollateralHeadList.size()[{}]", newCollateralHeadList.size());
//                proposeCollateralInfoHeadDAO.persist(newCollateralHeadList);
//                log.debug("-- newCollateralHeadList[{}]", newCollateralHeadList);
//            }
//        }
    }


    private void insertProposeCollateralInfo(final ProposeCollateralInfo proposeCollateralInfo){
        proposeCollateralInfoDAO.persistAR2PTA(proposeCollateralInfo);
        insertProposeCollateralInfoHead(proposeCollateralInfo);
    }
    private void insertProposeCollateralInfoHead(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("-- insertProposeCollateralInfoHead()");
        log.debug("-- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = proposeCollateralInfo.getProposeCollateralInfoHeadList();
        if(Util.isSafetyList(proposeCollateralInfoHeadList)){
            log.debug("-- ProposeCollateralInfoHeadList.size()[{}]", proposeCollateralInfoHeadList.size());
            for(final ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfoHeadList){
                proposeCollateralInfoHead.setProposeCollateral(proposeCollateralInfo);
                proposeCollateralInfoHeadDAO.persistAR2PTA(proposeCollateralInfoHead);
                insertProposeCollateralInfoSub(proposeCollateralInfoHead);
            }
        }
    }
    private void insertProposeCollateralInfoSub(final ProposeCollateralInfoHead proposeCollateralInfoHead){
        log.debug("-- insertProposeCollateralInfoSub()");
        log.debug("-- ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
        final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoHead.getProposeCollateralInfoSubList();
        if(Util.isSafetyList(proposeCollateralInfoSubList)){
            log.debug("-- ProposeCollateralInfoSubList.size()[{}]", proposeCollateralInfoSubList.size());
            for(final ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoSubList){
                proposeCollateralInfoSub.setProposeCollateralHead(proposeCollateralInfoHead);
                proposeCollateralInfoSubDAO.persist(proposeCollateralInfoSub);
            }
        }
    }


    private void clearAllOfProposeCollateralInfo(final ProposeLine proposeLine){
        log.debug("-- clearAllOfProposeCollateralInfo()");
        if(!Util.isNull(proposeLine)){
            log.debug("-- ProposeLine.id[{}]", proposeLine.getId());
            final List<ProposeCollateralInfo> proposeCollateralInfoList = proposeCollateralInfoDAO.findNewCollateralByProposeLineId(proposeLine.getId());
            if(Util.isSafetyList(proposeCollateralInfoList)){
                log.debug("-- ProposeCollateralInfoList.size()[{}]", proposeCollateralInfoList.size());
                for(final ProposeCollateralInfo proposeCollateralInfo : proposeCollateralInfoList){
                    clearProposeCollateralInfo(proposeCollateralInfo);
                }
                proposeCollateralInfoDAO.delete(proposeCollateralInfoList);
            }
        }
    }
    private void clearProposeCollateralInfo(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("-- clearProposeCollateralInfo()");
        clearProposeCollateralInfoHead(proposeCollateralInfo);
        clearProposeCollateralInfoRelation(proposeCollateralInfo);
    }
    private void clearProposeCollateralInfoHead(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("-- clearProposeCollateralInfoHead()");
        log.debug("-- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = proposeCollateralInfoHeadDAO.findByNewCollateralId(proposeCollateralInfo.getId());
        if(Util.isSafetyList(proposeCollateralInfoHeadList)){
            log.debug("-- ProposeCollateralInfoHeadList.size()[{}]", proposeCollateralInfoHeadList.size());
            for(final ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfoHeadList){
                clearProposeCollateralInfoSub(proposeCollateralInfoHead);
            }
            proposeCollateralInfoHeadDAO.delete(proposeCollateralInfoHeadList);
        }
    }
    private void clearProposeCollateralInfoSub(final ProposeCollateralInfoHead proposeCollateralInfoHead){
        log.debug("-- clearProposeCollateralInfoSub()");
        log.debug("-- ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
        final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoSubDAO.findByNewCollateralHeadId(proposeCollateralInfoHead.getId());
        if(Util.isSafetyList(proposeCollateralInfoSubList)){
            log.debug("-- ProposeCollateralInfoSubList.size()[{}]", proposeCollateralInfoSubList.size());
            proposeCollateralInfoSubDAO.delete(proposeCollateralInfoSubList);
        }
    }
    private void clearProposeCollateralInfoRelation(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("-- clearProposeCollateralInfoRelation()");
        log.debug("-- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        final List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = proposeCollateralInfoRelationDAO.findByNewCollateralId(proposeCollateralInfo.getId());
        if(Util.isSafetyList(proposeCollateralInfoRelationList)){
            log.debug("-- ProposeCollateralInfoRelationList.size()[{}]", proposeCollateralInfoRelationList.size());
            proposeCollateralInfoRelationDAO.delete(proposeCollateralInfoRelationList);
        }
    }


    public AppraisalDataResult retrieveDataFromCOMS(final String jobID) throws COMSInterfaceException {
        log.debug("-- retrieveDataFromCOMS ::: jobID : {}", jobID);
        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobID);
        return appraisalDataResult;
    }
}
