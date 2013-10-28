
package com.tmb.sme.data.responsesearchcorporatecustomer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for corporateList complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="corporateList">
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
 *         &lt;element name="cId1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="citizenCId1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="title1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name11">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
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
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl41">
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
 *         &lt;element name="estDate1">
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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "corporateList", propOrder = {
        "custNbr1",
        "cId1",
        "citizenCId1",
        "title1",
        "name11",
        "ctl11",
        "ctl21",
        "ctl31",
        "ctl41",
        "address1",
        "type1",
        "telephoneNumber1",
        "estDate1"
})
public class CorporateList {

    @XmlElement(required = true)
    protected String custNbr1;
    @XmlElement(required = true)
    protected String cId1;
    @XmlElement(required = true)
    protected String citizenCId1;
    @XmlElement(required = true)
    protected String title1;
    @XmlElement(required = true)
    protected String name11;
    @XmlElement(required = true)
    protected String ctl11;
    @XmlElement(required = true)
    protected String ctl21;
    @XmlElement(required = true)
    protected String ctl31;
    @XmlElement(required = true)
    protected String ctl41;
    @XmlElement(required = true)
    protected String address1;
    protected String type1;
    protected String telephoneNumber1;
    @XmlElement(required = true)
    protected String estDate1;

    /**
     * Gets the value of the custNbr1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCustNbr1() {
        return custNbr1;
    }

    /**
     * Sets the value of the custNbr1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustNbr1(String value) {
        this.custNbr1 = value;
    }

    /**
     * Gets the value of the cId1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCId1() {
        return cId1;
    }

    /**
     * Sets the value of the cId1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCId1(String value) {
        this.cId1 = value;
    }

    /**
     * Gets the value of the citizenCId1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCitizenCId1() {
        return citizenCId1;
    }

    /**
     * Sets the value of the citizenCId1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCitizenCId1(String value) {
        this.citizenCId1 = value;
    }

    /**
     * Gets the value of the title1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTitle1() {
        return title1;
    }

    /**
     * Sets the value of the title1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle1(String value) {
        this.title1 = value;
    }

    /**
     * Gets the value of the name11 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName11() {
        return name11;
    }

    /**
     * Sets the value of the name11 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName11(String value) {
        this.name11 = value;
    }

    /**
     * Gets the value of the ctl11 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl11() {
        return ctl11;
    }

    /**
     * Sets the value of the ctl11 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl11(String value) {
        this.ctl11 = value;
    }

    /**
     * Gets the value of the ctl21 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl21() {
        return ctl21;
    }

    /**
     * Sets the value of the ctl21 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl21(String value) {
        this.ctl21 = value;
    }

    /**
     * Gets the value of the ctl31 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl31() {
        return ctl31;
    }

    /**
     * Sets the value of the ctl31 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl31(String value) {
        this.ctl31 = value;
    }

    /**
     * Gets the value of the ctl41 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl41() {
        return ctl41;
    }

    /**
     * Sets the value of the ctl41 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl41(String value) {
        this.ctl41 = value;
    }

    /**
     * Gets the value of the address1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the value of the address1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddress1(String value) {
        this.address1 = value;
    }

    /**
     * Gets the value of the type1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType1() {
        return type1;
    }

    /**
     * Sets the value of the type1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType1(String value) {
        this.type1 = value;
    }

    /**
     * Gets the value of the telephoneNumber1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber1() {
        return telephoneNumber1;
    }

    /**
     * Sets the value of the telephoneNumber1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber1(String value) {
        this.telephoneNumber1 = value;
    }

    /**
     * Gets the value of the estDate1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEstDate1() {
        return estDate1;
    }

    /**
     * Sets the value of the estDate1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEstDate1(String value) {
        this.estDate1 = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("custNbr1", custNbr1)
                .append("cId1", cId1)
                .append("citizenCId1", citizenCId1)
                .append("title1", title1)
                .append("name11", name11)
                .append("ctl11", ctl11)
                .append("ctl21", ctl21)
                .append("ctl31", ctl31)
                .append("ctl41", ctl41)
                .append("address1", address1)
                .append("type1", type1)
                .append("telephoneNumber1", telephoneNumber1)
                .append("estDate1", estDate1)
                .toString();
    }
}
