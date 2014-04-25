package com.clevel.selos.controller;

import com.clevel.selos.dao.history.CaseCreationHistoryDAO;
import com.clevel.selos.dao.history.CaseHistoryDAO;
import com.clevel.selos.dao.master.WorkCaseIdByCustomerIdDAO;
import com.clevel.selos.dao.working.NCBDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCaseOwnerDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.ws.CaseCreation;
import com.clevel.selos.ws.WSCaseCreation;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 4/18/14
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
@ViewScoped
@ManagedBean(name = "appealResubmit")
public class AppealResubmit implements Serializable {

    boolean disableAppealButton;

    boolean disableResubmitButton;

    boolean checkNCBFlag;

    private long workCaseId;

    private long workCasePreScreenId;

    private String ncbRemarks;

    private String ncbReason;

    private String ncbFlag;

    //private List ncbReasonsList;

    @Inject
    User user;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;

    @Inject
    @Config(name = "interface.appeal.days")
    String noOfDays;

    @Inject
    @Config(name = "interface.appeal.limit")
    String appealLimit;

    @Inject
    @Config(name = "interface.resubmit.limit")
    String resubmitLimit;

    @Inject
    @Config(name = "interface.resumbit.reason")
    String reason;

    @Inject
    @Config(name = "interface.checkncb.days")
    String ncbDays;

    @Inject
    @SELOS
    Logger log;

    @Inject
    CaseHistoryDAO caseHistoryDAO;

    @Inject
    WorkCaseIdByCustomerIdDAO workCaseIdByCustomerIdDAO;

    @Inject
    NCBDAO ncbdao;
    
    @Inject
    CaseCreationHistoryDAO caseCreationHistoryDAO;

    @Inject
    WSCaseCreation wsCaseCreation;

    public String getNcbRemarks() {
        return ncbRemarks;
    }

    public void setNcbRemarks(String ncbRemarks) {
        this.ncbRemarks = ncbRemarks;
    }

    public String getNcbReason() {
        return ncbReason;
    }

    public void setNcbReason(String ncbReason) {
        this.ncbReason = ncbReason;
    }

