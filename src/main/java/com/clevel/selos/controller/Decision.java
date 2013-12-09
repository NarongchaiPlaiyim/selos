package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.dao.master.DisbursementDAO;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.DecisionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.DecisionTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.ValidationUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "decision")
public class Decision implements Serializable {
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
    DecisionControl decisionControl;

    //DAO
    @Inject
    DecisionDAO decisionDAO;
    @Inject
    CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    BaseRateDAO baseRateDAO;
    @Inject
    DisbursementDAO disbursementDAO;
    @Inject
    CustomerDAO customerDAO;

    //Transform
    @Inject
    DecisionTransform decisionTransform;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    private DecisionView decisionView;

    // View for Approved
    private List<NewCreditDetailView> approvedProposeCreditList;
    private List<NewCollateralInfoView> approvedProposeCollateralList;
    private List<NewGuarantorDetailView> approvedProposeGuarantorList;
    private BigDecimal approvedTotalCredit;
    private BigDecimal approvedTotalCommercial;
    private BigDecimal approvedTotalComAndOBOD;
    private BigDecimal approvedTotalExposure;
    private BigDecimal approvedTotalNumOfNewOD;
    private BigDecimal approvedTotalNumProposeCreditFac;
    private BigDecimal approvedTotalNumContingentPropose;

    private BigDecimal grdTotalNumOfCoreAsset;
    private BigDecimal grdTotalNumOfNonCoreAsset;

    private BigDecimal approvedTotalGuaranteeAmt;
    private BigDecimal approvedTotalTCGGuaranteeAmt;
    private BigDecimal approvedTotalIndvGuaranteeAmt;
    private BigDecimal approvedTotalJurisGuaranteeAmt;

    // View Selected for Add/Edit/Delete
    private NewCreditDetailView selectedAppProposeCredit;
    private NewCollateralInfoView selectedAppProposeCollateral;
    private NewGuarantorDetailView selectedAppProposeGuarantor;

    // Select Items List
    // Retrieve Price/Fee
    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;
    // Propose Credit Dialog
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<BaseRate> baseRateList;
    private List<Disbursement> disbursementList;
    // Propose Guarantor Dialog
    private List<Customer> guarantorList;

    private List<FollowUpConditionView> followUpConditionViewList;
    private FollowUpConditionView followUpConditionView;

    // Mode
    enum ModeForButton {ADD, EDIT}
    private ModeForButton modeForButton;
    private boolean modeEdit;
    private boolean modeEditCollateral;
    private boolean modeEditGuarantor;

