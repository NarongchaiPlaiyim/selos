package com.clevel.selos.model.db.working;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_individual")
public class Individual implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_INDIVIDUAL_ID", sequenceName = "SEQ_WRK_INDIVIDUAL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_INDIVIDUAL_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "citizen_id")
    private String citizenId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender")
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "nationality_id")
    private Nationality nationality;

    @OneToOne
    @JoinColumn(name = "snd_nationality_id")
    private Nationality sndNationality;

    @OneToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @OneToOne
    @JoinColumn(name = "education_id")
    private Education education;

    @OneToOne
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @OneToOne
    @JoinColumn(name = "maritalstatus_id")
    private MaritalStatus maritalStatus;

    @Column(name = "num_of_children")
    private int numberOfChildren;

    public Individual() {
    }

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

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public Nationality getSndNationality() {
        return sndNationality;
    }

    public void setSndNationality(Nationality sndNationality) {
        this.sndNationality = sndNationality;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("citizenId", citizenId)
                .append("birthDate", birthDate)
                .append("gender", gender)
                .append("nationality", nationality)
                .append("sndNationality", sndNationality)
                .append("race", race)
                .append("education", education)
                .append("occupation", occupation)
                .append("maritalStatus", maritalStatus)
                .append("numberOfChildren", numberOfChildren)
                .toString();
    }
}
