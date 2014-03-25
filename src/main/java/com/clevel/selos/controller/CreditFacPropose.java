package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMSInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.coms.model.AppraisalDataResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
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
    private String severity;

    //Master all in Propose
    private List<DisbursementTypeView> disbursementTypeViewList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<CreditRequestTypeView> creditRequestTypeViewList;
    private List<CountryView> countryViewList;
    private ProductGroup productGroup;
    private List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList;
    private List<PrdProgramToCreditTypeView> prdProgramToCreditTypeViewList;
    private List<BaseRate> baseRateList;
    private List<CollateralTypeView> collTypeViewList;
    private List<CollateralTypeView> headCollTypeViewList;
    private List<CollateralType> headCollateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;
    private List<MortgageType> mortgageTypeList;
    private List<MortgageTypeView> mortgageTypeViewList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<PotentialCollateral> potentialCollList;
    private List<PotentialCollateralView> potentialCollViewList;
    private List<CollateralType> collateralTypeList;
    private List<CollateralTypeView> collateralTypeViewList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<SubCollateralTypeView> subCollateralTypeViewList;

    private NewCreditFacilityView newCreditFacilityView;
    // case from select database must to transform to view before to use continue
    private BasicInfoView basicInfoView;
    private TCGView tcgView;
    private int applyTCG;
    private SpecialProgramView specialProgramView;

    //for control Propose Credit
    private NewCreditDetailView newCreditDetailView;
    private NewCreditDetailView newCreditDetailSelected;
    private List<NewCreditTierDetailView> newCreditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;
    private boolean cannotAddTier;
    private int seq;
    private HashMap<Integer, Integer> hashSeqCredit;
    private boolean modeEditReducePricing;
    private boolean modeEditReduceFront;
    private BigDecimal reducePrice;
    private boolean reducePricePanelRendered;
    private boolean cannotEditStandard;
    private boolean notRetrievePricing;
    private List<Long> deleteCreditIdList;

    // for control Propose Collateral
    private NewCollateralView newCollateralView;
    private NewCollateralView selectCollateralDetailView;
    private List<ProposeCreditDetailView> selectedCollateralCrdTypeItems;
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
    private boolean disableComs;
    private boolean flagButtonCollateral;
    private boolean editProposeColl;
    private List<Long> deleteCollIdList;
    private List<Long> deleteSubCollIdList;

    // for  control Guarantor Information Dialog
    private NewGuarantorDetailView newGuarantorDetailView;
    private NewGuarantorDetailView newGuarantorDetailViewItem;
    private List<CustomerInfoView> guarantorList;
    private List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems;
    private List<ProposeCreditDetailView> guarantorCreditTypeList;
    private List<Long> deleteGuarantorIdList;

    //List of creditType : Propose Credit and Existing Credit Together
    private List<ProposeCreditDetailView> proposeCreditDetailListTemp;
    private List<ProposeCreditDetailView> newCollateralCreditDetailList;
    private List<ProposeCreditDetailView> newGuarantorCreditDetailList;
    private List<ProposeCreditDetailView> proposeCreditDetailViewList;
    private List<ExistingCreditDetailView> existingCreditDetailViewList;

    //for  control Condition Information Dialog
    private NewConditionDetailView newConditionDetailView;
    private NewConditionDetailView selectConditionItem;
    private List<Long> deleteConditionIdList;

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
    private NewCollateralTransform collateralInfoTransform;
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
    private MortgageTypeTransform mortgageTypeTransform;
    @Inject
    private PotentialCollateralTransform potentialCollateralTransform;
    @Inject
    private CollateralTypeTransform collateralTypeTransform;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private TCGInfoControl tcgInfoControl;
    @Inject
    private ExSummaryControl exSummaryControl;
    @Inject
    private ProductControl productControl;
    @Inject
    private CreditFacProposeControl creditFacProposeControl;
    @Inject
    private LoanPurposeControl loanPurposeControl;
    @Inject
    private DisbursementTypeControl disbursementTypeControl;
    @Inject
    private SpecialProgramTransform specialProgramTransform;
    @Inject
    private COMSInterface comsInterface;
    @Inject
    private BRMSControl brmsControl;
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private ProposeCreditDetailTransform proposeCreditDetailTransform;
    @Inject
    private NewFeeDetailTransform newFeeDetailTransform;
    @Inject
    private NewCreditTierTransform newCreditTierTransform;
    @Inject
    private FeeTransform feeTransform;

    public CreditFacPropose() {}

    public void preRender() {
        log.debug("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);

        if (!Util.isNull(session.getAttribute("workCaseId"))) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.debug("workCaseId :: {} ", workCaseId);
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            log.debug("preRender :: {} ", stepId);
        } else {
            log.debug("preRender ::: workCaseId is null.");
            try {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e) {
                log.debug("Exception :: {}", e);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();
        log.debug("onCreation.");

        if (workCaseId != null) {
            modeForDB = ModeForDB.ADD_DB;
            // Initial sequence number credit
            hashSeqCredit = new HashMap<Integer, Integer>();
            notRetrievePricing = true;
            // delete list on save
            deleteCreditIdList  = new ArrayList<Long>();
            deleteCollIdList  = new ArrayList<Long>();
            deleteGuarantorIdList  = new ArrayList<Long>();
            deleteSubCollIdList = new ArrayList<Long>();
            deleteConditionIdList = new ArrayList<Long>();

            try {
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                log.info("workCase :: {}",workCase.getId());
                if(!Util.isNull(workCase)){
                    productGroup = workCase.getProductGroup();
                }

                newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
                log.debug("onCreation ::: newCreditFacilityView : {}", newCreditFacilityView);
                if (newCreditFacilityView != null) {
                    log.debug("newCreditFacilityView.id ::: {}", newCreditFacilityView.getId());

                    modeForDB = ModeForDB.EDIT_DB;
                    // find existingCreditType >>> Borrower Commercial in this workCase
                    existingCreditDetailViewList = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
                    proposeCreditDetailViewList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
                    log.debug("[List for select in Collateral] :: proposeCreditDetailViewList :: {}", proposeCreditDetailViewList.size());
                    int lastSeqNumber = creditFacProposeControl.getLastSeqNumberFromProposeCredit(proposeCreditDetailViewList);
                    if (lastSeqNumber > 1) {
                        seq = lastSeqNumber + 1;
                    } else {
                        seq = lastSeqNumber;
                    }
                    log.info("lastSeqNumber :: {}", lastSeqNumber);
                    for (int i = 0; i < proposeCreditDetailViewList.size(); i++) {
                        if (proposeCreditDetailViewList.get(i).getTypeOfStep().equals("N")) {
                            log.info("proposeCreditDetailViewList.get(i).getUseCount :: {}", proposeCreditDetailViewList.get(i).getUseCount());
                            hashSeqCredit.put(i, proposeCreditDetailViewList.get(i).getUseCount());
                        }
                    }
                    notRetrievePricing = false;
                } else { // for show on add new only !!
                    newCreditFacilityView = new NewCreditFacilityView();
//                    newCreditFacilityView.setWCNeed(BigDecimal.ZERO);
//                    newCreditFacilityView.setTotalWcDebit(BigDecimal.ZERO);
//                    newCreditFacilityView.setTotalWcTmb(BigDecimal.ZERO);
//                    newCreditFacilityView.setWCNeedDiffer(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase1WcLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase1WcMinLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase1Wc50CoreWc(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase1WcDebitCoreWc(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase2WcLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase2WcMinLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase2Wc50CoreWc(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase2WcDebitCoreWc(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase3WcLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase3WcMinLimit(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase3Wc50CoreWc(BigDecimal.ZERO);
//                    newCreditFacilityView.setCase3WcDebitCoreWc(BigDecimal.ZERO);
                }

            } catch (Exception ex) {
                log.error("Exception while loading [Credit Facility] page :: ", ex);
            }

            log.debug("onCreation :: modeForDB :: {}", modeForDB);

            basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
            if (basicInfoView != null) {
                if (basicInfoView.getSpProgram() == RadioValue.YES.value()) {
                    specialProgramView = specialProgramTransform.transformToView(basicInfoView.getSpecialProgram());
                } else {
                    specialProgramView = specialProgramTransform.transformToView(specialProgramDAO.findById(3));
                }
//                productGroup = basicInfoView.getProductGroup();
            }

            tcgView = tcgInfoControl.getTcgView(workCaseId);
            if (tcgView != null) {
                applyTCG = tcgView.getTCG();
            }

            guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
            log.debug("guarantorList size :: {}", guarantorList.size());

            collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
            log.debug("collateralOwnerUwAllList size :: {}", collateralOwnerUwAllList.size());
        }

        if (newCreditFacilityView == null) {
            newCreditFacilityView = new NewCreditFacilityView();
            reducePricePanelRendered = false;
            cannotEditStandard = true;
        }

        collateralOwnerUW = new CustomerInfoView();

        newCreditDetailView = new NewCreditDetailView();

        newConditionDetailView = new NewConditionDetailView();

        newGuarantorDetailView = new NewGuarantorDetailView();

        newCollateralView = new NewCollateralView();

        newCollateralSubView = new NewCollateralSubView();

        subCollateralTypeList = new ArrayList<SubCollateralType>();

        flagComs = false;
        flagButtonCollateral = true;
        modeEditReducePricing = false;
        modeEditReduceFront = false;
        editProposeColl = false;

        creditRequestTypeViewList = creditRequestTypeTransform.transformToView(creditRequestTypeDAO.findAll());
        countryViewList = countryTransform.transformToView(countryDAO.findAll());
        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();
        potentialCollViewList = potentialCollateralTransform.transformToView(potentialCollateralDAO.findAll());
        collTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());
        headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());
        mortgageTypeList = mortgageTypeDAO.findAll();
        mortgageTypeViewList = mortgageTypeTransform.transformToView(mortgageTypeList);
        baseRateList = baseRateDAO.findAll();
        if (baseRateList == null) {
            baseRateList = new ArrayList<BaseRate>();
        }

        suggestBasePriceDlg = new BaseRate();
        suggestInterestDlg = BigDecimal.ZERO;
        standardBasePriceDlg = new BaseRate();
        standardInterestDlg = BigDecimal.ZERO;

        prdGroupToPrdProgramViewAll = productControl.getPrdGroupToPrdProgramFromAllPrdProgram();
        prdGroupToPrdProgramViewByGroup = productControl.getPrdGroupToPrdProgramProposeByGroup(productGroup);
    }

    //TODO call coms for retrieve data of Collateral
    public void onCallRetrieveAppraisalReportInfo() {
        String jobId = newCollateralView.getJobID();
        log.debug("onCallRetrieveAppraisalReportInfo begin key is  :: {}", jobId);
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
                        messageHeader = msg.get("app.messageHeader.error");
                        message = appraisalDataResult.getReason();
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    }
                    proposeCreditDetailViewList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(),existingCreditDetailViewList, workCaseId);
                    newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

                } catch (COMSInterfaceException e) {
                    log.debug("COMSInterfaceException :: ");
                    messageHeader = msg.get("app.messageHeader.error");
                    message = e.getMessage();
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else {
                messageHeader = msg.get("app.messageHeader.error");
                message = msg.get("app.credit.facility.propose.coms.err");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        }

        log.debug("onCallRetrieveAppraisalReportInfo End");
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

    //TODO Call BRMS to get data Propose Credit Info
    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee ::workCaseId :::  {}", workCaseId);
        if (!Util.isNull(workCaseId)) {
            try {
                List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
                NewFeeDetailView newFeeDetailView;
                StandardPricingResponse standardPricingResponse = brmsControl.getPriceFeeInterest(workCaseId);
                if (ActionResult.SUCCESS.equals(standardPricingResponse.getActionResult())) {

                    for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()) {
                        FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);

                        log.debug("-- transformToView :: feeDetailView ::: {}", feeDetailView.toString());
                        // find productProgram
                        if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                            newFeeDetailView = new NewFeeDetailView();
                            ProductProgramView productProgramView = productTransform.transformToView(productProgramDAO.findById((int) feeDetailView.getCreditDetailViewId()));
                            newFeeDetailView.setProductProgram(productProgramView.getDescription());
                            if (feeDetailView.getFeeTypeView().getId() == 9) {//type=9,(Front-End-Fee)
                                newFeeDetailView.setStandardFrontEndFee(feeDetailView);
                            } else if (feeDetailView.getFeeTypeView().getId() == 15) { //type=15,(Prepayment Fee)
                                newFeeDetailView.setPrepaymentFee(feeDetailView);
                            } else if (feeDetailView.getFeeTypeView().getId() == 20) {//type=20,(CancellationFee)
                                newFeeDetailView.setCancellationFee(feeDetailView);
                            } else if (feeDetailView.getFeeTypeView().getId() == 21) { //type=21,(ExtensionFee)
                                newFeeDetailView.setExtensionFee(feeDetailView);
                            } else if (feeDetailView.getFeeTypeView().getId() == 22) {//type=22,(CommitmentFee)
                                newFeeDetailView.setCommitmentFee(feeDetailView);
                            }

                            log.debug("FeePaymentMethodView():::: {}", feeDetailView.getFeePaymentMethodView().getBrmsCode());
                            newFeeDetailViewList.add(newFeeDetailView);
                        }
                    }

                    if (newFeeDetailViewList != null) {
                        log.debug("newFeeDetailViewList not null ::: {}", newFeeDetailViewList.size());
                        newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);
                    }

