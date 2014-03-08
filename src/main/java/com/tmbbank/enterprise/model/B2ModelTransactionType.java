
package com.tmbbank.enterprise.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for B2ModelTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="B2ModelTransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asOfDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ICModelTransaction" type="{http://www.tmbbank.com/enterprise/model}ICModelTransactionType" minOccurs="0"/>
 *         &lt;element name="BSCFModelTransaction" type="{http://www.tmbbank.com/enterprise/model}BSCFModelTransactionType" minOccurs="0"/>
 *         &lt;element name="IBNRModelTransaction" type="{http://www.tmbbank.com/enterprise/model}IBNRModelTransactionType" minOccurs="0"/>
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
@XmlType(name = "B2ModelTransactionType", propOrder = {
    "id",
    "asOfDate",
    "icModelTransaction",
    "bscfModelTransaction",
    "ibnrModelTransaction",
    "attribute"
})
public class B2ModelTransactionType {

    @XmlElement(name = "ID")
    protected String id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar asOfDate;
    @XmlElement(name = "ICModelTransaction")
    protected ICModelTransactionType icModelTransaction;
    @XmlElement(name = "BSCFModelTransaction")
    protected BSCFModelTransactionType bscfModelTransaction;
    @XmlElement(name = "IBNRModelTransaction")
    protected IBNRModelTransactionType ibnrModelTransaction;
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
     * Gets the value of the icModelTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link ICModelTransactionType }
     *     
     */
    public ICModelTransactionType getICModelTransaction() {
        return icModelTransaction;
    }

    /**
     * Sets the value of the icModelTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICModelTransactionType }
     *     
     */
    public void setICModelTransaction(ICModelTransactionType value) {
        this.icModelTransaction = value;
    }

    /**
     * Gets the value of the bscfModelTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link BSCFModelTransactionType }
     *     
     */
    public BSCFModelTransactionType getBSCFModelTransaction() {
        return bscfModelTransaction;
    }

    /**
     * Sets the value of the bscfModelTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link BSCFModelTransactionType }
     *     
     */
    public void setBSCFModelTransaction(BSCFModelTransactionType value) {
        this.bscfModelTransaction = value;
    }

    /**
     * Gets the value of the ibnrModelTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link IBNRModelTransactionType }
     *     
     */
    public IBNRModelTransactionType getIBNRModelTransaction() {
        return ibnrModelTransaction;
    }

    /**
     * Sets the value of the ibnrModelTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link IBNRModelTransactionType }
     *     
     */
    public void setIBNRModelTransaction(IBNRModelTransactionType value) {
        this.ibnrModelTransaction = value;
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
