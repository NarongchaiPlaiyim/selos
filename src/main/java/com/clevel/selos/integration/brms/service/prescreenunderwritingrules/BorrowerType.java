
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
 * <p>Java class for BorrowerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BorrowerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rmNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="botID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="botName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="botClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legalEntity" type="{http://www.tmbbank.com/enterprise/model}LegalEntityType" minOccurs="0"/>
 *         &lt;element name="individual" type="{http://www.tmbbank.com/enterprise/model}IndividualType" minOccurs="0"/>
 *         &lt;element name="postCode" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="areaOfMailPostcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceAreaOfMailPostcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tgBusinessType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="businessSegment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tgBusinessCustomerSegment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerSince" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="characterIssueFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sourceOfCashIdentifiedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sourceOfRepaymentVerifiedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="coreAssetCollateralFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="obligorCreditLimitExceededFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="riskCustomerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kycRiskLevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nationality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="preScoredFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="s48FullMatchedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="s48PartialMatchedFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="s49CategoryFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="relationshipWithMainBorrower" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardHolderType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="benefitPlusGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aggregatedCreditExposure" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="aggregatedCreditExposureLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="aggregatedCreditExposureOutstanding" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="debtToIncomeRatio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="existTMBHomeLoanOutstandAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="existTMBHomeLoanRepayAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="remainingDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="existingDebtToIncome" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="debtServiceRatio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="maxDebtServiceRatio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="loanToValueRatio" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="paymentCapacity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="totalCreditLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="limitSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tdrFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="employment" type="{http://www.tmbbank.com/enterprise/model}EmploymentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="account" type="{http://www.tmbbank.com/enterprise/model}AccountType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="income" type="{http://www.tmbbank.com/enterprise/model}IncomeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="debt" type="{http://www.tmbbank.com/enterprise/model}DebtType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="product" type="{http://www.tmbbank.com/enterprise/model}ProductType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fraudDetail" type="{http://www.tmbbank.com/enterprise/model}FraudDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="litigationDetail" type="{http://www.tmbbank.com/enterprise/model}LitigationDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="bankruptcyDetail" type="{http://www.tmbbank.com/enterprise/model}BankruptcyDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.tmbbank.com/enterprise/model}ScoreType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ncbReport" type="{http://www.tmbbank.com/enterprise/model}NCBReportType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tmbAccountsReport" type="{http://www.tmbbank.com/enterprise/model}TMBAccountsReportType" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requiredDocumentSet" type="{http://www.tmbbank.com/enterprise/model}DocumentSetType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="guarantor" type="{http://www.tmbbank.com/enterprise/model}GuarantorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dateBehaviorScore" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="riskModel" type="{http://www.tmbbank.com/enterprise/model}RiskModelType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="delinquency" type="{http://www.tmbbank.com/enterprise/model}DelinquencyType" minOccurs="0"/>
 *         &lt;element name="otherLoanStatus" type="{http://www.tmbbank.com/enterprise/model}OtherLoanStatusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="warningCodeFullMatched" type="{http://www.tmbbank.com/enterprise/model}WarningCodeFullMatchedType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="WarningCodePartialMatched" type="{http://www.tmbbank.com/enterprise/model}WarningCodePartialMatchedType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BorrowerType", propOrder = {
    "id",
    "rmNumber",
    "botID",
    "botName",
    "botClass",
    "legalEntity",
    "individual",
    "postCode",
    "areaOfMailPostcode",
    "provinceAreaOfMailPostcode",
    "type",
    "businessType",
    "tgBusinessType",
    "businessSegment",
    "tgBusinessCustomerSegment",
    "customerSince",
    "characterIssueFlag",
    "sourceOfCashIdentifiedFlag",
    "sourceOfRepaymentVerifiedFlag",
    "coreAssetCollateralFlag",
    "obligorCreditLimitExceededFlag",
    "riskCustomerType",
    "kycRiskLevel",
    "nationality",
    "preScoredFlag",
    "s48FullMatchedFlag",
    "s48PartialMatchedFlag",
    "s49CategoryFlag",
    "relationshipWithMainBorrower",
    "cardHolderType",
    "benefitPlusGroup",
    "aggregatedCreditExposure",
    "aggregatedCreditExposureLimit",
    "aggregatedCreditExposureOutstanding",
    "debtToIncomeRatio",
    "existTMBHomeLoanOutstandAmt",
    "existTMBHomeLoanRepayAmt",
    "remainingDebtToIncome",
    "maxDebtToIncome",
    "existingDebtToIncome",
    "debtServiceRatio",
    "maxDebtServiceRatio",
    "loanToValueRatio",
    "paymentCapacity",
    "totalCreditLimit",
    "limitSize",
    "tdrFlag",
    "employment",
    "account",
    "income",
    "debt",
    "product",
    "fraudDetail",
    "litigationDetail",
    "bankruptcyDetail",
    "score",
    "ncbReport",
    "tmbAccountsReport",
    "attribute",
    "requiredDocumentSet",
    "guarantor",
    "dateBehaviorScore",
    "riskModel",
    "delinquency",
    "otherLoanStatus",
    "warningCodeFullMatched",
    "warningCodePartialMatched"
})
public class BorrowerType {

