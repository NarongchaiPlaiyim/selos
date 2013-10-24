
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

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
 * <p>Java class for AccountType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lendingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subProductType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="closedDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monthsOnBook" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="loanOutstanding" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="currMinPayment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="lastPaymentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="paidAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="creditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="outstandingBalance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="availableBalance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="tenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="monthlyRepaymentAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numMthsSinceLastPayment" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxDelinq_7to12" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxConsecMthsDelinq_1to12" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxNumMthsConsecDelinqChange_7to12" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="numMthsSinceDelinq_1to12" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="avgBalance_6to8" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="avgBalance_7to12" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="avgBalalnce_9to11" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="avgBalPercentCreditLimit_7to12" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="avgBalPercentCreditLimit_9to11_6to8" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="currBalPercentCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="delinquencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tdrFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="overdueCountInLast6Months" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue30dLast6MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue60dLast12MthsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="overdue90dFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="riskModel" type="{http://www.tmbbank.com/enterprise/model}RiskModelType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="delinquency" type="{http://www.tmbbank.com/enterprise/model}DelinquencyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountType", propOrder = {
    "id",
    "accountNumber",
    "accountType",
    "lendingType",
    "productType",
    "subProductType",
    "openDate",
    "closedDate",
    "status",
    "monthsOnBook",
    "loanOutstanding",
    "currMinPayment",
    "lastPaymentDate",
    "paidAmount",
    "creditLimit",
    "outstandingBalance",
    "availableBalance",
    "tenor",
    "monthlyRepaymentAmt",
    "numMthsSinceLastPayment",
    "maxDelinq7To12",
    "maxConsecMthsDelinq1To12",
    "maxNumMthsConsecDelinqChange7To12",
    "numMthsSinceDelinq1To12",
    "avgBalance6To8",
    "avgBalance7To12",
    "avgBalalnce9To11",
    "avgBalPercentCreditLimit7To12",
    "avgBalPercentCreditLimit9To116To8",
    "currBalPercentCreditLimit",
    "delinquencyCode",
    "tdrFlag",
    "overdueCountInLast6Months",
    "overdue30DLast6MthsCount",
    "overdue60DLast12MthsCount",
    "overdue90DFlag",
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
    "attribute",
    "score",
    "riskModel",
    "delinquency"
})
public class AccountType {

    @XmlElement(name = "ID")
    protected String id;
    protected String accountNumber;
    protected String accountType;
    protected String lendingType;
    protected String productType;
    protected String subProductType;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar openDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar closedDate;
    protected String status;
    protected Integer monthsOnBook;
    protected BigDecimal loanOutstanding;
    protected BigDecimal currMinPayment;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lastPaymentDate;
    protected BigDecimal paidAmount;
    protected BigDecimal creditLimit;
    protected BigDecimal outstandingBalance;
    protected BigDecimal availableBalance;
    protected Integer tenor;
    protected BigDecimal monthlyRepaymentAmt;
    protected Integer numMthsSinceLastPayment;
    @XmlElement(name = "maxDelinq_7to12")
    protected Integer maxDelinq7To12;
    @XmlElement(name = "maxConsecMthsDelinq_1to12")
    protected Integer maxConsecMthsDelinq1To12;
    @XmlElement(name = "maxNumMthsConsecDelinqChange_7to12")
    protected Integer maxNumMthsConsecDelinqChange7To12;
    @XmlElement(name = "numMthsSinceDelinq_1to12")
    protected Integer numMthsSinceDelinq1To12;
    @XmlElement(name = "avgBalance_6to8")
    protected BigDecimal avgBalance6To8;
    @XmlElement(name = "avgBalance_7to12")
    protected BigDecimal avgBalance7To12;
    @XmlElement(name = "avgBalalnce_9to11")
    protected BigDecimal avgBalalnce9To11;
    @XmlElement(name = "avgBalPercentCreditLimit_7to12")
    protected BigDecimal avgBalPercentCreditLimit7To12;
    @XmlElement(name = "avgBalPercentCreditLimit_9to11_6to8")
    protected BigDecimal avgBalPercentCreditLimit9To116To8;
    protected BigDecimal currBalPercentCreditLimit;
    protected String delinquencyCode;
    protected Boolean tdrFlag;
    protected Integer overdueCountInLast6Months;
    @XmlElement(name = "overdue30dLast6MthsCount")
    protected Integer overdue30DLast6MthsCount;
    @XmlElement(name = "overdue60dLast12MthsCount")
    protected Integer overdue60DLast12MthsCount;
    @XmlElement(name = "overdue90dFlag")
    protected Boolean overdue90DFlag;
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
    protected List<AttributeType> attribute;
    protected List<ScoreType> score;
    protected List<RiskModelType> riskModel;
    protected DelinquencyType delinquency;

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
     * Gets the value of the lendingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLendingType() {
        return lendingType;
    }

