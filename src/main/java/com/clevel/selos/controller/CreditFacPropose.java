package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.TCGDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.NewCollateralInfoTransform;
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
import java.util.Hashtable;
import java.util.List;


@ViewScoped
@ManagedBean(name = "creditFacPropose")
public class CreditFacPropose implements Serializable {
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

    private Long workCaseId;
    private User user;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private ModeForButton modeForSubColl;

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}

    private ModeForDB modeForDB;

    int rowIndex;
    int rowIndexGuarantor;
    int rowSubIndex;
    int rowCollHeadIndex;
    int rowIndexCollateral;

    private String messageHeader;
    private String message;
    private boolean messageErr;

    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;
    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private ProductGroup productGroup;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<Disbursement> disbursementList;
    private List<BaseRate> baseRateList;
    private NewCreditFacilityView newCreditFacilityView;
    private List<LoanPurpose> loanPurposeList;

    // case from select database must to transform to view before to use continue
    private BasicInfo basicInfo;
    private TCG tcg;
    private SpecialProgram specialProgramBasicInfo;
    private int applyTCG;

    //for control Propose Credit
    private NewCreditDetailView newCreditDetailView;
    private NewCreditDetailView proposeCreditDetailSelected;
    private NewCreditTierDetailView newCreditTierDetailView;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;
    private int seq;
    private Hashtable hashSeqCredit;
    private BigDecimal suggestPrice;
    private String suggestPriceLabel;
    private BigDecimal standardPrice;
    private String standardPriceLabel;
    private BaseRate finalBaseRate;
    private BigDecimal finalInterest;
    private String finalPriceRate;
    private boolean modeEditReducePricing;
    private boolean modeEditReduceFront;
    private BigDecimal reducePrice;
    private boolean reducePricePanelRendered;
    private boolean cannotEditStandard;

    // for control Propose Collateral
    private NewCollateralInfoView newCollateralInfoView;
    private NewCollateralInfoView selectCollateralDetailView;
    private NewCollateralHeadDetailView newCollateralHeadDetailView;
    private NewCollateralHeadDetailView collateralHeaderDetailItem;
    private NewSubCollateralDetailView newSubCollateralDetailView;
    private NewSubCollateralDetailView subCollateralDetailItem;
    private List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList;
    private List<NewSubCollateralDetailView> newSubCollateralDetailViewList;
    private List<MortgageType> mortgageTypeList;
    private List<NewSubCollateralDetailView> relatedWithAllList;
    private NewSubCollateralDetailView relatedWithSelected;
    private List<CustomerInfoView> collateralOwnerUwAllList;   // case from select database must to transform to view before to use continue
    private CustomerInfoView collateralOwnerUW;

    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;
    private List<CustomerInfoView> guarantorList;     // case from select database must to transform to view before to use continue

    private List<NewCreditDetailView> newCreditDetailListTemp;
    private List<NewCreditDetailView> newCollateralCreditDetailList;
    private List<NewCreditDetailView> newGuarantorCreditDetailList;
    private List<NewCreditDetailView> newCreditDetailList;

    // for  control Condition Information Dialog
    private NewConditionDetailView newConditionDetailView;
    private NewConditionDetailView selectConditionItem;


    @Inject
    UserDAO userDAO;
    @Inject
    CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    DisbursementDAO disbursementDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    NewCollateralInfoTransform collateralInfoTransform;
    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    LoanPurposeDAO loanPurposeDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    SpecialProgramDAO specialProgramDAO;
    @Inject
    TCGDAO tcgdao;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;

    public CreditFacPropose() {
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        //test
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", new Long(2));    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
        }

        if (workCaseId != null) {

            modeForDB = ModeForDB.ADD_DB;

            try {
                newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
                log.info("newCreditFacilityView.id ::: {}", newCreditFacilityView.getId());
                if (newCreditFacilityView != null) {
                    modeForDB = ModeForDB.EDIT_DB;
                }
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }

            log.info("onCreation :: modeForDB :: {}", modeForDB);

            basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            log.info("basicInfo:: {}", basicInfo.getId());
            log.info("basicInfo:: {}", basicInfo.getProductGroup());
            if (basicInfo == null) {
                productGroup = null;
                specialProgramBasicInfo = null;
            } else {
                log.info("basicInfo.id ::: {}", basicInfo.getId());
                productGroup = basicInfo.getProductGroup();
                specialProgramBasicInfo = basicInfo.getSpecialProgram();
            }

            tcg = tcgdao.findByWorkCaseId(workCaseId);

            if (tcg == null) {
                applyTCG = 0;
            } else {
                log.info("tcg.id ::: {}", tcg.getId());
                applyTCG = tcg.getTcgFlag();
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
        }

        if (collateralOwnerUW == null) {
            collateralOwnerUW = new CustomerInfoView();
        }

        if (newCreditFacilityView == null) {
            newCreditFacilityView = new NewCreditFacilityView();
            reducePricePanelRendered = false;
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
            hashSeqCredit = new Hashtable<String, String>();
        }

        if (productProgramList == null) {
            productProgramList = new ArrayList<ProductProgram>();
        }

        if (creditTypeList == null) {
            creditTypeList = new ArrayList<CreditType>();
        }

        if (disbursementList == null) {
            disbursementList = new ArrayList<Disbursement>();
        }

        if (newConditionDetailView == null) {
            newConditionDetailView = new NewConditionDetailView();
        }

        if (newGuarantorDetailView == null) {
            newGuarantorDetailView = new NewGuarantorDetailView();
        }

        if (newCollateralInfoView == null) {
            newCollateralInfoView = new NewCollateralInfoView();
        }

        if (newSubCollateralDetailView == null) {
            newSubCollateralDetailView = new NewSubCollateralDetailView();
        }

        if (subCollateralTypeList == null) {
            subCollateralTypeList = new ArrayList<SubCollateralType>();
        }

        if (collateralTypeList == null) {
            collateralTypeList = new ArrayList<CollateralType>();
        }

        if (potentialCollateralList == null) {
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        if (prdGroupToPrdProgramList == null) {
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        if (baseRateList == null) {
            baseRateList = new ArrayList<BaseRate>();
        }

        if (loanPurposeList == null) {
            loanPurposeList = new ArrayList<LoanPurpose>();
        }

        if (mortgageTypeList == null) {
            mortgageTypeList = new ArrayList<MortgageType>();
        }

        modeEditReducePricing = false;
        modeEditReduceFront = false;
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        mortgageTypeList = mortgageTypeDAO.findAll();
        loanPurposeList = loanPurposeDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        baseRateList = baseRateDAO.findAll();
        modeEdit = false;
        suggestPrice = BigDecimal.ZERO;
        standardPrice = BigDecimal.ZERO;
        finalBaseRate = new BaseRate();
        finalInterest = BigDecimal.ZERO;
        suggestPriceLabel = "";
        standardPriceLabel = "";
        finalPriceRate = "";

    }

    //Call  BRMS to get data Propose Credit Info
    public void onRetrievePricingFee() {
        // test create data from retrieving
        NewCreditDetailView creditDetailRetrieve = new NewCreditDetailView();
        BaseRate baseRate = baseRateDAO.findById(1);
        creditDetailRetrieve.setStandardBasePrice(baseRate);
        creditDetailRetrieve.setStandardInterest(BigDecimal.valueOf(-1.75));

        if (creditDetailRetrieve.getStandardInterest().doubleValue() < 0) {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " " + creditDetailRetrieve.getStandardInterest());
        } else {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " + " + creditDetailRetrieve.getStandardInterest());
        }

        //****** tier test create ********//
        newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        newCreditTierDetailView = new NewCreditTierDetailView();
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

        for (NewCreditDetailView proposeCreditDetail : newCreditFacilityView.getNewCreditDetailViewList()) {
            proposeCreditDetail.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            proposeCreditDetail.setStandardBasePrice(baseRate);
            proposeCreditDetail.setSuggestBasePrice(baseRate);
            proposeCreditDetail.setStandardInterest(creditDetailRetrieve.getStandardInterest());
            proposeCreditDetail.setSuggestInterest(creditDetailRetrieve.getSuggestInterest());
        }

    }

    // Call  COMs to get Data Propose Collateral
    public void onCallRetrieveAppraisalReportInfo() {
        log.info("onCallRetrieveAppraisalReportInfo begin key is  :: {}", newCollateralInfoView.getJobID());

        //COMSInterface
        log.info("getData From COMS begin");

        /*newCollateralInfoView.setAppraisalDate(DateTime.now().toDate());
        newCollateralInfoView.setAadDecision("ผ่าน");
        newCollateralInfoView.setAadDecisionReason("กู้");
        newCollateralInfoView.setAadDecisionReasonDetail("ok");

        newCollateralHeadDetailView = new NewCollateralHeadDetailView();
        newCollateralHeadDetailView.setCollateralLocation("ประเทศไทย แลน ออฟ สไมล์");
        newCollateralHeadDetailView.setTitleDeed("กค 126,ญก 156");
        newCollateralHeadDetailView.setAppraisalValue(BigDecimal.valueOf(4810000));

        CollateralType collateralType = collateralTypeDAO.findById(1);
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(2);
        newCollateralHeadDetailView.setHeadCollType(collateralType);
        newCollateralHeadDetailView.setPotentialCollateral(potentialCollateral);

        newSubCollateralDetailView = new NewSubCollateralDetailView();
        newSubCollateralDetailView.setLandOffice("ขอนแก่น");
        newSubCollateralDetailView.setAddress("ถนน ข้าวเหนียว จ ขอนแก่น");
        newSubCollateralDetailView.setTitleDeed("กค 126");
        newSubCollateralDetailView.setCollateralOwner("AAA");
        newSubCollateralDetailView.setAppraisalValue(new BigDecimal(3810000));
        newSubCollateralDetailViewList.add(newSubCollateralDetailView);

        newSubCollateralDetailView = new NewSubCollateralDetailView();
        newSubCollateralDetailView.setLandOffice("กทม");
        newSubCollateralDetailView.setAddress("ถนน วิภาวดีรังสิต");
        newSubCollateralDetailView.setTitleDeed("กค 126");
        newSubCollateralDetailView.setCollateralOwner("BBB");
        newSubCollateralDetailView.setAppraisalValue(new BigDecimal(9000000));
        newSubCollateralDetailViewList.add(newSubCollateralDetailView);

        newCollateralHeadDetailView.setNewSubCollateralDetailViewList(newSubCollateralDetailViewList);
        newCollateralInfoView.getNewCollateralHeadDetailViewList().add(newCollateralHeadDetailView);*/

        log.info("onCallRetrieveAppraisalReportInfo End");

    }

    //  Start Propose Credit Information  //

    public void onChangeProductProgram() {
        ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
        log.debug("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        newCreditDetailView.setProductCode("");
        newCreditDetailView.setProjectCode("");

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " + prdProgramToCreditTypeList.size());
        newCreditDetailView.getCreditType().setId(0);
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType :::: creditType : {}", newCreditDetailView.getCreditType().getId());
        if ((newCreditDetailView.getProductProgram().getId() != 0) && (newCreditDetailView.getCreditType().getId() != 0)) {
            ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());
            //productFormulaDAO
            //where 4 ตัว ProductProgramFacilityId , CreditCusType (prime/normal),applyTCG (TCG),spec_program_id(basicInfo)
            if (productProgram != null && creditType != null) {
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);
                if ((prdProgramToCreditType.getId() != 0) && (specialProgramBasicInfo.getId() != 0)) {
                    log.info("onChangeCreditType :: prdProgramToCreditType :: {}", prdProgramToCreditType.getId());
                    log.info("onChangeCreditType :: newCreditFacilityView.getCreditCustomerType() :: {}", newCreditFacilityView.getCreditCustomerType());
                    log.info("onChangeCreditType :: specialProgramBasicInfo :: {}", specialProgramBasicInfo.getId());
                    log.info("onChangeCreditType :: applyTCG :: {}", applyTCG);
                    SpecialProgram specialProgram = specialProgramDAO.findById(specialProgramBasicInfo.getId());
                    ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, newCreditFacilityView.getCreditCustomerType(), specialProgram, applyTCG);

                    if (productFormula != null) {
                        log.debug("onChangeCreditType :::: productFormula : {}", productFormula.getId());
                        newCreditDetailView.setProductCode(productFormula.getProductCode());
                        newCreditDetailView.setProjectCode(productFormula.getProjectCode());
                        log.info("productFormula.getReduceFrontEndFee() ::: {}", productFormula.getReduceFrontEndFee());
                        log.info("productFormula.getReducePricing() ::: {}", productFormula.getReducePricing());

                        modeEditReducePricing = flagForModeDisable(productFormula.getReducePricing());
                        modeEditReduceFront =  flagForModeDisable(productFormula.getReduceFrontEndFee());

                        reducePricePanelRendered =(modeEditReducePricing==true)?true:false;
                        log.info("reducePricePanelRendered:: {}",reducePricePanelRendered);
                    }
                }
            }
        }
    }

    // 2:Y(false)can to edit , 1:N(true) cannot to edit
    public static boolean flagForModeDisable(int value){
        return (value == 1)?true:false;
    }


    public void onChangeRequestType() {
        log.info("newCreditDetailView.getRequestType() :: {}", newCreditDetailView.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();

        if (newCreditDetailView.getRequestType() == RequestTypes.CHANGE.value()) {   //change
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
            newCreditDetailView.getProductProgram().setId(0);
            cannotEditStandard =false;
        } else if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
                newCreditDetailView.getCreditType().setId(0);
            }
            cannotEditStandard =true;
        }
    }

    public void onRequestReducePrice(){
        log.info("reducePrice ::: {}",reducePrice);

    }

    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        RequestContext.getCurrentInstance().execute("creditInfoDlg.show()");
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        newCreditDetailView = new NewCreditDetailView();
        modeForButton = ModeForButton.ADD;
        onChangeRequestType();
    }


    public void onEditCreditInfo() {
        modeEdit = false;
        modeForButton = ModeForButton.EDIT;
        log.info("rowIndex :: {}", rowIndex);
        log.info("newCreditFacilityView.creditInfoDetailViewList :: {}", newCreditFacilityView.getNewCreditDetailViewList());
        ProductProgram productProgram = proposeCreditDetailSelected.getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);

        if (rowIndex < newCreditFacilityView.getNewCreditDetailViewList().size()) {
            newCreditDetailView = new NewCreditDetailView();
            newCreditDetailView.setProductProgram(productProgram);
            newCreditDetailView.setCreditType(proposeCreditDetailSelected.getCreditType());
            newCreditDetailView.setRequestType(proposeCreditDetailSelected.getRequestType());
            newCreditDetailView.setRefinance(proposeCreditDetailSelected.getRefinance());
            newCreditDetailView.setProductCode(proposeCreditDetailSelected.getProductCode());
            newCreditDetailView.setProjectCode(proposeCreditDetailSelected.getProjectCode());
            newCreditDetailView.setLimit(proposeCreditDetailSelected.getLimit());
            newCreditDetailView.setPCEPercent(proposeCreditDetailSelected.getPCEPercent());
            newCreditDetailView.setPCEAmount(proposeCreditDetailSelected.getPCEAmount());
            newCreditDetailView.setReduceFrontEndFee(proposeCreditDetailSelected.isReduceFrontEndFee());
            newCreditDetailView.setReducePriceFlag(proposeCreditDetailSelected.isReducePriceFlag());
            newCreditDetailView.setStandardBasePrice(proposeCreditDetailSelected.getStandardBasePrice());
            newCreditDetailView.setStandardInterest(proposeCreditDetailSelected.getStandardInterest());
            newCreditDetailView.setSuggestBasePrice(proposeCreditDetailSelected.getSuggestBasePrice());
            newCreditDetailView.setSuggestInterest(proposeCreditDetailSelected.getSuggestInterest());
            newCreditDetailView.setFrontEndFee(proposeCreditDetailSelected.getFrontEndFee());
            newCreditDetailView.setLoanPurpose(newCreditDetailView.getLoanPurpose());
            newCreditDetailView.setRemark(proposeCreditDetailSelected.getRemark());
            newCreditDetailView.setDisbursement(proposeCreditDetailSelected.getDisbursement());
            newCreditDetailView.setHoldLimitAmount(proposeCreditDetailSelected.getHoldLimitAmount());

            newCreditDetailView.setNewCreditTierDetailViewList(proposeCreditDetailSelected.getNewCreditTierDetailViewList());

        }
    }

    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if ((newCreditDetailView.getProductProgram().getId() != 0) && (newCreditDetailView.getCreditType().getId() != 0)
                && (newCreditDetailView.getLoanPurpose().getId() != 0) && (newCreditDetailView.getDisbursement().getId() != 0)) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

                ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());
                LoanPurpose loanPurpose = loanPurposeDAO.findById(newCreditDetailView.getLoanPurpose().getId());
                Disbursement disbursement = disbursementDAO.findById(newCreditDetailView.getDisbursement().getId());

                NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
                creditDetailAdd.setProductProgram(productProgram);
                creditDetailAdd.setCreditType(creditType);
                creditDetailAdd.setRequestType(newCreditDetailView.getRequestType());
                creditDetailAdd.setRefinance(newCreditDetailView.getRefinance());
                creditDetailAdd.setProductCode(newCreditDetailView.getProductCode());
                creditDetailAdd.setProjectCode(newCreditDetailView.getProjectCode());
                creditDetailAdd.setLimit(newCreditDetailView.getLimit());
                creditDetailAdd.setPCEPercent(newCreditDetailView.getPCEPercent());
                creditDetailAdd.setPCEAmount(newCreditDetailView.getPCEAmount());
                creditDetailAdd.setReduceFrontEndFee(newCreditDetailView.isReduceFrontEndFee());
                creditDetailAdd.setReducePriceFlag(newCreditDetailView.isReducePriceFlag());
                creditDetailAdd.setStandardBasePrice(newCreditDetailView.getStandardBasePrice());
                creditDetailAdd.setStandardInterest(newCreditDetailView.getStandardInterest());
                creditDetailAdd.setSuggestBasePrice(newCreditDetailView.getSuggestBasePrice());
                creditDetailAdd.setSuggestInterest(newCreditDetailView.getSuggestInterest());
                creditDetailAdd.setFrontEndFee(newCreditDetailView.getFrontEndFee());
                creditDetailAdd.setLoanPurpose(loanPurpose);
                creditDetailAdd.setRemark(newCreditDetailView.getRemark());
                creditDetailAdd.setDisbursement(disbursement);
                creditDetailAdd.setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());

                creditDetailAdd.setNewCreditTierDetailViewList(newCreditDetailView.getNewCreditTierDetailViewList());
                creditDetailAdd.setSeq(seq);
                newCreditFacilityView.getNewCreditDetailViewList().add(creditDetailAdd);

                log.info("seq of credit after add proposeCredit :: {}", seq);
            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onEditRecord ::: mode : {}", modeForButton);
                ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());
                LoanPurpose loanPurpose = loanPurposeDAO.findById(newCreditDetailView.getLoanPurpose().getId());
                Disbursement disbursement = disbursementDAO.findById(newCreditDetailView.getDisbursement().getId());

                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProductProgram(productProgram);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setCreditType(creditType);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRequestType(newCreditDetailView.getRequestType());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRefinance(newCreditDetailView.getRefinance());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProductCode(newCreditDetailView.getProductCode());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProjectCode(newCreditDetailView.getProjectCode());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setLimit(newCreditDetailView.getLimit());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setPCEPercent(newCreditDetailView.getPCEPercent());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setPCEAmount(newCreditDetailView.getPCEAmount());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReducePriceFlag(newCreditDetailView.isReducePriceFlag());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReduceFrontEndFee(newCreditDetailView.isReduceFrontEndFee());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setStandardBasePrice(newCreditDetailView.getStandardBasePrice());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setStandardInterest(newCreditDetailView.getStandardInterest());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setSuggestBasePrice(newCreditDetailView.getSuggestBasePrice());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setSuggestInterest(newCreditDetailView.getSuggestInterest());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setFrontEndFee(newCreditDetailView.getFrontEndFee());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setLoanPurpose(loanPurpose);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRemark(newCreditDetailView.getRemark());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setDisbursement(disbursement);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());

                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setNewCreditTierDetailViewList(newCreditDetailView.getNewCreditTierDetailViewList());
            } else {
                log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
            }

            complete = true;
            hashSeqCredit.put(seq, 0);
            seq++;
            log.info("seq++ of credit after add complete proposeCredit :: {}", seq);

            newCreditDetailList =  newCreditFacilityView.getNewCreditDetailViewList();

        } else {

            log.info("onSaveCreditInfo ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    public void onDeleteCreditInfo() {
        log.info("delete :: rowIndex :: {}", rowIndex);
        int used;
        for (int l = 0; l < hashSeqCredit.size(); l++) {
            log.info("hashSeqCredit.get(i) in use   :  " + l + " is   " + hashSeqCredit.get(l).toString());
        }
        log.info("onDeleteCreditInfo ::: seq is : {} " + proposeCreditDetailSelected.getSeq());
        log.info("onDeleteCreditInfo ::: use is : {} " + Integer.parseInt(hashSeqCredit.get(proposeCreditDetailSelected.getSeq()).toString()));

        used = Integer.parseInt(hashSeqCredit.get(proposeCreditDetailSelected.getSeq()).toString());

        log.info("before del use is  " + used);

        if (used == 0) {
            log.info("used ::: {} ", used);
            newCreditFacilityView.getNewCreditDetailViewList().remove(rowIndex);

        } else {
            log.info("used::: {}", used);
            messageHeader = msg.get("app.propose.exception");
            message = msg.get("app.propose.error.delete.credit");
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    //  END Propose Credit Information  //

    //  Start Tier Dialog //
    public void onAddTierInfo() {
        log.info("onAddTierInfo ::: rowIndex of proposeCredit to edit :: {}", rowIndex);
        NewCreditTierDetailView creditTierDetailAdd = new NewCreditTierDetailView();
        BaseRate finalBase = baseRateDAO.findById(finalBaseRate.getId());
        creditTierDetailAdd.setFinalBasePrice(finalBase);
        creditTierDetailAdd.setFinalInterest(finalInterest);
        creditTierDetailAdd.setFinalPriceRate(finalPriceRate);
        creditTierDetailAdd.setStandardPrice(standardPriceLabel);
        creditTierDetailAdd.setSuggestPrice(suggestPriceLabel);
        creditTierDetailAdd.setCanEdit(true);
        newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).getNewCreditTierDetailViewList().add(0, creditTierDetailAdd);

    }

    public void onChangeSuggestValue() {
        log.info("onChangeSuggestValue ::{}", rowIndex);
        suggestPrice = BigDecimal.ZERO;
        standardPrice = BigDecimal.ZERO;
        finalBaseRate = new BaseRate();
        finalInterest = BigDecimal.ZERO;
        suggestPriceLabel = "";
        standardPriceLabel = "";
        finalPriceRate = "";

        BaseRate baseRate1 = baseRateDAO.findById(newCreditDetailView.getSuggestBasePrice().getId());
        BaseRate baseRate2 = baseRateDAO.findById(newCreditDetailView.getStandardBasePrice().getId());

        suggestPrice = baseRate1.getValue().add(newCreditDetailView.getSuggestInterest());

        if (newCreditDetailView.getSuggestInterest().doubleValue() < 0) {
            suggestPriceLabel = baseRate1.getName() + " " + newCreditDetailView.getSuggestInterest();
        } else {
            suggestPriceLabel = baseRate1.getName() + " + " + newCreditDetailView.getSuggestInterest();
        }

        standardPrice = baseRate2.getValue().add(newCreditDetailView.getStandardInterest());
        if (newCreditDetailView.getStandardInterest().doubleValue() < 0) {
            standardPriceLabel = baseRate2.getName() + " " + newCreditDetailView.getStandardInterest();
        } else {
            standardPriceLabel = baseRate2.getName() + " + " + newCreditDetailView.getStandardInterest();
        }

        log.info("baseRate1 getValue :: {}", baseRate1.getValue());
        log.info("getSuggestInterest :: {}", newCreditDetailView.getSuggestInterest());
        log.info("baseRate2 getValue :: {}", baseRate2.getValue());
        log.info("getStandardInterest :: {}", newCreditDetailView.getStandardInterest());
        log.info("suggestPrice :: {}", suggestPrice);
        log.info("standardPrice :: {}", standardPrice);

        if (standardPrice.doubleValue() > suggestPrice.doubleValue()) {
            finalBaseRate = baseRate1;
            finalInterest = newCreditDetailView.getStandardInterest();
            finalPriceRate = standardPriceLabel;
        } else if (suggestPrice.doubleValue() > standardPrice.doubleValue()) {
            finalBaseRate = baseRate2;
            finalInterest = newCreditDetailView.getSuggestInterest();
            finalPriceRate = suggestPriceLabel;
        }
    }

    public void onDeleteProposeTierInfo(int row) {
        log.info("onDeleteProposeTierInfo::");
        newCreditDetailView.getNewCreditTierDetailViewList().remove(row);
    }

    //  END Tier Dialog //

    //  Start Propose Collateral Information  //
    public void onAddProposeCollInfo() {
        log.info("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCreditDetailViewList().size());
        modeForButton = ModeForButton.ADD;
        Cloner cloner = new Cloner();
        newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
        newCollateralInfoView = new NewCollateralInfoView();
        newCollateralInfoView.setNewCreditDetailViewList(newCreditDetailListTemp);
        newCollateralInfoView.getNewCollateralHeadDetailViewList().add(new NewCollateralHeadDetailView());

    }

    public void onEditProposeCollInfo() {
        log.info("onEditProposeCollInfo :: {}", selectCollateralDetailView.getId());
        log.info("onEditProposeCollInfo ::rowIndexCollateral  {}", rowIndexCollateral);
        modeForButton = ModeForButton.EDIT;
        newCollateralInfoView.setJobID(selectCollateralDetailView.getJobID());
        newCollateralInfoView.setAppraisalDate(selectCollateralDetailView.getAppraisalDate());
        newCollateralInfoView.setAadDecision(selectCollateralDetailView.getAadDecision());
        newCollateralInfoView.setAadDecisionReason(selectCollateralDetailView.getAadDecisionReason());
        newCollateralInfoView.setAadDecisionReasonDetail(selectCollateralDetailView.getAadDecisionReasonDetail());
        newCollateralInfoView.setUsage(selectCollateralDetailView.getUsage());
        newCollateralInfoView.setTypeOfUsage(selectCollateralDetailView.getTypeOfUsage());
        newCollateralInfoView.setUwDecision(selectCollateralDetailView.getUwDecision());
        newCollateralInfoView.setUwRemark(selectCollateralDetailView.getUwRemark());
        newCollateralInfoView.setMortgageCondition(selectCollateralDetailView.getMortgageCondition());
        newCollateralInfoView.setMortgageConditionDetail(selectCollateralDetailView.getMortgageConditionDetail());
        newCollateralInfoView.setBdmComments(selectCollateralDetailView.getBdmComments());
        newCollateralInfoView.setNewCollateralHeadDetailViewList(selectCollateralDetailView.getNewCollateralHeadDetailViewList());

        int tempSeq = 0;
        Cloner cloner = new Cloner();
        newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
        newCollateralInfoView.setNewCreditDetailViewList(newCreditDetailListTemp);

        if (selectCollateralDetailView.getNewCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectCollateralDetailView.getNewCreditDetailViewList().size(); i++) {
                for (int j = tempSeq; j < newCreditDetailListTemp.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + newCreditDetailListTemp.get(j).getSeq());

                    if (selectCollateralDetailView.getNewCreditDetailViewList().get(i).getSeq() == newCreditDetailListTemp.get(j).getSeq()) {
                        newCollateralInfoView.getNewCreditDetailViewList().get(j).setNoFlag(true);
                        tempSeq = j;
                    }
                    continue;
                }
            }
        }

    }

    public void onSaveProposeCollInfo() {
        log.info("onSaveProposeCollInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        int seqTemp;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            NewCollateralInfoView proposeCollateralInfoAdd = new NewCollateralInfoView();
            proposeCollateralInfoAdd.setJobID(newCollateralInfoView.getJobID());
            proposeCollateralInfoAdd.setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            proposeCollateralInfoAdd.setAadDecision(newCollateralInfoView.getAadDecision());
            proposeCollateralInfoAdd.setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            proposeCollateralInfoAdd.setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            proposeCollateralInfoAdd.setUsage(newCollateralInfoView.getUsage());
            proposeCollateralInfoAdd.setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            proposeCollateralInfoAdd.setUwDecision(newCollateralInfoView.getUwDecision());
            proposeCollateralInfoAdd.setUwRemark(newCollateralInfoView.getUwRemark());
            proposeCollateralInfoAdd.setBdmComments(newCollateralInfoView.getBdmComments());
            proposeCollateralInfoAdd.setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            proposeCollateralInfoAdd.setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());

            newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();

            for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollateralHeadDetailView.getPotentialCollateral().getId());
                CollateralType collTypePercentLTV = collateralTypeDAO.findById(newCollateralHeadDetailView.getCollTypePercentLTV().getId());
                CollateralType headCollType = collateralTypeDAO.findById(newCollateralHeadDetailView.getHeadCollType().getId());

                NewCollateralHeadDetailView newCollateralHeadDetailAdd = new NewCollateralHeadDetailView();
                newCollateralHeadDetailAdd.setPotentialCollateral(potentialCollateral);
                newCollateralHeadDetailAdd.setCollTypePercentLTV(collTypePercentLTV);
                newCollateralHeadDetailAdd.setExistingCredit(newCollateralHeadDetailView.getExistingCredit());
                newCollateralHeadDetailAdd.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
                newCollateralHeadDetailAdd.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
                newCollateralHeadDetailAdd.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
                newCollateralHeadDetailAdd.setHeadCollType(headCollType);
                newCollateralHeadDetailAdd.setInsuranceCompany(newCollateralHeadDetailView.getInsuranceCompany());
                newCollateralHeadDetailAdd.setNewSubCollateralDetailViewList(newCollateralHeadDetailView.getNewSubCollateralDetailViewList());

                newCollateralHeadDetailViewList.add(newCollateralHeadDetailAdd);
            }

            proposeCollateralInfoAdd.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViewList);

            if (newCollateralInfoView.getNewCreditDetailViewList().size() > 0) {

                for (NewCreditDetailView creditTypeDetail : newCollateralInfoView.getNewCreditDetailViewList()) {
                    log.info("creditTypeDetail.isNoFlag()  :: {}", creditTypeDetail.isNoFlag());
                    if (creditTypeDetail.isNoFlag()) {
                        proposeCollateralInfoAdd.getNewCreditDetailViewList().add(creditTypeDetail);
                        seqTemp = creditTypeDetail.getSeq();
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                    }
                }

                newCreditFacilityView.getNewCollateralInfoViewList().add(proposeCollateralInfoAdd);
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }


            log.info("newCreditFacilityView.getNewCollateralInfoViewList() {}", newCreditFacilityView.getNewCollateralInfoViewList().size());

        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            log.info("modeForButton:: {} ", modeForButton);

            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setJobID(newCollateralInfoView.getJobID());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setAppraisalDate(newCollateralInfoView.getAppraisalDate());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setAadDecision(newCollateralInfoView.getAadDecision());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setAadDecisionReason(newCollateralInfoView.getAadDecisionReason());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setAadDecisionReasonDetail(newCollateralInfoView.getAadDecisionReasonDetail());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setUsage(newCollateralInfoView.getUsage());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setTypeOfUsage(newCollateralInfoView.getTypeOfUsage());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setUwDecision(newCollateralInfoView.getUwDecision());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setUwRemark(newCollateralInfoView.getUwRemark());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setBdmComments(newCollateralInfoView.getBdmComments());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setMortgageCondition(newCollateralInfoView.getMortgageCondition());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setMortgageConditionDetail(newCollateralInfoView.getMortgageConditionDetail());
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setNewCollateralHeadDetailViewList(newCollateralInfoView.getNewCollateralHeadDetailViewList());

            boolean checkPlus;
