package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalAppointmentControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.AppraisalDetailTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "appraisalAppointment")
public class AppraisalAppointment implements Serializable {

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
    private AppraisalCompanyDAO appraisalCompanyDAO;
    @Inject
    private AppraisalDivisionDAO appraisalDivisionDAO;
    @Inject
    private LocationPropertyDAO locationPropertyDAO;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private AppraisalAppointmentControl appraisalAppointmentControl;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;

    private enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;
    private String messageHeader;
    private String message;

    private long workCaseId;
    private long workCasePreScreenId;
    private long stepId;
    private AppraisalView appraisalView;

    private boolean showNoRequest;
    private Date currentDate;
    private String currentDateDDMMYY;



    private AppraisalDetailView appraisalDetailView;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private AppraisalDetailView appraisalDetailViewSelected;
    private AppraisalDetailView appraisalDetailViewDialog;

    private AppraisalContactDetailView appraisalContactDetailView;
    private List<AppraisalContactDetailView> appraisalContactDetailViewList;
    private AppraisalContactDetailView selectAppraisalContactDetailView;

    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private ContactRecordDetailView contactRecordDetailView;

    private ContactRecordDetailView selectContactRecordDetail;
    private ContactRecordDetailView contactRecordDetailViewTemp;
    private List<AppraisalCompany> appraisalCompanyList;
    private List<AppraisalDivision> appraisalDivisionList;
    private List<LocationProperty> locationPropertyList;
    private List<Province> provinceList;


    private AppraisalCompany appraisalCompany;
    private AppraisalDivision appraisalDivision;
    private LocationProperty locationProperty;
    private Province province;

    private boolean titleDeedFlag;
    private boolean purposeFlag;
    private boolean numberOfDocumentsFlag;
    private boolean contactFlag2;
    private boolean contactFlag3;

    public AppraisalAppointment() {

    }

    private void init(){
        log.debug("-- init()");
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        appraisalContactDetailViewList = new ArrayList<AppraisalContactDetailView>();
        contactRecordDetailView = new ContactRecordDetailView();
        contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();

        modeForButton = ModeForButton.ADD;
        appraisalCompanyList = appraisalCompanyDAO.findAll();
        appraisalDivisionList= appraisalDivisionDAO.findAll();
        locationPropertyList= locationPropertyDAO.findAll();
        provinceList= provinceDAO.findAll();
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        contactFlag2 = false;
        contactFlag3 = false;
        appraisalDetailViewDialog = new AppraisalDetailView();
        appraisalDetailViewSelected = new AppraisalDetailView();
    }

