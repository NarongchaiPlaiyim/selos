package com.clevel.selos.transform;

import com.clevel.selos.dao.working.ProposeCollateralInfoDAO;
import com.clevel.selos.dao.working.ProposeCollateralInfoHeadDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ProposeCollateralInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppraisalDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    private Message msg;
    @Inject
    private ProposeLineTransform proposeLineTransform;

    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO newCollateralHeadDAO;
    private List<ProposeCollateralInfo> newCollateralList;
    private List<ProposeCollateralInfo> newCollateralListForReturn;
    private List<ProposeCollateralInfoHead> newCollateralHeadList;

    private ProposeCollateralInfo newCollateral;
    private List<AppraisalDetailView> appraisalDetailViewList;

    @Inject
    public AppraisalDetailTransform() {
    }

    public List<ProposeCollateralInfo> transformToModel(List<AppraisalDetailView> appraisalDetailViewList, ProposeLine newCreditFacility, User user, RequestAppraisalValue requestAppraisalValue, ProposeType proposeType){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", appraisalDetailViewList.size());

        long newCollateralId = 0;
        long newCollateralHeadId = 0;
        boolean createNewCollateralFlag = true;

        if(newCreditFacility != null && newCreditFacility.getId() != 0){
            //Find all Collateral in Application
            newCollateralList = Util.safetyList(newCollateralDAO.findCollateralForAppraisal(newCreditFacility, proposeType));
        }else{
            newCollateralList = new ArrayList<ProposeCollateralInfo>();
        }
        newCollateralListForReturn = new ArrayList<ProposeCollateralInfo>();

        for(AppraisalDetailView view : appraisalDetailViewList){
            newCollateralId = view.getNewCollateralId();
            newCollateralHeadId = view.getNewCollateralHeadId();
            log.debug("-- newCollateralId {}", newCollateralId);
            log.debug("-- newCollateralHeadId {}", newCollateralHeadId);

            //set value for Collateral from Credit facility
            if(!Util.isZero(newCollateralId)){
                //loop for check collateral with appraisal detail
                for(ProposeCollateralInfo newCollateral : newCollateralList){
                    if(newCollateral.getId() == newCollateralId){
                        log.debug("---- newCollateral.getId()[{}] == newCollateralId[{}] ", newCollateral.getId(), newCollateralId);
                        newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
                        //loop for check head collateral with appraisal detail
                        List<ProposeCollateralInfoHead> newCollateralHeadForAdd = new ArrayList<ProposeCollateralInfoHead>();
                        for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                            log.debug("------ NewCollateralHead.getId()[{}] == newCollateralHeadId[{}] ",newCollateralHead.getId(), newCollateralHeadId);
                            if(newCollateralHead.getId() == newCollateralHeadId){
                                //to set collateral head from appraisalDetailView
                                newCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
                                newCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
                                newCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
                                newCollateralHead.setTitleDeed(view.getTitleDeed());
                                newCollateralHead.setCollateralChar(view.getCharacteristic());
                                newCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
                                newCollateralHead.setModifyBy(user);
                                newCollateralHead.setModifyDate(DateTime.now().toDate());
                                newCollateralHead.setAppraisalRequest(requestAppraisalValue.value());
                                newCollateralHead.setProposeType(proposeType);

                                newCollateralHeadForAdd.add(newCollateralHead);

                                continue;
                            }
                        }
                        newCollateral.setAppraisalRequest(requestAppraisalValue.value());
                        newCollateral.setProposeCollateralInfoHeadList(newCollateralHeadForAdd);
                        newCollateralListForReturn.add(newCollateral);
                        continue;
                    }
                }
            } else {
                //Create new collateral and collateral head
                List<ProposeCollateralInfoHead> newCollateralHeadListForNewCollateralHead = null;
                ProposeCollateralInfoHead newCollateralHeadForNewCollateralHead = null;
                //New collateral for all new head collateral
                log.debug("-- Create new Collateral for Appraisal");
                newCollateral = new ProposeCollateralInfo();
                newCollateral.setModifyDate(DateTime.now().toDate());
                newCollateral.setModifyBy(user);
                newCollateral.setCreateDate(DateTime.now().toDate());
                newCollateral.setCreateBy(user);
                newCollateral.setProposeLine(newCreditFacility);
                newCollateral.setWorkCase(newCreditFacility.getWorkCase());
                newCollateral.setAppraisalRequest(requestAppraisalValue.value());
                newCollateral.setProposeType(proposeType);
                newCollateral.setUwDecision(DecisionType.NO_DECISION);
                log.debug("transformToModel ::: newCollateral : {}", newCollateral);

                newCollateralHeadListForNewCollateralHead = new ArrayList<ProposeCollateralInfoHead>();
                newCollateralHeadForNewCollateralHead = new ProposeCollateralInfoHead();

                newCollateralHeadForNewCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
                newCollateralHeadForNewCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
                newCollateralHeadForNewCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
                newCollateralHeadForNewCollateralHead.setTitleDeed(view.getTitleDeed());
                newCollateralHeadForNewCollateralHead.setCollateralChar(view.getCharacteristic());
                newCollateralHeadForNewCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
                newCollateralHeadForNewCollateralHead.setModifyBy(user);
                newCollateralHeadForNewCollateralHead.setModifyDate(view.getModifyDate());
                newCollateralHeadForNewCollateralHead.setCreateDate(DateTime.now().toDate());
                newCollateralHeadForNewCollateralHead.setCreateBy(user);
                newCollateralHeadForNewCollateralHead.setAppraisalRequest(requestAppraisalValue.value());
                newCollateralHeadForNewCollateralHead.setProposeType(proposeType);
                newCollateralHeadForNewCollateralHead.setProposeCollateral(newCollateral);

                newCollateralHeadListForNewCollateralHead.add(newCollateralHeadForNewCollateralHead);
                newCollateral.setProposeCollateralInfoHeadList(newCollateralHeadListForNewCollateralHead);
                newCollateralListForReturn.add(newCollateral);
                log.debug("-- NewCollateral {}", newCollateral);
                log.debug("-- NewCollateral added to newCollateralList[Size {}]", newCollateralListForReturn.size());
            }
        }

        return newCollateralListForReturn;
    }

    public List<ProposeCollateralInfo> transformAppraisalResult(AppraisalView appraisalView, ProposeLine newCreditFacility, User currentUser, RequestAppraisalValue requestAppraisalValue, ProposeType proposeType, WorkCase workCase){
        List<ProposeCollateralInfo> proposeCollateralInfoList = new ArrayList<ProposeCollateralInfo>();
        long newCollateralId = 0;
        long newCollateralHeadId = 0;
        boolean createNewCollateralFlag = true;

        if(newCreditFacility != null && newCreditFacility.getId() != 0){
            //Find all Collateral in Application
            newCollateralList = Util.safetyList(newCollateralDAO.findCollateralForAppraisal(newCreditFacility, proposeType));
        }else{
            newCollateralList = new ArrayList<ProposeCollateralInfo>();
        }

        for(ProposeCollateralInfoView proposeCol : appraisalView.getNewCollateralViewList()){
            proposeCol.setAppraisalRequest(RequestAppraisalValue.REQUESTED.value());

            ProposeCollateralInfo proposeCollateralInfo = proposeLineTransform.transformProposeCollateralToModel(workCase, newCreditFacility, proposeCol, currentUser, proposeType);
            proposeCollateralInfoList.add(proposeCollateralInfo);
        }

        return proposeCollateralInfoList;
    }

    public List<AppraisalDetailView> transformToView(final List<ProposeCollateralInfo> newCollateralList){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", newCollateralList != null ? newCollateralList.size() : null);
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        AppraisalDetailView view = null;
        List<ProposeCollateralInfoHead> newCollateralHeadList = null;
        int id = 0;
        for(ProposeCollateralInfo newCollateral : newCollateralList) {
            newCollateralHeadList = Util.safetyList(newCollateral.getProposeCollateralInfoHeadList());
            log.debug("transformToView ::: newCollateralHeadList : {}", newCollateralHeadList);
            for (ProposeCollateralInfoHead model  : newCollateralHeadList) {

                if(!Util.isNull(model.getPotentialCollateral()) && !Util.isTrue(model.getPotentialCollateral().getAppraisalFlag()))
                    continue;

                view = new AppraisalDetailView();
                view.setNewCollateralId(newCollateral.getId());
                view.setNewCollateralHeadId(model.getId());
                view.setTitleDeed(model.getTitleDeed());

                id = model.getPurposeNewAppraisal();
                view.setPurposeNewAppraisal(id);
                view.setPurposeNewAppraisalB(Util.isTrue(id));

                id= model.getPurposeReviewAppraisal();
                view.setPurposeReviewAppraisal(model.getPurposeReviewAppraisal());
                view.setPurposeReviewAppraisalB(Util.isTrue(id));

                id= model.getPurposeReviewBuilding();
                view.setPurposeReviewBuilding(id);
                view.setPurposeReviewBuildingB(Util.isTrue(id));
                view.setCharacteristic(model.getCollateralChar());
                view.setNumberOfDocuments(model.getNumberOfDocuments());
                view.setModifyBy(model.getModifyBy());
                view.setModifyDate(model.getModifyDate());

                appraisalDetailViewList.add(view);
            }
        }
        return appraisalDetailViewList;
    }

    public List<AppraisalDetailView> updateLabel(List<AppraisalDetailView> appraisalDetailViewList){
        StringBuilder stringBuilder = null;
        for(AppraisalDetailView appraisalDetailView : appraisalDetailViewList){
            stringBuilder = new StringBuilder();
            if(appraisalDetailView.isPurposeReviewAppraisalB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeReviewAppraisal"));
            }
            if(appraisalDetailView.isPurposeNewAppraisalB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal"));
            }
            if(appraisalDetailView.isPurposeReviewBuildingB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeReviewBuilding"));
            }
            String result = stringBuilder.toString();
            if(!Util.isEmpty(result) && result.length() >= 2){
                appraisalDetailView.setPurposeReviewAppraisalLabel(result.substring(2, result.length()));
            } else {
                appraisalDetailView.setPurposeReviewAppraisalLabel("");
            }

        }
        return appraisalDetailViewList;
    }
}
