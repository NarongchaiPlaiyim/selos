package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageButton;
import com.clevel.selos.model.PricingDOAValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.view.*;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "baseController")
public class BaseController implements Serializable {
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
    FullApplicationControl fullApplicationControl;
    @Inject
    AppraisalRequestControl appraisalRequestControl;
    @Inject
    ReturnControl returnControl;
    @Inject
    StepStatusControl stepStatusControl;
    @Inject
    ReturnInfoTransform returnInfoTransform;
    @Inject
    AppraisalDetailTransform appraisalDetailTransform;

    @Inject
    private CheckMandateDocControl checkMandateDocControl;
    @Inject
    BRMSControl brmsControl;

    private ManageButton manageButton;
    private AppHeaderView appHeaderView;
    private long stepId;
    private long statusId;
    private int stageId;
    private int requestAppraisal;
    private boolean requestAppraisalPage;
    private int qualitativeType;
    private int pricingDOALevel;
    private List<User> abdmUserList;
    private List<User> zmUserList;
    private List<User> rgmUserList;
    private List<User> ghmUserList;
    private List<User> cssoUserList;

    private User user;
    //private User abdm;

    private String aadCommitteeId;

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

    private boolean isSubmitToRGM;
    private boolean isSubmitToGHM;
    private boolean isSubmitToCSSO;

    //Cancel CA FullApp
    private List<Reason> cancelReason;
    private String cancalCARemark;
    private int reasonId;

    //Return BDM Dialog
    private List<ReturnInfoView> returnInfoViewList;
    private List<Reason> returnReason;
    private String returnRemark;
    private int editRecordNo;
    private List<ReturnInfoView> returnInfoHistoryViewList;

    //Request Appraisal
    private enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;

    private AppraisalView appraisalView;

    private AppraisalDetailView appraisalDetailView;
    private AppraisalDetailView appraisalDetailViewSelected;
    private List<AppraisalDetailView> appraisalDetailViewList;



    private AppraisalContactDetailView appraisalContactDetailView;
    private AppraisalContactDetailView selectAppraisalContactDetailView;

    private boolean titleDeedFlag;
    private boolean purposeFlag;
    private boolean numberOfDocumentsFlag;
    private boolean contactFlag;
    private boolean contactFlag2;
    private boolean contactFlag3;

    private HashMap<String, Integer> stepStatusMap;

    //CheckMandate
    private CheckMandateDocView checkMandateDocView;
    private long workCaseId;

    private String messageHeader;
    private String message;

    public BaseController() {
    }

