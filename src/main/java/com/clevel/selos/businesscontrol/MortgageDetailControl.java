package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.MortgageLandOfficeDAO;
import com.clevel.selos.dao.master.MortgageOSCompanyDAO;
import com.clevel.selos.dao.working.CustomerAttorneyDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.MortgageInfoCollOwnerDAO;
import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.AttorneyType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.MortgageLandOffice;
import com.clevel.selos.model.db.master.MortgageOSCompany;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerAttorney;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.MortgageInfoCollOwner;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.MortgageInfoAttorneySelectView;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.transform.CustomerAttorneyTransform;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.MortgageInfoAttorneySelectTransform;
import com.clevel.selos.transform.MortgageInfoCollOwnerTransform;
import com.clevel.selos.transform.MortgageInfoTransform;

@Stateless
public class MortgageDetailControl extends BusinessControl {
	 private static final long serialVersionUID = 2723725914845871936L;

	 @Inject @SELOS
	 private Logger log;
	 
	 @Inject private WorkCaseDAO workCaseDAO;
	 @Inject private MortgageOSCompanyDAO mortgageOSCompanyDAO;
	 @Inject private MortgageLandOfficeDAO mortgageLandOfficeDAO;
	 @Inject private MortgageInfoDAO mortgageInfoDAO;
	 @Inject private CustomerDAO customerDAO;
	 @Inject private CustomerAttorneyDAO customerAttorneyDAO;
	 @Inject private MortgageInfoCollOwnerDAO mortgageInfoCollOwnerDAO;
	 
	 @Inject private CustomerTransform customerTransform;
	 @Inject private MortgageInfoTransform mortgageInfoTransform;
	 @Inject private MortgageInfoCollOwnerTransform mortgageInfoCollOwnerTransform;
	 @Inject private MortgageInfoAttorneySelectTransform mortgageInfoAttorneySelectTransform;
	 @Inject private CustomerAttorneyTransform customerAttorneyTransform;
	 public MortgageDetailControl() {
	 }
	 
