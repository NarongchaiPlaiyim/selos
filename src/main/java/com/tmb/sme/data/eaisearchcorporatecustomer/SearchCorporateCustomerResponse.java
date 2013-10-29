
package com.tmb.sme.data.eaisearchcorporatecustomer;

import com.tmb.sme.data.responsesearchcorporatecustomer.ResSearchCorporateCustomer;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
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
     * @return possible object is
     *         {@link ResSearchCorporateCustomer }
     */
    public ResSearchCorporateCustomer getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     *
     * @param value allowed object is
     *              {@link ResSearchCorporateCustomer }
     */
    public void setResponseData(ResSearchCorporateCustomer value) {
        this.responseData = value;
    }

}
