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
@Table(name = "wrk_customer")
public class Customer implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CUSTOMER_ID", sequenceName = "SEQ_WRK_CUSTOMER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CUSTOMER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name = "customerentity_id")
    private CustomerEntity customerEntity;

    @OneToOne
    @JoinColumn(name = "documenttype_id")
    private DocumentType documentType;

    @Column(name = "document_authorize_by")
    private String documentAuthorizeBy;

    @Column(name = "service_segment")
    private String serviceSegment;

    @Column(name = "collateral_owner")
    private int collateralOwner;

    @Column(name = "percent_share")
    private BigDecimal percentShare;

    @Column(name = "approx_income")
    private BigDecimal approxIncome;

    @Column(name = "id_number")
    private String idNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_date")
    private Date expireDate;

    @OneToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_th")
    private String nameTh;

    @Column(name = "lastname_th")
    private String lastNameTh;

    @Column(name = "lastname_en")
    private String lastNameEn;

    @Column(name = "age")
    private int age;

    @Column(name = "ncb_checked", nullable = false, columnDefinition = "int default 0")
    private int ncbFlag;

    @Column(name = "csi_checked", nullable = false, columnDefinition = "int default 0")
    private int csiFlag;

    @OneToOne(mappedBy = "customer")
    private Individual individual;

    @OneToOne(mappedBy = "customer")
    private Juristic juristic;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressesList;

    @OneToOne
    @JoinColumn(name = "businesstype_id")
    private BusinessType businessType;

    @OneToOne
    @JoinColumn(name = "relation_id")
    private Relation relation;

    @OneToOne
    @JoinColumn(name = "reference_id")
    private Reference reference;

    @OneToOne(mappedBy = "customer")
    private NCB ncb;

    @OneToOne
    @JoinColumn(name = "warningcode_id")
    private WarningCode csi;

    @Column(name = "is_spouse", nullable = false, columnDefinition = "int default 0")
    private int isSpouse;

    @Column(name = "spouse_id")
    private long spouseId;

    @Column(name = "is_search_rm", nullable = false, columnDefinition = "int default 0")
    private int searchFromRM;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("customerEntity", customerEntity)
                .append("documentType", documentType)
                .append("documentAuthorizeBy", documentAuthorizeBy)
                .append("serviceSegment", serviceSegment)
                .append("collateralOwner", collateralOwner)
                .append("percentShare", percentShare)
                .append("approxIncome", approxIncome)
                .append("idNumber", idNumber)
                .append("expireDate", expireDate)
                .append("title", title)
                .append("nameEn", nameEn)
                .append("nameTh", nameTh)
                .append("lastNameTh", lastNameTh)
                .append("lastNameEn", lastNameEn)
                .append("age", age)
                .append("ncbFlag", ncbFlag)
                .append("individual", individual)
                .append("juristic", juristic)
                .append("addressesList", addressesList)
                .append("businessType", businessType)
                .append("relation", relation)
                .append("reference", reference)
                .append("ncb", ncb)
                .append("csi", csi)
                .append("isSpouse", isSpouse)
                .append("spouseId", spouseId)
                .toString();
    }
}
