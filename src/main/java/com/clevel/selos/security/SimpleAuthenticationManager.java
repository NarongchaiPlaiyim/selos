package com.clevel.selos.security;

import com.clevel.selos.model.db.Role;
import com.clevel.selos.model.db.User;
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

//    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
//    static {
//        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("###: authenticate.");
        log.debug("user to authenticate: {}",user);
        if (user.getPassword().equalsIgnoreCase(authentication.getCredentials().toString().trim())) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            for (Role role: user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new UsernamePasswordAuthenticationToken(authentication.getName(),
                    authentication.getCredentials(), grantedAuthorities);
        }
//        if (authentication.getName().equals(authentication.getCredentials())) {
//            return new UsernamePasswordAuthenticationToken(authentication.getName(),
//                    authentication.getCredentials(), AUTHORITIES);
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

