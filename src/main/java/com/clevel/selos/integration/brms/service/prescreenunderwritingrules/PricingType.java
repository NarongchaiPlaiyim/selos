
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PricingType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="PricingType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="projectCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baseRate" type="{http://www.tmbbank.com/enterprise/model}BaseRateType" minOccurs="0"/>
 *         &lt;element name="penaltyRate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="pricingSegment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pricingTier" type="{http://www.tmbbank.com/enterprise/model}PricingTierType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PricingType", propOrder = {
        "id",
        "productType",
        "projectCode",
        "baseRate",
        "penaltyRate",
        "pricingSegment",
        "pricingTier",
        "attribute"
})
public class PricingType {

    @XmlElement(name = "ID")
    protected String id;
    protected String productType;
    protected String projectCode;
    protected BaseRateType baseRate;
    protected BigDecimal penaltyRate;
    protected String pricingSegment;
    protected List<PricingTierType> pricingTier;
    protected List<AttributeType> attribute;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the productType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the projectCode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * Sets the value of the projectCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProjectCode(String value) {
        this.projectCode = value;
    }

    /**
     * Gets the value of the baseRate property.
     *
     * @return possible object is
     *         {@link BaseRateType }
     */
    public BaseRateType getBaseRate() {
        return baseRate;
    }

    /**
     * Sets the value of the baseRate property.
     *
     * @param value allowed object is
     *              {@link BaseRateType }
     */
    public void setBaseRate(BaseRateType value) {
        this.baseRate = value;
    }

    /**
     * Gets the value of the penaltyRate property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    /**
     * Sets the value of the penaltyRate property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setPenaltyRate(BigDecimal value) {
        this.penaltyRate = value;
    }

    /**
     * Gets the value of the pricingSegment property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPricingSegment() {
        return pricingSegment;
    }

    /**
     * Sets the value of the pricingSegment property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPricingSegment(String value) {
        this.pricingSegment = value;
    }

    /**
     * Gets the value of the pricingTier property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pricingTier property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPricingTier().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link PricingTierType }
     */
    public List<PricingTierType> getPricingTier() {
        if (pricingTier == null) {
            pricingTier = new ArrayList<PricingTierType>();
        }
        return this.pricingTier;
    }

    /**
     * Gets the value of the attribute property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     */
    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeType>();
        }
        return this.attribute;
    }

}
