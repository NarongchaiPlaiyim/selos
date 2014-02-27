package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class FullApplicationControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;

    @Inject
    UserDAO userDAO;
    @Inject
    RoleDAO roleDAO;
    @Inject
    AppraisalDAO appraisalDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    NewCreditDetailDAO newCreditDetailDAO;

    @Inject
    BPMExecutor bpmExecutor;

    public List<User> getABDMUserList(){
        User currentUser = getCurrentUser();
        Role abdmRole = roleDAO.findById(RoleValue.ABDM.id());

        List<User> abdmUserList = userDAO.findABDMList(currentUser, abdmRole);
        if(abdmUserList == null){
            abdmUserList = new ArrayList<User>();
        }
        log.debug("getABDMUserList : abdmUserList size : {}", abdmUserList.size());

        return abdmUserList;
    }

    public List<User> getZMUserList(){
        User currentUser = getCurrentUser();

        List<User> zmUserList = userDAO.findUserZoneList(currentUser);
        if(zmUserList == null){
            zmUserList = new ArrayList<User>();
        }

        return zmUserList;
    }

    public List<User> getRMUserList(){
        User currentUser = getCurrentUser();

        List<User> rmUserList = userDAO.findUserRegionList(currentUser);
        if(rmUserList == null){
            rmUserList = new ArrayList<User>();
        }

        return rmUserList;
    }

    public List<User> getHeadUserList(){
        User currentUser = getCurrentUser();

        List<User> ghUserList = userDAO.findUserHeadList(currentUser);
        if(ghUserList == null){
            ghUserList = new ArrayList<User>();
        }

        return ghUserList;
    }

    public void assignToABDM(String abdmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.assignToABDM(workCaseId, queueName, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    public void submitToZM(String zmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.submitZM(workCaseId, queueName, zmUserId, ActionCode.SUBMIT_TO_ZM.getVal());
    }

    public void requestAppraisalBDM(long workCasePreScreenId, long workCaseId) throws Exception{
        //update request appraisal flag
        if(workCasePreScreenId != 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setRequestAppraisal(1);
            workCasePrescreenDAO.persist(workCasePrescreen);
        } else if(workCaseId != 0) {
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setRequestAppraisal(1);
            workCaseDAO.persist(workCase);
        } else {
            log.error("exception while Request Appraisal (BDM), can not find workcase or workcaseprescreen.");
            throw new Exception("exception while Request Appraisal, can not find case.");
        }
    }

    public void requestAppraisal(long workCasePreScreenId, long workCaseId) throws Exception{
        try{
            WorkCaseAppraisal workCaseAppraisal = createWorkCaseAppraisal(workCasePreScreenId, workCaseId);
            try{
                createWorkItemAppraisal(workCaseAppraisal);
                log.debug("create workcase for appraisal complete.");
            }catch (Exception ex){
                workCaseAppraisalDAO.delete(workCaseAppraisal);
                log.error("exception while launch workflow for appraisal, remove workcase appraisal.");
                throw new Exception(Util.getMessageException(ex));
            }
        }catch (Exception ex){
            throw new Exception(ex);
        }

    }

    public void createWorkItemAppraisal(WorkCaseAppraisal workCaseAppraisal) throws Exception{
        log.debug("requestAppraisal ::: workCaseAppraisal : {}", workCaseAppraisal);
        bpmExecutor.requestAppraisal(workCaseAppraisal.getAppNumber(), "", workCaseAppraisal.getProductGroup().getName(), workCaseAppraisal.getRequestType().getId(), getCurrentUserID());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public WorkCaseAppraisal createWorkCaseAppraisal(long workCasePreScreenId, long workCaseId) throws Exception {
        //Find all data in WorkCase or WorkCasePreScreen
        WorkCasePrescreen workCasePrescreen;
        WorkCase workCase;
        String appNumber = "";
        ProductGroup productGroup = null;
        RequestType requestType = null;
        log.debug("requestAppraisal ::: start...");
        if(Long.toString(workCasePreScreenId) != null && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            log.debug("requestAppraisal ::: getAppNumber from workCasePrescreen : {}", workCasePrescreen);
            if(workCasePrescreen != null){
                appNumber = workCasePrescreen.getAppNumber();
                productGroup = workCasePrescreen.getProductGroup();
                requestType = workCasePrescreen.getRequestType();
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from prescreen");
            }
        }else if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            log.debug("requestAppraisal ::: getAppNumber from workCase : {}", workCase);
            if(workCase != null){
                appNumber = workCase.getAppNumber();
                productGroup = workCase.getProductGroup();
                requestType = workCase.getRequestType();
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from full application");
            }
        }else{
            throw new Exception("exception while request appraisal, cause session variable expired.");
        }

        //TODO Insert data into WRK_APPRAISAL
        WorkCaseAppraisal workCaseAppraisal = new WorkCaseAppraisal();
        workCaseAppraisal.setAppNumber(appNumber);
        workCaseAppraisal.setCreateDate(DateTime.now().toDate());
        workCaseAppraisal.setCreateBy(getCurrentUser());
        workCaseAppraisal.setModifyDate(DateTime.now().toDate());
        workCaseAppraisal.setModifyBy(getCurrentUser());
        workCaseAppraisal.setRequestDate(DateTime.now().toDate());
        workCaseAppraisal.setRequestBy(getCurrentUser());
        workCaseAppraisal.setProductGroup(productGroup);
        workCaseAppraisal.setRequestType(requestType);
        workCaseAppraisalDAO.persist(workCaseAppraisal);

        return workCaseAppraisal;
    }

    public void submitToAADCommittee(String aadCommitteeUserId, long workCaseId, long workCasePreScreenId, String queueName) throws Exception{
        log.debug("submitToAADCommittee ::: starting...");
        String appNumber = "";
        Appraisal appraisal = null;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            appNumber = workCase.getAppNumber();
            log.debug("submitToAADCommittee ::: find appraisal by workCase : {}", appraisal);
            log.debug("submitToAADCommittee ::: find workCase : {}", workCase);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCaseId);
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            appNumber = workCasePrescreen.getAppNumber();
            log.debug("submitToAADCommittee ::: find appraisal by workCasePrescreen: {}", appraisal);
            log.debug("submitToAADCommittee ::: find workCasePrescreen : {}", workCasePrescreen);
        }

        if(appraisal != null){
            User aadCommittee = userDAO.findById(aadCommitteeUserId);
            appraisal.setAadCommittee(aadCommittee);
            appraisalDAO.persist(appraisal);
            //Save appointment date for appraisal work case
            if(!Util.isEmpty(appNumber)){
                WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByAppNumber(appNumber);
                log.debug("submitToAADCommittee ::: find workCaseAppraisal : {}", workCaseAppraisal);
                workCaseAppraisal.setAppointmentDate(appraisal.getAppointmentDate());
                workCaseAppraisalDAO.persist(workCaseAppraisal);
                log.debug("submitToAADCommittee ::: save workCaseAppraisal : {}", workCaseAppraisal);
                long appraisalLocationCode = 0;
                if(appraisal.getLocationOfProperty() != null){
                    appraisalLocationCode = appraisal.getLocationOfProperty().getCode();
                }
                bpmExecutor.submitAADCommittee(appNumber, aadCommitteeUserId, appraisal.getAppointmentDate(), appraisalLocationCode, queueName, ActionCode.SUBMIT_TO_ADD_COMMITTEE.getVal(), workCaseAppraisal.getWobNumber());
            }
        }
        log.debug("submitToAADCommittee ::: end...");
    }

    public void submitUW2FromAAD(){

    }

    public void calculatePricingDOA(long workCaseId){
        //List of Credit detail
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        List<NewCreditDetail> newCreditDetailList = newCreditFacility.getNewCreditDetailList();
        //List of Credit tier ( find by Credit detail )
        BigDecimal priceReduceDOA = newCreditFacility.getIntFeeDOA();
        BigDecimal frontEndFeeReduceDOA = newCreditFacility.getFrontendFeeDOA();

        if(priceReduceDOA.compareTo(BigDecimal.ONE) > 0 || priceReduceDOA.compareTo(BigDecimal.ZERO) == 0){

        } else {

        }

        for(NewCreditDetail newCreditDetail : newCreditDetailList){
            BigDecimal standardPrice = null;
            BigDecimal suggestPrice = null;
            BigDecimal finalPrice = null;
            BigDecimal tmpStandardPrice = null;
            BigDecimal tmpSuggestPrice = null;
            BigDecimal tmpFinalPrice = null;
            int reducePricing = newCreditDetail.getReducePriceFlag();
            int reduceFrontEndFee = newCreditDetail.getReduceFrontEndFee();
            for(NewCreditTierDetail newCreditTierDetail : newCreditDetail.getProposeCreditTierDetailList()){
                //Check for Final Price first...
                if(finalPrice != null){
                    tmpFinalPrice = newCreditTierDetail.getFinalInterest().add(newCreditTierDetail.getFinalBasePrice().getValue());
                    tmpStandardPrice = newCreditTierDetail.getStandardInterest().add(newCreditTierDetail.getStandardBasePrice().getValue());
                    tmpSuggestPrice = newCreditTierDetail.getSuggestInterest().add(newCreditTierDetail.getSuggestBasePrice().getValue());

                    if(reducePricing == 1){
                        tmpFinalPrice = tmpFinalPrice.subtract(priceReduceDOA);
                    }
                    if(tmpFinalPrice.compareTo(finalPrice) > 0){
                        finalPrice = tmpFinalPrice;
                        standardPrice = tmpStandardPrice;
                        suggestPrice = tmpSuggestPrice;
                    }
                }else{
                    finalPrice = newCreditTierDetail.getFinalInterest().add(newCreditTierDetail.getFinalBasePrice().getValue());
                    standardPrice = newCreditTierDetail.getStandardInterest().add(newCreditTierDetail.getStandardBasePrice().getValue());
                    suggestPrice = newCreditTierDetail.getSuggestInterest().add(newCreditTierDetail.getSuggestBasePrice().getValue());
                    if(reducePricing == 1){
                        finalPrice = finalPrice.subtract(priceReduceDOA);
                    }
                    //Check for Exceptional flow (CSSO DOA Only)
                    if(priceReduceDOA.compareTo(BigDecimal.ZERO) > 0 && priceReduceDOA.compareTo(BigDecimal.ONE) <= 0){
                        if(finalPrice.compareTo(suggestPrice) < 0){
                            //DOA is CSSO only

                        }
                    }
                }
            }

        }

    }
}
