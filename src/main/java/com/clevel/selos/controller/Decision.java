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
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
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
    private List<NewConditionDetailView> existingConditionCommCrdList;
    private ExistingCollateralView existingColBorrower;
    private ExistingCollateralView existingColRelated;
    private List<NewGuarantorDetailView> existingGuarantorList;
    private BigDecimal existingTotalGuaranteeAmount;

    //----- Propose Credit -----//
    private CreditFacProposeView creditFacProposeView;
    // Approved Propose (Credit, Collateral, Guarantor)
    private NewCreditDetailView approvedProposeCredit;
    private List<NewCreditDetailView> approvedProposeCreditList;
    private List<NewCollateralInfoView> approvedProposeCollateralList;
    private List<NewGuarantorDetailView> approvedProposeGuarantorList;
    // Propose Condition
    private NewConditionDetailView proposeCondition;
    private Date proposeConditionDate;

    // Select Items - Propose Credit
    private List<CreditRequestType> creditRequestTypeList;
    private CreditRequestType creditRequestTypeSelected;
    private List<Country> investedCountryList;
    private Country investedCountrySelected;

    public Decision() {
    }

    @PostConstruct
    public void onCreation() {
        //========================================= Existing =========================================//
        existingCreditView = new ExistingCreditView();

        //========================================= Propose =========================================//
        creditFacProposeView = new CreditFacProposeView();
        // Proposed Credit
        List<NewCreditDetailView> newCreditDetailViewList = new ArrayList<NewCreditDetailView>();

        ProductProgram productProgram = new ProductProgram();
        productProgram.setDescription("TMB SME SmartBiz");
        CreditType creditType = new CreditType();
        creditType.setDescription("Loan");
        Disbursement disbursement = new Disbursement();
        disbursement.setDisbursement("Normal Disbursement");

        //----------------------------------------- 1 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList1 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView1 = new CreditTierDetailView();
        tierDetailView1.setStandardPrice("MLR+1.00");
        tierDetailView1.setSuggestPrice("MLR+1.00");
        tierDetailView1.setFinalPriceRate("MLR+1.00");
        tierDetailView1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView1.setTenor(3);
        tierDetailViewList1.add(tierDetailView1);

        NewCreditDetailView newCreditDetailView1 = new NewCreditDetailView();
        newCreditDetailView1.setProductProgram(productProgram);
        newCreditDetailView1.setCreditType(creditType);
        newCreditDetailView1.setProductCode("EAC1");
        newCreditDetailView1.setProjectCode("90074");
        newCreditDetailView1.setLimit(BigDecimal.valueOf(123456.78));
        newCreditDetailView1.setFrontEndFee(BigDecimal.valueOf(1.75));
        newCreditDetailView1.setCreditTierDetailViewList(tierDetailViewList1);
        newCreditDetailView1.setRequestType(0);
        newCreditDetailView1.setRefinance(0);
        newCreditDetailView1.setRemark("Purpose Detail Example");
        newCreditDetailView1.setDisbursement(disbursement);
        newCreditDetailView1.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

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

        NewCreditDetailView newCreditDetailView2 = new NewCreditDetailView();
        newCreditDetailView2.setProductProgram(productProgram);
        newCreditDetailView2.setCreditType(creditType);
        newCreditDetailView2.setProductCode("EAC2");
        newCreditDetailView2.setProjectCode("90078");
        newCreditDetailView2.setLimit(BigDecimal.valueOf(123456.78));
        newCreditDetailView2.setFrontEndFee(BigDecimal.valueOf(1.75));
        newCreditDetailView2.setCreditTierDetailViewList(tierDetailViewList2);
        newCreditDetailView2.setRequestType(1);
        newCreditDetailView2.setRefinance(1);
        newCreditDetailView2.setRemark("Purpose Detail Example 2");
        newCreditDetailView2.setDisbursement(disbursement);
        newCreditDetailView2.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 3 ---------------------------------------//
        NewCreditDetailView newCreditDetailView3 = new NewCreditDetailView();
        newCreditDetailView3.setProductProgram(productProgram);
        newCreditDetailView3.setCreditType(creditType);
        newCreditDetailView3.setProductCode("EAC3");
        newCreditDetailView3.setProjectCode("90082");
        newCreditDetailView3.setLimit(BigDecimal.valueOf(123456.78));
        newCreditDetailView3.setFrontEndFee(BigDecimal.valueOf(2.25));
        newCreditDetailView3.setCreditTierDetailViewList(null);
        newCreditDetailView3.setRequestType(0);
        newCreditDetailView3.setRefinance(1);
        newCreditDetailView3.setRemark("Purpose Detail Example 3");
        newCreditDetailView3.setDisbursement(disbursement);
        newCreditDetailView3.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        //----------------------------------------- 4 ---------------------------------------//
        NewCreditDetailView newCreditDetailView4 = new NewCreditDetailView();
        newCreditDetailView4.setProductProgram(productProgram);
        newCreditDetailView4.setCreditType(creditType);
        newCreditDetailView4.setProductCode("EAC4");
        newCreditDetailView4.setProjectCode("90086");
        newCreditDetailView4.setLimit(BigDecimal.valueOf(123456.78));
        newCreditDetailView4.setFrontEndFee(BigDecimal.valueOf(2.75));
        newCreditDetailView4.setCreditTierDetailViewList(new ArrayList<CreditTierDetailView>());
        newCreditDetailView4.setRequestType(1);
        newCreditDetailView4.setRefinance(0);
        newCreditDetailView4.setRemark("Purpose Detail Example 4");
        newCreditDetailView4.setDisbursement(disbursement);
        newCreditDetailView4.setHoldLimitAmount(BigDecimal.valueOf(1234567.89));

        newCreditDetailViewList.add(newCreditDetailView1);
        newCreditDetailViewList.add(newCreditDetailView2);
        newCreditDetailViewList.add(newCreditDetailView3);
        newCreditDetailViewList.add(newCreditDetailView4);
        creditFacProposeView.setNewCreditDetailViewList(newCreditDetailViewList);

        // Fee Information
        List<NewFeeDetailView> newFeeDetailViewList = new ArrayList<NewFeeDetailView>();
        NewFeeDetailView newFeeDetailView1 = new NewFeeDetailView();
        newFeeDetailView1.setProductProgram("TMB SME SmartBiz");
        newFeeDetailView1.setStandardFrontEndFee("12.34%, -");
        newFeeDetailView1.setCommitmentFee("12.34%, -");
        newFeeDetailView1.setExtensionFee("12.34%, -");
        newFeeDetailView1.setPrepaymentFee("12.34%, 5 Years");
        newFeeDetailView1.setCancellationFee("12.34%, 2 Years");
        newFeeDetailViewList.add(newFeeDetailView1);
        creditFacProposeView.setNewFeeDetailViewList(newFeeDetailViewList);

        // Propose Collateral
        List<NewCollateralInfoView> newCollateralInfoViewList = new ArrayList<NewCollateralInfoView>();
        NewCollateralInfoView newCollateralInfoView1 = new NewCollateralInfoView();
        newCollateralInfoView1.setJobID("#001");
        newCollateralInfoView1.setAppraisalDate(new Date());
        newCollateralInfoView1.setAadDecision("Accept");
        newCollateralInfoView1.setAadDecisionReason("Reason Example");
        newCollateralInfoView1.setAadDecisionReasonDetail("Remark Example");
        newCollateralInfoView1.setUsage("Use");
        newCollateralInfoView1.setTypeOfUsage("Type of usage");
        newCollateralInfoView1.setMortgageCondition("Mortgage Condition XXX");
        newCollateralInfoView1.setMortgageConditionDetail("Mortgage Condition Detail.....");
        newCollateralInfoView1.setBdmComments("BDM Comments Detail.....");

        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView1 = new CreditTypeDetailView();
        creditTypeDetailView1.setAccount("Mr.A Example");
        creditTypeDetailView1.setType("New");
        creditTypeDetailView1.setProductProgram("TMB SME SmartBiz");
        creditTypeDetailView1.setCreditFacility("Loan");
        creditTypeDetailView1.setGuaranteeAmount(BigDecimal.valueOf(123456.78));
        creditTypeDetailViewList.add(creditTypeDetailView1);
        newCollateralInfoView1.setCreditTypeDetailViewList(creditTypeDetailViewList);

        NewCollateralHeadDetailView newCollateralHeadDetailView1 = new NewCollateralHeadDetailView();
        PotentialCollateral potentialCollateral1 = new PotentialCollateral();
        potentialCollateral1.setName("Core Asset");
        potentialCollateral1.setDescription("Core Asset");
        newCollateralHeadDetailView1.setPotentialCollateral(potentialCollateral1);

        CollateralType collateralType1 = new CollateralType();
        collateralType1.setDescription("Land and Building");
        newCollateralHeadDetailView1.setHeadCollType(collateralType1);

        newCollateralHeadDetailView1.setExistingCredit(BigDecimal.valueOf(1234567.89));
        newCollateralHeadDetailView1.setTitleDeed("12, 1234");
        newCollateralHeadDetailView1.setCollateralLocation("Jompol, Jatujak, Bangkok");
        newCollateralHeadDetailView1.setAppraisalValue(BigDecimal.valueOf(3000000.00));
        newCollateralHeadDetailView1.setInsuranceCompany(RadioValue.NOT_SELECTED.value());

        List<NewSubCollateralDetailView> newSubCollateralDetailViewList = new ArrayList<NewSubCollateralDetailView>();
        NewSubCollateralDetailView newSubCollateralDetailView1 = new NewSubCollateralDetailView();
        SubCollateralType subCollateralType1 = new SubCollateralType();
        subCollateralType1.setDescription("Land and House");
        newSubCollateralDetailView1.setSubCollateralType(subCollateralType1);
        newSubCollateralDetailView1.setAddress("234 Jompol, Jatujak, Bangkok");
        newSubCollateralDetailView1.setLandOffice("Ladpraow");
        newSubCollateralDetailView1.setTitleDeed("12, 1234");
        newSubCollateralDetailView1.setCollateralOwnerAAD("Mr.A Example");
        newSubCollateralDetailView1.setCollateralOwner("Mr.B Example");
        //todo: newSubCollateralDetailView.setMortgageType, newSubCollateralDetailView.setRelatedWith
//        newSubCollateralDetailView1.setMortgageType();
//        newSubCollateralDetailView1.setRelatedWith();
        newSubCollateralDetailView1.setAppraisalValue(BigDecimal.valueOf(2000000.00));
        newSubCollateralDetailView1.setMortgageValue(BigDecimal.valueOf(3200000.00));
        newSubCollateralDetailViewList.add(newSubCollateralDetailView1);

        newCollateralHeadDetailView1.setNewSubCollateralDetailViewList(newSubCollateralDetailViewList);

        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        newCollateralHeadDetailViewList.add(newCollateralHeadDetailView1);
        newCollateralInfoView1.setNewCollateralHeadDetailViewList(newCollateralHeadDetailViewList);
        newCollateralInfoViewList.add(newCollateralInfoView1);
        creditFacProposeView.setNewCollateralInfoViewList(newCollateralInfoViewList);

        // Proposed Guarantor
        List<NewGuarantorDetailView> newGuarantorDetailViewList = new ArrayList<NewGuarantorDetailView>();
        NewGuarantorDetailView newGuarantorDetailView1 = new NewGuarantorDetailView();
        Customer guarantor = new Customer();
        guarantor.setNameTh("Guarantor Name");
        guarantor.setLastNameTh("Lastname");
        newGuarantorDetailView1.setGuarantorName(guarantor);
        newGuarantorDetailView1.setGuaranteeAmount(BigDecimal.valueOf(11222333.44));
        newGuarantorDetailView1.setTcgLgNo("11-23456");
        newGuarantorDetailView1.setCreditTypeDetailViewList(creditTypeDetailViewList);
        newGuarantorDetailViewList.add(newGuarantorDetailView1);
        creditFacProposeView.setNewGuarantorDetailViewList(newGuarantorDetailViewList);

        // Propose Condition
        proposeCondition = new NewConditionDetailView();
        proposeConditionDate = DateTimeUtil.getCurrentDateTH();
        creditFacProposeView.setNewConditionDetailViewList(new ArrayList<NewConditionDetailView>());
    }

    private void initSelectList() {
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        investedCountryList = countryDAO.findAll();
    }

    public void onAddProposeApprovedCredit() {
        log.debug("onAddProposeApprovedCredit");
    }

    public void onAddProposeCondition() {
        log.debug("onAddProposeCondition");
    }

    public void onSave() {
        log.debug("onSaveDecision()");
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

    public List<NewConditionDetailView> getExistingConditionCommCrdList() {
        return existingConditionCommCrdList;
    }

    public void setExistingConditionCommCrdList(List<NewConditionDetailView> existingConditionCommCrdList) {
        this.existingConditionCommCrdList = existingConditionCommCrdList;
    }

    public CreditFacProposeView getCreditFacProposeView() {
        return creditFacProposeView;
    }

    public void setCreditFacProposeView(CreditFacProposeView creditFacProposeView) {
        this.creditFacProposeView = creditFacProposeView;
    }

    public ExistingCollateralView getExistingColBorrower() {
        return existingColBorrower;
    }

    public void setExistingColBorrower(ExistingCollateralView existingColBorrower) {
        this.existingColBorrower = existingColBorrower;
    }

    public ExistingCollateralView getExistingColRelated() {
        return existingColRelated;
    }

    public void setExistingColRelated(ExistingCollateralView existingColRelated) {
        this.existingColRelated = existingColRelated;
    }

    public List<NewGuarantorDetailView> getExistingGuarantorList() {
        return existingGuarantorList;
    }

    public void setExistingGuarantorList(List<NewGuarantorDetailView> existingGuarantorList) {
        this.existingGuarantorList = existingGuarantorList;
    }

    public BigDecimal getExistingTotalGuaranteeAmount() {
        return existingTotalGuaranteeAmount;
    }

    public void setExistingTotalGuaranteeAmount(BigDecimal existingTotalGuaranteeAmount) {
        this.existingTotalGuaranteeAmount = existingTotalGuaranteeAmount;
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

    public List<NewCreditDetailView> getApprovedProposeCreditList() {
        return approvedProposeCreditList;
    }

    public void setApprovedProposeCreditList(List<NewCreditDetailView> approvedProposeCreditList) {
        this.approvedProposeCreditList = approvedProposeCreditList;
    }

    public NewCreditDetailView getApprovedProposeCredit() {
        return approvedProposeCredit;
    }

    public void setApprovedProposeCredit(NewCreditDetailView approvedProposeCredit) {
        this.approvedProposeCredit = approvedProposeCredit;
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
}
