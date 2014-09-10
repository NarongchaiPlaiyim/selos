package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalRequestControl;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "appraisalRequest")
public class AppraisalRequest extends BaseController {

    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private UserDAO userDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private AppraisalRequestControl appraisalRequestControl;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject private WorkCasePrescreenDAO workCasePreScreenDAO;

    private enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;
    private String messageHeader;
    private String message;

    //private User user;
    private long workCaseId;
    private long workCasePreScreenId;
    private long stepId;
    private AppraisalView appraisalView;

    private AppraisalDetailView appraisalDetailView;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private AppraisalDetailView appraisalDetailViewSelected;
    private AppraisalDetailView appraisalDetailViewDialog;

    private AppraisalContactDetailView appraisalContactDetailView;
    private AppraisalContactDetailView selectAppraisalContactDetailView;

    private boolean titleDeedFlag;
    private boolean purposeFlag;
    private boolean numberOfDocumentsFlag;
    private boolean contactFlag;
    private boolean contactFlag2;
    private boolean contactFlag3;

    public AppraisalRequest() {

    }

    private void init(){
        log.debug("init...");
        modeForButton = ModeForButton.ADD;
        appraisalDetailView = new AppraisalDetailView();
        appraisalDetailViewDialog = new AppraisalDetailView();
        appraisalDetailViewSelected = new AppraisalDetailView();
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        contactFlag = false;
        contactFlag2 = false;
        contactFlag3 = false;
    }

    public void preRender(){
        log.debug("preRender...");
        HttpSession session = FacesUtil.getSession(false);
        if(checkSession(session)){
            /*stepId = getCurrentStep(session);
            if(stepId != StepValue.REQUEST_APPRAISAL_RETURN.value() || stepId != StepValue.REQUEST_APPRAISAL_BDM.value()) {
                log.debug("preRender ::: Invalid step id : {}", stepId);
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }*/
        } else {
            log.debug("preRender ::: workCasePreScreenId, workCaseId, stepId is null.");
            FacesUtil.redirect("/site/inbox.jsf");
            return;
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation...");
        init();
        HttpSession session = FacesUtil.getSession(false);
        String bdmUserId = "";
        if(checkSession(session)){
            stepId = (Long)session.getAttribute("stepId");
            workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);

            if (!Util.isZero(workCaseId)){
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                bdmUserId = workCase.getCreateBy().getId();
            } else {
                WorkCasePrescreen workCasePrescreen = workCasePreScreenDAO.findById(workCasePreScreenId);
                bdmUserId = workCasePrescreen.getCreateBy().getId();
            }

            log.debug("onCreation ::: workCasePreScreenId : [{}], workCaseId : [{}]", workCasePreScreenId, workCaseId);

            appraisalView = appraisalRequestControl.getAppraisalRequest(workCaseId, workCasePreScreenId);
            log.debug("onCreation ::: appraisalView : {}", appraisalView);
            if (!Util.isEmpty(bdmUserId)){
                appraisalView.setZoneLocation(appraisalRequestControl.getZoneLocation(bdmUserId));
            }

            if(!Util.isNull(appraisalView)){
                log.debug("onCreation ::: appraisalView.id : [{}]", appraisalView.getId());
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(Util.safetyList(appraisalView.getAppraisalDetailViewList()));

                if(Util.isZero(appraisalDetailViewList.size())){
                    appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
                }

                appraisalContactDetailView = appraisalView.getAppraisalContactDetailView();
                if(Util.isNull(appraisalContactDetailView)){
                    appraisalContactDetailView = new AppraisalContactDetailView();
                }
                log.debug("onCreation ::: appraisalContactDetailView.id : [{}]", appraisalContactDetailView.getId());
            } else {
                appraisalView = new AppraisalView();
                if (!Util.isEmpty(bdmUserId)){
                    appraisalView.setZoneLocation(appraisalRequestControl.getZoneLocation(bdmUserId));
                }
                log.debug("-- AppraisalView[New] created");
                appraisalContactDetailView = new AppraisalContactDetailView();
                log.debug("-- AppraisalContactDetailView[New] created");
                appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
                log.debug("-- AppraisalDetailViewList[New] created");
            }
        } else {
            //TODO Show dialog for exception cannot load data from database.
        }
    }

