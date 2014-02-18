package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalResultControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.business.CollateralBizTransform;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "appraisalResult")
public class AppraisalResult implements Serializable {

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
    private AppraisalResultControl appraisalResultControl;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private COMSInterface comsInterface;
    @Inject
    private CollateralBizTransform collateralBizTransform;

    private int rowIndex;
    private String messageHeader;
    private String message;
    private boolean showNoRequest;
    private Date currentDate;
    private int rowCollateral;
    private boolean flagReadOnly;

    //private User user;
    private long workCaseId;
    private long workCasePreScreenId;
    private long stepId;
    private AppraisalView appraisalView;

    //collateralDetailViewList
    private List<NewCollateralView> newCollateralViewList;
    //collateralDetailView
    private NewCollateralView newCollateralView;

    //collateralHeaderDetailViewList
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    //collateralHeaderDetailView
    private NewCollateralHeadView newCollateralHeadView;

    //subCollateralDetailViewList
    private List<NewCollateralSubView> newCollateralSubViewList;
    //subCollateralDetailView
    private NewCollateralSubView newCollateralSubView;



    private AppraisalData appraisalData;

    private List<HeadCollateralData> headCollateralDataList;
    private HeadCollateralData headCollateralData;

    private SubCollateralData subCollateralData;
    private List<SubCollateralData> subCollateralDataList;

    private NewCollateralView selectCollateralDetailView;

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

    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private boolean saveAndEditFlag;

    public AppraisalResult() {

    }

    private void init(){
        log.debug("-- init");
        modeForButton = ModeForButton.ADD;
        appraisalCompanyList = appraisalCompanyDAO.findAll();
        appraisalDivisionList= appraisalDivisionDAO.findAll();
        locationPropertyList= locationPropertyDAO.findAll();
        provinceList= provinceDAO.findAll();

        newCollateralViewList = new ArrayList<NewCollateralView>();
        newCollateralViewList.add(newCollateralViewForTest());
        newCollateralViewList.add(newCollateralViewForTest2());

        appraisalView = new AppraisalView();
        flagReadOnly = false;
        saveAndEditFlag = false;
    }

