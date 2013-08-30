
package com.tmb.common.data.eaisearchcustomeraccount;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tmb.common.data.responsesearchcustomeraccount.ResSearchCustomerAccount;


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
 *         &lt;element name="responseData" type="{http://data.common.tmb.com/responseSearchCustomerAccount}resSearchCustomerAccount"/>
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
@XmlRootElement(name = "searchCustomerAccountResponse")
public class SearchCustomerAccountResponse {

    @XmlElement(required = true)
    protected ResSearchCustomerAccount responseData;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ResSearchCustomerAccount }
     *     
     */
    public ResSearchCustomerAccount getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResSearchCustomerAccount }
     *     
     */
    public void setResponseData(ResSearchCustomerAccount value) {
        this.responseData = value;
    }

}
