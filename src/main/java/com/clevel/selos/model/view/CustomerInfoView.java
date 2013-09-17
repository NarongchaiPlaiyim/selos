package com.clevel.selos.model.view;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerInfoView implements Serializable {
    private BigDecimal id;
    private int searchBy;
    private String searchId;
    private DocumentType documentType;
    private CustomerEntity customerEntity;
    private BorrowerType borrowerType;
    private Relation relation;
    private Reference reference;
    private String citizenId;
    private String registrationId;
    private String cardAuthorizeBy;
    private Date documentExpiredDate;
    private String customerId;
    private String serviceSegment;
    private Boolean collateralOwner;
    private BigDecimal percentShare;
    private Title titleTh;
    private Title titleEn;
    private String firstNameTh;
    private String lastNameTh;
    private String firstNameEn;
    private String lastNameEn;
    private Date dateOfBirth;
    private Date dateOfRegister;
    private Gender gender;
    private int age;
    private Nationality origin;
    private Nationality nationality;
    private Education education;
    private Occupation occupation;
    private MaritalStatus maritalStatus;
    private int numberOfChild;
    private List<ChildrenView> childrenList;
    private Country citizenCountry;
    private Country registrationCountry;
    private AddressView currentAddress;
    private AddressView workAddress;
    private AddressView registerAddress;
    private AddressType mailingAddressType;
    private BigDecimal approxIncome;
    private String mobileNumber;
    private String faxNumber;
    private String email;
    private KYCLevel kycLevel;
    private boolean convenantFlag;
    private boolean ewsFlag;
    private boolean reviewFlag;
    private String reason;

    public CustomerInfoView(){
        reset();
    }

    public void reset(){
        this.id = new BigDecimal(0);
        this.searchBy = 0;
        this.searchId = "";
        this.documentType = new DocumentType();
        this.customerEntity = new CustomerEntity();
        this.borrowerType = new BorrowerType();
        this.relation = new Relation();
        this.reference = new Reference();
        this.citizenId = "";
        this.registrationId = "";
        this.cardAuthorizeBy = "";
        this.documentExpiredDate = new Date();
        this.customerId = "";
        this.serviceSegment = "";
        this.collateralOwner = false;
        this.percentShare = new BigDecimal(0);
        this.titleTh = new Title();
        this.titleEn = new Title();
        this.firstNameTh = "";
        this.lastNameTh = "";
        this.firstNameEn = "";
        this.lastNameEn = "";
        this.dateOfBirth = new Date();
        this.dateOfRegister = new Date();
        //this.gender = new Gender(0);
        this.age = 0;
        this.origin = new Nationality();
        this.nationality = new Nationality();
        this.education = new Education();
        this.occupation = new Occupation();
        this.maritalStatus = new MaritalStatus();
        this.numberOfChild = 0;
        this.childrenList = new ArrayList<ChildrenView>();
        this.citizenCountry = new Country();
        this.registrationCountry = new Country();
        this.currentAddress = new AddressView();
        this.workAddress = new AddressView();
        this.registerAddress = new AddressView();
        this.mailingAddressType = new AddressType();
        this.approxIncome = new BigDecimal(0);
        this.mobileNumber = "";
        this.faxNumber = "";
        this.email = "";
        this.kycLevel = new KYCLevel();
        this.convenantFlag = false;
        this.ewsFlag = false;
        this.reviewFlag = false;
        this.reason = "";
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public int getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(int searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public BorrowerType getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(BorrowerType borrowerType) {
        this.borrowerType = borrowerType;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getCardAuthorizeBy() {
        return cardAuthorizeBy;
    }

    public void setCardAuthorizeBy(String cardAuthorizeBy) {
        this.cardAuthorizeBy = cardAuthorizeBy;
    }

    public Date getDocumentExpiredDate() {
        return documentExpiredDate;
    }

    public void setDocumentExpiredDate(Date documentExpiredDate) {
        this.documentExpiredDate = documentExpiredDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceSegment() {
        return serviceSegment;
    }

    public void setServiceSegment(String serviceSegment) {
        this.serviceSegment = serviceSegment;
    }

    public Boolean getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(Boolean collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public BigDecimal getPercentShare() {
        return percentShare;
    }

    public void setPercentShare(BigDecimal percentShare) {
        this.percentShare = percentShare;
    }

    public Title getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(Title titleTh) {
        this.titleTh = titleTh;
    }

    public Title getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(Title titleEn) {
        this.titleEn = titleEn;
    }

    public String getFirstNameTh() {
        return firstNameTh;
    }

    public void setFirstNameTh(String firstNameTh) {
        this.firstNameTh = firstNameTh;
    }

    public String getLastNameTh() {
        return lastNameTh;
    }

    public void setLastNameTh(String lastNameTh) {
        this.lastNameTh = lastNameTh;
    }

    public String getFirstNameEn() {
        return firstNameEn;
    }

    public void setFirstNameEn(String firstNameEn) {
        this.firstNameEn = firstNameEn;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(Date dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Nationality getOrigin() {
        return origin;
    }

    public void setOrigin(Nationality origin) {
        this.origin = origin;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getNumberOfChild() {
        return numberOfChild;
    }

    public void setNumberOfChild(int numberOfChild) {
        this.numberOfChild = numberOfChild;
    }

    public List<ChildrenView> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<ChildrenView> childrenList) {
        this.childrenList = childrenList;
    }

    public Country getCitizenCountry() {
        return citizenCountry;
    }

    public void setCitizenCountry(Country citizenCountry) {
        this.citizenCountry = citizenCountry;
    }

    public Country getRegistrationCountry() {
        return registrationCountry;
    }

    public void setRegistrationCountry(Country registrationCountry) {
        this.registrationCountry = registrationCountry;
    }

    public AddressView getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(AddressView currentAddress) {
        this.currentAddress = currentAddress;
    }

    public AddressView getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(AddressView workAddress) {
        this.workAddress = workAddress;
    }

    public AddressView getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(AddressView registerAddress) {
        this.registerAddress = registerAddress;
    }

    public AddressType getMailingAddressType() {
        return mailingAddressType;
    }

    public void setMailingAddressType(AddressType mailingAddressType) {
        this.mailingAddressType = mailingAddressType;
    }

    public BigDecimal getApproxIncome() {
        return approxIncome;
    }

    public void setApproxIncome(BigDecimal approxIncome) {
        this.approxIncome = approxIncome;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public KYCLevel getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(KYCLevel kycLevel) {
        this.kycLevel = kycLevel;
    }

    public boolean isConvenantFlag() {
        return convenantFlag;
    }

    public void setConvenantFlag(boolean convenantFlag) {
        this.convenantFlag = convenantFlag;
    }

    public boolean isEwsFlag() {
        return ewsFlag;
    }

    public void setEwsFlag(boolean ewsFlag) {
        this.ewsFlag = ewsFlag;
    }

    public boolean isReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(boolean reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("searchBy", searchBy)
                .append("searchId", searchId)
                .append("documentType", documentType)
                .append("customerEntity", customerEntity)
                .append("borrowerType", borrowerType)
                .append("relation", relation)
                .append("reference", reference)
                .append("citizenId", citizenId)
                .append("registrationId", registrationId)
                .append("cardAuthorizeBy", cardAuthorizeBy)
                .append("documentExpiredDate", documentExpiredDate)
                .append("customerId", customerId)
                .append("serviceSegment", serviceSegment)
                .append("collateralOwner", collateralOwner)
                .append("percentShare", percentShare)
                .append("titleTh", titleTh)
                .append("titleEn", titleEn)
                .append("firstNameTh", firstNameTh)
                .append("lastNameTh", lastNameTh)
                .append("firstNameEn", firstNameEn)
                .append("lastNameEn", lastNameEn)
                .append("dateOfBirth", dateOfBirth)
                .append("dateOfRegister", dateOfRegister)
                .append("gender", gender)
                .append("age", age)
                .append("origin", origin)
                .append("nationality", nationality)
                .append("education", education)
                .append("occupation", occupation)
                .append("maritalStatus", maritalStatus)
                .append("numberOfChild", numberOfChild)
                .append("childrenList", childrenList)
                .append("citizenCountry", citizenCountry)
                .append("registrationCountry", registrationCountry)
                .append("currentAddress", currentAddress)
                .append("workAddress", workAddress)
                .append("registerAddress", registerAddress)
                .append("mailingAddressType", mailingAddressType)
                .append("approxIncome", approxIncome)
                .append("mobileNumber", mobileNumber)
                .append("faxNumber", faxNumber)
                .append("email", email)
                .append("kycLevel", kycLevel)
                .append("convenantFlag", convenantFlag)
                .append("ewsFlag", ewsFlag)
                .append("reviewFlag", reviewFlag)
                .append("reason", reason)
                .toString();
    }
}
