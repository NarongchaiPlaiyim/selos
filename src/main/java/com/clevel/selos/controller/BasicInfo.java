package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.CalculationControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.OpenAccountControl;
import com.clevel.selos.businesscontrol.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.OpenAccountView;
import com.clevel.selos.model.view.master.BankAccountProductView;
import com.clevel.selos.model.view.master.BankAccountPurposeView;
import com.clevel.selos.model.view.master.BankAccountTypeView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.master.BankAccountTypeTransform;
import com.clevel.selos.transform.master.SBFScoreTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "basicInfo")
public class BasicInfo extends BaseController {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    private SLOSAuditor slosAuditor;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private BankAccountTypeTransform bankAccountTypeTransform;
    @Inject
    private SBFScoreTransform sbfScoreTransform;
    @Inject
    private CustomerTransform customerTransform;

    @Inject
    private ProductControl productControl;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private CalculationControl calculationControl;
    @Inject
    private OpenAccountControl openAccountControl;
    @Inject
    private BankAccountPurposeControl bankAccountPurposeControl;
    @Inject
    private BankAccountTypeControl bankAccountTypeControl;
    @Inject
    private BankAccountProductControl bankAccountProductControl;
    @Inject
    private SpecialProgramControl specialProgramControl;
    @Inject
    private RequestTypeControl requestTypeControl;
    @Inject
    private RiskTypeControl riskTypeControl;
    @Inject
    private SBFScoreControl sbfScoreControl;
    @Inject
    private BankControl bankControl;
    @Inject
    private BorrowingTypeControl borrowingTypeControl;
    @Inject
    private CustomerInfoControl customerInfoControl;

    //*** Drop down List ***//
    private List<SelectItem> productGroupList;
    private List<SelectItem> specialProgramList;
    private List<SelectItem> requestTypeList;
    private List<SelectItem> riskTypeList;
    private List<SelectItem> sbfScoreViewList;
    private List<SelectItem> bankList;

    private List<BankAccountTypeView> bankAccountTypeList;
    private List<BankAccountProductView> accountProductList;
    private List<BankAccountPurposeView> bankAccountPurposeViewList;

    private List<SelectItem> borrowingTypeList;

    private List<String> yearList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    //Dialog
    private OpenAccountView openAccountView;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private OpenAccountView selectAccount;
    private int rowIndex;

    private String messageHeader;
    private String message;
    private String severity;

    //session
    private long workCaseId;

    //for mandate
    private boolean reqApplicationNo;
    private boolean reqRefApplicationNo;
    private boolean reqCaNo;
    private boolean reqRequestType;
    private boolean reqProductGroup;
    private boolean reqUnpaidFeeInsurance;
    private boolean reqNoPendingClaimLG;
    private boolean reqIsConstructionRequestLG;
    private boolean reqIsAbleToGettingGuarantorJob;
    private boolean reqNoClaimLGHistory;
    private boolean reqNoRevokedLicense;
    private boolean reqNoLateWorkDelivery;
    private boolean reqIsAdequateOfCapitalResource;
    private boolean reqIsApplySpecialProgram;
    private boolean reqSpecialProgramValue;
    private boolean reqIsRefinanceIN;
    private boolean reqRefinanceInValue;
    private boolean reqIsRefinanceOUT;
    private boolean reqRefinanceOutValue;
    private boolean reqRiskCustomerType;
    private boolean reqQualitativeType;
    private boolean reqIsExistingSMECustomer;
    private boolean reqExistingSMECustomerSince;
    private boolean reqLastReviewDate;
    private boolean reqExtendedReviewDate;
    private boolean reqSCFScore;
    private boolean reqRequestLoanWithSameName;
    private boolean reqHaveLoanInOneYear;
    private boolean reqPassAnnualReview;
    private boolean reqLoanRequestPattern;
    private boolean reqReferralName;
    private boolean reqReferralID;
    private boolean reqIsApplyBA;
    private boolean reqBaPaymentMethod;

    //tmp for radio button
    private int tmpSpecialProgram;
    private int tmpRefinanceIN;
    private int tmpRefinanceOUT;
    private int tmpApplyBA;

    private String accDlg;

    private String currentDateDDMMYY;

    private List<CustomerInfoView> customerInfoViewList;
    private long customerId;
    private List<CustomerInfoView> accountNameList;
    private CustomerInfoView selectAccountName;

    private boolean permissionCheck;

    private String userId;