    public String getNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(String ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public boolean isCheckNCBFlag() {
        return checkNCBFlag;
    }

    public void setCheckNCBFlag(boolean checkNCBFlag) {
        this.checkNCBFlag = checkNCBFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNcbDays() {
        return ncbDays;
    }

    public void setNcbDays(String ncbDays) {
        this.ncbDays = ncbDays;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public CaseHistoryDAO getCaseHistoryDAO() {
        return caseHistoryDAO;
    }

    public void setCaseHistoryDAO(CaseHistoryDAO caseHistoryDAO) {
        this.caseHistoryDAO = caseHistoryDAO;
    }

    public WorkCaseIdByCustomerIdDAO getWorkCaseIdByCustomerIdDAO() {
        return workCaseIdByCustomerIdDAO;
    }

    public void setWorkCaseIdByCustomerIdDAO(WorkCaseIdByCustomerIdDAO workCaseIdByCustomerIdDAO) {
        this.workCaseIdByCustomerIdDAO = workCaseIdByCustomerIdDAO;
    }

    public NCBDAO getNcbdao() {
        return ncbdao;
    }

    public void setNcbdao(NCBDAO ncbdao) {
        this.ncbdao = ncbdao;
    }

    public String getResubmitLimit() {
        return resubmitLimit;
    }

    public void setResubmitLimit(String resubmitLimit) {
        this.resubmitLimit = resubmitLimit;
    }

    public boolean isDisableResubmitButton() {
        return disableResubmitButton;
    }

    public void setDisableResubmitButton(boolean disableResubmitButton) {
        this.disableResubmitButton = disableResubmitButton;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getWorkCasePreScreenId() {
        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WorkCaseDAO getWorkCaseDAO() {
        return workCaseDAO;
    }

    public void setWorkCaseDAO(WorkCaseDAO workCaseDAO) {
        this.workCaseDAO = workCaseDAO;
    }

    public WorkCasePrescreenDAO getWorkCasePrescreenDAO() {
        return workCasePrescreenDAO;
    }

    public void setWorkCasePrescreenDAO(WorkCasePrescreenDAO workCasePrescreenDAO) {
        this.workCasePrescreenDAO = workCasePrescreenDAO;
    }

    public WorkCaseOwnerDAO getWorkCaseOwnerDAO() {
        return workCaseOwnerDAO;
    }

    public void setWorkCaseOwnerDAO(WorkCaseOwnerDAO workCaseOwnerDAO) {
        this.workCaseOwnerDAO = workCaseOwnerDAO;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getAppealLimit() {
        return appealLimit;
    }

    public void setAppealLimit(String appealLimit) {
        this.appealLimit = appealLimit;
    }

    public boolean isDisableAppealButton() {
        return disableAppealButton;
    }

    public void setDisableAppealButton(boolean disableAppealButton) {
        this.disableAppealButton = disableAppealButton;
    }

    @PostConstruct
    public void onCreation()
    {

        try
        {
            HttpSession session = FacesUtil.getSession(true);

            user = (User)session.getAttribute("user");

            //appeal button
            if(user.getRole().getId() == RoleValue.BDM.id() || user.getRole().getId() == RoleValue.SSO.id() && (Long)session.getAttribute("stepId") == 0)
            {
                if(checkSession(session))
                {
                    if((Long)session.getAttribute("workCaseId") != 0)
                    {

                        workCaseId = (Long)session.getAttribute("workCaseId");

                        WorkCase workCase = workCaseDAO.findById(workCaseId);

                        Long statusId = workCase.getStatus().getId();

                        int statusIdInt = statusId.intValue();

                        if(statusIdInt == StatusValue.REJECT_CA.value() || statusIdInt == StatusValue.REJECT_UW1.value()
                                || statusIdInt == StatusValue.REJECT_UW2.value() || statusIdInt == StatusValue.CANCEL_CA.value())
                        {

                            String refAppNumber = workCase.getRefAppNumber();

                            WorkCase refWorkCase1= workCaseDAO.findByAppNumber(refAppNumber);

                            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getLatestUWActionDate(workCase.getId());

                            if(workCaseOwner == null)
                            {
                                disableAppealButton = true;
                                log.info("No UW Steps for the case in case owner, disabled button");
                            }

                            else
                            {
                                Date lastUWActionDate = workCaseOwner.getCreateDate();

                                long duration = new Date().getTime() - lastUWActionDate.getTime();

                                long diffDays = TimeUnit.MILLISECONDS.toDays(duration);

                                if(diffDays>Integer.parseInt(noOfDays))
                                {
                                    disableAppealButton = true;

                                    log.info("No of days limit surpassed, disabled button");
                                }

                                else
                                {
                                    Integer appealCount = workCasePrescreenDAO.getAppealResubmitCount(refAppNumber, 2);

                                    if(appealCount>=Integer.parseInt(appealLimit))
                                    {
                                        disableAppealButton = true;
                                        log.info("Appeal Limit Reached. Disabled Appeal");
                                    }

                                    else
                                    {

                                        disableAppealButton =false;
                                        log.info("All Criteria Satisfied, enabled Appeal Button");

                                    }
                                }

                            }

                        }

                        else
                        {
                            disableAppealButton = true;
                            log.info("Status not in the required status list, button disabled");
                        }

                    }

                    else
                    {
                        disableAppealButton = true;
                        log.info("WorkCase Id is null/0, disabled button");
                    }
                }

            }

            else
            {
                disableAppealButton = true;

                log.info("Role is Not BDM/SSO. Disable Appeal");

            }

            //Resubmit Button
            if(user.getRole().getId() == RoleValue.BDM.id() || user.getRole().getId() == RoleValue.SSO.id())
            {
                if(checkSession(session))
                {
                    if((Long)session.getAttribute("workCaseId") != 0)
                    {
                        workCaseId = (Long)session.getAttribute("workCaseId");

                        WorkCase workCase = workCaseDAO.findById(workCaseId);

                        Long statusId = workCase.getStatus().getId();

                        int statusIdInt = statusId.intValue();

                        if(statusIdInt == StatusValue.REJECT_CA.value() || statusIdInt == StatusValue.REJECT_UW1.value()
                                || statusIdInt == StatusValue.REJECT_UW2.value() || statusIdInt == StatusValue.CANCEL_CA.value())
                        {
                            String refAppNumber = workCase.getRefAppNumber();

                            WorkCase refWorkCase1= workCaseDAO.findByAppNumber(refAppNumber);

                            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getLatestUWActionDate(workCase.getId());

                            if(workCaseOwner == null)
                            {
                                disableResubmitButton = true;
                                log.info("No UW Steps for the case in case owner, resubmit button");
                            }
                            else
                            {
                                Date lastUWActionDate = workCaseOwner.getCreateDate();

                                long duration = new Date().getTime() - lastUWActionDate.getTime();

                                long diffDays = TimeUnit.MILLISECONDS.toDays(duration);

                                if(diffDays>Integer.parseInt(noOfDays))
                                {
                                    disableResubmitButton = true;

                                    log.info("No of days limit surpassed, resubmit button");
                                }
                                else
                                {
                                    Integer resubmitCount = workCasePrescreenDAO.getAppealResubmitCount(refAppNumber, 3);

                                    if(resubmitCount>=Integer.parseInt(resubmitLimit))
                                    {

                                        disableResubmitButton = true;
                                        log.info("Appeal Limit Reached. resubmit Appeal");
                                    }

                                    else
                                    {

                                        disableResubmitButton = caseHistoryDAO.checkCaseReason(refAppNumber,reason);

                                        log.info("DisableResubmit Button : "+disableResubmitButton);

                                    }
                                }
                            }
                        }
                        else
                        {
                            disableResubmitButton = true;
                            log.info("Status not in the required status list, button disabled");
                        }
                    }
                    else
                    {
                        disableResubmitButton = true;
                        log.info("WorkCase Id is null/0, disabled Resubmit");
                    }
                }
            }
            else
            {
                disableResubmitButton = true;

                log.info("Role is Not BDM/SSO. Disable Resubmit");
            }

        }
        catch (Exception e)
        {

            log.error("Error in Appeal Resubmit : ",e);

        }
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( (Long)session.getAttribute("workCaseId") != 0 || (Long)session.getAttribute("workCasePreScreenId") != 0 ) &&
                (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void checkNCB()
    {
        try
        {
            HttpSession session = FacesUtil.getSession(true);

            Long workCaseId = (Long)session.getAttribute("workCaseId");

            List<Integer> customerIdList = workCaseIdByCustomerIdDAO.getCustomerIdByWOrkCaseId(workCaseId);

            Date checkNCBDate = ncbdao.checkNCBDate(customerIdList);

            long duration = new Date().getTime() - checkNCBDate.getTime();

            long diffDays = TimeUnit.MILLISECONDS.toDays(duration);

            if(diffDays>Integer.parseInt(ncbDays))
            {
                checkNCBFlag = true;
            }

            else
            {
                checkNCBFlag = false;
            }
        }
        catch (Exception e)
        {

            log.error("Error in Check NCB: ",e);

        }
    }

    public void appealCase()
    {

        HttpSession session = FacesUtil.getSession(true);
        AppHeaderView appHeaderView = (AppHeaderView)session.getAttribute("appHeaderView");

        CaseCreationHistory caseCreationHistory = caseCreationHistoryDAO.getCaseDetails(appHeaderView.getAppNo());

        String flag = "";

        if(checkNCBFlag)
        {
            flag = "Y";
        }
        else
        {
            flag="N";
        }

        wsCaseCreation.newCase(caseCreationHistory.getJobName(),caseCreationHistory.getCaNumber(),caseCreationHistory.getOldCaNumber()
            ,caseCreationHistory.getAccountNo1(),caseCreationHistory.getCustomerId(),caseCreationHistory.getCustomerName(),caseCreationHistory.getCitizenId()
            , 2,caseCreationHistory.getCustomerType(),caseCreationHistory.getBdmId(),caseCreationHistory.getHubCode()
            ,caseCreationHistory.getRegionCode(),caseCreationHistory.getUwId(),caseCreationHistory.getAppInDateBDM(),caseCreationHistory.getFinalApproved()
            ,caseCreationHistory.getParallel(),caseCreationHistory.getPending(),caseCreationHistory.getCaExist(),caseCreationHistory.getCaEnd()
            ,caseCreationHistory.getAccountNo2(),caseCreationHistory.getAccountNo3(),caseCreationHistory.getAccountNo4(),caseCreationHistory.getAccountNo5()
            ,caseCreationHistory.getAccountNo6(),caseCreationHistory.getAccountNo7(),caseCreationHistory.getAccountNo8(),caseCreationHistory.getAccountNo9()
            ,caseCreationHistory.getAccountNo10(),caseCreationHistory.getAppInDateUW(),caseCreationHistory.getRefAppNumber(),ncbReason
            ,flag,caseCreationHistory.getSsoId());

        RequestContext.getCurrentInstance().execute("successDlg.show()");

    }

    public void resubmitCase()
    {

        HttpSession session = FacesUtil.getSession(true);
        AppHeaderView appHeaderView = (AppHeaderView)session.getAttribute("appHeaderView");

        CaseCreationHistory caseCreationHistory = caseCreationHistoryDAO.getCaseDetails(appHeaderView.getAppNo());

        String flag = "";

        if(checkNCBFlag)
        {
            flag = "Y";
        }
        else
        {
            flag="N";
        }

        wsCaseCreation.newCase(caseCreationHistory.getJobName(),caseCreationHistory.getCaNumber(),caseCreationHistory.getOldCaNumber()
                ,caseCreationHistory.getAccountNo1(),caseCreationHistory.getCustomerId(),caseCreationHistory.getCustomerName(),caseCreationHistory.getCitizenId()
                , 3,caseCreationHistory.getCustomerType(),caseCreationHistory.getBdmId(),caseCreationHistory.getHubCode()
                ,caseCreationHistory.getRegionCode(),caseCreationHistory.getUwId(),caseCreationHistory.getAppInDateBDM(),caseCreationHistory.getFinalApproved()
                ,caseCreationHistory.getParallel(),caseCreationHistory.getPending(),caseCreationHistory.getCaExist(),caseCreationHistory.getCaEnd()
                ,caseCreationHistory.getAccountNo2(),caseCreationHistory.getAccountNo3(),caseCreationHistory.getAccountNo4(),caseCreationHistory.getAccountNo5()
                ,caseCreationHistory.getAccountNo6(),caseCreationHistory.getAccountNo7(),caseCreationHistory.getAccountNo8(),caseCreationHistory.getAccountNo9()
                ,caseCreationHistory.getAccountNo10(),caseCreationHistory.getAppInDateUW(),caseCreationHistory.getRefAppNumber(),ncbReason
                ,flag,caseCreationHistory.getSsoId());

        RequestContext.getCurrentInstance().execute("successDlg.show()");

    }

}
