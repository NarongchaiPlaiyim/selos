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
    private FollowUpConditionView followUpConditionView;
    private ApprovalHistory approvalHistory;
    private boolean roleUW;
    private int creditCustomerType;
    private CreditRequestType creditRequestType;
    private Country country;

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

    // Mode
    enum ModeForButton {ADD, EDIT}
    private ModeForButton modeForButton;
    private boolean modeEditCredit;
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
            roleUW = decisionControl.isRoleUW();
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

    private void initSelectItemsList() {
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        // Propose Credit Dialog
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        baseRateList = baseRateDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        // Propose Guarantor Dialog
        guarantorList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
    }

    private void initSelectedView() {
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

    private void setDummyData() {
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

        List<ExistingSplitLineDetailView> splitLineList = new ArrayList<ExistingSplitLineDetailView>();
        ExistingSplitLineDetailView splitLineDetail_1 = new ExistingSplitLineDetailView();
        splitLineDetail_1.setProductProgram(productProgram);
        splitLineDetail_1.setLimit(BigDecimal.valueOf(34220000));
        splitLineList.add(splitLineDetail_1);

        CreditTypeDetailView creditTypeDetailView1 = new CreditTypeDetailView();
        creditTypeDetailView1.setAccount("Mr.A Example");
        creditTypeDetailView1.setType("New");
        creditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView1.setCreditFacility("Loan");
        creditTypeDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        creditTypeDetailView1.setGuaranteeAmount(BigDecimal.valueOf(1000000));

        CreditTypeDetailView creditTypeDetailView2 = new CreditTypeDetailView();
        creditTypeDetailView2.setAccount("Mr.B Example");
        creditTypeDetailView2.setType("Change");
        creditTypeDetailView2.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView2.setCreditFacility("Loan");
        creditTypeDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        creditTypeDetailView2.setGuaranteeAmount(BigDecimal.valueOf(2000000));

        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        creditTypeDetailViewList.add(creditTypeDetailView1);
        creditTypeDetailViewList.add(creditTypeDetailView2);

        BigDecimal guaranteeAmtOfEachCreditFac = creditTypeDetailView1.getGuaranteeAmount().add(creditTypeDetailView1.getGuaranteeAmount());

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
        existingCreditDetail_1.setExistingSplitLineDetailViewList(splitLineList);

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

        decisionView.setExtBorrowerTotalCommercial(existingCreditDetail_1.getOutstanding());
        decisionView.setExtBorrowerTotalComAndOBOD(existingCreditDetail_1.getOutstanding());
        decisionView.setExtBorrowerTotalExposure(decisionView.getExtBorrowerTotalComAndOBOD().add(decisionView.getExtBorrowerTotalRetailLimit()));

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

        decisionView.setExtRelatedTotalCommercial(existingCreditDetail_1.getOutstanding());
        decisionView.setExtRelatedTotalComAndOBOD(existingCreditDetail_1.getOutstanding());
        decisionView.setExtRelatedTotalExposure(decisionView.getExtRelatedTotalComAndOBOD().add(decisionView.getExtRelatedTotalRetailLimit()));

        decisionView.setExtGroupTotalCommercial(decisionView.getExtBorrowerTotalCommercial().add(decisionView.getExtRelatedTotalCommercial()));
        decisionView.setExtGroupTotalComAndOBOD(decisionView.getExtBorrowerTotalComAndOBOD().add(decisionView.getExtRelatedTotalComAndOBOD()));
        decisionView.setExtGroupTotalExposure(decisionView.getExtBorrowerTotalExposure().add(decisionView.getExtRelatedTotalExposure()));

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
        existingCollateralDetail.setCreditFacility("OD");
        existingCollateralDetail.setLimit(BigDecimal.valueOf(123456789));
        existingCollateralDetail.setMortgageType(mortgageType1);
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

        ExistingCreditTypeDetailView existingCreditTypeDetailView1 = new ExistingCreditTypeDetailView();
        existingCreditTypeDetailView1.setAccountName("Mr.A Example");
        existingCreditTypeDetailView1.setAccountNumber("123-4-56789-0");
        existingCreditTypeDetailView1.setAccountSuf("000");
        existingCreditTypeDetailView1.setType("New");
        existingCreditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        existingCreditTypeDetailView1.setCreditFacility("Loan");
        existingCreditTypeDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        existingCreditTypeDetailView1.setGuaranteeAmount(BigDecimal.valueOf(1015002835));

        ExistingCreditTypeDetailView existingCreditTypeDetailView2 = new ExistingCreditTypeDetailView();
        existingCreditTypeDetailView2.setAccountName("Mr.B Example");
        existingCreditTypeDetailView2.setAccountNumber("098-7-65432-1");
        existingCreditTypeDetailView2.setAccountSuf("001");
        existingCreditTypeDetailView2.setType("Change");
        existingCreditTypeDetailView2.setProductProgram("TMB SME SmartBiz");
        existingCreditTypeDetailView2.setCreditFacility("Loan");
        existingCreditTypeDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        existingCreditTypeDetailView2.setGuaranteeAmount(BigDecimal.valueOf(1015002835));

        BigDecimal extTotalGuaranteeLimitPerProduct = existingCreditTypeDetailView1.getGuaranteeAmount().add(existingCreditTypeDetailView2.getGuaranteeAmount());

        List<ExistingCreditTypeDetailView> existingCreditTypeDetailViewList = new ArrayList<ExistingCreditTypeDetailView>();
        existingCreditTypeDetailViewList.add(existingCreditTypeDetailView1);
        existingCreditTypeDetailViewList.add(existingCreditTypeDetailView2);
        existingGuarantor.setExistingCreditTypeDetailViewList(existingCreditTypeDetailViewList);
        existingGuarantor.setTotalLimitGuaranteeAmount(extTotalGuaranteeLimitPerProduct);

        BigDecimal extTotalGuaranteeAmount = existingGuarantor.getTotalLimitGuaranteeAmount();

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
        decisionView.setProposeTotalCreditLimit(proposeTotalCreditLimit);

        // Approved Credit
        decisionView.setApproveCreditList(proposeCreditList);
        decisionView.setApproveTotalCreditLimit(proposeTotalCreditLimit);

        // Fee Information
        NewFeeDetailView proposeFeeDetailView1 = new NewFeeDetailView();
        proposeFeeDetailView1.setProductProgram("TMB SME SmartBiz");
        proposeFeeDetailView1.setStandardFrontEndFee("12.34%, -");
        proposeFeeDetailView1.setCommitmentFee("12.34%, -");
        proposeFeeDetailView1.setExtensionFee("12.34%, -");
        proposeFeeDetailView1.setPrepaymentFee("12.34%, 5 Years");
        proposeFeeDetailView1.setCancellationFee("12.34%, 2 Years");

        List<NewFeeDetailView> proposeFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        proposeFeeDetailViewList.add(proposeFeeDetailView1);
        decisionView.setProposeFeeInfoList(proposeFeeDetailViewList);

        // Propose Collateral
        NewCollateralInfoView proposeCollateral1 = new NewCollateralInfoView();
        proposeCollateral1.setJobID("#001");
        proposeCollateral1.setAppraisalDate(new Date());
        proposeCollateral1.setAadDecision("Accept");
        proposeCollateral1.setAadDecisionReason("Reason Example");
        proposeCollateral1.setAadDecisionReasonDetail("Remark Example");
        proposeCollateral1.setUsage("Use");
        proposeCollateral1.setTypeOfUsage("Type of usage");
        proposeCollateral1.setMortgageCondition("Mortgage Condition XXX");
        proposeCollateral1.setMortgageConditionDetail("Mortgage Condition Detail.....");
        proposeCollateral1.setBdmComments("BDM Comments Detail.....");
        proposeCollateral1.setCreditTypeDetailViewList(creditTypeDetailViewList);

        // Collateral Head
        NewCollateralHeadDetailView collateralHeader1 = new NewCollateralHeadDetailView();
        collateralHeader1.setPotentialCollateral(potentialCollateral);
        collateralHeader1.setCollTypePercentLTV(collateralType);
        collateralHeader1.setExistingCredit(BigDecimal.valueOf(1234567.89));
        collateralHeader1.setTitleDeed("12, 1234");
        collateralHeader1.setCollateralLocation("Jompol, Jatujak, Bangkok");
        collateralHeader1.setHeadCollType(collateralType);
        collateralHeader1.setAppraisalValue(BigDecimal.valueOf(3000000.00));
        collateralHeader1.setInsuranceCompany(RadioValue.NOT_SELECTED.value());

        // Sub Collateral Detail of Collateral Head Detail
        NewSubCollateralDetailView subCollateral1 = new NewSubCollateralDetailView();
        subCollateral1.setSubCollateralType(subCollateralType);
        subCollateral1.setAddress("234 Jompol, Jatujak, Bangkok");
        subCollateral1.setLandOffice("Ladpraow");
        subCollateral1.setTitleDeed("12, 1234");
        subCollateral1.setCollateralOwnerAAD("Mr.A Example");
//        subCollateral1.setCollateralOwnerUWList(collateralOwnerUWList);
        //subCollateral1.setCollateralOwnerUWList(collateralOwnerUWList);
        subCollateral1.setMortgageList(mortgageTypeList);
//        subCollateral1.setRelatedWithList(relatedWithList);
        //subCollateral1.setRelatedWithList(relatedWithList);
        subCollateral1.setAppraisalValue(BigDecimal.valueOf(2000000.00));
        subCollateral1.setMortgageValue(BigDecimal.valueOf(3200000.00));

        NewSubCollateralDetailView subCollateral2 = new NewSubCollateralDetailView();
        subCollateral2.setSubCollateralType(subCollateralType);
        subCollateral2.setAddress("567 Jompol, Jatujak, Bangkok");
        subCollateral2.setLandOffice("Ladpraow 17");
        subCollateral2.setTitleDeed("15, 888");
        subCollateral2.setCollateralOwnerAAD("Mr.C Example");
//        subCollateral2.setCollateralOwnerUWList(collateralOwnerUWList);
        //subCollateral2.setCollateralOwnerUWList(collateralOwnerUWList);
        subCollateral2.setMortgageList(mortgageTypeList);
//        subCollateral2.setRelatedWithList(relatedWithList);
        //subCollateral2.setRelatedWithList(relatedWithList);
        subCollateral2.setAppraisalValue(BigDecimal.valueOf(2457000.00));
        subCollateral2.setMortgageValue(BigDecimal.valueOf(520000.00));

        List<NewSubCollateralDetailView> subCollateralList = new ArrayList<NewSubCollateralDetailView>();
        subCollateralList.add(subCollateral1);
        subCollateralList.add(subCollateral2);
        collateralHeader1.setNewSubCollateralDetailViewList(subCollateralList);

        List<NewCollateralHeadDetailView> collateralHeaderList = new ArrayList<NewCollateralHeadDetailView>();
        collateralHeaderList.add(collateralHeader1);
        proposeCollateral1.setNewCollateralHeadDetailViewList(collateralHeaderList);

        List<NewCollateralInfoView> proposeCollateralList = new ArrayList<NewCollateralInfoView>();
        proposeCollateralList.add(proposeCollateral1);
        decisionView.setProposeCollateralList(proposeCollateralList);

        // Approved Collateral
        decisionView.setApproveCollateralList(proposeCollateralList);

        // Proposed Guarantor
        NewGuarantorDetailView proposeGuarantor1 = new NewGuarantorDetailView();
        proposeGuarantor1.setGuarantorName(guarantor1);
        proposeGuarantor1.setTcgLgNo("11-23456");
        proposeGuarantor1.setCreditTypeDetailViewList(creditTypeDetailViewList);
        proposeGuarantor1.setTotalLimitGuaranteeAmount(guaranteeAmtOfEachCreditFac);

        NewGuarantorDetailView proposeGuarantor2 = new NewGuarantorDetailView();
        proposeGuarantor2.setGuarantorName(guarantor2);
        proposeGuarantor2.setTcgLgNo("22-56789");
        proposeGuarantor2.setCreditTypeDetailViewList(creditTypeDetailViewList);
        proposeGuarantor2.setTotalLimitGuaranteeAmount(guaranteeAmtOfEachCreditFac);

        BigDecimal totalGuaranteeAmount = proposeGuarantor1.getTotalLimitGuaranteeAmount().add(proposeGuarantor2.getTotalLimitGuaranteeAmount());

        List<NewGuarantorDetailView> proposeGuarantorList = new ArrayList<NewGuarantorDetailView>();
        proposeGuarantorList.add(proposeGuarantor1);
        proposeGuarantorList.add(proposeGuarantor2);
        decisionView.setProposeGuarantorList(proposeGuarantorList);
        decisionView.setProposeTotalGuaranteeAmt(totalGuaranteeAmount);

        // Approved Guarantor
        decisionView.setApproveGuarantorList(proposeGuarantorList);
        decisionView.setApproveTotalGuaranteeAmt(totalGuaranteeAmount);
    }

    @PostConstruct
    public void onCreation() {
        decisionView = decisionTransform.getDecisionView(decisionDAO.findByWorkCaseId(workCaseId));
        if (decisionView == null || decisionView.getId() == 0) {
            decisionView = new DecisionView();
        }
        initSelectItemsList();
        initSelectedView();
        setDummyData();

        // Retrieve Pricing/Fee
        creditCustomerType = RadioValue.NOT_SELECTED.value();
        creditRequestType = new CreditRequestType();
        country = new Country();

        followUpConditionView = new FollowUpConditionView();
        approvalHistory = new ApprovalHistory();
        approvalHistory.setAction("Approve CA");
        approvalHistory.setApprover(decisionControl.getApprover());
    }

    public void onAddAppProposeCredit() {
        log.debug("onAddAppProposeCredit()");
        selectedAppProposeCredit = new NewCreditDetailView();
        initSelectedView();
        modeEditCredit = false;
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
        if (decisionView.getFollowUpConditionList() != null) {
            decisionView.getFollowUpConditionList().add(followUpConditionView);
        } else {
            List<FollowUpConditionView> followUpConditionList = new ArrayList<FollowUpConditionView>();
            followUpConditionList.add(followUpConditionView);
            decisionView.setFollowUpConditionList(followUpConditionList);
        }
        // Clear form
        followUpConditionView = new FollowUpConditionView();
    }

    public void onEditAppProposeCredit() {
        log.debug("onEditAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        modeEditCredit = true;
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
        decisionView.getApproveCreditList().remove(selectedAppProposeCredit);
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() selectedAppProposeCollateral: {}", selectedAppProposeCollateral);
        decisionView.getApproveCollateralList().remove(selectedAppProposeCollateral);
    }

    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral()");

    }

    public void onDeleteAppProposeGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() selectedAppProposeGuarantor: {}", selectedAppProposeGuarantor);
        decisionView.getApproveGuarantorList().remove(selectedAppProposeGuarantor);
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
        if (decisionView.getApproveCreditList() != null) {
            List<NewCreditDetailView> proposeCreditList = decisionView.getApproveCreditList();
            if (modeEditCredit) {
                proposeCreditList.set(proposeCreditList.indexOf(selectedAppProposeCredit), selectedAppProposeCredit);
            }
            else {
                proposeCreditList.add(selectedAppProposeCredit);
            }
        }
        else {
            List<NewCreditDetailView> newApproveCreditList = new ArrayList<NewCreditDetailView>();
            newApproveCreditList.add(selectedAppProposeCredit);
            decisionView.setApproveCreditList(newApproveCreditList);
        }
    }

    // ---------- Propose Collateral Dialog ---------- //
    public void onSaveProposeCollInfo() {

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

    public FollowUpConditionView getFollowUpConditionView() {
        return followUpConditionView;
    }

    public void setFollowUpConditionView(FollowUpConditionView followUpConditionView) {
        this.followUpConditionView = followUpConditionView;
    }

    public ApprovalHistory getApprovalHistory() {
        return approvalHistory;
    }

    public void setApprovalHistory(ApprovalHistory approvalHistory) {
        this.approvalHistory = approvalHistory;
    }

    public NewCreditDetailView getSelectedAppProposeCredit() {
        return selectedAppProposeCredit;
    }

    public void setSelectedAppProposeCredit(NewCreditDetailView selectedAppProposeCredit) {
        this.selectedAppProposeCredit = selectedAppProposeCredit;
    }

    public NewCollateralInfoView getSelectedAppProposeCollateral() {
        return selectedAppProposeCollateral;
    }

    public void setSelectedAppProposeCollateral(NewCollateralInfoView selectedAppProposeCollateral) {
        this.selectedAppProposeCollateral = selectedAppProposeCollateral;
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

    public List<BaseRate> getBaseRateList() {
        return baseRateList;
    }

    public void setBaseRateList(List<BaseRate> baseRateList) {
        this.baseRateList = baseRateList;
    }

    public List<Disbursement> getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(List<Disbursement> disbursementList) {
        this.disbursementList = disbursementList;
    }

    public List<Customer> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<Customer> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public boolean isRoleUW() {
        return roleUW;
    }

    public void setRoleUW(boolean roleUW) {
        this.roleUW = roleUW;
    }

    public int getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(int creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public CreditRequestType getCreditRequestType() {
        return creditRequestType;
    }

    public void setCreditRequestType(CreditRequestType creditRequestType) {
        this.creditRequestType = creditRequestType;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public boolean isModeEditCredit() {
        return modeEditCredit;
    }

    public void setModeEditCredit(boolean modeEditCredit) {
        this.modeEditCredit = modeEditCredit;
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
}