    public void preRender(){
        log.info("-- preRender.");
        HttpSession session = FacesUtil.getSession(true);
        log.debug("preRender ::: setSession ");
//        workCaseId = 4;
//        user = (User)session.getAttribute("user");

        if((!Util.isNull(session.getAttribute("workCaseId")) || !Util.isNull(session.getAttribute("workCasePreScreenId"))) && !Util.isNull(session.getAttribute("stepId"))){
            stepId = Long.valueOf(""+session.getAttribute("stepId"));
            log.debug("-- stepId[{}]", stepId);

            if(stepId != StepValue.REVIEW_APPRAISAL_REQUEST.value()){
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } else {
                if(!Util.isNull(session.getAttribute("workCaseId"))){
                    workCaseId = Long.valueOf(""+session.getAttribute("workCaseId"));
                }else if(!Util.isNull(session.getAttribute("workCasePreScreenId"))){
                    workCasePreScreenId = Long.valueOf(""+session.getAttribute("workCasePreScreenId"));
                }else{
                    log.error("error while loading page, can not find workCaseId/workCasePreScreenId in session.");
                    FacesUtil.redirect("/site/inbox.jsf");
                    return;
                }
            }
        } else {
            log.error("error while loading page, can not find workCaseId/workCasePreScreenId in session.");
            FacesUtil.redirect("/site/inbox.jsf");
            return;
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        preRender();
        init();
        appraisalView = appraisalResultControl.getAppraisalResult(workCaseId, workCasePreScreenId);
        if(!Util.isNull(appraisalView)){
            newCollateralViewList = Util.safetyList(appraisalView.getNewCollateralViewList());
            if(newCollateralViewList.size() == 0){
                newCollateralViewList = new ArrayList<NewCollateralView>();
            }
        } else {
            appraisalView = new AppraisalView();
            log.debug("-- AppraisalView[New] created");
        }
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
        } catch(Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.result.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.appraisal.result.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.result.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }
    public void onAddCollateralDetailView(){
        log.info("-- onAddCollateralDetailView >>> begin ");
        modeForButton = ModeForButton.ADD;
        flagReadOnly = false;
        saveAndEditFlag = false;
        newCollateralView = new NewCollateralView();
        newCollateralView.setJobID("");
        log.debug("-- NewCollateralView[New] created");
    }
    public void onCallRetrieveAppraisalReportInfo() {
        String jobID = newCollateralView.getJobID();
        log.info("-- onCallRetrieveAppraisalReportInfo  NewCollateralView.jobIDSearch[{}]", jobID);
        boolean flag = true;
        messageHeader = "Information";
        message = "Duplicate Job ID";
        if(!Util.isNull(jobID)){
            try {
                if(ModeForButton.ADD.equals(modeForButton)){
                    log.debug("-- ADD");
                    flag = checkJobIdExist(newCollateralViewList, jobID);
                    if(flag){
                        AppraisalDataResult appraisalDataResult = callCOM_S(jobID);
                        if(!Util.isNull(appraisalDataResult) && ActionResult.SUCCESS.equals(appraisalDataResult.getActionResult())){
                            newCollateralView = collateralBizTransform.transformCollateral(appraisalDataResult);
                            saveAndEditFlag = true;
                        } else {
                            saveAndEditFlag = false;
                            messageHeader = ""+appraisalDataResult.getActionResult();
                            message = appraisalDataResult.getReason();
                            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        }
                    } else {
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    }

                } else if(ModeForButton.EDIT.equals(modeForButton)){
                    log.debug("-- EDIT");
                    AppraisalDataResult appraisalDataResult = callCOM_S(jobID);
                    if(!Util.isNull(appraisalDataResult) && ActionResult.SUCCESS.equals(appraisalDataResult.getActionResult())){
                        newCollateralView = collateralBizTransform.transformCollateral(appraisalDataResult);
                        saveAndEditFlag = true;
                    } else {
                        saveAndEditFlag = false;
                        messageHeader = ""+appraisalDataResult.getActionResult();
                        message = appraisalDataResult.getReason();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    }
                }
            } catch (COMSInterfaceException e){
                messageHeader = "Exception";
                message = e.getMessage();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        } else {
            messageHeader = "Exception";
            message = "Job ID Search is empty or null";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }
    private boolean checkJobIdExist(final List<NewCollateralView> viewList, String jobIDSearch){
        for(NewCollateralView view : viewList){
            if(Util.equals(view.getJobID(), jobIDSearch)){
                return false;
            }
        }
        return true;
    }
    private AppraisalDataResult callCOM_S(final String jobID){
        AppraisalDataResult appraisalDataResult = appraisalResultControl.retrieveDataFromCOMS(jobID);
        return appraisalDataResult;
    }
    public void onSaveCollateralDetailView(){
        log.debug("-- onSaveCollateralDetailView()");
        boolean complete = false;
        if(ModeForButton.ADD.equals(modeForButton)){
            log.debug("-- Flag {}", ModeForButton.ADD);
            complete=true;
            String jobID = newCollateralView.getJobID();
            if(!Util.isNull(jobID) && !Util.equals(jobID, "")){
                if(saveAndEditFlag){
                    newCollateralViewList.add(newCollateralView);
                    log.info("-- NewCollateralView.jobID[{}] added to NewCollateralViewList[{}]", newCollateralView.getJobID(), newCollateralViewList.size()+1);
                }
            }
        }else if(ModeForButton.EDIT.equals(modeForButton)){
            complete=true;
            log.debug("-- Flag {}", ModeForButton.EDIT);
            if(saveAndEditFlag){
                newCollateralViewList.set(rowCollateral, newCollateralView);
                log.info("-- NewCollateralView.jobID[{}] updated to NewCollateralViewList[{}]", newCollateralView.getJobID(), rowCollateral);
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);
    }
    public void onEditCollateralDetailView(){
        log.info("-- onEditCollateralDetailView " + newCollateralViewList.size());
        modeForButton = ModeForButton.EDIT;
        newCollateralView = selectCollateralDetailView;
        if(Util.isNull(newCollateralView.getJobID()) || Util.isZero(newCollateralView.getJobID().length())){
            flagReadOnly = false;
        } else {
            flagReadOnly = true;
        }
    }
    public void onDeleteCollateralDetailView(){
        newCollateralViewList.remove(selectCollateralDetailView);
        log.info("-- onDeleteCollateralDetailView Job id {} deleted", selectCollateralDetailView.getJobID());
    }

    public void onSaveAppraisalResult() {
        log.info("-- onSaveAppraisalResult");
        try{
            appraisalView.setNewCollateralViewList(newCollateralViewList);

            appraisalResultControl.onSaveAppraisalResult(appraisalView, workCaseId, workCasePreScreenId);
            messageHeader = msg.get("app.appraisal.result.message.header.save.success");
            message = msg.get("app.appraisal.result.body.message.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.result.message.header.save.fail");
            if(ex.getCause() != null){
                message = msg.get("app.appraisal.result.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.result.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }
    public void onCancelAppraisalResult(){
        log.info("onCancelAppraisalResult::::  ");
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

    public ContactRecordDetailView getSelectContactRecordDetail() {
        return selectContactRecordDetail;
    }

    public void setSelectContactRecordDetail(ContactRecordDetailView selectContactRecordDetail) {
        this.selectContactRecordDetail = selectContactRecordDetail;
    }

    public AppraisalView getAppraisalView() {
        return appraisalView;
    }

    public void setAppraisalView(AppraisalView appraisalView) {
        this.appraisalView = appraisalView;
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

    public List<NewCollateralView> getNewCollateralViewList() {
        return newCollateralViewList;
    }

    public void setNewCollateralViewList(List<NewCollateralView> newCollateralViewList) {
        this.newCollateralViewList = newCollateralViewList;
    }

    public NewCollateralView getNewCollateralView() {
        return newCollateralView;
    }

    public void setNewCollateralView(NewCollateralView newCollateralView) {
        this.newCollateralView = newCollateralView;
    }

    public List<NewCollateralHeadView> getNewCollateralHeadViewList() {
        return newCollateralHeadViewList;
    }

    public void setNewCollateralHeadViewList(List<NewCollateralHeadView> newCollateralHeadViewList) {
        this.newCollateralHeadViewList = newCollateralHeadViewList;
    }

    public NewCollateralHeadView getNewCollateralHeadView() {
        return newCollateralHeadView;
    }

    public void setNewCollateralHeadView(NewCollateralHeadView newCollateralHeadView) {
        this.newCollateralHeadView = newCollateralHeadView;
    }

    public List<NewCollateralSubView> getNewCollateralSubViewList() {
        return newCollateralSubViewList;
    }

    public void setNewCollateralSubViewList(List<NewCollateralSubView> newCollateralSubViewList) {
        this.newCollateralSubViewList = newCollateralSubViewList;
    }

    public NewCollateralSubView getNewCollateralSubView() {
        return newCollateralSubView;
    }

    public void setNewCollateralSubView(NewCollateralSubView newCollateralSubView) {
        this.newCollateralSubView = newCollateralSubView;
    }

    public AppraisalData getAppraisalData() {
        return appraisalData;
    }

    public void setAppraisalData(AppraisalData appraisalData) {
        this.appraisalData = appraisalData;
    }

    public HeadCollateralData getHeadCollateralData() {
        return headCollateralData;
    }

    public void setHeadCollateralData(HeadCollateralData headCollateralData) {
        this.headCollateralData = headCollateralData;
    }

    public SubCollateralData getSubCollateralData() {
        return subCollateralData;
    }

    public void setSubCollateralData(SubCollateralData subCollateralData) {
        this.subCollateralData = subCollateralData;
    }

    public List<SubCollateralData> getSubCollateralDataList() {
        return subCollateralDataList;
    }

    public void setSubCollateralDataList(List<SubCollateralData> subCollateralDataList) {
        this.subCollateralDataList = subCollateralDataList;
    }

    public NewCollateralView getSelectCollateralDetailView() {
        return selectCollateralDetailView;
    }

    public void setSelectCollateralDetailView(NewCollateralView selectCollateralDetailView) {
        this.selectCollateralDetailView = selectCollateralDetailView;
    }

    public ContactRecordDetailView getContactRecordDetailViewTemp() {
        return contactRecordDetailViewTemp;
    }

    public void setContactRecordDetailViewTemp(ContactRecordDetailView contactRecordDetailViewTemp) {
        this.contactRecordDetailViewTemp = contactRecordDetailViewTemp;
    }

    public AppraisalCompany getAppraisalCompany() {
        return appraisalCompany;
    }

    public void setAppraisalCompany(AppraisalCompany appraisalCompany) {
        this.appraisalCompany = appraisalCompany;
    }

    public AppraisalDivision getAppraisalDivision() {
        return appraisalDivision;
    }

    public void setAppraisalDivision(AppraisalDivision appraisalDivision) {
        this.appraisalDivision = appraisalDivision;
    }

    public LocationProperty getLocationProperty() {
        return locationProperty;
    }

    public void setLocationProperty(LocationProperty locationProperty) {
        this.locationProperty = locationProperty;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public int getRowCollateral() {
        return rowCollateral;
    }

    public void setRowCollateral(int rowCollateral) {
        this.rowCollateral = rowCollateral;
    }

    public boolean isFlagReadOnly() {
        return flagReadOnly;
    }

    public void setFlagReadOnly(boolean flagReadOnly) {
        this.flagReadOnly = flagReadOnly;
    }

    //todo : for test
    private NewCollateralView newCollateralViewForTest(){
        newCollateralView = new NewCollateralView();

        newCollateralView.setJobID("111");
        newCollateralView.setJobIDSearch("111");
        newCollateralView.setAadDecision("AADDecision");
        newCollateralView.setAadDecisionReason("AadDecisionReason");
        newCollateralView.setAadDecisionReasonDetail("AadDecisionReasonDetail");
        newCollateralView.setAppraisalDate(new Date());
        newCollateralView.setUwDecision(DecisionType.NO_DECISION);
        newCollateralView.setBdmComments("BdmComments");

        newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        newCollateralHeadView = new NewCollateralHeadView();
        newCollateralHeadView.setAppraisalValue(BigDecimal.valueOf(55555));
        newCollateralHeadView.setExistingCredit(BigDecimal.valueOf(66666));

        newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        newCollateralSubView = new NewCollateralSubView();
        newCollateralSubView.setAddress("Address");
        newCollateralSubView.setCollateralOwner("sadfsadfsda");
        newCollateralSubViewList.add(newCollateralSubView);
        newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubViewList);
        newCollateralHeadViewList.add(newCollateralHeadView);
        newCollateralView.setNewCollateralHeadViewList(newCollateralHeadViewList);

        return newCollateralView;
    }
    private NewCollateralView newCollateralViewForTest2(){
        newCollateralView = new NewCollateralView();

        newCollateralView.setJobID("222");
        newCollateralView.setJobIDSearch("222");
        newCollateralView.setAadDecision("AADDecision");
        newCollateralView.setAadDecisionReason("AadDecisionReason");
        newCollateralView.setAadDecisionReasonDetail("AadDecisionReasonDetail");
        newCollateralView.setAppraisalDate(new Date());
        newCollateralView.setUwDecision(DecisionType.NO_DECISION);
        newCollateralView.setBdmComments("BdmComments");

        newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        newCollateralHeadView = new NewCollateralHeadView();
        newCollateralHeadView.setAppraisalValue(BigDecimal.valueOf(999999999));
        newCollateralHeadView.setExistingCredit(BigDecimal.valueOf(555555555));

        newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        newCollateralSubView = new NewCollateralSubView();
        newCollateralSubView.setAddress("Address");
        newCollateralSubView.setCollateralOwner("sadfsadfsda");
        newCollateralSubViewList.add(newCollateralSubView);
        newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubViewList);
        newCollateralHeadViewList.add(newCollateralHeadView);
        newCollateralView.setNewCollateralHeadViewList(newCollateralHeadViewList);

        return newCollateralView;
    }

}
