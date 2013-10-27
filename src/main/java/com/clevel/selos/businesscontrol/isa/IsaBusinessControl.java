package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaCreateUserView;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.sql.SQLException;

@Stateless
public class IsaBusinessControl implements Serializable {

    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    @Inject
    public IsaBusinessControl() {

    }

    @PostConstruct
    public void onCreate() {

    }

    boolean complete = true;

    public void createUser(IsaCreateUserView isaCreateUserView) throws Exception{

        log.debug("createUser()");

        User user = new User();
        user.setId(isaCreateUserView.getUsername());
        user.setUserName(isaCreateUserView.getUsername());
        user.setBuCode(isaCreateUserView.getBuCode());
        user.setPhoneExt(isaCreateUserView.getPhoneExt());
        user.setPhoneNumber(isaCreateUserView.getPhoneNumber());
        user.setEmailAddress(isaCreateUserView.getEmailAddress());
        user.setRole(isaCreateUserView.getRole());
        user.setDepartment(isaCreateUserView.getUserDepartment());
        user.setDivision(isaCreateUserView.getUserDivision());
        user.setRegion(isaCreateUserView.getUserRegion());
        user.setTeam(isaCreateUserView.getUserTeam());
        user.setTitle(isaCreateUserView.getUserTitle());
        user.setZone(isaCreateUserView.getUserZone());
        user.setUserStatus(UserStatus.NORMAL);
        user.setActive(isaCreateUserView.getActive());

            userDAO.persist(user);

    }


    public void editUser(IsaCreateUserView isaCreateUserView) throws ConstraintViolationException,Exception {

        log.debug("editUser()");
        RequestContext context=RequestContext.getCurrentInstance();
        User user = new User();
        user.setId(isaCreateUserView.getUsername());
        user.setUserName(isaCreateUserView.getUsername());
        user.setBuCode(isaCreateUserView.getBuCode());
        user.setPhoneExt(isaCreateUserView.getPhoneExt());
        user.setPhoneNumber(isaCreateUserView.getPhoneNumber());
        user.setEmailAddress(isaCreateUserView.getEmailAddress());
        user.setRole(isaCreateUserView.getRole());
        user.setDepartment(isaCreateUserView.getUserDepartment());
        user.setDivision(isaCreateUserView.getUserDivision());
        user.setRegion(isaCreateUserView.getUserRegion());
        user.setTeam(isaCreateUserView.getUserTeam());
        user.setTitle(isaCreateUserView.getUserTitle());

        user.setZone(isaCreateUserView.getUserZone());

        user.setUserStatus(UserStatus.NORMAL);

            userDAO.persist(user);

    }


    public IsaCreateUserView SelectUserById(String id) {
        log.debug("SelectUserById()");
        System.out.println("SelectUserById : "+id);
        User user = userDAO.findById(id);

        IsaCreateUserView isaCreateUserView = new IsaCreateUserView();
        isaCreateUserView.setId(user.getId());
        isaCreateUserView.setUsername(user.getUserName());
        isaCreateUserView.setPhoneExt(user.getPhoneExt());
        isaCreateUserView.setPhoneNumber(user.getPhoneNumber());
        isaCreateUserView.setBuCode(user.getBuCode());
        isaCreateUserView.setEmailAddress(user.getEmailAddress());
        isaCreateUserView.setRole(user.getRole());
        isaCreateUserView.setUserDepartment(user.getDepartment());
        isaCreateUserView.setUserDivision(user.getDivision());
        isaCreateUserView.setUserRegion(user.getRegion());
        isaCreateUserView.setUserTeam(user.getTeam());
        isaCreateUserView.setUserTitle(user.getTitle());
        isaCreateUserView.setUserZone(user.getZone());

        return isaCreateUserView;
    }




    public void deleteUser(String id) {
        log.debug("deleteUser()");

        RequestContext context=RequestContext.getCurrentInstance();
        User user = userDAO.findById(id);
        user.setUserStatus(UserStatus.MARK_AS_DELETED);

        try {
            userDAO.persist(user);
        } catch (ConstraintViolationException ex) {
            complete = false;
            throw ex;
        } catch (Exception e) {
            complete = false;
        }

        context.addCallbackParam("functionComplete", complete);

    }

}
