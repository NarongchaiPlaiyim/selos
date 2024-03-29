package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.relation.ReasonToStepDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.UWRuleResultSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.UWRuleResultSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "headerController")
public class HeaderController extends BaseController {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    UserDAO userDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    ReasonDAO reasonDAO;
    @Inject
    ReasonToStepDAO reasonToStepDAO;
    @Inject
    UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    private BRMSControl brmsControl;
    @Inject
    private UWRuleResultControl uwRuleResultControl;
    @Inject
    private HeaderControl headerControl;
    @Inject
    FullApplicationControl fullApplicationControl;
    @Inject
    AppraisalRequestControl appraisalRequestControl;
    @Inject
    ReturnControl returnControl;
    @Inject
    StepStatusControl stepStatusControl;
    @Inject
    UserSysParameterControl userSysParameterControl;
    @Inject
    PrescreenBusinessControl prescreenBusinessControl;
    @Inject
    private CheckMandateDocControl checkMandateDocControl;

    @Inject
    ReturnInfoTransform returnInfoTransform;
    @Inject
    AppraisalDetailTransform appraisalDetailTransform;

    private ManageButton manageButton;

    private int qualitativeType;
    private int pricingDOALevel;
    private List<User> bdmCheckerList;
    private List<User> abdmUserList;
    private List<User> zmUserList;
    private List<User> rgmUserList;
    private List<User> ghmUserList;
    private List<User> cssoUserList;
    private List<User> aadCommiteeList;
    private boolean requestPricing;

    private User user;

    private String aadCommitteeId;

    private String bdmCheckerId;
    private String abdmUserId;
    private String assignRemark;

    private String zmEndorseUserId;
    private String zmEndorseRemark;
    private String submitRemark;
    private String slaRemark;

    private String zmUserId;
    private String rgmUserId;
    private String ghmUserId;
    private String cssoUserId;

    private boolean isSubmitToZM;
    private boolean isSubmitToRGM;
    private boolean isSubmitToGHM;
    private boolean isSubmitToCSSO;
    private boolean isSubmitForBDM;
    private boolean isSubmitForUW;
    private boolean isSubmitForUW2;
    private boolean isUWRejected;

    private int submitPricingLevel;
    private int submitOverSLA;

    private int slaReasonId;

    private String aadAdminId;
    private String aadAdminName;

    //Cancel CA FullApp
    private List<Reason> cancelReason;
    private String cancelRemark;
    private int reasonId;
    private int reasonAADId;
    private int reasonBDMId;
    private int reasonBUId;
    private int reasonMakerId;
    private int reasonAADUWId;

    //Return BDM Dialog
    private List<ReturnInfoView> returnInfoViewList;
    private List<ReturnInfoView> returnInfoAADViewList;
    private List<ReturnInfoView> returnInfoBDMViewList;
    private List<ReturnInfoView> returnInfoBUViewList;
    private List<Reason> returnReason;
    private List<Reason> returnAADReason;
    private List<Reason> returnBDMReason;
    private List<Reason> returnBUReason;
    private List<Reason> returnMakerReason;
    private List<Reason> returnAADUWReason;
    private String returnRemark;
    private int editRecordNo;
    private int editAADRecordNo;
    private int editBDMRecordNo;
    private int editBURecordNo;
    private int editMakerRecordNo;
    private int editAADUWRecordNo;
    private List<ReturnInfoView> returnInfoHistoryViewList;

    private HashMap<String, Integer> stepStatusMap;

    //Session variable
    private long workCaseId;
    private long workCasePreScreenId;
    private long stepId;
    private long statusId;
    private int stageId;
    private int requestAppraisal;
    private String queueName;
    private String wobNumber;
    private String slaStatus;
    private String currentUserId;

    private String messageHeader;
    private String message;

    private List<MandateFieldMessageView> mandateFieldMessageViewList;

    //UW submit dialog
    private List<User> uw2UserList;
    private List<AuthorizationDOA> authorizationDOAList;
    private long selectedDOALevel;
    private String selectedUW2User;

    //Customer Acceptance
    private List<Reason> reasonList;
    private String pendingRemark;
    private int pendingReasonId;

    //Request Appraisal ( After Customer Acceptance )
    private List<User> aadAdminList;

    //Return AAD Admin ( UW2 )
    private int returnReasonId;
    private String returnAADRemark;
    private String returnBDMRemark;
    private String returnBURemark;
    private String returnMakerRemark;
    private String returnAADUWRemark;

    //Check Pre-Screen Result
    private boolean canCloseSale;
    private UWResultColor uwResultColor;
    private String deviationFlag = "";

    //Check Criteria Result
    private boolean canSubmitCA;

    //Check Time of Criteria Check
    private boolean canCheckCriteria;

    //Check NCB result (if red , cannot submit CA and cannot check prescreen again)
    private boolean canCheckPreScreen;
    private boolean canCheckFullApp;

    //Check Can Request Appraisal
    private boolean canRequestAppraisal;

    private int timesOfCriteriaCheck;
    private int timesOfPreScreenCheck;

    private int requestAppraisalRequire;

    //Return User Name
    private String userName;

    //Over SLA Reason List
    private List<Reason> slaReasonList;

    //Cancel CA
    private int cancelReasonId;

    //Cancel Request Appraisal
    private int cancelRequestReasonId;
    private String cancelRequestRemark;

    //Cancel Price Reduction
    private int cancelPriceReduceReasonId;
    private String cancelPriceReduceRemark;

    private int requestType;

    public HeaderController() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("HeaderController ::: Creation ");

        _loadSessionVariable();

        manageButton = new ManageButton();

        log.debug("HeaderController ::: getSession : workCasePreScreenId : {}, workCase = {}, stepId = {}, statusId = {}, stageId = {}", workCasePreScreenId, workCaseId, stepId, statusId, stageId);
        log.debug("HeaderController ::: find active button");

        //Get all action from Database By Step and Status and Role
        stepStatusMap = stepStatusControl.getStepStatusByStepStatusRole(stepId, statusId, currentUserId);
        log.debug("HeaderController ::: stepStatusMap : {}", stepStatusMap);

        loadUserAccessMenu();

        canSubmitCA = false;
        canCheckCriteria = false;
        requestType = 1;
        if(workCaseId != 0){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                requestType = workCase.getRequestType() != null ? workCase.getRequestType().getId() : 1;
                UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);
                if(uwRuleResultSummary!=null && uwRuleResultSummary.getId()>0){
                    if(uwRuleResultSummary.getUwResultColor() == UWResultColor.GREEN || uwRuleResultSummary.getUwResultColor() == UWResultColor.YELLOW){
                        canSubmitCA = true;
                        canRequestAppraisal = true;
                    } else {
                        if(uwRuleResultSummary.getUwDeviationFlag()!=null && uwRuleResultSummary.getUwDeviationFlag().getBrmsCode()!=null && !uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("")){
                            if(uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("AD") || uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("AI")){
                                canSubmitCA = true;
                                canRequestAppraisal = true;
                            }
                        }
                    }
                } else {
                    canRequestAppraisal = true;
                }
                canCheckFullApp = true;
                timesOfCriteriaCheck = fullApplicationControl.getTimesOfCriteriaCheck(workCaseId, stepId);
                UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("CHK_CRI_LIM");

                log.debug("userSysParameterView : {}", userSysParameterView);

                int limitTimeOfCriteriaCheck = 100;

                if(!Util.isNull(userSysParameterView))
                    limitTimeOfCriteriaCheck = Util.parseInt(userSysParameterView.getValue(), 0);

                if(timesOfCriteriaCheck < limitTimeOfCriteriaCheck)
                    canCheckCriteria = true;

