package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingIntTier;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.FeeLevel;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class ProposeLineTransform extends Transform {
    @Inject
    @SELOS
    Logger log;

    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ProposeCreditInfoDAO proposeCreditInfoDAO;
    @Inject
    private ProposeCreditInfoTierDetailDAO proposeCreditInfoTierDetailDAO;
    @Inject
    private ProposeConditionDAO proposeConditionDAO;
    @Inject
    private ProposeGuarantorInfoDAO proposeGuarantorInfoDAO;
    @Inject
    private ProposeGuarantorInfoRelationDAO proposeGuarantorInfoRelationDAO;
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private FeeTypeDAO feeTypeDAO;
    @Inject
    private FeePaymentMethodDAO feePaymentMethodDAO;
    @Inject
    private ProposeFeeDetailDAO proposeFeeDetailDAO;
    @Inject
    private ProposeCollateralInfoDAO proposeCollateralInfoDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO proposeCollateralInfoRelationDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO proposeCollateralInfoHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO proposeCollateralInfoSubDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO proposeCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubMortgageDAO proposeCollateralSubMortgageDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO proposeCollateralSubRelatedDAO;
    @Inject
    private DecisionFollowConditionDAO decisionFollowConditionDAO;
    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;

    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private ProductTransform productTransform;
    @Inject
    private LoanPurposeTransform loanPurposeTransform;
    @Inject
    private DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    private BaseRateTransform baseRateTransform;
    @Inject
    private CustomerTransform customerTransform;
    @Inject
    private FeeTransform feeTransform;
    @Inject
    private MortgageTypeTransform mortgageTypeTransform;
    @Inject
    private DecisionFollowConditionTransform decisionFollowConditionTransform;
    @Inject
    private ApprovalHistoryTransform approvalHistoryTransform;
    @Inject
    private AADDecisionDAO aadDecisionDAO;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    public ProposeLineTransform() {
    }

    //-------------------------------------------------------- Propose Line --------------------------------------------------------//

    public ProposeLine transformProposeLineToModel(ProposeLineView proposeLineView, WorkCase workCase, User user, ProposeType proposeType) {
        ProposeLine proposeLine = null;
        if(!Util.isNull(proposeLineView)){
            proposeLine = new ProposeLine();
            if (!Util.isZero(proposeLineView.getId())) {
                proposeLine = proposeLineDAO.findById(proposeLineView.getId());
            } else {
                proposeLine.setCreateDate(new Date());
                proposeLine.setCreateBy(user);
            }
            proposeLine.setModifyDate(new Date());
            proposeLine.setModifyBy(user);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeLine.setWorkCase(workCase);
                } else {
                    proposeLine.setWorkCase(null);
                }
            } else {
                proposeLine.setWorkCase(workCase);
            }

            proposeLine.setWcNeed(proposeLineView.getWcNeed());
            proposeLine.setTotalWCTmb(proposeLineView.getTotalWCTmb());
            proposeLine.setWcNeedDiffer(proposeLineView.getWcNeedDiffer());
            proposeLine.setTotalWCDebit(proposeLineView.getTotalWCDebit());
            proposeLine.setTotalLoanWCTMB(proposeLineView.getTotalLoanWCTMB());

            proposeLine.setCase1WCLimit(proposeLineView.getCase1WCLimit());
            proposeLine.setCase1WCMinLimit(proposeLineView.getCase1WCMinLimit());
            proposeLine.setCase1WC50CoreWC(proposeLineView.getCase1WC50CoreWC());
            proposeLine.setCase1WCDebitCoreWC(proposeLineView.getCase1WCDebitCoreWC());

            proposeLine.setCase2WCLimit(proposeLineView.getCase2WCLimit());
            proposeLine.setCase2WCMinLimit(proposeLineView.getCase2WCMinLimit());
            proposeLine.setCase2WC50CoreWC(proposeLineView.getCase2WC50CoreWC());
            proposeLine.setCase2WCDebitCoreWC(proposeLineView.getCase2WCDebitCoreWC());

            proposeLine.setCase3WCLimit(proposeLineView.getCase3WCLimit());
            proposeLine.setCase3WCMinLimit(proposeLineView.getCase3WCMinLimit());
            proposeLine.setCase3WC50CoreWC(proposeLineView.getCase3WC50CoreWC());
            proposeLine.setCase3WCDebitCoreWC(proposeLineView.getCase3WCDebitCoreWC());

            proposeLine.setCreditCustomerType(proposeLineView.getCreditCustomerType().value());
            proposeLine.setLoanRequestType(creditRequestTypeTransform.transformSelectToModel(proposeLineView.getLoanRequestType()));
            proposeLine.setInvestedCountry(countryTransform.transformSelectToModel(proposeLineView.getInvestedCountry()));
            proposeLine.setExistingSMELimit(proposeLineView.getExistingSMELimit());
            proposeLine.setMaximumSMELimit(proposeLineView.getMaximumSMELimit());

            proposeLine.setContactName(proposeLineView.getContactName());
            proposeLine.setContactPhoneNo(proposeLineView.getContactPhoneNo());
            proposeLine.setInterService(proposeLineView.getInterService());
            proposeLine.setCurrentAddress(proposeLineView.getCurrentAddress());
            proposeLine.setRegisteredAddress(proposeLineView.getRegisteredAddress());
            proposeLine.setImportMail(proposeLineView.getImportMail());
            proposeLine.setExportMail(proposeLineView.getExportMail());
            proposeLine.setDepositBranchCode(proposeLineView.getDepositBranchCode());
            proposeLine.setOwnerBranchCode(proposeLineView.getOwnerBranchCode());

            proposeLine.setIntFeeDOA(proposeLineView.getIntFeeDOA());
            proposeLine.setFrontendFeeDOA(proposeLineView.getFrontendFeeDOA());
            proposeLine.setGuarantorBA(proposeLineView.getGuarantorBA());
            proposeLine.setReasonForReduction(proposeLineView.getReasonForReduction());

            proposeLine.setRelatedTMBLending(proposeLineView.getRelatedTMBLending());
            proposeLine.setTwentyFivePercentShareRelatedTMBLending(proposeLineView.getTwentyFivePercentShareRelatedTMBLending());
            proposeLine.setSingleLendingLimit(proposeLineView.getSingleLendingLimit());

            proposeLine.setTotalPropose(proposeLineView.getTotalPropose());
            proposeLine.setTotalCommercial(proposeLineView.getTotalCommercial());
            proposeLine.setTotalCommercialAndOBOD(proposeLineView.getTotalCommercialAndOBOD());
            proposeLine.setTotalExposure(proposeLineView.getTotalExposure());

            proposeLine.setTotalGuaranteeAmount(proposeLineView.getTotalGuaranteeAmount());

            proposeLine.setTotalProposeLoanDBR(proposeLineView.getTotalProposeLoanDBR());
            proposeLine.setTotalProposeNonLoanDBR(proposeLineView.getTotalProposeNonLoanDBR());
            proposeLine.setTotalNumberOfNewOD(proposeLineView.getTotalNumberOfNewOD());
            proposeLine.setTotalNumberProposeCreditFac(proposeLineView.getTotalNumberProposeCreditFac());
            proposeLine.setTotalNumberContingenPropose(proposeLineView.getTotalNumberContingenPropose());
            proposeLine.setTotalNumberOfCoreAsset(proposeLineView.getTotalNumberOfCoreAsset());
            proposeLine.setTotalNumberOfNonCoreAsset(proposeLineView.getTotalNumberOfNonCoreAsset());
            proposeLine.setTotalMortgageValue(proposeLineView.getTotalMortgageValue());
            proposeLine.setTotalTCGGuaranteeAmount(proposeLineView.getTotalTCGGuaranteeAmount());
            proposeLine.setTotalIndvGuaranteeAmount(proposeLineView.getTotalIndvGuaranteeAmount());
            proposeLine.setTotalJurisGuaranteeAmount(proposeLineView.getTotalJurisGuaranteeAmount());

            proposeLine.setProposeCreditInfoList(transformProposeCreditToModelList(proposeLine, proposeLineView.getProposeCreditInfoDetailViewList(), workCase, user, proposeType));

            proposeLine.setProposeConditionInfoList(transformProposeConditionToModelList(proposeLine, proposeLineView.getProposeConditionInfoViewList(), user, proposeType));
        }
        return proposeLine;
    }

    public ProposeLineView transformProposeLineToView(ProposeLine proposeLine, ProposeType proposeType) {
        ProposeLineView proposeLineView = new ProposeLineView();
        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())){
            proposeLineView.setId(proposeLine.getId());

            proposeLineView.setCreateDate(proposeLine.getCreateDate());
            proposeLineView.setCreateBy(proposeLine.getCreateBy());
            proposeLineView.setModifyDate(proposeLine.getModifyDate());
            proposeLineView.setModifyBy(proposeLine.getModifyBy());

            proposeLineView.setWcNeed(proposeLine.getWcNeed());
            proposeLineView.setTotalWCTmb(proposeLine.getTotalWCTmb());
            proposeLineView.setWcNeedDiffer(proposeLine.getWcNeedDiffer());
            proposeLineView.setTotalWCDebit(proposeLine.getTotalWCDebit());
            proposeLineView.setTotalLoanWCTMB(proposeLine.getTotalLoanWCTMB());

            proposeLineView.setCase1WCLimit(proposeLine.getCase1WCLimit());
            proposeLineView.setCase1WCMinLimit(proposeLine.getCase1WCMinLimit());
            proposeLineView.setCase1WC50CoreWC(proposeLine.getCase1WC50CoreWC());
            proposeLineView.setCase1WCDebitCoreWC(proposeLine.getCase1WCDebitCoreWC());

            proposeLineView.setCase2WCLimit(proposeLine.getCase2WCLimit());
            proposeLineView.setCase2WCMinLimit(proposeLine.getCase2WCMinLimit());
            proposeLineView.setCase2WC50CoreWC(proposeLine.getCase2WC50CoreWC());
            proposeLineView.setCase2WCDebitCoreWC(proposeLine.getCase2WCDebitCoreWC());

            proposeLineView.setCase3WCLimit(proposeLine.getCase3WCLimit());
            proposeLineView.setCase3WCMinLimit(proposeLine.getCase3WCMinLimit());
            proposeLineView.setCase3WC50CoreWC(proposeLine.getCase3WC50CoreWC());
            proposeLineView.setCase3WCDebitCoreWC(proposeLine.getCase3WCDebitCoreWC());

            proposeLineView.setCreditCustomerType(proposeLine.getCreditCustomerType() == 2 ? CreditCustomerType.PRIME : proposeLine.getCreditCustomerType() == 1 ? CreditCustomerType.NORMAL : CreditCustomerType.NOT_SELECTED);
            proposeLineView.setLoanRequestType(creditRequestTypeTransform.transformToView(proposeLine.getLoanRequestType()));
            proposeLineView.setInvestedCountry(countryTransform.transformToView(proposeLine.getInvestedCountry()));
            proposeLineView.setExistingSMELimit(proposeLine.getExistingSMELimit());
            proposeLineView.setMaximumSMELimit(proposeLine.getMaximumSMELimit());

            proposeLineView.setContactName(proposeLine.getContactName());
            proposeLineView.setContactPhoneNo(proposeLine.getContactPhoneNo());
            proposeLineView.setInterService(proposeLine.getInterService());
            proposeLineView.setCurrentAddress(proposeLine.getCurrentAddress());
            proposeLineView.setRegisteredAddress(proposeLine.getRegisteredAddress());
            proposeLineView.setImportMail(proposeLine.getImportMail());
            proposeLineView.setExportMail(proposeLine.getExportMail());
            proposeLineView.setDepositBranchCode(proposeLine.getDepositBranchCode());
            proposeLineView.setOwnerBranchCode(proposeLine.getOwnerBranchCode());

            proposeLineView.setIntFeeDOA(proposeLine.getIntFeeDOA());
            proposeLineView.setFrontendFeeDOA(proposeLine.getFrontendFeeDOA());
            proposeLineView.setGuarantorBA(proposeLine.getGuarantorBA());
            proposeLineView.setReasonForReduction(proposeLine.getReasonForReduction());

            proposeLineView.setRelatedTMBLending(proposeLine.getRelatedTMBLending());
            proposeLineView.setTwentyFivePercentShareRelatedTMBLending(proposeLine.getTwentyFivePercentShareRelatedTMBLending());
            proposeLineView.setSingleLendingLimit(proposeLine.getSingleLendingLimit());

            proposeLineView.setTotalPropose(proposeLine.getTotalPropose());
            proposeLineView.setTotalCommercial(proposeLine.getTotalCommercial());
            proposeLineView.setTotalCommercialAndOBOD(proposeLine.getTotalCommercialAndOBOD());
            proposeLineView.setTotalExposure(proposeLine.getTotalExposure());

            proposeLineView.setTotalGuaranteeAmount(proposeLine.getTotalGuaranteeAmount());

            proposeLineView.setTotalProposeLoanDBR(proposeLine.getTotalProposeLoanDBR());
            proposeLineView.setTotalProposeNonLoanDBR(proposeLine.getTotalProposeNonLoanDBR());
            proposeLineView.setTotalNumberOfNewOD(proposeLine.getTotalNumberOfNewOD());
            proposeLineView.setTotalNumberProposeCreditFac(proposeLine.getTotalNumberProposeCreditFac());
            proposeLineView.setTotalNumberContingenPropose(proposeLine.getTotalNumberContingenPropose());
            proposeLineView.setTotalNumberOfCoreAsset(proposeLine.getTotalNumberOfCoreAsset());
            proposeLineView.setTotalNumberOfNonCoreAsset(proposeLine.getTotalNumberOfNonCoreAsset());
            proposeLineView.setTotalMortgageValue(proposeLine.getTotalMortgageValue());
            proposeLineView.setTotalTCGGuaranteeAmount(proposeLine.getTotalTCGGuaranteeAmount());
            proposeLineView.setTotalIndvGuaranteeAmount(proposeLine.getTotalIndvGuaranteeAmount());
            proposeLineView.setTotalJurisGuaranteeAmount(proposeLine.getTotalJurisGuaranteeAmount());

            proposeLineView.setProposeCreditInfoDetailViewList(transformProposeCreditToViewList(proposeLine.getProposeCreditInfoList(), proposeType));

            proposeLineView.setProposeConditionInfoViewList(transformProposeConditionToViewList(proposeLine.getProposeConditionInfoList(), proposeType));

            proposeLineView.setProposeGuarantorInfoViewList(transformProposeGuarantorToViewList(proposeLine.getProposeGuarantorInfoList(), proposeType));

            List<ProposeFeeDetail> proposeFeeDetailList = proposeFeeDetailDAO.findByWorkCaseId(proposeLine.getWorkCase().getId(), proposeType);
            List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = transformProposeFeeToViewList(proposeFeeDetailList, proposeType);
            List<ProposeFeeDetailView> proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();

            Map<String, ProposeFeeDetailView> proposeFeeDetailViewMap = new HashMap<String, ProposeFeeDetailView>();
            if(!Util.isNull(proposeFeeDetailViewOriginalList) && !Util.isZero(proposeFeeDetailViewOriginalList.size())) {
                for(ProposeFeeDetailView proFeeDetView : proposeFeeDetailViewOriginalList) {
                    if(!proposeFeeDetailViewMap.containsKey(proFeeDetView.getProductProgram())){
                        proposeFeeDetailViewMap.put(proFeeDetView.getProductProgram(), proFeeDetView);
                        proposeFeeDetailViewList.add(proFeeDetView);
                    }
                }
            }

            proposeLineView.setProposeFeeDetailViewList(proposeFeeDetailViewList);
            proposeLineView.setProposeFeeDetailViewOriginalList(proposeFeeDetailViewOriginalList);

            proposeLineView.setProposeCollateralInfoViewList(transformProposeCollateralToViewList(proposeLine.getProposeCollateralInfoList(), proposeType));
        }

        return proposeLineView;
    }

    public ProposeLineView transformProposeLineToReport(ProposeLine proposeLine, ProposeType proposeType) {
        ProposeLineView proposeLineView = new ProposeLineView();
        if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())){
            proposeLineView.setProposeCreditInfoDetailViewList(transformProposeCreditToReportList(proposeLine.getProposeCreditInfoList(), proposeType));
        }
        return proposeLineView;
    }

    public List<ProposeCreditInfoDetailView> transformProposeCreditToReportList(List<ProposeCreditInfo> proposeCreditInfoList, ProposeType proposeType) {
        List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if (!Util.isNull(proposeCreditInfoList)) {
            for (ProposeCreditInfo proCredit : proposeCreditInfoList) {
                ProposeCreditInfoDetailView proposeCreditInfoDetailView = transformProposeCreditToReport(proCredit, proposeType);
                if(!Util.isNull(proposeCreditInfoDetailView)) {
                    proposeCreditInfoDetailViewList.add(proposeCreditInfoDetailView);
                }
            }
        }
        return proposeCreditInfoDetailViewList;
    }

    public ProposeCreditInfoDetailView transformProposeCreditToReport(ProposeCreditInfo proposeCreditInfo, ProposeType proposeType) {
        ProposeCreditInfoDetailView proposeCreditInfoDetailView = null;
        if(!Util.isNull(proposeCreditInfo) && !Util.isZero(proposeCreditInfo.getId()) && proposeCreditInfo.getProposeType() == proposeType && proposeCreditInfo.getUwDecision() == DecisionType.APPROVED){
            proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();

            proposeCreditInfoDetailView.setId(proposeCreditInfo.getId());

            proposeCreditInfoDetailView.setRequestType(proposeCreditInfo.getRequestType());
            proposeCreditInfoDetailView.setRefinance(proposeCreditInfo.getRefinance());
            proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(proposeCreditInfo.getProductProgram()));
            proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(proposeCreditInfo.getCreditType()));
            proposeCreditInfoDetailView.setProductCode(proposeCreditInfo.getProductCode());
            proposeCreditInfoDetailView.setProjectCode(proposeCreditInfo.getProjectCode());
            proposeCreditInfoDetailView.setLimit(proposeCreditInfo.getLimit());
            proposeCreditInfoDetailView.setPCEPercent(proposeCreditInfo.getPcePercent());
            proposeCreditInfoDetailView.setPCEAmount(proposeCreditInfo.getPceAmount());
            proposeCreditInfoDetailView.setReducePriceFlag(Util.isTrue(proposeCreditInfo.getReducePriceFlag()));
            proposeCreditInfoDetailView.setReduceFrontEndFee(Util.isTrue(proposeCreditInfo.getReduceFrontEndFee()));
            proposeCreditInfoDetailView.setFrontEndFee(proposeCreditInfo.getFrontEndFee());
            proposeCreditInfoDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(proposeCreditInfo.getLoanPurpose()));
            proposeCreditInfoDetailView.setProposeDetail(proposeCreditInfo.getRemark());
            proposeCreditInfoDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(proposeCreditInfo.getDisbursementType()));
            proposeCreditInfoDetailView.setHoldLimitAmount(proposeCreditInfo.getHoldLimitAmount());
            proposeCreditInfoDetailView.setUseCount(proposeCreditInfo.getUseCount());
            proposeCreditInfoDetailView.setSeq(proposeCreditInfo.getSeq());
            proposeCreditInfoDetailView.setInstallment(proposeCreditInfo.getInstallment());
            proposeCreditInfoDetailView.setFrontEndFeeOriginal(proposeCreditInfo.getFrontEndFeeOriginal());
            proposeCreditInfoDetailView.setStandardInterest(proposeCreditInfo.getStandardInterest());
            proposeCreditInfoDetailView.setStandardBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getStandardBasePrice()));
            proposeCreditInfoDetailView.setSuggestInterest(proposeCreditInfo.getSuggestInterest());
            proposeCreditInfoDetailView.setSuggestBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getSuggestBasePrice()));
            proposeCreditInfoDetailView.setLastNo(proposeCreditInfo.getLastNo());
            proposeCreditInfoDetailView.setSetupCompleted(proposeCreditInfo.getSetupCompleted());

            proposeCreditInfoDetailView.setUwDecision(proposeCreditInfo.getUwDecision());

            List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList = transformProposeCreditTierToViewList(proposeCreditInfo.getProposeCreditInfoTierDetailList(), proposeCreditInfoDetailView);
            proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(orderTierDetail(proposeCreditInfoTierDetailViewList, proposeCreditInfoDetailView));
        }

        return proposeCreditInfoDetailView;
    }

    //-------------------------------------------------------- Propose Credit Info --------------------------------------------------------//

    public ProposeCreditInfo transformProposeCreditToModel(ProposeLine proposeLine, ProposeCreditInfoDetailView proposeCreditInfoDetailView, WorkCase workCase, User user, ProposeType proposeType) {
        ProposeCreditInfo proposeCreditInfo = null;
        if(!Util.isNull(proposeCreditInfoDetailView)){
            proposeCreditInfo = new ProposeCreditInfo();
            if (!Util.isZero(proposeCreditInfoDetailView.getId())) {
                proposeCreditInfo = proposeCreditInfoDAO.findById(proposeCreditInfoDetailView.getId());
            } else {
                proposeCreditInfo.setCreateDate(new Date());
                proposeCreditInfo.setCreateBy(user);
            }
            proposeCreditInfo.setModifyDate(new Date());
            proposeCreditInfo.setModifyBy(user);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeCreditInfo.setWorkCase(workCase);
                } else {
                    proposeCreditInfo.setWorkCase(null);
                }
            } else {
                proposeCreditInfo.setWorkCase(workCase);
            }

            proposeCreditInfo.setProposeType(proposeType);
            proposeCreditInfo.setProposeLine(proposeLine);

            proposeCreditInfo.setRequestType(proposeCreditInfoDetailView.getRequestType());
            proposeCreditInfo.setRefinance(proposeCreditInfoDetailView.getRefinance());
            proposeCreditInfo.setProductProgram(productTransform.transformToModel(proposeCreditInfoDetailView.getProductProgramView()));
            proposeCreditInfo.setCreditType(productTransform.transformToModel(proposeCreditInfoDetailView.getCreditTypeView()));
            proposeCreditInfo.setProductCode(proposeCreditInfoDetailView.getProductCode());
            proposeCreditInfo.setProjectCode(proposeCreditInfoDetailView.getProjectCode());
            proposeCreditInfo.setLimit(proposeCreditInfoDetailView.getLimit());
            proposeCreditInfo.setPcePercent(proposeCreditInfoDetailView.getPCEPercent());
            proposeCreditInfo.setPceAmount(proposeCreditInfoDetailView.getPCEAmount());
            proposeCreditInfo.setReducePriceFlag(Util.isTrue(proposeCreditInfoDetailView.isReducePriceFlag()));
            proposeCreditInfo.setReduceFrontEndFee(Util.isTrue(proposeCreditInfoDetailView.isReduceFrontEndFee()));
            proposeCreditInfo.setFrontEndFee(proposeCreditInfoDetailView.getFrontEndFee());
            proposeCreditInfo.setLoanPurpose(loanPurposeTransform.transformToModel(proposeCreditInfoDetailView.getLoanPurposeView()));
            proposeCreditInfo.setRemark(proposeCreditInfoDetailView.getProposeDetail());
            proposeCreditInfo.setDisbursementType(disbursementTypeTransform.transformToModel(proposeCreditInfoDetailView.getDisbursementTypeView()));
            proposeCreditInfo.setHoldLimitAmount(proposeCreditInfoDetailView.getHoldLimitAmount());
            proposeCreditInfo.setUseCount(proposeCreditInfoDetailView.getUseCount());
            proposeCreditInfo.setSeq(proposeCreditInfoDetailView.getSeq());
            proposeCreditInfo.setInstallment(proposeCreditInfoDetailView.getInstallment());
            proposeCreditInfo.setFrontEndFeeOriginal(proposeCreditInfoDetailView.getFrontEndFeeOriginal());
            proposeCreditInfo.setStandardInterest(proposeCreditInfoDetailView.getStandardInterest());
            proposeCreditInfo.setStandardBasePrice(baseRateTransform.transformToModel(proposeCreditInfoDetailView.getStandardBaseRate()));
            proposeCreditInfo.setSuggestInterest(proposeCreditInfoDetailView.getSuggestInterest());
            proposeCreditInfo.setSuggestBasePrice(baseRateTransform.transformToModel(proposeCreditInfoDetailView.getSuggestBaseRate()));
            proposeCreditInfo.setLastNo(proposeCreditInfoDetailView.getLastNo());
            proposeCreditInfo.setSetupCompleted(proposeCreditInfoDetailView.getSetupCompleted());

            proposeCreditInfo.setUwDecision(proposeCreditInfoDetailView.getUwDecision());

            proposeCreditInfo.setProposeCreditInfoTierDetailList(transformProposeCreditTierToModelList(proposeCreditInfo, proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList(), user));
        }

        return proposeCreditInfo;
    }

    public ProposeCreditInfoDetailView transformProposeCreditToView(ProposeCreditInfo proposeCreditInfo, ProposeType proposeType) {
        ProposeCreditInfoDetailView proposeCreditInfoDetailView = null;
        if(!Util.isNull(proposeCreditInfo) && !Util.isZero(proposeCreditInfo.getId()) && proposeCreditInfo.getProposeType() == proposeType){
            proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();

            proposeCreditInfoDetailView.setId(proposeCreditInfo.getId());

            proposeCreditInfoDetailView.setRequestType(proposeCreditInfo.getRequestType());
            proposeCreditInfoDetailView.setRefinance(proposeCreditInfo.getRefinance());
            proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(proposeCreditInfo.getProductProgram()));
            proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(proposeCreditInfo.getCreditType()));
            proposeCreditInfoDetailView.setProductCode(proposeCreditInfo.getProductCode());
            proposeCreditInfoDetailView.setProjectCode(proposeCreditInfo.getProjectCode());
            proposeCreditInfoDetailView.setLimit(proposeCreditInfo.getLimit());
            proposeCreditInfoDetailView.setPCEPercent(proposeCreditInfo.getPcePercent());
            proposeCreditInfoDetailView.setPCEAmount(proposeCreditInfo.getPceAmount());
            proposeCreditInfoDetailView.setReducePriceFlag(Util.isTrue(proposeCreditInfo.getReducePriceFlag()));
            proposeCreditInfoDetailView.setReduceFrontEndFee(Util.isTrue(proposeCreditInfo.getReduceFrontEndFee()));
            proposeCreditInfoDetailView.setFrontEndFee(proposeCreditInfo.getFrontEndFee());
            proposeCreditInfoDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(proposeCreditInfo.getLoanPurpose()));
            proposeCreditInfoDetailView.setProposeDetail(proposeCreditInfo.getRemark());
            proposeCreditInfoDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(proposeCreditInfo.getDisbursementType()));
            proposeCreditInfoDetailView.setHoldLimitAmount(proposeCreditInfo.getHoldLimitAmount());
            proposeCreditInfoDetailView.setUseCount(proposeCreditInfo.getUseCount());
            proposeCreditInfoDetailView.setSeq(proposeCreditInfo.getSeq());
            proposeCreditInfoDetailView.setInstallment(proposeCreditInfo.getInstallment());
            proposeCreditInfoDetailView.setFrontEndFeeOriginal(proposeCreditInfo.getFrontEndFeeOriginal());
            proposeCreditInfoDetailView.setStandardInterest(proposeCreditInfo.getStandardInterest());
            proposeCreditInfoDetailView.setStandardBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getStandardBasePrice()));
            proposeCreditInfoDetailView.setSuggestInterest(proposeCreditInfo.getSuggestInterest());
            proposeCreditInfoDetailView.setSuggestBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getSuggestBasePrice()));
            proposeCreditInfoDetailView.setLastNo(proposeCreditInfo.getLastNo());
            proposeCreditInfoDetailView.setSetupCompleted(proposeCreditInfo.getSetupCompleted());

            proposeCreditInfoDetailView.setUwDecision(proposeCreditInfo.getUwDecision());

            List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList = transformProposeCreditTierToViewList(proposeCreditInfo.getProposeCreditInfoTierDetailList(), proposeCreditInfoDetailView);
            proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(orderTierDetail(proposeCreditInfoTierDetailViewList, proposeCreditInfoDetailView));
        }

        return proposeCreditInfoDetailView;
    }

    public List<ProposeCreditInfo> transformProposeCreditToModelList(ProposeLine proposeLine, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, WorkCase workCase, User user, ProposeType proposeType) {
        List<ProposeCreditInfo> proposeCreditInfoList = new ArrayList<ProposeCreditInfo>();
        if (!Util.isNull(proposeCreditInfoDetailViewList)) {
            for (ProposeCreditInfoDetailView proCreditView : proposeCreditInfoDetailViewList) {
                ProposeCreditInfo proposeCreditInfo = transformProposeCreditToModel(proposeLine, proCreditView, workCase, user, proposeType);
                proposeCreditInfoList.add(proposeCreditInfo);
            }
        }
        return proposeCreditInfoList;
    }

    public List<ProposeCreditInfoDetailView> transformProposeCreditToViewList(List<ProposeCreditInfo> proposeCreditInfoList, ProposeType proposeType) {
        List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if (!Util.isNull(proposeCreditInfoList)) {
            for (ProposeCreditInfo proCredit : proposeCreditInfoList) {
                ProposeCreditInfoDetailView proposeCreditInfoDetailView = transformProposeCreditToView(proCredit, proposeType);
                if(!Util.isNull(proposeCreditInfoDetailView)) {
                    proposeCreditInfoDetailViewList.add(proposeCreditInfoDetailView);
                }
            }
        }
        return proposeCreditInfoDetailViewList;
    }

    //for only show in screen
    public ProposeCreditInfoDetailView transformProposeCreditToViewScreen(ProposeCreditInfo proposeCreditInfo, BigDecimal guaranteeAmount) {
        ProposeCreditInfoDetailView proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();
        if(!Util.isNull(proposeCreditInfo) && !Util.isZero(proposeCreditInfo.getId())){
            proposeCreditInfoDetailView.setId(proposeCreditInfo.getId());

            proposeCreditInfoDetailView.setRequestType(proposeCreditInfo.getRequestType());
            proposeCreditInfoDetailView.setRefinance(proposeCreditInfo.getRefinance());
            proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(proposeCreditInfo.getProductProgram()));
            proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(proposeCreditInfo.getCreditType()));
            proposeCreditInfoDetailView.setProductCode(proposeCreditInfo.getProductCode());
            proposeCreditInfoDetailView.setProjectCode(proposeCreditInfo.getProjectCode());
            proposeCreditInfoDetailView.setLimit(proposeCreditInfo.getLimit());
            proposeCreditInfoDetailView.setPCEPercent(proposeCreditInfo.getPcePercent());
            proposeCreditInfoDetailView.setPCEAmount(proposeCreditInfo.getPceAmount());
            proposeCreditInfoDetailView.setReducePriceFlag(Util.isTrue(proposeCreditInfo.getReducePriceFlag()));
            proposeCreditInfoDetailView.setReduceFrontEndFee(Util.isTrue(proposeCreditInfo.getReduceFrontEndFee()));
            proposeCreditInfoDetailView.setFrontEndFee(proposeCreditInfo.getFrontEndFee());
            proposeCreditInfoDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(proposeCreditInfo.getLoanPurpose()));
            proposeCreditInfoDetailView.setProposeDetail(proposeCreditInfo.getRemark());
            proposeCreditInfoDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(proposeCreditInfo.getDisbursementType()));
            proposeCreditInfoDetailView.setHoldLimitAmount(proposeCreditInfo.getHoldLimitAmount());
            proposeCreditInfoDetailView.setUseCount(proposeCreditInfo.getUseCount());
            proposeCreditInfoDetailView.setSeq(proposeCreditInfo.getSeq());
            proposeCreditInfoDetailView.setInstallment(proposeCreditInfo.getInstallment());
            proposeCreditInfoDetailView.setStandardInterest(proposeCreditInfo.getStandardInterest());
            proposeCreditInfoDetailView.setStandardBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getStandardBasePrice()));
            proposeCreditInfoDetailView.setSuggestInterest(proposeCreditInfo.getSuggestInterest());
            proposeCreditInfoDetailView.setSuggestBaseRate(baseRateTransform.transformToView(proposeCreditInfo.getSuggestBasePrice()));
            proposeCreditInfoDetailView.setFrontEndFeeOriginal(proposeCreditInfo.getFrontEndFeeOriginal());
            proposeCreditInfoDetailView.setLastNo(proposeCreditInfo.getLastNo());

            proposeCreditInfoDetailView.setGuaranteeAmount(guaranteeAmount);
            proposeCreditInfoDetailView.setNoFlag(true);

            proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(transformProposeCreditTierToViewList(proposeCreditInfo.getProposeCreditInfoTierDetailList(), proposeCreditInfoDetailView));
        }

        return proposeCreditInfoDetailView;
    }

    //for only show in screen
    public ProposeCreditInfoDetailView transformProposeCreditToViewByExisting(ExistingCreditDetail existingCreditDetail, BigDecimal guaranteeAmount) {
        ProposeCreditInfoDetailView proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();
        if(!Util.isNull(existingCreditDetail) && !Util.isZero(existingCreditDetail.getId())){
            proposeCreditInfoDetailView.setId(existingCreditDetail.getId());
            proposeCreditInfoDetailView.setAccountName(existingCreditDetail.getAccountName());
            proposeCreditInfoDetailView.setAccountNumber(existingCreditDetail.getAccountNumber());
            proposeCreditInfoDetailView.setAccountSuf(existingCreditDetail.getAccountSuf());
            proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(existingCreditDetail.getExistProductProgram()));
            proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(existingCreditDetail.getExistCreditType()));
            proposeCreditInfoDetailView.setLimit(existingCreditDetail.getLimit());
            proposeCreditInfoDetailView.setGuaranteeAmount(guaranteeAmount);
            proposeCreditInfoDetailView.setExistingCredit(true);
            proposeCreditInfoDetailView.setNoFlag(true);
        }

        return proposeCreditInfoDetailView;
    }

    //-------------------------------------------------------- Propose Credit Info Tier --------------------------------------------------------//

    public ProposeCreditInfoTierDetail transformProposeCreditTierToModel(ProposeCreditInfo proposeCreditInfo, ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView, User user) {
        ProposeCreditInfoTierDetail proposeCreditInfoTierDetail = null;
        if(!Util.isNull(proposeCreditInfoTierDetailView)){
            proposeCreditInfoTierDetail = new ProposeCreditInfoTierDetail();
            if (!Util.isZero(proposeCreditInfoTierDetailView.getId())) {
                proposeCreditInfoTierDetail = proposeCreditInfoTierDetailDAO.findById(proposeCreditInfoTierDetailView.getId());
            } else {
                proposeCreditInfoTierDetail.setCreateDate(new Date());
                proposeCreditInfoTierDetail.setCreateBy(user);
            }
            proposeCreditInfoTierDetail.setModifyDate(new Date());
            proposeCreditInfoTierDetail.setModifyBy(user);

            proposeCreditInfoTierDetail.setProposeCreditInfo(proposeCreditInfo);

            proposeCreditInfoTierDetail.setFinalBasePrice(baseRateTransform.transformToModel(proposeCreditInfoTierDetailView.getFinalBasePrice()));
            proposeCreditInfoTierDetail.setFinalInterest(proposeCreditInfoTierDetailView.getFinalInterest());
            proposeCreditInfoTierDetail.setNo(proposeCreditInfoTierDetailView.getNo());
            proposeCreditInfoTierDetail.setInstallment(proposeCreditInfoTierDetailView.getInstallment());
            proposeCreditInfoTierDetail.setTenor(proposeCreditInfoTierDetailView.getTenor());
            proposeCreditInfoTierDetail.setBrmsFlag(proposeCreditInfoTierDetailView.getBrmsFlag());
            proposeCreditInfoTierDetail.setFinalInterestOriginal(proposeCreditInfoTierDetailView.getFinalInterestOriginal());
            proposeCreditInfoTierDetail.setInstallmentOriginal(proposeCreditInfoTierDetailView.getInstallmentOriginal());
        }

        return proposeCreditInfoTierDetail;
    }

    public ProposeCreditInfoTierDetailView transformProposeCreditTierToView(ProposeCreditInfoTierDetail proposeCreditInfoTierDetail, ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView = new ProposeCreditInfoTierDetailView();
        if(!Util.isNull(proposeCreditInfoTierDetail) && !Util.isZero(proposeCreditInfoTierDetail.getId())){
            proposeCreditInfoTierDetailView.setId(proposeCreditInfoTierDetail.getId());

            proposeCreditInfoTierDetailView.setFinalBasePrice(baseRateTransform.transformToView(proposeCreditInfoTierDetail.getFinalBasePrice()));
            proposeCreditInfoTierDetailView.setFinalInterest(proposeCreditInfoTierDetail.getFinalInterest());
            proposeCreditInfoTierDetailView.setFinalInterestOriginal(proposeCreditInfoTierDetail.getFinalInterestOriginal());
            //////////////////////////////////////////////// for show on screen ///////////////////////////////////////////////////
            proposeCreditInfoTierDetailView.setStandardPriceLabel("-"); // for default
            proposeCreditInfoTierDetailView.setSuggestPriceLabel("-"); // for default
            if (!Util.isNull(proposeCreditInfoDetailView)){
                if(proposeCreditInfoDetailView.getRequestType() == 2) { // New
                    if(proposeCreditInfoTierDetail.getBrmsFlag() == 1){
                        if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getStandardInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                            proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                        } else {
                            proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                        }
                        if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getSuggestInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                            proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                        } else {
                            proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                        }

                        proposeCreditInfoTierDetailView.setStandardInterest(proposeCreditInfoDetailView.getStandardInterest());
                        proposeCreditInfoTierDetailView.setStandardBasePrice(proposeCreditInfoDetailView.getStandardBaseRate());

                        proposeCreditInfoTierDetailView.setSuggestInterest(proposeCreditInfoDetailView.getSuggestInterest());
                        proposeCreditInfoTierDetailView.setSuggestBasePrice(proposeCreditInfoDetailView.getSuggestBaseRate());
                    }
                }
            }

            if (ValidationUtil.isValueCompareToZero(proposeCreditInfoTierDetailView.getFinalInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " " + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
            } else {
                proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " +" + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            proposeCreditInfoTierDetailView.setNo(proposeCreditInfoTierDetail.getNo());
            proposeCreditInfoTierDetailView.setInstallment(proposeCreditInfoTierDetail.getInstallment());
            proposeCreditInfoTierDetailView.setTenor(proposeCreditInfoTierDetail.getTenor());
            proposeCreditInfoTierDetailView.setBrmsFlag(proposeCreditInfoTierDetail.getBrmsFlag());
            proposeCreditInfoTierDetailView.setFinalInterestOriginal(proposeCreditInfoTierDetail.getFinalInterestOriginal());
            proposeCreditInfoTierDetailView.setInstallmentOriginal(proposeCreditInfoTierDetail.getInstallmentOriginal());
        }
        return proposeCreditInfoTierDetailView;
    }

    public List<ProposeCreditInfoTierDetail> transformProposeCreditTierToModelList(ProposeCreditInfo proposeCreditInfo, List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList, User user) {
        List<ProposeCreditInfoTierDetail> proposeCreditInfoTierDetailList = new ArrayList<ProposeCreditInfoTierDetail>();
        if (!Util.isNull(proposeCreditInfoTierDetailViewList)) {
            for (ProposeCreditInfoTierDetailView proCreditTierView : proposeCreditInfoTierDetailViewList) {
                ProposeCreditInfoTierDetail proposeCreditInfoTierDetail = transformProposeCreditTierToModel(proposeCreditInfo, proCreditTierView, user);
                proposeCreditInfoTierDetailList.add(proposeCreditInfoTierDetail);
            }
        }
        return proposeCreditInfoTierDetailList;
    }

    public List<ProposeCreditInfoTierDetailView> transformProposeCreditTierToViewList(List<ProposeCreditInfoTierDetail> proposeCreditInfoTierDetailList, ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList = new ArrayList<ProposeCreditInfoTierDetailView>();
        if (!Util.isNull(proposeCreditInfoTierDetailList)) {
            for (ProposeCreditInfoTierDetail proCreditTier : proposeCreditInfoTierDetailList) {
                ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView = transformProposeCreditTierToView(proCreditTier, proposeCreditInfoDetailView);
                proposeCreditInfoTierDetailViewList.add(proposeCreditInfoTierDetailView);
            }
        }
        return proposeCreditInfoTierDetailViewList;
    }

    public List<ProposeCreditInfoTierDetailView> orderTierDetail(List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList, ProposeCreditInfoDetailView proposeCreditInfoDetailView){
        if(!Util.isNull(proposeCreditInfoDetailView)) {
            if(!Util.isNull(proposeCreditInfoTierDetailViewList) && !Util.isZero(proposeCreditInfoTierDetailViewList.size())) {
                Comparator<ProposeCreditInfoTierDetailView> comparator = new Comparator<ProposeCreditInfoTierDetailView>() {
                    @Override
                    public int compare(ProposeCreditInfoTierDetailView o1, ProposeCreditInfoTierDetailView o2) {
                        return (o1.getNo() > o2.getNo() ? -1 : (o1.getNo() == o2.getNo() ? 0 : 1));
                    }
                };
                Collections.sort(proposeCreditInfoTierDetailViewList, comparator);
            }

            if(proposeCreditInfoDetailView.getRequestType() == 1) { // 1 = change , 2 = new || Change Order 1->x, New Order x->1
                Collections.reverse(proposeCreditInfoTierDetailViewList);
            }
        }
        return proposeCreditInfoTierDetailViewList;
    }

    public ProposeCreditInfoDetailView transformPricingIntTierToView(List<PricingIntTier> pricingInterestList, ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        List<ProposeCreditInfoTierDetailView> proposeCreditInfoTierDetailViewList = new ArrayList<ProposeCreditInfoTierDetailView>();
        ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView;

        //for remove old list
        for(ProposeCreditInfoTierDetailView proCreInfTieDetView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
            if(!Util.isZero(proCreInfTieDetView.getId())) {
                proposeCreditInfoDetailView.getDeleteCreditTierIdList().add(proCreInfTieDetView.getId());
            }
        }

        for (PricingIntTier newPricingIntTier : pricingInterestList) {
            StringBuilder standRate = new StringBuilder();
            StringBuilder suggestRate = new StringBuilder();
            StringBuilder finalRate = new StringBuilder();
            proposeCreditInfoTierDetailView = new ProposeCreditInfoTierDetailView();

            BigDecimal interest = BigDecimal.ZERO;
            boolean isNegate = false;
            String baseRate;
            if(!Util.isNull(newPricingIntTier.getSpread())) {
                if(newPricingIntTier.getSpread().equalsIgnoreCase("-")){
                    isNegate = true;
                }
            }

            if(!Util.isNull(newPricingIntTier.getRateVariance())) {
                if(isNegate){
                    interest = newPricingIntTier.getRateVariance().negate();
                } else {
                    interest = newPricingIntTier.getRateVariance();
                }
            }

            if(!Util.isNull(newPricingIntTier.getRateType()) && !Util.isEmpty(newPricingIntTier.getRateType())) {
                if(!newPricingIntTier.getRateType().equalsIgnoreCase("null")) {
                    baseRate = newPricingIntTier.getRateType();
                } else {
                    newPricingIntTier.setRateType("As Manual");
                    baseRate = newPricingIntTier.getRateType();
                }
            } else {
                newPricingIntTier.setRateType("As Manual");
                baseRate = newPricingIntTier.getRateType();
            }

            //Standard
            standRate.append(baseRate).append(" ").append(isNegate ? "-" : "+").append(Util.formatNumberPricing(newPricingIntTier.getRateVariance()));
            proposeCreditInfoTierDetailView.setStandardPriceLabel(standRate.toString());
            proposeCreditInfoTierDetailView.setStandardBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            proposeCreditInfoTierDetailView.setStandardInterest(interest);
            proposeCreditInfoDetailView.setStandardBaseRate(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            proposeCreditInfoDetailView.setStandardInterest(interest);

            //Suggestion
            suggestRate.append(baseRate).append(" ").append(isNegate ? "-" : "+").append(Util.formatNumberPricing(newPricingIntTier.getRateVariance()));
            proposeCreditInfoTierDetailView.setSuggestPriceLabel(suggestRate.toString());
            proposeCreditInfoTierDetailView.setSuggestBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            proposeCreditInfoTierDetailView.setSuggestInterest(interest);
            proposeCreditInfoDetailView.setSuggestBaseRate(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            proposeCreditInfoDetailView.setSuggestInterest(interest);

            //Final
            finalRate.append(baseRate).append(" ").append(isNegate ? "-" : "+").append(Util.formatNumberPricing(newPricingIntTier.getRateVariance()));
            proposeCreditInfoTierDetailView.setFinalPriceLabel(finalRate.toString());
            proposeCreditInfoTierDetailView.setFinalBasePrice(baseRateTransform.transformToView(baseRateDAO.findByName(newPricingIntTier.getRateType())));
            proposeCreditInfoTierDetailView.setFinalInterest(interest);

            proposeCreditInfoTierDetailView.setNo(1);
            proposeCreditInfoTierDetailView.setBrmsFlag(1);

            proposeCreditInfoTierDetailViewList.add(proposeCreditInfoTierDetailView);
        }

        proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(proposeCreditInfoTierDetailViewList);

        return proposeCreditInfoDetailView;
    }

    //-------------------------------------------------------- Propose Condition Info --------------------------------------------------------//

    public ProposeConditionInfo transformProposeConditionToModel(ProposeLine proposeLine, ProposeConditionInfoView proposeConditionInfoView, User user, ProposeType proposeType) {
        ProposeConditionInfo proposeConditionInfo = null;
        if(!Util.isNull(proposeConditionInfoView)){
            proposeConditionInfo = new ProposeConditionInfo();
            if (!Util.isZero(proposeConditionInfoView.getId())) {
                proposeConditionInfo = proposeConditionDAO.findById(proposeConditionInfoView.getId());
            } else {
                proposeConditionInfo.setCreateDate(new Date());
                proposeConditionInfo.setCreateBy(user);
            }
            proposeConditionInfo.setModifyDate(new Date());
            proposeConditionInfo.setModifyBy(user);

            proposeConditionInfo.setProposeType(proposeType);
            proposeConditionInfo.setProposeLine(proposeLine);

            proposeConditionInfo.setLoanType(proposeConditionInfoView.getLoanType());
            proposeConditionInfo.setConditionDesc(proposeConditionInfoView.getConditionDesc());
        }

        return proposeConditionInfo;
    }

    public ProposeConditionInfoView transformProposeConditionToView(ProposeConditionInfo proposeConditionInfo, ProposeType proposeType) {
        ProposeConditionInfoView proposeConditionInfoView = null;
        if(!Util.isNull(proposeConditionInfo) && !Util.isZero(proposeConditionInfo.getId()) && proposeConditionInfo.getProposeType() == proposeType){
            proposeConditionInfoView = new ProposeConditionInfoView();

            proposeConditionInfoView.setId(proposeConditionInfo.getId());

            proposeConditionInfoView.setLoanType(proposeConditionInfo.getLoanType());
            proposeConditionInfoView.setConditionDesc(proposeConditionInfo.getConditionDesc());
        }

        return proposeConditionInfoView;
    }

    public List<ProposeConditionInfo> transformProposeConditionToModelList(ProposeLine proposeLine, List<ProposeConditionInfoView> proposeConditionInfoViewList, User user, ProposeType proposeType) {
        List<ProposeConditionInfo> proposeConditionInfoList = new ArrayList<ProposeConditionInfo>();
        if (!Util.isNull(proposeConditionInfoViewList)) {
            for (ProposeConditionInfoView proConditionView : proposeConditionInfoViewList) {
                ProposeConditionInfo proposeConditionInfoView = transformProposeConditionToModel(proposeLine, proConditionView, user, proposeType);
                proposeConditionInfoList.add(proposeConditionInfoView);
            }
        }
        return proposeConditionInfoList;
    }

    public List<ProposeConditionInfoView> transformProposeConditionToViewList(List<ProposeConditionInfo> proposeConditionInfoList, ProposeType proposeType) {
        List<ProposeConditionInfoView> proposeConditionInfoViewList = new ArrayList<ProposeConditionInfoView>();
        if (!Util.isNull(proposeConditionInfoList)) {
            for (ProposeConditionInfo proCondition : proposeConditionInfoList) {
                ProposeConditionInfoView proposeConditionInfoView = transformProposeConditionToView(proCondition, proposeType);
                if(!Util.isNull(proposeConditionInfoView)) {
                    proposeConditionInfoViewList.add(proposeConditionInfoView);
                }
            }
        }
        return proposeConditionInfoViewList;
    }

    //-------------------------------------------------------- Propose Guarantor Info --------------------------------------------------------//

    public ProposeGuarantorInfo transformProposeGuarantorToModel(ProposeLine proposeLine, ProposeGuarantorInfoView proposeGuarantorInfoView, User user, ProposeType proposeType) {
        ProposeGuarantorInfo proposeGuarantorInfo = null;
        if(!Util.isNull(proposeGuarantorInfoView)){
            proposeGuarantorInfo = new ProposeGuarantorInfo();
            if (!Util.isZero(proposeGuarantorInfoView.getId())) {
                proposeGuarantorInfo = proposeGuarantorInfoDAO.findById(proposeGuarantorInfoView.getId());
            } else {
                proposeGuarantorInfo.setCreateDate(new Date());
                proposeGuarantorInfo.setCreateBy(user);
            }
            proposeGuarantorInfo.setModifyDate(new Date());
            proposeGuarantorInfo.setModifyBy(user);

            proposeGuarantorInfo.setProposeType(proposeType);
            proposeGuarantorInfo.setProposeLine(proposeLine);

            if(!Util.isNull(proposeGuarantorInfoView.getGuarantorName()) && proposeGuarantorInfoView.getGuarantorName().getId() != -1){
                proposeGuarantorInfo.setGuarantorName(customerTransform.transformSelectListToModel(proposeGuarantorInfoView.getGuarantorName()));
            } else {
                proposeGuarantorInfo.setGuarantorName(null);
            }
            proposeGuarantorInfo.setGuarantorCategory(proposeGuarantorInfoView.getGuarantorCategory());
            proposeGuarantorInfo.setTcgLgNo(proposeGuarantorInfoView.getTcgLgNo());
            proposeGuarantorInfo.setTotalLimitGuaranteeAmount(proposeGuarantorInfoView.getGuaranteeAmount());

            proposeGuarantorInfo.setUwDecision(proposeGuarantorInfoView.getUwDecision());
        }

        return proposeGuarantorInfo;
    }

    public ProposeGuarantorInfoView transformProposeGuarantorToView(ProposeGuarantorInfo proposeGuarantorInfo, ProposeType proposeType) {
        ProposeGuarantorInfoView proposeGuarantorInfoView = null;
        if(!Util.isNull(proposeGuarantorInfo) && !Util.isZero(proposeGuarantorInfo.getId()) && proposeGuarantorInfo.getProposeType() == proposeType){
            proposeGuarantorInfoView = new ProposeGuarantorInfoView();

            proposeGuarantorInfoView.setId(proposeGuarantorInfo.getId());

            proposeGuarantorInfoView.setTcgLgNo(proposeGuarantorInfo.getTcgLgNo());
            if(!Util.isNull(proposeGuarantorInfo.getGuarantorName())) {
                proposeGuarantorInfoView.setGuarantorName(customerTransform.transformToView(proposeGuarantorInfo.getGuarantorName()));
            } else {
                CustomerInfoView customerInfoView = new CustomerInfoView();
                customerInfoView.setId(-1);
                customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                proposeGuarantorInfoView.setGuarantorName(customerInfoView);
            }
            proposeGuarantorInfoView.setUwDecision(proposeGuarantorInfo.getUwDecision());
            proposeGuarantorInfoView.setGuarantorCategory(proposeGuarantorInfo.getGuarantorCategory());
            proposeGuarantorInfoView.setGuaranteeAmount(proposeGuarantorInfo.getTotalLimitGuaranteeAmount());

            List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList = proposeGuarantorInfoRelationDAO.findByGuarantorId(proposeGuarantorInfo.getId(), proposeType);

            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();

            for (ProposeGuarantorInfoRelation proposeGuarantorInfoRelation : proposeGuarantorInfoRelationList) {
                if(!Util.isNull(proposeGuarantorInfoRelation)){
                    if(!Util.isNull(proposeGuarantorInfoRelation.getProposeCreditInfo())) {
                        ProposeCreditInfoDetailView proposeCreditInfoDetailView = transformProposeCreditToViewScreen(proposeGuarantorInfoRelation.getProposeCreditInfo(), proposeGuarantorInfoRelation.getGuaranteeAmount());
                        proposeCreditInfoDetailViewList.add(proposeCreditInfoDetailView);
                    } else if(!Util.isNull(proposeGuarantorInfoRelation.getExistingCreditDetail())) {
                        ProposeCreditInfoDetailView existingCreditDetailView = transformProposeCreditToViewByExisting(proposeGuarantorInfoRelation.getExistingCreditDetail(), proposeGuarantorInfoRelation.getGuaranteeAmount());
                        proposeCreditInfoDetailViewList.add(existingCreditDetailView);
                    }
                }
            }

            proposeGuarantorInfoView.setProposeCreditInfoDetailViewList(proposeCreditInfoDetailViewList);
        }

        return proposeGuarantorInfoView;
    }

    public List<ProposeGuarantorInfoView> transformProposeGuarantorToViewList(List<ProposeGuarantorInfo> proposeGuarantorInfoList, ProposeType proposeType) {
        List<ProposeGuarantorInfoView> proposeGuarantorViewList = new ArrayList<ProposeGuarantorInfoView>();
        if (!Util.isNull(proposeGuarantorInfoList)) {
            for (ProposeGuarantorInfo proGuarantor : proposeGuarantorInfoList) {
                ProposeGuarantorInfoView proposeGuarantorInfoView = transformProposeGuarantorToView(proGuarantor, proposeType);
                if(!Util.isNull(proposeGuarantorInfoView)) {
                    proposeGuarantorViewList.add(proposeGuarantorInfoView);
                }
            }
        }
        return proposeGuarantorViewList;
    }

    //-------------------------------------------------------- Propose Guarantor Info Relation --------------------------------------------------------//
    // Remove All Guarantor Relate and this transform for Add New Only -- Relate Id always 0 !
    public ProposeGuarantorInfoRelation transformProposeGuarantorRelationToModel(ProposeLine proposeLine, ProposeGuarantorInfo proposeGuarantorInfo, ProposeCreditInfo proposeCreditInfo, ExistingCreditDetail existingCreditDetail, ProposeType proposeType, User user, BigDecimal guaranteeAmount) {
        ProposeGuarantorInfoRelation proposeGuarantorInfoRelation = new ProposeGuarantorInfoRelation();

        proposeGuarantorInfoRelation.setProposeGuarantorInfo(proposeGuarantorInfo);
        proposeGuarantorInfoRelation.setProposeCreditInfo(proposeCreditInfo);
        proposeGuarantorInfoRelation.setExistingCreditDetail(existingCreditDetail);
        proposeGuarantorInfoRelation.setGuaranteeAmount(guaranteeAmount);
        proposeGuarantorInfoRelation.setProposeType(proposeType);
        proposeGuarantorInfoRelation.setProposeLine(proposeLine);
        proposeGuarantorInfoRelation.setCreateDate(new Date());
        proposeGuarantorInfoRelation.setCreateBy(user);
        proposeGuarantorInfoRelation.setModifyDate(new Date());
        proposeGuarantorInfoRelation.setModifyBy(user);

        return proposeGuarantorInfoRelation;
    }

    // Remove All Collateral Relate and this transform for Add New Only -- Relate Id always 0 !
    public ProposeCollateralInfoRelation transformProposeCollateralRelationToModel(ProposeLine proposeLine, ProposeCollateralInfo proposeCollateralInfo, ProposeCreditInfo proposeCreditInfo, ExistingCreditDetail existingCreditDetail, ProposeType proposeType, User user) {
        ProposeCollateralInfoRelation proposeCollateralInfoRelation = new ProposeCollateralInfoRelation();

        proposeCollateralInfoRelation.setProposeCollateral(proposeCollateralInfo);
        proposeCollateralInfoRelation.setProposeCreditInfo(proposeCreditInfo);
        proposeCollateralInfoRelation.setExistingCreditDetail(existingCreditDetail);
        proposeCollateralInfoRelation.setProposeType(proposeType);
        proposeCollateralInfoRelation.setProposeLine(proposeLine);
        proposeCollateralInfoRelation.setCreateDate(new Date());
        proposeCollateralInfoRelation.setCreateBy(user);
        proposeCollateralInfoRelation.setModifyDate(new Date());
        proposeCollateralInfoRelation.setModifyBy(user);

        return proposeCollateralInfoRelation;
    }

    //-------------------------------------------------------- Propose Fee Info Detail --------------------------------------------------------//
    public List<ProposeFeeDetail> transformProposeFeeToModel(ProposeLine proposeLine, ProposeFeeDetailView proposeFeeDetailView, ProposeType proposeType) { // add new only
        List<ProposeFeeDetail> proposeFeeDetailList = new ArrayList<ProposeFeeDetail>();
        ProposeCreditInfo proposeCreditInfo = new ProposeCreditInfo();
        proposeCreditInfo.setId(proposeFeeDetailView.getProposeCreditInfoDetailView().getId());
        WorkCase workCase = proposeLine.getWorkCase();

        ProposeFeeDetail proposeFeeDetail;

        if(!Util.isNull(proposeFeeDetailView.getStandardFrontEndFee())){ // StandardFrontEndFee
            proposeFeeDetail = new ProposeFeeDetail();

            FeeType feeType = feeTypeDAO.findByBRMSCode(proposeFeeDetailView.getStandardFrontEndFee().getFeeTypeView().getBrmsCode());
            FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(proposeFeeDetailView.getStandardFrontEndFee().getFeePaymentMethodView().getBrmsCode());

            proposeFeeDetail.setPercentFee(proposeFeeDetailView.getStandardFrontEndFee().getPercentFee());
            proposeFeeDetail.setPercentFeeAfter(proposeFeeDetailView.getStandardFrontEndFee().getPercentFeeAfter());
            proposeFeeDetail.setFeeYear(proposeFeeDetailView.getStandardFrontEndFee().getFeeYear());
            proposeFeeDetail.setAmount(proposeFeeDetailView.getStandardFrontEndFee().getFeeAmount());
            proposeFeeDetail.setFeeLevel(proposeFeeDetailView.getStandardFrontEndFee().getFeeLevel());
            proposeFeeDetail.setDescription(proposeFeeDetailView.getStandardFrontEndFee().getDescription());
            proposeFeeDetail.setProposeCreditInfo(proposeCreditInfo);
            proposeFeeDetail.setProposeType(proposeType);
            proposeFeeDetail.setFeeType(feeType);
            proposeFeeDetail.setPaymentMethod(feePaymentMethod);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeFeeDetail.setWorkCase(workCase);
                } else {
                    proposeFeeDetail.setWorkCase(null);
                }
            } else {
                proposeFeeDetail.setWorkCase(workCase);
            }

            proposeFeeDetailList.add(proposeFeeDetail);
        }

        if(!Util.isNull(proposeFeeDetailView.getCommitmentFee())){ // CommitmentFee
            proposeFeeDetail = new ProposeFeeDetail();

            FeeType feeType = feeTypeDAO.findByBRMSCode(proposeFeeDetailView.getCommitmentFee().getFeeTypeView().getBrmsCode());
            FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(proposeFeeDetailView.getCommitmentFee().getFeePaymentMethodView().getBrmsCode());

            proposeFeeDetail.setPercentFee(proposeFeeDetailView.getCommitmentFee().getPercentFee());
            proposeFeeDetail.setPercentFeeAfter(proposeFeeDetailView.getCommitmentFee().getPercentFeeAfter());
            proposeFeeDetail.setFeeYear(proposeFeeDetailView.getCommitmentFee().getFeeYear());
            proposeFeeDetail.setAmount(proposeFeeDetailView.getCommitmentFee().getFeeAmount());
            proposeFeeDetail.setFeeLevel(proposeFeeDetailView.getCommitmentFee().getFeeLevel());
            proposeFeeDetail.setDescription(proposeFeeDetailView.getCommitmentFee().getDescription());
            proposeFeeDetail.setProposeCreditInfo(proposeCreditInfo);
            proposeFeeDetail.setProposeType(proposeType);
            proposeFeeDetail.setFeeType(feeType);
            proposeFeeDetail.setPaymentMethod(feePaymentMethod);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeFeeDetail.setWorkCase(workCase);
                } else {
                    proposeFeeDetail.setWorkCase(null);
                }
            } else {
                proposeFeeDetail.setWorkCase(workCase);
            }

            proposeFeeDetailList.add(proposeFeeDetail);
        }

        if(!Util.isNull(proposeFeeDetailView.getCancellationFee())){ // CancellationFee
            proposeFeeDetail = new ProposeFeeDetail();

            FeeType feeType = feeTypeDAO.findByBRMSCode(proposeFeeDetailView.getCancellationFee().getFeeTypeView().getBrmsCode());
            FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(proposeFeeDetailView.getCancellationFee().getFeePaymentMethodView().getBrmsCode());

            proposeFeeDetail.setPercentFee(proposeFeeDetailView.getCancellationFee().getPercentFee());
            proposeFeeDetail.setPercentFeeAfter(proposeFeeDetailView.getCancellationFee().getPercentFeeAfter());
            proposeFeeDetail.setFeeYear(proposeFeeDetailView.getCancellationFee().getFeeYear());
            proposeFeeDetail.setAmount(proposeFeeDetailView.getCancellationFee().getFeeAmount());
            proposeFeeDetail.setFeeLevel(proposeFeeDetailView.getCancellationFee().getFeeLevel());
            proposeFeeDetail.setDescription(proposeFeeDetailView.getCancellationFee().getDescription());
            proposeFeeDetail.setProposeCreditInfo(proposeCreditInfo);
            proposeFeeDetail.setProposeType(proposeType);
            proposeFeeDetail.setFeeType(feeType);
            proposeFeeDetail.setPaymentMethod(feePaymentMethod);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeFeeDetail.setWorkCase(workCase);
                } else {
                    proposeFeeDetail.setWorkCase(null);
                }
            } else {
                proposeFeeDetail.setWorkCase(workCase);
            }

            proposeFeeDetailList.add(proposeFeeDetail);
        }

        if(!Util.isNull(proposeFeeDetailView.getExtensionFee())){ // ExtensionFee
            proposeFeeDetail = new ProposeFeeDetail();

            FeeType feeType = feeTypeDAO.findByBRMSCode(proposeFeeDetailView.getExtensionFee().getFeeTypeView().getBrmsCode());
            FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(proposeFeeDetailView.getExtensionFee().getFeePaymentMethodView().getBrmsCode());

            proposeFeeDetail.setPercentFee(proposeFeeDetailView.getExtensionFee().getPercentFee());
            proposeFeeDetail.setPercentFeeAfter(proposeFeeDetailView.getExtensionFee().getPercentFeeAfter());
            proposeFeeDetail.setFeeYear(proposeFeeDetailView.getExtensionFee().getFeeYear());
            proposeFeeDetail.setAmount(proposeFeeDetailView.getExtensionFee().getFeeAmount());
            proposeFeeDetail.setFeeLevel(proposeFeeDetailView.getExtensionFee().getFeeLevel());
            proposeFeeDetail.setDescription(proposeFeeDetailView.getExtensionFee().getDescription());
            proposeFeeDetail.setProposeCreditInfo(proposeCreditInfo);
            proposeFeeDetail.setProposeType(proposeType);
            proposeFeeDetail.setFeeType(feeType);
            proposeFeeDetail.setPaymentMethod(feePaymentMethod);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeFeeDetail.setWorkCase(workCase);
                } else {
                    proposeFeeDetail.setWorkCase(null);
                }
            } else {
                proposeFeeDetail.setWorkCase(workCase);
            }

            proposeFeeDetailList.add(proposeFeeDetail);
        }

        if(!Util.isNull(proposeFeeDetailView.getPrepaymentFee())){ // PrepaymentFee
            proposeFeeDetail = new ProposeFeeDetail();

            FeeType feeType = feeTypeDAO.findByBRMSCode(proposeFeeDetailView.getPrepaymentFee().getFeeTypeView().getBrmsCode());
            FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(proposeFeeDetailView.getPrepaymentFee().getFeePaymentMethodView().getBrmsCode());

            proposeFeeDetail.setPercentFee(proposeFeeDetailView.getPrepaymentFee().getPercentFee());
            proposeFeeDetail.setPercentFeeAfter(proposeFeeDetailView.getPrepaymentFee().getPercentFeeAfter());
            proposeFeeDetail.setFeeYear(proposeFeeDetailView.getPrepaymentFee().getFeeYear());
            proposeFeeDetail.setAmount(proposeFeeDetailView.getPrepaymentFee().getFeeAmount());
            proposeFeeDetail.setFeeLevel(proposeFeeDetailView.getPrepaymentFee().getFeeLevel());
            proposeFeeDetail.setDescription(proposeFeeDetailView.getPrepaymentFee().getDescription());
            proposeFeeDetail.setProposeCreditInfo(proposeCreditInfo);
            proposeFeeDetail.setProposeType(proposeType);
            proposeFeeDetail.setFeeType(feeType);
            proposeFeeDetail.setPaymentMethod(feePaymentMethod);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeFeeDetail.setWorkCase(workCase);
                } else {
                    proposeFeeDetail.setWorkCase(null);
                }
            } else {
                proposeFeeDetail.setWorkCase(workCase);
            }

            proposeFeeDetailList.add(proposeFeeDetail);
        }

        return proposeFeeDetailList;
    }

    public List<ProposeFeeDetail> transformProposeFeeToModelList(ProposeLine proposeLine, List<ProposeFeeDetailView> proposeFeeDetailViewList, ProposeType proposeType) {
        List<ProposeFeeDetail> proposeFeeDetailList = new ArrayList<ProposeFeeDetail>();
        if (!Util.isNull(proposeFeeDetailViewList)) {
            for (ProposeFeeDetailView proFeeDetView : proposeFeeDetailViewList) {
                List<ProposeFeeDetail> proposeFeeDetailListReturn = transformProposeFeeToModel(proposeLine, proFeeDetView, proposeType);
                if(!Util.isNull(proposeFeeDetailListReturn) && !Util.isZero(proposeFeeDetailListReturn.size())) {
                    for(ProposeFeeDetail proposeFeeDetail : proposeFeeDetailListReturn) {
                        proposeFeeDetailList.add(proposeFeeDetail);
                    }
                }
            }
        }
        return proposeFeeDetailList;
    }

    public List<ProposeFeeDetailView> transformProposeFeeToViewList(List<ProposeFeeDetail> proposeFeeDetailList, ProposeType proposeType) {
        List<ProposeFeeDetailView> proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();
        if (!Util.isNull(proposeFeeDetailList)) {
            Map<Long, ProposeFeeDetailView> newFeeDetailViewMap = new HashMap<Long, ProposeFeeDetailView>();
            ProposeFeeDetailView proposeFeeDetailView;
            for(ProposeFeeDetail proposeFeeDetail : proposeFeeDetailList){
                FeeDetailView feeDetailView = transformFeeDetailToView(proposeFeeDetail);
                if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                    if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                        proposeFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                    } else {
                        proposeFeeDetailView = new ProposeFeeDetailView();
                        newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), proposeFeeDetailView);
                    }

                    ProposeCreditInfo proposeCreditInfo = proposeFeeDetail.getProposeCreditInfo();
                    if (!Util.isNull(proposeCreditInfo)) {
                        proposeFeeDetailView.setProposeCreditInfoDetailView(transformProposeCreditToView(proposeCreditInfo, proposeType));
                        if (!Util.isNull(proposeCreditInfo.getProductProgram())) {
                            proposeFeeDetailView.setProductProgram(proposeCreditInfo.getProductProgram().getName());
                        }
                        if ("9".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=9,(Front-End-Fee)
                            proposeFeeDetailView.setStandardFrontEndFee(feeDetailView);
                        } else if ("15".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=15,(Prepayment Fee)
                            proposeFeeDetailView.setPrepaymentFee(feeDetailView);
                        } else if ("20".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=20,(CancellationFee)
                            proposeFeeDetailView.setCancellationFee(feeDetailView);
                        } else if ("21".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=21,(ExtensionFee)
                            proposeFeeDetailView.setExtensionFee(feeDetailView);
                        } else if ("22".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=22,(CommitmentFee)
                            proposeFeeDetailView.setCommitmentFee(feeDetailView);
                        }
                    }
                }
            }

            if (newFeeDetailViewMap.size() > 0) {
                Iterator it = newFeeDetailViewMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    proposeFeeDetailViewList.add((ProposeFeeDetailView) pairs.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        }
        return proposeFeeDetailViewList;
    }

    public FeeDetailView transformFeeDetailToView(ProposeFeeDetail proposeFeeDetail){
        FeeDetailView feeDetailView = new FeeDetailView();
        feeDetailView.setId(proposeFeeDetail.getId());
        feeDetailView.setFeeYear(proposeFeeDetail.getFeeYear());
        feeDetailView.setPercentFee(proposeFeeDetail.getPercentFee());
        feeDetailView.setCreditDetailViewId(proposeFeeDetail.getProposeCreditInfo().getId());
        feeDetailView.setDescription(proposeFeeDetail.getDescription());
        feeDetailView.setFeeLevel(proposeFeeDetail.getFeeLevel());
        feeDetailView.setFeePaymentMethodView(feeTransform.getFeePaymentMethodView(proposeFeeDetail.getPaymentMethod().getBrmsCode()));
        feeDetailView.setFeeTypeView(feeTransform.getFeeTypeView(proposeFeeDetail.getFeeType().getBrmsCode()));
        feeDetailView.setPercentFeeAfter(proposeFeeDetail.getPercentFeeAfter());
        feeDetailView.setFeeAmount(proposeFeeDetail.getAmount());

        return feeDetailView;
    }

    //-------------------------------------------------------- Propose Collateral Info --------------------------------------------------------//
    public ProposeCollateralInfo transformProposeCollateralToModel(WorkCase workCase, ProposeLine proposeLine, ProposeCollateralInfoView proposeCollateralInfoView, User user, ProposeType proposeType) {
        ProposeCollateralInfo proposeCollateralInfo = null;
        if(!Util.isNull(proposeCollateralInfoView)){
            proposeCollateralInfo = new ProposeCollateralInfo();
            log.debug("## proposeCollateralInfoView.getId() [{}] ##",proposeCollateralInfoView.getId());
            if (!Util.isZero(proposeCollateralInfoView.getId())) {
                proposeCollateralInfo = proposeCollateralInfoDAO.findById(proposeCollateralInfoView.getId());
            } else {
                proposeCollateralInfo.setCreateDate(new Date());
                proposeCollateralInfo.setCreateBy(user);
            }
            proposeCollateralInfo.setModifyDate(new Date());
            proposeCollateralInfo.setModifyBy(user);

            proposeCollateralInfo.setProposeType(proposeType);
            proposeCollateralInfo.setProposeLine(proposeLine);
            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeCollateralInfo.setWorkCase(workCase);
                } else {
                    proposeCollateralInfo.setWorkCase(null);
                }
            } else {
                proposeCollateralInfo.setWorkCase(null);
            }

            proposeCollateralInfo.setAppraisalRequest(proposeCollateralInfoView.getAppraisalRequest());
            proposeCollateralInfo.setComs(Util.isTrue(proposeCollateralInfoView.isComs()));
            proposeCollateralInfo.setJobID(proposeCollateralInfoView.getJobID());
            proposeCollateralInfo.setAppraisalDate(proposeCollateralInfoView.getAppraisalDate());
            proposeCollateralInfo.setNumberMonthsFromApprDate(proposeCollateralInfoView.getNumberMonthsFromApprDate());
            proposeCollateralInfo.setAadDecision(proposeCollateralInfoView.getAadDecision());
            proposeCollateralInfo.setAadDecisionReason(proposeCollateralInfoView.getAadDecisionReason());
            proposeCollateralInfo.setAadDecisionReasonDetail(proposeCollateralInfoView.getAadDecisionReasonDetail());
            proposeCollateralInfo.setUsage(proposeCollateralInfoView.getUsage());
            proposeCollateralInfo.setTypeOfUsage(proposeCollateralInfoView.getTypeOfUsage());
            proposeCollateralInfo.setUwRemark(proposeCollateralInfoView.getUwRemark());
            proposeCollateralInfo.setMortgageCondition(proposeCollateralInfoView.getMortgageCondition());
            proposeCollateralInfo.setMortgageConditionDetail(proposeCollateralInfoView.getMortgageConditionDetail());
            proposeCollateralInfo.setBdmComments(proposeCollateralInfoView.getBdmComments());
            proposeCollateralInfo.setUwDecision(proposeCollateralInfoView.getUwDecision());

            final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = transformProposeCollateralHeadToModelList(workCase, proposeCollateralInfo, proposeCollateralInfoView.getProposeCollateralInfoHeadViewList(), user, proposeType);
            if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                proposeCollateralInfo.setProposeCollateralInfoHeadList(proposeCollateralInfoHeadList);
            } else {
                proposeCollateralInfo.setProposeCollateralInfoHeadList(null);
            }

            final List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = null;
            if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                proposeCollateralInfo.setProposeCollateralInfoRelationList(proposeCollateralInfoRelationList);
            } else {
                proposeCollateralInfo.setProposeCollateralInfoRelationList(null);
            }


        }

        return proposeCollateralInfo;
    }

    public ProposeCollateralInfoHead transformProposeCollateralHeadToModel(WorkCase workCase, ProposeCollateralInfo proposeCollateralInfo, ProposeCollateralInfoHeadView proposeCollateralInfoHeadView, User user, ProposeType proposeType) {
        ProposeCollateralInfoHead proposeCollateralInfoHead = null;
        if(!Util.isNull(proposeCollateralInfoHeadView)){
            proposeCollateralInfoHead = new ProposeCollateralInfoHead();
            if (!Util.isZero(proposeCollateralInfoHeadView.getId())) {
                proposeCollateralInfoHead = proposeCollateralInfoHeadDAO.findById(proposeCollateralInfoHeadView.getId());
            } else {
                proposeCollateralInfoHead.setCreateDate(new Date());
                proposeCollateralInfoHead.setCreateBy(user);
            }

            proposeCollateralInfoHead.setModifyDate(new Date());
            proposeCollateralInfoHead.setModifyBy(user);
            proposeCollateralInfoHead.setProposeType(proposeType);

            if(!Util.isNull(proposeCollateralInfoHeadView.getPotentialCollateral()) && !Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())){
                proposeCollateralInfoHead.setPotentialCollateral(proposeCollateralInfoHeadView.getPotentialCollateral());
            } else {
                proposeCollateralInfoHead.setPotentialCollateral(null);
            }

            if(!Util.isNull(proposeCollateralInfoHeadView.getTcgCollateralType()) && !Util.isZero(proposeCollateralInfoHeadView.getTcgCollateralType().getId())){
                proposeCollateralInfoHead.setCollateralType(proposeCollateralInfoHeadView.getTcgCollateralType());
            } else {
                proposeCollateralInfoHead.setCollateralType(null);
            }

            if(!Util.isNull(proposeCollateralInfoHeadView.getHeadCollType()) && !Util.isZero(proposeCollateralInfoHeadView.getHeadCollType().getId())){
                proposeCollateralInfoHead.setHeadCollType(proposeCollateralInfoHeadView.getHeadCollType());
            } else {
                proposeCollateralInfoHead.setHeadCollType(null);
            }

            proposeCollateralInfoHead.setExistingCredit(proposeCollateralInfoHeadView.getExistingCredit());
            proposeCollateralInfoHead.setTitleDeed(proposeCollateralInfoHeadView.getTitleDeed());
            proposeCollateralInfoHead.setCollateralLocation(proposeCollateralInfoHeadView.getCollateralLocation());
            proposeCollateralInfoHead.setAppraisalValue(proposeCollateralInfoHeadView.getAppraisalValue());
            proposeCollateralInfoHead.setInsuranceCompany(proposeCollateralInfoHeadView.getInsuranceCompany());
            proposeCollateralInfoHead.setProposeCollateral(proposeCollateralInfo);
            proposeCollateralInfoHead.setComs(proposeCollateralInfoHeadView.getComs());

            final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = transformProposeCollateralSubToModelList(workCase, proposeCollateralInfoHead, proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList(), user, proposeType);
            if(Util.isSafetyList(proposeCollateralInfoSubList)){
                proposeCollateralInfoHead.setProposeCollateralInfoSubList(proposeCollateralInfoSubList);
            } else {
                proposeCollateralInfoHead.setProposeCollateralInfoSubList(null);
            }
        }

        return proposeCollateralInfoHead;
    }

    public List<ProposeCollateralInfoHead> transformProposeCollateralHeadToModelList(WorkCase workCase, ProposeCollateralInfo proposeCollateralInfo, List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList, User user, ProposeType proposeType) {
        List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = new ArrayList<ProposeCollateralInfoHead>();
        if (!Util.isNull(proposeCollateralInfoHeadViewList)) {
            for (ProposeCollateralInfoHeadView proColHeadView : proposeCollateralInfoHeadViewList) {
                ProposeCollateralInfoHead proposeColHead = transformProposeCollateralHeadToModel(workCase, proposeCollateralInfo, proColHeadView, user, proposeType);
                proposeCollateralInfoHeadList.add(proposeColHead);
            }
        }
        return proposeCollateralInfoHeadList;
    }

    public ProposeCollateralInfoSub transformProposeCollateralSubToModel(WorkCase workCase, ProposeCollateralInfoHead proposeCollateralInfoHead, ProposeCollateralInfoSubView proposeCollateralInfoSubView, User user, ProposeType proposeType) {
        ProposeCollateralInfoSub proposeCollateralInfoSub = null;
        if(!Util.isNull(proposeCollateralInfoSubView)){
            proposeCollateralInfoSub = new ProposeCollateralInfoSub();
            if (!Util.isZero(proposeCollateralInfoSubView.getId())) {
                proposeCollateralInfoSub = proposeCollateralInfoSubDAO.findById(proposeCollateralInfoSubView.getId());
            } else {
                proposeCollateralInfoSub.setCreateDate(new Date());
                proposeCollateralInfoSub.setCreateBy(user);
            }
            proposeCollateralInfoSub.setModifyDate(new Date());
            proposeCollateralInfoSub.setModifyBy(user);

            if(!Util.isNull(proposeCollateralInfoSubView.getSubCollateralType()) && !Util.isZero(proposeCollateralInfoSubView.getSubCollateralType().getId())){
                proposeCollateralInfoSub.setSubCollateralType(proposeCollateralInfoSubView.getSubCollateralType());
            } else {
                proposeCollateralInfoSub.setSubCollateralType(null);
            }

            proposeCollateralInfoSub.setTitleDeed(proposeCollateralInfoSubView.getTitleDeed());
            proposeCollateralInfoSub.setAddress(proposeCollateralInfoSubView.getAddress());
            proposeCollateralInfoSub.setLandOffice(proposeCollateralInfoSubView.getLandOffice());
            proposeCollateralInfoSub.setCollateralOwnerAAD(proposeCollateralInfoSubView.getCollateralOwnerAAD());
            proposeCollateralInfoSub.setAppraisalValue(proposeCollateralInfoSubView.getAppraisalValue());
            proposeCollateralInfoSub.setMortgageValue(proposeCollateralInfoSubView.getMortgageValue());
            proposeCollateralInfoSub.setSubId(proposeCollateralInfoSubView.getSubId());
            proposeCollateralInfoSub.setComs(proposeCollateralInfoSubView.getComs());
            proposeCollateralInfoSub.setTypeOfUsage(proposeCollateralInfoSubView.getTypeOfUsage());

            proposeCollateralInfoSub.setProposeCollateralHead(proposeCollateralInfoHead);

            final List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = transformProposeCollateralSubOwnerToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.getCollateralOwnerUWList(), proposeType);
            if(Util.isSafetyList(proposeCollateralSubOwnerList)){
                proposeCollateralInfoSub.setProposeCollateralSubOwnerList(proposeCollateralSubOwnerList);
            } else {
                proposeCollateralInfoSub.setProposeCollateralSubOwnerList(null);
            }

            final List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = transformProposeCollateralSubMortgageToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.getMortgageList(), proposeType);
            if(Util.isSafetyList(proposeCollateralSubMortgageList)){
                proposeCollateralInfoSub.setProposeCollateralSubMortgageList(proposeCollateralSubMortgageList);
            } else {
                proposeCollateralInfoSub.setProposeCollateralSubMortgageList(null);
            }

