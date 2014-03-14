package com.clevel.selos.controller;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
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
    private String inboxName;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    PEDBExecute pedbExecute;

    @PostConstruct
    public void onCreation()
    {

        log.info("Controller in onCreation method of PESQLInbox.java ");

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("onCreation userDetail PESQLInbox.java : {}", userDetail);

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCasePreScreenId", 0);
        session.setAttribute("workCaseId", 0);
        session.setAttribute("requestAppraisal", 0);

        try
        {
            inboxName = "My Box";
            inboxViewList =  pedbExecute.getPEInbox(inboxName);
            log.debug("onCreation ::: inboxViewList : {}", inboxViewList);
        }
        catch(Exception ex)
        {
            log.error("Exception while getInboxPE : ", ex);
        }
    }


    public void onSelectInbox() {

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("onSelectInbox ::: userDetail  : {}", userDetail);

        /*if(Util.isNull(userDetail))
        {
            FacesUtil.redirect("/login.jsf");
            return;
        }*/

        HttpSession session = FacesUtil.getSession(false);
        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", inboxViewSelectItem);

        long stepId = inboxViewSelectItem.getStepId();
        long wrkCasePreScreenId = 0;
        long wrkCaseId = 0;
        int requestAppraisalFlag = 0;

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value())
        {
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(inboxViewSelectItem.getFwobnumber());
            if(workCasePrescreen != null){
                wrkCasePreScreenId = workCasePrescreen.getId();
                requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
            }
            session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
        }

        else
        {
            WorkCase workCase = workCaseDAO.findByWobNumber(inboxViewSelectItem.getFwobnumber());
            if(workCase != null){
                wrkCaseId = workCase.getId();
                requestAppraisalFlag = workCase.getRequestAppraisal();
            }
            session.setAttribute("workCaseId", wrkCaseId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
        }

        session.setAttribute("stepId", stepId);

        String queueName = inboxViewSelectItem.getQueuename();
        if(Util.isNull(queueName)) {
            session.setAttribute("queueName", "0");
        } else {
            session.setAttribute("queueName", inboxViewSelectItem.getQueuename());
        }

        AppHeaderView appHeaderView = pedbExecute.getHeaderInformation(stepId, inboxViewSelectItem.getFwobnumber());
        session.setAttribute("appHeaderInfo", appHeaderView);

        String landingPage = pedbExecute.getLandingPage(stepId);

        log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, requestAppraisalFlag, stepId, queueName);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {
            //TODO Show dialog
        }
    }

    public String getInboxName() {
        return inboxName;
    }

    public void setInboxName(String inboxName) {
        this.inboxName = inboxName;
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
