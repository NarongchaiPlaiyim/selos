package com.clevel.selos.security;

import com.clevel.selos.model.db.master.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SimpleAuthenticationManager implements AuthenticationManager {
    Logger log = LoggerFactory.getLogger(SimpleAuthenticationManager.class);
    User user;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("authenticate: {}",user);

        // special user for system test, bypass all authentication.
        if (user.getRole().getRoleType().getName().equalsIgnoreCase(RoleTypeName.SYSTEM.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), grantedAuthorities);
        }

        // todo: authentication with ECM
        if (user.getRole().getRoleType().getName().equalsIgnoreCase(RoleTypeName.BUSINESS.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), grantedAuthorities);
        }

        // todo: authentication with LDAP
        if (user.getRole().getRoleType().getName().equalsIgnoreCase(RoleTypeName.NON_BUSINESS.name())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), grantedAuthorities);
        }


//        if (user.getPassword().equalsIgnoreCase(authentication.getCredentials().toString().trim())) {
//            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
//            return new UsernamePasswordAuthenticationToken(authentication.getName(),
//                    authentication.getCredentials(), grantedAuthorities);
//        }

        throw new BadCredentialsException("Bad Credentials");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

