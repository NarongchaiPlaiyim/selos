
package com.tmb.sme.data.responsesearchcorporatecustomer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for corporateDetail complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="corporateDetail">
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
 *         &lt;element name="thaiName1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="thaiName2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="thaiName3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="engName1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="engName2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="engName3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="citizenCId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="estDate">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isoCountry">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addressLine1PRI">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addressLine2PRI">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addrTumbon">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addrAumper">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="city">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="postalCd">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctry">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isoCtryCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="emailAddress1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="70"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addressLine1REG">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="addressLine2REG">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comRegTumbon">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comRegAumper">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="city2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ctry2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isoCtryCode2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="emailAddress2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="70"/>
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
 *         &lt;element name="titleOfContact" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="contactPerson" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="telephoneNbr" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="contactReason" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="description" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="typeOfOwnership" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="salesVolume" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="\d{0,13}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nbrOfEmps" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="\d{0,10}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="smallBusiness" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
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
 *         &lt;element name="co" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="netFixedAssetAmt">
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
 *         &lt;element name="capitalRegAmt" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="capitalPaidAmt" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
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
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
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
 *               &lt;pattern value="[0-9]{2}/[0-9]{2}/[0-9]{4}"/>
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
 *         &lt;element name="telephoneType2" minOccurs="0">
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
 *         &lt;element name="telephoneType3" minOccurs="0">
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
 *         &lt;element name="telephoneType4" minOccurs="0">
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
 *         &lt;element name="telephoneType5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
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
 *         &lt;element name="telephoneType6" minOccurs="0">
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
 *         &lt;element name="telephoneType7" minOccurs="0">
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
 *         &lt;element name="telephoneType8" minOccurs="0">
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
 *         &lt;element name="emailType1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="1"/>
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
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "corporateDetail", propOrder = {
        "custNbr",
        "title",
        "thaiName1",
        "thaiName2",
        "thaiName3",
        "engName1",
        "engName2",
        "engName3",
        "cId",
        "citizenCId",
        "estDate",
        "isoCountry",
        "addressLine1PRI",
        "addressLine2PRI",
        "addrTumbon",
        "addrAumper",
        "city",
        "postalCd",
        "ctry",
        "isoCtryCode",
        "emailAddress1",
        "addressLine1REG",
        "addressLine2REG",
        "comRegTumbon",
        "comRegAumper",
        "city2",
        "ctry2",
        "isoCtryCode2",
        "emailAddress2",
        "telephoneType1",
        "telephoneNumber1",
        "extension1",
        "titleOfContact",
        "contactPerson",
        "telephoneNbr",
        "contactReason",
        "description",
        "typeOfOwnership",
        "salesVolume",
        "nbrOfEmps",
        "smallBusiness",
        "custType",
        "busType1",
        "busType2",
        "branchOpening",
        "branchResp",
        "co",
        "netFixedAssetAmt",
        "hunter",
        "farmer",
        "capitalRegAmt",
        "capitalPaidAmt",
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
        "telephoneType2",
        "telephoneNumber2",
        "extension2",
        "telephoneType3",
        "telephoneNumber3",
        "extension3",
        "telephoneType4",
        "telephoneNumber4",
        "extension4",
        "telephoneType5",
        "telephoneNumber5",
        "extension5",
        "telephoneType6",
        "telephoneNumber6",
        "extension6",
        "telephoneType7",
        "telephoneNumber7",
        "extension7",
        "telephoneType8",
        "telephoneNumber8",
        "extension8",
        "emailType1",
        "emailType2",
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
        "telephoneType12"
})
public class CorporateDetail {