//                if(standardPricingResponse.getPricingInterest()!=null){
//                    List<NewCreditTierDetailView> newCreditTier = newCreditTierTransform.transformPricingIntTierToView(standardPricingResponse.getPricingInterest());
//                }

                }
                else if (ActionResult.FAILED.equals(standardPricingResponse.getActionResult())) {
                    messageHeader = msg.get("app.messageHeader.error");
                    message = standardPricingResponse.getReason();
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            }
            catch (BRMSInterfaceException e) {
                log.debug("BRMSInterfaceException :: ");
                messageHeader = msg.get("app.messageHeader.error");
                message = e.getMessage();
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }
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
        log.debug("onChangeCreditType {}::::", newCreditDetailView.getId());
        log.debug("onChangeCreditType :::: creditType : {}", newCreditDetailView.getCreditTypeView().getId());
        if ((newCreditDetailView.getProductProgramView().getId() != 0) && (newCreditDetailView.getCreditTypeView().getId() != 0)) {

            ProductFormulaView productFormulaView = productControl.getProductFormulaView(newCreditDetailView.getCreditTypeView().getId(),
                    newCreditDetailView.getProductProgramView().getId(),
                    newCreditFacilityView.getCreditCustomerType(), specialProgramView.getId(), applyTCG);

            if (productFormulaView != null) {
                log.debug("onChangeCreditType :::: productFormula : {}", productFormulaView.getId());
                newCreditDetailView.setProductCode(productFormulaView.getProductCode());
                newCreditDetailView.setProjectCode(productFormulaView.getProjectCode());
                log.debug("productFormula.getReduceFrontEndFee() ::: {}", productFormulaView.getReduceFrontEndFee());
                log.debug("productFormula.getReducePricing() ::: {}", productFormulaView.getReducePricing());

                modeEditReducePricing = flagForModeDisable(productFormulaView.getReducePricing());
                modeEditReduceFront = flagForModeDisable(productFormulaView.getReduceFrontEndFee());

                reducePricePanelRendered = modeEditReducePricing;
                log.debug("reducePricePanelRendered:: {}", reducePricePanelRendered);
            }
        }
    }

    // 2:Y(false)can to edit , 1:N(true) cannot to edit
    public static boolean flagForModeDisable(int value) {
        return (value == 1) ? true : false;
    }

    public void onChangeRequestType() {
        log.debug("newCreditDetailView.getRequestType() :: {}", newCreditDetailView.getRequestType());
        newCreditDetailView.setProductProgramView(new ProductProgramView());
        newCreditDetailView.setCreditTypeView(new CreditTypeView());

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
        log.debug("onCalInstallment :: ");
        creditFacProposeControl.calculateInstallment(newCreditDetailView);
    }

    public void onAddCreditInfo() {
        log.debug("onAddCreditInfo ::: ");
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
        log.debug("rowIndex :: {}", rowIndex);
        log.debug("newCreditFacilityView.creditInfoDetailViewList :: {}", newCreditFacilityView.getNewCreditDetailViewList());
        modeEdit = true;
        modeForButton = ModeForButton.EDIT;
        onChangeRequestType();
        Cloner cloner = new Cloner();
        newCreditDetailView = cloner.deepClone(newCreditDetailSelected);



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
        log.debug("onSaveCreditInfo ::: mode : {}", modeForButton);
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
                log.debug("onAddRecord ::: mode : {}", modeForButton);
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

                creditFacProposeControl.calculateInstallment(creditDetailAdd);
                log.debug("creditDetailAdd :getInstallment: {}", creditDetailAdd.getInstallment());
                newCreditFacilityView.getNewCreditDetailViewList().add(creditDetailAdd);
                ProposeCreditDetailView newProposeCredit = proposeCreditDetailTransform.convertNewCreditToProposeCredit(creditDetailAdd, seq);
                if (Util.isNull(proposeCreditDetailViewList)) {
                    proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
                }
                proposeCreditDetailViewList.add(newProposeCredit);
                // Grouping ProposeCredit by TypeOfStep (N -> E) and Order the seqNumber for display on "Collateral and Guarantor" dialog
                creditFacProposeControl.groupTypeOfStepAndOrderBySeq(proposeCreditDetailViewList);
                complete = true;

                log.debug("seq of credit after add proposeCredit :: {}", seq);
            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.debug("onEditRecord ::: mode : {}", modeForButton);
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
                log.debug("onSaveCreditInfo ::: Undefined modeForButton !!");
            }

            complete = true;
            hashSeqCredit.put(seq, 0);
            seq++;
            log.debug("seq++ of credit after add complete proposeCredit :: {}", seq);

        } else {
            log.debug("onSaveCreditInfo ::: validation failed.");
            complete = false;
        }

        log.debug("  complete >>>>  :  {}", complete);

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
//        log.debug("delete :: rowIndex :: {}", rowIndex);
//        int used;
//        log.info("onDeleteCreditInfo ::: seq is : {} " + newCreditDetailSelected.getSeq());
//
//
//        used = hashSeqCredit.get(newCreditDetailSelected.getSeq());
//
//        log.info("before del use is  " + used);
//
//        if (used <= 0) {
//            log.info("used ::: {} ", used);
            if (newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).getId() != 0) {
                deleteCreditIdList.add(newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).getId());
            }

