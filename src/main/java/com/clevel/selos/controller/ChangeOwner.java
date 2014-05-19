package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ChangeOwnerControl;
import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.master.UserTeamDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.ChangeOwnerView;
import com.clevel.selos.model.view.PERoster;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 3/20/14
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "change")
@ViewScoped
public class ChangeOwner implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    UserDAO userDAO;
    @Inject
    PEDBExecute pedbExecute;
    @Inject
    ActionDAO actionDAO;

    @Inject
    UserTeamDAO userTeamDAO;

    @Inject
    InboxControl inboxControl;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    StepDAO stepDAO;

    @Inject
    HeaderControl headerControl;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public PEDBExecute getPedbExecute() {
        return pedbExecute;
    }

    public void setPedbExecute(PEDBExecute pedbExecute) {
        this.pedbExecute = pedbExecute;
    }

    public ActionDAO getActionDAO() {
        return actionDAO;
    }

    public void setActionDAO(ActionDAO actionDAO) {
        this.actionDAO = actionDAO;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setRolSet(List<ChangeOwnerView> rolSet) {
        this.rolSet = rolSet;
    }

    public String getChangeOwnerQueryList() {
        return changeOwnerQueryList;
    }

    public void setChangeOwnerQueryList(String changeOwnerQueryList) {
        this.changeOwnerQueryList = changeOwnerQueryList;
    }

    public String getRosterName() {
        return rosterName;
    }

    public void setRosterName(String rosterName) {
        this.rosterName = rosterName;
    }

    public int getSelectedTeamId() {
        return selectedTeamId;
    }

    public void setSelectedTeamId(int selectedTeamId) {
        this.selectedTeamId = selectedTeamId;
    }

    public Map.Entry getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry entry) {
        this.entry = entry;
    }

    public String getPeRosterName() {
        return peRosterName;
    }

    public void setPeRosterName(String peRosterName) {
        this.peRosterName = peRosterName;
    }

    public BPMInterfaceImpl getBpmInterfaceImpl() {
        return bpmInterfaceImpl;
    }

    public void setBpmInterfaceImpl(BPMInterfaceImpl bpmInterfaceImpl) {
        this.bpmInterfaceImpl = bpmInterfaceImpl;
    }

    UserDetail userDetail;
    private int teamId;
   // private Set<ChangeOwnerView> rolSet = null;
    private List<ChangeOwnerView> rolSet = null;
    List<PERoster> changeOwerViewList = null;
    String  changeOwnerQueryList = null;
    private List userList1 = null;
    List<ChangeOwnerView> teamTypeName;
    private String selectTeam;
    private String selectRole;
    private String selectuser;
    List<String> userNamesForChangeOwnerList = null;
    private String selectUserChangeOwner;
    private String remark;
    List<ChangeOwnerView>teamNamesForChangeOwerTo =null;
    private Map<String, Boolean> checked = new HashMap<String, Boolean>();
    private boolean disableReassign = true;
    String rosterName = null;
    String roleName;

    public boolean isDisableReassign() {
        return disableReassign;
    }

    public void setDisableReassign(boolean disableReassign) {
        this.disableReassign = disableReassign;
    }

    private PERoster rosterViewSelectItem;

    public PERoster getRosterViewSelectItem() {
        return rosterViewSelectItem;
    }

    public void setRosterViewSelectItem(PERoster rosterViewSelectItem) {
        this.rosterViewSelectItem = rosterViewSelectItem;
    }

    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    String roleNumber;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String id;
    private String user;
    int selectedTeamId;
    Map.Entry entry;
    @Inject
    @Config(name = "interface.pe.rosterName")
    String peRosterName;
    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

    @Inject
    ChangeOwnerControl changeOwnerControl;

    List<User> usersIdNameList1 = new ArrayList<User>();

    List<User> usersIdNameList = new ArrayList<User>();

    public List<User> getUsersIdNameList1() {
        return usersIdNameList1;
    }

    public void setUsersIdNameList1(List<User> usersIdNameList1) {
        this.usersIdNameList1 = usersIdNameList1;
    }

    public List<User> getUsersIdNameList() {
        return usersIdNameList;
    }

    public void setUsersIdNameList(List<User> usersIdNameList) {
        this.usersIdNameList = usersIdNameList;
    }

    public List<ChangeOwnerView> getTeamTypeName() {
        return teamTypeName;
    }

    public void setTeamTypeName(List<ChangeOwnerView> teamTypeName) {
        this.teamTypeName = teamTypeName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getSelectUserChangeOwner() {
        return selectUserChangeOwner;
    }
    public void setSelectUserChangeOwner(String selectUserChangeOwner) {
        this.selectUserChangeOwner = selectUserChangeOwner;
    }
    public List<String> getUserNamesForChangeOwnerList() {
        return userNamesForChangeOwnerList;
    }
    public void setUserNamesForChangeOwnerList(List<String> userNamesForChangeOwnerList) {
        this.userNamesForChangeOwnerList = userNamesForChangeOwnerList;
    }
    public String getSelectTeamNameChangeOwner() {
        return selectTeamNameChangeOwner;
    }
    public void setSelectTeamNameChangeOwner(String selectTeamNameChangeOwner) {
        this.selectTeamNameChangeOwner = selectTeamNameChangeOwner;
    }
    private String selectTeamNameChangeOwner;
    public List<ChangeOwnerView> getTeamNamesForChangeOwerTo() {
        return teamNamesForChangeOwerTo;
    }
    public void setTeamNamesForChangeOwerTo(List<ChangeOwnerView> teamNamesForChangeOwerTo) {
        this.teamNamesForChangeOwerTo = teamNamesForChangeOwerTo;
    }
    public List<PERoster> getChangeOwerViewList() {
        return changeOwerViewList;
    }
    public void setChangeOwerViewList(List<PERoster> changeOwerViewList) {
        this.changeOwerViewList = changeOwerViewList;
    }
    public List getUserList1() {
        return userList1;
    }
    public void setUserList1(List userList1) {
        this.userList1 = userList1;
    }
    public String getSelectRole() {
        return selectRole;
    }
    public void setSelectRole(String selectRole) {
        this.selectRole = selectRole;
    }
    public String getSelectTeam() {
        return selectTeam;
    }
    public void setSelectTeam(String selectTeam) {
        this.selectTeam = selectTeam;
    }

    public String getSelectuser() {
        return selectuser;
    }
    public void setSelectuser(String selectuser) {
        this.selectuser = selectuser;
    }
    public List<ChangeOwnerView> getRolSet() {
        return rolSet;
    }
    /*public void setRolSet(Set rolSet) {
        this.rolSet = rolSet;
    }*/
    public Map<String, Boolean> getChecked() {
        return checked;
    }
    public void setChecked(Map<String, Boolean> checked) {
        this.checked = checked;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @PostConstruct
    public void init()
    {

        HttpSession session = FacesUtil.getSession(false);
        try
        {

            if(session.getAttribute("wobNumber")!=null && session.getAttribute("queueName")!=null && session.getAttribute("fetchType")!=null)
            {
                String wobNumber = (String)session.getAttribute("wobNumber");
                bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNumber,(Integer)session.getAttribute("fetchType"));
            }
        }
        catch (Exception e)
        {
            log.error("Error while unlocking case in queue : {}, wobNumber : {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"), e);
        }

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        teamId = userDetail.getTeamid();
        log.info("teamIdList::::::::" + teamId);
        teamTypeName = new ArrayList<ChangeOwnerView>();
        teamTypeName = userDAO.getTeamLeadUsers(teamId);
        teamNamesForChangeOwerTo =new ArrayList<ChangeOwnerView>();
        teamNamesForChangeOwerTo = userDAO.getTeamUserForChangeOwner(teamId);

        log.info("teamTypeName::::" + teamTypeName);


    }

    public List<ChangeOwnerView> getRoleNameForSelectedTeam(ValueChangeEvent event)
    {

        rolSet = null;
        //rolSet = new TreeSet<ChangeOwnerView>();
        rolSet = new ArrayList<ChangeOwnerView>();
        selectTeam = event.getNewValue().toString();

        if(selectTeam != null && selectTeam.trim().length()>0)
        {
        selectedTeamId = Integer.parseInt(selectTeam);
        }
        if(selectTeam != null)
        {
            rolSet = userDAO.getRoleList(selectedTeamId, teamId);
        }

        userList1 = null;
        return rolSet;

    }


    public List<User> getAllUserNames(ValueChangeEvent event)
    {
        try
        {
            userList1 = null;
            userList1 = new ArrayList();
            selectRole = event.getNewValue().toString();

            usersIdNameList = new ArrayList<User>();

            if(selectRole != null && selectRole.trim().length()>0 && selectTeam != null)
            {
                selectedTeamId = Integer.parseInt(selectTeam);
                userList1 = userDAO.getUserNames(teamId, selectedTeamId,selectRole);
                log.info("userList::::" + userList1);
                Iterator<String> it = userList1.iterator();
                while (it.hasNext())
                {
                    User user1= new User();
                    user1.setUserName(it.next());
                    user1.setId(userDAO.getUserIdByName(user1.getUserName()));
                    usersIdNameList.add(user1);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

       /* String all = "ALL";
        if(userList1.size() > 0)
        {
            userList1.add(all);
        }*/
        return usersIdNameList;
        //return userList1;
    }


    public void changOwnerSearchQuery()
    {

        changeOwerViewList = new ArrayList<PERoster>();

        log.info("in Change Owner :{},{},{},{}",selectTeam,selectRole,selectuser);

        if( selectTeam !=null  && selectTeam.trim().length() > 0 && selectRole != null && selectRole.trim().length() > 0  && selectuser != null && selectuser.trim().length() > 0 )
        {
            changeOwnerQueryList =  userDAO.getChangeOwnerWorkItems(selectTeam,selectRole,selectuser,userList1);

            log.info("in Change Owner if 1 ");
        }
        else
        {
            changeOwnerQueryList = null;

            log.info("in Change Owner if else 1 ");
        }
        if(changeOwnerQueryList != null && changeOwnerQueryList.length() > 0)
        {
            changeOwerViewList = pedbExecute.queryForChangeOwner(changeOwnerQueryList);

            log.info("in Change Owner if 2 ");

        }

        checked.clear();

        setChecked(checked);

        selectTeamNameChangeOwner = "";

        setSelectTeamNameChangeOwner("");

        selectUserChangeOwner = "";

        setSelectUserChangeOwner("");

        remark = "";

        setRemark("");

    }

    public List<User> userNamesForChangeTo(AjaxBehaviorEvent ajaxBehaviorEvent)
    {
        log.info("this is userNamesForChangeTo method");
        UIComponent source = (UIComponent)ajaxBehaviorEvent.getSource();
        log.info("Value:" + ((HtmlSelectOneMenu) source).getValue());
        String selectTeamName= ((HtmlSelectOneMenu)source).getValue().toString();

        usersIdNameList1 = new ArrayList<User>();

        if(selectTeamName != null && selectRole != null)
        {
            selectedTeamId = Integer.parseInt(selectTeamName);
            userNamesForChangeOwnerList = userDAO.getUserNames(teamId, selectedTeamId,selectRole);
            Iterator<String> it = userNamesForChangeOwnerList.iterator();
            while (it.hasNext())
            {
                User user1= new User();
                user1.setUserName(it.next());
                user1.setId(userDAO.getUserIdByName(user1.getUserName()));
                usersIdNameList1.add(user1);
            }
            log.info("userNamesForChangeOwnerList:::::::::::"+userNamesForChangeOwnerList);
        }
        //return  userNamesForChangeOwnerList;
        return usersIdNameList1;

    }



    public void onReassignBulk()
    {
        log.info("controller cmes to onReassignBulk method ");

        HashMap<String,String> fieldsMap = new HashMap<String, String>();
        List<String>wobNumbersList = new ArrayList<String>();
        Object[] arryOfObjectWobNOs = null;
        String[] stringArrayOfWobNos = null;
        String roleName = null;

        if(selectRole != null && selectRole.trim().length()>0)
        {
            int   selectedRoleId = Integer.parseInt(selectRole);

            roleName = userDAO.getRoleNameFromRoleTable(selectedRoleId);
            log.info("roleName:::::::"+roleName);

        }
        if(selectUserChangeOwner != null && selectUserChangeOwner.trim().length() > 0 && selectuser != null && roleName != null)
        {
            log.info("cntroler in not null condition");
            fieldsMap.put(BPMConstants.BPM_FIELD_ACTION_NAME, "Change Owner");
            fieldsMap.put("NewUser",selectUserChangeOwner);
            fieldsMap.put("LastUser",selectuser);
            fieldsMap.put("CO_Role",roleName);
            fieldsMap.put("Remarks",remark);
        }


        log.info("stringStringHashMap length is:::::"+fieldsMap.size());

        for ( Map.Entry<String, Boolean> entry : checked.entrySet()) {

            if (entry.getValue()==true)
            {
                String wobNo =entry.getKey().toString();
                //log.info("wobno:::::::::::::"+wobNo);
                wobNumbersList.add(wobNo);

            }

        }

        if(wobNumbersList!=null && wobNumbersList.size()>5)
        {
            log.info("in if for more than 5 cases");
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg1.show()");
            log.info("after execute.. ");
            return;
        }
        arryOfObjectWobNOs =wobNumbersList.toArray();
        stringArrayOfWobNos = Arrays.copyOf(arryOfObjectWobNOs, arryOfObjectWobNOs.length, String[].class);
        log.info("stringArray length:::::::::::::::::::::::"+stringArrayOfWobNos.length);


        try {
            if(peRosterName != null && stringArrayOfWobNos.length !=0 && fieldsMap.size() !=0)
            {

                bpmInterfaceImpl.batchDispatchCaseFromRoster(peRosterName,stringArrayOfWobNos,fieldsMap);

                changeOwnerControl.updateUserName(stringArrayOfWobNos, selectUserChangeOwner, selectuser);

                log.info("batchDispatch successful.... ");
                changOwnerSearchQuery();
            }

            checked.clear();
            setChecked(checked);

            selectTeamNameChangeOwner = "";

            setSelectTeamNameChangeOwner("");

            selectUserChangeOwner = "";

            setSelectUserChangeOwner("");

            remark = "";

            setRemark("");

            RequestContext.getCurrentInstance().execute("successDlg.show()");

            return;

        }
        catch (Exception e)
        {

            wobNumbersList = null;
            arryOfObjectWobNOs = null;
            stringArrayOfWobNos = null;
            roleName = null;
            fieldsMap = null;
            log.error("Error in change owner : {}",e);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg2.show()");
            return;
        }
    }
    
    public void onSelectInbox(){

        HttpSession session = FacesUtil.getSession(false);

        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: rosterViewSelectItem : {}", rosterViewSelectItem);

        long stepId = rosterViewSelectItem.getStepId();
        String appNumber = rosterViewSelectItem.getAppNumber();
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long wrkCaseAppraisalId = 0L;
        long statusId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;
        String queueName = rosterViewSelectItem.getQueuename();

        try {

            try{
                //Try to Lock case
                log.info("locking case queue: {}, wobNumber : {}, fetchtype: {}",queueName, rosterViewSelectItem.getF_WobNum(),rosterViewSelectItem.getFetchType());
                bpmInterfaceImpl.lockCase(queueName,rosterViewSelectItem.getF_WobNum(),rosterViewSelectItem.getFetchType());
            /*session.setAttribute("isLocked","true");*/
            } catch (Exception e) {
                log.error("Error while Locking case in queue : {}, wobNumber : {}",queueName, rosterViewSelectItem.getF_WobNum(), e);
                //message = "Another User is Working on this case!! Please Retry Later.";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
                return;
                //log.error("Error while opening case",e);
            }

            if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value()) {     //For Case in Stage PreScreen
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
                if(workCasePrescreen != null){
                    wrkCasePreScreenId = workCasePrescreen.getId();
                    requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
                    statusId = workCasePrescreen.getStatus().getId();
                }
                session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
                session.setAttribute("requestAppraisal", requestAppraisalFlag);
                session.setAttribute("statusId", statusId);
                session.setAttribute("wobNumber",rosterViewSelectItem.getF_WobNum());
            } else if (stepId == StepValue.REQUEST_APPRAISAL_POOL.value() || stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()) {     //For Case in Stage Parallel Appraisal
                WorkCase workCase = workCaseDAO.findByAppNumber(appNumber);
                if(workCase != null){
                    wrkCaseId = workCase.getId();
                    requestAppraisalFlag = workCase.getRequestAppraisal();
                    session.setAttribute("workCaseId", wrkCaseId);
                    session.setAttribute("requestAppraisal", requestAppraisalFlag);
                } else {
                    WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
                    wrkCasePreScreenId = workCasePrescreen.getId();
                    requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
                    session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
                    session.setAttribute("requestAppraisal", requestAppraisalFlag);
                }
                WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByAppNumber(appNumber);
                if(workCaseAppraisal != null){
                    statusId = workCaseAppraisal.getStatus().getId();
                    wrkCaseAppraisalId = workCaseAppraisal.getId();
                    session.setAttribute("statusId", statusId);
                    session.setAttribute("workCaseAppraisalId", wrkCaseAppraisalId);
                }
            } else {        //For Case in Stage FullApplication
                WorkCase workCase = workCaseDAO.findByAppNumber(appNumber);
                if(workCase != null){
                    wrkCaseId = workCase.getId();
                    requestAppraisalFlag = workCase.getRequestAppraisal();
                    statusId = workCase.getStatus().getId();
                }
                session.setAttribute("workCaseId", wrkCaseId);
                session.setAttribute("requestAppraisal", requestAppraisalFlag);
                session.setAttribute("statusId", statusId);
                session.setAttribute("wobNumber",rosterViewSelectItem.getF_WobNum());
            }

            if(Util.isNull(rosterViewSelectItem.getFetchType())) {
                session.setAttribute("fetchType",0);
            } else {
                session.setAttribute("fetchType",rosterViewSelectItem.getFetchType());
            }

            if(stepId != 0){
                Step step = stepDAO.findById(stepId);
                stageId = step != null ? step.getStage().getId() : 0;
            }

            session.setAttribute("stepId", stepId);
            session.setAttribute("stageId", stageId);
            session.setAttribute("caseOwner",rosterViewSelectItem.getAtUser());

            if(Util.isNull(queueName)) {
                session.setAttribute("queueName", "0");
            } else {
                session.setAttribute("queueName", queueName);
            }

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, rosterViewSelectItem.getAppNumber());
            session.setAttribute("appHeaderInfo", appHeaderView);

            String landingPage = inboxControl.getLandingPage(stepId,0);

            log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, queueName);

            if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
                FacesUtil.redirect(landingPage);
                return;
            } else {
                //TODO Show dialog
            }

        } catch (Exception e) {
            //log.error("Error while Locking case in queue : {}, wobNumber : {}",queueName, rosterViewSelectItem.getFwobnumber(), e);
            //message = e.getMessage();
            //RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            log.error("Error while opening case",e);
        }

    }

    public void checkReassign(AjaxBehaviorEvent ajaxBehaviorEvent)
    {

        log.info("Selected User Name : {}",selectuser);

        selectTeamNameChangeOwner = "";

        setSelectTeamNameChangeOwner("");

        selectUserChangeOwner = "";

        setSelectUserChangeOwner("");

        remark = "";

        setRemark("");

        if(selectuser.equalsIgnoreCase("ALL"))
        {
            disableReassign = true;

            log.info("Reassign Button disabled : {}",disableReassign);
        }

        else
        {
            UIComponent source = (UIComponent)ajaxBehaviorEvent.getSource();

            log.info("UIComponent source : {}",source);

            if(source!= null)
            {
                log.info("Value:" + ((SelectBooleanCheckbox) source).getValue());

                String selectedCase= ((SelectBooleanCheckbox)source).getValue().toString();

                log.info("Selected Case WobNum : {}",selectedCase);

                int noOfCases = 0;

                for ( Map.Entry<String, Boolean> entry : checked.entrySet())
                {

                    if (entry.getValue()==true)
                    {
                        String wobNo = entry.getKey().toString();

                        log.info("WobNum : {}",wobNo);

                        noOfCases++;

                    }
                }

                log.info("No. Of Cases Selected : {}", noOfCases);

                if(selectedCase.equalsIgnoreCase("true") || noOfCases > 0)
                {

                    disableReassign = false;

                    log.info("Reassign Button enabled : {}",disableReassign);
                }

                else
                {
                    disableReassign =true;

                    log.info("Reassign Button disabled : {}",disableReassign);
                }

                if(noOfCases >5 )
                {
                    disableReassign =true;

                    RequestContext.getCurrentInstance().execute("msgBoxErrorDlg1.show()");

                    log.info("Reassign Button disabled : {}",disableReassign);
                }

            }

            else
            {
                disableReassign = true;

                log.info("Reassign Button disabled : {}",disableReassign);
            }

        }

    }
    
}

