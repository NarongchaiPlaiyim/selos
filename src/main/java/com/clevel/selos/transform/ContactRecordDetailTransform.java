package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.ContactRecordDetailView;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ContactRecordDetailTransform extends Transform  {
    private static final long serialVersionUID = -4310732427668590367L;
    @Inject
    @SELOS
    private Logger log;
	/*
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private CustomerAcceptanceTransform customerAcceptanceTransform;
    @Inject
    private ContactRecordDetailDAO contactRecordDetailDAO;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<ContactRecordDetail> contactRecordDetailList;
    */
    @Inject
    public ContactRecordDetailTransform() {

    }
/*
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
*/

    public List<ContactRecordDetailView> transformModelToView(final List<ContactRecordDetail> contactRecordDetailList){
        log.debug("-- transform List<ContactRecordDetailView> to List<ContactRecordDetail>(Size of list is {})", contactRecordDetailList.size());
        List<ContactRecordDetailView> contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
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
            view.setStatus(model.getStatus());
            view.setCreateBy(model.getCreateBy());
//            view.setNeedUpdate();
            view.setUpdReasonId(model.getReason().getId());
            contactRecordDetailViewList.add(view);
        }
        log.debug("-- ContactRecordDetailViewList.size()[{}]", contactRecordDetailViewList.size());
        return contactRecordDetailViewList;
    }
    public List<ContactRecordDetail> transformToModel(final List<ContactRecordDetailView> contactRecordDetailViewList, final WorkCase workCase, final User user, final WorkCasePrescreen workCasePrescreen, final Step step, final CustomerAcceptance customerAcceptance){
        log.debug("-- transform List<ContactRecordDetailView> to List<ContactRecordDetail>(Size of list is {})", ""+contactRecordDetailViewList.size());
        List<ContactRecordDetail> contactRecordDetailList = new ArrayList<ContactRecordDetail>();
        ContactRecordDetail model = null;
        for(ContactRecordDetailView view : contactRecordDetailViewList){
            model = new ContactRecordDetail();
            model.setCreateDate(DateTime.now().toDate());
            model.setWorkCase(workCase);
            model.setWorkCasePrescreen(workCasePrescreen);
            model.setCreateBy(user);
            model.setCallingDate(view.getCallingDate());
            model.setCallingResult(view.getCallingResult());
            model.setAcceptResult(view.getAcceptResult());
            model.setNextCallingDate(view.getNextCallingDate());
            model.setReason(view.getReason());
            model.setRemark(view.getRemark());
            model.setStep(step);
            model.setStatus(view.getStatus());
            model.setModifyDate(DateTime.now().toDate());
            model.setModifyBy(user);
            model.setCustomerAcceptance(customerAcceptance);
            contactRecordDetailList.add(model);
        }
        return contactRecordDetailList;
    }



    public List<ContactRecordDetailView> transformToView(List<ContactRecordDetail> contactRecordDetailList){
        if (contactRecordDetailList == null || contactRecordDetailList.isEmpty())
        	return Collections.emptyList();
        
        List<ContactRecordDetailView> rtnDatas = new ArrayList<ContactRecordDetailView>();
        for(ContactRecordDetail detail : contactRecordDetailList){
        	ContactRecordDetailView view = new ContactRecordDetailView();
        	view.setId(detail.getId());
        	view.setCallingDate(detail.getCallingDate());
        	view.setCallingResult(detail.getCallingResult());
        	view.setAcceptResult(detail.getAcceptResult());
        	view.setNextCallingDate(detail.getNextCallingDate());
        	view.setReason(detail.getReason());
        	view.setRemark(detail.getRemark());
        	view.setStatus(detail.getStatus());
        	view.setCreateBy(detail.getCreateBy());
        	view.setNeedUpdate(false);
        	
        	if (detail.getReason() != null)
    			view.setUpdReasonId(detail.getReason().getId());
    		rtnDatas.add(view);
        }
        return rtnDatas;
    }
    
    public ContactRecordDetail transformToNewModel(ContactRecordDetailView view,User user,CustomerAcceptance customerAcceptance) {
    	ContactRecordDetail model = new ContactRecordDetail();
    	model.setCreateDate(new Date());
    	model.setCreateBy(user);
    	model.setCustomerAcceptance(customerAcceptance);
    	model.setWorkCase(customerAcceptance.getWorkCase());
    	
    	updateModelFromView(model, view, user);
    	return model;
    }
    public void updateModelFromView(ContactRecordDetail model,ContactRecordDetailView view,User user) {
    	model.setCallingDate(view.getCallingDate());
    	model.setCallingResult(view.getCallingResult());
    	model.setAcceptResult(view.getAcceptResult());
    	model.setNextCallingDate(view.getNextCallingDate());
    	model.setReason(view.getReason());
    	model.setRemark(view.getRemark());
    	model.setStatus(view.getStatus());
    	model.setModifyDate(new Date());
    	model.setModifyBy(user);
    }
}
