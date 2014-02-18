package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.DecisionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.DecisionTransform;
import com.clevel.selos.transform.DisbursementTypeTransform;
import com.clevel.selos.transform.LoanPurposeTransform;
import com.clevel.selos.transform.ProductTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
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
    DecisionControl decisionControl;
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
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
    DecisionDAO decisionDAO;
    @Inject
    CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    DisbursementTypeDAO disbursementDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    SpecialProgramDAO specialProgramDAO;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;

    @Inject
    LoanPurposeDAO loanPurposeDAO;

    //Transform
    @Inject
    DecisionTransform decisionTransform;
    @Inject
    ProductTransform productTransform;
    @Inject
    DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    LoanPurposeTransform loanPurposeTransform;

    //Session
    private long workCaseId;

    //User Role
    private boolean roleBDM;
    private boolean roleZM_RGM;
    private boolean roleUW;

    //Main Model View
    private DecisionView decisionView;
    private FollowUpConditionView followUpConditionView;
    private ApprovalHistory approvalHistory;

    private BasicInfoView basicInfoView;
    private SpecialProgram specialProgramBasicInfo;
    private TCGView tcgView;
    private int applyTCG;

    private List<ProposeCreditDetailView> sharedProposeCreditTypeList;
    // View Selected for Add/Edit/Delete
    private NewCreditDetailView selectedAppProposeCredit;
    private NewCollateralView selectedAppProposeCollateral;
    private NewCollateralSubView selectedAppSubCollateral;
    private NewGuarantorDetailView selectedAppProposeGuarantor;

    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private ProductGroup productGroup;

    // Retrieve Price/Fee
    private int creditCustomerType;
    private CreditRequestType creditRequestType;
    private Country country;

    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;

    private NewCreditDetailView newCreditDetailView;
    private NewCreditDetailView proposeCreditDetailSelected;
    private NewCreditTierDetailView newCreditTierDetailView;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;
    private int seq;
    private HashMap<Integer, Integer> hashSeqCredit;
//    private BigDecimal suggestPrice;
//    private BigDecimal standardPrice;
    private boolean modeEditReduceFront;
    private BigDecimal reducePrice;
    private boolean reducePricePanelRendered;
    private boolean canAddTier;

    // for control Propose Collateral
    private NewCollateralView newCollateralView;
    private NewCollateralView selectCollateralDetailView;
    private NewCollateralHeadView newCollateralHeadView;
    private NewCollateralHeadView collateralHeaderDetailItem;
    private NewCollateralSubView newCollateralSubView;
    private NewCollateralSubView subCollateralDetailItem;
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    private List<NewCollateralSubView> newCollateralSubViewList;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;

    // Propose/Approve Credit
    private BaseRate standardBasePriceDlg;
    private BigDecimal standardInterestDlg;
    private BaseRate suggestBasePriceDlg;
    private BigDecimal suggestInterestDlg;

    private boolean modeEditReducePricing;
    private boolean modeEditReduceFrontEndFee;
    private boolean cannotEditStandard;

    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList;
    private List<BaseRate> baseRateList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<DisbursementTypeView> disbursementTypeViewList;
    private int rowIndexCredit;

    // Propose/Approve - Collateral
    private List<PotentialCollateral> potentialCollateralList;
    private List<CollateralType> collateralTypeList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageType> mortgageTypeList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<ProposeCreditDetailView> collateralCreditTypeList;
    private List<ProposeCreditDetailView> selectedCollateralCrdTypeItems;
    private List<NewCreditDetailView> collCrdTypePrev;
    private int rowIndexCollateral;
    private int rowIndexCollHead;
    private int rowIndexSubColl;

    // Propose/Approve - Guarantor
    private List<CustomerInfoView> guarantorList;
    private List<ProposeCreditDetailView> guarantorCreditTypeList;
    private List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems;
    private List<ProposeCreditDetailView> guarantorCrdTypeItemsTmp;
    private int rowIndexGuarantor;

    // FollowUp Condition
    private int rowIndexFollowUpCondition;

    //Message Dialog
    private String messageHeader;
    private String message;
    private boolean messageErr;

    // Mode
    enum ModeForButton {
        ADD, EDIT
    }

    private ModeForButton modeForButton;
    private boolean modeEditCredit;
    private boolean modeEditCollateral;
    private boolean modeEditSubColl;
    private boolean modeEditGuarantor;

    private NewCreditFacilityView newCreditFacilityView;

    public Decision() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

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

        decisionView = decisionTransform.getDecisionView(decisionDAO.findByWorkCaseId(workCaseId));
        if (decisionView == null || decisionView.getId() == 0) {
            decisionView = new DecisionView();
            newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
//            log.info("newCreditFacilityView :: {}", newCreditFacilityView.getId());
//            log.info("newCreditFacilityView :: getNewCreditDetailViewList :: {}", newCreditFacilityView.getNewCreditDetailViewList().size());
//            log.info("newCreditFacilityView :: getNewConditionDetailViewList :: {}", newCreditFacilityView.getNewConditionDetailViewList().size());
//            log.info("newCreditFacilityView :: getNewGuarantorDetailViewList :: {}", newCreditFacilityView.getNewGuarantorDetailViewList().size());

            if (newCreditFacilityView != null) {
                setDataFromNewCreditFacility();
            }
        }


        basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        if (basicInfoView != null) {
            productGroup = basicInfoView.getProductGroup();
            specialProgramBasicInfo = basicInfoView.getSpecialProgram();
        }

        tcgView = tcgInfoControl.getTcgView(workCaseId);
        if (tcgView != null) {
            applyTCG = tcgView.getTCG();
        }

        guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);

        log.info("guarantorList size :: {}", guarantorList.size());
        if (guarantorList == null) {
            guarantorList = new ArrayList<CustomerInfoView>();
        }

        collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
        log.info("collateralOwnerUwAllList size :: {}", collateralOwnerUwAllList.size());
        if (collateralOwnerUwAllList == null) {
            collateralOwnerUwAllList = new ArrayList<CustomerInfoView>();
        }

