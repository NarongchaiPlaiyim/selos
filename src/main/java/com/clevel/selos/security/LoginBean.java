package com.clevel.selos.security;

import com.clevel.selos.dao.audit.ActivityLogDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.RoleTypeName;
import com.clevel.selos.model.db.audit.ActivityLog;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.system.Language;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;
    @Inject
    ActivityLogDAO activityLogDAO;

    private String userName;
    private String password;

    @Inject
    private SimpleAuthenticationManager authenticationManager;

    public String login() {
        User user = userDAO.findByUserName(userName.trim());
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (user==null) {
            log.debug("user not found in system! (user: {})",userName.trim());
            return "unSecured";
        }
        UserDetail userDetail = new UserDetail(user.getUserName(),user.getRole().getName(),user.getRole().getRoleType().getRoleTypeName().name());
        try {
            UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(userDetail, this.getPassword());
            request.setDetails(new WebAuthenticationDetails(httpServletRequest));
            Authentication result = authenticationManager.authenticate(request);
            log.debug("authentication result: {}", result);
            SecurityContextHolder.getContext().setAuthentication(result);
            log.debug("login successful. ({})",userDetail);
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            httpSession.setAttribute("language", Language.EN);

            return user.getRole().getRoleType().getRoleTypeName().name();
        } catch (AuthenticationException e) {
            log.debug("login failed!. ({})", e.getMessage());
        }
        return "failed";
    }

    public UserDetail getUserDetail() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String logout() {
        log.debug("logging out.");
        SecurityContextHolder.clearContext();
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
}
