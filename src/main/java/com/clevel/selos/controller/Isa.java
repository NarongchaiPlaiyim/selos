package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.IsaBusinessControl;
import com.clevel.selos.dao.master.*;
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
import java.io.Serializable;
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

    IsaBusinessControl isaBusinessControl;

    public Isa(){

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

    private String testString;

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    @PostConstruct
    public void onCreate(){

             onSelectUser();
    }

    public void onCreateNewUser(){

       log.debug("onCreateNewUser()");

        isaBusinessControl.createUser(createUserView);

        RequestContext.getCurrentInstance().closeDialog("createUserDialog");
    }

    public void onOpenNewUserForm(){
        createUserView=new IsaCreateUserView();
        createUserView.setId("55");

        userRoleList=roleDAO.findAll();
        userDepartmentList=userDepartmentDAO.findAll();
        userDivisionList=userDivisionDAO.findAll();
        userRegionList=userRegionDAO.findAll();
        userTeamList=userTeamDAO.findAll();
        userTitleList=userTitleDAO.findAll();
        userZoneList=userZoneDAO.findAll();
    }

    public void onSelectUser(){
        userDetail =new ArrayList<User>();
        User user=new User();
        userDetail=userDAO.findByCriteria(Restrictions.eq("userStatus", UserStatus.NORMAL));
        testString=userDetail.size()+"";
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
}
