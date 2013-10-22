
package com.tmb.sme.data.responsesearchindividualcustomer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for personalDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personalDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custNbr">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="title" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="custId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="citizenId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="expDt">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="custType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
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
 *         &lt;element name="extension1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="branchOpening">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="branchResp" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dateOfBirth">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="gender">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="educationLevel">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="race">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="maritalStatus">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isoCitizenCtry">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ownRent" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="noOfChildren" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="spouseName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="spouseTin" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="13"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="spouseDateOfBirth" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="name1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="address1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cityStatePostalCd1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNbr1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="offExtension1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="jobTitle1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="selfEmployed1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="occupationCode1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="\d{1,3}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="grossSalary1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="13"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="currencyCode1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busType1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busType2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resAddrLine1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resAddrLine2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resAddrLine3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCity" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resPostalCd" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resCtry" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="resIsoCtryCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regAddrLine1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regAddrLine2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regAddrLine3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regCity" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regPostalCd" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regCtry" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regIsoCtryCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busAddrLine1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busAddrLine2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busAddrLine3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busCity" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busPostalCd" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busCtry" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="busIsoCtryCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="co" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="netFixedAssetAmt" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="hunter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="farmer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="historicalRating" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="issuedDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ratedBy" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="lastReviewDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nextReviewDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="haveData" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="localRating1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="localIssuedDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="localRatedBy" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="foreignRating1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="foreignIssuedDate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="foreignRatedBy" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber6" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension6" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType6" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber7" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension7" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType7" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber8" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension8" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType8" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="emailType1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="email1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="emailType2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="email2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="titleEng" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nameEng" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber9" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension9" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType9" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber11" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension11" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType11" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber12" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension12" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType12" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber13" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension13" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType13" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNumber14" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extension14" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneType14" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="statusCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="kycFlag" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
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
@XmlType(name = "personalDetail", propOrder = {
    "custNbr",
    "title",
    "name",
    "custId",
    "citizenId",
    "expDt",
    "custType",
    "telephoneNumber1",
    "extension1",
    "telephoneType1",
    "telephoneNumber2",
    "extension2",
    "telephoneType2",
    "telephoneNumber3",
    "extension3",
    "telephoneType3",
    "telephoneNumber4",
    "extension4",
    "telephoneType4",
    "branchOpening",
    "branchResp",
    "dateOfBirth",
    "gender",
    "educationLevel",
    "race",
    "maritalStatus",
    "isoCitizenCtry",
    "ownRent",
    "noOfChildren",
    "spouseName",
    "spouseTin",
    "spouseDateOfBirth",
    "name1",
    "address1",
    "cityStatePostalCd1",
    "telephoneNbr1",
    "offExtension1",
    "jobTitle1",
    "selfEmployed1",
    "occupationCode1",
    "grossSalary1",
    "currencyCode1",
    "busType1",
    "busType2",
    "resAddrLine1",
    "resAddrLine2",
    "resAddrLine3",
    "resCity",
    "resPostalCd",
    "resCtry",
    "resIsoCtryCode",
    "regAddrLine1",
    "regAddrLine2",
    "regAddrLine3",
    "regCity",
    "regPostalCd",
    "regCtry",
    "regIsoCtryCode",
    "busAddrLine1",
    "busAddrLine2",
    "busAddrLine3",
    "busCity",
    "busPostalCd",
    "busCtry",
    "busIsoCtryCode",
    "co",
    "netFixedAssetAmt",
    "hunter",
    "farmer",
    "historicalRating",
    "issuedDate",
    "ratedBy",
    "lastReviewDate",
    "nextReviewDate",
    "haveData",
    "localRating1",
    "localIssuedDate",
    "localRatedBy",
    "foreignRating1",
    "foreignIssuedDate",
    "foreignRatedBy",
    "telephoneNumber5",
    "extension5",
    "telephoneType5",
    "telephoneNumber6",
    "extension6",
    "telephoneType6",
    "telephoneNumber7",
    "extension7",
    "telephoneType7",
    "telephoneNumber8",
    "extension8",
    "telephoneType8",
    "emailType1",
    "email1",
    "emailType2",
    "email2",
    "titleEng",
    "nameEng",
    "telephoneNumber9",
    "extension9",
    "telephoneType9",
    "telephoneNumber10",
    "extension10",
    "telephoneType10",
    "telephoneNumber11",
    "extension11",
    "telephoneType11",
    "telephoneNumber12",
    "extension12",
    "telephoneType12",
    "telephoneNumber13",
    "extension13",
    "telephoneType13",
    "telephoneNumber14",
    "extension14",
    "telephoneType14",
    "statusCode",
    "kycFlag"
})
public class PersonalDetail {

