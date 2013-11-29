package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.BankAccountTypeTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "basicInfo")
public class BasicInfo extends MandatoryFieldsControl {
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
    private OpenAccountProductDAO openAccountProductDAO;
    @Inject
    private OpenAccountPurposeDAO openAccountPurposeDAO;
    @Inject
    private BankDAO bankDAO;
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;
    @Inject
    private BAPaymentMethodDAO baPaymentMethodDAO;
    @Inject
    private BankAccountTypeTransform bankAccountTypeTransform;

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<SpecialProgram> specialProgramList;
    private List<RequestType> requestTypeList;
    private List<RiskType> riskTypeList;
    private List<SBFScore> sbfScoreList;
    private List<Bank> bankList;

    private List<BankAccountType> bankAccountTypeList;
    private List<OpenAccountProduct> openAccountProductList;
    private List<OpenAccountPurpose> openAccountPurposeList;

    private List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViewList;

    private List<BorrowingType> borrowingTypeList;
    private List<BAPaymentMethod> baPaymentMethodList;

    private List<String> yearList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    //Dialog
    private BasicInfoAccountView basicInfoAccountView;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private BasicInfoAccountView selectAccount;
    private int rowIndex;

    private String messageHeader;
    private String message;

    //session
    private long workCaseId;
    private User user;

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

    //for readOnly
    private boolean disApplicationNo;
    private boolean disRefApplicationNo;
    private boolean disCaNo;
    private boolean disRequestType;
    private boolean disProductGroup;
    private boolean disUnpaidFeeInsurance;
    private boolean disNoPendingClaimLG;
    private boolean disIsConstructionRequestLG;
    private boolean disIsAbleToGettingGuarantorJob;
    private boolean disNoClaimLGHistory;
    private boolean disNoRevokedLicense;
    private boolean disNoLateWorkDelivery;
    private boolean disIsAdequateOfCapitalResource;
    private boolean disIsApplySpecialProgram;
    private boolean disSpecialProgramValue;
    private boolean disIsRefinanceIN;
    private boolean disRefinanceInValue;
    private boolean disIsRefinanceOUT;
    private boolean disRefinanceOutValue;
    private boolean disRiskCustomerType;
    private boolean disQualitativeType;
    private boolean disIsExistingSMECustomer;
    private boolean disExistingSMECustomerSince;
    private boolean disLastReviewDate;
    private boolean disExtendedReviewDate;
    private boolean disSCFScore;
    private boolean disRequestLoanWithSameName;
    private boolean disHaveLoanInOneYear;
    private boolean disPassAnnualReview;
    private boolean disLoanRequestPattern;
    private boolean disReferralName;
    private boolean disReferralID;
    private boolean disIsApplyBA;
    private boolean disBaPaymentMethod;

    //tmp for radio button
    private int tmpSpecialProgram;
    private int tmpRefinanceIN;
    private int tmpRefinanceOUT;
    private int tmpApplyBA;

    public BasicInfo(){
    }

