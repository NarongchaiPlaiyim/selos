
package com.clevel.selos.integration.brms.service.document.apprisalrules;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for RiskModelType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="RiskModelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modelID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="riskClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pd" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="tmbMasterScale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="percentNotCured" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dateOfScore" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskModelType", propOrder = {
        "id",
        "modelID",
        "name",
        "riskClass",
        "pd",
        "tmbMasterScale",
        "color",
        "result",
        "percentNotCured",
        "dateOfScore",
        "score",
        "attribute"
})
public class RiskModelType {

    @XmlElement(name = "ID")
    protected String id;
    protected String modelID;
    protected String name;
    protected String riskClass;
    protected BigDecimal pd;
    protected String tmbMasterScale;
    protected String color;
    protected String result;
    protected BigDecimal percentNotCured;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfScore;
    protected List<ScoreType> score;
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
     * Gets the value of the modelID property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getModelID() {
        return modelID;
    }

    /**
     * Sets the value of the modelID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModelID(String value) {
        this.modelID = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the pd property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getPd() {
        return pd;
    }

    /**
     * Sets the value of the pd property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setPd(BigDecimal value) {
        this.pd = value;
    }

    /**
     * Gets the value of the tmbMasterScale property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTmbMasterScale() {
        return tmbMasterScale;
    }

    /**
     * Sets the value of the tmbMasterScale property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTmbMasterScale(String value) {
        this.tmbMasterScale = value;
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
     * Gets the value of the percentNotCured property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getPercentNotCured() {
        return percentNotCured;
    }

    /**
     * Sets the value of the percentNotCured property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setPercentNotCured(BigDecimal value) {
        this.percentNotCured = value;
    }

    /**
     * Gets the value of the dateOfScore property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDateOfScore() {
        return dateOfScore;
    }

    /**
     * Sets the value of the dateOfScore property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDateOfScore(XMLGregorianCalendar value) {
        this.dateOfScore = value;
    }

    /**
     * Gets the value of the score property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the score property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScore().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ScoreType }
     */
    public List<ScoreType> getScore() {
        if (score == null) {
            score = new ArrayList<ScoreType>();
        }
        return this.score;
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
