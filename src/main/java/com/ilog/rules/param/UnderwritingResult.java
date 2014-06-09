
package com.ilog.rules.param;

import com.tmbbank.enterprise.model.UnderwritingApprovalResultType;
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
 *         &lt;element ref="{http://www.tmbbank.com/enterprise/model}underwritingApprovalResult"/>
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
    "underwritingApprovalResult"
})
@XmlRootElement(name = "underwritingResult")
public class UnderwritingResult {

    @XmlElement(namespace = "http://www.tmbbank.com/enterprise/model", required = true)
    protected UnderwritingApprovalResultType underwritingApprovalResult;

    /**
     * Gets the value of the underwritingApprovalResult property.
     * 
     * @return
     *     possible object is
     *     {@link UnderwritingApprovalResultType }
     *     
     */
    public UnderwritingApprovalResultType getUnderwritingApprovalResult() {
        return underwritingApprovalResult;
    }

    /**
     * Sets the value of the underwritingApprovalResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnderwritingApprovalResultType }
     *     
     */
    public void setUnderwritingApprovalResult(UnderwritingApprovalResultType value) {
        this.underwritingApprovalResult = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("underwritingApprovalResult", underwritingApprovalResult)
                .toString();
    }
}
