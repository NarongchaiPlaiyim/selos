package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.dao.master.DisbursementDAO;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
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

    //Transform

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //----- Existing Credit -----//
    private ExistingCreditView existingCreditView;
    private List<NewConditionDetailView> existingConditionList;
    private List<ExistingGuarantorDetailView> existingGuarantorList;

    //----- Propose Credit -----//
    private NewCreditFacilityView newCreditFacilityView;
    // Approved Model View
    private List<NewCreditDetailView> approvedProposeCreditList;
    private List<NewCollateralInfoView> approvedProposeCollateralList;
    private List<NewGuarantorDetailView> approvedProposeGuarantorList;
    private NewConditionDetailView proposeCondition;
    private Date proposeConditionDate;
    private BigDecimal totalLimitApprovedCredit;

    // Add/Edit/Delete
    private NewCreditDetailView selectedAppProposeCredit;
    private NewCollateralInfoView selectedAppProposeCollateral;
    private NewGuarantorDetailView selectedAppProposeGuarantor;

    // Select items - Approved Propose Credit
    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;

    // Propose Credit Dialog
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<BaseRate> baseRateList;
    private List<Disbursement> disbursementList;

    // Mode
    enum ModeForButton {ADD, EDIT}
    private ModeForButton modeForButton;
    private boolean modeEdit;
    private boolean modeEditCollateral;

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
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        baseRateList = baseRateDAO.findAll();
        disbursementList = disbursementDAO.findAll();
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

        SubCollateralType subCollateralType = new SubCollateralType();
        subCollateralType.setDescription("Land and House");
        subCollateralType.setCollateralType(collateralType);

        MortgageType mortgageType1 = new MortgageType();
        mortgageType1.setMortgage("Mortgage type 1");

        MortgageType mortgageType2 = new MortgageType();
        mortgageType2.setMortgage("Mortgage type 2");

        List<MortgageType> mortgageTypeList = new ArrayList<MortgageType>();

        //========================================= Existing =========================================//
        existingCreditView = new ExistingCreditView();

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

        // ----- Existing - Borrower
        List<ExistingCreditDetailView> borrowerComExistingCreditList = new ArrayList<ExistingCreditDetailView>();
        borrowerComExistingCreditList.add(existingCreditDetail_1);
        existingCreditView.setBorrowerComExistingCredit(borrowerComExistingCreditList);

        List<ExistingCreditDetailView> borrowerRetailExistingCreditList = new ArrayList<ExistingCreditDetailView>();
        borrowerRetailExistingCreditList.add(existingCreditDetail_1);
        existingCreditView.setBorrowerRetailExistingCredit(borrowerRetailExistingCreditList);

        List<ExistingCreditDetailView> borrowerAppInRLOSCreditList = new ArrayList<ExistingCreditDetailView>();
        borrowerAppInRLOSCreditList.add(existingCreditDetail_1);
        existingCreditView.setBorrowerAppInRLOSCredit(borrowerAppInRLOSCreditList);

        // ----- Existing - Related Person
        List<ExistingCreditDetailView> relatedComExistingCreditList = new ArrayList<ExistingCreditDetailView>();
        relatedComExistingCreditList.add(existingCreditDetail_1);
        existingCreditView.setRelatedComExistingCredit(relatedComExistingCreditList);

        List<ExistingCreditDetailView> relatedRetailExistingCreditList = new ArrayList<ExistingCreditDetailView>();
        relatedRetailExistingCreditList.add(existingCreditDetail_1);
        existingCreditView.setRelatedRetailExistingCredit(relatedRetailExistingCreditList);

        List<ExistingCreditDetailView> relatedAppInRLOSCreditList = new ArrayList<ExistingCreditDetailView>();
        relatedAppInRLOSCreditList.add(existingCreditDetail_1);
        existingCreditView.setRelatedAppInRLOSCredit(relatedAppInRLOSCreditList);

        // ----- Existing - Collateral
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

        List<ExistingCollateralDetailView> borrowerCollateralList = new ArrayList<ExistingCollateralDetailView>();
        borrowerCollateralList.add(existingCollateralDetail);
        existingCreditView.setBorrowerCollateralList(borrowerCollateralList);

        List<ExistingCollateralDetailView> relatedCollateralList = new ArrayList<ExistingCollateralDetailView>();
        relatedCollateralList.add(existingCollateralDetail);
        existingCreditView.setRelatedCollateralList(relatedCollateralList);

        // ----- Existing - Guarantor
        existingGuarantorList = new ArrayList<ExistingGuarantorDetailView>();
        ExistingGuarantorDetailView existingGuarantor = new ExistingGuarantorDetailView();
        existingGuarantor.setGuarantorName("ABC Co., Ltd.");
        existingGuarantor.setTcgLgNo("12-34567");

        List<ExistingCreditDetailView> existingCreditFacilityList = new ArrayList<ExistingCreditDetailView>();
        existingCreditFacilityList.add(existingCreditDetail_1);
        existingCreditFacilityList.add(existingCreditDetail_1);

        existingGuarantor.setCreditFacilityList(existingCreditFacilityList);
        existingGuarantor.setGuaranteeAmount(BigDecimal.valueOf(2030005670));
        existingGuarantorList.add(existingGuarantor);

        // ----- Total
        existingCreditView.setTotalBorrowerComLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalBorrowerRetailLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalBorrowerAppInRLOSLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalRelatedComLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalRelatedRetailLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalRelatedAppInRLOSLimit(BigDecimal.valueOf(123456789));
        existingCreditView.setTotalGroupCom(existingCreditView.getTotalBorrowerComLimit().add(existingCreditView.getTotalRelatedComLimit()));
        existingCreditView.setTotalGroupComOBOD(existingCreditView.getTotalBorrowerRetailLimit().add(existingCreditView.getTotalRelatedRetailLimit()));
        existingCreditView.setTotalGroupExposure(existingCreditView.getTotalBorrowerAppInRLOSLimit().add(existingCreditView.getTotalRelatedAppInRLOSLimit()));
        existingCreditView.setTotalBorrowerAppraisalValue(BigDecimal.valueOf(9000000));
        existingCreditView.setTotalBorrowerMortgageValue(BigDecimal.valueOf(12000000));
        existingCreditView.setTotalRelatedAppraisalValue(BigDecimal.valueOf(9000000));
        existingCreditView.setTotalRelatedMortgageValue(BigDecimal.valueOf(12000000));
        existingCreditView.setTotalGuaranteeAmount(BigDecimal.valueOf(2030005670));

        // ----- Existing - Condition
        existingConditionList = new ArrayList<NewConditionDetailView>();
        NewConditionDetailView existingCondition_1 = new NewConditionDetailView();
        existingCondition_1.setLoanType("Loan Type 1");
        existingCondition_1.setConditionDesc("Condition Example 1");
        existingConditionList.add(existingCondition_1);

        NewConditionDetailView existingCondition_2 = new NewConditionDetailView();
        existingCondition_2.setLoanType("Loan Type 2");
        existingCondition_2.setConditionDesc("Condition Example 2");
        existingConditionList.add(existingCondition_2);

        //========================================= Propose =========================================//
        newCreditFacilityView = new NewCreditFacilityView();

        // Proposed Credit
        List<NewCreditDetailView> proposeCreditDetailViewList = new ArrayList<NewCreditDetailView>();
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

        proposeCreditDetailViewList.add(proposeCreditDetailView1);
        proposeCreditDetailViewList.add(proposeCreditDetailView2);
        proposeCreditDetailViewList.add(proposeCreditDetailView3);
        proposeCreditDetailViewList.add(proposeCreditDetailView4);
        newCreditFacilityView.setNewCreditDetailViewList(proposeCreditDetailViewList);

        BigDecimal totalLimit = proposeCreditDetailView1.getLimit().add(proposeCreditDetailView2.getLimit())
                .add(proposeCreditDetailView3.getLimit()).add(proposeCreditDetailView4.getLimit());
        newCreditFacilityView.setTotalPropose(totalLimit);
        totalLimitApprovedCredit = totalLimit;

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
        newCreditFacilityView.setNewFeeDetailViewList(proposeFeeDetailViewList);

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

        // Credit Type of Collateral
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
        subCollateralDetailView1.setCollateralOwnerUW("Mr.A Example");
        subCollateralDetailView1.setMortgageList(new ArrayList<MortgageType>()); //todo : change this , AS
        subCollateralDetailView1.setAppraisalValue(BigDecimal.valueOf(2000000.00));
        subCollateralDetailView1.setMortgageValue(BigDecimal.valueOf(3200000.00));

        List<NewSubCollateralDetailView> subCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        subCollateralDetailViewList.add(subCollateralDetailView1);

        collateralHeaderDetailView1.setNewSubCollateralDetailViewList(subCollateralDetailViewList);

        List<NewCollateralHeadDetailView> collateralHeaderDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        collateralHeaderDetailViewList.add(collateralHeaderDetailView1);
        proposeCollateralInfoView1.setNewCollateralHeadDetailViewList(collateralHeaderDetailViewList);

        List<NewCollateralInfoView> proposeCollateralInfoViewList = new ArrayList<NewCollateralInfoView>();
        proposeCollateralInfoViewList.add(proposeCollateralInfoView1);
        newCreditFacilityView.setNewCollateralInfoViewList(proposeCollateralInfoViewList);

        // Proposed Guarantor
        List<NewGuarantorDetailView> proposeGuarantorDetailViewList = new ArrayList<NewGuarantorDetailView>();
        NewGuarantorDetailView proposeGuarantorDetailView1 = new NewGuarantorDetailView();
        Customer guarantor = new Customer();
        guarantor.setNameTh("Guarantor Name");
        guarantor.setLastNameTh("Last name");
        proposeGuarantorDetailView1.setGuarantorName(guarantor);
        proposeGuarantorDetailView1.setTotalLimitGuaranteeAmount(BigDecimal.valueOf(11222333.44)); //todo : change this , AS
        proposeGuarantorDetailView1.setTcgLgNo("11-23456");
        proposeGuarantorDetailView1.setCreditTypeDetailViewList(creditTypeDetailViewList);
        proposeGuarantorDetailViewList.add(proposeGuarantorDetailView1);
        newCreditFacilityView.setNewGuarantorDetailViewList(proposeGuarantorDetailViewList);

        // Propose Condition
        proposeCondition = new NewConditionDetailView();
        proposeConditionDate = DateTimeUtil.getCurrentDateTH();
        newCreditFacilityView.setNewConditionDetailViewList(new ArrayList<NewConditionDetailView>());

        // Duplicate Approved Propose Credit
        approvedProposeCreditList = new ArrayList<NewCreditDetailView>();
        approvedProposeCreditList.add(proposeCreditDetailView1);
        approvedProposeCreditList.add(proposeCreditDetailView2);
        approvedProposeCreditList.add(proposeCreditDetailView3);
        approvedProposeCreditList.add(proposeCreditDetailView4);        
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
    }

    public void onAddProposeCondition() {
        log.debug("onAddProposeCondition()");
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
        log.debug("onEditAppProposeGuarantor()");
    }

    public void onDeleteAppProposeCredit() {
        log.debug("onDeleteAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        approvedProposeCreditList.remove(selectedAppProposeCredit);
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() selectedAppProposeCollateral: {}", selectedAppProposeCollateral);
        approvedProposeCollateralList.remove(selectedAppProposeCollateral);
    }

    public void onDeleteAppProposeGuarantor() {
        log.debug("onDeleteAppProposeGuarantor()");
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

    // ---------- Add new Propose Credit Dialog ---------- //
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
        if (newCreditFacilityView.getNewCreditDetailViewList() != null) {
            List<NewCreditDetailView> proposeCreditDetailViewList = newCreditFacilityView.getNewCreditDetailViewList();
            if (modeEdit) {
                proposeCreditDetailViewList.set(proposeCreditDetailViewList.indexOf(selectedAppProposeCredit), selectedAppProposeCredit);
            }
            else {
                proposeCreditDetailViewList.add(selectedAppProposeCredit);
            }
        }
        else {
            List<NewCreditDetailView> proposeCreditDetailViewList = new ArrayList<NewCreditDetailView>();
            proposeCreditDetailViewList.add(selectedAppProposeCredit);
        }
    }

    public ExistingCreditView getExistingCreditView() {
        return existingCreditView;
    }

    public void setExistingCreditView(ExistingCreditView existingCreditView) {
        this.existingCreditView = existingCreditView;
    }

    public List<NewConditionDetailView> getExistingConditionList() {
        return existingConditionList;
    }

    public void setExistingConditionList(List<NewConditionDetailView> existingConditionList) {
        this.existingConditionList = existingConditionList;
    }

    public List<ExistingGuarantorDetailView> getExistingGuarantorList() {
        return existingGuarantorList;
    }

    public void setExistingGuarantorList(List<ExistingGuarantorDetailView> existingGuarantorList) {
        this.existingGuarantorList = existingGuarantorList;
    }

    public NewCreditFacilityView getNewCreditFacilityView() {
        return newCreditFacilityView;
    }

    public void setNewCreditFacilityView(NewCreditFacilityView newCreditFacilityView) {
        this.newCreditFacilityView = newCreditFacilityView;
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

    public NewConditionDetailView getProposeCondition() {
        return proposeCondition;
    }

    public void setProposeCondition(NewConditionDetailView proposeCondition) {
        this.proposeCondition = proposeCondition;
    }

    public Date getProposeConditionDate() {
        return proposeConditionDate;
    }

    public void setProposeConditionDate(Date proposeConditionDate) {
        this.proposeConditionDate = proposeConditionDate;
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

    public BigDecimal getTotalLimitApprovedCredit() {
        return totalLimitApprovedCredit;
    }

    public void setTotalLimitApprovedCredit(BigDecimal totalLimitApprovedCredit) {
        this.totalLimitApprovedCredit = totalLimitApprovedCredit;
    }

    public NewCollateralInfoView getSelectedAppProposeCollateral() {
        return selectedAppProposeCollateral;
    }

    public void setSelectedAppProposeCollateral(NewCollateralInfoView selectedAppProposeCollateral) {
        this.selectedAppProposeCollateral = selectedAppProposeCollateral;
    }
}
