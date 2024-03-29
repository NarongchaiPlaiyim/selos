
package com.tmb.common.data.eaisearchindividualcustomercm;

import com.tmb.common.data.requestsearchindividualcustomer.ReqSearchIndividualCustomer;

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
 *         &lt;element name="requestData" type="{http://data.common.tmb.com/requestSearchIndividualCustomer}reqSearchIndividualCustomer"/>
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
    "requestData"
})
@XmlRootElement(name = "searchIndividualCustomer")
public class SearchIndividualCustomer {

    @XmlElement(required = true)
    protected ReqSearchIndividualCustomer requestData;

    /**
     * Gets the value of the requestData property.
     * 
     * @return
     *     possible object is
     *     {@link ReqSearchIndividualCustomer }
     *     
     */
    public ReqSearchIndividualCustomer getRequestData() {
        return requestData;
    }

    /**
     * Sets the value of the requestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqSearchIndividualCustomer }
     *     
     */
    public void setRequestData(ReqSearchIndividualCustomer value) {
        this.requestData = value;
    }

}
