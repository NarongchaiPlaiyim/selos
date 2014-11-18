package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.StatusDAO;
import com.clevel.selos.dao.master.UserTeamDAO;
import com.clevel.selos.dao.relation.RelTeamUserDetailsDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.UserTeam;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;
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
    StatusDAO statusDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseAppraisalDAO workCaseAppraisalDAO;
    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    RelTeamUserDetailsDAO relTeamUserDetailsDAO;
    @Inject
    UserTeamDAO userTeamDAO;

    @Inject
    NCBInterface ncbInterface;

    @Inject
    public HeaderControl(){

    }

    //TODO:: To review Application Header.
    public AppHeaderView getHeaderInformation(long stepId, long statusId, String appNumber) {
        log.info("getHeaderInformation ::: StepId : {} , appNumber : {}", stepId, appNumber);
        AppHeaderView appHeaderView = new AppHeaderView();
        appHeaderView.setBorrowerHeaderViewList(new ArrayList<AppBorrowerHeaderView>());
        String bdmUserId = "";
        String uwUserId = "";

        //List<Customer> customerList = new ArrayList<Customer>();
        List<CustomerBasicView> customerBasicViewList = new ArrayList<CustomerBasicView>();

        WorkCase workCase = workCaseDAO.findByAppNumber(appNumber);
        Status status = statusDAO.findById(statusId);
        if(!Util.isNull(workCase)){
            log.debug("getHeaderInformation ::: get information from WorkCase");
            bdmUserId = workCase.getCreateBy() != null? workCase.getCreateBy().getId() : "";
            uwUserId = workCaseOwnerDAO.getUW1(StepValue.CREDIT_DECISION_UW1.value(), workCase.getId());

            BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCase.getId());

            if(!Util.isNull(basicInfo)) {
                appHeaderView.setCaNo(basicInfo.getCaNumber());
                appHeaderView.setRefinance(basicInfo.getRefinanceInValue() != null ? basicInfo.getRefinanceInValue().getName() : "");
                appHeaderView.setRefinanceOut(basicInfo.getRefinanceOutValue() != null ? basicInfo.getRefinanceOutValue().getName() : "");
                appHeaderView.setSubmitDate(DateTimeUtil.convertToStringDDMMYYYY(basicInfo.getUwSubmitDate()));
                appHeaderView.setLastDecisionDate(DateTimeUtil.convertToStringDDMMYYYY(basicInfo.getLastDecisionDate()));
                appHeaderView.setLimitSetupExpireDate(DateTimeUtil.convertToStringDDMMYYYY(basicInfo.getLimitSetupExpiryDate()));
            }
            appHeaderView.setAppNo(workCase.getAppNumber());
            appHeaderView.setAppRefNo(workCase.getRefAppNumber());
            appHeaderView.setAppRefDate(DateTimeUtil.convertToStringDDMMYYYY(workCase.getRefAppDate()));
            appHeaderView.setProductGroup(workCase.getProductGroup().getName());
            appHeaderView.setCaseStatus(status != null ? status.getName() : "");
            appHeaderView.setRequestType(workCase.getRequestType() != null ? workCase.getRequestType().getName() : "");

            //customerList = customerDAO.getBorrowerByWorkCaseId(workCase.getId(), 0);
            customerBasicViewList = customerDAO.getCustomerForHeader(0, workCase.getId());
            log.debug("getHeaderInformation ::: get information from WorkCase : customer list size : {}", customerBasicViewList.size());
        }else{
            log.debug("getHeaderInformation ::: get information from WorkCasePreScreen");
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findByAppNumber(appNumber);
            if(!Util.isNull(workCasePrescreen)){
                bdmUserId = workCasePrescreen.getCreateBy().getId();

                Prescreen prescreen = prescreenDAO.findByWorkCasePrescreen(workCasePrescreen);
                if(prescreen != null){
                    appHeaderView.setRefinance(prescreen.getRefinanceInValue() != null ? prescreen.getRefinanceInValue().getName() : "");
                    appHeaderView.setRefinanceOut(prescreen.getRefinanceOutValue() != null ? prescreen.getRefinanceOutValue().getName() : "");
                    appHeaderView.setLastDecisionDate(DateTimeUtil.convertToStringDDMMYYYY(prescreen.getLastDecisionDate()));
                }
                appHeaderView.setCaNo(workCasePrescreen.getCaNumber());
                appHeaderView.setAppNo(workCasePrescreen.getAppNumber());
                appHeaderView.setAppRefNo(workCasePrescreen.getRefAppNumber());
                appHeaderView.setAppRefDate(DateTimeUtil.convertToStringDDMMYYYY(workCasePrescreen.getRefAppDate()));
                appHeaderView.setProductGroup(workCasePrescreen.getProductGroup() != null ? workCasePrescreen.getProductGroup().getName() : "");
                appHeaderView.setCaseStatus(status != null ? status.getName() : "");
                appHeaderView.setRequestType(workCasePrescreen.getRequestType() != null ? workCasePrescreen.getRequestType().getName() : "");

                //customerList = customerDAO.getBorrowerByWorkCaseId(0, workCasePrescreen.getId());
                customerBasicViewList = customerDAO.getCustomerForHeader(workCasePrescreen.getId(), 0);
                log.debug("getHeaderInformation ::: get information from WorkCasePreScreen : customer list size : {}", customerBasicViewList.size());
            }
        }

        if(customerBasicViewList != null && customerBasicViewList.size() > 0){
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for(CustomerBasicView item : customerBasicViewList){
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                String borrowerName = "";
                borrowerName = item.getTitleTh();
                borrowerName = borrowerName.concat("").concat(item.getFirstNameTh());
                borrowerName = borrowerName.concat(" ").concat(item.getLastNameTh());

                appBorrowerHeaderView.setBorrowerName(borrowerName);

                if (item.getCustomerEntityId() == BorrowerType.INDIVIDUAL.value()) {
                    appBorrowerHeaderView.setPersonalId(item.getCitizenId());
                }else if(item.getCustomerEntityId() == BorrowerType.JURISTIC.value()){
                    appBorrowerHeaderView.setPersonalId(item.getRegistrationId());
                }
                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
        }

        /*if (customerList != null && customerList.size() > 0) {
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
        }*/

        if (!Util.isEmpty(bdmUserId)) {
            User bdmUser = userDAO.findById(bdmUserId);
            if (bdmUser != null) {
                appHeaderView.setBdmName(bdmUser.getUserName());
                appHeaderView.setBdmPhoneNumber(bdmUser.getPhoneNumber());
                appHeaderView.setBdmPhoneExtNumber(bdmUser.getPhoneExt());

                if(!Util.isNull(bdmUser.getTeam())){
                    appHeaderView.setBdmZoneName(bdmUser.getTeam().getTeam_name());
                    int zmTeamId = relTeamUserDetailsDAO.getTeamLeadHeadIdByTeamId(bdmUser.getTeam().getId());
                    if(!Util.isZero(zmTeamId)) {
                        int rgmTeamId = relTeamUserDetailsDAO.getTeamLeadHeadIdByTeamId(zmTeamId);
                        UserTeam userTeam = userTeamDAO.findById(rgmTeamId);

                        if(!Util.isNull(userTeam)){
                            appHeaderView.setBdmRegionName(userTeam.getTeam_name());
                        }
                    }
                }
            }
        }

        if (!Util.isEmpty(uwUserId)) {
            User uwUser = userDAO.findById(uwUserId);
            if (uwUser != null) {
                appHeaderView.setUwName(uwUser.getUserName());
                appHeaderView.setUwPhoneNumber(uwUser.getPhoneNumber());
                appHeaderView.setUwPhoneExtNumber(uwUser.getPhoneExt());
                appHeaderView.setUwTeamName(uwUser.getTeam().getTeam_name());
            }
        }

        return appHeaderView;
    }

    public AppHeaderView updateCustomerInfo(AppHeaderView appHeaderView, long workCasePreScreenId, long workCaseId){
        List<CustomerBasicView> customerBasicViewList = new ArrayList<CustomerBasicView>();

        if(!Util.isZero(workCasePreScreenId)){
            customerBasicViewList = customerDAO.getCustomerForHeader(workCasePreScreenId, 0);
        }else if(!Util.isZero(workCaseId)){
            customerBasicViewList = customerDAO.getCustomerForHeader(0, workCaseId);
        }

        if(customerBasicViewList != null && customerBasicViewList.size() > 0){
            List<AppBorrowerHeaderView> appBorrowerHeaderViewList = new ArrayList<AppBorrowerHeaderView>();
            for(CustomerBasicView item : customerBasicViewList){
                AppBorrowerHeaderView appBorrowerHeaderView = new AppBorrowerHeaderView();
                String borrowerName = "";
                borrowerName = item.getTitleTh();
                borrowerName = borrowerName.concat("").concat(item.getFirstNameTh());
                borrowerName = borrowerName.concat(" ").concat(item.getLastNameTh());

                appBorrowerHeaderView.setBorrowerName(borrowerName);

                if (item.getCustomerEntityId() == BorrowerType.INDIVIDUAL.value()) {
                    appBorrowerHeaderView.setPersonalId(item.getCitizenId());
                }else if(item.getCustomerEntityId() == BorrowerType.JURISTIC.value()){
                    appBorrowerHeaderView.setPersonalId(item.getRegistrationId());
                }
                appBorrowerHeaderViewList.add(appBorrowerHeaderView);
            }
            appHeaderView.setBorrowerHeaderViewList(appBorrowerHeaderViewList);
        }

        return appHeaderView;
    }

    public boolean getRequestAppraisalFlag(long workCaseId, long workCasePreScreenId){
        boolean requestAppraisal = false;
        if(workCaseId != 0){
            WorkCase workCase = workCaseDAO.findById(workCaseId);
            if(!Util.isNull(workCase)){
                requestAppraisal = Util.isTrue(workCase.getRequestAppraisal());
            }
        } else if (workCasePreScreenId != 0){
            WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            if(!Util.isNull(workCasePrescreen)){
                requestAppraisal = Util.isTrue(workCasePrescreen.getRequestAppraisal());
            }
        }

        return requestAppraisal;
    }

    public void updateNCBRejectFlag(long workCasePreScreenId, boolean canCheckPreScreen){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        if(canCheckPreScreen){
            workCasePrescreen.setNcbRejectFlag(0);
        } else {
            workCasePrescreen.setNcbRejectFlag(1);
        }
        workCasePrescreenDAO.persist(workCasePrescreen);
    }

    public void updateNCBRejectFlagFullApp(long workCaseId, boolean canCheckFullApp){
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(canCheckFullApp){
            workCase.setNcbRejectFlag(0);
        } else {
            workCase.setNcbRejectFlag(1);
        }
        workCaseDAO.persist(workCase);
    }
}
