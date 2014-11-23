package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.UsagesView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.CollateralTypeTransform;
import com.clevel.selos.transform.CreditRequestTypeTransform;
import com.clevel.selos.transform.MortgageTypeTransform;
import com.clevel.selos.transform.PotentialCollateralTransform;
import com.clevel.selos.transform.master.CountryTransform;
import com.clevel.selos.transform.master.SpecialProgramTransform;
import com.clevel.selos.transform.master.UsagesTransform;
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
@ManagedBean(name = "proposeLine")
public class ProposeLine extends BaseController {
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

    @Inject
    private SLOSAuditor slosAuditor;

    @Inject
    private ProposeLineControl proposeLineControl;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private TCGInfoControl tcgInfoControl;
    @Inject
    private LoanPurposeControl loanPurposeControl;
    @Inject
    private DisbursementTypeControl disbursementTypeControl;
    @Inject
    private CalculationControl calculationControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private FullApplicationControl fullApplicationControl;

    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private SpecialProgramTransform specialProgramTransform;
    @Inject
    private PotentialCollateralTransform potentialCollateralTransform;
    @Inject
    private CollateralTypeTransform collateralTypeTransform;
    @Inject
    private MortgageTypeTransform mortgageTypeTransform;
    @Inject
    private UsagesTransform usagesTransform;

    @Inject
    private CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private BaseRateDAO baseRateDAO;
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private MortgageTypeDAO mortgageTypeDAO;
    @Inject
    private UsagesDAO usagesDAO;

    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;

    //////////////////////////////////////////////////////////////////////

    private long workCaseId;
    private User user;
    private ProposeLineView proposeLineView;

    //Credit Info Dialog
    private ProposeCreditInfoDetailView proposeCreditInfoDetailView;
    private ProductGroup productGroup;
    //Drop Down Credit Info Dialog
    private List<PrdGroupToPrdProgramView> productProgramViewList;
    private List<PrdProgramToCreditTypeView> creditTypeViewList;
    private List<LoanPurposeView> loanPurposeViewList;
    private List<DisbursementTypeView> disbursementTypeViewList;
    private List<BaseRate> baseRateList;
    //Static Value for Credit Info Dialog
    private int applyTCG;
    private int specialProgramId;

    //Condition Info Dialog
    private ProposeConditionInfoView proposeConditionInfoView;

    //Guarantor Info Dialog
    private ProposeGuarantorInfoView proposeGuarantorInfoView;
    //Drop Down Guarantor Info Dialog
    private List<CustomerInfoView> customerInfoViewList;

    //Drop Down Guarantor & Collateral Dialog
    private List<ProposeCreditInfoDetailView> proposeCreditViewList;

    //Collateral Info Dialog
    private ProposeCollateralInfoView proposeCollateralInfoView;
    private ProposeCollateralInfoView proposeCollateralInfoViewTmp;
    //Drop Down Collateral Info Dialog
    private List<PotentialCollateralView> potentialCollViewList;
    private List<CollateralTypeView> headCollTypeViewList;

    //Sub Collateral Info Dialog
    private ProposeCollateralInfoSubView proposeCollateralInfoSubView;
    //Drop Down Sub Collateral Info Dialog
    private List<SubCollateralType> subCollateralTypeList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageTypeView> mortgageTypeViewList;
    private List<ProposeCollateralInfoSubView> relateWithList;
    //Value for Add List
    private Long collOwnerId;
    private int mortgageTypeId;
    private String relateWithSubId;

    //Drop Down Main Page
    private List<CreditRequestTypeView> creditRequestTypeViewList;
    private List<CountryView> countryViewList;

    //Message Dialog
    private String messageHeader;
    private String message;
    private String severity;

    //For All
    private int rowIndex;
    private int rowHeadCollIndex;
    private int rowSubCollIndex; // head click on sub
    private boolean creditFlag; // for retrieve
    private int lastSeq;
    private Hashtable hashSeqCredit; // seq , usage
    private Hashtable hashSeqCreditTmp;

    //Mode
    private boolean isModeEdit;
    enum Mode {ADD, EDIT}
    private Mode mode;
    private Mode modeSubColl;

    //UsagesList
    private List<UsagesView> usagesViewList;

    private String userId;

