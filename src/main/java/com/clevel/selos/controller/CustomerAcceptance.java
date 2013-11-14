package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CustomerAcceptanceControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "customerAcceptance")
public class CustomerAcceptance implements Serializable {

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
    private String modeForButton;
    private int rowIndex;
    private String messageHeader;
    private String message;
    private Date currentDate;

    private User user;

    private Long workCaseId;
    private CustomerAcceptanceView customerAcceptanceView;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private ContactRecordDetailView contactRecordDetailView;

    private ContactRecordDetailView selectContactRecordDetail;
    private ContactRecordDetailView contactRecordDetailViewTemp;

    @Inject
    private CustomerAcceptanceControl customerAcceptanceControl;
    @Inject
    private UserDAO userDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;

    public CustomerAcceptance() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        modeForButton = "add";

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 10001);
        contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();


        contactRecordDetailView = new ContactRecordDetailView();

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
            customerAcceptanceView = customerAcceptanceControl.getCustomerAcceptanceByWorkCase(workCaseId); // find NCB by customer
            if(customerAcceptanceView != null){
                contactRecordDetailViewList = customerAcceptanceControl.getContactRecordViewList(workCaseId);
                log.info("contactRecordDetailViewList  :::::::::::: {} ", contactRecordDetailViewList);
                if(contactRecordDetailViewList == null){
                    contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
                }
                setStrOnDataTable();
                log.info("customerAcceptanceView.getApproveResult is   :::::::::::: {} "+customerAcceptanceView.getApproveResult());

            }else{
                customerAcceptanceView = new CustomerAcceptanceView();
            }
        }
    }

    private void setStrOnDataTable(){
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

            if(contactRecordDetailViewForStr.getReason()==1){
                contactRecordDetailViewForStr.setReasonStr("ลูกค้าแจ้งนัดกับ BDM แล้ว");
            }else if(contactRecordDetailViewForStr.getReason()==2){
                contactRecordDetailViewForStr.setReasonStr("ไม่สามารถติดต่อได้");
            }else if(contactRecordDetailViewForStr.getReason()==3){
                contactRecordDetailViewForStr.setReasonStr("ลูกค้ายังไม่รับนัด");
            }else if(contactRecordDetailViewForStr.getReason()==4){
                contactRecordDetailViewForStr.setReasonStr("ลูกค้าไม่สะดวก");
            }else if(contactRecordDetailViewForStr.getReason()==5){
                contactRecordDetailViewForStr.setReasonStr("ลูกค้าขอติดต่อ BDM ก่อน");
            }else if(contactRecordDetailViewForStr.getReason()==6){
                contactRecordDetailViewForStr.setReasonStr("ลูกค้ารอเงินที่จะเป็นหลักประกัน");
            }

        }
    }

    public void onSaveContactRecordDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if(true){
            complete = true;
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveContactRecordDetailView add >>> begin ");
                log.info("contactRecordDetailViewList size >>> is " + contactRecordDetailViewList.size());
                contactRecordDetailView.setNo(contactRecordDetailViewList.size()+1);
                log.info("onSaveContactRecordDetailView contactRecordDetailView >>> " + contactRecordDetailView);
                contactRecordDetailViewList.add(contactRecordDetailView);

                log.info("onSaveContactRecordDetailView add >>> end ");

            }else if(modeForButton.equalsIgnoreCase("edit")){
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
        context.addCallbackParam("functionComplete", complete);
    }
    
    public void onEditContactRecordDetailView(){
        log.info( " onEditBizProductDetailView getRemark is " + selectContactRecordDetail.getRemark());
        modeForButton = "edit";
        contactRecordDetailView = new ContactRecordDetailView();
        //*** Check list size ***//
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
        setStrOnDataTable();
    }

    public void onAddContactRecordDetailView(){
        log.info("onAddContactRecordView >>> begin ");
        contactRecordDetailView = new ContactRecordDetailView();
        modeForButton = "add";
    }

    public void onDeleteContactRecordDetailView() {
        log.info( " onDeleteContactRecordDetailView getRemark is " + selectContactRecordDetail.getRemark());
        contactRecordDetailViewList.remove(selectContactRecordDetail);
        onSetRowNoContactRecordDetailView();
        log.info( " onDeleteContactRecordDetailView end ");
    }

    public void onSetRowNoContactRecordDetailView(){
        ContactRecordDetailView contactRecordDetailViewRow;
        for(int i=0;i< contactRecordDetailViewList.size();i++){
            contactRecordDetailViewRow = contactRecordDetailViewList.get(i);
            contactRecordDetailViewRow.setNo(i+1);
        }
    }


    public void onSaveCustomerAcceptance() {    // call transform  and then call businessControl
        log.info("onSaveCustomerAcceptance::::");
        log.info("contactRecordDetailViewList.size() ::: {} ", contactRecordDetailViewList.size());
        try{
            if(contactRecordDetailViewList.size() > 0){
                if(customerAcceptanceView.getId() == 0){
                    customerAcceptanceView.setCreateBy(user);
                    customerAcceptanceView.setCreateDate(DateTime.now().toDate());
                }
                customerAcceptanceView.setModifyBy(user);
                customerAcceptanceControl.onSaveCustomerAcceptance(customerAcceptanceView, contactRecordDetailViewList, workCaseId);
                messageHeader = msg.get("app.contactRecordDetail.message.header.save.success");
                message = msg.get("app.contactRecordDetail.message.body.save.success");
                onCreation();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else{
                messageHeader = msg.get("app.contactRecordDetail.message.header.save.fail");
                message = msg.get("app.contactRecordDetail.message.body.noContactRecord.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }


        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.contactRecordDetail.message.header.save.fail");

            if(ex.getCause() != null){
                message = msg.get("app.contactRecordDetail.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.contactRecordDetail.message.body.save.fail") + ex.getMessage();
            }

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }
    }

    public void onCancelCustomerAcceptance(){
        log.info("onCancelCustomerAcceptance::::  ");
        onCreation();
    }

    public CustomerAcceptanceView getCustomerAcceptanceView() {
        return customerAcceptanceView;
    }

    public void setCustomerAcceptanceView(CustomerAcceptanceView customerAcceptanceView) {
        this.customerAcceptanceView = customerAcceptanceView;
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

    public ContactRecordDetailView getContactRecordDetailViewTemp() {
        return contactRecordDetailViewTemp;
    }

    public void setContactRecordDetailViewTemp(ContactRecordDetailView contactRecordDetailViewTemp) {
        this.contactRecordDetailViewTemp = contactRecordDetailViewTemp;
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

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
