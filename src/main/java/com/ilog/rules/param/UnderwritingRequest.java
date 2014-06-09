
package com.ilog.rules.param;

import com.tmbbank.enterprise.model.UnderwritingApprovalRequestType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;


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
 *         &lt;element ref="{http://www.tmbbank.com/enterprise/model}underwritingApprovalRequest"/>
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
    "underwritingApprovalRequest"
})
@XmlRootElement(name = "underwritingRequest")
public class UnderwritingRequest {

    @XmlElement(namespace = "http://www.tmbbank.com/enterprise/model", required = true)
    protected UnderwritingApprovalRequestType underwritingApprovalRequest;

    /**
     * Gets the value of the underwritingApprovalRequest property.
     * 
     * @return
     *     possible object is
     *     {@link UnderwritingApprovalRequestType }
     *     
     */
    public UnderwritingApprovalRequestType getUnderwritingApprovalRequest() {
        return underwritingApprovalRequest;
    }

    /**
     * Sets the value of the underwritingApprovalRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnderwritingApprovalRequestType }
     *     
     */
    public void setUnderwritingApprovalRequest(UnderwritingApprovalRequestType value) {
        this.underwritingApprovalRequest = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("underwritingApprovalRequest", underwritingApprovalRequest)
                .toString();
    }
}
