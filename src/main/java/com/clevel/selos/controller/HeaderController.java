package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.AuthorizationDOA;
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
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "headerController")
public class HeaderController implements Serializable {
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
    private CheckMandateDocControl checkMandateDocControl;
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
    ReturnInfoTransform returnInfoTransform;
    @Inject
    AppraisalDetailTransform appraisalDetailTransform;

    private ManageButton manageButton;
    private AppHeaderView appHeaderView;

    private int qualitativeType;
    private int pricingDOALevel;
    private List<User> abdmUserList;
    private List<User> zmUserList;
    private List<User> rgmUserList;
    private List<User> ghmUserList;
    private List<User> cssoUserList;
    private List<User> aadCommiteeList;
    private boolean requestPricing;

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

    private String aadAdminId;
    private String aadAdminName;

    //Cancel CA FullApp
    private List<Reason> cancelReason;
    private String cancelRemark;
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

    //Session variable
    private long workCaseId;
    private long workCasePreScreenId;
    private long stepId;
    private long statusId;
    private int stageId;
    private int requestAppraisal;
    private String queueName;
    private String wobNumber;

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

    //Request Appraisal ( After Customer Acceptance )
    private List<User> aadAdminList;

    //Return AAD Admin ( UW2 )
    private int returnReasonId;
    private String returnAADRemark;

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
        stepStatusMap = stepStatusControl.getStepStatusByStepStatusRole(stepId, statusId);
        log.debug("HeaderController ::: stepStatusMap : {}", stepStatusMap);

        //FOR Appraisal Request Dialog
        appraisalView = new AppraisalView();
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();

        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
        log.debug("HeaderController ::: appHeader : {}", appHeaderView);

        if(workCaseId != 0){
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

    private void _loadSessionVariable(){
        HttpSession session = FacesUtil.getSession(true);

        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        stageId = Util.parseInt(session.getAttribute("stageId"), 0);
        requestAppraisal = Util.parseInt(session.getAttribute("requestAppraisal"), 0);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
    }

    public boolean checkButton(String buttonName){
        boolean check = false;
        if(stepStatusMap != null && stepStatusMap.containsKey(buttonName)){
            check = Util.isTrue(stepStatusMap.get(buttonName));
        }
        return check;
    }

    //------- Function for Assign to ABDM ----------//
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
                String queueName = Util.parseString(session.getAttribute("queueName"), "");
                String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
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

    //---------- Function for Cancel CA -----------//
    public void onOpenCancelCA(){
        log.debug("onOpenCancelCA ::: starting...");
        cancelRemark = "";
        reasonId = 0;
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.CANCEL_REASON);
        log.debug("onOpenCancelCA ::: reasonList.size() : {}", reasonList.size());
    }

