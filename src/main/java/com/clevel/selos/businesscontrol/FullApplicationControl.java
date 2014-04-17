package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelTeamUserDetailsDAO;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.UserToAuthorizationDOA;
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
    ReasonDAO reasonDAO;
    @Inject
    ReasonTypeDAO reasonTypeDAO;
    @Inject
    ReturnInfoDAO returnInfoDAO;
    @Inject
    CustomerDAO customerDAO;
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
    ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    AuthorizationDOADAO authorizationDOADAO;
    @Inject
    UserToAuthorizationDOADAO userToAuthorizationDOADAO;

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

    public void submitToZMPricing(String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, String submitRemark, String queueName, long workCaseId) throws Exception {
        WorkCase workCase = null;
        String productGroup = "";
        int requestType = 0;
        String deviationCode = "";
        String resultCode = "G"; //TODO: get result code
        BigDecimal totalCommercial = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;
        User user = getCurrentUser();
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

        //Insert Approval History
        ApprovalHistory approvalHistory = new ApprovalHistory();
        approvalHistory.setComments(submitRemark);
        approvalHistory.setRole(user.getRole());
        approvalHistory.setStep(workCase.getStep());
        approvalHistory.setSubmit(1);
        approvalHistory.setSubmitDate(new Date());
        approvalHistory.setUser(user);
        approvalHistory.setWorkCase(workCase);
        approvalHistoryDAO.persist(approvalHistory);
    }

    public void submitToRM(String queueName, long workCaseId) throws Exception {
        WorkCase workCase;
        String zmDecisionFlag = "A"; //TODO
        String zmPricingRequestFlag = "E"; //TODO
        BigDecimal totalCommercial = BigDecimal.ZERO; //TODO
        BigDecimal totalRetail = BigDecimal.ZERO; //TODO
        String resultCode = "G"; //TODO
        String deviationCode = ""; //TODO
        //int requestType = 1; //TEMPORARY
        int requestType = 0;
        ApprovalHistory approvalHistoryEndorseCA = null;
        ApprovalHistory approvalHistoryEndorsePricing = null;
        boolean isPricingRequest = false;
        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            int priceDOALevel = workCase.getPricingDoaLevel();
            isPricingRequest = Util.isTrue(workCase.getRequestPricing());
            if(workCase.getProductGroup()!=null){
                requestType = workCase.getRequestType().getId();
                if(isPricingRequest){
                    approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorseCA==null || approvalHistoryEndorsePricing==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorseCA.getApproveDecision()==0 || approvalHistoryEndorsePricing.getApproveDecision()==0){
                            throw new Exception("Please make decision before submit.");
                        }

                        zmDecisionFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"A";
                        approvalHistoryEndorseCA.setSubmit(1);
                        approvalHistoryEndorseCA.setSubmitDate(new Date());
                        if(priceDOALevel>PricingDOAValue.ZM_DOA.value()){
                            zmPricingRequestFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"E";
                        } else {
                            zmPricingRequestFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"A";
                        }
                        approvalHistoryEndorsePricing.setSubmit(1);
                        approvalHistoryEndorsePricing.setSubmitDate(new Date());
                    }
                } else {
                    approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
                    if(approvalHistoryEndorseCA==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorseCA.getApproveDecision()==0){
                            throw new Exception("Please make decision before submit.");
                        }

                        zmDecisionFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"A";
                        approvalHistoryEndorseCA.setSubmit(1);
                        approvalHistoryEndorseCA.setSubmitDate(new Date());
                    }
                }
            }
        }

        if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase("R")){
            deviationCode = "AD"; //TODO:
        }

        bpmExecutor.submitRM(workCaseId, queueName, zmDecisionFlag, zmPricingRequestFlag, totalCommercial, totalRetail, resultCode, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());

        approvalHistoryDAO.persist(approvalHistoryEndorseCA);
        if(isPricingRequest){
            approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
        }
    }

    public void submitToGH(String queueName, long workCaseId) throws Exception {
        String rgmDecisionFlag = "E"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            int priceDOALevel = workCase.getPricingDoaLevel();
            if(workCase.getProductGroup()!=null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                            if(priceDOALevel>PricingDOAValue.RGM_DOA.value()){
                                rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"E";
                            } else {
                                rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"A";
                            }
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision before submit.");
                        }
                    }
                }
            }
        }

        bpmExecutor.submitGH(workCaseId, queueName, rgmDecisionFlag, ActionCode.SUBMIT_CA.getVal());

        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
    }

    public void submitToCSSO(String queueName, long workCaseId) throws Exception {
        String ghDecisionFlag = "E"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            int priceDOALevel = workCase.getPricingDoaLevel();
            if(workCase.getProductGroup()!=null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                            if(priceDOALevel>PricingDOAValue.GH_DOA.value()){
                                ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"E";
                            } else {
                                ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"A";
                            }
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision before submit.");
                        }
                    }
                }
            }
        }

        bpmExecutor.submitCSSO(workCaseId, queueName, ghDecisionFlag, ActionCode.SUBMIT_CA.getVal());

        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
    }

    public void submitToUWFromCSSO(String queueName, long workCaseId) throws Exception {
        String cssoDecisionFlag = "A"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase.getProductGroup()!=null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != 0){
                            cssoDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"A";
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision before submit.");
                        }
                    }
                }
            }
        }

        bpmExecutor.submitUWFromCSSO(workCaseId, queueName, cssoDecisionFlag, ActionCode.SUBMIT_CA.getVal());

        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
    }

    public void submitToUWFromZM(String queueName, long workCaseId) throws Exception {
        String zmDecisionFlag = "A"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase.getProductGroup()!=null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId,getCurrentUser(),ApprovalType.PRICING_APPROVAL_FINAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != 0){
                            zmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision()==1?"R":"A";
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision before submit.");
                        }
                    }
                }
            }
        }

        bpmExecutor.submitUWFromZM(workCaseId, queueName, zmDecisionFlag, ActionCode.SUBMIT_CA.getVal());

        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
    }

    public void submitToUW2(String uw2Name, long uw2DOALevelId, String remark, String queueName, long workCaseId) throws Exception {
        String decisionFlag = "A";
        String haveRG001 = "N"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorseCA = null;

        AuthorizationDOA authorizationDOA = authorizationDOADAO.findById(uw2DOALevelId);

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());

            if(approvalHistoryEndorseCA==null){
                throw new Exception("Please make decision before submit.");
            } else {
                decisionFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"A";
                approvalHistoryEndorseCA.setSubmit(1);
                approvalHistoryEndorseCA.setSubmitDate(new Date());
                approvalHistoryEndorseCA.setComments(remark);
            }
        }

        bpmExecutor.submitUW2(workCaseId, queueName, uw2Name, authorizationDOA.getDescription(), decisionFlag, haveRG001, ActionCode.SUBMIT_TO_UW2.getVal());
        approvalHistoryDAO.persist(approvalHistoryEndorseCA);
    }

    public void submitCA(String queueName, long workCaseId) throws Exception {
        String decisionFlag = "A";
        String haveRG001 = "N"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorseCA = null;

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId,getCurrentUserID(), ApprovalType.CA_APPROVAL.value());

            if(approvalHistoryEndorseCA==null){
                throw new Exception("Please make decision before submit.");
            } else {
                decisionFlag = approvalHistoryEndorseCA.getApproveDecision()==1?"R":"A";
                approvalHistoryEndorseCA.setSubmit(1);
                approvalHistoryEndorseCA.setSubmitDate(new Date());
            }
        }

        bpmExecutor.submitCA(workCaseId, queueName, decisionFlag, haveRG001, ActionCode.SUBMIT_CA.getVal());
        approvalHistoryDAO.persist(approvalHistoryEndorseCA);
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
            //Get Customer Name
            List<Customer> customerList = customerDAO.getBorrowerByWorkCaseId(workCaseId, workCasePreScreenId);
            String borrowerName = "";
            if(customerList != null && customerList.size() > 0){
                Customer customer = customerList.get(0);
                borrowerName = customer.getNameTh();
                if(customer.getLastNameTh() != null){
                    borrowerName = borrowerName.concat(" ").concat(customer.getLastNameTh());
                }
            }
            bpmExecutor.requestAppraisal(workCaseAppraisal.getAppNumber(), borrowerName, workCaseAppraisal.getProductGroup().getName(), workCaseAppraisal.getRequestType().getId(), getCurrentUserID());
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

    public boolean checkAppointmentInformation(long workCaseId, long workCasePreScreenId){
        Appraisal appraisal = null;
        boolean checkAppointment = false;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            log.debug("checkAppointmentInformation ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCaseId);
            log.debug("checkAppointmentInformation ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        if(appraisal != null){
            if(!Util.isNull(appraisal.getAppointmentDate()) && !Util.isEmpty(appraisal.getAppointmentCusName())){
                checkAppointment = true;
            }
        }

        return checkAppointment;
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

    public void calculatePricingDOA(long workCaseId, NewCreditFacility newCreditFacility){
        log.debug("calculatePricingDOA ::: newCreditFacility : {}", newCreditFacility);
        PricingDOAValue pricingDOALevel = PricingDOAValue.NO_DOA;

        if(newCreditFacility != null){
            //List of Credit detail
            List<NewCreditDetail> newCreditDetailList = newCreditFacility.getNewCreditDetailList();
            //List of Credit tier ( find by Credit detail )
            BigDecimal priceReduceDOA = newCreditFacility.getIntFeeDOA();
            BigDecimal frontEndFeeReduceDOA = newCreditFacility.getFrontendFeeDOA();
            int requestPricing = 0;

            //Check Case Have request pricing or not?
            log.debug("calculatePricingDOA ::: Check Request Pricing and Fee : priceReduce : {}, frontEndFee : {}", priceReduceDOA, frontEndFeeReduceDOA);
            if(priceReduceDOA != null && frontEndFeeReduceDOA != null){
                if(priceReduceDOA.compareTo(BigDecimal.ZERO) == 0
                        && (frontEndFeeReduceDOA.compareTo(BigDecimal.ZERO) >= 0 && frontEndFeeReduceDOA.compareTo(new BigDecimal("0.75")) <= 0)){
                    //DOA Level equals ZM
                    pricingDOALevel = PricingDOAValue.ZM_DOA;
                    requestPricing = 1;
                    log.debug("calculatePricingDOA Level [ZONE MANAGER] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                } else if(priceReduceDOA.compareTo(BigDecimal.ONE) > 0 && frontEndFeeReduceDOA.compareTo(BigDecimal.ONE) > 0) {
                    pricingDOALevel = PricingDOAValue.CSSO_DOA;
                    requestPricing = 1;
                    log.debug("calculatePricingDOA Level [CSSO MANAGER] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                } else {
                    //Check for Exceptional Case
                    boolean exceptionalFlow = false;
                    if(newCreditDetailList != null && newCreditDetailList.size() > 0){
                        for(NewCreditDetail newCreditDetail : newCreditDetailList){
                            BigDecimal standardPrice = null;
                            BigDecimal suggestPrice = null;
                            BigDecimal finalPrice = null;
                            BigDecimal tmpStandardPrice = null;
                            BigDecimal tmpSuggestPrice = null;
                            BigDecimal tmpFinalPrice = null;
                            int reducePricing = newCreditDetail.getReducePriceFlag();
                            int reduceFrontEndFee = newCreditDetail.getReduceFrontEndFee();
                            if(newCreditDetail.getProposeCreditTierDetailList() != null){
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
                                    }

                                    if(finalPrice.compareTo(suggestPrice) < 0){
                                        exceptionalFlow = true;
                                        break;
                                    }
                                }
                            }
                            if(exceptionalFlow){
                                break;
                            }
                        }
                    }

                    if(!exceptionalFlow){
                        if((priceReduceDOA.compareTo(BigDecimal.ZERO) >= 0 && priceReduceDOA.compareTo(new BigDecimal("0.25")) <= 0)
                                && (frontEndFeeReduceDOA.compareTo(BigDecimal.ZERO) > 0 && frontEndFeeReduceDOA.compareTo(BigDecimal.ONE) <= 0)){
                            pricingDOALevel = PricingDOAValue.RGM_DOA;
                            requestPricing = 1;
                        }else if((priceReduceDOA.compareTo(BigDecimal.ZERO) >= 0 && priceReduceDOA.compareTo(BigDecimal.ONE) <= 0)
                                && frontEndFeeReduceDOA.compareTo(BigDecimal.ONE) > 0){
                            pricingDOALevel = PricingDOAValue.GH_DOA;
                            requestPricing = 1;
                        }
                    }
                }
            }

            if(pricingDOALevel == PricingDOAValue.NO_DOA){
                requestPricing = 0;
            }
            log.debug("calculatePricingDOA ::: requestPricing : {}", requestPricing);
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setRequestPricing(requestPricing);
            workCase.setPricingDoaLevel(pricingDOALevel.value());
            workCaseDAO.persist(workCase);
        }
    }

    public boolean getRequestPricing(long workCaseId){
        boolean requestPricing = false;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        try {
            if(!Util.isNull(workCase)){
                requestPricing = Util.isTrue(workCase.getRequestPricing());
            }
        } catch (Exception ex){
            log.error("Exception while getRequestPricing : ", ex);
        }

        return requestPricing;
    }

    public int getPricingDOALevel(long workCaseId) throws Exception{
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase!=null){
            return workCase.getPricingDoaLevel();
        }
        return 0;
    }

    public List<AuthorizationDOA> getAuthorizationDOALevelList(long workCaseId) throws Exception{
        List<AuthorizationDOA> authorizationDOAList = new ArrayList<AuthorizationDOA>();
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase!=null && workCase.getUwDOA2()!=null){
            authorizationDOAList = authorizationDOADAO.getListByPriority(workCase.getUwDOA2().getDoapriorityorder());
            if(authorizationDOAList == null){
                authorizationDOAList = new ArrayList<AuthorizationDOA>();
            }
        }

        return authorizationDOAList;
    }

    public List<User> getUWUserListFromDOALevel(long doaLevelId) throws Exception{
        List<User> authorizationDOAList = userToAuthorizationDOADAO.getUserListFromDOALevel(doaLevelId);
        if(authorizationDOAList == null){
            authorizationDOAList = new ArrayList<User>();
        }
        return authorizationDOAList;
    }

    public List<Reason> getCancelReasonList() throws Exception{
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
