package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.ManageUserAction;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaManageUserView;
import com.clevel.selos.model.view.IsaSearchView;
import com.clevel.selos.model.view.IsaUserReportView;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    public void createUser(IsaManageUserView isaManageUserView) throws Exception {

        log.debug("createUser()");

        User user = new User();
        user.setId(isaManageUserView.getId());
        user.setUserName(isaManageUserView.getUsername());
        user.setBuCode(isaManageUserView.getBuCode());
        user.setPhoneExt(isaManageUserView.getPhoneExt());
        user.setPhoneNumber(isaManageUserView.getPhoneNumber());
        user.setEmailAddress(isaManageUserView.getEmailAddress());
        user.setRole(isaManageUserView.getRole());
        user.setDepartment(isaManageUserView.getUserDepartment());
        user.setDivision(isaManageUserView.getUserDivision());
        user.setRegion(isaManageUserView.getUserRegion());
        user.setTeam(isaManageUserView.getUserTeam());
        user.setTitle(isaManageUserView.getUserTitle());
        user.setZone(isaManageUserView.getUserZone());
        user.setUserStatus(UserStatus.NORMAL);
        user.setActive(isaManageUserView.getActive());


        if (isaManageUserView.getUserDepartment().getId() == 0) {
            user.setDepartment(null);
        }
        if (isaManageUserView.getRole().getId() == 0) {
            user.setRole(null);
        }
        if (isaManageUserView.getUserDivision().getId() == 0) {
            user.setDivision(null);
        }
        if (isaManageUserView.getUserRegion().getId() == 0) {
            user.setRegion(null);
        }
        if (isaManageUserView.getUserTeam().getId() == 0) {
            user.setTeam(null);
        }
        if (isaManageUserView.getUserTitle().getId() == 0) {
            user.setTitle(null);
        }
        if (isaManageUserView.getUserZone().getId() == 0) {
            user.setZone(null);
        }

        userDAO.persist(user);

    }


    public void editUser(IsaManageUserView isaManageUserView) throws ConstraintViolationException, Exception {

        log.debug("editUser()");


        User user = userDAO.findById(isaManageUserView.getId());


        user.setUserName(isaManageUserView.getUsername());
        user.setBuCode(isaManageUserView.getBuCode());
        user.setPhoneExt(isaManageUserView.getPhoneExt());
        user.setPhoneNumber(isaManageUserView.getPhoneNumber());
        user.setEmailAddress(isaManageUserView.getEmailAddress());
        user.setRole(isaManageUserView.getRole());
        user.setDepartment(isaManageUserView.getUserDepartment());
        user.setDivision(isaManageUserView.getUserDivision());
        user.setRegion(isaManageUserView.getUserRegion());
        user.setTeam(isaManageUserView.getUserTeam());
        user.setTitle(isaManageUserView.getUserTitle());
        user.setZone(isaManageUserView.getUserZone());
        user.setActive(isaManageUserView.getActive());
        user.setUserStatus(user.getUserStatus());


        if (isaManageUserView.getUserDepartment().getId() == 0) {
            user.setDepartment(null);
        }
        if (isaManageUserView.getRole().getId() == 0) {
            user.setRole(null);
        }
        if (isaManageUserView.getUserDivision().getId() == 0) {
            user.setDivision(null);
        }
        if (isaManageUserView.getUserRegion().getId() == 0) {
            user.setRegion(null);
        }
        if (isaManageUserView.getUserTeam().getId() == 0) {
            user.setTeam(null);
        }
        if (isaManageUserView.getUserTitle().getId() == 0) {
            user.setTitle(null);
        }
        if (isaManageUserView.getUserZone().getId() == 0) {
            user.setZone(null);
        }

        userDAO.persist(user);

    }


    public IsaManageUserView SelectUserById(String id) throws Exception {
        log.debug("SelectUserById()");
        System.out.println("SelectUserById : " + id);
        User user = userDAO.findById(id);

        IsaManageUserView isaManageUserView = new IsaManageUserView();
        isaManageUserView.setId(user.getId());
        isaManageUserView.setUsername(user.getUserName());
        isaManageUserView.setPhoneExt(user.getPhoneExt());
        isaManageUserView.setPhoneNumber(user.getPhoneNumber());
        isaManageUserView.setBuCode(user.getBuCode());
        isaManageUserView.setEmailAddress(user.getEmailAddress());
        isaManageUserView.setRole(user.getRole() != null ? user.getRole() : new Role());
        isaManageUserView.setUserDepartment(user.getDepartment() != null ? user.getDepartment() : new UserDepartment());
        isaManageUserView.setUserDivision(user.getDivision() != null ? user.getDivision() : new UserDivision());
        isaManageUserView.setUserRegion(user.getRegion() != null ? user.getRegion() : new UserRegion());
        isaManageUserView.setUserTeam(user.getTeam() != null ? user.getTeam() : new UserTeam());
        isaManageUserView.setUserTitle(user.getTitle() != null ? user.getTitle() : new UserTitle());
        isaManageUserView.setUserZone(user.getZone() != null ? user.getZone() : new UserZone());
        isaManageUserView.setActive(user.getActive());

        return isaManageUserView;
    }


    public void deleteUser(String id) throws Exception {
        log.debug("deleteUser()");

        User user = userDAO.findById(id);
        user.setUserStatus(UserStatus.MARK_AS_DELETED);
        userDAO.persist(user);

    }

    public void deleteUserList(User[] users) throws Exception {
        log.debug("deleteUserList()");

        for (User list : users) {
            User user = userDAO.findById(list.getId());
            user.setUserStatus(UserStatus.MARK_AS_DELETED);
            userDAO.persist(user);

        }
    }


    public List<User> searchUser(IsaSearchView isaSearchView) throws Exception {
        log.debug("searchUser()");

        Criteria criteria = userDAO.createCriteria();

        if (!isaSearchView.getId().equals("")) {
            criteria.add(Restrictions.like("id", "%" + isaSearchView.getId() + "%"));
        }
        if (!isaSearchView.getUsername().equals("")) {
            criteria.add(Restrictions.like("userName", "%" + isaSearchView.getUsername() + "%"));
        }
        if (isaSearchView.getRoleId().getId() != 0) {
            criteria.add(Restrictions.eq("role", isaSearchView.getRoleId()));
        }
        if (isaSearchView.getDepartmentId().getId() != 0) {
            criteria.add(Restrictions.eq("department", isaSearchView.getDepartmentId()));
        }
        if (isaSearchView.getDivisionId().getId() != 0) {
            criteria.add(Restrictions.eq("division", isaSearchView.getDivisionId()));
        }
        if (isaSearchView.getRegionId().getId() != 0) {
            criteria.add(Restrictions.eq("region", isaSearchView.getRegionId()));
        }
        if (isaSearchView.getTeamId().getId() != 0) {
            criteria.add(Restrictions.eq("team", isaSearchView.getTeamId()));
        }
        if (isaSearchView.getTitleId().getId() != 0) {
            criteria.add(Restrictions.eq("title", isaSearchView.getTitleId()));
        }
        if (isaSearchView.getZoneId().getId() != 0) {
            criteria.add(Restrictions.eq("zone", isaSearchView.getZoneId()));
        }
        List<User> list = criteria.list();


        return list;
    }

    public void editUserActive(User[] users, ManageUserActive manageUserActive) throws Exception {
        log.debug("editUserActive()");
        System.out.println("========================= : " + manageUserActive.getValue());
        for (User list : users) {
            User user = userDAO.findById(list.getId());
            user.setActive(manageUserActive.getValue());
            userDAO.persist(user);
        }

    }

    public List<User>getUserAuditLogReport ()throws Exception{

        return null;
    }


    public List<IsaUserReportView> getUserNotLogonOver(int day) throws Exception {
        log.debug("getUserNotLogonOver");

        List<IsaUserReportView>list=new ArrayList<IsaUserReportView>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -(day+1));

        List<User> users = userDAO.findByCriteria(Restrictions.le("lastLogon", cal.getTime()));
            for(User userlist:users){
                IsaUserReportView isaUserReportView=new IsaUserReportView();
                isaUserReportView.setUserId(userlist.getId());
                isaUserReportView.setUserName(userlist.getUserName());
                isaUserReportView.setEmailAddress(userlist.getEmailAddress());
                isaUserReportView.setBuCode(userlist.getBuCode());
                isaUserReportView.setLastIp(userlist.getLastIP());
                isaUserReportView.setLastLogon(userlist.getLastLogon());
                isaUserReportView.setPhoneExt(userlist.getPhoneExt());
                isaUserReportView.setPhoneNumber(userlist.getPhoneNumber());
                isaUserReportView.setRole(userlist.getRole().getName());
                isaUserReportView.setDepartment(userlist.getDepartment().getName());
                isaUserReportView.setDivision(userlist.getDivision().getName());
                isaUserReportView.setRegion(userlist.getRegion().getName());
                isaUserReportView.setTeam(userlist.getTeam().getName());
                isaUserReportView.setTitle(userlist.getTitle().getName());
                isaUserReportView.setZone(userlist.getZone().getName());
                isaUserReportView.setActive(userlist.getActive()==1?ManageUserActive.ACTIVE:ManageUserActive.INACTIVE);
                isaUserReportView.setUserStatus(userlist.getUserStatus().name());

                list.add(isaUserReportView);
            }

        return list;
    }


    public List<IsaUserReportView> getUserReportList() throws Exception {
        log.debug("getUserNotLogonOver");

        List<IsaUserReportView>list=new ArrayList<IsaUserReportView>();

        Role role=new Role();
        role.setId(1);

        List<User> users = userDAO.findByCriteria(Restrictions.gt("role",role));
        for(User userlist:users){
            IsaUserReportView isaUserReportView=new IsaUserReportView();
            isaUserReportView.setUserId(userlist.getId());
            isaUserReportView.setUserName(userlist.getUserName());
            isaUserReportView.setEmailAddress(userlist.getEmailAddress());
            isaUserReportView.setBuCode(userlist.getBuCode());
            isaUserReportView.setLastIp(userlist.getLastIP());
            isaUserReportView.setLastLogon(userlist.getLastLogon());
            isaUserReportView.setPhoneExt(userlist.getPhoneExt());
            isaUserReportView.setPhoneNumber(userlist.getPhoneNumber());
            isaUserReportView.setRole(userlist.getRole().getName());
            isaUserReportView.setDepartment(userlist.getDepartment().getName());
            isaUserReportView.setDivision(userlist.getDivision().getName());
            isaUserReportView.setRegion(userlist.getRegion().getName());
            isaUserReportView.setTeam(userlist.getTeam().getName());
            isaUserReportView.setTitle(userlist.getTitle().getName());
            isaUserReportView.setZone(userlist.getZone().getName());
            isaUserReportView.setActive(userlist.getActive()==1?ManageUserActive.ACTIVE:ManageUserActive.INACTIVE);
            isaUserReportView.setUserStatus(userlist.getUserStatus().name());

            list.add(isaUserReportView);
        }

        return list;
    }



}
