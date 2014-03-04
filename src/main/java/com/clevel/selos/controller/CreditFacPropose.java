package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.business.CollateralBizTransform;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@ViewScoped
@ManagedBean(name = "creditFacPropose")
public class CreditFacPropose extends MandatoryFieldsControl {
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
    private Long stepId;

    enum ModeForButton {ADD, EDIT}

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}

    private ModeForButton modeForButton;
    private ModeForButton modeForSubColl;
    private ModeForDB modeForDB;

    int rowIndex;
    int rowIndexGuarantor;
    int rowSubIndex;
    int rowCollHeadIndex;
    int rowIndexCollateral;

    private String messageHeader;
    private String message;
    private boolean messageErr;

    //Master all in Propose
    private List<DisbursementTypeView> disbursementTypeViewList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<CreditRequestTypeView> creditRequestTypeViewList;
    private List<CountryView> countryViewList;
    private ProductGroup productGroup;
    private List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList;
    private List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList;
    private List<BaseRate> baseRateList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<SubCollateralTypeView> subCollateralTypeViewList;
    private List<CollateralType> collateralTypeList;
    private List<CollateralType> headCollateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;
    private List<MortgageType> mortgageTypeList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<CustomerInfoView> collateralOwnerUwAllList;

    private NewCreditFacilityView newCreditFacilityView;
    // case from select database must to transform to view before to use continue
    private BasicInfo basicInfo;
    private TCG tcg;
    private SpecialProgramView specialProgramBasicInfo;
    private int applyTCG;

    //for control Propose Credit
    private NewCreditDetailView newCreditDetailView;
    private NewCreditDetailView newCreditDetailSelected;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;
    private boolean cannotAddTier;
    private int seq;
    private HashMap hashSeqCredit;
    private boolean modeEditReducePricing;
    private boolean modeEditReduceFront;
    private BigDecimal reducePrice;
    private boolean reducePricePanelRendered;
    private boolean cannotEditStandard;
    private boolean notRetrivePricing;

    // for control Propose Collateral
    private NewCollateralView newCollateralView;
    private NewCollateralView selectCollateralDetailView;
    private NewCollateralHeadView newCollateralHeadView;
    private NewCollateralHeadView collateralHeaderDetailItem;
    private NewCollateralSubView newCollateralSubView;
    private NewCollateralSubView subCollateralDetailItem;
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    private List<NewCollateralSubView> newCollateralSubViewList;
    private NewCollateralSubView relatedWithSelected;
    private CustomerInfoView collateralOwnerUW;
    private List<NewCollateralView> newCollateralViewDelList;
    private boolean flagComs;
    private boolean flagButtonCollateral;
    private boolean editProposeColl;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;
    private List<CustomerInfoView> guarantorList;

    //List of creditType : Propose Credit and Existing Credit Together
    private List<ProposeCreditDetailView> proposeCreditDetailListTemp;
    private List<ProposeCreditDetailView> newCollateralCreditDetailList;
    private List<ProposeCreditDetailView> newGuarantorCreditDetailList;
    private List<ProposeCreditDetailView> proposeCreditDetailViewList;

    //for  control Condition Information Dialog
    private NewConditionDetailView newConditionDetailView;
    private NewConditionDetailView selectConditionItem;

    //for suggest
    private BaseRate standardBasePriceDlg;
    private BigDecimal standardInterestDlg;
    private BaseRate suggestBasePriceDlg;
    private BigDecimal suggestInterestDlg;

    //Query one time on init
    private List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewAll;
    private List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewByGroup;

    @Inject
    WorkCaseDAO workCaseDAO;
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
    DisbursementTypeDAO disbursementDAO;

    @Inject
    NewCollateralTransform collateralInfoTransform;
    @Inject
    private CollateralBizTransform collateralBizTransform;
    @Inject
    private ProductTransform productTransform;
    @Inject
    private LoanPurposeTransform loanPurposeTransform;
    @Inject
    private DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private SubCollateralTypeTransform subCollateralTypeTransform;

    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    ExSummaryControl exSummaryControl;
    @Inject
    ProductControl productControl;
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    private LoanPurposeControl loanPurposeControl;
    @Inject
    DisbursementTypeControl disbursementTypeControl;

    @Inject
    private COMSInterface comsInterface;

    public CreditFacPropose() {
    }

    public void preRender() {
        log.debug("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);

        if (!Util.isNull(session.getAttribute("workCaseId"))) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            log.info("preRender :: {} ", stepId);
        } else {
            log.debug("preRender ::: workCaseId is null.");
            FacesUtil.redirect("/site/inbox.jsf");
            return;
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        log.debug("onCreation.");

        if (workCaseId != null) {

            modeForDB = ModeForDB.ADD_DB;
            hashSeqCredit = new HashMap<String, String>();
            notRetrivePricing = true;
            try {
                newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
                log.debug("onCreation ::: newCreditFacilityView : {}", newCreditFacilityView);
                if (newCreditFacilityView != null) {
                    log.debug("newCreditFacilityView.id ::: {}", newCreditFacilityView.getId());

                    modeForDB = ModeForDB.EDIT_DB;
                    proposeCreditDetailViewList = creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId);
                    log.debug("[List for select in Collateral] :: proposeCreditDetailViewList :: {}", proposeCreditDetailViewList.size());

                    for (int i = 0; i < proposeCreditDetailViewList.size(); i++) {
                        if (proposeCreditDetailViewList.get(i).getTypeOfStep().equals("N")) {
                            hashSeqCredit.put(i, proposeCreditDetailViewList.get(i).getUseCount());
                        }
                    }
                    notRetrivePricing = false;
                }

            } catch (Exception ex) {
                log.error("Exception while loading [Credit Facility] page :: ", ex);
            }

            log.debug("onCreation :: modeForDB :: {}", modeForDB);

            basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            if (basicInfo == null) {
                specialProgramBasicInfo = null;
                productGroup = null;
                FacesUtil.redirect("/site/basicInfo.jsf");
            } else {
                log.info("basicInfo.id ::: {}", basicInfo.getId());
                if (basicInfo.getApplySpecialProgram() == RadioValue.YES.value()) {
                    specialProgramBasicInfo = productTransform.transformToView(basicInfo.getSpecialProgram());
                } else {
                    specialProgramBasicInfo = productTransform.transformToView(specialProgramDAO.findById(3));
                }
                log.debug("specialProgramBasicInfo : {}", specialProgramBasicInfo);
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
        }

        if (creditRequestTypeViewList == null) {
            creditRequestTypeViewList = new ArrayList<CreditRequestTypeView>();
        }

        if (countryViewList == null) {
            countryViewList = new ArrayList<CountryView>();
        }

        if (newCreditDetailView == null) {
            newCreditDetailView = new NewCreditDetailView();
            seq = 0;
        }

        // change to view model
        if (disbursementTypeViewList == null) {
            disbursementTypeViewList = new ArrayList<DisbursementTypeView>();
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

        if (prdGroupToPrdProgramViewList == null) {
            prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
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

        flagComs = false;
        flagButtonCollateral = true;
        modeEditReducePricing = false;
        modeEditReduceFront = false;
        editProposeColl = false;

        creditRequestTypeViewList = creditRequestTypeTransform.transformToView(creditRequestTypeDAO.findAll());
        countryViewList = countryTransform.transformToView(countryDAO.findAll());
        mortgageTypeList = mortgageTypeDAO.findAll();
        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();
        collateralTypeList = collateralTypeDAO.findAll();
        headCollateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        baseRateList = baseRateDAO.findAll();
        cannotAddTier = false;

        suggestBasePriceDlg = new BaseRate();
        suggestInterestDlg = BigDecimal.ZERO;
        standardBasePriceDlg = new BaseRate();
        standardInterestDlg = BigDecimal.ZERO;

        prdGroupToPrdProgramViewAll = productControl.getPrdGroupToPrdProgramProposeAll();
        prdGroupToPrdProgramViewByGroup = productControl.getPrdGroupToPrdProgramProposeByGroup(productGroup);

    }


    //TODO call coms for retrieve data of Collateral
    public void onCallRetrieveAppraisalReportInfo() {
        String jobId = newCollateralView.getJobID();
        log.info("onCallRetrieveAppraisalReportInfo begin key is  :: {}", jobId);
        boolean flag = true;
        User user = getCurrentUser();

        if (!Util.isNull(jobId)) {
            flag = checkJobIdExist(newCreditFacilityView.getNewCollateralViewList(), jobId);

            if (flag) {
                try {
                    AppraisalDataResult appraisalDataResult = comsInterface.getAppraisalData(user.getId(), jobId);

                    if (!Util.isNull(appraisalDataResult) && ActionResult.SUCCESS.equals(appraisalDataResult.getActionResult())) {
                        newCollateralView = collateralBizTransform.transformCollateral(appraisalDataResult);
                        newCollateralView.setComs(true);
                        flagComs = true;
                        flagButtonCollateral = false;
                    } else {
                        newCollateralView = new NewCollateralView();
                        messageHeader = msg.get("app.propose.exception");
                        message = appraisalDataResult.getReason();
                        messageErr = true;
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    }
                } catch (COMSInterfaceException e) {
                    log.info("COMSInterfaceException :: ");
                    messageHeader = msg.get("app.propose.exception");
                    message = e.getMessage();
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.credit.facility.propose.coms.err");
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
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

    public void onChangeJobId() {
        flagButtonCollateral = true;
    }

    // ***************************************************************************************************************//

    //TODO Call Brms to get data Propose Credit Info
    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee ::workCaseId :::  {}", workCaseId);
        if (!Util.isNull(workCaseId)) {
            try {
                StandardPricingResponse standardPricingResponse = creditFacProposeControl.getPriceFeeInterest(workCaseId);

                if (!Util.isNull(standardPricingResponse) && ActionResult.SUCCESS.equals(standardPricingResponse.getActionResult())) {
                    log.debug("standardPricingResponse ::: {}", standardPricingResponse.getPricingInterest().toString());
                    log.debug("standardPricingResponse ::: {}", standardPricingResponse.getPricingFeeList().toString());
                } else if (!Util.isNull(standardPricingResponse) && ActionResult.FAILED.equals(standardPricingResponse.getActionResult())) {
                    messageHeader = msg.get("app.propose.exception");
                    message = standardPricingResponse.getActionResult().toString();
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }

            } catch (Exception e) {
                log.error("Exception while get getPriceFeeInterest data!", e);
//                messageHeader = msg.get("app.propose.exception");
//                message = e.getMessage();
//                messageErr = true;
//                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else {
//            messageHeader = msg.get("app.propose.exception");
//            message = msg.get("app.credit.facility.propose.coms.err");
//            messageErr = true;
//            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

/*
        // test create data from retrieving
        BaseRate baseRate = baseRateDAO.findById(1);                            // for test only
        BigDecimal testValue = BigDecimal.valueOf(-1.75);                       // for test only
        String testLabel;                                                       // for test only

        // ************************************************* fix ****************************************************//*/

        if (testValue.compareTo(BigDecimal.ZERO) < 0) {
            testLabel = baseRate.getName() + " " + testValue;
        } else {
            testLabel = baseRate.getName() + " + " + testValue;
        }
        /*//****** tier test create ********//*/
        newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();

        NewCreditTierDetailView newCreditTierDetailView = new NewCreditTierDetailView();

        newCreditTierDetailView.setFinalBasePrice(baseRate);
        newCreditTierDetailView.setFinalInterest(testValue);
        newCreditTierDetailView.setFinalPriceLabel(testLabel);

        newCreditTierDetailView.setSuggestBasePrice(baseRate);
        newCreditTierDetailView.setSuggestInterest(testValue);
        newCreditTierDetailView.setSuggestPriceLabel(testLabel);

        newCreditTierDetailView.setStandardBasePrice(baseRate);
        newCreditTierDetailView.setStandardInterest(testValue);
        newCreditTierDetailView.setStandardPriceLabel(testLabel);

        newCreditTierDetailView.setCanEdit(false);

        newCreditTierDetailViewList.add(newCreditTierDetailView);

        if (newCreditFacilityView.getNewCreditDetailViewList() != null && newCreditFacilityView.getNewCreditDetailViewList().size() > 0) {
            List<Integer> intTmp = new ArrayList<Integer>();
            for (int i = 0; i < newCreditFacilityView.getNewCreditDetailViewList().size(); i++) {
                if (newCreditFacilityView.getNewCreditDetailViewList().get(i).getRequestType() == RequestTypes.NEW.value()) {
                    intTmp.add(i);
                }
            }
            for (Integer a : intTmp) {
                newCreditFacilityView.getNewCreditDetailViewList().get(a).setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            }
        }
        */

    }

    // **************************************** Start Propose Credit Information   ****************************************//
    public void onChangeProductProgram() {
        log.debug("onChangeProductProgram :::: productProgram : {}", newCreditDetailView.getProductProgramView());
        newCreditDetailView.setProductCode("");
        newCreditDetailView.setProjectCode("");

        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(newCreditDetailView.getProductProgramView());
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " + prdProgramToCreditTypeViewList.size());
        newCreditDetailView.setCreditTypeView(new CreditTypeView());
    }

    public void onChangeCreditType() {
        log.info("onChangeCreditType {}::::", newCreditDetailView.getId());
        log.debug("onChangeCreditType :::: creditType : {}", newCreditDetailView.getCreditTypeView().getId());
        if ((newCreditDetailView.getProductProgramView().getId() != 0) && (newCreditDetailView.getCreditTypeView().getId() != 0)) {

            ProductFormulaView productFormulaView = productControl.getProductFormulaView(newCreditDetailView.getCreditTypeView().getId(),
                    newCreditDetailView.getProductProgramView().getId(),
                    newCreditFacilityView.getCreditCustomerType(), specialProgramBasicInfo.getId(), applyTCG);
            if (productFormulaView != null) {
                log.debug("onChangeCreditType :::: productFormula : {}", productFormulaView.getId());
                newCreditDetailView.setProductCode(productFormulaView.getProductCode());
                newCreditDetailView.setProjectCode(productFormulaView.getProjectCode());
                log.info("productFormula.getReduceFrontEndFee() ::: {}", productFormulaView.getReduceFrontEndFee());
                log.info("productFormula.getReducePricing() ::: {}", productFormulaView.getReducePricing());

                modeEditReducePricing = flagForModeDisable(productFormulaView.getReducePricing());
                modeEditReduceFront = flagForModeDisable(productFormulaView.getReduceFrontEndFee());

                //reducePricePanelRendered = (modeEditReducePricing == true) ? true : false;
                reducePricePanelRendered = modeEditReducePricing;
                log.info("reducePricePanelRendered:: {}", reducePricePanelRendered);
            }
        }
    }

    // 2:Y(false)can to edit , 1:N(true) cannot to edit
    public static boolean flagForModeDisable(int value) {
        return (value == 1) ? true : false;
    }

    public void onChangeRequestType() {
        log.info("newCreditDetailView.getRequestType() :: {}", newCreditDetailView.getRequestType());
        prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        prdProgramToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (newCreditDetailView.getRequestType() == RequestTypes.CHANGE.value()) {   //change
            prdGroupToPrdProgramViewList = prdGroupToPrdProgramViewAll;
            cannotEditStandard = false;
            cannotAddTier = false;
        } else if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramViewList = prdGroupToPrdProgramViewByGroup;
            }
            cannotEditStandard = true;
            if (modeForButton == ModeForButton.ADD) {
                cannotAddTier = true;
            } else {
                if (newCreditDetailView.getNewCreditTierDetailViewList() == null || newCreditDetailView.getNewCreditTierDetailViewList().isEmpty()) {
                    cannotAddTier = true;
                } else {
                    cannotAddTier = false;
                }
            }
        }
    }

    public void onCalInstallment(NewCreditDetailView newCreditDetailView) {
        log.info("onCalInstallment :: ");
        creditFacProposeControl.calculateInstallment(newCreditDetailView);
    }

    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        RequestContext.getCurrentInstance().execute("creditInfoDlg.show()");
        newCreditDetailView = new NewCreditDetailView();
        modeEdit = false;
        modeForButton = ModeForButton.ADD;

        onChangeRequestType();

        if (baseRateList != null && !baseRateList.isEmpty()) {
            standardBasePriceDlg = getNewBaseRate(baseRateList.get(0));
            suggestBasePriceDlg = getNewBaseRate(baseRateList.get(0));
        }
        standardInterestDlg = BigDecimal.ZERO;
        suggestInterestDlg = BigDecimal.ZERO;
    }

    public void onEditCreditInfo() {
        log.info("rowIndex :: {}", rowIndex);
        log.info("newCreditFacilityView.creditInfoDetailViewList :: {}", newCreditFacilityView.getNewCreditDetailViewList());
        modeEdit = true;
        modeForButton = ModeForButton.EDIT;
        Cloner cloner = new Cloner();
        newCreditDetailView = cloner.deepClone(newCreditDetailSelected);

        onChangeRequestType();

        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(newCreditDetailView.getProductProgramView());
        creditFacProposeControl.calculateInstallment(newCreditDetailView);

        if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {
            if (newCreditDetailView.getNewCreditTierDetailViewList() != null && newCreditDetailView.getNewCreditTierDetailViewList().size() > 0) {
                BaseRate suggestBaseRate = newCreditDetailView.getNewCreditTierDetailViewList().get(0).getSuggestBasePrice();
                BigDecimal suggestInterest = newCreditDetailView.getNewCreditTierDetailViewList().get(0).getSuggestInterest();
                suggestInterestDlg = new BigDecimal(suggestInterest.doubleValue());
                suggestBasePriceDlg = getNewBaseRate(suggestBaseRate);

                BaseRate standardBaseRate = newCreditDetailView.getNewCreditTierDetailViewList().get(0).getStandardBasePrice();
                BigDecimal standardInterest = newCreditDetailView.getNewCreditTierDetailViewList().get(0).getStandardInterest();
                standardInterestDlg = new BigDecimal(standardInterest.doubleValue());
                standardBasePriceDlg = getNewBaseRate(standardBaseRate);
            }
        } else {
            if (baseRateList != null && !baseRateList.isEmpty()) {
                suggestBasePriceDlg = getNewBaseRate(baseRateList.get(0));
                standardBasePriceDlg = getNewBaseRate(baseRateList.get(0));
            } else {
                suggestBasePriceDlg = new BaseRate();
                standardBasePriceDlg = new BaseRate();
            }
            suggestInterestDlg = BigDecimal.ZERO;
            standardInterestDlg = BigDecimal.ZERO;
        }
    }

    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if ((newCreditDetailView.getProductProgramView().getId() != 0)
                && (newCreditDetailView.getCreditTypeView().getId() != 0)
                && (newCreditDetailView.getLoanPurposeView().getId() != 0)
                && (newCreditDetailView.getDisbursementTypeView().getId() != 0)) {

            ProductProgramView productProgramView = getProductProgramById(newCreditDetailView.getProductProgramView().getId());
            CreditTypeView creditTypeView = getCreditTypeById(newCreditDetailView.getCreditTypeView().getId());
            LoanPurposeView loanPurposeView = getLoanPurposeById(newCreditDetailView.getLoanPurposeView().getId());
            DisbursementTypeView disbursementTypeView = getDisbursementTypeById(newCreditDetailView.getDisbursementTypeView().getId());

            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.info("onAddRecord ::: mode : {}", modeForButton);
                NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
                creditDetailAdd.setProductProgramView(productProgramView);
                creditDetailAdd.setCreditTypeView(creditTypeView);
                creditDetailAdd.setRequestType(newCreditDetailView.getRequestType());
                creditDetailAdd.setRefinance(newCreditDetailView.getRefinance());
                creditDetailAdd.setProductCode(newCreditDetailView.getProductCode());
                creditDetailAdd.setProjectCode(newCreditDetailView.getProjectCode());
                creditDetailAdd.setLimit(newCreditDetailView.getLimit());
                creditDetailAdd.setPCEPercent(newCreditDetailView.getPCEPercent());
                creditDetailAdd.setPCEAmount(newCreditDetailView.getPCEAmount());
                creditDetailAdd.setReduceFrontEndFee(newCreditDetailView.isReduceFrontEndFee());
                creditDetailAdd.setReducePriceFlag(newCreditDetailView.isReducePriceFlag());
                creditDetailAdd.setFrontEndFee(newCreditDetailView.getFrontEndFee());
                creditDetailAdd.setLoanPurposeView(loanPurposeView);
                creditDetailAdd.setRemark(newCreditDetailView.getRemark());
                creditDetailAdd.setDisbursementTypeView(disbursementTypeView);
                creditDetailAdd.setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());
                creditDetailAdd.setNewCreditTierDetailViewList(newCreditDetailView.getNewCreditTierDetailViewList());
                creditDetailAdd.setSeq(seq);

                if (modeForDB == ModeForDB.EDIT_DB) {
                    creditDetailAdd.setModeSaved(true);
                }

                creditFacProposeControl.calculateInstallment(creditDetailAdd);
                log.info("creditDetailAdd :getInstallment: {}", creditDetailAdd.getInstallment());
                newCreditFacilityView.getNewCreditDetailViewList().add(creditDetailAdd);
                complete = true;
                log.info("seq of credit after add proposeCredit :: {}", seq);
            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onEditRecord ::: mode : {}", modeForButton);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProductProgramView(productProgramView);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setCreditTypeView(creditTypeView);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRequestType(newCreditDetailView.getRequestType());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRefinance(newCreditDetailView.getRefinance());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProductCode(newCreditDetailView.getProductCode());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setProjectCode(newCreditDetailView.getProjectCode());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setLimit(newCreditDetailView.getLimit());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setPCEPercent(newCreditDetailView.getPCEPercent());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setPCEAmount(newCreditDetailView.getPCEAmount());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReducePriceFlag(newCreditDetailView.isReducePriceFlag());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setReduceFrontEndFee(newCreditDetailView.isReduceFrontEndFee());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setFrontEndFee(newCreditDetailView.getFrontEndFee());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setLoanPurposeView(loanPurposeView);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setRemark(newCreditDetailView.getRemark());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setDisbursementTypeView(disbursementTypeView);
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setHoldLimitAmount(newCreditDetailView.getHoldLimitAmount());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setSeq(newCreditDetailView.getSeq());
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setNewCreditTierDetailViewList(newCreditDetailView.getNewCreditTierDetailViewList());

                creditFacProposeControl.calculateInstallment(newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex));
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

        //todo : remove & check on ui repeat only
        if (newCreditFacilityView.getNewCreditDetailViewList() != null && newCreditFacilityView.getNewCreditDetailViewList().size() > 0) {
            for (NewCreditDetailView nc : newCreditFacilityView.getNewCreditDetailViewList()) {
                log.debug("newCreditDetail : {} ", nc);
//                log.debug("before tier : {}", nc.getNewCreditTierDetailViewList());
//                log.debug("tier size : {}", nc.getNewCreditTierDetailViewList().size());
                if (nc.getNewCreditTierDetailViewList() != null && nc.getNewCreditTierDetailViewList().size() == 0) {
                    log.debug("set null");
                    nc.setNewCreditTierDetailViewList(null);
                }
                log.debug("after tier : {}", nc.getNewCreditTierDetailViewList());

                if (nc.getNewCreditTierDetailViewList() != null && nc.getNewCreditTierDetailViewList().size() > 0) {
                    for (NewCreditTierDetailView nct : nc.getNewCreditTierDetailViewList()) {
                        log.debug("--------------------------------------------------------------");
                        log.debug("[1] - tier stpl : {}", nct.getStandardPriceLabel());
                        log.debug("[2] - tier supl : {}", nct.getSuggestPriceLabel());
                        log.debug("[3] - tier fnpl : {}", nct.getFinalPriceLabel());
                        log.debug("[4] - tier fnpl : {}", nct.getFinalPriceLabel());
                        log.debug("[5] - tier inst : {}", nct.getInstallment());
                        log.debug("[6] - tier tenor : {}", nct.getTenor());
                        log.debug("####### tier : {}", nct);
                        log.debug("--------------------------------------------------------------");
                    }
                }

                log.debug("after newCreditDetail : {} ", nc);
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteCreditInfo() {
        log.info("delete :: rowIndex :: {}", rowIndex);
//        int used;
//        for (int i = 0; i < hashSeqCredit.size(); i++) {
//            log.info("hashSeqCredit.get(i) in use   :  " + i + " is   " + hashSeqCredit.get(i).toString());
//        }
//        log.info("onDeleteCreditInfo ::: seq is : {} " + newCreditDetailSelected.getSeq());
//        log.info("onDeleteCreditInfo ::: use is : {} " + Integer.parseInt(hashSeqCredit.get(newCreditDetailSelected.getSeq()).toString()));
//
//        used = Integer.parseInt(hashSeqCredit.get(newCreditDetailSelected.getSeq()).toString());
//
//        log.info("before del use is  " + used);
//
//        if (used == 0) {
//            log.info("used ::: {} ", used);

        newCreditFacilityView.getNewCreditViewDelList().add(newCreditDetailSelected);
        newCreditFacilityView.getNewCreditDetailViewList().remove(newCreditDetailSelected);
//        } else {
//            log.info("used::: {}", used);
//            messageHeader = msg.get("app.propose.exception");
//            message = msg.get("app.propose.error.delete.credit");
//            messageErr = true;
//            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//        }

    }

    private void onSetInUsedProposeCreditDetail() {
        int useCount;
        int seq;
        if (!Util.isNull(proposeCreditDetailViewList)) {
            for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
                seq = proposeCreditDetailView.getSeq();
                useCount = Integer.parseInt(hashSeqCredit.get(seq).toString());
                if (proposeCreditDetailView.getTypeOfStep().equals("N")) {
                    proposeCreditDetailView.setUseCount(useCount);
                }
            }
        }
    }

    private BaseRate getNewBaseRate(BaseRate baseRate) {
        if (baseRate == null) {
            return new BaseRate();
        }
        BaseRate newBaseRate = new BaseRate();
        newBaseRate.setId(baseRate.getId());
        newBaseRate.setActive(baseRate.getActive());
        newBaseRate.setName(baseRate.getName());
        newBaseRate.setValue(baseRate.getValue());
        return newBaseRate;
    }

    private BaseRate getBaseRateById(int id) {
        if (baseRateList == null || baseRateList.isEmpty() || id == 0) {
            return new BaseRate();
        }

        BaseRate returnBaseRate = new BaseRate();
        for (BaseRate baseRate : baseRateList) {
            if (baseRate.getId() == id) {
                returnBaseRate.setId(baseRate.getId());
                returnBaseRate.setActive(baseRate.getActive());
                returnBaseRate.setName(baseRate.getName());
                returnBaseRate.setValue(baseRate.getValue());
            }
        }
        return returnBaseRate;
    }

    private ProductProgramView getProductProgramById(int id) {
        if (prdGroupToPrdProgramViewList == null || prdGroupToPrdProgramViewList.isEmpty() || id == 0) {
            return new ProductProgramView();
        }

        ProductProgramView returnPrdProgramView = new ProductProgramView();
        for (PrdGroupToPrdProgramView groupToProgramView : prdGroupToPrdProgramViewList) {
            if (groupToProgramView.getProductProgramView() != null
                    && groupToProgramView.getProductProgramView().getId() == id) {

                returnPrdProgramView.setId(groupToProgramView.getProductProgramView().getId());
                returnPrdProgramView.setActive(groupToProgramView.getProductProgramView().getActive());
                returnPrdProgramView.setName(groupToProgramView.getProductProgramView().getName());
                returnPrdProgramView.setDescription(groupToProgramView.getProductProgramView().getDescription());
                returnPrdProgramView.setBrmsCode(groupToProgramView.getProductProgramView().getBrmsCode());
            }
        }
        return returnPrdProgramView;
    }

    private CreditTypeView getCreditTypeById(int id) {
        if (prdProgramToCreditTypeViewList == null || prdProgramToCreditTypeViewList.isEmpty() || id == 0) {
            return new CreditTypeView();
        }

        CreditTypeView returnCreditTypeView = new CreditTypeView();
        for (PrdProgramToCreditTypeView programToCreditTypeView : prdProgramToCreditTypeViewList) {
            if (programToCreditTypeView.getCreditTypeView() != null
                    && programToCreditTypeView.getCreditTypeView().getId() == id) {

                returnCreditTypeView.setId(programToCreditTypeView.getCreditTypeView().getId());
                returnCreditTypeView.setActive(programToCreditTypeView.getCreditTypeView().getActive());
                returnCreditTypeView.setName(programToCreditTypeView.getCreditTypeView().getName());
                returnCreditTypeView.setDescription(programToCreditTypeView.getCreditTypeView().getDescription());
                returnCreditTypeView.setComsIntType(programToCreditTypeView.getCreditTypeView().getComsIntType());
                returnCreditTypeView.setBrmsCode(programToCreditTypeView.getCreditTypeView().getBrmsCode());
            }
        }
        return returnCreditTypeView;
    }

    private LoanPurposeView getLoanPurposeById(int id) {
        if (loanPurposeViewList == null || loanPurposeViewList.isEmpty() || id == 0) {
            return new LoanPurposeView();
        }

        LoanPurposeView returnLoanPurpose = new LoanPurposeView();
        for (LoanPurposeView loanPurposeView : loanPurposeViewList) {
            if (loanPurposeView.getId() == id) {
                returnLoanPurpose.setId(loanPurposeView.getId());
                returnLoanPurpose.setActive(loanPurposeView.getActive());
                returnLoanPurpose.setDescription(loanPurposeView.getDescription());
                returnLoanPurpose.setBrmsCode(loanPurposeView.getBrmsCode());
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
                returnDisbursementType.setActive(disbursementTypeView.getActive());
                returnDisbursementType.setDisbursement(disbursementTypeView.getDisbursement());
            }
        }
        return returnDisbursementType;
    }

//   **************************************** END Propose Credit Information  **************************************** //

    //  **************************************** Start Tier Dialog  ****************************************//
    public void onAddTierInfo() {
        log.info("onAddTierInfo ::: rowIndex of proposeCredit to edit :: {}", rowIndex);
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
                    suggestPriceLabel = suggestBase.getName() + " " + suggestInterestDlg.doubleValue();
                } else {
                    suggestPriceLabel = suggestBase.getName() + " + " + suggestInterestDlg.doubleValue();
                }
            }
        }

        if (standardBasePriceDlg.getId() != 0) {
            standardBase = getBaseRateById(standardBasePriceDlg.getId());
            if (standardBase != null) {
                standardPrice = standardBase.getValue().add(standardInterestDlg);
                if (ValidationUtil.isValueCompareToZero(standardInterestDlg, ValidationUtil.CompareMode.LESS_THAN)) {
                    standardPriceLabel = standardBase.getName() + " " + standardInterestDlg.doubleValue();
                } else {
                    standardPriceLabel = standardBase.getName() + " + " + standardInterestDlg.doubleValue();
                }
            }
        }

        log.info("standardBase :: {}", standardBase);
        log.info("SuggestInterest :: {}", suggestInterestDlg);
        log.info("suggestBase :: {}", suggestBase);
        log.info("StandardInterest :: {}", standardInterestDlg);
        log.info("suggestPrice :: {}", suggestPrice);
        log.info("standardPrice :: {}", standardPrice);

        if (ValidationUtil.isFirstCompareToSecond(standardPrice, suggestPrice, ValidationUtil.CompareMode.GREATER_THAN)) {
            finalBaseRate = getNewBaseRate(standardBasePriceDlg);
            finalInterest = new BigDecimal(standardInterestDlg.doubleValue());
            finalPriceLabel = standardPriceLabel;
        } else if (ValidationUtil.isFirstCompareToSecond(standardPrice, suggestPrice, ValidationUtil.CompareMode.LESS_THAN)) {
            finalBaseRate = getNewBaseRate(suggestBasePriceDlg);
            finalInterest = new BigDecimal(suggestInterestDlg.doubleValue());
            finalPriceLabel = suggestPriceLabel;
        } else { // if equal
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

        if (newCreditDetailView.getRequestType() == 2) {
            log.debug("newCreditDetailView.getRequestType() ::: {}", newCreditDetailView.getRequestType());
            if (newCreditDetailView.getNewCreditTierDetailViewList() != null) {
                newCreditDetailView.getNewCreditTierDetailViewList().add(0, creditTierDetailAdd);
            } else {
                List<NewCreditTierDetailView> tierDetailViewList = new ArrayList<NewCreditTierDetailView>();
                tierDetailViewList.add(0, creditTierDetailAdd);
                newCreditDetailView.setNewCreditTierDetailViewList(tierDetailViewList);
            }
        } else if (newCreditDetailView.getRequestType() == 1) {
            if (newCreditDetailView.getNewCreditTierDetailViewList() != null) {
                newCreditDetailView.getNewCreditTierDetailViewList().add(creditTierDetailAdd);
            } else {
                List<NewCreditTierDetailView> tierDetailViewList = new ArrayList<NewCreditTierDetailView>();
                tierDetailViewList.add(creditTierDetailAdd);
                newCreditDetailView.setNewCreditTierDetailViewList(tierDetailViewList);
            }
        }
    }

    public void onDeleteProposeTierInfo(int row) {
        log.info("onDeleteProposeTierInfo::");
        newCreditDetailView.getNewCreditTierDetailViewList().remove(row);
    }

//  **************************************** END Tier Dialog  ****************************************//

    // **************************************** Start Propose Collateral Information  *********************************//
    public void onChangeCollTypeLTV(NewCollateralHeadView newCollateralHeadView) {
        if (!flagComs) {
            log.info("onChangeCollTypeLTV ::; {}", newCollateralHeadView.getCollTypePercentLTV().getId());
            CollateralType headType = collateralTypeDAO.findRefById(newCollateralHeadView.getCollTypePercentLTV().getId());
            newCollateralHeadView.setHeadCollType(headType);
        }
    }


    public void onAddProposeCollInfo() {
        log.debug("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCollateralViewList().size());
        modeForButton = ModeForButton.ADD;
        newCollateralView = new NewCollateralView();
        Cloner cloner = new Cloner();
        proposeCreditDetailListTemp = cloner.deepClone(proposeCreditDetailViewList);
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailListTemp);
        newCollateralView.getNewCollateralHeadViewList().add(new NewCollateralHeadView());
        flagButtonCollateral = true;
        flagComs = false;
        editProposeColl = false;
    }

    public void onEditProposeCollInfo() {
        log.debug("onEditProposeCollInfo :: {}", selectCollateralDetailView.getId());
        log.debug("onEditProposeCollInfo ::rowIndexCollateral  {}", rowIndexCollateral);
        modeForButton = ModeForButton.EDIT;
        editProposeColl = true;
        newCollateralView = new NewCollateralView();
        Cloner collateralClone = new Cloner();
        newCollateralView = collateralClone.deepClone(selectCollateralDetailView);
        int tempSeq = 0;
        Cloner cloner = new Cloner();
        proposeCreditDetailListTemp = cloner.deepClone(proposeCreditDetailViewList);
        flagComs = false;
        log.info("selectCollateralDetailView.isComs() ::: {}", selectCollateralDetailView.isComs());
        if (selectCollateralDetailView.isComs()) { //ถ้าเป็น data  ที่ได้จาก coms  set rendered false (ไม่แสดงปุ่ม edit)
            flagButtonCollateral = false;
            flagComs = true;
        } else {
            flagButtonCollateral = true;
            newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailListTemp);
        }

        if (selectCollateralDetailView.getProposeCreditDetailViewList().size() > 0) {
            for (int i = 0; i < selectCollateralDetailView.getProposeCreditDetailViewList().size(); i++) {
                for (int j = tempSeq; j < proposeCreditDetailListTemp.size(); j++) {
                    log.debug("creditType at " + j + " id is     " + proposeCreditDetailListTemp.get(j).getId());

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
        log.debug("onSaveProposeCollInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        boolean complete1 = false;
        boolean complete2 = false;
        boolean complete3 = false;
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
            proposeCollateralInfoAdd.setComs(newCollateralView.isComs());
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
                        message = msg.get("app.propose.desc.add.sub.data");
                        messageErr = true;
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        complete1 = false;
                    }

                    newCollateralHeadViewList.add(newCollateralHeadDetailAdd);
                    complete1 = true;
                    log.info("  complete1 >>>>  :  {}", complete1);
                }

                proposeCollateralInfoAdd.setNewCollateralHeadViewList(newCollateralHeadViewList);
                complete2 = true;
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.head.data");
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                complete2 = false;
            }

            log.info("  complete2 >>>>  :  {}", complete2);
            log.info("flagComs ::; {}",flagComs);

            if (flagComs==false) {
                if (newCollateralView.getProposeCreditDetailViewList().size() > 0) { //if this is data from COMS it 's not have List of ProposeCreditType

                    for (ProposeCreditDetailView proposeCreditDetailView : newCollateralView.getProposeCreditDetailViewList()) {
                        log.debug("proposeCreditDetailView.isNoFlag()  :: {}", proposeCreditDetailView.isNoFlag());
                        if (proposeCreditDetailView.isNoFlag()) {
                            proposeCollateralInfoAdd.getProposeCreditDetailViewList().add(proposeCreditDetailView);
                            seqTemp = proposeCreditDetailView.getSeq();
                            // hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        }
                    }
                } else {
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.desc.add.data");
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    complete3 = false;
                }


            }else{
                complete3 = true;
            }

            newCreditFacilityView.getNewCollateralViewList().add(proposeCollateralInfoAdd);
            complete3 = true;
            log.info("newCreditFacilityView.getNewCollateralViewList() {}", newCreditFacilityView.getNewCollateralViewList().size());
            log.info("  complete3 >>>>  :  {}", complete3);

            if (complete1 == true && complete2 == true && complete3 == true) {
                complete = true;
            }

        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            log.debug("modeForButton:: {} ", modeForButton);

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
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setComs(newCollateralView.isComs());
            //  headCollateral not update
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setNewCollateralHeadViewList(newCollateralView.getNewCollateralHeadViewList());

            boolean checkPlus;
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(newCollateralView.getProposeCreditDetailViewList());
            newCollateralView.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());

            if (flagComs) {
                newCollateralView.setComs(false);
                flagButtonCollateral = false;
            } else {
                newCollateralView.setComs(true);
                flagButtonCollateral = true;
            }

            for (int i = 0; i < proposeCreditDetailListTemp.size(); i++) {
                if (proposeCreditDetailListTemp.get(i).isNoFlag() == true) {
                    newCollateralView.getProposeCreditDetailViewList().add(proposeCreditDetailListTemp.get(i));
                    seqTemp = proposeCreditDetailListTemp.get(i).getSeq();
                    checkPlus = true;
                    for (ProposeCreditDetailView proposeCreditCollateral : selectCollateralDetailView.getProposeCreditDetailViewList()) {
                        if (proposeCreditCollateral.getSeq() == seqTemp) {
                            checkPlus = false;
                        }
                    }
//                    if (checkPlus) {
//                        hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
//                    }
                } else if (proposeCreditDetailListTemp.get(i).isNoFlag() == false) {
//                    if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
//                        hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
//                    }
                }
            }
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(newCollateralView.getProposeCreditDetailViewList());
            editProposeColl = false;
            complete = true;
        } else {
            log.debug("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        flagComs = false;
        log.debug("  complete >>>>  :  {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);

    }

    public void onDeleteProposeCollInfo() {
        log.debug("onDeleteProposeCollInfo :: ");
        for (int i = 0; i < selectCollateralDetailView.getProposeCreditDetailViewList().size(); i++) {
            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
            }
        }
        newCreditFacilityView.getNewCollateralViewDelList().add(selectCollateralDetailView);
        newCreditFacilityView.getNewCollateralViewList().remove(selectCollateralDetailView);

        log.info("onDeleteProposeCollInfo :: newCreditFacilityView.getNewCollateralViewDelList() :: {}",newCreditFacilityView.getNewCollateralViewDelList().size());
    }

    // ****************************************************Start Add SUB Collateral****************************************************//
    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral and rowCollHeadIndex :: {}", rowCollHeadIndex);
        newCollateralSubView = new NewCollateralSubView();
        relatedWithSelected = new NewCollateralSubView();
        modeForSubColl = ModeForButton.ADD;
        log.debug(" newCreditFacilityView.getNewCollateralViewList().size ::{}", newCreditFacilityView.getNewCollateralViewList().size());
        relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(newCreditFacilityView.getNewCollateralViewList());

        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeList ::: {}", subCollateralTypeList.size());
        } else {

        }

    }

    public void onEditSubCollateral() {
        log.debug("rowSubIndex :: {}", rowSubIndex);
        modeForSubColl = ModeForButton.EDIT;
//        Cloner cloner = new Cloner();
        newCollateralSubView = new NewCollateralSubView();
//        newCollateralSubView = cloner.deepClone(subCollateralDetailItem);
        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeViewList ::: {}", subCollateralTypeViewList.size());
            log.debug("subCollateralTypeList ::: {}", subCollateralTypeList.size());
        }

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
        log.debug("onSaveSubCollateral ::: mode : {}", modeForSubColl);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)) {
            log.debug("modeForSubColl ::: {}", modeForSubColl);
            log.debug("newCollateralSubView.getRelatedWithList() :: {}", newCollateralSubView.getRelatedWithList().size());
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
            complete = true;
        } else if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.EDIT)) {
            log.debug("modeForSubColl ::: {}", modeForSubColl);
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
            complete = true;
        } else {
            log.debug("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        log.debug("  complete >>>>  :  {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral :: ");
        newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubDeleteList().add(subCollateralDetailItem);
        newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().remove(subCollateralDetailItem);
        log.debug("rowCollHeadIndex :: ");
    }

    // for sub collateral Owner  <List>
    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", newCollateralSubView.getCollateralOwnerUW().getId());

        if (newCollateralSubView.getCollateralOwnerUW() != null) {
            if (newCollateralSubView.getCollateralOwnerUW().getId() == 0) {
                log.error("Can not add CollateralOwnerUw because id = 0!");
                return;
            }
//            if(findDuplicateId(newCollateralSubView.getCollateralOwnerUWList(),newCollateralSubView.getCollateralOwnerUW().getId())){
//                log.error("Can not add CollateralOwnerUw dup !!!!!");
//                return;
//            }
            CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(newCollateralSubView.getCollateralOwnerUW());
            newCollateralSubView.getCollateralOwnerUWList().add(collateralOwnerAdd);
        }

    }

    public void onDeleteCollateralOwnerUW(int row) {
        log.debug("row :: {} ", row);
        newCollateralSubView.getCollateralOwnerUWList().remove(row);
    }

    public boolean findDuplicateId(List<CustomerInfoView> collateralOwnerUWList, long id) {
        boolean flag = false;

        for (CustomerInfoView customerInfo : collateralOwnerUWList) {
            log.info("customerInfo id is  :: {}", customerInfo.getId());
            log.info("id seq is :: {}", id);
            if (id == customerInfo.getId()) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    // for sub MortgageType <List>
    public void onAddMortgageType() {
        log.debug("onAddMortgageType() mortgageType.id: {}", newCollateralSubView.getMortgageType().getId());

        if (newCollateralSubView.getMortgageType() != null) {
            if (newCollateralSubView.getMortgageType().getId() == 0) {
                log.info("Can not add MortgageType because id = 0!");
                return;
            }
            MortgageType mortgageType = mortgageTypeDAO.findById(newCollateralSubView.getMortgageType().getId());
            log.debug("onAddMortgageType :: {} ", newCollateralSubView.getMortgageType());
            newCollateralSubView.getMortgageList().add(mortgageType);
        }
    }


    public void onDeleteMortgageType(int row) {
        newCollateralSubView.getMortgageList().remove(row);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() relatedWithSelected.getId = {}", relatedWithSelected.getId());
        NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(relatedWithSelected.getId());
        if (relatedWithSelected.getNo() == 0) {
            log.info("Can not add relatedWith because id = 0!");
            return;
        }
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
                        log.debug("newSubCollateralDetailView1 id ::: {}", newSubCollateralDetailOnAdded.getNo());
                        log.debug("newSubCollateralDetailView1 title deed ::: {}", newSubCollateralDetailOnAdded.getTitleDeed());
                        if (newSubCollateralId == newSubCollateralDetailOnAdded.getId()) {
                            newSubCollateralReturn = newSubCollateralDetailOnAdded;
                        }
                    }
                }
            }
        }
        return newSubCollateralReturn;
    }

    // ****************************************************END Add SUB Collateral **************************************************** //

    //  *************************************************** END Propose Collateral Information  ****************************************************//

    // **************************************************** Start Guarantor ****************************************************//
    public void onAddGuarantorInfo() {
        newGuarantorDetailView = new NewGuarantorDetailView();
        modeForButton = ModeForButton.ADD;
        newGuarantorDetailView.setProposeCreditDetailViewList(creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId));
    }

    public void onEditGuarantorInfo() {
        log.info("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;
        int tempSeq = 0;
        newGuarantorDetailView = new NewGuarantorDetailView();
        newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
        newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());

        if (newGuarantorDetailViewItem != null) {
            Cloner cloner = new Cloner();
            proposeCreditDetailListTemp = cloner.deepClone(proposeCreditDetailViewList);
            newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailListTemp);

            if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().size() > 0) {
                for (int i = 0; i < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); i++) {
                    for (int j = tempSeq; j < proposeCreditDetailListTemp.size(); j++) {
                        log.debug("creditType at " + j + " id is     " + proposeCreditDetailListTemp.get(j).getId());

                        if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getSeq() == proposeCreditDetailListTemp.get(j).getSeq()) {
                            newGuarantorDetailView.getProposeCreditDetailViewList().get(j).setNoFlag(true);
                            newGuarantorDetailView.getProposeCreditDetailViewList().get(j).setGuaranteeAmount(newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getGuaranteeAmount());
                            tempSeq = j;
                        }
                        continue;
                    }
                }
            }
        }

    }

    public void onSaveGuarantorInfoDlg() {
        log.debug("onSaveGuarantorInfoDlg ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        BigDecimal summary = BigDecimal.ZERO;
        int seqTemp;

        if (newGuarantorDetailView.getGuarantorName() != null) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.debug("modeForButton ::: {}", modeForButton);
                CustomerInfoView customerInfoView = customerInfoControl.getCustomerById(newGuarantorDetailView.getGuarantorName());
                NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
                guarantorDetailAdd.setGuarantorName(customerInfoView);
                /*if(customerInfoView.get){

                }*/
                guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                guarantorDetailAdd.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());

                if (newGuarantorDetailView.getProposeCreditDetailViewList().size() > 0) {
                    for (ProposeCreditDetailView proposeCreditDetailView : newGuarantorDetailView.getProposeCreditDetailViewList()) {
                        if (proposeCreditDetailView.isNoFlag()) {
                            guarantorDetailAdd.getProposeCreditDetailViewList().add(proposeCreditDetailView);
                            summary = summary.add(proposeCreditDetailView.getGuaranteeAmount());
                            seqTemp = proposeCreditDetailView.getSeq();
//                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
//                            log.info(" ::: seqTemp ::: {}", seqTemp);
                        }
                    }
                }

                guarantorDetailAdd.setTotalLimitGuaranteeAmount(summary);

                if (guarantorDetailAdd.getProposeCreditDetailViewList().size() > 0) {
                    newCreditFacilityView.getNewGuarantorDetailViewList().add(guarantorDetailAdd);
                    complete = true;
                } else {
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.desc.add.data");
                    messageErr = true;
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.debug("modeForButton ::: {}", modeForButton);
                CustomerInfoView customerInfoEdit = customerInfoControl.getCustomerById(newGuarantorDetailView.getGuarantorName());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(customerInfoEdit);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
                boolean checkPlus;
                List<ProposeCreditDetailView> proposeCreditDetailViewList1 = new ArrayList<ProposeCreditDetailView>();

                if (newGuarantorDetailView.getProposeCreditDetailViewList().size() > 0) {
                    for (ProposeCreditDetailView proposeCreditDetailView : newGuarantorDetailView.getProposeCreditDetailViewList()) {
                        if (proposeCreditDetailView.isNoFlag() == true) {
                            log.debug("proposeCreditDetailView ;::: {}", proposeCreditDetailView.getGuaranteeAmount());
                            proposeCreditDetailViewList1.add(proposeCreditDetailView);
                            summary = Util.add(summary, proposeCreditDetailView.getGuaranteeAmount());
                        }
                    }
                }

                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setProposeCreditDetailViewList(proposeCreditDetailViewList1);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTotalLimitGuaranteeAmount(summary);

                for (int i = 0; i < proposeCreditDetailListTemp.size(); i++) {
                    if (proposeCreditDetailListTemp.get(i).isNoFlag()==true) {
                        newCollateralView.getProposeCreditDetailViewList().add(proposeCreditDetailListTemp.get(i));
                        seqTemp = proposeCreditDetailListTemp.get(i).getSeq();
                        checkPlus = true;

                        for (ProposeCreditDetailView proposeCreditCollateral : newGuarantorDetailViewItem.getProposeCreditDetailViewList()) {
                            if (proposeCreditCollateral.getSeq() == seqTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        }

                    } else if (proposeCreditDetailListTemp.get(i).isNoFlag() == false) {
//                        if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
//                            hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
//                        }
                    }
                }

                complete = true;

            } else {
                log.debug("onSaveGuarantorInfoDlg ::: Undefined modeForButton !!");
                complete = false;
            }
        }

        newCreditFacilityView.setTotalGuaranteeAmount(creditFacProposeControl.calTotalGuaranteeAmount(newCreditFacilityView.getNewGuarantorDetailViewList()));
        log.debug("  complete >>>>  :  {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteGuarantorInfo() {
        log.debug("onDeleteGuarantorInfo ::: {}", newGuarantorDetailViewItem.getTcgLgNo());

        for (int i = 0; i < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); i++) {
//            if (Integer.parseInt(hashSeqCredit.get(i).toString()) > 0) {
//                hashSeqCredit.put(i, Integer.parseInt(hashSeqCredit.get(i).toString()) - 1);
//            }
        }
        newCreditFacilityView.getNewGuarantorViewDelList().add(newGuarantorDetailViewItem);
        newCreditFacilityView.getNewGuarantorDetailViewList().remove(newGuarantorDetailViewItem);
        log.debug("delete success");
        newCreditFacilityView.setTotalGuaranteeAmount(creditFacProposeControl.calTotalGuaranteeAmount(newCreditFacilityView.getNewGuarantorDetailViewList()));
    }

