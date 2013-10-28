
package com.clevel.selos.integration.brms.service.standardpricing.interestrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ResultType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkingCriteria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="screen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messageDisplay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="underwritingAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stringValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numericValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ruleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultType", propOrder = {
        "id",
        "code",
        "type",
        "description",
        "severity",
        "checkingCriteria",
        "screen",
        "field",
        "messageDisplay",
        "underwritingAction",
        "stringValue",
        "numericValue",
        "color",
        "ruleName",
        "attribute"
})
public class ResultType {

    @XmlElement(name = "ID")
    protected String id;
    protected String code;
    protected String type;
    protected String description;
    protected String severity;
    protected String checkingCriteria;
    protected String screen;
    protected String field;
    protected String messageDisplay;
    protected String underwritingAction;
    protected String stringValue;
    protected BigDecimal numericValue;
    protected String color;
    protected String ruleName;
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
     * Gets the value of the code property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the severity property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSeverity(String value) {
        this.severity = value;
    }

    /**
     * Gets the value of the checkingCriteria property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCheckingCriteria() {
        return checkingCriteria;
    }

    /**
     * Sets the value of the checkingCriteria property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCheckingCriteria(String value) {
        this.checkingCriteria = value;
    }

    /**
     * Gets the value of the screen property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getScreen() {
        return screen;
    }

    /**
     * Sets the value of the screen property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setScreen(String value) {
        this.screen = value;
    }

    /**
     * Gets the value of the field property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the value of the field property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setField(String value) {
        this.field = value;
    }

    /**
     * Gets the value of the messageDisplay property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMessageDisplay() {
        return messageDisplay;
    }

    /**
     * Sets the value of the messageDisplay property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageDisplay(String value) {
        this.messageDisplay = value;
    }

    /**
     * Gets the value of the underwritingAction property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUnderwritingAction() {
        return underwritingAction;
    }

    /**
     * Sets the value of the underwritingAction property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUnderwritingAction(String value) {
        this.underwritingAction = value;
    }

    /**
     * Gets the value of the stringValue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the value of the stringValue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStringValue(String value) {
        this.stringValue = value;
    }

    /**
     * Gets the value of the numericValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getNumericValue() {
        return numericValue;
    }

    /**
     * Sets the value of the numericValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setNumericValue(BigDecimal value) {
        this.numericValue = value;
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
     * Gets the value of the ruleName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets the value of the ruleName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRuleName(String value) {
        this.ruleName = value;
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
