
package com.clevel.selos.integration.brms.service.document.customerrules;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ProductType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ProductType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lendingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productProgram" type="{http://www.tmbbank.com/enterprise/model}ProductProgramType" minOccurs="0"/>
 *         &lt;element name="productName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loanLimitAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="pricingMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pricing" type="{http://www.tmbbank.com/enterprise/model}PricingType" minOccurs="0"/>
 *         &lt;element name="loanToValueRatio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="mrtaFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="mrtaAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="postponeRegistrationMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxCreditLimitByDSR" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxCreditLimitByDTI" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxCreditLimitByMaxLTV" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxCreditLimitByProductProgram" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxCreditLimitByBOTRegulation" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="requestedCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="recommendedCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="creditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="requestedCardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfFinishTenor" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="requestedTenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="minTenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxTenor" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="topUpAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="topUpAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="loanType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="outstandAmtOfRefinancedLoan" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="loanLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="odLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="comboLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="campaign" type="{http://www.tmbbank.com/enterprise/model}CampaignType" minOccurs="0"/>
 *         &lt;element name="monthlyInstallment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="recommendedMonthlyInstallment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="debtFactor" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="collateral" type="{http://www.tmbbank.com/enterprise/model}CollateralType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fee" type="{http://www.tmbbank.com/enterprise/model}FeeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="selosProductProgram" type="{http://www.tmbbank.com/enterprise/model}SELOSProductProgramType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductType", propOrder = {
        "id",
        "lendingType",
        "productType",
        "productCode",
        "productProgram",
        "productName",
        "loanLimitAmount",
        "pricingMode",
        "pricing",
        "loanToValueRatio",
        "mrtaFlag",
        "mrtaAmount",
        "postponeRegistrationMode",
        "maxCreditLimitByDSR",
        "maxCreditLimitByDTI",
        "maxCreditLimitByMaxLTV",
        "maxCreditLimitByProductProgram",
        "maxCreditLimitByBOTRegulation",
        "requestedCreditLimit",
        "recommendedCreditLimit",
        "creditLimit",
        "requestedCardType",
        "cardType",
        "dateOfFinishTenor",
        "requestedTenor",
        "tenor",
        "minTenor",
        "maxTenor",
        "topUpAccountNumber",
        "topUpAmount",
        "loanType",
        "outstandAmtOfRefinancedLoan",
        "loanLimit",
        "odLimit",
        "comboLimit",
        "campaign",
        "monthlyInstallment",
        "recommendedMonthlyInstallment",
        "debtFactor",
        "collateral",
        "attribute",
        "score",
        "fee",
        "selosProductProgram"
})
public class ProductType {

    @XmlElement(name = "ID")
    protected String id;
    protected String lendingType;
    protected String productType;
    protected String productCode;
    protected ProductProgramType productProgram;
    protected String productName;
    protected BigDecimal loanLimitAmount;
    protected String pricingMode;
    protected PricingType pricing;
    protected BigDecimal loanToValueRatio;
    protected Boolean mrtaFlag;
    protected BigDecimal mrtaAmount;
    protected String postponeRegistrationMode;
    protected BigDecimal maxCreditLimitByDSR;
    protected BigDecimal maxCreditLimitByDTI;
    protected BigDecimal maxCreditLimitByMaxLTV;
    protected BigDecimal maxCreditLimitByProductProgram;
    protected BigDecimal maxCreditLimitByBOTRegulation;
    protected BigDecimal requestedCreditLimit;
    protected BigDecimal recommendedCreditLimit;
    protected BigDecimal creditLimit;
    protected String requestedCardType;
    protected String cardType;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfFinishTenor;
    protected Integer requestedTenor;
    protected Integer tenor;
    protected Integer minTenor;
    protected Integer maxTenor;
    protected String topUpAccountNumber;
    protected BigDecimal topUpAmount;
    protected String loanType;
    protected BigDecimal outstandAmtOfRefinancedLoan;
    protected BigDecimal loanLimit;
    protected BigDecimal odLimit;
    protected BigDecimal comboLimit;
    protected CampaignType campaign;
    protected BigDecimal monthlyInstallment;
    protected BigDecimal recommendedMonthlyInstallment;
    protected BigDecimal debtFactor;
    protected List<CollateralType> collateral;
    protected List<AttributeType> attribute;
    protected List<ScoreType> score;
    protected List<FeeType> fee;
    protected List<SELOSProductProgramType> selosProductProgram;

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
     * Gets the value of the lendingType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLendingType() {
        return lendingType;
    }

    /**
     * Sets the value of the lendingType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLendingType(String value) {
        this.lendingType = value;
    }

    /**
     * Gets the value of the productType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the productCode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the productProgram property.
     *
     * @return possible object is
     *         {@link ProductProgramType }
     */
    public ProductProgramType getProductProgram() {
        return productProgram;
    }

    /**
     * Sets the value of the productProgram property.
     *
     * @param value allowed object is
     *              {@link ProductProgramType }
     */
    public void setProductProgram(ProductProgramType value) {
        this.productProgram = value;
    }

    /**
     * Gets the value of the productName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the value of the productName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProductName(String value) {
        this.productName = value;
    }

    /**
     * Gets the value of the loanLimitAmount property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getLoanLimitAmount() {
        return loanLimitAmount;
    }

    /**
     * Sets the value of the loanLimitAmount property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setLoanLimitAmount(BigDecimal value) {
        this.loanLimitAmount = value;
    }

    /**
     * Gets the value of the pricingMode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPricingMode() {
        return pricingMode;
    }

    /**
     * Sets the value of the pricingMode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPricingMode(String value) {
        this.pricingMode = value;
    }

    /**
     * Gets the value of the pricing property.
     *
     * @return possible object is
     *         {@link PricingType }
     */
    public PricingType getPricing() {
        return pricing;
    }

    /**
     * Sets the value of the pricing property.
     *
     * @param value allowed object is
     *              {@link PricingType }
     */
    public void setPricing(PricingType value) {
        this.pricing = value;
    }

    /**
     * Gets the value of the loanToValueRatio property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getLoanToValueRatio() {
        return loanToValueRatio;
    }

    /**
     * Sets the value of the loanToValueRatio property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setLoanToValueRatio(BigDecimal value) {
        this.loanToValueRatio = value;
    }

    /**
     * Gets the value of the mrtaFlag property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isMrtaFlag() {
        return mrtaFlag;
    }

    /**
     * Sets the value of the mrtaFlag property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setMrtaFlag(Boolean value) {
        this.mrtaFlag = value;
    }

    /**
     * Gets the value of the mrtaAmount property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMrtaAmount() {
        return mrtaAmount;
    }

    /**
     * Sets the value of the mrtaAmount property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMrtaAmount(BigDecimal value) {
        this.mrtaAmount = value;
    }

    /**
     * Gets the value of the postponeRegistrationMode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPostponeRegistrationMode() {
        return postponeRegistrationMode;
    }

    /**
     * Sets the value of the postponeRegistrationMode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPostponeRegistrationMode(String value) {
        this.postponeRegistrationMode = value;
    }

    /**
     * Gets the value of the maxCreditLimitByDSR property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMaxCreditLimitByDSR() {
        return maxCreditLimitByDSR;
    }

    /**
     * Sets the value of the maxCreditLimitByDSR property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMaxCreditLimitByDSR(BigDecimal value) {
        this.maxCreditLimitByDSR = value;
    }

    /**
     * Gets the value of the maxCreditLimitByDTI property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMaxCreditLimitByDTI() {
        return maxCreditLimitByDTI;
    }

    /**
     * Sets the value of the maxCreditLimitByDTI property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMaxCreditLimitByDTI(BigDecimal value) {
        this.maxCreditLimitByDTI = value;
    }

    /**
     * Gets the value of the maxCreditLimitByMaxLTV property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMaxCreditLimitByMaxLTV() {
        return maxCreditLimitByMaxLTV;
    }

    /**
     * Sets the value of the maxCreditLimitByMaxLTV property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMaxCreditLimitByMaxLTV(BigDecimal value) {
        this.maxCreditLimitByMaxLTV = value;
    }

    /**
     * Gets the value of the maxCreditLimitByProductProgram property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMaxCreditLimitByProductProgram() {
        return maxCreditLimitByProductProgram;
    }

    /**
     * Sets the value of the maxCreditLimitByProductProgram property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMaxCreditLimitByProductProgram(BigDecimal value) {
        this.maxCreditLimitByProductProgram = value;
    }

    /**
     * Gets the value of the maxCreditLimitByBOTRegulation property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMaxCreditLimitByBOTRegulation() {
        return maxCreditLimitByBOTRegulation;
    }

    /**
     * Sets the value of the maxCreditLimitByBOTRegulation property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMaxCreditLimitByBOTRegulation(BigDecimal value) {
        this.maxCreditLimitByBOTRegulation = value;
    }

    /**
     * Gets the value of the requestedCreditLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getRequestedCreditLimit() {
        return requestedCreditLimit;
    }

    /**
     * Sets the value of the requestedCreditLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setRequestedCreditLimit(BigDecimal value) {
        this.requestedCreditLimit = value;
    }

    /**
     * Gets the value of the recommendedCreditLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getRecommendedCreditLimit() {
        return recommendedCreditLimit;
    }

    /**
     * Sets the value of the recommendedCreditLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setRecommendedCreditLimit(BigDecimal value) {
        this.recommendedCreditLimit = value;
    }

    /**
     * Gets the value of the creditLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * Sets the value of the creditLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setCreditLimit(BigDecimal value) {
        this.creditLimit = value;
    }

    /**
     * Gets the value of the requestedCardType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRequestedCardType() {
        return requestedCardType;
    }

    /**
     * Sets the value of the requestedCardType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestedCardType(String value) {
        this.requestedCardType = value;
    }

    /**
     * Gets the value of the cardType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the dateOfFinishTenor property.
     *
     * @return possible object is
     *         {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDateOfFinishTenor() {
        return dateOfFinishTenor;
    }

    /**
     * Sets the value of the dateOfFinishTenor property.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDateOfFinishTenor(XMLGregorianCalendar value) {
        this.dateOfFinishTenor = value;
    }

    /**
     * Gets the value of the requestedTenor property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getRequestedTenor() {
        return requestedTenor;
    }

    /**
     * Sets the value of the requestedTenor property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setRequestedTenor(Integer value) {
        this.requestedTenor = value;
    }

    /**
     * Gets the value of the tenor property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getTenor() {
        return tenor;
    }

    /**
     * Sets the value of the tenor property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setTenor(Integer value) {
        this.tenor = value;
    }

    /**
     * Gets the value of the minTenor property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getMinTenor() {
        return minTenor;
    }

    /**
     * Sets the value of the minTenor property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMinTenor(Integer value) {
        this.minTenor = value;
    }

    /**
     * Gets the value of the maxTenor property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getMaxTenor() {
        return maxTenor;
    }

    /**
     * Sets the value of the maxTenor property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMaxTenor(Integer value) {
        this.maxTenor = value;
    }

    /**
     * Gets the value of the topUpAccountNumber property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTopUpAccountNumber() {
        return topUpAccountNumber;
    }

    /**
     * Sets the value of the topUpAccountNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTopUpAccountNumber(String value) {
        this.topUpAccountNumber = value;
    }

    /**
     * Gets the value of the topUpAmount property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getTopUpAmount() {
        return topUpAmount;
    }

    /**
     * Sets the value of the topUpAmount property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setTopUpAmount(BigDecimal value) {
        this.topUpAmount = value;
    }

    /**
     * Gets the value of the loanType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLoanType() {
        return loanType;
    }

    /**
     * Sets the value of the loanType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLoanType(String value) {
        this.loanType = value;
    }

    /**
     * Gets the value of the outstandAmtOfRefinancedLoan property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getOutstandAmtOfRefinancedLoan() {
        return outstandAmtOfRefinancedLoan;
    }

    /**
     * Sets the value of the outstandAmtOfRefinancedLoan property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setOutstandAmtOfRefinancedLoan(BigDecimal value) {
        this.outstandAmtOfRefinancedLoan = value;
    }

    /**
     * Gets the value of the loanLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getLoanLimit() {
        return loanLimit;
    }

    /**
     * Sets the value of the loanLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setLoanLimit(BigDecimal value) {
        this.loanLimit = value;
    }

    /**
     * Gets the value of the odLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getOdLimit() {
        return odLimit;
    }

    /**
     * Sets the value of the odLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setOdLimit(BigDecimal value) {
        this.odLimit = value;
    }

    /**
     * Gets the value of the comboLimit property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getComboLimit() {
        return comboLimit;
    }

    /**
     * Sets the value of the comboLimit property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setComboLimit(BigDecimal value) {
        this.comboLimit = value;
    }

    /**
     * Gets the value of the campaign property.
     *
     * @return possible object is
     *         {@link CampaignType }
     */
    public CampaignType getCampaign() {
        return campaign;
    }

    /**
     * Sets the value of the campaign property.
     *
     * @param value allowed object is
     *              {@link CampaignType }
     */
    public void setCampaign(CampaignType value) {
        this.campaign = value;
    }

    /**
     * Gets the value of the monthlyInstallment property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }

    /**
     * Sets the value of the monthlyInstallment property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMonthlyInstallment(BigDecimal value) {
        this.monthlyInstallment = value;
    }

    /**
     * Gets the value of the recommendedMonthlyInstallment property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getRecommendedMonthlyInstallment() {
        return recommendedMonthlyInstallment;
    }

    /**
     * Sets the value of the recommendedMonthlyInstallment property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setRecommendedMonthlyInstallment(BigDecimal value) {
        this.recommendedMonthlyInstallment = value;
    }

    /**
     * Gets the value of the debtFactor property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getDebtFactor() {
        return debtFactor;
    }

    /**
     * Sets the value of the debtFactor property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setDebtFactor(BigDecimal value) {
        this.debtFactor = value;
    }

    /**
     * Gets the value of the collateral property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collateral property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollateral().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link CollateralType }
     */
    public List<CollateralType> getCollateral() {
        if (collateral == null) {
            collateral = new ArrayList<CollateralType>();
        }
        return this.collateral;
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
     * Gets the value of the fee property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fee property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFee().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FeeType }
     */
    public List<FeeType> getFee() {
        if (fee == null) {
            fee = new ArrayList<FeeType>();
        }
        return this.fee;
    }

    /**
     * Gets the value of the selosProductProgram property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the selosProductProgram property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSelosProductProgram().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SELOSProductProgramType }
     */
    public List<SELOSProductProgramType> getSelosProductProgram() {
        if (selosProductProgram == null) {
            selosProductProgram = new ArrayList<SELOSProductProgramType>();
        }
        return this.selosProductProgram;
    }

}
