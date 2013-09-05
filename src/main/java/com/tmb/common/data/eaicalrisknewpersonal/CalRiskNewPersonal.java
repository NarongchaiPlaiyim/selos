
package com.tmb.common.data.eaicalrisknewpersonal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tmb.common.data.requestcalrisknewpersonal.ReqCalRiskNewPersonal;


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
