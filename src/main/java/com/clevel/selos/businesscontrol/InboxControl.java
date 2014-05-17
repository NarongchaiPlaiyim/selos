package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.StepLandingPage;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.StepToStatus;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Inject
    public InboxControl(){

    }

    public String getLandingPage(long stepId, long status){
        User user = getCurrentUser();
        if(user!=null){
//            StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepStatusAndRole(stepId,status,user.getRole().getId());
            StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepId(stepId);
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
