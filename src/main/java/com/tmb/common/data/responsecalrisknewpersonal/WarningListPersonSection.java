
package com.tmb.common.data.responsecalrisknewpersonal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for warningListPersonSection complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="warningListPersonSection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fullName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="901"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cardId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="riskLevel" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="remark" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="500"/>
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
@XmlType(name = "warningListPersonSection", propOrder = {
        "fullName",
        "cardId",
        "riskLevel",
        "remark"
})
public class WarningListPersonSection {

    @XmlElementRef(name = "fullName", type = JAXBElement.class)
    protected JAXBElement<String> fullName;
    @XmlElementRef(name = "cardId", type = JAXBElement.class)
    protected JAXBElement<String> cardId;
    @XmlElementRef(name = "riskLevel", type = JAXBElement.class)
    protected JAXBElement<String> riskLevel;
    @XmlElementRef(name = "remark", type = JAXBElement.class)
    protected JAXBElement<String> remark;

    /**
     * Gets the value of the fullName property.
     *
     * @return possible object is
     *         {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setFullName(JAXBElement<String> value) {
        this.fullName = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the cardId property.
     *
     * @return possible object is
     *         {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getCardId() {
        return cardId;
    }

    /**
     * Sets the value of the cardId property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setCardId(JAXBElement<String> value) {
        this.cardId = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the riskLevel property.
     *
     * @return possible object is
     *         {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getRiskLevel() {
        return riskLevel;
    }

    /**
     * Sets the value of the riskLevel property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setRiskLevel(JAXBElement<String> value) {
        this.riskLevel = ((JAXBElement<String>) value);
    }

    /**
     * Gets the value of the remark property.
     *
     * @return possible object is
     *         {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getRemark() {
        return remark;
    }

    /**
     * Sets the value of the remark property.
     *
     * @param value allowed object is
     *              {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setRemark(JAXBElement<String> value) {
        this.remark = ((JAXBElement<String>) value);
    }

}
