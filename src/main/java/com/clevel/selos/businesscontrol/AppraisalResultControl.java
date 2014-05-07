package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.ProposeType;
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
import java.util.Collections;
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
    private NewCollateralCreditDAO newCollateralCreditDAO;

    @Inject
    private NewCollateralSubMortgageDAO newCollateralSubMortgageDAO;
    @Inject
    private NewCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    private NewCollateralSubRelatedDAO newCollateralSubRelatedDAO;

    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralSubTransform newCollateralSubTransform;

    @Inject
    private COMSInterface comsInterface;

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
            newCollateralViewList = new ArrayList<NewCollateralView>();
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
            if(!Util.isNull(newCreditFacility)){
                List<NewCollateral> newCollateralListTypeP = null;
                List<NewCollateral> newCollateralListTypeA2 = null;

                newCollateralListTypeP = Util.safetyList(newCollateralDAO.findNewCollateralByTypeP(newCreditFacility));//normal query
                newCollateralListTypeA2 = Util.safetyList(newCollateralDAO.findNewCollateralByTypeA2(newCreditFacility));

                newCollateralList = new ArrayList<NewCollateral>();
                if(!Util.isZero(newCollateralListTypeP.size())){
                    newCollateralList.addAll(newCollateralListTypeP);
                }
                if(!Util.isZero(newCollateralListTypeA2.size())){
                    newCollateralList.addAll(newCollateralListTypeA2);
                }

                List<NewCollateral> tempNewCollateralList = new ArrayList<NewCollateral>();
                for(NewCollateral newCollateral : newCollateralList){
                    newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                    tempNewCollateralList.add(newCollateral);
                }
                newCollateralViewList = newCollateralTransform.transformToView(tempNewCollateralList);
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
        //for remove coll
        List<NewCollateral> newCollateralList = new ArrayList<NewCollateral>();
        if(appraisalView != null && appraisalView.getRemoveCollListId() != null && appraisalView.getRemoveCollListId().size() > 0){
            for(Long l : appraisalView.getRemoveCollListId()){
                NewCollateral newCollateral = newCollateralDAO.findById(l);
                if(newCollateral != null){
                    newCollateralList.add(newCollateral);
                }
            }
        }

        if(newCollateralList.size() > 0){
            for(NewCollateral nc : newCollateralList){
                if(nc.getNewCollateralHeadList() != null && nc.getNewCollateralHeadList().size() > 0){
                    for(NewCollateralHead nch : nc.getNewCollateralHeadList()){
                        if(nch.getNewCollateralSubList() != null && nch.getNewCollateralSubList().size() > 0){
                            for(NewCollateralSub ncs : nch.getNewCollateralSubList()){
                                List<NewCollateralSubRelated> newCollSub = newCollateralSubRelatedDAO.findByMainCollSubId(ncs.getId());
                                newCollateralSubRelatedDAO.delete(newCollSub);
                            }
                        }
                    }
                }
            }
        }

        newCollateralDAO.delete(newCollateralList);

        if(appraisalView != null && !Util.isNull(appraisalView.getNewCollateralViewList()) && !Util.isZero(appraisalView.getNewCollateralViewList().size())){
            List<NewCollateralView> newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
            insertToDB(newCollateralViewList, currentUser , workCaseId , workCasePreScreenId);
        }
    }

    public void onSaveAppraisalResultOld(final AppraisalView appraisalView, final long workCaseId, final long workCasePreScreenId){
        log.info("-- onSaveAppraisalResult begin");
        User currentUser = getCurrentUser();
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            log.debug("onSaveAppraisalResult ::: find creditFacility by workCaseId : {}", workCaseId);
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("onSaveAppraisalResult ::: find creditFacility by workCasePreScreenId : {}", workCasePreScreenId);
        }
        log.debug("onSaveAppraisalResult ::: newCreditFacility : {}", newCreditFacility);

        List<NewCollateral> newCollateralListTypeP = null;
        List<NewCollateral> newCollateralListTypeA = null;

        newCollateralListTypeP = Util.safetyList(newCollateralDAO._findNewCollateralByTypeP(newCreditFacility));
        newCollateralListTypeA = Util.safetyList(newCollateralDAO.findNewCollateralByTypeA(newCreditFacility)); //normal query

        newCollateralList = new ArrayList<NewCollateral>();
        if(!Util.isZero(newCollateralListTypeP.size())){
            newCollateralList.addAll(newCollateralListTypeP);
        }
        if(!Util.isZero(newCollateralListTypeA.size())){
            newCollateralList.addAll(newCollateralListTypeA);
        }

        if(Util.isNull(appraisalView.getNewCollateralViewList()) || Util.isZero(appraisalView.getNewCollateralViewList().size())){
            log.debug("-- NewCollateralViewList.size()[{}]", 0);
            log.debug("-- NewCollateralList.size()[{}]", newCollateralList.size());
            clearDB(newCollateralList);
//            for (NewCollateral newCollateral : newCollateralList){
//                log.debug("-- NewCollateral.id[{}] ", newCollateral.getId());
//                newCollateralDAO.delete(newCollateral);
//                log.debug("-- deleted");
//            }
//            newCollateralDAO.delete(newCollateralList);
        } else {
            log.debug("-- NewCollateralList.size()[{}]", newCollateralList.size());
            for (NewCollateral newCollateral : newCollateralList){
                newCollateralDAO.delete(newCollateral);
                log.debug("-- NewCollateral.id[{}] deleted", newCollateral.getId());
            }

            newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
            log.debug("onSaveAppraisalResult ::: saveCollateralData : newCollateralViewList : {}", newCollateralViewList);
            insertToDB(newCollateralViewList, currentUser, workCaseId, workCasePreScreenId);
            log.debug("onSaveAppraisalResult ::: newCollateralList for save : {}", newCollateralList);
        }


