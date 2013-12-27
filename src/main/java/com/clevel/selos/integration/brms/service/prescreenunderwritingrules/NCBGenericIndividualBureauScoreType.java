
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for NCBGenericIndividualBureauScoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NCBGenericIndividualBureauScoreType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scoreName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scoreVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scoreSegment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recaliberationVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scoreDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="scoreGrade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="odds" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="reasonCode1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reasonCode2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reasonCode3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reasonCode4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reasonCode5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enquiryControlNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "NCBGenericIndividualBureauScoreType", propOrder = {
    "id",
    "scoreName",
    "scoreVersion",
    "scoreSegment",
    "recaliberationVersion",
    "scoreDate",
    "score",
    "scoreGrade",
    "odds",
    "reasonCode1",
    "reasonCode2",
    "reasonCode3",
    "reasonCode4",
    "reasonCode5",
    "errorCode",
    "enquiryControlNumber",
    "attribute"
})
public class NCBGenericIndividualBureauScoreType {

    @XmlElement(name = "ID")
    protected String id;
    protected String scoreName;
    protected String scoreVersion;
    protected String scoreSegment;
    protected String recaliberationVersion;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar scoreDate;
    protected BigDecimal score;
    protected String scoreGrade;
    protected BigDecimal odds;
    protected String reasonCode1;
    protected String reasonCode2;
    protected String reasonCode3;
    protected String reasonCode4;
    protected String reasonCode5;
    protected String errorCode;
    protected Integer enquiryControlNumber;
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
     * Gets the value of the scoreName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreName() {
        return scoreName;
    }

    /**
     * Sets the value of the scoreName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreName(String value) {
        this.scoreName = value;
    }

    /**
     * Gets the value of the scoreVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreVersion() {
        return scoreVersion;
    }

    /**
     * Sets the value of the scoreVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreVersion(String value) {
        this.scoreVersion = value;
    }

    /**
     * Gets the value of the scoreSegment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreSegment() {
        return scoreSegment;
    }

    /**
     * Sets the value of the scoreSegment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreSegment(String value) {
        this.scoreSegment = value;
    }

    /**
     * Gets the value of the recaliberationVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecaliberationVersion() {
        return recaliberationVersion;
    }

    /**
     * Sets the value of the recaliberationVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecaliberationVersion(String value) {
        this.recaliberationVersion = value;
    }

    /**
     * Gets the value of the scoreDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getScoreDate() {
        return scoreDate;
    }

    /**
     * Sets the value of the scoreDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setScoreDate(XMLGregorianCalendar value) {
        this.scoreDate = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setScore(BigDecimal value) {
        this.score = value;
    }

    /**
     * Gets the value of the scoreGrade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScoreGrade() {
        return scoreGrade;
    }

    /**
     * Sets the value of the scoreGrade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScoreGrade(String value) {
        this.scoreGrade = value;
    }

    /**
     * Gets the value of the odds property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOdds() {
        return odds;
    }

    /**
     * Sets the value of the odds property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOdds(BigDecimal value) {
        this.odds = value;
    }

    /**
     * Gets the value of the reasonCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode1() {
        return reasonCode1;
    }

    /**
     * Sets the value of the reasonCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode1(String value) {
        this.reasonCode1 = value;
    }

    /**
     * Gets the value of the reasonCode2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode2() {
        return reasonCode2;
    }

    /**
     * Sets the value of the reasonCode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode2(String value) {
        this.reasonCode2 = value;
    }

    /**
     * Gets the value of the reasonCode3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode3() {
        return reasonCode3;
    }

    /**
     * Sets the value of the reasonCode3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode3(String value) {
        this.reasonCode3 = value;
    }

    /**
     * Gets the value of the reasonCode4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode4() {
        return reasonCode4;
    }

    /**
     * Sets the value of the reasonCode4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode4(String value) {
        this.reasonCode4 = value;
    }

    /**
     * Gets the value of the reasonCode5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode5() {
        return reasonCode5;
    }

    /**
     * Sets the value of the reasonCode5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode5(String value) {
        this.reasonCode5 = value;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the enquiryControlNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEnquiryControlNumber() {
        return enquiryControlNumber;
    }

    /**
     * Sets the value of the enquiryControlNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEnquiryControlNumber(Integer value) {
        this.enquiryControlNumber = value;
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

}
