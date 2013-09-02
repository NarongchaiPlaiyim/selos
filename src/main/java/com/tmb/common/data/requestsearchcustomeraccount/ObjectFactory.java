
package com.tmb.common.data.requestsearchcustomeraccount;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tmb.common.data.requestsearchcustomeraccount package. 
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

    private final static QName _ReqSearchCustomerAccount_QNAME = new QName("http://data.common.tmb.com/requestSearchCustomerAccount", "reqSearchCustomerAccount");
    private final static QName _HeaderSessionId_QNAME = new QName("", "sessionId");
    private final static QName _HeaderServerURL_QNAME = new QName("", "serverURL");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.common.data.requestsearchcustomeraccount
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link ReqSearchCustomerAccount }
     * 
     */
    public ReqSearchCustomerAccount createReqSearchCustomerAccount() {
        return new ReqSearchCustomerAccount();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReqSearchCustomerAccount }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://data.common.tmb.com/requestSearchCustomerAccount", name = "reqSearchCustomerAccount")
    public JAXBElement<ReqSearchCustomerAccount> createReqSearchCustomerAccount(ReqSearchCustomerAccount value) {
        return new JAXBElement<ReqSearchCustomerAccount>(_ReqSearchCustomerAccount_QNAME, ReqSearchCustomerAccount.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sessionId", scope = Header.class)
    public JAXBElement<String> createHeaderSessionId(String value) {
        return new JAXBElement<String>(_HeaderSessionId_QNAME, String.class, Header.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "serverURL", scope = Header.class)
    public JAXBElement<String> createHeaderServerURL(String value) {
        return new JAXBElement<String>(_HeaderServerURL_QNAME, String.class, Header.class, value);
    }

}
