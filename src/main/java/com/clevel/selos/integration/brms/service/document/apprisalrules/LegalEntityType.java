
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
 * <p>Java class for LegalEntityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LegalEntityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="juristicID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfIncorporation" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="timeInBusiness" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="keyPerson" type="{http://www.tmbbank.com/enterprise/model}IndividualType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="salesTurnover" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="atLeast1yrFinStmtFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="netWorth" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="business" type="{http://www.tmbbank.com/enterprise/model}BusinessType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="juristicBusinessOwner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "LegalEntityType", propOrder = {
    "id",
    "juristicID",
    "businessType",
    "dateOfIncorporation",
    "timeInBusiness",
    "keyPerson",
    "salesTurnover",
    "atLeast1YrFinStmtFlag",
    "netWorth",
    "business",
    "juristicBusinessOwner",
    "attribute"
})
public class LegalEntityType {

    @XmlElement(name = "ID")
    protected String id;
    protected String juristicID;
    protected String businessType;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfIncorporation;
    protected Double timeInBusiness;
    protected List<IndividualType> keyPerson;
    protected BigDecimal salesTurnover;
    @XmlElement(name = "atLeast1yrFinStmtFlag")
    protected Boolean atLeast1YrFinStmtFlag;
    protected BigDecimal netWorth;
    protected List<BusinessType> business;
    protected String juristicBusinessOwner;
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
     * Gets the value of the juristicID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJuristicID() {
        return juristicID;
    }

    /**
     * Sets the value of the juristicID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJuristicID(String value) {
        this.juristicID = value;
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
     * Gets the value of the dateOfIncorporation property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfIncorporation() {
        return dateOfIncorporation;
    }

    /**
     * Sets the value of the dateOfIncorporation property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setDateOfIncorporation(XMLGregorianCalendar value) {
        this.dateOfIncorporation = value;
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
     * Gets the value of the keyPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndividualType }
     * 
     * 
     */
    public List<IndividualType> getKeyPerson() {
        if (keyPerson == null) {
            keyPerson = new ArrayList<IndividualType>();
        }
        return this.keyPerson;
    }

    /**
     * Gets the value of the salesTurnover property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
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
     *     {@link java.math.BigDecimal }
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
     * Gets the value of the netWorth property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getNetWorth() {
        return netWorth;
    }

    /**
     * Sets the value of the netWorth property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setNetWorth(BigDecimal value) {
        this.netWorth = value;
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
     * Gets the value of the juristicBusinessOwner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJuristicBusinessOwner() {
        return juristicBusinessOwner;
    }

    /**
     * Sets the value of the juristicBusinessOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJuristicBusinessOwner(String value) {
        this.juristicBusinessOwner = value;
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