    /**
     * Sets the value of the lendingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLendingType(String value) {
        this.lendingType = value;
    }

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the subProductType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubProductType() {
        return subProductType;
    }

    /**
     * Sets the value of the subProductType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubProductType(String value) {
        this.subProductType = value;
    }

    /**
     * Gets the value of the openDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOpenDate() {
        return openDate;
    }

    /**
     * Sets the value of the openDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setOpenDate(XMLGregorianCalendar value) {
        this.openDate = value;
    }

    /**
     * Gets the value of the closedDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getClosedDate() {
        return closedDate;
    }

    /**
     * Sets the value of the closedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setClosedDate(XMLGregorianCalendar value) {
        this.closedDate = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the monthsOnBook property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMonthsOnBook() {
        return monthsOnBook;
    }

    /**
     * Sets the value of the monthsOnBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMonthsOnBook(Integer value) {
        this.monthsOnBook = value;
    }

    /**
     * Gets the value of the loanOutstanding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getLoanOutstanding() {
        return loanOutstanding;
    }

    /**
     * Sets the value of the loanOutstanding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setLoanOutstanding(BigDecimal value) {
        this.loanOutstanding = value;
    }

    /**
     * Gets the value of the currMinPayment property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getCurrMinPayment() {
        return currMinPayment;
    }

    /**
     * Sets the value of the currMinPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setCurrMinPayment(BigDecimal value) {
        this.currMinPayment = value;
    }

    /**
     * Gets the value of the lastPaymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
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
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastPaymentDate(XMLGregorianCalendar value) {
        this.lastPaymentDate = value;
    }

    /**
     * Gets the value of the paidAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    /**
     * Sets the value of the paidAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setPaidAmount(BigDecimal value) {
        this.paidAmount = value;
    }

    /**
     * Gets the value of the creditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
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
     *     {@link java.math.BigDecimal }
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
     *     {@link java.math.BigDecimal }
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
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setOutstandingBalance(BigDecimal value) {
        this.outstandingBalance = value;
    }

    /**
     * Gets the value of the availableBalance property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Sets the value of the availableBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvailableBalance(BigDecimal value) {
        this.availableBalance = value;
    }

    /**
     * Gets the value of the tenor property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTenor() {
        return tenor;
    }

    /**
     * Sets the value of the tenor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTenor(Integer value) {
        this.tenor = value;
    }

    /**
     * Gets the value of the monthlyRepaymentAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getMonthlyRepaymentAmt() {
        return monthlyRepaymentAmt;
    }

    /**
     * Sets the value of the monthlyRepaymentAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setMonthlyRepaymentAmt(BigDecimal value) {
        this.monthlyRepaymentAmt = value;
    }

    /**
     * Gets the value of the numMthsSinceLastPayment property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumMthsSinceLastPayment() {
        return numMthsSinceLastPayment;
    }

    /**
     * Sets the value of the numMthsSinceLastPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumMthsSinceLastPayment(Integer value) {
        this.numMthsSinceLastPayment = value;
    }

    /**
     * Gets the value of the maxDelinq7To12 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxDelinq7To12() {
        return maxDelinq7To12;
    }

    /**
     * Sets the value of the maxDelinq7To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxDelinq7To12(Integer value) {
        this.maxDelinq7To12 = value;
    }

    /**
     * Gets the value of the maxConsecMthsDelinq1To12 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxConsecMthsDelinq1To12() {
        return maxConsecMthsDelinq1To12;
    }

    /**
     * Sets the value of the maxConsecMthsDelinq1To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxConsecMthsDelinq1To12(Integer value) {
        this.maxConsecMthsDelinq1To12 = value;
    }

    /**
     * Gets the value of the maxNumMthsConsecDelinqChange7To12 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumMthsConsecDelinqChange7To12() {
        return maxNumMthsConsecDelinqChange7To12;
    }

    /**
     * Sets the value of the maxNumMthsConsecDelinqChange7To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumMthsConsecDelinqChange7To12(Integer value) {
        this.maxNumMthsConsecDelinqChange7To12 = value;
    }

    /**
     * Gets the value of the numMthsSinceDelinq1To12 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumMthsSinceDelinq1To12() {
        return numMthsSinceDelinq1To12;
    }

    /**
     * Sets the value of the numMthsSinceDelinq1To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumMthsSinceDelinq1To12(Integer value) {
        this.numMthsSinceDelinq1To12 = value;
    }

    /**
     * Gets the value of the avgBalance6To8 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvgBalance6To8() {
        return avgBalance6To8;
    }

    /**
     * Sets the value of the avgBalance6To8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvgBalance6To8(BigDecimal value) {
        this.avgBalance6To8 = value;
    }

    /**
     * Gets the value of the avgBalance7To12 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvgBalance7To12() {
        return avgBalance7To12;
    }

    /**
     * Sets the value of the avgBalance7To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvgBalance7To12(BigDecimal value) {
        this.avgBalance7To12 = value;
    }

    /**
     * Gets the value of the avgBalalnce9To11 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvgBalalnce9To11() {
        return avgBalalnce9To11;
    }

    /**
     * Sets the value of the avgBalalnce9To11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvgBalalnce9To11(BigDecimal value) {
        this.avgBalalnce9To11 = value;
    }

    /**
     * Gets the value of the avgBalPercentCreditLimit7To12 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvgBalPercentCreditLimit7To12() {
        return avgBalPercentCreditLimit7To12;
    }

    /**
     * Sets the value of the avgBalPercentCreditLimit7To12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvgBalPercentCreditLimit7To12(BigDecimal value) {
        this.avgBalPercentCreditLimit7To12 = value;
    }

    /**
     * Gets the value of the avgBalPercentCreditLimit9To116To8 property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAvgBalPercentCreditLimit9To116To8() {
        return avgBalPercentCreditLimit9To116To8;
    }

    /**
     * Sets the value of the avgBalPercentCreditLimit9To116To8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAvgBalPercentCreditLimit9To116To8(BigDecimal value) {
        this.avgBalPercentCreditLimit9To116To8 = value;
    }

    /**
     * Gets the value of the currBalPercentCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getCurrBalPercentCreditLimit() {
        return currBalPercentCreditLimit;
    }

    /**
     * Sets the value of the currBalPercentCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setCurrBalPercentCreditLimit(BigDecimal value) {
        this.currBalPercentCreditLimit = value;
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
     * Gets the value of the overdue30DLast6MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue30DLast6MthsCount() {
        return overdue30DLast6MthsCount;
    }

    /**
     * Sets the value of the overdue30DLast6MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue30DLast6MthsCount(Integer value) {
        this.overdue30DLast6MthsCount = value;
    }

    /**
     * Gets the value of the overdue60DLast12MthsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOverdue60DLast12MthsCount() {
        return overdue60DLast12MthsCount;
    }

    /**
     * Sets the value of the overdue60DLast12MthsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOverdue60DLast12MthsCount(Integer value) {
        this.overdue60DLast12MthsCount = value;
    }

    /**
     * Gets the value of the overdue90DFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOverdue90DFlag() {
        return overdue90DFlag;
    }

    /**
     * Sets the value of the overdue90DFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverdue90DFlag(Boolean value) {
        this.overdue90DFlag = value;
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

    /**
     * Gets the value of the score property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the score property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScore().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScoreType }
     * 
     * 
     */
    public List<ScoreType> getScore() {
        if (score == null) {
            score = new ArrayList<ScoreType>();
        }
        return this.score;
    }

    /**
     * Gets the value of the riskModel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the riskModel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRiskModel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RiskModelType }
     * 
     * 
     */
    public List<RiskModelType> getRiskModel() {
        if (riskModel == null) {
            riskModel = new ArrayList<RiskModelType>();
        }
        return this.riskModel;
    }

    /**
     * Gets the value of the delinquency property.
     * 
     * @return
     *     possible object is
     *     {@link DelinquencyType }
     *     
     */
    public DelinquencyType getDelinquency() {
        return delinquency;
    }

    /**
     * Sets the value of the delinquency property.
     * 
     * @param value
     *     allowed object is
     *     {@link DelinquencyType }
     *     
     */
    public void setDelinquency(DelinquencyType value) {
        this.delinquency = value;
    }

}
