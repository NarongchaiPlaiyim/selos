
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

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
 *         &lt;element ref="{http://www.tmbbank.com/enterprise/model}underwritingApprovalResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "underwritingApprovalResult"
})
@XmlRootElement(name = "underwritingResult", namespace = "http://www.ilog.com/rules/param")
public class UnderwritingResult {

    @XmlElement(namespace = "http://www.tmbbank.com/enterprise/model", required = true)
    protected UnderwritingApprovalResultType underwritingApprovalResult;

    /**
     * Gets the value of the underwritingApprovalResult property.
     *
     * @return possible object is
     *         {@link UnderwritingApprovalResultType }
     */
    public UnderwritingApprovalResultType getUnderwritingApprovalResult() {
        return underwritingApprovalResult;
    }

    /**
     * Sets the value of the underwritingApprovalResult property.
     *
     * @param value allowed object is
     *              {@link UnderwritingApprovalResultType }
     */
    public void setUnderwritingApprovalResult(UnderwritingApprovalResultType value) {
        this.underwritingApprovalResult = value;
    }

}
