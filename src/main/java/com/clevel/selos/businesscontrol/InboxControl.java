package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.StepLandingPageDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.SELOS;
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
    private UserDAO userDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
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

    public List<InboxView> getInboxFromBPM(UserDetail userDetail) {

        List<InboxView> inboxViewList = new ArrayList<InboxView>();

        //For WebSphere//
        List<CaseDTO> caseDTOList = bpmInterface.getInboxList();

        /*List<CaseDTO> caseDTOList = new ArrayList<CaseDTO>();
        List<WorkCasePrescreen> workCasePrescreenList = getWorkCasePreScreen();

        for (WorkCasePrescreen workCasePrescreen : workCasePrescreenList) {
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
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
        log.debug("workCase List : {}", workCaseList);
        for (WorkCase workCase : workCaseList) {
            CaseDTO caseDTO = new CaseDTO();
            HashMap<String, String> caseData = new HashMap<String, String>();
            caseData.put("F_WobNum", workCase.getWobNumber());
            caseData.put("F_StepName", "PS1001");
            caseData.put("Step_Code", Long.toString(workCase.getStep().getId()));
            caseData.put("Lock_Status", "0");
            caseData.put("Locked_User", "0");
            caseData.put("QUEUE_NAME", "0");

            caseDTO.setCaseData(caseData);

            caseDTOList.add(caseDTO);
        }*/

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

    //TODO:: To review Application Header.
    public AppHeaderView getHeaderInformation(long workCasePreScreenId, long workCaseId) {
        log.info("getHeaderInformation ::: workCasePreScreenId : {}, workCaseId : {}", workCasePreScreenId, workCaseId);
        AppHeaderView appHeaderView = new AppHeaderView();
        appHeaderView.setBorrowerHeaderViewList(new ArrayList<AppBorrowerHeaderView>());
        String bdmUserId;
        String uwUserId = "";

        List<Customer> customerList = new ArrayList<Customer>();
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();

        if (Long.toString(workCaseId) != null && workCaseId != 0) {
            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            bdmUserId = workCase.getCreateBy().getId();

            appHeaderView.setCaNo(basicInfo.getCaNumber());
            appHeaderView.setAppNo(workCase.getAppNumber());
            appHeaderView.setCaseStatus(workCase.getStatus().getDescription());

            customerList = customerDAO.findBorrowerByWorkCaseId(workCaseId);
            customerInfoViewList = customerTransform.transformToViewList(customerList);
            log.debug("customerInfo size : {}", customerInfoViewList.size());
        } else {
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            log.info("getHeaderInformation ::: workCasePreScreen : {}", workCasePrescreen);
            bdmUserId = ((User)workCasePrescreen.getCreateBy()).getId();

            appHeaderView.setCaNo(workCasePrescreen.getCaNumber());
            appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
            //appHeaderView.setAppRefNo(workCase.getAppN);
            //appHeaderView.setAppRefDate();
            appHeaderView.setCaseStatus(workCasePrescreen.getStatus().getDescription());

            customerList = customerDAO.findBorrowerByWorkCasePreScreenId(workCasePreScreenId);
            customerInfoViewList = customerTransform.transformToViewList(customerList);
            log.debug("customerInfo size : {}", customerInfoViewList.size());
        }

        log.info("getHeaderInformation ::: customerInfoViewList : {}", customerInfoViewList);
        if (customerInfoViewList != null) {
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for (CustomerInfoView item : customerInfoViewList) {
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                if (item.getTitleTh() != null) {
                    appBorrowerHeaderView.setBorrowerName(item.getTitleTh().getTitleTh() + "" + item.getFirstNameTh() + " " + item.getLastNameTh());
                } else {
                    appBorrowerHeaderView.setBorrowerName(item.getFirstNameTh() + " " + item.getLastNameTh());
                }
                if (item.getCustomerEntity().getId() == 1) {
                    appBorrowerHeaderView.setPersonalId(item.getCitizenId());
                } else if (item.getCustomerEntity().getId() == 2) {
                    appBorrowerHeaderView.setPersonalId(item.getRegistrationId());
                }
                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
        }

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



        if (!Util.isEmpty(bdmUserId)) {
            User bdmUser = userDAO.findById(bdmUserId);
            if (bdmUser != null) {
                appHeaderView.setBdmName(bdmUser.getUserName());
                appHeaderView.setBdmPhoneNumber(bdmUser.getPhoneNumber());
                appHeaderView.setBdmPhoneExtNumber(bdmUser.getPhoneExt());
                if (bdmUser.getZone() != null) {
                    appHeaderView.setBdmZoneName(bdmUser.getZone().getName());
                }
                if (bdmUser.getRegion() != null) {
                    appHeaderView.setBdmRegionName(bdmUser.getRegion().getName());
                }
            }
        }

        if (!Util.isEmpty(uwUserId)) {
            User uwUser = userDAO.findById(uwUserId);
            if (uwUser != null) {
                appHeaderView.setUwName(uwUser.getUserName());
                appHeaderView.setUwPhoneNumber(uwUser.getPhoneExt());
                appHeaderView.setUwTeamName(uwUser.getTeam().getName());
            }
        }
        return appHeaderView;
    }
}
