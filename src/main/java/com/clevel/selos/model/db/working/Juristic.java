package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BusinessActivity;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;
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
    @SequenceGenerator(name="SEQ_WRK_JURISTIC_ID", sequenceName="SEQ_WRK_JURISTIC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_JURISTIC_ID")
    private long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="founded")
    private Date founded;
    @OneToOne
    @JoinColumn(name="owner_id")
    private Individual owner;

    @Column(name="financial_year")
    private int financialYear;

    @OneToOne
    @JoinColumn(name="businesstype_id")
    private BusinessType businessType;
    @OneToOne
    @JoinColumn(name="businessgroup_id")
    private BusinessGroup businessGroup;
    @OneToOne
    @JoinColumn(name="businessdescription_id")
    private BusinessDescription businessDescription;
    @OneToOne
    @JoinColumn(name="businessactivity_id")
    private BusinessActivity businessActivity;

    @Column(name="capital")
    private BigDecimal capital;
    @Column(name="paidCapital")
    private BigDecimal paidCapital;
    @Column(name="totalShare")
    private BigDecimal totalShare;
    @Column(name="signCondition")
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

    public Date getFounded() {
        return founded;
    }

    public void setFounded(Date founded) {
        this.founded = founded;
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

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;
    }

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
    }

    public BusinessActivity getBusinessActivity() {
        return businessActivity;
    }

    public void setBusinessActivity(BusinessActivity businessActivity) {
        this.businessActivity = businessActivity;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customer", customer).
                append("founded", founded).
                append("owner", owner).
                append("financialYear", financialYear).
                append("businessType", businessType).
                append("businessGroup", businessGroup).
                append("businessDescription", businessDescription).
                append("businessActivity", businessActivity).
                append("capital", capital).
                append("paidCapital", paidCapital).
                append("totalShare", totalShare).
                append("signCondition", signCondition).
                toString();
    }
}
