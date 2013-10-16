package com.clevel.selos.integration.rlos.appin.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CustomerDetail implements Serializable {
    private String citizenId;
    private String title;
    private String firstNameTh;
    private String lastNameTh;
    private String borrowerOrder;
    private String spouseCitizenId;
    private String spouseFirstNameTh;
    private String spouseLastNameTh;
    private String spouseCoApplicant;

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstNameTh() {
        return firstNameTh;
    }

    public void setFirstNameTh(String firstNameTh) {
        this.firstNameTh = firstNameTh;
    }

    public String getLastNameTh() {
        return lastNameTh;
    }

    public void setLastNameTh(String lastNameTh) {
        this.lastNameTh = lastNameTh;
    }

    public String getBorrowerOrder() {
        return borrowerOrder;
    }

    public void setBorrowerOrder(String borrowerOrder) {
        this.borrowerOrder = borrowerOrder;
    }

    public String getSpouseCitizenId() {
        return spouseCitizenId;
    }

    public void setSpouseCitizenId(String spouseCitizenId) {
        this.spouseCitizenId = spouseCitizenId;
    }

    public String getSpouseFirstNameTh() {
        return spouseFirstNameTh;
    }

    public void setSpouseFirstNameTh(String spouseFirstNameTh) {
        this.spouseFirstNameTh = spouseFirstNameTh;
    }

    public String getSpouseLastNameTh() {
        return spouseLastNameTh;
    }

    public void setSpouseLastNameTh(String spouseLastNameTh) {
        this.spouseLastNameTh = spouseLastNameTh;
    }

    public String getSpouseCoApplicant() {
        return spouseCoApplicant;
    }

    public void setSpouseCoApplicant(String spouseCoApplicant) {
        this.spouseCoApplicant = spouseCoApplicant;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("citizenId", citizenId)
                .append("title", title)
                .append("firstNameTh", firstNameTh)
                .append("lastNameTh", lastNameTh)
                .append("borrowerOrder", borrowerOrder)
                .append("spouseCitizenId", spouseCitizenId)
                .append("spouseFirstNameTh", spouseFirstNameTh)
                .append("spouseLastNameTh", spouseLastNameTh)
                .append("spouseCoApplicant", spouseCoApplicant)
                .toString();
    }
}
