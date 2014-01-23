package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DecisionView implements Serializable {
    private long id;
    private User createBy;
    private Date createDate;
    private User modifyBy;
    private Date modifyDate;

    // Existing
    private List<NewConditionDetailView> extConditionComCreditList;
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
    private List<NewCreditDetailView> proposeCreditList;
    private List<NewFeeDetailView> proposeFeeInfoList;
    private List<NewCollateralView> proposeCollateralList;
    private List<NewGuarantorDetailView> proposeGuarantorList;

    private BigDecimal proposeTotalCreditLimit;
    private BigDecimal proposeTotalGuaranteeAmt;

    private BigDecimal existingSMELimit;
    private BigDecimal maximumSMELimit;

    // Approve
    private List<NewCreditDetailView> approveCreditList;
    private List<NewCollateralView> approveCollateralList;
    private List<NewGuarantorDetailView> approveGuarantorList;

    private BigDecimal approveTotalCreditLimit;
    private BigDecimal approveBrwTotalCommercial;
    private BigDecimal approveBrwTotalComAndOBOD;
    private BigDecimal approveTotalExposure;
    private BigDecimal approveTotalNumOfNewOD;
    private BigDecimal approveTotalNumProposeCreditFac;
    private BigDecimal approveTotalNumContingentPropose;
    private BigDecimal approveTotalGuaranteeAmt;
    private BigDecimal grandTotalNumOfCoreAsset;
    private BigDecimal grandTotalNumOfNonCoreAsset;
    private BigDecimal approveTotalTCGGuaranteeAmt;
    private BigDecimal approveTotalIndvGuaranteeAmt;
    private BigDecimal approveTotalJurisGuaranteeAmt;

    private List<FollowUpConditionView> followUpConditionList;

    private BigDecimal intFeeDOA;
    private BigDecimal frontendFeeDOA;
    private BigDecimal guarantorBA;
    private String reasonForReduction;

    private List<ApprovalHistory> approvalHistoryList;

    public DecisionView() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public List<NewConditionDetailView> getExtConditionComCreditList() {
        return extConditionComCreditList;
    }

    public void setExtConditionComCreditList(List<NewConditionDetailView> extConditionComCreditList) {
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

    public List<NewCreditDetailView> getProposeCreditList() {
        return proposeCreditList;
    }

    public void setProposeCreditList(List<NewCreditDetailView> proposeCreditList) {
        this.proposeCreditList = proposeCreditList;
    }

    public List<NewFeeDetailView> getProposeFeeInfoList() {
        return proposeFeeInfoList;
    }

    public void setProposeFeeInfoList(List<NewFeeDetailView> proposeFeeInfoList) {
        this.proposeFeeInfoList = proposeFeeInfoList;
    }

    public List<NewCollateralView> getProposeCollateralList() {
        return proposeCollateralList;
    }

    public void setProposeCollateralList(List<NewCollateralView> proposeCollateralList) {
        this.proposeCollateralList = proposeCollateralList;
    }

    public List<NewGuarantorDetailView> getProposeGuarantorList() {
        return proposeGuarantorList;
    }

    public void setProposeGuarantorList(List<NewGuarantorDetailView> proposeGuarantorList) {
        this.proposeGuarantorList = proposeGuarantorList;
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

    public List<NewCreditDetailView> getApproveCreditList() {
        return approveCreditList;
    }

    public void setApproveCreditList(List<NewCreditDetailView> approveCreditList) {
        this.approveCreditList = approveCreditList;
    }

    public List<NewCollateralView> getApproveCollateralList() {
        return approveCollateralList;
    }

    public void setApproveCollateralList(List<NewCollateralView> approveCollateralList) {
        this.approveCollateralList = approveCollateralList;
    }

    public List<NewGuarantorDetailView> getApproveGuarantorList() {
        return approveGuarantorList;
    }

    public void setApproveGuarantorList(List<NewGuarantorDetailView> approveGuarantorList) {
        this.approveGuarantorList = approveGuarantorList;
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

    public List<FollowUpConditionView> getFollowUpConditionList() {
        return followUpConditionList;
    }

    public void setFollowUpConditionList(List<FollowUpConditionView> followUpConditionList) {
        this.followUpConditionList = followUpConditionList;
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

    public List<ApprovalHistory> getApprovalHistoryList() {
        return approvalHistoryList;
    }

    public void setApprovalHistoryList(List<ApprovalHistory> approvalHistoryList) {
        this.approvalHistoryList = approvalHistoryList;
    }

    public BigDecimal getApproveTotalCreditLimit() {
        return approveTotalCreditLimit;
    }

    public void setApproveTotalCreditLimit(BigDecimal approveTotalCreditLimit) {
        this.approveTotalCreditLimit = approveTotalCreditLimit;
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
}
