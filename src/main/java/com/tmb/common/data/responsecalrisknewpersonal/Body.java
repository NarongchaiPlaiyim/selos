
package com.tmb.common.data.responsecalrisknewpersonal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for body complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="body">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxRisk" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="maxRiskRM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="statusSWF" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sourceOfRisk" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCode1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resDesc1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCode2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resDesc2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCode3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resDesc3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCode4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resDesc4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="messageSection" type="{http://data.common.tmb.com/responseCalRiskNewPersonal}messageSection" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="warningListPersonSection" type="{http://data.common.tmb.com/responseCalRiskNewPersonal}warningListPersonSection" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "body", propOrder = {
    "maxRisk",
    "maxRiskRM",
    "statusSWF",
    "sourceOfRisk",
    "resCode1",
    "resDesc1",
    "resCode2",
    "resDesc2",
    "resCode3",
    "resDesc3",
    "resCode4",
    "resDesc4",
    "messageSection",
    "warningListPersonSection"
})
public class Body {

    @XmlElementRef(name = "maxRisk", type = JAXBElement.class)
    protected JAXBElement<String> maxRisk;
    @XmlElementRef(name = "maxRiskRM", type = JAXBElement.class)
    protected JAXBElement<String> maxRiskRM;
    @XmlElementRef(name = "statusSWF", type = JAXBElement.class)
    protected JAXBElement<String> statusSWF;
    @XmlElementRef(name = "sourceOfRisk", type = JAXBElement.class)
    protected JAXBElement<String> sourceOfRisk;
    @XmlElementRef(name = "resCode1", type = JAXBElement.class)
    protected JAXBElement<String> resCode1;
    @XmlElementRef(name = "resDesc1", type = JAXBElement.class)
    protected JAXBElement<String> resDesc1;
    @XmlElementRef(name = "resCode2", type = JAXBElement.class)
    protected JAXBElement<String> resCode2;
    @XmlElementRef(name = "resDesc2", type = JAXBElement.class)
    protected JAXBElement<String> resDesc2;
    @XmlElementRef(name = "resCode3", type = JAXBElement.class)
    protected JAXBElement<String> resCode3;
    @XmlElementRef(name = "resDesc3", type = JAXBElement.class)
    protected JAXBElement<String> resDesc3;
    @XmlElementRef(name = "resCode4", type = JAXBElement.class)
    protected JAXBElement<String> resCode4;
    @XmlElementRef(name = "resDesc4", type = JAXBElement.class)
    protected JAXBElement<String> resDesc4;
    protected List<MessageSection> messageSection;
    protected List<WarningListPersonSection> warningListPersonSection;

    /**
     * Gets the value of the maxRisk property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMaxRisk() {
        return maxRisk;
    }

    /**
     * Sets the value of the maxRisk property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMaxRisk(JAXBElement<String> value) {
        this.maxRisk = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the maxRiskRM property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMaxRiskRM() {
        return maxRiskRM;
    }

    /**
     * Sets the value of the maxRiskRM property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMaxRiskRM(JAXBElement<String> value) {
        this.maxRiskRM = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the statusSWF property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatusSWF() {
        return statusSWF;
    }

    /**
     * Sets the value of the statusSWF property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatusSWF(JAXBElement<String> value) {
        this.statusSWF = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sourceOfRisk property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSourceOfRisk() {
        return sourceOfRisk;
    }

    /**
     * Sets the value of the sourceOfRisk property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSourceOfRisk(JAXBElement<String> value) {
        this.sourceOfRisk = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResCode1() {
        return resCode1;
    }

    /**
     * Sets the value of the resCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResCode1(JAXBElement<String> value) {
        this.resCode1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resDesc1 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResDesc1() {
        return resDesc1;
    }

    /**
     * Sets the value of the resDesc1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResDesc1(JAXBElement<String> value) {
        this.resDesc1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resCode2 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResCode2() {
        return resCode2;
    }

    /**
     * Sets the value of the resCode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResCode2(JAXBElement<String> value) {
        this.resCode2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resDesc2 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResDesc2() {
        return resDesc2;
    }

    /**
     * Sets the value of the resDesc2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResDesc2(JAXBElement<String> value) {
        this.resDesc2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resCode3 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResCode3() {
        return resCode3;
    }

    /**
     * Sets the value of the resCode3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResCode3(JAXBElement<String> value) {
        this.resCode3 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resDesc3 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResDesc3() {
        return resDesc3;
    }

    /**
     * Sets the value of the resDesc3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResDesc3(JAXBElement<String> value) {
        this.resDesc3 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resCode4 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResCode4() {
        return resCode4;
    }

    /**
     * Sets the value of the resCode4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResCode4(JAXBElement<String> value) {
        this.resCode4 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the resDesc4 property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResDesc4() {
        return resDesc4;
    }

    /**
     * Sets the value of the resDesc4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResDesc4(JAXBElement<String> value) {
        this.resDesc4 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the messageSection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageSection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageSection }
     * 
     * 
     */
    public List<MessageSection> getMessageSection() {
        if (messageSection == null) {
            messageSection = new ArrayList<MessageSection>();
        }
        return this.messageSection;
    }

    /**
     * Gets the value of the warningListPersonSection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warningListPersonSection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarningListPersonSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WarningListPersonSection }
     * 
     * 
     */
    public List<WarningListPersonSection> getWarningListPersonSection() {
        if (warningListPersonSection == null) {
            warningListPersonSection = new ArrayList<WarningListPersonSection>();
        }
        return this.warningListPersonSection;
    }

}
