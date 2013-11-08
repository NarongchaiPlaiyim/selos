package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageUserAction;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.IsaManageUserView;
import com.clevel.selos.model.view.IsaSearchView;
import com.clevel.selos.model.view.IsaUserReportView;
import com.clevel.selos.util.ScvExport;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "isa")
public class Isa implements Serializable {

    @Inject
    @SELOS
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


    private IsaManageUserView isaManageUserView;
    private IsaSearchView isaSearchView;

    private int userSize;
    private String id;
    private int active;
    private int notLogonOverDay;

    private String messageHeader;
    private String message;

    private static char COMMA_DELIMITED = ',';

    @PostConstruct
    public void onCreate() {
        onSelectUser();
        isaManageUserView = new IsaManageUserView();
        isaSearchView = new IsaSearchView();

        isaManageUserView.reset();
        isaSearchView.reset();
//        isaSearchView.getRoleId().setId(-1);

    }

    private boolean complete;

    public void onManageUserAction() {
        log.debug("onCreateNewUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        System.out.println("------------------  :  " + isaManageUserView.toString());

        try {
            if (isaManageUserView.getFlag() == ManageUserAction.ADD) {
                messageHeader = "Add New User.";

                User user = userDAO.findOneByCriteria(Restrictions.eq("id", isaManageUserView.getId()));
                if (user != null) {
                    complete = false;
                    message = "Add new User failed. Cause : Duplicate UserId found in system!";

                } else {
                    isaBusinessControl.createUser(isaManageUserView);
                    onSelectUser();
                    message = "Add New User Success.";
                }
                context.execute("msgBoxSystemMessageDlg.show()");
            } else if (isaManageUserView.getFlag() == ManageUserAction.EDIT) {
                context.execute("confirmEditUserDlg.show()");
                complete = false;
            }

            context.addCallbackParam("functionComplete", complete);
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
//        userDetail = userDAO.findByCriteria(Restrictions.eq("userStatus", UserStatus.NORMAL));
        userDetail = userDAO.findAll();
        getSelectUserDetailList();
        userSize = userDetail.size();
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
//        isaManageUserView = new IsaManageUserView();
        isaManageUserView.reset();
        isaManageUserView.getRole().setId(-1);
        isaManageUserView.setFlag(ManageUserAction.ADD);

        getSelectUserDetailList();
    }


    public void onOpenEditForm() {
        System.out.println("------------------ " + id);

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            isaManageUserView = isaBusinessControl.SelectUserById(id);
            if (isaManageUserView != null) {

                isaManageUserView.setFlag(ManageUserAction.EDIT);
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
                message = "Delete User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Delete User failed. Cause : " + e.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onDeleteUserList() {
        System.out.println("-------------------------------------22 : " + selectUserDetail.length);
        for (User list : selectUserDetail) {
            System.out.println(list.getId());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            messageHeader = "Delete User.";

            if (selectUserDetail.length == 0) {
                message = "Please Select User ";
                context.execute("msgBoxSystemMessageDlg.show()");
            } else {
                context.execute("confirmDeleteUserListDlg.show()");
            }

        } catch (Exception e) {
            complete = false;

            if (e.getCause() != null) {
                message = "Delete User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Delete User failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void deleteUserList() {

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            isaBusinessControl.deleteUserList(selectUserDetail);

        } catch (Exception e) {

            messageHeader = "Delete User.";
            if (e.getCause() != null) {
                message = "Delete User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Delete User failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
        onSelectUser();
    }

    public void editUser() {
        log.debug("editUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            isaBusinessControl.editUser(isaManageUserView);
            onSelectUser();
            context.execute("manageUserDlg.hide()");
        } catch (Exception e) {
            messageHeader = "Edit User.";
            if (e.getCause() != null) {
                message = "Edit User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Edit User failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }


    }


    public void onSearchUser() {
        log.debug("onSearchUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            System.out.println(isaSearchView.toString());
            userDetail = isaBusinessControl.searchUser(isaSearchView);
            userSize = userDetail.size();

        } catch (Exception e) {
            messageHeader = "Search User.";
            if (e.getCause() != null) {
                message = "Search User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Search User failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onEditUserActive() {
        log.debug("onEditUserActive()");
        RequestContext context = RequestContext.getCurrentInstance();
        try {

//            if (selectUserDetail.length == 0) {
//                message = "Please Select User ";
//                context.execute("msgBoxSystemMessageDlg.show()");
//            } else {
//                context.execute("confirmDeleteUserListDlg.show()");
//            }

            if (active == 1) {
                isaBusinessControl.editUserActive(selectUserDetail, ManageUserActive.ACTIVE);
            } else if (active == 0) {
                isaBusinessControl.editUserActive(selectUserDetail, ManageUserActive.INACTIVE);
            }
            onSelectUser();

        } catch (Exception e) {
            if (e.getCause() != null) {
                message = "Edit UserActive failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Edit UserActive failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }

    }


    public void notLogonOver() {
        log.debug("notLogonOver()");

        List<IsaUserReportView> list=new ArrayList<IsaUserReportView>();
        StringBuilder builder = new StringBuilder();
        builder.append("No");builder.append(COMMA_DELIMITED);
        builder.append("User ID");builder.append(COMMA_DELIMITED);
        builder.append("User Name");builder.append(COMMA_DELIMITED);
        builder.append("Email Address");builder.append(COMMA_DELIMITED);
        builder.append("Bu Code");builder.append(COMMA_DELIMITED);
        builder.append("Last IP");builder.append(COMMA_DELIMITED);
        builder.append("Last Logon");builder.append(COMMA_DELIMITED);
        builder.append("Phone Ext");builder.append(COMMA_DELIMITED);
        builder.append("Phone Number");builder.append(COMMA_DELIMITED);
        builder.append("Role");builder.append(COMMA_DELIMITED);
        builder.append("Department");builder.append(COMMA_DELIMITED);
        builder.append("Division");builder.append(COMMA_DELIMITED);
        builder.append("Region");builder.append(COMMA_DELIMITED);
        builder.append("Team");builder.append(COMMA_DELIMITED);
        builder.append("Title");builder.append(COMMA_DELIMITED);
        builder.append("Zone");builder.append(COMMA_DELIMITED);
        builder.append("Active");builder.append(COMMA_DELIMITED);
        builder.append("Status");builder.append("\n");

        try{
            list=isaBusinessControl.getUserNotLogonOver(notLogonOverDay);
        }catch (Exception e){

        }
        int number=1;
        for(IsaUserReportView isaUserReportView:list){
            builder.append("'"+number+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserId()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserName()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getEmailAddress()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getBuCode()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getLastIp()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getLastLogon()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getPhoneExt()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getPhoneNumber()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getRole()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getDepartment()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getDivision()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getRegion()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getTeam()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getTitle()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getZone()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getActive()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserStatus()+"'");builder.append("\n");

            number++;
        }
        System.out.println(builder.toString());

    }


    public void userReport() {
        log.debug("userReport()");

        List<IsaUserReportView> list=new ArrayList<IsaUserReportView>();
        StringBuilder builder = new StringBuilder();
        builder.append("No");builder.append(COMMA_DELIMITED);
        builder.append("User ID");builder.append(COMMA_DELIMITED);
        builder.append("User Name");builder.append(COMMA_DELIMITED);
        builder.append("Email Address");builder.append(COMMA_DELIMITED);
        builder.append("Bu Code");builder.append(COMMA_DELIMITED);
        builder.append("Last IP");builder.append(COMMA_DELIMITED);
        builder.append("Last Logon");builder.append(COMMA_DELIMITED);
        builder.append("Phone Ext");builder.append(COMMA_DELIMITED);
        builder.append("Phone Number");builder.append(COMMA_DELIMITED);
        builder.append("Role");builder.append(COMMA_DELIMITED);
        builder.append("Department");builder.append(COMMA_DELIMITED);
        builder.append("Division");builder.append(COMMA_DELIMITED);
        builder.append("Region");builder.append(COMMA_DELIMITED);
        builder.append("Team");builder.append(COMMA_DELIMITED);
        builder.append("Title");builder.append(COMMA_DELIMITED);
        builder.append("Zone");builder.append(COMMA_DELIMITED);
        builder.append("Active");builder.append(COMMA_DELIMITED);
        builder.append("Status");builder.append("\n");

        try{
             list=isaBusinessControl.getUserReportList();
        }catch (Exception e){

        }
        int number=1;
        if(list!=null){
        for(IsaUserReportView isaUserReportView:list){
            builder.append("'"+number+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserId()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserName()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getEmailAddress()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getBuCode()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getLastIp()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getLastLogon()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getPhoneExt()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getPhoneNumber()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getRole()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getDepartment()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getDivision()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getRegion()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getTeam()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getTitle()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getZone()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getActive()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+isaUserReportView.getUserStatus()+"'");builder.append("\n");

            number++;
        }
        }
        System.out.println(builder.toString());
        ScvExport scvExport=new ScvExport();

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        scvExport.exportCSV(response,"ok",builder.toString());


        context.responseComplete();
    }


    public void uploadUserFile(){
         log.debug("uploadUserFile()");





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

    public List<UserZone> getUserZoneList() {
        return userZoneList;
    }

    public void setUserZoneList(List<UserZone> userZoneList) {
        this.userZoneList = userZoneList;
    }

    public int getUserSize() {
        return userSize;
    }

    public void setUserSize(int userSize) {
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

    public IsaManageUserView getIsaManageUserView() {
        return isaManageUserView;
    }

    public void setIsaManageUserView(IsaManageUserView isaManageUserView) {
        this.isaManageUserView = isaManageUserView;
    }

    public IsaSearchView getIsaSearchView() {
        return isaSearchView;
    }

    public void setIsaSearchView(IsaSearchView isaSearchView) {
        this.isaSearchView = isaSearchView;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getNotLogonOverDay() {
        return notLogonOverDay;
    }

    public void setNotLogonOverDay(int notLogonOverDay) {
        this.notLogonOverDay = notLogonOverDay;
    }
}
