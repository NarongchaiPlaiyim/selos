package com.clevel.selos.businesscontrol;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.GuarantorInfoDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.GuarantorInfo;
import com.clevel.selos.model.view.GuarantorInfoFullView;
import com.clevel.selos.transform.GuarantorInfoTransform;

@Stateless
public class GuarantorDetailControl extends BusinessControl {

	private static final long serialVersionUID = 4927177511734889822L;

	@Inject @SELOS
	private Logger log;
	
	@Inject private GuarantorInfoDAO guarantorInfoDAO;
	@Inject private GuarantorInfoTransform guarantorInfoTransform;
	
	public GuarantorInfoFullView getGuarantorInfoFull(long guarantorInfoId) {
		GuarantorInfo result = null;
		long workCaseId = 0;
		try {
			if (guarantorInfoId > 0) {
				result = guarantorInfoDAO.findById(guarantorInfoId);
				if (result != null && result.getWorkCase() !=null)
					workCaseId = result.getWorkCase().getId();
			}
		} catch (Throwable e) {}
		return guarantorInfoTransform.transformToFullView(result,workCaseId);
	}
	
	 public void saveGuarantorDetail(GuarantorInfoFullView view) {
		 if (view.getId() <= 0)
			 return;
		 
		 User user = getCurrentUser();
		 GuarantorInfo model = guarantorInfoDAO.findById(view.getId());
		 guarantorInfoTransform.updateModel(model, view, user);
		 guarantorInfoDAO.persist(model);
	 }
	
}
