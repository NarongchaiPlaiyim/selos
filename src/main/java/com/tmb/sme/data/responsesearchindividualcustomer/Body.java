
package com.tmb.sme.data.responsesearchindividualcustomer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for body complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="body">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="searchResult">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="lastPageFlag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="personalListSection" type="{http://data.sme.tmb.com/responseSearchIndividualCustomer}personalListSection" minOccurs="0"/>
 *         &lt;element name="personalDetailSection" type="{http://data.sme.tmb.com/responseSearchIndividualCustomer}personalDetailSection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "body", propOrder = {
    "searchResult",
    "lastPageFlag",
    "personalListSection",
    "personalDetailSection"
})
public class Body {

    @XmlElement(required = true)
    protected String searchResult;
    protected String lastPageFlag;
    protected PersonalListSection personalListSection;
    protected PersonalDetailSection personalDetailSection;

    /**
     * Gets the value of the searchResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchResult() {
        return searchResult;
    }

    /**
     * Sets the value of the searchResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchResult(String value) {
        this.searchResult = value;
    }

    /**
     * Gets the value of the lastPageFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastPageFlag() {
        return lastPageFlag;
    }

    /**
     * Sets the value of the lastPageFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastPageFlag(String value) {
        this.lastPageFlag = value;
    }

    /**
     * Gets the value of the personalListSection property.
     * 
     * @return
     *     possible object is
     *     {@link PersonalListSection }
     *     
     */
    public PersonalListSection getPersonalListSection() {
        return personalListSection;
    }

    /**
     * Sets the value of the personalListSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalListSection }
     *     
     */
    public void setPersonalListSection(PersonalListSection value) {
        this.personalListSection = value;
    }

    /**
     * Gets the value of the personalDetailSection property.
     * 
     * @return
     *     possible object is
     *     {@link PersonalDetailSection }
     *     
     */
    public PersonalDetailSection getPersonalDetailSection() {
        return personalDetailSection;
    }

    /**
     * Sets the value of the personalDetailSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalDetailSection }
     *     
     */
    public void setPersonalDetailSection(PersonalDetailSection value) {
        this.personalDetailSection = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("searchResult", searchResult)
                .append("lastPageFlag", lastPageFlag)
                .append("personalListSection", personalListSection)
                .append("personalDetailSection", personalDetailSection)
                .toString();
    }
}
