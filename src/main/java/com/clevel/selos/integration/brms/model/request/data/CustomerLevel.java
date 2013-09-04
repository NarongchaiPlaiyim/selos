package com.clevel.selos.integration.brms.model.request.data;

import java.util.Date;

public class CustomerLevel {
    public int borrowerType; //enum
    public int relationshipType; //enum
    public int nationality; //enum
    public String personalId; //use for citizenID, registrationID, passportNumber
    public int ageMonths;
    public int newQualitativeClass; //enum ,Default to 'P'
    public Date lastReviewDate;
    public Date extendedReviewDate;
    public int SCFScore;
    public int warningCodeFullMatch; //enum
    public int warningCodeSomeMatch; //enum
    public boolean creditWorthiness;
    public int kycLevel; //enum
    public int noOfNCBCheckIn6months;
    public int numberOfDaysLastNCBCheck;

    public NcbAccount ncbAccount; //should be in CustomerLevel?
    public BankAccount bankAccount; //should be in CustomerLevel?
    public TmbAccount tmbAccount; //should be in CustomerLevel?
    //todo add more field

    public CustomerLevel() {
    }

    public CustomerLevel(int borrowerType, int relationshipType, int nationality, String personalId, int ageMonths, int newQualitativeClass, Date lastReviewDate, Date extendedReviewDate, int SCFScore, int warningCodeFullMatch, int warningCodeSomeMatch, boolean creditWorthiness, int kycLevel, int noOfNCBCheckIn6months, int numberOfDaysLastNCBCheck, NcbAccount ncbAccount) {
        this.borrowerType = borrowerType;
        this.relationshipType = relationshipType;
        this.nationality = nationality;
        this.personalId = personalId;
        this.ageMonths = ageMonths;
        this.newQualitativeClass = newQualitativeClass;
        this.lastReviewDate = lastReviewDate;
        this.extendedReviewDate = extendedReviewDate;
        this.SCFScore = SCFScore;
        this.warningCodeFullMatch = warningCodeFullMatch;
        this.warningCodeSomeMatch = warningCodeSomeMatch;
        this.creditWorthiness = creditWorthiness;
        this.kycLevel = kycLevel;
        this.noOfNCBCheckIn6months = noOfNCBCheckIn6months;
        this.numberOfDaysLastNCBCheck = numberOfDaysLastNCBCheck;
        this.ncbAccount = ncbAccount;
    }

    public int getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(int borrowerType) {
        this.borrowerType = borrowerType;
    }

    public int getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
        this.nationality = nationality;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public int getNewQualitativeClass() {
        return newQualitativeClass;
    }

    public void setNewQualitativeClass(int newQualitativeClass) {
        this.newQualitativeClass = newQualitativeClass;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public Date getExtendedReviewDate() {
        return extendedReviewDate;
    }

    public void setExtendedReviewDate(Date extendedReviewDate) {
        this.extendedReviewDate = extendedReviewDate;
    }

    public int getSCFScore() {
        return SCFScore;
    }

    public void setSCFScore(int SCFScore) {
        this.SCFScore = SCFScore;
    }

    public int getWarningCodeFullMatch() {
        return warningCodeFullMatch;
    }

    public void setWarningCodeFullMatch(int warningCodeFullMatch) {
        this.warningCodeFullMatch = warningCodeFullMatch;
    }

    public int getWarningCodeSomeMatch() {
        return warningCodeSomeMatch;
    }

    public void setWarningCodeSomeMatch(int warningCodeSomeMatch) {
        this.warningCodeSomeMatch = warningCodeSomeMatch;
    }

    public boolean isCreditWorthiness() {
        return creditWorthiness;
    }

    public void setCreditWorthiness(boolean creditWorthiness) {
        this.creditWorthiness = creditWorthiness;
    }

    public int getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(int kycLevel) {
        this.kycLevel = kycLevel;
    }

    public int getNoOfNCBCheckIn6months() {
        return noOfNCBCheckIn6months;
    }

    public void setNoOfNCBCheckIn6months(int noOfNCBCheckIn6months) {
        this.noOfNCBCheckIn6months = noOfNCBCheckIn6months;
    }

    public int getNumberOfDaysLastNCBCheck() {
        return numberOfDaysLastNCBCheck;
    }

    public void setNumberOfDaysLastNCBCheck(int numberOfDaysLastNCBCheck) {
        this.numberOfDaysLastNCBCheck = numberOfDaysLastNCBCheck;
    }

    public NcbAccount getNcbAccount() {
        return ncbAccount;
    }

    public void setNcbAccount(NcbAccount ncbAccount) {
        this.ncbAccount = ncbAccount;
    }
}
