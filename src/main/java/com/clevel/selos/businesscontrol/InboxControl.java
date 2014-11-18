package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.StepToStatus;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCaseOwner;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class InboxControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BPMInterface bpmInterface;
    @Inject
    BPMExecutor bpmExecutor;

    @Inject
    StepStatusControl stepStatusControl;

    @Inject
    private UserDAO userDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    StepDAO stepDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    StepLandingPageDAO stepLandingPageDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;

    @Inject
    InboxBizTransform inboxBizTransform;
    @Inject
    CustomerTransform customerTransform;

    public static final String RETURN_REPLY_PAGE = "/site/returnInfoReply.jsf";
    public static final String RETURN_REVIEW_PAGE = "/site/returnInfoReview.jsf";
    private static final  String BASIC_INFO_PAGE = "/site/basicInfo.jsf";
    private static final String DECISION_PAGE = "/site/decision.jsf";

    @Inject
    public InboxControl(){

    }

    public String getLandingPage(long stepId, long status){
        User user = getCurrentUser();
        if(user!=null){
            if(stepId==StepValue.CREDIT_DECISION_UW1_BDM.value() && status==20006){
                if(user.getRole().getId()== RoleValue.BDM.id()){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==StepValue.CREDIT_DECISION_UW1_CORRECT_INFO_BDM.value() && status==20006){
                if(user.getRole().getId()== RoleValue.BDM.id()){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==StepValue.CREDIT_DECISION_UW2_BDM.value() && status==20015){
                if(user.getRole().getId()== RoleValue.BDM.id()){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==StepValue.CREDIT_DECISION_UW2_BDM_UPD_INFO.value() && status==20015){
                if(user.getRole().getId()== RoleValue.BDM.id()){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==StepValue.CREDIT_DECISION_UW1.value() && status==20007){
                if(user.getRole().getId()==RoleValue.UW.id()){
                    return RETURN_REVIEW_PAGE;
                }
            }

            StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepStatus(stepId,status);
            String landingPage = "";
            if(stepLandingPage != null){
                landingPage = stepLandingPage.getPageName();
                if(user.getRole().getId() == RoleValue.UW.id() && !(landingPage.equals(BASIC_INFO_PAGE) || landingPage.equals(DECISION_PAGE))){
                    landingPage = BASIC_INFO_PAGE;
                }else if(user.getRole().getId() == RoleValue.VIEWER.id() && !landingPage.equals(BASIC_INFO_PAGE)){
                    landingPage = BASIC_INFO_PAGE;
                }
            } else {
                landingPage = "LANDING_PAGE_NOT_FOUND";
            }
            return landingPage;
        } else {
            return "LANDING_PAGE_NOT_FOUND";
        }
    }

    public void selectCasePoolBox(String queueName, String wobNumber, long actionCode) throws Exception{
        //Send only QueueName, Action, WobNum
        bpmExecutor.selectCase(actionCode, queueName, wobNumber);
    }

    public PEInbox getNextStep(PEInbox peInbox, long actionCode){
        PEInbox tempPeInbox = peInbox;
        StepToStatus stepToStatus = stepStatusControl.getNextStep(Util.parseLong(peInbox.getStepId(), 0), Util.parseLong(peInbox.getStatuscode(), 0), actionCode);
        if(stepToStatus != null) {
            tempPeInbox.setStep(Util.parseString(stepToStatus.getNextStep() != null ? stepToStatus.getNextStep().getName() : "", ""));
            tempPeInbox.setStepId(Util.parseLong(stepToStatus.getNextStep() != null ? stepToStatus.getNextStep().getId() : 0, 0));
            tempPeInbox.setStatuscode(Util.parseString(stepToStatus.getNextStatus().getId(), ""));
            tempPeInbox.setQueuename("Inbox(0)");
        }

        return tempPeInbox;
    }

    public void updateWorkCaseOwner(long workCasePreScreenId, long workCaseId, long stepId, String caseOwner){
        log.debug("updateWorkCaseOwner : workCasePreScreenId : {}, workCaseId : {}, stepId : {}, caseOwner : {}", workCasePreScreenId, workCaseId, stepId, caseOwner);
        try{
            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerByStep(workCasePreScreenId, workCaseId, stepId);
            WorkCasePrescreen workCasePrescreen = null;
            WorkCase workCase = null;
            //User currentUser = userDAO.findById(Util.upperCase(caseOwner));
            User currentUser = getCurrentUser();
            Step step = stepDAO.findById(stepId);

            if(!Util.isZero(workCasePreScreenId))
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

            if(!Util.isZero(workCaseId))
                workCase = workCaseDAO.findById(workCaseId);

            if(!Util.isNull(workCaseOwner)){
                workCaseOwner.setWorkCase(workCase);
                workCaseOwner.setWorkCasePrescreen(workCasePrescreen);
                workCaseOwner.setUser(currentUser);
                workCaseOwner.setRole(currentUser != null ? currentUser.getRole() : null);
                workCaseOwner.setModifyDate(new Date());
            }else{
                workCaseOwner = new WorkCaseOwner();
                workCaseOwner.setWorkCase(workCase);
                workCaseOwner.setWorkCasePrescreen(workCasePrescreen);
                workCaseOwner.setTimesOfCriteriaChecked(0);
                workCaseOwner.setUser(currentUser);
                workCaseOwner.setRole(currentUser != null ? currentUser.getRole() : null);
                workCaseOwner.setStep(step);
                workCaseOwner.setCreateDate(new Date());
            }
            workCaseOwnerDAO.persist(workCaseOwner);
        }catch (Exception ex){
            log.error("Exception while insert to WorkCaseOwner : ", ex);
        }
    }
}
