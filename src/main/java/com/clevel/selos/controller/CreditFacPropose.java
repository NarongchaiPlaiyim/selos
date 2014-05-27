package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.working.FeeDetailDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
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
import com.clevel.selos.model.db.working.NewCreditDetail;
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
import java.util.*;

@ViewScoped
@ManagedBean(name = "creditFacPropose")
public class CreditFacPropose extends BaseController {
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
    private List<MortgageType> mortgageTypeList;
    private List<MortgageTypeView> mortgageTypeViewList;
    private List<NewCollateralSubView> relatedWithAllList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<PotentialCollateralView> potentialCollViewList;
    private List<PotentialColToTCGCol> potentialColToTCGColList;
    private List<PotentialColToTCGCol> headCollTypeList;
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
    private List<NewCreditDetailView> newCreditDetailSeqList;

    private int rowSpanNumber;
    private boolean modeEdit;
    private boolean cannotAddTier;
    private int seq;
    private Hashtable hashSeqCredit;
    private Hashtable hashSeqCreditTmp;
    private boolean modeEditReducePricing;
    private boolean modeEditReduceFront;
    private BigDecimal reducePrice;
    private boolean reducePricePanelRendered;
    private boolean cannotEditStandard;
    private List<Long> deleteCreditIdList;

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
    private boolean flagComs;
    private boolean flagButtonCollateral;
    private boolean editProposeColl;
    private List<Long> deleteCollIdList;

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

    private boolean creditFlag;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private NewCreditDetailDAO newCreditDetailDAO;
    @Inject
    private FeeDetailDAO feeDetailDAO;
    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    private CollateralBizTransform collateralBizTransform;
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
    private SpecialProgramTransform specialProgramTransform;
    @Inject
    private ProposeCreditDetailTransform proposeCreditDetailTransform;
    @Inject
    private NewCreditTierTransform newCreditTierTransform;
    @Inject
    private FeeTransform feeTransform;
    @Inject
    private NewCreditDetailTransform newCreditDetailTransform;
    @Inject
    private BaseRateTransform baseRateTransform;

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
    private BRMSControl brmsControl;
    @Inject
    private CreditFacExistingControl creditFacExistingControl;

    @Inject
    private COMSInterface comsInterface;

    public CreditFacPropose(){}

