package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalRequestControl;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "appraisalRequest")
public class AppraisalRequest implements Serializable {

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


    private String modeForButton;
    private int rowIndex;
    private String messageHeader;
    private String message;

    private User user;
    private Long workCaseId;
    private AppraisalView appraisalView;

    private AppraisalDetailView appraisalDetailView;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private AppraisalDetailView selectAppraisalDetailView;

    private AppraisalContactDetailView appraisalContactDetailView;
    private AppraisalContactDetailView selectAppraisalContactDetailView;

    private boolean titleDeedFlag;
    private boolean purposeFlag;
    private boolean numberOfDocumentsFlag;
    private boolean contactFlag;

    public AppraisalRequest() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        modeForButton = "add";

        HttpSession session = FacesUtil.getSession(true);
        //session.setAttribute("workCaseId", 10001);
        user = (User)session.getAttribute("user");
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
            appraisalView = appraisalRequestControl.getAppraisalRequestByWorkCase(workCaseId);
            if(appraisalView != null){
                appraisalDetailViewList = appraisalView.getAppraisalDetailViewList();
                log.info("appraisalDetailViewList  :::::::::::: {} ", appraisalDetailViewList);
                if(appraisalDetailViewList == null){
                    appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
                }

                setStringOnAppraisalTable();
            }else{
                appraisalView = new AppraisalView();
            }
        } else {
            appraisalView = new AppraisalView();
        }

        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        contactFlag = false;
    }

    private void setStringOnAppraisalTable(){
        AppraisalDetailView appraisalDetailViewS;
        for(int i=0;i<appraisalDetailViewList.size();i++){

            appraisalDetailViewS =  appraisalDetailViewList.get(i);

            if(appraisalDetailViewS.getPurposeReviewAppraisal()==1){
                appraisalDetailViewS.setPurposeReviewAppraisalLabel(msg.get("app.appraisal.appraisalDetail.label.purposeReviewAppraisal"));
            }

            if(appraisalDetailViewS.getPurposeNewAppraisal()==1){
                appraisalDetailViewS.setPurposeNewAppraisalLabel(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal"));
            }

            if(appraisalDetailViewS.getPurposeReviewBuilding()==1){
                appraisalDetailViewS.setPurposeReviewBuildingLabel(msg.get("app.appraisal.appraisalDetail.label.purposeReviewBuilding"));
            }

            if(appraisalDetailViewS.getCharacteristic()==0){
                appraisalDetailViewS.setCharacteristicLabel(msg.get("app.appraisal.appraisalDetail.label.noBuilding"));
            }else if(appraisalDetailViewS.getCharacteristic()==1){
                appraisalDetailViewS.setCharacteristicLabel(msg.get("app.appraisal.appraisalDetail.label.haveBuilding"));
            }
            /// Transform value to boolean
              appraisalDetailViewS.setPurposeNewAppraisalB(isTrue(appraisalDetailViewS.getPurposeNewAppraisal()));
              appraisalDetailViewS.setPurposeReviewBuildingB(isTrue(appraisalDetailViewS.getPurposeReviewBuilding()));
              appraisalDetailViewS.setPurposeReviewAppraisalB(isTrue(appraisalDetailViewS.getPurposeReviewAppraisal()));
        }
    }

    public void onSaveAppraisalDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if(appraisalDetailViewMandate()){
            complete = true;
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveAppraisalDetailView add >>> begin ");
                log.info("appraisalDetailViewList size >>> is " + appraisalDetailViewList.size());
                appraisalDetailView.setNo(appraisalDetailViewList.size()+1);
                appraisalDetailView.setPurposeReviewAppraisal(isCheck(appraisalDetailView.isPurposeReviewAppraisalB()));
                appraisalDetailView.setPurposeNewAppraisal(isCheck(appraisalDetailView.isPurposeNewAppraisalB()));
                appraisalDetailView.setPurposeReviewBuilding(isCheck(appraisalDetailView.isPurposeReviewBuildingB()));
                log.info("onSaveContactRecordDetailView contactRecordDetailView >>> " + appraisalDetailView);
                appraisalDetailViewList.add(appraisalDetailView);
                log.info("onSaveContactRecordDetailView add >>> end ");
            }else if(modeForButton.equalsIgnoreCase("edit")){
                log.info("onSaveAppraisalDetailView edit >>> begin ");
                AppraisalDetailView appraisalDetailViewRow;
                appraisalDetailViewRow = appraisalDetailViewList.get(rowIndex);
                appraisalDetailViewRow.setTitleDeed(appraisalDetailView.getTitleDeed());
                appraisalDetailViewRow.setPurposeReviewAppraisalB(appraisalDetailView.isPurposeReviewAppraisalB());
                appraisalDetailViewRow.setPurposeNewAppraisalB(appraisalDetailView.isPurposeNewAppraisalB());
                appraisalDetailViewRow.setPurposeReviewBuildingB(appraisalDetailView.isPurposeReviewBuildingB());
                appraisalDetailViewRow.setPurposeReviewAppraisal(isCheck(appraisalDetailView.isPurposeReviewAppraisalB()));
                appraisalDetailViewRow.setPurposeNewAppraisal(isCheck(appraisalDetailView.isPurposeNewAppraisalB()));
                appraisalDetailViewRow.setPurposeReviewBuilding(isCheck(appraisalDetailView.isPurposeReviewBuildingB()));
                appraisalDetailViewRow.setCharacteristic(appraisalDetailView.getCharacteristic());
                appraisalDetailViewRow.setNumberOfDocuments(appraisalDetailView.getNumberOfDocuments());
                appraisalDetailView = new AppraisalDetailView();
                log.info("onSaveContactRecordDetailView edit >>> end ");
            }
            setStringOnAppraisalTable();
            context.addCallbackParam("functionComplete", complete);
        }else {
            setStringOnAppraisalTable();
            context.addCallbackParam("functionComplete", complete);
        }

    }
    
    public void onEditAppraisalDetailView(){
        log.info( " onEditAppraisalDetailView " + selectAppraisalDetailView);
        modeForButton = "edit";
        appraisalDetailView = new AppraisalDetailView();
        //*** Check list size ***//
        if( rowIndex < appraisalDetailViewList.size() ) {
            appraisalDetailView.setTitleDeed(selectAppraisalDetailView.getTitleDeed());
            appraisalDetailView.setPurposeReviewAppraisalB(selectAppraisalDetailView.isPurposeReviewAppraisalB());
            appraisalDetailView.setPurposeNewAppraisalB(selectAppraisalDetailView.isPurposeNewAppraisalB());
            appraisalDetailView.setPurposeReviewBuildingB(selectAppraisalDetailView.isPurposeReviewBuildingB());
            appraisalDetailView.setPurposeReviewAppraisal(isCheck(selectAppraisalDetailView.isPurposeReviewAppraisalB()));
            appraisalDetailView.setPurposeNewAppraisal(isCheck(selectAppraisalDetailView.isPurposeNewAppraisalB()));
            appraisalDetailView.setPurposeReviewBuilding(isCheck(selectAppraisalDetailView.isPurposeReviewBuildingB()));
            appraisalDetailView.setCharacteristic(selectAppraisalDetailView.getCharacteristic());
            appraisalDetailView.setNumberOfDocuments(selectAppraisalDetailView.getNumberOfDocuments());
        }
    }

    public void onAddAppraisalDetailView(){
        log.info("onAddAppraisalDetailView >>> begin ");
        appraisalDetailView = new AppraisalDetailView();
        modeForButton = "add";
    }

    public void onAddAppraisalContactDetailView(){
        log.info("onAddAppraisalContactDetailView >>> begin ");
        appraisalContactDetailView = new AppraisalContactDetailView();
        modeForButton = "add";
    }


    public void onDeleteAppraisalDetailView() {
        log.info( " onDeleteAppraisalDetailView " + selectAppraisalDetailView);
        appraisalDetailViewList.remove(selectAppraisalDetailView);
        onSetRowNoAppraisalDetailView();
        log.info( " onDeleteAppraisalDetailView end ");
    }

    public void onSetRowNoAppraisalDetailView(){
        AppraisalDetailView appraisalDetailViewRow;
        for(int i=0;i< appraisalDetailViewList.size();i++){
            appraisalDetailViewRow = appraisalDetailViewList.get(i);
            appraisalDetailViewRow.setNo(i+1);
        }
    }


    public void onSaveAppraisalRequest() {
        log.info("onSaveAppraisalRequest::::");
        log.info("appraisalDetailViewList.size() ::: {} ", appraisalDetailViewList.size());

        if(appraisalContactDetailViewMandate()){
            try{
                if(appraisalView.getId() == 0){
                    appraisalView.setCreateBy(user);
                    appraisalView.setCreateDate(DateTime.now().toDate());
                }
                appraisalView.setModifyBy(user);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);

                appraisalRequestControl.onSaveAppraisalRequest(appraisalView, workCaseId, user);
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
            message = msg.get("app.appraisal.request.message.body.save.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelAppraisalRequest(){
        log.info("onCancelCustomerAcceptance::::  ");
        onCreation();
    }

    private boolean appraisalDetailViewMandate(){
        log.debug("-- appraisalDetailViewMandate()");
        boolean result = true;
        if(appraisalDetailView.getTitleDeed().length() == 0){
            titleDeedFlag = true;
            result = false;
        } else {
            titleDeedFlag = false;
        }
        if(!appraisalDetailView.isPurposeNewAppraisalB() && !appraisalDetailView.isPurposeReviewAppraisalB() && !appraisalDetailView.isPurposeReviewBuildingB()){
            purposeFlag = true;
            result = false;
        } else {
            purposeFlag = false;
        }
        if(appraisalDetailView.getCharacteristic() == 1 && appraisalDetailView.getNumberOfDocuments() == BigDecimal.ZERO){
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
    private boolean appraisalContactDetailViewMandate(){
        log.debug("-- AppraisalContactDetailViewMandate()");
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

    public AppraisalDetailView getSelectAppraisalDetailView() {
        return selectAppraisalDetailView;
    }

    public void setSelectAppraisalDetailView(AppraisalDetailView selectAppraisalDetailView) {
        this.selectAppraisalDetailView = selectAppraisalDetailView;
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
