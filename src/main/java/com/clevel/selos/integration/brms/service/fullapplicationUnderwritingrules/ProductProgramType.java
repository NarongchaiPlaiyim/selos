
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProductProgramType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductProgramType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxAge" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="minCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="minNumTimesOfGMI" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxNumTimesOfGMI" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="minTenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxTenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductProgramType", propOrder = {
    "id",
    "name",
    "businessType",
    "maxAge",
    "minCreditLimit",
    "maxCreditLimit",
    "minNumTimesOfGMI",
    "maxNumTimesOfGMI",
    "attribute",
    "minTenor",
    "maxTenor"
})
public class ProductProgramType {

    @XmlElement(name = "ID")
    protected String id;
    protected String name;
    protected String businessType;
    protected Integer maxAge;
    protected BigDecimal minCreditLimit;
    protected BigDecimal maxCreditLimit;
    protected Integer minNumTimesOfGMI;
    protected Integer maxNumTimesOfGMI;
    protected List<AttributeType> attribute;
    protected Integer minTenor;
    protected Integer maxTenor;

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the maxAge property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the value of the maxAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxAge(Integer value) {
        this.maxAge = value;
    }

    /**
     * Gets the value of the minCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinCreditLimit() {
        return minCreditLimit;
    }

    /**
     * Sets the value of the minCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinCreditLimit(BigDecimal value) {
        this.minCreditLimit = value;
    }

    /**
     * Gets the value of the maxCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxCreditLimit() {
        return maxCreditLimit;
    }

    /**
     * Sets the value of the maxCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxCreditLimit(BigDecimal value) {
        this.maxCreditLimit = value;
    }

    /**
     * Gets the value of the minNumTimesOfGMI property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinNumTimesOfGMI() {
        return minNumTimesOfGMI;
    }

    /**
     * Sets the value of the minNumTimesOfGMI property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinNumTimesOfGMI(Integer value) {
        this.minNumTimesOfGMI = value;
    }

    /**
     * Gets the value of the maxNumTimesOfGMI property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumTimesOfGMI() {
        return maxNumTimesOfGMI;
    }

    /**
     * Sets the value of the maxNumTimesOfGMI property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumTimesOfGMI(Integer value) {
        this.maxNumTimesOfGMI = value;
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
     * Gets the value of the minTenor property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinTenor() {
        return minTenor;
    }

    /**
     * Sets the value of the minTenor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinTenor(Integer value) {
        this.minTenor = value;
    }

    /**
     * Gets the value of the maxTenor property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxTenor() {
        return maxTenor;
    }

    /**
     * Sets the value of the maxTenor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxTenor(Integer value) {
        this.maxTenor = value;
    }

}
