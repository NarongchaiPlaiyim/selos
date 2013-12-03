package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.NewCollateralInfoTransform;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
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

    enum ModeForDB{ADD_DB, EDIT_DB, CANCEL_DB}

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

    private BasicInfo basicInfo;

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

    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;
    private List<Customer> guarantorList;
    private BigDecimal sumTotalGuarantor;

    private List<CreditTypeDetailView> creditTypeDetailList;
    private CreditTypeDetailView creditTypeDetailView;

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

    public CreditFacPropose() {}


    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", new Long(2));    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox
        user = (User) session.getAttribute("user");

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
            modeForDB = ModeForDB.ADD_DB;
        }

        if (workCaseId != null) {
            if (guarantorList == null) {
                guarantorList = new ArrayList<Customer>();
                guarantorList = creditFacProposeControl.getListOfGuarantor(workCaseId);
            }

            if (productGroup == null) {
                basicInfo = creditFacProposeControl.getBasicByWorkCaseId(workCaseId);

                if (basicInfo != null) {
                    productGroup = basicInfo.getProductGroup();
                }
            }
        }


        if (newCreditFacilityView == null) {
            newCreditFacilityView = new NewCreditFacilityView();
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

        if(mortgageTypeList == null){
            mortgageTypeList = new ArrayList<MortgageType>();
        }

        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
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
        sumTotalGuarantor = BigDecimal.ZERO;
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

        newCollateralInfoView.setAppraisalDate(DateTime.now().toDate());
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
        newCollateralInfoView.getNewCollateralHeadDetailViewList().add(newCollateralHeadDetailView);

        log.info("onCallRetrieveAppraisalReportInfo End");

    }

    //  Start Propose Credit Information  //

    public void onChangeProductProgram() {
        ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
        log.debug("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = null;
        newCreditDetailView.setProductCode("");
        newCreditDetailView.setProjectCode("");

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        if (prdProgramToCreditTypeList == null) {
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " + prdProgramToCreditTypeList.size());
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType :::: creditType : {}", newCreditDetailView.getCreditType().getId());
        if ((newCreditDetailView.getProductProgram().getId() != 0) && (newCreditDetailView.getCreditType().getId() != 0)) {
            ProductProgram productProgram = productProgramDAO.findById(newCreditDetailView.getProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById(newCreditDetailView.getCreditType().getId());

            if (productProgram != null && creditType != null) {
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);
                ProductFormula productFormula = productFormulaDAO.findProductFormula(prdProgramToCreditType);
                log.debug("onChangeCreditType :::: productFormula : {}", productFormula.getId());

                if (productFormula != null) {
                    newCreditDetailView.setProductCode(productFormula.getProductCode());
                    newCreditDetailView.setProjectCode(productFormula.getProjectCode());
                }
            }
        }

    }

    public void calculateSumLimitOfCreditPropose() {
        BigDecimal sumOfLimit = BigDecimal.ZERO;

        if (newCreditFacilityView.getNewCreditDetailViewList().size() > 0) {
            for (NewCreditDetailView proposeCreditDetail : newCreditFacilityView.getNewCreditDetailViewList()) {
                sumOfLimit = sumOfLimit.add(proposeCreditDetail.getLimit());
            }
        }

        newCreditFacilityView.setTotalPropose(sumOfLimit);
    }


    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        newCreditDetailView = new NewCreditDetailView();
        modeForButton = ModeForButton.ADD;

    }

    public void onChangeRequestType() {
        log.info("newCreditDetailView.getRequestType() :: {}", newCreditDetailView.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();

        if (newCreditDetailView.getRequestType() == 0) {   //change
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
        } else if (newCreditDetailView.getRequestType() == 1) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
            }
        }
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
            newCreditDetailView.setReduceFlag(proposeCreditDetailSelected.isReduceFlag());
            newCreditDetailView.setReduceFrontEndFlag(proposeCreditDetailSelected.isReduceFrontEndFlag());
            newCreditDetailView.setStandardBasePrice(proposeCreditDetailSelected.getStandardBasePrice());
            newCreditDetailView.setStandardInterest(proposeCreditDetailSelected.getStandardInterest());
            newCreditDetailView.setSuggestBasePrice(proposeCreditDetailSelected.getSuggestBasePrice());
            newCreditDetailView.setSuggestInterest(proposeCreditDetailSelected.getSuggestInterest());
            newCreditDetailView.setFrontEndFee(proposeCreditDetailSelected.getFrontEndFee());
            //newCreditDetailView.setLoanPurpose(newCreditDetailView.getLoanPurpose());
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
           &&(newCreditDetailView.getLoanPurpose().getId() != 0) && (newCreditDetailView.getDisbursement().getId() != 0))
        {
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
                creditDetailAdd.setReduceFlag(newCreditDetailView.isReduceFlag());
                creditDetailAdd.setReduceFrontEndFlag(newCreditDetailView.isReduceFrontEndFlag());
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
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReduceFlag(newCreditDetailView.isReduceFlag());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReduceFrontEndFlag(newCreditDetailView.isReduceFrontEndFlag());
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
            calculateSumLimitOfCreditPropose();
            log.info("seq++ of credit after add complete proposeCredit :: {}", seq);
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
            log.info("use 0 ");
            newCreditFacilityView.getNewCreditDetailViewList().remove(rowIndex);
            calculateSumLimitOfCreditPropose();
        } else {
            log.info("use > 0 ");
            messageHeader = "เกิดข้อผิดพลาด";
            message = "มีการใช้งาน Credit Type Record นี้แล้ว";
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

    public List<CreditTypeDetailView> findCreditFacility() {

        creditTypeDetailList = new ArrayList<CreditTypeDetailView>();  // find credit existing and propose in this workCase

        if (newCreditFacilityView.getNewCreditDetailViewList().size() > 0) {
            for (int i = 0; i < newCreditFacilityView.getNewCreditDetailViewList().size(); i++) {
                creditTypeDetailView = new CreditTypeDetailView();
                creditTypeDetailView.setSeq(newCreditFacilityView.getNewCreditDetailViewList().get(i).getSeq());
                creditTypeDetailView.setAccount("-");
                creditTypeDetailView.setRequestType(newCreditFacilityView.getNewCreditDetailViewList().get(i).getRequestType());
                creditTypeDetailView.setProductProgram(newCreditFacilityView.getNewCreditDetailViewList().get(i).getProductProgram().getName());
                creditTypeDetailView.setCreditFacility(newCreditFacilityView.getNewCreditDetailViewList().get(i).getCreditType().getName());
                creditTypeDetailView.setLimit(newCreditFacilityView.getNewCreditDetailViewList().get(i).getLimit());
                creditTypeDetailList.add(creditTypeDetailView);
            }
        }

        return creditTypeDetailList;
    }

    //  Start Propose Collateral Information  //
    public void onAddProposeCollInfo() {
        log.info("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCreditDetailViewList().size());
        modeForButton = ModeForButton.ADD;
        newCollateralInfoView = new NewCollateralInfoView();
        newCollateralInfoView.setCreditTypeDetailViewList(findCreditFacility());
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
        creditTypeDetailList = findCreditFacility();  //all creditType that add this time
        newCollateralInfoView.setCreditTypeDetailViewList(creditTypeDetailList);

        for (int i = 0; i < selectCollateralDetailView.getCreditTypeDetailViewList().size(); i++) {
            for (int j = tempSeq; j < creditTypeDetailList.size(); j++) {
                log.info("creditType at " + j + " seq is     " + creditTypeDetailList.get(j).getSeq());

                if (selectCollateralDetailView.getCreditTypeDetailViewList().get(i).getSeq() == creditTypeDetailList.get(j).getSeq()) {
                    newCollateralInfoView.getCreditTypeDetailViewList().get(j).setNoFlag(true);
                    tempSeq = j;
                }
                continue;
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

            for(NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralInfoView.getNewCollateralHeadDetailViewList())
            {
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollateralHeadDetailView.getPotentialCollateral().getId());
                CollateralType  collTypePercentLTV = collateralTypeDAO.findById(newCollateralHeadDetailView.getCollTypePercentLTV().getId());
                CollateralType  headCollType = collateralTypeDAO.findById(newCollateralHeadDetailView.getHeadCollType().getId());

                NewCollateralHeadDetailView newCollateralHeadDetailAdd  = new NewCollateralHeadDetailView();
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

            for (CreditTypeDetailView creditTypeDetail : newCollateralInfoView.getCreditTypeDetailViewList()) {
                log.info("creditTypeDetail.isNoFlag()  :: {}", creditTypeDetail.isNoFlag());
                if (creditTypeDetail.isNoFlag()) {
                    proposeCollateralInfoAdd.getCreditTypeDetailViewList().add(creditTypeDetail);
                    seqTemp = creditTypeDetailView.getSeq();
                    hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                }
            }

            newCreditFacilityView.getNewCollateralInfoViewList().add(proposeCollateralInfoAdd);

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

            newCollateralInfoView.setCreditTypeDetailViewList(new ArrayList<CreditTypeDetailView>());

            for (int i = 0; i < creditTypeDetailList.size(); i++) {
                if (creditTypeDetailList.get(i).isNoFlag() == true) {
                    newCollateralInfoView.getCreditTypeDetailViewList().add(creditTypeDetailList.get(i));
                    seqTemp = creditTypeDetailList.get(i).getSeq();
                    checkPlus = true;

                    for (int j = 0; j < selectCollateralDetailView.getCreditTypeDetailViewList().size(); j++) {
                        if (selectCollateralDetailView.getCreditTypeDetailViewList().get(j).getSeq() == seqTemp) {
                            checkPlus = false;
                        }
                    }

                    if (checkPlus) {
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                    }

                } else if (creditTypeDetailList.get(i).isNoFlag() == false) {
                    if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                        hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
                    }
                }
            }
            newCreditFacilityView.getNewCollateralInfoViewList().get(rowIndexCollateral).setCreditTypeDetailViewList(newCollateralInfoView.getCreditTypeDetailViewList());


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
        for (int i = 0; i < selectCollateralDetailView.getCreditTypeDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }
        newCreditFacilityView.getNewCollateralInfoViewList().remove(selectCollateralDetailView);
    }

    // for sub collateral dialog
    public void onAddCollateralOwnerUW() {
        log.info("onAddCollateralOwnerUW :: {} ", newSubCollateralDetailView.getCollateralOwnerUW());
        newSubCollateralDetailView.getCollateralOwnerUWList().add(newSubCollateralDetailView.getCollateralOwnerUW());
        newSubCollateralDetailView.setCollateralOwnerUW("");
    }

    public void onDeleteCollateralOwnerUW(int row) {
        log.info("row :: {} ", row);
        newSubCollateralDetailView.getCollateralOwnerUWList().remove(row);
    }

    public void onAddMortgageType() {
        MortgageType mortgageType = mortgageTypeDAO.findById(newSubCollateralDetailView.getMortgageType().getId());
        log.info("onAddMortgageType :: {} ", newSubCollateralDetailView.getMortgageType());
        newSubCollateralDetailView.getMortgageList().add(mortgageType.getMortgage());
    }

    public void onDeleteMortgageType(int row) {
        newSubCollateralDetailView.getMortgageList().remove(row);
    }

    public void onAddRelatedWith() {
        log.info("onAddRelatedWith :: {} ", newSubCollateralDetailView.getRelatedWith());
        newSubCollateralDetailView.getRelatedWithList().add(newSubCollateralDetailView.getRelatedWith());
        newSubCollateralDetailView.setRelatedWith("");
    }

    public void onDeleteRelatedWith(int row) {
        newSubCollateralDetailView.getRelatedWithList().remove(row);
    }

    // end for sub collateral dialog

    //  END Propose Collateral Information  //

    // Start Add SUB Collateral
    public void onAddSubCollateral() {
        log.info("onAddSubCollateral and rowCollHeadIndex :: {}", rowCollHeadIndex);
        newSubCollateralDetailView = new NewSubCollateralDetailView();
        modeForSubColl = ModeForButton.ADD;

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

        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)){
            log.info("modeForSubColl ::: {}", modeForSubColl);
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
        newGuarantorDetailView.setCreditTypeDetailViewList(findCreditFacility());

    }

    public void onEditGuarantorInfo() {
        log.info("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;
        int tempSeq = 0;

        if (newGuarantorDetailViewItem != null) {
            newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());
            creditTypeDetailList = findCreditFacility();  //all creditType that add this time
            newGuarantorDetailView.setCreditTypeDetailViewList(creditTypeDetailList);

            for (int i = 0; i < newGuarantorDetailViewItem.getCreditTypeDetailViewList().size(); i++) {
                for (int j = tempSeq; j < creditTypeDetailList.size(); j++) {
                    log.info("creditType at " + j + " seq is     " + creditTypeDetailList.get(j).getSeq());

                    if (newGuarantorDetailViewItem.getCreditTypeDetailViewList().get(i).getSeq() == creditTypeDetailList.get(j).getSeq()) {
                        newGuarantorDetailView.getCreditTypeDetailViewList().get(j).setNoFlag(true);
                        newGuarantorDetailView.getCreditTypeDetailViewList().get(j).setGuaranteeAmount(newGuarantorDetailViewItem.getCreditTypeDetailViewList().get(i).getGuaranteeAmount());
                        tempSeq = j;
                    }
                    continue;
                }
            }

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
                Customer guarantor = customerDAO.findById(newGuarantorDetailView.getGuarantorName().getId());
                NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
                guarantorDetailAdd.setGuarantorName(guarantor);
                guarantorDetailAdd.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());

                for (CreditTypeDetailView creditTypeDetailView : newGuarantorDetailView.getCreditTypeDetailViewList()) {

                    if (creditTypeDetailView.isNoFlag()) {
                        guarantorDetailAdd.getCreditTypeDetailViewList().add(creditTypeDetailView);
                        summary = summary.add(creditTypeDetailView.getGuaranteeAmount());
                        seqTemp = creditTypeDetailView.getSeq();
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                    }

                    guarantorDetailAdd.setGuaranteeAmount(summary);
                }

                if (guarantorDetailAdd.getCreditTypeDetailViewList().size() > 0) {
                    newCreditFacilityView.getNewGuarantorDetailViewList().add(guarantorDetailAdd);
                } else {
                    //dialog error
                }

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("modeForButton ::: {}", modeForButton);
                Customer guarantor = customerDAO.findById(newGuarantorDetailView.getGuarantorName().getId());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(guarantor);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
                boolean checkPlus;

                if (newGuarantorDetailView.getCreditTypeDetailViewList() != null) {
                    newGuarantorDetailView.setCreditTypeDetailViewList(new ArrayList<CreditTypeDetailView>());

                    for (int i = 0; i < creditTypeDetailList.size(); i++) {
                        if (creditTypeDetailList.get(i).isNoFlag() == true) {
                            newGuarantorDetailView.getCreditTypeDetailViewList().add(creditTypeDetailList.get(i));
                            summary = summary.add(creditTypeDetailList.get(i).getGuaranteeAmount());
                            seqTemp = creditTypeDetailList.get(i).getSeq();
                            checkPlus = true;

                            for (int j = 0; j < newGuarantorDetailViewItem.getCreditTypeDetailViewList().size(); j++) {
                                if (newGuarantorDetailViewItem.getCreditTypeDetailViewList().get(j).getSeq() == seqTemp) {
                                    checkPlus = false;
                                }
                            }

                            if (checkPlus) {
                                hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                            }

                        } else if (creditTypeDetailList.get(i).isNoFlag() == false) {
                            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
                            }
                        }
                    }

                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setCreditTypeDetailViewList(newGuarantorDetailView.getCreditTypeDetailViewList());

                }

                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuaranteeAmount(summary);

            } else {
                log.info("onSaveGuarantorInfoDlg ::: Undefined modeForButton !!");
                complete = false;
            }
        }

        complete = true;
        calculationSummaryGuarantor();
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteGuarantorInfo() {
        log.info("onDeleteGuarantorInfo ::: {}", newGuarantorDetailViewItem.getTcgLgNo());

        for (int i = 0; i < newGuarantorDetailViewItem.getCreditTypeDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }

        newCreditFacilityView.getNewGuarantorDetailViewList().remove(newGuarantorDetailViewItem);
        log.info("delete success");
        calculationSummaryGuarantor();
    }

    public void calculationSummaryGuarantor() {
        sumTotalGuarantor = BigDecimal.ZERO;
        for (int i = 0; i < newCreditFacilityView.getNewGuarantorDetailViewList().size(); i++) {
            sumTotalGuarantor = sumTotalGuarantor.add(newCreditFacilityView.getNewGuarantorDetailViewList().get(i).getGuaranteeAmount());
        }
        newCreditFacilityView.setTotalGuaranteeAmount(sumTotalGuarantor);
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
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("");

            creditFacProposeControl.onSaveNewCreditFacility(newCreditFacilityView,workCaseId,user);
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }

    }


    public void onCancelCreditFacPropose() {
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelCreditFacPropose ::: ");

        onCreation();
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

    public List<Customer> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<Customer> guarantorList) {
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

    public List<CreditTypeDetailView> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetailView> creditTypeDetailList) {
        this.creditTypeDetailList = creditTypeDetailList;
    }

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


    public CreditTypeDetailView getCreditTypeDetailView() {
        return creditTypeDetailView;
    }

    public void setCreditTypeDetailView(CreditTypeDetailView creditTypeDetailView) {
        this.creditTypeDetailView = creditTypeDetailView;
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
}

