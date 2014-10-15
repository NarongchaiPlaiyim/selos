package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelTeamUserDetailsDAO;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.CustomerTransform;
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
import java.util.Collections;
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
    private UserDAO userDAO;
    @Inject
    private RoleDAO roleDAO;
    @Inject
    private AppraisalDAO appraisalDAO;
    @Inject
    private CancelRejectInfoDAO cancelRejectInfoDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO newCollateralHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO newCollateralSubDAO;
    @Inject
    private ProposeCreditInfoDAO newCreditDetailDAO;
    @Inject
    private ReasonDAO reasonDAO;
    @Inject
    private ReasonTypeDAO reasonTypeDAO;
    @Inject
    private ReturnInfoDAO returnInfoDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ReturnInfoHistoryDAO returnInfoHistoryDAO;
    @Inject
    private WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    private DecisionDAO decisionDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private StatusDAO statusDAO;
    @Inject
    private RelTeamUserDetailsDAO relTeamUserDetailsDAO;
    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    private AuthorizationDOADAO authorizationDOADAO;
    @Inject
    private UserToAuthorizationDOADAO userToAuthorizationDOADAO;
    @Inject
    private CustomerCSIDAO customerCSIDAO;
    @Inject
    private CustomerAccountDAO customerAccountDAO;
    @Inject
    private CustomerAccountNameDAO customerAccountNameDAO;
    @Inject
    private CustomerTransform customerTransform;
    @Inject
    private IndividualDAO individualDAO;
    @Inject
    private JuristicDAO juristicDAO;
    @Inject
    private RLOSInterface rlosInterface;
    @Inject
    private WarningCodeDAO warningCodeDAO;
    @Inject
    private UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject
    private TCGDAO tcgDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    private ProposeGuarantorInfoDAO proposeGuarantorInfoDAO;

    @Inject
    private ReturnInfoTransform returnInfoTransform;
    @Inject
    private UserTransform userTransform;
    @Inject
    private StepTransform stepTransform;

    @Inject
    private AppraisalRequestControl appraisalRequestControl;

    @Inject
    private ReturnControl returnControl;
    @Inject
    private MortgageSummaryControl mortgageSummaryControl;

    @Inject
    private BPMExecutor bpmExecutor;

    @Inject
    private STPExecutor stpExecutor;

    @Inject
    NCBInterface ncbInterface;

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
        log.debug("Assign to ABDM : queueName : {}, wobNumber : {}, abdmUserId : {}", queueName, wobNumber, abdmUserId);
        bpmExecutor.assignToABDM(queueName, wobNumber, abdmUserId, ActionCode.ASSIGN_TO_ABDM.getVal());
    }

    //---------- Function for Submit Case ----------//
    /** Submit CA form ABDM to BDM **/
    public void submitForABDM(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception{
        if(workCaseId != 0) {
            User user = getCurrentUser();
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                bpmExecutor.submitForABDM(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), ActionCode.SUBMIT_CA.getVal());
                //Insert Approval History
                log.debug("submitCAByBDM : add approval History into ApprovalHistory");
                ApprovalHistory approvalHistory = approvalHistoryDAO.findExistHistoryByUser(workCaseId, user.getId());
                if(approvalHistory == null) {
                    log.debug("submitForABDM : add new Approval History");
                    approvalHistory = new ApprovalHistory();
                    approvalHistory.setComments(submitRemark);
                    approvalHistory.setRole(user.getRole());
                    approvalHistory.setStep(workCase.getStep());
                    approvalHistory.setSubmit(1);
                    approvalHistory.setSubmitDate(new Date());
                    approvalHistory.setUser(user);
                    approvalHistory.setWorkCase(workCase);
                } else {
                    log.debug("submitForABDM : replace existing Approval History");
                    approvalHistory.setComments(submitRemark);
                    approvalHistory.setRole(user.getRole());
                    approvalHistory.setStep(workCase.getStep());
                    approvalHistory.setSubmit(1);
                    approvalHistory.setSubmitDate(new Date());
                    approvalHistory.setUser(user);
                    approvalHistory.setWorkCase(workCase);
                }
                approvalHistoryDAO.persist(approvalHistory);
            } else {
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA from BDM to NextStep **/
    public void submitForBDM(String queueName, String wobNumber, String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception {
        WorkCase workCase;
        String productGroup;
        String deviationCode = "";
        String resultCode = "G";
        int requestType;
        int appraisalRequestRequire = 0;
        BigDecimal totalCommercial = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;

        if(workCaseId != 0){
            User user = getCurrentUser();
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null && workCase.getProductGroup() != null){
                productGroup = workCase.getProductGroup().getName();
                requestType = workCase.getRequestType().getId();
                appraisalRequestRequire = workCase.getRequestAppraisalRequire();

                ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
                if(proposeLine != null) {
                    totalCommercial = proposeLine.getTotalExposure();
                }

                ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
                if(existingCreditFacility != null) {
                    totalRetail = existingCreditFacility.getTotalBorrowerRetailLimit() != null ? existingCreditFacility.getTotalBorrowerRetailLimit() : BigDecimal.ZERO;
                }

                UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);
                if(uwRuleResultSummary!=null && uwRuleResultSummary.getId()>0){
                    if(uwRuleResultSummary.getUwResultColor()!=null){
                        resultCode = uwRuleResultSummary.getUwResultColor().code();
                    }

                    if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase(UWResultColor.RED.code())){
                        deviationCode = "AD";
                        if(uwRuleResultSummary.getUwDeviationFlag()!=null && uwRuleResultSummary.getUwDeviationFlag().getId()>0){
                            deviationCode = uwRuleResultSummary.getUwDeviationFlag().getBrmsCode();
                        }
                    }
                }
                log.debug("submitForBDM :: Duplicate Facility data start.");
                duplicateFacilityData(workCaseId);
                log.debug("submitForBDM :: Duplicate Facility data complete...");
                bpmExecutor.submitForBDM(queueName, wobNumber, zmUserId, rgmUserId, ghUserId, cssoUserId, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), totalCommercial, totalRetail, resultCode, productGroup, deviationCode, requestType, appraisalRequestRequire, ActionCode.SUBMIT_CA.getVal());

                //Insert Approval History
                log.debug("submitCAByBDM : add approval History into ApprovalHistory");
                ApprovalHistory approvalHistory = approvalHistoryDAO.findExistHistoryByUser(workCaseId, user.getId());
                if(approvalHistory == null) {
                    log.debug("submitZM : add new Approval History");
                    approvalHistory = new ApprovalHistory();
                    approvalHistory.setComments(submitRemark);
                    approvalHistory.setRole(user.getRole());
                    approvalHistory.setStep(workCase.getStep());
                    approvalHistory.setSubmit(1);
                    approvalHistory.setSubmitDate(new Date());
                    approvalHistory.setUser(user);
                    approvalHistory.setWorkCase(workCase);
                } else {
                    log.debug("submitZM : replace existing Approval History");
                    approvalHistory.setComments(submitRemark);
                    approvalHistory.setRole(user.getRole());
                    approvalHistory.setStep(workCase.getStep());
                    approvalHistory.setSubmit(1);
                    approvalHistory.setSubmitDate(new Date());
                    approvalHistory.setUser(user);
                    approvalHistory.setWorkCase(workCase);
                }
                approvalHistoryDAO.persist(approvalHistory);
            } else {
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA from ZM to NextStep **/
    public void submitForZM(String queueName, String wobNumber, String rgmUserId, String ghUserId, String cssoUserId, String submitRemark, String slaRemark, int slaReasonId, long workCaseId, long stepId) throws Exception {
        if(workCaseId != 0){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null){
                log.debug("submitForZM ::: stepId : {}", stepId);
                if(stepId == StepValue.FULLAPP_ZM.value()){ //Submit case for ZM after BDM Submit ( normal flow )
                    log.debug("submitForZM ::: stepId : FULLAPP_ZM [{}] Normal BU Flow...", stepId);
                    String zmDecisionFlag = "";
                    String zmPricingRequestFlag = "";
                    BigDecimal totalCommercial = BigDecimal.ZERO;   //TODO
                    BigDecimal totalRetail = BigDecimal.ZERO;       //TODO
                    String resultCode = "G";
                    String deviationCode = "";
                    int requestType;
                    int priceDOALevel;
                    ApprovalHistory approvalHistoryEndorseCA = null;
                    ApprovalHistory approvalHistoryEndorsePricing = null;
                    boolean isPricingRequest;

                    priceDOALevel = workCase.getPricingDoaLevel();
                    requestType = workCase.getRequestType().getId();
                    isPricingRequest = Util.isTrue(workCase.getRequestPricing());

                    ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
                    if(proposeLine != null) {
                        totalCommercial = proposeLine.getTotalExposure() != null ? proposeLine.getTotalExposure() : BigDecimal.ZERO;
                    }

                    ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(workCaseId);
                    if(existingCreditFacility != null) {
                        totalRetail = existingCreditFacility.getTotalBorrowerRetailLimit() != null ? existingCreditFacility.getTotalBorrowerRetailLimit() : BigDecimal.ZERO;
                    }

                    log.debug("submitForZM ::: totalCommercial : {}, totalRetail : {}", totalCommercial, totalRetail);

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
                                log.debug("submitForZM :: no decision found. ( endorse )");
                                throw new Exception("Please make decision ( Endorse CA ) before submit.");
                            }
                        } else {
                            log.debug("submitForZM :: no decision found. ( pricing and endorse )");
                            throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
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
                                log.debug("submitForZM :: no decision found. ( endorse )");
                                throw new Exception("Please make decision ( Endorse Pricing ) before submit.");
                            }
                        } else {
                            log.debug("submitForZM :: no decision found. ( pricing and endorse )");
                            throw new Exception("Please make decision ( Endorse Pricing ) before submit.");
                        }

                    } else {
                        approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
                        if(approvalHistoryEndorseCA == null){
                            log.debug("submitForZM :: no decision found ( endorse )");
                            throw new Exception("Please make decision before submit.");
                        } else {
                            if(approvalHistoryEndorseCA.getApproveDecision() == DecisionType.NO_DECISION.value()){
                                log.debug("submitForZM :: no decision selected ( endorse )");
                                throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                            }

                            zmDecisionFlag = approvalHistoryEndorseCA.getApproveDecision()==DecisionType.APPROVED.value()?"A":"R";
                            zmPricingRequestFlag = "NA";
                            approvalHistoryEndorseCA.setSubmit(1);
                            approvalHistoryEndorseCA.setSubmitDate(new Date());
                        }
                    }

                    UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);
                    if(uwRuleResultSummary != null && uwRuleResultSummary.getId() > 0){
                        if(uwRuleResultSummary.getUwResultColor() != null){
                            resultCode = uwRuleResultSummary.getUwResultColor().code();
                        }

                        if(!Util.isEmpty(resultCode) && resultCode.trim().equalsIgnoreCase(UWResultColor.RED.code())){
                            deviationCode = "AD";
                            if(uwRuleResultSummary.getUwDeviationFlag() != null && uwRuleResultSummary.getUwDeviationFlag().getId() > 0){
                                deviationCode = uwRuleResultSummary.getUwDeviationFlag().getBrmsCode();
                            }
                        }
                    }

                    bpmExecutor.submitForZM(queueName, wobNumber, rgmUserId, ghUserId, cssoUserId, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), zmDecisionFlag, zmPricingRequestFlag, totalCommercial, totalRetail, resultCode, deviationCode, requestType, ActionCode.SUBMIT_CA.getVal());

                    approvalHistoryDAO.persist(approvalHistoryEndorseCA);
                    if(isPricingRequest){
                        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
                    }
                }else if(stepId == StepValue.CREDIT_DECISION_BU_ZM.value()){ //Submit case for ZM after BDM Submit ( F-CASH flow )
                    log.debug("submitForZM ::: stepId : CREDIT_DECISION_BU_ZM [{}] F-Cash Flow...", stepId);
                    String zmDecisionFlag;
                    ApprovalHistory approvalHistoryApprove = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
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
                }
            }else{
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        }else{
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for ZM F-Cash 2nd time **/
    public void submitForZMFCash(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception{
        String zmDecisionFlag;
        ApprovalHistory approvalHistoryApprove;

        if(workCaseId != 0){
            approvalHistoryApprove = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.CA_APPROVAL.value());
            if(!Util.isNull(approvalHistoryApprove)){
                if(approvalHistoryApprove.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                    zmDecisionFlag = getDecisionFlag(approvalHistoryApprove.getApproveDecision());

                    if(Util.isEmpty(zmDecisionFlag))
                        throw new Exception("Please make decision before submit.");

                    approvalHistoryApprove.setSubmit(1);
                    approvalHistoryApprove.setSubmitDate(new Date());

                    log.debug("submitForZMFCash 2nd ::: approvalHistory : {}", approvalHistoryApprove);

                    bpmExecutor.submitForZMFCash(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), zmDecisionFlag, ActionCode.SUBMIT_CA.getVal());

                    approvalHistoryDAO.persist(approvalHistoryApprove);
                } else {
                    throw new Exception("Please make decision before submit.");
                }
            } else {
                throw new Exception("Please make decision before submit.");
            }
        }else{
            log.debug("submitForZMFCash ::: workCaseId : {}", workCaseId);
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for RGM to NextStep **/
    public void submitForRGM(String queueName, String wobNumber, String ghUserId, String cssoUserId, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception {
        String rgmDecisionFlag;
        int priceDOALevel;
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing;

        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null && workCase.getProductGroup() != null){
                priceDOALevel = workCase.getPricingDoaLevel();
                approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                if(!Util.isNull(approvalHistoryEndorsePricing)){
                    if(approvalHistoryEndorsePricing.getApproveDecision() != DecisionType.NO_DECISION.value()){
                        if(priceDOALevel > PricingDOAValue.RGM_DOA.value()){
                            rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"E":"R";
                        } else {
                            rgmDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
                        }
                        approvalHistoryEndorsePricing.setSubmit(1);
                        approvalHistoryEndorsePricing.setSubmitDate(new Date());

                        bpmExecutor.submitForRGM(queueName, wobNumber, ghUserId, cssoUserId, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), rgmDecisionFlag, ActionCode.SUBMIT_CA.getVal());
                        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
                    } else {
                        log.debug("submitForRGM :: no decision selected.");
                        throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                    }
                }else{
                    log.debug("submitForRGM :: no decision found. ( pricing )");
                    throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                }
            } else {
                log.debug("submitForRGM :: workcase : {}", workCase);
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            log.debug("submitForRGM :: workCaseId : {}", workCaseId);
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for GH to NextStep **/
    public void submitForGH(String queueName, String wobNumber, String cssoUserId, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception {
        String ghDecisionFlag;
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorsePricing;
        int priceDOALevel;

        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null && workCase.getProductGroup()!=null){
                priceDOALevel = workCase.getPricingDoaLevel();
                approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

                if(!Util.isNull(approvalHistoryEndorsePricing)){
                    if(approvalHistoryEndorsePricing.getApproveDecision() != RadioValue.NOT_SELECTED.value()){
                        if(priceDOALevel > PricingDOAValue.GH_DOA.value()){
                            ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"E":"R";
                        } else {
                            ghDecisionFlag = approvalHistoryEndorsePricing.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
                        }
                        approvalHistoryEndorsePricing.setSubmit(1);
                        approvalHistoryEndorsePricing.setSubmitDate(new Date());

                        bpmExecutor.submitForGH(queueName, wobNumber, cssoUserId, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), ghDecisionFlag, ActionCode.SUBMIT_CA.getVal());
                        approvalHistoryDAO.persist(approvalHistoryEndorsePricing);

                    } else {
                        log.debug("submitForGH :: no decision found. ( pricing )");
                        throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                    }
                }else{
                    log.debug("submitForGH :: no decision selected.");
                    throw new Exception(msg.get("exception.submit.makedecision.beforesubmit"));
                }
            } else {
                throw new Exception(msg.get("exception.submit.workitem.notfound"));
            }
        } else {
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for CSSO to NextStep **/
    public void submitForCSSO(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception {
        String cssoDecisionFlag;
        ApprovalHistory approvalHistoryEndorsePricing = null;

        if(!Util.isZero(workCaseId)){
            approvalHistoryEndorsePricing = approvalHistoryDAO.findByWorkCaseAndUserAndApproveType(workCaseId, getCurrentUser(), ApprovalType.PRICING_APPROVAL.value());

            if(!Util.isNull(approvalHistoryEndorsePricing)){
                if(approvalHistoryEndorsePricing.getApproveDecision() != 0){
                    cssoDecisionFlag = getDecisionFlag(approvalHistoryEndorsePricing.getApproveDecision());

                    if(Util.isEmpty(cssoDecisionFlag))
                        throw new Exception("Please make decision before submit.");

                    approvalHistoryEndorsePricing.setSubmit(1);
                    approvalHistoryEndorsePricing.setSubmitDate(new Date());

                    bpmExecutor.submitForCSSO(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), cssoDecisionFlag, ActionCode.SUBMIT_CA.getVal());
                    approvalHistoryDAO.persist(approvalHistoryEndorsePricing);
                } else {
                    throw new Exception("Please make decision before submit.");
                }
            }else{
                throw new Exception("Please make decision before submit.");
            }
        }
    }

    /** Submit CA for UW to Next Step **/
    public void submitForUW(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId, String uw2Name, long uw2DOALevelId, long workCaseId) throws Exception {
        String decisionFlag;
        String haveRG001 = "N";
        ApprovalHistory approvalHistoryEndorseCA = null;
        AuthorizationDOA authorizationDOA = null;

        if(uw2DOALevelId != 0) {
            authorizationDOA = authorizationDOADAO.findById(uw2DOALevelId);
        }

        if(workCaseId != 0){
            approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());

            if(approvalHistoryEndorseCA==null){
                throw new Exception("Please make decision before submit.");
            } else {
                decisionFlag = getDecisionFlag(approvalHistoryEndorseCA.getApproveDecision());

                if(Util.isEmpty(decisionFlag))
                    throw new Exception("Please make decision before submit.");

                WorkCase workCase = workCaseDAO.findById(workCaseId);
                String appraisalRequired = workCase != null ? String.valueOf(workCase.getRequestAppraisalRequire()) : "0";

                if(returnControl.getReturnHistoryHaveRG001(workCaseId)){
                    haveRG001 = "Y";
                }

                bpmExecutor.submitForUW(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), uw2Name, authorizationDOA != null ? authorizationDOA.getDescription() : "", decisionFlag, haveRG001, appraisalRequired, ActionCode.SUBMIT_CA.getVal());

                ApprovalHistory approvalHistory = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());
                approvalHistory.setSubmit(1);
                approvalHistory.setSubmitDate(new Date());
                approvalHistoryDAO.persist(approvalHistory);
            }
        }else{
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for UW2 to Next Step **/
    public void submitForUW2(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId, long workCaseId) throws Exception {
        String decisionFlag;
        String haveRG001 = "N";
        ApprovalHistory approvalHistoryEndorseCA = null;

        String insuranceRequired = "N";
        String approvalFlag = "N";
        String tcgRequired = "N";

        if(workCaseId != 0){
            approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());

            if(approvalHistoryEndorseCA==null){
                throw new Exception("Please make decision before submit.");
            } else {

                decisionFlag = getDecisionFlag(approvalHistoryEndorseCA.getApproveDecision());

                if(Util.isEmpty(decisionFlag))
                    throw new Exception("Please make decision before submit.");

                if(returnControl.getReturnHistoryHaveRG001(workCaseId))
                    haveRG001 = "Y";


                BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
                if(!Util.isNull(basicInfo)){
                    approvalFlag = basicInfo.getApproveResult().value() == 1 ? "Y" : "N";
                    tcgRequired = basicInfo.getTcgFlag() == 1 ? "Y" : "N";
                    insuranceRequired = basicInfo.getPremiumQuote() == 1 ? "Y" : "N";
                }

                if(!decisionFlag.equals("R")) {
                    mortgageSummaryControl.calculateMortgageSummary(workCaseId);
                }

                bpmExecutor.submitForUW2(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), decisionFlag, haveRG001, insuranceRequired, approvalFlag, tcgRequired, ActionCode.SUBMIT_CA.getVal());
                log.debug("Save approval history for SubmitUW2 :: approvalHistoryEndorseCA : {}", approvalHistoryEndorseCA);
                ApprovalHistory approvalHistory = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());
                approvalHistory.setSubmit(1);
                approvalHistory.setSubmitDate(new Date());
                approvalHistoryDAO.persist(approvalHistory);
            }
        }else{
            throw new Exception(msg.get("exception.submit.workitem.notfound"));
        }
    }

    /** Submit CA for BDM to UW **/
    public void submitForBDMUW(String queueName, String wobNumber, String submitRemark, String slaRemark, int slaReasonId) throws Exception{
        bpmExecutor.submitForBDMUW(queueName, wobNumber, getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId), ActionCode.SUBMIT_CA.getVal());
    }

    /** Submit CA [ Function for merge remark for BPM ] **/
    private String getRemark(String submitRemark, String slaRemark){
        String remark = "";
        if(!Util.isEmpty(submitRemark)){
            remark = remark.concat(submitRemark);
        }
        if(!Util.isEmpty(slaRemark)){
            remark = remark.concat(slaRemark);
        }
        return remark;
    }

    /** Submit CA [ Function to Check UW Decision ] **/
    public boolean checkUWDecision(long workCaseId){
        boolean isUWReject = false;
        ApprovalHistory approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUser(workCaseId, getCurrentUserID());
        if(!Util.isNull(approvalHistoryEndorseCA)){
            if(approvalHistoryEndorseCA.getApproveDecision() == DecisionType.REJECTED.value()){
                isUWReject = true;
            }
        }
        return isUWReject;
    }

    /** Submit CA [ Function to Update Select BU User ] **/
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveSelectedUserBU(String zmUserId, String rgmUserId, String ghUserId, String cssoUserId, long workCaseId){
        try {
            BasicInfo basicInfo = basicInfoDAO.getBasicInfoByWorkCaseId(workCaseId);
            //Update user bu
            if (!Util.isEmpty(zmUserId)) {
                basicInfo.setZmUser(userDAO.findById(zmUserId));
            }
            if (!Util.isEmpty(rgmUserId)) {
                basicInfo.setRgmUser(userDAO.findById(rgmUserId));
            }
            if (!Util.isEmpty(ghUserId)) {
                basicInfo.setGhUser(userDAO.findById(ghUserId));
            }
            if (!Util.isEmpty(cssoUserId)) {
                basicInfo.setCssoUser(userDAO.findById(cssoUserId));
            }
            basicInfoDAO.persist(basicInfo);
        }catch (Exception ex){
            log.error("Exception while saveSelectedUserBU : ", ex);
        }
    }
    //---------- End function for Submit UW ----------//

    //---------- Function for Appraisal ----------//
    /** Appraisal [ Request Appraisal - BDM at RequestAppraisal Screen ( PreScreen, Full App ) ] **/
    public void requestAppraisal(long workCasePreScreenId, long workCaseId, long stepId) throws Exception{
        WorkCasePrescreen workCasePrescreen;
        WorkCase workCase;

        //Create WRK_APPRAISAL for workflow to update step and status
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
            bpmExecutor.requestAppraisal(workCaseAppraisal.getAppNumber(), "", borrowerName, workCaseAppraisal.getProductGroup().getName(), workCaseAppraisal.getRequestType().getId(), getCurrentUserID());
            log.debug("requestAppraisal ::: Create Work Item for appraisal complete.");
        } catch (Exception ex){
            log.error("Exception while Create Work Item for Appraisal.");
            workCaseAppraisalDAO.delete(workCaseAppraisal);
            throw new Exception("Exception while Create Work Item for Appraisal.");
        }

        ProposeLine newCreditFacility = null;

        if(workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

            if(workCasePrescreen != null){
                workCasePrescreen.setRequestAppraisal(1);
                workCasePrescreen.setParallelAppraisalFlag(ParallelAppraisalStatus.REQUESTED_PARALLEL.value());
                workCasePrescreenDAO.persist(workCasePrescreen);

                newCreditFacility = proposeLineDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from prescreen");
            }
        } else if(workCaseId != 0) {
            workCase = workCaseDAO.findById(workCaseId);

            if(workCase != null){
                workCase.setRequestAppraisal(1);
                workCase.setParallelAppraisalFlag(ParallelAppraisalStatus.REQUESTED_PARALLEL.value());
                workCaseDAO.persist(workCase);

                newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
            } else{
                throw new Exception("exception while request appraisal, cause can not find data from full application");
            }
        } else {
            log.error("exception while Request Appraisal (BDM), can not find workcase or workcaseprescreen.");
            throw new Exception("exception while Request Appraisal, can not find case.");
        }
        log.debug("requestAppraisal ::: Update Request Appraisal Flag Complete.");

        ProposeType proposeType;
        if(stepId != StepValue.REQUEST_APPRAISAL.value()){
            proposeType = ProposeType.P;
        }else{
            proposeType = ProposeType.A;
        }

        List<ProposeCollateralInfo> proposeCollateralInfoList = newCollateralDAO.findCollateralForAppraisalRequest(newCreditFacility, proposeType);
        if(proposeCollateralInfoList != null && proposeCollateralInfoList.size() > 0){
            for(ProposeCollateralInfo proposeCol : proposeCollateralInfoList){
                proposeCol.setAppraisalRequest(RequestAppraisalValue.REQUESTED.value());
                if(proposeCol.getProposeCollateralInfoHeadList() != null && proposeCol.getProposeCollateralInfoHeadList().size() > 0){
                    for(ProposeCollateralInfoHead proposeColHead : proposeCol.getProposeCollateralInfoHeadList()){
                        if(proposeColHead.getAppraisalRequest() == RequestAppraisalValue.READY_FOR_REQUEST.value()) {
                            proposeColHead.setAppraisalRequest(RequestAppraisalValue.REQUESTED.value());
                        }
                    }
                }
                newCollateralDAO.persist(proposeCol);
            }
        }
    }

    public void duplicateCollateralForAppraisal(long workCaseId, long workCasePreScreenId){
        //Duplicate Data From Propose ( Requested ) to Approve Requested...
        log.debug("duplicateCollateralData : workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
        duplicateCollateralData(workCaseId, workCasePreScreenId);
    }

    /** Appraisal [ Request Appraisal - BDM at RequestAppraisal Screen ( Customer Acceptance ) ] **/
    public void requestAppraisal(String queueName, String wobNumber, long workCaseId, String submitRemark, int slaReasonId, String slaRemark ) throws Exception{
        if(!Util.isEmpty(queueName) && !Util.isEmpty(wobNumber)) {
            try{
                log.debug("requestAppraisal Customer Acceptance ::: Create WorkCaseAppraisal Complete.");
                ProposeLine proposeLine = null;
                if(!Util.isZero(workCaseId)) {
                    proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);

                    if (!Util.isNull(proposeLine)) {
                        if (!Util.isNull(proposeLine.getProposeCollateralInfoList()) && proposeLine.getProposeCollateralInfoList().size() > 0) {
                            for (ProposeCollateralInfo proposeCollateralInfo : proposeLine.getProposeCollateralInfoList()) {
                                if (proposeCollateralInfo.getProposeType() == ProposeType.A && proposeCollateralInfo.getAppraisalRequest() == RequestAppraisalValue.READY_FOR_REQUEST.value()) {
                                    proposeCollateralInfo.setAppraisalRequest(RequestAppraisalValue.REQUESTED.value());
                                    if (!Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && proposeCollateralInfo.getProposeCollateralInfoHeadList().size() > 0) {
                                        for (ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()) {
                                            if (proposeCollateralInfoHead.getProposeType() == ProposeType.A && proposeCollateralInfoHead.getAppraisalRequest() == RequestAppraisalValue.READY_FOR_REQUEST.value()) {
                                                proposeCollateralInfoHead.setAppraisalRequest(RequestAppraisalValue.REQUESTED.value());
                                            }
                                        }
                                    }
                                }
                                newCollateralDAO.persist(proposeCollateralInfo);
                            }
                        }
                    }
                }
                bpmExecutor.requestAppraisal(queueName, wobNumber, ActionCode.REQUEST_APPRAISAL.getVal(), getRemark(submitRemark, slaRemark), getReasonDescription(slaReasonId));
                log.debug("requestAppraisal Customer Acceptance ::: Create Work Item for appraisal complete.");
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

    /** Appraisal [ Request Appraisal - BDM at PreScreen/Full App ] **/
    public void requestParallelAppraisal(long workCaseId, long workCasePreScreenId) throws Exception{
        log.debug("requestParallelAppraisal ::: start, workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);

        if(!Util.isZero(workCaseId)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setParallelAppraisalFlag(ParallelAppraisalStatus.REQUESTING_PARALLEL.value());
            workCaseDAO.persist(workCase);
        } else if(!Util.isZero(workCasePreScreenId)){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setParallelAppraisalFlag(ParallelAppraisalStatus.REQUESTING_PARALLEL.value());
            workCasePrescreenDAO.persist(workCasePrescreen);
        }
    }

    /** Appraisal [ Cancel Appraisal - BDM at RequestAppraisal Screen ( PreScreen, Full App ) ] **/
    public void cancelParallelRequestAppraisal(long workCasePreScreenId, long workCaseId, long stepId){
        log.debug("cancelParallelRequestAppraisal : workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
        ProposeLine newCreditFacility = null;
        if(!Util.isZero(workCasePreScreenId)){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setParallelAppraisalFlag(ParallelAppraisalStatus.NO_REQUEST.value());
            workCasePrescreenDAO.persist(workCasePrescreen);
            newCreditFacility = proposeLineDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }else if(!Util.isZero(workCaseId)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setParallelAppraisalFlag(ParallelAppraisalStatus.NO_REQUEST.value());
            workCaseDAO.persist(workCase);
            newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        }

        //Set flag for Collateral to READY_TO_REQUEST
        ProposeType proposeType;
        if(stepId != StepValue.REQUEST_APPRAISAL.value()){
            proposeType = ProposeType.P;
        }else{
            proposeType = ProposeType.A;
        }

        List<ProposeCollateralInfo> proposeCollateralInfoList = newCollateralDAO.findCollateralForAppraisalRequest(newCreditFacility, proposeType);
        if(proposeCollateralInfoList != null && proposeCollateralInfoList.size() > 0){
            for(ProposeCollateralInfo proposeCol : proposeCollateralInfoList){
                proposeCol.setAppraisalRequest(RequestAppraisalValue.READY_FOR_REQUEST.value());
                if(proposeCol.getProposeCollateralInfoHeadList() != null && proposeCol.getProposeCollateralInfoHeadList().size() > 0){
                    for(ProposeCollateralInfoHead proposeColHead : proposeCol.getProposeCollateralInfoHeadList()){
                        proposeColHead.setAppraisalRequest(RequestAppraisalValue.READY_FOR_REQUEST.value());
                    }
                }
                newCollateralDAO.persist(proposeCol);
            }
        }
    }

    /** Appraisal [ Cancel Appraisal - BDM after Return from AAD Admin ] **/
    public void cancelRequestAppraisal(String queueName, String wobNumber, int reasonId, String remark, long workCasePreScreenId, long workCaseId, long stepId) throws Exception {
        //TODO Change Status for WorkCase/WorkCasePreScreen and flag for Collateral
        log.debug("cancelParallelRequestAppraisal : workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
        ProposeLine newCreditFacility = null;
        if(!Util.isZero(workCasePreScreenId)){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setParallelAppraisalFlag(ParallelAppraisalStatus.NO_REQUEST.value());
            workCasePrescreenDAO.persist(workCasePrescreen);
            newCreditFacility = proposeLineDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }else if(!Util.isZero(workCaseId)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setParallelAppraisalFlag(ParallelAppraisalStatus.NO_REQUEST.value());
            workCaseDAO.persist(workCase);
            newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        }

        //Set flag for Collateral to READY_TO_REQUEST
        ProposeType proposeType;
        if(stepId != StepValue.REQUEST_APPRAISAL.value()){
            proposeType = ProposeType.P;
        }else{
            proposeType = ProposeType.A;
        }

        List<ProposeCollateralInfo> proposeCollateralInfoList = newCollateralDAO.findCollateralForAppraisalRequest(newCreditFacility, proposeType);
        if(proposeCollateralInfoList != null && proposeCollateralInfoList.size() > 0){
            for(ProposeCollateralInfo proposeCol : proposeCollateralInfoList){
                proposeCol.setAppraisalRequest(RequestAppraisalValue.READY_FOR_REQUEST.value());
                if(proposeCol.getProposeCollateralInfoHeadList() != null && proposeCol.getProposeCollateralInfoHeadList().size() > 0){
                    for(ProposeCollateralInfoHead proposeColHead : proposeCol.getProposeCollateralInfoHeadList()){
                        proposeColHead.setAppraisalRequest(RequestAppraisalValue.READY_FOR_REQUEST.value());
                    }
                }
                newCollateralDAO.persist(proposeCol);
            }
        }
        bpmExecutor.cancelCase(queueName, wobNumber, ActionCode.CANCEL_APPRAISAL.getVal(), getReasonDescription(reasonId), remark);
    }

    /** Appraisal [ Request Appraisal - BDM Create Parallel Appraisal Work Case ] **/
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public WorkCaseAppraisal createWorkCaseAppraisal(long workCasePreScreenId, long workCaseId) throws Exception {
        WorkCasePrescreen workCasePrescreen;
        WorkCase workCase;
        String appNumber = "";
        ProductGroup productGroup = null;
        RequestType requestType = null;
        log.debug("requestAppraisal ::: start... workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
        if(!Util.isZero(workCasePreScreenId)){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            log.debug("requestAppraisal ::: getAppNumber from workCasePreScreen : {}", workCasePrescreen);
            if(workCasePrescreen != null){
                appNumber = workCasePrescreen.getAppNumber();
                productGroup = workCasePrescreen.getProductGroup();
                requestType = workCasePrescreen.getRequestType();
            } else{
                log.error("exception while request appraisal, cause can not find data from prescreen");
                throw new Exception("exception while request appraisal, cause can not find data from prescreen");
            }
        }else if(!Util.isZero(workCaseId)){
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
        log.debug("createWorkCaseAppraisal : workCaseAppraisal : {}", workCaseAppraisal);

        return workCaseAppraisal;
    }

    /** Appraisal [ Request Appraisal - BDM Check appraisal data ] **/
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

    /** Appraisal [ Request Appraisal - BDM Check appointment data ] **/
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

    /** Appraisal [ Submit Appraisal Appointment to Committee ] **/
    public void submitForAADAdmin(String aadCommitteeUserId, long workCaseId, long workCasePreScreenId, String queueName, String wobNumber) throws Exception{
        log.debug("submitForAADAdmin ::: starting...");
        String appNumber = "";
        Appraisal appraisal = null;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            appNumber = workCase.getAppNumber();
            log.debug("submitForAADAdmin ::: find appraisal by workCase : {}", appraisal);
            log.debug("submitForAADAdmin ::: find workCase : {}", workCase);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            appNumber = workCasePrescreen.getAppNumber();
            log.debug("submitForAADAdmin ::: find appraisal by workCasePrescreen: {}", appraisal);
            log.debug("submitForAADAdmin ::: find workCasePrescreen : {}", workCasePrescreen);
        }

        if(appraisal != null){
            User aadCommittee = userDAO.findById(aadCommitteeUserId);
            appraisal.setAadCommittee(aadCommittee);
            appraisalDAO.persist(appraisal);
            //Save appointment date for appraisal work case
            if(!Util.isEmpty(appNumber)){
                WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByAppNumber(appNumber);
                log.debug("submitForAADAdmin ::: find workCaseAppraisal : {}", workCaseAppraisal);
                if(!Util.isNull(workCaseAppraisal)) {
                    workCaseAppraisal.setAppointmentDate(appraisal.getAppointmentDate());
                    workCaseAppraisalDAO.persist(workCaseAppraisal);
                    log.debug("submitForAADAdmin ::: save workCaseAppraisal : {}", workCaseAppraisal);
                }
                long appraisalLocationCode = 0;
                if(appraisal.getLocationOfProperty() != null){
                    appraisalLocationCode = appraisal.getLocationOfProperty().getCode();
                }
                bpmExecutor.submitForAADAdmin(aadCommitteeUserId, DateTimeUtil.convertDateWorkFlowFormat(appraisal.getAppointmentDate()), appraisalLocationCode, queueName, ActionCode.SUBMIT_CA.getVal(), wobNumber);
            }
        } else {
            throw new Exception("Submit case failed, could not find appraisal data.");
        }
        log.debug("submitForAADAdmin ::: end...");
    }

    /** Appraisal [ Submit Appraisal Result to UW ] **/
    public void submitForAADCommittee(String queueName, String wobNumber, long workCaseId, long workCasePreScreenId) throws Exception{
        //Update Appraisal Request to Completed
        ProposeLine proposeLine = null;
        if(!Util.isZero(workCaseId)){
            proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        }else if(!Util.isZero(workCasePreScreenId)){
            proposeLine = proposeLineDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }
        if(!Util.isNull(proposeLine)){
            if(!Util.isNull(proposeLine.getProposeCollateralInfoList()) && proposeLine.getProposeCollateralInfoList().size() > 0){
                for(ProposeCollateralInfo proposeCollateralInfo : proposeLine.getProposeCollateralInfoList()){
                    if(proposeCollateralInfo.getProposeType() == ProposeType.A && proposeCollateralInfo.getAppraisalRequest() == RequestAppraisalValue.REQUESTED.value()){
                        proposeCollateralInfo.setAppraisalRequest(RequestAppraisalValue.COMPLETED.value());
                        if(!Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && proposeCollateralInfo.getProposeCollateralInfoHeadList().size() > 0){
                            for(ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()){
                                if(proposeCollateralInfoHead.getProposeType() == ProposeType.A && proposeCollateralInfoHead.getAppraisalRequest() == RequestAppraisalValue.REQUESTED.value()){
                                    proposeCollateralInfoHead.setAppraisalRequest(RequestAppraisalValue.COMPLETED.value());
                                }
                            }
                        }
                    }
                    newCollateralDAO.persist(proposeCollateralInfo);
                }
            }
        }
        bpmExecutor.submitUW2FromCommittee(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }
    //---------- End Function for Appraisal ----------//

    /*public void returnAADCommittee(String queueName, String wobNumber, String reason, String remark) throws Exception {
        bpmExecutor.returnCase(queueName, wobNumber, remark, reason, ActionCode.RETURN_TO_AAD_ADMIN.getVal());
    }

    public String getAADAdmin(long workCaseId, long workCasePreScreenId){
        String aadAdminName = "";
        Appraisal appraisal;
        if(!Util.isNull(workCaseId) && !Util.isZero(workCaseId)){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            aadAdminName = appraisal.getAadAdmin()!=null?appraisal.getAadAdmin().getUserName():"";
            log.debug("submitForAADAdmin ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && !Util.isZero(workCasePreScreenId)){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            aadAdminName = appraisal.getAadAdmin()!=null?appraisal.getAadAdmin().getUserName():"";
            log.debug("submitForAADAdmin ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        return aadAdminName;
    }

    public String getAADCommittee(long workCaseId, long workCasePreScreenId){
        String aadCommitteeName = "";
        Appraisal appraisal;
        if(!Util.isNull(workCaseId) && workCaseId != 0){
            appraisal = appraisalDAO.findByWorkCaseId(workCaseId);
            aadCommitteeName = appraisal.getAadCommittee()!=null?appraisal.getAadCommittee().getUserName():"";
            log.debug("submitForAADAdmin ::: find appraisal by workCase : {}", appraisal);
        }else if(!Util.isNull(workCasePreScreenId) && workCasePreScreenId != 0){
            appraisal = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            aadCommitteeName = appraisal.getAadCommittee()!=null?appraisal.getAadCommittee().getUserName():"";
            log.debug("submitForAADAdmin ::: find appraisal by workCasePrescreen: {}", appraisal);
        }

        return aadCommitteeName;
    }

    public void submitUW2FromAAD(){

    }*/

    public void submitCustomerAcceptance(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCustomerAcceptance(queueName, wobNumber, ActionCode.CUSTOMER_ACCEPT.getVal());
    }

    public void submitPendingDecision(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitPendingDecision(queueName, wobNumber, ActionCode.PENDING_FOR_DECISION.getVal());
    }

    public void submitRequestPriceReduction(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.REQUEST_PRICE_REDUCE.getVal());
    }

    public void returnBDMByAAD(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.returnCase(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.RETURN_TO_BDM.getVal());
    }

    public void returnBDMByBU(String queueName, String wobNumber, String remark, int reasonId) throws Exception{
        bpmExecutor.returnCase(queueName, wobNumber, remark, getReasonDescription(reasonId), ActionCode.REVISE_CA.getVal());
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

    /*public void submitToBDM(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }*/

    public void calculateApprovedPricingDOA(long workCaseId, ProposeType proposeType, long stepId){
        boolean skipReduceFlag = false;
        if(stepId == StepValue.REVIEW_PRICING_REQUEST_BDM.value()){
            skipReduceFlag = true;
        }
        ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        workCase = calculatePricingDOA(workCase, newCreditFacility, proposeType, skipReduceFlag);
        workCaseDAO.persist(workCase);
    }

    public WorkCase calculatePricingDOA(WorkCase workCase, ProposeLine newCreditFacility, ProposeType proposeType, boolean skipReduceFlag){
        log.debug("calculatePricingDOA ::: newCreditFacility : {}", newCreditFacility);
        PricingDOAValue pricingDOALevel = PricingDOAValue.NO_DOA;

        if(newCreditFacility != null){
            //List of Credit detail
            List<ProposeCreditInfo> newCreditDetailList = newCreditDetailDAO.findNewCreditDetail(workCase.getId(), proposeType);
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
                for(ProposeCreditInfo itemCreditDetail : newCreditDetailList){
                    if(itemCreditDetail.getReduceFrontEndFee() == 1 || itemCreditDetail.getReducePriceFlag() == 1){
                        requestPricing = 1;
                        break;
                    }
                }

                for(ProposeCreditInfo proposeCreditInfo : newCreditDetailList){
                    BigDecimal standardPrice = null;
                    BigDecimal suggestPrice = null;
                    BigDecimal finalPrice = null;
                    BigDecimal tmpStandardPrice = null;
                    BigDecimal tmpSuggestPrice = null;
                    BigDecimal tmpFinalPrice = null;
                    BigDecimal tmpBigDecimal = null;
                    int reducePricing = proposeCreditInfo.getReducePriceFlag();
                    //int reduceFrontEndFee = newCreditDetail.getReduceFrontEndFee();
                    if(proposeCreditInfo.getRequestType() == RequestTypes.NEW.value()) {
                        if((proposeCreditInfo.getReduceFrontEndFee() == 1 || proposeCreditInfo.getReducePriceFlag() == 1) || skipReduceFlag){
                            //Check for Final Price first...
                            if(proposeCreditInfo.getProposeCreditInfoTierDetailList() != null && proposeCreditInfo.getProposeCreditInfoTierDetailList().size() > 0) {
                                if (finalPrice != null) {
                                    for (ProposeCreditInfoTierDetail proposeCreditInfoTierDetail : proposeCreditInfo.getProposeCreditInfoTierDetailList()) {
                                        if (proposeCreditInfoTierDetail.getBrmsFlag() == 1) {
                                            tmpFinalPrice = proposeCreditInfoTierDetail.getFinalInterest().add(proposeCreditInfoTierDetail.getFinalBasePrice().getValue());
                                        }
                                    }
                                    tmpStandardPrice = proposeCreditInfo.getStandardInterest().add(proposeCreditInfo.getStandardBasePrice().getValue());
                                    tmpSuggestPrice = proposeCreditInfo.getSuggestInterest().add(proposeCreditInfo.getSuggestBasePrice().getValue());

                                    if (tmpStandardPrice.compareTo(tmpSuggestPrice) > 0) {
                                        tmpBigDecimal = tmpStandardPrice;
                                    } else {
                                        tmpBigDecimal = tmpSuggestPrice;
                                    }

                                    if (reducePricing == 1) {
                                        tmpFinalPrice = tmpBigDecimal.subtract(priceReduceDOA);
                                    }
                                    if (tmpFinalPrice.compareTo(finalPrice) > 0) {
                                        finalPrice = tmpFinalPrice;
                                        standardPrice = tmpStandardPrice;
                                        suggestPrice = tmpSuggestPrice;
                                    }
                                } else {
                                    for (ProposeCreditInfoTierDetail proposeCreditInfoTierDetail : proposeCreditInfo.getProposeCreditInfoTierDetailList()) {
                                        if (proposeCreditInfoTierDetail.getBrmsFlag() == 1) {
                                            finalPrice = proposeCreditInfoTierDetail.getFinalInterest().add(proposeCreditInfoTierDetail.getFinalBasePrice().getValue());
                                        }
                                    }
                                    standardPrice = proposeCreditInfo.getStandardInterest().add(proposeCreditInfo.getStandardBasePrice().getValue());
                                    suggestPrice = proposeCreditInfo.getSuggestInterest().add(proposeCreditInfo.getSuggestBasePrice().getValue());

                                    if (standardPrice.compareTo(suggestPrice) > 0) {
                                        tmpBigDecimal = standardPrice;
                                    } else {
                                        tmpBigDecimal = suggestPrice;
                                    }

                                    if (reducePricing == 1) {
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

            //Check for Appraisal Require
            int appraisalRequire = calculateAppraisalRequest(newCreditFacility);

            log.debug("calculatePricingDOA ::: requestPricing : {}", requestPricing);
            //WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setRequestPricing(requestPricing);
            workCase.setPricingDoaLevel(pricingDOALevel.value());
            workCase.setRequestAppraisalRequire(appraisalRequire);
            //workCaseDAO.persist(workCase);
        }
        return workCase;
    }

    public int calculateInsuranceRequired(ProposeLine newCreditFacility){
        int insuranceRequire = 0;
        int insuranceRequireCount = 0;
        if(!Util.isNull(newCreditFacility)){
            if(!Util.isNull(newCreditFacility.getProposeCollateralInfoList()) && newCreditFacility.getProposeCollateralInfoList().size() > 0){
                for(ProposeCollateralInfo newCollateral : newCreditFacility.getProposeCollateralInfoList()){
                    if(!Util.isNull(newCollateral.getProposeCollateralInfoHeadList()) && newCollateral.getProposeCollateralInfoHeadList().size() > 0){
                        for(ProposeCollateralInfoHead newCollateralHead : newCollateral.getProposeCollateralInfoHeadList()){
                            if(!Util.isNull(newCollateralHead.getProposeCollateralInfoSubList()) && newCollateralHead.getProposeCollateralInfoSubList().size() > 0){
                                for(ProposeCollateralInfoSub newCollateralSub : newCollateralHead.getProposeCollateralInfoSubList()){
                                    if(!Util.isNull(newCollateralSub.getSubCollateralType()) && newCollateralSub.getSubCollateralType().getId() != 0){
                                        if(!Util.isZero(newCollateralSub.getSubCollateralType().getInsuranceFlag())){
                                            insuranceRequireCount = insuranceRequireCount + 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(insuranceRequireCount > 0){
            insuranceRequire = 1;
        }
        return insuranceRequire;
    }

    public int calculateAppraisalRequest(ProposeLine newCreditFacility){
        int appraisalRequire = 0;
        int appraisalRequireCount = 0;
        if(!Util.isNull(newCreditFacility)){
            if(!Util.isNull(newCreditFacility.getProposeCollateralInfoList()) && newCreditFacility.getProposeCollateralInfoList().size() > 0){
                for(ProposeCollateralInfo newCollateral : newCreditFacility.getProposeCollateralInfoList()){
                    if(!Util.isNull(newCollateral.getProposeCollateralInfoHeadList()) && newCollateral.getProposeCollateralInfoHeadList().size() > 0){
                        for(ProposeCollateralInfoHead newCollateralHead : newCollateral.getProposeCollateralInfoHeadList()){
                            if(!Util.isNull(newCollateralHead.getHeadCollType()) && newCollateralHead.getHeadCollType().getId() != 0){
                                if(newCollateralHead.getHeadCollType().getAppraisalRequire() == 1){
                                    if(!Util.isNull(newCollateral.getAppraisalDate())){
                                        if(Util.calAge(newCollateral.getAppraisalDate()) > 1){
                                            appraisalRequireCount = appraisalRequireCount + 1;
                                        }
                                    } else {
                                        appraisalRequireCount = appraisalRequireCount + 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(appraisalRequireCount > 0){
            appraisalRequire = 1;
        }

        return appraisalRequire;
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
        List<User> authorizationDOAList = userToAuthorizationDOADAO.getUserListFromDOALevel(doaLevelId, getCurrentUserID());
        if(authorizationDOAList == null){
            authorizationDOAList = new ArrayList<User>();
        }
        return authorizationDOAList;
    }

    public void cancelCA(String queueName, String wobNumber, int reasonId, String remark) throws Exception {
        log.info("Cancel CA - queueName : {}, wobNumber : {}, reasonId : {}, remark : {}", queueName, wobNumber, reasonId, remark);
        bpmExecutor.cancelCase(queueName, wobNumber, ActionCode.CANCEL_CA.getVal(), getReasonDescription(reasonId), remark);
    }

    public void saveCancelRejectInfo(long workCaseId, long workCasePreScreenId, int reasonId){
        CancelRejectInfo cancelRejectInfo = new CancelRejectInfo();
        cancelRejectInfo.setReason(reasonDAO.findById(reasonId));
        if(workCaseId != 0)
            cancelRejectInfo.setWorkCase(workCaseDAO.findById(workCaseId));
        if(workCasePreScreenId != 0)
            cancelRejectInfo.setWorkCasePrescreen(workCasePrescreenDAO.findById(workCasePreScreenId));

        cancelRejectInfoDAO.persist(cancelRejectInfo);

    }

    public void cancelRequestPriceReduction(String queueName, String wobNumber, int reasonId, String remark) throws Exception {
        bpmExecutor.cancelRequestPriceReduction(queueName, wobNumber, getReasonDescription(reasonId), remark, ActionCode.CANCEL_REQUEST_PRICE_REDUCTION.getVal());
    }

    private String getDecisionFlag(int decisionType){
        String decisionFlag = "";
        if(decisionType == DecisionType.APPROVED.getValue()){
            decisionFlag = "A";
        }else if(decisionType == DecisionType.REJECTED.getValue()){
            decisionFlag = "R";
        }

        return  decisionFlag;
    }

    public List<Reason> getReasonList(ReasonTypeValue reasonTypeValue){
        ReasonType reasonType = reasonTypeDAO.findById(reasonTypeValue.value());
        List<Reason> reasonList = reasonDAO.getList(reasonType);
        if(reasonList == null){
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateCSIDataFullApp(long workCaseId) throws Exception{
        List<Customer> customers = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customers);
        List<CSIResult> csiResultList = new ArrayList<CSIResult>();
        long customerId = 0;
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            customerId = customerInfoView.getId();
            log.debug("updateCSIDataFullApp ::: customerId : {}", customerId);
            if(customerId != 0){
                List<CustomerAccount> customerAccountList = customerAccountDAO.getCustomerAccountByCustomerId(customerId);
                log.debug("updateCSIDataFullApp ::: customerAccountList : {}", customerAccountList);
                List<CustomerAccountName> customerAccountNameList = customerAccountNameDAO.getCustomerAccountNameByCustomerId(customerId);
                log.debug("updateCSIDataFullApp ::: customerAccountNameList : {}", customerAccountNameList);

                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();
                for(CustomerAccount customerAccount : customerAccountList){
                    AccountInfoId accountInfoId = new AccountInfoId();
                    accountInfoId.setIdNumber(customerAccount.getIdNumber());
                    if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 1){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CITIZEN_ID);
                    }else if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 2){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.PASSPORT);
                    }else if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 3){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CORPORATE_ID);
                    }
                    accountInfoIdList.add(accountInfoId);
                }

                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                //Add default account name to check CSI
                AccountInfoName accountInfoName = new AccountInfoName();
                accountInfoName.setNameTh(customerInfoView.getFirstNameTh());
                accountInfoName.setNameEn(customerInfoView.getFirstNameEn());
                accountInfoName.setSurnameTh(customerInfoView.getLastNameTh());
                accountInfoName.setSurnameEn(customerInfoView.getLastNameEn());
                accountInfoNameList.add(accountInfoName);

                for(CustomerAccountName customerAccountName : customerAccountNameList){
                    accountInfoName = new AccountInfoName();

                    accountInfoName.setNameTh(customerAccountName.getNameTh());
                    accountInfoName.setNameEn(customerAccountName.getNameEn());
                    accountInfoName.setSurnameTh(customerAccountName.getSurnameTh());
                    accountInfoName.setSurnameEn(customerAccountName.getSurnameEn());

                    accountInfoNameList.add(accountInfoName);
                }

                log.debug("updateCSIDataFullApp ::: accountInfoIdList : {}", accountInfoIdList);
                log.debug("updateCSIDataFullApp ::: accountInfoNameList : {}", accountInfoNameList);

                CSIInputData csiInputData = new CSIInputData();
                csiInputData.setIdModelList(accountInfoIdList);
                csiInputData.setNameModelList(accountInfoNameList);

                log.info("getCSI ::: csiInputData : {}", csiInputData);
                CSIResult csiResult = new CSIResult();
                String idNumber = "";
                Customer customer = new Customer();
                if(customerInfoView.getCustomerEntity().getId() == 1){
                    idNumber = customerInfoView.getCitizenId();
                    customer = individualDAO.findCustomerByCitizenIdAndWorkCase(idNumber, workCaseId);
                } else if (customerInfoView.getCustomerEntity().getId() == 2){
                    idNumber = customerInfoView.getRegistrationId();
                    customer = juristicDAO.findCustomerByRegistrationIdAndWorkCase(idNumber, workCaseId);
                }
                try{
                    User user = getCurrentUser();
                    csiResult = rlosInterface.getCSIData(user.getId(), csiInputData);

                    csiResult.setIdNumber(idNumber);
                    csiResult.setActionResult(ActionResult.SUCCESS);
                    csiResult.setResultReason("SUCCESS");
                    csiResultList.add(csiResult);

                    List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();
                    List<CustomerCSI> customerCSIListDel = customerCSIDAO.findCustomerCSIByCustomerId(customerId);
                    customerCSIDAO.delete(customerCSIListDel);

                    customer.setCustomerCSIList(Collections.<CustomerCSI>emptyList());

                    if(csiResult != null && csiResult.getWarningCodeFullMatched() != null && csiResult.getWarningCodeFullMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodeFullMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.F.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    if(csiResult != null && csiResult.getWarningCodePartialMatched() != null && csiResult.getWarningCodePartialMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodePartialMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.P.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    log.info("getCSI ::: customerCSIList : {}", customerCSIList);
                    if(customerCSIList != null && customerCSIList.size() > 0){
                        log.info("getCSI ::: persist item");
                        customerCSIDAO.persist(customerCSIList);
                    }
                    log.info("getCSI ::: end...");

                } catch (Exception ex){
                    log.error("getCSI ::: error ", ex);
                    throw ex;
                }
            }
        }
    }

    public void updateTimeOfCheckCriteria(long workCaseId, long stepId){
        try{
            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerByRole(workCaseId, getCurrentUser().getRole().getId(), getCurrentUserID(), stepId);
            log.debug("Update time of criteria checked [workCaseOwner] : {}", workCaseOwner);
            if(!Util.isNull(workCaseOwner)) {
                int timesOfCriteriaChecked = workCaseOwner.getTimesOfCriteriaChecked();
                timesOfCriteriaChecked = timesOfCriteriaChecked + 1;
                workCaseOwner.setTimesOfCriteriaChecked(timesOfCriteriaChecked);
                log.debug("Update time of criteria checked [timeOfCriteriaCheck] : {}", timesOfCriteriaChecked);
                workCaseOwnerDAO.persist(workCaseOwner);
            }
        }catch(Exception ex){
            log.error("Exception while update time of check criteria.", ex);
        }
    }

    public void clearCaseUpdateFlag(long workCaseId){
        try{
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setCaseUpdateFlag(0);
            workCaseDAO.persist(workCase);
        }catch (Exception ex){
            log.debug("Exception while clear case update flag : ", ex);
        }
    }

    public int getTimesOfCriteriaCheck(long workCaseId, long stepId){
        int timesOfCriteriaCheck = 0;
        try{
            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerByRole(workCaseId, getCurrentUser().getRole().getId(), getCurrentUserID(), stepId);
            if(!Util.isNull(workCaseOwner)){
                log.debug("getTimesOfCriteriaCheck ::: workCaseOwner : {}", workCaseOwner);
                timesOfCriteriaCheck = workCaseOwner.getTimesOfCriteriaChecked();
            }
            log.debug("getTimesOfCriteriaCheck ::: timesOfCriteriaCheck : {}", timesOfCriteriaCheck);
        }catch(Exception ex){
            log.error("Exception while get time of check criteria : ", ex);
        }

        return timesOfCriteriaCheck;
    }

    public int getRequestAppraisalRequire(long workCaseId){
        int requestAppraisalRequire = 0;
        try{
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                requestAppraisalRequire = workCase.getRequestAppraisalRequire();
            }
        }catch (Exception ex){
            log.error("Exception while getRequestAppraisalRequire : ", ex);
        }

        return requestAppraisalRequire;
    }

    public boolean checkCaseUpdate(long workCaseId){
        boolean caseUpdateFlag = false;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(!Util.isNull(workCase)) {
            caseUpdateFlag = Util.isTrue(workCase.getCaseUpdateFlag());
        }

        return caseUpdateFlag;
    }

    public void calculateApprovedResult(long workCaseId){
        log.debug("calculateApprovedResult");
        try {
            Decision decision = decisionDAO.findByWorkCaseId(workCaseId);
            ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
            log.debug("calculateApprovedResult ::: decision : {}", decision);
            log.debug("calculateApprovedResult ::: prpose : {}", newCreditFacility);

            //Compare for total approved equal to propose or not
            int sameRequest = 1;
            if (!Util.isNull(decision)) {
                BigDecimal totalApprovedCredit = decision.getTotalApproveCredit();
                BigDecimal totalProposedCredit = newCreditFacility.getTotalPropose();
                if (!Util.isNull(totalApprovedCredit) && !Util.isNull(totalProposedCredit)) {
                    if (totalProposedCredit.compareTo(totalApprovedCredit) != 0) {
                        sameRequest = 0;
                    }
                }
            }
            log.debug("calculateApprovedResult ::: sameRequest : {}", sameRequest);
            int approvedType = calculateApprovedType(workCaseId);
            Date limitSetupExpiryDate = calculateLimitSetupExpiryDate();
            int tcgFlag = calculateTCGFlag(workCaseId);
            int premiumQuote = calculatePremiumQuote(workCaseId);

            //Update value in BasicInfo
            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            basicInfo.setApproveResult(sameRequest == 1 ? ApproveResult.SAME_REQUEST : ApproveResult.DIFF_REQUEST);
            basicInfo.setApproveType(approvedType == 1 ? ApproveType.NEW : ApproveType.NEW_CHANGE);
            basicInfo.setLimitSetupExpiryDate(limitSetupExpiryDate);
            basicInfo.setTcgFlag(tcgFlag);
            basicInfo.setPremiumQuote(premiumQuote);

            basicInfoDAO.persist(basicInfo);
        } catch (Exception ex){
            log.error("Exception while Calculate Approved Result : ", ex);
        }
    }

    public int calculateApprovedType(long workCaseId){
        int requestType = 1;        //for new = 1, new+change = 2;
        try {
            log.debug("calculateApprovedType");
            List<ProposeCreditInfo> newCreditDetailApprovedList = newCreditDetailDAO.findNewCreditDetail(workCaseId, ProposeType.A);
            log.debug("calculateApprovedType ::: newCreditDetailApprovedList size : {}", newCreditDetailApprovedList != null ? newCreditDetailApprovedList.size() : null);
            for (ProposeCreditInfo newCreditDetail : newCreditDetailApprovedList) {
                log.debug("calculateApprovedType ::: newCreditDetail : {}", newCreditDetail);
                if (newCreditDetail.getUwDecision() == DecisionType.APPROVED && newCreditDetail.getRequestType() == RequestTypes.CHANGE.value()) {
                    requestType = 2;
                    break;
                }
            }
        } catch (Exception ex){
            log.debug("Exception while calculateApprovedType : ", ex);
        }

        return requestType;
    }

    public Date calculateLimitSetupExpiryDate(){
        Date today = new Date();
        Date limitSetupExpiryDate = DateTimeUtil.addDays(today, 90);

        return limitSetupExpiryDate;
    }

    public int calculatePremiumQuote(long workCaseId){
        int premiumQuote = 0;
        int insuranceFlagCount = 0;
        ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(newCreditFacility)) {
            List<ProposeCollateralInfo> newCollateralList = newCreditFacility.getProposeCollateralInfoList();
            if(!Util.isNull(newCollateralList)){
                for(ProposeCollateralInfo newCollateral : newCollateralList){
                    List<ProposeCollateralInfoHead> newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
                    if(!Util.isNull(newCollateralHeadList)){
                        for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                            List<ProposeCollateralInfoSub> newCollateralSubList = newCollateralHead.getProposeCollateralInfoSubList();
                            if(!Util.isNull(newCollateralSubList)){
                                for(ProposeCollateralInfoSub newCollateralSub : newCollateralSubList){
                                    if(!Util.isNull(newCollateralSub.getSubCollateralType()) && newCollateralSub.getSubCollateralType().getInsuranceFlag() == 1){
                                        insuranceFlagCount = insuranceFlagCount + 1;
                                        break;
                                    }
                                }
                            }
                            if(insuranceFlagCount > 0)
                                break;
                        }
                    }
                    if(insuranceFlagCount > 0)
                        break;
                }
            }
        }

        if(insuranceFlagCount > 0){
            premiumQuote = 1;
        }

        return premiumQuote;
    }

    public int calculateTCGFlag(long workCaseId){
        int tcgFlag = 0;

        //Get all Approve Guarantor to find TCG As Guarantor
        List<ProposeGuarantorInfo> proposeGuarantorInfoList = proposeGuarantorInfoDAO.findApprovedTCGGuarantor(workCaseId);
        if(Util.isSafetyList(proposeGuarantorInfoList) && proposeGuarantorInfoList.size() > 0){
            tcgFlag = 1;
        }

        return tcgFlag;
    }

    /*public User getUserOwnerByRole(long workCaseId, int roleId){
        WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getLatestWorkCaseOwnerByRole(workCaseId, roleId);
        User user = null;
        if(workCaseOwner != null){
            user = workCaseOwner.getUser();
        }

        return user;
    }

    public void addSubmitHistoryInfo(String appNumber, long stepId, long statusId, int reasonId, String remark, String toUserId, SubmitType submitType){
        SubmitInfoHistory submitInfoHistory = new SubmitInfoHistory();
        submitInfoHistory.setAppNumber(appNumber);
        submitInfoHistory.setStep(stepId!=0?stepDAO.findById(stepId):null);
        submitInfoHistory.setStatus(statusId!=0?statusDAO.findById(statusId):null);
        submitInfoHistory.setReason(reasonId!=0?reasonDAO.findById(reasonId):null);
        submitInfoHistory.setRemark(remark);
        submitInfoHistory.setFromUser(getCurrentUser());
        submitInfoHistory.setToUser(!toUserId.equalsIgnoreCase("")?userDAO.findById(toUserId):null);
        submitInfoHistory.setSubmitDate(new Date());
        submitInfoHistory.setSubmitType(submitType.value());
    }*/

    public void duplicateFacilityData(long workCaseId){
        try {
            stpExecutor.duplicateFacilityData(workCaseId);
        }catch (Exception ex){
            log.error("Exception while duplicateFacilityData : ", ex);
        }
    }

    public void duplicateCollateralData(long workCaseId, long workCasePreScreenId){
        try {
            stpExecutor.duplicateCollateralData(workCaseId, workCasePreScreenId);
        }catch (Exception ex){
            log.error("Exception while duplicateFacilityData : ", ex);
        }
    }

    public void checkNCBReject(long workCasePreScreenId, long workCaseId) throws Exception{
        log.debug("ncbResultValidation()");
        UWRuleResultSummary uwRuleResultSummary = null;
        if(!Util.isZero(workCasePreScreenId)) {
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePreScreenId);
        }else if(!Util.isZero(workCaseId)){
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);
        }

        boolean rejectedNCB = false;
        if(!Util.isNull(uwRuleResultSummary)){
            if(uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("ND")) {
                List<UWRuleResultDetail> uwRuleResultDetailList = uwRuleResultSummary.getUwRuleResultDetailList();
                if(Util.isSafetyList(uwRuleResultDetailList)){
                    for(UWRuleResultDetail uwRuleResultDetail : uwRuleResultDetailList){
                        if(uwRuleResultDetail.getUwRuleName() != null &&
                                uwRuleResultDetail.getUwRuleName().getRuleGroup() != null &&
                                uwRuleResultDetail.getUwRuleName().getRuleGroup().getName() != null &&
                                uwRuleResultDetail.getUwRuleName().getRuleGroup().getName().equalsIgnoreCase("NCB")){
                            if(uwRuleResultDetail.getUwDeviationFlag() != null &&
                                    uwRuleResultDetail.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("ND")){
                                if(uwRuleResultDetail.getUwResultColor() == UWResultColor.RED){
                                    log.debug("NCB Result is RED without Deviate, auto reject case!");
                                    rejectedNCB = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(rejectedNCB){
            ncbInterface.generateRejectedLetter(getCurrentUserID(), workCasePreScreenId, workCaseId);
            //Update ncb reject flag
            updateNCBRejectFlag(workCasePreScreenId, workCaseId, 1);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void updateNCBRejectFlag(long workCasePreScreenId, long workCaseId, int isNCBReject){
        if(!Util.isZero(workCasePreScreenId)){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setNcbRejectFlag(isNCBReject);
            workCasePrescreenDAO.persist(workCasePrescreen);
        }else if(!Util.isZero(workCaseId)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setNcbRejectFlag(isNCBReject);
            workCaseDAO.persist(workCase);
        }
    }
}
