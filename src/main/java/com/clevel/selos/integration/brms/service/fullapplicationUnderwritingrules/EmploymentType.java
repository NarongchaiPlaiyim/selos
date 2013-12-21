
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
 * <p>Java class for EmploymentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EmploymentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nameOfEmployer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="timeOnJob" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="occupationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessTypeRiskLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monthlySalary" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="monthlyResidualIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="appointedAtCurrentPostFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="passedProbationFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="occupationTypeRiskLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="thaiMilitaryFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="thaiMilitaryRank" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryOfEmployment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contractPeriod" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lengthOfEmployment" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="natureOfBusiness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "EmploymentType", propOrder = {
    "id",
    "nameOfEmployer",
    "startDate",
    "endDate",
    "timeOnJob",
    "occupationType",
    "position",
    "businessType",
    "businessTypeRiskLevel",
    "monthlySalary",
    "monthlyResidualIncome",
    "appointedAtCurrentPostFlag",
    "passedProbationFlag",
    "occupationTypeRiskLevel",
    "thaiMilitaryFlag",
    "thaiMilitaryRank",
    "countryOfEmployment",
    "contractPeriod",
    "lengthOfEmployment",
    "natureOfBusiness",
    "attribute"
})
public class EmploymentType {

    @XmlElement(name = "ID")
    protected String id;
    protected String nameOfEmployer;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    protected Double timeOnJob;
    protected String occupationType;
    protected String position;
    protected String businessType;
    protected String businessTypeRiskLevel;
    protected BigDecimal monthlySalary;
    protected BigDecimal monthlyResidualIncome;
    protected Boolean appointedAtCurrentPostFlag;
    protected Boolean passedProbationFlag;
    protected String occupationTypeRiskLevel;
    protected Boolean thaiMilitaryFlag;
    protected String thaiMilitaryRank;
    protected String countryOfEmployment;
    protected Integer contractPeriod;
    protected Integer lengthOfEmployment;
    protected String natureOfBusiness;
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
     * Gets the value of the nameOfEmployer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfEmployer() {
        return nameOfEmployer;
    }

    /**
     * Sets the value of the nameOfEmployer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfEmployer(String value) {
        this.nameOfEmployer = value;
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
     * Gets the value of the timeOnJob property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTimeOnJob() {
        return timeOnJob;
    }

    /**
     * Sets the value of the timeOnJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTimeOnJob(Double value) {
        this.timeOnJob = value;
    }

    /**
     * Gets the value of the occupationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationType() {
        return occupationType;
    }

    /**
     * Sets the value of the occupationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationType(String value) {
        this.occupationType = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
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
     * Gets the value of the monthlySalary property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    /**
     * Sets the value of the monthlySalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonthlySalary(BigDecimal value) {
        this.monthlySalary = value;
    }

    /**
     * Gets the value of the monthlyResidualIncome property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonthlyResidualIncome() {
        return monthlyResidualIncome;
    }

    /**
     * Sets the value of the monthlyResidualIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonthlyResidualIncome(BigDecimal value) {
        this.monthlyResidualIncome = value;
    }

    /**
     * Gets the value of the appointedAtCurrentPostFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAppointedAtCurrentPostFlag() {
        return appointedAtCurrentPostFlag;
    }

    /**
     * Sets the value of the appointedAtCurrentPostFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAppointedAtCurrentPostFlag(Boolean value) {
        this.appointedAtCurrentPostFlag = value;
    }

    /**
     * Gets the value of the passedProbationFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPassedProbationFlag() {
        return passedProbationFlag;
    }

    /**
     * Sets the value of the passedProbationFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPassedProbationFlag(Boolean value) {
        this.passedProbationFlag = value;
    }

    /**
     * Gets the value of the occupationTypeRiskLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationTypeRiskLevel() {
        return occupationTypeRiskLevel;
    }

    /**
     * Sets the value of the occupationTypeRiskLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationTypeRiskLevel(String value) {
        this.occupationTypeRiskLevel = value;
    }

    /**
     * Gets the value of the thaiMilitaryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isThaiMilitaryFlag() {
        return thaiMilitaryFlag;
    }

    /**
     * Sets the value of the thaiMilitaryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setThaiMilitaryFlag(Boolean value) {
        this.thaiMilitaryFlag = value;
    }

    /**
     * Gets the value of the thaiMilitaryRank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThaiMilitaryRank() {
        return thaiMilitaryRank;
    }

    /**
     * Sets the value of the thaiMilitaryRank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThaiMilitaryRank(String value) {
        this.thaiMilitaryRank = value;
    }

    /**
     * Gets the value of the countryOfEmployment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfEmployment() {
        return countryOfEmployment;
    }

    /**
     * Sets the value of the countryOfEmployment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfEmployment(String value) {
        this.countryOfEmployment = value;
    }

    /**
     * Gets the value of the contractPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getContractPeriod() {
        return contractPeriod;
    }

    /**
     * Sets the value of the contractPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setContractPeriod(Integer value) {
        this.contractPeriod = value;
    }

    /**
     * Gets the value of the lengthOfEmployment property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLengthOfEmployment() {
        return lengthOfEmployment;
    }

    /**
     * Sets the value of the lengthOfEmployment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLengthOfEmployment(Integer value) {
        this.lengthOfEmployment = value;
    }

    /**
     * Gets the value of the natureOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    /**
     * Sets the value of the natureOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfBusiness(String value) {
        this.natureOfBusiness = value;
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
