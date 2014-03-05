package com.clevel.selos.controller;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import com.clevel.selos.model.view.PEInbox;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "peinbox")
public class PESQLInbox implements Serializable
{
    @Inject
    @SELOS
    Logger log;

    private List<PEInbox> inboxViewList;

    private PEInbox inboxViewSelectItem;

    private UserDetail userDetail;

    private String columnName;

    private String orderType;



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
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

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

    public PESQLInbox()
    {
    }

    @PostConstruct
    public void onCreation()
    {

        log.info("controler in onCreation method of pesqlinbox.java ");

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("onCreation userDetail pesqlinbox.java : {}", userDetail);

        try
        {
            String inboxname = "My Box";

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

       /* if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCasePreScreenId())))
        {
            session.setAttribute("workCasePreScreenId", inboxViewSelectItem.getWorkCasePreScreenId());
        }

        else
        {
            session.setAttribute("workCasePreScreenId", 0);
        }

        if(!Util.isEmpty(Long.toString(inboxViewSelectItem.getWorkCaseId())))
        {
            session.setAttribute("workCaseId", inboxViewSelectItem.getWorkCaseId());
        }

        else
        {
            session.setAttribute("workCaseId", 0);
        }*/

        long stepId = inboxViewSelectItem.getStepId();

        long wrkCasePreScreenId;

        long wrkCaseId;

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {

            wrkCasePreScreenId = workCasePrescreenDAO.findIdByWobNumber(inboxViewSelectItem.getFwobnumber());
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            //session.setAttribute("workCaseId", 0);

        }

        else
        {

            wrkCaseId = workCaseDAO.findIdByWobNumber(inboxViewSelectItem.getFwobnumber());
            session.setAttribute("workCaseId", wrkCaseId);
            //session.setAttribute("workCasePreScreenId", 0);

        }

        session.setAttribute("stepId", inboxViewSelectItem.getStep());
        if(inboxViewSelectItem.getQueuename() == null)
        {
            session.setAttribute("queueName","0");
        }

        else
        {
            session.setAttribute("queueName",inboxViewSelectItem.getQueuename());
        }



        //AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(inboxViewSelectItem.getWorkCasePreScreenId(), inboxViewSelectItem.getWorkCaseId());
        AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(inboxViewSelectItem.getStep(), inboxViewSelectItem.getFwobnumber());
        session.setAttribute("appHeaderInfo", appHeaderView);

        long selectedStepId = Long.parseLong(inboxViewSelectItem.getStep());
        String landingPage = pedbExecute.getLandingPage(selectedStepId);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {
            //TODO Show dialog
        }

    }


}
