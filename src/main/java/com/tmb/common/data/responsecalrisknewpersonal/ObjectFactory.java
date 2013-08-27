
package com.tmb.common.data.responsecalrisknewpersonal;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tmb.common.data.responsecalrisknewpersonal package. 
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

    private final static QName _ResCalRiskNewPersonal_QNAME = new QName("http://data.common.tmb.com/responseCalRiskNewPersonal", "resCalRiskNewPersonal");
    private final static QName _WarningListPersonSectionCardId_QNAME = new QName("", "cardId");
    private final static QName _WarningListPersonSectionRiskLevel_QNAME = new QName("", "riskLevel");
    private final static QName _WarningListPersonSectionRemark_QNAME = new QName("", "remark");
    private final static QName _WarningListPersonSectionFullName_QNAME = new QName("", "fullName");
    private final static QName _BodyResCode1_QNAME = new QName("", "resCode1");
    private final static QName _BodyResCode2_QNAME = new QName("", "resCode2");
    private final static QName _BodyResCode3_QNAME = new QName("", "resCode3");
    private final static QName _BodyResCode4_QNAME = new QName("", "resCode4");
    private final static QName _BodyMaxRisk_QNAME = new QName("", "maxRisk");
    private final static QName _BodyResDesc1_QNAME = new QName("", "resDesc1");
    private final static QName _BodyResDesc2_QNAME = new QName("", "resDesc2");
    private final static QName _BodyResDesc3_QNAME = new QName("", "resDesc3");
    private final static QName _BodyResDesc4_QNAME = new QName("", "resDesc4");
    private final static QName _BodyMaxRiskRM_QNAME = new QName("", "maxRiskRM");
    private final static QName _BodyStatusSWF_QNAME = new QName("", "statusSWF");
    private final static QName _BodySourceOfRisk_QNAME = new QName("", "sourceOfRisk");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.common.data.responsecalrisknewpersonal
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link WarningListPersonSection }
     * 
     */
    public WarningListPersonSection createWarningListPersonSection() {
        return new WarningListPersonSection();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link ResCalRiskNewPersonal }
     * 
     */
    public ResCalRiskNewPersonal createResCalRiskNewPersonal() {
        return new ResCalRiskNewPersonal();
    }

    /**
     * Create an instance of {@link MessageSection }
     * 
     */
    public MessageSection createMessageSection() {
        return new MessageSection();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ResCalRiskNewPersonal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://data.common.tmb.com/responseCalRiskNewPersonal", name = "resCalRiskNewPersonal")
    public JAXBElement<ResCalRiskNewPersonal> createResCalRiskNewPersonal(ResCalRiskNewPersonal value) {
        return new JAXBElement<ResCalRiskNewPersonal>(_ResCalRiskNewPersonal_QNAME, ResCalRiskNewPersonal.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cardId", scope = WarningListPersonSection.class)
    public JAXBElement<String> createWarningListPersonSectionCardId(String value) {
        return new JAXBElement<String>(_WarningListPersonSectionCardId_QNAME, String.class, WarningListPersonSection.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "riskLevel", scope = WarningListPersonSection.class)
    public JAXBElement<String> createWarningListPersonSectionRiskLevel(String value) {
        return new JAXBElement<String>(_WarningListPersonSectionRiskLevel_QNAME, String.class, WarningListPersonSection.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "remark", scope = WarningListPersonSection.class)
    public JAXBElement<String> createWarningListPersonSectionRemark(String value) {
        return new JAXBElement<String>(_WarningListPersonSectionRemark_QNAME, String.class, WarningListPersonSection.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fullName", scope = WarningListPersonSection.class)
    public JAXBElement<String> createWarningListPersonSectionFullName(String value) {
        return new JAXBElement<String>(_WarningListPersonSectionFullName_QNAME, String.class, WarningListPersonSection.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resCode1", scope = Body.class)
    public JAXBElement<String> createBodyResCode1(String value) {
        return new JAXBElement<String>(_BodyResCode1_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resCode2", scope = Body.class)
    public JAXBElement<String> createBodyResCode2(String value) {
        return new JAXBElement<String>(_BodyResCode2_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resCode3", scope = Body.class)
    public JAXBElement<String> createBodyResCode3(String value) {
        return new JAXBElement<String>(_BodyResCode3_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resCode4", scope = Body.class)
    public JAXBElement<String> createBodyResCode4(String value) {
        return new JAXBElement<String>(_BodyResCode4_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "maxRisk", scope = Body.class)
    public JAXBElement<String> createBodyMaxRisk(String value) {
        return new JAXBElement<String>(_BodyMaxRisk_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resDesc1", scope = Body.class)
    public JAXBElement<String> createBodyResDesc1(String value) {
        return new JAXBElement<String>(_BodyResDesc1_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resDesc2", scope = Body.class)
    public JAXBElement<String> createBodyResDesc2(String value) {
        return new JAXBElement<String>(_BodyResDesc2_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resDesc3", scope = Body.class)
    public JAXBElement<String> createBodyResDesc3(String value) {
        return new JAXBElement<String>(_BodyResDesc3_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resDesc4", scope = Body.class)
    public JAXBElement<String> createBodyResDesc4(String value) {
        return new JAXBElement<String>(_BodyResDesc4_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "maxRiskRM", scope = Body.class)
    public JAXBElement<String> createBodyMaxRiskRM(String value) {
        return new JAXBElement<String>(_BodyMaxRiskRM_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "statusSWF", scope = Body.class)
    public JAXBElement<String> createBodyStatusSWF(String value) {
        return new JAXBElement<String>(_BodyStatusSWF_QNAME, String.class, Body.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sourceOfRisk", scope = Body.class)
    public JAXBElement<String> createBodySourceOfRisk(String value) {
        return new JAXBElement<String>(_BodySourceOfRisk_QNAME, String.class, Body.class, value);
    }

}
