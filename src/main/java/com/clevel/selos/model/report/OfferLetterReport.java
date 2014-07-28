package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class OfferLetterReport extends ReportModel{

    private String borrowName;
    private Date finalApproved;
    private BigDecimal totalLimit;
    private BigDecimal valueMLR;
    private BigDecimal valueMOR;
    private BigDecimal valueMRR;
    private String bdmName;
    private String tel;
    private String zone;

    private BigDecimal sumFeecalculation;
    private BigDecimal sumNonFeecalculation;

    private String teamName;
    private String telPhone;
    private String telFax;

    private String loanDate;
    private String loanTime;
    private String loanLocation;
    private String loanCustomerName;

    private String mortgageDate;
    private String mortgageTime;
    private String mortgageLocation;
    private String mortgageCustomerName;
    private String mortgageAuthorized;

    public OfferLetterReport() {
        borrowName = getDefaultString();
        finalApproved = getDefaultDate();
        totalLimit = getDefaultBigDecimal();
        valueMLR = getDefaultBigDecimal();
        valueMOR = getDefaultBigDecimal();
        valueMRR = getDefaultBigDecimal();
        bdmName = getDefaultString();
        tel = getDefaultString();
        zone = getDefaultString();
        sumFeecalculation = getDefaultBigDecimal();
        sumNonFeecalculation = getDefaultBigDecimal();
        teamName = getDefaultString();
        telPhone = getDefaultString();
        telFax = getDefaultString();
        loanDate = getDefaultString();
        loanTime = getDefaultString();
        loanLocation = getDefaultString();
        loanCustomerName = getDefaultString();
        mortgageDate = getDefaultString();
        mortgageTime = getDefaultString();
        mortgageLocation = getDefaultString();
        mortgageCustomerName = getDefaultString();
        mortgageAuthorized = getDefaultString();
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Date getFinalApproved() {
        return finalApproved;
    }

    public void setFinalApproved(Date finalApproved) {
        this.finalApproved = finalApproved;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getValueMLR() {
        return valueMLR;
    }

    public void setValueMLR(BigDecimal valueMLR) {
        this.valueMLR = valueMLR;
    }

    public BigDecimal getValueMOR() {
        return valueMOR;
    }

    public void setValueMOR(BigDecimal valueMOR) {
        this.valueMOR = valueMOR;
    }

    public BigDecimal getValueMRR() {
        return valueMRR;
    }

    public void setValueMRR(BigDecimal valueMRR) {
        this.valueMRR = valueMRR;
    }

    public String getBdmName() {
        return bdmName;
    }

    public void setBdmName(String bdmName) {
        this.bdmName = bdmName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public BigDecimal getSumFeecalculation() {
        return sumFeecalculation;
    }

    public void setSumFeecalculation(BigDecimal sumFeecalculation) {
        this.sumFeecalculation = sumFeecalculation;
    }

    public BigDecimal getSumNonFeecalculation() {
        return sumNonFeecalculation;
    }

    public void setSumNonFeecalculation(BigDecimal sumNonFeecalculation) {
        this.sumNonFeecalculation = sumNonFeecalculation;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getTelFax() {
        return telFax;
    }

    public void setTelFax(String telFax) {
        this.telFax = telFax;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getMortgageDate() {
        return mortgageDate;
    }

    public void setMortgageDate(String mortgageDate) {
        this.mortgageDate = mortgageDate;
    }

    public String getMortgageTime() {
        return mortgageTime;
    }

    public void setMortgageTime(String mortgageTime) {
        this.mortgageTime = mortgageTime;
    }

    public String getLoanLocation() {
        return loanLocation;
    }

    public void setLoanLocation(String loanLocation) {
        this.loanLocation = loanLocation;
    }

    public String getLoanCustomerName() {
        return loanCustomerName;
    }

    public void setLoanCustomerName(String loanCustomerName) {
        this.loanCustomerName = loanCustomerName;
    }

    public String getMortgageLocation() {
        return mortgageLocation;
    }

    public void setMortgageLocation(String mortgageLocation) {
        this.mortgageLocation = mortgageLocation;
    }

    public String getMortgageCustomerName() {
        return mortgageCustomerName;
    }

    public void setMortgageCustomerName(String mortgageCustomerName) {
        this.mortgageCustomerName = mortgageCustomerName;
    }

    public String getMortgageAuthorized() {
        return mortgageAuthorized;
    }

    public void setMortgageAuthorized(String mortgageAuthorized) {
        this.mortgageAuthorized = mortgageAuthorized;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("borrowName", borrowName)
                .append("finalApproved", finalApproved)
                .append("totalLimit", totalLimit)
                .append("valueMLR", valueMLR)
                .append("valueMOR", valueMOR)
                .append("valueMRR", valueMRR)
                .append("bdmName", bdmName)
                .append("tel", tel)
                .append("zone", zone)
                .append("sumFeecalculation", sumFeecalculation)
                .append("sumNonFeecalculation", sumNonFeecalculation)
                .append("teamName", teamName)
                .append("telPhone", telPhone)
                .append("telFax", telFax)
                .append("loanDate", loanDate)
                .append("loanTime", loanTime)
                .append("loanLocation", loanLocation)
                .append("loanCustomerName", loanCustomerName)
                .append("mortgageDate", mortgageDate)
                .append("mortgageTime", mortgageTime)
                .append("mortgageLocation", mortgageLocation)
                .append("mortgageCustomerName", mortgageCustomerName)
                .append("mortgageAuthorized", mortgageAuthorized)
                .toString();
    }
}