//            Cloner cloner = new Cloner();
//            newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
            newCollateralInfoView.setNewCreditDetailViewList(new ArrayList<NewCreditDetailView>());

            for (int i = 0; i < newCreditDetailListTemp.size(); i++) {
                if (newCreditDetailListTemp.get(i).isNoFlag() == true) {
                    newCollateralInfoView.getNewCreditDetailViewList().add(newCreditDetailListTemp.get(i));
                    seqTemp = newCreditDetailListTemp.get(i).getSeq();
                    checkPlus = true;

                    for (int j = 0; j < selectCollateralDetailView.getNewCreditDetailViewList().size(); j++) {
                        if (selectCollateralDetailView.getNewCreditDetailViewList().get(j).getSeq() == seqTemp) {
                            checkPlus = false;
                        }
                    }

                    if (checkPlus) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                    }

                } else if (newCreditDetailListTemp.get(i).isNoFlag() == false) {
                    if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                        hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
                    }
                }
            }
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setNewCreditDetailViewList(newCollateralInfoView.getNewCreditDetailViewList());


        } else {
            log.info("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        complete = true;
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteProposeCollInfo() {
        log.info("onDeleteProposeCollInfo :: ");
        for (int i = 0; i < selectCollateralDetailView.getNewCreditDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }
        newCreditFacilityView.getNewCollateralInfoViewList().remove(selectCollateralDetailView);
    }

    // for sub collateral dialog
    public void onAddCollateralOwnerUW() {
        log.info("newSubCollateralDetailView.getCollateralOwnerUWList() {}", newSubCollateralDetailView.getCollateralOwnerUWList().size());
//        if (newSubCollateralDetailView.getCollateralOwnerUWList().size() > 0) {
//            for (CustomerInfoView collateralUW : newSubCollateralDetailView.getCollateralOwnerUWList()) {
//                if (collateralUW.getId() == newSubCollateralDetailView.getCollateralOwnerUW().getId()) {
//                    messageHeader = msg.get("app.propose.exception");
//                    message = msg.get("app.propose.desc.add.collateralUW");
//                    messageErr = true;
//                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//                } else {
                    log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", newSubCollateralDetailView.getCollateralOwnerUW().getId());
                    if (newSubCollateralDetailView.getCollateralOwnerUW().getId() == 0) {
                        log.error("Can not add CollateralOwnerUw because id = 0!");
                        return;
                    }

                    CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(newSubCollateralDetailView.getCollateralOwnerUW());
                    newSubCollateralDetailView.getCollateralOwnerUWList().add(collateralOwnerAdd);
//                }
//            }
//        }

    }

    public void onDeleteCollateralOwnerUW(int row) {
        log.info("row :: {} ", row);
        newSubCollateralDetailView.getCollateralOwnerUWList().remove(row);
    }

    public void onAddMortgageType() {
//        for (MortgageType mortgage : newSubCollateralDetailView.getMortgageList()) {
//            if (mortgage.getId() == newSubCollateralDetailView.getMortgageType().getId()) {
//                messageHeader = msg.get("app.propose.exception");
//                message = msg.get("app.propose.desc.add.mortgage");
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            } else {
                log.debug("onAddMortgageType() mortgageType.id: {}", newSubCollateralDetailView.getMortgageType().getId());
                if (newSubCollateralDetailView.getMortgageType().getId() == 0) {
                    log.error("Can not add MortgageType because id = 0!");
                    return;
                }
                MortgageType mortgageType = mortgageTypeDAO.findById(newSubCollateralDetailView.getMortgageType().getId());
                log.info("onAddMortgageType :: {} ", newSubCollateralDetailView.getMortgageType());
                newSubCollateralDetailView.getMortgageList().add(mortgageType);
//            }
//        }
    }

    public void onDeleteMortgageType(int row) {
        newSubCollateralDetailView.getMortgageList().remove(row);
    }

    public void onAddRelatedWith() {
//        for (NewSubCollateralDetailView relatedWithThis : newSubCollateralDetailView.getRelatedWithList()) {
//            if (relatedWithSelected.getId() == relatedWithThis.getId()) {
//                messageHeader = msg.get("app.propose.exception");
//                message = msg.get("app.propose.desc.add.relatedWith");
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            } else {
                log.debug("onAddRelatedWith() relatedWithSelected.relatedWithId = {}", relatedWithSelected.getId());
                if (relatedWithSelected.getId() == 0) {
                    log.error("Can not add RelatedWith because id = 0!");
                    return;
                }
                NewSubCollateralDetailView relatedWith = getIdNewSubCollateralDetail(relatedWithSelected.getId());
                newSubCollateralDetailView.getRelatedWithList().add(relatedWith);
//            }
//        }

    }

    public void onDeleteRelatedWith(int row) {
        newSubCollateralDetailView.getRelatedWithList().remove(row);
    }

    public NewSubCollateralDetailView getIdNewSubCollateralDetail(long newSubCollateralId) {
        NewSubCollateralDetailView newSubCollateralReturn = new NewSubCollateralDetailView();
        if (newCreditFacilityView.getNewCollateralInfoViewList().size() > 0) {
            for (NewCollateralInfoView newCollateralInfoView : Util.safetyList(newCreditFacilityView.getNewCollateralInfoViewList()))
            {
                for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                    for (NewSubCollateralDetailView newSubCollateralDetailOnAdded : newCollateralHeadDetailView.getNewSubCollateralDetailViewList()) {
                        log.info("newSubCollateralDetailView1 id ::: {}", newSubCollateralDetailOnAdded.getId());
                        log.info("newSubCollateralDetailView1 title deed ::: {}", newSubCollateralDetailOnAdded.getTitleDeed());
                        if (newSubCollateralId == newSubCollateralDetailOnAdded.getId()) {
                            newSubCollateralReturn = newSubCollateralDetailOnAdded;
                        }
                    }
                }
            }
        }
        return newSubCollateralReturn;
    }
    // end for sub collateral dialog

    //  END Propose Collateral Information  //

    // Start Add SUB Collateral
    public void onAddSubCollateral() {
        log.info("onAddSubCollateral and rowCollHeadIndex :: {}", rowCollHeadIndex);
        newSubCollateralDetailView = new NewSubCollateralDetailView();
        relatedWithSelected = new NewSubCollateralDetailView();
        modeForSubColl = ModeForButton.ADD;
        log.info(" newCreditFacilityView.getNewCollateralInfoViewList().size ::{}", newCreditFacilityView.getNewCollateralInfoViewList().size());

        if (newCreditFacilityView.getNewCollateralInfoViewList().size() > 0) {
            for (NewCollateralInfoView newCollateralInfoView : newCreditFacilityView.getNewCollateralInfoViewList()) {
                relatedWithAllList = new ArrayList<NewSubCollateralDetailView>();  // find list of Title Deed other Collateral but not include this Collateral
                for (NewCollateralHeadDetailView newCollateralHeadDetail : newCollateralInfoView.getNewCollateralHeadDetailViewList()) {
                    if (newCollateralHeadDetail.getNewSubCollateralDetailViewList().size() > 0) {
                        log.info("newCollateralHeadDetail . getId:: {}", newCollateralHeadDetail.getId());
                        for (NewSubCollateralDetailView newSubCollateralDetailView : newCollateralHeadDetail.getNewSubCollateralDetailViewList()) {
                            log.info("newSubCollateralDetailView :::{}", newSubCollateralDetailView.getId());
                            relatedWithAllList.add(newSubCollateralDetailView);
                            log.info("relatedWithAllList > size :: {}", relatedWithAllList.size());
                        }
                    }
                }
            }
        }
    }

    public void onEditSubCollateral() {
        log.info("rowSubIndex :: {}", rowSubIndex);
        modeForSubColl = ModeForButton.EDIT;
        newSubCollateralDetailView.setSubCollateralType(subCollateralDetailItem.getSubCollateralType());
        newSubCollateralDetailView.setTitleDeed(subCollateralDetailItem.getTitleDeed());
        newSubCollateralDetailView.setAddress(subCollateralDetailItem.getAddress());
        newSubCollateralDetailView.setLandOffice(subCollateralDetailItem.getLandOffice());
        newSubCollateralDetailView.setCollateralOwnerAAD(subCollateralDetailItem.getCollateralOwnerAAD());
        newSubCollateralDetailView.setAppraisalValue(subCollateralDetailItem.getAppraisalValue());
        newSubCollateralDetailView.setMortgageValue(subCollateralDetailItem.getMortgageValue());
        newSubCollateralDetailView.setCollateralOwnerUWList(subCollateralDetailItem.getCollateralOwnerUWList());
        newSubCollateralDetailView.setMortgageList(subCollateralDetailItem.getMortgageList());
        newSubCollateralDetailView.setRelatedWithList(subCollateralDetailItem.getRelatedWithList());
    }

    public void onSaveSubCollateral() {
        log.info("onSaveSubCollateral ::: mode : {}", modeForSubColl);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)) {
            log.info("modeForSubColl ::: {}", modeForSubColl);
            log.info("newSubCollateralDetailView.getRelatedWithList() :: {}", newSubCollateralDetailView.getRelatedWithList().size());
            NewSubCollateralDetailView subCollAdd = new NewSubCollateralDetailView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newSubCollateralDetailView.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            subCollAdd.setAddress(newSubCollateralDetailView.getAddress());
            subCollAdd.setLandOffice(newSubCollateralDetailView.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(newSubCollateralDetailView.getCollateralOwnerAAD());
            subCollAdd.setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            subCollAdd.setMortgageValue(newSubCollateralDetailView.getMortgageValue());
            subCollAdd.setCollateralOwnerUWList(newSubCollateralDetailView.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(newSubCollateralDetailView.getMortgageList());
            subCollAdd.setRelatedWithList(newSubCollateralDetailView.getRelatedWithList());

            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().add(subCollAdd);

        } else if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.EDIT)) {
            log.info("modeForSubColl ::: {}", modeForSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newSubCollateralDetailView.getSubCollateralType().getId());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setSubCollateralType(subCollateralType);
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setTitleDeed(newSubCollateralDetailView.getTitleDeed());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setAddress(newSubCollateralDetailView.getAddress());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setLandOffice(newSubCollateralDetailView.getLandOffice());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setCollateralOwnerAAD(newSubCollateralDetailView.getCollateralOwnerAAD());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setAppraisalValue(newSubCollateralDetailView.getAppraisalValue());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setMortgageValue(newSubCollateralDetailView.getMortgageValue());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setCollateralOwnerUWList(newSubCollateralDetailView.getCollateralOwnerUWList());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setMortgageList(newSubCollateralDetailView.getMortgageList());
            newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().get(rowSubIndex).setRelatedWithList(newSubCollateralDetailView.getRelatedWithList());
        } else {
            log.info("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        complete = true;
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }


    public void onDeleteSubCollateral() {
        log.info("onDeleteSubCollateral :: ");
        newCollateralInfoView.getNewCollateralHeadDetailViewList().get(rowCollHeadIndex).getNewSubCollateralDetailViewList().remove(subCollateralDetailItem);
    }
    // END Add SUB Collateral

    //  Start Guarantor //
    public void onAddGuarantorInfo() {
        newGuarantorDetailView = new NewGuarantorDetailView();
        modeForButton = ModeForButton.ADD;
        Cloner cloner = new Cloner();
        newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
        newGuarantorDetailView.setNewCreditDetailViewList(newCreditDetailListTemp);
    }

    public void onEditGuarantorInfo() {
        log.info("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;
        int tempSeq = 0;
        BigDecimal summary = BigDecimal.ZERO;

        if (newGuarantorDetailViewItem != null) {
            newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());
            Cloner cloner = new Cloner();
            newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
            newGuarantorDetailView.setNewCreditDetailViewList(newCreditDetailListTemp);

            if (newGuarantorDetailViewItem.getNewCreditDetailViewList().size() > 0) {
                for (int i = 0; i < newGuarantorDetailViewItem.getNewCreditDetailViewList().size(); i++) {
                    for (int j = tempSeq; j < newCreditDetailListTemp.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + newCreditDetailListTemp.get(j).getSeq());

                        if (newGuarantorDetailViewItem.getNewCreditDetailViewList().get(i).getSeq() == newCreditDetailListTemp.get(j).getSeq()) {
                            newGuarantorDetailView.getNewCreditDetailViewList().get(j).setNoFlag(true);
                            newGuarantorDetailView.getNewCreditDetailViewList().get(j).setGuaranteeAmount(newGuarantorDetailViewItem.getNewCreditDetailViewList().get(i).getGuaranteeAmount());
                            summary = summary.add(newGuarantorDetailViewItem.getNewCreditDetailViewList().get(i).getGuaranteeAmount());
                            tempSeq = j;
                        }
                        continue;
                    }
                }
            }

            newGuarantorDetailView.setTotalLimitGuaranteeAmount(summary);
        }
    }

    public void onSaveGuarantorInfoDlg() {
        log.info("onSaveGuarantorInfoDlg ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        BigDecimal summary = BigDecimal.ZERO;
        int seqTemp;

        if (newGuarantorDetailView.getGuarantorName() != null) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.info("modeForButton ::: {}", modeForButton);
                CustomerInfoView customerInfoView = customerInfoControl.getCustomerById(newGuarantorDetailView.getGuarantorName());
                NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
                guarantorDetailAdd.setGuarantorName(customerInfoView);
                guarantorDetailAdd.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());

                for (NewCreditDetailView newCreditDetailView1 : newGuarantorDetailView.getNewCreditDetailViewList()) {
                    if (newCreditDetailView1.isNoFlag()) {
                        guarantorDetailAdd.getNewCreditDetailViewList().add(newCreditDetailView1);
                        summary = summary.add(newCreditDetailView1.getGuaranteeAmount());
                        seqTemp = newCreditDetailView1.getSeq();
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        log.info(" ::: seqTemp ::: {}", seqTemp);
                    }
                }

                guarantorDetailAdd.setTotalLimitGuaranteeAmount(summary);

                if (guarantorDetailAdd.getNewCreditDetailViewList().size() > 0) {
                    newCreditFacilityView.getNewGuarantorDetailViewList().add(guarantorDetailAdd);
                } else {
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.desc.add.data");
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("modeForButton ::: {}", modeForButton);
                CustomerInfoView customerInfoEdit = customerInfoControl.getCustomerById(newGuarantorDetailView.getGuarantorName());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(customerInfoEdit);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
                boolean checkPlus;
//                Cloner cloner = new Cloner();
//                newCreditDetailListTemp = cloner.deepClone(newCreditDetailList);
                newGuarantorDetailView.setNewCreditDetailViewList(new ArrayList<NewCreditDetailView>());

                for (int i = 0; i < newCreditDetailListTemp.size(); i++) {
                    if (newCreditDetailListTemp.get(i).isNoFlag() == true) {
                        newGuarantorDetailView.getNewCreditDetailViewList().add(newCreditDetailListTemp.get(i));
                        summary = summary.add(newCreditDetailListTemp.get(i).getGuaranteeAmount());
                        seqTemp = newCreditDetailListTemp.get(i).getSeq();
                        checkPlus = true;

                        for (int j = 0; j < newGuarantorDetailViewItem.getNewCreditDetailViewList().size(); j++) {
                            if (newGuarantorDetailViewItem.getNewCreditDetailViewList().get(j).getSeq() == seqTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        }

                    } else if (newCreditDetailListTemp.get(i).isNoFlag() == false) {
                        if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                            hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
                        }
                    }
                }

                if (newGuarantorDetailView.getNewCreditDetailViewList().size() > 0) {
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setNewCreditDetailViewList(newGuarantorDetailView.getNewCreditDetailViewList());
                }

                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTotalLimitGuaranteeAmount(summary);

            } else {
                log.info("onSaveGuarantorInfoDlg ::: Undefined modeForButton !!");
                complete = false;
            }
        }

        complete = true;
        newCreditFacilityView.setTotalGuaranteeAmount(creditFacProposeControl.calTotalGuaranteeAmount(newCreditFacilityView.getNewGuarantorDetailViewList()));
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteGuarantorInfo() {
        log.info("onDeleteGuarantorInfo ::: {}", newGuarantorDetailViewItem.getTcgLgNo());

        for (int i = 0; i < newGuarantorDetailViewItem.getNewCreditDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }

        newCreditFacilityView.getNewGuarantorDetailViewList().remove(newGuarantorDetailViewItem);
        log.info("delete success");
        newCreditFacilityView.setTotalGuaranteeAmount(creditFacProposeControl.calTotalGuaranteeAmount(newCreditFacilityView.getNewGuarantorDetailViewList()));
    }

    //  END Guarantor //

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        newConditionDetailView = new NewConditionDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg() {
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            NewConditionDetailView newConditionDetailViewAdd = new NewConditionDetailView();
            newConditionDetailViewAdd.setLoanType(newConditionDetailView.getLoanType());
            newConditionDetailViewAdd.setConditionDesc(newConditionDetailView.getConditionDesc());
            newCreditFacilityView.getNewConditionDetailViewList().add(newConditionDetailViewAdd);
            complete = true;

        } else {
            log.info("onSaveConditionInfoDlg ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }


    public void onDeleteConditionInfo() {
        log.info("onDeleteConditionInfo :: ");
        newCreditFacilityView.getNewConditionDetailViewList().remove(selectConditionItem);
    }

    // END Condition Information //

    // Database Action
    public void onSaveCreditFacPropose() {
        log.info("onSaveCreditFacPropose ::: ModeForDB  {}", modeForDB);

        try {
            if ((newCreditFacilityView.getInvestedCountry().getId() != 0) && (newCreditFacilityView.getLoanRequestType().getId() != 0)) {
                if ((newCreditFacilityView.getNewCreditDetailViewList().size() > 0) && (newCreditFacilityView.getNewCollateralInfoViewList().size() > 0)
                        && (newCreditFacilityView.getNewConditionDetailViewList().size() > 0) && (newCreditFacilityView.getNewGuarantorDetailViewList().size() > 0)) {
                    if (modeForDB != null && modeForDB.equals(ModeForDB.ADD_DB)) {
                        creditFacProposeControl.onSaveNewCreditFacility(newCreditFacilityView, workCaseId);
                        messageHeader = msg.get("app.header.save.success");
                        message = msg.get("app.propose.response.save.success");
                    } else if (modeForDB != null && modeForDB.equals(ModeForDB.EDIT_DB)) {
                        creditFacProposeControl.onSaveNewCreditFacility(newCreditFacilityView, workCaseId);
                        messageHeader = msg.get("app.header.save.success");
                        message = msg.get("app.propose.response.save.success");
                    } else {
                        messageHeader = msg.get("app.propose.response.cannot.save");
                        message = msg.get("app.propose.response.desc.cannot.save");

                    }

                    onCreation();

                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else {
                messageHeader = msg.get("app.propose.response.cannot.save");
                message = msg.get("app.propose.response.desc.cannot.save");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.propose.response.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("app.propose.response.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.propose.response.save.failed") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }

    }


    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
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

    public NewCreditFacilityView getNewCreditFacilityView() {
        return newCreditFacilityView;
    }

    public void setNewCreditFacilityView(NewCreditFacilityView newCreditFacilityView) {
        this.newCreditFacilityView = newCreditFacilityView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NewCreditDetailView getNewCreditDetailView() {
        return newCreditDetailView;
    }

    public void setNewCreditDetailView(NewCreditDetailView newCreditDetailView) {
        this.newCreditDetailView = newCreditDetailView;
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

    public List<Disbursement> getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(List<Disbursement> disbursementList) {
        this.disbursementList = disbursementList;
    }

    public List<LoanPurpose> getLoanPurposeList() {
        return loanPurposeList;
    }

    public void setLoanPurposeList(List<LoanPurpose> loanPurposeList) {
        this.loanPurposeList = loanPurposeList;
    }

    public NewConditionDetailView getNewConditionDetailView() {
        return newConditionDetailView;
    }

    public void setNewConditionDetailView(NewConditionDetailView newConditionDetailView) {
        this.newConditionDetailView = newConditionDetailView;
    }

    public NewConditionDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(NewConditionDetailView selectConditionItem) {
        this.selectConditionItem = selectConditionItem;
    }

    public NewGuarantorDetailView getNewGuarantorDetailViewItem() {
        return newGuarantorDetailViewItem;
    }

    public void setNewGuarantorDetailViewItem(NewGuarantorDetailView newGuarantorDetailViewItem) {
        this.newGuarantorDetailViewItem = newGuarantorDetailViewItem;
    }

    public NewGuarantorDetailView getNewGuarantorDetailView() {
        return newGuarantorDetailView;
    }

    public void setNewGuarantorDetailView(NewGuarantorDetailView newGuarantorDetailView) {
        this.newGuarantorDetailView = newGuarantorDetailView;
    }

    public List<CustomerInfoView> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<CustomerInfoView> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public NewCollateralInfoView getNewCollateralInfoView() {
        return newCollateralInfoView;
    }

    public void setNewCollateralInfoView(NewCollateralInfoView newCollateralInfoView) {
        this.newCollateralInfoView = newCollateralInfoView;
    }

    public NewCollateralInfoView getSelectCollateralDetailView() {
        return selectCollateralDetailView;
    }

    public void setSelectCollateralDetailView(NewCollateralInfoView selectCollateralDetailView) {
        this.selectCollateralDetailView = selectCollateralDetailView;
    }

    public NewSubCollateralDetailView getNewSubCollateralDetailView() {
        return newSubCollateralDetailView;
    }

    public void setNewSubCollateralDetailView(NewSubCollateralDetailView newSubCollateralDetailView) {
        this.newSubCollateralDetailView = newSubCollateralDetailView;
    }

    public List<SubCollateralType> getSubCollateralTypeList() {
        return subCollateralTypeList;
    }

    public void setSubCollateralTypeList(List<SubCollateralType> subCollateralTypeList) {
        this.subCollateralTypeList = subCollateralTypeList;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public int getRowSpanNumber() {
        return rowSpanNumber;
    }

    public void setRowSpanNumber(int rowSpanNumber) {
        this.rowSpanNumber = rowSpanNumber;
    }

    public boolean isModeEdit() {
        return modeEdit;
    }

    public void setModeEdit(boolean modeEdit) {
        this.modeEdit = modeEdit;
    }

    public NewCreditDetailView getProposeCreditDetailSelected() {
        return proposeCreditDetailSelected;
    }

    public void setProposeCreditDetailSelected(NewCreditDetailView proposeCreditDetailSelected) {
        this.proposeCreditDetailSelected = proposeCreditDetailSelected;
    }

    public NewCreditTierDetailView getNewCreditTierDetailView() {
        return newCreditTierDetailView;
    }

    public void setNewCreditTierDetailView(NewCreditTierDetailView newCreditTierDetailView) {
        this.newCreditTierDetailView = newCreditTierDetailView;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

/*    public List<CreditTypeDetailView> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetailView> creditTypeDetailList) {
        this.creditTypeDetailList = creditTypeDetailList;
    }*/

    public List<PrdProgramToCreditType> getPrdProgramToCreditTypeList() {
        return prdProgramToCreditTypeList;
    }

    public void setPrdProgramToCreditTypeList(List<PrdProgramToCreditType> prdProgramToCreditTypeList) {
        this.prdProgramToCreditTypeList = prdProgramToCreditTypeList;
    }

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
    }

    public List<NewCreditDetailView> getNewCollateralCreditDetailList() {
        return newCollateralCreditDetailList;
    }

    public void setNewCollateralCreditDetailList(List<NewCreditDetailView> newCollateralCreditDetailList) {
        this.newCollateralCreditDetailList = newCollateralCreditDetailList;
    }

    public int getRowIndexGuarantor() {
        return rowIndexGuarantor;
    }

    public void setRowIndexGuarantor(int rowIndexGuarantor) {
        this.rowIndexGuarantor = rowIndexGuarantor;
    }

    public int getRowIndexCollateral() {
        return rowIndexCollateral;
    }

    public void setRowIndexCollateral(int rowIndexCollateral) {
        this.rowIndexCollateral = rowIndexCollateral;
    }

    public NewCollateralHeadDetailView getNewCollateralHeadDetailView() {
        return newCollateralHeadDetailView;
    }

    public void setNewCollateralHeadDetailView(NewCollateralHeadDetailView newCollateralHeadDetailView) {
        this.newCollateralHeadDetailView = newCollateralHeadDetailView;
    }

    public int getRowSubIndex() {
        return rowSubIndex;
    }

    public void setRowSubIndex(int rowSubIndex) {
        this.rowSubIndex = rowSubIndex;
    }

    public NewSubCollateralDetailView getSubCollateralDetailItem() {
        return subCollateralDetailItem;
    }

    public void setSubCollateralDetailItem(NewSubCollateralDetailView subCollateralDetailItem) {
        this.subCollateralDetailItem = subCollateralDetailItem;
    }

    public NewCollateralHeadDetailView getCollateralHeaderDetailItem() {
        return collateralHeaderDetailItem;
    }

    public void setCollateralHeaderDetailItem(NewCollateralHeadDetailView collateralHeaderDetailItem) {
        this.collateralHeaderDetailItem = collateralHeaderDetailItem;
    }

    public int getRowCollHeadIndex() {
        return rowCollHeadIndex;
    }

    public void setRowCollHeadIndex(int rowCollHeadIndex) {
        this.rowCollHeadIndex = rowCollHeadIndex;
    }

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public List<MortgageType> getMortgageTypeList() {
        return mortgageTypeList;
    }

    public void setMortgageTypeList(List<MortgageType> mortgageTypeList) {
        this.mortgageTypeList = mortgageTypeList;
    }

    public List<NewCreditTierDetailView> getNewCreditTierDetailViewList() {
        return newCreditTierDetailViewList;
    }

    public void setNewCreditTierDetailViewList(List<NewCreditTierDetailView> newCreditTierDetailViewList) {
        this.newCreditTierDetailViewList = newCreditTierDetailViewList;
    }

    public List<NewCollateralHeadDetailView> getNewCollateralHeadDetailViewList() {
        return newCollateralHeadDetailViewList;
    }

    public void setNewCollateralHeadDetailViewList(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList) {
        this.newCollateralHeadDetailViewList = newCollateralHeadDetailViewList;
    }

    public List<NewSubCollateralDetailView> getNewSubCollateralDetailViewList() {
        return newSubCollateralDetailViewList;
    }

    public void setNewSubCollateralDetailViewList(List<NewSubCollateralDetailView> newSubCollateralDetailViewList) {
        this.newSubCollateralDetailViewList = newSubCollateralDetailViewList;
    }

    public List<NewSubCollateralDetailView> getRelatedWithAllList() {
        return relatedWithAllList;
    }

    public void setRelatedWithAllList(List<NewSubCollateralDetailView> relatedWithAllList) {
        this.relatedWithAllList = relatedWithAllList;
    }

    public List<CustomerInfoView> getCollateralOwnerUwAllList() {
        return collateralOwnerUwAllList;
    }

    public void setCollateralOwnerUwAllList(List<CustomerInfoView> collateralOwnerUwAllList) {
        this.collateralOwnerUwAllList = collateralOwnerUwAllList;
    }

    public CustomerInfoView getCollateralOwnerUW() {
        return collateralOwnerUW;
    }

    public void setCollateralOwnerUW(CustomerInfoView collateralOwnerUW) {
        this.collateralOwnerUW = collateralOwnerUW;
    }

    public NewSubCollateralDetailView getRelatedWithSelected() {
        return relatedWithSelected;
    }

    public void setRelatedWithSelected(NewSubCollateralDetailView relatedWithSelected) {
        this.relatedWithSelected = relatedWithSelected;
    }

    public boolean isModeEditReduceFront() {
        return modeEditReduceFront;
    }

    public void setModeEditReduceFront(boolean modeEditReduceFront) {
        this.modeEditReduceFront = modeEditReduceFront;
    }

    public boolean isModeEditReducePricing() {
        return modeEditReducePricing;
    }

    public void setModeEditReducePricing(boolean modeEditReducePricing) {
        this.modeEditReducePricing = modeEditReducePricing;
    }

    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }

    public boolean getReducePricePanelRendered() {
        return reducePricePanelRendered;
    }

    public void setReducePricePanelRendered(boolean reducePricePanelRendered) {
        this.reducePricePanelRendered = reducePricePanelRendered;
    }

    public boolean isCannotEditStandard() {
        return cannotEditStandard;
    }

    public void setCannotEditStandard(boolean cannotEditStandard) {
        this.cannotEditStandard = cannotEditStandard;
    }

    public List<NewCreditDetailView> getNewCreditDetailListTemp() {
        return newCreditDetailListTemp;
    }

    public void setNewCreditDetailListTemp(List<NewCreditDetailView> newCreditDetailListTemp) {
        this.newCreditDetailListTemp = newCreditDetailListTemp;
    }

    public List<NewCreditDetailView> getNewGuarantorCreditDetailList() {
        return newGuarantorCreditDetailList;
    }

    public void setNewGuarantorCreditDetailList(List<NewCreditDetailView> newGuarantorCreditDetailList) {
        this.newGuarantorCreditDetailList = newGuarantorCreditDetailList;
    }
}

