package com.clevel.selos.model.db.working;

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

    @Column(name = "financial_year")
    private int financialYear;

    @Column(name = "capital")
    private BigDecimal capital;

    @Column(name = "paid_capital")
    private BigDecimal paidCapital;

    @Column(name = "total_share")
    private BigDecimal totalShare;

    @Column(name = "sign_sondition")
    private String signCondition;

    @Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "document_issue_date")
    private Date documentIssueDate;

    @Column(name = "sales_financial_stmt")
    private BigDecimal salesFromFinancialStmt;

    @Column(name = "share_holder_ratio")
    private BigDecimal shareHolderRatio;

    @Column(name = "number_authorized_users")
    private String numberOfAuthorizedUsers;

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

    public Date getDocumentIssueDate() {
        return documentIssueDate;
    }

    public void setDocumentIssueDate(Date documentIssueDate) {
        this.documentIssueDate = documentIssueDate;
    }

    public BigDecimal getSalesFromFinancialStmt() {
        return salesFromFinancialStmt;
    }

    public void setSalesFromFinancialStmt(BigDecimal salesFromFinancialStmt) {
        this.salesFromFinancialStmt = salesFromFinancialStmt;
    }

    public BigDecimal getShareHolderRatio() {
        return shareHolderRatio;
    }

    public void setShareHolderRatio(BigDecimal shareHolderRatio) {
        this.shareHolderRatio = shareHolderRatio;
    }

    public String getNumberOfAuthorizedUsers() {
        return numberOfAuthorizedUsers;
    }

    public void setNumberOfAuthorizedUsers(String numberOfAuthorizedUsers) {
        this.numberOfAuthorizedUsers = numberOfAuthorizedUsers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customer", customer).
                append("registrationId", registrationId).
                append("registerDate", registerDate).
                append("financialYear", financialYear).
                append("capital", capital).
                append("paidCapital", paidCapital).
                append("totalShare", totalShare).
                append("signCondition", signCondition).
                append("documentIssueDate", documentIssueDate).
                append("salesFromFinancialStmt", salesFromFinancialStmt).
                append("shareHolderRatio", shareHolderRatio).
                append("numberOfAuthorizedUsers", numberOfAuthorizedUsers).
                toString();
    }
}
