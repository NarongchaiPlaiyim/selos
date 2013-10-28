
package com.clevel.selos.integration.brms.service.document.customerrules;

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
 *         &lt;element name="ilog.rules.outputString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ilog.rules.firedRulesCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DecisionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.ilog.com/rules/param}underwritingRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "ilogRulesOutputString",
        "ilogRulesFiredRulesCount",
        "decisionID",
        "underwritingRequest"
})
@XmlRootElement(name = "DecisionServiceResponse", namespace = "http://www.ilog.com/rules/DecisionService")
public class DecisionServiceResponse {

    @XmlElement(name = "ilog.rules.outputString", namespace = "http://www.ilog.com/rules/DecisionService", required = true)
    protected String ilogRulesOutputString;
    @XmlElement(name = "ilog.rules.firedRulesCount", namespace = "http://www.ilog.com/rules/DecisionService")
    protected int ilogRulesFiredRulesCount;
    @XmlElement(name = "DecisionID", namespace = "http://www.ilog.com/rules/DecisionService", required = true)
    protected String decisionID;
    @XmlElement(namespace = "http://www.ilog.com/rules/param", required = true)
    protected UnderwritingRequest underwritingRequest;

    /**
     * Gets the value of the ilogRulesOutputString property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIlogRulesOutputString() {
        return ilogRulesOutputString;
    }

    /**
     * Sets the value of the ilogRulesOutputString property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIlogRulesOutputString(String value) {
        this.ilogRulesOutputString = value;
    }

    /**
     * Gets the value of the ilogRulesFiredRulesCount property.
     */
    public int getIlogRulesFiredRulesCount() {
        return ilogRulesFiredRulesCount;
    }

    /**
     * Sets the value of the ilogRulesFiredRulesCount property.
     */
    public void setIlogRulesFiredRulesCount(int value) {
        this.ilogRulesFiredRulesCount = value;
    }

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