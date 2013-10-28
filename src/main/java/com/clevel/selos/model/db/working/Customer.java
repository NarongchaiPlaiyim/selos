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
    @JoinColumn(name="title_th_id")
    private Title titleTh;

    @OneToOne
    @JoinColumn(name="title_en_id")
    private Title titleEn;

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

    @OneToOne(mappedBy="customer")
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

    @OneToOne
    @JoinColumn(name="warningcode_id")
    private WarningCode csi;

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

    @Column(name="worthiness")
    private int worthiness;

    @Column(name="mobile_number")
    private String mobileNumber;

    @Column(name="fax_number")
    private String faxNumber;

    @Column(name="email")
    private String email;

    @Column(name="convenant_flag")
    private int convenantFlag;

    @Column(name="review_flag")
    private int reviewFlag;

    @Column(name="reason")
    private String reason;

    @OneToOne
    @JoinColumn(name="kyclevel_id")
    private KYCLevel kycLevel;

    @OneToOne
    @JoinColumn(name="mailing_address_id")
    private AddressType mailingAddressType;

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

    public Title getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(Title title) {
        this.titleTh = title;
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

    public WarningCode getCsi() {
        return csi;
    }

    public void setCsi(WarningCode csi) {
        this.csi = csi;
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

    public Title getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(Title titleEn) {
        this.titleEn = titleEn;
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
                append("idNumber", tmbCustomerId).
                append("expireDate", documentExpiredDate).
                append("title", titleTh).
                append("nameEn", nameEn).
                append("nameTh", nameTh).
                append("lastNameTh", lastNameTh).
                append("lastNameEn", lastNameEn).
                append("age", age).
                append("ncbFlag", ncbFlag).
                append("individual", individual).
                append("juristic", juristic).
                append("addressesList", addressesList).
                append("businessType", businessType).
                append("relation", relation).
                append("reference", reference).
                append("ncb", ncb).
                append("csi", csi).
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
                append("businessType", businessType).
                append("mailingAddressType", mailingAddressType).
                toString();
    }
}
