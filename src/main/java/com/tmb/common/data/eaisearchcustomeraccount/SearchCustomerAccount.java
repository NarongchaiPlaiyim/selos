
package com.tmb.common.data.eaisearchcustomeraccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tmb.common.data.requestsearchcustomeraccount.ReqSearchCustomerAccount;


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
 *         &lt;element name="requestData" type="{http://data.common.tmb.com/requestSearchCustomerAccount}reqSearchCustomerAccount"/>
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
@XmlRootElement(name = "searchCustomerAccount")
public class SearchCustomerAccount {

    @XmlElement(required = true)
    protected ReqSearchCustomerAccount requestData;

    /**
     * Gets the value of the requestData property.
     * 
     * @return
     *     possible object is
     *     {@link ReqSearchCustomerAccount }
     *     
     */
    public ReqSearchCustomerAccount getRequestData() {
        return requestData;
    }

    /**
     * Sets the value of the requestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqSearchCustomerAccount }
     *     
     */
    public void setRequestData(ReqSearchCustomerAccount value) {
        this.requestData = value;
    }

}
