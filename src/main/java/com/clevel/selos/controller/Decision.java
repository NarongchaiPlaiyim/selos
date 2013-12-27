package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.DecisionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.DecisionTransform;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
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
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    BasicInfoControl basicInfoControl;
    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    CustomerInfoControl customerInfoControl;

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
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    SpecialProgramDAO specialProgramDAO;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    MortgageTypeDAO mortgageTypeDAO;
    @Inject
    LoanPurposeDAO loanPurposeDAO;

    //Transform
    @Inject
    DecisionTransform decisionTransform;

    //Session
    private long workCaseId;
    private long workCasePrescreenId;

    private boolean roleUW;
    private DecisionView decisionView;
    private FollowUpConditionView followUpConditionView;
    private ApprovalHistory approvalHistory;

    private BasicInfoView basicInfoView;
    private ProductGroup productGroup;
    private SpecialProgram specialProgramBasicInfo;
    private TCGView tcgView;
    private int applyTCG;

    private int seq;
    private Hashtable hashSeqCredit;
//    private List<CreditTypeDetailView> sharedCreditTypeList;
    private List<NewCreditDetailView> sharedCreditTypeList;

    // View Selected for Add/Edit/Delete
    private NewCreditDetailView selectedAppProposeCredit;
    private NewCollateralInfoView selectedAppProposeCollateral;
    private NewSubCollateralDetailView selectedAppSubCollateral;
    private NewGuarantorDetailView selectedAppProposeGuarantor;

    // Retrieve Price/Fee
    private int creditCustomerType;
    private CreditRequestType creditRequestType;
    private Country country;

    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;

    // Propose/Approve Credit
    private BaseRate standardBaseRate;
    private BigDecimal standardInterest;
    private String standardPriceLabel;
    private BaseRate suggestBaseRate;
    private BigDecimal suggestInterest;
    private String suggestPriceLabel;
    private BaseRate finalBaseRate;
    private BigDecimal finalInterest;
    private String finalPriceRate;

    private boolean modeEditReducePricing;
    private boolean modeEditReduceFrontEndFee;
    private boolean cannotEditStandard;

    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<BaseRate> baseRateList;
    private List<LoanPurpose> loanPurposeList;
    private List<Disbursement> disbursementList;
    private int rowIndexCredit;

    // Propose/Approve - Collateral
    private List<PotentialCollateral> potentialCollateralList;
    private List<CollateralType> collateralTypeList;
    private List<SubCollateralType> subCollateralTypeList;
    private List<CustomerInfoView> collateralOwnerUwAllList;
    private List<MortgageType> mortgageTypeList;
    private List<NewSubCollateralDetailView> relatedWithAllList;
//    private List<CreditTypeDetailView> collateralCreditTypeList;
//    private List<CreditTypeDetailView> selectedCollateralCrdTypeItems;
    private List<NewCreditDetailView> collateralCreditTypeList;
    private List<NewCreditDetailView> selectedCollateralCrdTypeItems;
    private int rowIndexCollateral;
    private int rowIndexCollHead;
    private int rowIndexSubColl;

    // Propose/Approve - Guarantor
    private List<CustomerInfoView> guarantorList;
