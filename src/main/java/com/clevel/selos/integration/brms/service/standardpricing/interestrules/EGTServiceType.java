
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for EGTServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EGTServiceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="processDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="botID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versionNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="account" type="{http://www.tmbbank.com/enterprise/model}AccountType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.tmbbank.com/enterprise/model}MessageType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="result" type="{http://www.tmbbank.com/enterprise/model}ResultType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "EGTServiceType", propOrder = {
    "id",
    "processDate",
    "botID",
    "passFlag",
    "versionNumber",
    "account",
    "borrower",
    "message",
    "result",
    "attribute"
})
public class EGTServiceType {

    @XmlElement(name = "ID")
    protected String id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar processDate;
    protected String botID;
    protected String passFlag;
    protected String versionNumber;
    protected List<AccountType> account;
    protected List<BorrowerType> borrower;
    protected List<MessageType> message;
    protected List<ResultType> result;
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
     * Gets the value of the processDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProcessDate() {
        return processDate;
    }

    /**
     * Sets the value of the processDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setProcessDate(XMLGregorianCalendar value) {
        this.processDate = value;
    }

    /**
     * Gets the value of the botID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotID() {
        return botID;
    }

    /**
     * Sets the value of the botID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotID(String value) {
        this.botID = value;
    }

    /**
     * Gets the value of the passFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassFlag() {
        return passFlag;
    }

    /**
     * Sets the value of the passFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassFlag(String value) {
        this.passFlag = value;
    }

    /**
     * Gets the value of the versionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets the value of the versionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNumber(String value) {
        this.versionNumber = value;
    }

    /**
     * Gets the value of the account property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the account property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountType }
     * 
     * 
     */
    public List<AccountType> getAccount() {
        if (account == null) {
            account = new ArrayList<AccountType>();
        }
        return this.account;
    }

    /**
     * Gets the value of the borrower property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the borrower property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBorrower().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BorrowerType }
     * 
     * 
     */
    public List<BorrowerType> getBorrower() {
        if (borrower == null) {
            borrower = new ArrayList<BorrowerType>();
        }
        return this.borrower;
    }

    /**
     * Gets the value of the message property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageType }
     * 
     * 
     */
    public List<MessageType> getMessage() {
        if (message == null) {
            message = new ArrayList<MessageType>();
        }
        return this.message;
    }

    /**
     * Gets the value of the result property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the result property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultType }
     * 
     * 
     */
    public List<ResultType> getResult() {
        if (result == null) {
            result = new ArrayList<ResultType>();
        }
        return this.result;
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