	 public List<SelectItem> listMortgageOSCompanies() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<MortgageOSCompany> list = mortgageOSCompanyDAO.findActiveAll();
		 for (MortgageOSCompany data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 public List<SelectItem> listMortgageLandOffices() {
		 List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		 
		 List<MortgageLandOffice> list = mortgageLandOfficeDAO.findActiveAll();
		 for (MortgageLandOffice data : list) {
			 SelectItem item = new SelectItem();
			 item.setValue(data.getId());
			 item.setLabel(data.getName());
			 item.setDescription(data.getDescription());
			 
			 rtnDatas.add(item);
		 }
		 return rtnDatas;
	 }
	 
	 public MortgageInfoView getMortgageInfo(long mortgageInfoId) {
		 MortgageInfo result = null;
		 try {
			 if (mortgageInfoId > 0)
				 result = mortgageInfoDAO.findById(mortgageInfoId);
		 } catch (Throwable e) {}
		 return mortgageInfoTransform.transfromToView(result);
	 }
	 
	 public List<CustomerInfoView> getCustomerCanBePOAList(long workCaseId) {
		 List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
		 if (customers == null || customers.isEmpty())
			 return Collections.emptyList();
		 else
			 return customerTransform.transformToViewList(customers);
	 }
	 
	 public List<MortgageInfoCollOwnerView> getCollOwners(long workCaseId,long mortgageInfoId) {
		 if (workCaseId <=0)
			 return Collections.emptyList();
		 List<Customer> customers = customerDAO.findByWorkCaseId(workCaseId);
		 List<MortgageInfoCollOwner> owners = mortgageInfoCollOwnerDAO.findAllByMortgageInfoId(mortgageInfoId);
		 HashMap<Long,MortgageInfoCollOwner> map = new HashMap<Long,MortgageInfoCollOwner>();
		 for (MortgageInfoCollOwner owner : owners) {
			 map.put(owner.getCustomer().getId(), owner);
		 }
		 
		 ArrayList<MortgageInfoCollOwnerView> rtnDatas = new ArrayList<MortgageInfoCollOwnerView>();
		 for (Customer customer : customers) {
			 long ownerId = 0;
			 MortgageInfoCollOwner owner = map.get(customer.getId());
			 if (owner != null)
				 ownerId = owner.getId();
			 
			 rtnDatas.add(mortgageInfoCollOwnerTransform.transformToView(customer, ownerId));
		 }
		 return rtnDatas;
	 }
	 public CustomerAttorneyView getCustomerAttorneyView(long customerAttorneyId) {
		 CustomerAttorney model = null;
		 try {
			 if (customerAttorneyId > 0)
				 model = customerAttorneyDAO.findById(customerAttorneyId);
		 } catch (Throwable e) {}
		 return customerAttorneyTransform.transformToView(model);
	 }
	 public List<MortgageInfoAttorneySelectView> getAttorneySelectList(long workCaseId) {
		 if (workCaseId <=0)
			 return Collections.emptyList();
		 List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
		 List<MortgageInfoAttorneySelectView> rtnDatas = new ArrayList<MortgageInfoAttorneySelectView>();
		 for (Customer customer : customers) {
			 List<Address> addresses = customer.getAddressesList();
			 //TODO Hardcode for address by registration (ที่อยู่ทะเบียนบ้าน = 2)
			 Address address = null;
			 for (Address check : addresses) {
				 if (check.getAddressType() != null && check.getAddressType().getId() == 2) {
					 address = check;
					 break;
				 }
			 }
			 rtnDatas.add(mortgageInfoAttorneySelectTransform.transformToView(customer, address));
		 }
		 return rtnDatas;
	 }
	 
	 public long saveMortgageDetail(long workCaseId,MortgageInfoView info,
			 	List<MortgageInfoCollOwnerView> collOwners, CustomerAttorneyView attorney) {
		 User user = getCurrentUser();
		 
		 WorkCase workCase = workCaseDAO.findRefById(workCaseId);
		 
		 //Process Customer Attorney
		 CustomerAttorney attorneyModel = null;
		 RadioValue poa = info.getPoa();
		 if (RadioValue.YES.equals(poa)) {
			 if (info.getCustomerAttorneyId() > 0) {
				 attorneyModel = customerAttorneyDAO.findById(info.getCustomerAttorneyId());
				 customerAttorneyTransform.updateModelFromView(attorneyModel, attorney);
				 customerAttorneyDAO.persist(attorneyModel);
			 } else {
				 attorneyModel = customerAttorneyTransform.createNewModel(attorney, workCase, AttorneyType.POWER_OF_ATTORNEY);
				 customerAttorneyDAO.save(attorneyModel);
			 }
		 } else {
			 //TODO do it need to remove current attorney ?
			 if (info.getCustomerAttorneyId() > 0)
				 customerAttorneyDAO.deleteById(info.getCustomerAttorneyId());
		 }
		 
		 //Process mortgage info
		 MortgageInfo infoModel = null;
		 if (info.getId() > 0) { //update
			 infoModel = mortgageInfoDAO.findById(info.getId());
			 mortgageInfoTransform.updateModelFromView(infoModel, info, user);
			 infoModel.setCustomerAttorney(attorneyModel);
			 mortgageInfoDAO.persist(infoModel);
		 } else {
			 infoModel = mortgageInfoTransform.createNewModel(info, user, workCase);
			 infoModel.setCustomerAttorney(attorneyModel);
			 mortgageInfoDAO.save(infoModel);
		 }
		 
		 //Process mortgage coll owner
		 for (MortgageInfoCollOwnerView collOwner : collOwners) {
			 if (!collOwner.isPOA()) {
				 if (collOwner.getId() > 0)
					 mortgageInfoCollOwnerDAO.deleteById(collOwner.getId());
				 continue;
			 }
			 
			 if (collOwner.getId() <= 0 && collOwner.getCustomerId() > 0) {
				 MortgageInfoCollOwner owner = new MortgageInfoCollOwner();
				 owner.setMortgageInfo(infoModel);
				 owner.setCustomer(customerDAO.findRefById(collOwner.getCustomerId()));
				 
				 mortgageInfoCollOwnerDAO.save(owner);
			 }
		 }
		 return infoModel.getId();
	 }
}
