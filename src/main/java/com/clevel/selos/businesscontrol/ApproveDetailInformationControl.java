package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.ApproveDetailInformationTransform;
import com.clevel.selos.transform.CreditDetailSimpleTransform;
import com.clevel.selos.transform.OpenAccountTransform;
import com.clevel.selos.transform.ProposeLineTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class ApproveDetailInformationControl extends BusinessControl {
	@Inject
	@SELOS
	private Logger log;

	@Inject
	BPMInterface bpmInterface;

	@Inject
	WorkCaseDAO workCaseDAO;

    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ProposeCreditInfoDAO proposeCreditInfoDAO;
    @Inject
    private ProposeLineTransform proposeLineTransform;

	@Inject
    ProposeLineTransform newCreditFacilityTransform;

	@Inject
	OpenAccountDAO openAccountDAO;

	@Inject
	OpenAccountTransform openAccountTransform;

	@Inject
	AgreementInfoDAO agreementInfoDAO;

	@Inject
	BankAccountPurposeDAO bankAccountPurposeDAO;

	@Inject
	CreditDetailSimpleTransform creditDetailSimpleTransform;

	@Inject
	ApproveDetailInformationTransform approveDetailInformationTransform;

	@Inject
	public ApproveDetailInformationControl() {

	}

	public ApproveDetailInformationView getApproveDetailInformationView(long workCaseId) {
		BigDecimal totalApprovedLimit = new BigDecimal(0);
		AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCaseId);
		ApproveDetailInformationView approveDetailInformationView = approveDetailInformationTransform.transformToView(agreementInfo);
		if (workCaseId > 0) {
			WorkCase workCase = workCaseDAO.findById(workCaseId);
			approveDetailInformationView.setModifyBy(workCase.getModifyBy());
			approveDetailInformationView.setModifyDate(workCase.getModifyDate());
			List<ProposeCreditInfo> newCreditDetailList = proposeCreditInfoDAO.findApprovedNewCreditDetail(workCaseId);
			List<ProposeCreditInfoDetailView> newCreditDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
			for (ProposeCreditInfo newCreditDetail : newCreditDetailList) {
                ProposeCreditInfoDetailView newCreditDetailView = proposeLineTransform.transformProposeCreditToView(newCreditDetail);
				newCreditDetailViewList.add(newCreditDetailView);
				totalApprovedLimit = totalApprovedLimit.add(newCreditDetail.getLimit());
			}
			approveDetailInformationView.setNewCreditDetailViewList(newCreditDetailViewList);
			approveDetailInformationView.setTotalApprovedCredit(totalApprovedLimit);
			approveDetailInformationView.setOpenAccountFullViewList(getOpenAccountViewList(workCaseId));
		}
		return approveDetailInformationView;
	}

	public ProposeLineView getNewCreditFacilityView(long workCaseId) {
        ProposeLine newCreditFacility = proposeLineDAO.findByWorkCaseId(workCaseId);
		return newCreditFacilityTransform.transformProposeLineToView(newCreditFacility, ProposeType.A);
	}

	public List<OpenAccountFullView> getOpenAccountViewList(long workCaseId) {
		if (workCaseId <= 0)
			return Collections.emptyList();

		List<OpenAccount> models = openAccountDAO.findByWorkCaseId(workCaseId);

		List<OpenAccountFullView> rtnDatas = new ArrayList<OpenAccountFullView>();
		for (OpenAccount model : models) {
			OpenAccountFullView view = openAccountTransform.transformToFullView(model);

			// Credits
			List<OpenAccountCreditView> creditViews = new ArrayList<OpenAccountCreditView>();
			HashSet<Long> newCreditSet = new HashSet<Long>();
			List<OpenAccountCredit> creditModels = model.getOpenAccountCreditList();
			if (creditModels != null && !creditModels.isEmpty()) {
				for (OpenAccountCredit creditModel : creditModels) {
					OpenAccountCreditView creditView = new OpenAccountCreditView();
					if (creditModel.getExistingCreditDetail() != null) {
						creditDetailSimpleTransform.updateSimpleView(creditView, creditModel.getExistingCreditDetail());
					} else if (creditModel.getProposeCreditInfo() != null) {
						creditDetailSimpleTransform.updateSimpleView(creditView, creditModel.getProposeCreditInfo());
						newCreditSet.add(creditModel.getProposeCreditInfo().getId());
					} else {
						continue;
					}
					creditView.setOpenAccountCreditId(creditModel.getId());
					creditView.setFromPledge(creditModel.isFromPledge());
					creditView.setChecked(true);
					creditViews.add(creditView);
				}
			}
			// List from new credit detail
			List<ProposeCreditInfo> newCreditModels = proposeCreditInfoDAO.findApprovedNewCreditDetail(model.getWorkCase().getId());
			for (ProposeCreditInfo newCreditModel : newCreditModels) {
				if (newCreditSet.contains(newCreditModel.getId()))
					continue;
				OpenAccountCreditView creditView = new OpenAccountCreditView();
				creditDetailSimpleTransform.updateSimpleView(creditView, newCreditModel);
				creditView.setOpenAccountCreditId(0);
				creditView.setFromPledge(false);
				creditView.setChecked(false);
				creditViews.add(creditView);
			}
			Collections.sort(creditViews);
			view.setCredits(creditViews);

			// Purpose
			List<OpenAccountPurposeView> purposeViews = new ArrayList<OpenAccountPurposeView>();
			HashMap<Long, OpenAccountPurpose> purposeMap = new HashMap<Long, OpenAccountPurpose>();
			List<OpenAccountPurpose> purposeModels = model.getOpenAccountPurposeList();
			if (purposeModels != null && !purposeModels.isEmpty()) {
				for (OpenAccountPurpose purposeModel : purposeModels) {
					purposeMap.put(purposeModel.getAccountPurpose().getId(), purposeModel);
				}
			}
			List<BankAccountPurpose> purposeDataModels = bankAccountPurposeDAO.findActiveAll();
			for (BankAccountPurpose purposeDataModel : purposeDataModels) {
				OpenAccountPurposeView purposeView = new OpenAccountPurposeView();
				purposeView.setPurposeId(purposeDataModel.getId());
				purposeView.setPurpose(purposeDataModel.getName());

				OpenAccountPurpose purposeModel = purposeMap.get(purposeDataModel.getId());
				if (purposeModel != null) {
					purposeView.setId(purposeModel.getId());
					purposeView.setChecked(true);
				} else {
					purposeView.setChecked(false);
				}
				if (model.isPledgeAccount() && purposeDataModel.isPledgeDefault()) {
					purposeView.setChecked(true);
					purposeView.setEditable(false);
				} else {
					purposeView.setEditable(true);
				}
				purposeViews.add(purposeView);
			}
			view.setPurposes(purposeViews);

			// Account Name
			List<OpenAccountNameView> nameViews = new ArrayList<OpenAccountNameView>();
			List<OpenAccountName> nameModels = model.getOpenAccountNameList();
			for (OpenAccountName nameModel : nameModels) {
				OpenAccountNameView nameView = new OpenAccountNameView();
				nameView.setId(nameModel.getId());
				nameView.setCustomerId(nameModel.getCustomer().getId());
				nameView.setName(nameModel.getCustomer().getDisplayName());
				nameView.setFromPledge(nameModel.isFromPledge());
				nameViews.add(nameView);
			}
			view.setNames(nameViews);

			rtnDatas.add(view);
		}
		return rtnDatas;
	}

	public void saveApproveDetailInformationView(ApproveDetailInformationView approveDetailInformationView, long workCaseId) {
		AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCaseId);
		if (agreementInfo != null) {
			agreementInfo.setFirstPaymentDate(approveDetailInformationView.getFirstPaymentDate());
			agreementInfo.setPayDate(approveDetailInformationView.getPayDate());
			agreementInfoDAO.persist(agreementInfo);
		}
	}

}