//    private List<CreditTypeDetailView> guarantorCreditTypeList;
//    private List<CreditTypeDetailView> selectedGuarantorCrdTypeItems;
    private List<NewCreditDetailView> guarantorCreditTypeList;
    private List<NewCreditDetailView> selectedGuarantorCrdTypeItems;
    private int rowIndexGuarantor;

    // FollowUp Condition
    private int rowIndexFollowUpCondition;

    //Message Dialog
    private String messageHeader;
    private String message;
    private boolean messageErr;

    // Mode
    enum ModeForButton {ADD, EDIT}
    private ModeForButton modeForButton;
    private boolean modeEditCredit;
    private boolean modeEditCollateral;
    private boolean modeEditSubColl;
    private boolean modeEditGuarantor;

    public Decision() {
    }

    private void preRender() {
        log.info("preRender ::: setSession ");
        HttpSession session = FacesUtil.getSession(true);
        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
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
        // Retrieve Pricing/Fee
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();

        // Propose Credit Dialog
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        baseRateList = baseRateDAO.findAll();
        loanPurposeList = loanPurposeDAO.findAll();
        disbursementList = disbursementDAO.findAll();

        // Propose Collateral Dialog
        potentialCollateralList = potentialCollateralDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();

        // Sub Collateral Dialog
        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralOwnerUwAllList = new ArrayList<CustomerInfoView>();
        mortgageTypeList = mortgageTypeDAO.findAll();
        // Example related with data
        relatedWithAllList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView subColl_1 = new NewSubCollateralDetailView();
        subColl_1.setId(1l);
        subColl_1.setTitleDeed("Sub-Coll 1");
        NewSubCollateralDetailView subColl_2 = new NewSubCollateralDetailView();
        subColl_2.setId(2l);
        subColl_2.setTitleDeed("Sub-Coll 2");

        // Propose Guarantor Dialog
        guarantorList = customerInfoControl.getGuarantorByWorkCase(workCaseId);
    }

    private void setDummyData() {
        // -------------------- Common Object -------------------- //
        BankAccountStatusView bankAccountStatusView = new BankAccountStatusView();
        bankAccountStatusView.setCode("01");
        bankAccountStatusView.setDescription("Normal");

        PotentialCollateral potentialCollateral;
        if (potentialCollateralList != null && !potentialCollateralList.isEmpty()) {
            potentialCollateral = potentialCollateralList.get(0);
        } else {
            potentialCollateral = new PotentialCollateral();
            potentialCollateral.setName("Core Asset");
            potentialCollateral.setDescription("Core Asset");
        }

        CollateralType collateralType;
        if (collateralTypeList != null && !collateralTypeList.isEmpty()) {
            collateralType = collateralTypeList.get(0);
        } else {
            collateralType = new CollateralType();
            collateralType.setCode("1");
            collateralType.setDescription("Land and Building");
        }

        Relation relation = new Relation();
        relation.setDescription("Borrower");

        ProductProgram productProgram = new ProductProgram();
        productProgram.setName("TMB SME SmartBiz");
        productProgram.setDescription("TMB SME SmartBiz");

        CreditType creditType = new CreditType();
        creditType.setName("Loan");
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
        creditTypeDetailView1.setAccountName("Mr.A Example");
        creditTypeDetailView1.setAccountNumber("101-1-11111-1");
        creditTypeDetailView1.setAccountSuf("111");
        creditTypeDetailView1.setType("New");
        creditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView1.setCreditFacility("Loan");
        creditTypeDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        creditTypeDetailView1.setGuaranteeAmount(BigDecimal.valueOf(1000000));

        CreditTypeDetailView creditTypeDetailView2 = new CreditTypeDetailView();
        creditTypeDetailView2.setAccountName("Mr.B Example");
        creditTypeDetailView2.setAccountNumber("102-2-22222-2");
        creditTypeDetailView2.setAccountSuf("222");
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

        CustomerInfoView guarantor1 = new CustomerInfoView();
        CustomerInfoView guarantor2 = new CustomerInfoView();
        if (guarantorList != null && guarantorList.size() > 0) {
            guarantor1 = guarantorList.get(0);
            guarantor2 = guarantorList.get(0);
        } else {
            guarantor1.setFirstNameTh("Guarantor1");
            guarantor1.setLastNameTh("LastName1");
            guarantor2.setFirstNameTh("Guarantor2");
            guarantor2.setLastNameTh("LastName2");
        }

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
        CustomerInfoView customerInfoView = new CustomerInfoView();
        customerInfoView.setFirstNameTh("ABC Co., Ltd.");
        existingGuarantor.setGuarantorName(customerInfoView);
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
        Cloner cloner = new Cloner();

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
        proposeCreditDetailView1.setRequestType(1);
        proposeCreditDetailView1.setRefinance(RadioValue.YES.value());
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
        proposeCreditDetailView2.setRefinance(RadioValue.NO.value());
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
        proposeCreditDetailView3.setRequestType(2);
        proposeCreditDetailView3.setRefinance(RadioValue.NO.value());
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
        proposeCreditDetailView4.setRequestType(2);
        proposeCreditDetailView4.setRefinance(RadioValue.NO.value());
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
        decisionView.setApproveCreditList(cloner.deepClone(proposeCreditList));
        decisionView.setApproveTotalCreditLimit(proposeTotalCreditLimit);

        // Credit Type List for (Collateral, Guarantor)
        generateSeqFromCreditList(decisionView.getApproveCreditList());

//        NewCreditFacilityView creditFacProposeView =  creditFacProposeControl.findNewCreditFacilityByWorkCase(workCaseId);
//
//        if(creditFacProposeView != null){
//            if(creditFacProposeView.getNewCreditDetailViewList().size()>0){
//                sharedCreditTypeList = creditFacProposeView.getNewCreditDetailViewList();
//            }
//        }

        sharedCreditTypeList = decisionView.getApproveCreditList();
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
        collateralCreditTypeList = cloner.deepClone(sharedCreditTypeList);

        NewCollateralInfoView proposeCollateral1 = new NewCollateralInfoView();
        proposeCollateral1.setJobID("#001");
        proposeCollateral1.setAadDecision("Accept");
        proposeCollateral1.setAadDecisionReason("Reason Example");
        proposeCollateral1.setAadDecisionReasonDetail("Remark Example");
        proposeCollateral1.setUsage("Use");
        proposeCollateral1.setTypeOfUsage("Type of usage");
        proposeCollateral1.setMortgageCondition("Mortgage Condition XXX");
        proposeCollateral1.setMortgageConditionDetail("Mortgage Condition Detail.....");
        proposeCollateral1.setBdmComments("BDM Comments Detail.....");
        proposeCollateral1.setNewCreditDetailViewList(cloner.deepClone(sharedCreditTypeList));

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
        decisionView.setApproveCollateralList(cloner.deepClone(proposeCollateralList));

        // Proposed Guarantor
        guarantorCreditTypeList = cloner.deepClone(sharedCreditTypeList);
        guaranteeAmtOfEachCreditFac = BigDecimal.ZERO;
        /*for (CreditTypeDetailView creditTypeDetailView : guarantorCreditTypeList) {
            guaranteeAmtOfEachCreditFac = guaranteeAmtOfEachCreditFac.add(creditTypeDetailView.getGuaranteeAmount());
        }*/
        for (NewCreditDetailView newCreditDetailView : guarantorCreditTypeList) {
            guaranteeAmtOfEachCreditFac = guaranteeAmtOfEachCreditFac.add(newCreditDetailView.getGuaranteeAmount());
        }

        NewGuarantorDetailView proposeGuarantor1 = new NewGuarantorDetailView();
        proposeGuarantor1.setGuarantorName(guarantor1);
        proposeGuarantor1.setTcgLgNo("11-2345");
        proposeGuarantor1.setNewCreditDetailViewList(cloner.deepClone(sharedCreditTypeList));
        proposeGuarantor1.setTotalLimitGuaranteeAmount(guaranteeAmtOfEachCreditFac);

        NewGuarantorDetailView proposeGuarantor2 = new NewGuarantorDetailView();
        proposeGuarantor2.setGuarantorName(guarantor2);
        proposeGuarantor2.setTcgLgNo("22-5678");
        proposeGuarantor2.setNewCreditDetailViewList(cloner.deepClone(sharedCreditTypeList));
        proposeGuarantor2.setTotalLimitGuaranteeAmount(guaranteeAmtOfEachCreditFac);

        BigDecimal totalGuaranteeAmount = proposeGuarantor1.getTotalLimitGuaranteeAmount().add(proposeGuarantor2.getTotalLimitGuaranteeAmount());

        List<NewGuarantorDetailView> proposeGuarantorList = new ArrayList<NewGuarantorDetailView>();
        proposeGuarantorList.add(proposeGuarantor1);
        proposeGuarantorList.add(proposeGuarantor2);
        decisionView.setProposeGuarantorList(proposeGuarantorList);
        decisionView.setProposeTotalGuaranteeAmt(totalGuaranteeAmount);

        // Approved Guarantor
        decisionView.setApproveGuarantorList(cloner.deepClone(proposeGuarantorList));
        decisionView.setApproveTotalGuaranteeAmt(totalGuaranteeAmount);
    }

    @PostConstruct
    public void onCreation() {
        preRender();

        decisionView = decisionTransform.getDecisionView(decisionDAO.findByWorkCaseId(workCaseId));
        if (decisionView == null || decisionView.getId() == 0) {
            decisionView = new DecisionView();
        }

        basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        if (basicInfoView != null) {
            productGroup = basicInfoView.getProductGroup();
            specialProgramBasicInfo = basicInfoView.getSpecialProgram();
        }

        tcgView  = tcgInfoControl.getTcgView(workCaseId);
        if(tcgView != null){
            applyTCG = tcgView.getTCG();
        }

        initSelectItemsList();

        selectedAppProposeCredit = new NewCreditDetailView();
        selectedAppProposeCollateral = new NewCollateralInfoView();
        selectedAppSubCollateral = new NewSubCollateralDetailView();
        selectedAppProposeGuarantor = new NewGuarantorDetailView();

        hashSeqCredit = new Hashtable<String, String>();

        // Retrieve Pricing/Fee
        creditCustomerType = RadioValue.NOT_SELECTED.value();
        creditRequestType = new CreditRequestType();
        country = new Country();

        followUpConditionView = new FollowUpConditionView();
        approvalHistory = new ApprovalHistory();
        approvalHistory.setAction("Approve CA");
        approvalHistory.setApprover(decisionControl.getApprover());

        setDummyData();
    }

    public void onRetrievePricingFee() {
        log.debug("onRetrievePricingFee()");
        // test create data from retrieving
        // todo: retrieve base rate by criteria?
        BaseRate baseRate = baseRateDAO.findById(1);
        NewCreditDetailView creditDetailRetrieve = new NewCreditDetailView();
        creditDetailRetrieve.setStandardBasePrice(baseRate);
        creditDetailRetrieve.setStandardInterest(BigDecimal.valueOf(-1.75));

        if ( ValidationUtil.isValueLessThanZero(creditDetailRetrieve.getStandardInterest()) ) {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " " + creditDetailRetrieve.getStandardInterest());
        } else {
            creditDetailRetrieve.setStandardPrice(creditDetailRetrieve.getStandardBasePrice().getName() + " + " + creditDetailRetrieve.getStandardInterest());
        }

        //****** tier test create ********//
        List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
        NewCreditTierDetailView newCreditTierDetailView = new NewCreditTierDetailView();
        newCreditTierDetailView.setStandardBasePrice(baseRate);
        newCreditTierDetailView.setStandardPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setStandardInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setSuggestBasePrice(baseRate);
        newCreditTierDetailView.setSuggestPrice(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setSuggestInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setFinalBasePrice(baseRate);
        newCreditTierDetailView.setFinalPriceRate(creditDetailRetrieve.getStandardPrice());
        newCreditTierDetailView.setFinalInterest(creditDetailRetrieve.getStandardInterest());
        newCreditTierDetailView.setTenor(6);
        newCreditTierDetailView.setInstallment(BigDecimal.valueOf(2000000));
        newCreditTierDetailView.setCanEdit(false);
        newCreditTierDetailViewList.add(newCreditTierDetailView);

        for (NewCreditDetailView proposeCreditDetail : decisionView.getApproveCreditList()) {
            proposeCreditDetail.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
            proposeCreditDetail.setStandardBasePrice(baseRate);
            proposeCreditDetail.setSuggestBasePrice(baseRate);
            proposeCreditDetail.setStandardInterest(creditDetailRetrieve.getStandardInterest());
            proposeCreditDetail.setSuggestInterest(creditDetailRetrieve.getSuggestInterest());
        }
    }

    // ---------- Propose Credit Dialog ---------- //
    public void onAddAppProposeCredit() {
        log.debug("onAddAppProposeCredit()");
        if(CreditCustomerType.NOT_SELECTED.value() == creditCustomerType){
            messageHeader = "Warning !!!!";
            message = "Credit Customer Type is required";
            messageErr = true;
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } else {
            selectedAppProposeCredit = new NewCreditDetailView();

            if (baseRateList != null && !baseRateList.isEmpty()) {
                selectedAppProposeCredit.setStandardBasePrice(baseRateList.get(0));
                selectedAppProposeCredit.setSuggestBasePrice(baseRateList.get(0));
            }

            if (loanPurposeList != null && !loanPurposeList.isEmpty()) {
                selectedAppProposeCredit.setLoanPurpose(loanPurposeList.get(0));
            }

            if (disbursementList != null && !disbursementList.isEmpty()) {
                selectedAppProposeCredit.setDisbursement(disbursementList.get(0));
            }

            onChangeRequestType();
            onChangeSuggestValue();

            modeEditCredit = false;
            modeForButton = ModeForButton.ADD;
//            RequestContext.getCurrentInstance().execute("proposeCreditDlg.show()");
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        }
    }

    public void onEditAppProposeCredit() {
        log.debug("onEditAppProposeCredit() selectedAppProposeCredit: {}", selectedAppProposeCredit);
        ProductProgram productProgram = selectedAppProposeCredit.getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);

        modeEditCredit = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCredit() {
        log.debug("onDeleteAppProposeCredit() rowIndexCredit: {}", rowIndexCredit);
        decisionView.getApproveCreditList().remove(rowIndexCredit);
        // todo: check credit type is use?

        BigDecimal sumTotalCreditLimit = BigDecimal.ZERO;
        for (NewCreditDetailView newCreditDetailView : Util.safetyList(decisionView.getApproveCreditList())) {
            sumTotalCreditLimit = Util.add(sumTotalCreditLimit, newCreditDetailView.getLimit());
        }
        decisionView.setApproveTotalCreditLimit(sumTotalCreditLimit);
    }

    public void onSaveAppProposeCredit() {
        log.debug("onSaveAppProposeCredit()");
        boolean success = false;

        ProductProgram productProgram = productProgramDAO.findById(selectedAppProposeCredit.getProductProgram().getId());
        CreditType creditType = creditTypeDAO.findById(selectedAppProposeCredit.getCreditType().getId());
        LoanPurpose loanPurpose = loanPurposeDAO.findById(selectedAppProposeCredit.getLoanPurpose().getId());
        Disbursement disbursement = disbursementDAO.findById(selectedAppProposeCredit.getDisbursement().getId());

        if (modeEditCredit) {
            NewCreditDetailView creditDetailEdit = decisionView.getApproveCreditList().get(rowIndexCredit);
            creditDetailEdit.setProductProgram(productProgram);
            creditDetailEdit.setCreditType(creditType);
            creditDetailEdit.setRequestType(selectedAppProposeCredit.getRequestType());
            creditDetailEdit.setRefinance(selectedAppProposeCredit.getRefinance());
            creditDetailEdit.setProductCode(selectedAppProposeCredit.getProductCode());
            creditDetailEdit.setProjectCode(selectedAppProposeCredit.getProjectCode());
            creditDetailEdit.setLimit(selectedAppProposeCredit.getLimit());
            creditDetailEdit.setPCEPercent(selectedAppProposeCredit.getPCEPercent());
            creditDetailEdit.setPCEAmount(selectedAppProposeCredit.getPCEAmount());
            creditDetailEdit.setReducePriceFlag(selectedAppProposeCredit.isReducePriceFlag());
            creditDetailEdit.setReduceFrontEndFee(selectedAppProposeCredit.isReduceFrontEndFee());
            creditDetailEdit.setStandardBasePrice(selectedAppProposeCredit.getStandardBasePrice());
            creditDetailEdit.setStandardInterest(selectedAppProposeCredit.getStandardInterest());
            creditDetailEdit.setSuggestBasePrice(selectedAppProposeCredit.getSuggestBasePrice());
            creditDetailEdit.setSuggestInterest(selectedAppProposeCredit.getSuggestInterest());
            creditDetailEdit.setFrontEndFee(selectedAppProposeCredit.getFrontEndFee());
            creditDetailEdit.setLoanPurpose(loanPurpose);
            creditDetailEdit.setRemark(selectedAppProposeCredit.getRemark());
            creditDetailEdit.setDisbursement(disbursement);
            creditDetailEdit.setHoldLimitAmount(selectedAppProposeCredit.getHoldLimitAmount());
            creditDetailEdit.setNewCreditTierDetailViewList(selectedAppProposeCredit.getNewCreditTierDetailViewList());

            success = true;
        }
        else {
            // Add New
            NewCreditDetailView creditDetailAdd = new NewCreditDetailView();
            creditDetailAdd.setProductProgram(productProgram);
            creditDetailAdd.setCreditType(creditType);
            creditDetailAdd.setRequestType(selectedAppProposeCredit.getRequestType());
            creditDetailAdd.setRefinance(selectedAppProposeCredit.getRefinance());
            creditDetailAdd.setProductCode(selectedAppProposeCredit.getProductCode());
            creditDetailAdd.setProjectCode(selectedAppProposeCredit.getProjectCode());
            creditDetailAdd.setLimit(selectedAppProposeCredit.getLimit());
            creditDetailAdd.setPCEPercent(selectedAppProposeCredit.getPCEPercent());
            creditDetailAdd.setPCEAmount(selectedAppProposeCredit.getPCEAmount());
            creditDetailAdd.setReducePriceFlag(selectedAppProposeCredit.isReducePriceFlag());
            creditDetailAdd.setReduceFrontEndFee(selectedAppProposeCredit.isReduceFrontEndFee());
            creditDetailAdd.setStandardBasePrice(selectedAppProposeCredit.getStandardBasePrice());
            creditDetailAdd.setStandardInterest(selectedAppProposeCredit.getStandardInterest());
            creditDetailAdd.setSuggestBasePrice(selectedAppProposeCredit.getSuggestBasePrice());
            creditDetailAdd.setSuggestInterest(selectedAppProposeCredit.getSuggestInterest());
            creditDetailAdd.setFrontEndFee(selectedAppProposeCredit.getFrontEndFee());
            creditDetailAdd.setLoanPurpose(loanPurpose);
            creditDetailAdd.setRemark(selectedAppProposeCredit.getRemark());
            creditDetailAdd.setDisbursement(disbursement);
            creditDetailAdd.setHoldLimitAmount(selectedAppProposeCredit.getHoldLimitAmount());
            creditDetailAdd.setNewCreditTierDetailViewList(selectedAppProposeCredit.getNewCreditTierDetailViewList());
            creditDetailAdd.setSeq(seq);

            if (decisionView.getApproveCreditList() != null) {
                decisionView.getApproveCreditList().add(creditDetailAdd);
            } else {
                List<NewCreditDetailView> newApproveCreditList = new ArrayList<NewCreditDetailView>();
                newApproveCreditList.add(creditDetailAdd);
                decisionView.setApproveCreditList(newApproveCreditList);
            }

            success = true;
        }

        BigDecimal sumTotalCreditLimit = BigDecimal.ZERO;
        for (NewCreditDetailView newCreditDetailView : Util.safetyList(decisionView.getApproveCreditList())) {
            sumTotalCreditLimit = Util.add(sumTotalCreditLimit, newCreditDetailView.getLimit());
        }
        decisionView.setApproveTotalCreditLimit(sumTotalCreditLimit);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onChangeRequestType() {
        log.debug("onChangeRequestType() requestType = {}", selectedAppProposeCredit.getRequestType());
        prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();

        if (RequestTypes.CHANGE.value() == selectedAppProposeCredit.getRequestType()) {   //change
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll();
            selectedAppProposeCredit.getProductProgram().setId(0);
            cannotEditStandard = false;
        }
        else if (RequestTypes.NEW.value() == selectedAppProposeCredit.getRequestType()) {  //new
            if (productGroup != null) {
                prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
                selectedAppProposeCredit.getCreditType().setId(0);
            }
            cannotEditStandard = true;
        }
    }

    public void onChangeProductProgram() {
        log.debug("onChangeProductProgram() productProgram.id = {}", selectedAppProposeCredit.getProductProgram().getId());
        selectedAppProposeCredit.setProductCode("");
        selectedAppProposeCredit.setProjectCode("");

        ProductProgram productProgram = productProgramDAO.findById(selectedAppProposeCredit.getProductProgram().getId());
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        selectedAppProposeCredit.getCreditType().setId(0);
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType() creditType.id: {}", selectedAppProposeCredit.getCreditType().getId());
        if ((selectedAppProposeCredit.getProductProgram().getId() != 0) && (selectedAppProposeCredit.getCreditType().getId() != 0)) {
            ProductProgram productProgram = productProgramDAO.findById(selectedAppProposeCredit.getProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById(selectedAppProposeCredit.getCreditType().getId());
            //productFormulaDAO
            //ProductProgramFacilityId , CreditCusType (prime/normal),applyTCG (TCG),spec_program_id(basicInfo)
            if (productProgram != null && creditType != null) {
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType, productProgram);

                if((prdProgramToCreditType != null && prdProgramToCreditType.getId() != 0)
                        && (specialProgramBasicInfo != null && specialProgramBasicInfo.getId() != 0)){
                    log.info("onChangeCreditType() :: prdProgramToCreditType :: {}", prdProgramToCreditType.getId());
                    log.info("onChangeCreditType() :: creditCustomerType :: {}", creditCustomerType);
                    log.info("onChangeCreditType() :: specialProgramBasicInfo :: {}",specialProgramBasicInfo.getId());
                    log.info("onChangeCreditType() :: applyTCG :: {}", applyTCG);
                    SpecialProgram specialProgram = specialProgramDAO.findById(specialProgramBasicInfo.getId());
                    ProductFormula productFormula = productFormulaDAO.findProductFormulaForPropose(prdProgramToCreditType, creditCustomerType, specialProgram, applyTCG);

                    if (productFormula != null) {
                        log.debug("onChangeCreditType() :::: productFormula : {}", productFormula.getId());
                        selectedAppProposeCredit.setProductCode(productFormula.getProductCode());
                        selectedAppProposeCredit.setProjectCode(productFormula.getProjectCode());

                        log.debug("productFormula.", productFormula.getReducePricing());
                        log.debug("productFormula.", productFormula.getReducePricing());
                        modeEditReducePricing = productFormula.getReducePricing() == 1 ? true : false;
                        modeEditReduceFrontEndFee = productFormula.getReduceFrontEndFee() == 1 ? true : false;
                    }
                }
            }
        }
    }

    public void onChangeSuggestValue() {
        log.info("onChangeSuggestValue(standardBaseRate.id: {}, standardInterest: {}, suggestBaseRate.id: {}, suggestInterest: {})",
                selectedAppProposeCredit.getStandardBasePrice().getId(), selectedAppProposeCredit.getStandardInterest(),
                selectedAppProposeCredit.getSuggestBasePrice().getId(), selectedAppProposeCredit.getSuggestInterest());

        Object[] values = creditFacProposeControl.findFinalPriceRate(
                selectedAppProposeCredit.getStandardBasePrice().getId(),
                selectedAppProposeCredit.getStandardInterest(),
                selectedAppProposeCredit.getSuggestBasePrice().getId(),
                selectedAppProposeCredit.getSuggestInterest()
        );

        finalBaseRate = (BaseRate) values[0];
        finalInterest = (BigDecimal) values[1];
        finalPriceRate = (String) values[2];
        standardBaseRate = (BaseRate) values[3];
        standardPriceLabel = (String) values[4];
        suggestBaseRate = (BaseRate) values[5];
        suggestPriceLabel = (String) values[6];
    }

    public void onAddTierInfo() {
        log.debug("onAddTierInfo(standardBaseRate.id: {}, standardInterest: {}, suggestBaseRate.id: {}, suggestInterest: {}, finalBaseRate.id: {}, finalInterest: {})",
                standardBaseRate.getId(), selectedAppProposeCredit.getStandardInterest(),
                suggestBaseRate.getId(), selectedAppProposeCredit.getSuggestInterest(),
                finalBaseRate.getId(), finalInterest);

        NewCreditTierDetailView newCreditTierDetail = new NewCreditTierDetailView();
        newCreditTierDetail.setFinalBasePrice(finalBaseRate);
        newCreditTierDetail.setFinalInterest(finalInterest);
        newCreditTierDetail.setFinalPriceRate(finalPriceRate);
        newCreditTierDetail.setStandardBasePrice(standardBaseRate);
        newCreditTierDetail.setStandardInterest(selectedAppProposeCredit.getStandardInterest());
        newCreditTierDetail.setStandardPrice(standardPriceLabel);
        newCreditTierDetail.setSuggestBasePrice(suggestBaseRate);
        newCreditTierDetail.setSuggestInterest(selectedAppProposeCredit.getSuggestInterest());
        newCreditTierDetail.setSuggestPrice(suggestPriceLabel);
        newCreditTierDetail.setCanEdit(true);

        if (selectedAppProposeCredit.getNewCreditTierDetailViewList() != null) {
            selectedAppProposeCredit.getNewCreditTierDetailViewList().add(newCreditTierDetail);
        } else {
            List<NewCreditTierDetailView> newCreditTierDetailViewList = new ArrayList<NewCreditTierDetailView>();
            newCreditTierDetailViewList.add(newCreditTierDetail);
            selectedAppProposeCredit.setNewCreditTierDetailViewList(newCreditTierDetailViewList);
        }
    }

    public void onDeleteTierInfo(int rowIndex) {
        selectedAppProposeCredit.getNewCreditTierDetailViewList().remove(rowIndex);
    }

    // ---------- Propose Collateral Dialog ---------- //
    public void onEditAppProposeCollateral() {
        log.debug("onEditAppProposeCollateral() selectedAppProposeCollateral: {}", selectedAppProposeCollateral);
        if (selectedAppProposeCollateral.getNewCreditDetailViewList() != null && selectedAppProposeCollateral.getNewCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedCollateralCrdTypeItems = selectedAppProposeCollateral.getNewCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            collateralCreditTypeList = cloner.deepClone(sharedCreditTypeList);
        }

        modeEditCollateral = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onDeleteAppProposeCollateral() {
        log.debug("onDeleteAppProposeCollateral() rowIndexCollateral: {}", rowIndexCollateral);
        decisionView.getApproveCollateralList().remove(rowIndexCollateral);
    }

    public void onSaveProposeCollInfo() {
        log.debug("onSaveProposeCollInfo()");
        boolean success = false;

        log.debug("===> Edit - Collateral: {}", selectedAppProposeCollateral);
        NewCollateralInfoView collateralInfoEdit = decisionView.getApproveCollateralList().get(rowIndexCollateral);
        collateralInfoEdit.setJobID(selectedAppProposeCollateral.getJobID());
        collateralInfoEdit.setAppraisalDate(selectedAppProposeCollateral.getAppraisalDate());
        collateralInfoEdit.setAadDecision(selectedAppProposeCollateral.getAadDecision());
        collateralInfoEdit.setAadDecisionReason(selectedAppProposeCollateral.getAadDecisionReason());
        collateralInfoEdit.setAadDecisionReasonDetail(selectedAppProposeCollateral.getAadDecisionReasonDetail());
        collateralInfoEdit.setUsage(selectedAppProposeCollateral.getUsage());
        collateralInfoEdit.setTypeOfUsage(selectedAppProposeCollateral.getTypeOfUsage());
        collateralInfoEdit.setApproved(selectedAppProposeCollateral.getApproved());
        collateralInfoEdit.setUwDecision(selectedAppProposeCollateral.getApproved() == 2 ? "Approved"
                : selectedAppProposeCollateral.getApproved() == 1 ? "Not Approved" : "");
        collateralInfoEdit.setUwRemark(selectedAppProposeCollateral.getUwRemark());
        collateralInfoEdit.setMortgageCondition(selectedAppProposeCollateral.getMortgageCondition());
        collateralInfoEdit.setMortgageConditionDetail(selectedAppProposeCollateral.getMortgageConditionDetail());
        collateralInfoEdit.setBdmComments(selectedAppProposeCollateral.getBdmComments());
        collateralInfoEdit.setNewCollateralHeadDetailViewList(selectedAppProposeCollateral.getNewCollateralHeadDetailViewList());

        if (selectedCollateralCrdTypeItems != null && selectedCollateralCrdTypeItems.size() > 0) {
            collateralInfoEdit.getNewCreditDetailViewList().clear();
            for (NewCreditDetailView creditTypeItem : selectedCollateralCrdTypeItems) {
                collateralInfoEdit.getNewCreditDetailViewList().add(creditTypeItem);
            }

            success = true;
            log.debug("Success: Edit Collateral from ApproveCollateralList");
        } else {
            messageHeader = "Error Message";
            message = "Non selected Credit Type!";
            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            log.error("Failed: Can not edit Collateral from ApproveCollateralList because non selected credit type!");
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onCallRetrieveAppraisalReportInfo() {
        log.debug("onCallRetrieveAppraisalReportInfo()");
    }

    public void onAddSubCollateral() {
        log.debug("onAddSubCollateral()");
        selectedAppSubCollateral = new NewSubCollateralDetailView();
        modeEditSubColl = false;
    }

    public void onEditSubCollateral() {
        log.debug("onEditSubCollateral()");
        modeEditSubColl = true;
    }


    public void onDeleteSubCollateral() {
        log.debug("onDeleteSubCollateral() rowIndexCollateral, rowIndexCollHead, rowIndexSubColl: {}",
                rowIndexCollateral, rowIndexCollHead, rowIndexSubColl);
        decisionView.getApproveCollateralList().get(rowIndexCollHead)
                .getNewCollateralHeadDetailViewList().get(rowIndexCollateral)
                .getNewSubCollateralDetailViewList().get(rowIndexSubColl);
    }

    public void onSaveSubCollateral() {
        log.debug("onSaveSubCollateral()");
        boolean success = false;

        if (modeEditSubColl) {
            log.debug("===> Edit - SubCollateral: {}", selectedAppSubCollateral);
            NewSubCollateralDetailView subCollEdit = selectedAppProposeCollateral.getNewCollateralHeadDetailViewList().get(rowIndexCollHead)
                    .getNewSubCollateralDetailViewList().get(rowIndexSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedAppSubCollateral.getSubCollateralType().getId());
            subCollEdit.setSubCollateralType(subCollateralType);
            subCollEdit.setTitleDeed(selectedAppSubCollateral.getTitleDeed());
            subCollEdit.setAddress(selectedAppSubCollateral.getAddress());
            subCollEdit.setLandOffice(selectedAppSubCollateral.getLandOffice());
            subCollEdit.setCollateralOwnerAAD(selectedAppSubCollateral.getCollateralOwnerAAD());
            subCollEdit.setCollateralOwnerUWList(selectedAppSubCollateral.getCollateralOwnerUWList());
            subCollEdit.setMortgageList(selectedAppSubCollateral.getMortgageList());
            subCollEdit.setRelatedWithList(selectedAppSubCollateral.getRelatedWithList());
            subCollEdit.setAppraisalValue(selectedAppSubCollateral.getAppraisalValue());
            subCollEdit.setMortgageValue(selectedAppSubCollateral.getMortgageValue());

            success = true;
        }
        else {
            //Add New
            log.debug("===> Add New - SubCollateral: {}", selectedAppSubCollateral);
            NewSubCollateralDetailView subCollAdd = new NewSubCollateralDetailView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(selectedAppSubCollateral.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(selectedAppSubCollateral.getTitleDeed());
            subCollAdd.setAddress(selectedAppSubCollateral.getAddress());
            subCollAdd.setLandOffice(selectedAppSubCollateral.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(selectedAppSubCollateral.getCollateralOwnerAAD());
            subCollAdd.setCollateralOwnerUWList(selectedAppSubCollateral.getCollateralOwnerUWList());
            subCollAdd.setMortgageList(selectedAppSubCollateral.getMortgageList());
            subCollAdd.setRelatedWithList(selectedAppSubCollateral.getRelatedWithList());
            subCollAdd.setAppraisalValue(selectedAppSubCollateral.getAppraisalValue());
            subCollAdd.setMortgageValue(selectedAppSubCollateral.getMortgageValue());

            if (selectedAppProposeCollateral.getNewCollateralHeadDetailViewList().get(rowIndexCollHead).getNewSubCollateralDetailViewList() == null) {
                selectedAppProposeCollateral.getNewCollateralHeadDetailViewList().get(rowIndexCollHead).setNewSubCollateralDetailViewList(new ArrayList<NewSubCollateralDetailView>());
            }

            selectedAppProposeCollateral.getNewCollateralHeadDetailViewList().get(rowIndexCollHead)
                    .getNewSubCollateralDetailViewList().add(subCollAdd);

            success = true;
        }

        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onAddCollateralOwnerUW() {
        log.debug("onAddCollateralOwnerUW() collateralOwnerUW.id: {}", selectedAppSubCollateral.getCollateralOwnerUW().getId());
        if (selectedAppSubCollateral.getCollateralOwnerUW().getId() == 0) {
            log.error("Can not add CollateralOwnerUw because id = 0!");
            return;
        }

        CustomerInfoView collateralOwnerAdd = customerInfoControl.getCustomerById(selectedAppSubCollateral.getCollateralOwnerUW());
        if (selectedAppSubCollateral.getCollateralOwnerUWList() == null) {
            selectedAppSubCollateral.setCollateralOwnerUWList(new ArrayList<CustomerInfoView>());
        }
        selectedAppSubCollateral.getCollateralOwnerUWList().add(collateralOwnerAdd);
    }

    public void onDeleteCollateralOwnerUW(int rowIndex) {
        log.debug("onDeleteCollateralOwnerUW(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getCollateralOwnerUWList().remove(rowIndex);
    }

    public void onAddMortgageType() {
        log.debug("onAddMortgageType() mortgageType.id: {}", selectedAppSubCollateral.getMortgageType().getId());
        if (selectedAppSubCollateral.getMortgageType().getId() == 0) {
            log.error("Can not add MortgageType because id = 0!");
            return;
        }

        MortgageType mortgageType = mortgageTypeDAO.findById(selectedAppSubCollateral.getMortgageType().getId());
        if (selectedAppSubCollateral.getMortgageList() == null) {
            selectedAppSubCollateral.setMortgageList(new ArrayList<MortgageType>());
        }
        selectedAppSubCollateral.getMortgageList().add(mortgageType);
    }

    public void onDeleteMortgageType(int rowIndex) {
        log.debug("onDeleteMortgageType(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getMortgageList().remove(rowIndex);
    }

    public void onAddRelatedWith() {
        log.debug("onAddRelatedWith() selectedAppSubCollateral.relatedWithId = {}", selectedAppSubCollateral.getRelatedWithId());
        if (selectedAppSubCollateral.getRelatedWithId() == 0) {
            log.error("Can not add RelatedWith because id = 0!");
            return;
        }

        NewSubCollateralDetailView relatedWith = getIdNewSubCollateralDetail(selectedAppSubCollateral.getRelatedWithId());
        if (selectedAppSubCollateral.getRelatedWithList() != null) {
            selectedAppSubCollateral.setRelatedWithList(new ArrayList<NewSubCollateralDetailView>());
        }
        selectedAppSubCollateral.getRelatedWithList().add(relatedWith);
    }

    public NewSubCollateralDetailView getIdNewSubCollateralDetail(long newSubCollateralId) {
        NewSubCollateralDetailView newSubCollateralReturn = new NewSubCollateralDetailView();
        for (NewCollateralInfoView newCollateralInfoView : Util.safetyList(decisionView.getApproveCollateralList())) {
            for (NewCollateralHeadDetailView newCollateralHeadDetailView : Util.safetyList(newCollateralInfoView.getNewCollateralHeadDetailViewList())) {
                for (NewSubCollateralDetailView newSubCollateralDetailOnAdded : Util.safetyList(newCollateralHeadDetailView.getNewSubCollateralDetailViewList())) {
                    log.info("newSubCollateralDetailView1 id ::: {}", newSubCollateralDetailOnAdded.getId());
                    log.info("newSubCollateralDetailView1 title deed ::: {}", newSubCollateralDetailOnAdded.getTitleDeed());
                    if (newSubCollateralId == newSubCollateralDetailOnAdded.getId()) {
                        newSubCollateralReturn = newSubCollateralDetailOnAdded;
                    }
                }
            }
        }
        return newSubCollateralReturn;
    }

    public void onDeleteRelatedWith(int rowIndex) {
        log.debug("onDeleteRelatedWith(rowIndex: {})", rowIndex);
        selectedAppSubCollateral.getRelatedWithList().remove(rowIndex);
    }

    // ---------- Propose Guarantor Dialog ---------- //
    public void onAddAppProposeGuarantor() {
        log.debug("onAddAppProposeGuarantor()");
        selectedAppProposeGuarantor = new NewGuarantorDetailView();
        selectedGuarantorCrdTypeItems = new ArrayList<NewCreditDetailView>();
        Cloner cloner = new Cloner();
        guarantorCreditTypeList = cloner.deepClone(sharedCreditTypeList);

        modeEditGuarantor = false;
        modeForButton = ModeForButton.ADD;
    }

    public void onEditAppProposeGuarantor() {
        log.debug("onEditAppProposeGuarantor() selectedAppProposeGuarantor: {}", selectedAppProposeGuarantor);
        if (selectedAppProposeGuarantor.getNewCreditDetailViewList() != null && selectedAppProposeGuarantor.getNewCreditDetailViewList().size() > 0) {
            // set selected credit type items (check/uncheck)
            selectedGuarantorCrdTypeItems = selectedAppProposeGuarantor.getNewCreditDetailViewList();
            // update Guarantee Amount before render dialog
            Cloner cloner = new Cloner();
            guarantorCreditTypeList = cloner.deepClone(sharedCreditTypeList);
            for (NewCreditDetailView creditTypeFromAll : guarantorCreditTypeList) {
                for (NewCreditDetailView creditTypeFromSelected : selectedAppProposeGuarantor.getNewCreditDetailViewList()) {
                    if (creditTypeFromAll.getSeq() == creditTypeFromSelected.getSeq()) {
                        creditTypeFromAll.setGuaranteeAmount(creditTypeFromSelected.getGuaranteeAmount());
                    }
                }
            }
        }

        modeEditGuarantor = true;
        modeForButton = ModeForButton.EDIT;
    }

    public void onSaveGuarantorInfo() {
        log.debug("onSaveGuarantorInfo()");
        log.debug("selectedGuarantorCrdTypeItems.size() = {}", selectedGuarantorCrdTypeItems.size());
        log.debug("selectedGuarantorCrdTypeItems: {}", selectedGuarantorCrdTypeItems);

        boolean success = false;
        BigDecimal sumGuaranteeAmtPerCrdType = BigDecimal.ZERO;

        if (modeEditGuarantor) {
            log.debug("===> Edit - Guarantor: {}", selectedAppProposeGuarantor);
            NewGuarantorDetailView guarantorDetailEdit = decisionView.getApproveGuarantorList().get(rowIndexGuarantor);
            guarantorDetailEdit.setGuarantorName(getByIdFromGuarantorList(selectedAppProposeGuarantor.getGuarantorName().getId()));
            guarantorDetailEdit.setTcgLgNo(selectedAppProposeGuarantor.getTcgLgNo());

            // needed to select credit type items
            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
                guarantorDetailEdit.getNewCreditDetailViewList().clear();
                for (NewCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    guarantorDetailEdit.getNewCreditDetailViewList().add(creditTypeItem);
                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
                }
                guarantorDetailEdit.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);
                success = true;
                log.debug("Success: Edit Guarantor from ApproveGuarantorList");
            }
            else {
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                log.error("Failed: Can not edit Guarantor from ApproveGuarantorList because non selected credit type!");
            }
        }
        else {
            // Add New
            log.debug("===> Add New - Guarantor: {}", selectedAppProposeGuarantor);
            NewGuarantorDetailView guarantorDetailAdd = new NewGuarantorDetailView();
            guarantorDetailAdd.setApproved(selectedAppProposeGuarantor.getApproved());
            guarantorDetailAdd.setGuarantorName(getByIdFromGuarantorList(selectedAppProposeGuarantor.getGuarantorName().getId()));
            guarantorDetailAdd.setTcgLgNo(selectedAppProposeGuarantor.getTcgLgNo());

            // needed to select credit type items
            if (selectedGuarantorCrdTypeItems != null && selectedGuarantorCrdTypeItems.size() > 0) {
                for (NewCreditDetailView creditTypeItem : selectedGuarantorCrdTypeItems) {
                    guarantorDetailAdd.getNewCreditDetailViewList().add(creditTypeItem);
                    sumGuaranteeAmtPerCrdType = sumGuaranteeAmtPerCrdType.add(creditTypeItem.getGuaranteeAmount());
                }
                guarantorDetailAdd.setTotalLimitGuaranteeAmount(sumGuaranteeAmtPerCrdType);

                if (decisionView.getApproveGuarantorList() != null) {
                    decisionView.getApproveGuarantorList().add(guarantorDetailAdd);
                } else {
                    List<NewGuarantorDetailView> newApproveGuarantorList = new ArrayList<NewGuarantorDetailView>();
                    newApproveGuarantorList.add(guarantorDetailAdd);
                    decisionView.setApproveGuarantorList(newApproveGuarantorList);
                }

                success = true;
                log.debug("Success: Add new Guarantor to ApproveGuarantorList.");
            }
            else {
                messageHeader = "Error Message";
                message = "Non selected Credit Type!";
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                log.error("Failed: Can not add new Guarantor to ApproveGuarantorList because non selected credit type!");
            }
        }

        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", success);
    }

    public void onDeleteAppProposeGuarantor() {
        log.debug("onDeleteAppProposeGuarantor() rowIndexGuarantor: {}", rowIndexGuarantor);
        decisionView.getApproveGuarantorList().remove(rowIndexGuarantor);
        decisionView.setApproveTotalGuaranteeAmt(creditFacProposeControl.calTotalGuaranteeAmount(decisionView.getApproveGuarantorList()));
    }

    // ---------- FollowUp Condition & (Save/Cancel) Decision ---------- //
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

    public void onDeleteFollowUpCondition() {
        log.debug("onDeleteFollowUpCondition() rowIndexFollowUpCondition: {}", rowIndexFollowUpCondition);
        decisionView.getFollowUpConditionList().remove(rowIndexFollowUpCondition);
    }

    public void onSave() {
        log.debug("onSave()");
    }

    public void onCancel() {
        log.debug("onCancel()");
    }

    private void generateSeqFromCreditList(List<NewCreditDetailView> newCreditDetailViewList) {
        int seq = 0;
        for (NewCreditDetailView newCreditDetailView : Util.safetyList(newCreditDetailViewList)) {
            newCreditDetailView.setSeq(++seq);
        }
    }

    private CustomerInfoView getByIdFromGuarantorList(long id) {
        CustomerInfoView returnGuarantor = new CustomerInfoView();
        for (CustomerInfoView guarantor : Util.safetyList(guarantorList)) {
            if (guarantor.getId() == id) {
                returnGuarantor = guarantor;
                break;
            }
        }
        return returnGuarantor;
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

    public List<CustomerInfoView> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<CustomerInfoView> guarantorList) {
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

    public int getRowIndexCredit() {
        return rowIndexCredit;
    }

    public void setRowIndexCredit(int rowIndexCredit) {
        this.rowIndexCredit = rowIndexCredit;
    }

    public int getRowIndexGuarantor() {
        return rowIndexGuarantor;
    }

    public void setRowIndexGuarantor(int rowIndexGuarantor) {
        this.rowIndexGuarantor = rowIndexGuarantor;
    }

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
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

    public List<MortgageType> getMortgageTypeList() {
        return mortgageTypeList;
    }

    public void setMortgageTypeList(List<MortgageType> mortgageTypeList) {
        this.mortgageTypeList = mortgageTypeList;
    }

    public List<NewSubCollateralDetailView> getRelatedWithAllList() {
        return relatedWithAllList;
    }

    public void setRelatedWithAllList(List<NewSubCollateralDetailView> relatedWithAllList) {
        this.relatedWithAllList = relatedWithAllList;
    }

    public int getRowIndexCollHead() {
        return rowIndexCollHead;
    }

    public void setRowIndexCollHead(int rowIndexCollHead) {
        this.rowIndexCollHead = rowIndexCollHead;
    }

    public int getRowIndexSubColl() {
        return rowIndexSubColl;
    }

    public void setRowIndexSubColl(int rowIndexSubColl) {
        this.rowIndexSubColl = rowIndexSubColl;
    }

    public NewSubCollateralDetailView getSelectedAppSubCollateral() {
        return selectedAppSubCollateral;
    }

    public void setSelectedAppSubCollateral(NewSubCollateralDetailView selectedAppSubCollateral) {
        this.selectedAppSubCollateral = selectedAppSubCollateral;
    }

    public List<LoanPurpose> getLoanPurposeList() {
        return loanPurposeList;
    }

    public void setLoanPurposeList(List<LoanPurpose> loanPurposeList) {
        this.loanPurposeList = loanPurposeList;
    }

    public int getRowIndexCollateral() {
        return rowIndexCollateral;
    }

    public void setRowIndexCollateral(int rowIndexCollateral) {
        this.rowIndexCollateral = rowIndexCollateral;
    }

    public List<NewCreditDetailView> getSharedCreditTypeList() {
        return sharedCreditTypeList;
    }

    public void setSharedCreditTypeList(List<NewCreditDetailView> sharedCreditTypeList) {
        this.sharedCreditTypeList = sharedCreditTypeList;
    }

    public List<NewCreditDetailView> getCollateralCreditTypeList() {
        return collateralCreditTypeList;
    }

    public void setCollateralCreditTypeList(List<NewCreditDetailView> collateralCreditTypeList) {
        this.collateralCreditTypeList = collateralCreditTypeList;
    }

    public List<NewCreditDetailView> getSelectedCollateralCrdTypeItems() {
        return selectedCollateralCrdTypeItems;
    }

    public void setSelectedCollateralCrdTypeItems(List<NewCreditDetailView> selectedCollateralCrdTypeItems) {
        this.selectedCollateralCrdTypeItems = selectedCollateralCrdTypeItems;
    }

    public List<NewCreditDetailView> getSelectedGuarantorCrdTypeItems() {
        return selectedGuarantorCrdTypeItems;
    }

    public void setSelectedGuarantorCrdTypeItems(List<NewCreditDetailView> selectedGuarantorCrdTypeItems) {
        this.selectedGuarantorCrdTypeItems = selectedGuarantorCrdTypeItems;
    }

    public List<NewCreditDetailView> getGuarantorCreditTypeList() {
        return guarantorCreditTypeList;
    }

    public void setGuarantorCreditTypeList(List<NewCreditDetailView> guarantorCreditTypeList) {
        this.guarantorCreditTypeList = guarantorCreditTypeList;
    }

    public boolean isModeEditSubColl() {
        return modeEditSubColl;
    }

    public void setModeEditSubColl(boolean modeEditSubColl) {
        this.modeEditSubColl = modeEditSubColl;
    }

    public int getRowIndexFollowUpCondition() {
        return rowIndexFollowUpCondition;
    }

    public void setRowIndexFollowUpCondition(int rowIndexFollowUpCondition) {
        this.rowIndexFollowUpCondition = rowIndexFollowUpCondition;
    }

    public boolean isModeEditReducePricing() {
        return modeEditReducePricing;
    }

    public void setModeEditReducePricing(boolean modeEditReducePricing) {
        this.modeEditReducePricing = modeEditReducePricing;
    }

    public boolean isModeEditReduceFrontEndFee() {
        return modeEditReduceFrontEndFee;
    }

    public void setModeEditReduceFrontEndFee(boolean modeEditReduceFrontEndFee) {
        this.modeEditReduceFrontEndFee = modeEditReduceFrontEndFee;
    }

    public boolean isCannotEditStandard() {
        return cannotEditStandard;
    }

    public void setCannotEditStandard(boolean cannotEditStandard) {
        this.cannotEditStandard = cannotEditStandard;
    }
}