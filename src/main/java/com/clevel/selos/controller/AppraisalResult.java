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
    private boolean searchCOMS;
    int rowCollateral;

    private User user;
    private Long workCaseId;
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
        newCollateralView = new NewCollateralView();
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        init();

        HttpSession session = FacesUtil.getSession(true);
        user = (User)session.getAttribute("user");

        try{
            workCaseId = (Long)session.getAttribute("workCaseId");
        } catch (Exception e) {
//            workCaseId = 0L;
            workCaseId = 2001L;//for test.
        }
        workCaseId = 2L;//for test.
        log.info("-- workCaseId :: {} ",workCaseId);

        if(workCaseId != 0 ){
            appraisalView = appraisalResultControl.getAppraisalResult(workCaseId);
            if(appraisalView != null){
                newCollateralViewList = appraisalView.getNewCollateralViewList();


                if(newCollateralViewList != null){
                    for(NewCollateralView collateralView : newCollateralViewList){
                        newCollateralHeadViewList = collateralView.getNewCollateralHeadViewList();
                        if(newCollateralHeadViewList != null){
                            for(NewCollateralHeadView collateralHeadView : newCollateralHeadViewList){
                                newCollateralSubViewList = collateralHeadView.getNewCollateralSubViewList();
                                if(newCollateralSubViewList == null){
                                    newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
                                }
                            }
                        } else {
                            newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
                        }
                        newCollateralViewList.add(collateralView);
                    }
                } else {
//                    log.debug("-- newCollateralViewList is null and than create newCollateralViewList");
                    newCollateralViewList = new ArrayList<NewCollateralView>();
                }


                onSearchCollateral();
                data();

            } else {
                log.debug("-- appraisalView is null");

//                try {
//                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//                    ec.redirect(ec.getRequestContextPath() + "/site/appraisalRequest.jsf");
//                    return;
//                } catch (Exception ex) {
//                    log.info("Exception :: {}", ex);
//                }

                appraisalCompany = new AppraisalCompany();
                appraisalDivision = new AppraisalDivision();
                locationProperty = new LocationProperty();
                province = new Province();

                appraisalView = new AppraisalView();
                appraisalView.setAppraisalCompany(appraisalCompany);
                appraisalView.setAppraisalDivision(appraisalDivision);
                appraisalView.setLocationOfProperty(locationProperty);
                appraisalView.setProvinceOfProperty(province);
                showNoRequest = true;

                log.info("Click Button .show");
                RequestContext.getCurrentInstance().execute("btnShowNoRequest.click()");
                log.info("msgBoxNoRequestMessageDlg.show");
            }
        } else {
            log.debug("-- WorkCaseId not found.");
        }
    }

    private void data(){
        log.debug("-- Generate data.");
        appraisalData = new AppraisalData();
        appraisalData.setJobId("job 1");
        appraisalData.setAppraisalDate(DateTime.now().toDate());
        appraisalData.setAadDecision("ไม่ผ่าน");
        appraisalData.setAadDecisionReason("ดารากู้");
        appraisalData.setAadDecisionReasonDetail("bdm ไม่เป็น แฟนคลับผู้หญิง");

        List<HeadCollateralData> headCollateralDataList = new ArrayList<HeadCollateralData>();

        headCollateralData = new HeadCollateralData();
        headCollateralData.setCollateralLocation("ประเทศไทย แลน ออฟ สไมล์");
        headCollateralData.setTitleDeed("สส 126,ออ 156");
        headCollateralData.setAppraisalValue(BigDecimal.valueOf(200000));
        headCollateralDataList.add(headCollateralData);
        appraisalData.setHeadCollateralDataList(headCollateralDataList);

        subCollateralDataList = new ArrayList<SubCollateralData>();
        subCollateralData = new SubCollateralData();
        subCollateralData.setLandOffice("ขอนแก่น");
        subCollateralData.setAddress("ถนน ข้าวแนว จ ขอนแก่น");
        subCollateralData.setTitleDeed("สส 126");
        subCollateralData.setCollateralOwner("ญาญ่า ปันดอน");
        subCollateralData.setAppraisalValue(BigDecimal.valueOf(150000));
        subCollateralDataList.add(subCollateralData);

        subCollateralData = new SubCollateralData();

        subCollateralData.setTitleDeed("ออ 156");
        subCollateralData.setLandOffice("กทม");
        subCollateralData.setAddress("ถนน ข้าวหมาก จ กรุงเทพมหานคร");
        subCollateralData.setCollateralOwner("คิม เบอลิน");
        subCollateralData.setAppraisalValue(BigDecimal.valueOf(1000000));
        subCollateralDataList.add(subCollateralData);


        newCollateralViewList = new ArrayList<NewCollateralView>();
        newCollateralView = new NewCollateralView();

        appraisalData.setSubCollateralDataList(subCollateralDataList);

        convertCollateral(appraisalData);

        newCollateralHeadView = new NewCollateralHeadView();

        newCollateralHeadView.setTitleDeed("xx");
        newCollateralHeadView.setCollateralLocation( "yy");

        newCollateralSubViewList = new ArrayList<NewCollateralSubView>();

        newCollateralSubView = new NewCollateralSubView();
        newCollateralSubView.setTitleDeed("LN 159");
        newCollateralSubView.setLandOffice("london");
        newCollateralSubView.setAddress("Nothing Hill");
        newCollateralSubView.setCollateralOwner("Bill Bill Bang");
        newCollateralSubView.setAppraisalValue(new BigDecimal(1000000));
        newCollateralSubViewList.add(newCollateralSubView);

        newCollateralHeadView.setNewCollateralSubViewList(newCollateralSubViewList);

        newCollateralView.getNewCollateralHeadViewList().add(newCollateralHeadView);
        newCollateralViewList.add(newCollateralView);

//        for(CollateralDetailView detailView : collateralDetailViewList){
//            collateralHeaderDetailViewList = safetyList(detailView.getCollateralHeaderDetailViewList());
//            for(CollateralHeaderDetailView headerDetailView : collateralHeaderDetailViewList){
//                subCollateralDetailViewList = headerDetailView.getSubCollateralDetailViewList();
//            }
//        }

    }
    private <T> List<T> safetyList(List<T> list) {
        return Util.safetyList(list);
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
        newCollateralView = new NewCollateralView();
        searchCOMS = false;
    }
    public void onSaveCollateralDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        Cloner cloner = new Cloner();
        if(!searchCOMS){
            messageHeader = msg.get("app.appraisal.message.validate.header.fail");
            newCollateralView = new NewCollateralView();
            newCollateralView.setNewCollateralHeadViewList(new ArrayList<NewCollateralHeadView>());
            message = "ไม่สามารถบันทึกได้ เนื่องจากไม่พบข้อมูล AAD ";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        newCollateralViewList = new ArrayList<NewCollateralView>();
        newCollateralView = new NewCollateralView();

        log.info( "onSaveCollateralDetailView rowCollateral " + rowCollateral);
        if(ModeForButton.ADD.equals(modeForButton)){
            complete=true;
            log.info("onSaveCollateralDetailView add >>> begin ");
            log.info("newCollateralViewList size >>> is " + newCollateralViewList.size());
//            newCollateralView.setNo(newCollateralViewList.size()+1);       TODO : to check setNo????
            newCollateralViewList.add(newCollateralView);
            log.info("onSaveCollateralDetailView add >>> end ");
        }else if(ModeForButton.EDIT.equals(modeForButton)){
            complete=true;
//            CollateralDetailView collateralDetailViewOnRow = collateralDetailViewList.get(rowCollateral-1);
//            log.info("onSaveCollateralDetailView edit >>> begin ");
//            //collateralDetailView.setAADDecision("set decision at EDIT Dialog");
//            //collateralDetailViewOnRow  = cloner.deepClone(collateralDetailView);
//            collateralDetailView.setAADDecision(collateralDetailView.getAADDecision());
//            collateralDetailView.setAADDecisionReason(collateralDetailView.getAADDecisionReason());
//            collateralDetailView.setAADDecisionReasonDetail(collateralDetailView.getAADDecisionReasonDetail());
//            collateralDetailView.setMortgageCondition(collateralDetailView.getMortgageCondition());
//            collateralDetailView.setMortgageConditionDetail(collateralDetailView.getMortgageConditionDetail());
//
//            collateralDetailViewOnRow.setJobID(collateralDetailView.getJobID());
//            collateralDetailViewOnRow.setAppraisalDate(collateralDetailView.getAppraisalDate());
//            collateralDetailViewOnRow.setAADDecision(collateralDetailView.getAADDecision());
//            collateralDetailViewOnRow.setAADDecisionReason(collateralDetailView.getAADDecisionReason());
//            collateralDetailViewOnRow.setAADDecisionReasonDetail(collateralDetailView.getAADDecisionReasonDetail());
//            collateralDetailViewOnRow.setUsage(collateralDetailView.getUsage());
//            collateralDetailViewOnRow.setTypeOfUsage(collateralDetailView.getTypeOfUsage());
//            collateralDetailViewOnRow.setMortgageCondition(collateralDetailView.getMortgageCondition());
//            collateralDetailViewOnRow.setMortgageConditionDetail(collateralDetailView.getMortgageConditionDetail());
//
//            collateralDetailViewOnRow.setCollateralHeaderDetailViewList(collateralDetailView.getCollateralHeaderDetailViewList());

            log.info("onSaveCollateralDetailView edit >>> begin ");

        }

        context.addCallbackParam("functionComplete", complete);
    }
    public void onEditCollateralDetailView(){
//        log.info(" onEditCollateralDetailView " + collateralDetailViewList.size());
//        modeForButton = ModeForButton.EDIT;
//        collateralDetailView = new CollateralDetailView();
//        //*** Check list size ***//
//        rowCollateral =  selectCollateralDetailView.getNo();
//        log.info( "onEditCollateralDetailView rowCollateral " + rowCollateral);
//        if( rowIndex < collateralDetailViewList.size() ) {
//
//            collateralDetailView.setJobID(selectCollateralDetailView.getJobID());
//            collateralDetailView.setAppraisalDate(selectCollateralDetailView.getAppraisalDate());
//            collateralDetailView.setAADDecision(selectCollateralDetailView.getAADDecision());
//            collateralDetailView.setAADDecisionReason(selectCollateralDetailView.getAADDecisionReason());
//            collateralDetailView.setAADDecisionReasonDetail(selectCollateralDetailView.getAADDecisionReasonDetail());
//            collateralDetailView.setUsage(selectCollateralDetailView.getUsage());
//            collateralDetailView.setTypeOfUsage(selectCollateralDetailView.getTypeOfUsage());
//            collateralDetailView.setMortgageCondition(selectCollateralDetailView.getMortgageCondition());
//            collateralDetailView.setMortgageConditionDetail(selectCollateralDetailView.getMortgageConditionDetail());
//
//            collateralDetailView.setCollateralHeaderDetailViewList(selectCollateralDetailView.getCollateralHeaderDetailViewList());
//        }
    }

    public void onDeleteCollateralDetailView(){
//        log.info(" onDeleteCollateralDetailView " + collateralDetailViewList.size());
//        collateralDetailViewList.remove(selectCollateralDetailView);
//        onSetRowNoCollateralDetailView();
//        log.info( " onDeleteCollateralDetailView end ");
    }

    public void onSetRowNoCollateralDetailView(){
//        CollateralDetailView collateralDetailViewRow;
//        for(int i=0;i< collateralDetailViewList.size();i++){
//            collateralDetailViewRow = collateralDetailViewList.get(i);
//            collateralDetailViewRow.setNo(i+1);
//        }
    }

    public void onSaveAppraisalResult() {
        log.info("onSaveAppraisalResult::::");
//        log.info("collateralDetailViewList.size()        ::: {} ", collateralDetailViewList.size());
        try{
            if(appraisalView.getId() == 0){
                appraisalView.setCreateBy(user);
                appraisalView.setCreateDate(DateTime.now().toDate());
            }
            appraisalView.setModifyBy(user);
//            appraisalView.setCollateralDetailViewList(collateralDetailViewList);

            appraisalResultControl.onSaveAppraisalResult(appraisalView, workCaseId);
            messageHeader = msg.get("app.appraisal.result.message.header.save.success");
            message = msg.get("app.appraisal.result.message.body.save.success");
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
    public void onChangeReceivedTaskDate(){
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


    public void onSetRowNoHeaderCollaral(List<NewCollateralHeadView> collateralHeaderDetailViewList){
//        CollateralHeaderDetailView collateralHeaderDetailView;
//        for(int h=0;h<collateralHeaderDetailViewList.size();h++){
//            collateralHeaderDetailView = collateralHeaderDetailViewList.get(h);
//            collateralHeaderDetailView.setNo(h+1);
//        }
    }

    public void onSetRowNoSubCollaral(List<NewCollateralSubView> subCollateralDetailViewList){
//        SubCollateralDetailView subCollateralDetailView;
//        for(int s=0;s<subCollateralDetailViewList.size();s++){
//            subCollateralDetailView = subCollateralDetailViewList.get(s);
//            subCollateralDetailView.setNo(s+1);
//        }
    }

    public void onSearchCollateral() {
//        log.info("onSearchCollateral begin key is " +collateralDetailView.getJobIDSearch() );

        ///Call ComS
        //DataComes
        //COMSInterface
        //
        String jobId;
        jobId = "";//collateralDetailView.getJobIDSearch();
        AppraisalDataResult appraisalDataResult;
        log.info("userId is   " + user.getId() + "      jobId is  " + jobId);
        try{
            log.info("begin coms ");
            appraisalDataResult = comsInterface.getAppraisalData(user.getId(),jobId);
            log.info("end coms ");
            searchCOMS = true;
//            collateralDetailView = collateralBizTransform.transformCollteral(appraisalDataResult);
//
//            for(int i=0;i<collateralDetailView.getCollateralHeaderDetailViewList().size();i++){
//                onSetRowNoHeaderCollaral(collateralDetailView.getCollateralHeaderDetailViewList());
//
//                for(int j=0;j<collateralDetailView.getCollateralHeaderDetailViewList().get(i).getSubCollateralDetailViewList().size();j++){
//                    onSetRowNoSubCollaral(collateralDetailView.getCollateralHeaderDetailViewList().get(i).getSubCollateralDetailViewList());
//                }
//
//            }




        }catch (COMSInterfaceException comsEx){
            log.info("error comsEx ");
            searchCOMS = false;
            messageHeader = msg.get("app.appraisal.message.validate.header.fail");
            message = comsEx.getMessage() + " JOB ID " + jobId ;
//            collateralDetailView = new CollateralDetailView();
//            collateralDetailView.setCollateralHeaderDetailViewList(new ArrayList<CollateralHeaderDetailView>());
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }
        /*log.info("getData From COMS begin");
        appraisalData = new AppraisalData();
        appraisalData.setJobId(collateralDetailView.getJobID());
        appraisalData.setAppraisalDate(DateTime.now().toDate());
        appraisalData.setAadDecision("ผ่าน");
        appraisalData.setAadDecisionReason("ดารากู้");
        appraisalData.setAadDecisionReasonDetail("bdm เป็น แฟนคลับ");

        headCollateralData = new HeadCollateralData();
        headCollateralData.setCollateralLocation("ประเทศไทย แลน ออฟ สไมล์");
        headCollateralData.setTitleDeed("กค 126,ญก 156");
        headCollateralData.setAppraisalValue("4810000");
        appraisalData.setHeadCollateralData(headCollateralData);

        subCollateralDataList = new ArrayList<SubCollateralData>();
        subCollateralData = new SubCollateralData();
        subCollateralData.setLandOffice("ขอนแก่น");
        subCollateralData.setAddress("ถนน ข้าวแนว จ ขอนแก่น");
        subCollateralData.setTitleDeed("กค 126");
        subCollateralData.setCollateralOwner("แบรี่ ณเดช");
        subCollateralData.setAppraisalValue(new BigDecimal(3810000));
        subCollateralDataList.add(subCollateralData);

        subCollateralData = new SubCollateralData();

        subCollateralData.setTitleDeed("ญก 156");
        subCollateralData.setLandOffice("กทม");
        subCollateralData.setAddress("ถนน ข้าวหมาก จ กรุงเทพมหานคร");
        subCollateralData.setCollateralOwner("หมาก ปริญ");
        subCollateralData.setAppraisalValue(new BigDecimal(1000000));
        subCollateralDataList.add(subCollateralData);

        appraisalData.setSubCollateralDataList(subCollateralDataList);

        log.info("getData From COMS end");

        log.info("getData convert ComS to View Model Begin");
        collateralDetailView =  convertCollateral(appraisalData);
        log.info("getData convert ComS to View Model End");

        log.info("onSearchCollateral End");*/

    }
    private NewCollateralView convertCollateral(AppraisalData appraisalData ){
        log.info("convertCollateral begin");
        /*collateralDetailView = new CollateralDetailView();
        collateralDetailView.setJobID(appraisalData.getJobId());
        collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
        collateralDetailView.setAADDecision(appraisalData.getAadDecision());
        collateralDetailView.setAADDecisionReason(appraisalData.getAadDecisionReason());
        collateralDetailView.setAADDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
        collateralDetailView.setUsage(appraisalData.getUsage());
        collateralDetailView.setTypeOfUsage(appraisalData.getTypeOfUsage());
        collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
        collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

        List<CollateralHeaderDetailView> collateralHeadDetailViewList = new ArrayList<CollateralHeaderDetailView>();
        HeadCollateralData headCollateralData = appraisalData.getHeadCollateralData();
        CollateralHeaderDetailView collateralHeadDetailView = convertCollateralHeader(headCollateralData);
        collateralHeadDetailViewList.add(collateralHeadDetailView);

        List<SubCollateralData> SubCollateralDataList = appraisalData.getSubCollateralDataList();
        List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();

        for(int i=0;i< collateralHeadDetailViewList.size();i++){
            CollateralHeaderDetailView collateralHeadDetailViewTemp = collateralHeadDetailViewList.get(i);
            collateralHeadDetailViewTemp.setNo(i+1);

            for(int j= 0;j<SubCollateralDataList.size();j++){
                SubCollateralDetailView subCollateralDetailView = convertSubCollateral(SubCollateralDataList.get(j));
                subCollateralDetailView.setNo(j+1);
                subCollateralDetailView.getSubCollateralType().setCollateralType(collateralHeadDetailViewTemp.getHeadCollType());
                SubCollateralType  subCollateralTypeResult = subCollateralTypeDAO.findByBySubColCode(subCollateralDetailView.getSubCollateralType());
                log.info("subCollateralTypeDAO.findByBySubColCode ID is " + subCollateralTypeResult.getId());
                subCollateralDetailView.setSubCollateralType(subCollateralTypeResult);
                subCollateralDetailViewList.add(subCollateralDetailView);
            }
            collateralHeadDetailViewTemp.setSubCollateralDetailViewList(subCollateralDetailViewList);
        }

        collateralDetailView.setCollateralHeaderDetailViewList(collateralHeadDetailViewList);
        log.info("convertCollateral end");*/
        return null;
    }

    private NewCollateralHeadView convertCollateralHeader(HeadCollateralData headCollateralData ){
        log.info("convertCollateralHeader begin");
        /*CollateralHeaderDetailView collateralHeaderDetailView = new CollateralHeaderDetailView();

        collateralHeadDetailView.setTitleDeed(headCollateralData.getTitleDeed());
        double appraisalValue = Double.parseDouble(headCollateralData.getAppraisalValue());
        collateralHeadDetailView.setAppraisalValue(new BigDecimal(appraisalValue));
        collateralHeadDetailView.setCollateralLocation(headCollateralData.getCollateralLocation());
        CollateralType headCollType = new CollateralType();
        if(headCollateralData.getHeadCollType()== null || headCollateralData.getHeadCollType().equals("")){
            headCollType.setCode("286003");
        }else{
            headCollType.setCode(headCollateralData.getHeadCollType());
        }

        headCollType = collateralTypeDAO.findByCollateralCode(headCollType);
        log.info("collateralTypeDAO.findByCollateralCode ID is " + headCollType.getId());
        collateralHeadDetailView.setHeadCollType(headCollType);
        log.info("convertCollateralHeader end");*/

        //todo : change this , AS
        return null;
    }

    private NewCollateralSubView convertSubCollateral(SubCollateralData subCollateralData ){
        log.info("convertSubCollateral begin");
        /*SubCollateralDetailView subCollateralDetailView = new SubCollateralDetailView();

        subCollateralDetailView.setTitleDeed(subCollateralData.getTitleDeed());
        subCollateralDetailView.setAppraisalValue(subCollateralData.getAppraisalValue());
        subCollateralDetailView.setAddress(subCollateralData.getAddress());
        subCollateralDetailView.setLandOffice(subCollateralData.getLandOffice());
        subCollateralDetailView.setCollateralOwnerAAD(subCollateralData.getCollateralOwner());
        SubCollateralType subCollType = new SubCollateralType();

        if(subCollateralData.getCollateralType()==null || subCollateralData.getCollateralType().equals("")){
            subCollType.setCode("00");
        }else{
            subCollType.setCode(subCollateralData.getCollateralType());
        }

        subCollateralDetailView.setSubCollateralType(subCollType);
        log.info("convertSubCollateral end");*/
        return null;
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
}
