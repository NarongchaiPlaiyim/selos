package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.OpenAccountControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.MessageDialogSeverity;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.BankAccountTypeTransform;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.SBFScoreTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.rits.cloning.Cloner;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private RequestTypeDAO requestTypeDAO;
    @Inject
    private RiskTypeDAO riskTypeDAO;
    @Inject
    private SBFScoreDAO sbfScoreDAO;
    @Inject
    private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    private BankAccountProductDAO accountProductDAO;
    @Inject
    private BankAccountPurposeDAO accountPurposeDAO;
    @Inject
    private BankDAO bankDAO;
    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;
    @Inject
    private CustomerDAO customerDAO;

    @Inject
    private BankAccountTypeTransform bankAccountTypeTransform;
    @Inject
    private SBFScoreTransform sbfScoreTransform;
    @Inject
    private CustomerTransform customerTransform;

    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private OpenAccountControl openAccountControl;

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<SpecialProgram> specialProgramList;
    private List<RequestType> requestTypeList;
    private List<RiskType> riskTypeList;
    private List<SBFScoreView> sbfScoreViewList;
    private List<Bank> bankList;

    private List<BankAccountType> bankAccountTypeList;
    private List<BankAccountProduct> accountProductList;
    private List<BankAccountPurpose> accountPurposeList;

    private List<BankAccountPurposeView> bankAccountPurposeViewList;

    private List<BorrowingType> borrowingTypeList;

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

    public BasicInfo(){
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(true);

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

        HttpSession session = FacesUtil.getSession(true);

        if(checkSession(session)){
            workCaseId = (Long)session.getAttribute("workCaseId");

            loadFieldControl(workCaseId, Screen.BASIC_INFO);
            fieldsControlInitial();

            basicInfoView = new BasicInfoView();

            productGroupList = productGroupDAO.findAll();
            specialProgramList = specialProgramDAO.findAll();
            requestTypeList = requestTypeDAO.findAll();
            riskTypeList = riskTypeDAO.findAll();
            sbfScoreViewList =  sbfScoreTransform.transformToView(sbfScoreDAO.findAll());
            bankList = bankDAO.getListRefinance();

            customerInfoViewList = openAccountControl.getCustomerList(workCaseId);

            bankAccountTypeList = bankAccountTypeDAO.findOpenAccountType();
            accountProductList = new ArrayList<BankAccountProduct>();
            accountPurposeList = accountPurposeDAO.findAll();
            accountNameList = new ArrayList<CustomerInfoView>();
            bankAccountPurposeViewList = new ArrayList<BankAccountPurposeView>();

            for(BankAccountPurpose oap : accountPurposeList){
                BankAccountPurposeView purposeView = new BankAccountPurposeView();
                purposeView.setPurpose(oap);
                bankAccountPurposeViewList.add(purposeView);
            }

            CustomerEntity customerEntity = basicInfoControl.getCustomerEntityByWorkCaseId(workCaseId);

            borrowingTypeList = borrowingTypeDAO.findByCustomerEntity(customerEntity);

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

        }
    }

    public void onInitAddAccount(){
        openAccountView = new OpenAccountView();

        customerId = 0;

        accountNameList = new ArrayList<CustomerInfoView>();

        bankAccountTypeList = bankAccountTypeDAO.findOpenAccountType();

        accountProductList = new ArrayList<BankAccountProduct>();

        accountPurposeList = accountPurposeDAO.findAll();
        bankAccountPurposeViewList = new ArrayList<BankAccountPurposeView>();
        for(BankAccountPurpose oap : accountPurposeList){
            BankAccountPurposeView purposeView = new BankAccountPurposeView();
            purposeView.setPurpose(oap);
            bankAccountPurposeViewList.add(purposeView);
        }

        modeForButton = ModeForButton.ADD;
    }

    public void onSelectEditAccount(){
        try {
            customerId = 0;

            Cloner cloner = new Cloner();
            openAccountView = cloner.deepClone(selectAccount);
            onChangeAccountType();

            accountNameList = cloner.deepClone(openAccountView.getAccountNameList());

            bankAccountPurposeViewList = new ArrayList<BankAccountPurposeView>();
            for(BankAccountPurpose oap : accountPurposeList){
                BankAccountPurposeView purposeView = new BankAccountPurposeView();
                purposeView.setPurpose(oap);
                bankAccountPurposeViewList.add(purposeView);
            }

            for(BankAccountPurposeView biapv : openAccountView.getBankAccountPurposeView()){
                if(biapv.isSelected()){
                    for(BankAccountPurposeView purposeView : bankAccountPurposeViewList){
                        if(biapv.getPurpose().getName().equals(purposeView.getPurpose().getName())){
                            purposeView.setSelected(true);
                        }
                    }
                }
            }
            modeForButton = ModeForButton.EDIT;
        } catch (Exception e) {
            log.error("onSelectEditAccount Exception : {}",e);
        }
    }

    public void onChangeAccountType(){
        accountProductList = accountProductDAO.findByBankAccountTypeId(openAccountView.getBankAccountTypeView().getId());
    }

    public void onSave(){
        try{
            basicInfoControl.saveBasicInfo(basicInfoView, workCaseId);
            messageHeader = msg.get("app.messageHeader.info");
            message = "Save data in basic information success.";
            severity = MessageDialogSeverity.INFO.severity();
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = msg.get("app.messageHeader.error");
            if(ex.getCause() != null){
                message = "Save basic info data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save basic info data failed. Cause : " + ex.getMessage();
            }
            severity = MessageDialogSeverity.ALERT.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
        }
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
        try{
            messageHeader = msg.get("app.messageHeader.info");
            message = "Waiting for this function.";
            severity = MessageDialogSeverity.INFO.severity();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            log.debug("duplicateApplication Exception : {}", ex);
            messageHeader = msg.get("app.messageHeader.error");
            message = ex.getMessage();
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
        CustomerInfoView customerInfoView = customerTransform.transformToView(customerDAO.findById(customerId));
        accountNameList.add(customerInfoView);
    }

    public void onDeleteAccountName(){
        accountNameList.remove(selectAccountName);
    }

    public void onAddAccount(){
        if(accountNameList.size() == 0){
            messageHeader = "Information.";
            message = "Please Add Account Name !!";
            severity = "info";
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
            openAccountView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findById(openAccountView.getBankAccountTypeView().getId())));
        }else{
            openAccountView.getBankAccountTypeView().setName("-");
        }

        if(openAccountView.getBankAccountProduct().getId() != 0){
            openAccountView.setBankAccountProduct(accountProductDAO.findById(openAccountView.getBankAccountProduct().getId()));
        }else{
            openAccountView.getBankAccountProduct().setName("-");
        }

        StringBuilder stringBuilder = new StringBuilder();

        openAccountView.setBankAccountPurposeView(new ArrayList<BankAccountPurposeView>());
        for(BankAccountPurposeView bia : bankAccountPurposeViewList){
            if(bia.isSelected()){
                if(openAccountView.getBankAccountPurposeView().size() == 0){
                    openAccountView.getBankAccountPurposeView().add(bia);
                    stringBuilder.append(bia.getPurpose().getName());
                }else{
                    openAccountView.getBankAccountPurposeView().add(bia);
                    stringBuilder.append(", "+bia.getPurpose().getName());
                }
            }
        }

        if(!stringBuilder.toString().isEmpty()){
            openAccountView.setPurposeForShow(stringBuilder.toString());
        }else{
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
    }

    public void onDeleteAccount() {
        basicInfoView.getDeleteTmpList().add(selectAccount.getId());
        basicInfoView.getOpenAccountViews().remove(selectAccount);
    }

    // Get Set
    public BasicInfoView getBasicInfoView() {
        return basicInfoView;
    }

    public void setBasicInfoView(BasicInfoView basicInfoView) {
        this.basicInfoView = basicInfoView;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<RequestType> getRequestTypeList() {
        return requestTypeList;
    }

    public void setRequestTypeList(List<RequestType> requestTypeList) {
        this.requestTypeList = requestTypeList;
    }

    public List<SpecialProgram> getSpecialProgramList() {
        return specialProgramList;
    }

    public void setSpecialProgramList(List<SpecialProgram> specialProgramList) {
        this.specialProgramList = specialProgramList;
    }

    public List<RiskType> getRiskTypeList() {
        return riskTypeList;
    }

    public void setRiskTypeList(List<RiskType> riskTypeList) {
        this.riskTypeList = riskTypeList;
    }

    public OpenAccountView getOpenAccountView() {
        return openAccountView;
    }

    public void setOpenAccountView(OpenAccountView openAccountView) {
        this.openAccountView = openAccountView;
    }

    public List<BankAccountType> getBankAccountTypeList() {
        return bankAccountTypeList;
    }

    public void setBankAccountTypeList(List<BankAccountType> bankAccountTypeList) {
        this.bankAccountTypeList = bankAccountTypeList;
    }

    public List<BankAccountProduct> getAccountProductList() {
        return accountProductList;
    }

    public void setAccountProductList(List<BankAccountProduct> accountProductList) {
        this.accountProductList = accountProductList;
    }

    public List<BankAccountPurposeView> getBankAccountPurposeViewList() {
        return bankAccountPurposeViewList;
    }

    public void setBankAccountPurposeViewList(List<BankAccountPurposeView> bankAccountPurposeViewList) {
        this.bankAccountPurposeViewList = bankAccountPurposeViewList;
    }

    public List<SBFScoreView> getSbfScoreViewList() {
        return sbfScoreViewList;
    }

    public void setSbfScoreViewList(List<SBFScoreView> sbfScoreViewList) {
        this.sbfScoreViewList = sbfScoreViewList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
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

    public List<BorrowingType> getBorrowingTypeList() {
        return borrowingTypeList;
    }

    public void setBorrowingTypeList(List<BorrowingType> borrowingTypeList) {
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
