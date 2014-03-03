package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.ApprovalHistoryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.ApprovalHistory;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.clevel.selos.util.ValidationUtil;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "decision")
public class Decision implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    //Business logic
    @Inject
    private DecisionControl decisionControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private TCGInfoControl tcgInfoControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private ExSummaryControl exSummaryControl;
    @Inject
    private LoanPurposeControl loanPurposeControl;
    @Inject
    private DisbursementTypeControl disbursementTypeControl;
    @Inject
    private ProductControl productControl;

    //DAO
    @Inject
    CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    FollowConditionDAO followConditionDAO;
    @Inject
    ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    SpecialProgramDAO specialProgramDAO;

    //Transform
    @Inject
    CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    CountryTransform countryTransform;
    @Inject
    DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    LoanPurposeTransform loanPurposeTransform;
    @Inject
    FollowConditionTransform followConditionTransform;
    @Inject
    ApprovalHistoryTransform approvalHistoryTransform;
    @Inject
    SpecialProgramTransform specialProgramTransform;
    @Inject
    PotentialCollateralTransform potentialCollateralTransform;
    @Inject
    CollateralTypeTransform collateralTypeTransform;
    @Inject
    SubCollateralTypeTransform subCollateralTypeTransform;
    @Inject
    MortgageTypeTransform mortgageTypeTransform;

    // Session
    private long workCaseId;
    private long stepId;
    
    // User Role
    private boolean roleBDM;
    private boolean roleZM_RGM;
    private boolean roleUW;    

    // Mode
    enum ModeForButton {
        ADD, EDIT
    }
    private ModeForButton modeForButton;
    private boolean modeEditCredit;
    private boolean modeEditCollateral;
    private boolean modeEditSubColl;
    private boolean modeEditGuarantor;

    // Dialog Messages
    private String messageHeader;
    private String message;

    //Main Model View
    private DecisionView decisionView;
    private SpecialProgramView specialProgramView;
    private int applyTCG;
    private ProductGroup productGroup;
    private int seq;
    private HashMap<Integer, Integer> hashSeqCredit;

    // Retrieve Price/Fee
    private List<CreditRequestTypeView> creditRequestTypeViewList;
    private List<CountryView> countryViewList;

    // Approve Credit
    private NewCreditDetailView selectedApproveCredit;
    private int rowIndexCredit;
    private BaseRate standardBasePriceDlg;
    private BigDecimal standardInterestDlg;
    private BaseRate suggestBasePriceDlg;
    private BigDecimal suggestInterestDlg;
    private boolean modeEditReducePricing;
    private boolean modeEditReduceFrontEndFee;
    private boolean cannotEditStandard;
    private boolean cannotAddTier;
    private List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList;
    private List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList;
    private List<BaseRate> baseRateList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<DisbursementTypeView> disbursementTypeViewList;
    private List<Long> deleteCreditIdList;

    // Approve Collateral
    private NewCollateralView selectedApproveCollateral;
    private NewCollateralHeadView selectedApproveCollHead;
    private NewCollateralSubView selectedApproveSubColl;
    private int rowIndexCollateral;
    private int rowIndexCollHead;
    private int rowIndexSubColl;
    private boolean flagComs;
    private List<PotentialCollateral> potentialCollList;
    private List<PotentialCollateralView> potentialCollViewList;
    private List<CollateralType> collateralTypeList;
    private List<CollateralTypeView> collateralTypeViewList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<SubCollateralTypeView> subCollateralTypeViewList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageType> mortgageTypeList;
    private List<MortgageTypeView> mortgageTypeViewList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<ProposeCreditDetailView> collateralCreditTypeList;
    private List<ProposeCreditDetailView> selectedCollateralCrdTypeItems;
    private List<Long> deleteCollIdList;
    private List<Long> deleteSubCollIdList;

    // Approve Guarantor
    private NewGuarantorDetailView selectedApproveGuarantor;
    private int rowIndexGuarantor;
    private List<CustomerInfoView> guarantorList;
    private List<ProposeCreditDetailView> guarantorCreditTypeList;
    private List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems;
    private List<Long> deleteGuarantorIdList;

    // Follow Up Condition
    private DecisionFollowConditionView decisionFollowConditionView;
    private int rowIndexDecisionFollowCondition;
    private List<FollowConditionView> followConditionViewList;
    private List<Long> deleteConditionIdList;

    // Approval History
    private ApprovalHistoryView approvalHistoryView;

    // List One Time Query on init
    private List<PrdGroupToPrdProgramView> _prdGroupToPrdProgramAll;
    private List<PrdGroupToPrdProgramView> _prdGroupToPrdProgramByGroup;

    public Decision() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

            if (session.getAttribute("stepId") != null) {
                stepId = Long.parseLong(session.getAttribute("stepId").toString());
            }

            // set role
            int roleId = decisionControl.getUserRoleId();
            if (RoleValue.ABDM.id() == roleId || RoleValue.BDM.id() == roleId) {
                roleBDM = true;
            }
            else if (RoleValue.UW.id() == roleId) {
                roleUW = true;
            }
            else if (RoleValue.ZM.id() == roleId || RoleValue.RGM.id() == roleId) {
                roleZM_RGM = true;
            }

        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e) {
                log.info("Exception :: {}", e);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();

        Map<String, Object> mapValue = decisionControl.getDecisionMapValue(workCaseId);
        decisionView = (DecisionView) mapValue.get("decisionView");
        // For delete on save
        deleteCreditIdList = (List<Long>) mapValue.get("deleteCreditIdList");
        deleteCollIdList = (List<Long>) mapValue.get("deleteCollIdList");
        deleteGuarantorIdList = (List<Long>) mapValue.get("deleteGuarantorIdList");
        deleteSubCollIdList = new ArrayList<Long>();
        deleteConditionIdList = new ArrayList<Long>();

        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        if (basicInfoView != null) {
            if (basicInfoView.getSpProgram() == RadioValue.YES.value()) {
                specialProgramView = specialProgramTransform.transformToView(basicInfoView.getSpecialProgram());
            } else {
                specialProgramView = specialProgramTransform.transformToView(specialProgramDAO.findById(3));
            }
            productGroup = basicInfoView.getProductGroup();
        }

        TCGView tcgView = tcgInfoControl.getTcgView(workCaseId);
        if (tcgView != null) {
            applyTCG = tcgView.getTCG();
        }

        // ========== Retrieve Pricing/Fee ========== //
        creditRequestTypeViewList = creditRequestTypeTransform.transformToView(creditRequestTypeDAO.findAll());
        countryViewList = countryTransform.transformToView(countryDAO.findAll());
        // ================================================== //


        // ========== Approve Credit Dialog ========== //
        selectedApproveCredit = new NewCreditDetailView();

        standardBasePriceDlg = new BaseRate();
        standardInterestDlg = BigDecimal.ZERO;
        suggestBasePriceDlg = new BaseRate();
        suggestInterestDlg = BigDecimal.ZERO;

        baseRateList = baseRateDAO.findAll();
        if (baseRateList == null)
            baseRateList = new ArrayList<BaseRate>();

        prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        _prdGroupToPrdProgramAll = productControl.getPrdGroupToPrdProgramProposeAll();
        _prdGroupToPrdProgramByGroup = productControl.getPrdGroupToPrdProgramProposeByGroup(productGroup);
        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();
        // ================================================== //

        // ========== Approve Collateral Dialog ========== //
        selectedApproveCollateral = new NewCollateralView();
        potentialCollList = potentialCollateralDAO.findAll();
        potentialCollViewList = potentialCollateralTransform.transformToView(potentialCollList);
        collateralTypeList = collateralTypeDAO.findAll();
        collateralTypeViewList = collateralTypeTransform.transformToView(collateralTypeList);
        // ================================================== //

        // ========== Sub Collateral Dialog ========== //
        selectedApproveSubColl = new NewCollateralSubView();
        collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
        mortgageTypeList = mortgageTypeDAO.findAll();
        mortgageTypeViewList = mortgageTypeTransform.transformToView(mortgageTypeList);
        relatedWithAllList = new ArrayList<NewCollateralSubView>();
        // ================================================== //

        // ========== Approve Guarantor Dialog ========== //
        selectedApproveGuarantor = new NewGuarantorDetailView();
        guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
        // ================================================== //

        // ========== Follow Up Condition ========== //
        decisionFollowConditionView = new DecisionFollowConditionView();
        followConditionViewList = followConditionTransform.transformToView(followConditionDAO.findAll());

        // ========== Approval History ========== //
        List<ApprovalHistory> approvalHistories = approvalHistoryDAO.findByWorkCase(workCaseId, false);
        if (approvalHistories != null && !approvalHistories.isEmpty()) {
            approvalHistoryView = approvalHistoryTransform.transformToView(approvalHistories.get(0));
        } else {
            approvalHistoryView = decisionControl.getApprovalHistoryView(stepId);
        }

        // Initial sequence number credit
        seq = 1;
        hashSeqCredit = new HashMap<Integer, Integer>();
    }

    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee()");
        //todo: Call BRMS to get data Propose Credit Info
    }

    // ==================== Approve Credit - Actions ==================== //
    public void onAddApproveCredit() {
        log.debug("onAddApproveCredit()");
        selectedApproveCredit = new NewCreditDetailView();

        onChangeRequestType();

        if (baseRateList != null && !baseRateList.isEmpty()) {
            standardBasePriceDlg = baseRateList.get(0);
            suggestBasePriceDlg = baseRateList.get(0);
        }
        standardInterestDlg = BigDecimal.ZERO;
        suggestInterestDlg = BigDecimal.ZERO;

        modeEditCredit = false;
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onEditApproveCredit() {
        log.debug("onEditApproveCredit() selectedApproveCredit: {}", selectedApproveCredit);

        onChangeRequestType();

        creditFacProposeControl.calculateInstallment(selectedApproveCredit);

        if (selectedApproveCredit.getRequestType() == RequestTypes.NEW.value()) {
            if (selectedApproveCredit.getNewCreditTierDetailViewList() != null && !selectedApproveCredit.getNewCreditTierDetailViewList().isEmpty()) {
                BaseRate standardBaseRate = selectedApproveCredit.getNewCreditTierDetailViewList().get(0).getStandardBasePrice();
                BigDecimal standardInterest = selectedApproveCredit.getNewCreditTierDetailViewList().get(0).getStandardInterest();
                standardBasePriceDlg = getNewBaseRate(standardBaseRate);
                standardInterestDlg = new BigDecimal(standardInterest.doubleValue());

                BaseRate suggestBaseRate = selectedApproveCredit.getNewCreditTierDetailViewList().get(0).getSuggestBasePrice();
                BigDecimal suggestInterest = selectedApproveCredit.getNewCreditTierDetailViewList().get(0).getSuggestInterest();
                suggestBasePriceDlg = getNewBaseRate(suggestBaseRate);
                suggestInterestDlg = new BigDecimal(suggestInterest.doubleValue());
            }
        } else {
            if (baseRateList != null && !baseRateList.isEmpty()) {
                standardBasePriceDlg = getNewBaseRate(baseRateList.get(0));
                suggestBasePriceDlg = getNewBaseRate(baseRateList.get(0));
            } else {
                standardBasePriceDlg = new BaseRate();
                suggestBasePriceDlg = new BaseRate();
            }
            standardInterestDlg = BigDecimal.ZERO;
            suggestInterestDlg = BigDecimal.ZERO;
        }

        modeEditCredit = true;
    }

    public void onDeleteApproveCredit() {
        log.debug("onDeleteApproveCredit() rowIndexCredit: {}", rowIndexCredit);
        // keep exist id from DB for delete on save decision
        if (decisionView.getApproveCreditList().get(rowIndexCredit).getId() != 0) {
            deleteCreditIdList.add(decisionView.getApproveCreditList().get(rowIndexCredit).getId());
        }
        decisionView.getApproveCreditList().remove(rowIndexCredit);
    }

    public void onSaveApproveCredit() {
        log.debug("onSaveApproveCredit()");
        boolean success = false;

        if (selectedApproveCredit.getProductProgramView().getId() != 0
                && selectedApproveCredit.getCreditTypeView().getId() != 0
                && selectedApproveCredit.getLoanPurposeView().getId() != 0
                && selectedApproveCredit.getDisbursementTypeView().getId() != 0) {

            ProductProgramView productProgramView = getProductProgramById(selectedApproveCredit.getProductProgramView().getId());
            CreditTypeView creditTypeView = getCreditTypeById(selectedApproveCredit.getCreditTypeView().getId());
            LoanPurposeView loanPurposeView = getLoanPurposeById(selectedApproveCredit.getLoanPurposeView().getId());
            DisbursementTypeView disbursementTypeView = getDisbursementTypeById(selectedApproveCredit.getDisbursementTypeView().getId());

            if (modeEditCredit) {
                NewCreditDetailView creditDetailEdit = decisionView.getApproveCreditList().get(rowIndexCredit);
                creditDetailEdit.setUwDecision(selectedApproveCredit.getUwDecision());
                creditDetailEdit.setProductProgramView(productProgramView);
                creditDetailEdit.setCreditTypeView(creditTypeView);
                creditDetailEdit.setRequestType(selectedApproveCredit.getRequestType());
                creditDetailEdit.setRefinance(selectedApproveCredit.getRefinance());
                creditDetailEdit.setProductCode(selectedApproveCredit.getProductCode());
                creditDetailEdit.setProjectCode(selectedApproveCredit.getProjectCode());
                creditDetailEdit.setLimit(selectedApproveCredit.getLimit());
                creditDetailEdit.setPCEPercent(selectedApproveCredit.getPCEPercent());
                creditDetailEdit.setPCEAmount(selectedApproveCredit.getPCEAmount());
                creditDetailEdit.setReducePriceFlag(selectedApproveCredit.isReducePriceFlag());
                creditDetailEdit.setReduceFrontEndFee(selectedApproveCredit.isReduceFrontEndFee());
                creditDetailEdit.setFrontEndFee(selectedApproveCredit.getFrontEndFee());
                creditDetailEdit.setLoanPurposeView(loanPurposeView);
                creditDetailEdit.setRemark(selectedApproveCredit.getRemark());
                creditDetailEdit.setDisbursementTypeView(disbursementTypeView);
                creditDetailEdit.setHoldLimitAmount(selectedApproveCredit.getHoldLimitAmount());
                creditDetailEdit.setNewCreditTierDetailViewList(selectedApproveCredit.getNewCreditTierDetailViewList());
                creditDetailEdit.setSeq(selectedApproveCredit.getSeq());

                success = true;
            } else {
                // Add New
                NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
                creditDetailAdd.setUwDecision(selectedApproveCredit.getUwDecision());
                creditDetailAdd.setProductProgramView(productProgramView);
                creditDetailAdd.setCreditTypeView(creditTypeView);
                creditDetailAdd.setRequestType(selectedApproveCredit.getRequestType());
                creditDetailAdd.setRefinance(selectedApproveCredit.getRefinance());
                creditDetailAdd.setProductCode(selectedApproveCredit.getProductCode());
                creditDetailAdd.setProjectCode(selectedApproveCredit.getProjectCode());
                creditDetailAdd.setLimit(selectedApproveCredit.getLimit());
                creditDetailAdd.setPCEPercent(selectedApproveCredit.getPCEPercent());
                creditDetailAdd.setPCEAmount(selectedApproveCredit.getPCEAmount());
                creditDetailAdd.setReducePriceFlag(selectedApproveCredit.isReducePriceFlag());
                creditDetailAdd.setReduceFrontEndFee(selectedApproveCredit.isReduceFrontEndFee());
                creditDetailAdd.setFrontEndFee(selectedApproveCredit.getFrontEndFee());
                creditDetailAdd.setLoanPurposeView(loanPurposeView);
                creditDetailAdd.setRemark(selectedApproveCredit.getRemark());
                creditDetailAdd.setDisbursementTypeView(disbursementTypeView);
                creditDetailAdd.setHoldLimitAmount(selectedApproveCredit.getHoldLimitAmount());
                creditDetailAdd.setNewCreditTierDetailViewList(selectedApproveCredit.getNewCreditTierDetailViewList());
                creditDetailAdd.setSeq(seq);

                if (decisionView.getApproveCreditList() != null) {
                    decisionView.getApproveCreditList().add(creditDetailAdd);
                } else {
                    List<NewCreditDetailView> newApproveCreditList = new ArrayList<NewCreditDetailView>();
                    newApproveCreditList.add(creditDetailAdd);
                    decisionView.setApproveCreditList(newApproveCreditList);
                }

                success = true;
            }

            hashSeqCredit.put(seq, 0);
            seq++;
            log.debug("seq++ of credit after add complete Approve Propose Credit :: {}", seq);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onChangeRequestType() {
        log.debug("onChangeRequestType() requestType: {}", selectedApproveCredit.getRequestType());
        prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        prdProgramToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (RequestTypes.CHANGE.value() == selectedApproveCredit.getRequestType()) {   //change
            prdGroupToPrdProgramViewList = _prdGroupToPrdProgramAll;
            selectedApproveCredit.setProductProgramView(new ProductProgramView());
            cannotEditStandard = false;
            cannotAddTier = false;
        }
        else if (RequestTypes.NEW.value() == selectedApproveCredit.getRequestType()) {
            if (productGroup != null) {
                prdGroupToPrdProgramViewList = _prdGroupToPrdProgramByGroup;
            }
            cannotEditStandard = true;

            if (modeEditCredit) {
                if (selectedApproveCredit.getNewCreditTierDetailViewList() == null || selectedApproveCredit.getNewCreditTierDetailViewList().isEmpty()) {
                    cannotAddTier = true;
                } else {
                    cannotAddTier = false;
                }
            } else {
                // on click add new
                cannotAddTier = true;
            }
        }
    }

    public void onChangeProductProgram() {
        log.debug("onChangeProductProgram() productProgram.id = {}", selectedApproveCredit.getProductProgramView().getId());
        selectedApproveCredit.setProductCode("");
        selectedApproveCredit.setProjectCode("");

        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(selectedApproveCredit.getProductProgramView());
        selectedApproveCredit.setCreditTypeView(new CreditTypeView());
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType() creditType.id: {}", selectedApproveCredit.getCreditTypeView().getId());
        if (selectedApproveCredit.getProductProgramView().getId() != 0 && selectedApproveCredit.getCreditTypeView().getId() != 0) {
            int specialProgramId = specialProgramView != null ? specialProgramView.getId() : 0;
            int creditCusType = decisionView.getCreditCustomerType() != null ? decisionView.getCreditCustomerType().value() : CreditCustomerType.NOT_SELECTED.value();

            ProductFormulaView productFormulaView = productControl.getProductFormulaView(
                    selectedApproveCredit.getCreditTypeView().getId(),
                    selectedApproveCredit.getProductProgramView().getId(),
                    creditCusType, specialProgramId, applyTCG);

            if (productFormulaView != null) {
                log.debug("onChangeCreditType :::: productFormula : {}", productFormulaView.getId());
                selectedApproveCredit.setProductCode(productFormulaView.getProductCode());
                selectedApproveCredit.setProjectCode(productFormulaView.getProjectCode());
                log.info("productFormula.getReduceFrontEndFee() ::: {}", productFormulaView.getReduceFrontEndFee());
                log.info("productFormula.getReducePricing() ::: {}", productFormulaView.getReducePricing());

                modeEditReducePricing = productFormulaView.getReducePricing() == 1;
                modeEditReduceFrontEndFee = productFormulaView.getReduceFrontEndFee() == 1;
            }
        }
    }

    public void onAddTierInfo() {
        log.debug("onAddTierInfo()");
        BaseRate finalBaseRate;
        BigDecimal finalInterest;
        String finalPriceLabel;

        BaseRate suggestBase = new BaseRate();
        BigDecimal suggestPrice = BigDecimal.ZERO;
        String suggestPriceLabel = "";

        BaseRate standardBase = new BaseRate();
        BigDecimal standardPrice = BigDecimal.ZERO;
        String standardPriceLabel = "";

        if (suggestBasePriceDlg.getId() != 0) {
            suggestBase = getBaseRateById(suggestBasePriceDlg.getId());
            if (suggestBase != null) {
                suggestPrice = suggestBase.getValue().add(suggestInterestDlg);
                if (ValidationUtil.isValueCompareToZero(suggestInterestDlg, ValidationUtil.CompareMode.LESS_THAN)) {
                    suggestPriceLabel = suggestBase.getName() + " " + suggestInterestDlg;
                } else {
                    suggestPriceLabel = suggestBase.getName() + " + " + suggestInterestDlg;
                }
            }
        }

        if (standardBasePriceDlg.getId() != 0) {
            standardBase = getBaseRateById(standardBasePriceDlg.getId());
            if (standardBase != null) {
                standardPrice = standardBase.getValue().add(standardInterestDlg);
                if (ValidationUtil.isValueCompareToZero(standardInterestDlg, ValidationUtil.CompareMode.LESS_THAN)) {
                    standardPriceLabel = standardBase.getName() + " " + standardInterestDlg;
                } else {
                    standardPriceLabel = standardBase.getName() + " + " + standardInterestDlg;
                }
            }
        }

        if (ValidationUtil.isFirstCompareToSecond(standardPrice, suggestPrice, ValidationUtil.CompareMode.GREATER_THAN)) {
            finalBaseRate = getNewBaseRate(standardBasePriceDlg);
            finalInterest = new BigDecimal(standardInterestDlg.doubleValue());
            finalPriceLabel = standardPriceLabel;
        } else if (ValidationUtil.isFirstCompareToSecond(standardPrice, suggestPrice, ValidationUtil.CompareMode.LESS_THAN)) {
            finalBaseRate = getNewBaseRate(suggestBasePriceDlg);
            finalInterest = new BigDecimal(suggestInterestDlg.doubleValue());
            finalPriceLabel = suggestPriceLabel;
        } else {
            finalBaseRate = getNewBaseRate(standardBasePriceDlg);
            finalInterest = new BigDecimal(standardInterestDlg.doubleValue());
            finalPriceLabel = standardPriceLabel;
        }

        NewCreditTierDetailView creditTierDetailAdd = new NewCreditTierDetailView();

        creditTierDetailAdd.setFinalPriceLabel(finalPriceLabel);
        creditTierDetailAdd.setFinalInterest(finalInterest);
        creditTierDetailAdd.setFinalBasePrice(finalBaseRate);

        creditTierDetailAdd.setSuggestPriceLabel(suggestPriceLabel);
        creditTierDetailAdd.setSuggestInterest(new BigDecimal(suggestInterestDlg.doubleValue()));
        creditTierDetailAdd.setSuggestBasePrice(suggestBase);

        creditTierDetailAdd.setStandardPriceLabel(standardPriceLabel);
        creditTierDetailAdd.setStandardInterest(new BigDecimal(standardInterestDlg.doubleValue()));
        creditTierDetailAdd.setStandardBasePrice(standardBase);

        creditTierDetailAdd.setCanEdit(true);

        if (selectedApproveCredit.getNewCreditTierDetailViewList() != null) {
            selectedApproveCredit.getNewCreditTierDetailViewList().add(0, creditTierDetailAdd);
        } else {
            List<NewCreditTierDetailView> tierDetailViewList = new ArrayList<NewCreditTierDetailView>();
            tierDetailViewList.add(0, creditTierDetailAdd);
            selectedApproveCredit.setNewCreditTierDetailViewList(tierDetailViewList);
        }
    }

    public void onDeleteTierInfo(int rowIndex) {
        selectedApproveCredit.getNewCreditTierDetailViewList().remove(rowIndex);
    }

    // ==================== Approve Collateral - Actions ==================== //
    public void onAddApproveCollateral() {
        log.debug("onAddApproveCollateral()");
        selectedApproveCollateral = new NewCollateralView();
        selectedCollateralCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        collateralCreditTypeList = creditFacProposeControl.findProposeCreditDetail(decisionView.getApproveCreditList(), workCaseId);
        flagComs = false;
        modeEditCollateral = false;
    }

    public void onEditApproveCollateral() {
        log.debug("onEditApproveCollateral() rowIndexCollateral: {}, selectedApproveCollateral: {}", rowIndexCollateral, selectedApproveCollateral);
        if (selectedApproveCollateral.getProposeCreditDetailViewList() != null && selectedApproveCollateral.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedCollateralCrdTypeItems = selectedApproveCollateral.getProposeCreditDetailViewList();
        }
        collateralCreditTypeList = creditFacProposeControl.findProposeCreditDetail(decisionView.getApproveCreditList(), workCaseId);
        flagComs = false;

        log.info("selectedApproveCollateral.isComs: {}", selectedApproveCollateral.isComs());
        if (selectedApproveCollateral.isComs()) {
            flagComs = true;
        }
        modeEditCollateral = true;
    }

    public void onDeleteApproveCollateral() {
        log.debug("onDeleteApproveCollateral() rowIndexCollateral: {}", rowIndexCollateral);
        // keep exist id from DB for delete on save decision
        if (decisionView.getApproveCollateralList().get(rowIndexCollateral).getId() != 0) {
            deleteCollIdList.add(decisionView.getApproveCollateralList().get(rowIndexCollateral).getId());
        }
        decisionView.getApproveCollateralList().remove(rowIndexCollateral);
    }

    public void onSaveProposeCollInfo() {
        log.debug("onSaveProposeCollInfo()");
        boolean success = false;

        NewCollateralView collateralInfoEdit;
        if (modeEditCollateral) {
            log.debug("===> Edit - Collateral: {}", selectedApproveCollateral);
            collateralInfoEdit = decisionView.getApproveCollateralList().get(rowIndexCollateral);
        } else {
            log.debug("===> Add New - Collateral: {}", selectedApproveCollateral);
            collateralInfoEdit = new NewCollateralView();
        }

        collateralInfoEdit.setJobID(selectedApproveCollateral.getJobID());
        collateralInfoEdit.setAppraisalDate(selectedApproveCollateral.getAppraisalDate());
        collateralInfoEdit.setAadDecision(selectedApproveCollateral.getAadDecision());
        collateralInfoEdit.setAadDecisionReason(selectedApproveCollateral.getAadDecisionReason());
        collateralInfoEdit.setAadDecisionReasonDetail(selectedApproveCollateral.getAadDecisionReasonDetail());
        collateralInfoEdit.setUsage(selectedApproveCollateral.getUsage());
        collateralInfoEdit.setTypeOfUsage(selectedApproveCollateral.getTypeOfUsage());
        collateralInfoEdit.setUwDecision(selectedApproveCollateral.getUwDecision());
        collateralInfoEdit.setUwRemark(selectedApproveCollateral.getUwRemark());
        collateralInfoEdit.setMortgageCondition(selectedApproveCollateral.getMortgageCondition());
        collateralInfoEdit.setMortgageConditionDetail(selectedApproveCollateral.getMortgageConditionDetail());
        collateralInfoEdit.setBdmComments(selectedApproveCollateral.getBdmComments());
        collateralInfoEdit.setComs(selectedApproveCollateral.isComs());

        List<NewCollateralHeadView> newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
        if (selectedApproveCollateral.getNewCollateralHeadViewList() != null && selectedApproveCollateral.getNewCollateralHeadViewList().size() > 0) {
            for (NewCollateralHeadView collateralHeadView : selectedApproveCollateral.getNewCollateralHeadViewList()) {
                PotentialCollateral potentialCollateral = getPotentialCollateralById(collateralHeadView.getPotentialCollateral().getId());
                CollateralType collTypePercentLTV = getCollateralTypeById(collateralHeadView.getCollTypePercentLTV().getId());
                CollateralType headCollType = getCollateralTypeById(collateralHeadView.getHeadCollType().getId());

                NewCollateralHeadView newCollateralHeadDetailAdd = new NewCollateralHeadView();
                newCollateralHeadDetailAdd.setPotentialCollateral(potentialCollateral);
                newCollateralHeadDetailAdd.setCollTypePercentLTV(collTypePercentLTV);
                newCollateralHeadDetailAdd.setExistingCredit(collateralHeadView.getExistingCredit());
                newCollateralHeadDetailAdd.setTitleDeed(collateralHeadView.getTitleDeed());
                newCollateralHeadDetailAdd.setCollateralLocation(collateralHeadView.getCollateralLocation());
                newCollateralHeadDetailAdd.setAppraisalValue(collateralHeadView.getAppraisalValue());
                newCollateralHeadDetailAdd.setHeadCollType(headCollType);
                newCollateralHeadDetailAdd.setInsuranceCompany(collateralHeadView.getInsuranceCompany());

                if (collateralHeadView.getNewCollateralSubViewList() != null && collateralHeadView.getNewCollateralSubViewList().size() > 0) {
                    Cloner cloner = new Cloner();
                    newCollateralHeadDetailAdd.setNewCollateralSubViewList(cloner.deepClone(collateralHeadView.getNewCollateralSubViewList()));
                }

                newCollateralHeadViewList.add(newCollateralHeadDetailAdd);
            }
        }
        collateralInfoEdit.setNewCollateralHeadViewList(newCollateralHeadViewList);

        if (selectedCollateralCrdTypeItems != null && selectedCollateralCrdTypeItems.size() > 0) {

            List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
            for (ProposeCreditDetailView creditTypeItem : selectedCollateralCrdTypeItems) {
                proposeCreditDetailViewList.add(creditTypeItem);

            }
            collateralInfoEdit.setProposeCreditDetailViewList(proposeCreditDetailViewList);

            success = true;
            log.debug("Success: Edit Collateral from ApproveCollateralList");
        } else {
            messageHeader = "Error Message";
            message = "Non selected Credit Type!";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("Failed: Can not edit Collateral from ApproveCollateralList because non selected credit type!");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral() : {}", rowIndexCollHead);
        selectedApproveSubColl = new NewCollateralSubView();
        relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(decisionView.getApproveCollateralList());

        if (selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeViewList ::: {}", subCollateralTypeViewList.size());
        }
        modeEditSubColl = false;
    }

    public void onEditSubCollateral() {
        log.debug("onEditSubCollateral()");
        if (selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeViewList ::: {}", subCollateralTypeViewList.size());
        }
        modeEditSubColl = true;
    }

    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral() rowIndexCollateral: {}, rowIndexCollHead: {}, rowIndexSubColl: {}",
                rowIndexCollateral, rowIndexCollHead, rowIndexSubColl);
        List<NewCollateralSubView> subCollateralViewList = decisionView.getApproveCollateralList().get(rowIndexCollateral).getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList();
        // keep exist id from DB on save decision
        if (subCollateralViewList.get(rowIndexSubColl).getId() != 0) {
            deleteSubCollIdList.add(subCollateralViewList.get(rowIndexSubColl).getId());
        }
        subCollateralViewList.remove(rowIndexSubColl);
    }

    public void onSaveSubCollateral() {
        log.debug("onSaveSubCollateral()");
        boolean success;
        SubCollateralType subCollateralType = getSubCollTypeById(selectedApproveSubColl.getSubCollateralType().getId());
        List<CustomerInfoView> _collOwnerUWList = selectedApproveSubColl.getCollateralOwnerUWList();
        List<MortgageType> _mortgageTypeList = selectedApproveSubColl.getMortgageList();
        List<NewCollateralSubView> _relatedWithList = selectedApproveSubColl.getRelatedWithList();

        if (modeEditSubColl) {
            log.debug("===> Edit - SubCollateral: {}", selectedApproveSubColl);
            NewCollateralSubView subCollEdit = selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList().get(rowIndexSubColl);
            subCollEdit.setSubCollateralType(subCollateralType);
            subCollEdit.setTitleDeed(selectedApproveSubColl.getTitleDeed());
            subCollEdit.setAddress(selectedApproveSubColl.getAddress());
            subCollEdit.setLandOffice(selectedApproveSubColl.getLandOffice());
            subCollEdit.setCollateralOwnerAAD(selectedApproveSubColl.getCollateralOwnerAAD());
            subCollEdit.setCollateralOwnerUWList(_collOwnerUWList);
            subCollEdit.setMortgageList(_mortgageTypeList);
            subCollEdit.setRelatedWithList(_relatedWithList);
            subCollEdit.setAppraisalValue(selectedApproveSubColl.getAppraisalValue());
            subCollEdit.setMortgageValue(selectedApproveSubColl.getMortgageValue());

            success = true;
        } else {
            //Add New
            log.debug("===> Add New - SubCollateral: {}", selectedApproveSubColl);
            NewCollateralSubView subCollAdd = new NewCollateralSubView();
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(selectedApproveSubColl.getTitleDeed());
            subCollAdd.setAddress(selectedApproveSubColl.getAddress());
            subCollAdd.setLandOffice(selectedApproveSubColl.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(selectedApproveSubColl.getCollateralOwnerAAD());
            subCollAdd.setCollateralOwnerUWList(_collOwnerUWList);
            subCollAdd.setMortgageList(_mortgageTypeList);
            subCollAdd.setRelatedWithList(_relatedWithList);
            subCollAdd.setAppraisalValue(selectedApproveSubColl.getAppraisalValue());
            subCollAdd.setMortgageValue(selectedApproveSubColl.getMortgageValue());

            if (selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList() == null) {
                selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).setNewCollateralSubViewList(new ArrayList<NewCollateralSubView>());
            }
            selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList().add(subCollAdd);

            success = true;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() id: {}", selectedApproveSubColl.getCollateralOwnerUW().getId());
        if (selectedApproveSubColl.getCollateralOwnerUW().getId() != 0) {
            if (selectedApproveSubColl.getCollateralOwnerUWList() == null) {
                selectedApproveSubColl.setCollateralOwnerUWList(new ArrayList<CustomerInfoView>());
            }
            selectedApproveSubColl.getCollateralOwnerUWList().add(
                    getCustomerInfoViewById(selectedApproveSubColl.getCollateralOwnerUW().getId(), collateralOwnerUwAllList));
        }
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        log.debug("onDeleteCollateralOwnerUW() rowIndex: {}", rowIndex);
        selectedApproveSubColl.getCollateralOwnerUWList().remove(rowIndex);
    }

    public void onAddMortgageType() {
        log.debug("onAddMortgageType() id: {}", selectedApproveSubColl.getMortgageType().getId());
        if (selectedApproveSubColl.getMortgageType().getId() != 0) {
            if (selectedApproveSubColl.getMortgageList() == null) {
                selectedApproveSubColl.setMortgageList(new ArrayList<MortgageType>());
            }
            selectedApproveSubColl.getMortgageList().add(
                    getMortgageTypeById(selectedApproveSubColl.getMortgageType().getId()));
        }
    }

    public void onDeleteMortgageType(int rowIndex) {
        log.debug("onDeleteMortgageType() rowIndex: {}", rowIndex);
        selectedApproveSubColl.getMortgageList().remove(rowIndex);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() id = {}", selectedApproveSubColl.getRelatedWithId());
        if (selectedApproveSubColl.getRelatedWithId() != 0) {
            NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(selectedApproveSubColl.getRelatedWithId());
            if (selectedApproveSubColl.getRelatedWithList() != null) {
                selectedApproveSubColl.setRelatedWithList(new ArrayList<NewCollateralSubView>());
            }
            selectedApproveSubColl.getRelatedWithList().add(relatedWith);
        }
    }

    public void onDeleteRelatedWith(int rowIndex) {
        log.debug("onDeleteRelatedWith() rowIndex: {}", rowIndex);
        selectedApproveSubColl.getRelatedWithList().remove(rowIndex);
    }

    public NewCollateralSubView getIdNewSubCollateralDetail(long newSubCollateralId) {
        NewCollateralSubView newSubCollateralReturn = new NewCollateralSubView();
        for (NewCollateralView newCollateralView : Util.safetyList(decisionView.getApproveCollateralList())) {
            for (NewCollateralHeadView newCollateralHeadView : Util.safetyList(newCollateralView.getNewCollateralHeadViewList())) {
                for (NewCollateralSubView newSubCollateralDetailOnAdded : Util.safetyList(newCollateralHeadView.getNewCollateralSubViewList())) {
                    log.info("newSubCollateralDetailView1 id ::: {}", newSubCollateralDetailOnAdded.getId());
                    log.info("newSubCollateralDetailView1 title deed ::: {}", newSubCollateralDetailOnAdded.getTitleDeed());
                    if (newSubCollateralId == newSubCollateralDetailOnAdded.getId()) {
                        newSubCollateralReturn = newSubCollateralDetailOnAdded;
                    }
                }
            }
        }
        return newSubCollateralReturn;
    }

    // ---------- Propose Guarantor Dialog ---------- //
    public void onAddApproveGuarantor() {
        log.debug("onAddAppProposeGuarantor()");
        selectedApproveGuarantor = new NewGuarantorDetailView();
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        guarantorCreditTypeList = creditFacProposeControl.findProposeCreditDetail(decisionView.getApproveCreditList(), workCaseId);

        modeEditGuarantor = false;
    }

    public void onEditApproveGuarantor() {
        log.debug("onEditAppProposeGuarantor() selectedApproveGuarantor: {}", selectedApproveGuarantor);
        guarantorCreditTypeList = creditFacProposeControl.findProposeCreditDetail(decisionView.getApproveCreditList(), workCaseId);

        if (selectedApproveGuarantor.getProposeCreditDetailViewList() != null && selectedApproveGuarantor.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedGuarantorCrdTypeItems = selectedApproveGuarantor.getProposeCreditDetailViewList();
            // update Guarantee Amount before render dialog
            for (ProposeCreditDetailView creditTypeFromAll : guarantorCreditTypeList) {
                for (ProposeCreditDetailView creditTypeFromSelected : selectedApproveGuarantor.getProposeCreditDetailViewList()) {
                    if (creditTypeFromAll.getSeq() == creditTypeFromSelected.getSeq()) {
                        creditTypeFromAll.setGuaranteeAmount(creditTypeFromSelected.getGuaranteeAmount());
                    }
                }
            }
        }

        modeEditGuarantor = true;
    }

    public void onSaveApproveGuarantor() {
        log.debug("onSaveApproveGuarantor()");
        log.debug("selectedGuarantorCrdTypeItems.size() = {}", selectedGuarantorCrdTypeItems.size());
        log.debug("selectedGuarantorCrdTypeItems: {}", selectedGuarantorCrdTypeItems);

        boolean success = false;
        BigDecimal sumGuaranteeAmtPerCrdType = BigDecimal.ZERO;

        if (modeEditGuarantor) {
            log.debug("===> Edit - Guarantor: {}", selectedApproveGuarantor);
            NewGuarantorDetailView guarantorDetailEdit = decisionView.getApproveGuarantorList().get(rowIndexGuarantor);
            guarantorDetailEdit.setUwDecision(selectedApproveGuarantor.getUwDecision());
            guarantorDetailEdit.setGuarantorName(getCustomerInfoViewById(selectedApproveGuarantor.getGuarantorName().getId(), guarantorList));
            guarantorDetailEdit.setTcgLgNo(selectedApproveGuarantor.getTcgLgNo());

            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                List<ProposeCreditDetailView> newCreditTypeItems = new ArrayList<ProposeCreditDetailView>();
                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    newCreditTypeItems.add(creditTypeItem);
                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());

                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                }
                guarantorDetailEdit.setProposeCreditDetailViewList(newCreditTypeItems);
                guarantorDetailEdit.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);

                success = true;
            } else {
                log.error("Failed: Can not edit Guarantor from ApproveGuarantorList because non selected credit type!");
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        } else {
            // Add New
            log.debug("===> Add New - Guarantor: {}", selectedApproveGuarantor);
            NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
            guarantorDetailAdd.setUwDecision(selectedApproveGuarantor.getUwDecision());
            guarantorDetailAdd.setGuarantorName(getCustomerInfoViewById(selectedApproveGuarantor.getGuarantorName().getId(), guarantorList));
            guarantorDetailAdd.setTcgLgNo(selectedApproveGuarantor.getTcgLgNo());

            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                if (guarantorDetailAdd.getProposeCreditDetailViewList() == null) {
                    guarantorDetailAdd.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
                }

                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    guarantorDetailAdd.getProposeCreditDetailViewList().add(creditTypeItem);
                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());

                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                }

                guarantorDetailAdd.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);

                if (decisionView.getApproveGuarantorList() != null) {
                    decisionView.getApproveGuarantorList().add(guarantorDetailAdd);
                } else {
                    List<NewGuarantorDetailView> newApproveGuarantorList = new ArrayList<NewGuarantorDetailView>();
                    newApproveGuarantorList.add(guarantorDetailAdd);
                    decisionView.setApproveGuarantorList(newApproveGuarantorList);
                }

                success = true;
            } else {
                log.error("Failed: Can not add new Guarantor to ApproveGuarantorList because non selected credit type!");
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        }

        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
        log.debug("success: {}", success);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onDeleteApproveGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() rowIndexGuarantor: {}", rowIndexGuarantor);
        // keep exist id from DB for delete on save decision()
        if (decisionView.getApproveGuarantorList().get(rowIndexGuarantor).getId() != 0) {
            deleteGuarantorIdList.add(decisionView.getApproveGuarantorList().get(rowIndexGuarantor).getId());
        }
        decisionView.getApproveGuarantorList().remove(rowIndexGuarantor);
        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
    }

    // ---------- FollowUp Condition - Action ---------- //
    public void onAddFollowUpCondition() {
        log.debug("onAddFollowUpCondition()");
        DecisionFollowConditionView addNewDecisionFollowCondition = new DecisionFollowConditionView();
        addNewDecisionFollowCondition.setConditionView(getFollowConditionById(decisionFollowConditionView.getConditionView().getId()));
        addNewDecisionFollowCondition.setDetail(decisionFollowConditionView.getDetail());
        addNewDecisionFollowCondition.setFollowDate(decisionFollowConditionView.getFollowDate());

        if (decisionView.getDecisionFollowConditionViewList() != null) {
            decisionView.getDecisionFollowConditionViewList().add(addNewDecisionFollowCondition);
        } else {
            List<DecisionFollowConditionView> decisionFollowConditionViewList = new ArrayList<DecisionFollowConditionView>();
            decisionFollowConditionViewList.add(addNewDecisionFollowCondition);
            decisionView.setDecisionFollowConditionViewList(decisionFollowConditionViewList);
        }
        // Clear form
        decisionFollowConditionView = new DecisionFollowConditionView();
    }

    public void onDeleteFollowUpCondition() {
        log.debug("onDeleteFollowUpCondition() rowIndexDecisionFollowCondition: {}", rowIndexDecisionFollowCondition);
        // keep exist id from DB for delete on save decision
        if (decisionView.getDecisionFollowConditionViewList().get(rowIndexDecisionFollowCondition).getId() != 0) {
            deleteConditionIdList.add(decisionView.getDecisionFollowConditionViewList().get(rowIndexDecisionFollowCondition).getId());
        }
        decisionView.getDecisionFollowConditionViewList().remove(rowIndexDecisionFollowCondition);
    }

    // ---------- Decision - Action ---------- //
    public void onSave() {
        log.debug("onSave()");

        try {
            // Save All Approve (Credit, Collateral, Guarantor) and Follow up Condition
            decisionView = decisionControl.saveDecision(decisionView, workCaseId);
            // todo: calculate Total Approve and Hidden field for NewCreditFacility
            // Save Approval History for UW
            if (roleUW) {
                approvalHistoryView = decisionControl.saveApprovalHistory(approvalHistoryView, workCaseId);
            }
            // Delete List
            decisionControl.deleteAllApproveByIdList(deleteCreditIdList, deleteCollIdList, deleteGuarantorIdList, deleteConditionIdList);

            //exSummaryControl.calForDecision(workCaseId);

        } catch (Exception e) {
            messageHeader = "Save Decision Failed.";
            if (e.getCause() != null) {
                message = "Save Decision data failed. Cause : " + e.getCause().toString();
            } else {
                message = "Save Decision data failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    // ----------------------------------------------- get Item from Select List ----------------------------------------------- //
    private BaseRate getNewBaseRate(BaseRate baseRate) {
        BaseRate newBaseRate = new BaseRate();
        if (baseRate == null) {
            return newBaseRate;
        }
        newBaseRate.setId(baseRate.getId());
        newBaseRate.setActive(baseRate.getActive());
        newBaseRate.setName(baseRate.getName());
        newBaseRate.setValue(baseRate.getValue());
        return newBaseRate;
    }

    private BaseRate getBaseRateById(int id) {
        BaseRate returnBaseRate = new BaseRate();
        if (baseRateList != null && !baseRateList.isEmpty() && id != 0) {
            for (BaseRate baseRate : baseRateList) {
                if (baseRate.getId() == id) {
                    returnBaseRate.setId(baseRate.getId());
                    returnBaseRate.setActive(baseRate.getActive());
                    returnBaseRate.setName(baseRate.getName());
                    returnBaseRate.setValue(baseRate.getValue());
                    break;
                }
            }
        }
        return returnBaseRate;
    }

    private ProductProgramView getProductProgramById(int id) {
        ProductProgramView returnPrdProgramView = new ProductProgramView();
        if (prdGroupToPrdProgramViewList != null && !prdGroupToPrdProgramViewList.isEmpty() && id != 0) {
            for (PrdGroupToPrdProgramView groupToProgramView : prdGroupToPrdProgramViewList) {
                if (groupToProgramView.getProductProgramView() != null
                    && groupToProgramView.getProductProgramView().getId() == id) {

                    returnPrdProgramView.setId(groupToProgramView.getProductProgramView().getId());
                    returnPrdProgramView.setActive(groupToProgramView.getProductProgramView().getActive());
                    returnPrdProgramView.setName(groupToProgramView.getProductProgramView().getName());
                    returnPrdProgramView.setDescription(groupToProgramView.getProductProgramView().getDescription());
                    returnPrdProgramView.setBrmsCode(groupToProgramView.getProductProgramView().getBrmsCode());
                    break;
                }
            }
        }
        return returnPrdProgramView;
    }

    private CreditTypeView getCreditTypeById(int id) {
        CreditTypeView returnCreditTypeView = new CreditTypeView();
        if (prdProgramToCreditTypeViewList != null && !prdProgramToCreditTypeViewList.isEmpty() && id != 0) {
            for (PrdProgramToCreditTypeView programToCreditTypeView : prdProgramToCreditTypeViewList) {
                if (programToCreditTypeView.getCreditTypeView() != null
                    && programToCreditTypeView.getCreditTypeView().getId() == id) {

                    returnCreditTypeView.setId(programToCreditTypeView.getCreditTypeView().getId());
                    returnCreditTypeView.setActive(programToCreditTypeView.getCreditTypeView().getActive());
                    returnCreditTypeView.setName(programToCreditTypeView.getCreditTypeView().getName());
                    returnCreditTypeView.setDescription(programToCreditTypeView.getCreditTypeView().getDescription());
                    returnCreditTypeView.setComsIntType(programToCreditTypeView.getCreditTypeView().getComsIntType());
                    returnCreditTypeView.setBrmsCode(programToCreditTypeView.getCreditTypeView().getBrmsCode());
                    break;
                }
            }
        }
        return returnCreditTypeView;
    }

    private LoanPurposeView getLoanPurposeById(int id) {
        LoanPurposeView returnLoanPurpose = new LoanPurposeView();
        if (loanPurposeViewList != null && !loanPurposeViewList.isEmpty() && id != 0) {
            for (LoanPurposeView loanPurposeView : loanPurposeViewList) {
                if (loanPurposeView.getId() == id) {
                    returnLoanPurpose.setId(loanPurposeView.getId());
                    returnLoanPurpose.setActive(loanPurposeView.getActive());
                    returnLoanPurpose.setDescription(loanPurposeView.getDescription());
                    returnLoanPurpose.setBrmsCode(loanPurposeView.getBrmsCode());
                    break;
                }
            }
        }
        return returnLoanPurpose;
    }

    private DisbursementTypeView getDisbursementTypeById(int id) {
        DisbursementTypeView returnDisbursementType = new DisbursementTypeView();
        if (disbursementTypeViewList != null && !disbursementTypeViewList.isEmpty() && id != 0) {
            for (DisbursementTypeView disbursementTypeView : disbursementTypeViewList) {
                if (disbursementTypeView.getId() == id) {
                    returnDisbursementType.setId(disbursementTypeView.getId());
                    returnDisbursementType.setActive(disbursementTypeView.getActive());
                    returnDisbursementType.setDisbursement(disbursementTypeView.getDisbursement());
                    break;
                }
            }
        }
        return returnDisbursementType;
    }

    private PotentialCollateral getPotentialCollateralById(int id) {
        PotentialCollateral returnPotentialColl = new PotentialCollateral();
        if (potentialCollList != null && !potentialCollList.isEmpty() && id != 0) {
            for (PotentialCollateral potentialCollateral : potentialCollList) {
                if (potentialCollateral.getId() == id) {
                    returnPotentialColl.setId(potentialCollateral.getId());
                    returnPotentialColl.setName(potentialCollateral.getName());
                    returnPotentialColl.setDescription(potentialCollateral.getDescription());
                    returnPotentialColl.setActive(potentialCollateral.getActive());
                    break;
                }
            }
        }
        return returnPotentialColl;
    }

    private CollateralType getCollateralTypeById(int id) {
        CollateralType returnCollType = new CollateralType();
        if (collateralTypeList != null && !collateralTypeList.isEmpty() && id != 0) {
            for (CollateralType collateralType : collateralTypeList) {
                if (collateralType.getId() == id) {
                    returnCollType.setId(collateralType.getId());
                    returnCollType.setCode(collateralType.getCode());
                    returnCollType.setDescription(collateralType.getDescription());
                    returnCollType.setActive(collateralType.getActive());
                    break;
                }
            }
        }
        return returnCollType;
    }

    private SubCollateralType getSubCollTypeById(int id) {
        SubCollateralType returnSubCollType = new SubCollateralType();
        if (subCollateralTypeList != null && !subCollateralTypeList.isEmpty() && id != 0) {
            for (SubCollateralType subCollateralType : subCollateralTypeList) {
                if (subCollateralType.getId() == id) {
                    returnSubCollType.setId(subCollateralType.getId());
                    returnSubCollType.setActive(subCollateralType.getActive());
                    returnSubCollType.setCode(subCollateralType.getCode());
                    returnSubCollType.setDescription(subCollateralType.getDescription());
                    returnSubCollType.setDefaultType(subCollateralType.getDefaultType());
                    returnSubCollType.setCollateralType(subCollateralType.getCollateralType());
                    break;
                }
            }
        }
        return returnSubCollType;
    }

    private MortgageType getMortgageTypeById(int id) {
        MortgageType returnMortgageType = new MortgageType();
        if (mortgageTypeList != null && !mortgageTypeList.isEmpty() && id != 0) {
            for (MortgageType mortgageType : mortgageTypeList) {
                if (mortgageType.getId() == id) {
                    returnMortgageType.setId(mortgageType.getId());
                    returnMortgageType.setActive(mortgageType.getActive());
                    returnMortgageType.setMortgage(mortgageType.getMortgage());
                    //returnMortgageType.setRedeem(mortgageType.isRedeem());
                    returnMortgageType.setMortgageFeeFlag(mortgageType.isMortgageFeeFlag());
                    returnMortgageType.setMortgageFlag(mortgageType.isMortgageFlag());
                    returnMortgageType.setPledgeFlag(mortgageType.isPledgeFlag());
                    returnMortgageType.setGuarantorFlag(mortgageType.isGuarantorFlag());
                    returnMortgageType.setTcgFlag(mortgageType.isTcgFlag());
                    returnMortgageType.setReferredFlag(mortgageType.isReferredFlag());
                }
            }
        }
        return returnMortgageType;
    }

    private CustomerInfoView getCustomerInfoViewById(long id, List<CustomerInfoView> customerInfoViewList) {
        CustomerInfoView returnCusInfoView = new CustomerInfoView();
        if (customerInfoViewList != null && !customerInfoViewList.isEmpty() && id != 0) {
            for (CustomerInfoView customerInfoView : customerInfoViewList) {
                if (customerInfoView.getId() == id) {
                    returnCusInfoView.setId(customerInfoView.getId());
                    returnCusInfoView.setFirstNameTh(customerInfoView.getFirstNameTh());
                    returnCusInfoView.setFirstNameEn(customerInfoView.getFirstNameEn());
                    returnCusInfoView.setLastNameTh(customerInfoView.getLastNameTh());
                    returnCusInfoView.setLastNameEn(customerInfoView.getLastNameEn());
                    returnCusInfoView.setTitleTh(customerInfoView.getTitleTh());
                    returnCusInfoView.setTitleEn(customerInfoView.getTitleEn());
                    break;
                }
            }
        }
        return returnCusInfoView;
    }

    private FollowConditionView getFollowConditionById(long id) {
        FollowConditionView returnFollowConditionView = new FollowConditionView();
        if (followConditionViewList != null && !followConditionViewList.isEmpty() && id != 0) {
            for (FollowConditionView followConditionView : followConditionViewList) {
                if (followConditionView.getId() == id) {
                    returnFollowConditionView.setId(followConditionView.getId());
                    returnFollowConditionView.setActive(followConditionView.getActive());
                    returnFollowConditionView.setName(followConditionView.getName());
                    returnFollowConditionView.setDescription(followConditionView.getDescription());
                    break;
                }
            }
        }
        return returnFollowConditionView;
    }

    // ----------------------------------------------- Enum Items ----------------------------------------------- //
    public CreditCustomerType getCreditCusTypeNORMAL() {
        return CreditCustomerType.NORMAL;
    }

    public CreditCustomerType getCreditCusTypePRIME() {
        return CreditCustomerType.PRIME;
    }

    public RequestTypes getRequestTypeNEW() {
        return RequestTypes.NEW;
    }

    public RequestTypes getRequestTypeCHANGE() {
        return RequestTypes.CHANGE;
    }

    public DecisionType getDecisionAPPROVED() {
        return DecisionType.APPROVED;
    }

    public DecisionType getDecisionREJECTED() {
        return DecisionType.REJECTED;
    }

    public int getYesValue() {
        return RadioValue.YES.value();
    }

    public int getNoValue() {
        return RadioValue.NO.value();
    }

    // ----------------------------------------------- Getter/Setter ----------------------------------------------- //
    public DecisionView getDecisionView() {
        return decisionView;
    }

    public void setDecisionView(DecisionView decisionView) {
        this.decisionView = decisionView;
    }

    public DecisionFollowConditionView getDecisionFollowConditionView() {
        return decisionFollowConditionView;
    }

    public void setDecisionFollowConditionView(DecisionFollowConditionView decisionFollowConditionView) {
        this.decisionFollowConditionView = decisionFollowConditionView;
    }

    public ApprovalHistoryView getApprovalHistoryView() {
        return approvalHistoryView;
    }

    public void setApprovalHistoryView(ApprovalHistoryView approvalHistoryView) {
        this.approvalHistoryView = approvalHistoryView;
    }

    public NewCreditDetailView getSelectedApproveCredit() {
        return selectedApproveCredit;
    }

    public void setSelectedApproveCredit(NewCreditDetailView selectedApproveCredit) {
        this.selectedApproveCredit = selectedApproveCredit;
    }

    public NewCollateralView getSelectedApproveCollateral() {
        return selectedApproveCollateral;
    }

    public void setSelectedApproveCollateral(NewCollateralView selectedApproveCollateral) {
        this.selectedApproveCollateral = selectedApproveCollateral;
    }

    public NewGuarantorDetailView getSelectedApproveGuarantor() {
        return selectedApproveGuarantor;
    }

    public void setSelectedApproveGuarantor(NewGuarantorDetailView selectedApproveGuarantor) {
        this.selectedApproveGuarantor = selectedApproveGuarantor;
    }

    public List<CreditRequestTypeView> getCreditRequestTypeViewList() {
        return creditRequestTypeViewList;
    }

    public void setCreditRequestTypeViewList(List<CreditRequestTypeView> creditRequestTypeViewList) {
        this.creditRequestTypeViewList = creditRequestTypeViewList;
    }

    public List<CountryView> getCountryViewList() {
        return countryViewList;
    }

    public void setCountryViewList(List<CountryView> countryViewList) {
        this.countryViewList = countryViewList;
    }

    public List<PrdGroupToPrdProgramView> getPrdGroupToPrdProgramViewList() {
        return prdGroupToPrdProgramViewList;
    }

    public void setPrdGroupToPrdProgramViewList(List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList) {
        this.prdGroupToPrdProgramViewList = prdGroupToPrdProgramViewList;
    }

    public List<PrdProgramToCreditTypeView> getPrdProgramToCreditTypeViewList() {
        return prdProgramToCreditTypeViewList;
    }

    public void setPrdProgramToCreditTypeViewList(List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList) {
        this.prdProgramToCreditTypeViewList = prdProgramToCreditTypeViewList;
    }

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public List<DisbursementTypeView> getDisbursementTypeViewList() {
        return disbursementTypeViewList;
    }

    public void setDisbursementTypeViewList(List<DisbursementTypeView> disbursementTypeViewList) {
        this.disbursementTypeViewList = disbursementTypeViewList;
    }

    public List<CustomerInfoView> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<CustomerInfoView> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public boolean isModeEditCredit() {
        return modeEditCredit;
    }

    public void setModeEditCredit(boolean modeEditCredit) {
        this.modeEditCredit = modeEditCredit;
    }

    public boolean isModeEditCollateral() {
        return modeEditCollateral;
    }

    public void setModeEditCollateral(boolean modeEditCollateral) {
        this.modeEditCollateral = modeEditCollateral;
    }

    public boolean isModeEditGuarantor() {
        return modeEditGuarantor;
    }

    public void setModeEditGuarantor(boolean modeEditGuarantor) {
        this.modeEditGuarantor = modeEditGuarantor;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRowIndexCredit() {
        return rowIndexCredit;
    }

    public void setRowIndexCredit(int rowIndexCredit) {
        this.rowIndexCredit = rowIndexCredit;
    }

    public int getRowIndexGuarantor() {
        return rowIndexGuarantor;
    }

    public void setRowIndexGuarantor(int rowIndexGuarantor) {
        this.rowIndexGuarantor = rowIndexGuarantor;
    }

    public List<PotentialCollateralView> getPotentialCollViewList() {
        return potentialCollViewList;
    }

    public void setPotentialCollViewList(List<PotentialCollateralView> potentialCollViewList) {
        this.potentialCollViewList = potentialCollViewList;
    }

    public List<CollateralTypeView> getCollateralTypeViewList() {
        return collateralTypeViewList;
    }

    public void setCollateralTypeViewList(List<CollateralTypeView> collateralTypeViewList) {
        this.collateralTypeViewList = collateralTypeViewList;
    }

    public List<SubCollateralTypeView> getSubCollateralTypeViewList() {
        return subCollateralTypeViewList;
    }

    public void setSubCollateralTypeViewList(List<SubCollateralTypeView> subCollateralTypeViewList) {
        this.subCollateralTypeViewList = subCollateralTypeViewList;
    }

    public List<CustomerInfoView> getCollateralOwnerUwAllList() {
        return collateralOwnerUwAllList;
    }

    public void setCollateralOwnerUwAllList(List<CustomerInfoView> collateralOwnerUwAllList) {
        this.collateralOwnerUwAllList = collateralOwnerUwAllList;
    }

    public List<MortgageTypeView> getMortgageTypeViewList() {
        return mortgageTypeViewList;
    }

    public void setMortgageTypeViewList(List<MortgageTypeView> mortgageTypeViewList) {
        this.mortgageTypeViewList = mortgageTypeViewList;
    }

    public List<NewCollateralSubView> getRelatedWithAllList() {
        return relatedWithAllList;
    }

    public void setRelatedWithAllList(List<NewCollateralSubView> relatedWithAllList) {
        this.relatedWithAllList = relatedWithAllList;
    }

    public int getRowIndexCollHead() {
        return rowIndexCollHead;
    }

    public void setRowIndexCollHead(int rowIndexCollHead) {
        this.rowIndexCollHead = rowIndexCollHead;
    }

    public int getRowIndexSubColl() {
        return rowIndexSubColl;
    }

    public void setRowIndexSubColl(int rowIndexSubColl) {
        this.rowIndexSubColl = rowIndexSubColl;
    }

    public NewCollateralHeadView getSelectedApproveCollHead() {
        return selectedApproveCollHead;
    }

    public void setSelectedApproveCollHead(NewCollateralHeadView selectedApproveCollHead) {
        this.selectedApproveCollHead = selectedApproveCollHead;
    }

    public NewCollateralSubView getSelectedApproveSubColl() {
        return selectedApproveSubColl;
    }

    public void setSelectedApproveSubColl(NewCollateralSubView selectedApproveSubColl) {
        this.selectedApproveSubColl = selectedApproveSubColl;
    }

    public List<LoanPurposeView> getLoanPurposeViewList() {
        return loanPurposeViewList;
    }

    public void setLoanPurposeViewList(List<LoanPurposeView> loanPurposeViewList) {
        this.loanPurposeViewList = loanPurposeViewList;
    }

    public int getRowIndexCollateral() {
        return rowIndexCollateral;
    }

    public void setRowIndexCollateral(int rowIndexCollateral) {
        this.rowIndexCollateral = rowIndexCollateral;
    }

    public List<ProposeCreditDetailView> getCollateralCreditTypeList() {
        return collateralCreditTypeList;
    }

    public void setCollateralCreditTypeList(List<ProposeCreditDetailView> collateralCreditTypeList) {
        this.collateralCreditTypeList = collateralCreditTypeList;
    }

    public List<ProposeCreditDetailView> getSelectedCollateralCrdTypeItems() {
        return selectedCollateralCrdTypeItems;
    }

    public void setSelectedCollateralCrdTypeItems(List<ProposeCreditDetailView> selectedCollateralCrdTypeItems) {
        this.selectedCollateralCrdTypeItems = selectedCollateralCrdTypeItems;
    }

    public List<ProposeCreditDetailView> getSelectedGuarantorCrdTypeItems() {
        return selectedGuarantorCrdTypeItems;
    }

    public void setSelectedGuarantorCrdTypeItems(List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems) {
        this.selectedGuarantorCrdTypeItems = selectedGuarantorCrdTypeItems;
    }

    public List<ProposeCreditDetailView> getGuarantorCreditTypeList() {
        return guarantorCreditTypeList;
    }

    public void setGuarantorCreditTypeList(List<ProposeCreditDetailView> guarantorCreditTypeList) {
        this.guarantorCreditTypeList = guarantorCreditTypeList;
    }

    public boolean isModeEditSubColl() {
        return modeEditSubColl;
    }

    public void setModeEditSubColl(boolean modeEditSubColl) {
        this.modeEditSubColl = modeEditSubColl;
    }

    public int getRowIndexDecisionFollowCondition() {
        return rowIndexDecisionFollowCondition;
    }

    public void setRowIndexDecisionFollowCondition(int rowIndexDecisionFollowCondition) {
        this.rowIndexDecisionFollowCondition = rowIndexDecisionFollowCondition;
    }

    public boolean isModeEditReducePricing() {
        return modeEditReducePricing;
    }

    public void setModeEditReducePricing(boolean modeEditReducePricing) {
        this.modeEditReducePricing = modeEditReducePricing;
    }

    public boolean isModeEditReduceFrontEndFee() {
        return modeEditReduceFrontEndFee;
    }

    public void setModeEditReduceFrontEndFee(boolean modeEditReduceFrontEndFee) {
        this.modeEditReduceFrontEndFee = modeEditReduceFrontEndFee;
    }

    public boolean isCannotEditStandard() {
        return cannotEditStandard;
    }

    public void setCannotEditStandard(boolean cannotEditStandard) {
        this.cannotEditStandard = cannotEditStandard;
    }

    public boolean isRoleBDM() {
        return roleBDM;
    }

    public void setRoleBDM(boolean roleBDM) {
        this.roleBDM = roleBDM;
    }

    public boolean isRoleZM_RGM() {
        return roleZM_RGM;
    }

    public void setRoleZM_RGM(boolean roleZM_RGM) {
        this.roleZM_RGM = roleZM_RGM;
    }

    public boolean isRoleUW() {
        return roleUW;
    }

    public void setRoleUW(boolean roleUW) {
        this.roleUW = roleUW;
    }

    public BaseRate getStandardBasePriceDlg() {
        return standardBasePriceDlg;
    }

    public void setStandardBasePriceDlg(BaseRate standardBasePriceDlg) {
        this.standardBasePriceDlg = standardBasePriceDlg;
    }

    public BigDecimal getStandardInterestDlg() {
        return standardInterestDlg;
    }

    public void setStandardInterestDlg(BigDecimal standardInterestDlg) {
        this.standardInterestDlg = standardInterestDlg;
    }

    public BaseRate getSuggestBasePriceDlg() {
        return suggestBasePriceDlg;
    }

    public void setSuggestBasePriceDlg(BaseRate suggestBasePriceDlg) {
        this.suggestBasePriceDlg = suggestBasePriceDlg;
    }

    public BigDecimal getSuggestInterestDlg() {
        return suggestInterestDlg;
    }

    public void setSuggestInterestDlg(BigDecimal suggestInterestDlg) {
        this.suggestInterestDlg = suggestInterestDlg;
    }

    public List<FollowConditionView> getFollowConditionViewList() {
        return followConditionViewList;
    }

    public void setFollowConditionViewList(List<FollowConditionView> followConditionViewList) {
        this.followConditionViewList = followConditionViewList;
    }

    public boolean isFlagComs() {
        return flagComs;
    }

    public void setFlagComs(boolean flagComs) {
        this.flagComs = flagComs;
    }

}