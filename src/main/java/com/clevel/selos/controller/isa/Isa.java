package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.isa.IsaManageUserView;
import com.clevel.selos.model.view.isa.IsaSearchView;
import com.clevel.selos.model.view.isa.IsaUserDetailView;
import com.clevel.selos.system.audit.IsaAuditor;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "isa")
public class Isa implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private IsaBusinessControl isaBusinessControl;
    @Inject
    private IsaAuditor isaAuditor;
    private User[] selectUserDetail;
    private List<User> userList;
    private List<Role> userRoleList;
    private List<UserDepartment> userDepartmentList;
    private List<UserDivision> userDivisionList;
    private List<UserRegion> userRegionList;
    private List<UserTeam> userTeamList;
    private List<UserTitle> userTitleList;
    private List<UserZone> userZoneList;
    private IsaManageUserView isaManageUserView;
    private IsaManageUserView isaUserEditView;
    private IsaSearchView isaSearchView;
    private int userSize;
    private String id;
    private int active;
    private boolean readUserId;
    //dialogMessage
    private String messageHeader;
    private String message;
    
    private enum ModeForButton {ADD,EDIT,DELETE};
    private ModeForButton modeForButton;
    private boolean complete;
    private enum Result{Success};
    private String userId;
    private User user;
    public Isa() {

    }

    @PostConstruct
    public void onCreate() {
        isaManageUserView = new IsaManageUserView();
        isaSearchView = new IsaSearchView();
        init();
    }

    private void init(){
        log.debug("-- init()");
        onLoadAllOfSelectOneMenu();
        onLoadUser();
        onLoadAllUser();
    }

    private void onLoadAllOfSelectOneMenu(){
        log.debug("-- onLoadAllOfSelectOneMenu");
        onLoadRole();
        onLoadUserTeam();
        onLoadUserDepartment();
        onLoadUserDivision();
        onLoadUserRegion();
        onLoadUserTitle();
    }

    private void onLoadUser(){
        log.debug("-- onLoadUser()");
        HttpSession session = FacesUtil.getSession(false);
        user = (User) session.getAttribute("user");
        if(!Util.isNull(user)){
            onLoadUserId(user);
        }
    }

    private void onLoadUserId(final User user){
        log.debug("-- onLoadUserId(id : {})", user.getId());
        userId = user.getId();
    }

    private void onLoadRole(){
        log.debug("-- onLoadRole()");
        userRoleList = isaBusinessControl.getAllRole();
    }
    private void onLoadUserTeam(){
        log.debug("-- onLoadUserTeam()");
        userTeamList = new ArrayList<UserTeam>();
    }
    private void onLoadUserDepartment(){
        log.debug("-- onLoadUserDepartment()");
        userDepartmentList = isaBusinessControl.getAllUserDepartment();
    }
    private void onLoadUserDivision(){
        log.debug("-- onLoadUserDivision()");
        userDivisionList = isaBusinessControl.getAllUserDivision();
    }
    private void onLoadUserRegion(){
        log.debug("-- onLoadUserRegion()");
        userRegionList = isaBusinessControl.getAllUserRegion();
    }
    private void onLoadUserTitle(){
        log.debug("-- onLoadUserTitle()");
        userTitleList = isaBusinessControl.getAllUserTitle();
    }
    private void onLoadAllUser(){
        log.debug("-- onLoadAllUser()");
        userList = isaBusinessControl.getAllUser();
        userSize = userList.size();
    }

    public void onChangeRole(){
        log.debug("-- onChangeRole()");
        if(!Util.isNull(isaSearchView.getRoleId().getId()) && !Util.isZero(isaSearchView.getRoleId().getId())){
            userTeamList = getUserTeamByRoleId(isaSearchView.getRoleId().getId());
        } else {
            userTeamList = getUserTeamByRoleId(isaManageUserView.getRole().getId());
            isaManageUserView.setUserTeam(new UserTeam());
        }
    }

    private List<UserTeam> getUserTeamByRoleId(final int id){
        return isaBusinessControl.getUserTeamByRoleId(id);
    }

    public void onSubmitSaveDialog(){
        log.debug("-- onSubmitSaveDialog()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;

        String oldData = user.toStringForAudit();
        String newData = isaBusinessControl.getNewData(user.getId());
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if(!Util.isNull(isaManageUserView)){
                log.debug("-- Mode : {}", modeForButton);
//                IsaUserDetailView isaUserDetailView = isaBusinessControl.mappingToAudit(isaManageUserView);
                if(ModeForButton.ADD == modeForButton){
                    messageHeader = "Add New User.";
                    if(!isaBusinessControl.isExistId(isaManageUserView.getId())){
                        isaBusinessControl.createUser(isaManageUserView, user);
                        message = Result.Success.toString();
                        isaAuditor.audit(isaManageUserView.getId(), modeForButton.name(), isaManageUserView.toStringForAudit(),  ActionResult.SUCCESS, null, user, "", isaBusinessControl.getNewData(isaManageUserView.getId()));
                    } else if (isaBusinessControl.isExistIdAndActivityAndStatus(isaManageUserView.getId())){
                        log.debug("--Update user.");
                        isaBusinessControl.editUserAfterDelete(isaManageUserView, user);
                        String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change status to Edit").append(" By ").append(this.user.getId()).toString();
                        isaAuditor.audit(id , ModeForButton.EDIT.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                        message = Result.Success.toString();
                    } else {
                        message = "User ID already exists";
                        isaAuditor.audit(isaManageUserView.getId(), modeForButton.name(), isaManageUserView.toStringForAudit(),  ActionResult.FAILED, message, user, "", "");
                    }
                } else if(ModeForButton.EDIT == modeForButton) {
                    messageHeader = "Edit User.";
                    isaBusinessControl.editUser(isaManageUserView, user);
                    String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change status to Edit").append(" By ").append(this.user.getId()).toString();
                    isaAuditor.audit(id , ModeForButton.EDIT.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                    message = Result.Success.toString();
                }
                context.execute("manageUserDlg.hide()");
                onLoadAllUser();
                context.execute("msgBoxSystemMessageDlg.show()");

            }
        } catch (Exception e){
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSubmitCreateNewUser(){
        log.debug("-- onSubmitCreateNewUser()");
        modeForButton = ModeForButton.ADD;
        isaManageUserView = new IsaManageUserView();
        onLoadUserTeam();
    }



    public void onClickEdit() {
        log.debug("-- onClickEdit()");
        modeForButton = ModeForButton.EDIT;
        RequestContext context = RequestContext.getCurrentInstance();
        messageHeader = "Edit Form.";
        try {
            isaManageUserView = isaBusinessControl.getUserById(id);
            if (!Util.isNull(isaManageUserView)){
                isaManageUserView.setReadOnlyUserId(true);
                if(!Util.isNull(isaManageUserView.getUserTeam())){
                    userTeamList.clear();
                    userTeamList = isaBusinessControl.getUserTeamByRoleId(isaManageUserView.getRole().getId());
                }
                isaUserEditView = isaManageUserView;
            } else {
                message = id + " not found!";
            }
        } catch (Exception e) {
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onClickDelete() {
        log.debug("-- onClickDelete()");
        complete = true;
        RequestContext context = RequestContext.getCurrentInstance();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            messageHeader = "Delete User.";
            if (!Util.isNull(id)){
                String oldData = isaBusinessControl.getOldData(id);
                isaBusinessControl.deleteUserById(id);
                String newData = isaBusinessControl.getNewData(id);
                String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change status to ").append(UserStatus.MARK_AS_DELETED).append(" By ").append(this.user.getId()).toString();
                isaAuditor.audit(id , ModeForButton.DELETE.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                message = Result.Success.toString();
                log.debug("messageHeader [{}] message [{}]",messageHeader,message);
            }
            onLoadAllUser();
            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception e) {
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            isaAuditor.audit(user.getId(), ModeForButton.DELETE.name(), isaManageUserView.toStringForAudit(),  ActionResult.EXCEPTION, message, user, "", "");

            context.execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onDeleteUserList() {
        log.debug("-- onDeleteUserList()");
        RequestContext context = RequestContext.getCurrentInstance();
        StringBuilder stringBuilder = null;
        try {
            messageHeader = "Delete User.";
            if (selectUserDetail.length != 0) {
                for ( final User user : selectUserDetail){
                    String oldData = user.toStringForAudit();
                    isaBusinessControl.deleteUserById(user.getId());
                    String newData = isaBusinessControl.getNewData(user.getId());
                    stringBuilder = new StringBuilder();
                    String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change status to ").append(UserStatus.MARK_AS_DELETED).append(" By ").append(this.user.getId()).toString();
                    isaAuditor.audit(user.getId() , ModeForButton.DELETE.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                }
                message = Result.Success.toString();
            } else {
                message = "Please Select User ";
            }
            onLoadAllUser();
            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception e) {
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            isaAuditor.audit(user.getId(), ModeForButton.DELETE.name(), isaManageUserView.toStringForAudit(),  ActionResult.EXCEPTION, message, user, "", "");
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSubmitSearchUser() {
        log.debug("-- onSubmitSearchUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        messageHeader = "Search User.";
        try {
            if(!Util.isNull(isaSearchView)){
                userList = Util.safetyList(isaBusinessControl.getUserBySearch(isaSearchView));
                userSize = userList.size();
                message = Result.Success.toString();
                context.execute("msgBoxSystemMessageDlg.show()");
            }
        } catch (Exception e) {
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        }
    }
    public void onEditUserActive() {
        log.debug("-- onEditUserActive()");
        RequestContext context = RequestContext.getCurrentInstance();
        StringBuilder stringBuilder = null;
        if (active == 1) {
            messageHeader = "Active";
        } else if (active == 0) {
            messageHeader = "Inactive";
        }
        try {
            log.debug("-- {} Selected", selectUserDetail.length);
            if(!Util.isNull(selectUserDetail) && !Util.isZero(selectUserDetail.length)){
                if (active == 1) {
                    for(final User user : selectUserDetail){
                        if(ManageUserActive.ACTIVE.getValue() != user.getActive()){
                            String oldData = user.toStringForAudit();
                            isaBusinessControl.editUserActive(user, ManageUserActive.ACTIVE);
                            String newData = isaBusinessControl.getNewData(user.getId());
                            stringBuilder=new StringBuilder();
                            String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change active to ").append(ManageUserActive.ACTIVE.name()).append(" By ").append(this.user.getId()).toString();
                            isaAuditor.audit(user.getId() , ModeForButton.EDIT.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                        }
                    }
                    message = Result.Success.toString();
                } else if (active == 0) {
                    for(final User user : selectUserDetail){
                        if(ManageUserActive.INACTIVE.getValue() != user.getActive()){
                            String oldData = user.toStringForAudit();
                            isaBusinessControl.editUserActive(user, ManageUserActive.INACTIVE);
                            String newData = isaBusinessControl.getNewData(user.getId());
                            stringBuilder=new StringBuilder();
                            String actionDetail = stringBuilder.append("ID ").append(user.getId()).append(" change active to ").append(ManageUserActive.INACTIVE.name()).append(" By ").append(this.user.getId()).toString();
                            isaAuditor.audit(user.getId() , ModeForButton.EDIT.name(), actionDetail,  ActionResult.SUCCESS, null, this.user, oldData, newData);
                        }
                    }
                    message = Result.Success.toString();
                }
                onLoadAllUser();
            } else {
                message = "Please select user";
            }
            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception e) {
            complete = false;
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
            isaAuditor.audit(user.getId(), ModeForButton.EDIT.name(), isaManageUserView.toStringForAudit(),  ActionResult.EXCEPTION, message, user, "", "");
        }
    }

    public User[] getSelectUserDetail() {
        return selectUserDetail;
    }

    public void setSelectUserDetail(User[] selectUserDetail) {
        this.selectUserDetail = selectUserDetail;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
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

    public IsaManageUserView getIsaUserEditView() {
        return isaUserEditView;
    }

    public void setIsaUserEditView(IsaManageUserView isaUserEditView) {
        this.isaUserEditView = isaUserEditView;
    }

    public boolean isReadUserId() {
        return readUserId;
    }

    public void setReadUserId(boolean readUserId) {
        this.readUserId = readUserId;
    }
}