    @PostConstruct
    public void onCreation() {
        log.info("BaseController ::: Creation ");
        manageButton = new ManageButton();
        HttpSession session = FacesUtil.getSession(true);
        long workCasePreScreenId = 0L;
        long workCaseId = 0L;
        stepId = 0L;
        statusId = 0L;
        stageId = 0;

        requestAppraisal = 0;

        requestAppraisalPage = false;

        if (!Util.isNull(session.getAttribute("workCasePreScreenId"))) {
            workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
        }
        if (!Util.isNull(session.getAttribute("stepId"))) {
            stepId = (Long)session.getAttribute("stepId");
        }
        if(!Util.isNull(session.getAttribute("statusId"))) {
            statusId = (Long)session.getAttribute("statusId");
        }
        if(!Util.isNull(session.getAttribute("stageId"))){
            stageId = (Integer)session.getAttribute("stageId");
        }
        if (!Util.isNull(session.getAttribute("requestAppraisal"))){
            requestAppraisal = Integer.valueOf(session.getAttribute("requestAppraisal").toString());
            if((stepId == StepValue.REQUEST_APPRAISAL.value() || stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()) && requestAppraisal == 2){
                requestAppraisal = 1;
            }
        }
        log.debug("Current Page : {}", Util.getCurrentPage());
        if (Util.getCurrentPage().equals("appraisalRequest.jsf")){
            requestAppraisalPage = true;
        }

        log.info("BaseController ::: getSession : workCasePreScreenId : {}, workcase = {}, stepId = {}, stageId = {}", workCasePreScreenId, workCaseId, stepId, stageId);
        log.debug("BaseController ::: find active button");

        //TODO Get all action from Database By Step and Status and Role
        stepStatusMap = stepStatusControl.getStepStatusByStepStatusRole(stepId, statusId);
        log.debug("stepStatusMap : {}", stepStatusMap);

        /*if (stepId == StepValue.PRESCREEN_INITIAL.value()) {
            manageButton.setAssignToCheckerButton(true);
            manageButton.setCancelCAButton(true);
            manageButton.setCheckMandateDocButton(true);
        } else if (stepId == StepValue.PRESCREEN_CHECKER.value()) {
            manageButton.setCheckMandateDocButton(true);
            manageButton.setCheckNCBButton(true);
            manageButton.setReturnToMakerButton(true);
        } else if (stepId == StepValue.PRESCREEN_MAKER.value()) {
            if(Util.getCurrentPage().equals("prescreenMaker.jsf") || Util.getCurrentPage().equals("prescreenResult.jsf")){
                manageButton.setCancelCAButton(true);
                manageButton.setCloseSaleButton(true);
                manageButton.setCheckBRMSButton(true);
                manageButton.setCheckMandateDocButton(true);
                if(requestAppraisal == 0){
                    manageButton.setRequestAppraisalButton(true);
                }
            }else if(Util.getCurrentPage().equals("appraisalRequest.jsf")){
                manageButton.setCheckMandateDocButton(true);
                manageButton.setCancelAppraisalButton(true);
                //manageButton.setSubmitAppraisalButton(true);
                manageButton.setSubmitRequestAppraisalButton(true);
            }
        } else if (stepId == StepValue.FULLAPP_BDM_SSO_ABDM.value()) {
            if(Util.getCurrentPage().equals("/site/appraisalRequest.jsf")){
                manageButton.setCheckMandateDocButton(true);
                manageButton.setCancelAppraisalButton(true);
                //manageButton.setSubmitAppraisalButton(true);
                manageButton.setSubmitRequestAppraisalButton(true);
            }else{
                manageButton.setViewRelatedCA(true);
                if(requestAppraisal == 0){
                    manageButton.setRequestAppraisalButton(true);
                }
                manageButton.setCheckMandateDocButton(true);
                manageButton.setCheckCriteriaButton(true);
                manageButton.setAssignToABDMButton(true);
                manageButton.setCancelCAFullAppButton(true);
                manageButton.setSubmitCAButton(true);
                manageButton.setReturnBDMButton(true);
            }
        } else if (stepId == StepValue.REQUEST_APPRAISAL.value()) {
            //Step at AAD Admin (Appraisal Appointment)
            manageButton.setCheckMandateDocButton(true);
            manageButton.setReturnAppraisalBDMButton(true);
            manageButton.setSubmitAADCommitteeButton(true);
        } else if (stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()){
            //Step at AAD Committee (Appraisal Result)
            manageButton.setReturnAADAdminButton(true);
            manageButton.setSubmitAppraisalButton(true);
        }*/

        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
        log.info("BaseController ::: appHeader : {}", appHeaderView);

        if(session.getAttribute("workCaseId") != null){
            try{
                workCaseId = (Long)session.getAttribute("workCaseId");
            } catch (ClassCastException ex){
                log.error("Exception :", ex);
            }

            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if(basicInfo != null){
                qualitativeType = basicInfo.getQualitativeType();
            }
            log.debug("Qualitative type : {}", qualitativeType);
        }


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
        if(stepStatusMap!=null && stepStatusMap.containsKey(buttonName)){
            check = Util.isTrue(stepStatusMap.get(buttonName));
        }
        return check;
    }

    /*public String getQualitativeType(){

    }*/

    public void onOpenAssignToABDM(){
        log.debug("onOpenAssignToABDM ::: starting...");
        abdmUserId = "";
        assignRemark = "";
        abdmUserList = fullApplicationControl.getABDMUserList();
        log.debug("onOpenAssignToABDM ::: abdmUserList size : {}", abdmUserList.size());
    }

