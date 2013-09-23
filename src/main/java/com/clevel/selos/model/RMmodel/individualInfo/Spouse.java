package com.clevel.selos.model.RMmodel.individualInfo;

import java.io.Serializable;


public class Spouse implements Serializable {

    private String firstname;
    private String lastname;
    private String citizenID;
    private String dateOfBirth;

    public Spouse(){

    }

    public Spouse(String firstname, String lastname, String citizenID, String dateOfBirth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.citizenID = citizenID;
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }



}
