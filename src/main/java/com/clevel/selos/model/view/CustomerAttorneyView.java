package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.Gender;

public class CustomerAttorneyView implements Serializable {
	private static final long serialVersionUID = 3741911726626140767L;
	private long customerId;
	private int documentTypeId;
	private String personalId;
	private int titleId;
	private String nameTh;
	private String lastNameTh;
	private Date birthDate;
	private int age;
	private Gender gender;
	private int raceId;
	private int nationalityId;
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
	private int maritalStatusId;
	private int spouseTitleId;
	private String spouseNameTh;
	private String spouseLastNameTh;
	private int fatherTitleId;
	private String fatherNameTh;
	private String fatherLastNameTh;
	private int motherTitleId;
	private String motherNameTh;
	private String motherLastNameTh;
	private String homePhoneNumber;
	private String homePhoneExt;
	private String mobile1;
	private String mobile2;
	
	//Permission Set
	private boolean canUpdateGeneralData;
	private boolean canUpdateAddress;
	private boolean canUpdateRelationInfo;
	private boolean canUpdateOthers;
	
	public CustomerAttorneyView() {
		gender = Gender.NA;
		canUpdateGeneralData = true;
		canUpdateAddress = true;
		canUpdateRelationInfo = true;
		canUpdateOthers = true;
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

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
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

	public int getFatherTitleId() {
		return fatherTitleId;
	}

	public void setFatherTitleId(int fatherTitleId) {
		this.fatherTitleId = fatherTitleId;
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

	public int getMotherTitleId() {
		return motherTitleId;
	}

	public void setMotherTitleId(int motherTitleId) {
		this.motherTitleId = motherTitleId;
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

	public boolean isCanUpdateGeneralData() {
		return canUpdateGeneralData;
	}

	public void setCanUpdateGeneralData(boolean canUpdateGeneralData) {
		this.canUpdateGeneralData = canUpdateGeneralData;
	}

	public boolean isCanUpdateAddress() {
		return canUpdateAddress;
	}

	public void setCanUpdateAddress(boolean canUpdateAddress) {
		this.canUpdateAddress = canUpdateAddress;
	}

	public boolean isCanUpdateRelationInfo() {
		return canUpdateRelationInfo;
	}

	public void setCanUpdateRelationInfo(boolean canUpdateRelationInfo) {
		this.canUpdateRelationInfo = canUpdateRelationInfo;
	}

	public boolean isCanUpdateOthers() {
		return canUpdateOthers;
	}

	public void setCanUpdateOthers(boolean canUpdateOthers) {
		this.canUpdateOthers = canUpdateOthers;
	}
	
	
}
