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
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private NewCollateralTransform newCollateralTransform;


    private Appraisal appraisal;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private AppraisalView appraisalView;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private AppraisalContactDetailView appraisalContactDetailView;

    private WorkCase workCase;
    private NewCreditFacility newCreditFacility;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralSub> newCollateralSubList;

    private List<NewCollateralView> newCollateralViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;

    @Inject
    public AppraisalRequestControl(){

    }
	
	public AppraisalView getAppraisalRequest(final long workCaseId, final User user){
        log.info("-- getAppraisalRequest WorkCaseId : {}, UserId : {}", workCaseId, user.getId());
        appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
        if(appraisal != null){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, user);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            if(newCreditFacility != null){
                newCollateralList = safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            } else {
                log.debug("-- newCreditFacility = null");
            }
            log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
            return appraisalView;
        } else {
            log.debug("-- Find by work case id = {} appraisal is null. ", workCaseId);
            return appraisalView;
        }
    }

    public void onSaveAppraisalRequest(final AppraisalView appraisalView,final long workCaseId, final User user){
        log.info("-- onSaveAppraisalRequest");
        workCase = workCaseDAO.findById(workCaseId);
        appraisal = appraisalTransform.transformToModel(appraisalView, workCase, user);

        if(!Util.isZero(appraisal.getId())){
            log.debug("-- Appraisal id is {}", appraisal.getId());
            appraisalContactDetailList = safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailList);

            appraisalContactDetailList = safetyList(appraisal.getAppraisalContactDetailList());
            for(AppraisalContactDetail model : appraisalContactDetailList){
                model.setAppraisal(appraisal);
            }
            appraisalContactDetailDAO.persist(appraisalContactDetailList);
            appraisalDAO.persist(appraisal);
        } else {
            log.debug("-- Appraisal id is zero");
            appraisalDAO.persist(appraisal);
            appraisalContactDetailList = safetyList(appraisal.getAppraisalContactDetailList());
            for(AppraisalContactDetail model : appraisalContactDetailList){
                model.setAppraisal(appraisal);
            }
            appraisalContactDetailDAO.persist(appraisalContactDetailList);
        }
        log.info("-- appraisalDAO persist end" );





        newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        appraisalDetailViewList = safetyList(appraisalView.getAppraisalDetailViewList());
        newCollateralList = safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility,user));

        log.debug("-- Size of {}", newCollateralList.size());
        for(NewCollateral newCollateral : newCollateralList){
            log.debug("-- NewCollateral.id[{}]", newCollateral.getId());
            newCollateralHeadList = safetyList(newCollateral.getNewCollateralHeadList());
            for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                log.debug("---- NewCollateralHead.id[{}]", newCollateralHead.getId());
                newCollateralSubList = safetyList(newCollateralHead.getNewCollateralSubList());
                for (NewCollateralSub newCollateralSub : newCollateralSubList){
                    log.debug("------ NewCollateralSub.id[{}]", newCollateralSub.getId());
                }
            }
        }


        if(newCollateralList.size() != 0){
            log.debug("1......................................................................1");
            clearDB(newCollateralList);
            log.debug("222222222222222222222222222222222222222222222222222222222222222222222222222");
//            newCollateralDAO.delete(newCollateralList);
            log.debug("333333333333333333333333333333333333333333333333333333333333333333333333333");
//            newCollateralViewList = safetyList(appraisalView.getNewCollateralViewList());
            log.debug("44444444444444444444444444444444444444444444444444444444444444444444444444444");
//            insertToDB(newCollateralViewList, user);
        } else {
            log.debug("1......................................................................2");
            newCollateralDAO.delete(newCollateralList);
            log.debug("222222222222222222222222222222222222222222222222222222222222222222222222222");
            newCollateralViewList = safetyList(appraisalView.getNewCollateralViewList());
            log.debug("333333333333333333333333333333333333333333333333333333333333333333333333333");
            insertToDB(newCollateralViewList, user);
        }
        log.info("-- onSaveAppraisalRequest end");
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
            if(!Util.isZero(id)){
                log.debug("-- NewCollateral.id[{}]", id);
                newCollateralHeadList = safetyList(newCollateralHeadDAO.findByNewCollateralId(id));
                if(newCollateralHeadList.size() != 0){
                    for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                        id = newCollateralHead.getId();
                        log.debug("---- NewCollateralHead.id[{}]", id);
                        newCollateralSubList = safetyList(newCollateralSubDAO.findByNewCollateralHeadId(id));
                        if(newCollateralSubList.size() != 0){
                            for (NewCollateralSub newCollateralSub : newCollateralSubList){
                                log.debug("------ NewCollateralSub.id[{}]", newCollateralSub.getId());
                            }
                        }
                    }
                }

//                    newCollateralSubList = safetyList(newCollateralSubDAO.findByNewCollateralHeadId(id));
//                    newCollateralSubDAO.delete(newCollateralSubList);

//                newCollateralHeadDAO.delete(newCollateralHeadList);
            }
        }
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}