
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SMEBehavioralScoreRequestType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SMEBehavioralScoreRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType"/>
 *         &lt;element name="timeOfRequest" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMEBehavioralScoreRequestType", propOrder = {
        "borrower",
        "timeOfRequest"
})
public class SMEBehavioralScoreRequestType {

    @XmlElement(required = true)
    protected BorrowerType borrower;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeOfRequest;

    /**
     * Gets the value of the borrower property.
     *
     * @return possible object is
     *         {@link BorrowerType }
     */
    public BorrowerType getBorrower() {
        return borrower;
    }

    /**
     * Sets the value of the borrower property.
     *
     * @param value allowed object is
     *              {@link BorrowerType }
     */
    public void setBorrower(BorrowerType value) {
        this.borrower = value;
    }

    /**
     * Gets the value of the timeOfRequest property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimeOfRequest() {
        return timeOfRequest;
    }

    /**
     * Sets the value of the timeOfRequest property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setTimeOfRequest(XMLGregorianCalendar value) {
        this.timeOfRequest = value;
    }

}
