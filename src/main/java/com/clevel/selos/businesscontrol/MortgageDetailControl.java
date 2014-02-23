package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
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
import com.clevel.selos.dao.working.MortgageInfoCollSubDAO;
import com.clevel.selos.dao.working.MortgageInfoCreditDAO;
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
import com.clevel.selos.model.db.working.MortgageInfoCollSub;
import com.clevel.selos.model.db.working.MortgageInfoCredit;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CreditDetailSimpleView;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.MortgageInfoAttorneySelectView;
import com.clevel.selos.model.view.MortgageInfoCollOwnerView;
import com.clevel.selos.model.view.MortgageInfoCollSubView;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.transform.CreditDetailSimpleTransform;
import com.clevel.selos.transform.CustomerAttorneyTransform;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.MortgageInfoAttorneySelectTransform;
import com.clevel.selos.transform.MortgageInfoCollOwnerTransform;
import com.clevel.selos.transform.MortgageInfoCollSubTransform;
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
	 @Inject private MortgageInfoCollSubDAO mortgageInfoCollSubDAO;
	 @Inject private MortgageInfoCreditDAO mortgageInfoCreditDAO;
	 @Inject private MortgageInfoCollOwnerDAO mortgageInfoCollOwnerDAO;
	 
	 @Inject private CustomerTransform customerTransform;
	 @Inject private MortgageInfoTransform mortgageInfoTransform;
	 @Inject private MortgageInfoCollSubTransform mortgageInfoCollSubTransform;
	 @Inject private MortgageInfoCollOwnerTransform mortgageInfoCollOwnerTransform;
	 @Inject private MortgageInfoAttorneySelectTransform mortgageInfoAttorneySelectTransform;
	 @Inject private CustomerAttorneyTransform customerAttorneyTransform;
	 @Inject private CreditDetailSimpleTransform creditDetailSimpleTransform;
	 
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
		 return mortgageInfoTransform.transformToView(result);
	 }
	 
	 public List<CustomerInfoView> getCustomerCanBePOAList(long workCaseId) {
		 List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
		 if (customers == null || customers.isEmpty())
			 return Collections.emptyList();
		 else
			 return customerTransform.transformToViewList(customers);
	 }
	 public List<MortgageInfoCollSubView> getMortgageInfoCollSubList(long mortgageInfoId) {
		 if (mortgageInfoId <=0)
			 return Collections.emptyList();
		 List<MortgageInfoCollSub> subs = mortgageInfoCollSubDAO.findAllByMortgageInfoId(mortgageInfoId);
		 ArrayList<MortgageInfoCollSubView> rtnDatas = new ArrayList<MortgageInfoCollSubView>();
		 for (MortgageInfoCollSub sub : subs) {
			 rtnDatas.add(mortgageInfoCollSubTransform.transformToView(sub));
		 }
		 return rtnDatas;
	 }
	 public List<CreditDetailSimpleView> getMortgageInfoCreditList(long mortgageInfoId) {
		 if (mortgageInfoId <=0)
			 return Collections.emptyList();
		 List<MortgageInfoCredit> credits = mortgageInfoCreditDAO.findAllByMortgageInfoId(mortgageInfoId);
		 ArrayList<CreditDetailSimpleView> rtnDatas = new ArrayList<CreditDetailSimpleView>();
		 for (MortgageInfoCredit credit : credits) {
			 CreditDetailSimpleView view = null;
			 NewCollateralCredit collCredit = credit.getNewCollateralCredit();
			 if (collCredit.getExistingCreditDetail() != null) {
				 view = creditDetailSimpleTransform.transformToSimpleView(collCredit.getExistingCreditDetail());
			 } else if (collCredit.getNewCreditDetail() != null) {
				 view = creditDetailSimpleTransform.transformToSimpleView(collCredit.getNewCreditDetail());
			 } else {
				 continue;
			 }
			 rtnDatas.add(view);
		 }
		 return rtnDatas;
	 }
	 public List<MortgageInfoCollOwnerView> getCollOwners(long mortgageInfoId) {
		 if (mortgageInfoId <=0)
			 return Collections.emptyList();
		 List<MortgageInfoCollOwner> owners = mortgageInfoCollOwnerDAO.findAllByMortgageInfoId(mortgageInfoId);
		 ArrayList<MortgageInfoCollOwnerView> rtnDatas = new ArrayList<MortgageInfoCollOwnerView>();
		 for (MortgageInfoCollOwner owner : owners) {
			  rtnDatas.add(mortgageInfoCollOwnerTransform.transformToView(owner));
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
		 if (info.getId() <= 0)
			 return 0;
		 
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
			 if (info.getCustomerAttorneyId() > 0)
				 customerAttorneyDAO.deleteById(info.getCustomerAttorneyId());
		 }
		 
		 //Process mortgage info
		 MortgageInfo infoModel = mortgageInfoDAO.findById(info.getId());
		 mortgageInfoTransform.updateModelFromView(infoModel, info, user);
		 infoModel.setCustomerAttorney(attorneyModel);
		 mortgageInfoDAO.persist(infoModel);
		 
		 //Process mortgage coll owner
		 for (MortgageInfoCollOwnerView collOwner : collOwners) {
			 MortgageInfoCollOwner owner = mortgageInfoCollOwnerDAO.findById(collOwner.getId());
			 if (owner.isPoa() == collOwner.isPOA())
				 continue;
			 owner.setPoa(collOwner.isPOA());
			 mortgageInfoCollOwnerDAO.persist(owner);
		 }
		 return infoModel.getId();
	 }
}
