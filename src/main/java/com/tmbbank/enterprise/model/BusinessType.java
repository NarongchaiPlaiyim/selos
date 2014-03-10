
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
 * <p>Java class for BusinessType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BusinessType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nameOfBusiness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="timeInBusiness" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="borrowerRelationship" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partnershipPercentage" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="businessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessTypeRiskLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryOfBusiness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationInThai" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monthlyRevenue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="annualRevenue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="salesTurnover" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="atLeast1yrFinStmtFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "BusinessType", propOrder = {
    "id",
    "nameOfBusiness",
    "startDate",
    "endDate",
    "timeInBusiness",
    "borrowerRelationship",
    "partnershipPercentage",
    "businessType",
    "businessTypeRiskLevel",
    "countryOfBusiness",
    "locationInThai",
    "monthlyRevenue",
    "annualRevenue",
    "salesTurnover",
    "atLeast1YrFinStmtFlag",
    "attribute"
})
public class BusinessType {

    @XmlElement(name = "ID")
    protected String id;
    protected String nameOfBusiness;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    protected Double timeInBusiness;
    protected String borrowerRelationship;
    protected BigDecimal partnershipPercentage;
    protected String businessType;
    protected String businessTypeRiskLevel;
    protected String countryOfBusiness;
    protected String locationInThai;
    protected BigDecimal monthlyRevenue;
    protected BigDecimal annualRevenue;
    protected BigDecimal salesTurnover;
    @XmlElement(name = "atLeast1yrFinStmtFlag")
    protected Boolean atLeast1YrFinStmtFlag;
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
     * Gets the value of the nameOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfBusiness() {
        return nameOfBusiness;
    }

    /**
     * Sets the value of the nameOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfBusiness(String value) {
        this.nameOfBusiness = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the timeInBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTimeInBusiness() {
        return timeInBusiness;
    }

    /**
     * Sets the value of the timeInBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTimeInBusiness(Double value) {
        this.timeInBusiness = value;
    }

    /**
     * Gets the value of the borrowerRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBorrowerRelationship() {
        return borrowerRelationship;
    }

    /**
     * Sets the value of the borrowerRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBorrowerRelationship(String value) {
        this.borrowerRelationship = value;
    }

    /**
     * Gets the value of the partnershipPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPartnershipPercentage() {
        return partnershipPercentage;
    }

    /**
     * Sets the value of the partnershipPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPartnershipPercentage(BigDecimal value) {
        this.partnershipPercentage = value;
    }

    /**
     * Gets the value of the businessType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Sets the value of the businessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessType(String value) {
        this.businessType = value;
    }

    /**
     * Gets the value of the businessTypeRiskLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessTypeRiskLevel() {
        return businessTypeRiskLevel;
    }

    /**
     * Sets the value of the businessTypeRiskLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessTypeRiskLevel(String value) {
        this.businessTypeRiskLevel = value;
    }

    /**
     * Gets the value of the countryOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfBusiness() {
        return countryOfBusiness;
    }

    /**
     * Sets the value of the countryOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfBusiness(String value) {
        this.countryOfBusiness = value;
    }

    /**
     * Gets the value of the locationInThai property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationInThai() {
        return locationInThai;
    }

    /**
     * Sets the value of the locationInThai property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationInThai(String value) {
        this.locationInThai = value;
    }

    /**
     * Gets the value of the monthlyRevenue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    /**
     * Sets the value of the monthlyRevenue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonthlyRevenue(BigDecimal value) {
        this.monthlyRevenue = value;
    }

    /**
     * Gets the value of the annualRevenue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAnnualRevenue() {
        return annualRevenue;
    }

    /**
     * Sets the value of the annualRevenue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAnnualRevenue(BigDecimal value) {
        this.annualRevenue = value;
    }

    /**
     * Gets the value of the salesTurnover property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalesTurnover() {
        return salesTurnover;
    }

    /**
     * Sets the value of the salesTurnover property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalesTurnover(BigDecimal value) {
        this.salesTurnover = value;
    }

    /**
     * Gets the value of the atLeast1YrFinStmtFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAtLeast1YrFinStmtFlag() {
        return atLeast1YrFinStmtFlag;
    }

    /**
     * Sets the value of the atLeast1YrFinStmtFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAtLeast1YrFinStmtFlag(Boolean value) {
        this.atLeast1YrFinStmtFlag = value;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("nameOfBusiness", nameOfBusiness)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("timeInBusiness", timeInBusiness)
                .append("borrowerRelationship", borrowerRelationship)
                .append("partnershipPercentage", partnershipPercentage)
                .append("businessType", businessType)
                .append("businessTypeRiskLevel", businessTypeRiskLevel)
                .append("countryOfBusiness", countryOfBusiness)
                .append("locationInThai", locationInThai)
                .append("monthlyRevenue", monthlyRevenue)
                .append("annualRevenue", annualRevenue)
                .append("salesTurnover", salesTurnover)
                .append("atLeast1YrFinStmtFlag", atLeast1YrFinStmtFlag)
                .append("attribute", attribute)
                .toString();
    }
}