    public Decision() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 2l);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        } else {
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            } catch (Exception e) {
                log.info("Exception :: {}", e);
            }
        }
    }

    private void initSelectList() {
        // Propose Credit
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        baseRateList = baseRateDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        // Propose Guarantor
        guarantorList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
    }

    private void initialProposeView() {
        selectedAppProposeCredit = new NewCreditDetailView();
        selectedAppProposeCredit.setProductProgram(new ProductProgram());
        selectedAppProposeCredit.setCreditType(new CreditType());
        if (baseRateList != null && !baseRateList.isEmpty()) {
            selectedAppProposeCredit.setStandardBasePrice(baseRateList.get(0));
            selectedAppProposeCredit.setSuggestBasePrice(baseRateList.get(0));
        } else {
            selectedAppProposeCredit.setStandardBasePrice(new BaseRate());
            selectedAppProposeCredit.setSuggestBasePrice(new BaseRate());
        }
        if (disbursementList != null && !disbursementList.isEmpty()) {
            selectedAppProposeCredit.setDisbursement(disbursementList.get(0));
        } else {
            selectedAppProposeCredit.setDisbursement(new Disbursement());
        }

    }

    @PostConstruct
    public void onCreation() {
        initSelectList();
        initialProposeView();

        //todo: find ExistingCredit and creditFacPropose by workCaseId
//        decisionView = decisionTransform.getDecisionView(decisionDAO.findByWorkCaseId(workCaseId));
        decisionView = new DecisionView();

        // -------------------- Common Object -------------------- //
        BankAccountStatusView bankAccountStatusView = new BankAccountStatusView();
        bankAccountStatusView.setCode("01");
        bankAccountStatusView.setDescription("Normal");

        PotentialCollateral potentialCollateral = new PotentialCollateral();
        potentialCollateral.setName("Core Asset");
        potentialCollateral.setDescription("Core Asset");

        CollateralType collateralType = new CollateralType();
        collateralType.setCode("1");
        collateralType.setDescription("Land and Building");

        Relation relation = new Relation();
        relation.setDescription("Borrower");

        ProductProgram productProgram = new ProductProgram();
        productProgram.setName("TMB SME SmartBiz");
        productProgram.setDescription("TMB SME SmartBiz");

        CreditType creditType = new CreditType();
        creditType.setDescription("Loan");

        LoanPurpose loanPurpose = new LoanPurpose();
        loanPurpose.setDescription("Loan Purpose Example");

        Disbursement disbursement = new Disbursement();
        disbursement.setDisbursement("Normal Disbursement");

        List<SplitLineDetailView> splitLineList = new ArrayList<SplitLineDetailView>();
        SplitLineDetailView splitLineDetail_1 = new SplitLineDetailView();
        splitLineDetail_1.setProductProgram(productProgram);
        splitLineDetail_1.setLimit(BigDecimal.valueOf(34220000));
        splitLineList.add(splitLineDetail_1);

        CreditTypeDetailView creditTypeDetailView1 = new CreditTypeDetailView();
        creditTypeDetailView1.setAccount("Mr.A Example");
        creditTypeDetailView1.setType("New");
        creditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView1.setCreditFacility("Loan");
        creditTypeDetailView1.setLimit(BigDecimal.valueOf(123456.78));

        CreditTypeDetailView creditTypeDetailView2 = new CreditTypeDetailView();
        creditTypeDetailView2.setAccount("Mr.B Example");
        creditTypeDetailView2.setType("Change");
        creditTypeDetailView2.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView2.setCreditFacility("Loan");
        creditTypeDetailView2.setLimit(BigDecimal.valueOf(123456.78));

        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        creditTypeDetailViewList.add(creditTypeDetailView1);
        creditTypeDetailViewList.add(creditTypeDetailView2);

        SubCollateralType subCollateralType = new SubCollateralType();
        subCollateralType.setDescription("Land and House");
        subCollateralType.setCollateralType(collateralType);

        List<String> collateralOwnerUWList = new ArrayList<String>();
        collateralOwnerUWList.add("Mr.A Example");
        collateralOwnerUWList.add("Ms.B Example");

        MortgageType mortgageType1 = new MortgageType();
        mortgageType1.setMortgage("Mortgage type 1");

        MortgageType mortgageType2 = new MortgageType();
        mortgageType2.setMortgage("Mortgage type 2");

        List<MortgageType> mortgageTypeList = new ArrayList<MortgageType>();
        mortgageTypeList.add(mortgageType1);
        mortgageTypeList.add(mortgageType2);

        List<String> relatedWithList = new ArrayList<String>();
        relatedWithList.add("Related with C");
        relatedWithList.add("Related with D");

        Customer guarantor1 = new Customer();
        guarantor1.setNameTh("Guarantor1");
        guarantor1.setLastNameTh("LastName1");

        Customer guarantor2 = new Customer();
        guarantor2.setNameTh("Guarantor2");
        guarantor2.setLastNameTh("LastName2");

        //========================================= Existing =========================================//
        ExistingCreditDetailView existingCreditDetail_1 = new ExistingCreditDetailView();
        existingCreditDetail_1.setAccountName("Mr.A Example");
        existingCreditDetail_1.setAccountNumber("012-3-45678-9");
        existingCreditDetail_1.setAccountSuf("001");
        existingCreditDetail_1.setAccountStatusID(1);
        existingCreditDetail_1.setAccountStatus(bankAccountStatusView);
        existingCreditDetail_1.setProductProgram("SmartBiz");
        existingCreditDetail_1.setCreditType("Loan");
        existingCreditDetail_1.setProductCode("EAC1");
        existingCreditDetail_1.setProjectCode("90074");
        existingCreditDetail_1.setLimit(BigDecimal.valueOf(123456789));
        existingCreditDetail_1.setNotional(BigDecimal.valueOf(10000000));
        existingCreditDetail_1.setPcePercent(BigDecimal.valueOf(10));
        existingCreditDetail_1.setPceLimit(BigDecimal.valueOf(100000));
        existingCreditDetail_1.setOutstanding(BigDecimal.valueOf(9600000));
        existingCreditDetail_1.setInstallment(BigDecimal.valueOf(200000));
        existingCreditDetail_1.setIntFeePercent(BigDecimal.valueOf(2.00));
        existingCreditDetail_1.setTenor(BigDecimal.valueOf(48));
        existingCreditDetail_1.setSplitLineDetailViewList(splitLineList);

        BigDecimal extTotalLimit = existingCreditDetail_1.getLimit();

        // Borrower
        List<ExistingCreditDetailView> extBorrowerComCreditList = new ArrayList<ExistingCreditDetailView>();
        extBorrowerComCreditList.add(existingCreditDetail_1);
        decisionView.setExtBorrowerComCreditList(extBorrowerComCreditList);
        decisionView.setExtBorrowerTotalComLimit(extTotalLimit);

        List<ExistingCreditDetailView> extBorrowerRetailCreditList = new ArrayList<ExistingCreditDetailView>();
        extBorrowerRetailCreditList.add(existingCreditDetail_1);
        decisionView.setExtBorrowerRetailCreditList(extBorrowerRetailCreditList);
        decisionView.setExtBorrowerTotalRetailLimit(extTotalLimit);

        List<ExistingCreditDetailView> extBorrowerAppInRLOSList = new ArrayList<ExistingCreditDetailView>();
        extBorrowerAppInRLOSList.add(existingCreditDetail_1);
        decisionView.setExtBorrowerAppInRLOSList(extBorrowerAppInRLOSList);
        decisionView.setExtBorrowerTotalAppInRLOSLimit(extTotalLimit);

        // Related Person
        List<ExistingCreditDetailView> extRelatedComCreditList = new ArrayList<ExistingCreditDetailView>();
        extRelatedComCreditList.add(existingCreditDetail_1);
        decisionView.setExtRelatedComCreditList(extRelatedComCreditList);
        decisionView.setExtRelatedTotalComLimit(extTotalLimit);

        List<ExistingCreditDetailView> extRelatedRetailCreditList = new ArrayList<ExistingCreditDetailView>();
        extRelatedRetailCreditList.add(existingCreditDetail_1);
        decisionView.setExtRelatedRetailCreditList(extRelatedRetailCreditList);
        decisionView.setExtRelatedTotalRetailLimit(extTotalLimit);

        List<ExistingCreditDetailView> extRelatedAppInRLOSList = new ArrayList<ExistingCreditDetailView>();
        extRelatedAppInRLOSList.add(existingCreditDetail_1);
        decisionView.setExtRelatedAppInRLOSList(extRelatedAppInRLOSList);
        decisionView.setExtRelatedTotalAppInRLOSLimit(extTotalLimit);

        // Collateral
        ExistingCollateralDetailView existingCollateralDetail = new ExistingCollateralDetailView();
        existingCollateralDetail.setPotentialCollateral(potentialCollateral);
        existingCollateralDetail.setCollateralType(collateralType);
        existingCollateralDetail.setOwner("Mr.A Example");
        existingCollateralDetail.setRelation(relation);
        existingCollateralDetail.setAppraisalDate(new Date());
        existingCollateralDetail.setCollateralNumber("A.888");
        existingCollateralDetail.setCollateralLocation("209 Dindaeng Bangkok 10400");
        existingCollateralDetail.setRemark("remark example");
        existingCollateralDetail.setCusName("Mr.A Example");
        existingCollateralDetail.setAccountNumber("012-3-45678-9");
        existingCollateralDetail.setAccountSuffix("001");
        existingCollateralDetail.setProductProgram("SmartBiz");
        existingCollateralDetail.setLimit(BigDecimal.valueOf(123456789));
        existingCollateralDetail.setMortgageType(new MortgageType());
        existingCollateralDetail.setAppraisalValue(BigDecimal.valueOf(9000000));
        existingCollateralDetail.setMortgageValue(BigDecimal.valueOf(12000000));

        BigDecimal extTotalAppraisalValue = existingCollateralDetail.getAppraisalValue();
        BigDecimal extTotalMortgageValue = existingCollateralDetail.getMortgageValue();

        List<ExistingCollateralDetailView> extBorrowerCollateralList = new ArrayList<ExistingCollateralDetailView>();
        extBorrowerCollateralList.add(existingCollateralDetail);
        decisionView.setExtBorrowerCollateralList(extBorrowerCollateralList);
        decisionView.setExtBorrowerTotalAppraisalValue(extTotalAppraisalValue);
        decisionView.setExtBorrowerTotalMortgageValue(extTotalMortgageValue);

        List<ExistingCollateralDetailView> extRelatedCollateralList = new ArrayList<ExistingCollateralDetailView>();
        extRelatedCollateralList.add(existingCollateralDetail);
        decisionView.setExtRelatedCollateralList(extRelatedCollateralList);
        decisionView.setExtRelatedTotalAppraisalValue(extTotalAppraisalValue);
        decisionView.setExtRelatedTotalMortgageValue(extTotalMortgageValue);

        // Guarantor
        ExistingGuarantorDetailView existingGuarantor = new ExistingGuarantorDetailView();
        existingGuarantor.setGuarantorName("ABC Co., Ltd.");
        existingGuarantor.setTcgLgNo("12-34567");

        List<ExistingCreditDetailView> existingCreditFacilityList = new ArrayList<ExistingCreditDetailView>();
        existingCreditFacilityList.add(existingCreditDetail_1);
        existingCreditFacilityList.add(existingCreditDetail_1);


        ExistingCreditTypeDetailView existingCreditTypeDetailView1 = new ExistingCreditTypeDetailView();
        existingCreditTypeDetailView1.setAccountName("Mr.A Example");
        existingCreditTypeDetailView1.setType("New");
        existingCreditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        existingCreditTypeDetailView1.setCreditFacility("Loan");
        existingCreditTypeDetailView1.setLimit(BigDecimal.valueOf(123456.78));

        ExistingCreditTypeDetailView existingCreditTypeDetailView2 = new ExistingCreditTypeDetailView();
        existingCreditTypeDetailView2.setAccountName("Mr.B Example");
        existingCreditTypeDetailView2.setType("Change");
        existingCreditTypeDetailView2.setProductProgram("TMB SME SmartBiz");
        existingCreditTypeDetailView2.setCreditFacility("Loan");
        existingCreditTypeDetailView2.setLimit(BigDecimal.valueOf(123456.78));

        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        existingCreditTypeDetailViewList.add(existingCreditTypeDetailView1);
        existingCreditTypeDetailViewList.add(existingCreditTypeDetailView2);

        existingGuarantor.setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        existingGuarantor.setGuaranteeAmount(BigDecimal.valueOf(2030005670));

        BigDecimal extTotalGuaranteeAmount = existingGuarantor.getGuaranteeAmount();

        List<ExistingGuarantorDetailView> extGuarantorList = new ArrayList<ExistingGuarantorDetailView>();
        extGuarantorList.add(existingGuarantor);
        decisionView.setExtGuarantorList(extGuarantorList);
        decisionView.setExtTotalGuaranteeAmount(extTotalGuaranteeAmount);

        // Condition
        NewConditionDetailView extCondition_1 = new NewConditionDetailView();
        extCondition_1.setLoanType("Loan Type 1");
        extCondition_1.setConditionDesc("Condition Example 1");

        NewConditionDetailView extCondition_2 = new NewConditionDetailView();
        extCondition_2.setLoanType("Loan Type 2");
        extCondition_2.setConditionDesc("Condition Example 2");

        List<NewConditionDetailView> extConditionComCreditList = new ArrayList<NewConditionDetailView>();
        extConditionComCreditList.add(extCondition_1);
        extConditionComCreditList.add(extCondition_2);
        decisionView.setExtConditionComCreditList(extConditionComCreditList);

        //========================================= Propose =========================================//
        // Proposed Credit
        //----------------------------------------- 1 ---------------------------------------//
        List<NewCreditTierDetailView> tierDetailViewList1 = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView tierDetailView1 = new NewCreditTierDetailView();
        tierDetailView1.setStandardPrice("MLR+1.00");
        tierDetailView1.setSuggestPrice("MLR+1.00");
        tierDetailView1.setFinalPriceRate("MLR+1.00");
        tierDetailView1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView1.setTenor(3);
        tierDetailViewList1.add(tierDetailView1);

        NewCreditDetailView proposeCreditDetailView1 = new NewCreditDetailView();
        proposeCreditDetailView1.setProductProgram(productProgram);
        proposeCreditDetailView1.setCreditType(creditType);
        proposeCreditDetailView1.setProductCode("EAC1");
        proposeCreditDetailView1.setProjectCode("90074");
        proposeCreditDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView1.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView1.setNewCreditTierDetailViewList(tierDetailViewList1);
        proposeCreditDetailView1.setRequestType(0);
        proposeCreditDetailView1.setRefinance(0);
        proposeCreditDetailView1.setLoanPurpose(loanPurpose);
        proposeCreditDetailView1.setRemark("Purpose Detail Example");
        proposeCreditDetailView1.setDisbursement(disbursement);
        proposeCreditDetailView1.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 2 ---------------------------------------//
        List<NewCreditTierDetailView> tierDetailViewList2 = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView tierDetailView2_1 = new NewCreditTierDetailView();
        tierDetailView2_1.setStandardPrice("MLR+1.00");
        tierDetailView2_1.setSuggestPrice("MLR+1.00");
        tierDetailView2_1.setFinalPriceRate("MLR+1.00");
        tierDetailView2_1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_1.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_1);

        NewCreditTierDetailView tierDetailView2_2 = new NewCreditTierDetailView();
        tierDetailView2_2.setStandardPrice("MLR+1.00");
        tierDetailView2_2.setSuggestPrice("MLR+1.00");
        tierDetailView2_2.setFinalPriceRate("MLR+1.00");
        tierDetailView2_2.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_2.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_2);

        NewCreditTierDetailView tierDetailView2_3 = new NewCreditTierDetailView();
        tierDetailView2_3.setStandardPrice("MLR+1.00");
        tierDetailView2_3.setSuggestPrice("MLR+1.00");
        tierDetailView2_3.setFinalPriceRate("MLR+1.00");
        tierDetailView2_3.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_3.setTenor(24);
        tierDetailViewList2.add(tierDetailView2_3);

        NewCreditDetailView proposeCreditDetailView2 = new NewCreditDetailView();
        proposeCreditDetailView2.setProductProgram(productProgram);
        proposeCreditDetailView2.setCreditType(creditType);
        proposeCreditDetailView2.setProductCode("EAC2");
        proposeCreditDetailView2.setProjectCode("90078");
        proposeCreditDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView2.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView2.setNewCreditTierDetailViewList(tierDetailViewList2);
        proposeCreditDetailView2.setRequestType(1);
        proposeCreditDetailView2.setRefinance(1);
        proposeCreditDetailView2.setLoanPurpose(loanPurpose);
        proposeCreditDetailView2.setRemark("Purpose Detail Example 2");
        proposeCreditDetailView2.setDisbursement(disbursement);
        proposeCreditDetailView2.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 3 ---------------------------------------//
        NewCreditDetailView proposeCreditDetailView3 = new NewCreditDetailView();
        proposeCreditDetailView3.setProductProgram(productProgram);
        proposeCreditDetailView3.setCreditType(creditType);
        proposeCreditDetailView3.setProductCode("EAC3");
        proposeCreditDetailView3.setProjectCode("90082");
        proposeCreditDetailView3.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView3.setFrontEndFee(BigDecimal.valueOf(2.25));
        proposeCreditDetailView3.setNewCreditTierDetailViewList(null);
        proposeCreditDetailView3.setRequestType(0);
        proposeCreditDetailView3.setRefinance(1);
        proposeCreditDetailView3.setLoanPurpose(loanPurpose);
        proposeCreditDetailView3.setRemark("Purpose Detail Example 3");
        proposeCreditDetailView3.setDisbursement(disbursement);
        proposeCreditDetailView3.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 4 ---------------------------------------//
        NewCreditDetailView proposeCreditDetailView4 = new NewCreditDetailView();
        proposeCreditDetailView4.setProductProgram(productProgram);
        proposeCreditDetailView4.setCreditType(creditType);
        proposeCreditDetailView4.setProductCode("EAC4");
        proposeCreditDetailView4.setProjectCode("90086");
        proposeCreditDetailView4.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView4.setFrontEndFee(BigDecimal.valueOf(2.75));
        proposeCreditDetailView4.setNewCreditTierDetailViewList(new ArrayList<NewCreditTierDetailView>());
        proposeCreditDetailView4.setRequestType(1);
        proposeCreditDetailView4.setRefinance(0);
        proposeCreditDetailView4.setLoanPurpose(loanPurpose);
        proposeCreditDetailView4.setRemark("Purpose Detail Example 4");
        proposeCreditDetailView4.setDisbursement(disbursement);
        proposeCreditDetailView4.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        BigDecimal proposeTotalCreditLimit = proposeCreditDetailView1.getLimit().add(proposeCreditDetailView2.getLimit()).add(proposeCreditDetailView3.getLimit()).add(proposeCreditDetailView4.getLimit());

        List<NewCreditDetailView> proposeCreditList = new ArrayList<NewCreditDetailView>();
        proposeCreditList.add(proposeCreditDetailView1);
        proposeCreditList.add(proposeCreditDetailView2);
        proposeCreditList.add(proposeCreditDetailView3);
        proposeCreditList.add(proposeCreditDetailView4);
        decisionView.setProposeCreditList(proposeCreditList);

        BigDecimal totalLimit = proposeCreditDetailView1.getLimit().add(proposeCreditDetailView2.getLimit())
                .add(proposeCreditDetailView3.getLimit()).add(proposeCreditDetailView4.getLimit());
