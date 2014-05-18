package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.FeeCollectionAccountView;
import com.clevel.selos.model.view.FeeCollectionDetailView;
import com.clevel.selos.model.view.FeeSummaryView;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class FeeCalculationControl extends BusinessControl {

	private static final long serialVersionUID = -7081593994037218310L;
	@Inject @SELOS
	private Logger log;
	
	@Inject private WorkCaseDAO workCaseDAO;
	@Inject private FeeSummaryDAO feeSummaryDAO;
	@Inject private FeeCollectionAccountDAO feeCollectionAccountDAO;
	@Inject private FeeCollectionDetailDAO feeCollectionDetailDAO;
	
	@Inject private FeeDetailDAO feeDetailDAO;
	@Inject private OpenAccountDAO openAccountDAO;
	
	public FeeSummaryView getFeeSummary(long workCaseId) {
		FeeSummary model = null;
        if (workCaseId > 0) {
        	try {
        		model = feeSummaryDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        }
        return _transformSummary(model);
	}
	
	public List<List<FeeCollectionDetailView>> getFeeCollectionDetails(long workCaseId) {
		HashMap<Integer, List<FeeCollectionDetailView>> map = new HashMap<Integer, List<FeeCollectionDetailView>>();
		List<FeeCollectionDetail> list = feeCollectionDetailDAO.findAllByWorkCaseId(workCaseId);
		for (FeeCollectionDetail data : list) {
			if (data.getPaymentMethod() == null || data.getFeeType() == null)
				continue;
			List<FeeCollectionDetailView> views = map.get(data.getPaymentMethod().getId());
			if (views == null) {
				views = new ArrayList<FeeCollectionDetailView>();
				map.put(data.getPaymentMethod().getId(),views);
			}
			FeeCollectionDetailView view = new FeeCollectionDetailView();
			view.setId(data.getId());
			if (data.getPaymentMethod() != null)
				view.setPaymentMethod(data.getPaymentMethod().getDescription());
			if (data.getFeeType() != null)
				view.setPaymentType(data.getFeeType().getDescription());
			view.setDescription(data.getDescription());
			view.setAmount(data.getAmount());
			view.setAgreementSign(data.getPaymentMethod().isIncludeInAgreementSign());
			views.add(view);
		}
		Integer[] keys = map.keySet().toArray(new Integer[map.size()]);
		Arrays.sort(keys);
		ArrayList<List<FeeCollectionDetailView>> rtnDatas = new ArrayList<List<FeeCollectionDetailView>>();
		for (Integer key : keys) {
			rtnDatas.add(map.get(key));
		}
		return rtnDatas;
	}
	
	public List<FeeCollectionAccountView> getFeeCollectionAccounts(long workCaseId) {
		List<FeeCollectionAccountView> rtnDatas = new ArrayList<FeeCollectionAccountView>();
		
		List<FeeCollectionAccount> accounts = feeCollectionAccountDAO.findAllByWorkCaseId(workCaseId);
		for (FeeCollectionAccount account : accounts) {
			FeeCollectionAccountView view = new FeeCollectionAccountView();
			view.setId(account.getId());
			view.setAccountNo(account.getDisplayAccountNo());
			view.setAmount(account.getAmount());
			view.setBankAccountType(account.getOpenAccount().getBankAccountType().getName());
			rtnDatas.add(view);
		}
		return rtnDatas;
	}
	
	public FeeSummaryView calculateFeeCollection(long workCaseId) {
		User user = getCurrentUser();
		WorkCase workCase = workCaseDAO.findRefById(workCaseId);
		
		
		BigDecimal totalDebitFee = _processFeeDetails(workCase);
		_processFeeAccounts(workCase, totalDebitFee);
		
		FeeSummary summary = null;
		try {
			summary = feeSummaryDAO.findByWorkCaseId(workCaseId);
		} catch (Throwable e) {}	
		if (summary == null) {
			summary = new FeeSummary();
			summary.setCollectCompleted(RadioValue.NA);
			summary.setWorkCase(workCaseDAO.findRefById(workCaseId));
			summary.setCreateBy(user);
			summary.setCreateDate(new Date());
			summary.setModifyBy(user);
			summary.setModifyDate(new Date());
			feeSummaryDAO.save(summary);
		} else {
			summary.setModifyBy(user);
			summary.setModifyDate(new Date());
			feeSummaryDAO.persist(summary);
		}
		return _transformSummary(summary);
	}
	
	public void saveFeeConfirm(FeeSummaryView view) {
		if (view == null || view.getId() <= 0)
			return;
		FeeSummary summary = feeSummaryDAO.findById(view.getId());
		summary.setModifyBy(getCurrentUser());
		summary.setModifyDate(new Date());
		summary.setCollectCompleted(view.getCollectCompleted());
		feeSummaryDAO.persist(summary);
	}
	
	private FeeSummaryView _transformSummary(FeeSummary model) {
		FeeSummaryView view = new FeeSummaryView();
		if (model != null) {
			view.setId(model.getId());
			view.setCollectCompleted(model.getCollectCompleted());
			view.setModifyDate(model.getModifyDate());
			if (model.getModifyBy() != null)
				view.setModifyUser(model.getModifyBy().getDisplayName());
		}
		return view;
	}
	
	private BigDecimal _processFeeDetails(WorkCase workCase) {
		BigDecimal totalDebit = BigDecimal.ZERO;
		List<FeeDetail> details = feeDetailDAO.findAllByWorkCaseId(workCase.getId());
		
		HashMap<String, FeeCollectionDetail> map = new HashMap<String, FeeCollectionDetail>();
		for (FeeDetail detail : details) {
			FeePaymentMethod paymentMethod = detail.getPaymentMethod();
			FeeType paymentType = detail.getFeeType();
			if (paymentMethod == null || paymentType == null)
				continue;
			String key = _keyForFeeDetail(paymentMethod, paymentType);
			FeeCollectionDetail coll = map.get(key);
			if (coll == null) {
				coll = new FeeCollectionDetail();
				coll.setPaymentMethod(paymentMethod);
				coll.setFeeType(paymentType);
				coll.setDescription(detail.getDescription());
				coll.setAmount(BigDecimal.ZERO);
				map.put(key, coll);
			}
			if (detail.getAmount() != null) {
				coll.setAmount(coll.getAmount().add(detail.getAmount()));
				if (paymentMethod.isDebitFromCustomer())
					totalDebit = totalDebit.add(detail.getAmount());
			}
		}
		
		List<FeeCollectionDetail> currList = feeCollectionDetailDAO.findAllByWorkCaseId(workCase.getId());
		for (FeeCollectionDetail curr : currList) {
			String key = _keyForFeeDetail(curr.getPaymentMethod(),curr.getFeeType());
			FeeCollectionDetail newColl = map.get(key);
			if (newColl == null) {
				//delete
				feeCollectionDetailDAO.delete(curr);
				continue;
			}
			//update
			curr.setDescription(newColl.getDescription());
			curr.setAmount(newColl.getAmount());
			feeCollectionDetailDAO.persist(curr);
			//remove from map for using in add new process
			map.remove(key);
		}
		
		for (String key : map.keySet()) {
			FeeCollectionDetail newColl = map.get(key);
			newColl.setWorkCase(workCase);
			feeCollectionDetailDAO.save(newColl);
		}
		return totalDebit;
	}
	
	private String _keyForFeeDetail(FeePaymentMethod paymentMethod,FeeType paymentType) {
		return paymentMethod.getId() + ":"+paymentType.getId();
	}
	
	private void _processFeeAccounts(WorkCase workCase,BigDecimal totalDebitFee) {
		List<OpenAccount> accounts = openAccountDAO.findByWorkCaseId(workCase.getId());
		HashMap<Long,FeeCollectionAccount> map = new HashMap<Long, FeeCollectionAccount>();
		for (OpenAccount account : accounts) {
			boolean foundOD= false;
			List<OpenAccountPurpose> purposes = account.getOpenAccountPurposeList();
			for (OpenAccountPurpose purpose : purposes) {
				if (purpose.getAccountPurpose().isForOD()) {
					foundOD = true;
					break;
				}
			}
			if (!foundOD)
				continue;
			
			FeeCollectionAccount feeAcc = new FeeCollectionAccount();
			feeAcc.setDisplayAccountNo(account.getAccountNumber());
			feeAcc.setAmount(totalDebitFee);
			feeAcc.setOpenAccount(account);
			map.put(account.getId(), feeAcc);
		}
		
		List<FeeCollectionAccount> currList = feeCollectionAccountDAO.findAllByWorkCaseId(workCase.getId());
		for (FeeCollectionAccount currAcc : currList) {
			long key = currAcc.getOpenAccount().getId();
			FeeCollectionAccount newAcc = map.remove(key);
			if (newAcc == null) {
				//delete curr Acc
				feeCollectionAccountDAO.delete(currAcc);
				continue;
			} 
			//update
			currAcc.setDisplayAccountNo(newAcc.getDisplayAccountNo());
			currAcc.setAmount(newAcc.getAmount());
			feeCollectionAccountDAO.persist(currAcc);
		}
		//add list
		for (Long key : map.keySet()) {
			FeeCollectionAccount newAcc = map.get(key);
			newAcc.setWorkCase(workCase);
			feeCollectionAccountDAO.save(newAcc);
		}
	}
}