//            newCreditFacilityView.getNewCreditDetailViewList().remove(newCreditDetailSelected);
//        } else {
//            log.info("used::: {}", used);
//            messageHeader = msg.get("app.propose.exception");
//            message = msg.get("app.propose.error.delete.credit");
//            severity = MessageDialogSeverity.ALERT.severity();
//            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
//        }


    }

    private void onSetInUsedProposeCreditDetail() {
        int useCount;
        int seq;
        if (!Util.isNull(proposeCreditDetailViewList)) {
            for (ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList) {
                seq = proposeCreditDetailView.getSeq();
                log.info("seq :: {}",seq);
                useCount = hashSeqCredit.get(seq);
                if (proposeCreditDetailView.getTypeOfStep().equals("N")) {
                    proposeCreditDetailView.setUseCount(useCount);
                }

            }
        }
    }
    //   **************************************** END Propose Credit Information  **************************************** //

//   **************************************** END Propose Credit Information  **************************************** //

    //  **************************************** Start Tier Dialog  ****************************************//
    public void onAddTierInfo() {
        log.debug("onAddTierInfo ::: rowIndex of proposeCredit to edit :: {}", rowIndex);
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

        log.debug("standardBase :: {}", standardBase);
        log.debug("SuggestInterest :: {}", suggestInterestDlg);
        log.debug("suggestBase :: {}", suggestBase);
        log.debug("StandardInterest :: {}", standardInterestDlg);
        log.debug("suggestPrice :: {}", suggestPrice);
        log.debug("standardPrice :: {}", standardPrice);

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
        log.debug("onDeleteProposeTierInfo::");
        newCreditDetailView.getNewCreditTierDetailViewList().remove(row);
    }

