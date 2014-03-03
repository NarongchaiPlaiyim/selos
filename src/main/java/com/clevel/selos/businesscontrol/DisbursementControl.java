package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.dao.master.CrossTypeDAO;
import com.clevel.selos.dao.master.DisbursementTypeDAO;
import com.clevel.selos.dao.working.BAPAInfoCreditDAO;
import com.clevel.selos.dao.working.BAPAInfoDAO;
import com.clevel.selos.dao.working.DisbursementBahtnetCreditDAO;
import com.clevel.selos.dao.working.DisbursementBahtnetDAO;
import com.clevel.selos.dao.working.DisbursementDAO;
import com.clevel.selos.dao.working.DisbursementMCCreditDAO;
import com.clevel.selos.dao.working.DisbursementMCDAO;
import com.clevel.selos.dao.working.DisbursementSummaryDAO;
import com.clevel.selos.dao.working.DisbursementTRCreditDAO;
import com.clevel.selos.dao.working.DisbursementTRDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.CrossType;
import com.clevel.selos.model.db.master.DisbursementType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.BAPAInfo;
import com.clevel.selos.model.db.working.BAPAInfoCredit;
import com.clevel.selos.model.db.working.CreditTypeDetail;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementBahtnet;
import com.clevel.selos.model.db.working.DisbursementBahtnetCredit;
import com.clevel.selos.model.db.working.DisbursementCredit;
import com.clevel.selos.model.db.working.DisbursementMC;
import com.clevel.selos.model.db.working.DisbursementMCCredit;
import com.clevel.selos.model.db.working.DisbursementTR;
import com.clevel.selos.model.db.working.DisbursementTRCredit;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.view.CreditTypeDetailView;
import com.clevel.selos.model.view.DisbursementBahtnetDetailView;
import com.clevel.selos.model.view.DisbursementCreditTypeView;
import com.clevel.selos.model.view.DisbursementDepositBaDetailView;
import com.clevel.selos.model.view.DisbursementInfoView;
import com.clevel.selos.model.view.DisbursementMcDetailView;
import com.clevel.selos.model.view.DisbursementSummaryView;
import com.clevel.selos.model.view.DisbursementTypeView;
import com.clevel.selos.transform.CreditTypeDetailTransform;
import com.clevel.selos.transform.DisbursementTypeTransform;

import org.apache.soap.encoding.soapenc.DecimalDeserializer;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Stateless
public class DisbursementControl extends BusinessControl {

	@Inject
	@SELOS
	Logger logger;

	@Inject
	NewCreditDetailDAO newCreditDetailDAO;

	@Inject
	DisbursementDAO disbursementDAO;

	@Inject
	DisbursementSummaryDAO disbursementSummaryDAO;

	@Inject
	DisbursementMCDAO disbursementMCDAO;

	@Inject
	DisbursementTRDAO disbursementTRDAO;

	@Inject
	DisbursementBahtnetDAO disbursementBahtnetDAO;

	@Inject
	BAPAInfoDAO bapaInfoDAO;

	@Inject
	BAPAInfoCreditDAO bapaInfoCreditDAO;

	@Inject
	BankDAO bankDAO;

	@Inject
	CrossTypeDAO crossTypeDAO;

	@Inject
	BankBranchDAO bankBranchDAO;

	@Inject
	WorkCaseDAO workCaseDAO;

	@Inject
	DisbursementMCCreditDAO disbursementMCCreditDAO;

	@Inject
	DisbursementTRCreditDAO disbursementTRCreditDAO;

	@Inject
	DisbursementBahtnetCreditDAO disbursementBahtnetCreditDAO;
	
	@Inject
	OpenAccountDAO openAccountDAO;

	@Inject
	CreditTypeDetailTransform creditTypeDetailTransform;

