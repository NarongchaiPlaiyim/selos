package com.clevel.selos.security;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.exception.ApplicationRuntimeException;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.LDAPInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Language;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SecurityAuditor;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    @SELOS
    Logger log;
    @Inject
    UserDAO userDAO;

    @Inject
    SecurityAuditor securityAuditor;

    private String userName;
    private String password;
    private String loginExceptionMessage;

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
    @ExceptionMessage
    Message msg;

    @Inject
    private SimpleAuthenticationManager authenticationManager;
    @ManagedProperty(value = "#{sessionRegistry}")
    private SessionRegistry sessionRegistry;

    @PostConstruct
    public void onCreation(){
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(userDetail != null){
                FacesUtil.redirect("/site/inbox.jsf");
            }
        }
    }

    public String login() {
        log.debug("SessionRegistry principle size: {}", sessionRegistry.getAllPrincipals().size());

        loginExceptionMessage = "";

        // make authentication with AD first
        if (Util.isTrue(ldapEnable)) {
            log.debug("LDAP authentication enabled.");
            try {
                ldapInterface.authenticate(userName, password);
            } catch (ApplicationRuntimeException e) {
                try {
                    log.debug("LDAP authentication failed! (user: {})", userName.trim());
                    securityAuditor.addFailed(userName.trim(), "Login", "", e.getMessage());
                    loginExceptionMessage = e.getMessage();
                } catch (Exception ex) {
                    loginExceptionMessage = ex.getCause().getMessage();
                }
                return "failed";
            }
        }

        // find user profile in database
        User user = userDAO.findById(userName.trim());
        UserDetail userDetail = null;
        if (Util.isTrue(encryptionEnable)) {
            password = Base64.encodeBase64String(encryptionService.encrypt(password.trim()));
        } else {
            password = password.trim();
        }
        try {
            userDetail = new UserDetail(user.getId(), password, user.getRole().getSystemName(), user.getRole().getRoleType().getRoleTypeName().name());
        } catch (EntityNotFoundException e) {
            String message = msg.get(ExceptionMapping.USER_NOT_FOUND, userName.trim());
            log.debug("{}", message);
            securityAuditor.addFailed(userName.trim(), "Login", "", message);
            loginExceptionMessage = message;
            return "failed";
        }


        if (user.getActive() != 1) {
            String message = msg.get(ExceptionMapping.USER_NOT_ACTIVE, userName.trim());
            log.debug("{}", message);
            securityAuditor.addFailed(userName.trim(), "Login", "", message);
            loginExceptionMessage = message;
            return "failed";
        }

        // handle user status here
        UserStatus userStatus = user.getUserStatus();
        if (UserStatus.DISABLED == userStatus) {
            String message = msg.get(ExceptionMapping.USER_STATUS_DISABLED, userName.trim());
            log.debug("{}", message);
            securityAuditor.addFailed(userName.trim(), "Login", "", message);
            loginExceptionMessage = message;
            return "failed";
        } else if (UserStatus.MARK_AS_DELETED == userStatus) {
            String message = msg.get(ExceptionMapping.USER_STATUS_DELETED, userName.trim());
            log.debug("{}", message);
            securityAuditor.addFailed(userName.trim(), "Login", "", message);
            loginExceptionMessage = message;
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
            httpSession.setAttribute("user", null);
            httpSession.setAttribute("language", Language.EN);

            securityAuditor.addSucceed(userDetail.getUserName(), "Login", "", new Date());

            return user.getRole().getRoleType().getRoleTypeName().name();
        } catch (ApplicationRuntimeException e) {
            securityAuditor.addException(userName.trim(), "Login", "", e.getMessage());
            log.debug("login failed!. ({})", e.getMessage());
            loginExceptionMessage = e.getMessage();
        } catch (AuthenticationException e) {
            securityAuditor.addException(userName.trim(), "Login", "", e.getMessage());
            log.debug("login failed!. ({})", e.getMessage());
            loginExceptionMessage = e.getMessage();
        }
//        securityAuditor.addFailed(userName.trim(), "Login", "", "Authentication failed!");
        return "failed";
    }

    public UserDetail getUserDetail() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String logout() {
        log.debug("logging out.");
        HttpSession httpSession = FacesUtil.getSession(false);
        httpSession.setAttribute("user", null);
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.clearContext();
        securityAuditor.addSucceed(userDetail.getUserName(), "Logout", "", new Date());
        loginExceptionMessage = "";
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

    public String getLoginExceptionMessage() {
        return loginExceptionMessage;
    }

    public void setLoginExceptionMessage(String loginExceptionMessage) {
        this.loginExceptionMessage = loginExceptionMessage;
    }
}
