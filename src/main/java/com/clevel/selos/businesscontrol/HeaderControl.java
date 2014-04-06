package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HeaderControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;

    @Inject
    public HeaderControl(){

    }

    //TODO:: To review Application Header.
    public AppHeaderView getHeaderInformation(long stepId, String wobNumber) {
        log.info("getHeaderInformation ::: StepId : {} , WOBNumber : {}", stepId, wobNumber);
        AppHeaderView appHeaderView = new AppHeaderView();
        appHeaderView.setBorrowerHeaderViewList(new ArrayList<AppBorrowerHeaderView>());
        String bdmUserId = "";
        String uwUserId = "";

        List<Customer> customerList = new ArrayList<Customer>();

        if(stepId == StepValue.PRESCREEN_INITIAL.value() || stepId == StepValue.PRESCREEN_CHECKER.value() || stepId == StepValue.PRESCREEN_MAKER.value()) {
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByWobNumber(wobNumber);
            log.info("getHeaderInformation ::: workCasePreScreen : {}", workCasePrescreen);
            if(workCasePrescreen != null){
                bdmUserId = workCasePrescreen.getCreateBy().getId();

                appHeaderView.setCaNo(workCasePrescreen.getCaNumber());
                appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
                appHeaderView.setAppRefNo("");
                appHeaderView.setAppRefDate("");
                appHeaderView.setCaseStatus(workCasePrescreen.getStatus() != null ?workCasePrescreen.getStatus().getDescription() : "");
                appHeaderView.setRequestType(workCasePrescreen.getRequestType() != null ? workCasePrescreen.getRequestType().getName() : "");

                customerList = customerDAO.getBorrowerByWorkCaseId(0, workCasePrescreen.getId());
                log.debug("customerList size : {}", customerList.size());

                //Find product program from WorkCasePreScreenId
                Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePrescreen.getId());
                if (prescreen != null) {
                    appHeaderView.setProductGroup(prescreen.getProductGroup() != null ? prescreen.getProductGroup().getName() : "");
                }
            }
        } else if(stepId == StepValue.REVIEW_APPRAISAL_REQUEST.value()){
            WorkCaseAppraisal workCaseAppraisal = workCaseAppraisalDAO.findByWobNumber(wobNumber);
            log.debug("getHeaderInformation ::: workCaseAppraisal : {}", workCaseAppraisal);

            //Find workCase or workCasePreScreen
            if(workCaseAppraisal != null){
                WorkCase workCase = workCaseDAO.findByAppNumber(workCaseAppraisal.getAppNumber());
                if(workCase != null){
                    bdmUserId = workCase.getCreateBy() != null ? workCase.getCreateBy().getId() : "";

                    appHeaderView.setAppNo(workCase.getAppNumber());
                    appHeaderView.setAppRefNo("");
                    appHeaderView.setAppRefDate("");
                    appHeaderView.setCaseStatus(workCaseAppraisal.getStatus() != null ? workCaseAppraisal.getStatus().getDescription() : "");
                    appHeaderView.setRequestType(workCase.getRequestType() != null ? workCase.getRequestType().getName() : "");
                    appHeaderView.setProductGroup(workCase.getProductGroup() != null ? workCase.getProductGroup().getName() : "");

                    customerList = customerDAO.getBorrowerByWorkCaseId(workCase.getId(), 0);
                    log.debug("customerList size : {}", customerList.size());
                }else{
                    WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(workCaseAppraisal.getAppNumber());
                    if(workCasePrescreen != null){
                        bdmUserId = workCasePrescreen.getCreateBy().getId();

                        appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
                        appHeaderView.setAppRefNo("");
                        appHeaderView.setAppRefDate("");
                        appHeaderView.setCaseStatus(workCaseAppraisal.getStatus() != null ? workCaseAppraisal.getStatus().getDescription() : "");
                        appHeaderView.setRequestType(workCasePrescreen.getRequestType() != null ? workCasePrescreen.getRequestType().getName() : "");

                        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreen(workCasePrescreen);
                        if(prescreen != null){
                            appHeaderView.setProductGroup(prescreen.getProductGroup() != null ? prescreen.getProductGroup().getName() : "");
                        }

                        customerList = customerDAO.getBorrowerByWorkCaseId(0, workCasePrescreen.getId());
                        log.debug("customerList size : {}", customerList.size());
                    }
                }

            }
        } else {
            WorkCase workCase = workCaseDAO.findByWobNumber(wobNumber);

            bdmUserId = workCase.getCreateBy().getId();

            appHeaderView.setAppNo(workCase.getAppNumber());
            appHeaderView.setCaseStatus(workCase.getStatus() != null ? workCase.getStatus().getDescription() : "");
            appHeaderView.setRequestType(workCase.getRequestType() != null ? workCase.getRequestType().getName() : "");
            appHeaderView.setProductGroup(workCase.getProductGroup() != null ? workCase.getProductGroup().getName() : "");

            customerList = customerDAO.findBorrowerByWorkCaseId(workCase.getId());
            log.debug("customerList size : {}", customerList.size());

        }

        if (customerList != null && customerList.size() > 0) {
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for (Customer item : customerList) {
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                String borrowerName = "";
                borrowerName = item.getTitle() != null ? item.getTitle().getTitleTh() : "";
                borrowerName = borrowerName.concat("").concat(item.getNameTh() != null ? item.getNameTh() : "");
                borrowerName = borrowerName.concat(" ").concat(item.getLastNameTh() != null ? item.getLastNameTh() : "");

                appBorrowerHeaderView.setBorrowerName(borrowerName);

                if (item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()) {
                    appBorrowerHeaderView.setPersonalId(item.getIndividual() != null ? item.getIndividual().getCitizenId() : "");
                } else if (item.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()) {
                    appBorrowerHeaderView.setPersonalId(item.getJuristic() != null ? item.getJuristic().getRegistrationId() : "");
                }
                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
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
