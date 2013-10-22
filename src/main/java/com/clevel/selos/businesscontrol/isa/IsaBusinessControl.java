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
import java.io.Serializable;

@Stateless
public class IsaBusinessControl implements Serializable{

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

        if(isaCreateUserView.getUserZone().getId()==0){
        user.setZone(isaCreateUserView.getUserZone());
        }
        user.setUserStatus(UserStatus.NORMAL);

        userDAO.save(user);

        RequestContext.getCurrentInstance().execute("newUserDlg.hide()");

        System.out.println(isaCreateUserView.toString());
    }

    public IsaCreateUserView editUser(String id){
        log.debug("editUser()");

        User user=userDAO.findById(id);

        IsaCreateUserView isaCreateUserView=new IsaCreateUserView();
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


    public void deleteUser(String id){
        log.debug("deleteUser()");

        User user=userDAO.findById(id);
        user.setUserStatus(UserStatus.MARK_AS_DELETED);
        userDAO.persist(user);
        RequestContext context=RequestContext.getCurrentInstance();

        context.update(":userTableData");
        context.execute("confirmDlg.hide()");
    }

}