//  **************************************** END Tier Dialog  ****************************************//

    // **************************************** Start Propose Collateral Information  *********************************//
    public void onChangeCollTypeLTV(NewCollateralHeadView newCollateralHeadView) {
        if (!flagComs) {
            log.debug("onChangeCollTypeLTV ::; {}", newCollateralHeadView.getCollTypePercentLTV().getId());
            CollateralType headType = collateralTypeDAO.findRefById(newCollateralHeadView.getCollTypePercentLTV().getId());
            newCollateralHeadView.setHeadCollType(headType);
        }
    }

    public void onAddProposeCollInfo() {
        log.debug("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCollateralViewList().size());
        modeForButton = ModeForButton.ADD;
        newCollateralView = new NewCollateralView();
        newCollateralView.getNewCollateralHeadViewList().add(new NewCollateralHeadView());
        selectedCollateralCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        proposeCreditDetailViewList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
        flagButtonCollateral = true;
        flagComs = false;
        editProposeColl = false;
    }

    public void onEditProposeCollInfo() {
        log.debug("onEditProposeCollInfo :: {},::rowIndexCollateral  {}", selectCollateralDetailView.getId(), rowIndexCollateral);

        modeForButton = ModeForButton.EDIT;
        editProposeColl = true;
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
        newCollateralView.setBdmComments(selectCollateralDetailView.getBdmComments());
        newCollateralView.setMortgageCondition(selectCollateralDetailView.getMortgageCondition());
        newCollateralView.setMortgageConditionDetail(selectCollateralDetailView.getMortgageConditionDetail());
        newCollateralView.setComs(selectCollateralDetailView.isComs());
        newCollateralView.setNewCollateralHeadViewList(new ArrayList<NewCollateralHeadView>());
        newCollateralView.setNewCollateralHeadViewList(selectCollateralDetailView.getNewCollateralHeadViewList());
        flagComs = false;
        selectedCollateralCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        proposeCreditDetailViewList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

        log.debug("selectCollateralDetailView.isComs() ::: {}", newCollateralView.isComs());
        if (newCollateralView.isComs()) { //ถ้าเป็น data  ที่ได้จาก coms  set rendered false (ไม่แสดงปุ่ม edit)
            flagButtonCollateral = false;
            flagComs = true;
        } else {
            flagButtonCollateral = true;
        }
        if (selectCollateralDetailView.getProposeCreditDetailViewList() != null && selectCollateralDetailView.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedCollateralCrdTypeItems = selectCollateralDetailView.getProposeCreditDetailViewList();
        }

    }

    public void onSaveProposeCollInfo() {
        log.debug("rowIndexCollateral :: {}, mode : {}", rowIndexCollateral, modeForButton);
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
                        newCollateralHeadViewList.add(newCollateralHeadDetailAdd);
                        complete1 = true;
                        proposeCollateralInfoAdd.setNewCollateralHeadViewList(newCollateralHeadViewList);
                        complete2 = true;
                    } else {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = msg.get("app.propose.desc.add.sub.data");
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        complete1 = false;
                    }
                }
            } else {
                messageHeader = msg.get("app.messageHeader.error");
                message = msg.get("app.propose.desc.add.head.data");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                complete2 = false;
            }

            log.debug("  complete2 >>>>  :  {}", complete2);
            log.debug("flagComs ::; {}", flagComs);

            if (selectedCollateralCrdTypeItems != null && selectedCollateralCrdTypeItems.size() > 0) {

                List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
                for (int i = 0; i < selectedCollateralCrdTypeItems.size(); i++) {
                    selectedCollateralCrdTypeItems.get(i).setNoFlag(true);
                    proposeCreditDetailViewList.add(selectedCollateralCrdTypeItems.get(i));
                    seqTemp = selectedCollateralCrdTypeItems.get(i).getSeq();

//                    hashSeqCredit.put(seqTemp, hashSeqCredit.get(i) + 1);


                }
                proposeCollateralInfoAdd.setProposeCreditDetailViewList(proposeCreditDetailViewList);
                complete3 = true;

                for (int j = 0; j < proposeCollateralInfoAdd.getProposeCreditDetailViewList().size(); j++) {
                    seqTemp = proposeCollateralInfoAdd.getProposeCreditDetailViewList().get(j).getSeq();
//                    if (proposeCollateralInfoAdd.getProposeCreditDetailViewList().get(j).isNoFlag()) {
//                        hashSeqCredit.put(seqTemp, hashSeqCredit.get(j) + 1);
//                    } else {
//                        hashSeqCredit.put(seqTemp, hashSeqCredit.get(j) - 1);
//                    }
                }

            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                complete3 = false;
            }

            log.debug("newCreditFacilityView.getNewCollateralViewList() {}", newCreditFacilityView.getNewCollateralViewList().size());
            log.info("  complete3 >>>>  :  {}", complete3);

            if (complete1 && complete2 && complete3) {
                newCreditFacilityView.getNewCollateralViewList().add(proposeCollateralInfoAdd);
                complete = true;
                flagComs = false;
            } else {
                complete = false;
            }

            log.info("  complete >>>>  :  {}", complete);
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);

        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            log.debug("modeForButton:: {} ", modeForButton);
            boolean checkPlus;

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
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());

