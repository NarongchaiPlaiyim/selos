package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.*;
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
    private List<ProposeConditionDetailView> existingConditionCommCrdList;
    private ExistingCollateralView existingColBorrower;
    private ExistingCollateralView existingColRelated;
    private List<ProposeGuarantorDetailView> existingGuarantorList;
    private BigDecimal existingTotalGuaranteeAmount;

    //----- Propose Credit -----//
    private CreditFacProposeView creditFacProposeView;
    // Approved Propose (Credit, Collateral, Guarantor)
    private ProposeCreditDetailView approvedProposeCredit;
    private List<ProposeCreditDetailView> approvedProposeCreditList;
    private List<ProposeCollateralInfoView> approvedProposeCollateralList;
    private List<ProposeGuarantorDetailView> approvedProposeGuarantorList;
    // Propose Condition
    private ProposeConditionDetailView proposeCondition;
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
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();

        ProductProgram productProgram = new ProductProgram();
        productProgram.setDescription("TMB SME SmartBiz");
        CreditType creditType = new CreditType();
        creditType.setDescription("Loan");
        Disbursement disbursement = new Disbursement();
        disbursement.setDisbursement("Normal Disbursement");

        //----------------------------------------- 1 ---------------------------------------//
        List<CreditTierDetailView> tierDetailViewList1 = new ArrayList<CreditTierDetailView>();
        CreditTierDetailView tierDetailView1 = new CreditTierDetailView();
        tierDetailView1.setStandardPrice(BigDecimal.valueOf(1.00));
        tierDetailView1.setSuggestPrice(BigDecimal.valueOf(2.00));
        tierDetailView1.setFinalPriceRate(BigDecimal.valueOf(2.00));
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
        tierDetailView2_1.setStandardPrice(BigDecimal.valueOf(1.00));
        tierDetailView2_1.setSuggestPrice(BigDecimal.valueOf(1.50));
        tierDetailView2_1.setFinalPriceRate(BigDecimal.valueOf(1.50));
        tierDetailView2_1.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_1.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_1);

        CreditTierDetailView tierDetailView2_2 = new CreditTierDetailView();
        tierDetailView2_2.setStandardPrice(BigDecimal.valueOf(1.25));
        tierDetailView2_2.setSuggestPrice(BigDecimal.valueOf(2.00));
        tierDetailView2_2.setFinalPriceRate(BigDecimal.valueOf(2.00));
        tierDetailView2_2.setInstallment(BigDecimal.valueOf(123456.78));
        tierDetailView2_2.setTenor(3);
        tierDetailViewList2.add(tierDetailView2_2);

        CreditTierDetailView tierDetailView2_3 = new CreditTierDetailView();
        tierDetailView2_3.setStandardPrice(BigDecimal.valueOf(2.00));
        tierDetailView2_3.setSuggestPrice(BigDecimal.valueOf(2.50));
        tierDetailView2_3.setFinalPriceRate(BigDecimal.valueOf(2.50));
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

        proposeCollateralInfoView1.setCollateralHeaderDetailView(collateralHeaderDetailView1);
        proposeCollateralInfoViewList.add(proposeCollateralInfoView1);
        creditFacProposeView.setProposeCollateralInfoViewList(proposeCollateralInfoViewList);

        // Proposed Guarantor
        List<ProposeGuarantorDetailView> proposeGuarantorDetailViewList = new ArrayList<ProposeGuarantorDetailView>();
        ProposeGuarantorDetailView proposeGuarantorDetailView1 = new ProposeGuarantorDetailView();
        proposeGuarantorDetailView1.setGuarantorName("Guarantor name 1");
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

    public List<ProposeConditionDetailView> getExistingConditionCommCrdList() {
        return existingConditionCommCrdList;
    }

    public void setExistingConditionCommCrdList(List<ProposeConditionDetailView> existingConditionCommCrdList) {
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

    public List<ProposeGuarantorDetailView> getExistingGuarantorList() {
        return existingGuarantorList;
    }

    public void setExistingGuarantorList(List<ProposeGuarantorDetailView> existingGuarantorList) {
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

    public List<ProposeCreditDetailView> getApprovedProposeCreditList() {
        return approvedProposeCreditList;
    }

    public void setApprovedProposeCreditList(List<ProposeCreditDetailView> approvedProposeCreditList) {
        this.approvedProposeCreditList = approvedProposeCreditList;
    }

    public ProposeCreditDetailView getApprovedProposeCredit() {
        return approvedProposeCredit;
    }

    public void setApprovedProposeCredit(ProposeCreditDetailView approvedProposeCredit) {
        this.approvedProposeCredit = approvedProposeCredit;
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
}