	public DisbursementInfoView getDisbursementInfoView(long workCaseId) {
		DisbursementInfoView disbursementInfoView = new DisbursementInfoView();
		Disbursement disbursement = disbursementDAO.findByWorkCaseId(workCaseId);

		disbursementInfoView.setDisburse(this.getDisbursementSummaryViewByWorkCase(workCaseId));
		if (disbursement != null) {
			disbursementInfoView.setId(disbursement.getId());
			disbursementInfoView.setDisburseMcList(this.getDisbursementMcDetailView(disbursement.getId()));
			disbursementInfoView.setNumberOfCheque(disbursementInfoView.getDisburseMcList().size());
			disbursementInfoView.setDisburseDepositList(this.getDisbursementDepositBaDetailView(disbursement.getId()));
			disbursementInfoView.setDisbursementBahtnetList(this.getDisbursementBahtnetDetailView(disbursement.getId()));
			disbursementInfoView.setDisbursementBaList(this.getBADisbursementView(workCaseId));
			disbursementInfoView.setModifyBy(disbursement.getModifyBy());
			disbursementInfoView.setModifyDate(disbursement.getModifyDate());
		}

		return disbursementInfoView;
	}

	public List<DisbursementSummaryView> getDisbursementSummaryViewByWorkCase(long workCaseId) {
		List<NewCreditDetail> newCreditDetailList = newCreditDetailDAO.findApprovedNewCreditDetail(workCaseId);
		List<DisbursementSummaryView> disbursementSummaryViewList = new ArrayList<DisbursementSummaryView>();
		for (NewCreditDetail newCreditDetail : newCreditDetailList) {
			DisbursementSummaryView disbursementSummaryView = new DisbursementSummaryView();
			disbursementSummaryView.setNewCreditDetailID(newCreditDetail.getId());
			disbursementSummaryView.setProductCode(newCreditDetail.getProductCode());
			disbursementSummaryView.setProductProgram(newCreditDetail.getProductProgram().getName());
			disbursementSummaryView.setProjectCode(newCreditDetail.getProjectCode());

			disbursementSummaryView.setCreditFacility(newCreditDetail.getCreditType().getName());
			if (newCreditDetail.getRefinance() == 0)
				disbursementSummaryView.setRefinance("N/A");
			else if (newCreditDetail.getRefinance() == 1)
				disbursementSummaryView.setRefinance("Yes");
			else
				disbursementSummaryView.setRefinance("No");

			disbursementSummaryView.setApprovedLimit(newCreditDetail.getLimit());
			disbursementSummaryView.setHoldAmount(newCreditDetail.getHoldLimitAmount());

			DisbursementCredit disbursementCredit = disbursementSummaryDAO.findByNewCreditId(newCreditDetail.getId());
			if (disbursementCredit != null) {
				disbursementSummaryView.setId(disbursementCredit.getId());
				disbursementSummaryView.setDisburseAmount(disbursementCredit.getDisburseAmount());
				disbursementSummaryView.setDiffAmount(disbursementCredit.getDiffAmount());
			} else {
				disbursementSummaryView.setDisburseAmount(new BigDecimal(0));
				disbursementSummaryView.setDiffAmount(newCreditDetail.getLimit().subtract(newCreditDetail.getHoldLimitAmount()));
			}

			disbursementSummaryViewList.add(disbursementSummaryView);
		}

		return disbursementSummaryViewList;
	}

	public List<DisbursementMcDetailView> getDisbursementMcDetailView(long disbursementId) {
		List<DisbursementMcDetailView> disbursementMcDetailViewList = new ArrayList<DisbursementMcDetailView>();
		List<DisbursementMC> disbursementMCList = disbursementMCDAO.findByDisbursementId(disbursementId);
		for (DisbursementMC disbursementMC : disbursementMCList) {
			logger.info("disbursementMC : id {}", disbursementMC.getId());
			DisbursementMcDetailView disbursementMcDetailView = new DisbursementMcDetailView();
			disbursementMcDetailView.setId(disbursementMC.getId());
			disbursementMcDetailView.setIssuedBy(disbursementMC.getIssuedBy().getId());
			disbursementMcDetailView.setIssuedDate(disbursementMC.getIssuedDate());
			disbursementMcDetailView.setPayeeName(disbursementMC.getPayeeName().getCode());
			disbursementMcDetailView.setCrossType(disbursementMC.getCrossType().getId());

			disbursementMcDetailView
					.setDisbursementCreditTypeView(this.getDisbursementCreditView(disbursementMC, disbursementMcDetailView));
			disbursementMcDetailViewList.add(disbursementMcDetailView);
		}
		return disbursementMcDetailViewList;
	}

