
package com.tmb.sme.data.responsesearchcorporatecustomer;

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
 *         &lt;element name="corporateCustomerListSection" type="{http://data.sme.tmb.com/responseSearchCorporateCustomer}corporateCustomerListSection" minOccurs="0"/>
 *         &lt;element name="corporateCustomerDetailSection" type="{http://data.sme.tmb.com/responseSearchCorporateCustomer}corporateCustomerDetailSection" minOccurs="0"/>
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
    "corporateCustomerListSection",
    "corporateCustomerDetailSection"
})
public class Body {

    @XmlElement(required = true)
    protected String searchResult;
    protected CorporateCustomerListSection corporateCustomerListSection;
    protected CorporateCustomerDetailSection corporateCustomerDetailSection;

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
     * Gets the value of the corporateCustomerListSection property.
     * 
     * @return
     *     possible object is
     *     {@link CorporateCustomerListSection }
     *     
     */
    public CorporateCustomerListSection getCorporateCustomerListSection() {
        return corporateCustomerListSection;
    }

    /**
     * Sets the value of the corporateCustomerListSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorporateCustomerListSection }
     *     
     */
    public void setCorporateCustomerListSection(CorporateCustomerListSection value) {
        this.corporateCustomerListSection = value;
    }

    /**
     * Gets the value of the corporateCustomerDetailSection property.
     * 
     * @return
     *     possible object is
     *     {@link CorporateCustomerDetailSection }
     *     
     */
    public CorporateCustomerDetailSection getCorporateCustomerDetailSection() {
        return corporateCustomerDetailSection;
    }

    /**
     * Sets the value of the corporateCustomerDetailSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorporateCustomerDetailSection }
     *     
     */
    public void setCorporateCustomerDetailSection(CorporateCustomerDetailSection value) {
        this.corporateCustomerDetailSection = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("searchResult", searchResult)
                .append("corporateCustomerListSection", corporateCustomerListSection)
                .append("corporateCustomerDetailSection", corporateCustomerDetailSection)
                .toString();
    }
}