    @XmlElement(required = true)
    protected String custNbr;
    protected String title;
    @XmlElement(required = true)
    protected String thaiName1;
    protected String thaiName2;
    protected String thaiName3;
    @XmlElement(required = true)
    protected String engName1;
    protected String engName2;
    protected String engName3;
    @XmlElement(required = true)
    protected String cId;
    @XmlElement(required = true)
    protected String citizenCId;
    @XmlElement(required = true)
    protected String estDate;
    @XmlElement(required = true)
    protected String isoCountry;
    @XmlElement(required = true)
    protected String addressLine1PRI;
    @XmlElement(required = true)
    protected String addressLine2PRI;
    @XmlElement(required = true)
    protected String addrTumbon;
    @XmlElement(required = true)
    protected String addrAumper;
    @XmlElement(required = true)
    protected String city;
    @XmlElement(required = true)
    protected String postalCd;
    @XmlElement(required = true)
    protected String ctry;
    @XmlElement(required = true)
    protected String isoCtryCode;
    protected String emailAddress1;
    @XmlElement(required = true)
    protected String addressLine1REG;
    @XmlElement(required = true)
    protected String addressLine2REG;
    @XmlElement(required = true)
    protected String comRegTumbon;
    @XmlElement(required = true)
    protected String comRegAumper;
    @XmlElement(required = true)
    protected String city2;
    @XmlElement(required = true)
    protected String ctry2;
    @XmlElement(required = true)
    protected String isoCtryCode2;
    protected String emailAddress2;
    protected String telephoneType1;
    protected String telephoneNumber1;
    protected String extension1;
    protected String titleOfContact;
    protected String contactPerson;
    protected String telephoneNbr;
    protected String contactReason;
    protected String description;
    protected String typeOfOwnership;
    protected String salesVolume;
    protected String nbrOfEmps;
    protected String smallBusiness;
    @XmlElement(required = true)
    protected String custType;
    @XmlElement(required = true)
    protected String busType1;
    @XmlElement(required = true)
    protected String busType2;
    @XmlElement(required = true)
    protected String branchOpening;
    protected String branchResp;
    protected String co;
    @XmlElement(required = true)
    protected BigDecimal netFixedAssetAmt;
    protected String hunter;
    protected String farmer;
    protected BigDecimal capitalRegAmt;
    protected BigDecimal capitalPaidAmt;
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
    protected String telephoneType2;
    protected String telephoneNumber2;
    protected String extension2;
    protected String telephoneType3;
    protected String telephoneNumber3;
    protected String extension3;
    protected String telephoneType4;
    protected String telephoneNumber4;
    protected String extension4;
    protected String telephoneType5;
    protected String telephoneNumber5;
    protected String extension5;
    protected String telephoneType6;
    protected String telephoneNumber6;
    protected String extension6;
    protected String telephoneType7;
    protected String telephoneNumber7;
    protected String extension7;
    protected String telephoneType8;
    protected String telephoneNumber8;
    protected String extension8;
    protected String emailType1;
    protected String emailType2;
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