    public ProposeLine(){
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
        log.debug("onCreation");
        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        if(checkSession(session)) {
            workCaseId = (Long)session.getAttribute("workCaseId");
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.CREDIT_FACILITY_PROPOSE, ownerCaseUserId);

            proposeLineView = proposeLineControl.findProposeLineViewByWorkCaseId(workCaseId);

            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if (!Util.isNull(workCase) && !Util.isZero(workCase.getId())) {
                productGroup = workCase.getProductGroup();
            }

            BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
            if (basicInfoView != null) {
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

            // Credit
            proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();

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

            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = proposeLineView.getProposeCreditInfoDetailViewList();
            if(!Util.isNull(proposeCreditInfoDetailViewList) && !Util.isZero(proposeCreditInfoDetailViewList.size())){
                for(ProposeCreditInfoDetailView proCredit : proposeCreditInfoDetailViewList){
                    hashSeqCredit.put(proCredit.getSeq(),proCredit.getUseCount());
                }
            }

            lastSeq = proposeLineControl.getLastSeqNumberFromProposeCredit(proposeCreditInfoDetailViewList);

            // Condition
            proposeConditionInfoView = new ProposeConditionInfoView();

            // Guarantor
            proposeGuarantorInfoView = new ProposeGuarantorInfoView();
            customerInfoViewList = proposeLineControl.getGuarantorByWorkCaseId(workCaseId);
            CustomerInfoView customerInfoView = new CustomerInfoView();
            customerInfoView.setId(-1);
            customerInfoView.setFirstNameTh(msg.get("app.select.tcg"));
            customerInfoViewList.add(customerInfoView);

            proposeCreditViewList = new ArrayList<ProposeCreditInfoDetailView>();

            //disable retrieve pricing button
            if(!Util.isNull(proposeLineView) && !Util.isNull(proposeLineView.getProposeCreditInfoDetailViewList())
                    && Util.isZero(proposeLineView.getProposeCreditInfoDetailViewList().size())) { // list not null but size = 0
                setDisabledValue("retrieveProposeCreditButton",true);
            } else if(Util.isNull(proposeLineView.getProposeCreditInfoDetailViewList())) { // list is null
                setDisabledValue("retrieveProposeCreditButton",true);
            }

            //Collateral
            proposeCollateralInfoView = new ProposeCollateralInfoView();
            proposeCollateralInfoViewTmp = new ProposeCollateralInfoView();
            potentialCollViewList = potentialCollateralTransform.transformToView(potentialCollateralDAO.findAll());

            headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());

            //Sub Collateral
            proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
            subCollateralTypeList = new ArrayList<SubCollateralType>();
            collateralOwnerUwAllList = customerInfoControl.getCollateralOwnerUWByWorkCase(workCaseId);
            mortgageTypeViewList = mortgageTypeTransform.transformToView(mortgageTypeDAO.findAll());
            relateWithList = new ArrayList<ProposeCollateralInfoSubView>();

            //UsagesList
            usagesViewList = usagesTransform.transformToViewList(usagesDAO.findActiveAll());

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    ///////////////////////////////////////////////// Credit Info /////////////////////////////////////////////////

    public void onRetrievePricingFee() {
        Date date = new Date();
        if(creditFlag){
            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "Retrieve Pricing", date, ActionResult.FAILED, "Retrieve Pricing failed cause do not save Propose Line");

            messageHeader = msg.get("app.messageHeader.info");
            message = "Please save Propose Line before Retrieve Pricing/Fee.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onRetrievePricing(workCaseId, proposeLineView);

        int complete = (Integer) resultMapVal.get("complete"); // 1 success , 2 fail , 3 error
        String standardPricingResponse = (String) resultMapVal.get("standardPricingResponse");
        String error = (String) resultMapVal.get("error");
        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
        if(complete == 1) {
            messageHeader = msg.get("app.messageHeader.info");
            message = "Retrieve Pricing/Fee Success";
            severity = MessageDialogSeverity.INFO.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "Retrieve Pricing", date, ActionResult.SUCCESS, "");
        } else if(complete == 2) {
            messageHeader = msg.get("app.messageHeader.error");
            message = standardPricingResponse;
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "Retrieve Pricing", date, ActionResult.FAILED, "Error Standard Response :: "+standardPricingResponse);
        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = error;
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "Retrieve Pricing", date, ActionResult.FAILED, "Error BRMS :: "+error);
        }

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onAddCreditInfo() {
        Date date = new Date();

        mode = Mode.ADD;
        isModeEdit = false;

        proposeCreditInfoDetailView = new ProposeCreditInfoDetailView();

        onChangeRequestType();

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Credit Information Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().execute("creditInfoDlg.show()");
    }

    public void onSaveCreditInfo() {
        Date date = new Date();

        boolean complete = false;
        if((proposeCreditInfoDetailView.getProductProgramView().getId() != 0)
                && (proposeCreditInfoDetailView.getCreditTypeView().getId() != 0)
                && (proposeCreditInfoDetailView.getLoanPurposeView().getId() != 0)
                && (proposeCreditInfoDetailView.getDisbursementTypeView().getId() != 0)) {
            Map<String, Object> resultMapVal;
            if(mode == Mode.ADD) {
                log.debug("onSaveCreditInfo :: Mode Add Before lastSeq :: {}", lastSeq);
                resultMapVal = proposeLineControl.onSaveCreditInfo(proposeLineView, proposeCreditInfoDetailView, 1, rowIndex, lastSeq, hashSeqCredit);
            } else {
                log.debug("onSaveCreditInfo :: Mode Edit Before lastSeq :: {}", lastSeq);
                resultMapVal = proposeLineControl.onSaveCreditInfo(proposeLineView, proposeCreditInfoDetailView, 2, rowIndex, lastSeq, hashSeqCredit);
            }
            proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
            lastSeq = (Integer) resultMapVal.get("lastSeq");
            log.debug("onSaveCreditInfo :: After lastSeq :: {}", lastSeq);
            hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
            creditFlag = true;
            complete = true;

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Credit Information Dialog", date, ActionResult.SUCCESS, "");
        }
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
    }

    public void onEditCreditInfo() {
        Date date = new Date();

        mode = Mode.EDIT;
        isModeEdit = true;

        onChangeRequestTypeInitialForEdit();
        onChangeProductProgram();
        onChangeCreditType();

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_EDIT, "On Edit Credit Information Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onDeleteCreditInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal;

        resultMapVal = proposeLineControl.onDeleteCreditInfo(proposeLineView, proposeCreditInfoDetailView, hashSeqCredit);

        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
            creditFlag = (Boolean) resultMapVal.get("creditFlag");

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Credit Information Dialog", date, ActionResult.SUCCESS, "");
        } else {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.error.delete.credit");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_EDIT, "On Delete Credit Information Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelCreditInfo() {
        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Credit Information Dialog", new Date(), ActionResult.SUCCESS, "");

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
    }

    public void onChangeRequestTypeInitialForEdit() {
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onChangeRequestType(proposeCreditInfoDetailView, productGroup, 1);
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        productProgramViewList = (List<PrdGroupToPrdProgramView>) resultMapVal.get("productProgramViewList");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeRequestType() {
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onChangeRequestType(proposeCreditInfoDetailView, productGroup, 2);
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        productProgramViewList = (List<PrdGroupToPrdProgramView>) resultMapVal.get("productProgramViewList");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeProductProgram() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD){
             resultMapVal = proposeLineControl.onChangeProductProgram(proposeCreditInfoDetailView, 1);
        } else {
            resultMapVal = proposeLineControl.onChangeProductProgram(proposeCreditInfoDetailView, 2);
        }
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
        creditTypeViewList = (List<PrdProgramToCreditTypeView>) resultMapVal.get("creditTypeViewList");
    }

    public void onChangeCreditType() {
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD){
            resultMapVal = proposeLineControl.onChangeCreditType(proposeLineView, proposeCreditInfoDetailView, specialProgramId, applyTCG, 1);
        } else {
            resultMapVal = proposeLineControl.onChangeCreditType(proposeLineView, proposeCreditInfoDetailView, specialProgramId, applyTCG, 2);
        }
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");
    }

    public void onAddTierInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onAddCreditTier(proposeCreditInfoDetailView);
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Tier", date, ActionResult.SUCCESS, "");
    }

    public void onDeleteProposeTierInfo(int rowIndex) {
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onDeleteProposeTierInfo(proposeCreditInfoDetailView, rowIndex);
        proposeCreditInfoDetailView = (ProposeCreditInfoDetailView) resultMapVal.get("proposeCreditInfoDetailView");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Tier", date, ActionResult.SUCCESS, "");
    }

    public void onChangeBaseRate() {
        Map<String, Object> resultMapVal = proposeLineControl.onChangeBaseRate(proposeCreditInfoDetailView.getSuggestBaseRate(), baseRateList);
        proposeCreditInfoDetailView.setSuggestBaseRate((BaseRateView) resultMapVal.get("baseRateView"));
    }

    public void onChangeBaseRate(int rowIndex) {
        Map<String, Object> resultMapVal = proposeLineControl.onChangeBaseRate(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).getFinalBasePrice(), baseRateList);
        proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().get(rowIndex).setFinalBasePrice((BaseRateView) resultMapVal.get("baseRateView"));
    }

    ///////////////////////////////////////////////// Collateral Info /////////////////////////////////////////////////

    public void onAddCollateralInfo() {
        Date date = new Date();

        mode = Mode.ADD;
        isModeEdit = false;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        proposeCollateralInfoView = new ProposeCollateralInfoView();

        List<ExistingCreditDetailView> exCreDetView = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(proposeLineView.getProposeCreditInfoDetailViewList(), exCreDetView);

        relateWithList = proposeLineControl.getRelateWithList(proposeLineView.getProposeCollateralInfoViewList());

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Collateral Information Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().execute("collateralInfoDlg.show()");
    }

    public void onEditCollateralInfo() {
        Date date = new Date();

        mode = Mode.EDIT;
        isModeEdit = true;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        List<ExistingCreditDetailView> exCreDetView = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(proposeLineView.getProposeCreditInfoDetailViewList(), exCreDetView);

        relateWithList = proposeLineControl.getRelateWithList(proposeLineView.getProposeCollateralInfoViewList());

        Map<String, Object> resultMapVal = proposeLineControl.onEditCollateralInfo(proposeCollateralInfoView, proposeCreditViewList);

        proposeCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");

        Cloner cloner = new Cloner();
        proposeCollateralInfoViewTmp = cloner.deepClone(proposeCollateralInfoView);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_EDIT, "On Edit Collateral Information Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onSaveCollateralInfo() {
        Date date = new Date();
        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveCollateralInfo(proposeLineView, proposeCollateralInfoView, potentialCollViewList, headCollTypeViewList, hashSeqCredit, proposeCreditViewList, 1, rowIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveCollateralInfo(proposeLineView, proposeCollateralInfoView, potentialCollViewList, headCollTypeViewList, hashSeqCredit, proposeCreditViewList, 2, rowIndex);
        }

        boolean notCheckNoFlag = (Boolean) resultMapVal.get("notCheckNoFlag");
        if(notCheckNoFlag) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.data");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Collateral Information Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        boolean notHaveSub = (Boolean) resultMapVal.get("notHaveSub");
        if(notHaveSub) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.sub.data");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Collateral Information Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
        proposeCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Collateral Information Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteCollateralInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onDeleteCollateralInfo(proposeLineView, proposeCollateralInfoView, hashSeqCredit);
        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
            hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Collateral Information Dialog", date, ActionResult.SUCCESS, "");
        } else {
            messageHeader = msg.get("app.propose.exception");
            message = msg.get("app.propose.error.delete.coll.relate");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Collateral Information Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onCancelCollateralInfo(){
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onCancelCollateralAndGuarantor(proposeCreditViewList, hashSeqCredit, hashSeqCreditTmp);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");

        if(mode == Mode.EDIT) {
            proposeLineView.getProposeCollateralInfoViewList().set(rowIndex, proposeCollateralInfoViewTmp);
        }

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Collateral Information Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onChangePotentialCollateralType(ProposeCollateralInfoHeadView proposeCollateralInfoHeadView) {
        Map<String, Object> resultMapVal = proposeLineControl.onChangePotentialCollateralType(proposeCollateralInfoHeadView);
        proposeCollateralInfoHeadView = (ProposeCollateralInfoHeadView) resultMapVal.get("proposeCollateralInfoHeadView");
    }

    public void onRetrieveAppraisalReportInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onRetrieveAppraisalReport(proposeCollateralInfoView.getJobID(), user, proposeLineView.getProposeCollateralInfoViewList(), isModeEdit);

        headCollTypeViewList = collateralTypeTransform.transformToView(collateralTypeDAO.findAll());

        Integer completeFlag = (Integer) resultMapVal.get("completeFlag");
        String error = (String) resultMapVal.get("error");
        if(completeFlag == 1) { // success
            proposeCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.credit.facility.propose.coms.success");
            severity = MessageDialogSeverity.INFO.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "On Retrieve Appraisal", date, ActionResult.SUCCESS, "");
        } else if(completeFlag == 2) { // error
            messageHeader = msg.get("app.messageHeader.error");
            message = error;
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "On Retrieve Appraisal", date, ActionResult.FAILED, message);
        } else if(completeFlag == 3) { // dup
            messageHeader = msg.get("app.messageHeader.error");
            message = msg.get("app.credit.facility.propose.coms.err");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "On Retrieve Appraisal", date, ActionResult.FAILED, message);
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onAddSubCollateral() {
        Date date = new Date();

        collOwnerId = 0L;
        mortgageTypeId = 0;
        relateWithSubId = "";

        int headCollTypeId = proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getHeadCollType().getId();
        if(!Util.isZero(headCollTypeId)) {
            modeSubColl = Mode.ADD;

            proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();

            subCollateralTypeList = subCollateralTypeDAO.findByCollateralTypeId(headCollTypeId);

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "On Add Sub Collateral Dialog", date, ActionResult.SUCCESS, "");

            RequestContext.getCurrentInstance().execute("subCollateralInfoDlg.show()");
        } else {
            messageHeader = msg.get("app.messageHeader.error");
            message = "Please to choose Head Collateral Type";
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ACTION, "On Add Sub Collateral Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSaveSubCollateral() {
        Date date = new Date();

        log.debug("onSaveSubCollateral :: modeSubColl :: {}, rowHeadCollIndex :: {}, rowSubCollIndex :: {}, proposeCollateralInfoSubView :: {}",modeSubColl, rowHeadCollIndex, rowSubCollIndex, proposeCollateralInfoSubView);
        Map<String, Object> resultMapVal;
        if(modeSubColl == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveSubCollateralInfo(proposeCollateralInfoView, proposeCollateralInfoSubView, relateWithList, subCollateralTypeList, 1, rowHeadCollIndex, rowSubCollIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveSubCollateralInfo(proposeCollateralInfoView, proposeCollateralInfoSubView, relateWithList, subCollateralTypeList, 2, rowHeadCollIndex, rowSubCollIndex);
        }

        boolean notHaveMortgage = (Boolean) resultMapVal.get("notHaveMortgage");
        if(notHaveMortgage) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.mort.data");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Sub Collateral Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        proposeCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
        relateWithList = (List<ProposeCollateralInfoSubView>) resultMapVal.get("relateWithList");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Sub Collateral Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteSubCollateral() {
        Date date = new Date();

        Map<String, Object> resultMapVal;

        resultMapVal = proposeLineControl.onDeleteSubCollateralInfo(proposeLineView, proposeCollateralInfoView, proposeCollateralInfoSubView, relateWithList, rowHeadCollIndex);

        boolean completeFlag = (Boolean) resultMapVal.get("completeFlag");
        if(completeFlag) {
            proposeCollateralInfoView = (ProposeCollateralInfoView) resultMapVal.get("proposeCollateralInfoView");
            relateWithList = (List<ProposeCollateralInfoSubView>) resultMapVal.get("relateWithList");

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Sub Collateral Dialog", date, ActionResult.SUCCESS, "");
        } else {
            messageHeader = msg.get("app.propose.exception");
            message = msg.get("app.propose.error.delete.coll.relate");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Sub Collateral Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onEditSubCollateral() {
        Date date = new Date();
        log.debug("proposeCollateralInfoSubView :: {}", proposeCollateralInfoSubView);
        collOwnerId = 0L;
        mortgageTypeId = 0;
        relateWithSubId = "";

        modeSubColl = Mode.EDIT;

        subCollateralTypeList = subCollateralTypeDAO.findByCollateralTypeId(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().get(rowHeadCollIndex).getHeadCollType().getId());

        if(!Util.isNull(relateWithList) && !Util.isZero(relateWithList.size())){
            for(ProposeCollateralInfoSubView proCollInfSubView : relateWithList){
                if(proCollInfSubView.getSubId().equalsIgnoreCase(proposeCollateralInfoSubView.getSubId())){
                    relateWithList.remove(proCollInfSubView);
                    break;
                }
            }
        }

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_EDIT, "On Edit Sub Collateral Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onCancelSubCollateralInfo() {
        Date date = new Date();

        relateWithList.add(proposeCollateralInfoSubView);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Sub Collateral Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onAddCollateralOwnerUW() {
        Date date = new Date();
        if(!Util.isZero(collOwnerId)) {
            if(!Util.isNull(proposeCollateralInfoSubView.getCollateralOwnerUWList()) && !Util.isZero(proposeCollateralInfoSubView.getCollateralOwnerUWList().size())) {
                for(CustomerInfoView customerInfoView : proposeCollateralInfoSubView.getCollateralOwnerUWList()) {
                    if(collOwnerId == customerInfoView.getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Customer owner !";
                        severity = MessageDialogSeverity.ALERT.severity();

                        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Collateral Owner in Sub Collateral Dialog", date, ActionResult.FAILED, message);

                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            proposeCollateralInfoSubView.getCollateralOwnerUWList().add(proposeLineControl.getCustomerViewFromList(collOwnerId, collateralOwnerUwAllList));

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Collateral Owner in Sub Collateral Dialog", date, ActionResult.SUCCESS, "");
        }
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        proposeCollateralInfoSubView.getCollateralOwnerUWList().remove(rowIndex);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Collateral Owner in Sub Collateral Dialog", new Date(), ActionResult.SUCCESS, "");
    }

    public void onAddMortgageType() {
        Date date = new Date();
        if(!Util.isZero(mortgageTypeId)) {
            if(!Util.isNull(proposeCollateralInfoSubView.getMortgageList()) && !Util.isZero(proposeCollateralInfoSubView.getMortgageList().size())) {
                for(MortgageTypeView mortgageTypeView : proposeCollateralInfoSubView.getMortgageList()) {
                    if(mortgageTypeId == mortgageTypeView.getId()) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Mortgage type !";
                        severity = MessageDialogSeverity.ALERT.severity();

                        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Mortgage Type in Sub Collateral Dialog", date, ActionResult.FAILED, message);

                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            proposeCollateralInfoSubView.getMortgageList().add(proposeLineControl.getMortgageTypeById(mortgageTypeId, mortgageTypeViewList));

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Mortgage Type in Sub Collateral Dialog", date, ActionResult.SUCCESS, "");
        }
    }

    public void onDeleteMortgageType(int rowIndex) {
        proposeCollateralInfoSubView.getMortgageList().remove(rowIndex);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Mortgage Type in Sub Collateral Dialog", new Date(), ActionResult.SUCCESS, "");
    }

    public void onAddRelatedWith() {
        Date date = new Date();
        if(!Util.isNull(relateWithSubId) && !Util.isEmpty(relateWithSubId)) {
            if(!Util.isNull(proposeCollateralInfoSubView.getRelatedWithList()) && !Util.isZero(proposeCollateralInfoSubView.getRelatedWithList().size())) {
                for(ProposeCollateralInfoSubView relateWith : proposeCollateralInfoSubView.getRelatedWithList()) {
                    if(relateWithSubId.equalsIgnoreCase(relateWith.getSubId())) {
                        messageHeader = msg.get("app.messageHeader.error");
                        message = "Can not add duplicate Related !";
                        severity = MessageDialogSeverity.ALERT.severity();

                        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Related With in Sub Collateral Dialog", date, ActionResult.FAILED, message);

                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return;
                    }
                }
            }
            proposeCollateralInfoSubView.getRelatedWithList().add(proposeLineControl.getRelateWithBySubId(relateWithSubId, relateWithList));

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Related With in Sub Collateral Dialog", date, ActionResult.SUCCESS, "");
        }
    }

    public void onDeleteRelatedWith(int rowIndex) {
        proposeCollateralInfoSubView.getRelatedWithList().remove(rowIndex);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Related With in Sub Collateral Dialog", new Date(), ActionResult.SUCCESS, "");
    }

    ///////////////////////////////////////////////// Guarantor Info /////////////////////////////////////////////////

    public void onAddGuarantorInfo() {
        Date date = new Date();

        mode = Mode.ADD;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        proposeGuarantorInfoView = new ProposeGuarantorInfoView();

        List<ExistingCreditDetailView> exCreDetView = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(proposeLineView.getProposeCreditInfoDetailViewList(), exCreDetView);

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Guarantor Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().execute("guarantorInfoDlg.show()");
    }

    public void onEditGuarantorInfo() {
        Date date = new Date();

        mode = Mode.EDIT;
        hashSeqCreditTmp = new Hashtable<Integer, Integer>();

        List<ExistingCreditDetailView> exCreDetView = creditFacExistingControl.onFindBorrowerExistingCreditFacility(workCaseId);
        proposeCreditViewList = proposeLineControl.getProposeCreditFromProposeCreditAndExistingCredit(proposeLineView.getProposeCreditInfoDetailViewList(), exCreDetView);

        Map<String, Object> resultMapVal = proposeLineControl.onEditGuarantorInfo(proposeGuarantorInfoView, proposeCreditViewList);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_EDIT, "On Edit Guarantor Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onSaveGuarantorInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal;
        if(mode == Mode.ADD) {
            resultMapVal = proposeLineControl.onSaveGuarantorInfo(proposeLineView, proposeGuarantorInfoView, hashSeqCredit, proposeCreditViewList, customerInfoViewList, 1, rowIndex);
        } else {
            resultMapVal = proposeLineControl.onSaveGuarantorInfo(proposeLineView, proposeGuarantorInfoView, hashSeqCredit, proposeCreditViewList, customerInfoViewList, 2, rowIndex);
        }
        boolean notCheckNoFlag = (Boolean) resultMapVal.get("notCheckNoFlag");
        if(notCheckNoFlag) {
            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.desc.add.data");
            severity = MessageDialogSeverity.ALERT.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Guarantor Dialog", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }
        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");
        proposeGuarantorInfoView = (ProposeGuarantorInfoView) resultMapVal.get("proposeGuarantorInfoView");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Guarantor Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteGuarantorInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onDeleteGuarantorInfo(proposeLineView, proposeGuarantorInfoView, hashSeqCredit);
        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Guarantor Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onCancelGuarantorInfo(){
        Date date = new Date();

        Map<String, Object> resultMapVal = proposeLineControl.onCancelCollateralAndGuarantor(proposeCreditViewList, hashSeqCredit, hashSeqCreditTmp);

        proposeCreditViewList = (List<ProposeCreditInfoDetailView>) resultMapVal.get("proposeCreditViewList");
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Guarantor Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    ///////////////////////////////////////////////// Condition Info /////////////////////////////////////////////////

    public void onAddConditionInfo() {
        proposeConditionInfoView = new ProposeConditionInfoView();

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_ADD, "On Add Condition Information Dialog", new Date(), ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().execute("conditionDlg.show()");
    }

    public void onSaveConditionInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onSaveConditionInfo(proposeLineView, proposeConditionInfoView);
        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "On Save Condition Information Dialog", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public void onDeleteConditionInfo() {
        Date date = new Date();

        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onDeleteConditionInfo(proposeLineView, proposeConditionInfoView);
        proposeLineView = (ProposeLineView) resultMapVal.get("proposeLineView");

        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_DELETE, "On Delete Condition Information Dialog", date, ActionResult.SUCCESS, "");
    }

    public void onCancelConditionInfo() {
        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Condition Information Dialog", new Date(), ActionResult.SUCCESS, "");

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
    }

    ///////////////////////////////////////////////// Propose Line /////////////////////////////////////////////////

    public void onCheckNoFlag(ProposeCreditInfoDetailView proposeCreditInfoDetailView){
        Map<String, Object> resultMapVal;
        resultMapVal = proposeLineControl.onCheckNoFlag(proposeCreditInfoDetailView, hashSeqCredit, hashSeqCreditTmp);
        hashSeqCredit = (Hashtable) resultMapVal.get("hashSeqCredit");
        hashSeqCreditTmp = (Hashtable) resultMapVal.get("hashSeqCreditTmp");
    }

    public void onSaveProposeLine() {
        Date date = new Date();
        try {
            HttpSession session = FacesUtil.getSession(false);
            proposeLineControl.onSaveProposeLine(workCaseId, proposeLineView, ProposeType.P, hashSeqCredit);
            calculationControl.calculateTotalProposeAmount(workCaseId);
            calculationControl.calculateMaximumSMELimit(workCaseId);
            calculationControl.calWC(workCaseId);
            calculationControl.calForProposeLine(workCaseId);
            calculationControl.calculateFinalDBR(workCaseId);
            fullApplicationControl.calculateApprovedPricingDOA(workCaseId, ProposeType.P, getCurrentStep(session));

            onCreation();

            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.propose.response.save.success");
            severity = MessageDialogSeverity.INFO.severity();

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, "");
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.messageHeader.error");
            severity = MessageDialogSeverity.ALERT.severity();
            message = msg.get("app.propose.response.save.failed") + " cause : " + Util.getMessageException(ex);

            slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, message);
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancelProposeLine() {
        slosAuditor.add(Screen.CREDIT_FACILITY_PROPOSE.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");
        onCreation();
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public ProposeLineView getProposeLineView() {
        return proposeLineView;
    }

    public void setProposeLineView(ProposeLineView proposeLineView) {
        this.proposeLineView = proposeLineView;
    }

    public ProposeCreditInfoDetailView getProposeCreditInfoDetailView() {
        return proposeCreditInfoDetailView;
    }

    public void setProposeCreditInfoDetailView(ProposeCreditInfoDetailView proposeCreditInfoDetailView) {
        this.proposeCreditInfoDetailView = proposeCreditInfoDetailView;
    }

    public List<CountryView> getCountryViewList() {
        return countryViewList;
    }

    public void setCountryViewList(List<CountryView> countryViewList) {
        this.countryViewList = countryViewList;
    }

    public List<CreditRequestTypeView> getCreditRequestTypeViewList() {
        return creditRequestTypeViewList;
    }

    public void setCreditRequestTypeViewList(List<CreditRequestTypeView> creditRequestTypeViewList) {
        this.creditRequestTypeViewList = creditRequestTypeViewList;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
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

    public boolean isModeEdit() {
        return isModeEdit;
    }

    public void setModeEdit(boolean modeEdit) {
        isModeEdit = modeEdit;
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

    public List<LoanPurposeView> getLoanPurposeViewList() {
        return loanPurposeViewList;
    }

    public void setLoanPurposeViewList(List<LoanPurposeView> loanPurposeViewList) {
        this.loanPurposeViewList = loanPurposeViewList;
    }

    public List<DisbursementTypeView> getDisbursementTypeViewList() {
        return disbursementTypeViewList;
    }

    public void setDisbursementTypeViewList(List<DisbursementTypeView> disbursementTypeViewList) {
        this.disbursementTypeViewList = disbursementTypeViewList;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public ProposeConditionInfoView getProposeConditionInfoView() {
        return proposeConditionInfoView;
    }

    public void setProposeConditionInfoView(ProposeConditionInfoView proposeConditionInfoView) {
        this.proposeConditionInfoView = proposeConditionInfoView;
    }

    public ProposeGuarantorInfoView getProposeGuarantorInfoView() {
        return proposeGuarantorInfoView;
    }

    public void setProposeGuarantorInfoView(ProposeGuarantorInfoView proposeGuarantorInfoView) {
        this.proposeGuarantorInfoView = proposeGuarantorInfoView;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditViewList() {
        return proposeCreditViewList;
    }

    public void setProposeCreditViewList(List<ProposeCreditInfoDetailView> proposeCreditViewList) {
        this.proposeCreditViewList = proposeCreditViewList;
    }

    public ProposeCollateralInfoView getProposeCollateralInfoView() {
        return proposeCollateralInfoView;
    }

    public void setProposeCollateralInfoView(ProposeCollateralInfoView proposeCollateralInfoView) {
        this.proposeCollateralInfoView = proposeCollateralInfoView;
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

    public int getRowSubCollIndex() {
        return rowSubCollIndex;
    }

    public void setRowSubCollIndex(int rowSubCollIndex) {
        this.rowSubCollIndex = rowSubCollIndex;
    }

    public ProposeCollateralInfoSubView getProposeCollateralInfoSubView() {
        return proposeCollateralInfoSubView;
    }

    public void setProposeCollateralInfoSubView(ProposeCollateralInfoSubView proposeCollateralInfoSubView) {
        this.proposeCollateralInfoSubView = proposeCollateralInfoSubView;
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

    public List<MortgageTypeView> getMortgageTypeViewList() {
        return mortgageTypeViewList;
    }

    public void setMortgageTypeViewList(List<MortgageTypeView> mortgageTypeViewList) {
        this.mortgageTypeViewList = mortgageTypeViewList;
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

    public int getRowHeadCollIndex() {
        return rowHeadCollIndex;
    }

    public void setRowHeadCollIndex(int rowHeadCollIndex) {
        this.rowHeadCollIndex = rowHeadCollIndex;
    }

    public List<UsagesView> getUsagesViewList() {
        return usagesViewList;
    }

    public void setUsagesViewList(List<UsagesView> usagesViewList) {
        this.usagesViewList = usagesViewList;
    }
}
