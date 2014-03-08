
package com.tmbbank.enterprise.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NCBReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NCBReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxNumHouseMortgageLoans" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ncbGenericIndividualBureauScore" type="{http://www.tmbbank.com/enterprise/model}NCBGenericIndividualBureauScoreType" minOccurs="0"/>
 *         &lt;element name="ncbAccount" type="{http://www.tmbbank.com/enterprise/model}NCBAccountType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ncbEnquiry" type="{http://www.tmbbank.com/enterprise/model}NCBEnquiryType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NCBReportType", propOrder = {
    "id",
    "maxNumHouseMortgageLoans",
    "ncbGenericIndividualBureauScore",
    "ncbAccount",
    "ncbEnquiry",
    "attribute"
})
public class NCBReportType {

    @XmlElement(name = "ID")
    protected String id;
    protected Integer maxNumHouseMortgageLoans;
    protected NCBGenericIndividualBureauScoreType ncbGenericIndividualBureauScore;
    protected List<NCBAccountType> ncbAccount;
    protected List<NCBEnquiryType> ncbEnquiry;
    protected List<AttributeType> attribute;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the maxNumHouseMortgageLoans property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxNumHouseMortgageLoans() {
        return maxNumHouseMortgageLoans;
    }

    /**
     * Sets the value of the maxNumHouseMortgageLoans property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxNumHouseMortgageLoans(Integer value) {
        this.maxNumHouseMortgageLoans = value;
    }

    /**
     * Gets the value of the ncbGenericIndividualBureauScore property.
     * 
     * @return
     *     possible object is
     *     {@link NCBGenericIndividualBureauScoreType }
     *     
     */
    public NCBGenericIndividualBureauScoreType getNcbGenericIndividualBureauScore() {
        return ncbGenericIndividualBureauScore;
    }

    /**
     * Sets the value of the ncbGenericIndividualBureauScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link NCBGenericIndividualBureauScoreType }
     *     
     */
    public void setNcbGenericIndividualBureauScore(NCBGenericIndividualBureauScoreType value) {
        this.ncbGenericIndividualBureauScore = value;
    }

    /**
     * Gets the value of the ncbAccount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ncbAccount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNcbAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NCBAccountType }
     * 
     * 
     */
    public List<NCBAccountType> getNcbAccount() {
        if (ncbAccount == null) {
            ncbAccount = new ArrayList<NCBAccountType>();
        }
        return this.ncbAccount;
    }

    /**
     * Gets the value of the ncbEnquiry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ncbEnquiry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNcbEnquiry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NCBEnquiryType }
     * 
     * 
     */
    public List<NCBEnquiryType> getNcbEnquiry() {
        if (ncbEnquiry == null) {
            ncbEnquiry = new ArrayList<NCBEnquiryType>();
        }
        return this.ncbEnquiry;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     * 
     * 
     */
    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeType>();
        }
        return this.attribute;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("maxNumHouseMortgageLoans", maxNumHouseMortgageLoans)
                .append("ncbGenericIndividualBureauScore", ncbGenericIndividualBureauScore)
                .append("ncbAccount", ncbAccount)
                .append("ncbEnquiry", ncbEnquiry)
                .append("attribute", attribute)
                .toString();
    }
}
