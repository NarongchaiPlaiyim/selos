package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaCreateUserView;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;

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

    public void createUser(IsaCreateUserView isaCreateUserView) throws Exception {

        log.debug("createUser()");

        User user = new User();
        user.setId(isaCreateUserView.getId());
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


        if (isaCreateUserView.getUserDepartment().getId() == -1) {
            user.setDepartment(null);
        }
        if (isaCreateUserView.getRole().getId() == -1) {
            user.setRole(null);
        }
        if (isaCreateUserView.getUserDivision().getId() == -1) {
            user.setDivision(null);
        }
        if (isaCreateUserView.getUserRegion().getId() == -1) {
            user.setRegion(null);
        }
        if (isaCreateUserView.getUserTeam().getId() == -1) {
            user.setTeam(null);
        }
        if (isaCreateUserView.getUserTitle().getId() == -1) {
            user.setTitle(null);
        }
        if (isaCreateUserView.getUserZone().getId() == -1) {
            user.setZone(null);
        }

        userDAO.persist(user);

    }


    public void editUser(IsaCreateUserView isaCreateUserView) throws ConstraintViolationException, Exception {

        log.debug("editUser()");

        User user = new User();
//        User user =userDAO.findById(isaCreateUserView.getId());

        user.setId(isaCreateUserView.getId());
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
        user.setActive(isaCreateUserView.getActive());
        user.setUserStatus(UserStatus.NORMAL);


        if (isaCreateUserView.getUserDepartment().getId() == 0) {
            user.setDepartment(null);
        }
        if (isaCreateUserView.getRole().getId() == 0) {
            user.setRole(null);
        }
        if (isaCreateUserView.getUserDivision().getId() == 0) {
            user.setDivision(null);
        }
        if (isaCreateUserView.getUserRegion().getId() == 0) {
            user.setRegion(null);
        }
        if (isaCreateUserView.getUserTeam().getId() == 0) {
            user.setTeam(null);
        }
        if (isaCreateUserView.getUserTitle().getId() == 0) {
            user.setTitle(null);
        }
        if (isaCreateUserView.getUserZone().getId() == 0) {
            user.setZone(null);
        }

        userDAO.persist(user);

    }


    public IsaCreateUserView SelectUserById(String id) throws Exception{
        log.debug("SelectUserById()");
        System.out.println("SelectUserById : " + id);
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
        isaCreateUserView.setActive(user.getActive());


        if (isaCreateUserView.getUserDepartment() == null) {
            isaCreateUserView.setUserDepartment(new UserDepartment());
        }
        if (isaCreateUserView.getRole() == null) {
            isaCreateUserView.setRole(new Role());
        }
        if (isaCreateUserView.getUserDivision() == null) {
            isaCreateUserView.setUserDivision(new UserDivision());
        }
        if (isaCreateUserView.getUserRegion() == null) {
            isaCreateUserView.setUserRegion(new UserRegion());
        }
        if (isaCreateUserView.getUserTeam() == null) {
            isaCreateUserView.setUserTeam(new UserTeam());
        }
        if (isaCreateUserView.getUserTitle() == null) {
            isaCreateUserView.setUserTitle(new UserTitle());
        }
        if (isaCreateUserView.getUserZone() == null) {
            isaCreateUserView.setUserZone(new UserZone());
        }

        return isaCreateUserView;
    }


    public void deleteUser(String id) throws Exception {
        log.debug("deleteUser()");

        User user = userDAO.findById(id);
        user.setUserStatus(UserStatus.MARK_AS_DELETED);
        userDAO.persist(user);

    }

    public void deleteUserList(User[] userlist) {
        log.debug("deleteUserList()");

        for (User list : userlist) {
            User user = userDAO.findById(list.getId());
            user.setUserStatus(UserStatus.MARK_AS_DELETED);
            userDAO.persist(user);

        }
    }
}