	private List<DisbursementCreditTypeView> getDisbursementCreditView(DisbursementMC disbursementMC,
			DisbursementMcDetailView disbursementMcDetailView) {

		BigDecimal totalDisbursementAmount = new BigDecimal(0);
		List<DisbursementCreditTypeView> disbursementMcCreditViewList = new ArrayList<DisbursementCreditTypeView>();
		List<DisbursementMCCredit> disbursementMCCreditList = disbursementMC.getDisbursementMCCreditList();
		for (DisbursementMCCredit disbursementMCCredit : disbursementMCCreditList) {
			DisbursementCreditTypeView disbursementCreditView = new DisbursementCreditTypeView();
			logger.info("disbursementMCCredit : id {}", disbursementMCCredit.getId());
			disbursementCreditView.setId(disbursementMCCredit.getId());
			disbursementCreditView.setNewCreditDetailId(disbursementMCCredit.getCreditDetail().getId());
			disbursementCreditView.setProductProgram(disbursementMCCredit.getCreditDetail().getProductProgram().getName());
			disbursementCreditView.setCreditFacility(disbursementMCCredit.getCreditDetail().getCreditType().getName());
			disbursementCreditView.setDisburseAmount(disbursementMCCredit.getDisburseAmount());
			disbursementCreditView.setLimitAmount(disbursementMCCredit.getCreditDetail().getLimit());
			totalDisbursementAmount = totalDisbursementAmount.add(disbursementMCCredit.getDisburseAmount());
			disbursementMcCreditViewList.add(disbursementCreditView);
		}
		disbursementMcDetailView.setTotalAmount(totalDisbursementAmount);
		return disbursementMcCreditViewList;
	}

	public List<DisbursementDepositBaDetailView> getDisbursementDepositBaDetailView(long disbursementId) {
		List<DisbursementDepositBaDetailView> disbursementDepositBaDetailViewList = new ArrayList<DisbursementDepositBaDetailView>();
		List<DisbursementTR> disbursementTRList = disbursementTRDAO.findByDisbursementId(disbursementId);
		for (DisbursementTR disbursementTR : disbursementTRList) {
			DisbursementDepositBaDetailView depositBaDetailView = new DisbursementDepositBaDetailView();
			depositBaDetailView.setId(disbursementTR.getId());
			depositBaDetailView.setOpenAccountId(disbursementTR.getOpenAccount().getId());
			depositBaDetailView.setAccountNumber(disbursementTR.getOpenAccount().getAccountNumber());
			List<OpenAccountName> openAccountNameList = disbursementTR.getOpenAccount().getOpenAccountNameList();
			String accountName = "";
			for (OpenAccountName openAccountName : openAccountNameList) {
				accountName += openAccountName.getCustomer().getNameEn() + ", ";
			}
			depositBaDetailView.setAccountName(accountName.substring(0, accountName.length() - 2));
			depositBaDetailView.setDisbursementCreditTypeView(this.getDisbursementCreditView(disbursementTR, depositBaDetailView));
			disbursementDepositBaDetailViewList.add(depositBaDetailView);
		}

		return disbursementDepositBaDetailViewList;
	}

