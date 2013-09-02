package com.clevel.selos.security;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.Language;
import com.clevel.selos.model.db.audit.UserActivity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.system.audit.UserAudit;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;
    @Inject
    UserAudit userAudit;

    private String userName;
    private String password;

    @Inject
    private SimpleAuthenticationManager authenticationManager;
    @ManagedProperty(value="#{sessionRegistry}")
    private SessionRegistry sessionRegistry;

    public String login() {
        log.debug("SessionRegistry principle size: {}",sessionRegistry.getAllPrincipals().size());
        User user = userDAO.findByUserName(userName.trim());
        HttpServletRequest httpServletRequest = FacesUtil.getRequest();
        HttpServletResponse httpServletResponse = FacesUtil.getResponse();
        if (user == null) {
            log.debug("user not found in system! (user: {})", userName.trim());
            userAudit.addFailed(userName.trim(), "Login", "", "User not found in system!");
            return "unSecured";
        }
        UserDetail userDetail = new UserDetail(user.getUserName(), user.getRole().getName(), user.getRole().getRoleType().getRoleTypeName().name());
        try {
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

            userAudit.addSucceed(userDetail.getUserName(), "Login", "");
            return user.getRole().getRoleType().getRoleTypeName().name();
        } catch (AuthenticationException e) {
            userAudit.addException(userName.trim(), "Login", "", e.getMessage());
            log.debug("login failed!. ({})", e.getMessage());
        }
        userAudit.addFailed(userName.trim(), "Login", "", "Authentication failed!");
        return "failed";
    }

    public UserDetail getUserDetail() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String logout() {
        log.debug("logging out.");
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.clearContext();
        userAudit.addSucceed(userDetail.getUserName(),"Logout","");
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