//        if(newCollateralList.size() > 0){
//            log.debug("onSaveAppraisalResult ::: clearCollateralData");
//            clearDB(newCollateralList);
//            log.debug("onSaveAppraisalResult ::: newCollateralList for delete : {}", newCollateralList);
//            newCollateralDAO.delete(newCollateralList);
//
//            newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
//            log.debug("onSaveAppraisalResult ::: saveCollateralData : newCollateralViewList : {}", newCollateralViewList);
//            insertToDB(newCollateralViewList, currentUser);
//            log.debug("onSaveAppraisalResult ::: newCollateralList for save : {}", newCollateralList);
//            updateWRKNewColl(newCollateralViewList, currentUser);
//        } else {
//            newCollateralDAO.delete(newCollateralList);
//            newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
//            insertToDB(newCollateralViewList, currentUser);
//            updateWRKNewColl(newCollateralViewList, currentUser);
//        }

        log.info("-- onSaveAppraisalResult end");
    }

    private void updateWRKNewColl(final List<NewCollateralView> newCollateralViewList, final User user){
        log.debug("-- updateWRKNewColl");
        newCollateralList = Util.safetyList(newCollateralTransform.transformToModel(newCollateralViewList, user, newCreditFacility));
        for(NewCollateral newCollateral : newCollateralList){
            if(!Util.isZero(newCollateral.getId())){
                newCollateral =  newCollateralDAO.findById(newCollateral.getId());
                newCollateral.setAppraisalRequest(2);
                newCollateral.setProposeType(ProposeType.P);
                newCollateralDAO.save(newCollateral);
            }
        }
    }

    private void insertToDB(final List<NewCollateralView> newCollateralViewList, final User user, long workCaseId, long workCasePreScreenId){
        log.debug("insertToDB ::: newCollateralViewList ::: {} ", newCollateralViewList);
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }

        newCollateralList = Util.safetyList(newCollateralTransform.transformToNewModel(newCollateralViewList, user, newCreditFacility));
        newCollateralDAO.persistProposeTypeA(newCollateralList);
        for(NewCollateral newCollateral : newCollateralList){
            newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                newCollateralHead.setNewCollateral(newCollateral);
                newCollateralHead.setProposeType("P");
                newCollateralHead.setAppraisalRequest(2);
                newCollateralSubList = Util.safetyList(newCollateralHead.getNewCollateralSubList());
                for(NewCollateralSub newCollateralSub : newCollateralSubList){
                    newCollateralSub.setNewCollateralHead(newCollateralHead);
                }
                newCollateralSubDAO.persist(newCollateralSubList);
            }
            newCollateralHeadDAO.persist(newCollateralHeadList);
        }
    }

    private void clearDB(final List<NewCollateral> newCollateralList){
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
    }

    public AppraisalDataResult retrieveDataFromCOMS(final String jobID){
        log.debug("-- retrieveDataFromCOMS ::: jobID : {}", jobID);
        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(getCurrentUserID(), jobID);
        return appraisalDataResult;
    }
}