    public void onCancelCA(){
        log.debug("onCancelCA ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.cancelCAFullApp(queueName, wobNumber, reasonId, cancelRemark);
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

    //---------- Function for Submit CA ( BDM to Zone ) ------------//
    public void onOpenSubmitZM(){
        log.debug("onOpenSubmitZM ::: starting...");
        log.debug("onOpenSubmitZM ::: find Pricing DOA Level");
        _loadSessionVariable();
        try{
            requestPricing = fullApplicationControl.getRequestPricing(workCaseId);
            if(requestPricing){
                pricingDOALevel = fullApplicationControl.getPricingDOALevel(workCaseId);
                if(pricingDOALevel != 0){
                    zmEndorseUserId = "";
                    zmUserId = "";
                    rgmUserId = "";
                    ghmUserId = "";
                    cssoUserId = "";

                    zmEndorseRemark = "";
                    submitRemark = "";
                    slaRemark = "";

                    isSubmitToRGM = false;
                    isSubmitToGHM = false;
                    isSubmitToCSSO = false;

                    zmUserList = fullApplicationControl.getUserList(user);

                    if(pricingDOALevel >= PricingDOAValue.RGM_DOA.value()){
                        isSubmitToRGM = true;
                    }

                    if(pricingDOALevel >= PricingDOAValue.GH_DOA.value()){
                        isSubmitToGHM = true;
                    }

                    if(pricingDOALevel >= PricingDOAValue.CSSO_DOA.value()){
                        isSubmitToCSSO = true;
                    }
                    log.debug("onOpenSubmitZM ::: pricingDOALevel : {}", pricingDOALevel);
                    RequestContext.getCurrentInstance().execute("submitZMDlg.show()");
                } else {
                    messageHeader = msg.get("app.messageHeader.exception");
                    message = msg.get("app.message.dialog.doapricing.notfound");
                    showMessageBox();
                }
            } else {
                zmUserList = fullApplicationControl.getUserList(user);
                log.debug("onOpenSubmitZM ::: No pricing request");
                RequestContext.getCurrentInstance().execute("submitZMDlg.show()");
            }
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    //---------- Submit CA ( BDM to Zone ) -----------//
    public void onSubmitZM(){
        log.debug("onSubmitZM ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        if(zmUserId != null && !zmUserId.equals("")){
            try{
                fullApplicationControl.submitToZM(queueName, wobNumber, zmUserId, rgmUserId, ghmUserId, cssoUserId, submitRemark, workCaseId);
                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.message.dialog.submit.success");
                showMessageRedirect();
                complete = true;
                log.debug("onSubmitZM ::: success.");
            } catch (Exception ex){
                messageHeader = msg.get("app.messageHeader.exception");
                message = Util.getMessageException(ex);
                showMessageBox();
                log.error("onSubmitZM ::: exception occurred : ", ex);
            }
        } else {
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : ZM not selected";
            showMessageBox();
            log.error("onSubmitZM ::: submit failed (ZM not selected)");
        }
        sendCallBackParam(complete);
    }

    public void onSelectedZM(){
        if(pricingDOALevel >= PricingDOAValue.RGM_DOA.value()){
            rgmUserId = "";
            User userZm = userDAO.findById(zmUserId);
            rgmUserList = fullApplicationControl.getUserList(userZm);
        }
    }

    public void onSelectedRM(){
        if(pricingDOALevel >= PricingDOAValue.GH_DOA.value()){
            ghmUserId = "";
            User userRm = userDAO.findById(rgmUserId);
            ghmUserList = fullApplicationControl.getUserList(userRm);
        }
    }

    public void onSelectedGH(){
        if(pricingDOALevel >= PricingDOAValue.CSSO_DOA.value()){
            cssoUserId = "";
            User userGh = userDAO.findById(ghmUserId);
            cssoUserList = fullApplicationControl.getUserList(userGh);
        }
    }

    //---------- Submit CA ( Zone to Region [Price Reduction]) -----------//
    public void onSubmitPriceReduceRGM(){
        log.debug("onSubmitPriceReduceRM ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitToRGMPriceReduce(queueName, wobNumber, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitPriceReduceRM ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitPriceReduceRM ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    //---------- Submit CA ( Zone to Region ) -----------//
    public void onSubmitRM(){
        log.debug("onSubmitRM ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitToRM(queueName, wobNumber, workCaseId);
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

    //---------- Submit CA ( Region to Group Head ) -----------//
    public void onSubmitGH(){
        log.debug("onSubmitGH ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitToGH(queueName, wobNumber, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitGH ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitGH ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    //---------- Submit CA ( Group Head to CSSO ) -----------//
    public void onSubmitCSSO(){
        log.debug("onSubmitCSSO ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitToCSSO(queueName, wobNumber, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageBox();
            complete = true;
            log.debug("onSubmitCSSO ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : " + Util.getMessageException(ex);
            showMessageRedirect();
            log.error("onSubmitCSSO ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    //---------- Submit CA ( CSSO to UW1 ) -----------//
    public void onSubmitUWFromCSSO(){
        log.debug("onSubmitUWFromCSSO ::: starting...");
        _loadSessionVariable();
        boolean complete = false;
        try{
            fullApplicationControl.submitToUWFromCSSO(queueName, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.submit.success");
            showMessageRedirect();
            complete = true;
            log.debug("onSubmitUWFromCSSO ::: success.");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = "Submit case failed, cause : " + Util.getMessageException(ex);
            showMessageBox();
            log.error("onSubmitUWFromCSSO ::: exception occurred : ", ex);
        }
        sendCallBackParam(complete);
    }

    public void onSubmitUWFromZM(){
        log.debug("onSubmitUWFromZM ::: starting...");
        boolean complete = false;
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            fullApplicationControl.submitToUWFromZM(queueName, workCaseId);
            messageHeader = "Information.";
            message = "Submit case success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            complete = true;
            log.debug("onSubmitUWFromZM ::: success.");
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Submit case failed, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            complete = false;
            log.error("onSubmitUWFromZM ::: exception occurred : ", ex);
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    //---------- Submit CA ( to UW2 ) -----------//
    public void onOpenSubmitUW2(){
        log.debug("onOpenSubmitUW ::: starting...");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = (Long)session.getAttribute("workCaseId");
        selectedUW2User = "";
        selectedDOALevel = 0;
        try{
            authorizationDOAList = fullApplicationControl.getAuthorizationDOALevelList(workCaseId);
            RequestContext.getCurrentInstance().execute("submitUWDlg.show()");
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
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

    public void onSubmitUW2(){ //Submit From UW1 (no return)
        log.debug("onSubmitUW2 begin");
        _loadSessionVariable();
        boolean complete = false;
        try{
            HttpSession session = FacesUtil.getSession(true);
            User user = (User) session.getAttribute("user");

            if(selectedUW2User != null && !selectedUW2User.equals("")){
                List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId);

                if(returnInfoViews!=null && returnInfoViews.size()>0){
                    messageHeader = "Information.";
                    message = "Submit case fail. Please check return information before submit again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitUW2 ::: fail.");
                } else {
                    //check if have return not accept
                    List<ReturnInfoView> returnInfoViewsNoAccept = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId);
                    if(returnInfoViewsNoAccept!=null && returnInfoViewsNoAccept.size()>0){
                        messageHeader = "Information.";
                        message = "Submit case fail. Please check return information before submit again.";
                        RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                        log.error("onSubmitUW2 ::: fail.");
                    } else {
                        returnControl.saveReturnHistory(workCaseId,user);

                        fullApplicationControl.submitToUW2(selectedUW2User, selectedDOALevel, submitRemark, queueName, workCaseId);

                        messageHeader = "Information.";
                        message = "Submit case success";
                        RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                        log.debug("onSubmitUW2 ::: success.");
                    }
                }
                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.message.dialog.submit.success");
                showMessageRedirect();
                complete = true;
                log.debug("onSubmitUW2 ::: success.");
            } else {
                messageHeader = msg.get("app.messageHeader.exception");
                message = "Submit case failed, cause : UW2 was not selected";
                showMessageBox();
                complete = false;
                log.error("onSubmitUW2 ::: submit failed (UW2 not selected)");
            }
        } catch (Exception ex){
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            showMessageBox();

            log.error("onSubmitUW2 ::: exception occurred : ", ex);
        }

        sendCallBackParam(complete);
    }

    public void onSubmitFCashZM(){
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
    }

    /*public void onCancelCA(){
        fullApplicationControl.getUserList(user);
    }*/


    //---------- Submit CA ( UW2 [End Case] ) -----------//
    public void onSubmitCA(){ //Submit From UW2
        log.debug("onSubmitCA begin");
        _loadSessionVariable();
        boolean complete = false;
        try{
            HttpSession session = FacesUtil.getSession(true);
            User user = (User) session.getAttribute("user");

            List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId);

            if(returnInfoViews!=null && returnInfoViews.size()>0){
                messageHeader = "Information.";
                message = "Submit case fail. Please check return information before submit again.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                log.error("onSubmitCA ::: fail.");
            } else {
                //check if have return not accept
                List<ReturnInfoView> returnInfoViewsNoAccept = returnControl.getReturnInfoViewListFromMandateDocAndNoAccept(workCaseId);
                if(returnInfoViewsNoAccept!=null && returnInfoViewsNoAccept.size()>0){
                    messageHeader = "Information.";
                    message = "Submit case fail. Please check return information before submit again.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                    log.error("onSubmitCA ::: fail.");
                } else {
                    returnControl.saveReturnHistory(workCaseId,user);

                    fullApplicationControl.submitCA(queueName, workCaseId);

                    messageHeader = "Information.";
                    message = "Submit case success";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                    log.debug("onSubmitCA ::: success.");
                }
            }
            messageHeader = "Information.";
            message = "Submit case success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            complete = true;
            log.debug("onSubmitCA ::: success.");
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit case fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onSubmitCA ::: exception occurred : ", ex);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onOpenReturnAADMByUW2(){
        log.debug("onOpenReturnAADCommittee ( return to AAD Admin from UW2 [ Open dialog ] )");
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.RETURN_REASON);
        returnAADRemark = "";

        RequestContext.getCurrentInstance().execute("returnAADM_UW2Dlg.show()");
    }

    public void onReturnAADMByUW2(){
        log.debug("onReturnAADCommittee ( return to AAD Committee from UW2 )");
        HttpSession session = FacesUtil.getSession(true);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try {
            fullApplicationControl.returnAADAdminByUW2(queueName, wobNumber, returnAADRemark, returnReasonId);
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.message.dialog.return.success");
            showMessageRedirect();
        } catch (Exception ex) {
            log.error("Exception while return to aad committee : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            showMessageBox();
        }
    }

    public void onOpenSubmitAADCommittee(){
        log.debug("onOpenSubmitAADCommittee ( submit to AAD committee )");
        HttpSession session = FacesUtil.getSession(true);
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        }
        if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        }
        if(fullApplicationControl.checkAppointmentInformation(workCaseId, workCasePreScreenId)){
            //List AAD Admin by team structure
            aadCommiteeList = fullApplicationControl.getUserListByRole(RoleValue.AAD_COMITTEE);
            RequestContext.getCurrentInstance().execute("submitAADCDlg.show()");
        } else {
            //TO show error
            messageHeader = "Exception.";
            message = "Please input Appointment Date first.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onSubmitAADCommittee(){
        log.debug("onSubmitAADCommittee ( submit to AAD committee )");
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            long workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
            String queueName = Util.parseString(session.getAttribute("queueName"), "");
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

            fullApplicationControl.submitToAADCommittee(aadCommitteeId, workCaseId, workCasePreScreenId, queueName, wobNumber);

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

    public void onSubmitAppraisalToUW(){
        log.debug("onSubmitAppraisalToUW ( appraisal to uw )");
        String wobNumber = "";
        String queueName = "";

        HttpSession session = FacesUtil.getSession(true);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try{

            fullApplicationControl.submitToUWFromCommittee(queueName, wobNumber);

            messageHeader = "Information.";
            message = "Submit case success.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex){
            log.error("exception while submit case to uw2 : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onSubmitCustomerAcceptance(){
        log.debug("onSubmitCustomerAcceptance ( BDM submit to UW )");
        long workCaseId = 0;
        String wobNumber = "";
        String queueName = "";

        HttpSession session = FacesUtil.getSession(true);
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
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.PENDING_REASON);
        RequestContext.getCurrentInstance().execute("pendingDecisionDlg.show()");
    }

    public void onSubmitPendingDecision(){
        String wobNumber = "";
        String queueName = "";
        boolean complete = false;

        HttpSession session = FacesUtil.getSession(true);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        if(!Util.isNull(reasonId) && !Util.isZero(reasonId)){
            try{
                fullApplicationControl.submitPendingDecision(queueName, wobNumber, pendingRemark, reasonId);
                messageHeader = "Information.";
                message = "Submit case success.";
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
            } catch (Exception ex) {
                log.error("Exception while submit pending decision, ", ex);
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
                complete = true;
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onRequestPriceReduction(){
        log.debug("onRequestPriceReduction ( in step Customer Acceptance )");
        HttpSession session = FacesUtil.getSession(true);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
        long workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        requestPricing = fullApplicationControl.getRequestPricing(workCaseId);
        if(requestPricing) {
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
        } else {
            messageHeader = "Information.";
            message = "Can not Request for Price Reduction, cause this case has no Pricing Request.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }


    }

    public void onOpenCancelRequestPriceReduction(){
        log.debug("onOpenCancelRequestPriceReduction");
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.CANCEL_REASON);
        reasonId = 0;
        cancelRemark = "";
        RequestContext.getCurrentInstance().execute("cancelRequestPriceReduceDlg.show()");
    }

    public void onCancelRequestPriceReduction(){
        log.debug("onCancelRequestPriceReduction");
        HttpSession session = FacesUtil.getSession(true);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        if(!Util.isNull(reasonId) && !Util.isZero(reasonId)){
            try{
                fullApplicationControl.cancelRequestPriceReduction(queueName, wobNumber, reasonId, cancelRemark);
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

    //Request Appraisal after Customer Acceptance
    public void onOpenRequestAppraisalCustomerAccepted(){
        log.debug("onOpenRequestAppraisalCustomerAccepted ( after customer acceptance )");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);

        //Check Appraisal data exist.
        if(fullApplicationControl.checkAppraisalInformation(workCaseId)) {
            aadAdminList = fullApplicationControl.getUserListByRole(RoleValue.AAD_ADMIN);
            aadAdminId = "";
            RequestContext.getCurrentInstance().execute("reqAppr_BDMDlg.show()");
        } else {
            log.debug("onOpenRequestAppraisalCustomerAccepted : check appraisal information failed. do not open dialog.");
            messageHeader = "Information.";
            message = "Please complete request appraisal form.";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }

    }
    public void onRequestAppraisalCustomerAccepted(){
        log.debug("onRequestAppraisal by BDM ( after customer acceptance )");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try {
            fullApplicationControl.requestAppraisal(workCaseId, 0, queueName, wobNumber, aadAdminId);
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

    public void onCheckPreScreen(){
        long workCasePreScreenId = 0;
        HttpSession session = FacesUtil.getSession(true);
        if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            try{
                UWRuleResponseView uwRuleResponseView = brmsControl.getPrescreenResult(workCasePreScreenId, 1006);
                log.info("onCheckPreScreen uwRulesResponse : {}", uwRuleResponseView);
                if(uwRuleResponseView != null){
                    if(uwRuleResponseView.getActionResult().equals(ActionResult.SUCCESS)){
                        UWRuleResultSummaryView uwRuleResultSummaryView = null;
                        try{
                            uwRuleResultSummaryView = uwRuleResponseView.getUwRuleResultSummaryView();
                            uwRuleResultSummaryView.setWorkCasePrescreenId(workCasePreScreenId);
                            uwRuleResultControl.saveNewUWRuleResult(uwRuleResultSummaryView);
                        }catch (Exception ex){
                            log.error("Cannot Save UWRuleResultSummary {}", uwRuleResultSummaryView);
                            messageHeader = "Exception.";
                            message = Util.getMessageException(ex);

                        }
                        messageHeader = "Information.";
                        message = "Request for Check Pre-Screen success";
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
            } catch (Exception ex){
                log.error("Exception while getPrescreenResult : ", ex);
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
            }

            if(mandateFieldMessageViewList == null || mandateFieldMessageViewList.size() == 0)
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            else
                RequestContext.getCurrentInstance().execute("msgBoxMandateMessageDlg.show()");



        }
    }

    public void onOpenReturnBDMByAAD(){
        //TODO Get BDM Name
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.RETURN_REASON);
        returnRemark = "";
        RequestContext.getCurrentInstance().execute("returnBDM_AADAdminDlg.show()");
    }

    public void onReturnBDMByAAD(){
        String wobNumber = "";
        String queueName = "";
        boolean complete = false;

        HttpSession session = FacesUtil.getSession(true);
        queueName = Util.parseString(session.getAttribute("queueName"), "");
        wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

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

    public void onOpenReturnAADAdmin(){
        log.debug("onOpenReturnAADAdmin ( return to AADAdmin )");
        //Get aadCommittee from appraisal
        HttpSession session = FacesUtil.getSession(true);
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        aadAdminName = fullApplicationControl.getAADAdmin(workCaseId, workCasePreScreenId);
        reasonList = fullApplicationControl.getReasonList(ReasonTypeValue.RETURN_REASON);
        reasonId = 0;
        returnRemark = "";
        //if(!Util.isEmpty(aadAdminName)){
        RequestContext.getCurrentInstance().execute("returnAADM_AADCDlg.show()");
        //} else {
        //    messageHeader = "Exception.";
        //    message = "Could not find AAD Admin name.";
        //    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        //}
    }

    public void onSubmitReturnAADAdmin(){
        String wobNumber = "";
        String queueName = "";
        boolean complete = false;

        HttpSession session = FacesUtil.getSession(true);
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
    //Maker
    public void onCheckMandateForMaker(){
        log.debug("onCheckMandateForMaker ::: start...");
        HttpSession session = FacesUtil.getSession(true);
        try {
            workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            workCaseId = 0;
        } catch (Exception e) {
            workCasePreScreenId = 0;
        }

        try {
            stepId = (Long)session.getAttribute("stepId");
        } catch (Exception e) {
            stepId = 0;
        }

        checkMandateDocView = null;
        try{
            checkMandateDocView = checkMandateDocControl.getMandateDocViewByMaker(workCasePreScreenId);
            if(!Util.isNull(checkMandateDocView)){
                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
            } else {
                log.debug("-- Find by work case id = {} CheckMandateDocView is {}   ", workCaseId, checkMandateDocView);
                checkMandateDocView = new CheckMandateDocView();
                log.debug("-- CheckMandateDocView[New] created");
            }
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
        }

        log.debug("onCheckMandateForMaker ::: stop...");
    }
    //Checker
    public void onCheckMandateForChecker(){
        log.debug("onCheckMandateForChecker ::: start...");
        HttpSession session = FacesUtil.getSession(true);
        try {
            workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            workCaseId = 0;
        } catch (Exception e) {
            workCasePreScreenId = 0;
        }

        try {
            stepId = (Long)session.getAttribute("stepId");
        } catch (Exception e) {
            stepId = 0;
        }

        String result = null;
        checkMandateDocView = null;
        try{
            checkMandateDocView = checkMandateDocControl.getMandateDocViewByChecker(workCasePreScreenId);
            if(!Util.isNull(checkMandateDocView)){
                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
            } else {
                log.debug("-- Find by work case pre screen id = {} CheckMandateDocView is {} ", workCasePreScreenId, checkMandateDocView);
                checkMandateDocView = new CheckMandateDocView();
                log.debug("-- CheckMandateDocView[New] created");
            }
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : ", e);
            result = e.getMessage();
        }
        log.debug("onCheckMandateForChecker ::: stop...");
    }

    //Full App
    public void onCheckMandateForFullApp(){
        log.debug("onCheckMandateForFullApp ::: start...");
        try{
            callFullApp();
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : ", e);
        }
        log.debug("onCheckMandateForFullApp ::: stop...");
    }
    //AAD Admin
    public void onCheckMandateForAADAdmin(){
        log.debug("onCheckMandateForAADAdmin ::: start...");
        try{
            callFullApp();
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : ", e);
        }
        log.debug("onCheckMandateForFullApp ::: stop...");
    }
    //AAD Committee
    public void onCheckMandateForAADCommittee(){
        log.debug("onCheckMandateForAADCommittee ::: start...");
        try{
            callFullApp();
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : ", e);
        }
        log.debug("onCheckMandateForFullApp ::: stop...");
    }

    private void callFullApp() throws Exception{
        HttpSession session = FacesUtil.getSession(true);
        try {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCasePreScreenId = 0;
        } catch (Exception e) {
            workCaseId = 0;
        }

        String result = null;
        checkMandateDocView = null;
        try{
            checkMandateDocView = checkMandateDocControl.getMandateDocViewByFullApp(workCaseId);
            if(!Util.isNull(checkMandateDocView)){
                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
            } else {
                log.debug("-- Find by work case id = {} CheckMandateDocView is {}   ", workCaseId, checkMandateDocView);
                checkMandateDocView = new CheckMandateDocView();
                log.debug("-- CheckMandateDocView[New] created");
            }
            log.debug("stop...");
        } catch (Exception e) {
            log.error("-- Exception : ", e);
            throw e;
        }
    }


    public void onSaveCheckMandateDoc(){
        log.debug("-- onSaveCheckMandateDoc().");
        try {
            if(!Util.isZero(workCaseId)){
                checkMandateDocControl.onSaveMandateDoc(checkMandateDocView, workCaseId, 0);
            } else {
                checkMandateDocControl.onSaveMandateDoc(checkMandateDocView, 0, workCasePreScreenId);
            }
            messageHeader = "Information";
            message = "Success";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        } catch (Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = "Exception";
            message = "Failed" + Util.getMessageException(ex);
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

    public void onSubmitReturnBDM(){ //Submit return from UW1 to BDM
        log.debug("onSubmitReturnBDM ::: returnInfoViewList size : {}", returnInfoViewList);
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
            log.debug("onSubmitReturnBDM ::: Return to BDM failed, have no reason to return.");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    //-SUBMIT CASE FROM ABDM
    public void onSubmitToBDM(){
        log.debug("onSubmitBDM");
        HttpSession session = FacesUtil.getSession(true);
        String queueName = Util.parseString(session.getAttribute("queueName"), "");
        String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

        try{
            fullApplicationControl.submitToBDM(queueName, wobNumber);

            messageHeader = "Information.";
            message = "Submit case success.";

            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
        } catch (Exception ex) {
            log.error("Exception while submit to BDM ( from ABDM ), ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);

            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }

    }

    public void onSubmitReturnUW1(){ //Submit Reply From BDM to UW1
        log.debug("onSubmitReturnUW1");

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

                log.error("onSubmitReturnUW1 ::: fail.");
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

            log.error("onSubmitReturnUW1 ::: exception occurred : ", ex);
        }
    }

    public void onRestartCase(){ //UW Restart case
        log.debug("onRestartCase");

        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

            fullApplicationControl.restartCase(queueName,wobNumber);

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
            HttpSession session = FacesUtil.getSession(true);
            String queueName = Util.parseString(session.getAttribute("queueName"), "");
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

            messageHeader = "Information.";
            message = "Return to AAD Admin success.";

            fullApplicationControl.returnAADAdminByBDM(queueName, wobNumber);

            RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
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
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            String queueName = session.getAttribute("queueName").toString();
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");

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

    //-------------- Function for Appraisal Request ( BDM ) -------------------//
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

        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);

        log.debug("onSubmitRequestAppraisal ::: workCaseId : {}, workCasePreScreenId : {}", session.getAttribute("workCaseId"), session.getAttribute("workCasePreScreenId"));

        if(!headerControl.getRequestAppraisalFlag(workCaseId, workCasePreScreenId)){
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
        } else {
            messageHeader = "Information.";
            message = "This case already Request Appraisal. Please contact to AAD Admin";

            context.execute("msgBoxBaseMessageDlg.show()");
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
    //-------------- End of Function for Appraisal Request ( BDM ) ------------------//

    public void onCheckCriteria(){
        long workCaseId = 0;
        HttpSession session = FacesUtil.getSession(true);
        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            try{
                UWRuleResponseView uwRuleResponseView = brmsControl.getFullApplicationResult(workCaseId);
                log.info("onCheckCriteria uwRulesResponse : {}", uwRuleResponseView);
                if(uwRuleResponseView != null){
                    if(uwRuleResponseView.getActionResult().equals(ActionResult.SUCCESS)){
                        UWRuleResultSummaryView uwRuleResultSummaryView = null;
                        try{
                            uwRuleResultSummaryView = uwRuleResponseView.getUwRuleResultSummaryView();
                            uwRuleResultSummaryView.setWorkCaseId(workCaseId);
                            uwRuleResultControl.saveNewUWRuleResult(uwRuleResultSummaryView);
                        }catch (Exception ex){
                            log.error("Cannot Save UWRuleResultSummary {}", uwRuleResultSummaryView);
                            messageHeader = "Exception.";
                            message = Util.getMessageException(ex);
                            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                        }
                        messageHeader = "Information.";
                        message = "Request for Check Criteria Success.";
                        RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                    }else {
                        messageHeader = "Exception.";
                        message = uwRuleResponseView.getReason();
                        RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                    }
                } else {
                    uwRuleResultControl.saveNewUWRuleResult(uwRuleResponseView.getUwRuleResultSummaryView());
                    messageHeader = "Exception.";
                    message = "Request for Check Criteria Fail.";
                    RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
                }
            } catch (Exception ex){
                log.error("Exception while onCheckCriteria : ", ex);
                messageHeader = "Exception.";
                message = Util.getMessageException(ex);
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
            }

        }
    }

    public boolean checkAccessStage(String stageString){
        boolean accessible = false;
        HttpSession session = FacesUtil.getSession(true);
        long stageId = Util.parseLong(session.getAttribute("stageId"), 0);
        if("PRESCREEN".equalsIgnoreCase(stageString)){
            if(stageId == 101){
                accessible = true;
            }
        } else if ("FULLAPP".equalsIgnoreCase(stageString)){
            if(stageId == 201 || stageId == 202 || stageId == 204 || stageId == 206 || stageId == 207){
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
        }

        return accessible;
    }

    public void onGoToInbox(){
        FacesUtil.redirect("/site/inbox.jsf");
    }

    private void showMessageRedirect(){
        RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
    }

    private void showMessageBox(){
        RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
    }

    private void sendCallBackParam(boolean value){
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", value);
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
}