//            if (newCollateralView.getNewCollateralHeadViewList().size() > 0) {
//                for (int i = 0; i < newCollateralView.getNewCollateralHeadViewList().size(); i++) {
//                    PotentialCollateral potentialCollateralEdit = potentialCollateralDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getPotentialCollateral().getId());
//                    CollateralType collTypePercentLTVEdit = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getCollTypePercentLTV().getId());
//                    CollateralType headCollTypeEdit = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getHeadCollType().getId());
//
//                    newCollateralView.getNewCollateralHeadViewList().get(i).setPotentialCollateral(potentialCollateralEdit);
//                    newCollateralView.getNewCollateralHeadViewList().get(i).setHeadCollType(headCollTypeEdit);
//                    newCollateralView.getNewCollateralHeadViewList().get(i).setCollTypePercentLTV(collTypePercentLTVEdit);
//                }
//            }
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setNewCollateralHeadViewList(newCollateralView.getNewCollateralHeadViewList());


            if (flagComs) {
                newCollateralView.setComs(false);
                flagButtonCollateral = false;
            } else {
                newCollateralView.setComs(true);
                flagButtonCollateral = true;
            }

            if (selectedCollateralCrdTypeItems != null && selectedCollateralCrdTypeItems.size() > 0) {

                List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
                for (int i = 0; i < selectedCollateralCrdTypeItems.size(); i++) {
                    selectedCollateralCrdTypeItems.get(i).setNoFlag(true);
                    proposeCreditDetailViewList.add(selectedCollateralCrdTypeItems.get(i));
                    log.info("selectedCollateralCrdTypeItems.get(i).isNoFlag() :: {}", selectedCollateralCrdTypeItems.get(i).isNoFlag());
                    /*if (selectedCollateralCrdTypeItems.get(i).isNoFlag() == true) {
                        seqTemp=selectedCollateralCrdTypeItems.get(i).getSeq();
                        checkPlus = true;

                        for (int j = 0; j < selectedCollateralCrdTypeItems.size(); j++) {
                            if (selectedCollateralCrdTypeItems.get(j).getSeq() == seqTemp) {
                                checkPlus = false;
                            }
                        }

                        if (checkPlus) {
                            hashSeqCredit.put(seqTemp,hashSeqCredit.get(seqTemp) + 1);
                        }

                    }else if(selectedCollateralCrdTypeItems.get(i).isNoFlag() == false) {
                        if (hashSeqCredit.get(i) > 0) {
                            hashSeqCredit.put(i, hashSeqCredit.get(i) - 1);
                        }

                    }*/

                }

                newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(proposeCreditDetailViewList);
                editProposeColl = false;
                complete = true;
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                complete = false;
            }

        } else {
            log.debug("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        log.debug("  complete >>>>  :  {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteProposeCollInfo() {
        log.debug("onDeleteProposeCollInfo :: id ::  {}", (newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).getId()));
        for (int i = 0; i < selectCollateralDetailView.getProposeCreditDetailViewList().size(); i++) {
            if (hashSeqCredit.get(i) > 0) {
                hashSeqCredit.put(i, hashSeqCredit.get(i) - 1);
            }
        }

        if (newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).getId() != 0) {
            deleteCollIdList.add(newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).getId());
        }

        newCreditFacilityView.getNewCollateralViewList().remove(selectCollateralDetailView);
        log.debug("onDeleteProposeCollInfo :: newCreditFacilityView.getNewCollateralViewList() :: {}", newCreditFacilityView.getNewCollateralViewList().size());
    }

    // ****************************************************Start Add SUB Collateral****************************************************//
    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral and rowCollHeadIndex :: {}", rowCollHeadIndex);
        newCollateralSubView = new NewCollateralSubView();
        relatedWithSelected = new NewCollateralSubView();
        modeForSubColl = ModeForButton.ADD;
        log.debug(" newCreditFacilityView.getNewCollateralViewList().size ::{}", newCreditFacilityView.getNewCollateralViewList().size());
        newCollateralSubView.setRelatedWithList(new ArrayList<NewCollateralSubView>());
        relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(newCreditFacilityView.getNewCollateralViewList());

        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeList ::: {}", subCollateralTypeList.size());
        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = "Please to choose Head Collateral Type";
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

    }

    public void onEditSubCollateral() {
        log.debug("rowSubIndex :: {}", rowSubIndex);
        modeForSubColl = ModeForButton.EDIT;
        newCollateralSubView = new NewCollateralSubView();

        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
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

        newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().remove(subCollateralDetailItem);
        log.debug("rowCollHeadIndex :: ");
    }

    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", newCollateralSubView.getCollateralOwnerUW().getId());
        if (newCollateralSubView.getCollateralOwnerUW() != null && newCollateralSubView.getCollateralOwnerUW().getId() != 0) {

            // Validate duplicate select
            if (newCollateralSubView.getCollateralOwnerUWList().size() > 0) {
                for (CustomerInfoView existCustomer : newCollateralSubView.getCollateralOwnerUWList()) {
                    if (existCustomer.getId() == newCollateralSubView.getCollateralOwnerUW().getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Customer owner!";
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            // Add to list
            newCollateralSubView.getCollateralOwnerUWList().add(
                    getCustomerInfoViewById(newCollateralSubView.getCollateralOwnerUW().getId()));
        }

    }

    public void onDeleteCollateralOwnerUW(int row) {
        log.debug("row :: {} ", row);
        newCollateralSubView.getCollateralOwnerUWList().remove(row);
    }

    public void onAddMortgageType() {
        log.debug("onAddMortgageType() mortgageType.id: {}", newCollateralSubView.getMortgageType().getId());
        if (newCollateralSubView.getMortgageType() != null && newCollateralSubView.getMortgageType().getId() != 0) {

            // Validate duplicate select
            if (newCollateralSubView.getMortgageList().size() > 0) {
                for (MortgageTypeView mortgageType : newCollateralSubView.getMortgageList()) {
                    if (mortgageType.getId() == newCollateralSubView.getMortgageType().getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Mortgage type!";
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            // Add to list
            newCollateralSubView.getMortgageList().add(getMortgageTypeById(newCollateralSubView.getMortgageType().getId()));

        }
    }

    public void onDeleteMortgageType(int row) {
        newCollateralSubView.getMortgageList().remove(row);
    }

    public void onAddRelatedWith() {
        log.info("onAddRelatedWith() relatedWithSelected.getId = {}", relatedWithSelected.getId());
        NewCollateralSubView relatedWith = getIdNewSubCollateralDetail(relatedWithSelected.getId());
        if (relatedWithSelected.getId() == 0) {
            log.debug("Can not add relatedWith because id = 0!");
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
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
//        guarantorCreditTypeList = creditFacProposeControl.findProposeCreditDetail(newCreditFacilityView.getNewCreditDetailViewList(), workCaseId);
        newGuarantorDetailView.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
        guarantorCreditTypeList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
        newGuarantorDetailView.setProposeCreditDetailViewList(guarantorCreditTypeList);
    }

    public void onEditGuarantorInfo() {
        log.debug("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;
        int tempSeq = 0;
        newGuarantorDetailView = new NewGuarantorDetailView();
        newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
        newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());
        newGuarantorDetailView.setGuarantorCategory(newGuarantorDetailViewItem.getGuarantorCategory());
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        proposeCreditDetailViewList = creditFacProposeControl.findAndGenerateSeqProposeCredits(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
        newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
//        }
        if (newGuarantorDetailViewItem.getProposeCreditDetailViewList() != null && newGuarantorDetailViewItem.getProposeCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedGuarantorCrdTypeItems = newGuarantorDetailViewItem.getProposeCreditDetailViewList();
            log.debug("newGuarantorDetailViewItem.getProposeCreditDetailViewList():: amount ::  {}", newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(0).getGuaranteeAmount());

            for (ProposeCreditDetailView proposeCreditDetailView : newGuarantorDetailView.getProposeCreditDetailViewList()) {
                for (ProposeCreditDetailView proposeeCreditDetailSelect : selectedGuarantorCrdTypeItems) {
                    if (proposeCreditDetailView.getSeq() == proposeeCreditDetailSelect.getSeq()) {
                        proposeCreditDetailView.setGuaranteeAmount(proposeeCreditDetailSelect.getGuaranteeAmount());
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
                if(customerInfoView.getCustomerEntity().getId()==GuarantorCategory.INDIVIDUAL.value()){
                    guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                }else if (customerInfoView.getCustomerEntity().getId()==GuarantorCategory.JURISTIC.value()){
                    guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.JURISTIC);
//                todo: What is TCG ?
//                }else if (customerInfoView.getCustomerEntity().getId()==GuarantorCategory.TCG.value()){
//                    guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.TCG);
                }else{
                    guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.NA);
                }

                guarantorDetailAdd.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
                if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                    List<ProposeCreditDetailView> newCreditTypeItems = new ArrayList<ProposeCreditDetailView>();
                    for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                        creditTypeItem.setNoFlag(true);
                        newCreditTypeItems.add(creditTypeItem);
                        log.debug("creditTypeItem.getGuaranteeAmount() :: {}", creditTypeItem.getGuaranteeAmount());
                        summary = Util.add(summary, creditTypeItem.getGuaranteeAmount());
                        log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                        log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                    }

                    guarantorDetailAdd.setProposeCreditDetailViewList(newCreditTypeItems);

                    for (int i = 0; i < guarantorDetailAdd.getProposeCreditDetailViewList().size(); i++) {
                        seqTemp = guarantorDetailAdd.getProposeCreditDetailViewList().get(i).getSeq();
                        if (guarantorDetailAdd.getProposeCreditDetailViewList().get(i).isNoFlag()) {
                            hashSeqCredit.put(seqTemp, hashSeqCredit.get(i) + 1);
                        } else {
                            hashSeqCredit.put(seqTemp, hashSeqCredit.get(i) - 1);
                        }
                    }

                    guarantorDetailAdd.setTotalLimitGuaranteeAmount(summary);
                    newCreditFacilityView.getNewGuarantorDetailViewList().add(guarantorDetailAdd);
                    complete = true;
                } else {
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.desc.add.data");
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.debug("modeForButton ::: {}", modeForButton);
                CustomerInfoView customerInfoEdit = customerInfoControl.getCustomerById(newGuarantorDetailView.getGuarantorName());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(customerInfoEdit);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(newGuarantorDetailView.getGuarantorCategory());
                if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                    for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                        creditTypeItem.setNoFlag(true);
                        newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().add(creditTypeItem);
                        log.debug(" newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().get(0).getGuaranteeAmount(); :: {}", newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().get(0).getGuaranteeAmount());
                        summary = Util.add(summary, creditTypeItem.getGuaranteeAmount());
                        log.debug("guarantor seq: {} = {} + 1", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                        log.debug("guarantor seq: {} = {}", creditTypeItem.getSeq(), hashSeqCredit.get(creditTypeItem.getSeq()));
                    }
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTotalLimitGuaranteeAmount(summary);
                    complete = true;

                    for (int i = 0; i < newGuarantorDetailView.getProposeCreditDetailViewList().size(); i++) {
                        seqTemp = newGuarantorDetailView.getProposeCreditDetailViewList().get(i).getSeq();
                        if (newGuarantorDetailView.getProposeCreditDetailViewList().get(i).isNoFlag()) {
                            hashSeqCredit.put(seqTemp, hashSeqCredit.get(i) + 1);
                        } else {
                            hashSeqCredit.put(seqTemp, hashSeqCredit.get(i) - 1);
                        }
                    }

                } else {
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.desc.add.data");
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
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

            int seqForDel = newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getSeq();
            if (hashSeqCredit.containsKey(newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getSeq()) &&
                    hashSeqCredit.get(newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(i).getSeq()) > 0) {
                hashSeqCredit.put(seqForDel, hashSeqCredit.get(i) - 1);
                log.info("before hashSeqCredit seq :  " + i + " use is   " + hashSeqCredit.get(i - 1));
            }
        }


        if (newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getId() != 0) {
            deleteGuarantorIdList.add(newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getId());
        }

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
    }

    public void onDeleteConditionInfo() {
        log.debug("onDeleteConditionInfo :: ");
        if (selectConditionItem.getId() != 0) {
            deleteConditionIdList.add(selectConditionItem.getId());
        }
        newCreditFacilityView.getNewConditionDetailViewList().remove(selectConditionItem);
    }
    //***************************************END Condition Information ****************************************************//

    public void onSaveCreditFacPropose() {
        log.debug("onSaveCreditFacPropose ::: ModeForDB  {}", modeForDB);
//        onSetInUsedProposeCreditDetail();
        try {
            //TEST FOR NEW FUNCTION SAVE CREDIT FACILITY
            creditFacProposeControl.deleteAllNewCreditFacilityByIdList(deleteCreditIdList, deleteCollIdList, deleteGuarantorIdList, deleteConditionIdList);
            // Calculate Total Propose
            creditFacProposeControl.calculateTotalProposeAmount(newCreditFacilityView, basicInfoView, tcgView, workCaseId);
            // Calculate Total for BRMS
            creditFacProposeControl.calculateTotalForBRMS(newCreditFacilityView);
            // Save NewCreditFacility, ProposeCredit, Collateral, Guarantor
            newCreditFacilityView = creditFacProposeControl.saveCreditFacility(newCreditFacilityView, workCaseId);
            onCreation();

            exSummaryControl.calForCreditFacility(workCaseId);

            notRetrievePricing = false;

            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.response.save.success");
            severity = MessageDialogSeverity.INFO.severity();
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            if (ex.getCause() != null) {
                message = msg.get("app.propose.response.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.propose.response.save.failed") + ex.getMessage();
            }
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
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

    private CustomerInfoView getCustomerInfoViewById(long id) {
        CustomerInfoView returnCusInfoView = new CustomerInfoView();
        if (collateralOwnerUwAllList != null && collateralOwnerUwAllList.size() > 0 && id != 0) {
            for (CustomerInfoView cusInfoView : collateralOwnerUwAllList) {
                if (cusInfoView.getId() == id) {
                    returnCusInfoView.setId(cusInfoView.getId());
                    returnCusInfoView.setFirstNameTh(cusInfoView.getFirstNameTh());
                    returnCusInfoView.setFirstNameEn(cusInfoView.getFirstNameEn());
                    returnCusInfoView.setLastNameTh(cusInfoView.getLastNameTh());
                    returnCusInfoView.setLastNameEn(cusInfoView.getLastNameEn());
                    returnCusInfoView.setTitleTh(cusInfoView.getTitleTh());
                    returnCusInfoView.setTitleEn(cusInfoView.getTitleEn());
                    returnCusInfoView.setCustomerEntity(cusInfoView.getCustomerEntity());
                    break;
                }
            }
        }
        return returnCusInfoView;
    }

    private MortgageTypeView getMortgageTypeById(int id) {
        MortgageTypeView returnMortgageType = new MortgageTypeView();
        if (mortgageTypeViewList != null && !mortgageTypeViewList.isEmpty() && id != 0) {
            for (MortgageTypeView mortgageType : mortgageTypeViewList) {
                if (mortgageType.getId() == id) {
                    returnMortgageType.setId(mortgageType.getId());
                    returnMortgageType.setActive(mortgageType.getActive());
                    returnMortgageType.setMortgage(mortgageType.getMortgage());
//                    returnMortgageType.setRedeem(mortgageType.isRedeem());
                    returnMortgageType.setMortgageFeeFlag(mortgageType.isMortgageFeeFlag());
                    returnMortgageType.setMortgageFlag(mortgageType.isMortgageFlag());
                    returnMortgageType.setPledgeFlag(mortgageType.isPledgeFlag());
                    returnMortgageType.setGuarantorFlag(mortgageType.isGuarantorFlag());
                    returnMortgageType.setTcgFlag(mortgageType.isTcgFlag());
                    returnMortgageType.setReferredFlag(mortgageType.isReferredFlag());
                    break;
                }
            }
        }
        return returnMortgageType;
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
                    returnCreditTypeView.setCanSplit(programToCreditTypeView.getCreditTypeView().getCanSplit());
                    returnCreditTypeView.setCalLimitType(programToCreditTypeView.getCreditTypeView().getCalLimitType());
                    returnCreditTypeView.setCreditGroup(programToCreditTypeView.getCreditTypeView().getCreditGroup());
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

    //*************************************** Getter/Setter ****************************************************//
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

    public List<CollateralTypeView> getCollTypeViewList() {
        return collTypeViewList;
    }

    public void setCollTypeViewList(List<CollateralTypeView> collTypeViewList) {
        this.collTypeViewList = collTypeViewList;
    }

    public List<CollateralTypeView> getHeadCollTypeViewList() {
        return headCollTypeViewList;
    }

    public void setHeadCollTypeViewList(List<CollateralTypeView> headCollTypeViewList) {
        this.headCollTypeViewList = headCollTypeViewList;
    }

    public List<PotentialCollateralView> getPotentialCollViewList() {
        return potentialCollViewList;
    }

    public void setPotentialCollViewList(List<PotentialCollateralView> potentialCollViewList) {
        this.potentialCollViewList = potentialCollViewList;
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

    public List<MortgageTypeView> getMortgageTypeViewList() {
        return mortgageTypeViewList;
    }

    public void setMortgageTypeViewList(List<MortgageTypeView> mortgageTypeViewList) {
        this.mortgageTypeViewList = mortgageTypeViewList;
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

    public boolean isNotRetrievePricing() {
        return notRetrievePricing;
    }

    public void setNotRetrievePricing(boolean notRetrievePricing) {
        this.notRetrievePricing = notRetrievePricing;
    }

    public List<SubCollateralTypeView> getSubCollateralTypeViewList() {
        return subCollateralTypeViewList;
    }

    public void setSubCollateralTypeViewList(List<SubCollateralTypeView> subCollateralTypeViewList) {
        this.subCollateralTypeViewList = subCollateralTypeViewList;
    }

    public List<ProposeCreditDetailView> getSelectedGuarantorCrdTypeItems() {
        return selectedGuarantorCrdTypeItems;
    }

    public void setSelectedGuarantorCrdTypeItems(List<ProposeCreditDetailView> selectedGuarantorCrdTypeItems) {
        this.selectedGuarantorCrdTypeItems = selectedGuarantorCrdTypeItems;
    }

    public List<ProposeCreditDetailView> getSelectedCollateralCrdTypeItems() {
        return selectedCollateralCrdTypeItems;
    }

    public void setSelectedCollateralCrdTypeItems(List<ProposeCreditDetailView> selectedCollateralCrdTypeItems) {
        this.selectedCollateralCrdTypeItems = selectedCollateralCrdTypeItems;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<ProposeCreditDetailView> getGuarantorCreditTypeList() {
        return guarantorCreditTypeList;
    }

    public void setGuarantorCreditTypeList(List<ProposeCreditDetailView> guarantorCreditTypeList) {
        this.guarantorCreditTypeList = guarantorCreditTypeList;
    }
}

