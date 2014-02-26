package com.clevel.selos.integration.brms.model.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BRMSCustomerInfo implements Serializable{

    private boolean individual;
    private String customerEntity;
    private boolean existingSMECustomer;
    private String relation;
    private String reference;
    private String nationality;
    private BigDecimal numberOfMonthLastContractDate;
    private String qualitativeClass;
    private String adjustClass;
    private Date nextReviewDate;
    private boolean nextReviewDateFlag;
    private Date extendedReviewDate;
    private boolean extendedReviewDateFlag;
    private String ratingFinal;
    private boolean unpaidFeeInsurance;
    private boolean pendingClaimLG;
    private String creditWorthiness;
    private int kycLevel;
    private String spousePersonalID;
    private String spouseRelationType;
    private String personalID;
    private int ageMonths;
    private String marriageStatus;
    private boolean ncbFlag;
    private int numberOfNCBCheckIn6Months;
    private BigDecimal numberOfDayLastNCBCheck;
    private String numberOfDaysOverAnnualReview;

    private List<String> csiFullyMatchCode;
    private List<String> csiSomeMatchCode;
    private List<BRMSTMBAccountInfo> tmbAccountInfoList;
    private List<BRMSNCBAccountInfo> ncbAccountInfoList;

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public String getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(String customerEntity) {
        this.customerEntity = customerEntity;
    }

    public boolean isExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public BigDecimal getNumberOfMonthLastContractDate() {
        return numberOfMonthLastContractDate;
    }

    public void setNumberOfMonthLastContractDate(BigDecimal numberOfMonthLastContractDate) {
        this.numberOfMonthLastContractDate = numberOfMonthLastContractDate;
    }

    public String getQualitativeClass() {
        return qualitativeClass;
    }

    public void setQualitativeClass(String qualitativeClass) {
        this.qualitativeClass = qualitativeClass;
    }

    public String getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(String adjustClass) {
        this.adjustClass = adjustClass;
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

    public boolean isUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(boolean unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public boolean isPendingClaimLG() {
        return pendingClaimLG;
    }

    public void setPendingClaimLG(boolean pendingClaimLG) {
        this.pendingClaimLG = pendingClaimLG;
    }

    public String getCreditWorthiness() {
        return creditWorthiness;
    }

    public void setCreditWorthiness(String creditWorthiness) {
        this.creditWorthiness = creditWorthiness;
    }

    public int getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(int kycLevel) {
        this.kycLevel = kycLevel;
    }

    public String getSpousePersonalID() {
        return spousePersonalID;
    }

    public void setSpousePersonalID(String spousePersonalID) {
        this.spousePersonalID = spousePersonalID;
    }

    public String getSpouseRelationType() {
        return spouseRelationType;
    }

    public void setSpouseRelationType(String spouseRelationType) {
        this.spouseRelationType = spouseRelationType;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public boolean isNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(boolean ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public int getNumberOfNCBCheckIn6Months() {
        return numberOfNCBCheckIn6Months;
    }

    public void setNumberOfNCBCheckIn6Months(int numberOfNCBCheckIn6Months) {
        this.numberOfNCBCheckIn6Months = numberOfNCBCheckIn6Months;
    }

    public BigDecimal getNumberOfDayLastNCBCheck() {
        return numberOfDayLastNCBCheck;
    }

    public void setNumberOfDayLastNCBCheck(BigDecimal numberOfDayLastNCBCheck) {
        this.numberOfDayLastNCBCheck = numberOfDayLastNCBCheck;
    }

    public List<String> getCsiFullyMatchCode() {
        return csiFullyMatchCode;
    }

    public void setCsiFullyMatchCode(List<String> csiFullyMatchCode) {
        this.csiFullyMatchCode = csiFullyMatchCode;
    }

    public List<String> getCsiSomeMatchCode() {
        return csiSomeMatchCode;
    }

    public void setCsiSomeMatchCode(List<String> csiSomeMatchCode) {
        this.csiSomeMatchCode = csiSomeMatchCode;
    }

    public String getNumberOfDaysOverAnnualReview() {
        return numberOfDaysOverAnnualReview;
    }

    public void setNumberOfDaysOverAnnualReview(String numberOfDaysOverAnnualReview) {
        this.numberOfDaysOverAnnualReview = numberOfDaysOverAnnualReview;
    }

    public List<BRMSTMBAccountInfo> getTmbAccountInfoList() {
        return tmbAccountInfoList;
    }

    public void setTmbAccountInfoList(List<BRMSTMBAccountInfo> tmbAccountInfoList) {
        this.tmbAccountInfoList = tmbAccountInfoList;
    }

    public List<BRMSNCBAccountInfo> getNcbAccountInfoList() {
        return ncbAccountInfoList;
    }

    public void setNcbAccountInfoList(List<BRMSNCBAccountInfo> ncbAccountInfoList) {
        this.ncbAccountInfoList = ncbAccountInfoList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("individual", individual)
                .append("customerEntity", customerEntity)
                .append("existingSMECustomer", existingSMECustomer)
                .append("relation", relation)
                .append("reference", reference)
                .append("nationality", nationality)
                .append("numberOfMonthLastContractDate", numberOfMonthLastContractDate)
                .append("qualitativeClass", qualitativeClass)
                .append("adjustClass", adjustClass)
                .append("nextReviewDate", nextReviewDate)
                .append("nextReviewDateFlag", nextReviewDateFlag)
                .append("extendedReviewDate", extendedReviewDate)
                .append("extendedReviewDateFlag", extendedReviewDateFlag)
                .append("ratingFinal", ratingFinal)
                .append("unpaidFeeInsurance", unpaidFeeInsurance)
                .append("pendingClaimLG", pendingClaimLG)
                .append("creditWorthiness", creditWorthiness)
                .append("kycLevel", kycLevel)
                .append("spousePersonalID", spousePersonalID)
                .append("spouseRelationType", spouseRelationType)
                .append("personalID", personalID)
                .append("ageMonths", ageMonths)
                .append("marriageStatus", marriageStatus)
                .append("ncbFlag", ncbFlag)
                .append("numberOfNCBCheckIn6Months", numberOfNCBCheckIn6Months)
                .append("numberOfDayLastNCBCheck", numberOfDayLastNCBCheck)
                .append("numberOfDaysOverAnnualReview", numberOfDaysOverAnnualReview)
                .append("csiFullyMatchCode", csiFullyMatchCode)
                .append("csiSomeMatchCode", csiSomeMatchCode)
                .toString();
    }
}
