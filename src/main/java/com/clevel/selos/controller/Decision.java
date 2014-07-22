package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.ApprovalHistoryDAO;
import com.clevel.selos.dao.working.ProposeCreditInfoDAO;
import com.clevel.selos.dao.working.ProposeFeeDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.*;
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
import java.util.*;

@ViewScoped
@ManagedBean(name = "decision")
public class Decision extends BaseController {
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
    private ProposeLineControl proposeLineControl;
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
    @Inject
    private BRMSControl brmsControl;
    @Inject
    private FullApplicationControl fullApplicationControl;
    @Inject
    private StepStatusControl stepStatusControl;

    //DAO
    @Inject
    private CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;
    @Inject
    private FollowConditionDAO followConditionDAO;
    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private ProposeCreditInfoDAO newCreditDetailDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private ProposeFeeDetailDAO feeDetailDAO;
    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;

    //Transform
    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private DisbursementTypeTransform disbursementTypeTransform;
    @Inject
    private LoanPurposeTransform loanPurposeTransform;
    @Inject
    private FollowConditionTransform followConditionTransform;
    @Inject
    private ApprovalHistoryTransform approvalHistoryTransform;
    @Inject
    private SpecialProgramTransform specialProgramTransform;
    @Inject
    private PotentialCollateralTransform potentialCollateralTransform;
    @Inject
    private CollateralTypeTransform collateralTypeTransform;
    @Inject
    private SubCollateralTypeTransform subCollateralTypeTransform;
    @Inject
    private MortgageTypeTransform mortgageTypeTransform;
    @Inject
    private ProposeLineTransform proposeLineTransform;
    @Inject
    private BaseRateTransform baseRateTransform;
    @Inject
    private FeeTransform feeTransform;
    @Inject
    private DecisionFollowConditionTransform decisionFollowConditionTransform;

    // Session
    private long workCaseId;
    private long stepId;
    private int roleId;
    private User user;
    private DecisionView decisionView;

    // Dialog Messages
    private String messageHeader;
    private String message;
    private String severity;

    // Approve Credit Info Dialog
    private ProposeCreditInfoDetailView approveCreditInfoDetailView;
    private ProductGroup productGroup;
    private List<CreditRequestTypeView> creditRequestTypeViewList;
    private List<CountryView> countryViewList;
    //Drop Down Credit Info Dialog
    private List<PrdGroupToPrdProgramView> productProgramViewList;
    private List<PrdProgramToCreditTypeView> creditTypeViewList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<DisbursementTypeView> disbursementTypeViewList;
    private List<BaseRate> baseRateList;
    //Static Value for Credit Info Dialog
    private int applyTCG;
    private int specialProgramId;

    // Approve Guarantor
    private ProposeGuarantorInfoView approveGuarantorInfoView;
    private List<CustomerInfoView> guarantorList;

    private List<ProposeCreditInfoDetailView> proposeCreditViewList;

    // Approve Collateral
    private ProposeCollateralInfoView approveCollateralInfoView;
    private ProposeCollateralInfoView approveCollateralInfoViewTmp;
    //Drop Down Collateral Info Dialog
    private List<PotentialCollateralView> potentialCollViewList;
    private List<CollateralTypeView> headCollTypeViewList;
    //Sub Collateral Info Dialog
    private ProposeCollateralInfoSubView approveCollateralInfoSubView;
    //Drop Down Sub Collateral Info Dialog
    private List<SubCollateralType> subCollateralTypeList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageTypeView> mortgageTypeViewList;
    private List<ProposeCollateralInfoSubView> relateWithList;
    //Value for Add List
    private Long collOwnerId;
    private int mortgageTypeId;
    private String relateWithSubId;

    // Follow Up Condition
    private DecisionFollowConditionView decisionFollowConditionView;
    private int rowIndexDecisionFollowCondition;
    private List<FollowConditionView> followConditionViewList;

    // Approval History
    private ApprovalHistoryView approvalHistoryView;
    private ApprovalHistoryView approvalHistoryPricingView;

    private boolean requestPricing;
    private boolean decisionDialog;
    private boolean pricingDecisionDialog;
    private boolean endorseDecisionDialog;

    private boolean isModeEdit;
    enum Mode {ADD, EDIT}
    private Mode mode;
    private Mode modeSubColl;

    //For All
    private int rowIndex;
    private int rowHeadCollIndex;
    private int rowSubCollIndex; // head click on sub
    private boolean creditFlag; // for retrieve
    private int lastSeq;
    private Hashtable hashSeqCredit; // seq , usage
    private Hashtable hashSeqCreditTmp;
    private boolean workCaseRequestAppraisal;

