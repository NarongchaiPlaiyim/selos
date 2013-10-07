package com.clevel.selos.security;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.exception.ApplicationRuntimeException;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.LDAPInterface;
import com.clevel.selos.integration.RM;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Language;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SecurityAuditor;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.audit.UserAuditor;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    @Inject
    SecurityAuditor securityAuditor;

    private String userName;
    private String password;

    @Inject
    LDAPInterface ldapInterface;
    @Inject
    BPMInterface bpmInterface;

    @Inject
    @Config(name = "interface.ldap.enable")
    String ldapEnable;

    @Inject
    private SimpleAuthenticationManager authenticationManager;
    @ManagedProperty(value="#{sessionRegistry}")
    private SessionRegistry sessionRegistry;

    public String login() {
        log.debug("SessionRegistry principle size: {}",sessionRegistry.getAllPrincipals().size());

        // make authentication with AD first
        if (Util.isTrue(ldapEnable)) {
            log.debug("LDAP authentication enabled.");
            try {
                ldapInterface.authenticate(userName,password);
            } catch (ApplicationRuntimeException e) {
                log.debug("LDAP authentication failed! (user: {})", userName.trim());
                securityAuditor.addFailed(userName.trim(), "Login", "", e.getMessage());
                return "failed";
            }
        }

        // find user profile in database
        User user = userDAO.findById(userName.trim());
        UserDetail userDetail = null;
        try {
            userDetail = new UserDetail(user.getId(),password.trim(), user.getRole().getSystemName(), user.getRole().getRoleType().getRoleTypeName().name());
        } catch (EntityNotFoundException e) {
            log.debug("user not found in system! (user: {})", userName.trim());
            securityAuditor.addFailed(userName.trim(), "Login", "", "User not found in system!");
            return "failed";
        }

        try {
            HttpServletRequest httpServletRequest = FacesUtil.getRequest();
            HttpServletResponse httpServletResponse = FacesUtil.getResponse();
            UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(userDetail, this.getPassword());
            request.setDetails(new WebAuthenticationDetails(httpServletRequest));

            Authentication result = authenticationManager.authenticate(request);
            log.debug("authentication result: {}", result);
            SecurityContextHolder.getContext().setAuthentication(result);
            log.debug("login successful. ({})", userDetail);

            ConcurrentSessionControlStrategy concurrentSessionControlStrategy = new ConcurrentSessionControlStrategy(sessionRegistry);
            concurrentSessionControlStrategy.onAuthentication(request, httpServletRequest, httpServletResponse);
            HttpSession httpSession = FacesUtil.getSession(false);
            httpSession.setAttribute("language", Language.EN);

            securityAuditor.addSucceed(userDetail.getUserName(), "Login", "",new Date());

            return user.getRole().getRoleType().getRoleTypeName().name();
        } catch (ApplicationRuntimeException e) {
            securityAuditor.addException(userName.trim(), "Login", "", e.getMessage());
            log.debug("login failed!. ({})", e.getMessage());
        } catch (AuthenticationException e) {
            securityAuditor.addException(userName.trim(), "Login", "", e.getMessage());
            log.debug("login failed!. ({})", e.getMessage());
        }
//        securityAuditor.addFailed(userName.trim(), "Login", "", "Authentication failed!");
        return "failed";
    }

    public UserDetail getUserDetail() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String logout() {
        log.debug("logging out.");
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.clearContext();
        securityAuditor.addSucceed(userDetail.getUserName(), "Logout", "",new Date());
        return "loggedOut";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
