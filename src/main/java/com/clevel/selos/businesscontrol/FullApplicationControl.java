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
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.transform.StepTransform;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.DateTimeUtil;
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
    @NormalMessage
    private Message msg;

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
        List<User> userList = null;
        List<UserTeam> userTeamList = relTeamUserDetailsDAO.getTeamHeadLeadByTeamId(currentUser.getTeam().getId());
        if(userTeamList != null && userTeamList.size() > 0){
            userList = userDAO.findUserList(userTeamList);
        }

        if(userList == null){
            userList = new ArrayList<User>();
        }

        return userList;
    }

    public List<User> getUserListByRole(RoleValue roleValue){
        List<User> userList = userDAO.findUserListByRoleId(getCurrentUser(), roleValue.id());
        if(userList == null){
            userList = new ArrayList<User>();
        }

        return userList;
    }

    public void assignToABDM(String queueName, String wobNumber, String abdmUserId) throws Exception {
        bpmExecutor.assignToABDM(queueName, wobNumber, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    public void submitToZM(String queueName, String wobNumber, String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, String submitRemark, long workCaseId) throws Exception {
        WorkCase workCase = null;
        String productGroup = "";
        int requestType = 0;
        String deviationCode = "";
        String resultCode = "G"; //TODO: get result code
        BigDecimal totalCommercial = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;
        User user = getCurrentUser();
        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null && workCase.getProductGroup() != null){
                productGroup = workCase.getProductGroup().getName();
                requestType = workCase.getRequestType().getId();

                //TODO: get total com and retail

                if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase("R")){
                    deviationCode = "AD"; //TODO:
                }

                bpmExecutor.submitZM(queueName, wobNumber, zmUserId, rgmUserId, ghUserId, cssoUserId, totalCommercial, totalRetail, resultCode, productGroup, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());

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
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    public void submitToRM(String queueName, String wobNumber, long workCaseId) throws Exception {
        WorkCase workCase;
        String zmDecisionFlag = "";
        String zmPricingRequestFlag = "";
        BigDecimal totalCommercial = BigDecimal.ZERO; //TODO
        BigDecimal totalRetail = BigDecimal.ZERO; //TODO
        String resultCode = "G"; //TODO
        String deviationCode = ""; //TODO
        int requestType = 0;
        int priceDOALevel = 0;
        ApprovalHistory approvalHistoryEndorseCA = null;
        ApprovalHistory approvalHistoryEndorsePricing = null;
        boolean isPricingRequest = false;
        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            priceDOALevel = workCase.getPricingDoaLevel();
            isPricingRequest = Util.isTrue(workCase.getRequestPricing());
            if(workCase != null && workCase.getProductGroup() != null){
                requestType = workCase.getRequestType().getId();
                if(isPricingRequest){
                    approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());
                    //--Check for Decision from Zone --//
                    if(approvalHistoryEndorseCA != null){
                        if(approvalHistoryEndorseCA.getApproveDecision() != DecisionType.NO_DECISION.value()){
                            zmDecisionFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                            approvalHistoryEndorseCA.setSubmit(1);
                            approvalHistoryEndorseCA.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision ( Endorse CA ) before submit.");
                        }
                    } else {
                        throw new Exception("Please make decision before submit.");
                    }
                    //--Check for Decision (Pricing) from Zone--//
                    if(approvalHistoryEndorsePricing != null){
                        if(approvalHistoryEndorsePricing.getApproveDecision() != DecisionType.NO_DECISION.value()){
                            if(approvalHistoryEndorseCA.getApproveDecision() == DecisionType.REJECTED.value()){
                                zmPricingRequestFlag = "NA";
                            } else {
                                if(priceDOALevel > PricingDOAValue.ZM_DOA.value()){
                                    zmPricingRequestFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"E":"R";
                                } else {
                                    zmPricingRequestFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                                }
                            }
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());
                        } else {
                            throw new Exception("Please make decision ( Endorse Pricing ) before submit.");
                        }
                    } else {
                        throw new Exception("Please make decision ( Endorse Pricing ) before submit.");
                    }

                } else {
                    approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
                    if(approvalHistoryEndorseCA==null){
                        throw new Exception("Please make decision before submit.");
                    } else {
                        if(approvalHistoryEndorseCA.getApproveDecision()==0){
                            throw new Exception("Please make decision before submit.");
                        }

                        zmDecisionFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                        zmPricingRequestFlag = "NA";
                        approvalHistoryEndorseCA.setSubmit(1);
                        approvalHistoryEndorseCA.setSubmitDate(new Date());
                    }
                }
                if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase("R")){
                    deviationCode = "AD"; //TODO:
                }

                bpmExecutor.submitRM(queueName, wobNumber, zmDecisionFlag, zmPricingRequestFlag, totalCommercial, totalRetail, resultCode, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());

                approvalHistoryDAO.persist(approvalHistoryEndorseCA);
                if(isPricingRequest){
                    approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
                }
            }

        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    public void submitToGH(String queueName, String wobNumber, long workCaseId) throws Exception {
        String rgmDecisionFlag = "E"; //TODO
        int priceDOALevel = 0;
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            priceDOALevel = workCase.getPricingDoaLevel();
            if(workCase != null && workCase.getProductGroup() != null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != DecisionType.NO_DECISION.value()){
                            if(priceDOALevel>PricingDOAValue.RGM_DOA.value()){
                                rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"E":"R";
                            } else {
                                rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
                            }
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());

                            bpmExecutor.submitGH(queueName, wobNumber, rgmDecisionFlag, ActionCode.SUBMIT_CA.getVal());
                            approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
                        } else {
                            throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                        }
                    }
                }
            } else {
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    public void submitToCSSO(String queueName, String wobNumber, long workCaseId) throws Exception {
        String ghDecisionFlag = "E"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing = null;
        int priceDOALevel = 0;

        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            priceDOALevel = workCase.getPricingDoaLevel();
            if(workCase != null && workCase.getProductGroup()!=null){
                if(Util.isTrue(workCase.getRequestPricing())){
                    approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                    if(approvalHistoryEndorsePricing==null){
                        throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                    } else {
                        if(approvalHistoryEndorsePricing.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                            if(priceDOALevel > PricingDOAValue.GH_DOA.value()){
                                ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"E":"R";
                            } else {
                                ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
                            }
                            approvalHistoryEndorsePricing.setSubmit(1);
                            approvalHistoryEndorsePricing.setSubmitDate(new Date());

                            bpmExecutor.submitCSSO(queueName, wobNumber, ghDecisionFlag, ActionCode.SUBMIT_CA.getVal());
                            approvalHistoryDAO.persist(approvalHistoryEndorsePricing);

                        } else {
                            throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                        }
                    }
                }
            } else {
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }


    }

    public void submitToRGMPriceReduce(String queueName, String wobNumber, long workCaseId) throws Exception {
        String zmPricingRequestFlag = "A";
        WorkCase workCase;
        ApprovalHistory approvalHistoryApprove = null;

        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            int priceDOALevel = workCase.getPricingDoaLevel();
            approvalHistoryApprove = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());
            if(approvalHistoryApprove==null){
                throw new Exception("Please make decision before submit.");
            } else {
                if(approvalHistoryApprove.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                    if(priceDOALevel > PricingDOAValue.ZM_DOA.value()){
                        zmPricingRequestFlag = approvalHistoryApprove.getApproveDecision()==DecisionType.APPROVED.value()?"E":"R";
                    } else {
                        zmPricingRequestFlag = approvalHistoryApprove.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                    }
                    approvalHistoryApprove.setSubmit(1);
                    approvalHistoryApprove.setSubmitDate(new Date());
                } else {
                    throw new Exception("Please make decision before submit.");
                }

                bpmExecutor.submitRGMPriceReduce(queueName, wobNumber, zmPricingRequestFlag, ActionCode.SUBMIT_CA.getVal());

                approvalHistoryDAO.persist(approvalHistoryApprove);
            }
        }
    }

    public void submitFCashZM(String queueName, String wobNumber, long workCaseId) throws Exception {
        String zmDecisionFlag;
        ApprovalHistory approvalHistoryApprove = null;

        if(workCaseId != 0){
            approvalHistoryApprove = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
            if(approvalHistoryApprove==null){
                throw new Exception("Please make decision before submit.");
            } else {
                if(approvalHistoryApprove.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                    zmDecisionFlag = approvalHistoryApprove.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                    approvalHistoryApprove.setSubmit(1);
                    approvalHistoryApprove.setSubmitDate(new Date());
                } else {
                    throw new Exception("Please make decision before submit.");
                }
                log.debug("submitFCashZM ::: approvalHistory : {}", approvalHistoryApprove);
                bpmExecutor.submitFCashZM(queueName, wobNumber, zmDecisionFlag, ActionCode.SUBMIT_CA.getVal());

                approvalHistoryDAO.persist(approvalHistoryApprove);
            }
        }else{
            log.debug("submitFCashZM ::: workCaseId : {}", workCaseId);
            throw new Exception("Exception while Submit Case, Could not found Work Item.");
        }
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
                            cssoDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
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
                            zmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
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
                decisionFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                approvalHistoryEndorseCA.setSubmit(1);
                approvalHistoryEndorseCA.setSubmitDate(new Date());
                approvalHistoryEndorseCA.setComments(remark);
            }
        }

        bpmExecutor.submitUW2(workCaseId, queueName, uw2Name, authorizationDOA.getDescription(), decisionFlag, haveRG001, ActionCode.SUBMIT_CA.getVal());
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
                decisionFlag = approvalHistoryEndorseCA.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
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

    //Request appraisal after Customer Acceptance
    public void requestAppraisal(long workCasePreScreenId, long workCaseId, String queueName, String wobNumber, String aadAdminUserName) throws Exception{
        //Update Request Appraisal Flag

        if(!Util.isEmpty(queueName) && !Util.isEmpty(wobNumber)) {
            try{
                log.debug("requestAppraisal ::: Create WorkCaseAppraisal Complete.");
                bpmExecutor.requestAppraisal(queueName, wobNumber, aadAdminUserName, ActionCode.REQUEST_APPRAISAL.getVal());
                log.debug("requestAppraisal ::: Create Work Item for appraisal complete.");
            } catch (Exception ex){
                log.error("Exception while Create Work Item for Appraisal. : ", ex);
                throw ex;
            }
        } else {
            log.error("exception while Request Appraisal (BDM), Could not find WobNumber/QueueName.");
            throw new Exception("exception while Request Appraisal, Could not find WobNumber/QueueName.");
        }
        log.debug("requestAppraisal ::: Update Request Appraisal Flag Complete.");
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
        workCaseAppraisal.setAppraisalResult(1);
        workCaseAppraisalDAO.persist(workCaseAppraisal);

        return workCaseAppraisal;
    }

    public boolean checkAppraisalInformation(long workCaseId){
        Appraisal appraisal = null;
        boolean checkAppraisal = false;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            log.debug("checkAppraisalInformation ::: find appraisal by workCase : {}", appraisal);
            if(!Util.isNull(appraisal)){
                checkAppraisal = true;
            }
        }

        return checkAppraisal;
    }

    public boolean checkAppointmentInformation(long workCaseId, long workCasePreScreenId){
        Appraisal appraisal = null;
        boolean checkAppointment = false;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            log.debug("checkAppointmentInformation ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("checkAppointmentInformation ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        if(appraisal != null){
            if(!Util.isNull(appraisal.getAppointmentDate()) && !Util.isEmpty(appraisal.getAppointmentCusName())){
                checkAppointment = true;
            }
        }

        return checkAppointment;
    }

    public void submitToAADCommittee(String aadCommitteeUserId, long workCaseId, long workCasePreScreenId, String queueName, String wobNumber) throws Exception{
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
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
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
                if(!Util.isNull(workCaseAppraisal)) {
                    workCaseAppraisal.setAppointmentDate(appraisal.getAppointmentDate());
                    workCaseAppraisalDAO.persist(workCaseAppraisal);
                    log.debug("submitToAADCommittee ::: save workCaseAppraisal : {}", workCaseAppraisal);
                }
                long appraisalLocationCode = 0;
                if(appraisal.getLocationOfProperty() != null){
                    appraisalLocationCode = appraisal.getLocationOfProperty().getCode();
                }
                bpmExecutor.submitAADCommittee(appNumber, aadCommitteeUserId, DateTimeUtil.convertDateWorkFlowFormat(appraisal.getAppointmentDate()), appraisalLocationCode, queueName, ActionCode.SUBMIT_CA.getVal(), wobNumber);
            }
        } else {
            throw new Exception("Submit case failed, could not find appraisal data.");
        }
        log.debug("submitToAADCommittee ::: end...");
    }

    public void submitToUWFromCommittee(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitUW2FromCommittee(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }

    public void returnAADCommittee(String queueName, String wobNumber, String reason, String remark) throws Exception {
        bpmExecutor.returnCase(queueName, wobNumber, remark, reason, ActionCode.RETURN_TO_AAD_ADMIN.getVal());
    }

    public String getAADAdmin(long workCaseId, long workCasePreScreenId){
        String aadAdminName = "";
        Appraisal appraisal;
        if(!Util.isNull(workCaseId) && !Util.isZero(workCaseId)){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            aadAdminName = appraisal.getAadAdmin()!=null?appraisal.getAadAdmin().getUserName():"";
            log.debug("submitToAADCommittee ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && !Util.isZero(workCasePreScreenId)){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            aadAdminName = appraisal.getAadAdmin()!=null?appraisal.getAadAdmin().getUserName():"";
            log.debug("submitToAADCommittee ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        return aadAdminName;
    }

    public String getAADCommittee(long workCaseId, long workCasePreScreenId){
        String aadCommitteeName = "";
        Appraisal appraisal;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            aadCommitteeName = appraisal.getAadCommittee()!=null?appraisal.getAadCommittee().getUserName():"";
            log.debug("submitToAADCommittee ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            aadCommitteeName = appraisal.getAadCommittee()!=null?appraisal.getAadCommittee().getUserName():"";
            log.debug("submitToAADCommittee ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        return aadCommitteeName;
    }

    public void submitUW2FromAAD(){

    }

    public void submitCustomerAcceptance(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCustomerAcceptance(queueName, wobNumber, ActionCode.CUSTOMER_ACCEPT.getVal());
    }

    public void submitPendingDecision(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.submitPendingDecision(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.PENDING_FOR_DECISION.getVal());
    }

    public void submitRequestPriceReduction(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.REQUEST_PRICE_REDUCE.getVal());
    }

    public void returnBDMByAAD(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.returnCase(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.RETURN_TO_BDM.getVal());
    }

    public void returnAADAdminByAADCommittee(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.returnCase(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.RETURN_TO_AAD_ADMIN.getVal());
    }

    public void returnAADAdminByBDM(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.RETURN_TO_AAD_ADMIN.getVal());
    }

    public void returnAADAdminByUW2(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.returnCase(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.RETURN_TO_AAD_ADMIN.getVal());
    }

    public void completeCase(String queueName, String wobNumber) throws Exception {
        bpmExecutor.completeCase(queueName, ActionCode.COMPLETE.getVal(), wobNumber);
    }

    public void restartCase(String queueName, String wobNumber) throws Exception {
        bpmExecutor.restartCase(queueName, ActionCode.RESTART.getVal(), wobNumber);
    }

    public void submitToBDM(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }

    public void calculateApprovedPricingDOA(long workCaseId){
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        calculatePricingDOA(workCaseId, newCreditFacility);
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

            //Check for Exceptional Case
            boolean exceptionalFlow = false;
            if(newCreditDetailList != null && newCreditDetailList.size() > 0){
                //Check for Request Price Reduction
                for(NewCreditDetail itemCreditDetail : newCreditDetailList){
                    if(itemCreditDetail.getReduceFrontEndFee() == 1 || itemCreditDetail.getReducePriceFlag() == 1){
                        requestPricing = 1;
                        break;
                    }
                }

                for(NewCreditDetail newCreditDetail : newCreditDetailList){
                    BigDecimal standardPrice = null;
                    BigDecimal suggestPrice = null;
                    BigDecimal finalPrice = null;
                    BigDecimal tmpStandardPrice = null;
                    BigDecimal tmpSuggestPrice = null;
                    BigDecimal tmpFinalPrice = null;
                    BigDecimal tmpBigDecimal = null;
                    int reducePricing = newCreditDetail.getReducePriceFlag();
                    //int reduceFrontEndFee = newCreditDetail.getReduceFrontEndFee();
                    if(newCreditDetail.getReduceFrontEndFee() == 1 || newCreditDetail.getReducePriceFlag() == 1){
                        if(newCreditDetail.getProposeCreditTierDetailList() != null){
                            for(NewCreditTierDetail newCreditTierDetail : newCreditDetail.getProposeCreditTierDetailList()){
                                //Check for Final Price first...
                                if(finalPrice != null){
                                    tmpFinalPrice = newCreditTierDetail.getFinalInterest().add(newCreditTierDetail.getFinalBasePrice().getValue());
                                    tmpStandardPrice = newCreditTierDetail.getStandardInterest().add(newCreditTierDetail.getStandardBasePrice().getValue());
                                    tmpSuggestPrice = newCreditTierDetail.getSuggestInterest().add(newCreditTierDetail.getSuggestBasePrice().getValue());

                                    if(tmpStandardPrice.compareTo(tmpSuggestPrice) > 0){
                                        tmpBigDecimal = tmpStandardPrice;
                                    }else{
                                        tmpBigDecimal = tmpSuggestPrice;
                                    }

                                    if(reducePricing == 1){
                                        tmpFinalPrice = tmpBigDecimal.subtract(priceReduceDOA);
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

                                    if(standardPrice.compareTo(suggestPrice) > 0){
                                        tmpBigDecimal = standardPrice;
                                    }else{
                                        tmpBigDecimal = suggestPrice;
                                    }

                                    if(reducePricing == 1){
                                        finalPrice = tmpBigDecimal.subtract(priceReduceDOA);
                                    }
                                }

                                if(finalPrice.compareTo(suggestPrice) < 0){
                                    exceptionalFlow = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(exceptionalFlow){
                        break;
                    }
                }
            }

            if(!exceptionalFlow){
                //Checking for GenericDOA
                if(priceReduceDOA != null && frontEndFeeReduceDOA != null){
                    if(priceReduceDOA.compareTo(BigDecimal.ONE) <= 0){
                        if(frontEndFeeReduceDOA.compareTo(BigDecimal.ONE) > 0){
                            //GH DOA Level
                            pricingDOALevel = PricingDOAValue.GH_DOA;
                            log.debug("calculatePricingDOA Level [GROUP HEAD] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                        } else {
                            //RGM DOA Level
                            pricingDOALevel = PricingDOAValue.RGM_DOA;
                            log.debug("calculatePricingDOA Level [REGION MANAGER] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                        }

                    } else {
                        //CSSO DOA Level
                        pricingDOALevel = PricingDOAValue.CSSO_DOA;
                        log.debug("calculatePricingDOA Level [CSSO MANAGER] ::: priceReduceDOA : {}, frontEndFeeReduceDOA : {}", priceReduceDOA, frontEndFeeReduceDOA);
                    }
                }

            } else {
                pricingDOALevel = PricingDOAValue.CSSO_DOA;
            }

            /*if(pricingDOALevel == PricingDOAValue.NO_DOA){
                requestPricing = 0;
            }*/

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

    public void cancelCAFullApp(String queueName, String wobNumber, int reasonId, String remark) throws Exception {
        bpmExecutor.cancelCase(queueName, wobNumber, ActionCode.CANCEL_CA.getVal(), getReasonDescription(reasonId), remark);
    }

    public void cancelRequestPriceReduction(String queueName, String wobNumber, int reasonId, String remark) throws Exception {
        bpmExecutor.cancelRequestPriceReduction(queueName, wobNumber, getReasonDescription(reasonId), remark, ActionCode.CANCEL_REQUEST_PRICE_REDUCTION.getVal());
    }

    public List<Reason> getReasonList(ReasonTypeValue reasonTypeValue){
        ReasonType reasonType = reasonTypeDAO.findById(reasonTypeValue.value());
        List<Reason> reasonList = reasonDAO.getList(reasonType);
        if(Util.isNull(reasonList)){
            reasonList = new ArrayList<Reason>();
        }

        return reasonList;
    }

    public String getReasonDescription(int reasonId){
        String reasonDescription = "";
        if(!Util.isZero(reasonId)){
            try {
                Reason reason = reasonDAO.findById(reasonId);
                if (!Util.isNull(reason)) {
                    reasonDescription = reason.getCode() != null ? reason.getCode() : "";
                    reasonDescription = reason.getDescription() != null ? reasonDescription.concat(" - ").concat(reason.getDescription()) : reasonDescription;
                }
            } catch (Exception ex) {
                log.error("Exception while get reason description : ", ex);
            }
        }

        return reasonDescription;
    }
}
