package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BRMSControl;
import com.clevel.selos.businesscontrol.FullApplicationControl;
import com.clevel.selos.businesscontrol.ReturnControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.BRMSInterfaceImpl;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageButton;
import com.clevel.selos.model.PricingDOAValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.ReasonType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.security.UserDetail;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "baseController")
public class BaseController implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    UserDAO userDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    ReasonDAO reasonDAO;

    @Inject
    FullApplicationControl fullApplicationControl;
    @Inject
    ReturnControl returnControl;
    @Inject
    ReturnInfoTransform returnInfoTransform;

    @Inject
    BRMSControl brmsControl;

    private ManageButton manageButton;
    private AppHeaderView appHeaderView;
    private long stepId;
    private int requestAppraisal;
    private int qualitativeType;
    private int pricingDOALevel;
    private List<User> abdmUserList;
    private List<User> zmUserList;
    private List<User> rmUserList;
    private List<User> ghUserList;

    private User user;
    //private User abdm;

    private String aadCommitteeId;

    private String abdmUserId;
    private String assignRemark;

    private String zmEndorseUserId;
    private String zmEndorseRemark;

    private String zmPriceUserId;
    private String rmPriceUserId;
    private String ghPriceUserId;

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

    private String messageHeader;
    private String message;

    public BaseController() {
    }

    @PostConstruct
    public void onCreation() {
        log.info("BaseController ::: Creation ");
        manageButton = new ManageButton();
        HttpSession session = FacesUtil.getSession(true);
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        stepId = 0;
        requestAppraisal = 0;

        if (!Util.isNull(session.getAttribute("workCasePreScreenId"))) {
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
        }
        if (!Util.isNull(session.getAttribute("stepId"))) {
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }
        if (!Util.isNull(session.getAttribute("requestAppraisal"))){
            requestAppraisal = Integer.valueOf(session.getAttribute("requestAppraisal").toString());
            if((stepId == StepValue.REQUEST_APPRAISAL.value() || stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()) && requestAppraisal == 2){
                requestAppraisal = 1;
            }
        }
        log.info("BaseController ::: getSession : workcase = {}, stepid = {}", workCasePreScreenId, stepId);

        if (stepId == StepValue.PRESCREEN_INITIAL.value()) {
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
        }

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
        if(!Util.isNull(pricingDOA)){
            pricingDOALevel = pricingDOA.value();
            zmEndorseUserId = "";
            zmEndorseRemark = "";

            zmUserList = fullApplicationControl.getZMUserList();

            if(pricingDOA.value() >= PricingDOAValue.RGM_DOA.value()){
                rmPriceUserId = "";
                rmUserList = fullApplicationControl.getRMUserList();
            }

            if(pricingDOA.value() >= PricingDOAValue.GH_DOA.value()){
                ghPriceUserId = "";
                ghUserList = fullApplicationControl.getHeadUserList();
            }

            log.debug("onOpenSubmitZM ::: zmUserList size : {}", zmUserList.size());
            RequestContext.getCurrentInstance().execute("submitZMDialog.show()");
        } else {
            messageHeader = "Exception.";
            message = "Can not find Pricing DOA Level. Please check value for calculate DOA Level";
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onSubmitZM(){
        log.debug("onSubmitZM ::: starting...");
        boolean complete = false;
        if(zmEndorseUserId != null && !zmEndorseUserId.equals("")){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                //fullApplicationControl.submitToZM(zmEndorseUserId, queueName, workCaseId);
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
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onCancelCA(){

    }

    public void onSubmitCA(){

    }

    public void onRequestAppraisal(){
        log.debug("onRequestAppraisal ( bdm input data for aad admin )");
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        try{
            HttpSession session = FacesUtil.getSession(true);
            if(session.getAttribute("workCaseId") != null){
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            }

            if(session.getAttribute("workCasePreScreenId") != null){
                workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            }

            fullApplicationControl.requestAppraisalBDM(workCasePreScreenId, workCaseId);
            FacesUtil.redirect("/site/appraisalRequest.jsf");

        } catch (Exception ex){
            log.error("exception while request appraisal : ", ex);
            messageHeader = "Exception.";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
        }
    }

    public void onSubmitAppraisalAdmin(){
        log.debug("onRequestAppraisal ( submit to aad admin )");
        long workCasePreScreenId = 0;
        long workCaseId = 0;
        try{
            HttpSession session = FacesUtil.getSession(true);
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());

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
    }

    public void onSubmitAppraisalCommittee(){
        log.debug("onSubmitAppraisalCommittee ( submit to aad committee )");
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

    public void onOpenReturnBDMDialog(){
        log.debug("onOpenReturnBDM ::: starting...");
        HttpSession session = FacesUtil.getSession(true);
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

        //TODO: get return list from CheckMandate Doc

        //get from not accept List
        returnInfoViewList = returnControl.getNoAcceptReturnInfoViewList(workCaseId);

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

    public void onSumbitReturnSummary(){
        log.debug("onSumbitReturnSummary ::: returnInfoViewList size : {}", returnInfoViewList);
        boolean complete = false;
        if(returnInfoViewList!=null && returnInfoViewList.size()>0){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                User user = (User) session.getAttribute("user");
                long stepId = Long.parseLong(session.getAttribute("stepId").toString());

                returnControl.submitReturnSummary(workCaseId, queueName, user, stepId, returnInfoViewList);
                messageHeader = "Information.";
                message = "Return to BDM success.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
                complete = true;
                log.debug("onReturnBDMSubmit ::: success.");
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

    public void onSumbitReplyReturn(){
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

                log.error("onReturnBDMSubmit ::: fail.");
            } else {
                returnControl.updateReplyReturnDate(workCaseId);

                //TODO: execute bpm workflow for reply return

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

    public void onSubmitReviewReturn(){
        log.debug("onSubmitReviewReturn begin");
        try{
            HttpSession session = FacesUtil.getSession(true);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            User user = (User) session.getAttribute("user");

            List<ReturnInfoView> returnInfoViews = returnControl.getReturnNoReviewList(workCaseId);

            if(returnInfoViews!=null && returnInfoViews.size()>0){
                messageHeader = "Information.";
                message = "Submit Review fail. Please check return information again.";
                RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

                log.error("onSubmitReviewReturn ::: fail.");
            } else {
                returnControl.saveReturnHistory(workCaseId,user);

                //TODO: execute bpm workflow for review return

                messageHeader = "Information.";
                message = "Submit Return success";
                RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");

                log.debug("onSubmitReviewReturn ::: success.");
            }
        } catch (Exception ex){
            messageHeader = "Information.";
            message = "Submit Review fail, cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");

            log.error("onSubmitReviewReturn ::: exception occurred : ", ex);
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

    public String getZmPriceUserId() {
        return zmPriceUserId;
    }

    public void setZmPriceUserId(String zmPriceUserId) {
        this.zmPriceUserId = zmPriceUserId;
    }

    public String getRmPriceUserId() {
        return rmPriceUserId;
    }

    public void setRmPriceUserId(String rmPriceUserId) {
        this.rmPriceUserId = rmPriceUserId;
    }

    public String getGhPriceUserId() {
        return ghPriceUserId;
    }

    public void setGhPriceUserId(String ghPriceUserId) {
        this.ghPriceUserId = ghPriceUserId;
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

    public List<User> getRmUserList() {
        return rmUserList;
    }

    public void setRmUserList(List<User> rmUserList) {
        this.rmUserList = rmUserList;
    }

    public List<User> getGhUserList() {
        return ghUserList;
    }

    public void setGhUserList(List<User> ghUserList) {
        this.ghUserList = ghUserList;
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
}
