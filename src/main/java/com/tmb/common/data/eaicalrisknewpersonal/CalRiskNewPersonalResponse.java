
package com.tmb.common.data.eaicalrisknewpersonal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tmb.common.data.responsecalrisknewpersonal.ResCalRiskNewPersonal;


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
 *         &lt;element name="responseData" type="{http://data.common.tmb.com/responseCalRiskNewPersonal}resCalRiskNewPersonal"/>
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
@XmlRootElement(name = "calRiskNewPersonalResponse")
public class CalRiskNewPersonalResponse {

    @XmlElement(required = true)
    protected ResCalRiskNewPersonal responseData;

    /**
     * Gets the value of the responseData property.
     * 
     * @return
     *     possible object is
     *     {@link ResCalRiskNewPersonal }
     *     
     */
    public ResCalRiskNewPersonal getResponseData() {
        return responseData;
    }

    /**
     * Sets the value of the responseData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResCalRiskNewPersonal }
     *     
     */
    public void setResponseData(ResCalRiskNewPersonal value) {
        this.responseData = value;
    }

}