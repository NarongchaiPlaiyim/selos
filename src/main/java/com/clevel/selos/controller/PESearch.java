package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.master.UserTeamDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCaseOwnerDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.TeamTypeValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.model.view.ReassignTeamNameId;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@ViewScoped
@ManagedBean(name = "peSearch")
public class PESearch implements Serializable
{
    @Inject
    @SELOS
    private Logger log;

    @Inject
    private BPMInterfaceImpl bpmInterfaceImpl;

    @Inject
    private PEDBExecute pedbExecute;

    @Inject
    private InboxControl inboxControl;
    @Inject
    private HeaderControl headerControl;

    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private UserTeamDAO userTeamDAO;
    @Inject
    private WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    private StepDAO stepDAO;

    private List<PEInbox> searchViewList;
    private PEInbox searchViewSelectItem;
    private UserDetail userDetail;
    private String applicationNumber;
    private String step;
    private String status;
    private Date date1;
    private Date date2;
    private Date date3;
    private Date date4;
    private String firstname;
    private String lastname;
    private String userid;
    private String citizendid;
    private String statusType;
    private String currentDateDDMMYY;
    private String message;

    public PESearch()
    {
        setStatusType(statusType);

    }

    @PostConstruct
    public void onCreation()
    {
        log.debug("Controller in onCreation method of PESearch.java ");

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);

