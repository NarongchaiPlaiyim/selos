package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.ActionCode;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
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
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
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

    private String message;

    @Inject
    PEDBExecute pedbExecute;
    @Inject
    InboxControl inboxControl;
    @Inject
    HeaderControl headerControl;

    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

    public PESQLAllBoxes() { }

    @PostConstruct
    public void onCreation()
    {
        log.debug("Controller in onCreation method of PESQLAllBoxes.java ");

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(false);
        try
        {
            if(session.getAttribute("stepId")!=null)
            {

                //String isLocked = (String) session.getAttribute("isLocked");

                if((Long)session.getAttribute("stepId") !=0 && session.getAttribute("wobNumber")!=null && session.getAttribute("queueName")!=null && session.getAttribute("fetchType")!=null)
                {
                    String wobNumber = (String)session.getAttribute("wobNumber");
                    log.info("unlocking case queue: {}, wobNumber : {}, fetchType: {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"),session.getAttribute("fetchType"));
                    bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNumber,(Integer)session.getAttribute("fetchType"));
                }
                /*else
                {
                    session.removeAttribute("isLocked");
                }*/

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
        session.setAttribute("queueName","");
        session.removeAttribute("wobNumber");

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
        String appNumber = inboxViewSelectItem.getApplicationno();
        long wrkCasePreScreenId = 0L;
        long wrkCaseAppraisalId = 0L;
        long wrkCaseId = 0L;
        int stageId = 0;
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
            session.setAttribute("wobNumber",inboxViewSelectItem.getFwobnumber());
        } else if (stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()) {     //For Parallel Appraisal
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
                wrkCaseAppraisalId = workCaseAppraisal.getId();
                session.setAttribute("workCaseAppraisalId", wrkCaseAppraisalId);
            }
        } else {
            WorkCase workCase = workCaseDAO.findByWobNumber(inboxViewSelectItem.getFwobnumber());
            if(workCase != null){
                wrkCaseId = workCase.getId();
                requestAppraisalFlag = workCase.getRequestAppraisal();
            }
            session.setAttribute("workCaseId", wrkCaseId);
            session.setAttribute("requestAppraisal", requestAppraisalFlag);
            session.setAttribute("wobNumber",inboxViewSelectItem.getFwobnumber());
        }

        session.setAttribute("stepId", inboxViewSelectItem.getStepId());
        session.setAttribute("statusId", Util.parseLong(inboxViewSelectItem.getStatuscode(), 0));

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

        if(Util.isNull(inboxViewSelectItem.getFetchType()))
        {
            session.setAttribute("fetchType",0);
        }
        else
        {
            session.setAttribute("fetchType",inboxViewSelectItem.getFetchType());
        }

        String caseOwner = inboxViewSelectItem.getAtuser();

        caseOwner = (caseOwner.contains("-")?caseOwner.substring(0,caseOwner.indexOf("-")).trim():caseOwner);

        AppHeaderView appHeaderView = headerControl.getHeaderInformation(inboxViewSelectItem.getStepId(), Util.parseLong(inboxViewSelectItem.getStatuscode(), 0), inboxViewSelectItem.getFwobnumber());
        session.setAttribute("caseOwner",caseOwner);

        try
        {

            log.info("locking case queue: {}, wobNumber : {}, fetchtype: {}",queueName, inboxViewSelectItem.getFwobnumber(),inboxViewSelectItem.getFetchType());
            bpmInterfaceImpl.lockCase(queueName,inboxViewSelectItem.getFwobnumber(),inboxViewSelectItem.getFetchType());
            //session.setAttribute("isLocked","true");

        }
        catch (Exception e)
        {
            //TODO Alert Box
            log.error("Error while Locking case in queue : {}, wobNumber : {}",queueName, inboxViewSelectItem.getFwobnumber(), e);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            return;
        }

        session.setAttribute("appHeaderInfo", appHeaderView);

        String landingPage = inboxControl.getLandingPage(stepId,Util.parseLong(inboxViewSelectItem.getStatuscode(), 0));

        log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, queueName);

        if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
            FacesUtil.redirect(landingPage);
            return;
        } else {
            //TODO Show dialog
        }
    }

    public void onClickPickUpCase(){
        RequestContext.getCurrentInstance().execute("msgBoxConfirmDlg.show()");
    }

    public void onPickUpCase(){
        try{
            //TODO dispatch for Select case
            String queueName = inboxViewSelectItem.getQueuename();
            String wobNumber = inboxViewSelectItem.getFwobnumber();
            inboxControl.selectCasePoolBox(queueName, wobNumber, ActionCode.ASSIGN_TO_ME.getVal());
            //TODO Reload all value for Inbox Select
            //onSelectInbox();
        } catch (Exception ex){
            log.error("Exception while select case from PoolBox : ", ex);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
