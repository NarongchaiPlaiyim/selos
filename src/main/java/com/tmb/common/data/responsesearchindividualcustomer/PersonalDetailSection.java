
package com.tmb.common.data.responsesearchindividualcustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for personalDetailSection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personalDetailSection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personalDetail" type="{http://data.common.tmb.com/responseSearchIndividualCustomer}personalDetail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personalDetailSection", propOrder = {
    "personalDetail"
})
public class PersonalDetailSection {

    @XmlElement(required = true)
    protected PersonalDetail personalDetail;

    /**
     * Gets the value of the personalDetail property.
     * 
     * @return
     *     possible object is
     *     {@link PersonalDetail }
     *     
     */
    public PersonalDetail getPersonalDetail() {
        return personalDetail;
    }

    /**
     * Sets the value of the personalDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalDetail }
     *     
     */
    public void setPersonalDetail(PersonalDetail value) {
        this.personalDetail = value;
    }

}
