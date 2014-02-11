package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.NewCollateralTransform;
import com.clevel.selos.transform.business.CollateralBizTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    private List<DisbursementType> disbursementList;
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
    private NewCreditDetailView newCreditDetailSelected;
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
    private List<MortgageType> mortgageTypeList;
    private List<NewCollateralSubView> relatedWithAllList;
    private NewCollateralSubView relatedWithSelected;
    private List<CustomerInfoView> collateralOwnerUwAllList;   // case from select database must to transform to view before to use continue
    private CustomerInfoView collateralOwnerUW;

    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;
    private List<CollateralType> headCollateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;
    private List<CustomerInfoView> guarantorList;     // case from select database must to transform to view before to use continue

    private List<ProposeCreditDetailView> proposeCreditDetailListTemp;
    private List<ProposeCreditDetailView> newCollateralCreditDetailList;
    private List<ProposeCreditDetailView> newGuarantorCreditDetailList;
    private List<ProposeCreditDetailView> proposeCreditDetailViewList;

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
    NewCollateralTransform collateralInfoTransform;
    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    LoanPurposeDAO loanPurposeDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    SpecialProgramDAO specialProgramDAO;
    @Inject
    TCGDAO TCGDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    ExSummaryControl exSummaryControl;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    private COMSInterface comsInterface;
    @Inject
    private CollateralBizTransform collateralBizTransform;

    public CreditFacPropose() {
    }

    public void preRender() {

        log.info("preRender ::: setSession ");

        HttpSession session = FacesUtil.getSession(true);

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
            user = (User) session.getAttribute("user");

        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        log.info("onCreation.");

        if (workCaseId != null) {

            modeForDB = ModeForDB.ADD_DB;
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            hashSeqCredit = new Hashtable<String, String>();

            try {
                newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);

                if (newCreditFacilityView != null) {
                    log.info("newCreditFacilityView.id ::: {}", newCreditFacilityView.getId());

                    modeForDB = ModeForDB.EDIT_DB;
                    proposeCreditDetailViewList = creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId);
                    log.info("proposeCreditDetailViewList :: {}", proposeCreditDetailViewList.size());


                }

            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }

            log.info("onCreation :: modeForDB :: {}", modeForDB);

            basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if (basicInfo == null) {
                specialProgramBasicInfo = null;
                productGroup = null;
            } else {
                log.info("basicInfo.id ::: {}", basicInfo.getId());
                specialProgramBasicInfo = basicInfo.getSpecialProgram();
                productGroup = basicInfo.getProductGroup();
            }

            tcg = TCGDAO.findByWorkCaseId(workCaseId);

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
            canAddTier = false;
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

        if (disbursementList == null) {
            disbursementList = new ArrayList<DisbursementType>();
        }

        if (newConditionDetailView == null) {
            newConditionDetailView = new NewConditionDetailView();
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

        if (headCollateralTypeList == null) {
            headCollateralTypeList = new ArrayList<CollateralType>();
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
//        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        headCollateralTypeList = collateralTypeDAO.findAll();
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
    public void onRetrieveStandardPrice() {
        BaseRate baseRate = baseRateDAO.findById(1);     //test
        NewCreditDetailView creditDetailRetrieve = new NewCreditDetailView();
        creditDetailRetrieve.setStandardBasePrice(baseRate);
        creditDetailRetrieve.setStandardInterest(BigDecimal.valueOf(-1.75));
    }

    public void onRetrievePricingFee() {
        // test create data from retrieving
        NewCreditDetailView creditDetailRetrieve = new NewCreditDetailView();
        BaseRate baseRate = baseRateDAO.findById(1);
        BigDecimal installment = BigDecimal.ZERO;
        creditDetailRetrieve.setStandardBasePrice(baseRate);
        creditDetailRetrieve.setStandardInterest(BigDecimal.valueOf(-1.75));

        if (creditDetailRetrieve.getStandardInterest().doubleValue() < 0) {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " " + creditDetailRetrieve.getStandardInterest());
        } else {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " + " + creditDetailRetrieve.getStandardInterest());
        }
        BigDecimal sumStandard = baseRate.getValue().add(creditDetailRetrieve.getStandardInterest());
        log.info("sumStandard ::: {}", sumStandard);
        BigDecimal sumSuggest = baseRate.getValue().add(creditDetailRetrieve.getStandardInterest());
        log.info("sumSuggest ::: {}", sumSuggest);
        BigDecimal sumFinal = baseRate.getValue().add(creditDetailRetrieve.getStandardInterest());
        log.info("sumFinal ::: {}", sumFinal);
        //****** tier test create ********//
        newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        newCreditTierDetailView = new NewCreditTierDetailView();
        newCreditTierDetailView.setStandardBasePrice(baseRate);
        newCreditTierDetailView.setStandardPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setStandardInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setStandardPriceSum(sumStandard);
        newCreditTierDetailView.setSuggestBasePrice(baseRate);
        newCreditTierDetailView.setSuggestPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setSuggestInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setSuggestPriceSum(sumSuggest);
        newCreditTierDetailView.setFinalBasePrice(baseRate);
        newCreditTierDetailView.setFinalPriceRate(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setFinalInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setFinalPriceSum(sumFinal);
        newCreditTierDetailView.setCanEdit(false);
        newCreditTierDetailViewList.add(newCreditTierDetailView);

        for (NewCreditDetailView proposeCreditDetail : newCreditFacilityView.getNewCreditDetailViewList()) {
            proposeCreditDetail.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            log.info("proposeCreditDetail :: {}", proposeCreditDetail.getNewCreditTierDetailViewList());
            proposeCreditDetail.setStandardBasePrice(baseRate);
            proposeCreditDetail.setSuggestBasePrice(baseRate);
            proposeCreditDetail.setStandardInterest(creditDetailRetrieve.getStandardInterest());
            proposeCreditDetail.setSuggestInterest(creditDetailRetrieve.getSuggestInterest());
            proposeCreditDetail.setInstallment(newCreditTierDetailView.getInstallment());
            calculateInstallment(proposeCreditDetail);
            log.info("proposeCreditDetail.getInstallment ::  {}", proposeCreditDetail.getInstallment());
        }
    }

    public void calculateInstallment(NewCreditDetailView proposeCreditDetail) {
        log.info("proposeCreditDetail : {}", proposeCreditDetail);
        if(proposeCreditDetail != null && proposeCreditDetail.getNewCreditTierDetailViewList() != null && proposeCreditDetail.getNewCreditTierDetailViewList().size() > 0){
            for (NewCreditTierDetailView newCreditTierDetailView : proposeCreditDetail.getNewCreditTierDetailViewList()) {
//                  Installment = (อัตราดอกเบี้ยต่อเดือน * Limit * (1 + อัตราดอกเบี้ยต่อเดือน)ยกกำลัง tenors(month)) / ((1 + อัตราดอกเบี้ยต่อเดือน) ยกกำลัง tenors(month) - 1)
                // อัตราดอกเบี้ยต่อเดือน = baseRate.value +  interest + 1% / 12
                BigDecimal twelve = BigDecimal.valueOf(12);
                BigDecimal baseRate = BigDecimal.ZERO;
                BigDecimal interest = BigDecimal.ZERO;
                if(newCreditTierDetailView.getFinalBasePrice() != null){
                    baseRate = newCreditTierDetailView.getFinalBasePrice().getValue();
                }
                if(newCreditTierDetailView.getFinalInterest() != null){
                    interest = newCreditTierDetailView.getFinalInterest();
                }

                BigDecimal interestPerMonth = Util.divide(Util.add(baseRate,Util.add(interest,BigDecimal.ONE)),twelve);
                log.info("baseRate :: {}",baseRate);
                log.info("interest :: {}",interest);
                log.info("interestPerMonth :: {}",interestPerMonth);

                BigDecimal limit = BigDecimal.ZERO;
                int tenor = newCreditTierDetailView.getTenor();
                BigDecimal installment;

                if(proposeCreditDetail.getLimit() != null){
                    limit = proposeCreditDetail.getLimit();
                }

                log.info("limit :: {}",limit);
                log.info("tenor :: {}",tenor);

                installment = Util.divide(Util.multiply(Util.multiply(interestPerMonth,limit),(Util.add(BigDecimal.ONE,interestPerMonth)).pow(tenor)),
                        Util.subtract(Util.add(BigDecimal.ONE,interestPerMonth).pow(tenor),BigDecimal.ONE));
                log.info("installment : {}", installment);


                newCreditTierDetailView.setInstallment(installment);
            }
        }
    }

    public void onCallRetrieveAppraisalReportInfo() {
        String jobID = newCollateralView.getJobID();
        log.info("onCallRetrieveAppraisalReportInfo begin key is  :: {}", jobID);
        boolean flag = true;
        try {
            if (ModeForButton.ADD.equals(modeForButton)) {
                flag = checkJobIdExist(newCreditFacilityView.getNewCollateralViewList(), jobID);
                if (flag) {
                    newCollateralView = COMSInterface(jobID);
                    if (Util.isNull(newCollateralView)) {
                        newCollateralView = new NewCollateralView();
                    }
                } else {
                    log.debug("-- {}", message);
                    messageHeader = msg.get("app.propose.exception");
//                    message = msg.get("app.propose.error.delete.credit");
                    message = "มีรายการของ Collateral ที่ JobId นี้แล้ว";
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else {
                newCollateralView = COMSInterface(jobID);
            }
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
        }
        log.info("onCallRetrieveAppraisalReportInfo End");
    }

    private boolean checkJobIdExist(final List<NewCollateralView> viewList, String jobIDSearch) {
        for (NewCollateralView view : viewList) {
            if (Util.equals(view.getJobID(), jobIDSearch)) {
                return false;
            }
        }
        return true;
    }

    // Call COMSInterface
    public NewCollateralView COMSInterface(final String jobId) {
        log.info("onCallRetrieveAppraisalReportInfo begin jobId is  :: {}", jobId);

        AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(user.getId(), jobId);
        log.info("appraisalDataResult :: {}", appraisalDataResult.toString());

        if (!Util.isNull(appraisalDataResult) && ActionResult.SUCCEED.equals(appraisalDataResult.getActionResult())) {
            log.debug("-- call com-s is succeed ::: {}", ActionResult.SUCCEED);
            log.info("appraisalDataResult :: {}", appraisalDataResult.toString());
            newCollateralView = collateralBizTransform.transformCollateral(appraisalDataResult);
            return newCollateralView;
        } else {
            log.error("Exception : {}", appraisalDataResult.getActionResult());
            return null;
        }
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

            log.info("productProgram :: {}", productProgram.getId());
            log.info("creditType :: {}", creditType.getId());
            log.info("specialProgramBasicInfo.getId() :: {}", specialProgramBasicInfo.getId());
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
                        modeEditReduceFront = flagForModeDisable(productFormula.getReduceFrontEndFee());

                        reducePricePanelRendered = (modeEditReducePricing == true) ? true : false;
                        log.info("reducePricePanelRendered:: {}", reducePricePanelRendered);
                    }
                }
            }
        }
    }

    // 2:Y(false)can to edit , 1:N(true) cannot to edit
    public static boolean flagForModeDisable(int value) {
        return (value == 1) ? true : false;
    }

    public void onChangeRequestType() {
        log.info("newCreditDetailView.getRequestType() :: {}", newCreditDetailView.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();

        if (newCreditDetailView.getRequestType() == RequestTypes.CHANGE.value()) {   //change
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
            newCreditDetailView.getProductProgram().setId(0);
            cannotEditStandard = false;
            canAddTier = true;
        } else if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
                newCreditDetailView.getCreditType().setId(0);
            }
            cannotEditStandard = true;
            canAddTier = false;
        }
    }

    public void onRequestReducePrice() {
        log.info("reducePrice ::: {}", reducePrice);

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
        onChangeRequestType();
//        Cloner cloner = new Cloner();
//        newCreditDetailView = cloner.deepClone(newCreditDetailSelected);
//        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(newCreditDetailView.getProductProgram());
        log.info("newCreditDetailSelected.getProductProgram().getId() ::: {}", newCreditDetailSelected.getProductProgram().getId());
        ProductProgram productProgram = productProgramDAO.findById(newCreditDetailSelected.getProductProgram().getId());
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        CreditType creditType = creditTypeDAO.findById(newCreditDetailSelected.getCreditType().getId());

        if (rowIndex < newCreditFacilityView.getNewCreditDetailViewList().size()) {
            newCreditDetailView = new NewCreditDetailView();
            newCreditDetailView.setProductProgram(productProgram);
            newCreditDetailView.setCreditType(creditType);
            newCreditDetailView.setRequestType(newCreditDetailSelected.getRequestType());
            newCreditDetailView.setRefinance(newCreditDetailSelected.getRefinance());
            newCreditDetailView.setProductCode(newCreditDetailSelected.getProductCode());
            newCreditDetailView.setProjectCode(newCreditDetailSelected.getProjectCode());
            newCreditDetailView.setLimit(newCreditDetailSelected.getLimit());
            newCreditDetailView.setPCEPercent(newCreditDetailSelected.getPCEPercent());
            newCreditDetailView.setPCEAmount(newCreditDetailSelected.getPCEAmount());
            newCreditDetailView.setReduceFrontEndFee(newCreditDetailSelected.isReduceFrontEndFee());
            newCreditDetailView.setReducePriceFlag(newCreditDetailSelected.isReducePriceFlag());
            newCreditDetailView.setStandardBasePrice(newCreditDetailSelected.getStandardBasePrice());
            newCreditDetailView.setStandardInterest(newCreditDetailSelected.getStandardInterest());
            newCreditDetailView.setSuggestBasePrice(newCreditDetailSelected.getSuggestBasePrice());
            newCreditDetailView.setSuggestInterest(newCreditDetailSelected.getSuggestInterest());
            newCreditDetailView.setFrontEndFee(newCreditDetailSelected.getFrontEndFee());
            newCreditDetailView.setLoanPurpose(newCreditDetailSelected.getLoanPurpose());
            newCreditDetailView.setRemark(newCreditDetailSelected.getRemark());
            newCreditDetailView.setDisbursement(newCreditDetailSelected.getDisbursement());
            newCreditDetailView.setHoldLimitAmount(newCreditDetailSelected.getHoldLimitAmount());
            newCreditDetailView.setSeq(newCreditDetailSelected.getSeq());
            newCreditDetailView.setNewCreditTierDetailViewList(newCreditDetailSelected.getNewCreditTierDetailViewList());

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
                DisbursementType disbursement = disbursementDAO.findById(newCreditDetailView.getDisbursement().getId());

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
                DisbursementType disbursement = disbursementDAO.findById(newCreditDetailView.getDisbursement().getId());

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
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setSeq(newCreditDetailView.getSeq());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setNewCreditTierDetailViewList(newCreditDetailView.getNewCreditTierDetailViewList());
            } else {
                log.info("onSaveCreditInfo ::: Undefined modeForButton !!");
            }

            complete = true;
            hashSeqCredit.put(seq, 0);
            seq++;
            log.info("seq++ of credit after add complete proposeCredit :: {}", seq);

            if (modeForDB == ModeForDB.ADD_DB) {
                proposeCreditDetailViewList = creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId);
                log.info("proposeCreditDetailViewList :: {}", proposeCreditDetailViewList.size());
            }

        } else {
            log.info("onSaveCreditInfo ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    public void onDeleteCreditInfo() {
        log.info("delete :: rowIndex :: {}", rowIndex);
//        int used;
//        for (int i = 0; i < hashSeqCredit.size(); i++) {
//            log.info("hashSeqCredit.get(i) in use   :  " + i + " is   " + hashSeqCredit.get(i).toString());
//        }
//        log.info("onDeleteCreditInfo ::: seq is : {} " + proposeCreditDetailSelected.getSeq());
//        log.info("onDeleteCreditInfo ::: use is : {} " + Integer.parseInt(hashSeqCredit.get(proposeCreditDetailSelected.getSeq()).toString()));
//
//        used = Integer.parseInt(hashSeqCredit.get(proposeCreditDetailSelected.getSeq()).toString());
//
//        log.info("before del use is  " + used);

//        if (used == 0) {
//            log.info("used ::: {} ", used);
        newCreditFacilityView.getNewCreditDetailViewList().remove(rowIndex);

//        } else {
//            log.info("used::: {}", used);
//            messageHeader = msg.get("app.propose.exception");
//            message = msg.get("app.propose.error.delete.credit");
//            messageErr = true;
//            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//        }

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
        log.info("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCollateralViewList().size());
        modeForButton = ModeForButton.ADD;
        newCollateralView = new NewCollateralView();
        Cloner cloner = new Cloner();
        proposeCreditDetailListTemp = cloner.deepClone(proposeCreditDetailViewList);
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailListTemp);
        newCollateralView.getNewCollateralHeadViewList().add(new NewCollateralHeadView());

    }

    public void onEditProposeCollInfo() {
        log.info("onEditProposeCollInfo :: {}", selectCollateralDetailView.getId());
        log.info("onEditProposeCollInfo ::rowIndexCollateral  {}", rowIndexCollateral);
        modeForButton = ModeForButton.EDIT;
        newCollateralView = new NewCollateralView();
        newCollateralView.setJobID(selectCollateralDetailView.getJobID());
        newCollateralView.setAppraisalDate(selectCollateralDetailView.getAppraisalDate());
        newCollateralView.setAadDecision(selectCollateralDetailView.getAadDecision());
        newCollateralView.setAadDecisionReason(selectCollateralDetailView.getAadDecisionReason());
        newCollateralView.setAadDecisionReasonDetail(selectCollateralDetailView.getAadDecisionReasonDetail());
        newCollateralView.setUsage(selectCollateralDetailView.getUsage());
        newCollateralView.setTypeOfUsage(selectCollateralDetailView.getTypeOfUsage());
        newCollateralView.setUwDecision(selectCollateralDetailView.getUwDecision());
        newCollateralView.setUwRemark(selectCollateralDetailView.getUwRemark());
        newCollateralView.setMortgageCondition(selectCollateralDetailView.getMortgageCondition());
        newCollateralView.setMortgageConditionDetail(selectCollateralDetailView.getMortgageConditionDetail());
        newCollateralView.setBdmComments(selectCollateralDetailView.getBdmComments());
        newCollateralView.setNewCollateralHeadViewList(selectCollateralDetailView.getNewCollateralHeadViewList());

        int tempSeq = 0;
        Cloner cloner = new Cloner();
        proposeCreditDetailListTemp = cloner.deepClone(proposeCreditDetailViewList);
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailListTemp);
        if (selectCollateralDetailView.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectCollateralDetailView.getProposeCreditDetailViewList().size(); i++) {
                for (int j = tempSeq; j < proposeCreditDetailListTemp.size(); j++) {
                    log.info("creditType at " + j + " id is     " + proposeCreditDetailListTemp.get(j).getId());

                    if (selectCollateralDetailView.getProposeCreditDetailViewList().get(i).getSeq() == proposeCreditDetailListTemp.get(j).getSeq()) {
                        newCollateralView.getProposeCreditDetailViewList().get(j).setNoFlag(true);
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
            NewCollateralView proposeCollateralInfoAdd = new NewCollateralView();
            proposeCollateralInfoAdd.setJobID(newCollateralView.getJobID());
            proposeCollateralInfoAdd.setAppraisalDate(newCollateralView.getAppraisalDate());
            proposeCollateralInfoAdd.setAadDecision(newCollateralView.getAadDecision());
            proposeCollateralInfoAdd.setAadDecisionReason(newCollateralView.getAadDecisionReason());
            proposeCollateralInfoAdd.setAadDecisionReasonDetail(newCollateralView.getAadDecisionReasonDetail());
            proposeCollateralInfoAdd.setUsage(newCollateralView.getUsage());
            proposeCollateralInfoAdd.setTypeOfUsage(newCollateralView.getTypeOfUsage());
            proposeCollateralInfoAdd.setUwDecision(newCollateralView.getUwDecision());
            proposeCollateralInfoAdd.setUwRemark(newCollateralView.getUwRemark());
            proposeCollateralInfoAdd.setBdmComments(newCollateralView.getBdmComments());
            proposeCollateralInfoAdd.setMortgageCondition(newCollateralView.getMortgageCondition());
            proposeCollateralInfoAdd.setMortgageConditionDetail(newCollateralView.getMortgageConditionDetail());

            newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();

            if (newCollateralView.getNewCollateralHeadViewList().size() > 0) {
                for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                    PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollateralHeadView.getPotentialCollateral().getId());
                    CollateralType collTypePercentLTV = collateralTypeDAO.findById(newCollateralHeadView.getCollTypePercentLTV().getId());
                    CollateralType headCollType = collateralTypeDAO.findById(newCollateralHeadView.getHeadCollType().getId());

                    NewCollateralHeadView newCollateralHeadDetailAdd = new NewCollateralHeadView();
                    newCollateralHeadDetailAdd.setPotentialCollateral(potentialCollateral);
                    newCollateralHeadDetailAdd.setCollTypePercentLTV(collTypePercentLTV);
                    newCollateralHeadDetailAdd.setExistingCredit(newCollateralHeadView.getExistingCredit());
                    newCollateralHeadDetailAdd.setTitleDeed(newCollateralHeadView.getTitleDeed());
                    newCollateralHeadDetailAdd.setCollateralLocation(newCollateralHeadView.getCollateralLocation());
                    newCollateralHeadDetailAdd.setAppraisalValue(newCollateralHeadView.getAppraisalValue());
                    newCollateralHeadDetailAdd.setHeadCollType(headCollType);
                    newCollateralHeadDetailAdd.setInsuranceCompany(newCollateralHeadView.getInsuranceCompany());

                    if (newCollateralHeadView.getNewCollateralSubViewList().size() > 0) {
                        newCollateralHeadDetailAdd.setNewCollateralSubViewList(newCollateralHeadView.getNewCollateralSubViewList());
                    } else {
                        messageHeader = msg.get("app.propose.exception");
                        message = msg.get("app.propose.desc.add.data");
                        messageErr = true;
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    }

                    newCollateralHeadViewList.add(newCollateralHeadDetailAdd);
                }

                proposeCollateralInfoAdd.setNewCollateralHeadViewList(newCollateralHeadViewList);
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

            if (newCollateralView.getProposeCreditDetailViewList().size() > 0) {

                for (ProposeCreditDetailView proposeCreditDetailView : newCollateralView.getProposeCreditDetailViewList()) {
                    log.info("proposeCreditDetailView.isNoFlag()  :: {}", proposeCreditDetailView.isNoFlag());
                    if (proposeCreditDetailView.isNoFlag()) {
                        proposeCollateralInfoAdd.getProposeCreditDetailViewList().add(proposeCreditDetailView);
//                        seqTemp = proposeCreditDetailView.getSeq();
//                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                    }
                }

                newCreditFacilityView.getNewCollateralViewList().add(proposeCollateralInfoAdd);
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

            log.info("newCreditFacilityView.getNewCollateralViewList() {}", newCreditFacilityView.getNewCollateralViewList().size());

        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            log.info("modeForButton:: {} ", modeForButton);

            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setJobID(newCollateralView.getJobID());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setAppraisalDate(newCollateralView.getAppraisalDate());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setAadDecision(newCollateralView.getAadDecision());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setAadDecisionReason(newCollateralView.getAadDecisionReason());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setAadDecisionReasonDetail(newCollateralView.getAadDecisionReasonDetail());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setUsage(newCollateralView.getUsage());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setTypeOfUsage(newCollateralView.getTypeOfUsage());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setUwDecision(newCollateralView.getUwDecision());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setUwRemark(newCollateralView.getUwRemark());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setBdmComments(newCollateralView.getBdmComments());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setMortgageCondition(newCollateralView.getMortgageCondition());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setMortgageConditionDetail(newCollateralView.getMortgageConditionDetail());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setNewCollateralHeadViewList(newCollateralView.getNewCollateralHeadViewList());

            boolean checkPlus;
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(newCollateralView.getProposeCreditDetailViewList());
            newCollateralView.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());

            for (int i = 0; i < proposeCreditDetailListTemp.size(); i++) {
//                if (proposeCreditDetailListTemp.get(i).isNoFlag() == true) {
//                    newCollateralView.getProposeCreditDetailViewList().add(proposeCreditDetailListTemp.get(i));
//                    seqTemp = proposeCreditDetailListTemp.get(i).getSeq();
//                    checkPlus = true;
//
//                    for (int j = 0; j < selectCollateralDetailView.getProposeCreditDetailViewList().size(); j++) {
//                        if (selectCollateralDetailView.getProposeCreditDetailViewList().get(j).getSeq() == seqTemp) {
//                            checkPlus = false;
//                        }
//                    }
//
//                    if (checkPlus) {
//                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
//                    }
//
//                } else if (proposeCreditDetailListTemp.get(i).isNoFlag() == false) {
//                    if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
//                        hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
//                    }
//                }
            }
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(newCollateralView.getProposeCreditDetailViewList());


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
        for (int i = 0; i < selectCollateralDetailView.getProposeCreditDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }
        newCreditFacilityView.getNewCollateralViewList().remove(selectCollateralDetailView);
    }

    // for sub collateral dialog
    public void onAddCollateralOwnerUW() {
        log.info("newCollateralSubView.getCollateralOwnerUWList() {}", newCollateralSubView.getCollateralOwnerUWList().size());
//        if (newCollateralSubView.getCollateralOwnerUWList().size() > 0) {
//            for (CustomerInfoView collateralUW : newCollateralSubView.getCollateralOwnerUWList()) {
//                if (collateralUW.getId() == newCollateralSubView.getCollateralOwnerUW().getId()) {
//                    messageHeader = msg.get("app.propose.exception");
//                    message = msg.get("app.propose.desc.add.collateralUW");
//                    messageErr = true;
//                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//                } else {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", newCollateralSubView.getCollateralOwnerUW().getId());

        if (newCollateralSubView.getCollateralOwnerUW() != null) {
            if (newCollateralSubView.getCollateralOwnerUW().getId() == 0) {
                log.error("Can not add CollateralOwnerUw because id = 0!");
                return;
            }

            CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(newCollateralSubView.getCollateralOwnerUW());
            newCollateralSubView.getCollateralOwnerUWList().add(collateralOwnerAdd);
        }
//                }
//            }
//        }

    }

    public void onDeleteCollateralOwnerUW(int row) {
        log.info("row :: {} ", row);
        newCollateralSubView.getCollateralOwnerUWList().remove(row);
    }

    public void onAddMortgageType() {
//        for (MortgageType mortgage : newCollateralSubView.getMortgageList()) {
//            if (mortgage.getId() == newCollateralSubView.getMortgageType().getId()) {
//                messageHeader = msg.get("app.propose.exception");
//                message = msg.get("app.propose.desc.add.mortgage");
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            } else {
        log.debug("onAddMortgageType() mortgageType.id: {}", newCollateralSubView.getMortgageType().getId());
        if (newCollateralSubView.getMortgageType() != null) {
            if (newCollateralSubView.getMortgageType().getId() == 0) {
                log.error("Can not add MortgageType because id = 0!");
                return;
            }
            MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubView.getMortgageType().getId());
            log.info("onAddMortgageType :: {} ", newCollateralSubView.getMortgageType());
            newCollateralSubView.getMortgageList().add(mortgageType);
        }
//            }
//        }
    }

    public void onDeleteMortgageType(int row) {
        newCollateralSubView.getMortgageList().remove(row);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() relatedWithSelected.getId = {}", relatedWithSelected.getId());
//        if (relatedWithSelected.getId() == 0) {
//            log.error("Can not add RelatedWith because id = 0!");
//            return;
//        }
        NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(relatedWithSelected.getNo());
        newCollateralSubView.getRelatedWithList().add(relatedWith);

    }

    public void onDeleteRelatedWith(int row) {
        newCollateralSubView.getRelatedWithList().remove(row);
    }

    public NewCollateralSubView getIdNewSubCollateralDetail(long newSubCollateralId) {
        NewCollateralSubView newSubCollateralReturn = new NewCollateralSubView();
        if (newCreditFacilityView.getNewCollateralViewList().size() > 0) {
            for (NewCollateralView newCollateralView : Util.safetyList(newCreditFacilityView.getNewCollateralViewList())) {
                for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                    for (NewCollateralSubView newSubCollateralDetailOnAdded : newCollateralHeadView.getNewCollateralSubViewList()) {
                        log.info("newSubCollateralDetailView1 id ::: {}", newSubCollateralDetailOnAdded.getNo());
                        log.info("newSubCollateralDetailView1 title deed ::: {}", newSubCollateralDetailOnAdded.getTitleDeed());
                        if (newSubCollateralId == newSubCollateralDetailOnAdded.getNo()) {
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
        newCollateralSubView = new NewCollateralSubView();
        relatedWithSelected = new NewCollateralSubView();
        modeForSubColl = ModeForButton.ADD;
        log.info(" newCreditFacilityView.getNewCollateralViewList().size ::{}", newCreditFacilityView.getNewCollateralViewList().size());
        relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(newCreditFacilityView.getNewCollateralViewList());
        CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
        subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
        log.info("subCollateralTypeList ::: {}", subCollateralTypeList.size());
    }

    public void onEditSubCollateral() {
        log.info("rowSubIndex :: {}", rowSubIndex);
        modeForSubColl = ModeForButton.EDIT;
        newCollateralSubView = new NewCollateralSubView();
        newCollateralSubView.setSubCollateralType(subCollateralDetailItem.getSubCollateralType());
        newCollateralSubView.setTitleDeed(subCollateralDetailItem.getTitleDeed());
        newCollateralSubView.setAddress(subCollateralDetailItem.getAddress());
        newCollateralSubView.setLandOffice(subCollateralDetailItem.getLandOffice());
        newCollateralSubView.setCollateralOwnerAAD(subCollateralDetailItem.getCollateralOwnerAAD());
        newCollateralSubView.setAppraisalValue(subCollateralDetailItem.getAppraisalValue());
        newCollateralSubView.setMortgageValue(subCollateralDetailItem.getMortgageValue());
        newCollateralSubView.setCollateralOwnerUWList(subCollateralDetailItem.getCollateralOwnerUWList());
        newCollateralSubView.setMortgageList(subCollateralDetailItem.getMortgageList());
        newCollateralSubView.setRelatedWithList(subCollateralDetailItem.getRelatedWithList());
    }

    public void onSaveSubCollateral() {
        log.info("onSaveSubCollateral ::: mode : {}", modeForSubColl);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)) {
            log.info("modeForSubColl ::: {}", modeForSubColl);
            log.info("newCollateralSubView.getRelatedWithList() :: {}", newCollateralSubView.getRelatedWithList().size());
            NewCollateralSubView subCollAdd = new NewCollateralSubView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newCollateralSubView.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(newCollateralSubView.getTitleDeed());
            subCollAdd.setAddress(newCollateralSubView.getAddress());
            subCollAdd.setLandOffice(newCollateralSubView.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(newCollateralSubView.getCollateralOwnerAAD());
            subCollAdd.setAppraisalValue(newCollateralSubView.getAppraisalValue());
            subCollAdd.setMortgageValue(newCollateralSubView.getMortgageValue());
            subCollAdd.setCollateralOwnerUWList(newCollateralSubView.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(newCollateralSubView.getMortgageList());
            subCollAdd.setRelatedWithList(newCollateralSubView.getRelatedWithList());

            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().add(subCollAdd);

        } else if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.EDIT)) {
            log.info("modeForSubColl ::: {}", modeForSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newCollateralSubView.getSubCollateralType().getId());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setSubCollateralType(subCollateralType);
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setTitleDeed(newCollateralSubView.getTitleDeed());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setAddress(newCollateralSubView.getAddress());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setLandOffice(newCollateralSubView.getLandOffice());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setCollateralOwnerAAD(newCollateralSubView.getCollateralOwnerAAD());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setAppraisalValue(newCollateralSubView.getAppraisalValue());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setMortgageValue(newCollateralSubView.getMortgageValue());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setCollateralOwnerUWList(newCollateralSubView.getCollateralOwnerUWList());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setMortgageList(newCollateralSubView.getMortgageList());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setRelatedWithList(newCollateralSubView.getRelatedWithList());
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
        newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().remove(subCollateralDetailItem);
        log.info("rowCollHeadIndex :: ");
    }
    // END Add SUB Collateral

    //  Start Guarantor //
    public void onAddGuarantorInfo() {
        newGuarantorDetailView = new NewGuarantorDetailView();
        modeForButton = ModeForButton.ADD;
        newGuarantorDetailView.setProposeCreditDetailViewList(creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId));

    }

    public void onEditGuarantorInfo() {
        log.info("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;
        int tempSeq = 0;
        BigDecimal summary = BigDecimal.ZERO;
        newGuarantorDetailView = new NewGuarantorDetailView();
        if (newGuarantorDetailViewItem != null) {
            newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
            newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());
            List<ProposeCreditDetailView> proposeCreditDetailViewList;
            proposeCreditDetailViewList = creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId);
            newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

            if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().size() > 0) {
                for (int i = 0; i < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); i++) {
                    for (int j = tempSeq; j < proposeCreditDetailListTemp.size(); j++) {
                        log.info("creditType at " + j + " seq is     " + proposeCreditDetailListTemp.get(j).getId());
                        log.info("newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getGuaranteeAmount() :: {}", newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getGuaranteeAmount());
                        if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getId() == proposeCreditDetailListTemp.get(j).getId()) {
                            newGuarantorDetailView.getProposeCreditDetailViewList().get(j).setNoFlag(true);
                            newGuarantorDetailView.getProposeCreditDetailViewList().get(j).setGuaranteeAmount(newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getGuaranteeAmount());
                            summary = summary.add(newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getGuaranteeAmount());
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

                for (ProposeCreditDetailView proposeCreditDetailView : newGuarantorDetailView.getProposeCreditDetailViewList()) {
                    if (proposeCreditDetailView.isNoFlag()) {
                        guarantorDetailAdd.getProposeCreditDetailViewList().add(proposeCreditDetailView);
                        summary = summary.add(proposeCreditDetailView.getGuaranteeAmount());
                        seqTemp = proposeCreditDetailView.getSeq();
                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        log.info(" ::: seqTemp ::: {}", seqTemp);
                    }
                }

                guarantorDetailAdd.setTotalLimitGuaranteeAmount(summary);

                if (guarantorDetailAdd.getProposeCreditDetailViewList().size() > 0) {
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
                newGuarantorDetailView.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());

                for (int i = 0; i < proposeCreditDetailListTemp.size(); i++) {
                    if (proposeCreditDetailListTemp.get(i).isNoFlag() == true) {
                        newGuarantorDetailView.getProposeCreditDetailViewList().add(proposeCreditDetailListTemp.get(i));
                        summary = summary.add(proposeCreditDetailListTemp.get(i).getGuaranteeAmount());
                        seqTemp = proposeCreditDetailListTemp.get(i).getSeq();
                        checkPlus = true;

                        for (int j = 0; j < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); j++) {
                            if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(j).getSeq() == seqTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        }

                    } else if (proposeCreditDetailListTemp.get(i).isNoFlag() == false) {
                        if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                            hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
                        }
                    }
                }

                if (newGuarantorDetailView.getProposeCreditDetailViewList().size() > 0) {
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setProposeCreditDetailViewList(newGuarantorDetailView.getProposeCreditDetailViewList());
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

        for (int i = 0; i < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); i++) {
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
//            if ((newCreditFacilityView.getInvestedCountry().getId() != 0)
//                && (newCreditFacilityView.getLoanRequestType().getId() != 0)
//                && (newCreditFacilityView.getNewCreditDetailViewList().size() > 0)
//                && (newCreditFacilityView.getNewCollateralViewList().size() > 0)
//                && (newCreditFacilityView.getNewConditionDetailViewList().size() > 0)
//                && (newCreditFacilityView.getNewGuarantorDetailViewList().size() > 0)) {

            creditFacProposeControl.onSaveNewCreditFacility(newCreditFacilityView, workCaseId);
            creditFacProposeControl.calculateTotalProposeAmount(workCaseId);
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.propose.response.save.success");
            exSummaryControl.calForCreditFacility(workCaseId);
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

//            }else{
//                messageHeader = msg.get("app.propose.response.cannot.save");
//                message = msg.get("app.propose.response.desc.cannot.save");
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//            }
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

    public List<DisbursementType> getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(List<DisbursementType> disbursementList) {
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

    public NewCollateralView getNewCollateralView() {
        return newCollateralView;
    }

    public void setNewCollateralView(NewCollateralView newCollateralView) {
        this.newCollateralView = newCollateralView;
    }

    public NewCollateralView getSelectCollateralDetailView() {
        return selectCollateralDetailView;
    }

    public void setSelectCollateralDetailView(NewCollateralView selectCollateralDetailView) {
        this.selectCollateralDetailView = selectCollateralDetailView;
    }

    public NewCollateralSubView getNewCollateralSubView() {
        return newCollateralSubView;
    }

    public void setNewCollateralSubView(NewCollateralSubView newCollateralSubView) {
        this.newCollateralSubView = newCollateralSubView;
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

    public NewCreditDetailView getNewCreditDetailSelected() {
        return newCreditDetailSelected;
    }

    public void setNewCreditDetailSelected(NewCreditDetailView newCreditDetailSelected) {
        this.newCreditDetailSelected = newCreditDetailSelected;
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

    public List<ProposeCreditDetailView> getNewCollateralCreditDetailList() {
        return newCollateralCreditDetailList;
    }

    public void setNewCollateralCreditDetailList(List<ProposeCreditDetailView> newCollateralCreditDetailList) {
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

    public NewCollateralHeadView getNewCollateralHeadView() {
        return newCollateralHeadView;
    }

    public void setNewCollateralHeadView(NewCollateralHeadView newCollateralHeadView) {
        this.newCollateralHeadView = newCollateralHeadView;
    }

    public int getRowSubIndex() {
        return rowSubIndex;
    }

    public void setRowSubIndex(int rowSubIndex) {
        this.rowSubIndex = rowSubIndex;
    }

    public NewCollateralSubView getSubCollateralDetailItem() {
        return subCollateralDetailItem;
    }

    public void setSubCollateralDetailItem(NewCollateralSubView subCollateralDetailItem) {
        this.subCollateralDetailItem = subCollateralDetailItem;
    }

    public NewCollateralHeadView getCollateralHeaderDetailItem() {
        return collateralHeaderDetailItem;
    }

    public void setCollateralHeaderDetailItem(NewCollateralHeadView collateralHeaderDetailItem) {
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

    public List<NewCollateralHeadView> getNewCollateralHeadViewList() {
        return newCollateralHeadViewList;
    }

    public void setNewCollateralHeadViewList(List<NewCollateralHeadView> newCollateralHeadViewList) {
        this.newCollateralHeadViewList = newCollateralHeadViewList;
    }

    public List<NewCollateralSubView> getNewCollateralSubViewList() {
        return newCollateralSubViewList;
    }

    public void setNewCollateralSubViewList(List<NewCollateralSubView> newCollateralSubViewList) {
        this.newCollateralSubViewList = newCollateralSubViewList;
    }

    public List<NewCollateralSubView> getRelatedWithAllList() {
        return relatedWithAllList;
    }

    public void setRelatedWithAllList(List<NewCollateralSubView> relatedWithAllList) {
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

    public NewCollateralSubView getRelatedWithSelected() {
        return relatedWithSelected;
    }

    public void setRelatedWithSelected(NewCollateralSubView relatedWithSelected) {
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

    public List<ProposeCreditDetailView> getProposeCreditDetailListTemp() {
        return proposeCreditDetailListTemp;
    }

    public void setProposeCreditDetailListTemp(List<ProposeCreditDetailView> proposeCreditDetailListTemp) {
        this.proposeCreditDetailListTemp = proposeCreditDetailListTemp;
    }

    public List<ProposeCreditDetailView> getProposeCreditDetailViewList() {
        return proposeCreditDetailViewList;
    }

    public void setProposeCreditDetailViewList(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        this.proposeCreditDetailViewList = proposeCreditDetailViewList;
    }

    public List<ProposeCreditDetailView> getNewGuarantorCreditDetailList() {
        return newGuarantorCreditDetailList;
    }

    public void setNewGuarantorCreditDetailList(List<ProposeCreditDetailView> newGuarantorCreditDetailList) {
        this.newGuarantorCreditDetailList = newGuarantorCreditDetailList;
    }

    public boolean isCanAddTier() {
        return canAddTier;
    }

    public void setCanAddTier(boolean canAddTier) {
        this.canAddTier = canAddTier;
    }

    public List<CollateralType> getHeadCollateralTypeList() {
        return headCollateralTypeList;
    }

    public void setHeadCollateralTypeList(List<CollateralType> headCollateralTypeList) {
        this.headCollateralTypeList = headCollateralTypeList;
    }
}