    public void onAssignToABDM(){
        log.debug("onAssignToABDM ::: starting...");
        boolean complete = false;
        if(abdmUserId != null && !abdmUserId.equals("")){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                fullApplicationControl.assignToABDM(abdmUserId, queueName, workCaseId);
                messageHeader = "Information.";
                message = "Assign to ABDM success.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                complete = true;
                log.debug("onAssignToABDM ::: success.");
            } catch (Exception ex){
                messageHeader = "Information.";
                message = "Assign to ABDM failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onAssignToABDM ::: exception occurred : ", ex);
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onOpenCancelCAFullApp(){
        log.debug("onOpenCancelCAFullApp ::: starting...");
        cancalCARemark = "";
        reasonId = 0;
        cancelReason = fullApplicationControl.getCancelReasonList();
        log.debug("onOpenCancelCAFullApp ::: cancelReason size : {}", cancelReason.size());
    }

    public void onCancelCAFullApp(){
        log.debug("onCancelCAFullApp ::: starting...");
        boolean complete = false;
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            fullApplicationControl.cancelCAFullApp(workCaseId, queueName, reasonId, cancalCARemark);
            messageHeader = "Information.";
            message = "Cancel CA success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            complete = true;
            log.debug("onCancelCAFullApp ::: success.");
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Cancel CA failed, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.error("onCancelCAFullApp ::: exception occurred : ", ex);
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onOpenSubmitZM(){
        log.debug("onOpenSubmitZM ::: starting...");
        log.debug("onOpenSubmitZM ::: find Pricing DOA Level");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        PricingDOAValue pricingDOA = fullApplicationControl.calculatePricingDOA(workCaseId);
        pricingDOA = PricingDOAValue.CSSO_DOA;
        if(!Util.isNull(pricingDOA)){
            pricingDOALevel = pricingDOA.value();
            zmEndorseUserId = "";
            rgmUserId = "";
            ghmUserId = "";
            cssoUserId = "";

            zmEndorseRemark = "";
            submitRemark = "";
            slaRemark = "";

            isSubmitToRGM = false;
            isSubmitToGHM = false;
            isSubmitToCSSO = false;

            zmUserList = fullApplicationControl.getZMUserList();

            if(pricingDOA.value() >= PricingDOAValue.RGM_DOA.value()){
                rgmUserList = fullApplicationControl.getRMUserList();
                isSubmitToRGM = true;
            }

            if(pricingDOA.value() >= PricingDOAValue.GH_DOA.value()){
                ghmUserList = fullApplicationControl.getHeadUserList();
                isSubmitToGHM = true;
            }

            if(pricingDOA.value() >= PricingDOAValue.CSSO_DOA.value()){
                cssoUserList = fullApplicationControl.getCSSOUserList();
                isSubmitToCSSO = true;
            }

            log.debug("pricingDOALevel ::: {}", pricingDOA);
            RequestContext.getCurrentInstance().execute("submitZMDlg.show()");
        } else {
            messageHeader = "Exception.";
            message = "Can not find Pricing DOA Level. Please check value for calculate DOA Level";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onSubmitZM(){
        log.debug("onSubmitZM ::: starting...");
        boolean complete = false;
        if(zmUserId != null && !zmUserId.equals("")){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                fullApplicationControl.submitToZMPricing(zmUserId, rgmUserId, ghmUserId, cssoUserId, queueName, workCaseId);
                messageHeader = "Information.";
                message = "Submit to Zone Manager success.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                complete = true;
                log.debug("onAssignToABDM ::: success.");
            } catch (Exception ex){
                messageHeader = "Exception.";
                message = "Submit to Zone Manager failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onSubmitZM ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = "Exception.";
            message = "Submit to Zone Manager failed, cause : ZM not selected";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.error("onSubmitZM ::: submit failed (ZM not selected)");
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onCancelCA(){

    }

    public void onSubmitCA(){

    }

    /*public void onRequestAppraisal(){
        log.debug("onRequestAppraisal ( bdm input data for aad admin )");
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        try{
            HttpSession session = FacesUtil.getSession(true);
            if(session.getAttribute("workCaseId") != null){
                workCaseId = (Long)session.getAttribute("workCaseId");
            }

            if(session.getAttribute("workCasePreScreenId") != null){
                workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            }

            fullApplicationControl.requestAppraisalBDM(workCasePreScreenId, workCaseId);
            log.debug("onRequestAppraisal ::: save Appraisal Flag completed. Redirect to appraisalRequest.jsf");
            FacesUtil.redirect("/site/appraisalRequest.jsf");

        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }*/

    /*public void onSubmitAppraisalAdmin(){
        log.debug("onRequestAppraisal ( submit to AAD admin )");
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        try{
            HttpSession session = FacesUtil.getSession(true);
            if(session.getAttribute("workCaseId") != null){
                workCaseId = (Long)session.getAttribute("workCaseId");
            }
            if(session.getAttribute("workCasePreScreenId") != null){
                workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            }

            fullApplicationControl.requestAppraisal(workCasePreScreenId, workCaseId);

            messageHeader = "Information.";
            message = "Request for appraisal success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }*/

    public void onSubmitAppraisalCommittee(){
        log.debug("onSubmitAppraisalCommittee ( submit to AAD committee )");
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        String queueName = "";
        try{
            HttpSession session = FacesUtil.getSession(true);
            if(!Util.isNull(session.getAttribute("workCaseId"))){
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            }
            if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
                workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            }
            if(!Util.isNull(session.getAttribute("queueName"))){
                queueName = session.getAttribute("queueName").toString();
            }

            //TODO Save AADCommittee user id to appraisal
            fullApplicationControl.submitToAADCommittee(aadCommitteeId, workCaseId, workCasePreScreenId, queueName);

            //fullApplicationControl.submitToAADCommittee(workCaseId, workCasePreScreenId, queueName);

            messageHeader = "Information.";
            message = "Request for appraisal success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }

    }

    public void onCheckPreScreen(){
        long workCasePreScreenId = 0;
        HttpSession session = FacesUtil.getSession(true);
        if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            UWRulesResponse uwRulesResponse = brmsControl.getPrescreenResult(workCasePreScreenId);
            log.debug("onCheckPreScreen uwRulesResponse : {}", uwRulesResponse);
            if(uwRulesResponse != null){
                if(uwRulesResponse.getActionResult().equals(ActionResult.SUCCEED)){

                }else if(uwRulesResponse.getActionResult().equals(ActionResult.FAILED)){

                }
            }
        }

    }

    //
    public void onCheckMandateDialog(){
        log.debug("onCheckMandateDialog ::: starting...");
        HttpSession session = FacesUtil.getSession(true);
        try {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        } catch (Exception e) {
            workCaseId = 0;
        }

        String result = null;
        checkMandateDocView = null;
        try{
            checkMandateDocView = checkMandateDocControl.getMandateDocView(workCaseId);
            if(!Util.isNull(checkMandateDocView)){
                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
            } else {
                log.debug("-- Find by work case id = {} CheckMandateDocView is {}   ", workCaseId, checkMandateDocView);
                checkMandateDocView = new CheckMandateDocView();
                log.debug("-- CheckMandateDocView[New] created");
            }
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onSaveCheckMandateDoc(){
        log.debug("-- onSaveCheckMandateDoc().");
        try {
            checkMandateDocControl.onSaveMandateDoc(checkMandateDocView, workCaseId);
            messageHeader = "Success";
            message = "Success";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        } catch (Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = "Failed";
            message = "Failed";
//            if(ex.getCause() != null){
//                message = "Failed " + ex.getCause().toString();
//            } else {
//                message = "Failed " + ex.getMessage();
//            }
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onCancelCheckMandateDoc(){

    }

    public void onOpenReturnBDMDialog(){
        log.debug("onOpenReturnBDM ::: starting...");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

        //get from not accept List and from CheckMandateDoc
        returnInfoViewList = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId);

        //set return code master
        returnReason = returnControl.getReturnReasonList();
        returnRemark = "";

        log.debug("onOpenReturnBDM ::: returnInfoViewList size : {}", returnInfoViewList.size());
    }

    public void resetAddReturnInfo(){
        returnRemark = "";
        reasonId = 0;
        editRecordNo = -1;
    }

    public void onOpenAddReturnInfo(){
        log.debug("onOpenAddReturnInfo ::: starting...");
        resetAddReturnInfo();
    }

    public void onSaveReturnInfo(){
        log.debug("onSaveReturnInfo ::: starting...");
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

    public void onSumbitReturnBDM(){ //Submit return from UW1 to BDM
        log.debug("onSumbitReturnSummary ::: returnInfoViewList size : {}", returnInfoViewList);
        boolean complete = false;
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                User user = (User) session.getAttribute("user");
                long stepId = Long.parseLong(session.getAttribute("stepId").toString());

                List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId);

                if(returnInfoViews!=null && returnInfoViews.size()>0){
                    messageHeader = "Information.";
                    message = "Submit fail. Please check return information before submit return again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitReviewReturn ::: fail.");
                } else {
                    returnControl.submitReturnBDM(workCaseId, queueName, user, stepId, returnInfoViewList);
                    messageHeader = "Information.";
                    message = "Return to BDM success.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                    complete = true;
                    log.debug("onReturnBDMSubmit ::: success.");
                }
            } catch (Exception ex){
                messageHeader = "Information.";
                message = "Return to BDM failed, cause : " + Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                complete = false;
                log.error("onReturnBDMSubmit ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = "Information.";
            message = "Return to BDM failed, have no reason to return.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.debug("onSumbitReturnSummary ::: Return to BDM failed, have no reason to return.");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onSumbitReturnUW1(){ //Submit Reply From BDM to UW1
        log.debug("onSumbitReturnReply");

        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            User user = (User) session.getAttribute("user");
            long stepId = Long.parseLong(session.getAttribute("stepId").toString());
            List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReplyList(workCaseId);

            if(returnInfoViews!=null && returnInfoViews.size()>0){
                messageHeader = "Information.";
                message = "Submit Return fail. Please check return information again.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                log.error("onSumbitReturnReply ::: fail.");
            } else {
                returnControl.submitReturnUW1(workCaseId, queueName);

                messageHeader = "Information.";
                message = "Submit Return success";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                log.debug("onReturnBDMSubmit ::: success.");
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit Return fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onSumbitReturnReply ::: exception occurred : ", ex);
        }
    }

    public void onSubmitUW2(){ //Submit From UW1 (no return)
        log.debug("onSubmitUW2 begin");
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            User user = (User) session.getAttribute("user");

            List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId);

            if(returnInfoViews!=null && returnInfoViews.size()>0){
                messageHeader = "Information.";
                message = "Submit fail. Please check return information before submit again.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                log.error("onSubmitUW2 ::: fail.");
            } else {
                //check if have return not accept
                List<ReturnInfoView> returnInfoViewsNoAccept = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId);
                if(returnInfoViewsNoAccept!=null && returnInfoViewsNoAccept.size()>0){
                    messageHeader = "Information.";
                    message = "Submit fail. Please check return information before submit again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitUW2 ::: fail.");
                } else {
                    returnControl.saveReturnHistory(workCaseId,user);

                    //TODO: execute bpm workflow for submit to UW2

                    messageHeader = "Information.";
                    message = "Submit success";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                    log.debug("onSubmitUW2 ::: success.");
                }
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onSubmitUW2 ::: exception occurred : ", ex);
        }
    }

    public void onEditReturnInfo(int rowOnTable) {
        log.debug("onEditReturnInfo ::: rowOnTable : {}",rowOnTable);
        ReturnInfoView returnInfoView = returnInfoViewList.get(rowOnTable);
        reasonId = returnInfoView.getReasonId();
        returnRemark = returnInfoView.getReasonDetail();
        editRecordNo = rowOnTable;
        log.debug("onEditReturnInfo ::: end");
    }

    public void onDeleteReturnInfo(int rowOnTable) {
        log.debug("onDeleteReturnInfo ::: rowOnTable : {}",rowOnTable);
        returnInfoViewList.remove(rowOnTable);

        resetAddReturnInfo();
        log.debug("onDeleteReturnInfo ::: end");
    }


    //Function for Appraisal Request ( BDM )
    public void onOpenRequestAppraisal(){
        log.debug("onOpenRequestAppraisal");

        appraisalView = new AppraisalView();
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
    }

    public void onSubmitRequestAppraisal(){
        log.debug("onSubmitRequestAppraisal ( bdm input data for aad admin )");
        log.debug("onSubmitRequestAppraisal ::: starting to save RequestAppraisal.");
        HttpSession session = FacesUtil.getSession(true);
        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;
        long workCaseId = 0;
        long workCasePreScreenId = 0;

        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = (Long)session.getAttribute("workCaseId");
        }

        if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
        }

        log.debug("onSubmitRequestAppraisal ::: workCaseId : {}, workCasePreScreenId : {}", session.getAttribute("workCaseId"), session.getAttribute("workCasePreScreenId"));

        if(checkAppraisalContact()){
            if(appraisalDetailViewList.size() > 0){
                try{
                    //Save Appraisal Request
                    appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
                    appraisalView.setAppraisalContactDetailView(appraisalContactDetailView);

                    //Submit Appraisal - Create WRK_Appraisal And Launch new Workflow
                    fullApplicationControl.requestAppraisal(appraisalView, workCasePreScreenId, workCaseId);
                    log.debug("onSubmitRequestAppraisal ::: create new Work Case Appraisal, Launch new workflow.");

                    complete = true;

                    messageHeader = "Information.";
                    message = "Request appraisal completed.";

                    context.execute("msgBoxBaseMessageDlg.show()");
                } catch(Exception ex){
                    log.error("Exception while submitRequestAppraisal : ", ex);
                    messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
                    message = msg.get("app.appraisal.request.message.body.save.fail") + Util.getMessageException(ex);

                    context.execute("msgBoxBaseMessageDlg.show()");
                }
            } else {
                messageHeader = "Information.";
                message = "Please add information in Appraisal Detail at least 1.";

                context.execute("msgBoxBaseMessageDlg.show()");
            }
        }

        context.addCallbackParam("functionComplete", complete);
    }

    public void onSaveAppraisalDetail(){
        log.debug("-- onSaveAppraisalDetailView() flag = {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if(checkAppraisalDialog()){
            complete = true;
            if(ModeForButton.ADD == modeForButton){
                appraisalDetailViewList.add(appraisalDetailView);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }else if(ModeForButton.EDIT == modeForButton){
                log.debug("-- RowIndex[{}]", rowIndex);
                appraisalDetailViewList.set(rowIndex, appraisalDetailView);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }
            context.addCallbackParam("functionComplete", complete);
        }else {
            context.addCallbackParam("functionComplete", complete);
        }
    }

    public boolean checkAppraisalDialog(){
        log.debug("-- appraisalDetailViewMandate() ::: appraisalDetailView : {}", appraisalDetailView);
        boolean result = true;
        if(!Util.isNull(appraisalDetailView.getTitleDeed())){
            if(Util.isZero(appraisalDetailView.getTitleDeed().length())){
                titleDeedFlag = true;
                result = false;
            } else {
                titleDeedFlag = false;
            }
        } else {
            titleDeedFlag = true;
            result = false;
        }
        if(!appraisalDetailView.isPurposeNewAppraisalB() && !appraisalDetailView.isPurposeReviewAppraisalB() && !appraisalDetailView.isPurposeReviewBuildingB()){
            purposeFlag = true;
            result = false;
        } else {
            purposeFlag = false;
        }
        if(appraisalDetailView.getCharacteristic() == 1 && appraisalDetailView.getNumberOfDocuments() == 0){
            numberOfDocumentsFlag = true;
            result = false;
        } else {
            numberOfDocumentsFlag = false;
        }

        log.debug("-- titleDeedFlag = {}", titleDeedFlag);
        log.debug("-- purposeFlag = {}", purposeFlag);
        log.debug("-- numberOfDocumentsFlag = {}", numberOfDocumentsFlag);
        log.debug("-- result = {}", result);

        return result;
    }

    public boolean checkAppraisalContact(){
        log.debug("-- checkAppraisalContact()");
        //todo :  2 0 21
        boolean result = true;

        if(appraisalContactDetailView.getCustomerName1().length() == 0 && appraisalContactDetailView.getContactNo1().length() == 0 ){
            contactFlag = true;
            result = false;
        } else {
            contactFlag = false;
        }

        log.debug("-- contactFlag = {}", contactFlag);
        log.debug("-- result = {}", result);
        return result;
    }

    public void onEditAppraisalDetail(){
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalDetailView() RowIndex[{}]", rowIndex);
        Cloner cloner = new Cloner();
        try{
            appraisalDetailView = cloner.deepClone(appraisalDetailViewSelected);
        } catch (Exception ex){
            log.error("Exception occur when clone appraisalDetailView.");
        }
    }

    public void onAddAppraisalDetail(){
        log.info("-- onAddAppraisalDetailView() ModeForButton[ADD]");
        appraisalDetailView = new AppraisalDetailView();
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onGoToInbox(){
        FacesUtil.redirect("/site/inbox.jsf");
    }

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

    public AppHeaderView getAppHeaderView() {
        return appHeaderView;
    }

    public void setAppHeaderView(AppHeaderView appHeaderView) {
        this.appHeaderView = appHeaderView;
    }

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
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

    public String getCancalCARemark() {
        return cancalCARemark;
    }

    public void setCancalCARemark(String cancalCARemark) {
        this.cancalCARemark = cancalCARemark;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
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

    public boolean isRequestAppraisalPage() {
        return requestAppraisalPage;
    }

    public void setRequestAppraisalPage(boolean requestAppraisalPage) {
        this.requestAppraisalPage = requestAppraisalPage;
    }

    public CheckMandateDocView getCheckMandateDocView() {
        return checkMandateDocView;
    }

    public void setCheckMandateDocView(CheckMandateDocView checkMandateDocView) {
        this.checkMandateDocView = checkMandateDocView;
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

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public AppraisalView getAppraisalView() {
        return appraisalView;
    }

    public void setAppraisalView(AppraisalView appraisalView) {
        this.appraisalView = appraisalView;
    }

    public AppraisalDetailView getAppraisalDetailView() {
        return appraisalDetailView;
    }

    public void setAppraisalDetailView(AppraisalDetailView appraisalDetailView) {
        this.appraisalDetailView = appraisalDetailView;
    }

    public AppraisalDetailView getAppraisalDetailViewSelected() {
        return appraisalDetailViewSelected;
    }

    public void setAppraisalDetailViewSelected(AppraisalDetailView appraisalDetailViewSelected) {
        this.appraisalDetailViewSelected = appraisalDetailViewSelected;
    }

    public List<AppraisalDetailView> getAppraisalDetailViewList() {
        return appraisalDetailViewList;
    }

    public void setAppraisalDetailViewList(List<AppraisalDetailView> appraisalDetailViewList) {
        this.appraisalDetailViewList = appraisalDetailViewList;
    }

    public AppraisalContactDetailView getAppraisalContactDetailView() {
        return appraisalContactDetailView;
    }

    public void setAppraisalContactDetailView(AppraisalContactDetailView appraisalContactDetailView) {
        this.appraisalContactDetailView = appraisalContactDetailView;
    }

    public boolean isTitleDeedFlag() {
        return titleDeedFlag;
    }

    public void setTitleDeedFlag(boolean titleDeedFlag) {
        this.titleDeedFlag = titleDeedFlag;
    }

    public boolean isPurposeFlag() {
        return purposeFlag;
    }

    public void setPurposeFlag(boolean purposeFlag) {
        this.purposeFlag = purposeFlag;
    }

    public boolean isNumberOfDocumentsFlag() {
        return numberOfDocumentsFlag;
    }

    public void setNumberOfDocumentsFlag(boolean numberOfDocumentsFlag) {
        this.numberOfDocumentsFlag = numberOfDocumentsFlag;
    }

    public boolean isContactFlag() {
        return contactFlag;
    }

    public void setContactFlag(boolean contactFlag) {
        this.contactFlag = contactFlag;
    }

    public boolean isContactFlag2() {
        return contactFlag2;
    }

    public void setContactFlag2(boolean contactFlag2) {
        this.contactFlag2 = contactFlag2;
    }

    public boolean isContactFlag3() {
        return contactFlag3;
    }

    public void setContactFlag3(boolean contactFlag3) {
        this.contactFlag3 = contactFlag3;
    }
}
