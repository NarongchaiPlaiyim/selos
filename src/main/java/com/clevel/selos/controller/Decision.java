package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
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

    //Transform

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //----- Existing Credit -----//
    private ExistingCreditView existingCreditView;
    private List<ProposeConditionDetailView> existingConditionList;
    private List<ExistingGuarantorDetailView> existingGuarantorList;

    //----- Propose Credit -----//
    private CreditFacProposeView creditFacProposeView;
    // Approved Model View
    private ProposeCreditDetailView approvedProposeCredit;
    private List<ProposeCreditDetailView> approvedProposeCreditList;
    private List<ProposeCollateralInfoView> approvedProposeCollateralList;
    private List<ProposeGuarantorDetailView> approvedProposeGuarantorList;
    private ProposeConditionDetailView proposeCondition;
    private Date proposeConditionDate;

    // Add/Edit/Delete
    private ProposeCreditDetailView addNewAppProposeCredit;
    private ProposeCreditDetailView selectedAppProposeCredit;
    private ProposeGuarantorDetailView addNewAppProposeGuarantor;
    private ProposeGuarantorDetailView selectedAppProposeGuarantor;

    // Select Items - Propose Credit
    private List<CreditRequestType> creditRequestTypeList;
    private CreditRequestType creditRequestTypeSelected;
    private List<Country> investedCountryList;
    private Country investedCountrySelected;

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

    @PostConstruct
    public void onCreation() {
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

        Disbursement disbursement = new Disbursement();
        disbursement.setDisbursement("Normal Disbursement");

        List<SplitLineDetailView> splitLineList = new ArrayList<SplitLineDetailView>();
        SplitLineDetailView splitLineDetail_1 = new SplitLineDetailView();
        splitLineDetail_1.setProductProgram(productProgram);
        splitLineDetail_1.setLimit(BigDecimal.valueOf(34220000));
        splitLineList.add(splitLineDetail_1);

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
        existingCollateralDetail.setMortgageType("Mortgage Type");
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
        existingConditionList = new ArrayList<ProposeConditionDetailView>();
        ProposeConditionDetailView existingCondition_1 = new ProposeConditionDetailView();
        existingCondition_1.setLoanType("Loan Type 1");
        existingCondition_1.setConditionDesc("Condition Example 1");
        existingConditionList.add(existingCondition_1);

        ProposeConditionDetailView existingCondition_2 = new ProposeConditionDetailView();
        existingCondition_2.setLoanType("Loan Type 2");
        existingCondition_2.setConditionDesc("Condition Example 2");
        existingConditionList.add(existingCondition_2);

        //========================================= Propose =========================================//
        creditFacProposeView = new CreditFacProposeView();

        // Proposed Credit
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        //----------------------------------------- 1 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList1 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView1 = new CreditTierDetailView();
        tierDetailView1.setStandardPrice("MLR+1.00");
        tierDetailView1.setSuggestPrice("MLR+1.00");
        tierDetailView1.setFinalPriceRate("MLR+1.00");
        tierDetailView1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView1.setTenor(3);
        tierDetailViewList1.add(tierDetailView1);

        ProposeCreditDetailView proposeCreditDetailView1 = new ProposeCreditDetailView();
        proposeCreditDetailView1.setProductProgram(productProgram);
        proposeCreditDetailView1.setCreditType(creditType);
        proposeCreditDetailView1.setProductCode("EAC1");
        proposeCreditDetailView1.setProjectCode("90074");
        proposeCreditDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView1.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView1.setCreditTierDetailViewList(tierDetailViewList1);
        proposeCreditDetailView1.setRequestType(0);
        proposeCreditDetailView1.setRefinance(0);
        proposeCreditDetailView1.setRemark("Purpose Detail Example");
        proposeCreditDetailView1.setDisbursement(disbursement);
        proposeCreditDetailView1.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 2 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList2 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView2_1 = new CreditTierDetailView();
        tierDetailView2_1.setStandardPrice("MLR+1.00");
        tierDetailView2_1.setSuggestPrice("MLR+1.00");
        tierDetailView2_1.setFinalPriceRate("MLR+1.00");
        tierDetailView2_1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_1.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_1);

        CreditTierDetailView tierDetailView2_2 = new CreditTierDetailView();
        tierDetailView2_2.setStandardPrice("MLR+1.00");
        tierDetailView2_2.setSuggestPrice("MLR+1.00");
        tierDetailView2_2.setFinalPriceRate("MLR+1.00");
        tierDetailView2_2.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_2.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_2);

        CreditTierDetailView tierDetailView2_3 = new CreditTierDetailView();
        tierDetailView2_3.setStandardPrice("MLR+1.00");
        tierDetailView2_3.setSuggestPrice("MLR+1.00");
        tierDetailView2_3.setFinalPriceRate("MLR+1.00");
        tierDetailView2_3.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_3.setTenor(24);
        tierDetailViewList2.add(tierDetailView2_3);

        ProposeCreditDetailView proposeCreditDetailView2 = new ProposeCreditDetailView();
        proposeCreditDetailView2.setProductProgram(productProgram);
        proposeCreditDetailView2.setCreditType(creditType);
        proposeCreditDetailView2.setProductCode("EAC2");
        proposeCreditDetailView2.setProjectCode("90078");
        proposeCreditDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView2.setFrontEndFee(BigDecimal.valueOf(1.75));
        proposeCreditDetailView2.setCreditTierDetailViewList(tierDetailViewList2);
        proposeCreditDetailView2.setRequestType(1);
        proposeCreditDetailView2.setRefinance(1);
        proposeCreditDetailView2.setRemark("Purpose Detail Example 2");
        proposeCreditDetailView2.setDisbursement(disbursement);
        proposeCreditDetailView2.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 3 ---------------------------------------//
        ProposeCreditDetailView proposeCreditDetailView3 = new ProposeCreditDetailView();
        proposeCreditDetailView3.setProductProgram(productProgram);
        proposeCreditDetailView3.setCreditType(creditType);
        proposeCreditDetailView3.setProductCode("EAC3");
        proposeCreditDetailView3.setProjectCode("90082");
        proposeCreditDetailView3.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView3.setFrontEndFee(BigDecimal.valueOf(2.25));
        proposeCreditDetailView3.setCreditTierDetailViewList(null);
        proposeCreditDetailView3.setRequestType(0);
        proposeCreditDetailView3.setRefinance(1);
        proposeCreditDetailView3.setRemark("Purpose Detail Example 3");
        proposeCreditDetailView3.setDisbursement(disbursement);
        proposeCreditDetailView3.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 4 ---------------------------------------//
        ProposeCreditDetailView proposeCreditDetailView4 = new ProposeCreditDetailView();
        proposeCreditDetailView4.setProductProgram(productProgram);
        proposeCreditDetailView4.setCreditType(creditType);
        proposeCreditDetailView4.setProductCode("EAC4");
        proposeCreditDetailView4.setProjectCode("90086");
        proposeCreditDetailView4.setLimit(BigDecimal.valueOf(123456.78));
        proposeCreditDetailView4.setFrontEndFee(BigDecimal.valueOf(2.75));
        proposeCreditDetailView4.setCreditTierDetailViewList(new ArrayList<CreditTierDetailView>());
        proposeCreditDetailView4.setRequestType(1);
        proposeCreditDetailView4.setRefinance(0);
        proposeCreditDetailView4.setRemark("Purpose Detail Example 4");
        proposeCreditDetailView4.setDisbursement(disbursement);
        proposeCreditDetailView4.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        proposeCreditDetailViewList.add(proposeCreditDetailView1);
        proposeCreditDetailViewList.add(proposeCreditDetailView2);
        proposeCreditDetailViewList.add(proposeCreditDetailView3);
        proposeCreditDetailViewList.add(proposeCreditDetailView4);
        creditFacProposeView.setProposeCreditDetailViewList(proposeCreditDetailViewList);

        // Fee Information
        List<ProposeFeeDetailView> proposeFeeDetailViewList = new ArrayList<ProposeFeeDetailView>();
        ProposeFeeDetailView proposeFeeDetailView1 = new ProposeFeeDetailView();
        proposeFeeDetailView1.setProductProgram("TMB SME SmartBiz");
        proposeFeeDetailView1.setStandardFrontEndFee("12.34%, -");
        proposeFeeDetailView1.setCommitmentFee("12.34%, -");
        proposeFeeDetailView1.setExtensionFee("12.34%, -");
        proposeFeeDetailView1.setPrepaymentFee("12.34%, 5 Years");
        proposeFeeDetailView1.setCancellationFee("12.34%, 2 Years");
        proposeFeeDetailViewList.add(proposeFeeDetailView1);
        creditFacProposeView.setProposeFeeDetailViewList(proposeFeeDetailViewList);

        // Propose Collateral
        List<ProposeCollateralInfoView> proposeCollateralInfoViewList = new ArrayList<ProposeCollateralInfoView>();
        ProposeCollateralInfoView proposeCollateralInfoView1 = new ProposeCollateralInfoView();
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

        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView1 = new CreditTypeDetailView();
        creditTypeDetailView1.setAccount("Mr.A Example");
        creditTypeDetailView1.setType("New");
        creditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView1.setCreditFacility("Loan");
        creditTypeDetailView1.setGuaranteeAmount(BigDecimal.valueOf(123456.78));
        creditTypeDetailViewList.add(creditTypeDetailView1);
        proposeCollateralInfoView1.setCreditTypeDetailViewList(creditTypeDetailViewList);

        CollateralHeaderDetailView collateralHeaderDetailView1 = new CollateralHeaderDetailView();
        PotentialCollateral potentialCollateral1 = new PotentialCollateral();
        potentialCollateral1.setName("Core Asset");
        potentialCollateral1.setDescription("Core Asset");
        collateralHeaderDetailView1.setPotentialCollateral(potentialCollateral1);

        CollateralType collateralType1 = new CollateralType();
        collateralType1.setDescription("Land and Building");
        collateralHeaderDetailView1.setHeadCollType(collateralType1);

        collateralHeaderDetailView1.setExistingCredit("1,234,567.89");
        collateralHeaderDetailView1.setTitleDeed("12, 1234");
        collateralHeaderDetailView1.setCollateralLocation("Jompol, Jatujak, Bangkok");
        collateralHeaderDetailView1.setAppraisalValue(BigDecimal.valueOf(3000000.00));
        collateralHeaderDetailView1.setInsuranceCompany(RadioValue.NOT_SELECTED.value());

        List<SubCollateralDetailView> subCollateralDetailViewList = new ArrayList<SubCollateralDetailView>();
        SubCollateralDetailView subCollateralDetailView1 = new SubCollateralDetailView();
        SubCollateralType subCollateralType1 = new SubCollateralType();
        subCollateralType1.setDescription("Land and House");
        subCollateralDetailView1.setSubCollateralType(subCollateralType1);
        subCollateralDetailView1.setAddress("234 Jompol, Jatujak, Bangkok");
        subCollateralDetailView1.setLandOffice("Ladpraow");
        subCollateralDetailView1.setTitleDeed("12, 1234");
        subCollateralDetailView1.setCollateralOwnerAAD("Mr.A Example");
        subCollateralDetailView1.setCollateralOwner("Mr.B Example");
        //todo: subCollateralDetailView.setMortgageType, subCollateralDetailView.setRelatedWith