    public void preRender(){
        log.info("-- preRender.");
        HttpSession session = FacesUtil.getSession(true);
        log.debug("preRender ::: setSession ");
        if((!Util.isNull(session.getAttribute("workCaseId")) || !Util.isNull(session.getAttribute("workCasePreScreenId")) ) && !Util.isNull(session.getAttribute("stepId"))){
            stepId = Long.valueOf(""+session.getAttribute("stepId"));
            log.debug("-- stepId[{}]", stepId);

            if(stepId != StepValue.REQUEST_APPRAISAL.value()){
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
        } else {
            log.debug("preRender ::: workCaseId is null.");
            FacesUtil.redirect("/site/inbox.jsf");
            return;
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        //preRender();
        HttpSession session = FacesUtil.getSession(true);
        boolean canRender = false;
        if(!Util.isNull(session.getAttribute("workCaseId")) && Long.valueOf(""+session.getAttribute("workCaseId")) != 0){
            workCaseId = Long.valueOf(""+session.getAttribute("workCaseId"));
            canRender = true;
        }else if(!Util.isNull(session.getAttribute("workCasePreScreenId")) && Long.valueOf(""+session.getAttribute("workCasePreScreenId")) != 0){
            workCasePreScreenId = Long.valueOf(""+session.getAttribute("workCasePreScreenId"));
            canRender = true;
        }

        if(canRender){
            init();
            appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId, workCasePreScreenId);
            if(!Util.isNull(appraisalView)){
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(Util.safetyList(appraisalView.getAppraisalDetailViewList()));
                if(Util.isZero(appraisalDetailViewList.size())){
                    appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
                }
                appraisalContactDetailView = appraisalView.getAppraisalContactDetailView();
                if(Util.isNull(appraisalContactDetailView)){
                    appraisalContactDetailView = new AppraisalContactDetailView();
                }

                updateContractFlag(appraisalContactDetailView);
            } else {
                appraisalView = new AppraisalView();
                log.debug("-- AppraisalView[New] created");
                appraisalContactDetailView = new AppraisalContactDetailView();
                log.debug("-- AppraisalContactDetailView[New] created");
                appraisalContactDetailView = new AppraisalContactDetailView();
                log.debug("-- AppraisalContactDetailView[New] created");
            }
        }
    }

    private void updateContractFlag(final AppraisalContactDetailView appraisalContactDetailView){
        log.debug("-- updateContractFlag()");
        if(!Util.isZero(appraisalContactDetailView.getContractId2())){
            contactFlag2 = true;
        }
        if(!Util.isZero(appraisalContactDetailView.getContractId3())){
            contactFlag3 = true;
        }
    }

    public void onClickForDialogNoRequest(){
        log.info("onClickForDialogNoRequest");
        messageHeader = "เกิดข้อผิดพลาด";
        message = "ยังไม่มีการกรอกข้อมูลการร้องขอ Appraisal มาก่อน";
        RequestContext.getCurrentInstance().execute("msgBoxNoRequestMessageDlg.show()");

    }
    public void onChangePageCauseNoRequest(){
        try{
            log.info("onChangePageCauseNoRequest 1");
            String url = "appraisalRequest.jsf";
            log.info("onChangePageCauseNoRequest 2");
            FacesContext fc = FacesContext.getCurrentInstance();
            log.info("onChangePageCauseNoRequest 3");
            ExternalContext ec = fc.getExternalContext();
            log.info("redirect to new page");
            ec.redirect(url);

        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.appointment.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.appraisal.appointment.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.appointment.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }


    private void setStrOnDataTable(){
    	/*
        ContactRecordDetailView contactRecordDetailViewForStr;
        for(int i=0;i<contactRecordDetailViewList.size();i++){

            contactRecordDetailViewForStr =  contactRecordDetailViewList.get(i);
            if(contactRecordDetailViewForStr.getCallingResult()==0){
                contactRecordDetailViewForStr.setCallingResultStr(msg.get("app.contactRecordDetail.radio.label.callingResult.cannotContact"));
            }else if(contactRecordDetailViewForStr.getCallingResult()==1){
                contactRecordDetailViewForStr.setCallingResultStr(msg.get("app.contactRecordDetail.radio.label.callingResult.canContact"));
            }else{
                contactRecordDetailViewForStr.setCallingResultStr(msg.get("app.contactRecordDetail.radio.label.callingResult.etc"));
            }

            if(contactRecordDetailViewForStr.getAcceptResult()==0){
                contactRecordDetailViewForStr.setAcceptResultStr(msg.get("app.contactRecordDetail.radio.label.acceptResult.notAccept"));
            }else if(contactRecordDetailViewForStr.getAcceptResult()==1){
                contactRecordDetailViewForStr.setAcceptResultStr(msg.get("app.contactRecordDetail.radio.label.acceptResult.accept"));
            }else{
                contactRecordDetailViewForStr.setAcceptResultStr(msg.get("app.contactRecordDetail.radio.label.acceptResult.etc"));
            }

//            if(contactRecordDetailViewForStr.getReason()==0){
//                contactRecordDetailViewForStr.setReasonStr(msg.get("app.contactRecordDetail.radio.label.reason.accept"));
//            }else if(contactRecordDetailViewForStr.getReason()==1){
//                contactRecordDetailViewForStr.setReasonStr(msg.get("app.contactRecordDetail.radio.label.reason.notAccept"));
//            }else if(contactRecordDetailViewForStr.getReason()==2){
//                contactRecordDetailViewForStr.setReasonStr(msg.get("app.contactRecordDetail.radio.label.reason.needToConsider"));
//            }else{
//                contactRecordDetailViewForStr.setReasonStr(msg.get("app.contactRecordDetail.radio.label.reason.etc"));
//            }
        }
        */
    }

    public void onSaveContactRecordDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
/*
        if(true){
            complete = true;
            if(ModeForButton.ADD.equals(modeForButton)){
                log.info("onSaveContactRecordDetailView add >>> begin ");
                log.info("contactRecordDetailViewList size >>> is " + contactRecordDetailViewList.size());

                contactRecordDetailView.setNo(contactRecordDetailViewList.size()+1);
                log.info("onSaveContactRecordDetailView contactRecordDetailView >>> " + contactRecordDetailView);

                contactRecordDetailViewList.add(contactRecordDetailView);

                log.info("onSaveContactRecordDetailView add >>> end ");

            }else if(ModeForButton.EDIT.equals(modeForButton)){
                log.info("onSaveContactRecordDetailView edit >>> begin ");
                ContactRecordDetailView contactRecordDetailViewRow;
                contactRecordDetailViewRow = contactRecordDetailViewList.get(rowIndex);

                contactRecordDetailViewRow.setCallingDate(contactRecordDetailView.getCallingDate());
                contactRecordDetailViewRow.setCallingTime(contactRecordDetailView.getCallingTime());
                contactRecordDetailViewRow.setCallingResult(contactRecordDetailView.getCallingResult());
                contactRecordDetailViewRow.setAcceptResult(contactRecordDetailView.getAcceptResult());
                contactRecordDetailViewRow.setNextCallingDate(contactRecordDetailView.getNextCallingDate());
                contactRecordDetailViewRow.setNextCallingTime(contactRecordDetailView.getNextCallingTime());


                contactRecordDetailViewRow.setReason(contactRecordDetailView.getReason());
                contactRecordDetailViewRow.setRemark(contactRecordDetailView.getRemark());

                contactRecordDetailView = new ContactRecordDetailView();
                log.info("onSaveContactRecordDetailView edit >>> end ");
            }
        }
        setStrOnDataTable();
        */
        context.addCallbackParam("functionComplete", complete);
    }

    public void onEditContactRecordDetailView(){
        log.info( " onEditContactRecordDetailView " + selectContactRecordDetail.getRemark());
        modeForButton = ModeForButton.EDIT;
        contactRecordDetailView = new ContactRecordDetailView();
       
        //*** Check list size ***//
        /*
        if( rowIndex < contactRecordDetailViewList.size() ) {
            contactRecordDetailView.setCallingDate(selectContactRecordDetail.getCallingDate());
            contactRecordDetailView.setCallingTime(selectContactRecordDetail.getCallingTime());
            contactRecordDetailView.setCallingResult(selectContactRecordDetail.getCallingResult());
            contactRecordDetailView.setAcceptResult(selectContactRecordDetail.getAcceptResult());
            contactRecordDetailView.setNextCallingDate(selectContactRecordDetail.getNextCallingDate());
            contactRecordDetailView.setNextCallingTime(selectContactRecordDetail.getNextCallingTime());
            contactRecordDetailView.setReason(selectContactRecordDetail.getReason());
            contactRecordDetailView.setRemark(selectContactRecordDetail.getRemark());
        }
        contactRecordDetailViewTemp = new ContactRecordDetailView();
        contactRecordDetailViewTemp.setCallingDate(selectContactRecordDetail.getCallingDate());
        contactRecordDetailViewTemp.setCallingTime(selectContactRecordDetail.getCallingTime());
        contactRecordDetailViewTemp.setCallingResult(selectContactRecordDetail.getCallingResult());
        contactRecordDetailViewTemp.setAcceptResult(selectContactRecordDetail.getAcceptResult());
        contactRecordDetailViewTemp.setNextCallingDate(selectContactRecordDetail.getNextCallingDate());
        contactRecordDetailViewTemp.setNextCallingTime(selectContactRecordDetail.getNextCallingTime());
        contactRecordDetailViewTemp.setReason(selectContactRecordDetail.getReason());
        contactRecordDetailViewTemp.setRemark(selectContactRecordDetail.getRemark());
        */
        setStrOnDataTable();
    }

    public void onAddContactRecordDetailView(){
        log.info("onAddContactRecordView >>> begin ");
        contactRecordDetailView = new ContactRecordDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onDeleteContactRecordDetailView() {
        log.info( " onDeleteContactRecordDetailView getRemark is " + selectContactRecordDetail.getRemark());
        contactRecordDetailViewList.remove(selectContactRecordDetail);
        onSetRowNoContactRecordDetailView();
        log.info( " onDeleteContactRecordDetailView end ");
    }

    public void onSetRowNoContactRecordDetailView(){
    	/*
        ContactRecordDetailView contactRecordDetailViewRow;
        for(int i=0;i< contactRecordDetailViewList.size();i++){
            contactRecordDetailViewRow = contactRecordDetailViewList.get(i);
            contactRecordDetailViewRow.setNo(i+1);
        }
        */
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
        log.debug("-- onSaveAppraisalDetailView() flag = {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if(appraisalDetailViewMandate()){
            complete = true;
            if(ModeForButton.ADD.equals(modeForButton)){
                appraisalDetailViewList.add(appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }else if(ModeForButton.EDIT.equals(modeForButton)){
                log.debug("-- RowIndex[{}]", rowIndex);
                appraisalDetailViewList.set(rowIndex, appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
            }
            context.addCallbackParam("functionComplete", complete);
        }else {
            context.addCallbackParam("functionComplete", complete);
        }
    }

    private boolean appraisalDetailViewMandate(){
        log.debug("-- appraisalDetailViewMandate()");
        boolean result = true;
        if(appraisalDetailViewDialog.getTitleDeed().length() == 0){
            titleDeedFlag = true;
            result = false;
        } else {
            titleDeedFlag = false;
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

    public void onSaveAppraisalContactDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if(true){
            complete = true;
            if(ModeForButton.ADD.equals(modeForButton)){
                log.info("onSaveAppraisalContactDetailView add >>> begin ");
                log.info("appraisalContactDetailViewList size >>> is " + appraisalContactDetailViewList.size());
//                appraisalContactDetailView.setNo(appraisalContactDetailViewList.size()+1);
                log.info("onSaveContactRecordDetailView contactRecordDetailView >>> " + appraisalContactDetailView);
                appraisalContactDetailViewList.add(appraisalContactDetailView);
                log.info("onSaveAppraisalContactDetailView add >>> end ");

            }else if(ModeForButton.EDIT.equals(modeForButton)){
                log.info("onSaveAppraisalContactDetailView edit >>> begin ");

                AppraisalContactDetailView appraisalContactDetailViewRow;
                appraisalContactDetailViewRow = appraisalContactDetailViewList.get(rowIndex);
//                appraisalContactDetailViewRow.setCustomerName(appraisalContactDetailView.getCustomerName());
//                appraisalContactDetailViewRow.setContactNo(appraisalContactDetailView.getContactNo());

                appraisalContactDetailView = new AppraisalContactDetailView();
                log.info("onSaveAppraisalContactDetailView edit >>> end ");
            }
        }
        context.addCallbackParam("functionComplete", complete);
    }
    
    public void onEditAppraisalDetailView(){
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalDetailView() RowIndex[{}]", rowIndex);
//        Cloner cloner = new Cloner();
//        appraisalDetailViewDialog = cloner.deepClone(appraisalDetailViewSelected);
        appraisalDetailViewDialog = appraisalDetailViewSelected;
    }

    public void onEditAppraisalContactDetailView(){
        log.info( " onEditAppraisalContactDetailView " + selectAppraisalContactDetailView);
        modeForButton = ModeForButton.EDIT;
        appraisalContactDetailView = new AppraisalContactDetailView();
        //*** Check list size ***//
        if( rowIndex < appraisalContactDetailViewList.size() ) {
//            appraisalContactDetailView.setCustomerName(selectAppraisalContactDetailView.getCustomerName());
//            appraisalContactDetailView.setContactNo(selectAppraisalContactDetailView.getContactNo());
        }
    }

    public void onAddAppraisalDetailView(){
        log.info("onAddAppraisalDetailView >>> begin ");
        appraisalDetailView = new AppraisalDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onAddAppraisalContactDetailView(){
        log.info("onAddAppraisalContactDetailView >>> begin ");
        appraisalContactDetailView = new AppraisalContactDetailView();
        modeForButton = ModeForButton.ADD;
    }


    public void onDeleteAppraisalDetailView() {
        log.info( "-- onDeleteAppraisalDetailView RowIndex[{}]", rowIndex);
        appraisalDetailViewList.remove(rowIndex);
        log.info( "-- AppraisalDetailViewList[{}] deleted", rowIndex);
    }

    public void onSetRowNoAppraisalDetailView(){
        AppraisalDetailView appraisalDetailViewRow;
        for(int i=0;i< appraisalDetailViewList.size();i++){
            appraisalDetailViewRow = appraisalDetailViewList.get(i);
            appraisalDetailViewRow.setNo(i+1);
        }
    }
    
    public void onDeleteAppraisalContactDetailView() {
        log.info( " onDeleteAppraisalContactDetailView " + selectAppraisalContactDetailView);
        appraisalContactDetailViewList.remove(selectAppraisalContactDetailView);
        onSetRowNoAppraisalContactDetailView();
        log.info( " onDeleteAppraisalContactDetailView end ");
    }

    public void onSetRowNoAppraisalContactDetailView(){
        AppraisalContactDetailView appraisalContactDetailViewRow;
        for(int i=0;i< appraisalContactDetailViewList.size();i++){
            appraisalContactDetailViewRow = appraisalContactDetailViewList.get(i);
//            appraisalContactDetailViewRow.setNo(i+1);
        }
    }

    public void onSaveAppraisalAppointment() {
        log.info("-- onSaveAppraisalAppointment::::");
        try{
            appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            appraisalAppointmentControl.onSaveAppraisalAppointment(appraisalView, workCaseId, workCasePreScreenId);
            messageHeader = msg.get("app.appraisal.request.message.header.save.success");
            message = msg.get("app.appraisal.request.message.body.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
            if(ex.getCause() != null){
                message = msg.get("app.appraisal.request.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.request.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }


        log.info("onSaveAppraisalAppointment::::");
        log.info("appraisalDetailViewList.size()        ::: {} ", appraisalDetailViewList.size());
        log.info("appraisalContactDetailViewList.size() ::: {} ", appraisalContactDetailViewList.size());
        try{
            //must set user from business controller only
            /*if(appraisalView.getId() == 0){
                appraisalView.setCreateBy(user);
                appraisalView.setCreateDate(DateTime.now().toDate());
            }
            appraisalView.setModifyBy(user);*/
            appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
//            appraisalView.setAppraisalContactDetailViewList(appraisalContactDetailViewList);
            appraisalView.setContactRecordDetailViewList(contactRecordDetailViewList);

            appraisalAppointmentControl.onSaveAppraisalAppointment(appraisalView, workCaseId, workCasePreScreenId);
            messageHeader = msg.get("app.appraisal.appointment.message.header.save.success");
            message = msg.get("app.appraisal.appointment.message.body.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.appointment.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.appraisal.appointment.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.appointment.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelAppraisalAppointment(){
        log.info("onCancelCustomerAcceptance::::  ");
        onCreation();
    }

    public void onChangeAppraisalDate(){
        log.info("onChangeAppraisalDate");
        int locate = appraisalView.getLocationOfProperty().getId();

        log.info("locate is " + locate);

        Date dueDate;
        DateTime dueDateTime = new DateTime(appraisalView.getAppraisalDate());
        DateTime addedDate  = new DateTime(appraisalView.getAppraisalDate());
        int nowDay = dueDateTime.getDayOfWeek();

        log.info ("dueDateTime dayOfWeek before plus is " + dueDateTime.getDayOfWeek());

        if(locate == 1){
            log.info("in locate 1 ");
            if(nowDay==1||nowDay>5) {
                addedDate = dueDateTime.plusDays(3);
            }else if(nowDay==4){
                addedDate = dueDateTime.plusDays(4);
            }else{
                addedDate = dueDateTime.plusDays(5);
            }
        }else if(locate == 2){
            log.info("in locate 2");
            if(nowDay>5) {
                addedDate = dueDateTime.plusDays(4);
            }else if(nowDay==5){
                addedDate = dueDateTime.plusDays(5);
            }else{
                addedDate = dueDateTime.plusDays(6);
            }
        }else if(locate == 3){
            log.info("in locate 3");
            if(nowDay==5){
                addedDate = dueDateTime.plusDays(9);
            }else if(nowDay==4){
                addedDate = dueDateTime.plusDays(10);
            }else{
                addedDate = dueDateTime.plusDays(8);
            }
        }

        log.info ("dueDateTime dayOfWeek after plus is " + dueDateTime.getDayOfWeek());

        dueDate = addedDate.toDate();
        appraisalView.setDueDate(dueDate);
        appraisalView.setAppointmentDate(appraisalView.getAppraisalDate());
    }

    public void onChangeReceivedTaskDate(){

    }

    public AppraisalContactDetailView getSelectAppraisalContactDetailView() {
        return selectAppraisalContactDetailView;
    }

    public void setSelectAppraisalContactDetailView(AppraisalContactDetailView selectAppraisalContactDetailView) {
        this.selectAppraisalContactDetailView = selectAppraisalContactDetailView;
    }

    public List<AppraisalContactDetailView> getAppraisalContactDetailViewList() {
        return appraisalContactDetailViewList;
    }

    public void setAppraisalContactDetailViewList(List<AppraisalContactDetailView> appraisalContactDetailViewList) {
        this.appraisalContactDetailViewList = appraisalContactDetailViewList;
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

    public List<ContactRecordDetailView> getContactRecordDetailViewList() {
        return contactRecordDetailViewList;
    }

    public void setContactRecordDetailViewList(List<ContactRecordDetailView> contactRecordDetailViewList) {
        this.contactRecordDetailViewList = contactRecordDetailViewList;
    }

    public ContactRecordDetailView getContactRecordDetailView() {
        return contactRecordDetailView;
    }

    public void setContactRecordDetailView(ContactRecordDetailView contactRecordDetailView) {
        this.contactRecordDetailView = contactRecordDetailView;
    }

    public ContactRecordDetailView getSelectContactRecordDetail() {
        return selectContactRecordDetail;
    }

    public void setSelectContactRecordDetail(ContactRecordDetailView selectContactRecordDetail) {
        this.selectContactRecordDetail = selectContactRecordDetail;
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

    public List<LocationProperty> getLocationPropertyList() {
        return locationPropertyList;
    }

    public void setLocationPropertyList(List<LocationProperty> locationPropertyList) {
        this.locationPropertyList = locationPropertyList;
    }

    public List<AppraisalDivision> getAppraisalDivisionList() {
        return appraisalDivisionList;
    }

    public void setAppraisalDivisionList(List<AppraisalDivision> appraisalDivisionList) {
        this.appraisalDivisionList = appraisalDivisionList;
    }

    public List<AppraisalCompany> getAppraisalCompanyList() {
        return appraisalCompanyList;
    }

    public void setAppraisalCompanyList(List<AppraisalCompany> appraisalCompanyList) {
        this.appraisalCompanyList = appraisalCompanyList;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public boolean getShowNoRequest() {
        return showNoRequest;
    }

    public void setShowNoRequest(boolean showNoRequest) {
        this.showNoRequest = showNoRequest;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
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

    public boolean isContactFlag3() {
        return contactFlag3;
    }

    public void setContactFlag3(boolean contactFlag3) {
        this.contactFlag3 = contactFlag3;
    }

    public boolean isContactFlag2() {
        return contactFlag2;
    }

    public void setContactFlag2(boolean contactFlag2) {
        this.contactFlag2 = contactFlag2;
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
}
