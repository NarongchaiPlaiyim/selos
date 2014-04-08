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
@ManagedBean(name = "peInbox")
public class PESQLInbox implements Serializable
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
    BPMInterfaceImpl bpmInterfaceImpl;

    @Inject
    StepDAO stepDAO;

    @Inject
    PEDBExecute pedbExecute;
    @Inject
    InboxControl inboxControl;
    @Inject
    HeaderControl headerControl;

    private List<PEInbox> inboxViewList;
    private PEInbox inboxViewSelectItem;
    private UserDetail userDetail;
    private String columnName;
    private String orderType;
    private String inboxName;
    private String message;
    private String messageHeader;

    @PostConstruct
    public void onCreation()
    {
        log.info("Controller in onCreation method of PESQLInbox.java ");

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("onCreation userDetail PESQLInbox.java : {}", userDetail);

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(true);

        try {
            if(session.getAttribute("isLocked") != null) {
                String isLocked = (String) session.getAttribute("isLocked");

                if(isLocked.equalsIgnoreCase("true")) {
                    if(session.getAttribute("wobNum")!=null && session.getAttribute("queueName")!=null && session.getAttribute("fetchType")!=null)
                    {
                        String wobNum = (String)session.getAttribute("wobNum");
                        bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNum,(Integer)session.getAttribute("fetchType"));
                    }

                } else {
                    session.removeAttribute("isLocked");
                }
            }

            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            try {
                log.debug("Request parameter is [id] : {}", request.getParameter("id"));
                inboxName =  request.getParameter("id") ;
                if(Util.isEmpty(inboxName)) inboxName = "My Box";
                inboxViewList =  pedbExecute.getPEInbox(inboxName);
                log.debug("onCreation ::: inboxViewList : {}", inboxViewList);
            } catch(Exception ex) {
                log.error("Exception while getInboxPE : ", ex);
                message = "Error while retrieve case list.";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            }
        } catch (Exception e) {
            log.error("Error while unlocking case in queue : {}, WobNum : {}",session.getAttribute("queueName"), session.getAttribute("wobNum"), e);
            message = "Error while unlocking case.";
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
        }

        //Clear all session
        session = FacesUtil.getSession(false);

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
        session.setAttribute("fetchType",0);
    }


    public void onSelectInbox() {
        HttpSession session = FacesUtil.getSession(false);

        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", inboxViewSelectItem);

        long stepId = inboxViewSelectItem.getStepId();
        String appNumber = inboxViewSelectItem.getApplicationno();
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long wrkCaseAppraisalId = 0L;
        long statusId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;
        String queueName = inboxViewSelectItem.getQueuename();

        try {
            //Try to Lock case
            bpmInterfaceImpl.lockCase(queueName,inboxViewSelectItem.getFwobnumber(),inboxViewSelectItem.getFetchType());
            session.setAttribute("isLocked","true");

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
                session.setAttribute("wobNum",inboxViewSelectItem.getFwobnumber());
            } else if (stepId == StepValue.REQUEST_APPRAISAL.value() || stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()) {     //For Case in Stage Parallel Appraisal
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
                session.setAttribute("wobNum",inboxViewSelectItem.getFwobnumber());
            }

            if(Util.isNull(inboxViewSelectItem.getFetchType())) {
                session.setAttribute("fetchType",0);
            } else {
                session.setAttribute("fetchType",inboxViewSelectItem.getFetchType());
            }

            if(stepId != 0){
                Step step = stepDAO.findById(stepId);
                stageId = step != null ? step.getStage().getId() : 0;
            }

            session.setAttribute("stepId", stepId);
            session.setAttribute("stageId", stageId);
            session.setAttribute("caseOwner",inboxViewSelectItem.getAtuser());

            if(Util.isNull(queueName)) {
                session.setAttribute("queueName", "0");
            } else {
                session.setAttribute("queueName", queueName);
            }

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, inboxViewSelectItem.getFwobnumber());
            session.setAttribute("appHeaderInfo", appHeaderView);

            String landingPage = inboxControl.getLandingPage(stepId);

            log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, queueName);

            if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
                FacesUtil.redirect(landingPage);
                return;
            } else {
                //TODO Show dialog
            }

        } catch (Exception e) {
            message = "Another User is Working on this case!! Please Retry Later.";
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            log.error("Error while Locking case in queue : {}, WobNum : {}",queueName, inboxViewSelectItem.getFwobnumber(), e);
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
            onSelectInbox();
        } catch (Exception ex){
            log.error("Exception while select case from PoolBox : ", ex);
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }
}