    public BasicInfo(){
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");

        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        User user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        if(checkSession(session)){
            workCaseId = (Long)session.getAttribute("workCaseId");
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            loadFieldControl(workCaseId, Screen.BASIC_INFO, ownerCaseUserId);
            fieldsControlInitial();

            basicInfoView = new BasicInfoView();

            productGroupList = productControl.getProductGroupSelectItem();
            specialProgramList = specialProgramControl.getSpecialProgramSelectItem();
            requestTypeList = requestTypeControl.getRequestTypeViewActive();
            riskTypeList = riskTypeControl.getRiskTypeActive();
            sbfScoreViewList =  sbfScoreControl.getSBFScoreActive();
            bankList = bankControl.getBankRefinanceList();

            customerInfoViewList = openAccountControl.getCustomerList(workCaseId);

            bankAccountTypeList = bankAccountTypeControl.getOpenAccountTypeList();
            accountProductList = new ArrayList<BankAccountProductView>();
            accountNameList = new ArrayList<CustomerInfoView>();
            bankAccountPurposeViewList = bankAccountPurposeControl.getBankAccountPurposeViewActiveList();

            CustomerEntity customerEntity = basicInfoControl.getCustomerEntityByWorkCaseId(workCaseId);

            borrowingTypeList = borrowingTypeControl.getBorrowingTypeByCustomerEntity(customerEntity.getId());

            basicInfoView.setSpProgram(0);
            basicInfoView.setRefIn(0);
            basicInfoView.setRefOut(0);
            basicInfoView.setApplyBA(0);

            tmpSpecialProgram = 0;
            tmpRefinanceIN = 0;
            tmpRefinanceOUT = 0;
            tmpApplyBA = 0;

            basicInfoView = basicInfoControl.getBasicInfo(workCaseId);

            if(basicInfoView.getId() == 0){
                basicInfoView.setQualitative(customerEntity.getDefaultQualitative());
            }

            setDisabledValue("qualitativeType",false);
            if(!customerEntity.isChangeQualtiEnable()){
                setDisabledValue("qualitativeType",true);
            }

            openAccountView = new OpenAccountView();

            yearList = DateTimeUtil.getPreviousFiftyYearTH();

            onChangeSpecialProgramInit();
            onChangeRefInInit();
            onChangeRefOutInit();
            onChangeExistingSMEInit();
            onChangeBAInit();
            onChangeReqLGInit();

            loadUserAccessMatrix(Screen.BASIC_INFO);
            permissionCheck = canAccess(Screen.BASIC_INFO);

            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void onCancel() {
        slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");
        onCreation();
    }

    public void onInitAddAccount(){
        Date date = new Date();

        modeForButton = ModeForButton.ADD;

        openAccountView = new OpenAccountView();
        accountNameList = new ArrayList<CustomerInfoView>();
        bankAccountTypeList = bankAccountTypeControl.getOpenAccountTypeList();
        accountProductList = new ArrayList<BankAccountProductView>();

        customerId = 0;
        bankAccountPurposeViewList = bankAccountPurposeControl.getBankAccountPurposeViewActiveList();

        slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_ADD, "On Add Open Account", date, ActionResult.SUCCESS, "");
    }

    public void onSelectEditAccount(){
        Date date = new Date();

        try {
            modeForButton = ModeForButton.EDIT;

            customerId = 0;

            Cloner cloner = new Cloner();
            openAccountView = cloner.deepClone(selectAccount);
            onChangeAccountType();

            accountNameList = cloner.deepClone(openAccountView.getAccountNameList());

            bankAccountPurposeViewList = bankAccountPurposeControl.getBankAccountPurposeViewActiveList();

            for(BankAccountPurposeView _OpenBAPV : openAccountView.getBankAccountPurposeView()){
                for(BankAccountPurposeView bankAccountPurposeView : bankAccountPurposeViewList){
                    if(_OpenBAPV.getId() == bankAccountPurposeView.getId()){
                        bankAccountPurposeView.setSelected(true);
                    }
                }
            }
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_EDIT, "On Edit Open Account", date, ActionResult.SUCCESS, "");
        } catch (Exception e) {
            log.error("onSelectEditAccount Exception : {}", Util.getMessageException(e));
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_EDIT, "On Edit Open Account", date, ActionResult.FAILED, Util.getMessageException(e));
        }
    }

    public void onChangeAccountType(){
        accountProductList = bankAccountProductControl.getAccountProductByAccountTypeId(openAccountView.getBankAccountTypeView().getId());
    }

    public void onSave(){
        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        try{
            String queueName = Util.parseString(session.getAttribute("queueName"), "");
            String wobNumber = Util.parseString(session.getAttribute("wobNumber"), "");
            basicInfoControl.saveBasicInfo(basicInfoView, workCaseId, queueName, wobNumber);
            calculationControl.calculateBOTClass(workCaseId);
            calculationControl.calForBasicInfo(workCaseId);
            calculationControl.calculateTotalProposeAmount(workCaseId);
            calculationControl.calculateMaximumSMELimit(workCaseId);

            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, "");

            onCreation();
            updateHeaderInfo();

            messageHeader = msg.get("app.messageHeader.info");
            message = "Save data in basic information success.";
            severity = MessageDialogSeverity.INFO.severity();
        } catch(Exception ex) {
            log.error("Basic Info :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, Util.getMessageException(ex));

            messageHeader = msg.get("app.messageHeader.error");
            message = "Save basic info data failed. Cause : " + Util.getMessageException(ex);
            severity = MessageDialogSeverity.ALERT.severity();
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    private void updateHeaderInfo(){
        AppHeaderView appHeaderView = getAppHeaderView();
        appHeaderView.setProductGroup(basicInfoView.getProductGroup() != null ? basicInfoView.getProductGroup().getName() : "");
        appHeaderView.setRefinance(basicInfoView.getRefinanceIn() != null ? basicInfoView.getRefinanceIn().getName() : "");
        appHeaderView.setRefinanceOut(basicInfoView.getRefinanceOut() != null ? basicInfoView.getRefinanceOut().getName() : "");
        setAppHeaderView(appHeaderView);
    }

    public void onChangeSpecialProgram(){
        if(tmpSpecialProgram == basicInfoView.getSpProgram()){
            basicInfoView.setSpProgram(0);
            basicInfoView.setSpecialProgram(new SpecialProgram());
            tmpSpecialProgram = 0;
        } else {
            tmpSpecialProgram = basicInfoView.getSpProgram();
            basicInfoView.setSpecialProgram(new SpecialProgram());
        }

        if(basicInfoView.getSpProgram() == 2){ // yes
            reqSpecialProgramValue = true;
            setDisabledValue("specialProgramValue",false);
        } else {
            reqSpecialProgramValue = false;
            setDisabledValue("specialProgramValue",true);
        }
    }

    public void onChangeRefIn(){
        if(tmpRefinanceIN == basicInfoView.getRefIn()){
            basicInfoView.setRefIn(0);
            basicInfoView.setRefinanceIn(new Bank());
            tmpRefinanceIN = 0;
        } else {
            tmpRefinanceIN = basicInfoView.getRefIn();
            basicInfoView.setRefinanceIn(new Bank());
        }

        if(basicInfoView.getRefIn() == 2){ // yes
            reqRefinanceInValue = true;
            setDisabledValue("refinanceInValue",false);
        } else {
            reqRefinanceInValue = false;
            setDisabledValue("refinanceInValue",true);
        }
    }

    public void onChangeRefOut(){
        if(tmpRefinanceOUT == basicInfoView.getRefOut()){
            basicInfoView.setRefOut(0);
            basicInfoView.setRefinanceOut(new Bank());
            tmpRefinanceOUT = 0;
        } else {
            tmpRefinanceOUT = basicInfoView.getRefOut();
            basicInfoView.setRefinanceOut(new Bank());
        }

        if(basicInfoView.getRefOut() == 2){ // yes
            reqRefinanceOutValue = true;
            setDisabledValue("refinanceOutValue",false);
        } else {
            reqRefinanceOutValue = false;
            setDisabledValue("refinanceOutValue",true);
        }
    }

    public void onChangeExistingSME(){
        if(basicInfoView.getExistingSME() == 2){ // yes
            reqExistingSMECustomerSince = true;
            setDisabledValue("existingSMECustomerSince",false);
            basicInfoView.setExistingSME(0);
        } else {
            reqExistingSMECustomerSince = false;
            setDisabledValue("existingSMECustomerSince",true);
            basicInfoView.setExistingSME(0);
        }
    }

    public void onChangeBA(){
        if(tmpApplyBA == basicInfoView.getApplyBA()){
            basicInfoView.setApplyBA(0);
            tmpApplyBA = 0;
        } else {
            tmpApplyBA = basicInfoView.getApplyBA();
        }

        if(basicInfoView.getApplyBA() == 1 || basicInfoView.getApplyBA() == 0){
            basicInfoView.setBaPaymentMethodValue(null);
        }else{
            basicInfoView.setBaPaymentMethodValue(BAPaymentMethodValue.TOPUP);
        }

        if(basicInfoView.getApplyBA() == 2){ // yes
            reqBaPaymentMethod = true;
            setDisabledValue("baPaymentMethod",false);
        } else {
            reqBaPaymentMethod = false;
            setDisabledValue("baPaymentMethod",true);
        }
    }

    public void onChangeReqLG(){
        if(basicInfoView.isCharFCLG()){
            setDisabledValue("isAbleToGettingGuarantorJob",false);
            setDisabledValue("noClaimLGHistory",false);
            setDisabledValue("noRevokedLicense",false);
            setDisabledValue("noLateWorkDelivery",false);
            setDisabledValue("isAdequateOfCapitalResource",false);
            basicInfoView.setCharFCIns(false);
            basicInfoView.setCharFCCom(false);
            basicInfoView.setCharFCAba(false);
            basicInfoView.setCharFCLate(false);
            basicInfoView.setCharFCFund(false);

        } else {
            setDisabledValue("isAbleToGettingGuarantorJob",true);
            setDisabledValue("noClaimLGHistory",true);
            setDisabledValue("noRevokedLicense",true);
            setDisabledValue("noLateWorkDelivery",true);
            setDisabledValue("isAdequateOfCapitalResource",true);
            basicInfoView.setCharFCIns(false);
            basicInfoView.setCharFCCom(false);
            basicInfoView.setCharFCAba(false);
            basicInfoView.setCharFCLate(false);
            basicInfoView.setCharFCFund(false);
        }
    }

    //Init
    public void onChangeSpecialProgramInit(){
        if(tmpSpecialProgram == basicInfoView.getSpProgram()){
            basicInfoView.setSpProgram(0);
            tmpSpecialProgram = 0;
        } else {
            tmpSpecialProgram = basicInfoView.getSpProgram();
        }

        if(basicInfoView.getSpProgram() == 2){ // yes
            reqSpecialProgramValue = true;
            setDisabledValue("specialProgramValue",false);
        } else {
            reqSpecialProgramValue = false;
            setDisabledValue("specialProgramValue",true);
        }
    }

    public void onChangeRefInInit(){
        if(tmpRefinanceIN == basicInfoView.getRefIn()){
            basicInfoView.setRefIn(0);
            tmpRefinanceIN = 0;
        } else {
            tmpRefinanceIN = basicInfoView.getRefIn();
        }

        if(basicInfoView.getRefIn() == 2){ // yes
            reqRefinanceInValue = true;
            setDisabledValue("refinanceInValue",false);
        } else {
            reqRefinanceInValue = false;
            setDisabledValue("refinanceInValue",true);
        }
    }

    public void onChangeRefOutInit(){
        if(tmpRefinanceOUT == basicInfoView.getRefOut()){
            basicInfoView.setRefOut(0);
            tmpRefinanceOUT = 0;
        } else {
            tmpRefinanceOUT = basicInfoView.getRefOut();
        }

        if(basicInfoView.getRefOut() == 2){ // yes
            reqRefinanceOutValue = true;
            setDisabledValue("refinanceOutValue",false);
        } else {
            reqRefinanceOutValue = false;
            setDisabledValue("refinanceOutValue",true);
        }
    }

    public void onChangeExistingSMEInit(){
        if(basicInfoView.getExistingSME() == 2){ // yes
            reqExistingSMECustomerSince = true;
            setDisabledValue("existingSMECustomerSince",false);
        } else {
            reqExistingSMECustomerSince = false;
            setDisabledValue("existingSMECustomerSince",true);
        }
    }

    public void onChangeBAInit(){
        if(tmpApplyBA == basicInfoView.getApplyBA()){
            basicInfoView.setApplyBA(0);
            tmpApplyBA = 0;
        } else {
            tmpApplyBA = basicInfoView.getApplyBA();
        }

        if(basicInfoView.getApplyBA() == 2){ // yes
            reqBaPaymentMethod = true;
            setDisabledValue("baPaymentMethod",false);
        } else {
            reqBaPaymentMethod = false;
            setDisabledValue("baPaymentMethod",true);
        }
    }

    public void onChangeReqLGInit(){
        if(basicInfoView.isCharFCLG()){
            setDisabledValue("isAbleToGettingGuarantorJob",false);
            setDisabledValue("noClaimLGHistory",false);
            setDisabledValue("noRevokedLicense",false);
            setDisabledValue("noLateWorkDelivery",false);
            setDisabledValue("isAdequateOfCapitalResource",false);
        } else {
            setDisabledValue("isAbleToGettingGuarantorJob",true);
            setDisabledValue("noClaimLGHistory",true);
            setDisabledValue("noRevokedLicense",true);
            setDisabledValue("noLateWorkDelivery",true);
            setDisabledValue("isAdequateOfCapitalResource",true);
        }
    }

    public void onDuplicateApplication(){
        Date date = new Date();
        try {
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_ACTION, "Duplicate Application", date, ActionResult.SUCCESS, "");
            messageHeader = msg.get("app.messageHeader.info");
            message = "Waiting for this function.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.debug("duplicateApplication Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_ACTION, "Duplicate Application", date, ActionResult.FAILED, Util.getMessageException(ex));
            messageHeader = msg.get("app.messageHeader.error");
            message = Util.getMessageException(ex);
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void fieldsControlInitial(){
        reqApplicationNo = true;
        reqRefApplicationNo = false;
        reqCaNo = true;
        reqRequestType = true;
        reqProductGroup = true;
        reqUnpaidFeeInsurance = false;
        reqNoPendingClaimLG = false;
        reqIsConstructionRequestLG = false;
        reqIsAbleToGettingGuarantorJob = false;
        reqNoClaimLGHistory = false;
        reqNoRevokedLicense = false;
        reqNoLateWorkDelivery = false;
        reqIsAdequateOfCapitalResource = false;
        reqIsApplySpecialProgram = true;
        reqSpecialProgramValue = false;
        reqIsRefinanceIN = true;
        reqRefinanceInValue = false;
        reqIsRefinanceOUT = true;
        reqRefinanceOutValue = false;
        reqRiskCustomerType = true;
        reqQualitativeType = true;
        reqIsExistingSMECustomer = false;
        reqExistingSMECustomerSince = false;
        reqLastReviewDate = false;
        reqExtendedReviewDate = false;
        reqSCFScore = false;
        reqRequestLoanWithSameName = false;
        reqHaveLoanInOneYear = false;
        reqPassAnnualReview = false;
        reqLoanRequestPattern = true;
        reqReferralName = false;
        reqReferralID = false;
        reqIsApplyBA = false;
        reqBaPaymentMethod = false;
    }

    public void onAddAccountName(){
        if(customerId == 0){
            return;
        }
        if(accountNameList.size() > 0){
            for (CustomerInfoView c : accountNameList) {
                if(customerId == c.getId()){
                    return;
                }
            }
        }

        CustomerInfoView customerInfoView = customerInfoControl.getCustomerById(customerId);
        accountNameList.add(customerInfoView);
    }

    public void onDeleteAccountName(){
        accountNameList.remove(selectAccountName);
    }

    public void onAddAccount() {
        Date date = new Date();

        if(accountNameList.size() == 0){
            messageHeader = "Information.";
            message = "Please Add Account Name !!";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg2.show()");
            return;
        }

        StringBuilder accName = new StringBuilder();
        for (int i=0; i<accountNameList.size(); i++){
            if(accountNameList.size()-1 == i){
                accName.append(accountNameList.get(i).getFirstNameTh()+" "+accountNameList.get(i).getLastNameTh());
            } else {
                accName.append(accountNameList.get(i).getFirstNameTh()+" "+accountNameList.get(i).getLastNameTh()+", ");
            }
        }
        openAccountView.setAccountName(accName.toString());
        openAccountView.setAccountNameList(accountNameList);

        if(openAccountView.getBankAccountTypeView().getId() != 0){
            for(BankAccountTypeView bankAccountTypeView : bankAccountTypeList){
                if(bankAccountTypeView.getId() == openAccountView.getBankAccountTypeView().getId()){
                    openAccountView.setBankAccountTypeView(bankAccountTypeView);
                    break;
                }
            }
        } else {
            openAccountView.getBankAccountTypeView().setName("-");
        }

        if(openAccountView.getBankAccountTypeView().getId() == 0){
            openAccountView.getBankAccountTypeView().setName("-");
        }

        if(openAccountView.getBankAccountProductView().getId() != 0){
            for(BankAccountProductView bankAccountProductView : accountProductList){
                if(bankAccountProductView.getId() == openAccountView.getBankAccountProductView().getId()){
                    openAccountView.setBankAccountProductView(bankAccountProductView);
                    break;
                }
            }
        } else {
            openAccountView.getBankAccountProductView().setName("-");
        }

        if(openAccountView.getBankAccountProductView().getId() == 0){
            openAccountView.getBankAccountProductView().setName("-");
        }

        StringBuilder stringBuilder = new StringBuilder();

        openAccountView.setBankAccountPurposeView(new ArrayList<BankAccountPurposeView>());
        for(BankAccountPurposeView bia : bankAccountPurposeViewList){
            if(bia.isSelected()){
                if(openAccountView.getBankAccountPurposeView().size() == 0){
                    openAccountView.getBankAccountPurposeView().add(bia);
                    stringBuilder.append(bia.getName());
                }else{
                    openAccountView.getBankAccountPurposeView().add(bia);
                    stringBuilder.append(", " + bia.getName());
                }
            }
        }

        if(!stringBuilder.toString().isEmpty()){
            openAccountView.setPurposeForShow(stringBuilder.toString());
        } else {
            openAccountView.setPurposeForShow("-");
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            basicInfoView.getOpenAccountViews().add(openAccountView);
        }else{
            basicInfoView.getOpenAccountViews().set(rowIndex, openAccountView);
        }

        boolean complete = true;        //Change only failed to save
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);

        slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_SAVE, "On Save Open Account", date, ActionResult.SUCCESS, "");
    }

    public void onCancelAccount() {
        slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Open Account", new Date(), ActionResult.SUCCESS, "");

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
    }

    public void onDeleteAccount() {
        basicInfoView.getDeleteTmpList().add(selectAccount.getId());
        basicInfoView.getOpenAccountViews().remove(selectAccount);

        slosAuditor.add(Screen.BASIC_INFO.value(), userId, ActionAudit.ON_DELETE, "On Delete Open Account :: Open Account ID :: " + selectAccount.getId(), new Date(), ActionResult.SUCCESS, "");
    }

    // Get Set
    public BasicInfoView getBasicInfoView() {
        return basicInfoView;
    }

    public void setBasicInfoView(BasicInfoView basicInfoView) {
        this.basicInfoView = basicInfoView;
    }

    public List<SelectItem> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<SelectItem> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<SelectItem> getRequestTypeList() {
        return requestTypeList;
    }

    public void setRequestTypeList(List<SelectItem> requestTypeList) {
        this.requestTypeList = requestTypeList;
    }

    public List<SelectItem> getSpecialProgramList() {
        return specialProgramList;
    }

    public void setSpecialProgramList(List<SelectItem> specialProgramList) {
        this.specialProgramList = specialProgramList;
    }

    public List<SelectItem> getRiskTypeList() {
        return riskTypeList;
    }

    public void setRiskTypeList(List<SelectItem> riskTypeList) {
        this.riskTypeList = riskTypeList;
    }

    public OpenAccountView getOpenAccountView() {
        return openAccountView;
    }

    public void setOpenAccountView(OpenAccountView openAccountView) {
        this.openAccountView = openAccountView;
    }

    public List<BankAccountTypeView> getBankAccountTypeList() {
        return bankAccountTypeList;
    }

    public void setBankAccountTypeList(List<BankAccountTypeView> bankAccountTypeList) {
        this.bankAccountTypeList = bankAccountTypeList;
    }

    public List<BankAccountProductView> getAccountProductList() {
        return accountProductList;
    }

    public void setAccountProductList(List<BankAccountProductView> accountProductList) {
        this.accountProductList = accountProductList;
    }

    public List<BankAccountPurposeView> getBankAccountPurposeViewList() {
        return bankAccountPurposeViewList;
    }

    public void setBankAccountPurposeViewList(List<BankAccountPurposeView> bankAccountPurposeViewList) {
        this.bankAccountPurposeViewList = bankAccountPurposeViewList;
    }

    public List<SelectItem> getSbfScoreViewList() {
        return sbfScoreViewList;
    }

    public void setSbfScoreViewList(List<SelectItem> sbfScoreViewList) {
        this.sbfScoreViewList = sbfScoreViewList;
    }

    public List<SelectItem> getBankList() {
        return bankList;
    }

    public void setBankList(List<SelectItem> bankList) {
        this.bankList = bankList;
    }

    public OpenAccountView getSelectAccount() {
        return selectAccount;
    }

    public void setSelectAccount(OpenAccountView selectAccount) {
        this.selectAccount = selectAccount;
    }

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<SelectItem> getBorrowingTypeList() {
        return borrowingTypeList;
    }

    public void setBorrowingTypeList(List<SelectItem> borrowingTypeList) {
        this.borrowingTypeList = borrowingTypeList;
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

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public boolean isReqApplicationNo() {
        return reqApplicationNo;
    }

    public void setReqApplicationNo(boolean reqApplicationNo) {
        this.reqApplicationNo = reqApplicationNo;
    }

    public boolean isReqRefApplicationNo() {
        return reqRefApplicationNo;
    }

    public void setReqRefApplicationNo(boolean reqRefApplicationNo) {
        this.reqRefApplicationNo = reqRefApplicationNo;
    }

    public boolean isReqCaNo() {
        return reqCaNo;
    }

    public void setReqCaNo(boolean reqCaNo) {
        this.reqCaNo = reqCaNo;
    }

    public boolean isReqRequestType() {
        return reqRequestType;
    }

    public void setReqRequestType(boolean reqRequestType) {
        this.reqRequestType = reqRequestType;
    }

    public boolean isReqProductGroup() {
        return reqProductGroup;
    }

    public void setReqProductGroup(boolean reqProductGroup) {
        this.reqProductGroup = reqProductGroup;
    }

    public boolean isReqUnpaidFeeInsurance() {
        return reqUnpaidFeeInsurance;
    }

    public void setReqUnpaidFeeInsurance(boolean reqUnpaidFeeInsurance) {
        this.reqUnpaidFeeInsurance = reqUnpaidFeeInsurance;
    }

    public boolean isReqNoPendingClaimLG() {
        return reqNoPendingClaimLG;
    }

    public void setReqNoPendingClaimLG(boolean reqNoPendingClaimLG) {
        this.reqNoPendingClaimLG = reqNoPendingClaimLG;
    }

    public boolean isReqIsConstructionRequestLG() {
        return reqIsConstructionRequestLG;
    }

    public void setReqIsConstructionRequestLG(boolean reqIsConstructionRequestLG) {
        this.reqIsConstructionRequestLG = reqIsConstructionRequestLG;
    }

    public boolean isReqIsAbleToGettingGuarantorJob() {
        return reqIsAbleToGettingGuarantorJob;
    }

    public void setReqIsAbleToGettingGuarantorJob(boolean reqIsAbleToGettingGuarantorJob) {
        this.reqIsAbleToGettingGuarantorJob = reqIsAbleToGettingGuarantorJob;
    }

    public boolean isReqNoClaimLGHistory() {
        return reqNoClaimLGHistory;
    }

    public void setReqNoClaimLGHistory(boolean reqNoClaimLGHistory) {
        this.reqNoClaimLGHistory = reqNoClaimLGHistory;
    }

    public boolean isReqNoRevokedLicense() {
        return reqNoRevokedLicense;
    }

    public void setReqNoRevokedLicense(boolean reqNoRevokedLicense) {
        this.reqNoRevokedLicense = reqNoRevokedLicense;
    }

    public boolean isReqNoLateWorkDelivery() {
        return reqNoLateWorkDelivery;
    }

    public void setReqNoLateWorkDelivery(boolean reqNoLateWorkDelivery) {
        this.reqNoLateWorkDelivery = reqNoLateWorkDelivery;
    }

    public boolean isReqIsAdequateOfCapitalResource() {
        return reqIsAdequateOfCapitalResource;
    }

    public void setReqIsAdequateOfCapitalResource(boolean reqIsAdequateOfCapitalResource) {
        this.reqIsAdequateOfCapitalResource = reqIsAdequateOfCapitalResource;
    }

    public boolean isReqIsApplySpecialProgram() {
        return reqIsApplySpecialProgram;
    }

    public void setReqIsApplySpecialProgram(boolean reqIsApplySpecialProgram) {
        this.reqIsApplySpecialProgram = reqIsApplySpecialProgram;
    }

    public boolean isReqSpecialProgramValue() {
        return reqSpecialProgramValue;
    }

    public void setReqSpecialProgramValue(boolean reqSpecialProgramValue) {
        this.reqSpecialProgramValue = reqSpecialProgramValue;
    }

    public boolean isReqIsRefinanceIN() {
        return reqIsRefinanceIN;
    }

    public void setReqIsRefinanceIN(boolean reqIsRefinanceIN) {
        this.reqIsRefinanceIN = reqIsRefinanceIN;
    }

    public boolean isReqRefinanceInValue() {
        return reqRefinanceInValue;
    }

    public void setReqRefinanceInValue(boolean reqRefinanceInValue) {
        this.reqRefinanceInValue = reqRefinanceInValue;
    }

    public boolean isReqIsRefinanceOUT() {
        return reqIsRefinanceOUT;
    }

    public void setReqIsRefinanceOUT(boolean reqIsRefinanceOUT) {
        this.reqIsRefinanceOUT = reqIsRefinanceOUT;
    }

    public boolean isReqRefinanceOutValue() {
        return reqRefinanceOutValue;
    }

    public void setReqRefinanceOutValue(boolean reqRefinanceOutValue) {
        this.reqRefinanceOutValue = reqRefinanceOutValue;
    }

    public boolean isReqRiskCustomerType() {
        return reqRiskCustomerType;
    }

    public void setReqRiskCustomerType(boolean reqRiskCustomerType) {
        this.reqRiskCustomerType = reqRiskCustomerType;
    }

    public boolean isReqQualitativeType() {
        return reqQualitativeType;
    }

    public void setReqQualitativeType(boolean reqQualitativeType) {
        this.reqQualitativeType = reqQualitativeType;
    }

    public boolean isReqIsExistingSMECustomer() {
        return reqIsExistingSMECustomer;
    }

    public void setReqIsExistingSMECustomer(boolean reqIsExistingSMECustomer) {
        this.reqIsExistingSMECustomer = reqIsExistingSMECustomer;
    }

    public boolean isReqExistingSMECustomerSince() {
        return reqExistingSMECustomerSince;
    }

    public void setReqExistingSMECustomerSince(boolean reqExistingSMECustomerSince) {
        this.reqExistingSMECustomerSince = reqExistingSMECustomerSince;
    }

    public boolean isReqLastReviewDate() {
        return reqLastReviewDate;
    }

    public void setReqLastReviewDate(boolean reqLastReviewDate) {
        this.reqLastReviewDate = reqLastReviewDate;
    }

    public boolean isReqExtendedReviewDate() {
        return reqExtendedReviewDate;
    }

    public void setReqExtendedReviewDate(boolean reqExtendedReviewDate) {
        this.reqExtendedReviewDate = reqExtendedReviewDate;
    }

    public boolean isReqSCFScore() {
        return reqSCFScore;
    }

    public void setReqSCFScore(boolean reqSCFScore) {
        this.reqSCFScore = reqSCFScore;
    }

    public boolean isReqRequestLoanWithSameName() {
        return reqRequestLoanWithSameName;
    }

    public void setReqRequestLoanWithSameName(boolean reqRequestLoanWithSameName) {
        this.reqRequestLoanWithSameName = reqRequestLoanWithSameName;
    }

    public boolean isReqHaveLoanInOneYear() {
        return reqHaveLoanInOneYear;
    }

    public void setReqHaveLoanInOneYear(boolean reqHaveLoanInOneYear) {
        this.reqHaveLoanInOneYear = reqHaveLoanInOneYear;
    }

    public boolean isReqPassAnnualReview() {
        return reqPassAnnualReview;
    }

    public void setReqPassAnnualReview(boolean reqPassAnnualReview) {
        this.reqPassAnnualReview = reqPassAnnualReview;
    }

    public boolean isReqLoanRequestPattern() {
        return reqLoanRequestPattern;
    }

    public void setReqLoanRequestPattern(boolean reqLoanRequestPattern) {
        this.reqLoanRequestPattern = reqLoanRequestPattern;
    }

    public boolean isReqReferralName() {
        return reqReferralName;
    }

    public void setReqReferralName(boolean reqReferralName) {
        this.reqReferralName = reqReferralName;
    }

    public boolean isReqReferralID() {
        return reqReferralID;
    }

    public void setReqReferralID(boolean reqReferralID) {
        this.reqReferralID = reqReferralID;
    }

    public boolean isReqIsApplyBA() {
        return reqIsApplyBA;
    }

    public void setReqIsApplyBA(boolean reqIsApplyBA) {
        this.reqIsApplyBA = reqIsApplyBA;
    }

    public boolean isReqBaPaymentMethod() {
        return reqBaPaymentMethod;
    }

    public void setReqBaPaymentMethod(boolean reqBaPaymentMethod) {
        this.reqBaPaymentMethod = reqBaPaymentMethod;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public String getAccDlg() {
        return accDlg;
    }

    public void setAccDlg(String accDlg) {
        this.accDlg = accDlg;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public List<CustomerInfoView> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<CustomerInfoView> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public CustomerInfoView getSelectAccountName() {
        return selectAccountName;
    }

    public void setSelectAccountName(CustomerInfoView selectAccountName) {
        this.selectAccountName = selectAccountName;
    }

    public boolean isPermissionCheck() {
        return permissionCheck;
    }

    public void setPermissionCheck(boolean permissionCheck) {
        this.permissionCheck = permissionCheck;
    }
}
