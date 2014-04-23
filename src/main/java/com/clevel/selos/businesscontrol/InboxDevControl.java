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
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.InboxView;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.transform.CustomerTransform;
import com.clevel.selos.transform.business.InboxBizTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Stateless
public class InboxDevControl extends BusinessControl {
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
    public InboxDevControl(){

    }

    public List<InboxView> getInboxFromBPM(UserDetail userDetail) {

        List<InboxView> inboxViewList = new ArrayList<InboxView>();

        //For WebSphere//
        //List<CaseDTO> caseDTOList = bpmInterface.getInboxList();

        List<CaseDTO> caseDTOList = new ArrayList<CaseDTO>();

        List<WorkCasePrescreen> workCasePrescreenList = getWorkCasePreScreen();
        log.debug("workCasePrescreen List.size() : {}", workCasePrescreenList.size());
        for (WorkCasePrescreen workCasePrescreen : workCasePrescreenList) {
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
            caseData.put("F_AppNumber", workCasePrescreen.getAppNumber());
            caseData.put("F_WobNum", workCasePrescreen.getWobNumber());
            caseData.put("F_StepName", "PS1001");
            caseData.put("Step_Code", Long.toString(workCasePrescreen.getStep().getId()));
            caseData.put("Lock_Status", "0");
            caseData.put("Locked_User", "0");
            caseData.put("QUEUE_NAME", "0");

            caseDTO.setCaseData(caseData);

            caseDTOList.add(caseDTO);
        }

        List<WorkCase> workCaseList = getWorkCase();
        log.debug("workCase List.size() : {}", workCaseList.size());
        for (WorkCase workCase : workCaseList) {
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
            caseData.put("F_AppNumber", workCase.getAppNumber());
            caseData.put("F_WobNum", workCase.getWobNumber());
            caseData.put("F_StepName", "PS2001");
            caseData.put("Step_Code", Long.toString(workCase.getStep().getId()));
            caseData.put("Lock_Status", "0");
            caseData.put("Locked_User", "0");
            caseData.put("QUEUE_NAME", "0");

            caseDTO.setCaseData(caseData);

            caseDTOList.add(caseDTO);
        }

        List<WorkCaseAppraisal> workCaseAppraisalList = getWorkCaseAppraisal();
        log.debug("workCase List : {}", workCaseList);
        for(WorkCaseAppraisal workCaseAppraisal : workCaseAppraisalList) {
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
            caseData.put("F_AppNumber", workCaseAppraisal.getAppNumber());
            caseData.put("F_WobNum", workCaseAppraisal.getWobNumber());
            caseData.put("F_StepName", "PS2005");
            caseData.put("Step_Code", Long.toString(workCaseAppraisal.getStep().getId()));
            caseData.put("Lock_Status", "0");
            caseData.put("Locked_User", "0");
            caseData.put("QUEUE_NAME", "0");

            caseDTO.setCaseData(caseData);

            caseDTOList.add(caseDTO);
        }

        log.info("CaseDTO : caseDTOList : {}", caseDTOList);
        inboxViewList = inboxBizTransform.transformToView(caseDTOList);

        return inboxViewList;
    }

    public List<InboxView> getInboxPoolFromBPM(UserDetail userDetail){
        List<InboxView> inboxViewList = new ArrayList<InboxView>();
        List<CaseDTO> caseDTOPoolList = new ArrayList<CaseDTO>();

        if(userDetail.getRole().equals("ROLE_UW")){
            caseDTOPoolList = bpmInterface.getInboxPoolList("UW1_Pool_Q");

        }else if(userDetail.getRole().equals("ROLE_AAD")){
            caseDTOPoolList = bpmInterface.getInboxPoolList("AAD_Admin_Q");
        }

        inboxViewList = inboxBizTransform.transformToView(caseDTOPoolList);

        return inboxViewList;
    }

    //Tempory to remove
    public List<WorkCase> getWorkCase() {
        List<WorkCase> workCases = workCaseDAO.findAll();

        return workCases;
    }

    //Tempory to remove
    public List<WorkCasePrescreen> getWorkCasePreScreen() {
        List<WorkCasePrescreen> workCasePrescreenList = workCasePrescreenDAO.findAll();

        return workCasePrescreenList;
    }