//        if (collateralOwnerUW == null) {
//            collateralOwnerUW = new CustomerInfoView();
//        }

        if (newCreditFacilityView == null) {
            newCreditFacilityView = new NewCreditFacilityView();
            cannotEditStandard = true;
        }

        if (creditRequestTypeList == null) {
            creditRequestTypeList = new ArrayList<CreditRequestType>();
        }

        if (countryList == null) {
            countryList = new ArrayList<Country>();
        }

        if (newCreditDetailView == null) {
            newCreditDetailView = new NewCreditDetailView();
            seq = 0;
        }

        if (productProgramList == null) {
            productProgramList = new ArrayList<ProductProgram>();
        }

        if (creditTypeList == null) {
            creditTypeList = new ArrayList<CreditType>();
        }

        if (disbursementTypeViewList == null) {
            disbursementTypeViewList = new ArrayList<DisbursementTypeView>();
        }

        if (newGuarantorDetailView == null) {
            newGuarantorDetailView = new NewGuarantorDetailView();
        }

        if (newCollateralView == null) {
            newCollateralView = new NewCollateralView();
        }

        if (newCollateralSubView == null) {
            newCollateralSubView = new NewCollateralSubView();
        }

        if (subCollateralTypeList == null) {
            subCollateralTypeList = new ArrayList<SubCollateralType>();
        }

        if (collateralTypeList == null) {
            collateralTypeList = new ArrayList<CollateralType>();
        }