//        subCollateralDetailView1.setMortgageType();
//        subCollateralDetailView1.setRelatedWith();
        subCollateralDetailView1.setAppraisalValue(BigDecimal.valueOf(2000000.00));
        subCollateralDetailView1.setMortgageValue(BigDecimal.valueOf(3200000.00));
        subCollateralDetailViewList.add(subCollateralDetailView1);

        collateralHeaderDetailView1.setSubCollateralDetailViewList(subCollateralDetailViewList);

        List<CollateralHeaderDetailView> collateralHeaderDetailViewList = new ArrayList<CollateralHeaderDetailView>();
        collateralHeaderDetailViewList.add(collateralHeaderDetailView1);
        proposeCollateralInfoView1.setCollateralHeaderDetailViewList(collateralHeaderDetailViewList);
        proposeCollateralInfoViewList.add(proposeCollateralInfoView1);
        creditFacProposeView.setProposeCollateralInfoViewList(proposeCollateralInfoViewList);

        // Proposed Guarantor
        List<ProposeGuarantorDetailView> proposeGuarantorDetailViewList = new ArrayList<ProposeGuarantorDetailView>();
        ProposeGuarantorDetailView proposeGuarantorDetailView1 = new ProposeGuarantorDetailView();
        Customer guarantor = new Customer();
        guarantor.setNameTh("Guarantor Name");
        guarantor.setLastNameTh("Lastname");
        proposeGuarantorDetailView1.setGuarantorName(guarantor);
        proposeGuarantorDetailView1.setGuaranteeAmount(BigDecimal.valueOf(11222333.44));
        proposeGuarantorDetailView1.setTcgLgNo("11-23456");
        proposeGuarantorDetailView1.setCreditTypeDetailViewList(creditTypeDetailViewList);
        proposeGuarantorDetailViewList.add(proposeGuarantorDetailView1);
        creditFacProposeView.setProposeGuarantorDetailViewList(proposeGuarantorDetailViewList);

        // Propose Condition
        proposeCondition = new ProposeConditionDetailView();
        proposeConditionDate = DateTimeUtil.getCurrentDateTH();
        creditFacProposeView.setProposeConditionDetailViewList(new ArrayList<ProposeConditionDetailView>());
    }

    private void initSelectList() {
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        investedCountryList = countryDAO.findAll();
    }

    public void onAddAppProposeCredit() {
        log.debug("onAddAppProposeCredit()");
    }

    public void onAddAppProposeGuarantor() {
        log.debug("onAddAppProposeGuarantor()");
    }

    public void onAddProposeCondition() {
        log.debug("onAddProposeCondition()");
    }

    public void onEditAppProposeCredit() {
        log.debug("onEditAppProposeCredit()");
    }

    public void onEditAppProposeCollateral() {
        log.debug("onEditAppProposeCollateral()");
    }

    public void onEditAppProposeGuarantor() {
        log.debug("onEditAppProposeGuarantor()");
    }

    public void onDeleteAppProposeCredit() {
        log.debug("onDeleteAppProposeCredit()");
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral()");
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

    public ExistingCreditView getExistingCreditView() {
        return existingCreditView;
    }

    public void setExistingCreditView(ExistingCreditView existingCreditView) {
        this.existingCreditView = existingCreditView;
    }

    public List<ProposeConditionDetailView> getExistingConditionList() {
        return existingConditionList;
    }

    public void setExistingConditionList(List<ProposeConditionDetailView> existingConditionList) {
        this.existingConditionList = existingConditionList;
    }

    public List<ExistingGuarantorDetailView> getExistingGuarantorList() {
        return existingGuarantorList;
    }

    public void setExistingGuarantorList(List<ExistingGuarantorDetailView> existingGuarantorList) {
        this.existingGuarantorList = existingGuarantorList;
    }

    public CreditFacProposeView getCreditFacProposeView() {
        return creditFacProposeView;
    }

    public void setCreditFacProposeView(CreditFacProposeView creditFacProposeView) {
        this.creditFacProposeView = creditFacProposeView;
    }

    public ProposeCreditDetailView getApprovedProposeCredit() {
        return approvedProposeCredit;
    }

    public void setApprovedProposeCredit(ProposeCreditDetailView approvedProposeCredit) {
        this.approvedProposeCredit = approvedProposeCredit;
    }

    public List<ProposeCreditDetailView> getApprovedProposeCreditList() {
        return approvedProposeCreditList;
    }

    public void setApprovedProposeCreditList(List<ProposeCreditDetailView> approvedProposeCreditList) {
        this.approvedProposeCreditList = approvedProposeCreditList;
    }

    public List<ProposeCollateralInfoView> getApprovedProposeCollateralList() {
        return approvedProposeCollateralList;
    }

    public void setApprovedProposeCollateralList(List<ProposeCollateralInfoView> approvedProposeCollateralList) {
        this.approvedProposeCollateralList = approvedProposeCollateralList;
    }

    public List<ProposeGuarantorDetailView> getApprovedProposeGuarantorList() {
        return approvedProposeGuarantorList;
    }

    public void setApprovedProposeGuarantorList(List<ProposeGuarantorDetailView> approvedProposeGuarantorList) {
        this.approvedProposeGuarantorList = approvedProposeGuarantorList;
    }

    public ProposeConditionDetailView getProposeCondition() {
        return proposeCondition;
    }

    public void setProposeCondition(ProposeConditionDetailView proposeCondition) {
        this.proposeCondition = proposeCondition;
    }

    public Date getProposeConditionDate() {
        return proposeConditionDate;
    }

    public void setProposeConditionDate(Date proposeConditionDate) {
        this.proposeConditionDate = proposeConditionDate;
    }

    public ProposeCreditDetailView getAddNewAppProposeCredit() {
        return addNewAppProposeCredit;
    }

    public void setAddNewAppProposeCredit(ProposeCreditDetailView addNewAppProposeCredit) {
        this.addNewAppProposeCredit = addNewAppProposeCredit;
    }

    public ProposeCreditDetailView getSelectedAppProposeCredit() {
        return selectedAppProposeCredit;
    }

    public void setSelectedAppProposeCredit(ProposeCreditDetailView selectedAppProposeCredit) {
        this.selectedAppProposeCredit = selectedAppProposeCredit;
    }

    public ProposeGuarantorDetailView getAddNewAppProposeGuarantor() {
        return addNewAppProposeGuarantor;
    }

    public void setAddNewAppProposeGuarantor(ProposeGuarantorDetailView addNewAppProposeGuarantor) {
        this.addNewAppProposeGuarantor = addNewAppProposeGuarantor;
    }

    public ProposeGuarantorDetailView getSelectedAppProposeGuarantor() {
        return selectedAppProposeGuarantor;
    }

    public void setSelectedAppProposeGuarantor(ProposeGuarantorDetailView selectedAppProposeGuarantor) {
        this.selectedAppProposeGuarantor = selectedAppProposeGuarantor;
    }

    public List<CreditRequestType> getCreditRequestTypeList() {
        return creditRequestTypeList;
    }

    public void setCreditRequestTypeList(List<CreditRequestType> creditRequestTypeList) {
        this.creditRequestTypeList = creditRequestTypeList;
    }

    public CreditRequestType getCreditRequestTypeSelected() {
        return creditRequestTypeSelected;
    }

    public void setCreditRequestTypeSelected(CreditRequestType creditRequestTypeSelected) {
        this.creditRequestTypeSelected = creditRequestTypeSelected;
    }

    public List<Country> getInvestedCountryList() {
        return investedCountryList;
    }

    public void setInvestedCountryList(List<Country> investedCountryList) {
        this.investedCountryList = investedCountryList;
    }

    public Country getInvestedCountrySelected() {
        return investedCountrySelected;
    }

    public void setInvestedCountrySelected(Country investedCountrySelected) {
        this.investedCountrySelected = investedCountrySelected;
    }
}
