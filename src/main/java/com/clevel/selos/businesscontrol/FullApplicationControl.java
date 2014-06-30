package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.history.ReturnInfoHistoryDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelTeamUserDetailsDAO;
import com.clevel.selos.dao.relation.UserToAuthorizationDOADAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalView;
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
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private NewCreditDetailDAO newCreditDetailDAO;
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
    private ReturnInfoTransform returnInfoTransform;
    @Inject
    private UserTransform userTransform;
    @Inject
    private StepTransform stepTransform;

    @Inject
    private AppraisalRequestControl appraisalRequestControl;
    @Inject
    private ActionValidationControl actionValidationControl;

    @Inject
    private BPMExecutor bpmExecutor;

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
        String deviationCode = "";
        String resultCode = "G";
        int requestType = 0;
        int appraisalRequestRequire = 0;
        BigDecimal totalCommercial = BigDecimal.ZERO;
        BigDecimal totalRetail = BigDecimal.ZERO;
        User user = getCurrentUser();
        if(workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            if(workCase != null && workCase.getProductGroup() != null){
                productGroup = workCase.getProductGroup().getName();
                requestType = workCase.getRequestType().getId();
                appraisalRequestRequire = workCase.getRequestAppraisalRequire();

                //TODO: get total com and retail

                UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcaseId(workCaseId);
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

                bpmExecutor.submitZM(queueName, wobNumber, zmUserId, rgmUserId, ghUserId, cssoUserId, totalCommercial, totalRetail, resultCode, productGroup, deviationCode, requestType, appraisalRequestRequire, ActionCode.SUBMIT_CA.getVal());

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
        String resultCode = "G";
        String deviationCode = "";
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

                UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcaseId(workCaseId);
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
        String rgmDecisionFlag = "E";
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
        String ghDecisionFlag = "E";
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
        String cssoDecisionFlag = "A";
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
        String zmDecisionFlag = "A";
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

    public void submitCA(String wobNumber, String queueName, long workCaseId) throws Exception {
        String decisionFlag = "A";
        String haveRG001 = "N"; //TODO
        WorkCase workCase;
        ApprovalHistory approvalHistoryEndorseCA = null;

        String insuranceRequired = "N";
        String approvalFlag = "N";
        String tcgRequired = "N";

        if(Long.toString(workCaseId) != null && workCaseId != 0){
            approvalHistoryEndorseCA = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), ApprovalType.CA_APPROVAL.value());

            if(approvalHistoryEndorseCA==null){
                throw new Exception("Please make decision before submit.");
            } else {
                decisionFlag = approvalHistoryEndorseCA.getApproveDecision() == DecisionType.APPROVED.value()?"A":"R";
                approvalHistoryEndorseCA.setSubmit(1);
                approvalHistoryEndorseCA.setSubmitDate(new Date());
            }

            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if(!Util.isNull(basicInfo)){
                approvalFlag = basicInfo.getApproveResult().value() == 1 ? "Y" : "N";
                tcgRequired = basicInfo.getTcgFlag() == 1 ? "Y" : "N";
            }

            workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                insuranceRequired = workCase.getInsuranceFlag() == 1 ? "Y" : "N";
            }
        }

