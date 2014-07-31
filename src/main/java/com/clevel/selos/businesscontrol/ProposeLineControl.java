package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.integration.brms.model.response.PricingIntTier;
import com.clevel.selos.integration.brms.model.response.PricingInterest;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.business.CollateralBizTransform;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Stateless
public class ProposeLineControl extends BusinessControl {
    @SELOS
    @Inject
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private LoanPurposeDAO loanPurposeDAO;
    @Inject
    private DisbursementTypeDAO disbursementTypeDAO;
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
    private WorkCaseDAO workCaseDAO;
    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private ProposeFeeDetailDAO proposeFeeDetailDAO;
    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private ProposeCollateralInfoDAO proposeCollateralInfoDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO proposeCollateralInfoRelationDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO proposeCollateralInfoHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO proposeCollateralInfoSubDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO proposeCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubMortgageDAO proposeCollateralSubMortgageDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO proposeCollateralSubRelatedDAO;
    @Inject
    private BankStatementSummaryDAO bankStatementSummaryDAO;
    @Inject
    private BasicInfoDAO basicInfoDAO;

    @Inject
    private ProposeLineTransform proposeLineTransform;
    @Inject
    private BaseRateTransform baseRateTransform;
    @Inject
    private ProductTransform productTransform;
    @Inject
    private LoanPurposeTransform loanPurposeTransform;
    @Inject
    private DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    private CustomerTransform customerTransform;
    @Inject
    private FeeTransform feeTransform;
    @Inject
    private CollateralBizTransform collateralBizTransform;
    @Inject
    private PotentialCollateralTransform potentialCollateralTransform;

    @Inject
    private ProductControl productControl;
    @Inject
    private DBRControl dbrControl;
    @Inject
    private NCBInfoControl ncbInfoControl;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private TCGInfoControl tcgInfoControl;
    @Inject
    private BRMSControl brmsControl;
    @Inject
    private CreditFacExistingControl creditFacExistingControl;

    @Inject
    private COMSInterface comsInterface;

    @Inject
    public ProposeLineControl() {
    }

    public ProposeLineView findProposeLineViewByWorkCaseId(long workCaseId) {
        log.debug("findProposeLineViewByWorkCaseId :: workCaseId :: {}", workCaseId);
        ProposeLine proposeLine = null;
        if(!Util.isZero(workCaseId)) {
             proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        }
        ProposeLineView proposeLineView = proposeLineTransform.transformProposeLineToView(proposeLine, ProposeType.P);

        return proposeLineView;
    }

    public Map<String, Object> onChangeRequestType(ProposeCreditInfoDetailView proposeCreditInfoDetailView, ProductGroup productGroup, int mode) { //mode 1 = initial for edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(mode == 2){
            proposeCreditInfoDetailView.setProductProgramView(new ProductProgramView());
            proposeCreditInfoDetailView.setCreditTypeView(new CreditTypeView());
            proposeCreditInfoDetailView.setProductCode("");
            proposeCreditInfoDetailView.setProjectCode("");
            proposeCreditInfoDetailView.setFrontEndFee(BigDecimal.ZERO);
        }

