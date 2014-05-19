package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.PERoster;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@ManagedBean(name = "trackBean")
public class TrackcaBean implements Serializable
{

    @Inject
    @SELOS
    Logger log;

    @Inject
    PEDBExecute pedbExecute;

    @Inject
    ActionDAO actionDAO;

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
    InboxControl inboxControl;
    @Inject
    HeaderControl headerControl;

    private List<PERoster> rosterViewList;

    private PERoster rosterViewSelectItem;

    private String statusType;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PERoster> getRosterViewList() {
        return rosterViewList;
    }

    public void setRosterViewList(List<PERoster> rosterViewList) {
        this.rosterViewList = rosterViewList;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public PERoster getRosterViewSelectItem() {
        return rosterViewSelectItem;
    }

    public void setRosterViewSelectItem(PERoster rosterViewSelectItem) {
        this.rosterViewSelectItem = rosterViewSelectItem;
    }

    @PostConstruct
    public void onCreation()
    {
        statusType = "CreatedByMe";

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("#{trackBean}");

        log.info("in controller");

    }

    public List<PERoster> peRosterQueryForTrackCa()
    {
        try
        {

            String description = "";

            log.info("before call the method::::");

            description = actionDAO.getDescripationFromAction();

            rosterViewList =  pedbExecute.getRosterQuery(statusType,description);

            log.info("List Size : {}",rosterViewList.size());

        }

        catch(Exception e)
        {

            log.error("Error im track Ca *** ",e);

        }

        log.info("rosterViewList is : {}",rosterViewList.size());

        return  rosterViewList;

    }

    public void onSelectInbox()
    {

        log.info("*** in onSelectInbox of Track CA ***");

        HttpSession session = FacesUtil.getSession(false);

        log.info("onSelectInbox ::: setSession ");
        log.info("onSelectInbox ::: inboxViewSelectItem : {}", rosterViewSelectItem);

        long stepId = rosterViewSelectItem.getStepId();
        String appNumber = rosterViewSelectItem.getAppNumber();
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long wrkCaseAppraisalId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;
        String queueName = rosterViewSelectItem.getQueuename();

        try {

            try{
                //Try to Lock case
                log.info("locking case queue: {}, WobNum : {}, fetchtype: {}",queueName, rosterViewSelectItem.getF_WobNum(),rosterViewSelectItem.getFetchType());
                bpmInterfaceImpl.lockCase(queueName,rosterViewSelectItem.getF_WobNum(),rosterViewSelectItem.getFetchType());
                    /*session.setAttribute("isLocked","true");*/
            } catch (Exception e) {
                log.error("Error while Locking case in queue : {}, WobNum : {}",queueName, rosterViewSelectItem.getF_WobNum(), e);
                message = "Another User is Working on this case!! Please Retry Later.";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            }

            if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value()) {     //For Case in Stage PreScreen
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
                if(workCasePrescreen != null){
                    wrkCasePreScreenId = workCasePrescreen.getId();
                    requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
                }
                session.setAttribute("workCasePreScreenId", wrkCasePreScreenId);
                session.setAttribute("requestAppraisal", requestAppraisalFlag);

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
                    wrkCaseAppraisalId = workCaseAppraisal.getId();
                    session.setAttribute("workCaseAppraisalId", wrkCaseAppraisalId);
                }
            } else {        //For Case in Stage FullApplication
                WorkCase workCase = workCaseDAO.findByAppNumber(appNumber);
                if(workCase != null){
                    wrkCaseId = workCase.getId();
                    requestAppraisalFlag = workCase.getRequestAppraisal();
                }
                session.setAttribute("workCaseId", wrkCaseId);
                session.setAttribute("requestAppraisal", requestAppraisalFlag);
            }

            session.setAttribute("wobNumber", rosterViewSelectItem.getF_WobNum());
            session.setAttribute("statusId", Util.parseLong(rosterViewSelectItem.getStatusCode(), 0));

            if(Util.isNull(rosterViewSelectItem.getFetchType())) {
                session.setAttribute("fetchType",0);
            } else {
                session.setAttribute("fetchType",rosterViewSelectItem.getFetchType());
            }

            if(stepId != 0){
                Step step = stepDAO.findById(stepId);
                stageId = step != null ? step.getStage().getId() : 0;
            }

            session.setAttribute("stepId", stepId);
            session.setAttribute("stageId", stageId);
            session.setAttribute("caseOwner",rosterViewSelectItem.getAtUser());

            if(Util.isNull(queueName)) {
                session.setAttribute("queueName", "0");
            } else {
                session.setAttribute("queueName", queueName);
            }

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, rosterViewSelectItem.getAppNumber());
            session.setAttribute("appHeaderInfo", appHeaderView);

            String landingPage = inboxControl.getLandingPage(stepId,Util.parseLong(rosterViewSelectItem.getStatusCode(), 0));

            log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, queueName);

            if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
                FacesUtil.redirect(landingPage);
                //return;
            } else {
                log.debug("onSelectInbox :: LANDING_PAGE_NOT_FOUND");
                FacesUtil.redirect("/site/inbox.jsf");
            }

        } catch (Exception e) {
            //log.error("Error while Locking case in queue : {}, WobNum : {}",queueName, rosterViewSelectItem.getFwobnumber(), e);
            //message = "Another User is Working on this case!! Please Retry Later.";
            //RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            log.error("Error while opening case",e);
        }
    }

}