    public List<WorkCaseAppraisal> getWorkCaseAppraisal() {
        List<WorkCaseAppraisal> workCaseAppraisalList = workCaseAppraisalDAO.findAll();

        return workCaseAppraisalList;
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

    /*public String getLandingPage(long stepId, long statusCode){
        StepLandingPage stepLandingPage = stepLandingPageDAO.findByStepId(stepId, statusCode);
        String landingPage = "";
        if(stepLandingPage != null){
            landingPage = stepLandingPage.getPageName();
        } else {
            landingPage = "LANDING_PAGE_NOT_FOUND";
        }
        return landingPage;
    }*/

    //TODO:: To review Application Header.
    public AppHeaderView getHeaderInformation(long workCasePreScreenId, long workCaseId) {
        log.info("getHeaderInformation ::: workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
        AppHeaderView appHeaderView = new AppHeaderView();
        appHeaderView.setBorrowerHeaderViewList(new ArrayList<AppBorrowerHeaderView>());
        String uwUserId = "";

        List<Customer> customerList = new ArrayList<Customer>();

        if (Long.toString(workCaseId) != null && workCaseId != 0) {
            log.debug("getHeaderInformation ::: getBasicInfoByWorkCaseId");
            BasicInfo basicInfo = basicInfoDAO.getBasicInfoByWorkCaseId(workCaseId);
            log.debug("getHeaderInformation ::: getWorkCaseById");
            WorkCase workCase = workCaseDAO.getWorkCaseById(workCaseId);

            appHeaderView.setCaNo(basicInfo.getCaNumber());
            appHeaderView.setAppNo(workCase.getAppNumber());
            appHeaderView.setCaseStatus(workCase.getStatus().getDescription());

            appHeaderView.setBdmName(workCase.getCreateBy().getUserName());
            appHeaderView.setBdmPhoneNumber(workCase.getCreateBy().getPhoneNumber());
            appHeaderView.setBdmPhoneExtNumber(workCase.getCreateBy().getPhoneExt());
            if (workCase.getCreateBy().getZone() != null) {
                appHeaderView.setBdmZoneName(workCase.getCreateBy().getZone().getName());
            }
            if (workCase.getCreateBy().getRegion() != null) {
                appHeaderView.setBdmRegionName(workCase.getCreateBy().getRegion().getName());
            }
            log.debug("getHeaderInformation ::: findBorrowerByWorkCaseId");
            customerList = customerDAO.getBorrowerByWorkCaseId(workCaseId, 0);
        } else {
            log.debug("getHeaderInformation ::: getWorkCasePreScreenById");
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.getWorkCasePreScreenById(workCasePreScreenId);

            appHeaderView.setCaNo(workCasePrescreen.getCaNumber());
            appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
            appHeaderView.setCaseStatus(workCasePrescreen.getStatus().getDescription());

            appHeaderView.setBdmName(workCasePrescreen.getCreateBy().getUserName());
            appHeaderView.setBdmPhoneNumber(workCasePrescreen.getCreateBy().getPhoneNumber());
            appHeaderView.setBdmPhoneExtNumber(workCasePrescreen.getCreateBy().getPhoneExt());
            if (workCasePrescreen.getCreateBy().getZone() != null) {
                appHeaderView.setBdmZoneName(workCasePrescreen.getCreateBy().getZone().getName());
            }
            if (workCasePrescreen.getCreateBy().getRegion() != null) {
                appHeaderView.setBdmRegionName(workCasePrescreen.getCreateBy().getRegion().getName());
            }

            customerList = customerDAO.getBorrowerByWorkCaseId(0, workCasePreScreenId);

            //Find product program from WorkCasePreScreenId
            Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);
            if (prescreen != null) {
                List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(prescreen.getId());
                log.info("getHeaderInformation ::: prescreenFacilityList : {}", prescreenFacilityList);
                if (prescreenFacilityList != null) {
                    List<String> productProgram = new ArrayList<String>();
                    for (PrescreenFacility item : prescreenFacilityList) {
                        String prdPrg = item.getProductProgram().getDescription();
                        productProgram.add(prdPrg);
                    }
                    appHeaderView.setProductProgramList(productProgram);
                }
            }
        }

        log.info("getHeaderInformation ::: customerList.size : {}", customerList.size());
        if (customerList != null) {
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for (Customer item : customerList) {
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                String borrowerName = "";
                String borrowerPersonalId = "";
                if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    borrowerName = item.getTitle().getTitleTh() + "" + item.getNameTh() + " " + item.getLastNameTh();
                    borrowerPersonalId = item.getIndividual().getCitizenId();
                } else {
                    borrowerName = item.getTitle().getTitleTh() + "" + item.getNameTh();
                    borrowerPersonalId = item.getJuristic().getRegistrationId();
                }
                appBorrowerHeaderView.setBorrowerName(borrowerName);
                appBorrowerHeaderView.setPersonalId(borrowerPersonalId);

                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
        }

        /*if (!Util.isEmpty(uwUserId)) {
            User uwUser = userDAO.findById(uwUserId);
            if (uwUser != null) {
                appHeaderView.setUwName(uwUser.getUserName());
                appHeaderView.setUwPhoneNumber(uwUser.getPhoneExt());
                appHeaderView.setUwTeamName(uwUser.getTeam().getName());
            }
        }*/
        log.debug("getHeaderInformation ::: end : return appHeaderView : {}", appHeaderView);
        return appHeaderView;
    }

    public void selectCasePoolBox(String queueName, String wobNumber, long actionCode) throws Exception{
        //Send only QueueName, Action, WobNum
        bpmExecutor.selectCase(actionCode, queueName, wobNumber);
    }
}
