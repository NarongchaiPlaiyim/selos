package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.db.master.Relation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BorrowerTypeLevel implements Serializable {

    //Attribute
    private Nationality nationality;
    private int kyc;
    private CustomerEntity customerEntity; //5
    private boolean isExistingSMECustomer; //6
    private Relation relationshipType;
    private Reference reference;
    private int numberOfMonthFromLastSetUpDate;
    private String newQualitative; //todo : to be enum
    private Date nextReviewDate;
    private boolean nextReviewDateFlag;
    private Date extendedReviewDate;
    private boolean extendedReviewDateFlag;
    private String ratingFinal;
    private boolean unpaidFeePaid;
    private boolean claimedLGFlag;
    private String creditWorthiness; //todo : to be enum
    private String sPouseId;
    private String sPouseRelationshipType; //todo : to be enum
    private BigDecimal dayAnnualReviewOverdue;

    //AccountType
    private List<AccountTypeLevelBorrower> accountType;

    //IndividualType
    private IndividualTypeLevel individualType;

    //NCBReportType
    private NCBReportTypeLevel ncbReportType;

    //WarningCodeFullMatched
    private List<WarningCodeFullMatched> warningCodeFullMatched;

    //WarningCodePartialMatched
    private List<WarningCodePartialMatched> warningCodePartialMatched;

    public BorrowerTypeLevel() {
    }

    public BorrowerTypeLevel(Nationality nationality, int kyc, CustomerEntity customerEntity, boolean existingSMECustomer, Relation relationshipType, Reference reference, int numberOfMonthFromLastSetUpDate, String newQualitative, Date nextReviewDate, boolean nextReviewDateFlag, Date extendedReviewDate, boolean extendedReviewDateFlag, String ratingFinal, boolean unpaidFeePaid, boolean claimedLGFlag, String creditWorthiness, String sPouseId, String sPouseRelationshipType, BigDecimal dayAnnualReviewOverdue, List<AccountTypeLevelBorrower> accountType, IndividualTypeLevel individualType, NCBReportTypeLevel ncbReportType, List<WarningCodeFullMatched> warningCodeFullMatched, List<WarningCodePartialMatched> warningCodePartialMatched) {
        this.nationality = nationality;
        this.kyc = kyc;
        this.customerEntity = customerEntity;
        isExistingSMECustomer = existingSMECustomer;
        this.relationshipType = relationshipType;
        this.reference = reference;
        this.numberOfMonthFromLastSetUpDate = numberOfMonthFromLastSetUpDate;
        this.newQualitative = newQualitative;
        this.nextReviewDate = nextReviewDate;
        this.nextReviewDateFlag = nextReviewDateFlag;
        this.extendedReviewDate = extendedReviewDate;
        this.extendedReviewDateFlag = extendedReviewDateFlag;
        this.ratingFinal = ratingFinal;
        this.unpaidFeePaid = unpaidFeePaid;
        this.claimedLGFlag = claimedLGFlag;
        this.creditWorthiness = creditWorthiness;
        this.sPouseId = sPouseId;
        this.sPouseRelationshipType = sPouseRelationshipType;
        this.dayAnnualReviewOverdue = dayAnnualReviewOverdue;
        this.accountType = accountType;
        this.individualType = individualType;
        this.ncbReportType = ncbReportType;
        this.warningCodeFullMatched = warningCodeFullMatched;
        this.warningCodePartialMatched = warningCodePartialMatched;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public int getKyc() {
        return kyc;
    }

    public void setKyc(int kyc) {
        this.kyc = kyc;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public boolean isExistingSMECustomer() {
        return isExistingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        isExistingSMECustomer = existingSMECustomer;
    }

    public Relation getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(Relation relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public int getNumberOfMonthFromLastSetUpDate() {
        return numberOfMonthFromLastSetUpDate;
    }

    public void setNumberOfMonthFromLastSetUpDate(int numberOfMonthFromLastSetUpDate) {
        this.numberOfMonthFromLastSetUpDate = numberOfMonthFromLastSetUpDate;
    }

    public String getNewQualitative() {
        return newQualitative;
    }

    public void setNewQualitative(String newQualitative) {
        this.newQualitative = newQualitative;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public boolean isNextReviewDateFlag() {
        return nextReviewDateFlag;
    }

    public void setNextReviewDateFlag(boolean nextReviewDateFlag) {
        this.nextReviewDateFlag = nextReviewDateFlag;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public boolean isExtendedReviewDateFlag() {
        return extendedReviewDateFlag;
    }

    public void setExtendedReviewDateFlag(boolean extendedReviewDateFlag) {
        this.extendedReviewDateFlag = extendedReviewDateFlag;
    }

    public String getRatingFinal() {
        return ratingFinal;
    }

    public void setRatingFinal(String ratingFinal) {
        this.ratingFinal = ratingFinal;
    }

    public boolean isUnpaidFeePaid() {
        return unpaidFeePaid;
    }

    public void setUnpaidFeePaid(boolean unpaidFeePaid) {
        this.unpaidFeePaid = unpaidFeePaid;
    }

    public boolean isClaimedLGFlag() {
        return claimedLGFlag;
    }

    public void setClaimedLGFlag(boolean claimedLGFlag) {
        this.claimedLGFlag = claimedLGFlag;
    }

    public String getCreditWorthiness() {
        return creditWorthiness;
    }

    public void setCreditWorthiness(String creditWorthiness) {
        this.creditWorthiness = creditWorthiness;
    }

    public String getsPouseId() {
        return sPouseId;
    }

    public void setsPouseId(String sPouseId) {
        this.sPouseId = sPouseId;
    }

    public String getsPouseRelationshipType() {
        return sPouseRelationshipType;
    }

    public void setsPouseRelationshipType(String sPouseRelationshipType) {
        this.sPouseRelationshipType = sPouseRelationshipType;
    }

    public BigDecimal getDayAnnualReviewOverdue() {
        return dayAnnualReviewOverdue;
    }

    public void setDayAnnualReviewOverdue(BigDecimal dayAnnualReviewOverdue) {
        this.dayAnnualReviewOverdue = dayAnnualReviewOverdue;
    }

    public List<AccountTypeLevelBorrower> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<AccountTypeLevelBorrower> accountType) {
        this.accountType = accountType;
    }

    public IndividualTypeLevel getIndividualType() {
        return individualType;
    }

    public void setIndividualType(IndividualTypeLevel individualType) {
        this.individualType = individualType;
    }

    public NCBReportTypeLevel getNcbReportType() {
        return ncbReportType;
    }

    public void setNcbReportType(NCBReportTypeLevel ncbReportType) {
        this.ncbReportType = ncbReportType;
    }

    public List<WarningCodeFullMatched> getWarningCodeFullMatched() {
        return warningCodeFullMatched;
    }

    public void setWarningCodeFullMatched(List<WarningCodeFullMatched> warningCodeFullMatched) {
        this.warningCodeFullMatched = warningCodeFullMatched;
    }

    public List<WarningCodePartialMatched> getWarningCodePartialMatched() {
        return warningCodePartialMatched;
    }

    public void setWarningCodePartialMatched(List<WarningCodePartialMatched> warningCodePartialMatched) {
        this.warningCodePartialMatched = warningCodePartialMatched;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("nationality", nationality)
                .append("kyc", kyc)
                .append("customerEntity", customerEntity)
                .append("isExistingSMECustomer", isExistingSMECustomer)
                .append("relationshipType", relationshipType)
                .append("reference", reference)
                .append("numberOfMonthFromLastSetUpDate", numberOfMonthFromLastSetUpDate)
                .append("newQualitative", newQualitative)
                .append("nextReviewDate", nextReviewDate)
                .append("nextReviewDateFlag", nextReviewDateFlag)
                .append("extendedReviewDate", extendedReviewDate)
                .append("extendedReviewDateFlag", extendedReviewDateFlag)
                .append("ratingFinal", ratingFinal)
                .append("unpaidFeePaid", unpaidFeePaid)
                .append("claimedLGFlag", claimedLGFlag)
                .append("creditWorthiness", creditWorthiness)
                .append("sPouseId", sPouseId)
                .append("sPouseRelationshipType", sPouseRelationshipType)
                .append("dayAnnualReviewOverdue", dayAnnualReviewOverdue)
                .append("accountType", accountType)
                .append("individualType", individualType)
                .append("ncbReportType", ncbReportType)
                .append("warningCodeFullMatched", warningCodeFullMatched)
                .append("warningCodePartialMatched", warningCodePartialMatched)
                .toString();
    }
}
