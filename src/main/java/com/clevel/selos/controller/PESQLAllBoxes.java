package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
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

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    StepDAO stepDAO;

    private List<PEInbox> inboxViewList;
    private PEInbox inboxViewSelectItem;
    private UserDetail userDetail;
    private String columnName;
    private String orderType;
    private String inboxname;

    @Inject
    PEDBExecute pedbExecute;

    public PESQLAllBoxes() { }

    @PostConstruct
    public void onCreation()
    {
        log.debug("Controller in onCreation method of PESQLAllBoxes.java ");

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCasePreScreenId", 0L);
        session.setAttribute("workCaseId", 0L);
        session.setAttribute("stepId", 0L);
        session.setAttribute("statusId", 0L);
        session.setAttribute("stageId", 0);
        session.setAttribute("requestAppraisal", 0);
        session.setAttribute("queueName","");

        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try
        {
            log.debug("Request parameter is [id] : {}", request.getParameter("id"));
            inboxname =  request.getParameter("id") ;
            inboxViewList =  pedbExecute.getPEInbox(inboxname);
            log.debug("onCreation ::: inboxViewList : {}", inboxViewList);
        }
        catch(Exception ex)
        {
            log.error("Exception while getInboxPE : ", ex);
        }
    }


    public void onSelectInbox() {

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("userDetails  : "+userDetail);

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", inboxViewSelectItem);

        long stepId = inboxViewSelectItem.getStepId();
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long statusId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(inboxViewSelectItem.getFwobnumber());
            if(workCasePrescreen != null){
                wrkCasePreScreenId = workCasePrescreen.getId();
                requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
                statusId = workCasePrescreen.getStatus().getId();
            }
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
            session.setAttribute("statusId", statusId);
        } else {
            WorkCase workCase = workCaseDAO.findByWobNumber(inboxViewSelectItem.getFwobnumber());
            if(workCase != null){
                wrkCaseId = workCase.getId();
                requestAppraisalFlag = workCase.getRequestAppraisal();
                statusId = workCase.getStatus().getId();
            }
            session.setAttribute("workCaseId", wrkCaseId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
            session.setAttribute("statusId", statusId);
        }

        session.setAttribute("stepId", inboxViewSelectItem.getStepId());

        if(stepId != 0){
            Step step = stepDAO.findById(stepId);
            stageId = step != null ? step.getStage().getId() : 0;
        }

        session.setAttribute("stageId", stageId);

        String queueName = inboxViewSelectItem.getQueuename();
        if(Util.isNull(queueName)) {
            session.setAttribute("queueName", "0");
        } else {
            session.setAttribute("queueName", inboxViewSelectItem.getQueuename());
        }

        AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(inboxViewSelectItem.getStepId(), inboxViewSelectItem.getFwobnumber());
        session.setAttribute("appHeaderInfo", appHeaderView);

        String landingPage = pedbExecute.getLandingPage(stepId);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {
            //TODO Show dialog
        }
    }

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

}
