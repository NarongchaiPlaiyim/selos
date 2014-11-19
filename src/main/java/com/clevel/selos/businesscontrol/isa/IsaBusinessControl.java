package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.audit.IsaActivityDAO;
import com.clevel.selos.dao.audit.SecurityActivityDAO;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.CommandType;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.audit.IsaActivity;
import com.clevel.selos.model.db.audit.SecurityActivity;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.report.ISAViewReport;
import com.clevel.selos.model.view.isa.*;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.IsaAuditor;
import com.clevel.selos.transform.UserTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Stateless
//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
    @Inject
    private IsaAuditor isaAuditor;
    @Inject
    @Config(name = "isa.path.temp")
    private String path;
    @Inject
    @Config(name = "isa.path.export.fileName")
    private String exportFileName;
    @Inject
    @Config(name = "isa.path.import.fileName")
    private String importFileName;
    @Inject
    @Config(name = "isa.path.result.fileName")
    private String resultFileName;
    private final String CSV = ".csv";
    @Inject
    private STPExecutor stpExecutor;
    private User user;
    @Inject
    public IsaBusinessControl() {
    }
    @PostConstruct
    public void onCreate() {
        onLoadUserId();
    }

    boolean complete = true;
    private final Locale THAI_LOCALE = new Locale("th", "TH");
    private final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss.sss";

    private void onLoadUserId(){
        log.debug("-- onLoadUser()");
        user = getCurrentUser();
    }
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
    public void createUser(final IsaManageUserView isaManageUserView, final User user) throws Exception {
        log.debug("-- createUser()");
        userDAO.createNewUserByISA(userTransform.transformToNewModel(isaManageUserView, user));
    }
    public void editUser(final IsaManageUserView isaManageUserView, final User user) throws Exception {
        log.debug("-- editUser()");
        User model = null;
        model = userTransform.transformToModel(isaManageUserView, user);
        if(!Util.isNull(model)){
            userDAO.persist(model);
        }
    }
    public void editUserAfterDelete(final IsaManageUserView isaManageUserView, final User user) throws Exception {
        log.debug("-- editUser()");
        User model = null;
        model = userTransform.transformToModelAfterDelete(isaManageUserView, user);
        if(!Util.isNull(model)){
            userDAO.persist(model);
        }
    }
    public void editUserFromDelete(final IsaManageUserView isaManageUserView, final User user) throws Exception {
        log.debug("-- editUser()");
        User model = null;
        model = userTransform.transformToModelModify(isaManageUserView, user);
        if(!Util.isNull(model)){
            userDAO.persist(model);
        }
    }
    public void modifyAfterDeleteById(final IsaManageUserView isaManageUserView, final User user) throws Exception {
        log.debug("-- IsaManageUserView : {}", isaManageUserView);
        userDAO.createNewUserByISA(userTransform.transformToNewModel(isaManageUserView, user));
    }
    public void deleteUserById(final String id) throws Exception {
        log.debug("-- deleteUser(id : {})", id);
        userDAO.deleteUserByISA(id, getCurrentUser());
    }
    public void deleteUserList(final User[] users) throws Exception {
        log.debug("deleteUserList()");
        for (final User user : users) {
            userDAO.deleteUserByISA(user.getId(), getCurrentUser());
        }
    }
    public List<User> getUserBySearch(final IsaSearchView isaSearchView) throws Exception {
        log.debug("-- getUserBySearch()");
        return userDAO.findByISA(isaSearchView);
    }
    public boolean isExistId(final String id) throws Exception{
        return userDAO.isExistId(id);
    }
    public boolean isExistUserName(final String userId) throws Exception{
        return userDAO.isExistUserName(userId);
    }

    public boolean isExistIdAndActivityAndStatus(final String id) throws Exception{
        log.debug("--On isExistIdAndActivityAndStatus.");
        user = userDAO.findById(id);
        log.debug("-- user. [{}]",user);
            if ((UserStatus.MARK_AS_DELETED).equals(user.getUserStatus()) && user.getActive() == ManageUserActive.INACTIVE.getValue()){
                return true;
            }
        return false;
    }

    public String getNewData(final String id){
        String result = "";
        User user = userDAO.findById(id);
        if(!Util.isNull(user)){
            result = user.toStringForAudit();
        }
        return result;
    }

    public String getOldData(final String id){
        return getNewData(id);
    }

    public User getUser(final String id){
        return userDAO.findById(id);
    }

    public String exportProcess() throws Exception {
        log.debug("-- exportProcess()");
        List<User> userList = null;
        userList = Util.safetyList(userDAO.findAll());
        String fullPath = null;
        StringBuilder stringBuilder = null;
        if(!Util.isZero(userList.size())){
            stringBuilder = new StringBuilder().append(path).append(exportFileName).append(Util.getFileNameForISA()).append(CSV);
            fullPath = stringBuilder.toString();
            csvService.CSVExport(fullPath, userList);
        }
        return fullPath;
    }

    public List<DownloadView> getFileUploaded(){
        List<DownloadView> downloadViewList = null;
        File file = null;
        File[] fileList = null;
        try {
            file = new File(path);
            fileList = file.listFiles();
            downloadViewList = new ArrayList<DownloadView>();
            for(final File fileFromList : fileList){
                if(fileFromList.getName().indexOf(resultFileName) > -1){
                    downloadViewList.add(new DownloadView(DateTimeUtil.convertStringToDate(fileFromList.getName().substring(fileFromList.getName().indexOf(resultFileName) + resultFileName.length(), fileFromList.getName().indexOf(CSV))), fileFromList.getAbsolutePath(), fileFromList.getName()));
                }
            }
        } catch (Exception e) {
            log.debug("-- ex. {}", e);
        }
        return downloadViewList;
    }

    public DownloadView importProcess(final InputStream inputStream) throws Exception {
        log.debug("-- importProcess()");
        List<CSVModel> csvModelList = Util.safetyList(csvService.CSVImport(inputStream));
        List<ResultModel> resultModelList = null;
        ResultModel resultModel = null;
        StringBuilder stringBuilder = null;
        DownloadView downloadModel = null;
        if(!Util.isZero(csvModelList.size())){
            resultModelList = new ArrayList<ResultModel>();
            onLoadUserId();
            for(final CSVModel csv : csvModelList){
                final String command = csv.getCommandType();
//                log.debug("--command. [{}]",command);
                log.debug("csv : {}",csv.getUserId());

                User userCSV = getUserByCSV(csv.getUserId());
                if(CommandType.CREATE.equals(command) && Util.isNull(userCSV)){
                    resultModel = executeInsert(csv, CommandType.CREATE);
                } else if(CommandType.UPDATE.equals(command)){
                    resultModel = executeUpdate(csv, CommandType.UPDATE);
                } else if(CommandType.DELETE.equals(command)){
                    resultModel = executeDelete(csv, CommandType.DELETE);
                } else {
                    resultModel = new ResultModel();
                    resultModel.setCommand(command);
                    resultModel.setId(csv.getUserId());
                    resultModel.setResult(ActionResult.FAILED.toString());
                    resultModel.setDetail("Command not found");
                }
                resultModelList.add(resultModel);
            }

            if(!Util.isZero(resultModelList.size())){
                final String fileName = new StringBuilder().append(resultFileName).append(Util.getFileNameForISA()).append(CSV).toString();
                stringBuilder = new StringBuilder().append(path).append(fileName);
                final String fullPath = stringBuilder.toString();
                csvService.CSVExport(fullPath, resultModelList, null);
                downloadModel = new DownloadView(DateTime.now().toDate(), fullPath, fileName);
            }
        }
        return downloadModel;
    }

    private ResultModel executeInsert(final CSVModel csvModel, final CommandType commandType){
        String result = null;
        ResultModel resultModel = null;
        try {
            result = csvModel.valid(commandType);
            resultModel = new ResultModel();
            resultModel.setCommand(commandType.toString());
            resultModel.setId(csvModel.getUserId());
            if(Util.isZero(result.length())){
                result = stpExecutor.createFromCSV(csvModel, getCurrentUser());
                if(ActionResult.SUCCESS.name().equalsIgnoreCase(result)){
                    resultModel.setResult(ActionResult.SUCCESS.toString());
//                    user = null;
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(),  ActionResult.SUCCESS, null, user, "", getNewData(csvModel.getUserId()));
                } else {
                    resultModel.setResult(ActionResult.FAILED.toString());
                    resultModel.setDetail(result);
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.FAILED, result, user, "", "");
                }
            } else {
                resultModel.setResult(ActionResult.FAILED.toString());
                resultModel.setDetail(result);
            }
        } catch(Exception e){
            if (e.getCause() != null) {
                result = e.getCause().getMessage();
            } else {
                result = e.getMessage();
            }
            resultModel.setResult(ActionResult.EXCEPTION.toString());
            resultModel.setDetail(result);
            isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.EXCEPTION, result, user, "", "");
        }
        return resultModel;
    }
    private ResultModel executeUpdate(final CSVModel csvModel, final CommandType commandType){
        String result = null;
        ResultModel resultModel = null;
        try {
            result = csvModel.valid(commandType);
            resultModel = new ResultModel();
            resultModel.setCommand(commandType.toString());
            resultModel.setId(csvModel.getUserId());
            if(Util.isZero(result.length())){
                String oldData = getOldData(csvModel.getUserId());
                result = stpExecutor.updateFromCSV(csvModel, getCurrentUser());
                if(ActionResult.SUCCESS.name().equalsIgnoreCase(result)){
                    resultModel.setResult(ActionResult.SUCCESS.toString());
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(),  ActionResult.SUCCESS, null, user, oldData, getNewData(csvModel.getUserId()));
                } else {
                    resultModel.setResult(ActionResult.FAILED.toString());
                    resultModel.setDetail(result);
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.FAILED, result, user, "", "");
                }
            } else {
                resultModel.setResult(ActionResult.FAILED.toString());
                resultModel.setDetail(result);
            }
        } catch(Exception e){
            if (e.getCause() != null) {
                result = e.getCause().getMessage();
            } else {
                result = e.getMessage();
            }
            resultModel.setResult(ActionResult.EXCEPTION.toString());
            resultModel.setDetail(result);
            isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.EXCEPTION, result, user, "", "");
        }
        return resultModel;
    }
    private ResultModel executeDelete(final CSVModel csvModel, final CommandType commandType){
        String result = null;
        ResultModel resultModel = null;
        try {
            result = csvModel.valid(commandType);
            resultModel = new ResultModel();
            resultModel.setCommand(commandType.toString());
            resultModel.setId(csvModel.getUserId());
            if(Util.isZero(result.length())){
                String oldData = getOldData(csvModel.getUserId());
                result = stpExecutor.deleteFromCSV(csvModel, getCurrentUser());
                if(ActionResult.SUCCESS.name().equalsIgnoreCase(result)){
                    resultModel.setResult(ActionResult.SUCCESS.toString());
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(),  ActionResult.SUCCESS, null, user, oldData, getNewData(csvModel.getUserId()));
                } else {
                    resultModel.setResult(ActionResult.FAILED.toString());
                    resultModel.setDetail(result);
                    isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.FAILED, result, user, "", "");
                }
            } else {
                resultModel.setResult(ActionResult.FAILED.toString());
                resultModel.setDetail(result);
            }
        } catch(Exception e){
            if (e.getCause() != null) {
                result = e.getCause().getMessage();
            } else {
                result = e.getMessage();
            }
            resultModel.setResult(ActionResult.EXCEPTION.toString());
            resultModel.setDetail(result);
            isaAuditor.audit(user.getId(), commandType.name(), csvModel.toStringForAudit(), ActionResult.EXCEPTION, result, user, "", "");
        }
        return resultModel;
    }


    public IsaManageUserView getUserById(final String id) throws Exception {
        log.debug("-- getUserById(id: {})",id);
        IsaManageUserView isaManageUserView = null;
        User user = userDAO.findById(id);
        if(!Util.isNull(user)){
            isaManageUserView = userTransform.transformToISAView(user);
        }
        log.debug("-- view {}", isaManageUserView.toString());
        return isaManageUserView;
    }

    public void editUserActive(final User model, final ManageUserActive manageUserActive) throws Exception {
        log.debug("-- editUserActive()");
        if(!Util.isNull(model)){
            userDAO.updateActiveOrInactive(model, manageUserActive.getValue(), getCurrentUser());
        }

    }

    public List<ISAViewReport> getUserProFileByUser() throws SQLException {
        log.debug("--getUserProFile by CSVService.");
        ResultSet rs = stpExecutor.getUserProfileByUserMaster();
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();

        try{
            if (!Util.isNull(rs)){
                while (rs.next()){
                    ISAViewReport viewReport = new ISAViewReport();
                    viewReport.setUserId(rs.getString("USER_ID"));
                    viewReport.setUserName(rs.getString("USER_NAME"));
                    viewReport.setActive(rs.getString("ACTIVE"));
                    viewReport.setRoleId(rs.getString("ROLE_NAME"));
                    viewReport.setTeam(rs.getString("TEAM_NAME"));
                    viewReport.setDepartment(rs.getString("DEPARTMENT"));
                    viewReport.setDivision(rs.getString("DIVISION"));
                    viewReport.setRegion(rs.getString("REGION"));
                    viewReport.setTitle(rs.getString("TITLE"));
                    viewReport.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    viewReport.setLastLogOn(rs.getTimestamp("LAST_SING_ON_DATE"));
                    viewReportList.add(viewReport);
                }
            }
        } catch (Exception e){
            log.debug("getUserProFile SQLException : ",e) ;
        }

        return viewReportList;
    }

    public List<ISAViewReport> getUserProFileByISA(final Map<String, Object> map) throws SQLException {
        log.debug("--getUserProFile by CSVService.");
        ResultSet rs = stpExecutor.getActivity(map);
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();

        try{
            if (!Util.isNull(rs)){
                while (rs.next()){
                    ISAViewReport viewReport = new ISAViewReport();
                    viewReport.setAdminTask(rs.getString("ADMIN_TASK"));
                    viewReport.setEmpID(rs.getString("EMP_ID"));
                    viewReport.setEmpName(rs.getString("EMP_NAME"));
                    viewReport.setOldData(rs.getString("OLD_DATA"));
                    viewReport.setNewData(rs.getString("NEW_DATA"));
                    viewReport.setModifyDate(rs.getTimestamp("MODIFY_DATE"));
                    viewReport.setModifyBy(rs.getString("MODIFY_BY"));
                    viewReport.setAdminName(rs.getString("ADMIN_NAME"));
                    viewReportList.add(viewReport);
                }
            }
        } catch (Exception e){
            log.debug("getUserProFile SQLException : ",e) ;
        }

        return viewReportList;
    }

    public List<ISAViewReport> getUserAccessMatrix(final int roleId) throws SQLException{
        log.debug("--getUserAccessMatrix by CSVSrevice");
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
        ResultSet rs = stpExecutor.getMatrix(roleId);

        try {
            if (!Util.isNull(rs)){
                while (rs.next()){
                    ISAViewReport isaViewReport = new ISAViewReport();
                    isaViewReport.setRoleId(rs.getString("ROLE_ID"));
                    isaViewReport.setRoleName(rs.getString("ROLE_NAME"));
                    isaViewReport.setScreenId(rs.getString("SCREEN_ID"));
                    isaViewReport.setScreenName(rs.getString("SCREEN_NAME"));
                    viewReportList.add(isaViewReport);
                }
            }
        } catch (Exception e){
            log.debug("getUserAccessMatrix SQLException : ",e);
        }

        return viewReportList;
    }

    private User getUserByCSV(String userId){
        return userDAO.findById(userId);
    }
}
