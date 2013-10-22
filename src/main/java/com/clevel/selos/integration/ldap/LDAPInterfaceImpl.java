package com.clevel.selos.integration.ldap;

import com.clevel.selos.exception.LDAPInterfaceException;
import com.clevel.selos.integration.LDAPInterface;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

@Default
public class LDAPInterfaceImpl implements LDAPInterface {
    @Inject
    Logger log;

    @Inject
    @Config(name = "interface.ldap.url")
    String ldapURL;
    @Inject
    @Config(name = "interface.ldap.baseDN")
    String baseDN;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    public LDAPInterfaceImpl() {
    }

    @Override
    public void authenticate(String userName, String password) {
        log.debug("authenticate. (userName: {}, password: [HIDDEN])", userName);
        DirContext ctx;
        if (userName == null || "".equalsIgnoreCase(userName)){
            log.debug("username is null or empty!");
            throw new LDAPInterfaceException(null, ExceptionMapping.LDAP_EMPTY_USERNAME, msg.get(ExceptionMapping.LDAP_EMPTY_USERNAME));
        }
        if (password == null || "".equalsIgnoreCase(password)) {
            log.debug("password is null or empty!");
            throw new LDAPInterfaceException(null, ExceptionMapping.LDAP_EMPTY_PASSWORD, msg.get(ExceptionMapping.LDAP_EMPTY_PASSWORD));
        }
        try {
            ctx = getDirContext(userName, password);
            SearchResult result = findAccountByAccountName(ctx, baseDN, userName);
            Attributes attributes = result.getAttributes();
            log.debug("user found. (commonName: {}, displayName: {}, memberOf: {})", attributes.get("cn"), attributes.get("displayName"),
                    attributes.get("memberOf"));
        } catch (NamingException e) {
            log.error("", e);
            throw new LDAPInterfaceException(e, ExceptionMapping.LDAP_AUTHENTICATION_FAILED, msg.get(ExceptionMapping.LDAP_AUTHENTICATION_FAILED,getLDAPErrorCode(e.getMessage())));
        }
        log.debug("authentication success. (userName: {})",userName);
    }

    private DirContext getDirContext(String userName, String password) throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapURL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialDirContext(env);
    }

    private SearchResult findAccountByAccountName(DirContext ctx, String ldapSearchBase, String accountName) throws NamingException {
        log.debug("findAccountByAccountName. (accountName: {})", accountName);
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + accountName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);
        SearchResult searchResult = null;
        if (results.hasMoreElements()) {
            searchResult = results.nextElement();

            if (results.hasMoreElements()) {
                log.error("Matched multiple users for the accountName: {}", accountName);
                return null;
            }
        }
        return searchResult;
    }

    private String getLDAPErrorCode(String exceptionMessage) {
        try{
        String[] tmp = exceptionMessage.substring(exceptionMessage.indexOf(", data")).split(",");

        tmp = tmp[1].split(" ");
        return tmp[2].trim();
        }catch (StringIndexOutOfBoundsException e){
            return "";
        }
    }

}
