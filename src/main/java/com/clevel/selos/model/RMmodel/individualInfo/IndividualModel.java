package com.clevel.selos.model.RMmodel.individualInfo;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.Nationality;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
    private String phoneNo;
    private String extension;
    private String dateOfBirth;
    private String gender;
    private String educationBackground;
    private String race;
    private String marriageStatus;
    private String nationality;
    private String numberOfChild;
    //spouse
    private Spouse spouse;

    //working information
    private String occupationCode;
    private String bizCode;
    //Contact Detail
    private ContactDetails homeAddress;
    private ContactDetails currentAddress;
    private ContactDetails workAddress;

    //personal list Section
    private List<IndividualPersonalList> personalLists;

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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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

    public List<IndividualPersonalList> getPersonalLists() {
        return personalLists;
    }

    public void setPersonalLists(List<IndividualPersonalList> personalLists) {
        this.personalLists = personalLists;
    }
}
