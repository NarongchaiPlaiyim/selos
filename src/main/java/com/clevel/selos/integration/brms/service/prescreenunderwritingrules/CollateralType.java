
package com.clevel.selos.integration.brms.service.prescreenunderwritingrules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for CollateralType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="CollateralType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collateralType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eligibleFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="coreAssetFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="propertyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collateralBuildingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pledgedPercentage" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="project" type="{http://www.tmbbank.com/enterprise/model}ProjectType" minOccurs="0"/>
 *         &lt;element name="spaceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="spaceValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="document" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainBorrowerOwnerFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="coBorrowerOwnerFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="buildingMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propertyGrade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="propertyStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="marketValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="haircutValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="purchasedValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="appraisedValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="province" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="appraisal" type="{http://www.tmbbank.com/enterprise/model}AppraisalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="developer" type="{http://www.tmbbank.com/enterprise/model}DeveloperType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attribute" type="{http://www.tmbbank.com/enterprise/model}AttributeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requiredDocumentSet" type="{http://www.tmbbank.com/enterprise/model}DocumentSetType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollateralType", propOrder = {
        "id",
        "name",
        "collateralType",
        "eligibleFlag",
        "coreAssetFlag",
        "propertyType",
        "buildingType",
        "collateralBuildingType",
        "description",
        "pledgedPercentage",
        "project",
        "spaceType",
        "spaceValue",
        "document",
        "mainBorrowerOwnerFlag",
        "coBorrowerOwnerFlag",
        "buildingMaterialType",
        "propertyGrade",
        "propertyStatus",
        "value",
        "marketValue",
        "haircutValue",
        "purchasedValue",
        "appraisedValue",
        "province",
        "appraisal",
        "developer",
        "attribute",
        "requiredDocumentSet"
})
public class CollateralType {

    @XmlElement(name = "ID")
    protected String id;
    protected String name;
    protected String collateralType;
    protected Boolean eligibleFlag;
    protected Boolean coreAssetFlag;
    protected String propertyType;
    protected String buildingType;
    protected String collateralBuildingType;
    protected String description;
    protected Double pledgedPercentage;
    protected ProjectType project;
    protected String spaceType;
    protected BigDecimal spaceValue;
    protected String document;
    protected Boolean mainBorrowerOwnerFlag;
    protected Boolean coBorrowerOwnerFlag;
    protected String buildingMaterialType;
    protected String propertyGrade;
    protected String propertyStatus;
    protected BigDecimal value;
    protected BigDecimal marketValue;
    protected BigDecimal haircutValue;
    protected BigDecimal purchasedValue;
    protected BigDecimal appraisedValue;
    protected String province;
    protected List<AppraisalType> appraisal;
    protected List<DeveloperType> developer;
    protected List<AttributeType> attribute;
    protected List<DocumentSetType> requiredDocumentSet;

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
     * Gets the value of the collateralType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCollateralType() {
        return collateralType;
    }

    /**
     * Sets the value of the collateralType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCollateralType(String value) {
        this.collateralType = value;
    }

    /**
     * Gets the value of the eligibleFlag property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isEligibleFlag() {
        return eligibleFlag;
    }

    /**
     * Sets the value of the eligibleFlag property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setEligibleFlag(Boolean value) {
        this.eligibleFlag = value;
    }

    /**
     * Gets the value of the coreAssetFlag property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isCoreAssetFlag() {
        return coreAssetFlag;
    }

    /**
     * Sets the value of the coreAssetFlag property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setCoreAssetFlag(Boolean value) {
        this.coreAssetFlag = value;
    }

    /**
     * Gets the value of the propertyType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the value of the propertyType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPropertyType(String value) {
        this.propertyType = value;
    }

    /**
     * Gets the value of the buildingType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBuildingType() {
        return buildingType;
    }

    /**
     * Sets the value of the buildingType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBuildingType(String value) {
        this.buildingType = value;
    }

    /**
     * Gets the value of the collateralBuildingType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCollateralBuildingType() {
        return collateralBuildingType;
    }

    /**
     * Sets the value of the collateralBuildingType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCollateralBuildingType(String value) {
        this.collateralBuildingType = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the pledgedPercentage property.
     *
     * @return possible object is
     *         {@link Double }
     */
    public Double getPledgedPercentage() {
        return pledgedPercentage;
    }

