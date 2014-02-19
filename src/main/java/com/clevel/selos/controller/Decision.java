package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.DecisionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
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

    
    private long workCaseId;
    
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
    private boolean messageErr;

    //Main Model View
    private DecisionView decisionView;
    private FollowUpConditionView followUpConditionView;
    private ApprovalHistory approvalHistory;

    private SpecialProgram specialProgramBasicInfo;
    private TCGView tcgView;
    private int applyTCG;

    private List<ProposeCreditDetailView> sharedProposeCreditTypeList;
    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private ProductGroup productGroup;

    private NewCreditDetailView newCreditDetailView;
    private NewCreditDetailView proposeCreditDetailSelected;
    private NewCreditTierDetailView newCreditTierDetailView;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;
    private int seq;
    private HashMap<Integer, Integer> hashSeqCredit;
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

    // Retrieve Price/Fee
    private CreditCustomerType creditCustomerType;
    private CreditRequestType creditRequestType;
    private Country country;
    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;

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
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList;
    private List<BaseRate> baseRateList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<DisbursementTypeView> disbursementTypeViewList;

    // Approve Collateral
    private NewCollateralView selectedApproveCollateral;
    private NewCollateralSubView selectedApproveSubColl;
    private int rowIndexCollateral;
    private int rowIndexCollHead;
    private int rowIndexSubColl;
    private List<PotentialCollateral> potentialCollateralList;
    private List<CollateralType> collateralTypeList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageType> mortgageTypeList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<ProposeCreditDetailView> collateralCreditTypeList;
    private List<ProposeCreditDetailView> selectedCollateralCrdTypeItems;
    private List<NewCreditDetailView> collCrdTypePrev;

    // Approve Guarantor
    private NewGuarantorDetailView selectedApproveGuarantor;
    private int rowIndexGuarantor;
    private List<CustomerInfoView> guarantorList;
    private List<ProposeCreditDetailView> guarantorCreditTypeList;
    private List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems;

    // FollowUp Condition
    private int rowIndexFollowUpCondition;

    // List one time query on init
    private List<PrdGroupToPrdProgram> _prdGroupToPrdProgramAll;
    private List<PrdGroupToPrdProgram> _prdGroupToPrdProgramPropose;

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
        }

        // New Credit Facility
        newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
        if (newCreditFacilityView != null) {
            Cloner cloner = new Cloner();

            decisionView.setProposeCreditList(newCreditFacilityView.getNewCreditDetailViewList());
            decisionView.setProposeGuarantorList(newCreditFacilityView.getNewGuarantorDetailViewList());
            decisionView.setProposeCollateralList(newCreditFacilityView.getNewCollateralViewList());
            decisionView.setProposeFeeInfoList(newCreditFacilityView.getNewFeeDetailViewList());
            decisionView.setProposeTotalCreditLimit(newCreditFacilityView.getTotalPropose());
            decisionView.setProposeTotalGuaranteeAmt(newCreditFacilityView.getTotalGuaranteeAmount());

            // clone Credit info
            decisionView.setApproveCreditList(cloner.deepClone(newCreditFacilityView.getNewCreditDetailViewList()));
            decisionView.setApproveCollateralList(cloner.deepClone(newCreditFacilityView.getNewCollateralViewList()));
            decisionView.setApproveGuarantorList(cloner.deepClone(newCreditFacilityView.getNewGuarantorDetailViewList()));
        }


        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        if (basicInfoView != null) {
            productGroup = basicInfoView.getProductGroup();
            specialProgramBasicInfo = basicInfoView.getSpecialProgram();
        }

        tcgView = tcgInfoControl.getTcgView(workCaseId);
        if (tcgView != null) {
            applyTCG = tcgView.getTCG();
        }

