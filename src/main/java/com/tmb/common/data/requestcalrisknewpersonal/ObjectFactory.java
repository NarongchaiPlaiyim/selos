
package com.tmb.common.data.requestcalrisknewpersonal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tmb.common.data.requestcalrisknewpersonal package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ReqCalRiskNewPersonal_QNAME = new QName("http://data.common.tmb.com/requestCalRiskNewPersonal", "reqCalRiskNewPersonal");
    private final static QName _BodyDateOfBirth_QNAME = new QName("", "dateOfBirth");
    private final static QName _BodyTellerId_QNAME = new QName("", "tellerId");
    private final static QName _BodyReserve10_QNAME = new QName("", "reserve10");
    private final static QName _BodyCity_QNAME = new QName("", "city");
    private final static QName _BodyLastNameEng_QNAME = new QName("", "lastNameEng");
    private final static QName _BodyFirstNameEng_QNAME = new QName("", "firstNameEng");
    private final static QName _BodyIsoCountryOfBirth_QNAME = new QName("", "isoCountryOfBirth");
    private final static QName _BodyReserve7_QNAME = new QName("", "reserve7");
    private final static QName _BodyReserve6_QNAME = new QName("", "reserve6");
    private final static QName _BodyReserve5_QNAME = new QName("", "reserve5");
    private final static QName _BodyReserve4_QNAME = new QName("", "reserve4");
    private final static QName _BodyReserve9_QNAME = new QName("", "reserve9");
    private final static QName _BodyReserve8_QNAME = new QName("", "reserve8");
    private final static QName _BodyCountryCode_QNAME = new QName("", "countryCode");
    private final static QName _BodyIsoCountryIncomeSource_QNAME = new QName("", "isoCountryIncomeSource");
    private final static QName _BodyOccupationCode_QNAME = new QName("", "occupationCode");
    private final static QName _BodyNationalCode_QNAME = new QName("", "nationalCode");
    private final static QName _BodyReserve3_QNAME = new QName("", "reserve3");
    private final static QName _BodyBranchCode_QNAME = new QName("", "branchCode");
    private final static QName _BodyReserve2_QNAME = new QName("", "reserve2");
    private final static QName _BodyReserve1_QNAME = new QName("", "reserve1");
    private final static QName _BodyAddress_QNAME = new QName("", "address");
    private final static QName _BodyBusinessCode_QNAME = new QName("", "businessCode");
    private final static QName _BodyNationalCode2_QNAME = new QName("", "nationalCode2");
    private final static QName _BodyBranchName_QNAME = new QName("", "branchName");
    private final static QName _BodyReferenceNo_QNAME = new QName("", "referenceNo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.common.data.requestcalrisknewpersonal
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReqCalRiskNewPersonal }
     * 
     */
    public ReqCalRiskNewPersonal createReqCalRiskNewPersonal() {
        return new ReqCalRiskNewPersonal();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ReqCalRiskNewPersonal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://data.common.tmb.com/requestCalRiskNewPersonal", name = "reqCalRiskNewPersonal")
    public JAXBElement<ReqCalRiskNewPersonal> createReqCalRiskNewPersonal(ReqCalRiskNewPersonal value) {
        return new JAXBElement<ReqCalRiskNewPersonal>(_ReqCalRiskNewPersonal_QNAME, ReqCalRiskNewPersonal.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dateOfBirth", scope = Body.class)
    public JAXBElement<String> createBodyDateOfBirth(String value) {
        return new JAXBElement<String>(_BodyDateOfBirth_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "tellerId", scope = Body.class)
    public JAXBElement<String> createBodyTellerId(String value) {
        return new JAXBElement<String>(_BodyTellerId_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve10", scope = Body.class)
    public JAXBElement<String> createBodyReserve10(String value) {
        return new JAXBElement<String>(_BodyReserve10_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "city", scope = Body.class)
    public JAXBElement<String> createBodyCity(String value) {
        return new JAXBElement<String>(_BodyCity_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "lastNameEng", scope = Body.class)
    public JAXBElement<String> createBodyLastNameEng(String value) {
        return new JAXBElement<String>(_BodyLastNameEng_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "firstNameEng", scope = Body.class)
    public JAXBElement<String> createBodyFirstNameEng(String value) {
        return new JAXBElement<String>(_BodyFirstNameEng_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "isoCountryOfBirth", scope = Body.class)
    public JAXBElement<String> createBodyIsoCountryOfBirth(String value) {
        return new JAXBElement<String>(_BodyIsoCountryOfBirth_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve7", scope = Body.class)
    public JAXBElement<String> createBodyReserve7(String value) {
        return new JAXBElement<String>(_BodyReserve7_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve6", scope = Body.class)
    public JAXBElement<String> createBodyReserve6(String value) {
        return new JAXBElement<String>(_BodyReserve6_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve5", scope = Body.class)
    public JAXBElement<String> createBodyReserve5(String value) {
        return new JAXBElement<String>(_BodyReserve5_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve4", scope = Body.class)
    public JAXBElement<String> createBodyReserve4(String value) {
        return new JAXBElement<String>(_BodyReserve4_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve9", scope = Body.class)
    public JAXBElement<String> createBodyReserve9(String value) {
        return new JAXBElement<String>(_BodyReserve9_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve8", scope = Body.class)
    public JAXBElement<String> createBodyReserve8(String value) {
        return new JAXBElement<String>(_BodyReserve8_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "countryCode", scope = Body.class)
    public JAXBElement<String> createBodyCountryCode(String value) {
        return new JAXBElement<String>(_BodyCountryCode_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "isoCountryIncomeSource", scope = Body.class)
    public JAXBElement<String> createBodyIsoCountryIncomeSource(String value) {
        return new JAXBElement<String>(_BodyIsoCountryIncomeSource_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "occupationCode", scope = Body.class)
    public JAXBElement<String> createBodyOccupationCode(String value) {
        return new JAXBElement<String>(_BodyOccupationCode_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nationalCode", scope = Body.class)
    public JAXBElement<String> createBodyNationalCode(String value) {
        return new JAXBElement<String>(_BodyNationalCode_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve3", scope = Body.class)
    public JAXBElement<String> createBodyReserve3(String value) {
        return new JAXBElement<String>(_BodyReserve3_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "branchCode", scope = Body.class)
    public JAXBElement<String> createBodyBranchCode(String value) {
        return new JAXBElement<String>(_BodyBranchCode_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve2", scope = Body.class)
    public JAXBElement<String> createBodyReserve2(String value) {
        return new JAXBElement<String>(_BodyReserve2_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reserve1", scope = Body.class)
    public JAXBElement<String> createBodyReserve1(String value) {
        return new JAXBElement<String>(_BodyReserve1_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "address", scope = Body.class)
    public JAXBElement<String> createBodyAddress(String value) {
        return new JAXBElement<String>(_BodyAddress_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "businessCode", scope = Body.class)
    public JAXBElement<String> createBodyBusinessCode(String value) {
        return new JAXBElement<String>(_BodyBusinessCode_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "nationalCode2", scope = Body.class)
    public JAXBElement<String> createBodyNationalCode2(String value) {
        return new JAXBElement<String>(_BodyNationalCode2_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "branchName", scope = Body.class)
    public JAXBElement<String> createBodyBranchName(String value) {
        return new JAXBElement<String>(_BodyBranchName_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "referenceNo", scope = Body.class)
    public JAXBElement<String> createBodyReferenceNo(String value) {
        return new JAXBElement<String>(_BodyReferenceNo_QNAME, String.class, Body.class, value);
    }

}
