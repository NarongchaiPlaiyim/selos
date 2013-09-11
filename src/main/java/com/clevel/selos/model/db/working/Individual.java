package com.clevel.selos.model.db.working;

import com.clevel.selos.model.Gender;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.db.master.Occupation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_indivual")
public class Individual implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_INDIVIDUAL_ID", sequenceName="SEQ_WRK_INDIVIDUAL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_INDIVIDUAL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="birth_date")
    private Date birthDate;

    @Column(name="gender")
    private Gender gender;

    @OneToOne
    @JoinColumn(name="nationality_id")
    private Nationality nationality;

    @OneToOne
    @JoinColumn(name="education_id")
    private Education education;

    @OneToOne
    @JoinColumn(name="occupation_id")
    private Occupation occupation;

    @OneToOne
    @JoinColumn(name="maritalstatus_id")
    private MaritalStatus maritalStatus;

    @Column(name="num_of_children")
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customer", customer).
                append("birthDate", birthDate).
                append("gender", gender).
                append("nationality", nationality).
                append("education", education).
                append("occupation", occupation).
                append("maritalStatus", maritalStatus).
                append("numberOfChildren", numberOfChildren).
                toString();
    }
}
