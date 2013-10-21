package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

public class ExistingCreditView {

    private List<ActionStatusView> status;
    private long id;
    private long workcasePrescreenId;
    private long getWorkcaseFullAppId;
    private BigDecimal totalBorrowerComLimit;
    private BigDecimal totalBorrowerRetailLimit;
    private BigDecimal totalBorrowerAppInRLOSLimit;
    private BigDecimal totalRelatedComLimit;
    private BigDecimal totalRelatedRetailLimit;
    private BigDecimal totalRelatedAppInRLOSLimit;
    private BigDecimal totalGroupCom;
    private BigDecimal totalGroupComOBOD;
    private BigDecimal totalGroupExposure;

    private List<ExistingCreditDetailView> borrowerComExistingCredit;
    private List<ExistingCreditDetailView> borrowerRetailExistingCredit;
    private List<ExistingCreditDetailView> borrowerAppInRLOSCredit;
    private List<ExistingCreditDetailView> relatedComExistingCredit;
    private List<ExistingCreditDetailView> relatedRetailExistingCredit;
    private List<ExistingCreditDetailView> relatedAppInRLOSCredit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkcasePrescreenId() {
        return workcasePrescreenId;
    }

    public void setWorkcasePrescreenId(long workcasePrescreenId) {
        this.workcasePrescreenId = workcasePrescreenId;
    }

    public long getGetWorkcaseFullAppId() {
        return getWorkcaseFullAppId;
    }

    public void setGetWorkcaseFullAppId(long getWorkcaseFullAppId) {
        this.getWorkcaseFullAppId = getWorkcaseFullAppId;
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

    public List<ActionStatusView> getStatus() {
        return status;
    }

    public void setStatus(List<ActionStatusView> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("status", status)
                .append("totalBorrowerComLimit", totalBorrowerComLimit)
                .append("totalBorrowerRetailLimit", totalBorrowerRetailLimit)
                .append("totalBorrowerAppInRLOSLimit", totalBorrowerAppInRLOSLimit)
                .append("totalRelatedComLimit", totalRelatedComLimit)
                .append("totalRelatedRetailLimit", totalRelatedRetailLimit)
                .append("totalRelatedAppInRLOSLimit", totalRelatedAppInRLOSLimit)
                .append("totalGroupCom", totalGroupCom)
                .append("totalGroupComOBOD", totalGroupComOBOD)
                .append("totalGroupExposure", totalGroupExposure)
                .append("borrowerComExistingCredit", borrowerComExistingCredit)
                .append("borrowerRetailExistingCredit", borrowerRetailExistingCredit)
                .append("borrowerAppInRLOSCredit", borrowerAppInRLOSCredit)
                .append("relatedComExistingCredit", relatedComExistingCredit)
                .append("relatedRetailExistingCredit", relatedRetailExistingCredit)
                .append("relatedAppInRLOSCredit", relatedAppInRLOSCredit)
                .toString();
    }
}
