package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.view.ContactRecordDetailView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ContactRecordDetailTransform extends Transform {

    public List<ContactRecordDetail> transformToModel(List<ContactRecordDetailView> contactRecordDetailViewList, CustomerAcceptance customerAcceptance) {
        List<ContactRecordDetail> contactRecordDetailList = new ArrayList<ContactRecordDetail>();
        for (ContactRecordDetailView contactRecordDetailView : contactRecordDetailViewList) {

            ContactRecordDetail ContactRecordDetail = new ContactRecordDetail();
            ContactRecordDetail.setCustomerAcceptance(customerAcceptance);

            ContactRecordDetail.setCreateBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setCreateDate(DateTime.now().toDate());
            ContactRecordDetail.setNo(contactRecordDetailView.getNo());
            ContactRecordDetail.setCallingDate(contactRecordDetailView.getCallingDate());
            ContactRecordDetail.setCallingTime(contactRecordDetailView.getCallingTime());
            ContactRecordDetail.setCallingResult(contactRecordDetailView.getCallingResult());
            ContactRecordDetail.setAcceptResult(contactRecordDetailView.getAcceptResult());
            ContactRecordDetail.setNextCallingDate(contactRecordDetailView.getNextCallingDate());
            ContactRecordDetail.setNextCallingTime(contactRecordDetailView.getNextCallingTime());
            ContactRecordDetail.setReason(contactRecordDetailView.getReason());
            ContactRecordDetail.setRemark(contactRecordDetailView.getRemark());
            ContactRecordDetail.setModifyBy(contactRecordDetailView.getCreateBy());
            ContactRecordDetail.setModifyDate(DateTime.now().toDate());
            contactRecordDetailList.add(ContactRecordDetail);
        }
        return contactRecordDetailList;
    }

    public List<ContactRecordDetailView> transformToView(List<ContactRecordDetail> contactRecordDetailList) {
        List<ContactRecordDetailView> contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
        for (ContactRecordDetail ContactRecordDetail : contactRecordDetailList) {
            ContactRecordDetailView contactRecordDetailView = new ContactRecordDetailView();
            contactRecordDetailView.setId(ContactRecordDetail.getId());
            contactRecordDetailView.setNo(ContactRecordDetail.getNo());
            contactRecordDetailView.setCallingDate(ContactRecordDetail.getCallingDate());
            contactRecordDetailView.setCallingTime(ContactRecordDetail.getCallingTime());
            contactRecordDetailView.setCallingResult(ContactRecordDetail.getCallingResult());
            contactRecordDetailView.setAcceptResult(ContactRecordDetail.getAcceptResult());
            contactRecordDetailView.setNextCallingDate(ContactRecordDetail.getNextCallingDate());
            contactRecordDetailView.setNextCallingTime(ContactRecordDetail.getNextCallingTime());
            contactRecordDetailView.setReason(ContactRecordDetail.getReason());
            contactRecordDetailView.setRemark(ContactRecordDetail.getRemark());
            contactRecordDetailView.setCreateBy(ContactRecordDetail.getCreateBy());
            contactRecordDetailView.setCreateDate(ContactRecordDetail.getCreateDate());
            contactRecordDetailView.setModifyBy(ContactRecordDetail.getModifyBy());
            contactRecordDetailView.setModifyDate(ContactRecordDetail.getModifyDate());
            contactRecordDetailViewList.add(contactRecordDetailView);
        }
        return contactRecordDetailViewList;

    }
}
