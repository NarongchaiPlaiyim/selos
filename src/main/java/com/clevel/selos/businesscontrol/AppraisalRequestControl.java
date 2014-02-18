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
    private WorkCasePrescreen workCasePrescreen;
    private NewCreditFacility newCreditFacility;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralSub> newCollateralSubList;

    private List<NewCollateralView> newCollateralViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;

    @Inject
    public AppraisalRequestControl(){

    }
	
	public AppraisalView getAppraisalRequest(final long workCaseId, final long workCasePreScreenId){
        log.info("-- getAppraisalRequest WorkCaseId : {}, workCasePreScreenId : {}, User.id[{}]", workCaseId, workCasePreScreenId, getCurrentUserID());
        if(Long.toString(workCaseId) != null && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
        } else if(Long.toString(workCasePreScreenId) != null && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }

        if(!Util.isNull(appraisal)){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());
            if(Long.toString(workCaseId) != null && workCaseId != 0){
                newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            } else if(Long.toString(workCasePreScreenId) != null && workCasePreScreenId != 0){
                newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            }
            log.debug("getAppraisalRequest ::: newCreditFacility : {}", newCreditFacility);
            if(!Util.isNull(newCreditFacility)){
                newCollateralList = safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
                log.debug("getAppraisalRequest ::: newCollateralList : {}", newCollateralList);
                List<NewCollateral> newCollateralListForAdd = new ArrayList<NewCollateral>();
                for(NewCollateral newCollateral : newCollateralList){
                    newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                    newCollateralListForAdd.add(newCollateral);
                }
                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralListForAdd);
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

    public void onSaveAppraisalRequest(final AppraisalView appraisalView,final long workCaseId, final long workCasePreScreenId){
        log.info("-- onSaveAppraisalRequest ::: workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
        User currentUser = getCurrentUser();
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            workCasePrescreen = null;
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCase = null;
        }

        log.debug("onSaveAppraisalRequest ::: workCase : {}, workCasePrescreen : {}", workCase, workCasePrescreen);

        if(!Util.isNull(workCase) || !Util.isNull(workCasePrescreen)){
            log.debug("onSaveAppraisalRequest ::: workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
            appraisal = appraisalTransform.transformToModel(appraisalView, workCase, workCasePrescreen, currentUser);

            //remove all appraisalContactDetailList
            List<AppraisalContactDetail> appraisalContactDetailDeleteList = safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailDeleteList);

            log.debug("onSaveAppraisalRequest ::: before persist appraisal : {}", appraisal);
            appraisalDAO.persist(appraisal);
            log.debug("onSaveAppraisalRequest ::: after persist appraisal : {}", appraisal);

            if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
                newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
                newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            }
            log.debug("onSaveAppraisalRequest ::: newCreditFacility : {}");

            //check newCreditFacility is exist? else create new one
            if(Util.isNull(newCreditFacility)){
                newCreditFacility = new NewCreditFacility();
                newCreditFacility.setWorkCasePrescreen(workCasePrescreen);
                newCreditFacility.setWorkCase(workCase);
            }
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());

            appraisalDetailViewList = safetyList(appraisalView.getAppraisalDetailViewList());

            //find all collateral in credit facility
            if(newCreditFacility.getId() != 0){
                newCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            }else{
                newCollateralList = new ArrayList<NewCollateral>();
            }
            //set flag 0 for all collateral
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralHeadList = safetyList(newCollateralHeadDAO.findByNewCollateralId(newCollateral.getId()));
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setAppraisalRequest(0);
                }
                newCollateralHeadDAO.persist(newCollateralHeadList);
            }

            newCollateralList.clear();
            newCollateralList = safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, currentUser));
            log.debug("onSaveAppraisalRequest ::: before persist newCreditfacility : {}", newCreditFacility);
            newCreditFacilityDAO.persist(newCreditFacility);
            log.debug("onSaveAppraisalRequest ::: after persist newCreditfacility : {}", newCreditFacility);

            log.debug("onSaveAppraisalRequest ::: before persist newCollateralList : {}", newCollateralList);
            newCollateralDAO.persist(newCollateralList);
            log.debug("onSaveAppraisalRequest ::: after persist newCollateralList : {}", newCollateralList);

            /*for(NewCollateral newCollateral : newCollateralList){
                newCollateralDAO.persist(newCollateral);
                newCollateralHeadList = safetyList(newCollateral.getNewCollateralHeadList());
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setNewCollateral(newCollateral);
                    newCollateralHeadDAO.persist(newCollateralHead);
                }
            }*/

            log.info("-- onSaveAppraisalRequest end");
        }
    }

    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
    }
}