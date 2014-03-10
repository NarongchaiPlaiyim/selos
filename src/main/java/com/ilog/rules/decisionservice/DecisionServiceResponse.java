
package com.ilog.rules.decisionservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.ilog.rules.param.UnderwritingRequest;
import com.ilog.rules.param.UnderwritingResult;
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
 *         &lt;element name="ilog.rules.outputString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ilog.rules.firedRulesCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DecisionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.ilog.com/rules/param}underwritingRequest"/>
 *         &lt;element ref="{http://www.ilog.com/rules/param}underwritingResult"/>
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
    "ilogRulesOutputString",
    "ilogRulesFiredRulesCount",
    "decisionID",
    "underwritingRequest",
    "underwritingResult"
})
@XmlRootElement(name = "DecisionServiceResponse")
public class DecisionServiceResponse {

    @XmlElement(name = "ilog.rules.outputString", required = true)
    protected String ilogRulesOutputString;
    @XmlElement(name = "ilog.rules.firedRulesCount")
    protected int ilogRulesFiredRulesCount;
    @XmlElement(name = "DecisionID", required = true)
    protected String decisionID;
    @XmlElement(namespace = "http://www.ilog.com/rules/param", required = true)
    protected UnderwritingRequest underwritingRequest;
    @XmlElement(namespace = "http://www.ilog.com/rules/param", required = true)
    protected UnderwritingResult underwritingResult;

    /**
     * Gets the value of the ilogRulesOutputString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIlogRulesOutputString() {
        return ilogRulesOutputString;
    }

    /**
     * Sets the value of the ilogRulesOutputString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIlogRulesOutputString(String value) {
        this.ilogRulesOutputString = value;
    }

    /**
     * Gets the value of the ilogRulesFiredRulesCount property.
     * 
     */
    public int getIlogRulesFiredRulesCount() {
        return ilogRulesFiredRulesCount;
    }

    /**
     * Sets the value of the ilogRulesFiredRulesCount property.
     * 
     */
    public void setIlogRulesFiredRulesCount(int value) {
        this.ilogRulesFiredRulesCount = value;
    }

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

    /**
     * Gets the value of the underwritingResult property.
     * 
     * @return
     *     possible object is
     *     {@link UnderwritingResult }
     *     
     */
    public UnderwritingResult getUnderwritingResult() {
        return underwritingResult;
    }

    /**
     * Sets the value of the underwritingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnderwritingResult }
     *     
     */
    public void setUnderwritingResult(UnderwritingResult value) {
        this.underwritingResult = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ilogRulesOutputString", ilogRulesOutputString)
                .append("ilogRulesFiredRulesCount", ilogRulesFiredRulesCount)
                .append("decisionID", decisionID)
                .append("underwritingRequest", underwritingRequest)
                .append("underwritingResult", underwritingResult)
                .toString();
    }
}
