
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SMEBehaviorScoreTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMEBehaviorScoreTransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asOfDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMEBehaviorScoreTransactionType", propOrder = {
    "borrower",
    "id",
    "asOfDate"
})
public class SMEBehaviorScoreTransactionType {

    @XmlElement(required = true)
    protected BorrowerType borrower;
    @XmlElement(name = "ID")
    protected String id;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar asOfDate;

    /**
     * Gets the value of the borrower property.
     * 
     * @return
     *     possible object is
     *     {@link BorrowerType }
     *     
     */
    public BorrowerType getBorrower() {
        return borrower;
    }

    /**
     * Sets the value of the borrower property.
     * 
     * @param value
     *     allowed object is
     *     {@link BorrowerType }
     *     
     */
    public void setBorrower(BorrowerType value) {
        this.borrower = value;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("borrower", borrower)
                .append("id", id)
                .append("asOfDate", asOfDate)
                .toString();
    }
}