//        bpmExecutor.submitCA(workCaseId, queueName, decisionFlag, haveRG001, insuranceRequired, approvalFlag, tcgRequired, ActionCode.SUBMIT_CA.getVal());
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

    public void submitToBDM(String queueName, String wobNumber) throws Exception{
        bpmExecutor.submitCase(queueName, wobNumber, ActionCode.SUBMIT_CA.getVal());
    }

    public void calculateApprovedPricingDOA(long workCaseId){
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        workCase = calculatePricingDOA(workCase, newCreditFacility);
        workCaseDAO.persist(workCase);
    }

    public WorkCase calculatePricingDOA(WorkCase workCase, NewCreditFacility newCreditFacility){
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

    public int calculateInsuranceRequired(NewCreditFacility newCreditFacility){
        int insuranceRequire = 0;
        int insuranceRequireCount = 0;
        if(!Util.isNull(newCreditFacility)){
            if(!Util.isNull(newCreditFacility.getNewCollateralDetailList()) && newCreditFacility.getNewCollateralDetailList().size() > 0){
                for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
                    if(!Util.isNull(newCollateral.getNewCollateralHeadList()) && newCollateral.getNewCollateralHeadList().size() > 0){
                        for(NewCollateralHead newCollateralHead : newCollateral.getNewCollateralHeadList()){
                            if(!Util.isNull(newCollateralHead.getNewCollateralSubList()) && newCollateralHead.getNewCollateralSubList().size() > 0){
                                for(NewCollateralSub newCollateralSub : newCollateralHead.getNewCollateralSubList()){
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

    public int calculateAppraisalRequest(NewCreditFacility newCreditFacility){
        int appraisalRequire = 0;
        int appraisalRequireCount = 0;
        if(!Util.isNull(newCreditFacility)){
            if(!Util.isNull(newCreditFacility.getNewCollateralDetailList()) && newCreditFacility.getNewCollateralDetailList().size() > 0){
                for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
                    if(!Util.isNull(newCollateral.getNewCollateralHeadList()) && newCollateral.getNewCollateralHeadList().size() > 0){
                        for(NewCollateralHead newCollateralHead : newCollateral.getNewCollateralHeadList()){
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
        List<User> authorizationDOAList = userToAuthorizationDOADAO.getUserListFromDOALevel(doaLevelId);
        if(authorizationDOAList == null){
            authorizationDOAList = new ArrayList<User>();
        }
        return authorizationDOAList;
    }

    public void cancelRequestAppraisal(String queueName, String wobNumber, int reasonId, String remark) throws Exception {
        bpmExecutor.cancelCase(queueName, wobNumber, ActionCode.CANCEL_APPRAISAL.getVal(), getReasonDescription(reasonId), remark);
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
                for(CustomerAccountName customerAccountName : customerAccountNameList){
                    AccountInfoName accountInfoName = new AccountInfoName();

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

    public void requestParallelAppraisal(long workCaseId, long workCasePreScreenId) throws Exception{
        log.debug("requestParallelAppraisal ::: start, workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);

        if(!Util.isZero(workCaseId)){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            workCase.setParallelAppraisalFlag(1);
            workCaseDAO.persist(workCase);
        } else if(!Util.isZero(workCasePreScreenId)){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCasePrescreen.setParallelAppraisalFlag(1);
            workCasePrescreenDAO.persist(workCasePrescreen);
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
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
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
            List<NewCreditDetail> newCreditDetailApprovedList = newCreditDetailDAO.findNewCreditDetail(workCaseId, ProposeType.A);
            log.debug("calculateApprovedType ::: newCreditDetailApprovedList size : {}", newCreditDetailApprovedList != null ? newCreditDetailApprovedList.size() : null);
            for (NewCreditDetail newCreditDetail : newCreditDetailApprovedList) {
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
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(newCreditFacility)) {
            List<NewCollateral> newCollateralList = newCreditFacility.getNewCollateralDetailList();
            if(!Util.isNull(newCollateralList)){
                for(NewCollateral newCollateral : newCollateralList){
                    List<NewCollateralHead> newCollateralHeadList = newCollateral.getNewCollateralHeadList();
                    if(!Util.isNull(newCollateralHeadList)){
                        for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                            List<NewCollateralSub> newCollateralSubList = newCollateralHead.getNewCollateralSubList();
                            if(!Util.isNull(newCollateralSubList)){
                                for(NewCollateralSub newCollateralSub : newCollateralSubList){
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

        TCG tcg = tcgDAO.findByWorkCaseId(workCaseId);

        if(!Util.isNull(tcg)){
            if(tcg.getTcgFlag() == RadioValue.YES.value())
                tcgFlag = 1;
        }

        return tcgFlag;
    }
}
