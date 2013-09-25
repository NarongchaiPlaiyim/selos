package com.clevel.selos.integration.ncrs.ncrsmodel;


import java.io.Serializable;

public class NCRSInputModel implements Serializable {
    private String firstName;
    private String lastName;
    private String citizenId;
    private String idType;

    public NCRSInputModel() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public void setIdTypePassport() {
        this.idType = IdType.PASSPORT.value();
    }

    public void setIdTypeCitizen() {
        this.idType = IdType.CITIZEN.value();
    }

    public String getIdType() {
        return idType;
    }
}
