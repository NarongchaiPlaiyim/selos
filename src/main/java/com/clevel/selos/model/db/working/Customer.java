package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="wrk_customer")
public class Customer implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CUSTOMER_ID", sequenceName="SEQ_WRK_CUSTOMER_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CUSTOMER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name="customerentity_id")
    private CustomerEntity customerEntity;

    @OneToOne
    @JoinColumn(name="documenttype_id")
    private DocumentType documentType;

    @Column(name="document_authorize_by")
    private String documentAuthorizeBy;

    @Column(name="service_segment")
    private String serviceSegment;

    @Column(name="collateral_owner")
    private int collateralOwner;

    @Column(name="percent_share")
    private BigDecimal percentShare;

    @Column(name="approx_income")
    private BigDecimal approxIncome;

    @Column(name="tmb_customer_id")
    private String tmbCustomerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="document_expire_date")
    private Date documentExpiredDate;

    @OneToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name="name_en")
    private String nameEn;

    @Column(name="name_th")
    private String nameTh;

    @Column(name="lastname_th")
    private String lastNameTh;

    @Column(name="lastname_en")
    private String lastNameEn;

    @Column(name="age")
    private int age;

    @Column(name="ncb_checked", nullable=false, columnDefinition="int default 0")
    private int ncbFlag;

    @Column(name = "csi_checked", nullable = false, columnDefinition = "int default 0")
    private int csiFlag;

    @OneToOne(mappedBy = "customer")
    private Individual individual;

    @OneToOne(mappedBy="customer")
    private Juristic juristic;

    @OneToMany(mappedBy="customer")
    private List<Address> addressesList;

    @OneToOne
    @JoinColumn(name="businesstype_id")
    private BusinessType businessType;

    @OneToOne
    @JoinColumn(name="relation_id")
    private Relation relation;

    @OneToOne
    @JoinColumn(name="reference_id")
    private Reference reference;

    @OneToOne(mappedBy="customer")
    private NCB ncb;

    @OneToMany(mappedBy = "customer")
    private List<CustomerCSI> customerCSIList;

    @Column(name="is_spouse", nullable=false, columnDefinition="int default 0")
    private int isSpouse;

    @Column(name="spouse_id")
    private long spouseId;

    @Column(name="is_search_rm", nullable=false, columnDefinition = "int default 0")
    private int searchFromRM;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="document_authorize_date")
    private Date documentAuthorizeDate;

    @Column(name="kyc_reason")
    private String kycReason;

    @Column(name="worthiness", nullable = false, columnDefinition = "int default -1")
    private int worthiness;

    @Column(name="mobile_number")
    private String mobileNumber;

    @Column(name="fax_number")
    private String faxNumber;

    @Column(name="email")
    private String email;

    @Column(name="convenant_flag", nullable = false, columnDefinition = "int default -1")
    private int convenantFlag;

    @Column(name="review_flag", nullable = false, columnDefinition = "int default -1")
    private int reviewFlag;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name="kyclevel_id")
    private KYCLevel kycLevel;

    @OneToOne
    @JoinColumn(name="mailing_address_id")
    private AddressType mailingAddressType;

    @Column(name="search_by", nullable = false, columnDefinition = "int default 0")
    private int searchBy;

    @Column(name="search_id")
    private String searchId;

    @OneToOne
    @JoinColumn(name="source_income")
    private Country sourceIncome;

    @OneToOne
    @JoinColumn(name="country_income")
    private Country countryIncome;

    @Column(name="is_committee", nullable=false, columnDefinition="int default 0")
    private int isCommittee;

    @Column(name="juristic_id")
    private long juristicId;

    @Column(name="age_months", nullable=false, columnDefinition="int default 0")
    private int ageMonths;

    @Column(name="is_existing_customer", nullable=false, columnDefinition="int default 0")
    private int isExistingSMECustomer;

    @Column(name="last_review_date")
    private Date lastReviewDate;

    @Column(name="extended_review_date")
    private Date extendedReviewDate;

    @Column(name="extended_review_date_flag", nullable=false, columnDefinition="int default 0")
    private int extendedReviewDateFlag;

    @Column(name="next_review_date")
    private Date nextReviewDate;

    @Column(name="next_review_date_flag", nullable=false, columnDefinition="int default 0")
    private int nextReviewDateFlag;

    @Column(name="last_contract_date")
    private Date lastContractDate;

    @Column(name="months_last_contract_date")
    private Date numberOfMonthsLastContractDate;

    @Column(name="adjust_class")
    private String adjustClass;

    @Column(name="rating_final")
    private String ratingFinal;

    @Column(name="unpaid_fee_insurance", nullable=false, columnDefinition="int default 0")
    private int unpaidFeeInsurance;

    @Column(name="no_pending_claim_LG", nullable=false, columnDefinition="int default 0")
    private int noPendingClaimLG;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_detail_id")
    private NewCollateralSubDetail newCollateralSubDetail;

    public Customer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
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

    public int getCollateralOwner() {
        return collateralOwner;
    }

    public void setCollateralOwner(int collateralOwner) {
        this.collateralOwner = collateralOwner;
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

    public String getTmbCustomerId() {
        return tmbCustomerId;
    }

    public void setTmbCustomerId(String idNumber) {
        this.tmbCustomerId = idNumber;
    }

    public Date getDocumentExpiredDate() {
        return documentExpiredDate;
    }

    public void setDocumentExpiredDate(Date expireDate) {
        this.documentExpiredDate = expireDate;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getLastNameTh() {
        return lastNameTh;
    }

    public void setLastNameTh(String lastNameTh) {
        this.lastNameTh = lastNameTh;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCsiFlag() {
        return csiFlag;
    }

    public void setCsiFlag(int csiFlag) {
        this.csiFlag = csiFlag;
    }

    public int getNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(int ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Juristic getJuristic() {
        return juristic;
    }

    public void setJuristic(Juristic juristic) {
        this.juristic = juristic;
    }

    public List<Address> getAddressesList() {
        return addressesList;
    }

    public void setAddressesList(List<Address> addressesList) {
        this.addressesList = addressesList;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
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

    public NCB getNcb() {
        return ncb;
    }

    public void setNcb(NCB ncb) {
        this.ncb = ncb;
    }

    public List<CustomerCSI> getCustomerCSIList() {
        return customerCSIList;
    }

    public void setCustomerCSIList(List<CustomerCSI> customerCSIList) {
        this.customerCSIList = customerCSIList;
    }

    public int getIsSpouse() {
        return isSpouse;
    }

    public void setIsSpouse(int spouse) {
        isSpouse = spouse;
    }

    public long getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(long spouseId) {
        this.spouseId = spouseId;
    }

    public int getSearchFromRM() {
        return searchFromRM;
    }

    public void setSearchFromRM(int searchFromRM) {
        this.searchFromRM = searchFromRM;
    }

    public int getSpouse() {
        return isSpouse;
    }

    public void setSpouse(int spouse) {
        isSpouse = spouse;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public KYCLevel getKycLevel() {
        return kycLevel;
    }

    public void setKycLevel(KYCLevel kycLevel) {
        this.kycLevel = kycLevel;
    }

    public AddressType getMailingAddressType() {
        return mailingAddressType;
    }

    public void setMailingAddressType(AddressType mailingAddressType) {
        this.mailingAddressType = mailingAddressType;
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

    public int getIsCommittee() {
        return isCommittee;
    }

    public void setIsCommittee(int committee) {
        isCommittee = committee;
    }

    public long getJuristicId() {
        return juristicId;
    }

    public void setJuristicId(long juristicId) {
        this.juristicId = juristicId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
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

    public NewCollateralSubDetail getNewCollateralSubDetail() {
        return newCollateralSubDetail;
    }

    public void setNewCollateralSubDetail(NewCollateralSubDetail newCollateralSubDetail) {
        this.newCollateralSubDetail = newCollateralSubDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                append("workCasePrescreen", workCasePrescreen).
                append("customerEntity", customerEntity).
                append("documentType", documentType).
                append("documentAuthorizeBy", documentAuthorizeBy).
                append("serviceSegment", serviceSegment).
                append("collateralOwner", collateralOwner).
                append("percentShare", percentShare).
                append("approxIncome", approxIncome).
                append("tmbCustomerId", tmbCustomerId).
                append("documentExpiredDate", documentExpiredDate).
                append("title", title).
                append("nameEn", nameEn).
                append("nameTh", nameTh).
                append("lastNameTh", lastNameTh).
                append("lastNameEn", lastNameEn).
                append("age", age).
                append("ncbFlag", ncbFlag).
                append("csiFlag", csiFlag).
                append("individual", individual).
                append("juristic", juristic).
                append("addressesList", addressesList).
                append("businessType", businessType).
                append("relation", relation).
                append("reference", reference).
                append("ncb", ncb).
                append("customerCSIList", customerCSIList).
                append("isSpouse", isSpouse).
                append("spouseId", spouseId).
                append("searchFromRM", searchFromRM).
                append("documentAuthorizeDate", documentAuthorizeDate).
                append("kycReason", kycReason).
                append("worthiness", worthiness).
                append("mobileNumber", mobileNumber).
                append("faxNumber", faxNumber).
                append("email", email).
                append("convenantFlag", convenantFlag).
                append("reviewFlag", reviewFlag).
                append("reason", reason).
                append("kycLevel", kycLevel).
                append("mailingAddressType", mailingAddressType).
                append("searchBy", searchBy).
                append("searchId", searchId).
                append("sourceIncome", sourceIncome).
                append("countryIncome", countryIncome).
                append("isCommittee", isCommittee).
                append("juristicId", juristicId).
                toString();
    }
}
