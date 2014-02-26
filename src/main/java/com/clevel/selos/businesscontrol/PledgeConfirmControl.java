package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.view.PledgeInfoView;
import com.clevel.selos.transform.PledgeInfoTransform;

@Stateless
public class PledgeConfirmControl extends BusinessControl {
	private static final long serialVersionUID = 1702507214722201565L;

	@Inject @SELOS
	private Logger log;
	
	@Inject private PledgeInfoDAO pledgeInfoDAO;
	@Inject private PledgeInfoTransform pledgeInfoTransform;
	
	public List<PledgeInfoView> getPledgeInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<PledgeInfoView> rtnDatas = new ArrayList<PledgeInfoView>();
    	List<PledgeInfo> datas = pledgeInfoDAO.findAllByWorkCaseId(workCaseId);
    	for (PledgeInfo data : datas) {
    		rtnDatas.add(pledgeInfoTransform.transformToView(data));
    	}
    	return rtnDatas;
    }
}