    @XmlElement(name = "ID")
    protected String id;
    protected String rmNumber;
    protected String botID;
    protected String botName;
    protected String botClass;
    protected LegalEntityType legalEntity;
    protected IndividualType individual;
    protected Long postCode;
    protected String areaOfMailPostcode;
    protected String provinceAreaOfMailPostcode;
    protected String type;
    protected String businessType;
    protected String tgBusinessType;
    protected String businessSegment;
    protected String tgBusinessCustomerSegment;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar customerSince;
    protected Boolean characterIssueFlag;
    protected Boolean sourceOfCashIdentifiedFlag;
    protected Boolean sourceOfRepaymentVerifiedFlag;
    protected Boolean coreAssetCollateralFlag;
    protected Boolean obligorCreditLimitExceededFlag;
    protected String riskCustomerType;
    protected Integer kycRiskLevel;
    protected String nationality;
    protected Boolean preScoredFlag;
    protected Boolean s48FullMatchedFlag;
    protected Boolean s48PartialMatchedFlag;
    protected Boolean s49CategoryFlag;
    protected String relationshipWithMainBorrower;
    protected String cardHolderType;
    protected String benefitPlusGroup;
    protected BigDecimal aggregatedCreditExposure;
    protected BigDecimal aggregatedCreditExposureLimit;
    protected BigDecimal aggregatedCreditExposureOutstanding;
    protected BigDecimal debtToIncomeRatio;
    protected BigDecimal existTMBHomeLoanOutstandAmt;
    protected BigDecimal existTMBHomeLoanRepayAmt;
    protected BigDecimal remainingDebtToIncome;
    protected BigDecimal maxDebtToIncome;
    protected BigDecimal existingDebtToIncome;
    protected BigDecimal debtServiceRatio;
    protected BigDecimal maxDebtServiceRatio;
    protected BigDecimal loanToValueRatio;
    protected BigDecimal paymentCapacity;
    protected BigDecimal totalCreditLimit;
    protected String limitSize;
    protected String tdrFlag;
    protected List<EmploymentType> employment;
    protected List<AccountType> account;
    protected List<IncomeType> income;
    protected List<DebtType> debt;
    protected List<ProductType> product;
    protected List<FraudDetailType> fraudDetail;
    protected List<LitigationDetailType> litigationDetail;
    protected List<BankruptcyDetailType> bankruptcyDetail;
    protected List<ScoreType> score;
    protected List<NCBReportType> ncbReport;
    protected TMBAccountsReportType tmbAccountsReport;
    protected List<AttributeType> attribute;
    protected List<DocumentSetType> requiredDocumentSet;
    protected List<GuarantorType> guarantor;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateBehaviorScore;
    protected List<RiskModelType> riskModel;
    protected DelinquencyType delinquency;
    protected List<OtherLoanStatusType> otherLoanStatus;
    protected List<WarningCodeFullMatchedType> warningCodeFullMatched;
    @XmlElement(name = "WarningCodePartialMatched")
    protected List<WarningCodePartialMatchedType> warningCodePartialMatched;

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
     * Gets the value of the rmNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmNumber() {
        return rmNumber;
    }

    /**
     * Sets the value of the rmNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmNumber(String value) {
        this.rmNumber = value;
    }

    /**
     * Gets the value of the botID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotID() {
        return botID;
    }

    /**
     * Sets the value of the botID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotID(String value) {
        this.botID = value;
    }

    /**
     * Gets the value of the botName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotName() {
        return botName;
    }

    /**
     * Sets the value of the botName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotName(String value) {
        this.botName = value;
    }

    /**
     * Gets the value of the botClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBotClass() {
        return botClass;
    }

    /**
     * Sets the value of the botClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBotClass(String value) {
        this.botClass = value;
    }

    /**
     * Gets the value of the legalEntity property.
     * 
     * @return
     *     possible object is
     *     {@link LegalEntityType }
     *     
     */
    public LegalEntityType getLegalEntity() {
        return legalEntity;
    }

