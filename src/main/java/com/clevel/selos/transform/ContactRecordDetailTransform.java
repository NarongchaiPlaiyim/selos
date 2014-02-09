package com.clevel.selos.transform;

import com.clevel.selos.dao.working.ContactRecordDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ContactRecordDetailTransform extends Transform  {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private CustomerAcceptanceTransform customerAcceptanceTransform;
    @Inject
    private ContactRecordDetailDAO contactRecordDetailDAO;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<ContactRecordDetail> contactRecordDetailList;
    public ContactRecordDetailTransform() {

    }

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

    public List<ContactRecordDetail> transformToModel(final List<ContactRecordDetailView> contactRecordDetailViewList, final WorkCase workCase, final User user){
        log.debug("-- transform List<ContactRecordDetailView> to List<ContactRecordDetail>(Size of list is {})", ""+contactRecordDetailViewList.size());
        contactRecordDetailList = new ArrayList<ContactRecordDetail>();
        ContactRecordDetail model = null;
        for(ContactRecordDetailView view : contactRecordDetailViewList){
            if(!Util.isZero(view.getId())){
                model = contactRecordDetailDAO.findById(view.getId());
            } else {
                model = new ContactRecordDetail();
                model.setCreateDate(DateTime.now().toDate());
                model.setWorkCase(workCase);
                model.setCreateBy(user);
            }
            model.setCallingDate(view.getCallingDate());
            model.setCallingResult(view.getCallingResult());
            model.setAcceptResult(view.getAcceptResult());
            model.setNextCallingDate(view.getNextCallingDate());
            model.setReason(view.getReason());
            model.setRemark(view.getRemark());
            model.setStep(view.getStep());
            model.setStatus(view.getStatus());
            model.setModifyDate(DateTime.now().toDate());
            model.setModifyBy(user);
            if(!Util.isNull(model.getCustomerAcceptance())){
                model.setCustomerAcceptance(customerAcceptanceTransform.transformToModel(view.getCustomerAcceptance(), workCase, user));
            } else {
                model.setCustomerAcceptance(new CustomerAcceptance());
            }
            contactRecordDetailList.add(model);
        }
        return contactRecordDetailList;
    }

    public List<ContactRecordDetailView> transformToView(List<ContactRecordDetail> contactRecordDetailList){
        log.debug("-- transform List<ContactRecordDetail> to List<ContactRecordDetailView>(Size of list is {})", ""+contactRecordDetailList.size());
        contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
        ContactRecordDetailView view = null;
        for(ContactRecordDetail model : contactRecordDetailList){
            view = new ContactRecordDetailView();
            view.setId(model.getId());
            view.setCallingDate(model.getCallingDate());
            view.setCallingResult(model.getCallingResult());
            view.setAcceptResult(model.getAcceptResult());
            view.setNextCallingDate(model.getNextCallingDate());
            view.setReason(model.getReason());
            view.setRemark(model.getRemark());
            view.setStep(model.getStep());
            view.setStatus(model.getStatus());
            view.setCreateDate(model.getCreateDate());
            view.setModifyDate(model.getModifyDate());
            view.setCreateBy(model.getCreateBy());
            view.setModifyBy(model.getModifyBy());
            if(!Util.isNull(model.getCustomerAcceptance())){
                view.setCustomerAcceptance(customerAcceptanceTransform.transformToView(model.getCustomerAcceptance()));
            } else {
                view.setCustomerAcceptance(new CustomerAcceptanceView());
            }
            contactRecordDetailViewList.add(view);
        }
        return contactRecordDetailViewList;
    }
}
