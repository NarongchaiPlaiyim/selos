package com.clevel.selos.transform;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.UserView;
import com.clevel.selos.model.view.isa.IsaManageUserView;
import com.clevel.selos.util.Util;
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
        return userView;
    }

    public User transformToModel(final IsaManageUserView view){
        log.debug("-- transformToModel()");
        User model = null;
        if(!Util.isNull(view)){
            if(!Util.isNull(view.getId()) && !Util.isZero(view.getId().length())){
                model = userDAO.findById(view.getId());
            } else {
                model = new User();
                model.setId(view.getId());
            }
            model.setUserName(view.getUsername());
            model.setBuCode(view.getBuCode());
            model.setPhoneExt(view.getPhoneExt());
            model.setPhoneNumber(view.getPhoneNumber());
            model.setEmailAddress(view.getEmailAddress());
            model.setRole(view.getRole());
            model.setDepartment(view.getUserDepartment());
            model.setDivision(view.getUserDivision());
            model.setRegion(view.getUserRegion());
            model.setTeam(view.getUserTeam());
            model.setTitle(view.getUserTitle());
            model.setZone(view.getUserZone());
            model.setActive(view.getActive());
            model.setUserStatus(view.getUserStatus());
        }
        return model;
    }

    public IsaManageUserView transformToISAView(final User model){
        log.debug("-- transformToISAView()");
        IsaManageUserView view = null;
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
        }
        return view;
    }
}