//        if(headCollateralTypeList == null){
//            headCollateralTypeList = new ArrayList<CollateralType>();
//        }

        if (potentialCollateralList == null) {
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        if (prdGroupToPrdProgramList == null) {
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        if (baseRateList == null) {
            baseRateList = new ArrayList<BaseRate>();
        }

        if (loanPurposeViewList == null) {
            loanPurposeViewList = new ArrayList<LoanPurposeView>();
        }

        if (mortgageTypeList == null) {
            mortgageTypeList = new ArrayList<MortgageType>();
        }

        modeEditReducePricing = false;
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        mortgageTypeList = mortgageTypeDAO.findAll();
        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();
        collateralTypeList = collateralTypeDAO.findAll();
//        headCollateralTypeList  = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        baseRateList = baseRateDAO.findAll();

        selectedAppProposeCredit = new NewCreditDetailView();
        selectedAppProposeCollateral = new NewCollateralView();
        selectedAppSubCollateral = new NewCollateralSubView();
        selectedAppProposeGuarantor = new NewGuarantorDetailView();

        // Initial sequence number credit
        seq = 1;
        hashSeqCredit = new HashMap<Integer, Integer>();

        // Retrieve Pricing/Fee
        creditCustomerType = RadioValue.NOT_SELECTED.value();
        creditRequestType = new CreditRequestType();
        country = new Country();

        followUpConditionView = new FollowUpConditionView();
        approvalHistory = new ApprovalHistory();
        approvalHistory.setAction("Approve CA");
        approvalHistory.setApprover(decisionControl.getApprover());

        // Initial Standard & Suggest BaseRate to Approved Credit Dialog
        standardBasePriceDlg = new BaseRate();
        standardInterestDlg = BigDecimal.ZERO;
        suggestBasePriceDlg = new BaseRate();
        suggestInterestDlg = BigDecimal.ZERO;
    }

    public void setDataFromNewCreditFacility() {
        Cloner cloner = new Cloner();

        decisionView.setProposeCreditList(newCreditFacilityView.getNewCreditDetailViewList());
        decisionView.setProposeGuarantorList(newCreditFacilityView.getNewGuarantorDetailViewList());
        decisionView.setProposeCollateralList(newCreditFacilityView.getNewCollateralViewList());
        decisionView.setProposeFeeInfoList(newCreditFacilityView.getNewFeeDetailViewList());
        decisionView.setProposeTotalCreditLimit(newCreditFacilityView.getTotalPropose());
        decisionView.setProposeTotalGuaranteeAmt(newCreditFacilityView.getTotalGuaranteeAmount());

        // Approved Credit
        decisionView.setApproveCreditList(cloner.deepClone(newCreditFacilityView.getNewCreditDetailViewList()));
        decisionView.setApproveCollateralList(cloner.deepClone(newCreditFacilityView.getNewCollateralViewList()));
        decisionView.setApproveGuarantorList(cloner.deepClone(newCreditFacilityView.getNewGuarantorDetailViewList()));
    }

    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee()");
        // test create data from retrieving
        // todo: retrieve base rate by criteria?
        BaseRate baseRate = baseRateDAO.findById(1);
        NewCreditDetailView creditDetailRetrieve = new NewCreditDetailView();
        // todo: change standard to tier
        /*creditDetailRetrieve.setStandardBasePrice(baseRate);
        creditDetailRetrieve.setStandardInterest(BigDecimal.valueOf(-1.75));

        if (ValidationUtil.isValueLessThanZero(creditDetailRetrieve.getStandardInterest())) {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " " + creditDetailRetrieve.getStandardInterest());
        } else {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " + " + creditDetailRetrieve.getStandardInterest());
        }

        /*//****** tier test create ********//*/
        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newCreditTierDetailView = new NewCreditTierDetailView();
        newCreditTierDetailView.setStandardBasePrice(baseRate);
        newCreditTierDetailView.setStandardPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setStandardInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setSuggestBasePrice(baseRate);
        newCreditTierDetailView.setSuggestPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setSuggestInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setFinalBasePrice(baseRate);
        newCreditTierDetailView.setFinalPriceRate(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setFinalInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setTenor(6);
        newCreditTierDetailView.setInstallment(BigDecimal.valueOf(2000000));
        newCreditTierDetailView.setCanEdit(false);
        newCreditTierDetailViewList.add(newCreditTierDetailView);

        for (NewCreditDetailView proposeCreditDetail : decisionView.getApproveCreditList()) {
            proposeCreditDetail.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            proposeCreditDetail.setStandardBasePrice(baseRate);
//            proposeCreditDetail.setSuggestBasePrice(baseRate); //todo: change suggest to tier
            proposeCreditDetail.setStandardInterest(creditDetailRetrieve.getStandardInterest());
//            proposeCreditDetail.setSuggestInterest(creditDetailRetrieve.getSuggestInterest()); //todo: change suggest to tier
        }*/
    }

    // ---------- Propose Credit Dialog ---------- //
    public void onAddAppProposeCredit() {
        log.debug("onAddAppProposeCredit()");
        if (CreditCustomerType.NOT_SELECTED.value() == creditCustomerType) {
            messageHeader = "Warning !!!!";
            message = "Credit Customer Type is required";
            messageErr = true;
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } else {
            selectedAppProposeCredit = new NewCreditDetailView();

            if (baseRateList != null && !baseRateList.isEmpty()) {
//                selectedAppProposeCredit.setStandardBasePrice(baseRateList.get(0)); // todo: change standard to tier
//                selectedAppProposeCredit.setSuggestBasePrice(baseRateList.get(0)); //todo: change suggest to tier
            }

            if (loanPurposeViewList != null && !loanPurposeViewList.isEmpty()) {
                selectedAppProposeCredit.setLoanPurposeView(loanPurposeViewList.get(0));
            }

            if (disbursementTypeViewList != null && !disbursementTypeViewList.isEmpty()) {
                selectedAppProposeCredit.setDisbursementTypeView(disbursementTypeViewList.get(0));
            }

            onChangeRequestType();
            onChangeSuggestValue();

            modeEditCredit = false;
            modeForButton = ModeForButton.ADD;
//            RequestContext.getCurrentInstance().execute("proposeCreditDlg.show()");
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        }
    }

    public void onEditAppProposeCredit() {
        log.debug("onEditAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        ProductProgramView productProgramView = selectedAppProposeCredit.getProductProgramView();

        //prdProgramToCreditTypeViewList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgramView);
        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(productProgramView);

        modeEditCredit = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCredit() {
        log.debug("onDeleteAppProposeCredit() rowIndexCredit: {}", rowIndexCredit);
//        int numberOfUsed = hashSeqCredit.get(selectedAppProposeCredit.getSeq());

//        log.debug("seq: {}, number of used = {}", selectedAppProposeCredit.getSeq(), numberOfUsed);
//        if (numberOfUsed == 0) {
            decisionView.getApproveCreditList().remove(rowIndexCredit);

//            BigDecimal sumTotalCreditLimit = BigDecimal.ZERO;
//            for (NewCreditDetailView newCreditDetailView : Util.safetyList(decisionView.getApproveCreditList())) {
//                sumTotalCreditLimit = Util.add(sumTotalCreditLimit, newCreditDetailView.getLimit());
//            }
//            decisionView.setApproveTotalCreditLimit(sumTotalCreditLimit);
//        } else {
//            messageHeader = msg.get("app.propose.exception");
//            message = msg.get("app.propose.error.delete.credit");
//            messageErr = true;
//            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//        }

    }

    public void onSaveAppProposeCredit() {
        log.debug("onSaveAppProposeCredit()");
        boolean success = false;

        if (selectedAppProposeCredit.getProductProgramView().getId() != 0
                && selectedAppProposeCredit.getCreditTypeView().getId() != 0
                && selectedAppProposeCredit.getLoanPurposeView().getId() != 0
                && selectedAppProposeCredit.getDisbursementTypeView().getId() != 0) {

            ProductProgram productProgram = productProgramDAO.findById(selectedAppProposeCredit.getProductProgramView().getId());
            CreditType creditType = creditTypeDAO.findById(selectedAppProposeCredit.getCreditTypeView().getId());
            LoanPurpose loanPurpose = loanPurposeDAO.findById(selectedAppProposeCredit.getLoanPurposeView().getId());
            DisbursementType disbursement = disbursementDAO.findById(selectedAppProposeCredit.getDisbursementTypeView().getId());

            if (modeEditCredit) {
                NewCreditDetailView creditDetailEdit = decisionView.getApproveCreditList().get(rowIndexCredit);
                creditDetailEdit.setProductProgramView(productTransform.transformToView(productProgram));
                creditDetailEdit.setCreditTypeView(productTransform.transformToView(creditType));
                creditDetailEdit.setRequestType(selectedAppProposeCredit.getRequestType());
                creditDetailEdit.setRefinance(selectedAppProposeCredit.getRefinance());
                creditDetailEdit.setProductCode(selectedAppProposeCredit.getProductCode());
                creditDetailEdit.setProjectCode(selectedAppProposeCredit.getProjectCode());
                creditDetailEdit.setLimit(selectedAppProposeCredit.getLimit());
                creditDetailEdit.setPCEPercent(selectedAppProposeCredit.getPCEPercent());
                creditDetailEdit.setPCEAmount(selectedAppProposeCredit.getPCEAmount());
                creditDetailEdit.setReducePriceFlag(selectedAppProposeCredit.isReducePriceFlag());
                creditDetailEdit.setReduceFrontEndFee(selectedAppProposeCredit.isReduceFrontEndFee());
//                creditDetailEdit.setStandardBasePrice(selectedAppProposeCredit.getStandardBasePrice()); // todo: change standard to tier
//                creditDetailEdit.setStandardInterest(selectedAppProposeCredit.getStandardInterest()); // todo: change standard to tier
//                creditDetailEdit.setSuggestBasePrice(selectedAppProposeCredit.getSuggestBasePrice()); //todo: change suggest to tier
//                creditDetailEdit.setSuggestInterest(selectedAppProposeCredit.getSuggestInterest()); //todo: change suggest to tier
                creditDetailEdit.setFrontEndFee(selectedAppProposeCredit.getFrontEndFee());
                creditDetailEdit.setLoanPurposeView(loanPurposeTransform.transformToView(loanPurpose));
                creditDetailEdit.setRemark(selectedAppProposeCredit.getRemark());
                creditDetailEdit.setDisbursementTypeView(disbursementTypeTransform.transformToView(disbursement));
                creditDetailEdit.setHoldLimitAmount(selectedAppProposeCredit.getHoldLimitAmount());
                creditDetailEdit.setNewCreditTierDetailViewList(selectedAppProposeCredit.getNewCreditTierDetailViewList());

                success = true;
            } else {
                // Add New
                NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
                creditDetailAdd.setProductProgramView(productTransform.transformToView(productProgram));
                creditDetailAdd.setCreditTypeView(productTransform.transformToView(creditType));
                creditDetailAdd.setRequestType(selectedAppProposeCredit.getRequestType());
                creditDetailAdd.setRefinance(selectedAppProposeCredit.getRefinance());
                creditDetailAdd.setProductCode(selectedAppProposeCredit.getProductCode());
                creditDetailAdd.setProjectCode(selectedAppProposeCredit.getProjectCode());
                creditDetailAdd.setLimit(selectedAppProposeCredit.getLimit());
                creditDetailAdd.setPCEPercent(selectedAppProposeCredit.getPCEPercent());
                creditDetailAdd.setPCEAmount(selectedAppProposeCredit.getPCEAmount());
                creditDetailAdd.setReducePriceFlag(selectedAppProposeCredit.isReducePriceFlag());
                creditDetailAdd.setReduceFrontEndFee(selectedAppProposeCredit.isReduceFrontEndFee());
//                creditDetailAdd.setStandardBasePrice(selectedAppProposeCredit.getStandardBasePrice()); // todo: change standard to tier
//                creditDetailAdd.setStandardInterest(selectedAppProposeCredit.getStandardInterest()); // todo: change standard to tier
//                creditDetailAdd.setSuggestBasePrice(selectedAppProposeCredit.getSuggestBasePrice()); //todo: change suggest to tier
//                creditDetailAdd.setSuggestInterest(selectedAppProposeCredit.getSuggestInterest()); //todo: change suggest to tier
                creditDetailAdd.setFrontEndFee(selectedAppProposeCredit.getFrontEndFee());
                creditDetailAdd.setLoanPurposeView(loanPurposeTransform.transformToView(loanPurpose));
                creditDetailAdd.setRemark(selectedAppProposeCredit.getRemark());
                creditDetailAdd.setDisbursementTypeView(disbursementTypeTransform.transformToView(disbursement));
                creditDetailAdd.setHoldLimitAmount(selectedAppProposeCredit.getHoldLimitAmount());
                creditDetailAdd.setNewCreditTierDetailViewList(selectedAppProposeCredit.getNewCreditTierDetailViewList());
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

            BigDecimal sumTotalCreditLimit = BigDecimal.ZERO;
            for (NewCreditDetailView newCreditDetailView : Util.safetyList(decisionView.getApproveCreditList())) {
                sumTotalCreditLimit = Util.add(sumTotalCreditLimit, newCreditDetailView.getLimit());
            }
            decisionView.setApproveTotalCreditLimit(sumTotalCreditLimit);

            hashSeqCredit.put(seq, 0);
            seq++;
            log.debug("seq++ of credit after add complete Approve Propose Credit :: {}", seq);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onChangeRequestType() {
        log.debug("onChangeRequestType() requestType = {}", selectedAppProposeCredit.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (RequestTypes.CHANGE.value() == selectedAppProposeCredit.getRequestType()) {   //change
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
            selectedAppProposeCredit.setProductProgramView(new ProductProgramView());
            cannotEditStandard = false;
        } else if (RequestTypes.NEW.value() == selectedAppProposeCredit.getRequestType()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
                selectedAppProposeCredit.setCreditTypeView(new CreditTypeView());
            }
            cannotEditStandard = true;
        }
    }

    public void onChangeProductProgram() {
        log.debug("onChangeProductProgram() productProgram.id = {}", selectedAppProposeCredit.getProductProgramView().getId());
        selectedAppProposeCredit.setProductCode("");
        selectedAppProposeCredit.setProjectCode("");

        //ProductProgram productProgram = productProgramDAO.findById(selectedAppProposeCredit.getProductProgram().getId());
        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(selectedAppProposeCredit.getProductProgramView());;
        selectedAppProposeCredit.setCreditTypeView(new CreditTypeView());
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType() creditType.id: {}", selectedAppProposeCredit.getCreditTypeView().getId());
        if ((selectedAppProposeCredit.getProductProgramView().getId() != 0) && (selectedAppProposeCredit.getCreditTypeView().getId() != 0)) {
            ProductFormulaView productFormulaView = productControl.getProductFormulaView(newCreditDetailView.getCreditTypeView().getId(),
                    newCreditDetailView.getProductProgramView().getId(),
                    newCreditFacilityView.getCreditCustomerType(), specialProgramBasicInfo.getId(), applyTCG);
            if (productFormulaView != null) {
                log.debug("onChangeCreditType :::: productFormula : {}", productFormulaView.getId());
                newCreditDetailView.setProductCode(productFormulaView.getProductCode());
                newCreditDetailView.setProjectCode(productFormulaView.getProjectCode());
                log.info("productFormula.getReduceFrontEndFee() ::: {}", productFormulaView.getReduceFrontEndFee());
                log.info("productFormula.getReducePricing() ::: {}", productFormulaView.getReducePricing());

                modeEditReducePricing = productFormulaView.getReducePricing() == 1 ? true : false;
                modeEditReduceFrontEndFee = productFormulaView.getReduceFrontEndFee() == 1 ? true : false;
            }
        }
    }

    public void onChangeSuggestValue() {
        /*log.info("onChangeSuggestValue(standardBaseRate.id: {}, standardInterest: {}, suggestBaseRate.id: {}, suggestInterest: {})", //todo: change suggest to tier
                selectedAppProposeCredit.getStandardBasePrice().getId(), selectedAppProposeCredit.getStandardInterest(),
                selectedAppProposeCredit.getSuggestBasePrice().getId(), selectedAppProposeCredit.getSuggestInterest());

        Object[] values = creditFacProposeControl.findFinalPriceRate(
                selectedAppProposeCredit.getStandardBasePrice().getId(),
                selectedAppProposeCredit.getStandardInterest(),
                selectedAppProposeCredit.getSuggestBasePrice().getId(),
                selectedAppProposeCredit.getSuggestInterest()
        );

        finalBaseRate = (BaseRate) values[0];
        finalInterest = (BigDecimal) values[1];
        finalPriceRate = (String) values[2];
        standardBaseRate = (BaseRate) values[3];
        standardPriceLabel = (String) values[4];
        suggestBaseRate = (BaseRate) values[5];
        suggestPriceLabel = (String) values[6];*/
    }

    public void onAddTierInfo() {
//        log.debug("onAddTierInfo(standardBaseRate.id: {}, standardInterest: {}, suggestBaseRate.id: {}, suggestInterest: {}, finalBaseRate.id: {}, finalInterest: {})",
//                standardBaseRate.getId(), selectedAppProposeCredit.getStandardInterest(),
//                suggestBaseRate.getId(), selectedAppProposeCredit.getSuggestInterest(),
//                finalBaseRate.getId(), finalInterest);

//        NewCreditTierDetailView newCreditTierDetail = new NewCreditTierDetailView();
//        newCreditTierDetail.setFinalBasePrice(finalBaseRate);
//        newCreditTierDetail.setFinalInterest(finalInterest);
//        newCreditTierDetail.setFinalPriceLabel(finalPriceRate);
//        newCreditTierDetail.setStandardBasePrice(standardBaseRate);
//        newCreditTierDetail.setStandardPriceLabel(standardPriceLabel);
//        newCreditTierDetail.setSuggestBasePrice(suggestBaseRate);
//        newCreditTierDetail.setSuggestPriceLabel(suggestPriceLabel);
//        newCreditTierDetail.setCanEdit(true);
//
//        if (selectedAppProposeCredit.getNewCreditTierDetailViewList() != null) {
//            selectedAppProposeCredit.getNewCreditTierDetailViewList().add(newCreditTierDetail);
//        } else {
//            List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
//            newCreditTierDetailViewList.add(newCreditTierDetail);
//            selectedAppProposeCredit.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
//        }
    }

    public void onDeleteTierInfo(int rowIndex) {
        selectedAppProposeCredit.getNewCreditTierDetailViewList().remove(rowIndex);
    }

    // ---------- Propose Collateral Dialog ---------- //
    public void onEditAppProposeCollateral() {
        log.debug("onEditAppProposeCollateral() rowIndexCollateral: {}, selectedAppProposeCollateral: {}", rowIndexCollateral, selectedAppProposeCollateral);
        if (selectedAppProposeCollateral.getProposeCreditDetailViewList() != null && selectedAppProposeCollateral.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedCollateralCrdTypeItems = selectedAppProposeCollateral.getProposeCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            collateralCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);
        }

        modeEditCollateral = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() rowIndexCollateral: {}", rowIndexCollateral);
        if (selectedAppProposeCollateral.getProposeCreditDetailViewList() != null && selectedAppProposeCollateral.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectedAppProposeCollateral.getProposeCreditDetailViewList().size(); i++) {
                ProposeCreditDetailView collCredit = selectedAppProposeCollateral.getProposeCreditDetailViewList().get(i);
//                if (hashSeqCredit.get(collCredit.getSeq()) > 0) {
//                    log.debug("coll seq: {} = {} - 1", collCredit.getSeq(), hashSeqCredit.get(collCredit.getSeq()));
//                    hashSeqCredit.put(collCredit.getSeq(), hashSeqCredit.get(collCredit.getSeq()) - 1);
//                    log.debug("coll seq: {} = {}", collCredit.getSeq(), hashSeqCredit.get(collCredit.getSeq()));
//                }
            }
        }
        decisionView.getApproveCollateralList().remove(rowIndexCollateral);
    }

    public void onSaveProposeCollInfo() {
        log.debug("onSaveProposeCollInfo()");
        boolean success = false;

        log.debug("===> Edit - Collateral: {}", selectedAppProposeCollateral);
        NewCollateralView collateralInfoEdit = decisionView.getApproveCollateralList().get(rowIndexCollateral);
        collateralInfoEdit.setJobID(selectedAppProposeCollateral.getJobID());
        collateralInfoEdit.setAppraisalDate(selectedAppProposeCollateral.getAppraisalDate());
        collateralInfoEdit.setAadDecision(selectedAppProposeCollateral.getAadDecision());
        collateralInfoEdit.setAadDecisionReason(selectedAppProposeCollateral.getAadDecisionReason());
        collateralInfoEdit.setAadDecisionReasonDetail(selectedAppProposeCollateral.getAadDecisionReasonDetail());
        collateralInfoEdit.setUsage(selectedAppProposeCollateral.getUsage());
        collateralInfoEdit.setTypeOfUsage(selectedAppProposeCollateral.getTypeOfUsage());
        collateralInfoEdit.setUwDecision(selectedAppProposeCollateral.getUwDecision());
        collateralInfoEdit.setUwRemark(selectedAppProposeCollateral.getUwRemark());
        collateralInfoEdit.setMortgageCondition(selectedAppProposeCollateral.getMortgageCondition());
        collateralInfoEdit.setMortgageConditionDetail(selectedAppProposeCollateral.getMortgageConditionDetail());
        collateralInfoEdit.setBdmComments(selectedAppProposeCollateral.getBdmComments());
        collateralInfoEdit.setNewCollateralHeadViewList(selectedAppProposeCollateral.getNewCollateralHeadViewList());

        if (selectedCollateralCrdTypeItems != null && selectedCollateralCrdTypeItems.size() > 0) {
            collateralInfoEdit.getProposeCreditDetailViewList().clear();
            for (ProposeCreditDetailView creditTypeItem : selectedCollateralCrdTypeItems) {
                collateralInfoEdit.getProposeCreditDetailViewList().add(creditTypeItem);

            }

            success = true;
            log.debug("Success: Edit Collateral from ApproveCollateralList");
        } else {
            messageHeader = "Error Message";
            message = "Non selected Credit Type!";
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("Failed: Can not edit Collateral from ApproveCollateralList because non selected credit type!");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onCallRetrieveAppraisalReportInfo() {
        log.debug("onCallRetrieveAppraisalReportInfo()");
    }

    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral()");
        selectedAppSubCollateral = new NewCollateralSubView();
        modeEditSubColl = false;
    }

    public void onEditSubCollateral() {
        log.debug("onEditSubCollateral()");
        modeEditSubColl = true;
    }

    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral() rowIndexCollateral: {}, rowIndexCollHead: {}, rowIndexSubColl: {}",
                rowIndexCollateral, rowIndexCollHead, rowIndexSubColl);
        decisionView.getApproveCollateralList().get(rowIndexCollateral)
                .getNewCollateralHeadViewList().get(rowIndexCollHead)
                .getNewCollateralSubViewList().remove(rowIndexSubColl);
    }

    public void onSaveSubCollateral() {
        log.debug("onSaveSubCollateral()");
        boolean success = false;

        if (modeEditSubColl) {
            log.debug("===> Edit - SubCollateral: {}", selectedAppSubCollateral);
            NewCollateralSubView subCollEdit = selectedAppProposeCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead)
                    .getNewCollateralSubViewList().get(rowIndexSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedAppSubCollateral.getSubCollateralType().getId());
            subCollEdit.setSubCollateralType(subCollateralType);
            subCollEdit.setTitleDeed(selectedAppSubCollateral.getTitleDeed());
            subCollEdit.setAddress(selectedAppSubCollateral.getAddress());
            subCollEdit.setLandOffice(selectedAppSubCollateral.getLandOffice());
            subCollEdit.setCollateralOwnerAAD(selectedAppSubCollateral.getCollateralOwnerAAD());
            subCollEdit.setCollateralOwnerUWList(selectedAppSubCollateral.getCollateralOwnerUWList());
            subCollEdit.setMortgageList(selectedAppSubCollateral.getMortgageList());
            subCollEdit.setRelatedWithList(selectedAppSubCollateral.getRelatedWithList());
            subCollEdit.setAppraisalValue(selectedAppSubCollateral.getAppraisalValue());
            subCollEdit.setMortgageValue(selectedAppSubCollateral.getMortgageValue());

            success = true;
        } else {
            //Add New
            log.debug("===> Add New - SubCollateral: {}", selectedAppSubCollateral);
            NewCollateralSubView subCollAdd = new NewCollateralSubView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedAppSubCollateral.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(selectedAppSubCollateral.getTitleDeed());
            subCollAdd.setAddress(selectedAppSubCollateral.getAddress());
            subCollAdd.setLandOffice(selectedAppSubCollateral.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(selectedAppSubCollateral.getCollateralOwnerAAD());
            subCollAdd.setCollateralOwnerUWList(selectedAppSubCollateral.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(selectedAppSubCollateral.getMortgageList());
            subCollAdd.setRelatedWithList(selectedAppSubCollateral.getRelatedWithList());
            subCollAdd.setAppraisalValue(selectedAppSubCollateral.getAppraisalValue());
            subCollAdd.setMortgageValue(selectedAppSubCollateral.getMortgageValue());

            if (selectedAppProposeCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList() == null) {
                selectedAppProposeCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).setNewCollateralSubViewList(new ArrayList<NewCollateralSubView>());
            }

            selectedAppProposeCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead)
                    .getNewCollateralSubViewList().add(subCollAdd);

            success = true;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", selectedAppSubCollateral.getCollateralOwnerUW().getId());
        if (selectedAppSubCollateral.getCollateralOwnerUW().getId() == 0) {
            log.error("Can not add CollateralOwnerUw because id = 0!");
            return;
        }

        CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(selectedAppSubCollateral.getCollateralOwnerUW());
        if (selectedAppSubCollateral.getCollateralOwnerUWList() == null) {
            selectedAppSubCollateral.setCollateralOwnerUWList(new ArrayList<CustomerInfoView>());
        }
        selectedAppSubCollateral.getCollateralOwnerUWList().add(collateralOwnerAdd);
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        log.debug("onDeleteCollateralOwnerUW(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getCollateralOwnerUWList().remove(rowIndex);
    }

    public void onAddMortgageType() {
        log.debug("onAddMortgageType() mortgageType.id: {}", selectedAppSubCollateral.getMortgageType().getId());
        if (selectedAppSubCollateral.getMortgageType().getId() == 0) {
            log.error("Can not add MortgageType because id = 0!");
            return;
        }

        MortgageType mortgageType = mortgageTypeDAO.findById(selectedAppSubCollateral.getMortgageType().getId());
        if (selectedAppSubCollateral.getMortgageList() == null) {
            selectedAppSubCollateral.setMortgageList(new ArrayList<MortgageType>());
        }
        selectedAppSubCollateral.getMortgageList().add(mortgageType);
    }

    public void onDeleteMortgageType(int rowIndex) {
        log.debug("onDeleteMortgageType(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getMortgageList().remove(rowIndex);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() selectedAppSubCollateral.relatedWithId = {}", selectedAppSubCollateral.getRelatedWithId());
        if (selectedAppSubCollateral.getRelatedWithId() == 0) {
            log.error("Can not add RelatedWith because id = 0!");
            return;
        }

        NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(selectedAppSubCollateral.getRelatedWithId());
        if (selectedAppSubCollateral.getRelatedWithList() != null) {
            selectedAppSubCollateral.setRelatedWithList(new ArrayList<NewCollateralSubView>());
        }
        selectedAppSubCollateral.getRelatedWithList().add(relatedWith);
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

    public void onDeleteRelatedWith(int rowIndex) {
        log.debug("onDeleteRelatedWith(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getRelatedWithList().remove(rowIndex);
    }

    // ---------- Propose Guarantor Dialog ---------- //
    public void onAddAppProposeGuarantor() {
        log.debug("onAddAppProposeGuarantor()");
        selectedAppProposeGuarantor = new NewGuarantorDetailView();
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        Cloner cloner = new Cloner();
        guarantorCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);

        modeEditGuarantor = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onEditAppProposeGuarantor() {
        log.debug("onEditAppProposeGuarantor() selectedAppProposeGuarantor: {}", selectedAppProposeGuarantor);
        if (selectedAppProposeGuarantor.getProposeCreditDetailViewList() != null && selectedAppProposeGuarantor.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedGuarantorCrdTypeItems = selectedAppProposeGuarantor.getProposeCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            guarantorCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);
            for (ProposeCreditDetailView creditTypeFromAll : guarantorCreditTypeList) {
                for (ProposeCreditDetailView creditTypeFromSelected : selectedAppProposeGuarantor.getProposeCreditDetailViewList()) {
                    if (creditTypeFromAll.getSeq() == creditTypeFromSelected.getSeq()) {
                        creditTypeFromAll.setGuaranteeAmount(creditTypeFromSelected.getGuaranteeAmount());
                    }
                }
            }
        }

        modeEditGuarantor = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onSaveGuarantorInfo() {
        log.debug("onSaveGuarantorInfo()");
        log.debug("selectedGuarantorCrdTypeItems.size() = {}", selectedGuarantorCrdTypeItems.size());
        log.debug("selectedGuarantorCrdTypeItems: {}", selectedGuarantorCrdTypeItems);

        boolean success = false;
        BigDecimal sumGuaranteeAmtPerCrdType = BigDecimal.ZERO;

        if (modeEditGuarantor) {
            log.debug("===> Edit - Guarantor: {}", selectedAppProposeGuarantor);
            NewGuarantorDetailView guarantorDetailEdit = decisionView.getApproveGuarantorList().get(rowIndexGuarantor);
            guarantorDetailEdit.setGuarantorName(getByIdFromGuarantorList(selectedAppProposeGuarantor.getGuarantorName().getId()));
            guarantorDetailEdit.setTcgLgNo(selectedAppProposeGuarantor.getTcgLgNo());

            // needed to select credit type items
            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
                guarantorDetailEdit.getProposeCreditDetailViewList().clear();
                // seq usage
                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                    guarantorDetailEdit.getProposeCreditDetailViewList().add(creditTypeItem);
                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));

                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
                }
                guarantorDetailEdit.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);

                for (ProposeCreditDetailView creditOfList : guarantorCreditTypeList) {
                    boolean isSelect = false;
                    for (ProposeCreditDetailView creditSelected : selectedGuarantorCrdTypeItems) {
                        if (creditOfList.getSeq() == creditSelected.getSeq()) {
                            isSelect = true;
                            break;
                        }
                    }

                }

                success = true;
                log.debug("Success: Edit Guarantor from ApproveGuarantorList");
            } else {
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                log.error("Failed: Can not edit Guarantor from ApproveGuarantorList because non selected credit type!");
            }
        } else {
            // Add New
            log.debug("===> Add New - Guarantor: {}", selectedAppProposeGuarantor);
            NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
            //guarantorDetailAdd.setApproved(selectedAppProposeGuarantor.getApproved());
            guarantorDetailAdd.setGuarantorName(getByIdFromGuarantorList(selectedAppProposeGuarantor.getGuarantorName().getId()));
            guarantorDetailAdd.setTcgLgNo(selectedAppProposeGuarantor.getTcgLgNo());

            // needed to select credit type items
            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    guarantorDetailAdd.getProposeCreditDetailViewList().add(creditTypeItem);

                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
//                    hashSeqCredit.put(creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()) + 1);
                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));

                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
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
                log.debug("Success: Add new Guarantor to ApproveGuarantorList.");
            } else {
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                log.error("Failed: Can not add new Guarantor to ApproveGuarantorList because non selected credit type!");
            }
        }

        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onDeleteAppProposeGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() rowIndexGuarantor: {}", rowIndexGuarantor);
        if (selectedAppProposeGuarantor.getProposeCreditDetailViewList() != null && selectedAppProposeGuarantor.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectedAppProposeGuarantor.getProposeCreditDetailViewList().size(); i++) {
                ProposeCreditDetailView guarantorCredit = selectedAppProposeGuarantor.getProposeCreditDetailViewList().get(i);
//                if (hashSeqCredit.get(guarantorCredit.getSeq()) > 0) {
//                    log.debug("guarantor seq: {} = {} - 1", guarantorCredit.getSeq(), hashSeqCredit.get(guarantorCredit.getSeq()));
//                    hashSeqCredit.put(guarantorCredit.getSeq(), hashSeqCredit.get(guarantorCredit.getSeq()) - 1);
//                    log.debug("guarantor seq: {} = {}", guarantorCredit.getSeq(), hashSeqCredit.get(guarantorCredit.getSeq()));
//                }
            }
        }
        decisionView.getApproveGuarantorList().remove(rowIndexGuarantor);
        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
    }

    // ---------- FollowUp Condition & (Save/Cancel) Decision ---------- //
    public void onAddFollowUpCondition() {
        log.debug("onAddFollowUpCondition()");
        if (decisionView.getFollowUpConditionList() != null) {
            decisionView.getFollowUpConditionList().add(followUpConditionView);
        } else {
            List<FollowUpConditionView> followUpConditionList = new ArrayList<FollowUpConditionView>();
            followUpConditionList.add(followUpConditionView);
            decisionView.setFollowUpConditionList(followUpConditionList);
        }
        // Clear form
        followUpConditionView = new FollowUpConditionView();
    }

    public void onDeleteFollowUpCondition() {
        log.debug("onDeleteFollowUpCondition() rowIndexFollowUpCondition: {}", rowIndexFollowUpCondition);
        decisionView.getFollowUpConditionList().remove(rowIndexFollowUpCondition);
    }

    public void onSave() {
        log.debug("onSave()");
        exSummaryControl.calForDecision(workCaseId);
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    private void generateSeqFromCreditList(List<NewCreditDetailView> newCreditDetailViewList) {
        int seq = 0;
        for (NewCreditDetailView newCreditDetailView : Util.safetyList(newCreditDetailViewList)) {
            newCreditDetailView.setSeq(seq);
            seq++;
        }
    }

    private CustomerInfoView getByIdFromGuarantorList(long id) {
        CustomerInfoView returnGuarantor = new CustomerInfoView();
        for (CustomerInfoView guarantor : Util.safetyList(guarantorList)) {
            if (guarantor.getId() == id) {
                returnGuarantor = guarantor;
                break;
            }
        }
        return returnGuarantor;
    }

    // ----------------------------------------------- Getter/Setter -----------------------------------------------//
    public DecisionView getDecisionView() {
        return decisionView;
    }

    public void setDecisionView(DecisionView decisionView) {
        this.decisionView = decisionView;
    }

    public FollowUpConditionView getFollowUpConditionView() {
        return followUpConditionView;
    }

    public void setFollowUpConditionView(FollowUpConditionView followUpConditionView) {
        this.followUpConditionView = followUpConditionView;
    }

    public ApprovalHistory getApprovalHistory() {
        return approvalHistory;
    }

    public void setApprovalHistory(ApprovalHistory approvalHistory) {
        this.approvalHistory = approvalHistory;
    }

    public NewCreditDetailView getSelectedAppProposeCredit() {
        return selectedAppProposeCredit;
    }

    public void setSelectedAppProposeCredit(NewCreditDetailView selectedAppProposeCredit) {
        this.selectedAppProposeCredit = selectedAppProposeCredit;
    }

    public NewCollateralView getSelectedAppProposeCollateral() {
        return selectedAppProposeCollateral;
    }

    public void setSelectedAppProposeCollateral(NewCollateralView selectedAppProposeCollateral) {
        this.selectedAppProposeCollateral = selectedAppProposeCollateral;
    }

    public NewGuarantorDetailView getSelectedAppProposeGuarantor() {
        return selectedAppProposeGuarantor;
    }

    public void setSelectedAppProposeGuarantor(NewGuarantorDetailView selectedAppProposeGuarantor) {
        this.selectedAppProposeGuarantor = selectedAppProposeGuarantor;
    }

    public List<CreditRequestType> getCreditRequestTypeList() {
        return creditRequestTypeList;
    }

    public void setCreditRequestTypeList(List<CreditRequestType> creditRequestTypeList) {
        this.creditRequestTypeList = creditRequestTypeList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
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

    public int getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(int creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public CreditRequestType getCreditRequestType() {
        return creditRequestType;
    }

    public void setCreditRequestType(CreditRequestType creditRequestType) {
        this.creditRequestType = creditRequestType;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }

    public List<SubCollateralType> getSubCollateralTypeList() {
        return subCollateralTypeList;
    }

    public void setSubCollateralTypeList(List<SubCollateralType> subCollateralTypeList) {
        this.subCollateralTypeList = subCollateralTypeList;
    }

    public List<CustomerInfoView> getCollateralOwnerUwAllList() {
        return collateralOwnerUwAllList;
    }

    public void setCollateralOwnerUwAllList(List<CustomerInfoView> collateralOwnerUwAllList) {
        this.collateralOwnerUwAllList = collateralOwnerUwAllList;
    }

    public List<MortgageType> getMortgageTypeList() {
        return mortgageTypeList;
    }

    public void setMortgageTypeList(List<MortgageType> mortgageTypeList) {
        this.mortgageTypeList = mortgageTypeList;
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

    public NewCollateralSubView getSelectedAppSubCollateral() {
        return selectedAppSubCollateral;
    }

    public void setSelectedAppSubCollateral(NewCollateralSubView selectedAppSubCollateral) {
        this.selectedAppSubCollateral = selectedAppSubCollateral;
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

    public List<ProposeCreditDetailView> getsharedProposeCreditTypeList() {
        return sharedProposeCreditTypeList;
    }

    public void setsharedProposeCreditTypeList(List<ProposeCreditDetailView> sharedProposeCreditTypeList) {
        this.sharedProposeCreditTypeList = sharedProposeCreditTypeList;
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

    public int getRowIndexFollowUpCondition() {
        return rowIndexFollowUpCondition;
    }

    public void setRowIndexFollowUpCondition(int rowIndexFollowUpCondition) {
        this.rowIndexFollowUpCondition = rowIndexFollowUpCondition;
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

    public List<ProductProgram> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<ProductProgram> productProgramList) {
        this.productProgramList = productProgramList;
    }

    public List<CreditType> getCreditTypeList() {
        return creditTypeList;
    }

    public void setCreditTypeList(List<CreditType> creditTypeList) {
        this.creditTypeList = creditTypeList;
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
}