    /**
     * Sets the value of the legalEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegalEntityType }
     *     
     */
    public void setLegalEntity(LegalEntityType value) {
        this.legalEntity = value;
    }

    /**
     * Gets the value of the individual property.
     * 
     * @return
     *     possible object is
     *     {@link IndividualType }
     *     
     */
    public IndividualType getIndividual() {
        return individual;
    }

    /**
     * Sets the value of the individual property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividualType }
     *     
     */
    public void setIndividual(IndividualType value) {
        this.individual = value;
    }

    /**
     * Gets the value of the postCode property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPostCode() {
        return postCode;
    }

    /**
     * Sets the value of the postCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPostCode(Long value) {
        this.postCode = value;
    }

    /**
     * Gets the value of the areaOfMailPostcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaOfMailPostcode() {
        return areaOfMailPostcode;
    }

    /**
     * Sets the value of the areaOfMailPostcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaOfMailPostcode(String value) {
        this.areaOfMailPostcode = value;
    }

    /**
     * Gets the value of the provinceAreaOfMailPostcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinceAreaOfMailPostcode() {
        return provinceAreaOfMailPostcode;
    }

    /**
     * Sets the value of the provinceAreaOfMailPostcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinceAreaOfMailPostcode(String value) {
        this.provinceAreaOfMailPostcode = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the businessType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Sets the value of the businessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessType(String value) {
        this.businessType = value;
    }

    /**
     * Gets the value of the tgBusinessType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgBusinessType() {
        return tgBusinessType;
    }

    /**
     * Sets the value of the tgBusinessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgBusinessType(String value) {
        this.tgBusinessType = value;
    }

    /**
     * Gets the value of the businessSegment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessSegment() {
        return businessSegment;
    }

    /**
     * Sets the value of the businessSegment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessSegment(String value) {
        this.businessSegment = value;
    }

    /**
     * Gets the value of the tgBusinessCustomerSegment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTgBusinessCustomerSegment() {
        return tgBusinessCustomerSegment;
    }

    /**
     * Sets the value of the tgBusinessCustomerSegment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTgBusinessCustomerSegment(String value) {
        this.tgBusinessCustomerSegment = value;
    }

    /**
     * Gets the value of the customerSince property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCustomerSince() {
        return customerSince;
    }

    /**
     * Sets the value of the customerSince property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setCustomerSince(XMLGregorianCalendar value) {
        this.customerSince = value;
    }

    /**
     * Gets the value of the characterIssueFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCharacterIssueFlag() {
        return characterIssueFlag;
    }

    /**
     * Sets the value of the characterIssueFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCharacterIssueFlag(Boolean value) {
        this.characterIssueFlag = value;
    }

    /**
     * Gets the value of the sourceOfCashIdentifiedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSourceOfCashIdentifiedFlag() {
        return sourceOfCashIdentifiedFlag;
    }

    /**
     * Sets the value of the sourceOfCashIdentifiedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSourceOfCashIdentifiedFlag(Boolean value) {
        this.sourceOfCashIdentifiedFlag = value;
    }

    /**
     * Gets the value of the sourceOfRepaymentVerifiedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSourceOfRepaymentVerifiedFlag() {
        return sourceOfRepaymentVerifiedFlag;
    }

    /**
     * Sets the value of the sourceOfRepaymentVerifiedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSourceOfRepaymentVerifiedFlag(Boolean value) {
        this.sourceOfRepaymentVerifiedFlag = value;
    }

    /**
     * Gets the value of the coreAssetCollateralFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCoreAssetCollateralFlag() {
        return coreAssetCollateralFlag;
    }

    /**
     * Sets the value of the coreAssetCollateralFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCoreAssetCollateralFlag(Boolean value) {
        this.coreAssetCollateralFlag = value;
    }

    /**
     * Gets the value of the obligorCreditLimitExceededFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isObligorCreditLimitExceededFlag() {
        return obligorCreditLimitExceededFlag;
    }

    /**
     * Sets the value of the obligorCreditLimitExceededFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setObligorCreditLimitExceededFlag(Boolean value) {
        this.obligorCreditLimitExceededFlag = value;
    }

    /**
     * Gets the value of the riskCustomerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskCustomerType() {
        return riskCustomerType;
    }

    /**
     * Sets the value of the riskCustomerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskCustomerType(String value) {
        this.riskCustomerType = value;
    }

    /**
     * Gets the value of the kycRiskLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKycRiskLevel() {
        return kycRiskLevel;
    }

    /**
     * Sets the value of the kycRiskLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKycRiskLevel(Integer value) {
        this.kycRiskLevel = value;
    }

    /**
     * Gets the value of the nationality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the value of the nationality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Gets the value of the preScoredFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPreScoredFlag() {
        return preScoredFlag;
    }

    /**
     * Sets the value of the preScoredFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreScoredFlag(Boolean value) {
        this.preScoredFlag = value;
    }

    /**
     * Gets the value of the s48FullMatchedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isS48FullMatchedFlag() {
        return s48FullMatchedFlag;
    }

    /**
     * Sets the value of the s48FullMatchedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setS48FullMatchedFlag(Boolean value) {
        this.s48FullMatchedFlag = value;
    }

    /**
     * Gets the value of the s48PartialMatchedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isS48PartialMatchedFlag() {
        return s48PartialMatchedFlag;
    }

    /**
     * Sets the value of the s48PartialMatchedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setS48PartialMatchedFlag(Boolean value) {
        this.s48PartialMatchedFlag = value;
    }

    /**
     * Gets the value of the s49CategoryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isS49CategoryFlag() {
        return s49CategoryFlag;
    }

    /**
     * Sets the value of the s49CategoryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setS49CategoryFlag(Boolean value) {
        this.s49CategoryFlag = value;
    }

    /**
     * Gets the value of the relationshipWithMainBorrower property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelationshipWithMainBorrower() {
        return relationshipWithMainBorrower;
    }

    /**
     * Sets the value of the relationshipWithMainBorrower property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelationshipWithMainBorrower(String value) {
        this.relationshipWithMainBorrower = value;
    }

    /**
     * Gets the value of the cardHolderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardHolderType() {
        return cardHolderType;
    }

    /**
     * Sets the value of the cardHolderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardHolderType(String value) {
        this.cardHolderType = value;
    }

    /**
     * Gets the value of the benefitPlusGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBenefitPlusGroup() {
        return benefitPlusGroup;
    }

    /**
     * Sets the value of the benefitPlusGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBenefitPlusGroup(String value) {
        this.benefitPlusGroup = value;
    }

    /**
     * Gets the value of the aggregatedCreditExposure property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAggregatedCreditExposure() {
        return aggregatedCreditExposure;
    }

    /**
     * Sets the value of the aggregatedCreditExposure property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAggregatedCreditExposure(BigDecimal value) {
        this.aggregatedCreditExposure = value;
    }

    /**
     * Gets the value of the aggregatedCreditExposureLimit property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAggregatedCreditExposureLimit() {
        return aggregatedCreditExposureLimit;
    }

    /**
     * Sets the value of the aggregatedCreditExposureLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAggregatedCreditExposureLimit(BigDecimal value) {
        this.aggregatedCreditExposureLimit = value;
    }

    /**
     * Gets the value of the aggregatedCreditExposureOutstanding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getAggregatedCreditExposureOutstanding() {
        return aggregatedCreditExposureOutstanding;
    }

    /**
     * Sets the value of the aggregatedCreditExposureOutstanding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setAggregatedCreditExposureOutstanding(BigDecimal value) {
        this.aggregatedCreditExposureOutstanding = value;
    }

    /**
     * Gets the value of the debtToIncomeRatio property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getDebtToIncomeRatio() {
        return debtToIncomeRatio;
    }

    /**
     * Sets the value of the debtToIncomeRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setDebtToIncomeRatio(BigDecimal value) {
        this.debtToIncomeRatio = value;
    }

    /**
     * Gets the value of the existTMBHomeLoanOutstandAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getExistTMBHomeLoanOutstandAmt() {
        return existTMBHomeLoanOutstandAmt;
    }

    /**
     * Sets the value of the existTMBHomeLoanOutstandAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setExistTMBHomeLoanOutstandAmt(BigDecimal value) {
        this.existTMBHomeLoanOutstandAmt = value;
    }

    /**
     * Gets the value of the existTMBHomeLoanRepayAmt property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getExistTMBHomeLoanRepayAmt() {
        return existTMBHomeLoanRepayAmt;
    }

    /**
     * Sets the value of the existTMBHomeLoanRepayAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setExistTMBHomeLoanRepayAmt(BigDecimal value) {
        this.existTMBHomeLoanRepayAmt = value;
    }

    /**
     * Gets the value of the remainingDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getRemainingDebtToIncome() {
        return remainingDebtToIncome;
    }

    /**
     * Sets the value of the remainingDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setRemainingDebtToIncome(BigDecimal value) {
        this.remainingDebtToIncome = value;
    }

    /**
     * Gets the value of the maxDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getMaxDebtToIncome() {
        return maxDebtToIncome;
    }

    /**
     * Sets the value of the maxDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setMaxDebtToIncome(BigDecimal value) {
        this.maxDebtToIncome = value;
    }

    /**
     * Gets the value of the existingDebtToIncome property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getExistingDebtToIncome() {
        return existingDebtToIncome;
    }

    /**
     * Sets the value of the existingDebtToIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setExistingDebtToIncome(BigDecimal value) {
        this.existingDebtToIncome = value;
    }

    /**
     * Gets the value of the debtServiceRatio property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getDebtServiceRatio() {
        return debtServiceRatio;
    }

    /**
     * Sets the value of the debtServiceRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setDebtServiceRatio(BigDecimal value) {
        this.debtServiceRatio = value;
    }

    /**
     * Gets the value of the maxDebtServiceRatio property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getMaxDebtServiceRatio() {
        return maxDebtServiceRatio;
    }

    /**
     * Sets the value of the maxDebtServiceRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setMaxDebtServiceRatio(BigDecimal value) {
        this.maxDebtServiceRatio = value;
    }

    /**
     * Gets the value of the loanToValueRatio property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getLoanToValueRatio() {
        return loanToValueRatio;
    }

    /**
     * Sets the value of the loanToValueRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setLoanToValueRatio(BigDecimal value) {
        this.loanToValueRatio = value;
    }

    /**
     * Gets the value of the paymentCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getPaymentCapacity() {
        return paymentCapacity;
    }

    /**
     * Sets the value of the paymentCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setPaymentCapacity(BigDecimal value) {
        this.paymentCapacity = value;
    }

    /**
     * Gets the value of the totalCreditLimit property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getTotalCreditLimit() {
        return totalCreditLimit;
    }

    /**
     * Sets the value of the totalCreditLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setTotalCreditLimit(BigDecimal value) {
        this.totalCreditLimit = value;
    }

    /**
     * Gets the value of the limitSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLimitSize() {
        return limitSize;
    }

    /**
     * Sets the value of the limitSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimitSize(String value) {
        this.limitSize = value;
    }

    /**
     * Gets the value of the tdrFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTdrFlag() {
        return tdrFlag;
    }

    /**
     * Sets the value of the tdrFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTdrFlag(String value) {
        this.tdrFlag = value;
    }

    /**
     * Gets the value of the employment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the employment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmployment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmploymentType }
     * 
     * 
     */
    public List<EmploymentType> getEmployment() {
        if (employment == null) {
            employment = new ArrayList<EmploymentType>();
        }
        return this.employment;
    }

