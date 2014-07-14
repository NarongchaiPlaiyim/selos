package com.clevel.selos.model.view;

import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DecisionView implements Serializable {
    private long id;
    // Existing
    private List<ExistingConditionDetailView> extConditionComCreditList;
    private List<ExistingCreditDetailView> extBorrowerComCreditList;
    private List<ExistingCreditDetailView> extBorrowerRetailCreditList;
    private List<ExistingCreditDetailView> extBorrowerAppInRLOSList;
    private List<ExistingCreditDetailView> extRelatedComCreditList;
    private List<ExistingCreditDetailView> extRelatedRetailCreditList;
    private List<ExistingCreditDetailView> extRelatedAppInRLOSList;
    private List<ExistingCollateralDetailView> extBorrowerCollateralList;
    private List<ExistingCollateralDetailView> extRelatedCollateralList;
    private List<ExistingGuarantorDetailView> extGuarantorList;

    private BigDecimal extBorrowerTotalComLimit;
    private BigDecimal extBorrowerTotalRetailLimit;
    private BigDecimal extBorrowerTotalAppInRLOSLimit;
    private BigDecimal extBorrowerTotalCommercial;
    private BigDecimal extBorrowerTotalComAndOBOD;
    private BigDecimal extBorrowerTotalExposure;
    private BigDecimal extRelatedTotalComLimit;
    private BigDecimal extRelatedTotalRetailLimit;
    private BigDecimal extRelatedTotalAppInRLOSLimit;
    private BigDecimal extRelatedTotalCommercial;
    private BigDecimal extRelatedTotalComAndOBOD;
    private BigDecimal extRelatedTotalExposure;
    private BigDecimal extGroupTotalCommercial;
    private BigDecimal extGroupTotalComAndOBOD;
    private BigDecimal extGroupTotalExposure;
    private BigDecimal extBorrowerTotalAppraisalValue;
    private BigDecimal extBorrowerTotalMortgageValue;
    private BigDecimal extRelatedTotalAppraisalValue;
    private BigDecimal extRelatedTotalMortgageValue;
    private BigDecimal extTotalGuaranteeAmount;

    // Propose
    private List<ProposeCreditInfoDetailView> proposeCreditList;
    private List<ProposeCollateralInfoView> proposeCollateralList;
    private List<ProposeGuarantorInfoView> proposeGuarantorList;

    private BigDecimal proposeTotalCreditLimit;
    private BigDecimal proposeTotalGuaranteeAmt;

    private CreditCustomerType creditCustomerType;
    private CreditRequestTypeView loanRequestType;
    private CountryView investedCountry;
    private BigDecimal existingSMELimit;
    private BigDecimal maximumSMELimit;

    // Approve
    private List<ProposeCreditInfoDetailView> approveCreditList;
    private List<ProposeCollateralInfoView> approveCollateralList;
    private List<ProposeGuarantorInfoView> approveGuarantorList;

    private BigDecimal approveTotalCreditLimit;
    private BigDecimal approveBrwTotalCommercial;
    private BigDecimal approveBrwTotalComAndOBOD;
    private BigDecimal approveTotalExposure;
    private BigDecimal approveTotalODLimit;
    private BigDecimal approveTotalNumOfNewOD;
    private BigDecimal approveTotalNumProposeCreditFac;
    private BigDecimal approveTotalNumContingentPropose;
    private BigDecimal approveTotalGuaranteeAmt;
    private BigDecimal grandTotalNumOfCoreAsset;
    private BigDecimal grandTotalNumOfNonCoreAsset;
    private BigDecimal approveTotalTCGGuaranteeAmt;
    private BigDecimal approveTotalIndvGuaranteeAmt;
    private BigDecimal approveTotalJurisGuaranteeAmt;

    private List<DecisionFollowConditionView> decisionFollowConditionViewList;

    private BigDecimal intFeeDOA;
    private BigDecimal frontendFeeDOA;
    private BigDecimal guarantorBA;
    private String reasonForReduction;

    private List<ApprovalHistoryView> approvalHistoryList;

    private long newCreditFacilityViewId;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private List<ProposeFeeDetailView> approveFeeDetailViewList; // for only show in screen
    private List<ProposeFeeDetailView> approveFeeDetailViewOriginalList; // have all proposeFeeDetail

    private List<Long> deleteCreditIdList;
    private List<Long> deleteFollowConditionIdList;
    private List<Long> deleteGuarantorIdList;
    private List<Long> deleteCollateralIdList;

    public DecisionView() {
        this.approveTotalCreditLimit = BigDecimal.ZERO;
        this.approveBrwTotalCommercial = BigDecimal.ZERO;
        this.approveBrwTotalComAndOBOD = BigDecimal.ZERO;
        this.approveTotalExposure = BigDecimal.ZERO;
        this.approveTotalGuaranteeAmt = BigDecimal.ZERO;

        this.deleteCreditIdList = new ArrayList<Long>();
        this.deleteFollowConditionIdList = new ArrayList<Long>();
        this.deleteGuarantorIdList = new ArrayList<Long>();
        this.deleteCollateralIdList = new ArrayList<Long>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ExistingConditionDetailView> getExtConditionComCreditList() {
        return extConditionComCreditList;
    }

    public void setExtConditionComCreditList(List<ExistingConditionDetailView> extConditionComCreditList) {
        this.extConditionComCreditList = extConditionComCreditList;
    }

    public List<ExistingCreditDetailView> getExtBorrowerComCreditList() {
        return extBorrowerComCreditList;
    }

    public void setExtBorrowerComCreditList(List<ExistingCreditDetailView> extBorrowerComCreditList) {
        this.extBorrowerComCreditList = extBorrowerComCreditList;
    }

    public List<ExistingCreditDetailView> getExtBorrowerRetailCreditList() {
        return extBorrowerRetailCreditList;
    }

    public void setExtBorrowerRetailCreditList(List<ExistingCreditDetailView> extBorrowerRetailCreditList) {
        this.extBorrowerRetailCreditList = extBorrowerRetailCreditList;
    }

    public List<ExistingCreditDetailView> getExtBorrowerAppInRLOSList() {
        return extBorrowerAppInRLOSList;
    }

    public void setExtBorrowerAppInRLOSList(List<ExistingCreditDetailView> extBorrowerAppInRLOSList) {
        this.extBorrowerAppInRLOSList = extBorrowerAppInRLOSList;
    }

    public List<ExistingCreditDetailView> getExtRelatedComCreditList() {
        return extRelatedComCreditList;
    }

    public void setExtRelatedComCreditList(List<ExistingCreditDetailView> extRelatedComCreditList) {
        this.extRelatedComCreditList = extRelatedComCreditList;
    }

    public List<ExistingCreditDetailView> getExtRelatedRetailCreditList() {
        return extRelatedRetailCreditList;
    }

    public void setExtRelatedRetailCreditList(List<ExistingCreditDetailView> extRelatedRetailCreditList) {
        this.extRelatedRetailCreditList = extRelatedRetailCreditList;
    }

    public List<ExistingCreditDetailView> getExtRelatedAppInRLOSList() {
        return extRelatedAppInRLOSList;
    }

    public void setExtRelatedAppInRLOSList(List<ExistingCreditDetailView> extRelatedAppInRLOSList) {
        this.extRelatedAppInRLOSList = extRelatedAppInRLOSList;
    }

    public List<ExistingCollateralDetailView> getExtBorrowerCollateralList() {
        return extBorrowerCollateralList;
    }

    public void setExtBorrowerCollateralList(List<ExistingCollateralDetailView> extBorrowerCollateralList) {
        this.extBorrowerCollateralList = extBorrowerCollateralList;
    }

    public List<ExistingCollateralDetailView> getExtRelatedCollateralList() {
        return extRelatedCollateralList;
    }

    public void setExtRelatedCollateralList(List<ExistingCollateralDetailView> extRelatedCollateralList) {
        this.extRelatedCollateralList = extRelatedCollateralList;
    }

    public List<ExistingGuarantorDetailView> getExtGuarantorList() {
        return extGuarantorList;
    }

    public void setExtGuarantorList(List<ExistingGuarantorDetailView> extGuarantorList) {
        this.extGuarantorList = extGuarantorList;
    }

    public BigDecimal getExtBorrowerTotalComLimit() {
        return extBorrowerTotalComLimit;
    }

    public void setExtBorrowerTotalComLimit(BigDecimal extBorrowerTotalComLimit) {
        this.extBorrowerTotalComLimit = extBorrowerTotalComLimit;
    }

    public BigDecimal getExtBorrowerTotalRetailLimit() {
        return extBorrowerTotalRetailLimit;
    }

    public void setExtBorrowerTotalRetailLimit(BigDecimal extBorrowerTotalRetailLimit) {
        this.extBorrowerTotalRetailLimit = extBorrowerTotalRetailLimit;
    }

    public BigDecimal getExtBorrowerTotalAppInRLOSLimit() {
        return extBorrowerTotalAppInRLOSLimit;
    }

    public void setExtBorrowerTotalAppInRLOSLimit(BigDecimal extBorrowerTotalAppInRLOSLimit) {
        this.extBorrowerTotalAppInRLOSLimit = extBorrowerTotalAppInRLOSLimit;
    }

    public BigDecimal getExtBorrowerTotalCommercial() {
        return extBorrowerTotalCommercial;
    }

    public void setExtBorrowerTotalCommercial(BigDecimal extBorrowerTotalCommercial) {
        this.extBorrowerTotalCommercial = extBorrowerTotalCommercial;
    }

    public BigDecimal getExtBorrowerTotalComAndOBOD() {
        return extBorrowerTotalComAndOBOD;
    }

    public void setExtBorrowerTotalComAndOBOD(BigDecimal extBorrowerTotalComAndOBOD) {
        this.extBorrowerTotalComAndOBOD = extBorrowerTotalComAndOBOD;
    }

    public BigDecimal getExtBorrowerTotalExposure() {
        return extBorrowerTotalExposure;
    }

    public void setExtBorrowerTotalExposure(BigDecimal extBorrowerTotalExposure) {
        this.extBorrowerTotalExposure = extBorrowerTotalExposure;
    }

    public BigDecimal getExtRelatedTotalComLimit() {
        return extRelatedTotalComLimit;
    }

    public void setExtRelatedTotalComLimit(BigDecimal extRelatedTotalComLimit) {
        this.extRelatedTotalComLimit = extRelatedTotalComLimit;
    }

    public BigDecimal getExtRelatedTotalRetailLimit() {
        return extRelatedTotalRetailLimit;
    }

    public void setExtRelatedTotalRetailLimit(BigDecimal extRelatedTotalRetailLimit) {
        this.extRelatedTotalRetailLimit = extRelatedTotalRetailLimit;
    }

    public BigDecimal getExtRelatedTotalAppInRLOSLimit() {
        return extRelatedTotalAppInRLOSLimit;
    }

    public void setExtRelatedTotalAppInRLOSLimit(BigDecimal extRelatedTotalAppInRLOSLimit) {
        this.extRelatedTotalAppInRLOSLimit = extRelatedTotalAppInRLOSLimit;
    }

    public BigDecimal getExtRelatedTotalCommercial() {
        return extRelatedTotalCommercial;
    }

    public void setExtRelatedTotalCommercial(BigDecimal extRelatedTotalCommercial) {
        this.extRelatedTotalCommercial = extRelatedTotalCommercial;
    }

    public BigDecimal getExtRelatedTotalComAndOBOD() {
        return extRelatedTotalComAndOBOD;
    }

    public void setExtRelatedTotalComAndOBOD(BigDecimal extRelatedTotalComAndOBOD) {
        this.extRelatedTotalComAndOBOD = extRelatedTotalComAndOBOD;
    }

    public BigDecimal getExtRelatedTotalExposure() {
        return extRelatedTotalExposure;
    }

    public void setExtRelatedTotalExposure(BigDecimal extRelatedTotalExposure) {
        this.extRelatedTotalExposure = extRelatedTotalExposure;
    }

    public BigDecimal getExtGroupTotalCommercial() {
        return extGroupTotalCommercial;
    }

    public void setExtGroupTotalCommercial(BigDecimal extGroupTotalCommercial) {
        this.extGroupTotalCommercial = extGroupTotalCommercial;
    }

    public BigDecimal getExtGroupTotalComAndOBOD() {
        return extGroupTotalComAndOBOD;
    }

    public void setExtGroupTotalComAndOBOD(BigDecimal extGroupTotalComAndOBOD) {
        this.extGroupTotalComAndOBOD = extGroupTotalComAndOBOD;
    }

    public BigDecimal getExtGroupTotalExposure() {
        return extGroupTotalExposure;
    }

    public void setExtGroupTotalExposure(BigDecimal extGroupTotalExposure) {
        this.extGroupTotalExposure = extGroupTotalExposure;
    }

    public BigDecimal getExtBorrowerTotalAppraisalValue() {
        return extBorrowerTotalAppraisalValue;
    }

    public void setExtBorrowerTotalAppraisalValue(BigDecimal extBorrowerTotalAppraisalValue) {
        this.extBorrowerTotalAppraisalValue = extBorrowerTotalAppraisalValue;
    }

    public BigDecimal getExtBorrowerTotalMortgageValue() {
        return extBorrowerTotalMortgageValue;
    }

    public void setExtBorrowerTotalMortgageValue(BigDecimal extBorrowerTotalMortgageValue) {
        this.extBorrowerTotalMortgageValue = extBorrowerTotalMortgageValue;
    }

    public BigDecimal getExtRelatedTotalAppraisalValue() {
        return extRelatedTotalAppraisalValue;
    }

    public void setExtRelatedTotalAppraisalValue(BigDecimal extRelatedTotalAppraisalValue) {
        this.extRelatedTotalAppraisalValue = extRelatedTotalAppraisalValue;
    }

    public BigDecimal getExtRelatedTotalMortgageValue() {
        return extRelatedTotalMortgageValue;
    }

    public void setExtRelatedTotalMortgageValue(BigDecimal extRelatedTotalMortgageValue) {
        this.extRelatedTotalMortgageValue = extRelatedTotalMortgageValue;
    }

    public BigDecimal getExtTotalGuaranteeAmount() {
        return extTotalGuaranteeAmount;
    }

    public void setExtTotalGuaranteeAmount(BigDecimal extTotalGuaranteeAmount) {
        this.extTotalGuaranteeAmount = extTotalGuaranteeAmount;
    }

    public BigDecimal getProposeTotalCreditLimit() {
        return proposeTotalCreditLimit;
    }

    public void setProposeTotalCreditLimit(BigDecimal proposeTotalCreditLimit) {
        this.proposeTotalCreditLimit = proposeTotalCreditLimit;
    }

    public BigDecimal getProposeTotalGuaranteeAmt() {
        return proposeTotalGuaranteeAmt;
    }

    public void setProposeTotalGuaranteeAmt(BigDecimal proposeTotalGuaranteeAmt) {
        this.proposeTotalGuaranteeAmt = proposeTotalGuaranteeAmt;
    }

    public CreditRequestTypeView getLoanRequestType() {
        return loanRequestType;
    }

    public void setLoanRequestType(CreditRequestTypeView loanRequestType) {
        this.loanRequestType = loanRequestType;
    }

    public CountryView getInvestedCountry() {
        return investedCountry;
    }

    public void setInvestedCountry(CountryView investedCountry) {
        this.investedCountry = investedCountry;
    }

    public BigDecimal getExistingSMELimit() {
        return existingSMELimit;
    }

    public void setExistingSMELimit(BigDecimal existingSMELimit) {
        this.existingSMELimit = existingSMELimit;
    }

    public BigDecimal getMaximumSMELimit() {
        return maximumSMELimit;
    }

    public void setMaximumSMELimit(BigDecimal maximumSMELimit) {
        this.maximumSMELimit = maximumSMELimit;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditList() {
        return proposeCreditList;
    }

    public void setProposeCreditList(List<ProposeCreditInfoDetailView> proposeCreditList) {
        this.proposeCreditList = proposeCreditList;
    }

    public List<ProposeCollateralInfoView> getProposeCollateralList() {
        return proposeCollateralList;
    }

    public void setProposeCollateralList(List<ProposeCollateralInfoView> proposeCollateralList) {
        this.proposeCollateralList = proposeCollateralList;
    }

    public List<ProposeGuarantorInfoView> getProposeGuarantorList() {
        return proposeGuarantorList;
    }

    public void setProposeGuarantorList(List<ProposeGuarantorInfoView> proposeGuarantorList) {
        this.proposeGuarantorList = proposeGuarantorList;
    }

    public List<ProposeCreditInfoDetailView> getApproveCreditList() {
        return approveCreditList;
    }

    public void setApproveCreditList(List<ProposeCreditInfoDetailView> approveCreditList) {
        this.approveCreditList = approveCreditList;
    }

    public List<ProposeCollateralInfoView> getApproveCollateralList() {
        return approveCollateralList;
    }

    public void setApproveCollateralList(List<ProposeCollateralInfoView> approveCollateralList) {
        this.approveCollateralList = approveCollateralList;
    }

    public List<ProposeGuarantorInfoView> getApproveGuarantorList() {
        return approveGuarantorList;
    }

    public void setApproveGuarantorList(List<ProposeGuarantorInfoView> approveGuarantorList) {
        this.approveGuarantorList = approveGuarantorList;
    }

    public BigDecimal getApproveTotalCreditLimit() {
        return approveTotalCreditLimit;
    }

    public void setApproveTotalCreditLimit(BigDecimal approveTotalCreditLimit) {
        this.approveTotalCreditLimit = approveTotalCreditLimit;
    }

    public BigDecimal getApproveBrwTotalCommercial() {
        return approveBrwTotalCommercial;
    }

    public void setApproveBrwTotalCommercial(BigDecimal approveBrwTotalCommercial) {
        this.approveBrwTotalCommercial = approveBrwTotalCommercial;
    }

    public BigDecimal getApproveBrwTotalComAndOBOD() {
        return approveBrwTotalComAndOBOD;
    }

    public void setApproveBrwTotalComAndOBOD(BigDecimal approveBrwTotalComAndOBOD) {
        this.approveBrwTotalComAndOBOD = approveBrwTotalComAndOBOD;
    }

    public BigDecimal getApproveTotalExposure() {
        return approveTotalExposure;
    }

    public void setApproveTotalExposure(BigDecimal approveTotalExposure) {
        this.approveTotalExposure = approveTotalExposure;
    }

    public BigDecimal getApproveTotalNumOfNewOD() {
        return approveTotalNumOfNewOD;
    }

    public void setApproveTotalNumOfNewOD(BigDecimal approveTotalNumOfNewOD) {
        this.approveTotalNumOfNewOD = approveTotalNumOfNewOD;
    }

    public BigDecimal getApproveTotalNumProposeCreditFac() {
        return approveTotalNumProposeCreditFac;
    }

    public void setApproveTotalNumProposeCreditFac(BigDecimal approveTotalNumProposeCreditFac) {
        this.approveTotalNumProposeCreditFac = approveTotalNumProposeCreditFac;
    }

    public BigDecimal getApproveTotalNumContingentPropose() {
        return approveTotalNumContingentPropose;
    }

    public void setApproveTotalNumContingentPropose(BigDecimal approveTotalNumContingentPropose) {
        this.approveTotalNumContingentPropose = approveTotalNumContingentPropose;
    }

    public BigDecimal getApproveTotalGuaranteeAmt() {
        return approveTotalGuaranteeAmt;
    }

    public void setApproveTotalGuaranteeAmt(BigDecimal approveTotalGuaranteeAmt) {
        this.approveTotalGuaranteeAmt = approveTotalGuaranteeAmt;
    }

    public BigDecimal getGrandTotalNumOfCoreAsset() {
        return grandTotalNumOfCoreAsset;
    }

    public void setGrandTotalNumOfCoreAsset(BigDecimal grandTotalNumOfCoreAsset) {
        this.grandTotalNumOfCoreAsset = grandTotalNumOfCoreAsset;
    }

    public BigDecimal getGrandTotalNumOfNonCoreAsset() {
        return grandTotalNumOfNonCoreAsset;
    }

    public void setGrandTotalNumOfNonCoreAsset(BigDecimal grandTotalNumOfNonCoreAsset) {
        this.grandTotalNumOfNonCoreAsset = grandTotalNumOfNonCoreAsset;
    }

    public BigDecimal getApproveTotalTCGGuaranteeAmt() {
        return approveTotalTCGGuaranteeAmt;
    }

    public void setApproveTotalTCGGuaranteeAmt(BigDecimal approveTotalTCGGuaranteeAmt) {
        this.approveTotalTCGGuaranteeAmt = approveTotalTCGGuaranteeAmt;
    }

    public BigDecimal getApproveTotalIndvGuaranteeAmt() {
        return approveTotalIndvGuaranteeAmt;
    }

    public void setApproveTotalIndvGuaranteeAmt(BigDecimal approveTotalIndvGuaranteeAmt) {
        this.approveTotalIndvGuaranteeAmt = approveTotalIndvGuaranteeAmt;
    }

    public BigDecimal getApproveTotalJurisGuaranteeAmt() {
        return approveTotalJurisGuaranteeAmt;
    }

    public void setApproveTotalJurisGuaranteeAmt(BigDecimal approveTotalJurisGuaranteeAmt) {
        this.approveTotalJurisGuaranteeAmt = approveTotalJurisGuaranteeAmt;
    }

    public List<DecisionFollowConditionView> getDecisionFollowConditionViewList() {
        return decisionFollowConditionViewList;
    }

    public void setDecisionFollowConditionViewList(List<DecisionFollowConditionView> decisionFollowConditionViewList) {
        this.decisionFollowConditionViewList = decisionFollowConditionViewList;
    }

    public BigDecimal getIntFeeDOA() {
        return intFeeDOA;
    }

    public void setIntFeeDOA(BigDecimal intFeeDOA) {
        this.intFeeDOA = intFeeDOA;
    }

    public BigDecimal getFrontendFeeDOA() {
        return frontendFeeDOA;
    }

    public void setFrontendFeeDOA(BigDecimal frontendFeeDOA) {
        this.frontendFeeDOA = frontendFeeDOA;
    }

    public BigDecimal getGuarantorBA() {
        return guarantorBA;
    }

    public void setGuarantorBA(BigDecimal guarantorBA) {
        this.guarantorBA = guarantorBA;
    }

    public String getReasonForReduction() {
        return reasonForReduction;
    }

    public void setReasonForReduction(String reasonForReduction) {
        this.reasonForReduction = reasonForReduction;
    }

    public List<ApprovalHistoryView> getApprovalHistoryList() {
        return approvalHistoryList;
    }

    public void setApprovalHistoryList(List<ApprovalHistoryView> approvalHistoryList) {
        this.approvalHistoryList = approvalHistoryList;
    }

    public CreditCustomerType getCreditCustomerType() {
        return creditCustomerType;
    }

    public void setCreditCustomerType(CreditCustomerType creditCustomerType) {
        this.creditCustomerType = creditCustomerType;
    }

    public BigDecimal getApproveTotalODLimit() {
        return approveTotalODLimit;
    }

    public void setApproveTotalODLimit(BigDecimal approveTotalODLimit) {
        this.approveTotalODLimit = approveTotalODLimit;
    }

    public long getNewCreditFacilityViewId() {
        return newCreditFacilityViewId;
    }

    public void setNewCreditFacilityViewId(long newCreditFacilityViewId) {
        this.newCreditFacilityViewId = newCreditFacilityViewId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public List<ProposeFeeDetailView> getApproveFeeDetailViewList() {
        return approveFeeDetailViewList;
    }

    public void setApproveFeeDetailViewList(List<ProposeFeeDetailView> approveFeeDetailViewList) {
        this.approveFeeDetailViewList = approveFeeDetailViewList;
    }

    public List<ProposeFeeDetailView> getApproveFeeDetailViewOriginalList() {
        return approveFeeDetailViewOriginalList;
    }

    public void setApproveFeeDetailViewOriginalList(List<ProposeFeeDetailView> approveFeeDetailViewOriginalList) {
        this.approveFeeDetailViewOriginalList = approveFeeDetailViewOriginalList;
    }

    public List<Long> getDeleteCreditIdList() {
        return deleteCreditIdList;
    }

    public void setDeleteCreditIdList(List<Long> deleteCreditIdList) {
        this.deleteCreditIdList = deleteCreditIdList;
    }

    public List<Long> getDeleteFollowConditionIdList() {
        return deleteFollowConditionIdList;
    }

    public void setDeleteFollowConditionIdList(List<Long> deleteFollowConditionIdList) {
        this.deleteFollowConditionIdList = deleteFollowConditionIdList;
    }

    public List<Long> getDeleteGuarantorIdList() {
        return deleteGuarantorIdList;
    }

    public void setDeleteGuarantorIdList(List<Long> deleteGuarantorIdList) {
        this.deleteGuarantorIdList = deleteGuarantorIdList;
    }

    public List<Long> getDeleteCollateralIdList() {
        return deleteCollateralIdList;
    }

    public void setDeleteCollateralIdList(List<Long> deleteCollateralIdList) {
        this.deleteCollateralIdList = deleteCollateralIdList;
    }
}
