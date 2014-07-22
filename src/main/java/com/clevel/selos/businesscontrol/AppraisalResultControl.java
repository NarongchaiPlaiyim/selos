package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.transform.AppraisalTransform;
import com.clevel.selos.transform.ProposeLineTransform;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
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
    private WorkCasePrescreenDAO workCasePrescreenDAO;
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

    private List<ProposeCollateralInfo> newCollateralList;

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
            log.debug("-- getAppraisalResult ::: findByWorkCasePreScreenId :{}", workCasePreScreenId);
        }

        appraisalView = new AppraisalView();

        if(!Util.isNull(appraisal)){
            newCollateralViewList = new ArrayList<ProposeCollateralInfoView>();
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
            if(!Util.isNull(newCreditFacility)){
                List<ProposeCollateralInfo> newCollateralListTypeP = null;
                List<ProposeCollateralInfo> newCollateralListTypeA2 = null;

                newCollateralListTypeP = proposeCollateralInfoDAO.findNewCollateralByTypeP(newCreditFacility);//normal query
                newCollateralListTypeA2 = proposeCollateralInfoDAO.findNewCollateralByTypeA2(newCreditFacility);

                newCollateralList = new ArrayList<ProposeCollateralInfo>();
                if(Util.isSafetyList(newCollateralListTypeP)){
                    newCollateralList.addAll(newCollateralListTypeP);
                }
                if(Util.isSafetyList(newCollateralListTypeA2)){
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

        List<ProposeCollateralInfo> proposeCollateralInfoList = null;
        if(!Util.isNull(appraisalView) && Util.isSafetyList(appraisalView.getRemoveCollListId())){
            proposeCollateralInfoList = new ArrayList<ProposeCollateralInfo>();
            log.debug("--[NEW] ProposeCollateralInfoList");
            for(final Long proposeCollateralInfoId : appraisalView.getRemoveCollListId()){
                final ProposeCollateralInfo proposeCollateralInfo = proposeCollateralInfoDAO.findById(proposeCollateralInfoId);
                if(!Util.isNull(proposeCollateralInfo)){
                    proposeCollateralInfoList.add(proposeCollateralInfo);
                }
            }

            log.debug("-- ProposeCollateralInfoList().size()[{}]", proposeCollateralInfoList.size());
            if(Util.isSafetyList(proposeCollateralInfoList)){
                for(final ProposeCollateralInfo proposeCollateralInfo : proposeCollateralInfoList){
                    log.debug("-- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
                    final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = proposeCollateralInfo.getProposeCollateralInfoHeadList();
                    if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                        for(final ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfoHeadList){
                            log.debug("---- ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
                            final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoHead.getProposeCollateralInfoSubList();
                            if(Util.isSafetyList(proposeCollateralInfoSubList)){
                                for(final ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoSubList){
                                    log.debug("------ ProposeCollateralInfoSub.id[{}]", proposeCollateralInfoSub.getId());
                                    final List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = proposeCollateralSubRelatedDAO.findByMainCollSubId(proposeCollateralInfoSub.getId());
                                    for(final ProposeCollateralSubRelated proposeCollateralSubRelated : proposeCollateralSubRelatedList){
                                        proposeCollateralSubRelatedDAO.delete(proposeCollateralSubRelated);
                                        log.debug("-------- ProposeCollateralSubRelated.id[{}] was deleted", proposeCollateralSubRelated.getId());
                                    }
                                }
                            }
                        }
                    }
                    proposeCollateralInfoDAO.delete(proposeCollateralInfo);
                    log.debug("-- ProposeCollateralInfo.id[{}] was deleted", proposeCollateralInfo.getId());
                }
            }
        }

        if(!Util.isNull(appraisalView) && Util.isSafetyList(appraisalView.getNewCollateralViewList())){
            final List<ProposeCollateralInfoView> proposeCollateralInfoViewList = appraisalView.getNewCollateralViewList();
            if(Util.isSafetyList(proposeCollateralInfoViewList)){
                log.debug("-- ProposeCollateralInfoViewList().size()[{}]", proposeCollateralInfoViewList.size());
                insertToDB(proposeCollateralInfoViewList, currentUser);
            }
        }

        log.debug("-- done.");
    }


    private void insertToDB(final List<ProposeCollateralInfoView> newCollateralViewList, final User user){
        log.debug("-- insertIntoDB(ProposeCollateralInfoViewList.size()[{}])", newCollateralViewList.size());
        final List<ProposeCollateralInfo> proposeCollateralInfoList = new ArrayList<ProposeCollateralInfo>();
        log.debug("-- User.id[{}]", user.getId());
        log.debug("-- WorkCase.id[{}]", workCase.getId());
        log.debug("-- ProposeLine.id[{}]", newCreditFacility.getId());
        for(final ProposeCollateralInfoView proposeCollateralInfoView : newCollateralViewList) {
            proposeCollateralInfoList.add(proposeLineTransform.transformProposeCollateralToModel(workCase, newCreditFacility, proposeCollateralInfoView, user, ProposeType.A));
        }

        for (final ProposeCollateralInfo proposeCollateralInfo : proposeCollateralInfoList) {
            log.debug("-- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
            if(!proposeCollateralInfoDAO.isExist(proposeCollateralInfo.getId())){
                log.debug("-- Insert into new record of NewCollateral");
                log.debug("-- processing one step...");
                proposeCollateralInfoDAO.persistAR2PTA(proposeCollateralInfo);
                log.debug("-- id[{}] saved", proposeCollateralInfo.getId());
            } else {
                log.debug("-- Update to exist record of NewCollateral.id[{}]", proposeCollateralInfo.getId());
                log.debug("-- processing first step...");
//                ProposeCollateralInfo model = proposeCollateralInfoDAO.findById(proposeCollateralInfo.getId());

//                model.setProposeCollateralInfoHeadList(null);
                log.debug("-- Model[{}]", proposeCollateralInfo.toString());
                log.debug("-- processing second step...");
                proposeCollateralInfoDAO.persistAR2PTA(proposeCollateralInfo);
                log.debug("-- id[{}] updated", proposeCollateralInfo.getId());
            }
        }

        for (final ProposeCollateralInfo proposeCollateralInfo : proposeCollateralInfoList) {
            final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = proposeCollateralInfo.getProposeCollateralInfoHeadList();
            if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                for(final ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfoHeadList){
                    proposeCollateralInfoHead.setProposeCollateral(proposeCollateralInfo);
                    final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoHead.getProposeCollateralInfoSubList();
                    if(Util.isSafetyList(proposeCollateralInfoSubList)){
                        for(final ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoSubList){
                            proposeCollateralInfoSub.setProposeCollateralHead(proposeCollateralInfoHead);
                            proposeCollateralInfoSubDAO.persist(proposeCollateralInfoSub);
                        }
                    }
                    proposeCollateralInfoHeadDAO.persistAR2PTA(proposeCollateralInfoHead);
                }
            }
        }
    }

    private void insertProposeCollateralInfo(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("--[BEFORE] ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        proposeCollateralInfoDAO.persistAR2PTA(proposeCollateralInfo);
        log.debug("--[AFTER] ProposeCollateralInfo.id[{}] was inserted", proposeCollateralInfo.getId());
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
                log.debug("----[BEFORE] ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
                proposeCollateralInfoHeadDAO.persistAR2PTA(proposeCollateralInfoHead);
                log.debug("----[AFTER] ProposeCollateralInfoHead.id[{}] was inserted", proposeCollateralInfoHead.getId());
                insertProposeCollateralInfoSub(proposeCollateralInfoHead);
            }
        }
    }
    private void insertProposeCollateralInfoSub(final ProposeCollateralInfoHead proposeCollateralInfoHead){
        log.debug("------ insertProposeCollateralInfoSub()");
        log.debug("------ ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
        final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoHead.getProposeCollateralInfoSubList();
        if(Util.isSafetyList(proposeCollateralInfoSubList)){
            log.debug("------ ProposeCollateralInfoSubList.size()[{}]", proposeCollateralInfoSubList.size());
            for(final ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoSubList){
                proposeCollateralInfoSub.setProposeCollateralHead(proposeCollateralInfoHead);
                log.debug("------[BEFORE] ProposeCollateralInfoSub.id[{}]", proposeCollateralInfoSub.getId());
                proposeCollateralInfoSubDAO.persist(proposeCollateralInfoSub);
                log.debug("------[AFTER] ProposeCollateralInfoSub.id[{}] was inserted", proposeCollateralInfoSub.getId());
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
//        clearProposeCollateralInfoRelation(proposeCollateralInfo);
//        clearProposeCollateralInfoHead(proposeCollateralInfo);
        proposeCollateralInfoDAO.delete(proposeCollateralInfo);
        log.debug("-- ProposeCollateralInfo.id[{}] was deleted", proposeCollateralInfo.getId());
    }
    private void clearProposeCollateralInfoHead(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("---- clearProposeCollateralInfoHead()");
        log.debug("---- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = proposeCollateralInfoHeadDAO.findByNewCollateralId(proposeCollateralInfo.getId());
        if(Util.isSafetyList(proposeCollateralInfoHeadList)){
            log.debug("---- ProposeCollateralInfoHeadList.size()[{}]", proposeCollateralInfoHeadList.size());
            for(final ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfoHeadList){
                clearProposeCollateralInfoSub(proposeCollateralInfoHead);
                proposeCollateralInfoHeadDAO.delete(proposeCollateralInfoHead);
                log.debug("---- ProposeCollateralInfoHead.id[{}] was deleted", proposeCollateralInfoHead.getId());
            }
        }
    }
    private void clearProposeCollateralInfoSub(final ProposeCollateralInfoHead proposeCollateralInfoHead){
        log.debug("------ clearProposeCollateralInfoSub()");
        log.debug("------ ProposeCollateralInfoHead.id[{}]", proposeCollateralInfoHead.getId());
        final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = proposeCollateralInfoSubDAO.findByNewCollateralHeadId(proposeCollateralInfoHead.getId());
        if(Util.isSafetyList(proposeCollateralInfoSubList)){
            log.debug("------ ProposeCollateralInfoSubList.size()[{}]", proposeCollateralInfoSubList.size());
            for(final ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoSubList){
                proposeCollateralInfoSubDAO.delete(proposeCollateralInfoSub);
                log.debug("------ ProposeCollateralInfoSub.id[{}] was deleted", proposeCollateralInfoSub.getId());
            }
        }
    }
    private void clearProposeCollateralInfoRelation(final ProposeCollateralInfo proposeCollateralInfo){
        log.debug("---- clearProposeCollateralInfoRelation()");
        log.debug("---- ProposeCollateralInfo.id[{}]", proposeCollateralInfo.getId());
        final List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = proposeCollateralInfoRelationDAO.findByNewCollateralId(proposeCollateralInfo.getId());
        if(Util.isSafetyList(proposeCollateralInfoRelationList)){
            log.debug("---- ProposeCollateralInfoRelationList.size()[{}]", proposeCollateralInfoRelationList.size());
            for(final ProposeCollateralInfoRelation proposeCollateralInfoRelation : proposeCollateralInfoRelationList){
                proposeCollateralInfoRelationDAO.delete(proposeCollateralInfoRelation);
                log.debug("---- ProposeCollateralInfoRelation.id[{}] was deleted", proposeCollateralInfoRelation.getId());
            }
        }
    }


    public AppraisalDataResult retrieveDataFromCOMS(final String jobID) throws COMSInterfaceException {
        log.debug("-- retrieveDataFromCOMS ::: jobID : {}", jobID);
        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobID);
//        AppraisalDataResult appraisalDataResult = mockUp();// for test.
        return appraisalDataResult;
    }

    private AppraisalDataResult mockUp(){
        final AppraisalDataResult appraisalDataResult = new AppraisalDataResult();
        appraisalDataResult.setActionResult(ActionResult.SUCCESS);
        final AppraisalData appraisalData = new AppraisalData();
        appraisalData.setJobId("JobId");
        appraisalData.setAppraisalDate(DateTime.now().toDate());
        appraisalData.setMATI("isMATI");
        appraisalData.setAadDecision("AadDecision");
        appraisalData.setAadDecisionReason("AadDecisionReason");
        appraisalData.setAadDecisionReasonDetail("AadDecisionReasonDetail");
        appraisalData.setMortgageCondition("MortgageCondition");
        appraisalData.setMortgageConditionDetail("MortgageConditionDetail");


        final List<HeadCollateralData> headCollateralDataList = new ArrayList<HeadCollateralData>();
        final HeadCollateralData headCollateralData = new HeadCollateralData();
        headCollateralData.setCollId("CollId");
        headCollateralData.setTitleDeed("TitleDeed");
        headCollateralData.setCollateralLocation("CollateralLocation");
        headCollateralData.setAppraisalValue(BigDecimal.TEN);
        headCollateralData.setHeadCollType("HeadCollType");
        headCollateralData.setSubCollType("SubCollType");

        final SubCollateralData subCollateralData = new SubCollateralData();
        final List<SubCollateralData> collateralDataList = new ArrayList<SubCollateralData>();
        subCollateralData.setCollId("CollId");
        subCollateralData.setHeadCollId("HeadCollId");
        subCollateralData.setLandOffice("LandOffice");
        subCollateralData.setRunningNumber(69);
        subCollateralData.setHeadCollType("HeadCollType");
        subCollateralData.setSubCollType("SubCollType");
        subCollateralData.setTitleDeed("TitleDeed");
        subCollateralData.setCollateralOwnerId("CollateralOwnerId");
        subCollateralData.setCollateralOwner("CollateralOwner");
        subCollateralData.setAppraisalValue(BigDecimal.ONE);
        subCollateralData.setUsage("Usage");
        subCollateralData.setTypeOfUsage("TypeOfUsage");
        subCollateralData.setAddress("Address");

        collateralDataList.add(subCollateralData);
        headCollateralData.setSubCollateralDataList(collateralDataList);
        headCollateralDataList.add(headCollateralData);

        appraisalData.setHeadCollateralDataList(headCollateralDataList);

        appraisalDataResult.setAppraisalData(appraisalData);
        return appraisalDataResult;
    }
}
