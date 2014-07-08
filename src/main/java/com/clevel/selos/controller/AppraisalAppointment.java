package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalAppointmentControl;
import com.clevel.selos.businesscontrol.CustomerAcceptanceControl;
import com.clevel.selos.dao.master.AppraisalDivisionDAO;
import com.clevel.selos.dao.master.LocationPropertyDAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DayOff;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
    private AppraisalDivisionDAO appraisalDivisionDAO;
    @Inject
    private LocationPropertyDAO locationPropertyDAO;
    @Inject
    private ReasonDAO reasonDAO;

    @Inject
    private AppraisalAppointmentControl appraisalAppointmentControl;
    @Inject
    private CustomerAcceptanceControl customerAcceptanceControl;

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

    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private ContactRecordDetailView contactRecordDetailView;
    private List<ContactRecordDetailView> deleteList;
    private ContactRecordDetailView contactRecord;
    private int deletedRowId;
    private List<Reason> reasons;
    private boolean addDialog;
    private Status workCaseStatus;
    private CustomerAcceptanceView customerAcceptanceView;

    public AppraisalAppointment() {

    }

    private void init(){
        log.debug("-- init()");
        appraisalView = new AppraisalView();
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        appraisalContactDetailViewList = new ArrayList<AppraisalContactDetailView>();
        contactRecordDetailView = new ContactRecordDetailView();
        contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
        customerAcceptanceView = new CustomerAcceptanceView();
        modeForButton = ModeForButton.ADD;
        appraisalDivisionList= appraisalDivisionDAO.findAll();
        locationPropertyList= locationPropertyDAO.findAll();
        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        contactFlag2 = false;
        contactFlag3 = false;
        appraisalDetailViewDialog = new AppraisalDetailView();
        appraisalDetailViewSelected = new AppraisalDetailView();
        onLoadCompany();
        onLoadProvince();
    }

    private void onLoadProvince(){
        log.debug("-- onLoadProvince()");
        provinceList =  appraisalAppointmentControl.getProvince();
        if(!Util.isSafetyList(provinceList)){
            provinceList = new ArrayList<Province>();
        }
    }

    private void onLoadCompany(){
        log.debug("-- onLoadCompany()");
        appraisalCompanyList =  appraisalAppointmentControl.getCompany();
        if(!Util.isSafetyList(appraisalCompanyList)){
            appraisalCompanyList = new ArrayList<AppraisalCompany>();
        }
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( (Long)session.getAttribute("workCaseId") != 0 || (Long)session.getAttribute("workCasePreScreenId") != 0 ) &&
                (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void preRender(){
        log.info("preRender ::: ");
        HttpSession session = FacesUtil.getSession(false);
        if(checkSession(session)){
            stepId = (Long)session.getAttribute("stepId");

            if(stepId != StepValue.REVIEW_APPRAISAL_REQUEST.value()){
                log.debug("preRender ::: invalid stepId : [{}]", stepId);
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
        } else {
            log.debug("preRender ::: workCasePreScreenId, workCaseId, stepId is null.");
            FacesUtil.redirect("/site/inbox.jsf");
            return;
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation :::");
        init();
        HttpSession session = FacesUtil.getSession(false);
        if(checkSession(session)){
            if((Long)session.getAttribute("workCaseId") != 0){
                workCaseId = (Long)session.getAttribute("workCaseId");
            } else if ((Long)session.getAttribute("workCasePreScreenId") != 0){
                workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            }

            reasons = reasonDAO.findAll();

            contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();


            appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId, workCasePreScreenId);
            workCaseStatus = customerAcceptanceControl.getWorkCaseStatus(workCaseId);

            if(!Util.isNull(appraisalView)){
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(Util.safetyList(appraisalView.getAppraisalDetailViewList()));
                if(Util.isZero(appraisalDetailViewList.size())){
                    appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
                }
                appraisalContactDetailView = appraisalView.getAppraisalContactDetailView();
                if(Util.isNull(appraisalContactDetailView)){
                    appraisalContactDetailView = new AppraisalContactDetailView();
                }

                contactRecordDetailViewList = appraisalView.getContactRecordDetailViewList();

                customerAcceptanceView = customerAcceptanceControl.getCustomerAcceptanceView(workCaseId, workCasePreScreenId);

//                contactRecordDetailViewList = Util.safetyList(customerAcceptanceControl.getContactRecordDetails(customerAcceptanceView.getId()));

                updateContractFlag(appraisalContactDetailView);
            } else {
                appraisalView = new AppraisalView();
            }
        } else {
            //TODO show message box
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

    public void onSaveContactRecordDetailView(){
        log.debug("-- onSaveContactRecordDetailView() flag = {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        Cloner cloner = new Cloner();
        if(Util.isZero(contactRecordDetailViewList.size())){
            contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
            log.debug("-- [NEW]ContactRecordDetailViewList created");
        }

        if(ModeForButton.ADD.equals(modeForButton)){
            log.debug("-- [AFTER]ContactRecordDetailViewList.size()[{}]", contactRecordDetailViewList.size());
            contactRecordDetailView = cloner.deepClone(contactRecord);
            if(!Util.isNull(reasons) && !Util.isZero(reasons.size())){
                log.debug("-- ReasonList.size()[{}]", reasons.size());
                for(Reason reason : reasons){
                    if(reason.getId() == contactRecordDetailView.getUpdReasonId()){
                        log.debug("-- ContactRecordDetailView.UpdReasonId[{}]", contactRecordDetailView.getUpdReasonId());
                        contactRecordDetailView.setReason(reason);
                        break;
                    }
                }
            }
            contactRecordDetailViewList.add(contactRecordDetailView);
            complete = true;
            log.debug("-- [BEFORE]ContactRecordDetailViewList.size()[{}]", contactRecordDetailViewList.size());
        }else if(ModeForButton.EDIT.equals(modeForButton)){
            log.debug("-- RowIndex[{}]", rowIndex);
            log.debug("-- [AFTER]ContactRecordDetailViewList.size()[{}]", contactRecordDetailViewList.size());
            contactRecordDetailView = cloner.deepClone(contactRecord);
            if(!Util.isNull(reasons) && !Util.isZero(reasons.size())){
                log.debug("-- ReasonList.size()[{}]", reasons.size());
                for(Reason reason : reasons){
                    if(reason.getId() == contactRecordDetailView.getUpdReasonId()){
                        log.debug("-- ContactRecordDetailView.UpdReasonId[{}]", contactRecordDetailView.getUpdReasonId());
                        contactRecordDetailView.setReason(reason);
                        break;
                    }
                }
            }
            complete = true;
            contactRecordDetailViewList.set(rowIndex, contactRecordDetailView);
            log.debug("-- [BEFORE]ContactRecordDetailViewList.size()[{}]", contactRecordDetailViewList.size());
        }
        context.addCallbackParam("functionComplete", complete);

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
        Cloner cloner = new Cloner();
        appraisalDetailViewDialog = cloner.deepClone(appraisalDetailViewSelected);
    }

    public void onEditAppraisalContactDetailView(){
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalContactDetailView() RowIndex[{}]", rowIndex);
        Cloner cloner = new Cloner();
        contactRecord = cloner.deepClone(contactRecord);
    }

    public void onAddAppraisalDetailView(){
        log.info("onAddAppraisalDetailView >>> begin ");
        appraisalDetailView = new AppraisalDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onAddAppraisalContactDetailView(){
        log.info("-- onAddAppraisalContactDetailView() ModeForButton[ADD]");
        contactRecord = new ContactRecordDetailView();
        if (Util.isNull(reasons) || Util.isZero(reasons.size())){
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        contactRecord.setCallingDate(getCurrentDate());
        modeForButton = ModeForButton.ADD;
    }


    public void onDeleteAppraisalDetailView() {
        log.info( "-- onDeleteAppraisalDetailView RowIndex[{}]", rowIndex);
        appraisalDetailViewList.remove(rowIndex);
        log.info( "-- AppraisalDetailViewList[{}] deleted", rowIndex);
    }

    public void onDeleteAppraisalContactDetailView() {
        log.info( "-- onDeleteAppraisalContactDetailView RowIndex[{}]", rowIndex);
        contactRecordDetailViewList.remove(rowIndex);
        log.info( "-- onDeleteAppraisalContactDetailView[{}] deleted", rowIndex);
    }

    public void onSetRowNoAppraisalDetailView(){
        AppraisalDetailView appraisalDetailViewRow;
        for(int i=0;i< appraisalDetailViewList.size();i++){
            appraisalDetailViewRow = appraisalDetailViewList.get(i);
            appraisalDetailViewRow.setNo(i+1);
        }
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
            appraisalAppointmentControl.onSaveAppraisalAppointment(appraisalView, workCaseId, workCasePreScreenId, contactRecordDetailViewList, customerAcceptanceView);
            messageHeader = msg.get("app.appraisal.request.message.header.save.success");
            message = msg.get("app.appraisal.request.message.body.save.success");

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
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

            appraisalAppointmentControl.onSaveAppraisalAppointment(appraisalView, workCaseId, workCasePreScreenId, contactRecordDetailViewList, customerAcceptanceView);
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

    //TODO edit this for fix due date
    public void onChangeAppraisalDate(){
        log.info("-- onChangeAppraisalDate()");
        if(!Util.isNull(appraisalView.getLocationOfProperty())){
            int locate = appraisalView.getLocationOfProperty().getId();
            log.info("-- locate name is {}", appraisalView.getLocationOfProperty().getName());


            if(Util.isNull(appraisalView.getAppraisalDate())){
                appraisalView.setAppraisalDate(DateTime.now().toDate());
                log.debug("--[NEW] AppraisalDate");
            }

            if(Util.isNull(appraisalView.getDueDate())){
                appraisalView.setDueDate(DateTime.now().toDate());
                log.debug("--[NEW] DueDate");
            }

            /*
            1 +3
            2 +4
            3 +6
            * */
            int locateDay = 0;
            if(locate == 1){
                locateDay = 3;
                log.info("-- in locate due date +{}", locateDay);
                appraisalView.setDueDate(updateDueDate(appraisalView.getAppraisalDate(), locateDay));
            }else if(locate == 2){
                locateDay = 4;
                log.info("-- in locate due date +{}", locateDay);
                appraisalView.setDueDate(updateDueDate(appraisalView.getAppraisalDate(), locateDay));
            }else if(locate == 3){
                locateDay = 6;
                log.info("-- in locate due date +{}", locateDay);
                appraisalView.setDueDate(updateDueDate(appraisalView.getAppraisalDate(), locateDay));
            } else {
                appraisalView.setAppraisalDate(DateTime.now().toDate());
                log.debug("--[NEW] AppraisalDate");
                appraisalView.setDueDate(DateTime.now().toDate());
                log.debug("--[NEW] DueDate");
            }
        } else {
            log.debug("-- AppraisalView.getLocationOfProperty() is null");
            appraisalView.setAppraisalDate(DateTime.now().toDate());
            log.debug("--[NEW] AppraisalDate");
            appraisalView.setDueDate(DateTime.now().toDate());
            log.debug("--[NEW] DueDate");
        }

//        appraisalView.setAppointmentDate(appraisalView.getAppraisalDate());  //Edited by Chai for fixed issue
//        appraisalView.setAppraisalDate(appraisalView.getAppointmentDate());
    }

    public void onChangeAppointmentDate(){
        log.info("-- onChangeAppointmentDate()");
        if(Util.isNull(appraisalView.getAppointmentDate())){
            appraisalView.setAppointmentDate(DateTime.now().toDate());
            log.debug("--[NEW] AppointmentDate");
        }
        appraisalView.setAppraisalDate(appraisalView.getAppointmentDate());
        onChangeAppraisalDate();
    }

    public void onChangeLocationOfProperty(){
        log.info("-- onChangeLocationOfProperty()");
        onChangeAppraisalDate();
    }

    private Date updateDueDate(final Date appraisalDate, final int dayByLocate){
        int addDayForDueDate = 0;
        log.debug("-- AppraisalDate : {}", DateTimeUtil.convertToStringDDMMYYYY(appraisalDate));
        for (int i = 1; i <= dayByLocate; i++) {
            final Date date = DateTimeUtil.addDayForDueDate(appraisalDate, i);
            final String dayOfWeek = DateTimeUtil.getDayOfWeek(date);
            log.debug("-- Check DATE : {}", DateTimeUtil.convertToStringDDMMYYYY(date));
            log.debug("-- Check Day of week : {}", dayOfWeek);
            if(DayOff.SATURDAY.equals(dayOfWeek) ||
               DayOff.SUNDAY.equals(dayOfWeek)){
                log.debug("-- {} is day off.", DateTimeUtil.convertToStringDDMMYYYY(date));
                addDayForDueDate++;
                log.debug("-- addDayForDueDate : {}", addDayForDueDate);
            }  else if(appraisalAppointmentControl.isHoliday(date)){
                log.debug("-- {} is holiday.", DateTimeUtil.convertToStringDDMMYYYY(date));
                addDayForDueDate++;
                log.debug("-- addDayForDueDate : {}", addDayForDueDate);
            }
        }
        final int totalDate = addDayForDueDate + dayByLocate;
        log.debug("-- addDayForDueDate : {}", addDayForDueDate);
        log.debug("-- dayByLocate : {}", dayByLocate);
        log.debug("-- Total Date : {}", totalDate);

        final Date date = DateTimeUtil.addDayForDueDate(appraisalDate, totalDate);
        log.debug("-- AppraisalDate : {}", DateTimeUtil.convertToStringDDMMYYYY(appraisalDate));
        log.debug("-- DueDate : {}", DateTimeUtil.convertToStringDDMMYYYY(date));
        return checkDueDate(date);
    }

    private Date checkDueDate(final Date dueDate){
        final String dayOfWeek = DateTimeUtil.getDayOfWeek(dueDate);
        Date date = dueDate;
        boolean flag = true;
        while (flag) {
            if(DayOff.SATURDAY.equals(dayOfWeek)){
                date = DateTimeUtil.addDayForDueDate(date, 2);
                flag = !flag;
            } else if(DayOff.SUNDAY.equals(dayOfWeek)){
                date = DateTimeUtil.addDayForDueDate(date, 1);
                flag = !flag;
            } else if(appraisalAppointmentControl.isHoliday(dueDate)){
                date = DateTimeUtil.addDayForDueDate(date, 1);
            }
        }
        log.debug("-- DueDate : {}", DateTimeUtil.convertToStringDDMMYYYY(date));
        return date;
    }

    public void onOpenAddContactRecordDialog() {
        log.info("Open Contact Record Dialog");
        contactRecord = new ContactRecordDetailView();
        contactRecord.setId(0);
        contactRecord.setCallingDate(new Date());
        //contactRecord.setCreateBy(user);
        contactRecord.setStatus(workCaseStatus);
        if (reasons == null) {
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        addDialog = true;
    }

    public void onOpenUpdateContactRecordDialog() {
        log.info("Open Update Contact Record Dialog");
        if (reasons == null) {
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        addDialog = false;
    }

    public void onChangeReceivedTaskDate(){

    }


    public boolean isAddDialog() {
        return addDialog;
    }

    public void setAddDialog(boolean addDialog) {
        this.addDialog = addDialog;
    }

    public int getDeletedRowId() {
        return deletedRowId;
    }

    public void setDeletedRowId(int deletedRowId) {
        this.deletedRowId = deletedRowId;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
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

    public ContactRecordDetailView getContactRecord() {
        return contactRecord;
    }

    public void setContactRecord(ContactRecordDetailView contactRecord) {
        this.contactRecord = contactRecord;
    }

    public String getMinDate() {
        SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
        return dFmt.format(new Date());
    }
}