	private List<DisbursementCreditTypeView> getDisbursementCreditView(DisbursementTR disbursementTR,
			DisbursementDepositBaDetailView depositBaDetailView) {

		BigDecimal totalDisbursementAmount = new BigDecimal(0);
		List<DisbursementCreditTypeView> disbursementCreditViewList = new ArrayList<DisbursementCreditTypeView>();
		List<DisbursementTRCredit> disbursementTRCreditList = disbursementTR.getDisbursementTRCreditList();
		for (DisbursementTRCredit disbursementTRCredit : disbursementTRCreditList) {
			DisbursementCreditTypeView disbursementCreditView = new DisbursementCreditTypeView();
			disbursementCreditView.setId(disbursementTRCredit.getId());
			disbursementCreditView.setNewCreditDetailId(disbursementTRCredit.getCreditDetail().getId());
			disbursementCreditView.setProductProgram(disbursementTRCredit.getCreditDetail().getProductProgram().getName());
			disbursementCreditView.setCreditFacility(disbursementTRCredit.getCreditDetail().getCreditType().getName());
			disbursementCreditView.setDisburseAmount(disbursementTRCredit.getDisburseAmount());
			totalDisbursementAmount = totalDisbursementAmount.add(disbursementTRCredit.getDisburseAmount());
			disbursementCreditViewList.add(disbursementCreditView);
		}
		depositBaDetailView.setTotalAmount(totalDisbursementAmount);
		return disbursementCreditViewList;
	}

	public List<DisbursementBahtnetDetailView> getDisbursementBahtnetDetailView(long disbursementId) {
		List<DisbursementBahtnetDetailView> disbursementBahtnetDetailViewList = new ArrayList<DisbursementBahtnetDetailView>();
		List<DisbursementBahtnet> disbursementBahtnetList = disbursementBahtnetDAO.findByDisbursementId(disbursementId);
		for (DisbursementBahtnet disbursementBahtnet : disbursementBahtnetList) {
			DisbursementBahtnetDetailView disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
			disbursementBahtnetDetailView.setId(disbursementBahtnet.getId());
			disbursementBahtnetDetailView.setBankCode(disbursementBahtnet.getBank().getCode());
			disbursementBahtnetDetailView.setAccountNumber(disbursementBahtnet.getAccountNumber());
			disbursementBahtnetDetailView.setBenefitName(disbursementBahtnet.getBeneficiaryName());
			disbursementBahtnetDetailView.setDisbursementCreditTypeView(this.getDisbursementCreditView(disbursementBahtnet,
					disbursementBahtnetDetailView));
			disbursementBahtnetDetailViewList.add(disbursementBahtnetDetailView);
		}

		return disbursementBahtnetDetailViewList;
	}

	private List<DisbursementCreditTypeView> getDisbursementCreditView(DisbursementBahtnet disbursementBahtnet,
			DisbursementBahtnetDetailView disbursementBahtnetDetailView) {

		BigDecimal totalDisbursementAmount = new BigDecimal(0);
		List<DisbursementCreditTypeView> disbursementCreditViewList = new ArrayList<DisbursementCreditTypeView>();
		List<DisbursementBahtnetCredit> disbursementBahtnetCreditList = disbursementBahtnet.getDisburseBahtnetCreditList();
		for (DisbursementBahtnetCredit disbursementBahtnetCredit : disbursementBahtnetCreditList) {
			DisbursementCreditTypeView disbursementCreditView = new DisbursementCreditTypeView();
			disbursementCreditView.setId(disbursementBahtnetCredit.getId());
			disbursementCreditView.setNewCreditDetailId(disbursementBahtnetCredit.getCreditDetail().getId());
			disbursementCreditView.setProductProgram(disbursementBahtnetCredit.getCreditDetail().getProductProgram().getName());
			disbursementCreditView.setCreditFacility(disbursementBahtnetCredit.getCreditDetail().getCreditType().getName());
			disbursementCreditView.setDisburseAmount(disbursementBahtnetCredit.getDisburseAmount());
			totalDisbursementAmount = totalDisbursementAmount.add(disbursementBahtnetCredit.getDisburseAmount());
			disbursementCreditViewList.add(disbursementCreditView);
		}
		disbursementBahtnetDetailView.setTotalAmount(totalDisbursementAmount);
		return disbursementCreditViewList;
	}