    public void preRender() {
        log.debug("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);

        if (!Util.isNull(session.getAttribute("workCaseId"))) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.debug("workCaseId :: {} ", workCaseId);
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

        if (!Util.isNull(workCaseId)) {
            modeForDB = ModeForDB.ADD_DB;

            loadFieldControl(workCaseId, Screen.CREDIT_FACILITY_PROPOSE);

            hashSeqCredit = new Hashtable<Integer, Integer>();
            // delete list on save
            deleteCreditIdList = new ArrayList<Long>();
            deleteCollIdList = new ArrayList<Long>();
            deleteGuarantorIdList = new ArrayList<Long>();
            deleteConditionIdList = new ArrayList<Long>();

            try {
                WorkCase workCase = workCaseDAO.findById(workCaseId);
                log.info("workCase :: {}", workCase.getId());
                if (!Util.isNull(workCase)) {
                    productGroup = workCase.getProductGroup();
                }

                newCreditFacilityView = creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);

                if (Util.isNull(newCreditFacilityView)) {
                    newCreditFacilityView = new NewCreditFacilityView();
                    reducePricePanelRendered = false;
                    cannotEditStandard = true;
                    if(!isDisabled("retrieveProposeCreditButton")){
                        setDisabledValue("retrieveProposeCreditButton",true);
                    }
                } else {
                    if(newCreditFacilityView.getNewCreditDetailViewList()==null ||
                            (newCreditFacilityView.getNewCreditDetailViewList()!=null && newCreditFacilityView.getNewCreditDetailViewList().isEmpty())){
                        if(!isDisabled("retrieveProposeCreditButton")){
                            setDisabledValue("retrieveProposeCreditButton",true);
                        }
                    }

                    log.debug("newCreditFacilityView.id ::: {}", newCreditFacilityView.getId());

                    modeForDB = ModeForDB.EDIT_DB;
                    // find existingCreditType >>> Borrower Commercial in this workCase
                    existingCreditDetailViewList = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
                    proposeCreditDetailViewList = creditFacProposeControl.getProposeCreditFromCreditAndExisting(newCreditFacilityView.getNewCreditDetailViewList(), existingCreditDetailViewList, workCaseId);
                    int lastSeqNumber = creditFacProposeControl.getLastSeqNumberFromProposeCredit(proposeCreditDetailViewList);

                    if (lastSeqNumber > 1) {
                        seq = lastSeqNumber + 1;
                    } else {
                        seq = lastSeqNumber;
                    }

                    log.info("lastSeqNumber :: {}", lastSeqNumber);

                    newCreditDetailSeqList = newCreditFacilityView.getNewCreditDetailViewList();
                    if(newCreditDetailSeqList != null && newCreditDetailSeqList.size() > 0){
                        for(NewCreditDetailView ncdv : newCreditDetailSeqList){
                            hashSeqCredit.put(ncdv.getSeq(),ncdv.getUseCount());
                        }
                    }
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
            }

            tcgView = tcgInfoControl.getTcgView(workCaseId);
            if (tcgView != null) {
                applyTCG = tcgView.getTCG();
            }

            guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
//            CustomerInfoView customerInfoView = new CustomerInfoView();
//            customerInfoView.setId(-1);
//            customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
//            guarantorList.add(customerInfoView);
            collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
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
        headCollTypeList = potentialColToTCGColDAO.findAll();
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

        creditFlag = false;
    }

    public void onCallRetrieveAppraisalReportInfo() {
        String jobId = newCollateralView.getJobID();
        log.debug("onCallRetrieveAppraisalReportInfo begin key is  :: {}", jobId);
        boolean flag;
        HttpSession session = FacesUtil.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        if (!Util.isNull(jobId) && user != null) {
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

    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee ::workCaseId :::  {}", workCaseId);

        if(creditFlag){
            messageHeader = msg.get("app.messageHeader.info");
            message = "Please save Propose Line before Retrieve Pricing/Fee.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        if (!Util.isNull(workCaseId)) {
            try {
                List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
                StandardPricingResponse standardPricingResponse = brmsControl.getPriceFeeInterest(workCaseId);
                if (ActionResult.SUCCESS.equals(standardPricingResponse.getActionResult())) {
                    Map<Long, NewFeeDetailView> newFeeDetailViewMap = new HashMap<Long, NewFeeDetailView>();
                    NewFeeDetailView newFeeDetailView;
                    for (PricingFee pricingFee : standardPricingResponse.getPricingFeeList()) {
                        FeeDetailView feeDetailView = feeTransform.transformToView(pricingFee);
                        if (feeDetailView.getFeeLevel() == FeeLevel.CREDIT_LEVEL) {
                            if (newFeeDetailViewMap.containsKey(feeDetailView.getCreditDetailViewId())) {
                                newFeeDetailView = newFeeDetailViewMap.get(feeDetailView.getCreditDetailViewId());
                            } else {
                                newFeeDetailView = new NewFeeDetailView();
                                newFeeDetailViewMap.put(feeDetailView.getCreditDetailViewId(), newFeeDetailView);
                            }

                            // find productProgram
                            log.debug("feeDetailView.getFeeLevel() :::: {}", feeDetailView.getFeeLevel());
                            log.debug("feeDetailView.getCreditDetailViewId() :::: {}", feeDetailView.getCreditDetailViewId());
                            NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(feeDetailView.getCreditDetailViewId());
                            if (newCreditDetail != null) {
                                NewCreditDetailView newCreditView = newCreditDetailTransform.transformToView(newCreditDetail);
                                newFeeDetailView.setNewCreditDetailView(newCreditView);
                                log.debug("newCreditView.getProductProgramView().getId() :::: {}", newCreditView.getProductProgramView().getId());
                                ProductProgram productProgram = productProgramDAO.findById(newCreditView.getProductProgramView().getId());
                                if (productProgram != null) {
                                    log.debug("productProgram :: {}", productProgram.toString());
                                    newFeeDetailView.setProductProgram(productProgram.getName());
                                }
                                if ("9".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=9,(Front-End-Fee)
                                    newFeeDetailView.setStandardFrontEndFee(feeDetailView);
                                } else if ("15".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=15,(Prepayment Fee)
                                    newFeeDetailView.setPrepaymentFee(feeDetailView);
                                } else if ("20".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=20,(CancellationFee)
                                    newFeeDetailView.setCancellationFee(feeDetailView);
                                } else if ("21".equals(feeDetailView.getFeeTypeView().getBrmsCode())) { //type=21,(ExtensionFee)
                                    newFeeDetailView.setExtensionFee(feeDetailView);
                                } else if ("22".equals(feeDetailView.getFeeTypeView().getBrmsCode())) {//type=22,(CommitmentFee)
                                    newFeeDetailView.setCommitmentFee(feeDetailView);
                                }
                                log.debug("FeePaymentMethodView():::: {}", feeDetailView.getFeePaymentMethodView().getBrmsCode());
                            }
                        }
                    }

                    if (newFeeDetailViewMap != null && newFeeDetailViewMap.size() > 0) {
                        Iterator it = newFeeDetailViewMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            newFeeDetailViewList.add((NewFeeDetailView) pairs.getValue());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }

                    newCreditFacilityView.setNewFeeDetailViewList(newFeeDetailViewList);

                    log.debug("standardPricingResponse.getPricingInterest() : {}", standardPricingResponse.getPricingInterest());
                    if (standardPricingResponse.getPricingInterest() != null && standardPricingResponse.getPricingInterest().size() > 0) {
                        for (PricingInterest pricingInterest : standardPricingResponse.getPricingInterest()) {
                            log.debug("pricingInterest : {}", pricingInterest);
                            String creditTypeId = pricingInterest.getCreditDetailId();
                            String stringId;
                            log.debug("getPricingInterest :: creditTypeId :: {}", creditTypeId);
                            List<PricingIntTier> pricingIntTierList = pricingInterest.getPricingIntTierList();
                            //transform tier to view
                            List<NewCreditTierDetailView> newCreditTierViewList = newCreditTierTransform.transformPricingIntTierToView(pricingIntTierList);
                            //assign tier view to credit detail view mapping by creditTypeId
                            for (NewCreditDetailView newCreditView : newCreditFacilityView.getNewCreditDetailViewList()) {
                                stringId = String.valueOf(newCreditView.getId());
                                log.debug("newCreditView.getId() toString :: {}", newCreditView.getId());

                                if (stringId.equals(creditTypeId)) {
                                    if(newCreditView.getNewCreditTierDetailViewList() != null && newCreditView.getNewCreditTierDetailViewList().size() > 0){
                                        for(NewCreditTierDetailView nctdv : newCreditView.getNewCreditTierDetailViewList()){
                                            newCreditView.getDeleteTmpList().add(nctdv.getId());
                                        }
                                    }
                                    newCreditView.setNewCreditTierDetailViewList(newCreditTierViewList);
                                    break;
                                }
                            }
                        }
                        cannotAddTier = false;
                    }
                    messageHeader = msg.get("app.messageHeader.info");
                    message = "Retrieve Pricing/Fee Success";
                    severity = MessageDialogSeverity.INFO.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                } else if (ActionResult.FAILED.equals(standardPricingResponse.getActionResult())) {
                    log.debug("ActionResult.FAILED. standardPricingResponse.getReason() :: {}",standardPricingResponse.getReason());
                    messageHeader = msg.get("app.messageHeader.error");
                    message = standardPricingResponse.getReason();
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                }
            } catch (BRMSInterfaceException e) {
                log.debug("BRMSInterfaceException ::{}",e);
                messageHeader = msg.get("app.messageHeader.error");
                message = e.getMessage();
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }
    }
/*

    public void onRetrievePricingFeeTest() {
        if(newCreditFacilityView.getNewCreditDetailViewList() != null && newCreditFacilityView.getNewCreditDetailViewList().size() > 0){
            for(NewCreditDetailView newCreditView : newCreditFacilityView.getNewCreditDetailViewList()){
                if(newCreditView.getNewCreditTierDetailViewList() != null && newCreditView.getNewCreditTierDetailViewList().size() > 0){
                    for(NewCreditTierDetailView nctdv : newCreditView.getNewCreditTierDetailViewList()){
                        newCreditView.getDeleteTmpList().add(nctdv.getId());
                    }
                }

                List<NewCreditTierDetailView> newCreditTierViewList = new ArrayList<NewCreditTierDetailView>();
                NewCreditTierDetailView newCreditTierDetailView = new NewCreditTierDetailView();
                BaseRateView baseRateView = new BaseRateView();
                baseRateView.setName("name");
                baseRateView.setValue(BigDecimal.ONE);

                newCreditTierDetailView.setStandardBasePrice(baseRateView);
                newCreditTierDetailView.setSuggestBasePrice(baseRateView);
                newCreditTierDetailView.setFinalBasePrice(baseRateView);

                newCreditTierDetailView.setStandardPriceLabel("Standard Label");
                newCreditTierDetailView.setSuggestPriceLabel("Suggest Label");
                newCreditTierDetailView.setFinalPriceLabel("Final Label");

                newCreditTierDetailView.setStandardInterest(BigDecimal.ZERO);
                newCreditTierDetailView.setSuggestInterest(BigDecimal.ZERO);
                newCreditTierDetailView.setFinalInterest(BigDecimal.ZERO);

                newCreditTierViewList.add(newCreditTierDetailView);
                newCreditView.setNewCreditTierDetailViewList(newCreditTierViewList);
            }
        }
    }
*/

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
        newCreditDetailView.setProductProgramView(new ProductProgramView());
        newCreditDetailView.setCreditTypeView(new CreditTypeView());

        prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        prdProgramToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();

        if (newCreditDetailView.getRequestType() == RequestTypes.CHANGE.value()) {   //change
            prdGroupToPrdProgramViewList = prdGroupToPrdProgramViewAll;
            cannotAddTier = false;
            cannotEditStandard = false;
        } else if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramViewList = prdGroupToPrdProgramViewByGroup;
            }
            if (modeForButton == ModeForButton.ADD) { // can not do anything
                cannotAddTier = true;
                cannotEditStandard = true;
            } else {
                cannotAddTier = true;
                cannotEditStandard = true;
                if (newCreditDetailView.getNewCreditTierDetailViewList() != null
                        && newCreditDetailView.getNewCreditTierDetailViewList().size() > 0){
                    cannotAddTier = false;
                    cannotEditStandard = false;
                }
            }
        }
    }

    public void onCalInstallment(NewCreditDetailView newCreditDetailView) {
        log.debug("onCalInstallment :: ");
        creditFacProposeControl.calculateInstallment(newCreditDetailView,basicInfoView,tcgView,newCreditFacilityView);
        creditFacProposeControl.calculatePCEAmount(newCreditDetailView);
    }

    public void onAddCreditInfo() {
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

        RequestContext.getCurrentInstance().execute("creditInfoDlg.show()");
    }

    public void onEditCreditInfo() {
        log.debug("rowIndex ::: {} , newCreditFacilityView.creditInfoDetailViewList ::: {}", rowIndex, newCreditFacilityView.getNewCreditDetailViewList());
        modeEdit = true;
        modeForButton = ModeForButton.EDIT;
        onChangeRequestType();
        Cloner cloner = new Cloner();
        newCreditDetailView = cloner.deepClone(newCreditDetailSelected);

        prdProgramToCreditTypeViewList = productControl.getPrdProgramToCreditTypeViewList(newCreditDetailView.getProductProgramView());
        creditFacProposeControl.calculateInstallment(newCreditDetailView,basicInfoView,tcgView,newCreditFacilityView);

        if (newCreditDetailView.getRequestType() == RequestTypes.NEW.value()) {
            if (newCreditDetailView.getNewCreditTierDetailViewList() != null && newCreditDetailView.getNewCreditTierDetailViewList().size() > 0) {
                BaseRate suggestBaseRate = baseRateTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList().get(0).getSuggestBasePrice());
                BigDecimal suggestInterest = newCreditDetailView.getNewCreditTierDetailViewList().get(0).getSuggestInterest();
                suggestInterestDlg = new BigDecimal(suggestInterest.doubleValue());
                suggestBasePriceDlg = getNewBaseRate(suggestBaseRate);

                BaseRate standardBaseRate = baseRateTransform.transformToModel(newCreditDetailView.getNewCreditTierDetailViewList().get(0).getStandardBasePrice());
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

            if (newCreditDetailView.getNewCreditTierDetailViewList() != null && newCreditDetailView.getNewCreditTierDetailViewList().size() > 0) {
                for(NewCreditTierDetailView newCreditTierDetailView:newCreditDetailView.getNewCreditTierDetailViewList()){
                    newCreditTierDetailView.setCanEdit(true);
                }
            }
        }
    }

    public void onSaveCreditInfo() {
        log.debug("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete;

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
                creditDetailAdd.setDeleteTmpList(newCreditDetailView.getDeleteTmpList());
                creditDetailAdd.setSeq(seq);

                creditFacProposeControl.calculateInstallment(creditDetailAdd,basicInfoView,tcgView,newCreditFacilityView);
                log.debug("creditDetailAdd :getInstallment: {}", creditDetailAdd.getInstallment());
                newCreditFacilityView.getNewCreditDetailViewList().add(creditDetailAdd);
                ProposeCreditDetailView newProposeCredit = proposeCreditDetailTransform.convertNewCreditToProposeCredit(creditDetailAdd, seq);
                if (Util.isNull(proposeCreditDetailViewList)) {
                    proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
                }
                proposeCreditDetailViewList.add(newProposeCredit);
                // Grouping ProposeCredit by TypeOfStep (N -> E) and Order the seqNumber for display on "Collateral and Guarantor" dialog
                creditFacProposeControl.groupTypeOfStepAndOrderBySeq(proposeCreditDetailViewList);
                log.debug("seq of credit after add proposeCredit :: {}", seq);
                hashSeqCredit.put(seq, 0);
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
                newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).setDeleteTmpList(newCreditDetailView.getDeleteTmpList());
                log.debug("detail list ::: {}",newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex).getNewCreditTierDetailViewList());
                creditFacProposeControl.calculateInstallment(newCreditFacilityView.getNewCreditDetailViewList().get(rowIndex),basicInfoView,tcgView,newCreditFacilityView);
            } else {
                log.debug("onSaveCreditInfo ::: Undefined modeForButton !!");
            }
            complete = true;
            creditFlag = true;
            seq++;
            log.debug("seq++ of credit after add complete proposeCredit :: {}", seq);
        } else {
            log.debug("onSaveCreditInfo ::: validation failed.");
            complete = false;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteCreditInfo() {
        if(hashSeqCredit.containsKey(newCreditDetailSelected.getSeq())){
            int a = (Integer)hashSeqCredit.get(newCreditDetailSelected.getSeq());
            if(a < 1){ // can delete
                deleteCreditIdList.add(newCreditDetailSelected.getId());
                newCreditFacilityView.getNewCreditDetailViewList().remove(newCreditDetailSelected);
            } else {
                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.propose.error.delete.credit");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }
        creditFlag = true;
    }

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
                    suggestPriceLabel = suggestBase.getName() + " +" + suggestInterestDlg.doubleValue();
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
                    standardPriceLabel = standardBase.getName() + " +" + standardInterestDlg.doubleValue();
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
        creditTierDetailAdd.setFinalBasePrice(baseRateTransform.transformToView(finalBaseRate));

        creditTierDetailAdd.setSuggestPriceLabel(suggestPriceLabel);
        creditTierDetailAdd.setSuggestInterest(new BigDecimal(suggestInterestDlg.doubleValue()));
        creditTierDetailAdd.setSuggestBasePrice(baseRateTransform.transformToView(suggestBase));

        creditTierDetailAdd.setStandardPriceLabel(standardPriceLabel);
        creditTierDetailAdd.setStandardInterest(new BigDecimal(standardInterestDlg.doubleValue()));
        creditTierDetailAdd.setStandardBasePrice(baseRateTransform.transformToView(standardBase));


        if (newCreditDetailView.getRequestType()== RequestTypes.NEW.value()) {
            log.debug("newCreditDetailView.getRequestType() ::: {}", newCreditDetailView.getRequestType());
            if (newCreditDetailView.getNewCreditTierDetailViewList() != null) {
                newCreditDetailView.getNewCreditTierDetailViewList().add(0, creditTierDetailAdd);
            } else {
                List<NewCreditTierDetailView> tierDetailViewList = new ArrayList<NewCreditTierDetailView>();
                tierDetailViewList.add(0, creditTierDetailAdd);
                newCreditDetailView.setNewCreditTierDetailViewList(tierDetailViewList);
            }
        } else if (newCreditDetailView.getRequestType() == RequestTypes.CHANGE.value()) {
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
        log.info("onDeleteProposeTierInfo ::: {}",newCreditDetailView.getNewCreditTierDetailViewList().get(row).getId());
        newCreditDetailView.getDeleteTmpList().add(newCreditDetailView.getNewCreditTierDetailViewList().get(row).getId());
        newCreditDetailView.getNewCreditTierDetailViewList().remove(row);
    }

//  **************************************** END Tier Dialog  ****************************************//

// **************************************** Start Propose Collateral Information  *********************************//
    public void onChangeHeadCollType(NewCollateralHeadView newCollateralHeadView) {
        log.info("onChangeHeadCollType :: ");
        if (newCollateralHeadView.getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralHeadView.getHeadCollType().getId());
            List<SubCollateralType> subCollateralTypeResult = subCollateralTypeDAO.findByHeadAndSubColDefaultType(collateralType);
            if(subCollateralTypeResult!=null && subCollateralTypeResult.size()>0){
               SubCollateralType subCollateralType = subCollateralTypeDAO.findById(subCollateralTypeResult.get(0).getId());
               newCollateralHeadView.setSubCollType(subCollateralType);
            }else{
                newCollateralHeadView.setSubCollType(null);
            }

        }

    }

    //TODO change to tcgCollateralType for sub head
    public void onChangeCollTypeLTV(NewCollateralHeadView newCollateralHeadView) {
//        if (!flagComs) {
//            if (newCollateralHeadView.getPotentialCollateral().getId() != 0) {
//                log.info("onChangePotentialCollateralType ::: newCollateralHeadView.getPotentialCollateral().getId() : {}", newCollateralHeadView.getPotentialCollateral().getId());
//
//                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollateralHeadView.getPotentialCollateral().getId());
//
//                if (potentialCollateral != null) {
//                    log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);
//
//                    //*** Get TCG Collateral  List from Potential Collateral    ***//
//                    headCollTypeList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);
//
//                    if (headCollTypeList == null) {
//                        headCollTypeList = new ArrayList<PotentialColToTCGCol>();
//                    }
//
//                    log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", headCollTypeList.size());
//                }
//            }
//            log.debug("onChangeCollTypeLTV ::; {}", newCollateralHeadView.getTcgCollateralType().getId());
//            TCGCollateralType headType = tcgCollateralTypeDAO.findById(newCollateralHeadView.getTcgCollateralType().getId());
//            newCollateralHeadView.setTcgHeadCollType(headType);
//        }
    }

    public void onChangePotentialCollateralType(NewCollateralHeadView newCollateralHeadView) {
        if (newCollateralHeadView.getPotentialCollateral().getId() != 0) {
            log.info("onChangePotentialCollateralType ::: newCollateralHeadView.getPotentialCollateral().getId() : {}", newCollateralHeadView.getPotentialCollateral().getId());
            headCollTypeList = new ArrayList<PotentialColToTCGCol>();
            potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();

            PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollateralHeadView.getPotentialCollateral().getId());

            if (potentialCollateral != null) {
                log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);

                //*** Get TCG Collateral  List from Potential Collateral    ***//
                potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);

                if (potentialColToTCGColList == null) {
                    potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
                }

                log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", potentialColToTCGColList.size());
            }
        }
    }

    public void onCheckNoFlag(ProposeCreditDetailView proposeCreditDetailView){
        log.debug("onCheckNoFlag - proposeCreditDetailView ::: {} ",proposeCreditDetailView);
        if(proposeCreditDetailView.isNoFlag()){
            if(hashSeqCredit.containsKey(proposeCreditDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCredit.get(proposeCreditDetailView.getSeq());
                hashSeqCredit.put(proposeCreditDetailView.getSeq(),++tmpCount);
            }
            if(hashSeqCreditTmp.containsKey(proposeCreditDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCreditTmp.get(proposeCreditDetailView.getSeq());
                hashSeqCreditTmp.put(proposeCreditDetailView.getSeq(),++tmpCount);
            } else {
                hashSeqCreditTmp.put(proposeCreditDetailView.getSeq(),1);
            }
        } else {
            if(hashSeqCredit.containsKey(proposeCreditDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCredit.get(proposeCreditDetailView.getSeq());
                hashSeqCredit.put(proposeCreditDetailView.getSeq(),--tmpCount);
            }
            if(hashSeqCreditTmp.containsKey(proposeCreditDetailView.getSeq())){
                int tmpCount = (Integer)hashSeqCreditTmp.get(proposeCreditDetailView.getSeq());
                hashSeqCreditTmp.put(proposeCreditDetailView.getSeq(),--tmpCount);
            }
        }
    }

    public void onCancelProposeCollInfo(){
        log.debug("onCancelProposeCollInfo");
        if(proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0){
            for(ProposeCreditDetailView pcdv : proposeCreditDetailViewList){
                if(hashSeqCreditTmp.containsKey(pcdv.getSeq())){
                    int tmpCount = (Integer)hashSeqCreditTmp.get(pcdv.getSeq());
                    int tmpCountOrigin = (Integer)hashSeqCredit.get(pcdv.getSeq());
                    if(tmpCount != 0){
                        hashSeqCreditTmp.put(pcdv.getSeq(),0);
                        if(tmpCount > 0){
                            hashSeqCredit.put(pcdv.getSeq(),tmpCountOrigin-1);
                        } else {
                            hashSeqCredit.put(pcdv.getSeq(),tmpCountOrigin+1);
                        }
                    }
                }
            }
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onAddProposeCollInfo() {
        log.debug("onAddProposeCollInfo ::: {}", newCreditFacilityView.getNewCollateralViewList().size());
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();
        modeForButton = ModeForButton.ADD;
        newCollateralView = new NewCollateralView();
        newCollateralView.getNewCollateralHeadViewList().add(new NewCollateralHeadView());
        if(proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0){
            for(ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList){
                proposeCreditDetailView.setNoFlag(false);
            }
        }
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
        flagButtonCollateral = true;
        flagComs = false;
        editProposeColl = false;
    }

    public void onEditProposeCollInfo() {
        log.debug("onEditProposeCollInfo :: {},::rowIndexCollateral  {}", selectCollateralDetailView.getId(), rowIndexCollateral);
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();
        modeForButton = ModeForButton.EDIT;
        editProposeColl = true;
        Cloner cloner = new Cloner();
        newCollateralView = new NewCollateralView();
        newCollateralView = cloner.deepClone(selectCollateralDetailView);
        for (NewCollateralHeadView newCollHeadEdit : newCollateralView.getNewCollateralHeadViewList()) {
            if (newCollHeadEdit.getPotentialCollateral().getId() != 0) {
                log.info("onChangePotentialCollateralType ::: newCollateralHeadView.getPotentialCollateral().getId() : {}", newCollHeadEdit.getPotentialCollateral().getId());
                headCollTypeList = new ArrayList<PotentialColToTCGCol>();
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(newCollHeadEdit.getPotentialCollateral().getId());
                if (potentialCollateral != null) {
                    log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);
                    //*** Get TCG Collateral  List from Potential Collateral    ***//
                    potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);
                    if (potentialColToTCGColList == null) {
                        potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
                    }
                    log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", potentialColToTCGColList.size());
                    newCollHeadEdit.setTcgCollateralType(newCollHeadEdit.getTcgCollateralType());
                }
            }
        }

        flagComs = false;
        proposeCreditDetailViewList = creditFacProposeControl.setNoFlagForCollateralRelateCredit(selectCollateralDetailView,proposeCreditDetailViewList,newCreditFacilityView.getId());
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

        log.debug("selectCollateralDetailView.isComs() ::: {}", newCollateralView.isComs());
        if (newCollateralView.isComs()) { //??????? data  ????????? coms  set rendered false (??????????? edit)
            flagButtonCollateral = false;
            flagComs = true;
        } else {
            flagButtonCollateral = true;
        }
    }

    public void onSaveProposeCollInfo() {
        log.debug("rowIndexCollateral :: {}, mode : {}", rowIndexCollateral, modeForButton);
        boolean complete = false;
        boolean complete1 = false;
        boolean complete2 = false;
        boolean complete3;

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
//                    CollateralType collTypePercentLTV = collateralTypeDAO.findById(newCollateralHeadView.getCollTypePercentLTV().getId());
                    CollateralType headCollType = collateralTypeDAO.findById(newCollateralHeadView.getHeadCollType().getId());
                    TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(newCollateralHeadView.getTcgCollateralType().getId());
                    SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newCollateralHeadView.getSubCollType().getId());

                    NewCollateralHeadView newCollateralHeadDetailAdd = new NewCollateralHeadView();
                    newCollateralHeadDetailAdd.setPotentialCollateral(potentialCollateral);
//                    newCollateralHeadDetailAdd.setCollTypePercentLTV(collTypePercentLTV);
                    newCollateralHeadDetailAdd.setExistingCredit(newCollateralHeadView.getExistingCredit());
                    newCollateralHeadDetailAdd.setTitleDeed(newCollateralHeadView.getTitleDeed());
                    newCollateralHeadDetailAdd.setCollateralLocation(newCollateralHeadView.getCollateralLocation());
                    newCollateralHeadDetailAdd.setAppraisalValue(newCollateralHeadView.getAppraisalValue());
                    newCollateralHeadDetailAdd.setHeadCollType(headCollType);
                    newCollateralHeadDetailAdd.setInsuranceCompany(newCollateralHeadView.getInsuranceCompany());
                    newCollateralHeadDetailAdd.setTcgCollateralType(tcgCollateralType);
                    newCollateralHeadDetailAdd.setSubCollType(subCollateralType);

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

            proposeCollateralInfoAdd.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
            if(proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0){
                for(ProposeCreditDetailView pcdv : proposeCreditDetailViewList){
                    if(pcdv.isNoFlag()){
                        proposeCollateralInfoAdd.getProposeCreditDetailViewList().add(pcdv);
                    }
                }
                complete3 = true;
            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                complete3 = false;
            }

            if (complete1 && complete2 && complete3) {
                newCreditFacilityView.getNewCollateralViewList().add(proposeCollateralInfoAdd);
                complete = true;
                flagComs = false;
            } else {
                complete = false;
            }

            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);

        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            if (newCollateralView.getNewCollateralHeadViewList().size() > 0) {
                for (int i = 0; i < newCollateralView.getNewCollateralHeadViewList().size(); i++) {
                    if(newCollateralView.getNewCollateralHeadViewList().get(i).getNewCollateralSubViewList() != null &&
                            newCollateralView.getNewCollateralHeadViewList().get(i).getNewCollateralSubViewList().size() > 0){
                    } else {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = msg.get("app.propose.desc.add.sub.data");
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg2.show()");
                        return;
                    }
                }
            }

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
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setNewCollateralHeadViewList(new ArrayList<NewCollateralHeadView>());

            if (newCollateralView.getNewCollateralHeadViewList().size() > 0) {
                for (int i = 0; i < newCollateralView.getNewCollateralHeadViewList().size(); i++) {
                    PotentialCollateral potentialCollateralEdit = potentialCollateralDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getPotentialCollateral().getId());
                    TCGCollateralType tcgCollateralType = tcgCollateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getTcgCollateralType().getId());
                    CollateralType headCollTypeEdit = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(i).getHeadCollType().getId());
//                    SubCollateralType subCollateralTypeEdit = subCollateralTypeDAO.findByHeadAndSubColDefaultType(headCollTypeEdit);
                    List<SubCollateralType> subCollateralTypeResult = subCollateralTypeDAO.findByHeadAndSubColDefaultType(headCollTypeEdit);

                    if(subCollateralTypeResult != null && subCollateralTypeResult.size() > 0){
                        SubCollateralType subCollateralTypeEdit = subCollateralTypeDAO.findById(subCollateralTypeResult.get(0).getId());
                        newCollateralView.getNewCollateralHeadViewList().get(i).setSubCollType(subCollateralTypeEdit);
                    }

                    newCollateralView.getNewCollateralHeadViewList().get(i).setPotentialCollateral(potentialCollateralEdit);
                    newCollateralView.getNewCollateralHeadViewList().get(i).setHeadCollType(headCollTypeEdit);
                    newCollateralView.getNewCollateralHeadViewList().get(i).setTcgCollateralType(tcgCollateralType);
                }
            }

            newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setNewCollateralHeadViewList(newCollateralView.getNewCollateralHeadViewList());

            if (flagComs) {
                newCollateralView.setComs(false);
                flagButtonCollateral = false;
            } else {
                newCollateralView.setComs(true);
                flagButtonCollateral = true;
            }

            List<ProposeCreditDetailView> proposeCreditDetailViews = new ArrayList<ProposeCreditDetailView>();
            if(proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0){
                for(ProposeCreditDetailView pcdv : proposeCreditDetailViewList){
                    if(pcdv.isNoFlag()){
                        proposeCreditDetailViews.add(pcdv);
                    }
                }
                complete = true;
                newCreditFacilityView.getNewCollateralViewList().get(rowIndexCollateral).setProposeCreditDetailViewList(proposeCreditDetailViews);
                editProposeColl = false;
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
        //check usage relate with
        List<String> removeSubId = new ArrayList<String>();
        List<String> allSubId = new ArrayList<String>();
        if(selectCollateralDetailView.getNewCollateralHeadViewList() != null && selectCollateralDetailView.getNewCollateralHeadViewList().size() > 0){
            for(NewCollateralHeadView nchv : selectCollateralDetailView.getNewCollateralHeadViewList()){
                if(nchv.getNewCollateralSubViewList() != null && nchv.getNewCollateralSubViewList().size() > 0){
                    for(NewCollateralSubView ncsv : nchv.getNewCollateralSubViewList()){
                        removeSubId.add(ncsv.getSubId());
                    }
                }
            }
        }

        if(newCreditFacilityView.getNewCollateralViewList() != null && newCreditFacilityView.getNewCollateralViewList().size() > 0){
            for(NewCollateralView ncv : newCreditFacilityView.getNewCollateralViewList()){
                if(ncv.getNewCollateralHeadViewList() != null && ncv.getNewCollateralHeadViewList().size() > 0){
                    for(NewCollateralHeadView nchv : ncv.getNewCollateralHeadViewList()){
                        if(nchv.getNewCollateralSubViewList() != null && nchv.getNewCollateralSubViewList().size() > 0){
                            for(NewCollateralSubView ncsv : nchv.getNewCollateralSubViewList()){
                                if(ncsv.getRelatedWithList() != null && ncsv.getRelatedWithList().size() > 0){
                                    for(NewCollateralSubView ncsvRelate : ncsv.getRelatedWithList()){
                                        allSubId.add(ncsvRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(removeSubId != null && removeSubId.size() > 0 && allSubId != null && allSubId.size() > 0){
            for(String asid : allSubId){
                for(String rsid : removeSubId){
                    if(rsid.equalsIgnoreCase(asid)){
                        messageHeader = msg.get("app.propose.exception");
                        message = msg.get("app.propose.error.delete.coll.relate");
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
        }
        //end check related

        if(selectCollateralDetailView.getProposeCreditDetailViewList() != null && selectCollateralDetailView.getProposeCreditDetailViewList().size() > 0){
            for(ProposeCreditDetailView pcdv : selectCollateralDetailView.getProposeCreditDetailViewList()){
                if(hashSeqCredit.containsKey(pcdv.getSeq())){
                    int a = (Integer)hashSeqCredit.get(pcdv.getSeq());
                    hashSeqCredit.put(pcdv.getSeq(),--a);
                }
            }
        }

        if (selectCollateralDetailView.getId() != 0) {
            deleteCollIdList.add(selectCollateralDetailView.getId());
        }

        if(newCreditFacilityView.getNewCollateralViewList() != null && newCreditFacilityView.getNewCollateralViewList().size() > 0){
            newCreditFacilityView.getNewCollateralViewList().remove(selectCollateralDetailView);
        }
    }

    // ****************************************************Start Add SUB Collateral****************************************************//

    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral and rowCollHeadIndex :: {}", rowCollHeadIndex);
        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {

            RequestContext.getCurrentInstance().execute("subCollateralInfoDlg.show()");
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            subCollateralTypeViewList = subCollateralTypeTransform.transformToView(subCollateralTypeList);
            log.debug("subCollateralTypeList ::: {}", subCollateralTypeList.size());

            newCollateralSubView = new NewCollateralSubView();
            newCollateralSubView.setHeadCollType(collateralType);
            modeForSubColl = ModeForButton.ADD;
            log.debug(" newCreditFacilityView.getNewCollateralViewList().size ::{}", newCreditFacilityView.getNewCollateralViewList().size());

            relatedWithSelected = new NewCollateralSubView();
            relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(newCreditFacilityView.getNewCollateralViewList());
            newCollateralSubView.setRelatedWithList(new ArrayList<NewCollateralSubView>());

            UUID uid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
            newCollateralSubView.setSubId(uid.randomUUID().toString());

        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = "Please to choose Head Collateral Type";
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

    }

    public void onEditSubCollateral() {
        log.info("rowSubIndex :: {}", rowSubIndex);
        modeForSubColl = ModeForButton.EDIT;
        newCollateralSubView = new NewCollateralSubView();

        relatedWithSelected = new NewCollateralSubView();
        relatedWithAllList = creditFacProposeControl.findNewCollateralSubView(newCreditFacilityView.getNewCollateralViewList());
        if(relatedWithAllList != null && relatedWithAllList.size() > 0){
            for(NewCollateralSubView ncsv : relatedWithAllList){
                if(ncsv.getSubId().equalsIgnoreCase(subCollateralDetailItem.getSubId())){
                    relatedWithAllList.remove(ncsv);
                    break;
                }
            }
        }

        if (newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId() != 0) {
            CollateralType collateralType = collateralTypeDAO.findById(newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getHeadCollType().getId());
            subCollateralTypeList = subCollateralTypeDAO.findByCollateralType(collateralType);
            newCollateralSubView.setHeadCollType(collateralType);
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
        newCollateralSubView.setSubId(subCollateralDetailItem.getSubId());
    }

    public void onSaveSubCollateral() {
        log.info("onSaveSubCollateral ::: mode : {}", modeForSubColl);
        boolean complete;
        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)) {
            log.debug("modeForSubColl ::: {}", modeForSubColl);
            log.debug("newCollateralSubView.getRelatedWithList() :: {}", newCollateralSubView.getRelatedWithList().size());
            NewCollateralSubView subCollAdd = new NewCollateralSubView();

            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newCollateralSubView.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);

            CollateralType headCollType = collateralTypeDAO.findById(newCollateralSubView.getHeadCollType().getId());
            subCollAdd.setHeadCollType(headCollType);

            subCollAdd.setTitleDeed(newCollateralSubView.getTitleDeed());
            subCollAdd.setAddress(newCollateralSubView.getAddress());
            subCollAdd.setLandOffice(newCollateralSubView.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(newCollateralSubView.getCollateralOwnerAAD());
            subCollAdd.setAppraisalValue(newCollateralSubView.getAppraisalValue());
            subCollAdd.setMortgageValue(newCollateralSubView.getMortgageValue());
            subCollAdd.setCollateralOwnerUWList(newCollateralSubView.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(newCollateralSubView.getMortgageList());
            subCollAdd.setRelatedWithList(newCollateralSubView.getRelatedWithList());
            subCollAdd.setSubId(newCollateralSubView.getSubId());

            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().add(subCollAdd);
            complete = true;
        } else if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.EDIT)) {
            log.info("modeForSubColl ::: {}", modeForSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(newCollateralSubView.getSubCollateralType().getId());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setSubCollateralType(subCollateralType);

            CollateralType headCollType = collateralTypeDAO.findById(newCollateralSubView.getHeadCollType().getId());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setHeadCollType(headCollType);

            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setTitleDeed(newCollateralSubView.getTitleDeed());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setAddress(newCollateralSubView.getAddress());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setLandOffice(newCollateralSubView.getLandOffice());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setCollateralOwnerAAD(newCollateralSubView.getCollateralOwnerAAD());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setAppraisalValue(newCollateralSubView.getAppraisalValue());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setMortgageValue(newCollateralSubView.getMortgageValue());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setCollateralOwnerUWList(newCollateralSubView.getCollateralOwnerUWList());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setMortgageList(newCollateralSubView.getMortgageList());
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setRelatedWithList(newCollateralSubView.getRelatedWithList());
//            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setSubId(uid.randomUUID().toString()); // only gen UUID in add new
            newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().get(rowSubIndex).setSubId(newCollateralSubView.getSubId());

            complete = true;
        } else {
            log.debug("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }
        log.debug("complete >>>> :::: {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteSubCollateral() {
        //check usage relate with
        List<String> allSubId = new ArrayList<String>();

        if(newCreditFacilityView.getNewCollateralViewList() != null && newCreditFacilityView.getNewCollateralViewList().size() > 0){
            for(NewCollateralView ncv : newCreditFacilityView.getNewCollateralViewList()){
                if(ncv.getNewCollateralHeadViewList() != null && ncv.getNewCollateralHeadViewList().size() > 0){
                    for(NewCollateralHeadView nchv : ncv.getNewCollateralHeadViewList()){
                        if(nchv.getNewCollateralSubViewList() != null && nchv.getNewCollateralSubViewList().size() > 0){
                            for(NewCollateralSubView ncsv : nchv.getNewCollateralSubViewList()){
                                if(ncsv.getRelatedWithList() != null && ncsv.getRelatedWithList().size() > 0){
                                    for(NewCollateralSubView ncsvRelate : ncsv.getRelatedWithList()){
                                        allSubId.add(ncsvRelate.getSubId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(allSubId != null && allSubId.size() > 0){
            for(String asid : allSubId){
                if(subCollateralDetailItem.getSubId().equalsIgnoreCase(asid)){
                    messageHeader = msg.get("app.propose.exception");
                    message = msg.get("app.propose.error.delete.coll.relate");
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        newCollateralView.getNewCollateralHeadViewList().get(rowCollHeadIndex).getNewCollateralSubViewList().remove(subCollateralDetailItem);
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
        log.info("onAddRelatedWith() relatedWithSelected ::: {}", relatedWithSelected);
        if(newCollateralSubView.getRelatedWithList() != null){
            if(newCollateralSubView.getRelatedWithList().size() > 0){
                for(NewCollateralSubView relateList : newCollateralSubView.getRelatedWithList()){
                    if(relatedWithSelected.getSubId().equalsIgnoreCase(relateList.getSubId())){
                        return;
                    }
                }
                NewCollateralSubView relatedWith = getNewSubCollDetailBySubId(relatedWithSelected.getSubId());
                if(relatedWith != null){
                    newCollateralSubView.getRelatedWithList().add(relatedWith);
                }
            } else {
                NewCollateralSubView relatedWith = getNewSubCollDetailBySubId(relatedWithSelected.getSubId());
                if(relatedWith != null){
                    newCollateralSubView.getRelatedWithList().add(relatedWith);
                }
            }
        }
    }

    public void onDeleteRelatedWith(int row) {
        newCollateralSubView.getRelatedWithList().remove(row);
    }

    public NewCollateralSubView getNewSubCollDetailBySubId(String subId) {
        if(newCreditFacilityView != null){
            if (newCreditFacilityView.getNewCollateralViewList() != null && newCreditFacilityView.getNewCollateralViewList().size() > 0) {
                for (NewCollateralView newCollateralView : newCreditFacilityView.getNewCollateralViewList()) {
                    if(newCollateralView.getNewCollateralHeadViewList() != null && newCollateralView.getNewCollateralHeadViewList().size() > 0){
                        for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                            if(newCollateralHeadView.getNewCollateralSubViewList() != null && newCollateralHeadView.getNewCollateralSubViewList().size() > 0){
                                for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
                                    if(newCollateralSubView.getSubId().equalsIgnoreCase(subId)){
                                        return newCollateralSubView;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // ****************************************************END Add SUB Collateral **************************************************** //

    //  *************************************************** END Propose Collateral Information  ****************************************************//

    // **************************************************** Start Guarantor ****************************************************//

    public void onAddGuarantorInfo() {
        newGuarantorDetailView = new NewGuarantorDetailView();
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();
        modeForButton = ModeForButton.ADD;
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        newGuarantorDetailView.setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
        if(proposeCreditDetailViewList != null && proposeCreditDetailViewList.size() > 0){
            for(ProposeCreditDetailView proposeCreditDetailView : proposeCreditDetailViewList){
                proposeCreditDetailView.setNoFlag(false);
            }
        }
        newCollateralView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
        newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);
    }

    public void onEditGuarantorInfo() {
        log.debug("onEditGuarantorInfo");
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();
        modeForButton = ModeForButton.EDIT;
        newGuarantorDetailView = new NewGuarantorDetailView();
        newGuarantorDetailView.setGuarantorName(newGuarantorDetailViewItem.getGuarantorName());
        newGuarantorDetailView.setTcgLgNo(newGuarantorDetailViewItem.getTcgLgNo());
        newGuarantorDetailView.setGuarantorCategory(newGuarantorDetailViewItem.getGuarantorCategory());
        selectedGuarantorCrdTypeItems = new ArrayList<ProposeCreditDetailView>();
        proposeCreditDetailViewList = creditFacProposeControl.setNoFlagForGuarantorRelateCredit(newGuarantorDetailViewItem,proposeCreditDetailViewList,newCreditFacilityView.getId());
        newGuarantorDetailView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

        if (newGuarantorDetailViewItem.getProposeCreditDetailViewList() != null && newGuarantorDetailViewItem.getProposeCreditDetailViewList().size() > 0) {
            selectedGuarantorCrdTypeItems = newGuarantorDetailViewItem.getProposeCreditDetailViewList();

            for (ProposeCreditDetailView proposeCreditDetailView : newGuarantorDetailView.getProposeCreditDetailViewList()) {
                for (ProposeCreditDetailView proposeCreditDetailSelect : selectedGuarantorCrdTypeItems) {
                    if (proposeCreditDetailView.getSeq() == proposeCreditDetailSelect.getSeq()) {
                        proposeCreditDetailView.setGuaranteeAmount(proposeCreditDetailSelect.getGuaranteeAmount());
                    }
                }
            }
        }
    }

    public void onSaveGuarantorInfoDlg() {
        log.debug("onSaveGuarantorInfoDlg ::: mode : {}", modeForButton);
        boolean complete = false;
        BigDecimal summary = BigDecimal.ZERO;
        int seqTemp;
        boolean checkPlus;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            log.debug("modeForButton ::: {}", modeForButton);
            NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
            if(newGuarantorDetailView.getGuarantorName() != null){
                if(newGuarantorDetailView.getGuarantorName().getId() == -1){
                    guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    guarantorDetailAdd.setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(newGuarantorDetailView.getGuarantorName().getId(), guarantorList);
                    guarantorDetailAdd.setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        guarantorDetailAdd.setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            } else {
                guarantorDetailAdd.setGuarantorName(null);
                guarantorDetailAdd.setGuarantorCategory(null);
            }

            guarantorDetailAdd.setTcgLgNo(newGuarantorDetailView.getTcgLgNo());

            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                List<ProposeCreditDetailView> newCreditTypeItems = new ArrayList<ProposeCreditDetailView>();
                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    creditTypeItem.setNoFlag(true);
                    newCreditTypeItems.add(creditTypeItem);
                    log.debug("creditTypeItem.getGuaranteeAmount() :: {}", creditTypeItem.getGuaranteeAmount());
                    summary = Util.add(summary, creditTypeItem.getGuaranteeAmount());
                }

                guarantorDetailAdd.setProposeCreditDetailViewList(newCreditTypeItems);
                guarantorDetailAdd.setTotalLimitGuaranteeAmount(summary);
                newCreditFacilityView.getNewGuarantorDetailViewList().add(guarantorDetailAdd);
                complete = true;

                for (int j = 0; j < guarantorDetailAdd.getProposeCreditDetailViewList().size(); j++) {
                    seqTemp = guarantorDetailAdd.getProposeCreditDetailViewList().get(j).getSeq();
                    log.info("seqTemp :: {}",seqTemp);
                    if(hashSeqCredit.containsKey(j)){
                        if (guarantorDetailAdd.getProposeCreditDetailViewList().get(j).isNoFlag()) {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) + 1);
                        } else {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(j).toString()) - 1);
                        }
                    }
                }

            } else {
                messageHeader = msg.get("app.propose.exception");
                message = msg.get("app.propose.desc.add.data");
                severity = MessageDialogSeverity.ALERT.severity();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            log.debug("modeForButton ::: {}", modeForButton);
            if(newGuarantorDetailView.getGuarantorName() != null){
                if(newGuarantorDetailView.getGuarantorName().getId() == -1){
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(GuarantorCategory.TCG);
                    CustomerInfoView customerInfoView = new CustomerInfoView();
                    customerInfoView.setId(-1);
                    customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(customerInfoView);
                } else {
                    CustomerInfoView customerInfoView = customerInfoControl.getCustomerInfoViewById(newGuarantorDetailView.getGuarantorName().getId(), guarantorList);
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(customerInfoView);
                    if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.INDIVIDUAL.value()) {
                        newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(GuarantorCategory.INDIVIDUAL);
                    } else if (customerInfoView.getCustomerEntity().getId() == GuarantorCategory.JURISTIC.value()) {
                        newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(GuarantorCategory.JURISTIC);
                    } else {
                        newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(GuarantorCategory.NA);
                    }
                }
            } else {
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(null);
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorCategory(null);
            }

            newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(newGuarantorDetailView.getTcgLgNo());
            newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setProposeCreditDetailViewList(new ArrayList<ProposeCreditDetailView>());
            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {

                for (ProposeCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    creditTypeItem.setNoFlag(true);
                    newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().add(creditTypeItem);
                    log.debug(" newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().get(0).getGuaranteeAmount(); :: {}", newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getProposeCreditDetailViewList().get(0).getGuaranteeAmount());
                    summary = Util.add(summary, creditTypeItem.getGuaranteeAmount());

                    seqTemp = creditTypeItem.getSeq();
                    checkPlus = true;

                    for (int j = 0; j < newGuarantorDetailViewItem.getProposeCreditDetailViewList().size(); j++) {
                        if (newGuarantorDetailViewItem.getProposeCreditDetailViewList().get(j).getSeq() == seqTemp) {
                            checkPlus = false;
                        }
                    }

                    if(hashSeqCredit.containsKey(seqTemp)){
                        if (checkPlus) {
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) + 1);
                        }else{
                            hashSeqCredit.put(seqTemp, Integer.parseInt(hashSeqCredit.get(seqTemp).toString()) - 1);
                        }
                    }
                }
                newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).setTotalLimitGuaranteeAmount(summary);
                complete = true;
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

        newCreditFacilityView.setTotalGuaranteeAmount(creditFacProposeControl.calTotalGuaranteeAmount(newCreditFacilityView.getNewGuarantorDetailViewList()));
        log.debug("  complete >>>>  :  {}", complete);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onDeleteGuarantorInfo() {
        if(newGuarantorDetailViewItem.getProposeCreditDetailViewList() != null && newGuarantorDetailViewItem.getProposeCreditDetailViewList().size() > 0){
            for(ProposeCreditDetailView pcdv : newGuarantorDetailViewItem.getProposeCreditDetailViewList()){
                if(hashSeqCredit.containsKey(pcdv.getSeq())){
                    int a = (Integer)hashSeqCredit.get(pcdv.getSeq());
                    hashSeqCredit.put(pcdv.getSeq(),--a);
                }
            }
        }

        if (newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getId() != 0) {
            deleteGuarantorIdList.add(newCreditFacilityView.getNewGuarantorDetailViewList().get(rowIndexGuarantor).getId());
        }

        newCreditFacilityView.getNewGuarantorDetailViewList().remove(newGuarantorDetailViewItem);
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

        try {
            //TEST FOR NEW FUNCTION SAVE CREDIT FACILITY
            creditFacProposeControl.deleteAllNewCreditFacilityByIdList(deleteCreditIdList, deleteCollIdList, deleteGuarantorIdList, deleteConditionIdList,workCaseId);
            // Calculate Total Propose
            newCreditFacilityView = creditFacProposeControl.calculateTotalProposeAmount(newCreditFacilityView, basicInfoView, tcgView, workCaseId);
            // Calculate Total for BRMS
            newCreditFacilityView = creditFacProposeControl.calculateTotalForBRMS(newCreditFacilityView);
            //  Calculate Maximum SME Limit
            newCreditFacilityView = creditFacProposeControl.calculateMaximumSMELimit(newCreditFacilityView, workCaseId);
            // Save NewCreditFacility, ProposeCredit, Collateral, Guarantor
            newCreditFacilityView = creditFacProposeControl.saveCreditFacility(newCreditFacilityView, workCaseId, hashSeqCredit, deleteCreditIdList);
            // Calculate WC
            creditFacProposeControl.calWC(workCaseId);

            onCreation();

            exSummaryControl.calForCreditFacility(workCaseId);

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

    public List<PotentialColToTCGCol> getPotentialColToTCGColList() {
        return potentialColToTCGColList;
    }

    public void setPotentialColToTCGColList(List<PotentialColToTCGCol> potentialColToTCGColList) {
        this.potentialColToTCGColList = potentialColToTCGColList;
    }

    public List<PotentialColToTCGCol> getHeadCollTypeList() {
        return headCollTypeList;
    }

    public void setHeadCollTypeList(List<PotentialColToTCGCol> headCollTypeList) {
        this.headCollTypeList = headCollTypeList;
    }
}