    public void onSaveAppraisalDetailView(){
        log.debug("-- onSaveAppraisalDetailView() flag = {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if(appraisalDetailViewMandate()){
            complete = true;
            if(ModeForButton.ADD == modeForButton){
                appraisalDetailViewList.add(appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }else if(ModeForButton.EDIT == modeForButton){
                log.debug("-- RowIndex[{}]", rowIndex);
                appraisalDetailViewList.set(rowIndex, appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }
            context.addCallbackParam("functionComplete", complete);
        }else {
            context.addCallbackParam("functionComplete", complete);
        }
    }
    
    public void onEditAppraisalDetailView(){
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalDetailView() RowIndex[{}]", rowIndex);
        Cloner cloner = new Cloner();
        appraisalDetailViewDialog = cloner.deepClone(appraisalDetailViewSelected);
    }



    public void onAddAppraisalDetailView(){
        log.info("-- onAddAppraisalDetailView() ModeForButton[ADD]");
        appraisalDetailViewDialog = new AppraisalDetailView();
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onDeleteAppraisalDetailView() {
        log.info( "-- onDeleteAppraisalDetailView RowIndex[{}]", rowIndex);
        appraisalDetailViewList.remove(rowIndex);
        log.info( "-- AppraisalDetailViewList[{}] deleted", rowIndex);
    }

    public void onSaveAppraisalRequest() {
        log.info("-- onSaveAppraisalRequest::::");

        if(appraisalDetailViewListMandate()){
            if(appraisalContactDetailViewMandate()){
                try{
                    appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
                    appraisalView.setAppraisalContactDetailView(appraisalContactDetailView);
                    appraisalRequestControl.onSaveAppraisalRequest(appraisalView, workCaseId, workCasePreScreenId);

                    messageHeader = msg.get("app.appraisal.request.message.header.save.success");
                    message = msg.get("app.appraisal.request.message.body.save.success");
                    onCreation();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                } catch(Exception ex){
                    log.error("Exception : {}", ex);
                    messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
                    if(ex.getCause() != null){
                        message = msg.get("app.appraisal.request.message.body.save.fail") + " cause : "+ ex.getCause().toString();
                    } else {
                        message = msg.get("app.appraisal.request.message.body.save.fail") + ex.getMessage();
                    }
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else {
                messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
                message = "Please fill in all required information(Contact Detail).";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else {
            messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
            message = "Please fill in all required information(Estimate Detail).";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onCancelAppraisalRequest(){
        log.info("onCancelCustomerAcceptance::::  ");
        onCreation();
    }


    public boolean appraisalDetailViewMandate(){
        log.debug("-- appraisalDetailViewMandate() ::: appraisalDetailViewDialog : {}", appraisalDetailViewDialog);
        boolean result = true;
        if(!Util.isNull(appraisalDetailViewDialog.getTitleDeed())){
            if(Util.isZero(appraisalDetailViewDialog.getTitleDeed().length())){
                titleDeedFlag = true;
                result = false;
            } else {
                titleDeedFlag = false;
            }
        } else {
            titleDeedFlag = true;
            result = false;
        }
        if(!appraisalDetailViewDialog.isPurposeNewAppraisalB() && !appraisalDetailViewDialog.isPurposeReviewAppraisalB() && !appraisalDetailViewDialog.isPurposeReviewBuildingB()){
            purposeFlag = true;
            result = false;
        } else {
            purposeFlag = false;
        }
        if(appraisalDetailViewDialog.getCharacteristic() == 1 && appraisalDetailViewDialog.getNumberOfDocuments() == 0){
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
    public boolean appraisalContactDetailViewMandate(){
        log.debug("-- appraisalContactDetailViewMandate()");
        //todo :  2 0 21
        boolean result = true;

        if(appraisalContactDetailView.getCustomerName1().length() == 0 || appraisalContactDetailView.getContactNo1().length() == 0 ){
            contactFlag = true;
            result = false;
        } else {
            contactFlag = false;
        }

        log.debug("-- contactFlag = {}", contactFlag);
        log.debug("-- result = {}", result);
        return result;
    }


    private boolean appraisalDetailViewListMandate(){
        log.debug("-- appraisalDetailViewListMandate()");
        boolean result = true;
        if(Util.isZero(appraisalDetailViewList.size())){
            result = false;
        }
        log.debug("-- result = {}", result);
        return result;
    }


    public boolean isContactFlag() {
        return contactFlag;
    }

    public void setContactFlag(boolean contactFlag) {
        this.contactFlag = contactFlag;
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

    public AppraisalContactDetailView getSelectAppraisalContactDetailView() {
        return selectAppraisalContactDetailView;
    }

    public void setSelectAppraisalContactDetailView(AppraisalContactDetailView selectAppraisalContactDetailView) {
        this.selectAppraisalContactDetailView = selectAppraisalContactDetailView;
    }

    public AppraisalContactDetailView getAppraisalContactDetailView() {
        return appraisalContactDetailView;
    }

    public void setAppraisalContactDetailView(AppraisalContactDetailView appraisalContactDetailView) {
        this.appraisalContactDetailView = appraisalContactDetailView;
    }

    public List<AppraisalDetailView> getAppraisalDetailViewList() {
        return appraisalDetailViewList;
    }

    public void setAppraisalDetailViewList(List<AppraisalDetailView> appraisalDetailViewList) {
        this.appraisalDetailViewList = appraisalDetailViewList;
    }

    public AppraisalDetailView getAppraisalDetailViewSelected() {
        return appraisalDetailViewSelected;
    }

    public void setAppraisalDetailViewSelected(AppraisalDetailView appraisalDetailViewSelected) {
        this.appraisalDetailViewSelected = appraisalDetailViewSelected;
    }

    public AppraisalDetailView getAppraisalDetailViewDialog() {
        return appraisalDetailViewDialog;
    }

    public void setAppraisalDetailViewDialog(AppraisalDetailView appraisalDetailViewDialog) {
        this.appraisalDetailViewDialog = appraisalDetailViewDialog;
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

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
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

    public int isCheck(boolean value){
        if(value){
            return 1;
        } else {
            return 0;
        }
    }
    public boolean isTrue(int value){
        if(value==1){
            return true;
        } else {
            return false;
        }
    }
}
