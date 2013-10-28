package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.ManageUserAction;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaCreateUserView;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "isa")
public class Isa implements Serializable {

    @Inject
    Logger log;

    @Inject
    UserDAO userDAO;

    @Inject
    RoleDAO roleDAO;

    @Inject
    UserDepartmentDAO userDepartmentDAO;

    @Inject
    UserDivisionDAO userDivisionDAO;

    @Inject
    UserRegionDAO userRegionDAO;

    @Inject
    UserTeamDAO userTeamDAO;

    @Inject
    UserTitleDAO userTitleDAO;

    @Inject
    UserZoneDAO userZoneDAO;

    @Inject
    IsaBusinessControl isaBusinessControl;

    public Isa() {
         complete=true;
    }

    private List<User> userDetail;
    private List<Role> userRoleList;
    private List<UserDepartment> userDepartmentList;
    private List<UserDivision> userDivisionList;
    private List<UserRegion> userRegionList;
    private List<UserTeam> userTeamList;
    private List<UserTitle> userTitleList;
    private List<UserZone> userZoneList;

    private IsaCreateUserView createUserView;

    private String userSize;
    private String id;

    private String messageHeader;
    private String message;


    @PostConstruct
    public void onCreate() {
        onSelectUser();
    }

    private boolean complete;

    public void onManageUserAction() {
        log.debug("onCreateNewUser()");
        RequestContext context = RequestContext.getCurrentInstance();


        try {
            if (createUserView.getFlag() == ManageUserAction.ADD) {
                messageHeader = "Add New User!";

                User user = userDAO.findByUserName(createUserView.getUsername());
                if (user != null) {
                    complete = false;
                    message = "Add new User failed. Cause : Duplicate UserId found in system!";

                } else {
                    isaBusinessControl.createUser(createUserView);
                    onSelectUser();
                    message = "Add New User Success.";
                }

            } else if (createUserView.getFlag() == ManageUserAction.EDIT) {
                messageHeader = "Edit User!";
                isaBusinessControl.editUser(createUserView);
                onSelectUser();
                message="Edit User Success.";

            }

            context.addCallbackParam("functionComplete", complete);
            context.execute("msgBoxSystemMessageDlg.show()");
        }catch (ConstraintViolationException e){

            message="Edit User failed. Cause :";
            context.execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex) {
            log.debug("Exception : {}", ex);
            complete = false;
            if (ex.getCause() != null) {
                message = "Add new customer failed. Cause : " + ex.getCause().getMessage();
            } else {
                message = "Add new customer failed. Cause : " + ex.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onSelectUser() {

        log.debug("onSelectUser()");
        userDetail = new ArrayList<User>();
        User user = new User();
        userDetail = userDAO.findByCriteria(Restrictions.eq("userStatus", UserStatus.NORMAL));
        userSize = userDetail.size() + "";
    }

    public void getSelectUserDetailList() {
        try{
        userRoleList = roleDAO.findAll();
        userDepartmentList = userDepartmentDAO.findAll();
        userDivisionList = userDivisionDAO.findAll();
        userRegionList = userRegionDAO.findAll();
        userTeamList = userTeamDAO.findAll();
        userTitleList = userTitleDAO.findAll();
        userZoneList = userZoneDAO.findAll();
        }catch (Exception e){

        }

    }


    public void onOpenNewUserForm() {
        log.debug("onCreateNewUser()");
        createUserView = new IsaCreateUserView();
        createUserView.reset();
        createUserView.setFlag(ManageUserAction.ADD);

        getSelectUserDetailList();
    }


    public void onOpenEditForm(String id) {
        System.out.println("------------------ " + id);

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            createUserView = isaBusinessControl.SelectUserById(id);
            if (createUserView != null) {

                createUserView.setFlag(ManageUserAction.EDIT);
                getSelectUserDetailList();

            } else {
                messageHeader = "Edit Form";
                message = "Open edit form failed. Cause : User is not found!";
                context.execute("msgBoxSystemMessageDlg.show()");
            }
        } catch (Exception e) {
            messageHeader = "Edit Form";
            if (e.getCause() != null) {
                message = "Open edit form failed. Cause : "+e.getCause().getMessage();
            } else {
                message = "Open edit form failed. Cause : "+e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");

        }
    }

    public void onDelete() {
        System.out.println("------------------ " + id);

        isaBusinessControl.deleteUser(id);
        onSelectUser();
    }


    public List<User> getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(List<User> userDetail) {
        this.userDetail = userDetail;
    }

    public List<Role> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<Role> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public List<UserDepartment> getUserDepartmentList() {
        return userDepartmentList;
    }

    public void setUserDepartmentList(List<UserDepartment> userDepartmentList) {
        this.userDepartmentList = userDepartmentList;
    }

    public List<UserDivision> getUserDivisionList() {
        return userDivisionList;
    }

    public void setUserDivisionList(List<UserDivision> userDivisionList) {
        this.userDivisionList = userDivisionList;
    }

    public List<UserRegion> getUserRegionList() {
        return userRegionList;
    }

    public void setUserRegionList(List<UserRegion> userRegionList) {
        this.userRegionList = userRegionList;
    }

    public List<UserTeam> getUserTeamList() {
        return userTeamList;
    }

    public void setUserTeamList(List<UserTeam> userTeamList) {
        this.userTeamList = userTeamList;
    }

    public List<UserTitle> getUserTitleList() {
        return userTitleList;
    }

    public void setUserTitleList(List<UserTitle> userTitleList) {
        this.userTitleList = userTitleList;
    }

    public IsaCreateUserView getCreateUserView() {
        return createUserView;
    }

    public void setCreateUserView(IsaCreateUserView createUserView) {
        this.createUserView = createUserView;
    }

    public List<UserZone> getUserZoneList() {
        return userZoneList;
    }

    public void setUserZoneList(List<UserZone> userZoneList) {
        this.userZoneList = userZoneList;
    }

    public String getUserSize() {
        return userSize;
    }

    public void setUserSize(String userSize) {
        this.userSize = userSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }
}