//        if (collateralOwnerUW == null) {
//            collateralOwnerUW = new CustomerInfoView();
//        }

        if (newCreditFacilityView == null) {
            newCreditFacilityView = new NewCreditFacilityView();
            cannotEditStandard = true;
        }

        if (newCreditDetailView == null) {
            newCreditDetailView = new NewCreditDetailView();
            seq = 0;
        }

        if (productProgramList == null) {
            productProgramList = new ArrayList<ProductProgram>();
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

        // ========== Retrieve Pricing/Fee ========== //
        creditCustomerType = CreditCustomerType.NOT_SELECTED;

        creditRequestType = new CreditRequestType();
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        if (creditRequestTypeList == null)
            creditRequestTypeList = new ArrayList<CreditRequestType>();

        country = new Country();
        countryList = countryDAO.findAll();
        if (countryList == null)
            countryList = new ArrayList<Country>();
        // ================================================== //


        // ========== Approve Credit Dialog ========== //
        selectedApproveCredit = new NewCreditDetailView();

        standardBasePriceDlg = new BaseRate();
        standardInterestDlg = BigDecimal.ZERO;
        suggestBasePriceDlg = new BaseRate();
        suggestInterestDlg = BigDecimal.ZERO;

        _prdGroupToPrdProgramAll = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
        _prdGroupToPrdProgramPropose = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);

        if (prdGroupToPrdProgramList == null)
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();

        creditTypeList = creditTypeDAO.findAll();
        if (creditTypeList == null)
            creditTypeList = new ArrayList<CreditType>();

        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        if (loanPurposeViewList == null)
            loanPurposeViewList = new ArrayList<LoanPurposeView>();

        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();
        if (disbursementTypeViewList == null)
            disbursementTypeViewList = new ArrayList<DisbursementTypeView>();

        baseRateList = baseRateDAO.findAll();
        if (baseRateList == null)
            baseRateList = new ArrayList<BaseRate>();
        // ================================================== //


        // ========== Approve Collateral Dialog ========== //
        selectedApproveCollateral = new NewCollateralView();
        
        potentialCollateralList = potentialCollateralDAO.findAll();
        if (potentialCollateralList == null)
            potentialCollateralList = new ArrayList<PotentialCollateral>();

        collateralTypeList = collateralTypeDAO.findAll();

        if (collateralTypeList == null)
            collateralTypeList = new ArrayList<CollateralType>();

        //headCollateralTypeList  = collateralTypeDAO.findAll();
        //if(headCollateralTypeList == null){
