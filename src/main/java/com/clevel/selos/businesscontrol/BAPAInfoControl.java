package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BAResultHCDAO;
import com.clevel.selos.dao.master.FeePaymentMethodDAO;
import com.clevel.selos.dao.master.FeeTypeDAO;
import com.clevel.selos.dao.master.InsuranceCompanyDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BAPAType;
import com.clevel.selos.model.FeeLevel;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BAResultHC;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BAPAInfoCreditToSelectView;
import com.clevel.selos.model.view.BAPAInfoCreditView;
import com.clevel.selos.model.view.BAPAInfoCustomerView;
import com.clevel.selos.model.view.BAPAInfoView;
import com.clevel.selos.transform.BAPAInfoTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
	 private ProposeCreditInfoDAO newCreditDetailDAO;
     @Inject
     private ProposeFeeDetailDAO feeDetailDAO;
     @Inject
     private FeePaymentMethodDAO feePaymentMethodDAO;
     @Inject
     private FeeTypeDAO feeTypeDAO;
	 
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
	 
	 public void saveBAPAInfo(long workCaseId,
			 BAPAInfoView bapaInfoView,List<BAPAInfoCustomerView> customers,
			 List<BAPAInfoCreditView> credits,List<BAPAInfoCreditView> deleteCredits) {
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

         //Save Fee Collection
         BigDecimal feeAmount = bapaInfoView.getTotalExpense();
         long baPaFeeId = 13;
         FeeType type = feeTypeDAO.findById(baPaFeeId); //BA/PA
         log.debug("saveBAPAInfo : get fee type (type : {})",type);

         if(type!=null){
             ProposeFeeDetail model = feeDetailDAO.findByType(workCase.getId(), type.getId());
             log.debug("saveBAPAInfo : wrk_fee_detail (model : {})",model);
             if(feeAmount!=null && feeAmount.compareTo(BigDecimal.ZERO)>0){
                 if (model == null) {
                     model = new ProposeFeeDetail();
                     model.setPaymentMethod(feePaymentMethodDAO.findByBRMSCode("01"));
                     model.setFeeType(type);
                     model.setWorkCase(workCase);
                 }
                 model.setPercentFee(BigDecimal.ZERO);
                 model.setPercentFeeAfter(BigDecimal.ZERO);
                 model.setFeeYear(BigDecimal.ZERO);
                 model.setAmount(feeAmount);
                 model.setFeeLevel(FeeLevel.APP_LEVEL);
                 model.setDescription(type.getFormula());
                 model.setProposeType(null);
                 model.setProposeCreditInfo(null);
                 feeDetailDAO.save(model);
             } else {
                 if(model!=null)
                 feeDetailDAO.delete(model);
             }
         }
		 
		 //Customer
		 for (BAPAInfoCustomerView customer  : customers) {
			 if (!customer.isApplyBA() || !RadioValue.YES.equals(bapaInfo.getApplyBA())) {
				 if (customer.getId() > 0) {
					 //remove
					 bapaInfoCustomerDAO.deleteById(customer.getId());
				 }
				 continue;
			 }
				 
			 if (customer.getId() > 0) {
				 if (!customer.isNeedUpdate())
					 continue;
				 BAPAInfoCustomer model = bapaInfoCustomerDAO.findById(customer.getId());
				 if (customer.getUpdBAResultHC() > 0)
					 model.setBaResultHC(baResultHCDAO.findRefById(customer.getUpdBAResultHC()));
				 else
					 model.setBaResultHC(null);
				 model.setHealthCheckDate(customer.getCheckDate());
				 bapaInfoCustomerDAO.persist(model);
			 } else {
				 BAPAInfoCustomer model = new BAPAInfoCustomer();
				 model.setBapaInfo(bapaInfo);
				 model.setCustomer(customerDAO.findRefById(customer.getCustomerId()));
				 if (customer.getUpdBAResultHC() > 0)
					 model.setBaResultHC(baResultHCDAO.findRefById(customer.getUpdBAResultHC()));
				 else
					 model.setBaResultHC(null);
				 model.setHealthCheckDate(customer.getCheckDate());
				 
				 bapaInfoCustomerDAO.save(model);
			 }
		 }
		 
		 //Credit
		 for (BAPAInfoCreditView credit : credits) {
			 if (credit.getId() > 0) {
				 if (!credit.isNeedUpdate())
					 continue;
				 BAPAInfoCredit model = bapaInfoCreditDAO.findById(credit.getId());
				 if (!credit.isFromCredit()) {
					 model.setBapaType(credit.getType());
					 model.setPayByCustomer(credit.isPayByCustomer());
					 if (credit.getCreditId() > 0)
						 model.setCreditDetail(newCreditDetailDAO.findRefById(credit.getCreditId()));
					 else
						 model.setCreditDetail(null);
				 }
				 model.setLimit(credit.getLimit());
				 model.setPremiumAmount(credit.getPremiumAmount());
				 model.setExpenseAmount(credit.getExpenseAmount());
				 
				 bapaInfoCreditDAO.persist(model);
			 } else {
				 BAPAInfoCredit model = new BAPAInfoCredit();
				 model.setBapaType(credit.getType());
				 model.setPayByCustomer(credit.isPayByCustomer());
				 model.setLimit(credit.getLimit());
				 model.setPremiumAmount(credit.getPremiumAmount());
				 model.setExpenseAmount(credit.getExpenseAmount());
				 if (credit.getCreditId() > 0)
					 model.setCreditDetail(newCreditDetailDAO.findRefById(credit.getCreditId()));
				 else
					 model.setCreditDetail(null);
				 model.setBapaInfo(bapaInfo);
				 model.setFromCredit(credit.isFromCredit());
				 
				 bapaInfoCreditDAO.save(model);		
			 }
		 }
		 
		 //Delete Credit
		 for (BAPAInfoCreditView delete : deleteCredits) {
			 if (delete.getId() <= 0 || delete.isFromCredit())
				 continue;
			 bapaInfoCreditDAO.deleteById(delete.getId());
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
			 String customerName = customer.getDisplayName();
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
	 
	
	 private String _getCustomerContractNo(Customer customer) {
		 List<Address> addresses = customer.getAddressesList();
		 if (addresses != null && !addresses.isEmpty()) {
			 StringBuilder builder  = new StringBuilder();
			 //Using address 1 or 4
			 Address toUseAddr = null;
			 for (Address address : addresses) {
				 if (address.getAddressType().getId() == 1 || address.getAddressType().getId() == 4) {
					 toUseAddr = address;
					 break;
				 }
			 }
			 if (toUseAddr == null)
				 toUseAddr = addresses.get(0);
			 if (!Util.isEmpty(toUseAddr.getPhoneNumber())) {
				 builder.append(toUseAddr.getPhoneNumber());
				 if (!Util.isEmpty(toUseAddr.getExtension())) {
					 builder.append(" Ext ");
					 builder.append(toUseAddr.getExtension());
				 }
			 }
			 return builder.toString();
		 }
		 return null;
	 }
	 public List<BAPAInfoCreditToSelectView> getBAPAInfoCreditToSelectView(long workCaseId) {
		 if (workCaseId <= 0)
			 return Collections.emptyList();
		 List<ProposeCreditInfo> credits = newCreditDetailDAO.findNewCreditDetailByWorkCaseId(workCaseId,false);
		 ArrayList<BAPAInfoCreditToSelectView> rtnDatas = new ArrayList<BAPAInfoCreditToSelectView>();
		 for (ProposeCreditInfo credit : credits) {
			 BAPAInfoCreditToSelectView view = new BAPAInfoCreditToSelectView();
			 view.setId(credit.getId());
			 view.setProductProgram(credit.getProductProgram().getName());
			 view.setTopupBA(credit.getProductProgram().isBa());
			 view.setCreditType(credit.getCreditType().getName());
			 view.setLoanPurpose(credit.getLoanPurpose().getDescription());
			 view.setLimit(credit.getLimit());
			 rtnDatas.add(view);
		 }
		 return rtnDatas;
	 }
	 public List<BAPAInfoCreditView> getBAPAInfoCreditView(long workCaseId,long bapaInfoId) {
		 if (workCaseId <= 0)
			 return Collections.emptyList();
		 List<BAPAInfoCredit> bapaCredits = bapaInfoCreditDAO.findByBAPAInfo(bapaInfoId);
		 List<ProposeCreditInfo> credits = newCreditDetailDAO.findNewCreditDetailByWorkCaseIdForBA(workCaseId,true);
		 
		 HashMap<Long, ProposeCreditInfo> creditHash = new HashMap<Long, ProposeCreditInfo>();
		 for (ProposeCreditInfo credit : credits) {
			 creditHash.put(credit.getId(), credit);
		 }
		
		 
		 ArrayList<BAPAInfoCreditView> rtnDatas = new ArrayList<BAPAInfoCreditView>();
		 for (BAPAInfoCredit bapaCredit : bapaCredits) {
			 BAPAInfoCreditView view = new BAPAInfoCreditView();
			 view.setId(bapaCredit.getId());
			 view.setType(bapaCredit.getBapaType());
			 if (bapaCredit.getCreditDetail() != null) {
				 view.setCreditId(bapaCredit.getCreditDetail().getId());
				 view.setProductProgram(bapaCredit.getCreditDetail().getProductProgram().getName());
				 view.setCreditType(bapaCredit.getCreditDetail().getCreditType().getName());
				 view.setLoanPurpose(bapaCredit.getCreditDetail().getLoanPurpose().getDescription());
			 } else {
				 view.setCreditId(0);
				 view.setProductProgram("-");
				 view.setCreditType("-");
				 view.setLoanPurpose("-");
			 }
			 
			 view.setPayByCustomer(bapaCredit.isPayByCustomer());
			 view.setFromCredit(bapaCredit.isFromCredit());
			 view.setLimit(bapaCredit.getLimit());
			 view.setPremiumAmount(bapaCredit.getPremiumAmount());
			 view.setExpenseAmount(bapaCredit.getExpenseAmount());
			 
			 if (bapaCredit.getCreditDetail() != null) {
                 ProposeCreditInfo credit = creditHash.get(bapaCredit.getCreditDetail().getId());
				 if (credit != null) {
					 creditHash.remove(bapaCredit.getCreditDetail().getId());
				 }
			 }
			 rtnDatas.add(view);
		 }
		
		 if (!creditHash.isEmpty()) {
			 for (long id : creditHash.keySet()) {
                 ProposeCreditInfo credit = creditHash.get(id);
				 
				 BAPAInfoCreditView view = new BAPAInfoCreditView();
				 view.setId(0);
				 view.setType(BAPAType.BA);
				 view.setCreditId(credit.getId());
				 view.setProductProgram(credit.getProductProgram().getName());
				 view.setCreditType(credit.getCreditType().getName());
				 view.setLoanPurpose(credit.getLoanPurpose().getDescription());
				 view.setPayByCustomer(!credit.getProductProgram().isBa());
				 view.setFromCredit(true);
				 view.setLimit(credit.getLimit());
				 view.setPremiumAmount(new BigDecimal(0));
				 view.setExpenseAmount(credit.getLimit());
				 rtnDatas.add(view);
			 }
		 }
		 Collections.sort(rtnDatas);
		 return rtnDatas;
	 }

}
