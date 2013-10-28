
package com.tmb.sme.data.eaisearchcorporatecustomer;

import com.tmb.sme.data.requestsearchcorporatecustomer.ReqSearchCorporateCustomer;

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
 *         &lt;element name="requestData" type="{http://data.sme.tmb.com/requestSearchCorporateCustomer}reqSearchCorporateCustomer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestData"
})
@XmlRootElement(name = "searchCorporateCustomer")
public class SearchCorporateCustomer {

    @XmlElement(required = true)
    protected ReqSearchCorporateCustomer requestData;

    /**
     * Gets the value of the requestData property.
     *
     * @return possible object is
     *         {@link ReqSearchCorporateCustomer }
     */
    public ReqSearchCorporateCustomer getRequestData() {
        return requestData;
    }

    /**
     * Sets the value of the requestData property.
     *
     * @param value allowed object is
     *              {@link ReqSearchCorporateCustomer }
     */
    public void setRequestData(ReqSearchCorporateCustomer value) {
        this.requestData = value;
    }

}
