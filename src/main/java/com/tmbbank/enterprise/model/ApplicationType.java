
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ApplicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="applicationNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bookingChannel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="segment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="projectCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pricingMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfApplication" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="loanLimitAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="loanCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalMonthlyIncomeMainBorrower" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="totalMonthlyIncomeCoBorrowers" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="totalMonthlyIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="aggregatedCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="aggregatedCreditExposureLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="remainingDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="existingDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="underwritingAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxNumOfBorrowers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType" maxOccurs="unbounded"/>
 *         &lt;element name="product" type="{http://www.tmbbank.com/enterprise/model}ProductType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="riskModel" type="{http://www.tmbbank.com/enterprise/model}RiskModelType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="thirdPartyPledgor" type="{http://www.tmbbank.com/enterprise/model}ThirdPartyPledgorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requiredDocumentSet" type="{http://www.tmbbank.com/enterprise/model}DocumentSetType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fee" type="{http://www.tmbbank.com/enterprise/model}FeeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="business" type="{http://www.tmbbank.com/enterprise/model}BusinessType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="account" type="{http://www.tmbbank.com/enterprise/model}AccountType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationType", propOrder = {
    "id",
    "applicationNumber",
    "bookingChannel",
    "segment",
    "projectCode",
    "pricingMode",
    "dateOfApplication",
    "loanLimitAmount",
    "loanCategory",
    "totalMonthlyIncomeMainBorrower",
    "totalMonthlyIncomeCoBorrowers",
    "totalMonthlyIncome",
    "aggregatedCreditLimit",
    "aggregatedCreditExposureLimit",
    "remainingDebtToIncome",
    "maxDebtToIncome",
    "existingDebtToIncome",
    "underwritingAction",
    "maxNumOfBorrowers",
    "status",
    "borrower",
    "product",
    "riskModel",
    "thirdPartyPledgor",
    "score",
    "attribute",
    "requiredDocumentSet",
    "fee",
    "business",
    "account"
})
public class ApplicationType {

    @XmlElement(name = "ID")
    protected String id;
    protected String applicationNumber;
    protected String bookingChannel;
    protected String segment;
    protected String projectCode;
    protected String pricingMode;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfApplication;
    protected BigDecimal loanLimitAmount;
    protected String loanCategory;
    protected BigDecimal totalMonthlyIncomeMainBorrower;
    protected BigDecimal totalMonthlyIncomeCoBorrowers;
    protected BigDecimal totalMonthlyIncome;
    protected BigDecimal aggregatedCreditLimit;
    protected BigDecimal aggregatedCreditExposureLimit;
    protected BigDecimal remainingDebtToIncome;
    protected BigDecimal maxDebtToIncome;
    protected BigDecimal existingDebtToIncome;
    protected String underwritingAction;
    protected Integer maxNumOfBorrowers;
    protected String status;
    @XmlElement(required = true)
    protected List<BorrowerType> borrower;
    protected List<ProductType> product;
    protected List<RiskModelType> riskModel;
    protected List<ThirdPartyPledgorType> thirdPartyPledgor;
    protected List<ScoreType> score;
    protected List<AttributeType> attribute;
    protected List<DocumentSetType> requiredDocumentSet;
    protected List<FeeType> fee;
    protected List<BusinessType> business;
    protected List<AccountType> account;

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
     * Gets the value of the applicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationNumber() {
        return applicationNumber;
    }

