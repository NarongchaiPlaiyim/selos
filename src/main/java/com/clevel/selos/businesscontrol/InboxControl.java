package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.StepToStatus;
import com.clevel.selos.model.view.PEInbox;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

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
    public static final String ROLE_BDM = "SYSTEM_BDM";
    public static final String ROLE_UW = "SYSTEM_UW";

    @Inject
    public InboxControl(){

    }

    public String getLandingPage(long stepId, long status){
        User user = getCurrentUser();
        if(user!=null){
            if(stepId==2004 && status==20006){
                if(user.getRole().getSystemName()==ROLE_BDM){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==2027 && status==20006){
                if(user.getRole().getSystemName()==ROLE_BDM){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==2017 && status==20015){
                if(user.getRole().getSystemName()==ROLE_BDM){
                    return RETURN_REPLY_PAGE;
                }
            }

            if(stepId==2030 && status==20015){
                if(user.getRole().getSystemName()==ROLE_BDM){
                    return RETURN_REPLY_PAGE;
                }
            }

            StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepStatus(stepId,status);
            String landingPage = "";
            if(stepLandingPage != null){
                landingPage = stepLandingPage.getPageName();
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
}