        List<PrdGroupToPrdProgramView> productProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        List<PrdProgramToCreditTypeView> creditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (proposeCreditInfoDetailView.getRequestType() == RequestTypes.CHANGE.value()) { // change can add only tier
            productProgramViewList = productControl.getProductProgramForPropose();
            proposeCreditInfoDetailView.setRequestTypeMode(1);
        } else if (proposeCreditInfoDetailView.getRequestType() == RequestTypes.NEW.value()) {
            if (productGroup != null) {
                productProgramViewList = productControl.getPrdGroupToPrdProgramProposeByGroup(productGroup);
            }
            if (!Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList())
                && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())){
                if(mode == 1) {
                    proposeCreditInfoDetailView.setRequestTypeMode(3);
                } else {
                    proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(new ArrayList<ProposeCreditInfoTierDetailView>());
                    proposeCreditInfoDetailView.setRequestTypeMode(2);
                }
            } else {
                proposeCreditInfoDetailView.setRequestTypeMode(2);
            }
        }

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);
        returnMapVal.put("productProgramViewList", productProgramViewList);
        returnMapVal.put("creditTypeViewList", creditTypeViewList);

        return returnMapVal;
    }

    public Map<String, Object> onChangeProductProgram(ProposeCreditInfoDetailView proposeCreditInfoDetailView, int mode) { //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(mode == 1){
            proposeCreditInfoDetailView.setCreditTypeView(new CreditTypeView());
            proposeCreditInfoDetailView.setProductCode("");
            proposeCreditInfoDetailView.setProjectCode("");
        }

        List<PrdProgramToCreditTypeView> creditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(proposeCreditInfoDetailView.getProductProgramView());

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);
        returnMapVal.put("creditTypeViewList", creditTypeViewList);

        return returnMapVal;
    }

    public Map<String, Object> onChangeCreditType(ProposeLineView proposeLineView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int specialProgramId, int applyTCG, int mode) { //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeCreditInfoDetailView)) {
            if((!Util.isNull(proposeCreditInfoDetailView.getProductProgramView()) && !Util.isZero(proposeCreditInfoDetailView.getProductProgramView().getId())) &&
                    (!Util.isNull(proposeCreditInfoDetailView.getCreditTypeView()) && !Util.isZero(proposeCreditInfoDetailView.getCreditTypeView().getId()))) {
                ProductFormulaView productFormulaView = productControl.getProductFormulaView(proposeCreditInfoDetailView.getCreditTypeView().getId(),
                        proposeCreditInfoDetailView.getProductProgramView().getId(),
                        proposeLineView.getCreditCustomerType().value(),
                        specialProgramId,
                        applyTCG);
                if(!Util.isNull(productFormulaView) && !Util.isZero(productFormulaView.getId())) {
                    if(mode == 1) {
                        proposeCreditInfoDetailView.setProductCode(productFormulaView.getProductCode());
                        proposeCreditInfoDetailView.setProjectCode(productFormulaView.getProjectCode());
                    }
                    proposeCreditInfoDetailView.setCannotCheckReduceFront(productFormulaView.getReduceFrontEndFee() == 1);
                    proposeCreditInfoDetailView.setCannotCheckReducePricing(productFormulaView.getReducePricing() == 1);
                } else {
                    proposeCreditInfoDetailView.setProductCode("");
                    proposeCreditInfoDetailView.setProjectCode("");
                    proposeCreditInfoDetailView.setCannotCheckReduceFront(true);
                    proposeCreditInfoDetailView.setCannotCheckReducePricing(true);
                }
            } else {
                proposeCreditInfoDetailView.setProductCode("");
                proposeCreditInfoDetailView.setProjectCode("");
                proposeCreditInfoDetailView.setCannotCheckReduceFront(true);
                proposeCreditInfoDetailView.setCannotCheckReducePricing(true);
            }
        }

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);

        return returnMapVal;
    }

    public Map<String, Object> onChangeCreditType(DecisionView decisionView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int specialProgramId, int applyTCG, int mode) { //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeCreditInfoDetailView)) {
            if((!Util.isNull(proposeCreditInfoDetailView.getProductProgramView()) && !Util.isZero(proposeCreditInfoDetailView.getProductProgramView().getId())) &&
                    (!Util.isNull(proposeCreditInfoDetailView.getCreditTypeView()) && !Util.isZero(proposeCreditInfoDetailView.getCreditTypeView().getId()))) {
                ProductFormulaView productFormulaView = productControl.getProductFormulaView(proposeCreditInfoDetailView.getCreditTypeView().getId(),
                        proposeCreditInfoDetailView.getProductProgramView().getId(),
                        decisionView.getCreditCustomerType().value(),
                        specialProgramId,
                        applyTCG);
                if(!Util.isNull(productFormulaView) && !Util.isZero(productFormulaView.getId())) {
                    if(mode == 1) {
                        proposeCreditInfoDetailView.setProductCode(productFormulaView.getProductCode());
                        proposeCreditInfoDetailView.setProjectCode(productFormulaView.getProjectCode());
                    }
                    proposeCreditInfoDetailView.setCannotCheckReduceFront(productFormulaView.getReduceFrontEndFee() == 1);
                    proposeCreditInfoDetailView.setCannotCheckReducePricing(productFormulaView.getReducePricing() == 1);
                } else {
                    proposeCreditInfoDetailView.setProductCode("");
                    proposeCreditInfoDetailView.setProjectCode("");
                    proposeCreditInfoDetailView.setCannotCheckReduceFront(true);
                    proposeCreditInfoDetailView.setCannotCheckReducePricing(true);
                }
            } else {
                proposeCreditInfoDetailView.setProductCode("");
                proposeCreditInfoDetailView.setProjectCode("");
                proposeCreditInfoDetailView.setCannotCheckReduceFront(true);
                proposeCreditInfoDetailView.setCannotCheckReducePricing(true);
            }
        }

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);

        return returnMapVal;
    }

    public Map<String, Object> onChangeBaseRate(BaseRateView baseRateView,List<BaseRate> baseRateList){
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        baseRateView = getBaseRate(baseRateView, baseRateList);

        returnMapVal.put("baseRateView", baseRateView);

        return returnMapVal;
    }

    public Map<String, Object> onAddCreditTier(ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeCreditInfoDetailView)) {
            ProposeCreditInfoTierDetailView creditTierDetailAdd = new ProposeCreditInfoTierDetailView();
            creditTierDetailAdd.setFinalInterest(BigDecimal.ZERO);
            BaseRate baseRate = baseRateDAO.findById(1);
            creditTierDetailAdd.setFinalBasePrice(baseRateTransform.transformToView(baseRate));
            creditTierDetailAdd.setNo(proposeCreditInfoDetailView.getLastNo()+1);
            proposeCreditInfoDetailView.setLastNo(proposeCreditInfoDetailView.getLastNo()+1);

            if (proposeCreditInfoDetailView.getRequestType() == RequestTypes.NEW.value()) {
                if (proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList() != null) {
                    proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().add(0, creditTierDetailAdd);
                } else {
                    List<ProposeCreditInfoTierDetailView> tierDetailViewList = new ArrayList<ProposeCreditInfoTierDetailView>();
                    tierDetailViewList.add(0, creditTierDetailAdd);
                    proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(tierDetailViewList);
                }
            } else if (proposeCreditInfoDetailView.getRequestType() == RequestTypes.CHANGE.value()) {
                if (proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList() != null) {
                    proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().add(creditTierDetailAdd);
                } else {
                    List<ProposeCreditInfoTierDetailView> tierDetailViewList = new ArrayList<ProposeCreditInfoTierDetailView>();
                    tierDetailViewList.add(creditTierDetailAdd);
                    proposeCreditInfoDetailView.setProposeCreditInfoTierDetailViewList(tierDetailViewList);
                }
            }
        }

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);

        return returnMapVal;
    }

    public Map<String, Object> onRetrievePricing(long workCaseId, ProposeLineView proposeLineView) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();
        if (!Util.isNull(workCaseId)) {
            try {
                List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = new ArrayList<ProposeFeeDetailView>();
                StandardPricingResponse standardPricingResponse = brmsControl.getPriceFeeInterest(workCaseId);
                if (ActionResult.SUCCESS.equals(standardPricingResponse.getActionResult())) {
                    Map<Long, ProposeFeeDetailView> newFeeDetailViewMap = new HashMap<Long, ProposeFeeDetailView>();
                    ProposeFeeDetailView proposeFeeDetailView;
                    for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()) {
                        FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);
                        if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                            if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                                proposeFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                            } else {
                                proposeFeeDetailView = new ProposeFeeDetailView();
                                newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), proposeFeeDetailView);
                            }

                            ProposeCreditInfo proposeCreditInfo = proposeCreditInfoDAO.findById(feeDetailView.getCreditDetailViewId());
                            if (!Util.isNull(proposeCreditInfo)) {
                                ProposeCreditInfoDetailView proposeCreditInfoDetailView = proposeLineTransform.transformProposeCreditToView(proposeCreditInfo, ProposeType.P);
                                proposeFeeDetailView.setProposeCreditInfoDetailView(proposeCreditInfoDetailView);
                                ProductProgram productProgram = productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId());
                                if (!Util.isNull(productProgram)) {
                                    proposeFeeDetailView.setProductProgram(productProgram.getName());
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

                    if (newFeeDetailViewMap != null && newFeeDetailViewMap.size() > 0) {
                        Iterator it = newFeeDetailViewMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            proposeFeeDetailViewOriginalList.add((ProposeFeeDetailView) pairs.getValue());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }

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

                    if (standardPricingResponse.getPricingInterest() != null && standardPricingResponse.getPricingInterest().size() > 0) {
                        for (PricingInterest pricingInterest : standardPricingResponse.getPricingInterest()) {
                            String creditTypeId = pricingInterest.getCreditDetailId();
                            List<PricingIntTier> pricingIntTierList = pricingInterest.getPricingIntTierList();
                            for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeLineView.getProposeCreditInfoDetailViewList()) {
                                if (creditTypeId.equals(proposeCreditInfoDetailView.getId()+"")) {
                                    proposeCreditInfoDetailView = proposeLineTransform.transformPricingIntTierToView(pricingIntTierList, proposeCreditInfoDetailView);
                                    break;
                                }
                            }
                        }
                    }
                    returnMapVal.put("complete", 1);
                } else if (ActionResult.FAILED.equals(standardPricingResponse.getActionResult())) {
                    returnMapVal.put("complete", 2);
                    returnMapVal.put("standardPricingResponse", standardPricingResponse.getReason());
                }
            } catch (BRMSInterfaceException e) {
                log.debug("BRMSInterfaceException ::{}",e);
                returnMapVal.put("complete", 3);
                returnMapVal.put("error", e.getMessage());
            }
        }
        returnMapVal.put("proposeLineView", proposeLineView);
        return returnMapVal;
    }

    public Map<String, Object> onRetrievePricing(long workCaseId, DecisionView decisionView) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();
        if (!Util.isNull(workCaseId)) {
            try {
                List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = new ArrayList<ProposeFeeDetailView>();
                StandardPricingResponse standardPricingResponse = brmsControl.getPriceFeeInterest(workCaseId);
                if (ActionResult.SUCCESS.equals(standardPricingResponse.getActionResult())) {
                    Map<Long, ProposeFeeDetailView> newFeeDetailViewMap = new HashMap<Long, ProposeFeeDetailView>();
                    ProposeFeeDetailView proposeFeeDetailView;
                    for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()) {
                        FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);
                        if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                            if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                                proposeFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                            } else {
                                proposeFeeDetailView = new ProposeFeeDetailView();
                                newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), proposeFeeDetailView);
                            }

                            ProposeCreditInfo proposeCreditInfo = proposeCreditInfoDAO.findById(feeDetailView.getCreditDetailViewId());
                            if (!Util.isNull(proposeCreditInfo)) {
                                ProposeCreditInfoDetailView proposeCreditInfoDetailView = proposeLineTransform.transformProposeCreditToView(proposeCreditInfo, ProposeType.P);
                                proposeFeeDetailView.setProposeCreditInfoDetailView(proposeCreditInfoDetailView);
                                ProductProgram productProgram = productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId());
                                if (!Util.isNull(productProgram)) {
                                    proposeFeeDetailView.setProductProgram(productProgram.getName());
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

                    if (newFeeDetailViewMap != null && newFeeDetailViewMap.size() > 0) {
                        Iterator it = newFeeDetailViewMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            proposeFeeDetailViewOriginalList.add((ProposeFeeDetailView) pairs.getValue());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }

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


                    if (standardPricingResponse.getPricingInterest() != null && standardPricingResponse.getPricingInterest().size() > 0) {
                        for (PricingInterest pricingInterest : standardPricingResponse.getPricingInterest()) {
                            String creditTypeId = pricingInterest.getCreditDetailId();
                            List<PricingIntTier> pricingIntTierList = pricingInterest.getPricingIntTierList();
                            for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : decisionView.getApproveCreditList()) {
                                if (creditTypeId.equals(proposeCreditInfoDetailView.getId()+"")) {
                                    proposeCreditInfoDetailView = proposeLineTransform.transformPricingIntTierToView(pricingIntTierList, proposeCreditInfoDetailView);
                                    break;
                                }
                            }
                        }
                    }
                    returnMapVal.put("complete", 1);
                } else if (ActionResult.FAILED.equals(standardPricingResponse.getActionResult())) {
                    returnMapVal.put("complete", 2);
                    returnMapVal.put("standardPricingResponse", standardPricingResponse.getReason());
                }
            } catch (BRMSInterfaceException e) {
                log.debug("BRMSInterfaceException ::{}",e);
                returnMapVal.put("complete", 3);
                returnMapVal.put("error", e.getMessage());
            }
        }
        returnMapVal.put("decisionView", decisionView);
        return returnMapVal;
    }

    public Map<String, Object> onSaveCreditInfo(ProposeLineView proposeLineView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int mode, int rowIndex, int lastSeq, Hashtable hashSeqCredit) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId())));
        proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(creditTypeDAO.findById(proposeCreditInfoDetailView.getCreditTypeView().getId())));
        proposeCreditInfoDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(loanPurposeDAO.findById(proposeCreditInfoDetailView.getLoanPurposeView().getId())));
        proposeCreditInfoDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(disbursementTypeDAO.findById(proposeCreditInfoDetailView.getDisbursementTypeView().getId())));

        //for update label ( new & retrieve = have standard & suggest )
        if(!Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            for(ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(proposeCreditInfoDetailView.getRequestType() == 2) { // 1 = change , 2 = new
                    if(proposeCreditInfoTierDetailView.getBrmsFlag() == 1) { //is retrieve ?
                        if(!Util.isNull(proposeCreditInfoDetailView.getStandardBaseRate())) {
                            if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getStandardInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                                proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                            } else {
                                proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                            }
                        }

                        if(!Util.isNull(proposeCreditInfoDetailView.getSuggestBaseRate())) {
                            if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getSuggestInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                                proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                            } else {
                                proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                            }
                        }
                    }
                }
                if(!Util.isNull(proposeCreditInfoTierDetailView.getFinalBasePrice())) {
                    if (ValidationUtil.isValueCompareToZero(proposeCreditInfoTierDetailView.getFinalInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                        proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " " + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
                    } else {
                        proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " +" + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
                    }
                }
            }
        }

        if(mode == 1){
            lastSeq = lastSeq + 1;
            proposeCreditInfoDetailView.setSeq(lastSeq);
            proposeCreditInfoDetailView.setFrontEndFeeOriginal(proposeCreditInfoDetailView.getFrontEndFee()); // only set on this step
            proposeLineView.getProposeCreditInfoDetailViewList().add(proposeCreditInfoDetailView);
            hashSeqCredit.put(lastSeq, 0);
            lastSeq++;
        } else {
            proposeLineView.getProposeCreditInfoDetailViewList().set(rowIndex, proposeCreditInfoDetailView);
        }

        returnMapVal.put("proposeLineView", proposeLineView);
        returnMapVal.put("lastSeq", lastSeq);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onSaveCreditInfo(DecisionView decisionView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int mode, int rowIndex, int lastSeq, Hashtable hashSeqCredit) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        proposeCreditInfoDetailView.setProductProgramView(productTransform.transformToView(productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId())));
        proposeCreditInfoDetailView.setCreditTypeView(productTransform.transformToView(creditTypeDAO.findById(proposeCreditInfoDetailView.getCreditTypeView().getId())));
        proposeCreditInfoDetailView.setLoanPurposeView(loanPurposeTransform.transformToView(loanPurposeDAO.findById(proposeCreditInfoDetailView.getLoanPurposeView().getId())));
        proposeCreditInfoDetailView.setDisbursementTypeView(disbursementTypeTransform.transformToView(disbursementTypeDAO.findById(proposeCreditInfoDetailView.getDisbursementTypeView().getId())));

        //for update label ( new & retrieve = have standard & suggest )
        if(!Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            for(ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(proposeCreditInfoDetailView.getRequestType() == 2) { // 1 = change , 2 = new
                    if(proposeCreditInfoTierDetailView.getBrmsFlag() == 1) { //is retrieve ?
                        if(!Util.isNull(proposeCreditInfoDetailView.getStandardBaseRate())) {
                            if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getStandardInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                                proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                            } else {
                                proposeCreditInfoTierDetailView.setStandardPriceLabel(proposeCreditInfoDetailView.getStandardBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getStandardInterest()));
                            }
                        }

                        if(!Util.isNull(proposeCreditInfoDetailView.getSuggestBaseRate())) {
                            if (ValidationUtil.isValueCompareToZero(proposeCreditInfoDetailView.getSuggestInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                                proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " " + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                            } else {
                                proposeCreditInfoTierDetailView.setSuggestPriceLabel(proposeCreditInfoDetailView.getSuggestBaseRate().getName() + " +" + Util.formatNumber(proposeCreditInfoDetailView.getSuggestInterest()));
                            }
                        }
                    }
                }
                if(!Util.isNull(proposeCreditInfoTierDetailView.getFinalBasePrice())) {
                    if (ValidationUtil.isValueCompareToZero(proposeCreditInfoTierDetailView.getFinalInterest(), ValidationUtil.CompareMode.LESS_THAN)) {
                        proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " " + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
                    } else {
                        proposeCreditInfoTierDetailView.setFinalPriceLabel(proposeCreditInfoTierDetailView.getFinalBasePrice().getName() + " +" + Util.formatNumber(proposeCreditInfoTierDetailView.getFinalInterest()));
                    }
                }
            }
        }

        if(mode == 1){
            lastSeq = lastSeq + 1;
            proposeCreditInfoDetailView.setSeq(lastSeq);
            proposeCreditInfoDetailView.setFrontEndFeeOriginal(proposeCreditInfoDetailView.getFrontEndFee()); // only set on this step
            decisionView.getApproveCreditList().add(proposeCreditInfoDetailView);
            hashSeqCredit.put(lastSeq, 0);
            lastSeq++;
        } else {
            decisionView.getApproveCreditList().set(rowIndex, proposeCreditInfoDetailView);
        }

        returnMapVal.put("decisionView", decisionView);
        returnMapVal.put("lastSeq", lastSeq);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteCreditInfo(ProposeLineView proposeLineView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        boolean creditFlag = false;
        boolean completeFlag = false;

        if(hashSeqCredit.containsKey(proposeCreditInfoDetailView.getSeq())){
            int usageCount = (Integer)hashSeqCredit.get(proposeCreditInfoDetailView.getSeq());
            if(usageCount < 1){
                if(!Util.isZero(proposeCreditInfoDetailView.getId())){
                    proposeLineView.getDeleteCreditIdList().add(proposeCreditInfoDetailView.getId());
                }
                proposeLineView.getProposeCreditInfoDetailViewList().remove(proposeCreditInfoDetailView);
                creditFlag = true;
                completeFlag = true;
            }
        }

        if(!Util.isZero(proposeCreditInfoDetailView.getId())) {
            if(!Util.isNull(proposeLineView.getProposeFeeDetailViewList()) && !Util.isZero(proposeLineView.getProposeFeeDetailViewList().size())) {
                for(ProposeFeeDetailView proposeFeeDetailView : proposeLineView.getProposeFeeDetailViewOriginalList()) {
                    if(!Util.isNull(proposeFeeDetailView.getProposeCreditInfoDetailView()) && !Util.isZero(proposeFeeDetailView.getProposeCreditInfoDetailView().getId())) {
                        if(proposeFeeDetailView.getProposeCreditInfoDetailView().getId() == proposeCreditInfoDetailView.getId()) {
                            proposeLineView.getProposeFeeDetailViewOriginalList().remove(proposeFeeDetailView);
                        }
                    }
                }
            }
        }

        List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = proposeLineView.getProposeFeeDetailViewOriginalList();
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

        returnMapVal.put("proposeLineView", proposeLineView);
        returnMapVal.put("creditFlag", creditFlag);
        returnMapVal.put("completeFlag", completeFlag);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteCreditInfo(DecisionView decisionView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        boolean creditFlag = false;
        boolean completeFlag = false;

        if(hashSeqCredit.containsKey(proposeCreditInfoDetailView.getSeq())){
            int usageCount = (Integer)hashSeqCredit.get(proposeCreditInfoDetailView.getSeq());
            if(usageCount < 1){
                if(!Util.isZero(proposeCreditInfoDetailView.getId())){
                    decisionView.getDeleteCreditIdList().add(proposeCreditInfoDetailView.getId());
                }
                decisionView.getApproveCreditList().remove(proposeCreditInfoDetailView);
                creditFlag = true;
                completeFlag = true;
            }
        }

        if(!Util.isZero(proposeCreditInfoDetailView.getId())) {
            if(!Util.isNull(decisionView.getApproveFeeDetailViewList()) && !Util.isZero(decisionView.getApproveFeeDetailViewList().size())) {
                for(ProposeFeeDetailView proposeFeeDetailView : decisionView.getApproveFeeDetailViewOriginalList()) {
                    if(!Util.isNull(proposeFeeDetailView.getProposeCreditInfoDetailView()) && !Util.isZero(proposeFeeDetailView.getProposeCreditInfoDetailView().getId())) {
                        if(proposeFeeDetailView.getProposeCreditInfoDetailView().getId() == proposeCreditInfoDetailView.getId()) {
                            decisionView.getApproveFeeDetailViewOriginalList().remove(proposeFeeDetailView);
                        }
                    }
                }
            }
        }

        List<ProposeFeeDetailView> proposeFeeDetailViewOriginalList = decisionView.getApproveFeeDetailViewOriginalList();
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

        returnMapVal.put("decisionView", decisionView);
        returnMapVal.put("creditFlag", creditFlag);
        returnMapVal.put("completeFlag", completeFlag);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteProposeTierInfo(ProposeCreditInfoDetailView proposeCreditInfoDetailView, int rowIndex) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).getId())){
            proposeCreditInfoDetailView.getDeleteCreditTierIdList().add(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).getId());
        }
        proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().remove(rowIndex);

        returnMapVal.put("proposeCreditInfoDetailView", proposeCreditInfoDetailView);

        return returnMapVal;
    }

    public Map<String, Object> onCheckNoFlag(ProposeCreditInfoDetailView proposeCreditInfoDetailView, Hashtable hashSeqCredit, Hashtable hashSeqCreditTmp){
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(proposeCreditInfoDetailView.isNoFlag()){
            if(hashSeqCredit.containsKey(proposeCreditInfoDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCredit.get(proposeCreditInfoDetailView.getSeq());
                hashSeqCredit.put(proposeCreditInfoDetailView.getSeq(),++tmpCount);
            }
            if(hashSeqCreditTmp.containsKey(proposeCreditInfoDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCreditTmp.get(proposeCreditInfoDetailView.getSeq());
                hashSeqCreditTmp.put(proposeCreditInfoDetailView.getSeq(),++tmpCount);
            } else {
                hashSeqCreditTmp.put(proposeCreditInfoDetailView.getSeq(),1);
            }
        } else {
            if(hashSeqCredit.containsKey(proposeCreditInfoDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCredit.get(proposeCreditInfoDetailView.getSeq());
                hashSeqCredit.put(proposeCreditInfoDetailView.getSeq(),--tmpCount);
            }
            if(hashSeqCreditTmp.containsKey(proposeCreditInfoDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCreditTmp.get(proposeCreditInfoDetailView.getSeq());
                hashSeqCreditTmp.put(proposeCreditInfoDetailView.getSeq(),--tmpCount);
            }
        }

        returnMapVal.put("hashSeqCredit", hashSeqCredit);
        returnMapVal.put("hashSeqCreditTmp", hashSeqCreditTmp);

        return returnMapVal;
    }

    public Map<String, Object> onSaveConditionInfo(ProposeLineView proposeLineView, ProposeConditionInfoView proposeConditionInfoView) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        proposeLineView.getProposeConditionInfoViewList().add(proposeConditionInfoView);

        returnMapVal.put("proposeLineView", proposeLineView);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteConditionInfo(ProposeLineView proposeLineView, ProposeConditionInfoView proposeConditionInfoView) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isZero(proposeConditionInfoView.getId())){
            proposeLineView.getDeleteConditionIdList().add(proposeConditionInfoView.getId());
        }
        proposeLineView.getProposeConditionInfoViewList().remove(proposeConditionInfoView);

        returnMapVal.put("proposeLineView", proposeLineView);

        return returnMapVal;
    }

    public Map<String, Object> onSaveGuarantorInfo(ProposeLineView proposeLineView, ProposeGuarantorInfoView proposeGuarantorInfoView, Hashtable hashSeqCredit, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, List<CustomerInfoView> customerInfoViewList, int mode, int rowIndex) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        BigDecimal sumGuaranteeAmount = BigDecimal.ZERO;

        List<ProposeCreditInfoDetailView> proCreInfDetViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
            int checkList = 0;
            for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                if(!Util.isNull(proposeCreditInfoDetailView) && proposeCreditInfoDetailView.isNoFlag()) {
                    proCreInfDetViewList.add(proposeCreditInfoDetailView);
                    sumGuaranteeAmount = Util.add(sumGuaranteeAmount, proposeCreditInfoDetailView.getGuaranteeAmount());
                    checkList++;
                }
            }

            if(checkList > 0){
                returnMapVal.put("notCheckNoFlag", false);
            } else {
                returnMapVal.put("notCheckNoFlag", true); // not have check on credit
                return returnMapVal;
            }
        } else {
            returnMapVal.put("notCheckNoFlag", true); // not have check on credit
            return returnMapVal;
        }

        if(!Util.isNull(proposeGuarantorInfoView)){
            if(!Util.isNull(proposeGuarantorInfoView.getGuarantorName())){
                if(proposeGuarantorInfoView.getGuarantorName().getId() == -1){
                    proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    proposeGuarantorInfoView.setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(proposeGuarantorInfoView.getGuarantorName().getId(), customerInfoViewList);
                    proposeGuarantorInfoView.setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            }

            proposeGuarantorInfoView.setProposeCreditInfoDetailViewList(proCreInfDetViewList);
            proposeGuarantorInfoView.setGuaranteeAmount(sumGuaranteeAmount);

            if(mode == 1) {
                proposeLineView.getProposeGuarantorInfoViewList().add(proposeGuarantorInfoView);
            } else {
                proposeLineView.getProposeGuarantorInfoViewList().set(rowIndex, proposeGuarantorInfoView);
            }

            int seqTemp;
            for (int j = 0; j < proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size(); j++) {
                seqTemp = proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().get(j).getSeq();
                if(hashSeqCredit.containsKey(j)){
                    if (proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().get(j).isNoFlag()) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) + 1);
                    } else {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) - 1);
                    }
                }
            }
        }

        returnMapVal.put("proposeLineView", proposeLineView);
        returnMapVal.put("proposeGuarantorInfoView", proposeGuarantorInfoView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onSaveGuarantorInfo(DecisionView decisionView, ProposeGuarantorInfoView proposeGuarantorInfoView, Hashtable hashSeqCredit, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, List<CustomerInfoView> customerInfoViewList, int mode, int rowIndex) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        BigDecimal sumGuaranteeAmount = BigDecimal.ZERO;

        List<ProposeCreditInfoDetailView> proCreInfDetViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
            int checkList = 0;
            for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                if(!Util.isNull(proposeCreditInfoDetailView) && proposeCreditInfoDetailView.isNoFlag()) {
                    proCreInfDetViewList.add(proposeCreditInfoDetailView);
                    sumGuaranteeAmount = Util.add(sumGuaranteeAmount, proposeCreditInfoDetailView.getGuaranteeAmount());
                    checkList++;
                }
            }

            if(checkList > 0){
                returnMapVal.put("notCheckNoFlag", false);
            } else {
                returnMapVal.put("notCheckNoFlag", true); // not have check on credit
                return returnMapVal;
            }
        } else {
            returnMapVal.put("notCheckNoFlag", true); // not have check on credit
            return returnMapVal;
        }

        if(!Util.isNull(proposeGuarantorInfoView)){
            if(!Util.isNull(proposeGuarantorInfoView.getGuarantorName())){
                if(proposeGuarantorInfoView.getGuarantorName().getId() == -1){
                    proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    proposeGuarantorInfoView.setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(proposeGuarantorInfoView.getGuarantorName().getId(), customerInfoViewList);
                    proposeGuarantorInfoView.setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        proposeGuarantorInfoView.setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            }

            proposeGuarantorInfoView.setProposeCreditInfoDetailViewList(proCreInfDetViewList);
            proposeGuarantorInfoView.setGuaranteeAmount(sumGuaranteeAmount);

            if(mode == 1) {
                decisionView.getApproveGuarantorList().add(proposeGuarantorInfoView);
            } else {
                decisionView.getApproveGuarantorList().set(rowIndex, proposeGuarantorInfoView);
            }

            int seqTemp;
            for (int j = 0; j < proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size(); j++) {
                seqTemp = proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().get(j).getSeq();
                if(hashSeqCredit.containsKey(j)){
                    if (proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().get(j).isNoFlag()) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) + 1);
                    } else {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) - 1);
                    }
                }
            }
        }

        returnMapVal.put("decisionView", decisionView);
        returnMapVal.put("proposeGuarantorInfoView", proposeGuarantorInfoView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteGuarantorInfo(ProposeLineView proposeLineView, ProposeGuarantorInfoView proposeGuarantorInfoView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeGuarantorInfoView) && !Util.isNull(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size())) {
            for(ProposeCreditInfoDetailView proCreInfDetView : proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) {
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())){
                    int a = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    hashSeqCredit.put(proCreInfDetView.getSeq(),--a);
                }
            }
            if (proposeGuarantorInfoView.getId() != 0) {
                proposeLineView.getDeleteGuarantorIdList().add(proposeGuarantorInfoView.getId());
            }

            proposeLineView.getProposeGuarantorInfoViewList().remove(proposeGuarantorInfoView);
        }
        returnMapVal.put("proposeLineView", proposeLineView);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteGuarantorInfo(DecisionView decisionView, ProposeGuarantorInfoView proposeGuarantorInfoView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeGuarantorInfoView) && !Util.isNull(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size())) {
            for(ProposeCreditInfoDetailView proCreInfDetView : proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) {
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())){
                    int a = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    hashSeqCredit.put(proCreInfDetView.getSeq(),--a);
                }
            }
            if (proposeGuarantorInfoView.getId() != 0) {
                decisionView.getDeleteGuarantorIdList().add(proposeGuarantorInfoView.getId());
            }

            decisionView.getApproveGuarantorList().remove(proposeGuarantorInfoView);
        }
        returnMapVal.put("decisionView", decisionView);

        return returnMapVal;
    }

    public Map<String, Object> onCancelCollateralAndGuarantor(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, Hashtable hashSeqCredit, Hashtable hashSeqCreditTmp) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())){
            for(ProposeCreditInfoDetailView proCreInfDetView : proposeCreditInfoDetailViewList){
                if(hashSeqCreditTmp.containsKey(proCreInfDetView.getSeq())){
                    int tmpCount = (Integer)hashSeqCreditTmp.get(proCreInfDetView.getSeq());
                    int tmpCountOrigin = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    if(tmpCount != 0){
                        hashSeqCreditTmp.put(proCreInfDetView.getSeq(),0);
                        if(tmpCount > 0){
                            hashSeqCredit.put(proCreInfDetView.getSeq(),tmpCountOrigin-1);
                        } else {
                            hashSeqCredit.put(proCreInfDetView.getSeq(),tmpCountOrigin+1);
                        }
                    }
                }
            }
        }

        returnMapVal.put("proposeCreditInfoDetailViewList", proposeCreditInfoDetailViewList);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);
        returnMapVal.put("hashSeqCreditTmp", hashSeqCreditTmp);

        return returnMapVal;
    }

    public Map<String, Object> onEditGuarantorInfo(ProposeGuarantorInfoView proposeGuarantorInfoView, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeGuarantorInfoView)){
            if(!Util.isNull(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size())) {
                for(ProposeCreditInfoDetailView proCreInfDetView : proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) {
                    if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
                        for(ProposeCreditInfoDetailView proCreInfDetViewList : proposeCreditInfoDetailViewList) {
                            if(proCreInfDetView.getSeq() == proCreInfDetViewList.getSeq()) {
                                proCreInfDetViewList.setNoFlag(true);
                                proCreInfDetViewList.setGuaranteeAmount(proCreInfDetView.getGuaranteeAmount());
                            }
                        }
                    }
                }
            }
        }

        returnMapVal.put("proposeCreditViewList", proposeCreditInfoDetailViewList);

        return returnMapVal;
    }

    public Map<String, Object> onChangePotentialCollateralType(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView) { //mode 1 = initial for edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<PotentialColToTCGCol> potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
        if(!Util.isNull(proposeCollateralInfoHeadView) && !Util.isNull(proposeCollateralInfoHeadView.getPotentialCollateral()) && !Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())) {
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(proposeCollateralInfoHeadView.getPotentialCollateral().getId());
            if (Util.isNull(potentialColToTCGColList)) {
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
            }
        }

        proposeCollateralInfoHeadView.setPotentialColToTCGColList(potentialColToTCGColList);

        returnMapVal.put("proposeCollateralInfoHeadView", proposeCollateralInfoHeadView);

        return returnMapVal;
    }

    public Map<String, Object> onSaveCollateralInfo(ProposeLineView proposeLineView, ProposeCollateralInfoView proposeCollateralInfoView, List<PotentialCollateralView> potentialCollateralViewList, List<CollateralTypeView> collateralTypeViewList, Hashtable hashSeqCredit, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, int mode, int rowIndex) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<ProposeCreditInfoDetailView> proCreInfDetViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
            int checkList = 0;
            for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                if(!Util.isNull(proposeCreditInfoDetailView) && proposeCreditInfoDetailView.isNoFlag()) {
                    proCreInfDetViewList.add(proposeCreditInfoDetailView);
                    checkList++;
                }
            }

            if(checkList > 0){
                returnMapVal.put("notCheckNoFlag", false);
            } else {
                returnMapVal.put("notCheckNoFlag", true); // not have check on credit
                return returnMapVal;
            }
        } else {
            returnMapVal.put("notCheckNoFlag", true); // not have check on credit
            return returnMapVal;
        }

        returnMapVal.put("notHaveSub", false);
        if(!Util.isNull(proposeCollateralInfoView) && !Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
            for(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                if(!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList())) {
                    if(Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                        returnMapVal.put("notHaveSub", true);
                        return returnMapVal;
                    }
                } else {
                    returnMapVal.put("notHaveSub", true);
                    return returnMapVal;
                }
            }
        } else {
            returnMapVal.put("notHaveSub", true);
            return returnMapVal;
        }

        if(!Util.isNull(proposeCollateralInfoView)){
            proposeCollateralInfoView.setProposeCreditInfoDetailViewList(proCreInfDetViewList);
            if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                for(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                    PotentialCollateral potentialCollateral = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())) {
                        for(PotentialCollateralView pcv : potentialCollateralViewList) {
                            if(pcv.getId() == proposeCollateralInfoHeadView.getPotentialCollateral().getId()) {
                                potentialCollateral = new PotentialCollateral();
                                potentialCollateral.setId(pcv.getId());
                                potentialCollateral.setName(pcv.getName());
                                potentialCollateral.setDescription(pcv.getDescription());
                                break;
                            }
                        }
                    }

                    TCGCollateralType tcgCollateralType = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getTcgCollateralType().getId())) {
                        for(PotentialColToTCGCol p : proposeCollateralInfoHeadView.getPotentialColToTCGColList()) {
                            if(!Util.isNull(p.getTcgCollateralType()) && p.getTcgCollateralType().getId() == proposeCollateralInfoHeadView.getTcgCollateralType().getId()) {
                                tcgCollateralType = p.getTcgCollateralType();
                                break;
                            }
                        }
                    }

                    CollateralType collateralType = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getHeadCollType().getId())) {
                        for(CollateralTypeView ctv : collateralTypeViewList) {
                            if(ctv.getId() == proposeCollateralInfoHeadView.getHeadCollType().getId()) {
                                collateralType = new CollateralType();
                                collateralType.setId(ctv.getId());
                                collateralType.setDescription(ctv.getDescription());
                                collateralType.setCode(ctv.getCode());
                            }
                        }
                    }

                    if(!Util.isNull(potentialCollateral)){
                        proposeCollateralInfoHeadView.setPotentialCollateral(potentialCollateral);
                    }
                    if(!Util.isNull(tcgCollateralType)) {
                        proposeCollateralInfoHeadView.setTcgCollateralType(tcgCollateralType);
                    }
                    if(!Util.isNull(collateralType)) {
                        proposeCollateralInfoHeadView.setHeadCollType(collateralType);
                    }
                }
            }

            if(!proposeCollateralInfoView.isComs()) {
                proposeCollateralInfoView.setJobID("");
            }

            if(mode == 1) {
                proposeLineView.getProposeCollateralInfoViewList().add(proposeCollateralInfoView);
            } else {
                proposeLineView.getProposeCollateralInfoViewList().set(rowIndex, proposeCollateralInfoView);
            }

            int seqTemp;
            for (int j = 0; j < proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size(); j++) {
                seqTemp = proposeCollateralInfoView.getProposeCreditInfoDetailViewList().get(j).getSeq();
                if(hashSeqCredit.containsKey(j)){
                    if (proposeCollateralInfoView.getProposeCreditInfoDetailViewList().get(j).isNoFlag()) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) + 1);
                    } else {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) - 1);
                    }
                }
            }
        }

        returnMapVal.put("proposeLineView", proposeLineView);
        returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onSaveCollateralInfo(DecisionView decisionView, ProposeCollateralInfoView proposeCollateralInfoView, List<PotentialCollateralView> potentialCollateralViewList, List<CollateralTypeView> collateralTypeViewList, Hashtable hashSeqCredit, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, int mode, int rowIndex) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<ProposeCreditInfoDetailView> proCreInfDetViewList = new ArrayList<ProposeCreditInfoDetailView>();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
            int checkList = 0;
            for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                if(!Util.isNull(proposeCreditInfoDetailView) && proposeCreditInfoDetailView.isNoFlag()) {
                    proCreInfDetViewList.add(proposeCreditInfoDetailView);
                    checkList++;
                }
            }

            if(checkList > 0){
                returnMapVal.put("notCheckNoFlag", false);
            } else {
                returnMapVal.put("notCheckNoFlag", true); // not have check on credit
                return returnMapVal;
            }
        } else {
            returnMapVal.put("notCheckNoFlag", true); // not have check on credit
            return returnMapVal;
        }

        returnMapVal.put("notHaveSub", false);
        if(!Util.isNull(proposeCollateralInfoView) && !Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
            for(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                if(!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList())) {
                    if(Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                        returnMapVal.put("notHaveSub", true);
                        return returnMapVal;
                    }
                } else {
                    returnMapVal.put("notHaveSub", true);
                    return returnMapVal;
                }
            }
        } else {
            returnMapVal.put("notHaveSub", true);
            return returnMapVal;
        }

        if(!Util.isNull(proposeCollateralInfoView)){
            proposeCollateralInfoView.setProposeCreditInfoDetailViewList(proCreInfDetViewList);
            if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                for(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                    PotentialCollateral potentialCollateral = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())) {
                        for(PotentialCollateralView pcv : potentialCollateralViewList) {
                            if(pcv.getId() == proposeCollateralInfoHeadView.getPotentialCollateral().getId()) {
                                potentialCollateral = new PotentialCollateral();
                                potentialCollateral.setId(pcv.getId());
                                potentialCollateral.setName(pcv.getName());
                                potentialCollateral.setDescription(pcv.getDescription());
                                break;
                            }
                        }
                    }

                    TCGCollateralType tcgCollateralType = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getTcgCollateralType().getId())) {
                        for(PotentialColToTCGCol p : proposeCollateralInfoHeadView.getPotentialColToTCGColList()) {
                            if(!Util.isNull(p.getTcgCollateralType()) && p.getTcgCollateralType().getId() == proposeCollateralInfoHeadView.getTcgCollateralType().getId()) {
                                tcgCollateralType = p.getTcgCollateralType();
                                break;
                            }
                        }
                    }

                    CollateralType collateralType = null;
                    if(!Util.isZero(proposeCollateralInfoHeadView.getHeadCollType().getId())) {
                        for(CollateralTypeView ctv : collateralTypeViewList) {
                            if(ctv.getId() == proposeCollateralInfoHeadView.getHeadCollType().getId()) {
                                collateralType = new CollateralType();
                                collateralType.setId(ctv.getId());
                                collateralType.setDescription(ctv.getDescription());
                                collateralType.setCode(ctv.getCode());
                            }
                        }
                    }

                    if(!Util.isNull(potentialCollateral)){
                        proposeCollateralInfoHeadView.setPotentialCollateral(potentialCollateral);
                    }
                    if(!Util.isNull(tcgCollateralType)) {
                        proposeCollateralInfoHeadView.setTcgCollateralType(tcgCollateralType);
                    }
                    if(!Util.isNull(collateralType)) {
                        proposeCollateralInfoHeadView.setHeadCollType(collateralType);
                    }
                }
            }

            if(!proposeCollateralInfoView.isComs()) {
                proposeCollateralInfoView.setJobID("");
            }

            if(mode == 1) {
                decisionView.getApproveCollateralList().add(proposeCollateralInfoView);
            } else {
                decisionView.getApproveCollateralList().set(rowIndex, proposeCollateralInfoView);
            }

            int seqTemp;
            for (int j = 0; j < proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size(); j++) {
                seqTemp = proposeCollateralInfoView.getProposeCreditInfoDetailViewList().get(j).getSeq();
                if(hashSeqCredit.containsKey(j)){
                    if (proposeCollateralInfoView.getProposeCreditInfoDetailViewList().get(j).isNoFlag()) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) + 1);
                    } else {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) - 1);
                    }
                }
            }
        }

        returnMapVal.put("decisionView", decisionView);
        returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onSaveSubCollateralInfo(ProposeCollateralInfoView proposeCollateralInfoView, ProposeCollateralInfoSubView proposeCollateralInfoSubView, List<ProposeCollateralInfoSubView> relateWithList, List<SubCollateralType> subCollateralTypeList, int mode, int rowHeadCollIndex, int rowSubCollIndex) {  //mode 1 = add , 2 edit
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        for(SubCollateralType subCollateralType : subCollateralTypeList) {
            if(subCollateralType.getId() == proposeCollateralInfoSubView.getSubCollateralType().getId()) {
                proposeCollateralInfoSubView.setSubCollateralType(subCollateralType);
                break;
            }
        }

        if(!Util.isNull(proposeCollateralInfoView)){
            if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                if(mode == 1) {
                    if(Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().size())) {
                        proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).setHaveSubColl(true);
                    }
                    UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
                    proposeCollateralInfoSubView.setSubId(uid.randomUUID().toString());
                    proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().add(proposeCollateralInfoSubView);
                } else {
                    proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().set(rowSubCollIndex, proposeCollateralInfoSubView);
                }
            }
        }

        relateWithList.add(proposeCollateralInfoSubView);

        returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        returnMapVal.put("relateWithList", relateWithList);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteSubCollateralInfo(ProposeLineView proposeLineView, ProposeCollateralInfoView proposeCollateralInfoView, ProposeCollateralInfoSubView proposeCollateralInfoSubView, List<ProposeCollateralInfoSubView> relateWithList, int rowHeadCollIndex) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<String> subIdList = new ArrayList<String>();

        //for all
        if(!Util.isNull(proposeLineView.getProposeCollateralInfoViewList()) && !Util.isZero(proposeLineView.getProposeCollateralInfoViewList().size())){
            for(ProposeCollateralInfoView pciv : proposeLineView.getProposeCollateralInfoViewList()){
                if(!Util.isNull(pciv.getProposeCollateralInfoHeadViewList()) && !Util.isZero(pciv.getProposeCollateralInfoHeadViewList().size())){
                    for(ProposeCollateralInfoHeadView pcihv : pciv.getProposeCollateralInfoHeadViewList()){
                        if(!Util.isNull(pcihv.getProposeCollateralInfoSubViewList()) && !Util.isZero(pcihv.getProposeCollateralInfoSubViewList().size())){
                            for(ProposeCollateralInfoSubView pcisv : pcihv.getProposeCollateralInfoSubViewList()){
                                if(!Util.isNull(pcisv.getRelatedWithList()) && !Util.isZero(pcisv.getRelatedWithList().size())){
                                    for(ProposeCollateralInfoSubView pcisvRelate : pcisv.getRelatedWithList()){
                                        subIdList.add(pcisvRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //for dialog
        if(!Util.isNull(proposeCollateralInfoView) && !Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
            for(ProposeCollateralInfoHeadView pcihv : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()){
                if(!Util.isNull(pcihv.getProposeCollateralInfoSubViewList()) && !Util.isZero(pcihv.getProposeCollateralInfoSubViewList().size())){
                    for(ProposeCollateralInfoSubView pcisv : pcihv.getProposeCollateralInfoSubViewList()){
                        if(!Util.isNull(pcisv.getRelatedWithList()) && !Util.isZero(pcisv.getRelatedWithList().size())){
                            for(ProposeCollateralInfoSubView pcisvRelate : pcisv.getRelatedWithList()){
                                subIdList.add(pcisvRelate.getSubId());
                            }
                        }
                    }
                }
            }
        }

        boolean completeFlag = true;

        if(!Util.isNull(subIdList) && !Util.isZero(subIdList.size())){
            for(String subId : subIdList){
                if(proposeCollateralInfoSubView.getSubId().equalsIgnoreCase(subId)){
                    completeFlag = false;
                }
            }
        }
        if(completeFlag){
            proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().remove(proposeCollateralInfoSubView);

            if(Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().size())) {
                proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).setHaveSubColl(false);
            }

            if(!Util.isZero(proposeCollateralInfoSubView.getId())){
                proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getDeleteSubColHeadIdList().add(proposeCollateralInfoSubView.getId());
            }

            relateWithList.remove(proposeCollateralInfoSubView);
            returnMapVal.put("relateWithList", relateWithList);
            returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        }

        returnMapVal.put("completeFlag", completeFlag);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteSubCollateralInfo(DecisionView decisionView, ProposeCollateralInfoView proposeCollateralInfoView, ProposeCollateralInfoSubView proposeCollateralInfoSubView, List<ProposeCollateralInfoSubView> relateWithList, int rowHeadCollIndex) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<String> subIdList = new ArrayList<String>();

        //for all
        if(!Util.isNull(decisionView.getApproveCollateralList()) && !Util.isZero(decisionView.getApproveCollateralList().size())){
            for(ProposeCollateralInfoView pciv : decisionView.getApproveCollateralList()){
                if(!Util.isNull(pciv.getProposeCollateralInfoHeadViewList()) && !Util.isZero(pciv.getProposeCollateralInfoHeadViewList().size())){
                    for(ProposeCollateralInfoHeadView pcihv : pciv.getProposeCollateralInfoHeadViewList()){
                        if(!Util.isNull(pcihv.getProposeCollateralInfoSubViewList()) && !Util.isZero(pcihv.getProposeCollateralInfoSubViewList().size())){
                            for(ProposeCollateralInfoSubView pcisv : pcihv.getProposeCollateralInfoSubViewList()){
                                if(!Util.isNull(pcisv.getRelatedWithList()) && !Util.isZero(pcisv.getRelatedWithList().size())){
                                    for(ProposeCollateralInfoSubView pcisvRelate : pcisv.getRelatedWithList()){
                                        subIdList.add(pcisvRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //for dialog
        if(!Util.isNull(proposeCollateralInfoView) && !Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
            for(ProposeCollateralInfoHeadView pcihv : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()){
                if(!Util.isNull(pcihv.getProposeCollateralInfoSubViewList()) && !Util.isZero(pcihv.getProposeCollateralInfoSubViewList().size())){
                    for(ProposeCollateralInfoSubView pcisv : pcihv.getProposeCollateralInfoSubViewList()){
                        if(!Util.isNull(pcisv.getRelatedWithList()) && !Util.isZero(pcisv.getRelatedWithList().size())){
                            for(ProposeCollateralInfoSubView pcisvRelate : pcisv.getRelatedWithList()){
                                subIdList.add(pcisvRelate.getSubId());
                            }
                        }
                    }
                }
            }
        }

        boolean completeFlag = true;

        if(!Util.isNull(subIdList) && !Util.isZero(subIdList.size())){
            for(String subId : subIdList){
                if(proposeCollateralInfoSubView.getSubId().equalsIgnoreCase(subId)){
                    completeFlag = false;
                }
            }
        }
        if(completeFlag){
            proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().remove(proposeCollateralInfoSubView);

            if(Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getProposeCollateralInfoSubViewList().size())) {
                proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).setHaveSubColl(false);
            }

            if(!Util.isZero(proposeCollateralInfoSubView.getId())){
                proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getDeleteSubColHeadIdList().add(proposeCollateralInfoSubView.getId());
            }

            relateWithList.remove(proposeCollateralInfoSubView);
            returnMapVal.put("relateWithList", relateWithList);
            returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        }

        returnMapVal.put("completeFlag", completeFlag);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteCollateralInfo(ProposeLineView proposeLineView, ProposeCollateralInfoView proposeCollateralInfoView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<String> removeSubId = new ArrayList<String>();
        List<String> allSubId = new ArrayList<String>();
        if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())){
            for(ProposeCollateralInfoHeadView proColHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()){
                if(!Util.isNull(proColHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proColHeadView.getProposeCollateralInfoSubViewList().size())){
                    for(ProposeCollateralInfoSubView proColSubView : proColHeadView.getProposeCollateralInfoSubViewList()){
                        removeSubId.add(proColSubView.getSubId());
                    }
                }
            }
        }

        if(!Util.isNull(proposeLineView.getProposeCollateralInfoViewList()) && !Util.isZero(proposeLineView.getProposeCollateralInfoViewList().size())){
            for(ProposeCollateralInfoView proColView : proposeLineView.getProposeCollateralInfoViewList()){
                if(!Util.isNull(proColView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proColView.getProposeCollateralInfoHeadViewList().size())){
                    for(ProposeCollateralInfoHeadView proColHeadView : proColView.getProposeCollateralInfoHeadViewList()){
                        if(!Util.isNull(proColHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proColHeadView.getProposeCollateralInfoSubViewList().size())){
                            for(ProposeCollateralInfoSubView proColSubView : proColHeadView.getProposeCollateralInfoSubViewList()){
                                if(!Util.isNull(proColSubView.getRelatedWithList()) && !Util.isZero(proColSubView.getRelatedWithList().size())){
                                    for(ProposeCollateralInfoSubView proColSubRelate : proColSubView.getRelatedWithList()){
                                        allSubId.add(proColSubRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        returnMapVal.put("completeFlag", true);
        if(!Util.isNull(removeSubId) && !Util.isZero(removeSubId.size()) && !Util.isNull(allSubId) && !Util.isZero(allSubId.size())){
            for(String asid : allSubId){
                for(String rsid : removeSubId){
                    if(rsid.equalsIgnoreCase(asid)){
                        returnMapVal.put("completeFlag", false);
                        return returnMapVal;
                    }
                }
            }
        }

        if(!Util.isNull(proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size())){
            for(ProposeCreditInfoDetailView proCreInfDetView : proposeCollateralInfoView.getProposeCreditInfoDetailViewList()){
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())){
                    int a = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    hashSeqCredit.put(proCreInfDetView.getSeq(),--a);
                }
            }
        }

        if (!Util.isZero(proposeCollateralInfoView.getId())) {
            proposeLineView.getDeleteCollateralIdList().add(proposeCollateralInfoView.getId());
        }

        proposeLineView.getProposeCollateralInfoViewList().remove(proposeCollateralInfoView);

        returnMapVal.put("proposeLineView", proposeLineView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onDeleteCollateralInfo(DecisionView decisionView, ProposeCollateralInfoView proposeCollateralInfoView, Hashtable hashSeqCredit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        List<String> removeSubId = new ArrayList<String>();
        List<String> allSubId = new ArrayList<String>();
        if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())){
            for(ProposeCollateralInfoHeadView proColHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()){
                if(!Util.isNull(proColHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proColHeadView.getProposeCollateralInfoSubViewList().size())){
                    for(ProposeCollateralInfoSubView proColSubView : proColHeadView.getProposeCollateralInfoSubViewList()){
                        removeSubId.add(proColSubView.getSubId());
                    }
                }
            }
        }

        if(!Util.isNull(decisionView.getApproveCollateralList()) && !Util.isZero(decisionView.getApproveCollateralList().size())){
            for(ProposeCollateralInfoView proColView : decisionView.getApproveCollateralList()){
                if(!Util.isNull(proColView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proColView.getProposeCollateralInfoHeadViewList().size())){
                    for(ProposeCollateralInfoHeadView proColHeadView : proColView.getProposeCollateralInfoHeadViewList()){
                        if(!Util.isNull(proColHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proColHeadView.getProposeCollateralInfoSubViewList().size())){
                            for(ProposeCollateralInfoSubView proColSubView : proColHeadView.getProposeCollateralInfoSubViewList()){
                                if(!Util.isNull(proColSubView.getRelatedWithList()) && !Util.isZero(proColSubView.getRelatedWithList().size())){
                                    for(ProposeCollateralInfoSubView proColSubRelate : proColSubView.getRelatedWithList()){
                                        allSubId.add(proColSubRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        returnMapVal.put("completeFlag", true);
        if(!Util.isNull(removeSubId) && !Util.isZero(removeSubId.size()) && !Util.isNull(allSubId) && !Util.isZero(allSubId.size())){
            for(String asid : allSubId){
                for(String rsid : removeSubId){
                    if(rsid.equalsIgnoreCase(asid)){
                        returnMapVal.put("completeFlag", false);
                        return returnMapVal;
                    }
                }
            }
        }

        if(!Util.isNull(proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size())){
            for(ProposeCreditInfoDetailView proCreInfDetView : proposeCollateralInfoView.getProposeCreditInfoDetailViewList()){
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())){
                    int a = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    hashSeqCredit.put(proCreInfDetView.getSeq(),--a);
                }
            }
        }

        if (!Util.isZero(proposeCollateralInfoView.getId())) {
            decisionView.getDeleteCollateralIdList().add(proposeCollateralInfoView.getId());
        }

        decisionView.getApproveCollateralList().remove(proposeCollateralInfoView);

        returnMapVal.put("decisionView", decisionView);
        returnMapVal.put("hashSeqCredit", hashSeqCredit);

        return returnMapVal;
    }

    public Map<String, Object> onEditCollateralInfo(ProposeCollateralInfoView proposeCollateralInfoView, List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();

        if(!Util.isNull(proposeCollateralInfoView)){
            if(!Util.isNull(proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size())) {
                for(ProposeCreditInfoDetailView proCreInfDetView : proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) {
                    if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
                        for(ProposeCreditInfoDetailView proCreInfDetViewList : proposeCreditInfoDetailViewList) {
                            if(proCreInfDetView.getSeq() == proCreInfDetViewList.getSeq()) {
                                proCreInfDetViewList.setNoFlag(true);
                            }
                        }
                    }
                }
            }

            if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                for(ProposeCollateralInfoHeadView proColHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                    if(!Util.isNull(proColHeadView.getProposeCollateralInfoSubViewList())) {
                        if(Util.isZero(proColHeadView.getProposeCollateralInfoSubViewList().size())) {
                            proColHeadView.setHaveSubColl(false);
                        } else {
                            proColHeadView.setHaveSubColl(true);
                        }
                    } else {
                        proColHeadView.setHaveSubColl(false);
                    }

                    Map<String, Object> resultMapVal = onChangePotentialCollateralType(proColHeadView);
                    proColHeadView = (ProposeCollateralInfoHeadView) resultMapVal.get("proposeCollateralInfoHeadView");
                }
            }
        }

        returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
        returnMapVal.put("proposeCreditViewList", proposeCreditInfoDetailViewList);

        return returnMapVal;
    }

    public Map<String, Object> onRetrieveAppraisalReport(String jobId, User user, List<ProposeCollateralInfoView> proposeCollateralInfoViewList) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();
        Integer completeFlag = 1;
        if (!Util.isNull(jobId) && !Util.isEmpty(jobId) && !Util.isNull(user)) {
            if (checkJobIdExist(proposeCollateralInfoViewList, jobId)) {
                try {
                    AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(user.getId(), jobId);
                    if (!Util.isNull(appraisalDataResult) && ActionResult.SUCCESS.equals(appraisalDataResult.getActionResult())) {
                        ProposeCollateralInfoView proposeCollateralInfoView = collateralBizTransform.transformAppraisalToProposeCollateralView(appraisalDataResult);
                        returnMapVal.put("proposeCollateralInfoView", proposeCollateralInfoView);
                        returnMapVal.put("completeFlag", completeFlag);
                    } else {
                        completeFlag = 2;
                        returnMapVal.put("error", appraisalDataResult.getReason());
                        returnMapVal.put("completeFlag", completeFlag);
                    }
                } catch (COMSInterfaceException e) {
                    log.debug("COMSInterfaceException :: {}", e);
                    completeFlag = 2;
                    returnMapVal.put("error", e.getMessage());
                    returnMapVal.put("completeFlag", completeFlag);
                }
            } else {
                completeFlag = 3;
                returnMapVal.put("completeFlag", completeFlag);
            }
        }

        return returnMapVal;
    }

    public boolean checkJobIdExist(List<ProposeCollateralInfoView> proposeCollateralInfoViewList, String jobId) {
        for (ProposeCollateralInfoView proposeCollateralInfoView : proposeCollateralInfoViewList) {
            if (Util.equals(proposeCollateralInfoView.getJobID(), jobId)) {
                return false;
            }
        }
        return true;
    }

    public int getLastSeqNumberFromProposeCredit(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        int lastSeqNumber = 1;
        if (proposeCreditInfoDetailViewList != null && proposeCreditInfoDetailViewList.size() > 0) {
            for(ProposeCreditInfoDetailView proCredit : proposeCreditInfoDetailViewList){
                if(lastSeqNumber < proCredit.getSeq()){
                    lastSeqNumber = proCredit.getSeq();
                }
            }
        }
        return lastSeqNumber;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditFromProposeCreditAndExistingCredit(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList, List<ExistingCreditDetailView> existingCreditDetailViewList) {
        List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViews = new ArrayList<ProposeCreditInfoDetailView>();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
            Cloner cloner = new Cloner();
            proposeCreditInfoDetailViews = cloner.deepClone(proposeCreditInfoDetailViewList);
        }

        if(!Util.isNull(existingCreditDetailViewList) && !Util.isZero(existingCreditDetailViewList.size())) {
            for(ExistingCreditDetailView exCreView : existingCreditDetailViewList) {
                ProposeCreditInfoDetailView proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();
                proposeCreditInfoDetailView.setId(exCreView.getId());
                proposeCreditInfoDetailView.setAccountName(exCreView.getAccountName());
                proposeCreditInfoDetailView.setAccountNumber(exCreView.getAccountNumber());
                proposeCreditInfoDetailView.setAccountSuf(exCreView.getAccountSuf());
                proposeCreditInfoDetailView.setProductProgramView(exCreView.getExistProductProgramView());
                proposeCreditInfoDetailView.setCreditTypeView(exCreView.getExistCreditTypeView());
                proposeCreditInfoDetailView.setLimit(exCreView.getLimit());
                proposeCreditInfoDetailView.setExistingCredit(true);
                proposeCreditInfoDetailViews.add(proposeCreditInfoDetailView);
            }
        }

        return proposeCreditInfoDetailViews;
    }

    public void calWC(long workCaseId) {
        log.debug("calWC ::: workCaseId : {}", workCaseId);
        BigDecimal dayOfYear = BigDecimal.valueOf(365);
        BigDecimal monthOfYear = BigDecimal.valueOf(12);
        BigDecimal onePointTwoFive = BigDecimal.valueOf(1.25);
        BigDecimal onePointFive = BigDecimal.valueOf(1.50);
        BigDecimal thirtyFive = BigDecimal.valueOf(35);
        BigDecimal fifty = BigDecimal.valueOf(50);
        BigDecimal oneHundred = BigDecimal.valueOf(100);
        BigDecimal two = BigDecimal.valueOf(2);

        // ยอดขาย/รายได้
        BigDecimal adjustDBR = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนจากหน้า NCB
        BigDecimal revolvingCreditNCB = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนส่วนผู้เกี่ยวข้องในหน้า DBR
        BigDecimal revolvingCreditDBR = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes
        BigDecimal loanBurdenWCFlag = BigDecimal.ZERO;
        // วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB
        BigDecimal revolvingCreditNCBTMBFlag = BigDecimal.ZERO;
        // ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        BigDecimal loanBurdenTMBFlag = BigDecimal.ZERO;

        //how to check role and get ap ar inv !?
        BigDecimal weightAP = BigDecimal.ZERO;
        BigDecimal weightAR = BigDecimal.ZERO;
        BigDecimal weightINV = BigDecimal.ZERO;
        // Sum(weight cost of goods sold * businessProportion)
        // cost of goods = business desc ( column COG )
        // business proportion = สัดส่วนธุรกิจ ในแต่ละ business < %Income >
        BigDecimal aaaValue = BigDecimal.ZERO;

        //table 1
        BigDecimal wcNeed;
        BigDecimal totalWcDebit;
        BigDecimal totalWcTmb;
        BigDecimal wcNeedDiffer;

        //table 2
        BigDecimal case1WcLimit;
        BigDecimal case1WcMinLimit;
        BigDecimal case1Wc50CoreWc;
        BigDecimal case1WcDebitCoreWc;

        //table 3
        BigDecimal case2WcLimit;
        BigDecimal case2WcMinLimit;
        BigDecimal case2Wc50CoreWc;
        BigDecimal case2WcDebitCoreWc;

        //table 4
        BigDecimal case3WcLimit;
        BigDecimal case3WcMinLimit;
        BigDecimal case3Wc50CoreWc;
        BigDecimal case3WcDebitCoreWc;

        ////////////////////////////////////////////////////

        DBRView dbrView = dbrControl.getDBRByWorkCase(workCaseId);
        if (dbrView != null) {
            adjustDBR = dbrView.getMonthlyIncomeAdjust();
            revolvingCreditDBR = dbrView.getTotalMonthDebtRelatedWc();
        }

        List<NCB> ncbList = ncbInfoControl.getNCBByWorkCaseId(workCaseId);
        if (ncbList != null && ncbList.size() > 0) {
            for (NCB ncb : ncbList) {
                revolvingCreditNCB = Util.add(revolvingCreditNCB, ncb.getLoanCreditNCB());
                loanBurdenWCFlag = Util.add(loanBurdenWCFlag, ncb.getLoanCreditWC());
                revolvingCreditNCBTMBFlag = Util.add(revolvingCreditNCBTMBFlag, ncb.getLoanCreditTMB());
                loanBurdenTMBFlag = Util.add(loanBurdenTMBFlag, ncb.getLoanCreditWCTMB());
            }
        }


        BizInfoSummaryView bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
        if (bizInfoSummaryView != null) {
            weightAR = bizInfoSummaryView.getSumWeightAR();
            weightAP = bizInfoSummaryView.getSumWeightAP();
            weightINV = bizInfoSummaryView.getSumWeightINV();
            // Sum(weight cost of goods sold * businessProportion)
            List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
            if (bizInfoSummaryView.getId() != 0) {
                bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryView.getId());
                if (bizInfoDetailViewList != null && bizInfoDetailViewList.size() > 0) {
                    for (BizInfoDetailView bidv : bizInfoDetailViewList) {
                        BigDecimal cog = BigDecimal.ZERO;
                        if (bidv.getBizDesc() != null) {
                            cog = bidv.getBizDesc().getCog();
                        }
                        aaaValue = Util.add(aaaValue, Util.divide(Util.multiply(cog, bidv.getPercentBiz()), oneHundred));
                    }
                }
            }
        }

        //*** ยอดขาย/รายได้  = รายได้ต่อเดือน (adjusted) [DBR] * 12
        BigDecimal salesIncome = Util.multiply(adjustDBR, monthOfYear);
        //calculation
        //(ยอดขาย/รายได้ หาร 365 คูณ Weighted AR) + (AAAValue หาร 365 คูณ Weighted INV) - ((AAAValue หาร 365 คูณ Weighted AP)
        wcNeed = Util.subtract((Util.add(Util.multiply(Util.divide(salesIncome, dayOfYear), weightAR), Util.multiply(Util.divide(aaaValue, dayOfYear), weightINV))), (Util.multiply(Util.divide(aaaValue, dayOfYear), weightAP)));
        //Sum (วงเงินสินเชื่อหมุนเวียนจากหน้า NCB และ ส่วนผู้เกี่ยวข้องในหน้า DBR + ภาระสินเชื่อประเภทอื่นๆ จากหน้า NCB ที่มี flag W/C = Yes )
        totalWcDebit = Util.add(Util.add(revolvingCreditNCB, revolvingCreditDBR), loanBurdenWCFlag);
        //วงเงินสินเชื่อหมุนเวียนใน NCB ที่ flag เป็น TMB + ภาระสินเชื่อประเภทอื่น ที่ flag TMB และ flag W/C
        totalWcTmb = Util.add(revolvingCreditNCBTMBFlag, loanBurdenTMBFlag);
        //wcNeed - totalWcDebit
        wcNeedDiffer = Util.subtract(wcNeed, totalWcDebit);

        log.debug("Value ::: wcNeed : {}, totalWcDebit : {}, totalWcTmb : {}, wcNeedDiffer : {}", wcNeed, totalWcDebit, totalWcTmb, wcNeedDiffer);


        //1.25 x wcNeed
        case1WcLimit = Util.multiply(wcNeed, onePointTwoFive);
        //case1WcLimit - totalWcDebit
        case1WcMinLimit = Util.subtract(case1WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case1WcLimit และไม่เกิน case1WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case1Wc50CoreWc = Util.compareToFindLower(Util.divide(case1WcLimit, two), case1WcMinLimit);
        //case1WcMinLimit - case1Wc50CoreWc
        case1WcDebitCoreWc = Util.subtract(case1WcMinLimit, case1Wc50CoreWc);

        log.debug("Value ::: case1WcLimit : {}, case1WcMinLimit : {}, case1Wc50CoreWc : {}, case1WcDebitCoreWc : {}", case1WcLimit, case1WcMinLimit, case1Wc50CoreWc, case1WcDebitCoreWc);

        //1.5 x wcNeed
        case2WcLimit = Util.multiply(wcNeed, onePointFive);
        //case2WcLimit - totalWcDebit
        case2WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case2WcLimit และไม่เกิน case2WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case2Wc50CoreWc = Util.compareToFindLower(Util.divide(case2WcLimit, two), case2WcMinLimit);
        //case2WcMinLimit - case2Wc50CoreWc
        case2WcDebitCoreWc = Util.subtract(case2WcMinLimit, case2Wc50CoreWc);

        log.debug("Value ::: case2WcLimit : {}, case2WcMinLimit : {}, case2Wc50CoreWc : {}, case2WcDebitCoreWc : {}", case2WcLimit, case2WcMinLimit, case2Wc50CoreWc, case2WcDebitCoreWc);

        //ยอดขาย/รายได้ หาร 12 คูณ 35%
        case3WcLimit = Util.divide(Util.multiply(Util.divide(salesIncome, monthOfYear), thirtyFive), oneHundred);
        //case3WcLimit - totalWcDebit
        case3WcMinLimit = Util.subtract(case2WcLimit, totalWcDebit);
        //ไม่เกิน 50% ของ case3WcLimit และไม่เกิน case3WcMinLimit แล้วแต่ตัวไหนจะต่ำกว่า
        case3Wc50CoreWc = Util.compareToFindLower(Util.divide(case3WcLimit, two), case3WcMinLimit);
        //case3WcMinLimit - case3Wc50CoreWc
        case3WcDebitCoreWc = Util.subtract(case3WcMinLimit, case3Wc50CoreWc);

        log.debug("Value ::: case3WcLimit : {}, case3WcMinLimit : {}, case3Wc50CoreWc : {}, case3WcDebitCoreWc : {}", case3WcLimit, case3WcMinLimit, case3Wc50CoreWc, case3WcDebitCoreWc);

        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        if(proposeLine == null){
            proposeLine = new ProposeLine();
            WorkCase workCase = new WorkCase();
            workCase.setId(workCaseId);
            proposeLine.setWorkCase(workCase);
        }

        proposeLine.setWcNeed(wcNeed);
        proposeLine.setTotalWCDebit(totalWcDebit);
        proposeLine.setTotalWCTmb(totalWcTmb);
        proposeLine.setWcNeedDiffer(wcNeedDiffer);
        proposeLine.setCase1WCLimit(case1WcLimit);
        proposeLine.setCase1WCMinLimit(case1WcMinLimit);
        proposeLine.setCase1WC50CoreWC(case1Wc50CoreWc);
        proposeLine.setCase1WCDebitCoreWC(case1WcDebitCoreWc);
        proposeLine.setCase2WCLimit(case2WcLimit);
        proposeLine.setCase2WCMinLimit(case2WcMinLimit);
        proposeLine.setCase2WC50CoreWC(case2Wc50CoreWc);
        proposeLine.setCase2WCDebitCoreWC(case2WcDebitCoreWc);
        proposeLine.setCase3WCLimit(case3WcLimit);
        proposeLine.setCase3WCMinLimit(case3WcMinLimit);
        proposeLine.setCase3WC50CoreWC(case3Wc50CoreWc);
        proposeLine.setCase3WCDebitCoreWC(case3WcDebitCoreWc);
        proposeLineDAO.persist(proposeLine);
    }

    public List<CustomerInfoView> getGuarantorByWorkCaseId(long workCaseId) {
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToSelectList(customerDAO.findGuarantorByWorkCaseId(workCaseId));
        return customerInfoViewList;
    }

    private BaseRateView getBaseRate(BaseRateView inputBaseRate, List<BaseRate> baseRateList) {
        BaseRateView returnBaseRateView = new BaseRateView();
        if(!Util.isNull(inputBaseRate) && !Util.isZero(inputBaseRate.getId())){
            if (!Util.isNull(baseRateList) && !baseRateList.isEmpty() && inputBaseRate.getId() != 0) {
                for (BaseRate baseRate : baseRateList) {
                    if (baseRate.getId() == inputBaseRate.getId()) {
                        returnBaseRateView.setId(baseRate.getId());
                        returnBaseRateView.setActive(baseRate.getActive());
                        returnBaseRateView.setName(baseRate.getName());
                        returnBaseRateView.setValue(baseRate.getValue());
                    }
                }
            }
        }
        return returnBaseRateView;
    }

    public ProposeCreditInfoDetailView onCalInstallment(ProposeLineView proposeLineView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int specialProgramId, int applyTCG){
        if(!Util.isNull(proposeCreditInfoDetailView) && !Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            if(proposeCreditInfoDetailView.getLimit().compareTo(BigDecimal.ZERO) < 0) { // limit < 0
                return proposeCreditInfoDetailView;
            }
            for(ProposeCreditInfoTierDetailView proCreInfTieDetView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(proCreInfTieDetView.getBrmsFlag() == 1){ // cal only brms
                    if(proCreInfTieDetView.getTenor() < 0){ // if tenor < 0
                        return proposeCreditInfoDetailView;
                    }

                    if(proposeCreditInfoDetailView.isReduceFrontEndFee()){ // on save front end fee to original only , cal save on front end fee only
                        proposeCreditInfoDetailView.setFrontEndFee(Util.subtract(proposeCreditInfoDetailView.getFrontEndFeeOriginal(),proposeLineView.getFrontendFeeDOA()));
                    } else {
                        proposeCreditInfoDetailView.setFrontEndFee(proposeCreditInfoDetailView.getFrontEndFeeOriginal());
                    }

                    BigDecimal standard = BigDecimal.ZERO;
                    BigDecimal suggest = BigDecimal.ZERO;
                    if(!Util.isNull(proposeCreditInfoDetailView.getStandardBaseRate())){
                        standard  = Util.add(proposeCreditInfoDetailView.getStandardBaseRate().getValue(), proposeCreditInfoDetailView.getStandardInterest());
                    }
                    if(!Util.isNull(proposeCreditInfoDetailView.getSuggestBaseRate())){
                        suggest = Util.add(proposeCreditInfoDetailView.getSuggestBaseRate().getValue(), proposeCreditInfoDetailView.getSuggestInterest());
                    }

                    if(standard.compareTo(suggest) > 0){
                        proCreInfTieDetView.setFinalBasePrice(proposeCreditInfoDetailView.getStandardBaseRate());
                        proCreInfTieDetView.setFinalInterest(proposeCreditInfoDetailView.getStandardInterest());
                        proCreInfTieDetView.setFinalInterestOriginal(proposeCreditInfoDetailView.getStandardInterest());
                    } else {
                        proCreInfTieDetView.setFinalBasePrice(proposeCreditInfoDetailView.getSuggestBaseRate());
                        proCreInfTieDetView.setFinalInterest(proposeCreditInfoDetailView.getSuggestInterest());
                        proCreInfTieDetView.setFinalInterestOriginal(proposeCreditInfoDetailView.getSuggestInterest());
                    }

                    if(proposeCreditInfoDetailView.isReducePriceFlag()){
                        proCreInfTieDetView.setFinalInterest(Util.subtract(proCreInfTieDetView.getFinalInterestOriginal(),proposeLineView.getIntFeeDOA())); // reduce interest
                    }

                    BigDecimal twelve = new BigDecimal(12);
                    BigDecimal oneHundred = new BigDecimal(100);
                    BigDecimal baseRate = BigDecimal.ZERO;
                    BigDecimal interest = BigDecimal.ZERO;
                    BigDecimal interestOriginal = BigDecimal.ZERO;
                    BigDecimal spread = BigDecimal.ZERO;

                    if((!Util.isNull(proposeCreditInfoDetailView.getProductProgramView()) && !Util.isZero(proposeCreditInfoDetailView.getProductProgramView().getId())) &&
                            (!Util.isNull(proposeCreditInfoDetailView.getCreditTypeView()) && !Util.isZero(proposeCreditInfoDetailView.getCreditTypeView().getId()))){

                    }

                    PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(proposeCreditInfoDetailView.getCreditTypeView().getId(), proposeCreditInfoDetailView.getProductProgramView().getId());

                    if(!Util.isNull(prdProgramToCreditType)){
                        ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLineView.getCreditCustomerType().value(), specialProgramId, applyTCG);
                        if(!Util.isNull(productFormula)){
                            spread = productFormula.getDbrSpread();
                        }
                    }

                    if (proCreInfTieDetView.getFinalBasePrice() != null) {
                        baseRate = proCreInfTieDetView.getFinalBasePrice().getValue();
                    }
                    if (proCreInfTieDetView.getFinalInterest() != null) {
                        interest = proCreInfTieDetView.getFinalInterest();
                        interestOriginal = proCreInfTieDetView.getFinalInterestOriginal();
                    }

                    BigDecimal interestPerMonth = Util.divide(Util.divide(Util.add(Util.add(spread,baseRate),interest),oneHundred,100),twelve,100);
                    BigDecimal interestPerMonthOriginal = Util.divide(Util.divide(Util.add(Util.add(spread,baseRate),interestOriginal),oneHundred,100),twelve,100);
                    BigDecimal limit = BigDecimal.ZERO;
                    int tenor = proCreInfTieDetView.getTenor();
                    BigDecimal installment;
                    BigDecimal installmentOriginal;

                    if (proposeCreditInfoDetailView.getLimit() != null) {
                        limit = proposeCreditInfoDetailView.getLimit();
                    }

                    installment = Util.divide(Util.multiply(Util.multiply(interestPerMonth,limit),Util.add(BigDecimal.ONE,interestPerMonth).pow(tenor)) ,
                            Util.subtract(Util.add(BigDecimal.ONE,interestPerMonth).pow(tenor),BigDecimal.ONE));

                    installmentOriginal = Util.divide(Util.multiply(Util.multiply(interestPerMonthOriginal,limit),Util.add(BigDecimal.ONE,interestPerMonthOriginal).pow(tenor)) ,
                            Util.subtract(Util.add(BigDecimal.ONE,interestPerMonthOriginal).pow(tenor),BigDecimal.ONE));

                    if (installment != null) {
                        installment.setScale(2, RoundingMode.HALF_UP);
                    }

                    if (installmentOriginal != null) {
                        installmentOriginal.setScale(2, RoundingMode.HALF_UP);
                    }

                    proCreInfTieDetView.setInstallment(installment);
                    proCreInfTieDetView.setInstallmentOriginal(installmentOriginal);
                } else {
                    proCreInfTieDetView.setInstallmentOriginal(proCreInfTieDetView.getInstallment());
                }
            }
        }

        if (!Util.isNull(proposeCreditInfoDetailView)) {
            BigDecimal sumOfPCE = Util.multiply(proposeCreditInfoDetailView.getLimit(), proposeCreditInfoDetailView.getPCEPercent());
            BigDecimal sum = Util.divide(sumOfPCE, BigDecimal.valueOf(100));

            if (!Util.isNull(sum)){
                sum.setScale(2, RoundingMode.HALF_UP);
            }
            proposeCreditInfoDetailView.setPCEAmount(sum);
        }

        if(!Util.isNull(proposeCreditInfoDetailView) && !Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            BigDecimal mostInstallment = BigDecimal.ZERO;
            for(ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(mostInstallment.compareTo(proposeCreditInfoTierDetailView.getInstallmentOriginal()) < 0){
                    mostInstallment = proposeCreditInfoTierDetailView.getInstallmentOriginal();
                }
            }
            proposeCreditInfoDetailView.setInstallment(mostInstallment);
        }

        return proposeCreditInfoDetailView;
    }

    public List<ProposeCollateralInfoSubView> getRelateWithList(List<ProposeCollateralInfoView> proposeCollateralInfoViewList) {
        List<ProposeCollateralInfoSubView> relatedWithAllList = new ArrayList<ProposeCollateralInfoSubView>();
        if(!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
            for (ProposeCollateralInfoView proposeCollateralInfoView : proposeCollateralInfoViewList) {
                if(!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                    for (ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                        if (!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) {
                                relatedWithAllList.add(proposeCollateralInfoSubView);
                            }
                        }
                    }
                }
            }
        }
        return relatedWithAllList;
    }

    public CustomerInfoView getCustomerViewFromList(long customerId, List<CustomerInfoView> customerList) {
        CustomerInfoView returnCusInfoView = new CustomerInfoView();
        if (!Util.isNull(customerList) && !Util.isZero(customerList.size()) && !Util.isZero(customerId) ) {
            for (CustomerInfoView cusInfoView : customerList) {
                if (cusInfoView.getId() == customerId) {
                    returnCusInfoView.setId(cusInfoView.getId());
                    returnCusInfoView.setFirstNameTh(cusInfoView.getFirstNameTh());
                    returnCusInfoView.setLastNameTh(cusInfoView.getLastNameTh());
                    returnCusInfoView.setTitleTh(cusInfoView.getTitleTh());
                    returnCusInfoView.setCustomerEntity(cusInfoView.getCustomerEntity());
                    break;
                }
            }
        }
        return returnCusInfoView;
    }

    public MortgageTypeView getMortgageTypeById(int mortgageTypeId, List<MortgageTypeView> mortgageTypeViewList) {
        MortgageTypeView returnMortgageTypeView = new MortgageTypeView();
        if (!Util.isNull(mortgageTypeViewList) && !Util.isZero(mortgageTypeViewList.size()) && !Util.isZero(mortgageTypeId) ) {
            for (MortgageTypeView mortgageTypeView : mortgageTypeViewList) {
                if (mortgageTypeView.getId() == mortgageTypeId) {
                    returnMortgageTypeView.setId(mortgageTypeView.getId());
                    returnMortgageTypeView.setMortgage(mortgageTypeView.getMortgage());
                    break;
                }
            }
        }
        return returnMortgageTypeView;
    }

    public ProposeCollateralInfoSubView getRelateWithBySubId(String relateWithSubId, List<ProposeCollateralInfoSubView> relateWithList) {
        ProposeCollateralInfoSubView returnRelate = new ProposeCollateralInfoSubView();
        if (!Util.isNull(relateWithList) && !Util.isZero(relateWithList.size()) && !Util.isNull(relateWithSubId) && !Util.isEmpty(relateWithSubId)) {
            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : relateWithList) {
                if (proposeCollateralInfoSubView.getSubId().equalsIgnoreCase(relateWithSubId)) {
                    returnRelate.setId(proposeCollateralInfoSubView.getId());
                    returnRelate.setSubId(proposeCollateralInfoSubView.getSubId());
                    returnRelate.setTitleDeed(proposeCollateralInfoSubView.getTitleDeed());
                    break;
                }
            }
        }
        return returnRelate;
    }

    public void onSaveProposeLine(long workCaseId, ProposeLineView proposeLineView, ProposeType proposeType, Hashtable hashSeqCredit) {
        log.debug("onSaveProposeLine :: workCaseId :: {}, proposeType :: {}, proposeLineView :: {}",workCaseId, proposeType, proposeLineView);
        User currentUser = getCurrentUser();
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(!Util.isNull(workCase)){
            workCase.setCaseUpdateFlag(1);
            workCaseDAO.persist(workCase);
        }
        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        int specialProgramId = 0;
        int applyTCG = 0;
        TCGView tcgView = tcgInfoControl.getTCGView(workCaseId);
        if (!Util.isNull(tcgView)) {
            applyTCG = tcgView.getTCG();
        }

        if(!Util.isNull(proposeLineView)) {
            //Calculation
            proposeLineView = calculateTotalProposeAmount(proposeLineView, basicInfoView, tcgView, workCaseId);
            proposeLineView = calculateTotalForBRMS(proposeLineView);
            proposeLineView = calculateMaximumSMELimit(proposeLineView, workCaseId, workCase, basicInfoView, tcgView);

            //Remove All Fee Detail
            List<ProposeFeeDetail> proposeFeeDetailList = proposeFeeDetailDAO.findByWorkCaseId(workCaseId,proposeType);
            if(!Util.isNull(proposeFeeDetailList) && !Util.isZero(proposeFeeDetailList.size())) {
                proposeFeeDetailDAO.delete(proposeFeeDetailList);
            }

            //Remove Tier On Credit Info Detail
            if(!Util.isNull(proposeLineView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeLineView.getProposeCreditInfoDetailViewList().size())) {
                for(ProposeCreditInfoDetailView proCreditDetailView : proposeLineView.getProposeCreditInfoDetailViewList()) {
                    if(!Util.isNull(proCreditDetailView)) {
                        if(!Util.isNull(proCreditDetailView.getDeleteCreditTierIdList()) && !Util.isZero(proCreditDetailView.getDeleteCreditTierIdList().size())) {
                            for(Long deleteId : proCreditDetailView.getDeleteCreditTierIdList()) {
                                if(!Util.isZero(deleteId)) {
                                    ProposeCreditInfoTierDetail proposeCreditInfoTierDetail = proposeCreditInfoTierDetailDAO.findById(deleteId);
                                    if(!Util.isNull(proposeCreditInfoTierDetail)) {
                                        proposeCreditInfoTierDetailDAO.delete(proposeCreditInfoTierDetail);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Remove Credit Detail On Propose Line
            if(!Util.isNull(proposeLineView.getDeleteCreditIdList()) && !Util.isZero(proposeLineView.getDeleteCreditIdList().size())) {
                for(Long deleteId : proposeLineView.getDeleteCreditIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeCreditInfo proposeCreditInfo = proposeCreditInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList()) && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {
                            proposeCreditInfoTierDetailDAO.delete(proposeCreditInfo.getProposeCreditInfoTierDetailList());
                        }
                        if(!Util.isNull(proposeCreditInfo)) {
                            proposeCreditInfoDAO.delete(proposeCreditInfo);
                        }
                    }
                }
            }

            //Remove Condition Info On Propose Line
            if(!Util.isNull(proposeLineView.getDeleteConditionIdList()) && !Util.isZero(proposeLineView.getDeleteConditionIdList().size())) {
                for(Long deleteId : proposeLineView.getDeleteConditionIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeConditionInfo proposeConditionInfo = proposeConditionDAO.findById(deleteId);
                        if(!Util.isNull(proposeConditionInfo)) {
                            proposeConditionDAO.delete(proposeConditionInfo);
                        }
                    }
                }
            }
        }


        if (basicInfoView != null) {
            if (basicInfoView.getSpProgram() == RadioValue.YES.value()) {
                if(!Util.isNull(basicInfoView.getSpecialProgram()) && !Util.isZero(basicInfoView.getSpecialProgram().getId())){
                    specialProgramId = basicInfoView.getSpecialProgram().getId();
                }
            } else {
                SpecialProgram specialProgram = specialProgramDAO.findById(3);
                if(!Util.isNull(specialProgram) && !Util.isZero(specialProgram.getId())) {
                    specialProgramId = specialProgram.getId();
                }
            }
        }

        //Cal Installment
        if(!Util.isNull(proposeLineView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeLineView.getProposeCreditInfoDetailViewList().size())) {
            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
            for (ProposeCreditInfoDetailView proCreInfDetView : proposeLineView.getProposeCreditInfoDetailViewList()) {
                proCreInfDetView = onCalInstallment(proposeLineView, proCreInfDetView, specialProgramId, applyTCG);
                proposeCreditInfoDetailViewList.add(proCreInfDetView);
            }
            proposeLineView.setProposeCreditInfoDetailViewList(proposeCreditInfoDetailViewList);
        }

        //Set Use Count for Propose Credit
        if(!Util.isNull(proposeLineView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeLineView.getProposeCreditInfoDetailViewList().size())) {
            for (ProposeCreditInfoDetailView proCreInfDetView : proposeLineView.getProposeCreditInfoDetailViewList()) {
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())) {
                    int seq = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    proCreInfDetView.setUseCount(seq);
                }
            }
        }

        ProposeLine proposeLine = proposeLineTransform.transformProposeLineToModel(proposeLineView, workCase, currentUser, proposeType);

        if(!Util.isNull(proposeLine)) {
            proposeLineDAO.persist(proposeLine);
            if(!Util.isNull(proposeLine.getProposeCreditInfoList()) && !Util.isZero(proposeLine.getProposeCreditInfoList().size())) {
                proposeCreditInfoDAO.persist(proposeLine.getProposeCreditInfoList());
                for(ProposeCreditInfo proposeCreditInfo : proposeLine.getProposeCreditInfoList()) {
                    if(!Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList()) && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {
                        proposeCreditInfoTierDetailDAO.persist(proposeCreditInfo.getProposeCreditInfoTierDetailList());
                    }
                }
            }

            if(!Util.isNull(proposeLine.getProposeConditionInfoList()) && !Util.isZero(proposeLine.getProposeConditionInfoList().size())) {
                proposeConditionDAO.persist(proposeLine.getProposeConditionInfoList());
            }

            // ------------------------------------------ Guarantor ------------------------------------------- //
            //Remove All Guarantor Relation By Propose Line
            if(!Util.isNull(proposeLineView) && !Util.isZero(proposeLineView.getId())){
                List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList = proposeGuarantorInfoRelationDAO.findByProposeLine(proposeLineView.getId(), proposeType);
                if(!Util.isNull(proposeGuarantorInfoRelationList) && !Util.isZero(proposeGuarantorInfoRelationList.size())) {
                    proposeGuarantorInfoRelationDAO.delete(proposeGuarantorInfoRelationList);
                }
            }

            //Remove Guarantor Info On Propose Line
            if(!Util.isNull(proposeLineView.getDeleteGuarantorIdList()) && !Util.isZero(proposeLineView.getDeleteGuarantorIdList().size())) {
                for(Long deleteId : proposeLineView.getDeleteGuarantorIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeGuarantorInfo proposeGuarantorInfo = proposeGuarantorInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeGuarantorInfo)) {
                            proposeGuarantorInfoDAO.delete(proposeGuarantorInfo);
                        }
                    }
                }
            }

            BigDecimal sumGuaranteeAmount = BigDecimal.ZERO;
            if(!Util.isNull(proposeLineView.getProposeGuarantorInfoViewList()) && !Util.isZero(proposeLineView.getProposeGuarantorInfoViewList().size())) {
                for(ProposeGuarantorInfoView proposeGuarantorInfoView : proposeLineView.getProposeGuarantorInfoViewList()) {
                    ProposeGuarantorInfo proposeGuarantorInfo = proposeLineTransform.transformProposeGuarantorToModel(proposeLine, proposeGuarantorInfoView, currentUser, proposeType);
                    proposeGuarantorInfoDAO.persist(proposeGuarantorInfo);

                    if(!Util.isNull(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size())) {
                        for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) {
                            if(proposeCreditInfoDetailView.isNoFlag()) {
                                ExistingCreditDetail existingCreditDetail = null;
                                ProposeCreditInfo proposeCreditInfo = null;
                                if(proposeCreditInfoDetailView.isExistingCredit()) {
                                    existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditInfoDetailView.getId());
                                } else { // can't find by id coz propose credit id is zero
                                    proposeCreditInfo = proposeCreditInfoDAO.findBySeqAndPropose(proposeCreditInfoDetailView.getSeq(), proposeLine, proposeType);
                                }
                                ProposeGuarantorInfoRelation proposeGuarantorInfoRelation = proposeLineTransform.transformProposeGuarantorRelationToModel(proposeLine, proposeGuarantorInfo, proposeCreditInfo, existingCreditDetail , proposeType, currentUser, proposeCreditInfoDetailView.getGuaranteeAmount());

                                proposeGuarantorInfoRelationDAO.persist(proposeGuarantorInfoRelation);
                            }
                        }
                    }
                    sumGuaranteeAmount = Util.add(sumGuaranteeAmount, proposeGuarantorInfo.getTotalLimitGuaranteeAmount());
                }
            }

            proposeLine.setTotalGuaranteeAmount(sumGuaranteeAmount);
            proposeLineDAO.persist(proposeLine);

            //Save Propose Fee Detail
            List<ProposeFeeDetail> proposeFeeDetailList = proposeLineTransform.transformProposeFeeToModelList(proposeLine, proposeLineView.getProposeFeeDetailViewOriginalList(), proposeType);
            if(!Util.isNull(proposeFeeDetailList) && !Util.isZero(proposeFeeDetailList.size())) {
                proposeFeeDetailDAO.persist(proposeFeeDetailList);
            }

            // ------------------------------------------ Collateral ------------------------------------------- //
            //Remove All Collateral Relation By Propose Line
            if(!Util.isNull(proposeLineView) && !Util.isZero(proposeLineView.getId())){
                List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = proposeCollateralInfoRelationDAO.findByProposeLine(proposeLineView.getId(), proposeType);
                if(!Util.isNull(proposeCollateralInfoRelationList) && !Util.isZero(proposeCollateralInfoRelationList.size())) {
                    proposeCollateralInfoRelationDAO.delete(proposeCollateralInfoRelationList);
                }
            }

            //Remove All List On Sub Collateral
            List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = proposeCollateralSubOwnerDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubOwnerList) && !Util.isZero(proposeCollateralSubOwnerList.size())) {
                proposeCollateralSubOwnerDAO.delete(proposeCollateralSubOwnerList);
            }
            List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = proposeCollateralSubMortgageDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubMortgageList) && !Util.isZero(proposeCollateralSubMortgageList.size())) {
                proposeCollateralSubMortgageDAO.delete(proposeCollateralSubMortgageList);
            }
            List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = proposeCollateralSubRelatedDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubRelatedList) && !Util.isZero(proposeCollateralSubRelatedList.size())) {
                proposeCollateralSubRelatedDAO.delete(proposeCollateralSubRelatedList);
            }

            //Remove Collateral Sub
            if(!Util.isNull(proposeLineView.getProposeCollateralInfoViewList()) && !Util.isZero(proposeLineView.getProposeCollateralInfoViewList().size())) {
                for(ProposeCollateralInfoView proCollView : proposeLineView.getProposeCollateralInfoViewList()) {
                    if(!Util.isNull(proCollView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proCollView.getProposeCollateralInfoHeadViewList().size())) {
                        for(ProposeCollateralInfoHeadView proCollHeadView : proCollView.getProposeCollateralInfoHeadViewList()) {
                            if(!Util.isNull(proCollHeadView.getDeleteSubColHeadIdList()) && !Util.isZero(proCollHeadView.getDeleteSubColHeadIdList().size())) {
                                for(Long deleteId : proCollHeadView.getDeleteSubColHeadIdList()) {
                                    ProposeCollateralInfoSub proCollSub = proposeCollateralInfoSubDAO.findById(deleteId);
                                    if(!Util.isNull(proCollSub)) {
                                        proposeCollateralInfoSubDAO.delete(proCollSub);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Remove Collateral Info On Propose Line
            if(!Util.isNull(proposeLineView.getDeleteCollateralIdList()) && !Util.isZero(proposeLineView.getDeleteCollateralIdList().size())) {
                for(Long deleteId : proposeLineView.getDeleteCollateralIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeCollateralInfo proposeCollateralInfo = proposeCollateralInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeCollateralInfo)) {
                            if(!Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && !Util.isZero(proposeCollateralInfo.getProposeCollateralInfoHeadList().size())){
                                for(ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()) {
                                    if(!Util.isNull(proposeCollateralInfoHead) && !Util.isNull(proposeCollateralInfoHead.getProposeCollateralInfoSubList()) && !Util.isZero(proposeCollateralInfoHead.getProposeCollateralInfoSubList().size())) {
                                        proposeCollateralInfoSubDAO.delete(proposeCollateralInfoHead.getProposeCollateralInfoSubList());
                                    }
                                }
                                proposeCollateralInfoHeadDAO.delete(proposeCollateralInfo.getProposeCollateralInfoHeadList());
                            }
                            proposeCollateralInfoDAO.delete(proposeCollateralInfo);
                        }
                    }
                }
            }

            //Save Collateral
            if(!Util.isNull(proposeLineView.getProposeCollateralInfoViewList()) && !Util.isZero(proposeLineView.getProposeCollateralInfoViewList().size())) {
                log.debug("Propose Collateral Size ::: {}", proposeLineView.getProposeCollateralInfoViewList().size());
                for(ProposeCollateralInfoView proposeCollateralInfoView : proposeLineView.getProposeCollateralInfoViewList()) {
                    ProposeCollateralInfo proposeCollateralInfo = proposeLineTransform.transformProposeCollateralToModel(workCase, proposeLine, proposeCollateralInfoView, currentUser, proposeType);
                    proposeCollateralInfoDAO.persist(proposeCollateralInfo);
                    log.debug("Coll ID ::: {}", proposeCollateralInfoView.getId());
                    log.debug("Coll ID ::: {}", proposeCollateralInfo.getId());
                    if(!Util.isZero(proposeCollateralInfoView.getId())) {
                        proposeCollateralInfoView.setId(proposeCollateralInfo.getId());
                    }
                    if(!Util.isNull(proposeCollateralInfo) && !Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && !Util.isZero(proposeCollateralInfo.getProposeCollateralInfoHeadList().size())) {
                        for(ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()) {
                            proposeCollateralInfoHeadDAO.persist(proposeCollateralInfoHead);
                            log.debug("Coll Head ID ::: {}", proposeCollateralInfoHead.getId());
                            if(!Util.isNull(proposeCollateralInfoHead) && !Util.isNull(proposeCollateralInfoHead.getProposeCollateralInfoSubList()) && !Util.isZero(proposeCollateralInfoHead.getProposeCollateralInfoSubList().size())) {
                                for(ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoHead.getProposeCollateralInfoSubList()) {
                                    proposeCollateralInfoSubDAO.persist(proposeCollateralInfoSub);
                                    log.debug("Coll Sub ID ::: {}", proposeCollateralInfoSub.getId());
                                    log.debug("Coll Sub ( Sub ID ) ::: {}", proposeCollateralInfoSub.getSubId());
                                    if(!Util.isNull(proposeCollateralInfoSub) && !Util.isNull(proposeCollateralInfoSub.getProposeCollateralSubOwnerList()) && !Util.isZero(proposeCollateralInfoSub.getProposeCollateralSubOwnerList().size())) {
                                        proposeCollateralSubOwnerDAO.persist(proposeCollateralInfoSub.getProposeCollateralSubOwnerList());
                                    }
                                    if(!Util.isNull(proposeCollateralInfoSub) && !Util.isNull(proposeCollateralInfoSub.getProposeCollateralSubMortgageList()) && !Util.isZero(proposeCollateralInfoSub.getProposeCollateralSubMortgageList().size())) {
                                        proposeCollateralSubMortgageDAO.persist(proposeCollateralInfoSub.getProposeCollateralSubMortgageList());
                                    }
                                }
                            }
                        }
                    }

                    /*//after persist all collateral sub
                    if (!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                        for (ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                            if (!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                                for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) {
                                    ProposeCollateralInfoSub mainCollSub = proposeCollateralInfoSubDAO.findBySubId(proposeCollateralInfoSubView.getSubId());
                                    if (!Util.isNull(proposeCollateralInfoSubView.getRelatedWithList()) && !Util.isZero(proposeCollateralInfoSubView.getRelatedWithList().size())) {
                                        for (ProposeCollateralInfoSubView relatedCollSubView : proposeCollateralInfoSubView.getRelatedWithList()) {
                                            ProposeCollateralInfoSub relatedCollSub = proposeCollateralInfoSubDAO.findBySubId(relatedCollSubView.getSubId());
                                            ProposeCollateralSubRelated proposeCollateralSubRelated = proposeLineTransform.transformProposeCollateralSubRelatedToModel(workCase, mainCollSub, relatedCollSub, proposeType);
                                            proposeCollateralSubRelatedDAO.persist(proposeCollateralSubRelated);
                                        }
                                    }
                                }
                            }
                        }
                    }*/

                    if(!Util.isNull(proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size())) {
                        for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) {
                            if(proposeCreditInfoDetailView.isNoFlag()) {
                                ExistingCreditDetail existingCreditDetail = null;
                                ProposeCreditInfo proposeCreditInfo = null;
                                if(proposeCreditInfoDetailView.isExistingCredit()) {
                                    existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditInfoDetailView.getId());
                                } else { // can't find by id coz propose credit id is zero
                                    proposeCreditInfo = proposeCreditInfoDAO.findBySeqAndPropose(proposeCreditInfoDetailView.getSeq(), proposeLine, proposeType);
                                }
                                ProposeCollateralInfoRelation proposeCollateralInfoRelation = proposeLineTransform.transformProposeCollateralRelationToModel(proposeLine, proposeCollateralInfo, proposeCreditInfo, existingCreditDetail , proposeType, currentUser);

                                proposeCollateralInfoRelationDAO.persist(proposeCollateralInfoRelation);
                            }
                        }
                    }
                }

                for(ProposeCollateralInfoView proposeCollateralInfoView : proposeLineView.getProposeCollateralInfoViewList()) {
                    //after persist all collateral sub
                    if (!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                        for (ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                            if (!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                                for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) {
                                    ProposeCollateralInfoSub mainCollSub = proposeCollateralInfoSubDAO.findBySubId(proposeCollateralInfoSubView.getSubId());
                                    log.debug("Main Coll Sub :: {}", mainCollSub);
                                    log.debug("Main Coll Sub ( Sub ID ) :: {}", mainCollSub != null ? mainCollSub.getSubId() : "NULL");
                                    if (!Util.isNull(proposeCollateralInfoSubView.getRelatedWithList()) && !Util.isZero(proposeCollateralInfoSubView.getRelatedWithList().size())) {
                                        for (ProposeCollateralInfoSubView relatedCollSubView : proposeCollateralInfoSubView.getRelatedWithList()) {
                                            ProposeCollateralInfoSub relatedCollSub = proposeCollateralInfoSubDAO.findBySubId(relatedCollSubView.getSubId());
                                            log.debug("Related Coll Sub :: {}", relatedCollSub);
                                            log.debug("Related Coll Sub ( Sub ID ) :: {}", relatedCollSub != null ? relatedCollSub.getSubId() : "NULL");
                                            ProposeCollateralSubRelated proposeCollateralSubRelated = proposeLineTransform.transformProposeCollateralSubRelatedToModel(workCase, mainCollSub, relatedCollSub, proposeType);
                                            log.debug("Propose Coll Sub Related :::: {}", proposeCollateralSubRelated);
                                            proposeCollateralSubRelatedDAO.persist(proposeCollateralSubRelated);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ProposeLineView calculateTotalProposeAmount(ProposeLineView proposeLineView, BasicInfoView basicInfoView, TCGView tcgView, long workCaseId) {
        if (!Util.isNull(proposeLineView)) {
            BigDecimal sumTotalOBOD = BigDecimal.ZERO;         // OBOD of Propose
            BigDecimal sumTotalCommercial = BigDecimal.ZERO;   // Commercial of Propose
            BigDecimal sumTotalPropose = BigDecimal.ZERO;      // All Propose
            BigDecimal sumTotalBorrowerCommercial = BigDecimal.ZERO;   // Without : OBOD  Propose and Existing
            BigDecimal sumTotalBorrowerCommercialAndOBOD = BigDecimal.ZERO;  //All Propose and Existing
            BigDecimal sumTotalGroupExposure = BigDecimal.ZERO;
            BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;
            BigDecimal borrowerComOBOD = BigDecimal.ZERO;
            BigDecimal borrowerCom = BigDecimal.ZERO;
            BigDecimal groupExposure = BigDecimal.ZERO;

            ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);

            if (existingCreditFacilityView != null) {
                borrowerComOBOD = existingCreditFacilityView.getTotalBorrowerComOBOD();
                borrowerCom = existingCreditFacilityView.getTotalBorrowerCom();
                groupExposure = existingCreditFacilityView.getTotalBorrowerExposure();
            }

            if(!Util.isNull(basicInfoView) && !Util.isNull(basicInfoView.getSpecialProgram()) && !Util.isNull(tcgView)) {
                List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = proposeLineView.getProposeCreditInfoDetailViewList();
                if (!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
                    ProductProgram productProgram;
                    CreditType creditType;
                    PrdProgramToCreditType prdProgramToCreditType;
                    ProductFormula productFormula;

                    for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                        if (!Util.isNull(proposeCreditInfoDetailView.getProductProgramView()) && !Util.isZero(proposeCreditInfoDetailView.getProductProgramView().getId()) &&
                            !Util.isNull(proposeCreditInfoDetailView.getCreditTypeView()) && !Util.isZero(proposeCreditInfoDetailView.getCreditTypeView().getId())) {
                            productProgram = productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId());
                            creditType = creditTypeDAO.findById(proposeCreditInfoDetailView.getCreditTypeView().getId());

                            if (productProgram != null && creditType != null) {
                                prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);
                                productFormula = productFormulaDAO.findProductFormulaPropose(
                                        prdProgramToCreditType, proposeLineView.getCreditCustomerType().value(), basicInfoView.getSpecialProgram(), tcgView.getTCG());
                                if (productFormula != null) {
                                    if (CreditTypeGroup.CASH_IN.value() == (productFormula.getProgramToCreditType().getCreditType().getCreditGroup())) { //OBOD or CASH_IN
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            sumTotalOBOD = sumTotalOBOD.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            sumTotalOBOD = sumTotalOBOD.add(proposeCreditInfoDetailView.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) { //(limit * %PCE)/100
                                            sumTotalOBOD = sumTotalOBOD.add(proposeCreditInfoDetailView.getPCEAmount());
                                        }
                                    } else {
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            sumTotalCommercial = sumTotalCommercial.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            sumTotalCommercial = sumTotalCommercial.add(proposeCreditInfoDetailView.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //(limit * %PCE)/100
                                            sumTotalCommercial = sumTotalCommercial.add(proposeCreditInfoDetailView.getPCEAmount());
                                        }
                                    }

                                    sumTotalPropose = Util.add(sumTotalCommercial, sumTotalOBOD);// Commercial + OBOD  All Credit

                                    //For DBR  sumTotalLoanDbr and sumTotalNonLoanDbr
                                    if (productFormula.getDbrCalculate() == 1) {// No
                                        sumTotalNonLoanDbr = BigDecimal.ZERO;
                                    } else if (productFormula.getDbrCalculate() == 2) {// Yes
                                        if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(proposeCreditInfoDetailView.getInstallment());
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYear(proposeCreditInfoDetailView, productFormula.getDbrSpread()));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    sumTotalBorrowerCommercialAndOBOD = Util.add(borrowerComOBOD, sumTotalPropose); // Total Commercial&OBOD  ของ Borrower (จาก Existing credit) +Total Propose Credit
                    sumTotalBorrowerCommercial = Util.add(borrowerCom, sumTotalCommercial); //Total Commercial  ของ Borrower (จาก Existing credit) + *Commercial ของ propose
                    sumTotalGroupExposure = Util.add(groupExposure, sumTotalBorrowerCommercialAndOBOD); //ได้มาจาก  Total Exposure ของ Group  (จาก Existing credit) +  Total Borrower Commercial&OBOD (Propose credit)
                }

            }

            proposeLineView.setTotalPropose(sumTotalPropose);                 //sumTotalPropose All Credit in this case
            proposeLineView.setTotalProposeLoanDBR(sumTotalLoanDbr);          //sumTotalLoanDbr
            proposeLineView.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr);    //sumTotalNonLoanDbr
            proposeLineView.setTotalCommercial(sumTotalBorrowerCommercial);               //sum Commercial of Existing and Propose
            proposeLineView.setTotalCommercialAndOBOD(sumTotalBorrowerCommercialAndOBOD); //sum Commercial and OBOD of Existing and Propose
            proposeLineView.setTotalExposure(sumTotalGroupExposure);
        }

        return proposeLineView;
    }

    public BigDecimal calTotalProposeLoanDBRForIntYear(ProposeCreditInfoDetailView proposeCreditInfoDetailView, BigDecimal dbrSpread) {
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        if (!Util.isNull(proposeCreditInfoDetailView)
                && !Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList())
                && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {

            BigDecimal oneHundred = BigDecimal.valueOf(100);
            BigDecimal twelve = BigDecimal.valueOf(12);
            BigDecimal sum = BigDecimal.ZERO;

            for (ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                if (!Util.isNull(proposeCreditInfoTierDetailView)) {
                    sum = Util.divide(Util.multiply(Util.divide(Util.add(Util.add(proposeCreditInfoTierDetailView.getFinalBasePrice().getValue(), proposeCreditInfoTierDetailView.getFinalInterest()), dbrSpread), oneHundred), proposeCreditInfoDetailView.getLimit()), twelve);
                    sumTotalLoanDbr = Util.add(sumTotalLoanDbr, sum);
                }
            }
        }
        return sumTotalLoanDbr;
    }

    public void calculateTotalProposeAmountForExisting(ExistingCreditFacilityView existingCreditFacilityView, long workCaseId, User user) {
        ProposeLine proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        if (!Util.isNull(proposeLine)) {
            BigDecimal sumTotalOBOD = BigDecimal.ZERO;         // OBOD of Propose
            BigDecimal sumTotalCommercial = BigDecimal.ZERO;   // Commercial of Propose
            BigDecimal sumTotalPropose = BigDecimal.ZERO;      // All Propose
            BigDecimal sumTotalBorrowerCommercial = BigDecimal.ZERO;   // Without : OBOD  Propose and Existing
            BigDecimal sumTotalBorrowerCommercialAndOBOD = BigDecimal.ZERO;  //All Propose and Existing
            BigDecimal sumTotalGroupExposure = BigDecimal.ZERO;
            BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
            BigDecimal sumTotalNonLoanDbr = BigDecimal.ZERO;
            BigDecimal borrowerComOBOD = BigDecimal.ZERO;
            BigDecimal borrowerCom = BigDecimal.ZERO;
            BigDecimal groupExposure = BigDecimal.ZERO;

            if (existingCreditFacilityView != null) {
                borrowerComOBOD = existingCreditFacilityView.getTotalBorrowerComOBOD();
                borrowerCom = existingCreditFacilityView.getTotalBorrowerCom();
                groupExposure = existingCreditFacilityView.getTotalBorrowerExposure();
            }

            BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
            TCGView tcgView = tcgInfoControl.getTCGView(workCaseId);

            if(!Util.isNull(basicInfoView) && !Util.isNull(basicInfoView.getSpecialProgram()) && !Util.isNull(tcgView)) {
                List<ProposeCreditInfo> proposeCreditInfoList = proposeLine.getProposeCreditInfoList();
                if (!Util.isNull(proposeCreditInfoList) && !Util.isZero(proposeCreditInfoList.size())) {
                    ProductProgram productProgram;
                    CreditType creditType;
                    PrdProgramToCreditType prdProgramToCreditType;
                    ProductFormula productFormula;

                    for (ProposeCreditInfo proposeCreditInfo : proposeCreditInfoList) {
                        if (!Util.isNull(proposeCreditInfo.getProductProgram()) && !Util.isZero(proposeCreditInfo.getProductProgram().getId()) &&
                                !Util.isNull(proposeCreditInfo.getCreditType()) && !Util.isZero(proposeCreditInfo.getCreditType().getId())) {
                            productProgram = proposeCreditInfo.getProductProgram();
                            creditType = proposeCreditInfo.getCreditType();

                            if (productProgram != null && creditType != null) {
                                prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);
                                productFormula = productFormulaDAO.findProductFormulaPropose(
                                        prdProgramToCreditType, proposeLine.getCreditCustomerType(), basicInfoView.getSpecialProgram(), tcgView.getTCG());
                                if (productFormula != null) {
                                    if (CreditTypeGroup.CASH_IN.value() == (productFormula.getProgramToCreditType().getCreditType().getCreditGroup())) { //OBOD or CASH_IN
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            sumTotalOBOD = sumTotalOBOD.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            sumTotalOBOD = sumTotalOBOD.add(proposeCreditInfo.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) { //(limit * %PCE)/100
                                            sumTotalOBOD = sumTotalOBOD.add(proposeCreditInfo.getPceAmount());
                                        }
                                    } else {
                                        //ExposureMethod for check to use limit or limit*PCE%
                                        if (productFormula.getExposureMethod() == ExposureMethod.NOT_CALCULATE.value()) { //ไม่คำนวณ
                                            sumTotalCommercial = sumTotalCommercial.add(BigDecimal.ZERO);
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.LIMIT.value()) { //limit
                                            sumTotalCommercial = sumTotalCommercial.add(proposeCreditInfo.getLimit());
                                        } else if (productFormula.getExposureMethod() == ExposureMethod.PCE_LIMIT.value()) {    //(limit * %PCE)/100
                                            sumTotalCommercial = sumTotalCommercial.add(proposeCreditInfo.getPceAmount());
                                        }
                                    }

                                    sumTotalPropose = Util.add(sumTotalCommercial, sumTotalOBOD);// Commercial + OBOD  All Credit

                                    //For DBR  sumTotalLoanDbr and sumTotalNonLoanDbr
                                    if (productFormula.getDbrCalculate() == 1) {// No
                                        sumTotalNonLoanDbr = BigDecimal.ZERO;
                                    } else if (productFormula.getDbrCalculate() == 2) {// Yes
                                        if (productFormula.getDbrMethod() == DBRMethod.NOT_CALCULATE.value()) {// not calculate
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(BigDecimal.ZERO);
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INSTALLMENT.value()) { //Installment
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(proposeCreditInfo.getInstallment());
                                        } else if (productFormula.getDbrMethod() == DBRMethod.INT_YEAR.value()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                                            sumTotalLoanDbr = sumTotalLoanDbr.add(calTotalProposeLoanDBRForIntYearForExisting(proposeCreditInfo, productFormula.getDbrSpread()));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    sumTotalBorrowerCommercialAndOBOD = Util.add(borrowerComOBOD, sumTotalPropose); // Total Commercial&OBOD  ของ Borrower (จาก Existing credit) +Total Propose Credit
                    sumTotalBorrowerCommercial = Util.add(borrowerCom, sumTotalCommercial); //Total Commercial  ของ Borrower (จาก Existing credit) + *Commercial ของ propose
                    sumTotalGroupExposure = Util.add(groupExposure, sumTotalBorrowerCommercialAndOBOD); //ได้มาจาก  Total Exposure ของ Group  (จาก Existing credit) +  Total Borrower Commercial&OBOD (Propose credit)
                }

            }

            proposeLine.setTotalPropose(sumTotalPropose);                 //sumTotalPropose All Credit in this case
            proposeLine.setTotalProposeLoanDBR(sumTotalLoanDbr);          //sumTotalLoanDbr
            proposeLine.setTotalProposeNonLoanDBR(sumTotalNonLoanDbr);    //sumTotalNonLoanDbr
            proposeLine.setTotalCommercial(sumTotalBorrowerCommercial);               //sum Commercial of Existing and Propose
            proposeLine.setTotalCommercialAndOBOD(sumTotalBorrowerCommercialAndOBOD); //sum Commercial and OBOD of Existing and Propose
            proposeLine.setTotalExposure(sumTotalGroupExposure);
        }

        proposeLineDAO.persist(proposeLine);
    }

    public BigDecimal calTotalProposeLoanDBRForIntYearForExisting(ProposeCreditInfo proposeCreditInfo, BigDecimal dbrSpread) {
        BigDecimal sumTotalLoanDbr = BigDecimal.ZERO;
        if (!Util.isNull(proposeCreditInfo)
                && !Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList())
                && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {

            BigDecimal oneHundred = BigDecimal.valueOf(100);
            BigDecimal twelve = BigDecimal.valueOf(12);
            BigDecimal sum;

            for (ProposeCreditInfoTierDetail proposeCreditInfoTierDetail : proposeCreditInfo.getProposeCreditInfoTierDetailList()) { //(Limit*((อัตราดอกเบี้ย+ Spread)/100))/12
                if (!Util.isNull(proposeCreditInfoTierDetail)) {
                    sum = Util.divide(Util.multiply(Util.divide(Util.add(Util.add(proposeCreditInfoTierDetail.getFinalBasePrice().getValue(), proposeCreditInfoTierDetail.getFinalInterest()), dbrSpread), oneHundred), proposeCreditInfo.getLimit()), twelve);
                    sumTotalLoanDbr = Util.add(sumTotalLoanDbr, sum);
                }
            }
        }
        return sumTotalLoanDbr;
    }

    public ProposeLineView calculateTotalForBRMS(ProposeLineView proposeLineView) {
        if (!Util.isNull(proposeLineView)) {
            // ***** Credit Detail ***** //
            BigDecimal totalNumOfNewOD = BigDecimal.ZERO;
            BigDecimal totalNumProposeCreditFac = BigDecimal.ZERO;
            BigDecimal totalNumContingentPropose = BigDecimal.ZERO;

            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = proposeLineView.getProposeCreditInfoDetailViewList();
            if (!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())) {
                for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCreditInfoDetailViewList) {
                    // Count All 'New' propose credit
                    totalNumProposeCreditFac = Util.add(totalNumProposeCreditFac, BigDecimal.ONE);

                    CreditTypeView creditTypeView = proposeCreditInfoDetailView.getCreditTypeView();
                    if (creditTypeView != null) {
                        // Count propose credit which credit facility = 'OD'
                        if (CreditTypeGroup.OD.value() == creditTypeView.getCreditGroup()) {
                            totalNumOfNewOD = Util.add(totalNumOfNewOD, BigDecimal.ONE);
                        }
                        // Count the 'New' propose credit which has Contingent Flag 'Y'
                        if (creditTypeView.isContingentFlag()) {
                            totalNumContingentPropose = Util.add(totalNumContingentPropose, BigDecimal.ONE);
                        }
                    }
                }
            }

            // ***** Collateral ***** //
            BigDecimal totalNumOfCoreAsset = BigDecimal.ZERO;
            BigDecimal totalNumOfNonCoreAsset = BigDecimal.ZERO;
            BigDecimal totalMortgageValue = BigDecimal.ZERO;

            List<ProposeCollateralInfoView> proposeCollateralInfoViewList = proposeLineView.getProposeCollateralInfoViewList();
            if (!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
                for (ProposeCollateralInfoView proposeCollateralInfoView : proposeCollateralInfoViewList) {
                    List<ProposeCollateralInfoHeadView> collHeadViewList = proposeCollateralInfoView.getProposeCollateralInfoHeadViewList();
                    if (collHeadViewList != null && collHeadViewList.size() > 0) {
                        for (ProposeCollateralInfoHeadView collHeadView : collHeadViewList) {
                            PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();
                            // Count core asset and none core asset
                            if(!Util.isNull(potentialCollateral)){
                                if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                    totalNumOfCoreAsset = Util.add(totalNumOfCoreAsset, BigDecimal.ONE);
                                } else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
                                    totalNumOfNonCoreAsset = Util.add(totalNumOfNonCoreAsset, BigDecimal.ONE);
                                }
                            }

                            List<ProposeCollateralInfoSubView> collSubViewList = collHeadView.getProposeCollateralInfoSubViewList();
                            if (!Util.isNull(collSubViewList) && !Util.isZero(collSubViewList.size())) {
                                for (ProposeCollateralInfoSubView collSubView : collSubViewList) {
                                    totalMortgageValue = Util.add(totalMortgageValue, collSubView.getMortgageValue());
                                }
                            }
                        }
                    }
                }
            }

            // ***** Guarantor Detail ***** //
            BigDecimal totalTCGGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalIndiGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalJuriGuaranteeAmt = BigDecimal.ZERO;

            List<ProposeGuarantorInfoView> proposeGuarantorInfoViewList = proposeLineView.getProposeGuarantorInfoViewList();
            if (!Util.isNull(proposeGuarantorInfoViewList) && !Util.isZero(proposeGuarantorInfoViewList.size())) {
                for (ProposeGuarantorInfoView guarantorDetailView : proposeGuarantorInfoViewList) {
                    CustomerInfoView customerInfoView = guarantorDetailView.getGuarantorName();
                    if (!Util.isNull(customerInfoView)) {
                        CustomerEntity customerEntity = customerInfoView.getCustomerEntity();
                        if (!Util.isNull(customerEntity)) {
                            if (GuarantorCategory.INDIVIDUAL.value() == customerEntity.getId()) {
                                totalIndiGuaranteeAmt = Util.add(totalIndiGuaranteeAmt, guarantorDetailView.getGuaranteeAmount());
                            } else if (GuarantorCategory.JURISTIC.value() == customerEntity.getId()) {
                                totalJuriGuaranteeAmt = Util.add(totalJuriGuaranteeAmt, guarantorDetailView.getGuaranteeAmount());
                            } else if (GuarantorCategory.TCG.value() == customerEntity.getId()) {
                                totalTCGGuaranteeAmt = Util.add(totalTCGGuaranteeAmt, guarantorDetailView.getGuaranteeAmount());
                            }
                        }
                    }
                }
            }

            // set to credit facility propose
            proposeLineView.setTotalNumberOfNewOD(totalNumOfNewOD);
            proposeLineView.setTotalNumberProposeCreditFac(totalNumProposeCreditFac);
            proposeLineView.setTotalNumberContingenPropose(totalNumContingentPropose);
            proposeLineView.setTotalNumberOfCoreAsset(totalNumOfCoreAsset);
            proposeLineView.setTotalNumberOfNonCoreAsset(totalNumOfNonCoreAsset);
            proposeLineView.setTotalMortgageValue(totalMortgageValue);
            proposeLineView.setTotalTCGGuaranteeAmount(totalTCGGuaranteeAmt);
            proposeLineView.setTotalIndvGuaranteeAmount(totalIndiGuaranteeAmt);
            proposeLineView.setTotalJurisGuaranteeAmount(totalJuriGuaranteeAmt);

        }
        return proposeLineView;
    }

    public ProposeLineView calculateMaximumSMELimit(ProposeLineView proposeLineView, long workCaseId, WorkCase workCase, BasicInfoView basicInfoView, TCGView tcgView) {
        BigDecimal maximumSMELimit = BigDecimal.ZERO;
        if (!Util.isNull(proposeLineView)) {
            // ***** Collateral ***** //
            BigDecimal thirtyPercent = BigDecimal.valueOf(0.30);
            BigDecimal fiftyPercent = BigDecimal.valueOf(0.50);

            BigDecimal num1 = BigDecimal.valueOf(10000000);  //10,000,000
            BigDecimal num2 = BigDecimal.valueOf(23000000);  //23,000,000
            BigDecimal num3 = BigDecimal.valueOf(3000000);   //3,000,000
            BigDecimal num4 = BigDecimal.valueOf(20000000); //20,000,000

            BigDecimal summaryOne = BigDecimal.ZERO;
            BigDecimal summaryTwo;
            BigDecimal potentialCollValue = BigDecimal.ZERO;

            BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);

            if (!Util.isNull(workCase) && !Util.isNull(workCase.getProductGroup()) && !Util.isNull(bankStatementSummary) && !Util.isNull(tcgView) && !Util.isNull(basicInfoView)) {
                ProductGroupView productGroupView = productTransform.transformToView(workCase.getProductGroup());
                //********************************************** TCG is YES****************************************//
                if (tcgView.getTCG() == RadioValue.YES.value()) {
                    //********** ProductGroup เป็น TMB_SME_SMART_BIZ หรือ RETENTION*********//
                    if ((ProductGroupValue.TMB_SME_SMART_BIZ.id() == productGroupView.getId()) || (ProductGroupValue.RETENTION.id() == productGroupView.getId())) {
                        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = proposeLineView.getProposeCollateralInfoViewList();
                        if (!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
                            for (ProposeCollateralInfoView collateralView : proposeCollateralInfoViewList) {
                                List<ProposeCollateralInfoHeadView> collHeadViewList = collateralView.getProposeCollateralInfoHeadViewList();
                                if (collHeadViewList != null && collHeadViewList.size() > 0) {
                                    for (ProposeCollateralInfoHeadView collHeadView : collHeadViewList) {
                                        PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();
                                         /*
                                          Sum of [(HeadCollateral-Appraisal of coreAsset/30%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())] +
                                          Sum of [(HeadCollateral-Appraisal of nonCoreAsset/50%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())] +
                                          Sum of [(HeadCollateral-Appraisal of cashCollateral/BE(คือฟิลล์ไหน ???)/30%)-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())]
                                         */
                                        if (potentialCollateral.getId() != 0) {
                                            if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                                potentialCollValue = Util.subtract((Util.divide(collHeadView.getAppraisalValue(), thirtyPercent)), collHeadView.getExistingCredit());
                                            } else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
                                                potentialCollValue = Util.subtract((Util.divide(collHeadView.getAppraisalValue(), fiftyPercent)), collHeadView.getExistingCredit());
                                            } else if (PotentialCollateralValue.CASH_COLLATERAL.id() == potentialCollateral.getId()) {
                                                potentialCollValue = Util.subtract((Util.divide(collHeadView.getAppraisalValue(), thirtyPercent)), collHeadView.getExistingCredit());
                                            }
                                        }
                                        summaryOne = summaryOne.add(potentialCollValue);
                                    }
                                }
                            }
                        }

                        summaryTwo = calSum2ForCompareSum1(proposeLineView, workCaseId);
                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }
                        //********** ProductGroup เป็น F_CASH *********//
                    } else if (ProductGroupValue.F_CASH.id() == productGroupView.getId()) {
                        //Sum of [(HeadCollateral-Appraisal of cashCollateral/BE(คือฟิลล์ไหน ???))-ภาระสินเชื่อเดิม(collHeadView.getExistingCredit())]
                        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = proposeLineView.getProposeCollateralInfoViewList();
                        if (!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
                            for (ProposeCollateralInfoView collateralView : proposeCollateralInfoViewList) {
                                List<ProposeCollateralInfoHeadView> collHeadViewList = collateralView.getProposeCollateralInfoHeadViewList();
                                if (!Util.isNull(collHeadViewList) && !Util.isZero(collHeadViewList.size())) {
                                    for (ProposeCollateralInfoHeadView collHeadView : collHeadViewList) {
                                        PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();
                                        if (!Util.isNull(potentialCollateral) && !Util.isZero(potentialCollateral.getId())) {
                                            if (PotentialCollateralValue.CASH_COLLATERAL.id() == potentialCollateral.getId()) {
                                                potentialCollValue = Util.subtract(collHeadView.getAppraisalValue(), collHeadView.getExistingCredit());
                                            }
                                        }
                                        summaryOne = summaryOne.add(potentialCollValue);   // Sum of [Head Coll - Appraisal of Cash Collateral / BE - ภาระสินเชื่อเดิม]
                                    }
                                }
                            }
                        }

                        //20,000,000 - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่ม/กิจการในเครือ)
                        summaryTwo = Util.subtract(num4, proposeLineView.getExistingSMELimit());
                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if(summaryOne.compareTo(summaryTwo) < 0){
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    } else if (ProductGroupValue.OD_NO_ASSET.id() == productGroupView.getId()) {//********** ProductGroup เป็น OD_NO_ASSET *********//
                        maximumSMELimit = Util.subtract(num1, proposeLineView.getExistingSMELimit()); // 10 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                    } else if (ProductGroupValue.QUICK_LOAN.id() == productGroupView.getId()) {   //********** ProductGroup เป็น QUICK_LOAN *********//
                        summaryOne = num3;  // 3 ล้าน

                        if (proposeLineView.getExistingSMELimit().compareTo(num4) < 0) {  // if วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ) น้อยกว่า 20 ล้าน
                            summaryTwo = Util.subtract(num2, proposeLineView.getExistingSMELimit()); // 23 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                        } else {
                            summaryTwo = BigDecimal.ZERO;
                        }

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.compareTo(summaryTwo) < 0) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    }
                    //********************************************** TCG is NO ****************************************//
                } else if (tcgView.getTCG() == RadioValue.NO.value()) {
                    //********** ProductGroup เป็น TMB_SME_SMART_BIZ หรือ RETENTION หรือ F_CASH*********//
                    if ((ProductGroupValue.TMB_SME_SMART_BIZ.id() == productGroupView.getId()) ||
                            (ProductGroupValue.RETENTION.id() == productGroupView.getId()) ||
                            (ProductGroupValue.F_CASH.id() == productGroupView.getId())) {

                        // sum of[(ราคาประเมิน (collHeadView.getAppraisalValue())*LTVPercent) - ภาระสินเชื่อเดิม]
                        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = proposeLineView.getProposeCollateralInfoViewList();
                        if (!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
                            BigDecimal ltvValue;
                            for (ProposeCollateralInfoView collateralView : proposeCollateralInfoViewList) {
                                List<ProposeCollateralInfoHeadView> collHeadViewList = collateralView.getProposeCollateralInfoHeadViewList();
                                if (!Util.isNull(collHeadViewList) && !Util.isZero(collHeadViewList.size())) {
                                    BigDecimal percentLTV;
                                    for (ProposeCollateralInfoHeadView collHeadView : collHeadViewList) {
                                        percentLTV = findLTVPercent(collHeadView, workCaseId);
                                        ltvValue = Util.multiply(collHeadView.getAppraisalValue(), percentLTV);
                                        summaryOne = Util.add(summaryOne, (Util.subtract(ltvValue, collHeadView.getExistingCredit())));
                                    }
                                }
                            }
                        }

                        summaryTwo = calSum2ForCompareSum1(proposeLineView, workCaseId);

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }

                    } else if (ProductGroupValue.OD_NO_ASSET.id() == productGroupView.getId()) {   //********** ProductGroup เป็น OD_NO_ASSET *********//
                        maximumSMELimit = Util.subtract(num1, proposeLineView.getExistingSMELimit()); // 10 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                    } else if (ProductGroupValue.QUICK_LOAN.id() == productGroupView.getId()) {   //********** ProductGroup เป็น QUICK_LOAN *********//
                        summaryOne = num3;  // 3 ล้าน

                        if (proposeLineView.getExistingSMELimit().compareTo(num4) < 0) {  // if วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ) น้อยกว่า 20 ล้าน
                            summaryTwo = Util.subtract(num2, proposeLineView.getExistingSMELimit()); // 23 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวกลุ่ม/กิจการในเครือ)
                        } else {
                            summaryTwo = BigDecimal.ZERO;
                        }

                        //เอาผลลัพธ์ที่น้อยกว่าเสมอ
                        if (summaryOne.doubleValue() < summaryTwo.doubleValue()) {
                            maximumSMELimit = summaryOne;
                        } else {
                            maximumSMELimit = summaryTwo;
                        }
                    }
                }
            }
        }

        if (maximumSMELimit.compareTo(BigDecimal.ZERO) < 0) {
            maximumSMELimit = BigDecimal.ZERO;
        }

        proposeLineView.setMaximumSMELimit(maximumSMELimit);

        return proposeLineView;
    }

    public BigDecimal calSum2ForCompareSum1(ProposeLineView proposeLineView, long workCaseId) {
        BigDecimal num1 = BigDecimal.valueOf(20000000);      //20,000,000
        BigDecimal num2 = BigDecimal.valueOf(35000000);      //35,000,000
        BigDecimal numBank = BigDecimal.valueOf(100000000);  //100,000,000
        BigDecimal sumBank = BigDecimal.ZERO;
        BigDecimal summary = BigDecimal.ZERO;
        boolean flag_for_core_asset = false;
        /*
        1. Customer Type = Individual
        2. มี Core Asset ใน Proposed หรือ Approved Collateral
        3. SCF <= 13
        4. [Sum of (Income Gross_TMB Bank Statement Summary)+Sum of (Income Gross_Other Bank Statement Summary)] x 12 >= 100,000,000
        5. ใช้สินเชื่อทางตรงกับ TMB อย่างน้อย 1 ปี (ในหน้า Basic Info) = Yes
        */
        BankStatementSummary bankStatementSummary = bankStatementSummaryDAO.findByWorkCaseId(workCaseId);
        if (!Util.isNull(bankStatementSummary)) {
            sumBank = Util.multiply(Util.add(bankStatementSummary.getTMBTotalIncomeGross(), bankStatementSummary.getOthTotalIncomeGross()), BigDecimal.valueOf(12));
        }

        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = proposeLineView.getProposeCollateralInfoViewList();
        if (!Util.isNull(proposeCollateralInfoViewList) && !Util.isZero(proposeCollateralInfoViewList.size())) {
            for (ProposeCollateralInfoView collateralView : proposeCollateralInfoViewList) {
                if(!Util.isNull(collateralView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(collateralView.getProposeCollateralInfoHeadViewList().size())){
                    for (ProposeCollateralInfoHeadView collHeadView : collateralView.getProposeCollateralInfoHeadViewList()) {
                        if(collHeadView.getPotentialCollateral() != null){
                            PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();
                            if (potentialCollateral.getId() != 0) {
                                if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                    flag_for_core_asset = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if (!Util.isNull(basicInfo)) {
            if (((!Util.isNull(basicInfo.getBorrowerType())) && (basicInfo.getBorrowerType().getId() == BorrowerType.INDIVIDUAL.value())) &&
                    ((!Util.isNull(basicInfo.getSbfScore())) && (basicInfo.getSbfScore().getScore() <= 13)) &&
                    ((!Util.isNull(basicInfo.getSbfScore())) && (basicInfo.getHaveLoanInOneYear() == RadioValue.YES.value())) &&
                    (sumBank.doubleValue() >= numBank.doubleValue()) &&
                    (flag_for_core_asset))
            {
                summary = Util.subtract(num2, proposeLineView.getExistingSMELimit());   //35 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่มกิจการในเครื่อ)
            } else {
                summary = Util.subtract(num1, proposeLineView.getExistingSMELimit());   //20 ล้าน - วงเงิน/ภาระสินเชื่อ SME เดิม (รวมกลุ่มกิจการในเครื่อ)
            }
        }
        return summary;
    }

    public BigDecimal findLTVPercent(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView, long workCaseId) {
        BigDecimal ltvPercentBig = BigDecimal.ZERO;
        if(!Util.isNull(proposeCollateralInfoHeadView)){
            if(!Util.isNull(proposeCollateralInfoHeadView.getPotentialCollateral()) && !Util.isZero(proposeCollateralInfoHeadView.getPotentialCollateral().getId())
                    && !Util.isNull(proposeCollateralInfoHeadView.getTcgCollateralType()) && !Util.isZero(proposeCollateralInfoHeadView.getTcgCollateralType().getId())){
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(proposeCollateralInfoHeadView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(proposeCollateralInfoHeadView.getTcgCollateralType().getId());
                if (!Util.isNull(potentialCollateral) && !Util.isNull(tcgCollateralType)) {
                    PotentialColToTCGCol potentialColToTCGCol = potentialColToTCGColDAO.getPotentialColToTCGCol(potentialCollateral, tcgCollateralType);
                    if (potentialColToTCGCol != null) {
                        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
                        WorkCase workCase = workCaseDAO.findById(workCaseId);
                        if (basicInfo != null && workCase != null) {
                            if (workCase.getProductGroup() != null && Util.isTrue(workCase.getProductGroup().getSpecialLTV())) {
                                if (potentialColToTCGCol.getRetentionLTV() != null) {
                                    ltvPercentBig = potentialColToTCGCol.getRetentionLTV();
                                } else {
                                    if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                            Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                            Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                            Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                            (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
                                        ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                                    } else {
                                        ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                                    }
                                }
                            } else if (Util.isRadioTrue(basicInfo.getExistingSMECustomer()) &&
                                    Util.isRadioTrue(basicInfo.getPassAnnualReview()) &&
                                    Util.isRadioTrue(basicInfo.getRequestLoanWithSameName()) &&
                                    Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()) &&
                                    (basicInfo.getSbfScore() != null && basicInfo.getSbfScore().getScore() <= 15)) {
                                ltvPercentBig = potentialColToTCGCol.getTenPercentLTV();
                            } else {
                                ltvPercentBig = potentialColToTCGCol.getPercentLTV();
                            }
                        }
                    }
                }
            }
        }
        return ltvPercentBig;
    }
}