package com.clevel.selos.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.view.ContactRecordDetailView;

public class ContactRecordDetailTransform extends Transform  {
    private static final long serialVersionUID = -4310732427668590367L;
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
    	model.setCallingDate(view.getCallingDate());
    	model.setCallingResult(view.getCallingResult());
    	model.setAcceptResult(view.getAcceptResult());
    	model.setNextCallingDate(view.getNextCallingDate());
    	model.setReason(view.getReason());
    	model.setRemark(view.getRemark());
    	model.setStatus(view.getStatus());
    	model.setCreateDate(new Date());
    	model.setModifyDate(new Date());
    	model.setModifyBy(user);
    	model.setCreateBy(user);
    	model.setCustomerAcceptance(customerAcceptance);
    	model.setWorkCase(customerAcceptance.getWorkCase());
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
