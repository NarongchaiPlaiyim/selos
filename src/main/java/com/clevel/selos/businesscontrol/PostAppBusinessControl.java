package com.clevel.selos.businesscontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.FeePaymentMethodDAO;
import com.clevel.selos.dao.master.FeeTypeDAO;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.dao.working.AgreementInfoDAO;
import com.clevel.selos.dao.working.DisbursementDAO;
import com.clevel.selos.dao.working.InsuranceInfoDAO;
import com.clevel.selos.dao.working.MortgageInfoDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.PerfectionReviewDAO;
import com.clevel.selos.dao.working.PledgeInfoDAO;
import com.clevel.selos.dao.working.ProposeFeeDetailDAO;
import com.clevel.selos.dao.working.ReturnInfoDAO;
import com.clevel.selos.dao.working.TCGInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.FeeLevel;
import com.clevel.selos.model.PerfectReviewStatus;
import com.clevel.selos.model.PerfectReviewType;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AgreementInfo;
import com.clevel.selos.model.db.working.InsuranceInfo;
import com.clevel.selos.model.db.working.MortgageInfo;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import com.clevel.selos.model.db.working.PerfectionReview;
import com.clevel.selos.model.db.working.PledgeInfo;
import com.clevel.selos.model.db.working.ProposeFeeDetail;
import com.clevel.selos.model.db.working.ReturnInfo;
import com.clevel.selos.model.db.working.TCGInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.FeeCollectionDetailView;
import com.clevel.selos.model.view.ReturnInfoView;
import com.clevel.selos.model.view.StepView;
import com.clevel.selos.model.view.UserView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.ReturnInfoTransform;
import com.clevel.selos.transform.StepTransform;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;

@Stateless
public class PostAppBusinessControl extends BusinessControl {
	private static final long serialVersionUID = 1881119889067519324L;
	
