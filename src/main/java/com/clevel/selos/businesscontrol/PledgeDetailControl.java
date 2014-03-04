package com.clevel.selos.businesscontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.OpenAccountDepositDAO;
import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountDeposit;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.PledgeACDepView;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.transform.PledgeInfoTransform;

@Stateless
public class PledgeDetailControl extends BusinessControl {
	private static final long serialVersionUID = 1971358128333982570L;

	@Inject @SELOS
	private Logger log;
	
	@Inject private PledgeInfoDAO pledgeInfoDAO;
	@Inject private OpenAccountDAO openAccountDAO;
	@Inject private OpenAccountDepositDAO openAccountDepositDAO;
	@Inject private PledgeInfoTransform pledgeInfoTransform;
	
	public PledgeInfoFullView getPledgeInfoFull(long pledgeInfoId) {
		PledgeInfo result = null;
		long workCaseId = 0;
		try {
			if (pledgeInfoId > 0) {
				result = pledgeInfoDAO.findById(pledgeInfoId);
				if (result != null && result.getWorkCase() !=null)
					workCaseId = result.getWorkCase().getId();
			}
		} catch (Throwable e) {}
		return pledgeInfoTransform.transformToFullView(result,workCaseId);
	}
	public List<PledgeACDepView> getPledgeACList(long pledgeInfoId) {
		PledgeInfo result = null;
		try {
			if (pledgeInfoId > 0)
				result = pledgeInfoDAO.findById(pledgeInfoId);
		} catch (Throwable e) {}
		if (result == null || result.getOpenAccount() == null || result.getOpenAccount().getOpenAccountDepositList() == null)
			return Collections.emptyList();
		List<OpenAccountDeposit> deposits = result.getOpenAccount().getOpenAccountDepositList();
		ArrayList<PledgeACDepView> rtnDatas = new ArrayList<PledgeACDepView>();
		for (OpenAccountDeposit deposit : deposits) {
			PledgeACDepView view = new PledgeACDepView();
			view.setId(deposit.getId());
			view.setDep(deposit.getDepositNumber());
			view.setHoldAmount(deposit.getHoldAmount());
			rtnDatas.add(view);
		}
		return rtnDatas;
	}
	public void savePledgeDetail(PledgeInfoFullView view,List<PledgeACDepView> deposits,List<PledgeACDepView> deleteDeposits) {
		if (view.getId() <= 0)
			 return;
		User user = getCurrentUser();
		
		PledgeInfo model = pledgeInfoDAO.findById(view.getId());
		OpenAccount account = model.getOpenAccount();
		
		BigDecimal totalHold = new BigDecimal(0);
		List<OpenAccountDeposit> accountDeposits = account.getOpenAccountDepositList();
		
		HashMap<Long, OpenAccountDeposit> depositMap = new HashMap<Long, OpenAccountDeposit>();
		for (OpenAccountDeposit accountDeposit : accountDeposits) {
			depositMap.put(accountDeposit.getId(), accountDeposit);
		}
		for (PledgeACDepView deposit : deposits) {
			OpenAccountDeposit accountDeposit = null;
			if (deposit.getId() > 0 )
				accountDeposit = depositMap.get(deposit.getId());
			if (accountDeposit == null) {
				accountDeposit = new OpenAccountDeposit();
				accountDeposit.setOpenAccount(account);
				accountDeposits.add(accountDeposit);
			} 
			
			accountDeposit.setDepositNumber(deposit.getDep());
			if (deposit.getHoldAmount() == null)
				accountDeposit.setHoldAmount(new BigDecimal(0));
			else
				accountDeposit.setHoldAmount(deposit.getHoldAmount());
			totalHold = totalHold.add(accountDeposit.getHoldAmount());
		}
		
		for (PledgeACDepView delete : deleteDeposits) {
			OpenAccountDeposit accountDeposit = depositMap.get(delete.getId());
			accountDeposit.setOpenAccount(null);
			accountDeposits.remove(accountDeposit);
			openAccountDepositDAO.delete(accountDeposit);
		}
		openAccountDAO.persist(account);
		
		pledgeInfoTransform.updateModel(model, view, totalHold, user);
		pledgeInfoDAO.persist(model);
	}
	
}
