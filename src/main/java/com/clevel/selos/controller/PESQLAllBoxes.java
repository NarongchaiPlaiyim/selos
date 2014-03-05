package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
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
import java.util.List;

@ViewScoped
@ManagedBean(name = "peAllBoxes")
public class PESQLAllBoxes implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    private List<PEInbox> inboxViewList;

    private PEInbox inboxViewSelectItem;

    private UserDetail userDetail;

    private String columnName;

    private String orderType;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    private String inboxname;

    public String getInboxname() {
        return inboxname;
    }

    public void setInboxname(String inboxname) {
        this.inboxname = inboxname;
    }



    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }



    @Inject
    PEDBExecute pedbExecute;

    public List<PEInbox> getInboxViewList() {
        return inboxViewList;
    }

    public void setInboxViewList(List<PEInbox> inboxViewList) {
        this.inboxViewList = inboxViewList;
    }

    public PEInbox getInboxViewSelectItem() {
        return inboxViewSelectItem;
    }

    public void setInboxViewSelectItem(PEInbox inboxViewSelectItem) {
        this.inboxViewSelectItem = inboxViewSelectItem;
    }

    public PESQLAllBoxes()
    {
    }

    @PostConstruct
    public void onCreation()
    {

        try
        {
            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

            log.info("request parameter is : {}",request.getParameter("id"));

             inboxname =  request.getParameter("id") ;

            log.info("controler in onCreation method of pesqlinbox.java ");


            inboxViewList =  pedbExecute.getPEInbox(inboxname);

            log.debug("onCreation ::: inboxViewList : {}", inboxViewList);

            //inboxViewList.clear();


        }
        catch(Exception e)
        {

        }
        finally
        {


        }
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
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", inboxViewSelectItem);

        long stepId = inboxViewSelectItem.getStepId();

        long wrkCasePreScreenId;

        long wrkCaseId;

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {

            wrkCasePreScreenId = workCasePrescreenDAO.findIdByWobNumber(inboxViewSelectItem.getFwobnumber());
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            log.info("Work case pre screen id : {}",wrkCasePreScreenId);
            //session.setAttribute("workCaseId", 0);

        }

        else
        {

            wrkCaseId = workCaseDAO.findIdByWobNumber(inboxViewSelectItem.getFwobnumber());
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

        session.setAttribute("stepId", inboxViewSelectItem.getStepId());
        if(inboxViewSelectItem.getQueuename() == null)
        {
            session.setAttribute("queueName","0");
        }

        else
        {
            session.setAttribute("queueName",inboxViewSelectItem.getQueuename());
        }

        AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(inboxViewSelectItem.getStepId(), inboxViewSelectItem.getFwobnumber());
        session.setAttribute("appHeaderInfo", appHeaderView);


        long selectedStepId = inboxViewSelectItem.getStepId();
        String landingPage = pedbExecute.getLandingPage(selectedStepId);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {

        }

    }



}
