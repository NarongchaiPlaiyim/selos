package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.OpenAccountDeposit;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.PledgeACDepView;
import com.clevel.selos.model.view.PledgeInfoFullView;
import com.clevel.selos.transform.PledgeInfoTransform;

@Stateless
public class PledgeDetailControl extends BusinessControl {
	@Inject @SELOS
	private Logger log;
	
	@Inject private PledgeInfoDAO pledgeInfoDAO;
	@Inject private PledgeInfoTransform pledgeInfoTransform;
	
	public PledgeInfoFullView getPledgeInfoFull(long pledgeInfoId) {
		PledgeInfo result = null;
		try {
			if (pledgeInfoId > 0)
				result = pledgeInfoDAO.findById(pledgeInfoId);
		} catch (Throwable e) {}
		return pledgeInfoTransform.transformToFullView(result);
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
	public void savePledgeDetail(PledgeInfoFullView view,List<PledgeACDepView> deposits) {
		if (view.getId() <= 0)
			 return;
		
		User user = getCurrentUser();
		
		PledgeInfo model = pledgeInfoDAO.findById(view.getId());
		
	}
	
}
