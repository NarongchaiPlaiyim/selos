
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for IBNRModelTransactionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IBNRModelTransactionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asOfDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="processingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="flagNonK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sumPDHL" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberPDHL" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sumPDPL" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberPDPL" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sumPDOD" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberPDOD" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sumPDTDR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberPDTDR" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sumPDOT" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberPDOT" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="sumPDNonKAccount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="numberNonKAccount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="account" type="{http://www.tmbbank.com/enterprise/model}AccountType" minOccurs="0"/>
 *         &lt;element name="borrower" type="{http://www.tmbbank.com/enterprise/model}BorrowerType" minOccurs="0"/>
 *         &lt;element name="product" type="{http://www.tmbbank.com/enterprise/model}ProductType" minOccurs="0"/>
 *         &lt;element name="riskModel" type="{http://www.tmbbank.com/enterprise/model}RiskModelType" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IBNRModelTransactionType", propOrder = {
    "id",
    "asOfDate",
    "processingDate",
    "flagNonK",
    "sumPDHL",
    "numberPDHL",
    "sumPDPL",
    "numberPDPL",
    "sumPDOD",
    "numberPDOD",
    "sumPDTDR",
    "numberPDTDR",
    "sumPDOT",
    "numberPDOT",
    "sumPDNonKAccount",
    "numberNonKAccount",
    "account",
    "borrower",
    "product",
    "riskModel",
    "score",
    "attribute"
})
public class IBNRModelTransactionType {

    @XmlElement(name = "ID")
    protected String id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar asOfDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar processingDate;
    protected String flagNonK;
    protected BigDecimal sumPDHL;
    protected Integer numberPDHL;
    protected BigDecimal sumPDPL;
    protected Integer numberPDPL;
    protected BigDecimal sumPDOD;
    protected Integer numberPDOD;
    protected BigDecimal sumPDTDR;
    protected Integer numberPDTDR;
    protected BigDecimal sumPDOT;
    protected Integer numberPDOT;
    protected BigDecimal sumPDNonKAccount;
    protected Integer numberNonKAccount;
    protected AccountType account;
    protected BorrowerType borrower;
    protected ProductType product;
    protected RiskModelType riskModel;
    protected List<ScoreType> score;
    protected List<AttributeType> attribute;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the asOfDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAsOfDate() {
        return asOfDate;
    }

    /**
     * Sets the value of the asOfDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAsOfDate(XMLGregorianCalendar value) {
        this.asOfDate = value;
    }

    /**
     * Gets the value of the processingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProcessingDate() {
        return processingDate;
    }

    /**
     * Sets the value of the processingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setProcessingDate(XMLGregorianCalendar value) {
        this.processingDate = value;
    }

    /**
     * Gets the value of the flagNonK property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagNonK() {
        return flagNonK;
    }

    /**
     * Sets the value of the flagNonK property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagNonK(String value) {
        this.flagNonK = value;
    }

    /**
     * Gets the value of the sumPDHL property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDHL() {
        return sumPDHL;
    }

    /**
     * Sets the value of the sumPDHL property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDHL(BigDecimal value) {
        this.sumPDHL = value;
    }

    /**
     * Gets the value of the numberPDHL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberPDHL() {
        return numberPDHL;
    }

    /**
     * Sets the value of the numberPDHL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberPDHL(Integer value) {
        this.numberPDHL = value;
    }

    /**
     * Gets the value of the sumPDPL property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDPL() {
        return sumPDPL;
    }

    /**
     * Sets the value of the sumPDPL property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDPL(BigDecimal value) {
        this.sumPDPL = value;
    }

    /**
     * Gets the value of the numberPDPL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberPDPL() {
        return numberPDPL;
    }

    /**
     * Sets the value of the numberPDPL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberPDPL(Integer value) {
        this.numberPDPL = value;
    }

    /**
     * Gets the value of the sumPDOD property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDOD() {
        return sumPDOD;
    }

    /**
     * Sets the value of the sumPDOD property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDOD(BigDecimal value) {
        this.sumPDOD = value;
    }

    /**
     * Gets the value of the numberPDOD property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberPDOD() {
        return numberPDOD;
    }

    /**
     * Sets the value of the numberPDOD property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberPDOD(Integer value) {
        this.numberPDOD = value;
    }

    /**
     * Gets the value of the sumPDTDR property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDTDR() {
        return sumPDTDR;
    }

    /**
     * Sets the value of the sumPDTDR property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDTDR(BigDecimal value) {
        this.sumPDTDR = value;
    }

    /**
     * Gets the value of the numberPDTDR property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberPDTDR() {
        return numberPDTDR;
    }

    /**
     * Sets the value of the numberPDTDR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberPDTDR(Integer value) {
        this.numberPDTDR = value;
    }

    /**
     * Gets the value of the sumPDOT property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDOT() {
        return sumPDOT;
    }

    /**
     * Sets the value of the sumPDOT property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDOT(BigDecimal value) {
        this.sumPDOT = value;
    }

    /**
     * Gets the value of the numberPDOT property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberPDOT() {
        return numberPDOT;
    }

    /**
     * Sets the value of the numberPDOT property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberPDOT(Integer value) {
        this.numberPDOT = value;
    }

    /**
     * Gets the value of the sumPDNonKAccount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSumPDNonKAccount() {
        return sumPDNonKAccount;
    }

    /**
     * Sets the value of the sumPDNonKAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSumPDNonKAccount(BigDecimal value) {
        this.sumPDNonKAccount = value;
    }

    /**
     * Gets the value of the numberNonKAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberNonKAccount() {
        return numberNonKAccount;
    }

    /**
     * Sets the value of the numberNonKAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberNonKAccount(Integer value) {
        this.numberNonKAccount = value;
    }

    /**
     * Gets the value of the account property.
     * 
     * @return
     *     possible object is
     *     {@link AccountType }
     *     
     */
    public AccountType getAccount() {
        return account;
    }

    /**
     * Sets the value of the account property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountType }
     *     
     */
    public void setAccount(AccountType value) {
        this.account = value;
    }

    /**
     * Gets the value of the borrower property.
     * 
     * @return
     *     possible object is
     *     {@link BorrowerType }
     *     
     */
    public BorrowerType getBorrower() {
        return borrower;
    }

    /**
     * Sets the value of the borrower property.
     * 
     * @param value
     *     allowed object is
     *     {@link BorrowerType }
     *     
     */
    public void setBorrower(BorrowerType value) {
        this.borrower = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link ProductType }
     *     
     */
    public ProductType getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductType }
     *     
     */
    public void setProduct(ProductType value) {
        this.product = value;
    }

    /**
     * Gets the value of the riskModel property.
     * 
     * @return
     *     possible object is
     *     {@link RiskModelType }
     *     
     */
    public RiskModelType getRiskModel() {
        return riskModel;
    }

    /**
     * Sets the value of the riskModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiskModelType }
     *     
     */
    public void setRiskModel(RiskModelType value) {
        this.riskModel = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the score property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScore().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScoreType }
     * 
     * 
     */
    public List<ScoreType> getScore() {
        if (score == null) {
            score = new ArrayList<ScoreType>();
        }
        return this.score;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     * 
     * 
     */
    public List<AttributeType> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeType>();
        }
        return this.attribute;
    }

}