                requestAppraisalRequire = fullApplicationControl.getRequestAppraisalRequire(workCaseId);
            }

            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if(basicInfo != null)
                qualitativeType = basicInfo.getQualitativeType();

            log.debug("canSubmit : {}, canCheckCriteria : {}, canCheckFullApp : {}", canSubmitCA, canCheckCriteria, canCheckFullApp);
        }

        canCloseSale = false;
        if(workCasePreScreenId != 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(!Util.isNull(workCasePrescreen)){
                requestType = workCasePrescreen.getRequestType() != null ? workCasePrescreen.getRequestType().getId() : 1;
                UWRuleResultSummary uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePreScreenId);
                if(uwRuleResultSummary != null && uwRuleResultSummary.getId() > 0){
                    if(uwRuleResultSummary.getUwResultColor() == UWResultColor.GREEN || uwRuleResultSummary.getUwResultColor() == UWResultColor.YELLOW){
                        canCloseSale = true;
                        canRequestAppraisal = true;
                    }else{
                        if(uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("AD")
                                || uwRuleResultSummary.getUwDeviationFlag().getBrmsCode().equalsIgnoreCase("AI")){
                            canCloseSale = true;
                            canRequestAppraisal = true;
                        }
                    }
                } else {
                    canRequestAppraisal = false;
                }

                timesOfPreScreenCheck = prescreenBusinessControl.getTimesOfPreScreenCheck(workCasePreScreenId, stepId);
                UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("CHK_PRE_LIM");
                int limitTimeOfPreScreenCheck = 100;

                if(!Util.isNull(userSysParameterView))
                    limitTimeOfPreScreenCheck = Util.parseInt(userSysParameterView.getValue(), 0);

                if(timesOfPreScreenCheck < limitTimeOfPreScreenCheck)
                    canCheckPreScreen = true;

            }
        }
    }

    private void _loadSessionVariable(){
        HttpSession session = FacesUtil.getSession(false);

        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        stageId = Util.parseInt(session.getAttribute("stageId"), 0);
        requestAppraisal = Util.parseInt(session.getAttribute("requestAppraisal"), 0);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
        slaStatus = Util.parseString(session.getAttribute("slaStatus"), "");
        currentUserId = Util.parseString(session.getAttribute("caseOwner"), "");
        user = (User) session.getAttribute("user");
        if (user == null) {
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = userDAO.findById(userDetail.getUserName());
            session = FacesUtil.getSession(false);
            session.setAttribute("user", user);
        }
    }

    public boolean checkButton(String buttonName){
        boolean check = false;
        if(stepStatusMap != null && stepStatusMap.containsKey(buttonName))
            check = Util.isTrue(stepStatusMap.get(buttonName));

        return check;
    }

    //************** FUNCTION FOR PRE_SCREEN STAGE *************//
    //-------------- Assign to ABDM -------------//
    public void onOpenAssignToABDM(){
        log.debug("onOpenAssignToABDM ::: starting...");
        abdmUserId = "";
        assignRemark = "";
        abdmUserList = fullApplicationControl.getABDMUserList();
        log.debug("onOpenAssignToABDM ::: abdmUserList size : {}", abdmUserList.size());
    }

    public void onAssignToABDM(){
        log.debug("onAssignToABDM ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        if(abdmUserId != null && !abdmUserId.equals("")){
            try{
                fullApplicationControl.assignToABDM(queueName, wobNumber, abdmUserId);
                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.message.dialog.assign.abdm.success");
                showMessageRedirect();
                complete = true;
                log.debug("onAssignToABDM ::: success.");
            } catch (Exception ex){
                messageHeader = msg.get("app.messageHeader.exception");
                message = Util.getMessageException(ex);
                showMessageBox();
                complete = false;
                log.error("Exception while onAssignToABDM : ", ex);
            }
        }
        sendCallBackParam(complete);
    }

    //-------------- Check Pre_Screen ---------------//
    public void onCheckTimeOfCheckPreScreen(){
        _loadSessionVariable();
        log.debug("onCheckTimeOfCheckPreScreen");
        UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("CHK_PRE_LIM");
        int limitTimeOfPreScreenCheck = 100;
        int currentTimeOfCheckPreScreen = timesOfPreScreenCheck + 1;

        if(!Util.isNull(userSysParameterView))
            limitTimeOfPreScreenCheck = Util.parseInt(userSysParameterView.getValue(), 0);

        if(Util.compareInt(currentTimeOfCheckPreScreen, limitTimeOfPreScreenCheck) >= 0){
            //To show message to confirm check criteria.
            RequestContext.getCurrentInstance().execute("confirmCheckPreScreen.show()");
        }else{
            onCheckPreScreen();
        }
    }

    public void onCheckPreScreen(){
        boolean success = false;
        _loadSessionVariable();
        if(workCasePreScreenId != 0){
            try{
                mandateFieldMessageViewList = null;
                int modifyFlag = prescreenBusinessControl.getModifyValue(workCasePreScreenId);
                if (modifyFlag == 0) {
                    mandateFieldMessageViewList = null;

                    prescreenBusinessControl.updateCSIData(workCasePreScreenId);
                    UWRuleResponseView uwRuleResponseView = brmsControl.getPrescreenResult(workCasePreScreenId, ActionCode.CHECK_PRESCREEN.getVal());
                    log.info("onCheckPreScreen uwRulesResponse : {}", uwRuleResponseView);
                    if(uwRuleResponseView != null){
                        if(uwRuleResponseView.getActionResult().equals(ActionResult.SUCCESS)){
                            UWRuleResultSummaryView uwRuleResultSummaryView = null;
                            try{
                                uwRuleResultSummaryView = uwRuleResponseView.getUwRuleResultSummaryView();
                                uwRuleResultSummaryView.setWorkCasePrescreenId(workCasePreScreenId);
                                uwRuleResultControl.saveNewUWRuleResult(uwRuleResultSummaryView);

                                //----Update Times of Check Criteria----//
                                prescreenBusinessControl.updateTimeOfCheckPreScreen(workCasePreScreenId, stepId);
                            }catch (Exception ex){
                                log.error("Cannot Save UWRuleResultSummary {}", uwRuleResultSummaryView);
                                messageHeader = "Exception.";
                                message = Util.getMessageException(ex);
                            }
                            messageHeader = "Information.";
                            message = "Request for Check Pre-Screen and Save data success.";
                            success = true;
                        }else {
                            messageHeader = "Exception.";
                            message = uwRuleResponseView.getReason();
                            mandateFieldMessageViewList = uwRuleResponseView.getMandateFieldMessageViewList();
                        }
                    } else {
                        uwRuleResultControl.saveNewUWRuleResult(uwRuleResponseView.getUwRuleResultSummaryView());
                        messageHeader = "Information.";
                        message = "There is no data returned from getPrescreen. Please contact system administrator";
                    }
                } else {
                    messageHeader = "Warning.";
                    message = "Cannot Check Prescreen. Some of data has been modified. Please Retrieve Interface Info and Save.";
                }
            } catch (Exception ex){
                log.error("Exception while getPrescreenResult : ", ex);
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
            }

            if(mandateFieldMessageViewList == null || mandateFieldMessageViewList.size() == 0)
                if(success)
                    showMessageRefresh();
                else
                    showMessageBox();
            else
                showMessageMandate();
        }
    }

    public void onOpenCloseSales(){
        //Check SLA Over or not?
        _loadSessionVariable();
        submitOverSLA = slaStatus.equalsIgnoreCase("R") ? 1 : 0;
        if(submitOverSLA == 1){
            //Show Close sales dialog with SLA Reason
            reasonId = 0;
            reasonList = reasonToStepDAO.getOverSLAReason(stepId);
            slaRemark = "";
        }else{
            submitRemark = "";
        }

        RequestContext.getCurrentInstance().execute("closeSalesDlg.show()");
    }

    public void onCloseSales(){
        _loadSessionVariable();
        log.debug("onCloseSale ::: queueName : {}", queueName);
        boolean complete = false;
        String tmpRemark = "";
        try {
            if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)){

                int modifyFlag = prescreenBusinessControl.getModifyValue(workCasePreScreenId);
                log.debug("modifyFlag : {}", modifyFlag);
                if (modifyFlag == 1) {
                    messageHeader = "Exception";
                    message = "Some of data has been changed. Please Retrive Interface before Closesale.";
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                } else if (modifyFlag == 2) {
                    messageHeader = "Exception";
                    message = "Could not get data for PreScreen.";
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                } else {
                    if (submitOverSLA == 1) {
                        if (reasonId != 0 && !Integer.toString(reasonId).equals("")) {
                            complete = true;
                            tmpRemark = slaRemark;
                        }
                    } else {
                        tmpRemark = submitRemark;
                        complete = true;
                    }
                    log.debug("complete : {}", complete);
                    if (complete) {
                        log.debug("complete true : starting duplicate data ");
                        prescreenBusinessControl.duplicateData(queueName, wobNumber, ActionCode.CLOSE_SALES.getVal(), workCasePreScreenId, reasonId, tmpRemark);
                        returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);
                        log.debug("Duplicate data complete and submit complete.");
                        messageHeader = "Information.";
                        message = "Close Sales Complete. Click 'OK' return Inbox.";
                        showMessageRedirect();
                    }
                }
            } else {
                log.debug("onCloseSales ::: MandateDoc not complete");
                messageHeader = "Warning.";
                message = "Cannot " + ActionCode.ASSIGN_TO_CHECKER.getActionName() + ". Some Mandate Document are incompleted.";
                showMessageBox();
            }
        } catch (Exception ex) {
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            log.error("onCloseSales error : ", ex);
            showMessageBox();
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onOpenAssignCheckerDialog() {
        log.debug("onOpenAssignCheckerDialog");
        try {
            if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)){
                log.debug("onOpenAssignDialog ::: MandateDoc complete");
                bdmCheckerList = userDAO.findBDMChecker(user);
                bdmCheckerId = "";
                assignRemark = "";
                log.debug("onOpenAssignDialog ::: bdmCheckerList size : {}", bdmCheckerList.size());
                RequestContext.getCurrentInstance().execute("assignCheckerDlg.show()");
            } else {
                log.debug("onOpenAssignDialog ::: MandateDoc not complete");
                messageHeader = "Warning.";
                message = "Cannot " + ActionCode.ASSIGN_TO_CHECKER.getActionName() + ". Some Mandate Document are incompleted.";
                showMessageBox();
            }
        } catch (Exception ex) {
            messageHeader = "Exception.";
            message = "Exception while open Assign to Checker dialog. Please try again.";
            showMessageBox();
        }
    }

    public void onAssignToChecker() {
        log.debug("onAssignToChecker ::: starting...");
        boolean complete = false;
        try {
            if(canSubmitWithoutReplyDetail(workCaseId, workCasePreScreenId)){
                if (bdmCheckerId != null && !bdmCheckerId.equals("")) {
                    prescreenBusinessControl.assignChecker(queueName, wobNumber, ActionCode.ASSIGN_TO_CHECKER.getVal(), workCasePreScreenId, bdmCheckerId, assignRemark);
                    returnControl.updateReplyDate(workCaseId, workCasePreScreenId);
                    complete = true;
                    messageHeader = "Information.";
                    message = "Assign to checker complete.";
                    showMessageRedirect();
                }
            } else {
                messageHeader = "Assign to checker failed.";
                message = "Please update reply detail before submit.";
                showMessageBox();
                RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
            }

            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
            log.debug("onAssignToChecker ::: complete");
        } catch (Exception ex) {
            messageHeader = "Assign to checker failed.";
            message = "Assign to checker failed. Cause : " + Util.getMessageException(ex);
            showMessageBox();
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);

            log.error("onAssignToChecker ::: exception : {}", ex);
        }
    }
    //************** END FUNCTION FOR PRE_SCREEN STAGE ***************//

    //************** FUNCTION FOR FULL APPLICATION STAGE ************//
    //-------------- Submit CA [ Generic for BU ] -----------//
    public boolean checkSubmitBU(){
        boolean isSubmitBU = true;
        if(stepId == StepValue.CREDIT_DECISION_UW1.value() || stepId == StepValue.CREDIT_DECISION_UW2.value() ||
                stepId == StepValue.CREDIT_DECISION_UW1_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM.value() ||
                    stepId == StepValue.CREDIT_DECISION_UW1_CORRECT_INFO_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM_UPD_INFO.value()){
            isSubmitBU = false;
        }
        return isSubmitBU;
    }

    private boolean checkStepABDM(){
        boolean isStepABDM = false;
        if(stepId == StepValue.FULLAPP_ABDM.value() || stepId == StepValue.CREDIT_DECISION_BU_ABDM.value() ||
                stepId == StepValue.CREDIT_DECISION_UW1_ABDM.value() || stepId == StepValue.CREDIT_DECISION_UW1_ABDM_UPDATE.value() ||
                stepId == StepValue.CREDIT_DECISION_UW2_ABDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_ABDM_UPDATE.value()){
            isStepABDM = true;
        }

        return isStepABDM;
    }

    public void onOpenSubmitBU(){
        _loadSessionVariable();
        log.debug("onOpenSubmitFullApplication ::: Start... workCaseId : [{}], stepId : [{}], statusId : [{}]", workCaseId, stepId, statusId);
        try{
            if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)){
                if(!fullApplicationControl.checkCaseUpdate(workCaseId)){
                    //Check for Request Pricing
                    requestPricing = fullApplicationControl.getRequestPricing(workCaseId);
                    log.debug("onOpenSubmitFullApplication ::: requestPricing : {}", requestPricing);

                    submitOverSLA = slaStatus.equalsIgnoreCase("R") ? 1 : 0;
                    if(submitOverSLA == 1){
                        slaReasonList = reasonToStepDAO.getOverSLAReason(stepId);
                    }

                    if(!checkStepABDM()){
                        if(requestPricing){
                            if(stepId != StepValue.CREDIT_DECISION_BU_ZM.value()){
                                //Check for Pricing DOA Level
                                pricingDOALevel = fullApplicationControl.getPricingDOALevel(workCaseId);
                                log.debug("onOpenSubmitFullApplication ::: pricingDOALevel : {}", pricingDOALevel);
                                if(pricingDOALevel != 0) {
                                    zmEndorseUserId = "";
                                    zmUserId = "";
                                    rgmUserId = "";
                                    ghmUserId = "";
                                    cssoUserId = "";

                                    zmEndorseRemark = "";
                                    submitRemark = "";
                                    slaRemark = "";

                                    isSubmitToZM = false;
                                    isSubmitToRGM = false;
                                    isSubmitToGHM = false;
                                    isSubmitToCSSO = false;

                                    //TO Get all owner of case
                                    getUserOwnerBU();

                                    log.debug("onOpenSubmitFullApplication ::: stepId : {}", stepId);
                                    if (stepId <= StepValue.FULLAPP_BDM.value() || stepId == StepValue.REVIEW_PRICING_REQUEST_BDM.value()) {
                                        //if(stepId <= StepValue.FULLAPP_BDM.value()) {
                                        zmUserList = fullApplicationControl.getUserList(user);
                                        log.debug("onOpenSubmitFullApplication ::: zmUserList : {}", zmUserList);
                                    }

                                    //TO Disabled DDL DOA Lower than RGM
                                    if ((stepId > StepValue.FULLAPP_BDM.value() && stepId <= StepValue.FULLAPP_ZM.value()) || stepId == StepValue.REVIEW_PRICING_REQUEST_ZM.value()) {         //Step After BDM Submit to ZM ( Current Step [2002] )
                                        //if(stepId > StepValue.FULLAPP_BDM.value() && stepId <= StepValue.FULLAPP_ZM.value()) {         //Step After BDM Submit to ZM ( Current Step [2002] )
                                        isSubmitToZM = false;
                                    }

                                    //TO Disabled DDL DOA Lower than GH
                                    if ((stepId > StepValue.FULLAPP_ZM.value() && stepId <= StepValue.REVIEW_PRICING_REQUEST_RGM.value()) && !(stepId == StepValue.REVIEW_PRICING_REQUEST_BDM.value() || stepId == StepValue.REVIEW_PRICING_REQUEST_ZM.value())) {    //Step After Zone Submit to Region
                                        //if(stepId > StepValue.FULLAPP_ZM.value() && stepId <= StepValue.REVIEW_PRICING_REQUEST_RGM.value()){    //Step After Zone Submit to Region
                                        isSubmitToZM = false;
                                        isSubmitToRGM = false;
                                    }

                                    //TO Disabled DDL DOA Lower than CSSO
                                    if (stepId > StepValue.REVIEW_PRICING_REQUEST_RGM.value() && stepId <= StepValue.REVIEW_PRICING_REQUEST_GH.value()) {
                                        isSubmitToZM = false;
                                        isSubmitToRGM = false;
                                        isSubmitToGHM = false;
                                    }
                                    //TO All ( End of Pricing DOA )
                                    if (stepId > StepValue.REVIEW_PRICING_REQUEST_GH.value() && stepId <= StepValue.REVIEW_PRICING_REQUEST_CSSO.value()) {
                                        isSubmitToZM = false;
                                        isSubmitToRGM = false;
                                        isSubmitToGHM = false;
                                        isSubmitToCSSO = false;
                                    }
                                    RequestContext.getCurrentInstance().execute("submitBUDlg.show()");
                                } else {
                                    messageHeader = msg.get("app.messageHeader.exception");
                                    message = msg.get("app.message.dialog.doapricing.notfound");
                                    showMessageBox();
                                }

                            } else {
                                isSubmitToZM = false;
                                RequestContext.getCurrentInstance().execute("submitBUDlg.show()");
                            }
                        } else {
                            if((stepId > StepValue.FULLAPP_BDM.value() && stepId <= StepValue.FULLAPP_ZM.value()) || stepId == StepValue.CREDIT_DECISION_BU_ZM.value()) {         //Step After BDM Submit to ZM ( Current Step [2002] )
                                isSubmitToZM = false;
                            } else {
                                isSubmitToZM = true;
                                zmUserList = fullApplicationControl.getUserList(user);
                                log.debug("onOpenSubmitZM ::: No pricing request");
                            }
                            RequestContext.getCurrentInstance().execute("submitBUDlg.show()");
                        }
                    } else {
                        isSubmitForBDM = true;
                        isSubmitToZM = false;
                        isSubmitToRGM = false;
                        isSubmitToGHM = false;
                        isSubmitToCSSO = false;
                        RequestContext.getCurrentInstance().execute("submitBUDlg.show()");
                    }
                } else {
                    //----Case is updated please check criteria before submit----
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = "CA information is updated, please Check Criteria before submit.";
                    showMessageBox();
                }
            } else {
                messageHeader = "Warning.";
                message = "Cannot " + ActionCode.SUBMIT_CA.getActionName() + ". Some Mandate Document are incompleted.";
                showMessageBox();
            }
        }catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    public void onSubmitBU(){
        _loadSessionVariable();
        //Submit case by Step Id
        if(checkStepABDM()) {
            //Submit case from ABDM to BDM
            submitForABDM();
        }else if(stepId == StepValue.FULLAPP_BDM.value()) {
            //Submit case from BDM to ZM ( Step 2001 )
            submitForBDM();
        }else if(stepId == StepValue.FULLAPP_ZM.value()) {
            //Submit case from ZM to RGM or UW ( DOA Level ) ( Step 2002 ) and Submit for FCash 2nd time ( Step 2009 )
            submitForZM();
        }else if(stepId == StepValue.CREDIT_DECISION_BU_ZM.value()) {
            submitForZMFCash();
        }else if(stepId == StepValue.REVIEW_PRICING_REQUEST_BDM.value()) {
            //Submit case from BDM to ZM ( Price Reduction ) ( Step 2018 )
            submitForBDMReducePrice();
        }else if(stepId == StepValue.REVIEW_PRICING_REQUEST_ZM.value()){
            //Submit case from BDM to ZM ( Price Reduction ) ( Step 2019 )
            submitForZMReducePrice();
        }else if(stepId == StepValue.REVIEW_PRICING_REQUEST_RGM.value()){
            //Submit case from RGM to GH or UW ( DOA Level ) ( Step 2020 )
            submitForRGM();
        }else if(stepId == StepValue.REVIEW_PRICING_REQUEST_GH.value()){
            //Submit case from GH to CSSO or UW ( DOA Level ) ( Step 2021 )
            submitForGH();
        }else if(stepId == StepValue.REVIEW_PRICING_REQUEST_CSSO.value()){
            //Submit case from CSSO to UW ( DOA Level ) ( Step 2022 )
            submitForCSSO();
        }
        //TO Save Selected UserId to BasicInfo
        fullApplicationControl.saveSelectedUserBU(zmUserId, rgmUserId, ghmUserId, cssoUserId, workCaseId);
        RequestContext.getCurrentInstance().execute("blockUI.hide()");
    }

    private void submitForABDM(){
        log.debug("submitForABDM");
        try{
            fullApplicationControl.submitForABDM(queueName, wobNumber, submitRemark, slaRemark, slaReasonId, workCaseId);

            messageHeader = "Information.";
            message = "Submit case success.";

            showMessageRedirect();
        } catch (Exception ex) {
            log.error("Exception while submit to BDM ( from ABDM ), ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);

            showMessageBox();
        }
    }

    private void submitForBDM(){
        log.debug("Submit Case from BDM to ZM Starting..., stepId : {}", stepId);
        boolean complete = false;
        if(zmUserId != null && !zmUserId.equals("")){
            try{
                if(canSubmitWithoutReplyDetail(workCaseId,workCasePreScreenId)){
                    fullApplicationControl.submitForBDM(queueName, wobNumber, zmUserId, rgmUserId, ghmUserId, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId);
                    log.debug("submitForBDM ::: success.");
                    log.debug("submitForBDM ::: Backup return info to History Start...");
                    returnControl.updateReplyDate(workCaseId, workCasePreScreenId);
                    log.debug("submitForBDM ::: Backup return info to History Success...");
                    messageHeader = msg.get("app.messageHeader.info");
                    message = msg.get("app.message.dialog.submit.success");
                    showMessageRedirect();
                    complete = true;
                } else {
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = "Please update return reply detail before submit.";
                    showMessageBox();
                    log.error("submitForBDM ::: submit failed (reply detail invalid)");
                }
            } catch (Exception ex){
                messageHeader = msg.get("app.messageHeader.exception");
                message = Util.getMessageException(ex);
                showMessageBox();
                log.error("submitForBDM ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : ZM not selected";
            showMessageBox();
            log.error("submitForBDM ::: submit failed (ZM not selected)");
        }
        sendCallBackParam(complete);
    }

    private void submitForBDMReducePrice(){
        log.debug("Submit Case from BDM to ZM Pricing Starting..., stepId : {}", stepId);
        boolean complete = false;
        if(zmUserId != null && !zmUserId.equals("")){
            try{
                if(canSubmitWithoutReplyDetail(workCaseId, workCasePreScreenId)){
                    fullApplicationControl.submitForBDMReducePrice(queueName, wobNumber, zmUserId, rgmUserId, ghmUserId, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId);
                    log.debug("submitForBDMReducePrice ::: success.");
                    log.debug("submitForBDMReducePrice ::: Backup return info to History Start...");
                    returnControl.updateReplyDate(workCaseId, workCasePreScreenId);
                    log.debug("submitForBDMReducePrice ::: Backup return info to History Success...");
                    messageHeader = msg.get("app.messageHeader.info");
                    message = msg.get("app.message.dialog.submit.success");
                    showMessageRedirect();
                    complete = true;
                } else {
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = "Please update return reply detail before submit.";
                    showMessageBox();
                    log.error("submitForBDMReducePrice ::: submit failed (reply detail invalid)");
                }
            } catch (Exception ex){
                messageHeader = msg.get("app.messageHeader.exception");
                message = Util.getMessageException(ex);
                showMessageBox();
                log.error("submitForBDMReducePrice ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : ZM not selected";
            showMessageBox();
            log.error("submitForBDMReducePrice ::: submit failed (ZM not selected)");
        }
        sendCallBackParam(complete);
    }

    private void submitForZM(){
        log.debug("Submit case from ZM to Next Step ::: Starting..., stepId : {}, statusId : {}", stepId, statusId);
        boolean complete = false;
        try{
            fullApplicationControl.submitForZM(queueName, wobNumber, rgmUserId, ghmUserId, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId, stepId);
            returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitRM ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitRM ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForZMReducePrice(){
        log.debug("Submit case from ZM to Next Step ::: Starting..., stepId : {}, statusId : {}", stepId, statusId);
        boolean complete = false;
        try{
            fullApplicationControl.submitForZMReducePrice(queueName, wobNumber, rgmUserId, ghmUserId, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId, stepId);
            returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("submitForZMReducePrice ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForZMReducePrice ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForZMFCash(){
        log.debug("submitForZMFCash ::: starting...");
        boolean complete = false;
        try{
            fullApplicationControl.submitForZMFCash(queueName, wobNumber, submitRemark, slaRemark, slaReasonId, workCaseId);
            fullApplicationControl.calculateApprovedResult(workCaseId, StepValue.CREDIT_DECISION_BU_ZM);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitFCashZM ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitFCashZM ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForRGM(){
        log.debug("Submit case from RGM to Next Step ::: Starting..., stepId : {}, statusId : {}", stepId, statusId);
        boolean complete = false;
        try{
            fullApplicationControl.submitForRGM(queueName, wobNumber, ghmUserId, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("submitForRGM ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForRGM ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForGH(){
        log.debug("Submit case from GH to Next Step ::: Starting..., stepId : {}, statusId : {}", stepId, statusId);
        boolean complete = false;
        try{
            fullApplicationControl.submitForGH(queueName, wobNumber, cssoUserId, submitRemark, slaRemark, slaReasonId, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("submitForGH ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : " + Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForGH ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForCSSO(){
        log.debug("Submit case from CSSO to Next Step ::: Starting..., stepId : {}, statusId : {}", stepId, statusId);
        boolean complete = false;
        try{
            fullApplicationControl.submitForCSSO(queueName, wobNumber, submitRemark, slaRemark, slaReasonId, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("submitForCSSO ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : " + Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForCSSO ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void getUserOwnerBU(){
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if(pricingDOALevel == 1) {
            isSubmitToZM = true;
            //Get User ZM from WorkCase Owner
            User zmUser = basicInfo.getZmUser();
            if(!Util.isNull(zmUser)) {
                zmUserId = zmUser.getId();
                onSelectedZM();
            }
        }else if(pricingDOALevel == 2){
            isSubmitToZM = true;
            isSubmitToRGM = true;
            //Get User ZM from WorkCase Owner
            User zmUser = basicInfo.getZmUser();
            if(!Util.isNull(zmUser)) {
                zmUserId = zmUser.getId();
                onSelectedZM();
            }
            //Get User RGM from WorkCase Owner
            User rgmUser = basicInfo.getRgmUser();
            if(!Util.isNull(rgmUser)) {
                rgmUserId = rgmUser.getId();
            }
        }else if(pricingDOALevel == 3){
            isSubmitToZM = true;
            isSubmitToRGM = true;
            isSubmitToGHM = true;
            //Get User ZM from WorkCase Owner
            User zmUser = basicInfo.getZmUser();
            if(!Util.isNull(zmUser)) {
                zmUserId = zmUser.getId();
                onSelectedZM();
            }
            //Get User RGM from WorkCase Owner
            User rgmUser = basicInfo.getRgmUser();
            if(!Util.isNull(rgmUser)) {
                rgmUserId = rgmUser.getId();
                onSelectedRM();
            }
            //Get User GH from WorkCase Owner
            User ghmUser = basicInfo.getGhUser();
            if(!Util.isNull(ghmUser)) {
                ghmUserId = ghmUser.getId();
            }
        }else if(pricingDOALevel == 4){
            isSubmitToZM = true;
            isSubmitToRGM = true;
            isSubmitToGHM = true;
            isSubmitToCSSO = true;
            //Get User ZM from WorkCase Owner
            User zmUser = basicInfo.getZmUser();
            if(!Util.isNull(zmUser)) {
                zmUserId = zmUser.getId();
                onSelectedZM();
            }
            //Get User RGM from WorkCase Owner
            User rgmUser = basicInfo.getRgmUser();
            if(!Util.isNull(rgmUser)) {
                rgmUserId = rgmUser.getId();
                onSelectedRM();
            }
            //Get User GH from WorkCase Owner
            User ghmUser = basicInfo.getGhUser();
            if(!Util.isNull(ghmUser)) {
                ghmUserId = ghmUser.getId();
                onSelectedGH();
            }
            //Get User CSSO from WorkCase Owner
            User cssoUser = basicInfo.getCssoUser();
            if(!Util.isNull(cssoUser)) {
                cssoUserId = cssoUser.getId();
            }
        }
    }

    public void onSelectedZM(){
        log.debug("onSelectedZM : pricingDOALevel : {}", pricingDOALevel);
        if(pricingDOALevel >= PricingDOAValue.ZM_DOA.value()){
            rgmUserId = "";
            log.debug("onSelectedZM : zmUserId : {}", zmUserId);
            if(!Util.isEmpty(zmUserId)) {
                User userZm = userDAO.findById(zmUserId);
                rgmUserList = fullApplicationControl.getUserList(userZm);
            }else{
                rgmUserList = new ArrayList<User>();
                ghmUserList = new ArrayList<User>();
                cssoUserList = new ArrayList<User>();
            }
        }
    }

    public void onSelectedRM(){
        log.debug("onSelectedRM : pricingDOALevel : {}", pricingDOALevel);
        if(pricingDOALevel >= PricingDOAValue.GH_DOA.value()){
            ghmUserId = "";
            log.debug("onSelectedRM : rgmUserId : {}", rgmUserId);
            if(!Util.isEmpty(rgmUserId)) {
                User userRm = userDAO.findById(rgmUserId);
                ghmUserList = fullApplicationControl.getUserList(userRm);
            }else{
                ghmUserList = new ArrayList<User>();
                cssoUserList = new ArrayList<User>();
            }
        }
    }

    public void onSelectedGH(){
        log.debug("onSelectedGH : pricingDOALevel : {}", pricingDOALevel);
        if(pricingDOALevel >= PricingDOAValue.CSSO_DOA.value()){
            cssoUserId = "";
            User userGh = userDAO.findById(ghmUserId);
            cssoUserList = fullApplicationControl.getUserList(userGh);
        }
    }

    //------------- Submit CA [ Generic for UW ] --------------------//
    private void checkSubmitRole(){
        isSubmitForUW = false;
        isSubmitForUW2 = false;
        isSubmitForBDM = false;
        if(stepId == StepValue.CREDIT_DECISION_UW1.value()){
            isSubmitForUW = true;
        }else if(stepId == StepValue.CREDIT_DECISION_UW2.value()) {
            isSubmitForUW2 = true;
        }else if(stepId == StepValue.CREDIT_DECISION_UW1_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM.value() ||
                stepId == StepValue.CREDIT_DECISION_UW1_CORRECT_INFO_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM_UPD_INFO.value()){
            isSubmitForBDM = true;
        }else{
            log.debug("No step match : stepId : {}", stepId);
        }

        /*else if(stepId == StepValue.CREDIT_DECISION_UW1_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM.value() || stepId == StepValue.CREDIT_DECISION_UW2_BDM_UPD_INFO.value()){
            isSubmitForBDM = true;
        }*/
    }

    public void onSelectedUWDOALevel(){
        log.debug("selected UW2 DOALevel id : ()",selectedDOALevel);
        try{
            if(selectedDOALevel!=0){
                uw2UserList = fullApplicationControl.getUWUserListFromDOALevel(selectedDOALevel);
            } else {
                uw2UserList = new ArrayList<User>();
            }
        } catch (Exception ex) {
            uw2UserList = new ArrayList<User>();
            log.error("onSelectedUWDOALevel Exception : ",ex);
        }
    }

    public void onOpenSubmitUW(){
        _loadSessionVariable();
        try {
            boolean checkForCheckCriteria = false;
            if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)){
                if(checkButton("Check Criteria")){
                    if(fullApplicationControl.checkCaseUpdate(workCaseId)){
                        checkForCheckCriteria = true;
                    }
                }

                if (!checkForCheckCriteria) {
                    submitRemark = "";
                    slaReasonId = 0;
                    submitOverSLA = slaStatus.equalsIgnoreCase("R") ? 1 : 0;
                    if (submitOverSLA == 1) {
                        slaReasonList = reasonToStepDAO.getOverSLAReason(stepId);
                    }

                    checkSubmitRole();
                    log.debug("isSubmitForUW : {}, isSubmitForUW2 : {}, isSubmitForBDM : {}", isSubmitForUW, isSubmitForUW2, isSubmitForBDM);

                    if(isSubmitForUW){
                        //Get drop down DOA Level
                        selectedUW2User = "";
                        selectedDOALevel = 0;

                        isUWRejected = fullApplicationControl.checkUWDecision(workCaseId);
                        authorizationDOAList = new ArrayList<AuthorizationDOA>();
                        if(!isUWRejected){
                            authorizationDOAList = fullApplicationControl.getAuthorizationDOALevelList(workCaseId);
                        }
                        log.debug("authorizationDOAList : {}", authorizationDOAList.size());
                    }
                    RequestContext.getCurrentInstance().execute("submitUWDlg.show()");
                }else{
                    //----Case is updated please check criteria before submit----
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = "CA information is updated, please Check Criteria before submit.";
                    showMessageBox();
                }
            } else {
                messageHeader = "Warning.";
                message = "Cannot " + ActionCode.SUBMIT_CA.getActionName() + ". Some Mandate Document are incompleted.";
                showMessageBox();
            }
        }catch(Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            log.error("Exception while open submit uw : ", ex);
            showMessageBox();
        }
    }

    public void onSubmitUW(){
        //TODO Split 2 function to Submit ( UW1 to UW2, UW2 submit for End Case )
        if(isSubmitForUW){
            //Submit case for UW to UW2
            submitForUW();
        }else if(isSubmitForUW2){
            //Submit case for UW2 to End Case
            submitForUW2();
        }else if(isSubmitForBDM){
            //Submit case for BDM ( from return by UW/UW2 )
            submitForBDMUW();
        }
    }

    private void submitForUW(){
        log.debug("submitForUW :: Start..");
        boolean complete = false;
        boolean checkUW = true;
        try{
            if(!isUWRejected){
                if(selectedUW2User != null && !selectedUW2User.equals("")){
                    checkUW = true;
                }else{
                    checkUW = false;
                }
            }

            if(checkUW){
                if(canSubmitWithoutReturn()){
                    fullApplicationControl.calculateApprovedResult(workCaseId, StepValue.CREDIT_DECISION_UW1);
                    fullApplicationControl.submitForUW(queueName, wobNumber, submitRemark, slaRemark, slaReasonId, selectedUW2User, selectedDOALevel, workCaseId);
                    messageHeader = msg.get("app.messageHeader.info");
                    message = msg.get("app.message.dialog.submit.success");
                    complete = true;
                    showMessageRedirect();
                    log.debug("submitForUW ::: success.");
                } else {
                    messageHeader = "Information.";
                    message = "Submit case fail. Please check return information before submit again.";
                    showMessageBox();
                    log.debug("submitForUW ::: fail.");
                }
            } else {
                messageHeader = msg.get("app.messageHeader.exception");
                message = "Submit case failed, cause : UW2 was not selected";
                showMessageBox();
                log.debug("submitForUW ::: submit failed (UW2 not selected)");
            }
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForUW ::: exception occurred : ", ex);
        }

        sendCallBackParam(complete);
    }

    private void submitForUW2(){
        log.debug("submitForUW2 :: Start");
        boolean complete = false;
        try{
            if(canSubmitWithoutReturn()){
                fullApplicationControl.calculateApprovedResult(workCaseId, StepValue.CREDIT_DECISION_UW2);
                fullApplicationControl.submitForUW2(queueName, wobNumber, submitRemark, slaRemark, slaReasonId, workCaseId);

                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.message.dialog.submit.success");
                complete = true;

                showMessageRedirect();

                log.debug("submitForUW2 ::: success.");
            } else {
                messageHeader = "Information.";
                message = "Submit case fail. Please check return information before submit again.";
                showMessageBox();
                log.error("submitForUW2 ::: fail.");
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit case fail, cause : " + Util.getMessageException(ex);
            showMessageBox();
            log.error("submitForUW2 ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    private void submitForBDMUW(){
        log.debug("submitForBDMUW :: Start");
        boolean complete = false;
        try{
            if(canSubmitWithoutReply(workCaseId,workCasePreScreenId)){
                fullApplicationControl.submitForBDMUW(queueName, wobNumber, submitRemark, slaRemark, slaReasonId);
                returnControl.updateReplyDate(workCaseId,workCasePreScreenId);

                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.message.dialog.submit.success");
                complete = true;
                showMessageRedirect();
            } else {
                messageHeader = "Information.";
                message = "Submit case fail. Please check return information before submit again.";
                showMessageBox();
                log.error("submitForBDMUW ::: fail.");
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit case fail, cause : " + Util.getMessageException(ex);
            showMessageBox();

            log.error("submitForBDMUW ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }
    //************** END OF FUNCTION FOR FULL APPLICATION STAGE ************//

    //************* FUNCTION FOR CANCEL CA ****************//
    public void onOpenCancelCA(){
        log.debug("onOpenCancelCA ::: starting...");
        _loadSessionVariable();
        cancelRemark = "";
        cancelReasonId = 0;
        //Check BRMS Result
        UWRuleResultSummary uwRuleResultSummary = null;

        if(workCasePreScreenId != 0)
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePreScreenId);
        else if(workCaseId != 0)
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkCaseId(workCaseId);

        if(stepId == StepValue.CUSTOMER_ACCEPTANCE_PRE.value()){
            reasonList = reasonToStepDAO.getCancelReason(stepId, ActionCode.CANCEL_CA.getVal());
            cancelReasonId = 0;
        } else {
            if(uwRuleResultSummary != null && uwRuleResultSummary.getUwResultColor() == UWResultColor.RED){
                reasonList = reasonToStepDAO.getRejectReason(stepId);
                cancelReasonId = reasonDAO.getBRMSReasonId();
            } else {
                reasonList = reasonToStepDAO.getCancelReason(stepId, ActionCode.CANCEL_CA.getVal());
                cancelReasonId = 0;
            }
        }

        log.debug("onOpenCancelCA ::: reasonList.size() : {}", reasonList.size());
    }

    public void onCancelCA(){
        log.debug("onCancelCA ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.cancelCA(queueName, wobNumber, reasonId, cancelRemark);
            log.debug("saveCancelRejectInfo... cancelReasonId : {}, remark : {}, workCaseId : {}, workCasePreScreenId : {}", cancelReasonId, cancelRemark, workCaseId, workCasePreScreenId);
            fullApplicationControl.saveCancelRejectInfo(workCaseId, workCasePreScreenId, cancelReasonId);
            fullApplicationControl.checkNCBReject(workCasePreScreenId, workCaseId);
            messageHeader =  msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.cancel.success");
            showMessageRedirect();
            complete = true;
            log.debug("onCancelCA ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onCancelCA ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    //************* END FUNCTION FOR CANCEL CA *************//

    //************** FUNCTION FOR APPRAISAL STAGE *************//

    //---------------- Function for Request Appraisal ( Parallel ) -----------------//
    //*** For BDM Request from PreScreen/FullApplication
    //*   Flag for Parallel flag to REQUESTING_PARALLEL
    //*   Step 2001/1003
    public void onRequestParallelAppraisal(){
        _loadSessionVariable();
        try {
            log.debug("onRequestParallelAppraisal : workCaseId : {}, workCasePreScreenId : {}", workCaseId, workCasePreScreenId);
            fullApplicationControl.requestParallelAppraisal(workCaseId, workCasePreScreenId);
            //Redirect to Appraisal Request Page
            FacesUtil.redirect("/site/appraisalRequest.jsf");
            return;
        }catch (Exception ex){
            log.error("Exception while request parallel appraisal : ", ex);
            messageHeader = "Exception.";
            message = "Exception while request parallel appraisal, " + Util.getMessageException(ex);
            showMessageBox();
        }
    }

    //*** For BDM Request from RequestAppraisal Screen
    //*   Show Dialog for Submit Request Parallel
    //*   Step 2001/1003
    public void onOpenSubmitParallelRequestAppraisal(){
        _loadSessionVariable();

        if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)) {
            slaReasonId = 0;
            submitRemark = "";
            slaRemark = "";
            RequestContext.getCurrentInstance().execute("requestParallelAppraisalDlg.show()");
        } else {
            messageHeader = "Warning.";
            message = "Cannot " + ActionCode.REQUEST_APPRAISAL.getActionName() + ". Some Mandate Document are incompleted.";
            showMessageBox();
        }
    }

    //*** For BDM Request from RequestAppraisal Screen
    //*   Submit case and Create parallel Case for Appraisal
    //*   Step 2001/1003
    public void onSubmitParallelRequestAppraisal(){
        try{
            if(fullApplicationControl.checkAppraisalInformation(workCasePreScreenId, workCaseId)) {
                fullApplicationControl.requestAppraisalParallelBDM(workCasePreScreenId, workCaseId, stepId);
                fullApplicationControl.duplicateCollateralForAppraisal(workCaseId, workCasePreScreenId);
                messageHeader = "Information.";
                message = "Request for Appraisal complete.";
                showMessageRedirect();
            }else{
                messageHeader = "Exception.";
                message = "Please save Appraisal Request Screen before submit.";
            }
        }catch(Exception ex){
            log.error("Exception while submit parallel request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }

    }

    //*** For BDM Cancel Request from RequestAppraisal Screen
    //*   Step 2001/1003
    public void onCancelParallelRequestAppraisal(){
        _loadSessionVariable();
        log.debug("onCancelParallelRequestAppraisal : workCaseId : {}, workCasePreScreenId : {}, stepId : {}", workCaseId, workCasePreScreenId, stepId);
        fullApplicationControl.cancelParallelRequestAppraisal(workCasePreScreenId, workCaseId, stepId);
        messageHeader = "Information.";
        message = "Cancel request appraisal complete.";
        showMessageRedirect();
    }

    //*** For BDM Cancel Request from RequestAppraisal Screen
    //*   Clear Parallel Appraisal Flag, Delete Collateral ProposeType A ( Requested )
    //*   Step 2007
    public void onOpenCancelAppraisalRequest(){
        log.debug("onOpenCancelAppraisalRequest ::: starting...");
        _loadSessionVariable();
        cancelRemark = "";
        reasonId = 0;
        reasonList = reasonToStepDAO.getCancelReason(stepId, ActionCode.CANCEL_APPRAISAL.getVal());
        log.debug("onOpenCancelAppraisalRequest ::: reasonList.size() : {}", reasonList.size());
    }

    //*** For BDM Cancel Request from RequestAppraisal Screen
    //*   Clear Parallel Appraisal Flag, Delete Collateral ProposeType A ( Requested )
    //*   Step 2007
    public void onCancelAppraisalRequest(){
        log.debug("onCancelAppraisalRequest ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.cancelRequestAppraisal(queueName, wobNumber, cancelRequestReasonId, cancelRequestRemark, workCasePreScreenId, workCaseId, stepId);
            fullApplicationControl.deleteUnUseCollateral(workCaseId, workCasePreScreenId);
            messageHeader =  msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.cancel.success");
            showMessageRedirect();
            complete = true;
            log.debug("onCancelAppraisalRequest ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onCancelAppraisalRequest ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    //*** For BDM Request Appraisal after Customer Acceptance
    //*   Step 2032
    public void onOpenRequestAppraisalCustomerAccepted(){
        log.debug("onOpenRequestAppraisalCustomerAccepted ( after customer acceptance )");
        _loadSessionVariable();

        if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)){
            //Check Appraisal data exist.
            if(fullApplicationControl.checkAppraisalInformation(workCasePreScreenId, workCaseId)) {
                RequestContext.getCurrentInstance().execute("reqAppr_BDMDlg.show()");
            } else {
                log.debug("onOpenRequestAppraisalCustomerAccepted : check appraisal information failed. do not open dialog.");
                messageHeader = "Information.";
                message = "Please complete request appraisal form.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        } else {
            messageHeader = "Warning.";
            message = "Cannot " + ActionCode.REQUEST_APPRAISAL.getActionName() + ". Some Mandate Document are incompleted.";
            showMessageBox();
        }

    }

    //*** For BDM Request Appraisal after Customer Acceptance
    //*   Step 2032
    public void onSubmitRequestAppraisalCustomerAccepted(){
        log.debug("onRequestAppraisal by BDM ( after customer acceptance )");
        _loadSessionVariable();

        try {
            fullApplicationControl.requestAppraisal(queueName, wobNumber, workCaseId, submitRemark, slaReasonId, slaRemark);
            messageHeader = "Information.";
            message = "Submit case success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    //*** For AAD Admin Submit to AAD Committee
    //*   Step 2006
    public void onOpenSubmitForAADAdmin(){
        log.debug("onOpenSubmitForAADAdmin ( submit to AAD committee )");
        _loadSessionVariable();
        if(checkMandateDocControl.isMandateDocCompleted(workCaseId, workCasePreScreenId, stepId)) {
            if(fullApplicationControl.checkAppointmentInformation(workCaseId, workCasePreScreenId)){
                slaRemark = "";
                submitRemark = "";
                aadCommitteeId = "";
                submitOverSLA = slaStatus.equalsIgnoreCase("R") ? 1 : 0;
                if (submitOverSLA == 1) {
                    slaReasonList = reasonToStepDAO.getOverSLAReason(stepId);
                }
                //List AAD Admin by team structure
                aadCommiteeList = fullApplicationControl.getUserListByRole(RoleValue.AAD_COMITTEE);
                RequestContext.getCurrentInstance().execute("submitAADCDlg.show()");
            } else {
                messageHeader = "Exception.";
                message = "Please input Appointment Date first.";
                showMessageBox();
            }
        } else {
            messageHeader = "Warning.";
            message = "Cannot " + ActionCode.ASSIGN_TO_CHECKER.getActionName() + ". Some Mandate Document are incompleted.";
            showMessageBox();
        }
    }

    //*** For AAD Admin Submit to AAD Committee
    //*   Step 2006
    public void onSubmitForAADAdmin(){
        log.debug("onSubmitForAADAdmin ( submit to AAD committee )");
        _loadSessionVariable();
        boolean canSubmit = false;
        try{
            if(stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value() && statusId == StatusValue.REPLY_FROM_BDM.value()){
                log.debug("onSubmitForAADAdmin ( save Return History For Restart )");
                returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);
                canSubmit = true;
            } else {
                if(canSubmitWithoutReplyDetail(workCaseId,workCasePreScreenId)){
                    returnControl.updateReplyDate(workCaseId,workCasePreScreenId);
                    canSubmit = true;
                } else {
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = "Please update return reply detail before submit.";
                    showMessageBox();
                    log.error("onSubmitForAADAdmin ::: submit failed (reply detail invalid)");
                }

            }

            if(canSubmit){
                fullApplicationControl.submitForAADAdmin(queueName, wobNumber, aadCommitteeId, submitRemark, slaReasonId, slaRemark, workCaseId, workCasePreScreenId);
                messageHeader = "Information.";
                message = "Request for appraisal success.";
                showMessageRedirect();
            }
        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    //*** For AAD Committee Submit to UW
    public void onOpenSubmitForAADCommittee(){
        _loadSessionVariable();
        log.debug("onOpenSubmitForAADCommittee");

    }

    //*** For AAD Committee Submit to UW
    public void onSubmitForAADCommittee(){
        _loadSessionVariable();
        log.debug("onSubmitForAADCommittee ( appraisal to uw )");
        try{
            returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);
            fullApplicationControl.submitForAADCommittee(queueName, wobNumber, workCaseId, workCasePreScreenId);
            //Delete Request Data
            fullApplicationControl.deleteUnUseCollateral(workCaseId, workCasePreScreenId);
            messageHeader = "Information.";
            message = "Submit case success.";
            showMessageRedirect();
        } catch (Exception ex){
            log.error("exception while submit case to uw2 : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    //************** END FUNCTION FOR APPRAISAL STAGE **************//

    public boolean canSubmitWithoutReturn() throws Exception{
        List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId,workCasePreScreenId);

        if(returnInfoViews!=null && returnInfoViews.size()>0){
            return  false;
        } else {
            //check if have return not accept
            List<ReturnInfoView> returnInfoViewsNoAccept = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId, stepId);
            if(returnInfoViewsNoAccept!=null && returnInfoViewsNoAccept.size()>0){
                return false;
            } else {
                returnControl.saveReturnHistory(workCaseId,workCasePreScreenId);
                return true;
            }
        }
    }

    /*public void onSubmitFCashZM(){
        log.debug("onSubmitFCashZM ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitFCashZM(queueName, wobNumber, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitFCashZM ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitFCashZM ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }*/

    public void onReturnBDMByBU(){
        log.debug("onReturnBDMByZM ( return to BDM from ZM )");
        HttpSession session = FacesUtil.getSession(false);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try{
            fullApplicationControl.returnBDMByBU(queueName, wobNumber, returnRemark, reasonId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.return.success");
            showMessageRedirect();
        }catch (Exception ex){
            log.error("Exception while return to BDM : ", ex);
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    public void onReturnAADMByUW2(){
        log.debug("onReturnAADAdmin ( return to AAD Admin from UW2 )");
        HttpSession session = FacesUtil.getSession(false);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try {
            fullApplicationControl.returnAADAdminByUW2(queueName, wobNumber, returnAADRemark, returnReasonId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.return.success");
            showMessageRedirect();
        } catch (Exception ex) {
            log.error("Exception while return to aad admin : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    public void onSubmitCustomerAcceptance(){
        log.debug("onSubmitCustomerAcceptance ( BDM submit to UW )");
        long workCaseId = 0;
        String wobNumber = "";
        String queueName = "";

        HttpSession session = FacesUtil.getSession(false);
        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        if(workCaseId != 0){
            try{
                fullApplicationControl.submitCustomerAcceptance(queueName, wobNumber);
                messageHeader = "Information.";
                message = "Submit case success.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } catch (Exception e){
                log.error("Exception while submit customer acceptance. cause : ", e);
                messageHeader = "Exception.";
                message = Util.getMessageException(e);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        } else {
            log.error("Could not find session for workcase id");
            messageHeader = "Exception.";
            message = "Could not find session to submit case";
        }
    }

    public void onOpenPendingDecision(){
        _loadSessionVariable();
        pendingReasonId = 0;
        pendingRemark = "";
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.PENDING_REASON);
        RequestContext.getCurrentInstance().execute("pendingDecisionDlg.show()");
    }

    public void onSubmitPendingDecision(){
        try{
            fullApplicationControl.submitPendingDecision(queueName, wobNumber);
            messageHeader = "Information.";
            message = "Submit case success.";
            showMessageRedirect();
        } catch (Exception ex) {
            log.error("Exception while submit pending decision, ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    public void onRequestPriceReduction(){
        log.debug("onRequestPriceReduction ( in step Customer Acceptance )");
        _loadSessionVariable();
        /*requestPricing = fullApplicationControl.getRequestPricing(workCaseId);
        if(requestPricing) {*/
        try {
            fullApplicationControl.submitRequestPriceReduction(queueName, wobNumber);
            messageHeader = "Information.";
            message = "Request for Price Reduction success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex){
            log.error("Exception while submit request price reduction, ", ex);
            messageHeader = "Exception";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
        /*} else {
            messageHeader = "Information.";
            message = "Can not Request for Price Reduction, cause this case has no Pricing Request.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }*/
    }

    public void onOpenCancelRequestPriceReduction(){
        log.debug("onOpenCancelRequestPriceReduction");
        reasonList = reasonToStepDAO.getCancelReason(stepId, ActionCode.CANCEL_REQUEST_PRICE_REDUCTION.getVal());
        cancelReasonId = 0;
        cancelRemark = "";
        RequestContext.getCurrentInstance().execute("cancelRequestPriceReduceDlg.show()");
    }

    public void onCancelRequestPriceReduction(){
        log.debug("onCancelRequestPriceReduction");
        _loadSessionVariable();

        if(!Util.isNull(cancelReasonId) && !Util.isZero(cancelReasonId)){
            try{
                fullApplicationControl.cancelRequestPriceReduction(queueName, wobNumber, cancelReasonId, cancelRemark);
                messageHeader = "Information.";
                message = "Cancel Request Price Reduction success.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } catch (Exception ex){
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        }
    }

    public void onReturnBDMByAAD(){
        _loadSessionVariable();
        boolean complete = false;

        if(!Util.isNull(reasonId) && !Util.isZero(reasonId)){
            try{
                fullApplicationControl.returnBDMByAAD(queueName, wobNumber, returnRemark, reasonId);
                messageHeader = "Information.";
                message = "Return case success.";
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } catch (Exception ex) {
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onSubmitReturnAADAdmin(){
        String wobNumber = "";
        String queueName = "";
        boolean complete = false;

        HttpSession session = FacesUtil.getSession(false);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        if(!Util.isNull(reasonId) && !Util.isZero(reasonId)){
            try{
                fullApplicationControl.returnAADAdminByAADCommittee(queueName, wobNumber, returnRemark, reasonId);
                messageHeader = "Information.";
                message = "Return case success.";
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } catch (Exception ex) {
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    //*************** FUNCTION FOR RETURN INFO ********************//
    public void onOpenReturnInfoDialog(){
        log.debug("onOpenReturnInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId,stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.RETURN_TO_BDM.getVal());
        returnRemark = "";
        resetAddReturnInfo();

        log.debug("onOpenReturnInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void onOpenReturnAADInfoDialog(){
        log.debug("onOpenReturnInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId,stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnAADReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.RETURN_TO_AAD_ADMIN.getVal());
        returnAADRemark = "";
        resetAddReturnAADInfo();

        log.debug("onOpenReturnInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void onOpenReturnBDMInfoDialog(){
        log.debug("onOpenReturnBDMInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId,stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnBDMReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.RETURN_TO_BDM.getVal());
        returnBDMRemark = "";
        resetAddReturnBDMInfo();

        log.debug("onOpenReturnBDMInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void onOpenReturnBUInfoDialog(){
        log.debug("onOpenReturnBUInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId, stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnBUReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.REVISE_CA.getVal());
        returnBURemark = "";
        resetAddReturnBUInfo();

        log.debug("onOpenReturnBDMInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void onOpenReturnMakerInfoDialog(){
        log.debug("onOpenReturnMakerInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId, stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnMakerReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.RETURN_TO_BDM.getVal());
        returnMakerRemark = "";
        resetAddReturnMakerInfo();

        log.debug("onOpenReturnMakerInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void onOpenReturnAADUWInfoDialog(){
        log.debug("onOpenReturnInfoDialog ::: starting...");
        _loadSessionVariable();

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId,workCasePreScreenId,stepId);

        //set return code master
        //returnReason = returnControl.getReturnReasonList();
        returnAADUWReason = reasonToStepDAO.getReturnReason(stepId, ActionCode.RETURN_TO_AAD_ADMIN.getVal());
        returnAADUWRemark = "";
        resetAddReturnAADInfo();

        log.debug("onOpenReturnInfoDialog ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void resetAddReturnInfo(){
        returnRemark = "";
        reasonId = 0;
        editRecordNo = -1;
    }

    public void resetAddReturnAADInfo(){
        returnAADRemark = "";
        reasonAADId = 0;
        editAADRecordNo = -1;
    }

    public void resetAddReturnBDMInfo(){
        returnBDMRemark = "";
        reasonBDMId = 0;
        editBDMRecordNo = -1;
    }

    public void resetAddReturnBUInfo(){
        returnBURemark = "";
        reasonBUId = 0;
        editBURecordNo = -1;
    }

    public void resetAddReturnMakerInfo(){
        returnMakerRemark = "";
        reasonMakerId = 0;
        editMakerRecordNo = -1;
    }

    public void resetAddReturnAADUWInfo(){
        returnAADUWRemark = "";
        reasonAADUWId = 0;
        editAADUWRecordNo = -1;
    }

    public void onOpenAddReturnInfo(){
        log.debug("onOpenAddReturnInfo ::: starting...");
        resetAddReturnInfo();
    }

    public void onOpenAddReturnAADInfo(){
        log.debug("onOpenAddReturnInfo ::: starting...");
        resetAddReturnAADInfo();
    }

    public void onOpenAddReturnBDMInfo(){
        log.debug("onOpenAddReturnBDMInfo ::: starting...");
        resetAddReturnBDMInfo();
    }

    public void onOpenAddReturnBUInfo(){
        log.debug("onOpenAddReturnBUInfo ::: starting...");
        resetAddReturnBUInfo();
    }

    public void onOpenAddReturnMakerInfo(){
        log.debug("onOpenAddReturnMakerInfo ::: starting...");
        resetAddReturnMakerInfo();
    }

    public void onOpenAddReturnAADUWInfo(){
        log.debug("onOpenAddReturnAADUWInfo ::: starting...");
        resetAddReturnAADUWInfo();
    }

    public void onSaveReturnInfo(){
        log.debug("onSaveReturnInfo ::: starting... (reasonId: {})",reasonId);
        Reason reason = reasonDAO.findById(reasonId);

        if(editRecordNo>-1){
            returnInfoViewList.get(editRecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editRecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editRecordNo).setReasonDetail(returnRemark);
            returnInfoViewList.get(editRecordNo).setCanEdit(true);
            returnInfoViewList.get(editRecordNo).setReasonId(reasonId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnRemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnInfo();

        log.debug("onSaveReturnInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onSaveReturnAADInfo(){
        log.debug("onSaveReturnAADInfo ::: starting... (reasonAADId: {})",reasonAADId);
        Reason reason = reasonDAO.findById(reasonAADId);

        if(editAADRecordNo>-1){
            returnInfoViewList.get(editAADRecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editAADRecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editAADRecordNo).setReasonDetail(returnAADRemark);
            returnInfoViewList.get(editAADRecordNo).setCanEdit(true);
            returnInfoViewList.get(editAADRecordNo).setReasonId(reasonAADId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnAADRemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonAADId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnAADInfo();

        log.debug("onSaveReturnInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onSaveReturnBDMInfo(){
        log.debug("onSaveReturnBDMInfo ::: starting... (reasonAADId: {})",reasonBDMId);
        Reason reason = reasonDAO.findById(reasonBDMId);

        if(editBDMRecordNo>-1){
            returnInfoViewList.get(editBDMRecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editBDMRecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editBDMRecordNo).setReasonDetail(returnBDMRemark);
            returnInfoViewList.get(editBDMRecordNo).setCanEdit(true);
            returnInfoViewList.get(editBDMRecordNo).setReasonId(reasonBDMId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnBDMRemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonBDMId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnBDMInfo();

        log.debug("onSaveReturnBDMInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onSaveReturnBUInfo(){
        log.debug("onSaveReturnBUInfo ::: starting... (reasonAADId: {})",reasonBUId);
        Reason reason = reasonDAO.findById(reasonBUId);

        if(editBURecordNo>-1){
            returnInfoViewList.get(editBURecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editBURecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editBURecordNo).setReasonDetail(returnBURemark);
            returnInfoViewList.get(editBURecordNo).setCanEdit(true);
            returnInfoViewList.get(editBURecordNo).setReasonId(reasonBUId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnBURemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonBUId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnInfo();

        log.debug("onSaveReturnBUInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onSaveReturnMakerInfo(){
        log.debug("onSaveReturnMakerInfo ::: starting... (reasonMakerId: {})",reasonMakerId);
        Reason reason = reasonDAO.findById(reasonMakerId);

        if(editMakerRecordNo>-1){
            returnInfoViewList.get(editMakerRecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editMakerRecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editMakerRecordNo).setReasonDetail(returnMakerRemark);
            returnInfoViewList.get(editMakerRecordNo).setCanEdit(true);
            returnInfoViewList.get(editMakerRecordNo).setReasonId(reasonMakerId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnMakerRemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonMakerId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnMakerInfo();

        log.debug("onSaveReturnMakerInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onSaveReturnAADUWInfo(){
        log.debug("onSaveReturnAADUWInfo ::: starting... (reasonAADUWId: {})",reasonAADUWId);
        Reason reason = reasonDAO.findById(reasonAADUWId);

        if(editAADUWRecordNo>-1){
            returnInfoViewList.get(editAADUWRecordNo).setReturnCode(reason.getCode());
            returnInfoViewList.get(editAADUWRecordNo).setDescription(reason.getDescription());
            returnInfoViewList.get(editAADUWRecordNo).setReasonDetail(returnAADUWRemark);
            returnInfoViewList.get(editAADUWRecordNo).setCanEdit(true);
            returnInfoViewList.get(editAADUWRecordNo).setReasonId(reasonAADUWId);
        } else {
            ReturnInfoView returnInfoView = new ReturnInfoView();
            returnInfoView.setReturnCode(reason.getCode());
            returnInfoView.setDescription(reason.getDescription());
            returnInfoView.setReasonDetail(returnAADUWRemark);
            returnInfoView.setCanEdit(true);
            returnInfoView.setReasonId(reasonAADUWId);

            returnInfoViewList.add(returnInfoView);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

        resetAddReturnAADUWInfo();

        log.debug("onSaveReturnInfo ::: complete. returnInfoViewList size: {}", returnInfoViewList.size());
    }

    public void onEditReturnInfo(int rowOnTable) {
        log.debug("onEditReturnInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonId = returnInfoView.getReasonId();
        returnRemark = returnInfoView.getReasonDetail();
        editRecordNo = rowOnTable;
        log.debug("onEditReturnInfo ::: end");
    }

    public void onEditReturnAADInfo(int rowOnTable) {
        log.debug("onEditReturnInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonAADId = returnInfoView.getReasonId();
        returnAADRemark = returnInfoView.getReasonDetail();
        editAADRecordNo = rowOnTable;
        log.debug("onEditReturnInfo ::: end");
    }

    public void onEditReturnBDMInfo(int rowOnTable) {
        log.debug("onEditReturnBDMInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonBDMId = returnInfoView.getReasonId();
        returnBDMRemark = returnInfoView.getReasonDetail();
        editBDMRecordNo = rowOnTable;
        log.debug("onEditReturnBDMInfo ::: end");
    }

    public void onEditReturnBUInfo(int rowOnTable) {
        log.debug("onEditReturnBUInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonBUId = returnInfoView.getReasonId();
        returnBURemark = returnInfoView.getReasonDetail();
        editBURecordNo = rowOnTable;
        log.debug("onEditReturnBUInfo ::: end");
    }

    public void onEditReturnMakerInfo(int rowOnTable) {
        log.debug("onEditReturnMakerInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonMakerId = returnInfoView.getReasonId();
        returnMakerRemark = returnInfoView.getReasonDetail();
        editMakerRecordNo = rowOnTable;
        log.debug("onEditReturnMakerInfo ::: end");
    }

    public void onEditReturnAADUWInfo(int rowOnTable) {
        log.debug("onEditReturnInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonAADUWId = returnInfoView.getReasonId();
        returnAADUWRemark = returnInfoView.getReasonDetail();
        editAADUWRecordNo = rowOnTable;
        log.debug("onEditReturnInfo ::: end");
    }

    public void onDeleteReturnInfo(int rowOnTable) {
        log.debug("onDeleteReturnInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnInfo();
        log.debug("onDeleteReturnInfo ::: end");
    }

    public void onDeleteReturnAADInfo(int rowOnTable) {
        log.debug("onDeleteReturnAADInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnAADInfo();
        log.debug("onDeleteReturnInfo ::: end");
    }

    public void onDeleteReturnBDMInfo(int rowOnTable) {
        log.debug("onDeleteReturnBDMInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnBDMInfo();
        log.debug("onDeleteReturnInfo ::: end");
    }

    public void onDeleteReturnBUInfo(int rowOnTable) {
        log.debug("onDeleteReturnBUInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnBUInfo();
        log.debug("onDeleteReturnBUInfo ::: end");
    }

    public void onDeleteReturnMakerInfo(int rowOnTable) {
        log.debug("onDeleteReturnMakerInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnMakerInfo();
        log.debug("onDeleteReturnMakerInfo ::: end");
    }

    public void onDeleteReturnAADUWInfo(int rowOnTable) {
        log.debug("onDeleteReturnAADInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnAADUWInfo();
        log.debug("onDeleteReturnInfo ::: end");
    }

    public void onSubmitReturnInfo(){ //Submit return to BDM
        log.debug("onSubmitReturnInfo ::: returnInfoViewList size : {}", returnInfoViewList);
        boolean complete = false;
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            try{
                HttpSession session = FacesUtil.getSession(false);
                //long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                User user = (User) session.getAttribute("user");
                long stepId = Long.parseLong(session.getAttribute("stepId").toString());

                List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId,workCasePreScreenId);

                if(returnInfoViews!=null && returnInfoViews.size()>0){
                    messageHeader = "Information.";
                    message = "Submit fail. Please check return information before submit return again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitReviewReturn ::: fail.");
                } else {
                    returnControl.submitReturnBDM(workCaseId, workCasePreScreenId, queueName, user, stepId, returnInfoViewList, wobNumber);
                    //TODO Remove Collateral List when AAD Admin return to BDM
                    messageHeader = "Information.";
                    message = msg.get("app.message.dialog.return.success");
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                    complete = true;
                    log.debug("onSubmitReturnInfo ::: success.");
                }
            } catch (Exception ex){
                messageHeader = "Information.";
                message = "Return case failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onSubmitReturnInfo ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = "Information.";
            message = "Return case failed, have no reason to return.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.debug("onSubmitReturnInfo ::: Return to BDM failed, have no reason to return.");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onSubmitReturnAADInfo(){ //Submit Return to AAD Admin ( from AAD Committee )
        log.debug("onSubmitReturnAADInfo ::: returnInfoViewList size : {}", returnInfoViewList);
        boolean complete = false;
        _loadSessionVariable();
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            try{
                List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId,workCasePreScreenId);

                if(returnInfoViews!=null && returnInfoViews.size()>0){
                    messageHeader = "Information.";
                    message = "Submit fail. Please check return information before submit return again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitReviewReturn ::: fail.");
                } else {
                    returnControl.submitReturnAADAdmin(workCaseId, workCasePreScreenId, queueName, user, stepId, returnInfoViewList, wobNumber);
                    messageHeader = "Information.";
                    message = msg.get("app.message.dialog.return.success");
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                    complete = true;
                    log.debug("onReturnBDMSubmit ::: success.");
                }
            } catch (Exception ex){
                messageHeader = "Information.";
                message = "Return case failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onReturnBDMSubmit ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = "Information.";
            message = "Return case failed, have no reason to return.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.debug("onSubmitReturnBDM ::: Return to BDM failed, have no reason to return.");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onSubmitReturnBUInfo(){ //Submit return BU
        log.debug("onSubmitReturnBUInfo ::: returnInfoViewList size : {}", returnInfoViewList);
        boolean complete = false;
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            try{
                HttpSession session = FacesUtil.getSession(false);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                User user = (User) session.getAttribute("user");
                long stepId = Long.parseLong(session.getAttribute("stepId").toString());

                List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId,workCasePreScreenId);

                if(returnInfoViews!=null && returnInfoViews.size()>0){
                    messageHeader = "Information.";
                    message = "Submit fail. Please check return information before submit return again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitReviewReturn ::: fail.");
                } else {
                    returnControl.submitReturnBU(workCaseId, workCasePreScreenId, queueName, user, stepId, returnInfoViewList, wobNumber);
                    messageHeader = "Information.";
                    message = msg.get("app.message.dialog.return.success");
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                    complete = true;
                    log.debug("onReturnBDMSubmit ::: success.");
                }
            } catch (Exception ex){
                messageHeader = "Information.";
                message = "Return case failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onReturnBDMSubmit ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = "Information.";
            message = "Return case failed, have no reason to return.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.debug("onSubmitReturnBDM ::: Return to BDM failed, have no reason to return.");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onSubmitReply(){ //Submit Reply From BDM to UW1
        log.debug("onSubmitReturnUW1");
        _loadSessionVariable();
        try{
            if(canSubmitWithoutReply(workCaseId,workCasePreScreenId)) {
                returnControl.submitReply(workCaseId,workCasePreScreenId,queueName);
                messageHeader = "Information.";
                message = "Submit Return success";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                log.debug("onReturnBDMSubmit ::: success.");
            } else {
                messageHeader = "Information.";
                message = "Submit Return fail. Please check return information again.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                log.error("onSubmitReturnUW1 ::: fail.");
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit Return fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onSubmitReturnUW1 ::: exception occurred : ", ex);
        }
    }

    public void onReturnToMaker(){
        log.debug("onReturnToMaker ::: starting...");
        boolean complete;
        try{
            if(returnReasonId != 0 && !Integer.toString(returnReasonId).equals("")){
                prescreenBusinessControl.returnBDM(workCasePreScreenId, queueName, ActionCode.RETURN_TO_BDM.getVal());
                messageHeader = "Information.";
                message = "Return to BDM Maker success. Click 'OK' return to inbox.";
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
                log.debug("onReturnToMaker ::: success...");
            } else {
                complete = false;
            }
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Return to BDM Maker failed. " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("onReturnToMaker ::: error... : ", ex);
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
        }
    }

    //****************** END FUNCTION FOR RETURN INFO *******************//

    public boolean canSubmitWithoutReply(long workCaseId, long workCasePreScreenId) throws Exception{
        List<ReturnInfoView> returnInfoViews;
        returnInfoViews = returnControl.getReturnNoReplyList(workCaseId,workCasePreScreenId);

        if(returnInfoViews!=null && returnInfoViews.size()>0){
            return false;
        } else {
            return true;
        }
    }

    public boolean canSubmitWithoutReplyDetail(long workCaseId, long workCasePreScreenId) throws Exception{
        List<ReturnInfoView> returnInfoViews;
        returnInfoViews = returnControl.getReturnNoReplyDetailList(workCaseId,workCasePreScreenId);

        if(returnInfoViews!=null && returnInfoViews.size()>0){
            return false;
        } else {
            return true;
        }
    }

    public void onRestartCase(){ //UW Restart case
        log.debug("onRestartCase");

        try{
            HttpSession session = FacesUtil.getSession(false);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

            fullApplicationControl.restartCase(queueName,wobNumber);
            returnControl.saveReturnHistoryForRestart(workCaseId,workCasePreScreenId);

            messageHeader = "Information.";
            message = "Restart Success";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

            log.debug("onRestartCase ::: success.");
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Restart fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onRestartCase ::: exception occurred : ", ex);
        }
    }

    //STEP AFTER RETURN FROM AAD ADMIN
    public void onReturnToAADAdminByBDM(){
        log.debug("onReturnToAADAdminByBDM");

        try {
            if(canSubmitWithoutReplyDetail(workCaseId,workCasePreScreenId)){
                _loadSessionVariable();

                messageHeader = "Information.";
                message = "Return to AAD Admin success.";

                fullApplicationControl.returnAADAdminByBDM(queueName, wobNumber);
                //TODO Duplicate Collateral to Propose Type A
                returnControl.updateReplyDate(workCaseId,workCasePreScreenId);

                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } else {
                messageHeader = msg.get("app.messageHeader.exception");
                message = "Please update return reply detail before submit.";
                log.error("submitForBDM ::: submit failed (reply detail invalid)");
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        } catch (Exception ex) {
            log.debug("Exception while Return to AAD Admin : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onCompleteCase(){ //UW Complete case
        log.debug("onCompleteCase");

        try{
            _loadSessionVariable();

            fullApplicationControl.completeCase(queueName,wobNumber);

            messageHeader = "Information.";
            message = "Complete Success";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

            log.debug("onCompleteCase ::: success.");
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Complete fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onCompleteCase ::: exception occurred : ", ex);
        }
    }

    public void onCheckTimeOfCheckCriteria(){
        _loadSessionVariable();
        log.debug("onCheckTimeOfCheckCriteria : workCaseId : {}", workCaseId);
        UserSysParameterView userSysParameterView = userSysParameterControl.getSysParameterValue("CHK_CRI_LIM");
        int limitTimeOfCriteriaCheck = 100;
        int currentTimeOfCheckCriteria = timesOfCriteriaCheck + 1;

        log.debug("userSysParameterView : {}", userSysParameterView);
        if(!Util.isNull(userSysParameterView))
            limitTimeOfCriteriaCheck = Util.parseInt(userSysParameterView.getValue(), 0);

        log.debug("limitTimeOfCriteriaCheck : {}", limitTimeOfCriteriaCheck);
        if(Util.compareInt(currentTimeOfCheckCriteria, limitTimeOfCriteriaCheck) >= 0){
            //To show message to confirm check criteria.
            RequestContext.getCurrentInstance().execute("confirmCheckCriteria.show()");
        }else{
            onCheckCriteria();
        }
    }

    public void onCheckCriteria(){
        boolean success = false;
        _loadSessionVariable();
        log.debug("onCheckCriteria : workCaseId : {}", workCaseId);
        if(workCaseId != 0){
            try{
                mandateFieldMessageViewList = null;
                fullApplicationControl.updateCSIDataFullApp(workCaseId);
                //----Delete all UWRuleResult----//
                UWRuleResponseView uwRuleResponseDeleteView = new UWRuleResponseView();
                UWRuleResultSummaryView uwRuleResultSummaryDeleteView = new UWRuleResultSummaryView();
                uwRuleResultSummaryDeleteView.setWorkCaseId(workCaseId);
                uwRuleResponseDeleteView.setUwRuleResultSummaryView(uwRuleResultSummaryDeleteView);
                uwRuleResultControl.deleteUWRuleResult(uwRuleResultSummaryDeleteView);

                //----Request BRMS System to get new UWRuleResult----//
                UWRuleResponseView uwRuleResponseView = brmsControl.getFullApplicationResult(workCaseId, ActionCode.CHECK_CRITERIA.getVal());
                log.info("onCheckCriteria uwRulesResponse : {}", uwRuleResponseView);
                if(uwRuleResponseView != null){
                    if(uwRuleResponseView.getActionResult().equals(ActionResult.SUCCESS)){
                        UWRuleResultSummaryView uwRuleResultSummaryView = null;
                        try{
                            uwRuleResultSummaryView = uwRuleResponseView.getUwRuleResultSummaryView();
                            uwRuleResultSummaryView.setWorkCaseId(workCaseId);
                            uwRuleResultControl.saveNewUWRuleResult(uwRuleResultSummaryView);

                            //----Update Times of Check Criteria----//
                            fullApplicationControl.updateTimeOfCheckCriteria(workCaseId, stepId);
                            fullApplicationControl.clearCaseUpdateFlag(workCaseId);

                            /*if(!headerControl.ncbResultValidation(uwRuleResultSummaryView,0,workCaseId,user)){
                                canCheckFullApp = false;
                            } else {
                                canCheckFullApp = true;
                            }
                            headerControl.updateNCBRejectFlagFullApp(workCaseId, canCheckFullApp);*/
                        }catch (Exception ex){
                            log.error("Cannot Save UWRuleResultSummary {}", uwRuleResultSummaryView);
                            messageHeader = "Exception.";
                            message = Util.getMessageException(ex);
                            showMessageBox();
                        }
                        messageHeader = "Information.";
                        message = "Request for Check Criteria Success.";
                        success = true;
                    }else {
                        messageHeader = "Failed.";
                        message = uwRuleResponseView.getReason();
                        mandateFieldMessageViewList = uwRuleResponseView.getMandateFieldMessageViewList();
                    }
                } else {
                    uwRuleResultControl.saveNewUWRuleResult(uwRuleResponseView.getUwRuleResultSummaryView());
                    messageHeader = "Exception.";
                    message = "Request for Check Criteria Fail.";
                }
            } catch (Exception ex){
                log.error("Exception while onCheckCriteria : ", ex);
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
            }
            log.debug("mandateFieldMessageViewList: {}", mandateFieldMessageViewList);
            if(mandateFieldMessageViewList == null || mandateFieldMessageViewList.size() == 0)
                if(success)
                    showMessageRefresh();
                else
                    showMessageBox();
            else
                showMessageMandate();

        }
        //RequestContext.getCurrentInstance().execute("blockUI.hide()");
    }

    public boolean checkAccessStage(String stageString){
        boolean accessible = false;
        _loadSessionVariable();

        if("PRESCREEN".equalsIgnoreCase(stageString) && workCasePreScreenId != 0){
            if(stageId == 101){
                accessible = true;
            }
        } else if ("FULLAPP".equalsIgnoreCase(stageString) && workCaseId != 0){
            if(stageId == 201 || stageId == 202 || stageId == 204 || stageId == 206 || stageId == 207 || stageId == 208){
                accessible = true;
            }
        } else if ("APPRAISAL".equalsIgnoreCase(stageString)){
            if(stageId == 203){
                accessible = true;
            }
        } else if ("CUSTOMERACCEPTANCE".equalsIgnoreCase(stageString)){
            if(stageId == 205){
                accessible = true;
            }
        } else if ("GENERIC".equalsIgnoreCase(stageString)){
            if(stageId == 0){
                accessible = true;
            }
        } else if ("ENDSTAGE".equalsIgnoreCase(stageString)){
            if(statusId == 0 || statusId == StatusValue.CANCEL_CA.value() || statusId == StatusValue.REJECT_CA.value() ||
                    statusId == StatusValue.REJECT_UW1.value() || statusId == StatusValue.REJECT_UW2.value()){
                accessible = true;
            }
        }

        return accessible;
    }

    public void onGoToInbox(){
        FacesUtil.redirect("/site/inbox.jsf");
    }

    public String defaultPage(String mainPage){
        if(mainPage.equals("borrowerInfo")){
            if(!canAccessMenu(Screen.CUSTOMER_INFO_SUMMARY.value()) && !canAccessMenu(Screen.QUALITATIVE.value()) && !canAccessMenu(Screen.NCB_SUMMARY.value()) && !canAccessMenu(Screen.TCG_INFO.value())){
                return "";
            }else if(canAccessMenu(Screen.CUSTOMER_INFO_SUMMARY.value())){
                return "customerInfoSummary";
            }else if(canAccessMenu(Screen.NCB_SUMMARY.value())){
                return "NCBSummary";
            }else if(canAccessMenu(Screen.QUALITATIVE.value())){
                return "qualitative";
            }else if(canAccessMenu(Screen.TCG_INFO.value())){
                return "TCGInfo";
            }
        }else if(mainPage.equals("financialInfo")){
            if(!canAccessMenu(Screen.BANK_STATEMENT_SUMMARY.value()) && !canAccessMenu(Screen.DBR_INFO.value()) && !canAccessMenu(Screen.CREDIT_FACILITY_EXISTING.value()) && !canAccessMenu(Screen.CREDIT_FACILITY_PROPOSE.value())){
                return "";
            }else if(canAccessMenu(Screen.BANK_STATEMENT_SUMMARY.value())){
                return "bankStatementSummary";
            }else if(canAccessMenu(Screen.DBR_INFO.value())){
                return "dbrInfo";
            }else if(canAccessMenu(Screen.CREDIT_FACILITY_EXISTING.value())){
                return "creditFacExisting";
            }else if(canAccessMenu(Screen.CREDIT_FACILITY_PROPOSE.value())){
                return "proposeLine";
            }
        }else if(mainPage.equals("decision")){
            if(!canAccessMenu(Screen.DECISION.value()) && !canAccessMenu(Screen.EXECUTIVE_SUMMARY.value())){
                return "";
            }else if(canAccessMenu(Screen.EXECUTIVE_SUMMARY.value())){
                return "exSummary";
            }else if(canAccessMenu(Screen.DECISION.value())){
                return "decision";
            }
        }

        return "";
    }

    //************** Variable Getter/Setter **************//
    public int getQualitativeType() {
        return qualitativeType;
    }

    public void setQualitativeType(int qualitativeType) {
        this.qualitativeType = qualitativeType;
    }

    public ManageButton getManageButton() {
        return manageButton;
    }

    public void setManageButton(ManageButton manageButton) {
        this.manageButton = manageButton;
    }

    /*public AppHeaderView getAppHeaderView() {
        return appHeaderView;
    }

    public void setAppHeaderView(AppHeaderView appHeaderView) {
        this.appHeaderView = appHeaderView;
    }*/

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public List<User> getAbdmUserList() {
        return abdmUserList;
    }

    public void setAbdmUserList(List<User> abdmUserList) {
        this.abdmUserList = abdmUserList;
    }

    /*public User getAbdm() {
        return abdm;
    }

    public void setAbdm(User abdm) {
        this.abdm = abdm;
    }*/

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAbdmUserId() {
        return abdmUserId;
    }

    public void setAbdmUserId(String abdmUserId) {
        this.abdmUserId = abdmUserId;
    }

    public String getAssignRemark() {
        return assignRemark;
    }

    public void setAssignRemark(String assignRemark) {
        this.assignRemark = assignRemark;
    }

    public String getZmUserId() {
        return zmUserId;
    }

    public void setZmUserId(String zmUserId) {
        this.zmUserId = zmUserId;
    }

    public String getRgmUserId() {
        return rgmUserId;
    }

    public void setRgmUserId(String rgmUserId) {
        this.rgmUserId = rgmUserId;
    }

    public String getGhmUserId() {
        return ghmUserId;
    }

    public void setGhmUserId(String ghmUserId) {
        this.ghmUserId = ghmUserId;
    }

    public String getZmEndorseUserId() {
        return zmEndorseUserId;
    }

    public void setZmEndorseUserId(String zmEndorseUserId) {
        this.zmEndorseUserId = zmEndorseUserId;
    }

    public String getZmEndorseRemark() {
        return zmEndorseRemark;
    }

    public void setZmEndorseRemark(String zmEndorseRemark) {
        this.zmEndorseRemark = zmEndorseRemark;
    }

    public List<User> getZmUserList() {
        return zmUserList;
    }

    public void setZmUserList(List<User> zmUserList) {
        this.zmUserList = zmUserList;
    }

    public int getRequestAppraisal() {
        return requestAppraisal;
    }

    public void setRequestAppraisal(int requestAppraisal) {
        this.requestAppraisal = requestAppraisal;
    }

    public List<User> getRgmUserList() {
        return rgmUserList;
    }

    public void setRgmUserList(List<User> rgmUserList) {
        this.rgmUserList = rgmUserList;
    }

    public List<User> getGhmUserList() {
        return ghmUserList;
    }

    public void setGhmUserList(List<User> ghmUserList) {
        this.ghmUserList = ghmUserList;
    }

    public String getAadCommitteeId() {
        return aadCommitteeId;
    }

    public void setAadCommitteeId(String aadCommitteeId) {
        this.aadCommitteeId = aadCommitteeId;
    }

    public int getPricingDOALevel() {
        return pricingDOALevel;
    }

    public void setPricingDOALevel(int pricingDOALevel) {
        this.pricingDOALevel = pricingDOALevel;
    }
    
    public boolean isStep3Screen() {
    	return (stepId/1000 == 3); // 3XXX 
    }

    public List<Reason> getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(List<Reason> cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelRemark() {
        return cancelRemark;
    }

    public void setCancelRemark(String cancelRemark) {
        this.cancelRemark = cancelRemark;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public int getReasonAADId() {
        return reasonAADId;
    }

    public void setReasonAADId(int reasonAADId) {
        this.reasonAADId = reasonAADId;
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark;
    }

    public List<Reason> getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(List<Reason> returnReason) {
        this.returnReason = returnReason;
    }

    public List<Reason> getReturnAADReason() {
        return returnAADReason;
    }

    public void setReturnAADReason(List<Reason> returnAADReason) {
        this.returnAADReason = returnAADReason;
    }

    public int getReasonBDMId() {
        return reasonBDMId;
    }

    public void setReasonBDMId(int reasonBDMId) {
        this.reasonBDMId = reasonBDMId;
    }

    public List<Reason> getReturnBDMReason() {
        return returnBDMReason;
    }

    public void setReturnBDMReason(List<Reason> returnBDMReason) {
        this.returnBDMReason = returnBDMReason;
    }

    public int getEditAADRecordNo() {
        return editAADRecordNo;
    }

    public void setEditAADRecordNo(int editAADRecordNo) {
        this.editAADRecordNo = editAADRecordNo;
    }

    public int getEditBDMRecordNo() {
        return editBDMRecordNo;
    }

    public void setEditBDMRecordNo(int editBDMRecordNo) {
        this.editBDMRecordNo = editBDMRecordNo;
    }

    public String getReturnBDMRemark() {
        return returnBDMRemark;
    }

    public void setReturnBDMRemark(String returnBDMRemark) {
        this.returnBDMRemark = returnBDMRemark;
    }

    public List<ReturnInfoView> getReturnInfoViewList() {
        return returnInfoViewList;
    }

    public void setReturnInfoViewList(List<ReturnInfoView> returnInfoViewList) {
        this.returnInfoViewList = returnInfoViewList;
    }

    public int getEditRecordNo() {
        return editRecordNo;
    }

    public void setEditRecordNo(int editRecordNo) {
        this.editRecordNo = editRecordNo;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public boolean isSubmitToGHM() {
        return isSubmitToGHM;
    }

    public void setSubmitToGHM(boolean submitToGHM) {
        isSubmitToGHM = submitToGHM;
    }

    public boolean isSubmitToRGM() {
        return isSubmitToRGM;
    }

    public void setSubmitToRGM(boolean submitToRGM) {
        isSubmitToRGM = submitToRGM;
    }

    public String getCssoUserId() {
        return cssoUserId;
    }

    public void setCssoUserId(String cssoUserId) {
        this.cssoUserId = cssoUserId;
    }

    public List<User> getCssoUserList() {
        return cssoUserList;
    }

    public void setCssoUserList(List<User> cssoUserList) {
        this.cssoUserList = cssoUserList;
    }

    public List<User> getAadCommiteeList() {
        return aadCommiteeList;
    }

    public void setAadCommiteeList(List<User> aadCommiteeList) {
        this.aadCommiteeList = aadCommiteeList;
    }

    public boolean isSubmitToCSSO() {
        return isSubmitToCSSO;
    }

    public void setSubmitToCSSO(boolean submitToCSSO) {
        isSubmitToCSSO = submitToCSSO;
    }

    public String getSlaRemark() {
        return slaRemark;
    }

    public void setSlaRemark(String slaRemark) {
        this.slaRemark = slaRemark;
    }

    public String getSubmitRemark() {
        return submitRemark;
    }

    public void setSubmitRemark(String submitRemark) {
        this.submitRemark = submitRemark;
    }

    public boolean isRequestPricing() {
        return requestPricing;
    }

    public void setRequestPricing(boolean requestPricing) {
        this.requestPricing = requestPricing;
    }

    public List<AuthorizationDOA> getAuthorizationDOAList() {
        return authorizationDOAList;
    }

    public void setAuthorizationDOAList(List<AuthorizationDOA> authorizationDOAList) {
        this.authorizationDOAList = authorizationDOAList;
    }

    public List<User> getUw2UserList() {
        return uw2UserList;
    }

    public void setUw2UserList(List<User> uw2UserList) {
        this.uw2UserList = uw2UserList;
    }

    public long getSelectedDOALevel() {
        return selectedDOALevel;
    }

    public void setSelectedDOALevel(long selectedDOALevel) {
        this.selectedDOALevel = selectedDOALevel;
    }

    public String getSelectedUW2User() {
        return selectedUW2User;
    }

    public void setSelectedUW2User(String selectedUW2User) {
        this.selectedUW2User = selectedUW2User;
    }

    public List<MandateFieldMessageView> getMandateFieldMessageViewList() {
        return mandateFieldMessageViewList;
    }

    public String getAadAdminName() {
        return aadAdminName;
    }

    public void setAadAdminName(String aadAdminName) {
        this.aadAdminName = aadAdminName;
    }

    public String getAadAdminId() {
        return aadAdminId;
    }

    public void setAadAdminId(String aadAdminId) {
        this.aadAdminId = aadAdminId;
    }

    public List<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public String getPendingRemark() {
        return pendingRemark;
    }

    public void setPendingRemark(String pendingRemark) {
        this.pendingRemark = pendingRemark;
    }

    public List<User> getAadAdminList() {
        return aadAdminList;
    }

    public void setAadAdminList(List<User> aadAdminList) {
        this.aadAdminList = aadAdminList;
    }

    public int getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(int returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public String getReturnAADRemark() {
        return returnAADRemark;
    }

    public void setReturnAADRemark(String returnAADRemark) {
        this.returnAADRemark = returnAADRemark;
    }

    public boolean isCanCloseSale() {
        return canCloseSale;
    }

    public void setCanCloseSale(boolean canCloseSale) {
        this.canCloseSale = canCloseSale;
    }

    public boolean isCanSubmitCA() {
        return canSubmitCA;
    }

    public void setCanSubmitCA(boolean canSubmitCA) {
        this.canSubmitCA = canSubmitCA;
    }

    public boolean isCanCheckCriteria() {
        return canCheckCriteria;
    }

    public void setCanCheckCriteria(boolean canCheckCriteria) {
        this.canCheckCriteria = canCheckCriteria;
    }

    public int getTimesOfCriteriaCheck() {
        return timesOfCriteriaCheck;
    }

    public void setTimesOfCriteriaCheck(int timesOfCriteriaCheck) {
        this.timesOfCriteriaCheck = timesOfCriteriaCheck;
    }

    public boolean isCanCheckPreScreen() {
        return canCheckPreScreen;
    }

    public void setCanCheckPreScreen(boolean canCheckPreScreen) {
        this.canCheckPreScreen = canCheckPreScreen;
    }

    public boolean isCanCheckFullApp() {
        return canCheckFullApp;
    }

    public void setCanCheckFullApp(boolean canCheckFullApp) {
        this.canCheckFullApp = canCheckFullApp;
    }

    public int getRequestAppraisalRequire() {
        return requestAppraisalRequire;
    }

    public void setRequestAppraisalRequire(int requestAppraisalRequire) {
        this.requestAppraisalRequire = requestAppraisalRequire;
    }

    public boolean isCanRequestAppraisal() {
        return canRequestAppraisal;
    }

    public void setCanRequestAppraisal(boolean canRequestAppraisal) {
        this.canRequestAppraisal = canRequestAppraisal;
    }

    public int getTimesOfPreScreenCheck() {
        return timesOfPreScreenCheck;
    }

    public void setTimesOfPreScreenCheck(int timesOfPreScreenCheck) {
        this.timesOfPreScreenCheck = timesOfPreScreenCheck;
    }

    public int getSubmitPricingLevel() {
        return submitPricingLevel;
    }

    public void setSubmitPricingLevel(int submitPricingLevel) {
        this.submitPricingLevel = submitPricingLevel;
    }

    public int getSubmitOverSLA() {
        return submitOverSLA;
    }

    public void setSubmitOverSLA(int submitOverSLA) {
        this.submitOverSLA = submitOverSLA;
    }

    public boolean isSubmitToZM() {
        return isSubmitToZM;
    }

    public void setSubmitToZM(boolean isSubmitToZM) {
        this.isSubmitToZM = isSubmitToZM;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Reason> getSlaReasonList() {
        return slaReasonList;
    }

    public void setSlaReasonList(List<Reason> slaReasonList) {
        this.slaReasonList = slaReasonList;
    }

    public List<User> getBdmCheckerList() {
        return bdmCheckerList;
    }

    public void setBdmCheckerList(List<User> bdmCheckerList) {
        this.bdmCheckerList = bdmCheckerList;
    }

    public String getBdmCheckerId() {
        return bdmCheckerId;
    }

    public void setBdmCheckerId(String bdmCheckerId) {
        this.bdmCheckerId = bdmCheckerId;
    }

    public List<Reason> getReturnBUReason() {
        return returnBUReason;
    }

    public void setReturnBUReason(List<Reason> returnBUReason) {
        this.returnBUReason = returnBUReason;
    }

    public int getReasonBUId() {
        return reasonBUId;
    }

    public void setReasonBUId(int reasonBUId) {
        this.reasonBUId = reasonBUId;
    }

    public int getEditBURecordNo() {
        return editBURecordNo;
    }

    public void setEditBURecordNo(int editBURecordNo) {
        this.editBURecordNo = editBURecordNo;
    }

    public String getReturnBURemark() {
        return returnBURemark;
    }

    public void setReturnBURemark(String returnBURemark) {
        this.returnBURemark = returnBURemark;
    }

    public List<ReturnInfoView> getReturnInfoAADViewList() {
        return returnInfoAADViewList;
    }

    public void setReturnInfoAADViewList(List<ReturnInfoView> returnInfoAADViewList) {
        this.returnInfoAADViewList = returnInfoAADViewList;
    }

    public List<ReturnInfoView> getReturnInfoBDMViewList() {
        return returnInfoBDMViewList;
    }

    public void setReturnInfoBDMViewList(List<ReturnInfoView> returnInfoBDMViewList) {
        this.returnInfoBDMViewList = returnInfoBDMViewList;
    }

    public List<ReturnInfoView> getReturnInfoBUViewList() {
        return returnInfoBUViewList;
    }

    public void setReturnInfoBUViewList(List<ReturnInfoView> returnInfoBUViewList) {
        this.returnInfoBUViewList = returnInfoBUViewList;
    }

    public int getReasonMakerId() {
        return reasonMakerId;
    }

    public void setReasonMakerId(int reasonMakerId) {
        this.reasonMakerId = reasonMakerId;
    }

    public List<Reason> getReturnMakerReason() {
        return returnMakerReason;
    }

    public void setReturnMakerReason(List<Reason> returnMakerReason) {
        this.returnMakerReason = returnMakerReason;
    }

    public int getEditMakerRecordNo() {
        return editMakerRecordNo;
    }

    public void setEditMakerRecordNo(int editMakerRecordNo) {
        this.editMakerRecordNo = editMakerRecordNo;
    }

    public String getReturnMakerRemark() {
        return returnMakerRemark;
    }

    public void setReturnMakerRemark(String returnMakerRemark) {
        this.returnMakerRemark = returnMakerRemark;
    }

    public int getSlaReasonId() {
        return slaReasonId;
    }

    public void setSlaReasonId(int slaReasonId) {
        this.slaReasonId = slaReasonId;
    }

    public int getCancelReasonId() {
        return cancelReasonId;
    }

    public void setCancelReasonId(int cancelReasonId) {
        this.cancelReasonId = cancelReasonId;
    }

    public int getCancelRequestReasonId() {
        return cancelRequestReasonId;
    }

    public void setCancelRequestReasonId(int cancelRequestReasonId) {
        this.cancelRequestReasonId = cancelRequestReasonId;
    }

    public String getCancelRequestRemark() {
        return cancelRequestRemark;
    }

    public void setCancelRequestRemark(String cancelRequestRemark) {
        this.cancelRequestRemark = cancelRequestRemark;
    }

    public int getCancelPriceReduceReasonId() {
        return cancelPriceReduceReasonId;
    }

    public void setCancelPriceReduceReasonId(int cancelPriceReduceReasonId) {
        this.cancelPriceReduceReasonId = cancelPriceReduceReasonId;
    }

    public String getCancelPriceReduceRemark() {
        return cancelPriceReduceRemark;
    }

    public void setCancelPriceReduceRemark(String cancelPriceReduceRemark) {
        this.cancelPriceReduceRemark = cancelPriceReduceRemark;
    }

    public boolean isSubmitForBDM() {
        return isSubmitForBDM;
    }

    public void setSubmitForBDM(boolean isSubmitForBDM) {
        this.isSubmitForBDM = isSubmitForBDM;
    }

    public boolean isSubmitForUW() {
        return isSubmitForUW;
    }

    public void setSubmitForUW(boolean isSubmitUW) {
        this.isSubmitForUW = isSubmitUW;
    }

    public boolean isSubmitForUW2() {
        return isSubmitForUW2;
    }

    public void setSubmitForUW2(boolean isSubmitUW2) {
        this.isSubmitForUW2 = isSubmitUW2;
    }

    public int getReasonAADUWId() {
        return reasonAADUWId;
    }

    public void setReasonAADUWId(int reasonAADUWId) {
        this.reasonAADUWId = reasonAADUWId;
    }

    public List<Reason> getReturnAADUWReason() {
        return returnAADUWReason;
    }

    public void setReturnAADUWReason(List<Reason> returnAADUWReason) {
        this.returnAADUWReason = returnAADUWReason;
    }

    public int getEditAADUWRecordNo() {
        return editAADUWRecordNo;
    }

    public void setEditAADUWRecordNo(int editAADUWRecordNo) {
        this.editAADUWRecordNo = editAADUWRecordNo;
    }

    public String getReturnAADUWRemark() {
        return returnAADUWRemark;
    }

    public void setReturnAADUWRemark(String returnAADUWRemark) {
        this.returnAADUWRemark = returnAADUWRemark;
    }

    public int getPendingReasonId() {
        return pendingReasonId;
    }

    public void setPendingReasonId(int pendingReasonId) {
        this.pendingReasonId = pendingReasonId;
    }

    public boolean isUWRejected() {
        return isUWRejected;
    }

    public void setUWRejected(boolean isUWRejected) {
        this.isUWRejected = isUWRejected;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }
}
