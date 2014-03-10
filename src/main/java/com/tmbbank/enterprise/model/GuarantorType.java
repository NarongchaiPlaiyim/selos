
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GuarantorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GuarantorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collateral" type="{http://www.tmbbank.com/enterprise/model}CollateralType"/>
 *         &lt;element name="borrowerRelationship" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessOwnerFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="familyRelativeOwnerFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "GuarantorType", propOrder = {
    "id",
    "collateral",
    "borrowerRelationship",
    "businessOwnerFlag",
    "familyRelativeOwnerFlag",
    "attribute"
})
public class GuarantorType {

    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(required = true)
    protected CollateralType collateral;
    protected String borrowerRelationship;
    protected Boolean businessOwnerFlag;
    protected Boolean familyRelativeOwnerFlag;
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
     * Gets the value of the collateral property.
     * 
     * @return
     *     possible object is
     *     {@link CollateralType }
     *     
     */
    public CollateralType getCollateral() {
        return collateral;
    }

    /**
     * Sets the value of the collateral property.
     * 
     * @param value
     *     allowed object is
     *     {@link CollateralType }
     *     
     */
    public void setCollateral(CollateralType value) {
        this.collateral = value;
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
     * Gets the value of the businessOwnerFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBusinessOwnerFlag() {
        return businessOwnerFlag;
    }

    /**
     * Sets the value of the businessOwnerFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBusinessOwnerFlag(Boolean value) {
        this.businessOwnerFlag = value;
    }

    /**
     * Gets the value of the familyRelativeOwnerFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFamilyRelativeOwnerFlag() {
        return familyRelativeOwnerFlag;
    }

    /**
     * Sets the value of the familyRelativeOwnerFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFamilyRelativeOwnerFlag(Boolean value) {
        this.familyRelativeOwnerFlag = value;
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
                .append("collateral", collateral)
                .append("borrowerRelationship", borrowerRelationship)
                .append("businessOwnerFlag", businessOwnerFlag)
                .append("familyRelativeOwnerFlag", familyRelativeOwnerFlag)
                .append("attribute", attribute)
                .toString();
    }
}
