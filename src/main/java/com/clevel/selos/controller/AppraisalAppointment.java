package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalAppointmentControl;
import com.clevel.selos.businesscontrol.CustomerAcceptanceControl;
import com.clevel.selos.dao.master.AppraisalDivisionDAO;
import com.clevel.selos.dao.master.LocationPropertyDAO;
import com.clevel.selos.dao.relation.ReasonToStepDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.audit.SLOSAuditor;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "appraisalAppointment")
public class AppraisalAppointment extends BaseController implements Serializable {
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
    SLOSAuditor slosAuditor;

    @Inject
    private AppraisalDivisionDAO appraisalDivisionDAO;
    @Inject
    private LocationPropertyDAO locationPropertyDAO;
    @Inject
    private ReasonToStepDAO reasonToStepDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;

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
    private long statusId;
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
    private User user;

    public AppraisalAppointment() {

    }

    private void _initial(HttpSession session){
        log.debug("-- initial()");
        modeForButton = ModeForButton.ADD;

        appraisalView = new AppraisalView();
        appraisalDetailView = new AppraisalDetailView();
        appraisalContactDetailView = new AppraisalContactDetailView();
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        appraisalContactDetailViewList = new ArrayList<AppraisalContactDetailView>();
        contactRecordDetailView = new ContactRecordDetailView();
        contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
        customerAcceptanceView = new CustomerAcceptanceView();
        appraisalDetailViewDialog = new AppraisalDetailView();
        appraisalDetailViewSelected = new AppraisalDetailView();

        appraisalDivisionList= appraisalDivisionDAO.findActiveAll();
        locationPropertyList= locationPropertyDAO.findActiveAll();

        titleDeedFlag = false;
        purposeFlag = false;
        numberOfDocumentsFlag = false;
        contactFlag2 = false;
        contactFlag3 = false;

        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        user = (User) session.getAttribute("user");

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

    public void preRender(){
        log.debug("preRender ::: ");
        HttpSession session = FacesUtil.getSession(false);
        if(checkSession(session)){
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);

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
        HttpSession session = FacesUtil.getSession(false);
        Date actionDate = new Date();
        log.debug("onCreation :::");
        _initial(session);
        WorkCase workCase;
        WorkCasePrescreen workCasePrescreen;
        String bdmUserId = "";
        if(checkSession(session)){
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            if(!Util.isZero(workCaseId)){
                workCase = workCaseDAO.findById(workCaseId);
                bdmUserId = workCase.getCreateBy() != null ? workCase.getCreateBy().getId() : "";
                loadFieldControl(workCaseId, Screen.AppraisalAppointment, ownerCaseUserId);
            }else if(!Util.isZero(workCasePreScreenId)){
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
                bdmUserId = workCasePrescreen.getCreateBy() != null ? workCasePrescreen.getCreateBy().getId() : "";
                loadFieldControlPreScreen(workCasePreScreenId, Screen.AppraisalAppointment, ownerCaseUserId);
            }

            reasons = reasonToStepDAO.getAppraisalReason();
            contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
            appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId, workCasePreScreenId, statusId);
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

                getZoneTeamId(bdmUserId);
                updateContractFlag(appraisalContactDetailView);
            } else {
                appraisalView = new AppraisalView();
                appraisalView.reset();
                getZoneTeamId(bdmUserId);
            }
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_CREATION, "", actionDate, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_CREATION, "", actionDate, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    private void getZoneTeamId(String bdmUserId){
        if (!Util.isEmpty(bdmUserId)){
            appraisalView.setZoneLocation(appraisalAppointmentControl.getZoneLocation(bdmUserId)); //Zone from user
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
        log.debug("onClickForDialogNoRequest");
        messageHeader = "เกิดข้อผิดพลาด";
        message = "ยังไม่มีการกรอกข้อมูลการร้องขอ Appraisal มาก่อน";
        RequestContext.getCurrentInstance().execute("msgBoxNoRequestMessageDlg.show()");
    }

    public void onChangePageCauseNoRequest(){
        try{
            log.debug("onChangePageCauseNoRequest 1");
            String url = "appraisalRequest.jsf";
            log.debug("onChangePageCauseNoRequest 2");
            FacesContext fc = FacesContext.getCurrentInstance();
            log.debug("onChangePageCauseNoRequest 3");
            ExternalContext ec = fc.getExternalContext();
            log.debug("redirect to new page");
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
        Date actionDate = new Date();
        log.debug("-- onSaveContactRecordDetailView() flag = {}", modeForButton);
        boolean complete = false;
        Cloner cloner = new Cloner();

        contactRecord.updateNextCallingDate();

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
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save New Record Detail", actionDate, ActionResult.SUCCESS, "");
        } else if(ModeForButton.EDIT.equals(modeForButton)) {
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
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Edit Record Detail", actionDate, ActionResult.SUCCESS, "");
        }
        sendCallBackParam(complete);
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
        Date actionDate = new Date();
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if(appraisalDetailViewMandate()){
            complete = true;
            if(ModeForButton.ADD.equals(modeForButton)){
                appraisalDetailViewList.add(appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
                slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save New Appraisal Detail", actionDate, ActionResult.SUCCESS, "");
            }else if(ModeForButton.EDIT.equals(modeForButton)){
                log.debug("-- RowIndex[{}]", rowIndex);
                appraisalDetailViewList.set(rowIndex, appraisalDetailViewDialog);
                appraisalDetailViewList = appraisalDetailTransform.updateLabel(appraisalDetailViewList);
                slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Edit Appraisal Detail", actionDate, ActionResult.SUCCESS, "");
            }
            context.addCallbackParam("functionComplete", complete);
        }else {
            context.addCallbackParam("functionComplete", complete);
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Edit Appraisal Detail", actionDate, ActionResult.FAILED, "Input Mandatory failed.");
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
        Date actionDate = new Date();
        RequestContext context = RequestContext.getCurrentInstance();

        if(ModeForButton.ADD.equals(modeForButton)){
            log.debug("onSaveAppraisalContactDetailView add >>> begin ");
            log.debug("appraisalContactDetailViewList size >>> is {}", appraisalContactDetailViewList.size());
            log.debug("onSaveContactRecordDetailView contactRecordDetailView >>> : {}", appraisalContactDetailView);
            appraisalContactDetailViewList.add(appraisalContactDetailView);
            log.debug("onSaveAppraisalContactDetailView add >>> end ");
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save New Contact Detail", actionDate, ActionResult.SUCCESS, "");
            complete = true;
        }else if(ModeForButton.EDIT.equals(modeForButton)){
            log.debug("onSaveAppraisalContactDetailView edit >>> begin ");
            AppraisalContactDetailView appraisalContactDetailViewRow = appraisalContactDetailViewList.get(rowIndex);
            appraisalContactDetailView = new AppraisalContactDetailView();
            log.debug("onSaveAppraisalContactDetailView edit >>> end ");
            complete = true;
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Edit Contact Detail", actionDate, ActionResult.SUCCESS, "");
        }
        context.addCallbackParam("functionComplete", complete);
    }
    
    public void onEditAppraisalDetailView(){
        Date actionDate = new Date();
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalDetailView() RowIndex[{}]", rowIndex);
        Cloner cloner = new Cloner();
        try {
            appraisalDetailViewDialog = cloner.deepClone(appraisalDetailViewSelected);
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_EDIT, "On Edit Appraisal Detail", actionDate, ActionResult.SUCCESS, "");
        }catch(Exception ex){
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_EDIT, "On Edit Appraisal Detail", actionDate, ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onEditAppraisalContactDetailView(){
        Date actionDate = new Date();
        modeForButton = ModeForButton.EDIT;
        log.debug("-- onEditAppraisalContactDetailView() RowIndex[{}]", rowIndex);
        Cloner cloner = new Cloner();
        try {
            contactRecord = cloner.deepClone(contactRecord);
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_EDIT, "On Edit Contact Detail", actionDate, ActionResult.SUCCESS, "");
        }catch(Exception ex){
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_EDIT, "On Edit Contact Detail", actionDate, ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onAddAppraisalDetailView(){
        log.debug("onAddAppraisalDetailView >>> begin ");
        appraisalDetailView = new AppraisalDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onAddAppraisalContactDetailView(){
        Date actionDate = new Date();
        log.debug("-- onAddAppraisalContactDetailView() ModeForButton[ADD]");
        contactRecord = new ContactRecordDetailView();
        contactRecord.setCreateBy(user);
        contactRecord.setStatus(workCaseStatus);
        if (Util.isNull(reasons) || Util.isZero(reasons.size())){
            reasons = customerAcceptanceControl.getContactRecordReasons();
        }
        contactRecord.setCallingDate(getCurrentDate());
        modeForButton = ModeForButton.ADD;
        slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_ADD, "On Add Contact Detail", actionDate, ActionResult.SUCCESS, "");
    }


    public void onDeleteAppraisalDetailView() {
        Date actionDate = new Date();
        appraisalDetailViewList.remove(rowIndex);
        appraisalView.getRemoveCollListId().add(appraisalDetailViewSelected.getNewCollateralId());
        log.debug( "-- AppraisalDetailViewList[{}] deleted", rowIndex);
        slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_DELETE, "On Delete Appraisal Detail", actionDate, ActionResult.SUCCESS, "");
    }

    public void onDeleteAppraisalContactDetailView() {
        Date actionDate = new Date();
        contactRecordDetailViewList.remove(rowIndex);
        log.debug( "-- onDeleteAppraisalContactDetailView[{}] deleted", rowIndex);
        slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_DELETE, "On Delete Contact Detail", actionDate, ActionResult.SUCCESS, "");
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
        Date actionDate = new Date();
        log.debug("-- onSaveAppraisalAppointment::::");
        log.debug("appraisalDetailViewList.size()        ::: {} ", appraisalDetailViewList.size());
        log.debug("appraisalContactDetailViewList.size() ::: {} ", appraisalContactDetailViewList.size());
        try{
            appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);
            appraisalView.setContactRecordDetailViewList(contactRecordDetailViewList);
            appraisalAppointmentControl.onSaveAppraisalAppointment(appraisalView, workCaseId, workCasePreScreenId, contactRecordDetailViewList, customerAcceptanceView, statusId);
            messageHeader = "Information";
            message = msg.get("app.appraisal.appointment.message.body.save.success");
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Appraisal Appointment", actionDate, ActionResult.SUCCESS, "");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = "Exception";
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_SAVE, "On Save Appraisal Appointment", actionDate, ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onCancelAppraisalAppointment(){
        log.debug("onCancelCustomerAcceptance::::  ");
        slosAuditor.add(Screen.AppraisalAppointment.value(), getCurrentUser().getId(), ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");
        onCreation();
    }

    //TODO edit this for fix due date
    public void onChangeAppraisalDate(){
        log.debug("-- onChangeAppraisalDate()");
        final Date NOW = DateTime.now().toDate();
        if(Util.isNull(appraisalView.getAppraisalDate())){
            appraisalView.setAppraisalDate(NOW);
            log.debug("--[NEW] AppraisalDate : {}", dateString(appraisalView.getAppraisalDate()));
        }

        if(Util.isNull(appraisalView.getDueDate())){
            appraisalView.setDueDate(NOW);
            log.debug("--[NEW] DueDate : {}", dateString(appraisalView.getDueDate()));
        }

        if(!Util.isNull(appraisalView.getLocationOfProperty())){
            log.debug("-- AppraisalView[{}]", appraisalView.toString());
            final int LOCATE = appraisalView.getLocationOfProperty().getId();
            log.debug("-- locate id is {}", appraisalView.getLocationOfProperty().getId());
            final int BANGKOK_AND_PERIMETER = 3;
            final int COUNTRY = 4;
            final int OTHER_CASE = 6;
            final Date APPRAISAL_DATE = appraisalView.getAppraisalDate();
            log.debug("-- APPRAISAL_DATE : {}", dateString(APPRAISAL_DATE));
            if(LOCATE == 1){
                log.debug("-- In locate due date +{}.", BANGKOK_AND_PERIMETER);
                log.debug("--[BEFORE] DueDate : {}", dateString(appraisalView.getDueDate()));
                appraisalView.setDueDate(updateDueDate(APPRAISAL_DATE, BANGKOK_AND_PERIMETER));
                log.debug("--[AFTER] DueDate : {}", dateString(appraisalView.getDueDate()));
            }else if(LOCATE == 2){
                log.debug("-- In locate due date +{}.", COUNTRY);
                log.debug("--[BEFORE] DueDate : {}", dateString(appraisalView.getDueDate()));
                appraisalView.setDueDate(updateDueDate(APPRAISAL_DATE, COUNTRY));
                log.debug("--[AFTER] DueDate : {}", dateString(appraisalView.getDueDate()));
            }else if(LOCATE == 3){
                log.debug("-- In locate due date +{}.", OTHER_CASE);
                log.debug("--[BEFORE] DueDate : {}", dateString(appraisalView.getDueDate()));
                appraisalView.setDueDate(updateDueDate(APPRAISAL_DATE, OTHER_CASE));
                log.debug("--[AFTER] DueDate : {}", dateString(appraisalView.getDueDate()));
            } else {
                log.debug("-- Other locate");
                log.debug("--[BEFORE] DueDate : {}", dateString(appraisalView.getDueDate()));
                appraisalView.setDueDate(updateDueDate(APPRAISAL_DATE, BANGKOK_AND_PERIMETER));
                log.debug("--[AFTER] DueDate : {}", dateString(appraisalView.getDueDate()));
            }
        } else {
            log.debug("-- AppraisalView.getLocationOfProperty() is null");
            appraisalView.setAppraisalDate(NOW);
            log.debug("--[NEW] AppraisalDate");
            appraisalView.setDueDate(NOW);
            log.debug("--[NEW] DueDate");
        }
    }

    public void onChangeAppointmentDate(){
        log.debug("-- onChangeAppointmentDate()");
        if(Util.isNull(appraisalView.getAppointmentDate())){
            appraisalView.setAppointmentDate(DateTime.now().toDate());
            log.debug("--[NEW] AppointmentDate");
        }
        appraisalView.setAppraisalDate(appraisalView.getAppointmentDate());
        onChangeAppraisalDate();
    }

    public void onChangeLocationOfProperty(){
        log.debug("-- onChangeLocationOfProperty()");
        if(!Util.isNull(appraisalView.getAppraisalDate()) && !Util.isNull(appraisalView.getDueDate())){
            log.debug("-- AppraisalDate and DuaDate is not null");
            onChangeAppraisalDate();
        }
    }

    private Date updateDueDate(final Date APPRAISAL_DATE, final int DAY_FOR_DUE_DATE){
        int addDayForDueDate = 0;
        log.debug("-- AppraisalDate : {}", dateString(APPRAISAL_DATE));
        for (int i = 1; i <= DAY_FOR_DUE_DATE; i++) {
            final Date date = addDate(APPRAISAL_DATE, i);
            log.debug("-- Check DATE : {}", dateString(date));
            if(isDayOff(date)){
                log.debug("-- {} is day off.", dateString(date));
                addDayForDueDate++;
                log.debug("-- addDayForDueDate : {}", addDayForDueDate);
            }  else if(isHoliday(date)){
                log.debug("-- {} is holiday.", dateString(date));
                addDayForDueDate++;
                log.debug("-- addDayForDueDate : {}", addDayForDueDate);
            }
        }
        final int TOTAL_DAY = addDayForDueDate + DAY_FOR_DUE_DATE;
        log.debug("-- addDayForDueDate[{}] + dayByLocate[{}] = Total Day[{}]",addDayForDueDate, DAY_FOR_DUE_DATE, TOTAL_DAY);

        final Date DATE = addDate(APPRAISAL_DATE, TOTAL_DAY);
        log.debug("-- AppraisalDate : {}", dateString(APPRAISAL_DATE));
        log.debug("-- DueDate : {}", dateString(DATE));
        return checkDueDate(DATE);
    }

    private Date checkDueDate(final Date DUE_DATE){
        log.debug("-- checkDueDate(DueDate : {})", dateString(DUE_DATE));
        final int TWO_DAYS = 2;
        final int ONE_DAY = 1;
        Date date = DUE_DATE;
        while (isDayOff(date) || isHoliday(date)) {
            if(isSaturday(date)){
                log.debug("--[BEFORE] DueDate : {}", dateString(date));
                date = addDate(date, TWO_DAYS);
                log.debug("--[AFTER] DueDate : {}", dateString(date));
            } else if(isSunday(date)){
                log.debug("--[BEFORE] DueDate : {}", dateString(date));
                date = addDate(date, ONE_DAY);
                log.debug("--[AFTER] DueDate : {}", dateString(date));
            } else if(isHoliday(date)){
                log.debug("--[BEFORE] DueDate : {}", dateString(date));
                date = addDate(date, ONE_DAY);
                log.debug("--[AFTER] DueDate : {}", dateString(date));
            }
        }
        log.debug("--[RETURN] DueDate : {}", dateString(date));
        return date;
    }

    private boolean isDayOff(final Date DATE){
        log.debug("-- isDayOff(Date : {})", dateString(DATE));
        return isSaturday(DATE)|| isSunday(DATE);
    }

    private boolean isSaturday(final Date DATE){
        log.debug("-- isSaturday(Date : {})", dateString(DATE));
        return DayOff.SATURDAY.equals(getDayOfWeek(DATE));
    }

    private boolean isSunday(final Date DATE){
        log.debug("-- isSunday(Date : {})", dateString(DATE));
        return DayOff.SUNDAY.equals(getDayOfWeek(DATE));
    }

    private String getDayOfWeek(final Date DATE){
        final String DAY_OF_WEEK = DateTimeUtil.getDayOfWeek(DATE);
        log.debug("-- {} is {}.", dateString(DATE), DAY_OF_WEEK);
        return DAY_OF_WEEK;
    }

    private boolean isHoliday(final Date DATE){
        return appraisalAppointmentControl.isHoliday(DATE);
    }

    private Date addDate(final Date DATE, final int DAY){
        return DateTimeUtil.addDayForDueDate(DATE, DAY);
    }

    private String dateString(final Date DATE){
        return DateTimeUtil.convertToStringDDMMYYYY(DATE);
    }

    public void onOpenAddContactRecordDialog() {
        log.debug("Open Contact Record Dialog");
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
        log.debug("Open Update Contact Record Dialog");
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
        log.debug("current date : {}", getCurrentDate());
        return DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }
}
