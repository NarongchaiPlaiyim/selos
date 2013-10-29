
package com.tmb.sme.data.responsesearchcorporatecustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for corporateCustomerDetailSection complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="corporateCustomerDetailSection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="corporateDetail" type="{http://data.sme.tmb.com/responseSearchCorporateCustomer}corporateDetail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "corporateCustomerDetailSection", propOrder = {
        "corporateDetail"
})
public class CorporateCustomerDetailSection {

    @XmlElement(required = true)
    protected CorporateDetail corporateDetail;

    /**
     * Gets the value of the corporateDetail property.
     *
     * @return possible object is
     *         {@link CorporateDetail }
     */
    public CorporateDetail getCorporateDetail() {
        return corporateDetail;
    }

    /**
     * Sets the value of the corporateDetail property.
     *
     * @param value allowed object is
     *              {@link CorporateDetail }
     */
    public void setCorporateDetail(CorporateDetail value) {
        this.corporateDetail = value;
    }

}
