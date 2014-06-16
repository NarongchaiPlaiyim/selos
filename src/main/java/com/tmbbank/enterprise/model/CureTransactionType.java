
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CureTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CureTransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asOfDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="account" type="{http://www.tmbbank.com/enterprise/model}AccountType"/>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CureTransactionType", propOrder = {
    "id",
    "dataSource",
    "asOfDate",
    "account",
    "borrower"
})
public class CureTransactionType {

    @XmlElement(name = "ID")
    protected String id;
    protected String dataSource;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar asOfDate;
    @XmlElement(required = true)
    protected AccountType account;
    @XmlElement(required = true)
    protected BorrowerType borrower;

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
     * Gets the value of the dataSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * Sets the value of the dataSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSource(String value) {
        this.dataSource = value;
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
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link AccountType }
     *     
     */
    public AccountType getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountType }
     *     
     */
    public void setAccount(AccountType value) {
        this.account = value;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("dataSource", dataSource)
                .append("asOfDate", asOfDate)
                .append("account", account)
                .append("borrower", borrower)
                .toString();
    }
}
