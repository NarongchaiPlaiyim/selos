package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelTeamUserDetailsDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.PricingDOAValue;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.transform.StepTransform;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    ReasonDAO reasonDAO;
    @Inject
    ReasonTypeDAO reasonTypeDAO;
    @Inject
    ReturnInfoDAO returnInfoDAO;
    @Inject
    ReturnInfoHistoryDAO returnInfoHistoryDAO;
    @Inject
    ReturnInfoTransform returnInfoTransform;
    @Inject
    UserTransform userTransform;
    @Inject
    StepTransform stepTransform;
    @Inject
    StepDAO stepDAO;
    @Inject
    RelTeamUserDetailsDAO relTeamUserDetailsDAO;

    @Inject
    AppraisalRequestControl appraisalRequestControl;

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

    public List<User> getUserList(User currentUser){
        List<User> zmUserList = null;
        List<UserTeam> zmUserTeamList = relTeamUserDetailsDAO.getTeamHeadLeadByTeamId(currentUser.getTeam().getId());
        if(zmUserTeamList!=null && zmUserTeamList.size()>0){
            zmUserList = userDAO.findUserZoneList(zmUserTeamList);
        }

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

    public List<User> getCSSOUserList(){
        User currentUser = getCurrentUser();

        List<User> ghUserList = userDAO.findCSSOList(currentUser);
        if(ghUserList == null){
            ghUserList = new ArrayList<User>();
        }

        return ghUserList;
    }

    public void assignToABDM(String abdmUserId, String queueName, long workCaseId) throws Exception {
        bpmExecutor.assignToABDM(workCaseId, queueName, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    public void submitToZMPricing(String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, String queueName, long workCaseId) throws Exception {
        WorkCase workCase;
        String productGroup = "";
        int requestType = 0;
        String deviationCode = "";
        String resultCode = "G"; //TODO: get result code
        BigDecimal totalCommercial = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase.getProductGroup()!=null){
                productGroup = workCase.getProductGroup().getName();
                requestType = workCase.getRequestType().getId();
            }
        }

        if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase("R")){
            deviationCode = "AD"; //TODO:
        }

        //TODO: get total com and retail

        bpmExecutor.submitZM(workCaseId, queueName, zmUserId, rgmUserId, ghUserId, cssoUserId, totalCommercial, totalRetail, resultCode, productGroup, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());
    }

    public void submitToRM(String queueName, long workCaseId) throws Exception {
        WorkCase workCase;
        String zmDecisionFlag = "A"; //TODO
        String zmPricingRequestFlag = "A"; //TODO
        BigDecimal totalCommercial = BigDecimal.ZERO; //TODO
        BigDecimal totalRetail = BigDecimal.ZERO; //TODO
        String resultCode = "G"; //TODO
        String deviationCode = ""; //TODO
        int requestType = 0;

        //TODO: verify decision flag. if not exist, throws Exception to controller

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase.getProductGroup()!=null){
                requestType = workCase.getRequestType().getId();
            }
        }

        if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase("R")){
            deviationCode = "AD"; //TODO:
        }

        bpmExecutor.submitRM(workCaseId, queueName, zmDecisionFlag, zmPricingRequestFlag, totalCommercial, totalRetail, resultCode, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());
    }

    public void submitToGH(String queueName, long workCaseId) throws Exception {
        String rgmDecisionFlag = "A"; //TODO

        //TODO: verify decision flag. if not exist, throws Exception to controller

        bpmExecutor.submitGH(workCaseId, queueName, rgmDecisionFlag, ActionCode.SUBMIT_CA.getVal());
    }

    public void submitToCSSO(String queueName, long workCaseId) throws Exception {
        String ghDecisionFlag = "A"; //TODO

        //TODO: verify decision flag. if not exist, throws Exception to controller

        bpmExecutor.submitCSSO(workCaseId, queueName, ghDecisionFlag, ActionCode.SUBMIT_CA.getVal());
    }

    public void submitToUWFromCSSO(String queueName, long workCaseId) throws Exception {
        String cssoDecisionFlag = "A"; //TODO

        //TODO: verify decision flag. if not exist, throws Exception to controller

        bpmExecutor.submitUWFromCSSO(workCaseId, queueName, cssoDecisionFlag, ActionCode.SUBMIT_CA.getVal());
    }

    public void submitToUW2(String queueName, long workCaseId) throws Exception {
        String assignToUW2Name = ""; //TODO
        String levelDOAForUW2 = ""; //TODO
        String decisionFlag = "A"; //TODO
        String haveRG001 = "N"; //TODO

        //TODO: verify data before execute BPM. If not valid, throws Exception to controller

        bpmExecutor.submitUW2(workCaseId, queueName, assignToUW2Name, levelDOAForUW2, decisionFlag, haveRG001, ActionCode.SUBMIT_TO_UW2.getVal());
    }

    public void submitCA(String queueName, long workCaseId) throws Exception {
        String decisionFlag = "A"; //TODO
        String haveRG001 = "N"; //TODO

        //TODO: verify data before execute BPM. If not valid, throws Exception to controller

        bpmExecutor.submitCA(workCaseId, queueName, decisionFlag, haveRG001, ActionCode.SUBMIT_CA.getVal());
    }

    /*public void requestAppraisalBDM(long workCasePreScreenId, long workCaseId) throws Exception{
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
    }*/

    public void requestAppraisal(AppraisalView appraisalView, long workCasePreScreenId, long workCaseId) throws Exception{
        //Update Request Appraisal Flag
        WorkCasePrescreen workCasePrescreen;
        WorkCase workCase;
        String appNumber = "";
        ProductGroup productGroup = null;
        RequestType requestType = null;

        //Create WorkCaseAppraisal
        WorkCaseAppraisal workCaseAppraisal = createWorkCaseAppraisal(workCasePreScreenId, workCaseId);
        log.debug("requestAppraisal ::: Create WorkCaseAppraisal Complete.");

        log.debug("requestAppraisal ::: workCaseAppraisal : {}", workCaseAppraisal);
        try{
            bpmExecutor.requestAppraisal(workCaseAppraisal.getAppNumber(), "", workCaseAppraisal.getProductGroup().getName(), workCaseAppraisal.getRequestType().getId(), getCurrentUserID());
            log.debug("requestAppraisal ::: Create Work Item for appraisal complete.");
        } catch (Exception ex){
            log.error("Exception while Create Work Item for Appraisal.");
            workCaseAppraisalDAO.delete(workCaseAppraisal);
            throw new Exception("Exception while Create Work Item for Appraisal.");
        }

        if(workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

            if(workCasePrescreen != null){
                workCasePrescreen.setRequestAppraisal(1);
                appNumber = workCasePrescreen.getAppNumber();
                productGroup = workCasePrescreen.getProductGroup();
                requestType = workCasePrescreen.getRequestType();
                workCasePrescreenDAO.persist(workCasePrescreen);
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from prescreen");
            }
        } else if(workCaseId != 0) {
            workCase = workCaseDAO.findById(workCaseId);

            if(workCase != null){
                workCase.setRequestAppraisal(1);
                appNumber = workCase.getAppNumber();
                productGroup = workCase.getProductGroup();
                requestType = workCase.getRequestType();
                workCaseDAO.persist(workCase);
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from full application");
            }
        } else {
            log.error("exception while Request Appraisal (BDM), can not find workcase or workcaseprescreen.");
            throw new Exception("exception while Request Appraisal, can not find case.");
        }
        log.debug("requestAppraisal ::: Update Request Appraisal Flag Complete.");

        //Save Appraisal Request Detail
        appraisalRequestControl.onSaveAppraisalRequest(appraisalView, workCaseId, workCasePreScreenId);
        log.debug("requestAppraisal ::: Save Appraisal Request Complete.");
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

    public PricingDOAValue calculatePricingDOA(long workCaseId){
        PricingDOAValue pricingDOALevel = null;
        //List of Credit detail
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        List<NewCreditDetail> newCreditDetailList = newCreditFacility.getNewCreditDetailList();
        //List of Credit tier ( find by Credit detail )
        BigDecimal priceReduceDOA = newCreditFacility.getIntFeeDOA();
        BigDecimal frontEndFeeReduceDOA = newCreditFacility.getFrontendFeeDOA();

        if(priceReduceDOA != null && frontEndFeeReduceDOA != null){
            if(priceReduceDOA.compareTo(BigDecimal.ONE) >= 0 || priceReduceDOA.compareTo(BigDecimal.ZERO) == 0){
                //Do not Check for exceptional flow
                if(priceReduceDOA.compareTo(BigDecimal.ZERO) == 0 || frontEndFeeReduceDOA.compareTo(new BigDecimal("0.75")) <= 0){
                    //DOA Level equals ZM
                    pricingDOALevel = PricingDOAValue.ZM_DOA;
                    log.debug("calculatePricingDOA Level [ZONE MANAGER] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                }else if(priceReduceDOA.compareTo(BigDecimal.ONE) > 0 || frontEndFeeReduceDOA.compareTo(BigDecimal.ONE) > 0){
                    //DOA Level equal CSSO
                    pricingDOALevel = PricingDOAValue.CSSO_DOA;
                    log.debug("calculatePricingDOA Level [CSSO] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                }
            } else {
                //Check for exceptional flow
                boolean exceptionalFlow = false;
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
                                    pricingDOALevel = PricingDOAValue.CSSO_DOA;
                                }
                            }
                        }

                        if(finalPrice.compareTo(suggestPrice) < 0){
                            exceptionalFlow = true;
                            break;
                        }
                    }
                    if(exceptionalFlow){
                        break;
                    }
                }
            }
        }

        return pricingDOALevel;
    }

    public List<Reason> getCancelReasonList(){
        List<Reason> reasons = reasonDAO.getCancelList();
        if(reasons == null){
            reasons = new ArrayList<Reason>();
        }

        return reasons;
    }

    public void cancelCAFullApp(long workCaseId, String queueName, int reasonId, String remark) throws Exception {
        String reasonTxt = "";
        if(reasonId!=0){
            Reason reason = reasonDAO.findById(reasonId);
            if(reason!=null && reason.getId()!=0){
                reasonTxt = reason.getCode().concat(" - ").concat(reason.getDescription());
            }
        }
        bpmExecutor.cancelCase(0, workCaseId, queueName, ActionCode.CANCEL_CA.getVal(), reasonTxt, remark);
    }
}