        setStatusType("InprocessCases");
        try
        {
            if(session.getAttribute("stepId")!=null)
            {

                if((Long)session.getAttribute("stepId") !=StepValue.COMPLETED_STEP.value() && session.getAttribute("wobNumber")!=null && session.getAttribute("queueName")!=null && session.getAttribute("fetchType")!=null)
                {
                    String wobNumber = (String)session.getAttribute("wobNumber");
                    String queueName = (String)session.getAttribute("queueName");
                    if(wobNumber.trim().length()!=0 || queueName.trim().length()!=0)
                    {
                        log.debug("unlocking case queue: {}, wobNumber : {}, fetchtype: {}", session.getAttribute("queueName"), session.getAttribute("wobNumber"),session.getAttribute("fetchType"));
                        bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNumber,(Integer)session.getAttribute("fetchType"));
                    }
                }

            }
        }
        catch (Exception e)
        {

            log.error("Error while unlocking case in queue : {}, wobNumber : {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"), e);
        }

        session.setAttribute("workCasePreScreenId", 0L);
        session.setAttribute("workCaseAppraisalId", 0L);
        session.setAttribute("workCaseId", 0L);
        session.setAttribute("stepId", 0L);
        session.setAttribute("statusId", 0L);
        session.setAttribute("stageId", 0);
        session.setAttribute("requestAppraisal", 0);
        session.setAttribute("queueName", "");
        session.setAttribute("wobNumber", "");
        session.setAttribute("caseOwner", "");
        session.setAttribute("fetchType", 0);

    }

    public List<PEInbox> search()
    {
        log.debug("Controller comes to PESearch method of search class ");

        try{
            searchViewList = pedbExecute.getPESearchList(statusType,applicationNumber,userid,step,status,citizendid,firstname,lastname,date1,date2,date3,date4);
            log.debug("searchViewList size is : {}", searchViewList.size());
        }catch (Exception e){
            searchViewList = new ArrayList<PEInbox>();
            log.error("Error in search() of PESearch",e);
        }finally{
            log.debug("searchViewList finally : {}", searchViewList.size());
        }

        return searchViewList;
    }

    public void resetSearchFields()
    {
        applicationNumber = "";
        step = "";
        status= "";
        date1 = null;
        date2 = null;
        date3 = null;
        date4 = null;
        firstname= "";
        lastname= "";
        userid= "";
        citizendid= "";
        searchViewList=null;
    }

    public void onSelectInbox() {

        try
        {

            log.debug("onSelectInbox ::: setSession ");
            log.debug("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);
            HttpSession session = FacesUtil.getSession(false);

            userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("userDetails  : {}", userDetail);
            User user = userDAO.findById(userDetail.getUserName());

            long stepId = Util.parseLong(searchViewSelectItem.getStepId(), 0);
            long statusId = Util.parseLong(searchViewSelectItem.getStatuscode(), 0);
            long wrkCasePreScreenId = 0L;
            long wrkCaseId = 0L;
            long wrkCaseAppraisalId = 0L;
            int stageId = 0;
            int requestAppraisalFlag = 0;
            int fetchType = Util.parseInt(searchViewSelectItem.getFetchType(), 0);
            String appNumber = Util.parseString(searchViewSelectItem.getApplicationno(), "");
            String queueName = Util.parseString(searchViewSelectItem.getQueuename(), "0");
            String wobNumber = Util.parseString(searchViewSelectItem.getFwobnumber(), "");
            String caseOwner = Util.parseString(searchViewSelectItem.getAtuser(), "");
            String createBy = "";

            caseOwner = (caseOwner.contains("-")?caseOwner.substring(0,caseOwner.indexOf("-")).trim():caseOwner);

            boolean accessAuthorize = false;

            WorkCase workCase = workCaseDAO.findByAppNumber(searchViewSelectItem.getApplicationno());
            if(workCase == null){
                log.debug("onSelectSearch ::: select by workCase ...");
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(searchViewSelectItem.getApplicationno());
                wrkCaseId = 0;
                if(workCasePrescreen != null) {
                    log.debug("onSelectSearch ::: select by workCasePreScreen ...");
                    wrkCasePreScreenId = workCasePrescreen.getId();
                    createBy = workCasePrescreen.getCreateBy() != null ? workCasePrescreen.getCreateBy().getId() : "";
                }
            }else{
                wrkCaseId = workCase.getId();
                wrkCasePreScreenId = 0;
                createBy = workCase.getCreateBy() != null ? workCase.getCreateBy().getId() : "";
            }

            log.debug("onSelectSearch ::: wrkCaseId : {}, wrkCasePreScreenId : {}, createBy : {}", wrkCaseId, wrkCasePreScreenId, createBy);

            String currentUser = searchViewSelectItem.getAtuser();

            log.debug("Current user : "+currentUser);

            currentUser = currentUser!=null&&currentUser.contains("-")?currentUser.substring(0,currentUser.indexOf("-")).trim():currentUser;

            log.debug("Current user : "+currentUser);

            log.debug("User Role IsNull : {}, User :{},  Role : {}",!Util.isNull(user.getRole()) ,user,user.getRole().getId());

            if(!Util.isNull(user.getRole()) && ( user.getRole().getId() == RoleValue.GH.id() || user.getRole().getId() == RoleValue.CSSO.id() || user.getRole().getId() == RoleValue.VIEWER.id() || user.getRole().getId() == RoleValue.UW.id())) {
                accessAuthorize = true;
                log.debug("onSelectSearch ::: after check by ROLE_GH, ROLE_CSSO ,, user role = : {}", user.getRole() != null ? user.getRole().getId() : "NULL");
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && currentUser!=null && currentUser.equalsIgnoreCase(user.getId()))
            {
                accessAuthorize = true;
                log.debug("onSelectSearch ::: after check by current user, Current User :{}",currentUser);
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && (!Util.isNull(user.getTeam()) &&
                    (user.getTeam().getTeam_type() == TeamTypeValue.GROUP_HEAD.value() || user.getTeam().getTeam_type() == TeamTypeValue.CSSO.value()))) {
                accessAuthorize = true;
                log.debug("onSelectSearch ::: after check by TEAM_GROUP_HEAD, TEAM_CSSO ,, user team = : {}", user.getTeam() != null ? user.getTeam().getTeam_type() : "NULL");
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && createBy.equalsIgnoreCase(user.getId())) {
                accessAuthorize = true;
                log.debug("onSelectSearch ::: after check by USER_ID ,, user id = : {}", user.getId());
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && checkAuthorizeWorkCaseOwner(wrkCasePreScreenId, wrkCaseId, user.getId()) && user.getRole().getId()!=RoleValue.BDM.id()) {
                accessAuthorize = true;
                log.debug("onSelectSearch ::: after checkAuthorizeWorkCaseOwner");
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && !Util.isNull(user.getTeam()) &&
                    (user.getTeam().getTeam_type() == TeamTypeValue.TEAM_HEAD.value() || user.getTeam().getTeam_type() == TeamTypeValue.TEAM_LEAD.value())) {
                if (checkAuthorizeTeam(wrkCasePreScreenId, wrkCaseId, user))
                    accessAuthorize = true;
                log.debug("onSelectSearch ::: after checkAuthorizeWorkCaseOwner");
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);
            }

            if(!accessAuthorize && !(user.getRole().getId()==RoleValue.BDM.id()) && !(user.getRole().getId()==RoleValue.ABDM.id()))
            {

                if(checkAuthorizeRole(wrkCasePreScreenId, wrkCaseId, user))
                {
                    accessAuthorize = true;
                }
                log.debug("onSelectSearch ::: after checkAuthorizeWorkCaseOwnerRole");
                log.debug("onSelectSearch ::: accessAuthorize : {}", accessAuthorize);

            }

            if(!accessAuthorize){
                log.debug("You are not authorised to view this case. else after 3 2 ");
                message = "You are not Authorised to view this case!";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg1.show()");
                return;
            }

            log.info("onSelectInbox ::: setSession ");
            log.info("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);

            try{
                //Try to Lock case
                log.info("locking case queue: {}, WobNum : {}, fetchtype: {}, Step Id:{}",queueName, searchViewSelectItem.getFwobnumber(), searchViewSelectItem.getFetchType(),searchViewSelectItem.getStepId());
                if(searchViewSelectItem.getStepId().intValue() != StepValue.COMPLETED_STEP.value()) {
                    bpmInterfaceImpl.lockCase(queueName, wobNumber, searchViewSelectItem.getFetchType());
                }
                /*session.setAttribute("isLocked","true");*/
            } catch (Exception e) {
                log.error("Error while Locking case in queue : {}, WobNum : {}",queueName, wobNumber, e);
                message = "Another User is Working on this case!! Please Retry Later.";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            }

            if(stepId != 0){
                Step step = stepDAO.findById(stepId);
                stageId = step != null ? step.getStage().getId() : 0;
            }

            workCase = workCaseDAO.findByAppNumber(appNumber);
            if(!Util.isNull(workCase)){
                wrkCaseId = workCase.getId();
                requestAppraisalFlag = workCase.getRequestAppraisal();
                if(Util.isZero(statusId))
                    statusId = workCase.getStatus().getId();
            } else {
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
                wrkCasePreScreenId = workCasePrescreen.getId();
                requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
                if(Util.isZero(statusId))
                    statusId = workCasePrescreen.getStatus().getId();
            }

            if(Util.isTrue(requestAppraisalFlag)){
                WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByAppNumber(appNumber);
                wrkCaseAppraisalId = workCaseAppraisal.getId();
            }

            session.setAttribute("workCaseId", wrkCaseId);
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            session.setAttribute("workCaseAppraisalId", wrkCaseAppraisalId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
            session.setAttribute("wobNumber", wobNumber);
            session.setAttribute("statusId", statusId);
            session.setAttribute("fetchType", fetchType);
            session.setAttribute("stepId", stepId);
            session.setAttribute("stageId", stageId);
            session.setAttribute("caseOwner", caseOwner);
            session.setAttribute("queueName", queueName);

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, statusId, searchViewSelectItem.getApplicationno());
            session.setAttribute("appHeaderInfo", appHeaderView);

            String landingPage = inboxControl.getLandingPage(stepId, statusId);

            log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, statusId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, statusId, queueName);
            log.debug("landingPage : {}", landingPage);

            if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
                if(stepId == 1) {
                    stageId = getStageByStatusId(statusId, wrkCasePreScreenId, wrkCaseId);
                    session.setAttribute("stageId", stageId);
                    FacesUtil.redirect(landingPage);
                }else{
                    if(stageId == 201) {
                        FacesUtil.redirect("/site/basicInfo.jsf");
                    }else{
                        FacesUtil.redirect(landingPage);
                    }
                }
                return;
            } else {
                log.debug("onSelectInbox :: LANDING_PAGE_NOT_FOUND");
                message = "Can not find landing page for step [" + stepId + "], status ["+statusId+"]";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlgLanding.show()");
            }
        } catch (Exception e) {
            log.error("Error while opening case",e);
        }
    }

    private int getStageByStatusId(long statusId, long workCasePreScreenId, long workCaseId){
        int stageId = 201;
        if(statusId == StatusValue.CANCEL_CA.value()){
            if(!Util.isZero(workCaseId)){
                WorkCase tempWorkCase = workCaseDAO.findById(workCaseId);
                if(!Util.isNull(tempWorkCase))
                    if(!Util.isNull(tempWorkCase.getCancelStepId()))
                        if(!Util.isNull(tempWorkCase.getCancelStepId().getStage()))
                            stageId = tempWorkCase.getCancelStepId().getStage().getId();
            }else if(!Util.isZero(workCasePreScreenId)){
                WorkCasePrescreen tempWorkCasePreScreen = workCasePrescreenDAO.findById(workCasePreScreenId);
                if(!Util.isNull(tempWorkCasePreScreen))
                    if(!Util.isNull(tempWorkCasePreScreen.getCancelStepId()))
                        if(!Util.isNull(tempWorkCasePreScreen.getCancelStepId().getStage()))
                            stageId = tempWorkCasePreScreen.getCancelStepId().getStage().getId();
            }
        }else{
            if(statusId == StatusValue.REJECT_UW1.value() || statusId == StatusValue.CA_APPROVED_UW1.value() ||
                    statusId == StatusValue.REJECT_CA.value() || statusId == StatusValue.CA_APPROVED_ZM.value() ||
                        statusId == StatusValue.CA_APPROVED_UW2.value() || statusId == StatusValue.REJECT_UW2.value()){
                stageId = 201;
            }
        }
        return stageId;
    }

    public boolean checkAuthorizeRole(long workCasePreScreenId, long workCaseId, User user)
    {

        log.info("in Check Authorize Role : User :{}",user);

        List<Integer> workCaseOwnerRoles = new ArrayList<Integer>();
        if(workCasePreScreenId != 0)
            workCaseOwnerRoles = workCaseOwnerDAO.getWorkCaseRolesByWorkCasePreScreenId(workCasePreScreenId);
        else if(workCaseId != 0)
            workCaseOwnerRoles = workCaseOwnerDAO.getWorkCaseRolesByWorkCaseId(workCaseId);

        log.debug("workCaseOwner List : {}", workCaseOwnerRoles.toString());

        if(workCaseOwnerRoles.contains(user.getRole().getId()))
            return true;

        return false;
    }

    public boolean checkAuthorizeTeam(long workCasePreScreenId, long workCaseId, User user){
        List<ReassignTeamNameId> userTeamList = userTeamDAO.getUserteams(user.getTeam().getId(), "Y");
        Iterator<ReassignTeamNameId> it = userTeamList.iterator();
        List<String> usersList = new ArrayList<String>();

        while (it.hasNext()){
            ReassignTeamNameId reassignTeamNameId = it.next();
            List<String> users = userTeamDAO.getUsers(reassignTeamNameId.getTeamid());
            usersList.addAll(users);
        }

        if(!usersList.contains(user.getId()))
            usersList.add(user.getId());

        List<String> workCaseOwner = new ArrayList<String>();
        if(workCasePreScreenId != 0)
            workCaseOwner = workCaseOwnerDAO.getWorkCaseByWorkCasePrescreenId(workCasePreScreenId);
        else if(workCaseId != 0)
            workCaseOwner = workCaseOwnerDAO.getWorkCaseByWorkCaseId(workCaseId);

        log.debug("Users List work case : {}", usersList.toString());

        log.debug("workCaseOwner List : {}", workCaseOwner.toString());

        log.debug("Users List Size before : {}", usersList.size());

        usersList.retainAll(workCaseOwner);

        if(usersList.size() > 0)
            return true;

        return false;
    }

    public boolean checkAuthorizeWorkCaseOwner(long workCasePreScreenId, long workCaseId, String userId){
        int workCaseSize = workCaseOwnerDAO.findWorkCaseOwner(workCasePreScreenId, workCaseId, userId);
        log.debug("checkAuthorizeWorkCaseOwner ::: workCaseSize : {}", workCaseSize);

        if(workCaseSize > 0)
            return true;

        return false;
    }

    public Date getCurrentDate() {
        //log.debug("++++++++++++++++++++++++++++++++++=== Current Date : {}", DateTimeUtil.getCurrentDateTH());
        return DateTime.now().toDate();
        //return DateTimeUtil.getCurrentDate();
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }



    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getDate4() {
        return date4;
    }

    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCitizendid() {
        return citizendid;
    }

    public void setCitizendid(String citizendid) {
        this.citizendid = citizendid;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public List<PEInbox> getSearchViewList() {
        return searchViewList;
    }

    public void setSearchViewList(List<PEInbox> searchViewList) {
        this.searchViewList = searchViewList;
    }

    public PEInbox getSearchViewSelectItem() {
        return searchViewSelectItem;
    }

    public void setSearchViewSelectItem(PEInbox searchViewSelectItem) {
        this.searchViewSelectItem = searchViewSelectItem;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PEInbox> getInboxViewList() {
        return searchViewList;
    }

    public void setInboxViewList(List<PEInbox> inboxViewList) {
        this.searchViewList = inboxViewList;
    }

    public PEInbox getInboxViewSelectItem() {
        return searchViewSelectItem;
    }

    public void setInboxViewSelectItem(PEInbox inboxViewSelectItem) {
        this.searchViewSelectItem = inboxViewSelectItem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