    @XmlElement(required = true)
    protected String custNbr;
    protected String title;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String custId;
    @XmlElement(required = true)
    protected String citizenId;
    @XmlElement(required = true)
    protected String expDt;
    @XmlElement(required = true)
    protected String custType;
    protected String telephoneNumber1;
    protected String extension1;
    protected String telephoneType1;
    protected String telephoneNumber2;
    protected String extension2;
    protected String telephoneType2;
    protected String telephoneNumber3;
    protected String extension3;
    protected String telephoneType3;
    protected String telephoneNumber4;
    protected String extension4;
    protected String telephoneType4;
    @XmlElement(required = true)
    protected String branchOpening;
    protected String branchResp;
    @XmlElement(required = true)
    protected String dateOfBirth;
    @XmlElement(required = true)
    protected String gender;
    @XmlElement(required = true)
    protected String educationLevel;
    @XmlElement(required = true)
    protected String race;
    @XmlElement(required = true)
    protected String maritalStatus;
    @XmlElement(required = true)
    protected String isoCitizenCtry;
    protected String ownRent;
    protected String noOfChildren;
    protected String spouseName;
    protected String spouseTin;
    protected String spouseDateOfBirth;
    protected String name1;
    protected String address1;
    protected String cityStatePostalCd1;
    protected String telephoneNbr1;
    protected String offExtension1;
    protected String jobTitle1;
    protected String selfEmployed1;
    @XmlElement(required = true)
    protected String occupationCode1;
    protected BigDecimal grossSalary1;
    protected String currencyCode1;
    @XmlElement(required = true)
    protected String busType1;
    @XmlElement(required = true)
    protected String busType2;
    protected String resAddrLine1;
    protected String resAddrLine2;
    protected String resAddrLine3;
    protected String resCity;
    protected String resPostalCd;
    protected String resCtry;
    protected String resIsoCtryCode;
    protected String regAddrLine1;
    protected String regAddrLine2;
    protected String regAddrLine3;
    protected String regCity;
    protected String regPostalCd;
    protected String regCtry;
    protected String regIsoCtryCode;
    protected String busAddrLine1;
    protected String busAddrLine2;
    protected String busAddrLine3;
    protected String busCity;
    protected String busPostalCd;
    protected String busCtry;
    protected String busIsoCtryCode;
    protected String co;
    protected BigDecimal netFixedAssetAmt;
    protected String hunter;
    protected String farmer;
    protected String historicalRating;
    protected String issuedDate;
    protected String ratedBy;
    protected String lastReviewDate;
    protected String nextReviewDate;
    protected String haveData;
    protected String localRating1;
    protected String localIssuedDate;
    protected String localRatedBy;
    protected String foreignRating1;
    protected String foreignIssuedDate;
    protected String foreignRatedBy;
    protected String telephoneNumber5;
    protected String extension5;
    protected String telephoneType5;
    protected String telephoneNumber6;
    protected String extension6;
    protected String telephoneType6;
    protected String telephoneNumber7;
    protected String extension7;
    protected String telephoneType7;
    protected String telephoneNumber8;
    protected String extension8;
    protected String telephoneType8;
    protected String emailType1;
    protected String email1;
    protected String emailType2;
    protected String email2;
    protected String titleEng;
    protected String nameEng;
    protected String telephoneNumber9;
    protected String extension9;
    protected String telephoneType9;
    protected String telephoneNumber10;
    protected String extension10;
    protected String telephoneType10;
    protected String telephoneNumber11;
    protected String extension11;
    protected String telephoneType11;
    protected String telephoneNumber12;
    protected String extension12;
    protected String telephoneType12;
    protected String telephoneNumber13;
    protected String extension13;
    protected String telephoneType13;
    protected String telephoneNumber14;
    protected String extension14;
    protected String telephoneType14;
    protected String statusCode;
    protected String kycFlag;

