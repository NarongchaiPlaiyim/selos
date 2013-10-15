package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class CustomerLevel implements Serializable {
    private CustomerEntity customerEntity;
    private boolean isExistingSMECustomer;
    private Relation relationshipType;
    private Reference reference;
    private Nationality nationality;
    private int numberOfMonthFromLastSetUpDate;
    private String newQualitative; //todo : to be enum
    private Date nextReviewDate;
    private boolean nextReviewDateFlag;
    private Date extendedReviewDate;
    private boolean extendedReviewDateFlag;
    private String ratingFinal;
    private boolean unpaidFeePaid;
    private boolean claimedLGFlag;
    private String personalId;
    private int age;
    private boolean ncbFlag;
    private int numSearchesLast6Mths;
    private int numberOfDaysNCBcheck;
    private WarningCode warningCodeFullyMatched;
    private WarningCode warningCodeSomeMatched;
    private int dayOverdueAnnualReview;


    public CustomerLevel() {
    }

    public CustomerLevel(CustomerEntity customerEntity, boolean existingSMECustomer, Relation relationshipType, Reference reference, Nationality nationality, int numberOfMonthFromLastSetUpDate, String newQualitative, Date nextReviewDate, boolean nextReviewDateFlag, Date extendedReviewDate, boolean extendedReviewDateFlag, String ratingFinal, boolean unpaidFeePaid, boolean claimedLGFlag, String personalId, int age, boolean ncbFlag, int numSearchesLast6Mths, int numberOfDaysNCBcheck, WarningCode warningCodeFullyMatched, WarningCode warningCodeSomeMatched, int dayOverdueAnnualReview) {
        this.customerEntity = customerEntity;
        isExistingSMECustomer = existingSMECustomer;
        this.relationshipType = relationshipType;
        this.reference = reference;
        this.nationality = nationality;
        this.numberOfMonthFromLastSetUpDate = numberOfMonthFromLastSetUpDate;
        this.newQualitative = newQualitative;
        this.nextReviewDate = nextReviewDate;
        this.nextReviewDateFlag = nextReviewDateFlag;
        this.extendedReviewDate = extendedReviewDate;
        this.extendedReviewDateFlag = extendedReviewDateFlag;
        this.ratingFinal = ratingFinal;
        this.unpaidFeePaid = unpaidFeePaid;
        this.claimedLGFlag = claimedLGFlag;
        this.personalId = personalId;
        this.age = age;
        this.ncbFlag = ncbFlag;
        this.numSearchesLast6Mths = numSearchesLast6Mths;
        this.numberOfDaysNCBcheck = numberOfDaysNCBcheck;
        this.warningCodeFullyMatched = warningCodeFullyMatched;
        this.warningCodeSomeMatched = warningCodeSomeMatched;
        this.dayOverdueAnnualReview = dayOverdueAnnualReview;
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

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
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

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(boolean ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public int getNumSearchesLast6Mths() {
        return numSearchesLast6Mths;
    }

    public void setNumSearchesLast6Mths(int numSearchesLast6Mths) {
        this.numSearchesLast6Mths = numSearchesLast6Mths;
    }

    public int getNumberOfDaysNCBcheck() {
        return numberOfDaysNCBcheck;
    }

    public void setNumberOfDaysNCBcheck(int numberOfDaysNCBcheck) {
        this.numberOfDaysNCBcheck = numberOfDaysNCBcheck;
    }

    public WarningCode getWarningCodeFullyMatched() {
        return warningCodeFullyMatched;
    }

    public void setWarningCodeFullyMatched(WarningCode warningCodeFullyMatched) {
        this.warningCodeFullyMatched = warningCodeFullyMatched;
    }

    public WarningCode getWarningCodeSomeMatched() {
        return warningCodeSomeMatched;
    }

    public void setWarningCodeSomeMatched(WarningCode warningCodeSomeMatched) {
        this.warningCodeSomeMatched = warningCodeSomeMatched;
    }

    public int getDayOverdueAnnualReview() {
        return dayOverdueAnnualReview;
    }

    public void setDayOverdueAnnualReview(int dayOverdueAnnualReview) {
        this.dayOverdueAnnualReview = dayOverdueAnnualReview;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("customerEntity", customerEntity)
                .append("isExistingSMECustomer", isExistingSMECustomer)
                .append("relationshipType", relationshipType)
                .append("reference", reference)
                .append("nationality", nationality)
                .append("numberOfMonthFromLastSetUpDate", numberOfMonthFromLastSetUpDate)
                .append("newQualitative", newQualitative)
                .append("nextReviewDate", nextReviewDate)
                .append("nextReviewDateFlag", nextReviewDateFlag)
                .append("extendedReviewDate", extendedReviewDate)
                .append("extendedReviewDateFlag", extendedReviewDateFlag)
                .append("ratingFinal", ratingFinal)
                .append("unpaidFeePaid", unpaidFeePaid)
                .append("claimedLGFlag", claimedLGFlag)
                .append("personalId", personalId)
                .append("age", age)
                .append("ncbFlag", ncbFlag)
                .append("numSearchesLast6Mths", numSearchesLast6Mths)
                .append("numberOfDaysNCBcheck", numberOfDaysNCBcheck)
                .append("warningCodeFullyMatched", warningCodeFullyMatched)
                .append("warningCodeSomeMatched", warningCodeSomeMatched)
                .append("dayOverdueAnnualReview", dayOverdueAnnualReview)
                .toString();
    }
}