//****************************************************END Guarantor ****************************************************//

    //****************************************************Start Condition Information ***************************************************//
    public void onAddConditionInfo() {
        log.debug("onAddConditionInfo ::: ");
        newConditionDetailView = new NewConditionDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg() {
        log.debug("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            NewConditionDetailView newConditionDetailViewAdd = new NewConditionDetailView();
            newConditionDetailViewAdd.setLoanType(newConditionDetailView.getLoanType());
            newConditionDetailViewAdd.setConditionDesc(newConditionDetailView.getConditionDesc());
            newCreditFacilityView.getNewConditionDetailViewList().add(newConditionDetailViewAdd);
            complete = true;

        } else {
            log.debug("onSaveConditionInfoDlg ::: validation failed.");
            complete = false;
        }

        log.debug("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
//        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }


    public void onDeleteConditionInfo() {
        log.debug("onDeleteConditionInfo :: ");
        newCreditFacilityView.getNewConditionViewDelList().add(selectConditionItem);
        newCreditFacilityView.getNewConditionDetailViewList().remove(selectConditionItem);
    }

//***************************************END Condition Information ****************************************************//

    //TODO Database Action
    public void onSaveCreditFacPropose() {
        log.debug("onSaveCreditFacPropose ::: ModeForDB  {}", modeForDB);
//        onSetInUsedProposeCreditDetail();
        try {
//            if ((newCreditFacilityView.getInvestedCountry().getId() != 0)
//                    && (newCreditFacilityView.getLoanRequestType().getId() != 0)
//                    && (newCreditFacilityView.getNewCreditDetailViewList().size() > 0)
//                    && (newCreditFacilityView.getNewCollateralViewList().size() > 0)
//                    && (newCreditFacilityView.getNewConditionDetailViewList().size() > 0)
//                    && (newCreditFacilityView.getNewGuarantorDetailViewList().size() > 0)) {
            //TEST FOR NEW FUNCTION SAVE CREDIT FACILITY
            creditFacProposeControl.saveCreditFacility(newCreditFacilityView, workCaseId);
            creditFacProposeControl.calculateTotalProposeAmount(workCaseId);
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("app.propose.response.save.success");
            exSummaryControl.calForCreditFacility(workCaseId);
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            notRetrivePricing = false;
//            } else {
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

    public NewCreditFacilityView getNewCreditFacilityView() {
        return newCreditFacilityView;
    }

    public void setNewCreditFacilityView(NewCreditFacilityView newCreditFacilityView) {
        this.newCreditFacilityView = newCreditFacilityView;
    }


    public NewCreditDetailView getNewCreditDetailView() {
        return newCreditDetailView;
    }

    public void setNewCreditDetailView(NewCreditDetailView newCreditDetailView) {
        this.newCreditDetailView = newCreditDetailView;
    }

    public List<DisbursementTypeView> getDisbursementTypeViewList() {
        return disbursementTypeViewList;
    }

    public void setDisbursementTypeViewList(List<DisbursementTypeView> disbursementTypeViewList) {
        this.disbursementTypeViewList = disbursementTypeViewList;
    }

    public List<LoanPurposeView> getLoanPurposeViewList() {
        return loanPurposeViewList;
    }

    public void setLoanPurposeViewList(List<LoanPurposeView> loanPurposeViewList) {
        this.loanPurposeViewList = loanPurposeViewList;
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

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
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

    public List<CollateralType> getHeadCollateralTypeList() {
        return headCollateralTypeList;
    }

    public void setHeadCollateralTypeList(List<CollateralType> headCollateralTypeList) {
        this.headCollateralTypeList = headCollateralTypeList;
    }

    public boolean isFlagButtonCollateral() {
        return flagButtonCollateral;
    }

    public void setFlagButtonCollateral(boolean flagButtonCollateral) {
        this.flagButtonCollateral = flagButtonCollateral;
    }

    public boolean isCannotAddTier() {
        return cannotAddTier;
    }

    public void setCannotAddTier(boolean cannotAddTier) {
        this.cannotAddTier = cannotAddTier;
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

    public boolean isFlagComs() {
        return flagComs;
    }

    public void setFlagComs(boolean flagComs) {
        this.flagComs = flagComs;
    }

    public boolean isEditProposeColl() {
        return editProposeColl;
    }

    public void setEditProposeColl(boolean editProposeColl) {
        this.editProposeColl = editProposeColl;
    }

    public boolean isNotRetrivePricing() {
        return notRetrivePricing;
    }

    public void setNotRetrivePricing(boolean notRetrivePricing) {
        this.notRetrivePricing = notRetrivePricing;
    }

    public List<SubCollateralTypeView> getSubCollateralTypeViewList() {
        return subCollateralTypeViewList;
    }

    public void setSubCollateralTypeViewList(List<SubCollateralTypeView> subCollateralTypeViewList) {
        this.subCollateralTypeViewList = subCollateralTypeViewList;
    }
}

