package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.StepDAO;
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
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.model.view.ReassignTeamNameId;
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

@ViewScoped
@ManagedBean(name = "reassignteamnames")
public class ReassignTeamNames implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    UserTeamDAO userTeamDAO;

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

    UserDetail userDetail;

    List<ReassignTeamNameId> reasignteamnames ;

    List<String> teamuserslist;

    private String selectedTeamName;

    private String selectedUserName;

    private List<PEInbox> reassignSearchViewList;

    private PEInbox searchViewSelectItem;

    int changedteamid = 0;

    private String popupselectedteamname;

    private String popupselectedusername;

    private String popupremark;

    private boolean disableReassign = true;

    private Map<String, Boolean> checked =  new HashMap<String, Boolean>();

    List<ReassignTeamNameId> popupreasignteamnames ;

    List<String> poupreasignUsernames;

    Map.Entry entry;

    @Inject
    PEDBExecute pedbExecute;
    @Inject
    InboxControl inboxControl;

    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

    public BPMExecutor getBpmExecutor() {
        return bpmExecutor;
    }

    public void setBpmExecutor(BPMExecutor bpmExecutor) {
        this.bpmExecutor = bpmExecutor;
    }

    @Inject

    BPMExecutor bpmExecutor;

    @Inject
    ActionDAO actionDAO;

    String username = null;

    String queryusername = null;

    String modifiedselectedusername = null;

    List<String> usernameslist = new ArrayList<String>();

    List<String> modifiedusernamelist = new ArrayList<String>();

    Object[] wobnos = null;

    String[] stringArray = null;

    HashMap<String,String> reassignhashmap = null;

    @Inject
    @Config(name = "interface.pe.rosterName")
    String rostername;

    public boolean isDisableReassign() {
        return disableReassign;
    }

    public void setDisableReassign(boolean disableReassign) {
        this.disableReassign = disableReassign;
    }

    public String getSelectedUserName() {
        return selectedUserName;
    }

    public void setSelectedUserName(String selectedUserName) {
        this.selectedUserName = selectedUserName;
    }

    public String getSelectedTeamName() {
        return selectedTeamName;
    }

    public void setSelectedTeamName(String selectedTeamName) {
        this.selectedTeamName = selectedTeamName;
    }

    public UserTeamDAO getUserTeamDAO() {
        return userTeamDAO;
    }

    public void setUserTeamDAO(UserTeamDAO userTeamDAO) {
        this.userTeamDAO = userTeamDAO;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public Map.Entry getEntry() {
        return entry;
    }

    public void setEntry(Map.Entry entry) {
        this.entry = entry;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQueryusername() {
        return queryusername;
    }

    public void setQueryusername(String queryusername) {
        this.queryusername = queryusername;
    }

    public String getModifiedselectedusername() {
        return modifiedselectedusername;
    }

    public void setModifiedselectedusername(String modifiedselectedusername) {
        this.modifiedselectedusername = modifiedselectedusername;
    }

    public List<String> getUsernameslist() {
        return usernameslist;
    }

    public void setUsernameslist(List<String> usernameslist) {
        this.usernameslist = usernameslist;
    }

    public List<String> getModifiedusernamelist() {
        return modifiedusernamelist;
    }

    public void setModifiedusernamelist(List<String> modifiedusernamelist) {
        this.modifiedusernamelist = modifiedusernamelist;
    }

    public Object[] getWobnos() {
        return wobnos;
    }

    public void setWobnos(Object[] wobnos) {
        this.wobnos = wobnos;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public HashMap<String, String> getReassignhashmap() {
        return reassignhashmap;
    }

    public void setReassignhashmap(HashMap<String, String> reassignhashmap) {
        this.reassignhashmap = reassignhashmap;
    }

    public String getRostername() {
        return rostername;
    }

    public void setRostername(String rostername) {
        this.rostername = rostername;
    }

    public List<PEInbox> getReassignSearchViewList() {
        return reassignSearchViewList;

    }

    public void setReassignSearchViewList(List<PEInbox> reassignSearchViewList) {
        this.reassignSearchViewList = reassignSearchViewList;
    }

    public PEInbox getSearchViewSelectItem() {
        return searchViewSelectItem;
    }

    public void setSearchViewSelectItem(PEInbox searchViewSelectItem) {
        this.searchViewSelectItem = searchViewSelectItem;
    }

    public List<String> getTeamuserslist() {
        return teamuserslist;
    }

    public void setTeamuserslist(List<String> teamuserslist) {
        this.teamuserslist = teamuserslist;
    }

    public String getPopupselectedusername() {
        return popupselectedusername;
    }

    public void setPopupselectedusername(String popupselectedusername) {
        this.popupselectedusername = popupselectedusername;
    }

    public String getPopupselectedteamname() {
        return popupselectedteamname;
    }

    public void setPopupselectedteamname(String popupselectedteamname) {
        this.popupselectedteamname = popupselectedteamname;
    }

    public String getPopupremark() {
        return popupremark;
    }

    public void setPopupremark(String popupremark) {
        this.popupremark = popupremark;
    }

    public List<String> getPoupreasignUsernames() {
        return poupreasignUsernames;
    }

    public void setPoupreasignUsernames(List<String> poupreasignUsernames) {
        this.poupreasignUsernames = poupreasignUsernames;
    }

    public Map<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<String, Boolean> checked) {
        this.checked = checked;
    }

    public void setReasignteamnames(List<ReassignTeamNameId> reasignteamnames) {
        this.reasignteamnames = reasignteamnames;
    }

    public List<ReassignTeamNameId> getPopupreasignteamnames() {
        return popupreasignteamnames;
    }

    public void setPopupreasignteamnames(List<ReassignTeamNameId> popupreasignteamnames) {
        this.popupreasignteamnames = popupreasignteamnames;
    }

    public ReassignTeamNames()
    {

    }

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

    @PostConstruct
    public void init()
    {

        disableReassign = true;

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);
        try
        {
            /*if(session.getAttribute("isLocked")!=null)
            {

                String isLocked = (String) session.getAttribute("isLocked");

                if(isLocked.equalsIgnoreCase("true"))
                {*/
                    String wobNumber = (String)session.getAttribute("wobNumber");
                    bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNumber,(Integer)session.getAttribute("fetchType"));
                /*}
                else
                {
                    session.removeAttribute("isLocked");
                }

            }*/
        }
        catch (Exception e)
        {
            log.error("Error while unlocking case in queue : {}, wobNumber : {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"), e);
        }

        reasignteamnames = new ArrayList<ReassignTeamNameId>();

        popupreasignteamnames = new ArrayList<ReassignTeamNameId>();

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("teamid value in init method of ReassignTeamNames class  is :::::: {}",userDetail.getTeamid());

        reasignteamnames =  userTeamDAO.getUserteams(userDetail.getTeamid(),"Y");

        popupreasignteamnames =  userTeamDAO.popUpUserTeamNames(userDetail.getTeamid(),"Y");

        log.info("popupreasignteamnames size is : {}",popupreasignteamnames.size());

        log.info("reasignteamnames ::::::::: {}",reasignteamnames.toString());

    }

    public List<User> valueChangeMethod(ValueChangeEvent e)
    {

        disableReassign = true;

        log.info("controller comes to valueChangeMethod of ReassignTeamNames class");

        teamuserslist = new ArrayList<String>();

        log.info("new value is :::::::: {}",Integer.parseInt(e.getNewValue().toString()));

        changedteamid = Integer.parseInt(e.getNewValue().toString());

        usersIdNameList = new ArrayList<User>();

        if(changedteamid != 0 )
        {

            teamuserslist = userTeamDAO.getUsers(changedteamid);

            Iterator<String> it = teamuserslist.iterator();
            while (it.hasNext())
            {
                User user1= new User();
                user1.setUserName(it.next());
                user1.setId(userTeamDAO.getUserIdByName(user1.getUserName()));
                usersIdNameList.add(user1);
                user1 = null;
            }


        }

        //return teamuserslist;

        return usersIdNameList;

    }

    public List<PEInbox> reassignSearch()
    {

        disableReassign = true;

         log.info("Controller comes to reassignSearch method of ReassignTeamNames class");

        reassignSearchViewList = new ArrayList<PEInbox>();

        log.info("selectedteam name is ::::::::::::{}",selectedTeamName);

        log.info("selectedUsername is :::::::::::::{}",selectedUserName);

        if(selectedUserName.equalsIgnoreCase("ALL"))
        {
            log.info("controller comes to selectedUsername is all condition ");

            String all = "ALL";

            usernameslist =  userTeamDAO.getUsers(changedteamid);

            if(usernameslist.contains(all))
            {
                usernameslist.remove(all);
            }

            log.info("After remove all string usernameslist size is :: {}",usernameslist.size());

            for(String user:usernameslist)
            {
                log.info("username : ",user);

                String modifieduser =  "'" + user + "'";

                log.info("modifiedusername is :: {}",modifieduser);

                modifiedusernamelist.add(modifieduser);
            }

            log.info("modified username list size is : ",modifiedusernamelist.size());

            username = modifiedusernamelist.toString();

            log.info("username iss (all) :::::::::::: {}",username);

            queryusername =  username.replace("[","").replace("]","");

            log.info("queryusername iss (all) :::::::::::: {}",queryusername);

        }
       else if(selectedUserName != "ALL")
        {
            log.info("selectedUserName is not ALL");

            modifiedselectedusername  =  "'" + selectedUserName + "'";

            queryusername     =    modifiedselectedusername;

            log.info("queryusername iss  :::::::::::: {}",queryusername);

        }

        try
        {


            if(selectedTeamName != "" && selectedTeamName.length() > 0 && queryusername.length() > 0 && queryusername != "")
            {
                reassignSearchViewList = pedbExecute.getReassignSearch(selectedTeamName,queryusername);
                checked.clear();
                setChecked(checked);
            }
            log.info("reassignsearchviewlist size is :::::: {}",reassignSearchViewList.size());

            popupselectedteamname= "";

            setPopupselectedteamname("");

            popupselectedusername = "";

            setPopupselectedusername("");

            popupremark = "";

            setPopupremark("");


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {

        }
        return reassignSearchViewList;
    }


    public void onSelectInbox() {

        HttpSession session = FacesUtil.getSession(false);

        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);

        long stepId = searchViewSelectItem.getStepId();
        String appNumber = searchViewSelectItem.getApplicationno();
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long wrkCaseAppraisalId = 0L;
        long statusId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;
        String queueName = searchViewSelectItem.getQueuename();

        try {

            try{
            //Try to Lock case
            log.info("locking case queue: {}, wobNumber : {}, fetchtype: {}",queueName, searchViewSelectItem.getFwobnumber(),searchViewSelectItem.getFetchType());
            bpmInterfaceImpl.lockCase(queueName,searchViewSelectItem.getFwobnumber(),searchViewSelectItem.getFetchType());
            /*session.setAttribute("isLocked","true");*/
            } catch (Exception e) {
                 log.error("Error while Locking case in queue : {}, wobNumber : {}",queueName, searchViewSelectItem.getFwobnumber(), e);
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
                session.setAttribute("wobNumber",searchViewSelectItem.getFwobnumber());
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
                session.setAttribute("wobNumber",searchViewSelectItem.getFwobnumber());
            }

            if(Util.isNull(searchViewSelectItem.getFetchType())) {
                session.setAttribute("fetchType",0);
            } else {
                session.setAttribute("fetchType",searchViewSelectItem.getFetchType());
            }

            if(stepId != 0){
                Step step = stepDAO.findById(stepId);
                stageId = step != null ? step.getStage().getId() : 0;
            }

            session.setAttribute("stepId", stepId);
            session.setAttribute("stageId", stageId);
            session.setAttribute("caseOwner",searchViewSelectItem.getAtuser());

            if(Util.isNull(queueName)) {
                session.setAttribute("queueName", "0");
            } else {
                session.setAttribute("queueName", queueName);
            }

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, Util.parseLong(searchViewSelectItem.getStatuscode(), 0), searchViewSelectItem.getFwobnumber());
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
            //log.error("Error while Locking case in queue : {}, wobNumber : {}",queueName, searchViewSelectItem.getFwobnumber(), e);
            //message = "Another User is Working on this case!! Please Retry Later.";
            // RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            log.error("Error while opening case",e);
        }

        /*userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("userDetails  : "+userDetail);

        if(userDetail == null)
        {
            FacesUtil.redirect("/login.jsf");
            return;
        }

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);

        if(!Util.isEmpty(Long.toString(searchViewSelectItem.getWorkCasePreScreenId()))){
            session.setAttribute("workCasePreScreenId", searchViewSelectItem.getWorkCasePreScreenId());
        } else {
            session.setAttribute("workCasePreScreenId", 0);
        }
        if(!Util.isEmpty(Long.toString(searchViewSelectItem.getWorkCaseId()))){
            session.setAttribute("workCaseId", searchViewSelectItem.getWorkCaseId());
        } else {
            session.setAttribute("workCaseId", 0);
        }

        session.setAttribute("wobNumber",searchViewSelectItem.getFwobnumber());
        session.setAttribute("statusId", Util.parseLong(searchViewSelectItem.getStatuscode(), 0));
        session.setAttribute("stepId", searchViewSelectItem.getStepId());
        session.setAttribute("queuename",searchViewSelectItem.getQueuename());
        session.setAttribute("fetchType",searchViewSelectItem.getFetchType());
        session.setAttribute("caseOwner",searchViewSelectItem.getAtuser());

        try
        {

            bpmInterfaceImpl.lockCase(searchViewSelectItem.getQueuename(),searchViewSelectItem.getFwobnumber(),searchViewSelectItem.getFetchType());
            session.setAttribute("isLocked","true");

        }
        catch (Exception e)
        {
            log.error("Error while Locking case in queue : {}, wobNumber : {}",searchViewSelectItem.getQueuename(), searchViewSelectItem.getFwobnumber(), e);
        }

       *//* AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(searchViewSelectItem.getWorkCasePreScreenId(), searchViewSelectItem.getWorkCaseId());
        session.setAttribute("appHeaderInfo", appHeaderView);*//*

        long selectedStepId = searchViewSelectItem.getStepId();
        String landingPage = inboxControl.getLandingPage(selectedStepId,Util.parseLong(searchViewSelectItem.getStatuscode(), 0));

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {

        }*/

    }

    public List<User> changeUserNameBasedOnTeamName(AjaxBehaviorEvent ajaxBehaviorEvent)
    {

        disableReassign = true;

        log.info("controller comes to changeUserNameBasedOnTeamName of ReassignTeamNames class ");

        poupreasignUsernames = new ArrayList<String>();

        UIComponent source = (UIComponent)ajaxBehaviorEvent.getSource();

        log.info("Value:" + ((HtmlSelectOneMenu) source).getValue());

        String selectTeamName= ((HtmlSelectOneMenu)source).getValue().toString();

        int selectTeamNameid = Integer.parseInt(selectTeamName);

        log.info("selected team name in changeUserNameBasedOnTeamName method of ReassignTeamNames class is : {}",selectTeamNameid);

        poupreasignUsernames = userTeamDAO.getPopUsers(selectTeamNameid);

        Iterator<String> it = poupreasignUsernames.iterator();
        usersIdNameList1 = new ArrayList<User>();

        while (it.hasNext())
        {

            User user1 = new User();
            user1.setUserName(it.next());
            user1.setId(userTeamDAO.getUserIdByName(user1.getUserName()));

            usersIdNameList1.add(user1);

            user1= null;
        }

        return usersIdNameList1;

        //return poupreasignUsernames;
    }


    public List<ReassignTeamNameId> getReasignteamnames() {
        return reasignteamnames;
    }

    public int getChangedteamid() {
        return changedteamid;
    }

    public void setChangedteamid(int changedteamid) {
        this.changedteamid = changedteamid;
    }

    public void reassignDisptchRecords()
    {

        disableReassign = true;

        ReassignTeamNameId reassignTeamNameId = new ReassignTeamNameId();

        log.info("controller entered in to reassignDispatchRecords method of ReassignTeamNames");

        log.info("popupselectedteamname :::::: {}",popupselectedteamname);

        log.info("popupselectedusername :::::: {}",popupselectedusername);

        log.info("popupremark :::::: {}",popupremark);

        log.info("checked ::::::: {}",checked);

        log.info("previous user is :::::::: {}",selectedUserName);

        reassignhashmap  = new HashMap<String,String>();

        reassignhashmap.put(BPMConstants.BPM_FIELD_ACTION_NAME,"Reassign");

        if(popupselectedusername != "")
        {
        reassignhashmap.put("NewUser",popupselectedusername);
        }
        if(selectedUserName != "")
        {
        reassignhashmap.put("LastUser",selectedUserName);
        }
        if(popupremark != "")
        {
            reassignhashmap.put("Remarks",popupremark);
        }

        log.info("reassign hash map size is :::::::::::: {}",reasignteamnames.size());

        List<String> wobNumbersList = new ArrayList<String>();

        for ( Map.Entry<String, Boolean> entry : checked.entrySet())
        {

            if (entry.getValue()==true)
            {
                String wobNo = entry.getKey().toString();

                log.info("wobno:::::::::::::{}",wobNo);


                wobNumbersList.add(wobNo) ;
            }

            if(wobNumbersList!=null && wobNumbersList.size()>5)
            {
                log.info("in if for more than 5 cases");
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg1.show()");
                log.info("after execute.. ");
                return;
            }
            wobnos =wobNumbersList.toArray();

            log.info("wobNumbersList:::::::::::::::::::{}",wobNumbersList.size());

            log.info("wobnos array is ::::::::::::::",wobnos.length);

            stringArray = Arrays.copyOf(wobnos, wobnos.length, String[].class);

            log.info("****** : "+stringArray);

        }

        log.info("rostertablename in getPESearchList method is : {}",rostername);

        log.info("stringArray::::::::::::::::::::::::::{}",stringArray.length);

        try
        {
            if(rostername != "" && stringArray.length != 0 && reassignhashmap.size() != 0)
            {
                bpmExecutor.batchDispatchCaseFromRoster(rostername,stringArray,reassignhashmap);
            }

            if(selectedTeamName != "" && selectedTeamName.length() > 0 && queryusername.length() > 0 && queryusername != "")
            {
                checked.clear();
                setChecked(checked);
                reassignSearchViewList = pedbExecute.getReassignSearch(selectedTeamName,queryusername);
                log.info("before execute dialog..");
                RequestContext.getCurrentInstance().execute("successDlg.show()");
                log.info("after execute dialog..");
            }
            log.info("reassignsearchviewlist size is :::::: {}",reassignSearchViewList.size());

            checked.clear();

            setChecked(checked);

            popupselectedteamname= "";

            setPopupselectedteamname("");

            popupselectedusername = "";

            setPopupselectedusername("");

            popupremark = "";

            setPopupremark("");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {

        }

    }

    public void checkReassign(AjaxBehaviorEvent ajaxBehaviorEvent)
    {

        log.info("Selected User Name : {}",selectedUserName);

        if(selectedUserName.equalsIgnoreCase("ALL"))
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

            }

            else
            {
                disableReassign = true;

                log.info("Reassign Button disabled : {}",disableReassign);
            }

        }

    }

}
