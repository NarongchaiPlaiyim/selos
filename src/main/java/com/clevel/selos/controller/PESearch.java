package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PEInbox;
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
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: searchViewSelectItem : {}", searchViewSelectItem);

        long stepId = searchViewSelectItem.getStepId();

        long wrkCasePreScreenId;

        long wrkCaseId;

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {

            wrkCasePreScreenId = workCasePrescreenDAO.findIdByWobNumber(searchViewSelectItem.getFwobnumber());
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            log.info("Work case pre screen id : {}",wrkCasePreScreenId);
            //session.setAttribute("workCaseId", 0);

        }

        else
        {

            wrkCaseId = workCaseDAO.findIdByWobNumber(searchViewSelectItem.getFwobnumber());
            session.setAttribute("workCaseId", wrkCaseId);
            //session.setAttribute("workCasePreScreenId", 0);

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
        if(searchViewSelectItem.getQueuename() == null)
        {
            session.setAttribute("queueName","0");
        }

        else
        {
            session.setAttribute("queueName",searchViewSelectItem.getQueuename());
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