    /**
     * Gets the value of the custNbr property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCustNbr() {
        return custNbr;
    }

    /**
     * Sets the value of the custNbr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustNbr(String value) {
        this.custNbr = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the thaiName1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getThaiName1() {
        return thaiName1;
    }

    /**
     * Sets the value of the thaiName1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setThaiName1(String value) {
        this.thaiName1 = value;
    }

    /**
     * Gets the value of the thaiName2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getThaiName2() {
        return thaiName2;
    }

    /**
     * Sets the value of the thaiName2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setThaiName2(String value) {
        this.thaiName2 = value;
    }

    /**
     * Gets the value of the thaiName3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getThaiName3() {
        return thaiName3;
    }

    /**
     * Sets the value of the thaiName3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setThaiName3(String value) {
        this.thaiName3 = value;
    }

    /**
     * Gets the value of the engName1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEngName1() {
        return engName1;
    }

    /**
     * Sets the value of the engName1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEngName1(String value) {
        this.engName1 = value;
    }

    /**
     * Gets the value of the engName2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEngName2() {
        return engName2;
    }

    /**
     * Sets the value of the engName2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEngName2(String value) {
        this.engName2 = value;
    }

    /**
     * Gets the value of the engName3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEngName3() {
        return engName3;
    }

    /**
     * Sets the value of the engName3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEngName3(String value) {
        this.engName3 = value;
    }

    /**
     * Gets the value of the cId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCId() {
        return cId;
    }

    /**
     * Sets the value of the cId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCId(String value) {
        this.cId = value;
    }

    /**
     * Gets the value of the citizenCId property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCitizenCId() {
        return citizenCId;
    }

    /**
     * Sets the value of the citizenCId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCitizenCId(String value) {
        this.citizenCId = value;
    }

    /**
     * Gets the value of the estDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEstDate() {
        return estDate;
    }

    /**
     * Sets the value of the estDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEstDate(String value) {
        this.estDate = value;
    }

    /**
     * Gets the value of the isoCountry property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIsoCountry() {
        return isoCountry;
    }

    /**
     * Sets the value of the isoCountry property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIsoCountry(String value) {
        this.isoCountry = value;
    }

    /**
     * Gets the value of the addressLine1PRI property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddressLine1PRI() {
        return addressLine1PRI;
    }

    /**
     * Sets the value of the addressLine1PRI property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine1PRI(String value) {
        this.addressLine1PRI = value;
    }

    /**
     * Gets the value of the addressLine2PRI property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddressLine2PRI() {
        return addressLine2PRI;
    }

    /**
     * Sets the value of the addressLine2PRI property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine2PRI(String value) {
        this.addressLine2PRI = value;
    }

    /**
     * Gets the value of the addrTumbon property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddrTumbon() {
        return addrTumbon;
    }

    /**
     * Sets the value of the addrTumbon property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddrTumbon(String value) {
        this.addrTumbon = value;
    }

    /**
     * Gets the value of the addrAumper property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddrAumper() {
        return addrAumper;
    }

    /**
     * Sets the value of the addrAumper property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddrAumper(String value) {
        this.addrAumper = value;
    }

    /**
     * Gets the value of the city property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the postalCd property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPostalCd() {
        return postalCd;
    }

    /**
     * Sets the value of the postalCd property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPostalCd(String value) {
        this.postalCd = value;
    }

    /**
     * Gets the value of the ctry property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtry() {
        return ctry;
    }

    /**
     * Sets the value of the ctry property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtry(String value) {
        this.ctry = value;
    }

    /**
     * Gets the value of the isoCtryCode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIsoCtryCode() {
        return isoCtryCode;
    }

    /**
     * Sets the value of the isoCtryCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIsoCtryCode(String value) {
        this.isoCtryCode = value;
    }

    /**
     * Gets the value of the emailAddress1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEmailAddress1() {
        return emailAddress1;
    }

    /**
     * Sets the value of the emailAddress1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailAddress1(String value) {
        this.emailAddress1 = value;
    }

    /**
     * Gets the value of the addressLine1REG property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddressLine1REG() {
        return addressLine1REG;
    }

    /**
     * Sets the value of the addressLine1REG property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine1REG(String value) {
        this.addressLine1REG = value;
    }

    /**
     * Gets the value of the addressLine2REG property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAddressLine2REG() {
        return addressLine2REG;
    }

    /**
     * Sets the value of the addressLine2REG property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine2REG(String value) {
        this.addressLine2REG = value;
    }

    /**
     * Gets the value of the comRegTumbon property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getComRegTumbon() {

        try {
            return new String(comRegTumbon.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the value of the comRegTumbon property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComRegTumbon(String value) {
        this.comRegTumbon = value;
    }

    /**
     * Gets the value of the comRegAumper property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getComRegAumper() {
        try {
            return new String(comRegAumper.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the value of the comRegAumper property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setComRegAumper(String value) {
        this.comRegAumper = value;
    }

    /**
     * Gets the value of the city2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCity2() {
        return city2;
    }

    /**
     * Sets the value of the city2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCity2(String value) {
        this.city2 = value;
    }

    /**
     * Gets the value of the ctry2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCtry2() {
        return ctry2;
    }

    /**
     * Sets the value of the ctry2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCtry2(String value) {
        this.ctry2 = value;
    }

    /**
     * Gets the value of the isoCtryCode2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIsoCtryCode2() {
        return isoCtryCode2;
    }

    /**
     * Sets the value of the isoCtryCode2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIsoCtryCode2(String value) {
        this.isoCtryCode2 = value;
    }

    /**
     * Gets the value of the emailAddress2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEmailAddress2() {
        return emailAddress2;
    }

    /**
     * Sets the value of the emailAddress2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailAddress2(String value) {
        this.emailAddress2 = value;
    }

    /**
     * Gets the value of the telephoneType1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType1() {
        return telephoneType1;
    }

    /**
     * Sets the value of the telephoneType1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType1(String value) {
        this.telephoneType1 = value;
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
     * Gets the value of the extension1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension1() {
        return extension1;
    }

    /**
     * Sets the value of the extension1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension1(String value) {
        this.extension1 = value;
    }

    /**
     * Gets the value of the titleOfContact property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTitleOfContact() {
        return titleOfContact;
    }

    /**
     * Sets the value of the titleOfContact property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitleOfContact(String value) {
        this.titleOfContact = value;
    }

    /**
     * Gets the value of the contactPerson property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the value of the contactPerson property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContactPerson(String value) {
        this.contactPerson = value;
    }

    /**
     * Gets the value of the telephoneNbr property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNbr() {
        return telephoneNbr;
    }

    /**
     * Sets the value of the telephoneNbr property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNbr(String value) {
        this.telephoneNbr = value;
    }

    /**
     * Gets the value of the contactReason property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getContactReason() {
        return contactReason;
    }

    /**
     * Sets the value of the contactReason property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContactReason(String value) {
        this.contactReason = value;
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
     * Gets the value of the typeOfOwnership property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTypeOfOwnership() {
        return typeOfOwnership;
    }

    /**
     * Sets the value of the typeOfOwnership property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTypeOfOwnership(String value) {
        this.typeOfOwnership = value;
    }

    /**
     * Gets the value of the salesVolume property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSalesVolume() {
        return salesVolume;
    }

    /**
     * Sets the value of the salesVolume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSalesVolume(String value) {
        this.salesVolume = value;
    }

    /**
     * Gets the value of the nbrOfEmps property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNbrOfEmps() {
        return nbrOfEmps;
    }

    /**
     * Sets the value of the nbrOfEmps property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNbrOfEmps(String value) {
        this.nbrOfEmps = value;
    }

    /**
     * Gets the value of the smallBusiness property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSmallBusiness() {
        return smallBusiness;
    }

    /**
     * Sets the value of the smallBusiness property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSmallBusiness(String value) {
        this.smallBusiness = value;
    }

    /**
     * Gets the value of the custType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCustType() {
        return custType;
    }

    /**
     * Sets the value of the custType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustType(String value) {
        this.custType = value;
    }

    /**
     * Gets the value of the busType1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBusType1() {
        return busType1;
    }

    /**
     * Sets the value of the busType1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBusType1(String value) {
        this.busType1 = value;
    }

    /**
     * Gets the value of the busType2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBusType2() {
        return busType2;
    }

    /**
     * Sets the value of the busType2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBusType2(String value) {
        this.busType2 = value;
    }

    /**
     * Gets the value of the branchOpening property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBranchOpening() {
        return branchOpening;
    }

    /**
     * Sets the value of the branchOpening property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBranchOpening(String value) {
        this.branchOpening = value;
    }

    /**
     * Gets the value of the branchResp property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBranchResp() {
        return branchResp;
    }

    /**
     * Sets the value of the branchResp property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBranchResp(String value) {
        this.branchResp = value;
    }

    /**
     * Gets the value of the co property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCo() {
        return co;
    }

    /**
     * Sets the value of the co property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCo(String value) {
        this.co = value;
    }

    /**
     * Gets the value of the netFixedAssetAmt property.
     *
     * @return possible object is
     *         {@link BigDecimal }
     */
    public BigDecimal getNetFixedAssetAmt() {
        return netFixedAssetAmt;
    }

