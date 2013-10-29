
package com.tmb.common.data.responsecalrisknewpersonal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for messageSection complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="messageSection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="msgDesc">
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
@XmlType(name = "messageSection", propOrder = {
        "msgDesc"
})
public class MessageSection {

    @XmlElement(required = true)
    protected String msgDesc;

    /**
     * Gets the value of the msgDesc property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMsgDesc() {
        return msgDesc;
    }

    /**
     * Sets the value of the msgDesc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMsgDesc(String value) {
        this.msgDesc = value;
    }

}
