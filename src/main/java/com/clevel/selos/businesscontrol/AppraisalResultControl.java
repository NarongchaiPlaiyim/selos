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
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO newCollateralHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO newCollateralSubDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO newCollateralCreditDAO;

    @Inject
    private ProposeCollateralSubMortgageDAO newCollateralSubMortgageDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO newCollateralSubRelatedDAO;

    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private ProposeLineTransform proposeLineTransform;

    @Inject
    private COMSInterface comsInterface;

    private Appraisal appraisal;
    private AppraisalView appraisalView;

    private ProposeLine newCreditFacility;

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
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            log.debug("-- getAppraisalResult ::: findByWorkCaseId :{}", workCaseId);
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("-- getAppraisalResult ::: findByWorkCasePreScreenId :{}", workCasePreScreenId);
        }

        appraisalView = new AppraisalView();

        if(!Util.isNull(appraisal)){
            newCollateralViewList = new ArrayList<ProposeCollateralInfoView>();
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
            if(!Util.isNull(newCreditFacility)){
                List<ProposeCollateralInfo> newCollateralListTypeP = null;
                List<ProposeCollateralInfo> newCollateralListTypeA2 = null;

                newCollateralListTypeP = Util.safetyList(newCollateralDAO.findNewCollateralByTypeP(newCreditFacility));//normal query
                newCollateralListTypeA2 = Util.safetyList(newCollateralDAO.findNewCollateralByTypeA2(newCreditFacility));

                newCollateralList = new ArrayList<ProposeCollateralInfo>();
                if(!Util.isZero(newCollateralListTypeP.size())){
                    newCollateralList.addAll(newCollateralListTypeP);
                }
                if(!Util.isZero(newCollateralListTypeA2.size())){
                    newCollateralList.addAll(newCollateralListTypeA2);
                }

                List<ProposeCollateralInfo> tempNewCollateralList = new ArrayList<ProposeCollateralInfo>();
                for(ProposeCollateralInfo newCollateral : newCollateralList){
                    newCollateral.setProposeCollateralInfoHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
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

        List<ProposeCollateralInfo> newCollateralList = new ArrayList<ProposeCollateralInfo>();
        if(!Util.isNull(appraisalView) && !Util.isNull(appraisalView.getRemoveCollListId()) && !Util.isZero(appraisalView.getRemoveCollListId().size())){
            for(Long l : appraisalView.getRemoveCollListId()){
                ProposeCollateralInfo newCollateral = newCollateralDAO.findById(l);
                if(newCollateral != null){
                    newCollateralList.add(newCollateral);
                }
            }
        }

        if(newCollateralList.size() > 0){
            for(ProposeCollateralInfo nc : newCollateralList){
                if(nc.getProposeCollateralInfoHeadList() != null && nc.getProposeCollateralInfoHeadList().size() > 0){
                    for(ProposeCollateralInfoHead nch : nc.getProposeCollateralInfoHeadList()){
                        if(nch.getProposeCollateralInfoSubList() != null && nch.getProposeCollateralInfoSubList().size() > 0){
                            for(ProposeCollateralInfoSub ncs : nch.getProposeCollateralInfoSubList()){
                                List<ProposeCollateralSubRelated> newCollSub = newCollateralSubRelatedDAO.findByMainCollSubId(ncs.getId());
                                newCollateralSubRelatedDAO.delete(newCollSub);
                            }
                        }
                    }
                }
            }
        }

        newCollateralDAO.delete(newCollateralList);

        if(appraisalView != null && !Util.isNull(appraisalView.getNewCollateralViewList()) && !Util.isZero(appraisalView.getNewCollateralViewList().size())){
            List<ProposeCollateralInfoView> newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
            insertToDB(newCollateralViewList, currentUser , workCaseId , workCasePreScreenId);
        }
    }

//    private void updateWRKNewColl(final List<NewCollateralView> newCollateralViewList, final User user){
//        log.debug("-- updateWRKNewColl");
//        newCollateralList = Util.safetyList(newCollateralTransform.transformToModel(newCollateralViewList, user, newCreditFacility));
//        for(NewCollateral newCollateral : newCollateralList){
//            if(!Util.isZero(newCollateral.getId())){
//                newCollateral =  newCollateralDAO.findById(newCollateral.getId());
//                newCollateral.setAppraisalRequest(2);
//                newCollateral.setProposeType(ProposeType.P);
//                newCollateralDAO.save(newCollateral);
//            }
//        }
//    }

    private void insertToDB(final List<ProposeCollateralInfoView> newCollateralViewList, final User user, long workCaseId, long workCasePreScreenId){
        log.debug("-- insertIntoDB ::: newCollateralViewList ::: {} ", newCollateralViewList);
        ProposeLine newCreditFacility = new ProposeLine();
        WorkCase workCase = null;
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            log.debug("-- WorkCaseId[{}]", workCaseId);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            workCase = newCreditFacility.getWorkCase();
            log.debug("-- WorkCase.id[{}]", workCase.getId());
        } else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }
        log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());

        List<ProposeCollateralInfo> newCollateralList = new ArrayList<ProposeCollateralInfo>();
        for(ProposeCollateralInfoView proposeCollateralInfoView : newCollateralViewList) {
            newCollateralList.add(proposeLineTransform.transformProposeCollateralToModel(workCase, newCreditFacility, proposeCollateralInfoView, user, ProposeType.A));
        }

        for (final ProposeCollateralInfo newCollateral : newCollateralList) {
            log.debug("-- NewCollateral[{}]", newCollateral);
            if(!newCollateralDAO.isExist(newCollateral.getId())){
                log.debug("-- Insert into new record of NewCollateral");
                log.debug("-- processing one step...");
                newCollateral.setAppraisalRequest(2);
                newCollateral.setProposeType(ProposeType.A);
                newCollateralDAO.persist(newCollateral);
                log.debug("-- id[{}] saved", newCollateral.getId());
            } else {
                log.debug("-- Update to exist record of NewCollateral.id[{}]", newCollateral.getId());
                log.debug("-- processing first step...");
//                newCollateralDAO.delete(newCollateralDAO.findById(newCollateral.getId()));
                ProposeCollateralInfo model = newCollateralDAO.findById(newCollateral.getId());
                model.setAppraisalRequest(2);
                model.setProposeType(ProposeType.A);
                model.setProposeLine(newCollateral.getProposeLine());
                log.debug("-- Model[{}]", model.toString());
                log.debug("-- processing second step...");
                newCollateralDAO.persist(model);
                log.debug("-- id[{}] updated", model.getId());
            }
        }

        for(final ProposeCollateralInfo newCollateral : newCollateralList){
            List<ProposeCollateralInfoHead> newCollateralHeadList = Util.safetyList(newCollateral.getProposeCollateralInfoHeadList());
            for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                newCollateralHead.setProposeCollateral(newCollateral);
                newCollateralHead.setProposeType(ProposeType.P);
                newCollateralHead.setAppraisalRequest(2);
                List<ProposeCollateralInfoSub> newCollateralSubList = Util.safetyList(newCollateralHead.getProposeCollateralInfoSubList());
                for(ProposeCollateralInfoSub newCollateralSub : newCollateralSubList){
                    newCollateralSub.setProposeCollateralHead(newCollateralHead);
                }
                if(!Util.isZero(newCollateralSubList.size())){
                    log.debug("-- persist newCollateralSubList.size()[{}]", newCollateralSubList.size());
                    newCollateralSubDAO.persist(newCollateralSubList);
                    log.debug("-- newCollateralSubList[{}]", newCollateralSubList);
                }
            }
            if(!Util.isZero(newCollateralHeadList.size())){
                log.debug("-- persist newCollateralHeadList.size()[{}]", newCollateralHeadList.size());
                newCollateralHeadDAO.persist(newCollateralHeadList);
                log.debug("-- newCollateralHeadList[{}]", newCollateralHeadList);
            }
        }
    }

    /*private void clearDB(final List<NewCollateral> newCollateralList){
        log.debug("-- clear db");
        long id;
        List<NewCollateralCredit> newCollateralCreditList = null;
        List<NewCollateralSubMortgage> newCollateralSubMortgageList = null;
        List<NewCollateralSubOwner> newCollateralSubOwnerList = null;
        List<NewCollateralSubRelated> newCollateralSubRelatedList = null;
        for(NewCollateral newCollateral : newCollateralList){
            id = newCollateral.getId();
            log.debug("-- NewCollateral.id[{}]", id);

            newCollateralHeadList = Util.safetyList(newCollateralHeadDAO.findByNewCollateralId(id));
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                id = newCollateralHead.getId();

                newCollateralSubList = Util.safetyList(newCollateralSubDAO.findByNewCollateralHeadId(id));
                for(NewCollateralSub newCollateralSub : newCollateralSubList){

                    newCollateralSubMortgageList = Util.safetyList(newCollateralSubMortgageDAO.findByNewCollateralSubId(newCollateralSub.getId()));
                    for(NewCollateralSubMortgage newCollateralSubMortgage : newCollateralSubMortgageList){
                        log.debug("------ NewCollateralSubMortgage.id[{}]", newCollateralSubMortgage.getId());
                        newCollateralSubMortgageDAO.delete(newCollateralSubMortgage);
                        log.debug("------ Deleted");
                    }
                    newCollateralSub.setNewCollateralSubMortgageList(Collections.<NewCollateralSubMortgage>emptyList());

                    newCollateralSubOwnerList = Util.safetyList(newCollateralSubOwnerDAO.findByNewCollateralSubId(newCollateralSub.getId()));
                    for(NewCollateralSubOwner newCollateralSubOwner : newCollateralSubOwnerList){
                        log.debug("------ NewCollateralSubOwner.id[{}]", newCollateralSubOwner.getId());
                        newCollateralSubOwnerDAO.delete(newCollateralSubOwner);
                        log.debug("------ Deleted");
                    }
                    newCollateralSub.setNewCollateralSubOwnerList(Collections.<NewCollateralSubOwner>emptyList());

                    newCollateralSubRelatedList = Util.safetyList(newCollateralSubRelatedDAO.findByNewCollateralSubId(newCollateralSub.getId()));
                    for(NewCollateralSubRelated newCollateralSubRelated : newCollateralSubRelatedList){
                        log.debug("------ NewCollateralSubRelated.id[{}]", newCollateralSubRelated.getId());
                        newCollateralSubRelatedDAO.delete(newCollateralSubRelated);
                        log.debug("------ Deleted");
                    }
                    newCollateralSub.setNewCollateralSubRelatedList(Collections.<NewCollateralSubRelated>emptyList());

                    log.debug("------ NewCollateralSub.id[{}]", newCollateralSub.getId());
                    newCollateralSubDAO.persist(newCollateralSub);
                    newCollateralSubDAO.delete(newCollateralSub);
                    log.debug("------ Deleted");
                }

                log.debug("---- NewCollateralHead.id[{}]", newCollateralHead.getId());
                newCollateralHead.setNewCollateralSubList(Collections.<NewCollateralSub>emptyList());
                newCollateralHeadDAO.persist(newCollateralHead);
                newCollateralHeadDAO.delete(newCollateralHead);
                log.debug("---- Deleted");
            }

            newCollateralCreditList = Util.safetyList(newCollateralCreditDAO.findByNewCollateralId(id));
            for(NewCollateralCredit newCollateralCredit : newCollateralCreditList){
                log.debug("---- NewCollateralCredit.id[{}]", newCollateralCredit.getId());
                newCollateralCreditDAO.delete(newCollateralCredit);
                log.debug("---- Deleted");
            }

            log.debug("-- NewCollateral.id[{}]", newCollateral.getId());
            newCollateral.setNewCollateralCreditList(Collections.<NewCollateralCredit>emptyList());
            newCollateralDAO.persist(newCollateral);
            newCollateralDAO.delete(newCollateral);
            log.debug("-- Deleted");
        }
    }*/

    public AppraisalDataResult retrieveDataFromCOMS(final String jobID) throws COMSInterfaceException {
        log.debug("-- retrieveDataFromCOMS ::: jobID : {}", jobID);
        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobID);
        return appraisalDataResult;
    }
}
