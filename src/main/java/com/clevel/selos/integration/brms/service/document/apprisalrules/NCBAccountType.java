
package com.clevel.selos.integration.brms.service.document.apprisalrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for NCBAccountType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NCBAccountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memberShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownershipIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOpened" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="dateClosed" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="lastPaymentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="asOfDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="creditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="outstandingBalance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="amountPastDue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="defaultDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="installmentFrequency" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="installmentAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="installmentNumPayments" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="paymentHistory1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentHistory2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentHistoryStartDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="paymentHistoryEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="loanObjective" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="percentPayment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="creditCardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numCoBorrowers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="creditType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overdueMonths" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tdrFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ncbAccountStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfLastDebtRestructure" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="accountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delinquencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentPattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overdueCountInLast6Months" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue31dTo60dLast6MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue61dLast12MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdueOver90dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue31dTo60dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue61dTo90dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue91dTo120dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue121dTo150dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue151dTo180dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue181dTo210dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue211dTo240dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue241dTo270dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue271dTo300dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdueOver301dCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overLimitLast6MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overLimitLast12MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="undefinedDelqCodeLast6MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="undefinedDelqCodeLast12MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ncbRecord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NCBAccountType", propOrder = {
    "id",
    "memberShortName",
    "ownershipIndicator",
    "accountNumber",
    "dateOpened",
    "dateClosed",
    "lastPaymentDate",
    "asOfDate",
    "creditLimit",
    "outstandingBalance",
    "amountPastDue",
    "defaultDate",
    "installmentFrequency",
    "installmentAmount",
    "installmentNumPayments",
    "paymentHistory1",
    "paymentHistory2",
    "paymentHistoryStartDate",
    "paymentHistoryEndDate",
    "loanObjective",
    "percentPayment",
    "creditCardType",
    "numCoBorrowers",
    "creditType",
    "overdueMonths",
    "tdrFlag",
    "ncbAccountStatus",
    "dateOfLastDebtRestructure",
    "accountType",
    "delinquencyCode",
    "paymentPattern",
    "overdueCountInLast6Months",
    "overdue31DTo60DLast6MthsCount",
    "overdue61DLast12MthsCount",
    "overdueOver90DCount",
    "overdue31DTo60DCount",
    "overdue61DTo90DCount",
    "overdue91DTo120DCount",
    "overdue121DTo150DCount",
    "overdue151DTo180DCount",
    "overdue181DTo210DCount",
    "overdue211DTo240DCount",
    "overdue241DTo270DCount",
    "overdue271DTo300DCount",
    "overdueOver301DCount",
    "overLimitLast6MthsCount",
    "overLimitLast12MthsCount",
    "undefinedDelqCodeLast6MthsCount",
    "undefinedDelqCodeLast12MthsCount",
    "ncbRecord",
    "attribute"
})
public class NCBAccountType {

    @XmlElement(name = "ID")
    protected String id;
    protected String memberShortName;
    protected String ownershipIndicator;
    protected String accountNumber;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOpened;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateClosed;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lastPaymentDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar asOfDate;
    protected BigDecimal creditLimit;
    protected BigDecimal outstandingBalance;
    protected BigDecimal amountPastDue;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar defaultDate;
    protected Integer installmentFrequency;
    protected BigDecimal installmentAmount;
    protected Integer installmentNumPayments;
    protected String paymentHistory1;
    protected String paymentHistory2;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar paymentHistoryStartDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar paymentHistoryEndDate;
    protected Integer loanObjective;
    protected BigDecimal percentPayment;
    protected String creditCardType;
    protected Integer numCoBorrowers;
    protected String creditType;
    protected String overdueMonths;
    protected Boolean tdrFlag;
    protected String ncbAccountStatus;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfLastDebtRestructure;
    protected String accountType;
    protected String delinquencyCode;
    protected String paymentPattern;
    protected Integer overdueCountInLast6Months;
    @XmlElement(name = "overdue31dTo60dLast6MthsCount")
    protected Integer overdue31DTo60DLast6MthsCount;
    @XmlElement(name = "overdue61dLast12MthsCount")
    protected Integer overdue61DLast12MthsCount;
    @XmlElement(name = "overdueOver90dCount")
    protected Integer overdueOver90DCount;
    @XmlElement(name = "overdue31dTo60dCount")
    protected Integer overdue31DTo60DCount;
    @XmlElement(name = "overdue61dTo90dCount")
    protected Integer overdue61DTo90DCount;
    @XmlElement(name = "overdue91dTo120dCount")
    protected Integer overdue91DTo120DCount;
    @XmlElement(name = "overdue121dTo150dCount")
    protected Integer overdue121DTo150DCount;
    @XmlElement(name = "overdue151dTo180dCount")
    protected Integer overdue151DTo180DCount;
    @XmlElement(name = "overdue181dTo210dCount")
    protected Integer overdue181DTo210DCount;
    @XmlElement(name = "overdue211dTo240dCount")
    protected Integer overdue211DTo240DCount;
    @XmlElement(name = "overdue241dTo270dCount")
    protected Integer overdue241DTo270DCount;
    @XmlElement(name = "overdue271dTo300dCount")
    protected Integer overdue271DTo300DCount;
    @XmlElement(name = "overdueOver301dCount")
    protected Integer overdueOver301DCount;
    protected Integer overLimitLast6MthsCount;
    protected Integer overLimitLast12MthsCount;
    protected Integer undefinedDelqCodeLast6MthsCount;
    protected Integer undefinedDelqCodeLast12MthsCount;
    protected String ncbRecord;
    protected List<AttributeType> attribute;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the memberShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberShortName() {
        return memberShortName;
    }

