
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DelinquencyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DelinquencyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DelinquencyCurrentMth" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="NumberOfDelinquencyL6M" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="MaxDelinquencyL6M" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
@XmlType(name = "DelinquencyType", propOrder = {
    "id",
    "delinquencyCurrentMth",
    "numberOfDelinquencyL6M",
    "maxDelinquencyL6M",
    "attribute"
})
public class DelinquencyType {

    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "DelinquencyCurrentMth")
    protected BigDecimal delinquencyCurrentMth;
    @XmlElement(name = "NumberOfDelinquencyL6M")
    protected BigDecimal numberOfDelinquencyL6M;
    @XmlElement(name = "MaxDelinquencyL6M")
    protected BigDecimal maxDelinquencyL6M;
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
     * Gets the value of the delinquencyCurrentMth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDelinquencyCurrentMth() {
        return delinquencyCurrentMth;
    }

    /**
     * Sets the value of the delinquencyCurrentMth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDelinquencyCurrentMth(BigDecimal value) {
        this.delinquencyCurrentMth = value;
    }

    /**
     * Gets the value of the numberOfDelinquencyL6M property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumberOfDelinquencyL6M() {
        return numberOfDelinquencyL6M;
    }

    /**
     * Sets the value of the numberOfDelinquencyL6M property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumberOfDelinquencyL6M(BigDecimal value) {
        this.numberOfDelinquencyL6M = value;
    }

    /**
     * Gets the value of the maxDelinquencyL6M property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxDelinquencyL6M() {
        return maxDelinquencyL6M;
    }

    /**
     * Sets the value of the maxDelinquencyL6M property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxDelinquencyL6M(BigDecimal value) {
        this.maxDelinquencyL6M = value;
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
