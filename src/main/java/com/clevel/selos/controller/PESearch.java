package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.master.UserTeamDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCaseOwnerDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.model.view.ReassignTeamNameId;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    Logger log;

    @Inject
    PEDBExecute pedbExecute;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

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


    public PESearch()
    {
        setStatusType(statusType);

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

    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    UserTeamDAO userTeamDAO;

    @PostConstruct
    public void onCreation()
    {
        log.debug("Controller in onCreation method of PESearch.java ");

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);
        try
        {
            if(session.getAttribute("isLocked")!=null)
            {

                String isLocked = (String) session.getAttribute("isLocked");

                if(isLocked.equalsIgnoreCase("true"))
                {
                    String wobNum = (String)session.getAttribute("wobNum");
                    bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNum,(Integer)session.getAttribute("fetchType"));
                }
                else
                {
                    session.removeAttribute("isLocked");
                }

            }
        }
        catch (Exception e)
        {

            log.error("Error while unlocking case in queue : {}, WobNum : {}",session.getAttribute("queueName"), session.getAttribute("wobNum"), e);
        }

        session.setAttribute("workCasePreScreenId", 0L);
        session.setAttribute("workCaseId", 0L);
        session.setAttribute("stepId", 0L);
        session.setAttribute("statusId", 0L);
        session.setAttribute("stageId", 0);
        session.setAttribute("requestAppraisal", 0);
        session.setAttribute("queueName","");
        session.removeAttribute("wobNum");

    }

    public List<PEInbox> search()
    {
        log.info(" Controller comes to PESearch method of search class ");

        try
        {
            searchViewList = pedbExecute.getPESearchList(statusType,applicationNumber,userid,step,status,citizendid,firstname,lastname,date1,date2,date3,date4);

            log.info("searchViewList size is : {}",searchViewList.size());



        }
        catch (Exception e)
        {
            log.error("Error in search() of PESearch",e);
        }
        finally
        {
        }
        return searchViewList;
    }

    public void onSelectInbox() {

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("userDetails  : "+userDetail);

        if(userDetail == null)
        {
            FacesUtil.redirect("/login.jsf");
            return;
        }

        User user = userDAO.findByUserName(userDetail.getUserName());

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);

        long stepId = searchViewSelectItem.getStepId();

        long wrkCasePreScreenId;

        long wrkCaseId;

        WorkCase workCase = workCaseDAO.findByWobNumber(searchViewSelectItem.getFwobnumber());

        if(workCase == null)
        {

            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(searchViewSelectItem.getFwobnumber());

            log.info("in prescreen if. case owner");

            if(userDetail.getRoleId() == RoleValue.GH.id() || userDetail.getRoleId() == RoleValue.CSSO.id()){}

            else if(userDetail.getRoleId() == RoleValue.BDM.id())
            {

                if(workCasePrescreen.getCreateBy().getUserName().equalsIgnoreCase(userDetail.getUserName())){}

                else
                {

                    //TODO Alert Box
                    log.info("You are not authorised to view this case.(BDM)");
                    FacesUtil.redirect("/site/generic_search.jsf");
                    return;
                }

            }

            else
            {

                if(user.getTeam().getTeam_type() == 4 || user.getTeam().getTeam_type() == 5){}

                else if(user.getTeam().getTeam_type() == 3 || user.getTeam().getTeam_type() == 2)
                {

                    List userTeamList = userTeamDAO.getUserteams(userDetail.getTeamid(),"Y");

                    Iterator<ReassignTeamNameId> it = userTeamList.iterator();

                    List<String> usersList = new ArrayList<String>();

                    while (it.hasNext())
                    {

                        ReassignTeamNameId reassignTeamNameId = it.next();

                        List<String> users = userTeamDAO.getUsers(reassignTeamNameId.getTeamid());

                        usersList.addAll(users);


                    }

                    List workCaseOwnerUsersList = workCaseOwnerDAO.getWorkCaseByWorkCasePrescreenId(new Long(workCasePrescreen.getId()).intValue());

                    log.info("Users List : "+usersList.toString());

                    log.info("WorkCaseOwnerUsers List :"+workCaseOwnerUsersList.toString());

                    log.info("Users List Size before"+usersList.size());

                    usersList.retainAll(workCaseOwnerUsersList);

                    log.info("Users List Size "+usersList.size());

                    if(usersList.size()>0){}

                    else
                    {
                        //TODO Alert Box
                        log.info("You are not authorised to view this case.(Team type 3,2)");
                        FacesUtil.redirect("/site/generic_search.jsf");
                        return;
                    }

                }

                else
                {

                    List workCaseOwnerUsersList = workCaseOwnerDAO.getWorkCaseByWorkCasePrescreenId(new Long(workCasePrescreen.getId()).intValue());

                    if(workCaseOwnerUsersList.contains(userDetail.getUserName())){}

                    else
                    {
                        //TODO Alert Box
                        log.info("You are not authorised to view this case.");
                        FacesUtil.redirect("/site/generic_search.jsf");
                        return;
                    }

                }

            }
        }

        else
        {
            if(userDetail.getRoleId() == RoleValue.GH.id() || userDetail.getRoleId() == RoleValue.CSSO.id()){}

            else if(userDetail.getRoleId() == RoleValue.BDM.id())
            {

                if(workCase.getCreateBy().getUserName().equalsIgnoreCase(userDetail.getUserName())){}

                else
                {
                    //TODO Alert Box
                    log.info("You are not authorised to view this case.");
                    FacesUtil.redirect("/site/generic_search.jsf");
                    return;
                }

            }

            else
            {

                if(user.getTeam().getTeam_type() == 4 || user.getTeam().getTeam_type() == 5){}

                else if(user.getTeam().getTeam_type() == 3 || user.getTeam().getTeam_type() == 2)
                {

                    List userTeamList = userTeamDAO.getUserteams(userDetail.getTeamid(),"Y");

                    Iterator<ReassignTeamNameId> it = userTeamList.iterator();

                    List<String> usersList = new ArrayList<String>();

                    while (it.hasNext())
                    {

                        ReassignTeamNameId reassignTeamNameId = it.next();

                        List<String> users = userTeamDAO.getUsers(reassignTeamNameId.getTeamid());

                        usersList.addAll(users);


                    }

                    List workCaseOwnerUsersList = workCaseOwnerDAO.getWorkCaseByWorkCaseId(new Long(workCase.getId()).intValue());

                    usersList.retainAll(workCaseOwnerUsersList);

                    if(usersList.size()>0){}

                    else
                    {
                        //TODO Alert Box
                        log.info("You are not authorised to view this case.");
                        FacesUtil.redirect("/site/generic_search.jsf");
                        return;
                    }

                }

                else
                {

                    List workCaseOwnerUsersList = workCaseOwnerDAO.getWorkCaseByWorkCaseId(new Long(workCase.getId()).intValue());

                    if(workCaseOwnerUsersList.contains(userDetail.getUserName())){}

                    else
                    {
                        //TODO Alert Box
                        log.info("You are not authorised to view this case.");
                        FacesUtil.redirect("/site/generic_search.jsf");
                        return;
                    }

                }

            }
        }

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {

            wrkCasePreScreenId = workCasePrescreenDAO.findIdByWobNumber(searchViewSelectItem.getFwobnumber());
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            log.info("Work case pre screen id : {}",wrkCasePreScreenId);
            //session.setAttribute("workCaseId", 0);
            session.setAttribute("wobNum",searchViewSelectItem.getFwobnumber());
        }

        else
        {

            wrkCaseId = workCaseDAO.findIdByWobNumber(searchViewSelectItem.getFwobnumber());
            session.setAttribute("workCaseId", wrkCaseId);
            //session.setAttribute("workCasePreScreenId", 0);
            session.setAttribute("wobNum",searchViewSelectItem.getFwobnumber());

        }

        if(Util.isNull(searchViewSelectItem.getFetchType()))
        {
            session.setAttribute("fetchType",0);
        }
        else
        {
            session.setAttribute("fetchType",searchViewSelectItem.getFetchType());
        }

        /*if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCasePreScreenId()))){
            session.setAttribute("workCasePreScreenId", inboxViewSelectItem.getWorkCasePreScreenId());
        } else {
            session.setAttribute("workCasePreScreenId", 0);
        }
        if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCaseId()))){
            session.setAttribute("workCaseId", inboxViewSelectItem.getWorkCaseId());
        } else {
            session.setAttribute("workCaseId", 0);
        }*/

        session.setAttribute("stepId", searchViewSelectItem.getStepId());
        session.setAttribute("caseOwner",searchViewSelectItem.getAtuser());

        if(searchViewSelectItem.getQueuename() == null)
        {
            session.setAttribute("queueName","0");
        }

        else
        {
            session.setAttribute("queueName",searchViewSelectItem.getQueuename());
        }

        try
        {

            bpmInterfaceImpl.lockCase(searchViewSelectItem.getQueuename(),searchViewSelectItem.getFwobnumber(),searchViewSelectItem.getFetchType());
            session.setAttribute("isLocked","true");

        }
        catch (Exception e)
        {
            log.error("Error while Locking case in queue : {}, WobNum : {}",searchViewSelectItem.getQueuename(),searchViewSelectItem.getFwobnumber(), e);
            FacesUtil.redirect("/site/generic_search.jsf");
            return;
        }

        AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(searchViewSelectItem.getStepId(), searchViewSelectItem.getFwobnumber());
        session.setAttribute("appHeaderInfo", appHeaderView);


        long selectedStepId = searchViewSelectItem.getStepId();
        String landingPage = pedbExecute.getLandingPage(selectedStepId);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {

        }

    }

}
