package com.clevel.selos.security;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.db.master.User;
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
        User user = userDAO.findByUserName(this.getUserName().trim());
        if (user==null) {
            log.debug("user not found in system!");
            return "unSecured";
        }
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), this.getPassword());
            authenticationManager.setUser(user);
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            log.debug("login successful. (user: {})",user.getUserName());
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            httpSession.setAttribute("language", Language.EN);
            switch (user.getRole().getRoleType().getId()) {
                case 1: return RoleTypeName.SYSTEM.name();
                case 2: return RoleTypeName.BUSINESS.name();
                case 3: return RoleTypeName.NON_BUSINESS.name();
            }
        } catch (AuthenticationException e) {
            log.debug("login failed!. ({})", e.getMessage());
        }
        return "failed";
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
