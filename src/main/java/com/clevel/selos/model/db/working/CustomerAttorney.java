package com.clevel.selos.model.db.working;

import com.clevel.selos.model.AttorneyType;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "wrk_customer_attorney")
public class CustomerAttorney {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CUST_ATTORNEY_ID", sequenceName = "SEQ_WRK_CUST_ATTORNEY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CUST_ATTORNEY_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @Column(name = "attorney_type")
    @Enumerated(EnumType.ORDINAL)
    private AttorneyType attorneyType;
    

    @OneToOne
    @JoinColumn(name = "documenttype_id")
    private DocumentType documentType;

    @Column (name = "personal_id")
    private String personalID;

    @OneToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @Column(name="name_th")
    private String nameTh;

    @Column(name="lastname_th")
    private String lastNameTh;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name="age")
    private int age;

    @Column(name = "gender", nullable=false, columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "nationality_id")
    private Nationality nationality;

    @OneToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(name = "address_no", length = 20)
    private String addressNo;

    @Column(name = "moo", length = 20)
    private String moo;

    @Column(name = "building", length = 200)
    private String building;

    @Column(name = "road", length = 255)
    private String road;

    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToOne
    @JoinColumn(name = "subdistrict_id")
    private SubDistrict subDistrict;

    @Column(name = "postal_code", length = 5)
    private String postalCode;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_extension")
    private String extension;

    @OneToOne
    @JoinColumn(name = "maritalstatus_id")
    private MaritalStatus maritalStatus;

    @OneToOne
    @JoinColumn(name = "spouse_title_id")
    private Title spouseTitle;

    @Column(name="spouse_name_th", length = 50)
    private String spouseNameTh;

    @Column(name="spouse_lastname_th", length = 50)
    private String spouseLastNameTh;

    @OneToOne
    @JoinColumn(name = "father_title_id")
    private Title fatherTitle;

    @Column(name="father_name_th", length = 50)
    private String fatherNameTh;

    @Column(name="father_lastname_th", length = 50)
    private String fatherLastNameTh;

    @OneToOne
    @JoinColumn(name = "mother_title_id")
    private Title motherTitle;

    @Column(name="mother_name_th", length = 50)
    private String motherNameTh;

    @Column(name="mother_lastname_th", length = 50)
    private String motherLastNameTh;

    @Column(name="home_phone_number")
    private String homePhoneNumber;

    @Column(name="home_phone_extension")
    private String homePhoneExtension;

    @Column(name="mobile_1")
    private String mobile1;

    @Column(name="mobile_2")
    private String mobile2;
    
    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AttorneyType getAttorneyType() {
		return attorneyType;
	}
    
    public void setAttorneyType(AttorneyType attorneyType) {
		this.attorneyType = attorneyType;
	}
    
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
		return gender;
	}
    
    public void setGender(Gender gender) {
		this.gender = gender;
	}
    
    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getMoo() {
        return moo;
    }

    public void setMoo(String moo) {
        this.moo = moo;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Title getSpouseTitle() {
        return spouseTitle;
    }

    public void setSpouseTitle(Title spouseTitle) {
        this.spouseTitle = spouseTitle;
    }

    public String getSpouseNameTh() {
        return spouseNameTh;
    }

    public void setSpouseNameTh(String spouseNameTh) {
        this.spouseNameTh = spouseNameTh;
    }

    public String getSpouseLastNameTh() {
        return spouseLastNameTh;
    }

    public void setSpouseLastNameTh(String spouseLastNameTh) {
        this.spouseLastNameTh = spouseLastNameTh;
    }

    public Title getFatherTitle() {
        return fatherTitle;
    }

    public void setFatherTitle(Title fatherTitle) {
        this.fatherTitle = fatherTitle;
    }

    public String getFatherNameTh() {
        return fatherNameTh;
    }

    public void setFatherNameTh(String fatherNameTh) {
        this.fatherNameTh = fatherNameTh;
    }

    public String getFatherLastNameTh() {
        return fatherLastNameTh;
    }

    public void setFatherLastNameTh(String fatherLastNameTh) {
        this.fatherLastNameTh = fatherLastNameTh;
    }

    public Title getMotherTitle() {
        return motherTitle;
    }

    public void setMotherTitle(Title motherTitle) {
        this.motherTitle = motherTitle;
    }

    public String getMotherNameTh() {
        return motherNameTh;
    }

    public void setMotherNameTh(String motherNameTh) {
        this.motherNameTh = motherNameTh;
    }

    public String getMotherLastNameTh() {
        return motherLastNameTh;
    }

    public void setMotherLastNameTh(String motherLastNameTh) {
        this.motherLastNameTh = motherLastNameTh;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getHomePhoneExtension() {
        return homePhoneExtension;
    }

    public void setHomePhoneExtension(String homePhoneExtension) {
        this.homePhoneExtension = homePhoneExtension;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }
    public WorkCase getWorkCase() {
		return workCase;
	}
    public void setWorkCase(WorkCase workCase) {
		this.workCase = workCase;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("attorneyType", attorneyType)
                .append("documentType", documentType)
                .append("personalID", personalID)
                .append("title", title)
                .append("nameTh", nameTh)
                .append("lastNameTh", lastNameTh)
                .append("birthDate", birthDate)
                .append("age", age)
                .append("gender", gender)
                .append("nationality", nationality)
                .append("race", race)
                .append("addressNo", addressNo)
                .append("moo", moo)
                .append("building", building)
                .append("road", road)
                .append("province", province)
                .append("district", district)
                .append("subDistrict", subDistrict)
                .append("postalCode", postalCode)
                .append("country", country)
                .append("phoneNumber", phoneNumber)
                .append("extension", extension)
                .append("maritalStatus", maritalStatus)
                .append("spouseTitle", spouseTitle)
                .append("spouseNameTh", spouseNameTh)
                .append("spouseLastNameTh", spouseLastNameTh)
                .append("fatherTitle", fatherTitle)
                .append("fatherNameTh", fatherNameTh)
                .append("fatherLastNameTh", fatherLastNameTh)
                .append("motherTitle", motherTitle)
                .append("motherNameTh", motherNameTh)
                .append("motherLastNameTh", motherLastNameTh)
                .append("homePhoneNumber", homePhoneNumber)
                .append("homePhoneExtension", homePhoneExtension)
                .append("mobile1", mobile1)
                .append("mobile2", mobile2)
                .toString();
    }
}
