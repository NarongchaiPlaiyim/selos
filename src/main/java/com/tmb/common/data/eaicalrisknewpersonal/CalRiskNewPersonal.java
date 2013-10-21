
package com.tmb.common.data.eaicalrisknewpersonal;

import com.tmb.common.data.requestcalrisknewpersonal.ReqCalRiskNewPersonal;

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
 *         &lt;element name="requestData" type="{http://data.common.tmb.com/requestCalRiskNewPersonal}reqCalRiskNewPersonal"/>
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
@XmlRootElement(name = "calRiskNewPersonal")
public class CalRiskNewPersonal {

    @XmlElement(required = true)
    protected ReqCalRiskNewPersonal requestData;

    /**
     * Gets the value of the requestData property.
     * 
     * @return
     *     possible object is
     *     {@link ReqCalRiskNewPersonal }
     *     
     */
    public ReqCalRiskNewPersonal getRequestData() {
        return requestData;
    }

    /**
     * Sets the value of the requestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqCalRiskNewPersonal }
     *     
     */
    public void setRequestData(ReqCalRiskNewPersonal value) {
        this.requestData = value;
    }

}