    /**
     * Sets the value of the applicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationNumber(String value) {
        this.applicationNumber = value;
    }

    /**
     * Gets the value of the bookingChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBookingChannel() {
        return bookingChannel;
    }

    /**
     * Sets the value of the bookingChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBookingChannel(String value) {
        this.bookingChannel = value;
    }

    /**
     * Gets the value of the segment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegment() {
        return segment;
    }

    /**
     * Sets the value of the segment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegment(String value) {
        this.segment = value;
    }

    /**
     * Gets the value of the projectCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * Sets the value of the projectCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectCode(String value) {
        this.projectCode = value;
    }

    /**
     * Gets the value of the pricingMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPricingMode() {
        return pricingMode;
    }

    /**
     * Sets the value of the pricingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPricingMode(String value) {
        this.pricingMode = value;
    }

    /**
     * Gets the value of the dateOfApplication property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfApplication() {
        return dateOfApplication;
    }

    /**
     * Sets the value of the dateOfApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfApplication(XMLGregorianCalendar value) {
        this.dateOfApplication = value;
    }

    /**
     * Gets the value of the loanLimitAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLoanLimitAmount() {
        return loanLimitAmount;
    }

    /**
     * Sets the value of the loanLimitAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLoanLimitAmount(BigDecimal value) {
        this.loanLimitAmount = value;
    }

    /**
     * Gets the value of the loanCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanCategory() {
        return loanCategory;
    }

    /**
     * Sets the value of the loanCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanCategory(String value) {
        this.loanCategory = value;
    }

    /**
     * Gets the value of the totalMonthlyIncomeMainBorrower property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalMonthlyIncomeMainBorrower() {
        return totalMonthlyIncomeMainBorrower;
    }

    /**
     * Sets the value of the totalMonthlyIncomeMainBorrower property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalMonthlyIncomeMainBorrower(BigDecimal value) {
        this.totalMonthlyIncomeMainBorrower = value;
    }

    /**
     * Gets the value of the totalMonthlyIncomeCoBorrowers property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalMonthlyIncomeCoBorrowers() {
        return totalMonthlyIncomeCoBorrowers;
    }

    /**
     * Sets the value of the totalMonthlyIncomeCoBorrowers property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalMonthlyIncomeCoBorrowers(BigDecimal value) {
        this.totalMonthlyIncomeCoBorrowers = value;
    }

    /**
     * Gets the value of the totalMonthlyIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalMonthlyIncome() {
        return totalMonthlyIncome;
    }

    /**
     * Sets the value of the totalMonthlyIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalMonthlyIncome(BigDecimal value) {
        this.totalMonthlyIncome = value;
    }

    /**
     * Gets the value of the aggregatedCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAggregatedCreditLimit() {
        return aggregatedCreditLimit;
    }

    /**
     * Sets the value of the aggregatedCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAggregatedCreditLimit(BigDecimal value) {
        this.aggregatedCreditLimit = value;
    }

    /**
     * Gets the value of the aggregatedCreditExposureLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAggregatedCreditExposureLimit() {
        return aggregatedCreditExposureLimit;
    }

    /**
     * Sets the value of the aggregatedCreditExposureLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAggregatedCreditExposureLimit(BigDecimal value) {
        this.aggregatedCreditExposureLimit = value;
    }

    /**
     * Gets the value of the remainingDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemainingDebtToIncome() {
        return remainingDebtToIncome;
    }

    /**
     * Sets the value of the remainingDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemainingDebtToIncome(BigDecimal value) {
        this.remainingDebtToIncome = value;
    }

    /**
     * Gets the value of the maxDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxDebtToIncome() {
        return maxDebtToIncome;
    }

    /**
     * Sets the value of the maxDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxDebtToIncome(BigDecimal value) {
        this.maxDebtToIncome = value;
    }

    /**
     * Gets the value of the existingDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getExistingDebtToIncome() {
        return existingDebtToIncome;
    }

    /**
     * Sets the value of the existingDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setExistingDebtToIncome(BigDecimal value) {
        this.existingDebtToIncome = value;
    }

    /**
     * Gets the value of the underwritingAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnderwritingAction() {
        return underwritingAction;
    }

    /**
     * Sets the value of the underwritingAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnderwritingAction(String value) {
        this.underwritingAction = value;
    }

    /**
     * Gets the value of the maxNumOfBorrowers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumOfBorrowers() {
        return maxNumOfBorrowers;
    }

    /**
     * Sets the value of the maxNumOfBorrowers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumOfBorrowers(Integer value) {
        this.maxNumOfBorrowers = value;
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
     * Gets the value of the borrower property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the borrower property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBorrower().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BorrowerType }
     * 
     * 
     */
    public List<BorrowerType> getBorrower() {
        if (borrower == null) {
            borrower = new ArrayList<BorrowerType>();
        }
        return this.borrower;
    }

    /**
     * Gets the value of the product property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductType }
     * 
     * 
     */
    public List<ProductType> getProduct() {
        if (product == null) {
            product = new ArrayList<ProductType>();
        }
        return this.product;
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
     * Gets the value of the thirdPartyPledgor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thirdPartyPledgor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThirdPartyPledgor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThirdPartyPledgorType }
     * 
     * 
     */
    public List<ThirdPartyPledgorType> getThirdPartyPledgor() {
        if (thirdPartyPledgor == null) {
            thirdPartyPledgor = new ArrayList<ThirdPartyPledgorType>();
        }
        return this.thirdPartyPledgor;
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
     * Gets the value of the requiredDocumentSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiredDocumentSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiredDocumentSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentSetType }
     * 
     * 
     */
    public List<DocumentSetType> getRequiredDocumentSet() {
        if (requiredDocumentSet == null) {
            requiredDocumentSet = new ArrayList<DocumentSetType>();
        }
        return this.requiredDocumentSet;
    }

    /**
     * Gets the value of the fee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FeeType }
     * 
     * 
     */
    public List<FeeType> getFee() {
        if (fee == null) {
            fee = new ArrayList<FeeType>();
        }
        return this.fee;
    }

    /**
     * Gets the value of the business property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the business property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusiness().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BusinessType }
     * 
     * 
     */
    public List<BusinessType> getBusiness() {
        if (business == null) {
            business = new ArrayList<BusinessType>();
        }
        return this.business;
    }

    /**
     * Gets the value of the account property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the account property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountType }
     * 
     * 
     */
    public List<AccountType> getAccount() {
        if (account == null) {
            account = new ArrayList<AccountType>();
        }
        return this.account;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("applicationNumber", applicationNumber)
                .append("bookingChannel", bookingChannel)
                .append("segment", segment)
                .append("projectCode", projectCode)
                .append("pricingMode", pricingMode)
                .append("dateOfApplication", dateOfApplication)
                .append("loanLimitAmount", loanLimitAmount)
                .append("loanCategory", loanCategory)
                .append("totalMonthlyIncomeMainBorrower", totalMonthlyIncomeMainBorrower)
                .append("totalMonthlyIncomeCoBorrowers", totalMonthlyIncomeCoBorrowers)
                .append("totalMonthlyIncome", totalMonthlyIncome)
                .append("aggregatedCreditLimit", aggregatedCreditLimit)
                .append("aggregatedCreditExposureLimit", aggregatedCreditExposureLimit)
                .append("remainingDebtToIncome", remainingDebtToIncome)
                .append("maxDebtToIncome", maxDebtToIncome)
                .append("existingDebtToIncome", existingDebtToIncome)
                .append("underwritingAction", underwritingAction)
                .append("maxNumOfBorrowers", maxNumOfBorrowers)
                .append("status", status)
                .append("borrower", borrower)
                .append("product", product)
                .append("riskModel", riskModel)
                .append("thirdPartyPledgor", thirdPartyPledgor)
                .append("score", score)
                .append("attribute", attribute)
                .append("requiredDocumentSet", requiredDocumentSet)
                .append("fee", fee)
                .append("business", business)
                .append("account", account)
                .toString();
    }
}
