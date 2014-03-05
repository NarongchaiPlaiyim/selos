package com.clevel.selos.businesscontrol;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AgreementInfoView;
import com.clevel.selos.transform.AgreementInfoTransform;

@Stateless
public class AgreementSignContractControl extends BusinessControl{
	private static final long serialVersionUID = -8482787055002273344L;
	@Inject @SELOS
	private Logger log;
	@Inject private AgreementInfoDAO agreementInfoDAO;
	@Inject private WorkCaseDAO workCaseDAO;
	@Inject private AgreementInfoTransform agreementInfoTransform;
	
	public AgreementInfoView getAgreementInfoView(long workCaseId) {
		AgreementInfo agreement = null;
        if (workCaseId > 0) {
        	try {
        		agreement = agreementInfoDAO.findByWorkCaseId(workCaseId);
        	} catch (Throwable e) {}
        }
        return agreementInfoTransform.transformToView(agreement);
	}
	
	public void saveAgreementConfirm(AgreementInfoView view,long workCaseId) {
		User user = getCurrentUser();
		
		
		if (view.getId() <= 0) {
			WorkCase workCase = workCaseDAO.findRefById(workCaseId);
			AgreementInfo agreementInfo = agreementInfoTransform.creatAgreementInfo(view, workCase, user);
			agreementInfoDAO.save(agreementInfo);
		} else {
			AgreementInfo agreementInfo = agreementInfoDAO.findById(view.getId());
			agreementInfoTransform.updateConfirmed(agreementInfo, view, user);
			agreementInfoDAO.persist(agreementInfo);
		}
	}
}
