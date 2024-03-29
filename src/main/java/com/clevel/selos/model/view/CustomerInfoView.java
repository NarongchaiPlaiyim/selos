package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.master.KYCLevelView;
import com.clevel.selos.model.view.master.SBFScoreView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private ServiceSegmentView serviceSegmentView;
    private String tmbCustomerId;
    private int collateralOwner;
    private BigDecimal percentShare;
    private BigDecimal approxIncome;
    private String mobileNumber;
    private String faxNumber;
    private String email;
    private KYCLevelView kycLevel;
    private int covenantFlag;
    private int reviewFlag;
    private String reason;
    private BusinessType businessType;
    private Date documentAuthorizeDate;
    private String kycReason;
    private int worthiness;
    private AddressType mailingAddressType;
    private long spouseId;
    private List<CustomerCSIView> customerCSIList;
    private IncomeSource sourceIncome;
    private CountryView countryIncome;
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
    private CountryView citizenCountry;

    //*** Var for Juristic ***//
    private BigDecimal capital;
    private int financialYear;
    private Date dateOfRegister;
    private BigDecimal paidCapital;
    private String registrationId;
    private String signCondition;
    private BigDecimal totalShare;
    private CountryView registrationCountry;
    private List<CustomerInfoView> individualViewList;
    private List<CustomerInfoView> individualViewForShowList; // 24-09-14
    private Date documentIssueDate;
    private BigDecimal salesFromFinancialStmt;
    private BigDecimal shareHolderRatio;
    private String numberOfAuthorizedUsers;
    private String contactName; // new 23-12-13
    private boolean signContract;
    private boolean needAttorney;

    //*** Var for Address ***//
    private AddressView currentAddress;
    private AddressView workAddress;
    private AddressView registerAddress;

    /*/*//*** Var for Children ***//*/
    private List<ChildrenView> childrenList;*/

    private CustomerInfoView spouse;

    // for show in Summary
    private String indLv; // new 11-12-13
    private String jurLv; // new 11-12-13

    //for new field
    //age , customer entity
    private long customerOblInfoID;
    private int existingSMECustomer;
    private Date lastReviewDate;
    private Date extendedReviewDate;
    private int extendedReviewDateFlag;
    private Date nextReviewDate;
    private int nextReviewDateFlag;
    private Date lastContractDate;
    private String adjustClass;
    private SBFScoreView ratingFinal;
    private BigDecimal unpaidFeeInsurance;
    private BigDecimal pendingClaimLG;
    private List<CustomerOblAccountInfoView> customerOblAccountInfoViewList;

    private BigDecimal shares;

    private List<Long> removeIndividualIdList;

    private BusinessDescription businessDescription;

    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

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
        this.serviceSegmentView = new ServiceSegmentView();
        this.collateralOwner = -1;
        this.percentShare = BigDecimal.ZERO;
        this.shares = BigDecimal.ZERO;
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
        //this.childrenList = new ArrayList<ChildrenView>();
        this.citizenCountry = new CountryView();
        this.registrationCountry = new CountryView();
        this.currentAddress = new AddressView();
        this.workAddress = new AddressView();
        this.registerAddress = new AddressView();
        this.mailingAddressType = new AddressType();
        this.approxIncome = BigDecimal.ZERO;
        this.mobileNumber = "";
        this.faxNumber = "";
        this.email = "";
        this.kycLevel = new KYCLevelView();
        this.covenantFlag = -1;
        this.reviewFlag = -1;
        this.reason = "";
        this.spouse = new CustomerInfoView();
        this.businessType = new BusinessType();
        this.businessDescription = new BusinessDescription();
        this.documentAuthorizeDate = new Date();
        this.customerCSIList = new ArrayList<CustomerCSIView>();
        this.sourceIncome = new IncomeSource();
        this.countryIncome = new CountryView();
        this.individualViewList = new ArrayList<CustomerInfoView>();
        this.individualViewForShowList = new ArrayList<CustomerInfoView>();
