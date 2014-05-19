package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.transform.MortgageInfoTransform;
import com.clevel.selos.transform.MortgageSummaryTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class MortgageConfirmControl extends BusinessControl {
	
	private static final long serialVersionUID = -3333248520011037542L;

	@Inject @SELOS
	private Logger log;
	
	@Inject private MortgageInfoDAO mortgageInfoDAO;
	@Inject private MortgageSummaryDAO mortgageSummaryDAO;
	@Inject private WorkCaseDAO workCaseDAO;
	@Inject private MortgageInfoTransform mortgageInfoTransform;
	@Inject private MortgageSummaryTransform mortgageSummaryTransform;
	
	public List<MortgageInfoView> getMortgageInfoList(long workCaseId) {
		if (workCaseId <= 0)
			return Collections.emptyList();
		List<MortgageInfoView> rtnDatas = new ArrayList<MortgageInfoView>();
		List<MortgageInfo> datas = mortgageInfoDAO.findAllByWorkCaseId(workCaseId);
		for (MortgageInfo data : datas) {
			rtnDatas.add(mortgageInfoTransform.transformToView(data,workCaseId));
		}
		return rtnDatas;
	}
	public MortgageSummaryView getMortgageSummaryView(long workCaseId) {
        MortgageSummary mortgage = null;
        if (workCaseId > 0) {
        	try {
        		mortgage = mortgageSummaryDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        }
        return mortgageSummaryTransform.transformToView(mortgage);
    }
	
	public void saveMortgageConfirm(MortgageSummaryView summaryView,List<MortgageInfoView> views,long workCaseId) {
		User user = getCurrentUser();
		
		for (MortgageInfoView view : views) {
			if (view.getId() <= 0)
				continue;
			MortgageInfo model = mortgageInfoDAO.findById(view.getId());
			mortgageInfoTransform.updateModelConfirmed(model, view, user);
			mortgageInfoDAO.save(model);
		}
		
		if (summaryView.getId() <= 0) {
			WorkCase workCase = workCaseDAO.findRefById(workCaseId);
			MortgageSummary model = mortgageSummaryTransform.createMortgageSummary(summaryView, user, workCase);
			mortgageSummaryDAO.save(model);
		} else {
			MortgageSummary model = mortgageSummaryDAO.findById(summaryView.getId());
			mortgageSummaryTransform.updateMortgageSummary(model, summaryView, user);
			mortgageSummaryDAO.persist(model);
		}
	}
}
