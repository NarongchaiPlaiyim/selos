
package com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SMEBehavioralScoreResultType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SMEBehavioralScoreResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="timeOfResult" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="totalScore" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="riskClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="probabilityOfDefault" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="masterScale" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validFrom" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="validTo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.tmbbank.com/enterprise/model}ResultType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMEBehavioralScoreResultType", propOrder = {
        "timeOfResult",
        "totalScore",
        "riskClass",
        "probabilityOfDefault",
        "masterScale",
        "color",
        "result",
        "validFrom",
        "validTo",
        "message"
})
public class SMEBehavioralScoreResultType {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeOfResult;
    protected Integer totalScore;
    protected String riskClass;
    protected BigDecimal probabilityOfDefault;
    protected BigDecimal masterScale;
    protected String color;
    protected String result;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar validFrom;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar validTo;
    protected List<ResultType> message;

    /**
     * Gets the value of the timeOfResult property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimeOfResult() {
        return timeOfResult;
    }

    /**
     * Sets the value of the timeOfResult property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setTimeOfResult(XMLGregorianCalendar value) {
        this.timeOfResult = value;
    }

    /**
     * Gets the value of the totalScore property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the value of the totalScore property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setTotalScore(Integer value) {
        this.totalScore = value;
    }

    /**
     * Gets the value of the riskClass property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRiskClass() {
        return riskClass;
    }

    /**
     * Sets the value of the riskClass property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRiskClass(String value) {
        this.riskClass = value;
    }

    /**
     * Gets the value of the probabilityOfDefault property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getProbabilityOfDefault() {
        return probabilityOfDefault;
    }

    /**
     * Sets the value of the probabilityOfDefault property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setProbabilityOfDefault(BigDecimal value) {
        this.probabilityOfDefault = value;
    }

    /**
     * Gets the value of the masterScale property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMasterScale() {
        return masterScale;
    }

    /**
     * Sets the value of the masterScale property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMasterScale(BigDecimal value) {
        this.masterScale = value;
    }

    /**
     * Gets the value of the color property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the result property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Gets the value of the validFrom property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the value of the validFrom property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setValidFrom(XMLGregorianCalendar value) {
        this.validFrom = value;
    }

    /**
     * Gets the value of the validTo property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getValidTo() {
        return validTo;
    }

    /**
     * Sets the value of the validTo property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setValidTo(XMLGregorianCalendar value) {
        this.validTo = value;
    }

    /**
     * Gets the value of the message property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultType }
     */
    public List<ResultType> getMessage() {
        if (message == null) {
            message = new ArrayList<ResultType>();
        }
        return this.message;
    }

}