    /**
     * Sets the value of the pledgedPercentage property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setPledgedPercentage(Double value) {
        this.pledgedPercentage = value;
    }

    /**
     * Gets the value of the project property.
     *
     * @return possible object is
     *         {@link ProjectType }
     */
    public ProjectType getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     *
     * @param value allowed object is
     *              {@link ProjectType }
     */
    public void setProject(ProjectType value) {
        this.project = value;
    }

    /**
     * Gets the value of the spaceType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSpaceType() {
        return spaceType;
    }

    /**
     * Sets the value of the spaceType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSpaceType(String value) {
        this.spaceType = value;
    }

    /**
     * Gets the value of the spaceValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getSpaceValue() {
        return spaceValue;
    }

    /**
     * Sets the value of the spaceValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setSpaceValue(BigDecimal value) {
        this.spaceValue = value;
    }

    /**
     * Gets the value of the document property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocument(String value) {
        this.document = value;
    }

    /**
     * Gets the value of the mainBorrowerOwnerFlag property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isMainBorrowerOwnerFlag() {
        return mainBorrowerOwnerFlag;
    }

    /**
     * Sets the value of the mainBorrowerOwnerFlag property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setMainBorrowerOwnerFlag(Boolean value) {
        this.mainBorrowerOwnerFlag = value;
    }

    /**
     * Gets the value of the coBorrowerOwnerFlag property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isCoBorrowerOwnerFlag() {
        return coBorrowerOwnerFlag;
    }

    /**
     * Sets the value of the coBorrowerOwnerFlag property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setCoBorrowerOwnerFlag(Boolean value) {
        this.coBorrowerOwnerFlag = value;
    }

    /**
     * Gets the value of the buildingMaterialType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBuildingMaterialType() {
        return buildingMaterialType;
    }

    /**
     * Sets the value of the buildingMaterialType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBuildingMaterialType(String value) {
        this.buildingMaterialType = value;
    }

    /**
     * Gets the value of the propertyGrade property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPropertyGrade() {
        return propertyGrade;
    }

    /**
     * Sets the value of the propertyGrade property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPropertyGrade(String value) {
        this.propertyGrade = value;
    }

    /**
     * Gets the value of the propertyStatus property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPropertyStatus() {
        return propertyStatus;
    }

    /**
     * Sets the value of the propertyStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPropertyStatus(String value) {
        this.propertyStatus = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the value of the marketValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getMarketValue() {
        return marketValue;
    }

    /**
     * Sets the value of the marketValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setMarketValue(BigDecimal value) {
        this.marketValue = value;
    }

    /**
     * Gets the value of the haircutValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getHaircutValue() {
        return haircutValue;
    }

    /**
     * Sets the value of the haircutValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setHaircutValue(BigDecimal value) {
        this.haircutValue = value;
    }

    /**
     * Gets the value of the purchasedValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getPurchasedValue() {
        return purchasedValue;
    }

    /**
     * Sets the value of the purchasedValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setPurchasedValue(BigDecimal value) {
        this.purchasedValue = value;
    }

    /**
     * Gets the value of the appraisedValue property.
     *
     * @return possible object is
     *         {@link java.math.BigDecimal }
     */
    public BigDecimal getAppraisedValue() {
        return appraisedValue;
    }

    /**
     * Sets the value of the appraisedValue property.
     *
     * @param value allowed object is
     *              {@link java.math.BigDecimal }
     */
    public void setAppraisedValue(BigDecimal value) {
        this.appraisedValue = value;
    }

    /**
     * Gets the value of the province property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the value of the province property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProvince(String value) {
        this.province = value;
    }

    /**
     * Gets the value of the appraisal property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the appraisal property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAppraisal().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AppraisalType }
     */
    public List<AppraisalType> getAppraisal() {
        if (appraisal == null) {
            appraisal = new ArrayList<AppraisalType>();
        }
        return this.appraisal;
    }

    /**
     * Gets the value of the developer property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the developer property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeveloper().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link DeveloperType }
     */
    public List<DeveloperType> getDeveloper() {
        if (developer == null) {
            developer = new ArrayList<DeveloperType>();
        }
        return this.developer;
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
     * Gets the value of the requiredDocumentSet property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiredDocumentSet property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiredDocumentSet().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentSetType }
     */
    public List<DocumentSetType> getRequiredDocumentSet() {
        if (requiredDocumentSet == null) {
            requiredDocumentSet = new ArrayList<DocumentSetType>();
        }
        return this.requiredDocumentSet;
    }

}
