package com.clevel.selos.security;

import com.clevel.selos.model.RoleTypeName;
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
    public SimpleAuthenticationManager() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        log.debug("authenticate: {}",userDetail);

        WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails)authentication.getDetails();

        // special user for system test, bypass all authentication.
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.SYSTEM.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userDetail.getRole()));

            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetail,
                    authentication.getCredentials(), grantedAuthorities);
            result.setDetails(authenticationDetails);
            return result;
        }

        // todo: authentication with ECM
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.BUSINESS.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userDetail.getRole()));
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetail,
                    authentication.getCredentials(), grantedAuthorities);
            result.setDetails(authenticationDetails);
            return result;
        }

        // todo: authentication with LDAP
        if (userDetail.getRoleType().equalsIgnoreCase(RoleTypeName.NON_BUSINESS.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(userDetail.getRole()));
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetail,
                    authentication.getCredentials(), grantedAuthorities);
            result.setDetails(authenticationDetails);
            return result;
        }
        throw new BadCredentialsException("Bad Credentials");
    }
}

