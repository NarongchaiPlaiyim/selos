
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="exception" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "exception"
})
@XmlRootElement(name = "DecisionServiceException", namespace = "http://www.ilog.com/rules/DecisionService")
public class DecisionServiceException {

    @XmlElement(namespace = "http://www.ilog.com/rules/DecisionService", required = true)
    protected String exception;

    /**
     * Gets the value of the exception property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getException() {
        return exception;
    }

    /**
     * Sets the value of the exception property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setException(String value) {
        this.exception = value;
    }

}
