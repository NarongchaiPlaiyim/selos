
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
 *         &lt;element ref="{http://www.tmbbank.com/enterprise/model}underwritingApprovalRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "underwritingApprovalRequest"
})
@XmlRootElement(name = "underwritingRequest", namespace = "http://www.ilog.com/rules/param")
public class UnderwritingRequest {

    @XmlElement(namespace = "http://www.tmbbank.com/enterprise/model", required = true)
    protected UnderwritingApprovalRequestType underwritingApprovalRequest;

    /**
     * Gets the value of the underwritingApprovalRequest property.
     *
     * @return possible object is
     *         {@link UnderwritingApprovalRequestType }
     */
    public UnderwritingApprovalRequestType getUnderwritingApprovalRequest() {
        return underwritingApprovalRequest;
    }

    /**
     * Sets the value of the underwritingApprovalRequest property.
     *
     * @param value allowed object is
     *              {@link UnderwritingApprovalRequestType }
     */
    public void setUnderwritingApprovalRequest(UnderwritingApprovalRequestType value) {
        this.underwritingApprovalRequest = value;
    }

}