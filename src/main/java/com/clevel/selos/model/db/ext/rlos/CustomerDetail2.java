package com.clevel.selos.model.db.ext.rlos;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ext_rlos_cus_detail2")
public class CustomerDetail2 implements Serializable {
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name="app_ref_number")
    private AppInProcess2 appInProcess2;

    @Column(name="citizen_id", length=25)
    private String citizenId;

    @Column(name="title", length=3)
    private String title;

    @Column(name="firstname_th", length=25)
    private String firstNameTh;

    @Column(name="lastname_th", length=30)
    private String lastNameTh;

    @Column(name="borrower_order", length=5)
    private String borrowerOrder;

    @Column(name="spouse_citizen_id", length=25)
    private String spouseCitizenId;

    @Column(name="spouse_firstname_th", length=25)
    private String spouseFirstNameTh;

    @Column(name="spouse_lastname_th", length=30)
    private String spouseLastNameTh;

    @Column(name="is_spouse_coapplicant", length=1)
    private String spouseCoApplicant;

    public CustomerDetail2() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppInProcess2 getAppInProcess2() {
        return appInProcess2;
    }

    public void setAppInProcess2(AppInProcess2 appInProcess2) {
        this.appInProcess2 = appInProcess2;
    }

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
}
