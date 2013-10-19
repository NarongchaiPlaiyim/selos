package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaCreateUserView;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class IsaBusinessControl {

    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    @Inject
    public IsaBusinessControl(){

    }

    @PostConstruct
    public void onCreate(){

    }

    public void createUser(IsaCreateUserView isaCreateUserView){

        log.debug("createUser()");

        User user=new User();
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

        userDAO.save(user);

        RequestContext context =RequestContext.getCurrentInstance();
        context.execute("newUserDialog.hide()");

        System.out.println(isaCreateUserView.toString());
    }


    public void deleteUser(String id){
        log.debug("deleteUser()");

        User user=userDAO.findById(id);
        user.setUserStatus(UserStatus.MARK_AS_DELETED);
        userDAO.persist(user);
    }
}
