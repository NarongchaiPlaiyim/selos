package com.clevel.selos.integration.corebanking.model.individualInfo;

import java.io.Serializable;


public class IndividualModel implements Serializable {
    private String reqId;
    private String resCode;
    private String resDesc;
    private String searchResult;
    private String lastPageFlag;

    //personal Detail Section

    private String tmbCusID;
    private String titleTH;
    private String firstname;
    private String lastname;
    private String documentType;
    private String citizenID;
    private String documentExpiredDate;
    private String cusType;
    private String dateOfBirth;
    private String gender;
    private String educationBackground;
    private String race;
    private String marriageStatus;
    private String nationality;
    private String numberOfChild;


    private Telephone telephoneNumber1;
    private Telephone telephoneNumber2;
    private Telephone telephoneNumber3;
    private Telephone telephoneNumber4;

    private String titleEN;
    private String firstnameEN;
    private String lastnameEN;

    //spouse
    private Spouse spouse;

    //working information
    private String occupationCode;
    private String bizCode;
    //Contact Detail
    private ContactDetails homeAddress;
    private ContactDetails currentAddress;
    private ContactDetails workAddress;

    public String getTmbCusID() {
        return tmbCusID;
    }

    public void setTmbCusID(String tmbCusID) {
        this.tmbCusID = tmbCusID;
    }

    public String getTitleTH() {
        return titleTH;
    }

    public void setTitleTH(String titleTH) {
        this.titleTH = titleTH;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getDocumentExpiredDate() {
        return documentExpiredDate;
    }

    public void setDocumentExpiredDate(String documentExpiredDate) {
        this.documentExpiredDate = documentExpiredDate;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNumberOfChild() {
        return numberOfChild;
    }

    public void setNumberOfChild(String numberOfChild) {
        this.numberOfChild = numberOfChild;
    }

    public Spouse getSpouse() {
        return spouse;
    }

    public void setSpouse(Spouse spouse) {
        this.spouse = spouse;
    }

    public String getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(String occupationCode) {
        this.occupationCode = occupationCode;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public ContactDetails getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(ContactDetails homeAddress) {
        this.homeAddress = homeAddress;
    }

    public ContactDetails getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(ContactDetails currentAddress) {
        this.currentAddress = currentAddress;
    }

    public ContactDetails getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(ContactDetails workAddress) {
        this.workAddress = workAddress;
    }
    ////////////////


    public String getLastnameEN() {
        return lastnameEN;
    }

    public void setLastnameEN(String lastnameEN) {
        this.lastnameEN = lastnameEN;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getFirstnameEN() {
        return firstnameEN;
    }

    public void setFirstnameEN(String firstnameEN) {
        this.firstnameEN = firstnameEN;
    }

    public Telephone getTelephoneNumber1() {
        return telephoneNumber1;
    }

    public void setTelephoneNumber1(Telephone telephoneNumber1) {
        this.telephoneNumber1 = telephoneNumber1;
    }

    public Telephone getTelephoneNumber2() {
        return telephoneNumber2;
    }

    public void setTelephoneNumber2(Telephone telephoneNumber2) {
        this.telephoneNumber2 = telephoneNumber2;
    }

    public Telephone getTelephoneNumber3() {
        return telephoneNumber3;
    }

    public void setTelephoneNumber3(Telephone telephoneNumber3) {
        this.telephoneNumber3 = telephoneNumber3;
    }

    public Telephone getTelephoneNumber4() {
        return telephoneNumber4;
    }

    public void setTelephoneNumber4(Telephone telephoneNumber4) {
        this.telephoneNumber4 = telephoneNumber4;
    }
}
