package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ContactRecordDetailView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ContactRecordDetailTransform extends Transform  {

    public List<ContactRecordDetail> transformToModel(List<ContactRecordDetailView> contactRecordDetailViewList,CustomerAcceptance customerAcceptance,WorkCase workCase){
        List<ContactRecordDetail> contactRecordDetailList = new ArrayList<ContactRecordDetail>();
        for(ContactRecordDetailView contactRecordDetailView : contactRecordDetailViewList){

            ContactRecordDetail ContactRecordDetail = new ContactRecordDetail();
            ContactRecordDetail.setWorkCase(workCase);
            ContactRecordDetail.setCustomerAcceptance(customerAcceptance);
            ContactRecordDetail.setCreateBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setCreateDate(DateTime.now().toDate());
            ContactRecordDetail.setCallingDate(contactRecordDetailView.getCallingDate());
            ContactRecordDetail.setCallingResult(contactRecordDetailView.getCallingResult());
            ContactRecordDetail.setAcceptResult(contactRecordDetailView.getAcceptResult());
            ContactRecordDetail.setNextCallingDate(contactRecordDetailView.getNextCallingDate());
            //ContactRecordDetail.setReason(contactRecordDetailView.getReason());
            ContactRecordDetail.setRemark(contactRecordDetailView.getRemark());
            //ContactRecordDetail.setStatus(contactRecordDetailView.getStatus());
            ContactRecordDetail.setModifyBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setModifyDate(DateTime.now().toDate());
            contactRecordDetailList.add(ContactRecordDetail);
        }
        return contactRecordDetailList;
    }

    public List<ContactRecordDetail> transformToModel(List<ContactRecordDetailView> contactRecordDetailViewList,Appraisal appraisal,WorkCase workCase){
        List<ContactRecordDetail> contactRecordDetailList = new ArrayList<ContactRecordDetail>();
        for(ContactRecordDetailView contactRecordDetailView : contactRecordDetailViewList){

            ContactRecordDetail ContactRecordDetail = new ContactRecordDetail();
            ContactRecordDetail.setWorkCase(workCase);
            //ContactRecordDetail.setAppraisal(appraisal);
            ContactRecordDetail.setCreateBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setCreateDate(DateTime.now().toDate());
            //ContactRecordDetail.setNo(contactRecordDetailView.getNo());
            ContactRecordDetail.setCallingDate(contactRecordDetailView.getCallingDate());
            //ContactRecordDetail.setCallingTime(contactRecordDetailView.getCallingTime());
            ContactRecordDetail.setCallingResult(contactRecordDetailView.getCallingResult());
            ContactRecordDetail.setAcceptResult(contactRecordDetailView.getAcceptResult());
            ContactRecordDetail.setNextCallingDate(contactRecordDetailView.getNextCallingDate());
            //ContactRecordDetail.setNextCallingTime(contactRecordDetailView.getNextCallingTime());
            //ContactRecordDetail.setReason(contactRecordDetailView.getReason());
            ContactRecordDetail.setRemark(contactRecordDetailView.getRemark());
            //ContactRecordDetail.setStatus(contactRecordDetailView.getStatus());
            ContactRecordDetail.setModifyBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setModifyDate(DateTime.now().toDate());
            contactRecordDetailList.add(ContactRecordDetail);
        }
        return contactRecordDetailList;
    }

    public List<ContactRecordDetailView> transformToView(List<ContactRecordDetail> contactRecordDetailList){
        List<ContactRecordDetailView> contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
        for(ContactRecordDetail ContactRecordDetail : contactRecordDetailList){
            ContactRecordDetailView contactRecordDetailView = new ContactRecordDetailView();
            contactRecordDetailView.setId(ContactRecordDetail.getId());
            contactRecordDetailView.setCallingDate(ContactRecordDetail.getCallingDate());
            contactRecordDetailView.setCallingResult(ContactRecordDetail.getCallingResult());
            contactRecordDetailView.setAcceptResult(ContactRecordDetail.getAcceptResult());
            contactRecordDetailView.setNextCallingDate(ContactRecordDetail.getNextCallingDate());
            //contactRecordDetailView.setReason(ContactRecordDetail.getReason());
            contactRecordDetailView.setRemark(ContactRecordDetail.getRemark());
            //contactRecordDetailView.setStatus(ContactRecordDetail.getStatus());
            contactRecordDetailView.setCreateBy(ContactRecordDetail.getCreateBy());
            contactRecordDetailView.setCreateDate(ContactRecordDetail.getCreateDate());
            contactRecordDetailView.setModifyBy(ContactRecordDetail.getModifyBy());
            contactRecordDetailView.setModifyDate(ContactRecordDetail.getModifyDate());
            contactRecordDetailViewList.add(contactRecordDetailView);
        }
        return contactRecordDetailViewList;

    }
}
