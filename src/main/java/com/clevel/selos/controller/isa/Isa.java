package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.ManageUserAction;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaCreateUserView;
import org.hibernate.Hibernate;
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
import java.util.Date;
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

    }

    private User[] selectUserDetail;

    public User[] getSelectUserDetail() {
        return selectUserDetail;
    }

    public void setSelectUserDetail(User[] selectUserDetail) {
        this.selectUserDetail = selectUserDetail;
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
        complete = true;
        System.out.println("------------------  :  " + createUserView.toString());

        try {
            if (createUserView.getFlag() == ManageUserAction.ADD) {
                messageHeader = "Add New User.";

                User user = userDAO.findOneByCriteria(Restrictions.eq("id", createUserView.getId()));
                if (user != null) {
                    complete = false;
                    message = "Add new User failed. Cause : Duplicate UserId found in system!";

                } else {
                    isaBusinessControl.createUser(createUserView);
                    onSelectUser();
                    message = "Add New User Success.";
                }

            } else if (createUserView.getFlag() == ManageUserAction.EDIT) {
                messageHeader = "Edit User.";
                isaBusinessControl.editUser(createUserView);
                onSelectUser();
                message = "Edit User Success.";

            }

            context.addCallbackParam("functionComplete", complete);
            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (ConstraintViolationException e) {

            message = "Edit User failed. Cause :";
            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
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
//        userDetail = userDAO.findByCriteria(Restrictions.eq("userStatus", UserStatus.NORMAL));
        userDetail = userDAO.findAll();
        getSelectUserDetailList();
        userSize = userDetail.size() + "";
    }

    public void getSelectUserDetailList() {
        try {
            userRoleList = roleDAO.findAll();
            userDepartmentList = userDepartmentDAO.findAll();
            userDivisionList = userDivisionDAO.findAll();
            userRegionList = userRegionDAO.findAll();
            userTeamList = userTeamDAO.findAll();
            userTitleList = userTitleDAO.findAll();
            userZoneList = userZoneDAO.findAll();
        } catch (Exception e) {

        }

    }


    public void onOpenNewUserForm() {
        log.debug("onCreateNewUser()");
        createUserView = new IsaCreateUserView();
        createUserView.reset();
        createUserView.getRole().setId(-1);
        createUserView.setFlag(ManageUserAction.ADD);


        getSelectUserDetailList();
    }


    public void onOpenEditForm() {
        System.out.println("------------------ " + id);

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            createUserView = isaBusinessControl.SelectUserById(id);
            if (createUserView != null) {

                createUserView.setFlag(ManageUserAction.EDIT);
                getSelectUserDetailList();

            } else {
                messageHeader = "Edit Form.";
                message = "Open edit form failed. Cause : User is not found!";
                context.execute("msgBoxSystemMessageDlg.show()");
            }
        } catch (Exception e) {
            messageHeader = "Edit Form.";
            if (e.getCause() != null) {
                message = "Open edit form failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Open edit form failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");

        }
    }

    public void onDelete() {
        System.out.println("------------------ " + id);
        complete = true;
        try {
            isaBusinessControl.deleteUser(id);
            onSelectUser();

        } catch (Exception e) {
            complete = false;
            messageHeader = "Delete User.";
            if (e.getCause() != null) {
            message = "Delete User failed. Cause : "+e.getCause().getMessage();
            }else{
            message = "Delete User failed. Cause : "+e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onDeleteUserList() {
        System.out.println("-------------------------------------22 : "+selectUserDetail.length);
        for (User list:selectUserDetail){
            System.out.println(list.getId());
        }
           RequestContext context=RequestContext.getCurrentInstance();
        try {
            messageHeader = "Delete User.";

            if(selectUserDetail.length==0){
                message = "Please Select User ";
                context.execute("msgBoxSystemMessageDlg.show()");
            }else{
                context.execute("confirmDeleteUserListDlg.show()");
            }

        } catch (Exception e) {
            complete = false;

            if (e.getCause() != null) {
                message = "Delete User failed. Cause : "+e.getCause().getMessage();
            }else{
                message = "Delete User failed. Cause : "+e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void deleteUserList(){

        RequestContext context=RequestContext.getCurrentInstance();
        try{

        isaBusinessControl.deleteUserList(selectUserDetail);

        }catch (Exception e){

            messageHeader = "Delete User.";
            if (e.getCause() != null) {
                message = "Delete User failed. Cause : "+e.getCause().getMessage();
            }else{
                message = "Delete User failed. Cause : "+e.getMessage();
            }
                    context.execute("msgBoxSystemMessageDlg.show()");
        }
        onSelectUser();
    }


    public void notLogonOver(){
//        List<NotLogonOverView> list = userProfileService.getUserNotLogonOver(days);
//        StringBuilder builder =  new StringBuilder();
//        builder.append("No."); builder.append(COMMA_DELIMITED);
//        builder.append("User ID"); builder.append(COMMA_DELIMITED);
//        builder.append("User Name"); builder.append(COMMA_DELIMITED);
//        builder.append("Create Date"); builder.append(COMMA_DELIMITED);
//        builder.append("Last Login Date"); builder.append(COMMA_DELIMITED);
//        builder.append("Status"); builder.append(COMMA_DELIMITED);
//        builder.append("Number of Day"); builder.append('\n');
//        int number = 1;
//        for(NotLogonOverView notLogonOverView : list) {
//            builder.append(number); builder.append(COMMA_DELIMITED);
//            builder.append('"'+notLogonOverView.getUserId()+'"'); builder.append(COMMA_DELIMITED);
//            builder.append('"'+notLogonOverView.getUserName()+'"'); builder.append(COMMA_DELIMITED);
//            builder.append('"'+notLogonOverView.getCreateDate()+'"'); builder.append(COMMA_DELIMITED);
//            builder.append('"'+notLogonOverView.getLastLogin()+'"'); builder.append(COMMA_DELIMITED);
//            builder.append('"'+notLogonOverView.getStatus()+'"'); builder.append(COMMA_DELIMITED);
//            builder.append(notLogonOverView.getNumberOfDays()); builder.append('\n');
//            number++;
//        }
//        streamExporter.exportCSV(response, "NotLogonOver_"+days+"_day_"+dateFileNameFormat.format(new Date()), builder.toString());
//    }

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
