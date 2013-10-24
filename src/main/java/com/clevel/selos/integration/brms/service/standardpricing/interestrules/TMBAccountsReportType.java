
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TMBAccountsReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TMBAccountsReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxNumTMBCreditCards" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxNumTMBAccounts" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxNumTMBHomeLoans" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountsNoDelinqFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="num31dpdLast6Mths" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="neverBeenNPLFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tdrFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="neverBeenWriteOffFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "TMBAccountsReportType", propOrder = {
    "id",
    "maxNumTMBCreditCards",
    "maxNumTMBAccounts",
    "maxNumTMBHomeLoans",
    "accountsNoDelinqFlag",
    "num31DpdLast6Mths",
    "neverBeenNPLFlag",
    "tdrFlag",
    "neverBeenWriteOffFlag",
    "attribute"
})
public class TMBAccountsReportType {

    @XmlElement(name = "ID")
    protected String id;
    protected Integer maxNumTMBCreditCards;
    protected Integer maxNumTMBAccounts;
    protected Integer maxNumTMBHomeLoans;
    protected Boolean accountsNoDelinqFlag;
    @XmlElement(name = "num31dpdLast6Mths")
    protected Integer num31DpdLast6Mths;
    protected Boolean neverBeenNPLFlag;
    protected Boolean tdrFlag;
    protected Boolean neverBeenWriteOffFlag;
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
     * Gets the value of the maxNumTMBCreditCards property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumTMBCreditCards() {
        return maxNumTMBCreditCards;
    }

    /**
     * Sets the value of the maxNumTMBCreditCards property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumTMBCreditCards(Integer value) {
        this.maxNumTMBCreditCards = value;
    }

    /**
     * Gets the value of the maxNumTMBAccounts property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumTMBAccounts() {
        return maxNumTMBAccounts;
    }

    /**
     * Sets the value of the maxNumTMBAccounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumTMBAccounts(Integer value) {
        this.maxNumTMBAccounts = value;
    }

    /**
     * Gets the value of the maxNumTMBHomeLoans property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumTMBHomeLoans() {
        return maxNumTMBHomeLoans;
    }

    /**
     * Sets the value of the maxNumTMBHomeLoans property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumTMBHomeLoans(Integer value) {
        this.maxNumTMBHomeLoans = value;
    }

    /**
     * Gets the value of the accountsNoDelinqFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccountsNoDelinqFlag() {
        return accountsNoDelinqFlag;
    }

    /**
     * Sets the value of the accountsNoDelinqFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccountsNoDelinqFlag(Boolean value) {
        this.accountsNoDelinqFlag = value;
    }

    /**
     * Gets the value of the num31DpdLast6Mths property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNum31DpdLast6Mths() {
        return num31DpdLast6Mths;
    }

    /**
     * Sets the value of the num31DpdLast6Mths property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNum31DpdLast6Mths(Integer value) {
        this.num31DpdLast6Mths = value;
    }

    /**
     * Gets the value of the neverBeenNPLFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNeverBeenNPLFlag() {
        return neverBeenNPLFlag;
    }

    /**
     * Sets the value of the neverBeenNPLFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNeverBeenNPLFlag(Boolean value) {
        this.neverBeenNPLFlag = value;
    }

    /**
     * Gets the value of the tdrFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTdrFlag() {
        return tdrFlag;
    }

    /**
     * Sets the value of the tdrFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTdrFlag(Boolean value) {
        this.tdrFlag = value;
    }

    /**
     * Gets the value of the neverBeenWriteOffFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNeverBeenWriteOffFlag() {
        return neverBeenWriteOffFlag;
    }

    /**
     * Sets the value of the neverBeenWriteOffFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNeverBeenWriteOffFlag(Boolean value) {
        this.neverBeenWriteOffFlag = value;
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
