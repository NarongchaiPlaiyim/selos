
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for UnderwritingApprovalRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnderwritingApprovalRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="application" type="{http://www.tmbbank.com/enterprise/model}ApplicationType"/>
 *         &lt;element name="stage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeOfRequest" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnderwritingApprovalRequestType", propOrder = {
    "application",
    "stage",
    "timeOfRequest"
})
public class UnderwritingApprovalRequestType {

    @XmlElement(required = true)
    protected ApplicationType application;
    protected String stage;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeOfRequest;

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationType }
     *     
     */
    public ApplicationType getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationType }
     *     
     */
    public void setApplication(ApplicationType value) {
        this.application = value;
    }

    /**
     * Gets the value of the stage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets the value of the stage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStage(String value) {
        this.stage = value;
    }

    /**
     * Gets the value of the timeOfRequest property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeOfRequest() {
        return timeOfRequest;
    }

    /**
     * Sets the value of the timeOfRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeOfRequest(XMLGregorianCalendar value) {
        this.timeOfRequest = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("application", application)
                .append("stage", stage)
                .append("timeOfRequest", timeOfRequest)
                .toString();
    }
}
