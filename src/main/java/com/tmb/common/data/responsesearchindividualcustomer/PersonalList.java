
package com.tmb.common.data.responsesearchindividualcustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for personalList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personalList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custNbr1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="title1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="custId1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="citizenId1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl11">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl21">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl31">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl4">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="address1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="type1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dateOfBirth1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personalList", propOrder = {
    "custNbr1",
    "title1",
    "name1",
    "custId1",
    "citizenId1",
    "ctl11",
    "ctl21",
    "ctl31",
    "ctl4",
    "address1",
    "type1",
    "telephoneNumber1",
    "dateOfBirth1"
})
public class PersonalList {

    @XmlElement(required = true)
    protected String custNbr1;
    protected String title1;
    @XmlElement(required = true)
    protected String name1;
    @XmlElement(required = true)
    protected String custId1;
    @XmlElement(required = true)
    protected String citizenId1;
    @XmlElement(required = true)
    protected String ctl11;
    @XmlElement(required = true)
    protected String ctl21;
    @XmlElement(required = true)
    protected String ctl31;
    @XmlElement(required = true)
    protected String ctl4;
    @XmlElement(required = true)
    protected String address1;
    protected String type1;
    protected String telephoneNumber1;
    @XmlElement(required = true)
    protected String dateOfBirth1;

    /**
     * Gets the value of the custNbr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustNbr1() {
        return custNbr1;
    }

    /**
     * Sets the value of the custNbr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustNbr1(String value) {
        this.custNbr1 = value;
    }

    /**
     * Gets the value of the title1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle1() {
        return title1;
    }

    /**
     * Sets the value of the title1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle1(String value) {
        this.title1 = value;
    }

    /**
     * Gets the value of the name1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName1() {
        return name1;
    }

    /**
     * Sets the value of the name1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName1(String value) {
        this.name1 = value;
    }

    /**
     * Gets the value of the custId1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustId1() {
        return custId1;
    }

    /**
     * Sets the value of the custId1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustId1(String value) {
        this.custId1 = value;
    }

    /**
     * Gets the value of the citizenId1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenId1() {
        return citizenId1;
    }

    /**
     * Sets the value of the citizenId1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenId1(String value) {
        this.citizenId1 = value;
    }

    /**
     * Gets the value of the ctl11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtl11() {
        return ctl11;
    }

    /**
     * Sets the value of the ctl11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtl11(String value) {
        this.ctl11 = value;
    }

    /**
     * Gets the value of the ctl21 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtl21() {
        return ctl21;
    }

    /**
     * Sets the value of the ctl21 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtl21(String value) {
        this.ctl21 = value;
    }

    /**
     * Gets the value of the ctl31 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtl31() {
        return ctl31;
    }

    /**
     * Sets the value of the ctl31 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtl31(String value) {
        this.ctl31 = value;
    }

    /**
     * Gets the value of the ctl4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtl4() {
        return ctl4;
    }

    /**
     * Sets the value of the ctl4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtl4(String value) {
        this.ctl4 = value;
    }

    /**
     * Gets the value of the address1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the value of the address1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress1(String value) {
        this.address1 = value;
    }

    /**
     * Gets the value of the type1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType1() {
        return type1;
    }

    /**
     * Sets the value of the type1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType1(String value) {
        this.type1 = value;
    }

    /**
     * Gets the value of the telephoneNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber1() {
        return telephoneNumber1;
    }

    /**
     * Sets the value of the telephoneNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber1(String value) {
        this.telephoneNumber1 = value;
    }

    /**
     * Gets the value of the dateOfBirth1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth1() {
        return dateOfBirth1;
    }

    /**
     * Sets the value of the dateOfBirth1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth1(String value) {
        this.dateOfBirth1 = value;
    }

}