    /**
     * Sets the value of the netFixedAssetAmt property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setNetFixedAssetAmt(BigDecimal value) {
        this.netFixedAssetAmt = value;
    }

    /**
     * Gets the value of the hunter property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getHunter() {
        return hunter;
    }

    /**
     * Sets the value of the hunter property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHunter(String value) {
        this.hunter = value;
    }

    /**
     * Gets the value of the farmer property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getFarmer() {
        return farmer;
    }

    /**
     * Sets the value of the farmer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFarmer(String value) {
        this.farmer = value;
    }

    /**
     * Gets the value of the capitalRegAmt property.
     *
     * @return possible object is
     *         {@link BigDecimal }
     */
    public BigDecimal getCapitalRegAmt() {
        return capitalRegAmt;
    }

    /**
     * Sets the value of the capitalRegAmt property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setCapitalRegAmt(BigDecimal value) {
        this.capitalRegAmt = value;
    }

    /**
     * Gets the value of the capitalPaidAmt property.
     *
     * @return possible object is
     *         {@link BigDecimal }
     */
    public BigDecimal getCapitalPaidAmt() {
        return capitalPaidAmt;
    }

    /**
     * Sets the value of the capitalPaidAmt property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setCapitalPaidAmt(BigDecimal value) {
        this.capitalPaidAmt = value;
    }

    /**
     * Gets the value of the historicalRating property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getHistoricalRating() {
        return historicalRating;
    }

    /**
     * Sets the value of the historicalRating property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHistoricalRating(String value) {
        this.historicalRating = value;
    }

    /**
     * Gets the value of the issuedDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIssuedDate() {
        return issuedDate;
    }

    /**
     * Sets the value of the issuedDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIssuedDate(String value) {
        this.issuedDate = value;
    }

    /**
     * Gets the value of the ratedBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRatedBy() {
        return ratedBy;
    }

    /**
     * Sets the value of the ratedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRatedBy(String value) {
        this.ratedBy = value;
    }

    /**
     * Gets the value of the lastReviewDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * Sets the value of the lastReviewDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastReviewDate(String value) {
        this.lastReviewDate = value;
    }

    /**
     * Gets the value of the nextReviewDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNextReviewDate() {
        return nextReviewDate;
    }

    /**
     * Sets the value of the nextReviewDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNextReviewDate(String value) {
        this.nextReviewDate = value;
    }

    /**
     * Gets the value of the haveData property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getHaveData() {
        return haveData;
    }

    /**
     * Sets the value of the haveData property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHaveData(String value) {
        this.haveData = value;
    }

    /**
     * Gets the value of the localRating1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocalRating1() {
        return localRating1;
    }

    /**
     * Sets the value of the localRating1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalRating1(String value) {
        this.localRating1 = value;
    }

    /**
     * Gets the value of the localIssuedDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocalIssuedDate() {
        return localIssuedDate;
    }

    /**
     * Sets the value of the localIssuedDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalIssuedDate(String value) {
        this.localIssuedDate = value;
    }

    /**
     * Gets the value of the localRatedBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocalRatedBy() {
        return localRatedBy;
    }

    /**
     * Sets the value of the localRatedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalRatedBy(String value) {
        this.localRatedBy = value;
    }

    /**
     * Gets the value of the foreignRating1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getForeignRating1() {
        return foreignRating1;
    }

    /**
     * Sets the value of the foreignRating1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setForeignRating1(String value) {
        this.foreignRating1 = value;
    }

    /**
     * Gets the value of the foreignIssuedDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getForeignIssuedDate() {
        return foreignIssuedDate;
    }

    /**
     * Sets the value of the foreignIssuedDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setForeignIssuedDate(String value) {
        this.foreignIssuedDate = value;
    }

    /**
     * Gets the value of the foreignRatedBy property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getForeignRatedBy() {
        return foreignRatedBy;
    }

    /**
     * Sets the value of the foreignRatedBy property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setForeignRatedBy(String value) {
        this.foreignRatedBy = value;
    }

    /**
     * Gets the value of the telephoneType2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType2() {
        return telephoneType2;
    }

    /**
     * Sets the value of the telephoneType2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType2(String value) {
        this.telephoneType2 = value;
    }

    /**
     * Gets the value of the telephoneNumber2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber2() {
        return telephoneNumber2;
    }

    /**
     * Sets the value of the telephoneNumber2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber2(String value) {
        this.telephoneNumber2 = value;
    }

    /**
     * Gets the value of the extension2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension2() {
        return extension2;
    }

    /**
     * Sets the value of the extension2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension2(String value) {
        this.extension2 = value;
    }

    /**
     * Gets the value of the telephoneType3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType3() {
        return telephoneType3;
    }

    /**
     * Sets the value of the telephoneType3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType3(String value) {
        this.telephoneType3 = value;
    }

    /**
     * Gets the value of the telephoneNumber3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber3() {
        return telephoneNumber3;
    }

    /**
     * Sets the value of the telephoneNumber3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber3(String value) {
        this.telephoneNumber3 = value;
    }

    /**
     * Gets the value of the extension3 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension3() {
        return extension3;
    }

    /**
     * Sets the value of the extension3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension3(String value) {
        this.extension3 = value;
    }

    /**
     * Gets the value of the telephoneType4 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType4() {
        return telephoneType4;
    }

    /**
     * Sets the value of the telephoneType4 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType4(String value) {
        this.telephoneType4 = value;
    }

    /**
     * Gets the value of the telephoneNumber4 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber4() {
        return telephoneNumber4;
    }

    /**
     * Sets the value of the telephoneNumber4 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber4(String value) {
        this.telephoneNumber4 = value;
    }

    /**
     * Gets the value of the extension4 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension4() {
        return extension4;
    }

    /**
     * Sets the value of the extension4 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension4(String value) {
        this.extension4 = value;
    }

    /**
     * Gets the value of the telephoneType5 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType5() {
        return telephoneType5;
    }

    /**
     * Sets the value of the telephoneType5 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType5(String value) {
        this.telephoneType5 = value;
    }

    /**
     * Gets the value of the telephoneNumber5 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber5() {
        return telephoneNumber5;
    }

    /**
     * Sets the value of the telephoneNumber5 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber5(String value) {
        this.telephoneNumber5 = value;
    }

    /**
     * Gets the value of the extension5 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension5() {
        return extension5;
    }

    /**
     * Sets the value of the extension5 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension5(String value) {
        this.extension5 = value;
    }

    /**
     * Gets the value of the telephoneType6 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType6() {
        return telephoneType6;
    }

    /**
     * Sets the value of the telephoneType6 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType6(String value) {
        this.telephoneType6 = value;
    }

    /**
     * Gets the value of the telephoneNumber6 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber6() {
        return telephoneNumber6;
    }

    /**
     * Sets the value of the telephoneNumber6 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber6(String value) {
        this.telephoneNumber6 = value;
    }

    /**
     * Gets the value of the extension6 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension6() {
        return extension6;
    }

    /**
     * Sets the value of the extension6 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension6(String value) {
        this.extension6 = value;
    }

    /**
     * Gets the value of the telephoneType7 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType7() {
        return telephoneType7;
    }

    /**
     * Sets the value of the telephoneType7 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType7(String value) {
        this.telephoneType7 = value;
    }

    /**
     * Gets the value of the telephoneNumber7 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber7() {
        return telephoneNumber7;
    }

    /**
     * Sets the value of the telephoneNumber7 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber7(String value) {
        this.telephoneNumber7 = value;
    }

    /**
     * Gets the value of the extension7 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension7() {
        return extension7;
    }

    /**
     * Sets the value of the extension7 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension7(String value) {
        this.extension7 = value;
    }

    /**
     * Gets the value of the telephoneType8 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType8() {
        return telephoneType8;
    }

    /**
     * Sets the value of the telephoneType8 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType8(String value) {
        this.telephoneType8 = value;
    }

    /**
     * Gets the value of the telephoneNumber8 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber8() {
        return telephoneNumber8;
    }

    /**
     * Sets the value of the telephoneNumber8 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber8(String value) {
        this.telephoneNumber8 = value;
    }

    /**
     * Gets the value of the extension8 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension8() {
        return extension8;
    }

    /**
     * Sets the value of the extension8 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension8(String value) {
        this.extension8 = value;
    }

    /**
     * Gets the value of the emailType1 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEmailType1() {
        return emailType1;
    }

    /**
     * Sets the value of the emailType1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailType1(String value) {
        this.emailType1 = value;
    }

    /**
     * Gets the value of the emailType2 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEmailType2() {
        return emailType2;
    }

    /**
     * Sets the value of the emailType2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmailType2(String value) {
        this.emailType2 = value;
    }

    /**
     * Gets the value of the telephoneNumber9 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber9() {
        return telephoneNumber9;
    }

    /**
     * Sets the value of the telephoneNumber9 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber9(String value) {
        this.telephoneNumber9 = value;
    }

    /**
     * Gets the value of the extension9 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension9() {
        return extension9;
    }

    /**
     * Sets the value of the extension9 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension9(String value) {
        this.extension9 = value;
    }

    /**
     * Gets the value of the telephoneType9 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType9() {
        return telephoneType9;
    }

    /**
     * Sets the value of the telephoneType9 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType9(String value) {
        this.telephoneType9 = value;
    }

    /**
     * Gets the value of the telephoneNumber10 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber10() {
        return telephoneNumber10;
    }

    /**
     * Sets the value of the telephoneNumber10 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber10(String value) {
        this.telephoneNumber10 = value;
    }

    /**
     * Gets the value of the extension10 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension10() {
        return extension10;
    }

    /**
     * Sets the value of the extension10 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension10(String value) {
        this.extension10 = value;
    }

    /**
     * Gets the value of the telephoneType10 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType10() {
        return telephoneType10;
    }

    /**
     * Sets the value of the telephoneType10 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType10(String value) {
        this.telephoneType10 = value;
    }

    /**
     * Gets the value of the telephoneNumber11 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber11() {
        return telephoneNumber11;
    }

    /**
     * Sets the value of the telephoneNumber11 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber11(String value) {
        this.telephoneNumber11 = value;
    }

    /**
     * Gets the value of the extension11 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension11() {
        return extension11;
    }

    /**
     * Sets the value of the extension11 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension11(String value) {
        this.extension11 = value;
    }

    /**
     * Gets the value of the telephoneType11 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType11() {
        return telephoneType11;
    }

    /**
     * Sets the value of the telephoneType11 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType11(String value) {
        this.telephoneType11 = value;
    }

    /**
     * Gets the value of the telephoneNumber12 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneNumber12() {
        return telephoneNumber12;
    }

    /**
     * Sets the value of the telephoneNumber12 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneNumber12(String value) {
        this.telephoneNumber12 = value;
    }

    /**
     * Gets the value of the extension12 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExtension12() {
        return extension12;
    }

    /**
     * Sets the value of the extension12 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExtension12(String value) {
        this.extension12 = value;
    }

    /**
     * Gets the value of the telephoneType12 property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTelephoneType12() {
        return telephoneType12;
    }

    /**
     * Sets the value of the telephoneType12 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelephoneType12(String value) {
        this.telephoneType12 = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("custNbr", custNbr)
                .append("title", title)
                .append("thaiName1", thaiName1)
                .append("thaiName2", thaiName2)
                .append("thaiName3", thaiName3)
                .append("engName1", engName1)
                .append("engName2", engName2)
                .append("engName3", engName3)
                .append("cId", cId)
                .append("citizenCId", citizenCId)
                .append("estDate", estDate)
                .append("isoCountry", isoCountry)
                .append("addressLine1PRI", addressLine1PRI)
                .append("addressLine2PRI", addressLine2PRI)
                .append("addrTumbon", addrTumbon)
                .append("addrAumper", addrAumper)
                .append("city", city)
                .append("postalCd", postalCd)
                .append("ctry", ctry)
                .append("isoCtryCode", isoCtryCode)
                .append("emailAddress1", emailAddress1)
                .append("addressLine1REG", addressLine1REG)
                .append("addressLine2REG", addressLine2REG)
                .append("comRegTumbon", comRegTumbon)
                .append("comRegAumper", comRegAumper)
                .append("city2", city2)
                .append("ctry2", ctry2)
                .append("isoCtryCode2", isoCtryCode2)
                .append("emailAddress2", emailAddress2)
                .append("telephoneType1", telephoneType1)
                .append("telephoneNumber1", telephoneNumber1)
                .append("extension1", extension1)
                .append("titleOfContact", titleOfContact)
                .append("contactPerson", contactPerson)
                .append("telephoneNbr", telephoneNbr)
                .append("contactReason", contactReason)
                .append("description", description)
                .append("typeOfOwnership", typeOfOwnership)
                .append("salesVolume", salesVolume)
                .append("nbrOfEmps", nbrOfEmps)
                .append("smallBusiness", smallBusiness)
                .append("custType", custType)
                .append("busType1", busType1)
                .append("busType2", busType2)
                .append("branchOpening", branchOpening)
                .append("branchResp", branchResp)
                .append("co", co)
                .append("netFixedAssetAmt", netFixedAssetAmt)
                .append("hunter", hunter)
                .append("farmer", farmer)
                .append("capitalRegAmt", capitalRegAmt)
                .append("capitalPaidAmt", capitalPaidAmt)
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
                .append("telephoneType2", telephoneType2)
                .append("telephoneNumber2", telephoneNumber2)
                .append("extension2", extension2)
                .append("telephoneType3", telephoneType3)
                .append("telephoneNumber3", telephoneNumber3)
                .append("extension3", extension3)
                .append("telephoneType4", telephoneType4)
                .append("telephoneNumber4", telephoneNumber4)
                .append("extension4", extension4)
                .append("telephoneType5", telephoneType5)
                .append("telephoneNumber5", telephoneNumber5)
                .append("extension5", extension5)
                .append("telephoneType6", telephoneType6)
                .append("telephoneNumber6", telephoneNumber6)
                .append("extension6", extension6)
                .append("telephoneType7", telephoneType7)
                .append("telephoneNumber7", telephoneNumber7)
                .append("extension7", extension7)
                .append("telephoneType8", telephoneType8)
                .append("telephoneNumber8", telephoneNumber8)
                .append("extension8", extension8)
                .append("emailType1", emailType1)
                .append("emailType2", emailType2)
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
                .toString();
    }
}