    /**
     * Gets the value of the custNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustNbr() {
        return custNbr;
    }

    /**
     * Sets the value of the custNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustNbr(String value) {
        this.custNbr = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the custId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustId() {
        return custId;
    }

    /**
     * Sets the value of the custId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustId(String value) {
        this.custId = value;
    }

    /**
     * Gets the value of the citizenId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenId() {
        return citizenId;
    }

    /**
     * Sets the value of the citizenId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenId(String value) {
        this.citizenId = value;
    }

    /**
     * Gets the value of the expDt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpDt() {
        return expDt;
    }

    /**
     * Sets the value of the expDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpDt(String value) {
        this.expDt = value;
    }

    /**
     * Gets the value of the custType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustType() {
        return custType;
    }

    /**
     * Sets the value of the custType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustType(String value) {
        this.custType = value;
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
     * Gets the value of the extension1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension1() {
        return extension1;
    }

    /**
     * Sets the value of the extension1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension1(String value) {
        this.extension1 = value;
    }

    /**
     * Gets the value of the telephoneType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType1() {
        return telephoneType1;
    }

    /**
     * Sets the value of the telephoneType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType1(String value) {
        this.telephoneType1 = value;
    }

    /**
     * Gets the value of the telephoneNumber2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber2() {
        return telephoneNumber2;
    }

    /**
     * Sets the value of the telephoneNumber2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber2(String value) {
        this.telephoneNumber2 = value;
    }

    /**
     * Gets the value of the extension2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension2() {
        return extension2;
    }

    /**
     * Sets the value of the extension2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension2(String value) {
        this.extension2 = value;
    }

    /**
     * Gets the value of the telephoneType2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType2() {
        return telephoneType2;
    }

    /**
     * Sets the value of the telephoneType2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType2(String value) {
        this.telephoneType2 = value;
    }

    /**
     * Gets the value of the telephoneNumber3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber3() {
        return telephoneNumber3;
    }

    /**
     * Sets the value of the telephoneNumber3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber3(String value) {
        this.telephoneNumber3 = value;
    }

    /**
     * Gets the value of the extension3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension3() {
        return extension3;
    }

    /**
     * Sets the value of the extension3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension3(String value) {
        this.extension3 = value;
    }

    /**
     * Gets the value of the telephoneType3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType3() {
        return telephoneType3;
    }

    /**
     * Sets the value of the telephoneType3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType3(String value) {
        this.telephoneType3 = value;
    }

    /**
     * Gets the value of the telephoneNumber4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber4() {
        return telephoneNumber4;
    }

    /**
     * Sets the value of the telephoneNumber4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber4(String value) {
        this.telephoneNumber4 = value;
    }

    /**
     * Gets the value of the extension4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension4() {
        return extension4;
    }

    /**
     * Sets the value of the extension4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension4(String value) {
        this.extension4 = value;
    }

    /**
     * Gets the value of the telephoneType4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType4() {
        return telephoneType4;
    }

    /**
     * Sets the value of the telephoneType4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType4(String value) {
        this.telephoneType4 = value;
    }

    /**
     * Gets the value of the branchOpening property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchOpening() {
        return branchOpening;
    }

    /**
     * Sets the value of the branchOpening property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchOpening(String value) {
        this.branchOpening = value;
    }

    /**
     * Gets the value of the branchResp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchResp() {
        return branchResp;
    }

    /**
     * Sets the value of the branchResp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchResp(String value) {
        this.branchResp = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the educationLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducationLevel() {
        return educationLevel;
    }

    /**
     * Sets the value of the educationLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationLevel(String value) {
        this.educationLevel = value;
    }

    /**
     * Gets the value of the race property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRace() {
        return race;
    }

    /**
     * Sets the value of the race property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRace(String value) {
        this.race = value;
    }

    /**
     * Gets the value of the maritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the isoCitizenCtry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsoCitizenCtry() {
        return isoCitizenCtry;
    }

    /**
     * Sets the value of the isoCitizenCtry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsoCitizenCtry(String value) {
        this.isoCitizenCtry = value;
    }

    /**
     * Gets the value of the ownRent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnRent() {
        return ownRent;
    }

    /**
     * Sets the value of the ownRent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnRent(String value) {
        this.ownRent = value;
    }

    /**
     * Gets the value of the noOfChildren property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoOfChildren() {
        return noOfChildren;
    }

    /**
     * Sets the value of the noOfChildren property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoOfChildren(String value) {
        this.noOfChildren = value;
    }

    /**
     * Gets the value of the spouseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseName() {
        return spouseName;
    }

    /**
     * Sets the value of the spouseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseName(String value) {
        this.spouseName = value;
    }

    /**
     * Gets the value of the spouseTin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseTin() {
        return spouseTin;
    }

    /**
     * Sets the value of the spouseTin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseTin(String value) {
        this.spouseTin = value;
    }

    /**
     * Gets the value of the spouseDateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpouseDateOfBirth() {
        return spouseDateOfBirth;
    }

    /**
     * Sets the value of the spouseDateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpouseDateOfBirth(String value) {
        this.spouseDateOfBirth = value;
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
     * Gets the value of the cityStatePostalCd1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityStatePostalCd1() {
        return cityStatePostalCd1;
    }

    /**
     * Sets the value of the cityStatePostalCd1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityStatePostalCd1(String value) {
        this.cityStatePostalCd1 = value;
    }

    /**
     * Gets the value of the telephoneNbr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNbr1() {
        return telephoneNbr1;
    }

    /**
     * Sets the value of the telephoneNbr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNbr1(String value) {
        this.telephoneNbr1 = value;
    }

    /**
     * Gets the value of the offExtension1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffExtension1() {
        return offExtension1;
    }

    /**
     * Sets the value of the offExtension1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffExtension1(String value) {
        this.offExtension1 = value;
    }

    /**
     * Gets the value of the jobTitle1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTitle1() {
        return jobTitle1;
    }

    /**
     * Sets the value of the jobTitle1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTitle1(String value) {
        this.jobTitle1 = value;
    }

    /**
     * Gets the value of the selfEmployed1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelfEmployed1() {
        return selfEmployed1;
    }

    /**
     * Sets the value of the selfEmployed1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelfEmployed1(String value) {
        this.selfEmployed1 = value;
    }

    /**
     * Gets the value of the occupationCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationCode1() {
        return occupationCode1;
    }

    /**
     * Sets the value of the occupationCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationCode1(String value) {
        this.occupationCode1 = value;
    }

    /**
     * Gets the value of the grossSalary1 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGrossSalary1() {
        return grossSalary1;
    }

    /**
     * Sets the value of the grossSalary1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGrossSalary1(BigDecimal value) {
        this.grossSalary1 = value;
    }

    /**
     * Gets the value of the currencyCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode1() {
        return currencyCode1;
    }

    /**
     * Sets the value of the currencyCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode1(String value) {
        this.currencyCode1 = value;
    }

    /**
     * Gets the value of the busType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusType1() {
        return busType1;
    }

    /**
     * Sets the value of the busType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusType1(String value) {
        this.busType1 = value;
    }

    /**
     * Gets the value of the busType2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusType2() {
        return busType2;
    }

    /**
     * Sets the value of the busType2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusType2(String value) {
        this.busType2 = value;
    }

    /**
     * Gets the value of the resAddrLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResAddrLine1() {
        return resAddrLine1;
    }

    /**
     * Sets the value of the resAddrLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResAddrLine1(String value) {
        this.resAddrLine1 = value;
    }

    /**
     * Gets the value of the resAddrLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResAddrLine2() {
        return resAddrLine2;
    }

    /**
     * Sets the value of the resAddrLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResAddrLine2(String value) {
        this.resAddrLine2 = value;
    }

    /**
     * Gets the value of the resAddrLine3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResAddrLine3() {
        return resAddrLine3;
    }

    /**
     * Sets the value of the resAddrLine3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResAddrLine3(String value) {
        this.resAddrLine3 = value;
    }

    /**
     * Gets the value of the resCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResCity() {
        return resCity;
    }

    /**
     * Sets the value of the resCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResCity(String value) {
        this.resCity = value;
    }

    /**
     * Gets the value of the resPostalCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResPostalCd() {
        return resPostalCd;
    }

    /**
     * Sets the value of the resPostalCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResPostalCd(String value) {
        this.resPostalCd = value;
    }

    /**
     * Gets the value of the resCtry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResCtry() {
        return resCtry;
    }

    /**
     * Sets the value of the resCtry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResCtry(String value) {
        this.resCtry = value;
    }

    /**
     * Gets the value of the resIsoCtryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResIsoCtryCode() {
        return resIsoCtryCode;
    }

    /**
     * Sets the value of the resIsoCtryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResIsoCtryCode(String value) {
        this.resIsoCtryCode = value;
    }

    /**
     * Gets the value of the regAddrLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegAddrLine1() {
        return regAddrLine1;
    }

    /**
     * Sets the value of the regAddrLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegAddrLine1(String value) {
        this.regAddrLine1 = value;
    }

    /**
     * Gets the value of the regAddrLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegAddrLine2() {
        return regAddrLine2;
    }

    /**
     * Sets the value of the regAddrLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegAddrLine2(String value) {
        this.regAddrLine2 = value;
    }

    /**
     * Gets the value of the regAddrLine3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegAddrLine3() {
        return regAddrLine3;
    }

    /**
     * Sets the value of the regAddrLine3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegAddrLine3(String value) {
        this.regAddrLine3 = value;
    }

    /**
     * Gets the value of the regCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegCity() {
        return regCity;
    }

    /**
     * Sets the value of the regCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegCity(String value) {
        this.regCity = value;
    }

    /**
     * Gets the value of the regPostalCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegPostalCd() {
        return regPostalCd;
    }

    /**
     * Sets the value of the regPostalCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegPostalCd(String value) {
        this.regPostalCd = value;
    }

    /**
     * Gets the value of the regCtry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegCtry() {
        return regCtry;
    }

    /**
     * Sets the value of the regCtry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegCtry(String value) {
        this.regCtry = value;
    }

    /**
     * Gets the value of the regIsoCtryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegIsoCtryCode() {
        return regIsoCtryCode;
    }

    /**
     * Sets the value of the regIsoCtryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegIsoCtryCode(String value) {
        this.regIsoCtryCode = value;
    }

    /**
     * Gets the value of the busAddrLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusAddrLine1() {
        return busAddrLine1;
    }

    /**
     * Sets the value of the busAddrLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusAddrLine1(String value) {
        this.busAddrLine1 = value;
    }

    /**
     * Gets the value of the busAddrLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusAddrLine2() {
        return busAddrLine2;
    }

    /**
     * Sets the value of the busAddrLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusAddrLine2(String value) {
        this.busAddrLine2 = value;
    }

    /**
     * Gets the value of the busAddrLine3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusAddrLine3() {
        return busAddrLine3;
    }

    /**
     * Sets the value of the busAddrLine3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusAddrLine3(String value) {
        this.busAddrLine3 = value;
    }

    /**
     * Gets the value of the busCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusCity() {
        return busCity;
    }

    /**
     * Sets the value of the busCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusCity(String value) {
        this.busCity = value;
    }

    /**
     * Gets the value of the busPostalCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusPostalCd() {
        return busPostalCd;
    }

    /**
     * Sets the value of the busPostalCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusPostalCd(String value) {
        this.busPostalCd = value;
    }

    /**
     * Gets the value of the busCtry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusCtry() {
        return busCtry;
    }

    /**
     * Sets the value of the busCtry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusCtry(String value) {
        this.busCtry = value;
    }

    /**
     * Gets the value of the busIsoCtryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusIsoCtryCode() {
        return busIsoCtryCode;
    }

    /**
     * Sets the value of the busIsoCtryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusIsoCtryCode(String value) {
        this.busIsoCtryCode = value;
    }

    /**
     * Gets the value of the co property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCo() {
        return co;
    }

    /**
     * Sets the value of the co property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCo(String value) {
        this.co = value;
    }

    /**
     * Gets the value of the netFixedAssetAmt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetFixedAssetAmt() {
        return netFixedAssetAmt;
    }

    /**
     * Sets the value of the netFixedAssetAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetFixedAssetAmt(BigDecimal value) {
        this.netFixedAssetAmt = value;
    }

    /**
     * Gets the value of the hunter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHunter() {
        return hunter;
    }

    /**
     * Sets the value of the hunter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHunter(String value) {
        this.hunter = value;
    }

    /**
     * Gets the value of the farmer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFarmer() {
        return farmer;
    }

    /**
     * Sets the value of the farmer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFarmer(String value) {
        this.farmer = value;
    }

    /**
     * Gets the value of the historicalRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistoricalRating() {
        return historicalRating;
    }

    /**
     * Sets the value of the historicalRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistoricalRating(String value) {
        this.historicalRating = value;
    }

    /**
     * Gets the value of the issuedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuedDate() {
        return issuedDate;
    }

    /**
     * Sets the value of the issuedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuedDate(String value) {
        this.issuedDate = value;
    }

    /**
     * Gets the value of the ratedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatedBy() {
        return ratedBy;
    }

    /**
     * Sets the value of the ratedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatedBy(String value) {
        this.ratedBy = value;
    }

    /**
     * Gets the value of the lastReviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * Sets the value of the lastReviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastReviewDate(String value) {
        this.lastReviewDate = value;
    }

    /**
     * Gets the value of the nextReviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextReviewDate() {
        return nextReviewDate;
    }

    /**
     * Sets the value of the nextReviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextReviewDate(String value) {
        this.nextReviewDate = value;
    }

    /**
     * Gets the value of the haveData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHaveData() {
        return haveData;
    }

    /**
     * Sets the value of the haveData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHaveData(String value) {
        this.haveData = value;
    }

    /**
     * Gets the value of the localRating1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalRating1() {
        return localRating1;
    }

    /**
     * Sets the value of the localRating1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalRating1(String value) {
        this.localRating1 = value;
    }

    /**
     * Gets the value of the localIssuedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalIssuedDate() {
        return localIssuedDate;
    }

    /**
     * Sets the value of the localIssuedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalIssuedDate(String value) {
        this.localIssuedDate = value;
    }

    /**
     * Gets the value of the localRatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalRatedBy() {
        return localRatedBy;
    }

    /**
     * Sets the value of the localRatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalRatedBy(String value) {
        this.localRatedBy = value;
    }

    /**
     * Gets the value of the foreignRating1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignRating1() {
        return foreignRating1;
    }

    /**
     * Sets the value of the foreignRating1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignRating1(String value) {
        this.foreignRating1 = value;
    }

    /**
     * Gets the value of the foreignIssuedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignIssuedDate() {
        return foreignIssuedDate;
    }

    /**
     * Sets the value of the foreignIssuedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignIssuedDate(String value) {
        this.foreignIssuedDate = value;
    }

    /**
     * Gets the value of the foreignRatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignRatedBy() {
        return foreignRatedBy;
    }

    /**
     * Sets the value of the foreignRatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignRatedBy(String value) {
        this.foreignRatedBy = value;
    }

    /**
     * Gets the value of the telephoneNumber5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber5() {
        return telephoneNumber5;
    }

    /**
     * Sets the value of the telephoneNumber5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber5(String value) {
        this.telephoneNumber5 = value;
    }

    /**
     * Gets the value of the extension5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension5() {
        return extension5;
    }

    /**
     * Sets the value of the extension5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension5(String value) {
        this.extension5 = value;
    }

    /**
     * Gets the value of the telephoneType5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType5() {
        return telephoneType5;
    }

    /**
     * Sets the value of the telephoneType5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType5(String value) {
        this.telephoneType5 = value;
    }

    /**
     * Gets the value of the telephoneNumber6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber6() {
        return telephoneNumber6;
    }

    /**
     * Sets the value of the telephoneNumber6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber6(String value) {
        this.telephoneNumber6 = value;
    }

    /**
     * Gets the value of the extension6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension6() {
        return extension6;
    }

    /**
     * Sets the value of the extension6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension6(String value) {
        this.extension6 = value;
    }

    /**
     * Gets the value of the telephoneType6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType6() {
        return telephoneType6;
    }

    /**
     * Sets the value of the telephoneType6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType6(String value) {
        this.telephoneType6 = value;
    }

    /**
     * Gets the value of the telephoneNumber7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber7() {
        return telephoneNumber7;
    }

    /**
     * Sets the value of the telephoneNumber7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber7(String value) {
        this.telephoneNumber7 = value;
    }

    /**
     * Gets the value of the extension7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension7() {
        return extension7;
    }

    /**
     * Sets the value of the extension7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension7(String value) {
        this.extension7 = value;
    }

    /**
     * Gets the value of the telephoneType7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType7() {
        return telephoneType7;
    }

    /**
     * Sets the value of the telephoneType7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType7(String value) {
        this.telephoneType7 = value;
    }

    /**
     * Gets the value of the telephoneNumber8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber8() {
        return telephoneNumber8;
    }

    /**
     * Sets the value of the telephoneNumber8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber8(String value) {
        this.telephoneNumber8 = value;
    }

    /**
     * Gets the value of the extension8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension8() {
        return extension8;
    }

    /**
     * Sets the value of the extension8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension8(String value) {
        this.extension8 = value;
    }

    /**
     * Gets the value of the telephoneType8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType8() {
        return telephoneType8;
    }

    /**
     * Sets the value of the telephoneType8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType8(String value) {
        this.telephoneType8 = value;
    }

    /**
     * Gets the value of the emailType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailType1() {
        return emailType1;
    }

    /**
     * Sets the value of the emailType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailType1(String value) {
        this.emailType1 = value;
    }

    /**
     * Gets the value of the email1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail1() {
        return email1;
    }

    /**
     * Sets the value of the email1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail1(String value) {
        this.email1 = value;
    }

    /**
     * Gets the value of the emailType2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailType2() {
        return emailType2;
    }

    /**
     * Sets the value of the emailType2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailType2(String value) {
        this.emailType2 = value;
    }

    /**
     * Gets the value of the email2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail2() {
        return email2;
    }

    /**
     * Sets the value of the email2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail2(String value) {
        this.email2 = value;
    }

    /**
     * Gets the value of the titleEng property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleEng() {
        return titleEng;
    }

    /**
     * Sets the value of the titleEng property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleEng(String value) {
        this.titleEng = value;
    }

    /**
     * Gets the value of the nameEng property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameEng() {
        return nameEng;
    }

    /**
     * Sets the value of the nameEng property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameEng(String value) {
        this.nameEng = value;
    }

    /**
     * Gets the value of the telephoneNumber9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber9() {
        return telephoneNumber9;
    }

    /**
     * Sets the value of the telephoneNumber9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber9(String value) {
        this.telephoneNumber9 = value;
    }

    /**
     * Gets the value of the extension9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension9() {
        return extension9;
    }

    /**
     * Sets the value of the extension9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension9(String value) {
        this.extension9 = value;
    }

    /**
     * Gets the value of the telephoneType9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType9() {
        return telephoneType9;
    }

    /**
     * Sets the value of the telephoneType9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType9(String value) {
        this.telephoneType9 = value;
    }

    /**
     * Gets the value of the telephoneNumber10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber10() {
        return telephoneNumber10;
    }

    /**
     * Sets the value of the telephoneNumber10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber10(String value) {
        this.telephoneNumber10 = value;
    }

    /**
     * Gets the value of the extension10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension10() {
        return extension10;
    }

    /**
     * Sets the value of the extension10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension10(String value) {
        this.extension10 = value;
    }

    /**
     * Gets the value of the telephoneType10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType10() {
        return telephoneType10;
    }

    /**
     * Sets the value of the telephoneType10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType10(String value) {
        this.telephoneType10 = value;
    }

    /**
     * Gets the value of the telephoneNumber11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber11() {
        return telephoneNumber11;
    }

    /**
     * Sets the value of the telephoneNumber11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber11(String value) {
        this.telephoneNumber11 = value;
    }

    /**
     * Gets the value of the extension11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension11() {
        return extension11;
    }

    /**
     * Sets the value of the extension11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension11(String value) {
        this.extension11 = value;
    }

    /**
     * Gets the value of the telephoneType11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType11() {
        return telephoneType11;
    }

    /**
     * Sets the value of the telephoneType11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType11(String value) {
        this.telephoneType11 = value;
    }

    /**
     * Gets the value of the telephoneNumber12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber12() {
        return telephoneNumber12;
    }

    /**
     * Sets the value of the telephoneNumber12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber12(String value) {
        this.telephoneNumber12 = value;
    }

    /**
     * Gets the value of the extension12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension12() {
        return extension12;
    }

    /**
     * Sets the value of the extension12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension12(String value) {
        this.extension12 = value;
    }

    /**
     * Gets the value of the telephoneType12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType12() {
        return telephoneType12;
    }

    /**
     * Sets the value of the telephoneType12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType12(String value) {
        this.telephoneType12 = value;
    }

    /**
     * Gets the value of the telephoneNumber13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber13() {
        return telephoneNumber13;
    }

    /**
     * Sets the value of the telephoneNumber13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber13(String value) {
        this.telephoneNumber13 = value;
    }

    /**
     * Gets the value of the extension13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension13() {
        return extension13;
    }

    /**
     * Sets the value of the extension13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension13(String value) {
        this.extension13 = value;
    }

    /**
     * Gets the value of the telephoneType13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType13() {
        return telephoneType13;
    }

    /**
     * Sets the value of the telephoneType13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType13(String value) {
        this.telephoneType13 = value;
    }

    /**
     * Gets the value of the telephoneNumber14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber14() {
        return telephoneNumber14;
    }

    /**
     * Sets the value of the telephoneNumber14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber14(String value) {
        this.telephoneNumber14 = value;
    }

    /**
     * Gets the value of the extension14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension14() {
        return extension14;
    }

    /**
     * Sets the value of the extension14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension14(String value) {
        this.extension14 = value;
    }

    /**
     * Gets the value of the telephoneType14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneType14() {
        return telephoneType14;
    }

    /**
     * Sets the value of the telephoneType14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneType14(String value) {
        this.telephoneType14 = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the kycFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKycFlag() {
        return kycFlag;
    }

    /**
     * Sets the value of the kycFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKycFlag(String value) {
        this.kycFlag = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("custNbr", custNbr)
                .append("title", title)
                .append("name", name)
                .append("custId", custId)
                .append("citizenId", citizenId)
                .append("expDt", expDt)
                .append("custType", custType)
                .append("telephoneNumber1", telephoneNumber1)
                .append("extension1", extension1)
                .append("telephoneType1", telephoneType1)
                .append("telephoneNumber2", telephoneNumber2)
                .append("extension2", extension2)
                .append("telephoneType2", telephoneType2)
                .append("telephoneNumber3", telephoneNumber3)
                .append("extension3", extension3)
                .append("telephoneType3", telephoneType3)
                .append("telephoneNumber4", telephoneNumber4)
                .append("extension4", extension4)
                .append("telephoneType4", telephoneType4)
                .append("branchOpening", branchOpening)
                .append("branchResp", branchResp)
                .append("dateOfBirth", dateOfBirth)
                .append("gender", gender)
                .append("educationLevel", educationLevel)
                .append("race", race)
                .append("maritalStatus", maritalStatus)
                .append("isoCitizenCtry", isoCitizenCtry)
                .append("ownRent", ownRent)
                .append("noOfChildren", noOfChildren)
                .append("spouseName", spouseName)
                .append("spouseTin", spouseTin)
                .append("spouseDateOfBirth", spouseDateOfBirth)
                .append("name1", name1)
                .append("address1", address1)
                .append("cityStatePostalCd1", cityStatePostalCd1)
                .append("telephoneNbr1", telephoneNbr1)
                .append("offExtension1", offExtension1)
                .append("jobTitle1", jobTitle1)
                .append("selfEmployed1", selfEmployed1)
                .append("occupationCode1", occupationCode1)
                .append("grossSalary1", grossSalary1)
                .append("currencyCode1", currencyCode1)
                .append("busType1", busType1)
                .append("busType2", busType2)
                .append("resAddrLine1", resAddrLine1)
                .append("resAddrLine2", resAddrLine2)
                .append("resAddrLine3", resAddrLine3)
                .append("resCity", resCity)
                .append("resPostalCd", resPostalCd)
                .append("resCtry", resCtry)
                .append("resIsoCtryCode", resIsoCtryCode)
                .append("regAddrLine1", regAddrLine1)
                .append("regAddrLine2", regAddrLine2)
                .append("regAddrLine3", regAddrLine3)
                .append("regCity", regCity)
                .append("regPostalCd", regPostalCd)
                .append("regCtry", regCtry)
                .append("regIsoCtryCode", regIsoCtryCode)
                .append("busAddrLine1", busAddrLine1)
                .append("busAddrLine2", busAddrLine2)
                .append("busAddrLine3", busAddrLine3)
                .append("busCity", busCity)
                .append("busPostalCd", busPostalCd)
                .append("busCtry", busCtry)
                .append("busIsoCtryCode", busIsoCtryCode)
                .append("co", co)
                .append("netFixedAssetAmt", netFixedAssetAmt)
                .append("hunter", hunter)
                .append("farmer", farmer)
                .append("historicalRating", historicalRating)
                .append("issuedDate", issuedDate)
                .append("ratedBy", ratedBy)
                .append("lastReviewDate", lastReviewDate)
                .append("nextReviewDate", nextReviewDate)
                .append("haveData", haveData)
                .append("localRating1", localRating1)
                .append("localIssuedDate", localIssuedDate)
                .append("localRatedBy", localRatedBy)
                .append("foreignRating1", foreignRating1)
                .append("foreignIssuedDate", foreignIssuedDate)
                .append("foreignRatedBy", foreignRatedBy)
                .append("telephoneNumber5", telephoneNumber5)
                .append("extension5", extension5)
                .append("telephoneType5", telephoneType5)
                .append("telephoneNumber6", telephoneNumber6)
                .append("extension6", extension6)
                .append("telephoneType6", telephoneType6)
                .append("telephoneNumber7", telephoneNumber7)
                .append("extension7", extension7)
                .append("telephoneType7", telephoneType7)
                .append("telephoneNumber8", telephoneNumber8)
                .append("extension8", extension8)
                .append("telephoneType8", telephoneType8)
                .append("emailType1", emailType1)
                .append("email1", email1)
                .append("emailType2", emailType2)
                .append("email2", email2)
                .append("titleEng", titleEng)
                .append("nameEng", nameEng)
                .append("telephoneNumber9", telephoneNumber9)
                .append("extension9", extension9)
                .append("telephoneType9", telephoneType9)
                .append("telephoneNumber10", telephoneNumber10)
                .append("extension10", extension10)
                .append("telephoneType10", telephoneType10)
                .append("telephoneNumber11", telephoneNumber11)
                .append("extension11", extension11)
                .append("telephoneType11", telephoneType11)
                .append("telephoneNumber12", telephoneNumber12)
                .append("extension12", extension12)
                .append("telephoneType12", telephoneType12)
                .append("telephoneNumber13", telephoneNumber13)
                .append("extension13", extension13)
                .append("telephoneType13", telephoneType13)
                .append("telephoneNumber14", telephoneNumber14)
                .append("extension14", extension14)
                .append("telephoneType14", telephoneType14)
                .append("statusCode", statusCode)
                .append("kycFlag", kycFlag)
                .toString();
    }
}