    public void preRender(){
        /*HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 101);
        session.setAttribute("stepId", 1004);*/

        log.info("preRender ::: setSession ");

        HttpSession session = FacesUtil.getSession(true);

        user = getCurrentUser();

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("preRender ::: workCaseId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        preRender();

        List<FieldsControlView> fieldsControlViewList = initialCreation(Screen.BASIC_INFO);
        fieldsControl(fieldsControlViewList);

        basicInfoView = new BasicInfoView();

        productGroupList = productGroupDAO.findAll();
        specialProgramList = specialProgramDAO.findAll();
        requestTypeList = requestTypeDAO.findAll();
        riskTypeList = riskTypeDAO.findAll();
        sbfScoreList = sbfScoreDAO.findAll();
        bankList = bankDAO.getListRefinance();

        bankAccountTypeList = bankAccountTypeDAO.findOpenAccountType();
        openAccountProductList = new ArrayList<OpenAccountProduct>();

        openAccountPurposeList = openAccountPurposeDAO.findAll();
        basicInfoAccountPurposeViewList = new ArrayList<BasicInfoAccountPurposeView>();
        for(OpenAccountPurpose oap : openAccountPurposeList){
            BasicInfoAccountPurposeView purposeView = new BasicInfoAccountPurposeView();
            purposeView.setPurpose(oap);
            basicInfoAccountPurposeViewList.add(purposeView);
        }

        CustomerEntity customerEntity = basicInfoControl.getCustomerEntityByWorkCaseId(workCaseId);

        borrowingTypeList = borrowingTypeDAO.findByCustomerEntity(customerEntity);

        baPaymentMethodList = baPaymentMethodDAO.findAll();

        if(baPaymentMethodList != null && baPaymentMethodList.size() > 0){
            basicInfoView.setBaPaymentMethod(baPaymentMethodList.get(0));
        }

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

        disQualitativeType = false;
        if(!customerEntity.isChangeQualtiEnable()){
            disQualitativeType = true;
        }

        basicInfoAccountView = new BasicInfoAccountView();

        yearList = DateTimeUtil.getPreviousFiftyYearTH();

        onChangeSpecialProgram();
        onChangeRefIn();
        onChangeRefOut();
        onChangeExistingSME();
        onChangeBA();
        onChangeReqLG();
    }

    public void fieldsControl(List<FieldsControlView> fieldsControlViewList){
        if(fieldsControlViewList != null && fieldsControlViewList.size() > 0){
            for(FieldsControlView fcv : fieldsControlViewList){
                if(fcv.getFieldName().equalsIgnoreCase("applicationNo")){
                    reqApplicationNo = fcv.isMandate();
                    disApplicationNo = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("refApplicationNo")){
                    reqRefApplicationNo = fcv.isMandate();
                    disRefApplicationNo = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("caNo")){
                    reqCaNo = fcv.isMandate();
                    disCaNo = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("requestType")){
                    reqRequestType = fcv.isMandate();
                    disRequestType = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("productGroup")){
                    reqProductGroup = fcv.isMandate();
                    disProductGroup = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("unpaidFeeInsurance")){
                    reqUnpaidFeeInsurance = fcv.isMandate();
                    disUnpaidFeeInsurance = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("noPendingClaimLG")){
                    reqNoPendingClaimLG = fcv.isMandate();
                    disNoPendingClaimLG = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isConstructionRequestLG")){
                    reqIsConstructionRequestLG = fcv.isMandate();
                    disIsConstructionRequestLG = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isAbleToGettingGuarantorJob")){
                    reqIsAbleToGettingGuarantorJob = fcv.isMandate();
                    disIsAbleToGettingGuarantorJob = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("noClaimLGHistory")){
                    reqNoClaimLGHistory = fcv.isMandate();
                    disNoClaimLGHistory = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("noRevokedLicense")){
                    reqNoRevokedLicense = fcv.isMandate();
                    disNoRevokedLicense = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("noLateWorkDelivery")){
                    reqNoLateWorkDelivery = fcv.isMandate();
                    disNoLateWorkDelivery = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isAdequateOfCapitalResource")){
                    reqIsAdequateOfCapitalResource = fcv.isMandate();
                    disIsAdequateOfCapitalResource = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isApplySpecialProgram")){
                    reqIsApplySpecialProgram = fcv.isMandate();
                    disIsApplySpecialProgram = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("specialProgramValue")){
                    reqSpecialProgramValue = fcv.isMandate();
                    disSpecialProgramValue = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isRefinanceIN")){
                    reqIsRefinanceIN = fcv.isMandate();
                    disIsRefinanceIN = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("refinanceInValue")){
                    reqRefinanceInValue = fcv.isMandate();
                    disRefinanceInValue = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isRefinanceOUT")){
                    reqIsRefinanceOUT = fcv.isMandate();
                    disIsRefinanceOUT = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("refinanceOutValue")){
                    reqRefinanceOutValue = fcv.isMandate();
                    disRefinanceOutValue = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("riskCustomerType")){
                    reqRiskCustomerType = fcv.isMandate();
                    disRiskCustomerType = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("qualitativeType")){
                    reqQualitativeType = fcv.isMandate();
                    disQualitativeType = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isExistingSMECustomer")){
                    reqIsExistingSMECustomer = fcv.isMandate();
                    disIsExistingSMECustomer = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("existingSMECustomerSince")){
                    reqExistingSMECustomerSince = fcv.isMandate();
                    disExistingSMECustomerSince = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("lastReviewDate")){
                    reqLastReviewDate = fcv.isMandate();
                    disLastReviewDate = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("extendedReviewDate")){
                    reqExtendedReviewDate = fcv.isMandate();
                    disExtendedReviewDate = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("SCFScore")){
                    reqSCFScore = fcv.isMandate();
                    disSCFScore = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("requestLoanWithSameName")){
                    reqRequestLoanWithSameName = fcv.isMandate();
                    disRequestLoanWithSameName = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("haveLoanInOneYear")){
                    reqHaveLoanInOneYear = fcv.isMandate();
                    disHaveLoanInOneYear = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("passAnnualReview")){
                    reqPassAnnualReview = fcv.isMandate();
                    disPassAnnualReview = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("loanRequestPattern")){
                    reqLoanRequestPattern = fcv.isMandate();
                    disLoanRequestPattern = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("referralName")){
                    reqReferralName = fcv.isMandate();
                    disReferralName = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("referralID")){
                    reqReferralID = fcv.isMandate();
                    disReferralID = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("isApplyBA")){
                    reqIsApplyBA = fcv.isMandate();
                    disIsApplyBA = fcv.isReadOnly();
                }
                if(fcv.getFieldName().equalsIgnoreCase("baPaymentMethod")){
                    reqBaPaymentMethod = fcv.isMandate();
                    disBaPaymentMethod = fcv.isReadOnly();
                }
            }
        }
    }

    public void onAddAccount(){
        basicInfoAccountView = new BasicInfoAccountView();

        bankAccountTypeList = bankAccountTypeDAO.findOpenAccountType();

        openAccountProductList = new ArrayList<OpenAccountProduct>();

        openAccountPurposeList = openAccountPurposeDAO.findAll();
        basicInfoAccountPurposeViewList = new ArrayList<BasicInfoAccountPurposeView>();
        for(OpenAccountPurpose oap : openAccountPurposeList){
            BasicInfoAccountPurposeView purposeView = new BasicInfoAccountPurposeView();
            purposeView.setPurpose(oap);
            basicInfoAccountPurposeViewList.add(purposeView);
        }

        modeForButton = ModeForButton.ADD;
    }

    public void onEditAccount(){
        basicInfoAccountView = new BasicInfoAccountView();
        basicInfoAccountView = selectAccount;
        onChangeAccountType();
        for(BasicInfoAccountPurposeView biapv : basicInfoAccountView.getBasicInfoAccountPurposeView()){
            if(biapv.isSelected()){
                for(BasicInfoAccountPurposeView purposeView : basicInfoAccountPurposeViewList){
                    if(biapv.getPurpose().getName().equals(purposeView.getPurpose().getName())){
                        purposeView.setSelected(true);
                    }
                }
            }
        }
        modeForButton = ModeForButton.EDIT;
    }

    public void addAccount(){
        if(basicInfoAccountView.getBankAccountTypeView().getId() != 0){

            basicInfoAccountView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(bankAccountTypeDAO.findById(basicInfoAccountView.getBankAccountTypeView().getId())));
        }else{
            basicInfoAccountView.getBankAccountTypeView().setName("-");
        }

        if(basicInfoAccountView.getProduct().getId() != 0){
            basicInfoAccountView.setProduct(openAccountProductDAO.findById(basicInfoAccountView.getProduct().getId()));
        }else{
            basicInfoAccountView.getProduct().setName("-");
        }

        StringBuilder stringBuilder = new StringBuilder();

        basicInfoAccountView.setBasicInfoAccountPurposeView(new ArrayList<BasicInfoAccountPurposeView>());
        for(BasicInfoAccountPurposeView bia : basicInfoAccountPurposeViewList){
            if(bia.isSelected()){
                if(basicInfoAccountView.getBasicInfoAccountPurposeView().size() == 0){
                    basicInfoAccountView.getBasicInfoAccountPurposeView().add(bia);
                    stringBuilder.append(bia.getPurpose().getName());
                }else{
                    basicInfoAccountView.getBasicInfoAccountPurposeView().add(bia);
                    stringBuilder.append(", "+bia.getPurpose().getName());
                }
            }
        }

        if(!stringBuilder.toString().isEmpty()){
            basicInfoAccountView.setPurposeForShow(stringBuilder.toString());
        }else{
            basicInfoAccountView.setPurposeForShow("-");
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            basicInfoView.getBasicInfoAccountViews().add(basicInfoAccountView);
        }else{
            basicInfoView.getBasicInfoAccountViews().set(rowIndex,basicInfoAccountView);
        }

        boolean complete = true;        //Change only failed to save
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteAccount() {
        basicInfoView.getBasicInfoAccountViews().remove(selectAccount);
    }

    public void onSave(){
        try{
            basicInfoControl.saveBasicInfo(basicInfoView, workCaseId, user);
            messageHeader = "Save Basic Info Success.";
            message = "Save Basic Info data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Basic Info Failed.";
            if(ex.getCause() != null){
                message = "Save Basic Info data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
        }
    }

    public void onChangeSpecialProgram(){
        if(tmpSpecialProgram == basicInfoView.getSpProgram()){
            basicInfoView.setSpProgram(0);
            tmpSpecialProgram = 0;
        } else {
            tmpSpecialProgram = basicInfoView.getSpProgram();
        }

        if(basicInfoView.getSpProgram() == 2){ // yes
            reqSpecialProgramValue = true;
            disSpecialProgramValue = false;
        } else {
            reqSpecialProgramValue = false;
            disSpecialProgramValue = true;
        }
    }

    public void onChangeRefIn(){
        if(tmpRefinanceIN == basicInfoView.getRefIn()){
            basicInfoView.setRefIn(0);
            tmpRefinanceIN = 0;
        } else {
            tmpRefinanceIN = basicInfoView.getRefIn();
        }

        if(basicInfoView.getRefIn() == 2){ // yes
            reqRefinanceInValue = true;
            disRefinanceInValue = false;
        } else {
            reqRefinanceInValue = false;
            disRefinanceInValue = true;
        }
    }

    public void onChangeRefOut(){
        if(tmpRefinanceOUT == basicInfoView.getRefOut()){
            basicInfoView.setRefOut(0);
            tmpRefinanceOUT = 0;
        } else {
            tmpRefinanceOUT = basicInfoView.getRefOut();
        }

        if(basicInfoView.getRefOut() == 2){ // yes
            reqRefinanceOutValue = true;
            disRefinanceOutValue = false;
        } else {
            reqRefinanceOutValue = false;
            disRefinanceOutValue = true;
        }
    }

    public void onChangeExistingSME(){
        if(basicInfoView.getExistingSME() == 2){ // yes
            reqExistingSMECustomerSince = true;
            disExistingSMECustomerSince = false;
        } else {
            reqExistingSMECustomerSince = false;
            disExistingSMECustomerSince = true;
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
            basicInfoView.getBaPaymentMethod().setId(0);
        }else{
            if(baPaymentMethodList != null && baPaymentMethodList.size() > 0){
                basicInfoView.getBaPaymentMethod().setId(baPaymentMethodList.get(0).getId());
            }else{
                basicInfoView.getBaPaymentMethod().setId(0);
            }
        }

        if(basicInfoView.getApplyBA() == 2){ // yes
            reqBaPaymentMethod = true;
            disBaPaymentMethod = false;
        } else {
            reqBaPaymentMethod = false;
            disBaPaymentMethod = true;
        }
    }

    public void onChangeReqLG(){
        if(basicInfoView.isCharFCLG()){
            disIsAbleToGettingGuarantorJob = false;
            disNoClaimLGHistory = false;
            disNoRevokedLicense = false;
            disNoLateWorkDelivery = false;
            disIsAdequateOfCapitalResource = false;
        } else {
            disIsAbleToGettingGuarantorJob = true;
            disNoClaimLGHistory = true;
            disNoRevokedLicense = true;
            disNoLateWorkDelivery = true;
            disIsAdequateOfCapitalResource = true;
        }
    }

    public void onChangeAccountType(){
        openAccountProductList = openAccountProductDAO.findByBankAccountTypeId(basicInfoAccountView.getBankAccountTypeView().getId());
    }

    public void onRefreshInterfaceInfo(){
        try{
            messageHeader = "Refresh Interface Info complete.";
            message = "Refresh Interface Info complete.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            log.debug("refreshInterfaceInfo Exception : {}", ex);
            messageHeader = "Refresh Interface Info Failed.";
            message = ex.getMessage();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onDuplicateApplication(){
        try{
            messageHeader = "Duplicate Application complete.";
            message = "Duplicate Application complete.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            log.debug("duplicateApplication Exception : {}", ex);
            messageHeader = "Duplicate Application Failed.";
            message = ex.getMessage();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
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

    public BasicInfoAccountView getBasicInfoAccountView() {
        return basicInfoAccountView;
    }

    public void setBasicInfoAccountView(BasicInfoAccountView basicInfoAccountView) {
        this.basicInfoAccountView = basicInfoAccountView;
    }

    public List<BankAccountType> getBankAccountTypeList() {
        return bankAccountTypeList;
    }

    public void setBankAccountTypeList(List<BankAccountType> bankAccountTypeList) {
        this.bankAccountTypeList = bankAccountTypeList;
    }

    public List<OpenAccountProduct> getOpenAccountProductList() {
        return openAccountProductList;
    }

    public void setOpenAccountProductList(List<OpenAccountProduct> openAccountProductList) {
        this.openAccountProductList = openAccountProductList;
    }

    public List<BasicInfoAccountPurposeView> getBasicInfoAccountPurposeViewList() {
        return basicInfoAccountPurposeViewList;
    }

    public void setBasicInfoAccountPurposeViewList(List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViewList) {
        this.basicInfoAccountPurposeViewList = basicInfoAccountPurposeViewList;
    }

    public List<SBFScore> getSbfScoreList() {
        return sbfScoreList;
    }

    public void setSbfScoreList(List<SBFScore> sbfScoreList) {
        this.sbfScoreList = sbfScoreList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public BasicInfoAccountView getSelectAccount() {
        return selectAccount;
    }

    public void setSelectAccount(BasicInfoAccountView selectAccount) {
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

    public List<BAPaymentMethod> getBaPaymentMethodList() {
        return baPaymentMethodList;
    }

    public void setBaPaymentMethodList(List<BAPaymentMethod> baPaymentMethodList) {
        this.baPaymentMethodList = baPaymentMethodList;
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

    public boolean isDisApplicationNo() {
        return disApplicationNo;
    }

    public void setDisApplicationNo(boolean disApplicationNo) {
        this.disApplicationNo = disApplicationNo;
    }

    public boolean isDisRefApplicationNo() {
        return disRefApplicationNo;
    }

    public void setDisRefApplicationNo(boolean disRefApplicationNo) {
        this.disRefApplicationNo = disRefApplicationNo;
    }

    public boolean isDisCaNo() {
        return disCaNo;
    }

    public void setDisCaNo(boolean disCaNo) {
        this.disCaNo = disCaNo;
    }

    public boolean isDisRequestType() {
        return disRequestType;
    }

    public void setDisRequestType(boolean disRequestType) {
        this.disRequestType = disRequestType;
    }

    public boolean isDisProductGroup() {
        return disProductGroup;
    }

    public void setDisProductGroup(boolean disProductGroup) {
        this.disProductGroup = disProductGroup;
    }

    public boolean isDisUnpaidFeeInsurance() {
        return disUnpaidFeeInsurance;
    }

    public void setDisUnpaidFeeInsurance(boolean disUnpaidFeeInsurance) {
        this.disUnpaidFeeInsurance = disUnpaidFeeInsurance;
    }

    public boolean isDisNoPendingClaimLG() {
        return disNoPendingClaimLG;
    }

    public void setDisNoPendingClaimLG(boolean disNoPendingClaimLG) {
        this.disNoPendingClaimLG = disNoPendingClaimLG;
    }

    public boolean isDisIsConstructionRequestLG() {
        return disIsConstructionRequestLG;
    }

    public void setDisIsConstructionRequestLG(boolean disIsConstructionRequestLG) {
        this.disIsConstructionRequestLG = disIsConstructionRequestLG;
    }

    public boolean isDisIsAbleToGettingGuarantorJob() {
        return disIsAbleToGettingGuarantorJob;
    }

    public void setDisIsAbleToGettingGuarantorJob(boolean disIsAbleToGettingGuarantorJob) {
        this.disIsAbleToGettingGuarantorJob = disIsAbleToGettingGuarantorJob;
    }

    public boolean isDisNoClaimLGHistory() {
        return disNoClaimLGHistory;
    }

    public void setDisNoClaimLGHistory(boolean disNoClaimLGHistory) {
        this.disNoClaimLGHistory = disNoClaimLGHistory;
    }

    public boolean isDisNoRevokedLicense() {
        return disNoRevokedLicense;
    }

    public void setDisNoRevokedLicense(boolean disNoRevokedLicense) {
        this.disNoRevokedLicense = disNoRevokedLicense;
    }

    public boolean isDisNoLateWorkDelivery() {
        return disNoLateWorkDelivery;
    }

    public void setDisNoLateWorkDelivery(boolean disNoLateWorkDelivery) {
        this.disNoLateWorkDelivery = disNoLateWorkDelivery;
    }

    public boolean isDisIsAdequateOfCapitalResource() {
        return disIsAdequateOfCapitalResource;
    }

    public void setDisIsAdequateOfCapitalResource(boolean disIsAdequateOfCapitalResource) {
        this.disIsAdequateOfCapitalResource = disIsAdequateOfCapitalResource;
    }

    public boolean isDisIsApplySpecialProgram() {
        return disIsApplySpecialProgram;
    }

    public void setDisIsApplySpecialProgram(boolean disIsApplySpecialProgram) {
        this.disIsApplySpecialProgram = disIsApplySpecialProgram;
    }

    public boolean isDisSpecialProgramValue() {
        return disSpecialProgramValue;
    }

    public void setDisSpecialProgramValue(boolean disSpecialProgramValue) {
        this.disSpecialProgramValue = disSpecialProgramValue;
    }

    public boolean isDisIsRefinanceIN() {
        return disIsRefinanceIN;
    }

    public void setDisIsRefinanceIN(boolean disIsRefinanceIN) {
        this.disIsRefinanceIN = disIsRefinanceIN;
    }

    public boolean isDisRefinanceInValue() {
        return disRefinanceInValue;
    }

    public void setDisRefinanceInValue(boolean disRefinanceInValue) {
        this.disRefinanceInValue = disRefinanceInValue;
    }

    public boolean isDisIsRefinanceOUT() {
        return disIsRefinanceOUT;
    }

    public void setDisIsRefinanceOUT(boolean disIsRefinanceOUT) {
        this.disIsRefinanceOUT = disIsRefinanceOUT;
    }

    public boolean isDisRefinanceOutValue() {
        return disRefinanceOutValue;
    }

    public void setDisRefinanceOutValue(boolean disRefinanceOutValue) {
        this.disRefinanceOutValue = disRefinanceOutValue;
    }

    public boolean isDisRiskCustomerType() {
        return disRiskCustomerType;
    }

    public void setDisRiskCustomerType(boolean disRiskCustomerType) {
        this.disRiskCustomerType = disRiskCustomerType;
    }

    public boolean isDisQualitativeType() {
        return disQualitativeType;
    }

    public void setDisQualitativeType(boolean disQualitativeType) {
        this.disQualitativeType = disQualitativeType;
    }

    public boolean isDisIsExistingSMECustomer() {
        return disIsExistingSMECustomer;
    }

    public void setDisIsExistingSMECustomer(boolean disIsExistingSMECustomer) {
        this.disIsExistingSMECustomer = disIsExistingSMECustomer;
    }

    public boolean isDisExistingSMECustomerSince() {
        return disExistingSMECustomerSince;
    }

    public void setDisExistingSMECustomerSince(boolean disExistingSMECustomerSince) {
        this.disExistingSMECustomerSince = disExistingSMECustomerSince;
    }

    public boolean isDisLastReviewDate() {
        return disLastReviewDate;
    }

    public void setDisLastReviewDate(boolean disLastReviewDate) {
        this.disLastReviewDate = disLastReviewDate;
    }

    public boolean isDisExtendedReviewDate() {
        return disExtendedReviewDate;
    }

    public void setDisExtendedReviewDate(boolean disExtendedReviewDate) {
        this.disExtendedReviewDate = disExtendedReviewDate;
    }

    public boolean isDisSCFScore() {
        return disSCFScore;
    }

    public void setDisSCFScore(boolean disSCFScore) {
        this.disSCFScore = disSCFScore;
    }

    public boolean isDisRequestLoanWithSameName() {
        return disRequestLoanWithSameName;
    }

    public void setDisRequestLoanWithSameName(boolean disRequestLoanWithSameName) {
        this.disRequestLoanWithSameName = disRequestLoanWithSameName;
    }

    public boolean isDisHaveLoanInOneYear() {
        return disHaveLoanInOneYear;
    }

    public void setDisHaveLoanInOneYear(boolean disHaveLoanInOneYear) {
        this.disHaveLoanInOneYear = disHaveLoanInOneYear;
    }

    public boolean isDisPassAnnualReview() {
        return disPassAnnualReview;
    }

    public void setDisPassAnnualReview(boolean disPassAnnualReview) {
        this.disPassAnnualReview = disPassAnnualReview;
    }

    public boolean isDisLoanRequestPattern() {
        return disLoanRequestPattern;
    }

    public void setDisLoanRequestPattern(boolean disLoanRequestPattern) {
        this.disLoanRequestPattern = disLoanRequestPattern;
    }

    public boolean isDisReferralName() {
        return disReferralName;
    }

    public void setDisReferralName(boolean disReferralName) {
        this.disReferralName = disReferralName;
    }

    public boolean isDisReferralID() {
        return disReferralID;
    }

    public void setDisReferralID(boolean disReferralID) {
        this.disReferralID = disReferralID;
    }

    public boolean isDisIsApplyBA() {
        return disIsApplyBA;
    }

    public void setDisIsApplyBA(boolean disIsApplyBA) {
        this.disIsApplyBA = disIsApplyBA;
    }

    public boolean isDisBaPaymentMethod() {
        return disBaPaymentMethod;
    }

    public void setDisBaPaymentMethod(boolean disBaPaymentMethod) {
        this.disBaPaymentMethod = disBaPaymentMethod;
    }
}