    /**
     * Gets the value of the account property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the account property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountType }
     * 
     * 
     */
    public List<AccountType> getAccount() {
        if (account == null) {
            account = new ArrayList<AccountType>();
        }
        return this.account;
    }

    /**
     * Gets the value of the income property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the income property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncome().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IncomeType }
     * 
     * 
     */
    public List<IncomeType> getIncome() {
        if (income == null) {
            income = new ArrayList<IncomeType>();
        }
        return this.income;
    }

    /**
     * Gets the value of the debt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the debt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDebt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DebtType }
     * 
     * 
     */
    public List<DebtType> getDebt() {
        if (debt == null) {
            debt = new ArrayList<DebtType>();
        }
        return this.debt;
    }

    /**
     * Gets the value of the product property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductType }
     * 
     * 
     */
    public List<ProductType> getProduct() {
        if (product == null) {
            product = new ArrayList<ProductType>();
        }
        return this.product;
    }

    /**
     * Gets the value of the fraudDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fraudDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFraudDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FraudDetailType }
     * 
     * 
     */
    public List<FraudDetailType> getFraudDetail() {
        if (fraudDetail == null) {
            fraudDetail = new ArrayList<FraudDetailType>();
        }
        return this.fraudDetail;
    }

    /**
     * Gets the value of the litigationDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the litigationDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLitigationDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LitigationDetailType }
     * 
     * 
     */
    public List<LitigationDetailType> getLitigationDetail() {
        if (litigationDetail == null) {
            litigationDetail = new ArrayList<LitigationDetailType>();
        }
        return this.litigationDetail;
    }