	private static final long ACTION_SUBMIT = 1015;
	@Inject
    @SELOS
    private Logger log;
	@Inject @NormalMessage
	private Message message;
	@Inject
    private BPMExecutor bpmExecutor;
	@Inject
	private ActionDAO actionDAO;
	@Inject
	private ReasonDAO reasonDAO;
	@Inject
	private WorkCaseDAO workCaseDAO;
	@Inject
	private UserTransform userTransform;
	@Inject
	private StepTransform stepTransform;
	@Inject
	private ReturnInfoTransform returnInfoTransform;
	@Inject
	private ReturnInfoDAO returnInfoDAO;
	@Inject
	private ProposeFeeDetailDAO feeDetailDAO;
	@Inject
	private PerfectionReviewDAO perfectionReviewDAO;
	@Inject
	private COMSInterface comsInterface;
	@Inject
	private MortgageInfoDAO mortgageInfoDAO;
	@Inject
	private AgreementInfoDAO agreementInfoDAO;
	@Inject
	private PledgeInfoDAO pledgeInfoDAO;
	@Inject
	private DisbursementDAO disbursementDAO;
	@Inject
	private FeeCalculationControl feeCalculationControl;
	@Inject
	private OpenAccountDAO openAccountDAO;
	@Inject
	private TCGInfoDAO tcgInfoDAO;
	@Inject
	private MortgageSummaryControl mortgageSummaryControl;
	@Inject
	private FeePaymentMethodDAO feePaymentMethodDAO;
	@Inject
	private FeeTypeDAO feeTypeDAO;
	@Inject
	private InsuranceInfoDAO insuranceInfoDAO;
	
	
	public void submitCA(long workCaseId, String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1015, -1, remark);
	}
	public void returnToBDM(long workCaseId, String queueName,String wobNumber,List<ReturnInfoView> returnInfos) throws Exception {
		_returnBPM(workCaseId, queueName,wobNumber, 1005, returnInfos);
	}
	public void returnToUW2(long workCaseId, String queueName,String wobNumber,List<ReturnInfoView> returnInfos) throws Exception {
		_returnBPM(workCaseId, queueName,wobNumber, 1033, returnInfos);
	}
	public void returnToDataEntry(long workCaseId, String queueName,String wobNumber,List<ReturnInfoView> returnInfos) throws Exception {
		_returnBPM(workCaseId, queueName,wobNumber, 1034, returnInfos);
	}
	public void returnToContactCenter(long workCaseId, String queueName,String wobNumber,List<ReturnInfoView> returnInfos) throws Exception {
		_returnBPM(workCaseId, queueName,wobNumber, 1037, returnInfos);
	}
	public void returnToLARBC(long workCaseId, String queueName,String wobNumber,List<ReturnInfoView> returnInfos) throws Exception {
		_returnBPM(workCaseId, queueName,wobNumber, 1038, returnInfos);
	}
	public void cancelCA(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1003, reasonId, remark);
	}
	public void cancelDisbursement(long workCaseId, String queueName,String wobNumber,int reasonId,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1040, reasonId, remark);
	}
	public void requestPriceReduction(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1028, -1, remark);
	}
	public void generateAgreement(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1036, -1, remark);
	}
	public void regenerateAgreement(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1039, -1, remark);
	}
	public void dataEntryComplete(long workCaseId,String queueName,String wobNumber,String remark) throws Exception {
		_executeBPM(workCaseId, queueName,wobNumber, 1035, -1, remark);
	}
	
	/**
	 * @return true if it has return code RG001
	 */
	private void _returnBPM(long workCaseId,String queueName,String wobNumber,long actionId,List<ReturnInfoView> returnInfos) throws Exception {
		WorkCase workCase = workCaseDAO.findById(workCaseId);
		Action action = actionDAO.findById(actionId);
		
		UserView userView = userTransform.transformToView(getCurrentUser());
		StepView stepView = stepTransform.transformToView(workCase.getStep());
		
		HashSet<String> returnCodeSet = new HashSet<String>(); 
		List<ReturnInfo> models = new ArrayList<ReturnInfo>();
		Date returnDate = new Date();
		for (ReturnInfoView view : returnInfos) {
			view.setReturnFromUser(userView);
			view.setReturnFromStep(stepView);
			view.setDateOfReturn(returnDate);
			view.setChallenge(0);
			view.setAcceptChallenge(0);
			if (view.getReturnCode() != null) {
				returnCodeSet.add(view.getReturnCode());
			}
			models.add(returnInfoTransform.transformToModel(view, workCase, getCurrentUser()));
		}
		returnInfoDAO.persist(models);
		
		_callBPM(workCase, action,queueName,wobNumber, -1, null, new HashSet<String>());
	}
	
	private void _executeBPM(long workCaseId,String queueName,String wobNumber,long actionId,int reasonId,String remark) throws Exception {
		WorkCase workCase = workCaseDAO.findById(workCaseId);
		Action action = actionDAO.findById(actionId);
		
		_callBPM(workCase, action,queueName,wobNumber, reasonId, remark, new HashSet<String>());
	}
	
	private void _callBPM(WorkCase workCase,Action action,String queueName,String wobNumber,int reasonId,String remark,HashSet<String> returnCodeSet) throws Exception{
		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("Action_Code", Long.toString(action.getId()));
        fields.put("Action_Name", action.getDescription());
        if (reasonId > 0) {
        	Reason reason = reasonDAO.findById(reasonId);
        	if (reason != null) {
	        	String reasonStr = reason.getCode() != null ? reason.getCode() : "";
	        	reasonStr = reason.getDescription() != null ? reasonStr + " - " + reason.getDescription() : reasonStr;
	        	fields.put("Reason", reasonStr);
	        	
	        	returnCodeSet.add(reason.getCode());
        	}
        }
        if (!Util.isEmpty(remark))
        	fields.put("Remarks", remark);
        
        String stepCode = workCase.getStep().getCode();
        long actionId = action.getId();
        //Add additional field
    	if ("3008".equals(stepCode)) { //Check Doc
    		_Before_3008_CheckDoc(workCase, actionId, fields, returnCodeSet);
    	} else if ("3023".equals(stepCode)) { // create update customer profile
    		_Before_3023_CreateCustProfile(workCase, actionId, fields);
    	} else if ("3029".equals(stepCode)) { //Generate Agreement
    		_Before_3029_GenerateAgreement(workCase, actionId, fields);
    	} else if ("3033".equals(stepCode)) { //Confirm mortgage registration
    		_Before_3033_ConfirmMortgage(workCase, actionId, fields);
    	} else if ("3035".equals(stepCode)) { //Confirm agreement sign fee
    		_Before_3035_ConfirmSign(workCase, actionId, fields);
    	} else if ("3036".equals(stepCode)) { //regenerate agreement
    		_Before_3036_RegenAgree(workCase, actionId, fields);
    	} else if ("3038".equals(stepCode)) { //review signed agreement
    		_Before_3038_ReviewSign(workCase, actionId, fields);
    	} else if ("3045".equals(stepCode)) {
    		_Before_3045_ReviewPerfection(workCase, actionId, fields);
    	} else if ("3046".equals(stepCode)) {
    		_Before_3046_RegenAgree_PerfectReview(workCase, actionId, fields);
    	} else if ("3049".equals(stepCode)) { //setup limit
    		_Before_3049_SetupLimit(workCase, actionId, fields);
    	}
        
        bpmExecutor.execute(queueName, wobNumber, fields);
        
        //After success
        if ("3002".equals(stepCode)) {
        	_3002_InsurancePremiumQuote(workCase, actionId);
        } else if ("3009".equals(stepCode)) {
        	_3009_FixDataInDecision(workCase, actionId);
        } else if ("3023".equals(stepCode)) {
        	_3023_CreateCustProfile(workCase, actionId);
        } else if ("3026".equals(stepCode)) {
        	_3026_OpenAccount(workCase, actionId);
        } else if ("3029".equals(stepCode)) {
        	_3029_GenerateAgree(workCase, actionId);
        } else if ("3034".equals(stepCode)) {
        	_3034_ResignAgree(workCase, actionId);
        } else if ("3035".equals(stepCode)) {
        	_3035_ConfirmSign(workCase, actionId);
        } else if ("3036".equals(stepCode)) {
        	_3036_RegenAgree(workCase, actionId);
        } else if ("3038".equals(stepCode)) {
        	_3038_ReviewSign(workCase, actionId);
        } else if ("3040".equals(stepCode)) {
        	_3040_PledgeCash(workCase, actionId);
        } else if ("3042".equals(stepCode)) {
        	_3042_CollectFee(workCase, actionId);
        } else if ("3046".equals(stepCode)) {
        	_3046_RegenAgree_PerfectReview(workCase, actionId);
        } else if ("3047".equals(stepCode)) {
        	_3047_ReviewPerfection_Resign(workCase, actionId);
        }
 	}
	
	/*
	 * Before Case
	 */
	private void _Before_3008_CheckDoc(WorkCase workCase,long actionId, HashMap<String,String> fields,HashSet<String> returnCodeSet) {
		boolean formC = returnCodeSet.contains("10100") && actionId == 1005;
		String formCFlag = formC ? "Y" : "N" ;
		fields.put("FormCFlag", formCFlag);
	}
	private void _Before_3023_CreateCustProfile(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String accOpenRequired = "N";
		fields.put("AccOpenRequired", accOpenRequired);
	}
	private void _Before_3029_GenerateAgreement(WorkCase workCase, long actionId,HashMap<String,String> fields) {
		String appointDateStr = "";
		String mortgageRequired = "N";
		if (actionId == ACTION_SUBMIT) {
			if (mortgageInfoDAO.countAllByWorkCaseId(workCase.getId()) > 0)
				mortgageRequired = "Y";
			AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCase.getId());
			if (agreementInfo != null && agreementInfo.getLoanContractDate() != null) {
				appointDateStr = DateTimeUtil.convertDateWorkFlowFormat(agreementInfo.getLoanContractDate());
			}
		}
		
		fields.put("AppointmentDate",appointDateStr);
		fields.put("MortgageRequired", mortgageRequired);
	}
	private void _Before_3033_ConfirmMortgage(WorkCase workCase,long actionId, HashMap<String, String> fields) {
		String appointDateStr = "";
		if (actionId == ACTION_SUBMIT) {
			AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCase.getId());
			if (agreementInfo != null && agreementInfo.getLoanContractDate() != null) {
				appointDateStr = DateTimeUtil.convertDateWorkFlowFormat(agreementInfo.getLoanContractDate());
			}
		}
		fields.put("AppointmentDate",appointDateStr);
	}
	private void _Before_3035_ConfirmSign(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String collectFee = "N";
		if (actionId == ACTION_SUBMIT) {
			//Checking from collect fee and is od or tcg ?
			BigDecimal grandTotalAgreement = BigDecimal.ZERO;
			List<List<FeeCollectionDetailView>> details = feeCalculationControl.getFeeCollectionDetails(workCase.getId());
			for (List<FeeCollectionDetailView> detailList : details) {
				if (detailList.isEmpty())
					continue;
				FeeCollectionDetailView firstView = detailList.get(0);
				if (!firstView.isAgreementSign()) {
					continue;
				}
				for (int i=0;i<detailList.size();i++) {
					FeeCollectionDetailView view = detailList.get(i);
					if (view.getAmount() != null)
						grandTotalAgreement = grandTotalAgreement.add(view.getAmount());
				}
			}
			if (grandTotalAgreement.compareTo(BigDecimal.ZERO) > 0) {
				//Checking from open account
				collectFee = "Y";
				List<OpenAccount> accounts = openAccountDAO.findByWorkCaseId(workCase.getId());
				if (accounts != null && !accounts.isEmpty()) {
					for (OpenAccount account : accounts) {
						List<OpenAccountPurpose> purposes = account.getOpenAccountPurposeList();
						if (purposes != null && !purposes.isEmpty()) {
							boolean isOD = false;
							boolean isTCG = false;
							for (OpenAccountPurpose purpose : purposes) {
								if (purpose.getAccountPurpose().isForOD())
									isOD = true;
								if (purpose.getAccountPurpose().isForTCG())
									isTCG = true;
							}
							if (isOD && isTCG) {
								collectFee = "N";
								break;
							}
						}
					}
				}
			}
			
		}
		fields.put("CollecFeeRequired", collectFee);
	}
	
	private void _Before_3036_RegenAgree(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String appointDateStr = "";
		if (actionId == ACTION_SUBMIT) {
			AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCase.getId());
			if (agreementInfo != null && agreementInfo.getLoanContractDate() != null) {
				appointDateStr = DateTimeUtil.convertDateWorkFlowFormat(agreementInfo.getLoanContractDate());
			}
		}
		fields.put("AppointmentDate", appointDateStr);
	}
	private void _Before_3038_ReviewSign(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String pledgeRequired = "N";
		if (actionId == ACTION_SUBMIT) {
			if (pledgeInfoDAO.countAllByWorkCaseId(workCase.getId()) > 0)
				pledgeRequired = "Y";
		}
		fields.put("PledgeRequired", pledgeRequired);
	}
	private void _Before_3045_ReviewPerfection(WorkCase workCase,long actionId,HashMap<String,String> fields) throws Exception {
		//validate that can be submitted or not
		if (actionId != ACTION_SUBMIT)
			return;
		AgreementInfo agree = agreementInfoDAO.findByWorkCaseId(workCase.getId());
		if (agree == null) {
			//ERROR
			String msg = message.get("exception.submit.postapp.reviewperfection.agreedate");
			throw new Exception(msg);
		}
		
		PerfectionReview review = perfectionReviewDAO.getPerfectionReviewByType(workCase.getId(), PerfectReviewType.FEE_COLLECTION);
		boolean hasCollect = review != null && PerfectReviewStatus.COMPLETE.equals(review.getStatus());
		Date agreeDate = _calculateSignDate(agree.getLoanContractDate());
		Date checkDate = null;
		if (hasCollect) {
			checkDate = _calculateSignDate(review.getCompletedDate());
		} else {
			checkDate = new Date(0);
			List<PledgeInfo> pledges = pledgeInfoDAO.findAllByWorkCaseId(workCase.getId());
			List<MortgageInfo> mortgages = mortgageInfoDAO.findAllByWorkCaseId(workCase.getId());
			TCGInfo tcg = tcgInfoDAO.findByWorkCaseId(workCase.getId());
			
			checkDate = _getMaxDate(checkDate, tcg.getApproveDate());
			if (pledges != null && !pledges.isEmpty()) {
				for (PledgeInfo pledge : pledges) {
					checkDate = _getMaxDate(checkDate, pledge.getPledgeSigningDate());
				}
			}
			if (mortgages != null && !mortgages.isEmpty()) {
				for (MortgageInfo mortgage : mortgages) {
					checkDate = _getMaxDate(checkDate, mortgage.getMortgageSigningDate());
				}
			}
			checkDate = _calculateSignDate(checkDate);
		}
		
		if (agreeDate != null && checkDate != null) {
			if (agreeDate.compareTo(checkDate) >= 0)
				return;
		}
		//ERROR
		String msg = message.get("exception.submit.postapp.reviewperfection.agreedate");
		throw new Exception(msg);
	}
	private void _Before_3046_RegenAgree_PerfectReview(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String appointDateStr = "";
		if (actionId == ACTION_SUBMIT) {
			AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCase.getId());
			if (agreementInfo != null && agreementInfo.getLoanContractDate() != null) {
				appointDateStr = DateTimeUtil.convertDateWorkFlowFormat(agreementInfo.getLoanContractDate());
			}
		}
		fields.put("AppointmentDate", appointDateStr);
	}
	
	private void _Before_3049_SetupLimit(WorkCase workCase, long actionId,HashMap<String, String> fields) {
		String disbursementRequired = "N";
		String basicCheckRequired = "N";
		if (actionId == ACTION_SUBMIT) {
			BigDecimal disbursementAmt = disbursementDAO.getTotalDisbursementAmount(workCase.getId());
			if (disbursementAmt != null && disbursementAmt.compareTo(BigDecimal.ZERO) > 0) {
				disbursementRequired = "Y";
			}	
		}
		
		//TODO -	BasicConditionCheckRequired
		
		fields.put("DisbursementRequired", disbursementRequired);
		fields.put("BasicConditionCheckRequired", basicCheckRequired);
	}
	
	/*
	 * After success case
	 */
	private void _3002_InsurancePremiumQuote(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Insurance Premium Quote (3002) , Action Submit CA (1015)
		BigDecimal amount;
		InsuranceInfo info = insuranceInfoDAO.findInsuranceInfoByWorkCaseId(workCase.getId());
		if (info != null && info.getTotalPremiumAmount() != null) {
			amount = info.getTotalPremiumAmount();
		} else {
			amount = BigDecimal.ZERO;
		}
		FeeType type = feeTypeDAO.findByDescription("Insurance Premium");
		if (type == null)
			return;
		ProposeFeeDetail model = feeDetailDAO.findByType(workCase.getId(), type.getId());
		if (model == null) {
			model = new ProposeFeeDetail();
			model.setPaymentMethod(feePaymentMethodDAO.findByBRMSCode("01"));
			model.setFeeType(type);
			model.setWorkCase(workCase);
		}
		model.setPercentFee(BigDecimal.ZERO);
		model.setPercentFeeAfter(BigDecimal.ZERO);
		model.setFeeYear(BigDecimal.ZERO);
		model.setAmount(amount);
		model.setFeeLevel(FeeLevel.APP_LEVEL);
		model.setDescription(null);
		model.setProposeType(null);
		model.setProposeCreditInfo(null);

		feeDetailDAO.save(model);
	}
	private void _3009_FixDataInDecision(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		mortgageSummaryControl.calculateMortgageSummary(workCase.getId());
	}
	private void _3023_CreateCustProfile(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Create/Update Customer Profile(3023), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.CUSTOMER,PerfectReviewStatus.COMPLETE);
		model.setRemark("Create Customer Profile Complete");
		persist(model);
	}
	private void _3026_OpenAccount(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Open Account(3026), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.ACCOUNT,PerfectReviewStatus.COMPLETE);
		model.setRemark("Open Account Complete");
		persist(model);
	}
	private void _3029_GenerateAgree(WorkCase workCase,long actionId) {
		if (actionId != 1036) //Gen agreement
			return;
		//Step Generate Agreement(3029), Action Generate Agreement (1036)
		comsInterface.generateAgreement(getCurrentUserID(), workCase.getId());
	}
	private void _3034_ResignAgree(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Re-Sign Agreement(3034), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.CONTRACT,PerfectReviewStatus.COMPLETE);
		model.setRemark("Agreement Sign Complete");
		persist(model);
	}
	private void _3035_ConfirmSign(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Confirm Agreement Sign & Collect Fee(3035), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.CONTRACT,PerfectReviewStatus.COMPLETE);
		model.setRemark("Agreement Sign Complete");
		persist(model);
	}
	private void _3036_RegenAgree(WorkCase workCase,long actionId) {
		if (actionId != 1036) //Gen agreement
			return;
		//Step Regenerate Agreement(3036), Action Generate Agreement (1036)
		comsInterface.generateAgreement(getCurrentUserID(), workCase.getId());
	}
	private void _3038_ReviewSign(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Reviewed Signed Agreement(Re-sign agreement)(3038), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.CONTRACT,PerfectReviewStatus.COMPLETE);
		model.setRemark("Agreement Sign Complete");
		persist(model);
	}
	private void _3040_PledgeCash(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Pledge Cash Collateral(3040), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.PLEDGE,PerfectReviewStatus.COMPLETE);
		model.setRemark("Pledge Complete");
		persist(model);
	}
	private void _3042_CollectFee(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step Collect Fee (3042), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.FEE_COLLECTION,PerfectReviewStatus.COMPLETE);
		model.setRemark("Collect Fee Complete");
		persist(model);
	}
	private void _3046_RegenAgree_PerfectReview(WorkCase workCase,long actionId) {
		if (actionId != 1036) //Gen agreement
			return;
		//Step Regenerate Agreement(3036), Action Generate Agreement (1036)
		comsInterface.generateAgreement(getCurrentUserID(), workCase.getId());
	}
	private void _3047_ReviewPerfection_Resign(WorkCase workCase,long actionId) {
		if (actionId != ACTION_SUBMIT)
			return;
		//Step 3047	Review Perfection (Re - Sign Agreement), Action Submit CA (1015)
		PerfectionReview model = createPerfectionReview(workCase,PerfectReviewType.CONTRACT,PerfectReviewStatus.COMPLETE);
		model.setRemark("Agreement Sign Complete");
		persist(model);
	}
		
	private void persist(PerfectionReview model) {
		if (model.getId() <= 0)
			perfectionReviewDAO.save(model);
		else
			perfectionReviewDAO.update(model);
	}
	private PerfectionReview createPerfectionReview(WorkCase workCase,PerfectReviewType type,PerfectReviewStatus status) {
		Date currDate = new Date();
		User user = getCurrentUser();
		PerfectionReview model = perfectionReviewDAO.getPerfectionReviewByType(workCase.getId(), type);
		if (model == null) {
			model = new PerfectionReview();
			model.setCreateBy(user);
			model.setCreateDate(currDate);
			model.setWorkCase(workCase);
		} 
		
		model.setStatus(status);
		model.setDate(currDate);
		if (PerfectReviewStatus.COMPLETE.equals(status))
			model.setCompletedDate(currDate);
		else
			model.setCompletedDate(null);
		model.setModifyDate(currDate);
		model.setModifyBy(user);
		return model;
	}
	private Date _calculateSignDate(Date input) {
		if (input == null)
			return null;
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.setTime(input);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 1);
		calendar.set(Calendar.MILLISECOND,0);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		if (day <= 15) {
			calendar.set(Calendar.DAY_OF_MONTH, 15);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return calendar.getTime();
	}
	
	private Date _getMaxDate(Date date1,Date date2) {
		if (date2 == null)
			return date1;
		if (date1.compareTo(date2) < 0)
			return date2;
		else
			return date1;
	}
}