//            //TODO
//            final List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = transformProposeCollateralSubRelatedToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.get, proposeType);
//            if(Util.isSafetyList(proposeCollateralSubMortgageList)){
//                proposeCollateralInfoSub.setProposeCollateralSubRelatedList(null);
//            } else {
                proposeCollateralInfoSub.setProposeCollateralSubRelatedList(null);
//            }




        }

        return proposeCollateralInfoSub;
    }

    public List<ProposeCollateralInfoSub> transformProposeCollateralSubToModelList(WorkCase workCase, ProposeCollateralInfoHead proposeCollateralInfoHead, List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList, User user, ProposeType proposeType) {
        List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = new ArrayList<ProposeCollateralInfoSub>();
        if (!Util.isNull(proposeCollateralInfoSubViewList)) {
            for (ProposeCollateralInfoSubView proSubView : proposeCollateralInfoSubViewList) {
                ProposeCollateralInfoSub proposeColSub = transformProposeCollateralSubToModel(workCase, proposeCollateralInfoHead, proSubView, user, proposeType);
                proposeCollateralInfoSubList.add(proposeColSub);
            }
        }
        return proposeCollateralInfoSubList;
    }

    public ProposeCollateralSubOwner transformProposeCollateralSubOwnerToModel(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, CustomerInfoView customerInfoView, ProposeType proposeType) {
        ProposeCollateralSubOwner proposeCollateralSubOwner = new ProposeCollateralSubOwner();

        if(!Util.isNull(workCase)){
            if(!Util.isZero(workCase.getId())) {
                proposeCollateralSubOwner.setWorkCase(workCase);
            } else {
                proposeCollateralSubOwner.setWorkCase(null);
            }
        } else {
            proposeCollateralSubOwner.setWorkCase(workCase);
        }
        proposeCollateralSubOwner.setProposeType(proposeType);
        proposeCollateralSubOwner.setProposeCollateralSub(proposeCollateralInfoSub);
        proposeCollateralSubOwner.setCustomer(customerDAO.findById(customerInfoView.getId()));

        return proposeCollateralSubOwner;
    }

    public List<ProposeCollateralSubOwner> transformProposeCollateralSubOwnerToModelList(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, List<CustomerInfoView> customerInfoViewList, ProposeType proposeType) {
        List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = new ArrayList<ProposeCollateralSubOwner>();
        if (!Util.isNull(customerInfoViewList)) {
            for (CustomerInfoView cusView : customerInfoViewList) {
                ProposeCollateralSubOwner proposeCollateralSubOwner = transformProposeCollateralSubOwnerToModel(workCase, proposeCollateralInfoSub, cusView, proposeType);
                proposeCollateralSubOwnerList.add(proposeCollateralSubOwner);
            }
        }
        return proposeCollateralSubOwnerList;
    }

    public ProposeCollateralSubMortgage transformProposeCollateralSubMortgageToModel(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, MortgageTypeView mortgageTypeView, ProposeType proposeType) {
        ProposeCollateralSubMortgage proposeCollateralSubMortgage = new ProposeCollateralSubMortgage();

        if(!Util.isNull(workCase)){
            if(!Util.isZero(workCase.getId())) {
                proposeCollateralSubMortgage.setWorkCase(workCase);
            } else {
                proposeCollateralSubMortgage.setWorkCase(null);
            }
        } else {
            proposeCollateralSubMortgage.setWorkCase(workCase);
        }
        proposeCollateralSubMortgage.setProposeType(proposeType);
        proposeCollateralSubMortgage.setProposeCollateralSub(proposeCollateralInfoSub);
        proposeCollateralSubMortgage.setMortgageType(mortgageTypeDAO.findById(mortgageTypeView.getId()));

        return proposeCollateralSubMortgage;
    }

    public List<ProposeCollateralSubMortgage> transformProposeCollateralSubMortgageToModelList(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, List<MortgageTypeView> mortgageTypeViewList, ProposeType proposeType) {
        List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = new ArrayList<ProposeCollateralSubMortgage>();
        if (!Util.isNull(mortgageTypeViewList)) {
            for (MortgageTypeView mortgageTypeView : mortgageTypeViewList) {
                ProposeCollateralSubMortgage proposeCollateralSubMortgage = transformProposeCollateralSubMortgageToModel(workCase, proposeCollateralInfoSub, mortgageTypeView, proposeType);
                proposeCollateralSubMortgageList.add(proposeCollateralSubMortgage);
            }
        }
        return proposeCollateralSubMortgageList;
    }

    public List<ProposeCollateralSubRelated> transformProposeCollateralSubRelatedToModelList(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, ProposeCollateralInfoSub proposeCollateralInfoSubRelated, ProposeType proposeType) {
        List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = new ArrayList<ProposeCollateralSubRelated>();
        return proposeCollateralSubRelatedList;
    }

    public ProposeCollateralSubRelated transformProposeCollateralSubRelatedToModel(WorkCase workCase, ProposeCollateralInfoSub proposeCollateralInfoSub, ProposeCollateralInfoSub proposeCollateralInfoSubRelated, ProposeType proposeType) {
        ProposeCollateralSubRelated proposeCollateralSubRelated = new ProposeCollateralSubRelated();

        if(!Util.isNull(workCase)){
            if(!Util.isZero(workCase.getId())) {
                proposeCollateralSubRelated.setWorkCase(workCase);
            } else {
                proposeCollateralSubRelated.setWorkCase(null);
            }
        } else {
            proposeCollateralSubRelated.setWorkCase(workCase);
        }

        proposeCollateralSubRelated.setProposeType(proposeType);
        proposeCollateralSubRelated.setProposeCollateralSub(proposeCollateralInfoSub);
        proposeCollateralSubRelated.setProposeCollateralSubRelated(proposeCollateralInfoSubRelated);

        return proposeCollateralSubRelated;
    }

    //View
    public ProposeCollateralInfoView transformProposeCollateralToView(ProposeCollateralInfo proposeCollateralInfo, ProposeType proposeType) {
        ProposeCollateralInfoView proposeCollateralInfoView = null;
        if(!Util.isNull(proposeCollateralInfo) && !Util.isZero(proposeCollateralInfo.getId()) && proposeCollateralInfo.getProposeType() == proposeType){
            proposeCollateralInfoView = new ProposeCollateralInfoView();

            proposeCollateralInfoView.setId(proposeCollateralInfo.getId());

            proposeCollateralInfoView.setAppraisalRequest(proposeCollateralInfo.getAppraisalRequest());
            proposeCollateralInfoView.setComs(Util.isTrue(proposeCollateralInfo.getComs()));
            proposeCollateralInfoView.setJobID(proposeCollateralInfo.getJobID());
            proposeCollateralInfoView.setAppraisalDate(proposeCollateralInfo.getAppraisalDate());
            proposeCollateralInfoView.setNumberMonthsFromApprDate(proposeCollateralInfo.getNumberMonthsFromApprDate());
            proposeCollateralInfoView.setAadDecision(proposeCollateralInfo.getAadDecision());
            if(proposeCollateralInfo.getAadDecision()!=null && !proposeCollateralInfo.getAadDecision().trim().equalsIgnoreCase("")){
                AADDecision aadDecision = aadDecisionDAO.getByCode(proposeCollateralInfo.getAadDecision());
                proposeCollateralInfoView.setAadDecisionLabel(aadDecision.getDescription());
            } else {
                proposeCollateralInfoView.setAadDecisionLabel("-");
            }
            proposeCollateralInfoView.setAadDecisionReason(proposeCollateralInfo.getAadDecisionReason());
            proposeCollateralInfoView.setAadDecisionReasonDetail(proposeCollateralInfo.getAadDecisionReasonDetail());
            proposeCollateralInfoView.setUsage(proposeCollateralInfo.getUsage());
            proposeCollateralInfoView.setTypeOfUsage(proposeCollateralInfo.getTypeOfUsage());
            proposeCollateralInfoView.setUwRemark(proposeCollateralInfo.getUwRemark());
            proposeCollateralInfoView.setMortgageCondition(proposeCollateralInfo.getMortgageCondition());
            proposeCollateralInfoView.setMortgageConditionDetail(proposeCollateralInfo.getMortgageConditionDetail());
            proposeCollateralInfoView.setBdmComments(proposeCollateralInfo.getBdmComments());

            proposeCollateralInfoView.setUwDecision(proposeCollateralInfo.getUwDecision());

            List<ProposeCollateralInfoRelation> proposeCollateralInfoRelations = proposeCollateralInfoRelationDAO.findByCollateralId(proposeCollateralInfo.getId(), proposeType);

            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();

            for (ProposeCollateralInfoRelation proposeCollateralInfoRelation : proposeCollateralInfoRelations) {
                if(!Util.isNull(proposeCollateralInfoRelation)){
                    if(!Util.isNull(proposeCollateralInfoRelation.getProposeCreditInfo())) {
                        ProposeCreditInfoDetailView proposeCreditInfoDetailView = transformProposeCreditToViewScreen(proposeCollateralInfoRelation.getProposeCreditInfo(), null);
                        proposeCreditInfoDetailViewList.add(proposeCreditInfoDetailView);
                    } else if(!Util.isNull(proposeCollateralInfoRelation.getExistingCreditDetail())) {
                        ProposeCreditInfoDetailView existingCreditDetailView = transformProposeCreditToViewByExisting(proposeCollateralInfoRelation.getExistingCreditDetail(), null);
                        proposeCreditInfoDetailViewList.add(existingCreditDetailView);
                    }
                }
            }

            proposeCollateralInfoView.setProposeCreditInfoDetailViewList(proposeCreditInfoDetailViewList);

            log.debug("###################### HEAD : {}" , proposeCollateralInfo.getProposeCollateralInfoHeadList());
            log.debug("###################### HEAD Size : {}" , proposeCollateralInfo.getProposeCollateralInfoHeadList().size());
            proposeCollateralInfoView.setProposeCollateralInfoHeadViewList(transformProposeCollateralHeadToViewList(proposeCollateralInfo.getProposeCollateralInfoHeadList()));

            /*if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                for(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                    if(!Util.isNull(proposeCollateralInfoHeadView) && !Util.isNull(proposeCollateralInfoHeadView.getHeadCollType())) {
                        if(proposeCollateralInfoHeadView.getHeadCollType().getAppraisalRequire() != 0) {
                            proposeCollateralInfoView.setHeadCollAppraisal(true);
                        } else {
                            proposeCollateralInfoView.setHeadCollAppraisal(false);
                        }
                    }
                }
            }*/
        } else if (!Util.isNull(proposeCollateralInfo) && !Util.isZero(proposeCollateralInfo.getId()) && ProposeType.BOTH == proposeType) {
            proposeCollateralInfoView = new ProposeCollateralInfoView();

            proposeCollateralInfoView.setId(proposeCollateralInfo.getId());

            proposeCollateralInfoView.setAppraisalRequest(proposeCollateralInfo.getAppraisalRequest());
            proposeCollateralInfoView.setComs(Util.isTrue(proposeCollateralInfo.getComs()));
            proposeCollateralInfoView.setJobID(proposeCollateralInfo.getJobID());
            proposeCollateralInfoView.setAppraisalDate(proposeCollateralInfo.getAppraisalDate());
            proposeCollateralInfoView.setNumberMonthsFromApprDate(proposeCollateralInfo.getNumberMonthsFromApprDate());
            proposeCollateralInfoView.setAadDecision(proposeCollateralInfo.getAadDecision());
            if(proposeCollateralInfo.getAadDecision()!=null && !proposeCollateralInfo.getAadDecision().trim().equalsIgnoreCase("")){
                AADDecision aadDecision = aadDecisionDAO.getByCode(proposeCollateralInfo.getAadDecision());
                proposeCollateralInfoView.setAadDecisionLabel(aadDecision.getDescription());
            } else {
                proposeCollateralInfoView.setAadDecisionLabel("-");
            }
            proposeCollateralInfoView.setAadDecisionReason(proposeCollateralInfo.getAadDecisionReason());
            proposeCollateralInfoView.setAadDecisionReasonDetail(proposeCollateralInfo.getAadDecisionReasonDetail());
            proposeCollateralInfoView.setUsage(proposeCollateralInfo.getUsage());
            proposeCollateralInfoView.setTypeOfUsage(proposeCollateralInfo.getTypeOfUsage());
            proposeCollateralInfoView.setUwRemark(proposeCollateralInfo.getUwRemark());
            proposeCollateralInfoView.setMortgageCondition(proposeCollateralInfo.getMortgageCondition());
            proposeCollateralInfoView.setMortgageConditionDetail(proposeCollateralInfo.getMortgageConditionDetail());
            proposeCollateralInfoView.setBdmComments(proposeCollateralInfo.getBdmComments());

            proposeCollateralInfoView.setUwDecision(proposeCollateralInfo.getUwDecision());

            List<ProposeCollateralInfoRelation> proposeCollateralInfoRelations = proposeCollateralInfoRelationDAO.findByCollateralId(proposeCollateralInfo.getId(), proposeType);

            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();

            for (ProposeCollateralInfoRelation proposeCollateralInfoRelation : proposeCollateralInfoRelations) {
                if(!Util.isNull(proposeCollateralInfoRelation)){
                    if(!Util.isNull(proposeCollateralInfoRelation.getProposeCreditInfo())) {
                        ProposeCreditInfoDetailView proposeCreditInfoDetailView = transformProposeCreditToViewScreen(proposeCollateralInfoRelation.getProposeCreditInfo(), null);
                        proposeCreditInfoDetailViewList.add(proposeCreditInfoDetailView);
                    } else if(!Util.isNull(proposeCollateralInfoRelation.getExistingCreditDetail())) {
                        ProposeCreditInfoDetailView existingCreditDetailView = transformProposeCreditToViewByExisting(proposeCollateralInfoRelation.getExistingCreditDetail(), null);
                        proposeCreditInfoDetailViewList.add(existingCreditDetailView);
                    }
                }
            }

            proposeCollateralInfoView.setProposeCreditInfoDetailViewList(proposeCreditInfoDetailViewList);

            proposeCollateralInfoView.setProposeCollateralInfoHeadViewList(transformProposeCollateralHeadToViewList(proposeCollateralInfo.getProposeCollateralInfoHeadList()));
        }

        return proposeCollateralInfoView;
    }

    public List<ProposeCollateralInfoView> transformProposeCollateralToViewList(List<ProposeCollateralInfo> proposeCollateralInfoList, ProposeType proposeType) {
        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = new ArrayList<ProposeCollateralInfoView>();
        if (!Util.isNull(proposeCollateralInfoList)) {
            for (ProposeCollateralInfo proCollateral : proposeCollateralInfoList) {
                ProposeCollateralInfoView proposeCollateralInfoView = transformProposeCollateralToView(proCollateral, proposeType);
                if(!Util.isNull(proposeCollateralInfoView)) {
                    proposeCollateralInfoViewList.add(proposeCollateralInfoView);
                }
            }
        }
        return proposeCollateralInfoViewList;
    }

    public ProposeCollateralInfoHeadView transformProposeCollateralHeadToView(ProposeCollateralInfoHead proposeCollateralInfoHead) {
        ProposeCollateralInfoHeadView proposeCollateralInfoHeadView = new ProposeCollateralInfoHeadView();
        if(!Util.isNull(proposeCollateralInfoHead) && !Util.isZero(proposeCollateralInfoHead.getId())){
            proposeCollateralInfoHeadView.setId(proposeCollateralInfoHead.getId());

            proposeCollateralInfoHeadView.setTitleDeed(proposeCollateralInfoHead.getTitleDeed());
            proposeCollateralInfoHeadView.setCollateralLocation(proposeCollateralInfoHead.getCollateralLocation());
            proposeCollateralInfoHeadView.setAppraisalValue(proposeCollateralInfoHead.getAppraisalValue());
            proposeCollateralInfoHeadView.setComs(proposeCollateralInfoHead.getComs());

            if(!Util.isNull(proposeCollateralInfoHead.getHeadCollType())) {
                proposeCollateralInfoHeadView.setHeadCollType(proposeCollateralInfoHead.getHeadCollType());
            }else{
                proposeCollateralInfoHeadView.setHeadCollType(new CollateralType());
            }

            if(!Util.isNull(proposeCollateralInfoHead.getPotentialCollateral())) {
                proposeCollateralInfoHeadView.setPotentialCollateral(proposeCollateralInfoHead.getPotentialCollateral());
            }else{
                proposeCollateralInfoHeadView.setPotentialCollateral(new PotentialCollateral());
            }

            if(!Util.isNull(proposeCollateralInfoHead.getCollateralType())) {
                proposeCollateralInfoHeadView.setTcgCollateralType(proposeCollateralInfoHead.getCollateralType());
            }else{
                proposeCollateralInfoHeadView.setTcgCollateralType(new TCGCollateralType());
            }
            proposeCollateralInfoHeadView.setExistingCredit(proposeCollateralInfoHead.getExistingCredit());
            proposeCollateralInfoHeadView.setInsuranceCompany(proposeCollateralInfoHead.getInsuranceCompany());

            proposeCollateralInfoHeadView.setProposeCollateralInfoSubViewList(transformProposeCollateralSubToViewList(proposeCollateralInfoHead.getProposeCollateralInfoSubList()));

            if(!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                proposeCollateralInfoHeadView.setHaveSubColl(true);
            } else {
                proposeCollateralInfoHeadView.setHaveSubColl(false);
            }
        }

        return proposeCollateralInfoHeadView;
    }

    public List<ProposeCollateralInfoHeadView> transformProposeCollateralHeadToViewList(List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList) {
        List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
        if (!Util.isNull(proposeCollateralInfoHeadList)) {
            for (ProposeCollateralInfoHead proCollHead : proposeCollateralInfoHeadList) {
                ProposeCollateralInfoHeadView proCollHeadView = transformProposeCollateralHeadToView(proCollHead);
                proposeCollateralInfoHeadViewList.add(proCollHeadView);
            }
        }
        return proposeCollateralInfoHeadViewList;
    }

    public ProposeCollateralInfoSubView transformProposeCollateralSubToView(ProposeCollateralInfoSub proposeCollateralInfoSub) {
        ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
        if(!Util.isNull(proposeCollateralInfoSub) && !Util.isZero(proposeCollateralInfoSub.getId())){
            proposeCollateralInfoSubView.setId(proposeCollateralInfoSub.getId());

            if(!Util.isNull(proposeCollateralInfoSub.getSubCollateralType())) {
                proposeCollateralInfoSubView.setSubCollateralType(proposeCollateralInfoSub.getSubCollateralType());
            }else{
                proposeCollateralInfoSubView.setSubCollateralType(new SubCollateralType());
            }

            proposeCollateralInfoSubView.setTitleDeed(proposeCollateralInfoSub.getTitleDeed());
            proposeCollateralInfoSubView.setAddress(proposeCollateralInfoSub.getAddress());
            proposeCollateralInfoSubView.setLandOffice(proposeCollateralInfoSub.getLandOffice());
            proposeCollateralInfoSubView.setCollateralOwnerAAD(proposeCollateralInfoSub.getCollateralOwnerAAD());
            proposeCollateralInfoSubView.setAppraisalValue(proposeCollateralInfoSub.getAppraisalValue());
            proposeCollateralInfoSubView.setMortgageValue(proposeCollateralInfoSub.getMortgageValue());
            proposeCollateralInfoSubView.setSubId(proposeCollateralInfoSub.getSubId());
            proposeCollateralInfoSubView.setComs(proposeCollateralInfoSub.getComs());

            List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = proposeCollateralSubOwnerDAO.findCollSubOwnerByCollSub(proposeCollateralInfoSub);
            if (!Util.isNull(proposeCollateralSubOwnerList) && !Util.isZero(proposeCollateralSubOwnerList.size())) {
                List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
                for(ProposeCollateralSubOwner proposeCollateralSubOwner : proposeCollateralSubOwnerList) {
                    CustomerInfoView customerInfoView = customerTransform.transformToSelect(proposeCollateralSubOwner.getCustomer());
                    customerInfoViewList.add(customerInfoView);
                }
                proposeCollateralInfoSubView.setCollateralOwnerUWList(customerInfoViewList);
            }

            List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = proposeCollateralSubMortgageDAO.findCollSubMortgageByCollSub(proposeCollateralInfoSub);
            if (!Util.isNull(proposeCollateralSubMortgageList) && !Util.isZero(proposeCollateralSubMortgageList.size())) {
                List<MortgageTypeView> mortgageTypeViewList = new ArrayList<MortgageTypeView>();
                for (ProposeCollateralSubMortgage proposeCollateralSubMortgage : proposeCollateralSubMortgageList) {
                    MortgageTypeView mortgageTypeView = mortgageTypeTransform.transformToView(proposeCollateralSubMortgage.getMortgageType());
                    mortgageTypeViewList.add(mortgageTypeView);
                }
                proposeCollateralInfoSubView.setMortgageList(mortgageTypeViewList);
            }

            List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = proposeCollateralSubRelatedDAO.findCollSubRelatedByCollSub(proposeCollateralInfoSub);
            if (!Util.isNull(proposeCollateralSubRelatedList) && !Util.isZero(proposeCollateralSubRelatedList.size())) {
                List<ProposeCollateralInfoSubView> relateList = new ArrayList<ProposeCollateralInfoSubView>();
                for (ProposeCollateralSubRelated proposeCollateralSubRelated : proposeCollateralSubRelatedList) {
                    ProposeCollateralInfoSubView related = transformProposeCollateralSubToSimpleView(proposeCollateralSubRelated.getProposeCollateralSubRelated());
                    relateList.add(related);
                }
                proposeCollateralInfoSubView.setRelatedWithList(relateList);
            }
        }

        return proposeCollateralInfoSubView;
    }

    public ProposeCollateralInfoSubView transformProposeCollateralSubToSimpleView(ProposeCollateralInfoSub proposeCollateralInfoSub) {
        ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
        if(!Util.isNull(proposeCollateralInfoSub) && !Util.isZero(proposeCollateralInfoSub.getId())){
            proposeCollateralInfoSubView.setId(proposeCollateralInfoSub.getId());

            proposeCollateralInfoSubView.setSubCollateralType(proposeCollateralInfoSub.getSubCollateralType());
            proposeCollateralInfoSubView.setTitleDeed(proposeCollateralInfoSub.getTitleDeed());
            proposeCollateralInfoSubView.setAddress(proposeCollateralInfoSub.getAddress());
            proposeCollateralInfoSubView.setLandOffice(proposeCollateralInfoSub.getLandOffice());
            proposeCollateralInfoSubView.setCollateralOwnerAAD(proposeCollateralInfoSub.getCollateralOwnerAAD());
            proposeCollateralInfoSubView.setAppraisalValue(proposeCollateralInfoSub.getAppraisalValue());
            proposeCollateralInfoSubView.setMortgageValue(proposeCollateralInfoSub.getMortgageValue());
            proposeCollateralInfoSubView.setSubId(proposeCollateralInfoSub.getSubId());
        }

        return proposeCollateralInfoSubView;
    }

    public List<ProposeCollateralInfoSubView> transformProposeCollateralSubToViewList(List<ProposeCollateralInfoSub> proposeCollateralInfoSubList) {
        List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
        if (!Util.isNull(proposeCollateralInfoSubList)) {
            for (ProposeCollateralInfoSub proCollSub : proposeCollateralInfoSubList) {
                ProposeCollateralInfoSubView proCollSubView = transformProposeCollateralSubToView(proCollSub);
                proposeCollateralInfoSubViewList.add(proCollSubView);
            }
        }
        return proposeCollateralInfoSubViewList;
    }

    //-------------------------------------------------------- Decision --------------------------------------------------------//

    public DecisionView transformToDecisionView(ExistingCreditFacilityView existingCreditFacilityView, ProposeLineView proposeLineView, ProposeLineView approveLineView, Decision decision, long workCaseId) {
        DecisionView decisionView = new DecisionView();

        if (!Util.isNull(existingCreditFacilityView) && !Util.isZero(existingCreditFacilityView.getId())) {
            // Existing Condition
            decisionView.setExtConditionComCreditList(existingCreditFacilityView.getExistingConditionDetailViewList());
            // Borrower - List
            decisionView.setExtBorrowerComCreditList(existingCreditFacilityView.getBorrowerComExistingCredit());
            decisionView.setExtBorrowerRetailCreditList(existingCreditFacilityView.getBorrowerRetailExistingCredit());
            decisionView.setExtBorrowerAppInRLOSList(existingCreditFacilityView.getBorrowerAppInRLOSCredit());
            // Borrower - Total
            decisionView.setExtBorrowerTotalComLimit(existingCreditFacilityView.getTotalBorrowerComLimit());
            decisionView.setExtBorrowerTotalRetailLimit(existingCreditFacilityView.getTotalBorrowerRetailLimit());
            decisionView.setExtBorrowerTotalAppInRLOSLimit(existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit());
            decisionView.setExtBorrowerTotalCommercial(existingCreditFacilityView.getTotalBorrowerCom());
            decisionView.setExtBorrowerTotalComAndOBOD(existingCreditFacilityView.getTotalBorrowerComOBOD());
            decisionView.setExtBorrowerTotalExposure(existingCreditFacilityView.getTotalBorrowerExposure());
            // Related - List
            decisionView.setExtRelatedComCreditList(existingCreditFacilityView.getRelatedComExistingCredit());
            decisionView.setExtRelatedRetailCreditList(existingCreditFacilityView.getRelatedRetailExistingCredit());
            decisionView.setExtRelatedAppInRLOSList(existingCreditFacilityView.getRelatedAppInRLOSCredit());
            // Related - Total
            decisionView.setExtRelatedTotalComLimit(existingCreditFacilityView.getTotalRelatedComLimit());
            decisionView.setExtRelatedTotalRetailLimit(existingCreditFacilityView.getTotalRelatedRetailLimit());
            decisionView.setExtRelatedTotalAppInRLOSLimit(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit());
            decisionView.setExtRelatedTotalCommercial(existingCreditFacilityView.getTotalRelatedCom());
            decisionView.setExtRelatedTotalComAndOBOD(existingCreditFacilityView.getTotalRelatedComOBOD());
            decisionView.setExtRelatedTotalExposure(existingCreditFacilityView.getTotalRelatedExposure());
            // Group Total
            decisionView.setExtGroupTotalCommercial(existingCreditFacilityView.getTotalGroupCom());
            decisionView.setExtGroupTotalComAndOBOD(existingCreditFacilityView.getTotalGroupComOBOD());
            decisionView.setExtGroupTotalExposure(existingCreditFacilityView.getTotalGroupExposure());
            // Collateral - List
            decisionView.setExtBorrowerCollateralList(existingCreditFacilityView.getBorrowerCollateralList());
            decisionView.setExtRelatedCollateralList(existingCreditFacilityView.getRelatedCollateralList());
            // Collateral - Total
            decisionView.setExtBorrowerTotalAppraisalValue(existingCreditFacilityView.getTotalBorrowerAppraisalValue());
            decisionView.setExtBorrowerTotalMortgageValue(existingCreditFacilityView.getTotalBorrowerMortgageValue());
            decisionView.setExtRelatedTotalAppraisalValue(existingCreditFacilityView.getTotalRelatedAppraisalValue());
            decisionView.setExtRelatedTotalMortgageValue(existingCreditFacilityView.getTotalRelatedMortgageValue());
            // Guarantor
            decisionView.setExtGuarantorList(existingCreditFacilityView.getBorrowerGuarantorList());
            decisionView.setExtTotalGuaranteeAmount(existingCreditFacilityView.getTotalGuaranteeAmount());
        }

        if (!Util.isNull(proposeLineView) && !Util.isZero(proposeLineView.getId())) {
            decisionView.setNewCreditFacilityViewId(proposeLineView.getId());
            decisionView.setCreditCustomerType(proposeLineView.getCreditCustomerType());
            decisionView.setLoanRequestType(proposeLineView.getLoanRequestType());
            decisionView.setInvestedCountry(proposeLineView.getInvestedCountry());
            decisionView.setExistingSMELimit(proposeLineView.getExistingSMELimit());
            decisionView.setMaximumSMELimit(proposeLineView.getMaximumSMELimit());
            // Propose Credit Info.
            decisionView.setProposeCreditList(proposeLineView.getProposeCreditInfoDetailViewList());
            decisionView.setProposeTotalCreditLimit(proposeLineView.getTotalPropose());
            // Propose Collateral
            decisionView.setProposeCollateralList(proposeLineView.getProposeCollateralInfoViewList());
            // Propose Guarantor
            decisionView.setProposeGuarantorList(proposeLineView.getProposeGuarantorInfoViewList());
            decisionView.setProposeTotalGuaranteeAmt(proposeLineView.getTotalGuaranteeAmount());
            // Propose Price Fee Reduction Info.
            decisionView.setIntFeeDOA(proposeLineView.getIntFeeDOA());
            decisionView.setFrontendFeeDOA(proposeLineView.getFrontendFeeDOA());
            decisionView.setGuarantorBA(proposeLineView.getGuarantorBA());
            decisionView.setReasonForReduction(proposeLineView.getReasonForReduction());
        } else {
            //If Propose Line not found
            decisionView.setCreditCustomerType(CreditCustomerType.NOT_SELECTED);
            decisionView.setLoanRequestType(new CreditRequestTypeView());
            decisionView.setInvestedCountry(new CountryView());
        }

        //For Decision ----------------------------------------------------------------------
        // Approve data already been recorded
//        List<ProposeCreditInfo> approveCreditList = proposeCreditInfoDAO.findNewCreditDetail(workCaseId, ProposeType.A);
//        decisionView.setApproveCreditList(transformProposeCreditToViewList(approveCreditList, ProposeType.A));
        decisionView.setApproveCreditList(approveLineView.getProposeCreditInfoDetailViewList());

//        List<ProposeCollateralInfo> approveCollateralList = proposeCollateralInfoDAO.findNewCollateral(workCaseId, ProposeType.A);
//        decisionView.setApproveCollateralList(transformProposeCollateralToViewList(approveCollateralList, ProposeType.A));
        decisionView.setApproveCollateralList(approveLineView.getProposeCollateralInfoViewList());

//        List<ProposeGuarantorInfo> approveGuarantorList = proposeGuarantorInfoDAO.findNewGuarantorByNewCreditFacId(proposeLineView.getId(), ProposeType.A);
//        decisionView.setApproveGuarantorList(transformProposeGuarantorToViewList(approveGuarantorList, ProposeType.A));
        decisionView.setApproveGuarantorList(approveLineView.getProposeGuarantorInfoViewList());

        if(!Util.isNull(decision) && !Util.isZero(decision.getId())) {
            decisionView.setId(decision.getId());
            decisionView.setApproveTotalCreditLimit(decision.getTotalApproveCredit());
            decisionView.setApproveBrwTotalCommercial(decision.getTotalApproveCommercial());
            decisionView.setApproveBrwTotalComAndOBOD(decision.getTotalApproveComAndOBOD());
            decisionView.setApproveTotalExposure(decision.getTotalApproveExposure());
            decisionView.setApproveTotalGuaranteeAmt(decision.getTotalApproveGuaranteeAmt());

            decisionView.setCreateBy(decision.getCreateBy());
            decisionView.setCreateDate(decision.getCreateDate());
            decisionView.setModifyBy(decision.getModifyBy());
            decisionView.setModifyDate(decision.getModifyDate());

            // Hidden field
            decisionView.setApproveTotalODLimit(decision.getTotalApprovedODLimit());
            decisionView.setApproveTotalNumOfNewOD(decision.getTotalApproveNumOfNewOD());
            decisionView.setApproveTotalNumProposeCreditFac(decision.getTotalApproveNumProposeCreditFac());
            decisionView.setApproveTotalNumContingentPropose(decision.getTotalApproveNumContingentPropose());
            decisionView.setGrandTotalNumOfCoreAsset(decision.getTotalApproveNumOfCoreAsset());
            decisionView.setGrandTotalNumOfNonCoreAsset(decision.getTotalApproveNumOfNonCoreAsset());
            decisionView.setApproveTotalTCGGuaranteeAmt(decision.getTotalApproveTCGGuaranteeAmt());
            decisionView.setApproveTotalIndvGuaranteeAmt(decision.getTotalApproveIndiGuaranteeAmt());
            decisionView.setApproveTotalJurisGuaranteeAmt(decision.getTotalApproveJuriGuaranteeAmt());

            //Fee Info.
            List<ProposeFeeDetail> proposeFeeDetailList = proposeFeeDetailDAO.findByWorkCaseId(decision.getWorkCase().getId(), ProposeType.A);
            List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = transformProposeFeeToViewList(proposeFeeDetailList, ProposeType.A);
            List<ProposeFeeDetailView> proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();

            Map<String, ProposeFeeDetailView> proposeFeeDetailViewMap = new HashMap<String, ProposeFeeDetailView>();
            if(!Util.isNull(proposeFeeDetailViewOriginalList) && !Util.isZero(proposeFeeDetailViewOriginalList.size())) {
                for(ProposeFeeDetailView proFeeDetView : proposeFeeDetailViewOriginalList) {
                    if(!proposeFeeDetailViewMap.containsKey(proFeeDetView.getProductProgram())){
                        proposeFeeDetailViewMap.put(proFeeDetView.getProductProgram(), proFeeDetView);
                        proposeFeeDetailViewList.add(proFeeDetView);
                    }
                }
            }

            decisionView.setApproveFeeDetailViewList(proposeFeeDetailViewList);
            decisionView.setApproveFeeDetailViewOriginalList(proposeFeeDetailViewOriginalList);

            Map<String, ProposeCreditInfoDetailView> proProgramMap = new Hashtable<String, ProposeCreditInfoDetailView>();
            for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : decisionView.getApproveCreditList()) {
                if(proposeCreditInfoDetailView.getUwDecision() == DecisionType.APPROVED) {
                    if(!proProgramMap.containsKey(proposeCreditInfoDetailView.getProductProgramView().getName())){
                        proProgramMap.put(proposeCreditInfoDetailView.getProductProgramView().getName(), proposeCreditInfoDetailView);
                    }
                }
            }

            for (ProposeFeeDetailView proFeeDetView : decisionView.getApproveFeeDetailViewList()) {
                if(!proProgramMap.containsKey(proFeeDetView.getProductProgram())){
                    decisionView.getApproveFeeDetailViewList().remove(proFeeDetView);
                }
            }

            Iterator<ProposeFeeDetailView> proposeFeeDetailViewIterator = decisionView.getApproveFeeDetailViewList().iterator();
            while (proposeFeeDetailViewIterator.hasNext()) {
                ProposeFeeDetailView proFeeDetView = proposeFeeDetailViewIterator.next();
                if(!proProgramMap.containsKey(proFeeDetView.getProductProgram())){
                    proposeFeeDetailViewIterator.remove();
                }
            }
        }

        // Decision FollowUp Condition
        List<DecisionFollowCondition> decisionFollowConditionList = decisionFollowConditionDAO.findByWorkCase(workCaseId);
        decisionView.setDecisionFollowConditionViewList(decisionFollowConditionTransform.transformToView(decisionFollowConditionList));

        // Approval History
        List<ApprovalHistory> submittedApprovalHistories = approvalHistoryDAO.findByWorkCase(workCaseId, true);
        decisionView.setApprovalHistoryList(approvalHistoryTransform.transformToView(submittedApprovalHistories));

        return decisionView;
    }

    public ProposeLine transformDecisionToModel(DecisionView decisionView, WorkCase workCase, User user, ProposeType proposeType) {
        ProposeLine proposeLine = null;
        if(!Util.isNull(decisionView)){
            proposeLine = proposeLineDAO.findByWorkCaseId(workCase.getId());
            if(!Util.isNull(proposeLine) && !Util.isZero(proposeLine.getId())) {
                proposeLine.setCreateDate(new Date());
                proposeLine.setCreateBy(user);
            }
            proposeLine.setModifyDate(new Date());
            proposeLine.setModifyBy(user);

            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeLine.setWorkCase(workCase);
                } else {
                    proposeLine.setWorkCase(null);
                }
            } else {
                proposeLine.setWorkCase(workCase);
            }

            proposeLine.setCreditCustomerType(decisionView.getCreditCustomerType().value());
            proposeLine.setLoanRequestType(creditRequestTypeTransform.transformSelectToModel(decisionView.getLoanRequestType()));
            proposeLine.setInvestedCountry(countryTransform.transformSelectToModel(decisionView.getInvestedCountry()));
            proposeLine.setExistingSMELimit(decisionView.getExistingSMELimit());
            proposeLine.setMaximumSMELimit(decisionView.getMaximumSMELimit());

            proposeLine.setIntFeeDOA(decisionView.getIntFeeDOA());
            proposeLine.setFrontendFeeDOA(decisionView.getFrontendFeeDOA());
            proposeLine.setGuarantorBA(decisionView.getGuarantorBA());
            proposeLine.setReasonForReduction(decisionView.getReasonForReduction());

            proposeLine.setProposeCreditInfoList(transformProposeCreditToModelList(proposeLine, decisionView.getApproveCreditList(), workCase, user, proposeType));
        }
        return proposeLine;
    }

    public ProposeCollateralInfo transformProposeCollateralToModelByAppraisalResult(WorkCase workCase, ProposeLine proposeLine, ProposeCollateralInfoView proposeCollateralInfoView, User user, ProposeType proposeType) {
        ProposeCollateralInfo proposeCollateralInfo = null;
        if(!Util.isNull(proposeCollateralInfoView)){
            proposeCollateralInfo = new ProposeCollateralInfo();
            log.debug("## proposeCollateralInfoView.getId() [{}] ##",proposeCollateralInfoView.getId());
            proposeCollateralInfo.setCreateDate(new Date());
            proposeCollateralInfo.setCreateBy(user);
            proposeCollateralInfo.setModifyDate(new Date());
            proposeCollateralInfo.setModifyBy(user);

            proposeCollateralInfo.setProposeType(proposeType);
            proposeCollateralInfo.setProposeLine(proposeLine);
            if(!Util.isNull(workCase)){
                if(!Util.isZero(workCase.getId())) {
                    proposeCollateralInfo.setWorkCase(workCase);
                } else {
                    proposeCollateralInfo.setWorkCase(null);
                }
            } else {
                proposeCollateralInfo.setWorkCase(null);
            }

            proposeCollateralInfo.setAppraisalRequest(proposeCollateralInfoView.getAppraisalRequest());
            proposeCollateralInfo.setComs(Util.isTrue(proposeCollateralInfoView.isComs()));
            proposeCollateralInfo.setJobID(proposeCollateralInfoView.getJobID());
            proposeCollateralInfo.setAppraisalDate(proposeCollateralInfoView.getAppraisalDate());
            proposeCollateralInfo.setNumberMonthsFromApprDate(proposeCollateralInfoView.getNumberMonthsFromApprDate());
            proposeCollateralInfo.setAadDecision(proposeCollateralInfoView.getAadDecision());
            proposeCollateralInfo.setAadDecisionReason(proposeCollateralInfoView.getAadDecisionReason());
            proposeCollateralInfo.setAadDecisionReasonDetail(proposeCollateralInfoView.getAadDecisionReasonDetail());
            proposeCollateralInfo.setUsage(proposeCollateralInfoView.getUsage());
            proposeCollateralInfo.setTypeOfUsage(proposeCollateralInfoView.getTypeOfUsage());
            proposeCollateralInfo.setUwRemark(proposeCollateralInfoView.getUwRemark());
            proposeCollateralInfo.setMortgageCondition(proposeCollateralInfoView.getMortgageCondition());
            proposeCollateralInfo.setMortgageConditionDetail(proposeCollateralInfoView.getMortgageConditionDetail());
            proposeCollateralInfo.setBdmComments(proposeCollateralInfoView.getBdmComments());
            proposeCollateralInfo.setUwDecision(proposeCollateralInfoView.getUwDecision());

            final List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = transformProposeCollateralHeadToModelListByAppraisalResult(workCase, proposeCollateralInfo, proposeCollateralInfoView.getProposeCollateralInfoHeadViewList(), user, proposeType);
            if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                proposeCollateralInfo.setProposeCollateralInfoHeadList(proposeCollateralInfoHeadList);
            } else {
                proposeCollateralInfo.setProposeCollateralInfoHeadList(null);
            }

            final List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = null;
            if(Util.isSafetyList(proposeCollateralInfoHeadList)){
                proposeCollateralInfo.setProposeCollateralInfoRelationList(proposeCollateralInfoRelationList);
            } else {
                proposeCollateralInfo.setProposeCollateralInfoRelationList(null);
            }


        }

        return proposeCollateralInfo;
    }

    public List<ProposeCollateralInfoHead> transformProposeCollateralHeadToModelListByAppraisalResult(WorkCase workCase, ProposeCollateralInfo proposeCollateralInfo, List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList, User user, ProposeType proposeType) {
        List<ProposeCollateralInfoHead> proposeCollateralInfoHeadList = new ArrayList<ProposeCollateralInfoHead>();
        if (!Util.isNull(proposeCollateralInfoHeadViewList)) {
            for (ProposeCollateralInfoHeadView proColHeadView : proposeCollateralInfoHeadViewList) {
                ProposeCollateralInfoHead proposeColHead = transformProposeCollateralHeadToModelByAppraisalResult(workCase, proposeCollateralInfo, proColHeadView, user, proposeType);
                proposeCollateralInfoHeadList.add(proposeColHead);
            }
        }
        return proposeCollateralInfoHeadList;
    }

    public ProposeCollateralInfoHead transformProposeCollateralHeadToModelByAppraisalResult(WorkCase workCase, ProposeCollateralInfo proposeCollateralInfo, ProposeCollateralInfoHeadView proposeCollateralInfoHeadView, User user, ProposeType proposeType) {
        ProposeCollateralInfoHead proposeCollateralInfoHead = null;
        if(!Util.isNull(proposeCollateralInfoHeadView)){
            proposeCollateralInfoHead = new ProposeCollateralInfoHead();
            proposeCollateralInfoHead.setCreateDate(new Date());
            proposeCollateralInfoHead.setCreateBy(user);
            proposeCollateralInfoHead.setModifyDate(new Date());
            proposeCollateralInfoHead.setModifyBy(user);
            proposeCollateralInfoHead.setProposeType(proposeType);

            if(!Util.isNull(proposeCollateralInfoHeadView.getPotentialCollateral()) && !Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())){
                proposeCollateralInfoHead.setPotentialCollateral(proposeCollateralInfoHeadView.getPotentialCollateral());
            } else {
                proposeCollateralInfoHead.setPotentialCollateral(null);
            }

            if(!Util.isNull(proposeCollateralInfoHeadView.getTcgCollateralType()) && !Util.isZero(proposeCollateralInfoHeadView.getTcgCollateralType().getId())){
                proposeCollateralInfoHead.setCollateralType(proposeCollateralInfoHeadView.getTcgCollateralType());
            } else {
                proposeCollateralInfoHead.setCollateralType(null);
            }

            if(!Util.isNull(proposeCollateralInfoHeadView.getHeadCollType()) && !Util.isZero(proposeCollateralInfoHeadView.getHeadCollType().getId())){
                proposeCollateralInfoHead.setHeadCollType(proposeCollateralInfoHeadView.getHeadCollType());
            } else {
                proposeCollateralInfoHead.setHeadCollType(null);
            }

            proposeCollateralInfoHead.setExistingCredit(proposeCollateralInfoHeadView.getExistingCredit());
            proposeCollateralInfoHead.setTitleDeed(proposeCollateralInfoHeadView.getTitleDeed());
            proposeCollateralInfoHead.setCollateralLocation(proposeCollateralInfoHeadView.getCollateralLocation());
            proposeCollateralInfoHead.setAppraisalValue(proposeCollateralInfoHeadView.getAppraisalValue());
            proposeCollateralInfoHead.setInsuranceCompany(proposeCollateralInfoHeadView.getInsuranceCompany());
            proposeCollateralInfoHead.setProposeCollateral(proposeCollateralInfo);

            final List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = transformProposeCollateralSubToModelListByAppraisalResult(workCase, proposeCollateralInfoHead, proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList(), user, proposeType);
            if(Util.isSafetyList(proposeCollateralInfoSubList)){
                proposeCollateralInfoHead.setProposeCollateralInfoSubList(proposeCollateralInfoSubList);
            } else {
                proposeCollateralInfoHead.setProposeCollateralInfoSubList(null);
            }
        }

        return proposeCollateralInfoHead;
    }

    public List<ProposeCollateralInfoSub> transformProposeCollateralSubToModelListByAppraisalResult(WorkCase workCase, ProposeCollateralInfoHead proposeCollateralInfoHead, List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList, User user, ProposeType proposeType) {
        List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = new ArrayList<ProposeCollateralInfoSub>();
        if (!Util.isNull(proposeCollateralInfoSubViewList)) {
            for (ProposeCollateralInfoSubView proSubView : proposeCollateralInfoSubViewList) {
                ProposeCollateralInfoSub proposeColSub = transformProposeCollateralSubToModelByAppraisalResult(workCase, proposeCollateralInfoHead, proSubView, user, proposeType);
                proposeCollateralInfoSubList.add(proposeColSub);
            }
        }
        return proposeCollateralInfoSubList;
    }

    public ProposeCollateralInfoSub transformProposeCollateralSubToModelByAppraisalResult(WorkCase workCase, ProposeCollateralInfoHead proposeCollateralInfoHead, ProposeCollateralInfoSubView proposeCollateralInfoSubView, User user, ProposeType proposeType) {
        ProposeCollateralInfoSub proposeCollateralInfoSub = null;
        if(!Util.isNull(proposeCollateralInfoSubView)){
            proposeCollateralInfoSub = new ProposeCollateralInfoSub();
            proposeCollateralInfoSub.setCreateDate(new Date());
            proposeCollateralInfoSub.setCreateBy(user);
            proposeCollateralInfoSub.setModifyDate(new Date());
            proposeCollateralInfoSub.setModifyBy(user);

            if(!Util.isNull(proposeCollateralInfoSubView.getSubCollateralType()) && !Util.isZero(proposeCollateralInfoSubView.getSubCollateralType().getId())){
                proposeCollateralInfoSub.setSubCollateralType(proposeCollateralInfoSubView.getSubCollateralType());
            } else {
                proposeCollateralInfoSub.setSubCollateralType(null);
            }

            proposeCollateralInfoSub.setTitleDeed(proposeCollateralInfoSubView.getTitleDeed());
            proposeCollateralInfoSub.setAddress(proposeCollateralInfoSubView.getAddress());
            proposeCollateralInfoSub.setLandOffice(proposeCollateralInfoSubView.getLandOffice());
            proposeCollateralInfoSub.setCollateralOwnerAAD(proposeCollateralInfoSubView.getCollateralOwnerAAD());
            proposeCollateralInfoSub.setAppraisalValue(proposeCollateralInfoSubView.getAppraisalValue());
            proposeCollateralInfoSub.setMortgageValue(proposeCollateralInfoSubView.getMortgageValue());
            proposeCollateralInfoSub.setTypeOfUsage(proposeCollateralInfoSubView.getTypeOfUsage());

            UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00e");
            proposeCollateralInfoSub.setSubId(uid.randomUUID().toString());

            proposeCollateralInfoSub.setProposeCollateralHead(proposeCollateralInfoHead);

            final List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = transformProposeCollateralSubOwnerToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.getCollateralOwnerUWList(), proposeType);
            if(Util.isSafetyList(proposeCollateralSubOwnerList)){
                proposeCollateralInfoSub.setProposeCollateralSubOwnerList(proposeCollateralSubOwnerList);
            } else {
                proposeCollateralInfoSub.setProposeCollateralSubOwnerList(null);
            }

            final List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = transformProposeCollateralSubMortgageToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.getMortgageList(), proposeType);
            if(Util.isSafetyList(proposeCollateralSubMortgageList)){
                proposeCollateralInfoSub.setProposeCollateralSubMortgageList(proposeCollateralSubMortgageList);
            } else {
                proposeCollateralInfoSub.setProposeCollateralSubMortgageList(null);
            }

//            //TODO
//            final List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = transformProposeCollateralSubRelatedToModelList(workCase, proposeCollateralInfoSub, proposeCollateralInfoSubView.get, proposeType);
//            if(Util.isSafetyList(proposeCollateralSubMortgageList)){
//                proposeCollateralInfoSub.setProposeCollateralSubRelatedList(null);
//            } else {
            proposeCollateralInfoSub.setProposeCollateralSubRelatedList(null);
//            }




        }

        return proposeCollateralInfoSub;
    }
}