//        newCreditFacilityView.setTotalPropose(totalLimit);

        // Fee Information
        List<NewFeeDetailView> proposeFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        NewFeeDetailView proposeFeeDetailView1 = new NewFeeDetailView();
        proposeFeeDetailView1.setProductProgram("TMB SME SmartBiz");
        proposeFeeDetailView1.setStandardFrontEndFee("12.34%, -");
        proposeFeeDetailView1.setCommitmentFee("12.34%, -");
        proposeFeeDetailView1.setExtensionFee("12.34%, -");
        proposeFeeDetailView1.setPrepaymentFee("12.34%, 5 Years");
        proposeFeeDetailView1.setCancellationFee("12.34%, 2 Years");
        proposeFeeDetailViewList.add(proposeFeeDetailView1);
//        newCreditFacilityView.setNewFeeDetailViewList(proposeFeeDetailViewList);

        // Propose Collateral Info.
        NewCollateralInfoView proposeCollateralInfoView1 = new NewCollateralInfoView();
        proposeCollateralInfoView1.setJobID("#001");
        proposeCollateralInfoView1.setAppraisalDate(new Date());
        proposeCollateralInfoView1.setAadDecision("Accept");
        proposeCollateralInfoView1.setAadDecisionReason("Reason Example");
        proposeCollateralInfoView1.setAadDecisionReasonDetail("Remark Example");
        proposeCollateralInfoView1.setUsage("Use");
        proposeCollateralInfoView1.setTypeOfUsage("Type of usage");
        proposeCollateralInfoView1.setMortgageCondition("Mortgage Condition XXX");
        proposeCollateralInfoView1.setMortgageConditionDetail("Mortgage Condition Detail.....");
        proposeCollateralInfoView1.setBdmComments("BDM Comments Detail.....");
        proposeCollateralInfoView1.setCreditTypeDetailViewList(creditTypeDetailViewList);

        // Collateral Head Detail
        NewCollateralHeadDetailView collateralHeaderDetailView1 = new NewCollateralHeadDetailView();
        collateralHeaderDetailView1.setPotentialCollateral(potentialCollateral);
        collateralHeaderDetailView1.setCollTypePercentLTV(collateralType);
        collateralHeaderDetailView1.setExistingCredit(BigDecimal.valueOf(1234567.89));
        collateralHeaderDetailView1.setTitleDeed("12, 1234");
        collateralHeaderDetailView1.setCollateralLocation("Jompol, Jatujak, Bangkok");
        collateralHeaderDetailView1.setHeadCollType(collateralType);
        collateralHeaderDetailView1.setAppraisalValue(BigDecimal.valueOf(3000000.00));
        collateralHeaderDetailView1.setInsuranceCompany(RadioValue.NOT_SELECTED.value());

        // Sub Collateral Detail of Collateral Head Detail
        NewSubCollateralDetailView subCollateralDetailView1 = new NewSubCollateralDetailView();
        subCollateralDetailView1.setSubCollateralType(subCollateralType);
        subCollateralDetailView1.setAddress("234 Jompol, Jatujak, Bangkok");
        subCollateralDetailView1.setLandOffice("Ladpraow");
        subCollateralDetailView1.setTitleDeed("12, 1234");
        subCollateralDetailView1.setCollateralOwnerAAD("Mr.A Example");
        subCollateralDetailView1.setCollateralOwnerUWList(collateralOwnerUWList);
        subCollateralDetailView1.setMortgageList(mortgageTypeList);
        subCollateralDetailView1.setRelatedWithList(relatedWithList);
        subCollateralDetailView1.setAppraisalValue(BigDecimal.valueOf(2000000.00));
        subCollateralDetailView1.setMortgageValue(BigDecimal.valueOf(3200000.00));

        NewSubCollateralDetailView subCollateralDetailView2 = new NewSubCollateralDetailView();
        subCollateralDetailView2.setSubCollateralType(subCollateralType);
        subCollateralDetailView2.setAddress("567 Jompol, Jatujak, Bangkok");
        subCollateralDetailView2.setLandOffice("Ladpraow 17");
        subCollateralDetailView2.setTitleDeed("15, 888");
        subCollateralDetailView2.setCollateralOwnerAAD("Mr.C Example");
        subCollateralDetailView2.setCollateralOwnerUWList(collateralOwnerUWList);
        subCollateralDetailView2.setMortgageList(mortgageTypeList);
        subCollateralDetailView2.setRelatedWithList(relatedWithList);
        subCollateralDetailView2.setAppraisalValue(BigDecimal.valueOf(2457000.00));
        subCollateralDetailView2.setMortgageValue(BigDecimal.valueOf(520000.00));

        List<NewSubCollateralDetailView> subCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        subCollateralDetailViewList.add(subCollateralDetailView1);
        subCollateralDetailViewList.add(subCollateralDetailView2);

        collateralHeaderDetailView1.setNewSubCollateralDetailViewList(subCollateralDetailViewList);

        List<NewCollateralHeadDetailView> collateralHeaderDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        collateralHeaderDetailViewList.add(collateralHeaderDetailView1);
        proposeCollateralInfoView1.setNewCollateralHeadDetailViewList(collateralHeaderDetailViewList);

        List<NewCollateralInfoView> proposeCollateralInfoViewList = new ArrayList<NewCollateralInfoView>();
        proposeCollateralInfoViewList.add(proposeCollateralInfoView1);
