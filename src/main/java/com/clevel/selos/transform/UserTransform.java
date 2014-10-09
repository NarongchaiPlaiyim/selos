package com.clevel.selos.transform;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.UserView;
import com.clevel.selos.model.view.isa.IsaManageUserView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserTransform extends Transform {
    @Inject
    private UserDAO userDAO;
    @Inject
    @SELOS
    private Logger log;

    public UserView transformToView(User user) {
        UserView userView = new UserView();
        if (user == null) {
            return userView;
        }
        userView.setId(user.getId());
        userView.setUserName(user.getUserName());
        userView.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");
        userView.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
        userView.setPositionName(user.getPosition() != null ? user.getPosition().getName() : "");
        return userView;
    }

    public User transformToModel(final IsaManageUserView view, final User user){
        log.debug("-- transformToModel()");
        User model = null;
        if(!Util.isNull(view)){
            if(!Util.isNull(view.getId()) && !Util.isZero(view.getId().length())){
                model = userDAO.findById(view.getId());
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
            } else {
                model = new User();
                model.setId(view.getId());
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setUserName(view.getUsername());
            model.setBuCode(view.getBuCode());
            model.setPhoneExt(view.getPhoneExt());
            model.setPhoneNumber(view.getPhoneNumber());
            model.setEmailAddress(view.getEmailAddress());

            if(!Util.isNull(view.getRole()) && !Util.isZero(view.getRole().getId())){
                model.setRole(view.getRole());
            } else {
                model.setRole(null);
            }

            if(!Util.isNull(view.getUserTeam()) && !Util.isZero(view.getUserTeam().getId())){
                model.setTeam(view.getUserTeam());
            } else {
                model.setTeam(null);
            }

            if(!Util.isNull(view.getUserDepartment()) && !Util.isZero(view.getUserDepartment().getId())){
                model.setDepartment(view.getUserDepartment());
            } else {
                model.setDepartment(null);
            }

            if(!Util.isNull(view.getUserDivision()) && !Util.isZero(view.getUserDivision().getId())){
                model.setDivision(view.getUserDivision());
            } else {
                model.setDivision(null);
            }

            if(!Util.isNull(view.getUserRegion()) && !Util.isZero(view.getUserRegion().getId())){
                model.setRegion(view.getUserRegion());
            } else {
                model.setRegion(null);
            }

            if(!Util.isNull(view.getUserTitle()) && !Util.isZero(view.getUserTitle().getId())){
                model.setTitle(view.getUserTitle());
            } else {
                model.setTitle(null);
            }

            model.setActive(view.getActive());
            model.setUserStatus(view.getUserStatus());
        }
        return model;
    }

    public User transformToModelAfterDelete(final IsaManageUserView view, final User user){
        log.debug("-- transformToModel()");
        User model = null;
        if(!Util.isNull(view)){
            if(!Util.isNull(view.getId()) && !Util.isZero(view.getId().length())){
                model = userDAO.findById(view.getId());
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
            } else {
                model = new User();
                model.setId(view.getId());
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setUserName(view.getUsername());
            model.setBuCode(view.getBuCode());
            model.setPhoneExt(view.getPhoneExt());
            model.setPhoneNumber(view.getPhoneNumber());
            model.setEmailAddress(view.getEmailAddress());

            if(!Util.isNull(view.getRole()) && !Util.isZero(view.getRole().getId())){
                model.setRole(view.getRole());
            } else {
                model.setRole(null);
            }

            if(!Util.isNull(view.getUserTeam()) && !Util.isZero(view.getUserTeam().getId())){
                model.setTeam(view.getUserTeam());
            } else {
                model.setTeam(null);
            }

            if(!Util.isNull(view.getUserDepartment()) && view.getUserDepartment().getId() != 0){
                model.setDepartment(view.getUserDepartment());
            } else {
                model.setDepartment(null);
            }

            if(!Util.isNull(view.getUserDivision()) && view.getUserDivision().getId() != 0){
                model.setDivision(view.getUserDivision());
            } else {
                model.setDivision(null);
            }

            if(!Util.isNull(view.getUserRegion()) && view.getUserRegion().getId() != 0){
                model.setRegion(view.getUserRegion());
            } else {
                model.setRegion(null);
            }

            if(!Util.isNull(view.getUserTitle()) && view.getUserTitle().getId() != 0 ){
                model.setTitle(view.getUserTitle());
            } else {
                model.setTitle(null);
            }

            model.setActive(view.getActive());
            model.setUserStatus(UserStatus.NORMAL);
        }
        return model;
    }

    public User transformToModelModify(final IsaManageUserView view, final User user){
        log.debug("-- transformToModel()");
        User model = null;
        if(!Util.isNull(view)){
            if(!Util.isNull(view.getId()) && !Util.isZero(view.getId().length())){
                model = userDAO.findById(view.getId());
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
            } else {
                model = new User();
                model.setId(view.getId());
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }
            model.setUserName(view.getUsername());
            model.setBuCode(view.getBuCode());
            model.setPhoneExt(view.getPhoneExt());
            model.setPhoneNumber(view.getPhoneNumber());
            model.setEmailAddress(view.getEmailAddress());

            if(!Util.isNull(view.getRole()) && !Util.isZero(view.getRole().getId())){
                model.setRole(view.getRole());
            } else {
                model.setRole(null);
            }

            if(!Util.isNull(view.getUserTeam()) && !Util.isZero(view.getUserTeam().getId())){
                model.setTeam(view.getUserTeam());
            } else {
                model.setTeam(null);
            }

            if(!Util.isNull(view.getUserDepartment()) && view.getUserDepartment().getId() != 0){
                model.setDepartment(view.getUserDepartment());
            } else {
                model.setDepartment(null);
            }

            if(!Util.isNull(view.getUserDivision()) && view.getUserDivision().getId() != 0){
                model.setDivision(view.getUserDivision());
            } else {
                model.setDivision(null);
            }

            if(!Util.isNull(view.getUserRegion()) && view.getUserRegion().getId() != 0){
                model.setRegion(view.getUserRegion());
            } else {
                model.setRegion(null);
            }

            if(!Util.isNull(view.getUserTitle()) && view.getUserTitle().getId() != 0 ){
                model.setTitle(view.getUserTitle());
            } else {
                model.setTitle(null);
            }

            model.setActive(view.getActive());
            model.setUserStatus(UserStatus.NORMAL);
        }
        return model;
    }

    public User transformToNewModel(final IsaManageUserView view, final User user){
        log.debug("-- transformToNewModel(View : {})", view.toString());
        User model = null;
        if(!Util.isNull(view)){
            model = new User();
            model.setId(view.getId());
            model.setUserName(view.getUsername());
            model.setBuCode(view.getBuCode());
            model.setPhoneExt(view.getPhoneExt());
            model.setPhoneNumber(view.getPhoneNumber());
            model.setEmailAddress(view.getEmailAddress());

            if(!Util.isNull(view.getRole())){
                model.setRole(view.getRole());
            }

            if(!Util.isNull(view.getUserTeam()) && !Util.isZero(view.getUserTeam().getId())){
                model.setTeam(view.getUserTeam());
            } else {
                model.setTeam(null);
            }

            if(!Util.isNull(view.getUserDepartment()) && !Util.isZero(view.getUserDepartment().getId())){
                model.setDepartment(view.getUserDepartment());
            } else {
                model.setDepartment(null);
            }

            if(!Util.isNull(view.getUserDivision()) && !Util.isZero(view.getUserDivision().getId())){
                model.setDivision(view.getUserDivision());
            } else {
                model.setDivision(null);
            }

            if(!Util.isNull(view.getUserRegion()) && !Util.isZero(view.getUserRegion().getId())){
                model.setRegion(view.getUserRegion());
            } else {
                model.setRegion(null);
            }

            if(!Util.isNull(view.getUserTitle()) && !Util.isZero(view.getUserTitle().getId())){
                model.setTitle(view.getUserTitle());
            } else {
                model.setTitle(null);
            }

            model.setActive(view.getActive());
            model.setUserStatus(view.getUserStatus());
            model.setCreateBy(user);
            model.setCreateDate(DateTime.now().toDate());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
        }
        return model;
    }

    public IsaManageUserView transformToISAView(final User model){
        log.debug("-- transformToISAView()");
        IsaManageUserView view = null;
        log.debug("-- Model : {}", model.toString());
        if(!Util.isNull(model)){
            view = new IsaManageUserView();
            view.setId(model.getId());
            view.setUsername(model.getUserName());
            view.setBuCode(model.getBuCode());
            view.setPhoneExt(model.getPhoneExt());
            view.setPhoneNumber(model.getPhoneNumber());
            view.setEmailAddress(model.getEmailAddress());
            view.setUserStatus(model.getUserStatus());
            view.setActive(model.getActive());
            if(!Util.isNull(model.getRole())){
                view.setRole(model.getRole());
            } else {
                view.setRole(new Role());
            }
            if(!Util.isNull(model.getDepartment())){
                view.setUserDepartment(model.getDepartment());
            } else {
                view.setUserDepartment(new UserDepartment());
            }
            if(!Util.isNull(model.getDivision())){
                view.setUserDivision(model.getDivision());
            } else {
                view.setUserDivision(new UserDivision());
            }
            if(!Util.isNull(model.getRegion())){
                view.setUserRegion(model.getRegion());
            } else {
                view.setUserRegion(new UserRegion());
            }
            if(!Util.isNull(model.getTeam())){
                view.setUserTeam(model.getTeam());
            } else {
                view.setUserTeam(new UserTeam());
            }
            if(!Util.isNull(model.getTitle())){
                view.setUserTitle(model.getTitle());
            } else {
                view.setUserTitle(new UserTitle());
            }
            if(!Util.isNull(model.getZone())){
                view.setUserZone(model.getZone());
            } else {
                view.setUserZone(new UserZone());
            }
            log.debug("-- IsaManageUserView : {}", view.toString());
        }
        return view;
    }
}
