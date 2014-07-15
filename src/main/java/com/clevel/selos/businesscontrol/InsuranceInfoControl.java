package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.InsuranceInfoDAO;
import com.clevel.selos.dao.working.ProposeCollateralInfoDAO;
import com.clevel.selos.dao.working.ProposeCollateralInfoHeadDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.InsuranceInfo;
import com.clevel.selos.model.db.working.ProposeCollateralInfo;
import com.clevel.selos.model.db.working.ProposeCollateralInfoHead;
import com.clevel.selos.model.view.insurance.InsuranceInfoSectionView;
import com.clevel.selos.model.view.insurance.InsuranceInfoSummaryView;
import com.clevel.selos.model.view.insurance.InsuranceInfoView;
import com.clevel.selos.transform.InsuranceInfoTransform;
import com.clevel.selos.transform.ProposeLineTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class InsuranceInfoControl extends BusinessControl {
	@Inject
	@SELOS
	private Logger log;

	@Inject
	BPMInterface bpmInterface;

	@Inject
    ProposeCollateralInfoDAO newCollateralDAO;

	@Inject
    ProposeCollateralInfoHeadDAO newCollateralHeadDAO;

	@Inject
	InsuranceInfoDAO insuranceInfoDAO;

	@Inject
	WorkCaseDAO workCaseDAO;

	@Inject
    ProposeLineTransform proposeLineTransform;

	@Inject
	InsuranceInfoTransform insuranceInfoTransform;

	@Inject
	public InsuranceInfoControl() {

	}

	public InsuranceInfoSummaryView getInsuranceInforSummaryView(long workCaseId) {
		InsuranceInfoSummaryView insuranceInfoSummaryView = new InsuranceInfoSummaryView();
		if (workCaseId > 0) {
			InsuranceInfo insuranceInfo = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCaseId);
			if (insuranceInfo != null) {
				insuranceInfoSummaryView = insuranceInfoTransform.transformsInsuranceInfoToView(insuranceInfo);
			}
		}
		return insuranceInfoSummaryView;
	}

	public List<InsuranceInfoView> getInsuranceInfo(long workCaseId) {
		List<ProposeCollateralInfo> newCollateralList = new ArrayList<ProposeCollateralInfo>();
		if (workCaseId > 0) {
			newCollateralList = newCollateralDAO.findNewCollateralByTypeA(workCaseId);
		}
		return insuranceInfoTransform.transformsNewCollateralToView(newCollateralList);
	}

	public void saveInsuranceInfo(List<InsuranceInfoView> insuranceInfoViewList, BigDecimal totalPremiumAmount, long workCaseId) {
		User user = getCurrentUser();
		for (InsuranceInfoView insuranceInfoView : insuranceInfoViewList) {
            ProposeCollateralInfo newCollateral = newCollateralDAO.findById(insuranceInfoView.getNewCollateralView().getId());
			newCollateral.setPremiumAmount(insuranceInfoView.getPremium());
			newCollateral.setModifyBy(user);
			newCollateral.setModifyDate(new Date());
			newCollateralDAO.persist(newCollateral);
			List<InsuranceInfoSectionView> insuranceInfoSectionViewList = insuranceInfoView.getSectionList();
			for (InsuranceInfoSectionView infoSectionView : insuranceInfoSectionViewList) {
				ProposeCollateralInfoHead newCollateralHead = newCollateralHeadDAO.findById(infoSectionView.getNewCollateralHeadView().getId());
				newCollateralHead.setInsuranceCompany(infoSectionView.getCompany());
				newCollateralHead.setExistingCredit(infoSectionView.getExistingCredit());
				newCollateralHead.setModifyBy(user);
				newCollateralHead.setModifyDate(new Date());
				newCollateralHeadDAO.persist(newCollateralHead);
			}
		}
		InsuranceInfo insuranceInfo = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCaseId);
		if (insuranceInfo != null) {
			insuranceInfo.setTotalPremiumAmount(totalPremiumAmount);
			insuranceInfo.setModifyBy(user);
			insuranceInfo.setModifyDate(new Date());
			insuranceInfoDAO.persist(insuranceInfo);
		} else { // New
			insuranceInfo = new InsuranceInfo();
			insuranceInfo.setCreateBy(user);
			insuranceInfo.setCreateDate(new Date());
			insuranceInfo.setTotalPremiumAmount(totalPremiumAmount);
			insuranceInfo.setWorkCase(workCaseDAO.findById(workCaseId));
			insuranceInfoDAO.persist(insuranceInfo);
		}
	}

}