//        newCreditFacilityView.setNewCollateralInfoViewList(proposeCollateralInfoViewList);

        // Proposed Guarantor
        NewGuarantorDetailView proposeGuarantorDetailView1 = new NewGuarantorDetailView();
        proposeGuarantorDetailView1.setGuarantorName(guarantor1);
        proposeGuarantorDetailView1.setTotalLimitGuaranteeAmount(BigDecimal.valueOf(11222333.44));
        proposeGuarantorDetailView1.setTcgLgNo("11-23456");
        proposeGuarantorDetailView1.setCreditTypeDetailViewList(creditTypeDetailViewList);

        NewGuarantorDetailView proposeGuarantorDetailView2 = new NewGuarantorDetailView();
        proposeGuarantorDetailView2.setGuarantorName(guarantor2);
        proposeGuarantorDetailView2.setTotalLimitGuaranteeAmount(BigDecimal.valueOf(44555666.77));
        proposeGuarantorDetailView2.setTcgLgNo("22-56789");
        proposeGuarantorDetailView2.setCreditTypeDetailViewList(creditTypeDetailViewList);

        BigDecimal totalGuaranteeAmount = proposeGuarantorDetailView1.getTotalLimitGuaranteeAmount().add(proposeGuarantorDetailView2.getTotalLimitGuaranteeAmount());

        List<NewGuarantorDetailView> proposeGuarantorDetailViewList = new ArrayList<NewGuarantorDetailView>();
        proposeGuarantorDetailViewList.add(proposeGuarantorDetailView1);
        proposeGuarantorDetailViewList.add(proposeGuarantorDetailView2);