	public void saveDisbursement(long workCaseId, DisbursementInfoView disbursementInfoView) {
		User user = getCurrentUser();
		Disbursement disbursement = new Disbursement();
		if (disbursementInfoView.getId() > 0) {
			disbursement = disbursementDAO.findById(disbursementInfoView.getId());
		} else {
			disbursement.setWorkCase(workCaseDAO.findById(workCaseId));
			disbursement.setCreateBy(user);
			disbursement.setCreateDate(new Date());
		}
		disbursement.setNumberOfCheque(disbursementInfoView.getNumberOfCheque());
		disbursement.setNumberOfTR(disbursementInfoView.getNumberOfDeposit());
		disbursement.setNumberOfBahtnet(disbursementInfoView.getNumberOfBahtnet());
		disbursement.setTotalMCDisbursement(disbursementInfoView.getTotalMCDisburse());
		disbursement.setTotalTRDisburse(disbursementInfoView.getTotalDepositDisburse());
		disbursement.setTotalBahtnetDisbursement(disbursementInfoView.getTotalBahtnetDisburse());
		disbursement.setModifyBy(user);
		disbursement.setModifyDate(new Date());
		disbursement = disbursementDAO.persist(disbursement);

		// Disbursement Summary
		for (DisbursementSummaryView disbursementSummaryView : disbursementInfoView.getDisburse()) {
			DisbursementCredit disbursementCredit = new DisbursementCredit();
			if (disbursementSummaryView.getId() > 0) {
				disbursementCredit = disbursementSummaryDAO.findById(disbursementSummaryView.getId());
			} else {
				disbursementCredit.setCreditDetail(newCreditDetailDAO.findById(disbursementSummaryView.getNewCreditDetailID()));
				disbursementCredit.setDisbursement(disbursement);
			}
			disbursementCredit.setDisburseAmount(disbursementSummaryView.getDisburseAmount());
			disbursementCredit.setDiffAmount(disbursementSummaryView.getDiffAmount());
			disbursementSummaryDAO.persist(disbursementCredit);
		}

		// Disbursement MC
		for (DisbursementMcDetailView disbursementMcDetailView : disbursementInfoView.getDisburseMcList()) {
			DisbursementMC disbursementMC = new DisbursementMC();
			if (disbursementMcDetailView.getId() > 0) {
				disbursementMC = disbursementMCDAO.findById(disbursementMcDetailView.getId());
			}else{
				disbursementMC.setDisbursement(disbursement);
			}
			disbursementMC.setCrossType(crossTypeDAO.findById(disbursementMcDetailView.getCrossType()));
			disbursementMC.setIssuedBy(bankBranchDAO.findById(disbursementMcDetailView.getIssuedBy()));
			disbursementMC.setPayeeName(bankDAO.findById(disbursementMcDetailView.getPayeeName()));
			disbursementMC.setIssuedDate(disbursementMcDetailView.getIssuedDate());
			disbursementMC.setPayeeRemark(disbursementMcDetailView.getPayeeSubname());
			disbursementMCDAO.persist(disbursementMC);

			// Disbursement MC credit
			for (DisbursementCreditTypeView disbursementCreditTypeView : disbursementMcDetailView.getDisbursementCreditTypeView()) {
				DisbursementMCCredit disbursementMCCredit = new DisbursementMCCredit();
				if (disbursementCreditTypeView.getId() > 0) {
					disbursementMCCredit = disbursementMCCreditDAO.findById(disbursementCreditTypeView.getId());
				} else {
					disbursementMCCredit.setCreditDetail(newCreditDetailDAO.findById(disbursementCreditTypeView.getNewCreditDetailId()));
					disbursementMCCredit.setDisbursementMC(disbursementMC);
				}
				disbursementMCCredit.setDisburseAmount(disbursementCreditTypeView.getDisburseAmount());
				disbursementMCCreditDAO.persist(disbursementMCCredit);
			}
		}

		// Disbursement TR
		for (DisbursementDepositBaDetailView depositBaDetailView : disbursementInfoView.getDisburseDepositList()) {
			DisbursementTR disbursementTR = new DisbursementTR();
			if (depositBaDetailView.getId() > 0) {
				disbursementTR = disbursementTRDAO.findById(depositBaDetailView.getId());
			} else {
				disbursementTR.setDisbursement(disbursement);

			}
			disbursementTR.setOpenAccount(openAccountDAO.findByAccountNumber(depositBaDetailView.getAccountNumber()));
			disbursementTRDAO.persist(disbursementTR);
			// Disbursement TR credit
			for (DisbursementCreditTypeView disbursementCreditTypeView : depositBaDetailView.getDisbursementCreditTypeView()) {
				DisbursementTRCredit disbursementTRCredit = new DisbursementTRCredit();
				if (disbursementCreditTypeView.getId() > 0) {
					disbursementTRCredit = disbursementTRCreditDAO.findById(disbursementCreditTypeView.getId());
				} else {
					disbursementTRCredit.setCreditDetail(newCreditDetailDAO.findById(disbursementCreditTypeView.getNewCreditDetailId()));
					disbursementTRCredit.setDisbursementTR(disbursementTR);
				}
				disbursementTRCredit.setDisburseAmount(disbursementCreditTypeView.getDisburseAmount());
				disbursementTRCreditDAO.persist(disbursementTRCredit);
			}
		}

		// Disbursement BA
		for (DisbursementBahtnetDetailView bahtnetDetailView : disbursementInfoView.getDisbursementBahtnetList()) {
			DisbursementBahtnet disbursementBahtnet = new DisbursementBahtnet();
			if (bahtnetDetailView.getId() > 0) {
				disbursementBahtnet = disbursementBahtnetDAO.findById(bahtnetDetailView.getId());
			} else {
				disbursementBahtnet.setDisbursement(disbursement);
			}
			disbursementBahtnet.setBank(bankDAO.findById(bahtnetDetailView.getBankCode()));
			disbursementBahtnet.setBranchName(bahtnetDetailView.getBranchName());
			disbursementBahtnet.setAccountNumber(bahtnetDetailView.getAccountNumber());
			disbursementBahtnet.setBeneficiaryName(bahtnetDetailView.getBenefitName());
			disbursementBahtnetDAO.persist(disbursementBahtnet);
			// Disbursement BA credit
			for (DisbursementCreditTypeView disbursementCreditTypeView : bahtnetDetailView.getDisbursementCreditTypeView()) {
				DisbursementBahtnetCredit disbursementBahtnetCredit = new DisbursementBahtnetCredit();
				if (disbursementCreditTypeView.getId() > 0) {
					disbursementBahtnetCredit = disbursementBahtnetCreditDAO.findById(disbursementCreditTypeView.getId());
				} else {
					disbursementBahtnetCredit.setCreditDetail(newCreditDetailDAO.findById(disbursementCreditTypeView.getNewCreditDetailId()));
					disbursementBahtnetCredit.setDisbursementBahtnet(disbursementBahtnet);
				}
				disbursementBahtnetCredit.setDisburseAmount(disbursementCreditTypeView.getDisburseAmount());
				disbursementBahtnetCreditDAO.persist(disbursementBahtnetCredit);
			}
		}

	}
	
