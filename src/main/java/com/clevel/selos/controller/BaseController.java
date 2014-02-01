package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.FullApplicationControl;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageButton;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.security.UserDetail;
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
    FullApplicationControl fullApplicationControl;

    private ManageButton manageButton;
    private AppHeaderView appHeaderView;
    private long stepId;
    private int qualitativeType;
    private List<User> abdmUserList;
    private List<User> zmUserList;

    private User user;
    //private User abdm;

    private String abdmUserId;
    private String assignRemark;

    private String zmEndorseUserId;
    private String zmEndorseRemark;

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

        if (session.getAttribute("workCasePreScreenId") != null) {
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
        }
        if (session.getAttribute("stepId") != null) {
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
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
            manageButton.setCancelCAButton(true);
            manageButton.setCloseSaleButton(true);
            manageButton.setCheckBRMSButton(true);
            manageButton.setCheckMandateDocButton(true);
            manageButton.setRequestAppraisalButton(true);
        } else if (stepId == StepValue.FULLAPP_BDM_SSO_ABDM.value()) {
            manageButton.setViewRelatedCA(true);
            manageButton.setRequestAppraisalButton(true);
            manageButton.setCheckMandateDocButton(true);
            manageButton.setCheckCriteriaButton(true);
            manageButton.setAssignToABDMButton(true);
//            manageButton.setCancelCAButton(true);
            manageButton.setSubmitCAButton(true);
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

    public void onOpenSubmitZM(){
        log.debug("onOpenSubmitZM ::: starting...");
        zmEndorseUserId = "";
        zmEndorseRemark = "";
        zmUserList = fullApplicationControl.getZMUserList();
        log.debug("onOpenSubmitZM ::: zmUserList size : {}", zmUserList.size());
    }

    public void onSubmitZM(){
        log.debug("onSubmitZM ::: starting...");
        boolean complete = false;
        if(zmEndorseUserId != null && !zmEndorseUserId.equals("")){
            try{
                HttpSession session = FacesUtil.getSession(true);
                long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                String queueName = session.getAttribute("queueName").toString();
                fullApplicationControl.submitToZM(zmEndorseUserId, queueName, workCaseId);
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
}
