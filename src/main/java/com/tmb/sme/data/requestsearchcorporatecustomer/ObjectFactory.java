
package com.tmb.sme.data.requestsearchcorporatecustomer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.tmb.sme.data.requestsearchcorporatecustomer package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ReqSearchCorporateCustomer_QNAME = new QName("http://data.sme.tmb.com/requestSearchCorporateCustomer", "reqSearchCorporateCustomer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.sme.data.requestsearchcorporatecustomer
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Header }
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link Body }
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link ReqSearchCorporateCustomer }
     */
    public ReqSearchCorporateCustomer createReqSearchCorporateCustomer() {
        return new ReqSearchCorporateCustomer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReqSearchCorporateCustomer }{@code >}}
     */
    @XmlElementDecl(namespace = "http://data.sme.tmb.com/requestSearchCorporateCustomer", name = "reqSearchCorporateCustomer")
    public JAXBElement<ReqSearchCorporateCustomer> createReqSearchCorporateCustomer(ReqSearchCorporateCustomer value) {
        return new JAXBElement<ReqSearchCorporateCustomer>(_ReqSearchCorporateCustomer_QNAME, ReqSearchCorporateCustomer.class, null, value);
    }

}
