package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.Gender;
import com.clevel.selos.util.Util;

public class CustomerAttorneyView implements Serializable {
	private static final long serialVersionUID = 3741911726626140767L;
	private long id;
	private long customerId;
	
	private int documentTypeId;
	private String personalId;
	private int age;
	private int titleId;
	private String nameTH;
	private String lastNameTH;
	private Date birthDate;
	
	private Gender gender;
	private int raceId;
	private int nationalityId;
	
	//Address
	private String addressNo;
	private String moo;
	private String building;
	private String road;
	private int provinceId;
	private int districtId;
	private int subDistrictId;
	private String postalCode;
	private int countryId;
	private String phoneNumber;
	private String phoneExt;
	
	//
	private int maritalStatusId;
	private int spouseTitleId;
	private String spouseNameTH;
	private String spouseLastNameTH;
	private int fatherTitleId;
	private String fatherNameTH;
	private String fatherLastNameTH;
	private int motherTitleId;
	private String motherNameTH;
	private String motherLastNameTH;
	
	private String homePhoneNumber;
	private String homePhoneExt;
	private String mobile1;
	private String mobile2;
	
	//Display
	private String displayDocumentType;
	private String displayTitle;
	
	private String displayRace;
	private String displayNationality;
	private String displayMaritalStatus;
	private String displaySpouseTitle;
	private String displayFatherTitle;
	private String displayMotherTitle;
	
	private String displayProvince;
	private String displayDistrict;
	private String displaySubDistrict;
	private String displayCountry;
	
	private boolean juristic;
	
	public CustomerAttorneyView() {
	}
	
	public void calculateAge() {
		if (birthDate == null)
			age = 0;
		else
			age = Util.calAge(birthDate);
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
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

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public String getNameTH() {
		return nameTH;
	}

	public void setNameTH(String nameTH) {
		this.nameTH = nameTH;
	}

	public String getLastNameTH() {
		return lastNameTH;
	}

	public void setLastNameTH(String lastNameTH) {
		this.lastNameTH = lastNameTH;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		if (gender == null)
			gender = Gender.NA;
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public int getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(int nationalityId) {
		this.nationalityId = nationalityId;
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

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getSubDistrictId() {
		return subDistrictId;
	}

	public void setSubDistrictId(int subDistrictId) {
		this.subDistrictId = subDistrictId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneExt() {
		return phoneExt;
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	public int getMaritalStatusId() {
		return maritalStatusId;
	}

	public void setMaritalStatusId(int maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

	public int getSpouseTitleId() {
		return spouseTitleId;
	}

	public void setSpouseTitleId(int spouseTitleId) {
		this.spouseTitleId = spouseTitleId;
	}

	public String getSpouseNameTH() {
		return spouseNameTH;
	}

	public void setSpouseNameTH(String spouseNameTH) {
		this.spouseNameTH = spouseNameTH;
	}

	public String getSpouseLastNameTH() {
		return spouseLastNameTH;
	}

	public void setSpouseLastNameTH(String spouseLastNameTH) {
		this.spouseLastNameTH = spouseLastNameTH;
	}

	public int getFatherTitleId() {
		return fatherTitleId;
	}

	public void setFatherTitleId(int fatherTitleId) {
		this.fatherTitleId = fatherTitleId;
	}

	public String getFatherNameTH() {
		return fatherNameTH;
	}

	public void setFatherNameTH(String fatherNameTH) {
		this.fatherNameTH = fatherNameTH;
	}

	public String getFatherLastNameTH() {
		return fatherLastNameTH;
	}

	public void setFatherLastNameTH(String fatherLastNameTH) {
		this.fatherLastNameTH = fatherLastNameTH;
	}

	public int getMotherTitleId() {
		return motherTitleId;
	}

	public void setMotherTitleId(int motherTitleId) {
		this.motherTitleId = motherTitleId;
	}

	public String getMotherNameTH() {
		return motherNameTH;
	}

	public void setMotherNameTH(String motherNameTH) {
		this.motherNameTH = motherNameTH;
	}

	public String getMotherLastNameTH() {
		return motherLastNameTH;
	}

	public void setMotherLastNameTH(String motherLastNameTH) {
		this.motherLastNameTH = motherLastNameTH;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	public String getHomePhoneExt() {
		return homePhoneExt;
	}

	public void setHomePhoneExt(String homePhoneExt) {
		this.homePhoneExt = homePhoneExt;
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

	public String getDisplayDocumentType() {
		return displayDocumentType;
	}

	public void setDisplayDocumentType(String displayDocumentType) {
		this.displayDocumentType = displayDocumentType;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public String getDisplayRace() {
		return displayRace;
	}

	public void setDisplayRace(String displayRace) {
		this.displayRace = displayRace;
	}

	public String getDisplayNationality() {
		return displayNationality;
	}

	public void setDisplayNationality(String displayNationality) {
		this.displayNationality = displayNationality;
	}

	public String getDisplayMaritalStatus() {
		return displayMaritalStatus;
	}

	public void setDisplayMaritalStatus(String displayMaritalStatus) {
		this.displayMaritalStatus = displayMaritalStatus;
	}

	public String getDisplaySpouseTitle() {
		return displaySpouseTitle;
	}

	public void setDisplaySpouseTitle(String displaySpouseTitle) {
		this.displaySpouseTitle = displaySpouseTitle;
	}

	public String getDisplayFatherTitle() {
		return displayFatherTitle;
	}

	public void setDisplayFatherTitle(String displayFatherTitle) {
		this.displayFatherTitle = displayFatherTitle;
	}

	public String getDisplayMotherTitle() {
		return displayMotherTitle;
	}

	public void setDisplayMotherTitle(String displayMotherTitle) {
		this.displayMotherTitle = displayMotherTitle;
	}

	public String getDisplayProvince() {
		return displayProvince;
	}

	public void setDisplayProvince(String displayProvince) {
		this.displayProvince = displayProvince;
	}

	public String getDisplayDistrict() {
		return displayDistrict;
	}

	public void setDisplayDistrict(String displayDistrict) {
		this.displayDistrict = displayDistrict;
	}

	public String getDisplaySubDistrict() {
		return displaySubDistrict;
	}

	public void setDisplaySubDistrict(String displaySubDistrict) {
		this.displaySubDistrict = displaySubDistrict;
	}

	public String getDisplayCountry() {
		return displayCountry;
	}

	public void setDisplayCountry(String displayCountry) {
		this.displayCountry = displayCountry;
	}
	
	public boolean isJuristic() {
		return juristic;
	}
	
	public void setJuristic(boolean juristic) {
		this.juristic = juristic;
	}
}
