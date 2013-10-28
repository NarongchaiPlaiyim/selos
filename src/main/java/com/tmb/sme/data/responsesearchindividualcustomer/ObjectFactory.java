
package com.tmb.sme.data.responsesearchindividualcustomer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.tmb.sme.data.responsesearchindividualcustomer package.
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

    private final static QName _ResSearchIndividualCustomer_QNAME = new QName("http://data.sme.tmb.com/responseSearchIndividualCustomer", "resSearchIndividualCustomer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.sme.data.responsesearchindividualcustomer
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersonalDetailSection }
     */
    public PersonalDetailSection createPersonalDetailSection() {
        return new PersonalDetailSection();
    }

    /**
     * Create an instance of {@link PersonalListSection }
     */
    public PersonalListSection createPersonalListSection() {
        return new PersonalListSection();
    }

    /**
     * Create an instance of {@link ResSearchIndividualCustomer }
     */
    public ResSearchIndividualCustomer createResSearchIndividualCustomer() {
        return new ResSearchIndividualCustomer();
    }

    /**
     * Create an instance of {@link PersonalList }
     */
    public PersonalList createPersonalList() {
        return new PersonalList();
    }

    /**
     * Create an instance of {@link Header }
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link PersonalDetail }
     */
    public PersonalDetail createPersonalDetail() {
        return new PersonalDetail();
    }

    /**
     * Create an instance of {@link Body }
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResSearchIndividualCustomer }{@code >}}
     */
    @XmlElementDecl(namespace = "http://data.sme.tmb.com/responseSearchIndividualCustomer", name = "resSearchIndividualCustomer")
    public JAXBElement<ResSearchIndividualCustomer> createResSearchIndividualCustomer(ResSearchIndividualCustomer value) {
        return new JAXBElement<ResSearchIndividualCustomer>(_ResSearchIndividualCustomer_QNAME, ResSearchIndividualCustomer.class, null, value);
    }

}
