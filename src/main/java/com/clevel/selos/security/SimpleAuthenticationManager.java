package com.clevel.selos.security;

import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.LDAPInterface;
import com.clevel.selos.model.RoleTypeName;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class SimpleAuthenticationManager implements AuthenticationManager {
    @Inject
    Logger log;
    @Inject
    LDAPInterface ldapInterface;
    @Inject
    BPMInterface bpmInterface;

    @Inject
    @Config(name = "interface.ldap.enable")
    String ldapEnable;
    @Inject
    EncryptionService encryptionService;
    @Inject
    @Config(name = "system.encryption.enable")
    String encryptionEnable;

    @Inject
    public SimpleAuthenticationManager() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        log.debug("authenticate: {}", userDetail);

        WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) authentication.getDetails();

        // system role
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.SYSTEM.name())) {
            log.debug("system role.");
            return getAuthority(userDetail, authentication, authenticationDetails);
        }

        // non business role
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.NON_BUSINESS.name())) {
            log.debug("non business role.");
            return getAuthority(userDetail, authentication, authenticationDetails);
        }

        // business role continue BPM authentication
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.BUSINESS.name())) {
            log.debug("business role. (continue BPM authentication)");
            if (Util.isTrue(ldapEnable)) {
                String password;
                if (Util.isTrue(encryptionEnable)) {
                    password = encryptionService.decrypt(Base64.decodeBase64(userDetail.getPassword()));
                } else {
                    password = userDetail.getPassword();
                }
                bpmInterface.authenticate(userDetail.getUserName(), password);
            }
            log.debug("Authentication with BPM success.");
            return getAuthority(userDetail, authentication, authenticationDetails);
        }
        throw new BadCredentialsException("Bad Credentials");
    }

    private UsernamePasswordAuthenticationToken getAuthority(UserDetail userDetail, Authentication authentication, WebAuthenticationDetails authenticationDetails) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userDetail.getRole()));
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetail,
                authentication.getCredentials(), grantedAuthorities);
        result.setDetails(authenticationDetails);
        return result;
    }
}

