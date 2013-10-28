
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ApplicationModelRiskScoringType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ApplicationModelRiskScoringType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="end" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="application" type="{http://www.tmbbank.com/enterprise/model}ApplicationType" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.tmbbank.com/enterprise/model}MessageType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationModelRiskScoringType", propOrder = {
        "start",
        "end",
        "application",
        "message"
})
public class ApplicationModelRiskScoringType {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar start;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar end;
    protected ApplicationType application;
    protected List<MessageType> message;

    /**
     * Gets the value of the start property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setStart(XMLGregorianCalendar value) {
        this.start = value;
    }

    /**
     * Gets the value of the end property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getEnd() {
        return end;
    }

    /**
     * Sets the value of the end property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setEnd(XMLGregorianCalendar value) {
        this.end = value;
    }

    /**
     * Gets the value of the application property.
     *
     * @return possible object is
     *         {@link ApplicationType }
     */
    public ApplicationType getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     *
     * @param value allowed object is
     *              {@link ApplicationType }
     */
    public void setApplication(ApplicationType value) {
        this.application = value;
    }

    /**
     * Gets the value of the message property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageType }
     */
    public List<MessageType> getMessage() {
        if (message == null) {
            message = new ArrayList<MessageType>();
        }
        return this.message;
    }

}
