package com.clevel.selos.model.view;

import java.util.Date;

import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.Gender;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.util.Util;

public class CustomerInfoPostIndvView extends CustomerInfoPostBaseView<CustomerInfoPostIndvView> {
	private static final long serialVersionUID = -3021510855802034155L;

	private long individualId;
	
	private String lastNameTH;
	private Date birthDate;
	private Gender gender;
	private int raceId;
	private int nationalityId;
	
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
	
	private AttorneyRelationType attorneyRelationType;
	private RadioValue attorneyRequired;
	private long customerAttorneyId;
	
	private boolean hasSpouseData;
	
	private String displayRace;
	private String displayNationality;
	private String displayMaritalStatus;
	private String displaySpouseTitle;
	private String displayFatherTitle;
	private String displayMotherTitle;
	
	
	public CustomerInfoPostIndvView() {
		
	}

	public long getIndividualId() {
		return individualId;
	}

	public void setIndividualId(long individualId) {
		this.individualId = individualId;
	}

	public String getLastNameTH() {
		return lastNameTH;
	}

	public void setLastNameTH(String lastNameTH) {
		this.lastNameTH = lastNameTH;
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
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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
	
	public AttorneyRelationType getAttorneyRelationType() {
		if (attorneyRelationType == null)
			attorneyRelationType = AttorneyRelationType.OTHERS;
		return attorneyRelationType;
	}
	public void setAttorneyRelationType(AttorneyRelationType attorneyRelationType) {
		this.attorneyRelationType = attorneyRelationType;
	}
	public RadioValue getAttorneyRequired() {
		if (attorneyRequired == null)
			attorneyRequired = RadioValue.NO;
		return attorneyRequired;
	}
	public void setAttorneyRequired(RadioValue attorneyRequired) {
		this.attorneyRequired = attorneyRequired;
	}
	public long getCustomerAttorneyId() {
		return customerAttorneyId;
	}
	public void setCustomerAttorneyId(long customerAttorneyId) {
		this.customerAttorneyId = customerAttorneyId;
	}
	public boolean isHasSpouseData() {
		return hasSpouseData;
	}
	public void setHasSpouseData(boolean hasSpouseData) {
		this.hasSpouseData = hasSpouseData;
	}
	
	@Override
	protected void updateOwnValue(CustomerInfoPostIndvView view) {
		individualId = view.individualId;
		lastNameTH = view.lastNameTH;
		birthDate = view.birthDate;
		gender = view.gender;
		raceId = view.raceId;
		nationalityId = view.nationalityId;
		maritalStatusId = view.maritalStatusId;
		spouseTitleId = view.spouseTitleId;
		spouseNameTH = view.spouseNameTH;
		spouseLastNameTH = view.spouseLastNameTH;
		fatherTitleId = view.fatherTitleId;
		fatherNameTH = view.fatherNameTH;
		fatherLastNameTH = view.fatherLastNameTH;
		motherTitleId = view.motherTitleId;
		motherNameTH = view.motherNameTH;
		motherLastNameTH = view.motherLastNameTH;
		
		attorneyRequired = view.attorneyRequired;
		attorneyRelationType = view.attorneyRelationType;
		customerAttorneyId = view.customerAttorneyId;
		
		hasSpouseData = view.hasSpouseData;
		
		displayRace = view.displayRace;
		displayNationality = view.displayNationality;
		displayMaritalStatus = view.displayMaritalStatus;
		displaySpouseTitle = view.displaySpouseTitle;
		displayFatherTitle = view.displayFatherTitle;
		displayMotherTitle = view.displayMotherTitle;
	}
	@Override
	public void calculateAge() {
		if (birthDate == null)
			age = 0;
		else
			age = Util.calAge(birthDate);
	}
	@Override
	public int getDefaultCustomerEntityId() {
		return 1;
	}
}
