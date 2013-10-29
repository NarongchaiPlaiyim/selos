package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Country;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_juristic")
public class Juristic implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_JURISTIC_ID", sequenceName = "SEQ_WRK_JURISTIC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_JURISTIC_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "registration_id")
    private String registrationId;

    @Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "register_date")
    private Date registerDate;

    @OneToOne
    @JoinColumn(name = "register_country")
    private Country registerCountry;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Individual owner;

    @Column(name = "financial_year")
    private int financialYear;

    @Column(name = "capital")
    private BigDecimal capital;

    @Column(name = "paidCapital")
    private BigDecimal paidCapital;

    @Column(name = "totalShare")
    private BigDecimal totalShare;

    @Column(name = "signCondition")
    private String signCondition;

    public Juristic() {
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

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Country getRegisterCountry() {
        return registerCountry;
    }

    public void setRegisterCountry(Country registerCountry) {
        this.registerCountry = registerCountry;
    }

    public Individual getOwner() {
        return owner;
    }

    public void setOwner(Individual owner) {
        this.owner = owner;
    }

    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getPaidCapital() {
        return paidCapital;
    }

    public void setPaidCapital(BigDecimal paidCapital) {
        this.paidCapital = paidCapital;
    }

    public BigDecimal getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(BigDecimal totalShare) {
        this.totalShare = totalShare;
    }

    public String getSignCondition() {
        return signCondition;
    }

    public void setSignCondition(String signCondition) {
        this.signCondition = signCondition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("registrationId", registrationId)
                .append("registerDate", registerDate)
                .append("registerCountry", registerCountry)
                .append("owner", owner)
                .append("financialYear", financialYear)
                .append("capital", capital)
                .append("paidCapital", paidCapital)
                .append("totalShare", totalShare)
                .append("signCondition", signCondition)
                .toString();
    }
}