//        this.percentShareSummary = BigDecimal.ZERO;
        this.unpaidFeeInsurance = BigDecimal.ZERO;
        this.pendingClaimLG = BigDecimal.ZERO;
        this.customerOblInfoID = 0;
        this.removeIndividualIdList = new ArrayList<Long>();
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

    public int getCovenantFlag() {
        return covenantFlag;
    }

    public void setCovenantFlag(int covenantFlag) {
        this.covenantFlag = covenantFlag;
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

    public ServiceSegmentView getServiceSegmentView() {
        return serviceSegmentView;
    }

    public void setServiceSegmentView(ServiceSegmentView serviceSegmentView) {
        this.serviceSegmentView = serviceSegmentView;
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

    public CountryView getCountryIncome() {
        return countryIncome;
    }

    public void setCountryIncome(CountryView countryIncome) {
        this.countryIncome = countryIncome;
    }

    public CountryView getCitizenCountry() {
        return citizenCountry;
    }

    public void setCitizenCountry(CountryView citizenCountry) {
        this.citizenCountry = citizenCountry;
    }

    public CountryView getRegistrationCountry() {
        return registrationCountry;
    }

    public void setRegistrationCountry(CountryView registrationCountry) {
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

    public KYCLevelView getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(KYCLevelView kycLevel) {
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

    public IncomeSource getSourceIncome() {
        return sourceIncome;
    }

    public void setSourceIncome(IncomeSource sourceIncome) {
        this.sourceIncome = sourceIncome;
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

    public int getExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(int existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public BigDecimal getUnpaidFeeInsurance() {
        return unpaidFeeInsurance;
    }

    public void setUnpaidFeeInsurance(BigDecimal unpaidFeeInsurance) {
        this.unpaidFeeInsurance = unpaidFeeInsurance;
    }

    public BigDecimal getPendingClaimLG() {
        return pendingClaimLG;
    }

    public void setPendingClaimLG(BigDecimal pendingClaimLG) {
        this.pendingClaimLG = pendingClaimLG;
    }

    public List<CustomerOblAccountInfoView> getCustomerOblAccountInfoViewList() {
        return customerOblAccountInfoViewList;
    }

    public void setCustomerOblAccountInfoViewList(List<CustomerOblAccountInfoView> customerOblAccountInfoViewList) {
        this.customerOblAccountInfoViewList = customerOblAccountInfoViewList;
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

    public String getAdjustClass() {
        return adjustClass;
    }

    public void setAdjustClass(String adjustClass) {
        this.adjustClass = adjustClass;
    }

    public SBFScoreView getRatingFinal() {
        return ratingFinal;
    }

    public void setRatingFinal(SBFScoreView ratingFinal) {
        this.ratingFinal = ratingFinal;
    }

    public long getCustomerOblInfoID() {
        return customerOblInfoID;
    }

    public void setCustomerOblInfoID(long customerOblInfoID) {
        this.customerOblInfoID = customerOblInfoID;
    }

    public String getJurLv() {
        return jurLv;
    }

    public void setJurLv(String jurLv) {
        this.jurLv = jurLv;
    }

    public String getIndLv() {
        return indLv;
    }

    public void setIndLv(String indLv) {
        this.indLv = indLv;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public List<Long> getRemoveIndividualIdList() {
        return removeIndividualIdList;
    }

    public void setRemoveIndividualIdList(List<Long> removeIndividualIdList) {
        this.removeIndividualIdList = removeIndividualIdList;
    }
    
    public boolean isNeedAttorney() {
		return needAttorney;
	}
    public void setNeedAttorney(boolean needAttorney) {
		this.needAttorney = needAttorney;
	}
    public boolean isSignContract() {
		return signContract;
	}
    public void setSignContract(boolean signContract) {
		this.signContract = signContract;
	}

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
    }

    public List<CustomerInfoView> getIndividualViewForShowList() {
        return individualViewForShowList;
    }

    public void setIndividualViewForShowList(List<CustomerInfoView> individualViewForShowList) {
        this.individualViewForShowList = individualViewForShowList;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("individualId", individualId)
                .append("juristicId", juristicId)
                .append("listIndex", listIndex)
                .append("subIndex", subIndex)
                .append("listName", listName)
                .append("isSpouse", isSpouse)
                .append("searchBy", searchBy)
                .append("searchId", searchId)
                .append("searchFromRM", searchFromRM)
                .append("inputId", inputId)
                .append("validId", validId)
                .append("ncbResult", ncbResult)
                .append("ncbReason", ncbReason)
                .append("csiResult", csiResult)
                .append("csiReason", csiReason)
                .append("csiFlag", csiFlag)
                .append("id", id)
                .append("age", age)
                .append("documentExpiredDate", documentExpiredDate)
//                .append("titleTh", titleTh)
//                .append("titleEn", titleEn)
                .append("firstNameTh", firstNameTh)
                .append("lastNameTh", lastNameTh)
//                .append("firstNameEn", firstNameEn)
//                .append("lastNameEn", lastNameEn)
                .append("ncbFlag", ncbFlag)
                .append("customerEntity", customerEntity != null ? customerEntity.getId() : "customerEntity null")
                .append("documentType", documentType != null ? documentType.getId() : "documentType null")
                .append("relation", relation != null ? relation.getId() : "relation null")
                .append("reference", reference != null ? reference.getId() : "reference null")
//                .append("documentAuthorizeBy", documentAuthorizeBy)
//                .append("serviceSegment", serviceSegment)
                .append("tmbCustomerId", tmbCustomerId)
//                .append("collateralOwner", collateralOwner)
//                .append("percentShare", percentShare)
//                .append("approxIncome", approxIncome)
                .append("dateOfBirth", dateOfBirth)
                .append("citizenId", citizenId)
                .append("passportId", passportId)
                .append("gender", gender)
//                .append("numberOfChild", numberOfChild)
//                .append("education", education)
                .append("maritalStatus", maritalStatus != null ? maritalStatus.getId() : "maritalStatus null")
//                .append("nationality", nationality)
//                .append("sndNationality", sndNationality)
//                .append("origin", origin)
//                .append("occupation", occupation)
//                .append("capital", capital)
//                .append("financialYear", financialYear)
//                .append("dateOfRegister", dateOfRegister)
//                .append("paidCapital", paidCapital)
//                .append("registrationId", registrationId)
//                .append("signCondition", signCondition)
//                .append("totalShare", totalShare)
//                .append("currentAddress", currentAddress)
//                .append("workAddress", workAddress)
//                .append("registerAddress", registerAddress)
//                .append("mailingAddressType", mailingAddressType)
                //.append("childrenList", childrenList)
//                .append("citizenCountry", citizenCountry)
//                .append("registrationCountry", registrationCountry)
//                .append("mobileNumber", mobileNumber)
//                .append("faxNumber", faxNumber)
//                .append("email", email)
//                .append("kycLevel", kycLevel)
//                .append("convenantFlag", convenantFlag)
//                .append("reviewFlag", reviewFlag)
//                .append("reason", reason)
//                .append("businessType", businessType)
//                .append("spouse", spouse)
//                .append("documentAuthorizeDate", documentAuthorizeDate)
//                .append("kycReason", kycReason)
//                .append("worthiness", worthiness)
                .append("customerOblInfoID", customerOblInfoID)
                .toString();
    }
}
