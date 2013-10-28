
package com.clevel.selos.integration.brms.service.standardpricing.feerules;

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
 *         &lt;element name="DecisionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.ilog.com/rules/param}underwritingRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "decisionID",
        "underwritingRequest"
})
@XmlRootElement(name = "DecisionServiceRequest", namespace = "http://www.ilog.com/rules/DecisionService")
public class DecisionServiceRequest {

    @XmlElement(name = "DecisionID", namespace = "http://www.ilog.com/rules/DecisionService")
    protected String decisionID;
    @XmlElement(namespace = "http://www.ilog.com/rules/param", required = true)
    protected UnderwritingRequest underwritingRequest;

    /**
     * Gets the value of the decisionID property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDecisionID() {
        return decisionID;
    }

    /**
     * Sets the value of the decisionID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDecisionID(String value) {
        this.decisionID = value;
    }

    /**
     * Gets the value of the underwritingRequest property.
     *
     * @return possible object is
     *         {@link UnderwritingRequest }
     */
    public UnderwritingRequest getUnderwritingRequest() {
        return underwritingRequest;
    }

    /**
     * Sets the value of the underwritingRequest property.
     *
     * @param value allowed object is
     *              {@link UnderwritingRequest }
     */
    public void setUnderwritingRequest(UnderwritingRequest value) {
        this.underwritingRequest = value;
    }

}