    private WorkCase workCase;

    public Decision() {
    }

    private void initial(){
        //Initial value for onCreation
        HttpSession session = FacesUtil.getSession(true);
        workCaseId = getCurrentWorkCaseId(session);
        stepId = getCurrentStep(session);
        user = (User) session.getAttribute("user");

        //Set role for UI
        roleId = decisionControl.getUserRoleId();

        decisionDialog = checkDecisionDialog(stepId, roleId);
        endorseDecisionDialog = checkEndorseDialog(stepId, roleId);
        pricingDecisionDialog = checkPricingDialog(stepId, roleId);

    }

    public boolean checkDecisionDialog(long stepId, int roleId){
        boolean canAccess = false;
        if(roleId != RoleValue.BDM.id() && roleId != RoleValue.ABDM.id() && roleId != RoleValue.AAD_ADMIN.id() && roleId != RoleValue.AAD_COMITTEE.id() && roleId != RoleValue.SSO.id()){
            canAccess = true;
        }
        return canAccess;
    }

    public boolean checkEndorseDialog(long stepId, int roleId){
        boolean canAccess = false;
        if(roleId != RoleValue.BDM.id() && roleId != RoleValue.ABDM.id()){
            if(roleId == RoleValue.ZM.id()){
                if(stepId != StepValue.REVIEW_PRICING_REQUEST_ZM.value()){
                    canAccess = true;
                }
            } else if (roleId == RoleValue.UW.id()){
                canAccess = true;
            }
        }

        return canAccess;
    }

