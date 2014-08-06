package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.ContactRecordDetailDAO;
import com.clevel.selos.dao.working.CustomerAcceptanceDAO;
import com.clevel.selos.dao.working.TCGInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCaseOwnerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.Role;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.TCGInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseOwner;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.model.view.TCGInfoView;
import com.clevel.selos.transform.ContactRecordDetailTransform;
import com.clevel.selos.transform.CustomerAcceptanceTransform;
import com.clevel.selos.transform.TCGInfoTransform;
import com.clevel.selos.util.Util;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
public class CustomerAcceptanceControl extends BusinessControl {
	private static final long serialVersionUID = -1417785938223413196L;

	@Inject @SELOS
    private Logger log;
	
	@Inject
	private WorkCaseDAO workCaseDAO;
	
	@Inject
	private TCGInfoDAO tcgInfoDAO;
	@Inject
	private TCGInfoTransform tcgInfoTransform;
	
	@Inject
	private CustomerAcceptanceDAO customerAcceptanceDAO;
	@Inject
	private CustomerAcceptanceTransform customerAcceptanceTransform;
	
	@Inject
	private ContactRecordDetailDAO contactRecordDetailDAO;
	@Inject
	private ContactRecordDetailTransform contactRecordDetailTransform;
	
	@Inject
	private ReasonDAO reasonDAO;
	
	@Inject
	private RoleDAO roleDAO;
	@Inject
	private WorkCaseOwnerDAO workCaseOwnerDAO;
	
	
	
	public Status getWorkCaseStatus(long workCaseId) {
		WorkCase workCase = null;
		try {
			if (workCaseId > 0)
				workCase = workCaseDAO.findById(workCaseId);
		} catch (Throwable e) {
			log.debug("Found error getCustomerAcceptanceView "+workCaseId,e);
		}

		if (workCase == null)
			return null;
		else
			return workCase.getStatus();
	}
	public TCGInfoView getTCGInfoView(long workCaseId) {
		TCGInfo info = null;
		if (workCaseId > 0) {
			try {
				info = tcgInfoDAO.findByWorkCaseId(workCaseId);
			} catch (Throwable e) {
				log.debug("Found error getTCGInfoView "+workCaseId,e);
			}
		}
		return tcgInfoTransform.transformToView(info);
	}
	public CustomerAcceptanceView getCustomerAcceptanceView(long workCaseId) {
		CustomerAcceptance result = null;
		try {
			if (workCaseId > 0)
				result = customerAcceptanceDAO.findCustomerAcceptanceByWorkCaseId(workCaseId);
		} catch (Throwable e) {
			log.debug("Found error getCustomerAcceptanceView "+workCaseId,e);
		}

		if (result == null)
			return new CustomerAcceptanceView();
		else {
			CustomerAcceptanceView view = customerAcceptanceTransform.transformToView(result);
			//add zonemgr info
            Role zmRole = roleDAO.findRoleByName("ZM");
            if (zmRole != null) {
            	WorkCaseOwner owner = workCaseOwnerDAO.getLatestWorkCaseOwnerByRole(workCaseId, zmRole.getId());
            	if (owner != null) {
	            	view.setZoneMgrName(owner.getUser().getDisplayName());
	            	view.setZoneMgrEmail(owner.getUser().getEmailAddress());
	            	view.setZoneMgrTel(owner.getUser().getPhoneNumber());
            	}
            }
            return view;
		}
	}

    public CustomerAcceptanceView getCustomerAcceptanceView(long workCaseId, long workCasePreScreenId) {
        CustomerAcceptance result = null;
        try {
            if(!Util.isNull(Long.toString(workCaseId)) && !Util.isZero(workCasePreScreenId)){
                result = customerAcceptanceDAO.findCustomerAcceptanceByWorkCaseId(workCaseId);
            }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && !Util.isZero(workCasePreScreenId)){
                result = customerAcceptanceDAO.findCustomerAcceptanceByWorkCasePrescreenId(workCasePreScreenId);
            }
        } catch (Throwable e) {
            log.debug("Found error getCustomerAcceptanceView "+workCaseId,e);
        }

        if (result == null)
            return new CustomerAcceptanceView();
        else {
            CustomerAcceptanceView view = customerAcceptanceTransform.transformToView(result);
            //add zonemgr info
            Role zmRole = roleDAO.findRoleByName("ZM");
            if (zmRole != null) {
            	WorkCaseOwner owner = workCaseOwnerDAO.getLatestWorkCaseOwnerByRole(workCaseId, zmRole.getId());
            	if (owner != null) {
	            	view.setZoneMgrName(owner.getUser().getDisplayName());
	            	view.setZoneMgrEmail(owner.getUser().getEmailAddress());
	            	view.setZoneMgrTel(owner.getUser().getPhoneNumber());
            	}
            }
            return view;
        }
    }
	
	public List<ContactRecordDetailView> getContactRecordDetails(long customerAcceptanceId) {
		List<ContactRecordDetail> details = contactRecordDetailDAO.findRecordCallingByCustomerAcceptance(customerAcceptanceId);
		return contactRecordDetailTransform.transformToView(details);
	}
	public List<Reason> getContactRecordReasons() {
		List<Reason> reasons =  reasonDAO.getContactReasonList();
		if (reasons == null)
			return Collections.emptyList();
		else
			return reasons;
	}
	
	
	public void saveCustomerContactRecords(long workCaseId,CustomerAcceptanceView cusAcceptView,TCGInfoView tcgInfoView,List<ContactRecordDetailView> details,List<ContactRecordDetailView> delDetails) {
		User user = getCurrentUser();
		WorkCase workCase = workCaseDAO.findById(workCaseId);
		CustomerAcceptance cusAccept = null;
		if (cusAcceptView.getId() <= 0) { //new
			cusAccept = customerAcceptanceTransform.transformToNewModel(cusAcceptView, workCase, user);
			customerAcceptanceDAO.save(cusAccept);
		} else {
			cusAccept = customerAcceptanceDAO.findById(cusAcceptView.getId());
			cusAccept.setModifyBy(user);
			cusAccept.setModifyDate(new Date());
			customerAcceptanceDAO.persist(cusAccept);
		}
		
		if (tcgInfoView.getId() <= 0) { //new
			TCGInfo tcgInfo = tcgInfoTransform.transformToNewModel(tcgInfoView, cusAccept.getWorkCase());
			tcgInfoDAO.save(tcgInfo);
		} else {
			TCGInfo tcgInfo = tcgInfoDAO.findById(tcgInfoView.getId());
			tcgInfo.setReceiveTCGSlip(tcgInfoView.getReceiveTCGSlip());
			tcgInfo.setPayinSlipSendDate(tcgInfoView.getPayinSlipSendDate());
			tcgInfoDAO.persist(tcgInfo);
		}
		
		//Add and update first
		for (ContactRecordDetailView view : details) {
			if (view.isNew()) {
				ContactRecordDetail model = contactRecordDetailTransform.transformToNewModel(view, user, cusAccept);
				contactRecordDetailDAO.save(model);
			} else if (view.isNeedUpdate()) {
				//get from db
				ContactRecordDetail model = contactRecordDetailDAO.findById(view.getId());
				contactRecordDetailTransform.updateModelFromView(model, view, user);
				contactRecordDetailDAO.persist(model);
			}
		}
		
		//Delete
		for (ContactRecordDetailView view : delDetails) {
			if (view.isNew())
				continue;
			contactRecordDetailDAO.deleteById(view.getId());
		}
	}

}
