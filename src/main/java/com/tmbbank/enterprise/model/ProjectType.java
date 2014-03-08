
package com.tmbbank.enterprise.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="appraisalFeeWaivedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="developer" type="{http://www.tmbbank.com/enterprise/model}DeveloperType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "ProjectType", propOrder = {
    "id",
    "name",
    "appraisalFeeWaivedFlag",
    "developer",
    "attribute"
})
public class ProjectType {

    @XmlElement(name = "ID")
    protected String id;
    protected String name;
    protected Boolean appraisalFeeWaivedFlag;
    protected List<DeveloperType> developer;
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
     * Gets the value of the appraisalFeeWaivedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAppraisalFeeWaivedFlag() {
        return appraisalFeeWaivedFlag;
    }

    /**
     * Sets the value of the appraisalFeeWaivedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAppraisalFeeWaivedFlag(Boolean value) {
        this.appraisalFeeWaivedFlag = value;
    }

    /**
     * Gets the value of the developer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the developer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeveloper().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeveloperType }
     * 
     * 
     */
    public List<DeveloperType> getDeveloper() {
        if (developer == null) {
            developer = new ArrayList<DeveloperType>();
        }
        return this.developer;
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
