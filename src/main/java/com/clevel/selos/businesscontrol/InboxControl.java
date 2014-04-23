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
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.InboxView;
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

    public String getLandingPage(long stepId){
        StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepId(stepId);
        String landingPage = "";
        if(stepLandingPage != null){
            landingPage = stepLandingPage.getPageName();
        } else {
            landingPage = "LANDING_PAGE_NOT_FOUND";
        }
        return landingPage;
    }

    public void selectCasePoolBox(String queueName, String wobNumber, long actionCode) throws Exception{
        //Send only QueueName, Action, WobNum
        bpmExecutor.selectCase(actionCode, queueName, wobNumber);
    }
}
