
package com.tmb.common.data.responsesearchcustomeraccount;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for accountList complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="accountList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rel">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cd">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pSO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="appl">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="accountNo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="trlr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="balance">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dir">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="prod" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctl3">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
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
 *         &lt;element name="status" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="date">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{0,2}/{0,1}[0-9]{0,2}/{0,1}[0-9]{0,4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="citizenId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="13"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="curr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="3"/>
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
@XmlType(name = "accountList", propOrder = {
        "rel",
        "cd",
        "pso",
        "appl",
        "accountNo",
        "trlr",
        "balance",
        "dir",
        "prod",
        "ctl1",
        "ctl2",
        "ctl3",
        "ctl4",
        "status",
        "date",
        "name",
        "citizenId",
        "curr"
})
public class AccountList {

    @XmlElement(required = true)
    protected String rel;
    @XmlElement(required = true)
    protected String cd;
    @XmlElement(name = "pSO", required = true)
    protected String pso;
    @XmlElement(required = true)
    protected String appl;
    @XmlElement(required = true)
    protected String accountNo;
    protected String trlr;
    @XmlElement(required = true)
    protected BigDecimal balance;
    @XmlElement(required = true)
    protected String dir;
    protected String prod;
    @XmlElement(required = true)
    protected String ctl1;
    @XmlElement(required = true)
    protected String ctl2;
    @XmlElement(required = true)
    protected String ctl3;
    @XmlElement(required = true)
    protected String ctl4;
    protected String status;
    @XmlElement(required = true, nillable = true)
    protected String date;
    protected String name;
    protected String citizenId;
    protected String curr;

    /**
     * Gets the value of the rel property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRel() {
        return rel;
    }

    /**
     * Sets the value of the rel property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRel(String value) {
        this.rel = value;
    }

    /**
     * Gets the value of the cd property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCd() {
        return cd;
    }

    /**
     * Sets the value of the cd property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCd(String value) {
        this.cd = value;
    }

    /**
     * Gets the value of the pso property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPSO() {
        return pso;
    }

    /**
     * Sets the value of the pso property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPSO(String value) {
        this.pso = value;
    }

    /**
     * Gets the value of the appl property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAppl() {
        return appl;
    }

    /**
     * Sets the value of the appl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAppl(String value) {
        this.appl = value;
    }

    /**
     * Gets the value of the accountNo property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the accountNo property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAccountNo(String value) {
        this.accountNo = value;
    }

    /**
     * Gets the value of the trlr property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTrlr() {
        return trlr;
    }

    /**
     * Sets the value of the trlr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTrlr(String value) {
        this.trlr = value;
    }

    /**
     * Gets the value of the balance property.
     *
     * @return possible object is
     *         {@link BigDecimal }
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setBalance(BigDecimal value) {
        this.balance = value;
    }

    /**
     * Gets the value of the dir property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDir() {
        return dir;
    }

    /**
     * Sets the value of the dir property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDir(String value) {
        this.dir = value;
    }

    /**
     * Gets the value of the prod property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProd() {
        return prod;
    }

    /**
     * Sets the value of the prod property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProd(String value) {
        this.prod = value;
    }

    /**
     * Gets the value of the ctl1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl1() {
        return ctl1;
    }

    /**
     * Sets the value of the ctl1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl1(String value) {
        this.ctl1 = value;
    }

    /**
     * Gets the value of the ctl2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl2() {
        return ctl2;
    }

    /**
     * Sets the value of the ctl2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl2(String value) {
        this.ctl2 = value;
    }

    /**
     * Gets the value of the ctl3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl3() {
        return ctl3;
    }

    /**
     * Sets the value of the ctl3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl3(String value) {
        this.ctl3 = value;
    }

    /**
     * Gets the value of the ctl4 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtl4() {
        return ctl4;
    }

    /**
     * Sets the value of the ctl4 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtl4(String value) {
        this.ctl4 = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the date property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDate(String value) {
        this.date = value;
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
     * Gets the value of the citizenId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCitizenId() {
        return citizenId;
    }

    /**
     * Sets the value of the citizenId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCitizenId(String value) {
        this.citizenId = value;
    }

    /**
     * Gets the value of the curr property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCurr() {
        return curr;
    }

    /**
     * Sets the value of the curr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCurr(String value) {
        this.curr = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("rel", rel)
                .append("cd", cd)
                .append("pso", pso)
                .append("appl", appl)
                .append("accountNo", accountNo)
                .append("trlr", trlr)
                .append("balance", balance)
                .append("dir", dir)
                .append("prod", prod)
                .append("ctl1", ctl1)
                .append("ctl2", ctl2)
                .append("ctl3", ctl3)
                .append("ctl4", ctl4)
                .append("status", status)
                .append("date", date)
                .append("name", name)
                .append("citizenId", citizenId)
                .append("curr", curr)
                .toString();
    }
}
