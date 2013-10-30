
package com.tmb.common.data.responsesearchcustomeraccount;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.tmb.common.data.responsesearchcustomeraccount package.
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

    private final static QName _ResSearchCustomerAccount_QNAME = new QName("http://data.common.tmb.com/responseSearchCustomerAccount", "resSearchCustomerAccount");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmb.common.data.responsesearchcustomeraccount
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
     * Create an instance of {@link AccountList }
     */
    public AccountList createAccountList() {
        return new AccountList();
    }

    /**
     * Create an instance of {@link ResSearchCustomerAccount }
     */
    public ResSearchCustomerAccount createResSearchCustomerAccount() {
        return new ResSearchCustomerAccount();
    }

    /**
     * Create an instance of {@link Body }
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResSearchCustomerAccount }{@code >}}
     */
    @XmlElementDecl(namespace = "http://data.common.tmb.com/responseSearchCustomerAccount", name = "resSearchCustomerAccount")
    public JAXBElement<ResSearchCustomerAccount> createResSearchCustomerAccount(ResSearchCustomerAccount value) {
        return new JAXBElement<ResSearchCustomerAccount>(_ResSearchCustomerAccount_QNAME, ResSearchCustomerAccount.class, null, value);
    }

}