//        newCreditFacilityView.setNewGuarantorDetailViewList(proposeGuarantorDetailViewList);
//        newCreditFacilityView.setTotalGuaranteeAmount(totalGuaranteeAmount);

        // Duplicate Approved Propose Credit
        approvedProposeCreditList = new ArrayList<NewCreditDetailView>();
        approvedProposeCreditList.add(proposeCreditDetailView1);
        approvedProposeCreditList.add(proposeCreditDetailView2);
        approvedProposeCreditList.add(proposeCreditDetailView3);
        approvedProposeCreditList.add(proposeCreditDetailView4);
        approvedTotalCredit = totalLimit;
        // Duplicate Approved Propose Collateral
        approvedProposeCollateralList = new ArrayList<NewCollateralInfoView>();
        approvedProposeCollateralList.add(proposeCollateralInfoView1);
        // Duplicate Approved Propose Guarantor
        approvedProposeGuarantorList = new ArrayList<NewGuarantorDetailView>();
        approvedProposeGuarantorList.add(proposeGuarantorDetailView1);
        approvedProposeGuarantorList.add(proposeGuarantorDetailView2);
        approvedTotalGuaranteeAmt = totalGuaranteeAmount;

        followUpConditionView = new FollowUpConditionView();
        followUpConditionViewList = new ArrayList<FollowUpConditionView>();
    }

    public void onAddAppProposeCredit() {
        log.debug("onAddAppProposeCredit()");
        selectedAppProposeCredit = new NewCreditDetailView();
        initialProposeView();
        modeEdit = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onAddAppProposeGuarantor() {
        log.debug("onAddAppProposeGuarantor()");
        selectedAppProposeGuarantor = new NewGuarantorDetailView();
        modeEditGuarantor = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onAddFollowUpCondition() {
        log.debug("onAddFollowUpCondition()");
        followUpConditionViewList.add(followUpConditionView);
        // Clear add form
        followUpConditionView = new FollowUpConditionView();
    }

    public void onEditAppProposeCredit() {
        log.debug("onEditAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        modeEdit = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onEditAppProposeCollateral() {
        log.debug("onEditAppProposeCollateral() selectedAppProposeCollateral: {}", selectedAppProposeCollateral);
        modeEditCollateral = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onEditAppProposeGuarantor() {
        log.debug("onEditAppProposeGuarantor() selectedAppProposeGuarantor: {}", selectedAppProposeGuarantor);
        modeEditGuarantor = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCredit() {
        log.debug("onDeleteAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        approvedProposeCreditList.remove(selectedAppProposeCredit);
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() selectedAppProposeCollateral: {}", selectedAppProposeCollateral);
        approvedProposeCollateralList.remove(selectedAppProposeCollateral);
    }

    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral()");

    }

    public void onDeleteAppProposeGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() selectedAppProposeGuarantor: {}", selectedAppProposeGuarantor);
        approvedProposeGuarantorList.remove(selectedAppProposeGuarantor);
    }

    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee()");
    }

    public void onSave() {
        log.debug("onSave()");
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    // ---------- Propose Credit Dialog ---------- //
    public void onChangeRequestType() {

    }

    public void onChangeProductProgram() {

    }

    public void onChangeCreditType() {

    }

    public void onChangeSuggestValue() {

    }

    public void onAddTierInfo() {
        BaseRate standardBaseRate = selectedAppProposeCredit.getStandardBasePrice();
        BigDecimal standardInterest = selectedAppProposeCredit.getStandardInterest();
        BigDecimal resultStandardPrice;
        if (standardBaseRate != null && standardInterest != null) {
            resultStandardPrice = standardBaseRate.getValue().add(standardInterest);
        } else {
            resultStandardPrice = standardBaseRate != null ? standardBaseRate.getValue() : standardInterest;
        }

        BaseRate suggestBaseRate = selectedAppProposeCredit.getSuggestBasePrice();
        BigDecimal suggestInterest = selectedAppProposeCredit.getSuggestInterest();
        BigDecimal resultSuggestPrice;
        if (suggestBaseRate != null && suggestInterest != null) {
            resultSuggestPrice = suggestBaseRate.getValue().add(suggestInterest);
        } else {
            resultSuggestPrice = suggestBaseRate != null ? suggestBaseRate.getValue() : suggestInterest;
        }

        NewCreditTierDetailView newCreditTierDetail = new NewCreditTierDetailView();
        newCreditTierDetail.setStandardBasePrice(standardBaseRate);
        newCreditTierDetail.setStandardInterest(standardInterest);
        newCreditTierDetail.setSuggestBasePrice(suggestBaseRate);
        newCreditTierDetail.setSuggestInterest(suggestInterest);

        // Final Price = Max of between StandardPrice and SuggestPrice
        if (ValidationUtil.isGreaterThan(resultStandardPrice, resultSuggestPrice)) {
            // if (standardPrice > suggestPrice)
            newCreditTierDetail.setFinalBasePrice(standardBaseRate);
            newCreditTierDetail.setFinalInterest(standardInterest);
        }
        else {
            // if (standardPrice < suggestPrice || standardPrice == suggestPrice)
            newCreditTierDetail.setFinalBasePrice(suggestBaseRate);
            newCreditTierDetail.setFinalInterest(suggestInterest);
        }
        newCreditTierDetail.setCanEdit(true);

        if (selectedAppProposeCredit.getNewCreditTierDetailViewList() != null) {
            selectedAppProposeCredit.getNewCreditTierDetailViewList().add(newCreditTierDetail);
        } else {
            List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
            newCreditTierDetailViewList.add(newCreditTierDetail);
        }

    }

    public void onDeleteTierInfo(int rowIndex) {
        selectedAppProposeCredit.getNewCreditTierDetailViewList().remove(rowIndex);
    }

    public void onSaveAppProposeCredit() {
//        if (newCreditFacilityView.getNewCreditDetailViewList() != null) {
//            List<NewCreditDetailView> proposeCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();
//            if (modeEdit) {
//                proposeCreditDetailViewList.set(proposeCreditDetailViewList.indexOf(selectedAppProposeCredit), selectedAppProposeCredit);
//            }
//            else {
//                proposeCreditDetailViewList.add(selectedAppProposeCredit);
//            }
//        }
//        else {
//            List<NewCreditDetailView> proposeCreditDetailViewList = new ArrayList<NewCreditDetailView>();
//            proposeCreditDetailViewList.add(selectedAppProposeCredit);
//        }
    }

    // ---------- Propose Guarantor Dialog ---------- //
    public void onSaveGuarantorInfo() {

    }

    // ----------------------------------------------- Getter/Setter -----------------------------------------------//

    public DecisionView getDecisionView() {
        return decisionView;
    }

    public void setDecisionView(DecisionView decisionView) {
        this.decisionView = decisionView;
    }

    public List<NewCreditDetailView> getApprovedProposeCreditList() {
        return approvedProposeCreditList;
    }

    public void setApprovedProposeCreditList(List<NewCreditDetailView> approvedProposeCreditList) {
        this.approvedProposeCreditList = approvedProposeCreditList;
    }

    public List<NewCollateralInfoView> getApprovedProposeCollateralList() {
        return approvedProposeCollateralList;
    }

    public void setApprovedProposeCollateralList(List<NewCollateralInfoView> approvedProposeCollateralList) {
        this.approvedProposeCollateralList = approvedProposeCollateralList;
    }

    public List<NewGuarantorDetailView> getApprovedProposeGuarantorList() {
        return approvedProposeGuarantorList;
    }

    public void setApprovedProposeGuarantorList(List<NewGuarantorDetailView> approvedProposeGuarantorList) {
        this.approvedProposeGuarantorList = approvedProposeGuarantorList;
    }

    public NewCreditDetailView getSelectedAppProposeCredit() {
        return selectedAppProposeCredit;
    }

    public void setSelectedAppProposeCredit(NewCreditDetailView selectedAppProposeCredit) {
        this.selectedAppProposeCredit = selectedAppProposeCredit;
    }

    public NewGuarantorDetailView getSelectedAppProposeGuarantor() {
        return selectedAppProposeGuarantor;
    }

    public void setSelectedAppProposeGuarantor(NewGuarantorDetailView selectedAppProposeGuarantor) {
        this.selectedAppProposeGuarantor = selectedAppProposeGuarantor;
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

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
    }

    public List<PrdProgramToCreditType> getPrdProgramToCreditTypeList() {
        return prdProgramToCreditTypeList;
    }

    public void setPrdProgramToCreditTypeList(List<PrdProgramToCreditType> prdProgramToCreditTypeList) {
        this.prdProgramToCreditTypeList = prdProgramToCreditTypeList;
    }

    public List<Disbursement> getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(List<Disbursement> disbursementList) {
        this.disbursementList = disbursementList;
    }

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public boolean isModeEdit() {
        return modeEdit;
    }

    public void setModeEdit(boolean modeEdit) {
        this.modeEdit = modeEdit;
    }

    public boolean isModeEditCollateral() {
        return modeEditCollateral;
    }

    public void setModeEditCollateral(boolean modeEditCollateral) {
        this.modeEditCollateral = modeEditCollateral;
    }

    public boolean isModeEditGuarantor() {
        return modeEditGuarantor;
    }

    public void setModeEditGuarantor(boolean modeEditGuarantor) {
        this.modeEditGuarantor = modeEditGuarantor;
    }

    public NewCollateralInfoView getSelectedAppProposeCollateral() {
        return selectedAppProposeCollateral;
    }

    public void setSelectedAppProposeCollateral(NewCollateralInfoView selectedAppProposeCollateral) {
        this.selectedAppProposeCollateral = selectedAppProposeCollateral;
    }

    public List<Customer> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<Customer> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public BigDecimal getApprovedTotalCredit() {
        return approvedTotalCredit;
    }

    public void setApprovedTotalCredit(BigDecimal approvedTotalCredit) {
        this.approvedTotalCredit = approvedTotalCredit;
    }

    public BigDecimal getApprovedTotalCommercial() {
        return approvedTotalCommercial;
    }

    public void setApprovedTotalCommercial(BigDecimal approvedTotalCommercial) {
        this.approvedTotalCommercial = approvedTotalCommercial;
    }

    public BigDecimal getApprovedTotalComAndOBOD() {
        return approvedTotalComAndOBOD;
    }

    public void setApprovedTotalComAndOBOD(BigDecimal approvedTotalComAndOBOD) {
        this.approvedTotalComAndOBOD = approvedTotalComAndOBOD;
    }

    public BigDecimal getApprovedTotalExposure() {
        return approvedTotalExposure;
    }

    public void setApprovedTotalExposure(BigDecimal approvedTotalExposure) {
        this.approvedTotalExposure = approvedTotalExposure;
    }

    public BigDecimal getApprovedTotalNumOfNewOD() {
        return approvedTotalNumOfNewOD;
    }

    public void setApprovedTotalNumOfNewOD(BigDecimal approvedTotalNumOfNewOD) {
        this.approvedTotalNumOfNewOD = approvedTotalNumOfNewOD;
    }

    public BigDecimal getApprovedTotalNumProposeCreditFac() {
        return approvedTotalNumProposeCreditFac;
    }

    public void setApprovedTotalNumProposeCreditFac(BigDecimal approvedTotalNumProposeCreditFac) {
        this.approvedTotalNumProposeCreditFac = approvedTotalNumProposeCreditFac;
    }

    public BigDecimal getApprovedTotalNumContingentPropose() {
        return approvedTotalNumContingentPropose;
    }

    public void setApprovedTotalNumContingentPropose(BigDecimal approvedTotalNumContingentPropose) {
        this.approvedTotalNumContingentPropose = approvedTotalNumContingentPropose;
    }

    public BigDecimal getGrdTotalNumOfCoreAsset() {
        return grdTotalNumOfCoreAsset;
    }

    public void setGrdTotalNumOfCoreAsset(BigDecimal grdTotalNumOfCoreAsset) {
        this.grdTotalNumOfCoreAsset = grdTotalNumOfCoreAsset;
    }

    public BigDecimal getGrdTotalNumOfNonCoreAsset() {
        return grdTotalNumOfNonCoreAsset;
    }

    public void setGrdTotalNumOfNonCoreAsset(BigDecimal grdTotalNumOfNonCoreAsset) {
        this.grdTotalNumOfNonCoreAsset = grdTotalNumOfNonCoreAsset;
    }

    public BigDecimal getApprovedTotalGuaranteeAmt() {
        return approvedTotalGuaranteeAmt;
    }

    public void setApprovedTotalGuaranteeAmt(BigDecimal approvedTotalGuaranteeAmt) {
        this.approvedTotalGuaranteeAmt = approvedTotalGuaranteeAmt;
    }

    public BigDecimal getApprovedTotalTCGGuaranteeAmt() {
        return approvedTotalTCGGuaranteeAmt;
    }

    public void setApprovedTotalTCGGuaranteeAmt(BigDecimal approvedTotalTCGGuaranteeAmt) {
        this.approvedTotalTCGGuaranteeAmt = approvedTotalTCGGuaranteeAmt;
    }

    public BigDecimal getApprovedTotalIndvGuaranteeAmt() {
        return approvedTotalIndvGuaranteeAmt;
    }

    public void setApprovedTotalIndvGuaranteeAmt(BigDecimal approvedTotalIndvGuaranteeAmt) {
        this.approvedTotalIndvGuaranteeAmt = approvedTotalIndvGuaranteeAmt;
    }

    public BigDecimal getApprovedTotalJurisGuaranteeAmt() {
        return approvedTotalJurisGuaranteeAmt;
    }

    public void setApprovedTotalJurisGuaranteeAmt(BigDecimal approvedTotalJurisGuaranteeAmt) {
        this.approvedTotalJurisGuaranteeAmt = approvedTotalJurisGuaranteeAmt;
    }

    public List<FollowUpConditionView> getFollowUpConditionViewList() {
        return followUpConditionViewList;
    }

    public void setFollowUpConditionViewList(List<FollowUpConditionView> followUpConditionViewList) {
        this.followUpConditionViewList = followUpConditionViewList;
    }

    public FollowUpConditionView getFollowUpConditionView() {
        return followUpConditionView;
    }

    public void setFollowUpConditionView(FollowUpConditionView followUpConditionView) {
        this.followUpConditionView = followUpConditionView;
    }
}