
package com.tmb.sme.data.eaisearchcorporatecustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseData" type="{http://data.sme.tmb.com/responseSearchCorporateCustomer}resSearchCorporateCustomer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "responseData"
})
@XmlRootElement(name = "searchCorporateCustomerResponse")
public class SearchCorporateCustomerResponse {

    @XmlElement(required = true)
    protected ResSearchCorporateCustomer responseData;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ResSearchCorporateCustomer }
     *     
     */
    public ResSearchCorporateCustomer getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResSearchCorporateCustomer }
     *     
     */
    public void setResponseData(ResSearchCorporateCustomer value) {
        this.responseData = value;
    }

}
