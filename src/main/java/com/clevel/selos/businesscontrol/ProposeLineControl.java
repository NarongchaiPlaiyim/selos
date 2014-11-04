package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.master.BaseRateControl;
import com.clevel.selos.businesscontrol.master.ProductControl;
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
    private BaseRateControl baseRateControl;

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
            BaseRateView baseRateView = baseRateControl.getBaseRate(BaseRateConfig.MLR);
            creditTierDetailAdd.setFinalBasePrice(baseRateView);
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
                    List<FeeDetailView> appFeeDetailView = new ArrayList<FeeDetailView>();
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
                                if(!Util.isNull(proposeCreditInfoDetailView)) {
                                    proposeFeeDetailView.setProposeCreditInfoDetailView(proposeCreditInfoDetailView);
                                    ProductProgram productProgram = productProgramDAO.findById(proposeCreditInfoDetailView.getProductProgramView().getId());
                                    if (!Util.isNull(productProgram)) {
                                        proposeFeeDetailView.setProductProgram(productProgram.getName());
                                    }
                                    if ("9".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=9,(Front-End-Fee)
                                        proposeFeeDetailView.setStandardFrontEndFee(feeDetailView);
                                        proposeCreditInfoDetailView.setFrontEndFeeOriginal(feeDetailView.getPercentFee());
                                        proposeCreditInfoDetailView.setFrontEndFee(feeDetailView.getPercentFee());
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
                        } else if (feeDetailView.getFeeLevel() == FeeLevel.APP_LEVEL) {
                            appFeeDetailView.add(feeDetailView);
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
                    proposeLineView.setProposeAppFeeDetailViewList(appFeeDetailView);

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
                        for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()){
                            if(pricingFee.getType().equals("9")) {
                                String creditTypeId = pricingFee.getCreditDetailId();
                                for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeLineView.getProposeCreditInfoDetailViewList()) {
                                    if (creditTypeId.equals(proposeCreditInfoDetailView.getId() + "")) {
                                        proposeCreditInfoDetailView.setFrontEndFee(pricingFee.getFeePercent());
                                        proposeCreditInfoDetailView.setFrontEndFeeOriginal(pricingFee.getFeePercent());
                                        break;
                                    }
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
                    List<FeeDetailView> appFeeDetailViewList = new ArrayList<FeeDetailView>();
                    for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()) {
                        FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);
                        if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                            log.debug("feeLevel : CREDIT_LEVEL : feeDetailView : {}", feeDetailView);
                            if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                                proposeFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                            } else {
                                proposeFeeDetailView = new ProposeFeeDetailView();
                                newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), proposeFeeDetailView);
                            }

                            ProposeCreditInfo proposeCreditInfo = proposeCreditInfoDAO.findById(feeDetailView.getCreditDetailViewId());
                            if (!Util.isNull(proposeCreditInfo)) {
                                ProposeCreditInfoDetailView proposeCreditInfoDetailView = proposeLineTransform.transformProposeCreditToView(proposeCreditInfo, ProposeType.A);
                                if(!Util.isNull(proposeCreditInfoDetailView)) {
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
                        } else if (feeDetailView.getFeeLevel() == FeeLevel.APP_LEVEL) {
                            appFeeDetailViewList.add(feeDetailView);
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
                    decisionView.setApproveAppFeeDetailViewList(appFeeDetailViewList);

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
                        for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()){
                            if(pricingFee.getType().equals("9")) {
                                String creditTypeId = pricingFee.getCreditDetailId();
                                for (ProposeCreditInfoDetailView proposeCreditInfoDetailView : decisionView.getApproveCreditList()) {
                                    if (creditTypeId.equals(proposeCreditInfoDetailView.getId() + "")) {
                                        proposeCreditInfoDetailView.setFrontEndFee(pricingFee.getFeePercent());
                                        proposeCreditInfoDetailView.setFrontEndFeeOriginal(pricingFee.getFeePercent());
                                        break;
                                    }
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
        if(proposeCollateralInfoView.getUwDecision() == DecisionType.APPROVED) {
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
        } else  {
            returnMapVal.put("notCheckNoFlag", false);
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

        if(!Util.isNull(proposeCollateralInfoSubView.getMortgageList()) && !Util.isZero(proposeCollateralInfoSubView.getMortgageList().size())) {
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
            returnMapVal.put("notHaveMortgage", false);
        } else {
            returnMapVal.put("notHaveMortgage", true);
        }

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

    public Map<String, Object> onRetrieveAppraisalReport(String jobId, User user, List<ProposeCollateralInfoView> proposeCollateralInfoViewList, boolean isModeEdit) {
        Map<String, Object> returnMapVal =  new HashMap<String, Object>();
        Integer completeFlag = 1;
        if (!Util.isNull(jobId) && !Util.isEmpty(jobId) && !Util.isNull(user)) {
            if (isModeEdit) {
                if(checkCountJobIdExist(proposeCollateralInfoViewList, jobId) == 1) {
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
            } else {
                if(checkJobIdExist(proposeCollateralInfoViewList, jobId)) {
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

    public int checkCountJobIdExist(List<ProposeCollateralInfoView> proposeCollateralInfoViewList, String jobId) {
        int count = 0;
        for (ProposeCollateralInfoView proposeCollateralInfoView : proposeCollateralInfoViewList) {
            if (Util.equals(proposeCollateralInfoView.getJobID(), jobId)) {
                count++;
            }
        }
        return count;
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

    public ProposeCreditInfoDetailView onCalInstallment(ProposeLineView proposeLineView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int specialProgramId, int applyTCG, int dbrMarketableFlag){
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
                        PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(proposeCreditInfoDetailView.getCreditTypeView().getId(), proposeCreditInfoDetailView.getProductProgramView().getId());

                        if(!Util.isNull(prdProgramToCreditType)){
                            if (prdProgramToCreditType.getCreditType().getCreditGroup() == CreditTypeGroup.OD.value()) { // check OD
                                ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLineView.getCreditCustomerType().value(), specialProgramId, applyTCG, dbrMarketableFlag);
                                if(!Util.isNull(productFormula)){
                                    spread = productFormula.getDbrSpread();
                                }
                            } else {
                                ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, proposeLineView.getCreditCustomerType().value(), specialProgramId, applyTCG);
                                if(!Util.isNull(productFormula)){
                                    spread = productFormula.getDbrSpread();
                                }
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
        DBRView dbrView = dbrControl.getDBRByWorkCase(workCaseId);
        int dbrMarketableFlag = 0;
        if(!Util.isNull(dbrView)){
            dbrMarketableFlag = dbrView.getDbrMarketableFlag();
        }

        if(!Util.isNull(proposeLineView)) {
            //Calculation
            proposeLineView = calculateTotalForBRMS(proposeLineView);

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
                proCreInfDetView = onCalInstallment(proposeLineView, proCreInfDetView, specialProgramId, applyTCG, dbrMarketableFlag);
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
            List<ProposeFeeDetail> proposeFeeDetailList = proposeLineTransform.transformProposeFeeToModelList(workCase, proposeLineView.getProposeFeeDetailViewOriginalList(), proposeType);
            if(!Util.isNull(proposeFeeDetailList) && !Util.isZero(proposeFeeDetailList.size())) {
                proposeFeeDetailDAO.persist(proposeFeeDetailList);
            }
            List<ProposeFeeDetail> proposeAppFeeDetailList = proposeLineTransform.transformFeeDetailToModelList(workCase, proposeLineView.getProposeAppFeeDetailViewList(), proposeType);
            if(!Util.isNull(proposeAppFeeDetailList) && !Util.isZero(proposeAppFeeDetailList.size())) {
                proposeFeeDetailDAO.persist(proposeAppFeeDetailList);
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
}