package com.clevel.selos.model.view;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerInfoView implements Serializable, Cloneable {
    //*** Var for transform ***//
    private long individualId;
    private long juristicId;

    //*** Var for list and editable ***//
    private int listIndex;
    private int subIndex;
    private String listName;
    private int isSpouse;
    private int isCommittee;

    //*** Var for search ***//
    private int searchBy;
    private String searchId;
    private int searchFromRM;

    //*** Var for Check Citizen ***//
    private String inputId;
    private int validId;
    private String ncbResult;
    private String ncbReason;
    private String csiResult;
    private String csiReason;
    private int csiFlag;

    //*** Var for Customer ***//
    private long id;
    private int age;
    private Date documentExpiredDate;
    private Title titleTh;
    private Title titleEn;
    private String firstNameTh;
    private String lastNameTh;
    private String firstNameEn;
    private String lastNameEn;
    private int ncbFlag;
    private CustomerEntity customerEntity;
    private DocumentType documentType;
    private Relation relation;
    private Reference reference;
    private String documentAuthorizeBy;
    private String serviceSegment;
    private String tmbCustomerId;
    private int collateralOwner;
    private BigDecimal percentShare;
    private BigDecimal approxIncome;
    private String mobileNumber;
    private String faxNumber;
    private String email;
    private KYCLevel kycLevel;
    private int convenantFlag;
    private int reviewFlag;
    private String reason;
    private BusinessType businessType;
    private Date documentAuthorizeDate;
    private String kycReason;
    private int worthiness;
    private AddressType mailingAddressType;
    private long spouseId;
    private List<CustomerCSIView> customerCSIList;
    private Country sourceIncome;
    private Country countryIncome;
    private long committeeId;

    //*** Var for Individual ***//
    private Date dateOfBirth;
    private String citizenId;
    private String passportId;
    private int gender;
    private int numberOfChild;
    private Education education;
    private MaritalStatus maritalStatus;
    private Nationality nationality;
    private Nationality sndNationality;
    private Race origin;
    private Occupation occupation;
    private Country citizenCountry;

    //*** Var for Juristic ***//
    private BigDecimal capital;
    private int financialYear;
    private Date dateOfRegister;
    private BigDecimal paidCapital;
    private String registrationId;
    private String signCondition;
    private BigDecimal totalShare;
    private Country registrationCountry;
    private List<CustomerInfoView> individualViewList;
    private Date documentIssueDate;
    private BigDecimal salesFromFinancialStmt;
    private BigDecimal shareHolderRatio;
    private String numberOfAuthorizedUsers;

    //*** Var for Address ***//
    private AddressView currentAddress;
    private AddressView workAddress;
    private AddressView registerAddress;

    //*** Var for Children ***//
    private List<ChildrenView> childrenList;

    private CustomerInfoView spouse;

    // for show in Summary
    private BigDecimal percentShareSummary;

    //for new field
    //age , customer entity
    private int ageMonths;
    private int isExistingSMECustomer;
    private Date lastReviewDate;
    private Date extendedReviewDate;
    private int extendedReviewDateFlag;
    private Date nextReviewDate;
    private int nextReviewDateFlag;
    private Date lastContractDate;
    private Date numberOfMonthsLastContractDate;
    private String adjustClass;
    private String ratingFinal;
    private int unpaidFeeInsurance;
    private int noPendingClaimLG;

    public CustomerInfoView(){
        //reset();
    }

    public void reset(){
        this.id = new Long(0);
        this.individualId = new Long(0);
        this.juristicId = new Long(0);
        this.searchBy = 0;
        this.searchId = "";
        this.documentType = new DocumentType();
        this.customerEntity = new CustomerEntity();
        this.relation = new Relation();
        this.reference = new Reference();
        this.citizenId = "";
        this.registrationId = "";
        this.documentAuthorizeBy = "";
        this.documentExpiredDate = new Date();
        this.tmbCustomerId = "";
        this.serviceSegment = "";
        this.collateralOwner = -1;
        this.percentShare = BigDecimal.ZERO;
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
        this.origin = new Race();
        this.nationality = new Nationality();
        this.sndNationality = new Nationality();
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
        this.approxIncome = BigDecimal.ZERO;
        this.mobileNumber = "";
        this.faxNumber = "";
        this.email = "";
        this.kycLevel = new KYCLevel();
        this.convenantFlag = -1;
        this.reviewFlag = -1;
        this.reason = "";
        this.spouse = new CustomerInfoView();
        this.businessType = new BusinessType();
        this.documentAuthorizeDate = new Date();
        this.customerCSIList = new ArrayList<CustomerCSIView>();
        this.sourceIncome = new Country();
        this.countryIncome = new Country();
        this.individualViewList = new ArrayList<CustomerInfoView>();
        this.percentShareSummary = BigDecimal.ZERO;
    }

    public long getIndividualId() {
        return individualId;
    }

    public void setIndividualId(long individualId) {
        this.individualId = individualId;
    }

    public long getJuristicId() {
        return juristicId;
    }

    public void setJuristicId(long juristicId) {
        this.juristicId = juristicId;
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

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public int getValidId() {
        return validId;
    }

    public void setValidId(int validId) {
        this.validId = validId;
    }

    public String getNcbResult() {
        return ncbResult;
    }

    public void setNcbResult(String ncbResult) {
        this.ncbResult = ncbResult;
    }

    public String getNcbReason() {
        return ncbReason;
    }

    public void setNcbReason(String ncbReason) {
        this.ncbReason = ncbReason;
    }

    public String getCsiResult() {
        return csiResult;
    }

    public void setCsiResult(String csiResult) {
        this.csiResult = csiResult;
    }

    public String getCsiReason() {
        return csiReason;
    }

    public void setCsiReason(String csiReason) {
        this.csiReason = csiReason;
    }

    public int getCsiFlag() {
        return csiFlag;
    }

    public void setCsiFlag(int csiFlag) {
        this.csiFlag = csiFlag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDocumentExpiredDate() {
        return documentExpiredDate;
    }

    public void setDocumentExpiredDate(Date documentExpiredDate) {
        this.documentExpiredDate = documentExpiredDate;
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

    public int getNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(int ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public int getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(int collateralOwner) {
        this.collateralOwner = collateralOwner;
    }

    public int getConvenantFlag() {
        return convenantFlag;
    }

    public void setConvenantFlag(int convenantFlag) {
        this.convenantFlag = convenantFlag;
    }

    public int getReviewFlag() {
        return reviewFlag;
    }

    public void setReviewFlag(int reviewFlag) {
        this.reviewFlag = reviewFlag;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
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

    public String getDocumentAuthorizeBy() {
        return documentAuthorizeBy;
    }

    public void setDocumentAuthorizeBy(String documentAuthorizeBy) {
        this.documentAuthorizeBy = documentAuthorizeBy;
    }

    public String getServiceSegment() {
        return serviceSegment;
    }

    public void setServiceSegment(String serviceSegment) {
        this.serviceSegment = serviceSegment;
    }

    public String getTmbCustomerId() {
        return tmbCustomerId;
    }

    public void setTmbCustomerId(String tmbCustomerId) {
        this.tmbCustomerId = tmbCustomerId;
    }

    public BigDecimal getPercentShare() {
        return percentShare;
    }

    public void setPercentShare(BigDecimal percentShare) {
        this.percentShare = percentShare;
    }

    public BigDecimal getApproxIncome() {
        return approxIncome;
    }

    public void setApproxIncome(BigDecimal approxIncome) {
        this.approxIncome = approxIncome;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getNumberOfChild() {
        return numberOfChild;
    }

    public void setNumberOfChild(int numberOfChild) {
        this.numberOfChild = numberOfChild;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Nationality getSndNationality() {
        return sndNationality;
    }

    public void setSndNationality(Nationality sndNationality) {
        this.sndNationality = sndNationality;
    }

    public Race getOrigin() {
        return origin;
    }

    public void setOrigin(Race origin) {
        this.origin = origin;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public Date getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(Date dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public BigDecimal getPaidCapital() {
        return paidCapital;
    }

    public void setPaidCapital(BigDecimal paidCapital) {
        this.paidCapital = paidCapital;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getSignCondition() {
        return signCondition;
    }

    public void setSignCondition(String signCondition) {
        this.signCondition = signCondition;
    }

    public BigDecimal getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(BigDecimal totalShare) {
        this.totalShare = totalShare;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public CustomerInfoView getSpouse() {
        return spouse;
    }

    public void setSpouse(CustomerInfoView spouse) {
        this.spouse = spouse;
    }

    public List<CustomerCSIView> getCustomerCSIList() {
        return customerCSIList;
    }

    public void setCustomerCSIList(List<CustomerCSIView> customerCSIList) {
        this.customerCSIList = customerCSIList;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setIsSpouse(int spouse) {
        this.isSpouse = spouse;
    }

    public int getIsSpouse(){
        return  isSpouse;
    }

    public int getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(int subIndex) {
        this.subIndex = subIndex;
    }

    public int getSearchFromRM() {
        return searchFromRM;
    }

    public void setSearchFromRM(int searchFromRM) {
        this.searchFromRM = searchFromRM;
    }
    public Date getDocumentAuthorizeDate() {
        return documentAuthorizeDate;
    }

    public void setDocumentAuthorizeDate(Date documentAuthorizeDate) {
        this.documentAuthorizeDate = documentAuthorizeDate;
    }

    public String getKycReason() {
        return kycReason;
    }

    public void setKycReason(String kycReason) {
        this.kycReason = kycReason;
    }

    public int getWorthiness() {
        return worthiness;
    }

    public void setWorthiness(int worthiness) {
        this.worthiness = worthiness;
    }

    public long getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(long spouseId) {
        this.spouseId = spouseId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public Country getSourceIncome() {
        return sourceIncome;
    }

    public void setSourceIncome(Country sourceIncome) {
        this.sourceIncome = sourceIncome;
    }

    public Country getCountryIncome() {
        return countryIncome;
    }

    public void setCountryIncome(Country countryIncome) {
        this.countryIncome = countryIncome;
    }

    public List<CustomerInfoView> getIndividualViewList() {
        return individualViewList;
    }

    public void setIndividualViewList(List<CustomerInfoView> individualViewList) {
        this.individualViewList = individualViewList;
    }

    public long getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(long committeeId) {
        this.committeeId = committeeId;
    }

    public Date getDocumentIssueDate() {
        return documentIssueDate;
    }

    public void setDocumentIssueDate(Date documentIssueDate) {
        this.documentIssueDate = documentIssueDate;
    }

    public BigDecimal getSalesFromFinancialStmt() {
        return salesFromFinancialStmt;
    }

    public void setSalesFromFinancialStmt(BigDecimal salesFromFinancialStmt) {
        this.salesFromFinancialStmt = salesFromFinancialStmt;
    }

    public BigDecimal getShareHolderRatio() {
        return shareHolderRatio;
    }

    public void setShareHolderRatio(BigDecimal shareHolderRatio) {
        this.shareHolderRatio = shareHolderRatio;
    }

    public String getNumberOfAuthorizedUsers() {
        return numberOfAuthorizedUsers;
    }

    public void setNumberOfAuthorizedUsers(String numberOfAuthorizedUsers) {
        this.numberOfAuthorizedUsers = numberOfAuthorizedUsers;
    }

    public void setIsCommittee(int committee) {
        this.isCommittee = committee;
    }

    public int getIsCommittee(){
        return isCommittee;
    }

    public BigDecimal getPercentShareSummary() {
        return percentShareSummary;
    }

    public void setPercentShareSummary(BigDecimal percentShareSummary) {
        this.percentShareSummary = percentShareSummary;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public int getIsExistingSMECustomer() {
        return isExistingSMECustomer;
    }

    public void setIsExistingSMECustomer(int existingSMECustomer) {
        isExistingSMECustomer = existingSMECustomer;
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

    public int getExtendedReviewDateFlag() {
        return extendedReviewDateFlag;
    }

    public void setExtendedReviewDateFlag(int extendedReviewDateFlag) {
        this.extendedReviewDateFlag = extendedReviewDateFlag;
    }

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public int getNextReviewDateFlag() {
        return nextReviewDateFlag;
    }

    public void setNextReviewDateFlag(int nextReviewDateFlag) {
        this.nextReviewDateFlag = nextReviewDateFlag;
    }

    public Date getLastContractDate() {
        return lastContractDate;
    }

    public void setLastContractDate(Date lastContractDate) {
        this.lastContractDate = lastContractDate;
    }

    public Date getNumberOfMonthsLastContractDate() {
        return numberOfMonthsLastContractDate;
    }

    public void setNumberOfMonthsLastContractDate(Date numberOfMonthsLastContractDate) {
        this.numberOfMonthsLastContractDate = numberOfMonthsLastContractDate;
    }

    public String getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(String adjustClass) {
        this.adjustClass = adjustClass;
    }

    public String getRatingFinal() {
        return ratingFinal;
    }

    public void setRatingFinal(String ratingFinal) {
        this.ratingFinal = ratingFinal;
    }

    public int getUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(int unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public int getNoPendingClaimLG() {
        return noPendingClaimLG;
    }

    public void setNoPendingClaimLG(int noPendingClaimLG) {
        this.noPendingClaimLG = noPendingClaimLG;
    }
}
