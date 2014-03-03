package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.MortgageSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.MortgageSummary;
import com.clevel.selos.model.view.MortgageInfoView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.transform.MortgageInfoTransform;
import com.clevel.selos.transform.MortgageSummaryTransform;

@Stateless
public class MortgageConfirmControl extends BusinessControl {
	
	private static final long serialVersionUID = -3333248520011037542L;

	@Inject @SELOS
	private Logger log;
	
	@Inject private MortgageInfoDAO mortgageInfoDAO;
	@Inject private MortgageSummaryDAO mortgageSummaryDAO;
	@Inject private MortgageInfoTransform mortgageInfoTransform;
	@Inject private MortgageSummaryTransform mortgageSummaryTransform;
	@Inject private AgreementInfoDAO agreementInfoDAO;
	
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
	public MortgageSummaryView getMortgageSummary(long workCaseId) {
		MortgageSummary mortgage = null;
        AgreementInfo agreement = null;
        if (workCaseId > 0) {
        	try {
        		mortgage = mortgageSummaryDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        	try {
        		agreement = agreementInfoDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        }
        return mortgageSummaryTransform.transformToView(mortgage, agreement);
	}
	
	public void saveMortgageConfirm(MortgageSummaryView summaryView,List<MortgageInfoView> views) {
		User user = getCurrentUser();
		
		for (MortgageInfoView view : views) {
			if (view.getId() <= 0)
				continue;
			MortgageInfo model = mortgageInfoDAO.findById(view.getId());
			mortgageInfoTransform.updateModelConfirmed(model, view, user);
			mortgageInfoDAO.save(model);
		}
		
		MortgageSummary summary = mortgageSummaryDAO.findById(summaryView.getId());
		AgreementInfo agreementInfo = agreementInfoDAO.findById(summaryView.getAgreementId());
		mortgageSummaryTransform.updateConfirmed(summary, agreementInfo, summaryView, user);
		mortgageSummaryDAO.persist(summary);
		agreementInfoDAO.persist(agreementInfo);
	}
}
