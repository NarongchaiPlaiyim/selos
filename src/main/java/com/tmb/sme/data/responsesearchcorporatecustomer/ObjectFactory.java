
package com.tmb.sme.data.responsesearchcorporatecustomer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tmb.sme.data.responsesearchcorporatecustomer package. 
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

    private final static QName _ResSearchCorporateCustomer_QNAME = new QName("http://data.sme.tmb.com/responseSearchCorporateCustomer", "resSearchCorporateCustomer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.sme.data.responsesearchcorporatecustomer
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CorporateDetail }
     * 
     */
    public CorporateDetail createCorporateDetail() {
        return new CorporateDetail();
    }

    /**
     * Create an instance of {@link CorporateList }
     * 
     */
    public CorporateList createCorporateList() {
        return new CorporateList();
    }

    /**
     * Create an instance of {@link CorporateCustomerDetailSection }
     * 
     */
    public CorporateCustomerDetailSection createCorporateCustomerDetailSection() {
        return new CorporateCustomerDetailSection();
    }

    /**
     * Create an instance of {@link ResSearchCorporateCustomer }
     * 
     */
    public ResSearchCorporateCustomer createResSearchCorporateCustomer() {
        return new ResSearchCorporateCustomer();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link CorporateCustomerListSection }
     * 
     */
    public CorporateCustomerListSection createCorporateCustomerListSection() {
        return new CorporateCustomerListSection();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResSearchCorporateCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://data.sme.tmb.com/responseSearchCorporateCustomer", name = "resSearchCorporateCustomer")
    public JAXBElement<ResSearchCorporateCustomer> createResSearchCorporateCustomer(ResSearchCorporateCustomer value) {
        return new JAXBElement<ResSearchCorporateCustomer>(_ResSearchCorporateCustomer_QNAME, ResSearchCorporateCustomer.class, null, value);
    }

}
