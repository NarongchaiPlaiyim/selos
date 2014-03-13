
package com.ilog.rules.decisionservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.ilog.rules.param.UnderwritingRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


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
 *         &lt;element name="DecisionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.ilog.com/rules/param}underwritingRequest"/>
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
    "decisionID",
    "underwritingRequest"
})
@XmlRootElement(name = "DecisionServiceRequest")
public class DecisionServiceRequest {

    @XmlElement(name = "DecisionID")
    protected String decisionID;
    @XmlElement(namespace = "http://www.ilog.com/rules/param", required = true)
    protected UnderwritingRequest underwritingRequest;

    /**
     * Gets the value of the decisionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecisionID() {
        return decisionID;
    }

    /**
     * Sets the value of the decisionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecisionID(String value) {
        this.decisionID = value;
    }

    /**
     * Gets the value of the underwritingRequest property.
     * 
     * @return
     *     possible object is
     *     {@link UnderwritingRequest }
     *     
     */
    public UnderwritingRequest getUnderwritingRequest() {
        return underwritingRequest;
    }

    /**
     * Sets the value of the underwritingRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnderwritingRequest }
     *     
     */
    public void setUnderwritingRequest(UnderwritingRequest value) {
        this.underwritingRequest = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("decisionID", decisionID)
                .append("underwritingRequest", underwritingRequest)
                .toString();
    }
}