//            headCollateralTypeList = new ArrayList<CollateralType>();
//        }
        // ================================================== //


        // ========== Sub Collateral Dialog ========== //
        selectedApproveSubColl = new NewCollateralSubView();

        collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
        if (collateralOwnerUwAllList == null)
            collateralOwnerUwAllList = new ArrayList<CustomerInfoView>();

        mortgageTypeList = mortgageTypeDAO.findAll();
        if (mortgageTypeList == null)
            mortgageTypeList = new ArrayList<MortgageType>();

        relatedWithAllList = new ArrayList<NewCollateralSubView>();

        // ================================================== //


        // ========== Approve Guarantor Dialog ========== //
        selectedApproveGuarantor = new NewGuarantorDetailView();
        
        guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
        if (guarantorList == null)
            guarantorList = new ArrayList<CustomerInfoView>();
        // ================================================== //

        // Initial sequence number credit
        seq = 1;
        hashSeqCredit = new HashMap<Integer, Integer>();

        followUpConditionView = new FollowUpConditionView();
        approvalHistory = new ApprovalHistory();
        approvalHistory.setAction("Approve CA");
        approvalHistory.setApprover(decisionControl.getApprover());

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

    // ==================== Approve Credit - Actions ==================== //
    public void onAddApproveCredit() {
        log.debug("onAddApproveCredit()");
        selectedApproveCredit = new NewCreditDetailView();

        if (baseRateList != null && !baseRateList.isEmpty()) {
            standardBasePriceDlg = baseRateList.get(0);
            suggestBasePriceDlg = baseRateList.get(0);
        }

        onChangeRequestType();

        modeEditCredit = false;
        modeForButton = ModeForButton.ADD;
//            RequestContext.getCurrentInstance().execute("proposeCreditDlg.show()");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onEditApproveCredit() {
        log.debug("onEditApproveCredit() selectedApproveCredit: {}", selectedApproveCredit);
        ProductProgramView productProgramView = selectedApproveCredit.getProductProgramView();

        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(productProgramView);

        modeEditCredit = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteApproveCredit() {
        log.debug("onDeleteApproveCredit() rowIndexCredit: {}", rowIndexCredit);
//        int numberOfUsed = hashSeqCredit.get(selectedApproveCredit.getSeq());

//        log.debug("seq: {}, number of used = {}", selectedApproveCredit.getSeq(), numberOfUsed);
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
//                creditDetailEdit.setStandardBasePrice(selectedApproveCredit.getStandardBasePrice()); // todo: change standard to tier
//                creditDetailEdit.setStandardInterest(selectedApproveCredit.getStandardInterest()); // todo: change standard to tier
//                creditDetailEdit.setSuggestBasePrice(selectedApproveCredit.getSuggestBasePrice()); //todo: change suggest to tier
//                creditDetailEdit.setSuggestInterest(selectedApproveCredit.getSuggestInterest()); //todo: change suggest to tier
                creditDetailEdit.setFrontEndFee(selectedApproveCredit.getFrontEndFee());
                creditDetailEdit.setLoanPurposeView(loanPurposeView);
                creditDetailEdit.setRemark(selectedApproveCredit.getRemark());
                creditDetailEdit.setDisbursementTypeView(disbursementTypeView);
                creditDetailEdit.setHoldLimitAmount(selectedApproveCredit.getHoldLimitAmount());
                creditDetailEdit.setNewCreditTierDetailViewList(selectedApproveCredit.getNewCreditTierDetailViewList());

                success = true;
            } else {
                // Add New
                NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
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
//                creditDetailAdd.setStandardBasePrice(selectedApproveCredit.getStandardBasePrice()); // todo: change standard to tier
//                creditDetailAdd.setStandardInterest(selectedApproveCredit.getStandardInterest()); // todo: change standard to tier
//                creditDetailAdd.setSuggestBasePrice(selectedApproveCredit.getSuggestBasePrice()); //todo: change suggest to tier
//                creditDetailAdd.setSuggestInterest(selectedApproveCredit.getSuggestInterest()); //todo: change suggest to tier
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
        log.debug("onChangeRequestType() requestType = {}", selectedApproveCredit.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (RequestTypes.CHANGE.value() == selectedApproveCredit.getRequestType()) {   //change
            prdGroupToPrdProgramList = _prdGroupToPrdProgramAll;
            selectedApproveCredit.setProductProgramView(new ProductProgramView());
            cannotEditStandard = false;
        }
        else if (RequestTypes.NEW.value() == selectedApproveCredit.getRequestType()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = _prdGroupToPrdProgramPropose;
                selectedApproveCredit.setCreditTypeView(new CreditTypeView());
            }
            cannotEditStandard = true;
        }
    }

    public void onChangeProductProgram() {
        log.debug("onChangeProductProgram() productProgram.id = {}", selectedApproveCredit.getProductProgramView().getId());
        selectedApproveCredit.setProductCode("");
        selectedApproveCredit.setProjectCode("");

        //ProductProgram productProgram = productProgramDAO.findById(selectedApproveCredit.getProductProgram().getId());
        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(selectedApproveCredit.getProductProgramView());;
        selectedApproveCredit.setCreditTypeView(new CreditTypeView());
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType() creditType.id: {}", selectedApproveCredit.getCreditTypeView().getId());
        if ((selectedApproveCredit.getProductProgramView().getId() != 0) && (selectedApproveCredit.getCreditTypeView().getId() != 0)) {
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
                selectedApproveCredit.getStandardBasePrice().getId(), selectedApproveCredit.getStandardInterest(),
                selectedApproveCredit.getSuggestBasePrice().getId(), selectedApproveCredit.getSuggestInterest());

        Object[] values = creditFacProposeControl.findFinalPriceRate(
                selectedApproveCredit.getStandardBasePrice().getId(),
                selectedApproveCredit.getStandardInterest(),
                selectedApproveCredit.getSuggestBasePrice().getId(),
                selectedApproveCredit.getSuggestInterest()
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
//                standardBaseRate.getId(), selectedApproveCredit.getStandardInterest(),
//                suggestBaseRate.getId(), selectedApproveCredit.getSuggestInterest(),
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
//        if (selectedApproveCredit.getNewCreditTierDetailViewList() != null) {
//            selectedApproveCredit.getNewCreditTierDetailViewList().add(newCreditTierDetail);
//        } else {
//            List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
//            newCreditTierDetailViewList.add(newCreditTierDetail);
//            selectedApproveCredit.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
//        }
    }

    public void onDeleteTierInfo(int rowIndex) {
        selectedApproveCredit.getNewCreditTierDetailViewList().remove(rowIndex);
    }

    // ==================== Approve Collateral - Actions ==================== //
    public void onEditAppProposeCollateral() {
        log.debug("onEditAppProposeCollateral() rowIndexCollateral: {}, selectedApproveCollateral: {}", rowIndexCollateral, selectedApproveCollateral);
        if (selectedApproveCollateral.getProposeCreditDetailViewList() != null && selectedApproveCollateral.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedCollateralCrdTypeItems = selectedApproveCollateral.getProposeCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            collateralCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);
        }

        modeEditCollateral = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() rowIndexCollateral: {}", rowIndexCollateral);
        if (selectedApproveCollateral.getProposeCreditDetailViewList() != null && selectedApproveCollateral.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectedApproveCollateral.getProposeCreditDetailViewList().size(); i++) {
                ProposeCreditDetailView collCredit = selectedApproveCollateral.getProposeCreditDetailViewList().get(i);
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

        log.debug("===> Edit - Collateral: {}", selectedApproveCollateral);
        NewCollateralView collateralInfoEdit = decisionView.getApproveCollateralList().get(rowIndexCollateral);
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
        collateralInfoEdit.setNewCollateralHeadViewList(selectedApproveCollateral.getNewCollateralHeadViewList());

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

    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral() : {}", rowIndexCollHead);
        selectedApproveSubColl = new NewCollateralSubView();
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
            log.debug("===> Edit - SubCollateral: {}", selectedApproveSubColl);
            NewCollateralSubView subCollEdit = selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead)
                    .getNewCollateralSubViewList().get(rowIndexSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedApproveSubColl.getSubCollateralType().getId());
            subCollEdit.setSubCollateralType(subCollateralType);
            subCollEdit.setTitleDeed(selectedApproveSubColl.getTitleDeed());
            subCollEdit.setAddress(selectedApproveSubColl.getAddress());
            subCollEdit.setLandOffice(selectedApproveSubColl.getLandOffice());
            subCollEdit.setCollateralOwnerAAD(selectedApproveSubColl.getCollateralOwnerAAD());
            subCollEdit.setCollateralOwnerUWList(selectedApproveSubColl.getCollateralOwnerUWList());
            subCollEdit.setMortgageList(selectedApproveSubColl.getMortgageList());
            subCollEdit.setRelatedWithList(selectedApproveSubColl.getRelatedWithList());
            subCollEdit.setAppraisalValue(selectedApproveSubColl.getAppraisalValue());
            subCollEdit.setMortgageValue(selectedApproveSubColl.getMortgageValue());

            success = true;
        } else {
            //Add New
            log.debug("===> Add New - SubCollateral: {}", selectedApproveSubColl);
            NewCollateralSubView subCollAdd = new NewCollateralSubView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedApproveSubColl.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(selectedApproveSubColl.getTitleDeed());
            subCollAdd.setAddress(selectedApproveSubColl.getAddress());
            subCollAdd.setLandOffice(selectedApproveSubColl.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(selectedApproveSubColl.getCollateralOwnerAAD());
            subCollAdd.setCollateralOwnerUWList(selectedApproveSubColl.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(selectedApproveSubColl.getMortgageList());
            subCollAdd.setRelatedWithList(selectedApproveSubColl.getRelatedWithList());
            subCollAdd.setAppraisalValue(selectedApproveSubColl.getAppraisalValue());
            subCollAdd.setMortgageValue(selectedApproveSubColl.getMortgageValue());

            if (selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).getNewCollateralSubViewList() == null) {
                selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead).setNewCollateralSubViewList(new ArrayList<NewCollateralSubView>());
            }

            selectedApproveCollateral.getNewCollateralHeadViewList().get(rowIndexCollHead)
                    .getNewCollateralSubViewList().add(subCollAdd);

            success = true;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", selectedApproveSubColl.getCollateralOwnerUW().getId());
        if (selectedApproveSubColl.getCollateralOwnerUW().getId() == 0) {
            log.error("Can not add CollateralOwnerUw because id = 0!");
            return;
        }

        CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(selectedApproveSubColl.getCollateralOwnerUW());
        if (selectedApproveSubColl.getCollateralOwnerUWList() == null) {
            selectedApproveSubColl.setCollateralOwnerUWList(new ArrayList<CustomerInfoView>());
        }
        selectedApproveSubColl.getCollateralOwnerUWList().add(collateralOwnerAdd);
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        log.debug("onDeleteCollateralOwnerUW(rowIndex: {})", rowIndex);
        selectedApproveSubColl.getCollateralOwnerUWList().remove(rowIndex);
    }

    public void onAddMortgageType() {
        log.debug("onAddMortgageType() mortgageType.id: {}", selectedApproveSubColl.getMortgageType().getId());
        if (selectedApproveSubColl.getMortgageType().getId() == 0) {
            log.error("Can not add MortgageType because id = 0!");
            return;
        }

        MortgageType mortgageType = mortgageTypeDAO.findById(selectedApproveSubColl.getMortgageType().getId());
        if (selectedApproveSubColl.getMortgageList() == null) {
            selectedApproveSubColl.setMortgageList(new ArrayList<MortgageType>());
        }
        selectedApproveSubColl.getMortgageList().add(mortgageType);
    }

    public void onDeleteMortgageType(int rowIndex) {
        log.debug("onDeleteMortgageType(rowIndex: {})", rowIndex);
        selectedApproveSubColl.getMortgageList().remove(rowIndex);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() selectedApproveSubColl.relatedWithId = {}", selectedApproveSubColl.getRelatedWithId());
        if (selectedApproveSubColl.getRelatedWithId() == 0) {
            log.error("Can not add RelatedWith because id = 0!");
            return;
        }

        NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(selectedApproveSubColl.getRelatedWithId());
        if (selectedApproveSubColl.getRelatedWithList() != null) {
            selectedApproveSubColl.setRelatedWithList(new ArrayList<NewCollateralSubView>());
        }
        selectedApproveSubColl.getRelatedWithList().add(relatedWith);
    }

    public void onDeleteRelatedWith(int rowIndex) {
        log.debug("onDeleteRelatedWith(rowIndex: {})", rowIndex);
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
        Cloner cloner = new Cloner();
        guarantorCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);

        modeEditGuarantor = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onEditApproveGuarantor() {
        log.debug("onEditAppProposeGuarantor() selectedApproveGuarantor: {}", selectedApproveGuarantor);
        if (selectedApproveGuarantor.getProposeCreditDetailViewList() != null && selectedApproveGuarantor.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedGuarantorCrdTypeItems = selectedApproveGuarantor.getProposeCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            guarantorCreditTypeList = cloner.deepClone(sharedProposeCreditTypeList);
            for (ProposeCreditDetailView creditTypeFromAll : guarantorCreditTypeList) {
                for (ProposeCreditDetailView creditTypeFromSelected : selectedApproveGuarantor.getProposeCreditDetailViewList()) {
                    if (creditTypeFromAll.getSeq() == creditTypeFromSelected.getSeq()) {
                        creditTypeFromAll.setGuaranteeAmount(creditTypeFromSelected.getGuaranteeAmount());
                    }
                }
            }
        }

        modeEditGuarantor = true;
        modeForButton = ModeForButton.EDIT;
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
            guarantorDetailEdit.setGuarantorName(getByIdFromGuarantorList(selectedApproveGuarantor.getGuarantorName().getId()));
            guarantorDetailEdit.setTcgLgNo(selectedApproveGuarantor.getTcgLgNo());

//            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
//
//                List<ProposeCreditDetailView> newCreditTypeItems = new ArrayList<ProposeCreditDetailView>();
//                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
//                    newCreditTypeItems.add(creditTypeItem);
//                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
//
//                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
//                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
//                }
//                guarantorDetailEdit.setProposeCreditDetailViewList(newCreditTypeItems);
                guarantorDetailEdit.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);
                success = true;
//            } else {
//                log.error("Failed: Can not edit Guarantor from ApproveGuarantorList because non selected credit type!");
//                messageHeader = "Error Message";
//                message = "Non selected Credit Type!";
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            }

        } else {
            // Add New
            log.debug("===> Add New - Guarantor: {}", selectedApproveGuarantor);
            NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
            guarantorDetailAdd.setUwDecision(selectedApproveGuarantor.getUwDecision());
            guarantorDetailAdd.setGuarantorName(getByIdFromGuarantorList(selectedApproveGuarantor.getGuarantorName().getId()));
            guarantorDetailAdd.setTcgLgNo(selectedApproveGuarantor.getTcgLgNo());

//            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
//
//                if (guarantorDetailAdd.getProposeCreditDetailViewList() == null) {
//                    guarantorDetailAdd.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
//                }
//
//                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
//                    guarantorDetailAdd.getProposeCreditDetailViewList().add(creditTypeItem);
//                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
//
//                    log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
//                    log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
//                }

                guarantorDetailAdd.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);

                if (decisionView.getApproveGuarantorList() != null) {
                    decisionView.getApproveGuarantorList().add(guarantorDetailAdd);
                } else {
                    List<NewGuarantorDetailView> newApproveGuarantorList = new ArrayList<NewGuarantorDetailView>();
                    newApproveGuarantorList.add(guarantorDetailAdd);
                    decisionView.setApproveGuarantorList(newApproveGuarantorList);
                }

                success = true;