	public void deleteDisbursementDetail(List<Long> disbursementMCList, List<Long> disbursementTRList, List<Long> disbursementBAList){
		for (long id : disbursementMCList){
			disbursementMCCreditDAO.delete(disbursementMCCreditDAO.findByDisbursementMCId(id));
			disbursementMCDAO.deleteById(id);
		}
		for (long id : disbursementTRList){
			disbursementTRCreditDAO.delete(disbursementTRCreditDAO.findByDisbursementTRId(id));
			disbursementTRDAO.deleteById(id);
		}
		for (long id : disbursementBAList){
			disbursementBahtnetCreditDAO.delete(disbursementBahtnetCreditDAO.findByDisbursementBahtnetId(id));
			disbursementBahtnetDAO.deleteById(id);
		}
	}
	

	public List<DisbursementDepositBaDetailView> getBADisbursementView(long workCaseId) {
		List<DisbursementDepositBaDetailView> disbursementDepositBaDetailViewList = new ArrayList<DisbursementDepositBaDetailView>();
		BAPAInfo bapaInfo = bapaInfoDAO.findByWorkCase(workCaseId);
		List<BAPAInfoCredit> bapaInfoCreditList = bapaInfoCreditDAO.findByBAPAInfo(bapaInfo.getId());
		for (BAPAInfoCredit bapaInfoCredit : bapaInfoCreditList) {
			NewCreditDetail newCreditDetail = bapaInfoCredit.getCreditDetail();
			DisbursementDepositBaDetailView depositBaDetailView = new DisbursementDepositBaDetailView();
			depositBaDetailView.setAccountName(newCreditDetail.getAccountName());
			depositBaDetailView.setAccountNumber(newCreditDetail.getAccountNumber());
			depositBaDetailView.setTotalAmount(bapaInfoCredit.getExpenseAmount());

			List<DisbursementCreditTypeView> disbursementCreditViewList = new ArrayList<DisbursementCreditTypeView>();
			DisbursementCreditTypeView disbursementCreditView = new DisbursementCreditTypeView();
			disbursementCreditView.setProductProgram(newCreditDetail.getProductProgram().getName());
			disbursementCreditView.setCreditType(newCreditDetail.getCreditType().getName());
			disbursementCreditView.setObjective("คุ้มครองวงเงิน " + newCreditDetail.getCreditType().getName());
			disbursementCreditViewList.add(disbursementCreditView);

			depositBaDetailView.setDisbursementCreditTypeView(disbursementCreditViewList);

			disbursementDepositBaDetailViewList.add(depositBaDetailView);
		}
		return disbursementDepositBaDetailViewList;
	}

