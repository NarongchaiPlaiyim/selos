package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.AppraisalResultControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
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

    private String modeForButton;
    private int rowIndex;
    private String messageHeader;
    private String message;
    private boolean showNoRequest;
    private Date currentDate;
    int rowCollateral;

    private User user;
    private Long workCaseId;
    private AppraisalView appraisalView;
    private CollateralDetailView collateralDetailView;



    private List<CollateralDetailView> collateralDetailViewList;
    private List<CollateralHeaderDetailView> collateralHeadDetailViewList;
    private List<SubCollateralDetailView> subCollateralDetailViewList;

    private CollateralHeaderDetailView collateralHeadDetailView;

    AppraisalData appraisalData;
    HeadCollateralData headCollateralData;
    SubCollateralData subCollateralData;
    List<SubCollateralData> subCollateralDataList;

    CollateralHeaderDetailView collateralHeaderDetailView;
    SubCollateralDetailView subCollateralDetailView;

    private CollateralDetailView selectCollateralDetailView;
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


    public AppraisalResult() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        modeForButton = "add";

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 10001);
        user = (User)session.getAttribute("user");

        collateralDetailView = new CollateralDetailView();

        appraisalCompanyList = appraisalCompanyDAO.findAll();
        appraisalDivisionList= appraisalDivisionDAO.findAll();
        locationPropertyList= locationPropertyDAO.findAll();
        provinceList= provinceDAO.findAll();
        collateralDetailViewList = new ArrayList<CollateralDetailView>();
        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
            appraisalView = appraisalResultControl.getAppraisalResultByWorkCase(workCaseId);

            if(appraisalView != null){
                log.info("appraisalView after search :: not null" + (appraisalView == null));

                collateralDetailViewList = appraisalView.getCollateralDetailViewList();
                log.info("collateralDetailViewList  :::::::::::: {} ", collateralDetailViewList);
                if(collateralDetailViewList == null){
                    collateralDetailViewList = new ArrayList<CollateralDetailView>();
                }else{
                    for(int i=0;i<collateralDetailViewList.size();i++){
                        collateralDetailView = collateralDetailViewList.get(i);
                        collateralHeadDetailViewList = collateralDetailView.getCollateralHeaderDetailViewList();
                        if(collateralHeadDetailViewList == null){
                            collateralHeadDetailViewList = new ArrayList<CollateralHeaderDetailView>();
                        }else{
                            for(int j=0;j<collateralHeadDetailViewList.size();j++){
                                collateralHeadDetailView = collateralHeadDetailViewList.get(j);
                                subCollateralDetailViewList = collateralHeadDetailView.getSubCollateralDetailViewList();
                            }
                            if(subCollateralDetailViewList == null){
                                subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();
                            }
                        }
                    }
                }
                /*
                onSearchCollateral();

                CollateralDetailView collateralDetailViewTemp;
                CollateralHeaderDetailView collateralHeaderDetailViewTemp;
                collateralDetailViewList = new ArrayList<CollateralDetailView>();
                collateralDetailViewList.add(collateralDetailView);

                appraisalData = new AppraisalData();
                appraisalData.setJobId("job 1");
                appraisalData.setAppraisalDate(DateTime.now().toDate());
                appraisalData.setAadDecision("ไม่ผ่าน");
                appraisalData.setAadDecisionReason("ดารากู้");
                appraisalData.setAadDecisionReasonDetail("bdm ไม่เป็น แฟนคลับผู้หญิง");

                headCollateralData = new HeadCollateralData();
                headCollateralData.setCollateralLocation("ประเทศไทย แลน ออฟ สไมล์");
                headCollateralData.setTitleDeed("สส 126,ออ 156");
                headCollateralData.setAppraisalValue("200000");
                appraisalData.setHeadCollateralData(headCollateralData);

                subCollateralDataList = new ArrayList<SubCollateralData>();
                subCollateralData = new SubCollateralData();
                subCollateralData.setLandOffice("ขอนแก่น");
                subCollateralData.setAddress("ถนน ข้าวแนว จ ขอนแก่น");
                subCollateralData.setTitleDeed("สส 126");
                subCollateralData.setCollateralOwner("ญาญ่า ปันดอน");
                subCollateralData.setAppraisalValue(new BigDecimal(150000));
                subCollateralDataList.add(subCollateralData);

                subCollateralData = new SubCollateralData();

                subCollateralData.setTitleDeed("ออ 156");
                subCollateralData.setLandOffice("กทม");
                subCollateralData.setAddress("ถนน ข้าวหมาก จ กรุงเทพมหานคร");
                subCollateralData.setCollateralOwner("คิม เบอลิน");
                subCollateralData.setAppraisalValue(new BigDecimal(1000000));
                subCollateralDataList.add(subCollateralData);

                appraisalData.setSubCollateralDataList(subCollateralDataList);

                convertCollateral(appraisalData);

                CollateralHeaderDetailView collateralHeaderDetailView = new CollateralHeaderDetailView();

                collateralHeaderDetailView.setTitleDeed("xx");
                collateralHeaderDetailView.setCollateralLocation( "yy");

                subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();
                subCollateralDetailView = new SubCollateralDetailView();

                subCollateralDetailView.setTitleDeed("LN 159");
                subCollateralDetailView.setLandOffice("london");
                subCollateralDetailView.setAddress("Nothing Hill");
                subCollateralDetailView.setCollateralOwner("Bill Bill Bang");
                subCollateralDetailView.setAppraisalValue(new BigDecimal(1000000));
                subCollateralDetailViewList.add(subCollateralDetailView);

                collateralHeaderDetailView.setSubCollateralDetailViewList(subCollateralDetailViewList);

                collateralDetailView.getCollateralHeaderDetailViewList().add(collateralHeaderDetailView);

                collateralDetailViewList.add(collateralDetailView);


                for(int i=0; i<collateralDetailViewList.size();i++){
                    collateralDetailViewTemp = collateralDetailViewList.get(i);
                    collateralHeadDetailViewList = collateralDetailViewTemp.getCollateralHeaderDetailViewList();

                    for(int j=0; j<collateralHeadDetailViewList.size();j++){
                        collateralHeaderDetailViewTemp = collateralHeadDetailViewList.get(j);
                        subCollateralDetailViewList = collateralHeaderDetailViewTemp.getSubCollateralDetailViewList();
                    }
                }
                */

            }else{

                try {
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath() + "/site/appraisalRequest.jsf");
                    return;
                } catch (Exception ex) {
                    log.info("Exception :: {}", ex);
                }

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

                log.info("appraisalView after search :: {} is null "  + (appraisalView == null));

                log.info("Click Button .show");
                RequestContext.getCurrentInstance().execute("btnShowNoRequest.click()");
                log.info("msgBoxNoRequestMessageDlg.show");
            }

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
            messageHeader = msg.get("app.customerAcceptance.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.customerAcceptance.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.customerAcceptance.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }



    public void onAddCollateralDetailView(){
        log.info("onAddCollateralDetailView >>> begin ");
        collateralDetailView = new CollateralDetailView();
        modeForButton = "add";
    }

    public void onSaveCollateralDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        Cloner cloner = new Cloner();


        log.info( "onSaveCollateralDetailView rowCollateral " + rowCollateral);
        if(modeForButton.equalsIgnoreCase("add")){
            complete=true;
            log.info("onSaveCollateralDetailView add >>> begin ");
            log.info("collateralDetailViewList size >>> is " + collateralDetailViewList.size());
            collateralDetailView.setNo(collateralDetailViewList.size()+1);
            collateralDetailViewList.add(collateralDetailView);
            log.info("onSaveCollateralDetailView add >>> end ");
        }else if(modeForButton.equalsIgnoreCase("edit")){
            complete=true;
            CollateralDetailView collateralDetailViewOnRow = collateralDetailViewList.get(rowCollateral-1);
            log.info("onSaveCollateralDetailView edit >>> begin ");
            //collateralDetailView.setAADDecision("set decision at EDIT Dialog");
            //collateralDetailViewOnRow  = cloner.deepClone(collateralDetailView);
            collateralDetailView.setAADDecision("decision");
            collateralDetailView.setAADDecisionReason("reason 1");
            collateralDetailView.setAADDecisionReasonDetail("reason 2");
            collateralDetailView.setMortgageCondition("mortgage 1 ");
            collateralDetailView.setMortgageConditionDetail("mortgage 2");

            collateralDetailViewOnRow.setJobID(collateralDetailView.getJobID());
            collateralDetailViewOnRow.setAppraisalDate(collateralDetailView.getAppraisalDate());
            collateralDetailViewOnRow.setAADDecision(collateralDetailView.getAADDecision());
            collateralDetailViewOnRow.setAADDecisionReason(collateralDetailView.getAADDecisionReason());
            collateralDetailViewOnRow.setAADDecisionReasonDetail(collateralDetailView.getAADDecisionReasonDetail());
            collateralDetailViewOnRow.setUsage(collateralDetailView.getUsage());
            collateralDetailViewOnRow.setTypeOfUsage(collateralDetailView.getTypeOfUsage());
            collateralDetailViewOnRow.setMortgageCondition(collateralDetailView.getMortgageCondition());
            collateralDetailViewOnRow.setMortgageConditionDetail(collateralDetailView.getMortgageConditionDetail());

            collateralDetailViewOnRow.setCollateralHeaderDetailViewList(collateralDetailView.getCollateralHeaderDetailViewList());

            log.info("onSaveCollateralDetailView edit >>> begin ");

        }

        context.addCallbackParam("functionComplete", complete);
    }

    public void onEditCollateralDetailView(){
        log.info( " onEditCollateralDetailView " + collateralDetailViewList.size());
        modeForButton = "edit";
        collateralDetailView = new CollateralDetailView();
        //*** Check list size ***//
        rowCollateral =  selectCollateralDetailView.getNo();
        log.info( "onEditCollateralDetailView rowCollateral " + rowCollateral);
        if( rowIndex < collateralDetailViewList.size() ) {

            selectCollateralDetailView.setAADDecision("set decision at Edit on Row ");
            selectCollateralDetailView.setAADDecisionReason("set decision Reason at Edit on Row ");

            /*collateralDetailView.setAADDecision(selectCollateralDetailView.getAADDecision());
            collateralDetailView.setAADDecisionReason(selectCollateralDetailView.getAADDecisionReason());
            collateralDetailView.setAADDecisionReasonDetail(selectCollateralDetailView.getAADDecisionReasonDetail());*/

            collateralDetailView.setMortgageCondition("Mort 1");
            collateralDetailView.setMortgageConditionDetail("Mort Detail");

            collateralDetailView.setJobID(selectCollateralDetailView.getJobID());
            collateralDetailView.setAppraisalDate(selectCollateralDetailView.getAppraisalDate());
            collateralDetailView.setAADDecision(selectCollateralDetailView.getAADDecision());
            collateralDetailView.setAADDecisionReason(selectCollateralDetailView.getAADDecisionReason());
            collateralDetailView.setAADDecisionReasonDetail(selectCollateralDetailView.getAADDecisionReasonDetail());
            collateralDetailView.setUsage(selectCollateralDetailView.getUsage());
            collateralDetailView.setTypeOfUsage(selectCollateralDetailView.getTypeOfUsage());
            collateralDetailView.setMortgageCondition(selectCollateralDetailView.getMortgageCondition());
            collateralDetailView.setMortgageConditionDetail(selectCollateralDetailView.getMortgageConditionDetail());

            collateralDetailView.setCollateralHeaderDetailViewList(selectCollateralDetailView.getCollateralHeaderDetailViewList());
        }
    }

    public void onDeleteCollateralDetailView(){
        log.info( " onDeleteCollateralDetailView " + collateralDetailViewList.size());
        collateralDetailViewList.remove(selectCollateralDetailView);
        onSetRowNoCollateralDetailView();
        log.info( " onDeleteCollateralDetailView end ");
    }

    public void onSetRowNoCollateralDetailView(){
        CollateralDetailView collateralDetailViewRow;
        for(int i=0;i< collateralDetailViewList.size();i++){
            collateralDetailViewRow = collateralDetailViewList.get(i);
            collateralDetailViewRow.setNo(i+1);
        }
    }

    public void onSaveAppraisalResult() {
        log.info("onSaveAppraisalResult::::");
        log.info("collateralDetailViewList.size()        ::: {} ", collateralDetailViewList.size());
        try{
            if(appraisalView.getId() == 0){
                appraisalView.setCreateBy(user);
                appraisalView.setCreateDate(DateTime.now().toDate());
            }
            appraisalView.setModifyBy(user);
            appraisalView.setCollateralDetailViewList(collateralDetailViewList);

            appraisalResultControl.onSaveAppraisalResult(appraisalView, workCaseId);
            messageHeader = msg.get("app.customerAcceptance.message.header.save.success");
            message = msg.get("app.customerAcceptance.message.body.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.customerAcceptance.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.customerAcceptance.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.customerAcceptance.message.body.save.fail") + ex.getMessage();
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

    public CollateralDetailView getCollateralDetailView() {
        return collateralDetailView;
    }

    public void setCollateralDetailView(CollateralDetailView collateralDetailView) {
        this.collateralDetailView = collateralDetailView;
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

    public void onSearchCollateral() {
        log.info("onSearchCollateral begin key is " +collateralDetailView.getJobID() );

        ///Call ComS
        //DataComes
        //COMSInterface
        //
        log.info("getData From COMS begin");
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

        log.info("onSearchCollateral End");

    }
    private CollateralDetailView convertCollateral(AppraisalData appraisalData ){
        log.info("convertCollateral begin");
        collateralDetailView = new CollateralDetailView();
        collateralDetailView.setJobID(appraisalData.getJobId());
        collateralDetailView.setAppraisalDate(appraisalData.getAppraisalDate());
        collateralDetailView.setAADDecision(appraisalData.getAadDecision());
        collateralDetailView.setAADDecisionReason(appraisalData.getAadDecisionReason());
        collateralDetailView.setAADDecisionReasonDetail(appraisalData.getAadDecisionReasonDetail());
        collateralDetailView.setUsage(appraisalData.getUsage());
        collateralDetailView.setTypeOfUsage(appraisalData.getTypeOfUsage());
        collateralDetailView.setMortgageCondition(appraisalData.getMortgageCondition());
        collateralDetailView.setMortgageConditionDetail(appraisalData.getMortgageConditionDetail());

        List<CollateralHeaderDetailView> collateralHeaderDetailViewList = new ArrayList<CollateralHeaderDetailView>();
        HeadCollateralData headCollateralData = appraisalData.getHeadCollateralData();
        CollateralHeaderDetailView collateralHeaderDetailView = convertCollateralHeader(headCollateralData);
        collateralHeaderDetailViewList.add(collateralHeaderDetailView);

        List<SubCollateralData> SubCollateralDataList = appraisalData.getSubCollateralDataList();
        List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();

        for(int i=0;i<collateralHeaderDetailViewList.size();i++){
            CollateralHeaderDetailView collateralHeaderDetailViewTemp = collateralHeaderDetailViewList.get(i);
            collateralHeaderDetailViewTemp.setNo(i+1);

            for(int j= 0;j<SubCollateralDataList.size();j++){
                SubCollateralDetailView subCollateralDetailView = convertSubCollateral(SubCollateralDataList.get(j));
                subCollateralDetailView.setNo(j+1);
                subCollateralDetailView.getSubCollateralType().setCollateralType(collateralHeaderDetailViewTemp.getHeadCollType());
                SubCollateralType  subCollateralTypeResult = subCollateralTypeDAO.findByBySubColCode(subCollateralDetailView.getSubCollateralType());
                log.info("subCollateralTypeDAO.findByBySubColCode ID is " + subCollateralTypeResult.getId());
                subCollateralDetailView.setSubCollateralType(subCollateralTypeResult);
                subCollateralDetailViewList.add(subCollateralDetailView);
            }
            collateralHeaderDetailViewTemp.setSubCollateralDetailViewList(subCollateralDetailViewList);
        }

        collateralDetailView.setCollateralHeaderDetailViewList(collateralHeaderDetailViewList);
        log.info("convertCollateral end");
        return collateralDetailView;
    }

    private CollateralHeaderDetailView convertCollateralHeader(HeadCollateralData headCollateralData ){
        log.info("convertCollateralHeader begin");
        CollateralHeaderDetailView collateralHeaderDetailView = new CollateralHeaderDetailView();

        collateralHeaderDetailView.setTitleDeed(headCollateralData.getTitleDeed());
        double appraisalValue = Double.parseDouble(headCollateralData.getAppraisalValue());
        collateralHeaderDetailView.setAppraisalValue(new BigDecimal(appraisalValue));
        collateralHeaderDetailView.setCollateralLocation(headCollateralData.getCollateralLocation());
        CollateralType headCollType = new CollateralType();
        if(headCollateralData.getHeadCollType()== null || headCollateralData.getHeadCollType().equals("")){
            headCollType.setCode("286003");
        }else{
            headCollType.setCode(headCollateralData.getHeadCollType());
        }

        headCollType = collateralTypeDAO.findByCollateralCode(headCollType);
        log.info("collateralTypeDAO.findByCollateralCode ID is " + headCollType.getId());
        collateralHeaderDetailView.setHeadCollType(headCollType);
        log.info("convertCollateralHeader end");
        return collateralHeaderDetailView;
    }

    private SubCollateralDetailView convertSubCollateral(SubCollateralData subCollateralData ){
        log.info("convertSubCollateral begin");
        SubCollateralDetailView subCollateralDetailView = new SubCollateralDetailView();

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
        log.info("convertSubCollateral end");
        return subCollateralDetailView;
    }

    public List<CollateralDetailView> getCollateralDetailViewList() {
        return collateralDetailViewList;
    }

    public void setCollateralDetailViewList(List<CollateralDetailView> collateralDetailViewList) {
        this.collateralDetailViewList = collateralDetailViewList;
    }

    public List<CollateralHeaderDetailView> getCollateralHeadDetailViewList() {
        return collateralHeadDetailViewList;
    }

    public void setCollateralHeadDetailViewList(List<CollateralHeaderDetailView> collateralHeadDetailViewList) {
        this.collateralHeadDetailViewList = collateralHeadDetailViewList;
    }

    public List<SubCollateralDetailView> getSubCollateralDetailViewList() {
        return subCollateralDetailViewList;
    }

    public void setSubCollateralDetailViewList(List<SubCollateralDetailView> subCollateralDetailViewList) {
        this.subCollateralDetailViewList = subCollateralDetailViewList;
    }

    public CollateralDetailView getSelectCollateralDetailView() {
        return selectCollateralDetailView;
    }

    public void setSelectCollateralDetailView(CollateralDetailView selectCollateralDetailView) {
        this.selectCollateralDetailView = selectCollateralDetailView;
    }
}
