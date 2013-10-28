
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;


/**
 * <p>Java class for AttributeType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="AttributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stringValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numericValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="dateTimeValue" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="booleanValue" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeType", propOrder = {
        "name",
        "stringValue",
        "numericValue",
        "dateTimeValue",
        "booleanValue"
})
public class AttributeType {

    protected String name;
    protected String stringValue;
    protected BigDecimal numericValue;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTimeValue;
    protected Boolean booleanValue;

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
     * Gets the value of the dateTimeValue property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDateTimeValue() {
        return dateTimeValue;
    }

    /**
     * Sets the value of the dateTimeValue property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDateTimeValue(XMLGregorianCalendar value) {
        this.dateTimeValue = value;
    }

    /**
     * Gets the value of the booleanValue property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isBooleanValue() {
        return booleanValue;
    }

    /**
     * Sets the value of the booleanValue property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setBooleanValue(Boolean value) {
        this.booleanValue = value;
    }

}
