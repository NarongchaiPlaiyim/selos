package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.BAResultHCDAO;
import com.clevel.selos.dao.master.InsuranceCompanyDAO;
import com.clevel.selos.dao.working.BAPAInfoCreditDAO;
import com.clevel.selos.dao.working.BAPAInfoCustomerDAO;
import com.clevel.selos.dao.working.BAPAInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BAResultHC;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.BAPAInfoCredit;
import com.clevel.selos.model.db.working.BAPAInfoCustomer;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BAPAInfoCreditView;
import com.clevel.selos.model.view.BAPAInfoCustomerView;
import com.clevel.selos.model.view.BAPAInfoView;
import com.clevel.selos.transform.BAPAInfoTransform;
import com.clevel.selos.util.Util;

@Stateless
public class BAPAInfoControl extends BusinessControl {
	 private static final long serialVersionUID = -4625744349595576091L;
	 @Inject @SELOS Logger log;
	 
	 @Inject
	 private WorkCaseDAO workCaseDAO;
	 @Inject
	 private BAPAInfoDAO bapaInfoDAO;
	 @Inject
	 private BAPAInfoCustomerDAO bapaInfoCustomerDAO;
	 @Inject 
	 private BAPAInfoCreditDAO bapaInfoCreditDAO;
	 @Inject
	 private NewCreditDetailDAO newCreditDetailDAO;
	 
	  @Inject 
	 private InsuranceCompanyDAO insuranceCompanyDAO;
	 @Inject
	 private BAResultHCDAO baResultHCDAO;
	 @Inject
	 private CustomerDAO customerDAO;
	 
	 @Inject
	 private BAPAInfoTransform bapaInfoTransform;
	 
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
	 public List<BAResultHC> getBAResultHCs() {
		 return baResultHCDAO.findActiveAll();
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
	 
	 public List<BAPAInfoCustomerView> getBAPAInfoCustomerView(long workCaseId,long bapaInfoId) {
		 if (workCaseId <= 0)
			 return Collections.emptyList();
		 List<BAPAInfoCustomer> bapaInfoCustomers = bapaInfoCustomerDAO.findByBAPAInfo(bapaInfoId);
		 List<Customer> customers = customerDAO.findByWorkCaseId(workCaseId);
		 
		 HashMap<Long, BAPAInfoCustomer> bapaHash = new HashMap<Long, BAPAInfoCustomer>();
		 for (BAPAInfoCustomer bapaCus : bapaInfoCustomers) {
			 bapaHash.put(bapaCus.getCustomer().getId(), bapaCus);
		 }
		 
		 ArrayList<BAPAInfoCustomerView> rtnDatas = new ArrayList<BAPAInfoCustomerView>();
		 for (Customer customer : customers) {
			 BAPAInfoCustomerView view = new BAPAInfoCustomerView();
			 String customerName = _getCustomerName(customer);
			 String contractNo = _getCustomerContractNo(customer);
			 
			 BAPAInfoCustomer bapaCus = bapaHash.get(customer.getId());
			 if (bapaCus == null) { //not apply BA
				 view.setId(0);
				 view.setApplyBA(false);
				 view.setBaResultHC(null);
				 view.setCheckDate(null);
				 view.setUpdBAResultHC(0);
			 } else {
				 view.setId(bapaCus.getId());
				 view.setApplyBA(true);
				 view.setBaResultHC(bapaCus.getBaResultHC());
				 view.setCheckDate(bapaCus.getHealthCheckDate());
				 if (bapaCus.getBaResultHC() != null)
					 view.setUpdBAResultHC(bapaCus.getBaResultHC().getId());

				 //remove from hash
				 bapaHash.remove(customer.getId());
			 }
			 view.setCustomerId(customer.getId());
			 view.setCustomerName(customerName);
			 view.setCustomerContractNo(contractNo);
			 view.setRelationship(customer.getRelation().getDescription());
			 rtnDatas.add(view);
		 }
		 
		 if (!bapaHash.isEmpty()) { //remove unused 
			 for (BAPAInfoCustomer bapaCus :bapaHash.values()) {
				 bapaInfoCustomerDAO.delete(bapaCus);
			 }
		 }
		 Collections.sort(rtnDatas);
		 return rtnDatas;
	 }
	 
	 private String _getCustomerName(Customer customer) {
		 StringBuilder builder = new StringBuilder();
		 if (customer.getTitle() != null)
			 builder.append(customer.getTitle().getTitleTh()).append(' ');
		 builder.append(customer.getNameTh());
		 if (!Util.isEmpty(customer.getLastNameTh()))
			 builder.append(" ").append(customer.getLastNameTh());
		 return builder.toString();
	 }
	 private String _getCustomerContractNo(Customer customer) {
		 //TODO Check contract no
		 return customer.getMobileNumber();
	 }
	 
	 public List<BAPAInfoCreditView> getBAPAInfoCreditView(long workCaseId,long bapaInfoId) {
		 if (workCaseId <= 0)
			 return Collections.emptyList();
		 List<BAPAInfoCredit> bapaCredits = bapaInfoCreditDAO.findByBAPAInfo(bapaInfoId);
		 List<NewCreditDetail> credits = newCreditDetailDAO.findNewCreditDetailByWorkCaseIdForBA(workCaseId);
		 
		 return Collections.emptyList();
	 }
}
