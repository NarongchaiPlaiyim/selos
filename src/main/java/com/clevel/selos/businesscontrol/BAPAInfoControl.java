package com.clevel.selos.businesscontrol;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.InsuranceCompanyDAO;
import com.clevel.selos.dao.working.BAPAInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BAPAInfoView;
import com.clevel.selos.transform.BAPAInfoTransform;

@Stateless
public class BAPAInfoControl extends BusinessControl {
	 private static final long serialVersionUID = -4625744349595576091L;
	 @Inject @SELOS Logger log;
	 
	 @Inject
	 private WorkCaseDAO workCaseDAO;
		
	 @Inject
	 private BAPAInfoDAO bapaInfoDAO;
	 @Inject
	 private BAPAInfoTransform bapaInfoTransform;
	 @Inject 
	 private InsuranceCompanyDAO insuranceCompanyDAO;
	 public BAPAInfoControl() {
	 }
	 
	 public BAPAInfoView getBAPAInfoView(long workCaseId) {
		 BAPAInfo result = null;
		 try {
			 if (workCaseId > 0)
				 result = bapaInfoDAO.findByWorkCase(workCaseId);
		 } catch (Throwable e) {
			log.debug("Found error getBAPAInfoView "+workCaseId,e);
		}
		return bapaInfoTransform.transformToView(result); 
	 }
	 
	 public List<InsuranceCompany> getInsuranceCompanies() {
		 return insuranceCompanyDAO.findActiveAll();
	 }
	 
	 public void saveBAPAInfo(long workCaseId,BAPAInfoView bapaInfoView) {
		 User user = getCurrentUser();
		 
		 WorkCase workCase = workCaseDAO.findRefById(workCaseId);
		 BAPAInfo bapaInfo = null;
		 if (bapaInfoView.getId() <= 0) {
			 bapaInfo = bapaInfoTransform.transformToNewModel(bapaInfoView, user, workCase);
			 bapaInfoDAO.save(bapaInfo);
		 } else {
			 bapaInfo = bapaInfoDAO.findById(bapaInfoView.getId());
			 bapaInfoTransform.updateModelFromView(bapaInfo, bapaInfoView, user);
			 bapaInfoDAO.persist(bapaInfo);
		 }
	 }
}
