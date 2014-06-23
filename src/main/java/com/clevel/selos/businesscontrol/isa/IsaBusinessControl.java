package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.businesscontrol.isa.csv.service.CSVService;
import com.clevel.selos.dao.audit.IsaActivityDAO;
import com.clevel.selos.dao.audit.SecurityActivityDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.model.db.audit.SecurityActivity;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.isa.IsaAuditLogView;
import com.clevel.selos.model.view.isa.IsaManageUserView;
import com.clevel.selos.model.view.isa.IsaSearchView;
import com.clevel.selos.model.view.isa.IsaUserDetailView;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class IsaBusinessControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private UserDAO userDAO;
    @Inject
    private SecurityActivityDAO securityActivityDAO;
    @Inject
    private IsaActivityDAO isaActivityDAO;
    @Inject
    public IsaBusinessControl() {

    }

    @Inject
    private RoleDAO roleDAO;
    @Inject
    private UserTeamDAO userTeamDAO;
    @Inject
    private UserDepartmentDAO userDepartmentDAO;
    @Inject
    private UserDivisionDAO userDivisionDAO;
    @Inject
    private UserRegionDAO userRegionDAO;
    @Inject
    private UserTitleDAO userTitleDAO;
    @Inject
    private UserTransform userTransform;
    @Inject
    private CSVService csvService;
    @PostConstruct
    public void onCreate() {

    }

    boolean complete = true;
    private final Locale THAI_LOCALE = new Locale("th", "TH");
    private final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.sss";

    public List<UserTeam> getUserTeamByRoleId(final int roleId){
        return userTeamDAO.findByRoleId(roleId);
    }
    public List<Role> getAllRole(){
        return Util.safetyList(roleDAO.findActiveAll());
    }
    public List<UserDepartment> getAllUserDepartment(){
        return Util.safetyList(userDepartmentDAO.findActiveAll());
    }
    public List<UserDivision> getAllUserDivision(){
        return Util.safetyList(userDivisionDAO.findActiveAll());
    }
    public List<UserRegion> getAllUserRegion(){
        return Util.safetyList(userRegionDAO.findActiveAll());
    }
    public List<UserTitle> getAllUserTitle(){
        return Util.safetyList(userTitleDAO.findActiveAll());
    }
    public List<User> getAllUser(){
        return Util.safetyList(userDAO.findByUserStatusNORMAL());
    }

    public void createUser(final IsaManageUserView isaManageUserView) throws Exception {
        log.debug("-- createUser()");
        userDAO.createNewUserByISA(userTransform.transformToModel(isaManageUserView));
    }
    public void editUser(final IsaManageUserView isaManageUserView) throws Exception {
        log.debug("-- editUser()");
        User user = null;
        user = userTransform.transformToModel(isaManageUserView);
        if(!Util.isNull(user)){
            userDAO.persist(user);
        }
    }
    public void deleteUser(final String id) throws Exception {
        log.debug("deleteUser()");
        userDAO.deleteUserByISA(id);

    }
    public void deleteUserList(final User[] users) throws Exception {
        log.debug("deleteUserList()");
        for (final User user : users) {
            userDAO.deleteUserByISA(user.getId());
        }
    }
    public List<User> searchUser(IsaSearchView isaSearchView) throws Exception {
        log.debug("searchUser()");
        return userDAO.findByISA(isaSearchView);
    }

    public void exportProcess() throws Exception {
        log.debug("-- exportProcess()");
        List<User> userList = null;
        userList = Util.safetyList(userDAO.findAll());
        final String FULL_PATH = "D:/06232014test.csv";
        if(!Util.isZero(userList.size())){
            csvService.CSVExport(FULL_PATH, userList);
        }
    }

    public void importProcess() throws Exception {
        log.debug("-- importProcess()");
        final String FULL_PATH = "D:/06232014test.csv";
        csvService.CSVImport(FULL_PATH);
    }


    public IsaManageUserView SelectUserById(final String id) throws Exception {
        log.debug("SelectUserById. (id: {})",id);
        IsaManageUserView isaManageUserView = null;
        User user = userDAO.findById(id);
        if(!Util.isNull(user)){
            isaManageUserView = userTransform.transformToISAView(user);
        }
        return isaManageUserView;
    }

    public void editUserActive(User[] users, ManageUserActive manageUserActive) throws Exception {
        log.debug("editUserActive()");
        log.debug("========================= : {}",manageUserActive.getValue());
        for (User list : users) {
            User user = userDAO.findById(list.getId());
            user.setActive(manageUserActive.getValue());
            userDAO.persist(user);
        }

    }

    public List<IsaAuditLogView> getUserMaintenanceReport(Date dateFrom,Date dateTo) throws Exception {
        log.debug("getUserMaintenanceReport()");
        List<IsaAuditLogView> list = new ArrayList<IsaAuditLogView>();

         Calendar calendar =Calendar.getInstance();
         calendar.setTime(dateTo);
         calendar.add(Calendar.DATE,1);

        List<IsaActivity> isaActivity = isaActivityDAO.findByCriteria(Restrictions.between("actionDate",dateFrom,calendar.getTime()));
        for (IsaActivity activityList : isaActivity) {
            IsaAuditLogView isaAuditLogView = new IsaAuditLogView();
            isaAuditLogView.setUserId(activityList.getUserId());
            User username = userDAO.findOneByCriteria(Restrictions.eq("id", activityList.getUserId()));
            isaAuditLogView.setUserName(username != null ? username.getUserName() : "");
            isaAuditLogView.setAction(activityList.getAction());
            isaAuditLogView.setActionDesc(activityList.getActionDesc());
            isaAuditLogView.setIpAddress(activityList.getIpAddress());
            isaAuditLogView.setActionDate(DateTimeUtil.convertDateToString(activityList.getActionDate(),THAI_LOCALE,DATE_FORMAT));
            isaAuditLogView.setResult(activityList.getActionResult().name());
            isaAuditLogView.setResultDesc(activityList.getResultDesc());

            list.add(isaAuditLogView);
        }

        return list;
    }


    public List<IsaUserDetailView> getUserNotLogonOver(int day) throws Exception {
        log.debug("getUserNotLogonOver()");

        List<IsaUserDetailView> list = new ArrayList<IsaUserDetailView>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -(day + 1));

        log.debug("{}",cal.getTime());
        List<User> users = userDAO.findByCriteria(Restrictions.le("lastLogon", cal.getTime()));
        for (User userlist : users) {
            IsaUserDetailView isaUserDetailView = new IsaUserDetailView();
            isaUserDetailView.setUserId(userlist.getId());
            isaUserDetailView.setUserName(userlist.getUserName());
            isaUserDetailView.setEmailAddress(userlist.getEmailAddress());
            isaUserDetailView.setBuCode(userlist.getBuCode());
            isaUserDetailView.setLastIp(userlist.getLastIP());
            isaUserDetailView.setLastLogon(DateTimeUtil.convertDateToString(userlist.getLastLogon(),THAI_LOCALE,DATE_FORMAT));
            isaUserDetailView.setPhoneExt(userlist.getPhoneExt());
            isaUserDetailView.setPhoneNumber(userlist.getPhoneNumber());
            isaUserDetailView.setRole(userlist.getRole()!=null && userlist.getRole().getName() != null? userlist.getRole().getName() : " ");
            isaUserDetailView.setDepartment(userlist.getDepartment()!=null && userlist.getDepartment().getName() != null ? userlist.getDepartment().getName() : " ");
            isaUserDetailView.setDivision(userlist.getDivision()!=null && userlist.getDivision().getName() != null ? userlist.getDivision().getName() : " ");
            isaUserDetailView.setRegion(userlist.getRegion()!=null && userlist.getRegion().getName() != null ? userlist.getRegion().getName() : " ");
            isaUserDetailView.setTeam(userlist.getTeam()!=null && userlist.getTeam().getName() != null ? userlist.getTeam().getName() : " ");
            isaUserDetailView.setTitle(userlist.getTitle()!=null && userlist.getTitle().getName() != null ? userlist.getTitle().getName() : " ");
            isaUserDetailView.setZone(userlist.getZone()!=null && userlist.getZone().getName() != null ? userlist.getZone().getName() : " ");
            isaUserDetailView.setActive(userlist.getActive() == 1 ? ManageUserActive.ACTIVE : ManageUserActive.INACTIVE);
            isaUserDetailView.setUserStatus(userlist.getUserStatus()!=null && userlist.getUserStatus().name() != null ? userlist.getUserStatus().name() : " ");

            list.add(isaUserDetailView);
        }

        return list;
    }

    public List<IsaAuditLogView> getViolationReport() throws Exception {
        log.debug("getViolationReport()");

        List<IsaAuditLogView> list = new ArrayList<IsaAuditLogView>();

        List<SecurityActivity> users = securityActivityDAO.findByCriteria(Restrictions.eq("actionResult", ActionResult.FAILED));
        for (SecurityActivity userlist : users) {
            IsaAuditLogView isaAuditLogView = new IsaAuditLogView();
            isaAuditLogView.setUserId(userlist.getUserId());

            User username = userDAO.findOneByCriteria(Restrictions.eq("id", userlist.getUserId()));
            isaAuditLogView.setUserName(username != null ? username.getUserName() : "");
            isaAuditLogView.setIpAddress(userlist.getIpAddress());
            isaAuditLogView.setActionDate(DateTimeUtil.convertDateToString(userlist.getActionDate(),THAI_LOCALE,DATE_FORMAT));
            isaAuditLogView.setResult(userlist.getActionResult().name());
            isaAuditLogView.setResultDesc(userlist.getResultDesc());
            list.add(isaAuditLogView);
        }

        return list;
    }


    public List<IsaUserDetailView> getUserReportList() throws Exception {
        log.debug("getUserNotLogonOver");

        List<IsaUserDetailView> list = new ArrayList<IsaUserDetailView>();

        Role role = new Role();
        role.setId(1);

        List<User> users = userDAO.findByCriteria(Restrictions.gt("role", role));
        log.debug("{}",users.size());
        for (User userlist : users) {
            IsaUserDetailView isaUserDetailView = new IsaUserDetailView();
            isaUserDetailView.setUserId(userlist.getId());
            isaUserDetailView.setUserName(userlist.getUserName());
            isaUserDetailView.setEmailAddress(userlist.getEmailAddress());
            isaUserDetailView.setBuCode(userlist.getBuCode());
            isaUserDetailView.setLastIp(userlist.getLastIP());
            isaUserDetailView.setLastLogon(DateTimeUtil.convertDateToString(userlist.getLastLogon(),THAI_LOCALE,DATE_FORMAT));
            isaUserDetailView.setPhoneExt(userlist.getPhoneExt());
            isaUserDetailView.setPhoneNumber(userlist.getPhoneNumber());
            isaUserDetailView.setRole(userlist.getRole()!=null && userlist.getRole().getName() != null? userlist.getRole().getName() : " ");
            isaUserDetailView.setDepartment(userlist.getDepartment()!=null && userlist.getDepartment().getName() != null ? userlist.getDepartment().getName() : " ");
            isaUserDetailView.setDivision(userlist.getDivision()!=null && userlist.getDivision().getName() != null ? userlist.getDivision().getName() : " ");
            isaUserDetailView.setRegion(userlist.getRegion()!=null && userlist.getRegion().getName() != null ? userlist.getRegion().getName() : " ");
            isaUserDetailView.setTeam(userlist.getTeam()!=null && userlist.getTeam().getName() != null ? userlist.getTeam().getName() : " ");
            isaUserDetailView.setTitle(userlist.getTitle()!=null && userlist.getTitle().getName() != null ? userlist.getTitle().getName() : " ");
            isaUserDetailView.setZone(userlist.getZone()!=null && userlist.getZone().getName() != null ? userlist.getZone().getName() : " ");
            isaUserDetailView.setActive(userlist.getActive() == 1 ? ManageUserActive.ACTIVE : ManageUserActive.INACTIVE);
            isaUserDetailView.setUserStatus(userlist.getUserStatus()!=null && userlist.getUserStatus().name() != null ? userlist.getUserStatus().name() : " ");

            list.add(isaUserDetailView);
        }

        return list;
    }



}