    /**
     * Sets the value of the memberShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberShortName(String value) {
        this.memberShortName = value;
    }

    /**
     * Gets the value of the ownershipIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnershipIndicator() {
        return ownershipIndicator;
    }

    /**
     * Sets the value of the ownershipIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnershipIndicator(String value) {
        this.ownershipIndicator = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the dateOpened property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOpened() {
        return dateOpened;
    }

    /**
     * Sets the value of the dateOpened property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOpened(XMLGregorianCalendar value) {
        this.dateOpened = value;
    }

    /**
     * Gets the value of the dateClosed property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateClosed() {
        return dateClosed;
    }

    /**
     * Sets the value of the dateClosed property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateClosed(XMLGregorianCalendar value) {
        this.dateClosed = value;
    }

    /**
     * Gets the value of the lastPaymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastPaymentDate() {
        return lastPaymentDate;
    }

    /**
     * Sets the value of the lastPaymentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastPaymentDate(XMLGregorianCalendar value) {
        this.lastPaymentDate = value;
    }

    /**
     * Gets the value of the asOfDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAsOfDate() {
        return asOfDate;
    }

    /**
     * Sets the value of the asOfDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAsOfDate(XMLGregorianCalendar value) {
        this.asOfDate = value;
    }

    /**
     * Gets the value of the creditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the value of the creditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCreditLimit(BigDecimal value) {
        this.creditLimit = value;
    }

    /**
     * Gets the value of the outstandingBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    /**
     * Sets the value of the outstandingBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOutstandingBalance(BigDecimal value) {
        this.outstandingBalance = value;
    }

    /**
     * Gets the value of the amountPastDue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountPastDue() {
        return amountPastDue;
    }

    /**
     * Sets the value of the amountPastDue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountPastDue(BigDecimal value) {
        this.amountPastDue = value;
    }

    /**
     * Gets the value of the defaultDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDefaultDate() {
        return defaultDate;
    }

    /**
     * Sets the value of the defaultDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDefaultDate(XMLGregorianCalendar value) {
        this.defaultDate = value;
    }

    /**
     * Gets the value of the installmentFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentFrequency() {
        return installmentFrequency;
    }

    /**
     * Sets the value of the installmentFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentFrequency(Integer value) {
        this.installmentFrequency = value;
    }

    /**
     * Gets the value of the installmentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    /**
     * Sets the value of the installmentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInstallmentAmount(BigDecimal value) {
        this.installmentAmount = value;
    }

    /**
     * Gets the value of the installmentNumPayments property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentNumPayments() {
        return installmentNumPayments;
    }

    /**
     * Sets the value of the installmentNumPayments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentNumPayments(Integer value) {
        this.installmentNumPayments = value;
    }

    /**
     * Gets the value of the paymentHistory1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentHistory1() {
        return paymentHistory1;
    }

    /**
     * Sets the value of the paymentHistory1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentHistory1(String value) {
        this.paymentHistory1 = value;
    }

    /**
     * Gets the value of the paymentHistory2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentHistory2() {
        return paymentHistory2;
    }

    /**
     * Sets the value of the paymentHistory2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentHistory2(String value) {
        this.paymentHistory2 = value;
    }

    /**
     * Gets the value of the paymentHistoryStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaymentHistoryStartDate() {
        return paymentHistoryStartDate;
    }

    /**
     * Sets the value of the paymentHistoryStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaymentHistoryStartDate(XMLGregorianCalendar value) {
        this.paymentHistoryStartDate = value;
    }

    /**
     * Gets the value of the paymentHistoryEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPaymentHistoryEndDate() {
        return paymentHistoryEndDate;
    }

    /**
     * Sets the value of the paymentHistoryEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPaymentHistoryEndDate(XMLGregorianCalendar value) {
        this.paymentHistoryEndDate = value;
    }

    /**
     * Gets the value of the loanObjective property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLoanObjective() {
        return loanObjective;
    }

    /**
     * Sets the value of the loanObjective property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLoanObjective(Integer value) {
        this.loanObjective = value;
    }

    /**
     * Gets the value of the percentPayment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentPayment() {
        return percentPayment;
    }

    /**
     * Sets the value of the percentPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentPayment(BigDecimal value) {
        this.percentPayment = value;
    }

    /**
     * Gets the value of the creditCardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditCardType() {
        return creditCardType;
    }

    /**
     * Sets the value of the creditCardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditCardType(String value) {
        this.creditCardType = value;
    }

    /**
     * Gets the value of the numCoBorrowers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumCoBorrowers() {
        return numCoBorrowers;
    }

    /**
     * Sets the value of the numCoBorrowers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumCoBorrowers(Integer value) {
        this.numCoBorrowers = value;
    }

    /**
     * Gets the value of the creditType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditType() {
        return creditType;
    }

    /**
     * Sets the value of the creditType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditType(String value) {
        this.creditType = value;
    }

    /**
     * Gets the value of the overdueMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverdueMonths() {
        return overdueMonths;
    }

    /**
     * Sets the value of the overdueMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverdueMonths(String value) {
        this.overdueMonths = value;
    }

    /**
     * Gets the value of the tdrFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTdrFlag() {
        return tdrFlag;
    }

    /**
     * Sets the value of the tdrFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTdrFlag(Boolean value) {
        this.tdrFlag = value;
    }

    /**
     * Gets the value of the ncbAccountStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNcbAccountStatus() {
        return ncbAccountStatus;
    }

    /**
     * Sets the value of the ncbAccountStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNcbAccountStatus(String value) {
        this.ncbAccountStatus = value;
    }

    /**
     * Gets the value of the dateOfLastDebtRestructure property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfLastDebtRestructure() {
        return dateOfLastDebtRestructure;
    }

    /**
     * Sets the value of the dateOfLastDebtRestructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfLastDebtRestructure(XMLGregorianCalendar value) {
        this.dateOfLastDebtRestructure = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the delinquencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelinquencyCode() {
        return delinquencyCode;
    }

    /**
     * Sets the value of the delinquencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelinquencyCode(String value) {
        this.delinquencyCode = value;
    }

    /**
     * Gets the value of the paymentPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentPattern() {
        return paymentPattern;
    }

    /**
     * Sets the value of the paymentPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentPattern(String value) {
        this.paymentPattern = value;
    }

    /**
     * Gets the value of the overdueCountInLast6Months property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdueCountInLast6Months() {
        return overdueCountInLast6Months;
    }

    /**
     * Sets the value of the overdueCountInLast6Months property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdueCountInLast6Months(Integer value) {
        this.overdueCountInLast6Months = value;
    }

    /**
     * Gets the value of the overdue31DTo60DLast6MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue31DTo60DLast6MthsCount() {
        return overdue31DTo60DLast6MthsCount;
    }

    /**
     * Sets the value of the overdue31DTo60DLast6MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue31DTo60DLast6MthsCount(Integer value) {
        this.overdue31DTo60DLast6MthsCount = value;
    }

    /**
     * Gets the value of the overdue61DLast12MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue61DLast12MthsCount() {
        return overdue61DLast12MthsCount;
    }

    /**
     * Sets the value of the overdue61DLast12MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue61DLast12MthsCount(Integer value) {
        this.overdue61DLast12MthsCount = value;
    }

    /**
     * Gets the value of the overdueOver90DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdueOver90DCount() {
        return overdueOver90DCount;
    }

    /**
     * Sets the value of the overdueOver90DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdueOver90DCount(Integer value) {
        this.overdueOver90DCount = value;
    }

    /**
     * Gets the value of the overdue31DTo60DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue31DTo60DCount() {
        return overdue31DTo60DCount;
    }

    /**
     * Sets the value of the overdue31DTo60DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue31DTo60DCount(Integer value) {
        this.overdue31DTo60DCount = value;
    }

    /**
     * Gets the value of the overdue61DTo90DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue61DTo90DCount() {
        return overdue61DTo90DCount;
    }

    /**
     * Sets the value of the overdue61DTo90DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue61DTo90DCount(Integer value) {
        this.overdue61DTo90DCount = value;
    }

    /**
     * Gets the value of the overdue91DTo120DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue91DTo120DCount() {
        return overdue91DTo120DCount;
    }

    /**
     * Sets the value of the overdue91DTo120DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue91DTo120DCount(Integer value) {
        this.overdue91DTo120DCount = value;
    }

    /**
     * Gets the value of the overdue121DTo150DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue121DTo150DCount() {
        return overdue121DTo150DCount;
    }

    /**
     * Sets the value of the overdue121DTo150DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue121DTo150DCount(Integer value) {
        this.overdue121DTo150DCount = value;
    }

    /**
     * Gets the value of the overdue151DTo180DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue151DTo180DCount() {
        return overdue151DTo180DCount;
    }

    /**
     * Sets the value of the overdue151DTo180DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue151DTo180DCount(Integer value) {
        this.overdue151DTo180DCount = value;
    }

    /**
     * Gets the value of the overdue181DTo210DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue181DTo210DCount() {
        return overdue181DTo210DCount;
    }

    /**
     * Sets the value of the overdue181DTo210DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue181DTo210DCount(Integer value) {
        this.overdue181DTo210DCount = value;
    }

    /**
     * Gets the value of the overdue211DTo240DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue211DTo240DCount() {
        return overdue211DTo240DCount;
    }

    /**
     * Sets the value of the overdue211DTo240DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue211DTo240DCount(Integer value) {
        this.overdue211DTo240DCount = value;
    }

    /**
     * Gets the value of the overdue241DTo270DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue241DTo270DCount() {
        return overdue241DTo270DCount;
    }

    /**
     * Sets the value of the overdue241DTo270DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue241DTo270DCount(Integer value) {
        this.overdue241DTo270DCount = value;
    }

    /**
     * Gets the value of the overdue271DTo300DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue271DTo300DCount() {
        return overdue271DTo300DCount;
    }

    /**
     * Sets the value of the overdue271DTo300DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue271DTo300DCount(Integer value) {
        this.overdue271DTo300DCount = value;
    }

    /**
     * Gets the value of the overdueOver301DCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdueOver301DCount() {
        return overdueOver301DCount;
    }

    /**
     * Sets the value of the overdueOver301DCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdueOver301DCount(Integer value) {
        this.overdueOver301DCount = value;
    }

    /**
     * Gets the value of the overLimitLast6MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverLimitLast6MthsCount() {
        return overLimitLast6MthsCount;
    }

    /**
     * Sets the value of the overLimitLast6MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverLimitLast6MthsCount(Integer value) {
        this.overLimitLast6MthsCount = value;
    }

    /**
     * Gets the value of the overLimitLast12MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverLimitLast12MthsCount() {
        return overLimitLast12MthsCount;
    }

    /**
     * Sets the value of the overLimitLast12MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverLimitLast12MthsCount(Integer value) {
        this.overLimitLast12MthsCount = value;
    }

    /**
     * Gets the value of the undefinedDelqCodeLast6MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUndefinedDelqCodeLast6MthsCount() {
        return undefinedDelqCodeLast6MthsCount;
    }

    /**
     * Sets the value of the undefinedDelqCodeLast6MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUndefinedDelqCodeLast6MthsCount(Integer value) {
        this.undefinedDelqCodeLast6MthsCount = value;
    }

    /**
     * Gets the value of the undefinedDelqCodeLast12MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUndefinedDelqCodeLast12MthsCount() {
        return undefinedDelqCodeLast12MthsCount;
    }

    /**
     * Sets the value of the undefinedDelqCodeLast12MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUndefinedDelqCodeLast12MthsCount(Integer value) {
        this.undefinedDelqCodeLast12MthsCount = value;
    }

    /**
     * Gets the value of the ncbRecord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNcbRecord() {
        return ncbRecord;
    }

    /**
     * Sets the value of the ncbRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNcbRecord(String value) {
        this.ncbRecord = value;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     * 
     * 
     */
    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeType>();
        }
        return this.attribute;
    }

}
