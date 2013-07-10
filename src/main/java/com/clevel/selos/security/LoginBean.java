package com.clevel.selos.security;

import com.clevel.selos.dao.UserDAO;
import com.clevel.selos.model.db.User;
import com.clevel.selos.system.Language;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    String userName;
    String password;

    private SimpleAuthenticationManager authenticationManager = new SimpleAuthenticationManager();

    public String login() {
        String contextPath = "";
        String urlPath = "";

        User user = userDAO.findByUserName(this.getUserName().trim());
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), this.getPassword());
            authenticationManager.setUser(user);
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            log.debug("login successful.");
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            httpSession.setAttribute("language", Language.EN);
            return "secured";
//            redirect("/admin/simple.jsf");
        } catch (AuthenticationException e) {
            log.debug("login failed!. ({})", e.getMessage());
        }
        return "unSecured";
    }

    public String logout() {
        log.debug("logging out.");
        SecurityContextHolder.clearContext();
        return "loggedOut";
//        redirect("/login.jsf");
    }

    public void checkAuthenticated() {
        if (isLoggedIn()) {
            log.debug("already logged in!");
            redirect("/admin/welcome.jsf");
        }
    }

    public void redirect(String urlPath) {
        String contextPath = "";
        try {
            contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            log.debug("redirect to: {}", contextPath+urlPath);
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath+urlPath);
        } catch (IOException e) {
            log.error("Exception while redirection! (contextPath: {}, urlPath: {})",contextPath,urlPath);
        }
    }


    public boolean isLoggedIn() {
        log.debug("isLoggedIn result is: {}",SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
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