    public boolean checkPricingDialog(long stepId, int roleId){
        boolean canAccess = false;
        if(roleId != RoleValue.BDM.id() && roleId != RoleValue.ABDM.id()){
            if(roleId == RoleValue.ZM.id() || roleId == RoleValue.RGM.id() || roleId == RoleValue.GH.id() || roleId == RoleValue.CSSO.id()){
                if(stepId != StepValue.CREDIT_DECISION_BU_ZM.value()) {
                    canAccess = true;
                }
            }
        }

        return canAccess;
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)) {
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");
        } else {
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        initial();
        loadFieldControl(workCaseId, Screen.DECISION);

        decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);

        workCase = workCaseDAO.findById(workCaseId);
        if (!Util.isNull(workCase) && !Util.isZero(workCase.getId())) {
            productGroup = workCase.getProductGroup();
        }

        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        if (!Util.isNull(basicInfoView)) {
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

        TCGView tcgView = tcgInfoControl.getTCGView(workCaseId);
        if (tcgView != null) {
            applyTCG = tcgView.getTCG();
        }

        // ========== Approve Credit Dialog ========== //
        approveCreditInfoDetailView = new ProposeCreditInfoDetailView();

        creditRequestTypeViewList = creditRequestTypeTransform.transformToViewList(creditRequestTypeDAO.findAll());
        countryViewList = countryTransform.transformToViewList(countryDAO.findAll());
        loanPurposeViewList = loanPurposeControl.getLoanPurposeViewList();
        disbursementTypeViewList = disbursementTypeControl.getDisbursementTypeViewList();

        baseRateList = baseRateDAO.findAll();
        if (Util.isNull(baseRateList)) {
            baseRateList = new ArrayList<BaseRate>();
        }

        creditFlag = false; // flag for disable Retrieve Pricing Fee
        hashSeqCredit = new Hashtable<Integer, Integer>();
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = decisionView.getApproveCreditList();
        if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())){
            for(ProposeCreditInfoDetailView proCredit : proposeCreditInfoDetailViewList){
                hashSeqCredit.put(proCredit.getSeq(),proCredit.getUseCount());
            }
        }

        lastSeq = proposeLineControl.getLastSeqNumberFromProposeCredit(proposeCreditInfoDetailViewList);

        //disable retrieve pricing button
        if(!Util.isNull(decisionView) && !Util.isNull(decisionView.getApproveCreditList())
                && Util.isZero(decisionView.getApproveCreditList().size())) { // list not null but size = 0
            setDisabledValue("retrieveApproveCreditButton",true);
        } else if(Util.isNull(decisionView.getApproveCreditList())) { // list is null
            setDisabledValue("retrieveApproveCreditButton",true);
        }
        // ================================================== //

        // ========== Approve Guarantor Dialog ========== //
        approveGuarantorInfoView = new ProposeGuarantorInfoView();
        guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
        CustomerInfoView customerInfoView = new CustomerInfoView();
        customerInfoView.setId(-1);
        customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
        guarantorList.add(customerInfoView);
        // ================================================== //

        // ========== Approve Collateral Dialog ========== //
        approveCollateralInfoView = new ProposeCollateralInfoView();
        approveCollateralInfoViewTmp = new ProposeCollateralInfoView();
        potentialCollViewList = potentialCollateralTransform.transformToView(potentialCollateralDAO.findAll());

        if(!Util.isNull(workCase) && !Util.isZero(workCase.getId())) {
            if(!Util.isZero(workCase.getRequestAppraisal())) {
                workCaseRequestAppraisal = true;
            } else {
                workCaseRequestAppraisal = false;
            }
        }

        if(workCaseRequestAppraisal) {
            headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findByAppraisal(0));
        } else {
            headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());
        }
        // ================================================== //

        // ========== Sub Collateral Dialog ========== //
        approveCollateralInfoSubView = new ProposeCollateralInfoSubView();
        subCollateralTypeList = new ArrayList<SubCollateralType>();
        collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
        mortgageTypeViewList = mortgageTypeTransform.transformToView(mortgageTypeDAO.findAll());
        relateWithList = new ArrayList<ProposeCollateralInfoSubView>();
        // ================================================== //

        // ========== Follow Up Condition ========== //
        decisionFollowConditionView = new DecisionFollowConditionView();
        followConditionViewList = followConditionTransform.transformToView(followConditionDAO.findAll());

        // ========== Check Request Pricing =========== //
        requestPricing = fullApplicationControl.getRequestPricing(workCaseId);

        // ========== Approval History Endorse CA ========== //
        approvalHistoryView = decisionControl.getCurrentApprovalHistory(workCaseId, ApprovalType.CA_APPROVAL.value(), stepId);

        if(requestPricing){
            approvalHistoryPricingView = decisionControl.getCurrentApprovalHistory(workCaseId, ApprovalType.PRICING_APPROVAL.value(), stepId);
        }
    }

    public void onRetrievePricingFee() {
        if(creditFlag){
            messageHeader = msg.get("app.messageHeader.info");
            message = "Please save Decision before Retrieve Pricing/Fee.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onRetrievePricing(workCaseId, decisionView);

        int complete = (Integer) resultMapVal.get("complete"); // 1 success , 2 fail , 3 error
        String standardPricingResponse = (String) resultMapVal.get("standardPricingResponse");
        String error = (String) resultMapVal.get("error");
        decisionView = (DecisionView) resultMapVal.get("decisionView");
        if(complete == 1) {
            messageHeader = msg.get("app.messageHeader.info");
            message = "Retrieve Pricing/Fee Success";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } else if(complete == 2) {
            messageHeader = msg.get("app.messageHeader.error");
            message = standardPricingResponse;
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = error;
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    // ==================== Approve Credit - Actions ==================== //
    public void onAddApproveCredit() {
        mode = Mode.ADD;
        isModeEdit = false;

        approveCreditInfoDetailView = new ProposeCreditInfoDetailView();

        onChangeRequestType();

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onSaveApproveCredit() {
        boolean complete = false;
        if((approveCreditInfoDetailView.getProductProgramView().getId() != 0)
                && (approveCreditInfoDetailView.getCreditTypeView().getId() != 0)
                && (approveCreditInfoDetailView.getLoanPurposeView().getId() != 0)
                && (approveCreditInfoDetailView.getDisbursementTypeView().getId() != 0)) {
            Map<String, Object> resultMapVal;
            if(mode == Mode.ADD) {
                resultMapVal = proposeLineControl.onSaveCreditInfo(decisionView, approveCreditInfoDetailView, 1, rowIndex, lastSeq, hashSeqCredit);
            } else {
                resultMapVal = proposeLineControl.onSaveCreditInfo(decisionView, approveCreditInfoDetailView, 2, rowIndex, lastSeq, hashSeqCredit);
            }
            decisionView = (DecisionView) resultMapVal.get("decisionView");
            lastSeq = (Integer) resultMapVal.get("lastSeq");
            hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
            creditFlag = true;
            complete = true;
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onEditApproveCredit() {
        mode = Mode.EDIT;
        isModeEdit = true;

        onChangeRequestTypeInitialForEdit();
        onChangeProductProgram();
        onChangeCreditType();
    }

    public void onDeleteApproveCredit() {
        Map<String, Object> resultMapVal;

        resultMapVal = proposeLineControl.onDeleteCreditInfo(decisionView, approveCreditInfoDetailView, hashSeqCredit);

        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            decisionView = (DecisionView) resultMapVal.get("decisionView");
            creditFlag = (Boolean) resultMapVal.get("creditFlag");
        } else {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.error.delete.credit");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeRequestTypeInitialForEdit() {
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onChangeRequestType(approveCreditInfoDetailView, productGroup, 1);
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        productProgramViewList = (List<PrdGroupToPrdProgramView>) resultMapVal.get("productProgramViewList");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeRequestType() {
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onChangeRequestType(approveCreditInfoDetailView, productGroup, 2);
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        productProgramViewList = (List<PrdGroupToPrdProgramView>) resultMapVal.get("productProgramViewList");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeProductProgram() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD){
            resultMapVal = proposeLineControl.onChangeProductProgram(approveCreditInfoDetailView, 1);
        } else {
            resultMapVal = proposeLineControl.onChangeProductProgram(approveCreditInfoDetailView, 2);
        }
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeCreditType() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD){
            resultMapVal = proposeLineControl.onChangeCreditType(decisionView, approveCreditInfoDetailView, specialProgramId, applyTCG, 1);
        } else {
            resultMapVal = proposeLineControl.onChangeCreditType(decisionView, approveCreditInfoDetailView, specialProgramId, applyTCG, 2);
        }
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
    }

    public void onAddTierInfo() {
        Map<String, Object> resultMapVal = proposeLineControl.onAddCreditTier(approveCreditInfoDetailView);
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
    }

    public void onDeleteTierInfo(int rowIndex) {
        Map<String, Object> resultMapVal = proposeLineControl.onDeleteProposeTierInfo(approveCreditInfoDetailView, rowIndex);
        approveCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
    }

    public void onChangeBaseRate() {
        Map<String, Object> resultMapVal = proposeLineControl.onChangeBaseRate(approveCreditInfoDetailView.getSuggestBaseRate(), baseRateList);
        approveCreditInfoDetailView.setSuggestBaseRate((BaseRateView) resultMapVal.get("baseRateView"));
    }

    public void onChangeBaseRate(int rowIndex) {
        Map<String, Object> resultMapVal = proposeLineControl.onChangeBaseRate(approveCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).getFinalBasePrice(), baseRateList);
        approveCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).setFinalBasePrice((BaseRateView) resultMapVal.get("baseRateView"));
    }

    public void onCheckNoFlag(ProposeCreditInfoDetailView proposeCreditInfoDetailView){
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onCheckNoFlag(proposeCreditInfoDetailView, hashSeqCredit, hashSeqCreditTmp);
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");
    }

    // ==================== Approve Collateral - Actions ==================== //
    public void onEditApproveCollateral() {
        mode = Mode.EDIT;
        isModeEdit = true;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        if(workCaseRequestAppraisal) {
            headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findByAppraisal(0));
        } else {
            headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());
        }

        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(decisionView.getApproveCreditList(), decisionView.getExtBorrowerComCreditList());

        relateWithList = proposeLineControl.getRelateWithList(decisionView.getApproveCollateralList());

        Map<String, Object> resultMapVal = proposeLineControl.onEditCollateralInfo(approveCollateralInfoView, proposeCreditViewList);

        approveCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");

        Cloner cloner = new Cloner();
        approveCollateralInfoViewTmp = cloner.deepClone(approveCollateralInfoView);
    }

    public void onSaveProposeCollInfo() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveCollateralInfo(decisionView, approveCollateralInfoView, potentialCollViewList, headCollTypeViewList, hashSeqCredit, proposeCreditViewList, 1, rowIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveCollateralInfo(decisionView, approveCollateralInfoView, potentialCollViewList, headCollTypeViewList, hashSeqCredit, proposeCreditViewList, 2, rowIndex);
        }

        boolean notCheckNoFlag = (Boolean) resultMapVal.get("notCheckNoFlag");
        if(notCheckNoFlag) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.data");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        boolean notHaveSub = (Boolean) resultMapVal.get("notHaveSub");
        if(notHaveSub) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.sub.data");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        decisionView = (DecisionView) resultMapVal.get("decisionView");
        approveCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteApproveCollateral() {
        Map<String, Object> resultMapVal = proposeLineControl.onDeleteCollateralInfo(decisionView, approveCollateralInfoView, hashSeqCredit);
        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            decisionView = (DecisionView) resultMapVal.get("decisionView");
            hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        } else {
            messageHeader = msg.get("app.propose.exception");
            message = msg.get("app.propose.error.delete.coll.relate");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelCollateralInfo(){
        Map<String, Object> resultMapVal = proposeLineControl.onCancelCollateralAndGuarantor(proposeCreditViewList, hashSeqCredit, hashSeqCreditTmp);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");

        if(mode == Mode.EDIT) {
            decisionView.getApproveCollateralList().set(rowIndex, approveCollateralInfoViewTmp);
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onChangePotentialCollateralType(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView) {
        Map<String, Object> resultMapVal = proposeLineControl.onChangePotentialCollateralType(proposeCollateralInfoHeadView);
        proposeCollateralInfoHeadView = (ProposeCollateralInfoHeadView) resultMapVal.get("proposeCollateralInfoHeadView");
    }

    public void onAddSubCollateral() {
        collOwnerId = 0L;
        mortgageTypeId = 0;
        relateWithSubId = "";

        int headCollTypeId = approveCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowIndex).getHeadCollType().getId();
        if(!Util.isZero(headCollTypeId)) {
            modeSubColl = Mode.ADD;

            approveCollateralInfoSubView = new ProposeCollateralInfoSubView();

            subCollateralTypeList = subCollateralTypeDAO.findByCollateralTypeId(headCollTypeId);

            RequestContext.getCurrentInstance().execute("subCollateralInfoDlg.show()");
        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = "Please to choose Head Collateral Type";
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSaveSubCollateral() {
        Map<String, Object> resultMapVal;
        if(modeSubColl == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveSubCollateralInfo(approveCollateralInfoView, approveCollateralInfoSubView, relateWithList, subCollateralTypeList, 1, rowHeadCollIndex, rowSubCollIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveSubCollateralInfo(approveCollateralInfoView, approveCollateralInfoSubView, relateWithList, subCollateralTypeList, 2, rowHeadCollIndex, rowSubCollIndex);
        }
        approveCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        relateWithList = (List<ProposeCollateralInfoSubView>) resultMapVal.get("relateWithList");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteSubCollateral() {
        Map<String, Object> resultMapVal;

        resultMapVal = proposeLineControl.onDeleteSubCollateralInfo(decisionView, approveCollateralInfoView, approveCollateralInfoSubView, relateWithList, rowHeadCollIndex);

        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            approveCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
            relateWithList = (List<ProposeCollateralInfoSubView>) resultMapVal.get("relateWithList");
        } else {
            messageHeader = msg.get("app.propose.exception");
            message = msg.get("app.propose.error.delete.coll.relate");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onEditSubCollateral() {
        collOwnerId = 0L;
        mortgageTypeId = 0;
        relateWithSubId = "";

        modeSubColl = Mode.EDIT;

        if(!Util.isNull(relateWithList) && !Util.isZero(relateWithList.size())){
            for(ProposeCollateralInfoSubView proCollInfSubView : relateWithList){
                if(proCollInfSubView.getSubId().equalsIgnoreCase(approveCollateralInfoSubView.getSubId())){
                    relateWithList.remove(proCollInfSubView);
                    break;
                }
            }
        }
    }

    public void onCancelSubCollateralInfo() {
        relateWithList.add(approveCollateralInfoSubView);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onAddCollateralOwnerUW() {
        if(!Util.isZero(collOwnerId)) {
            if(!Util.isNull(approveCollateralInfoSubView.getCollateralOwnerUWList()) && !Util.isZero(approveCollateralInfoSubView.getCollateralOwnerUWList().size())) {
                for(CustomerInfoView customerInfoView : approveCollateralInfoSubView.getCollateralOwnerUWList()) {
                    if(collOwnerId == customerInfoView.getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Customer owner !";
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            approveCollateralInfoSubView.getCollateralOwnerUWList().add(proposeLineControl.getCustomerViewFromList(collOwnerId, collateralOwnerUwAllList));
        }
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        approveCollateralInfoSubView.getCollateralOwnerUWList().remove(rowIndex);
    }

    public void onAddMortgageType() {
        if(!Util.isZero(mortgageTypeId)) {
            if(!Util.isNull(approveCollateralInfoSubView.getMortgageList()) && !Util.isZero(approveCollateralInfoSubView.getMortgageList().size())) {
                for(MortgageTypeView mortgageTypeView : approveCollateralInfoSubView.getMortgageList()) {
                    if(mortgageTypeId == mortgageTypeView.getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Mortgage type !";
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            approveCollateralInfoSubView.getMortgageList().add(proposeLineControl.getMortgageTypeById(mortgageTypeId, mortgageTypeViewList));
        }
    }

    public void onDeleteMortgageType(int rowIndex) {
        approveCollateralInfoSubView.getMortgageList().remove(rowIndex);
    }

    public void onAddRelatedWith() {
        if(!Util.isNull(relateWithSubId) && !Util.isEmpty(relateWithSubId)) {
            if(!Util.isNull(approveCollateralInfoSubView.getRelatedWithList()) && !Util.isZero(approveCollateralInfoSubView.getRelatedWithList().size())) {
                for(ProposeCollateralInfoSubView relateWith : approveCollateralInfoSubView.getRelatedWithList()) {
                    if(relateWithSubId.equalsIgnoreCase(relateWith.getSubId())) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Related !";
                        severity = MessageDialogSeverity.ALERT.severity();
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            approveCollateralInfoSubView.getRelatedWithList().add(proposeLineControl.getRelateWithBySubId(relateWithSubId, relateWithList));
        }
    }

    public void onDeleteRelatedWith(int rowIndex) {
        approveCollateralInfoSubView.getRelatedWithList().remove(rowIndex);
    }

    // ==================== Approve Guarantor - Actions ==================== //
    public void onAddApproveGuarantor() {
        mode = Mode.ADD;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        approveGuarantorInfoView = new ProposeGuarantorInfoView();

        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(decisionView.getApproveCreditList(), decisionView.getExtBorrowerComCreditList());

        RequestContext.getCurrentInstance().execute("proposeGuarantorDlg.show()");
    }

    public void onEditApproveGuarantor() {
        mode = Mode.EDIT;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(decisionView.getApproveCreditList(), decisionView.getExtBorrowerComCreditList());

        Map<String, Object> resultMapVal = proposeLineControl.onEditGuarantorInfo(approveGuarantorInfoView, proposeCreditViewList);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");
    }

    public void onSaveApproveGuarantor() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveGuarantorInfo(decisionView, approveGuarantorInfoView, hashSeqCredit, proposeCreditViewList, guarantorList, 1, rowIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveGuarantorInfo(decisionView, approveGuarantorInfoView, hashSeqCredit, proposeCreditViewList, guarantorList, 2, rowIndex);
        }
        boolean notCheckNoFlag = (Boolean) resultMapVal.get("notCheckNoFlag");
        if(notCheckNoFlag) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.data");
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }
        decisionView = (DecisionView) resultMapVal.get("decisionView");
        approveGuarantorInfoView = (ProposeGuarantorInfoView) resultMapVal.get("proposeGuarantorInfoView");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteApproveGuarantor() {
        Map<String, Object> resultMapVal = proposeLineControl.onDeleteGuarantorInfo(decisionView, approveGuarantorInfoView, hashSeqCredit);
        decisionView = (DecisionView) resultMapVal.get("decisionView");
    }

    public void onCancelGuarantorInfo(){
        Map<String, Object> resultMapVal = proposeLineControl.onCancelCollateralAndGuarantor(proposeCreditViewList, hashSeqCredit, hashSeqCreditTmp);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    // ---------- FollowUp Condition - Action ---------- //
    public void onAddFollowUpCondition() {
        log.debug("onAddFollowUpCondition()");
        if (decisionView.getDecisionFollowConditionViewList() == null) {
            decisionView.setDecisionFollowConditionViewList(new ArrayList<DecisionFollowConditionView>());
        }
        // Validate add duplicate condition
        if (decisionView.getDecisionFollowConditionViewList().size() > 0) {
            for (DecisionFollowConditionView followConditionView : decisionView.getDecisionFollowConditionViewList()) {
                if (followConditionView.getConditionView().getId() == decisionFollowConditionView.getConditionView().getId()) {
                    messageHeader = msg.get("app.messageHeader.error");
                    message = "Can not add duplicate Condition!";
                    severity = MessageDialogSeverity.ALERT.severity();
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        DecisionFollowConditionView addNewDecisionFollowCondition = new DecisionFollowConditionView();
        addNewDecisionFollowCondition.setConditionView(getFollowConditionById(decisionFollowConditionView.getConditionView().getId()));
        addNewDecisionFollowCondition.setDetail(decisionFollowConditionView.getDetail());
        addNewDecisionFollowCondition.setFollowDate(decisionFollowConditionView.getFollowDate());
        decisionView.getDecisionFollowConditionViewList().add(addNewDecisionFollowCondition);

        // Clear form
        decisionFollowConditionView = new DecisionFollowConditionView();
    }

    public void onDeleteFollowUpCondition() {
        log.debug("onDeleteFollowUpCondition() rowIndexDecisionFollowCondition: {}", rowIndexDecisionFollowCondition);
        // keep exist id from DB for delete on save decision
        if (decisionView.getDecisionFollowConditionViewList().get(rowIndexDecisionFollowCondition).getId() != 0) {
            decisionView.getDeleteFollowConditionIdList().add(decisionView.getDecisionFollowConditionViewList().get(rowIndexDecisionFollowCondition).getId());
        }
        decisionView.getDecisionFollowConditionViewList().remove(rowIndexDecisionFollowCondition);
    }

    // ---------- Decision - Action ---------- //
    public void onSaveDecision() {
        log.debug("onSaveDecision");
        try {
            log.debug("roleId :: {}", roleId);
            if (roleId == RoleValue.UW.id()) {
                log.debug("Save Decision Role UW");
                // Delete List
                //decisionControl.deleteAllApproveByIdList(deleteCreditIdList, deleteCollIdList, deleteGuarantorIdList, deleteConditionIdList);
                // Save All Approve (Credit, Collateral, Guarantor) and Follow up Condition
                decisionControl.saveApproveAndCondition(decisionView, workCaseId, hashSeqCredit);
                // Calculate Total Approve
                decisionControl.calculateTotalApprove(decisionView);
                // Save Total Approve to Decision
                decisionControl.saveDecision(decisionView, workCase);

                exSummaryControl.calForDecision(workCaseId);

                fullApplicationControl.calculateApprovedPricingDOA(workCase.getId(), ProposeType.A);
            }

            //Check valid step to Save Approval
            HttpSession session = FacesUtil.getSession(true);
            long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
            long statusId = Util.parseLong(session.getAttribute("statusId"), 0);

            HashMap<String, Integer> stepStatusMap = stepStatusControl.getStepStatusByStepStatusRole(stepId, statusId);

            if(stepStatusMap != null){
                if(stepStatusMap.containsKey("Submit CA") || stepStatusMap.containsKey("Submit to UW1")
                        || stepStatusMap.containsKey("Submit to UW2") || stepStatusMap.containsKey("Submit to ZM")){
                    if(decisionDialog){
                        // Save Approval History
                        if(endorseDecisionDialog){
                            approvalHistoryView = decisionControl.saveApprovalHistory(approvalHistoryView, workCase);
                        }

                        if(requestPricing && pricingDecisionDialog){
                            // Save Approval History Pricing
                            if(roleId != RoleValue.UW.id()) {
                                approvalHistoryPricingView = decisionControl.saveApprovalHistoryPricing(approvalHistoryPricingView, workCase);
                            }
                        }
                    }
                }
            }

            onCreation();

            messageHeader = msg.get("app.messageHeader.info");
            message = "Save Decision data success.";
            severity = MessageDialogSeverity.INFO.severity();
        } catch (Exception e) {
            log.debug("", e);
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            message = "Save Decision data failed. Cause : " + Util.getMessageException(e);
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    // ----------------------------------------------- get Item from Select List ----------------------------------------------- //
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

    public List<PotentialCollateralView> getPotentialCollViewList() {
        return potentialCollViewList;
    }

    public void setPotentialCollViewList(List<PotentialCollateralView> potentialCollViewList) {
        this.potentialCollViewList = potentialCollViewList;
    }

    public List<CollateralTypeView> getHeadCollTypeViewList() {
        return headCollTypeViewList;
    }

    public void setHeadCollTypeViewList(List<CollateralTypeView> headCollTypeViewList) {
        this.headCollTypeViewList = headCollTypeViewList;
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

    public List<LoanPurposeView> getLoanPurposeViewList() {
        return loanPurposeViewList;
    }

    public void setLoanPurposeViewList(List<LoanPurposeView> loanPurposeViewList) {
        this.loanPurposeViewList = loanPurposeViewList;
    }

    public int getRowIndexDecisionFollowCondition() {
        return rowIndexDecisionFollowCondition;
    }

    public void setRowIndexDecisionFollowCondition(int rowIndexDecisionFollowCondition) {
        this.rowIndexDecisionFollowCondition = rowIndexDecisionFollowCondition;
    }

    public List<FollowConditionView> getFollowConditionViewList() {
        return followConditionViewList;
    }

    public void setFollowConditionViewList(List<FollowConditionView> followConditionViewList) {
        this.followConditionViewList = followConditionViewList;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public ApprovalHistoryView getApprovalHistoryPricingView() {
        return approvalHistoryPricingView;
    }

    public void setApprovalHistoryPricingView(ApprovalHistoryView approvalHistoryPricingView) {
        this.approvalHistoryPricingView = approvalHistoryPricingView;
    }

    public boolean isRequestPricing() {
        return requestPricing;
    }

    public void setRequestPricing(boolean requestPricing) {
        this.requestPricing = requestPricing;
    }

    public boolean isDecisionDialog() {
        return decisionDialog;
    }

    public void setDecisionDialog(boolean decisionDialog) {
        this.decisionDialog = decisionDialog;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isPricingDecisionDialog() {
        return pricingDecisionDialog;
    }

    public void setPricingDecisionDialog(boolean pricingDecisionDialog) {
        this.pricingDecisionDialog = pricingDecisionDialog;
    }

    public boolean isEndorseDecisionDialog() {
        return endorseDecisionDialog;
    }

    public void setEndorseDecisionDialog(boolean endorseDecisionDialog) {
        this.endorseDecisionDialog = endorseDecisionDialog;
    }

    public ProposeCreditInfoDetailView getApproveCreditInfoDetailView() {
        return approveCreditInfoDetailView;
    }

    public void setApproveCreditInfoDetailView(ProposeCreditInfoDetailView approveCreditInfoDetailView) {
        this.approveCreditInfoDetailView = approveCreditInfoDetailView;
    }

    public List<PrdProgramToCreditTypeView> getCreditTypeViewList() {
        return creditTypeViewList;
    }

    public void setCreditTypeViewList(List<PrdProgramToCreditTypeView> creditTypeViewList) {
        this.creditTypeViewList = creditTypeViewList;
    }

    public List<PrdGroupToPrdProgramView> getProductProgramViewList() {

        return productProgramViewList;
    }

    public void setProductProgramViewList(List<PrdGroupToPrdProgramView> productProgramViewList) {
        this.productProgramViewList = productProgramViewList;
    }

    public boolean isModeEdit() {
        return isModeEdit;
    }

    public void setModeEdit(boolean modeEdit) {
        isModeEdit = modeEdit;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public ProposeGuarantorInfoView getApproveGuarantorInfoView() {
        return approveGuarantorInfoView;
    }

    public void setApproveGuarantorInfoView(ProposeGuarantorInfoView approveGuarantorInfoView) {
        this.approveGuarantorInfoView = approveGuarantorInfoView;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditViewList() {
        return proposeCreditViewList;
    }

    public void setProposeCreditViewList(List<ProposeCreditInfoDetailView> proposeCreditViewList) {
        this.proposeCreditViewList = proposeCreditViewList;
    }

    public ProposeCollateralInfoView getApproveCollateralInfoView() {
        return approveCollateralInfoView;
    }

    public void setApproveCollateralInfoView(ProposeCollateralInfoView approveCollateralInfoView) {
        this.approveCollateralInfoView = approveCollateralInfoView;
    }

    public ProposeCollateralInfoSubView getApproveCollateralInfoSubView() {
        return approveCollateralInfoSubView;
    }

    public void setApproveCollateralInfoSubView(ProposeCollateralInfoSubView approveCollateralInfoSubView) {
        this.approveCollateralInfoSubView = approveCollateralInfoSubView;
    }

    public List<SubCollateralType> getSubCollateralTypeList() {
        return subCollateralTypeList;
    }

    public void setSubCollateralTypeList(List<SubCollateralType> subCollateralTypeList) {
        this.subCollateralTypeList = subCollateralTypeList;
    }

    public List<ProposeCollateralInfoSubView> getRelateWithList() {
        return relateWithList;
    }

    public void setRelateWithList(List<ProposeCollateralInfoSubView> relateWithList) {
        this.relateWithList = relateWithList;
    }

    public Long getCollOwnerId() {
        return collOwnerId;
    }

    public void setCollOwnerId(Long collOwnerId) {
        this.collOwnerId = collOwnerId;
    }

    public int getMortgageTypeId() {
        return mortgageTypeId;
    }

    public void setMortgageTypeId(int mortgageTypeId) {
        this.mortgageTypeId = mortgageTypeId;
    }

    public String getRelateWithSubId() {
        return relateWithSubId;
    }

    public void setRelateWithSubId(String relateWithSubId) {
        this.relateWithSubId = relateWithSubId;
    }

    public int getRowSubCollIndex() {
        return rowSubCollIndex;
    }

    public void setRowSubCollIndex(int rowSubCollIndex) {
        this.rowSubCollIndex = rowSubCollIndex;
    }

    public int getRowHeadCollIndex() {
        return rowHeadCollIndex;
    }

    public void setRowHeadCollIndex(int rowHeadCollIndex) {
        this.rowHeadCollIndex = rowHeadCollIndex;
    }

    public boolean isCreditFlag() {
        return creditFlag;
    }

    public void setCreditFlag(boolean creditFlag) {
        this.creditFlag = creditFlag;
    }

    public boolean isWorkCaseRequestAppraisal() {
        return workCaseRequestAppraisal;
    }

    public void setWorkCaseRequestAppraisal(boolean workCaseRequestAppraisal) {
        this.workCaseRequestAppraisal = workCaseRequestAppraisal;
    }
}