	public List<SelectItem> getBankBranches() {
		List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		List<BankBranch> models = bankBranchDAO.findActiveAll();
		for (BankBranch model : models) {
			SelectItem item = new SelectItem();
			item.setValue(model.getId());
			item.setLabel(model.getName());
			item.setDescription(model.getDescription());
			rtnDatas.add(item);
		}
		return rtnDatas;
	}

	public List<SelectItem> getCrossTypes() {
		List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		List<CrossType> models = crossTypeDAO.findActiveAll();
		for (CrossType model : models) {
			SelectItem item = new SelectItem();
			item.setValue(model.getId());
			item.setLabel(model.getName());
			item.setDescription(model.getDescription());
			rtnDatas.add(item);
		}
		return rtnDatas;
	}

	public List<SelectItem> getBanks() {
		List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
		List<Bank> models = bankDAO.findActiveAll();
		for (Bank model : models) {
			SelectItem item = new SelectItem();
			item.setValue(model.getCode());
			item.setLabel(model.getName());
			item.setDescription(model.getShortName());
			rtnDatas.add(item);
		}
		return rtnDatas;
	}

	public HashMap<Integer, String> getBankMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<Bank> models = bankDAO.findActiveAll();
		for (Bank model : models) {
			map.put(model.getCode(), model.getName());
		}
		return map;
	}

	public HashMap<Integer, String> getCrossTypeMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<CrossType> models = crossTypeDAO.findActiveAll();
		for (CrossType model : models) {
			map.put(model.getId(), model.getName());
		}
		return map;
	}

	public HashMap<Integer, String> getBankBranchMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		List<BankBranch> models = bankBranchDAO.findActiveAll();
		for (BankBranch model : models) {
			map.put(model.getId(), model.getName());
		}
		return map;
	}

}
