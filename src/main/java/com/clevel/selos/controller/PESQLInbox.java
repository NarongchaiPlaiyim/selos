package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.WorkCaseAppraisalDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.filenet.bpm.util.constants.BPMConstants;
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
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

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

    private Map<String, Boolean> checked =  new HashMap<String, Boolean>();

    @Inject
    @Config(name = "interface.pe.rosterName")
    String peRosterName;

    @Inject
    BPMInterfaceImpl bpmInterfaceImpl;

    private boolean disableAssign = true;

    public boolean isDisableAssign() {
        return disableAssign;
    }

    public void setDisableAssign(boolean disableAssign) {
        this.disableAssign = disableAssign;
    }

    public Map<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<String, Boolean> checked) {
        this.checked = checked;
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

    @PostConstruct
    public void onCreation()
    {
        log.info("Controller in onCreation method of PESQLInbox.java ");

        userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("#{peInbox}");

        log.info("onCreation userDetail PESQLInbox.java : {}", userDetail);

        //Clear all session before selectInbox
        HttpSession session = FacesUtil.getSession(true);

        try {

            if(session.getAttribute("stepId")!=null)
            {

                //String isLocked = (String) session.getAttribute("isLocked");

                if((Long)session.getAttribute("stepId") != StepValue.COMPLETED_STEP.value() && session.getAttribute("wobNumber")!=null && session.getAttribute("queueName")!=null && session.getAttribute("fetchType")!=null)
                {
                    String wobNumber = (String)session.getAttribute("wobNumber");
                    String queueName = (String)session.getAttribute("queueName");
                    if(wobNumber.trim().length()!=0 || queueName.trim().length()!=0)
                    {
                        log.info("unlocking case queue: {}, wobNumber : {}, fetchType: {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"),session.getAttribute("fetchType"));
                        bpmInterfaceImpl.unLockCase((String)session.getAttribute("queueName"),wobNumber,(Integer)session.getAttribute("fetchType"));
                    }
                }
                /*else
                {
                    session.removeAttribute("isLocked");
                }*/

            }

        } catch (Exception e) {
            log.error("Error while unlocking case in queue : {}, WobNum : {}",session.getAttribute("queueName"), session.getAttribute("wobNumber"), e);
            message = "Error while unlocking case.";
            //RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
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

        long stepId = Util.parseLong(inboxViewSelectItem.getStepId(), 0);
        long statusId = Util.parseLong(inboxViewSelectItem.getStatuscode(), 0);
        long wrkCasePreScreenId = 0L;
        long wrkCaseId = 0L;
        long wrkCaseAppraisalId = 0L;
        int stageId = 0;
        int requestAppraisalFlag = 0;
        int fetchType = Util.parseInt(inboxViewSelectItem.getFetchType(), 0);
        String appNumber = Util.parseString(inboxViewSelectItem.getApplicationno(), "");
        String queueName = Util.parseString(inboxViewSelectItem.getQueuename(), "0");
        String wobNumber = Util.parseString(inboxViewSelectItem.getFwobnumber(), "");
        String caseOwner = Util.parseString(inboxViewSelectItem.getAtuser(), "");

        try {

            try{
                //Try to Lock case
                log.info("locking case queue: {}, WobNum : {}, fetchtype: {}",queueName, inboxViewSelectItem.getFwobnumber(),inboxViewSelectItem.getFetchType());
                bpmInterfaceImpl.lockCase(queueName, wobNumber, inboxViewSelectItem.getFetchType());
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

            WorkCase workCase = workCaseDAO.findByAppNumber(appNumber);
            if(!Util.isNull(workCase)){
                wrkCaseId = workCase.getId();
                requestAppraisalFlag = workCase.getRequestAppraisal();
            } else {
                WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
                wrkCasePreScreenId = workCasePrescreen.getId();
                requestAppraisalFlag = workCasePrescreen.getRequestAppraisal();
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

            AppHeaderView appHeaderView = headerControl.getHeaderInformation(stepId, statusId, inboxViewSelectItem.getApplicationno());
            session.setAttribute("appHeaderInfo", appHeaderView);

            String landingPage = inboxControl.getLandingPage(stepId,Util.parseLong(inboxViewSelectItem.getStatuscode(), 0));

            log.debug("onSelectInbox ::: workCasePreScreenId : {}, workCaseId : {}, workCaseAppraisalId : {}, requestAppraisal : {}, stepId : {}, queueName : {}", wrkCasePreScreenId, wrkCaseId, wrkCaseAppraisalId, requestAppraisalFlag, stepId, queueName);

            if(!landingPage.equals("") && !landingPage.equals("LANDING_PAGE_NOT_FOUND")){
                FacesUtil.redirect(landingPage);
                return;
            } else {
                log.debug("onSelectInbox :: LANDING_PAGE_NOT_FOUND");
                message = "Can not find landing page for step [" + stepId + "]";
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            }

            /*if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value()) {     //For Case in Stage PreScreen
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
            }*/
        } catch (Exception e) {
            //log.error("Error while Locking case in queue : {}, WobNum : {}",queueName, inboxViewSelectItem.getFwobnumber(), e);
            //message = "Another User is Working on this case!! Please Retry Later.";
            //RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            log.error("Error while opening case",e);
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
            log.debug("onPickUpCase ::: inboxViewSelectItem : {}", inboxViewSelectItem);
            inboxControl.selectCasePoolBox(queueName, wobNumber, ActionCode.ASSIGN_TO_ME.getVal());
            //TODO Reload all value for Inbox Select
            inboxViewSelectItem = inboxControl.getNextStep(inboxViewSelectItem, ActionCode.ASSIGN_TO_ME.getVal());
            log.debug("onPickUpCase ::: find next step : {}", inboxViewSelectItem);
            onSelectInbox();
        } catch (Exception ex){
            log.error("Exception while select case from PoolBox : ", ex);
            message = Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
        }

    }

    public void checkAssign(AjaxBehaviorEvent ajaxBehaviorEvent)
    {
        UIComponent source = (UIComponent)ajaxBehaviorEvent.getSource();

        log.info("UIComponent source : {}",source);

        if(source!= null)
        {
            log.info("Value:" + ((SelectBooleanCheckbox) source).getValue());

            String selectedCase= ((SelectBooleanCheckbox)source).getValue().toString();

            log.info("Selected Case WobNum : {}",selectedCase);

            int noOfCases = 0;

            for ( Map.Entry<String, Boolean> entry : checked.entrySet())
            {

                if (entry.getValue()==true)
                {
                    String wobNo = entry.getKey().toString();

                    log.info("WobNum : {}",wobNo);

                    noOfCases++;

                }
            }

            log.info("No. Of Cases Selected : {}", noOfCases);

            if(selectedCase.equalsIgnoreCase("true") || noOfCases > 0)
            {

                disableAssign = false;

                log.info("Reassign Button enabled : {}",disableAssign);
            }

            else
            {
                disableAssign =true;

                log.info("Reassign Button disabled : {}",disableAssign);
            }

            if(noOfCases >5 )
            {
                disableAssign =true;

                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg5.show()");

                log.info("Reassign Button disabled : {}",disableAssign);
            }

        }

        else
        {
            disableAssign = true;

            log.info("Reassign Button disabled : {}",disableAssign);
        }
    }

    public void onRestartCase()
    {

        String queueName = inboxViewSelectItem.getQueuename();
        String wobNumber = inboxViewSelectItem.getFwobnumber();
        log.debug("onRestartCase ::: inboxViewSelectItem : {}", inboxViewSelectItem);
        HashMap<String,String> fieldsMap = new HashMap<String, String>();
        try {

            fieldsMap.put(BPMConstants.BPM_FIELD_ACTION_NAME, "Restart");

            if(queueName != null && wobNumber != null && fieldsMap.size() !=0)
            {

                bpmInterfaceImpl.dispatchCase(queueName,wobNumber,fieldsMap);

                log.info("restart successful.... ");

                onCreation();
            }

            RequestContext.getCurrentInstance().execute("successDlg1.show()");

            return;

        }

        catch (Exception e)
        {

            fieldsMap = null;
            log.error("Error in restart : {}",e);
            message = Util.getMessageException(e);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            return;
        }
    }

    public void onAssignBulk()
    {

        HashMap<String,String> fieldsMap = new HashMap<String, String>();
        List<String>wobNumbersList = new ArrayList<String>();
        Object[] arryOfObjectWobNOs = null;
        String[] stringArrayOfWobNos = null;

        try {

            fieldsMap.put(BPMConstants.BPM_FIELD_ACTION_NAME, "Select Case");

            for ( Map.Entry<String, Boolean> entry : checked.entrySet()) {

                if (entry.getValue()==true)
                {
                    String wobNo =entry.getKey().toString();
                    wobNumbersList.add(wobNo);

                }

            }

            if(wobNumbersList!=null && wobNumbersList.size()>5)
            {
                log.info("in if for more than 5 cases");
                RequestContext.getCurrentInstance().execute("msgBoxErrorDlg1.show()");
                log.info("after execute.. ");
                return;
            }
            arryOfObjectWobNOs =wobNumbersList.toArray();
            stringArrayOfWobNos = Arrays.copyOf(arryOfObjectWobNOs, arryOfObjectWobNOs.length, String[].class);
            log.info("stringArray length : "+stringArrayOfWobNos.length);

            if(peRosterName != null && stringArrayOfWobNos.length !=0 && fieldsMap.size() !=0)
            {

                bpmInterfaceImpl.batchDispatchCaseFromRoster(peRosterName,stringArrayOfWobNos,fieldsMap);

                log.info("batchDispatch successful.... ");

                onCreation();
            }

            checked.clear();

            setChecked(checked);

            RequestContext.getCurrentInstance().execute("successDlg.show()");

            return;

        }

        catch (Exception e)
        {

            wobNumbersList = null;
            arryOfObjectWobNOs = null;
            stringArrayOfWobNos = null;
            fieldsMap = null;
            log.error("Error in assign bulk : {}",e);
            message = Util.getMessageException(e);
            RequestContext.getCurrentInstance().execute("msgBoxErrorDlg.show()");
            return;
        }

    }

}