    /**
     * Gets the value of the bankruptcyDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankruptcyDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankruptcyDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BankruptcyDetailType }
     * 
     * 
     */
    public List<BankruptcyDetailType> getBankruptcyDetail() {
        if (bankruptcyDetail == null) {
            bankruptcyDetail = new ArrayList<BankruptcyDetailType>();
        }
        return this.bankruptcyDetail;
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
     * Gets the value of the ncbReport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ncbReport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNcbReport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NCBReportType }
     * 
     * 
     */
    public List<NCBReportType> getNcbReport() {
        if (ncbReport == null) {
            ncbReport = new ArrayList<NCBReportType>();
        }
        return this.ncbReport;
    }

    /**
     * Gets the value of the tmbAccountsReport property.
     * 
     * @return
     *     possible object is
     *     {@link TMBAccountsReportType }
     *     
     */
    public TMBAccountsReportType getTmbAccountsReport() {
        return tmbAccountsReport;
    }

    /**
     * Sets the value of the tmbAccountsReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TMBAccountsReportType }
     *     
     */
    public void setTmbAccountsReport(TMBAccountsReportType value) {
        this.tmbAccountsReport = value;
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

    /**
     * Gets the value of the requiredDocumentSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiredDocumentSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiredDocumentSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentSetType }
     * 
     * 
     */
    public List<DocumentSetType> getRequiredDocumentSet() {
        if (requiredDocumentSet == null) {
            requiredDocumentSet = new ArrayList<DocumentSetType>();
        }
        return this.requiredDocumentSet;
    }

    /**
     * Gets the value of the guarantor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guarantor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuarantor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GuarantorType }
     * 
     * 
     */
    public List<GuarantorType> getGuarantor() {
        if (guarantor == null) {
            guarantor = new ArrayList<GuarantorType>();
        }
        return this.guarantor;
    }

    /**
     * Gets the value of the dateBehaviorScore property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateBehaviorScore() {
        return dateBehaviorScore;
    }

    /**
     * Sets the value of the dateBehaviorScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setDateBehaviorScore(XMLGregorianCalendar value) {
        this.dateBehaviorScore = value;
    }

    /**
     * Gets the value of the riskModel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the riskModel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRiskModel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RiskModelType }
     * 
     * 
     */
    public List<RiskModelType> getRiskModel() {
        if (riskModel == null) {
            riskModel = new ArrayList<RiskModelType>();
        }
        return this.riskModel;
    }

    /**
     * Gets the value of the delinquency property.
     * 
     * @return
     *     possible object is
     *     {@link DelinquencyType }
     *     
     */
    public DelinquencyType getDelinquency() {
        return delinquency;
    }

    /**
     * Sets the value of the delinquency property.
     * 
     * @param value
     *     allowed object is
     *     {@link DelinquencyType }
     *     
     */
    public void setDelinquency(DelinquencyType value) {
        this.delinquency = value;
    }

    /**
     * Gets the value of the otherLoanStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherLoanStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherLoanStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherLoanStatusType }
     * 
     * 
     */
    public List<OtherLoanStatusType> getOtherLoanStatus() {
        if (otherLoanStatus == null) {
            otherLoanStatus = new ArrayList<OtherLoanStatusType>();
        }
        return this.otherLoanStatus;
    }

    /**
     * Gets the value of the warningCodeFullMatched property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warningCodeFullMatched property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarningCodeFullMatched().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WarningCodeFullMatchedType }
     * 
     * 
     */
    public List<WarningCodeFullMatchedType> getWarningCodeFullMatched() {
        if (warningCodeFullMatched == null) {
            warningCodeFullMatched = new ArrayList<WarningCodeFullMatchedType>();
        }
        return this.warningCodeFullMatched;
    }

    /**
     * Gets the value of the warningCodePartialMatched property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the warningCodePartialMatched property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWarningCodePartialMatched().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WarningCodePartialMatchedType }
     * 
     * 
     */
    public List<WarningCodePartialMatchedType> getWarningCodePartialMatched() {
        if (warningCodePartialMatched == null) {
            warningCodePartialMatched = new ArrayList<WarningCodePartialMatchedType>();
        }
        return this.warningCodePartialMatched;
    }

}
