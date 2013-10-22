
package com.tmb.sme.data.eaisearchindividualcustomer;

import com.tmb.sme.data.responsesearchindividualcustomer.ResSearchIndividualCustomer;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="responseData" type="{http://data.sme.tmb.com/responseSearchIndividualCustomer}resSearchIndividualCustomer"/>
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
@XmlRootElement(name = "searchIndividualCustomerResponse")
public class SearchIndividualCustomerResponse {

    @XmlElement(required = true)
    protected ResSearchIndividualCustomer responseData;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ResSearchIndividualCustomer }
     *     
     */
    public ResSearchIndividualCustomer getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResSearchIndividualCustomer }
     *     
     */
    public void setResponseData(ResSearchIndividualCustomer value) {
        this.responseData = value;
    }

}
