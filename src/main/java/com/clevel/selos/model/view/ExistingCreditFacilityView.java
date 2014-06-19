package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExistingCreditFacilityView implements Serializable {

    private long id;
    private long workCasePrescreenId;
    private long workCaseId;
    private List<ActionStatusView> status;

    private BigDecimal totalBorrowerComLimit;
    private BigDecimal totalBorrowerRetailLimit;
    private BigDecimal totalBorrowerAppInRLOSLimit;
    private BigDecimal totalBorrowerCom;
    private BigDecimal totalBorrowerComOBOD;
    private BigDecimal totalBorrowerExposure;
    private BigDecimal totalBorrowerNumberOfExistingOD;
    private BigDecimal totalBorrowerExistingODLimit;
    private BigDecimal totalBorrowerLimitPreScreen;

    private BigDecimal totalRelatedComLimit;
    private BigDecimal totalRelatedRetailLimit;
    private BigDecimal totalRelatedAppInRLOSLimit;
    private BigDecimal totalRelatedCom;
    private BigDecimal totalRelatedComOBOD;
    private BigDecimal totalRelatedExposure;
    private BigDecimal totalRelatedLimitPreScreen;

    private BigDecimal totalGroupCom;
    private BigDecimal totalGroupComOBOD;
    private BigDecimal totalGroupExposure;
    private BigDecimal totalGuaranteeAmount;
    /* Total for Decision */
    private BigDecimal totalBorrowerAppraisalValue;
    private BigDecimal totalBorrowerMortgageValue;
    private BigDecimal totalRelatedAppraisalValue;
    private BigDecimal totalRelatedMortgageValue;

    private List<ExistingCreditDetailView> borrowerComExistingCredit;
    private List<ExistingCreditDetailView> borrowerRetailExistingCredit;
    private List<ExistingCreditDetailView> borrowerAppInRLOSCredit;
    private List<ExistingCreditDetailView> borrowerExistingCreditPreScreen;

    private List<ExistingConditionDetailView> existingConditionDetailViewList;

    private List<ExistingCreditDetailView> relatedComExistingCredit;
    private List<ExistingCreditDetailView> relatedRetailExistingCredit;
    private List<ExistingCreditDetailView> relatedAppInRLOSCredit;
    private List<ExistingCreditDetailView> relateExistingCreditPresScreen;

    /* Collateral list for Decision */
    private List<ExistingCollateralDetailView> borrowerCollateralList;
    private List<ExistingCollateralDetailView> relatedCollateralList;

    private List<ExistingGuarantorDetailView> borrowerGuarantorList;

    private List<ExistingCreditDetailView> borrowerComExistingCreditDeleteList;
    private List<ExistingCreditDetailView> borrowerRetailExistingCreditDeleteList;
    private List<ExistingCreditDetailView> borrowerAppInRLOSCreditDeleteList;
    private List<ExistingCreditDetailView> borrowerExistingCreditPreScreenDeleteList;

    private List<ExistingCreditDetailView> relatedComExistingCreditDeleteList;
    private List<ExistingCreditDetailView> relatedRetailExistingCreditDeleteList;
    private List<ExistingCreditDetailView> relatedAppInRLOSCreditDeleteList;
    private List<ExistingCreditDetailView> relateExistingCreditPresScreenDeleteList;


    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkCasePrescreenId() {
        return workCasePrescreenId;
    }

    public void setWorkCasePrescreenId(long workCasePrescreenId) {
        this.workCasePrescreenId = workCasePrescreenId;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public List<ActionStatusView> getStatus() {
        return status;
    }

    public void setStatus(List<ActionStatusView> status) {
        this.status = status;
    }

    public BigDecimal getTotalBorrowerComLimit() {
        return totalBorrowerComLimit;
    }

    public void setTotalBorrowerComLimit(BigDecimal totalBorrowerComLimit) {
        this.totalBorrowerComLimit = totalBorrowerComLimit;
    }

    public BigDecimal getTotalBorrowerRetailLimit() {
        return totalBorrowerRetailLimit;
    }

    public void setTotalBorrowerRetailLimit(BigDecimal totalBorrowerRetailLimit) {
        this.totalBorrowerRetailLimit = totalBorrowerRetailLimit;
    }

    public BigDecimal getTotalBorrowerAppInRLOSLimit() {
        return totalBorrowerAppInRLOSLimit;
    }

    public void setTotalBorrowerAppInRLOSLimit(BigDecimal totalBorrowerAppInRLOSLimit) {
        this.totalBorrowerAppInRLOSLimit = totalBorrowerAppInRLOSLimit;
    }

    public BigDecimal getTotalBorrowerCom() {
        return totalBorrowerCom;
    }

    public void setTotalBorrowerCom(BigDecimal totalBorrowerCom) {
        this.totalBorrowerCom = totalBorrowerCom;
    }

    public BigDecimal getTotalBorrowerComOBOD() {
        return totalBorrowerComOBOD;
    }

    public void setTotalBorrowerComOBOD(BigDecimal totalBorrowerComOBOD) {
        this.totalBorrowerComOBOD = totalBorrowerComOBOD;
    }

    public BigDecimal getTotalBorrowerExposure() {
        return totalBorrowerExposure;
    }

    public void setTotalBorrowerExposure(BigDecimal totalBorrowerExposure) {
        this.totalBorrowerExposure = totalBorrowerExposure;
    }

    public BigDecimal getTotalBorrowerNumberOfExistingOD() {
        return totalBorrowerNumberOfExistingOD;
    }

    public void setTotalBorrowerNumberOfExistingOD(BigDecimal totalBorrowerNumberOfExistingOD) {
        this.totalBorrowerNumberOfExistingOD = totalBorrowerNumberOfExistingOD;
    }

    public BigDecimal getTotalBorrowerExistingODLimit() {
        return totalBorrowerExistingODLimit;
    }

    public void setTotalBorrowerExistingODLimit(BigDecimal totalBorrowerExistingODLimit) {
        this.totalBorrowerExistingODLimit = totalBorrowerExistingODLimit;
    }

    public BigDecimal getTotalRelatedComLimit() {
        return totalRelatedComLimit;
    }

    public void setTotalRelatedComLimit(BigDecimal totalRelatedComLimit) {
        this.totalRelatedComLimit = totalRelatedComLimit;
    }

    public BigDecimal getTotalRelatedRetailLimit() {
        return totalRelatedRetailLimit;
    }

    public void setTotalRelatedRetailLimit(BigDecimal totalRelatedRetailLimit) {
        this.totalRelatedRetailLimit = totalRelatedRetailLimit;
    }

    public BigDecimal getTotalRelatedAppInRLOSLimit() {
        return totalRelatedAppInRLOSLimit;
    }

    public void setTotalRelatedAppInRLOSLimit(BigDecimal totalRelatedAppInRLOSLimit) {
        this.totalRelatedAppInRLOSLimit = totalRelatedAppInRLOSLimit;
    }

    public BigDecimal getTotalRelatedCom() {
        return totalRelatedCom;
    }

    public void setTotalRelatedCom(BigDecimal totalRelatedCom) {
        this.totalRelatedCom = totalRelatedCom;
    }

    public BigDecimal getTotalRelatedComOBOD() {
        return totalRelatedComOBOD;
    }

    public void setTotalRelatedComOBOD(BigDecimal totalRelatedComOBOD) {
        this.totalRelatedComOBOD = totalRelatedComOBOD;
    }

    public BigDecimal getTotalRelatedExposure() {
        return totalRelatedExposure;
    }

    public void setTotalRelatedExposure(BigDecimal totalRelatedExposure) {
        this.totalRelatedExposure = totalRelatedExposure;
    }

    public BigDecimal getTotalGroupCom() {
        return totalGroupCom;
    }

    public void setTotalGroupCom(BigDecimal totalGroupCom) {
        this.totalGroupCom = totalGroupCom;
    }

    public BigDecimal getTotalGroupComOBOD() {
        return totalGroupComOBOD;
    }

    public void setTotalGroupComOBOD(BigDecimal totalGroupComOBOD) {
        this.totalGroupComOBOD = totalGroupComOBOD;
    }

    public BigDecimal getTotalGroupExposure() {
        return totalGroupExposure;
    }

    public void setTotalGroupExposure(BigDecimal totalGroupExposure) {
        this.totalGroupExposure = totalGroupExposure;
    }

    public BigDecimal getTotalGuaranteeAmount() {
        return totalGuaranteeAmount;
    }

    public void setTotalGuaranteeAmount(BigDecimal totalGuaranteeAmount) {
        this.totalGuaranteeAmount = totalGuaranteeAmount;
    }

    public BigDecimal getTotalBorrowerAppraisalValue() {
        return totalBorrowerAppraisalValue;
    }

    public void setTotalBorrowerAppraisalValue(BigDecimal totalBorrowerAppraisalValue) {
        this.totalBorrowerAppraisalValue = totalBorrowerAppraisalValue;
    }

    public BigDecimal getTotalBorrowerMortgageValue() {
        return totalBorrowerMortgageValue;
    }

    public void setTotalBorrowerMortgageValue(BigDecimal totalBorrowerMortgageValue) {
        this.totalBorrowerMortgageValue = totalBorrowerMortgageValue;
    }

    public BigDecimal getTotalRelatedAppraisalValue() {
        return totalRelatedAppraisalValue;
    }

    public void setTotalRelatedAppraisalValue(BigDecimal totalRelatedAppraisalValue) {
        this.totalRelatedAppraisalValue = totalRelatedAppraisalValue;
    }

    public BigDecimal getTotalRelatedMortgageValue() {
        return totalRelatedMortgageValue;
    }

    public void setTotalRelatedMortgageValue(BigDecimal totalRelatedMortgageValue) {
        this.totalRelatedMortgageValue = totalRelatedMortgageValue;
    }

    public List<ExistingCreditDetailView> getBorrowerComExistingCredit() {
        return borrowerComExistingCredit;
    }

    public void setBorrowerComExistingCredit(List<ExistingCreditDetailView> borrowerComExistingCredit) {
        this.borrowerComExistingCredit = borrowerComExistingCredit;
    }

    public List<ExistingCreditDetailView> getBorrowerRetailExistingCredit() {
        return borrowerRetailExistingCredit;
    }

    public void setBorrowerRetailExistingCredit(List<ExistingCreditDetailView> borrowerRetailExistingCredit) {
        this.borrowerRetailExistingCredit = borrowerRetailExistingCredit;
    }

    public List<ExistingCreditDetailView> getBorrowerAppInRLOSCredit() {
        return borrowerAppInRLOSCredit;
    }

    public void setBorrowerAppInRLOSCredit(List<ExistingCreditDetailView> borrowerAppInRLOSCredit) {
        this.borrowerAppInRLOSCredit = borrowerAppInRLOSCredit;
    }

    public List<ExistingConditionDetailView> getExistingConditionDetailViewList() {
        return existingConditionDetailViewList;
    }

    public void setExistingConditionDetailViewList(List<ExistingConditionDetailView> existingConditionDetailViewList) {
        this.existingConditionDetailViewList = existingConditionDetailViewList;
    }

    public List<ExistingCreditDetailView> getRelatedComExistingCredit() {
        return relatedComExistingCredit;
    }

    public void setRelatedComExistingCredit(List<ExistingCreditDetailView> relatedComExistingCredit) {
        this.relatedComExistingCredit = relatedComExistingCredit;
    }

    public List<ExistingCreditDetailView> getRelatedRetailExistingCredit() {
        return relatedRetailExistingCredit;
    }

    public void setRelatedRetailExistingCredit(List<ExistingCreditDetailView> relatedRetailExistingCredit) {
        this.relatedRetailExistingCredit = relatedRetailExistingCredit;
    }

    public List<ExistingCreditDetailView> getRelatedAppInRLOSCredit() {
        return relatedAppInRLOSCredit;
    }

    public void setRelatedAppInRLOSCredit(List<ExistingCreditDetailView> relatedAppInRLOSCredit) {
        this.relatedAppInRLOSCredit = relatedAppInRLOSCredit;
    }

    public List<ExistingCollateralDetailView> getBorrowerCollateralList() {
        return borrowerCollateralList;
    }

    public void setBorrowerCollateralList(List<ExistingCollateralDetailView> borrowerCollateralList) {
        this.borrowerCollateralList = borrowerCollateralList;
    }

    public List<ExistingCollateralDetailView> getRelatedCollateralList() {
        return relatedCollateralList;
    }

    public void setRelatedCollateralList(List<ExistingCollateralDetailView> relatedCollateralList) {
        this.relatedCollateralList = relatedCollateralList;
    }

    public List<ExistingGuarantorDetailView> getBorrowerGuarantorList() {
        return borrowerGuarantorList;
    }

    public void setBorrowerGuarantorList(List<ExistingGuarantorDetailView> borrowerGuarantorList) {
        this.borrowerGuarantorList = borrowerGuarantorList;
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

    public BigDecimal getTotalBorrowerLimitPreScreen() {
        return totalBorrowerLimitPreScreen;
    }

    public void setTotalBorrowerLimitPreScreen(BigDecimal totalBorrowerLimitPreScreen) {
        this.totalBorrowerLimitPreScreen = totalBorrowerLimitPreScreen;
    }

    public BigDecimal getTotalRelatedLimitPreScreen() {
        return totalRelatedLimitPreScreen;
    }

    public void setTotalRelatedLimitPreScreen(BigDecimal totalRelatedLimitPreScreen) {
        this.totalRelatedLimitPreScreen = totalRelatedLimitPreScreen;
    }

    public List<ExistingCreditDetailView> getBorrowerExistingCreditPreScreen() {
        return borrowerExistingCreditPreScreen;
    }

    public void setBorrowerExistingCreditPreScreen(List<ExistingCreditDetailView> borrowerExistingCreditPreScreen) {
        this.borrowerExistingCreditPreScreen = borrowerExistingCreditPreScreen;
    }

    public List<ExistingCreditDetailView> getRelateExistingCreditPresScreen() {
        return relateExistingCreditPresScreen;
    }

    public void setRelateExistingCreditPresScreen(List<ExistingCreditDetailView> relateExistingCreditPresScreen) {
        this.relateExistingCreditPresScreen = relateExistingCreditPresScreen;
    }

    public List<ExistingCreditDetailView> getBorrowerComExistingCreditDeleteList() {
        return borrowerComExistingCreditDeleteList;
    }

    public void setBorrowerComExistingCreditDeleteList(List<ExistingCreditDetailView> borrowerComExistingCreditDeleteList) {
        this.borrowerComExistingCreditDeleteList = borrowerComExistingCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getBorrowerRetailExistingCreditDeleteList() {
        return borrowerRetailExistingCreditDeleteList;
    }

    public void setBorrowerRetailExistingCreditDeleteList(List<ExistingCreditDetailView> borrowerRetailExistingCreditDeleteList) {
        this.borrowerRetailExistingCreditDeleteList = borrowerRetailExistingCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getBorrowerAppInRLOSCreditDeleteList() {
        return borrowerAppInRLOSCreditDeleteList;
    }

    public void setBorrowerAppInRLOSCreditDeleteList(List<ExistingCreditDetailView> borrowerAppInRLOSCreditDeleteList) {
        this.borrowerAppInRLOSCreditDeleteList = borrowerAppInRLOSCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getBorrowerExistingCreditPreScreenDeleteList() {
        return borrowerExistingCreditPreScreenDeleteList;
    }

    public void setBorrowerExistingCreditPreScreenDeleteList(List<ExistingCreditDetailView> borrowerExistingCreditPreScreenDeleteList) {
        this.borrowerExistingCreditPreScreenDeleteList = borrowerExistingCreditPreScreenDeleteList;
    }

    public List<ExistingCreditDetailView> getRelatedComExistingCreditDeleteList() {
        return relatedComExistingCreditDeleteList;
    }

    public void setRelatedComExistingCreditDeleteList(List<ExistingCreditDetailView> relatedComExistingCreditDeleteList) {
        this.relatedComExistingCreditDeleteList = relatedComExistingCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getRelatedRetailExistingCreditDeleteList() {
        return relatedRetailExistingCreditDeleteList;
    }

    public void setRelatedRetailExistingCreditDeleteList(List<ExistingCreditDetailView> relatedRetailExistingCreditDeleteList) {
        this.relatedRetailExistingCreditDeleteList = relatedRetailExistingCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getRelatedAppInRLOSCreditDeleteList() {
        return relatedAppInRLOSCreditDeleteList;
    }

    public void setRelatedAppInRLOSCreditDeleteList(List<ExistingCreditDetailView> relatedAppInRLOSCreditDeleteList) {
        this.relatedAppInRLOSCreditDeleteList = relatedAppInRLOSCreditDeleteList;
    }

    public List<ExistingCreditDetailView> getRelateExistingCreditPresScreenDeleteList() {
        return relateExistingCreditPresScreenDeleteList;
    }

    public void setRelateExistingCreditPresScreenDeleteList(List<ExistingCreditDetailView> relateExistingCreditPresScreenDeleteList) {
        this.relateExistingCreditPresScreenDeleteList = relateExistingCreditPresScreenDeleteList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCasePrescreenId", workCasePrescreenId)
                .append("workCaseId", workCaseId)
                .append("status", status)
                .append("totalBorrowerComLimit", totalBorrowerComLimit)
                .append("totalBorrowerRetailLimit", totalBorrowerRetailLimit)
                .append("totalBorrowerAppInRLOSLimit", totalBorrowerAppInRLOSLimit)
                .append("totalBorrowerCom", totalBorrowerCom)
                .append("totalBorrowerComOBOD", totalBorrowerComOBOD)
                .append("totalBorrowerExposure", totalBorrowerExposure)
                .append("totalBorrowerNumberOfExistingOD", totalBorrowerNumberOfExistingOD)
                .append("totalBorrowerExistingODLimit", totalBorrowerExistingODLimit)
                .append("totalRelatedComLimit", totalRelatedComLimit)
                .append("totalRelatedRetailLimit", totalRelatedRetailLimit)
                .append("totalRelatedAppInRLOSLimit", totalRelatedAppInRLOSLimit)
                .append("totalRelatedCom", totalRelatedCom)
                .append("totalRelatedComOBOD", totalRelatedComOBOD)
                .append("totalRelatedExposure", totalRelatedExposure)
                .append("totalGroupCom", totalGroupCom)
                .append("totalGroupComOBOD", totalGroupComOBOD)
                .append("totalGroupExposure", totalGroupExposure)
                .append("totalGuaranteeAmount", totalGuaranteeAmount)
                .append("totalBorrowerAppraisalValue", totalBorrowerAppraisalValue)
                .append("totalBorrowerMortgageValue", totalBorrowerMortgageValue)
                .append("totalRelatedAppraisalValue", totalRelatedAppraisalValue)
                .append("totalRelatedMortgageValue", totalRelatedMortgageValue)
                .append("borrowerComExistingCredit", borrowerComExistingCredit)
                .append("borrowerRetailExistingCredit", borrowerRetailExistingCredit)
                .append("borrowerAppInRLOSCredit", borrowerAppInRLOSCredit)
                .append("existingConditionDetailViewList", existingConditionDetailViewList)
                .append("relatedComExistingCredit", relatedComExistingCredit)
                .append("relatedRetailExistingCredit", relatedRetailExistingCredit)
                .append("relatedAppInRLOSCredit", relatedAppInRLOSCredit)
                .append("borrowerCollateralList", borrowerCollateralList)
                .append("relatedCollateralList", relatedCollateralList)
                .append("borrowerGuarantorList", borrowerGuarantorList)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
