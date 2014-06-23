package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.dao.master.RoleDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.isa.IsaManageUserView;
import com.clevel.selos.model.view.isa.IsaSearchView;
import com.clevel.selos.model.view.isa.IsaUserDetailView;
import com.clevel.selos.system.audit.IsaAuditor;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
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
    private UserDAO userDAO;
    @Inject
    private RoleDAO roleDAO;
    @Inject
    private IsaBusinessControl isaBusinessControl;
    @Inject
    private IsaAuditor isaAuditor;

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

    @PostConstruct
    public void onCreate() {
        isaManageUserView = new IsaManageUserView();
        isaSearchView = new IsaSearchView();

        isaManageUserView.reset();
        isaSearchView.reset();
//        isaSearchView.getRoleId().setId(-1);

        init();
    }

    private void init(){
        log.debug("-- init()");
        onLoadAllOfSelectOneMenu();
        onLoadUser();
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
    private void onLoadUser(){
        log.debug("-- onLoadUser()");
        userDetail = isaBusinessControl.getAllUser();
        userSize = userDetail.size();
    }

    public void onChangeRole(){
        log.debug("-- onChangeRole");
        if(!Util.isNull(isaSearchView.getRoleId().getId()) && !Util.isZero(isaSearchView.getRoleId().getId())){
            userTeamList = getUserTeamByRoleId(isaSearchView.getRoleId().getId());
        } else {
            userTeamList = getUserTeamByRoleId(isaManageUserView.getRole().getId());
        }
    }

    private List<UserTeam> getUserTeamByRoleId(final int id){
        return isaBusinessControl.getUserTeamByRoleId(id);
    }


    public void onSubmitExportCSV(){
        log.debug("-- onSubmitExportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        messageHeader = "Export to CSV";
        try {
            isaBusinessControl.exportProcess();
            message = "Success.";
        } catch (Exception e){
            message = e.getMessage();
        }
        context.execute("msgBoxSystemMessageDlg.show()");
    }

    public void onSubmitImportCSV(){
        log.debug("-- onSubmitImportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        messageHeader = "Import to model";
        try {
            isaBusinessControl.importProcess();
            message = "Success.";
        } catch (Exception e){
            message = e.getMessage();
        }
        context.execute("msgBoxSystemMessageDlg.show()");
    }


    public void onManageUserAction() {
        log.debug("onCreateNewUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        log.debug("{}",isaManageUserView);

            IsaUserDetailView auditView=getAuditDesc(isaManageUserView);

        try {
            if (modeForButton == ModeForButton.ADD) {
                messageHeader = "Add New User.";

                User user = userDAO.findOneByCriteria(Restrictions.eq("id", isaManageUserView.getId()));
                if (user != null) {
                    complete = false;
                    message = "Add new User failed. Cause : Duplicate UserId found in system!";

                    isaAuditor.add("ISA", modeForButton.name(),auditView.toString(), ActionResult.FAILED, message);
                } else {
                    isaBusinessControl.createUser(isaManageUserView);
//                    UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                    System.out.println(userDetail.getUserName());
                    isaAuditor.add("ISA", modeForButton.name(),auditView.toString(), ActionResult.SUCCESS, "");

                    onLoadUser();
                    message = "Add New User Success.";
                }
                context.execute("msgBoxSystemMessageDlg.show()");
            } else if (modeForButton == ModeForButton.EDIT) {
                context.execute("confirmEditUserDlg.show()");
                complete = false;
            }

            context.addCallbackParam("functionComplete", complete);
        } catch (ConstraintViolationException e) {
            if (e.getCause() != null) {
                message = "Edit User failed. Cause : " +e.getCause().getMessage();
            } else {
                message = "Edit User failed. Cause : " +e.getMessage();
            }

            isaAuditor.add("ISA", modeForButton.name(),auditView.toString(), ActionResult.EXCEPTION,message);

            context.execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.debug("Exception : {}", ex);
            complete = false;
            if (ex.getCause() != null) {
                message = "Add new customer failed. Cause : " + ex.getCause().getMessage();
            } else {
                message = "Add new customer failed. Cause : " + ex.getMessage();
            }

            isaAuditor.add("ISA", modeForButton.name(),auditView.toString(), ActionResult.EXCEPTION, message);
            context.execute("msgBoxSystemMessageDlg.show()");
        }

    }


//    public void getSelectUserDetailList() {
//        try {
////            userRoleList = roleDAO.findAll();
//            userDepartmentList = userDepartmentDAO.findAll();
//            userDivisionList = userDivisionDAO.findAll();
//            userRegionList = userRegionDAO.findAll();
//            userTeamList = userTeamDAO.findAll();   //
//            userTitleList = userTitleDAO.findAll();
//            userZoneList = userZoneDAO.findAll();  //--
//        } catch (Exception e) {
//            log.error("",e);
//        }
//
//    }

    public void onSubmitCreateNewUser(){
        log.debug("-- onSubmitCreateNewUser()");
        modeForButton = ModeForButton.ADD;
        isaManageUserView = new IsaManageUserView();
        onLoadUserTeam();
    }



    public void onOpenEditForm() {
        log.debug("------------------ {}",id);

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            isaManageUserView = isaBusinessControl.SelectUserById(id);
            isaManageUserView.setReadOnlyUserId(true);
            if (isaManageUserView != null) {

                //set BeforeAudit
                isaUserEditView=isaBusinessControl.SelectUserById(id);
                modeForButton = ModeForButton.EDIT;
//                getSelectUserDetailList();

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
        log.debug("------------------ ",id);
        complete = true;

        try {
            isaBusinessControl.deleteUser(id);
            isaAuditor.add("ISA",ModeForButton.DELETE.name(),"userId : "+id,ActionResult.SUCCESS,"");
            onLoadUser();
        } catch (Exception e) {
            complete = false;
            messageHeader = "Delete User.";
            if (e.getCause() != null) {
                message = "Delete User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Delete User failed. Cause : " + e.getMessage();
            }
            isaAuditor.add("ISA",ModeForButton.DELETE.name(),"userId : "+id,ActionResult.EXCEPTION,message);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onDeleteUserList() {
        log.debug("onDeleteUserList()");
        log.debug("selectUserDetail length: {}",selectUserDetail.length);
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
            StringBuilder builder=new StringBuilder("");
            for(User list:selectUserDetail){
                builder.append(list.getId()); builder.append(",");
            }

            isaBusinessControl.deleteUserList(selectUserDetail);

            isaAuditor.add("ISA",ModeForButton.DELETE.name(),"userId : "+builder,ActionResult.SUCCESS,"");

        } catch (Exception e) {

            messageHeader = "Delete User.";
            if (e.getCause() != null) {
                message = "Delete User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Delete User failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
            isaAuditor.add("ISA",ModeForButton.DELETE.name(),"userId : "+id,ActionResult.EXCEPTION,message);
        }
        onLoadUser();
    }

    public void editUser() {
        log.debug("editUser()");
        RequestContext context = RequestContext.getCurrentInstance();

        IsaUserDetailView auditAfter=getAuditDesc(isaManageUserView);
        IsaUserDetailView auditBefore=getAuditDesc(isaUserEditView);
        try {


            isaBusinessControl.editUser(isaManageUserView);

            isaAuditor.add("ISA",ModeForButton.EDIT.name(),"before : "+auditBefore.toString()+ ", after : "+auditAfter.toString(),ActionResult.SUCCESS,"");

            onLoadUser();
            context.execute("manageUserDlg.hide()");
        } catch (Exception e) {
            messageHeader = "Edit User.";
            if (e.getCause() != null) {
                message = "Edit User failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Edit User failed. Cause : " + e.getMessage();
            }
            isaAuditor.add("ISA",ModeForButton.EDIT.name(),auditAfter.toString(),ActionResult.EXCEPTION,message);
            context.execute("msgBoxSystemMessageDlg.show()");
        }

    }



    public void onSubmitSearchUser() {
        log.debug("onSubmitSearchUser()");
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            log.debug("{}",isaSearchView);
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
            StringBuilder builder=new StringBuilder("");
            for(User list:selectUserDetail){
                builder.append(list.getId()); builder.append(",");
            }

            if (active == 1) {
                isaBusinessControl.editUserActive(selectUserDetail, ManageUserActive.ACTIVE);
                isaAuditor.add("ISA",ModeForButton.EDIT.name(),"userId : "+builder +" To "+ManageUserActive.ACTIVE,ActionResult.SUCCESS,"");
            } else if (active == 0) {
                isaBusinessControl.editUserActive(selectUserDetail, ManageUserActive.INACTIVE);
                isaAuditor.add("ISA",ModeForButton.EDIT.name(),"userId : "+builder +" To "+ManageUserActive.INACTIVE,ActionResult.SUCCESS,"");
            }
            onLoadUser();

        } catch (Exception e) {
            if (e.getCause() != null) {
                message = "Edit UserActive failed. Cause : " + e.getCause().getMessage();
            } else {
                message = "Edit UserActive failed. Cause : " + e.getMessage();
            }
            context.execute("msgBoxSystemMessageDlg.show()");
            isaAuditor.add("ISA",ModeForButton.EDIT.name(),"userId : "+id,ActionResult.EXCEPTION,message);
        }

    }


    public IsaUserDetailView getAuditDesc(IsaManageUserView isaManageUserView){

        IsaUserDetailView isaUserDetailView=new IsaUserDetailView();
        isaUserDetailView.setUserId(isaManageUserView.getId());
        isaUserDetailView.setUserName(isaManageUserView.getUsername());
        isaUserDetailView.setBuCode(isaManageUserView.getBuCode());
        isaUserDetailView.setEmailAddress(isaManageUserView.getEmailAddress());
        isaUserDetailView.setPhoneExt(isaManageUserView.getPhoneExt());
        isaUserDetailView.setPhoneNumber(isaManageUserView.getPhoneNumber());             //TODO plz add method for query
//        isaUserDetailView.setRole(roleDAO.findById(isaManageUserView.getRole().getId()).getName());
//        isaUserDetailView.setDepartment(userDepartmentDAO.findById(isaManageUserView.getUserDepartment().getId()).getName());
//        isaUserDetailView.setDivision(userDivisionDAO.findById(isaManageUserView.getUserDivision().getId()).getName());
//        isaUserDetailView.setRegion(userRegionDAO.findById(isaManageUserView.getUserRegion().getId()).getName());
//        //isaUserDetailView.setTeam(userTeamDAO.findById(isaManageUserView.getUserTeam().getId()).getName());
//        isaUserDetailView.setTitle(userTitleDAO.findById(isaManageUserView.getUserTitle().getId()).getName());
//        isaUserDetailView.setZone(userZoneDAO.findById(isaManageUserView.getUserZone().getId()).getName());
        isaUserDetailView.setActive(isaManageUserView.getActive()==1?ManageUserActive.ACTIVE:ManageUserActive.INACTIVE);

        return  isaUserDetailView;
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
