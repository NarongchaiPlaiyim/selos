package com.clevel.selos.model.db.working;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.db.master.Race;
import com.clevel.selos.model.db.master.Title;

@Entity
@Table(name = "wrk_individual")
public class Individual implements Serializable {
    private static final long serialVersionUID = -8290797178282007797L;

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

    @Column(name = "gender", nullable=false, columnDefinition="int default 0")
    private int gender;

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

    @OneToOne
    @JoinColumn(name="citizen_country_id")
    private Country citizenCountry;
    
    @ManyToOne
    @JoinColumn(name="father_title_id")
    private Title fatherTitle;
    
    @Column(name="father_name_th")
    private String fatherNameTh;
    
    @Column(name="father_lastname_th")
    private String fatherLastNameTh;
    
    @ManyToOne
    @JoinColumn(name="mother_title_id")
    private Title motherTitle;
    
    @Column(name="mother_name_th")
    private String motherNameTh;
    
    @Column(name="mother_lastname_th")
    private String motherLastNameTh;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="customer_attorney_id")
    private CustomerAttorney customerAttorney;
    

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
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

    public Country getCitizenCountry() {
        return citizenCountry;
    }

    public void setCitizenCountry(Country citizenCountry) {
        this.citizenCountry = citizenCountry;
    }
    
    public CustomerAttorney getCustomerAttorney() {
		return customerAttorney;
	}
    public void setCustomerAttorney(CustomerAttorney customerAttorney) {
		this.customerAttorney = customerAttorney;
	}
    public String getFatherLastNameTh() {
		return fatherLastNameTh;
	}
    public void setFatherLastNameTh(String fatherLastNameTh) {
		this.fatherLastNameTh = fatherLastNameTh;
	}
    public String getFatherNameTh() {
		return fatherNameTh;
	}
    public void setFatherNameTh(String fatherNameTh) {
		this.fatherNameTh = fatherNameTh;
	}
    public Title getFatherTitle() {
		return fatherTitle;
	}
    public void setFatherTitle(Title fatherTitle) {
		this.fatherTitle = fatherTitle;
	}
    public String getMotherLastNameTh() {
		return motherLastNameTh;
	}
    public void setMotherLastNameTh(String motherLastNameTh) {
		this.motherLastNameTh = motherLastNameTh;
	}
    public String getMotherNameTh() {
		return motherNameTh;
	}
    public void setMotherNameTh(String motherNameTh) {
		this.motherNameTh = motherNameTh;
	}
    public Title getMotherTitle() {
		return motherTitle;
	}
    public void setMotherTitle(Title motherTitle) {
		this.motherTitle = motherTitle;
	}
    

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customer", customer).
                append("citizenId", citizenId).
                append("birthDate", birthDate).
                append("gender", gender).
                append("nationality", nationality).
                append("sndNationality", sndNationality).
                append("race", race).
                append("education", education).
                append("occupation", occupation).
                append("maritalStatus", maritalStatus).
                append("numberOfChildren", numberOfChildren).
                append("citizenCountry", citizenCountry).
                append("customerAttorney", customerAttorney).
                append("fatherTitle", fatherTitle).
                append("fatherNameTh", fatherNameTh).
                append("fatherLastNameTh", fatherLastNameTh).
                append("motherTitle", motherTitle).
                append("motherNameTh", motherNameTh).
                append("motherLastNameTh", motherLastNameTh).
                
                toString();
    }
}