//            } else {
//                log.error("Failed: Can not add new Guarantor to ApproveGuarantorList because non selected credit type!");
//                messageHeader = "Error Message";
//                message = "Non selected Credit Type!";
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            }

        }

        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
        log.debug("success: {}", success);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onDeleteApproveGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() rowIndexGuarantor: {}", rowIndexGuarantor);

        if (selectedApproveGuarantor.getProposeCreditDetailViewList() != null && selectedApproveGuarantor.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectedApproveGuarantor.getProposeCreditDetailViewList().size(); i++) {
                ProposeCreditDetailView guarantorCredit = selectedApproveGuarantor.getProposeCreditDetailViewList().get(i);
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

    // ---------- FollowUp Condition - Action ---------- //
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

    // ----------------------------------------------- Private Methods ----------------------------------------------- //
    private ProductProgramView getProductProgramById(int id) {
        if (productProgramList == null || productProgramList.isEmpty() || id == 0) {
            return new ProductProgramView();
        }

        for (ProductProgram productProgram : productProgramList) {
            if (productProgram.getId() == id) {
                return productTransform.transformToView(productProgram);
            }
        }
        return new ProductProgramView();
    }

    private CreditTypeView getCreditTypeById(int id) {
        if (creditTypeList == null || creditTypeList.isEmpty() || id == 0) {
            return new CreditTypeView();
        }

        for (CreditType creditType : creditTypeList) {
            if (creditType.getId() == id) {
                return productTransform.transformToView(creditType);
            }
        }
        return new CreditTypeView();
    }

    private LoanPurposeView getLoanPurposeById(int id) {
        if (loanPurposeViewList == null || loanPurposeViewList.isEmpty() || id == 0) {
            return new LoanPurposeView();
        }

        LoanPurposeView returnLoanPurpose = new LoanPurposeView();
        for (LoanPurposeView loanPurposeView : loanPurposeViewList) {
            if (loanPurposeView.getId() == id) {
                returnLoanPurpose.setId(loanPurposeView.getId());
                returnLoanPurpose.setDescription(loanPurposeView.getDescription());
            }
        }
        return returnLoanPurpose;
    }

    private DisbursementTypeView getDisbursementTypeById(int id) {
        if (disbursementTypeViewList == null || disbursementTypeViewList.isEmpty() || id == 0) {
            return new DisbursementTypeView();
        }
        DisbursementTypeView returnDisbursementType = new DisbursementTypeView();
        for (DisbursementTypeView disbursementTypeView : disbursementTypeViewList) {
            if (disbursementTypeView.getId() == id) {
                returnDisbursementType.setId(disbursementTypeView.getId());
                returnDisbursementType.setDisbursement(disbursementTypeView.getDisbursement());
            }
        }
        return returnDisbursementType;
    }

    private CustomerInfoView getByIdFromGuarantorList(long id) {
        if (guarantorList == null || guarantorList.isEmpty() || id == 0) {
            return new CustomerInfoView();
        }

        CustomerInfoView returnCusInfoView = new CustomerInfoView();
        for (CustomerInfoView customerInfoView : Util.safetyList(guarantorList)) {
            if (customerInfoView.getId() == id) {
                returnCusInfoView.setId(customerInfoView.getId());
                returnCusInfoView.setFirstNameTh(customerInfoView.getFirstNameTh());
                returnCusInfoView.setFirstNameEn(customerInfoView.getFirstNameEn());
                returnCusInfoView.setLastNameTh(customerInfoView.getLastNameTh());
                returnCusInfoView.setLastNameEn(customerInfoView.getLastNameEn());
                returnCusInfoView.setTitleTh(customerInfoView.getTitleTh());
                returnCusInfoView.setTitleEn(customerInfoView.getTitleEn());
            }
        }
        return returnCusInfoView;
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

    public CreditCustomerType getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(CreditCustomerType creditCustomerType) {
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